error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8573.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8573.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8573.java
text:
```scala
@Override public A@@nalyzeRequestBuilder prepareAnalyze(String index, String text) {

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

package org.elasticsearch.client.support;

import org.elasticsearch.client.action.admin.indices.alias.IndicesAliasesRequestBuilder;
import org.elasticsearch.client.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.client.action.admin.indices.cache.clear.ClearIndicesCacheRequestBuilder;
import org.elasticsearch.client.action.admin.indices.close.CloseIndexRequestBuilder;
import org.elasticsearch.client.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.client.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.client.action.admin.indices.flush.FlushRequestBuilder;
import org.elasticsearch.client.action.admin.indices.gateway.snapshot.GatewaySnapshotRequestBuilder;
import org.elasticsearch.client.action.admin.indices.mapping.delete.DeleteMappingRequestBuilder;
import org.elasticsearch.client.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.client.action.admin.indices.open.OpenIndexRequestBuilder;
import org.elasticsearch.client.action.admin.indices.optimize.OptimizeRequestBuilder;
import org.elasticsearch.client.action.admin.indices.refresh.RefreshRequestBuilder;
import org.elasticsearch.client.action.admin.indices.settings.UpdateSettingsRequestBuilder;
import org.elasticsearch.client.action.admin.indices.status.IndicesStatusRequestBuilder;
import org.elasticsearch.client.action.admin.indices.template.delete.DeleteIndexTemplateRequestBuilder;
import org.elasticsearch.client.action.admin.indices.template.put.PutIndexTemplateRequestBuilder;
import org.elasticsearch.client.internal.InternalIndicesAdminClient;

/**
 * @author kimchy (shay.banon)
 */
public abstract class AbstractIndicesAdminClient implements InternalIndicesAdminClient {

    @Override public IndicesAliasesRequestBuilder prepareAliases() {
        return new IndicesAliasesRequestBuilder(this);
    }

    @Override public ClearIndicesCacheRequestBuilder prepareClearCache(String... indices) {
        return new ClearIndicesCacheRequestBuilder(this).setIndices(indices);
    }

    @Override public CreateIndexRequestBuilder prepareCreate(String index) {
        return new CreateIndexRequestBuilder(this, index);
    }

    @Override public DeleteIndexRequestBuilder prepareDelete(String index) {
        return new DeleteIndexRequestBuilder(this, index);
    }

    @Override public CloseIndexRequestBuilder prepareClose(String index) {
        return new CloseIndexRequestBuilder(this, index);
    }

    @Override public OpenIndexRequestBuilder prepareOpen(String index) {
        return new OpenIndexRequestBuilder(this, index);
    }

    @Override public FlushRequestBuilder prepareFlush(String... indices) {
        return new FlushRequestBuilder(this).setIndices(indices);
    }

    @Override public GatewaySnapshotRequestBuilder prepareGatewaySnapshot(String... indices) {
        return new GatewaySnapshotRequestBuilder(this).setIndices(indices);
    }

    @Override public PutMappingRequestBuilder preparePutMapping(String... indices) {
        return new PutMappingRequestBuilder(this).setIndices(indices);
    }

    @Override public DeleteMappingRequestBuilder prepareDeleteMapping(String... indices) {
        return new DeleteMappingRequestBuilder(this).setIndices(indices);
    }

    @Override public OptimizeRequestBuilder prepareOptimize(String... indices) {
        return new OptimizeRequestBuilder(this).setIndices(indices);
    }

    @Override public RefreshRequestBuilder prepareRefresh(String... indices) {
        return new RefreshRequestBuilder(this).setIndices(indices);
    }

    @Override public IndicesStatusRequestBuilder prepareStatus(String... indices) {
        return new IndicesStatusRequestBuilder(this).setIndices(indices);
    }

    @Override public UpdateSettingsRequestBuilder prepareUpdateSettings(String... indices) {
        return new UpdateSettingsRequestBuilder(this).setIndices(indices);
    }

    @Override public AnalyzeRequestBuilder prepareAnalyzer(String index, String text) {
        return new AnalyzeRequestBuilder(this, index, text);
    }

    @Override public PutIndexTemplateRequestBuilder preparePutTemplate(String name) {
        return new PutIndexTemplateRequestBuilder(this, name);
    }

    @Override public DeleteIndexTemplateRequestBuilder prepareDeleteTemplate(String name) {
        return new DeleteIndexTemplateRequestBuilder(this, name);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8573.java