error id: <WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2284.java
<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2284.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2284.java
text:
```scala
public abstract v@@oid release() throws IOException;

package org.apache.lucene.store;

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

import java.io.IOException;

/** An interprocess mutex lock.
 * <p>Typical use might look like:<pre>
 * new Lock.With(directory.makeLock("my.lock")) {
 *     public Object doBody() {
 *       <i>... code to execute while locked ...</i>
 *     }
 *   }.run();
 * </pre>
 *
 *
 * @version $Id$
 * @see Directory#makeLock(String)
 */
public abstract class Lock {

  /** How long {@link #obtain(long)} waits, in milliseconds,
   *  in between attempts to acquire the lock. */
  public static long LOCK_POLL_INTERVAL = 1000;

  /** Pass this value to {@link #obtain(long)} to try
   *  forever to obtain the lock. */
  public static final long LOCK_OBTAIN_WAIT_FOREVER = -1;

  /** Attempts to obtain exclusive access and immediately return
   *  upon success or failure.
   * @return true iff exclusive access is obtained
   */
  public abstract boolean obtain() throws IOException;

  /**
   * If a lock obtain called, this failureReason may be set
   * with the "root cause" Exception as to why the lock was
   * not obtained.
   */
  protected Throwable failureReason;

  /** Attempts to obtain an exclusive lock within amount of
   *  time given. Polls once per {@link #LOCK_POLL_INTERVAL}
   *  (currently 1000) milliseconds until lockWaitTimeout is
   *  passed.
   * @param lockWaitTimeout length of time to wait in
   *        milliseconds or {@link
   *        #LOCK_OBTAIN_WAIT_FOREVER} to retry forever
   * @return true if lock was obtained
   * @throws LockObtainFailedException if lock wait times out
   * @throws IllegalArgumentException if lockWaitTimeout is
   *         out of bounds
   * @throws IOException if obtain() throws IOException
   */
  public boolean obtain(long lockWaitTimeout) throws LockObtainFailedException, IOException {
    failureReason = null;
    boolean locked = obtain();
    if (lockWaitTimeout < 0 && lockWaitTimeout != LOCK_OBTAIN_WAIT_FOREVER)
      throw new IllegalArgumentException("lockWaitTimeout should be LOCK_OBTAIN_WAIT_FOREVER or a non-negative number (got " + lockWaitTimeout + ")");

    long maxSleepCount = lockWaitTimeout / LOCK_POLL_INTERVAL;
    long sleepCount = 0;
    while (!locked) {
      if (lockWaitTimeout != LOCK_OBTAIN_WAIT_FOREVER && sleepCount++ >= maxSleepCount) {
        String reason = "Lock obtain timed out: " + this.toString();
        if (failureReason != null) {
          reason += ": " + failureReason;
        }
        LockObtainFailedException e = new LockObtainFailedException(reason);
        if (failureReason != null) {
          e.initCause(failureReason);
        }
        throw e;
      }
      try {
        Thread.sleep(LOCK_POLL_INTERVAL);
      } catch (InterruptedException e) {
        throw new IOException(e.toString());
      }
      locked = obtain();
    }
    return locked;
  }

  /** Releases exclusive access. */
  public abstract void release();

  /** Returns true if the resource is currently locked.  Note that one must
   * still call {@link #obtain()} before using the resource. */
  public abstract boolean isLocked();


  /** Utility class for executing code with exclusive access. */
  public abstract static class With {
    private Lock lock;
    private long lockWaitTimeout;


    /** Constructs an executor that will grab the named lock. */
    public With(Lock lock, long lockWaitTimeout) {
      this.lock = lock;
      this.lockWaitTimeout = lockWaitTimeout;
    }

    /** Code to execute with exclusive access. */
    protected abstract Object doBody() throws IOException;

    /** Calls {@link #doBody} while <i>lock</i> is obtained.  Blocks if lock
     * cannot be obtained immediately.  Retries to obtain lock once per second
     * until it is obtained, or until it has tried ten times. Lock is released when
     * {@link #doBody} exits.
     * @throws LockObtainFailedException if lock could not
     * be obtained
     * @throws IOException if {@link Lock#obtain} throws IOException
     */
    public Object run() throws LockObtainFailedException, IOException {
      boolean locked = false;
      try {
         locked = lock.obtain(lockWaitTimeout);
         return doBody();
      } finally {
        if (locked)
	      lock.release();
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
	scala.meta.internal.mtags.MtagsIndexer.index(MtagsIndexer.scala:21)
	scala.meta.internal.mtags.MtagsIndexer.index$(MtagsIndexer.scala:20)
	scala.meta.internal.mtags.JavaMtags.index(JavaMtags.scala:38)
	scala.meta.internal.tvp.IndexedSymbols.javaSymbols(IndexedSymbols.scala:111)
	scala.meta.internal.tvp.IndexedSymbols.workspaceSymbolsFromPath(IndexedSymbols.scala:120)
	scala.meta.internal.tvp.IndexedSymbols.$anonfun$workspaceSymbols$2(IndexedSymbols.scala:146)
	scala.collection.concurrent.TrieMap.getOrElseUpdate(TrieMap.scala:960)
	scala.meta.internal.tvp.IndexedSymbols.$anonfun$workspaceSymbols$1(IndexedSymbols.scala:146)
	scala.meta.internal.tvp.IndexedSymbols.withTimer(IndexedSymbols.scala:71)
	scala.meta.internal.tvp.IndexedSymbols.workspaceSymbols(IndexedSymbols.scala:143)
	scala.meta.internal.tvp.FolderTreeViewProvider.$anonfun$projects$9(MetalsTreeViewProvider.scala:306)
	scala.collection.Iterator$$anon$9.next(Iterator.scala:584)
	scala.collection.Iterator$$anon$10.nextCur(Iterator.scala:594)
	scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:608)
	scala.collection.Iterator$$anon$6.hasNext(Iterator.scala:477)
	scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:601)
	scala.collection.Iterator$$anon$8.hasNext(Iterator.scala:562)
	scala.collection.immutable.List.prependedAll(List.scala:155)
	scala.collection.immutable.List$.from(List.scala:685)
	scala.collection.immutable.List$.from(List.scala:682)
	scala.collection.SeqFactory$Delegate.from(Factory.scala:306)
	scala.collection.immutable.Seq$.from(Seq.scala:42)
	scala.collection.IterableOnceOps.toSeq(IterableOnce.scala:1473)
	scala.collection.IterableOnceOps.toSeq$(IterableOnce.scala:1473)
	scala.collection.AbstractIterator.toSeq(Iterator.scala:1306)
	scala.meta.internal.tvp.ClasspathTreeView.children(ClasspathTreeView.scala:62)
	scala.meta.internal.tvp.FolderTreeViewProvider.getProjectRoot(MetalsTreeViewProvider.scala:390)
	scala.meta.internal.tvp.MetalsTreeViewProvider.$anonfun$children$1(MetalsTreeViewProvider.scala:84)
	scala.collection.immutable.List.map(List.scala:247)
	scala.meta.internal.tvp.MetalsTreeViewProvider.children(MetalsTreeViewProvider.scala:84)
	scala.meta.internal.metals.WorkspaceLspService.$anonfun$treeViewChildren$1(WorkspaceLspService.scala:705)
	scala.concurrent.Future$.$anonfun$apply$1(Future.scala:687)
	scala.concurrent.impl.Promise$Transformation.run(Promise.scala:467)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	java.base/java.lang.Thread.run(Thread.java:840)
```
#### Short summary: 

QDox parse error in <WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2284.java