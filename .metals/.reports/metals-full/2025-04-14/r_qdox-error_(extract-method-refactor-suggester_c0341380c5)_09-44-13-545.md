error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1362.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1362.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1059
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1362.java
text:
```scala
public interface TxAssociation {

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

p@@ackage org.jboss.as.cmp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.jboss.as.cmp.component.CmpEntityBeanComponent;
import org.jboss.as.cmp.context.CmpEntityBeanContext;
import org.jboss.logging.Logger;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.tm.TransactionLocal;
import org.jboss.tm.TxUtils;

import static org.jboss.as.cmp.CmpMessages.MESSAGES;

/**
 * @author John Bailey
 */
public class TransactionEntityMap implements Service<TransactionEntityMap> {
    private static final Logger log = Logger.getLogger(TransactionEntityMap.class);
    private final InjectedValue<TransactionManager> transactionManager = new InjectedValue<TransactionManager>();
    private TransactionLocal txSynch;

    public synchronized void start(StartContext context) throws StartException {
        txSynch = new TransactionLocal(transactionManager.getValue()) {
            public Transaction getTransaction() {
                try {
                    return transactionManager.getTransaction();
                } catch (SystemException e) {
                    throw MESSAGES.errorGettingCurrentTransaction(e);
                }
            }
        };
    }

    public synchronized void stop(StopContext context) {
        txSynch = null;
    }

    public synchronized TransactionEntityMap getValue() throws IllegalStateException, IllegalArgumentException {
        if (txSynch == null) {
            throw MESSAGES.noTransactionSync();
        }
        return this;
    }

    public Injector<TransactionManager> getTransactionManagerInjector() {
        return transactionManager;
    }

    /**
     * An instance can be in one of the three states:
     * <ul>
     * <li>not associated with the tx and, hence, does not need to be synchronized</li>
     * <li>associated with the tx and needs to be synchronized</li>
     * <li>associated with the tx but does not need to be synchronized</li>
     * </ul>
     * Implementations of TxAssociation implement these states.
     */
    public static interface TxAssociation {
        /**
         * Schedules the instance for synchronization. The instance might or might not be associated with the tx.
         *
         * @param tx      the transaction the instance should be associated with if not yet associated
         * @param context the instance to be scheduled for synchronization
         * @throws SystemException
         * @throws RollbackException
         */
        void scheduleSync(Transaction tx, CmpEntityBeanContext context) throws SystemException, RollbackException;

        /**
         * Synchronizes the instance if it is needed.
         *
         * @param thread  current thread
         * @param tx      current transaction
         * @param context the instance to be synchronized
         * @throws Exception thrown if synchronization failed
         */
        void synchronize(Thread thread, Transaction tx, CmpEntityBeanContext context) throws Exception;

        /**
         * Invokes ejbStore if needed
         *
         * @param thread  current thread
         * @param context the instance to be synchronized
         * @throws Exception thrown if synchronization failed
         */
        void invokeEjbStore(Thread thread, CmpEntityBeanContext context) throws Exception;
    }

    public static final TxAssociation NONE = new TxAssociation() {
        public void scheduleSync(Transaction tx, CmpEntityBeanContext context) throws SystemException, RollbackException {
            context.getComponent().getTransactionEntityMap().associate(tx, context);
            context.setTxAssociation(SYNC_SCHEDULED);
        }

        public void synchronize(Thread thread, Transaction tx, CmpEntityBeanContext context) {
            throw MESSAGES.methodNotSupported();
        }

        public void invokeEjbStore(Thread thread, CmpEntityBeanContext context) {
            throw MESSAGES.methodNotSupported();
        }
    };

    public static final TxAssociation SYNC_SCHEDULED = new TxAssociation() {
        public void scheduleSync(Transaction tx, CmpEntityBeanContext context) {
        }

        public void invokeEjbStore(Thread thread, CmpEntityBeanContext context) throws Exception {
            if (!context.isRemoved() && context.getPrimaryKeyUnchecked() != null) {
                CmpEntityBeanComponent container = context.getComponent();
                // set the context class loader before calling the store method
                container.invokeEjbStore(context);
            }
        }

        public void synchronize(Thread thread, Transaction tx, CmpEntityBeanContext context) throws Exception {
            // only synchronize if the id is not null.  A null id means
            // that the entity has been removed.
            if (!context.isRemoved() && context.getPrimaryKeyUnchecked() != null) {
                CmpEntityBeanComponent container = context.getComponent();

                // set the context class loader before calling the store method
                // store it
                container.storeEntity(context);
                context.setTxAssociation(SYNCHRONIZED);
            }
        }
    };

    public static final TxAssociation SYNCHRONIZED = new TxAssociation() {
        public void scheduleSync(Transaction tx, CmpEntityBeanContext context) {
            context.setTxAssociation(SYNC_SCHEDULED);
        }

        public void invokeEjbStore(Thread thread, CmpEntityBeanContext context) {
        }

        public void synchronize(Thread thread, Transaction tx, CmpEntityBeanContext context) {
        }
    };

    public static final TxAssociation PREVENT_SYNC = new TxAssociation() {
        public void scheduleSync(Transaction tx, CmpEntityBeanContext context) {
        }

        public void synchronize(Thread thread, Transaction tx, CmpEntityBeanContext context) throws Exception {
            CmpEntityBeanComponent container = context.getComponent();
            if (container.getStoreManager().isStoreRequired(context)) {
                throw MESSAGES.instanceEvictedBeforeSync(container.getComponentName(), context.getPrimaryKeyUnchecked());
            }
        }

        public void invokeEjbStore(Thread thread, CmpEntityBeanContext context) throws Exception {
            TransactionEntityMap.SYNC_SCHEDULED.invokeEjbStore(thread, context);
        }
    };

    /**
     * Used for instances in the create phase,
     * i.e. before the ejbCreate and until after the ejbPostCreate returns
     */
    public static final TxAssociation NOT_READY = new TxAssociation() {
        public void scheduleSync(Transaction tx, CmpEntityBeanContext context) {
        }

        public void synchronize(Thread thread, Transaction tx, CmpEntityBeanContext context) throws Exception {
        }

        public void invokeEjbStore(Thread thread, CmpEntityBeanContext context) throws Exception {
        }
    };

    /**
     * sync all EntityEnterpriseContext that are involved (and changed)
     * within a transaction.
     */
    public void synchronizeEntities(Transaction tx) {
        GlobalTxSynchronization globalSync = (GlobalTxSynchronization) txSynch.get(tx);
        if (globalSync != null) {
            globalSync.synchronize();
        }
    }

    public GlobalTxSynchronization getGlobalSynchronization(Transaction tx) throws RollbackException, SystemException {
        GlobalTxSynchronization globalSync = (GlobalTxSynchronization) txSynch.get(tx);
        if (globalSync == null) {
            globalSync = new GlobalTxSynchronization(tx);
            txSynch.set(tx, globalSync);
        }
        return globalSync;
    }

    public Transaction getTransaction() {
        return txSynch.getTransaction();
    }

    /**
     * associate instance with transaction
     */
    private void associate(Transaction tx, CmpEntityBeanContext context) throws RollbackException, SystemException {
        GlobalTxSynchronization globalSync = getGlobalSynchronization(tx);

        //There should be only one thread associated with this tx at a time.
        //Therefore we should not need to synchronize on entityFifoList to ensure exclusive
        //access.  entityFifoList is correct since it was obtained in a synch block.

        globalSync.associate(context);
    }

    // Inner

    /**
     * A list of instances associated with the transaction.
     */
    public static class GlobalTxSynchronization {
        private Transaction tx;
        private List<CmpEntityBeanContext> instances = new ArrayList<CmpEntityBeanContext>();
        private boolean synchronizing;

        private List<Synchronization> otherSync = Collections.emptyList();
        private Map<Object, Object> txLocals = Collections.emptyMap();

        public GlobalTxSynchronization(Transaction tx) {
            this.tx = tx;
        }


        public void associate(CmpEntityBeanContext context) {
            instances.add(context);
        }

        public void synchronize() {
            if (synchronizing || instances.isEmpty()) {
                return;
            }

            synchronizing = true;

            // This is an independent point of entry. We need to make sure the
            // thread is associated with the right context class loader
            Thread currentThread = Thread.currentThread();
            CmpEntityBeanContext context = null;
            try {
                for (CmpEntityBeanContext instance : instances) {
                    // any one can mark the tx rollback at any time so check
                    // before continuing to the next store
                    if (TxUtils.isRollback(tx)) {
                        return;
                    }
                    context = instance;
                    context.getTxAssociation().invokeEjbStore(currentThread, context);
                }

                for (CmpEntityBeanContext instance : instances) {
                    // any one can mark the tx rollback at any time so check
                    // before continuing to the next store
                    if (TxUtils.isRollback(tx)) {
                        return;
                    }
                    context = instance;
                    context.getTxAssociation().synchronize(currentThread, tx, context);
                }
            } catch (Exception causeByException) {
                // EJB 1.1 section 12.3.2 and EJB 2 section 18.3.3
                // exception during store must log exception, mark tx for
                // rollback and throw a TransactionRolledback[Local]Exception
                // if using caller's transaction.  All of this is handled by
                // the AbstractTxInterceptor and LogInterceptor.
                //
                // All we need to do here is mark the transaction for rollback
                // and rethrow the causeByException.  The caller will handle logging
                // and wraping with TransactionRolledback[Local]Exception.
                try {
                    tx.setRollbackOnly();
                } catch (Exception e) {
                    CmpLogger.ROOT_LOGGER.exceptionRollingBackTx(tx, e);
                }

                // Rethrow cause by exception
                if (causeByException instanceof EJBException) {
                    throw (EJBException) causeByException;
                }
                throw CmpMessages.MESSAGES.failedToStoreEntity(((context == null || context.getPrimaryKeyUnchecked() == null) ? "<null>" : context.getPrimaryKeyUnchecked().toString()), causeByException);
            } finally {
                synchronizing = false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1362.java