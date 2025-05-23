error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3414.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3414.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3414.java
text:
```scala
O@@bject contextPath = headers.get(WebExtension.WEB_CONTEXTPATH);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.as.osgi.web;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.ServletContext;

import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.osgi.OSGiLogger;
import org.jboss.as.osgi.OSGiMessages;
import org.jboss.as.web.host.ContextActivator;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.osgi.deployment.interceptor.AbstractLifecycleInterceptor;
import org.jboss.osgi.deployment.interceptor.InvocationContext;
import org.jboss.osgi.deployment.interceptor.LifecycleInterceptor;
import org.jboss.osgi.deployment.interceptor.LifecycleInterceptorException;
import org.jboss.osgi.framework.Services;
import org.jboss.osgi.resolver.XBundle;
import org.jboss.osgi.resolver.XBundleRevision;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

/**
 * {@link LifecycleInterceptor} for webapp bundles.
 *
 * @author Thomas.Diesler@jboss.com
 * @since 26-Jun-2012
 */
class WebContextLifecycleInterceptor extends AbstractLifecycleInterceptor implements Service<LifecycleInterceptor> {

    static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("as", "osgi", "web").append(WebContextLifecycleInterceptor.class.getSimpleName());

    private final InjectedValue<BundleContext> injectedSystemContext = new InjectedValue<BundleContext>();
    private ServiceRegistration<LifecycleInterceptor> registration;

    static ServiceController<LifecycleInterceptor> addService(ServiceTarget serviceTarget, ServiceVerificationHandler verificationHandler) {
        WebContextLifecycleInterceptor service = new WebContextLifecycleInterceptor();
        ServiceBuilder<LifecycleInterceptor> builder = serviceTarget.addService(SERVICE_NAME, service);
        builder.addDependency(Services.FRAMEWORK_CREATE, BundleContext.class, service.injectedSystemContext);
        builder.addListener(verificationHandler);
        builder.setInitialMode(Mode.ON_DEMAND);
        return builder.install();
    }

    @Override
    public void start(StartContext context) throws StartException {
        BundleContext syscontext = injectedSystemContext.getValue();
        registration = syscontext.registerService(LifecycleInterceptor.class, this, null);
    }

    @Override
    public void stop(StopContext context) {
        if (registration != null)
            registration.unregister();
    }

    @Override
    public void invoke(int state, InvocationContext context) {
        XBundle bundle = (XBundle) context.getBundle();
        XBundleRevision brev = bundle.getBundleRevision();
        ContextActivator activator = brev.getAttachment(WebExtension.CONTEXT_ACTIVATOR_KEY);
        if (activator != null) {
            switch (state) {
                case Bundle.ACTIVE:
                    try {
                        if (!activator.start(30, TimeUnit.SECONDS)) {
                            throw new LifecycleInterceptorException(OSGiMessages.MESSAGES.startContextFailed());
                        }
                        injectBundleContext(activator.getServletContext(), bundle.getBundleContext());
                    } catch (TimeoutException ex) {
                        throw new LifecycleInterceptorException(ex.getMessage(), ex);
                    }
                    break;
                case Bundle.RESOLVED:
                    uninjectBundleContext(activator.getServletContext());
                    activator.stop(30, TimeUnit.SECONDS);
                    break;
            }
        }
    }

    private void injectBundleContext(ServletContext webContext, BundleContext bundleContext) {
        OSGiLogger.LOGGER.debugf("Injecting bundle context %s into %s", bundleContext, webContext);
        webContext.setAttribute(WebExtension.OSGI_BUNDLECONTEXT, bundleContext);
        registerServletContextService(webContext, bundleContext);
    }

    private void uninjectBundleContext(ServletContext webContext) {
        if (webContext != null) {
            OSGiLogger.LOGGER.debugf("Uninjecting bundle context from %s", webContext);
            webContext.removeAttribute(WebExtension.OSGI_BUNDLECONTEXT);
        }
    }

    private void registerServletContextService(ServletContext servletContext, BundleContext bundleContext) {
        Bundle bundle = bundleContext.getBundle();
        Dictionary<String, Object> props = new Hashtable<String, Object>();
        props.put("osgi.web.symbolicname", bundle.getSymbolicName());
        Dictionary<?, ?> headers = bundle.getHeaders();
        // The version of the Web Application Bundle. If no Bundle-Version is specified in the manifest then
        // this property must not be set.
        Object version = headers.get(Constants.BUNDLE_VERSION);
        if (version instanceof String)
            props.put("osgi.web.version", version);

        Object contextPath = headers.get(WebExtension.WEB_CONTEXT_PATH);
        if (contextPath instanceof String)
            props.put("osgi.web.contextpath", contextPath);

        bundleContext.registerService(ServletContext.class.getName(), servletContext, props);
    }

    @Override
    public LifecycleInterceptor getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3414.java