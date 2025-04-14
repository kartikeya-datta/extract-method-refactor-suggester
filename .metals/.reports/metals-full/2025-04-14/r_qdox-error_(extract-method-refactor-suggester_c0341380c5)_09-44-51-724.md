error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6484.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6484.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6484.java
text:
```scala
t@@ranslog.close(true);

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

package org.elasticsearch.index.translog;

import org.apache.lucene.index.Term;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.shard.ShardId;
import org.hamcrest.Matchers;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.elasticsearch.index.translog.TranslogSizeMatcher.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author kimchy (Shay Banon)
 */
public abstract class AbstractSimpleTranslogTests {

    protected final ShardId shardId = new ShardId(new Index("index"), 1);

    protected Translog translog;

    @BeforeMethod public void setUp() {
        translog = create();
        translog.newTranslog();
    }

    @AfterMethod public void tearDown() {
        translog.close();
    }

    protected abstract Translog create();

    @Test public void testSimpleOperations() {
        Translog.Snapshot snapshot = translog.snapshot();
        assertThat(snapshot, translogSize(0));
        snapshot.release();

        translog.add(new Translog.Create("test", "1", new byte[]{1}));
        snapshot = translog.snapshot();
        assertThat(snapshot, translogSize(1));
        assertThat(snapshot.totalOperations(), equalTo(1));
        assertThat(snapshot.snapshotOperations(), equalTo(1));
        snapshot.release();

        translog.add(new Translog.Index("test", "2", new byte[]{2}));
        snapshot = translog.snapshot();
        assertThat(snapshot, translogSize(2));
        assertThat(snapshot.totalOperations(), equalTo(2));
        assertThat(snapshot.snapshotOperations(), equalTo(2));
        snapshot.release();

        translog.add(new Translog.Delete(newUid("3")));
        snapshot = translog.snapshot();
        assertThat(snapshot, translogSize(3));
        assertThat(snapshot.totalOperations(), equalTo(3));
        assertThat(snapshot.snapshotOperations(), equalTo(3));
        snapshot.release();

        translog.add(new Translog.DeleteByQuery(new byte[]{4}, null));
        snapshot = translog.snapshot();
        assertThat(snapshot, translogSize(4));
        assertThat(snapshot.totalOperations(), equalTo(4));
        assertThat(snapshot.snapshotOperations(), equalTo(4));
        snapshot.release();

        snapshot = translog.snapshot();

        assertThat(snapshot.hasNext(), equalTo(true));
        Translog.Create create = (Translog.Create) snapshot.next();
        assertThat(create.source(), equalTo(new byte[]{1}));

        assertThat(snapshot.hasNext(), equalTo(true));
        Translog.Index index = (Translog.Index) snapshot.next();
        assertThat(index.source(), equalTo(new byte[]{2}));

        assertThat(snapshot.hasNext(), equalTo(true));
        Translog.Delete delete = (Translog.Delete) snapshot.next();
        assertThat(delete.uid(), equalTo(newUid("3")));

        assertThat(snapshot.hasNext(), equalTo(true));
        Translog.DeleteByQuery deleteByQuery = (Translog.DeleteByQuery) snapshot.next();
        assertThat(deleteByQuery.source(), equalTo(new byte[]{4}));

        assertThat(snapshot.hasNext(), equalTo(false));

        snapshot.release();

        long firstId = translog.currentId();
        translog.newTranslog();
        assertThat(translog.currentId(), Matchers.not(equalTo(firstId)));

        snapshot = translog.snapshot();
        assertThat(snapshot, translogSize(0));
        assertThat(snapshot.totalOperations(), equalTo(0));
        assertThat(snapshot.snapshotOperations(), equalTo(0));
        snapshot.release();
    }

    @Test public void testSnapshot() {
        Translog.Snapshot snapshot = translog.snapshot();
        assertThat(snapshot, translogSize(0));
        snapshot.release();

        translog.add(new Translog.Create("test", "1", new byte[]{1}));
        snapshot = translog.snapshot();
        assertThat(snapshot, translogSize(1));
        assertThat(snapshot.totalOperations(), equalTo(1));
        assertThat(snapshot.snapshotOperations(), equalTo(1));
        snapshot.release();

        snapshot = translog.snapshot();
        assertThat(snapshot.hasNext(), equalTo(true));
        Translog.Create create = (Translog.Create) snapshot.next();
        assertThat(create.source(), equalTo(new byte[]{1}));
        snapshot.release();

        Translog.Snapshot snapshot1 = translog.snapshot();
        // we use the translogSize to also navigate to the last position on this snapshot
        // so snapshot(Snapshot) will work properly
        assertThat(snapshot1, translogSize(1));
        assertThat(snapshot1.totalOperations(), equalTo(1));
        assertThat(snapshot1.snapshotOperations(), equalTo(1));

        translog.add(new Translog.Index("test", "2", new byte[]{2}));
        snapshot = translog.snapshot(snapshot1);
        assertThat(snapshot, translogSize(1));
        assertThat(snapshot.totalOperations(), equalTo(2));
        assertThat(snapshot.snapshotOperations(), equalTo(1));
        snapshot.release();

        snapshot = translog.snapshot(snapshot1);
        assertThat(snapshot.hasNext(), equalTo(true));
        Translog.Index index = (Translog.Index) snapshot.next();
        assertThat(index.source(), equalTo(new byte[]{2}));
        assertThat(snapshot.hasNext(), equalTo(false));
        assertThat(snapshot.totalOperations(), equalTo(2));
        assertThat(snapshot.snapshotOperations(), equalTo(1));
        snapshot.release();
        snapshot1.release();
    }

    @Test public void testSnapshotWithNewTranslog() {
        Translog.Snapshot snapshot = translog.snapshot();
        assertThat(snapshot, translogSize(0));
        snapshot.release();

        translog.add(new Translog.Create("test", "1", new byte[]{1}));
        Translog.Snapshot actualSnapshot = translog.snapshot();

        translog.add(new Translog.Index("test", "2", new byte[]{2}));

        translog.newTranslog();

        translog.add(new Translog.Index("test", "3", new byte[]{3}));

        snapshot = translog.snapshot(actualSnapshot);
        assertThat(snapshot, translogSize(1));
        snapshot.release();

        snapshot = translog.snapshot(actualSnapshot);
        assertThat(snapshot.hasNext(), equalTo(true));
        Translog.Index index = (Translog.Index) snapshot.next();
        assertThat(index.source(), equalTo(new byte[]{3}));
        assertThat(snapshot.hasNext(), equalTo(false));

        actualSnapshot.release();
        snapshot.release();
    }

    @Test public void testSnapshotWithSeekForward() {
        Translog.Snapshot snapshot = translog.snapshot();
        assertThat(snapshot, translogSize(0));
        snapshot.release();

        translog.add(new Translog.Create("test", "1", new byte[]{1}));
        snapshot = translog.snapshot();
        assertThat(snapshot, translogSize(1));
        long lastPosition = snapshot.position();
        snapshot.release();

        translog.add(new Translog.Create("test", "2", new byte[]{1}));
        snapshot = translog.snapshot();
        snapshot.seekForward(lastPosition);
        assertThat(snapshot, translogSize(1));
        snapshot.release();

        snapshot = translog.snapshot();
        snapshot.seekForward(lastPosition);
        assertThat(snapshot.hasNext(), equalTo(true));
        Translog.Create create = (Translog.Create) snapshot.next();
        assertThat(create.id(), equalTo("2"));
        snapshot.release();
    }

    private Term newUid(String id) {
        return new Term("_uid", id);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6484.java