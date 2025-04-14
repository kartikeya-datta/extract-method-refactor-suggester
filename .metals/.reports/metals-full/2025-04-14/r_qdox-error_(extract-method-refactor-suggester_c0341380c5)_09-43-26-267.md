error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11976.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11976.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11976.java
text:
```scala
l@@ogger.debug("Starting WebSocket session url=" + url);

/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.web.socket.sockjs.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SettableListenableFuture;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A SockJS {@link Transport} that uses a
 * {@link org.springframework.web.socket.client.WebSocketClient WebSocketClient}.
 *
 * @author Rossen Stoyanchev
 * @since 4.1
 */
public class WebSocketTransport implements Transport {

	private static Log logger = LogFactory.getLog(WebSocketTransport.class);

	private final WebSocketClient webSocketClient;


	public WebSocketTransport(WebSocketClient webSocketClient) {
		Assert.notNull(webSocketClient, "'webSocketClient' is required");
		this.webSocketClient = webSocketClient;
	}


	/**
	 * Return the configured {@code WebSocketClient}.
	 */
	public WebSocketClient getWebSocketClient() {
		return this.webSocketClient;
	}

	@Override
	public ListenableFuture<WebSocketSession> connect(TransportRequest request, WebSocketHandler handler) {
		final SettableListenableFuture<WebSocketSession> future = new SettableListenableFuture<WebSocketSession>();
		WebSocketClientSockJsSession session = new WebSocketClientSockJsSession(request, handler, future);
		handler = new ClientSockJsWebSocketHandler(session);
		request.addTimeoutTask(session.getTimeoutTask());

		URI url = request.getTransportUrl();
		WebSocketHttpHeaders headers = new WebSocketHttpHeaders(request.getHandshakeHeaders());
		if (logger.isDebugEnabled()) {
			logger.debug("Opening WebSocket connection, url=" + url);
		}
		this.webSocketClient.doHandshake(handler, headers, url).addCallback(
				new ListenableFutureCallback<WebSocketSession>() {
					@Override
					public void onSuccess(WebSocketSession webSocketSession) {
						// WebSocket session ready, SockJS Session not yet
					}
					@Override
					public void onFailure(Throwable t) {
						future.setException(t);
					}
				});
		return future;
	}

	@Override
	public String toString() {
		return "WebSocketTransport[client=" + this.webSocketClient + "]";
	}


	private static class ClientSockJsWebSocketHandler extends TextWebSocketHandler {

		private final WebSocketClientSockJsSession sockJsSession;

		private final AtomicInteger connectCount = new AtomicInteger(0);


		private ClientSockJsWebSocketHandler(WebSocketClientSockJsSession session) {
			Assert.notNull(session);
			this.sockJsSession = session;
		}

		@Override
		public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
			Assert.isTrue(this.connectCount.compareAndSet(0, 1));
			this.sockJsSession.initializeDelegateSession(webSocketSession);
		}

		@Override
		public void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) throws Exception {
			this.sockJsSession.handleFrame(message.getPayload());
		}

		@Override
		public void handleTransportError(WebSocketSession webSocketSession, Throwable ex) throws Exception {
			this.sockJsSession.handleTransportError(ex);
		}

		@Override
		public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus status) throws Exception {
			this.sockJsSession.afterTransportClosed(status);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11976.java