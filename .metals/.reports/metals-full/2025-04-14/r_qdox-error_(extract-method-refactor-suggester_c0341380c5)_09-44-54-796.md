error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1989.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1989.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1989.java
text:
```scala
protected v@@oid handleChannelReset(Channel channel) {

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
package org.jboss.as.protocol.mgmt;

import java.io.Closeable;
import java.io.DataInput;
import java.io.IOException;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;

import org.jboss.as.protocol.ProtocolChannelSetup;
import org.jboss.as.protocol.StreamUtils;
import org.jboss.remoting3.Channel;
import org.jboss.remoting3.CloseHandler;
import org.jboss.remoting3.Connection;
import org.xnio.OptionMap;

/**
 * Strategy management clients can use for controlling the lifecycle of the channel.
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @author Emanuel Muckenhuber
 */
public abstract class ManagementClientChannelStrategy implements Closeable {

    /** The remoting channel service type. */
    private static final String DEFAULT_CHANNEL_SERVICE_TYPE = "management";

    /**
     * Get the channel.
     *
     * @return the channel
     * @throws IOException
     */
    public abstract Channel getChannel() throws IOException;

    /**
     * Create a new client channel strategy.
     *
     * @param channel the existing channel
     * @return the management client channel strategy
     */
    public static ManagementClientChannelStrategy create(final Channel channel) {
        return new Existing(channel);
    }

    /**
     * Create a new establishing management client channel-strategy
     *
     * @param setup the remoting setup
     * @param handler the {@code ManagementMessageHandler}
     * @param cbHandler a callback handler
     * @param saslOptions the sasl options
     * @return the management client channel strategy
     * @throws IOException
     */
    public static ManagementClientChannelStrategy create(final ProtocolChannelSetup setup,
                                                   final ManagementMessageHandler handler,
                                                   final CallbackHandler cbHandler,
                                                   final Map<String, String> saslOptions) throws IOException {
        return new Establishing(DEFAULT_CHANNEL_SERVICE_TYPE, setup, saslOptions, cbHandler, handler);
    }

    /**
     * The existing channel strategy.
     */
    private static class Existing extends ManagementClientChannelStrategy {
        // The underlying channel
        private final Channel channel;

        private Existing(final Channel channel) {
            this.channel = channel;
        }

        @Override
        public Channel getChannel() throws IOException {
            return channel;
        }

        @Override
        public void close() throws IOException {
            // closing is not our responsibility
        }
    }

    /**
     * When getting the underlying channel this strategy is trying to automatically (re-)connect
     * when either the connection or channel was closed.
     */
    private static class Establishing extends ManagementClientChannelStrategy {

        private final String channelName;
        private final Map<String,String> saslOptions;
        private final CallbackHandler callbackHandler;
        private final Channel.Receiver receiver;
        private final ProtocolChannelSetup setup;

        volatile Connection connection;
        volatile Channel channel;

        public Establishing(final String channelName, final ProtocolChannelSetup setup, final Map<String, String> saslOptions,
                            final CallbackHandler callbackHandler, final ManagementMessageHandler handler) {
            this.channelName = channelName;
            this.saslOptions = saslOptions;
            this.setup = setup;
            this.callbackHandler = callbackHandler;
            // Basic management channel receiver, which delegates messages to a {@code ManagementMessageHandler}
            // Additionally legacy bye-bye messages result in resetting the current channel
            this.receiver = new ManagementChannelReceiver() {

                @Override
                public void handleMessage(final Channel channel, final DataInput input, final ManagementProtocolHeader header) throws IOException {
                    handler.handleMessage(channel, input, header);
                }

                @Override
                protected void handleShutdownChannel(Channel channel) {
                    resetChannel(channel);
                }

            };
        }

        @Override
        public Channel getChannel() throws IOException {
            boolean ok = false;
            try {
                synchronized (this) {
                    if (connection == null) {
                        this.connection = setup.connect(callbackHandler, saslOptions).get();
                        this.connection.addCloseHandler(new CloseHandler<Connection>() {
                            @Override
                            public void handleClose(Connection closed, IOException exception) {
                                synchronized (Establishing.this) {
                                    if(connection == closed) {
                                        connection = null;
                                    }
                                }
                            }
                        });
                    }
                    if (channel == null) {
                        channel = connection.openChannel(channelName, OptionMap.EMPTY).get();
                        channel.addCloseHandler(new CloseHandler<Channel>() {
                            @Override
                            public void handleClose(Channel closed, IOException exception) {
                                synchronized (Establishing.this) {
                                    if(channel == closed) {
                                        channel = null;
                                    }
                                }
                            }
                        });
                        channel.receiveMessage(receiver);
                    }
                    ok = true;
                }
            } finally {
                if (! ok) {
                    StreamUtils.safeClose(connection);
                    StreamUtils.safeClose(channel);
                }
            }
            return channel;
        }

        private void resetChannel(Channel old) {
            synchronized (this) {
                if(channel == old) {
                    channel = null;
                }
            }
        }

        @Override
        public void close() throws IOException {
            StreamUtils.safeClose(channel);
            StreamUtils.safeClose(connection);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1989.java