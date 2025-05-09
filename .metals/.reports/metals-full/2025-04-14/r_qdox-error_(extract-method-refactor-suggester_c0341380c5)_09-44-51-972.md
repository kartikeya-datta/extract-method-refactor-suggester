error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7002.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7002.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7002.java
text:
```scala
r@@eturn create(ClientConfigurationImpl.create(protocol, hostName, port, handler, sslContext, connectionTimeout, saslOptions));

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

package org.jboss.as.controller.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.security.auth.callback.CallbackHandler;

import org.jboss.as.controller.client.impl.ClientConfigurationImpl;
import org.jboss.as.controller.client.impl.RemotingModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.jboss.threads.AsyncFuture;

/**
 * A client for an application server management model controller.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public interface ModelControllerClient extends Closeable {

    /**
     * Execute an operation synchronously.
     *
     * @param operation the operation to execute
     * @return the result of the operation
     * @throws IOException if an I/O error occurs while executing the operation
     */
    ModelNode execute(ModelNode operation) throws IOException;

    /**
     * Execute an operation synchronously.
     *
     * Note that associated input-streams have to be closed by the caller, after the
     * operation completed {@link OperationAttachments#isAutoCloseStreams()}.
     *
     * @param operation the operation to execute
     * @return the result of the operation
     * @throws IOException if an I/O error occurs while executing the operation
     */
    ModelNode execute(Operation operation) throws IOException;

    /**
     * Execute an operation synchronously, optionally receiving progress reports.
     *
     * @param operation the operation to execute
     * @param messageHandler the message handler to use for operation progress reporting, or {@code null} for none
     * @return the result of the operation
     * @throws IOException if an I/O error occurs while executing the operation
     */
    ModelNode execute(ModelNode operation, OperationMessageHandler messageHandler) throws IOException;

    /**
     * Execute an operation synchronously, optionally receiving progress reports.
     *
     * Note that associated input-streams have to be closed by the caller, after the
     * operation completed {@link OperationAttachments#isAutoCloseStreams()}.
     *
     * @param operation the operation to execute
     * @param messageHandler the message handler to use for operation progress reporting, or {@code null} for none
     * @return the result of the operation
     * @throws IOException if an I/O error occurs while executing the operation
     */
    ModelNode execute(Operation operation, OperationMessageHandler messageHandler) throws IOException;

    /**
     * Execute an operation.
     *
     * @param operation the operation to execute
     * @param messageHandler the message handler to use for operation progress reporting, or {@code null} for none
     * @return the future result of the operation
     */
    AsyncFuture<ModelNode> executeAsync(ModelNode operation, OperationMessageHandler messageHandler);

    /**
     * Execute an operation.
     *
     * Note that associated input-streams have to be closed by the caller, after the
     * operation completed {@link OperationAttachments#isAutoCloseStreams()}.
     *
     * @param operation the operation to execute
     * @param messageHandler the message handler to use for operation progress reporting, or {@code null} for none
     * @return the future result of the operation
     */
    AsyncFuture<ModelNode> executeAsync(Operation operation, OperationMessageHandler messageHandler);

    class Factory {

        /**
         * Create a client instance for a remote address and port.
         *
         * @param address the address of the remote host
         * @param port    the port
         * @return A model controller client
         */
        public static ModelControllerClient create(final InetAddress address, final int port) {
            return create(ClientConfigurationImpl.create(address, port));
        }

        /**
         * Create a client instance for a remote address and port.
         *
         * @param protocol The prototcol to use. If this is http-remoting or https-remoting http upgrade will be used rather than the native remote protocol
         * @param address  the address of the remote host
         * @param port     the port
         * @return A model controller client
         */
        public static ModelControllerClient create(final String protocol, final InetAddress address, final int port) {
            return create(ClientConfigurationImpl.create(protocol, address, port));
        }

        /**
         * Create a client instance for a remote address and port.
         *
         * @param address the address of the remote host
         * @param port    the port
         * @param handler CallbackHandler to obtain authentication information for the call.
         * @return A model controller client
         */
        public static ModelControllerClient create(final InetAddress address, final int port, final CallbackHandler handler) {
            return create(ClientConfigurationImpl.create(address, port, handler));
        }


        /**
         * Create a client instance for a remote address and port.
         *
         * @param protocol The prototcol to use. If this is http-remoting or https-remoting http upgrade will be used rather than the native remote protocol
         * @param address  the address of the remote host
         * @param port     the port
         * @param handler  CallbackHandler to obtain authentication information for the call.
         * @return A model controller client
         */
        public static ModelControllerClient create(final String protocol, final InetAddress address, final int port, final CallbackHandler handler) {
            return create(ClientConfigurationImpl.create(protocol, address, port, handler));
        }

        /**
         * Create a client instance for a remote address and port.
         *
         * @param address     the address of the remote host
         * @param port        the port
         * @param handler     CallbackHandler to obtain authentication information for the call.
         * @param saslOptions Additional options to be passed to the SASL mechanism.
         * @return A model controller client
         */
        public static ModelControllerClient create(final InetAddress address, final int port, final CallbackHandler handler, final Map<String, String> saslOptions) {
            return create(ClientConfigurationImpl.create(address, port, handler, saslOptions));
        }

        /**
         * Create a client instance for a remote address and port.
         *
         * @param protocol    The prototcol to use. If this is http-remoting or https-remoting http upgrade will be used
         * @param address     the address of the remote host
         * @param port        the port
         * @param handler     CallbackHandler to obtain authentication information for the call.
         * @param saslOptions Additional options to be passed to the SASL mechanism.
         * @return A model controller client
         */
        public static ModelControllerClient create(final String protocol, final InetAddress address, final int port, final CallbackHandler handler, final Map<String, String> saslOptions) {
            return create(ClientConfigurationImpl.create(protocol, address, port, handler, saslOptions));
        }

        /**
         * Create a client instance for a remote address and port.
         *
         * @param hostName the remote host
         * @param port     the port
         * @return A model controller client
         * @throws UnknownHostException if the host cannot be found
         */
        public static ModelControllerClient create(final String hostName, final int port) throws UnknownHostException {
            return create(ClientConfigurationImpl.create(hostName, port));
        }

        /**
         * Create a client instance for a remote address and port.
         *
         * @param protocol The prototcol to use. If this is http-remoting or https-remoting http upgrade will be used rather than the native remote protocol
         * @param hostName the remote host
         * @param port     the port
         * @return A model controller client
         * @throws UnknownHostException if the host cannot be found
         */
        public static ModelControllerClient create(final String protocol, final String hostName, final int port) throws UnknownHostException {
            return create(ClientConfigurationImpl.create(protocol, hostName, port));
        }

        /**
         * Create a client instance for a remote address and port and CallbackHandler.
         *
         * @param hostName the remote host
         * @param port     the port
         * @param handler  CallbackHandler to obtain authentication information for the call.
         * @return A model controller client
         * @throws UnknownHostException if the host cannot be found
         */
        public static ModelControllerClient create(final String hostName, final int port, final CallbackHandler handler) throws UnknownHostException {
            return create(ClientConfigurationImpl.create(hostName, port, handler));
        }

        /**
         * Create a client instance for a remote address and port and CallbackHandler.
         *
         * @param protocol The prototcol to use. If this is http-remoting or https-remoting http upgrade will be used rather than the native remote protocol
         * @param hostName the remote host
         * @param port     the port
         * @param handler  CallbackHandler to obtain authentication information for the call.
         * @return A model controller client
         * @throws UnknownHostException if the host cannot be found
         */
        public static ModelControllerClient create(final String protocol, final String hostName, final int port, final CallbackHandler handler) throws UnknownHostException {
            return create(ClientConfigurationImpl.create(protocol, hostName, port, handler));
        }

        /**
         * Create a client instance for a remote address and port and CallbackHandler.
         *
         * @param hostName   the remote host
         * @param port       the port
         * @param handler    CallbackHandler to obtain authentication information for the call.
         * @param sslContext a pre-initialised SSLContext
         * @return A model controller client
         * @throws UnknownHostException if the host cannot be found
         */
        public static ModelControllerClient create(final String hostName, final int port, final CallbackHandler handler, final SSLContext sslContext) throws UnknownHostException {
            return create(ClientConfigurationImpl.create(hostName, port, handler, sslContext));
        }

        /**
         * Create a client instance for a remote address and port and CallbackHandler.
         *
         * @param protocol    The prototcol to use. If this is http-remoting or https-remoting http upgrade will be used rather than the native remote protocol
         * @param hostName   the remote host
         * @param port       the port
         * @param handler    CallbackHandler to obtain authentication information for the call.
         * @param sslContext a pre-initialised SSLContext
         * @return A model controller client
         * @throws UnknownHostException if the host cannot be found
         */
        public static ModelControllerClient create(final String protocol, final String hostName, final int port, final CallbackHandler handler, final SSLContext sslContext) throws UnknownHostException {
            return create(ClientConfigurationImpl.create(protocol, hostName, port, handler, sslContext));
        }

        /**
         * Create a client instance for a remote address and port and CallbackHandler.
         *
         * @param hostName   the remote host
         * @param port       the port
         * @param handler    CallbackHandler to obtain authentication information for the call.
         * @param sslContext a pre-initialised SSLContext
         * @return A model controller client
         * @throws UnknownHostException if the host cannot be found
         */
        public static ModelControllerClient create(final String hostName, final int port, final CallbackHandler handler, final SSLContext sslContext, final int connectionTimeout) throws UnknownHostException {
            return create(ClientConfigurationImpl.create(hostName, port, handler, sslContext, connectionTimeout));
        }

        /**
         * Create a client instance for a remote address and port and CallbackHandler.
         *
         * @param protocol    The prototcol to use. If this is http-remoting or https-remoting http upgrade will be used rather than the native remote protocol
         * @param hostName   the remote host
         * @param port       the port
         * @param handler    CallbackHandler to obtain authentication information for the call.
         * @param sslContext a pre-initialised SSLContext
         * @return A model controller client
         * @throws UnknownHostException if the host cannot be found
         */
        public static ModelControllerClient create(final String protocol, final String hostName, final int port, final CallbackHandler handler, final SSLContext sslContext, final int connectionTimeout) throws UnknownHostException {
            return create(ClientConfigurationImpl.create(protocol, hostName, port, handler, sslContext, connectionTimeout));
        }

        /**
         * Create a client instance for a remote address and port and CallbackHandler.
         *
         * @param protocol    The prototcol to use. If this is http-remoting or https-remoting http upgrade will be used rather than the native remote protocol
         * @param hostName   the remote host
         * @param port       the port
         * @param handler    CallbackHandler to obtain authentication information for the call.
         * @param sslContext a pre-initialised SSLContext
         * @param saslOptions Additional options to be passed to the SASL mechanism.
         * @return A model controller client
         * @throws UnknownHostException if the host cannot be found
         */
        public static ModelControllerClient create(final String protocol, final String hostName, final int port, final CallbackHandler handler, final SSLContext sslContext, final int connectionTimeout, final Map<String, String> saslOptions) throws UnknownHostException {
            return create(ClientConfigurationImpl.create(protocol, hostName, port, handler, sslContext, connectionTimeout));
        }

        /**
         * Create a client instance for a remote address and port and CallbackHandler.
         *
         * @param hostName    the remote host
         * @param port        the port
         * @param handler     CallbackHandler to obtain authentication information for the call.
         * @param saslOptions Additional options to be passed to the SASL mechanism.
         * @return A model controller client
         * @throws UnknownHostException if the host cannot be found
         */
        public static ModelControllerClient create(final String hostName, final int port, final CallbackHandler handler, final Map<String, String> saslOptions) throws UnknownHostException {
            return create(ClientConfigurationImpl.create(hostName, port, handler, saslOptions));
        }

        /**
         * Create a client instance for a remote address and port and CallbackHandler.
         *
         * @param protocol    The prototcol to use. If this is http-remoting or https-remoting http upgrade will be used rather than the native remote protocol
         * @param hostName    the remote host
         * @param port        the port
         * @param handler     CallbackHandler to obtain authentication information for the call.
         * @param saslOptions Additional options to be passed to the SASL mechanism.
         * @return A model controller client
         * @throws UnknownHostException if the host cannot be found
         */
        public static ModelControllerClient create(final String protocol, final String hostName, final int port, final CallbackHandler handler, final Map<String, String> saslOptions) throws UnknownHostException {
            return create(ClientConfigurationImpl.create(protocol, hostName, port, handler, saslOptions));
        }

        /**
         * Create a client instance based on the client configuration.
         *
         * @param configuration the controller client configuration
         * @return the client
         */
        public static ModelControllerClient create(final ModelControllerClientConfiguration configuration) {
            return new RemotingModelControllerClient(configuration);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7002.java