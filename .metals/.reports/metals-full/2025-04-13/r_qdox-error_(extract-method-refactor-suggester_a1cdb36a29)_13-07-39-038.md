error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13829.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13829.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13829.java
text:
```scala
final D@@escriptorBasedEJBClientContextService service = new DescriptorBasedEJBClientContextService(ejbClientConfiguration, module.getClassLoader());

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

package org.jboss.as.ejb3.deployment.processors;

import org.jboss.as.ee.metadata.EJBClientDescriptorMetaData;
import org.jboss.as.ee.structure.Attachments;
import org.jboss.as.ejb3.EjbLogger;
import org.jboss.as.ejb3.EjbMessages;
import org.jboss.as.ejb3.deployment.EjbDeploymentAttachmentKeys;
import org.jboss.as.ejb3.remote.DescriptorBasedEJBClientContextService;
import org.jboss.as.ejb3.remote.EJBClientClusterConfig;
import org.jboss.as.ejb3.remote.EJBClientClusterNodeConfig;
import org.jboss.as.ejb3.remote.JBossEJBClientXmlConfiguration;
import org.jboss.as.ejb3.remote.LocalEjbReceiver;
import org.jboss.as.ejb3.remote.TCCLEJBClientContextSelectorService;
import org.jboss.as.remoting.AbstractOutboundConnectionService;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.ejb.client.DeploymentNodeSelector;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.logging.Logger;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.msc.service.ServiceTarget;
import org.xnio.Option;
import org.xnio.OptionMap;

import java.util.Properties;

/**
 * A deployment unit processor which processing only top level deployment units and checks for the presence
 * of a {@link Attachments#EJB_CLIENT_METADATA} key corresponding to {@link EJBClientDescriptorMetaData}, in the
 * deployment unit.
 * <p/>
 * If a {@link EJBClientDescriptorMetaData} is available then this deployment unit processor
 * creates and installs a {@link DescriptorBasedEJBClientContextService}. It then attaches the {@link org.jboss.ejb.client.EJBClientContext}
 * as an attachment to the deployment unit, under the key {@link EjbDeploymentAttachmentKeys#EJB_CLIENT_CONTEXT}
 *
 * @author Jaikiran Pai
 */
public class EJBClientDescriptorMetaDataProcessor implements DeploymentUnitProcessor {

    private static final Logger logger = Logger.getLogger(EJBClientDescriptorMetaDataProcessor.class);

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        // we only process top level deployment units
        if (deploymentUnit.getParent() != null) {
            return;
        }
        final EJBClientDescriptorMetaData ejbClientDescriptorMetaData = deploymentUnit.getAttachment(Attachments.EJB_CLIENT_METADATA);
        // no explicit EJB client configuration in this deployment, so nothing to do
        if (ejbClientDescriptorMetaData == null) {
            return;
        }
        final Module module = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.MODULE);
        if (module == null) {
            return;
        }
        // install the descriptor based EJB client context service
        final ServiceName ejbClientContextServiceName = DescriptorBasedEJBClientContextService.BASE_SERVICE_NAME.append(deploymentUnit.getName());
        final ServiceTarget serviceTarget = phaseContext.getServiceTarget();
        // create the client configuration from the metadata
        final EJBClientConfiguration ejbClientConfiguration = this.createClientConfiguration(phaseContext.getServiceRegistry(), module.getClassLoader(), ejbClientDescriptorMetaData);
        // create the service
        final DescriptorBasedEJBClientContextService service = new DescriptorBasedEJBClientContextService(ejbClientConfiguration);
        // add the service
        final ServiceBuilder serviceBuilder = serviceTarget.addService(ejbClientContextServiceName, service);
        // add the remoting connection reference dependencies
        for (final EJBClientDescriptorMetaData.RemotingReceiverConfiguration remotingReceiverConfiguration : ejbClientDescriptorMetaData.getRemotingReceiverConfigurations()) {
            final String connectionRef = remotingReceiverConfiguration.getOutboundConnectionRef();
            final ServiceName connectionDependencyService = AbstractOutboundConnectionService.OUTBOUND_CONNECTION_BASE_SERVICE_NAME.append(connectionRef);
            service.addRemotingConnectionDependency(serviceBuilder, connectionDependencyService);

            // setup the channel creation options for each outbound connection reference
            final Properties channelCreationProps = remotingReceiverConfiguration.getChannelCreationOptions();
            final OptionMap channelCreationOpts;
            if (channelCreationProps == null) {
                channelCreationOpts = OptionMap.EMPTY;
            } else {
                // we don't use the deployment CL here since the XNIO project isn't necessarily added as a dep on the deployment's
                // module CL
                channelCreationOpts = this.getOptionMapFromProperties(channelCreationProps, this.getClass().getClassLoader());
            }
            logger.debug("Channel creation options for connection " + connectionRef + " are " + channelCreationOpts);
            service.setChannelCreationOptions(connectionRef, channelCreationOpts);

            // setup connection timeout for each outbound connection ref
            service.setConnectionCreationTimeout(connectionRef, remotingReceiverConfiguration.getConnectionTimeout());
        }
        // if the local receiver is enabled for this context, then add a dependency on the appropriate LocalEjbReceiver
        // service
        if (!ejbClientDescriptorMetaData.isLocalReceiverExcluded()) {
            final Boolean passByValue = ejbClientDescriptorMetaData.isLocalReceiverPassByValue();
            if (passByValue != null) {
                final ServiceName localEjbReceiverServiceName = passByValue == true ? LocalEjbReceiver.BY_VALUE_SERVICE_NAME : LocalEjbReceiver.BY_REFERENCE_SERVICE_NAME;
                serviceBuilder.addDependency(localEjbReceiverServiceName, LocalEjbReceiver.class, service.getLocalEjbReceiverInjector());
            } else {
                // setup a dependency on the default local ejb receiver service configured at the subsystem level
                serviceBuilder.addDependency(LocalEjbReceiver.DEFAULT_LOCAL_EJB_RECEIVER_SERVICE_NAME, LocalEjbReceiver.class, service.getLocalEjbReceiverInjector());
            }
        }
        serviceBuilder.addDependency(TCCLEJBClientContextSelectorService.TCCL_BASED_EJB_CLIENT_CONTEXT_SELECTOR_SERVICE_NAME);
        // install the service
        serviceBuilder.install();
        logger.debug("Deployment unit " + deploymentUnit + " will use " + ejbClientContextServiceName + " as the EJB client context service");

        // attach the service name of this EJB client context to the deployment unit
        phaseContext.addDeploymentDependency(ejbClientContextServiceName, EjbDeploymentAttachmentKeys.EJB_CLIENT_CONTEXT);
    }

    @Override
    public void undeploy(DeploymentUnit context) {

    }

    private EJBClientConfiguration createClientConfiguration(final ServiceRegistry serviceRegistry, final ClassLoader classLoader, final EJBClientDescriptorMetaData ejbClientDescriptorMetaData) throws DeploymentUnitProcessingException {

        final JBossEJBClientXmlConfiguration ejbClientConfig = new JBossEJBClientXmlConfiguration();
        ejbClientConfig.setInvocationTimeout(ejbClientDescriptorMetaData.getInvocationTimeout());
        // deployment node selector
        final String deploymentNodeSelectorClassName = ejbClientDescriptorMetaData.getDeploymentNodeSelector();
        if (deploymentNodeSelectorClassName != null && !deploymentNodeSelectorClassName.trim().isEmpty()) {
            try {
                final Class<?> deploymentNodeSelectorClass = classLoader.loadClass(deploymentNodeSelectorClassName);
                ejbClientConfig.setDeploymentNodeSelector((DeploymentNodeSelector) deploymentNodeSelectorClass.newInstance());
            } catch (Exception e) {
                throw EjbMessages.MESSAGES.failedToCreateDeploymentNodeSelector(e, deploymentNodeSelectorClassName);
            }
        }

        for (final EJBClientDescriptorMetaData.ClusterConfig clusterMetadata : ejbClientDescriptorMetaData.getClusterConfigs()) {
            final EJBClientClusterConfig clusterConfig = new EJBClientClusterConfig(clusterMetadata, classLoader, serviceRegistry);
            // add it to the client configuration
            ejbClientConfig.addClusterConfiguration(clusterConfig);

            for (final EJBClientDescriptorMetaData.ClusterNodeConfig nodeMetadata : clusterMetadata.getClusterNodeConfigs()) {
                final EJBClientClusterNodeConfig clusterNodeConfig = new EJBClientClusterNodeConfig(nodeMetadata, classLoader, serviceRegistry);
                clusterConfig.addClusterNode(clusterNodeConfig);
            }

        }
        return ejbClientConfig;
    }


    private static OptionMap getOptionMapFromProperties(final Properties properties, final ClassLoader classLoader) {
        final OptionMap.Builder optionMapBuilder = OptionMap.builder();
        for (final String propertyName : properties.stringPropertyNames()) {
            try {
                final Option<?> option = Option.fromString(propertyName, classLoader);
                optionMapBuilder.parse(option, properties.getProperty(propertyName), classLoader);
            } catch (IllegalArgumentException e) {
                EjbLogger.EJB3_LOGGER.failedToCreateOptionForProperty(propertyName, e.getMessage());
            }
        }
        return optionMapBuilder.getMap();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13829.java