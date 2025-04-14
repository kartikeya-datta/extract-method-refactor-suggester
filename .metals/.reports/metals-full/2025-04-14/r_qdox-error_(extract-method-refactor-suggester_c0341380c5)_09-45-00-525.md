error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6754.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6754.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6754.java
text:
```scala
s@@ourceBuilder().setTrackScores(score);

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.action.percolate;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.support.broadcast.BroadcastOperationRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.internal.InternalClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;

import java.util.Map;

/**
 *
 */
public class PercolateRequestBuilder extends BroadcastOperationRequestBuilder<PercolateRequest, PercolateResponse, PercolateRequestBuilder> {

    private PercolateSourceBuilder sourceBuilder;

    public PercolateRequestBuilder(Client client) {
        super((InternalClient) client, new PercolateRequest());
    }

    /**
     * Sets the type of the document to percolate.
     */
    public PercolateRequestBuilder setDocumentType(String type) {
        request.documentType(type);
        return this;
    }

    /**
     * A comma separated list of routing values to control the shards the search will be executed on.
     */
    public PercolateRequestBuilder setRouting(String routing) {
        request.routing(routing);
        return this;
    }

    /**
     * List of routing values to control the shards the search will be executed on.
     */
    public PercolateRequestBuilder setRouting(String... routings) {
        request.routing(Strings.arrayToCommaDelimitedString(routings));
        return this;
    }

    /**
     * Sets the preference to execute the search. Defaults to randomize across shards. Can be set to
     * <tt>_local</tt> to prefer local shards, <tt>_primary</tt> to execute only on primary shards, or
     * a custom value, which guarantees that the same order will be used across different requests.
     */
    public PercolateRequestBuilder setPreference(String preference) {
        request.preference(preference);
        return this;
    }

    /**
     * Enables percolating an existing document. Instead of specifying the source of the document to percolate, define
     * a get request that will fetch a document and use its source.
     */
    public PercolateRequestBuilder setGetRequest(GetRequest getRequest) {
        request.getRequest(getRequest);
        return this;
    }

    /**
     * Whether only to return total count and don't keep track of the matches (Count percolation).
     */
    public PercolateRequestBuilder setOnlyCount(boolean onlyCount) {
        request.onlyCount(onlyCount);
        return this;
    }

    /**
     * Limits the maximum number of percolate query matches to be returned.
     */
    public PercolateRequestBuilder setSize(int size) {
        sourceBuilder().setSize(size);
        return this;
    }

    /**
     * Similar as {@link #setScore(boolean)}, but also sort by the score.
     */
    public PercolateRequestBuilder setSort(boolean sort) {
        sourceBuilder().setSort(sort);
        return this;
    }

    /**
     * Whether to compute a score for each match and include it in the response. The score is based on
     * {@link #setPercolateQuery(QueryBuilder)}}.
     */
    public PercolateRequestBuilder setScore(boolean score) {
        sourceBuilder().setScore(score);
        return this;
    }

    /**
     * Sets a query to reduce the number of percolate queries to be evaluated and score the queries that match based
     * on this query.
     */
    public PercolateRequestBuilder setPercolateDoc(PercolateSourceBuilder.DocBuilder docBuilder) {
        sourceBuilder().setDoc(docBuilder);
        return this;
    }

    /**
     * Sets a query to reduce the number of percolate queries to be evaluated and score the queries that match based
     * on this query.
     */
    public PercolateRequestBuilder setPercolateQuery(QueryBuilder queryBuilder) {
        sourceBuilder().setQueryBuilder(queryBuilder);
        return this;
    }

    /**
     * Sets a filter to reduce the number of percolate queries to be evaluated.
     */
    public PercolateRequestBuilder setPercolateFilter(FilterBuilder filterBuilder) {
        sourceBuilder().setFilterBuilder(filterBuilder);
        return this;
    }

    /**
     * Enables highlighting for the percolate document. Per matched percolate query highlight the percolate document.
     */
    public PercolateRequestBuilder setHighlightBuilder(HighlightBuilder highlightBuilder) {
        sourceBuilder().setHighlightBuilder(highlightBuilder);
        return this;
    }

    /**
     * Add a facet definition.
     */
    public PercolateRequestBuilder addFacet(FacetBuilder facetBuilder) {
        sourceBuilder().addFacet(facetBuilder);
        return this;
    }

    /**
     * Add a aggregation definition.
     */
    public PercolateRequestBuilder addAggregation(AggregationBuilder aggregationBuilder) {
        sourceBuilder().addAggregation(aggregationBuilder);
        return this;
    }

    /**
     * Sets the raw percolate request body.
     */
    public PercolateRequestBuilder setSource(PercolateSourceBuilder source) {
        sourceBuilder = source;
        return this;
    }

    public PercolateRequestBuilder setSource(Map<String, Object> source) {
        request.source(source);
        return this;
    }

    public PercolateRequestBuilder setSource(Map<String, Object> source, XContentType contentType) {
        request.source(source, contentType);
        return this;
    }

    public PercolateRequestBuilder setSource(String source) {
        request.source(source);
        return this;
    }

    public PercolateRequestBuilder setSource(XContentBuilder sourceBuilder) {
        request.source(sourceBuilder);
        return this;
    }

    public PercolateRequestBuilder setSource(BytesReference source) {
        request.source(source, false);
        return this;
    }

    public PercolateRequestBuilder setSource(BytesReference source, boolean unsafe) {
        request.source(source, unsafe);
        return this;
    }

    public PercolateRequestBuilder setSource(byte[] source) {
        request.source(source);
        return this;
    }

    public PercolateRequestBuilder setSource(byte[] source, int offset, int length) {
        request.source(source, offset, length);
        return this;
    }

    public PercolateRequestBuilder setSource(byte[] source, int offset, int length, boolean unsafe) {
        request.source(source, offset, length, unsafe);
        return this;
    }

    private PercolateSourceBuilder sourceBuilder() {
        if (sourceBuilder == null) {
            sourceBuilder = new PercolateSourceBuilder();
        }
        return sourceBuilder;
    }

    @Override
    public PercolateRequest request() {
        if (sourceBuilder != null) {
            request.source(sourceBuilder);
        }
        return request;
    }

    @Override
    protected void doExecute(ActionListener<PercolateResponse> listener) {
        if (sourceBuilder != null) {
            request.source(sourceBuilder);
        }
        ((Client) client).percolate(request, listener);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6754.java