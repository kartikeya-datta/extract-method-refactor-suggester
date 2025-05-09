error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14625.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14625.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14625.java
text:
```scala
r@@eturn isBeanManagedTransaction() ? StatefulAllowedMethodsInformation.INSTANCE_BMT : StatefulAllowedMethodsInformation.INSTANCE_CMT;

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
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.TimerService;

import org.jboss.as.ee.component.BasicComponentInstance;
import org.jboss.as.ee.component.Component;
import org.jboss.as.ee.component.ComponentInstance;
import org.jboss.as.ee.component.ComponentView;
import org.jboss.as.ejb3.cache.Cache;
import org.jboss.as.ejb3.cache.IdentifierFactory;
import org.jboss.as.ejb3.cache.PassivationManager;
import org.jboss.as.ejb3.cache.StatefulObjectFactory;
import org.jboss.as.ejb3.cache.TransactionAwareObjectFactory;
import org.jboss.as.ejb3.component.DefaultAccessTimeoutService;
import org.jboss.as.ejb3.component.EJBBusinessMethod;
import org.jboss.as.ejb3.component.allowedmethods.AllowedMethodsInformation;
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
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;

import static org.jboss.as.ejb3.EjbMessages.MESSAGES;

/**
 * Stateful Session Bean
 *
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class StatefulSessionComponent extends SessionBeanComponent implements StatefulObjectFactory<StatefulSessionComponentInstance>, PassivationManager<SessionID, StatefulSessionComponentInstance>, IdentifierFactory<SessionID> {

    public static final Object SESSION_ID_REFERENCE_KEY = new Object();

    private final Cache<SessionID, StatefulSessionComponentInstance> cache;

    private final InterceptorFactory afterBegin;
    private final Method afterBeginMethod;
    private final InterceptorFactory afterCompletion;
    private final Method afterCompletionMethod;
    private final InterceptorFactory beforeCompletion;
    private final Method beforeCompletionMethod;
    private final InterceptorFactory prePassivate;
    private final InterceptorFactory postActivate;
    private final Map<EJBBusinessMethod, AccessTimeoutDetails> methodAccessTimeouts;
    private final DefaultAccessTimeoutService defaultAccessTimeoutProvider;
    private final int currentMarshallingVersion;
    private final Map<Integer, MarshallingConfiguration> marshallingConfigurations;

    private final InterceptorFactory ejb2XRemoveMethod;

    /**
     * Set of context keys for serializable interceptors.
     * <p/>
     * These are used to serialize the user provided interceptors
     */
    private final Set<Object> serialiableInterceptorContextKeys;

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
        this.prePassivate = ejbComponentCreateService.getPrePassivate();
        this.postActivate = ejbComponentCreateService.getPostActivate();
        this.methodAccessTimeouts = ejbComponentCreateService.getMethodApplicableAccessTimeouts();
        this.defaultAccessTimeoutProvider = ejbComponentCreateService.getDefaultAccessTimeoutService();
        this.ejb2XRemoveMethod = ejbComponentCreateService.getEjb2XRemoveMethod();
        this.currentMarshallingVersion = ejbComponentCreateService.getCurrentMarshallingVersion();
        this.marshallingConfigurations = ejbComponentCreateService.getMarshallingConfigurations();
        this.serialiableInterceptorContextKeys = ejbComponentCreateService.getSerializableInterceptorContextKeys();

        String beanName = ejbComponentCreateService.getComponentClass().getName();
        StatefulObjectFactory<StatefulSessionComponentInstance> factory = new TransactionAwareObjectFactory<StatefulSessionComponentInstance>(this, this.getTransactionManager());
        StatefulTimeoutInfo timeout = ejbComponentCreateService.getStatefulTimeout();
        this.cache = ejbComponentCreateService.getCacheFactory().createCache(beanName, this, factory, this, timeout);
    }

    @Override
    public StatefulSessionComponentInstance createInstance() {
        return (StatefulSessionComponentInstance) super.createInstance();
    }

    @Override
    public StatefulSessionComponentInstance createInstance(final Object instance) {
        return (StatefulSessionComponentInstance) super.createInstance();
    }

    @Override
    protected StatefulSessionComponentInstance constructComponentInstance(ManagedReference instance, boolean invokePostConstruct, InterceptorFactoryContext context) {
        return (StatefulSessionComponentInstance) super.constructComponentInstance(instance, invokePostConstruct, context);
    }

    @Override
    public void destroyInstance(StatefulSessionComponentInstance instance) {
        instance.destroy();
    }

    @Override
    public void postActivate(StatefulSessionComponentInstance instance) {
        instance.postActivate();
    }

    @Override
    public void prePassivate(StatefulSessionComponentInstance instance) {
        instance.prePassivate();
    }

    @Override
    public int getCurrentMarshallingVersion() {
        return this.currentMarshallingVersion;
    }

    @Override
    public MarshallingConfiguration getMarshallingConfiguration(int version) {
        MarshallingConfiguration config = this.marshallingConfigurations.get(version);
        if (config == null) {
            throw MESSAGES.unsupportedMarshallingVersion(version);
        }
        return config;
    }

    protected SessionID getSessionIdOf(final InterceptorContext ctx) {
        final StatefulSessionComponentInstance instance = (StatefulSessionComponentInstance) ctx.getPrivateData(ComponentInstance.class);
        return instance.getId();
    }

    @Override
    public <T> T getBusinessObject(Class<T> businessInterface, final InterceptorContext context) throws IllegalStateException {
        if (businessInterface == null) {
            throw MESSAGES.businessInterfaceIsNull();
        }
        return createViewInstanceProxy(businessInterface, Collections.<Object, Object>singletonMap(SessionID.class, getSessionIdOf(context)));
    }

    @Override
    public EJBLocalObject getEJBLocalObject(final InterceptorContext ctx) throws IllegalStateException {
        if (getEjbLocalObjectViewServiceName() == null) {
            throw MESSAGES.ejbLocalObjectUnavailable(getComponentName());
        }
        return createViewInstanceProxy(EJBLocalObject.class, Collections.<Object, Object>singletonMap(SessionID.class, getSessionIdOf(ctx)), getEjbLocalObjectViewServiceName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public EJBObject getEJBObject(final InterceptorContext ctx) throws IllegalStateException {
        if (getEjbObjectViewServiceName() == null) {
            throw MESSAGES.beanComponentMissingEjbObject(getComponentName(), "EJBObject");
        }
        final ServiceController<?> serviceController = currentServiceContainer().getRequiredService(getEjbObjectViewServiceName());
        final ComponentView view = (ComponentView) serviceController.getValue();
        final String locatorAppName = getEarApplicationName() == null ? "" : getEarApplicationName();
        return EJBClient.createProxy(new StatefulEJBLocator<EJBObject>((Class<EJBObject>) view.getViewClass(), locatorAppName, getModuleName(), getComponentName(), getDistinctName(), getSessionIdOf(ctx), this.getCache().getStrictAffinity()));
    }

    @Override
    public TimerService getTimerService() throws IllegalStateException {
        throw MESSAGES.timerServiceNotSupportedForSFSB(this.getComponentName());
    }

    /**
     * Returns the {@link javax.ejb.AccessTimeout} applicable to given method
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

    protected Interceptor createInterceptor(final InterceptorFactory factory, final InterceptorFactoryContext context) {
        if (factory == null)
            return null;
        context.getContextData().put(Component.class, this);
        return factory.create(context);
    }

    public SessionID createSession() {
        return this.cache.create().getId();
    }

    public Cache<SessionID, StatefulSessionComponentInstance> getCache() {
        return this.cache;
    }

    @Override
    public SessionID createIdentifier() {
        final UUID uuid = UUID.randomUUID();
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return SessionID.createSessionID(bb.array());
    }

    @Override
    protected BasicComponentInstance instantiateComponentInstance(final AtomicReference<ManagedReference> instanceReference, final Interceptor preDestroyInterceptor, final Map<Method, Interceptor> methodInterceptors, final InterceptorFactoryContext interceptorContext) {
        return new StatefulSessionComponentInstance(this, instanceReference, preDestroyInterceptor, methodInterceptors, interceptorContext);
    }

    @Override
    protected void componentInstanceCreated(final BasicComponentInstance basicComponentInstance, final InterceptorFactoryContext context) {
        final StatefulSessionComponentInstance instance = (StatefulSessionComponentInstance) basicComponentInstance;
        final Map<Object, Object> serializableInterceptors = new HashMap<Object, Object>();
        for (final Object key : serialiableInterceptorContextKeys) {
            @SuppressWarnings("unchecked")
            final AtomicReference<ManagedReference> data = (AtomicReference<ManagedReference>) context.getContextData().get(key);
            if (data != null) {
                serializableInterceptors.put(key, data.get().getInstance());
            }
        }
        instance.setSerializableInterceptors(serializableInterceptors);
    }

    /**
     * Removes the session associated with the <code>sessionId</code>.
     *
     * @param sessionId The session id
     */
    public void removeSession(final SessionID sessionId) {
        //The cache takes care of the transactional behavoir
        this.cache.remove(sessionId);
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

    public InterceptorFactory getPrePassivate() {
        return this.prePassivate;
    }

    public InterceptorFactory getPostActivate() {
        return this.postActivate;
    }

    public InterceptorFactory getEjb2XRemoveMethod() {
        return this.ejb2XRemoveMethod;
    }

    @Override
    public void start() {
        getShutDownInterceptorFactory().start();
        super.start();
        cache.start();
    }

    @Override
    public void stop() {
        getShutDownInterceptorFactory().shutdown();
        super.stop();
        cache.stop();
    }

    @Override
    public AllowedMethodsInformation getAllowedMethodsInformation() {
        return StatefulAllowedMethodsInformation.INSTANCE;
    }

    public Set<Object> getSerialiableInterceptorContextKeys() {
        return serialiableInterceptorContextKeys;
    }

    private static ServiceContainer currentServiceContainer() {
        return AccessController.doPrivileged(new PrivilegedAction<ServiceContainer>() {
            @Override
            public ServiceContainer run() {
                return CurrentServiceContainer.getServiceContainer();
            }
        });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14625.java