error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/778.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/778.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/778.java
text:
```scala
public static R@@esource grabRootResource(ModelTestKernelServices<?> kernelServices) {

/*
* JBoss, Home of Professional Open Source.
* Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.model.test;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATION_NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_TRANSFORMED_RESOURCE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REPLY_PROPERTIES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REQUEST_PROPERTIES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUCCESS;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import junit.framework.Assert;

import org.jboss.as.controller.AbstractControllerService;
import org.jboss.as.controller.CompositeOperationHandler;
import org.jboss.as.controller.ControlledProcessState;
import org.jboss.as.controller.ExpressionResolver;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ProcessType;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.RunningModeControl;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.operations.global.GlobalOperationHandlers;
import org.jboss.as.controller.operations.validation.OperationValidator;
import org.jboss.as.controller.persistence.ConfigurationPersistenceException;
import org.jboss.as.controller.registry.ImmutableManagementResourceRegistration;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.transform.ReadTransformedResourceOperation;
import org.jboss.as.controller.transform.TransformerRegistry;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;

/**
 * Internal class used by test framework.
 * Boots up the model controller used for the test
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public abstract class ModelTestModelControllerService extends AbstractControllerService {

    private final CountDownLatch latch = new CountDownLatch(1);
    private final StringConfigurationPersister persister;
    protected final TransformerRegistry transformerRegistry;
    private final OperationValidation validateOps;
    private volatile ManagementResourceRegistration rootRegistration;
    private volatile Exception error;
    private volatile boolean bootSuccess;

    protected ModelTestModelControllerService(final ProcessType processType, final RunningModeControl runningModeControl, final TransformerRegistry transformerRegistry,
                           final StringConfigurationPersister persister, OperationValidation validateOps, final DescriptionProvider rootDescriptionProvider, ControlledProcessState processState) {
        super(processType, runningModeControl, persister,
                processState == null ? new ControlledProcessState(true) : processState, rootDescriptionProvider, null, ExpressionResolver.DEFAULT);
        this.persister = persister;
        this.transformerRegistry = transformerRegistry;
        this.validateOps = validateOps;
    }

    protected ModelTestModelControllerService(final ProcessType processType, final RunningModeControl runningModeControl, final TransformerRegistry transformerRegistry,
            final StringConfigurationPersister persister, final OperationValidation validateOps, final DelegatingResourceDefinition rootResourceDefinition, ControlledProcessState processState) {
        super(processType, runningModeControl, persister,
                processState == null ? new ControlledProcessState(true) : processState, rootResourceDefinition, null,
                ExpressionResolver.DEFAULT);
        this.persister = persister;
        this.transformerRegistry = transformerRegistry;
        this.validateOps = validateOps;
    }

    public boolean isSuccessfulBoot() {
        return bootSuccess;
    }

    public Throwable getBootError() {
        return error;
    }

    @Override
    protected void initModel(Resource rootResource, ManagementResourceRegistration rootRegistration) {
        this.rootRegistration = rootRegistration;
        initCoreModel(rootResource, rootRegistration);
        initExtraModel(rootResource, rootRegistration);
    }

    protected void initCoreModel(Resource rootResource, ManagementResourceRegistration rootRegistration) {
        if (transformerRegistry != null) {
            rootRegistration.registerOperationHandler(READ_TRANSFORMED_RESOURCE_OPERATION, new ReadTransformedResourceOperation(transformerRegistry), ReadTransformedResourceOperation.DESCRIPTION, true);
        }
        GlobalOperationHandlers.registerGlobalOperations(rootRegistration, ProcessType.STANDALONE_SERVER);

        rootRegistration.registerOperationHandler(CompositeOperationHandler.DEFINITION, CompositeOperationHandler.INSTANCE);

        //Handler to be able to get hold of the root resource
        rootRegistration.registerOperationHandler(ModelTestModelControllerService.RootResourceGrabber.NAME, ModelTestModelControllerService.RootResourceGrabber.INSTANCE, ModelTestModelControllerService.RootResourceGrabber.INSTANCE, false);
    }

    protected void initExtraModel(Resource rootResource, ManagementResourceRegistration rootRegistration) {
    }

    @Override
    protected boolean boot(List<ModelNode> bootOperations, boolean rollbackOnRuntimeFailure) throws ConfigurationPersistenceException {
        try {
            preBoot(bootOperations, rollbackOnRuntimeFailure);
            if (validateOps == OperationValidation.EXIT_ON_VALIDATION_ERROR) {
                new OperationValidator(rootRegistration).validateOperations(bootOperations);
            } else if (validateOps == OperationValidation.LOG_VALIDATION_ERRORS){
                new OperationValidator(rootRegistration, true, true, false).validateOperations(bootOperations);
            }
            bootSuccess = super.boot(persister.getBootOperations(), rollbackOnRuntimeFailure);
            return bootSuccess;
        } catch (Exception e) {
            error = e;
        } catch (Throwable t) {
            error = new Exception(t);
        } finally {
            postBoot();
        }
        return false;
    }

    protected void preBoot(List<ModelNode> bootOperations, boolean rollbackOnRuntimeFailure) {
    }

    protected void postBoot() {
    }

    @Override
    protected void bootThreadDone() {
        super.bootThreadDone();
        latch.countDown();
    }

    @Override
    public void start(StartContext context) throws StartException {
        try {
            super.start(context);
        } catch (StartException e) {
            error = e;
            latch.countDown();
            throw e;
        } catch (Exception e) {
            error = e;
            latch.countDown();
            throw new StartException(e);
        }
    }

    public void waitForSetup() throws Exception {
        latch.await();
        if (error != null) {
            throw error;
        }
    }

    public ManagementResourceRegistration getRootRegistration() {
        return rootRegistration;
    }

    /**
     * Grabs the current root resource
     *
     * @param kernelServices the kernel services used to access the controller
     */
    public static Resource grabRootResource(ModelTestKernelServices kernelServices) {
        ModelNode op = new ModelNode();
        op.get(OP).set(RootResourceGrabber.NAME);
        op.get(OP_ADDR).setEmptyList();
        ModelNode result = kernelServices.executeOperation(op);
        Assert.assertEquals(result.get(FAILURE_DESCRIPTION).asString(), SUCCESS, result.get(OUTCOME).asString());

        Resource rootResource = RootResourceGrabber.INSTANCE.resource;
        Assert.assertNotNull(rootResource);
        return rootResource;
    }

    static class RootResourceGrabber implements OperationStepHandler, DescriptionProvider {
        static String NAME = "grab-root-resource";
        static RootResourceGrabber INSTANCE = new RootResourceGrabber();
        volatile Resource resource;

        @Override
        public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
            resource = context.readResourceFromRoot(PathAddress.EMPTY_ADDRESS, true);
            context.getResult().setEmptyObject();
            context.stepCompleted();
        }

        @Override
        public ModelNode getModelDescription(Locale locale) {
            ModelNode node = new ModelNode();
            node.get(OPERATION_NAME).set(NAME);
            node.get(DESCRIPTION).set("Grabs the root resource");
            node.get(REQUEST_PROPERTIES).setEmptyObject();
            node.get(REPLY_PROPERTIES).setEmptyObject();
            return node;
        }
    }

    public static final DescriptionProvider DESC_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            ModelNode model = new ModelNode();
            model.get(DESCRIPTION).set("The test model controller");
            return model;
        }
    };

    public static class DelegatingResourceDefinition implements ResourceDefinition {
        private volatile ResourceDefinition delegate;

        public void setDelegate(ResourceDefinition delegate) {
            this.delegate = delegate;
        }

        @Override
        public void registerOperations(ManagementResourceRegistration resourceRegistration) {
            delegate.registerOperations(resourceRegistration);
        }

        @Override
        public void registerChildren(ManagementResourceRegistration resourceRegistration) {
            delegate.registerChildren(resourceRegistration);
        }

        @Override
        public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
            delegate.registerAttributes(resourceRegistration);
        }

        @Override
        public PathElement getPathElement() {
            return delegate.getPathElement();
        }

        @Override
        public DescriptionProvider getDescriptionProvider(ImmutableManagementResourceRegistration resourceRegistration) {
            return delegate.getDescriptionProvider(resourceRegistration);
        }
    };

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/778.java