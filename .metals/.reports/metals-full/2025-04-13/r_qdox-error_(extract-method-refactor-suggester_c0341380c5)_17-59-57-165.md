error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/798.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/798.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/798.java
text:
```scala
s@@ubsystem.registerXMLElementWriter(EJB3SubsystemXMLPersister.INSTANCE);

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

package org.jboss.as.ejb3.subsystem;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.ResourceDescriptionResolver;
import org.jboss.as.controller.descriptions.StandardResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.GenericSubsystemDescribeHandler;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.services.path.ResolvePathHandler;
import org.jboss.as.ejb3.subsystem.deployment.EntityBeanResourceDefinition;
import org.jboss.as.ejb3.subsystem.deployment.MessageDrivenBeanResourceDefinition;
import org.jboss.as.ejb3.subsystem.deployment.SingletonBeanDeploymentResourceDefinition;
import org.jboss.as.ejb3.subsystem.deployment.StatefulSessionBeanDeploymentResourceDefinition;
import org.jboss.as.ejb3.subsystem.deployment.StatelessSessionBeanDeploymentResourceDefinition;
import org.jboss.as.threads.ThreadFactoryResolver;
import org.jboss.as.threads.ThreadsServices;
import org.jboss.as.threads.UnboundedQueueThreadPoolResourceDefinition;

/**
 * Extension that provides the EJB3 subsystem.
 *
 * @author Emanuel Muckenhuber
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class EJB3Extension implements Extension {

    public static final String SUBSYSTEM_NAME = "ejb3";
    public static final String NAMESPACE_1_0 = EJB3SubsystemNamespace.EJB3_1_0.getUriString();
    public static final String NAMESPACE_1_1 = EJB3SubsystemNamespace.EJB3_1_1.getUriString();
    public static final String NAMESPACE_1_2 = EJB3SubsystemNamespace.EJB3_1_2.getUriString();
    public static final String NAMESPACE_1_3 = EJB3SubsystemNamespace.EJB3_1_3.getUriString();
    public static final String NAMESPACE_1_4 = EJB3SubsystemNamespace.EJB3_1_4.getUriString();

    private static final int MANAGEMENT_API_MAJOR_VERSION = 1;
    private static final int MANAGEMENT_API_MINOR_VERSION = 1;
    private static final int MANAGEMENT_API_MICRO_VERSION = 0;

    private static final String RESOURCE_NAME = EJB3Extension.class.getPackage().getName() + ".LocalDescriptions";

    public static ResourceDescriptionResolver getResourceDescriptionResolver(final String keyPrefix) {
        return new StandardResourceDescriptionResolver(keyPrefix, RESOURCE_NAME, EJB3Extension.class.getClassLoader(), true, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(ExtensionContext context) {

        final boolean registerRuntimeOnly = context.isRuntimeOnlyRegistrationValid();

        final SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME, MANAGEMENT_API_MAJOR_VERSION,
                MANAGEMENT_API_MINOR_VERSION, MANAGEMENT_API_MICRO_VERSION);

        subsystem.registerXMLElementWriter(EJB3Subsystem14Parser.INSTANCE);

        final ManagementResourceRegistration subsystemRegistration = subsystem.registerSubsystemModel(EJB3SubsystemRootResourceDefinition.INSTANCE);

        // describe operation for the subsystem
        subsystemRegistration.registerOperationHandler(GenericSubsystemDescribeHandler.DEFINITION, GenericSubsystemDescribeHandler.INSTANCE);

        // subsystem=ejb3/service=remote
        subsystemRegistration.registerSubModel(EJB3RemoteResourceDefinition.INSTANCE);

        // subsystem=ejb3/service=async
        subsystemRegistration.registerSubModel(EJB3AsyncResourceDefinition.INSTANCE);

        // subsystem=ejb3/strict-max-bean-instance-pool=*
        subsystemRegistration.registerSubModel(StrictMaxPoolResourceDefinition.INSTANCE);

        subsystemRegistration.registerSubModel(CacheFactoryResourceDefinition.INSTANCE);
        subsystemRegistration.registerSubModel(FilePassivationStoreResourceDefinition.INSTANCE);
        subsystemRegistration.registerSubModel(ClusterPassivationStoreResourceDefinition.INSTANCE);

        // subsystem=ejb3/service=timerservice
        final ManagementResourceRegistration timerService = subsystemRegistration.registerSubModel(TimerServiceResourceDefinition.INSTANCE);
        // Create the path resolver handler
        if (context.getProcessType().isServer()) {
            final ResolvePathHandler resolvePathHandler = ResolvePathHandler.Builder.of(context.getPathManager())
                    .setPathAttribute(TimerServiceResourceDefinition.PATH)
                    .setRelativeToAttribute(TimerServiceResourceDefinition.RELATIVE_TO)
                    .build();
            timerService.registerOperationHandler(resolvePathHandler.getOperationDefinition(), resolvePathHandler);
        }

        // subsystem=ejb3/thread-pool=*
        subsystemRegistration.registerSubModel(UnboundedQueueThreadPoolResourceDefinition.create(EJB3SubsystemModel.THREAD_POOL,
                new EJB3ThreadFactoryResolver(), EJB3SubsystemModel.BASE_THREAD_POOL_SERVICE_NAME, registerRuntimeOnly));

        // subsystem=ejb3/service=iiop
        subsystemRegistration.registerSubModel(EJB3IIOPResourceDefinition.INSTANCE);

        if (registerRuntimeOnly) {
            ResourceDefinition deploymentsDef = new SimpleResourceDefinition(PathElement.pathElement(ModelDescriptionConstants.SUBSYSTEM, SUBSYSTEM_NAME),
                    getResourceDescriptionResolver("deployed"));
            final ManagementResourceRegistration deploymentsRegistration = subsystem.registerDeploymentModel(deploymentsDef);
            deploymentsRegistration.registerSubModel(EntityBeanResourceDefinition.INSTANCE);
            deploymentsRegistration.registerSubModel(MessageDrivenBeanResourceDefinition.INSTANCE);
            deploymentsRegistration.registerSubModel(SingletonBeanDeploymentResourceDefinition.INSTANCE);
            deploymentsRegistration.registerSubModel(StatelessSessionBeanDeploymentResourceDefinition.INSTANCE);
            deploymentsRegistration.registerSubModel(StatefulSessionBeanDeploymentResourceDefinition.INSTANCE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParsers(ExtensionParsingContext context) {
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, NAMESPACE_1_0, EJB3Subsystem10Parser.INSTANCE);
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, NAMESPACE_1_1, EJB3Subsystem11Parser.INSTANCE);
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, NAMESPACE_1_2, EJB3Subsystem12Parser.INSTANCE);
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, NAMESPACE_1_3, EJB3Subsystem13Parser.INSTANCE);
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, NAMESPACE_1_4, EJB3Subsystem14Parser.INSTANCE);
    }

    private static class EJB3ThreadFactoryResolver extends ThreadFactoryResolver.SimpleResolver {

        private EJB3ThreadFactoryResolver() {
            super(ThreadsServices.FACTORY);
        }

        @Override
        protected String getThreadGroupName(String threadPoolName) {
            return "EJB " + threadPoolName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/798.java