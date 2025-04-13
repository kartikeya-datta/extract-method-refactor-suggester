error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/348.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/348.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/348.java
text:
```scala
F@@ileUtils.writeStringToFile(file, stopwords, "UTF-8");

package org.apache.solr;

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
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest.ACTION;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.SolrCore;
import org.junit.BeforeClass;
import org.junit.AfterClass;

public class AnalysisAfterCoreReloadTest extends SolrTestCaseJ4 {
  
  private static String tmpSolrHome;
  int port = 0;
  static final String context = "/solr";

  static final String collection = "collection1";
  
  @BeforeClass
  public static void beforeClass() throws Exception {
    createTempDir();
    tmpSolrHome = TEMP_DIR + File.separator + AnalysisAfterCoreReloadTest.class.getSimpleName() + System.currentTimeMillis();
    FileUtils.copyDirectory(new File(TEST_HOME()), new File(tmpSolrHome).getAbsoluteFile());
    initCore("solrconfig.xml", "schema.xml", new File(tmpSolrHome).getAbsolutePath());
  }

  @AfterClass
  public static void AfterClass() throws Exception {
    FileUtils.deleteDirectory(new File(tmpSolrHome).getAbsoluteFile());
  }
  
  public void testStopwordsAfterCoreReload() throws Exception {
    SolrInputDocument doc = new SolrInputDocument();
    doc.setField( "id", "42" );
    doc.setField( "teststop", "terma stopworda stopwordb stopwordc" );
    
    // default stopwords - stopworda and stopwordb
    
    UpdateRequest up = new UpdateRequest();
    up.setAction(ACTION.COMMIT, true, true);
    up.add( doc );
    up.process( getSolrCore() );

    SolrQuery q = new SolrQuery();
    QueryRequest r = new QueryRequest( q );
    q.setQuery( "teststop:terma" );
    assertEquals( 1, r.process( getSolrCore() ).getResults().size() );

    q = new SolrQuery();
    r = new QueryRequest( q );
    q.setQuery( "teststop:stopworda" );
    assertEquals( 0, r.process( getSolrCore() ).getResults().size() );

    q = new SolrQuery();
    r = new QueryRequest( q );
    q.setQuery( "teststop:stopwordb" );
    assertEquals( 0, r.process( getSolrCore() ).getResults().size() );

    q = new SolrQuery();
    r = new QueryRequest( q );
    q.setQuery( "teststop:stopwordc" );
    assertEquals( 1, r.process( getSolrCore() ).getResults().size() );

    // overwrite stopwords file with stopword list ["stopwordc"] and reload the core
    overwriteStopwords("stopwordc\n");
    h.getCoreContainer().reload(collection);

    up.process( getSolrCore() );

    q = new SolrQuery();
    r = new QueryRequest( q );
    q.setQuery( "teststop:terma" );
    assertEquals( 1, r.process( getSolrCore() ).getResults().size() );

    q = new SolrQuery();
    r = new QueryRequest( q );
    q.setQuery( "teststop:stopworda" );
    // stopworda is no longer a stopword
    assertEquals( 1, r.process( getSolrCore() ).getResults().size() );

    q = new SolrQuery();
    r = new QueryRequest( q );
    q.setQuery( "teststop:stopwordb" );
    // stopwordb is no longer a stopword
    assertEquals( 1, r.process( getSolrCore() ).getResults().size() );

    q = new SolrQuery();
    r = new QueryRequest( q );
    q.setQuery( "teststop:stopwordc" );
    // stopwordc should be a stopword
    assertEquals( 0, r.process( getSolrCore() ).getResults().size() );
  }
  
  private void overwriteStopwords(String stopwords) throws IOException {
    SolrCore core = h.getCoreContainer().getCore(collection);
    try {
      String configDir = core.getResourceLoader().getConfigDir();
      FileUtils.moveFile(new File(configDir, "stopwords.txt"), new File(configDir, "stopwords.txt.bak"));
      File file = new File(configDir, "stopwords.txt");
      FileUtils.writeStringToFile(file, stopwords);
     
    } finally {
      core.close();
    }
  }
  
  @Override
  public void tearDown() throws Exception {
    SolrCore core = h.getCoreContainer().getCore(collection);
    String configDir;
    try {
      configDir = core.getResourceLoader().getConfigDir();
    } finally {
      core.close();
    }
    super.tearDown();
    if (new File(configDir, "stopwords.txt.bak").exists()) {
      FileUtils.deleteQuietly(new File(configDir, "stopwords.txt"));
      FileUtils.moveFile(new File(configDir, "stopwords.txt.bak"), new File(configDir, "stopwords.txt"));
    }
  }

  protected SolrServer getSolrCore() {
    return new EmbeddedSolrServer(h.getCore());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/348.java