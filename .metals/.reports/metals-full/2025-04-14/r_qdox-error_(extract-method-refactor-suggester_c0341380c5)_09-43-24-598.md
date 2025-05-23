error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2585.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2585.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2585.java
text:
```scala
private static final i@@nt MANAGEMENT_API_MAJOR_VERSION = 3;

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

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

import org.jboss.as.clustering.jgroups.LogFactory;
import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.ResourceDescriptionResolver;
import org.jboss.as.controller.descriptions.StandardResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.GenericSubsystemDescribeHandler;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.transform.description.RejectAttributeChecker;
import org.jboss.as.controller.transform.description.ResourceTransformationDescriptionBuilder;
import org.jboss.as.controller.transform.description.TransformationDescription;
import org.jboss.as.controller.transform.description.TransformationDescriptionBuilder;
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

    public static final String SUBSYSTEM_NAME = "jgroups";
    static final PathElement SUBSYSTEM_PATH = PathElement.pathElement(ModelDescriptionConstants.SUBSYSTEM, SUBSYSTEM_NAME);
    public static final String RESOURCE_NAME = JGroupsExtension.class.getPackage().getName() + "." +"LocalDescriptions";

    private static final int MANAGEMENT_API_MAJOR_VERSION = 2;
    private static final int MANAGEMENT_API_MINOR_VERSION = 0;
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

    static ResourceDescriptionResolver getResourceDescriptionResolver(final String... keyPrefix) {
        StringBuilder prefix = new StringBuilder(SUBSYSTEM_NAME);
        for (String kp : keyPrefix) {
           prefix.append('.').append(kp);
        }
        return new StandardResourceDescriptionResolver(prefix.toString(), RESOURCE_NAME, JGroupsExtension.class.getClassLoader(), true, false);
    }

    /**
     * {@inheritDoc}
     * @see org.jboss.as.controller.Extension#initialize(org.jboss.as.controller.ExtensionContext)
     */
    @Override
    public void initialize(ExtensionContext context) {

        // IMPORTANT: Management API version != xsd version! Not all Management API changes result in XSD changes
        SubsystemRegistration registration = context.registerSubsystem(SUBSYSTEM_NAME, MANAGEMENT_API_MAJOR_VERSION, MANAGEMENT_API_MINOR_VERSION, MANAGEMENT_API_MICRO_VERSION);

        final boolean registerRuntimeOnly = context.isRuntimeOnlyRegistrationValid();
        final ManagementResourceRegistration subsystem = registration.registerSubsystemModel(new JGroupsSubsystemRootResourceDefinition());
        subsystem.registerOperationHandler(GenericSubsystemDescribeHandler.DEFINITION, JGroupsSubsystemDescribe.INSTANCE);

        subsystem.registerSubModel(new StackResourceDefinition(registerRuntimeOnly));
        registration.registerXMLElementWriter(new JGroupsSubsystemXMLWriter());

        if (context.isRegisterTransformers()) {
            // Register the model transformers
            registerTransformers(registration);
        }
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

    // Transformation

    /**
     * Register the transformers for older model versions.
     *
     * @param subsystem the subsystems registration
     */
    private static void registerTransformers(final SubsystemRegistration subsystem) {
        registerTransformers_1_1_0(subsystem);
        registerTransformers_1_2_0(subsystem);
    }

    /*
     * Register transformer to transform from current model version to model version 1.1.0
     */
    private static void registerTransformers_1_1_0(final SubsystemRegistration subsystem) {
        final ModelVersion version = ModelVersion.create(1,1,0);

        final ResourceTransformationDescriptionBuilder subsystemBuilder = TransformationDescriptionBuilder.Factory.createSubsystemInstance();
        final ResourceTransformationDescriptionBuilder stackBuilder = subsystemBuilder.addChildResource(StackResourceDefinition.STACK_PATH);

        // reject expressions in certain transport usages
        final ResourceTransformationDescriptionBuilder transportBuilder = stackBuilder.addChildResource(TransportResourceDefinition.TRANSPORT_PATH);
        transportBuilder.getAttributeBuilder()
                .addRejectCheck(RejectAttributeChecker.SIMPLE_EXPRESSIONS, TransportResourceDefinition.SHARED)
                .addRejectCheck(RejectAttributeChecker.SIMPLE_EXPRESSIONS, TransportResourceDefinition.PROPERTIES)
                .end();

        final ResourceTransformationDescriptionBuilder transportPropertyBuilder = transportBuilder.addChildResource(PropertyResourceDefinition.PROPERTY_PATH);
        transportPropertyBuilder.getAttributeBuilder()
                .addRejectCheck(RejectAttributeChecker.SIMPLE_EXPRESSIONS, PropertyResourceDefinition.VALUE)
                .end();

        final ResourceTransformationDescriptionBuilder protocolBuilder = stackBuilder.addChildResource(ProtocolResourceDefinition.PROTOCOL_PATH);
        protocolBuilder.getAttributeBuilder()
                .addRejectCheck(RejectAttributeChecker.SIMPLE_EXPRESSIONS, ProtocolResourceDefinition.PROPERTIES)
                .end();
        final ResourceTransformationDescriptionBuilder protocolPropertyBuilder = protocolBuilder.addChildResource(PropertyResourceDefinition.PROPERTY_PATH);
        protocolPropertyBuilder.getAttributeBuilder()
                .addRejectCheck(RejectAttributeChecker.SIMPLE_EXPRESSIONS, PropertyResourceDefinition.VALUE)
                .end();

        // reject relay
        stackBuilder.rejectChildResource(RelayResource.PATH);

        // now register the completed transform
        TransformationDescription.Tools.register(subsystemBuilder.build(), subsystem, version);
    }

    /*
     * Register transformer to transform from current model version to model version 1.2.0
     */
    private static void registerTransformers_1_2_0(final SubsystemRegistration subsystem) {
        final ModelVersion version = ModelVersion.create(1,2,0);

        final ResourceTransformationDescriptionBuilder subsystemBuilder = TransformationDescriptionBuilder.Factory.createSubsystemInstance();
        final ResourceTransformationDescriptionBuilder stackBuilder = subsystemBuilder.addChildResource(StackResourceDefinition.STACK_PATH);

        // reject relay
        stackBuilder.rejectChildResource(RelayResource.PATH);

        // now register the completed transform
        TransformationDescription.Tools.register(subsystemBuilder.build(), subsystem, version);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2585.java