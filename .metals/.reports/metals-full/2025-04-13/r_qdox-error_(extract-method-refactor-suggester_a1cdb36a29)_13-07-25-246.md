error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14949.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14949.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14949.java
text:
```scala
s@@essionKey = "wicket:" + servletPath + ":" + Session.SESSION_ATTRIBUTE_NAME;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Session;


/**
 * <p>
 * This filter can be used to make the Wicket
 * {@link org.apache.wicket.protocol.http.WebSession} instances available to non-org.apache.wicket
 * servlets.
 * </p>
 * <p>
 * The following example displays how you can make the Wicket session object of
 * application SessionApplication, mapped on <code>/sessiontest/*</code>
 * available for servlet WicketSessionServlet, mapped under
 * <code>/servlet/sessiontest</code>:
 * 
 * <pre>
 *    &lt;filter&gt;
 *      &lt;filter-name&gt;WicketSessionFilter&lt;/filter-name&gt;
 *      &lt;filter-class&gt;org.apache.wicket.protocol.http.servlet.WicketSessionFilter&lt;/filter-class&gt;
 *      &lt;init-param&gt;
 *        &lt;param-name&gt;servletPath&lt;/param-name&gt;
 *        &lt;param-value&gt;sessiontest&lt;/param-value&gt;
 *      &lt;/init-param&gt;
 *    &lt;/filter&gt;
 *   
 *    &lt;filter-mapping&gt;
 *      &lt;filter-name&gt;WicketSessionFilter&lt;/filter-name&gt;
 *      &lt;url-pattern&gt;/servlet/sessiontest&lt;/url-pattern&gt;
 *    &lt;/filter-mapping&gt;
 *   
 *    &lt;servlet&gt;
 *      &lt;servlet-name&gt;SessionApplication&lt;/servlet-name&gt;
 *      &lt;servlet-class&gt;org.apache.wicket.protocol.http.WicketServlet&lt;/servlet-class&gt;
 *      &lt;init-param&gt;
 *        &lt;param-name&gt;applicationClassName&lt;/param-name&gt;
 *        &lt;param-value&gt;session.SessionApplication&lt;/param-value&gt;
 *      &lt;/init-param&gt;
 *      &lt;load-on-startup&gt;1&lt;/load-on-startup&gt;
 *    &lt;/servlet&gt;
 *   
 *    &lt;servlet&gt;
 *      &lt;servlet-name&gt;WicketSessionServlet&lt;/servlet-name&gt;
 *      &lt;servlet-class&gt;session.WicketSessionServlet&lt;/servlet-class&gt;
 *      &lt;load-on-startup&gt;1&lt;/load-on-startup&gt;
 *    &lt;/servlet&gt;
 *   
 *    &lt;servlet-mapping&gt;
 *      &lt;servlet-name&gt;SessionApplication&lt;/servlet-name&gt;
 *      &lt;url-pattern&gt;/sessiontest/*&lt;/url-pattern&gt;
 *    &lt;/servlet-mapping&gt;
 *   
 *    &lt;servlet-mapping&gt;
 *      &lt;servlet-name&gt;WicketSessionServlet&lt;/servlet-name&gt;
 *      &lt;url-pattern&gt;/servlet/sessiontest&lt;/url-pattern&gt;
 *    &lt;/servlet-mapping&gt;
 * </pre>
 * 
 * After that, you can get to the Wicket session in the usual fashion:
 * 
 * <pre>
 * org.apache.wicket.Session wicketSession = org.apache.wicket.Session.get();
 * </pre>
 * 
 * </p>
 * 
 * @author Eelco Hillenius
 */
public class WicketSessionFilter implements Filter
{
	/** log. */
	private static final Log log = LogFactory.getLog(WicketSessionFilter.class);

	/** the servlet path. */
	private String servletPath;

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
		servletPath = filterConfig.getInitParameter("servletPath");

		if (servletPath == null)
		{
			throw new ServletException(
					"you must provide init parameter servlet-path if you want to use "
							+ getClass().getName());
		}

		if (servletPath.charAt(0) != '/')
		{
			servletPath = '/' + servletPath;
		}

		if (log.isDebugEnabled())
		{
			log.debug("servlet path set to " + servletPath);
		}

		sessionKey = "org.apache.wicket:" + servletPath + ":" + Session.SESSION_ATTRIBUTE_NAME;

		if (log.isDebugEnabled())
		{
			log.debug("will use " + sessionKey + " as the session key to get the Wicket session");
		}
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		HttpServletRequest httpServletRequest = ((HttpServletRequest)request);
		HttpSession httpSession = httpServletRequest.getSession(false);
		if (httpSession != null)
		{
			Session session = (Session)httpSession.getAttribute(sessionKey);
			if (session != null)
			{
				// set the session's threadlocal
				Session.set(session);

				if (log.isDebugEnabled())
				{
					log.debug("session " + session + " set as current for "
							+ httpServletRequest.getContextPath() + ","
							+ httpServletRequest.getServerName());
				}
			}
			else
			{
				if (log.isDebugEnabled())
				{
					log.debug("could not set Wicket session: key " + sessionKey
							+ " not found in http session for "
							+ httpServletRequest.getContextPath() + ","
							+ httpServletRequest.getServerName());
				}
			}
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("could not set Wicket session: no http session was created yet for "
						+ httpServletRequest.getContextPath() + ","
						+ httpServletRequest.getServerName());
			}
		}

		// go on with processing
		chain.doFilter(request, response);
		
		// clean up
		Session.unset();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14949.java