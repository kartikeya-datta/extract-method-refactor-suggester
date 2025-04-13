error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8008.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8008.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8008.java
text:
```scala
.@@addDependency(ConnectorServices.RESOURCE_ADAPTER_DEPLOYER_SERVICE_PREFIX.append(deploymentName))

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

package org.jboss.as.connector.metadata.deployment;

import org.jboss.as.connector.ConnectorServices;
import org.jboss.as.connector.registry.ResourceAdapterDeploymentRegistry;
import org.jboss.as.connector.services.AdminObjectReferenceFactoryService;
import org.jboss.as.connector.services.AdminObjectService;
import org.jboss.as.connector.services.ConnectionFactoryReferenceFactoryService;
import org.jboss.as.connector.services.ConnectionFactoryService;
import org.jboss.as.connector.subsystems.jca.JcaSubsystemConfiguration;
import org.jboss.as.connector.util.Injection;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.naming.ServiceBasedNamingStore;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.naming.service.BinderService;
import org.jboss.jca.common.api.metadata.ironjacamar.IronJacamar;
import org.jboss.jca.common.api.metadata.ra.ConfigProperty;
import org.jboss.jca.common.api.metadata.ra.Connector;
import org.jboss.jca.core.api.connectionmanager.ccm.CachedConnectionManager;
import org.jboss.jca.core.api.management.ManagementRepository;
import org.jboss.jca.core.spi.mdr.AlreadyExistsException;
import org.jboss.jca.core.spi.mdr.MetadataRepository;
import org.jboss.jca.core.spi.rar.ResourceAdapterRepository;
import org.jboss.jca.core.spi.transaction.TransactionIntegration;
import org.jboss.jca.deployers.common.AbstractResourceAdapterDeployer;
import org.jboss.jca.deployers.common.CommonDeployment;
import org.jboss.jca.deployers.common.DeployException;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.AbstractServiceListener;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.security.SubjectFactory;

import javax.resource.spi.IllegalStateException;
import javax.resource.spi.ResourceAdapter;
import javax.transaction.TransactionManager;
import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

import static org.jboss.as.connector.ConnectorLogger.DEPLOYMENT_CONNECTOR_LOGGER;
import static org.jboss.as.connector.ConnectorMessages.MESSAGES;

/**
 * A ResourceAdapterDeploymentService.
 * @author <a href="mailto:stefano.maestri@redhat.com">Stefano Maestri</a>
 * @author <a href="mailto:jesper.pedersen@jboss.org">Jesper Pedersen</a>
 */
public abstract class AbstractResourceAdapterDeploymentService {

    // Must be set by the start method
    protected ResourceAdapterDeployment value;

    protected final InjectedValue<MetadataRepository> mdr = new InjectedValue<MetadataRepository>();

    protected final InjectedValue<ResourceAdapterRepository> raRepository = new InjectedValue<ResourceAdapterRepository>();

    protected final InjectedValue<ResourceAdapterDeploymentRegistry> registry = new InjectedValue<ResourceAdapterDeploymentRegistry>();

    protected final InjectedValue<ManagementRepository> managementRepository = new InjectedValue<ManagementRepository>();

    protected final InjectedValue<JcaSubsystemConfiguration> config = new InjectedValue<JcaSubsystemConfiguration>();
    protected final InjectedValue<TransactionIntegration> txInt = new InjectedValue<TransactionIntegration>();
    protected final InjectedValue<SubjectFactory> subjectFactory = new InjectedValue<SubjectFactory>();
    protected final InjectedValue<CachedConnectionManager> ccmValue = new InjectedValue<CachedConnectionManager>();

    public ResourceAdapterDeployment getValue() {
        return ConnectorServices.notNull(value);
    }

    /**
     * Stop
     */
    public void stop(StopContext context) {
        if (value != null) {
            DEPLOYMENT_CONNECTOR_LOGGER.debugf("Undeploying: %s", value.getDeployment() != null ? value.getDeployment().getDeploymentName() : "");

            if (registry != null && registry.getValue() != null) {
                registry.getValue().unregisterResourceAdapterDeployment(value);
            }

            if (mdr != null && mdr.getValue() != null) {
                try {
                    mdr.getValue().unregisterResourceAdapter(value.getDeployment().getDeploymentName());
                } catch (Throwable t) {
                    DEPLOYMENT_CONNECTOR_LOGGER.debug("Exception during unregistering deployment", t);
                }
            }

            if (mdr != null && mdr.getValue() != null && value.getDeployment() != null
                    && value.getDeployment().getCfs() != null && value.getDeployment().getCfJndiNames() != null) {
                for (int i = 0; i < value.getDeployment().getCfs().length; i++) {
                    try {
                        String cf = value.getDeployment().getCfs()[i].getClass().getName();
                        String jndi = value.getDeployment().getCfJndiNames()[i];

                        mdr.getValue().unregisterJndiMapping(value.getDeployment().getURL().toExternalForm(), cf, jndi);
                    } catch (Throwable nfe) {
                        DEPLOYMENT_CONNECTOR_LOGGER.debug("Exception during JNDI unbinding", nfe);
                    }
                }
            }

            if (mdr != null && mdr.getValue() != null && value.getDeployment().getAos() != null
                    && value.getDeployment().getAoJndiNames() != null) {
                for (int i = 0; i < value.getDeployment().getAos().length; i++) {
                    try {
                        String ao = value.getDeployment().getAos()[i].getClass().getName();
                        String jndi = value.getDeployment().getAoJndiNames()[i];

                        mdr.getValue().unregisterJndiMapping(value.getDeployment().getURL().toExternalForm(), ao, jndi);
                    } catch (Throwable nfe) {
                        DEPLOYMENT_CONNECTOR_LOGGER.debug("Exception during JNDI unbinding", nfe);
                    }
                }
            }

            if (value.getDeployment() != null && value.getDeployment().getResourceAdapter() != null) {
                value.getDeployment().getResourceAdapter().stop();
            }
        }
    }

    public Injector<MetadataRepository> getMdrInjector() {
        return mdr;
    }

    public Injector<ResourceAdapterRepository> getRaRepositoryInjector() {
        return raRepository;
    }

    public Injector<ManagementRepository> getManagementRepositoryInjector() {
        return managementRepository;
    }

    public Injector<ResourceAdapterDeploymentRegistry> getRegistryInjector() {
        return registry;
    }

    public InjectedValue<JcaSubsystemConfiguration> getConfig() {
        return config;
    }

    public InjectedValue<TransactionIntegration> getTxIntegration() {
        return txInt;
    }

    public Injector<TransactionIntegration> getTxIntegrationInjector() {
        return txInt;
    }

    public Injector<JcaSubsystemConfiguration> getConfigInjector() {
        return config;
    }

    public Injector<SubjectFactory> getSubjectFactoryInjector() {
        return subjectFactory;
    }

    public Injector<CachedConnectionManager> getCcmInjector() {
        return ccmValue;
    }

    protected abstract class AbstractAS7RaDeployer extends AbstractResourceAdapterDeployer {

        protected final ServiceTarget serviceTarget;
        protected final URL url;
        protected final String deploymentName;
        protected final File root;
        protected final ClassLoader cl;
        protected final Connector cmd;

        protected AbstractAS7RaDeployer(ServiceTarget serviceTarget, URL url, String deploymentName, File root, ClassLoader cl,
                Connector cmd) {
            super(true);
            this.serviceTarget = serviceTarget;
            this.url = url;
            this.deploymentName = deploymentName;
            this.root = root;
            this.cl = cl;
            this.cmd = cmd;
        }

        public abstract CommonDeployment doDeploy() throws Throwable;

        @Override
        public String[] bindConnectionFactory(URL url, String deployment, Object cf) throws Throwable {
            throw MESSAGES.jndiBindingsNotSupported();
        }

        @Override
        public String[] bindConnectionFactory(URL url, String deployment, Object cf, final String jndi) throws Throwable {

            mdr.getValue().registerJndiMapping(url.toExternalForm(), cf.getClass().getName(), jndi);

            DEPLOYMENT_CONNECTOR_LOGGER.registeredConnectionFactory(jndi);

            final ConnectionFactoryService connectionFactoryService = new ConnectionFactoryService(cf);

            final ServiceName connectionFactoryServiceName = ConnectionFactoryService.SERVICE_NAME_BASE.append(jndi);
            serviceTarget.addService(connectionFactoryServiceName, connectionFactoryService)
                    .setInitialMode(ServiceController.Mode.ACTIVE).install();

            final ConnectionFactoryReferenceFactoryService referenceFactoryService = new ConnectionFactoryReferenceFactoryService();
            final ServiceName referenceFactoryServiceName = ConnectionFactoryReferenceFactoryService.SERVICE_NAME_BASE
                    .append(jndi);
            serviceTarget.addService(referenceFactoryServiceName, referenceFactoryService)
                    .addDependency(connectionFactoryServiceName, Object.class, referenceFactoryService.getDataSourceInjector())
                    .setInitialMode(ServiceController.Mode.ACTIVE).install();

            final ContextNames.BindInfo bindInfo = ContextNames.bindInfoFor(jndi);
            final BinderService binderService = new BinderService(bindInfo.getBindName());
            serviceTarget
                    .addService(bindInfo.getBinderServiceName(), binderService)
                    .addDependency(referenceFactoryServiceName, ManagedReferenceFactory.class,
                            binderService.getManagedObjectInjector())
                    .addDependency(bindInfo.getParentContextServiceName(), ServiceBasedNamingStore.class,
                            binderService.getNamingStoreInjector())
                    .addDependency(ConnectorServices.RESOURCE_ADAPTER_SERVICE_PREFIX.append(deploymentName))
                    .addListener(new AbstractServiceListener<Object>() {
                         public void transition(final ServiceController<? extends Object> controller, final ServiceController.Transition transition) {
                            switch (transition) {
                                case STARTING_to_UP: {
                                    DEPLOYMENT_CONNECTOR_LOGGER.boundJca("ConnectionFactory", jndi);
                                    break;
                                }
                                case STOPPING_to_DOWN: {
                                    DEPLOYMENT_CONNECTOR_LOGGER.unboundJca("ConnectionFactory", jndi);
                                    break;
                                }
                                case REMOVING_to_REMOVED: {
                                    DEPLOYMENT_CONNECTOR_LOGGER.debugf("Removed JCA ConnectionFactory [%s]", jndi);
                                }
                            }
                        }
                    }).setInitialMode(ServiceController.Mode.ACTIVE).install();

            return new String[] { jndi };
        }

        @Override
        public String[] bindAdminObject(URL url, String deployment, Object ao) throws Throwable {
            throw MESSAGES.jndiBindingsNotSupported();
        }

        @Override
        public String[] bindAdminObject(URL url, String deployment, Object ao, final String jndi) throws Throwable {

            mdr.getValue().registerJndiMapping(url.toExternalForm(), ao.getClass().getName(), jndi);

            DEPLOYMENT_CONNECTOR_LOGGER.registeredAdminObject(jndi);

            final AdminObjectService adminObjectService = new AdminObjectService(ao);

            final ServiceName adminObjectServiceName = AdminObjectService.SERVICE_NAME_BASE.append(jndi);
            serviceTarget.addService(adminObjectServiceName, adminObjectService).setInitialMode(ServiceController.Mode.ACTIVE)
                    .install();

            final AdminObjectReferenceFactoryService referenceFactoryService = new AdminObjectReferenceFactoryService();
            final ServiceName referenceFactoryServiceName = AdminObjectReferenceFactoryService.SERVICE_NAME_BASE.append(jndi);
            serviceTarget.addService(referenceFactoryServiceName, referenceFactoryService)
                    .addDependency(adminObjectServiceName, Object.class, referenceFactoryService.getDataSourceInjector())
                    .setInitialMode(ServiceController.Mode.ACTIVE).install();

            final ContextNames.BindInfo bindInfo = ContextNames.bindInfoFor(jndi);
            final BinderService binderService = new BinderService(bindInfo.getBindName());
            final ServiceName binderServiceName = bindInfo.getBinderServiceName();
            serviceTarget
                    .addService(binderServiceName, binderService)
                    .addDependency(referenceFactoryServiceName, ManagedReferenceFactory.class,
                            binderService.getManagedObjectInjector())
                    .addDependency(bindInfo.getParentContextServiceName(), ServiceBasedNamingStore.class,
                            binderService.getNamingStoreInjector()).addListener(new AbstractServiceListener<Object>() {

                        public void transition(final ServiceController<? extends Object> controller, final ServiceController.Transition transition) {
                            switch (transition) {
                                case STARTING_to_UP: {
                                    DEPLOYMENT_CONNECTOR_LOGGER.boundJca("AdminObject", jndi);
                                    break;
                                }
                                case STOPPING_to_DOWN: {
                                    DEPLOYMENT_CONNECTOR_LOGGER.unboundJca("AdminObject", jndi);
                                    break;
                                }
                                case REMOVING_to_REMOVED: {
                                    DEPLOYMENT_CONNECTOR_LOGGER.debugf("Removed JCA AdminObject [%s]", jndi);
                                }
                            }
                        }
                    }).setInitialMode(ServiceController.Mode.ACTIVE).install();

            return new String[] { jndi };
        }

        @Override
        protected abstract boolean checkActivation(Connector cmd, IronJacamar ijmd);

        @Override
        protected boolean checkConfigurationIsValid() {
            return this.getConfiguration() != null;
        }

        @Override
        protected PrintWriter getLogPrintWriter() {
            return new PrintWriter(System.out);
        }

        @Override
        protected File getReportDirectory() {
            // TODO: evaluate if provide something in config about that. atm
            // returning null and so skip its use
            return null;
        }

        @Override
        protected TransactionManager getTransactionManager() {
            AccessController.doPrivileged(new SetContextLoaderAction(
                    com.arjuna.ats.jbossatx.jta.TransactionManagerService.class.getClassLoader()));
            try {
                return getTxIntegration().getValue().getTransactionManager();
            } finally {
                AccessController.doPrivileged(CLEAR_ACTION);
            }
        }

        @Override
        public Object initAndInject(String className, List<? extends ConfigProperty> configs, ClassLoader cl)
                throws DeployException {
            try {
                Class clz = Class.forName(className, true, cl);
                Object o = clz.newInstance();

                if (configs != null) {
                    Injection injector = new Injection();
                    for (ConfigProperty cpmd : configs) {
                        if (cpmd.isValueSet()) {
                            boolean setValue = true;

                            if (cpmd instanceof org.jboss.jca.common.api.metadata.ra.ra16.ConfigProperty16) {
                                org.jboss.jca.common.api.metadata.ra.ra16.ConfigProperty16 cpmd16 = (org.jboss.jca.common.api.metadata.ra.ra16.ConfigProperty16) cpmd;

                                if (cpmd16.getConfigPropertyIgnore() != null && cpmd16.getConfigPropertyIgnore().booleanValue())
                                    setValue = false;
                            }

                            if (setValue)
                                injector.inject(cpmd.getConfigPropertyType().getValue(), cpmd.getConfigPropertyName()
                                        .getValue(), cpmd.getConfigPropertyValue().getValue(), o);
                        }
                    }
                }

                return o;
            } catch (Throwable t) {
                throw MESSAGES.deploymentFailed(t, className);
            }
        }

        @Override
        protected void registerResourceAdapterToMDR(URL url, File file, Connector connector, IronJacamar ij)
                throws AlreadyExistsException {
            DEPLOYMENT_CONNECTOR_LOGGER.debugf("Registering ResourceAdapter %s", deploymentName);
            mdr.getValue().registerResourceAdapter(deploymentName, file, connector, ij);
        }

        @Override
        protected String registerResourceAdapterToResourceAdapterRepository(ResourceAdapter instance) {
            return raRepository.getValue().registerResourceAdapter(instance);

        }

        @Override
        protected SubjectFactory getSubjectFactory(String securityDomain) throws DeployException {
            if (securityDomain == null || securityDomain.trim().equals("")) {
                return null;
            } else {
                return subjectFactory.getValue();
            }
        }

        @Override
        protected TransactionIntegration getTransactionIntegration() {
            return getTxIntegration().getValue();
        }

        @Override
        protected CachedConnectionManager getCachedConnectionManager() {
            return ccmValue.getValue();
        }

        // Override this method to change how jndiName is build in AS7
        @Override
        protected String buildJndiName(String rawJndiName, Boolean javaContext) {
            final String jndiName;
            if (!rawJndiName.startsWith("java:")) {
                if (rawJndiName.startsWith("jboss/")) {
                    // Bind to java:jboss/ namespace
                    jndiName = "java:" + rawJndiName;
                } else {
                    // Bind to java:/ namespace
                    jndiName= "java:/" + rawJndiName;
                }
            } else {
                jndiName = rawJndiName;
            }
            return jndiName;
        }

    }

    private static final SetContextLoaderAction CLEAR_ACTION = new SetContextLoaderAction(null);

    private static class SetContextLoaderAction implements PrivilegedAction<Void> {

        private final ClassLoader classLoader;

        public SetContextLoaderAction(final ClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        public Void run() {
            Thread.currentThread().setContextClassLoader(classLoader);
            return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8008.java