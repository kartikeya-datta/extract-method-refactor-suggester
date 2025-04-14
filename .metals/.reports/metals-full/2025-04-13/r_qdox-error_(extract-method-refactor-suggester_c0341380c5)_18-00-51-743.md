error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9050.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9050.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9050.java
text:
```scala
i@@f (Application.get().getDebugSettings().getSerializeSessionAttributes())

/*
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package wicket.protocol.http.portlet;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.portlet.PortletSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.AccessStackPageMap;
import wicket.Application;
import wicket.PageMap;
import wicket.Request;
import wicket.Session;
import wicket.WicketRuntimeException;
import wicket.protocol.http.IRequestLogger;
import wicket.protocol.http.WebRequest;
import wicket.session.ISessionStore;
import wicket.util.lang.Bytes;


/**
 * Abstract implementation of {@link ISessionStore} that works with portlets
 * 
 * @author Janne Hietam&auml;ki
 */
public class PortletSessionStore implements ISessionStore
{
	private final static int SCOPE = PortletSession.APPLICATION_SCOPE;

	/** log. */
	protected static Log log = LogFactory.getLog(PortletSessionStore.class);

	protected final PortletApplication application;

	/**
	 * Construct.
	 */
	public PortletSessionStore()
	{
		// sanity check
		Application app = Application.get();
		if (!(app instanceof PortletApplication))
		{
			throw new IllegalStateException(getClass().getName()
					+ " can only operate in the context of portal applications");
		}
		this.application = (PortletApplication)app;
	}

	/**
	 * @see wicket.session.ISessionStore#invalidate(Request)
	 */
	public final void invalidate(Request request)
	{
		WicketPortletRequest webRequest = toPortletRequest(request);
		PortletSession httpSession = getPortletSession(webRequest);
		if (httpSession != null)
		{
			try
			{
				httpSession.invalidate();
			}
			catch (IllegalStateException e)
			{
				// Ignore
			}
		}
	}

	/**
	 * Gets the session id.
	 * 
	 * @param request
	 * @return The session id
	 * @see wicket.session.ISessionStore#getSessionId(wicket.Request)
	 */
	public final String getSessionId(Request request)
	{
		WicketPortletRequest webRequest = toPortletRequest(request);
		return getPortletSession(webRequest).getId();
	}

	/**
	 * @see wicket.session.ISessionStore#bind(wicket.Request, wicket.Session)
	 */
	public final void bind(Request request, Session newSession)
	{
		// call template method
		onBind(request, newSession);

		WicketPortletRequest webRequest = toPortletRequest(request);
		PortletSession httpSession = getPortletSession(webRequest);

		// register the session object itself
		setAttribute(webRequest, Session.SESSION_ATTRIBUTE_NAME, newSession);
	}

	/**
	 * @see wicket.session.ISessionStore#unbind(java.lang.String)
	 */
	public final void unbind(String sessionId)
	{
		application.sessionDestroyed(sessionId);
		onUnbind(sessionId);
	}

	/**
	 * @see wicket.session.ISessionStore#lookup(wicket.Request)
	 */
	public Session lookup(Request request)
	{
		WicketPortletRequest webRequest = toPortletRequest(request);
		return (Session)getAttribute(webRequest, Session.SESSION_ATTRIBUTE_NAME);
	}

	/**
	 * Cast {@link Request} to {@link WebRequest}.
	 * 
	 * @param request
	 *            The request to cast
	 * @return The web request
	 */
	protected final WicketPortletRequest toPortletRequest(Request request)
	{
		if (request == null)
		{
			return null;
		}
		if (!(request instanceof WicketPortletRequest))
		{
			throw new IllegalArgumentException(getClass().getName()
					+ " can only work with WicketPortletRequest");
		}
		return (WicketPortletRequest)request;
	}

	/**
	 * Gets the underlying HttpSession object or null.
	 * <p>
	 * WARNING: it is a bad idea to depend on the http session object directly.
	 * Please use the classes and methods that are exposed by Wicket instead.
	 * Send an email to the mailing list in case it is not clear how to do
	 * things or you think you miss funcionality which causes you to depend on
	 * this directly.
	 * </p>
	 * 
	 * @param request
	 * 
	 * @return The underlying PortletSession object.
	 */
	protected final PortletSession getPortletSession(WicketPortletRequest request)
	{
		PortletSession httpSession = request.getPortletRequest().getPortletSession(true);
		return httpSession;
	}

	/**
	 * Template method that is called when a session is being bound to the
	 * session store. It is called <strong>before</strong> the session object
	 * itself is added to this store (which is done by calling
	 * {@link ISessionStore#setAttribute(Request, String, Object)} with key
	 * {@link Session#SESSION_ATTRIBUTE_NAME}.
	 * 
	 * @param request
	 *            The request
	 * @param newSession
	 *            The new session
	 */
	protected void onBind(Request request, Session newSession)
	{
	}

	/**
	 * Template method that is called when the session is being detached from
	 * the store, which typically happens when the portlet session was
	 * invalidated.
	 * 
	 * @param sessionId
	 *            The session id of the session that was invalidated.
	 */
	protected void onUnbind(String sessionId)
	{
	}

	/**
	 * @see wicket.session.ISessionStore#setAttribute(Request,java.lang.String,
	 *      java.lang.Object)
	 */
	public void setAttribute(Request request, String name, Object value)
	{
		// Do some extra profiling/ debugging. This can be a great help
		// just for testing whether your webbapp will behave when using
		// session replication
		if (log.isDebugEnabled())
		{
			String valueTypeName = (value != null ? value.getClass().getName() : "null");
			try
			{
				final ByteArrayOutputStream out = new ByteArrayOutputStream();
				new ObjectOutputStream(out).writeObject(value);
				log.debug("Stored attribute " + name + "{ " + valueTypeName + "} with size: "
						+ Bytes.bytes(out.size()));
			}
			catch (Exception e)
			{
				throw new WicketRuntimeException(
						"Internal error cloning object. Make sure all dependent objects implement Serializable. Class: "
								+ valueTypeName, e);
			}
		}

		WicketPortletRequest webRequest = toPortletRequest(request);
		PortletSession httpSession = getPortletSession(webRequest);
		if (httpSession != null)
		{
			IRequestLogger logger = application.getRequestLogger();
			String attributeName = getSessionAttributePrefix(webRequest) + name;
			if (logger != null)
			{
				if (httpSession.getAttribute(attributeName, SCOPE) == null)
				{
					logger.objectCreated(value);
				}
				else
				{
					logger.objectUpdated(value);
				}
			}
			httpSession.setAttribute(attributeName, value, SCOPE);
		}
	}

	/**
	 * @see wicket.session.ISessionStore#getAttribute(wicket.Request,
	 *      java.lang.String)
	 */
	public Object getAttribute(Request request, String name)
	{
		WicketPortletRequest webRequest = toPortletRequest(request);
		PortletSession httpSession = getPortletSession(webRequest);
		if (httpSession != null)
		{
			return httpSession.getAttribute(getSessionAttributePrefix(webRequest) + name, SCOPE);
		}
		return null;
	}

	/**
	 * @see wicket.session.ISessionStore#removeAttribute(Request,java.lang.String)
	 */
	public void removeAttribute(Request request, String name)
	{
		WicketPortletRequest webRequest = toPortletRequest(request);
		PortletSession httpSession = getPortletSession(webRequest);
		if (httpSession != null)
		{
			String attributeName = getSessionAttributePrefix(webRequest) + name;
			IRequestLogger logger = application.getRequestLogger();
			if (logger != null)
			{
				Object value = httpSession.getAttribute(attributeName, SCOPE);
				if (value != null)
				{
					logger.objectRemoved(value);
				}
			}
			httpSession.removeAttribute(attributeName, SCOPE);
		}
	}

	/**
	 * @see wicket.session.ISessionStore#getAttributeNames(Request)
	 */
	public List<String> getAttributeNames(Request request)
	{
		List<String> list = new ArrayList<String>();
		WicketPortletRequest webRequest = toPortletRequest(request);
		PortletSession httpSession = getPortletSession(webRequest);
		if (httpSession != null)
		{
			final Enumeration names = httpSession.getAttributeNames();
			final String prefix = getSessionAttributePrefix(webRequest);
			while (names.hasMoreElements())
			{
				final String name = (String)names.nextElement();
				if (name.startsWith(prefix))
				{
					list.add(name.substring(prefix.length()));
				}
			}
		}
		return list;
	}

	/**
	 * Gets the prefix for storing variables in the actual session (typically
	 * {@link PortletSession} for this application instance.
	 * 
	 * @param request
	 *            the request
	 * 
	 * @return the prefix for storing variables in the actual session
	 */
	private String getSessionAttributePrefix(final WicketPortletRequest request)
	{
		return application.getSessionAttributePrefix(request);
	}

	/**
	 * @see wicket.session.ISessionStore#createPageMap(java.lang.String,
	 *      wicket.Session)
	 */
	public PageMap createPageMap(String name, Session session)
	{
		return new AccessStackPageMap(name, session);
	}

	/**
	 * @see wicket.session.ISessionStore#getSessionId(wicket.Request, boolean)
	 */
	public final String getSessionId(Request request, boolean create)
	{
		WicketPortletRequest webRequest = toPortletRequest(request);
		PortletSession httpSession = webRequest.getPortletRequest().getPortletSession(create);
		return (httpSession != null) ? httpSession.getId() : null;
	}

	public void onBeginRequest(Request request)
	{
	}

	public void onEndRequest(Request request)
	{
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9050.java