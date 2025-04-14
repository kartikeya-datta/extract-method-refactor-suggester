error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1630.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1630.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1630.java
text:
```scala
.@@addDependency(TxnServices.JBOSS_TXN_ARJUNA_TRANSACTION_MANAGER, com.arjuna.ats.jbossatx.jta.TransactionManagerService.class, dataSourceDeploymentService.getTxmInjector())

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

package org.jboss.as.connector.deployers.processors;

import static org.jboss.as.connector.deployers.processors.DataSourcesAttachement.getDataSourcesAttachment;

import java.util.List;

import org.jboss.as.connector.ConnectorServices;
import org.jboss.as.connector.metadata.xmldescriptors.ConnectorXmlDescriptor;
import org.jboss.as.connector.registry.ResourceAdapterDeploymentRegistry;
import org.jboss.as.connector.subsystems.datasources.DataSourceDeploymentService;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.naming.service.NamingService;
import org.jboss.as.txn.TxnServices;
import org.jboss.jca.common.api.metadata.ds.DataSource;
import org.jboss.jca.common.api.metadata.ds.DataSources;
import org.jboss.jca.common.api.metadata.ds.XaDataSource;
import org.jboss.jca.core.spi.mdr.MetadataRepository;
import org.jboss.jca.core.spi.naming.JndiStrategy;
import org.jboss.logging.Logger;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;

/**
 * DeploymentUnitProcessor responsible for using IronJacamar metadata and create
 * service for ResourceAdapter.
 *
 * @author <a href="mailto:stefano.maestri@redhat.com">Stefano Maestri</a>
 * @author <a href="jesper.pedersen@jboss.org">Jesper Pedersen</a>
 */
public class DsDeploymentProcessor implements DeploymentUnitProcessor {

    public static final Logger log = Logger.getLogger("org.jboss.as.connector.deployer.dsdeployer");

    public DsDeploymentProcessor() {
    }

    /**
     * Deploy datasources
     *
     * @param phaseContext the deployment unit context
     * @throws DeploymentUnitProcessingException
     *
     */
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final ConnectorXmlDescriptor connectorXmlDescriptor = deploymentUnit.getAttachment(ConnectorXmlDescriptor.ATTACHMENT_KEY);

        Module module = phaseContext.getAttachment(Attachments.MODULE);

        String deploymentName = connectorXmlDescriptor == null ? null : connectorXmlDescriptor.getDeploymentName();

        DataSources datasources = getDataSourcesAttachment(deploymentUnit);
        if (datasources == null || deploymentName == null || !deploymentName.startsWith("jdbc"))
            return;

        log.tracef("Processing datasource deployement: %s", datasources);

        if (module == null)
            throw new DeploymentUnitProcessingException("Failed to get module attachment for " + deploymentUnit);


        String uniqueJdbcLocalId = null;
        String uniqueJdbcXAId = null;
        boolean shouldDeploy = false;

        if (deploymentName.indexOf("local") != -1) {
            // Local datasources
            List<DataSource> dss = datasources.getDataSource();
            if (dss != null && dss.size() > 0) {
                uniqueJdbcLocalId = deploymentName;
                shouldDeploy = true;
            }
        } else {
            // XA datasources
            List<XaDataSource> xadss = datasources.getXaDataSource();
            if (xadss != null && xadss.size() > 0) {
                uniqueJdbcXAId = deploymentName;
                shouldDeploy = true;
            }
        }

        if(shouldDeploy) {
            final ServiceTarget serviceTarget = phaseContext.getServiceTarget();

            final DataSourceDeploymentService dataSourceDeploymentService = new DataSourceDeploymentService(deploymentName, uniqueJdbcLocalId, uniqueJdbcXAId, datasources, module);
            ServiceBuilder<?> serviceBuilder = serviceTarget.addService(DataSourceDeploymentService.SERVICE_NAME_BASE.append(deploymentName), dataSourceDeploymentService)
                 .addDependency(ConnectorServices.IRONJACAMAR_MDR, MetadataRepository.class, dataSourceDeploymentService.getMdrInjector())
                    .addDependency(ConnectorServices.RESOURCE_ADAPTER_REGISTRY_SERVICE, ResourceAdapterDeploymentRegistry.class, dataSourceDeploymentService.getRegistryInjector())
                    .addDependency(ConnectorServices.JNDI_STRATEGY_SERVICE, JndiStrategy.class, dataSourceDeploymentService.getJndiInjector())
                    .addDependency(TxnServices.JBOSS_TXN_TRANSACTION_MANAGER, com.arjuna.ats.jbossatx.jta.TransactionManagerService.class, dataSourceDeploymentService.getTxmInjector())
                    .addDependency(NamingService.SERVICE_NAME)
                    .setInitialMode(ServiceController.Mode.ACTIVE);
            if(uniqueJdbcLocalId != null) {
                serviceBuilder.addDependency(ServiceBuilder.DependencyType.OPTIONAL, ConnectorServices.RESOURCE_ADAPTER_SERVICE_PREFIX.append(uniqueJdbcLocalId));
                serviceBuilder.addDependency(ServiceBuilder.DependencyType.OPTIONAL, ConnectorServices.RESOURCE_ADAPTER_XML_SERVICE_PREFIX.append(uniqueJdbcLocalId));
            } else if(uniqueJdbcXAId != null) {
                serviceBuilder.addDependency(ServiceBuilder.DependencyType.OPTIONAL, ConnectorServices.RESOURCE_ADAPTER_SERVICE_PREFIX.append(uniqueJdbcXAId));
                serviceBuilder.addDependency(ServiceBuilder.DependencyType.OPTIONAL, ConnectorServices.RESOURCE_ADAPTER_XML_SERVICE_PREFIX.append(uniqueJdbcXAId));
            }
            serviceBuilder.install();
        }
    }

    public void undeploy(DeploymentUnit context) {
        final ConnectorXmlDescriptor connectorXmlDescriptor = context.getAttachment(ConnectorXmlDescriptor.ATTACHMENT_KEY);
        if(connectorXmlDescriptor == null) {
            return;
        }
        final String deploymentName = connectorXmlDescriptor == null ? null : connectorXmlDescriptor.getDeploymentName();
        final ServiceName serviceName = DataSourceDeploymentService.SERVICE_NAME_BASE.append(deploymentName);
        final ServiceController<?> serviceController = context.getServiceRegistry().getService(serviceName);
        if(serviceController != null) {
            serviceController.setMode(ServiceController.Mode.REMOVE);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1630.java