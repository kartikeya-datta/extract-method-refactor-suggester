error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12005.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12005.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12005.java
text:
```scala
.@@setAllowExpression(true).setValidator(new IntRangeValidator(-65535, 65535, true, true))

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

package org.jboss.as.controller.resource;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.INTERFACE;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.AttributeMarshaller;
import org.jboss.as.controller.ControllerMessages;
import org.jboss.as.controller.ListAttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.PrimitiveListAttributeDefinition;
import org.jboss.as.controller.ReloadRequiredWriteAttributeHandler;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.common.ControllerResolver;
import org.jboss.as.controller.operations.validation.IntRangeValidator;
import org.jboss.as.controller.operations.validation.StringLengthValidator;
import org.jboss.as.controller.parsing.Attribute;
import org.jboss.as.controller.parsing.Element;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.controller.registry.Resource;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * {@link ResourceDefinition} for a resource representing a socket binding group.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class SocketBindingGroupResourceDefinition extends SimpleResourceDefinition {

    // Common attributes

    public static final SimpleAttributeDefinition NAME = new SimpleAttributeDefinitionBuilder(ModelDescriptionConstants.NAME, ModelType.STRING, false)
            .setResourceOnly()
            .setValidator(new StringLengthValidator(1)).build();

    public static final SimpleAttributeDefinition DEFAULT_INTERFACE = new SimpleAttributeDefinitionBuilder(ModelDescriptionConstants.DEFAULT_INTERFACE, ModelType.STRING, false)
            .setAllowExpression(true).setValidator(new StringLengthValidator(1, Integer.MAX_VALUE, false, true))
            .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();

    // Server-only attributes.

    public static final SimpleAttributeDefinition PORT_OFFSET = new SimpleAttributeDefinitionBuilder(ModelDescriptionConstants.PORT_OFFSET, ModelType.INT, true)
            .setAllowExpression(true).setValidator(new IntRangeValidator(0, 65535, true, true))
            .setDefaultValue(new ModelNode().set(0)).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();

    // Domain-only attributes
    public static final ListAttributeDefinition INCLUDES = new PrimitiveListAttributeDefinition.Builder(ModelDescriptionConstants.INCLUDES, ModelType.STRING)
            .setXmlName(Element.INCLUDE.getLocalName())
            .setAllowNull(true)
            .setMinSize(0)
            .setMaxSize(Integer.MAX_VALUE)
            .setValidator(new StringLengthValidator(1, true))
            .setAttributeMarshaller(new AttributeMarshaller() {
                @Override
                public void marshallAsElement(AttributeDefinition attribute, ModelNode resourceModel, boolean marshallDefault, XMLStreamWriter writer) throws XMLStreamException {
                    if (isMarshallable(attribute, resourceModel)) {
                        for (ModelNode included : resourceModel.get(attribute.getName()).asList()) {
                            writer.writeEmptyElement(attribute.getXmlName());
                            writer.writeAttribute(Attribute.SOCKET_BINDING_GROUP.getLocalName(), included.asString());
                        }
                    }
                }
            })
            .setFlags(AttributeAccess.Flag.RESTART_JVM)
            .build();

    private final boolean forDomainModel;
    private final ResourceDefinition[] children;


    public SocketBindingGroupResourceDefinition(final OperationStepHandler addHandler, final OperationStepHandler removeHandler, final boolean forDomainModel, ResourceDefinition...children) {
        super(PathElement.pathElement(ModelDescriptionConstants.SOCKET_BINDING_GROUP),
                ControllerResolver.getResolver(ModelDescriptionConstants.SOCKET_BINDING_GROUP),
                addHandler, removeHandler, OperationEntry.Flag.RESTART_ALL_SERVICES, OperationEntry.Flag.RESTART_ALL_SERVICES);
        this.forDomainModel = forDomainModel;
        this.children = children;
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
        super.registerAttributes(resourceRegistration);

        resourceRegistration.registerReadOnlyAttribute(NAME, null);
        resourceRegistration.registerReadWriteAttribute(DEFAULT_INTERFACE, null, new ReloadRequiredWriteAttributeHandler(DEFAULT_INTERFACE) {
            protected void validateUpdatedModel(final OperationContext context, final Resource model) throws OperationFailedException {
                validateDefaultInterfaceReference(context, model.getModel());
            }
        });

        if (forDomainModel) {
            /* This will be reintroduced for 7.2.0, leave commented out
            resourceRegistration.registerReadWriteAttribute(INCLUDES, null, new WriteAttributeHandlers.AttributeDefinitionValidatingHandler(INCLUDES));
            */
        } else {
            resourceRegistration.registerReadWriteAttribute(PORT_OFFSET, null, new ReloadRequiredWriteAttributeHandler(PORT_OFFSET));
        }
    }

    @Override
    public void registerOperations(ManagementResourceRegistration resourceRegistration) {
        super.registerOperations(resourceRegistration);
        /* This will be reintroduced for 7.2.0, leave commented out
        if (forDomainModel) {
            resourceRegistration.registerOperationHandler(SocketBindingGroupIncludeAddHandler.OPERATION_NAME, SocketBindingGroupIncludeAddHandler.INSTANCE, SocketBindingGroupIncludeAddHandler.INSTANCE);
            resourceRegistration.registerOperationHandler(SocketBindingGroupIncludeRemoveHandler.OPERATION_NAME, SocketBindingGroupIncludeRemoveHandler.INSTANCE, SocketBindingGroupIncludeRemoveHandler.INSTANCE);
        }
        */
    }

    @Override
    public void registerChildren(ManagementResourceRegistration resourceRegistration) {
        for (ResourceDefinition child : children) {
            resourceRegistration.registerSubModel(child);
        }
    }

    public static void validateDefaultInterfaceReference(final OperationContext context, final ModelNode bindingGroup) throws OperationFailedException {

        ModelNode defaultInterfaceNode = bindingGroup.get(DEFAULT_INTERFACE.getName());
        if (defaultInterfaceNode.getType() == ModelType.STRING) { // ignore UNDEFINED and EXPRESSION
            String defaultInterface = defaultInterfaceNode.asString();
            PathAddress interfaceAddress = PathAddress.pathAddress(PathElement.pathElement(INTERFACE, defaultInterface));
            try {
                context.readResourceFromRoot(interfaceAddress, false);
            } catch (RuntimeException e) {
                throw ControllerMessages.MESSAGES.nonexistentInterface(defaultInterface, DEFAULT_INTERFACE.getName());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12005.java