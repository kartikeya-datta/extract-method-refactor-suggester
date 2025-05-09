error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2283.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2283.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2283.java
text:
```scala
public synchronized v@@oid release() throws IOException {

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

import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A {@link LockFactory} that wraps another {@link
 * LockFactory} and verifies that each lock obtain/release
 * is "correct" (never results in two processes holding the
 * lock at the same time).  It does this by contacting an
 * external server ({@link LockVerifyServer}) to assert that
 * at most one process holds the lock at a time.  To use
 * this, you should also run {@link LockVerifyServer} on the
 * host & port matching what you pass to the constructor.
 *
 * @see LockVerifyServer
 * @see LockStressTest
 */

public class VerifyingLockFactory extends LockFactory {

  LockFactory lf;
  byte id;
  String host;
  int port;

  private class CheckedLock extends Lock {
    private Lock lock;

    public CheckedLock(Lock lock) {
      this.lock = lock;
    }

    private void verify(byte message) {
      try {
        Socket s = new Socket(host, port);
        OutputStream out = s.getOutputStream();
        out.write(id);
        out.write(message);
        InputStream in = s.getInputStream();
        int result = in.read();
        in.close();
        out.close();
        s.close();
        if (result != 0)
          throw new RuntimeException("lock was double acquired");
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    public synchronized boolean obtain(long lockWaitTimeout)
      throws LockObtainFailedException, IOException {
      boolean obtained = lock.obtain(lockWaitTimeout);
      if (obtained)
        verify((byte) 1);
      return obtained;
    }

    public synchronized boolean obtain()
      throws LockObtainFailedException, IOException {
      return lock.obtain();
    }

    public synchronized boolean isLocked() {
      return lock.isLocked();
    }

    public synchronized void release() {
      if (isLocked()) {
        verify((byte) 0);
        lock.release();
      }
    }
  }

  /**
   * @param id should be a unique id across all clients
   * @param lf the LockFactory that we are testing
   * @param host host or IP where {@link LockVerifyServer}
            is running
   * @param port the port {@link LockVerifyServer} is
            listening on
  */
  public VerifyingLockFactory(byte id, LockFactory lf, String host, int port) throws IOException {
    this.id = id;
    this.lf = lf;
    this.host = host;
    this.port = port;
  }

  public synchronized Lock makeLock(String lockName) {
    return new CheckedLock(lf.makeLock(lockName));
  }

  public synchronized void clearLock(String lockName)
    throws IOException {
    lf.clearLock(lockName);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2283.java