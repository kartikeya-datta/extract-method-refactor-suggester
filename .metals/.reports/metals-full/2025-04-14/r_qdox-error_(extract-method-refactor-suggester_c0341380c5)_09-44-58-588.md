error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/468.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/468.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/468.java
text:
```scala
t@@hrow new OperationFailedException(new ModelNode().set(e.getLocalizedMessage()));

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

package org.jboss.as.messaging;

import org.jboss.as.controller.PathAddress;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hornetq.api.core.DiscoveryGroupConfiguration;
import org.hornetq.core.config.Configuration;
import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SOCKET_BINDING;
import org.jboss.as.network.NetworkInterfaceBinding;
import org.jboss.as.network.SocketBinding;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.value.ImmediateValue;

/**
 * Handler for adding a discovery group.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class DiscoveryGroupAdd extends AbstractAddStepHandler implements DescriptionProvider {

    private static final OperationValidator VALIDATOR = new OperationValidator.AttributeDefinitionOperationValidator(CommonAttributes.DISCOVERY_GROUP_ATTRIBUTES);

    /**
     * Create an "add" operation using the existing model
     */
    public static ModelNode getAddOperation(final ModelNode address, ModelNode subModel) {
        final ModelNode operation = org.jboss.as.controller.operations.common.Util.getOperation(ADD, address, subModel);
        return operation;
    }

    public static final DiscoveryGroupAdd INSTANCE = new DiscoveryGroupAdd();

    private DiscoveryGroupAdd() {
    }

    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        model.setEmptyObject();
        VALIDATOR.validateAndSet(operation, model);
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {

        final PathAddress address = PathAddress.pathAddress(operation.require(OP_ADDR));
        final String name = address.getLastElement().getValue();
        ServiceRegistry registry = context.getServiceRegistry(false);
        ServiceController<?> hqService = registry.getService(MessagingServices.JBOSS_MESSAGING);
        if (hqService != null) {
            context.reloadRequired();
        } else {
            final ServiceTarget target = context.getServiceTarget();
            if(model.hasDefined(CommonAttributes.SOCKET_BINDING.getName())) {
                final GroupBindingService bindingService = new GroupBindingService();
                target.addService(GroupBindingService.DISCOVERY.append(name), bindingService)
                        .addDependency(SocketBinding.JBOSS_BINDING_NAME.append(model.get(SOCKET_BINDING).asString()), SocketBinding.class, bindingService.getBindingRef())
                        .install();
            } else {

                final ModelNode localAddrNode = CommonAttributes.LOCAL_BIND_ADDRESS.validateResolvedOperation(model);
                final String localAddress = localAddrNode.isDefined() ? localAddrNode.asString() : null;
                final String groupAddress = CommonAttributes.GROUP_ADDRESS.validateResolvedOperation(model).asString();
                final int groupPort = CommonAttributes.GROUP_PORT.validateResolvedOperation(model).asInt();

                try {

                    final InetAddress inet = localAddress != null ? InetAddress.getByName(localAddress) : InetAddress.getLocalHost();
                    final NetworkInterface intf = NetworkInterface.getByInetAddress(inet);
                    final NetworkInterfaceBinding b = new NetworkInterfaceBinding(Collections.singleton(intf), inet);
                    final InetAddress group = InetAddress.getByName(groupAddress);

                    final SocketBinding socketBinding = new SocketBinding(name, -1, false, group, groupPort, b, null);

                    final GroupBindingService bindingService = new GroupBindingService();
                    target.addService(GroupBindingService.DISCOVERY.append(name), bindingService)
                            .addInjectionValue(bindingService.getBindingRef(), new ImmediateValue<SocketBinding>(socketBinding))
                            .install();

                } catch (Exception e) {
                    throw new OperationFailedException(new ModelNode().set(e.getMessage()));
                }
            }
        }
    }

    @Override
    public ModelNode getModelDescription(Locale locale) {
        return MessagingDescriptions.getDiscoveryGroupAdd(locale);
    }

    static void addDiscoveryGroupConfigs(final Configuration configuration, final ModelNode model)  throws OperationFailedException {
        if (model.hasDefined(CommonAttributes.DISCOVERY_GROUP)) {
            Map<String, DiscoveryGroupConfiguration> configs = configuration.getDiscoveryGroupConfigurations();
            if (configs == null) {
                configs = new HashMap<String, DiscoveryGroupConfiguration>();
                configuration.setDiscoveryGroupConfigurations(configs);
            }
            for (Property prop : model.get(CommonAttributes.DISCOVERY_GROUP).asPropertyList()) {
                configs.put(prop.getName(), createDiscoveryGroupConfiguration(prop.getName(), prop.getValue()));

            }
        }
    }

    static DiscoveryGroupConfiguration createDiscoveryGroupConfiguration(final String name, final ModelNode model) throws OperationFailedException {

        final long refreshTimeout = CommonAttributes.REFRESH_TIMEOUT.validateResolvedOperation(model).asLong();
        final long initialWaitTimeout = CommonAttributes.INITIAL_WAIT_TIMEOUT.validateResolvedOperation(model).asLong();
        // Requires runtime service
        return new DiscoveryGroupConfiguration(name, null, null, 0, refreshTimeout, initialWaitTimeout);
    }

    static DiscoveryGroupConfiguration createDiscoveryGroupConfiguration(final String name, final DiscoveryGroupConfiguration config, final SocketBinding socketBinding) {

        final String localAddress = socketBinding.getAddress().toString();
        final String groupAddress = socketBinding.getMulticastAddress().toString();
        final int groupPort = socketBinding.getMulticastPort();
        final long refreshTimeout = config.getRefreshTimeout();
        final long initialWaitTimeout = config.getDiscoveryInitialWaitTimeout();

        return new DiscoveryGroupConfiguration(name, localAddress, groupAddress, groupPort, refreshTimeout, initialWaitTimeout);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/468.java