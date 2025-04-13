error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11947.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11947.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11947.java
text:
```scala
private U@@serSessionResolver userSessionResolver = new SimpleUserSessionResolver();

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
import org.springframework.util.StringUtils;


/**
 *
 * Supports destinations prefixed with "/user/{username}" and resolves them into a
 * destination to which the user is currently subscribed by appending the user session id.
 * For example a destination such as "/user/john/queue/trade-confirmation" would resolve
 * to "/trade-confirmation/i9oqdfzo" if "i9oqdfzo" is the user's session id.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class UserDestinationMessageHandler implements MessageHandler {

	private static final Log logger = LogFactory.getLog(UserDestinationMessageHandler.class);

	private final MessageSendingOperations<String> messagingTemplate;

	private String prefix = "/user/";

	private UserSessionResolver userSessionResolver = new InMemoryUserSessionResolver();


	public UserDestinationMessageHandler(MessageSendingOperations<String> messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	/**
	 * <p>The default prefix is "/user".
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		Assert.hasText(prefix, "prefix is required");
		this.prefix = prefix.endsWith("/") ? prefix : prefix + "/";
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return this.prefix;
	}

	/**
	 * @param userSessionResolver the userSessionResolver to set
	 */
	public void setUserSessionResolver(UserSessionResolver userSessionResolver) {
		this.userSessionResolver = userSessionResolver;
	}

	/**
	 * @return the userSessionResolver
	 */
	public UserSessionResolver getUserSessionResolver() {
		return this.userSessionResolver;
	}

	/**
	 * @return the messagingTemplate
	 */
	public MessageSendingOperations<String> getMessagingTemplate() {
		return this.messagingTemplate;
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {

		if (!shouldHandle(message)) {
			return;
		}

		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(message);
		String destination = headers.getDestination();

		if (logger.isTraceEnabled()) {
			logger.trace("Processing message to destination " + destination);
		}

		UserDestinationParser destinationParser = new UserDestinationParser(destination);
		String user = destinationParser.getUser();

		if (user == null) {
			if (logger.isErrorEnabled()) {
				logger.error("Ignoring message, expected destination \"" + this.prefix
						+ "{userId}/**\": " + destination);
			}
			return;
		}

		for (String sessionId : this.userSessionResolver.resolveUserSessionIds(user)) {

			String targetDestination = destinationParser.getTargetDestination(sessionId);
			headers.setDestination(targetDestination);
			message = MessageBuilder.fromMessage(message).copyHeaders(headers.toMap()).build();

			if (logger.isTraceEnabled()) {
				logger.trace("Sending message to resolved target destination " + targetDestination);
			}
			this.messagingTemplate.send(targetDestination, message);
		}
	}

	protected boolean shouldHandle(Message<?> message) {

		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(message);
		SimpMessageType messageType = headers.getMessageType();
		String destination = headers.getDestination();

		if (!SimpMessageType.MESSAGE.equals(messageType)) {
			return false;
		}

		if (!StringUtils.hasText(destination)) {
			if (logger.isErrorEnabled()) {
				logger.error("Ignoring message, no destination: " + headers);
			}
			return false;
		}
		else if (!destination.startsWith(this.prefix)) {
			return false;
		}

		return true;
	}


	private class UserDestinationParser {

		private final String user;

		private final String targetDestination;


		public UserDestinationParser(String destination) {

			int userStartIndex = prefix.length();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11947.java