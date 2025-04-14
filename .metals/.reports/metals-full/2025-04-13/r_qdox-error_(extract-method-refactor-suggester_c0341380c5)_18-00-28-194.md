error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7459.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7459.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7459.java
text:
```scala
protected v@@oid flushCache() {

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

package org.springframework.web.socket.sockjs.transport.session;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.mock.web.test.MockHttpServletResponse;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.sockjs.support.frame.SockJsFrame;
import org.springframework.web.socket.sockjs.support.frame.SockJsFrame.DefaultFrameFormat;
import org.springframework.web.socket.sockjs.support.frame.SockJsFrame.FrameFormat;
import org.springframework.web.socket.sockjs.transport.session.AbstractHttpSockJsSessionTests.TestAbstractHttpSockJsSession;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test fixture for {@link AbstractHttpSockJsSession}.
 *
 * @author Rossen Stoyanchev
 */
public class AbstractHttpSockJsSessionTests extends BaseAbstractSockJsSessionTests<TestAbstractHttpSockJsSession> {

	protected ServerHttpRequest request;

	protected ServerHttpResponse response;

	protected MockHttpServletRequest servletRequest;

	protected MockHttpServletResponse servletResponse;

	private FrameFormat frameFormat;


	@Before
	public void setup() {

		super.setUp();

		this.frameFormat = new DefaultFrameFormat("%s");

		this.servletResponse = new MockHttpServletResponse();
		this.response = new ServletServerHttpResponse(this.servletResponse);

		this.servletRequest = new MockHttpServletRequest();
		this.servletRequest.setAsyncSupported(true);
		this.request = new ServletServerHttpRequest(this.servletRequest);
	}

	@Override
	protected TestAbstractHttpSockJsSession initSockJsSession() {
		return new TestAbstractHttpSockJsSession(this.sockJsConfig, this.webSocketHandler);
	}

	@Test
	public void setInitialRequest() throws Exception {

		this.session.setInitialRequest(this.request, this.response, this.frameFormat);

		assertTrue(this.session.hasRequest());
		assertTrue(this.session.hasResponse());

		assertEquals("o", this.servletResponse.getContentAsString());
		assertFalse(this.servletRequest.isAsyncStarted());

		verify(this.webSocketHandler).afterConnectionEstablished(this.session);
	}

	@Test
	public void setLongPollingRequest() throws Exception {

		this.session.getMessageCache().add("x");
		this.session.setLongPollingRequest(this.request, this.response, this.frameFormat);

		assertTrue(this.session.hasRequest());
		assertTrue(this.session.hasResponse());
		assertTrue(this.servletRequest.isAsyncStarted());

		assertTrue(this.session.wasHeartbeatScheduled());
		assertTrue(this.session.wasCacheFlushed());

		verifyNoMoreInteractions(this.webSocketHandler);
	}

	@Test
	public void setLongPollingRequestWhenClosed() throws Exception {

		this.session.delegateConnectionClosed(CloseStatus.NORMAL);
		assertClosed();

		this.session.setLongPollingRequest(this.request, this.response, this.frameFormat);

		assertEquals("c[3000,\"Go away!\"]", this.servletResponse.getContentAsString());
		assertFalse(this.servletRequest.isAsyncStarted());
	}


	static class TestAbstractHttpSockJsSession extends AbstractHttpSockJsSession {

		private IOException exceptionOnWriteFrame;

		private boolean cacheFlushed;

		private boolean heartbeatScheduled;


		public TestAbstractHttpSockJsSession(SockJsServiceConfig config, WebSocketHandler handler) {
			super("1", config, handler);
		}

		public boolean wasCacheFlushed() {
			return this.cacheFlushed;
		}

		public boolean wasHeartbeatScheduled() {
			return this.heartbeatScheduled;
		}

		public boolean hasRequest() {
			return getRequest() != null;
		}

		public boolean hasResponse() {
			return getResponse() != null;
		}

		public void setExceptionOnWriteFrame(IOException exceptionOnWriteFrame) {
			this.exceptionOnWriteFrame = exceptionOnWriteFrame;
		}

		@Override
		protected void flushCache() throws IOException {
			this.cacheFlushed = true;
		}

		@Override
		protected void scheduleHeartbeat() {
			this.heartbeatScheduled = true;
		}

		@Override
		protected synchronized void writeFrameInternal(SockJsFrame frame) throws IOException {
			if (this.exceptionOnWriteFrame != null) {
				throw this.exceptionOnWriteFrame;
			}
			else {
				super.writeFrameInternal(frame);
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7459.java