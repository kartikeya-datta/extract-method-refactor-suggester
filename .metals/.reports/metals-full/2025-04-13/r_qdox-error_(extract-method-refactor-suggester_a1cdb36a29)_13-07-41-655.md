error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1290.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1290.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1290.java
text:
```scala
M@@odelNode getUncommittedResult() throws InterruptedException {

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

package org.jboss.as.domain.controller.operations.coordination;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import org.jboss.as.controller.NewModelController;
import org.jboss.as.controller.NewOperationContext;
import org.jboss.as.controller.NewProxyController;
import org.jboss.as.controller.client.MessageSeverity;
import org.jboss.as.controller.client.OperationAttachments;
import org.jboss.as.controller.client.OperationMessageHandler;
import org.jboss.dmr.ModelNode;

/**
* Callable that invokes an operation on a proxy.
*
* @author Brian Stansberry (c) 2011 Red Hat Inc.
*/
class ProxyTask implements Callable<ModelNode> {

    private final NewProxyController proxyController;
    private final String host;
    private final ModelNode operation;
    private final NewOperationContext context;

    private final AtomicReference<Boolean> transactionAction = new AtomicReference<Boolean>();
    private final AtomicReference<ModelNode> proxyResultRef = new AtomicReference<ModelNode>();
    private boolean cancelRemoteTransaction;

    public ProxyTask(String host, ModelNode operation, NewOperationContext context, NewProxyController proxyController) {
        this.host = host;
        this.operation = operation;
        this.context = context;
        this.proxyController = proxyController;
    }

    @Override
    public ModelNode call() throws Exception {

        System.out.println("Sending " + operation + " to " + host);
        OperationMessageHandler messageHandler = new DelegatingMessageHandler(context);

        final AtomicReference<NewModelController.OperationTransaction> txRef = new AtomicReference<NewModelController.OperationTransaction>();
        final AtomicReference<ModelNode> preparedResultRef = new AtomicReference<ModelNode>();
        final AtomicReference<ModelNode> finalResultRef = new AtomicReference<ModelNode>();
        final NewProxyController.ProxyOperationControl proxyControl = new NewProxyController.ProxyOperationControl() {

            @Override
            public void operationPrepared(NewModelController.OperationTransaction transaction, ModelNode result) {
                txRef.set(transaction);
                preparedResultRef.set(result);
            }

            @Override
            public void operationFailed(ModelNode response) {
                finalResultRef.set(response);
            }

            @Override
            public void operationCompleted(ModelNode response) {
                finalResultRef.set(response);
            }
        };

        proxyController.execute(operation, messageHandler, proxyControl, new DelegatingOperationAttachments(context));

        NewModelController.OperationTransaction remoteTransaction = null;
        ModelNode result = finalResultRef.get();
        if (result != null) {
            // operation failed before it could commit
            System.out.println("Received final result " + result + " from " + host);
        } else {
            result = preparedResultRef.get();
            System.out.println("Received prepared result " + result + " from " + host);
            remoteTransaction = txRef.get();
        }

        synchronized (proxyResultRef) {
            proxyResultRef.set(result);
            proxyResultRef.notifyAll();
        }

        if (remoteTransaction != null) {
            if (cancelRemoteTransaction) {
                // Controlling thread was cancelled
                remoteTransaction.rollback();
            } else {
                synchronized (transactionAction) {
                    while (transactionAction.get() == null) {
                        try {
                            transactionAction.wait();
                        }
                        catch (InterruptedException ie) {
                            // Treat as cancellation
                            transactionAction.set(Boolean.FALSE);
                        }
                    }
                    if (transactionAction.get().booleanValue()) {
                        remoteTransaction.commit();
                    } else {
                        remoteTransaction.rollback();
                    }
                }
            }
        }

        return finalResultRef.get();
    }

    ModelNode getResult() throws InterruptedException {
        synchronized (proxyResultRef) {

            while (proxyResultRef.get() == null) {
                try {
                    proxyResultRef.wait();
                }
                catch (InterruptedException ie) {
                    cancelRemoteTransaction = true;
                    throw ie;
                }
            }
            return proxyResultRef.get();
        }
    }

    void finalizeTransaction(boolean commit) {
        synchronized (transactionAction) {
            transactionAction.set(Boolean.valueOf(commit));
            transactionAction.notifyAll();
        }
    }

    void cancel() {
        synchronized (proxyResultRef) {
            cancelRemoteTransaction = true;
        }
    }

    private static class DelegatingMessageHandler implements OperationMessageHandler {

        private final NewOperationContext context;

        DelegatingMessageHandler(final NewOperationContext context) {
            this.context = context;
        }

        @Override
        public void handleReport(MessageSeverity severity, String message) {
            context.report(severity, message);
        }
    }

    private static class DelegatingOperationAttachments implements OperationAttachments {

        private final NewOperationContext context;
        private DelegatingOperationAttachments(final NewOperationContext context) {
            this.context = context;
        }

        @Override
        public List<InputStream> getInputStreams() {
            int count = context.getAttachmentStreamCount();
            List<InputStream> result = new ArrayList<InputStream>(count);
            for (int i = 0; i < count; i++) {
                result.add(context.getAttachmentStream(count));
            }
            return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1290.java