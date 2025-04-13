error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6328.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6328.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6328.java
text:
```scala
final B@@atchBuilder builder = updateContext.getServiceTarget();

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.txn;

import org.jboss.as.model.AbstractSubsystemAdd;
import org.jboss.as.model.UpdateContext;
import org.jboss.as.model.UpdateResultHandler;
import org.jboss.as.services.net.SocketBinding;
import org.jboss.as.services.path.AbstractPathService;
import org.jboss.as.services.path.RelativePathService;
import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceBuilder.DependencyType;
import org.jboss.tm.JBossXATerminator;
import org.omg.CORBA.ORB;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class TransactionSubsystemAdd extends AbstractSubsystemAdd<TransactionsSubsystemElement> {

    private static final long serialVersionUID = 6904905763469774913L;
    private static final String INTERNAL_OBJECTSTORE_PATH = "jboss.transactions.object.store.path";

    private String recoveryBindingName;
    private String recoveryStatusBindingName;
    private String nodeIdentifier = "1";
    private String bindingName;
    private boolean coordinatorEnableStatistics;
    private String objectStorePathRef = "jboss.server.data.dir";
    private String objectStorePath = "tx-object-store";
    private int maxPorts = 10;
    private int coordinatorDefaultTimeout = 300;

    protected TransactionSubsystemAdd() {
        super(Namespace.CURRENT.getUriString());
    }

    @Override
    protected <P> void applyUpdate(final UpdateContext updateContext, final UpdateResultHandler<? super Void, P> resultHandler,
            final P param) {
        final BatchBuilder builder = updateContext.getBatchBuilder();
        // XATerminator has no deps, so just add it in there
        final XATerminatorService xaTerminatorService = new XATerminatorService();
        builder.addService(TxnServices.JBOSS_TXN_XA_TERMINATOR, xaTerminatorService).setInitialMode(ServiceController.Mode.ACTIVE)
            .install();

        final TransactionManagerService transactionManagerService = new TransactionManagerService(nodeIdentifier, maxPorts, coordinatorEnableStatistics, coordinatorDefaultTimeout);
        builder.addService(TxnServices.JBOSS_TXN_TRANSACTION_MANAGER, transactionManagerService)
            .addDependency(DependencyType.OPTIONAL, ServiceName.JBOSS.append("iiop", "orb"), ORB.class, transactionManagerService.getOrbInjector())
            .addDependency(TxnServices.JBOSS_TXN_XA_TERMINATOR, JBossXATerminator.class, transactionManagerService.getXaTerminatorInjector())
            .addDependency(SocketBinding.JBOSS_BINDING_NAME.append(recoveryBindingName), SocketBinding.class, transactionManagerService.getRecoveryBindingInjector())
            .addDependency(SocketBinding.JBOSS_BINDING_NAME.append(recoveryStatusBindingName), SocketBinding.class, transactionManagerService.getStatusBindingInjector())
            .addDependency(SocketBinding.JBOSS_BINDING_NAME.append(bindingName), SocketBinding.class, transactionManagerService.getSocketProcessBindingInjector())
            .addDependency(AbstractPathService.pathNameOf(INTERNAL_OBJECTSTORE_PATH), String.class, transactionManagerService.getPathInjector())
            .setInitialMode(ServiceController.Mode.ACTIVE)
            .install();

        RelativePathService.addService(INTERNAL_OBJECTSTORE_PATH, objectStorePath, objectStorePathRef, builder);
    }

    @Override
    protected TransactionsSubsystemElement createSubsystemElement() {
        TransactionsSubsystemElement element = new TransactionsSubsystemElement();
        element.getCoreEnvironmentElement().setBindingRef(bindingName);
        element.getCoreEnvironmentElement().setMaxPorts(maxPorts);
        element.getCoreEnvironmentElement().setNodeIdentifier(nodeIdentifier);
        element.getRecoveryEnvironmentElement().setBindingRef(recoveryBindingName);
        element.getRecoveryEnvironmentElement().setStatusBindingRef(recoveryStatusBindingName);
        element.getObjectStoreEnvironmentElement().setRelativeTo(objectStorePathRef);
        element.getObjectStoreEnvironmentElement().setPath(objectStorePath);
        element.setCoordinatorDefaultTimeout(coordinatorDefaultTimeout);
        element.setCoordinatorEnableStatistics(coordinatorEnableStatistics);
        return element;
    }

    public String getRecoveryBindingName() {
        return recoveryBindingName;
    }

    public void setRecoveryBindingName(final String recoveryBindingName) {
        this.recoveryBindingName = recoveryBindingName;
    }

    public String getRecoveryStatusBindingName() {
        return recoveryStatusBindingName;
    }

    public void setRecoveryStatusBindingName(final String recoveryStatusBindingName) {
        this.recoveryStatusBindingName = recoveryStatusBindingName;
    }

    public String getNodeIdentifier() {
        return nodeIdentifier;
    }

    public void setNodeIdentifier(final String nodeIdentifier) {
        this.nodeIdentifier = nodeIdentifier;
    }

    public String getBindingName() {
        return bindingName;
    }

    public void setBindingName(final String bindingName) {
        this.bindingName = bindingName;
    }

    public boolean isCoordinatorEnableStatistics() {
        return coordinatorEnableStatistics;
    }

    public void setCoordinatorEnableStatistics(final boolean coordinatorEnableStatistics) {
        this.coordinatorEnableStatistics = coordinatorEnableStatistics;
    }

    public void setCoordinatorDefaultTimeout(final int timeout) {
        this.coordinatorDefaultTimeout = timeout;
    }

    public String getObjectStoreDirectory() {
        return objectStorePath;
    }

    public String getObjectStorePathRef() {
        return objectStorePathRef;
    }

    public void setObjectStorePathRef(String objectStorePathRef) {
        this.objectStorePathRef = objectStorePathRef;
    }

    public void setObjectStoreDirectory(final String objectStoreDirectory) {
        this.objectStorePath = objectStoreDirectory;
    }

    public int getMaxPorts() {
        return maxPorts;
    }

    public void setMaxPorts(final int maxPorts) {
        this.maxPorts = maxPorts;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6328.java