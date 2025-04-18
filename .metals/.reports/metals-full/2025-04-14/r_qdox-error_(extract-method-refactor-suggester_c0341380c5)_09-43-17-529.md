error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5025.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5025.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5025.java
text:
```scala
private final S@@tatefulSessionComponentInstance statefulSessionComponentInstance;

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

import org.jboss.as.ejb3.component.AbstractEJBInterceptor;
import org.jboss.as.ejb3.concurrency.AccessTimeoutDetails;
import org.jboss.invocation.InterceptorContext;
import org.jboss.logging.Logger;

import javax.ejb.ConcurrentAccessTimeoutException;
import javax.ejb.EJBException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.TransactionSynchronizationRegistry;
import java.util.concurrent.locks.ReentrantLock;

import static org.jboss.as.ejb3.component.stateful.StatefulComponentInstanceInterceptor.getComponentInstance;

/**
 * {@link org.jboss.invocation.Interceptor} which manages {@link Synchronization} semantics on a stateful session bean.
 *
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 * @author Jaikiran Pai
 */
public class StatefulSessionSynchronizationInterceptor extends AbstractEJBInterceptor {
    private static final Logger log = Logger.getLogger(StatefulSessionSynchronizationInterceptor.class);

    private final ReentrantLock lock = new ReentrantLock(true);
    private volatile Object transactionKey = null;

    /**
     * Handles the exception that occured during a {@link StatefulSessionSynchronization transaction synchronization} callback
     * invocations.
     * <p/>
     * This method discards the <code>statefulSessionComponentInstance</code> and resets the {@link #transactionKey} before
     * releasing the {@link #lock} held by this thread, for the <code>statefulSessionComponentInstance</code>
     *
     * @param statefulSessionComponentInstance
     *          The stateful component instance involved in the transaction
     * @param t The {@link Throwable throwable}
     * @return
     */
    private Error handleThrowableInTxSync(final StatefulSessionComponentInstance statefulSessionComponentInstance, final Throwable t) {
        log.error("Discarding stateful component instance: " + statefulSessionComponentInstance + " due to exception", t);
        try {
            // discard the SFSB instance
            statefulSessionComponentInstance.discard();
        } finally {
            // reset the transaction key, before unlocking so that we don't leave a race condition
            // with some other thread getting the (just released) lock and accessing the (not yet reset) transaction key
            // in processInvocation()
            transactionKey = null;
            // release the lock associated with the SFSB instance
            this.releaseLock();
        }
        // throw back an appropriate exception
        if (t instanceof RuntimeException)
            throw (RuntimeException) t;
        if (t instanceof Error)
            throw (Error) t;
        throw (EJBException) new EJBException().initCause(t);
    }

    @Override
    public Object processInvocation(InterceptorContext context) throws Exception {
        final StatefulSessionComponent component = getComponent(context, StatefulSessionComponent.class);
        final StatefulSessionComponentInstance instance = getComponentInstance(context);

        final TransactionSynchronizationRegistry transactionSynchronizationRegistry = component.getTransactionSynchronizationRegistry();
        final AccessTimeoutDetails timeout = component.getAccessTimeout(context.getMethod());
        if (log.isTraceEnabled()) {
            log.trace("Trying to acquire lock: " + lock + " for stateful component instance: " + instance + " during invocation: " + context);
        }
        // we obtain a lock in this synchronization interceptor because the lock needs to be tied to the synchronization
        // so that it can released on the tx synchronization callbacks
        boolean acquired = lock.tryLock(timeout.getValue(), timeout.getTimeUnit());
        if (!acquired) {
            throw new ConcurrentAccessTimeoutException("EJB 3.1 FR 4.3.14.1 concurrent access timeout on " + context
                    + " - could not obtain lock within " + timeout.getValue() + timeout.getTimeUnit());
        }
        if (log.isTraceEnabled()) {
            log.trace("Acquired lock: " + lock + " for stateful component instance: " + instance + " during invocation: " + context);
        }

        Object currentTransactionKey = null;
        boolean wasTxSyncRegistered = false;
        try {
            // get the key to current transaction associated with this thread
            currentTransactionKey = transactionSynchronizationRegistry.getTransactionKey();
            // if this SFSB instance is already associated with a different transaction, then it's an error
            if (transactionKey != null) {
                if (!transactionKey.equals(currentTransactionKey))
                    throw new EJBException("EJB 3.1 FR 4.6 Stateful instance " + instance + " is already associated with tx " + transactionKey + " (current tx " + currentTransactionKey + ")");
            } else {
                // if the thread is currently associated with a tx, then register a tx synchronization
                if (currentTransactionKey != null) {
                    // keep track of the transaction that the SFSB instance is involved in, so that we can use it to
                    // compare against the tx of any subsequent invocations on same SFSB instance
                    transactionKey = currentTransactionKey;
                    // register a tx synchronization for this SFSB instance
                    final Synchronization statefulSessionSync = new StatefulSessionSynchronization(instance);
                    transactionSynchronizationRegistry.registerInterposedSynchronization(statefulSessionSync);
                    wasTxSyncRegistered = true;
                    if (log.isTraceEnabled()) {
                        log.trace("Registered tx synchronization: " + statefulSessionSync + " for tx: " + currentTransactionKey +
                                " associated with stateful component instance: " + instance);
                    }
                    // invoke the afterBegin callback on the SFSB
                    instance.afterBegin();
                }
            }
            // proceed with the invocation
            return context.proceed();

        } finally {
            // if the current call did *not* register a tx SessionSynchronization, then we have to explicitly mark the
            // SFSB instance as "no longer in use". If it registered a tx SessionSynchronization, then releasing the lock is
            // taken care off by a tx synchronization callbacks.
            if (!wasTxSyncRegistered) {
                releaseInstance(instance);
            }
        }
    }

    /**
     * Releases the passed {@link StatefulSessionComponentInstance} i.e. marks it as no longer in use. After releasing the
     * instance, this method releases the lock, held by this thread, on the stateful component instance.
     *
     * @param instance The stateful component instance
     */
    private void releaseInstance(final StatefulSessionComponentInstance instance) {
        try {
            // mark the SFSB instance as no longer in use
            instance.getComponent().getCache().release(instance);
        } finally {
            // release the lock on the SFSB instance
            this.releaseLock();
        }
    }

    /**
     * Releases the lock, held by this thread, on the stateful component instance.
     */
    private void releaseLock() {
        lock.unlock();
        if (log.isTraceEnabled()) {
            log.trace("Released lock: " + lock);
        }
    }

    private class StatefulSessionSynchronization implements Synchronization {

        private StatefulSessionComponentInstance statefulSessionComponentInstance;

        StatefulSessionSynchronization(StatefulSessionComponentInstance statefulSessionComponentInstance) {
            this.statefulSessionComponentInstance = statefulSessionComponentInstance;
        }

        @Override
        public void beforeCompletion() {
            try {
                if (log.isTraceEnabled()) {
                    log.trace("Before completion callback invoked on Transaction synchronization: " + this +
                            " of stateful component instance: " + statefulSessionComponentInstance);
                }

                statefulSessionComponentInstance.beforeCompletion();
            } catch (Throwable t) {
                throw handleThrowableInTxSync(statefulSessionComponentInstance, t);
            }
        }

        @Override
        public void afterCompletion(int status) {

            try {
                if (log.isTraceEnabled()) {
                    log.trace("After completion callback invoked on Transaction synchronization: " + this +
                            " of stateful component instance: " + statefulSessionComponentInstance);
                }
                statefulSessionComponentInstance.afterCompletion(status == Status.STATUS_COMMITTED);

            } catch (Throwable t) {
                throw handleThrowableInTxSync(statefulSessionComponentInstance, t);
            }
            //if the above code throws an exception the lock is released in handleThrowableInTxSync
            //This must be set to null before the lock is released.
            transactionKey = null;
            // tx has completed, so mark the SFSB instance as no longer in use
            releaseInstance(statefulSessionComponentInstance);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5025.java