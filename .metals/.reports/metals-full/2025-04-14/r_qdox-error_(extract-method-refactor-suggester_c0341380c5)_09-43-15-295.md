error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2402.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2402.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2402.java
text:
```scala
a@@pplication.setHomePage(CookieValuePersisterTestPage.class);

/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==================================================================== 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain 
 * a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.markup.html.form.persistence;

import java.util.List;

import javax.servlet.http.Cookie;

import junit.framework.TestCase;
import wicket.Page;
import wicket.RequestCycle;
import wicket.markup.html.form.TextField;
import wicket.markup.html.form.persistence.CookieValuePersisterTestPage.TestForm;
import wicket.protocol.http.MockHttpServletRequest;
import wicket.protocol.http.MockHttpServletResponse;
import wicket.protocol.http.MockWebApplication;
import wicket.protocol.http.WebRequest;
import wicket.protocol.http.WebRequestCycle;
import wicket.protocol.http.WebResponse;

/**
 * How to test CookieValuePersister. Problem: CookieValuePersister relies on
 * RequestCycle.get().getApplication() to access application settings.
 * RequestCycle.get() however is valid only during the render process. It get's
 * automatically attached and detached. Thus RequestCycle.get() will be NULL
 * before and after render. Thus CookieValuePersister can not be tested outside
 * the render process. You may think you can subclass CookieValuePersister and
 * provide your own getSettings(). Unfortunately you can't as it is private. One
 * more note: Though CookieValuePersister could probably be re-arranged in a way
 * that RequestCycle.get() is no longer a problem, the next (testing-) problem
 * can not be solved. Cookies are added to the response and based on the servlet
 * api, the response has no means to validate (read) the already added cookies.
 * But the MockResponse does !?!?. The only solution I came up with so far is
 * indirect testing. Create a page with a form and a form component. Enable the
 * component to be persistent. The first run should give you a null value, as no
 * values are currently stored. Than set a new value and submit the form. The
 * component's values will (hopefully) be saved. Refresh the same page and voala
 * the saved values should be automatically loaded.
 * 
 * @author Juergen Donnerstag
 */
public class CookieValuePersisterTest extends TestCase
{
	private MockWebApplication application;

	protected void setUp() throws Exception
	{
		super.setUp();
		application = new MockWebApplication(null);
		application.getRequiredPageSettings().setHomePage(CookieValuePersisterTestPage.class);
		application.setupRequestAndResponse();
		application.processRequestCycle();
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void test1() throws Exception
	{
		// How does the test work: Make sure you have a page, form and form
		// component properly set up (getRelativePath() etc.). See setUp().
		final Page page = application.getLastRenderedPage();

		// Get the form and form component created
		final TestForm form = (TestForm)page.get("form");
		final TextField textField = (TextField)form.get("input");

		// Make sure a valid cycle is available through RequestCycle.get().
		// The RequestCycle's constructor will attach the new cycle to
		// the threadLocal retrieved by RequestCycle.get().
		// Attached to this cycle must be a valid request and response
		final WebRequestCycle cycle = application.createRequestCycle();

		// Just after init, the requests and responses cookie lists must be
		// empty
		assertEquals(0, getRequestCookies(cycle).length);
		assertEquals(0, getResponseCookies(cycle).size());

		// The persister to be used for the tests
		final CookieValuePersister persister = new CookieValuePersister();

		// See comment on CookieValuePersister on how clearing a value with
		// Cookies works. As no cookies in the request, no "delete" cookie
		// will be added to the response.
		persister.clear(textField);
		assertEquals(0, getRequestCookies(cycle).length);
		assertEquals(0, getResponseCookies(cycle).size());

		// Save the input field's value (add it to the response's cookie list)
		persister.save(textField);
		assertEquals(0, getRequestCookies(cycle).length);
		assertEquals(1, getResponseCookies(cycle).size());
		assertEquals("test", ((Cookie)getResponseCookies(cycle).get(0)).getValue());
		assertEquals("form:input", ((Cookie)getResponseCookies(cycle).get(0)).getName());
		assertNull(((Cookie)getResponseCookies(cycle).get(0)).getPath());

		// To clear in the context of cookies means to add a special cookie
		// (maxAge=0) to the response, provided a cookie with
		// the same name has been provided in the request. Thus, no changes in
		// our test case
		persister.clear(textField);
		assertEquals(0, getRequestCookies(cycle).length);
		assertEquals(1, getResponseCookies(cycle).size());
		assertEquals("test", ((Cookie)getResponseCookies(cycle).get(0)).getValue());
		assertEquals("form:input", ((Cookie)getResponseCookies(cycle).get(0)).getName());
		assertNull(((Cookie)getResponseCookies(cycle).get(0)).getPath());

		// Try to load it. Because there is no Cookie matching the textfield's name
		// it remains unchanged
		persister.load(textField);
		assertEquals("test", textField.getModelObjectAsString());

		// Simulate loading a textfield. Initialize textfield with a new
		// (default) value, copy the cookie from respone to request (simulating
		// a browser), than load the textfield from cookie and voala the
		// textfields value should change.
		// save means: add it to the respone
		// load means: take it from request
		assertEquals("test", textField.getModelObjectAsString());
		textField.setModelObject("new text");
		assertEquals("new text", textField.getModelObjectAsString());
		copyCookieFromResponseToRequest(cycle);
		assertEquals(1, getRequestCookies(cycle).length);
		assertEquals(1, getResponseCookies(cycle).size());

		persister.load(textField);
		assertEquals("test", textField.getModelObjectAsString());
		assertEquals(1, getRequestCookies(cycle).length);
		assertEquals(1, getResponseCookies(cycle).size());

		// remove all cookies from mock response
		// Because I'll find the cookie to be removed in the request, the
		// persister will create
		// a "delete" cookie to remove the cookie on the client and add it to
		// the response.
		persister.clear(textField);
		assertEquals(1, getRequestCookies(cycle).length);
		assertEquals(2, getResponseCookies(cycle).size());
		assertEquals("form:input", ((Cookie)getResponseCookies(cycle).get(1)).getName());
		assertEquals(0, ((Cookie)getResponseCookies(cycle).get(1)).getMaxAge());
	}

	private void copyCookieFromResponseToRequest(final RequestCycle cycle)
	{
		((MockHttpServletRequest)((WebRequest)cycle.getRequest()).getHttpServletRequest())
				.addCookie((Cookie)getResponseCookies(cycle).get(0));
	}

	private Cookie[] getRequestCookies(final RequestCycle cycle)
	{
		return ((WebRequest)cycle.getRequest()).getHttpServletRequest().getCookies();
	}

	private List getResponseCookies(final RequestCycle cycle)
	{
		return (List)((MockHttpServletResponse)((WebResponse)cycle.getResponse())
				.getHttpServletResponse()).getCookies();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2402.java