error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13258.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13258.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13258.java
text:
```scala
c@@ontext.addStep(responseMap.get(stepName).setEmptyObject(), subOperation, stepHandlerMap.get(stepName), OperationContext.Stage.MODEL, true);

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

package org.jboss.as.controller;

import static org.jboss.as.controller.ControllerMessages.MESSAGES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.common.ControllerResolver;
import org.jboss.as.controller.registry.ImmutableManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * Handler for the "composite" operation; i.e. one that includes one or more child operations
 * as steps.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public final class CompositeOperationHandler implements OperationStepHandler {

    @Deprecated
    public static final OperationContext.AttachmentKey<Boolean> DOMAIN_EXECUTION_KEY = OperationContext.AttachmentKey.create(Boolean.class);

    public static final CompositeOperationHandler INSTANCE = new CompositeOperationHandler();
    public static final String NAME = ModelDescriptionConstants.COMPOSITE;

    private static final AttributeDefinition STEPS = new PrimitiveListAttributeDefinition.Builder(ModelDescriptionConstants.STEPS, ModelType.OBJECT)
            .build();

    public static final OperationDefinition DEFINITION = new SimpleOperationDefinitionBuilder(NAME, ControllerResolver.getResolver("root"))
        .addParameter(STEPS)
        .setReplyType(ModelType.OBJECT)
        .setPrivateEntry()
        .build();

    private CompositeOperationHandler() {
    }

    public void execute(final OperationContext context, final ModelNode operation) throws OperationFailedException {
        STEPS.validateOperation(operation);

        ImmutableManagementResourceRegistration registry = context.getResourceRegistration();
        final List<ModelNode> list = operation.get(ModelDescriptionConstants.STEPS).asList();
        final ModelNode responseMap = context.getResult().setEmptyObject();
        Map<String, OperationStepHandler> stepHandlerMap = new HashMap<String, OperationStepHandler>();
        final int size = list.size();
        // Validate all needed handlers are available.
        for (int i = 0; i < size; i++) {
            String stepName = "step-" + (i+1);
            // This makes the result steps appear in the correct order
            responseMap.get(stepName);
            final ModelNode subOperation = list.get(i);
            PathAddress stepAddress = PathAddress.pathAddress(subOperation.get(OP_ADDR));
            String stepOpName = subOperation.require(OP).asString();
            OperationStepHandler stepHandler = registry.getOperationHandler(stepAddress, stepOpName);
            if (stepHandler == null) {
                ImmutableManagementResourceRegistration child = registry.getSubModel(stepAddress);
                if (child == null) {
                   context.getFailureDescription().set(MESSAGES.noSuchResourceType(stepAddress));
                } else {
                    context.getFailureDescription().set(MESSAGES.noHandlerForOperation(stepOpName, stepAddress));
                }
                context.completeStep(OperationContext.RollbackHandler.NOOP_ROLLBACK_HANDLER);
                return;
            }
            stepHandlerMap.put(stepName, stepHandler);
        }

        for (int i = size - 1; i >= 0; i --) {
            final ModelNode subOperation = list.get(i);
            String stepName = "step-" + (i+1);
            context.addStep(responseMap.get(stepName).setEmptyObject(), subOperation, stepHandlerMap.get(stepName), OperationContext.Stage.IMMEDIATE);
        }

        context.completeStep(new OperationContext.RollbackHandler() {
            @Override
            public void handleRollback(OperationContext context, ModelNode operation) {
                // don't override useful failure information in the domain
                if (context.getAttachment(DOMAIN_EXECUTION_KEY) != null) {
                    return;
                }

                final ModelNode failureMsg = new ModelNode();
                for (int i = 0; i < size; i++) {
                    String stepName = "step-" + (i+1);
                    ModelNode stepResponse = responseMap.get(stepName);
                    if (stepResponse.hasDefined(FAILURE_DESCRIPTION)) {
                        failureMsg.get(MESSAGES.compositeOperationFailed(), MESSAGES.operation(stepName)).set(stepResponse.get(FAILURE_DESCRIPTION));
                    }
                }
                if (!failureMsg.isDefined()) {
                    failureMsg.set(MESSAGES.compositeOperationRolledBack());
                }
                context.getFailureDescription().set(failureMsg);
            }
        });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13258.java