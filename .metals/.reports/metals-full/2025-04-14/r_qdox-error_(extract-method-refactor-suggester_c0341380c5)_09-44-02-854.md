error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/77.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/77.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/77.java
text:
```scala
r@@efreshLock.unlock();

package org.apache.lucene.search;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.lucene.store.AlreadyClosedException;

/**
 * Utility class to safely share instances of a certain type across multiple
 * threads, while periodically refreshing them. This class ensures each
 * reference is closed only once all threads have finished using it. It is
 * recommended to consult the documentation of {@link ReferenceManager}
 * implementations for their {@link #maybeRefresh()} semantics.
 * 
 * @param <G>
 *          the concrete type that will be {@link #acquire() acquired} and
 *          {@link #release(Object) released}.
 * 
 * @lucene.experimental
 */
public abstract class ReferenceManager<G> implements Closeable {

  private static final String REFERENCE_MANAGER_IS_CLOSED_MSG = "this ReferenceManager is closed";
  
  protected volatile G current;
  
  private final Lock refreshLock = new ReentrantLock();
  
  private void ensureOpen() {
    if (current == null) {
      throw new AlreadyClosedException(REFERENCE_MANAGER_IS_CLOSED_MSG);
    }
  }
  
  private synchronized void swapReference(G newReference) throws IOException {
    ensureOpen();
    final G oldReference = current;
    current = newReference;
    release(oldReference);
  }

  /** Decrement reference counting on the given reference. */
  protected abstract void decRef(G reference) throws IOException;
  
  /**
   * Refresh the given reference if needed. Returns {@code null} if no refresh
   * was needed, otherwise a new refreshed reference.
   */
  protected abstract G refreshIfNeeded(G referenceToRefresh) throws IOException;

  /**
   * Try to increment reference counting on the given reference. Return true if
   * the operation was successful.
   */
  protected abstract boolean tryIncRef(G reference);

  /**
   * Obtain the current reference. You must match every call to acquire with one
   * call to {@link #release}; it's best to do so in a finally clause, and set
   * the reference to {@code null} to prevent accidental usage after it has been
   * released.
   */
  public final G acquire() {
    G ref;
    do {
      if ((ref = current) == null) {
        throw new AlreadyClosedException(REFERENCE_MANAGER_IS_CLOSED_MSG);
      }
    } while (!tryIncRef(ref));
    return ref;
  }

  /**
   * Close this ReferenceManager to future {@link #acquire() acquiring}. Any
   * references that were previously {@link #acquire() acquired} won't be
   * affected, and they should still be {@link #release released} when they are
   * not needed anymore.
   */
  public final synchronized void close() throws IOException {
    if (current != null) {
      // make sure we can call this more than once
      // closeable javadoc says:
      // if this is already closed then invoking this method has no effect.
      swapReference(null);
      afterClose();
    }
  }

  /** Called after close(), so subclass can free any resources. */
  protected void afterClose() throws IOException {
  }

  private void doMaybeRefresh() throws IOException {
    // it's ok to call lock() here (blocking) because we're supposed to get here
    // from either maybeRefreh() or maybeRefreshBlocking(), after the lock has
    // already been obtained. Doing that protects us from an accidental bug
    // where this method will be called outside the scope of refreshLock.
    // Per ReentrantLock's javadoc, calling lock() by the same thread more than
    // once is ok, as long as unlock() is called a matching number of times.
    refreshLock.lock();
    try {
      final G reference = acquire();
      try {
        G newReference = refreshIfNeeded(reference);
        if (newReference != null) {
          assert newReference != reference : "refreshIfNeeded should return null if refresh wasn't needed";
          boolean success = false;
          try {
            swapReference(newReference);
            success = true;
          } finally {
            if (!success) {
              release(newReference);
            }
          }
        }
      } finally {
        release(reference);
      }
      afterRefresh();
    } finally {
      refreshLock.unlock();
    }
  }
  
  /**
   * You must call this (or {@link #maybeRefreshBlocking()}), periodically, if
   * you want that {@link #acquire()} will return refreshed instances.
   * 
   * <p>
   * <b>Threads</b>: it's fine for more than one thread to call this at once.
   * Only the first thread will attempt the refresh; subsequent threads will see
   * that another thread is already handling refresh and will return
   * immediately. Note that this means if another thread is already refreshing
   * then subsequent threads will return right away without waiting for the
   * refresh to complete.
   * 
   * <p>
   * If this method returns true it means the calling thread either refreshed or
   * that there were no changes to refresh. If it returns false it means another
   * thread is currently refreshing.
   */
  public final boolean maybeRefresh() throws IOException {
    ensureOpen();

    // Ensure only 1 thread does reopen at once; other threads just return immediately:
    final boolean doTryRefresh = refreshLock.tryLock();
    if (doTryRefresh) {
      try {
        doMaybeRefresh();
      } finally {
        refreshLock.unlock();
      }
    }

    return doTryRefresh;
  }
  
  /**
   * You must call this (or {@link #maybeRefresh()}), periodically, if you want
   * that {@link #acquire()} will return refreshed instances.
   * 
   * <p>
   * <b>Threads</b>: unlike {@link #maybeRefresh()}, if another thread is
   * currently refreshing, this method blocks until that thread completes. It is
   * useful if you want to guarantee that the next call to {@link #acquire()}
   * will return a refreshed instance. Otherwise, consider using the
   * non-blocking {@link #maybeRefresh()}.
   */
  public final void maybeRefreshBlocking() throws IOException, InterruptedException {
    ensureOpen();

    // Ensure only 1 thread does reopen at once
    refreshLock.lock();
    try {
      doMaybeRefresh();
    } finally {
      refreshLock.lock();
    }
  }

  /** Called after swapReference has installed a new
   *  instance. */
  protected void afterRefresh() throws IOException {
  }
  
  /**
   * Release the refernce previously obtained via {@link #acquire()}.
   * <p>
   * <b>NOTE:</b> it's safe to call this after {@link #close()}.
   */
  public final void release(G reference) throws IOException {
    assert reference != null;
    decRef(reference);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/77.java