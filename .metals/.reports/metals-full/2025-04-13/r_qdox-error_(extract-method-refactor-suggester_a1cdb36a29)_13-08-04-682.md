error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12493.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12493.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12493.java
text:
```scala
s@@trategy = new PathExtensionContentNegotiationStrategy(Collections.singletonMap("HTML", MediaType.APPLICATION_XHTML_XML));

/*
 * Copyright 2002-2012 the original author or authors.
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
package org.springframework.web.accept;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * A test fixture for PathExtensionContentNegotiationStrategy.
 *
 * @author Rossen Stoyanchev
 * @since 3.2
 */
public class PathExtensionContentNegotiationStrategyTests {

	private NativeWebRequest webRequest;

	private MockHttpServletRequest servletRequest;

	@Before
	public void setup() {
		this.servletRequest = new MockHttpServletRequest();
		this.webRequest = new ServletWebRequest(servletRequest );
	}

	@Test
	public void resolveMediaTypesFromMapping() {
		this.servletRequest.setRequestURI("test.html");
		PathExtensionContentNegotiationStrategy strategy = new PathExtensionContentNegotiationStrategy();

		List<MediaType> mediaTypes = strategy.resolveMediaTypes(this.webRequest);

		assertEquals(Arrays.asList(new MediaType("text", "html")), mediaTypes);

		strategy = new PathExtensionContentNegotiationStrategy(Collections.singletonMap("HTML", "application/xhtml+xml"));
		mediaTypes = strategy.resolveMediaTypes(this.webRequest);

		assertEquals(Arrays.asList(new MediaType("application", "xhtml+xml")), mediaTypes);
	}

	@Test
	public void resolveMediaTypesFromJaf() {
		this.servletRequest.setRequestURI("test.xls");
		PathExtensionContentNegotiationStrategy strategy = new PathExtensionContentNegotiationStrategy();

		List<MediaType> mediaTypes = strategy.resolveMediaTypes(this.webRequest);

		assertEquals(Arrays.asList(new MediaType("application", "vnd.ms-excel")), mediaTypes);
	}

	@Test
	public void getMediaTypeFromFilenameNoJaf() {
		this.servletRequest.setRequestURI("test.xls");
		PathExtensionContentNegotiationStrategy strategy = new PathExtensionContentNegotiationStrategy();
		strategy.setUseJaf(false);

		List<MediaType> mediaTypes = strategy.resolveMediaTypes(this.webRequest);

		assertEquals(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM), mediaTypes);
	}

	// SPR-8678

	@Test
	public void getMediaTypeFilenameWithContextPath() {
		this.servletRequest.setContextPath("/project-1.0.0.M3");
		this.servletRequest.setRequestURI("/project-1.0.0.M3/");
		PathExtensionContentNegotiationStrategy strategy = new PathExtensionContentNegotiationStrategy();

		assertTrue("Context path should be excluded", strategy.resolveMediaTypes(webRequest).isEmpty());

		this.servletRequest.setRequestURI("/project-1.0.0.M3");

		assertTrue("Context path should be excluded", strategy.resolveMediaTypes(webRequest).isEmpty());
	}

	// SPR-9390

	@Test
	public void getMediaTypeFilenameWithEncodedURI() {
		this.servletRequest.setRequestURI("/quo%20vadis%3f.html");
		PathExtensionContentNegotiationStrategy strategy = new PathExtensionContentNegotiationStrategy();

		List<MediaType> result = strategy.resolveMediaTypes(webRequest);

		assertEquals("Invalid content type", Collections.singletonList(new MediaType("text", "html")), result);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12493.java