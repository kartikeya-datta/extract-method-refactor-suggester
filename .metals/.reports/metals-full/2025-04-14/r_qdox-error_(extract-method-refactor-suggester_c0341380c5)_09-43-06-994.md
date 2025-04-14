error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7684.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7684.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7684.java
text:
```scala
final E@@xistingChannelModelControllerClient client = new ExistingChannelModelControllerClient(clientChannel, channels.getExecutorService());

/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
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

import static junit.framework.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.Assert;

import org.jboss.as.controller.client.MessageSeverity;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationAttachments;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.OperationMessageHandler;
import org.jboss.as.controller.remote.ExistingChannelModelControllerClient;
import org.jboss.as.controller.remote.ModelControllerClientOperationHandler;
import org.jboss.as.controller.support.RemoteChannelPairSetup;
import org.jboss.as.protocol.mgmt.ManagementChannelReceiver;
import org.jboss.as.protocol.mgmt.ManagementMessageHandler;
import org.jboss.as.protocol.mgmt.ManagementProtocolHeader;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.remoting3.Channel;
import org.jboss.threads.AsyncFuture;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xnio.IoUtils;
/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ModelControllerClientTestCase {

    Logger log = Logger.getLogger(ModelControllerClientTestCase.class);

    private final DelegatingChannelHandler handler = new DelegatingChannelHandler();
    private RemoteChannelPairSetup channels;
    @Before
    public void start() throws Exception {
        channels = new RemoteChannelPairSetup();
        channels.setupRemoting(handler);
        channels.startClientConnetion();
    }

    @After
    public void stop() throws Exception {
        channels.stopChannels();
        channels.shutdownRemoting();
        handler.setDelegate(null);
    }

    private ModelControllerClient createTestClient() {
        final Channel clientChannel = channels.getClientChannel();
        final ExistingChannelModelControllerClient client = new ExistingChannelModelControllerClient(clientChannel);
        clientChannel.receiveMessage(ManagementChannelReceiver.createDelegating(client));
        return client;
    }

    @Test @Ignore("OperationMessageHandlerProxy turned off temporarily")
    public void testSynchronousOperationMessageHandler() throws Exception {

        final CountDownLatch executeLatch = new CountDownLatch(1);
        MockModelController controller = new MockModelController() {
            @Override
            public ModelNode execute(ModelNode operation, OperationMessageHandler handler, OperationTransactionControl control, OperationAttachments attachments) {
                this.operation = operation;
                handler.handleReport(MessageSeverity.INFO, "Test1");
                handler.handleReport(MessageSeverity.INFO, "Test2");
                executeLatch.countDown();
                ModelNode result = new ModelNode();
                result.get("testing").set(operation.get("test"));
                return result;
            }
        };

        // Set the handler
        handler.setDelegate(new ModelControllerClientOperationHandler(controller, channels.getExecutorService()));

        final ModelControllerClient client = createTestClient();
        try {
            ModelNode operation = new ModelNode();
            operation.get("test").set("123");

            final BlockingQueue<String> messages = new LinkedBlockingQueue<String>();

            ModelNode result = client.execute(operation,
                    new OperationMessageHandler() {

                        @Override
                        public void handleReport(MessageSeverity severity, String message) {
                            if (severity == MessageSeverity.INFO && message.startsWith("Test")) {
                                messages.add(message);
                            }
                        }
                    });
            assertEquals("123", controller.getOperation().get("test").asString());
            assertEquals("123", result.get("testing").asString());
            assertEquals("Test1", messages.take());
            assertEquals("Test2", messages.take());
        } finally {
            IoUtils.safeClose(client);
        }
    }

    @Test
    public void testSynchronousAttachmentInputStreams() throws Exception {

        final byte[] firstBytes = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        final byte[] secondBytes = new byte[] {10, 9, 8 , 7 , 6, 5, 4, 3, 2, 1};
        final byte[] thirdBytes = new byte[] {1};

        final CountDownLatch executeLatch = new CountDownLatch(1);
        final AtomicInteger size = new AtomicInteger();
        final AtomicReference<byte[]> firstResult = new AtomicReference<byte[]>();
        final AtomicReference<byte[]> secondResult = new AtomicReference<byte[]>();
        final AtomicReference<byte[]> thirdResult = new AtomicReference<byte[]>();
        MockModelController controller = new MockModelController() {
            @Override
            public ModelNode execute(ModelNode operation, OperationMessageHandler handler, OperationTransactionControl control, OperationAttachments attachments) {
                int streamIndex = 0;
                for (InputStream in : attachments.getInputStreams()) {
                    try {
                        ArrayList<Integer> readBytes = new ArrayList<Integer>();
                        int b = in.read();
                        while (b != -1) {
                            readBytes.add(b);
                            b = in.read();
                        }

                        byte[] bytes = new byte[readBytes.size()];
                        for (int i = 0 ; i < bytes.length ; i++) {
                            bytes[i] = (byte)readBytes.get(i).intValue();
                        }

                        if (streamIndex == 0) {
                            firstResult.set(bytes);
                        } else if (streamIndex == 1) {
                            secondResult.set(bytes);
                        } else if (streamIndex == 2) {
                            thirdResult.set(bytes);
                        }
                        streamIndex++;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                size.set(streamIndex);
                executeLatch.countDown();
                return new ModelNode();
            }
        };
        // Set the handler
        handler.setDelegate(new ModelControllerClientOperationHandler(controller, channels.getExecutorService()));

        final ModelControllerClient client = createTestClient();
        try {
            ModelNode operation = new ModelNode();
            operation.get("test").set("123");

            ModelNode op = new ModelNode();
            op.get("name").set(123);
            OperationBuilder builder = new OperationBuilder(op);
            builder.addInputStream(new ByteArrayInputStream(firstBytes));
            builder.addInputStream(new ByteArrayInputStream(secondBytes));
            builder.addInputStream(new ByteArrayInputStream(thirdBytes));
            client.execute(builder.build());
            executeLatch.await();
            assertEquals(3, size.get());
            assertArrays(firstBytes, firstResult.get());
            assertArrays(secondBytes, secondResult.get());
            assertArrays(new byte[] { 1 }, thirdResult.get());
        } finally {
            IoUtils.safeClose(client);
        }
    }

    @Test @Ignore("OperationMessageHandlerProxy turned off temporarily")
    public void testAsynchronousOperationWithMessageHandler() throws Exception {
        final CountDownLatch executeLatch = new CountDownLatch(1);
        MockModelController controller = new MockModelController() {
            @Override
            public ModelNode execute(ModelNode operation, OperationMessageHandler handler, OperationTransactionControl control, OperationAttachments attachments) {
                this.operation = operation;
                handler.handleReport(MessageSeverity.INFO, "Test1");
                handler.handleReport(MessageSeverity.INFO, "Test2");
                executeLatch.countDown();
                ModelNode result = new ModelNode();
                result.get("testing").set(operation.get("test"));
                return result;
            }
        };

        // Set the handler
        handler.setDelegate(new ModelControllerClientOperationHandler(controller, channels.getExecutorService()));

        final ModelControllerClient client = createTestClient();
        try {

            ModelNode operation = new ModelNode();
            operation.get("test").set("123");

            final BlockingQueue<String> messages = new LinkedBlockingQueue<String>();

            AsyncFuture<ModelNode> resultFuture = client.executeAsync(operation,
                    new OperationMessageHandler() {

                        @Override
                        public void handleReport(MessageSeverity severity, String message) {
                            if (severity == MessageSeverity.INFO && message.startsWith("Test")) {
                                messages.add(message);
                            }
                        }
                    });
            ModelNode result = resultFuture.get();
            assertEquals("123", controller.getOperation().get("test").asString());
            assertEquals("123", result.get("testing").asString());
            assertEquals("Test1", messages.take());
            assertEquals("Test2", messages.take());
        } finally {
            IoUtils.safeClose(client);
        }
    }

    @Test
    public void testCancelAsynchronousOperation() throws Exception {
        final CountDownLatch executeLatch = new CountDownLatch(1);
        final CountDownLatch interrupted = new CountDownLatch(1);
        MockModelController controller = new MockModelController() {
            @Override
            public ModelNode execute(ModelNode operation, OperationMessageHandler handler, OperationTransactionControl control, OperationAttachments attachments) {
                this.operation = operation;
                executeLatch.countDown();

                try {
                    log.debug("Waiting for interrupt");
                    //Wait for this operation to be cancelled
                    Thread.sleep(10000000);
                    ModelNode result = new ModelNode();
                    result.get("testing").set(operation.get("test"));
                    return result;
                } catch (InterruptedException e) {
                    interrupted.countDown();
                    throw new RuntimeException(e);
                }
            }
        };

        // Set the handler
        handler.setDelegate(new ModelControllerClientOperationHandler(controller, channels.getExecutorService()));

        final ModelControllerClient client = createTestClient();
        try {
            ModelNode operation = new ModelNode();
            operation.get("test").set("123");

            final BlockingQueue<String> messages = new LinkedBlockingQueue<String>();

            AsyncFuture<ModelNode> resultFuture = client.executeAsync(operation,
                    new OperationMessageHandler() {

                        @Override
                        public void handleReport(MessageSeverity severity, String message) {
                            if (severity == MessageSeverity.INFO && message.startsWith("Test")) {
                                messages.add(message);
                            }
                        }
                    });
            executeLatch.await();
            resultFuture.cancel(false);
            interrupted.await();
        } finally {
            IoUtils.safeClose(client);
        }

    }

    private void assertArrays(byte[] expected, byte[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0 ; i < expected.length ; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    private static abstract class MockModelController implements ModelController {
        protected volatile ModelNode operation;

        ModelNode getOperation() {
            return operation;
        }

        @Override
        public ModelControllerClient createClient(Executor executor) {
            return null;
        }

    }

    static class DelegatingChannelHandler implements ManagementMessageHandler {

        private ManagementMessageHandler delegate;

        @Override
        public synchronized void handleMessage(Channel channel, DataInput input, ManagementProtocolHeader header) throws IOException {
            if(delegate == null) {
                throw new IllegalStateException();
            }
            delegate.handleMessage(channel, input, header);
        }

        public synchronized void setDelegate(ManagementMessageHandler delegate) {
            this.delegate = delegate;
        }

        @Override
        public void shutdown() {
            if(delegate != null) {
                delegate.shutdown();
            }
        }

        @Override
        public boolean awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException {
            if(delegate != null) {
                return delegate.awaitCompletion(timeout, unit);
            }
            return true;
        }

        @Override
        public void shutdownNow() {
            if(delegate != null) {
                delegate.shutdownNow();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7684.java