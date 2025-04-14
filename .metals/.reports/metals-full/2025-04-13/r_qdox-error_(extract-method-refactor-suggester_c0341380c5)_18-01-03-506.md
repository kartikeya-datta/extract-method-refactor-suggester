error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/741.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/741.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/741.java
text:
```scala
public v@@oid close() throws IOException {

package org.apache.solr.store.hdfs;

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

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileAlreadyExistsException;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.ipc.RemoteException;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.LockReleaseFailedException;
import org.apache.solr.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HdfsLockFactory extends LockFactory {
  public static Logger log = LoggerFactory.getLogger(HdfsLockFactory.class);
  
  private Path lockPath;
  private Configuration configuration;
  
  public HdfsLockFactory(Path lockPath, Configuration configuration) {
    this.lockPath = lockPath;
    this.configuration = configuration;
  }
  
  @Override
  public Lock makeLock(String lockName) {
    
    if (lockPrefix != null) {
      lockName = lockPrefix + "-" + lockName;
    }
    
    HdfsLock lock = new HdfsLock(lockPath, lockName, configuration);
    
    return lock;
  }
  
  @Override
  public void clearLock(String lockName) throws IOException {
    FileSystem fs = null;
    try {
      fs = FileSystem.newInstance(lockPath.toUri(), configuration);
      while (true) {
        if (fs.exists(lockPath)) {
          if (lockPrefix != null) {
            lockName = lockPrefix + "-" + lockName;
          }
          
          Path lockFile = new Path(lockPath, lockName);
          try {
            if (fs.exists(lockFile) && !fs.delete(lockFile, false)) {
              throw new IOException("Cannot delete " + lockFile);
            }
          } catch (RemoteException e) {
            if (e.getClassName().equals(
                "org.apache.hadoop.hdfs.server.namenode.SafeModeException")) {
              log.warn("The NameNode is in SafeMode - Solr will wait 5 seconds and try again.");
              try {
                Thread.sleep(5000);
              } catch (InterruptedException e1) {
                Thread.interrupted();
              }
              continue;
            }
            throw e;
          }
          break;
        }
      }
    } finally {
      IOUtils.closeQuietly(fs);
    }
  }
  
  public Path getLockPath() {
    return lockPath;
  }
  
  public void setLockPath(Path lockPath) {
    this.lockPath = lockPath;
  }
  
  static class HdfsLock extends Lock {
    
    private Path lockPath;
    private String lockName;
    private Configuration conf;
    
    public HdfsLock(Path lockPath, String lockName, Configuration conf) {
      this.lockPath = lockPath;
      this.lockName = lockName;
      this.conf = conf;
    }
    
    @Override
    public boolean obtain() throws IOException {
      FSDataOutputStream file = null;
      FileSystem fs = FileSystem.newInstance(lockPath.toUri(), conf);
      try {
        while (true) {
          try {
            if (!fs.exists(lockPath)) {
              boolean success = fs.mkdirs(lockPath);
              if (!success) {
                throw new RuntimeException("Could not create directory: " + lockPath);
              }
            } else {
              // just to check for safe mode
              fs.mkdirs(lockPath);
            }

            
            file = fs.create(new Path(lockPath, lockName), false);
            break;
          } catch (FileAlreadyExistsException e) {
            return false;
          } catch (RemoteException e) {
            if (e.getClassName().equals(
                "org.apache.hadoop.hdfs.server.namenode.SafeModeException")) {
              log.warn("The NameNode is in SafeMode - Solr will wait 5 seconds and try again.");
              try {
                Thread.sleep(5000);
              } catch (InterruptedException e1) {
                Thread.interrupted();
              }
              continue;
            }
            log.error("Error creating lock file", e);
            return false;
          } catch (IOException e) {
            log.error("Error creating lock file", e);
            return false;
          } finally {
            IOUtils.closeQuietly(file);
          }
        }
      } finally {
        IOUtils.closeQuietly(fs);
      }
      return true;
    }
    
    @Override
    public void release() throws IOException {
      FileSystem fs = FileSystem.newInstance(lockPath.toUri(), conf);
      try {
        if (fs.exists(new Path(lockPath, lockName))
            && !fs.delete(new Path(lockPath, lockName), false)) throw new LockReleaseFailedException(
            "failed to delete " + new Path(lockPath, lockName));
      } finally {
        IOUtils.closeQuietly(fs);
      }
    }
    
    @Override
    public boolean isLocked() throws IOException {
      boolean isLocked = false;
      FileSystem fs = FileSystem.newInstance(lockPath.toUri(), conf);
      try {
        isLocked = fs.exists(new Path(lockPath, lockName));
      } finally {
        IOUtils.closeQuietly(fs);
      }
      return isLocked;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/741.java