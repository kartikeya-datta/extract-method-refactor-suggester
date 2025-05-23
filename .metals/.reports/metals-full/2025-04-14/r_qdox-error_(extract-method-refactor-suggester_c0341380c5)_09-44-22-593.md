error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8346.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8346.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8346.java
text:
```scala
h@@eaders.setNativeHeader(StompHeaderAccessor.ORIGINAL_DESTINATION, "/user/queue/foo");

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

package org.springframework.web.socket.messaging;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.TestPrincipal;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompDecoder;
import org.springframework.messaging.simp.stomp.StompEncoder;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.DefaultUserSessionRegistry;
import org.springframework.messaging.simp.user.DestinationUserNameProvider;
import org.springframework.messaging.simp.user.UserSessionRegistry;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.handler.TestWebSocketSession;
import org.springframework.web.socket.sockjs.transport.SockJsSession;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test fixture for {@link StompSubProtocolHandler} tests.
 *
 * @author Rossen Stoyanchev
 */
public class StompSubProtocolHandlerTests {

	public static final byte[] EMPTY_PAYLOAD = new byte[0];

	private StompSubProtocolHandler protocolHandler;

	private TestWebSocketSession session;

	private MessageChannel channel;

	@SuppressWarnings("rawtypes")
	private ArgumentCaptor<Message> messageCaptor;


	@Before
	public void setup() {
		this.protocolHandler = new StompSubProtocolHandler();
		this.channel = Mockito.mock(MessageChannel.class);
		this.messageCaptor = ArgumentCaptor.forClass(Message.class);

		this.session = new TestWebSocketSession();
		this.session.setId("s1");
		this.session.setPrincipal(new TestPrincipal("joe"));
	}

	@Test
	public void handleMessageToClientConnected() {

		UserSessionRegistry registry = new DefaultUserSessionRegistry();
		this.protocolHandler.setUserSessionRegistry(registry);

		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.CONNECTED);
		Message<byte[]> message = MessageBuilder.createMessage(EMPTY_PAYLOAD, headers.getMessageHeaders());
		this.protocolHandler.handleMessageToClient(this.session, message);

		assertEquals(1, this.session.getSentMessages().size());
		WebSocketMessage<?> textMessage = this.session.getSentMessages().get(0);
		assertEquals("CONNECTED\n" + "user-name:joe\n" + "\n" + "\u0000", textMessage.getPayload());

		assertEquals(Collections.singleton("s1"), registry.getSessionIds("joe"));
	}

	@Test
	public void handleMessageToClientConnectedUniqueUserName() {

		this.session.setPrincipal(new UniqueUser("joe"));

		UserSessionRegistry registry = new DefaultUserSessionRegistry();
		this.protocolHandler.setUserSessionRegistry(registry);

		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.CONNECTED);
		Message<byte[]> message = MessageBuilder.createMessage(EMPTY_PAYLOAD, headers.getMessageHeaders());
		this.protocolHandler.handleMessageToClient(this.session, message);

		assertEquals(1, this.session.getSentMessages().size());
		WebSocketMessage<?> textMessage = this.session.getSentMessages().get(0);
		assertEquals("CONNECTED\n" + "user-name:joe\n" + "\n" + "\u0000", textMessage.getPayload());

		assertEquals(Collections.<String>emptySet(), registry.getSessionIds("joe"));
		assertEquals(Collections.singleton("s1"), registry.getSessionIds("Me myself and I"));
	}

	@Test
	public void handleMessageToClientConnectedWithHeartbeats() {

		SockJsSession sockJsSession = Mockito.mock(SockJsSession.class);

		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.CONNECTED);
		headers.setHeartbeat(0,10);
		Message<byte[]> message = MessageBuilder.createMessage(EMPTY_PAYLOAD, headers.getMessageHeaders());
		this.protocolHandler.handleMessageToClient(sockJsSession, message);

		verify(sockJsSession).disableHeartbeat();
	}

	@Test
	public void handleMessageToClientConnectAck() {

		StompHeaderAccessor connectHeaders = StompHeaderAccessor.create(StompCommand.CONNECT);
		connectHeaders.setHeartbeat(10000, 10000);
		connectHeaders.setAcceptVersion("1.0,1.1");
		Message<?> connectMessage = MessageBuilder.createMessage(EMPTY_PAYLOAD, connectHeaders.getMessageHeaders());

		SimpMessageHeaderAccessor connectAckHeaders = SimpMessageHeaderAccessor.create(SimpMessageType.CONNECT_ACK);
		connectAckHeaders.setHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER, connectMessage);
		Message<byte[]> connectAckMessage = MessageBuilder.createMessage(EMPTY_PAYLOAD, connectAckHeaders.getMessageHeaders());

		this.protocolHandler.handleMessageToClient(this.session, connectAckMessage);

		verifyNoMoreInteractions(this.channel);

		// Check CONNECTED reply

		assertEquals(1, this.session.getSentMessages().size());
		TextMessage textMessage = (TextMessage) this.session.getSentMessages().get(0);

		List<Message<byte[]>> messages = new StompDecoder().decode(ByteBuffer.wrap(textMessage.getPayload().getBytes()));
		assertEquals(1, messages.size());
		StompHeaderAccessor replyHeaders = StompHeaderAccessor.wrap(messages.get(0));

		assertEquals(StompCommand.CONNECTED, replyHeaders.getCommand());
		assertEquals("1.1", replyHeaders.getVersion());
		assertArrayEquals(new long[] {0, 0}, replyHeaders.getHeartbeat());
		assertEquals("joe", replyHeaders.getNativeHeader("user-name").get(0));
	}

	@Test
	public void eventPublication() {

		TestPublisher publisher = new TestPublisher();

		UserSessionRegistry registry = new DefaultUserSessionRegistry();
		this.protocolHandler.setUserSessionRegistry(registry);
		this.protocolHandler.setApplicationEventPublisher(publisher);
		this.protocolHandler.afterSessionStarted(this.session, this.channel);

		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.CONNECT);
		Message<byte[]> message = MessageBuilder.createMessage(EMPTY_PAYLOAD, headers.getMessageHeaders());
		TextMessage textMessage = new TextMessage(new StompEncoder().encode(message));
		this.protocolHandler.handleMessageFromClient(this.session, textMessage, this.channel);

		headers = StompHeaderAccessor.create(StompCommand.CONNECTED);
		message = MessageBuilder.createMessage(EMPTY_PAYLOAD, headers.getMessageHeaders());
		this.protocolHandler.handleMessageToClient(this.session, message);

		this.protocolHandler.afterSessionEnded(this.session, CloseStatus.BAD_DATA, this.channel);

		assertEquals("Unexpected events " + publisher.events, 3, publisher.events.size());
		assertEquals(SessionConnectEvent.class, publisher.events.get(0).getClass());
		assertEquals(SessionConnectedEvent.class, publisher.events.get(1).getClass());
		assertEquals(SessionDisconnectEvent.class, publisher.events.get(2).getClass());
	}

	@Test
	public void eventPublicationWithExceptions() {

		ApplicationEventPublisher publisher = new ApplicationEventPublisher() {

			@Override
			public void publishEvent(ApplicationEvent event) {
				throw new IllegalStateException();
			}
		};

		UserSessionRegistry registry = new DefaultUserSessionRegistry();
		this.protocolHandler.setUserSessionRegistry(registry);
		this.protocolHandler.setApplicationEventPublisher(publisher);
		this.protocolHandler.afterSessionStarted(this.session, this.channel);

		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.CONNECT);
		Message<byte[]> message = MessageBuilder.createMessage(EMPTY_PAYLOAD, headers.getMessageHeaders());
		TextMessage textMessage = new TextMessage(new StompEncoder().encode(message));
		this.protocolHandler.handleMessageFromClient(this.session, textMessage, this.channel);

		verify(this.channel).send(this.messageCaptor.capture());
		Message<?> actual = this.messageCaptor.getValue();
		assertNotNull(actual);
		assertEquals(StompCommand.CONNECT, StompHeaderAccessor.wrap(actual).getCommand());
		reset(this.channel);

		headers = StompHeaderAccessor.create(StompCommand.CONNECTED);
		message = MessageBuilder.createMessage(EMPTY_PAYLOAD, headers.getMessageHeaders());
		this.protocolHandler.handleMessageToClient(this.session, message);

		assertEquals(1, this.session.getSentMessages().size());
		textMessage = (TextMessage) this.session.getSentMessages().get(0);
		assertEquals("CONNECTED\n" + "user-name:joe\n" + "\n" + "\u0000", textMessage.getPayload());

		this.protocolHandler.afterSessionEnded(this.session, CloseStatus.BAD_DATA, this.channel);

		verify(this.channel).send(this.messageCaptor.capture());
		actual = this.messageCaptor.getValue();
		assertNotNull(actual);
		assertEquals(StompCommand.DISCONNECT, StompHeaderAccessor.wrap(actual).getCommand());
	}

	@Test
	public void handleMessageToClientUserDestination() {

		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.MESSAGE);
		headers.setMessageId("mess0");
		headers.setSubscriptionId("sub0");
		headers.setDestination("/queue/foo-user123");
		headers.setHeader(StompHeaderAccessor.ORIGINAL_DESTINATION, "/user/queue/foo");
		Message<byte[]> message = MessageBuilder.createMessage(EMPTY_PAYLOAD, headers.getMessageHeaders());
		this.protocolHandler.handleMessageToClient(this.session, message);

		assertEquals(1, this.session.getSentMessages().size());
		WebSocketMessage<?> textMessage = this.session.getSentMessages().get(0);
		assertTrue(((String) textMessage.getPayload()).contains("destination:/user/queue/foo\n"));
	}

	@Test
	public void handleMessageFromClient() {

		TextMessage textMessage = StompTextMessageBuilder.create(StompCommand.CONNECT).headers(
				"login:guest", "passcode:guest", "accept-version:1.1,1.0", "heart-beat:10000,10000").build();

		this.protocolHandler.afterSessionStarted(this.session, this.channel);
		this.protocolHandler.handleMessageFromClient(this.session, textMessage, this.channel);

		verify(this.channel).send(this.messageCaptor.capture());
		Message<?> actual = this.messageCaptor.getValue();
		assertNotNull(actual);

		StompHeaderAccessor headers = StompHeaderAccessor.wrap(actual);
		assertEquals(StompCommand.CONNECT, headers.getCommand());
		assertEquals("s1", headers.getSessionId());
		assertNotNull(headers.getSessionAttributes());
		assertEquals("joe", headers.getUser().getName());
		assertEquals("guest", headers.getLogin());
		assertEquals("guest", headers.getPasscode());
		assertArrayEquals(new long[] {10000, 10000}, headers.getHeartbeat());
		assertEquals(new HashSet<>(Arrays.asList("1.1","1.0")), headers.getAcceptVersion());

		assertEquals(0, this.session.getSentMessages().size());
	}

	@Test
	public void handleMessageFromClientInvalidStompCommand() {

		TextMessage textMessage = new TextMessage("FOO\n\n\0");

		this.protocolHandler.afterSessionStarted(this.session, this.channel);
		this.protocolHandler.handleMessageFromClient(this.session, textMessage, this.channel);

		verifyZeroInteractions(this.channel);
		assertEquals(1, this.session.getSentMessages().size());
		TextMessage actual = (TextMessage) this.session.getSentMessages().get(0);
		assertTrue(actual.getPayload().startsWith("ERROR"));
	}


	private static class UniqueUser extends TestPrincipal implements DestinationUserNameProvider {

		private UniqueUser(String name) {
			super(name);
		}

		@Override
		public String getDestinationUserName() {
			return "Me myself and I";
		}
	}

	private static class TestPublisher implements ApplicationEventPublisher {

		private final List<ApplicationEvent> events = new ArrayList<ApplicationEvent>();

		@Override
		public void publishEvent(ApplicationEvent event) {
			events.add(event);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8346.java