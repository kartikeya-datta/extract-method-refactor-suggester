error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8022.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8022.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8022.java
text:
```scala
r@@eturn "Batch Thread";

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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

package org.wildfly.extension.batch;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;

import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.DefaultAttributeMarshaller;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ReloadRequiredRemoveStepHandler;
import org.jboss.as.controller.ReloadRequiredWriteAttributeHandler;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.descriptions.StandardResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.GenericSubsystemDescribeHandler;
import org.jboss.as.controller.operations.validation.EnumValidator;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.as.server.deployment.Phase;
import org.jboss.as.threads.ThreadFactoryResolver;
import org.jboss.as.threads.ThreadFactoryResourceDefinition;
import org.jboss.as.threads.ThreadsServices;
import org.jboss.as.threads.UnboundedQueueThreadPoolResourceDefinition;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.msc.service.ServiceController;
import org.wildfly.extension.batch.deployment.BatchDependencyProcessor;
import org.wildfly.extension.batch.deployment.BatchEnvironmentProcessor;
import org.wildfly.jberet.BatchConfiguration;
import org.wildfly.jberet.BatchConfiguration.JobRepositoryType;

class BatchSubsystemDefinition extends SimpleResourceDefinition {

    /**
     * The name of our subsystem within the model.
     */
    public static final String NAME = "batch";
    static final PathElement SUBSYSTEM_PATH = PathElement.pathElement(SUBSYSTEM, NAME);
    static final PathElement THREAD_POOL_PATH = PathElement.pathElement(BatchConstants.THREAD_POOL, BatchConstants.THREAD_POOL_NAME);
    private static final String RESOURCE_NAME = BatchSubsystemExtension.class.getPackage().getName() + ".LocalDescriptions";


    static StandardResourceDescriptionResolver getResourceDescriptionResolver(final String keyPrefix) {
        String prefix = NAME + (keyPrefix == null ? "" : "." + keyPrefix);
        return new StandardResourceDescriptionResolver(prefix, RESOURCE_NAME, BatchSubsystemExtension.class.getClassLoader(), true, false);
    }

    static StandardResourceDescriptionResolver getResourceDescriptionResolver(final String... prefixes) {
        final StringBuilder prefix = new StringBuilder(NAME);
        for (String p : prefixes) {
            prefix.append('.').append(p);
        }
        return new StandardResourceDescriptionResolver(prefix.toString(), RESOURCE_NAME, BatchSubsystemExtension.class.getClassLoader(), true, false);
    }

    static final SimpleAttributeDefinition JOB_REPOSITORY_TYPE = SimpleAttributeDefinitionBuilder.create("job-repository-type", ModelType.STRING, true)
            .setAllowExpression(false)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                    marshallAsElement(attribute, resourceModel, true, writer);
                }

                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {
                    // Write the job-repository
                    writer.writeStartElement(Element.JOB_REPOSITORY.getLocalName());
                    // The value is the job repository type
                    final String value = resourceModel.get(JOB_REPOSITORY_TYPE.getName()).asString();
                    if (JobRepositoryType.JDBC.toString().equals(value)) {
                        writer.writeStartElement(Element.JDBC.getLocalName());
                        final PathElement jdbcPath = JobRepositoryDefinition.JDBC.getPathElement();
                        final ModelNode jdbcModel = resourceModel.clone().get(jdbcPath.getKey(), jdbcPath.getValue());
                        if (jdbcModel.isDefined()) {
                            JobRepositoryDefinition.JNDI_NAME.marshallAsAttribute(jdbcModel, false, writer);
                        }
                        writer.writeEndElement();
                    } else {
                        // Write in-memory by default
                        writer.writeStartElement(Element.IN_MEMORY.getLocalName());
                        writer.writeEndElement();
                    }
                    writer.writeEndElement();
                }
            })
            .setDefaultValue(new ModelNode(JobRepositoryType.IN_MEMORY.toString()))
            .setValidator(new EnumValidator<>(JobRepositoryType.class, true, true))
            .setRestartJVM()
            .build();

    public static final BatchSubsystemDefinition INSTANCE = new BatchSubsystemDefinition();

    private BatchSubsystemDefinition() {
        super(SUBSYSTEM_PATH, getResourceDescriptionResolver((String) null), BatchSubsystemAdd.INSTANCE,
                ReloadRequiredRemoveStepHandler.INSTANCE);
    }

    @Override
    public void registerOperations(final ManagementResourceRegistration resourceRegistration) {
        super.registerOperations(resourceRegistration);
        resourceRegistration.registerOperationHandler(GenericSubsystemDescribeHandler.DEFINITION, GenericSubsystemDescribeHandler.INSTANCE);
    }

    @Override
    public void registerChildren(final ManagementResourceRegistration resourceRegistration) {
        super.registerChildren(resourceRegistration);
        // thread-pool resource
        final UnboundedQueueThreadPoolResourceDefinition threadPoolResource = UnboundedQueueThreadPoolResourceDefinition.create(THREAD_POOL_PATH,
                BatchThreadFactoryResolver.INSTANCE, BatchServiceNames.BASE_BATCH_THREAD_POOL_NAME, false); // TODO (jrp) verify false value
        resourceRegistration.registerSubModel(threadPoolResource);

        // thread-factory resource
        final ThreadFactoryResourceDefinition threadFactoryResource = new ThreadFactoryResourceDefinition();
        resourceRegistration.registerSubModel(threadFactoryResource);

        resourceRegistration.registerSubModel(JobRepositoryDefinition.JDBC);
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
        resourceRegistration.registerReadWriteAttribute(JOB_REPOSITORY_TYPE, null, new ReloadRequiredWriteAttributeHandler(JOB_REPOSITORY_TYPE));
    }


    private static class BatchThreadFactoryResolver extends ThreadFactoryResolver.SimpleResolver {
        static final BatchThreadFactoryResolver INSTANCE = new BatchThreadFactoryResolver();

        private BatchThreadFactoryResolver() {
            super(ThreadsServices.FACTORY);
        }

        @Override
        protected String getThreadGroupName(String threadPoolName) {
            return "batch-" + threadPoolName;
        }
    }

    /**
     * Handler responsible for adding the subsystem resource to the model.
     */
    static class BatchSubsystemAdd extends AbstractAddStepHandler {

        static final BatchSubsystemAdd INSTANCE = new BatchSubsystemAdd();

        private BatchSubsystemAdd() {
        }

        @Override
        protected void populateModel(final ModelNode operation, final ModelNode model) throws OperationFailedException {
            model.setEmptyObject();
            JOB_REPOSITORY_TYPE.validateAndSet(operation, model);
        }

        @Override
        protected void performRuntime(final OperationContext context, final ModelNode operation, final ModelNode model,
                                      final ServiceVerificationHandler verificationHandler, final List<ServiceController<?>> newControllers)
                throws OperationFailedException {

            context.addStep(new AbstractDeploymentChainStep() {
                public void execute(DeploymentProcessorTarget processorTarget) {
                    processorTarget.addDeploymentProcessor(BatchSubsystemDefinition.NAME,
                            Phase.DEPENDENCIES, Phase.DEPENDENCIES_BATCH, new BatchDependencyProcessor());

                }
            }, OperationContext.Stage.RUNTIME);

            context.addStep(new AbstractDeploymentChainStep() {
                public void execute(DeploymentProcessorTarget processorTarget) {
                    processorTarget.addDeploymentProcessor(BatchSubsystemDefinition.NAME,
                            Phase.POST_MODULE, Phase.POST_MODULE_BATCH_ENVIRONMENT, new BatchEnvironmentProcessor());

                }
            }, OperationContext.Stage.RUNTIME);

            // Create the BatchEnvironment
            final BatchConfiguration batchConfiguration = BatchConfiguration.getInstance();
            // Determine the repository type
            final String repositoryType = JOB_REPOSITORY_TYPE.resolveModelAttribute(context, model).asString();
            batchConfiguration.setJobRepositoryType(repositoryType);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8022.java