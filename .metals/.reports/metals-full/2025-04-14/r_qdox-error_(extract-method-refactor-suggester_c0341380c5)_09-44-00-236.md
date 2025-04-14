error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6524.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6524.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6524.java
text:
```scala
r@@esult.expire();

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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.web.context.request.async.DeferredResult.DeferredResultHandler;

/**
 * DeferredResult tests.
 *
 * @author Rossen Stoyanchev
 */
public class DeferredResultTests {

	@Test
	public void setResult() {
		DeferredResultHandler handler = createMock(DeferredResultHandler.class);
		handler.handleResult("hello");
		replay(handler);

		DeferredResult<String> result = new DeferredResult<String>();
		result.setResultHandler(handler);

		assertTrue(result.setResult("hello"));

		verify(handler);
	}

	@Test
	public void setResultTwice() {
		DeferredResultHandler handler = createMock(DeferredResultHandler.class);
		handler.handleResult("hello");
		replay(handler);

		DeferredResult<String> result = new DeferredResult<String>();
		result.setResultHandler(handler);

		assertTrue(result.setResult("hello"));
		assertFalse(result.setResult("hi"));

		verify(handler);
	}

	@Test
	public void setResultWithException() {
		DeferredResultHandler handler = createMock(DeferredResultHandler.class);
		handler.handleResult("hello");
		expectLastCall().andThrow(new IllegalStateException());
		replay(handler);

		DeferredResult<String> result = new DeferredResult<String>();
		result.setResultHandler(handler);

		assertFalse(result.setResult("hello"));

		verify(handler);
	}

	@Test
	public void isSetOrExpired() {
		DeferredResultHandler handler = createMock(DeferredResultHandler.class);
		handler.handleResult("hello");
		replay(handler);

		DeferredResult<String> result = new DeferredResult<String>();
		result.setResultHandler(handler);

		assertFalse(result.isSetOrExpired());

		result.setResult("hello");

		assertTrue(result.isSetOrExpired());

		verify(handler);
	}

	@Test
	public void setExpired() {
		DeferredResult<String> result = new DeferredResult<String>();
		assertFalse(result.isSetOrExpired());

		result.getAndSetExpired();
		assertTrue(result.isSetOrExpired());
		assertFalse(result.setResult("hello"));
	}

	@Test
	public void hasTimeout() {
		assertFalse(new DeferredResult<String>().hasTimeoutResult());
		assertTrue(new DeferredResult<String>(null, "timed out").hasTimeoutResult());
	}

	@Test
	public void applyTimeoutResult() {
		DeferredResultHandler handler = createMock(DeferredResultHandler.class);
		handler.handleResult("timed out");
		replay(handler);

		DeferredResult<String> result = new DeferredResult<String>(null, "timed out");
		result.setResultHandler(handler);

		assertTrue(result.applyTimeoutResult());
		assertFalse("Shouldn't be able to set result after timeout", result.setResult("hello"));

		verify(handler);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6524.java