error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/341.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/341.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/341.java
text:
```scala
s@@ervers.registerReadWriteAttribute(SOCKET_BINDING_PORT_OFFSET, null, new WriteAttributeHandlers.IntRangeValidatingHandler(0), Storage.CONFIGURATION);

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
package org.jboss.as.host.controller;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.AUTO_START;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CPU_AFFINITY;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DOMAIN_CONTROLLER;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.EXTENSION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HOST;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HTTP_INTERFACE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.INTERFACE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.JVM;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MANAGEMENT_INTERFACES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAMESPACES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NATIVE_INTERFACE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PATH;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PRIORITY;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_CHILDREN_NAMES_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_CHILDREN_RESOURCES_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_CHILDREN_TYPES_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_OPERATION_DESCRIPTION_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_OPERATION_NAMES_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_DESCRIPTION_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RUNNING_SERVER;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SCHEMA_LOCATIONS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SERVER_CONFIG;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SOCKET_BINDING_GROUP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SOCKET_BINDING_PORT_OFFSET;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SYSTEM_PROPERTY;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;

import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.ExtensionContextImpl;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.descriptions.common.CommonProviders;
import org.jboss.as.controller.operations.common.InterfaceAddHandler;
import org.jboss.as.controller.operations.common.InterfaceRemoveHandler;
import org.jboss.as.controller.operations.common.JVMHandlers;
import org.jboss.as.controller.operations.common.NamespaceAddHandler;
import org.jboss.as.controller.operations.common.NamespaceRemoveHandler;
import org.jboss.as.controller.operations.common.PathAddHandler;
import org.jboss.as.controller.operations.common.PathRemoveHandler;
import org.jboss.as.controller.operations.common.SchemaLocationAddHandler;
import org.jboss.as.controller.operations.common.SchemaLocationRemoveHandler;
import org.jboss.as.controller.operations.common.SnapshotDeleteHandler;
import org.jboss.as.controller.operations.common.SnapshotListHandler;
import org.jboss.as.controller.operations.common.SnapshotTakeHandler;
import org.jboss.as.controller.operations.global.GlobalOperationHandlers;
import org.jboss.as.controller.operations.global.WriteAttributeHandlers;
import org.jboss.as.controller.persistence.ExtensibleConfigurationPersister;
import org.jboss.as.controller.registry.AttributeAccess.Storage;
import org.jboss.as.controller.registry.ModelNodeRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.domain.controller.operations.SystemPropertyAddHandler;
import org.jboss.as.domain.controller.operations.SystemPropertyRemoveHandler;
import org.jboss.as.host.controller.descriptions.HostDescriptionProviders;
import org.jboss.as.host.controller.operations.HttpManagementAddHandler;
import org.jboss.as.host.controller.operations.LocalDomainControllerAddHandler;
import org.jboss.as.host.controller.operations.LocalDomainControllerRemoveHandler;
import org.jboss.as.host.controller.operations.LocalHostAddHandler;
import org.jboss.as.host.controller.operations.NativeManagementAddHandler;
import org.jboss.as.host.controller.operations.RemoteDomainControllerAddHandler;
import org.jboss.as.host.controller.operations.RemoteDomainControllerRemoveHandler;
import org.jboss.as.host.controller.operations.ServerAddHandler;
import org.jboss.as.host.controller.operations.ServerRemoveHandler;
import org.jboss.as.server.operations.ExtensionAddHandler;
import org.jboss.as.server.operations.ExtensionRemoveHandler;

import org.jboss.as.server.services.net.SpecifiedInterfaceAddHandler;
import org.jboss.as.server.services.net.SpecifiedInterfaceRemoveHandler;

import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * Utility for creating the root element and populating the {@link ModelNodeRegistration}
 * for an individual host's portion of the model.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class HostModelUtil {

    public static void initCoreModel(final ModelNode root) {
        root.get(NAME);
        root.get(NAMESPACES).setEmptyList();
        root.get(SCHEMA_LOCATIONS).setEmptyList();
        root.get(EXTENSION);
        root.get(PATH);
        root.get(SYSTEM_PROPERTY);
        root.get(MANAGEMENT_INTERFACES);
        root.get(SERVER_CONFIG);
        root.get(DOMAIN_CONTROLLER);
        root.get(INTERFACE);
        root.get(JVM);
        root.get(RUNNING_SERVER);
    }

    /**
     * Create and return the base bootstrap host registry to allow the initial host registration.
     *
     * @return the ModelNodeRegistration
     */
    public static ModelNodeRegistration createBootstrapHostRegistry(ModelNodeRegistration hostRegistry, DomainModelProxy domainModelProxy) {
        final ModelNodeRegistration root = ModelNodeRegistration.Factory.create(HostDescriptionProviders.BOOTSTRAP_PROVIDER);

        ModelNodeRegistration hostRegistration = root.registerSubModel(PathElement.pathElement(HOST), HostDescriptionProviders.HOST_ROOT_PROVIDER);
        LocalHostAddHandler handler = LocalHostAddHandler.getInstance(hostRegistry, domainModelProxy);
        hostRegistration.registerOperationHandler(LocalHostAddHandler.OPERATION_NAME, handler, handler, false, OperationEntry.EntryType.PRIVATE);

        return root;
    }

    public static ModelNodeRegistration createHostRegistry(final ExtensibleConfigurationPersister configurationPersister, HostControllerEnvironment environment, DomainModelProxy domainModelProxy) {

        final ModelNodeRegistration root = ModelNodeRegistration.Factory.create(HostDescriptionProviders.ROOT_PROVIDER);
        // Global operations
        root.registerOperationHandler(GlobalOperationHandlers.ResolveAddressOperationHandler.OPERATION_NAME, GlobalOperationHandlers.RESOLVE, GlobalOperationHandlers.RESOLVE, false, OperationEntry.EntryType.PRIVATE);
        root.registerOperationHandler(READ_RESOURCE_OPERATION, GlobalOperationHandlers.READ_RESOURCE, CommonProviders.READ_RESOURCE_PROVIDER, true);
        root.registerOperationHandler(READ_ATTRIBUTE_OPERATION, GlobalOperationHandlers.READ_ATTRIBUTE, CommonProviders.READ_ATTRIBUTE_PROVIDER, true);
        root.registerOperationHandler(READ_RESOURCE_DESCRIPTION_OPERATION, GlobalOperationHandlers.READ_RESOURCE_DESCRIPTION, CommonProviders.READ_RESOURCE_DESCRIPTION_PROVIDER, true);
        root.registerOperationHandler(READ_CHILDREN_NAMES_OPERATION, GlobalOperationHandlers.READ_CHILDREN_NAMES, CommonProviders.READ_CHILDREN_NAMES_PROVIDER, true);
        root.registerOperationHandler(READ_CHILDREN_TYPES_OPERATION, GlobalOperationHandlers.READ_CHILDREN_TYPES, CommonProviders.READ_CHILDREN_TYPES_PROVIDER, true);
        root.registerOperationHandler(READ_CHILDREN_RESOURCES_OPERATION, GlobalOperationHandlers.READ_CHILDREN_RESOURCES, CommonProviders.READ_CHILDREN_RESOURCES_PROVIDER, true);
        root.registerOperationHandler(READ_OPERATION_NAMES_OPERATION, GlobalOperationHandlers.READ_OPERATION_NAMES, CommonProviders.READ_OPERATION_NAMES_PROVIDER, true);
        root.registerOperationHandler(READ_OPERATION_DESCRIPTION_OPERATION, GlobalOperationHandlers.READ_OPERATION_DESCRIPTION, CommonProviders.READ_OPERATION_PROVIDER, true);
        root.registerOperationHandler(WRITE_ATTRIBUTE_OPERATION, GlobalOperationHandlers.WRITE_ATTRIBUTE, CommonProviders.WRITE_ATTRIBUTE_PROVIDER, true);

        // Other root resource operations
        root.registerOperationHandler(NamespaceAddHandler.OPERATION_NAME, NamespaceAddHandler.INSTANCE, NamespaceAddHandler.INSTANCE, false);
        root.registerOperationHandler(NamespaceRemoveHandler.OPERATION_NAME, NamespaceRemoveHandler.INSTANCE, NamespaceRemoveHandler.INSTANCE, false);
        root.registerOperationHandler(SchemaLocationAddHandler.OPERATION_NAME, SchemaLocationAddHandler.INSTANCE, SchemaLocationAddHandler.INSTANCE, false);
        root.registerOperationHandler(SchemaLocationRemoveHandler.OPERATION_NAME, SchemaLocationRemoveHandler.INSTANCE, SchemaLocationRemoveHandler.INSTANCE, false);
        root.registerOperationHandler(SystemPropertyAddHandler.OPERATION_NAME, SystemPropertyAddHandler.INSTANCE, SystemPropertyAddHandler.INSTANCE, false);
        root.registerOperationHandler(SystemPropertyRemoveHandler.OPERATION_NAME, SystemPropertyRemoveHandler.INSTANCE, SystemPropertyRemoveHandler.INSTANCE, false);
        root.registerReadWriteAttribute(NAME, null, new WriteAttributeHandlers.StringLengthValidatingHandler(1), Storage.CONFIGURATION);
        // Management API protocols
        ModelNodeRegistration managementNative = root.registerSubModel(PathElement.pathElement(MANAGEMENT_INTERFACES, NATIVE_INTERFACE), CommonProviders.NATIVE_MANAGEMENT_PROVIDER);
        managementNative.registerOperationHandler(NativeManagementAddHandler.OPERATION_NAME, NativeManagementAddHandler.INSTANCE, NativeManagementAddHandler.INSTANCE, false);

        ModelNodeRegistration managementHttp = root.registerSubModel(PathElement.pathElement(MANAGEMENT_INTERFACES, HTTP_INTERFACE), CommonProviders.HTTP_MANAGEMENT_PROVIDER);
        HttpManagementAddHandler httpAddHandler = HttpManagementAddHandler.getInstance(environment);
        managementHttp.registerOperationHandler(HttpManagementAddHandler.OPERATION_NAME, httpAddHandler, httpAddHandler, false);
        // root.registerReadWriteAttribute(ModelDescriptionConstants.MANAGEMENT_INTERFACES, GlobalOperationHandlers.READ_ATTRIBUTE, ManagementSocketAddHandler.INSTANCE);
        //root.registerOperationHandler(ManagementSocketRemoveHandler.OPERATION_NAME, ManagementSocketRemoveHandler.INSTANCE, ManagementSocketRemoveHandler.INSTANCE, false);

        // TODO The DomainControllerAddHandlers need to be able to register the service and also associate with the DomainModel in use.
        LocalDomainControllerAddHandler localDcAddHandler = LocalDomainControllerAddHandler.getInstance(domainModelProxy, environment);
        root.registerOperationHandler(LocalDomainControllerAddHandler.OPERATION_NAME, localDcAddHandler, localDcAddHandler, false);
        root.registerOperationHandler(LocalDomainControllerRemoveHandler.OPERATION_NAME, LocalDomainControllerRemoveHandler.INSTANCE, LocalDomainControllerRemoveHandler.INSTANCE, false);
        RemoteDomainControllerAddHandler remoteDcAddHandler = RemoteDomainControllerAddHandler.getInstance(domainModelProxy, environment);
        root.registerOperationHandler(RemoteDomainControllerAddHandler.OPERATION_NAME, remoteDcAddHandler, remoteDcAddHandler, false);
        root.registerOperationHandler(RemoteDomainControllerRemoveHandler.OPERATION_NAME, RemoteDomainControllerRemoveHandler.INSTANCE, RemoteDomainControllerRemoveHandler.INSTANCE, false);
        SnapshotDeleteHandler snapshotDelete = new SnapshotDeleteHandler(configurationPersister);
        root.registerOperationHandler(SnapshotDeleteHandler.OPERATION_NAME, snapshotDelete, snapshotDelete, false);
        SnapshotListHandler snapshotList = new SnapshotListHandler(configurationPersister);
        root.registerOperationHandler(SnapshotListHandler.OPERATION_NAME, snapshotList, snapshotList, false);
        SnapshotTakeHandler snapshotTake = new SnapshotTakeHandler(configurationPersister);
        root.registerOperationHandler(SnapshotTakeHandler.OPERATION_NAME, snapshotTake, snapshotTake, false);

        //Extensions
        ModelNodeRegistration extensions = root.registerSubModel(PathElement.pathElement(EXTENSION), CommonProviders.EXTENSION_PROVIDER);
        ExtensionContext extensionContext = new ExtensionContextImpl(root, null, configurationPersister);
        ExtensionAddHandler addExtensionHandler = new ExtensionAddHandler(extensionContext);
        extensions.registerOperationHandler(ExtensionAddHandler.OPERATION_NAME, addExtensionHandler, addExtensionHandler, false);
        extensions.registerOperationHandler(ExtensionRemoveHandler.OPERATION_NAME, ExtensionRemoveHandler.INSTANCE, ExtensionRemoveHandler.INSTANCE, false);

        //TODO register system properties description provider
        //TODO register management description provider
        //TODO register domain controller description provider
        //TODO register jvm description provider

        final ModelNodeRegistration jvms = root.registerSubModel(PathElement.pathElement(JVM), CommonProviders.JVM_PROVIDER);
        JVMHandlers.register(jvms);

        //TODO register the rest of the root operations?

        //Paths
        ModelNodeRegistration paths = root.registerSubModel(PathElement.pathElement(PATH), CommonProviders.SPECIFIED_PATH_PROVIDER);
        paths.registerOperationHandler(PathAddHandler.OPERATION_NAME, PathAddHandler.SPECIFIED_INSTANCE, PathAddHandler.SPECIFIED_INSTANCE, false);
        paths.registerOperationHandler(PathRemoveHandler.OPERATION_NAME, PathRemoveHandler.INSTANCE, PathRemoveHandler.INSTANCE, false);

        //interface
        ModelNodeRegistration interfaces = root.registerSubModel(PathElement.pathElement(INTERFACE), CommonProviders.SPECIFIED_INTERFACE_PROVIDER);
        interfaces.registerOperationHandler(InterfaceAddHandler.OPERATION_NAME, SpecifiedInterfaceAddHandler.INSTANCE, SpecifiedInterfaceAddHandler.INSTANCE, false);
        interfaces.registerOperationHandler(InterfaceRemoveHandler.OPERATION_NAME, SpecifiedInterfaceRemoveHandler.INSTANCE, SpecifiedInterfaceRemoveHandler.INSTANCE, false);

        //server
        ModelNodeRegistration servers = root.registerSubModel(PathElement.pathElement(SERVER_CONFIG), HostDescriptionProviders.SERVER_PROVIDER);
        servers.registerOperationHandler(ServerAddHandler.OPERATION_NAME, ServerAddHandler.INSTANCE, ServerAddHandler.INSTANCE, false);
        servers.registerOperationHandler(ServerRemoveHandler.OPERATION_NAME, ServerRemoveHandler.INSTANCE, ServerRemoveHandler.INSTANCE, false);
        servers.registerOperationHandler(SystemPropertyAddHandler.OPERATION_NAME, SystemPropertyAddHandler.INSTANCE, SystemPropertyAddHandler.INSTANCE, false);
        servers.registerOperationHandler(SystemPropertyRemoveHandler.OPERATION_NAME, SystemPropertyRemoveHandler.INSTANCE, SystemPropertyRemoveHandler.INSTANCE, false);
        servers.registerReadWriteAttribute(AUTO_START, null, new WriteAttributeHandlers.ModelTypeValidatingHandler(ModelType.BOOLEAN), Storage.CONFIGURATION);
        servers.registerReadWriteAttribute(SOCKET_BINDING_GROUP, null, WriteAttributeHandlers.WriteAttributeOperationHandler.INSTANCE, Storage.CONFIGURATION);
        servers.registerReadWriteAttribute(SOCKET_BINDING_PORT_OFFSET, null, new WriteAttributeHandlers.IntRangeValidatingHandler(1), Storage.CONFIGURATION);
        servers.registerReadWriteAttribute(PRIORITY, null, new WriteAttributeHandlers.IntRangeValidatingHandler(0), Storage.CONFIGURATION);
        servers.registerReadWriteAttribute(CPU_AFFINITY, null, new WriteAttributeHandlers.StringLengthValidatingHandler(1), Storage.CONFIGURATION);
        //server paths
        ModelNodeRegistration serverPaths = servers.registerSubModel(PathElement.pathElement(PATH), CommonProviders.SPECIFIED_INTERFACE_PROVIDER);
        serverPaths.registerOperationHandler(PathAddHandler.OPERATION_NAME, PathAddHandler.SPECIFIED_INSTANCE, PathAddHandler.SPECIFIED_INSTANCE, false);
        serverPaths.registerOperationHandler(PathRemoveHandler.OPERATION_NAME, PathRemoveHandler.INSTANCE, PathRemoveHandler.INSTANCE, false);
        //server interfaces
        ModelNodeRegistration serverInterfaces = servers.registerSubModel(PathElement.pathElement(INTERFACE), CommonProviders.SPECIFIED_INTERFACE_PROVIDER);
        serverInterfaces.registerOperationHandler(InterfaceAddHandler.OPERATION_NAME, SpecifiedInterfaceAddHandler.INSTANCE, SpecifiedInterfaceAddHandler.INSTANCE, false);
        serverInterfaces.registerOperationHandler(InterfaceRemoveHandler.OPERATION_NAME, SpecifiedInterfaceRemoveHandler.INSTANCE, SpecifiedInterfaceRemoveHandler.INSTANCE, false);
        //TODO register server system properties description provider
        //TODO register server jvm description provider
        final ModelNodeRegistration serverVMs = servers.registerSubModel(PathElement.pathElement(JVM), CommonProviders.JVM_PROVIDER);
        JVMHandlers.register(serverVMs);

        //TODO register the rest of the server values

        return root;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/341.java