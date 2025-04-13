error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2800.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2800.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2800.java
text:
```scala
a@@ssertEquals(0, getRequestCookies().size());

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
package org.apache.wicket.util.cookies;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.Cookie;

import org.apache.wicket.Page;
import org.apache.wicket.WicketTestCase;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.cookies.CookieValuePersisterTestPage.TestForm;

/**
 * 
 * @author Juergen Donnerstag
 */
public class CookieUtilsTest extends WicketTestCase
{
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		tester.startPage(CookieValuePersisterTestPage.class);
	}


	/**
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings( { "unchecked" })
	public void test1() throws Exception
	{
		// How does the test work: Make sure you have a page, form and form component properly set
		// up (getRelativePath() etc.). See setUp().
		final Page page = tester.getLastRenderedPage();

		// Get the form and form component created
		final TestForm form = (TestForm)page.get("form");
		final TextField<String> textField = (TextField<String>)form.get("input");

		// Right after init, the requests and responses cookie lists must be empty
		assertEquals(0, getRequestCookies().size());
		assertEquals(0, getResponseCookies().size());

		// Create a persister for the test
		final CookieUtils persister = new CookieUtils();

		// See comment in CookieUtils on how removing a Cookies works. As no cookies in the request,
		// no "delete" cookie will be added to the response.
		persister.remove(textField);
		assertNull(getRequestCookies());
		assertEquals(0, getResponseCookies().size());

		// Save the input field's value (add it to the response's cookie list)
		persister.save(textField);
		assertNull(getRequestCookies());
		assertEquals(1, getResponseCookies().size());
		assertEquals("test", (getResponseCookies().get(0)).getValue());
		assertEquals("form.input", (getResponseCookies().get(0)).getName());
		assertEquals("/WicketTester$DummyWebApplication/WicketTester$DummyWebApplication",
			(getResponseCookies().get(0)).getPath());

		// To remove a cookie means to add a cookie with maxAge=0. Provided a cookie with the same
		// name has been provided in the request. Thus, no changes in our test case
		persister.remove(textField);
		assertNull(getRequestCookies());
		assertEquals(1, getResponseCookies().size());
		assertEquals("test", (getResponseCookies().get(0)).getValue());
		assertEquals("form.input", (getResponseCookies().get(0)).getName());
		assertEquals("/WicketTester$DummyWebApplication/WicketTester$DummyWebApplication",
			(getResponseCookies().get(0)).getPath());

		// Try to load it. Because there is no Cookie matching the textfield's name the model's
		// value remains unchanged
		persister.load(textField);
		assertEquals("test", textField.getDefaultModelObjectAsString());

		// Simulate loading a textfield. Initialize textfield with a new (default) value, copy the
		// cookie from response to request (simulating a browser), than load the textfield from
		// cookie and voala the textfield's value should change.
		// save means: add it to the response
		// load means: take it from request
		assertEquals("test", textField.getDefaultModelObjectAsString());
		textField.setDefaultModelObject("new text");
		assertEquals("new text", textField.getDefaultModelObjectAsString());
		copyCookieFromResponseToRequest();
		assertEquals(1, getRequestCookies().size());
		assertEquals(1, getResponseCookies().size());

		persister.load(textField);
		assertEquals("test", textField.getDefaultModelObjectAsString());
		assertEquals(1, getRequestCookies().size());
		assertEquals(1, getResponseCookies().size());

		// remove all cookies from mock response. Because I'll find the cookie to be removed in the
		// request, the persister will create a "delete" cookie to remove the cookie on the client
		// and add it to the response. The already existing Cookie from the previous test gets
		// removed from response since it is the same.
		persister.remove(textField);
		assertEquals(1, getRequestCookies().size());
		assertEquals(1, getResponseCookies().size());
		assertEquals("form.input", (getResponseCookies().get(0)).getName());
		assertEquals(0, (getResponseCookies().get(0)).getMaxAge());
	}

	private void copyCookieFromResponseToRequest()
	{
		tester.getRequest().addCookie(getResponseCookies().iterator().next());
	}

	private Collection<Cookie> getRequestCookies()
	{
		return tester.getRequest().getCookies();
	}

	private List<Cookie> getResponseCookies()
	{
		return tester.getLastResponse().getCookies();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2800.java