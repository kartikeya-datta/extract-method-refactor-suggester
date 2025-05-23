error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/378.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/378.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/378.java
text:
```scala
t@@racker.timeUpperBound = 100000;

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

package org.apache.solr.update;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.XmlUpdateRequestHandler;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.request.SolrQueryResponse;
import org.apache.solr.util.AbstractSolrTestCase;

/**
 * 
 * @author ryan
 *
 */
public class AutoCommitTest extends AbstractSolrTestCase {

  public String getSchemaFile() { return "schema.xml"; }
  public String getSolrConfigFile() { return "solrconfig.xml"; }
  
  /**
   * Take a string and make it an iterable ContentStream
   * 
   * This should be moved to a helper class. (it is useful for the client too!)
   */
  public static Collection<ContentStream> toContentStreams( final String str, final String contentType )
  {
    ArrayList<ContentStream> streams = new ArrayList<ContentStream>();
    ContentStreamBase stream = new ContentStreamBase.StringStream( str );
    stream.setContentType( contentType );
    streams.add( stream );
    return streams;
  }

  public void testMaxDocs() throws Exception {
    
    DirectUpdateHandler2 updater = (DirectUpdateHandler2)SolrCore.getSolrCore().getUpdateHandler();
    DirectUpdateHandler2.CommitTracker tracker = updater.tracker;
    tracker.timeUpperBound = -1;
    tracker.docsUpperBound = 14;
    
    XmlUpdateRequestHandler handler = new XmlUpdateRequestHandler();
    handler.init( null );
    
    SolrCore core = SolrCore.getSolrCore();
    MapSolrParams params = new MapSolrParams( new HashMap<String, String>() );
    
    // Add a single document
    SolrQueryResponse rsp = new SolrQueryResponse();
    SolrQueryRequestBase req = new SolrQueryRequestBase( core, params ) {};
    for( int i=0; i<14; i++ ) {
      req.setContentStreams( toContentStreams(
        adoc("id", "A"+i, "subject", "info" ), null ) );
      handler.handleRequest( req, rsp );
    }
    // It should not be there right away
    assertQ("shouldn't find any", req("id:A1") ,"//result[@numFound=0]" );
    assertEquals( 0, tracker.autoCommitCount );

    req.setContentStreams( toContentStreams(
        adoc("id", "A14", "subject", "info" ), null ) );
    handler.handleRequest( req, rsp );
    // Wait longer then the autocommit time
    Thread.sleep( 500 );
    // blocks until commit is complete
    req.setContentStreams( toContentStreams(
        adoc("id", "A15", "subject", "info" ), null ) );
    handler.handleRequest( req, rsp );
      
    // Now make sure we can find it
    assertQ("should find one", req("id:A14") ,"//result[@numFound=1]" );
    assertEquals( 1, tracker.autoCommitCount );
    // But not the one added afterward
    assertQ("should find one", req("id:A15") ,"//result[@numFound=0]" );
    assertEquals( 1, tracker.autoCommitCount );
    
    // Now add some more
    for( int i=0; i<14; i++ ) {
      req.setContentStreams( toContentStreams(
        adoc("id", "B"+i, "subject", "info" ), null ) );
      handler.handleRequest( req, rsp );
    }
    // It should not be there right away
    assertQ("shouldn't find any", req("id:B1") ,"//result[@numFound=0]" );
    assertEquals( 1, tracker.autoCommitCount );
    
    req.setContentStreams( toContentStreams(
        adoc("id", "B14", "subject", "info" ), null ) );
    handler.handleRequest( req, rsp );
    Thread.sleep( 500 );

    // add request will block if commit has already started or completed
    req.setContentStreams( toContentStreams(
        adoc("id", "B15", "subject", "info" ), null ) );
    handler.handleRequest( req, rsp );

    assertQ("should find one", req("id:B14") ,"//result[@numFound=1]" );
    assertEquals( 2, tracker.autoCommitCount );
    assertQ("should find none", req("id:B15") ,"//result[@numFound=0]" );
    assertEquals( 2, tracker.autoCommitCount );
  }

  public void testMaxTime() throws Exception {
    
    DirectUpdateHandler2 updater = (DirectUpdateHandler2)SolrCore.getSolrCore().getUpdateHandler();
    DirectUpdateHandler2.CommitTracker tracker = updater.tracker;
    tracker.timeUpperBound = 500;
    tracker.docsUpperBound = -1;
    
    XmlUpdateRequestHandler handler = new XmlUpdateRequestHandler();
    handler.init( null );
    
    SolrCore core = SolrCore.getSolrCore();
    MapSolrParams params = new MapSolrParams( new HashMap<String, String>() );
    
    // Add a single document
    SolrQueryResponse rsp = new SolrQueryResponse();
    SolrQueryRequestBase req = new SolrQueryRequestBase( core, params ) {};
    req.setContentStreams( toContentStreams(
      adoc("id", "529", "field_t", "what's inside?", "subject", "info"), null ) );
    handler.handleRequest( req, rsp );

    // Check it it is in the index
    assertQ("shouldn't find any", req("id:529") ,"//result[@numFound=0]" );

    // Wait longer then the autocommit time
    Thread.sleep( 1000 );
    req.setContentStreams( toContentStreams(
      adoc("id", "530", "field_t", "what's inside?", "subject", "info"), null ) );
    handler.handleRequest( req, rsp );
      
    // Now make sure we can find it
    assertQ("should find one", req("id:529") ,"//result[@numFound=1]" );
    // But not this one
    assertQ("should find none", req("id:530") ,"//result[@numFound=0]" );
    
    // now make the call 10 times really fast and make sure it 
    // only commits once
    req.setContentStreams( toContentStreams(
	      adoc("id", "500" ), null ) );
    for( int i=0;i<10; i++ ) {
    	handler.handleRequest( req, rsp );
    }
    assertQ("should not be there yet", req("id:500") ,"//result[@numFound=0]" );
    assertEquals( 1, tracker.autoCommitCount );
    
    // Wait longer then the autocommit time
    Thread.sleep( 1000 );
    req.setContentStreams( toContentStreams(
      adoc("id", "531", "field_t", "what's inside?", "subject", "info"), null ) );

    assertQ("now it should", req("id:500") ,"//result[@numFound=1]" );
    assertQ("but not this", req("id:531") ,"//result[@numFound=0]" );
    assertEquals( 2, tracker.autoCommitCount );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/378.java