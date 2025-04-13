error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12949.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12949.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12949.java
text:
```scala
public A@@bstractServerSockJsSession(String sessionId, SockJsConfiguration config, WebSocketHandler handler) {

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

package org.springframework.sockjs.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.springframework.sockjs.AbstractSockJsSession;
import org.springframework.util.Assert;
import org.springframework.websocket.CloseStatus;
import org.springframework.websocket.TextMessage;
import org.springframework.websocket.WebSocketHandler;
import org.springframework.websocket.WebSocketMessage;

/**
 * Provides partial implementations of {@link SockJsSession} methods to send messages,
 * including heartbeat messages and to manage session state.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public abstract class AbstractServerSockJsSession extends AbstractSockJsSession {

	private final SockJsConfiguration sockJsConfig;

	private ScheduledFuture<?> heartbeatTask;


	public AbstractServerSockJsSession(String sessionId, SockJsConfiguration config, WebSocketHandler<?> handler) {
		super(sessionId, handler);
		this.sockJsConfig = config;
	}

	protected SockJsConfiguration getSockJsConfig() {
		return this.sockJsConfig;
	}

	public final synchronized void sendMessage(WebSocketMessage message) throws IOException {
		Assert.isTrue(!isClosed(), "Cannot send a message when session is closed");
		Assert.isInstanceOf(TextMessage.class, message, "Expected text message: " + message);
		sendMessageInternal(((TextMessage) message).getPayload());
	}

	protected abstract void sendMessageInternal(String message) throws IOException;


	@Override
	public void connectionClosedInternal(CloseStatus status) {
		updateLastActiveTime();
		cancelHeartbeat();
	}

	@Override
	public final synchronized void closeInternal(CloseStatus status) throws IOException {
		if (isActive()) {
			// TODO: deliver messages "in flight" before sending close frame
			try {
				// bypass writeFrame
				writeFrameInternal(SockJsFrame.closeFrame(status.getCode(), status.getReason()));
			}
			catch (Throwable ex) {
				logger.warn("Failed to send SockJS close frame: " + ex.getMessage());
			}
		}
		updateLastActiveTime();
		cancelHeartbeat();
		disconnect(status);
	}

	// TODO: close status/reason
	protected abstract void disconnect(CloseStatus status) throws IOException;

	/**
	 * For internal use within a TransportHandler and the (TransportHandler-specific)
	 * session sub-class.
	 */
	protected void writeFrame(SockJsFrame frame) throws IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("Preparing to write " + frame);
		}
		try {
			writeFrameInternal(frame);
		}
		catch (IOException ex) {
			if (ex instanceof EOFException || ex instanceof SocketException) {
				logger.warn("Client went away. Terminating connection");
			}
			else {
				logger.warn("Terminating connection due to failure to send message: " + ex.getMessage());
			}
			close();
			throw ex;
		}
		catch (Throwable ex) {
			logger.warn("Terminating connection due to failure to send message: " + ex.getMessage());
			close();
			throw new NestedSockJsRuntimeException("Failed to write " + frame, ex);
		}
	}

	protected abstract void writeFrameInternal(SockJsFrame frame) throws Exception;

	public synchronized void sendHeartbeat() throws Exception {
		if (isActive()) {
			writeFrame(SockJsFrame.heartbeatFrame());
			scheduleHeartbeat();
		}
	}

	protected void scheduleHeartbeat() {
		Assert.notNull(getSockJsConfig().getTaskScheduler(), "heartbeatScheduler not configured");
		cancelHeartbeat();
		if (!isActive()) {
			return;
		}
		Date time = new Date(System.currentTimeMillis() + getSockJsConfig().getHeartbeatTime());
		this.heartbeatTask = getSockJsConfig().getTaskScheduler().schedule(new Runnable() {
			public void run() {
				try {
					sendHeartbeat();
				}
				catch (Throwable t) {
					// ignore
				}
			}
		}, time);
		if (logger.isTraceEnabled()) {
			logger.trace("Scheduled heartbeat after " + getSockJsConfig().getHeartbeatTime()/1000 + " seconds");
		}
	}

	protected void cancelHeartbeat() {
		if ((this.heartbeatTask != null) && !this.heartbeatTask.isDone()) {
			if (logger.isTraceEnabled()) {
				logger.trace("Cancelling heartbeat");
			}
			this.heartbeatTask.cancel(false);
		}
		this.heartbeatTask = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12949.java