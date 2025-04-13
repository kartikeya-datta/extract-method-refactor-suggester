error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1880.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1880.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1880.java
text:
```scala
r@@eturn Modifier.isPublic(method.getModifiers()) && !Modifier.isFinal(method.getModifiers()) && componentConfiguration.getComponentDescription().isIntercepted();

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

package org.jboss.as.ee.component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;

import org.jboss.as.ee.logging.EeLogger;
import org.jboss.as.naming.context.NamespaceContextSelector;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.invocation.InterceptorFactory;
import org.jboss.invocation.Interceptors;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

/**
 * A service for creating a component.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public class BasicComponentCreateService implements Service<Component> {
    private final InjectedValue<DeploymentUnit> deploymentUnit = new InjectedValue<DeploymentUnit>();

    private final ServiceName serviceName;
    private final String componentName;
    private final Class<?> componentClass;
    private final InterceptorFactory postConstruct;
    private final InterceptorFactory preDestroy;
    private final Map<Method, InterceptorFactory> componentInterceptors;
    private final NamespaceContextSelector namespaceContextSelector;

    private BasicComponent component;


    /**
     * Construct a new instance.
     *
     * @param componentConfiguration the component configuration
     */
    public BasicComponentCreateService(final ComponentConfiguration componentConfiguration) {
        serviceName = componentConfiguration.getComponentDescription().getCreateServiceName();
        componentName = componentConfiguration.getComponentName();
        postConstruct = Interceptors.getChainedInterceptorFactory(componentConfiguration.getPostConstructInterceptors());
        preDestroy = Interceptors.getChainedInterceptorFactory(componentConfiguration.getPreDestroyInterceptors());
        final IdentityHashMap<Method, InterceptorFactory> componentInterceptors = new IdentityHashMap<Method, InterceptorFactory>();
        for (Method method : componentConfiguration.getDefinedComponentMethods()) {
            if(requiresInterceptors(method, componentConfiguration)) {
                componentInterceptors.put(method, Interceptors.getChainedInterceptorFactory(componentConfiguration.getComponentInterceptors(method)));
            }
        }
        componentClass = componentConfiguration.getComponentClass();
        this.componentInterceptors = componentInterceptors;
        this.namespaceContextSelector = componentConfiguration.getNamespaceContextSelector();
    }

    protected boolean requiresInterceptors(final Method method, final ComponentConfiguration componentConfiguration) {
        return Modifier.isPublic(method.getModifiers()) && componentConfiguration.getComponentDescription().isIntercepted();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void start(final StartContext context) throws StartException {
        component = createComponent();
    }

    /**
     * Create the component.
     *
     * @return the component instance
     */
    protected BasicComponent createComponent() {
        return new BasicComponent(this);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void stop(final StopContext context) {
        component = null;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Component getValue() throws IllegalStateException, IllegalArgumentException {
        Component component = this.component;
        if (component == null) {
            throw EeLogger.ROOT_LOGGER.serviceNotStarted();
        }
        return component;
    }

    /**
     * Get the deployment unit injector.
     *
     * @return the deployment unit injector
     */
    public InjectedValue<DeploymentUnit> getDeploymentUnitInjector() {
        return deploymentUnit;
    }

    /**
     * Get the component name.
     *
     * @return the component name
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * Get the post-construct interceptor factory.
     *
     * @return the post-construct interceptor factory
     */
    public InterceptorFactory getPostConstruct() {
        return postConstruct;
    }

    /**
     * Get the pre-destroy interceptor factory.
     *
     * @return the pre-destroy interceptor factory
     */
    public InterceptorFactory getPreDestroy() {
        return preDestroy;
    }

    /**
     * Get the component interceptor factory map.
     *
     * @return the component interceptor factories
     */
    public Map<Method, InterceptorFactory> getComponentInterceptors() {
        return componentInterceptors;
    }

    /**
     * Get the component class.
     *
     * @return the component class
     */
    public Class<?> getComponentClass() {
        return componentClass;
    }

    /**
     *
     * @return the namespace context selector for the component, or null if it does not have one
     */
    public NamespaceContextSelector getNamespaceContextSelector() {
        return namespaceContextSelector;
    }

    public ServiceName getServiceName() {
        return this.serviceName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1880.java