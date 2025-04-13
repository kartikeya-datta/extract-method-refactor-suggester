error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6698.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6698.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6698.java
text:
```scala
d@@oSend(destination, message);

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
package org.springframework.messaging.simp;

import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.core.AbstractMessageSendingTemplate;
import org.springframework.messaging.core.MessagePostProcessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.Assert;


/**
 * A specialization of {@link AbstractMessageSendingTemplate} that adds String-based
 * destinations as a message header.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class SimpMessagingTemplate extends AbstractMessageSendingTemplate<String>
		implements SimpMessageSendingOperations {

	private final MessageChannel messageChannel;

	private String userDestinationPrefix = "/user/";

	private volatile long sendTimeout = -1;


	public SimpMessagingTemplate(MessageChannel messageChannel) {
		Assert.notNull(messageChannel, "messageChannel is required");
		this.messageChannel = messageChannel;
	}


	/**
	 * Configure the prefix to use for destinations targeting a specific user.
	 * <p>The default value is "/user/".
	 * @see org.springframework.messaging.simp.handler.UserDestinationMessageHandler
	 */
	public void setUserDestinationPrefix(String prefix) {
		Assert.notNull(prefix, "userDestinationPrefix is required");
		this.userDestinationPrefix = prefix;
	}

	/**
	 * @return the userDestinationPrefix
	 */
	public String getUserDestinationPrefix() {
		return this.userDestinationPrefix;
	}

	/**
	 * @return the messageChannel
	 */
	public MessageChannel getMessageChannel() {
		return this.messageChannel;
	}

	/**
	 * Specify the timeout value to use for send operations.
	 *
	 * @param sendTimeout the send timeout in milliseconds
	 */
	public void setSendTimeout(long sendTimeout) {
		this.sendTimeout = sendTimeout;
	}

	/**
	 * @return the sendTimeout
	 */
	public long getSendTimeout() {
		return this.sendTimeout;
	}


	@Override
	public void send(Message<?> message) {
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(message);
		String destination = headers.getDestination();
		destination = (destination != null) ? destination : getRequiredDefaultDestination();
		doSend(getRequiredDefaultDestination(), message);
	}

	@Override
	protected void doSend(String destination, Message<?> message) {
		Assert.notNull(destination, "destination is required");

		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(message);
		headers.setDestination(destination);
		headers.setMessageTypeIfNotSet(SimpMessageType.MESSAGE);
		message = MessageBuilder.withPayload(message.getPayload()).setHeaders(headers).build();

		long timeout = this.sendTimeout;
		boolean sent = (timeout >= 0)
				? this.messageChannel.send(message, timeout)
				: this.messageChannel.send(message);

		if (!sent) {
			throw new MessageDeliveryException(message,
					"failed to send message to destination '" + destination + "' within timeout: " + timeout);
		}
	}



	@Override
	public void convertAndSendToUser(String user, String destination, Object payload) throws MessagingException {
		MessagePostProcessor postProcessor = null;
		this.convertAndSendToUser(user, destination, payload, postProcessor);
	}

	@Override
	public void convertAndSendToUser(String user, String destination, Object payload,
			Map<String, Object> headers) throws MessagingException {

		MessagePostProcessor postProcessor = null;
		this.convertAndSendToUser(user, destination, payload, headers, postProcessor);
	}

	@Override
	public void convertAndSendToUser(String user, String destination, Object payload,
			MessagePostProcessor postProcessor) throws MessagingException {

		Map<String, Object> headers = null;
		this.convertAndSendToUser(user, destination, payload, headers, postProcessor);
	}

	@Override
	public void convertAndSendToUser(String user, String destination, Object payload, Map<String, Object> headers,
			MessagePostProcessor postProcessor) throws MessagingException {

		Assert.notNull(user, "user is required");
		super.convertAndSend(this.userDestinationPrefix + user + destination, payload, headers, postProcessor);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6698.java