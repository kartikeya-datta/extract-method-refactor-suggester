error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14582.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14582.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 706
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14582.java
text:
```scala
public class GlassFishRequestUpgradeStrategy extends AbstractStandardUpgradeStrategy {

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

p@@ackage org.springframework.web.socket.server.support;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.Arrays;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;

import org.glassfish.tyrus.core.ComponentProviderService;
import org.glassfish.tyrus.core.EndpointWrapper;
import org.glassfish.tyrus.core.ErrorCollector;
import org.glassfish.tyrus.core.RequestContext;
import org.glassfish.tyrus.server.TyrusEndpoint;
import org.glassfish.tyrus.servlet.TyrusHttpUpgradeHandler;
import org.glassfish.tyrus.websockets.Connection;
import org.glassfish.tyrus.websockets.Version;
import org.glassfish.tyrus.websockets.WebSocketEngine;
import org.glassfish.tyrus.websockets.WebSocketEngine.WebSocketHolderListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.endpoint.ServerEndpointRegistration;

/**
 * GlassFish support for upgrading an {@link HttpServletRequest} during a WebSocket
 * handshake.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class GlassFishRequestUpgradeStrategy extends AbstractEndpointUpgradeStrategy {

	private final static Random random = new Random();


	@Override
	public String[] getSupportedVersions() {
		return StringUtils.commaDelimitedListToStringArray(Version.getSupportedWireProtocolVersions());
	}

	@Override
	public void upgradeInternal(ServerHttpRequest request, ServerHttpResponse response,
			String selectedProtocol, Endpoint endpoint) throws IOException, HandshakeFailureException {

		Assert.isTrue(request instanceof ServletServerHttpRequest);
		HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

		Assert.isTrue(response instanceof ServletServerHttpResponse);
		HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
		servletResponse = new AlreadyUpgradedResponseWrapper(servletResponse);

		TyrusEndpoint tyrusEndpoint = createTyrusEndpoint(servletRequest, endpoint, selectedProtocol);
		WebSocketEngine engine = WebSocketEngine.getEngine();

		try {
			engine.register(tyrusEndpoint);
		}
		catch (DeploymentException ex) {
			throw new HandshakeFailureException("Failed to deploy endpoint in GlassFish", ex);
		}

		try {
			if (!performUpgrade(servletRequest, servletResponse, request.getHeaders(), tyrusEndpoint)) {
				throw new HandshakeFailureException("Failed to upgrade HttpServletRequest");
			}
		}
		finally {
			engine.unregister(tyrusEndpoint);
		}
	}

	private boolean performUpgrade(HttpServletRequest request, HttpServletResponse response,
			HttpHeaders headers, TyrusEndpoint tyrusEndpoint) throws IOException {

		final TyrusHttpUpgradeHandler upgradeHandler;
		try {
			upgradeHandler = request.upgrade(TyrusHttpUpgradeHandler.class);
		}
		catch (ServletException e) {
			throw new HandshakeFailureException("Unable to create UpgardeHandler", e);
		}

		Connection connection = createConnection(upgradeHandler, response);

		RequestContext wsRequest = RequestContext.Builder.create()
				.requestURI(URI.create(tyrusEndpoint.getPath())).requestPath(tyrusEndpoint.getPath())
				.connection(connection).secure(request.isSecure()).build();

		for (String header : headers.keySet()) {
			wsRequest.getHeaders().put(header, headers.get(header));
		}

		return WebSocketEngine.getEngine().upgrade(connection, wsRequest, new WebSocketHolderListener() {
			@Override
			public void onWebSocketHolder(WebSocketEngine.WebSocketHolder webSocketHolder) {
				upgradeHandler.setWebSocketHolder(webSocketHolder);
			}
		});
	}

	private TyrusEndpoint createTyrusEndpoint(HttpServletRequest request, Endpoint endpoint, String selectedProtocol) {

		// Use randomized path
		String requestUri = request.getRequestURI();
		String randomValue = String.valueOf(random.nextLong());
		String endpointPath = requestUri.endsWith("/") ? requestUri + randomValue : requestUri + "/" + randomValue;

		ServerEndpointRegistration endpointConfig = new ServerEndpointRegistration(endpointPath, endpoint);
		endpointConfig.setSubprotocols(Arrays.asList(selectedProtocol));

		return new TyrusEndpoint(new EndpointWrapper(endpoint, endpointConfig,
				ComponentProviderService.create(), null, "/", new ErrorCollector(),
				endpointConfig.getConfigurator()));
	}

	private Connection createConnection(TyrusHttpUpgradeHandler handler, HttpServletResponse response) {
		try {
			String name = "org.glassfish.tyrus.servlet.ConnectionImpl";
			Class<?> clazz = ClassUtils.forName(name, GlassFishRequestUpgradeStrategy.class.getClassLoader());
			Constructor<?> constructor = clazz.getDeclaredConstructor(TyrusHttpUpgradeHandler.class, HttpServletResponse.class);
			ReflectionUtils.makeAccessible(constructor);
			return (Connection) constructor.newInstance(handler, response);
		}
		catch (Exception ex) {
			throw new IllegalStateException("Failed to instantiate GlassFish connection", ex);
		}
	}


	private static class AlreadyUpgradedResponseWrapper extends HttpServletResponseWrapper {

		public AlreadyUpgradedResponseWrapper(HttpServletResponse response) {
			super(response);
		}

		@Override
		public void setStatus(int sc) {
			Assert.isTrue(sc == HttpStatus.SWITCHING_PROTOCOLS.value(), "Unexpected status code " + sc);
		}
		@Override
		public void addHeader(String name, String value) {
			// ignore
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14582.java