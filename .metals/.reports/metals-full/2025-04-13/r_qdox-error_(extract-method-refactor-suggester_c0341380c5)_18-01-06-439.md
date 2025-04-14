error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/981.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/981.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/981.java
text:
```scala
r@@eturn super.getSockJsService();

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

package org.springframework.messaging.simp.config;

import java.util.Set;

import org.springframework.messaging.handler.websocket.SubProtocolWebSocketHandler;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.DefaultHandshakeHandler;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.config.SockJsServiceRegistration;
import org.springframework.web.socket.sockjs.SockJsService;
import org.springframework.web.socket.sockjs.transport.handler.WebSocketTransportHandler;
import org.springframework.web.socket.support.WebSocketHandlerDecorator;


/**
 * An abstract base class class for configuring STOMP over WebSocket/SockJS endpoints.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public abstract class AbstractStompEndpointRegistration<M> implements StompEndpointRegistration {

	private final String[] paths;

	private final WebSocketHandler wsHandler;

	private HandshakeHandler handshakeHandler;

	private StompSockJsServiceRegistration sockJsServiceRegistration;

	private final TaskScheduler sockJsTaskScheduler;


	public AbstractStompEndpointRegistration(String[] paths, WebSocketHandler webSocketHandler,
			TaskScheduler sockJsTaskScheduler) {

		Assert.notEmpty(paths, "No paths specified");
		this.paths = paths;
		this.wsHandler = webSocketHandler;
		this.sockJsTaskScheduler = sockJsTaskScheduler;
	}


	/**
	 * Provide a custom or pre-configured {@link HandshakeHandler}. This property is
	 * optional.
	 */
	@Override
	public StompEndpointRegistration setHandshakeHandler(HandshakeHandler handshakeHandler) {
		this.handshakeHandler = handshakeHandler;
		return this;
	}

	/**
	 * Enable SockJS fallback options.
	 */
	@Override
	public SockJsServiceRegistration withSockJS() {

		this.sockJsServiceRegistration = new StompSockJsServiceRegistration(this.sockJsTaskScheduler);

		if (this.handshakeHandler != null) {
			WebSocketTransportHandler transportHandler = new WebSocketTransportHandler(this.handshakeHandler);
			this.sockJsServiceRegistration.setTransportHandlerOverrides(transportHandler);
		}

		return this.sockJsServiceRegistration;
	}

	protected final M getMappings() {

		M mappings = createMappings();

		if (this.sockJsServiceRegistration != null) {
			SockJsService sockJsService = this.sockJsServiceRegistration.getSockJsService();
			for (String path : this.paths) {
				String pathPattern = path.endsWith("/") ? path + "**" : path + "/**";
				addSockJsServiceMapping(mappings, sockJsService, this.wsHandler, pathPattern);
			}
		}
		else {
			HandshakeHandler handshakeHandler = getOrCreateHandshakeHandler();
			for (String path : this.paths) {
				addWebSocketHandlerMapping(mappings, this.wsHandler, handshakeHandler, path);
			}
		}

		return mappings;
	}

	protected abstract M createMappings();

	private HandshakeHandler getOrCreateHandshakeHandler() {

		HandshakeHandler handler = (this.handshakeHandler != null)
				? this.handshakeHandler : new DefaultHandshakeHandler();

		if (handler instanceof DefaultHandshakeHandler) {
			DefaultHandshakeHandler defaultHandshakeHandler = (DefaultHandshakeHandler) handler;
			if (ObjectUtils.isEmpty(defaultHandshakeHandler.getSupportedProtocols())) {
				Set<String> protocols = findSubProtocolWebSocketHandler(this.wsHandler).getSupportedProtocols();
				defaultHandshakeHandler.setSupportedProtocols(protocols.toArray(new String[protocols.size()]));
			}
		}

		return handler;
	}

	private static SubProtocolWebSocketHandler findSubProtocolWebSocketHandler(WebSocketHandler webSocketHandler) {
		WebSocketHandler actual = (webSocketHandler instanceof WebSocketHandlerDecorator) ?
				((WebSocketHandlerDecorator) webSocketHandler).getLastHandler() : webSocketHandler;
		Assert.isInstanceOf(SubProtocolWebSocketHandler.class, actual,
						"No SubProtocolWebSocketHandler found: " + webSocketHandler);
		return (SubProtocolWebSocketHandler) actual;
	}

	protected abstract void addSockJsServiceMapping(M mappings, SockJsService sockJsService,
			WebSocketHandler wsHandler, String pathPattern);

	protected abstract void addWebSocketHandlerMapping(M mappings,
			WebSocketHandler wsHandler, HandshakeHandler handshakeHandler, String path);


	private class StompSockJsServiceRegistration extends SockJsServiceRegistration {

		public StompSockJsServiceRegistration(TaskScheduler defaultTaskScheduler) {
			super(defaultTaskScheduler);
		}

		protected SockJsService getSockJsService() {
			return super.getSockJsService(paths);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/981.java