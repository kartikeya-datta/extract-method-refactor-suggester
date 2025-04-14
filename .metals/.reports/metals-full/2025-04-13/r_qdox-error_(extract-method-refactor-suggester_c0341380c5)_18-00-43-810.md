error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9420.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9420.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9420.java
text:
```scala
r@@eturn new SingletonComponentInstance(this);

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

package org.jboss.as.ejb3.component.singleton;

import org.jboss.as.ee.component.BasicComponentInstance;
import org.jboss.as.ee.component.Component;
import org.jboss.as.ee.component.ComponentInstance;
import org.jboss.as.ejb3.component.EJBBusinessMethod;
import org.jboss.as.ejb3.component.EJBComponentCreateService;
import org.jboss.as.ejb3.component.session.SessionBeanComponent;
import org.jboss.ejb3.concurrency.spi.LockableComponent;
import org.jboss.invocation.InterceptorContext;
import org.jboss.logging.Logger;
import org.jboss.msc.service.StopContext;

import javax.ejb.AccessTimeout;
import javax.ejb.LockType;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * {@link Component} representing a {@link javax.ejb.Singleton} EJB.
 *
 * @author Jaikiran Pai
 */
public class SingletonComponent extends SessionBeanComponent implements LockableComponent {

    private static final Logger logger = Logger.getLogger(SingletonComponent.class);

    private SingletonComponentInstance singletonComponentInstance;

    private boolean initOnStartup;

    private LockType beanLevelLockType;

    private Map<EJBBusinessMethod, LockType> methodLockTypes;

    private Map<EJBBusinessMethod, AccessTimeout> methodAccessTimeouts;

    /**
     * Construct a new instance.
     *
     * @param ejbComponentCreateService the component configuration
     */
    public SingletonComponent(final EJBComponentCreateService ejbComponentCreateService) {
        super(ejbComponentCreateService);
        this.initOnStartup = false; //ejbComponentCreateService.isInitOnStartup();

        this.beanLevelLockType = null; //ejbComponentCreateService.getBeanLevelLockType();
        this.methodLockTypes = null; //ejbComponentCreateService.getMethodApplicableLockTypes();
        this.methodAccessTimeouts = null; //ejbComponentCreateService.getMethodApplicableAccessTimeouts();
    }

    @Override
    public synchronized ComponentInstance createInstance() {
        if (this.singletonComponentInstance != null) {
            throw new IllegalStateException("A singleton component instance has already been created for bean: " + this.getComponentName());
        }
        return super.createInstance();
    }

    @Override
    protected BasicComponentInstance constructComponentInstance() {
        if (this.singletonComponentInstance != null) {
            throw new IllegalStateException("A singleton component instance has already been created for bean: " + this.getComponentName());
        }
        this.singletonComponentInstance = new SingletonComponentInstance(this);
        return this.singletonComponentInstance;
    }

//    @Override
//    public Interceptor createClientInterceptor(Class<?> view) {
//        return new Interceptor() {
//            @Override
//            public Object processInvocation(InterceptorContext context) throws Exception {
//                // TODO: FIXME: Component shouldn't be attached in a interceptor context that
//                // runs on remote clients.
//                context.putPrivateData(Component.class, SingletonComponent.this);
//
//                final Method method = context.getMethod();
//                if(isAsynchronous(method)) {
//                    return invokeAsynchronous(method, context);
//                }
//                return context.proceed();
//            }
//        };
//    }
//
//    @Override
//    public Interceptor createClientInterceptor(Class<?> view, Serializable sessionId) {
//        return createClientInterceptor(view);
//    }

    synchronized ComponentInstance getComponentInstance() {
        if (this.singletonComponentInstance == null) {
            this.singletonComponentInstance = (SingletonComponentInstance) this.createInstance();
        }
        return this.singletonComponentInstance;
    }

    @Override
    public void start() {
        super.start();
        if (this.initOnStartup) {
            // Do not call createInstance() because we can't ever assume that the singleton instance
            // hasn't already been created.
            logger.debug(this.getComponentName() + " bean is a @Startup (a.k.a init-on-startup) bean, creating/getting the singleton instance");
            this.getComponentInstance();
        }
    }

    @Override
    public void stop(final StopContext stopContext) {
        this.destroySingletonInstance();
        super.stop(stopContext);
    }

    @Override
    public LockType getLockType(Method method) {
        EJBBusinessMethod beanMethod = new EJBBusinessMethod(method.getName(), method.getParameterTypes());
        LockType lockType = this.methodLockTypes.get(beanMethod);
        if (lockType != null) {
            return lockType;
        }
        // check bean level lock type
        if (this.beanLevelLockType != null) {
            return this.beanLevelLockType;
        }
        // default WRITE lock type
        return LockType.WRITE;
    }

    @Override
    public AccessTimeout getAccessTimeout(Method method) {
        EJBBusinessMethod beanMethod = new EJBBusinessMethod(method.getName(), method.getParameterTypes());
        AccessTimeout accessTimeout = this.methodAccessTimeouts.get(beanMethod);
        if (accessTimeout != null) {
            return accessTimeout;
        }
        // check bean level access timeout
        if (this.beanLevelAccessTimeout != null) {
            return this.beanLevelAccessTimeout;
        }
        return null;
    }

    @Override
    public AccessTimeout getDefaultAccessTimeout() {
        // TODO: This has to be configurable.
        // Currently defaults to 5 minutes
        return new AccessTimeout() {
            @Override
            public long value() {
                return 5;
            }

            @Override
            public TimeUnit unit() {
                return TimeUnit.MINUTES;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return AccessTimeout.class;
            }
        };
    }

    private synchronized void destroySingletonInstance() {
        if (this.singletonComponentInstance != null) {
            // TODO: Implement destroying an instance
            logger.warn("Destorying of singleton instance not yet implemented");
            //this.destroyInstance(this.singletonComponentInstance);
            this.singletonComponentInstance = null;
        }
    }

    @Override
    public Object invoke(Serializable sessionId, Map<String, Object> contextData, Class<?> invokedBusinessInterface, Method beanMethod, Object[] args) throws Exception {
        if (sessionId != null)
            throw new IllegalArgumentException("Singleton " + this + " does not support sessions");
        if (invokedBusinessInterface != null)
            throw new UnsupportedOperationException("invokedBusinessInterface != null");
        InterceptorContext context = new InterceptorContext();
        context.putPrivateData(Component.class, this);
        context.setContextData(contextData);
        context.setMethod(beanMethod);
        context.setParameters(args);
        throw new RuntimeException("invoke() not yet implemented");
        //return getComponentInterceptor().processInvocation(context);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9420.java