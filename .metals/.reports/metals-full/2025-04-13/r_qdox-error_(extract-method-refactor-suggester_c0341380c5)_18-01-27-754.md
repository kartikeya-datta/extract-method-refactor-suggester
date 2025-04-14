error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7504.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7504.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7504.java
text:
```scala
i@@f (operation.get(FILE).hasDefined(CommonAttributes.RELATIVE_TO)) {

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

import org.jboss.as.controller.BasicOperationResult;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationResult;
import org.jboss.as.controller.RuntimeTask;
import org.jboss.as.controller.RuntimeTaskContext;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.logging.CommonAttributes.AUTOFLUSH;
import static org.jboss.as.logging.CommonAttributes.ENCODING;
import static org.jboss.as.logging.CommonAttributes.FILE;
import static org.jboss.as.logging.CommonAttributes.FORMATTER;
import static org.jboss.as.logging.CommonAttributes.LEVEL;
import static org.jboss.as.logging.CommonAttributes.MAX_BACKUP_INDEX;
import static org.jboss.as.logging.CommonAttributes.PATH;
import static org.jboss.as.logging.CommonAttributes.RELATIVE_TO;
import static org.jboss.as.logging.CommonAttributes.ROTATE_SIZE;

import java.util.logging.Handler;
import java.util.logging.Level;

import org.jboss.as.controller.ModelAddOperationHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ResultHandler;
import org.jboss.as.server.services.path.AbstractPathService;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceTarget;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author Emanuel Muckenhuber
 */
class SizeRotatingFileHandlerAdd implements ModelAddOperationHandler {

    static final SizeRotatingFileHandlerAdd INSTANCE = new SizeRotatingFileHandlerAdd();

    static long DEFAULT_ROTATE_SIZE = 2L * 1024L * 1024L;

    /** {@inheritDoc} */
    @Override
    public OperationResult execute(final OperationContext context, final ModelNode operation, final ResultHandler resultHandler) throws OperationFailedException {

        final PathAddress address = PathAddress.pathAddress(operation.require(OP_ADDR));
        final String name = address.getLastElement().getValue();

        final ModelNode compensatingOperation = new ModelNode();
        compensatingOperation.get(OP_ADDR).set(operation.require(OP_ADDR));
        compensatingOperation.get(OP).set(REMOVE);

        final ModelNode subModel = context.getSubModel();
        subModel.get(AUTOFLUSH).set(operation.get(AUTOFLUSH));
        subModel.get(ENCODING).set(operation.get(ENCODING));
        subModel.get(FORMATTER).set(operation.get(FORMATTER));
        subModel.get(LEVEL).set(operation.get(LEVEL));
        subModel.get(FILE).set(operation.get(FILE));
        subModel.get(MAX_BACKUP_INDEX).set(operation.get(MAX_BACKUP_INDEX));
        subModel.get(ROTATE_SIZE).set(operation.get(ROTATE_SIZE));

        if (context.getRuntimeContext() != null) {
            context.getRuntimeContext().setRuntimeTask(new RuntimeTask() {
                public void execute(RuntimeTaskContext context) throws OperationFailedException {
                    final ServiceTarget serviceTarget = context.getServiceTarget();
                    try {
                        final SizeRotatingFileHandlerService service = new SizeRotatingFileHandlerService();
                        final ServiceBuilder<Handler> serviceBuilder = serviceTarget.addService(LogServices.handlerName(name), service);
                        if (operation.hasDefined(FILE)) {
                            final HandlerFileService fileService = new HandlerFileService(operation.get(FILE, PATH).asString());
                            final ServiceBuilder<?> fileBuilder = serviceTarget.addService(LogServices.handlerFileName(name), fileService);
                            if (operation.hasDefined(CommonAttributes.RELATIVE_TO)) {
                                fileBuilder.addDependency(AbstractPathService.pathNameOf(operation.get(FILE, RELATIVE_TO).asString()), String.class, fileService.getRelativeToInjector());
                            }
                            fileBuilder.setInitialMode(ServiceController.Mode.ACTIVE).install();
                            serviceBuilder.addDependency(LogServices.handlerFileName(name), String.class, service.getFileNameInjector());
                        }
                        service.setLevel(Level.parse(operation.get(LEVEL).asString()));
                        final Boolean autoFlush = operation.get(AUTOFLUSH).asBoolean();
                        if (autoFlush != null) service.setAutoflush(autoFlush.booleanValue());
                        if (operation.has(ENCODING)) service.setEncoding(operation.get(ENCODING).asString());
                        if (operation.has(FORMATTER)) service.setFormatterSpec(createFormatterSpec(operation));
                        if (operation.has(MAX_BACKUP_INDEX))
                            service.setMaxBackupIndex(operation.get(MAX_BACKUP_INDEX).asInt());
                        if (operation.has(ROTATE_SIZE))
                            service.setRotateSize(operation.get(ROTATE_SIZE).asLong(DEFAULT_ROTATE_SIZE));
                        serviceBuilder.setInitialMode(ServiceController.Mode.ACTIVE);
                        serviceBuilder.install();
                        resultHandler.handleResultComplete();
                    } catch (Throwable t) {
                        throw new OperationFailedException(new ModelNode().set(t.getLocalizedMessage()));
                    }
                }
            });
        } else {
            resultHandler.handleResultComplete();
        }
        return new BasicOperationResult(compensatingOperation);
    }

    static AbstractFormatterSpec createFormatterSpec(final ModelNode operation) {
        return new PatternFormatterSpec(operation.get(FORMATTER).asString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7504.java