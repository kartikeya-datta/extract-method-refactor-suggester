error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14584.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14584.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14584.java
text:
```scala
c@@atch (TransportErrorException ex) {

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
import java.sql.Date;
import java.util.Collections;
import java.util.concurrent.ScheduledFuture;

import org.junit.Test;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;


/**
 * Test fixture for {@link AbstractSockJsSession}.
 *
 * @author Rossen Stoyanchev
 */
public class AbstractSockJsSessionTests extends BaseAbstractSockJsSessionTests<TestSockJsSession> {


	@Override
	protected TestSockJsSession initSockJsSession() {
		return new TestSockJsSession("1", this.sockJsConfig, this.webSocketHandler);
	}

	@Test
	public void getTimeSinceLastActive() throws Exception {

		Thread.sleep(1);

		long time1 = this.session.getTimeSinceLastActive();
		assertTrue(time1 > 0);

		Thread.sleep(1);

		long time2 = this.session.getTimeSinceLastActive();
		assertTrue(time2 > time1);

		this.session.delegateConnectionEstablished();

		Thread.sleep(1);

		this.session.setActive(false);
		assertTrue(this.session.getTimeSinceLastActive() > 0);

		this.session.setActive(true);
		assertEquals(0, this.session.getTimeSinceLastActive());
	}

	@Test
	public void delegateConnectionEstablished() throws Exception {
		assertNew();
		this.session.delegateConnectionEstablished();
		assertOpen();
		verify(this.webSocketHandler).afterConnectionEstablished(this.session);
	}

	@Test
	public void delegateError() throws Exception {
		Exception ex = new Exception();
		this.session.delegateError(ex);
		verify(this.webSocketHandler).handleTransportError(this.session, ex);
	}

	@Test
	public void delegateMessages() throws Exception {
		String msg1 = "message 1";
		String msg2 = "message 2";
		this.session.delegateMessages(new String[] { msg1, msg2 });

		verify(this.webSocketHandler).handleMessage(this.session, new TextMessage(msg1));
		verify(this.webSocketHandler).handleMessage(this.session, new TextMessage(msg2));
		verifyNoMoreInteractions(this.webSocketHandler);
	}

	@Test
	public void delegateConnectionClosed() throws Exception {
		this.session.delegateConnectionEstablished();
		this.session.delegateConnectionClosed(CloseStatus.GOING_AWAY);

		assertClosed();
		assertEquals(1, this.session.getNumberOfLastActiveTimeUpdates());
		assertTrue(this.session.didCancelHeartbeat());
		verify(this.webSocketHandler).afterConnectionClosed(this.session, CloseStatus.GOING_AWAY);
	}

	@Test
	public void closeWhenNotOpen() throws Exception {

		assertNew();

		this.session.close();
		assertNull("Close not ignored for a new session", this.session.getStatus());

		this.session.delegateConnectionEstablished();
		assertOpen();

		this.session.close();
		assertClosed();
		assertEquals(3000, this.session.getStatus().getCode());

		this.session.close(CloseStatus.SERVER_ERROR);
		assertEquals("Close should be ignored if already closed", 3000, this.session.getStatus().getCode());
	}

	@Test
	public void closeWhenNotActive() throws Exception {

		this.session.delegateConnectionEstablished();
		assertOpen();

		this.session.setActive(false);
		this.session.close();

		assertEquals(Collections.emptyList(), this.session.getSockJsFramesWritten());
	}

	@Test
	public void close() throws Exception {

		this.session.delegateConnectionEstablished();
		assertOpen();

		this.session.setActive(true);
		this.session.close();

		assertEquals(1, this.session.getSockJsFramesWritten().size());
		assertEquals(SockJsFrame.closeFrameGoAway(), this.session.getSockJsFramesWritten().get(0));

		assertEquals(1, this.session.getNumberOfLastActiveTimeUpdates());
		assertTrue(this.session.didCancelHeartbeat());

		assertEquals(new CloseStatus(3000, "Go away!"), this.session.getStatus());
		assertClosed();
		verify(this.webSocketHandler).afterConnectionClosed(this.session, new CloseStatus(3000, "Go away!"));
	}

	@Test
	public void closeWithWriteFrameExceptions() throws Exception {

		this.session.setExceptionOnWriteFrame(new IOException());

		this.session.delegateConnectionEstablished();
		this.session.setActive(true);
		this.session.close();

		assertEquals(new CloseStatus(3000, "Go away!"), this.session.getStatus());
		assertClosed();
	}

	@Test
	public void closeWithWebSocketHandlerExceptions() throws Exception {

		doThrow(new Exception()).when(this.webSocketHandler).afterConnectionClosed(this.session, CloseStatus.NORMAL);

		this.session.delegateConnectionEstablished();
		this.session.setActive(true);
		this.session.close(CloseStatus.NORMAL);

		assertEquals(CloseStatus.NORMAL, this.session.getStatus());
		assertClosed();
	}

	@Test
	public void writeFrame() throws Exception {
		this.session.writeFrame(SockJsFrame.openFrame());

		assertEquals(1, this.session.getSockJsFramesWritten().size());
		assertEquals(SockJsFrame.openFrame(), this.session.getSockJsFramesWritten().get(0));
	}

	@Test
	public void writeFrameIoException() throws Exception {
		this.session.setExceptionOnWriteFrame(new IOException());
		this.session.delegateConnectionEstablished();
		try {
			this.session.writeFrame(SockJsFrame.openFrame());
			fail("expected exception");
		}
		catch (IOException ex) {
			assertEquals(CloseStatus.SERVER_ERROR, this.session.getStatus());
			verify(this.webSocketHandler).afterConnectionClosed(this.session, CloseStatus.SERVER_ERROR);
		}
	}

	@Test
	public void writeFrameThrowable() throws Exception {
		this.session.setExceptionOnWriteFrame(new NullPointerException());
		this.session.delegateConnectionEstablished();
		try {
			this.session.writeFrame(SockJsFrame.openFrame());
			fail("expected exception");
		}
		catch (SockJsRuntimeException ex) {
			assertEquals(CloseStatus.SERVER_ERROR, this.session.getStatus());
			verify(this.webSocketHandler).afterConnectionClosed(this.session, CloseStatus.SERVER_ERROR);
		}
	}

	@Test
	public void sendHeartbeatWhenNotActive() throws Exception {
		this.session.setActive(false);
		this.session.sendHeartbeat();

		assertEquals(Collections.emptyList(), this.session.getSockJsFramesWritten());
	}

	@Test
	public void sendHeartbeat() throws Exception {
		this.session.setActive(true);
		this.session.sendHeartbeat();

		assertEquals(1, this.session.getSockJsFramesWritten().size());
		assertEquals(SockJsFrame.heartbeatFrame(), this.session.getSockJsFramesWritten().get(0));

		verify(this.taskScheduler).schedule(any(Runnable.class), any(Date.class));
		verifyNoMoreInteractions(this.taskScheduler);
	}

	@Test
	public void scheduleHeartbeatNotActive() throws Exception {
		this.session.setActive(false);
		this.session.scheduleHeartbeat();

		verifyNoMoreInteractions(this.taskScheduler);
	}

	@Test
	public void scheduleAndCancelHeartbeat() throws Exception {

		ScheduledFuture<?> task = mock(ScheduledFuture.class);
		doReturn(task).when(this.taskScheduler).schedule(any(Runnable.class), any(Date.class));

		this.session.setActive(true);
		this.session.scheduleHeartbeat();

		verify(this.taskScheduler).schedule(any(Runnable.class), any(Date.class));
		verifyNoMoreInteractions(this.taskScheduler);

		doReturn(false).when(task).isDone();

		this.session.cancelHeartbeat();

		verify(task).isDone();
		verify(task).cancel(false);
		verifyNoMoreInteractions(task);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14584.java