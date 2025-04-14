error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2071.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2071.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2071.java
text:
```scala
public R@@ateLimitedFSDirectory(Directory wrapped, StoreRateLimiting.Provider rateLimitingProvider,

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
package org.apache.lucene.store;

import org.apache.lucene.store.IOContext.Context;

import java.io.IOException;

public final class RateLimitedFSDirectory extends FilterDirectory {

    private final StoreRateLimiting.Provider rateLimitingProvider;

    private final StoreRateLimiting.Listener rateListener;

    public RateLimitedFSDirectory(FSDirectory wrapped, StoreRateLimiting.Provider rateLimitingProvider,
                                  StoreRateLimiting.Listener rateListener) {
        super(wrapped);
        this.rateLimitingProvider = rateLimitingProvider;
        this.rateListener = rateListener;
    }

    @Override
    public IndexOutput createOutput(String name, IOContext context) throws IOException {
        final IndexOutput output = in.createOutput(name, context);

        StoreRateLimiting rateLimiting = rateLimitingProvider.rateLimiting();
        StoreRateLimiting.Type type = rateLimiting.getType();
        RateLimiter limiter = rateLimiting.getRateLimiter();
        if (type == StoreRateLimiting.Type.NONE || limiter == null) {
            return output;
        }
        if (context.context == Context.MERGE || type == StoreRateLimiting.Type.ALL) {
            // we are merging, and type is either MERGE or ALL, rate limit...
            return new RateLimitedIndexOutput(new RateLimiterWrapper(limiter, rateListener), output);
        }
        // we shouldn't really get here...
        return output;
    }


    @Override
    public void close() throws IOException {
        in.close();
    }

    @Override
    public String toString() {
        StoreRateLimiting rateLimiting = rateLimitingProvider.rateLimiting();
        StoreRateLimiting.Type type = rateLimiting.getType();
        RateLimiter limiter = rateLimiting.getRateLimiter();
        if (type == StoreRateLimiting.Type.NONE || limiter == null) {
            return StoreUtils.toString(in);
        } else {
            return "rate_limited(" + StoreUtils.toString(in) + ", type=" + type.name() + ", rate=" + limiter.getMbPerSec() + ")";
        }
    }

    // we wrap the limiter to notify our store if we limited to get statistics
    static final class RateLimiterWrapper extends RateLimiter {
        private final RateLimiter delegate;
        private final StoreRateLimiting.Listener rateListener;

        RateLimiterWrapper(RateLimiter delegate, StoreRateLimiting.Listener rateListener) {
            this.delegate = delegate;
            this.rateListener = rateListener;
        }

        @Override
        public void setMbPerSec(double mbPerSec) {
            delegate.setMbPerSec(mbPerSec);
        }

        @Override
        public double getMbPerSec() {
            return delegate.getMbPerSec();
        }

        @Override
        public long pause(long bytes) {
            long pause = delegate.pause(bytes);
            rateListener.onPause(pause);
            return pause;
        }

        @Override
        public long getMinPauseCheckBytes() {
            return delegate.getMinPauseCheckBytes();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2071.java