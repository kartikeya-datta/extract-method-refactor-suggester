error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7832.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7832.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7832.java
text:
```scala
r@@eturn shards.get(index);

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

package org.elasticsearch.cluster.routing;

import java.util.List;

/**
 * @author kimchy (shay.banon)
 */
public class PlainShardsIterator implements ShardsIterator {

    private final List<ShardRouting> shards;

    private final int size;

    private final int index;

    private final int limit;

    private volatile int counter;

    public PlainShardsIterator(List<ShardRouting> shards) {
        this(shards, 0);
    }

    public PlainShardsIterator(List<ShardRouting> shards, int index) {
        this.shards = shards;
        this.size = shards.size();
        if (size == 0) {
            this.index = 0;
        } else {
            this.index = Math.abs(index % size);
        }
        this.counter = this.index;
        this.limit = this.index + size;
    }

    @Override public ShardsIterator reset() {
        this.counter = this.index;
        return this;
    }

    @Override public int remaining() {
        return limit - counter;
    }

    @Override public ShardRouting firstOrNull() {
        if (size == 0) {
            return null;
        }
        return shards.get((index + 1) % size);
    }

    @Override public ShardRouting nextOrNull() {
        if (size == 0) {
            return null;
        }
        int counter = (this.counter);
        if (counter >= size) {
            if (counter >= limit) {
                return null;
            }
            this.counter = counter + 1;
            return shards.get(counter - size);
        } else {
            this.counter = counter + 1;
            return shards.get(counter);
        }
    }

    @Override public int size() {
        return size;
    }

    @Override public int sizeActive() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (shards.get(i).active()) {
                count++;
            }
        }
        return count;
    }

    @Override public int assignedReplicasIncludingRelocating() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            ShardRouting shard = shards.get(i);
            if (shard.unassigned()) {
                continue;
            }
            // if the shard is primary and relocating, add one to the counter since we perform it on the replica as well
            // (and we already did it on the primary)
            if (shard.primary()) {
                if (shard.relocating()) {
                    count++;
                }
            } else {
                count++;
                // if we are relocating the replica, we want to perform the index operation on both the relocating
                // shard and the target shard. This means that we won't loose index operations between end of recovery
                // and reassignment of the shard by the master node
                if (shard.relocating()) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override public Iterable<ShardRouting> asUnordered() {
        return shards;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7832.java