error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10728.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10728.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10728.java
text:
```scala
t@@his.hostPersister = ConfigurationPersisterFactory.createHostXmlConfigurationPersister(configurationFile, environment.getHostControllerName());

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

package org.jboss.as.host.controller;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HOST;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.extension.ExtensionRegistry;
import org.jboss.as.controller.persistence.ConfigurationFile;
import org.jboss.as.controller.persistence.ConfigurationPersistenceException;
import org.jboss.as.controller.persistence.ExtensibleConfigurationPersister;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.as.domain.controller.LocalHostControllerInfo;
import org.jboss.as.host.controller.logging.HostControllerLogger;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementWriter;
import org.wildfly.security.manager.WildFlySecurityManager;

/**
 * Configuration persister that can delegate to a domain or host persister depending what needs to be persisted.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class HostControllerConfigurationPersister implements ExtensibleConfigurationPersister {

    private final HostControllerEnvironment environment;
    private ExtensibleConfigurationPersister domainPersister;
    private final ExtensibleConfigurationPersister hostPersister;
    private final LocalHostControllerInfo hostControllerInfo;
    private final ExecutorService executorService;
    private final ExtensionRegistry extensionRegistry;
    private Boolean slave;

    public HostControllerConfigurationPersister(final HostControllerEnvironment environment, final LocalHostControllerInfo localHostControllerInfo,
                                                final ExecutorService executorService, final ExtensionRegistry extensionRegistry) {
        this.environment = environment;
        this.hostControllerInfo = localHostControllerInfo;
        this.executorService = executorService;
        this.extensionRegistry = extensionRegistry;
        final ConfigurationFile configurationFile = environment.getHostConfigurationFile();
        if (environment.getRunningModeControl().isReloaded()) {
            configurationFile.resetBootFile(environment.getRunningModeControl().isUseCurrentConfig());
        }
        this.hostPersister = ConfigurationPersisterFactory.createHostXmlConfigurationPersister(configurationFile, environment);
    }

    public void initializeDomainConfigurationPersister(boolean slave) {
        if (domainPersister != null) {
            throw HostControllerLogger.ROOT_LOGGER.configurationPersisterAlreadyInitialized();
        }

        final File configDir = environment.getDomainConfigurationDir();
        ConfigurationFile domainConfigurationFile = null;
        if (slave) {
            if (environment.isBackupDomainFiles()) {
                // --backup
                domainConfigurationFile = getBackupDomainConfigurationFile();
                domainPersister = ConfigurationPersisterFactory.createRemoteBackupDomainXmlConfigurationPersister(configDir, executorService, extensionRegistry);
            } else if(environment.isUseCachedDc()) {
                // --cached-dc
                domainConfigurationFile = getBackupDomainConfigurationFile();
                domainPersister = ConfigurationPersisterFactory.createCachedRemoteDomainXmlConfigurationPersister(configDir, executorService, extensionRegistry);
            } else {
                domainPersister = ConfigurationPersisterFactory.createTransientDomainXmlConfigurationPersister(executorService, extensionRegistry);
            }
        } else {
            domainConfigurationFile = getStandardDomainConfigurationFile();
            if (environment.getRunningModeControl().isReloaded()) {
                if (environment.isBackupDomainFiles()) {
                    // We may have been promoted to master and reloaded.
                    // See if we should still use the domain-cached-remote.xml.
                    // If the standard file is newer or same age, we assume the user has either moved
                    // the cached-remote file to the standard file location, or has copied in the
                    // standard file from elsewhere. Prior to WFLY-3108 doing one of these was required,
                    // so we must support people who continue to do so. Plus it's valid to want to
                    // use the standard file name.
                    ConfigurationFile cachedRemote = getBackupDomainConfigurationFile();
                    File cachedRemoteFile = cachedRemote.getBootFile();
                    if (cachedRemoteFile.exists()
                            && cachedRemoteFile.lastModified() > domainConfigurationFile.getBootFile().lastModified()) {
                        domainConfigurationFile = cachedRemote;
                    }
                }
                domainConfigurationFile.resetBootFile(environment.getRunningModeControl().isUseCurrentDomainConfig());
            }

            domainPersister = ConfigurationPersisterFactory.createDomainXmlConfigurationPersister(domainConfigurationFile, executorService, extensionRegistry);
        }
        // Store this back to environment so mgmt api that exposes it can still work
        environment.setDomainConfigurationFile(domainConfigurationFile);

        this.slave = slave;
    }

    public boolean isSlave() {
        if (slave == null) {
            throw HostControllerLogger.ROOT_LOGGER.mustInvokeBeforeCheckingSlaveStatus("initializeDomainConfigurationPersister");
        }
        return slave;
    }

    public ExtensibleConfigurationPersister getDomainPersister() {
        if (domainPersister == null) {
            throw HostControllerLogger.ROOT_LOGGER.mustInvokeBeforePersisting("initializeDomainConfigurationPersister");
        }
        return domainPersister;
    }

    public ExtensibleConfigurationPersister getHostPersister() {
        return hostPersister;
    }

    @Override
    public PersistenceResource store(ModelNode model, Set<PathAddress> affectedAddresses) throws ConfigurationPersistenceException {
        final PersistenceResource[] delegates = new PersistenceResource[2];
        for (PathAddress addr : affectedAddresses) {
            if (delegates[0] == null && addr.size() > 0 && HOST.equals(addr.getElement(0).getKey()) && addr.getElement(0).getValue().equals(hostControllerInfo.getLocalHostName())) {
                ModelNode hostModel = new ModelNode();
                hostModel.set(model.get(HOST, hostControllerInfo.getLocalHostName()));
                delegates[0] = hostPersister.store(hostModel, affectedAddresses);
            } else if (delegates[1] == null && (addr.size() == 0 || !HOST.equals(addr.getElement(0).getKey()))) {
                delegates[1] = getDomainPersister().store(model, affectedAddresses);
            }

            if (delegates[0] != null && delegates[1] != null) {
                break;
            }
        }

        return new PersistenceResource() {
            @Override
            public void commit() {
                if (delegates[0] != null) {
                    delegates[0].commit();
                }
                if (delegates[1] != null) {
                    delegates[1].commit();
                }
            }

            @Override
            public void rollback() {
                if (delegates[0] != null) {
                    delegates[0].rollback();
                }
                if (delegates[1] != null) {
                    delegates[1].rollback();
                }
            }
        };
    }

    @Override
    public void marshallAsXml(ModelNode model, OutputStream output) throws ConfigurationPersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ModelNode> load() throws ConfigurationPersistenceException {
        return hostPersister.load();
    }

    @Override
    public void successfulBoot() throws ConfigurationPersistenceException {
        hostPersister.successfulBoot();
        if (domainPersister != null) {
            domainPersister.successfulBoot();
        }
    }

    @Override
    public String snapshot() throws ConfigurationPersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SnapshotInfo listSnapshots() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteSnapshot(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void registerSubsystemWriter(String name, XMLElementWriter<SubsystemMarshallingContext> writer) {
        domainPersister.registerSubsystemWriter(name, writer);
    }

    @Override
    public void unregisterSubsystemWriter(String name) {
        domainPersister.unregisterSubsystemWriter(name);
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public void registerSubsystemDeploymentWriter(String name, XMLElementWriter<SubsystemMarshallingContext> writer) {
        domainPersister.registerSubsystemDeploymentWriter(name, writer);
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public void unregisterSubsystemDeploymentWriter(String name) {
        domainPersister.unregisterSubsystemDeploymentWriter(name);
    }

    private ConfigurationFile getStandardDomainConfigurationFile() {
        final String defaultDomainConfig = WildFlySecurityManager.getPropertyPrivileged(HostControllerEnvironment.JBOSS_DOMAIN_DEFAULT_CONFIG, "domain.xml");
        final String initialDomainConfig = environment.getInitialDomainConfig();
        return new ConfigurationFile(environment.getDomainConfigurationDir(), defaultDomainConfig,
                initialDomainConfig == null ? environment.getDomainConfig() : initialDomainConfig, initialDomainConfig == null);
    }

    private ConfigurationFile getBackupDomainConfigurationFile() {
        return new ConfigurationFile(environment.getDomainConfigurationDir(), ConfigurationPersisterFactory.CACHED_DOMAIN_XML,
                null, environment.getInitialDomainConfig() == null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10728.java