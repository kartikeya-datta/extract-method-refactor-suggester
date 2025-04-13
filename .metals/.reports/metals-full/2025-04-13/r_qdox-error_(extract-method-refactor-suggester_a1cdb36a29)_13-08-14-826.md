error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2018.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2018.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2018.java
text:
```scala
O@@utputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");

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

package org.apache.solr.client.solrj.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.params.UpdateParams;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StreamingHttpSolrServer buffers all added documents and writes them
 * into open http connections. This class is thread safe.
 * 
 * Although any SolrServer request can be made with this implementation, 
 * it is only recommended to use the {@link StreamingUpdateSolrServer} with
 * /update requests.  The query interface is better suited for 
 * 
 * @version $Id: CommonsHttpSolrServer.java 724175 2008-12-07 19:07:11Z ryan $
 * @since solr 1.4
 */
public class StreamingUpdateSolrServer extends CommonsHttpSolrServer
{
  static final Logger log = LoggerFactory.getLogger( StreamingUpdateSolrServer.class );
  
  final BlockingQueue<UpdateRequest> queue;
  final ExecutorService scheduler = Executors.newCachedThreadPool();
  final String updateUrl = "/update";
  final Queue<Runner> runners;
  volatile CountDownLatch lock = null;  // used to block everything
  final int threadCount;
  
  public StreamingUpdateSolrServer(String solrServerUrl, int queueSize, int threadCount ) throws MalformedURLException  {
    super( solrServerUrl );
    queue = new LinkedBlockingQueue<UpdateRequest>( queueSize );
    this.threadCount = threadCount;
    runners = new LinkedList<Runner>();
  }

  /**
   * Opens a connection and sends everything...
   */
  class Runner implements Runnable {
    final Lock runnerLock = new ReentrantLock();

    public void run() {
      runnerLock.lock();

      // info is ok since this should only happen once for each thread
      log.info( "starting runner: {}" , this );
      PostMethod method = null;
      try {
        RequestEntity request = new RequestEntity() {
          // we don't know the length
          public long getContentLength() { return -1; }
          public String getContentType() { return ClientUtils.TEXT_XML; }
          public boolean isRepeatable()  { return false; }
  
          public void writeRequest(OutputStream out) throws IOException {
            try {
              OutputStreamWriter writer = new OutputStreamWriter( out );
              writer.append( "<stream>" ); // can be anything...
              UpdateRequest req = queue.poll( 250, TimeUnit.MILLISECONDS );
              while( req != null ) {
                log.debug( "sending: {}" , req );
                req.writeXML( writer ); 
                
                // check for commit or optimize
                SolrParams params = req.getParams();
                if( params != null ) {
                  String fmt = null;
                  if( params.getBool( UpdateParams.OPTIMIZE, false ) ) {
                    fmt = "<optimize waitSearcher=\"%s\" waitFlush=\"%s\" />";
                  }
                  else if( params.getBool( UpdateParams.COMMIT, false ) ) {
                    fmt = "<commit waitSearcher=\"%s\" waitFlush=\"%s\" />";
                  }
                  if( fmt != null ) {
                    log.info( fmt );
                    writer.write( String.format( fmt, 
                        params.getBool( UpdateParams.WAIT_SEARCHER, false )+"",
                        params.getBool( UpdateParams.WAIT_FLUSH, false )+"") );
                  }
                }
                
                writer.flush();
                req = queue.poll( 250, TimeUnit.MILLISECONDS );
              }
              writer.append( "</stream>" );
              writer.flush();
            }
            catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        };
        
        method = new PostMethod(_baseURL+updateUrl );
        method.setRequestEntity( request );
        method.setFollowRedirects( false );
        method.addRequestHeader( "User-Agent", AGENT );
        
        int statusCode = getHttpClient().executeMethod(method);
        if (statusCode != HttpStatus.SC_OK) {
          StringBuilder msg = new StringBuilder();
          msg.append( method.getStatusLine().getReasonPhrase() );
          msg.append( "\n\n" );
          msg.append( method.getStatusText() );
          msg.append( "\n\n" );
          msg.append( "request: "+method.getURI() );
          handleError( new Exception( msg.toString() ) );
        }
      }
      catch (Throwable e) {
        handleError( e );
      } 
      finally {
        try {
          // make sure to release the connection
          method.releaseConnection();
        }
        catch( Exception ex ){}
        
        // remove it from the list of running things...
        synchronized (runners) {
          runners.remove( this );
        }
        log.info( "finished: {}" , this );
        runnerLock.unlock();
      }
    }
  }
  
  @Override
  public NamedList<Object> request( final SolrRequest request ) throws SolrServerException, IOException
  {
    if( !(request instanceof UpdateRequest) ) {
      return super.request( request );
    }
    UpdateRequest req = (UpdateRequest)request;
    
    // this happens for commit...
    if( req.getDocuments()==null || req.getDocuments().isEmpty() ) {
      blockUntilFinished();
      return super.request( request );
    }

    SolrParams params = req.getParams();
    if( params != null ) {
      // check if it is waiting for the searcher
      if( params.getBool( UpdateParams.WAIT_SEARCHER, false ) ) {
        log.info( "blocking for commit/optimize" );
        blockUntilFinished();  // empty the queue
        return super.request( request );
      }
    }
    
    try {
      CountDownLatch tmpLock = lock;
      if( tmpLock != null ) {
        tmpLock.await();
      }

      queue.put( req );
      
      if( runners.isEmpty() 
 (queue.remainingCapacity() < queue.size() 
         && runners.size() < threadCount) ) 
      {
        synchronized( runners ) {
          Runner r = new Runner();
          scheduler.execute( r );
          runners.add( r );
        }
      }
    } 
    catch (InterruptedException e) {
      log.error( "interuped", e );
      throw new IOException( e.getLocalizedMessage() );
    }
    
    // RETURN A DUMMY result
    NamedList<Object> dummy = new NamedList<Object>();
    dummy.add( "NOTE", "the request is processed in a background stream" );
    return dummy;
  }

  public synchronized void blockUntilFinished()
  {
    lock = new CountDownLatch(1);
    try {
      // Wait until no runners are running
      Runner runner = runners.peek();
      while( runner != null ) {
        runner.runnerLock.lock();
        runner.runnerLock.unlock();
        runner = runners.peek();
      }
    } finally {
      lock.countDown();
      lock=null;
    }
  }
  
  public void handleError( Throwable ex )
  {
    log.error( "error", ex );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2018.java