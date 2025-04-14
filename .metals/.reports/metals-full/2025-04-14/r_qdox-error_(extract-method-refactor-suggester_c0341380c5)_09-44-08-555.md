error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17194.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17194.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17194.java
text:
```scala
final S@@tring encryptedPassword = crypt.encryptUrlSafe("test");

/*
 * $Id$ $Revision$
 * $Date$
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.examples.panels.signin;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.markup.html.WebPage;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.FormComponent;
import wicket.protocol.http.MockWebApplication;
import wicket.protocol.http.WebRequestCycle;
import wicket.settings.ISecuritySettings;
import wicket.util.crypt.ICrypt;
import wicket.util.crypt.NoCryptFactory;


/**
 * Test cases for Cookie handling
 * 
 * @author Juergen Donnerstag
 */
public class CookieTest extends TestCase
{
	private static Log log = LogFactory.getLog(CookieTest.class);

	private MockWebApplication application;
	private SignInPanel panel;
	private Form form;
	private Cookie cookieUsername;
	private Cookie cookiePassword;
	private Cookie[] cookies;
	private WebPage page;
	private WebRequestCycle cycle;

	/**
	 * Create the test case.
	 * 
	 * @param name
	 *            The test name
	 */
	public CookieTest(String name)
	{
		super(name);
	}

	protected void setUp() throws Exception
	{
		super.setUp();

		application = new MockWebApplication(null);
		application.setHomePage(MockPage.class);
		application.setupRequestAndResponse();

		final ISecuritySettings settings = application.getSecuritySettings();
		settings.setCryptFactory(new NoCryptFactory());

		this.panel = new SignInPanel("panel")
		{
			public boolean signIn(final String username, final String password)
			{
				return true;
			}
		};

		this.panel.setPersistent(true);
		this.form = (Form)panel.get("signInForm");

		final ICrypt crypt = application.getSecuritySettings().getCryptFactory().newCrypt();
		final String encryptedPassword = crypt.encrypt("test");
		assertNotNull(encryptedPassword);
		this.cookieUsername = new Cookie("panel:signInForm:username", "juergen");
		this.cookiePassword = new Cookie("panel:signInForm:password", encryptedPassword);
		this.cookies = new Cookie[] { cookieUsername, cookiePassword };

		application.getServletRequest().setCookies(cookies);

		cycle = new WebRequestCycle(application.getWicketSession(), application.getWicketRequest(),
				application.getWicketResponse());

		this.page = new MockPage(null);
		page.add(this.panel);

		WebRequestCycle cycle = new WebRequestCycle(application.getWicketSession(), application
				.getWicketRequest(), application.getWicketResponse());
	}

	/**
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void testSetCookieOnForm() throws IOException, ServletException
	{
		// initialize
		this.form.loadPersistentFormComponentValues();

		// validate
		FormComponent username = (FormComponent)panel.get("signInForm:username");
		FormComponent password = (FormComponent)panel.get("signInForm:password");

		Assert.assertNotNull(username);
		Assert.assertNotNull(password);

		Assert.assertNotNull(cookieUsername);

		Assert.assertEquals(cookieUsername.getValue(), username.getModelObjectAsString());
		Assert.assertEquals("test", password.getModelObjectAsString());
	}

	/**
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void testPersistCookieWithPersistenceDisabled() throws IOException, ServletException
	{
		// test will call persistFromComponentData(), which is private
		this.panel.setPersistent(false);
		this.form.onFormSubmitted();

		// validate
		Collection cookies = application.getServletResponse().getCookies();
		Iterator iter = cookies.iterator();
		while (iter.hasNext())
		{
			Assert.assertEquals(0, ((Cookie)iter.next()).getMaxAge());
		}
	}

	/**
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void testPersistCookie() throws IOException, ServletException
	{
		panel.setPersistent(true);

		// test will call persistFromComponentData(), which is private
		this.form.onFormSubmitted();

		// validate
		Collection cookies = application.getServletResponse().getCookies();
		Assert.assertEquals(2, cookies.size());
		Iterator iter = cookies.iterator();
		while (iter.hasNext())
		{
			Cookie cookie = (Cookie)iter.next();
			Assert.assertNotNull(page.get(cookie.getName()));
			// Skip "deleted" cookies
			if (page.get(cookie.getName()).getModelObjectAsString() != "")
			{
				Assert.assertEquals(cookie.getValue(), page.get(cookie.getName())
						.getModelObjectAsString());
			}
		}
	}

	/**
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void testRemoveFromPage() throws IOException, ServletException
	{
		panel.setPersistent(true);

		// test
		page.removePersistedFormData(SignInPanel.SignInForm.class, true);

		// validate
		Collection cookieCollection = application.getServletResponse().getCookies();
		// Cookies are remove by setting maxAge == 0
		Assert.assertEquals(2, cookieCollection.size());

		// initialize
		final Cookie cookieUsername = new Cookie("panel:signInForm:username", "juergen");
		final Cookie cookiePassword = new Cookie("panel:signInForm:password", "test");
		final Cookie[] cookies = new Cookie[] { cookieUsername, cookiePassword };

		application.getServletRequest().setCookies(cookies);

		// test
		page.removePersistedFormData(SignInPanel.SignInForm.class, true);

		// validate
		cookieCollection = application.getServletResponse().getCookies();
		Assert.assertEquals(4, cookieCollection.size());
		Iterator iter = cookieCollection.iterator();
		while (iter.hasNext())
		{
			Cookie cookie = (Cookie)iter.next();
			Assert.assertNotNull(page.get(cookie.getName()));
			Assert.assertEquals(cookie.getMaxAge(), 0);
		}
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17194.java