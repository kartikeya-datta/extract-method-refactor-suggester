error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/991.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/991.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/991.java
text:
```scala
r@@ootRegistration.registerOperationHandler("composite", NewCompositeOperationHandler.INSTANCE, DESC_PROVIDER, false);

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

/**
 *
 */
package org.jboss.as.controller;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CHILD_TYPE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATION_HEADERS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATION_REQUIRES_RELOAD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATION_REQUIRES_RESTART;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PROCESS_STATE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_CHILDREN_NAMES_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_CHILDREN_RESOURCES_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_CHILDREN_TYPES_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_OPERATION_DESCRIPTION_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_OPERATION_NAMES_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_DESCRIPTION_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RECURSIVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESPONSE_HEADERS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ROLLBACK_ON_RUNTIME_FAILURE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ROLLED_BACK;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RUNTIME_UPDATE_SKIPPED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUCCESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.common.CommonProviders;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.operations.global.GlobalOperationHandlers;
import org.jboss.as.controller.persistence.ConfigurationPersistenceException;
import org.jboss.as.controller.persistence.ConfigurationPersister;
import org.jboss.as.controller.registry.ModelNodeRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests of {@link NewModelControllerImpl}.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class ModelControllerImplUnitTestCase {

    private ServiceContainer container;
    private NewModelController controller;
    private AtomicBoolean sharedState;

    public static final void toggleRuntimeState(AtomicBoolean state) {
        boolean runtimeVal = false;
        while (!state.compareAndSet(runtimeVal, !runtimeVal)) {
            runtimeVal = !runtimeVal;
        }
    }

    @Before
    public void setupController() throws InterruptedException {
        container = ServiceContainer.Factory.create("test");
        ServiceTarget target = container.subTarget();
        ControlledProcessState processState = new ControlledProcessState(true);
        ModelControllerService svc = new ModelControllerService(container, processState);
        ServiceBuilder<NewModelController> builder = target.addService(ServiceName.of("ModelController"), svc);
        builder.install();
        sharedState = svc.state;
        svc.latch.await();
        controller = svc.getValue();
        ModelNode setup = Util.getEmptyOperation("setup", new ModelNode());
        controller.execute(setup, null, null, null);
        processState.setRunning();
    }

    @After
    public void shutdownServiceContainer() {
        if (container != null) {
            container.shutdown();
            try {
                container.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                container = null;
            }
        }
    }

    /** TODO make this an AbstractControllerService subclass once we sort setting up the registry */
    public static class ModelControllerService implements Service<NewModelController> {

        final AtomicBoolean state = new AtomicBoolean(true);
        final CountDownLatch latch = new CountDownLatch(1);
        private NewModelController value;
        private final ServiceContainer serviceContainer;
        private final ControlledProcessState processState;

        ModelControllerService(final ServiceContainer serviceContainer, final ControlledProcessState processState) {
            this.serviceContainer = serviceContainer;
            this.processState = processState;
        }

        @Override
        public void start(StartContext context) throws StartException {
            ServiceTarget target = serviceContainer.subTarget();
            ServiceController<?> serviceController = context.getController();
            ModelNodeRegistration rootRegistration = ModelNodeRegistration.Factory.create(DESC_PROVIDER);

            rootRegistration.registerOperationHandler("setup", new SetupHandler(), DESC_PROVIDER, false);
            rootRegistration.registerOperationHandler("composite", new NewCompositeOperationHandler(), DESC_PROVIDER, false);
            rootRegistration.registerOperationHandler("good", new ModelStageGoodHandler(), DESC_PROVIDER, false);
            rootRegistration.registerOperationHandler("bad", new ModelStageFailsHandler(), DESC_PROVIDER, false);
            rootRegistration.registerOperationHandler("evil", new ModelStageThrowsExceptionHandler(), DESC_PROVIDER, false);
            rootRegistration.registerOperationHandler("handleFailed", new RuntimeStageFailsHandler(state), DESC_PROVIDER, false);
            rootRegistration.registerOperationHandler("runtimeException", new RuntimeStageThrowsExceptionHandler(state), DESC_PROVIDER, false);
            rootRegistration.registerOperationHandler("operationFailedException", new RuntimeStageThrowsOFEHandler(), DESC_PROVIDER, false);
            rootRegistration.registerOperationHandler("good-service", new GoodServiceHandler(), DESC_PROVIDER, false);
            rootRegistration.registerOperationHandler("bad-service", new BadServiceHandler(), DESC_PROVIDER, false);
            rootRegistration.registerOperationHandler("missing-service", new MissingServiceHandler(), DESC_PROVIDER, false);
            rootRegistration.registerOperationHandler("reload-required", new ReloadRequiredHandler(), DESC_PROVIDER, false);
            rootRegistration.registerOperationHandler("restart-required", new RestartRequiredHandler(), DESC_PROVIDER, false);

            rootRegistration.registerOperationHandler(READ_RESOURCE_OPERATION, GlobalOperationHandlers.READ_RESOURCE, CommonProviders.READ_RESOURCE_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_ATTRIBUTE_OPERATION, GlobalOperationHandlers.READ_ATTRIBUTE, CommonProviders.READ_ATTRIBUTE_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_RESOURCE_DESCRIPTION_OPERATION, GlobalOperationHandlers.READ_RESOURCE_DESCRIPTION, CommonProviders.READ_RESOURCE_DESCRIPTION_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_CHILDREN_NAMES_OPERATION, GlobalOperationHandlers.READ_CHILDREN_NAMES, CommonProviders.READ_CHILDREN_NAMES_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_CHILDREN_TYPES_OPERATION, GlobalOperationHandlers.READ_CHILDREN_TYPES, CommonProviders.READ_CHILDREN_TYPES_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_CHILDREN_RESOURCES_OPERATION, GlobalOperationHandlers.READ_CHILDREN_RESOURCES, CommonProviders.READ_CHILDREN_RESOURCES_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_OPERATION_NAMES_OPERATION, GlobalOperationHandlers.READ_OPERATION_NAMES, CommonProviders.READ_OPERATION_NAMES_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_OPERATION_DESCRIPTION_OPERATION, GlobalOperationHandlers.READ_OPERATION_DESCRIPTION, CommonProviders.READ_OPERATION_PROVIDER, true);
            rootRegistration.registerOperationHandler(WRITE_ATTRIBUTE_OPERATION, GlobalOperationHandlers.WRITE_ATTRIBUTE, CommonProviders.WRITE_ATTRIBUTE_PROVIDER, true);

            rootRegistration.registerSubModel(PathElement.pathElement("child"), DESC_PROVIDER);

            value = new NewModelControllerImpl(new ModelNode(), context.getController().getServiceContainer(), target, rootRegistration, new ContainerStateMonitor(serviceContainer, serviceController),
                    new NullConfigurationPersister(), NewOperationContext.Type.SERVER, null, processState);
            latch.countDown();
        }

        @Override
        public void stop(StopContext context) {
            value = null;
        }

        @Override
        public NewModelController getValue() throws IllegalStateException, IllegalArgumentException {
            return value;
        }
    }


    @Test
    public void testGoodModelExecution() throws Exception {
        ModelNode result = controller.execute(getOperation("good", "attr1", 5), null, null, null);
        assertEquals(SUCCESS, result.get(OUTCOME).asString());
        assertEquals(1, result.get("result").asInt());
        result = controller.execute(getOperation("good", "attr1", 1), null, null, null);
        assertEquals(SUCCESS, result.get(OUTCOME).asString());
        assertEquals(5, result.get(RESULT).asInt());
    }

    /**
     * Test successfully updating the model but then having the caller roll back the transaction.
     * @throws Exception
     */
    @Test
    public void testGoodModelExecutionTxRollback() throws Exception {
        ModelNode result = controller.execute(getOperation("good", "attr1", 5), null, RollbackTransactionControl.INSTANCE, null);
        System.out.println(result);
        // Store response data for later assertions after we check more critical stuff
        String outcome = result.get(OUTCOME).asString();
        boolean rolledback = result.get(ROLLED_BACK).asBoolean();

        // Confirm model was unchanged
        result = controller.execute(getOperation("good", "attr1", 1), null, null, null);
        assertEquals(SUCCESS, result.get(OUTCOME).asString());
        assertEquals(1, result.get(RESULT).asInt());

        // Assert the first response was as expected
        assertEquals(FAILED, outcome);  // TODO success may be valid???
        assertTrue(rolledback);
    }

    @Test
    public void testModelStageFailureExecution() throws Exception {
        ModelNode result = controller.execute(getOperation("bad", "attr1", 5), null, null, null);
        assertEquals(FAILED, result.get(OUTCOME).asString());
        assertEquals("this request is bad", result.get(FAILURE_DESCRIPTION).asString());

        // Confirm model was unchanged
        result = controller.execute(getOperation("good", "attr1", 1), null, null, null);
        assertEquals(SUCCESS, result.get(OUTCOME).asString());
        assertEquals(1, result.get(RESULT).asInt());
    }

    @Test
    public void testModelStageUnhandledFailureExecution() throws Exception {
        ModelNode result = controller.execute(getOperation("evil", "attr1", 5), null, null, null);
        assertEquals(FAILED, result.get(OUTCOME).asString());
        assertTrue(result.get(FAILURE_DESCRIPTION).toString().indexOf("this handler is evil") > -1);

        // Confirm runtime state was unchanged
        assertTrue(sharedState.get());

        // Confirm model was unchanged
        result = controller.execute(getOperation("good", "attr1", 1), null, null, null);
        assertEquals("success", result.get("outcome").asString());
        assertEquals(1, result.get("result").asInt());
    }

    @Test
    public void testHandleFailedExecution() throws Exception {
        ModelNode result = controller.execute(getOperation("handleFailed", "attr1", 5, "good", false), null, null, null);
        assertEquals(FAILED, result.get(OUTCOME).asString());
        assertEquals("handleFailed", result.get("failure-description").asString());

        // Confirm runtime state was unchanged
        assertTrue(sharedState.get());

        // Confirm model was unchanged
        result = controller.execute(getOperation("good", "attr1", 1), null, null, null);
        assertEquals("success", result.get("outcome").asString());
        assertEquals(1, result.get("result").asInt());
    }

    @Test
    public void testRuntimeStageFailedNoRollback() throws Exception {

        ModelNode op = getOperation("handleFailed", "attr1", 5, "good");
        op.get(OPERATION_HEADERS, ROLLBACK_ON_RUNTIME_FAILURE).set(false);
        ModelNode result = controller.execute(op, null, null, null);
        assertEquals(FAILED, result.get(OUTCOME).asString());
        assertEquals("handleFailed", result.get("failure-description").asString());

        // Confirm runtime state was changed
        assertFalse(sharedState.get());

        // Confirm model was changed
        result = controller.execute(getOperation("good", "attr1", 1), null, null, null);
        assertEquals("success", result.get("outcome").asString());
        assertEquals(5, result.get("result").asInt());
    }

    @Test
    public void testRuntimeStageUnhandledFailureNoRollback() throws Exception {

        ModelNode op = getOperation("runtimeException", "attr1", 5, "good");
        op.get(OPERATION_HEADERS, ROLLBACK_ON_RUNTIME_FAILURE).set(false);
        ModelNode result = controller.execute(op, null, null, null);
        assertEquals(FAILED, result.get(OUTCOME).asString());
        assertTrue(result.get("failure-description").toString().indexOf("runtime exception") > - 1);

        // Confirm runtime state was changed (handler changes it and throws exception, does not fix state)
        assertFalse(sharedState.get());

        // Confirm model was unchanged
        result = controller.execute(getOperation("good", "attr1", 1), null, null, null);
        assertEquals("success", result.get("outcome").asString());
        assertEquals(1, result.get("result").asInt());
    }

    @Test
    public void testOperationFailedExceptionNoRollback() throws Exception {

        ModelNode op = getOperation("operationFailedException", "attr1", 5, "good");
        op.get(OPERATION_HEADERS, ROLLBACK_ON_RUNTIME_FAILURE).set(false);
        ModelNode result = controller.execute(op, null, null, null);
        assertEquals(FAILED, result.get(OUTCOME).asString());
        assertTrue(result.get("failure-description").toString().indexOf("OFE") > -1);

        // Confirm model was changed
        result = controller.execute(getOperation("good", "attr1", 1), null, null, null);
        System.out.println(result);
        assertEquals("success", result.get("outcome").asString());
        assertEquals(5, result.get("result").asInt());
    }

    @Test
    public void testPathologicalRollback() throws Exception {
        ModelNode result = controller.execute(getOperation("bad", "attr1", 5), null, null, null); // don't tell it to call the 'good' op on rollback
        assertEquals(FAILED, result.get(OUTCOME).asString());
        assertEquals("this request is bad", result.get("failure-description").asString());

        // Confirm runtime state was unchanged
        assertTrue(sharedState.get());

        // Confirm model was unchanged
        result = controller.execute(getOperation("good", "attr1", 1), null, null, null);
        assertEquals("success", result.get("outcome").asString());
        assertEquals(1, result.get("result").asInt());
    }

    @Test
    public void testGoodService() throws Exception {
        ModelNode result = controller.execute(getOperation("good-service", "attr1", 5), null, null, null);
        System.out.println(result);
        assertEquals("success", result.get("outcome").asString());
        assertEquals(1, result.get("result").asInt());

        ServiceController<?> sc = container.getService(ServiceName.JBOSS.append("good-service"));
        assertNotNull(sc);
        assertEquals(ServiceController.State.UP, sc.getState());

        result = controller.execute(getOperation("good", "attr1", 1), null, null, null);
        assertEquals("success", result.get("outcome").asString());
        assertEquals(5, result.get("result").asInt());
    }

    @Test
    public void testGoodServiceTxRollback() throws Exception {
        ModelNode result = controller.execute(getOperation("good-service", "attr1", 5), null, RollbackTransactionControl.INSTANCE, null);
        System.out.println(result);
        // Store response data for later assertions after we check more critical stuff

        ServiceController<?> sc = container.getService(ServiceName.JBOSS.append("good-service"));
        if (sc != null) {
            assertEquals(ServiceController.Mode.REMOVE, sc.getMode());
        }

        // Confirm model was unchanged
        ModelNode result2 = controller.execute(getOperation("good", "attr1", 1), null, null, null);
        assertEquals(SUCCESS, result2.get(OUTCOME).asString());
        assertEquals(1, result2.get(RESULT).asInt());

        // Assert the first response was as expected
        assertEquals(FAILED, result.get(OUTCOME).asString());   // TODO success may be valid???
        assertTrue(result.get(ROLLED_BACK).asBoolean());
    }

    @Test
    public void testBadService() throws Exception {
        ModelNode result = controller.execute(getOperation("bad-service", "attr1", 5, "good"), null, null, null);
        System.out.println(result);
        assertEquals(FAILED, result.get(OUTCOME).asString());
        assertTrue(result.hasDefined(FAILURE_DESCRIPTION));

        ServiceController<?> sc = container.getService(ServiceName.JBOSS.append("bad-service"));
        if (sc != null) {
            assertEquals(ServiceController.Mode.REMOVE, sc.getMode());
        }

        // Confirm model was unchanged
        result = controller.execute(getOperation("good", "attr1", 1), null, null, null);
        assertEquals("success", result.get("outcome").asString());
        assertEquals(1, result.get("result").asInt());
    }

    @Test
    public void testMissingService() throws Exception {
        ModelNode result = controller.execute(getOperation("missing-service", "attr1", 5, "good"), null, null, null);
        System.out.println(result);
        assertEquals(FAILED, result.get(OUTCOME).asString());
        assertTrue(result.hasDefined(FAILURE_DESCRIPTION));

        ServiceController<?> sc = container.getService(ServiceName.JBOSS.append("missing-service"));
        if (sc != null) {
            assertEquals(ServiceController.Mode.REMOVE, sc.getMode());
        }

        // Confirm model was unchanged
        result = controller.execute(getOperation("good", "attr1", 1), null, null, null);
        assertEquals("success", result.get("outcome").asString());
        assertEquals(1, result.get("result").asInt());
    }

    @Test
    public void testGlobal() throws Exception {

        ModelNode operation = new ModelNode();
        operation.get(OP).set(READ_RESOURCE_OPERATION);
        operation.get(OP_ADDR).setEmptyList();
        operation.get(RECURSIVE).set(false);

        ModelNode result = controller.execute(operation, null, null, null);
        assertTrue(result.get("result").hasDefined("child"));
        assertTrue(result.get("result", "child").has("one"));
        assertFalse(result.get("result", "child").hasDefined("one"));

        operation = new ModelNode();
        operation.get(OP).set(READ_RESOURCE_OPERATION);
        operation.get(OP_ADDR).setEmptyList();
        operation.get(RECURSIVE).set(true);

        result = controller.execute(operation, null, null, null);
        assertTrue(result.get("result", "child", "one").hasDefined("attribute1"));

        operation = new ModelNode();
        operation.get(OP).set(READ_CHILDREN_NAMES_OPERATION);
        operation.get(OP_ADDR).setEmptyList();
        operation.get(CHILD_TYPE).set("child");

        result = controller.execute(operation, null, null, null).get("result");
        assertEquals("one", result.get(0).asString());
        assertEquals("two", result.get(1).asString());

        operation = new ModelNode();
        operation.get(OP).set(READ_CHILDREN_TYPES_OPERATION);
        operation.get(OP_ADDR).setEmptyList();

        result = controller.execute(operation, null, null, null).get("result");
        assertEquals("child", result.get(0).asString());

        operation = new ModelNode();
        operation.get(OP).set(READ_CHILDREN_RESOURCES_OPERATION);
        operation.get(OP_ADDR).setEmptyList();
        operation.get(CHILD_TYPE).set("child");
    }

    public void testReloadRequired() throws Exception {
        ModelNode result = controller.execute(getOperation("reload-required", "attr1", 5), null, null, null);
        System.out.println(result);
        assertEquals(SUCCESS, result.get(OUTCOME).asString());
        assertTrue(result.get(RESPONSE_HEADERS, RUNTIME_UPDATE_SKIPPED).asBoolean());
        assertTrue(result.get(RESPONSE_HEADERS, OPERATION_REQUIRES_RELOAD).asBoolean());
        assertEquals(ControlledProcessState.State.RELOAD_REQUIRED.toString(), result.get(RESPONSE_HEADERS, PROCESS_STATE).asString());

        result = controller.execute(getOperation("good", "attr1", 1), null, null, null);
        assertEquals("success", result.get("outcome").asString());
        assertEquals(5, result.get("result").asInt());
    }

    @Test
    public void testRestartRequired() throws Exception {
        ModelNode result = controller.execute(getOperation("restart-required", "attr1", 5), null, null, null);
        System.out.println(result);
        assertEquals(SUCCESS, result.get(OUTCOME).asString());
        assertTrue(result.get(RESPONSE_HEADERS, RUNTIME_UPDATE_SKIPPED).asBoolean());
        assertTrue(result.get(RESPONSE_HEADERS, OPERATION_REQUIRES_RESTART).asBoolean());
        assertEquals(ControlledProcessState.State.RESTART_REQUIRED.toString(), result.get(RESPONSE_HEADERS, PROCESS_STATE).asString());

        result = controller.execute(getOperation("good", "attr1", 1), null, null, null);
        assertEquals("success", result.get("outcome").asString());
        assertEquals(5, result.get("result").asInt());
    }

    @Test
    public void testReloadRequiredTxRollback() throws Exception {
        ModelNode result = controller.execute(getOperation("reload-required", "attr1", 5), null, RollbackTransactionControl.INSTANCE, null);
        System.out.println(result);
        assertEquals(FAILED, result.get(OUTCOME).asString());
        assertTrue(result.get(RESPONSE_HEADERS, RUNTIME_UPDATE_SKIPPED).asBoolean());
        assertFalse(result.get(RESPONSE_HEADERS).hasDefined(OPERATION_REQUIRES_RELOAD));
        assertFalse(result.get(RESPONSE_HEADERS).hasDefined(PROCESS_STATE));

        // Confirm model was unchanged
        result = controller.execute(getOperation("good", "attr1", 1), null, null, null);
        assertEquals("success", result.get("outcome").asString());
        assertEquals(1, result.get("result").asInt());
    }

    @Test
    public void testRestartRequiredTxRollback() throws Exception {
        ModelNode result = controller.execute(getOperation("restart-required", "attr1", 5), null, RollbackTransactionControl.INSTANCE, null);
        System.out.println(result);
        assertEquals(FAILED, result.get(OUTCOME).asString());
        assertTrue(result.get(RESPONSE_HEADERS, RUNTIME_UPDATE_SKIPPED).asBoolean());
        assertFalse(result.get(RESPONSE_HEADERS).hasDefined(OPERATION_REQUIRES_RESTART));
        assertFalse(result.get(RESPONSE_HEADERS).hasDefined(PROCESS_STATE));

        // Confirm model was unchanged
        result = controller.execute(getOperation("good", "attr1", 1), null, null, null);
        assertEquals("success", result.get("outcome").asString());
        assertEquals(1, result.get("result").asInt());
    }

    public static ModelNode getOperation(String opName, String attr, int val) {
        return getOperation(opName, attr, val, null, false);
    }
    public static ModelNode getOperation(String opName, String attr, int val, String rollbackName) {
        return getOperation(opName, attr, val, rollbackName, false);
    }

    public static ModelNode getOperation(String opName, String attr, int val, String rollbackName, boolean async) {
        ModelNode op = new ModelNode();
        op.get(OP).set(opName);
        op.get(OP_ADDR).setEmptyList();
        op.get(NAME).set(attr);
        op.get(VALUE).set(val);
        op.get("rollbackName").set(rollbackName == null ? opName : rollbackName);

        if (async) {
            op.get("async").set(true);
        }
        return op;
    }

    public static class SetupHandler implements NewStepHandler {

        @Override
        public void execute(NewOperationContext context, ModelNode operation) {
            ModelNode model = new ModelNode();

            //Atttributes
            model.get("attr1").set(1);
            model.get("attr2").set(2);

            model.get("child", "one", "attribute1").set(1);
            model.get("child", "two", "attribute2").set(2);

            context.writeModel(PathAddress.EMPTY_ADDRESS, model);

            context.completeStep();
        }
    }

    public static class ModelStageGoodHandler implements NewStepHandler {

        @Override
        public void execute(NewOperationContext context, ModelNode operation) {

            String name = operation.require(NAME).asString();
            ModelNode model = context.readModelForUpdate(PathAddress.EMPTY_ADDRESS);
            ModelNode attr = model.get(name);
            final int current = attr.asInt();
            attr.set(operation.require(VALUE));

            context.getResult().set(current);

            context.completeStep();
        }
    }

    public static class ModelStageFailsHandler implements NewStepHandler {

        @Override
        public void execute(NewOperationContext context, ModelNode operation) {

            String name = operation.require(NAME).asString();
            ModelNode model = context.readModelForUpdate(PathAddress.EMPTY_ADDRESS);
            ModelNode attr = model.get(name);
            attr.set(operation.require(VALUE));

            context.getFailureDescription().set("this request is bad");

            context.completeStep();
        }
    }

    public static class ModelStageThrowsExceptionHandler implements NewStepHandler {

        @Override
        public void execute(NewOperationContext context, ModelNode operation) {

            String name = operation.require(NAME).asString();
            ModelNode model = context.readModelForUpdate(PathAddress.EMPTY_ADDRESS);
            ModelNode attr = model.get(name);
            attr.set(operation.require(VALUE));

            throw new RuntimeException("this handler is evil");
        }
    }

    public static class RuntimeStageFailsHandler implements NewStepHandler {

        private final AtomicBoolean state;

        public RuntimeStageFailsHandler(AtomicBoolean state) {
            this.state = state;
        }

        @Override
        public void execute(NewOperationContext context, ModelNode operation) {

            String name = operation.require("name").asString();
            ModelNode attr = context.readModelForUpdate(PathAddress.EMPTY_ADDRESS).get(name);
            int current = attr.asInt();
            attr.set(operation.require("value"));

            context.getResult().set(current);

            context.addStep(new NewStepHandler() {

                @Override
                public void execute(NewOperationContext context, ModelNode operation) {
                    toggleRuntimeState(state);
                    context.getFailureDescription().set("handleFailed");
                    if (context.completeStep() == NewOperationContext.ResultAction.ROLLBACK) {
                        toggleRuntimeState(state);
                    }
                }
            }, NewOperationContext.Stage.RUNTIME);

            context.completeStep();
        }
    }

    public static class RuntimeStageThrowsExceptionHandler implements NewStepHandler {

        private final AtomicBoolean state;

        public RuntimeStageThrowsExceptionHandler(AtomicBoolean state) {
            this.state = state;
        }

        @Override
        public void execute(NewOperationContext context, ModelNode operation) {

            String name = operation.require("name").asString();
            ModelNode attr = context.readModelForUpdate(PathAddress.EMPTY_ADDRESS).get(name);
            int current = attr.asInt();
            attr.set(operation.require("value"));

            context.getResult().set(current);

            context.addStep(new NewStepHandler() {

                @Override
                public void execute(NewOperationContext context, ModelNode operation) {
                    toggleRuntimeState(state);
                    throw new RuntimeException("runtime exception");
                }
            }, NewOperationContext.Stage.RUNTIME);

            context.completeStep();
        }
    }

    public static class RuntimeStageThrowsOFEHandler implements NewStepHandler {

        @Override
        public void execute(NewOperationContext context, ModelNode operation) {

            String name = operation.require("name").asString();
            ModelNode attr = context.readModelForUpdate(PathAddress.EMPTY_ADDRESS).get(name);
            int current = attr.asInt();
            attr.set(operation.require("value"));

            context.getResult().set(current);

            context.addStep(new NewStepHandler() {

                @Override
                public void execute(NewOperationContext context, ModelNode operation) throws OperationFailedException {
                    throw new OperationFailedException(new ModelNode().set("OFE"));
                }
            }, NewOperationContext.Stage.RUNTIME);

            context.completeStep();
        }
    }

    public static class GoodServiceHandler implements NewStepHandler {
        @Override
        public void execute(NewOperationContext context, ModelNode operation) {

            String name = operation.require("name").asString();
            ModelNode attr = context.readModelForUpdate(PathAddress.EMPTY_ADDRESS).get(name);
            final int current = attr.asInt();
            attr.set(operation.require("value"));

            context.addStep(new NewStepHandler() {

                @Override
                public void execute(NewOperationContext context, ModelNode operation) {

                    context.getResult().set(current);
                    ServiceName svcName =  ServiceName.JBOSS.append("good-service");
                    ServiceVerificationHandler verificationHandler = new ServiceVerificationHandler();
                    context.getServiceTarget().addService(svcName, Service.NULL)
                            .addListener(verificationHandler)
                            .install();
                    context.addStep(verificationHandler, NewOperationContext.Stage.VERIFY);
                    if (context.completeStep() == NewOperationContext.ResultAction.ROLLBACK) {
                        context.removeService(svcName);
                    }
                }
            }, NewOperationContext.Stage.RUNTIME);

            context.completeStep();
        }
    }

    public static class MissingServiceHandler implements NewStepHandler {
        @Override
        public void execute(NewOperationContext context, ModelNode operation) {

            String name = operation.require("name").asString();
            ModelNode attr = context.readModelForUpdate(PathAddress.EMPTY_ADDRESS).get(name);
            final int current = attr.asInt();
            attr.set(operation.require("value"));

            context.addStep(new NewStepHandler() {

                @Override
                public void execute(NewOperationContext context, ModelNode operation) {

                    context.getResult().set(current);

                    ServiceName svcName = ServiceName.JBOSS.append("missing-service");
                    ServiceVerificationHandler verificationHandler = new ServiceVerificationHandler();
                    context.getServiceTarget().addService(svcName, Service.NULL)
                            .addDependency(ServiceName.JBOSS.append("missing"))
                            .addListener(verificationHandler)
                            .install();
                    context.addStep(verificationHandler, NewOperationContext.Stage.VERIFY);
                    if (context.completeStep() == NewOperationContext.ResultAction.ROLLBACK) {
                        context.removeService(svcName);
                    }
                }
            }, NewOperationContext.Stage.RUNTIME);

            context.completeStep();
        }
    }

    public static class BadServiceHandler implements NewStepHandler {
        @Override
        public void execute(NewOperationContext context, ModelNode operation) {

            String name = operation.require("name").asString();
            ModelNode attr = context.readModelForUpdate(PathAddress.EMPTY_ADDRESS).get(name);
            final int current = attr.asInt();
            attr.set(operation.require("value"));

            context.addStep(new NewStepHandler() {

                @Override
                public void execute(NewOperationContext context, ModelNode operation) {

                    context.getResult().set(current);


                    Service<Void> bad = new Service<Void>() {

                        @Override
                        public Void getValue() throws IllegalStateException, IllegalArgumentException {
                            return null;
                        }

                        @Override
                        public void start(StartContext context) throws StartException {
                            throw new RuntimeException("Bad service!");
                        }

                        @Override
                        public void stop(StopContext context) {
                        }

                    };
                    ServiceName svcName = ServiceName.JBOSS.append("bad-service");
                    ServiceVerificationHandler verificationHandler = new ServiceVerificationHandler();
                    context.getServiceTarget().addService(svcName, bad)
                            .addListener(verificationHandler)
                            .install();
                    context.addStep(verificationHandler, NewOperationContext.Stage.VERIFY);
                    if (context.completeStep() == NewOperationContext.ResultAction.ROLLBACK) {
                        context.removeService(svcName);
                    }
                }
            }, NewOperationContext.Stage.RUNTIME);

            context.completeStep();
        }
    }

    public static class ReloadRequiredHandler implements NewStepHandler {
        @Override
        public void execute(NewOperationContext context, ModelNode operation) {

            String name = operation.require(NAME).asString();
            ModelNode model = context.readModelForUpdate(PathAddress.EMPTY_ADDRESS);
            ModelNode attr = model.get(name);
            final int current = attr.asInt();
            attr.set(operation.require(VALUE));

            context.getResult().set(current);

            context.runtimeUpdateSkipped();
            context.reloadRequired();
            if (context.completeStep() == NewOperationContext.ResultAction.ROLLBACK) {
                context.revertReloadRequired();
            }
        }
    }

    public static class RestartRequiredHandler implements NewStepHandler {
        @Override
        public void execute(NewOperationContext context, ModelNode operation) {

            String name = operation.require(NAME).asString();
            ModelNode model = context.readModelForUpdate(PathAddress.EMPTY_ADDRESS);
            ModelNode attr = model.get(name);
            final int current = attr.asInt();
            attr.set(operation.require(VALUE));

            context.getResult().set(current);

            context.runtimeUpdateSkipped();
            context.restartRequired();
            if (context.completeStep() == NewOperationContext.ResultAction.ROLLBACK) {
                context.revertRestartRequired();
            }
        }
    }

    public static final DescriptionProvider DESC_PROVIDER = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return new ModelNode();
        }
    };

    static class RollbackTransactionControl implements NewModelController.OperationTransactionControl {

        static final RollbackTransactionControl INSTANCE = new RollbackTransactionControl();

        @Override
        public void operationPrepared(NewModelController.OperationTransaction transaction, ModelNode result) {
            transaction.rollback();
        }
    }


    private static class NullConfigurationPersister implements ConfigurationPersister{

        @Override
        public void store(ModelNode model) throws ConfigurationPersistenceException {
        }

        @Override
        public void marshallAsXml(ModelNode model, OutputStream output) throws ConfigurationPersistenceException {
        }

        @Override
        public List<ModelNode> load() throws ConfigurationPersistenceException {
            return null;
        }

        @Override
        public void successfulBoot() throws ConfigurationPersistenceException {
        }

        @Override
        public SnapshotInfo listSnapshots() {
            return NULL_SNAPSHOT_INFO;
        }

        @Override
        public String snapshot() {
            return null;
        }

        @Override
        public void deleteSnapshot(String name) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/991.java