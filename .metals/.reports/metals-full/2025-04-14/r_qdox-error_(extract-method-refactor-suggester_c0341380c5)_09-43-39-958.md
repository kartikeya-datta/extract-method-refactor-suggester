error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1633.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1633.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1633.java
text:
```scala
.@@addDependency(TxnServices.JBOSS_TXN_ARJUNA_TRANSACTION_MANAGER, com.arjuna.ats.jbossatx.jta.TransactionManagerService.class, defaultBootCtxService.getTxManagerInjector())

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

package org.jboss.as.connector.subsystems.connector;

import java.util.concurrent.Executor;

import org.jboss.as.connector.ConnectorServices;
import org.jboss.as.connector.bootstrap.DefaultBootStrapContextService;
import org.jboss.as.connector.deployers.RaDeploymentActivator;
import org.jboss.as.connector.workmanager.WorkManagerService;
import org.jboss.as.model.AbstractSubsystemAdd;
import org.jboss.as.model.BootUpdateContext;
import org.jboss.as.model.UpdateContext;
import org.jboss.as.model.UpdateFailedException;
import org.jboss.as.model.UpdateResultHandler;
import org.jboss.as.threads.ThreadsServices;
import org.jboss.as.txn.TxnServices;
import org.jboss.jca.core.api.bootstrap.CloneableBootstrapContext;
import org.jboss.jca.core.api.workmanager.WorkManager;
import org.jboss.jca.core.bootstrapcontext.BaseCloneableBootstrapContext;
import org.jboss.jca.core.workmanager.WorkManagerImpl;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.tm.JBossXATerminator;

/**
 * @author <a href="mailto:stefano.maestri@redhat.comdhat.com">Stefano
 *         Maestri</a>
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class ConnectorSubsystemAdd extends AbstractSubsystemAdd<ConnectorSubsystemElement> {

    private static final long serialVersionUID = -874698675049495644L;
    private boolean archiveValidation = true;
    private boolean archiveValidationFailOnError = true;
    private boolean archiveValidationFailOnWarn = false;
    private boolean beanValidation = true;

    private String shortRunningThreadPool = null;
    private String longRunningThreadPool = null;

    protected ConnectorSubsystemAdd() {
        super(Namespace.CURRENT.getUriString());
    }

    @Override
    protected <P> void applyUpdate(final UpdateContext updateContext, final UpdateResultHandler<? super Void, P> resultHandler,
            final P param) {
        final ServiceTarget serviceTarget = updateContext.getServiceTarget();

        WorkManager wm = new WorkManagerImpl();

        final WorkManagerService wmService = new WorkManagerService(wm);
        serviceTarget.addService(ConnectorServices.WORKMANAGER_SERVICE, wmService)
            .addDependency(ThreadsServices.EXECUTOR.append(shortRunningThreadPool), Executor.class, wmService.getExecutorShortInjector())
            .addDependency(ThreadsServices.EXECUTOR.append(longRunningThreadPool), Executor.class, wmService.getExecutorLongInjector())
            .addDependency(TxnServices.JBOSS_TXN_XA_TERMINATOR, JBossXATerminator.class, wmService.getXaTerminatorInjector())
            .setInitialMode(Mode.ACTIVE)
            .install();

        CloneableBootstrapContext ctx = new BaseCloneableBootstrapContext();
        final DefaultBootStrapContextService defaultBootCtxService = new DefaultBootStrapContextService(ctx);
        serviceTarget.addService(ConnectorServices.DEFAULT_BOOTSTRAP_CONTEXT_SERVICE, defaultBootCtxService)
            .addDependency(ConnectorServices.WORKMANAGER_SERVICE, WorkManager.class, defaultBootCtxService.getWorkManagerValueInjector())
            .addDependency(TxnServices.JBOSS_TXN_XA_TERMINATOR, JBossXATerminator.class, defaultBootCtxService.getXaTerminatorInjector())
            .addDependency(TxnServices.JBOSS_TXN_TRANSACTION_MANAGER, com.arjuna.ats.jbossatx.jta.TransactionManagerService.class, defaultBootCtxService.getTxManagerInjector())
            .setInitialMode(Mode.ACTIVE)
            .install();

        final ConnectorSubsystemConfiguration config = new ConnectorSubsystemConfiguration();

        config.setArchiveValidation(archiveValidation);
        config.setArchiveValidationFailOnError(archiveValidationFailOnError);
        config.setArchiveValidationFailOnWarn(archiveValidationFailOnWarn);
        config.setBeanValidation(false);

        final ConnectorConfigService connectorConfigService = new ConnectorConfigService(config);
        serviceTarget.addService(ConnectorServices.CONNECTOR_CONFIG_SERVICE, connectorConfigService)
            .addDependency(ConnectorServices.DEFAULT_BOOTSTRAP_CONTEXT_SERVICE, CloneableBootstrapContext.class, connectorConfigService.getDefaultBootstrapContextInjector())
            .setInitialMode(Mode.ACTIVE)
            .install();
    }

    protected void applyUpdateBootAction(BootUpdateContext updateContext) {
        applyUpdate(updateContext, UpdateResultHandler.NULL, null);
        new RaDeploymentActivator().activate(updateContext);
    }

    protected void applyUpdate(ConnectorSubsystemElement element) throws UpdateFailedException {
        element.setArchiveValidation(archiveValidation);
        element.setArchiveValidationFailOnError(archiveValidationFailOnError);
        element.setArchiveValidationFailOnWarn(archiveValidationFailOnWarn);
        element.setBeanValidation(false);
        element.setLongRunningThreadPool(longRunningThreadPool);
        element.setShortRunningThreadPool(shortRunningThreadPool);
    }

    protected ConnectorSubsystemElement createSubsystemElement() {
        ConnectorSubsystemElement element = new ConnectorSubsystemElement();
        element.setArchiveValidation(archiveValidation);
        element.setArchiveValidationFailOnError(archiveValidationFailOnError);
        element.setArchiveValidationFailOnWarn(archiveValidationFailOnWarn);
        element.setBeanValidation(false);
        element.setLongRunningThreadPool(longRunningThreadPool);
        element.setShortRunningThreadPool(shortRunningThreadPool);
        return element;
    }

    public boolean isArchiveValidation() {
        return archiveValidation;
    }

    public void setArchiveValidation(final boolean archiveValidation) {
        this.archiveValidation = archiveValidation;
    }

    public boolean isArchiveValidationFailOnError() {
        return archiveValidationFailOnError;
    }

    public void setArchiveValidationFailOnError(final boolean archiveValidationFailOnError) {
        this.archiveValidationFailOnError = archiveValidationFailOnError;
    }

    public boolean isArchiveValidationFailOnWarn() {
        return archiveValidationFailOnWarn;
    }

    public void setArchiveValidationFailOnWarn(final boolean archiveValidationFailOnWarn) {
        this.archiveValidationFailOnWarn = archiveValidationFailOnWarn;
    }

    public boolean isBeanValidation() {
        return false;
    }

    public void setBeanValidation(final boolean beanValidation) {
        this.beanValidation = beanValidation;
    }

    public String getShortRunningThreadPool() {
        return shortRunningThreadPool;
    }

    public void setShortRunningThreadPool(String shortRunningThreadPool) {
        this.shortRunningThreadPool = shortRunningThreadPool;
    }

    public String getLongRunningThreadPool() {
        return longRunningThreadPool;
    }

    public void setLongRunningThreadPool(String longRunningThreadPool) {
        this.longRunningThreadPool = longRunningThreadPool;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1633.java