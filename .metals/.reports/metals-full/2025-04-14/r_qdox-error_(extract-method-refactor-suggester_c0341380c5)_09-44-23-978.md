error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4337.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4337.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4337.java
text:
```scala
d@@omainModel.initialiseAsSlaveDC(configurationPersister, deploymentRepository, fileRepository, hostRegistry.getValue());

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

package org.jboss.as.domain.controller;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATION_HEADERS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ROLLBACK_ON_RUNTIME_FAILURE;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.as.controller.ResultHandler;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.persistence.ConfigurationPersistenceException;
import org.jboss.as.controller.persistence.ExtensibleConfigurationPersister;
import org.jboss.as.process.CommandLineConstants;
import org.jboss.as.protocol.StreamUtils;
import org.jboss.as.server.deployment.api.DeploymentRepository;
import org.jboss.as.server.services.net.NetworkInterfaceBinding;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

/**
 * @author Emanuel Muckenhuber
 */
public final class DomainControllerService implements Service<DomainController> {

    private static final Logger log = Logger.getLogger("org.jboss.as.domain.controller");
    private final ExtensibleConfigurationPersister configurationPersister;
    private final DeploymentRepository deploymentRepository;
    private final FileRepository localFileRepository;
    private final InjectedValue<ScheduledExecutorService> scheduledExecutorService = new InjectedValue<ScheduledExecutorService>();
    private final InjectedValue<MasterDomainControllerClient> masterDomainControllerClient = new InjectedValue<MasterDomainControllerClient>();
    private final InjectedValue<LocalHostModel> hostController = new InjectedValue<LocalHostModel>();
    private final InjectedValue<NetworkInterfaceBinding> mgmtInterface = new InjectedValue<NetworkInterfaceBinding>();
    private final InjectedValue<HostRegistryService> hostRegistry = new InjectedValue<HostRegistryService>();
    private final String localHostName;
    private final boolean backupDomainFiles;
    private final boolean useCachedDc;
    private final int mgmtPort;
    private final DomainModelImpl domainModel;
    private DomainController controller;

    public DomainControllerService(final ExtensibleConfigurationPersister configurationPersister, final String localHostName, final int mgmtPort,
                                   final DeploymentRepository deploymentRepository, final FileRepository localFileRepository, final boolean backupDomainFiles,
                                   final boolean useCachedDc, final DomainModelImpl domainModel) {
        this.configurationPersister = configurationPersister;
        this.localHostName = localHostName;
        this.mgmtPort = mgmtPort;
        this.deploymentRepository = deploymentRepository;
        this.localFileRepository = localFileRepository;
        this.backupDomainFiles = backupDomainFiles;
        this.useCachedDc = useCachedDc;
        this.domainModel = domainModel;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void start(final StartContext context) throws StartException {
        MasterDomainControllerClient masterClient = masterDomainControllerClient.getOptionalValue();
        this.controller = masterClient == null ? startMasterDomainController() : startSlaveDomainController(masterClient);
        backupDomainFiles();
        hostController.getValue().startServers(controller);
        try {
            configurationPersister.successfulBoot();
        } catch (ConfigurationPersistenceException e) {
            throw new StartException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void stop(final StopContext context) {
        hostController.getValue().stopServers();
        MasterDomainControllerClient masterClient = masterDomainControllerClient.getOptionalValue();
        if (masterClient != null) {
            masterClient.unregister();
        }
        this.controller = null;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized DomainController getValue() throws IllegalStateException, IllegalArgumentException {
        final DomainController controller = this.controller;
        if(controller == null) {
            throw new IllegalStateException();
        }
        return controller;
    }

    public Injector<ScheduledExecutorService> getScheduledExecutorServiceInjector() {
        return scheduledExecutorService;
    }

    public Injector<LocalHostModel> getHostControllerServiceInjector() {
        return hostController;
    }

    public Injector<MasterDomainControllerClient> getMasterDomainControllerClientInjector() {
        return masterDomainControllerClient;
    }

    public Injector<NetworkInterfaceBinding> getInterfaceInjector() {
        return mgmtInterface;
    }

    public InjectedValue<HostRegistryService> getHostRegistryInjector() {
        return hostRegistry;
    }

    private DomainController startMasterDomainController() throws StartException {
        log.info("Starting Domain Controller");
        loadLocalDomainModel();
        return new DomainControllerImpl(scheduledExecutorService.getValue(), domainModel, localHostName, localFileRepository, deploymentRepository, hostRegistry.getValue());
    }

    private DomainController startSlaveDomainController(MasterDomainControllerClient masterClient) throws StartException {
        DomainController remoteController = startRemoteSlaveDomainController(masterClient);
        if (remoteController != null) {
            return remoteController;
        }
        if (useCachedDc) {
            return startLocalCopySlaveDomainController(masterClient);
        } else {
            throw new StartException("Could not contact master domain controller. No attempt to was made to start up from the cached domain controller since "
                    + CommandLineConstants.CACHED_DC + " was not passed in on the command line");
        }
    }

    private DomainController startRemoteSlaveDomainController(MasterDomainControllerClient masterClient) throws StartException {
        // By having a remote repo as a secondary content will be synced only if needed
        FallbackRepository fileRepository = new FallbackRepository(localFileRepository, masterClient.getRemoteFileRepository());
        domainModel.initialiseAsSlaveDC(configurationPersister, deploymentRepository, localFileRepository, hostRegistry.getValue());
        final DomainControllerImpl controller = new DomainControllerImpl(scheduledExecutorService.getValue(), domainModel, localHostName, localFileRepository, masterClient, hostRegistry.getValue());
        try {
            masterClient.register(hostController.getValue().getName(), mgmtInterface.getValue().getAddress(), mgmtPort, controller);
        } catch (IllegalStateException e) {
            return null;
        }

        try {
            configurationPersister.store(domainModel.getDomainModel());
        } catch (ConfigurationPersistenceException e) {
            log.error("Could not cache domain model", e);
        }

        return controller;
    }

    private DomainController startLocalCopySlaveDomainController(MasterDomainControllerClient masterClient) throws StartException {
        loadLocalDomainModel();
        return new DomainControllerImpl(scheduledExecutorService.getValue(), domainModel, localHostName, localFileRepository, masterClient, hostRegistry.getValue());
    }

    private void loadLocalDomainModel() throws StartException {
        domainModel.initialiseAsMasterDC(configurationPersister, deploymentRepository, localFileRepository, hostRegistry.getValue());

        final List<ModelNode> updates;
        try {
             updates = configurationPersister.load();
        } catch (final Exception e) {
            log.error("failed to start domain controller", e);
            throw new StartException(e);
        }

        final AtomicInteger count = new AtomicInteger(1);
        final ResultHandler resultHandler = new ResultHandler() {
            @Override
            public void handleResultFragment(final String[] location, final ModelNode result) {
            }

            @Override
            public void handleResultComplete() {
                if (count.decrementAndGet() == 0) {
                    // some action
                }
            }

            @Override
            public void handleFailed(final ModelNode failureDescription) {
                if (count.decrementAndGet() == 0) {
                    // some action
                }
            }

            @Override
            public void handleCancellation() {
                if (count.decrementAndGet() == 0) {
                    // some action
                }
            }
        };
        for (ModelNode update : updates) {
            count.incrementAndGet();
            update.get(OPERATION_HEADERS, ROLLBACK_ON_RUNTIME_FAILURE).set(false);
            domainModel.execute(OperationBuilder.Factory.create(update).build(), resultHandler);
        }
        if (count.decrementAndGet() == 0) {
            // some action?
        }
    }

    private void backupDomainFiles() {
        if (!backupDomainFiles) {
            return;
        }
        if (masterDomainControllerClient.getOptionalValue() == null) {
            log.warn(CommandLineConstants.BACKUP_DC + " is ignored so no backup of the domain controller files will happen, since the domain controller is running locally");
            return;
        }
        log.debug("Backing up the remote domain controller files");

        //Copy the original host since it will be overwritten by the copy from the domain
        final File originalHost = localFileRepository.getConfigurationFile("host.xml");
        File hostBackup;
        try {
            hostBackup = File.createTempFile("host", "xml", originalHost.getParentFile());
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
        try {
            copyFile(originalHost, hostBackup);
        } catch (IOException e) {
            throw new RuntimeException("Error backing up " + originalHost + " to " + hostBackup, e);
        }

        //Get the DC files, this will be handled for us by these calls on the client
        FileRepository remote = masterDomainControllerClient.getValue().getRemoteFileRepository();
        remote.getFile("");
        remote.getConfigurationFile("");
        remote.getDeploymentFiles(null);

        //We pulled the remote host which overwrote our host.xml, copy that to somewhere else and restore the backup
        final File remoteHost = new File(originalHost.getParent(), "remoteHost.xml");
        try {
            copyFile(originalHost, remoteHost);
            copyFile(hostBackup, originalHost);
        } catch (IOException e) {
            // AutoGenerated
            throw new RuntimeException("An error happened copying the remote domain controller files. " + originalHost + " has been overwritten. The original can be found at" + hostBackup);
        }
        hostBackup.delete();
    }

    private void copyFile(File src, File dest) throws IOException {
        if (dest.exists()) {
            dest.delete();
        }
        final InputStream in = new BufferedInputStream(new FileInputStream(src));
        try {
            final OutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
            try {
                int i = in.read();
                while (i != -1) {
                    out.write(i);
                    i = in.read();
                }
            } finally {
                StreamUtils.safeClose(out);
            }
        } finally {
            StreamUtils.safeClose(in);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4337.java