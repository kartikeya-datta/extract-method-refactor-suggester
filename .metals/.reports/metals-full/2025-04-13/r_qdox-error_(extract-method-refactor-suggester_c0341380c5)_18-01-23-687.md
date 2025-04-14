error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1297.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1297.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1297.java
text:
```scala
public v@@oid close() {

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
import java.io.FileNotFoundException;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * A memory-resident {@link Directory} implementation.  Locking
 * implementation is by default the {@link SingleInstanceLockFactory}
 * but can be changed with {@link #setLockFactory}.
 *
 * @version $Id$
 */
public class RAMDirectory extends Directory implements Serializable {

  private static final long serialVersionUID = 1l;

  HashMap fileMap = new HashMap();
  long sizeInBytes = 0;
  
  // *****
  // Lock acquisition sequence:  RAMDirectory, then RAMFile
  // *****

  /** Constructs an empty {@link Directory}. */
  public RAMDirectory() {
    setLockFactory(new SingleInstanceLockFactory());
  }

  /**
   * Creates a new <code>RAMDirectory</code> instance from a different
   * <code>Directory</code> implementation.  This can be used to load
   * a disk-based index into memory.
   * <P>
   * This should be used only with indices that can fit into memory.
   * <P>
   * Note that the resulting <code>RAMDirectory</code> instance is fully
   * independent from the original <code>Directory</code> (it is a
   * complete copy).  Any subsequent changes to the
   * original <code>Directory</code> will not be visible in the
   * <code>RAMDirectory</code> instance.
   *
   * @param dir a <code>Directory</code> value
   * @exception IOException if an error occurs
   */
  public RAMDirectory(Directory dir) throws IOException {
    this(dir, false);
  }
  
  private RAMDirectory(Directory dir, boolean closeDir) throws IOException {
    this();
    Directory.copy(dir, this, closeDir);
  }

  /**
   * Creates a new <code>RAMDirectory</code> instance from the {@link FSDirectory}.
   *
   * @param dir a <code>File</code> specifying the index directory
   *
   * @see #RAMDirectory(Directory)
   */
  public RAMDirectory(File dir) throws IOException {
    this(FSDirectory.getDirectory(dir), true);
  }

  /**
   * Creates a new <code>RAMDirectory</code> instance from the {@link FSDirectory}.
   *
   * @param dir a <code>String</code> specifying the full index directory path
   *
   * @see #RAMDirectory(Directory)
   */
  public RAMDirectory(String dir) throws IOException {
    this(FSDirectory.getDirectory(dir), true);
  }

  /** Returns an array of strings, one for each file in the directory. */
  public synchronized final String[] list() {
    Set fileNames = fileMap.keySet();
    String[] result = new String[fileNames.size()];
    int i = 0;
    Iterator it = fileNames.iterator();
    while (it.hasNext())
      result[i++] = (String)it.next();
    return result;
  }

  /** Returns true iff the named file exists in this directory. */
  public final boolean fileExists(String name) {
    RAMFile file;
    synchronized (this) {
      file = (RAMFile)fileMap.get(name);
    }
    return file != null;
  }

  /** Returns the time the named file was last modified.
   * @throws IOException if the file does not exist
   */
  public final long fileModified(String name) throws IOException {
    RAMFile file;
    synchronized (this) {
      file = (RAMFile)fileMap.get(name);
    }
    if (file==null)
      throw new FileNotFoundException(name);
    return file.getLastModified();
  }

  /** Set the modified time of an existing file to now.
   * @throws IOException if the file does not exist
   */
  public void touchFile(String name) throws IOException {
    RAMFile file;
    synchronized (this) {
      file = (RAMFile)fileMap.get(name);
    }
    if (file==null)
      throw new FileNotFoundException(name);
    
    long ts2, ts1 = System.currentTimeMillis();
    do {
      try {
        Thread.sleep(0, 1);
      } catch (InterruptedException e) {}
      ts2 = System.currentTimeMillis();
    } while(ts1 == ts2);
    
    file.setLastModified(ts2);
  }

  /** Returns the length in bytes of a file in the directory.
   * @throws IOException if the file does not exist
   */
  public final long fileLength(String name) throws IOException {
    RAMFile file;
    synchronized (this) {
      file = (RAMFile)fileMap.get(name);
    }
    if (file==null)
      throw new FileNotFoundException(name);
    return file.getLength();
  }
  
  /** Return total size in bytes of all files in this
   * directory.  This is currently quantized to
   * BufferedIndexOutput.BUFFER_SIZE. */
  public synchronized final long sizeInBytes() {
    return sizeInBytes;
  }
  
  /** Removes an existing file in the directory.
   * @throws IOException if the file does not exist
   */
  public synchronized void deleteFile(String name) throws IOException {
    RAMFile file = (RAMFile)fileMap.get(name);
    if (file!=null) {
        fileMap.remove(name);
        file.directory = null;
        sizeInBytes -= file.sizeInBytes;       // updates to RAMFile.sizeInBytes synchronized on directory
    } else
      throw new FileNotFoundException(name);
  }

  /** Renames an existing file in the directory.
   * @throws FileNotFoundException if from does not exist
   * @deprecated
   */
  public synchronized final void renameFile(String from, String to) throws IOException {
    RAMFile fromFile = (RAMFile)fileMap.get(from);
    if (fromFile==null)
      throw new FileNotFoundException(from);
    RAMFile toFile = (RAMFile)fileMap.get(to);
    if (toFile!=null) {
      sizeInBytes -= toFile.sizeInBytes;       // updates to RAMFile.sizeInBytes synchronized on directory
      toFile.directory = null;
    }
    fileMap.remove(from);
    fileMap.put(to, fromFile);
  }

  /** Creates a new, empty file in the directory with the given name. Returns a stream writing this file. */
  public IndexOutput createOutput(String name) {
    RAMFile file = new RAMFile(this);
    synchronized (this) {
      RAMFile existing = (RAMFile)fileMap.get(name);
      if (existing!=null) {
        sizeInBytes -= existing.sizeInBytes;
        existing.directory = null;
      }
      fileMap.put(name, file);
    }
    return new RAMOutputStream(file);
  }

  /** Returns a stream reading an existing file. */
  public IndexInput openInput(String name) throws IOException {
    RAMFile file;
    synchronized (this) {
      file = (RAMFile)fileMap.get(name);
    }
    if (file == null)
      throw new FileNotFoundException(name);
    return new RAMInputStream(file);
  }

  /** Closes the store to future operations, releasing associated memory. */
  public final void close() {
    fileMap = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1297.java