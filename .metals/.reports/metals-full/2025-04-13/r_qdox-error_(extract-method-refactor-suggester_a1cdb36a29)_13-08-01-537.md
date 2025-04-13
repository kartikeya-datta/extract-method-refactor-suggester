error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/600.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/600.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/600.java
text:
```scala
p@@arams.set( "collections", aliasedCollections );

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

package org.apache.solr.client.solrj.request;

import java.io.IOException;
import java.util.Collection;

import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.CollectionAdminResponse;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.common.params.CollectionParams.CollectionAction;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.CoreAdminParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.ContentStream;

/**
 * This class is experimental and subject to change.
 *
 * @since solr 4.5
 */
public class CollectionAdminRequest extends SolrRequest
{
  protected String collection = null;
  protected CollectionAction action = null;

  protected static class CollectionShardAdminRequest extends CollectionAdminRequest {
    protected String shardName = null;

    public void setShardName(String shard) { this.shardName = shard; }
    public String getShardName() { return this.shardName; }

    public ModifiableSolrParams getCommonParams() {
      ModifiableSolrParams params = (ModifiableSolrParams) super.getParams();
      params.remove( "name" );
      params.set( "collection", collection );
      params.set( "shard", shardName);
      return params;
    }

    @Override
    public SolrParams getParams() {
      return getCommonParams();
    }
  }

  //a create collection request
  public static class Create extends CollectionAdminRequest {
    protected String configName = null;
    protected String createNodeSet = null;
    protected String routerName;
    protected String shards;
    protected String routerField;
    protected Integer numShards;
    protected Integer maxShardsPerNode;
    protected Integer replicationFactor;


    public Create() {
      action = CollectionAction.CREATE;
    }

    public void setConfigName(String config) { this.configName = config; }
    public void setCreateNodeSet(String nodeSet) { this.createNodeSet = nodeSet; }
    public void setRouterName(String routerName) { this.routerName = routerName; }
    public void setShards(String shards) { this.shards = shards; }
    public void setRouterField(String routerField) { this.routerField = routerField; }
    public void setNumShards(Integer numShards) {this.numShards = numShards;}
    public void setMaxShardsPerNode(Integer numShards) { this.maxShardsPerNode = numShards; }
    public void setReplicationFactor(Integer repl) { this.replicationFactor = repl; }

    public String getConfigName()  { return configName; }
    public String getCreateNodeSet() { return createNodeSet; }
    public String getRouterName() { return  routerName; }
    public String getShards() { return  shards; }
    public Integer getNumShards() { return numShards; }
    public Integer getMaxShardsPerNode() { return maxShardsPerNode; }
    public Integer getReplicationFactor() { return replicationFactor; }

    @Override
    public SolrParams getParams() {
      ModifiableSolrParams params = (ModifiableSolrParams) super.getParams();

      if (configName != null) {
        params.set( "collection.configName", configName);
      }
      if (createNodeSet != null) {
        params.set( "createNodeSet", createNodeSet);
      }
      if (numShards != null) {
        params.set( ZkStateReader.NUM_SHARDS_PROP, numShards);
      }
      if (maxShardsPerNode != null) {
        params.set( "maxShardsPerNode", maxShardsPerNode);
      }
      if (routerName != null) {
        params.set( "router.name", routerName);
      }
      if (shards != null) {
        params.set("shards", shards);
      }
      if (routerField != null) {
        params.set("router.field", routerField);
      }
      if (replicationFactor != null) {
        // OverseerCollectionProcessor.REPLICATION_FACTOR
        params.set( "replicationFactor", replicationFactor);
      }

      return params;
    }
  }

  //a reload collection request
  public static class Reload extends CollectionAdminRequest {
    public Reload() {
      action = CollectionAction.RELOAD;
    }
  }

  //a delete collection request
  public static class Delete extends CollectionAdminRequest {
    public Delete() {
      action = CollectionAction.DELETE;
    }
  }

  //a create shard collection request
  public static class CreateShard extends CollectionShardAdminRequest {
    protected String nodeSet;

    public void setNodeSet(String nodeSet) { this.nodeSet = nodeSet; }
    public String getNodeSet() { return nodeSet; }

    public CreateShard() {
      action = CollectionAction.CREATESHARD;
    }

    @Override
    public SolrParams getParams() {
      ModifiableSolrParams params = getCommonParams();
      params.set( "createNodeSet", nodeSet);
      return params;
    }
  }

  //a split shard collection request
  public static class SplitShard extends CollectionShardAdminRequest {
    protected String ranges;

    public void setRanges(String ranges) { this.ranges = ranges; }
    public String getRanges() { return ranges; }

    public SplitShard() {
      action = CollectionAction.SPLITSHARD;
    }

    @Override
    public SolrParams getParams() {
      ModifiableSolrParams params = getCommonParams();
      params.set( "ranges", ranges);
      return params;
    }
  }

  //a delete shard collection request
  public static class DeleteShard extends CollectionShardAdminRequest {
    public DeleteShard() {
      action = CollectionAction.DELETESHARD;
    }
  }

  //a collection alias create request
  public static class CreateAlias extends CollectionAdminRequest {
    protected String aliasedCollections = null;

    public void setAliasedCollections(String alias) { this.aliasedCollections = alias; }
    public String getAliasedCollections() { return this.aliasedCollections; }

    @Override
    public SolrParams getParams() {
      ModifiableSolrParams params = (ModifiableSolrParams) super.getParams();
      params.set( "collections", collection );
      return params;
    }


    public CreateAlias() {
      action = CollectionAction.CREATEALIAS;
    }
  }

  //a collection alias delete request
  public static class DeleteAlias extends CollectionAdminRequest {
    public DeleteAlias() {
      action = CollectionAction.DELETEALIAS;
    }
  }

  public CollectionAdminRequest()
  {
    super( METHOD.GET, "/admin/collections" );
  }

  public CollectionAdminRequest( String path )
  {
    super( METHOD.GET, path );
  }

  public final void setCollectionName( String collectionName )
  {
    this.collection = collectionName;
  }

  //---------------------------------------------------------------------------------------
  //
  //---------------------------------------------------------------------------------------

  public void setAction( CollectionAction action )
  {
    this.action = action;
  }

  //---------------------------------------------------------------------------------------
  //
  //---------------------------------------------------------------------------------------

  @Override
  public SolrParams getParams()
  {
    if( action == null ) {
      throw new RuntimeException( "no action specified!" );
    }
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set( CoreAdminParams.ACTION, action.toString() );
    params.set( CoreAdminParams.NAME, collection );
    return params;
  }

  //---------------------------------------------------------------------------------------
  //
  //---------------------------------------------------------------------------------------

  @Override
  public Collection<ContentStream> getContentStreams() throws IOException {
    return null;
  }

  @Override
  public CollectionAdminResponse process(SolrServer server) throws SolrServerException, IOException
  {
    long startTime = System.currentTimeMillis();
    CollectionAdminResponse res = new CollectionAdminResponse();
    res.setResponse( server.request( this ) );
    res.setElapsedTime( System.currentTimeMillis()-startTime );
    return res;
  }

  //---------------------------------------------------------------------------------------
  //
  //---------------------------------------------------------------------------------------

  // creates collection using a compositeId router
  public static CollectionAdminResponse createCollection( String name,
                                                          Integer shards, Integer repl, Integer maxShards,
                                                          String nodeSet,
                                                          String conf,
                                                          String routerField,
                                                          SolrServer server ) throws SolrServerException, IOException
  {
    Create req = new Create();
    req.setCollectionName(name);
    req.setRouterName("compositeId");
    req.setNumShards(shards);
    req.setReplicationFactor(repl);
    req.setMaxShardsPerNode(maxShards);
    req.setCreateNodeSet(nodeSet);
    req.setConfigName(conf);
    req.setRouterField(routerField);
    return req.process( server );
  }
  public static CollectionAdminResponse createCollection( String name,
                                                          Integer shards, String conf,
                                                          SolrServer server ) throws SolrServerException, IOException
  {
    Create req = new Create();
    req.setCollectionName(name);
    req.setRouterName("compositeId");
    req.setNumShards(shards);
    req.setConfigName(conf);
    return req.process( server );
  }

  // creates a collection using an implicit router
  public static CollectionAdminResponse createCollection( String name,
                                                          String shards, Integer repl, Integer maxShards,
                                                          String nodeSet,
                                                          String conf,
                                                          String routerField,
                                                          SolrServer server ) throws SolrServerException, IOException
  {
    Create req = new Create();
    req.setCollectionName(name);
    req.setRouterName("implicit");
    req.setShards(shards);
    req.setReplicationFactor(repl);
    req.setMaxShardsPerNode(maxShards);
    req.setCreateNodeSet(nodeSet);
    req.setConfigName(conf);
    req.setRouterField(routerField);
    return req.process( server );
  }
  public static CollectionAdminResponse createCollection( String name,
                                                          String shards, String conf,
                                                          SolrServer server ) throws SolrServerException, IOException
  {
    Create req = new Create();
    req.setCollectionName(name);
    req.setRouterName("implicit");
    req.setShards(shards);
    req.setConfigName(conf);
    return req.process( server );
  }

  public static CollectionAdminResponse reloadCollection( String name, SolrServer server ) throws SolrServerException, IOException
  {
    CollectionAdminRequest req = new Reload();
    req.setCollectionName(name);
    return req.process( server );
  }

  public static CollectionAdminResponse deleteCollection( String name, SolrServer server ) throws SolrServerException, IOException
  {
    CollectionAdminRequest req = new Delete();
    req.setCollectionName(name);
    return req.process( server );
  }

  public static CollectionAdminResponse createShard( String name, String shard, String nodeSet, SolrServer server ) throws SolrServerException, IOException
  {
    CreateShard req = new CreateShard();
    req.setCollectionName(name);
    req.setShardName(shard);
    req.setNodeSet(nodeSet);
    return req.process( server );
  }
  public static CollectionAdminResponse createShard( String name, String shard, SolrServer server ) throws SolrServerException, IOException
  {
    return createShard(name, shard, null, server);
  }

  public static CollectionAdminResponse splitShard( String name, String shard, String ranges, SolrServer server ) throws SolrServerException, IOException
  {
    SplitShard req = new SplitShard();
    req.setCollectionName(name);
    req.setShardName(shard);
    req.setRanges(ranges);
    return req.process( server );
  }
  public static CollectionAdminResponse splitShard( String name, String shard, SolrServer server ) throws SolrServerException, IOException
  {
    return splitShard(name, shard, null, server);
  }

  public static CollectionAdminResponse deleteShard( String name, String shard, SolrServer server ) throws SolrServerException, IOException
  {
    CollectionShardAdminRequest req = new DeleteShard();
    req.setCollectionName(name);
    req.setShardName(shard);
    return req.process( server );
  }

  public static CollectionAdminResponse createAlias( String name, String collections, SolrServer server ) throws SolrServerException, IOException
  {
    CreateAlias req = new CreateAlias();
    req.setCollectionName(name);
    req.setAliasedCollections(collections);
    return req.process( server );
  }

  public static CollectionAdminResponse deleteAlias( String name, SolrServer server ) throws SolrServerException, IOException
  {
    CollectionAdminRequest req = new DeleteAlias();
    req.setCollectionName(name);
    return req.process( server );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/600.java