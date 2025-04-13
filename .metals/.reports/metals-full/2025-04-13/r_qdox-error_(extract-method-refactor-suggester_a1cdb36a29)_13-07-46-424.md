error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7500.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7500.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7500.java
text:
```scala
h@@ttpService.unregister("/example-interceptor/servlet");

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.as.testsuite.integration.osgi.interceptor.bundle;

import javax.servlet.http.HttpServlet;

import org.jboss.logging.Logger;
import org.jboss.osgi.deployment.interceptor.AbstractLifecycleInterceptor;
import org.jboss.osgi.deployment.interceptor.InvocationContext;
import org.jboss.osgi.deployment.interceptor.LifecycleInterceptorException;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;

/**
 * An interceptor that publishes HttpMetadata.
 *
 * @author thomas.diesler@jboss.com
 * @since 23-Oct-2009
 */
public class PublisherInterceptor extends AbstractLifecycleInterceptor {
    // Provide logging
    private static final Logger log = Logger.getLogger(PublisherInterceptor.class);

    PublisherInterceptor() {
        // Add the required input
        addInput(HttpMetadata.class);
    }

    public void invoke(int state, InvocationContext context) {
        // HttpMetadata is guaratied to be available because we registered
        // this type as required input
        HttpMetadata metadata = context.getAttachment(HttpMetadata.class);

        // Register HttpMetadata on STARTING
        if (state == Bundle.STARTING) {
            String servletName = metadata.getServletName();
            try {
                log.info("Publish HttpMetadata: " + metadata);

                // Load the endpoint servlet from the bundle
                Bundle bundle = context.getBundle();
                Class<?> servletClass = bundle.loadClass(servletName);
                HttpServlet servlet = (HttpServlet) servletClass.newInstance();

                ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
                try {
                    // Register the servlet with the HttpService
                    HttpService httpService = getHttpService(context, true);
                    httpService.registerServlet("/example-interceptor/servlet", servlet, null, null);
                } finally {
                    // [AS7-903] 3rd party code may leak TCCL
                    Thread.currentThread().setContextClassLoader(ctxLoader);
                }
            } catch (RuntimeException rte) {
                throw rte;
            } catch (Exception ex) {
                throw new LifecycleInterceptorException("Cannot publish: " + servletName, ex);
            }
        }

        // Unregister the endpoint on STOPPING
        else if (state == Bundle.STOPPING) {
            log.info("Unpublish HttpMetadata: " + metadata);
            ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
            try {
                HttpService httpService = getHttpService(context, false);
                if (httpService != null)
                    httpService.unregister("/servlet");
            } finally {
                // [AS7-903] 3rd party code may leak TCCL
                Thread.currentThread().setContextClassLoader(ctxLoader);
            }
        }
    }

    private HttpService getHttpService(InvocationContext context, boolean required) {
        BundleContext bndContext = context.getBundle().getBundleContext();
        ServiceReference sref = bndContext.getServiceReference(HttpService.class.getName());
        if (sref == null && required == true)
            throw new IllegalStateException("Required HttpService not available");

        HttpService httpService = (HttpService) bndContext.getService(sref);
        return httpService;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7500.java