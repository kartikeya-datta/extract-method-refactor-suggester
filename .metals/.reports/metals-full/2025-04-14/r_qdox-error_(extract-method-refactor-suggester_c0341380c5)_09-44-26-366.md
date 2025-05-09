error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/178.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/178.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,20]

error in qdox parser
file content:
```java
offset: 20
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/178.java
text:
```scala
final synchronized l@@ong getRecomputedActualSizeInBytes() {

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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * This is a subclass of RAMDirectory that adds methods
 * intented to be used only by unit tests.
 * @version $Id: RAMDirectory.java 437897 2006-08-29 01:13:10Z yonik $
 */

public class MockRAMDirectory extends RAMDirectory {
  long maxSize;

  // Max actual bytes used. This is set by MockRAMOutputStream:
  long maxUsedSize;
  double randomIOExceptionRate;
  Random randomState;
  boolean noDeleteOpenFile = true;

  // NOTE: we cannot initialize the Map here due to the
  // order in which our constructor actually does this
  // member initialization vs when it calls super.  It seems
  // like super is called, then our members are initialized:
  Map openFiles;

  public MockRAMDirectory() throws IOException {
    super();
    if (openFiles == null) {
      openFiles = new HashMap();
    }
  }
  public MockRAMDirectory(String dir) throws IOException {
    super(dir);
    if (openFiles == null) {
      openFiles = new HashMap();
    }
  }
  public MockRAMDirectory(Directory dir) throws IOException {
    super(dir);
    if (openFiles == null) {
      openFiles = new HashMap();
    }
  }
  public MockRAMDirectory(File dir) throws IOException {
    super(dir);
    if (openFiles == null) {
      openFiles = new HashMap();
    }
  }

  public void setMaxSizeInBytes(long maxSize) {
    this.maxSize = maxSize;
  }
  public long getMaxSizeInBytes() {
    return this.maxSize;
  }

  /**
   * Returns the peek actual storage used (bytes) in this
   * directory.
   */
  public long getMaxUsedSizeInBytes() {
    return this.maxUsedSize;
  }
  public void resetMaxUsedSizeInBytes() {
    this.maxUsedSize = getRecomputedActualSizeInBytes();
  }

  /**
   * Emulate windows whereby deleting an open file is not
   * allowed (raise IOException).
  */
  public void setNoDeleteOpenFile(boolean value) {
    this.noDeleteOpenFile = value;
  }
  public boolean getNoDeleteOpenFile() {
    return noDeleteOpenFile;
  }

  /**
   * If 0.0, no exceptions will be thrown.  Else this should
   * be a double 0.0 - 1.0.  We will randomly throw an
   * IOException on the first write to an OutputStream based
   * on this probability.
   */
  public void setRandomIOExceptionRate(double rate, long seed) {
    randomIOExceptionRate = rate;
    // seed so we have deterministic behaviour:
    randomState = new Random(seed);
  }
  public double getRandomIOExceptionRate() {
    return randomIOExceptionRate;
  }

  void maybeThrowIOException() throws IOException {
    maybeThrowDeterministicException();
    if (randomIOExceptionRate > 0.0) {
      int number = Math.abs(randomState.nextInt() % 1000);
      if (number < randomIOExceptionRate*1000) {
        throw new IOException("a random IOException");
      }
    }
  }

  public synchronized void deleteFile(String name) throws IOException {
    synchronized(openFiles) {
      if (noDeleteOpenFile && openFiles.containsKey(name)) {
        throw new IOException("MockRAMDirectory: file \"" + name + "\" is still open: cannot delete");
      }
    }
    super.deleteFile(name);
  }

  public IndexOutput createOutput(String name) {
    if (openFiles == null) {
      openFiles = new HashMap();
    }
    synchronized(openFiles) {
      if (noDeleteOpenFile && openFiles.containsKey(name)) {
        // RuntimeException instead of IOException because
        // super() does not throw IOException currently:
        throw new RuntimeException("MockRAMDirectory: file \"" + name + "\" is still open: cannot overwrite");
      }
    }
    RAMFile file = new RAMFile(this);
    synchronized (this) {
      RAMFile existing = (RAMFile)fileMap.get(name);
      if (existing!=null) {
        sizeInBytes -= existing.sizeInBytes;
        existing.directory = null;
      }
      fileMap.put(name, file);
    }

    return new MockRAMOutputStream(this, file);
  }

  public IndexInput openInput(String name) throws IOException {
    RAMFile file;
    synchronized (this) {
      file = (RAMFile)fileMap.get(name);
    }
    if (file == null)
      throw new FileNotFoundException(name);
    else {
      synchronized(openFiles) {
        if (openFiles.containsKey(name)) {
          Integer v = (Integer) openFiles.get(name);
          v = new Integer(v.intValue()+1);
          openFiles.put(name, v);
        } else {
          openFiles.put(name, new Integer(1));
        }
      }
    }
    return new MockRAMInputStream(this, name, file);
  }

  /** Provided for testing purposes.  Use sizeInBytes() instead. */
  public synchronized final long getRecomputedSizeInBytes() {
    long size = 0;
    Iterator it = fileMap.values().iterator();
    while (it.hasNext())
      size += ((RAMFile) it.next()).getSizeInBytes();
    return size;
  }

  /** Like getRecomputedSizeInBytes(), but, uses actual file
   * lengths rather than buffer allocations (which are
   * quantized up to nearest
   * RAMOutputStream.BUFFER_SIZE (now 1024) bytes.
   */

  final long getRecomputedActualSizeInBytes() {
    long size = 0;
    Iterator it = fileMap.values().iterator();
    while (it.hasNext())
      size += ((RAMFile) it.next()).length;
    return size;
  }

  public void close() {
    if (openFiles == null) {
      openFiles = new HashMap();
    }
    synchronized(openFiles) {
      if (noDeleteOpenFile && openFiles.size() > 0) {
        // RuntimeException instead of IOException because
        // super() does not throw IOException currently:
        throw new RuntimeException("MockRAMDirectory: cannot close: there are still open files: " + openFiles);
      }
    }
  }

  /**
   * Objects that represent fail-able conditions. Objects of a derived
   * class are created and registered with the mock directory. After
   * register, each object will be invoked once for each first write
   * of a file, giving the object a chance to throw an IOException.
   */
  public static class Failure {
    /**
     * eval is called on the first write of every new file.
     */
    public void eval(MockRAMDirectory dir) throws IOException { }

    /**
     * reset should set the state of the failure to its default
     * (freshly constructed) state. Reset is convenient for tests
     * that want to create one failure object and then reuse it in
     * multiple cases. This, combined with the fact that Failure
     * subclasses are often anonymous classes makes reset difficult to
     * do otherwise.
     *
     * A typical example of use is
     * Failure failure = new Failure() { ... };
     * ...
     * mock.failOn(failure.reset())
     */
    public Failure reset() { return this; }
  }

  ArrayList failures;

  /**
   * add a Failure object to the list of objects to be evaluated
   * at every potential failure point
   */
  public void failOn(Failure fail) {
    if (failures == null) {
      failures = new ArrayList();
    }
    failures.add(fail);
  }

  /**
   * Itterate through the failures list, giving each object a
   * chance to throw an IOE
   */
  void maybeThrowDeterministicException() throws IOException {
    if (failures != null) {
      for(int i = 0; i < failures.size(); i++) {
        ((Failure)failures.get(i)).eval(this);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/178.java