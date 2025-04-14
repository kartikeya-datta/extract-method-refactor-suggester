error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8691.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8691.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8691.java
text:
```scala
T@@ransactionUtil.getTransaction().toString());

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

package org.jboss.as.jpa.container;

import java.io.IOException;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SynchronizationType;

import org.jboss.as.jpa.service.PersistenceUnitServiceImpl;
import org.jboss.as.jpa.transaction.TransactionUtil;
import org.jboss.as.jpa.util.JPAServiceNames;
import org.jboss.as.server.CurrentServiceContainer;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;

import static org.jboss.as.jpa.messages.JpaLogger.JPA_LOGGER;
import static org.jboss.as.jpa.messages.JpaMessages.MESSAGES;

/**
 * Transaction scoped entity manager will be injected into SLSB or SFSB beans.  At bean invocation time, they
 * will join the active transaction if one is present.  Otherwise, they will simply be cleared at the end of
 * the bean invocation.
 * <p/>
 * This is a proxy for the underlying persistent provider EntityManager.
 *
 * @author Scott Marlow
 */
public class TransactionScopedEntityManager extends AbstractEntityManager implements Serializable {

    private static final long serialVersionUID = 455498112L;

    private final String puScopedName;          // Scoped name of the persistent unit
    private final Map properties;
    private transient EntityManagerFactory emf;
    private transient boolean isJPA21=true;          // true if persistence provider supports JPA 2.1
    private final SynchronizationType synchronizationType;

    public TransactionScopedEntityManager(String puScopedName, Map properties, EntityManagerFactory emf, SynchronizationType synchronizationType) {
        this.puScopedName = puScopedName;
        this.properties = properties;
        this.emf = emf;
        this.synchronizationType = synchronizationType;
    }

    @Override
    protected EntityManager getEntityManager() {
        EntityManager entityManager;
        boolean isInTx;

        isInTx = TransactionUtil.isInTx();

        if (isInTx) {
            entityManager = getOrCreateTransactionScopedEntityManager(emf, puScopedName, properties, synchronizationType);
        } else {
            entityManager = NonTxEmCloser.get(puScopedName);
            if (entityManager == null) {
                entityManager = createEntityManager(emf, properties, synchronizationType);
                NonTxEmCloser.add(puScopedName, entityManager);
            }
        }
        return entityManager;
    }

    @Override
    protected boolean isExtendedPersistenceContext() {
        return false;
    }

    @Override
    protected boolean isInTx() {
        return TransactionUtil.isInTx();
    }

    /**
     * Catch the application trying to close the container managed entity manager and throw an IllegalStateException
     */
    @Override
    public void close() {
        // Transaction scoped entity manager will be closed when the (owning) component invocation completes
        throw MESSAGES.cannotCloseTransactionContainerEntityManger();
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        // read all non-transient fields
        in.defaultReadObject();
        final ServiceController<?> controller = currentServiceContainer().getService(JPAServiceNames.getPUServiceName(puScopedName));
        final PersistenceUnitServiceImpl persistenceUnitService = (PersistenceUnitServiceImpl) controller.getService();
        emf = persistenceUnitService.getEntityManagerFactory();
        isJPA21 = true;
    }


    private static ServiceContainer currentServiceContainer() {
        return AccessController.doPrivileged(new PrivilegedAction<ServiceContainer>() {
            @Override
            public ServiceContainer run() {
                return CurrentServiceContainer.getServiceContainer();
            }
        });
    }

    @Override
    public SynchronizationType getSynchronizationType() {
        return synchronizationType;
    }

    /**
     * get or create a Transactional entity manager.
     * Only call while a transaction is active in the current thread.
     *
     * @param emf
     * @param scopedPuName
     * @param properties
     * @param synchronizationType
     * @return
     */
    private EntityManager getOrCreateTransactionScopedEntityManager(
            final EntityManagerFactory emf,
            final String scopedPuName,
            final Map properties,
            final SynchronizationType synchronizationType) {
        EntityManager entityManager = TransactionUtil.getTransactionScopedEntityManager(puScopedName);
        if (entityManager == null) {
            entityManager = createEntityManager(emf, properties, synchronizationType);
            if (JPA_LOGGER.isDebugEnabled())
                JPA_LOGGER.debugf("%s: created entity manager session %s", TransactionUtil.getEntityManagerDetails(entityManager),
                TransactionUtil.getTransaction().toString());
            TransactionUtil.registerSynchronization(entityManager, scopedPuName);
            TransactionUtil.putEntityManagerInTransactionRegistry(scopedPuName, entityManager);
        }
        else {
            testForMixedSyncronizationTypes(entityManager, puScopedName, synchronizationType);
            if (JPA_LOGGER.isDebugEnabled()) {
                JPA_LOGGER.debugf("%s: reuse entity manager session already in tx %s", TransactionUtil.getEntityManagerDetails(entityManager),
                    getTransaction().toString());
            }
        }
        return entityManager;
    }

    private EntityManager createEntityManager(
        EntityManagerFactory emf, Map properties, final SynchronizationType synchronizationType) {
        if (isJPA21()) {
            try {
                return emf.createEntityManager(synchronizationType, properties); // properties may be null in jpa 2.1
            } catch (AbstractMethodError consideredNotJPA21Exception) {          // dealing with JPA 1.0 or 2.0 provider?
                setJPA21(false);
            }

        }

        if (properties != null && properties.size() > 0) {
            return emf.createEntityManager(properties);
        }
        return emf.createEntityManager();
    }

    private boolean isJPA21() {
        return isJPA21;
    }

    private void setJPA21(boolean value) {
        isJPA21 = value;
    }


    /**
     * throw error if jta transaction already has an UNSYNCHRONIZED persistence context and a SYNCHRONIZED persistence context
     * is requested.  We are only fussy in this test, if the target component persistence context is SYNCHRONIZED.
     */
    private static void testForMixedSyncronizationTypes(EntityManager entityManager, String scopedPuName, final SynchronizationType targetSynchronizationType) {
        if (SynchronizationType.SYNCHRONIZED.equals(targetSynchronizationType)
                && entityManager instanceof AbstractEntityManager
                && SynchronizationType.UNSYNCHRONIZED.equals( ((AbstractEntityManager)entityManager).getSynchronizationType())) {
            throw MESSAGES.badSynchronizationTypeCombination(scopedPuName);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8691.java