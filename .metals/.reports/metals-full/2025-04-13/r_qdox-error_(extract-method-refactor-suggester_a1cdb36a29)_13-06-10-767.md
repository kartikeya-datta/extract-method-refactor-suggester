error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10141.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10141.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10141.java
text:
```scala
public static T@@ransactionSynchronizationRegistry getTransactionSynchronizationRegistry() {

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

package org.jboss.as.jpa.transaction;

import org.jboss.as.jpa.container.EntityManagerMetadata;
import org.jboss.as.jpa.container.EntityManagerUtil;
import org.jboss.logging.Logger;
import org.jboss.tm.TxUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import java.util.Map;

/**
 * Transaction utilities for JPA
 *
 * @author Scott Marlow (forked from code by Gavin King)
 */
public class TransactionUtil {

    private static final TransactionUtil INSTANCE = new TransactionUtil();
    private static final Logger log = Logger.getLogger("org.jboss.jpa");

    private static volatile TransactionSynchronizationRegistry transactionSynchronizationRegistry;
    private static volatile TransactionManager transactionManager;

    public static TransactionUtil getInstance() {
        return INSTANCE;
    }

    public static void setTransactionManager(TransactionManager tm) {
        if (transactionManager == null) {
            transactionManager = tm;
        }
    }

    public static TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public static void setTransactionSynchronizationRegistry(TransactionSynchronizationRegistry transactionSynchronizationRegistry) {
        if (TransactionUtil.transactionSynchronizationRegistry == null) {
            TransactionUtil.transactionSynchronizationRegistry = transactionSynchronizationRegistry;
        }
    }

    public boolean isInTx() {
        Transaction tx = getTransaction();
        if (tx == null || !TxUtils.isActive(tx))
            return false;
        return true;
    }

    /**
     * Register the specified entity manager (persistence context) with the current transaction.
     * Precondition:  Only call while a transaction is active in the current thread.
     *
     * @param scopedPuName is the fully (application deployment) scoped name of persistence unit
     * @param xpc is the entity manager (a org.jboss.as.jpa class) to register
     * @param underlyingEntityManager is the underlying entity manager obtained from the persistence provider
     */
    public void registerExtendedUnderlyingWithTransaction(String scopedPuName, EntityManager xpc, EntityManager underlyingEntityManager) {
        // xpc invoked this method, we cannot call xpc because it will recurse back to here, join with underloying em instead
        registerSynchronization(xpc, scopedPuName, false);
        underlyingEntityManager.joinTransaction();
        putEntityManagerInTransactionRegistry(scopedPuName, xpc);
    }

    /**
     * Register the specified entity manager (persistence context) with the current transaction.
     * Precondition:  Only call while a transaction is active in the current thread.
     *
     * @param scopedPuName is the fully (application deployment) scoped name of persistence unit
     * @param xpc is the entity manager (a org.jboss.as.jpa class) to register
     */
    public void registerExtendedWithTransaction(String scopedPuName, EntityManager xpc) {
        registerSynchronization(xpc, scopedPuName, false);
        putEntityManagerInTransactionRegistry(scopedPuName, xpc);
        xpc.joinTransaction();
    }

    /**
     * Get current persistence context.  Only call while a transaction is active in the current thread.
     *
     * @param puScopedName
     * @return
     */
    public EntityManager getTransactionScopedEntityManager(String puScopedName) {
        return getEntityManagerInTransactionRegistry(puScopedName);
    }

    /**
     * Get current PC or create a Transactional entity manager.
     * Only call while a transaction is active in the current thread.
     *
     * @param emf
     * @param scopedPuName
     * @param properties
     * @return
     */
    public EntityManager getOrCreateTransactionScopedEntityManager(EntityManagerFactory emf, String scopedPuName, Map properties) {
        EntityManager entityManager = getEntityManagerInTransactionRegistry(scopedPuName);
        if (entityManager == null) {
            entityManager = EntityManagerUtil.createEntityManager(emf, properties);
            if (log.isDebugEnabled())
                log.debug(getEntityManagerDetails(entityManager) + ": created entity manager session " +
                    getTransaction().toString());
            registerSynchronization(entityManager, scopedPuName, true);
            putEntityManagerInTransactionRegistry(scopedPuName, entityManager);
            entityManager.joinTransaction(); // force registration with TX
        } else {
            if (log.isDebugEnabled()) {
                log.debug(getEntityManagerDetails(entityManager) + ": reuse entity manager session already in tx" +
                    getTransaction().toString());
            }
        }
        return entityManager;
    }

    private void registerSynchronization(EntityManager entityManager, String puScopedName, boolean closeEMAtTxEnd) {
        Transaction tx = getTransaction();
        try {
            tx.registerSynchronization(new SessionSynchronization(entityManager, tx, closeEMAtTxEnd, puScopedName));
        } catch (RollbackException e) {
            throw new RuntimeException(e);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }

    private Transaction getTransaction() {
        try {
            return transactionManager.getTransaction();
        } catch (SystemException e) {
            throw new IllegalStateException("An error occured while getting the " +
                "transaction associated with the current thread: " + e);
        }
    }

    private static TransactionSynchronizationRegistry getTransactionSynchronizationRegistry() {
        return transactionSynchronizationRegistry;
    }


    private static String currentThread() {
        return Thread.currentThread().getName();
    }

    private static String getEntityManagerDetails(EntityManager manager) {
        String result = currentThread() + ":";  // show the thread for correlation with other modules
        EntityManagerMetadata metadata;
        try {
            if ((metadata = manager.unwrap(EntityManagerMetadata.class)) != null) {
                result += metadata.getPuName() +
                    ((metadata.isTransactionScopedEntityManager()) ? " [XPC]" : " [transactional]"
                    );
            }
        } catch (PersistenceException ignoreUnhandled) {  // TODO:  switch to a different way to lookup EntityManagerMetadata
            // so that we don't get this error on TransactionalEntityManager
            // (because the EntityManager is the underlying provider that
            // doesn't have the metadata.)
        }


        return result;
    }

    private EntityManager getEntityManagerInTransactionRegistry(String scopedPuName) {
        return (EntityManager) getTransactionSynchronizationRegistry().getResource(scopedPuName);
    }

    /**
     * Save the specified EntityManager in the local threads active transaction.  The TransactionSynchronizationRegistry
     * will clear the reference to the EntityManager when the transaction completes.
     *
     * @param scopedPuName
     * @param entityManager
     */
    private void putEntityManagerInTransactionRegistry(String scopedPuName, EntityManager entityManager) {
        getTransactionSynchronizationRegistry().putResource(scopedPuName, entityManager);
    }


    private static class SessionSynchronization implements Synchronization {
        private EntityManager manager;
        private boolean closeAtTxCompletion;
        private String scopedPuName;

        public SessionSynchronization(EntityManager session, Transaction tx, boolean close, String scopedPuName) {
            this.manager = session;
            closeAtTxCompletion = close;
            this.scopedPuName = scopedPuName;
        }

        public void beforeCompletion() {
        }

        public void afterCompletion(int status) {
            if (closeAtTxCompletion) {
                if (log.isDebugEnabled())
                    log.debug(getEntityManagerDetails(manager) + ": closing entity managersession ");
                manager.close();
            }
            // clear TX reference to entity manager
            getInstance().putEntityManagerInTransactionRegistry(scopedPuName, null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10141.java