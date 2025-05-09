error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7536.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7536.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7536.java
text:
```scala
t@@hrow new OperationFailedException(MessagingMessages.MESSAGES.hqServerManagementServiceResourceNotFound(PathAddress.pathAddress(operation.require(OP_ADDR))));

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

package org.jboss.as.messaging;

import static org.jboss.as.controller.SimpleAttributeDefinitionBuilder.create;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.START;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.STOP;
import static org.jboss.as.messaging.CommonAttributes.NAME;
import static org.jboss.as.messaging.MessagingLogger.ROOT_LOGGER;
import static org.jboss.as.messaging.MessagingMessages.MESSAGES;
import static org.jboss.dmr.ModelType.BOOLEAN;

import org.hornetq.api.core.management.HornetQComponentControl;
import org.hornetq.core.server.HornetQServer;
import org.jboss.as.controller.AbstractRuntimeOnlyHandler;
import org.jboss.as.controller.ControllerMessages;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationDefinition;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleOperationDefinitionBuilder;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.ResourceDescriptionResolver;
import org.jboss.as.controller.operations.validation.ParametersValidator;
import org.jboss.as.controller.operations.validation.StringLengthValidator;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;

/**
 * Base class for {@link org.jboss.as.controller.OperationStepHandler} implementations for handlers that interact
 * with an implementation of a {@link HornetQComponentControl} subinterface to perform their function.  This base class
 * handles a "start" and "stop" operation as well as a "read-attribute" call reading runtime attribute "started".
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public abstract class AbstractHornetQComponentControlHandler<T extends HornetQComponentControl> extends AbstractRuntimeOnlyHandler {

    private static final SimpleAttributeDefinition STARTED = create(CommonAttributes.STARTED, BOOLEAN)
            .setFlags(AttributeAccess.Flag.STORAGE_RUNTIME)
            .build();

    private ParametersValidator readAttributeValidator = new ParametersValidator();

    protected AbstractHornetQComponentControlHandler() {
        readAttributeValidator.registerValidator(NAME, new StringLengthValidator(1));
    }

    @Override
    protected void executeRuntimeStep(OperationContext context, ModelNode operation) throws OperationFailedException {

        final String operationName = operation.require(OP).asString();

        HornetQComponentControl control = null;
        boolean appliedToRuntime = false;
        Object handback = null;
        if (READ_ATTRIBUTE_OPERATION.equals(operationName)) {
            readAttributeValidator.validate(operation);
            final String name = operation.require(NAME).asString();
            if (STARTED.getName().equals(name)) {
                control = getHornetQComponentControl(context, operation, false);
                context.getResult().set(control.isStarted());
            } else {
                handleReadAttribute(name, context, operation);
            }
        } else if (START.equals(operationName)) {
            control = getHornetQComponentControl(context, operation, true);
            try {
                control.start();
                appliedToRuntime = true;
                context.getResult();
            } catch (Exception e) {
                context.getFailureDescription().set(e.getLocalizedMessage());
            }

        } else if (STOP.equals(operationName)) {
            control = getHornetQComponentControl(context, operation, true);
            try {
                control.stop();
                appliedToRuntime = true;
                context.getResult();
            } catch (Exception e) {
                context.getFailureDescription().set(e.getLocalizedMessage());
            }
        } else {
            handback = handleOperation(operationName, context, operation);
            appliedToRuntime = handback != null;
        }


        OperationContext.RollbackHandler rh;

        if (appliedToRuntime)  {
            final HornetQComponentControl rhControl = control;
            final Object rhHandback = handback;
            rh = new OperationContext.RollbackHandler() {
                @Override
                public void handleRollback(OperationContext context, ModelNode operation) {
                    try {
                        if (START.equals(operationName)) {
                            rhControl.stop();
                        } else if (STOP.equals(operationName)) {
                            rhControl.start();
                        } else {
                            handleRevertOperation(operationName, context, operation, rhHandback);
                        }
                    } catch (Exception e) {
                        ROOT_LOGGER.revertOperationFailed(e, getClass().getSimpleName(),
                                operation.require(ModelDescriptionConstants.OP).asString(),
                                PathAddress.pathAddress(operation.require(ModelDescriptionConstants.OP_ADDR)));
                    }
                }
            };
        } else {
            rh = OperationContext.RollbackHandler.NOOP_ROLLBACK_HANDLER;
        }

        context.completeStep(rh);
    }

    public void registerAttributes(final ManagementResourceRegistration registry) {
        registry.registerReadOnlyAttribute(STARTED, this);
    }

    public void registerOperations(final ManagementResourceRegistration registry, final ResourceDescriptionResolver resolver) {
        final OperationDefinition startOp = new SimpleOperationDefinitionBuilder(START, resolver)
                .build();
        registry.registerOperationHandler(startOp, this);
        final OperationDefinition stopOp = new SimpleOperationDefinitionBuilder(STOP, resolver)
        .build();
        registry.registerOperationHandler(stopOp, this);
    }

    /**
     * Gets the {@link HornetQComponentControl} implementation used by this handler.
     *
     * @param hqServer the HornetQServer installed in the runtime
     * @param address the address being invoked
     * @return the runtime HornetQ control object associated with the given address
     */
    protected abstract T getHornetQComponentControl(HornetQServer hqServer, PathAddress address);

    protected abstract String getDescriptionPrefix();

    /**
     * Hook to allow subclasses to handle read-attribute requests for attributes other than {@link CommonAttributes#STARTED}.
     * Implementations must not call any of the
     * {@link org.jboss.as.controller.OperationContext#completeStep(OperationContext.ResultHandler) context.completeStep variants}.
     * <p>
     * This default implementation just throws the exception returned by {@link #unsupportedAttribute(String)}.
     * </p>
     *
     *
     * @param attributeName the name of the attribute
     * @param context the operation context
     * @param operation
     * @throws OperationFailedException
     */
    protected void handleReadAttribute(String attributeName, OperationContext context, ModelNode operation) throws OperationFailedException {
        unsupportedAttribute(attributeName);
    }

    /**
     * Hook to allow subclasses to handle operations other than {@code read-attribute}, {@code start} and
     * {@code stop}. Implementations must not call any of the
     * {@link org.jboss.as.controller.OperationContext#completeStep(OperationContext.ResultHandler) context.completeStep variants}.
     * <p>
     * This default implementation just throws the exception returned by {@link #unsupportedOperation(String)}.
     * </p>
     *
     *
     * @param operationName the name of the operation
     * @param context the operation context
     * @param operation the operation
     *
     * @return an object that can be passed back in {@link #handleRevertOperation(String, org.jboss.as.controller.OperationContext, org.jboss.dmr.ModelNode, Object)}
     *         if the operation should be reverted. A value of {@code null} is an indication that no reversible
     *         modification was made
     * @throws OperationFailedException
     */
    protected Object handleOperation(String operationName, OperationContext context, ModelNode operation) throws OperationFailedException {
        unsupportedOperation(operationName);
        throw MESSAGES.unsupportedOperation(operationName);
    }

    /**
     * Hook to allow subclasses to handle revert changes made in
     * {@link #handleOperation(String, org.jboss.as.controller.OperationContext, org.jboss.dmr.ModelNode)}.
     * <p>
     * This default implementation does nothing.
     * </p>
     *
     *
     * @param operationName the name of the operation
     * @param context the operation context
     * @param operation the operation
     */
    protected void handleRevertOperation(String operationName, OperationContext context, ModelNode operation, Object handback) {
    }

    /**
     * Return an ISE with a message saying support for the attribute was not properly implemented. This handler should
     * only be called if for a "read-attribute" operation if {@link #registerOperations(ManagementResourceRegistration, ResourceDescriptionResolver)}
     * registers the attribute, so a handler then not recognizing the attribute name would be a bug and this method
     * returns an exception highlighting that bug.
     *
     * @param attributeName the name of the attribute
     * @throws IllegalStateException an exception with a message indicating a bug in this handler
     */
    protected final void unsupportedAttribute(final String attributeName) {
        // Bug
        throw MESSAGES.unsupportedAttribute(attributeName);
    }

    /**
     * Return an ISE with a message saying support for the operation was not properly implemented. This handler should
     * only be called if for a n operation if {@link #registerOperations(ManagementResourceRegistration, ResourceDescriptionResolver)}
     * registers it as a handler, so a handler then not recognizing the operation name would be a bug and this method
     * returns an exception highlighting that bug.
     *
     * @param operationName the name of the attribute
     * @throws IllegalStateException an exception with a message indicating a bug in this handler
     */
    protected final void unsupportedOperation(final String operationName) {
        // Bug
        throw MESSAGES.unsupportedOperation(operationName);
    }

    /**
     * Gets the runtime HornetQ control object that can help service this request.
     *
     * @param context  the operation context
     * @param operation the operation
     * @param forWrite {@code true} if this operation will modify the runtime; {@code false} if not.
     * @return the control object
     * @throws OperationFailedException
     */
    protected final T getHornetQComponentControl(final OperationContext context, final ModelNode operation, final boolean forWrite) throws OperationFailedException {
        final ServiceName hqServiceName = MessagingServices.getHornetQServiceName(PathAddress.pathAddress(operation.get(ModelDescriptionConstants.OP_ADDR)));
        ServiceController<?> hqService = context.getServiceRegistry(forWrite).getService(hqServiceName);
        HornetQServer server = HornetQServer.class.cast(hqService.getValue());
        PathAddress address = PathAddress.pathAddress(operation.require(OP_ADDR));
         T control = getHornetQComponentControl(server, address);
         if (control == null) {
             throw new OperationFailedException(ControllerMessages.MESSAGES.noHandler(READ_ATTRIBUTE_OPERATION, PathAddress.pathAddress(operation.require(OP_ADDR))));
         }
         return control;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7536.java