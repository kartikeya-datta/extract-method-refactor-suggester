error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5812.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5812.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5812.java
text:
```scala
i@@f(!componentInstance.isRemoved() && !componentInstance.isDiscarded()) {

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
package org.jboss.as.ejb3.component.entity.interceptors;

import javax.ejb.EJBException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.TransactionSynchronizationRegistry;
import org.jboss.as.ee.component.Component;
import org.jboss.as.ee.component.ComponentInstance;
import org.jboss.as.ee.component.ComponentInstanceInterceptorFactory;
import org.jboss.as.ejb3.component.interceptors.AbstractEJBInterceptor;
import org.jboss.as.ejb3.component.entity.EntityBeanComponent;
import org.jboss.as.ejb3.component.entity.EntityBeanComponentInstance;
import org.jboss.as.ejb3.tx.OwnableReentrantLock;
import org.jboss.invocation.Interceptor;
import org.jboss.invocation.InterceptorContext;
import org.jboss.invocation.InterceptorFactory;
import org.jboss.invocation.InterceptorFactoryContext;
import static org.jboss.as.ejb3.EjbLogger.ROOT_LOGGER;

/**
 * {@link org.jboss.invocation.Interceptor} which manages {@link javax.transaction.Synchronization} semantics on an entity bean.
 * <p/>
 * For now we are using a completely synchronized approach to entity concurrency. There is at most 1 entity active for a given primary
 * key at any time, and access is synchronized within a transaction.
 *
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 * @author Jaikiran Pai
 * @author Stuart Douglas
 */
public class EntityBeanSynchronizationInterceptor extends AbstractEJBInterceptor {

    private final Object threadLock = new Object();
    private final OwnableReentrantLock lock = new OwnableReentrantLock();

    @Override
    public Object processInvocation(InterceptorContext context) throws Exception {
        final EntityBeanComponent component = getComponent(context, EntityBeanComponent.class);
        final EntityBeanComponentInstance instance = (EntityBeanComponentInstance) context.getPrivateData(ComponentInstance.class);

        //we do not synchronize for instances that are not associated with an identity
        if (instance.getPrimaryKey() == null) {
            return context.proceed();
        }

        final TransactionSynchronizationRegistry transactionSynchronizationRegistry = component.getTransactionSynchronizationRegistry();
        if (ROOT_LOGGER.isTraceEnabled()) {
            ROOT_LOGGER.trace("Trying to acquire lock: " + lock + " for entity bean " + instance + " during invocation: " + context);
        }
        // we obtain a lock in this synchronization interceptor because the lock needs to be tied to the synchronization
        // so that it can released on the tx synchronization callbacks
        final Object lockOwner = getLockOwner(transactionSynchronizationRegistry);
        lock.pushOwner(lockOwner);
        try {
            lock.lock();
            synchronized (lock) {
                if (ROOT_LOGGER.isTraceEnabled()) {
                    ROOT_LOGGER.trace("Acquired lock: " + lock + " for entity bean instance: " + instance + " during invocation: " + context);
                }

                Object currentTransactionKey = null;
                try {
                    // get the key to current transaction associated with this thread
                    currentTransactionKey = transactionSynchronizationRegistry.getTransactionKey();
                    if (!instance.isSynchronizeRegistered()) {
                        component.getCache().reference(instance);
                        // if this entity instance is already associated with a different transaction, then it's an error

                        // if the thread is currently associated with a tx, then register a tx synchronization
                        if (currentTransactionKey != null) {
                            // register a tx synchronization for this entity instance
                            final Synchronization entitySynchronization = new EntityBeanSynchronization(instance, lockOwner);
                            transactionSynchronizationRegistry.registerInterposedSynchronization(entitySynchronization);
                            if (ROOT_LOGGER.isTraceEnabled()) {
                                ROOT_LOGGER.trace("Registered tx synchronization: " + entitySynchronization + " for tx: " + currentTransactionKey +
                                    " associated with stateful component instance: " + instance);
                            }
                        }
                        instance.setSynchronizationRegistered(true);
                    }
                    // proceed with the invocation
                    return context.proceed();

                } finally {
                    // if the current call did *not* register a tx SessionSynchronization, then we have to explicitly mark the
                    // entity instance as "no longer in use". If it registered a tx EntityBeanSynchronization, then releasing the lock is
                    // taken care off by a tx synchronization callbacks.
                    if (currentTransactionKey == null) {
                        instance.store();
                        releaseInstance(instance, true);
                    }
                }
            }
        } finally {
            lock.popOwner();
        }
    }

    /**
     * Releases the passed {@link EntityBeanComponentInstance} i.e. marks it as no longer in use. After releasing the
     * instance, this method releases the lock, held by this thread, on the stateful component instance.
     *
     * @param instance The stateful component instance
     * @param success
     */
    private void releaseInstance(final EntityBeanComponentInstance instance, final boolean success) {
        try {
            instance.getComponent().getCache().release(instance, success);
        } finally {
            instance.setSynchronizationRegistered(false);
            // release the lock on the SFSB instance
            this.releaseLock();
        }
    }

    /**
     * Releases the lock, held by this thread, on the stateful component instance.
     */
    private void releaseLock() {
        lock.unlock();
        if (ROOT_LOGGER.isTraceEnabled()) {
            ROOT_LOGGER.trace("Released lock: " + lock);
        }
    }

    private class EntityBeanSynchronization implements Synchronization {

        private final EntityBeanComponentInstance componentInstance;
        private final Object lockOwner;

        EntityBeanSynchronization(EntityBeanComponentInstance componentInstance, final Object lockOwner) {
            this.componentInstance = componentInstance;
            this.lockOwner = lockOwner;
        }

        @Override
        public void beforeCompletion() {
            synchronized (threadLock) {
                //invoke the EJB store method within the transaction
                try {
                    if(!componentInstance.isRemoved()) {
                        componentInstance.store();
                    }
                } catch (Throwable t) {
                    lock.pushOwner(lockOwner);
                    try {
                        handleThrowableInTxSync(componentInstance, t);
                    } finally {
                        lock.popOwner();
                    }
                }
            }
        }

        @Override
        public void afterCompletion(int status) {
            synchronized (threadLock) {
                // tx has completed, so mark the SFSB instance as no longer in use
                lock.pushOwner(lockOwner);
                try {
                    releaseInstance(componentInstance, status == Status.STATUS_COMMITTED);
                } finally {
                    lock.popOwner();
                }
            }
        }

        /**
         * Handles the exception that occurred during a {@link EntityBeanComponentInstance transaction synchronization} callback
         * invocations.
         * <p/>
         * This method discards the <code>EntityBeanComponentInstance</code> and resets the {@link #transactionKey} before
         * releasing the {@link #lock} held by this thread, for the <code>statefulSessionComponentInstance</code>
         *
         * @param instance The Entity component instance involved in the transaction
         * @param t        The {@link Throwable throwable}
         * @return
         */
        private Error handleThrowableInTxSync(final EntityBeanComponentInstance instance, final Throwable t) {
            ROOT_LOGGER.discardingEntityComponent(instance,t);
            try {
                // discard the Entity instance
                instance.discard();
            } finally {
                // release the lock associated with the SFSB instance
                EntityBeanSynchronizationInterceptor.this.releaseLock();
            }
            // throw back an appropriate exception
            if (t instanceof RuntimeException)
                throw (RuntimeException) t;
            if (t instanceof Error)
                throw (Error) t;
            throw (EJBException) new EJBException().initCause(t);
        }
    }

    public static final InterceptorFactory FACTORY = new ComponentInstanceInterceptorFactory() {
        @Override
        protected Interceptor create(final Component component, final InterceptorFactoryContext context) {
            return new EntityBeanSynchronizationInterceptor();
        }
    };

    /**
     * Use either the active transaction or the current thread as the lock owner
     *
     * @param transactionSynchronizationRegistry
     *         The synronization registry
     * @return The lock owner
     */
    private Object getLockOwner(final TransactionSynchronizationRegistry transactionSynchronizationRegistry) {
        Object owner = transactionSynchronizationRegistry.getTransactionKey();
        return owner != null ? owner : Thread.currentThread();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5812.java