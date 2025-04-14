error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14790.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14790.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14790.java
text:
```scala
i@@f (bundle == null || bundle.isFragment())

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

package org.jboss.as.osgi.deployment;

import static org.jboss.as.ee.component.Attachments.EE_MODULE_DESCRIPTION;
import static org.jboss.as.osgi.OSGiLogger.LOGGER;
import static org.jboss.as.osgi.OSGiMessages.MESSAGES;

import org.jboss.as.ee.component.Component;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.ComponentInstance;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.osgi.OSGiConstants;
import org.jboss.as.osgi.management.OperationAssociation;
import org.jboss.as.server.deployment.AttachmentKey;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.Attachments.BundleState;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.osgi.deployment.deployer.Deployment;
import org.jboss.osgi.framework.BundleManager;
import org.jboss.osgi.metadata.OSGiMetaData;
import org.jboss.osgi.resolver.XBundle;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleException;

/**
 * Attempt to activate the OSGi deployment.
 *
 * @author Thomas.Diesler@jboss.com
 * @since 20-Jun-2012
 */
public class BundleActivateProcessor implements DeploymentUnitProcessor {

    @Override
    public void deploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        DeploymentUnit depUnit = phaseContext.getDeploymentUnit();
        XBundle bundle = depUnit.getAttachment(OSGiConstants.BUNDLE_KEY);
        if (bundle == null || bundle.isFragment() || !bundle.isResolved())
            return;

        ServiceController<XBundle> controller = BundleActivateService.addService(phaseContext.getServiceTarget(), depUnit, bundle);
        phaseContext.addDependency(controller.getName(), AttachmentKey.create(XBundle.class));
    }

    @Override
    public void undeploy(final DeploymentUnit depUnit) {
    }

    static class BundleActivateService implements Service<XBundle> {

        private final InjectedValue<XBundle> injectedBundle = new InjectedValue<XBundle>();
        private final InjectedValue<Component> injectedComponent = new InjectedValue<Component>();
        private final DeploymentUnit depUnit;

        static ServiceController<XBundle> addService(ServiceTarget serviceTarget, DeploymentUnit depUnit, XBundle bundle) {
            BundleManager bundleManager = depUnit.getAttachment(OSGiConstants.BUNDLE_MANAGER_KEY);
            ServiceName resolvedBundle = bundleManager.getServiceName(bundle, Bundle.RESOLVED);
            ServiceName serviceName = depUnit.getServiceName().append("Activate");
            BundleActivateService service = new BundleActivateService(depUnit);
            ServiceBuilder<XBundle> builder = serviceTarget.addService(serviceName, service);
            builder.addDependency(resolvedBundle, XBundle.class, service.injectedBundle);
            // Add a dependency on the BundleActivator component
            OSGiMetaData metadata = depUnit.getAttachment(OSGiConstants.OSGI_METADATA_KEY);
            if (metadata != null && metadata.getBundleActivator() != null) {
                String activatorClass = metadata.getBundleActivator();
                EEModuleDescription moduleDescription = depUnit.getAttachment(EE_MODULE_DESCRIPTION);
                for (ComponentDescription componentDescription : moduleDescription.getComponentDescriptions()) {
                    if (activatorClass.equals(componentDescription.getComponentClassName())) {
                        ServiceName startServiceName = componentDescription.getStartServiceName();
                        builder.addDependency(startServiceName, Component.class, service.injectedComponent);
                    }
                }
            }
            return builder.install();
        }

        private BundleActivateService(DeploymentUnit depUnit) {
            this.depUnit = depUnit;
        }

        @Override
        public void start(StartContext context) throws StartException {
            XBundle bundle = injectedBundle.getValue();
            Deployment deployment = depUnit.getAttachment(OSGiConstants.DEPLOYMENT_KEY);
            Component activatorComponent = injectedComponent.getOptionalValue();
            if (activatorComponent != null && deployment.getAttachment(BundleActivator.class) == null) {
                ComponentInstance componentInstance = activatorComponent.createInstance();
                BundleActivator instance = (BundleActivator) componentInstance.getInstance();
                deployment.addAttachment(BundleActivator.class, instance);
            }
            OperationAssociation.INSTANCE.setAssociation(new ModelNode("deploy"));
            try {
                bundle.start(Bundle.START_ACTIVATION_POLICY);
                depUnit.putAttachment(Attachments.BUNDLE_STATE_KEY, BundleState.ACTIVE);
            } catch (BundleException ex) {
                throw MESSAGES.cannotStartBundle(ex, bundle);
            } finally {
                OperationAssociation.INSTANCE.removeAssociation();
            }
        }

        @Override
        public void stop(StopContext context) {
            XBundle bundle = injectedBundle.getValue();
            try {
                // Server shutdown should not modify the persistent start setting
                bundle.stop(Bundle.STOP_TRANSIENT);
                depUnit.putAttachment(Attachments.BUNDLE_STATE_KEY, BundleState.RESOLVED);
            } catch (BundleException ex) {
                LOGGER.debugf(ex, "Cannot stop bundle: %s", bundle);
            }
        }

        @Override
        public XBundle getValue() {
            return injectedBundle.getValue();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14790.java