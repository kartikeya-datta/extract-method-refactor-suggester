error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5992.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5992.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5992.java
text:
```scala
protected B@@asicComponentInstance instantiateComponentInstance(final AtomicReference<ManagedReference> instanceReference, final Interceptor preDestroyInterceptor, final Map<Method, Interceptor> methodInterceptors, final InterceptorFactoryContext interceptorContext) {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2011, Red Hat, Inc., and individual contributors
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
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.ejb.AccessTimeout;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.TimerService;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.jboss.as.ee.component.BasicComponentInstance;
import org.jboss.as.ee.component.Component;
import org.jboss.as.ee.component.ComponentInstance;
import org.jboss.as.ee.component.ComponentView;
import org.jboss.as.ejb3.cache.Cache;
import org.jboss.as.ejb3.cache.ExpiringCache;
import org.jboss.as.ejb3.cache.StatefulObjectFactory;
import org.jboss.as.ejb3.component.DefaultAccessTimeoutService;
import org.jboss.as.ejb3.component.EJBBusinessMethod;
import org.jboss.as.ejb3.component.session.SessionBeanComponent;
import org.jboss.as.ejb3.concurrency.AccessTimeoutDetails;
import org.jboss.as.naming.ManagedReference;
import org.jboss.as.server.CurrentServiceContainer;
import org.jboss.ejb.client.EJBClient;
import org.jboss.ejb.client.SessionID;
import org.jboss.ejb.client.StatefulEJBLocator;
import org.jboss.invocation.Interceptor;
import org.jboss.invocation.InterceptorContext;
import org.jboss.invocation.InterceptorFactory;
import org.jboss.invocation.InterceptorFactoryContext;
import org.jboss.invocation.SimpleInterceptorFactoryContext;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.StopContext;
import org.jboss.tm.TxUtils;


/**
 * Stateful Session Bean
 *
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class StatefulSessionComponent extends SessionBeanComponent {

    public static final Object SESSION_ID_REFERENCE_KEY = new Object();

    private static final Logger logger = Logger.getLogger(StatefulSessionComponent.class);

    private final Cache<StatefulSessionComponentInstance> cache;

    private final InterceptorFactory afterBegin;
    private final Method afterBeginMethod;
    private final InterceptorFactory afterCompletion;
    private final Method afterCompletionMethod;
    private final InterceptorFactory beforeCompletion;
    private final Method beforeCompletionMethod;
    private final Map<EJBBusinessMethod, AccessTimeoutDetails> methodAccessTimeouts;
    private final DefaultAccessTimeoutService defaultAccessTimeoutProvider;

    /**
     * Construct a new instance.
     *
     * @param ejbComponentCreateService the component configuration
     */
    protected StatefulSessionComponent(final StatefulSessionComponentCreateService ejbComponentCreateService) {
        super(ejbComponentCreateService);

        this.afterBegin = ejbComponentCreateService.getAfterBegin();
        this.afterBeginMethod = ejbComponentCreateService.getAfterBeginMethod();
        this.afterCompletion = ejbComponentCreateService.getAfterCompletion();
        this.afterCompletionMethod = ejbComponentCreateService.getAfterCompletionMethod();
        this.beforeCompletion = ejbComponentCreateService.getBeforeCompletion();
        this.beforeCompletionMethod = ejbComponentCreateService.getBeforeCompletionMethod();
        this.methodAccessTimeouts = ejbComponentCreateService.getMethodApplicableAccessTimeouts();
        this.defaultAccessTimeoutProvider = ejbComponentCreateService.getDefaultAccessTimeoutService();

        final StatefulTimeoutInfo statefulTimeout = ejbComponentCreateService.getStatefulTimeout();
        if (statefulTimeout != null) {
            cache = new ExpiringCache<StatefulSessionComponentInstance>(statefulTimeout.getValue(), statefulTimeout.getTimeUnit(), ejbComponentCreateService.getComponentClass().getName());
        } else {
            cache = new ExpiringCache<StatefulSessionComponentInstance>(-1, TimeUnit.MILLISECONDS, ejbComponentCreateService.getComponentClass().getName());
        }
        cache.setStatefulObjectFactory(new StatefulObjectFactory<StatefulSessionComponentInstance>() {
            @Override
            public StatefulSessionComponentInstance createInstance() {
                return (StatefulSessionComponentInstance) StatefulSessionComponent.this.createInstance();
            }

            @Override
            public void destroyInstance(StatefulSessionComponentInstance instance) {
                instance.destroy();
            }
        });
    }

    protected SessionID getSessionIdOf(final InterceptorContext ctx) {
        final StatefulSessionComponentInstance instance = (StatefulSessionComponentInstance) ctx.getPrivateData(ComponentInstance.class);
        return instance.getId();
    }

    public <T> T getBusinessObject(Class<T> businessInterface, final InterceptorContext context) throws IllegalStateException {
        if (businessInterface == null) {
            throw new IllegalStateException("Business interface type cannot be null");
        }
        return createViewInstanceProxy(businessInterface, Collections.<Object, Object>singletonMap(SessionID.SESSION_ID_KEY, getSessionIdOf(context)));
    }

    public EJBLocalObject getEJBLocalObject(final InterceptorContext ctx) throws IllegalStateException {
        if (getEjbLocalObjectView() == null) {
            throw new IllegalStateException("Bean " + getComponentName() + " does not have an EJBLocalObject");
        }
        return createViewInstanceProxy(EJBLocalObject.class, Collections.<Object, Object>singletonMap(SessionID.SESSION_ID_KEY, getSessionIdOf(ctx)), getEjbLocalObjectView());
    }

    public EJBObject getEJBObject(final InterceptorContext ctx) throws IllegalStateException {
        if (getEjbObjectView() == null) {
            throw new IllegalStateException("Bean " + getComponentName() + " does not have an EJBObject");
        }
        final ServiceController<?> serviceController = CurrentServiceContainer.getServiceContainer().getRequiredService(getEjbObjectView());
        final ComponentView view = (ComponentView) serviceController.getValue();
        return EJBClient.createProxy(new StatefulEJBLocator<EJBObject>((Class<EJBObject>) view.getViewClass(), getApplicationName(), getModuleName(), getComponentName(), getDistinctName(), getSessionIdOf(ctx)));
    }

    @Override
    public TimerService getTimerService() throws IllegalStateException {
        throw new IllegalStateException("TimerService is not supported for Stateful session bean " + this.getComponentName());
    }

    /**
     * Returns the {@link AccessTimeout} applicable to given method
     */
    public AccessTimeoutDetails getAccessTimeout(Method method) {
        final EJBBusinessMethod ejbMethod = new EJBBusinessMethod(method);
        final AccessTimeoutDetails accessTimeout = this.methodAccessTimeouts.get(ejbMethod);
        if (accessTimeout != null) {
            return accessTimeout;
        }
        // check bean level access timeout
        final AccessTimeoutDetails timeout = this.beanLevelAccessTimeout.get(method.getDeclaringClass().getName());
        if (timeout != null) {
            return timeout;
        }

        return defaultAccessTimeoutProvider.getDefaultAccessTimeout();
    }

    protected Interceptor createInterceptor(final InterceptorFactory factory) {
        if (factory == null)
            return null;
        final SimpleInterceptorFactoryContext context = new SimpleInterceptorFactoryContext();
        context.getContextData().put(Component.class, this);
        return factory.create(context);
    }

    public SessionID createSession() {
        return getCache().create().getId();
    }

    public Cache<StatefulSessionComponentInstance> getCache() {
        return cache;
    }

    @Override
    protected BasicComponentInstance instantiateComponentInstance(AtomicReference<ManagedReference> instanceReference, Interceptor preDestroyInterceptor, Map<Method, Interceptor> methodInterceptors, final InterceptorFactoryContext interceptorContext) {
        return new StatefulSessionComponentInstance(this, instanceReference, preDestroyInterceptor, methodInterceptors);
    }

    /**
     * Removes the session associated with the <code>sessionId</code>.
     *
     * @param sessionId The session id
     */
    public void removeSession(final SessionID sessionId) {
        Transaction currentTx = null;
        try {
            currentTx = getTransactionManager().getTransaction();
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }

        if (currentTx != null && TxUtils.isActive(currentTx)) {
            try {
                // A transaction is in progress, so register a Synchronization so that the session can be removed on tx
                // completion.
                currentTx.registerSynchronization(new RemoveSynchronization(this, sessionId));
            } catch (RollbackException e) {
                throw new RuntimeException(e);
            } catch (SystemException e) {
                throw new RuntimeException(e);
            }
        } else {
            // no tx currently in progress, so just remove the session
            getCache().remove(sessionId);
        }

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

    public Method getAfterBeginMethod() {
        return afterBeginMethod;
    }

    public Method getAfterCompletionMethod() {
        return afterCompletionMethod;
    }

    public Method getBeforeCompletionMethod() {
        return beforeCompletionMethod;
    }

    /**
     * A {@link javax.transaction.Synchronization} which removes a stateful session in it's {@link javax.transaction.Synchronization#afterCompletion(int)}
     * callback.
     */
    private static class RemoveSynchronization implements Synchronization {
        private final StatefulSessionComponent statefulComponent;
        private final SessionID sessionId;

        public RemoveSynchronization(final StatefulSessionComponent component, final SessionID sessionId) {
            if (sessionId == null) {
                throw new IllegalArgumentException("Session id cannot be null");
            }
            if (component == null) {
                throw new IllegalArgumentException("Stateful component cannot be null");
            }
            this.sessionId = sessionId;
            this.statefulComponent = component;

        }

        public void beforeCompletion() {
        }

        public void afterCompletion(int status) {
            try {
                // remove the session
                this.statefulComponent.getCache().remove(this.sessionId);
            } catch (Throwable t) {
                // An exception thrown from afterCompletion is gobbled up
                logger.error("Failed to remove bean: " + this.statefulComponent.getComponentName() + " with session id " + this.sessionId, t);
                if (t instanceof Error)
                    throw (Error) t;
                if (t instanceof RuntimeException)
                    throw (RuntimeException) t;
                throw new RuntimeException(t);
            }
        }

    }

    @Override
    public void start() {
        super.start();
        cache.start();
    }

    @Override
    public void stop(final StopContext stopContext) {
        super.stop(stopContext);
        cache.stop();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5992.java