error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4336.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4336.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4336.java
text:
```scala
public static final S@@impleAttributeDefinition SOCKET_BINDING_PORT_OFFSET = SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.SOCKET_BINDING_PORT_OFFSET, ModelType.INT, true)

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.as.host.controller.resources;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SERVER_CONFIG;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.ModelOnlyWriteAttributeHandler;
import org.jboss.as.controller.NoopOperationStepHandler;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ReadResourceNameOperationStepHandler;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.client.helpers.domain.ServerStatus;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.operations.validation.EnumValidator;
import org.jboss.as.controller.operations.validation.ModelTypeValidator;
import org.jboss.as.controller.parsing.Attribute;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.controller.resource.InterfaceDefinition;
import org.jboss.as.controller.services.path.PathManagerService;
import org.jboss.as.controller.services.path.PathResourceDefinition;
import org.jboss.as.host.controller.ServerInventory;
import org.jboss.as.host.controller.descriptions.HostRootDescription;
import org.jboss.as.host.controller.model.jvm.JvmResourceDefinition;
import org.jboss.as.host.controller.operations.ServerAddHandler;
import org.jboss.as.host.controller.operations.ServerRemoveHandler;
import org.jboss.as.host.controller.operations.ServerRestartHandler;
import org.jboss.as.host.controller.operations.ServerRestartRequiredServerConfigWriteAttributeHandler;
import org.jboss.as.host.controller.operations.ServerStartHandler;
import org.jboss.as.host.controller.operations.ServerStatusHandler;
import org.jboss.as.host.controller.operations.ServerStopHandler;
import org.jboss.as.server.controller.resources.SystemPropertyResourceDefinition;
import org.jboss.as.server.controller.resources.SystemPropertyResourceDefinition.Location;
import org.jboss.as.server.services.net.SpecifiedInterfaceAddHandler;
import org.jboss.as.server.services.net.SpecifiedInterfaceRemoveHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * {@link ResourceDefinition} for a {@code server-config} resource under a host.
 *
 * @author Brian Stansberry (c) 2012 Red Hat Inc.
 */
public class ServerConfigResourceDefinition extends SimpleResourceDefinition {

    public static final AttributeDefinition NAME = SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.NAME, ModelType.STRING).setResourceOnly().build();

    public static final SimpleAttributeDefinition AUTO_START = SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.AUTO_START, ModelType.BOOLEAN, true)
            .setAllowExpression(true)
            .setDefaultValue(new ModelNode(true)).build();

    public static final SimpleAttributeDefinition SOCKET_BINDING_GROUP = SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.SOCKET_BINDING_GROUP, ModelType.STRING, true)
            .setAllowExpression(true).build();

    public static final SimpleAttributeDefinition SOCKET_BINDING_PORT_OFFSET = SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.SOCKET_BINDING_PORT_OFFSET, ModelType.STRING, true)
            .setAllowExpression(true)
            .setDefaultValue(new ModelNode(0))
            .setXmlName(Attribute.PORT_OFFSET.getLocalName())
            .setValidator(new ModelTypeValidator(ModelType.INT, true, true))
            .build();

    public static final SimpleAttributeDefinition GROUP = SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.GROUP, ModelType.STRING)
            .setAllowExpression(true).build();

    public static final SimpleAttributeDefinition STATUS = SimpleAttributeDefinitionBuilder.create(ServerStatusHandler.ATTRIBUTE_NAME, ModelType.STRING)
            .setValidator(new EnumValidator<ServerStatus>(ServerStatus.class, false, false))
            .build();

    /**
     * Bogus attribute that we accidentally registered in AS 7.1.2/EAP 6 even though it didn't appear in the
     * resource description. So for compatibility we register it here as well, and include it in the description
     * to be consistent and to avoid having to do hacks just to not register it.
     */
    public static final AttributeDefinition PRIORITY  = SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.PRIORITY, ModelType.INT, true).build();
    /**
     * Bogus attribute that we accidentally registered in AS 7.1.2/EAP 6 even though it didn't appear in the
     * resource description. So for compatibility we register it here as well, and include it in the description
     * to be consistent and to avoid having to do hacks just to not register it.
     */
    public static final AttributeDefinition CPU_AFFINITY  = SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.CPU_AFFINITY, ModelType.STRING, true).build();


    /** The attributes that can be written by the {@code add} operation */
    public static final List<SimpleAttributeDefinition> WRITABLE_ATTRIBUTES = Arrays.asList(AUTO_START, SOCKET_BINDING_GROUP, SOCKET_BINDING_PORT_OFFSET, GROUP);

    private final ServerInventory serverInventory;
    private final PathManagerService pathManager;

    /**
     * Creates a ServerConfigResourceDefinition.
     * @param serverInventory  the server inventory to use for runtime server lifecycle operations. May be {@code null}
     *                         in which case no such operations will be registered
     * @param pathManager the {@link PathManagerService} to use for the child {@code path} resources. Cannot be {@code null}
     */
    public ServerConfigResourceDefinition(final ServerInventory serverInventory, final PathManagerService pathManager) {
        super(PathElement.pathElement(SERVER_CONFIG), HostRootDescription.getResourceDescriptionResolver(SERVER_CONFIG, false),
                ServerAddHandler.INSTANCE, ServerRemoveHandler.INSTANCE);

        assert pathManager != null : "pathManager is null";

        this.serverInventory = serverInventory;
        this.pathManager = pathManager;
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {

        resourceRegistration.registerReadOnlyAttribute(NAME, ReadResourceNameOperationStepHandler.INSTANCE);

        resourceRegistration.registerReadWriteAttribute(AUTO_START, null, new ModelOnlyWriteAttributeHandler(AUTO_START));
        resourceRegistration.registerReadWriteAttribute(SOCKET_BINDING_GROUP, null, ServerRestartRequiredServerConfigWriteAttributeHandler.SOCKET_BINDING_GROUP_INSTANCE);
        resourceRegistration.registerReadWriteAttribute(SOCKET_BINDING_PORT_OFFSET, null, ServerRestartRequiredServerConfigWriteAttributeHandler.SOCKET_BINDING_PORT_OFFSET_INSTANCE);
        resourceRegistration.registerReadWriteAttribute(GROUP, null, ServerRestartRequiredServerConfigWriteAttributeHandler.GROUP_INSTANCE);

        // For compatibility, register these should-be-removed attributes, with no-op handlers
        resourceRegistration.registerReadWriteAttribute(PRIORITY, NoopOperationStepHandler.WITH_RESULT, NoopOperationStepHandler.WITHOUT_RESULT);
        resourceRegistration.registerReadWriteAttribute(CPU_AFFINITY, NoopOperationStepHandler.WITH_RESULT, NoopOperationStepHandler.WITHOUT_RESULT);

        if (serverInventory != null) {
            resourceRegistration.registerMetric(STATUS, new ServerStatusHandler(serverInventory));
        }
    }

    @Override
    public void registerOperations(ManagementResourceRegistration resourceRegistration) {
        super.registerOperations(resourceRegistration);

        if (serverInventory != null) {
            // TODO convert these to use OperationDefinition
            ServerStartHandler startHandler = new ServerStartHandler(serverInventory);
            resourceRegistration.registerOperationHandler(ServerStartHandler.OPERATION_NAME, startHandler, startHandler, EnumSet.of(OperationEntry.Flag.HOST_CONTROLLER_ONLY));
            ServerRestartHandler restartHandler = new ServerRestartHandler(serverInventory);
            resourceRegistration.registerOperationHandler(ServerRestartHandler.OPERATION_NAME, restartHandler, restartHandler, EnumSet.of(OperationEntry.Flag.HOST_CONTROLLER_ONLY));
            ServerStopHandler stopHandler = new ServerStopHandler(serverInventory);
            resourceRegistration.registerOperationHandler(ServerStopHandler.OPERATION_NAME, stopHandler, stopHandler, EnumSet.of(OperationEntry.Flag.HOST_CONTROLLER_ONLY));
        }
    }

    @Override
    public void registerChildren(ManagementResourceRegistration resourceRegistration) {

        //server paths
        resourceRegistration.registerSubModel(PathResourceDefinition.createSpecifiedNoServices(pathManager));

        //server interfaces  TODO convert to ResourceDefinition
        //ManagementResourceRegistration serverInterfaces = resourceRegistration.registerSubModel(PathElement.pathElement(INTERFACE), CommonProviders.SPECIFIED_INTERFACE_PROVIDER);
        ManagementResourceRegistration serverInterfaces = resourceRegistration.registerSubModel(new InterfaceDefinition(
                SpecifiedInterfaceAddHandler.INSTANCE,
                SpecifiedInterfaceRemoveHandler.INSTANCE,
                true
        ));
        /*serverInterfaces.registerOperationHandler(InterfaceAddHandler.OPERATION_NAME, SpecifiedInterfaceAddHandler.INSTANCE, new DefaultResourceAddDescriptionProvider(serverInterfaces, CommonDescriptions.getResourceDescriptionResolver()), false);
        serverInterfaces.registerOperationHandler(InterfaceRemoveHandler.OPERATION_NAME, SpecifiedInterfaceRemoveHandler.INSTANCE, new DefaultResourceRemoveDescriptionProvider(CommonDescriptions.getResourceDescriptionResolver()), false);*/

        // Server system properties  TODO convert to ResourceDefinition
        resourceRegistration.registerSubModel(SystemPropertyResourceDefinition.createForDomainOrHost(Location.SERVER_CONFIG));
//        ManagementResourceRegistration serverSysProps = resourceRegistration.registerSubModel(PathElement.pathElement(SYSTEM_PROPERTY), HostDescriptionProviders.SERVER_SYSTEM_PROPERTIES_PROVIDER);
//        serverSysProps.registerOperationHandler(SystemPropertyAddHandler.OPERATION_NAME, SystemPropertyAddHandler.INSTANCE_WITH_BOOTTIME, SystemPropertyAddHandler.INSTANCE_WITH_BOOTTIME, false);
//        serverSysProps.registerOperationHandler(SystemPropertyRemoveHandler.OPERATION_NAME, SystemPropertyRemoveHandler.INSTANCE, SystemPropertyRemoveHandler.INSTANCE, false);
//        serverSysProps.registerReadWriteAttribute(VALUE, null, SystemPropertyValueWriteAttributeHandler.INSTANCE, AttributeAccess.Storage.CONFIGURATION);
//        serverSysProps.registerReadWriteAttribute(BOOT_TIME, null, new WriteAttributeHandlers.ModelTypeValidatingHandler(ModelType.BOOLEAN), AttributeAccess.Storage.CONFIGURATION);

        // Server jvm
        resourceRegistration.registerSubModel(JvmResourceDefinition.SERVER);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4336.java