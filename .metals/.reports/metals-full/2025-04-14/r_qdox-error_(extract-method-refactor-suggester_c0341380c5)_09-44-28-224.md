error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17682.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17682.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[248,2]

error in qdox parser
file content:
```java
offset: 7105
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17682.java
text:
```scala
{ // TODO finalize javadoc

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.protocol.http;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import wicket.ApplicationSettings;
import wicket.Page;
import wicket.protocol.http.HttpApplication;
import wicket.protocol.http.HttpRequest;
import wicket.protocol.http.HttpRequestCycle;
import wicket.protocol.http.HttpResponse;
import wicket.protocol.http.HttpSession;

/**
 * This class provides a mock implementation of a Wicket HTTP based
 * application that can be used for testing. It emulates all of the
 * functionality of an HttpServlet in a controlled, single-threaded
 * environment. It is supported with mock objects for HttpSession,
 * HttpServletRequest, HttpServletResponse and ServletContext.
 * <p>
 * In its most basic usage you can just create a new MockHttpApplication.
 * This should be sufficient to allow you to construct components and
 * pages and so on for testing. To use certain features such as localization
 * you must also call setupRequestAndResponse().
 * <p>
 * The application takes an optional path attribute that defines a directory
 * on the disk which will correspond to the root of the WAR bundle. This
 * can then be used for locating non-application resources.
 * <p>
 * To actually test the processing of a particular page or component
 * you can also call processRequestCycle() to do all the normal work
 * of a Wicket request.
 * <p>
 * Between calling setupRequestAndResponse() and processRequestCycle()
 * you can get hold of any of the objects for initialisation. The
 * servlet request object has some handy convenience methods for
 * initialising the request to invoke certain types of pages and
 * components.
 * <p>
 * After completion of processRequestCycle() you will probably just be
 * testing component states. However, you also have full access to
 * the wicket.response document (or binary data) and result codes via the
 * servlet wicket.response object.
 * <p>
 * IMPORTANT NOTES
 * <ul>
 * <li>This harness is SINGLE THREADED - there is only one global
 * session. For multi-threaded testing you must do integration testing
 * with a full application server.
 * </ul>
 *
 * @author Chris Turner
 */
public class MockHttpApplication extends HttpApplication
{

	/** Serial Version ID */
	private static final long serialVersionUID = 8409647488957949834L;

	/** Mock http servlet session. */
	private final MockHttpSession servletSession;

	/** Mock http servlet request. */
	private final MockHttpServletRequest servletRequest;

	/** Mock http servlet response. */
	private final MockHttpServletResponse servletResponse;

	/** Mock http servlet context. */
	private final MockServletContext context;

	/** application settings. */
	private final ApplicationSettings settings;

	/** session. */
	private HttpSession wicketSession;

	/** request. */
	private HttpRequest wicketRequest;

	/** response. */
	private HttpResponse wicketResponse;

	/** the last rendered page. */
	private Page lastRenderedPage;

	/**
	 * Create the mock http application that can be used for testing.
	 *
	 * @param path The absolute path on disk to the web application contents (e.g. war root) - may be null
	 * @see wicket.protocol.http.MockServletContext
	 */
	public MockHttpApplication(final String path)
	{
		settings = new ApplicationSettings(this);
		context = new MockServletContext(this, path);
		servletSession = new MockHttpSession(context);
		servletRequest = new MockHttpServletRequest(this, servletSession, context);
		servletResponse = new MockHttpServletResponse();
		wicketSession = HttpSession.getSession(this, servletRequest);
	}

	/**
	 * Reset the request and the wicket.response back to a starting state
	 * and recreate the necessary wicket request, wicket.response and
	 * session objects. The request and wicket.response objects can be
	 * accessed and initialised at this point.
	 * @throws IOException
	 */
	public void setupRequestAndResponse() throws IOException
	{
		servletRequest.initialise();
		servletResponse.initialise();
		wicketSession = HttpSession.getSession(this, servletRequest);
		wicketRequest = new HttpRequest(servletRequest);
		wicketResponse = new HttpResponse(servletResponse);
	}

	/**
	 * Create and process the request cycle using the current request
	 * and wicket.response information.
	 *
	 * @throws ServletException If the render cycle fails
	 */
	public void processRequestCycle() throws ServletException
	{
		HttpRequestCycle cycle = new HttpRequestCycle(this, wicketSession, wicketRequest,
				wicketResponse);
		cycle.render();
		lastRenderedPage = cycle.getPage();
	}

	/**
	 * Get the page that was just rendered by the last request cycle
	 * processing.
	 *
	 * @return The last rendered page
	 */
	public Page getLastRenderedPage()
	{
		return lastRenderedPage;
	}

	/**
	 * Get the application settings.
	 *
	 * @return The application settings.
	 */
	public ApplicationSettings getSettings()
	{
		return settings;
	}

	/**
	 * Get the session object so that we can apply configurations to it.
	 *
	 * @return The session object
	 */
	public MockHttpSession getServletSession()
	{
		return servletSession;
	}

	/**
	 * Get the request object so that we can apply configurations to it.
	 *
	 * @return The request object
	 */
	public MockHttpServletRequest getServletRequest()
	{
		return servletRequest;
	}

	/**
	 * Get the wicket.response object so that we can apply configurations to it.
	 *
	 * @return The wicket.response object
	 */
	public MockHttpServletResponse getServletResponse()
	{
		return servletResponse;
	}

	/**
	 * Get the context object so that we can apply configurations to it.
	 * This method always returns an instance of <code>MockServletContext</code>,
	 * so it is fine to cast the result to this class in order to get
	 * access to the set methods.
	 *
	 * @return The servlet context
	 */
	public ServletContext getServletContext()
	{
		return context;
	}

	/**
	 * Get the wicket session.
	 *
	 * @return The wicket session object
	 */
	public HttpSession getWicketSession()
	{
		return wicketSession;
	}

	/**
	 * Get the wicket request object.
	 *
	 * @return The wicket request object
	 */
	public HttpRequest getWicketRequest()
	{
		return wicketRequest;
	}

	/**
	 * Get the wicket wicket.response object.
	 *
	 * @return The wicket wicket.response object
	 */
	public HttpResponse getWicketResponse()
	{
		return wicketResponse;
	}

}@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17682.java