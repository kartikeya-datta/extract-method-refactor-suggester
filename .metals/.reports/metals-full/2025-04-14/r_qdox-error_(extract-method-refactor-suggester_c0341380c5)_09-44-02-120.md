error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2230.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2230.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2230.java
text:
```scala
public b@@oolean sendsSessionCookie() {

/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.web.socket.sockjs.transport;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;

/**
 * Defines SockJS transport types.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public enum TransportType {

	WEBSOCKET("websocket", HttpMethod.GET),

	XHR("xhr", HttpMethod.POST, "cors", "jsessionid", "no_cache"),

	XHR_SEND("xhr_send", HttpMethod.POST, "cors", "jsessionid", "no_cache"),

	JSONP("jsonp", HttpMethod.GET, "jsessionid", "no_cache"),

	JSONP_SEND("jsonp_send", HttpMethod.POST, "jsessionid", "no_cache"),

	XHR_STREAMING("xhr_streaming", HttpMethod.POST, "cors", "jsessionid", "no_cache"),

	EVENT_SOURCE("eventsource", HttpMethod.GET, "jsessionid", "no_cache"),

	HTML_FILE("htmlfile", HttpMethod.GET, "jsessionid", "no_cache");


	private final String value;

	private final HttpMethod httpMethod;

	private final List<String> headerHints;

	private static final Map<String, TransportType> TRANSPORT_TYPES;
	static {
		Map<String, TransportType> transportTypes = new HashMap<String, TransportType>();
		for (TransportType type : values()) {
			transportTypes.put(type.value, type);
		}
		TRANSPORT_TYPES = Collections.unmodifiableMap(transportTypes);
	}


	private TransportType(String value, HttpMethod httpMethod, String... headerHints) {
		this.value = value;
		this.httpMethod = httpMethod;
		this.headerHints = Arrays.asList(headerHints);
	}


	public String value() {
		return this.value;
	}

	/**
	 * The HTTP method for this transport.
	 */
	public HttpMethod getHttpMethod() {
		return this.httpMethod;
	}

	public boolean sendsNoCacheInstruction() {
		return this.headerHints.contains("no_cache");
	}

	public boolean supportsCors() {
		return this.headerHints.contains("cors");
	}

	public boolean setsJsessionId() {
		return this.headerHints.contains("jsessionid");
	}

	public static TransportType fromValue(String value) {
		return TRANSPORT_TYPES.get(value);
	}

	@Override
	public String toString() {
		return this.value;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2230.java