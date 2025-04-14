error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1018.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1018.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1018.java
text:
```scala
M@@odelNode toValidate = validateOpsFilter.adjustForValidation(op.clone());

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

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.jboss.as.controller.AbstractControllerService;
import org.jboss.as.controller.CompositeOperationHandler;
import org.jboss.as.controller.ControlledProcessState;
import org.jboss.as.controller.ExpressionResolver;
import org.jboss.as.controller.ModelController.OperationTransactionControl;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ProcessType;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.RunningMode;
import org.jboss.as.controller.RunningModeControl;
import org.jboss.as.controller.access.management.DelegatingConfigurableAuthorizer;
import org.jboss.as.controller.audit.AuditLogger;
import org.jboss.as.controller.client.OperationAttachments;
import org.jboss.as.controller.client.OperationMessageHandler;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.operations.global.GlobalOperationHandlers;
import org.jboss.as.controller.operations.validation.OperationValidator;
import org.jboss.as.controller.persistence.ConfigurationPersistenceException;
import org.jboss.as.controller.registry.ImmutableManagementResourceRegistration;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.transform.TransformerRegistry;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.junit.Assert;

/**
 * Internal class used by test framework.Boots up the model controller used for the test.
 * While the super class {@link AbstractControllerService} exists here in the main code source, for the legacy controllers it is got from the
 * xxxx/test-controller-xxx jars instead (see the constructor javadocs for more information)
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public abstract class ModelTestModelControllerService extends AbstractControllerService {

    private final CountDownLatch latch = new CountDownLatch(1);
    private final StringConfigurationPersister persister;
    private final TransformerRegistry transformerRegistry;
    private final ModelTestOperationValidatorFilter validateOpsFilter;
    private final RunningModeControl runningModeControl;
    private volatile ManagementResourceRegistration rootRegistration;
    private volatile Throwable error;
    private volatile boolean bootSuccess;

    /**
     * This is the constructor to use for the legacy controller using core-model-test/test-controller-7.1.x and subsystem-test/test-controller-7.1.x
     */
    protected ModelTestModelControllerService(final ProcessType processType, final RunningModeControl runningModeControl, final TransformerRegistry transformerRegistry,
                           final StringConfigurationPersister persister, final ModelTestOperationValidatorFilter validateOpsFilter,
                           final DescriptionProvider rootDescriptionProvider, ControlledProcessState processState, Controller71x version) {
        // Fails in core-model-test transformation testing if ExpressionResolver.TEST_RESOLVER is used because not present in 7.1.x
        super(processType, runningModeControl, persister,
                processState == null ? new ControlledProcessState(true) : processState, rootDescriptionProvider, null, ExpressionResolver.DEFAULT);
        this.persister = persister;
        this.transformerRegistry = transformerRegistry;
        this.validateOpsFilter = validateOpsFilter;
        this.runningModeControl = runningModeControl;
    }

    /**
     * This is the constructor to use for core-model/test-controller-7.2.x
     */
    protected ModelTestModelControllerService(final ProcessType processType, final RunningModeControl runningModeControl, final TransformerRegistry transformerRegistry,
            final StringConfigurationPersister persister, final ModelTestOperationValidatorFilter validateOpsFilter,
            final DelegatingResourceDefinition rootResourceDefinition, ControlledProcessState processState, Controller72x version) {
        super(processType, runningModeControl, persister,
                processState == null ? new ControlledProcessState(true) : processState, rootResourceDefinition, null, ExpressionResolver.DEFAULT);
        this.persister = persister;
        this.transformerRegistry = transformerRegistry;
        this.validateOpsFilter = validateOpsFilter;
        this.runningModeControl = runningModeControl;
    }

    /**
     * This is the constructor to use for subsystem-test/test-controller-7.2.x
     */
    protected ModelTestModelControllerService(final ProcessType processType, final RunningModeControl runningModeControl, final TransformerRegistry transformerRegistry,
            final StringConfigurationPersister persister, final ModelTestOperationValidatorFilter validateOpsFilter,
            final DescriptionProvider rootDescriptionProvider, ControlledProcessState processState, Controller72x version) {
        super(processType, runningModeControl, persister,
                processState == null ? new ControlledProcessState(true) : processState, rootDescriptionProvider, null, ExpressionResolver.DEFAULT);
        this.persister = persister;
        this.transformerRegistry = transformerRegistry;
        this.validateOpsFilter = validateOpsFilter;
        this.runningModeControl = runningModeControl;
    }


    /**
     * This is the constructor to use for master's core model test
     */
    protected ModelTestModelControllerService(final ProcessType processType, final RunningModeControl runningModeControl, final TransformerRegistry transformerRegistry,
            final StringConfigurationPersister persister, final ModelTestOperationValidatorFilter validateOpsFilter,
            final DelegatingResourceDefinition rootResourceDefinition, ControlledProcessState processState,
            ExpressionResolver expressionResolver, Controller80x version) {
        super(processType, runningModeControl, persister,
                processState == null ? new ControlledProcessState(true) : processState, rootResourceDefinition, null,
                expressionResolver, AuditLogger.NO_OP_LOGGER, new DelegatingConfigurableAuthorizer());
        this.persister = persister;
        this.transformerRegistry = transformerRegistry;
        this.validateOpsFilter = validateOpsFilter;
        this.runningModeControl = runningModeControl;
    }

    /**
     * THis is the constructor to
     */
    protected ModelTestModelControllerService(final ProcessType processType, final RunningModeControl runningModeControl, final TransformerRegistry transformerRegistry,
            final StringConfigurationPersister persister, final ModelTestOperationValidatorFilter validateOpsFilter,
            final DescriptionProvider rootDescriptionProvider, ControlledProcessState processState, Controller80x version) {
        // Fails in core-model-test transformation testing if ExpressionResolver.TEST_RESOLVER is used because not present in 7.1.x
        super(processType, runningModeControl, persister,
         processState == null ? new ControlledProcessState(true) : processState, rootDescriptionProvider, null, ExpressionResolver.DEFAULT);
        this.persister = persister;
        this.transformerRegistry = transformerRegistry;
        this.validateOpsFilter = validateOpsFilter;
        this.runningModeControl = runningModeControl;
    }

    public boolean isSuccessfulBoot() {
        return bootSuccess;
    }

    public Throwable getBootError() {
        return error;
    }

    RunningMode getRunningMode() {
        return runningModeControl.getRunningMode();
    }

    ProcessType getProcessType() {
        return processType;
    }

    @Override
    protected void initModel(Resource rootResource, ManagementResourceRegistration rootRegistration) {
        this.rootRegistration = rootRegistration;
        initCoreModel(rootResource, rootRegistration);
        initExtraModel(rootResource, rootRegistration);
    }

    protected void initCoreModel(Resource rootResource, ManagementResourceRegistration rootRegistration) {
        GlobalOperationHandlers.registerGlobalOperations(rootRegistration, ProcessType.STANDALONE_SERVER);

        rootRegistration.registerOperationHandler(CompositeOperationHandler.DEFINITION, CompositeOperationHandler.INSTANCE);
    }

    protected void initExtraModel(Resource rootResource, ManagementResourceRegistration rootRegistration) {
    }

    TransformerRegistry getTransformersRegistry() {
        return transformerRegistry;
    }

    @Override
    protected boolean boot(List<ModelNode> bootOperations, boolean rollbackOnRuntimeFailure) throws ConfigurationPersistenceException {
        try {
            preBoot(bootOperations, rollbackOnRuntimeFailure);
            OperationValidator validator = new OperationValidator(rootRegistration);
            for (ModelNode op : bootOperations) {
                ModelNode toValidate = validateOpsFilter.adjustForValidation(op);
                if (toValidate != null) {
                    validator.validateOperation(toValidate);
                }
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
        try {
            super.bootThreadDone();
        } finally {
            countdownDoneLatch();
        }
    }

    protected void countdownDoneLatch() {
        latch.countDown();
    }

    @Override
    public void start(StartContext context) throws StartException {
        try {
            super.start(context);
        } catch (StartException e) {
            error = e;
            e.printStackTrace();
            latch.countDown();
            throw e;
        } catch (Throwable t) {
            error = t;
            latch.countDown();
            throw new StartException(t);
        }
    }

    public void waitForSetup() throws Exception {
        latch.await();
        if (error != null) {
            if (error instanceof Exception)
                throw (Exception) error;
            throw new RuntimeException(error);
        }
    }

    public ManagementResourceRegistration getRootRegistration() {
        return rootRegistration;
    }

    /**
     * Grabs the current root resource. This cannot be called after the kernelServices
     * have been shut down
     *
     * @param kernelServices the kernel services used to access the controller
     */
    public static Resource grabRootResource(ModelTestKernelServices<?> kernelServices) {
        final AtomicReference<Resource> resourceRef = new AtomicReference<Resource>();
        ((ModelTestKernelServicesImpl<?>)kernelServices).internalExecute(new ModelNode(), new OperationStepHandler() {
            @Override
            public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
                resourceRef.set(context.readResourceFromRoot(PathAddress.EMPTY_ADDRESS, true));
                context.getResult().setEmptyObject();
                context.stepCompleted();
            }
        });
        Resource rootResource = resourceRef.get();
        Assert.assertNotNull(rootResource);
        return rootResource;
    }

    @Override
    protected ModelNode internalExecute(ModelNode operation, OperationMessageHandler handler,
            OperationTransactionControl control, OperationAttachments attachments, OperationStepHandler prepareStep) {
        return super.internalExecute(operation, handler, control, attachments, prepareStep);
    }

    public  ModelNode internalExecute(ModelNode operation, OperationStepHandler handler) {
        return internalExecute(operation, OperationMessageHandler.DISCARD, OperationTransactionControl.COMMIT, null, handler);
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

    //These are here to overload the constuctor used for the different legacy controllers

    public static class Controller71x {
        public static Controller71x INSTANCE = new Controller71x();
        private Controller71x() {
        }
    }

    public static class Controller72x {
        public static Controller72x INSTANCE = new Controller72x();
        private Controller72x() {
        }
    }

    public static class Controller80x {
        public static Controller80x INSTANCE = new Controller80x();
        private Controller80x() {
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1018.java