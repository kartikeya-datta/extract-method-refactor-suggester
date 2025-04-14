error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10131.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10131.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10131.java
text:
```scala
c@@leanupStartAsync(context, raName, deploymentServiceName, t);

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

package org.jboss.as.connector.services.resourceadapters.deployment;

import java.io.File;
import java.net.URL;

import org.jboss.as.connector.metadata.deployment.ResourceAdapterDeployment;
import org.jboss.as.connector.metadata.xmldescriptors.ConnectorXmlDescriptor;
import org.jboss.as.connector.services.resourceadapters.ResourceAdapterService;
import org.jboss.as.connector.subsystems.resourceadapters.ModifiableResourceAdapter;
import org.jboss.as.connector.util.ConnectorServices;
import org.jboss.as.naming.WritableServiceBasedNamingStore;
import org.jboss.jca.common.api.metadata.ironjacamar.IronJacamar;
import org.jboss.jca.common.api.metadata.ra.Connector;
import org.jboss.jca.common.api.metadata.resourceadapter.ResourceAdapter;
import org.jboss.jca.common.metadata.merge.Merger;
import org.jboss.jca.deployers.DeployersLogger;
import org.jboss.jca.deployers.common.CommonDeployment;
import org.jboss.logging.Logger;
import org.jboss.modules.Module;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.wildfly.security.manager.WildFlySecurityManager;

/**
 * A ResourceAdapterXmlDeploymentService.
 * @author <a href="mailto:stefano.maestri@redhat.com">Stefano Maestri</a>
 * @author <a href="mailto:jesper.pedersen@jboss.org">Jesper Pedersen</a>
 */
public final class ResourceAdapterXmlDeploymentService extends AbstractResourceAdapterDeploymentService implements
        Service<ResourceAdapterDeployment> {

    private static final DeployersLogger DEPLOYERS_LOGGER = Logger.getMessageLogger(DeployersLogger.class, "org.jboss.as.connector.deployers.RaXmlDeployer");

    private final Module module;
    private final ConnectorXmlDescriptor connectorXmlDescriptor;

    private ResourceAdapter raxml;
    private final String deployment;

    private String raName;
    private ServiceName deploymentServiceName;
    private CommonDeployment raxmlDeployment = null;
    private final ServiceName duServiceName;

    public ResourceAdapterXmlDeploymentService(ConnectorXmlDescriptor connectorXmlDescriptor, ResourceAdapter raxml,
                                               Module module, final String deployment, final ServiceName deploymentServiceName, final ServiceName duServiceName) {
        this.connectorXmlDescriptor = connectorXmlDescriptor;
        synchronized (this) {
            this.raxml = raxml;
        }
        this.module = module;
        this.deployment = deployment;
        this.raName = deployment;
        this.deploymentServiceName = deploymentServiceName;
        this.duServiceName = duServiceName;
    }

    /**
     * Start
     */
    @Override
    public void start(StartContext context) throws StartException {
        try {
            Connector cmd = mdr.getValue().getResourceAdapter(deployment);
            File root = mdr.getValue().getRoot(deployment);

            ResourceAdapter localRaXml = getRaxml();
            cmd = (new Merger()).mergeConnectorWithCommonIronJacamar(localRaXml, cmd);

            final AS7RaXmlDeployer raDeployer = new AS7RaXmlDeployer(context.getChildTarget(), connectorXmlDescriptor.getUrl(),
                raName, root, module.getClassLoader(), cmd, localRaXml, null, deploymentServiceName);

            raDeployer.setConfiguration(config.getValue());

            try {
                WritableServiceBasedNamingStore.pushOwner(duServiceName);
                ClassLoader old = WildFlySecurityManager.getCurrentContextClassLoaderPrivileged();
                try {
                    WildFlySecurityManager.setCurrentContextClassLoaderPrivileged(module.getClassLoader());
                    raxmlDeployment = raDeployer.doDeploy();
                } finally {
                    WildFlySecurityManager.setCurrentContextClassLoaderPrivileged(old);
                    WritableServiceBasedNamingStore.popOwner();
                }
            } catch (Throwable t) {
                // To clean up we need to invoke blocking behavior, so do that in another thread
                // and let this MSC thread return
                cleanupStartAsync(context, raName, t);
                return;
            }
            String id = ((ModifiableResourceAdapter) raxml).getId();
            final ServiceName raServiceName;
            if (id == null || id.trim().isEmpty()) {
               raServiceName = ConnectorServices.getResourceAdapterServiceName(raName);
            } else {
               raServiceName = ConnectorServices.getResourceAdapterServiceName(id);
            }

            value = new ResourceAdapterDeployment(raxmlDeployment, raName, raServiceName);
            managementRepository.getValue().getConnectors().add(value.getDeployment().getConnector());

            registry.getValue().registerResourceAdapterDeployment(value);

            context.getChildTarget()
                    .addService(raServiceName,
                            new ResourceAdapterService(raName, raServiceName, value.getDeployment().getResourceAdapter()))
                    .addDependency(deploymentServiceName)
                    .setInitialMode(ServiceController.Mode.ACTIVE).install();
        } catch (Exception e) {
            throw new StartException(e);
        }
    }

    /**
     * Stop
     */
    @Override
    public void stop(StopContext context) {
        stopAsync(context, raName, deploymentServiceName);
    }

    @Override
    public void unregisterAll(String deploymentName) {

        if (raName != null) {
            ConnectorServices.unregisterResourceAdapterIdentifier(raName);
        }

        super.unregisterAll(deploymentName);
    }

    public CommonDeployment getRaxmlDeployment() {
        return raxmlDeployment;
    }

    public synchronized void setRaxml(ResourceAdapter raxml) {
            this.raxml = raxml;
    }

    public synchronized ResourceAdapter getRaxml() {
            return raxml;
    }


    private class AS7RaXmlDeployer extends AbstractAS7RaDeployer {

        private final ResourceAdapter ra;
        private final IronJacamar ijmd;


        public AS7RaXmlDeployer(ServiceTarget serviceTarget, URL url, String deploymentName, File root, ClassLoader cl,
                Connector cmd, ResourceAdapter ra, IronJacamar ijmd,  final ServiceName deploymentServiceName) {
            super(serviceTarget, url, deploymentName, root, cl, cmd, deploymentServiceName);
            this.ra = ra;
            this.ijmd = ijmd;
        }

        @Override
        public CommonDeployment doDeploy() throws Throwable {

            this.setConfiguration(getConfig().getValue());

            this.start();

            CommonDeployment dep = this.createObjectsAndInjectValue(url, deploymentName, root, cl, cmd, ijmd, ra);

            return dep;
        }

        @Override
        protected boolean checkActivation(Connector cmd, IronJacamar ijmd) {
            return true;
        }

        @Override
        protected DeployersLogger getLogger() {
            return DEPLOYERS_LOGGER;
        }

        @Override
        protected String registerResourceAdapterToResourceAdapterRepository(javax.resource.spi.ResourceAdapter instance) {
            final String raIdentifier = raRepository.getValue().registerResourceAdapter(instance);
            // make a note of this identifier for future use
            ConnectorServices.registerResourceAdapterIdentifier( ((ModifiableResourceAdapter) raxml).getId(), raIdentifier);
            return raIdentifier;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10131.java