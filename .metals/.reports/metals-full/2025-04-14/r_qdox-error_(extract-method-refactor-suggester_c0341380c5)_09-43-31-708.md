error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5051.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5051.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5051.java
text:
```scala
public synchronized v@@oid activate(Object primaryKey) {

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
package org.jboss.as.ejb3.component.entity;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.EntityBean;
import javax.ejb.Timer;

import org.jboss.as.ee.component.BasicComponent;
import org.jboss.as.ee.component.interceptors.InvocationType;
import org.jboss.as.ejb3.component.EjbComponentInstance;
import org.jboss.as.ejb3.context.EntityContextImpl;
import org.jboss.as.ejb3.timerservice.TimerImpl;
import org.jboss.as.naming.ManagedReference;
import org.jboss.invocation.Interceptor;
import org.jboss.invocation.InterceptorContext;

import static org.jboss.as.ejb3.EjbMessages.MESSAGES;

/**
 * @author Stuart Douglas
 */
public class EntityBeanComponentInstance extends EjbComponentInstance {

    /**
     * The primary key of this instance, is it is associated with an object identity
     */
    private volatile Object primaryKey;
    private volatile EntityContextImpl entityContext;
    private volatile boolean removed = false;
    private volatile boolean synchronizeRegistered;
    private volatile boolean reloadRequired = false;

    private final Interceptor ejbStore;
    private final Interceptor ejbActivate;
    private final Interceptor ejbLoad;
    private final Interceptor ejbPassivate;
    private final Interceptor unsetEntityContext;

    protected EntityBeanComponentInstance(final BasicComponent component, final AtomicReference<ManagedReference> instanceReference, final Interceptor preDestroyInterceptor, final Map<Method, Interceptor> methodInterceptors) {
        super(component, instanceReference, preDestroyInterceptor, methodInterceptors);
        final EntityBeanComponent ejbComponent = (EntityBeanComponent) component;
        this.ejbStore = ejbComponent.createInterceptor(ejbComponent.getEjbStore());
        this.ejbActivate = ejbComponent.createInterceptor(ejbComponent.getEjbActivate());
        this.ejbLoad = ejbComponent.createInterceptor(ejbComponent.getEjbLoad());
        this.ejbPassivate = ejbComponent.createInterceptor(ejbComponent.getEjbPassivate());
        this.unsetEntityContext = ejbComponent.createInterceptor(ejbComponent.getUnsetEntityContext());
    }

    @Override
    public EntityBeanComponent getComponent() {
        return (EntityBeanComponent) super.getComponent();
    }

    @Override
    public EntityBean getInstance() {
        return (EntityBean) super.getInstance();
    }

    public Object getPrimaryKey() {
        return primaryKey;
    }

    public void reload() {
        try {
            final EntityBeanComponent component = getComponent();
            final InterceptorContext loadContext = prepareInterceptorContext();
            loadContext.putPrivateData(InvocationType.class, InvocationType.ENTITY_EJB_EJB_LOAD);
            loadContext.setMethod(component.getEjbLoadMethod());
            ejbLoad.processInvocation(loadContext);
            reloadRequired = false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isReloadRequired() {
        return reloadRequired;
    }

    public void setReloadRequired(final boolean reloadRequired) {
        this.reloadRequired = reloadRequired;
    }

    public void discard() {
        if (!isDiscarded()) {
            getComponent().getCache().discard(this);
            this.primaryKey = null;
        }
        super.discard();
    }

    @Override
    protected void preDestroy() {
        try {
            invokeUnsetEntityContext();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void invokeUnsetEntityContext() throws Exception {
        final InterceptorContext context = prepareInterceptorContext();
        final EntityBeanComponent component = getComponent();
        context.setMethod(component.getUnsetEntityContextMethod());
        context.putPrivateData(InvocationType.class, InvocationType.UNSET_ENTITY_CONTEXT);
        unsetEntityContext.processInvocation(context);
    }

    /**
     * Associates this entity with a primary key. This method is called when an entity bean is moved from the
     * pool to the entity cache
     *
     * @param primaryKey The primary key to associate the entity with
     */
    public synchronized void associate(Object primaryKey) {
        this.primaryKey = primaryKey;
        try {
            final InterceptorContext context = prepareInterceptorContext();
            final EntityBeanComponent component = getComponent();
            final Method ejbActivateMethod = component.getEjbActivateMethod();
            context.setMethod(ejbActivateMethod);
            context.putPrivateData(InvocationType.class, InvocationType.ENTITY_EJB_ACTIVATE);
            ejbActivate.processInvocation(context);
            final InterceptorContext loadContext = prepareInterceptorContext();
            loadContext.putPrivateData(InvocationType.class, InvocationType.ENTITY_EJB_EJB_LOAD);
            loadContext.setMethod(component.getEjbLoadMethod());
            ejbLoad.processInvocation(loadContext);
        } catch (RemoteException e) {
            throw new WrappedRemoteException(e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Invokes the ejbStore method
     */
    public synchronized void store() {
        try {
            if (!removed) {
                invokeEjbStore();
            }
        } catch (RemoteException e) {
            throw new WrappedRemoteException(e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void invokeEjbStore() throws Exception {
        final InterceptorContext context = prepareInterceptorContext();
        final EntityBeanComponent component = getComponent();
        context.setMethod(component.getEjbStoreMethod());
        ejbStore.processInvocation(context);
    }

    /**
     * Prepares the instance for release by calling the ejbPassivate method.
     * <p/>
     * This method does not actually release this instance into the pool
     */
    public synchronized void passivate() {
        try {
            if (!removed) {
                final InterceptorContext context = prepareInterceptorContext();
                final EntityBeanComponent component = getComponent();
                context.setMethod(component.getEjbPassivateMethod());
                context.putPrivateData(InvocationType.class, InvocationType.ENTITY_EJB_PASSIVATE);
                ejbPassivate.processInvocation(context);
            }
            primaryKey = null;
            removed = false;
        } catch (RemoteException e) {
            throw new WrappedRemoteException(e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setupContext(final InterceptorContext interceptorContext) {

        final InvocationType invocationType = interceptorContext.getPrivateData(InvocationType.class);
        try {
            interceptorContext.putPrivateData(InvocationType.class, InvocationType.SET_ENTITY_CONTEXT);
            final EntityContextImpl entityContext = new EntityContextImpl(this);
            setEjbContext(entityContext);
            getInstance().setEntityContext(entityContext);
        } catch (RemoteException e) {
            throw new WrappedRemoteException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            interceptorContext.putPrivateData(InvocationType.class, invocationType);
        }
    }

    public EntityContextImpl getEjbContext() {
        return entityContext;
    }

    protected void setEjbContext(EntityContextImpl entityContext) {
        this.entityContext = entityContext;
    }

    public EJBObject getEjbObject() {
        final Object pk = getPrimaryKey();
        if (pk == null) {
            throw MESSAGES.cannotCallGetEjbObjectBeforePrimaryKeyAssociation();
        }
        return getComponent().getEJBObject(pk);
    }

    public EJBLocalObject getEjbLocalObject() {
        final Object pk = getPrimaryKey();
        if (pk == null) {
            throw MESSAGES.cannotCallGetEjbLocalObjectBeforePrimaryKeyAssociation();
        }
        return getComponent().getEJBLocalObject(pk);
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(final boolean removed) {
        this.removed = removed;
    }

    public synchronized void setSynchronizationRegistered(final boolean synchronizeRegistered) {
        this.synchronizeRegistered = synchronizeRegistered;
    }

    public synchronized boolean isSynchronizeRegistered() {
        return synchronizeRegistered;
    }

    protected void clearPrimaryKey() {
        this.primaryKey = null;
    }

    /**
     * Remove all timers for this entity bean. This method is transactional, so if the current TX is rolled back
     * the timers will not be removed
     */
    public void removeAllTimers() {
        //cancel all timers for this entity
        for (final Timer timer : getComponent().getTimerService().getTimers()) {
            if (timer instanceof TimerImpl) {
                TimerImpl timerImpl = (TimerImpl) timer;
                if (timerImpl.getPrimaryKey() != null && timerImpl.getPrimaryKey().equals(getPrimaryKey())) {
                    timer.cancel();
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5051.java