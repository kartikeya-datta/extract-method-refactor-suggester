error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15103.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15103.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15103.java
text:
```scala
T@@hreadContext.setApplication(application);

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
package org.apache.wicket.protocol.http.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.ThreadContext;
import org.apache.wicket.protocol.http.WebApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This filter can be used to make the Wicket {@link org.apache.wicket.protocol.http.WebSession}
 * instances available to non-wicket servlets.
 * </p>
 * <p>
 * The following example shows how this filter is setup to for a servlet. You can find the example
 * in the wicket-examples project.
 * 
 * <pre>
 *  &lt;!-- The WicketSesionFilter can be used to provide thread local access to servlets/ JSPs/ etc --&gt;
 *  &lt;filter&gt;
 *    &lt;filter-name&gt;WicketSessionFilter&lt;/filter-name&gt;
 *    &lt;filter-class&gt;org.apache.wicket.protocol.http.servlet.WicketSessionFilter&lt;/filter-class&gt;
 *    &lt;init-param&gt;
 *      &lt;param-name&gt;filterName&lt;/param-name&gt;
 *      &lt;!-- expose the session of the input example app --&gt;
 *      &lt;param-value&gt;FormInputApplication&lt;/param-value&gt;
 *    &lt;/init-param&gt;
 *  &lt;/filter&gt;
 * 
 *  &lt;!-- couple the session filter to the helloworld servlet --&gt;
 *  &lt;filter-mapping&gt;
 *    &lt;filter-name&gt;WicketSessionFilter&lt;/filter-name&gt;
 *    &lt;url-pattern&gt;/helloworldservlet/*&lt;/url-pattern&gt;
 *  &lt;/filter-mapping&gt;
 *  ...
 * 
 *  &lt;servlet&gt;
 *    &lt;servlet-name&gt;HelloWorldServlet&lt;/servlet-name&gt;
 *    &lt;servlet-class&gt;org.apache.wicket.examples.HelloWorldServlet&lt;/servlet-class&gt;
 *  &lt;/servlet&gt;
 * 
 *  &lt;servlet-mapping&gt;
 *    &lt;servlet-name&gt;HelloWorldServlet&lt;/servlet-name&gt;
 *    &lt;url-pattern&gt;/helloworldservlet/*&lt;/url-pattern&gt;
 *  &lt;/servlet-mapping&gt;
 * </pre>
 * 
 * After that, you can get to the Wicket session in the usual fashion:
 * 
 * <pre>
 * Session wicketSession = Session.get();
 * </pre>
 * 
 * like the HelloWorldServlet does:
 * 
 * <pre>
 * public class HelloWorldServlet extends HttpServlet
 * {
 * 	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,
 * 		IOException
 * 	{
 * 		res.setContentType(&quot;text/html&quot;);
 * 		PrintWriter out = res.getWriter();
 * 		String message = &quot;Hi. &quot; +
 * 			(Session.exists() ? &quot; I know Wicket session &quot; + Session.get() + &quot;.&quot;
 * 				: &quot; I can't find a Wicket session.&quot;);
 * 		out.println(message);
 * 		out.close();
 * 	}
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author Eelco Hillenius
 */
public class WicketSessionFilter implements Filter
{
	/** log. */
	private static final Logger logger = LoggerFactory.getLogger(WicketSessionFilter.class);

	/** the filter name/ application key. */
	private String filterName;

	/** the session key where the Wicket session should be stored. */
	private String sessionKey;

	/**
	 * Construct.
	 */
	public WicketSessionFilter()
	{
	}

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException
	{
		filterName = filterConfig.getInitParameter("filterName");

		if (filterName == null)
		{
			throw new ServletException(
				"you must provide init parameter 'filterName if you want to use " +
					getClass().getName());
		}

		logger.debug("filterName/application key set to {}", filterName);
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException
	{
		try
		{
			WebApplication application = bindApplication();
			bindSession(request, application);
			chain.doFilter(request, response);
		}
		finally
		{
			cleanupBoundApplicationAndSession();
		}
	}

	private void cleanupBoundApplicationAndSession()
	{
		ThreadContext.detach();
	}

	private void bindSession(ServletRequest request, WebApplication application)
	{
		// find wicket session and bind it to thread

		HttpSession httpSession = ((HttpServletRequest)request).getSession(false);
		Session session = getSession(httpSession, application);
		if (session == null)
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("could not set Wicket session: key " + sessionKey +
					" not found in http session for " +
					((HttpServletRequest)request).getContextPath() + "," + request.getServerName() +
					", or http session does not exist");
			}
		}
		else
		{
			ThreadContext.setSession(session);
		}
	}

	private WebApplication bindApplication()
	{
		// find wicket application and bind it to thread

		WebApplication application = (WebApplication)Application.get(filterName);
		if (application == null)
		{
			throw new IllegalStateException("Could not find wicket application mapped to filter: " +
				filterName +
				". Make sure you set filterName attribute to the name of the wicket filter " +
				"for the wicket application whose session you want to access.");
		}
		application.set();
		return application;
	}

	private Session getSession(HttpSession session, WebApplication application)
	{
		if (session != null)
		{
			if (sessionKey == null)
			{
				sessionKey = application.getSessionAttributePrefix(null, filterName) +
					Session.SESSION_ATTRIBUTE_NAME;

				logger.debug("will use {} as the session key to get the Wicket session", sessionKey);
			}

			return (Session)session.getAttribute(sessionKey);
		}
		return null;
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy()
	{
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15103.java