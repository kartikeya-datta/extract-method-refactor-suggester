error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12245.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12245.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12245.java
text:
```scala
c@@ontext.stepCompleted();

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

import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceListener;

/**
 * Base class for operations that do nothing in {@link org.jboss.as.controller.OperationContext.Stage#MODEL} except
 * register a {@link org.jboss.as.controller.OperationContext.Stage#RUNTIME} step.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public abstract class AbstractRuntimeOnlyHandler implements OperationStepHandler {


    /**
     * Wait for the required service to start up and fail otherwise. This method is necessary when a runtime operation
     * uses a service that might have been created within a composite operation.
     *
     * This method will wait at most 100 millis.
     *
     * @param controller the service to wait for
     * @throws OperationFailedException if the service is not available, or the thread was interrupted.
     */
    public void waitFor(ServiceController<?> controller) throws OperationFailedException {
        ServiceWaitListener listener = null;
        int time = 100;
        while (time > 0) {
            if (controller.getState() == ServiceController.State.UP) {
                return;
            }

            if (listener == null) {
                listener = new ServiceWaitListener();
                controller.addListener(listener);
            }
            synchronized (listener) {
                try {
                    long start = System.currentTimeMillis();
                    listener.wait(time);
                    time -= System.currentTimeMillis() - start;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    controller.removeListener(listener);
                    throw new OperationFailedException((new ModelNode()).set("Interrupted waiting for service: " + controller.getName()));
                }
            }
        }

        controller.removeListener(listener);

        if (controller.getState() != ServiceController.State.UP) {
            throw new OperationFailedException(new ModelNode().set("Required service is not available: " + controller.getName()));
        }
    }

    /**
     * Simply adds a {@link org.jboss.as.controller.OperationContext.Stage#RUNTIME} step that calls
     * {@link #executeRuntimeStep(OperationContext, ModelNode)}.
     *
     * {@inheritDoc}
     */
    @Override
    public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
        context.addStep(new OperationStepHandler() {
            @Override
            public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
                executeRuntimeStep(context, operation);
            }
        }, OperationContext.Stage.RUNTIME);

        context.completeStep(OperationContext.RollbackHandler.NOOP_ROLLBACK_HANDLER);
    }

    /**
     * Execute this step in {@link org.jboss.as.controller.OperationContext.Stage#RUNTIME}.
     * If the operation fails, {@link OperationContext#getFailureDescription() context.getFailureDescroption()}
     * must be called, or {@link OperationFailedException} must be thrown, before calling {@link OperationContext#completeStep() context.completeStep()}.
     * If the operation succeeded, {@link OperationContext#getResult() context.getResult()} should
     * be called and the result populated with the outcome, after which {@link OperationContext#completeStep() context.completeStep()}
     * must be called.
     *
     * @param context the operation context
     * @param operation the operation being executed
     * @throws OperationFailedException if the operation failed <b>before</b> calling {@code context.completeStep()}
     */
    protected abstract void executeRuntimeStep(OperationContext context, ModelNode operation) throws OperationFailedException;

    private static class ServiceWaitListener<T> implements ServiceListener<T> {
        @Override
        public void listenerAdded(ServiceController<? extends T> serviceController) {
        }

        @Override
        public void transition(ServiceController<? extends T> serviceController, ServiceController.Transition transition) {
         if (transition.getAfter() == ServiceController.Substate.UP) {
                synchronized (this) {
                    notify();
                }
            }
        }

        @Override
        public void serviceRemoveRequested(ServiceController<? extends T> serviceController) {
        }

        @Override
        public void serviceRemoveRequestCleared(ServiceController<? extends T> serviceController) {
        }

        @Override
        public void dependencyFailed(ServiceController<? extends T> serviceController) {
        }

        @Override
        public void dependencyFailureCleared(ServiceController<? extends T> serviceController) {
        }

        @Override
        public void immediateDependencyUnavailable(ServiceController<? extends T> serviceController) {
        }

        @Override
        public void immediateDependencyAvailable(ServiceController<? extends T> serviceController) {
        }

        @Override
        public void transitiveDependencyUnavailable(ServiceController<? extends T> serviceController) {
        }

        @Override
        public void transitiveDependencyAvailable(ServiceController<? extends T> serviceController) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12245.java