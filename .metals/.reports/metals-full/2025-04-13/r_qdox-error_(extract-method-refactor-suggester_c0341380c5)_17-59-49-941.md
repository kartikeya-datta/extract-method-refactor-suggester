error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9525.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9525.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9525.java
text:
```scala
F@@ileTypeMap defaultFileTypeMap = FileTypeMap.getDefaultFileTypeMap();

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

package org.springframework.mock.web;

import java.util.Set;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.RequestDispatcher;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.*;

/**
 * @author Juergen Hoeller
 * @author Chris Beams
 * @author Sam Brannen
 * @since 19.02.2006
 */
public class MockServletContextTests {

	private final MockServletContext sc = new MockServletContext("org/springframework/mock");


	@Test
	public void listFiles() {
		Set<String> paths = sc.getResourcePaths("/web");
		assertNotNull(paths);
		assertTrue(paths.contains("/web/MockServletContextTests.class"));
	}

	@Test
	public void listSubdirectories() {
		Set<String> paths = sc.getResourcePaths("/");
		assertNotNull(paths);
		assertTrue(paths.contains("/web/"));
	}

	@Test
	public void listNonDirectory() {
		Set<String> paths = sc.getResourcePaths("/web/MockServletContextTests.class");
		assertNull(paths);
	}

	@Test
	public void listInvalidPath() {
		Set<String> paths = sc.getResourcePaths("/web/invalid");
		assertNull(paths);
	}

	@Test
	public void registerContextAndGetContext() {
		MockServletContext sc2 = new MockServletContext();
		sc.setContextPath("/");
		sc.registerContext("/second", sc2);
		assertSame(sc, sc.getContext("/"));
		assertSame(sc2, sc.getContext("/second"));
	}

	@Test
	public void getMimeType() {
		assertEquals("text/html", sc.getMimeType("test.html"));
		assertEquals("image/gif", sc.getMimeType("test.gif"));
	}

	/**
	 * Introduced to dispel claims in a thread on Stack Overflow:
	 * <a href="http://stackoverflow.com/questions/22986109/testing-spring-managed-servlet">Testing Spring managed servlet</a>
	 */
	@Test
	public void getMimeTypeWithCustomConfiguredType() {
		FileTypeMap defaultFileTypeMap = MimetypesFileTypeMap.getDefaultFileTypeMap();
		assertThat(defaultFileTypeMap, instanceOf(MimetypesFileTypeMap.class));
		MimetypesFileTypeMap mimetypesFileTypeMap = (MimetypesFileTypeMap) defaultFileTypeMap;
		mimetypesFileTypeMap.addMimeTypes("text/enigma    enigma");
		assertEquals("text/enigma", sc.getMimeType("filename.enigma"));
	}

	@Test
	public void servletVersion() {
		assertEquals(3, sc.getMajorVersion());
		assertEquals(0, sc.getMinorVersion());
		sc.setMinorVersion(1);
		assertEquals(1, sc.getMinorVersion());
	}

	@Test
	public void registerAndUnregisterNamedDispatcher() throws Exception {
		final String name = "test-servlet";
		final String url = "/test";

		assertNull(sc.getNamedDispatcher(name));

		sc.registerNamedDispatcher(name, new MockRequestDispatcher(url));
		RequestDispatcher namedDispatcher = sc.getNamedDispatcher(name);
		assertNotNull(namedDispatcher);
		MockHttpServletResponse response = new MockHttpServletResponse();
		namedDispatcher.forward(new MockHttpServletRequest(sc), response);
		assertEquals(url, response.getForwardedUrl());

		sc.unregisterNamedDispatcher(name);
		assertNull(sc.getNamedDispatcher(name));
	}

	@Test
	public void getNamedDispatcherForDefaultServlet() throws Exception {
		final String name = "default";
		RequestDispatcher namedDispatcher = sc.getNamedDispatcher(name);
		assertNotNull(namedDispatcher);

		MockHttpServletResponse response = new MockHttpServletResponse();
		namedDispatcher.forward(new MockHttpServletRequest(sc), response);
		assertEquals(name, response.getForwardedUrl());
	}

	@Test
	public void setDefaultServletName() throws Exception {
		final String originalDefault = "default";
		final String newDefault = "test";
		assertNotNull(sc.getNamedDispatcher(originalDefault));

		sc.setDefaultServletName(newDefault);
		assertEquals(newDefault, sc.getDefaultServletName());
		assertNull(sc.getNamedDispatcher(originalDefault));

		RequestDispatcher namedDispatcher = sc.getNamedDispatcher(newDefault);
		assertNotNull(namedDispatcher);
		MockHttpServletResponse response = new MockHttpServletResponse();
		namedDispatcher.forward(new MockHttpServletRequest(sc), response);
		assertEquals(newDefault, response.getForwardedUrl());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9525.java