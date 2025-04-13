error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4080.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4080.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4080.java
text:
```scala
m@@essage = MessageBuilder.withPayload(message.getPayload()).setHeaders(headers).build();

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

package org.springframework.messaging.simp.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.Assert;


/**
 * Supports destinations prefixed with "/user/{username}", transforms the
 * destination to a unique queue to which the user is subscribed, and then sends
 * the message for further processing.
 *
 * <p>The target destination has the prefix removed and a unique queue suffix,
 * resolved via {@link #setUserQueueSuffixResolver(UserQueueSuffixResolver)}, appended.
 * For example a destination such as "/user/john/queue/trade-confirmation" could
 * be transformed to "/queue/trade-confirmation/i9oqdfzo".
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class UserDestinationMessageHandler implements MessageHandler {

	private static final Log logger = LogFactory.getLog(UserDestinationMessageHandler.class);

	private final MessageSendingOperations<String> messagingTemplate;

	private String destinationPrefix = "/user/";

	private UserQueueSuffixResolver userQueueSuffixResolver = new SimpleUserQueueSuffixResolver();


	/**
	 *
	 * @param messagingTemplate
	 * @param resolver the resolver to use to find queue suffixes for a user
	 */
	public UserDestinationMessageHandler(MessageSendingOperations<String> messagingTemplate,
			UserQueueSuffixResolver userQueueSuffixResolver) {

		Assert.notNull(messagingTemplate, "messagingTemplate is required");
		Assert.notNull(userQueueSuffixResolver, "userQueueSuffixResolver is required");

		this.messagingTemplate = messagingTemplate;
		this.userQueueSuffixResolver = userQueueSuffixResolver;
	}

	/**
	 * <p>The default prefix is "/user".
	 * @param prefix the prefix to set
	 */
	public void setDestinationPrefix(String prefix) {
		Assert.hasText(prefix, "prefix is required");
		this.destinationPrefix = prefix.endsWith("/") ? prefix : prefix + "/";
	}

	/**
	 * @return the prefix
	 */
	public String getDestinationPrefix() {
		return this.destinationPrefix;
	}

	/**
	 * @return the resolver for queue suffixes for a user
	 */
	public UserQueueSuffixResolver getUserQueueSuffixResolver() {
		return this.userQueueSuffixResolver;
	}

	/**
	 * @return the messagingTemplate
	 */
	public MessageSendingOperations<String> getMessagingTemplate() {
		return this.messagingTemplate;
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {

		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(message);
		SimpMessageType messageType = headers.getMessageType();
		String destination = headers.getDestination();

		if (!SimpMessageType.MESSAGE.equals(messageType)) {
			return;
		}

		if (!checkDestination(destination)) {
			return;
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Processing message to destination " + destination);
		}

		UserDestinationParser destinationParser = new UserDestinationParser(destination);
		String user = destinationParser.getUser();

		if (user == null) {
			if (logger.isErrorEnabled()) {
				logger.error("Ignoring message, expected destination pattern \"" + this.destinationPrefix
						+ "{userId}/**\": " + destination);
			}
			return;
		}

		for (String sessionId : this.userQueueSuffixResolver.getUserQueueSuffixes(user)) {

			String targetDestination = destinationParser.getTargetDestination(sessionId);
			headers.setDestination(targetDestination);
			message = MessageBuilder.withPayloadAndHeaders(message.getPayload(), headers).build();

			if (logger.isTraceEnabled()) {
				logger.trace("Sending message to resolved target destination " + targetDestination);
			}
			this.messagingTemplate.send(targetDestination, message);
		}
	}

	private boolean checkDestination(String destination) {
		if (destination != null) {
			if (destination.startsWith(this.destinationPrefix)) {
				return true;
			}
		}
		return false;
	}


	private class UserDestinationParser {

		private final String user;

		private final String targetDestination;


		public UserDestinationParser(String destination) {

			int userStartIndex = destinationPrefix.length();
			int userEndIndex = destination.indexOf('/', userStartIndex);

			if (userEndIndex > 0) {
				this.user = destination.substring(userStartIndex, userEndIndex);
				this.targetDestination = destination.substring(userEndIndex);
			}
			else {
				this.user = null;
				this.targetDestination = null;
			}
		}

		public String getUser() {
			return this.user;
		}

		public String getTargetDestination(String sessionId) {
			return (this.targetDestination != null) ? this.targetDestination + sessionId : null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4080.java