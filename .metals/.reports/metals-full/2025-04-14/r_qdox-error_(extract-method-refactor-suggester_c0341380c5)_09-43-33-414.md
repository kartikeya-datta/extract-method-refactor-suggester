error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9792.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9792.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9792.java
text:
```scala
s@@b.append(" -> ").append(file.getAbsolutePath()).append(", free_space [").append(new ByteSizeValue(file.getFreeSpace())).append("], usable_space [").append(new ByteSizeValue(file.getUsableSpace())).append("]\n");

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
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

package org.elasticsearch.env;

import org.apache.lucene.store.Lock;
import org.apache.lucene.store.NativeFSLockFactory;
import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.FileSystemUtils;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.shard.ShardId;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 */
public class NodeEnvironment extends AbstractComponent {

    private final File[] nodeFiles;
    private final File[] nodeIndicesLocations;

    private final Lock[] locks;

    private final int localNodeId;

    @Inject
    public NodeEnvironment(Settings settings, Environment environment) {
        super(settings);

        if (!DiscoveryNode.nodeRequiresLocalStorage(settings)) {
            nodeFiles = null;
            nodeIndicesLocations = null;
            locks = null;
            localNodeId = -1;
            return;
        }

        File[] nodesFiles = new File[environment.dataWithClusterFiles().length];
        Lock[] locks = new Lock[environment.dataWithClusterFiles().length];
        int localNodeId = -1;
        IOException lastException = null;
        int maxLocalStorageNodes = settings.getAsInt("node.max_local_storage_nodes", 50);
        for (int possibleLockId = 0; possibleLockId < maxLocalStorageNodes; possibleLockId++) {
            for (int dirIndex = 0; dirIndex < environment.dataWithClusterFiles().length; dirIndex++) {
                File dir = new File(new File(environment.dataWithClusterFiles()[dirIndex], "nodes"), Integer.toString(possibleLockId));
                if (!dir.exists()) {
                    FileSystemUtils.mkdirs(dir);
                }
                logger.trace("obtaining node lock on {} ...", dir.getAbsolutePath());
                try {
                    NativeFSLockFactory lockFactory = new NativeFSLockFactory(dir);
                    Lock tmpLock = lockFactory.makeLock("node.lock");
                    boolean obtained = tmpLock.obtain();
                    if (obtained) {
                        locks[dirIndex] = tmpLock;
                        nodesFiles[dirIndex] = dir;
                        localNodeId = possibleLockId;
                    } else {
                        logger.trace("failed to obtain node lock on {}", dir.getAbsolutePath());
                        // release all the ones that were obtained up until now
                        for (int i = 0; i < locks.length; i++) {
                            if (locks[i] != null) {
                                try {
                                    locks[i].release();
                                } catch (Exception e1) {
                                    // ignore
                                }
                            }
                            locks[i] = null;
                        }
                        break;
                    }
                } catch (IOException e) {
                    logger.trace("failed to obtain node lock on {}", e, dir.getAbsolutePath());
                    lastException = new IOException("failed to obtain lock on " + dir.getAbsolutePath(), e);
                    // release all the ones that were obtained up until now
                    for (int i = 0; i < locks.length; i++) {
                        if (locks[i] != null) {
                            try {
                                locks[i].release();
                            } catch (Exception e1) {
                                // ignore
                            }
                        }
                        locks[i] = null;
                    }
                    break;
                }
            }
            if (locks[0] != null) {
                // we found a lock, break
                break;
            }
        }
        if (locks[0] == null) {
            throw new ElasticSearchIllegalStateException("Failed to obtain node lock, is the following location writable?: " + Arrays.toString(environment.dataWithClusterFiles()), lastException);
        }

        this.localNodeId = localNodeId;
        this.locks = locks;
        this.nodeFiles = nodesFiles;
        if (logger.isDebugEnabled()) {
            logger.debug("using node location [{}], local_node_id [{}]", nodesFiles, localNodeId);
        }
        if (logger.isTraceEnabled()) {
            StringBuilder sb = new StringBuilder("node data locations details:\n");
            for (File file : nodesFiles) {
                sb.append(" -> ").append(file.getAbsolutePath()).append(", free_space [").append(new ByteSizeValue(file.getFreeSpace())).append(", usable_space [").append(new ByteSizeValue(file.getUsableSpace())).append("\n");
            }
            logger.trace(sb.toString());
        }

        this.nodeIndicesLocations = new File[nodeFiles.length];
        for (int i = 0; i < nodeFiles.length; i++) {
            nodeIndicesLocations[i] = new File(nodeFiles[i], "indices");
        }
    }

    public int localNodeId() {
        return this.localNodeId;
    }

    public boolean hasNodeFile() {
        return nodeFiles != null && locks != null;
    }

    public File[] nodeDataLocations() {
        if (nodeFiles == null || locks == null) {
            throw new ElasticSearchIllegalStateException("node is not configured to store local location");
        }
        return nodeFiles;
    }

    public File[] indicesLocations() {
        return nodeIndicesLocations;
    }

    public File[] indexLocations(Index index) {
        File[] indexLocations = new File[nodeFiles.length];
        for (int i = 0; i < nodeFiles.length; i++) {
            indexLocations[i] = new File(new File(nodeFiles[i], "indices"), index.name());
        }
        return indexLocations;
    }

    public File[] shardLocations(ShardId shardId) {
        File[] shardLocations = new File[nodeFiles.length];
        for (int i = 0; i < nodeFiles.length; i++) {
            shardLocations[i] = new File(new File(new File(nodeFiles[i], "indices"), shardId.index().name()), Integer.toString(shardId.id()));
        }
        return shardLocations;
    }

    public void close() {
        if (locks != null) {
            for (Lock lock : locks) {
                try {
                    lock.release();
                } catch (IOException e) {
                    // ignore
                }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9792.java