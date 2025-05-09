error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1577.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1577.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1577.java
text:
```scala
z@@kStateReaderMock.getBaseUrlForNodeName(address);

package org.apache.solr.cloud;

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

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.cloud.DistributedQueue.QueueEvent;
import org.apache.solr.cloud.Overseer.LeaderStatus;
import org.apache.solr.common.cloud.ClusterState;
import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.solr.common.cloud.ZkNodeProps;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.common.params.CoreAdminParams;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.StrUtils;
import org.apache.solr.handler.component.ShardHandler;
import org.apache.solr.handler.component.ShardRequest;
import org.apache.solr.handler.component.ShardResponse;
import org.apache.zookeeper.CreateMode;
import org.easymock.Capture;
import org.easymock.IAnswer;
import org.eclipse.jetty.util.BlockingArrayQueue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import static org.easymock.EasyMock.anyBoolean;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

public class OverseerCollectionProcessorTest extends SolrTestCaseJ4 {
  
  private static final String ADMIN_PATH = "/admin/cores";
  private static final String COLLECTION_NAME = "mycollection";
  private static final String CONFIG_NAME = "myconfig";
  
  private static DistributedQueue workQueueMock;
  private static ShardHandler shardHandlerMock;
  private static ZkStateReader zkStateReaderMock;
  private static ClusterState clusterStateMock;
  private static SolrZkClient solrZkClientMock;
  private final Map zkMap = new HashMap();
  private final Set collectionsSet = new HashSet();

  private OverseerCollectionProcessorToBeTested underTest;
  
  private Thread thread;
  private Queue<QueueEvent> queue = new BlockingArrayQueue<QueueEvent>();

  private class OverseerCollectionProcessorToBeTested extends
      OverseerCollectionProcessor {
    
    private SolrResponse lastProcessMessageResult;
    
    public OverseerCollectionProcessorToBeTested(ZkStateReader zkStateReader,
        String myId, ShardHandler shardHandler, String adminPath,
        DistributedQueue workQueue) {
      super(zkStateReader, myId, shardHandler, adminPath, workQueue);
    }
    
    @Override
    protected SolrResponse processMessage(ZkNodeProps message, String operation) {
      lastProcessMessageResult = super.processMessage(message, operation);
      log.info("1 : "+System.currentTimeMillis());
      return lastProcessMessageResult;
    }
    
    @Override
    protected LeaderStatus amILeader() {
      return LeaderStatus.YES;
    }
    
  }
  
  @BeforeClass
  public static void setUpOnce() throws Exception {
    workQueueMock = createMock(DistributedQueue.class);
    shardHandlerMock = createMock(ShardHandler.class);
    zkStateReaderMock = createMock(ZkStateReader.class);
    clusterStateMock = createMock(ClusterState.class);
    solrZkClientMock = createMock(SolrZkClient.class);
  }
  
  @AfterClass
  public static void tearDownOnce() {
    workQueueMock = null;
    shardHandlerMock = null;
    zkStateReaderMock = null;
    clusterStateMock = null;
    solrZkClientMock = null;
  }
  
  @Before
  public void setUp() throws Exception {
    super.setUp();
    queue.clear();
    reset(workQueueMock);
    reset(workQueueMock);
    reset(shardHandlerMock);
    reset(zkStateReaderMock);
    reset(clusterStateMock);
    reset(solrZkClientMock);
    underTest = new OverseerCollectionProcessorToBeTested(zkStateReaderMock,
        "1234", shardHandlerMock, ADMIN_PATH, workQueueMock);
    zkMap.clear();
    collectionsSet.clear();
  }
  
  @After
  public void tearDown() throws Exception {
    stopComponentUnderTest();
    super.tearDown();
  }
  
  protected Set<String> commonMocks(int liveNodesCount) throws Exception {
    workQueueMock.peek(true);
    expectLastCall().andAnswer(new IAnswer<Object>() {
      @Override
      public Object answer() throws Throwable {
        Object result;
        while ((result = queue.peek()) == null) {
          Thread.sleep(1000);
        }
        return result;
      }
    }).anyTimes();
    
    workQueueMock.remove(anyObject(QueueEvent.class));
    expectLastCall().andAnswer(new IAnswer<Object>() {
      @Override
      public Object answer() throws Throwable {
        queue.remove((QueueEvent) getCurrentArguments()[0]);
        return null;
      }
    }).anyTimes();
    
    workQueueMock.poll();
    expectLastCall().andAnswer(new IAnswer<Object>() {
      @Override
      public Object answer() throws Throwable {
        return queue.poll();
      }
    }).anyTimes();
    
    zkStateReaderMock.getClusterState();
    expectLastCall().andAnswer(new IAnswer<Object>() {
      @Override
      public Object answer() throws Throwable {
        return clusterStateMock;
      }
    }).anyTimes();
    
    zkStateReaderMock.getZkClient();
    expectLastCall().andAnswer(new IAnswer<Object>() {
      @Override
      public Object answer() throws Throwable {
        return solrZkClientMock;
      }
    }).anyTimes();
    
    
    clusterStateMock.getCollections();
    expectLastCall().andAnswer(new IAnswer<Object>() {
      @Override
      public Object answer() throws Throwable {
        return collectionsSet;
      }
    }).anyTimes();
    final Set<String> liveNodes = new HashSet<String>();
    for (int i = 0; i < liveNodesCount; i++) {
      final String address = "localhost:" + (8963 + i) + "_solr";
      liveNodes.add(address);
      
      solrZkClientMock.getBaseUrlForNodeName(address);
      expectLastCall().andAnswer(new IAnswer<Object>() {
        @Override
        public Object answer() throws Throwable {
          // This works as long as this test does not use a 
          // webapp context with an underscore in it
          return address.replaceAll("_", "/");
        }
      }).anyTimes();
      
    }
    
    solrZkClientMock.getZkClientTimeout();
    expectLastCall().andAnswer(new IAnswer<Object>() {
      @Override
      public Object answer() throws Throwable {
        return 30000;
      }
    }).anyTimes();
    
    clusterStateMock.hasCollection(anyObject(String.class));
    expectLastCall().andAnswer(new IAnswer<Boolean>() {
      @Override
      public Boolean answer() throws Throwable {
        String key = (String) getCurrentArguments()[0];
        return collectionsSet.contains(key);
      }
    } ).anyTimes();


    clusterStateMock.getLiveNodes();
    expectLastCall().andAnswer(new IAnswer<Object>() {
      @Override
      public Object answer() throws Throwable {
        return liveNodes;
      }
    }).anyTimes();
    solrZkClientMock.create(anyObject(String.class), anyObject(byte[].class), anyObject(CreateMode.class), anyBoolean());
    expectLastCall().andAnswer(new IAnswer<String>() {
      @Override
      public String answer() throws Throwable {
        String key = (String) getCurrentArguments()[0];
        zkMap.put(key, null);
        handleCrateCollMessage((byte[]) getCurrentArguments()[1]);
        return key;
      }
    }).anyTimes();

    solrZkClientMock.create(anyObject(String.class), anyObject(byte[].class), anyObject(List.class),anyObject(CreateMode.class), anyBoolean());
    expectLastCall().andAnswer(new IAnswer<String>() {
      @Override
      public String answer() throws Throwable {
        String key = (String) getCurrentArguments()[0];
        zkMap.put(key, null);
        handleCrateCollMessage((byte[]) getCurrentArguments()[1]);
        return key;
      }
    }).anyTimes();

    solrZkClientMock.makePath(anyObject(String.class), anyObject(byte[].class), anyBoolean());
    expectLastCall().andAnswer(new IAnswer<String>() {
      @Override
      public String answer() throws Throwable {
        String key = (String) getCurrentArguments()[0];
        return key;
      }
    }).anyTimes();

    solrZkClientMock.exists(anyObject(String.class),anyBoolean());
    expectLastCall().andAnswer(new IAnswer<Boolean>() {
      @Override
      public Boolean answer() throws Throwable {
        String key = (String) getCurrentArguments()[0];
        return zkMap.containsKey(key);
      }
    }).anyTimes();
    
    return liveNodes;
  }

  private void handleCrateCollMessage(byte[] bytes) {
    try {
      ZkNodeProps props = ZkNodeProps.load(bytes);
      if("createcollection".equals(props.getStr("operation"))){
        String collName = props.getStr("name") ;
        if(collName != null) collectionsSet.add(collName);
      }
    } catch (Exception e) { }
  }

  protected void startComponentUnderTest() {
    thread = new Thread(underTest);
    thread.start();
  }
  
  protected void stopComponentUnderTest() throws Exception {
    underTest.close();
    thread.interrupt();
    thread.join();
  }
  
  private class SubmitCapture {
    public Capture<ShardRequest> shardRequestCapture = new Capture<ShardRequest>();
    public Capture<String> nodeUrlsWithoutProtocolPartCapture = new Capture<String>();
    public Capture<ModifiableSolrParams> params = new Capture<ModifiableSolrParams>();
  }
  
  protected List<SubmitCapture> mockShardHandlerForCreateJob(
      Integer numberOfSlices, Integer numberOfReplica) {
    List<SubmitCapture> submitCaptures = new ArrayList<SubmitCapture>();
    for (int i = 0; i < (numberOfSlices * numberOfReplica); i++) {
      SubmitCapture submitCapture = new SubmitCapture();
      shardHandlerMock.submit(capture(submitCapture.shardRequestCapture),
          capture(submitCapture.nodeUrlsWithoutProtocolPartCapture),
          capture(submitCapture.params));
      expectLastCall();
      submitCaptures.add(submitCapture);
      ShardResponse shardResponseWithoutException = new ShardResponse();
      shardResponseWithoutException.setSolrResponse(new QueryResponse());
      expect(shardHandlerMock.takeCompletedOrError()).andReturn(
          shardResponseWithoutException);
    }
    expect(shardHandlerMock.takeCompletedOrError()).andReturn(null);
    return submitCaptures;
  }
  
  protected void issueCreateJob(Integer numberOfSlices,
      Integer replicationFactor, Integer maxShardsPerNode, List<String> createNodeList, boolean sendCreateNodeList) {
    ZkNodeProps props;
    if (sendCreateNodeList) {
      props = new ZkNodeProps(Overseer.QUEUE_OPERATION,
          OverseerCollectionProcessor.CREATECOLLECTION,
          OverseerCollectionProcessor.REPLICATION_FACTOR,
          replicationFactor.toString(), "name", COLLECTION_NAME,
          "collection.configName", CONFIG_NAME,
          OverseerCollectionProcessor.NUM_SLICES, numberOfSlices.toString(),
          OverseerCollectionProcessor.MAX_SHARDS_PER_NODE,
          maxShardsPerNode.toString(),
          OverseerCollectionProcessor.CREATE_NODE_SET,
          (createNodeList != null)?StrUtils.join(createNodeList, ','):null);
    } else {
      props = new ZkNodeProps(Overseer.QUEUE_OPERATION,
          OverseerCollectionProcessor.CREATECOLLECTION,
          OverseerCollectionProcessor.REPLICATION_FACTOR,
          replicationFactor.toString(), "name", COLLECTION_NAME,
          "collection.configName", CONFIG_NAME,
          OverseerCollectionProcessor.NUM_SLICES, numberOfSlices.toString(),
          OverseerCollectionProcessor.MAX_SHARDS_PER_NODE,
          maxShardsPerNode.toString());
    }
    QueueEvent qe = new QueueEvent("id", ZkStateReader.toJSON(props), null);
    queue.add(qe);
  }
  
  protected void verifySubmitCaptures(List<SubmitCapture> submitCaptures,
      Integer numberOfSlices, Integer numberOfReplica, Collection<String> createNodes) {
    List<String> coreNames = new ArrayList<String>();
    Map<String,Map<String,Integer>> sliceToNodeUrlsWithoutProtocolPartToNumberOfShardsRunningMapMap = new HashMap<String,Map<String,Integer>>();
    List<String> nodeUrlWithoutProtocolPartForLiveNodes = new ArrayList<String>(
        createNodes.size());
    for (String nodeName : createNodes) {
      String nodeUrlWithoutProtocolPart = nodeName.replaceAll("_", "/");
      if (nodeUrlWithoutProtocolPart.startsWith("http://")) nodeUrlWithoutProtocolPart = nodeUrlWithoutProtocolPart
          .substring(7);
      nodeUrlWithoutProtocolPartForLiveNodes.add(nodeUrlWithoutProtocolPart);
    }
    
    for (SubmitCapture submitCapture : submitCaptures) {
      ShardRequest shardRequest = submitCapture.shardRequestCapture.getValue();
      assertEquals(CoreAdminAction.CREATE.toString(),
          shardRequest.params.get(CoreAdminParams.ACTION));
      // assertEquals(shardRequest.params, submitCapture.params);
      String coreName = shardRequest.params.get(CoreAdminParams.NAME);
      assertFalse("Core with name " + coreName + " created twice",
          coreNames.contains(coreName));
      coreNames.add(coreName);
      assertEquals(CONFIG_NAME,
          shardRequest.params.get("collection.configName"));
      assertEquals(COLLECTION_NAME,
          shardRequest.params.get(CoreAdminParams.COLLECTION));
      assertEquals(numberOfSlices.toString(),
          shardRequest.params.get(ZkStateReader.NUM_SHARDS_PROP));
      assertEquals(ADMIN_PATH, shardRequest.params.get("qt"));
      assertEquals(1, shardRequest.purpose);
      assertEquals(1, shardRequest.shards.length);
      assertEquals(submitCapture.nodeUrlsWithoutProtocolPartCapture.getValue(),
          shardRequest.shards[0]);
      assertTrue("Shard " + coreName + " created on wrong node "
          + shardRequest.shards[0],
          nodeUrlWithoutProtocolPartForLiveNodes
              .contains(shardRequest.shards[0]));
      assertEquals(shardRequest.shards, shardRequest.actualShards);
      
      String sliceName = shardRequest.params.get(CoreAdminParams.SHARD);
      if (!sliceToNodeUrlsWithoutProtocolPartToNumberOfShardsRunningMapMap
          .containsKey(sliceName)) {
        sliceToNodeUrlsWithoutProtocolPartToNumberOfShardsRunningMapMap.put(
            sliceName, new HashMap<String,Integer>());
      }
      Map<String,Integer> nodeUrlsWithoutProtocolPartToNumberOfShardsRunningMap = sliceToNodeUrlsWithoutProtocolPartToNumberOfShardsRunningMapMap
          .get(sliceName);
      Integer existingCount;
      nodeUrlsWithoutProtocolPartToNumberOfShardsRunningMap
          .put(
              shardRequest.shards[0],
              ((existingCount = nodeUrlsWithoutProtocolPartToNumberOfShardsRunningMap
                  .get(shardRequest.shards[0])) == null) ? 1
                  : (existingCount + 1));
    }
    
    assertEquals(numberOfSlices * numberOfReplica, coreNames.size());
    for (int i = 1; i <= numberOfSlices; i++) {
      for (int j = 1; j <= numberOfReplica; j++) {
        String coreName = COLLECTION_NAME + "_shard" + i + "_replica" + j;
        assertTrue("Shard " + coreName + " was not created",
            coreNames.contains(coreName));
      }
    }
    
    assertEquals(numberOfSlices.intValue(),
        sliceToNodeUrlsWithoutProtocolPartToNumberOfShardsRunningMapMap.size());
    for (int i = 1; i <= numberOfSlices; i++) {
      sliceToNodeUrlsWithoutProtocolPartToNumberOfShardsRunningMapMap.keySet()
          .contains("shard" + i);
    }
    int minShardsPerSlicePerNode = numberOfReplica / createNodes.size();
    int numberOfNodesSupposedToRunMaxShards = numberOfReplica
        % createNodes.size();
    int numberOfNodesSupposedToRunMinShards = createNodes.size()
        - numberOfNodesSupposedToRunMaxShards;
    int maxShardsPerSlicePerNode = (minShardsPerSlicePerNode + 1);
    if (numberOfNodesSupposedToRunMaxShards == 0) {
      numberOfNodesSupposedToRunMaxShards = numberOfNodesSupposedToRunMinShards;
      maxShardsPerSlicePerNode = minShardsPerSlicePerNode;
    }
    boolean diffBetweenMinAndMaxShardsPerSlicePerNode = (maxShardsPerSlicePerNode != minShardsPerSlicePerNode);
    
    for (Entry<String,Map<String,Integer>> sliceToNodeUrlsWithoutProtocolPartToNumberOfShardsRunningMapMapEntry : sliceToNodeUrlsWithoutProtocolPartToNumberOfShardsRunningMapMap
        .entrySet()) {
      int numberOfShardsRunning = 0;
      int numberOfNodesRunningMinShards = 0;
      int numberOfNodesRunningMaxShards = 0;
      int numberOfNodesRunningAtLeastOneShard = 0;
      for (String nodeUrlsWithoutProtocolPart : sliceToNodeUrlsWithoutProtocolPartToNumberOfShardsRunningMapMapEntry
          .getValue().keySet()) {
        int numberOfShardsRunningOnThisNode = sliceToNodeUrlsWithoutProtocolPartToNumberOfShardsRunningMapMapEntry
            .getValue().get(nodeUrlsWithoutProtocolPart);
        numberOfShardsRunning += numberOfShardsRunningOnThisNode;
        numberOfNodesRunningAtLeastOneShard++;
        assertTrue(
            "Node "
                + nodeUrlsWithoutProtocolPart
                + " is running wrong number of shards. Supposed to run "
                + minShardsPerSlicePerNode
                + (diffBetweenMinAndMaxShardsPerSlicePerNode ? (" or " + maxShardsPerSlicePerNode)
                    : ""),
            (numberOfShardsRunningOnThisNode == minShardsPerSlicePerNode)
 (numberOfShardsRunningOnThisNode == maxShardsPerSlicePerNode));
        if (numberOfShardsRunningOnThisNode == minShardsPerSlicePerNode) numberOfNodesRunningMinShards++;
        if (numberOfShardsRunningOnThisNode == maxShardsPerSlicePerNode) numberOfNodesRunningMaxShards++;
      }
      if (minShardsPerSlicePerNode == 0) numberOfNodesRunningMinShards = (createNodes
          .size() - numberOfNodesRunningAtLeastOneShard);
      assertEquals(
          "Too many shards are running under slice "
              + sliceToNodeUrlsWithoutProtocolPartToNumberOfShardsRunningMapMapEntry
                  .getKey(),
          numberOfReplica.intValue(), numberOfShardsRunning);
      assertEquals(numberOfNodesSupposedToRunMinShards,
          numberOfNodesRunningMinShards);
      assertEquals(numberOfNodesSupposedToRunMaxShards,
          numberOfNodesRunningMaxShards);
    }
  }
  
  protected void waitForEmptyQueue(long maxWait) throws Exception {
    long start = System.currentTimeMillis();
    while (queue.peek() != null) {
      if ((System.currentTimeMillis() - start) > maxWait) fail(" Queue not empty within "
          + maxWait + " ms" + System.currentTimeMillis());
      Thread.sleep(100);
    }
  }
  
  protected enum CreateNodeListOptions {
    SEND,
    DONT_SEND,
    SEND_NULL
  }
  protected void testTemplate(Integer numberOfNodes, Integer numberOfNodesToCreateOn, CreateNodeListOptions createNodeListOption, Integer replicationFactor,
      Integer numberOfSlices, Integer maxShardsPerNode,
      boolean collectionExceptedToBeCreated) throws Exception {
    assertTrue("Wrong usage of testTemplate. numberOfNodesToCreateOn " + numberOfNodesToCreateOn + " is not allowed to be higher than numberOfNodes " + numberOfNodes, numberOfNodes.intValue() >= numberOfNodesToCreateOn.intValue());
    assertTrue("Wrong usage of testTemplage. createNodeListOption has to be " + CreateNodeListOptions.SEND + " when numberOfNodes and numberOfNodesToCreateOn are unequal", ((createNodeListOption == CreateNodeListOptions.SEND) || (numberOfNodes.intValue() == numberOfNodesToCreateOn.intValue())));
    
    Set<String> liveNodes = commonMocks(numberOfNodes);
    List<String> createNodeList = new ArrayList<String>();
    int i = 0;
    for (String node : liveNodes) {
      if (i++ < numberOfNodesToCreateOn) {
        createNodeList.add(node);
      }
    }
    
    List<SubmitCapture> submitCaptures = null;
    if (collectionExceptedToBeCreated) {
      submitCaptures = mockShardHandlerForCreateJob(numberOfSlices,
          replicationFactor);
    }
    
    replay(workQueueMock);
    replay(solrZkClientMock);
    replay(zkStateReaderMock);
    replay(clusterStateMock);
    replay(shardHandlerMock);

    log.info("clusterstate " +clusterStateMock.hashCode());

    startComponentUnderTest();
    
    issueCreateJob(numberOfSlices, replicationFactor, maxShardsPerNode, (createNodeListOption != CreateNodeListOptions.SEND_NULL)?createNodeList:null, (createNodeListOption != CreateNodeListOptions.DONT_SEND));
    waitForEmptyQueue(10000);
    
    if (collectionExceptedToBeCreated) {
      assertNotNull(underTest.lastProcessMessageResult.getResponse().toString(), underTest.lastProcessMessageResult);
    }
    verify(shardHandlerMock);
    
    if (collectionExceptedToBeCreated) {
      verifySubmitCaptures(submitCaptures, numberOfSlices, replicationFactor,
          createNodeList);
    }
  }
  
  @Test
  public void testNoReplicationEqualNumberOfSlicesPerNode() throws Exception {
    Integer numberOfNodes = 4;
    Integer numberOfNodesToCreateOn = 4;
    CreateNodeListOptions createNodeListOptions = CreateNodeListOptions.DONT_SEND;
    Integer replicationFactor = 1;
    Integer numberOfSlices = 8;
    Integer maxShardsPerNode = 2;
    testTemplate(numberOfNodes, numberOfNodesToCreateOn, createNodeListOptions, replicationFactor, numberOfSlices,
        maxShardsPerNode, true);
  }
  
  @Test
  public void testReplicationEqualNumberOfSlicesPerNode() throws Exception {
    Integer numberOfNodes = 4;
    Integer numberOfNodesToCreateOn = 4;
    CreateNodeListOptions createNodeListOptions = CreateNodeListOptions.DONT_SEND;
    Integer replicationFactor = 2;
    Integer numberOfSlices = 4;
    Integer maxShardsPerNode = 2;
    testTemplate(numberOfNodes, numberOfNodesToCreateOn, createNodeListOptions, replicationFactor, numberOfSlices,
        maxShardsPerNode, true);
  }
  
  @Test
  public void testNoReplicationEqualNumberOfSlicesPerNodeSendCreateNodesEqualToLiveNodes() throws Exception {
    Integer numberOfNodes = 4;
    Integer numberOfNodesToCreateOn = 4;
    CreateNodeListOptions createNodeListOptions = CreateNodeListOptions.SEND;
    Integer replicationFactor = 1;
    Integer numberOfSlices = 8;
    Integer maxShardsPerNode = 2;
    testTemplate(numberOfNodes, numberOfNodesToCreateOn, createNodeListOptions, replicationFactor, numberOfSlices,
        maxShardsPerNode, true);
  }
  
  @Test
  public void testReplicationEqualNumberOfSlicesPerNodeSendCreateNodesEqualToLiveNodes() throws Exception {
    Integer numberOfNodes = 4;
    Integer numberOfNodesToCreateOn = 4;
    CreateNodeListOptions createNodeListOptions = CreateNodeListOptions.SEND;
    Integer replicationFactor = 2;
    Integer numberOfSlices = 4;
    Integer maxShardsPerNode = 2;
    testTemplate(numberOfNodes, numberOfNodesToCreateOn, createNodeListOptions, replicationFactor, numberOfSlices,
        maxShardsPerNode, true);
  }
  
  @Test
  public void testNoReplicationEqualNumberOfSlicesPerNodeSendNullCreateNodes() throws Exception {
    Integer numberOfNodes = 4;
    Integer numberOfNodesToCreateOn = 4;
    CreateNodeListOptions createNodeListOptions = CreateNodeListOptions.SEND_NULL;
    Integer replicationFactor = 1;
    Integer numberOfSlices = 8;
    Integer maxShardsPerNode = 2;
    testTemplate(numberOfNodes, numberOfNodesToCreateOn, createNodeListOptions, replicationFactor, numberOfSlices,
        maxShardsPerNode, true);
  }
  
  @Test
  public void testReplicationEqualNumberOfSlicesPerNodeSendNullCreateNodes() throws Exception {
    Integer numberOfNodes = 4;
    Integer numberOfNodesToCreateOn = 4;
    CreateNodeListOptions createNodeListOptions = CreateNodeListOptions.SEND_NULL;
    Integer replicationFactor = 2;
    Integer numberOfSlices = 4;
    Integer maxShardsPerNode = 2;
    testTemplate(numberOfNodes, numberOfNodesToCreateOn, createNodeListOptions, replicationFactor, numberOfSlices,
        maxShardsPerNode, true);
  }  
  
  @Test
  public void testNoReplicationUnequalNumberOfSlicesPerNode() throws Exception {
    Integer numberOfNodes = 4;
    Integer numberOfNodesToCreateOn = 4;
    CreateNodeListOptions createNodeListOptions = CreateNodeListOptions.DONT_SEND;
    Integer replicationFactor = 1;
    Integer numberOfSlices = 6;
    Integer maxShardsPerNode = 2;
    testTemplate(numberOfNodes, numberOfNodesToCreateOn, createNodeListOptions, replicationFactor, numberOfSlices,
        maxShardsPerNode, true);
  }
  
  @Test
  public void testReplicationUnequalNumberOfSlicesPerNode() throws Exception {
    Integer numberOfNodes = 4;
    Integer numberOfNodesToCreateOn = 4;
    CreateNodeListOptions createNodeListOptions = CreateNodeListOptions.DONT_SEND;
    Integer replicationFactor = 2;
    Integer numberOfSlices = 3;
    Integer maxShardsPerNode = 2;
    testTemplate(numberOfNodes, numberOfNodesToCreateOn, createNodeListOptions, replicationFactor, numberOfSlices,
        maxShardsPerNode, true);
  }
  
  @Test
  public void testNoReplicationCollectionNotCreatedDueToMaxShardsPerNodeLimit()
      throws Exception {
    Integer numberOfNodes = 4;
    Integer numberOfNodesToCreateOn = 4;
    CreateNodeListOptions createNodeListOptions = CreateNodeListOptions.DONT_SEND;
    Integer replicationFactor = 1;
    Integer numberOfSlices = 6;
    Integer maxShardsPerNode = 1;
    testTemplate(numberOfNodes, numberOfNodesToCreateOn, createNodeListOptions, replicationFactor, numberOfSlices,
        maxShardsPerNode, false);
  }
  
  @Test
  public void testReplicationCollectionNotCreatedDueToMaxShardsPerNodeLimit()
      throws Exception {
    Integer numberOfNodes = 4;
    Integer numberOfNodesToCreateOn = 4;
    CreateNodeListOptions createNodeListOptions = CreateNodeListOptions.DONT_SEND;
    Integer replicationFactor = 2;
    Integer numberOfSlices = 3;
    Integer maxShardsPerNode = 1;
    testTemplate(numberOfNodes, numberOfNodesToCreateOn, createNodeListOptions, replicationFactor, numberOfSlices,
        maxShardsPerNode, false);
  }

  @Test
  public void testNoReplicationLimitedNodesToCreateOn()
      throws Exception {
    Integer numberOfNodes = 4;
    Integer numberOfNodesToCreateOn = 2;
    CreateNodeListOptions createNodeListOptions = CreateNodeListOptions.SEND;
    Integer replicationFactor = 1;
    Integer numberOfSlices = 6;
    Integer maxShardsPerNode = 3;
    testTemplate(numberOfNodes, numberOfNodesToCreateOn, createNodeListOptions, replicationFactor, numberOfSlices,
        maxShardsPerNode, true);
  }
  
  @Test
  public void testReplicationLimitedNodesToCreateOn()
      throws Exception {
    Integer numberOfNodes = 4;
    Integer numberOfNodesToCreateOn = 2;
    CreateNodeListOptions createNodeListOptions = CreateNodeListOptions.SEND;
    Integer replicationFactor = 2;
    Integer numberOfSlices = 3;
    Integer maxShardsPerNode = 3;
    testTemplate(numberOfNodes, numberOfNodesToCreateOn, createNodeListOptions, replicationFactor, numberOfSlices,
        maxShardsPerNode, true);
  }

  @Test
  public void testNoReplicationCollectionNotCreatedDueToMaxShardsPerNodeAndNodesToCreateOnLimits()
      throws Exception {
    Integer numberOfNodes = 4;
    Integer numberOfNodesToCreateOn = 3;
    CreateNodeListOptions createNodeListOptions = CreateNodeListOptions.SEND;
    Integer replicationFactor = 1;
    Integer numberOfSlices = 8;
    Integer maxShardsPerNode = 2;
    testTemplate(numberOfNodes, numberOfNodesToCreateOn, createNodeListOptions, replicationFactor, numberOfSlices,
        maxShardsPerNode, false);
  }
  
  @Test
  public void testReplicationCollectionNotCreatedDueToMaxShardsPerNodeAndNodesToCreateOnLimits()
      throws Exception {
    Integer numberOfNodes = 4;
    Integer numberOfNodesToCreateOn = 3;
    CreateNodeListOptions createNodeListOptions = CreateNodeListOptions.SEND;
    Integer replicationFactor = 2;
    Integer numberOfSlices = 4;
    Integer maxShardsPerNode = 2;
    testTemplate(numberOfNodes, numberOfNodesToCreateOn, createNodeListOptions, replicationFactor, numberOfSlices,
        maxShardsPerNode, false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1577.java