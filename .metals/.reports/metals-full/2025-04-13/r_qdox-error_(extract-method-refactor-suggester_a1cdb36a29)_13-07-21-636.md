error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/307.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/307.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/307.java
text:
```scala
protected S@@tring pathPrefix = null; // strip this from the beginning of a path

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

package org.apache.solr.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.core.Config;
import org.apache.solr.core.SolrConfig;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.QueryResponseWriter;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryResponse;
import org.apache.solr.request.SolrRequestHandler;

/**
 * This filter looks at the incoming URL maps them to handlers defined in solrconfig.xml
 */
public class SolrDispatchFilter implements Filter 
{
  final Logger log = Logger.getLogger(SolrDispatchFilter.class.getName());
    
  protected SolrCore core;
  protected SolrRequestParsers parsers;
  protected boolean handleSelect = false;
  protected String pathPrefix = null; // strip this from the begging of a path
  protected String abortErrorMessage = null;
  
  public void init(FilterConfig config) throws ServletException 
  {
    log.info("SolrDispatchFilter.init()");

    boolean abortOnConfigurationError = true;
    try {
      // web.xml configuration
      this.pathPrefix = config.getInitParameter( "path-prefix" );
      
      log.info("user.dir=" + System.getProperty("user.dir"));
      core = SolrCore.getSolrCore();
      
      // Read the configuration
      Config solrConfig = core.getSolrConfig();

      long uploadLimitKB = solrConfig.getInt( 
          "requestDispatcher/requestParsers/@multipartUploadLimitInKB", 2000 ); // 2MB default
      
      boolean enableRemoteStreams = solrConfig.getBool( 
          "requestDispatcher/requestParsers/@enableRemoteStreaming", false ); 

      parsers = new SolrRequestParsers( enableRemoteStreams, uploadLimitKB );
      
      // Let this filter take care of /select?xxx format
      this.handleSelect = solrConfig.getBool( "requestDispatcher/@handleSelect", false ); 
      
      // should it keep going if we hit an error?
      abortOnConfigurationError = solrConfig.getBool("abortOnConfigurationError",true);
    }
    catch( Throwable t ) {
      // catch this so our filter still works
      log.log(Level.SEVERE, "Could not start SOLR. Check solr/home property", t);
      SolrConfig.severeErrors.add( t );
      SolrCore.log( t );
    }
    
    // Optionally abort if we found a sever error
    if( abortOnConfigurationError && SolrConfig.severeErrors.size() > 0 ) {
      StringWriter sw = new StringWriter();
      PrintWriter out = new PrintWriter( sw );
      out.println( "Severe errors in solr configuration.\n" );
      out.println( "Check your log files for more detailed information on what may be wrong.\n" );
      out.println( "If you want solr to continue after configuration errors, change: \n");
      out.println( " <abortOnConfigurationError>false</abortOnConfigurationError>\n" );
      out.println( "in solrconfig.xml\n" );
      
      for( Throwable t : SolrConfig.severeErrors ) {
        out.println( "-------------------------------------------------------------" );
        t.printStackTrace( out );
      }
      out.flush();
      
      // Servlet containers behave slightly differently if you throw an exception during 
      // initialization.  Resin will display that error for every page, jetty prints it in
      // the logs, but continues normally.  (We will see a 404 rather then the real error)
      // rather then leave the behavior undefined, lets cache the error and spit it out 
      // for every request.
      abortErrorMessage = sw.toString();
      //throw new ServletException( abortErrorMessage );
    }
    
    log.info("SolrDispatchFilter.init() done");
  }

  public void destroy() {
    core.close();
  }
  
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
  {
    if( abortErrorMessage != null ) {
      ((HttpServletResponse)response).sendError( 500, abortErrorMessage );
      return;
    }
    
    if( request instanceof HttpServletRequest) {
      SolrQueryRequest solrReq = null;
      HttpServletRequest req = (HttpServletRequest)request;
      try {
        String path = req.getServletPath();    
        if( req.getPathInfo() != null ) {
          // this lets you handle /update/commit when /update is a servlet
          path += req.getPathInfo(); 
        }
        if( pathPrefix != null && path.startsWith( pathPrefix ) ) {
          path = path.substring( pathPrefix.length() );
        }
        
        int idx = path.indexOf( ':' );
        if( idx > 0 ) {
          // save the portion after the ':' for a 'handler' path parameter
          path = path.substring( 0, idx );
        }
        
        SolrRequestHandler handler = null;
        if( path.length() > 1 ) { // don't match "" or "/" as valid path
          handler = core.getRequestHandler( path );
        }
        if( handler == null && handleSelect ) {
          if( "/select".equals( path ) || "/select/".equals( path ) ) {
            solrReq = parsers.parse( core, path, req );
            String qt = solrReq.getParams().get( CommonParams.QT );
            if( qt != null && qt.startsWith( "/" ) ) {
              throw new SolrException( SolrException.ErrorCode.BAD_REQUEST, "Invalid query type.  Do not use /select to access: "+qt);
            }
            handler = core.getRequestHandler( qt );
            if( handler == null ) {
              throw new SolrException( SolrException.ErrorCode.BAD_REQUEST, "unknown handler: "+qt);
            }
          }
        }
        if( handler != null ) {
          if( solrReq == null ) {
            solrReq = parsers.parse( core, path, req );
          }
          SolrQueryResponse solrRsp = new SolrQueryResponse();
          this.execute( req, handler, solrReq, solrRsp );
          if( solrRsp.getException() != null ) {
            sendError( (HttpServletResponse)response, solrRsp.getException() );
            return;
          }
          
          // Now write it out
          QueryResponseWriter responseWriter = core.getQueryResponseWriter(solrReq);
          response.setContentType(responseWriter.getContentType(solrReq, solrRsp));
          PrintWriter out = response.getWriter();
          responseWriter.write(out, solrReq, solrRsp);
          return;
        }
      }
      catch( Throwable ex ) {
        sendError( (HttpServletResponse)response, ex );
        return;
      }
      finally {
        if( solrReq != null ) {
          solrReq.close();
        }
      }
    }
    
    // Otherwise let the webapp handle the request
    chain.doFilter(request, response);
  }

  protected void execute( HttpServletRequest req, SolrRequestHandler handler, SolrQueryRequest sreq, SolrQueryResponse rsp) {
    // a custom filter could add more stuff to the request before passing it on.
    // for example: sreq.getContext().put( "HttpServletRequest", req );
    core.execute( handler, sreq, rsp );
  }
  
  protected void sendError(HttpServletResponse res, Throwable ex) throws IOException 
  {
    int code=500;
    String trace = "";
    if( ex instanceof SolrException ) {
      code = ((SolrException)ex).code();
    }
    
    // For any regular code, don't include the stack trace
    if( code == 500 || code < 100 ) {  
      StringWriter sw = new StringWriter();
      ex.printStackTrace(new PrintWriter(sw));
      trace = "\n\n"+sw.toString();
      
      SolrException.logOnce(log,null,ex );
      
      // non standard codes have undefined results with various servers
      if( code < 100 ) {
        log.warning( "invalid return code: "+code );
        code = 500;
      }
    }
    res.sendError( code, ex.getMessage() + trace );
  }

  //---------------------------------------------------------------------
  //---------------------------------------------------------------------

  /**
   * Should the filter handle /select even if it is not mapped in solrconfig.xml
   * 
   * This will use consistent error handling for /select?qt=xxx and /update/xml
   * 
   */
  public boolean isHandleSelect() {
    return handleSelect;
  }

  public void setHandleSelect(boolean handleSelect) {
    this.handleSelect = handleSelect;
  }

  /**
   * set the prefix for all paths.  This is useful if you want to apply the
   * filter to something other then *.  
   * 
   * For example, if web.xml specifies:
   * 
   * <filter-mapping>
   *  <filter-name>SolrRequestFilter</filter-name>
   *  <url-pattern>/xxx/*</url-pattern>
   * </filter-mapping>
   * 
   * Make sure to set the PathPrefix to "/xxx" either with this function
   * or in web.xml
   * 
   * <init-param>
   *  <param-name>path-prefix</param-name>
   *  <param-value>/xxx</param-value>
   * </init-param>
   * 
   */
  public void setPathPrefix(String pathPrefix) {
    this.pathPrefix = pathPrefix;
  }

  public String getPathPrefix() {
    return pathPrefix;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/307.java