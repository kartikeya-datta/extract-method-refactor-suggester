error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12026.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12026.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12026.java
text:
```scala
final M@@odelNode model = context.readModel(PathAddress.EMPTY_ADDRESS);

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

package org.jboss.as.connector.subsystems.resourceadapters;

import static org.jboss.as.connector.subsystems.resourceadapters.Constants.ARCHIVE;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.MODULE;
import static org.jboss.as.connector.subsystems.resourceadapters.Constants.RESOURCEADAPTERS_NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;

import java.util.LinkedList;
import java.util.List;

import org.jboss.as.connector.util.ConnectorServices;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;

/**
 * @author @author <a href="mailto:stefano.maestri@redhat.com">Stefano
 *         Maestri</a>
 */
public class RaRemove implements OperationStepHandler {
    static final RaRemove INSTANCE = new RaRemove();

    public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {

        final ModelNode opAddr = operation.require(OP_ADDR);
        final String idName = PathAddress.pathAddress(opAddr).getLastElement().getValue();


        // Compensating is add
        final ModelNode model = context.readResource(PathAddress.EMPTY_ADDRESS, false).getModel();
        final String archiveName;
        final boolean isModule;
        if (model.get(ARCHIVE.getName()).isDefined()) {
            isModule = false;
            archiveName = ARCHIVE.resolveModelAttribute(context, model).asString();
        } else {
            archiveName = null;
            isModule = true;
        }
        final ModelNode compensating = Util.getEmptyOperation(ADD, opAddr);

        if (model.hasDefined(RESOURCEADAPTERS_NAME)) {
            for (ModelNode raNode : model.get(RESOURCEADAPTERS_NAME).asList()) {
                ModelNode raCompensatingNode = raNode.clone();
                compensating.get(RESOURCEADAPTERS_NAME).add(raCompensatingNode);
            }
        }


        context.removeResource(PathAddress.EMPTY_ADDRESS);

        context.addStep(new OperationStepHandler() {
            public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
                final boolean wasActive;
                if (isModule) {
                    wasActive = RaOperationUtil.removeIfActive(context, null, idName);
                } else {
                    wasActive = RaOperationUtil.removeIfActive(context, archiveName, idName);
                    if (wasActive) {
                        context.reloadRequired();
                        context.completeStep(new OperationContext.RollbackHandler() {
                            @Override
                            public void handleRollback(OperationContext context, ModelNode operation) {
                                context.revertReloadRequired();
                            }
                        });
                        return;
                    }
                }
                ServiceName raServiceName = ServiceName.of(ConnectorServices.RA_SERVICE, idName);
                ServiceController<?> serviceController =  context.getServiceRegistry(false).getService(raServiceName);
                final ModifiableResourceAdapter resourceAdapter;
                if (serviceController != null) {
                    resourceAdapter = (ModifiableResourceAdapter) serviceController.getValue();
                } else {
                    resourceAdapter = null;
                }
                final List<ServiceName> serviceNameList = context.getServiceRegistry(false).getServiceNames();
                for (ServiceName name : serviceNameList) {
                    if (raServiceName.isParentOf(name)) {
                        context.removeService(name);
                    }

                }

                if (model.get(MODULE.getName()).isDefined()) {
                    //ServiceName deploymentServiceName = ConnectorServices.getDeploymentServiceName(model.get(MODULE.getName()).asString(),raId);
                    //context.removeService(deploymentServiceName);
                    ServiceName deployerServiceName = ConnectorServices.RESOURCE_ADAPTER_DEPLOYER_SERVICE_PREFIX.append(idName);
                    context.removeService(deployerServiceName);
                    ServiceName inactiveServiceName = ConnectorServices.INACTIVE_RESOURCE_ADAPTER_SERVICE.append(idName);
                    context.removeService(inactiveServiceName);
                }


                context.removeService(raServiceName);
                context.completeStep(new OperationContext.RollbackHandler() {
                    @Override
                    public void handleRollback(OperationContext context, ModelNode operation) {
                        if (resourceAdapter != null) {
                            List<ServiceController<?>>  newControllers = new LinkedList<ServiceController<?>>();
                            if (model.get(ARCHIVE.getName()).isDefined()) {
                                RaOperationUtil.installRaServices(context, new ServiceVerificationHandler(), idName, resourceAdapter, newControllers);
                            } else {
                                try {
                                    RaOperationUtil.installRaServicesAndDeployFromModule(context, new ServiceVerificationHandler(), idName, resourceAdapter, archiveName, newControllers);
                                } catch (OperationFailedException e) {

                                }
                            }
                            try {
                                if (wasActive){
                                    if(isModule){
                                        RaOperationUtil.activate(context, idName, null);
                                    } else {
                                        RaOperationUtil.activate(context, archiveName, null);
                                    }
                                }
                            } catch (OperationFailedException e) {

                            }
                        }

                    }
                });
            }
        }, OperationContext.Stage.RUNTIME);
        context.stepCompleted();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12026.java