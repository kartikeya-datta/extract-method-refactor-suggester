error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10320.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10320.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10320.java
text:
```scala
t@@hrow new RuntimeException(e);

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
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
package org.jboss.as.ejb3.component.entity;

import java.lang.reflect.Method;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;

import org.jboss.as.ee.component.BasicComponent;
import org.jboss.as.ee.component.BasicComponentCreateService;
import org.jboss.as.ee.component.ComponentConfiguration;
import org.jboss.as.ee.component.ComponentCreateServiceFactory;
import org.jboss.as.ee.component.TCCLInterceptor;
import org.jboss.as.ejb3.EjbMessages;
import org.jboss.as.ejb3.component.EJBComponentCreateService;
import org.jboss.as.ejb3.component.EJBComponentCreateServiceFactory;
import org.jboss.as.ejb3.component.InvokeMethodOnTargetInterceptor;
import org.jboss.as.ejb3.component.interceptors.CurrentInvocationContextInterceptor;
import org.jboss.as.ejb3.component.pool.PoolConfig;
import org.jboss.as.ejb3.deployment.ApplicationExceptions;
import org.jboss.invocation.ImmediateInterceptorFactory;
import org.jboss.invocation.InterceptorFactory;
import org.jboss.invocation.Interceptors;
import org.jboss.metadata.ejb.spec.EntityBeanMetaData;
import org.jboss.msc.value.InjectedValue;

/**
 * @author Stuart Douglas
 */
public class EntityBeanComponentCreateService extends EJBComponentCreateService {

    private final Class<EJBHome> homeClass;
    private final Class<EJBLocalHome> localHomeClass;
    private final Class<EJBObject> remoteClass;
    private final Class<EJBLocalObject> localClass;
    private final Class<Object> primaryKeyClass;
    private final Method ejbStoreMethod;
    private final Method ejbLoadMethod;
    private final Method ejbActivateMethod;
    private final Method ejbPassivateMethod;
    private final Method unsetEntityContextMethod;
    private final InterceptorFactory ejbStore;
    private final InterceptorFactory ejbLoad;
    private final InterceptorFactory ejbActivate;
    private final InterceptorFactory ejbPassivate;
    private final InterceptorFactory unsetEntityContext;
    private final InjectedValue<PoolConfig> poolConfig = new InjectedValue<PoolConfig>();
    private final InjectedValue<Boolean> defaultOptimisticLocking = new InjectedValue<Boolean>();

    public EntityBeanComponentCreateService(final ComponentConfiguration componentConfiguration, final ApplicationExceptions ejbJarConfiguration) {
        super(componentConfiguration, ejbJarConfiguration);
        final EntityBeanComponentDescription description = EntityBeanComponentDescription.class.cast(componentConfiguration.getComponentDescription());
        final EntityBeanMetaData beanMetaData = EntityBeanMetaData.class.cast(description.getDescriptorData());
        final ClassLoader classLoader = componentConfiguration.getComponentClass().getClassLoader();


        homeClass = (Class<EJBHome>) load(classLoader, beanMetaData.getHome());
        localHomeClass = (Class<EJBLocalHome>) load(classLoader, beanMetaData.getLocalHome());
        localClass = (Class<EJBLocalObject>) load(classLoader, beanMetaData.getLocal());
        remoteClass = (Class<EJBObject>) load(classLoader, beanMetaData.getRemote());
        primaryKeyClass = (Class<Object>) load(classLoader, beanMetaData.getPrimKeyClass());

        final InterceptorFactory tcclInterceptorFactory = new ImmediateInterceptorFactory(new TCCLInterceptor(componentConfiguration.getModuleClassLoader()));
        final InterceptorFactory namespaceContextInterceptorFactory = componentConfiguration.getNamespaceContextInterceptorFactory();


        Method ejbStore = null;
        Method ejbLoad = null;
        Method ejbActivate = null;
        Method ejbPassivate = null;
        Method unsetEntityContext = null;
        for (final Method method : componentConfiguration.getDefinedComponentMethods()) {
            if (method.getName().equals("ejbStore") && method.getParameterTypes().length == 0) {
                ejbStore = method;
            } else if (method.getName().equals("ejbLoad") && method.getParameterTypes().length == 0) {
                ejbLoad = method;
            } else if (method.getName().equals("ejbActivate") && method.getParameterTypes().length == 0) {
                ejbActivate = method;
            } else if (method.getName().equals("ejbPassivate") && method.getParameterTypes().length == 0) {
                ejbPassivate = method;
            } else if (method.getName().equals("unsetEntityContext") && method.getParameterTypes().length == 0) {
                unsetEntityContext = method;
            }
        }
        if (ejbStore == null) {
            throw EjbMessages.MESSAGES.couldNotFindEntityBeanMethod("ejbStore");
        } else if (ejbLoad == null) {
            throw EjbMessages.MESSAGES.couldNotFindEntityBeanMethod("ejbLoad");
        } else if (ejbActivate == null) {
            throw EjbMessages.MESSAGES.couldNotFindEntityBeanMethod("ejbActivate");
        } else if (ejbPassivate == null) {
            throw EjbMessages.MESSAGES.couldNotFindEntityBeanMethod("ejbPassivate");
        } else if (unsetEntityContext == null) {
            throw EjbMessages.MESSAGES.couldNotFindEntityBeanMethod("unsetEntityContext");
        }

        this.ejbActivateMethod = ejbActivate;
        this.ejbLoadMethod = ejbLoad;
        this.ejbStoreMethod = ejbStore;
        this.ejbPassivateMethod = ejbPassivate;
        this.unsetEntityContextMethod = unsetEntityContext;
        this.ejbActivate = Interceptors.getChainedInterceptorFactory(tcclInterceptorFactory, namespaceContextInterceptorFactory, CurrentInvocationContextInterceptor.FACTORY, invokeMethodOnTarget(ejbActivate));
        this.ejbLoad = Interceptors.getChainedInterceptorFactory(tcclInterceptorFactory, namespaceContextInterceptorFactory, CurrentInvocationContextInterceptor.FACTORY, invokeMethodOnTarget(ejbLoad));
        this.ejbStore = Interceptors.getChainedInterceptorFactory(tcclInterceptorFactory, namespaceContextInterceptorFactory, CurrentInvocationContextInterceptor.FACTORY, invokeMethodOnTarget(ejbStore));
        this.ejbPassivate = Interceptors.getChainedInterceptorFactory(tcclInterceptorFactory, namespaceContextInterceptorFactory, CurrentInvocationContextInterceptor.FACTORY, invokeMethodOnTarget(ejbPassivate));
        this.unsetEntityContext = Interceptors.getChainedInterceptorFactory(tcclInterceptorFactory, namespaceContextInterceptorFactory, CurrentInvocationContextInterceptor.FACTORY, invokeMethodOnTarget(unsetEntityContext));
    }

    private Class<?> load(ClassLoader classLoader, String ejbClass) {
        if (ejbClass != null) {
            try {
                return classLoader.loadClass(ejbClass);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Failed to load component view class: " + ejbClass, e);
            }
        }
        return null;
    }


    private static InterceptorFactory invokeMethodOnTarget(final Method method) {
        method.setAccessible(true);
        return InvokeMethodOnTargetInterceptor.factory(method);
    }

    @Override
    protected BasicComponent createComponent() {
        return new EntityBeanComponent(this);
    }

    public static final ComponentCreateServiceFactory FACTORY = new EJBComponentCreateServiceFactory() {
        @Override
        public BasicComponentCreateService constructService(final ComponentConfiguration configuration) {
            return new EntityBeanComponentCreateService(configuration, this.ejbJarConfiguration);
        }
    };

    public Class<EJBHome> getHomeClass() {
        return homeClass;
    }

    public Class<EJBLocalHome> getLocalHomeClass() {
        return localHomeClass;
    }

    public Class<EJBObject> getRemoteClass() {
        return remoteClass;
    }

    public Class<EJBLocalObject> getLocalClass() {
        return localClass;
    }

    public Class<Object> getPrimaryKeyClass() {
        return primaryKeyClass;
    }

    public Method getEjbStoreMethod() {
        return ejbStoreMethod;
    }

    public Method getEjbLoadMethod() {
        return ejbLoadMethod;
    }

    public Method getEjbActivateMethod() {
        return ejbActivateMethod;
    }

    public InterceptorFactory getEjbStore() {
        return ejbStore;
    }

    public InterceptorFactory getEjbLoad() {
        return ejbLoad;
    }

    public InterceptorFactory getEjbActivate() {
        return ejbActivate;
    }

    public Method getEjbPassivateMethod() {
        return ejbPassivateMethod;
    }

    public InterceptorFactory getEjbPassivate() {
        return ejbPassivate;
    }

    public Method getUnsetEntityContextMethod() {
        return unsetEntityContextMethod;
    }

    public InterceptorFactory getUnsetEntityContext() {
        return unsetEntityContext;
    }

    public PoolConfig getPoolConfig() {
        return this.poolConfig.getOptionalValue();
    }

    public InjectedValue<PoolConfig> getPoolConfigInjector() {
        return this.poolConfig;
    }

    public Boolean getOptimisticLocking() {
        return defaultOptimisticLocking.getOptionalValue();
    }

    public InjectedValue<Boolean> getOptimisticLockingInjector() {
        return defaultOptimisticLocking;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10320.java