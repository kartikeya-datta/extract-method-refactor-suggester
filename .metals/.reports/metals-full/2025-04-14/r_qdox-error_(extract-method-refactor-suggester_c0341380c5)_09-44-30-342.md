error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1342.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1342.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1342.java
text:
```scala
l@@og.tracef("Scheduling request %s with future %s - %d (%d)", this, future, getBatchId(), getCurrentRequestId());

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

package org.jboss.as.protocol.mgmt;

import java.io.DataInput;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.as.protocol.ProtocolChannel;
import org.jboss.logging.Logger;
import org.jboss.remoting3.Channel;
import org.jboss.remoting3.CloseHandler;
import org.jboss.threads.AsyncFuture;
import org.jboss.threads.AsyncFutureTask;

/**
 * Base management request used for remote requests.  Provides the basic mechanism for connecting to a remote host controller
 * for performing a task.  It will manage connecting and retrieving the correct response.
 *
 *
 * @author John Bailey
 * @author Kabir Khan
 */
public abstract class ManagementRequest<T> extends ManagementResponseHandler<T> {

    private final Logger log = Logger.getLogger("org.jboss.as.protocol");

    private static final AtomicInteger requestId = new AtomicInteger();
    private final int currentRequestId = requestId.incrementAndGet();
    private final ManagementFuture<T> future = new ManagementFuture<T>();
    private final int batchId;

    /**
     * Create a new ManagementRequest that is not part of an 'execution', i.e. this is a standalone request.
     */
    protected ManagementRequest() {
        this(0);
    }

    /**
     * Create a new ManagementRequest that is part of a batch
     *
     *  @param executionId the id of the 'execution' this request is a part of
     */
    protected ManagementRequest(int batchId) {
        this.batchId = batchId;
    }

    /**
     * Get the id of the protocol request. The {@link ManagementOperationHandler} will use this to
     * determine the {@link ManagementRequestHandler} to use.
     *
     * @return the request code
     */
    protected abstract byte getRequestCode();

    protected int getCurrentRequestId() {
        return currentRequestId;
    }

    protected int getBatchId() {
        return batchId;
    }

    /**
     * Execute the request by connecting and then delegating to the implementation's execute
     * and return a future used to get the response when complete.
     *
     * @param executor The executor to use to handle the request and response
     * @param channel The channel strategy
     * @return A future to retrieve the result when the request is complete
     */
    public AsyncFuture<T> execute(final ExecutorService executor, final ManagementClientChannelStrategy channelStrategy) {
        log.tracef("Scheduling request %s with future %s", this, this);
        executor.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    final ManagementChannel channel = channelStrategy.getChannel();
                    log.tracef("Got channel %s from request %s", channel, ManagementRequest.this);

                    //Ends up in writeRequest(ProtocolChannel, FlushableDataOutput)
                    channel.executeRequest(ManagementRequest.this, new DelegatingResponseHandler(channelStrategy));
                } catch (Exception e) {
                    log.tracef(e, "Could not get channel for request %s, failing %s", ManagementRequest.this, future);
                    future.failed(e);
                }
            }
        });

        return future;
    }

    void writeRequest(ProtocolChannel channel, FlushableDataOutput output) throws IOException {
        //Body
        writeRequest(ManagementProtocol.VERSION, output);
        output.writeByte(ManagementProtocol.REQUEST_END);
    }

    /**
     * Execute the request and wait for the result.
     *
     * @param executor The executor to use to handle the request and response
     * @param channelStrategy The channel strategy
     * @return The result
     * @throws IOException If any problems occur
     */
    public T executeForResult(final ExecutorService executor, final ManagementClientChannelStrategy channelStrategy) throws Exception {
        return execute(executor, channelStrategy).get();
    }

    /**
     * Override to send extra parameters to the server {@link ManagementRequestHandler}. This default
     * implementation does not send any extra data
     *
     * @param protocolVersion the protocol version
     * @param output the data output to write the data to
     */
    protected void writeRequest(final int protocolVersion, final FlushableDataOutput output) throws IOException {
    }

    /**
     * Override to register a close handler for the channel that will be removed once the request is done
     *
     * @return the close handler
     */
    protected CloseHandler<Channel> getRequestCloseHandler() {
        return null;
    }

    private final class DelegatingResponseHandler extends ManagementResponseHandler<T>{
        private final ManagementClientChannelStrategy clientChannelStrategy;

        public DelegatingResponseHandler(ManagementClientChannelStrategy clientChannelStrategy) {
            this.clientChannelStrategy = clientChannelStrategy;
        }

        @Override
        protected T readResponse(DataInput input) {
            final String error = getResponseContext().getResponse().getError();
            if (error != null) {
                future.failed(new IOException("A problem happened executing on the server: " + error));
                return null;
            }

            T result = null;
            try {
                ManagementRequest.this.setResponseContext(getResponseContext());
                result = ManagementRequest.this.readResponse(input);
                future.done(result);
                return result;
            } catch (Exception e) {
                future.failed(e);
            } finally {
                clientChannelStrategy.requestDone();
            }
            return result;
        }
    }

    protected void setError(Exception e) {
        future.failed(e);
    }

    static class ManagementFuture<T> extends AsyncFutureTask<T>{
        protected ManagementFuture() {
            super(null);
        }

        void done(T result) {
            super.setResult(result);
        }

        void failed(Exception ex) {
            super.setFailed(ex);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1342.java