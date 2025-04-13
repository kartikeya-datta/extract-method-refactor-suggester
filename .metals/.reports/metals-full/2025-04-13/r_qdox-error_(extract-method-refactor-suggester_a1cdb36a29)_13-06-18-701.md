error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1459.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1459.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1459.java
text:
```scala
A@@ssert.notNull(reconnectStrategy, "ReconnectStrategy must not be null");

/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.messaging.support.tcp;

import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;

import reactor.core.Environment;
import reactor.core.composable.Composable;
import reactor.core.composable.Deferred;
import reactor.core.composable.Promise;
import reactor.core.composable.Stream;
import reactor.core.composable.spec.Promises;
import reactor.function.Consumer;
import reactor.function.support.SingleUseConsumer;
import reactor.io.Buffer;
import reactor.tcp.Reconnect;
import reactor.tcp.TcpClient;
import reactor.tcp.TcpConnection;
import reactor.tcp.encoding.Codec;
import reactor.tcp.netty.NettyTcpClient;
import reactor.tcp.spec.TcpClientSpec;
import reactor.tuple.Tuple;
import reactor.tuple.Tuple2;

/**
 * A Reactor/Netty implementation of {@link TcpOperations}.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class ReactorNettyTcpClient<P> implements TcpOperations<P> {

	private final static Log logger = LogFactory.getLog(ReactorNettyTcpClient.class);

	private Environment environment;

	private TcpClient<Message<P>, Message<P>> tcpClient;


	public ReactorNettyTcpClient(String host, int port, Codec<Buffer, Message<P>, Message<P>> codec) {
		this.environment = new Environment();
		this.tcpClient = new TcpClientSpec<Message<P>, Message<P>>(NettyTcpClient.class)
				.env(this.environment)
				.codec(codec)
				.connect(host, port)
				.get();
	}


	@Override
	public ListenableFuture<Void> connect(TcpConnectionHandler<P> connectionHandler) {

		Promise<TcpConnection<Message<P>, Message<P>>> promise = this.tcpClient.open();
		composeConnectionHandling(promise, connectionHandler);

		return new AbstractPromiseToListenableFutureAdapter<TcpConnection<Message<P>, Message<P>>, Void>(promise) {
			@Override
			protected Void adapt(TcpConnection<Message<P>, Message<P>> result) {
				return null;
			}
		};
	}

	@Override
	public ListenableFuture<Void> connect(final TcpConnectionHandler<P> connectionHandler,
			final ReconnectStrategy reconnectStrategy) {

		Assert.notNull(reconnectStrategy, "'reconnectStrategy' is required");

		Stream<TcpConnection<Message<P>, Message<P>>> stream =
				this.tcpClient.open(new Reconnect() {
					@Override
					public Tuple2<InetSocketAddress, Long> reconnect(InetSocketAddress address, int attempt) {
						return Tuple.of(address, reconnectStrategy.getTimeToNextAttempt(attempt));
					}
				});
		composeConnectionHandling(stream, connectionHandler);

		return new PassThroughPromiseToListenableFutureAdapter<Void>(toPromise(stream));
	}

	private void composeConnectionHandling(Composable<TcpConnection<Message<P>, Message<P>>> composable,
			final TcpConnectionHandler<P> connectionHandler) {

		composable.when(Throwable.class, new Consumer<Throwable>() {
			@Override
			public void accept(Throwable ex) {
				connectionHandler.afterConnectFailure(ex);
			}
		});

		composable.consume(new Consumer<TcpConnection<Message<P>, Message<P>>>() {
			@Override
			public void accept(TcpConnection<Message<P>, Message<P>> connection) {
				connection.on().close(new Runnable() {
					@Override
					public void run() {
						connectionHandler.afterConnectionClosed();
					}
				});
				connection.in().consume(new Consumer<Message<P>>() {
					@Override
					public void accept(Message<P> message) {
						connectionHandler.handleMessage(message);
					}
				});
				connection.when(Throwable.class, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable t) {
					 	logger.error("Exception on connection " + connectionHandler, t);
					}
				});
				connectionHandler.afterConnected(new ReactorTcpConnection<P>(connection));
			}
		});
	}

	private Promise<Void> toPromise(Stream<TcpConnection<Message<P>, Message<P>>> stream) {

		final Deferred<Void,Promise<Void>> deferred = Promises.<Void>defer().get();

		stream.consume(SingleUseConsumer.once(new Consumer<TcpConnection<Message<P>, Message<P>>>() {
			@Override
			public void accept(TcpConnection<Message<P>, Message<P>> conn) {
				deferred.accept((Void) null);
			}
		}));

		stream.when(Throwable.class, SingleUseConsumer.once(new Consumer<Throwable>() {
			@Override
			public void accept(Throwable throwable) {
				deferred.accept(throwable);
			}
		}));

		return deferred.compose();
	}

	@Override
	public ListenableFuture<Void> shutdown() {
		try {
			Promise<Void> promise = this.tcpClient.close();
			return new AbstractPromiseToListenableFutureAdapter<Void, Void>(promise) {
				@Override
				protected Void adapt(Void result) {
					return result;
				}
			};
		}
		finally {
			this.environment.shutdown();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1459.java