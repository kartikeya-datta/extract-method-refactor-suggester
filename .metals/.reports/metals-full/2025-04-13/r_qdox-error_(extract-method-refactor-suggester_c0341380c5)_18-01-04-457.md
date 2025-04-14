error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13320.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13320.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13320.java
text:
```scala
c@@aptor.getValue().getHeaders().get(SimpMessageHeaderAccessor.ORIGINAL_DESTINATION));

/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.messaging.simp.user;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.StubMessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.TestPrincipal;
import org.springframework.messaging.support.MessageBuilder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link org.springframework.messaging.simp.user.UserDestinationMessageHandler}.
 */
public class UserDestinationMessageHandlerTests {

	public static final String SESSION_ID = "123";
	private UserDestinationMessageHandler messageHandler;


	@Mock
	private SubscribableChannel brokerChannel;

	private UserSessionRegistry registry;


	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.registry = new DefaultUserSessionRegistry();
		DefaultUserDestinationResolver resolver = new DefaultUserDestinationResolver(this.registry);
		this.messageHandler = new UserDestinationMessageHandler(new StubMessageChannel(), this.brokerChannel, resolver);
	}


	@Test
	@SuppressWarnings("rawtypes")
	public void handleSubscribe() {
		when(this.brokerChannel.send(Mockito.any(Message.class))).thenReturn(true);
		this.messageHandler.handleMessage(createMessage(SimpMessageType.SUBSCRIBE, "joe", SESSION_ID, "/user/queue/foo"));

		ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
		Mockito.verify(this.brokerChannel).send(captor.capture());

		assertEquals("/queue/foo-user123",
				captor.getValue().getHeaders().get(SimpMessageHeaderAccessor.DESTINATION_HEADER));
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void handleUnsubscribe() {
		when(this.brokerChannel.send(Mockito.any(Message.class))).thenReturn(true);
		this.messageHandler.handleMessage(createMessage(SimpMessageType.UNSUBSCRIBE, "joe", "123", "/user/queue/foo"));

		ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
		Mockito.verify(this.brokerChannel).send(captor.capture());

		assertEquals("/queue/foo-user123",
				captor.getValue().getHeaders().get(SimpMessageHeaderAccessor.DESTINATION_HEADER));
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void handleMessage() {
		this.registry.registerSessionId("joe", "123");
		when(this.brokerChannel.send(Mockito.any(Message.class))).thenReturn(true);
		this.messageHandler.handleMessage(createMessage(SimpMessageType.MESSAGE, "joe", "123", "/user/joe/queue/foo"));

		ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
		Mockito.verify(this.brokerChannel).send(captor.capture());

		assertEquals("/queue/foo-user123",
				captor.getValue().getHeaders().get(SimpMessageHeaderAccessor.DESTINATION_HEADER));
		assertEquals("/user/queue/foo",
				captor.getValue().getHeaders().get(UserDestinationMessageHandler.SUBSCRIBE_DESTINATION));
	}


	@Test
	public void ignoreMessage() {

		// no destination
		this.messageHandler.handleMessage(createMessage(SimpMessageType.MESSAGE, "joe", "123", null));
		Mockito.verifyZeroInteractions(this.brokerChannel);

		// not a user destination
		this.messageHandler.handleMessage(createMessage(SimpMessageType.MESSAGE, "joe", "123", "/queue/foo"));
		Mockito.verifyZeroInteractions(this.brokerChannel);

		// subscribe + no user
		this.messageHandler.handleMessage(createMessage(SimpMessageType.SUBSCRIBE, null, "123", "/user/queue/foo"));
		Mockito.verifyZeroInteractions(this.brokerChannel);

		// subscribe + not a user destination
		this.messageHandler.handleMessage(createMessage(SimpMessageType.SUBSCRIBE, "joe", "123", "/queue/foo"));
		Mockito.verifyZeroInteractions(this.brokerChannel);

		// no match on message type
		this.messageHandler.handleMessage(createMessage(SimpMessageType.CONNECT, "joe", "123", "user/joe/queue/foo"));
		Mockito.verifyZeroInteractions(this.brokerChannel);
	}


	private Message<?> createMessage(SimpMessageType messageType, String user, String sessionId, String destination) {
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.create(messageType);
		if (destination != null) {
			headers.setDestination(destination);
		}
		if (user != null) {
			headers.setUser(new TestPrincipal(user));
		}
		if (sessionId != null) {
			headers.setSessionId(sessionId);
		}
		return MessageBuilder.withPayload(new byte[0]).setHeaders(headers).build();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13320.java