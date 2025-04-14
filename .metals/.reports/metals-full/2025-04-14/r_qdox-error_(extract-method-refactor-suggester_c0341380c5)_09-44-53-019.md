error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4132.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4132.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4132.java
text:
```scala
S@@tore store = new RamStore(shardId, settings, null);

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

package org.elasticsearch.index.shard;

import org.elasticsearch.common.Unicode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.analysis.AnalysisService;
import org.elasticsearch.index.cache.IndexCache;
import org.elasticsearch.index.deletionpolicy.KeepOnlyLastDeletionPolicy;
import org.elasticsearch.index.deletionpolicy.SnapshotDeletionPolicy;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.engine.robin.RobinEngine;
import org.elasticsearch.index.engine.robin.RobinIndexEngine;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.merge.policy.LogByteSizeMergePolicyProvider;
import org.elasticsearch.index.merge.scheduler.SerialMergeSchedulerProvider;
import org.elasticsearch.index.query.IndexQueryParserService;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.index.shard.service.InternalIndexShard;
import org.elasticsearch.index.similarity.SimilarityService;
import org.elasticsearch.index.store.Store;
import org.elasticsearch.index.store.ram.RamStore;
import org.elasticsearch.index.translog.memory.MemoryTranslog;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.threadpool.scaling.ScalingThreadPool;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.elasticsearch.common.settings.ImmutableSettings.Builder.*;
import static org.elasticsearch.index.query.xcontent.QueryBuilders.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author kimchy
 */
public class SimpleIndexShardTests {

    private ThreadPool threadPool;

    private IndexShard indexShard;

    @BeforeMethod public void createIndexShard() throws IOException {
        Settings settings = EMPTY_SETTINGS;
        Environment environment = new Environment(settings);
        ShardId shardId = new ShardId("test", 1);
        AnalysisService analysisService = new AnalysisService(shardId.index());
        MapperService mapperService = new MapperService(shardId.index(), settings, environment, analysisService);
        IndexQueryParserService queryParserService = new IndexQueryParserService(shardId.index(), mapperService, new IndexCache(shardId.index()), new RobinIndexEngine(shardId.index()), analysisService);
        IndexCache indexCache = new IndexCache(shardId.index());

        SnapshotDeletionPolicy policy = new SnapshotDeletionPolicy(new KeepOnlyLastDeletionPolicy(shardId, settings));
        Store store = new RamStore(shardId, settings);
        MemoryTranslog translog = new MemoryTranslog(shardId, settings);
        Engine engine = new RobinEngine(shardId, settings, store, policy, translog,
                new LogByteSizeMergePolicyProvider(store), new SerialMergeSchedulerProvider(shardId, settings),
                analysisService, new SimilarityService(shardId.index()));

        threadPool = new ScalingThreadPool();

        indexShard = new InternalIndexShard(shardId, EMPTY_SETTINGS, store, engine, translog, threadPool, mapperService, queryParserService, indexCache).start();
    }

    @AfterMethod public void tearDown() {
        indexShard.close();
        threadPool.shutdown();
    }

    @Test public void testSimpleIndexGetDelete() {
        String source1 = "{ type1 : { _id : \"1\", name : \"test\", age : 35 } }";
        indexShard.index("type1", "1", Unicode.fromStringAsBytes(source1));
        indexShard.refresh(new Engine.Refresh(true));

        String sourceFetched = Unicode.fromBytes(indexShard.get("type1", "1"));

        assertThat(sourceFetched, equalTo(source1));

        assertThat(indexShard.count(0, termQuery("age", 35).buildAsBytes(), null), equalTo(1l));
        assertThat(indexShard.count(0, queryString("name:test").buildAsBytes(), null), equalTo(1l));
        assertThat(indexShard.count(0, queryString("age:35").buildAsBytes(), null), equalTo(1l));

        indexShard.delete("type1", "1");
        indexShard.refresh(new Engine.Refresh(true));

        assertThat(indexShard.get("type1", "1"), nullValue());

        indexShard.index("type1", "1", Unicode.fromStringAsBytes(source1));
        indexShard.refresh(new Engine.Refresh(true));
        sourceFetched = Unicode.fromBytes(indexShard.get("type1", "1"));
        assertThat(sourceFetched, equalTo(source1));
        indexShard.deleteByQuery(termQuery("name", "test").buildAsBytes(), null);
        indexShard.refresh(new Engine.Refresh(true));
        assertThat(indexShard.get("type1", "1"), nullValue());

        indexShard.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4132.java