error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9345.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9345.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9345.java
text:
```scala
v@@oid handleMessageFromClient(WebSocketSession session, WebSocketMessage<?> message,

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

package org.springframework.web.socket.messaging;

import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;


/**
 * A contract for handling WebSocket messages as part of a higher level protocol, referred
 * to as "sub-protocol" in the WebSocket RFC specification. Handles both
 * {@link WebSocketMessage}s from a client as well as {@link Message}s to a client.
 * <p>
 * Implementations of this interface can be configured on a
 * {@link SubProtocolWebSocketHandler} which selects a sub-protocol handler to delegate
 * messages to based on the sub-protocol requested by the client through the
 * {@code Sec-WebSocket-Protocol} request header.
 *
 * @author Andy Wilkinson
 * @author Rossen Stoyanchev
 *
 * @since 4.0
 */
public interface SubProtocolHandler {

	/**
	 * Return the list of sub-protocols supported by this handler, never {@code null}.
	 */
	List<String> getSupportedProtocols();

	/**
	 * Handle the given {@link WebSocketMessage} received from a client.
	 *
	 * @param session the client session
	 * @param message the client message
	 * @param outputChannel an output channel to send messages to
	 */
	void handleMessageFromClient(WebSocketSession session, WebSocketMessage message,
			MessageChannel outputChannel) throws Exception;

	/**
	 * Handle the given {@link Message} to the client associated with the given WebSocket
	 * session.
	 *
	 * @param session the client session
	 * @param message the client message
	 */
	void handleMessageToClient(WebSocketSession session, Message<?> message) throws Exception;

	/**
	 * Resolve the session id from the given message or return {@code null}.
	 *
	 * @param message the message to resolve the session id from
	 */
	String resolveSessionId(Message<?> message);

	/**
	 * Invoked after a {@link WebSocketSession} has started.
	 *
	 * @param session the client session
	 * @param outputChannel a channel
	 */
	void afterSessionStarted(WebSocketSession session, MessageChannel outputChannel) throws Exception;

	/**
	 * Invoked after a {@link WebSocketSession} has ended.
	 *
	 * @param session the client session
	 * @param closeStatus the reason why the session was closed
	 * @param outputChannel a channel
	 */
	void afterSessionEnded(WebSocketSession session, CloseStatus closeStatus,
			MessageChannel outputChannel) throws Exception;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9345.java