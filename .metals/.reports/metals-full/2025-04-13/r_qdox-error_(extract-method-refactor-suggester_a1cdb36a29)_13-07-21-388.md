error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9577.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9577.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9577.java
text:
```scala
I@@SessionStore sessionStore = (ISessionStore)sessionIdToSessionStore.remove(sessionId);

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
package wicket;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import wicket.session.ISessionStore;

/**
 * The session facade hides where where the session and session store come from.
 * The main reason for the existance of this class is to enable having a session
 * object that is stored in the session, and a session store object that is NOT
 * stored in the session. It is not a class typically used by framework clients.
 * 
 * @author Eelco Hillenius
 */
public abstract class SessionFacade
{
	/**
	 * Map of session ids to session stores.
	 */
	private Map sessionIdToSessionStore = Collections.synchronizedMap(new HashMap());

	/**
	 * Adds the provided new session to this facade using the provided request.
	 * 
	 * @param request
	 *            The request that triggered making a new sesion
	 * @param newSession
	 *            The new session
	 */
	public abstract void bind(Request request, Session newSession);

	/**
	 * Get the session id for the provided request.
	 * 
	 * @param request
	 *            The request
	 * @return The session id for the provided request
	 */
	public abstract String getSessionId(Request request);

	/**
	 * Retrieves the session for the provided request from this facade.
	 * 
	 * @param request
	 *            The request
	 * @return The session for the provided request.
	 */
	public final Session getSession(Request request)
	{
		if (request == null)
		{
			throw new IllegalArgumentException("request must be not null");
		}

		Session session = lookup(request);

		return session;
	}

	/**
	 * Gets the session store for the given request.
	 * 
	 * @param request
	 *            The request
	 * @return An instance of {@link ISessionStore}
	 */
	public final ISessionStore getSessionStore(Request request)
	{
		String sessionId = getSessionId(request);
		return getSessionStore(sessionId);
	}

	/**
	 * Creates a new instance of {@link ISessionStore} for the session with the
	 * provided id.
	 * 
	 * @param sessionId
	 *            The id of the session to create a new session store for
	 * @return A new session store instance
	 */
	protected abstract ISessionStore newSessionStore(String sessionId);

	/**
	 * Retrieves the session for the provided request from this facade.
	 * 
	 * @param request
	 *            The current request
	 * @return The session for the provided request. The contract is to never
	 *         return null. If it is somehow not possible to retrieve a session
	 *         object for the provided request, implementations should throw an
	 *         {@link IllegalArgumentException}
	 */
	protected abstract Session lookup(Request request);

	/**
	 * Unbinds the session with the provided session id.
	 * <p>
	 * <strong>It is the full responsibility of subclasses of the session facade
	 * to call the unbind method.
	 * </p>
	 * 
	 * @param applicationKey
	 *            The unique key of the application within this web application
	 * @param sessionId
	 *            The id of the session to be unbinded
	 */
	protected final void unbind(String applicationKey, String sessionId)
	{
		onUnbind(applicationKey, sessionId);
		ISessionStore sessionStore = getSessionStore(sessionId);
		sessionStore.destroy();
	}

	/**
	 * Template method that is called when the session with the provided session
	 * id is unbinded.
	 * 
	 * @param applicationKey
	 *            The unique key of the application within this web application
	 * @param sessionId
	 *            The id of the session to be unbinded
	 */
	protected void onUnbind(String applicationKey, String sessionId)
	{
	}

	/**
	 * Gets the session store for the session with the given session id.
	 * 
	 * @param sessionId
	 *            The id of the session
	 * @return The session store
	 */
	private final ISessionStore getSessionStore(String sessionId)
	{
		ISessionStore sessionStore = (ISessionStore)sessionIdToSessionStore.get(sessionId);
		if (sessionStore == null)
		{
			sessionStore = newSessionStore(sessionId);
			if (sessionStore == null)
			{
				throw new IllegalStateException("session facade " + getClass().getName()
						+ " did not produce a session store instance");
			}
			sessionIdToSessionStore.put(sessionId, sessionStore);
		}
		return sessionStore;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9577.java