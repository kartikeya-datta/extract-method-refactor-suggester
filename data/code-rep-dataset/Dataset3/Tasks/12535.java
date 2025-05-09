rootRegistration.registerOperationHandler(TestUtils.SETUP_OPERATION_DEF, new OperationStepHandler() {

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
package org.jboss.as.controller.test;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ACCESS_TYPE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ATTRIBUTES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CHILDREN;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MIN_LENGTH;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MODEL_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NILLABLE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATIONS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATION_NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PROFILE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_CHILDREN_NAMES_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_CHILDREN_RESOURCES_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_CHILDREN_TYPES_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_OPERATION_DESCRIPTION_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_OPERATION_NAMES_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_DESCRIPTION_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.TYPE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE_TYPE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.ModelOnlyWriteAttributeHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ProcessType;
import org.jboss.as.controller.ResourceBuilder;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.descriptions.NonResolvingResourceDescriptionResolver;
import org.jboss.as.controller.operations.global.GlobalOperationHandlers;
import org.jboss.as.controller.registry.AttributeAccess.AccessType;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.dmr.Property;
import org.jboss.dmr.ValueExpression;

/**
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public abstract class AbstractGlobalOperationsTestCase extends AbstractControllerTestBase {

    private final AccessType expectedRwAttributeAccess;

    protected AbstractGlobalOperationsTestCase() {
        super();
        this.expectedRwAttributeAccess = AccessType.READ_WRITE;
    }

    protected AbstractGlobalOperationsTestCase(ProcessType processType, AccessType expectedRwAttributeAccess) {
        super(processType);
        this.expectedRwAttributeAccess = expectedRwAttributeAccess;
    }

    @Override
    protected void initModel(Resource rootResource, ManagementResourceRegistration rootRegistration) {
        GlobalOperationHandlers.registerGlobalOperations(rootRegistration, processType);
        rootRegistration.registerOperationHandler(TestUtils.SETUP_OP_DEF, new OperationStepHandler() {
                    @Override
                    public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
                        final ModelNode model = new ModelNode();
                        //Atttributes
                        model.get("profile", "profileA", "subsystem", "subsystem1", "attr1").add(1);
                        model.get("profile", "profileA", "subsystem", "subsystem1", "attr1").add(2);
                        //Children
                        model.get("profile", "profileA", "subsystem", "subsystem1", "type1", "thing1", "name").set("Name11");
                        model.get("profile", "profileA", "subsystem", "subsystem1", "type1", "thing1", "value").set("201");
                        model.get("profile", "profileA", "subsystem", "subsystem1", "type1", "thing2", "name").set("Name12");
                        model.get("profile", "profileA", "subsystem", "subsystem1", "type1", "thing2", "value").set("202");
                        model.get("profile", "profileA", "subsystem", "subsystem1", "type2", "other", "name").set("Name2");


                        model.get("profile", "profileA", "subsystem", "subsystem2", "bigdecimal").set(new BigDecimal(100));
                        model.get("profile", "profileA", "subsystem", "subsystem2", "biginteger").set(new BigInteger("101"));
                        model.get("profile", "profileA", "subsystem", "subsystem2", "boolean").set(true);
                        model.get("profile", "profileA", "subsystem", "subsystem2", "bytes").set(new byte[]{1, 2, 3});
                        model.get("profile", "profileA", "subsystem", "subsystem2", "double").set(Double.MAX_VALUE);
                        model.get("profile", "profileA", "subsystem", "subsystem2", "expression").set(new ValueExpression("{expr}"));
                        model.get("profile", "profileA", "subsystem", "subsystem2", "int").set(102);
                        model.get("profile", "profileA", "subsystem", "subsystem2", "list").add("l1A");
                        model.get("profile", "profileA", "subsystem", "subsystem2", "list").add("l1B");
                        model.get("profile", "profileA", "subsystem", "subsystem2", "long").set(Long.MAX_VALUE);
                        model.get("profile", "profileA", "subsystem", "subsystem2", "object", "value").set("objVal");
                        model.get("profile", "profileA", "subsystem", "subsystem2", "property").set(new Property("prop1", new ModelNode().set("value1")));
                        model.get("profile", "profileA", "subsystem", "subsystem2", "string1").set("s1");
                        model.get("profile", "profileA", "subsystem", "subsystem2", "string2").set("s2");
                        model.get("profile", "profileA", "subsystem", "subsystem2", "type").set(ModelType.TYPE);


                        model.get("profile", "profileB", "name").set("Profile B");

                        model.get("profile", "profileC", "subsystem", "subsystem4");
                        model.get("profile", "profileC", "subsystem", "subsystem5", "name").set("Test");

                        createModel(context, model);

                        context.stepCompleted();
                    }
                }
        );

        ResourceDefinition profileDef = ResourceBuilder.Factory.create(PathElement.pathElement("profile", "*"),
                new NonResolvingResourceDescriptionResolver())
                .addReadOnlyAttribute(SimpleAttributeDefinitionBuilder.create("name", ModelType.STRING, false).setMinSize(1).build())
                .build();


        ManagementResourceRegistration profileReg = rootRegistration.registerSubModel(profileDef);

        ManagementResourceRegistration profileSub1Reg = profileReg.registerSubModel(new Subsystem1RootResource());

        ManagementResourceRegistration profileASub2Reg = profileReg.registerSubModel(
                new SimpleResourceDefinition(PathElement.pathElement("subsystem", "subsystem2"), new NonResolvingResourceDescriptionResolver()));
        AttributeDefinition longAttr = TestUtils.createAttribute("long", ModelType.LONG);
        profileASub2Reg.registerReadWriteAttribute(longAttr, null, new ModelOnlyWriteAttributeHandler(longAttr));


        ManagementResourceRegistration profileBSub3Reg = profileReg.registerSubModel(
                new SimpleResourceDefinition(PathElement.pathElement("subsystem", "subsystem3"), new NonResolvingResourceDescriptionResolver()));

        profileSub1Reg.registerOperationHandler(TestUtils.createOperationDefinition("testA1-1", TestUtils.createAttribute("paramA1", ModelType.INT)),
                new OperationStepHandler() {
                    @Override
                    public void execute(OperationContext context, ModelNode operation) {
                    }
                }
        );

        profileSub1Reg.registerOperationHandler(TestUtils.createOperationDefinition("testA1-2", TestUtils.createAttribute("paramA2", ModelType.STRING)),
                new OperationStepHandler() {
                    @Override
                    public void execute(OperationContext context, ModelNode operation) {
                    }
                }
        );


        profileASub2Reg.registerOperationHandler(TestUtils.createOperationDefinition("testA2", TestUtils.createAttribute("paramB", ModelType.LONG)),
                new OperationStepHandler() {

                    @Override
                    public void execute(OperationContext context, ModelNode operation) {
                    }
                }
        );

        ManagementResourceRegistration profileCSub4Reg = profileReg.registerSubModel(
                new SimpleResourceDefinition(PathElement.pathElement("subsystem", "subsystem4"), new NonResolvingResourceDescriptionResolver()));

        ManagementResourceRegistration profileCSub5Reg = profileReg.registerSubModel(
                new SimpleResourceDefinition(PathElement.pathElement("subsystem", "subsystem5"), new NonResolvingResourceDescriptionResolver()));

        profileCSub5Reg.registerReadOnlyAttribute(TestUtils.createAttribute("name", ModelType.STRING), new OperationStepHandler() {

            @Override
            public void execute(OperationContext context, ModelNode operation) {
                context.getResult().set("Overridden by special read handler");
                context.stepCompleted();
            }
        });

        ResourceDefinition profileCSub5Type1RegDef = ResourceBuilder.Factory.create(PathElement.pathElement("type1", "thing1"),
                new NonResolvingResourceDescriptionResolver())
                .build();
        ManagementResourceRegistration profileCSub5Type1Reg = profileCSub5Reg.registerSubModel(profileCSub5Type1RegDef);

        ManagementResourceRegistration profileCSub6Reg = profileReg.registerSubModel(
                new SimpleResourceDefinition(PathElement.pathElement("subsystem", "subsystem6"), new NonResolvingResourceDescriptionResolver()));

        profileCSub6Reg.registerOperationHandler(TestUtils.createOperationDefinition("testA", true),
                new OperationStepHandler() {

                    @Override
                    public void execute(OperationContext context, ModelNode operation) {
                    }
                }
        );
    }

    /**
     * Override to get the actual result from the response.
     */
    public ModelNode executeForResult(ModelNode operation) throws OperationFailedException {
        ModelNode rsp = getController().execute(operation, null, null, null);
        if (FAILED.equals(rsp.get(OUTCOME).asString())) {
            throw new OperationFailedException(rsp.get(FAILURE_DESCRIPTION));
        }
        return rsp.get(RESULT);
    }

    static class TestMetricHandler implements OperationStepHandler {
        static final TestMetricHandler INSTANCE = new TestMetricHandler();
        private static final Random random = new Random();

        @Override
        public void execute(final OperationContext context, final ModelNode operation) {
            context.getResult().set(random.nextInt());
            context.stepCompleted();
        }

    }

    protected void checkRootNodeDescription(ModelNode result, boolean recursive, boolean operations) {
        assertEquals("description", result.require(DESCRIPTION).asString());
        assertEquals("profile", result.require(CHILDREN).require(PROFILE).require(DESCRIPTION).asString());

        if (operations) {
            assertTrue(result.require(OPERATIONS).isDefined());
            Set<String> ops = result.require(OPERATIONS).keys();
            assertTrue(ops.contains(READ_ATTRIBUTE_OPERATION));
            assertTrue(ops.contains(READ_CHILDREN_NAMES_OPERATION));
            assertTrue(ops.contains(READ_CHILDREN_TYPES_OPERATION));
            assertTrue(ops.contains(READ_OPERATION_DESCRIPTION_OPERATION));
            assertTrue(ops.contains(READ_OPERATION_NAMES_OPERATION));
            assertTrue(ops.contains(READ_RESOURCE_DESCRIPTION_OPERATION));
            assertTrue(ops.contains(READ_RESOURCE_OPERATION));
            assertEquals(processType != ProcessType.DOMAIN_SERVER, ops.contains(WRITE_ATTRIBUTE_OPERATION));
            for (String op : ops) {
                assertEquals(op, result.require(OPERATIONS).require(op).require(OPERATION_NAME).asString());
            }
        } else {
            assertFalse(result.get(OPERATIONS).isDefined());
        }


        if (!recursive) {
            assertFalse(result.require(CHILDREN).require(PROFILE).require(MODEL_DESCRIPTION).isDefined());
            return;
        }
        assertTrue(result.require(CHILDREN).require(PROFILE).require(MODEL_DESCRIPTION).isDefined());
        assertEquals(1, result.require(CHILDREN).require(PROFILE).require(MODEL_DESCRIPTION).keys().size());
        checkProfileNodeDescription(result.require(CHILDREN).require(PROFILE).require(MODEL_DESCRIPTION).require("*"), true, operations);
    }

    protected void checkProfileNodeDescription(ModelNode result, boolean recursive, boolean operations) {
        assertEquals(ModelType.STRING, result.require(ATTRIBUTES).require(NAME).require(TYPE).asType());
        assertEquals(false, result.require(ATTRIBUTES).require(NAME).require(NILLABLE).asBoolean());
        assertEquals(1, result.require(ATTRIBUTES).require(NAME).require(MIN_LENGTH).asInt());
        assertEquals("subsystem", result.require(CHILDREN).require(SUBSYSTEM).require(DESCRIPTION).asString());
        if (!recursive) {
            assertFalse(result.require(CHILDREN).require(SUBSYSTEM).require(MODEL_DESCRIPTION).isDefined());
            return;
        }
        assertTrue(result.require(CHILDREN).require(SUBSYSTEM).require(MODEL_DESCRIPTION).isDefined());
        assertEquals(6, result.require(CHILDREN).require(SUBSYSTEM).require(MODEL_DESCRIPTION).keys().size());
        checkSubsystem1Description(result.require(CHILDREN).require(SUBSYSTEM).require(MODEL_DESCRIPTION).require("subsystem1"), recursive, operations);
    }

    protected void checkSubsystem1Description(ModelNode result, boolean recursive, boolean operations) {
        assertNotNull(result);

        assertEquals(ModelType.LIST, result.require(ATTRIBUTES).require("attr1").require(TYPE).asType());
        assertEquals(ModelType.INT, result.require(ATTRIBUTES).require("attr1").require(VALUE_TYPE).asType());
        assertFalse(result.require(ATTRIBUTES).require("attr1").require(NILLABLE).asBoolean());
        assertEquals(AccessType.READ_ONLY.toString(), result.require(ATTRIBUTES).require("attr1").get(ACCESS_TYPE).asString());
        assertEquals(ModelType.INT, result.require(ATTRIBUTES).require("read-only").require(TYPE).asType());
        assertFalse(result.require(ATTRIBUTES).require("read-only").require(NILLABLE).asBoolean());
        assertEquals(AccessType.READ_ONLY.toString(), result.require(ATTRIBUTES).require("read-only").get(ACCESS_TYPE).asString());
        assertEquals(ModelType.INT, result.require(ATTRIBUTES).require("metric1").require(TYPE).asType());
        assertEquals(AccessType.METRIC.toString(), result.require(ATTRIBUTES).require("metric1").get(ACCESS_TYPE).asString());
        assertEquals(AccessType.METRIC.toString(), result.require(ATTRIBUTES).require("metric2").get(ACCESS_TYPE).asString());
        assertEquals(ModelType.INT, result.require(ATTRIBUTES).require("read-write").require(TYPE).asType());
        assertFalse(result.require(ATTRIBUTES).require("read-write").require(NILLABLE).asBoolean());
        assertEquals(expectedRwAttributeAccess.toString(), result.require(ATTRIBUTES).require("read-write").get(ACCESS_TYPE).asString());

        //we don't have proper support for this!
        /*assertEquals(1, result.require(CHILDREN).require("type1").require(MIN_OCCURS).asInt());
        assertEquals(1, result.require(CHILDREN).require("type2").require(MIN_OCCURS).asInt());
        assertEquals(1, result.require(CHILDREN).require("type2").require(MIN_OCCURS).asInt());*/

        assertEquals("type1", result.require(CHILDREN).require("type1").require(DESCRIPTION).asString());
        assertEquals("type2", result.require(CHILDREN).require("type2").require(DESCRIPTION).asString());


        if (operations) {
            assertTrue(result.require(OPERATIONS).isDefined());
            Set<String> ops = result.require(OPERATIONS).keys();
            assertEquals(processType == ProcessType.DOMAIN_SERVER ? 8 : 12, ops.size());
            boolean runtimeOnly = processType != ProcessType.DOMAIN_SERVER;
            assertEquals(runtimeOnly, ops.contains("testA1-1"));
            assertEquals(runtimeOnly, ops.contains("testA1-2"));
            assertTrue(ops.contains(READ_RESOURCE_OPERATION));
            assertTrue(ops.contains(READ_ATTRIBUTE_OPERATION));
            assertTrue(ops.contains(READ_RESOURCE_DESCRIPTION_OPERATION));
            assertTrue(ops.contains(READ_CHILDREN_NAMES_OPERATION));
            assertTrue(ops.contains(READ_CHILDREN_TYPES_OPERATION));
            assertTrue(ops.contains(READ_CHILDREN_RESOURCES_OPERATION));
            assertTrue(ops.contains(READ_OPERATION_NAMES_OPERATION));
            assertTrue(ops.contains(READ_OPERATION_DESCRIPTION_OPERATION));
            assertEquals(runtimeOnly, ops.contains(WRITE_ATTRIBUTE_OPERATION));

        } else {
            assertFalse(result.get(OPERATIONS).isDefined());
        }

        if (!recursive) {
            assertFalse(result.require(CHILDREN).require("type1").require(MODEL_DESCRIPTION).isDefined());
            assertFalse(result.require(CHILDREN).require("type2").require(MODEL_DESCRIPTION).isDefined());
            return;
        }

        checkType1Description(result.require(CHILDREN).require("type1").require(MODEL_DESCRIPTION).require("*"));
        checkType2Description(result.require(CHILDREN).require("type2").require(MODEL_DESCRIPTION).require("other"));
    }

    protected void checkType1Description(ModelNode result) {
        assertNotNull(result);
        assertEquals(ModelType.STRING, result.require(ATTRIBUTES).require("name").require(TYPE).asType());
        assertEquals("name", result.require(ATTRIBUTES).require("name").require(DESCRIPTION).asString());
        assertFalse(result.require(ATTRIBUTES).require("name").require(NILLABLE).asBoolean());
        assertEquals(ModelType.INT, result.require(ATTRIBUTES).require("value").require(TYPE).asType());
        assertEquals("value", result.require(ATTRIBUTES).require("value").require(DESCRIPTION).asString());
        assertFalse(result.require(ATTRIBUTES).require("value").require(NILLABLE).asBoolean());
        //TODO should the inherited ops be picked up?
        if (result.hasDefined(OPERATIONS)) {
            assertTrue(result.require(OPERATIONS).isDefined());
            Set<String> ops = result.require(OPERATIONS).keys();
            assertEquals(processType == ProcessType.DOMAIN_SERVER ? 8 : 10, ops.size());
            assertTrue(ops.contains(READ_RESOURCE_OPERATION));
            assertTrue(ops.contains(READ_ATTRIBUTE_OPERATION));
            assertTrue(ops.contains(READ_RESOURCE_DESCRIPTION_OPERATION));
            assertTrue(ops.contains(READ_CHILDREN_NAMES_OPERATION));
            assertTrue(ops.contains(READ_CHILDREN_TYPES_OPERATION));
            assertTrue(ops.contains(READ_CHILDREN_RESOURCES_OPERATION));
            assertTrue(ops.contains(READ_OPERATION_NAMES_OPERATION));
            assertTrue(ops.contains(READ_OPERATION_DESCRIPTION_OPERATION));
            assertEquals(processType != ProcessType.DOMAIN_SERVER, ops.contains(WRITE_ATTRIBUTE_OPERATION));

        }

    }

    protected void checkType2Description(ModelNode result) {
        assertNotNull(result);
        assertEquals("description", result.require(DESCRIPTION).asString());
        assertEquals(ModelType.STRING, result.require(ATTRIBUTES).require("name").require(TYPE).asType());
        assertEquals("name", result.require(ATTRIBUTES).require("name").require(DESCRIPTION).asString());
        assertFalse(result.require(ATTRIBUTES).require("name").require(NILLABLE).asBoolean());
        if (result.hasDefined(OPERATIONS)) {
            assertTrue(result.require(OPERATIONS).isDefined());
            Set<String> ops = result.require(OPERATIONS).keys();
            assertEquals(processType == ProcessType.DOMAIN_SERVER ? 8 : 10, ops.size());
            assertTrue(ops.contains(READ_RESOURCE_OPERATION));
            assertTrue(ops.contains(READ_ATTRIBUTE_OPERATION));
            assertTrue(ops.contains(READ_RESOURCE_DESCRIPTION_OPERATION));
            assertTrue(ops.contains(READ_CHILDREN_NAMES_OPERATION));
            assertTrue(ops.contains(READ_CHILDREN_TYPES_OPERATION));
            assertTrue(ops.contains(READ_CHILDREN_RESOURCES_OPERATION));
            assertTrue(ops.contains(READ_OPERATION_NAMES_OPERATION));
            assertTrue(ops.contains(READ_OPERATION_DESCRIPTION_OPERATION));
            assertEquals(processType != ProcessType.DOMAIN_SERVER, ops.contains(WRITE_ATTRIBUTE_OPERATION));
        }
    }

    protected ModelNode createOperation(String operationName, String... address) {
        ModelNode operation = new ModelNode();
        operation.get(OP).set(operationName);
        if (address.length > 0) {
            for (String addr : address) {
                operation.get(OP_ADDR).add(addr);
            }
        } else {
            operation.get(OP_ADDR).setEmptyList();
        }

        return operation;
    }

    protected List<String> modelNodeListToStringList(List<ModelNode> nodes) {
        List<String> result = new ArrayList<String>();
        for (ModelNode node : nodes) {
            result.add(node.asString());
        }
        return result;
    }

}