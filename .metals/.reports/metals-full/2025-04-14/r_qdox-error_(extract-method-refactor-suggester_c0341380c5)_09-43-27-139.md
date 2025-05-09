error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1578.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1578.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1578.java
text:
```scala
s@@uss.setConnectionTimeout(30000);

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.util.LuceneTestCase.Slow;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.solr.common.cloud.ZkNodeProps;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.CollectionParams.CollectionAction;
import org.apache.solr.update.VersionInfo;
import org.apache.solr.update.processor.DistributedUpdateProcessor;
import org.apache.zookeeper.CreateMode;
import org.junit.BeforeClass;

/**
 * Super basic testing, no shard restarting or anything.
 */
@Slow
public class FullSolrCloudDistribCmdsTest extends AbstractFullDistribZkTestBase {
  
  
  @BeforeClass
  public static void beforeSuperClass() {
    schemaString = "schema15.xml";      // we need a string id
  }
  
  public FullSolrCloudDistribCmdsTest() {
    super();
    fixShardCount = true;
    shardCount = 6;
    sliceCount = 3;
  }
  
  @Override
  public void doTest() throws Exception {
    handle.clear();
    handle.put("QTime", SKIPVAL);
    handle.put("timestamp", SKIPVAL);
    
    waitForRecoveriesToFinish(false);
    
    // add a doc, update it, and delete it
    
    QueryResponse results;
    UpdateRequest uReq;
    long docId = addUpdateDelete();
    
    // add 2 docs in a request
    SolrInputDocument doc1;
    SolrInputDocument doc2;
    docId = addTwoDocsInOneRequest(docId);
    
    // two deletes
    uReq = new UpdateRequest();
    uReq.deleteById(Long.toString(docId-1));
    uReq.deleteById(Long.toString(docId-2)).process(cloudClient);
    controlClient.deleteById(Long.toString(docId-1));
    controlClient.deleteById(Long.toString(docId-2));
    
    commit();
    
    results = query(cloudClient);
    assertEquals(0, results.getResults().getNumFound());
    
    results = query(controlClient);
    assertEquals(0, results.getResults().getNumFound());
    
    // add two docs together, a 3rd doc and a delete
    indexr("id", docId++, t1, "originalcontent");
    
    uReq = new UpdateRequest();
    doc1 = new SolrInputDocument();

    addFields(doc1, "id", docId++);
    uReq.add(doc1);
    doc2 = new SolrInputDocument();
    addFields(doc2, "id", docId++);
    uReq.add(doc2);
 
    uReq.process(cloudClient);
    uReq.process(controlClient);
    
    uReq = new UpdateRequest();
    uReq.deleteById(Long.toString(docId - 2)).process(cloudClient);
    controlClient.deleteById(Long.toString(docId - 2));
    
    commit();
    
    assertDocCounts(VERBOSE);
    
    checkShardConsistency();
    
    results = query(controlClient);
    assertEquals(2, results.getResults().getNumFound());
    
    results = query(cloudClient);
    assertEquals(2, results.getResults().getNumFound());
    
    docId = testIndexQueryDeleteHierarchical(docId);
    
    docId = testIndexingDocPerRequestWithHttpSolrServer(docId);
    
    testIndexingWithSuss(docId);
    
    // TODO: testOptimisticUpdate(results);
    
    testDeleteByQueryDistrib();
    
    docId = testThatCantForwardToLeaderFails(docId);
    
    
    docId = testIndexingBatchPerRequestWithHttpSolrServer(docId);
  }

  private long testThatCantForwardToLeaderFails(long docId) throws Exception {
    ZkStateReader zkStateReader = cloudClient.getZkStateReader();
    ZkNodeProps props = zkStateReader.getLeaderRetry(DEFAULT_COLLECTION, "shard1");
    
    chaosMonkey.stopShard("shard1");
    
    Thread.sleep(1000);
    
    // fake that the leader is still advertised
    String leaderPath = ZkStateReader.getShardLeadersPath(DEFAULT_COLLECTION, "shard1");
    SolrZkClient zkClient = new SolrZkClient(zkServer.getZkAddress(), 10000);
    int fails = 0;
    try {
      zkClient.makePath(leaderPath, ZkStateReader.toJSON(props),
          CreateMode.EPHEMERAL, true);
      for (int i = 0; i < 200; i++) {
        try {
          index_specific(shardToJetty.get("shard2").get(0).client.solrClient, id, docId++);
        } catch (SolrException e) {
          // expected
          fails++;
          break;
        } catch (SolrServerException e) {
          // expected
          fails++;
          break;
        }
      }
    } finally {
      zkClient.close();
    }

    assertTrue("A whole shard is down - some of these should fail", fails > 0);
    return docId;
  }

  private long addTwoDocsInOneRequest(long docId) throws
      Exception {
    QueryResponse results;
    UpdateRequest uReq;
    uReq = new UpdateRequest();
    docId = addDoc(docId, uReq);
    docId = addDoc(docId, uReq);
    
    uReq.process(cloudClient);
    uReq.process(controlClient);
    
    commit();
    
    checkShardConsistency();
    
    assertDocCounts(VERBOSE);
    
    results = query(cloudClient);
    assertEquals(2, results.getResults().getNumFound());
    return docId;
  }

  private long addUpdateDelete() throws Exception,
      IOException {
    long docId = 99999999L;
    indexr("id", docId, t1, "originalcontent");
    
    commit();
    
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.add("q", t1 + ":originalcontent");
    QueryResponse results = clients.get(0).query(params);
    assertEquals(1, results.getResults().getNumFound());
    
    // update doc
    indexr("id", docId, t1, "updatedcontent");
    
    commit();
    
    assertDocCounts(VERBOSE);
    
    results = clients.get(0).query(params);
    assertEquals(0, results.getResults().getNumFound());
    
    params.set("q", t1 + ":updatedcontent");
    
    results = clients.get(0).query(params);
    assertEquals(1, results.getResults().getNumFound());
    
    UpdateRequest uReq = new UpdateRequest();
    //uReq.setParam(UpdateParams.UPDATE_CHAIN, DISTRIB_UPDATE_CHAIN);
    uReq.deleteById(Long.toString(docId)).process(clients.get(0));
    
    commit();
    
    results = clients.get(0).query(params);
    assertEquals(0, results.getResults().getNumFound());
    return docId;
  }

  private void testDeleteByQueryDistrib() throws Exception {
    del("*:*");
    commit();
    assertEquals(0, query(cloudClient).getResults().getNumFound());
  }

  private long testIndexQueryDeleteHierarchical(long docId) throws Exception {
    //index
    int topDocsNum = atLeast(10);
    int childsNum = 5+random().nextInt(5);
    for (int i = 0; i < topDocsNum; ++i) {
      UpdateRequest uReq = new UpdateRequest();
      SolrInputDocument topDocument = new SolrInputDocument();
      topDocument.addField("id", docId++);
      topDocument.addField("type_s", "parent");
      topDocument.addField(i + "parent_f1_s", "v1");
      topDocument.addField(i + "parent_f2_s", "v2");
      
      
      for (int index = 0; index < childsNum; ++index) {
        docId = addChildren("child", topDocument, index, false, docId);
      }
      
      uReq.add(topDocument);
      uReq.process(cloudClient);
      uReq.process(controlClient);
    }
    
    commit();
    checkShardConsistency();
    assertDocCounts(VERBOSE);
    
    //query
    // parents
    SolrQuery query = new SolrQuery("type_s:parent");
    QueryResponse results = cloudClient.query(query);
    assertEquals(topDocsNum, results.getResults().getNumFound());
    
    //childs 
    query = new SolrQuery("type_s:child");
    results = cloudClient.query(query);
    assertEquals(topDocsNum * childsNum, results.getResults().getNumFound());
    
    //grandchilds
    query = new SolrQuery("type_s:grand");
    results = cloudClient.query(query);
    //each topDoc has t childs where each child has x = 0 + 2 + 4 + ..(t-1)*2 grands
    //x = 2 * (1 + 2 + 3 +.. (t-1)) => arithmetic summ of t-1 
    //x = 2 * ((t-1) * t / 2) = t * (t - 1)
    assertEquals(topDocsNum * childsNum * (childsNum - 1), results.getResults().getNumFound());
    
    //delete
    del("*:*");
    commit();
    
    return docId;
  }
  
  private long addChildren(String prefix, SolrInputDocument topDocument, int childIndex, boolean lastLevel, long docId) {
    SolrInputDocument childDocument = new SolrInputDocument();
    childDocument.addField("id", docId++);
    childDocument.addField("type_s", prefix);
    for (int index = 0; index < childIndex; ++index) {
      childDocument.addField(childIndex + prefix + index + "_s", childIndex + "value"+ index);
    }   
  
    if (!lastLevel) {
      for (int i = 0; i < childIndex * 2; ++i) {
        docId = addChildren("grand", childDocument, i, true, docId);
      }
    }
    topDocument.addChildDocument(childDocument);
    return docId;
  }
  
  
  private long testIndexingDocPerRequestWithHttpSolrServer(long docId) throws Exception {
    int docs = random().nextInt(TEST_NIGHTLY ? 4013 : 97) + 1;
    for (int i = 0; i < docs; i++) {
      UpdateRequest uReq;
      uReq = new UpdateRequest();
      docId = addDoc(docId, uReq);
      
      uReq.process(cloudClient);
      uReq.process(controlClient);
      
    }
    commit();
    
    checkShardConsistency();
    assertDocCounts(VERBOSE);
    
    return docId++;
  }
  
  private long testIndexingBatchPerRequestWithHttpSolrServer(long docId) throws Exception {
    
    // remove collection
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("action", CollectionAction.DELETE.toString());
    params.set("name", "collection1");
    QueryRequest request = new QueryRequest(params);
    request.setPath("/admin/collections");
    
  
    cloudClient.request(request);
    
    controlClient.deleteByQuery("*:*");
    controlClient.commit();
    
    // somtimes we use an oversharded collection
    createCollection(null, "collection2", 7, 3, 100000, cloudClient, null, "conf1");
    cloudClient.setDefaultCollection("collection2");
    waitForRecoveriesToFinish("collection2", false);
    
    class IndexThread extends Thread {
      Integer name;
      
      public IndexThread(Integer name) {
        this.name = name;
      }
      
      @Override
      public void run() {
        int rnds = random().nextInt(TEST_NIGHTLY ? 25 : 3) + 1;
        for (int i = 0; i < rnds; i++) {
          UpdateRequest uReq;
          uReq = new UpdateRequest();
          int cnt = random().nextInt(TEST_NIGHTLY ? 3313 : 350) + 1;
          for (int j = 0; j <cnt; j++) {
            addDoc("thread" + name + "_" + i + "_" + j, uReq);
          }
          
          try {
            uReq.process(cloudClient);
            uReq.process(controlClient);
          } catch (SolrServerException e) {
            throw new RuntimeException(e);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }

          
        }
      }
    };
    List<Thread> threads = new ArrayList<Thread>();

    int nthreads = random().nextInt(TEST_NIGHTLY ? 4 : 2) + 1;
    for (int i = 0; i < nthreads; i++) {
      IndexThread thread = new IndexThread(i);
      threads.add(thread);
      thread.start();
    }
    
    for (Thread thread : threads) {
      thread.join();
    }
    
    commit();
    
    waitForRecoveriesToFinish("collection2", false);
    
    printLayout();
    
    SolrQuery query = new SolrQuery("*:*");
    long controlCount = controlClient.query(query).getResults()
        .getNumFound();
    long cloudCount = cloudClient.query(query).getResults().getNumFound();

    
    compareResults(controlCount, cloudCount);
    
    assertEquals("Control does not match cloud", controlCount, cloudCount);
    System.out.println("DOCS:" + controlCount);

    return docId;
  }

  private long addDoc(long docId, UpdateRequest uReq) {
    addDoc(Long.toString(docId++), uReq);
    return docId;
  }
  
  private long addDoc(String docId, UpdateRequest uReq) {
    SolrInputDocument doc1 = new SolrInputDocument();
    
    uReq.add(doc1);
    addFields(doc1, "id", docId, "text_t", "some text so that it not's negligent work to parse this doc, even though it's still a pretty short doc");
    return -1;
  }
  
  private long testIndexingWithSuss(long docId) throws Exception {
    ConcurrentUpdateSolrServer suss = new ConcurrentUpdateSolrServer(
        ((HttpSolrServer) clients.get(0)).getBaseURL(), 10, 2);
    QueryResponse results = query(cloudClient);
    long beforeCount = results.getResults().getNumFound();
    int cnt = TEST_NIGHTLY ? 2933 : 313;
    try {
      suss.setConnectionTimeout(15000);
      for (int i = 0; i < cnt; i++) {
        index_specific(suss, id, docId++, "text_t", "some text so that it not's negligent work to parse this doc, even though it's still a pretty short doc");
      }
      suss.blockUntilFinished();
      
      commit();

      checkShardConsistency();
      assertDocCounts(VERBOSE);
    } finally {
      suss.shutdown();
    }
    results = query(cloudClient);
    assertEquals(beforeCount + cnt, results.getResults().getNumFound());
    return docId;
  }
  
  private void testOptimisticUpdate(QueryResponse results) throws Exception {
    SolrDocument doc = results.getResults().get(0);
    Long version = (Long) doc.getFieldValue(VersionInfo.VERSION_FIELD);
    Integer theDoc = (Integer) doc.getFieldValue("id");
    UpdateRequest uReq = new UpdateRequest();
    SolrInputDocument doc1 = new SolrInputDocument();
    uReq.setParams(new ModifiableSolrParams());
    uReq.getParams().set(DistributedUpdateProcessor.VERSION_FIELD, Long.toString(version));
    addFields(doc1, "id", theDoc, t1, "theupdatestuff");
    uReq.add(doc1);
    
    uReq.process(cloudClient);
    uReq.process(controlClient);
    
    commit();
    
    // updating the old version should fail...
    SolrInputDocument doc2 = new SolrInputDocument();
    uReq = new UpdateRequest();
    uReq.setParams(new ModifiableSolrParams());
    uReq.getParams().set(DistributedUpdateProcessor.VERSION_FIELD, Long.toString(version));
    addFields(doc2, "id", theDoc, t1, "thenewupdatestuff");
    uReq.add(doc2);
    
    uReq.process(cloudClient);
    uReq.process(controlClient);
    
    commit();
    
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.add("q", t1 + ":thenewupdatestuff");
    QueryResponse res = clients.get(0).query(params);
    assertEquals(0, res.getResults().getNumFound());
    
    params = new ModifiableSolrParams();
    params.add("q", t1 + ":theupdatestuff");
    res = clients.get(0).query(params);
    assertEquals(1, res.getResults().getNumFound());
  }

  private QueryResponse query(SolrServer server) throws SolrServerException {
    SolrQuery query = new SolrQuery("*:*");
    return server.query(query);
  }
  
  @Override
  public void tearDown() throws Exception {
    super.tearDown();
  }
  
  protected SolrInputDocument addRandFields(SolrInputDocument sdoc) {
    return sdoc;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1578.java