error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/231.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/231.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/231.java
text:
```scala
private final S@@tring DEFAULT_CACHE_NAME = "java:jboss/infinispan/cache/web/default";

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.test.clustering.xsite;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.infinispan.Cache;

/**
 * Servlet providing get/put access to Infinispan cache instance.
 *
 * http://127.0.0.1:8080/cache?operation=get&key=a
 * http://127.0.0.1:8080/cache?operation=put&key=a;value=b
 *
 * where keys are Strings and Values are String representations of int.
 *
 * NOTE: Caches defined in the infinispan subsystem need to be started on demand.
 * On demand in this case means either:
 * (i) through deployment of a distributable web app which uses the cache as a session cache
 * (ii) by way of @Resource(lookup=) which will cause the corresponding JNDI binding instance to be started
 * and so the cache.
 *
 * Cache instances are started in this test case by way of a res-ref in the web.xml file in order to
 * permit parametrization of the JNDI name.
 *
 * @author Richard Achmatowicz
 */
@WebServlet(urlPatterns = {"/cache"})
public class CacheAccessServlet extends HttpServlet {
    private final String OPERATION = "operation";
    private final String GET = "get";
    private final String PUT = "put";
    private final String KEY = "key";
    private final String VALUE = "value";
    private final String DEFAULT_CACHE_NAME = "java:jboss/infinispan/cache/web/repl";

    public static final String URL = "cache";

    // default is java:jboss/infinispan/cache/web/repl
    @Resource(name="TheTargetCache")
    Cache<String,Custom> cache;

    @Override
    public void init() throws ServletException {

        // get the JNDI name of the cache we shall access
        ServletConfig sc = getServletConfig();
        String cacheJNDIName = sc.getInitParameter("jndi-name");
        if (cacheJNDIName == null) {
            cacheJNDIName = DEFAULT_CACHE_NAME ;
        }

        Context ctx = null ;
        try {
            ctx = (Context)new InitialContext();
            cache = (Cache<String,Custom>) ctx.lookup(cacheJNDIName);
            if (cache == null) {
                throw new ServletException(String.format("Unable to access cache with JNDI name '%s'", cacheJNDIName));
            }
        }
        catch(NamingException ne) {
            throw new ServletException(String.format("Problem looking up name for cache '%s'", cacheJNDIName));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String operation = req.getParameter(OPERATION);
        if (operation == null) {
            throw new ServletException(String.format("No '%s' parameter specified)", OPERATION));
        }
        //
        if (operation.equals(GET)) {
            String key = req.getParameter(KEY);
            validateKeyParam(operation, key);
            Custom value = cache.get(key);
            if (value == null) {
                throw new ServletException(String.format("No value is defined for key '%s'",key));
            }
            resp.setIntHeader("value", value.getValue());
            resp.getWriter().write("Success");

        } else if (operation.equals(PUT)) {
            String key = req.getParameter(KEY);
            validateKeyParam(operation, key);
            String value = req.getParameter(VALUE);
            validateValueParam(operation, value);
            int intValue = Integer.parseInt(value);
            // put the new instance of Custom here to the cache
            // todo: difference between putting new value and modifying existing old value
            cache.put(key, new Custom(intValue));
            resp.getWriter().write("Success");
        } else {
            throw new ServletException(String.format("Unknown operation '%s': valid operations are get/put)", operation));
        }
    }

    private void validateKeyParam(String operation, String key) throws ServletException {
        if (key == null || key.length() == 0) {
            throw new ServletException(String.format("key parameter for operation %s is null or has zero length", operation));
        }
    }

    private void validateValueParam(String operation, String value) throws ServletException {
        if (value == null || value.length() == 0) {
            throw new ServletException(String.format("key parameter for operation %s is null or has zero length", operation));
        }
        try {
            Integer.parseInt(value) ;
        }
        catch(NumberFormatException nfe) {
            throw new ServletException(String.format("value parameter for operation %s must be int", operation));
        }
    }

    /*
     * Serializable object holding an int value
     */
    public static class Custom implements Serializable {
        private static final long serialVersionUID = -5129400250276547619L;
        private transient boolean serialized = false;
        private int value;

        public Custom(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public boolean wasSerialized() {
            return this.serialized;
        }

        private void writeObject(java.io.ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            this.serialized = true;
        }

        private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            this.serialized = true;
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/231.java