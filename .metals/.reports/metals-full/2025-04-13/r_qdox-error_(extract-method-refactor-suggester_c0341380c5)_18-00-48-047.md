error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9020.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9020.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9020.java
text:
```scala
L@@ist<String> headerValues = mockResponse.getHeaders(headerName);

/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.http.server;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.test.MockHttpServletResponse;
import org.springframework.util.FileCopyUtils;

import static org.junit.Assert.*;

/**
 * @author Arjen Poutsma
 */
public class ServletServerHttpResponseTests {

	private ServletServerHttpResponse response;

	private MockHttpServletResponse mockResponse;

	@Before
	public void create() throws Exception {
		mockResponse = new MockHttpServletResponse();
		response = new ServletServerHttpResponse(mockResponse);
	}

	@Test
	public void setStatusCode() throws Exception {
		response.setStatusCode(HttpStatus.NOT_FOUND);
		assertEquals("Invalid status code", 404, mockResponse.getStatus());
	}

	@Test
	public void getHeaders() throws Exception {
		HttpHeaders headers = response.getHeaders();
		String headerName = "MyHeader";
		String headerValue1 = "value1";
		headers.add(headerName, headerValue1);
		String headerValue2 = "value2";
		headers.add(headerName, headerValue2);
		headers.setContentType(new MediaType("text", "plain", Charset.forName("UTF-8")));

		response.close();
		assertTrue("Header not set", mockResponse.getHeaderNames().contains(headerName));
		List headerValues = mockResponse.getHeaders(headerName);
		assertTrue("Header not set", headerValues.contains(headerValue1));
		assertTrue("Header not set", headerValues.contains(headerValue2));
		assertEquals("Invalid Content-Type", "text/plain;charset=UTF-8", mockResponse.getHeader("Content-Type"));
		assertEquals("Invalid Content-Type", "text/plain;charset=UTF-8", mockResponse.getContentType());
		assertEquals("Invalid Content-Type", "UTF-8", mockResponse.getCharacterEncoding());
	}

	@Test
	public void getHeadersFromHttpServletResponse() {

		String headerName = "Access-Control-Allow-Origin";
		String headerValue = "localhost:8080";

		this.mockResponse.addHeader(headerName, headerValue);
		this.response = new ServletServerHttpResponse(this.mockResponse);

		assertEquals(headerValue, this.response.getHeaders().getFirst(headerName));
		assertEquals(Arrays.asList(headerValue), this.response.getHeaders().get(headerName));
	}

	@Test
	public void getBody() throws Exception {
		byte[] content = "Hello World".getBytes("UTF-8");
		FileCopyUtils.copy(content, response.getBody());

		assertArrayEquals("Invalid content written", content, mockResponse.getContentAsByteArray());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9020.java