error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4267.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4267.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4267.java
text:
```scala
a@@ssertEquals("=m&=m2", mockRequest.getQueryString());

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.protocol.http;

import org.apache.wicket.WicketTestCase;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;


/**
 * Test of WebRequest.
 * 
 * @author Frank Bille (billen)
 */
public class WebRequestTest extends WicketTestCase
{
	/**
	 * Tests passing in an empty parameter.
	 */
	public void testEmptyParam()
	{
		MockHttpServletRequest mockRequest = tester.getServletRequest();
		mockRequest.setRequestToRedirectString("?a=");
		String value = mockRequest.getParameter("a");
		assertEquals("", value);

		mockRequest.setRequestToRedirectString("?a");
		value = mockRequest.getParameter("a");
		assertEquals("", value);
	}

	/**
	 * Test that ajax is true when the ajax header is present in the request
	 */
	public void testIsAjax_1()
	{
		assertWithHeader("Wicket-Ajax", "true", true);
	}

	/**
	 * Test that it also works when there are other "positive" values than true.
	 */
	public void testIsAjax_2()
	{
		assertWithHeader("Wicket-Ajax", "yes", true);
		assertWithHeader("Wicket-Ajax", "1", true);
		assertWithHeader("Wicket-Ajax", "on", true);
		assertWithHeader("Wicket-Ajax", "y", true);
	}

	/**
	 * Test that it's not ajax.
	 */
	public void testIsAjax_3()
	{
		assertWithHeader("dummyheader", "true", false);
		assertWithHeader("Wicket-Ajax", "false", false);
		assertWithHeader("Wicket-Ajax", "0", false);
		assertWithHeader("Wicket-Ajax", "off", false);
		assertWithHeader("Wicket-Ajax", "no", false);
		assertWithHeader("Wicket-Ajax", "Wicket-Ajax", false);
		assertWithHeader("true", "Wicket-Ajax", false);
		assertWithHeader("WicketAjax", "true", false);
		assertWithHeader("wicketajax", "true", false);
	}

	/**
	 * Tests passing in a string array.
	 */
	public void testStringArray()
	{
		MockHttpServletRequest mockRequest = tester.getServletRequest();
		mockRequest.setRequestToRedirectString("?a=1&a=2");
		Object obj = mockRequest.getParameterMap().get("a");
		assertTrue("Expected " + new String[0].getClass() + ", got " + obj.getClass(),
			obj instanceof String[]);
	}

	/**
	 * Tests encoded string.
	 */
	public void testStringEncoding()
	{
		MockHttpServletRequest mockRequest = tester.getServletRequest();
		mockRequest.setRequestToRedirectString("?a=%20");
		String value = mockRequest.getParameter("a");
		assertEquals(" ", value);
	}

	private void assertWithHeader(String header, String value, boolean isAjax)
	{
		MockHttpServletRequest mockRequest = tester.getServletRequest();
		mockRequest.addHeader(header, value);

		WebRequest webRequest = new ServletWebRequest(mockRequest);

		assertEquals(isAjax, webRequest.isAjax());
	}

	/**
	 * Test handling of null parameter values.
	 */
	public void testNullHandling()
	{
		MockHttpServletRequest mockRequest = tester.getServletRequest();
		mockRequest.setParameter("a", null);
		assertNull(mockRequest.getAttribute("a"));
		assertEquals("a=", mockRequest.getQueryString());
	}

	/**
	 * Test handling of null parameter keys.
	 */
	public void testNullHandling2()
	{
		MockHttpServletRequest mockRequest = tester.getServletRequest();
		mockRequest.setRequestToRedirectString("?=m"); // key is encoded as empty string
		assertEquals("=m", mockRequest.getQueryString());
		mockRequest.setParameter(null, "m2"); // force null string
		assertEquals("=m2&=m", mockRequest.getQueryString());

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4267.java