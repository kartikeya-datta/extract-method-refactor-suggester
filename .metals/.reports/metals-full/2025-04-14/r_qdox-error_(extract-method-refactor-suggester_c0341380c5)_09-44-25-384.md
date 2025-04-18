error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8601.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8601.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8601.java
text:
```scala
S@@ystem.err.println("Creating Standalone Persistent Unit  " + _unit);

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */

package org.apache.openjpa.persistence.jest;

import java.io.IOException;

import javax.persistence.Persistence;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.openjpa.kernel.AbstractBrokerFactory;
import org.apache.openjpa.kernel.BrokerFactory;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.persistence.JPAFacadeHelper;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;
import org.apache.openjpa.persistence.OpenJPAPersistence;

/**
 * A specialized HTTP servlet to interpret HTTP requests as Java Persistent API commands
 * on a running persistence unit. The persistence unit is identified by the name of the
 * unit and is supplied to this servlet during its initialization. The component using
 * the persistent unit and this servlet must be within the same module scope.
 * <p>
 * The syntax of the request URL is described in 
 * <a href="https://cwiki.apache.org/openjpa/jest-syntax.html">OpenJPA web site</a>.
 * <p>
 * The response to a resource request is represented in various format, namely  
 * XML, JSON or a JavaScript that will dynamically render in the browser. The format
 * can be controlled via the initialization parameter <code>response.format</code> in 
 * <code>&lt;init-param&gt;</code> clause or per request basis via <code>format=xml|dojo|json</code> 
 * encoded in the path expression of the Request URI. 
 * <p>
 * Servlet initialization parameter
 * <table cellspacing="20px">
 * <tr><th>Parameter</th><th>Value</th></tr>
 * <tr><td>persistence.unit</td><td>Name of the persistence unit. Mandatory</td></tr>
 * <tr><td>response.format</td><td>Default format used for representation. Defaults to <code>xml</code>.</td></tr>
 * </table>
 * <br>
 * @author Pinaki Poddar
 *
 */
@SuppressWarnings("serial")
public class JESTServlet extends HttpServlet  {
    /**
     * Servlet initialization parameter monikers
     */
    public static final String INIT_PARA_UNIT       = "persistence.unit";
    public static final String INIT_PARA_STANDALONE = "standalone";
    
    
    private String _unit;
    private boolean _debug;
    private OpenJPAEntityManagerFactory _emf;
    protected static Localizer _loc = Localizer.forPackage(JESTServlet.class);
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        _debug = "true".equalsIgnoreCase(config.getInitParameter("debug"));
        _unit = config.getInitParameter(INIT_PARA_UNIT);
        if (_unit == null) {
            throw new ServletException(_loc.get("no-persistence-unit-param").toString());
        }
        boolean standalone = "true".equalsIgnoreCase(config.getInitParameter(INIT_PARA_STANDALONE));
        System.err.println("Standalone Deployment Mode " + standalone);
        if (standalone) {
            createPersistenceUnit();
        }
        if (findPersistenceUnit()) {
            config.getServletContext().log(_loc.get("servlet-init", _unit).toString());
        } else {
            config.getServletContext().log(_loc.get("servlet-not-init", _unit).toString());
        }
    }
    
    /**
     * Peeks into the servlet path of the request to create appropriate {@link JESTCommand JEST command}.
     * Passes the request on to the command which is responsible for generating a response. 
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        debug(request);
        if (findPersistenceUnit()) {
            JESTContext ctx = new JESTContext(_unit, _emf, request, response);
            try {
                ctx.execute();
            } catch (Exception e) {
                handleError(ctx, e);
            }
        } else {
            throw new ServletException(_loc.get("no-persistence-unit", _unit).toString());
        } 
    }
    
    protected void createPersistenceUnit() throws ServletException {
        try {
            System.err.println("Creating Standalone Persistent Unit  " + _unit + ":" + _emf);
            _emf = OpenJPAPersistence.cast(Persistence.createEntityManagerFactory(_unit));
            System.err.println("Created Standalone Persistent Unit  " + _unit + ":" + _emf);
        } catch (Exception e) {
            System.err.println("Can not creating Standalone Persistent Unit  " + _unit);
            e.printStackTrace();
            throw new ServletException(_loc.get("no-persistence-unit-standalone", _unit).toString(), e);
        } 
    }
    
    protected boolean findPersistenceUnit() {
        if (_emf == null) {
            System.err.println("Discovering auxiliary Persistent Unit  " + _unit);
            BrokerFactory bf = AbstractBrokerFactory.getPooledFactoryForKey(_unit);
            if (bf != null) {
                _emf = (OpenJPAEntityManagerFactory)bf.getUserObject(JPAFacadeHelper.EMF_KEY);
            }
            System.err.println("Discovered auxiliary Persistent Unit  " + _unit + ":" + _emf);
        }
        return _emf != null;
    }
    
    protected void handleError(JPAServletContext ctx, Throwable t) throws IOException {
        if (t instanceof ProcessingException) {
            ((ProcessingException)t).printStackTrace();
        } else {
            new ProcessingException(ctx, t).printStackTrace();
        }
    }

    @Override
    public void destroy() {
        _emf = null;
        _unit = null;;
    }
    
    private void debug(HttpServletRequest r) {
        if (!_debug) return;
//        log("-----------------------------------------------------------");
        log(r.getRemoteUser() + "@" + r.getRemoteHost() + ":" + r.getRemotePort() + "[" + r.getPathInfo() + "]");
//        log("Request URL    = [" + request.getRequestURL() + "]");
//        log("Request URI    = [" + request.getRequestURI() + "]");
//        log("Servlet Path   = [" + request.getServletPath() + "]");
//        log("Context Path   = [" + request.getContextPath() + "]");
//        log("Path Info      = [" + request.getPathInfo() + "]");
//        log("Path Translated = [" + request.getPathTranslated() + "]");
    }
    
    public void log(String s) {
        System.err.println(s);
        super.log(s);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8601.java