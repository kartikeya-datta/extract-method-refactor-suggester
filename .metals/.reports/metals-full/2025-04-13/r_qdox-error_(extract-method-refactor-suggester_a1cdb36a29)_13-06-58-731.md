error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9558.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9558.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9558.java
text:
```scala
a@@ssertEquals("\"0da1ed070012f304e47b83c81c48ad620\"", response.getHeaders().getETag());

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

package org.springframework.web.socket.sockjs;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.AbstractHttpRequestTests;
import org.springframework.web.socket.WebSocketHandler;

import static org.junit.Assert.*;

/**
 * Test fixture for {@link AbstractSockJsService}.
 *
 * @author Rossen Stoyanchev
 */
public class AbstractSockJsServiceTests extends AbstractHttpRequestTests {

	private TestSockJsService service;

	private WebSocketHandler handler;


	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.service = new TestSockJsService(new ThreadPoolTaskScheduler());
	}

	@Test
	public void getSockJsPathForGreetingRequest() throws Exception {

		handleRequest("GET", "/a", HttpStatus.OK);
		assertEquals("Welcome to SockJS!\n", this.servletResponse.getContentAsString());

		handleRequest("GET", "/a/", HttpStatus.OK);
		assertEquals("Welcome to SockJS!\n", this.servletResponse.getContentAsString());

		this.service.setValidSockJsPrefixes("/b");

		handleRequest("GET", "/a", HttpStatus.NOT_FOUND);
		handleRequest("GET", "/a/", HttpStatus.NOT_FOUND);

		handleRequest("GET", "/b", HttpStatus.OK);
		assertEquals("Welcome to SockJS!\n", this.servletResponse.getContentAsString());
	}

	@Test
	public void getSockJsPathForInfoRequest() throws Exception {

		handleRequest("GET", "/a/info", HttpStatus.OK);

		assertTrue(this.servletResponse.getContentAsString().startsWith("{\"entropy\":"));

		handleRequest("GET", "/a/server/session/xhr", HttpStatus.OK);

		assertEquals("session", this.service.sessionId);
		assertEquals(TransportType.XHR, this.service.transportType);
		assertSame(this.handler, this.service.handler);

		this.service.setValidSockJsPrefixes("/b");

		handleRequest("GET", "/a/info", HttpStatus.NOT_FOUND);
		handleRequest("GET", "/b/info", HttpStatus.OK);

		assertTrue(this.servletResponse.getContentAsString().startsWith("{\"entropy\":"));
	}

	@Test
	public void getSockJsPathForTransportRequest() throws Exception {

		// Info or greeting requests must be first so "/a" is cached as a known prefix
		handleRequest("GET", "/a/info", HttpStatus.OK);
		handleRequest("GET", "/a/server/session/xhr", HttpStatus.OK);

		assertEquals("session", this.service.sessionId);
		assertEquals(TransportType.XHR, this.service.transportType);
		assertSame(this.handler, this.service.handler);
	}

	@Test
	public void getSockJsPathForTransportRequestWithConfiguredPrefix() throws Exception {

		this.service.setValidSockJsPrefixes("/a");
		handleRequest("GET", "/a/server/session/xhr", HttpStatus.OK);

		assertEquals("session", this.service.sessionId);
		assertEquals(TransportType.XHR, this.service.transportType);
		assertSame(this.handler, this.service.handler);
	}

	@Test
	public void validateRequest() throws Exception {

		this.service.setValidSockJsPrefixes("/echo");

		this.service.setWebSocketsEnabled(false);
		handleRequest("GET", "/echo/server/session/websocket", HttpStatus.NOT_FOUND);

		this.service.setWebSocketsEnabled(true);
		handleRequest("GET", "/echo/server/session/websocket", HttpStatus.OK);

		handleRequest("GET", "/echo//", HttpStatus.NOT_FOUND);
		handleRequest("GET", "/echo///", HttpStatus.NOT_FOUND);
		handleRequest("GET", "/echo/other", HttpStatus.NOT_FOUND);
		handleRequest("GET", "/echo//service/websocket", HttpStatus.NOT_FOUND);
		handleRequest("GET", "/echo/server//websocket", HttpStatus.NOT_FOUND);
		handleRequest("GET", "/echo/server/session/", HttpStatus.NOT_FOUND);
		handleRequest("GET", "/echo/s.erver/session/websocket", HttpStatus.NOT_FOUND);
		handleRequest("GET", "/echo/server/s.ession/websocket", HttpStatus.NOT_FOUND);
	}

	@Test
	public void handleInfoGet() throws Exception {

		handleRequest("GET", "/a/info", HttpStatus.OK);

		assertEquals("application/json;charset=UTF-8", this.servletResponse.getContentType());
		assertEquals("*", this.servletResponse.getHeader("Access-Control-Allow-Origin"));
		assertEquals("true", this.servletResponse.getHeader("Access-Control-Allow-Credentials"));
		assertEquals("no-store, no-cache, must-revalidate, max-age=0", this.servletResponse.getHeader("Cache-Control"));

		String body = this.servletResponse.getContentAsString();
		assertEquals("{\"entropy\"", body.substring(0, body.indexOf(':')));
		assertEquals(",\"origins\":[\"*:*\"],\"cookie_needed\":true,\"websocket\":true}",
				body.substring(body.indexOf(',')));

		this.service.setJsessionIdCookieRequired(false);
		this.service.setWebSocketsEnabled(false);
		handleRequest("GET", "/a/info", HttpStatus.OK);

		body = this.servletResponse.getContentAsString();
		assertEquals(",\"origins\":[\"*:*\"],\"cookie_needed\":false,\"websocket\":false}",
				body.substring(body.indexOf(',')));
	}

	@Test
	public void handleInfoOptions() throws Exception {

		this.servletRequest.addHeader("Access-Control-Request-Headers", "Last-Modified");

		handleRequest("OPTIONS", "/a/info", HttpStatus.NO_CONTENT);

		assertEquals("*", this.servletResponse.getHeader("Access-Control-Allow-Origin"));
		assertEquals("true", this.servletResponse.getHeader("Access-Control-Allow-Credentials"));
		assertEquals("Last-Modified", this.servletResponse.getHeader("Access-Control-Allow-Headers"));
		assertEquals("OPTIONS, GET", this.servletResponse.getHeader("Access-Control-Allow-Methods"));
		assertEquals("31536000", this.servletResponse.getHeader("Access-Control-Max-Age"));
	}

	@Test
	public void handleIframeRequest() throws Exception {

		this.service.setValidSockJsPrefixes("/a");
		handleRequest("GET", "/a/iframe.html", HttpStatus.OK);

		assertEquals("text/html;charset=UTF-8", this.servletResponse.getContentType());
		assertTrue(this.servletResponse.getContentAsString().startsWith("<!DOCTYPE html>\n"));
		assertEquals(496, this.servletResponse.getContentLength());
		assertEquals("public, max-age=31536000", this.response.getHeaders().getCacheControl());
		assertEquals("\"0da1ed070012f304e47b83c81c48ad620\"", this.response.getHeaders().getETag());
	}

	@Test
	public void handleIframeRequestNotModified() throws Exception {

		this.servletRequest.addHeader("If-None-Match", "\"0da1ed070012f304e47b83c81c48ad620\"");

		this.service.setValidSockJsPrefixes("/a");
		handleRequest("GET", "/a/iframe.html", HttpStatus.NOT_MODIFIED);
	}

	@Test
	public void handleRawWebSocketRequest() throws Exception {

		handleRequest("GET", "/a", HttpStatus.OK);
		assertEquals("Welcome to SockJS!\n", this.servletResponse.getContentAsString());

		handleRequest("GET", "/a/websocket", HttpStatus.OK);
		assertNull("Raw WebSocket should not open a SockJS session", this.service.sessionId);
		assertSame(this.handler, this.service.handler);
	}


	private void handleRequest(String httpMethod, String uri, HttpStatus httpStatus) throws IOException {
		resetResponse();
		setRequest(httpMethod, uri);
		this.service.handleRequest(this.request, this.response, this.handler);

		assertEquals(httpStatus.value(), this.servletResponse.getStatus());
	}

	private static class TestSockJsService extends AbstractSockJsService {

		private String sessionId;

		private TransportType transportType;

		private WebSocketHandler handler;

		public TestSockJsService(TaskScheduler scheduler) {
			super(scheduler);
		}

		@Override
		protected void handleRawWebSocketRequest(ServerHttpRequest request,
				ServerHttpResponse response, WebSocketHandler handler) throws IOException {

			this.handler = handler;
		}

		@Override
		protected void handleTransportRequest(ServerHttpRequest request,
				ServerHttpResponse response, String sessionId,
				TransportType transportType, WebSocketHandler handler)
				throws IOException, TransportErrorException {

			this.sessionId = sessionId;
			this.transportType = transportType;
			this.handler = handler;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9558.java