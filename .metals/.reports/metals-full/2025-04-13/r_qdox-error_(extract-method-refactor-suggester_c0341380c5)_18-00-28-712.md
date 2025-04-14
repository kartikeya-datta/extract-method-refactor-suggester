error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7410.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7410.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7410.java
text:
```scala
public v@@oid delegateError(Throwable ex) throws Exception {

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

package org.springframework.sockjs;

import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.websocket.CloseStatus;
import org.springframework.websocket.HandlerProvider;
import org.springframework.websocket.TextMessage;
import org.springframework.websocket.TextMessageHandler;
import org.springframework.websocket.WebSocketHandler;
import org.springframework.websocket.WebSocketSession;


/**
 * TODO
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public abstract class AbstractSockJsSession implements WebSocketSession {

	protected Log logger = LogFactory.getLog(this.getClass());

	private final String sessionId;

	private final HandlerProvider<WebSocketHandler> handlerProvider;

	private TextMessageHandler handler;

	private State state = State.NEW;

	private long timeCreated = System.currentTimeMillis();

	private long timeLastActive = System.currentTimeMillis();


	/**
	 *
	 * @param sessionId
	 * @param handlerProvider the recipient of SockJS messages
	 */
	public AbstractSockJsSession(String sessionId, HandlerProvider<WebSocketHandler> handlerProvider) {
		Assert.notNull(sessionId, "sessionId is required");
		Assert.notNull(handlerProvider, "handlerProvider is required");
		this.sessionId = sessionId;
		this.handlerProvider = handlerProvider;
	}

	public String getId() {
		return this.sessionId;
	}

	@Override
	public boolean isSecure() {
		// TODO
		return false;
	}

	@Override
	public URI getURI() {
		// TODO
		return null;
	}

	public boolean isNew() {
		return State.NEW.equals(this.state);
	}

	public boolean isOpen() {
		return State.OPEN.equals(this.state);
	}

	public boolean isClosed() {
		return State.CLOSED.equals(this.state);
	}

	/**
	 * Polling and Streaming sessions periodically close the current HTTP request and
	 * wait for the next request to come through. During this "downtime" the session is
	 * still open but inactive and unable to send messages and therefore has to buffer
	 * them temporarily. A WebSocket session by contrast is stateful and remain active
	 * until closed.
	 */
	public abstract boolean isActive();

	/**
	 * Return the time since the session was last active, or otherwise if the
	 * session is new, the time since the session was created.
	 */
	public long getTimeSinceLastActive() {
		if (isNew()) {
			return (System.currentTimeMillis() - this.timeCreated);
		}
		else {
			return isActive() ? 0 : System.currentTimeMillis() - this.timeLastActive;
		}
	}

	/**
	 * Should be invoked whenever the session becomes inactive.
	 */
	protected void updateLastActiveTime() {
		this.timeLastActive = System.currentTimeMillis();
	}

	public void delegateConnectionEstablished() throws Exception {
		this.state = State.OPEN;
		initHandler();
		this.handler.afterConnectionEstablished(this);
	}

	private void initHandler() {
		WebSocketHandler webSocketHandler = handlerProvider.getHandler();
		Assert.isInstanceOf(TextMessageHandler.class, webSocketHandler, "Expected a TextMessageHandler");
		this.handler = (TextMessageHandler) webSocketHandler;
	}

	public void delegateMessages(String[] messages) throws Exception {
		for (String message : messages) {
			this.handler.handleTextMessage(new TextMessage(message), this);
		}
	}

	public void delegateError(Throwable ex) {
		this.handler.handleError(ex, this);
	}

	/**
	 * Invoked in reaction to the underlying connection being closed by the remote side
	 * (or the WebSocket container) in order to perform cleanup and notify the
	 * {@link TextMessageHandler}. This is in contrast to {@link #close()} that pro-actively
	 * closes the connection.
	 */
	public final void delegateConnectionClosed(CloseStatus status) throws Exception {
		if (!isClosed()) {
			if (logger.isDebugEnabled()) {
				logger.debug(this + " was closed, " + status);
			}
			try {
				connectionClosedInternal(status);
			}
			finally {
				this.state = State.CLOSED;
				this.handler.afterConnectionClosed(status, this);
			}
		}
	}

	protected void connectionClosedInternal(CloseStatus status) {
	}

	/**
	 * {@inheritDoc}
	 * <p>Performs cleanup and notifies the {@link SockJsHandler}.
	 */
	public final void close() throws Exception {
		close(CloseStatus.NORMAL);
	}

	/**
	 * {@inheritDoc}
	 * <p>Performs cleanup and notifies the {@link SockJsHandler}.
	 */
	public final void close(CloseStatus status) throws Exception {
		if (!isClosed()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Closing " + this + ", " + status);
			}
			try {
				closeInternal(status);
			}
			finally {
				this.state = State.CLOSED;
				try {
					this.handler.afterConnectionClosed(status, this);
				}
				finally {
					this.handlerProvider.destroy(this.handler);
				}
			}
		}
	}

	protected abstract void closeInternal(CloseStatus status) throws Exception;


	@Override
	public String toString() {
		return "SockJS session id=" + this.sessionId;
	}


	private enum State { NEW, OPEN, CLOSED }

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7410.java