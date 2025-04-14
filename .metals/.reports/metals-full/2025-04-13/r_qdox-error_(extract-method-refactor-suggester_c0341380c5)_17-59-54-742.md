error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9840.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9840.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9840.java
text:
```scala
t@@his(new ArrayBlockingQueue<TransactionalProtocolClient.PreparedOperation<T>>(capacity));

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.as.controller.remote;

import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.dmr.ModelNode;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Basic operation listener backed by a blocking queue. If the limit of the queue is reached prepared operations
 * are going to be rolled back automatically.
 *
 * @param <T> the operation type
 */
public class BlockingQueueOperationListener<T extends TransactionalProtocolClient.Operation> implements TransactionalProtocolClient.TransactionalOperationListener<T> {

    private final BlockingQueue<TransactionalProtocolClient.PreparedOperation<T>> queue;

    public BlockingQueueOperationListener() {
        this(new LinkedBlockingQueue<TransactionalProtocolClient.PreparedOperation<T>>());
    }

    public BlockingQueueOperationListener(final int capacity) {
        this.queue = new ArrayBlockingQueue<TransactionalProtocolClient.PreparedOperation<T>>(capacity);
    }

    public BlockingQueueOperationListener(final BlockingQueue<TransactionalProtocolClient.PreparedOperation<T>> queue) {
        this.queue = queue;
    }

    @Override
    public void operationPrepared(final TransactionalProtocolClient.PreparedOperation<T> prepared) {
        if(! queue.offer(prepared)) {
            prepared.rollback();
        }
    }

    @Override
    public void operationFailed(T operation, ModelNode result) {
        queue.offer(new FailedOperation<T>(operation, result));
    }

    @Override
    public void operationComplete(T operation, ModelNode result) {
        //
    }

    /**
     * Retrieves and removes the head of the underlying queue, waiting if necessary until an element becomes available.
     *
     * @return the prepared operation
     * @throws InterruptedException
     */
    public TransactionalProtocolClient.PreparedOperation<T> retrievePreparedOperation() throws InterruptedException {
        return queue.take();
    }

    /**
     * Retrieves and removes the head of this queue, waiting up to the specified wait time if necessary for an element to become available.
     *
     * @param timeout the timeout
     * @param timeUnit the time unit
     * @return the prepared operation
     * @throws InterruptedException
     */
    public TransactionalProtocolClient.PreparedOperation<T> retrievePreparedOperation(final long timeout, final TimeUnit timeUnit) throws InterruptedException {
        return queue.poll(timeout, timeUnit);
    }

    public static class FailedOperation<T extends TransactionalProtocolClient.Operation> implements TransactionalProtocolClient.PreparedOperation<T> {

        private final T operation;
        private final ModelNode finalResult;

        /**
         * Create a failed operation.
         *
         * @param operation the operation
         * @param t the throwable
         * @param <T> the operation type
         * @return the failed operation
         */
        public static <T extends TransactionalProtocolClient.Operation> TransactionalProtocolClient.PreparedOperation<T> create(final T operation, final Throwable t) {
            final String failureDescription = t.getLocalizedMessage() == null ? t.getClass().getName() : t.getLocalizedMessage();
            return create(operation, failureDescription);
        }

        /**
         * Create a failed operation.
         *
         * @param operation the operation
         * @param failureDescription the failure description
         * @param <T> the operation type
         * @return the failed operation
         */
        public static <T extends TransactionalProtocolClient.Operation> TransactionalProtocolClient.PreparedOperation<T> create(final T operation, final String failureDescription) {
            final ModelNode failedResult = new ModelNode();
            failedResult.get(ModelDescriptionConstants.OUTCOME).set(ModelDescriptionConstants.FAILED);
            failedResult.get(ModelDescriptionConstants.FAILURE_DESCRIPTION).set(failureDescription);
            return new FailedOperation<T>(operation, failedResult);
        }

        public FailedOperation(final T operation, final ModelNode finalResult) {
            this.operation = operation;
            this.finalResult = finalResult;
        }

        @Override
        public T getOperation() {
            return operation;
        }

        @Override
        public ModelNode getPreparedResult() {
            return finalResult;
        }

        @Override
        public boolean isDone() {
            return true;
        }

        @Override
        public boolean isFailed() {
            return true;
        }

        @Override
        public Future<ModelNode> getFinalResult() {
            return new Future<ModelNode>() {
                @Override
                public boolean cancel(boolean mayInterruptIfRunning) {
                    return false;
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }

                @Override
                public boolean isDone() {
                    return true;
                }

                @Override
                public ModelNode get() {
                    return finalResult;
                }

                @Override
                public ModelNode get(long timeout, TimeUnit unit) {
                    return finalResult;
                }
            };
        }

        @Override
        public void commit() {
            throw new IllegalStateException();
        }

        @Override
        public void rollback() {
            throw new IllegalStateException();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9840.java