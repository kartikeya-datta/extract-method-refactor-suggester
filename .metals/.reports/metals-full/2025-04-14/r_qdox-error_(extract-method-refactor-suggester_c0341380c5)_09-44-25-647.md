error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1635.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1635.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1635.java
text:
```scala
final S@@erviceController<?> tmController = serviceRegistry.getService(TxnServices.JBOSS_TXN_ARJUNA_TRANSACTION_MANAGER);

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

import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.jboss.as.model.AbstractSubsystemElement;
import org.jboss.as.model.AbstractSubsystemUpdate;
import org.jboss.as.model.UpdateContext;
import org.jboss.as.model.UpdateResultHandler;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
final class TransactionsSubsystemElement extends AbstractSubsystemElement<TransactionsSubsystemElement> {

    private static final long serialVersionUID = 4097067542390229861L;

    private final RecoveryEnvironmentElement recoveryEnvironmentElement = new RecoveryEnvironmentElement();
    private final CoreEnvironmentElement coreEnvironmentElement = new CoreEnvironmentElement();
    private final CoordinatorEnvironmentElement coordinatorEnvironmentElement = new CoordinatorEnvironmentElement();
    private final ObjectStoreEnvironmentElement objectStoreEnvironmentElement = new ObjectStoreEnvironmentElement();

    public TransactionsSubsystemElement() {
        super(Namespace.TRANSACTIONS_1_0.getUriString());
    }

    @Override
    protected Class<TransactionsSubsystemElement> getElementClass() {
        return TransactionsSubsystemElement.class;
    }

    @Override
    public void writeContent(final XMLExtendedStreamWriter streamWriter) throws XMLStreamException {
        // BES this doesn't work right now; see if we can fix it as it should
        //streamWriter.writeStartElement(Namespace.TRANSACTIONS_1_0.name(), Element.RECOVERY_ENVIRONMENT.getLocalName());
        streamWriter.writeStartElement(Element.RECOVERY_ENVIRONMENT.getLocalName());
        recoveryEnvironmentElement.writeContent(streamWriter);
        // BES this doesn't work right now; see if we can fix it as it should
        //streamWriter.writeStartElement(Namespace.TRANSACTIONS_1_0.name(), Element.CORE_ENVIRONMENT.getLocalName());
        streamWriter.writeStartElement(Element.CORE_ENVIRONMENT.getLocalName());
        coreEnvironmentElement.writeContent(streamWriter);

        streamWriter.writeEmptyElement(Element.OBJECT_STORE.getLocalName());
        objectStoreEnvironmentElement.writeContent(streamWriter);

        streamWriter.writeEndElement();
    }

    public RecoveryEnvironmentElement getRecoveryEnvironmentElement() {
        return recoveryEnvironmentElement;
    }

    public CoreEnvironmentElement getCoreEnvironmentElement() {
        return coreEnvironmentElement;
    }

    public CoordinatorEnvironmentElement getCoordinatorEnvironmentElement() {
        return coordinatorEnvironmentElement;
    }

    public void setCoordinatorEnableStatistics(final boolean enable) {
        this.coordinatorEnvironmentElement.setEnableStatistics(enable);
    }

    public void setCoordinatorDefaultTimeout(final int timeout) {
        this.coordinatorEnvironmentElement.setDefaultTimeout(timeout);
    }

    public ObjectStoreEnvironmentElement getObjectStoreEnvironmentElement() {
        return objectStoreEnvironmentElement;
    }

    /** {@inheritDoc} */
    @Override
    protected void getUpdates(List<? super AbstractSubsystemUpdate<TransactionsSubsystemElement, ?>> list) {
    }

    /** {@inheritDoc} */
    @Override
    protected boolean isEmpty() {
        return true;
    }

    @Override
    protected TransactionSubsystemAdd getAdd() {
        final TransactionSubsystemAdd add = new TransactionSubsystemAdd();
        // TODO why not new TransactionSubsystemAdd(this) and do this in c'tor?
        add.setBindingName(coreEnvironmentElement.getBindingRef());
        add.setMaxPorts(coreEnvironmentElement.getMaxPorts());
        add.setNodeIdentifier(coreEnvironmentElement.getNodeIdentifier());
        add.setRecoveryBindingName(recoveryEnvironmentElement.getBindingRef());
        add.setRecoveryStatusBindingName(recoveryEnvironmentElement.getStatusBindingRef());
        add.setCoordinatorEnableStatistics(coordinatorEnvironmentElement.isEnableStatistics());
        add.setCoordinatorDefaultTimeout(coordinatorEnvironmentElement.getDefaultTimeout());
        add.setObjectStorePathRef(objectStoreEnvironmentElement.getRelativeTo());
        add.setObjectStoreDirectory(objectStoreEnvironmentElement.getPath());
        return add;
    }

    @Override
    protected <P> void applyRemove(final UpdateContext updateContext, final UpdateResultHandler<? super Void, P> resultHandler, final P param) {
        final ServiceRegistry serviceRegistry = updateContext.getServiceRegistry();
        final ServiceController<?> tmController = serviceRegistry.getService(TxnServices.JBOSS_TXN_TRANSACTION_MANAGER);
        tmController.setMode(ServiceController.Mode.REMOVE);
        final ServiceController<?> xaController = serviceRegistry.getService(TxnServices.JBOSS_TXN_XA_TERMINATOR);
        xaController.setMode(ServiceController.Mode.REMOVE);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1635.java