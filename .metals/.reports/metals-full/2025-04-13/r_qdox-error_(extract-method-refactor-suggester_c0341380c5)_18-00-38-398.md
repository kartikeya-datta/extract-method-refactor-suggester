error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2020.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2020.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2020.java
text:
```scala
l@@ong cacheSizeInBytes = componentSettings.getAsBytesSize("cache_size", new ByteSizeValue(200, ByteSizeUnit.MB)).bytes();

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

package org.elasticsearch.cache.memory;

import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author kimchy (shay.banon)
 */
public class ByteBufferCache extends AbstractComponent {

    public static final boolean CLEAN_SUPPORTED;

    private static final Method directBufferCleaner;
    private static final Method directBufferCleanerClean;

    static {
        Method directBufferCleanerX = null;
        Method directBufferCleanerCleanX = null;
        boolean v;
        try {
            directBufferCleanerX = Class.forName("java.nio.DirectByteBuffer").getMethod("cleaner");
            directBufferCleanerX.setAccessible(true);
            directBufferCleanerCleanX = Class.forName("sun.misc.Cleaner").getMethod("clean");
            directBufferCleanerCleanX.setAccessible(true);
            v = true;
        } catch (Exception e) {
            v = false;
        }
        CLEAN_SUPPORTED = v;
        directBufferCleaner = directBufferCleanerX;
        directBufferCleanerClean = directBufferCleanerCleanX;
    }


    private final Queue<ByteBuffer> cache;

    private final boolean disableCache;

    private final int bufferSizeInBytes;

    private final long cacheSizeInBytes;

    private final boolean direct;

    private final AtomicLong acquiredBuffers = new AtomicLong();

    public ByteBufferCache() {
        this(ImmutableSettings.Builder.EMPTY_SETTINGS);
    }

    public ByteBufferCache(int bufferSizeInBytes, int cacheSizeInBytes, boolean direct, boolean warmCache) {
        this(ImmutableSettings.settingsBuilder().put("buffer_size", bufferSizeInBytes).put("cache_size", cacheSizeInBytes).put("direct", direct).put("warm_cache", warmCache).build());
    }

    @Inject public ByteBufferCache(Settings settings) {
        super(settings);

        this.bufferSizeInBytes = (int) componentSettings.getAsBytesSize("buffer_size", new ByteSizeValue(100, ByteSizeUnit.KB)).bytes();
        long cacheSizeInBytes = componentSettings.getAsBytesSize("cache_size", new ByteSizeValue(20, ByteSizeUnit.MB)).bytes();
        this.direct = componentSettings.getAsBoolean("direct", true);
        boolean warmCache = componentSettings.getAsBoolean("warm_cache", false);

        disableCache = cacheSizeInBytes == 0;
        if (!disableCache && cacheSizeInBytes < bufferSizeInBytes) {
            throw new IllegalArgumentException("Cache size [" + cacheSizeInBytes + "] is smaller than buffer size [" + bufferSizeInBytes + "]");
        }
        int numberOfCacheEntries = (int) (cacheSizeInBytes / bufferSizeInBytes);
        this.cache = disableCache ? null : new ArrayBlockingQueue<ByteBuffer>(numberOfCacheEntries);
        this.cacheSizeInBytes = disableCache ? 0 : numberOfCacheEntries * bufferSizeInBytes;

        if (logger.isDebugEnabled()) {
            logger.debug("using bytebuffer cache with buffer_size [{}], cache_size [{}], direct [{}], warm_cache [{}]",
                    new ByteSizeValue(bufferSizeInBytes), new ByteSizeValue(cacheSizeInBytes), direct, warmCache);
        }
    }

    public ByteSizeValue bufferSize() {
        return new ByteSizeValue(bufferSizeInBytes);
    }

    public ByteSizeValue cacheSize() {
        return new ByteSizeValue(cacheSizeInBytes);
    }

    public ByteSizeValue allocatedMemory() {
        return new ByteSizeValue(acquiredBuffers.get() * bufferSizeInBytes);
    }

    public int bufferSizeInBytes() {
        return bufferSizeInBytes;
    }

    public boolean direct() {
        return direct;
    }

    public void close() {
        if (!disableCache) {
            ByteBuffer buffer = cache.poll();
            while (buffer != null) {
                closeBuffer(buffer);
                buffer = cache.poll();
            }
        }
        acquiredBuffers.set(0);
    }

    public ByteBuffer acquireBuffer() {
        acquiredBuffers.incrementAndGet();
        if (disableCache) {
            return createBuffer();
        }
        ByteBuffer byteBuffer = cache.poll();
        if (byteBuffer == null) {
            // everything is taken, return a new one
            return createBuffer();
        }
        byteBuffer.position(0);
        return byteBuffer;
    }

    public void releaseBuffer(ByteBuffer byteBuffer) {
        acquiredBuffers.decrementAndGet();
        if (disableCache) {
            closeBuffer(byteBuffer);
            return;
        }
        boolean success = cache.offer(byteBuffer);
        if (!success) {
            closeBuffer(byteBuffer);
        }
    }

    private ByteBuffer createBuffer() {
        if (direct) {
            return ByteBuffer.allocateDirect(bufferSizeInBytes);
        }
        return ByteBuffer.allocate(bufferSizeInBytes);
    }

    private void closeBuffer(ByteBuffer byteBuffer) {
        if (direct && CLEAN_SUPPORTED) {
            try {
                Object cleaner = directBufferCleaner.invoke(byteBuffer);
                directBufferCleanerClean.invoke(cleaner);
            } catch (Exception e) {
                logger.debug("Failed to clean memory");
                // ignore
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2020.java