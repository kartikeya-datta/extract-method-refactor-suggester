error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9986.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9986.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9986.java
text:
```scala
P@@rotocolMessages.MESSAGES.nullVar("connectTask");

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

package org.jboss.as.protocol;

import org.jboss.remoting3.CloseHandler;
import org.jboss.remoting3.Connection;

import java.io.IOException;

/**
 * A basic connection manager, notifying clients when the connection is closed or shutdown. The
 * {@code ProtocolConnectionManager.ConnectTask} can be used to implement different (re-)connection strategies.
 *
 * @author Emanuel Muckenhuber
 */
public class ProtocolConnectionManager {

    private ConnectTask connectTask;
    private volatile boolean shutdown;
    private volatile Connection connection;

    protected ProtocolConnectionManager(final ConnectTask initial) {
        if(initial == null) {
            throw ProtocolMessages.MESSAGES.nullVar("connectTask");
        }
        this.connectTask = initial;
    }

    /**
     * Check if connected.
     *
     * @return {@code true} if the connection is open, {@code false} otherwise
     */
    public boolean isConnected() {
        return connection != null && !shutdown;
    }

    /**
     * Get the connection. If not connected, the {@code ConnectTask} will be used to establish a connection.
     *
     * @return the connection
     * @throws IOException
     */
    public Connection connect() throws IOException {
        Connection connection;
        synchronized (this) {
            if(shutdown) throw ProtocolMessages.MESSAGES.channelClosed();
            connection = this.connection;
            if(connection == null) {
                connection = connectTask.connect();
                if(connection == null) {
                    throw ProtocolMessages.MESSAGES.channelClosed();
                }
                boolean ok = false;
                try {
                    // Connection opened notification
                    final ConnectionOpenHandler openHandler = connectTask.getConnectionOpenedHandler();
                    openHandler.connectionOpened(connection);
                    ok = true;
                    this.connection = connection;
                    connection.addCloseHandler(new CloseHandler<Connection>() {
                        @Override
                        public void handleClose(Connection closed, IOException exception) {
                            onConnectionClose(closed);
                        }
                    });
                } finally {
                    if(!ok) {
                        StreamUtils.safeClose(connection);
                    }
                }
            }
        }
        return connection;
    }

    /**
     * Get the connection.
     *
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Shutdown the connection manager.
     */
    public void shutdown() {
        final Connection connection;
        synchronized (this) {
            if(shutdown) return;
            shutdown = true;
            connection = this.connection;
            if(connectTask != null) {
                connectTask.shutdown();
            }
        }
        StreamUtils.safeClose(connection);
    }

    /**
     * Notification that a connection was closed.
     *
     * @param closed the closed connection
     */
    protected void onConnectionClose(final Connection closed) {
        synchronized (this) {
            if(connection == closed) {
                connection = null;
                if(shutdown) {
                    connectTask = DISCONNECTED;
                    return;
                }
                final ConnectTask previous = connectTask;
                connectTask = previous.connectionClosed();
            }
        }
    }

    public interface ConnectionOpenHandler {

        /**
         * Connection opened notification
         *
         * @param connection the connection
         * @throws IOException
         */
        void connectionOpened(final Connection connection) throws IOException;

    }

    /**
     * Task used to establish the connection.
     */
    public interface ConnectTask {

        /**
         * Get the connection opened handler.
         *
         * @return the connection opened handler
         */
        ConnectionOpenHandler getConnectionOpenedHandler();

        /**
         * Create a new connection
         *
         * @return the connection
         * @throws IOException
         */
        Connection connect() throws IOException;

        /**
         * Notification when the channel is closed, but the manager not shutdown.
         *
         * @return the next connect connectTask
         */
        ConnectTask connectionClosed();

        /**
         * Notification when the connection manager gets shutdown.
         */
        void shutdown();

    }

    /**
     * Create a new connection manager, based on an existing connection.
     *
     * @param connection the existing connection
     * @param openHandler a connection open handler
     * @return the connected manager
     */
    public static ProtocolConnectionManager create(final Connection connection, final ConnectionOpenHandler openHandler) {
        return create(new EstablishedConnection(connection, openHandler));
    }

    /**
     * Create a new connection manager, which will try to connect using the protocol connection configuration.
     *
     * @param configuration the connection configuration
     * @param openHandler the connection open handler
     * @return the connection manager
     */
    public static ProtocolConnectionManager create(final ProtocolConnectionConfiguration configuration, final ConnectionOpenHandler openHandler) {
        return create(new EstablishingConnection(configuration, openHandler));
    }

    /**
     * Create a new connection manager, which will try to connect using the protocol connection configuration.
     *
     * @param configuration the connection configuration
     * @param openHandler the connection open handler
     * @param next the next connect connectTask used once disconnected
     * @return the connection manager
     */
    public static ProtocolConnectionManager create(final ProtocolConnectionConfiguration configuration, final ConnectionOpenHandler openHandler, final ConnectTask next) {
        return create(new EstablishingConnection(configuration, openHandler, next));
    }

    /**
     * Create a new connection manager.
     *
     * @param connectTask the connect connectTask
     * @return the connection manager
     */
    public static ProtocolConnectionManager create(final ConnectTask connectTask) {
        return new ProtocolConnectionManager(connectTask);
    }

    static class EstablishingConnection implements ConnectTask {

        private final ConnectTask next;
        private final ConnectionOpenHandler openHandler;
        private final ProtocolConnectionConfiguration configuration;

        protected EstablishingConnection(final ProtocolConnectionConfiguration configuration, final ConnectionOpenHandler openHandler) {
            this.configuration = configuration;
            this.openHandler = openHandler;
            this.next = this;
        }

        protected EstablishingConnection(final ProtocolConnectionConfiguration configuration, final ConnectionOpenHandler openHandler, final ConnectTask next) {
            this.configuration = configuration;
            this.openHandler = openHandler;
            this.next = next;
        }

        @Override
        public ConnectionOpenHandler getConnectionOpenedHandler() {
            return openHandler;
        }

        @Override
        public Connection connect() throws IOException {
            return ProtocolConnectionUtils.connectSync(configuration);
        }

        @Override
        public ConnectTask connectionClosed() {
            return next;
        }

        @Override
        public void shutdown() {
            //
        }
    }

    static class EstablishedConnection implements ConnectTask {

        private final Connection connection;
        private final ConnectionOpenHandler openHandler;
        private EstablishedConnection(final Connection connection, final ConnectionOpenHandler openHandler) {
            this.connection = connection;
            this.openHandler = openHandler;
        }

        @Override
        public ConnectionOpenHandler getConnectionOpenedHandler() {
            return openHandler;
        }

        @Override
        public Connection connect() throws IOException {
            return connection;
        }

        @Override
        public ConnectTask connectionClosed() {
            return DISCONNECTED;
        }

        @Override
        public void shutdown() {
            //
        }
    }

    public static final ConnectTask DISCONNECTED = new ConnectTask() {

        @Override
        public ConnectionOpenHandler getConnectionOpenedHandler() {
            return new ConnectionOpenHandler() {
                @Override
                public void connectionOpened(final Connection connection) throws IOException {
                    //
                }
            };
        }

        @Override
        public Connection connect() throws IOException {
            throw ProtocolMessages.MESSAGES.channelClosed();
        }

        @Override
        public ConnectTask connectionClosed() {
            return this;
        }

        @Override
        public void shutdown() {
            //
        }
    };

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9986.java