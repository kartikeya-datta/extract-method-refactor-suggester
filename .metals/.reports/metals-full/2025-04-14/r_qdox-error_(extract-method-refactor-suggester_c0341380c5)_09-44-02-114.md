error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1463.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1463.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1463.java
text:
```scala
A@@ssert.notNull(taskExecutor, "TaskExecutor must not be null");

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

package org.springframework.web.socket.client.jetty;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.JettyWebSocketHandlerAdapter;
import org.springframework.web.socket.adapter.JettyWebSocketSession;
import org.springframework.web.socket.client.AbstractWebSocketClient;
import org.springframework.web.socket.support.WebSocketExtension;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Initiates WebSocket requests to a WebSocket server programatically through the Jetty
 * WebSocket API.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class JettyWebSocketClient extends AbstractWebSocketClient implements SmartLifecycle {

	private final org.eclipse.jetty.websocket.client.WebSocketClient client;

	private boolean autoStartup = true;

	private int phase = Integer.MAX_VALUE;

	private final Object lifecycleMonitor = new Object();

	private AsyncListenableTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("WebSocketClient-");


	/**
	 * Default constructor that creates an instance of
	 * {@link org.eclipse.jetty.websocket.client.WebSocketClient} with default settings.
	 */
	public JettyWebSocketClient() {
		this.client = new org.eclipse.jetty.websocket.client.WebSocketClient();
	}

	/**
	 * Constructor that accepts a pre-configured {@link WebSocketClient}.
	 */
	public JettyWebSocketClient(WebSocketClient client) {
		super();
		this.client = client;
	}


	/**
	 * Set a {@link TaskExecutor} to use to open the connection.
	 * By default {@link SimpleAsyncTaskExecutor} is used.
	 */
	public void setTaskExecutor(AsyncListenableTaskExecutor taskExecutor) {
		Assert.notNull(taskExecutor, "taskExecutor is required");
		this.taskExecutor = taskExecutor;
	}

	/**
	 * Return the configured {@link TaskExecutor}.
	 */
	public AsyncListenableTaskExecutor getTaskExecutor() {
		return this.taskExecutor;
	}

	public void setAutoStartup(boolean autoStartup) {
		this.autoStartup = autoStartup;
	}

	@Override
	public boolean isAutoStartup() {
		return this.autoStartup;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

	@Override
	public int getPhase() {
		return this.phase;
	}

	@Override
	public boolean isRunning() {
		synchronized (this.lifecycleMonitor) {
			return this.client.isStarted();
		}
	}

	@Override
	public void start() {
		synchronized (this.lifecycleMonitor) {
			if (!isRunning()) {
				try {
					if (logger.isDebugEnabled()) {
						logger.debug("Starting Jetty WebSocketClient");
					}
					this.client.start();
				}
				catch (Exception e) {
					throw new IllegalStateException("Failed to start Jetty client", e);
				}
			}
		}
	}

	@Override
	public void stop() {
		synchronized (this.lifecycleMonitor) {
			if (isRunning()) {
				try {
					if (logger.isDebugEnabled()) {
						logger.debug("Stopping Jetty WebSocketClient");
					}
					this.client.stop();
				}
				catch (Exception e) {
					logger.error("Error stopping Jetty WebSocketClient", e);
				}
			}
		}
	}

	@Override
	public void stop(Runnable callback) {
		this.stop();
		callback.run();
	}

	@Override
	public ListenableFuture<WebSocketSession> doHandshake(WebSocketHandler webSocketHandler,
			String uriTemplate, Object... uriVars) {

		UriComponents uriComponents = UriComponentsBuilder.fromUriString(uriTemplate).buildAndExpand(uriVars).encode();
		return doHandshake(webSocketHandler, null, uriComponents.toUri());
	}

	@Override
	public ListenableFuture<WebSocketSession> doHandshakeInternal(WebSocketHandler wsHandler,
			HttpHeaders headers, final URI uri, List<String> protocols,
			List<WebSocketExtension> extensions,  Map<String, Object> handshakeAttributes) {

		final ClientUpgradeRequest request = new ClientUpgradeRequest();
		request.setSubProtocols(protocols);

		for (WebSocketExtension e : extensions) {
			request.addExtensions(new WebSocketExtension.WebSocketToJettyExtensionConfigAdapter(e));
		}

		for (String header : headers.keySet()) {
			request.setHeader(header, headers.get(header));
		}

		Principal user = getUser();
		final JettyWebSocketSession wsSession = new JettyWebSocketSession(user, handshakeAttributes);
		final JettyWebSocketHandlerAdapter listener = new JettyWebSocketHandlerAdapter(wsHandler, wsSession);

		return this.taskExecutor.submitListenable(new Callable<WebSocketSession>() {
			@Override
			public WebSocketSession call() throws Exception {
				Future<Session> future = client.connect(listener, uri, request);
				future.get();
				return wsSession;
			}
		});
	}

	/**
	 * @return the user to make available through {@link WebSocketSession#getPrincipal()};
	 *         by default this method returns {@code null}
	 */
	protected Principal getUser() {
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1463.java