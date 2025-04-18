error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/905.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/905.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/905.java
text:
```scala
s@@uccess = syncStrategy.sync(zkController, core, leaderProps, weAreReplacement);

package org.apache.solr.cloud;

import java.io.IOException;
import java.util.Map;

import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.cloud.ClusterState;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.solr.common.cloud.ZkCmdExecutor;
import org.apache.solr.common.cloud.ZkCoreNodeProps;
import org.apache.solr.common.cloud.ZkNodeProps;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.update.UpdateLog;
import org.apache.solr.util.RefCounted;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public abstract class ElectionContext {
  private static Logger log = LoggerFactory.getLogger(ElectionContext.class);
  final String electionPath;
  final ZkNodeProps leaderProps;
  final String id;
  final String leaderPath;
  String leaderSeqPath;
  private SolrZkClient zkClient;
  
  public ElectionContext(final String coreNodeName,
      final String electionPath, final String leaderPath, final ZkNodeProps leaderProps, final SolrZkClient zkClient) {
    this.id = coreNodeName;
    this.electionPath = electionPath;
    this.leaderPath = leaderPath;
    this.leaderProps = leaderProps;
    this.zkClient = zkClient;
  }
  
  public void close() {}
  
  public void cancelElection() throws InterruptedException, KeeperException {
    try {
      zkClient.delete(leaderSeqPath, -1, true);
    } catch (NoNodeException e) {
      // fine
      log.warn("cancelElection did not find election node to remove");
    }
  }

  abstract void runLeaderProcess(boolean weAreReplacement) throws KeeperException, InterruptedException, IOException;

  public void checkIfIamLeaderFired() {}

  public void joinedElectionFired() {}
}

class ShardLeaderElectionContextBase extends ElectionContext {
  private static Logger log = LoggerFactory.getLogger(ShardLeaderElectionContextBase.class);
  protected final SolrZkClient zkClient;
  protected String shardId;
  protected String collection;
  protected LeaderElector leaderElector;

  public ShardLeaderElectionContextBase(LeaderElector leaderElector, final String shardId,
      final String collection, final String coreNodeName, ZkNodeProps props, ZkStateReader zkStateReader) {
    super(coreNodeName, ZkStateReader.COLLECTIONS_ZKNODE + "/" + collection + "/leader_elect/"
        + shardId, ZkStateReader.getShardLeadersPath(collection, shardId),
        props, zkStateReader.getZkClient());
    this.leaderElector = leaderElector;
    this.zkClient = zkStateReader.getZkClient();
    this.shardId = shardId;
    this.collection = collection;
    
    try {
      new ZkCmdExecutor(zkStateReader.getZkClient().getZkClientTimeout()).ensureExists(ZkStateReader.COLLECTIONS_ZKNODE + "/" + collection, zkClient);
    } catch (KeeperException e) {
      throw new SolrException(ErrorCode.SERVER_ERROR, e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new SolrException(ErrorCode.SERVER_ERROR, e);
    }
  }

  @Override
  void runLeaderProcess(boolean weAreReplacement) throws KeeperException,
      InterruptedException, IOException {
    
    zkClient.makePath(leaderPath, ZkStateReader.toJSON(leaderProps),
        CreateMode.EPHEMERAL, true);
    assert shardId != null;
    ZkNodeProps m = ZkNodeProps.fromKeyVals(Overseer.QUEUE_OPERATION, ZkStateReader.LEADER_PROP,
        ZkStateReader.SHARD_ID_PROP, shardId, ZkStateReader.COLLECTION_PROP,
        collection, ZkStateReader.BASE_URL_PROP, leaderProps.getProperties()
            .get(ZkStateReader.BASE_URL_PROP), ZkStateReader.CORE_NAME_PROP,
        leaderProps.getProperties().get(ZkStateReader.CORE_NAME_PROP),
        ZkStateReader.STATE_PROP, ZkStateReader.ACTIVE);
    Overseer.getInQueue(zkClient).offer(ZkStateReader.toJSON(m));
    
  }

}

// add core container and stop passing core around...
final class ShardLeaderElectionContext extends ShardLeaderElectionContextBase {
  private static Logger log = LoggerFactory.getLogger(ShardLeaderElectionContext.class);
  
  private final ZkController zkController;
  private final CoreContainer cc;
  private final SyncStrategy syncStrategy;

  private volatile boolean isClosed = false;
  
  public ShardLeaderElectionContext(LeaderElector leaderElector, 
      final String shardId, final String collection,
      final String coreNodeName, ZkNodeProps props, ZkController zkController, CoreContainer cc) {
    super(leaderElector, shardId, collection, coreNodeName, props,
        zkController.getZkStateReader());
    this.zkController = zkController;
    this.cc = cc;
    syncStrategy = new SyncStrategy(cc.getUpdateShardHandler());
  }
  
  @Override
  public void close() {
    this.isClosed  = true;
    syncStrategy.close();
  }
  
  /* 
   * weAreReplacement: has someone else been the leader already?
   */
  @Override
  void runLeaderProcess(boolean weAreReplacement) throws KeeperException,
      InterruptedException, IOException {
    log.info("Running the leader process for shard " + shardId);
    
    String coreName = leaderProps.getStr(ZkStateReader.CORE_NAME_PROP);
    
    // clear the leader in clusterstate
    ZkNodeProps m = new ZkNodeProps(Overseer.QUEUE_OPERATION, ZkStateReader.LEADER_PROP,
        ZkStateReader.SHARD_ID_PROP, shardId, ZkStateReader.COLLECTION_PROP,
        collection);
    Overseer.getInQueue(zkClient).offer(ZkStateReader.toJSON(m));
    
    int leaderVoteWait = cc.getZkController().getLeaderVoteWait();
    if (!weAreReplacement) {
      waitForReplicasToComeUp(weAreReplacement, leaderVoteWait);
    }
    
    SolrCore core = null;
    try {
      
      core = cc.getCore(coreName);
      
      if (core == null) {
        cancelElection();
        throw new SolrException(ErrorCode.SERVER_ERROR,
            "Fatal Error, SolrCore not found:" + coreName + " in "
                + cc.getCoreNames());
      }
      
      // should I be leader?
      if (weAreReplacement && !shouldIBeLeader(leaderProps, core, weAreReplacement)) {
        rejoinLeaderElection(leaderSeqPath, core);
        return;
      }
      
      log.info("I may be the new leader - try and sync");
 
      
      // we are going to attempt to be the leader
      // first cancel any current recovery
      core.getUpdateHandler().getSolrCoreState().cancelRecovery();
      
      if (weAreReplacement) {
        // wait a moment for any floating updates to finish
        try {
          Thread.sleep(2500);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new SolrException(ErrorCode.SERVICE_UNAVAILABLE, e);
        }
      }
      
      boolean success = false;
      try {
        success = syncStrategy.sync(zkController, core, leaderProps);
      } catch (Exception e) {
        SolrException.log(log, "Exception while trying to sync", e);
        success = false;
      }
      
      UpdateLog ulog = core.getUpdateHandler().getUpdateLog();

      if (!success) {
        boolean hasRecentUpdates = false;
        if (ulog != null) {
          // TODO: we could optimize this if necessary
          UpdateLog.RecentUpdates recentUpdates = ulog.getRecentUpdates();
          try {
            hasRecentUpdates = !recentUpdates.getVersions(1).isEmpty();
          } finally {
            recentUpdates.close();
          }
        }

        if (!hasRecentUpdates) {
          // we failed sync, but we have no versions - we can't sync in that case
          // - we were active
          // before, so become leader anyway
          log.info("We failed sync, but we have no versions - we can't sync in that case - we were active before, so become leader anyway");
          success = true;
        }
      }
      
      // solrcloud_debug
      if (log.isDebugEnabled()) {
        try {
          RefCounted<SolrIndexSearcher> searchHolder = core
              .getNewestSearcher(false);
          SolrIndexSearcher searcher = searchHolder.get();
          try {
            log.debug(core.getCoreDescriptor().getCoreContainer()
                .getZkController().getNodeName()
                + " synched "
                + searcher.search(new MatchAllDocsQuery(), 1).totalHits);
          } finally {
            searchHolder.decref();
          }
        } catch (Exception e) {
          throw new SolrException(ErrorCode.SERVER_ERROR, null, e);
        }
      }
      if (!success) {
        rejoinLeaderElection(leaderSeqPath, core);
        return;
      }

      log.info("I am the new leader: "
          + ZkCoreNodeProps.getCoreUrl(leaderProps) + " " + shardId);
      core.getCoreDescriptor().getCloudDescriptor().setLeader(true);
    } finally {
      if (core != null) {
        core.close();
      }
    }
    boolean success = false;
    try {
      super.runLeaderProcess(weAreReplacement);
      success = true;
    } catch (Exception e) {
      SolrException.log(log, "There was a problem trying to register as the leader", e);
  
      try {
        core = cc.getCore(coreName);
        if (core == null) {
          throw new SolrException(ErrorCode.SERVER_ERROR,
              "Fatal Error, SolrCore not found:" + coreName + " in "
                  + cc.getCoreNames());
        }
        
        core.getCoreDescriptor().getCloudDescriptor().setLeader(false);
        
        // we could not publish ourselves as leader - rejoin election
        rejoinLeaderElection(leaderSeqPath, core);
      } finally {
        try {
          if (!success) {
            cancelElection();
          }
        } finally {
          if (core != null) {
            core.close();
          }
        }
        
      }
    }
    
  }
  
  private boolean areAnyOtherReplicasActive(ZkController zkController,
      ZkNodeProps leaderProps, String collection, String shardId) {
    ClusterState clusterState = zkController.getZkStateReader()
        .getClusterState();
    Map<String,Slice> slices = clusterState.getSlicesMap(collection);
    Slice slice = slices.get(shardId);
    if (!slice.getState().equals(Slice.ACTIVE)) {
      //Return false if the Slice is not active yet.
      return false;
    }
    Map<String,Replica> replicasMap = slice.getReplicasMap();
    for (Map.Entry<String,Replica> shard : replicasMap.entrySet()) {
      String state = shard.getValue().getStr(ZkStateReader.STATE_PROP);
      // System.out.println("state:"
      // + state
      // + shard.getValue().get(ZkStateReader.NODE_NAME_PROP)
      // + " live: "
      // + clusterState.liveNodesContain(shard.getValue().get(
      // ZkStateReader.NODE_NAME_PROP)));
      if (state.equals(ZkStateReader.ACTIVE)
          && clusterState.liveNodesContain(shard.getValue().getStr(
              ZkStateReader.NODE_NAME_PROP))
          && !new ZkCoreNodeProps(shard.getValue()).getCoreUrl().equals(
              new ZkCoreNodeProps(leaderProps).getCoreUrl())) {
        return true;
      }
    }
    
    return false;
  }

  private void waitForReplicasToComeUp(boolean weAreReplacement,
      int timeout) throws InterruptedException {
    long timeoutAt = System.currentTimeMillis() + timeout;
    final String shardsElectZkPath = electionPath + LeaderElector.ELECTION_NODE;
    
    Slice slices = zkController.getClusterState().getSlice(collection, shardId);
    int cnt = 0;
    while (true && !isClosed && !cc.isShutDown()) {
      // wait for everyone to be up
      if (slices != null) {
        int found = 0;
        try {
          found = zkClient.getChildren(shardsElectZkPath, null, true).size();
        } catch (KeeperException e) {
          SolrException.log(log,
              "Error checking for the number of election participants", e);
        }
        
        // on startup and after connection timeout, wait for all known shards
        if (found >= slices.getReplicasMap().size()) {
          log.info("Enough replicas found to continue.");
          return;
        } else {
          if (cnt % 40 == 0) {
            log.info("Waiting until we see more replicas up for shard " + shardId + ": total="
              + slices.getReplicasMap().size() + " found=" + found
              + " timeoutin=" + (timeoutAt - System.currentTimeMillis()));
          }
        }
        
        if (System.currentTimeMillis() > timeoutAt) {
          log.info("Was waiting for replicas to come up, but they are taking too long - assuming they won't come back till later");
          return;
        }
      } else {
        log.warn("Shard not found: " + shardId + " for collection " + collection);

        return;

      }
      
      Thread.sleep(500);
      slices = zkController.getClusterState().getSlice(collection, shardId);
      // System.out.println("###### waitForReplicasToComeUp  : slices=" + slices + " all=" + zkController.getClusterState().getCollectionStates() );
      cnt++;
    }
  }

  private void rejoinLeaderElection(String leaderSeqPath, SolrCore core)
      throws InterruptedException, KeeperException, IOException {
    // remove our ephemeral and re join the election
    if (cc.isShutDown()) {
      log.info("Not rejoining election because CoreContainer is shutdown");
      return;
    }
    
    log.info("There may be a better leader candidate than us - going back into recovery");
    
    cancelElection();
    
    core.getUpdateHandler().getSolrCoreState().doRecovery(cc, core.getCoreDescriptor());
    
    leaderElector.joinElection(this, true);
  }

  private boolean shouldIBeLeader(ZkNodeProps leaderProps, SolrCore core, boolean weAreReplacement) {
    log.info("Checking if I should try and be the leader.");
    
    if (isClosed) {
      log.info("Bailing on leader process because we have been closed");
      return false;
    }
    
    if (!weAreReplacement) {
      // we are the first node starting in the shard - there is a configurable wait
      // to make sure others participate in sync and leader election, we can be leader
      return true;
    }
    
    if (core.getCoreDescriptor().getCloudDescriptor().getLastPublished()
        .equals(ZkStateReader.ACTIVE)) {
      log.info("My last published State was Active, it's okay to be the leader.");
      return true;
    }
    log.info("My last published State was "
        + core.getCoreDescriptor().getCloudDescriptor().getLastPublished()
        + ", I won't be the leader.");
    // TODO: and if no one is a good candidate?
    
    return false;
  }
  
}

final class OverseerElectionContext extends ElectionContext {
  
  private final SolrZkClient zkClient;
  private Overseer overseer;
  public static final String PATH = "/overseer_elect";

  public OverseerElectionContext(SolrZkClient zkClient, Overseer overseer, final String zkNodeName) {
    super(zkNodeName,PATH , PATH+"/leader", null, zkClient);
    this.overseer = overseer;
    this.zkClient = zkClient;
    try {
      new ZkCmdExecutor(zkClient.getZkClientTimeout()).ensureExists("/overseer_elect", zkClient);
    } catch (KeeperException e) {
      throw new SolrException(ErrorCode.SERVER_ERROR, e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new SolrException(ErrorCode.SERVER_ERROR, e);
    }
  }

  @Override
  void runLeaderProcess(boolean weAreReplacement) throws KeeperException,
      InterruptedException {
    
    final String id = leaderSeqPath
        .substring(leaderSeqPath.lastIndexOf("/") + 1);
    ZkNodeProps myProps = new ZkNodeProps("id", id);
    
    zkClient.makePath(leaderPath, ZkStateReader.toJSON(myProps),
        CreateMode.EPHEMERAL, true);
    
    overseer.start(id);
  }
  
  public void cancelElection() throws InterruptedException, KeeperException {
    super.cancelElection();
    overseer.close();
  }
  
  @Override
  public void joinedElectionFired() {
    overseer.close();
  }
  
  @Override
  public void checkIfIamLeaderFired() {
    // leader changed - close the overseer
    overseer.close();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/905.java