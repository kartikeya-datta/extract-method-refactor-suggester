error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9396.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9396.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9396.java
text:
```scala
t@@his.brokerRelay.setTcpClient(this.tcpClient);

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
package org.springframework.messaging.simp.stomp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.StubMessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.tcp.ReconnectStrategy;
import org.springframework.messaging.tcp.TcpConnection;
import org.springframework.messaging.tcp.TcpConnectionHandler;
import org.springframework.messaging.tcp.TcpOperations;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;

import static org.junit.Assert.*;

/**
 * Unit tests for StompBrokerRelayMessageHandler.
 *
 * @author Rossen Stoyanchev
 */
public class StompBrokerRelayMessageHandlerTests {

	private StompBrokerRelayMessageHandler brokerRelay;

	private StubTcpOperations tcpClient;


	@Before
	public void setup() {

		this.tcpClient = new StubTcpOperations();

		this.brokerRelay = new StompBrokerRelayMessageHandler(new StubMessageChannel(),
				new StubMessageChannel(), new StubMessageChannel(), Arrays.asList("/topic"));
		this.brokerRelay.setTcpClient(tcpClient);
	}


	@Test
	public void testVirtualHostHeader() {

		String virtualHost = "ABC";
		String sessionId = "sess1";

		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.CONNECT);
		headers.setSessionId(sessionId);

		this.brokerRelay.setVirtualHost(virtualHost);
		this.brokerRelay.start();
		this.brokerRelay.handleMessage(MessageBuilder.withPayload(new byte[0]).setHeaders(headers).build());

		List<Message<byte[]>> sent = this.tcpClient.connection.messages;
		assertEquals(2, sent.size());

		StompHeaderAccessor headers1 = StompHeaderAccessor.wrap(sent.get(0));
		assertEquals(virtualHost, headers1.getHost());

		StompHeaderAccessor headers2 = StompHeaderAccessor.wrap(sent.get(1));
		assertEquals(sessionId, headers2.getSessionId());
		assertEquals(virtualHost, headers2.getHost());
	}

	@Test
	public void testDestinationExcluded() {

		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		headers.setSessionId("sess1");
		headers.setDestination("/user/daisy/foo");

		this.brokerRelay.start();
		this.brokerRelay.handleMessage(MessageBuilder.withPayload(new byte[0]).setHeaders(headers).build());

		List<Message<byte[]>> sent = this.tcpClient.connection.messages;
		assertEquals(1, sent.size());
		assertEquals(StompCommand.CONNECT, StompHeaderAccessor.wrap(sent.get(0)).getCommand());
	}


	private static ListenableFutureTask<Void> getFuture() {
		ListenableFutureTask<Void> futureTask = new ListenableFutureTask<>(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return null;
			}
		});
		futureTask.run();
		return futureTask;
	}


	private static class StubTcpOperations implements TcpOperations<byte[]> {

		private StubTcpConnection connection = new StubTcpConnection();


		@Override
		public ListenableFuture<Void> connect(TcpConnectionHandler<byte[]> connectionHandler) {
			connectionHandler.afterConnected(this.connection);
			return getFuture();
		}

		@Override
		public ListenableFuture<Void> connect(TcpConnectionHandler<byte[]> connectionHandler, ReconnectStrategy reconnectStrategy) {
			connectionHandler.afterConnected(this.connection);
			return getFuture();
		}

		@Override
		public ListenableFuture<Void> shutdown() {
			return getFuture();
		}
	}


	private static class StubTcpConnection implements TcpConnection<byte[]> {

		private final List<Message<byte[]>> messages = new ArrayList<>();


		@Override
		public ListenableFuture<Void> send(Message<byte[]> message) {
			this.messages.add(message);
			return getFuture();
		}

		@Override
		public void onReadInactivity(Runnable runnable, long duration) {
		}

		@Override
		public void onWriteInactivity(Runnable runnable, long duration) {
		}

		@Override
		public void close() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9396.java