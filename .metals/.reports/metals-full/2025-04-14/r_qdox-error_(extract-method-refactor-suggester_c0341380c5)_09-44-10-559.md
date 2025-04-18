error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3838.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3838.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3838.java
text:
```scala
L@@OOPBACK, LOOPBACK_ADDRESS, MULTICAST, NIC, NIC_MATCH, POINT_TO_POINT, PUBLIC_ADDRESS,

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
package org.jboss.as.controller.descriptions.common;

import static org.jboss.as.controller.ControllerMessages.MESSAGES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ALTERNATIVES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HEAD_COMMENT_ALLOWED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATION_NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REPLY_PROPERTIES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REQUEST_PROPERTIES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.TAIL_COMMENT_ALLOWED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE_TYPE;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.ListAttributeDefinition;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.client.helpers.MeasurementUnit;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.ResourceDescriptionResolver;
import org.jboss.as.controller.operations.validation.ModelTypeValidator;
import org.jboss.as.controller.operations.validation.ParameterValidator;
import org.jboss.as.controller.parsing.Element;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * Model descriptions for interface elements.
 *
 * @author Brian Stansberry
 * @author Emanuel Muckenhuber
 */
public class InterfaceDescription {

    private static final String RESOURCE_NAME = InterfaceDescription.class.getPackage().getName() + ".LocalDescriptions";

    public static ModelNode getNamedInterfaceDescription(final Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);

        final ModelNode root = new ModelNode();
        root.get(DESCRIPTION).set(bundle.getString("named_interface"));
        NAME.addResourceAttributeDescription(bundle, "interface", root);
        populateInterface(root, bundle, false);
        return root;
    }

    public static ModelNode getSpecifiedInterfaceDescription(final Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);

        final ModelNode root = new ModelNode();
        root.get(DESCRIPTION).set(bundle.getString("specified_interface"));
        NAME.addResourceAttributeDescription(bundle, "interface", root);
        populateInterface(root, bundle, true);
        return root;
    }

    private static void populateInterface(ModelNode root, ResourceBundle bundle, boolean specified) {
        root.get(HEAD_COMMENT_ALLOWED).set(true);
        root.get(TAIL_COMMENT_ALLOWED).set(false);
        // Add the interface criteria operation params
        for(final AttributeDefinition def : ROOT_ATTRIBUTES) {
            def.addResourceAttributeDescription(bundle, "interface", root);
        }
    }

    public static ModelNode getNamedInterfaceAddOperation(final Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);
        final ModelNode root = new ModelNode();
        root.get(OPERATION_NAME).set(ADD);
        root.get(DESCRIPTION).set(bundle.getString("interface.add"));
        // Add the interface criteria attributes
        for(final AttributeDefinition def : ROOT_ATTRIBUTES) {
            def.addOperationParameterDescription(bundle, "interface", root);
        }
        return root;
    }

    public static ModelNode getSpecifiedInterfaceAddOperation(final Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);
        final ModelNode root = new ModelNode();
        root.get(OPERATION_NAME).set(ADD);
        root.get(DESCRIPTION).set(bundle.getString("interface.add"));
        // Add the interface criteria attributes
        for(final AttributeDefinition def : ROOT_ATTRIBUTES) {
            def.addOperationParameterDescription(bundle, "interface", root);
        }
        return root;
    }

    public static ModelNode getInterfaceRemoveOperation(final Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);
        final ModelNode root = new ModelNode();
        root.get(OPERATION_NAME).set(REMOVE);
        root.get(DESCRIPTION).set(bundle.getString("interface.remove"));
        root.get(REQUEST_PROPERTIES).setEmptyObject();
        root.get(REPLY_PROPERTIES).setEmptyObject();
        return root;
    }

    private static ResourceBundle getResourceBundle(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return ResourceBundle.getBundle(RESOURCE_NAME, locale);
    }

    /**
     * Test whether the operation has a defined criteria attribute.
     *
     * @param operation the operation
     * @return
     */
    public static boolean isOperationDefined(final ModelNode operation) {
        for(final AttributeDefinition def : ROOT_ATTRIBUTES) {
            if(operation.hasDefined(def.getName())) {
                return true;
            }
        }
        return false;
    }

    /** The any-* alternatives. */
    private static final String[] ALTERNATIVES_ANY = new String[] { ModelDescriptionConstants.ANY_ADDRESS, ModelDescriptionConstants.ANY_IPV4_ADDRESS, ModelDescriptionConstants.ANY_IPV6_ADDRESS};

    /** All other attribute names. */
    private static final String[] OTHERS = new String[] { localName(Element.INET_ADDRESS), localName(Element.LINK_LOCAL_ADDRESS),
            localName(Element.LOOPBACK), localName(Element.LOOPBACK_ADDRESS), localName(Element.MULTICAST), localName(Element.NIC),
            localName(Element.NIC_MATCH), localName(Element.POINT_TO_POINT), localName(Element.PUBLIC_ADDRESS), localName(Element.SITE_LOCAL_ADDRESS),
            localName(Element.SUBNET_MATCH), localName(Element.UP), localName(Element.VIRTUAL),
            localName(Element.ANY), localName(Element.NOT)
    };

    public static final AttributeDefinition NAME = SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.NAME, ModelType.STRING,true)
            .build();

    public static final AttributeDefinition ANY_ADDRESS = SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.ANY_ADDRESS, ModelType.BOOLEAN)
            .setAllowExpression(false).setAllowNull(true).setRestartAllServices()
            .setValidator(new ModelTypeValidator(ModelType.BOOLEAN, true, false))
            .addAlternatives(OTHERS).addAlternatives(ModelDescriptionConstants.ANY_IPV4_ADDRESS, ModelDescriptionConstants.ANY_IPV6_ADDRESS)
            .build();
    public static final AttributeDefinition ANY_IPV4_ADDRESS = SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.ANY_IPV4_ADDRESS, ModelType.BOOLEAN)
            .setAllowExpression(false).setAllowNull(true).setRestartAllServices()
            .setValidator(new ModelTypeValidator(ModelType.BOOLEAN, true, false))
            .addAlternatives(OTHERS).addAlternatives(ModelDescriptionConstants.ANY_ADDRESS, ModelDescriptionConstants.ANY_IPV6_ADDRESS)
            .build();
    public static final AttributeDefinition ANY_IPV6_ADDRESS = SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.ANY_IPV6_ADDRESS, ModelType.BOOLEAN)
            .setAllowExpression(false).setAllowNull(true).setRestartAllServices()
            .setValidator(new ModelTypeValidator(ModelType.BOOLEAN, true, false))
            .addAlternatives(OTHERS).addAlternatives(ModelDescriptionConstants.ANY_ADDRESS, ModelDescriptionConstants.ANY_IPV4_ADDRESS)
            .build();
    public static final AttributeDefinition INET_ADDRESS = SimpleAttributeDefinitionBuilder.create(ModelDescriptionConstants.INET_ADDRESS, ModelType.STRING)
            .setAllowExpression(true).setAllowNull(true).addAlternatives(ALTERNATIVES_ANY).setRestartAllServices()
            .build();
    public static final AttributeDefinition LINK_LOCAL_ADDRESS = SimpleAttributeDefinitionBuilder.create(localName(Element.LINK_LOCAL_ADDRESS), ModelType.BOOLEAN)
            .setAllowExpression(false).setAllowNull(true).addAlternatives(ALTERNATIVES_ANY).setRestartAllServices()
            .build();
    public static final AttributeDefinition LOOPBACK = SimpleAttributeDefinitionBuilder.create(localName(Element.LOOPBACK), ModelType.BOOLEAN)
            .setAllowExpression(false).setAllowNull(true).addAlternatives(ALTERNATIVES_ANY).setRestartAllServices()
            .build();
    public static final AttributeDefinition LOOPBACK_ADDRESS = SimpleAttributeDefinitionBuilder.create(localName(Element.LOOPBACK_ADDRESS), ModelType.STRING)
            .setAllowExpression(true).setAllowNull(true).addAlternatives(ALTERNATIVES_ANY).setRestartAllServices()
            .build();
    public static final AttributeDefinition NIC = SimpleAttributeDefinitionBuilder.create(localName(Element.NIC), ModelType.STRING)
            .setAllowExpression(true).setAllowNull(true).addAlternatives(ALTERNATIVES_ANY).setRestartAllServices()
            .build();
    public static final AttributeDefinition NIC_MATCH = SimpleAttributeDefinitionBuilder.create(localName(Element.NIC_MATCH), ModelType.STRING)
            .setAllowExpression(true).setAllowNull(true).addAlternatives(ALTERNATIVES_ANY).setRestartAllServices()
            .build();
    public static final AttributeDefinition MULTICAST = SimpleAttributeDefinitionBuilder.create(localName(Element.MULTICAST), ModelType.BOOLEAN)
            .setAllowExpression(false).setAllowNull(true).addAlternatives(ALTERNATIVES_ANY).setRestartAllServices()
            .build();
    public static final AttributeDefinition POINT_TO_POINT = SimpleAttributeDefinitionBuilder.create(localName(Element.POINT_TO_POINT), ModelType.BOOLEAN)
            .setAllowExpression(false).setAllowNull(true).addAlternatives(ALTERNATIVES_ANY).setRestartAllServices()
            .build();
    public static final AttributeDefinition PUBLIC_ADDRESS = SimpleAttributeDefinitionBuilder.create(localName(Element.PUBLIC_ADDRESS), ModelType.BOOLEAN)
            .setAllowExpression(false).setAllowNull(true).addAlternatives(ALTERNATIVES_ANY).setRestartAllServices()
            .build();
    public static final AttributeDefinition SITE_LOCAL_ADDRESS = SimpleAttributeDefinitionBuilder.create(localName(Element.SITE_LOCAL_ADDRESS), ModelType.BOOLEAN)
            .setAllowExpression(false).setAllowNull(true).addAlternatives(ALTERNATIVES_ANY).setRestartAllServices()
            .build();
    public static final AttributeDefinition SUBNET_MATCH = SimpleAttributeDefinitionBuilder.create(localName(Element.SUBNET_MATCH), ModelType.STRING)
            .setAllowExpression(true).setAllowNull(true).addAlternatives(ALTERNATIVES_ANY).setRestartAllServices()
            .build();
    public static final AttributeDefinition UP = SimpleAttributeDefinitionBuilder.create(localName(Element.UP), ModelType.BOOLEAN)
            .setAllowExpression(false).setAllowNull(true).addAlternatives(ALTERNATIVES_ANY).setRestartAllServices()
            .build();
    public static final AttributeDefinition VIRTUAL = SimpleAttributeDefinitionBuilder.create(localName(Element.VIRTUAL), ModelType.BOOLEAN)
            .setAllowExpression(false).setAllowNull(true).addAlternatives(ALTERNATIVES_ANY).setRestartAllServices()
            .build();
    public static final AttributeDefinition NOT = createNestedComplexType("not");
    public static final AttributeDefinition ANY = createNestedComplexType("any");

    /** The root attributes. */
    public static final AttributeDefinition[] ROOT_ATTRIBUTES = new AttributeDefinition[] {

            ANY_ADDRESS, ANY_IPV4_ADDRESS, ANY_IPV6_ADDRESS, INET_ADDRESS, LINK_LOCAL_ADDRESS,
            LOOPBACK, LOOPBACK_ADDRESS, MULTICAST, NAME, NIC, NIC_MATCH, POINT_TO_POINT, PUBLIC_ADDRESS,
            SITE_LOCAL_ADDRESS, SUBNET_MATCH, UP, VIRTUAL, ANY, NOT

    };

    /** The nested attributes for any, not. */
    public static final AttributeDefinition[] NESTED_ATTRIBUTES = new AttributeDefinition[] {
            INET_ADDRESS, LINK_LOCAL_ADDRESS, LOOPBACK, LOOPBACK_ADDRESS, MULTICAST, NIC,
            NIC_MATCH, POINT_TO_POINT, PUBLIC_ADDRESS, SITE_LOCAL_ADDRESS, SUBNET_MATCH, UP, VIRTUAL
    };


    public static final Set<AttributeDefinition> NESTED_LIST_ATTRIBUTES = new HashSet<AttributeDefinition>(
            Arrays.asList(INET_ADDRESS, NIC, NIC_MATCH, SUBNET_MATCH )
    );

    /** The wildcard criteria attributes */
    public static final AttributeDefinition[] WILDCARD_ATTRIBUTES = new AttributeDefinition[] {ANY_ADDRESS, ANY_IPV4_ADDRESS, ANY_IPV6_ADDRESS };

    public static final AttributeDefinition[] SIMPLE_ATTRIBUTES = new AttributeDefinition[] { LINK_LOCAL_ADDRESS, LOOPBACK,
            MULTICAST, POINT_TO_POINT, PUBLIC_ADDRESS, SITE_LOCAL_ADDRESS, UP, VIRTUAL };

    /**
     * Create the AttributeDefinition for the nested 'not' / 'any' type.
     *
     * @param name the name
     * @return the attribute definition
     */
    private static AttributeDefinition createNestedComplexType(final String name) {
        return new AttributeDefinition(name, name, null, ModelType.OBJECT, true, false, MeasurementUnit.NONE, createNestedParamValidator(), ALTERNATIVES_ANY, null, AttributeAccess.Flag.RESTART_ALL_SERVICES) {
            @Override
            public void marshallAsElement(ModelNode resourceModel, XMLStreamWriter writer) throws XMLStreamException {
                throw new UnsupportedOperationException();
            }

            @Override
            public ModelNode addResourceAttributeDescription(final ResourceBundle bundle, final String prefix, final ModelNode resourceDescription) {
                final ModelNode result = super.addResourceAttributeDescription(bundle, prefix, resourceDescription);
                addNestedDescriptions(result, prefix, bundle);
                return result;
            }

            @Override
            public ModelNode addOperationParameterDescription(ResourceBundle bundle, String prefix, ModelNode operationDescription) {
                final ModelNode result = super.addOperationParameterDescription(bundle, prefix, operationDescription);    //To change body of overridden methods use File | Settings | File Templates.
                addNestedDescriptions(result, prefix, bundle);
                return result;
            }

            void addNestedDescriptions(final ModelNode result, final String prefix, final ResourceBundle bundle) {
                for(final AttributeDefinition def : NESTED_ATTRIBUTES) {
                    final String bundleKey = prefix == null ? def.getName() : (prefix + "." + def.getName());
                    result.get(VALUE_TYPE, def.getName(), DESCRIPTION).set(bundle.getString(bundleKey));
                }
            }

            @Override
            public ModelNode getNoTextDescription(boolean forOperation) {
                final ModelNode model = super.getNoTextDescription(forOperation);
                final ModelNode valueType = model.get(VALUE_TYPE);
                for(final AttributeDefinition def : NESTED_ATTRIBUTES) {
                    final AttributeDefinition current;
                    if(NESTED_LIST_ATTRIBUTES.contains(def)) {
                        current = wrapAsList(def);
                    } else {
                        current = def;
                    }
                    final ModelNode m = current.getNoTextDescription(forOperation);
                    m.remove(ALTERNATIVES);
                    valueType.get(current.getName()).set(m);
                }
                return model;
            }
        };

    }

    /**
     * Wrap a simple attribute def as list.
     *
     * @param def the attribute definition
     * @return the list attribute def
     */
    private static ListAttributeDefinition wrapAsList(final AttributeDefinition def) {
        final ListAttributeDefinition list = new ListAttributeDefinition(def.getName(), true, def.getValidator()) {

            @Override
            public ModelNode getNoTextDescription(boolean forOperation) {
                final ModelNode model = super.getNoTextDescription(forOperation);
                setValueType(model);
                return model;
            }

            @Override
            protected void addValueTypeDescription(final ModelNode node, final ResourceBundle bundle) {
                setValueType(node);
            }
            @Override
            public void marshallAsElement(final ModelNode resourceModel, final XMLStreamWriter writer) throws XMLStreamException {
                throw new RuntimeException();
            }

            @Override
            protected void addAttributeValueTypeDescription(ModelNode node, ResourceDescriptionResolver resolver, Locale locale, ResourceBundle bundle) {
                setValueType(node);
            }

            @Override
            protected void addOperationParameterValueTypeDescription(ModelNode node, String operationName, ResourceDescriptionResolver resolver, Locale locale, ResourceBundle bundle) {
                setValueType(node);
            }
            private void setValueType(ModelNode node) {
                node.get(ModelDescriptionConstants.VALUE_TYPE).set(ModelType.STRING);
            }
        };
        return list;
    }

    /**
     * Create the nested complex attribute parameter validator.
     *
     * @return the parameter validator
     */
    static ParameterValidator createNestedParamValidator() {
        return new ModelTypeValidator(ModelType.OBJECT, true, false, true) {
            @Override
            public void validateParameter(final String parameterName, final ModelNode value) throws OperationFailedException {
                super.validateParameter(parameterName, value);

                for(final AttributeDefinition def : NESTED_ATTRIBUTES) {
                    final String name = def.getName();
                    if(value.hasDefined(name)) {
                        final ModelNode v = value.get(name);
                        if(NESTED_LIST_ATTRIBUTES.contains(def)) {
                            if (ModelType.LIST != v.getType()) {
                                throw new OperationFailedException(new ModelNode().set(MESSAGES.invalidType(v.getType())));
                            }
                        } else {
                            def.getValidator().validateParameter(name, v);
                        }
                    }
                }
            }
        };
    }

    static String localName(final Element element) {
        return element.getLocalName();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3838.java