error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7557.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7557.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7557.java
text:
```scala
t@@ypeName = XContentMapValues.nodeStringValue(indexSettings.get("type"), "status");

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.indexer.twitter;

import org.elasticsearch.ExceptionsHelper;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.action.bulk.BulkRequestBuilder;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.indexer.AbstractIndexerComponent;
import org.elasticsearch.indexer.Indexer;
import org.elasticsearch.indexer.IndexerName;
import org.elasticsearch.indexer.IndexerSettings;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import twitter4j.*;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kimchy (shay.banon)
 */
public class TwitterIndexer extends AbstractIndexerComponent implements Indexer {

    private final Client client;

    private final String indexName;

    private final String typeName;

    private final int bulkSize;

    private final int dropThreshold;


    private final TwitterStream stream;

    private final AtomicInteger onGoingBulks = new AtomicInteger();

    private volatile BulkRequestBuilder currentRequest;

    @Inject public TwitterIndexer(IndexerName indexerName, IndexerSettings settings, Client client) {
        super(indexerName, settings);
        this.client = client;

        String user = XContentMapValues.nodeStringValue(settings.settings().get("user"), null);
        String password = XContentMapValues.nodeStringValue(settings.settings().get("password"), null);

        logger.info("creating twitter stream indexer for [{}]", user);

        this.bulkSize = XContentMapValues.nodeIntegerValue(settings.settings().get("bulk_size"), 100);
        this.dropThreshold = XContentMapValues.nodeIntegerValue(settings.settings().get("drop_threshold"), 10);

        if (user == null || password == null) {
            stream = null;
            indexName = null;
            typeName = null;
            logger.warn("no user / password specified, disabling indexer...");
            return;
        }

        if (settings.settings().containsKey("index")) {
            Map<String, Object> indexSettings = (Map<String, Object>) settings.settings().get("index");
            indexName = XContentMapValues.nodeStringValue(indexSettings.get("index"), indexerName.name());
            typeName = XContentMapValues.nodeStringValue(indexSettings.get("type"), indexerName.name());
        } else {
            indexName = indexerName.name();
            typeName = "status";
        }

        stream = new TwitterStreamFactory(new StatusHandler()).getInstance(user, password);
    }

    @Override public void start() {
        logger.info("starting twitter stream");
        try {
            client.admin().indices().prepareCreate(indexName).execute().actionGet();
            currentRequest = client.prepareBulk();
            stream.sample();
        } catch (Exception e) {
            if (ExceptionsHelper.unwrapCause(e) instanceof IndexAlreadyExistsException) {
                // that's fine
            } else {
                logger.warn("failed to create index [{}], disabling indexer...", e, indexName);
            }
        }
    }

    @Override public void close() {
        logger.info("closing twitter stream indexer");
        if (stream != null) {
            stream.cleanup();
            stream.shutdown();
        }
    }

    private class StatusHandler extends StatusAdapter {

        @Override public void onStatus(Status status) {
            if (logger.isTraceEnabled()) {
                logger.trace("status {} : {}", status.getUser().getName(), status.getText());
            }
            try {
                XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
                builder.field("text", status.getText());
                builder.field("created_at", status.getCreatedAt());
                builder.field("source", status.getSource());
                builder.field("truncated", status.isTruncated());


                if (status.getUserMentions() != null) {
                    builder.startArray("mention");
                    for (User user : status.getUserMentions()) {
                        builder.startObject();
                        builder.field("id", user.getId());
                        builder.field("name", user.getName());
                        builder.field("screen_name", user.getScreenName());
                        builder.endObject();
                    }
                    builder.endArray();
                }

                if (status.getRetweetCount() != -1) {
                    builder.field("retweet_count", status.getRetweetCount());
                }

                if (status.getInReplyToStatusId() != -1) {
                    builder.startObject("in_reply");
                    builder.field("status", status.getInReplyToStatusId());
                    if (status.getInReplyToUserId() != -1) {
                        builder.field("user_id", status.getInReplyToUserId());
                        builder.field("user_screen_name", status.getInReplyToScreenName());
                    }
                    builder.endObject();
                }

                if (status.getHashtags() != null) {
                    builder.array("hashtag", status.getHashtags());
                }
                if (status.getContributors() != null) {
                    builder.array("contributor", status.getContributors());
                }
                if (status.getGeoLocation() != null) {
                    builder.startObject("location");
                    builder.field("lat", status.getGeoLocation().getLatitude());
                    builder.field("lon", status.getGeoLocation().getLongitude());
                    builder.endObject();
                }
                if (status.getPlace() != null) {
                    builder.startObject("place");
                    builder.field("id", status.getPlace().getId());
                    builder.field("name", status.getPlace().getName());
                    builder.field("type", status.getPlace().getPlaceType());
                    builder.field("full_name", status.getPlace().getFullName());
                    builder.field("street_address", status.getPlace().getStreetAddress());
                    builder.field("country", status.getPlace().getCountry());
                    builder.field("country_code", status.getPlace().getCountryCode());
                    builder.field("url", status.getPlace().getURL());
                    builder.endObject();
                }
                if (status.getURLs() != null) {
                    builder.startArray("link");
                    for (URL url : status.getURLs()) {
                        if (url != null) {
                            builder.value(url.toExternalForm());
                        }
                    }
                    builder.endArray();
                }
                if (status.getAnnotations() != null) {
                    builder.startObject("annotation");
                    for (Annotation ann : status.getAnnotations().getAnnotations()) {
                        builder.startObject(ann.getType());
                        for (Map.Entry<String, String> entry : ann.getAttributes().entrySet()) {
                            builder.field(entry.getKey(), entry.getValue());
                        }
                        builder.endObject();
                    }
                    builder.endObject();
                }

                builder.startObject("user");
                builder.field("id", status.getUser().getId());
                builder.field("name", status.getUser().getName());
                builder.field("screen_name", status.getUser().getScreenName());
                builder.field("location", status.getUser().getLocation());
                builder.field("description", status.getUser().getDescription());
                builder.endObject();

                builder.endObject();
                currentRequest.add(Requests.indexRequest(indexName).type(typeName).id(Long.toString(status.getId())).create(true).source(builder));
                processBulkIfNeeded();
            } catch (Exception e) {
                logger.warn("failed to construct index request", e);
            }
        }

        @Override public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            if (statusDeletionNotice.getStatusId() != -1) {
                currentRequest.add(Requests.deleteRequest(indexName).type(typeName).id(Long.toString(statusDeletionNotice.getStatusId())));
                processBulkIfNeeded();
            }
        }

        @Override public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        }

        @Override public void onException(Exception ex) {
            logger.warn("stream failure", ex);
        }

        private void processBulkIfNeeded() {
            if (currentRequest.numberOfActions() >= bulkSize) {
                // execute the bulk operation
                int currentOnGoingBulks = onGoingBulks.incrementAndGet();
                if (currentOnGoingBulks > dropThreshold) {
                    onGoingBulks.decrementAndGet();
                    logger.warn("dropping bulk, [{}] crossed threshold [{}]", onGoingBulks, dropThreshold);
                } else {
                    try {
                        currentRequest.execute(new ActionListener<BulkResponse>() {
                            @Override public void onResponse(BulkResponse bulkResponse) {
                                onGoingBulks.decrementAndGet();
                            }

                            @Override public void onFailure(Throwable e) {
                                logger.warn("failed to execute bulk");
                            }
                        });
                    } catch (Exception e) {
                        logger.warn("failed to process bulk", e);
                    }
                }
                currentRequest = client.prepareBulk();
            }
        }
    }
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7557.java