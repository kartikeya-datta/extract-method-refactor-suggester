error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11458.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11458.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11458.java
text:
```scala
public C@@lass< ? extends Page> getHomePage()

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
package org.apache.wicket.markup.html.form.login;

import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.protocol.http.BufferedWebResponse;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;


/**
 * @author marrink
 * 
 */
public class InterceptTest extends TestCase
{
	private WicketTester application;

	/**
	 * Constructor for InterceptTest.
	 * 
	 * @param arg0
	 */
	public InterceptTest(String arg0)
	{
		super(arg0);
	}

	/**
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		application = new WicketTester(new MyMockWebApplication());
	}

	/**
	 * @see TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception
	{
		application.destroy();
	}

	/**
	 * 
	 */
	public void testFormSubmit()
	{
		application = new WicketTester(new MyMockWebApplication()
		{
			@Override
			protected WebResponse newWebResponse(HttpServletResponse response)
			{
				return new BufferedWebResponse(response);
			}
		});
		// same as above but uses different technique to login
		application.setupRequestAndResponse();
		application.processRequestCycle();
		MockLoginPage loginPage = (MockLoginPage)application.getLastRenderedPage();
		assertEquals(((MyMockWebApplication)application.getApplication()).getLoginPage(),
			loginPage.getClass());
		FormTester form = application.newFormTester("form");
		form.setValue("username", "admin");
		form.submit();
		assertEquals(application.getApplication().getHomePage(), application.getLastRenderedPage()
			.getClass());
	}

	/**
	 * 
	 */
	public void testClickLink()
	{
		application.setupRequestAndResponse();
		application.processRequestCycle();
		MockLoginPage loginPage = (MockLoginPage)application.getLastRenderedPage();
		assertEquals(((MyMockWebApplication)application.getApplication()).getLoginPage(),
			loginPage.getClass());

		application.setupRequestAndResponse();
		application.getServletRequest().setRequestToComponent(loginPage.getForm());
		application.getServletRequest().setParameter(loginPage.getTextField().getInputName(),
			"admin");
		application.processRequestCycle();

		assertEquals(application.getApplication().getHomePage(), application.getLastRenderedPage()
			.getClass());

		application.setupRequestAndResponse();
		application.getServletRequest().setRequestToComponent(
			application.getLastRenderedPage().get("link"));
		application.processRequestCycle();
		assertEquals(PageA.class, application.getLastRenderedPage().getClass());
	}

	/**
	 * 
	 */
	public void testClickLink2()
	{
		// same as above but uses different technique to login
		application.setupRequestAndResponse();
		application.processRequestCycle();
		MockLoginPage loginPage = (MockLoginPage)application.getLastRenderedPage();
		assertEquals(((MyMockWebApplication)application.getApplication()).getLoginPage(),
			loginPage.getClass());

		// bypass form completely to login but continue to intercept page
		application.setupRequestAndResponse();
		WebRequestCycle requestCycle = application.createRequestCycle();
		assertTrue(((MockLoginPage)application.getLastRenderedPage()).login("admin"));
		application.processRequestCycle(requestCycle);

		assertEquals(application.getApplication().getHomePage(), application.getLastRenderedPage()
			.getClass());

		application.setupRequestAndResponse();
		application.getServletRequest().setRequestToComponent(
			application.getLastRenderedPage().get("link"));
		application.processRequestCycle();
		assertEquals(PageA.class, application.getLastRenderedPage().getClass());
	}

	/**
	 * 
	 * @author
	 */
	private static class MyMockWebApplication extends WebApplication
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Class< ? extends Page< ? >> getHomePage()
		{
			return MockHomePage.class;
		}

		@Override
		protected void init()
		{
			getSecuritySettings().setAuthorizationStrategy(new MyAuthorizationStrategy());
		}

		/**
		 * 
		 * @return Class
		 */
		public Class< ? extends Page> getLoginPage()
		{
			return MockLoginPage.class;
		}

		/**
		 * 
		 * @see org.apache.wicket.ISessionFactory#newSession(Request, Response)
		 */
		@Override
		public Session newSession(Request request, Response response)
		{
			return new MySession(this, request);
		}

		@Override
		protected WebResponse newWebResponse(HttpServletResponse servletResponse)
		{
			return new WebResponse(servletResponse);
		}

		@Override
		protected void outputDevelopmentModeWarning()
		{
			// Do nothing.
		}

		@Override
		protected ISessionStore newSessionStore()
		{
			// Don't use a filestore, or we spawn lots of threads, which makes things slow.
			return new HttpSessionStore(this);
		}
	}

	/**
	 * 
	 */
	public static class MySession extends WebSession
	{
		private static final long serialVersionUID = 1L;

		private String username;

		/**
		 * @param application
		 * @param request
		 */
		protected MySession(WebApplication application, Request request)
		{
			super(application, request);
		}

		protected final String getUsername()
		{
			return username;
		}

		protected final void setUsername(String username)
		{
			this.username = username;
		}

		/**
		 * 
		 * @return boolean
		 */
		public boolean isLoggedIn()
		{
			return !Strings.isEmpty(username);
		}
	}

	/**
	 * 
	 */
	private static class MyAuthorizationStrategy implements IAuthorizationStrategy
	{
		/**
		 * @see org.apache.wicket.authorization.IAuthorizationStrategy#isInstantiationAuthorized(java.lang.Class)
		 */
		public boolean isInstantiationAuthorized(Class componentClass)
		{
			if (MockHomePage.class.equals(componentClass) &&
				!((MySession)Session.get()).isLoggedIn())
			{
				throw new RestartResponseAtInterceptPageException(MockLoginPage.class);
			}
			return true;
		}

		/**
		 * @see org.apache.wicket.authorization.IAuthorizationStrategy#isActionAuthorized(org.apache.wicket.Component,
		 *      org.apache.wicket.authorization.Action)
		 */
		public boolean isActionAuthorized(Component component, Action action)
		{
			return true;
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11458.java