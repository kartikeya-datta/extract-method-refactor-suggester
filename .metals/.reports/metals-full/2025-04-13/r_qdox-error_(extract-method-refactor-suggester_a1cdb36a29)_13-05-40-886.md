error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11085.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11085.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11085.java
text:
```scala
b@@oolean applyToRuntime = context.isNormalServer();

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

package org.jboss.as.server.operations;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.operations.global.WriteAttributeHandlers.WriteAttributeOperationHandler;
import org.jboss.as.controller.operations.validation.ParameterValidator;
import org.jboss.as.server.ServerLogger;
import org.jboss.dmr.ModelNode;

/**
 * Abstract superclass for write-attribute operation handlers that run on the
 * server.
 *
 * @author Brian Stansberry
 *
 * @deprecated Use org.jboss.as.controller.AbstractWriteAttributeHandler
 */
@Deprecated
public class ServerWriteAttributeOperationHandler extends WriteAttributeOperationHandler {

    private final ParameterValidator resolvedValueValidator;

    /**
     * Creates a handler that doesn't validate values.
     */
    public ServerWriteAttributeOperationHandler() {
        this(null, null);
    }

    /**
     * Creates a handler that users the given {@code valueValidator}
     * to validate values before applying them to the model.
     *
     * @param valueValidator the validator to use to validate the value. May be {@code null}
     */
    public ServerWriteAttributeOperationHandler(ParameterValidator valueValidator) {
        this(valueValidator, null);
    }

    /**
     * Creates a handler that users the given {@code attributeDefinition}
     * to validate values before applying them to the model.
     *
     * @param attributeDefinition the definition of the attribute being written
     */
    public ServerWriteAttributeOperationHandler(AttributeDefinition attributeDefinition) {
        this(attributeDefinition.getValidator(), attributeDefinition.getValidator());
    }

    /**
     * Creates a handler that uses the given {@code valueValidator}
     * to validate values before applying them to the model, and a separate
     * validator to validate the {@link ModelNode#resolve() resolved value}
     * after it has been applied to the model.
     * <p/>
     * Typically if this constructor is used the {@code valueValidator} would
     * allow expressions, while the {@code resolvedValueValidator} would not.
     *
     * @param valueValidator         the validator to use to validate the value before application to the model. May be {@code null}     *
     * @param resolvedValueValidator the validator to use to validate the value before application to the model. May be {@code null}
     */
    public ServerWriteAttributeOperationHandler(ParameterValidator valueValidator, ParameterValidator resolvedValueValidator) {
        super(valueValidator);
        this.resolvedValueValidator = resolvedValueValidator;
    }

    @Override
    protected void modelChanged(final OperationContext context, final ModelNode operation,
                                final String attributeName, final ModelNode newValue, final ModelNode currentValue) throws OperationFailedException {

        boolean restartRequired = false;
        boolean applyToRuntime = context.getType() == OperationContext.Type.SERVER;
        ModelNode resolvedValue = null;
        if (applyToRuntime) {
            validateResolvedValue(attributeName, newValue);
            resolvedValue = newValue.resolve();
            restartRequired = applyUpdateToRuntime(context, operation, attributeName, resolvedValue, currentValue);
            if (restartRequired) {
                context.reloadRequired();
            }
        }

        if (context.completeStep() != OperationContext.ResultAction.KEEP && applyToRuntime) {
            ModelNode valueToRestore = currentValue.resolve();
            try {
                revertUpdateToRuntime(context, operation, attributeName, valueToRestore, resolvedValue);
            } catch (Exception e) {
                ServerLogger.ROOT_LOGGER.caughtExceptionRevertingOperation(e,
                        getClass().getSimpleName(),
                        operation.require(ModelDescriptionConstants.OP).asString(),
                        PathAddress.pathAddress(operation.require(ModelDescriptionConstants.OP_ADDR)));
            }
            if (restartRequired) {
                context.revertReloadRequired();
            }
        }
    }

    /**
     * If a resolved value validator was passed to the constructor, uses it to validate the value.
     * Subclasses can alter this behavior.
     */
    protected void validateResolvedValue(String name, ModelNode value) throws OperationFailedException {
        if (resolvedValueValidator != null) {
            resolvedValueValidator.validateParameter(name, value.resolve());
        }
    }


    /**
     * Hook to allow subclasses to make runtime changes to effect the attribute value change. Runtime changes
     * should be implemented by calling {@link org.jboss.as.controller.OperationContext#addStep(org.jboss.as.controller.OperationStepHandler, org.jboss.as.controller.OperationContext.Stage) adding a new step}
     * with {@link org.jboss.as.controller.OperationContext.Stage#RUNTIME}.
     * <p>
     * This default implementation simply returns {@code true}.
     * </p>
     *
     * @param context the context of the operation
     * @param operation the operation
     * @param attributeName the name of the attribute being modified
     * @param newValue the new value for the attribute
     * @param currentValue the existing value for the attribute
     *
     * @return {@code true} if the server requires restart to effect the attribute
     *         value change; {@code false} if not
     */
    protected boolean applyUpdateToRuntime(final OperationContext context, final ModelNode operation,
                                           final String attributeName, final ModelNode newValue, final ModelNode currentValue) throws OperationFailedException {
        return true;
    }

    /**
     * Hook to allow subclasses to revert runtime changes made in
     * {@link #applyUpdateToRuntime(org.jboss.as.controller.OperationContext, org.jboss.dmr.ModelNode, String, org.jboss.dmr.ModelNode, org.jboss.dmr.ModelNode)}.
     * <p>
     * This default implementation simply does nothing.
     * </p>
     *
     * @param context the context of the operation
     * @param operation the operation
     * @param attributeName the name of the attribute being modified
     * @param valueToRestore the previous value for the attribute, before this operation was executed
     * @param valueToRevert the new value for the attribute that should be reverted
     */
    protected void revertUpdateToRuntime(final OperationContext context, final ModelNode operation,
                                         final String attributeName, final ModelNode valueToRestore,
                                         final ModelNode valueToRevert) throws OperationFailedException {

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11085.java