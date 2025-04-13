error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9478.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9478.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9478.java
text:
```scala
t@@his.location = new File(nodeEnv.shardLocation(shardId), "translog");

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

package org.elasticsearch.index.translog.fs;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.io.stream.CachedStreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.env.NodeEnvironment;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.index.shard.AbstractIndexShardComponent;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.translog.Translog;
import org.elasticsearch.index.translog.TranslogException;
import org.elasticsearch.index.translog.TranslogStreams;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kimchy (shay.banon)
 */
public class FsTranslog extends AbstractIndexShardComponent implements Translog {

    private final File location;

    private final boolean useStream;

    private final Object mutex = new Object();

    private boolean syncOnEachOperation = false;

    private volatile long id = 0;

    private final AtomicInteger operationCounter = new AtomicInteger();

    private long lastPosition = 0;

    private RafReference raf;

    @Inject public FsTranslog(ShardId shardId, @IndexSettings Settings indexSettings, NodeEnvironment nodeEnv) {
        super(shardId, indexSettings);
        this.location = new File(new File(new File(new File(nodeEnv.nodeDataLocation(), "indices"), shardId.index().name()), Integer.toString(shardId.id())), "translog");
        this.location.mkdirs();
        this.useStream = componentSettings.getAsBoolean("use_stream", false);
    }

    public FsTranslog(ShardId shardId, @IndexSettings Settings indexSettings, File location) {
        this(shardId, indexSettings, location, false);
    }

    public FsTranslog(ShardId shardId, @IndexSettings Settings indexSettings, File location, boolean useStream) {
        super(shardId, indexSettings);
        this.location = location;
        this.location.mkdirs();
        this.useStream = useStream;
    }

    public File location() {
        return location;
    }

    @Override public long currentId() {
        return this.id;
    }

    @Override public int size() {
        return operationCounter.get();
    }

    @Override public ByteSizeValue estimateMemorySize() {
        return new ByteSizeValue(0, ByteSizeUnit.BYTES);
    }

    @Override public void clearUnreferenced() {
        synchronized (mutex) {
            File[] files = location.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().equals("translog-" + id)) {
                        continue;
                    }
                    try {
                        file.delete();
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        }
    }

    @Override public void newTranslog() throws TranslogException {
        synchronized (mutex) {
            operationCounter.set(0);
            lastPosition = 0;
            this.id = id + 1;
            if (raf != null) {
                raf.decreaseRefCount(true);
            }
            try {
                raf = new RafReference(new File(location, "translog-" + id));
                raf.raf().setLength(0);
            } catch (IOException e) {
                raf = null;
                throw new TranslogException(shardId, "translog not found", e);
            }
        }
    }

    @Override public void newTranslog(long id) throws TranslogException {
        synchronized (mutex) {
            operationCounter.set(0);
            lastPosition = 0;
            this.id = id;
            if (raf != null) {
                raf.decreaseRefCount(true);
            }
            try {
                raf = new RafReference(new File(location, "translog-" + id));
                // clean the file if it exists
                raf.raf().setLength(0);
            } catch (IOException e) {
                raf = null;
                throw new TranslogException(shardId, "translog not found", e);
            }
        }
    }

    @Override public void add(Operation operation) throws TranslogException {
        try {
            BytesStreamOutput out = CachedStreamOutput.cachedBytes();
            out.writeInt(0); // marker for the size...
            TranslogStreams.writeTranslogOperation(out, operation);
            out.flush();

            int size = out.size();
            out.seek(0);
            out.writeInt(size - 4);

            synchronized (mutex) {
                raf.raf().write(out.unsafeByteArray(), 0, size);
                if (syncOnEachOperation) {
                    sync();
                }
                lastPosition += size;
                operationCounter.incrementAndGet();
            }
        } catch (Exception e) {
            throw new TranslogException(shardId, "Failed to write operation [" + operation + "]", e);
        }
    }

    @Override public Snapshot snapshot() throws TranslogException {
        synchronized (mutex) {
            try {
                raf.increaseRefCount();
                if (useStream) {
                    return new FsStreamSnapshot(shardId, this.id, raf, lastPosition, operationCounter.get(), operationCounter.get());
                } else {
                    return new FsChannelSnapshot(shardId, this.id, raf, lastPosition, operationCounter.get(), operationCounter.get());
                }
            } catch (IOException e) {
                throw new TranslogException(shardId, "Failed to snapshot", e);
            }
        }
    }

    @Override public Snapshot snapshot(Snapshot snapshot) {
        synchronized (mutex) {
            if (currentId() != snapshot.translogId()) {
                return snapshot();
            }
            try {
                raf.increaseRefCount();
                if (useStream) {
                    FsStreamSnapshot newSnapshot = new FsStreamSnapshot(shardId, id, raf, lastPosition, operationCounter.get(), operationCounter.get() - snapshot.totalOperations());
                    newSnapshot.seekForward(snapshot.position());
                    return newSnapshot;
                } else {
                    FsChannelSnapshot newSnapshot = new FsChannelSnapshot(shardId, id, raf, lastPosition, operationCounter.get(), operationCounter.get() - snapshot.totalOperations());
                    newSnapshot.seekForward(snapshot.position());
                    return newSnapshot;
                }
            } catch (IOException e) {
                throw new TranslogException(shardId, "Failed to snapshot", e);
            }
        }
    }

    @Override public void sync() {
        synchronized (mutex) {
            if (raf != null) {
                try {
                    raf.raf().getFD().sync();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
    }

    @Override public void syncOnEachOperation(boolean syncOnEachOperation) {
        synchronized (mutex) {
            this.syncOnEachOperation = syncOnEachOperation;
        }
    }

    @Override public void close(boolean delete) {
        synchronized (mutex) {
            if (raf != null) {
                raf.decreaseRefCount(delete);
                raf = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9478.java