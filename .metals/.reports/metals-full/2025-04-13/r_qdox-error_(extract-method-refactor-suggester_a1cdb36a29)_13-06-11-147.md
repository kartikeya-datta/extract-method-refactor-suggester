error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/571.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/571.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/571.java
text:
```scala
n@@ewIndexWriter(core, true, false);

package org.apache.solr.update;

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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.AlreadyClosedException;
import org.apache.solr.cloud.RecoveryStrategy;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.CoreDescriptor;
import org.apache.solr.core.DirectoryFactory;
import org.apache.solr.core.SolrCore;
import org.apache.solr.util.RefCounted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DefaultSolrCoreState extends SolrCoreState implements RecoveryStrategy.RecoveryListener {
  public static Logger log = LoggerFactory.getLogger(DefaultSolrCoreState.class);
  
  private final boolean SKIP_AUTO_RECOVERY = Boolean.getBoolean("solrcloud.skip.autorecovery");
  
  private final Object recoveryLock = new Object();
  
  // protects pauseWriter and writerFree
  private final Object writerPauseLock = new Object();
  
  private SolrIndexWriter indexWriter = null;
  private DirectoryFactory directoryFactory;

  private volatile boolean recoveryRunning;
  private RecoveryStrategy recoveryStrat;
  private volatile boolean closed = false;

  private RefCounted<IndexWriter> refCntWriter;

  private boolean pauseWriter;
  private boolean writerFree = true;
  
  protected final ReentrantLock commitLock = new ReentrantLock();

  public DefaultSolrCoreState(DirectoryFactory directoryFactory) {
    this.directoryFactory = directoryFactory;
  }
  
  private void closeIndexWriter(IndexWriterCloser closer) {
    try {
      log.info("SolrCoreState ref count has reached 0 - closing IndexWriter");
      if (closer != null) {
        log.info("closing IndexWriter with IndexWriterCloser");
        closer.closeWriter(indexWriter);
      } else if (indexWriter != null) {
        log.info("closing IndexWriter...");
        indexWriter.close();
      }
    } catch (Throwable t) {
      log.error("Error during shutdown of writer.", t);
    } 
  }
  
  @Override
  public RefCounted<IndexWriter> getIndexWriter(SolrCore core)
      throws IOException {
    
    if (closed) {
      throw new SolrException(ErrorCode.SERVICE_UNAVAILABLE, "SolrCoreState already closed");
    }
    
    synchronized (writerPauseLock) {
      if (core == null) {
        // core == null is a signal to just return the current writer, or null
        // if none.
        if (refCntWriter != null) refCntWriter.incref();
        return refCntWriter;
      }
      
      while (pauseWriter) {
        try {
          writerPauseLock.wait(100);
        } catch (InterruptedException e) {}
        
        if (closed) {
          throw new SolrException(ErrorCode.SERVICE_UNAVAILABLE, "Already closed");
        }
      }
      
      if (indexWriter == null) {
        indexWriter = createMainIndexWriter(core, "DirectUpdateHandler2", false);
      }
      initRefCntWriter();
      writerFree = false;
      writerPauseLock.notifyAll();
      refCntWriter.incref();
      return refCntWriter;
    }
  }

  private void initRefCntWriter() {
    if (refCntWriter == null) {
      refCntWriter = new RefCounted<IndexWriter>(indexWriter) {
        @Override
        public void close() {
          synchronized (writerPauseLock) {
            writerFree = true;
            writerPauseLock.notifyAll();
          }
        }
      };
    }
  }

  @Override
  public synchronized void newIndexWriter(SolrCore core, boolean rollback, boolean forceNewDir) throws IOException {
    if (closed) {
      throw new AlreadyClosedException("SolrCoreState already closed");
    }
    log.info("Creating new IndexWriter...");
    String coreName = core.getName();
    synchronized (writerPauseLock) {
      // we need to wait for the Writer to fall out of use
      // first lets stop it from being lent out
      pauseWriter = true;
      // then lets wait until its out of use
      log.info("Waiting until IndexWriter is unused... core=" + coreName);
      
      while (!writerFree) {
        try {
          writerPauseLock.wait(100);
        } catch (InterruptedException e) {}
        
        if (closed) {
          throw new SolrException(ErrorCode.SERVICE_UNAVAILABLE, "SolrCoreState already closed");
        }
      }

      try {
        if (indexWriter != null) {
          if (!rollback) {
            try {
              log.info("Closing old IndexWriter... core=" + coreName);
              indexWriter.close();
            } catch (Throwable t) {
              SolrException.log(log, "Error closing old IndexWriter. core="
                  + coreName, t);
            }
          } else {
            try {
              log.info("Rollback old IndexWriter... core=" + coreName);
              indexWriter.rollback();
            } catch (Throwable t) {
              SolrException.log(log, "Error rolling back old IndexWriter. core="
                  + coreName, t);
            }
          }
        }
        indexWriter = createMainIndexWriter(core, "DirectUpdateHandler2", forceNewDir);
        log.info("New IndexWriter is ready to be used.");
        // we need to null this so it picks up the new writer next get call
        refCntWriter = null;
      } finally {
        
        pauseWriter = false;
        writerPauseLock.notifyAll();
      }
    }
  }

  @Override
  public synchronized void rollbackIndexWriter(SolrCore core) throws IOException {
    newIndexWriter(core, true, true);
  }
  
  protected SolrIndexWriter createMainIndexWriter(SolrCore core, String name, boolean forceNewDirectory) throws IOException {
    return SolrIndexWriter.create(name, core.getNewIndexDir(),
        core.getDirectoryFactory(), false, core.getSchema(),
        core.getSolrConfig().indexConfig, core.getDeletionPolicy(), core.getCodec(), forceNewDirectory);
  }

  @Override
  public DirectoryFactory getDirectoryFactory() {
    return directoryFactory;
  }

  @Override
  public void doRecovery(CoreContainer cc, CoreDescriptor cd) {
    if (SKIP_AUTO_RECOVERY) {
      log.warn("Skipping recovery according to sys prop solrcloud.skip.autorecovery");
      return;
    }
    
    // check before we grab the lock
    if (cc.isShutDown()) {
      log.warn("Skipping recovery because Solr is shutdown");
      return;
    }
    
    synchronized (recoveryLock) {
      // to be air tight we must also check after lock
      if (cc.isShutDown()) {
        log.warn("Skipping recovery because Solr is shutdown");
        return;
      }
      log.info("Running recovery - first canceling any ongoing recovery");
      cancelRecovery();
      
      while (recoveryRunning) {
        try {
          recoveryLock.wait(1000);
        } catch (InterruptedException e) {

        }
        // check again for those that were waiting
        if (cc.isShutDown()) {
          log.warn("Skipping recovery because Solr is shutdown");
          return;
        }
        if (closed) return;
      }

      // if true, we are recovering after startup and shouldn't have (or be receiving) additional updates (except for local tlog recovery)
      boolean recoveringAfterStartup = recoveryStrat == null;

      recoveryStrat = new RecoveryStrategy(cc, cd, this);
      recoveryStrat.setRecoveringAfterStartup(recoveringAfterStartup);
      recoveryStrat.start();
      recoveryRunning = true;
    }
    
  }
  
  @Override
  public void cancelRecovery() {
    synchronized (recoveryLock) {
      if (recoveryStrat != null && recoveryRunning) {
        recoveryStrat.close();
        while (true) {
          try {
            recoveryStrat.join();
          } catch (InterruptedException e) {
            // not interruptible - keep waiting
            continue;
          }
          break;
        }
        
        recoveryRunning = false;
        recoveryLock.notifyAll();
      }
    }
  }

  @Override
  public void recovered() {
    recoveryRunning = false;
  }

  @Override
  public void failed() {
    recoveryRunning = false;
  }

  @Override
  public synchronized void close(IndexWriterCloser closer) {
    closed = true;
    cancelRecovery();
    closeIndexWriter(closer);
  }
  
  @Override
  public Lock getCommitLock() {
    return commitLock;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/571.java