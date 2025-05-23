error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11983.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11983.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11983.java
text:
```scala
c@@heckReadAttributeOperationDescription(result.get(RESULT));

/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.controller.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADDRESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ATTRIBUTES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CHILDREN;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CHILD_TYPE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HOST;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MIN_LENGTH;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MIN_OCCURS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MODEL_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATIONS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATION_NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PROFILE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PROXIES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_CHILDREN_NAMES_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_CHILDREN_TYPES_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_OPERATION_DESCRIPTION_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_OPERATION_NAMES_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_DESCRIPTION_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RECURSIVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REPLY_PROPERTIES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REQUEST_PROPERTIES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REQUIRED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.TYPE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.as.controller.AbstractControllerService;
import org.jboss.as.controller.ControlledProcessState;
import org.jboss.as.controller.NewModelController;
import org.jboss.as.controller.NewOperationContext;
import org.jboss.as.controller.NewProxyController;
import org.jboss.as.controller.NewStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.client.NewModelControllerClient;
import org.jboss.as.controller.client.NewOperation;
import org.jboss.as.controller.client.NewOperationBuilder;
import org.jboss.as.controller.client.OperationAttachments;
import org.jboss.as.controller.client.OperationMessageHandler;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.common.CommonProviders;
import org.jboss.as.controller.operations.global.GlobalOperationHandlers;
import org.jboss.as.controller.operations.global.WriteAttributeHandlers;
import org.jboss.as.controller.persistence.NullConfigurationPersister;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ModelNodeRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.value.InjectedValue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Base class for tests of invocations from a main controller to a remote proxied controller.
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public abstract class AbstractProxyControllerTest {

    private ExecutorService executor;
    private NewModelControllerClient mainControllerClient;
    private NewModelControllerClient proxiedControllerClient;
    private ServiceContainer container;

    @Before
    public void setupController() throws InterruptedException {
        //System.out.println("\n------- test -------\n");
        executor = Executors.newCachedThreadPool();
        container = ServiceContainer.Factory.create("test");
        ServiceTarget target = container.subTarget();
        ControlledProcessState processState = new ControlledProcessState(true);

        ProxyModelControllerService proxyService = new ProxyModelControllerService(container, processState);
        ServiceBuilder<NewModelController> proxyBuilder = target.addService(ServiceName.of("ProxyModelController"), proxyService);
        proxyBuilder.install();

        MainModelControllerService mainService = new MainModelControllerService(container, processState);
        ServiceBuilder<NewModelController> mainBuilder = target.addService(ServiceName.of("MainModelController"), mainService);
        mainBuilder.addDependency(ServiceName.of("ProxyModelController"), NewModelController.class, mainService.proxy);
        mainBuilder.install();

        mainService.latch.await();
        mainControllerClient = mainService.getValue().createClient(executor);
        proxiedControllerClient = proxyService.getValue().createClient(executor);
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
        executor.shutdownNow();
        mainControllerClient = null;
        proxiedControllerClient = null;
    }

    @Test
    public void testRecursiveReadResourceDescriptionOperation() throws Exception {
        NewOperation operation = createOperation(READ_RESOURCE_DESCRIPTION_OPERATION);
        operation.getOperation().get(PROXIES).set(true);
        operation.getOperation().get(RECURSIVE).set(true);

        ModelNode result = proxiedControllerClient.execute(operation);

        checkHostSubModelDescription(result.get(RESULT), false);

        result = mainControllerClient.execute(operation);
        checkRootSubModelDescription(result.get(RESULT), false);

        result = mainControllerClient.execute(operation);
        checkRootSubModelDescription(result.get(RESULT), false);
    }

    @Test
    public void testRecursiveReadResourceDescriptionOperationForAddressInOtherController() throws Exception {
        NewOperation operation = createOperation(READ_RESOURCE_DESCRIPTION_OPERATION, "host", "hostA");
        operation.getOperation().get(RECURSIVE).set(true);

        ModelNode result = mainControllerClient.execute(operation);

        checkHostSubModelDescription(result.get(RESULT), false);

        result = mainControllerClient.execute(operation);
        checkHostSubModelDescription(result.get(RESULT), false);
    }

    @Test
    public void testRecursiveReadResourceDescriptionWithOperations() throws Exception {
        NewOperation operation = createOperation(READ_RESOURCE_DESCRIPTION_OPERATION);
        operation.getOperation().get(PROXIES).set(true);
        operation.getOperation().get(RECURSIVE).set(true);
        operation.getOperation().get(OPERATIONS).set(true);

        ModelNode result = mainControllerClient.execute(operation);
        checkRootSubModelDescription(result.get(RESULT), true);
    }

    @Test
    public void testRecursiveReadSubModelOperation() throws Exception {
        NewOperation operation = createOperation(READ_RESOURCE_OPERATION);
        operation.getOperation().get(PROXIES).set(true);
        operation.getOperation().get(RECURSIVE).set(true);

        ModelNode result = proxiedControllerClient.execute(operation);
        checkHostNode(result.get(RESULT));

        result = mainControllerClient.execute(operation);
        checkRootNode(result.get(RESULT));
    }

    @Test
    public void testRecursiveReadSubModelOperationForAddressInOtherController() throws Exception {
        NewOperation operation = createOperation(READ_RESOURCE_OPERATION, "host", "hostA");
        operation.getOperation().get(RECURSIVE).set(true);

        ModelNode result = mainControllerClient.execute(operation);
        checkHostNode(result.get(RESULT));
    }

    @Test
    public void testWriteAttributeOperation() throws Exception {
        NewOperation write = createOperation(WRITE_ATTRIBUTE_OPERATION, "hostchild", "hcA", "child", "childA");
        write.getOperation().get(NAME).set("value");
        write.getOperation().get(VALUE).set("NewValue");
        proxiedControllerClient.execute(write);

        NewOperation read = createOperation(READ_RESOURCE_OPERATION, "hostchild", "hcA", "child", "childA");
        read.getOperation().get(RECURSIVE).set(true);
        ModelNode result = proxiedControllerClient.execute(read);
        assertEquals("NewValue", result.get(RESULT, "value").asString());

        read = createOperation(READ_RESOURCE_OPERATION, "host", "hostA", "hostchild", "hcA", "child", "childA");
        read.getOperation().get(RECURSIVE).set(true);
        result = mainControllerClient.execute(read);
        assertEquals("NewValue", result.get(RESULT, "value").asString());
    }

    @Test
    public void testWriteAttributeOperationInChildController() throws Exception {
        NewOperation write = createOperation(WRITE_ATTRIBUTE_OPERATION, "host", "hostA", "hostchild", "hcA", "child", "childA");
        write.getOperation().get(NAME).set("value");
        write.getOperation().get(VALUE).set("NewValue2");
        ModelNode result = mainControllerClient.execute(write);

        NewOperation read = createOperation(READ_RESOURCE_OPERATION, "hostchild", "hcA", "child", "childA");
        read.getOperation().get(RECURSIVE).set(true);
        result = proxiedControllerClient.execute(read);
        assertEquals("NewValue2", result.get(RESULT, "value").asString());

        read = createOperation(READ_RESOURCE_OPERATION, "host", "hostA", "hostchild", "hcA", "child", "childA");
        read.getOperation().get(RECURSIVE).set(true);
        result = mainControllerClient.execute(read);
        assertEquals("NewValue2", result.get(RESULT, "value").asString());
    }

    @Test
    public void testWriteAttributeOperationSanity() throws Exception {
        NewOperation write = createOperation(WRITE_ATTRIBUTE_OPERATION, "hostchild", "hcA", "child", "childA");
        write.getOperation().get(NAME).set("value");
        write.getOperation().get(VALUE).set("NewValue2");
        ModelNode result = proxiedControllerClient.execute(write);

        NewOperation read = createOperation(READ_RESOURCE_OPERATION, "hostchild", "hcA", "child", "childA");
        read.getOperation().get(RECURSIVE).set(true);
        result = proxiedControllerClient.execute(read);
        assertEquals("NewValue2", result.get(RESULT, "value").asString());
    }


    @Test
    public void testReadAttributeOperationInChildController() throws Exception {
        NewOperation read = createOperation(READ_ATTRIBUTE_OPERATION, "hostchild", "hcA", "child", "childA");
        read.getOperation().get(NAME).set("name");
        ModelNode result = proxiedControllerClient.execute(read);
        assertEquals("childName", result.get(RESULT).asString());

        read.getOperation().get(NAME).set("metric");
        result = proxiedControllerClient.execute(read);
        assertEquals(ModelType.INT, result.get(RESULT).getType());
    }

    @Test
    public void testReadOperationNames() throws Exception {
        NewOperation read = createOperation(READ_OPERATION_NAMES_OPERATION);
        ModelNode result = mainControllerClient.execute(read);
        checkOperationNames(result.get(RESULT), false);

        read = createOperation(READ_OPERATION_NAMES_OPERATION, HOST, "hostA");
        result = mainControllerClient.execute(read);
        checkOperationNames(result.get(RESULT), true);

        read = createOperation(READ_OPERATION_NAMES_OPERATION,  HOST, "hostA", "hostchild", "hcA");
        result = mainControllerClient.execute(read);
        checkOperationNames(result.get(RESULT), true);
    }

    @Test
    public void testReadOperationDescription() throws Exception {
        NewOperation read = createOperation(READ_OPERATION_DESCRIPTION_OPERATION);
        read.getOperation().get(NAME).set(READ_ATTRIBUTE_OPERATION);
        ModelNode result = mainControllerClient.execute(read);
        checkReadAttributeOperationDescription(result.get(RESULT));

        read = createOperation(READ_OPERATION_DESCRIPTION_OPERATION, HOST, "hostA");
        read.getOperation().get(NAME).set(READ_ATTRIBUTE_OPERATION);
        result = mainControllerClient.execute(read);
        checkReadAttributeOperationDescription(result.get(RESULT));

        read = createOperation(READ_OPERATION_DESCRIPTION_OPERATION, HOST, "hostA", "hostchild", "hcA");
        read.getOperation().get(NAME).set(READ_ATTRIBUTE_OPERATION);
        result = mainControllerClient.execute(read);
        assertFalse(result.get(RESULT).isDefined());
    }

    @Test
    public void testReadChildNames() throws Exception {
        NewOperation read = createOperation(READ_CHILDREN_NAMES_OPERATION);
        read.getOperation().get(CHILD_TYPE).set("host");
        ModelNode result = mainControllerClient.execute(read).get(RESULT);
        assertNotNull(result);
        assertEquals(ModelType.LIST, result.getType());
        assertEquals(1, result.asList().size());
        assertEquals("hostA", result.get(0).asString());

        read = createOperation(READ_CHILDREN_NAMES_OPERATION, HOST, "hostA");
        read.getOperation().get(CHILD_TYPE).set("hostchild");
        result = mainControllerClient.execute(read).get(RESULT);
        assertNotNull(result);
        assertEquals(ModelType.LIST, result.getType());
        assertEquals(1, result.asList().size());
        assertEquals("hcA", result.get(0).asString());

        read = createOperation(READ_CHILDREN_NAMES_OPERATION, HOST, "hostA", "hostchild", "hcA");
        read.getOperation().get(CHILD_TYPE).set("child");
        result = mainControllerClient.execute(read).get(RESULT);
        assertNotNull(result);
        assertEquals(ModelType.LIST, result.getType());
        assertEquals(1, result.asList().size());
        assertEquals("childA", result.get(0).asString());

    }

    @Test
    public void testReadChildTypes() throws Exception {
        NewOperation read = createOperation(READ_CHILDREN_TYPES_OPERATION);
        ModelNode result = mainControllerClient.execute(read).get(RESULT);
        assertNotNull(result);
        assertEquals(ModelType.LIST, result.getType());
        List<ModelNode> nodes = result.asList();
        assertEquals(2, nodes.size());
        List<String> typeNames = Arrays.asList(nodes.get(0).asString(), nodes.get(1).asString());
        assertTrue(Arrays.asList("host", "profile").containsAll(typeNames));

        read = createOperation(READ_CHILDREN_TYPES_OPERATION, HOST, "hostA");
        assertNotNull(result);
        assertEquals(ModelType.LIST, result.getType());
        nodes = result.asList();
        assertEquals(2, nodes.size());
        typeNames = Arrays.asList(nodes.get(0).asString(), nodes.get(1).asString());
        assertTrue(Arrays.asList("host", "profile").containsAll(typeNames));

        read = createOperation(READ_CHILDREN_TYPES_OPERATION, HOST, "hostA", "hostchild", "hcA");
        result = mainControllerClient.execute(read).get(RESULT);
        assertNotNull(result);
        assertEquals(ModelType.LIST, result.getType());
        nodes = result.asList();
        assertEquals(1, nodes.size());
        assertEquals("child", nodes.get(0).asString());
    }

    private void checkReadAttributeOperationDescription(ModelNode result) {
        assertEquals(READ_ATTRIBUTE_OPERATION, result.get(OPERATION_NAME).asString());
        assertEquals(ModelType.STRING, result.require(REQUEST_PROPERTIES).require(NAME).require(TYPE).asType());
        assertEquals(ModelType.OBJECT, result.require(REPLY_PROPERTIES).require(TYPE).asType());
    }

    private void checkOperationNames(ModelNode operationNamesList, boolean isInHost) {
        assertTrue(operationNamesList.isDefined());
        assertEquals(ModelType.LIST, operationNamesList.getType());
        assertEquals(isInHost ? 9 : 8, operationNamesList.asList().size());
    }

    private void checkRootSubModelDescription(ModelNode result, boolean operations) {
        assertEquals("The root node of the test management API", result.get(DESCRIPTION).asString());
        assertEquals("A list of hosts", result.get(CHILDREN, HOST, DESCRIPTION).asString());
        assertEquals(1, result.get(CHILDREN, HOST, MIN_OCCURS).asInt());
        assertEquals(1, result.get(CHILDREN, HOST, MODEL_DESCRIPTION).keys().size());
        assertEquals("A list of profiles", result.get(CHILDREN, PROFILE, DESCRIPTION).asString());
        assertEquals(1, result.get(CHILDREN, PROFILE, MODEL_DESCRIPTION).keys().size());

        if (!operations) {
            assertFalse(result.has(OPERATIONS));
        } else {
            Set<String> ops = result.require(OPERATIONS).keys();
            assertTrue(ops.contains(READ_ATTRIBUTE_OPERATION));
            assertTrue(ops.contains(READ_CHILDREN_NAMES_OPERATION));
            assertTrue(ops.contains(READ_CHILDREN_TYPES_OPERATION));
            assertTrue(ops.contains(READ_OPERATION_DESCRIPTION_OPERATION));
            assertTrue(ops.contains(READ_OPERATION_NAMES_OPERATION));
            assertTrue(ops.contains(READ_RESOURCE_DESCRIPTION_OPERATION));
            assertTrue(ops.contains(READ_RESOURCE_OPERATION));
            assertTrue(ops.contains(WRITE_ATTRIBUTE_OPERATION));
            for (String op : ops) {
                assertEquals(op, result.require(OPERATIONS).require(op).require(OPERATION_NAME).asString());
            }
        }

        ModelNode proxy = result.get(CHILDREN, HOST, MODEL_DESCRIPTION, "hostA");

        checkHostSubModelDescription(proxy, operations);
    }

    private void checkHostSubModelDescription(ModelNode result, boolean operations) {
        int expectedChildren = operations ? 3 : 2;
        assertEquals(expectedChildren, result.keys().size());
        assertEquals("A host", result.get(DESCRIPTION).asString());
        assertEquals("A list of children", result.get(CHILDREN, "hostchild", DESCRIPTION).asString());
        assertEquals(1, result.get(CHILDREN, "hostchild", MIN_OCCURS).asInt());
        assertEquals(1, result.get(CHILDREN, "hostchild", MODEL_DESCRIPTION).keys().size());

        if (!operations) {
            assertFalse(result.has(OPERATIONS));
        } else {
            Set<String> ops = result.require(OPERATIONS).keys();
            assertTrue(ops.contains(READ_ATTRIBUTE_OPERATION));
            assertTrue(ops.contains(READ_CHILDREN_NAMES_OPERATION));
            assertTrue(ops.contains(READ_CHILDREN_TYPES_OPERATION));
            assertTrue(ops.contains(READ_OPERATION_DESCRIPTION_OPERATION));
            assertTrue(ops.contains(READ_OPERATION_NAMES_OPERATION));
            assertTrue(ops.contains(READ_RESOURCE_DESCRIPTION_OPERATION));
            assertTrue(ops.contains(READ_RESOURCE_OPERATION));
            assertTrue(ops.contains(WRITE_ATTRIBUTE_OPERATION));
            for (String op : ops) {
                assertEquals(op, result.require(OPERATIONS).require(op).require(OPERATION_NAME).asString());
            }
        }

        checkHostChildSubModelDescription(result.get(CHILDREN, "hostchild", MODEL_DESCRIPTION, "*"), operations);
    }

    private void checkHostChildSubModelDescription(ModelNode result, boolean operations) {
        int expectedChildren = operations ? 4 : 3;
        assertEquals(expectedChildren, result.keys().size());
        assertEquals("A host child", result.get(DESCRIPTION).asString());
        assertEquals(1, result.get(ATTRIBUTES).keys().size());
        assertEquals(ModelType.STRING, result.get(ATTRIBUTES, "name", TYPE).asType());
        assertEquals("The name of the host child", result.get(ATTRIBUTES, "name", DESCRIPTION).asString());
        assertTrue(result.get(ATTRIBUTES, "name", REQUIRED).asBoolean());
        assertEquals(1, result.get(ATTRIBUTES, "name", MIN_LENGTH).asInt());

        assertEquals(1, result.get(CHILDREN).keys().size());
        assertEquals("The children of the host child", result.get(CHILDREN, "child", DESCRIPTION).asString());
        assertEquals(1, result.get(CHILDREN, "child", MIN_OCCURS).asInt());
        assertEquals(1, result.get(CHILDREN, "child", MODEL_DESCRIPTION).keys().size());

        if (!operations) {
            assertFalse(result.has(OPERATIONS));
        } else {
            Set<String> ops = result.require(OPERATIONS).keys();
            assertTrue(ops.contains(READ_ATTRIBUTE_OPERATION));
            assertTrue(ops.contains(READ_CHILDREN_NAMES_OPERATION));
            assertTrue(ops.contains(READ_CHILDREN_TYPES_OPERATION));
            assertTrue(ops.contains(READ_OPERATION_DESCRIPTION_OPERATION));
            assertTrue(ops.contains(READ_OPERATION_NAMES_OPERATION));
            assertTrue(ops.contains(READ_RESOURCE_DESCRIPTION_OPERATION));
            assertTrue(ops.contains(READ_RESOURCE_OPERATION));
            assertTrue(ops.contains(WRITE_ATTRIBUTE_OPERATION));
            for (String op : ops) {
                assertEquals(op, result.require(OPERATIONS).require(op).require(OPERATION_NAME).asString());
            }
        }


        checkHostChildChildSubModelDescription(result.get(CHILDREN, "child", MODEL_DESCRIPTION, "*"), operations);
    }

    private void checkHostChildChildSubModelDescription(ModelNode result, boolean operations) {
        int expectedChildren = operations ? 3 : 2;
        assertEquals(expectedChildren, result.keys().size());
        assertEquals("A named set of children", result.get(DESCRIPTION).asString());
        assertEquals(2, result.get(ATTRIBUTES).keys().size());
        assertEquals(ModelType.STRING, result.get(ATTRIBUTES, "name", TYPE).asType());
        assertEquals("The name of the child", result.get(ATTRIBUTES, "name", DESCRIPTION).asString());
        assertTrue(result.get(ATTRIBUTES, "name", REQUIRED).asBoolean());
        assertEquals(1, result.get(ATTRIBUTES, "name", MIN_LENGTH).asInt());
        assertEquals(ModelType.STRING, result.get(ATTRIBUTES, "value", TYPE).asType());
        assertEquals("The value of the child", result.get(ATTRIBUTES, "value", DESCRIPTION).asString());
        assertTrue(result.get(ATTRIBUTES, "value", REQUIRED).asBoolean());
        assertEquals(1, result.get(ATTRIBUTES, "value", MIN_LENGTH).asInt());

        if (!operations) {
            assertFalse(result.has(OPERATIONS));
        } else {
            Set<String> ops = result.require(OPERATIONS).keys();
            assertTrue(ops.contains("test-op"));
            for (String op : ops) {
                assertEquals(op, result.require(OPERATIONS).require(op).require(OPERATION_NAME).asString());
            }
        }

    }

    private void checkRootNode(ModelNode result){
        assertEquals(2, result.keys().size());
        assertEquals(1, result.get("host").keys().size());
        checkHostNode(result.get("host", "hostA"));
        assertEquals(1, result.get("profile").keys().size());
        assertEquals("Profile A", result.get("profile", "profileA", NAME).asString());
    }

    private void checkHostNode(ModelNode result){
        assertEquals(1, result.keys().size());
        assertEquals("hostA", result.require("hostchild").require("hcA").require("name").asString());
        assertEquals("childName", result.get("hostchild", "hcA", "child", "childA", "name").asString());
        assertEquals("childValue", result.get("hostchild", "hcA", "child", "childA", "value").asString());
    }

    private NewOperation createOperation(String operationName, String...address) {
        ModelNode operation = new ModelNode();
        operation.get(OP).set(operationName);
        if (address.length > 0) {
            for (String addr : address) {
                operation.get(ADDRESS).add(addr);
            }
        } else {
            operation.get(ADDRESS).setEmptyList();
        }

        return new NewOperationBuilder(operation).build();
    }

    protected abstract NewProxyController createProxyController(NewModelController proxiedController, PathAddress proxyNodeAddress);

    public class MainModelControllerService extends AbstractControllerService {

        private final CountDownLatch latch = new CountDownLatch(1);
        private final InjectedValue<NewModelController> proxy = new InjectedValue<NewModelController>();

        MainModelControllerService(final ServiceContainer serviceContainer, final ControlledProcessState processState) {
            super(NewOperationContext.Type.SERVER, new NullConfigurationPersister(), processState, new DescriptionProvider() {
                @Override
                public ModelNode getModelDescription(Locale locale) {
                    ModelNode node = new ModelNode();
                    node.get(DESCRIPTION).set("The root node of the test management API");
                    node.get(CHILDREN, HOST, DESCRIPTION).set("A list of hosts");
                    node.get(CHILDREN, HOST, MIN_OCCURS).set(1);
                    node.get(CHILDREN, HOST, MODEL_DESCRIPTION);
                    node.get(CHILDREN, PROFILE, DESCRIPTION).set("A list of profiles");
                    node.get(CHILDREN, PROFILE, MODEL_DESCRIPTION);
                    return node;
                }
            }, null);
        }

        @Override
        public void start(StartContext context) throws StartException {
            super.start(context);
            latch.countDown();
        }

        protected ModelNode createCoreModel() {
            ModelNode mainModel = new ModelNode();
            mainModel.get("host", "hostA");  //Create an empty node to be got from the proxied model
            mainModel.get("profile", "profileA").get(NAME).set("Profile A");
            return mainModel;
        }

        protected void initModel(ModelNodeRegistration rootRegistration) {
            rootRegistration.registerOperationHandler(READ_RESOURCE_OPERATION, GlobalOperationHandlers.READ_RESOURCE, CommonProviders.READ_RESOURCE_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_ATTRIBUTE_OPERATION, GlobalOperationHandlers.READ_ATTRIBUTE, CommonProviders.READ_ATTRIBUTE_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_RESOURCE_DESCRIPTION_OPERATION, GlobalOperationHandlers.READ_RESOURCE_DESCRIPTION, CommonProviders.READ_RESOURCE_DESCRIPTION_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_CHILDREN_NAMES_OPERATION, GlobalOperationHandlers.READ_CHILDREN_NAMES, CommonProviders.READ_CHILDREN_NAMES_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_CHILDREN_TYPES_OPERATION, GlobalOperationHandlers.READ_CHILDREN_TYPES, CommonProviders.READ_CHILDREN_TYPES_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_OPERATION_NAMES_OPERATION, GlobalOperationHandlers.READ_OPERATION_NAMES, CommonProviders.READ_OPERATION_NAMES_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_OPERATION_DESCRIPTION_OPERATION, GlobalOperationHandlers.READ_OPERATION_DESCRIPTION, CommonProviders.READ_OPERATION_PROVIDER, true);
            rootRegistration.registerOperationHandler(WRITE_ATTRIBUTE_OPERATION, GlobalOperationHandlers.WRITE_ATTRIBUTE, CommonProviders.WRITE_ATTRIBUTE_PROVIDER, true);

            rootRegistration.registerSubModel(PathElement.pathElement("profile", "*"), new DescriptionProvider() {

                @Override
                public ModelNode getModelDescription(Locale locale) {
                    ModelNode node = new ModelNode();
                    node.get(DESCRIPTION).set("A host child");
                    node.get(ATTRIBUTES, NAME, TYPE).set(ModelType.STRING);
                    node.get(ATTRIBUTES, NAME, DESCRIPTION).set("The name of the profile");
                    node.get(ATTRIBUTES, NAME, REQUIRED).set(true);
                    return node;
                }
            });


            PathElement hostAElement = PathElement.pathElement(HOST, "hostA");
            rootRegistration.registerProxyController(hostAElement, createProxyController(proxy.getValue(), PathAddress.pathAddress(hostAElement)));
        }

    }

    public class ProxyModelControllerService extends AbstractControllerService {

        ProxyModelControllerService(final ServiceContainer serviceContainer, final ControlledProcessState processState) {
            super(NewOperationContext.Type.SERVER, new NullConfigurationPersister(), processState, new DescriptionProvider() {
                @Override
                public ModelNode getModelDescription(Locale locale) {
                    ModelNode node = new ModelNode();
                    node.get(DESCRIPTION).set("A host");
                    node.get(CHILDREN, "hostchild", DESCRIPTION).set("A list of children");
                    node.get(CHILDREN, "hostchild", MIN_OCCURS).set(1);
                    node.get(CHILDREN, "hostchild", MODEL_DESCRIPTION);
                    return node;
                }
            }, null);
        }

        @Override
        public void start(StartContext context) throws StartException {
            super.start(context);
        }

        protected ModelNode createCoreModel() {
            ModelNode proxyModel = new ModelNode();
            proxyModel.get("hostchild", "hcA", "name").set("hostA");
            proxyModel.get("hostchild", "hcA", "child", "childA", "name").set("childName");
            proxyModel.get("hostchild", "hcA", "child", "childA", "value").set("childValue");
            return proxyModel;
        }

        protected void initModel(ModelNodeRegistration rootRegistration) {
            rootRegistration.registerOperationHandler(READ_RESOURCE_OPERATION, GlobalOperationHandlers.READ_RESOURCE, CommonProviders.READ_RESOURCE_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_ATTRIBUTE_OPERATION, GlobalOperationHandlers.READ_ATTRIBUTE, CommonProviders.READ_ATTRIBUTE_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_RESOURCE_DESCRIPTION_OPERATION, GlobalOperationHandlers.READ_RESOURCE_DESCRIPTION, CommonProviders.READ_RESOURCE_DESCRIPTION_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_CHILDREN_NAMES_OPERATION, GlobalOperationHandlers.READ_CHILDREN_NAMES, CommonProviders.READ_CHILDREN_NAMES_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_CHILDREN_TYPES_OPERATION, GlobalOperationHandlers.READ_CHILDREN_TYPES, CommonProviders.READ_CHILDREN_TYPES_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_OPERATION_NAMES_OPERATION, GlobalOperationHandlers.READ_OPERATION_NAMES, CommonProviders.READ_OPERATION_NAMES_PROVIDER, true);
            rootRegistration.registerOperationHandler(READ_OPERATION_DESCRIPTION_OPERATION, GlobalOperationHandlers.READ_OPERATION_DESCRIPTION, CommonProviders.READ_OPERATION_PROVIDER, true);
            rootRegistration.registerOperationHandler(WRITE_ATTRIBUTE_OPERATION, GlobalOperationHandlers.WRITE_ATTRIBUTE, CommonProviders.WRITE_ATTRIBUTE_PROVIDER, true);
            rootRegistration.registerOperationHandler("Test",
                    new NewStepHandler() {
                        @Override
                        public void execute(NewOperationContext context, ModelNode operation) {
                            context.completeStep();
                        }
                    },
                    new DescriptionProvider() {

                        @Override
                        public ModelNode getModelDescription(Locale locale) {
                            ModelNode node = new ModelNode();
                            node.get(OPERATION_NAME).set("Test");
                            return node;
                        }
                    },
                    true);


            ModelNodeRegistration hostReg = rootRegistration.registerSubModel(PathElement.pathElement("hostchild", "*"), new DescriptionProvider() {

                @Override
                public ModelNode getModelDescription(Locale locale) {
                    ModelNode node = new ModelNode();
                    node.get(DESCRIPTION).set("A host child");
                    node.get(ATTRIBUTES, NAME, TYPE).set(ModelType.STRING);
                    node.get(ATTRIBUTES, NAME, DESCRIPTION).set("The name of the host child");
                    node.get(ATTRIBUTES, NAME, REQUIRED).set(true);
                    node.get(ATTRIBUTES, NAME, MIN_LENGTH).set(1);
                    node.get(CHILDREN, "child", DESCRIPTION).set("The children of the host child");
                    node.get(CHILDREN, "child", MIN_OCCURS).set(1);
                    node.get(CHILDREN, "child", MODEL_DESCRIPTION);
                    return node;
                }
            });

            ModelNodeRegistration hostChildReg = hostReg.registerSubModel(PathElement.pathElement("child", "*"), new DescriptionProvider() {

                @Override
                public ModelNode getModelDescription(Locale locale) {
                    ModelNode node = new ModelNode();
                    node.get(DESCRIPTION).set("A named set of children");
                    node.get(ATTRIBUTES, NAME, TYPE).set(ModelType.STRING);
                    node.get(ATTRIBUTES, NAME, DESCRIPTION).set("The name of the child");
                    node.get(ATTRIBUTES, NAME, REQUIRED).set(true);
                    node.get(ATTRIBUTES, NAME, MIN_LENGTH).set(1);
                    node.get(ATTRIBUTES, VALUE, TYPE).set(ModelType.STRING);
                    node.get(ATTRIBUTES, VALUE, DESCRIPTION).set("The value of the child");
                    node.get(ATTRIBUTES, VALUE, REQUIRED).set(true);
                    node.get(ATTRIBUTES, VALUE, MIN_LENGTH).set(1);
                    return node;
                }
            });
            hostChildReg.registerReadWriteAttribute("value", null, new WriteAttributeHandlers.ModelTypeValidatingHandler(ModelType.STRING), AttributeAccess.Storage.CONFIGURATION);
            hostChildReg.registerMetric("metric", GlobalOperationsTestCase.TestMetricHandler.INSTANCE);

            hostChildReg.registerOperationHandler("test-op",
                    new NewStepHandler() {
                        @Override
                        public void execute(NewOperationContext context, ModelNode operation) {
                            return;
                        }
                    },
                    new DescriptionProvider() {
                        @Override
                        public ModelNode getModelDescription(Locale locale) {
                            ModelNode node = new ModelNode();
                            node.get(OPERATION_NAME).set("test-op");
                            node.get(REQUEST_PROPERTIES).setEmptyObject();
                            node.get(REPLY_PROPERTIES, DESCRIPTION).setEmptyObject();
                            node.protect();

                            return node;
                        }
                    },
                    false);
        }
    }


    static class DelegatingProxyController implements NewProxyController {

        NewProxyController delegate;

        void setDelegate(NewProxyController delegate) {
            this.delegate = delegate;
        }

        @Override
        public PathAddress getProxyNodeAddress() {
            return delegate.getProxyNodeAddress();
        }

        @Override
        public void execute(ModelNode operation, OperationMessageHandler handler, ProxyOperationControl control, OperationAttachments attachments) {
            delegate.execute(operation, handler, control, attachments);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11983.java