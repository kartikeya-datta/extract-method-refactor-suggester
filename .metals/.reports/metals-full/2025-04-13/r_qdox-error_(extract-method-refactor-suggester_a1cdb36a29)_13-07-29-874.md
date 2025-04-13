error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5205.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5205.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5205.java
text:
```scala
c@@ontext.getLogger().logAttributeWarning(address, MESSAGES.invalidJSFSlotValue(slot.asString()), SLOT_ATTRIBUTE_NAME);

/*
* JBoss, Home of Professional Open Source.
* Copyright 2013, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.domain.controller.transformers;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.UNDEFINE_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;
import static org.jboss.as.domain.controller.DomainControllerMessages.MESSAGES;
import static org.jboss.as.domain.controller.transformers.DomainTransformers.IGNORED_SUBSYSTEMS;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jboss.as.controller.ControllerMessages;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.transform.OperationRejectionPolicy;
import org.jboss.as.controller.transform.OperationResultTransformer;
import org.jboss.as.controller.transform.OperationTransformer;
import org.jboss.as.controller.transform.ResourceTransformationContext;
import org.jboss.as.controller.transform.ResourceTransformer;
import org.jboss.as.controller.transform.TransformationContext;
import org.jboss.as.controller.transform.TransformerRegistry;
import org.jboss.as.controller.transform.TransformersSubRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;

/**
 * @author <a href="http://jmesnil.net">Jeff Mesnil</a> (c) 2012 Red Hat, inc
 */
class JSFSubsystemTransformers {

    private static final String JSF_SUBSYSTEM = "jsf";
    private static final String SLOT_ATTRIBUTE_NAME = "default-jsf-impl-slot";
    private static final String SLOT_DEFAULT_VALUE = "main";

    static void registerTransformers120(TransformerRegistry registry, TransformersSubRegistration parent) {
        registry.registerSubsystemTransformers(JSF_SUBSYSTEM, IGNORED_SUBSYSTEMS, new ResourceTransformer() {
            @Override
            public void transformResource(ResourceTransformationContext context, PathAddress address, Resource resource) throws OperationFailedException {
                ModelNode model = resource.getModel();
                if (model.hasDefined(SLOT_ATTRIBUTE_NAME)) {
                    ModelNode slot = model.get(SLOT_ATTRIBUTE_NAME);
                    if (!SLOT_DEFAULT_VALUE.equals(slot.asString())) {
                        context.getLogger().logAttributeWarning(address, SLOT_ATTRIBUTE_NAME, MESSAGES.invalidJSFSlotValue(slot.asString()));
                    }
                }
                Set<String> attributes = new HashSet<String>();
                for (Property prop : resource.getModel().asPropertyList()) {
                    attributes.add(prop.getName());
                }
                attributes.remove(SLOT_ATTRIBUTE_NAME);
                if (!attributes.isEmpty()) {
                    context.getLogger().logAttributeWarning(address, ControllerMessages.MESSAGES.attributesAreNotUnderstoodAndMustBeIgnored(), attributes);
                }
            }
        });

        TransformersSubRegistration jsfSubsystem = parent.registerSubResource(PathElement.pathElement(SUBSYSTEM, JSF_SUBSYSTEM));
        jsfSubsystem.registerOperationTransformer(ADD, new OperationTransformer() {

            @Override
            public TransformedOperation transformOperation(TransformationContext context, PathAddress address, ModelNode operation) throws OperationFailedException {
                if (operation.hasDefined(SLOT_ATTRIBUTE_NAME)) {
                    ModelNode slot = operation.get(SLOT_ATTRIBUTE_NAME);
                    if (!SLOT_DEFAULT_VALUE.equals(slot.asString())) {
                        return new TransformedOperation(operation,
                                new RejectionWithFailurePolicy(MESSAGES.invalidJSFSlotValue(slot.asString())),
                                OperationResultTransformer.ORIGINAL_RESULT);
                    }
                }
                Set<String> attributes = new HashSet<String>();
                for (Property prop : operation.asPropertyList()) {
                    attributes.add(prop.getName());
                }
                attributes.remove(SLOT_ATTRIBUTE_NAME);
                if (!attributes.isEmpty()) {
                    return new TransformedOperation(operation,
                            new RejectionWithFailurePolicy(MESSAGES.unknownAttributesFromSubsystemVersion(ADD,
                                    JSF_SUBSYSTEM,
                                    context.getTarget().getSubsystemVersion(JSF_SUBSYSTEM),
                                    attributes)),
                            OperationResultTransformer.ORIGINAL_RESULT);
                }
                return DISCARD.transformOperation(context, address, operation);
            }
        });

        jsfSubsystem.registerOperationTransformer(WRITE_ATTRIBUTE_OPERATION, new OperationTransformer() {
            @Override
            public TransformedOperation transformOperation(TransformationContext context, PathAddress address, ModelNode operation) throws OperationFailedException {
                final String name = operation.require(NAME).asString();
                final ModelNode value = operation.get(ModelDescriptionConstants.VALUE);
                if (SLOT_ATTRIBUTE_NAME.equals(name)) {
                    if (value.isDefined() && value.equals(SLOT_DEFAULT_VALUE)) {
                        return DISCARD.transformOperation(context, address, operation);
                    } else {
                        return new TransformedOperation(operation,
                                new RejectionWithFailurePolicy(MESSAGES.invalidJSFSlotValue(value.asString())),
                                OperationResultTransformer.ORIGINAL_RESULT);
                    }
                }
                // reject the operation for any other attribute
                return new TransformedOperation(operation,
                        new RejectionWithFailurePolicy(MESSAGES.unknownAttributesFromSubsystemVersion(ADD,
                                JSF_SUBSYSTEM,
                                context.getTarget().getSubsystemVersion(JSF_SUBSYSTEM),
                                Arrays.asList(name))),
                        OperationResultTransformer.ORIGINAL_RESULT);
            }
        });
        jsfSubsystem.registerOperationTransformer(UNDEFINE_ATTRIBUTE_OPERATION, new OperationTransformer() {
            @Override
            public TransformedOperation transformOperation(TransformationContext context, PathAddress address, ModelNode operation) throws OperationFailedException {
                String attributeName = operation.require(NAME).asString();
                if (!SLOT_ATTRIBUTE_NAME.equals(attributeName)) {
                    return DEFAULT.transformOperation(context, address, operation);
                } else {
                    context.getLogger().logAttributeWarning(address, ControllerMessages.MESSAGES.attributesAreNotUnderstoodAndMustBeIgnored(), attributeName);
                    return DISCARD.transformOperation(context, address, operation);
                }
            }
        });
    }


    private static class RejectionWithFailurePolicy implements OperationRejectionPolicy {
        private final String failureDescription;

        public RejectionWithFailurePolicy(String failureDescription) {
            this.failureDescription = failureDescription;
        }

        @Override
        public boolean rejectOperation(ModelNode preparedResult) {
            return true;
        }

        @Override
        public String getFailureDescription() {
            return failureDescription;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5205.java