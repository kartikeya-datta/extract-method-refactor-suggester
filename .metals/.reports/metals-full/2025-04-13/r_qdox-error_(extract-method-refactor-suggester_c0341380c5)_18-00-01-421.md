error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12966.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12966.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12966.java
text:
```scala
public W@@ebSocketSession doHandshake(WebSocketHandler webSocketHandler,

/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.websocket.client.endpoint;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ClientEndpointConfig.Configurator;
import javax.websocket.ContainerProvider;
import javax.websocket.Endpoint;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.websocket.WebSocketHandler;
import org.springframework.websocket.WebSocketSession;
import org.springframework.websocket.adapter.StandardEndpointAdapter;
import org.springframework.websocket.adapter.StandardWebSocketSessionAdapter;
import org.springframework.websocket.client.WebSocketClient;
import org.springframework.websocket.client.WebSocketConnectFailureException;

/**
 * A standard Java {@link WebSocketClient}.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class StandardWebSocketClient implements WebSocketClient {

	private static final Set<String> EXCLUDED_HEADERS = new HashSet<String>(
			Arrays.asList("Sec-WebSocket-Accept", "Sec-WebSocket-Extensions", "Sec-WebSocket-Key",
					"Sec-WebSocket-Protocol", "Sec-WebSocket-Version"));

	private WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();


	public void setWebSocketContainer(WebSocketContainer container) {
		this.webSocketContainer = container;
	}

	@Override
	public WebSocketSession doHandshake(WebSocketHandler webSocketHandler, String uriTemplate, Object... uriVariables)
			throws WebSocketConnectFailureException {

		URI uri = UriComponentsBuilder.fromUriString(uriTemplate).buildAndExpand(uriVariables).encode().toUri();
		return doHandshake(webSocketHandler, null, uri);
	}

	@Override
	public WebSocketSession doHandshake(WebSocketHandler<?> webSocketHandler,
			final HttpHeaders httpHeaders, URI uri) throws WebSocketConnectFailureException {

		Endpoint endpoint = new StandardEndpointAdapter(webSocketHandler);

		ClientEndpointConfig.Builder configBuidler = ClientEndpointConfig.Builder.create();
		if (httpHeaders != null) {
			List<String> protocols = httpHeaders.getSecWebSocketProtocol();
			if (!protocols.isEmpty()) {
				configBuidler.preferredSubprotocols(protocols);
			}
			configBuidler.configurator(new Configurator() {
				@Override
				public void beforeRequest(Map<String, List<String>> headers) {
					for (String headerName : httpHeaders.keySet()) {
						if (!EXCLUDED_HEADERS.contains(headerName)) {
							headers.put(headerName, httpHeaders.get(headerName));
						}
					}
				}
			});
		}

		try {
			Session session = this.webSocketContainer.connectToServer(endpoint, configBuidler.build(), uri);
			return new StandardWebSocketSessionAdapter(session);
		}
		catch (Exception e) {
			throw new WebSocketConnectFailureException("Failed to connect to " + uri, e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12966.java