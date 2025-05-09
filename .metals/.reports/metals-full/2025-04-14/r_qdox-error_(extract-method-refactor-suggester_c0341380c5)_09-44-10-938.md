error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12537.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12537.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12537.java
text:
```scala
M@@ap<String, RejectAttributeChecker> mapChecker = new HashMap<String, RejectAttributeChecker>();

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

package org.jboss.as.controller.transform.description;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.UNDEFINE_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.as.controller.ExpressionResolver;
import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ProcessType;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.RunningMode;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.NonResolvingResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.transform.OperationTransformer;
import org.jboss.as.controller.transform.ResourceTransformationContext;
import org.jboss.as.controller.transform.TransformationContext;
import org.jboss.as.controller.transform.TransformationTarget;
import org.jboss.as.controller.transform.TransformationTargetImpl;
import org.jboss.as.controller.transform.TransformerRegistry;
import org.jboss.as.controller.transform.Transformers;
import org.jboss.as.controller.transform.TransformersSubRegistration;
import org.jboss.as.controller.transform.description.AttributeConverter.DefaultAttributeConverter;
import org.jboss.as.controller.transform.description.DiscardAttributeChecker.DefaultDiscardAttributeChecker;
import org.jboss.as.controller.transform.description.RejectAttributeChecker.DefaultRejectAttributeChecker;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.dmr.ValueExpression;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class AttributesTestCase {

    private static PathElement PATH = PathElement.pathElement("toto", "testSubsystem");

    private Resource resourceRoot = Resource.Factory.create();
    private TransformerRegistry registry = TransformerRegistry.Factory.create(null);
    private ManagementResourceRegistration resourceRegistration = ManagementResourceRegistration.Factory.create(ROOT);
    private TransformersSubRegistration transformersSubRegistration;
    private ModelNode resourceModel;

    @Before
    public void setUp() {
        // Cleanup
        resourceRoot = Resource.Factory.create();
        registry = TransformerRegistry.Factory.create(null);
        resourceRegistration = ManagementResourceRegistration.Factory.create(ROOT);
        // test
        final Resource toto = Resource.Factory.create();
        resourceRoot.registerChild(PATH, toto);
        resourceModel = toto.getModel();

        // Register the description
        transformersSubRegistration = registry.getServerRegistration(ModelVersion.create(1));

    }


    @Test
    public void testRejectExpressions() throws Exception {
        //Set up the model
        resourceModel.get("reject").set(new ValueExpression("${expr}"));

        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
        builder.getAttributeBuilder().addRejectCheck(RejectAttributeChecker.SIMPLE_EXPRESSIONS, "reject").end();
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        //The rejection does not trigger for resource transformation
        Assert.assertTrue(model.hasDefined("reject"));

        ModelNode add = Util.createAddOperation(PathAddress.pathAddress(PATH));
        add.get("reject").set(new ValueExpression("${expr}"));
        OperationTransformer.TransformedOperation transformedAdd = transformOperation(add);
        Assert.assertTrue(transformedAdd.rejectOperation(success()));

        ModelNode write = Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "reject", new ModelNode().set(new ValueExpression("${expr}")));
        OperationTransformer.TransformedOperation transformedWrite = transformOperation(write);
        Assert.assertTrue(transformedWrite.rejectOperation(success()));
    }

    @Test
    public void testCustomRejectChecker() throws Exception {
        //Set up the model
        resourceModel.get("reject").set(new ValueExpression("${expr}"));
        DontRejectChecker dontRejectChecker = new DontRejectChecker();
        CustomRejectExpressionsChecker rejectAttributeChecker = new CustomRejectExpressionsChecker();
        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
            builder.getAttributeBuilder()
                .addRejectCheck(dontRejectChecker, "reject")
                .addRejectCheck(rejectAttributeChecker, "reject")
                .end();
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        dontRejectChecker.called = false;
        rejectAttributeChecker.clear();;
        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        //The rejection does not trigger for resource transformation
        //TODO this could be done if 'slave' is >= 7.2.0
        Assert.assertTrue(model.hasDefined("reject"));
        Assert.assertTrue(dontRejectChecker.called);
        Set<String> rejections = rejectAttributeChecker.getRejections();
        Assert.assertEquals(1, rejections.size());
        Assert.assertTrue(rejections.contains("reject"));

        dontRejectChecker.called = false;
        rejectAttributeChecker.clear();
        ModelNode add = Util.createAddOperation(PathAddress.pathAddress(PATH));
        add.get("reject").set(new ValueExpression("${expr}"));
        OperationTransformer.TransformedOperation transformedAdd = transformOperation(add);
        Assert.assertTrue(transformedAdd.rejectOperation(success()));
        Assert.assertTrue(dontRejectChecker.called);
        rejections = rejectAttributeChecker.getRejections();
        Assert.assertEquals(1, rejections.size());
        Assert.assertTrue(rejections.contains("reject"));


        dontRejectChecker.called = false;
        rejectAttributeChecker.clear();
        ModelNode write = Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "reject", new ModelNode().set(new ValueExpression("${expr}")));
        OperationTransformer.TransformedOperation transformedWrite = transformOperation(write);
        Assert.assertTrue(transformedWrite.rejectOperation(success()));
        Assert.assertTrue(dontRejectChecker.called);
        rejections = rejectAttributeChecker.getRejections();
        Assert.assertEquals(1, rejections.size());
        Assert.assertTrue(rejections.contains("reject"));

    }

    @Test
    public void testListReject() throws Exception {
        final ModelNode list = new ModelNode().add("one").add("two").add("three");
        resourceModel.get("reject").set(list.clone());

        final RejectTwoChecker checker = new RejectTwoChecker();
        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
        builder.getAttributeBuilder()
            .addRejectCheck(new RejectAttributeChecker.ListRejectAttributeChecker(checker), "reject")
            .end();
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        //The rejection does not trigger for resource transformation
        //TODO this could be done if 'slave' is >= 7.2.0
        Assert.assertTrue(model.hasDefined("reject"));
        Assert.assertEquals(2, checker.count);
        Assert.assertTrue(checker.rejected);

        checker.count = 0;
        checker.rejected = false;
        ModelNode add = Util.createAddOperation(PathAddress.pathAddress(PATH));
        add.get("reject").set(list.clone());
        OperationTransformer.TransformedOperation transformedAdd = transformOperation(add);
        Assert.assertTrue(transformedAdd.rejectOperation(success()));
        Assert.assertEquals(2, checker.count);
        Assert.assertTrue(checker.rejected);

        checker.count = 0;
        checker.rejected = false;
        ModelNode write = Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "reject", list.clone());
        OperationTransformer.TransformedOperation transformedWrite = transformOperation(write);
        Assert.assertTrue(transformedWrite.rejectOperation(success()));
        Assert.assertEquals(2, checker.count);
        Assert.assertTrue(checker.rejected);
    }

    @Test
    public void testObjectReject() throws Exception {
        final ModelNode object = new ModelNode();
        object.get("0").set("zero");
        object.get("1").set("one");
        object.get("2").set("two");
        object.get("3").set("two");
        resourceModel.get("reject").set(object.clone());

        final RejectTwoChecker checker = new RejectTwoChecker();
        Map<String, RejectAttributeChecker> mapChecker = new HashMap<>();
        mapChecker.put("1", checker);
        mapChecker.put("2", checker);

        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
        builder.getAttributeBuilder()
            .addRejectCheck(new RejectAttributeChecker.ObjectFieldsRejectAttributeChecker(mapChecker), "reject")
            .end();
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        //The rejection does not trigger for resource transformation
        //TODO this could be done if 'slave' is >= 7.2.0
        Assert.assertTrue(model.hasDefined("reject"));
        //The order is unpredicatble since a plain hashmap is used
        //Assert.assertEquals(2, checker.count);
        Assert.assertTrue(checker.rejected);

        checker.count = 0;
        checker.rejected = false;
        ModelNode add = Util.createAddOperation(PathAddress.pathAddress(PATH));
        add.get("reject").set(object.clone());
        OperationTransformer.TransformedOperation transformedAdd = transformOperation(add);
        Assert.assertTrue(transformedAdd.rejectOperation(success()));
        //The order is unpredicatble since a plain hashmap is used
        //Assert.assertEquals(2, checker.count);
        Assert.assertTrue(checker.rejected);

        checker.count = 0;
        checker.rejected = false;
        ModelNode write = Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "reject", object.clone());
        OperationTransformer.TransformedOperation transformedWrite = transformOperation(write);
        Assert.assertTrue(transformedWrite.rejectOperation(success()));
        //The order is unpredicatble since a plain hashmap is used
        //Assert.assertEquals(2, checker.count);
        Assert.assertTrue(checker.rejected);
    }

    @Test
    public void testDiscardAlways() throws Exception {
        //Set up the model
        resourceModel.get("discard").set("nothing");
        resourceModel.get("keep").set("here");

        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
            builder.getAttributeBuilder().setDiscard(DiscardAttributeChecker.ALWAYS, "discard").end();
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        Assert.assertTrue(model.hasDefined("keep"));
        Assert.assertFalse(model.has("discard"));

        ModelNode add = Util.createAddOperation(PathAddress.pathAddress(PATH));
        add.get("discard").set("nothing");
        add.get("keep").set("here");
        OperationTransformer.TransformedOperation transformedAdd = transformOperation(add);
        Assert.assertFalse(transformedAdd.rejectOperation(success()));
        Assert.assertTrue(transformedAdd.getTransformedOperation().hasDefined("keep"));
        Assert.assertFalse(transformedAdd.getTransformedOperation().has("discard"));

        checkOpDiscarded(Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "discard", new ModelNode("nothing")));
        checkOpDiscarded(Util.getUndefineAttributeOperation(PathAddress.pathAddress(PATH), "discard"));
    }


    @Test
    public void testDiscardUndefined() throws Exception {
        //Set up the model
        resourceModel.get("discard");
        resourceModel.get("keep").set("here");

        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
            builder.getAttributeBuilder().setDiscard(DiscardAttributeChecker.UNDEFINED, "discard", "keep").end();
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        Assert.assertTrue(model.hasDefined("keep"));
        Assert.assertFalse(model.has("discard"));

        ModelNode add = Util.createAddOperation(PathAddress.pathAddress(PATH));
        add.get("discard");
        add.get("keep").set("here");
        OperationTransformer.TransformedOperation transformedAdd = transformOperation(add);
        Assert.assertFalse(transformedAdd.rejectOperation(success()));
        Assert.assertTrue(transformedAdd.getTransformedOperation().hasDefined("keep"));
        Assert.assertFalse(transformedAdd.getTransformedOperation().has("discard"));

        checkOpDiscarded(Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "discard", new ModelNode()));

        checkWriteOp(Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "discard", new ModelNode("nothing")),
                "discard", new ModelNode("nothing"));
    }

    @Test
    public void testDiscardNotHappeningWithExpressions() throws Exception {
        //Set up the model
        resourceModel.get("discard").set(new ValueExpression("${xxx}"));

        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
            builder.getAttributeBuilder().setDiscard(new DefaultDiscardAttributeChecker(false, false) {
                @Override
                public boolean isValueDiscardable(PathAddress address, String attributeName, ModelNode attributeValue, TransformationContext context) {
                    return true;
                }
            }, "discard").end();
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        Assert.assertEquals(new ModelNode().set(new ValueExpression("${xxx}")), model.get("discard"));

        ModelNode add = Util.createAddOperation(PathAddress.pathAddress(PATH));
        add.get("discard").set(new ValueExpression("${xxx}"));
        OperationTransformer.TransformedOperation transformedAdd = transformOperation(add);
        Assert.assertFalse(transformedAdd.rejectOperation(success()));
        Assert.assertEquals(add, transformedAdd.getTransformedOperation());

        checkWriteOp(Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "discard", new ModelNode().set(new ValueExpression("${xxx}"))),
                "discard", new ModelNode().set(new ValueExpression("${xxx}")));
    }

    @Test
    public void testDiscardDefaultValue() throws Exception {
        //Set up the model
        resourceModel.get("discard").set("default");
        resourceModel.get("keep").set("non-default");

        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
            builder.getAttributeBuilder().setDiscard(new DefaultDiscardAttributeChecker(false, true) {
                @Override
                public boolean isValueDiscardable(PathAddress address, String attributeName, ModelNode attributeValue, TransformationContext context) {
                    if (attributeName.equals("discard") || attributeName.equals("keep")) {
                        if (attributeValue.asString().equals("default")) {
                            return true;
                        }
                    }
                    return false;
                }
            }, "discard", "keep").end();
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        Assert.assertTrue(model.hasDefined("keep"));
        Assert.assertFalse(model.has("discard"));

        ModelNode add = Util.createAddOperation(PathAddress.pathAddress(PATH));
        add.get("discard");
        add.get("keep").set("here");
        OperationTransformer.TransformedOperation transformedAdd = transformOperation(add);
        Assert.assertFalse(transformedAdd.rejectOperation(success()));
        Assert.assertTrue(transformedAdd.getTransformedOperation().hasDefined("keep"));
        Assert.assertFalse(transformedAdd.getTransformedOperation().has("discard"));

        checkOpDiscarded(Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "discard", new ModelNode("default")));
        checkWriteOp(Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "discard", new ModelNode("something")),
                "discard", new ModelNode("something"));
    }

    @Test
    public void testRenameAttribute() throws Exception {
        resourceModel.get("old").set("value");

        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
            builder.getAttributeBuilder().addRename("old", "new").end();
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        Assert.assertEquals(1, model.keys().size());
        Assert.assertEquals("value", model.get("new").asString());

        ModelNode add = Util.createAddOperation(PathAddress.pathAddress(PATH));
        add.get("old").set("value");
        OperationTransformer.TransformedOperation transformedAdd = transformOperation(add);
        Assert.assertFalse(transformedAdd.rejectOperation(success()));
        Assert.assertFalse(transformedAdd.getTransformedOperation().hasDefined("old"));
        Assert.assertEquals("value", transformedAdd.getTransformedOperation().get("new").asString());


        checkWriteOp(Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "old", new ModelNode("value")),
                "new", new ModelNode("value"));
    }

    @Test
    public void testConvertValue() throws Exception {
        resourceModel.get("value1").set("one");
        resourceModel.get("value2").set("two");

        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
        builder.getAttributeBuilder().setValueConverter(new DefaultAttributeConverter() {
            @Override
            public void convertAttribute(PathAddress address, String name, ModelNode attributeValue, TransformationContext context) {
                if (name.equals("value2") && attributeValue.asString().equals("two")) {
                    attributeValue.set(1);
                }
            }
        }, "value1", "value2").end();
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        Assert.assertEquals(2, model.keys().size());
        Assert.assertEquals("one", model.get("value1").asString());
        Assert.assertEquals(ModelType.INT, model.get("value2").getType());
        Assert.assertEquals(1, model.get("value2").asInt());

        ModelNode add = Util.createAddOperation(PathAddress.pathAddress(PATH));
        add.get("value1").set("one");
        add.get("value2").set("two");
        OperationTransformer.TransformedOperation transformedAdd = transformOperation(add);
        Assert.assertFalse(transformedAdd.rejectOperation(success()));
        Assert.assertEquals("one", transformedAdd.getTransformedOperation().get("value1").asString());
        Assert.assertEquals(ModelType.INT, transformedAdd.getTransformedOperation().get("value2").getType());
        Assert.assertEquals(1, transformedAdd.getTransformedOperation().get("value2").asInt());

        checkWriteOp(Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "value1", new ModelNode("value")),
                "value1", new ModelNode("value"));
        checkWriteOp(Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "value2", new ModelNode("two")),
                "value2", new ModelNode(1));
    }

    @Test
    public void testAddValue() throws Exception {
        resourceModel.get("old").set("existing");

        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
        builder.getAttributeBuilder().setValueConverter(new DefaultAttributeConverter() {
            @Override
            public void convertAttribute(PathAddress address, String name, ModelNode attributeValue, TransformationContext context) {
                attributeValue.set("extra");
            }
        }, "added").end();
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        Assert.assertEquals(2, model.keys().size());
        Assert.assertEquals("existing", model.get("old").asString());
        Assert.assertEquals("extra", model.get("added").asString());

        ModelNode add = Util.createAddOperation(PathAddress.pathAddress(PATH));
        add.get("old").set("existing");
        OperationTransformer.TransformedOperation transformedAdd = transformOperation(add);
        Assert.assertFalse(transformedAdd.rejectOperation(success()));
        Assert.assertEquals("existing", transformedAdd.getTransformedOperation().get("old").asString());
        Assert.assertEquals("extra", transformedAdd.getTransformedOperation().get("added").asString());

        //Can't write to this added attribute
    }

    @Test
    public void testComplexRejectAndModify() throws Exception {
        resourceModel.get("one").set("a");
        resourceModel.get("two").set("b");
        resourceModel.get("three").set("TRES");
        resourceModel.get("four");
        resourceModel.get("five");
        resourceModel.get("six");

        Map<String,String> renames = new HashMap<String, String>();
        renames.put("one", "uno");
        renames.put("two", "dos");

        CustomRejectExpressionsChecker rejectAttributeChecker = new CustomRejectExpressionsChecker();
        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
        builder.getAttributeBuilder()
                .addRejectCheck(rejectAttributeChecker, "one", "two")
                .setValueConverter(new DefaultAttributeConverter() {
                    @Override
                    public void convertAttribute(PathAddress address, String name, ModelNode attributeValue, TransformationContext context) {
                        attributeValue.set("NEW");
                    }
                }, "new")
                .setDiscard(DiscardAttributeChecker.UNDEFINED, "four", "five")
                .setDiscard(DiscardAttributeChecker.UNDEFINED, "six")
                .setValueConverter(new DefaultAttributeConverter() {
                    @Override
                    public void convertAttribute(PathAddress address, String name, ModelNode attributeValue, TransformationContext context) {
                        if (name.equals("one")) {
                            attributeValue.set("UNO");
                        } else if (name.equals("two")) {
                            attributeValue.set("DOS");
                        }
                    }
                }, "one", "two")
                .addRenames(renames)
                .addRename("three", "tres")
                //.rename(Collections.singletonMap("four", "cuatro"))
                .end();
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        //Try first with no expressions
        rejectAttributeChecker.clear();
        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        Assert.assertEquals(4, model.keys().size());
        Assert.assertEquals("NEW", model.get("new").asString());
        Assert.assertEquals("UNO", model.get("uno").asString());
        Assert.assertEquals("DOS", model.get("dos").asString());
        Assert.assertEquals("TRES", model.get("tres").asString());
        Set<String> rejections = rejectAttributeChecker.getRejections();
        Assert.assertEquals(0, rejections.size());


        final ModelNode add = Util.createAddOperation(PathAddress.pathAddress(PATH));
        add.get("one").set("a");
        add.get("two").set("b");
        add.get("three").set("TRES");
        add.get("four");
        add.get("five");
        add.get("six");
        final OperationTransformer.TransformedOperation transformedAdd = transformOperation(add);
        Assert.assertFalse(transformedAdd.rejectOperation(success()));
        final ModelNode transAdd = transformedAdd.getTransformedOperation();
        transAdd.remove(OP);
        transAdd.remove(OP_ADDR);
        Assert.assertEquals(4, transAdd.keys().size());
        Assert.assertEquals("NEW", transAdd.get("new").asString());
        Assert.assertEquals("UNO", transAdd.get("uno").asString());
        Assert.assertEquals("DOS", transAdd.get("dos").asString());
        Assert.assertEquals("TRES", transAdd.get("tres").asString());
        rejections = rejectAttributeChecker.getRejections();
        Assert.assertEquals(0, rejections.size());

        checkWriteOp(Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "one", new ModelNode("a")),
                 "uno", new ModelNode("UNO"));
        checkWriteOp(Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "two", new ModelNode("b")),
                "dos", new ModelNode("DOS"));
        checkWriteOp(Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "three", new ModelNode("THREE")),
                "tres", new ModelNode("THREE"));
        checkOpDiscarded(Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "four", new ModelNode()));
        checkOpDiscarded(Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "five", new ModelNode()));
        checkOpDiscarded(Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "six", new ModelNode()));
        rejections = rejectAttributeChecker.getRejections();
        Assert.assertEquals(0, rejections.size());

        //Check that expressions get rejected
        resourceModel.clear();
        resourceModel.get("one").set(new ValueExpression("${test}"));
        resourceModel.get("two").set("b");
        resourceModel.get("three").set("TRES");
        resourceModel.get("four");
        resourceModel.get("five");
        resourceModel.get("six");

        rejectAttributeChecker.clear();
        final Resource resource2 = transformResource();
        Assert.assertNotNull(resource2);
        final Resource toto2 = resource2.getChild(PATH);
        Assert.assertNotNull(toto2);
        final ModelNode model2 = toto2.getModel();
        Assert.assertEquals(4, model2.keys().size());
        Assert.assertEquals("NEW", model2.get("new").asString());
        Assert.assertEquals("UNO", model2.get("uno").asString());
        Assert.assertEquals("DOS", model2.get("dos").asString());
        Assert.assertEquals("TRES", model2.get("tres").asString());
        rejections = rejectAttributeChecker.getRejections();
        Assert.assertEquals(1, rejections.size());
        Assert.assertTrue(rejections.contains("one"));

        rejectAttributeChecker.clear();
        final ModelNode add2 = Util.createAddOperation(PathAddress.pathAddress(PATH));
        add2.get("one").set(new ValueExpression("${test}"));
        add2.get("two").set("b");
        add2.get("three").set("TRES");
        add2.get("four");
        add2.get("five");
        add2.get("six");
        final OperationTransformer.TransformedOperation transformedAdd2 = transformOperation(add2);
        Assert.assertTrue(transformedAdd2.rejectOperation(success()));
        rejections = rejectAttributeChecker.getRejections();
        Assert.assertEquals(1, rejections.size());
        Assert.assertTrue(rejections.contains("one"));

        rejectAttributeChecker.clear();
        OperationTransformer.TransformedOperation write = transformOperation(Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "one", new ModelNode().set(new ValueExpression("${test}"))));
        Assert.assertTrue(write.rejectOperation(success()));
        rejections = rejectAttributeChecker.getRejections();
        Assert.assertEquals(1, rejections.size());
        Assert.assertTrue(rejections.contains("one"));
    }

    @Test
    public void testNameFromAddressConverter() throws Exception {
        //Set up the model
        resourceModel.get("value").set("test");

        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
        builder.getAttributeBuilder().setValueConverter(AttributeConverter.NAME_FROM_ADDRESS, "name").end();
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        Assert.assertEquals(2, model.keys().size());
        Assert.assertEquals("test", model.get("value").asString());
        Assert.assertEquals(PATH.getValue(), model.get("name").asString());

        ModelNode add = Util.createAddOperation(PathAddress.pathAddress(PATH));
        add.get("value").set("test");
        OperationTransformer.TransformedOperation transformedAdd = transformOperation(add);
        Assert.assertEquals("test", transformedAdd.getTransformedOperation().get("value").asString());
        Assert.assertEquals(PATH.getValue(), transformedAdd.getTransformedOperation().get("name").asString());

        //No point in testing write-attribute for a new attribute
    }

    @Test
    public void testAccessOtherValue() throws Exception {
        //Set up the model
        resourceModel.get("checked").set("test");
        resourceModel.get("other").set("value");

        VisibilityCheckerAndConverter checker = new VisibilityCheckerAndConverter();

        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
        builder.getAttributeBuilder()
            .addRejectCheck(checker, "checked")
            .setDiscard(checker, "checked")
            .setValueConverter(checker, "checked")
            .end();
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        //The rejection does not trigger for resource transformation
        Assert.assertTrue(model.hasDefined("checked"));
        checker.checkValues("value");

        checker.reset();
        ModelNode add = Util.createAddOperation(PathAddress.pathAddress(PATH));
        add.get("checked").set("test");
        add.get("other").set("value");
        OperationTransformer.TransformedOperation transformedAdd = transformOperation(add);
        Assert.assertFalse(transformedAdd.rejectOperation(success()));
        checker.checkValues("value");

        checker.reset();
        ModelNode write = Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "checked", new ModelNode("test"));
        OperationTransformer.TransformedOperation transformedWrite = transformOperation(write);
        Assert.assertFalse(transformedWrite.rejectOperation(success()));
        checker.checkValues("value");
    }

    @Test
    public void testConvertVsAdd() throws Exception {
        resourceModel.get("convert").set("test");
        resourceModel.get("undefined");

        AttributeNameValueConverter converter = new AttributeNameValueConverter();
        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
        builder.getAttributeBuilder()
            .setValueConverter(converter, "convert", "undefined", "new", "dontAdd")
            .end();
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        Assert.assertEquals(3, model.keys().size());
        Assert.assertEquals("convert", model.get("convert").asString());
        Assert.assertEquals("undefined", model.get("undefined").asString());
        Assert.assertEquals("new", model.get("new").asString());
    }

    @Test
    public void testSimpleReject() throws Exception {
        //Set up the model
        resourceModel.get("reject").set(true);

        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
        builder.getAttributeBuilder().addRejectCheck(new RejectAttributeChecker.SimpleRejectAttributeChecker(new ModelNode(true)), "reject").end();
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        //The rejection does not trigger for resource transformation
        Assert.assertTrue(model.hasDefined("reject"));

        ModelNode add = Util.createAddOperation(PathAddress.pathAddress(PATH));
        add.get("reject").set(true);
        OperationTransformer.TransformedOperation transformedAdd = transformOperation(add);
        Assert.assertTrue(transformedAdd.rejectOperation(success()));

        ModelNode write = Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "reject", new ModelNode().set(true));
        OperationTransformer.TransformedOperation transformedWrite = transformOperation(write);
        Assert.assertTrue(transformedWrite.rejectOperation(success()));
    }

    @Test
    public void testSimpleAccept() throws Exception {
        //Set up the model
        resourceModel.get("reject").set(true);

        final ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createInstance(PATH);
        builder.getAttributeBuilder().addRejectCheck(new RejectAttributeChecker.SimpleAcceptAttributeChecker(new ModelNode(false)), "reject").end();
        TransformationDescription.Tools.register(builder.build(), transformersSubRegistration);

        final Resource resource = transformResource();
        Assert.assertNotNull(resource);
        final Resource toto = resource.getChild(PATH);
        Assert.assertNotNull(toto);
        final ModelNode model = toto.getModel();
        //The rejection does not trigger for resource transformation
        Assert.assertTrue(model.hasDefined("reject"));

        ModelNode add = Util.createAddOperation(PathAddress.pathAddress(PATH));
        add.get("reject").set(true);
        OperationTransformer.TransformedOperation transformedAdd = transformOperation(add);
        Assert.assertTrue(transformedAdd.rejectOperation(success()));

        ModelNode write = Util.getWriteAttributeOperation(PathAddress.pathAddress(PATH), "reject", new ModelNode().set(true));
        OperationTransformer.TransformedOperation transformedWrite = transformOperation(write);
        Assert.assertTrue(transformedWrite.rejectOperation(success()));
    }

    private void checkWriteOp(ModelNode write, String name, ModelNode value) throws OperationFailedException{
        OperationTransformer.TransformedOperation transformedWrite = transformOperation(write);
        Assert.assertFalse(transformedWrite.rejectOperation(success()));
        ModelNode transWrite = transformedWrite.getTransformedOperation();
        Assert.assertEquals(name, transWrite.get(NAME).asString());
        Assert.assertEquals(value, transWrite.get(VALUE));
        Assert.assertEquals(value.getType(), transWrite.get(VALUE).getType());
    }

    private void checkOpDiscarded(ModelNode write) throws OperationFailedException {
        OperationTransformer.TransformedOperation transformedWrite = transformOperation(write);
        Assert.assertFalse(transformedWrite.rejectOperation(success()));
        Assert.assertNull(transformedWrite.getTransformedOperation());
    }


    private Resource transformResource() throws OperationFailedException {
        final TransformationTarget target = create(registry, ModelVersion.create(1));
        final ResourceTransformationContext context = createContext(target);
        return getTransfomers(target).transformResource(context, resourceRoot);
    }

    private OperationTransformer.TransformedOperation transformOperation(final ModelNode operation) throws OperationFailedException {
        final TransformationTarget target = create(registry, ModelVersion.create(1));
        final TransformationContext context = createContext(target);
        return getTransfomers(target).transformOperation(context, operation);
    }

    private ResourceTransformationContext createContext(final TransformationTarget target) {
        return Transformers.Factory.create(target, resourceRoot, resourceRegistration, ExpressionResolver.TEST_RESOLVER, RunningMode.NORMAL, ProcessType.STANDALONE_SERVER);
    }

    private Transformers getTransfomers(final TransformationTarget target) {
        return Transformers.Factory.create(target);
    }

    protected TransformationTarget create(final TransformerRegistry registry, ModelVersion version) {
        return create(registry, version, TransformationTarget.TransformationTargetType.SERVER);
    }

    protected TransformationTarget create(final TransformerRegistry registry, ModelVersion version, TransformationTarget.TransformationTargetType type) {
        return TransformationTargetImpl.create(registry, version, Collections.<PathAddress, ModelVersion>emptyMap(), null, type, null);
    }

    private static final ResourceDefinition ROOT = new SimpleResourceDefinition(PathElement.pathElement("test"), new NonResolvingResourceDescriptionResolver());

    private static ModelNode success() {
        final ModelNode result = new ModelNode();
        result.get(ModelDescriptionConstants.OUTCOME).set(ModelDescriptionConstants.SUCCESS);
        result.get(ModelDescriptionConstants.RESULT);
        return result;
    }

    private static ModelNode failed() {
        final ModelNode result = new ModelNode();
        result.get(ModelDescriptionConstants.OUTCOME).set(ModelDescriptionConstants.FAILED);
        result.get(ModelDescriptionConstants.FAILURE_DESCRIPTION).set("failed");
        result.get(ModelDescriptionConstants.RESULT);
        return result;
    }

    private static class DontRejectChecker extends DefaultRejectAttributeChecker {
        boolean called;

        @Override
        public boolean rejectAttribute(PathAddress address, String attributeName, ModelNode attributeValue, TransformationContext context) {
            called = true;
            return false;
        }

        @Override
        public String getRejectionLogMessage(Map<String, ModelNode> attributes) {
            return "dont reject: " + attributes;
        }
    }

    private static class CustomRejectExpressionsChecker implements RejectAttributeChecker {
        Map<String, Boolean> rejected = new HashMap<String, Boolean>();

        @Override
        public boolean rejectOperationParameter(PathAddress address, String attributeName, ModelNode attributeValue, ModelNode operation,
                TransformationContext context) {
            boolean rejected = SIMPLE_EXPRESSIONS.rejectOperationParameter(address, attributeName, attributeValue, operation, context);
            this.rejected.put(attributeName, rejected);
            return rejected;
        }

        @Override
        public boolean rejectResourceAttribute(PathAddress address, String attributeName, ModelNode attributeValue, TransformationContext context) {
            boolean rejected = SIMPLE_EXPRESSIONS.rejectResourceAttribute(address, attributeName, attributeValue, context);
            this.rejected.put(attributeName, rejected);
            return rejected;
        }

        @Override
        public String getRejectionLogMessageId() {
            return SIMPLE_EXPRESSIONS.getRejectionLogMessageId();
        }

        @Override
        public String getRejectionLogMessage(Map<String, ModelNode> attributes) {
            return SIMPLE_EXPRESSIONS.getRejectionLogMessage(attributes);
        }

        void clear() {
            rejected.clear();
        }

        Set<String> getRejections() {
            Set<String> rejections = new HashSet<String>();
            for (Map.Entry<String, Boolean> entry : rejected.entrySet()) {
                if (entry.getValue()) {
                    rejections.add(entry.getKey());
                }
            }
            return rejections;
        }
    }

    private static class RejectTwoChecker extends DefaultRejectAttributeChecker {
        int count;
        boolean rejected;

        @Override
        public boolean rejectAttribute(PathAddress address, String attributeName, ModelNode attributeValue, TransformationContext context) {
            count++;
            rejected = attributeValue.asString().equals("two");
            return rejected;
        }

        @Override
        public String getRejectionLogMessage(Map<String, ModelNode> attributes) {
            return "Reject Two: " + attributes;
        }
    }

    private static class VisibilityCheckerAndConverter implements RejectAttributeChecker, AttributeConverter, DiscardAttributeChecker {

        String convertValue;
        String discardValue;
        String rejectValue;

        void checkValues(String expected) {
            Assert.assertEquals(expected, discardValue);
            Assert.assertEquals(expected, rejectValue);
            Assert.assertEquals(expected, convertValue);
        }

        void reset() {
            convertValue = null;
            discardValue = null;
            rejectValue = null;
        }


        @Override
        public boolean isDiscardExpressions() {
            return false;
        }

        @Override
        public boolean isDiscardUndefined() {
            return false;
        }

        @Override
        public boolean isOperationParameterDiscardable(PathAddress address, String attributeName, ModelNode attributeValue, ModelNode operation,
                TransformationContext context) {
            if (operation.get(OP).asString().equals(WRITE_ATTRIBUTE_OPERATION) || operation.get(OP).asString().equals(UNDEFINE_ATTRIBUTE_OPERATION)) {
                discardValue = context.readResource(PathAddress.EMPTY_ADDRESS).getModel().get("other").asString();
            } else {
                discardValue = operation.get("other").asString();
            }
            return false;
        }

        @Override
        public boolean isResourceAttributeDiscardable(PathAddress address, String attributeName, ModelNode attributeValue,
                TransformationContext context) {
            discardValue = context.readResource(PathAddress.EMPTY_ADDRESS).getModel().get("other").asString();
            return false;
        }

        @Override
        public void convertOperationParameter(PathAddress address, String name, ModelNode attributeValue, ModelNode operation,
                TransformationContext context) {
            if (operation.get(OP).asString().equals(WRITE_ATTRIBUTE_OPERATION) || operation.get(OP).asString().equals(UNDEFINE_ATTRIBUTE_OPERATION)) {
                convertValue = context.readResource(PathAddress.EMPTY_ADDRESS).getModel().get("other").asString();
            } else {
                convertValue = operation.get("other").asString();
            }
        }

        @Override
        public void convertResourceAttribute(PathAddress address, String name, ModelNode attributeValue,
                TransformationContext context) {
            convertValue = context.readResource(PathAddress.EMPTY_ADDRESS).getModel().get("other").asString();
        }

        @Override
        public boolean rejectOperationParameter(PathAddress address, String attributeName, ModelNode attributeValue, ModelNode operation,
                TransformationContext context) {
            if (operation.get(OP).asString().equals(WRITE_ATTRIBUTE_OPERATION) || operation.get(OP).asString().equals(UNDEFINE_ATTRIBUTE_OPERATION)) {
                rejectValue = context.readResource(PathAddress.EMPTY_ADDRESS).getModel().get("other").asString();
            } else {
                rejectValue = operation.get("other").asString();
            }
            return false;
        }

        @Override
        public boolean rejectResourceAttribute(PathAddress address, String attributeName, ModelNode attributeValue, TransformationContext context) {
            rejectValue = context.readResource(PathAddress.EMPTY_ADDRESS).getModel().get("other").asString();
            return false;
        }

        @Override
        public String getRejectionLogMessageId() {
            return getRejectionLogMessage(Collections.<String, ModelNode>emptyMap());
        }

        @Override
        public String getRejectionLogMessage(Map<String, ModelNode> attributes) {
            return "Visibility " + attributes;
        }
    }

    private static class AttributeNameValueConverter extends DefaultAttributeConverter {
        @Override
        protected void convertAttribute(PathAddress address, String name, ModelNode attributeValue, TransformationContext context) {
            if (!name.equals("dontAdd")) {
                attributeValue.set(name);
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12537.java