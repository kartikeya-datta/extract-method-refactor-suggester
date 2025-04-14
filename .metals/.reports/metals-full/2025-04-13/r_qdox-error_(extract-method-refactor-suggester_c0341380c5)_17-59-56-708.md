error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/579.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/579.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/579.java
text:
```scala
r@@eturn queryBuilder.buildAsString();

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

package org.elasticsearch.rest.action.support;

import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.action.ShardOperationFailedException;
import org.elasticsearch.action.support.broadcast.BroadcastOperationResponse;
import org.elasticsearch.index.query.json.JsonQueryBuilders;
import org.elasticsearch.index.query.json.QueryStringJsonQueryBuilder;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.util.Strings;
import org.elasticsearch.util.json.JsonBuilder;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author kimchy (Shay Banon)
 */
public class RestActions {

    public final static Pattern indicesPattern;
    public final static Pattern typesPattern;
    private final static Pattern nodesIdsPattern;


    static {
        indicesPattern = Pattern.compile(",");
        typesPattern = Pattern.compile(",");
        nodesIdsPattern = Pattern.compile(",");
    }

    public static void buildBroadcastShardsHeader(JsonBuilder builder, BroadcastOperationResponse response) throws IOException {
        builder.startObject("_shards");
        builder.field("total", response.totalShards());
        builder.field("successful", response.successfulShards());
        builder.field("failed", response.failedShards());
        if (!response.shardFailures().isEmpty()) {
            builder.startArray("failures");
            for (ShardOperationFailedException shardFailure : response.shardFailures()) {
                builder.startObject();
                if (shardFailure.index() != null) {
                    builder.field("index", shardFailure.index());
                }
                if (shardFailure.shardId() != -1) {
                    builder.field("shardId", shardFailure.shardId());
                }
                builder.field("reason", shardFailure.reason());
                builder.endObject();
            }
            builder.endArray();
        }
        builder.endObject();
    }

    public static String parseQuerySource(RestRequest request) {
        if (request.hasContent()) {
            return request.contentAsString();
        }
        String queryString = request.param("q");
        if (queryString == null) {
            throw new ElasticSearchIllegalArgumentException("No query to execute, not in body, and not bounded to 'q' parameter");
        }
        QueryStringJsonQueryBuilder queryBuilder = JsonQueryBuilders.queryString(queryString);
        queryBuilder.defaultField(request.param("df"));
        queryBuilder.analyzer(request.param("analyzer"));
        String defaultOperator = request.param("defaultOperator");
        if (defaultOperator != null) {
            if ("OR".equals(defaultOperator)) {
                queryBuilder.defualtOperator(QueryStringJsonQueryBuilder.Operator.OR);
            } else if ("AND".equals(defaultOperator)) {
                queryBuilder.defualtOperator(QueryStringJsonQueryBuilder.Operator.AND);
            } else {
                throw new ElasticSearchIllegalArgumentException("Unsupported defaultOperator [" + defaultOperator + "], can either be [OR] or [AND]");
            }
        }
        return queryBuilder.build();
    }

    public static String[] splitIndices(String indices) {
        if (indices == null) {
            return Strings.EMPTY_ARRAY;
        }
        return indicesPattern.split(indices);
    }

    public static String[] splitTypes(String typeNames) {
        return typesPattern.split(typeNames);
    }

    public static String[] splitNodes(String nodes) {
        if (nodes == null) {
            return Strings.EMPTY_ARRAY;
        }
        return nodesIdsPattern.split(nodes);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/579.java