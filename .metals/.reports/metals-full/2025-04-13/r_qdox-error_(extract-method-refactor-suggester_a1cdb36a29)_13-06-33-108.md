error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/784.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/784.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/784.java
text:
```scala
t@@hrow ControllerMessages.MESSAGES.nullNotAllowed(DESTINATION_ADDRESS);

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.jboss.as.server.services.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.jboss.as.controller.ControllerMessages;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CLIENT_MAPPINGS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESTINATION_ADDRESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESTINATION_PORT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SOURCE_NETWORK;

import org.jboss.as.controller.operations.common.SocketBindingAddHandler;
import org.jboss.as.controller.operations.validation.MaskedAddressValidator;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.resource.AbstractSocketBindingResourceDefinition;
import org.jboss.as.controller.resource.SocketBindingGroupResourceDefinition;
import org.jboss.as.network.ClientMapping;
import org.jboss.as.network.NetworkInterfaceBinding;
import org.jboss.as.network.SocketBinding;
import org.jboss.as.network.SocketBindingManager;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceTarget;

/**
 * Handler for the server socket-binding resource's add operation.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class BindingAddHandler extends SocketBindingAddHandler {

    public static final BindingAddHandler INSTANCE = new BindingAddHandler();
    private static final InetAddress ANY_IPV6;

    static {
        try {
            ANY_IPV6 = InetAddress.getByAddress(new byte[16]);
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Not possible");
        }
    }

    private BindingAddHandler() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void populateModel(final OperationContext context, final ModelNode operation, final Resource resource) throws OperationFailedException {

        ModelNode model = resource.getModel();
        populateModel(operation, model);

        SocketBindingResourceDefinition.validateInterfaceReference(context, model);
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {

        PathAddress address = PathAddress.pathAddress(operation.get(OP_ADDR));
        String name = address.getLastElement().getValue();

        try {
            newControllers.add(installBindingService(context, model, name));
        } catch (UnknownHostException e) {
            throw new OperationFailedException(new ModelNode().set(e.getLocalizedMessage()));
        }

    }

    @Override
    protected boolean requiresRuntime(OperationContext context) {
        return true;
    }

    @Override
    protected boolean requiresRuntimeVerification() {
        return false;
    }

    public static ServiceController<SocketBinding> installBindingService(OperationContext context, ModelNode config, String name)
            throws UnknownHostException, OperationFailedException {
        final ServiceTarget serviceTarget = context.getServiceTarget();

        final ModelNode intfNode = AbstractSocketBindingResourceDefinition.INTERFACE.resolveModelAttribute(context, config);
        final String intf = intfNode.isDefined() ? intfNode.asString() : null;
        final int port = AbstractSocketBindingResourceDefinition.PORT.resolveModelAttribute(context, config).asInt(0);
        final boolean fixedPort = AbstractSocketBindingResourceDefinition.FIXED_PORT.resolveModelAttribute(context, config).asBoolean();
        final ModelNode mcastNode = AbstractSocketBindingResourceDefinition.MULTICAST_ADDRESS.resolveModelAttribute(context, config);
        final String mcastAddr = mcastNode.isDefined() ? mcastNode.asString() : null;
        final int mcastPort = AbstractSocketBindingResourceDefinition.MULTICAST_PORT.resolveModelAttribute(context, config).asInt(0);
        final InetAddress mcastInet = mcastAddr == null ? null : InetAddress.getByName(mcastAddr);
        final ModelNode mappingsNode = config.get(CLIENT_MAPPINGS);
        final List<ClientMapping> clientMappings = mappingsNode.isDefined() ? parseClientMappings(mappingsNode) : null;

        final SocketBindingService service = new SocketBindingService(name, port, fixedPort, mcastInet, mcastPort, clientMappings);
        final ServiceBuilder<SocketBinding> builder = serviceTarget.addService(SocketBinding.JBOSS_BINDING_NAME.append(name), service);
        if (intf != null) {
            builder.addDependency(NetworkInterfaceService.JBOSS_NETWORK_INTERFACE.append(intf), NetworkInterfaceBinding.class, service.getInterfaceBinding());
        }
        return builder.addDependency(SocketBindingManager.SOCKET_BINDING_MANAGER, SocketBindingManager.class, service.getSocketBindings())
                .setInitialMode(Mode.ON_DEMAND)
                .install();
    }

    public static List<ClientMapping> parseClientMappings(ModelNode mappings) throws OperationFailedException {
        List<ClientMapping> clientMappings = new ArrayList<ClientMapping>();
        for (ModelNode mappingNode : mappings.asList()) {
            ModelNode sourceNode = mappingNode.get(SOURCE_NETWORK);
            final InetAddress sourceAddress;
            final int mask;
            final String destination;
            final int port;
            if (sourceNode.isDefined()) {
                MaskedAddressValidator.ParsedResult parsedResult = MaskedAddressValidator.parseMasked(sourceNode);
                sourceAddress = parsedResult.address;
                mask = parsedResult.mask;
            } else {
                // Client mappings are always communicated in IPv6
                sourceAddress = ANY_IPV6;
                mask = 0;
            }

            ModelNode destinationNode = mappingNode.get(DESTINATION_ADDRESS);
            if (! destinationNode.isDefined()) {
                // Validation prevents this, but just in case
                throw new OperationFailedException(ControllerMessages.MESSAGES.nullNotAllowed(DESTINATION_ADDRESS));
            }
            destination = destinationNode.asString();

            ModelNode portNode = mappingNode.get(DESTINATION_PORT);
            port = portNode.isDefined() ? portNode.asInt() : -1;
            clientMappings.add(new ClientMapping(sourceAddress, mask, destination, port));
        }

        return clientMappings;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/784.java