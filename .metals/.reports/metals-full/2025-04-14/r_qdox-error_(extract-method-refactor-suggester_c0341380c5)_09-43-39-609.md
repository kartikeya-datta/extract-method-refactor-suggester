error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7470.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7470.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7470.java
text:
```scala
C@@oncurrentMap<String, AtomicLong> accessCounts = new ConcurrentHashMap<>();

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

package org.elasticsearch.snapshots.mockstore;

import com.google.common.collect.ImmutableMap;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.blobstore.BlobMetaData;
import org.elasticsearch.common.blobstore.BlobPath;
import org.elasticsearch.common.blobstore.BlobStore;
import org.elasticsearch.common.blobstore.ImmutableBlobContainer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.index.snapshots.IndexShardRepository;
import org.elasticsearch.repositories.RepositoryName;
import org.elasticsearch.repositories.RepositorySettings;
import org.elasticsearch.repositories.fs.FsRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 */
public class MockRepository extends FsRepository {

    private final AtomicLong failureCounter = new AtomicLong();

    public void resetFailureCount() {
        failureCounter.set(0);
    }

    public long getFailureCount() {
        return failureCounter.get();
    }

    private final double randomControlIOExceptionRate;

    private final double randomDataFileIOExceptionRate;

    private final long waitAfterUnblock;

    private final MockBlobStore mockBlobStore;

    private final String randomPrefix;

    private volatile boolean blockOnControlFiles;

    private volatile boolean blockOnDataFiles;

    private volatile boolean blocked = false;

    @Inject
    public MockRepository(RepositoryName name, RepositorySettings repositorySettings, IndexShardRepository indexShardRepository) throws IOException {
        super(name, repositorySettings, indexShardRepository);
        randomControlIOExceptionRate = repositorySettings.settings().getAsDouble("random_control_io_exception_rate", 0.0);
        randomDataFileIOExceptionRate = repositorySettings.settings().getAsDouble("random_data_file_io_exception_rate", 0.0);
        blockOnControlFiles = repositorySettings.settings().getAsBoolean("block_on_control", false);
        blockOnDataFiles = repositorySettings.settings().getAsBoolean("block_on_data", false);
        randomPrefix = repositorySettings.settings().get("random");
        waitAfterUnblock = repositorySettings.settings().getAsLong("wait_after_unblock", 0L);
        logger.info("starting mock repository with random prefix " + randomPrefix);
        mockBlobStore = new MockBlobStore(super.blobStore());
    }

    private void addFailure() {
        failureCounter.incrementAndGet();
    }

    @Override
    protected void doStop() throws ElasticsearchException {
        unblock();
        super.doStop();
    }

    @Override
    protected BlobStore blobStore() {
        return mockBlobStore;
    }

    public boolean blocked() {
        return mockBlobStore.blocked();
    }

    public void unblock() {
        mockBlobStore.unblockExecution();
    }

    public void blockOnDataFiles(boolean blocked) {
        blockOnDataFiles = blocked;
    }

    public void blockOnControlFiles(boolean blocked) {
        blockOnControlFiles = blocked;
    }

    public class MockBlobStore extends BlobStoreWrapper {
        ConcurrentMap<String, AtomicLong> accessCounts = new ConcurrentHashMap<String, AtomicLong>();

        private long incrementAndGet(String path) {
            AtomicLong value = accessCounts.get(path);
            if (value == null) {
                value = accessCounts.putIfAbsent(path, new AtomicLong(1));
            }
            if (value != null) {
                return value.incrementAndGet();
            }
            return 1;
        }

        public MockBlobStore(BlobStore delegate) {
            super(delegate);
        }

        @Override
        public ImmutableBlobContainer immutableBlobContainer(BlobPath path) {
            return new MockImmutableBlobContainer(super.immutableBlobContainer(path));
        }

        public synchronized void unblockExecution() {
            if (blocked) {
                blocked = false;
                // Clean blocking flags, so we wouldn't try to block again
                blockOnDataFiles = false;
                blockOnControlFiles = false;
                this.notifyAll();
            }
        }

        public boolean blocked() {
            return blocked;
        }

        private synchronized boolean blockExecution() {
            boolean wasBlocked = false;
            try {
                while (blockOnDataFiles || blockOnControlFiles) {
                    blocked = true;
                    this.wait();
                    wasBlocked = true;
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            return wasBlocked;
        }

        private class MockImmutableBlobContainer extends ImmutableBlobContainerWrapper {
            private MessageDigest digest;

            private boolean shouldFail(String blobName, double probability) {
                if (probability > 0.0) {
                    String path = path().add(blobName).buildAsString("/") + "/" + randomPrefix;
                    path += "/" + incrementAndGet(path);
                    logger.info("checking [{}] [{}]", path, Math.abs(hashCode(path)) < Integer.MAX_VALUE * probability);
                    return Math.abs(hashCode(path)) < Integer.MAX_VALUE * probability;
                } else {
                    return false;
                }
            }

            private int hashCode(String path) {
                try {
                    digest = MessageDigest.getInstance("MD5");
                    byte[] bytes = digest.digest(path.getBytes("UTF-8"));
                    int i = 0;
                    return ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16)
 ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
                } catch (NoSuchAlgorithmException ex) {
                    throw new ElasticsearchException("cannot calculate hashcode", ex);
                } catch (UnsupportedEncodingException ex) {
                    throw new ElasticsearchException("cannot calculate hashcode", ex);
                }
            }

            private void maybeIOExceptionOrBlock(String blobName) throws IOException {
                if (blobName.startsWith("__")) {
                    if (shouldFail(blobName, randomDataFileIOExceptionRate)) {
                        logger.info("throwing random IOException for file [{}] at path [{}]", blobName, path());
                        addFailure();
                        throw new IOException("Random IOException");
                    } else if (blockOnDataFiles) {
                        logger.info("blocking I/O operation for file [{}] at path [{}]", blobName, path());
                        if (blockExecution() && waitAfterUnblock > 0) {
                            try {
                                // Delay operation after unblocking
                                // So, we can start node shutdown while this operation is still running.
                                Thread.sleep(waitAfterUnblock);
                            } catch (InterruptedException ex) {
                                //
                            }
                        }
                    }
                } else {
                    if (shouldFail(blobName, randomControlIOExceptionRate)) {
                        logger.info("throwing random IOException for file [{}] at path [{}]", blobName, path());
                        addFailure();
                        throw new IOException("Random IOException");
                    } else if (blockOnControlFiles) {
                        logger.info("blocking I/O operation for file [{}] at path [{}]", blobName, path());
                        if (blockExecution() && waitAfterUnblock > 0) {
                            try {
                                // Delay operation after unblocking
                                // So, we can start node shutdown while this operation is still running.
                                Thread.sleep(waitAfterUnblock);
                            } catch (InterruptedException ex) {
                                //
                            }
                        }
                    }
                }
            }

            private boolean maybeIOExceptionOrBlock(String blobName, ImmutableBlobContainer.WriterListener listener) {
                try {
                    maybeIOExceptionOrBlock(blobName);
                    return true;
                } catch (IOException ex) {
                    listener.onFailure(ex);
                    return false;
                }
            }

            private boolean maybeIOExceptionOrBlock(String blobName, ImmutableBlobContainer.ReadBlobListener listener) {
                try {
                    maybeIOExceptionOrBlock(blobName);
                    return true;
                } catch (IOException ex) {
                    listener.onFailure(ex);
                    return false;
                }
            }


            public MockImmutableBlobContainer(ImmutableBlobContainer delegate) {
                super(delegate);
            }

            @Override
            public void writeBlob(String blobName, InputStream is, long sizeInBytes, WriterListener listener) {
                if (maybeIOExceptionOrBlock(blobName, listener) ) {
                    super.writeBlob(blobName, is, sizeInBytes, listener);
                }
            }

            @Override
            public void writeBlob(String blobName, InputStream is, long sizeInBytes) throws IOException {
                maybeIOExceptionOrBlock(blobName);
                super.writeBlob(blobName, is, sizeInBytes);
            }

            @Override
            public boolean blobExists(String blobName) {
                return super.blobExists(blobName);
            }

            @Override
            public void readBlob(String blobName, ReadBlobListener listener) {
                if (maybeIOExceptionOrBlock(blobName, listener)) {
                    super.readBlob(blobName, listener);
                }
            }

            @Override
            public byte[] readBlobFully(String blobName) throws IOException {
                maybeIOExceptionOrBlock(blobName);
                return super.readBlobFully(blobName);
            }

            @Override
            public boolean deleteBlob(String blobName) throws IOException {
                maybeIOExceptionOrBlock(blobName);
                return super.deleteBlob(blobName);
            }

            @Override
            public void deleteBlobsByPrefix(String blobNamePrefix) throws IOException {
                maybeIOExceptionOrBlock(blobNamePrefix);
                super.deleteBlobsByPrefix(blobNamePrefix);
            }

            @Override
            public void deleteBlobsByFilter(BlobNameFilter filter) throws IOException {
                maybeIOExceptionOrBlock("");
                super.deleteBlobsByFilter(filter);
            }

            @Override
            public ImmutableMap<String, BlobMetaData> listBlobs() throws IOException {
                maybeIOExceptionOrBlock("");
                return super.listBlobs();
            }

            @Override
            public ImmutableMap<String, BlobMetaData> listBlobsByPrefix(String blobNamePrefix) throws IOException {
                maybeIOExceptionOrBlock(blobNamePrefix);
                return super.listBlobsByPrefix(blobNamePrefix);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7470.java