error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2743.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2743.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2743.java
text:
```scala
s@@cheduler.shutdownNow();

package org.apache.solr.update;

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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.request.SolrQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for tracking autoCommit state.
 * 
 * Note: This is purely an implementation detail of autoCommit and will
 * definitely change in the future, so the interface should not be relied-upon
 * 
 * Note: all access must be synchronized.
 */
final class CommitTracker implements Runnable {
  protected final static Logger log = LoggerFactory.getLogger(CommitTracker.class);
  
  // scheduler delay for maxDoc-triggered autocommits
  public final int DOC_COMMIT_DELAY_MS = 250;
  
  // settings, not final so we can change them in testing
  int docsUpperBound;
  long timeUpperBound;
  
  private final ScheduledExecutorService scheduler = Executors
      .newScheduledThreadPool(1);
  private ScheduledFuture pending;
  
  // state
  long docsSinceCommit;
  int autoCommitCount = 0;
  long lastAddedTime = -1;
  
  private SolrCore core;

  private boolean softCommit;
  private boolean waitSearcher;

  private String name;
  
  public CommitTracker(String name, SolrCore core, int docsUpperBound, int timeUpperBound, boolean waitSearcher, boolean softCommit) {
    this.core = core;
    this.name = name;
    docsSinceCommit = 0;
    pending = null;
    
    this.docsUpperBound = docsUpperBound;
    this.timeUpperBound = timeUpperBound;
    
    this.softCommit = softCommit;
    this.waitSearcher = waitSearcher;

    SolrCore.log.info(name + " AutoCommit: " + this);
  }
  
  public void close() {
    if (pending != null) {
      pending.cancel(true);
      pending = null;
    }
    scheduler.shutdown();
  }
  
  /** schedule individual commits */
  public synchronized void scheduleCommitWithin(long commitMaxTime) {
    _scheduleCommitWithin(commitMaxTime);
  }
  
  private void _scheduleCommitWithin(long commitMaxTime) {
    // Check if there is a commit already scheduled for longer then this time
    if (pending != null
        && pending.getDelay(TimeUnit.MILLISECONDS) >= commitMaxTime) {
      pending.cancel(false);
      pending = null;
    }
    
    // schedule a new commit
    if (pending == null) {
      pending = scheduler.schedule(this, commitMaxTime, TimeUnit.MILLISECONDS);
    }
  }
  
  /**
   * Indicate that documents have been added
   */
  public boolean addedDocument(int commitWithin) {
    docsSinceCommit++;
    lastAddedTime = System.currentTimeMillis();
    boolean triggered = false;
    // maxDocs-triggered autoCommit
    if (docsUpperBound > 0 && (docsSinceCommit > docsUpperBound)) {
      _scheduleCommitWithin(DOC_COMMIT_DELAY_MS);
      triggered = true;
    }
    
    // maxTime-triggered autoCommit
    long ctime = (commitWithin > 0) ? commitWithin : timeUpperBound;
    if (ctime > 0) {
      _scheduleCommitWithin(ctime);
      triggered = true;
    }

    return triggered;
  }
  
  /** Inform tracker that a commit has occurred, cancel any pending commits */
  public void didCommit() {
    if (pending != null) {
      pending.cancel(false);
      pending = null; // let it start another one
    }
    docsSinceCommit = 0;
  }
  
  /** Inform tracker that a rollback has occurred, cancel any pending commits */
  public void didRollback() {
    if (pending != null) {
      pending.cancel(false);
      pending = null; // let it start another one
    }
    docsSinceCommit = 0;
  }
  
  /** This is the worker part for the ScheduledFuture **/
  public synchronized void run() {
    long started = System.currentTimeMillis();
    SolrQueryRequest req = new LocalSolrQueryRequest(core,
        new ModifiableSolrParams());
    try {
      CommitUpdateCommand command = new CommitUpdateCommand(req, false);
      command.waitSearcher = waitSearcher;
      command.softCommit = softCommit;
      // no need for command.maxOptimizeSegments = 1; since it is not optimizing
      core.getUpdateHandler().commit(command);
      autoCommitCount++;
    } catch (Exception e) {
      log.error("auto commit error...");
      e.printStackTrace();
    } finally {
      pending = null;
      req.close();
    }
    
    // check if docs have been submitted since the commit started
    if (lastAddedTime > started) {
      if (docsUpperBound > 0 && docsSinceCommit > docsUpperBound) {
        pending = scheduler.schedule(this, 100, TimeUnit.MILLISECONDS);
      } else if (timeUpperBound > 0) {
        pending = scheduler.schedule(this, timeUpperBound,
            TimeUnit.MILLISECONDS);
      }
    }
  }
  
  // to facilitate testing: blocks if called during commit
  public synchronized int getCommitCount() {
    return autoCommitCount;
  }
  
  @Override
  public String toString() {
    if (timeUpperBound > 0 || docsUpperBound > 0) {
      return (timeUpperBound > 0 ? ("if uncommited for " + timeUpperBound + "ms; ")
          : "")
          + (docsUpperBound > 0 ? ("if " + docsUpperBound + " uncommited docs ")
              : "");
      
    } else {
      return "disabled";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2743.java