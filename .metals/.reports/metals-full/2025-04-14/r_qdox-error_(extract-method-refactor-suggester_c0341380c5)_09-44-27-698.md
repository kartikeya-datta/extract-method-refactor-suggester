error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10419.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10419.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10419.java
text:
```scala
l@@og.debug("Wicket application with name '" + applicationKey + "' not found.");

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
package org.apache.wicket.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.protocol.http.IRequestLogger;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.http.WebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link ISessionStore} that works with web applications and provides some
 * specific http servlet/ session related functionality.
 * 
 * @author jcompagner
 * @author Eelco Hillenius
 * @author Matej Knopp
 */
public class HttpSessionStore implements ISessionStore
{
	/** log. */
	private static Logger log = LoggerFactory.getLogger(HttpSessionStore.class);

	/** Name of session attribute under which this session is stored */
	public static final String SESSION_ATTRIBUTE_NAME = "session";

	/** */
	private final Set<UnboundListener> unboundListeners = new CopyOnWriteArraySet<UnboundListener>();

	/**
	 * Construct.
	 */
	public HttpSessionStore()
	{
	}

	private String getSessionAttribute()
	{
		return SESSION_ATTRIBUTE_NAME + Application.get().getApplicationKey();
	}

	/**
	 * 
	 * @param request
	 * @return The http servlet request
	 */
	protected final HttpServletRequest getHttpServletRequest(final Request request)
	{
		Object containerRequest = request.getContainerRequest();
		if (containerRequest == null || (containerRequest instanceof HttpServletRequest) == false)
		{
			throw new IllegalArgumentException("Request must be ServletWebRequest");
		}
		return (HttpServletRequest)containerRequest;
	}

	/**
	 * 
	 * @see HttpServletRequest#getSession(boolean)
	 * 
	 * @param request
	 *            A Wicket request object
	 * @param create
	 *            If true, a session will be created if it is not existing yet
	 * @return The HttpSession associated with this request or null if {@code create} is false and
	 *         the {@code request} has no valid session
	 */
	final HttpSession getHttpSession(final Request request, final boolean create)
	{
		return getHttpServletRequest(request).getSession(create);
	}

	/**
	 * @see org.apache.wicket.session.ISessionStore#bind(Request, Session)
	 */
	public final void bind(final Request request, final Session newSession)
	{
		if (getAttribute(request, getSessionAttribute()) != newSession)
		{
			// call template method
			onBind(request, newSession);

			HttpSession httpSession = getHttpSession(request, false);

			if (httpSession != null)
			{
				// register an unbinding listener for cleaning up
				String applicationKey = Application.get().getName();
				httpSession.setAttribute("Wicket:SessionUnbindingListener-" + applicationKey,
					new SessionBindingListener(applicationKey, httpSession.getId()));

				// register the session object itself
				setAttribute(request, getSessionAttribute(), newSession);
			}
		}
	}

	public void flushSession(Request request, Session session)
	{
		if (getAttribute(request, getSessionAttribute()) != session)
		{
			// this session is not yet bound, bind it
			bind(request, session);
		}
		else
		{
			setAttribute(request, getSessionAttribute(), session);
		}
	}

	/**
	 * @see org.apache.wicket.session.ISessionStore#destroy()
	 */
	public void destroy()
	{
	}

	/**
	 * @see org.apache.wicket.session.ISessionStore#getSessionId(org.apache.wicket.request.Request,
	 *      boolean)
	 */
	public String getSessionId(final Request request, final boolean create)
	{
		String id = null;

		HttpSession httpSession = getHttpSession(request, false);
		if (httpSession != null)
		{
			id = httpSession.getId();
		}
		else if (create)
		{
			httpSession = getHttpSession(request, true);
			id = httpSession.getId();

			IRequestLogger logger = Application.get().getRequestLogger();
			if (logger != null)
			{
				logger.sessionCreated(id);
			}
		}
		return id;
	}

	/**
	 * @see org.apache.wicket.session.ISessionStore#invalidate(Request)
	 */
	public final void invalidate(final Request request)
	{
		HttpSession httpSession = getHttpSession(request, false);
		if (httpSession != null)
		{
			// tell the app server the session is no longer valid
			httpSession.invalidate();
		}
	}

	/**
	 * @see org.apache.wicket.session.ISessionStore#lookup(org.apache.wicket.request.Request)
	 */
	public final Session lookup(final Request request)
	{
		String sessionId = getSessionId(request, false);
		if (sessionId != null)
		{
			return (Session)getAttribute(request, getSessionAttribute());
		}
		return null;
	}

	/**
	 * Template method that is called when a session is being bound to the session store. It is
	 * called <strong>before</strong> the session object itself is added to this store (which is
	 * done by calling {@link ISessionStore#setAttribute(Request, String, Serializable)} with key
	 * {@link Session#SESSION_ATTRIBUTE_NAME}.
	 * 
	 * @param request
	 *            The request
	 * @param newSession
	 *            The new session
	 */
	protected void onBind(final Request request, final Session newSession)
	{
	}

	/**
	 * Template method that is called when the session is being detached from the store, which
	 * typically happens when the {@link HttpSession} was invalidated.
	 * 
	 * @param sessionId
	 *            The session id of the session that was invalidated.
	 */
	protected void onUnbind(final String sessionId)
	{
	}

	/**
	 * Gets the prefix for storing variables in the actual session (typically {@link HttpSession})
	 * for this application instance.
	 * 
	 * @param request
	 *            the request
	 * 
	 * @return the prefix for storing variables in the actual session
	 */
	private String getSessionAttributePrefix(final Request request)
	{
		String sessionAttributePrefix = MarkupParser.WICKET;

		if (request instanceof WebRequest)
		{
			sessionAttributePrefix = WebApplication.get().getSessionAttributePrefix(
				(WebRequest)request, null);
		}

		return sessionAttributePrefix;
	}

	/**
	 * @see org.apache.wicket.session.ISessionStore#getAttribute(org.apache.wicket.request.Request,
	 *      java.lang.String)
	 */
	public final Serializable getAttribute(final Request request, final String name)
	{
		HttpSession httpSession = getHttpSession(request, false);
		if (httpSession != null)
		{
			return (Serializable)httpSession.getAttribute(getSessionAttributePrefix(request) + name);
		}
		return null;
	}

	/**
	 * @see org.apache.wicket.session.ISessionStore#getAttributeNames(org.apache.wicket.request.Request)
	 */
	public final List<String> getAttributeNames(final Request request)
	{
		List<String> list = new ArrayList<String>();
		HttpSession httpSession = getHttpSession(request, false);
		if (httpSession != null)
		{
			@SuppressWarnings("unchecked")
			final Enumeration<String> names = httpSession.getAttributeNames();
			final String prefix = getSessionAttributePrefix(request);
			while (names.hasMoreElements())
			{
				final String name = names.nextElement();
				if (name.startsWith(prefix))
				{
					list.add(name.substring(prefix.length()));
				}
			}
		}
		return list;
	}

	/**
	 * @see org.apache.wicket.session.ISessionStore#removeAttribute(org.apache.wicket.request.Request,
	 *      java.lang.String)
	 */
	public final void removeAttribute(final Request request, final String name)
	{
		HttpSession httpSession = getHttpSession(request, false);
		if (httpSession != null)
		{
			String attributeName = getSessionAttributePrefix(request) + name;

			IRequestLogger logger = Application.get().getRequestLogger();
			if (logger != null)
			{
				Object value = httpSession.getAttribute(attributeName);
				if (value != null)
				{
					logger.objectRemoved(value);
				}
			}
			httpSession.removeAttribute(attributeName);
		}
	}

	/**
	 * @see org.apache.wicket.session.ISessionStore#setAttribute(org.apache.wicket.request.Request,
	 *      java.lang.String, java.io.Serializable)
	 */
	public final void setAttribute(final Request request, final String name,
		final Serializable value)
	{
		// ignore call if the session was marked invalid
		HttpSession httpSession = getHttpSession(request, false);
		if (httpSession != null)
		{
			String attributeName = getSessionAttributePrefix(request) + name;
			IRequestLogger logger = Application.get().getRequestLogger();
			if (logger != null)
			{
				if (httpSession.getAttribute(attributeName) == null)
				{
					logger.objectCreated(value);
				}
				else
				{
					logger.objectUpdated(value);
				}
			}
			httpSession.setAttribute(attributeName, value);
		}
	}

	/**
	 * @see org.apache.wicket.session.ISessionStore#registerUnboundListener(org.apache.wicket.session.ISessionStore.UnboundListener)
	 */
	public final void registerUnboundListener(final UnboundListener listener)
	{
		unboundListeners.add(listener);
	}

	/**
	 * @see org.apache.wicket.session.ISessionStore#unregisterUnboundListener(org.apache.wicket.session.ISessionStore.UnboundListener)
	 */
	public final void unregisterUnboundListener(final UnboundListener listener)
	{
		unboundListeners.remove(listener);
	}

	/**
	 * @see org.apache.wicket.session.ISessionStore#getUnboundListener()
	 */
	public final Set<UnboundListener> getUnboundListener()
	{
		return Collections.unmodifiableSet(unboundListeners);
	}

	/**
	 * Reacts on unbinding from the session by cleaning up the session related data.
	 */
	protected static final class SessionBindingListener
		implements
			HttpSessionBindingListener,
			Serializable
	{
		private static final long serialVersionUID = 1L;

		/** The unique key of the application within this web application. */
		private final String applicationKey;

		/** Session id. */
		private final String sessionId;

		/**
		 * Construct.
		 * 
		 * @param applicationKey
		 *            The unique key of the application within this web application
		 * @param sessionId
		 *            The session's id
		 */
		public SessionBindingListener(final String applicationKey, final String sessionId)
		{
			this.applicationKey = applicationKey;
			this.sessionId = sessionId;
		}

		/**
		 * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
		 */
		public void valueBound(final HttpSessionBindingEvent evg)
		{
		}

		/**
		 * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
		 */
		public void valueUnbound(final HttpSessionBindingEvent evt)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Session unbound: " + sessionId);
			}

			Application application = Application.get(applicationKey);
			if (application == null)
			{
				log.error("Wicket application with name '" + applicationKey + "' not found.");
				return;
			}

			ISessionStore sessionStore = application.getSessionStore();
			if (sessionStore != null)
			{
				for (UnboundListener listener : sessionStore.getUnboundListener())
				{
					listener.sessionUnbound(sessionId);
				}
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10419.java