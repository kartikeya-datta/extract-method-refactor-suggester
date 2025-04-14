error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9481.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9481.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9481.java
text:
```scala
public S@@ession lookup(Request request)

/*
 * $Id: org.eclipse.jdt.ui.prefs 5004 2006-03-17 20:47:08 -0800 (Fri, 17 Mar
 * 2006) eelco12 $ $Revision: 5004 $ $Date: 2006-03-17 20:47:08 -0800 (Fri, 17
 * Mar 2006) $
 * 
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
package wicket.protocol.http;

import java.io.Serializable;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Application;
import wicket.Request;
import wicket.Session;
import wicket.session.ISessionStore;


/**
 * Abstract implementation of {@link ISessionStore} that works with web
 * applications and that provided some speficic http servlet/ session related
 * functionality.
 * 
 * @author jcompagner
 * @author Eelco Hillenius
 */
public abstract class AbstractHttpSessionStore implements ISessionStore
{

	/** log. */
	protected static Log log = LogFactory.getLog(AbstractHttpSessionStore.class);

	/**
	 * Reacts on unbinding from the session by cleaning up the session related
	 * application data.
	 */
	protected static final class SessionBindingListener
			implements
				HttpSessionBindingListener,
				Serializable
	{
		private static final long serialVersionUID = 1L;

		/** Session id. */
		private final String sessionId;

		/** The unique key of the application within this web application. */
		private final String applicationKey;

		/**
		 * Construct.
		 * 
		 * @param applicationKey
		 *            The unique key of the application within this web
		 *            application
		 * @param sessionId
		 *            The session's id
		 */
		public SessionBindingListener(String applicationKey, String sessionId)
		{
			this.applicationKey = applicationKey;
			this.sessionId = sessionId;
		}

		/**
		 * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
		 */
		public void valueBound(HttpSessionBindingEvent evg)
		{
		}

		/**
		 * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
		 */
		public void valueUnbound(HttpSessionBindingEvent evt)
		{
			Application application = Application.get(applicationKey);
			if (application != null)
			{
				application.getSessionStore().unbind(sessionId);
			}
		}
	}

	protected final WebApplication application;

	/**
	 * Construct.
	 */
	public AbstractHttpSessionStore()
	{
		// sanity check
		Application app = Application.get();
		if (!(app instanceof WebApplication))
		{
			throw new IllegalStateException(getClass().getName()
					+ " can only operate in the context of web applications");
		}
		this.application = (WebApplication)app;
	}

	/**
	 * @see wicket.session.ISessionStore#invalidate(Request)
	 */
	public final void invalidate(Request request)
	{
		WebRequest webRequest = toWebRequest(request);
		HttpSession httpSession = getHttpSession(webRequest);
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
	 * @see wicket.session.ISessionStore#getSessionId(wicket.Request)
	 */
	public final String getSessionId(Request request)
	{
		WebRequest webRequest = toWebRequest(request);
		return getHttpSession(webRequest).getId();
	}

	/**
	 * @see wicket.session.ISessionStore#bind(wicket.Request, wicket.Session)
	 */
	public final void bind(Request request, Session newSession)
	{
		WebRequest webRequest = toWebRequest(request);
		HttpSession httpSession = getHttpSession(webRequest);

		// register an unbinding listener for cleaning up
		String applicationKey = application.getApplicationKey();
		httpSession.setAttribute("Wicket:SessionUnbindingListener-" + applicationKey,
				new SessionBindingListener(applicationKey, httpSession.getId()));
		setAttribute(webRequest, Session.SESSION_ATTRIBUTE_NAME, newSession);

		// call template method
		onBind(request, newSession);
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
	public final Session lookup(Request request)
	{
		WebRequest webRequest = toWebRequest(request);
		return (Session)getAttribute(webRequest, Session.SESSION_ATTRIBUTE_NAME);
	}

	/**
	 * Cast {@link Request} to {@link WebRequest}.
	 * 
	 * @param request
	 *            The request to cast
	 * @return The web request
	 */
	protected final WebRequest toWebRequest(Request request)
	{
		if (request == null)
		{
			return null;
		}
		if (!(request instanceof WebRequest))
		{
			throw new IllegalArgumentException(getClass().getName()
					+ " can only work with WebRequests");
		}
		return (WebRequest)request;
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
	 * @return The underlying HttpSession object.
	 */
	protected final HttpSession getHttpSession(WebRequest request)
	{
		// FIXME allow for session-less operation
		HttpSession httpSession = request.getHttpServletRequest().getSession(true);
		return httpSession;
	}

	/**
	 * Template method that is called when a session is being bound to the
	 * session store.
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
	 * the store, which typically happens when the httpsession was invalidated.
	 * 
	 * @param sessionId
	 *            The session id of the session that was invalidated.
	 */
	protected void onUnbind(String sessionId)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9481.java