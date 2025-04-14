error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16288.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16288.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16288.java
text:
```scala
r@@eturn application.getSessionAttributePrefix(request, null);

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
package org.apache.wicket.protocol.http;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.wicket.AccessStackPageMap;
import org.apache.wicket.Application;
import org.apache.wicket.IPageMap;
import org.apache.wicket.Request;
import org.apache.wicket.Session;


/**
 * Default web implementation of {@link org.apache.wicket.session.ISessionStore} that uses the
 * {@link javax.servlet.http.HttpSession} to store its attributes.
 * 
 * @author Eelco Hillenius
 */
public class HttpSessionStore extends AbstractHttpSessionStore
{
	/**
	 * Construct.
	 * 
	 * @param application
	 *            The application to construct this store for
	 */
	public HttpSessionStore(Application application)
	{
		super(application);
	}

	/**
	 * @see org.apache.wicket.session.ISessionStore#createPageMap(java.lang.String)
	 */
	public IPageMap createPageMap(String name)
	{
		return new AccessStackPageMap(name);
	}

	/**
	 * @see org.apache.wicket.session.ISessionStore#getAttribute(org.apache.wicket.Request,
	 *      java.lang.String)
	 */
	public Object getAttribute(Request request, String name)
	{
		WebRequest webRequest = toWebRequest(request);
		HttpSession httpSession = getHttpSession(webRequest);
		if (httpSession != null)
		{
			return httpSession.getAttribute(getSessionAttributePrefix(webRequest) + name);
		}
		return null;
	}

	/**
	 * @see org.apache.wicket.session.ISessionStore#getAttributeNames(Request)
	 */
	public List<String> getAttributeNames(Request request)
	{
		List<String> list = new ArrayList<String>();
		WebRequest webRequest = toWebRequest(request);
		HttpSession httpSession = getHttpSession(webRequest);
		if (httpSession != null)
		{
			@SuppressWarnings("unchecked")
			final Enumeration<String> names = httpSession.getAttributeNames();
			final String prefix = getSessionAttributePrefix(webRequest);
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
	 * @see org.apache.wicket.session.ISessionStore#removeAttribute(Request,java.lang.String)
	 */
	public void removeAttribute(Request request, String name)
	{
		// ignore call if the session was marked invalid
		if (!isSessionValid())
		{
			return;
		}

		WebRequest webRequest = toWebRequest(request);
		HttpSession httpSession = getHttpSession(webRequest);
		if (httpSession != null)
		{
			String attributeName = getSessionAttributePrefix(webRequest) + name;
			IRequestLogger logger = application.getRequestLogger();
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
	 * @see org.apache.wicket.session.ISessionStore#setAttribute(Request,java.lang.String,
	 *      java.lang.Object)
	 */
	public void setAttribute(Request request, String name, Object value)
	{
		// ignore call if the session was marked invalid
		if (!isSessionValid())
		{
			return;
		}

		WebRequest webRequest = toWebRequest(request);
		HttpSession httpSession = getHttpSession(webRequest);
		if (httpSession != null)
		{
			IRequestLogger logger = application.getRequestLogger();
			String attributeName = getSessionAttributePrefix(webRequest) + name;
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
	 * Gets the prefix for storing variables in the actual session (typically {@link HttpSession}
	 * for this application instance.
	 * 
	 * @param request
	 *            the request
	 * 
	 * @return the prefix for storing variables in the actual session
	 */
	private String getSessionAttributePrefix(final WebRequest request)
	{
		return application.getSessionAttributePrefix(request);
	}

	/**
	 * @return Whether the session was marked invalid during this request (afterwards, we shouldn't
	 *         even come here as there is no session)
	 */
	private boolean isSessionValid()
	{
		if (Session.exists())
		{
			Session session = Session.get();
			if (session instanceof WebSession)
			{
				return !((WebSession)session).isSessionInvalidated();
			}
		}
		return true; // we simply don't know, so play safe and rely on
		// servlet container's code to check availability
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16288.java