error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4092.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4092.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4092.java
text:
```scala
r@@egistration.registerReadWriteAttribute(ModelConstants.ACTIVATION, null, ActivationAttributeHandler.INSTANCE, EnumSet.of(AttributeAccess.Flag.STORAGE_CONFIGURATION, AttributeAccess.Flag.RESTART_JVM));

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
package org.jboss.as.osgi.parser;

import java.util.EnumSet;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.AttributeAccess.Storage;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;

/**
 * Domain extension used to initialize the OSGi subsystem.
 *
 * @author Thomas.Diesler@jboss.com
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 * @author David Bosschaert
 */
public class OSGiExtension implements Extension {

    public static final String SUBSYSTEM_NAME = "osgi";

    @Override
    public void initializeParsers(ExtensionParsingContext context) {
        context.setSubsystemXmlMapping(Namespace.OSGI_1_0.getUriString(), OSGiNamespace10Parser.INSTANCE);
        context.setSubsystemXmlMapping(Namespace.OSGI_1_1.getUriString(), OSGiNamespace11Parser.INSTANCE);
    }

    @Override
    public void initialize(ExtensionContext context) {
        final SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME);
        final ManagementResourceRegistration registration = subsystem.registerSubsystemModel(OSGiSubsystemProviders.SUBSYSTEM);
        registration.registerOperationHandler(ModelDescriptionConstants.ADD, OSGiSubsystemAdd.INSTANCE, OSGiSubsystemAdd.DESCRIPTION, false);
        registration.registerReadWriteAttribute(ModelConstants.ACTIVATION, null, new ActivationAttributeHandler(), EnumSet.of(AttributeAccess.Flag.STORAGE_CONFIGURATION, AttributeAccess.Flag.RESTART_JVM));
        registration.registerReadWriteAttribute(ModelConstants.STARTLEVEL, StartLevelHandler.READ_HANDLER, StartLevelHandler.WRITE_HANDLER, Storage.RUNTIME);
        registration.registerOperationHandler(ModelConstants.ACTIVATE, ActivateOperationHandler.INSTANCE, ActivateOperationHandler.INSTANCE, EnumSet.of(OperationEntry.Flag.RESTART_NONE));
        registration.registerOperationHandler(ModelDescriptionConstants.DESCRIBE, OSGiSubsystemDescribeHandler.INSTANCE, OSGiSubsystemAdd.DESCRIPTION, false, OperationEntry.EntryType.PRIVATE);

        // Configuration Admin Setings
        ManagementResourceRegistration configuration = registration.registerSubModel(PathElement.pathElement(ModelConstants.CONFIGURATION), OSGiSubsystemProviders.CONFIGURATION_DESCRIPTION);
        configuration.registerOperationHandler(ModelDescriptionConstants.ADD, OSGiConfigurationAdd.INSTANCE, OSGiConfigurationAdd.DESCRIPTION, false);
        configuration.registerOperationHandler(ModelDescriptionConstants.REMOVE, OSGiConfigurationRemove.INSTANCE, OSGiConfigurationRemove.DESCRIPTION, false);

        // Framework Properties
        ManagementResourceRegistration properties = registration.registerSubModel(PathElement.pathElement(ModelConstants.PROPERTY), OSGiSubsystemProviders.PROPERTY_DESCRIPTION);
        properties.registerOperationHandler(ModelDescriptionConstants.ADD, OSGiFrameworkPropertyAdd.INSTANCE, OSGiFrameworkPropertyAdd.DESCRIPTION, false);
        properties.registerOperationHandler(ModelDescriptionConstants.REMOVE, OSGiFrameworkPropertyRemove.INSTANCE, OSGiFrameworkPropertyRemove.DESCRIPTION, false);
        properties.registerReadWriteAttribute(ModelConstants.VALUE, null, OSGiFrameworkPropertyWrite.INSTANCE, Storage.CONFIGURATION);

        // Pre loaded modules
        ManagementResourceRegistration capabilities = registration.registerSubModel(PathElement.pathElement(ModelConstants.CAPABILITY), OSGiSubsystemProviders.CAPABILITY_DESCRIPTION);
        capabilities.registerOperationHandler(ModelDescriptionConstants.ADD, OSGiCapabilityAdd.INSTANCE, OSGiCapabilityAdd.DESCRIPTION, false);
        capabilities.registerOperationHandler(ModelDescriptionConstants.REMOVE, OSGiCapabilityRemove.INSTANCE, OSGiCapabilityRemove.DESCRIPTION, false);

        if (context.getProcessType().isServer()) {
            // Bundles present at runtime, this info is not available for controllers
            ManagementResourceRegistration bundles = registration.registerSubModel(PathElement.pathElement(ModelConstants.BUNDLE), OSGiSubsystemProviders.BUNDLE_DESCRIPTION);
            BundleRuntimeHandler.INSTANCE.register(bundles);
        }

        subsystem.registerXMLElementWriter(OSGiSubsystemWriter.INSTANCE);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4092.java