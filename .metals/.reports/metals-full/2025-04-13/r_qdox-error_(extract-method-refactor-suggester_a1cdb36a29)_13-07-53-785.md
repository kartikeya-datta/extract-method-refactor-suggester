error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15086.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15086.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15086.java
text:
```scala
H@@andlerProvider<WebSocketHandler<?>> handler) {

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

package org.springframework.sockjs.server.transport;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.http.server.AsyncServerHttpRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.sockjs.server.AbstractServerSockJsSession;
import org.springframework.sockjs.server.SockJsConfiguration;
import org.springframework.sockjs.server.SockJsFrame;
import org.springframework.sockjs.server.TransportErrorException;
import org.springframework.sockjs.server.SockJsFrame.FrameFormat;
import org.springframework.util.Assert;
import org.springframework.websocket.CloseStatus;
import org.springframework.websocket.HandlerProvider;
import org.springframework.websocket.WebSocketHandler;

/**
 * An abstract base class for use with HTTP-based transports.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public abstract class AbstractHttpServerSockJsSession extends AbstractServerSockJsSession {

	private FrameFormat frameFormat;

	private final BlockingQueue<String> messageCache = new ArrayBlockingQueue<String>(100);

	private AsyncServerHttpRequest asyncRequest;

	private ServerHttpResponse response;


	public AbstractHttpServerSockJsSession(String sessionId, SockJsConfiguration sockJsConfig,
			HandlerProvider<WebSocketHandler> handler) {

		super(sessionId, sockJsConfig, handler);
	}

	public synchronized void setInitialRequest(ServerHttpRequest request, ServerHttpResponse response,
			FrameFormat frameFormat) throws TransportErrorException {

		try {
			udpateRequest(request, response, frameFormat);
			writePrelude();
			writeFrame(SockJsFrame.openFrame());
		}
		catch (Throwable t) {
			tryCloseWithSockJsTransportError(t, null);
			throw new TransportErrorException("Failed open SockJS session", t, getId());
		}
		delegateConnectionEstablished();
	}

	protected void writePrelude() throws IOException {
	}

	public synchronized void setLongPollingRequest(ServerHttpRequest request, ServerHttpResponse response,
			FrameFormat frameFormat) throws TransportErrorException {

		try {
			udpateRequest(request, response, frameFormat);

			if (isClosed()) {
				logger.debug("connection already closed");
				try {
					writeFrame(SockJsFrame.closeFrameGoAway());
				}
				catch (IOException ex) {
					throw new TransportErrorException("Failed to send SockJS close frame", ex, getId());
				}
				return;
			}

			this.asyncRequest.setTimeout(-1);
			this.asyncRequest.startAsync();

			scheduleHeartbeat();
			tryFlushCache();
		}
		catch (Throwable t) {
			tryCloseWithSockJsTransportError(t, null);
			throw new TransportErrorException("Failed to start long running request and flush messages", t, getId());
		}
	}

	private void udpateRequest(ServerHttpRequest request, ServerHttpResponse response, FrameFormat frameFormat) {
		Assert.notNull(request, "expected request");
		Assert.notNull(response, "expected response");
		Assert.notNull(frameFormat, "expected frameFormat");
		Assert.isInstanceOf(AsyncServerHttpRequest.class, request, "Expected AsyncServerHttpRequest");
		this.asyncRequest = (AsyncServerHttpRequest) request;
		this.response = response;
		this.frameFormat = frameFormat;
	}


	public synchronized boolean isActive() {
		return ((this.asyncRequest != null) && (!this.asyncRequest.isAsyncCompleted()));
	}

	protected BlockingQueue<String> getMessageCache() {
		return this.messageCache;
	}

	protected ServerHttpRequest getRequest() {
		return this.asyncRequest;
	}

	protected ServerHttpResponse getResponse() {
		return this.response;
	}

	protected final synchronized void sendMessageInternal(String message) throws IOException {
		this.messageCache.add(message);
		tryFlushCache();
	}

	private void tryFlushCache() throws IOException {
		if (isActive() && !getMessageCache().isEmpty()) {
			logger.trace("Flushing messages");
			flushCache();
		}
	}

	/**
	 * Only called if the connection is currently active
	 */
	protected abstract void flushCache() throws IOException;

	@Override
	protected void disconnect(CloseStatus status) {
		resetRequest();
	}

	protected synchronized void resetRequest() {
		updateLastActiveTime();
		if (isActive()) {
			try {
				this.asyncRequest.completeAsync();
			}
			catch (Throwable ex) {
				logger.warn("Failed to complete async request: " + ex.getMessage());
			}
		}
		this.asyncRequest = null;
		this.response = null;
	}

	protected synchronized void writeFrameInternal(SockJsFrame frame) throws IOException {
		if (isActive()) {
			frame = this.frameFormat.format(frame);
			if (logger.isTraceEnabled()) {
				logger.trace("Writing " + frame);
			}
			this.response.getBody().write(frame.getContentBytes());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15086.java