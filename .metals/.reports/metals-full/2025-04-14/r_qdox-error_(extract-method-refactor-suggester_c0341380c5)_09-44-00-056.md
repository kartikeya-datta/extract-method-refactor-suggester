error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12933.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12933.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12933.java
text:
```scala
protected L@@ist<InterceptorFactory> getComponentSystemInterceptorFactories() {

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

import org.jboss.as.naming.context.NamespaceContextSelector;
import org.jboss.invocation.Interceptor;
import org.jboss.invocation.InterceptorFactory;
import org.jboss.invocation.proxy.ProxyFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * The construction parameter set passed in to an abstract component.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public abstract class AbstractComponentConfiguration {

    private final AbstractComponentDescription description;
    private final List<ComponentLifecycle> postConstructLifecycles = new ArrayList<ComponentLifecycle>();
    private final List<ComponentLifecycle> preDestroyLifecycles = new ArrayList<ComponentLifecycle>();
    private final List<LifecycleInterceptorFactory> postConstructInterceptorLifecycles = new ArrayList<LifecycleInterceptorFactory>();
    private final List<LifecycleInterceptorFactory> preDestroyInterceptorLifecycles = new ArrayList<LifecycleInterceptorFactory>();
    private final List<ResourceInjection> resourceInjections = new ArrayList<ResourceInjection>();
    private final Map<Class<?>,List<ResourceInjection>> interceptorResourceInjections = new HashMap<Class<?>,List<ResourceInjection>>();
    private final List<InterceptorFactory> componentSystemInterceptorFactories = new ArrayList<InterceptorFactory>();
    private final Map<Class<?>, ComponentInvocationHandler> views = new IdentityHashMap<Class<?>, ComponentInvocationHandler>();
    private final Map<Method, InterceptorFactory> interceptorFactoryMap = new IdentityHashMap<Method, InterceptorFactory>();
    private final Map<Class<?>, ProxyFactory<?>> proxyFactories = new IdentityHashMap<Class<?>, ProxyFactory<?>>();
    private final List<ComponentInjector> componentInjectors = new ArrayList<ComponentInjector>();
    private Class<?> componentClass;
    private Interceptor componentInterceptor;
    private NamespaceContextSelector namespaceContextSelector;

    /**
     * Construct a new instance.
     *
     * @param description the original component description
     */
    protected AbstractComponentConfiguration(final AbstractComponentDescription description) {
        this.description = description;
    }

    /**
     * Get the original component description.
     *
     * @return the component description
     */
    public AbstractComponentDescription getDescription() {
        return description;
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
     * Set the component class.
     *
     * @param componentClass the component class
     */
    public void setComponentClass(final Class<?> componentClass) {
        this.componentClass = componentClass;
    }

    public void addPostConstructLifecycle(ComponentLifecycle lifecycle) {
        postConstructLifecycles.add(lifecycle);
    }

    List<ComponentLifecycle> getPostConstructLifecycles() {
        return postConstructLifecycles;
    }

    public void addPostConstructInterceptorLifecycle(LifecycleInterceptorFactory lifecycle) {
        postConstructInterceptorLifecycles.add(lifecycle);
    }

    List<LifecycleInterceptorFactory> getPostConstructInterceptorLifecycles() {
        return postConstructInterceptorLifecycles;
    }

    public void addPreDestroyLifecycle(ComponentLifecycle lifecycle) {
        preDestroyLifecycles.add(lifecycle);
    }

    List<ComponentLifecycle> getPreDestroyLifecycles() {
        return preDestroyLifecycles;
    }

    public void addPreDestroyInterceptorLifecycle(LifecycleInterceptorFactory lifecycle) {
        preDestroyInterceptorLifecycles.add(lifecycle);
    }

    List<LifecycleInterceptorFactory> getPreDestroyInterceptorLifecycles() {
        return preDestroyInterceptorLifecycles;
    }

    public void addInterceptorResourceInjection(Class<?> interceptorClass, List<ResourceInjection> resourceInjections) {
        interceptorResourceInjections.put(interceptorClass, new ArrayList<ResourceInjection>(resourceInjections));
    }

    public List<ResourceInjection> getInterceptorResourceInjections(Class<?> interceptorClass) {
        return interceptorResourceInjections.get(interceptorClass);
    }

    List<ResourceInjection> getResourceInjections() {
        return resourceInjections;
    }

    List<InterceptorFactory> getComponentSystemInterceptorFactories() {
        return componentSystemInterceptorFactories;
    }

    Map<Class<?>, ComponentInvocationHandler> getViews() {
        return views;
    }

    Map<Method, InterceptorFactory> getInterceptorFactoryMap() {
        return interceptorFactoryMap;
    }

    Interceptor getComponentInterceptor() {
        return componentInterceptor;
    }

    void setComponentInterceptor(final Interceptor componentInterceptor) {
        this.componentInterceptor = componentInterceptor;
    }

    Map<Class<?>,ProxyFactory<?>> getProxyFactories() {
        return proxyFactories;
    }

    public void addComponentInjector(ComponentInjector injector) {
        this.componentInjectors.add(injector);
    }

    public List<ComponentInjector> getComponentInjectors() {
        return Collections.unmodifiableList(componentInjectors);
    }

    /**
     * Construct a component by passing this configuration in to the component constructor.
     *
     * @return the component instance
     */
    public abstract AbstractComponent constructComponent();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12933.java