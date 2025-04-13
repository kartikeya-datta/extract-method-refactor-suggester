error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1534.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1534.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1534.java
text:
```scala
r@@eturn (int) (getDirectory().hashCode() + getVersion());

package org.apache.lucene.index;

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

import java.util.Collection;
import java.util.Map;
import java.io.IOException;
import org.apache.lucene.store.Directory;

/**
 * <p>Expert: represents a single commit into an index as seen by the
 * {@link IndexDeletionPolicy} or {@link IndexReader}.</p>
 *
 * <p> Changes to the content of an index are made visible
 * only after the writer who made that change commits by
 * writing a new segments file
 * (<code>segments_N</code>). This point in time, when the
 * action of writing of a new segments file to the directory
 * is completed, is an index commit.</p>
 *
 * <p>Each index commit point has a unique segments file
 * associated with it. The segments file associated with a
 * later index commit point would have a larger N.</p>
 *
 * @lucene.experimental
*/

public abstract class IndexCommit {

  /**
   * Get the segments file (<code>segments_N</code>) associated 
   * with this commit point.
   */
  public abstract String getSegmentsFileName();

  /**
   * Returns all index files referenced by this commit point.
   */
  public abstract Collection<String> getFileNames() throws IOException;

  /**
   * Returns the {@link Directory} for the index.
   */
  public abstract Directory getDirectory();
  
  /**
   * Delete this commit point.  This only applies when using
   * the commit point in the context of IndexWriter's
   * IndexDeletionPolicy.
   * <p>
   * Upon calling this, the writer is notified that this commit 
   * point should be deleted. 
   * <p>
   * Decision that a commit-point should be deleted is taken by the {@link IndexDeletionPolicy} in effect
   * and therefore this should only be called by its {@link IndexDeletionPolicy#onInit onInit()} or 
   * {@link IndexDeletionPolicy#onCommit onCommit()} methods.
  */
  public void delete() {
    throw new UnsupportedOperationException("This IndexCommit does not support this method.");
  }

  public boolean isDeleted() {
    throw new UnsupportedOperationException("This IndexCommit does not support this method.");
  }

  /**
   * Returns true if this commit is an optimized index.
   */
  public boolean isOptimized() {
    throw new UnsupportedOperationException("This IndexCommit does not support this method.");
  }

  /**
   * Two IndexCommits are equal if both their Directory and versions are equal.
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof IndexCommit) {
      IndexCommit otherCommit = (IndexCommit) other;
      return otherCommit.getDirectory().equals(getDirectory()) && otherCommit.getVersion() == getVersion();
    } else
      return false;
  }

  @Override
  public int hashCode() {
    return getDirectory().hashCode() + getSegmentsFileName().hashCode();
  }

  /** Returns the version for this IndexCommit.  This is the
   *  same value that {@link IndexReader#getVersion} would
   *  return if it were opened on this commit. */
  public long getVersion() {
    throw new UnsupportedOperationException("This IndexCommit does not support this method.");
  }

  /** Returns the generation (the _N in segments_N) for this
   *  IndexCommit */
  public long getGeneration() {
    throw new UnsupportedOperationException("This IndexCommit does not support this method.");
  }

  /** Convenience method that returns the last modified time
   *  of the segments_N file corresponding to this index
   *  commit, equivalent to
   *  getDirectory().fileModified(getSegmentsFileName()). */
  public long getTimestamp() throws IOException {
    return getDirectory().fileModified(getSegmentsFileName());
  }

  /** Returns userData, previously passed to {@link
   *  IndexWriter#commit(Map)} for this commit.  Map is
   *  String -> String. */
  public Map<String,String> getUserData() throws IOException {
    throw new UnsupportedOperationException("This IndexCommit does not support this method.");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1534.java