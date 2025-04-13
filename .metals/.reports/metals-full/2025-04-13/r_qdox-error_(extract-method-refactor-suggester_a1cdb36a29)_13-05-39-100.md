error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12798.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12798.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12798.java
text:
```scala
e@@lse if ((this.originalHeaders != null) && (this.originalHeaders.get(headerName) != null)) {

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

package org.springframework.web.messaging.support;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.messaging.MessageType;


/**
 * A base class for working with message headers in Web, messaging protocols that support
 * the publish-subscribe message pattern. Provides uniform access to specific values
 * common across protocols such as a destination, message type (publish,
 * subscribe/unsubscribe), session id, and others.
 * <p>
 * This class can be used to prepare headers for a new pub-sub message, or to access
 * and/or modify headers of an existing message.
 * <p>
 * Use one of the static factory method in this class, then call getters and setters, and
 * at the end if necessary call {@link #toHeaders()} to obtain the updated headers.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class PubSubHeaderAccesssor {

	protected Log logger = LogFactory.getLog(getClass());

	public static final String DESTINATIONS = "destinations";

	public static final String CONTENT_TYPE = "contentType";

	public static final String MESSAGE_TYPE = "messageType";

	public static final String PROTOCOL_MESSAGE_TYPE = "protocolMessageType";

	public static final String SESSION_ID = "sessionId";

	public static final String SUBSCRIPTION_ID = "subscriptionId";

	public static final String EXTERNAL_SOURCE_HEADERS = "extSourceHeaders";


	private static final Map<String, List<String>> emptyMultiValueMap =
			Collections.unmodifiableMap(new LinkedMultiValueMap<String, String>(0));


	// wrapped read-only message headers
	private final MessageHeaders originalHeaders;

	// header updates
	private final Map<String, Object> headers = new HashMap<String, Object>(4);

	// saved headers from a message from a remote source
	private final Map<String, List<String>> externalSourceHeaders;



	/**
	 * A constructor for creating new message headers.
	 * This constructor is protected. See factory methods in this and sub-classes.
	 */
	protected PubSubHeaderAccesssor(MessageType messageType, Object protocolMessageType,
			Map<String, List<String>> externalSourceHeaders) {

		this.originalHeaders = null;

		Assert.notNull(messageType, "messageType is required");
		this.headers.put(MESSAGE_TYPE, messageType);

		if (protocolMessageType != null) {
			this.headers.put(PROTOCOL_MESSAGE_TYPE, protocolMessageType);
		}

		if (externalSourceHeaders == null) {
			this.externalSourceHeaders = emptyMultiValueMap;
		}
		else {
			this.externalSourceHeaders = Collections.unmodifiableMap(externalSourceHeaders); // TODO: list values must also be read-only
			this.headers.put(EXTERNAL_SOURCE_HEADERS, this.externalSourceHeaders);
		}
	}

	/**
	 * A constructor for accessing and modifying existing message headers. This
	 * constructor is protected. See factory methods in this and sub-classes.
	 */
	@SuppressWarnings("unchecked")
	protected PubSubHeaderAccesssor(Message<?> message) {
		Assert.notNull(message, "message is required");
		this.originalHeaders = message.getHeaders();
		this.externalSourceHeaders = (this.originalHeaders.get(EXTERNAL_SOURCE_HEADERS) != null) ?
				(Map<String, List<String>>) this.originalHeaders.get(EXTERNAL_SOURCE_HEADERS) : emptyMultiValueMap;
	}


	/**
	 * Create {@link PubSubHeaderAccesssor} for a new {@link Message} with
	 * {@link MessageType#MESSAGE}.
	 */
	public static PubSubHeaderAccesssor create() {
		return new PubSubHeaderAccesssor(MessageType.MESSAGE, null, null);
	}

	/**
	 * Create {@link PubSubHeaderAccesssor} for a new {@link Message} of a specific type.
	 */
	public static PubSubHeaderAccesssor create(MessageType messageType) {
		return new PubSubHeaderAccesssor(messageType, null, null);
	}

	/**
	 * Create {@link PubSubHeaderAccesssor} from the headers of an existing message.
	 */
	public static PubSubHeaderAccesssor wrap(Message<?> message) {
		return new PubSubHeaderAccesssor(message);
	}


	/**
	 * Return the original, wrapped headers (i.e. unmodified) or a new Map including any
	 * updates made via setters.
	 */
	public Map<String, Object> toHeaders() {
		if (!isModified()) {
			return this.originalHeaders;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if (this.originalHeaders != null) {
			result.putAll(this.originalHeaders);
		}
		result.putAll(this.headers);
		return result;
	}

	public boolean isModified() {
		return ((this.originalHeaders == null) || !this.headers.isEmpty());
	}

	public MessageType getMessageType() {
		return (MessageType) getHeaderValue(MESSAGE_TYPE);
	}

	private Object getHeaderValue(String headerName) {
		if (this.headers.get(headerName) != null) {
			return this.headers.get(headerName);
		}
		else if (this.originalHeaders.get(headerName) != null) {
			return this.originalHeaders.get(headerName);
		}
		return null;
	}

	protected void setProtocolMessageType(Object protocolMessageType) {
		this.headers.put(PROTOCOL_MESSAGE_TYPE, protocolMessageType);
	}

	protected Object getProtocolMessageType() {
		return getHeaderValue(PROTOCOL_MESSAGE_TYPE);
	}

	public void setDestination(String destination) {
		Assert.notNull(destination, "destination is required");
		this.headers.put(DESTINATIONS, Arrays.asList(destination));
	}

	@SuppressWarnings("unchecked")
	public String getDestination() {
		List<String> destinations = (List<String>) getHeaderValue(DESTINATIONS);
		return CollectionUtils.isEmpty(destinations) ? null : destinations.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<String> getDestinations() {
		List<String> destinations = (List<String>) getHeaderValue(DESTINATIONS);
		return CollectionUtils.isEmpty(destinations) ? null : destinations;
	}

	public void setDestinations(List<String> destinations) {
		Assert.notNull(destinations, "destinations are required");
		this.headers.put(DESTINATIONS, destinations);
	}

	public MediaType getContentType() {
		return (MediaType) getHeaderValue(CONTENT_TYPE);
	}

	public void setContentType(MediaType contentType) {
		Assert.notNull(contentType, "contentType is required");
		this.headers.put(CONTENT_TYPE, contentType);
	}

	public String getSubscriptionId() {
		return (String) getHeaderValue(SUBSCRIPTION_ID);
	}

	public void setSubscriptionId(String subscriptionId) {
		this.headers.put(SUBSCRIPTION_ID, subscriptionId);
	}

	public String getSessionId() {
		return (String) getHeaderValue(SESSION_ID);
	}

	public void setSessionId(String sessionId) {
		this.headers.put(SESSION_ID, sessionId);
	}

	/**
	 * Return a read-only map of headers originating from a message received by the
	 * application from an external source (e.g. from a remote WebSocket endpoint). The
	 * header names and values are exactly as they were, and are protocol specific but may
	 * also be custom application headers if the protocol allows that.
	 */
	public Map<String, List<String>> getExternalSourceHeaders() {
		return this.externalSourceHeaders;
	}

	@Override
	public String toString() {
		return "PubSubHeaders [originalHeaders=" + this.originalHeaders + ", headers="
				+ this.headers + ", externalSourceHeaders=" + this.externalSourceHeaders + "]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12798.java