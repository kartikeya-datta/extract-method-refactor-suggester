error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2845.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2845.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2845.java
text:
```scala
s@@earchRequest.source(request.searchSource(), request.searchSourceOffset(), request.searchSourceLength(), request.searchSourceUnsafe());

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

package org.elasticsearch.action.mlt;

import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.Term;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.TransportActions;
import org.elasticsearch.action.get.GetField;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.TransportGetAction;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.TransportSearchAction;
import org.elasticsearch.action.support.BaseAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.index.mapper.*;
import org.elasticsearch.index.query.xcontent.BoolQueryBuilder;
import org.elasticsearch.index.query.xcontent.MoreLikeThisFieldQueryBuilder;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.transport.BaseTransportRequestHandler;
import org.elasticsearch.transport.TransportChannel;
import org.elasticsearch.transport.TransportService;
import org.elasticsearch.util.inject.Inject;
import org.elasticsearch.util.settings.Settings;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import static org.elasticsearch.client.Requests.*;
import static org.elasticsearch.index.query.xcontent.QueryBuilders.*;
import static org.elasticsearch.search.builder.SearchSourceBuilder.*;
import static org.elasticsearch.util.collect.Sets.*;

/**
 * The more like this action.
 *
 * @author kimchy (shay.banon)
 */
public class TransportMoreLikeThisAction extends BaseAction<MoreLikeThisRequest, SearchResponse> {

    private final TransportSearchAction searchAction;

    private final TransportGetAction getAction;

    private final IndicesService indicesService;

    private final ClusterService clusterService;

    @Inject public TransportMoreLikeThisAction(Settings settings, TransportSearchAction searchAction, TransportGetAction getAction,
                                               ClusterService clusterService, IndicesService indicesService, TransportService transportService) {
        super(settings);
        this.searchAction = searchAction;
        this.getAction = getAction;
        this.indicesService = indicesService;
        this.clusterService = clusterService;

        transportService.registerHandler(TransportActions.MORE_LIKE_THIS, new TransportHandler());
    }

    @Override protected void doExecute(final MoreLikeThisRequest request, final ActionListener<SearchResponse> listener) {
        // update to actual index name
        ClusterState clusterState = clusterService.state();
        // update to the concrete index
        request.index(clusterState.metaData().concreteIndex(request.index()));

        Set<String> getFields = newHashSet();
        if (request.fields() != null) {
            Collections.addAll(getFields, request.fields());
        }
        // add the source, in case we need to parse it to get fields
        getFields.add(SourceFieldMapper.NAME);

        GetRequest getRequest = getRequest(request.index())
                .fields(getFields.toArray(new String[getFields.size()]))
                .type(request.type())
                .id(request.id())
                .listenerThreaded(false)
                .operationThreaded(true);

        request.beforeLocalFork();
        getAction.execute(getRequest, new ActionListener<GetResponse>() {
            @Override public void onResponse(GetResponse getResponse) {
                if (!getResponse.exists()) {
                    listener.onFailure(new ElasticSearchException("document missing"));
                    return;
                }
                final BoolQueryBuilder boolBuilder = boolQuery();
                try {
                    DocumentMapper docMapper = indicesService.indexServiceSafe(request.index()).mapperService().documentMapper(request.type());
                    final Set<String> fields = newHashSet();
                    if (request.fields() != null) {
                        for (String field : request.fields()) {
                            FieldMappers fieldMappers = docMapper.mappers().smartName(field);
                            if (fieldMappers != null) {
                                fields.add(fieldMappers.mapper().names().indexName());
                            } else {
                                fields.add(field);
                            }
                        }
                    }

                    if (!fields.isEmpty()) {
                        // if fields are not empty, see if we got them in the response
                        for (Iterator<String> it = fields.iterator(); it.hasNext();) {
                            String field = it.next();
                            GetField getField = getResponse.field(field);
                            if (getField != null) {
                                for (Object value : getField.values()) {
                                    addMoreLikeThis(request, boolBuilder, getField.name(), value.toString());
                                }
                                it.remove();
                            }
                        }
                        if (!fields.isEmpty()) {
                            // if we don't get all the fields in the get response, see if we can parse the source
                            parseSource(getResponse, boolBuilder, docMapper, fields, request);
                        }
                    } else {
                        // we did not ask for any fields, try and get it from the source
                        parseSource(getResponse, boolBuilder, docMapper, fields, request);
                    }

                    if (boolBuilder.clauses().isEmpty()) {
                        // no field added, fail
                        listener.onFailure(new ElasticSearchException("No fields found to fetch the 'likeText' from"));
                        return;
                    }

                    // exclude myself
                    Term uidTerm = docMapper.uidMapper().term(request.type(), request.id());
                    boolBuilder.mustNot(termQuery(uidTerm.field(), uidTerm.text()));
                } catch (Exception e) {
                    listener.onFailure(e);
                    return;
                }

                String[] searchIndices = request.searchIndices();
                if (searchIndices == null) {
                    searchIndices = new String[]{request.index()};
                }
                String[] searchTypes = request.searchTypes();
                if (searchTypes == null) {
                    searchTypes = new String[]{request.type()};
                }

                SearchRequest searchRequest = searchRequest(searchIndices)
                        .types(searchTypes)
                        .searchType(request.searchType())
                        .scroll(request.searchScroll())
                        .extraSource(searchSource()
                                .query(boolBuilder)
                        )
                        .listenerThreaded(request.listenerThreaded());
                if (request.searchSource() != null) {
                    searchRequest.source(request.searchSource(), request.searchSourceOffset(), request.searchSourceLength());
                }
                searchAction.execute(searchRequest, new ActionListener<SearchResponse>() {
                    @Override public void onResponse(SearchResponse response) {
                        listener.onResponse(response);
                    }

                    @Override public void onFailure(Throwable e) {
                        listener.onFailure(e);
                    }
                });

            }

            @Override public void onFailure(Throwable e) {
                listener.onFailure(e);
            }
        });
    }

    private void parseSource(GetResponse getResponse, final BoolQueryBuilder boolBuilder, DocumentMapper docMapper, final Set<String> fields, final MoreLikeThisRequest request) {
        if (getResponse.source() == null) {
            return;
        }
        docMapper.parse(request.type(), request.id(), getResponse.source(), new DocumentMapper.ParseListenerAdapter() {
            @Override public boolean beforeFieldAdded(FieldMapper fieldMapper, Fieldable field, Object parseContext) {
                if (fieldMapper instanceof InternalMapper) {
                    return true;
                }
                String value = fieldMapper.valueAsString(field);
                if (value == null) {
                    return false;
                }

                if (fields.isEmpty() || fields.contains(field.name())) {
                    addMoreLikeThis(request, boolBuilder, fieldMapper, field);
                }

                return false;
            }
        });
    }

    private void addMoreLikeThis(MoreLikeThisRequest request, BoolQueryBuilder boolBuilder, FieldMapper fieldMapper, Fieldable field) {
        addMoreLikeThis(request, boolBuilder, field.name(), fieldMapper.valueAsString(field));
    }

    private void addMoreLikeThis(MoreLikeThisRequest request, BoolQueryBuilder boolBuilder, String fieldName, String likeText) {
        MoreLikeThisFieldQueryBuilder mlt = moreLikeThisFieldQuery(fieldName)
                .likeText(likeText)
                .percentTermsToMatch(request.percentTermsToMatch())
                .boostTerms(request.boostTerms())
                .minDocFreq(request.minDocFreq())
                .maxDocFreq(request.maxDocFreq())
                .minWordLen(request.minWordLen())
                .maxWordLen(request.maxWordLen())
                .minTermFreq(request.minTermFreq())
                .maxQueryTerms(request.maxQueryTerms())
                .stopWords(request.stopWords());
        boolBuilder.should(mlt);
    }

    private class TransportHandler extends BaseTransportRequestHandler<MoreLikeThisRequest> {

        @Override public MoreLikeThisRequest newInstance() {
            return new MoreLikeThisRequest();
        }

        @Override public void messageReceived(MoreLikeThisRequest request, final TransportChannel channel) throws Exception {
            // no need to have a threaded listener since we just send back a response
            request.listenerThreaded(false);
            execute(request, new ActionListener<SearchResponse>() {
                @Override public void onResponse(SearchResponse result) {
                    try {
                        channel.sendResponse(result);
                    } catch (Exception e) {
                        onFailure(e);
                    }
                }

                @Override public void onFailure(Throwable e) {
                    try {
                        channel.sendResponse(e);
                    } catch (Exception e1) {
                        logger.warn("Failed to send response for get", e1);
                    }
                }
            });
        }

        @Override public boolean spawn() {
            return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2845.java