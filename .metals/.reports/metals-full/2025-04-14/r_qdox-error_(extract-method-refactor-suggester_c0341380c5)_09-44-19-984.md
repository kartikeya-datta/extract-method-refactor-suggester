error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4609.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4609.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4609.java
text:
```scala
E@@jbLogger.ROOT_LOGGER.debugf("Trying to recover an imported transaction for Xid %s", this.xidTransactionID.getXid());

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

package org.jboss.as.ejb3.remote.protocol.versionone;

import java.io.IOException;

import javax.resource.spi.XATerminator;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import com.arjuna.ats.internal.jta.transaction.arjunacore.jca.SubordinateTransaction;
import com.arjuna.ats.internal.jta.transaction.arjunacore.jca.SubordinationManager;
import com.arjuna.ats.internal.jta.transaction.arjunacore.jca.XATerminatorImple;
import org.jboss.as.ejb3.logging.EjbLogger;
import org.jboss.as.ejb3.remote.EJBRemoteTransactionsRepository;
import org.jboss.ejb.client.XidTransactionID;
import org.jboss.marshalling.MarshallerFactory;
import org.xnio.IoUtils;


/**
 * @author Jaikiran Pai
 */
abstract class XidTransactionManagementTask implements Runnable {

    protected final short invocationId;
    protected final ChannelAssociation channelAssociation;
    protected final EJBRemoteTransactionsRepository transactionsRepository;
    protected final XidTransactionID xidTransactionID;
    protected final MarshallerFactory marshallerFactory;
    protected final TransactionRequestHandler transactionRequestHandler;

    XidTransactionManagementTask(final TransactionRequestHandler txRequestHandler, final EJBRemoteTransactionsRepository transactionsRepository,
                                 final MarshallerFactory marshallerFactory, final XidTransactionID xidTransactionID,
                                 final ChannelAssociation channelAssociation, final short invocationId) {

        this.transactionRequestHandler = txRequestHandler;
        this.channelAssociation = channelAssociation;
        this.marshallerFactory = marshallerFactory;
        this.invocationId = invocationId;
        this.transactionsRepository = transactionsRepository;
        this.xidTransactionID = xidTransactionID;
    }

    @Override
    public void run() {
        try {
            this.manageTransaction();
        } catch (Throwable t) {
            try {
                EjbLogger.ROOT_LOGGER.errorDuringTransactionManagement(t, this.xidTransactionID);
                // write out a failure message to the channel to let the client know that
                // the transaction operation failed
                transactionRequestHandler.writeException(this.channelAssociation, this.marshallerFactory, this.invocationId, t, null);
            } catch (IOException e) {
                EjbLogger.ROOT_LOGGER.couldNotWriteOutToChannel(e);
                // close the channel
                IoUtils.safeClose(this.channelAssociation.getChannel());
            }
            return;
        }

        try {
            // write out invocation success message to the channel
            transactionRequestHandler.writeTxInvocationResponseMessage(this.channelAssociation, this.invocationId);
        } catch (IOException e) {
            EjbLogger.ROOT_LOGGER.couldNotWriteInvocationSuccessMessage(e);
            // close the channel
            IoUtils.safeClose(this.channelAssociation.getChannel());
        }
    }

    protected abstract void manageTransaction() throws Throwable;

    protected void resumeTransaction(final Transaction transaction) throws Exception {
        final TransactionManager transactionManager = this.transactionsRepository.getTransactionManager();
        transactionManager.resume(transaction);
    }

    protected SubordinateTransaction tryRecoveryForImportedTransaction() throws Exception {
        final XATerminator xaTerminator = SubordinationManager.getXATerminator();
        if (xaTerminator instanceof XATerminatorImple) {
            EjbLogger.ROOT_LOGGER.debug("Trying to recover an imported transaction for Xid " + this.xidTransactionID.getXid());
            // We intentionally pass null for Xid since passing the specific Xid doesn't seem to work for some reason.
            // As for null for parentNodeName, we do that intentionally since we aren't aware of the parent node on which
            // the transaction originated
            ((XATerminatorImple) xaTerminator).doRecover(null, null);
        }
        // now that recovery has been completed via the XATerminator, it's possible that the subordinate tx will have been loaded
        return SubordinationManager.getTransactionImporter().getImportedTransaction(this.xidTransactionID.getXid());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4609.java