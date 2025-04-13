error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12473.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12473.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12473.java
text:
```scala
h@@andlers.registerOperationHandler("add-async-handler", NewAsyncHandlerAdd.INSTANCE, NewAsyncHandlerAdd.INSTANCE, false);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.logging;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;

import org.jboss.as.controller.Cancellable;
import org.jboss.as.controller.ModelAddOperationHandler;
import org.jboss.as.controller.NewExtension;
import org.jboss.as.controller.NewExtensionContext;
import org.jboss.as.controller.NewOperationContext;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ResultHandler;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.registry.ModelNodeRegistration;
import org.jboss.dmr.ModelNode;

/**
 * @author Emanuel Muckenhuber
 */
public class NewLoggingExtension implements NewExtension {

    public static final String SUBSYSTEM_NAME = "logging";
    private static final PathElement loggersPath = PathElement.pathElement(CommonAttributes.LOGGER);
    private static final PathElement handlersPath = PathElement.pathElement(CommonAttributes.HANDLER);

    /** {@inheritDoc} */
    public void initialize(NewExtensionContext context) {
        final SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME);
        final ModelNodeRegistration registration = subsystem.registerSubsystemModel(NewLoggingSubsystemProviders.SUBSYSTEM);
        registration.registerOperationHandler(ADD, ADD_INSTANCE, NewLoggingSubsystemProviders.SUBSYSTEM_ADD, false);
        registration.registerOperationHandler("set-root-logger", NewRootLoggerAdd.INSTANCE, NewLoggingSubsystemProviders.SET_ROOT_LOGGER, false);
        registration.registerOperationHandler("remove-root-logger", NewRootLoggerRemove.INSTANCE, NewLoggingSubsystemProviders.SET_ROOT_LOGGER, false);
        // loggers
        final ModelNodeRegistration loggers = registration.registerSubModel(loggersPath, NewLoggingSubsystemProviders.LOGGER);
        loggers.registerOperationHandler(ADD, NewLoggerAdd.INSTANCE, NewLoggingSubsystemProviders.LOGGER_ADD, false);
        loggers.registerOperationHandler(REMOVE, NewLoggerRemove.INSTANCE, NewLoggingSubsystemProviders.LOGGER_REMOVE, false);
        // handlers
        final ModelNodeRegistration handlers = registration.registerSubModel(handlersPath, NewLoggingSubsystemProviders.HANDLERS);
        handlers.registerOperationHandler(ADD, NewLoggerHandlerAdd.INSTANCE, NewLoggingSubsystemProviders.HANDLER_ADD, false);
        handlers.registerOperationHandler(REMOVE, NewLoggerHandlerRemove.INSTANCE, NewLoggingSubsystemProviders.HANDLER_REMOVE, false);
        handlers.registerOperationHandler("add-async-handler", NewAsyncHandlerAdd.INSTANCE, NewLoggingSubsystemProviders.ASYNC_HANDLER_ADD, false);
        handlers.registerOperationHandler("add-console-handler", NewConsoleHandlerAdd.INSTANCE, NewLoggingSubsystemProviders.CONSOLE_HANDLER_ADD, false);
        handlers.registerOperationHandler("add-file-handler", NewFileHandlerAdd.INSTANCE, NewLoggingSubsystemProviders.FILE_HANDLER_ADD, false);
        handlers.registerOperationHandler("add-periodic-handler", NewPeriodicFileHandlerAdd.INSTANCE, NewLoggingSubsystemProviders.PERIODIC_HANDLER_ADD, false);
        handlers.registerOperationHandler("add-size-periodic-handler", NewSizePeriodicFileHandlerAdd.INSTANCE, NewLoggingSubsystemProviders.SIZE_PERIODIC_HANDLER_ADD, false);
    }

    /** {@inheritDoc} */
    public void initializeParsers(ExtensionParsingContext context) {
        context.setSubsystemXmlMapping(Namespace.CURRENT.getUriString(), NewLoggingSubsystemParser.getInstance(), NewLoggingSubsystemParser.getInstance());
    }

    private static final NewLoggingSubsystemAdd ADD_INSTANCE = new NewLoggingSubsystemAdd();

    static class NewLoggingSubsystemAdd implements ModelAddOperationHandler {

        /** {@inheritDoc} */
        public Cancellable execute(final NewOperationContext context, final ModelNode operation, final ResultHandler resultHandler) {

            final ModelNode compensatingOperation = new ModelNode();
            compensatingOperation.get(OP).set(REMOVE);
            compensatingOperation.get(OP_ADDR).set(operation.get(OP_ADDR));

            final ModelNode subModel = context.getSubModel();
            subModel.get(CommonAttributes.LOGGER).setEmptyObject();
            subModel.get(CommonAttributes.HANDLER).setEmptyObject();

            resultHandler.handleResultComplete(compensatingOperation);

            return Cancellable.NULL;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12473.java