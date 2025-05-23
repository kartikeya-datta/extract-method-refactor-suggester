error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4605.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4605.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4605.java
text:
```scala
l@@ogger.debugf("Registered EJB XA resource deserializer %s", EJBXAResourceDeserializer.INSTANCE);

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

package org.jboss.as.ejb3.remote;

import com.arjuna.ats.arjuna.common.Uid;
import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionImple;
import com.arjuna.ats.internal.jta.transaction.arjunacore.jca.SubordinateTransaction;
import com.arjuna.ats.internal.jta.transaction.arjunacore.jca.SubordinationManager;
import com.arjuna.ats.internal.jta.transaction.arjunacore.jca.TransactionImporter;
import com.arjuna.ats.internal.jta.transaction.arjunacore.jca.TransactionImporterImple;
import com.arjuna.ats.internal.jta.transaction.arjunacore.jca.XATerminatorImple;
import com.arjuna.ats.jbossatx.jta.RecoveryManagerService;
import org.jboss.ejb.client.UserTransactionID;
import org.jboss.ejb.client.XidTransactionID;
import org.jboss.logging.Logger;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

import javax.resource.spi.XATerminator;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Jaikiran Pai
 */
public class EJBRemoteTransactionsRepository implements Service<EJBRemoteTransactionsRepository> {

    private static final Logger logger = Logger.getLogger(EJBRemoteTransactionsRepository.class);

    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("ejb").append("remote-transactions-repository");

    private final InjectedValue<TransactionManager> transactionManagerInjectedValue = new InjectedValue<TransactionManager>();

    private final InjectedValue<UserTransaction> userTransactionInjectedValue = new InjectedValue<UserTransaction>();

    private final InjectedValue<RecoveryManagerService> recoveryManagerService = new InjectedValue<>();

    private final Map<UserTransactionID, Uid> userTransactions = Collections.synchronizedMap(new HashMap<UserTransactionID, Uid>());

    @Override
    public void start(StartContext context) throws StartException {
        recoveryManagerService.getValue().addSerializableXAResourceDeserializer(EJBXAResourceDeserializer.INSTANCE);
        logger.debug("Registered EJB XA resource deserializer " + EJBXAResourceDeserializer.INSTANCE);
    }

    @Override
    public void stop(StopContext context) {
    }

    @Override
    public EJBRemoteTransactionsRepository getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    public TransactionManager getTransactionManager() {
        return this.transactionManagerInjectedValue.getValue();
    }

    /**
     * Removes any references maintained for the passed <code>{@link UserTransactionID}</code>
     * @param userTransactionID User transaction id
     * @return Returns the {@link Transaction} corresponding to the passed <code>userTransactionID</code>. If there
     *          is no such transaction, then this method returns null
     */
    public Transaction removeUserTransaction(final UserTransactionID userTransactionID) {
        final Uid uid = this.userTransactions.remove(userTransactionID);
        if (uid == null) {
            return null;
        }
        return TransactionImple.getTransaction(uid);
    }

    /**
     * @param userTransactionID User transaction id
     * @return Returns the {@link Transaction} corresponding to the passed <code>userTransactionID</code>. If there
     *          is no such transaction, then this method returns null
     */
    public Transaction getUserTransaction(final UserTransactionID userTransactionID) {
        final Uid uid = this.userTransactions.get(userTransactionID);
        if (uid == null) {
            return null;
        }
        return TransactionImple.getTransaction(uid);
    }

    /**
     * {@link javax.transaction.UserTransaction#begin() Begins} a new {@link UserTransaction} and
     * associates it with the passed {@link UserTransactionID}.
     * @param userTransactionID
     * @return Returns the transaction that has begun
     * @throws SystemException
     * @throws NotSupportedException
     */
    Transaction beginUserTransaction(final UserTransactionID userTransactionID) throws SystemException, NotSupportedException {
        this.getUserTransaction().begin();
        // get the tx that just got created and associated with the transaction manager
        final TransactionImple newlyAssociatedTx = TransactionImple.getTransaction();
        final Uid uid = newlyAssociatedTx.get_uid();
        this.userTransactions.put(userTransactionID, uid);
        return newlyAssociatedTx;
    }

    /**
     * Returns a {@link SubordinateTransaction} associated with the passed {@link XidTransactionID}.
     * If there's no such transaction, then this method returns null.
     *
     * @param xidTransactionID The {@link XidTransactionID}
     * @return
     * @throws XAException
     */
    public SubordinateTransaction getImportedTransaction(final XidTransactionID xidTransactionID) throws XAException {
        final Xid xid = xidTransactionID.getXid();
        final TransactionImporter transactionImporter = SubordinationManager.getTransactionImporter();
        return transactionImporter.getImportedTransaction(xid);
    }

    /**
     * Imports a {@link Transaction} into the {@link SubordinationManager} and associates it with the
     * passed {@link org.jboss.ejb.client.XidTransactionID#getXid()}  Xid}. Returns the imported transaction
     *
     * @param xidTransactionID The {@link XidTransactionID}
     * @param txTimeout The transaction timeout
     * @return
     * @throws XAException
     */
    Transaction importTransaction(final XidTransactionID xidTransactionID, final int txTimeout) throws XAException {
        final TransactionImporter transactionImporter = SubordinationManager.getTransactionImporter();
        return transactionImporter.importTransaction(xidTransactionID.getXid(), txTimeout);
    }

    public Xid[] getXidsToRecoverForParentNode(final String parentNodeName, int recoveryFlags) throws XAException {
        final Set<Xid> xidsToRecover = new HashSet<Xid>();
        final TransactionImporter transactionImporter = SubordinationManager.getTransactionImporter();
        if (transactionImporter instanceof TransactionImporterImple) {
            final Set<Xid> inFlightXids = ((TransactionImporterImple) transactionImporter).getInflightXids(parentNodeName);
            if (inFlightXids != null) {
                xidsToRecover.addAll(inFlightXids);
            }
        }
        final XATerminator xaTerminator = SubordinationManager.getXATerminator();
        if (xaTerminator instanceof XATerminatorImple) {
            final Xid[] inDoubtTransactions = ((XATerminatorImple) xaTerminator).doRecover(null, parentNodeName);
            if (inDoubtTransactions != null) {
                xidsToRecover.addAll(Arrays.asList(inDoubtTransactions));
            }
        } else {
            final Xid[] inDoubtTransactions = xaTerminator.recover(recoveryFlags);
            if (inDoubtTransactions != null) {
                xidsToRecover.addAll(Arrays.asList(inDoubtTransactions));
            }
        }
        return xidsToRecover.toArray(new Xid[0]);
    }

    public UserTransaction getUserTransaction() {
        return this.userTransactionInjectedValue.getValue();
    }

    public Injector<TransactionManager> getTransactionManagerInjector() {
        return this.transactionManagerInjectedValue;
    }

    public Injector<UserTransaction> getUserTransactionInjector() {
        return this.userTransactionInjectedValue;
    }

    public Injector<RecoveryManagerService> getRecoveryManagerInjector() {
        return this.recoveryManagerService;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4605.java