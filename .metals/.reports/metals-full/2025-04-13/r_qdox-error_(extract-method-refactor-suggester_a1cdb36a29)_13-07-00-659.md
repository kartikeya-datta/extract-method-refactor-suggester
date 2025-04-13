error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10380.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10380.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10380.java
text:
```scala
I@@nternalSearchRequest internalRequest = new InternalSearchRequest(shardRouting, numberOfShards, request.searchType());

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

package org.elasticsearch.action.search.type;

import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.Base64;
import org.elasticsearch.common.Unicode;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.search.SearchPhaseResult;
import org.elasticsearch.search.internal.InternalScrollSearchRequest;
import org.elasticsearch.search.internal.InternalSearchRequest;

import java.io.IOException;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * @author kimchy (Shay Banon)
 */
public abstract class TransportSearchHelper {


    private final static Pattern scrollIdPattern;

    static {
        scrollIdPattern = Pattern.compile(";");
    }

    /**
     * Builds the shard failures, and releases the cache (meaning this should only be called once!).
     */
    public static ShardSearchFailure[] buildShardFailures(Collection<ShardSearchFailure> shardFailures, TransportSearchCache searchCache) {
        ShardSearchFailure[] ret;
        if (shardFailures.isEmpty()) {
            ret = ShardSearchFailure.EMPTY_ARRAY;
        } else {
            ret = shardFailures.toArray(ShardSearchFailure.EMPTY_ARRAY);
        }
        searchCache.releaseShardFailures(shardFailures);
        return ret;
    }

    public static InternalSearchRequest internalSearchRequest(ShardRouting shardRouting, int numberOfShards, SearchRequest request) {
        InternalSearchRequest internalRequest = new InternalSearchRequest(shardRouting, numberOfShards);
        internalRequest.source(request.source(), request.sourceOffset(), request.sourceLength());
        internalRequest.extraSource(request.extraSource(), request.extraSourceOffset(), request.extraSourceLength());
        internalRequest.scroll(request.scroll());
        internalRequest.timeout(request.timeout());
        internalRequest.types(request.types());
        return internalRequest;
    }

    public static InternalScrollSearchRequest internalScrollSearchRequest(long id, SearchScrollRequest request) {
        InternalScrollSearchRequest internalRequest = new InternalScrollSearchRequest(id);
        internalRequest.scroll(request.scroll());
        return internalRequest;
    }

    public static String buildScrollId(SearchType searchType, Iterable<? extends SearchPhaseResult> searchPhaseResults) throws IOException {
        if (searchType == SearchType.DFS_QUERY_THEN_FETCH || searchType == SearchType.QUERY_THEN_FETCH) {
            return buildScrollId(ParsedScrollId.QUERY_THEN_FETCH_TYPE, searchPhaseResults);
        } else if (searchType == SearchType.QUERY_AND_FETCH || searchType == SearchType.DFS_QUERY_AND_FETCH) {
            return buildScrollId(ParsedScrollId.QUERY_AND_FETCH_TYPE, searchPhaseResults);
        } else if (searchType == SearchType.SCAN) {
            return buildScrollId(ParsedScrollId.SCAN, searchPhaseResults);
        } else {
            throw new ElasticSearchIllegalStateException();
        }
    }

    public static String buildScrollId(String type, Iterable<? extends SearchPhaseResult> searchPhaseResults) throws IOException {
        StringBuilder sb = new StringBuilder().append(type).append(';');
        for (SearchPhaseResult searchPhaseResult : searchPhaseResults) {
            sb.append(searchPhaseResult.id()).append(':').append(searchPhaseResult.shardTarget().nodeId()).append(';');
        }
        return Base64.encodeBytes(Unicode.fromStringAsBytes(sb.toString()), Base64.URL_SAFE);
    }

    public static ParsedScrollId parseScrollId(String scrollId) {
        try {
            scrollId = Unicode.fromBytes(Base64.decode(scrollId, Base64.URL_SAFE));
        } catch (IOException e) {
            throw new ElasticSearchIllegalArgumentException("Failed to decode scrollId", e);
        }
        String[] elements = scrollIdPattern.split(scrollId);
        @SuppressWarnings({"unchecked"}) Tuple<String, Long>[] values = new Tuple[elements.length - 1];
        for (int i = 1; i < elements.length; i++) {
            String element = elements[i];
            int index = element.indexOf(':');
            values[i - 1] = new Tuple<String, Long>(element.substring(index + 1), Long.parseLong(element.substring(0, index)));
        }
        return new ParsedScrollId(scrollId, elements[0], values);
    }

    private TransportSearchHelper() {

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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10380.java