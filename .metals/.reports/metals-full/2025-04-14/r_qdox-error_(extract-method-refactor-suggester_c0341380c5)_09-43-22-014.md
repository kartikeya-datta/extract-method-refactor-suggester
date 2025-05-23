error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15279.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15279.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15279.java
text:
```scala
a@@ssertEquals("Cannot use async request that has completed", ex.getMessage());

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

package org.springframework.web.context.request.async;


import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * A test fixture with a {@link StandardServletAsyncWebRequest}.
 *
 * @author Rossen Stoyanchev
 */
public class StandardServletAsyncWebRequestTests {

	private StandardServletAsyncWebRequest asyncRequest;

	private HttpServletRequest request;

	private MockHttpServletResponse response;

	@Before
	public void setup() {
		this.request = EasyMock.createMock(HttpServletRequest.class);
		this.response = new MockHttpServletResponse();
		this.asyncRequest = new StandardServletAsyncWebRequest(this.request, this.response);
		this.asyncRequest.setTimeout(60*1000L);
	}

	@Test
	public void isAsyncStarted() throws Exception {
		replay(this.request);
		assertEquals("Should be \"false\" before startAsync()", false, this.asyncRequest.isAsyncStarted());
		verify(this.request);

		startAsync();

		reset(this.request);
		expect(this.request.isAsyncStarted()).andReturn(true);
		replay(this.request);

		assertTrue("Should be \"true\" true startAsync()", this.asyncRequest.isAsyncStarted());
		verify(this.request);

		this.asyncRequest.onComplete(new AsyncEvent(null));

		assertFalse("Should be \"false\" after complete()", this.asyncRequest.isAsyncStarted());
	}

	@Test
	public void startAsync() throws Exception {
		AsyncContext asyncContext = EasyMock.createMock(AsyncContext.class);

		reset(this.request);
		expect(this.request.isAsyncSupported()).andReturn(true);
		expect(this.request.startAsync(this.request, this.response)).andStubReturn(asyncContext);
		replay(this.request);

		asyncContext.addListener(this.asyncRequest);
		asyncContext.setTimeout(60*1000);
		replay(asyncContext);

		this.asyncRequest.startAsync();

		verify(this.request);
	}

	@Test
	public void startAsync_notSupported() throws Exception {
		expect(this.request.isAsyncSupported()).andReturn(false);
		replay(this.request);
		try {
			this.asyncRequest.startAsync();
			fail("expected exception");
		}
		catch (IllegalStateException ex) {
			assertThat(ex.getMessage(), containsString("Async support must be enabled"));
		}
	}

	@Test
	public void startAsync_alreadyStarted() throws Exception {
		startAsync();

		reset(this.request);

		expect(this.request.isAsyncSupported()).andReturn(true);
		expect(this.request.isAsyncStarted()).andReturn(true);
		replay(this.request);

		try {
			this.asyncRequest.startAsync();
			fail("expected exception");
		}
		catch (IllegalStateException ex) {
			assertEquals("Async processing already started", ex.getMessage());
		}

		verify(this.request);
	}

	@Test
	public void startAsync_stale() throws Exception {
		expect(this.request.isAsyncSupported()).andReturn(true);
		replay(this.request);
		this.asyncRequest.onComplete(new AsyncEvent(null));
		try {
			this.asyncRequest.startAsync();
			fail("expected exception");
		}
		catch (IllegalStateException ex) {
			assertEquals("Cannot use async request after completion", ex.getMessage());
		}
	}

	@Test
	public void complete_stale() throws Exception {
		this.asyncRequest.onComplete(new AsyncEvent(null));
		this.asyncRequest.complete();

		assertFalse(this.asyncRequest.isAsyncStarted());
		assertTrue(this.asyncRequest.isAsyncCompleted());
	}

	@Test
	public void sendError() throws Exception {
		this.asyncRequest.sendError(HttpStatus.INTERNAL_SERVER_ERROR, "error");
		assertEquals(500, this.response.getStatus());
	}

	@Test
	public void sendError_stale() throws Exception {
		this.asyncRequest.onComplete(new AsyncEvent(null));
		this.asyncRequest.sendError(HttpStatus.INTERNAL_SERVER_ERROR, "error");
		assertEquals(200, this.response.getStatus());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15279.java