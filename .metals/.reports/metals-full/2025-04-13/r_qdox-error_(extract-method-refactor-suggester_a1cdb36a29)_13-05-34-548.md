error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7227.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7227.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7227.java
text:
```scala
c@@ontext[i] = new Tuple<>(element.substring(sep + 1), Long.parseLong(element.substring(0, sep)));

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.UnicodeUtil;
import org.elasticsearch.ElasticsearchIllegalArgumentException;
import org.elasticsearch.ElasticsearchIllegalStateException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.Base64;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.common.util.concurrent.AtomicArray;
import org.elasticsearch.search.SearchPhaseResult;
import org.elasticsearch.search.internal.InternalScrollSearchRequest;
import org.elasticsearch.search.internal.ShardSearchRequest;

import java.io.IOException;
import java.util.Map;

/**
 *
 */
public abstract class TransportSearchHelper {

    public static ShardSearchRequest internalSearchRequest(ShardRouting shardRouting, int numberOfShards, SearchRequest request, String[] filteringAliases, long nowInMillis, boolean useSlowScroll) {
        ShardSearchRequest shardRequest = new ShardSearchRequest(request, shardRouting, numberOfShards, useSlowScroll);
        shardRequest.filteringAliases(filteringAliases);
        shardRequest.nowInMillis(nowInMillis);
        return shardRequest;
    }

    public static InternalScrollSearchRequest internalScrollSearchRequest(long id, SearchScrollRequest request) {
        return new InternalScrollSearchRequest(request, id);
    }

    public static String buildScrollId(SearchType searchType, AtomicArray<? extends SearchPhaseResult> searchPhaseResults, @Nullable Map<String, String> attributes) throws IOException {
        if (searchType == SearchType.DFS_QUERY_THEN_FETCH || searchType == SearchType.QUERY_THEN_FETCH) {
            return buildScrollId(ParsedScrollId.QUERY_THEN_FETCH_TYPE, searchPhaseResults, attributes);
        } else if (searchType == SearchType.QUERY_AND_FETCH || searchType == SearchType.DFS_QUERY_AND_FETCH) {
            return buildScrollId(ParsedScrollId.QUERY_AND_FETCH_TYPE, searchPhaseResults, attributes);
        } else if (searchType == SearchType.SCAN) {
            return buildScrollId(ParsedScrollId.SCAN, searchPhaseResults, attributes);
        } else {
            throw new ElasticsearchIllegalStateException();
        }
    }

    public static String buildScrollId(String type, AtomicArray<? extends SearchPhaseResult> searchPhaseResults, @Nullable Map<String, String> attributes) throws IOException {
        StringBuilder sb = new StringBuilder().append(type).append(';');
        sb.append(searchPhaseResults.asList().size()).append(';');
        for (AtomicArray.Entry<? extends SearchPhaseResult> entry : searchPhaseResults.asList()) {
            SearchPhaseResult searchPhaseResult = entry.value;
            sb.append(searchPhaseResult.id()).append(':').append(searchPhaseResult.shardTarget().nodeId()).append(';');
        }
        if (attributes == null) {
            sb.append("0;");
        } else {
            sb.append(attributes.size()).append(";");
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                sb.append(entry.getKey()).append(':').append(entry.getValue()).append(';');
            }
        }
        BytesRef bytesRef = new BytesRef();
        UnicodeUtil.UTF16toUTF8(sb, 0, sb.length(), bytesRef);

        return Base64.encodeBytes(bytesRef.bytes, bytesRef.offset, bytesRef.length, Base64.URL_SAFE);
    }

    public static ParsedScrollId parseScrollId(String scrollId) {
        CharsRef spare = new CharsRef();
        try {
            byte[] decode = Base64.decode(scrollId, Base64.URL_SAFE);
            UnicodeUtil.UTF8toUTF16(decode, 0, decode.length, spare);
        } catch (IOException e) {
            throw new ElasticsearchIllegalArgumentException("Failed to decode scrollId", e);
        }
        String[] elements = Strings.splitStringToArray(spare, ';');
        int index = 0;
        String type = elements[index++];
        int contextSize = Integer.parseInt(elements[index++]);
        @SuppressWarnings({"unchecked"}) Tuple<String, Long>[] context = new Tuple[contextSize];
        for (int i = 0; i < contextSize; i++) {
            String element = elements[index++];
            int sep = element.indexOf(':');
            if (sep == -1) {
                throw new ElasticsearchIllegalArgumentException("Malformed scrollId [" + scrollId + "]");
            }
            context[i] = new Tuple<String, Long>(element.substring(sep + 1), Long.parseLong(element.substring(0, sep)));
        }
        Map<String, String> attributes;
        int attributesSize = Integer.parseInt(elements[index++]);
        if (attributesSize == 0) {
            attributes = ImmutableMap.of();
        } else {
            attributes = Maps.newHashMapWithExpectedSize(attributesSize);
            for (int i = 0; i < attributesSize; i++) {
                String element = elements[index++];
                int sep = element.indexOf(':');
                attributes.put(element.substring(0, sep), element.substring(sep + 1));
            }
        }
        return new ParsedScrollId(scrollId, type, context, attributes);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7227.java