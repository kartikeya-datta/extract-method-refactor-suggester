error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2515.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2515.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2515.java
text:
```scala
D@@irectory baseToDir = getBaseDir(toDir);

package org.apache.solr.core;
/*
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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.NRTCachingDirectory;
import org.apache.lucene.store.RateLimitedDirectoryWrapper;

/**
 * Directory provider which mimics original Solr 
 * {@link org.apache.lucene.store.FSDirectory} based behavior.
 * 
 * File based DirectoryFactory implementations generally extend
 * this class.
 * 
 */
public class StandardDirectoryFactory extends CachingDirectoryFactory {

  @Override
  protected Directory create(String path, DirContext dirContext) throws IOException {
    return FSDirectory.open(new File(path));
  }
  
  @Override
  public String normalize(String path) throws IOException {
    return new File(path).getCanonicalPath();
  }
  
  public boolean isPersistent() {
    return true;
  }
  
  @Override
  public void remove(Directory dir) throws IOException {
    CacheValue val = byDirectoryCache.get(dir);
    if (val == null) {
      throw new IllegalArgumentException("Unknown directory " + dir);
    }
    File dirFile = new File(val.path);
    FileUtils.deleteDirectory(dirFile);
  }
  

  @Override
  public void remove(String path) throws IOException {
    String fullPath = new File(path).getAbsolutePath();
    File dirFile = new File(fullPath);
    FileUtils.deleteDirectory(dirFile);
  }
  
  /**
   * Override for more efficient moves.
   * 
   * Intended for use with replication - use
   * carefully - some Directory wrappers will
   * cache files for example.
   * 
   * This implementation works with two wrappers:
   * NRTCachingDirectory and RateLimitedDirectoryWrapper.
   * 
   * You should first {@link Directory#sync(java.util.Collection)} any file that will be 
   * moved or avoid cached files through settings.
   * 
   * @throws IOException
   *           If there is a low-level I/O error.
   */
  @Override
  public void move(Directory fromDir, Directory toDir, String fileName, IOContext ioContext)
      throws IOException {
    
    Directory baseFromDir = getBaseDir(fromDir);
    Directory baseToDir = getBaseDir(fromDir);
    
    if (baseFromDir instanceof FSDirectory && baseToDir instanceof FSDirectory) {
      File dir1 = ((FSDirectory) baseFromDir).getDirectory();
      File dir2 = ((FSDirectory) baseToDir).getDirectory();
      File indexFileInTmpDir = new File(dir1, fileName);
      File indexFileInIndex = new File(dir2, fileName);
      boolean success = indexFileInTmpDir.renameTo(indexFileInIndex);
      if (success) {
        return;
      }
    }

    super.move(fromDir, toDir, fileName, ioContext);
  }

  // special hack to work with NRTCachingDirectory and RateLimitedDirectoryWrapper
  private Directory getBaseDir(Directory dir) {
    Directory baseDir;
    if (dir instanceof NRTCachingDirectory) {
      baseDir = ((NRTCachingDirectory)dir).getDelegate();
    } else if (dir instanceof RateLimitedDirectoryWrapper) {
      baseDir = ((RateLimitedDirectoryWrapper)dir).getDelegate();
    } else {
      baseDir = dir;
    }
    
    return baseDir;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2515.java