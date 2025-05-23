error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18277.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18277.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18277.java
text:
```scala
public S@@ession newSession(Request request, Response resposne)

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
package wicket.markup.html.form.login;

import junit.framework.TestCase;
import wicket.Component;
import wicket.ISessionFactory;
import wicket.Request;
import wicket.Response;
import wicket.RestartResponseAtInterceptPageException;
import wicket.Session;
import wicket.authorization.Action;
import wicket.authorization.IAuthorizationStrategy;
import wicket.protocol.http.WebApplication;
import wicket.protocol.http.WebRequestCycle;
import wicket.protocol.http.WebSession;
import wicket.util.string.Strings;
import wicket.util.tester.WicketTester;


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
	protected void setUp() throws Exception
	{
		application = new WicketTester(new MyMockWebApplication());
	}

	/**
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		application.destroy();
	}

	/**
	 * 
	 */
	public void testClickLink()
	{
		application.setupRequestAndResponse();
		application.processRequestCycle();
		MockLoginPage loginPage = (MockLoginPage)application.getLastRenderedPage();
		assertEquals(((MyMockWebApplication)application.getApplication()).getLoginPage(), loginPage.getClass());

		application.setupRequestAndResponse();
		application.getServletRequest().setRequestToComponent(loginPage.getForm());
		application.getServletRequest().setParameter(loginPage.getTextField().getInputName(),
				"admin");
		application.processRequestCycle();

		// continueToInterceptPage seems to return the same call, causing it to
		// login twice as a result the lastrendered page is null
		assertEquals(application.getApplication().getHomePage(), application.getLastRenderedPage().getClass());

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
		assertEquals(((MyMockWebApplication)application.getApplication()).getLoginPage(), loginPage.getClass());

		// bypass form completely to login but continue to intercept page
		application.setupRequestAndResponse();
		WebRequestCycle requestCycle = application.createRequestCycle();
		assertTrue(((MockLoginPage)application.getLastRenderedPage()).login("admin"));
		application.processRequestCycle(requestCycle);
		assertEquals(application.getApplication().getHomePage(), application.getLastRenderedPage().getClass());

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
	private static class MyMockWebApplication extends WebApplication implements ISessionFactory
	{
		private static final long serialVersionUID = 1L;
		public Class getHomePage()
		{
			return MockHomePage.class;
		}

		protected void init()
		{
			getSecuritySettings().setAuthorizationStrategy(new MyAuthorizationStrategy());
		}

		/**
		 * 
		 * @return Class
		 */
		public Class getLoginPage()
		{
			return MockLoginPage.class;
		}

		/**
		 * 
		 * @see wicket.ISessionFactory#newSession(Request, Response)
		 */
		public Session newSession(Request request, Response response)
		{
			return new MySession(this, request);
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
		 * @see wicket.authorization.IAuthorizationStrategy#isInstantiationAuthorized(java.lang.Class)
		 */
		public boolean isInstantiationAuthorized(Class componentClass)
		{
			if (MockHomePage.class.equals(componentClass)
					&& !((MySession)Session.get()).isLoggedIn())
			{
				throw new RestartResponseAtInterceptPageException(MockLoginPage.class);
			}
			return true;
		}

		/**
		 * @see wicket.authorization.IAuthorizationStrategy#isActionAuthorized(wicket.Component,
		 *      wicket.authorization.Action)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18277.java