error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14523.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14523.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14523.java
text:
```scala
t@@his.ejb2XRemoveMethod = Interceptors.getChainedInterceptorFactory(StatefulSessionSynchronizationInterceptor.factory(componentDescription.getTransactionManagementType()), new ImmediateInterceptorFactory(new StatefulRemoveInterceptor(false)), Interceptors.getTerminalInterceptorFactory());

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

package org.jboss.as.ejb3.component.stateful;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jboss.as.ee.component.BasicComponent;
import org.jboss.as.ee.component.ComponentConfiguration;
import org.jboss.as.ee.component.TCCLInterceptor;
import org.jboss.as.ejb3.cache.CacheFactory;
import org.jboss.as.ejb3.cache.CacheInfo;
import org.jboss.as.ejb3.component.DefaultAccessTimeoutService;
import org.jboss.as.ejb3.component.InvokeMethodOnTargetInterceptor;
import org.jboss.as.ejb3.component.interceptors.CurrentInvocationContextInterceptor;
import org.jboss.as.ejb3.component.session.SessionBeanComponentCreateService;
import org.jboss.as.ejb3.deployment.ApplicationExceptions;
import org.jboss.ejb.client.SessionID;
import org.jboss.invocation.ImmediateInterceptorFactory;
import org.jboss.invocation.InterceptorFactory;
import org.jboss.invocation.Interceptors;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.SimpleClassResolver;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.value.InjectedValue;

/**
 * @author Stuart Douglas
 */
public class StatefulSessionComponentCreateService extends SessionBeanComponentCreateService {
    private final InterceptorFactory afterBegin;
    private final Method afterBeginMethod;
    private final InterceptorFactory afterCompletion;
    private final Method afterCompletionMethod;
    private final InterceptorFactory beforeCompletion;
    private final Method beforeCompletionMethod;
    private final Collection<InterceptorFactory> prePassivate;
    private final Collection<InterceptorFactory> postActivate;
    private final StatefulTimeoutInfo statefulTimeout;
    private final CacheInfo cache;
    private final MarshallingConfiguration marshallingConfiguration;
    private final InjectedValue<DefaultAccessTimeoutService> defaultAccessTimeoutService = new InjectedValue<DefaultAccessTimeoutService>();
    private final InterceptorFactory ejb2XRemoveMethod;
    @SuppressWarnings("rawtypes")
    private final InjectedValue<CacheFactory> cacheFactory = new InjectedValue<CacheFactory>();

    /**
     * Construct a new instance.
     *
     * @param componentConfiguration the component configuration
     */
    public StatefulSessionComponentCreateService(final ComponentConfiguration componentConfiguration, final ApplicationExceptions ejbJarConfiguration) {
        super(componentConfiguration, ejbJarConfiguration);

        final StatefulComponentDescription componentDescription = (StatefulComponentDescription) componentConfiguration.getComponentDescription();
        final InterceptorFactory tcclInterceptorFactory = new ImmediateInterceptorFactory(new TCCLInterceptor(componentConfiguration.getModuleClassLoder()));
        final InterceptorFactory namespaceContextInterceptorFactory = componentConfiguration.getNamespaceContextInterceptorFactory();

        this.afterBeginMethod = componentDescription.getAfterBegin();
        this.afterBegin = (this.afterBeginMethod != null) ? Interceptors.getChainedInterceptorFactory(tcclInterceptorFactory, namespaceContextInterceptorFactory, CurrentInvocationContextInterceptor.FACTORY, invokeMethodOnTarget(this.afterBeginMethod)) : null;
        this.afterCompletionMethod = componentDescription.getAfterCompletion();
        this.afterCompletion = (this.afterCompletionMethod != null) ? Interceptors.getChainedInterceptorFactory(tcclInterceptorFactory, namespaceContextInterceptorFactory, CurrentInvocationContextInterceptor.FACTORY, invokeMethodOnTarget(this.afterCompletionMethod)) : null;
        this.beforeCompletionMethod = componentDescription.getBeforeCompletion();
        this.beforeCompletion = (this.beforeCompletionMethod != null) ? Interceptors.getChainedInterceptorFactory(tcclInterceptorFactory, namespaceContextInterceptorFactory, CurrentInvocationContextInterceptor.FACTORY, invokeMethodOnTarget(this.beforeCompletionMethod)) : null;
        this.prePassivate = createInterceptorFactories(componentDescription.getPrePassivateMethods(), tcclInterceptorFactory, namespaceContextInterceptorFactory, CurrentInvocationContextInterceptor.FACTORY);
        this.postActivate = createInterceptorFactories(componentDescription.getPostActivateMethods(), tcclInterceptorFactory, namespaceContextInterceptorFactory, CurrentInvocationContextInterceptor.FACTORY);
        this.statefulTimeout = componentDescription.getStatefulTimeout();
        //the interceptor chain for EJB e.x remove methods
        this.ejb2XRemoveMethod = Interceptors.getChainedInterceptorFactory(StatefulSessionSynchronizationInterceptor.FACTORY, new ImmediateInterceptorFactory(new StatefulRemoveInterceptor(false)), Interceptors.getTerminalInterceptorFactory());
        this.cache = componentDescription.getCache();
        this.marshallingConfiguration = new MarshallingConfiguration();
        this.marshallingConfiguration.setClassResolver(new SimpleClassResolver(componentConfiguration.getModuleClassLoder()));
    }

    private static Collection<InterceptorFactory> createInterceptorFactories(Collection<Method> methods, InterceptorFactory... factories) {
        if (methods.isEmpty()) return Collections.emptyList();
        Collection<InterceptorFactory> collection = new ArrayList<InterceptorFactory>(methods.size());
        for (Method method: methods) {
            List<InterceptorFactory> list = new ArrayList<InterceptorFactory>(factories.length + 1);
            list.addAll(Arrays.asList(factories));
            list.add(invokeMethodOnTarget(method));
            collection.add(Interceptors.getChainedInterceptorFactory(list));
        }
        return collection;
    }

    private static InterceptorFactory invokeMethodOnTarget(final Method method) {
        method.setAccessible(true);
        return InvokeMethodOnTargetInterceptor.factory(method);
    }


    @Override
    protected BasicComponent createComponent() {
        return new StatefulSessionComponent(this);
    }

    public InterceptorFactory getAfterBegin() {
        return afterBegin;
    }

    public InterceptorFactory getAfterCompletion() {
        return afterCompletion;
    }

    public InterceptorFactory getBeforeCompletion() {
        return beforeCompletion;
    }

    public Collection<InterceptorFactory> getPrePassivate() {
        return this.prePassivate;
    }

    public Collection<InterceptorFactory> getPostActivate() {
        return this.postActivate;
    }

    public Method getAfterBeginMethod() {
        return afterBeginMethod;
    }

    public Method getAfterCompletionMethod() {
        return afterCompletionMethod;
    }

    public Method getBeforeCompletionMethod() {
        return beforeCompletionMethod;
    }

    public StatefulTimeoutInfo getStatefulTimeout() {
        return statefulTimeout;
    }

    public CacheInfo getCache() {
        return this.cache;
    }

    public MarshallingConfiguration getMarshallingConfiguration() {
        return this.marshallingConfiguration;
    }

    public DefaultAccessTimeoutService getDefaultAccessTimeoutService() {
        return defaultAccessTimeoutService.getValue();
    }

    Injector<DefaultAccessTimeoutService> getDefaultAccessTimeoutInjector() {
        return this.defaultAccessTimeoutService;
    }

    public InterceptorFactory getEjb2XRemoveMethod() {
        return ejb2XRemoveMethod;
    }

    @SuppressWarnings("unchecked")
    public CacheFactory<SessionID, StatefulSessionComponentInstance> getCacheFactory() {
        return (CacheFactory<SessionID, StatefulSessionComponentInstance>) this.cacheFactory.getValue();
    }

    @SuppressWarnings("rawtypes")
    Injector<CacheFactory> getCacheFactoryInjector() {
        return this.cacheFactory;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14523.java