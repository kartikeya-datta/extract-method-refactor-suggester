error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4396.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4396.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4396.java
text:
```scala
final E@@numSet<OperationEntry.Flag> readOnly = EnumSet.of(OperationEntry.Flag.READ_ONLY, OperationEntry.Flag.RUNTIME_ONLY);

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
package org.jboss.as.clustering.jgroups.subsystem;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.EnumSet;
import java.util.List;

import org.jboss.as.clustering.jgroups.LogFactory;
import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.controller.registry.OperationEntry.EntryType;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;
import org.jgroups.Global;

/**
 * Registers the JGroups subsystem.
 *
 * @author Paul Ferraro
 * @author Richard Achmatowicz (c) 2011 Red Hat Inc.
 */
public class JGroupsExtension implements Extension {

    static final String SUBSYSTEM_NAME = "jgroups";

    private static final PathElement stacksPath = PathElement.pathElement(ModelKeys.STACK);
    private static final PathElement transportPath = PathElement.pathElement(ModelKeys.TRANSPORT, ModelKeys.TRANSPORT_NAME);
    private static final PathElement protocolPath = PathElement.pathElement(ModelKeys.PROTOCOL);
    private static final PathElement protocolPropertyPath = PathElement.pathElement(ModelKeys.PROPERTY);

    private static final int MANAGEMENT_API_MAJOR_VERSION = 1;
    private static final int MANAGEMENT_API_MINOR_VERSION = 1;
    private static final int MANAGEMENT_API_MICRO_VERSION = 0;

    // Temporary workaround for JGRP-1475
    // Configure JGroups to use jboss-logging.
    static {
        PrivilegedAction<Void> action = new PrivilegedAction<Void>() {
            @Override
            public Void run() {
                if (System.getProperty(Global.CUSTOM_LOG_FACTORY) == null) {
                    System.setProperty(Global.CUSTOM_LOG_FACTORY, LogFactory.class.getName());
                }
                return null;
            }
        };
        AccessController.doPrivileged(action);
    }

    /**
     * {@inheritDoc}
     * @see org.jboss.as.controller.Extension#initialize(org.jboss.as.controller.ExtensionContext)
     */
    @Override
    public void initialize(ExtensionContext context) {

        // IMPORTANT: Management API version != xsd version! Not all Management API changes result in XSD changes
        SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME, MANAGEMENT_API_MAJOR_VERSION,
                MANAGEMENT_API_MINOR_VERSION, MANAGEMENT_API_MICRO_VERSION);
        subsystem.registerXMLElementWriter(new JGroupsSubsystemXMLWriter());

        ManagementResourceRegistration registration = subsystem.registerSubsystemModel(JGroupsSubsystemProviders.SUBSYSTEM);
        registration.registerOperationHandler(ModelDescriptionConstants.ADD, JGroupsSubsystemAdd.INSTANCE, JGroupsSubsystemProviders.SUBSYSTEM_ADD, false);
        registration.registerOperationHandler(ModelDescriptionConstants.REMOVE, JGroupsSubsystemRemove.INSTANCE, JGroupsSubsystemProviders.SUBSYSTEM_REMOVE, false);
        registration.registerOperationHandler(ModelDescriptionConstants.DESCRIBE, JGroupsSubsystemDescribe.INSTANCE, JGroupsSubsystemProviders.SUBSYSTEM_DESCRIBE, false, EntryType.PRIVATE);
        SubsystemWriteAttributeHandler.INSTANCE.registerAttributes(registration);

        ManagementResourceRegistration stacks = registration.registerSubModel(stacksPath, JGroupsSubsystemProviders.STACK);
        stacks.registerOperationHandler(ModelDescriptionConstants.ADD, ProtocolStackAdd.INSTANCE, JGroupsSubsystemProviders.STACK_ADD, false);
        stacks.registerOperationHandler(ModelDescriptionConstants.REMOVE, ProtocolStackRemove.INSTANCE, JGroupsSubsystemProviders.STACK_REMOVE, false);
        // register the add-protocol and remove-protocol handlers
        stacks.registerOperationHandler(ModelKeys.ADD_PROTOCOL, StackConfigOperationHandlers.PROTOCOL_ADD, JGroupsSubsystemProviders.PROTOCOL_ADD);
        stacks.registerOperationHandler(ModelKeys.REMOVE_PROTOCOL, StackConfigOperationHandlers.PROTOCOL_REMOVE, JGroupsSubsystemProviders.PROTOCOL_REMOVE);
        // register the export operation
        if (context.isRuntimeOnlyRegistrationValid()) {
            final EnumSet<OperationEntry.Flag> readOnly = EnumSet.of(OperationEntry.Flag.READ_ONLY);
            stacks.registerOperationHandler(ModelKeys.EXPORT_NATIVE_CONFIGURATION, StackConfigOperationHandlers.EXPORT_NATIVE_CONFIGURATION, JGroupsSubsystemProviders.EXPORT_NATIVE_CONFIGURATION, readOnly);
        }

        // register the transport=TRANSPORT handlers
        final ManagementResourceRegistration transport = stacks.registerSubModel(transportPath, JGroupsSubsystemProviders.TRANSPORT);
        transport.registerOperationHandler(ADD, StackConfigOperationHandlers.TRANSPORT_ADD, JGroupsSubsystemProviders.TRANSPORT_ADD);
        transport.registerOperationHandler(REMOVE, StackConfigOperationHandlers.REMOVE, JGroupsSubsystemProviders.TRANSPORT_REMOVE);
        StackConfigOperationHandlers.TRANSPORT_ATTR.registerAttributes(transport);
        createPropertyRegistration(transport);

        // register the protocol=* handlers
        final ManagementResourceRegistration protocol = stacks.registerSubModel(protocolPath, JGroupsSubsystemProviders.PROTOCOL);
        StackConfigOperationHandlers.PROTOCOL_ATTR.registerAttributes(protocol);
        JGroupsExtension.createPropertyRegistration(protocol);
    }

    /**
     * {@inheritDoc}
     * @see org.jboss.as.controller.Extension#initializeParsers(org.jboss.as.controller.parsing.ExtensionParsingContext)
     */
    @Override
    public void initializeParsers(ExtensionParsingContext context) {
        for (Namespace namespace: Namespace.values()) {
            XMLElementReader<List<ModelNode>> reader = namespace.getXMLReader();
            if (reader != null) {
                context.setSubsystemXmlMapping(SUBSYSTEM_NAME, namespace.getUri(), reader);
            }
        }
    }


    static void createPropertyRegistration(final ManagementResourceRegistration parent) {
        final ManagementResourceRegistration registration = parent.registerSubModel(protocolPropertyPath, JGroupsSubsystemProviders.PROTOCOL_PROPERTY);
        registration.registerOperationHandler(ADD, StackConfigOperationHandlers.PROTOCOL_PROPERTY_ADD, JGroupsSubsystemProviders.PROTOCOL_PROPERTY_ADD);
        registration.registerOperationHandler(REMOVE, StackConfigOperationHandlers.REMOVE, JGroupsSubsystemProviders.PROTOCOL_PROPERTY_REMOVE);
        registration.registerReadWriteAttribute("value", null, StackConfigOperationHandlers.PROTOCOL_PROPERTY_ATTR, EnumSet.of(AttributeAccess.Flag.RESTART_ALL_SERVICES));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4396.java