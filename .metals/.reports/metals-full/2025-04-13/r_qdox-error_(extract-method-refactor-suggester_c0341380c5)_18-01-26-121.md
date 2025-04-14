error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10922.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10922.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10922.java
text:
```scala
a@@ddOperation.get(CommonAttributes.CONNECTION_CREATION_OPTIONS).add(entry.getKey(), entry.getValue());

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

package org.jboss.as.remoting;

import java.util.List;
import java.util.Map;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.network.OutboundSocketBinding;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.remoting3.Endpoint;
import org.xnio.OptionMap;

/**
 * @author Jaikiran Pai
 */
class LocalOutboundConnectionAdd extends AbstractOutboundConnectionAddHandler {

    static final LocalOutboundConnectionAdd INSTANCE = new LocalOutboundConnectionAdd();

    static ModelNode getAddOperation(final String connectionName, final String outboundSocketBindingRef, final Map<String, String> connectionCreationOptions) {
        if (connectionName == null || connectionName.trim().isEmpty()) {
            throw new IllegalArgumentException("Connection name cannot be null or empty");
        }
        if (outboundSocketBindingRef == null || outboundSocketBindingRef.trim().isEmpty()) {
            throw new IllegalArgumentException("Outbound socket binding reference cannot be null or empty for connection named " + connectionName);
        }
        final ModelNode addOperation = new ModelNode();
        addOperation.get(ModelDescriptionConstants.OP).set(ModelDescriptionConstants.ADD);
        // /subsystem=remoting/local-outbound-connection=<connection-name>
        final PathAddress address = PathAddress.pathAddress(PathElement.pathElement(ModelDescriptionConstants.SUBSYSTEM, RemotingExtension.SUBSYSTEM_NAME),
                PathElement.pathElement(CommonAttributes.LOCAL_OUTBOUND_CONNECTION, connectionName));
        addOperation.get(ModelDescriptionConstants.OP_ADDR).set(address.toModelNode());

        // set the other params
        addOperation.get(CommonAttributes.OUTBOUND_SOCKET_BINDING_REF).set(outboundSocketBindingRef);
        // optional connection creation options
        if (connectionCreationOptions != null) {
            for (final Map.Entry<String, String> entry : connectionCreationOptions.entrySet()) {
                if (entry.getKey() == null) {
                    // skip
                    continue;
                }
                addOperation.get(CommonAttributes.CONNECTION_CREATION_OPTIONS).set(entry.getKey(), entry.getValue());
            }
        }

        return addOperation;
    }

    private LocalOutboundConnectionAdd() {

    }

    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        super.populateModel(operation, model);

        LocalOutboundConnectionResourceDefinition.OUTBOUND_SOCKET_BINDING_REF.validateAndSet(operation, model);
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {
        final String name = PathAddress.pathAddress(operation.require(ModelDescriptionConstants.OP_ADDR)).getLastElement().getValue();
        final ServiceController serviceController = installRuntimeService(context, name, model, verificationHandler);
        newControllers.add(serviceController);
    }

    ServiceController installRuntimeService(OperationContext context, String connectionName, ModelNode outboundConnection,
                                            ServiceVerificationHandler verificationHandler) throws OperationFailedException {

        final String outboundSocketBindingRef = LocalOutboundConnectionResourceDefinition.OUTBOUND_SOCKET_BINDING_REF.resolveModelAttribute(context, outboundConnection).asString();
        final ServiceName outboundSocketBindingDependency = OutboundSocketBinding.OUTBOUND_SOCKET_BINDING_BASE_SERVICE_NAME.append(outboundSocketBindingRef);
        // fetch the connection creation options from the model
        final OptionMap connectionCreationOptions = getConnectionCreationOptions(outboundConnection);
        // create the service
        final LocalOutboundConnectionService outboundConnectionService = new LocalOutboundConnectionService(connectionName, connectionCreationOptions);
        final ServiceName serviceName = AbstractOutboundConnectionService.OUTBOUND_CONNECTION_BASE_SERVICE_NAME.append(connectionName);
        // also add a alias service name to easily distinguish between a generic, remote and local type of connection services
        final ServiceName aliasServiceName = LocalOutboundConnectionService.LOCAL_OUTBOUND_CONNECTION_BASE_SERVICE_NAME.append(connectionName);
        final ServiceBuilder<LocalOutboundConnectionService> svcBuilder = context.getServiceTarget().addService(serviceName, outboundConnectionService)
                .addAliases(aliasServiceName)
                .addDependency(RemotingServices.SUBSYSTEM_ENDPOINT, Endpoint.class, outboundConnectionService.getEnpointInjector())
                .addDependency(outboundSocketBindingDependency, OutboundSocketBinding.class, outboundConnectionService.getDestinationOutboundSocketBindingInjector());

        if (verificationHandler != null) {
            svcBuilder.addListener(verificationHandler);
        }
        return svcBuilder.install();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10922.java