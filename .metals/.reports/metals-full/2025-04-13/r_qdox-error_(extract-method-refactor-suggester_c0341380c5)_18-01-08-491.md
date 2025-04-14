error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2557.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2557.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2557.java
text:
```scala
a@@dminRequestParser = new SolrRequestParsers(new Config(null,"solr",new ByteArrayInputStream("<root/>".getBytes("UTF-8")),"") );

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
import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.WeakHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.core.*;
import org.apache.solr.request.*;
import org.apache.solr.response.BinaryQueryResponseWriter;
import org.apache.solr.response.QueryResponseWriter;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.servlet.cache.HttpCacheHeaderUtil;
import org.apache.solr.servlet.cache.Method;

/**
 * This filter looks at the incoming URL maps them to handlers defined in solrconfig.xml
 *
 * @since solr 1.2
 */
public class SolrDispatchFilter implements Filter
{
  final Logger log = LoggerFactory.getLogger(SolrDispatchFilter.class);

  protected CoreContainer cores;
  protected String pathPrefix = null; // strip this from the beginning of a path
  protected String abortErrorMessage = null;
  protected String solrConfigFilename = null;
  protected final Map<SolrConfig, SolrRequestParsers> parsers = new WeakHashMap<SolrConfig, SolrRequestParsers>();
  protected final SolrRequestParsers adminRequestParser;

  public SolrDispatchFilter() {
    try {
      adminRequestParser = new SolrRequestParsers(new Config(null,"solr",new ByteArrayInputStream("<root/>".getBytes()),"") );
    } catch (Exception e) {
      //unlikely
      throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,e);
    }
  }

  public void init(FilterConfig config) throws ServletException
  {
    log.info("SolrDispatchFilter.init()");

    boolean abortOnConfigurationError = true;
    CoreContainer.Initializer init = createInitializer();
    try {
      // web.xml configuration
      this.pathPrefix = config.getInitParameter( "path-prefix" );
      init.setSolrConfigFilename(config.getInitParameter("solrconfig-filename"));

      this.cores = init.initialize();
      abortOnConfigurationError = init.isAbortOnConfigurationError();
      log.info("user.dir=" + System.getProperty("user.dir"));
    }
    catch( Throwable t ) {
      // catch this so our filter still works
      log.error( "Could not start Solr. Check solr/home property", t);
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
      out.println( "in "+init.getSolrConfigFilename()+"\n" );

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

  /** Method to override to change how CoreContainer initialization is performed. */
  protected CoreContainer.Initializer createInitializer() {
    return new CoreContainer.Initializer();
  }
  
  public void destroy() {
    if (cores != null) {
      cores.shutdown();
      cores = null;
    }    
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    if( abortErrorMessage != null ) {
      ((HttpServletResponse)response).sendError( 500, abortErrorMessage );
      return;
    }

    if( request instanceof HttpServletRequest) {
      HttpServletRequest req = (HttpServletRequest)request;
      HttpServletResponse resp = (HttpServletResponse)response;
      SolrRequestHandler handler = null;
      SolrQueryRequest solrReq = null;
      SolrCore core = null;
      String corename = "";
      try {
        // put the core container in request attribute
        req.setAttribute("org.apache.solr.CoreContainer", cores);
        String path = req.getServletPath();
        if( req.getPathInfo() != null ) {
          // this lets you handle /update/commit when /update is a servlet
          path += req.getPathInfo();
        }
        if( pathPrefix != null && path.startsWith( pathPrefix ) ) {
          path = path.substring( pathPrefix.length() );
        }
        // check for management path
        String alternate = cores.getManagementPath();
        if (alternate != null && path.startsWith(alternate)) {
          path = path.substring(0, alternate.length());
        }
        // unused feature ?
        int idx = path.indexOf( ':' );
        if( idx > 0 ) {
          // save the portion after the ':' for a 'handler' path parameter
          path = path.substring( 0, idx );
        }

        // Check for the core admin page
        if( path.equals( cores.getAdminPath() ) ) {
          handler = cores.getMultiCoreHandler();
          solrReq =  adminRequestParser.parse(null,path, req);
          handleAdminRequest(req, response, handler, solrReq);
          return;
        }
        else {
          //otherwise, we should find a core from the path
          idx = path.indexOf( "/", 1 );
          if( idx > 1 ) {
            // try to get the corename as a request parameter first
            corename = path.substring( 1, idx );
            core = cores.getCore(corename);
            if (core != null) {
              path = path.substring( idx );
            }
          }
          if (core == null) {
            corename = "";
            core = cores.getCore("");
          }
        }

        // With a valid core...
        if( core != null ) {
          final SolrConfig config = core.getSolrConfig();
          // get or create/cache the parser for the core
          SolrRequestParsers parser = null;
          parser = parsers.get(config);
          if( parser == null ) {
            parser = new SolrRequestParsers(config);
            parsers.put(config, parser );
          }

          // Determine the handler from the url path if not set
          // (we might already have selected the cores handler)
          if( handler == null && path.length() > 1 ) { // don't match "" or "/" as valid path
            handler = core.getRequestHandler( path );
            // no handler yet but allowed to handle select; let's check
            if( handler == null && parser.isHandleSelect() ) {
              if( "/select".equals( path ) || "/select/".equals( path ) ) {
                solrReq = parser.parse( core, path, req );
                String qt = solrReq.getParams().get( CommonParams.QT );
                handler = core.getRequestHandler( qt );
                if( handler == null ) {
                  throw new SolrException( SolrException.ErrorCode.BAD_REQUEST, "unknown handler: "+qt);
                }
              }
            }
          }

          // With a valid handler and a valid core...
          if( handler != null ) {
            // if not a /select, create the request
            if( solrReq == null ) {
              solrReq = parser.parse( core, path, req );
            }

            final Method reqMethod = Method.getMethod(req.getMethod());
            HttpCacheHeaderUtil.setCacheControlHeader(config, resp, reqMethod);
            // unless we have been explicitly told not to, do cache validation
            // if we fail cache validation, execute the query
            if (config.getHttpCachingConfig().isNever304() ||
                !HttpCacheHeaderUtil.doCacheHeaderValidation(solrReq, req, reqMethod, resp)) {
                SolrQueryResponse solrRsp = new SolrQueryResponse();
                /* even for HEAD requests, we need to execute the handler to
                 * ensure we don't get an error (and to make sure the correct
                 * QueryResponseWriter is selected and we get the correct
                 * Content-Type)
                 */
                this.execute( req, handler, solrReq, solrRsp );
                HttpCacheHeaderUtil.checkHttpCachingVeto(solrRsp, resp, reqMethod);
              // add info to http headers
              //TODO: See SOLR-232 and SOLR-267.  
                /*try {
                  NamedList solrRspHeader = solrRsp.getResponseHeader();
                 for (int i=0; i<solrRspHeader.size(); i++) {
                   ((javax.servlet.http.HttpServletResponse) response).addHeader(("Solr-" + solrRspHeader.getName(i)), String.valueOf(solrRspHeader.getVal(i)));
                 }
                } catch (ClassCastException cce) {
                  log.log(Level.WARNING, "exception adding response header log information", cce);
                }*/
               QueryResponseWriter responseWriter = core.getQueryResponseWriter(solrReq);
              writeResponse(solrRsp, response, responseWriter, solrReq, reqMethod);
            }
            return; // we are done with a valid handler
          }
          // otherwise (we have a core), let's ensure the core is in the SolrCore request attribute so
          // a servlet/jsp can retrieve it
          else {
            req.setAttribute("org.apache.solr.SolrCore", core);
            // Modify the request so each core gets its own /admin
            if( path.startsWith( "/admin" ) ) {
              req.getRequestDispatcher( pathPrefix == null ? path : pathPrefix + path ).forward( request, response );
              return;
            }
          }
        }
        log.debug("no handler or core retrieved for " + path + ", follow through...");
      } 
      catch (Throwable ex) {
        sendError( (HttpServletResponse)response, ex );
        return;
      } 
      finally {
        if( solrReq != null ) {
          solrReq.close();
        }
        if (core != null) {
          core.close();
        }
      }
    }

    // Otherwise let the webapp handle the request
    chain.doFilter(request, response);
  }

  private void handleAdminRequest(HttpServletRequest req, ServletResponse response, SolrRequestHandler handler,
                                  SolrQueryRequest solrReq) throws IOException {
    SolrQueryResponse solrResp = new SolrQueryResponse();
    final NamedList<Object> responseHeader = new SimpleOrderedMap<Object>();
    solrResp.add("responseHeader", responseHeader);
    NamedList toLog = solrResp.getToLog();
    toLog.add("webapp", req.getContextPath());
    toLog.add("path", solrReq.getContext().get("path"));
    toLog.add("params", "{" + solrReq.getParamString() + "}");
    handler.handleRequest(solrReq, solrResp);
    SolrCore.setResponseHeaderValues(handler, solrReq, solrResp);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < toLog.size(); i++) {
      String name = toLog.getName(i);
      Object val = toLog.getVal(i);
      sb.append(name).append("=").append(val).append(" ");
    }
    QueryResponseWriter respWriter = SolrCore.DEFAULT_RESPONSE_WRITERS.get(solrReq.getParams().get(CommonParams.WT));
    if (respWriter == null) respWriter = SolrCore.DEFAULT_RESPONSE_WRITERS.get("standard");
    writeResponse(solrResp, response, respWriter, solrReq, Method.getMethod(req.getMethod()));
  }

  private void writeResponse(SolrQueryResponse solrRsp, ServletResponse response,
                             QueryResponseWriter responseWriter, SolrQueryRequest solrReq, Method reqMethod)
          throws IOException {
    if (solrRsp.getException() != null) {
      sendError((HttpServletResponse) response, solrRsp.getException());
    } else {
      // Now write it out
      final String ct = responseWriter.getContentType(solrReq, solrRsp);
      // don't call setContentType on null
      if (null != ct) response.setContentType(ct); 
      if (Method.HEAD != reqMethod) {
        if (responseWriter instanceof BinaryQueryResponseWriter) {
          BinaryQueryResponseWriter binWriter = (BinaryQueryResponseWriter) responseWriter;
          binWriter.write(response.getOutputStream(), solrReq, solrRsp);
        } else {
          PrintWriter out = response.getWriter();
          responseWriter.write(out, solrReq, solrRsp);

        }
      }
      //else http HEAD request, nothing to write out, waited this long just to get ContentType
    }
  }

  protected void execute( HttpServletRequest req, SolrRequestHandler handler, SolrQueryRequest sreq, SolrQueryResponse rsp) {
    // a custom filter could add more stuff to the request before passing it on.
    // for example: sreq.getContext().put( "HttpServletRequest", req );
    // used for logging query stats in SolrCore.execute()
    sreq.getContext().put( "webapp", req.getContextPath() );
    sreq.getCore().execute( handler, sreq, rsp );
  }

  protected void sendError(HttpServletResponse res, Throwable ex) throws IOException {
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
        log.warn( "invalid return code: "+code );
        code = 500;
      }
    }
    res.sendError( code, ex.getMessage() + trace );
  }

  //---------------------------------------------------------------------
  //---------------------------------------------------------------------

  /**
   * Set the prefix for all paths.  This is useful if you want to apply the
   * filter to something other then /*, perhaps because you are merging this
   * filter into a larger web application.
   *
   * For example, if web.xml specifies:
   *
   * <filter-mapping>
   *  <filter-name>SolrRequestFilter</filter-name>
   *  <url-pattern>/xxx/*</url-pattern>
   * </filter-mapping>
   *
   * Make sure to set the PathPrefix to "/xxx" either with this function
   * or in web.xml.
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2557.java