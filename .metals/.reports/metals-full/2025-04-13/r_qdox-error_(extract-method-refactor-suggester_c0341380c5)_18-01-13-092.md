error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16862.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16862.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16862.java
text:
```scala
p@@ublic interface ISessionStore

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
package org.apache.wicket.ng.session;

import java.io.Serializable;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.wicket.Request;
import org.apache.wicket.ng.Session;


/**
 * The actual store that is used by {@link org.apache.wicket.ng.Session} to store its attributes.
 * <p>
 * This class is intended for internal framework use.
 * 
 * @author Eelco Hillenius
 * @author Johan Compagner
 * @author Matej Knopp
 */
public interface SessionStore
{
	/**
	 * Gets the attribute value with the given name
	 * 
	 * @param request
	 *            the current request
	 * @param name
	 *            The name of the attribute to store
	 * @return The value of the attribute
	 */
	Serializable getAttribute(Request request, final String name);

	/**
	 * @param request
	 *            the current request
	 * 
	 * @return List of attributes for this session
	 */
	Set<String> getAttributeNames(Request request);

	/**
	 * Adds or replaces the attribute with the given name and value.
	 * 
	 * @param request
	 *            the current request
	 * @param name
	 *            the name of the attribute
	 * @param value
	 *            the value of the attribute
	 */
	void setAttribute(Request request, String name, Serializable value);

	/**
	 * Removes the attribute with the given name.
	 * 
	 * @param request
	 *            the current request
	 * @param name
	 *            the name of the attribute to remove
	 */
	void removeAttribute(Request request, String name);

	/**
	 * Invalidates the session.
	 * 
	 * @param request
	 *            the current request
	 */
	void invalidate(Request request);

	/**
	 * Get the session id for the provided request. If create is false and the creation of the
	 * actual session is deferred, this method should return null to reflect it doesn't have one.
	 * 
	 * @param request
	 *            The request
	 * @param create
	 *            Whether to create an actual session (typically an instance of {@link HttpSession})
	 *            when not already done so
	 * @return The session id for the provided request, possibly null if create is false and the
	 *         creation of the actual session was deferred
	 */
	String getSessionId(Request request, boolean create);

	/**
	 * Retrieves the session for the provided request from this facade.
	 * <p>
	 * This method should return null if it is not bound yet, so that Wicket can recognize that it
	 * should create a session and call {@link #bind(Request, Session)} right after that.
	 * </p>
	 * 
	 * @param request
	 *            The current request
	 * @return The session for the provided request or null if the session was not bound
	 */
	Session lookup(Request request);

	/**
	 * Adds the provided new session to this facade using the provided request.
	 * 
	 * @param request
	 *            The request that triggered making a new session
	 * @param newSession
	 *            The new session
	 */
	void bind(Request request, Session newSession);

	/**
	 * Called when the WebApplication is destroyed.
	 */
	void destroy();

	/**
	 * Listener invoked when session is unbound.
	 * 
	 * @author Matej Knopp
	 */
	public interface UnboundListener
	{
		/**
		 * Informs the listener that session with specifid id has been unbound.
		 * 
		 * @param sessionId
		 */
		public void sessionUnbound(String sessionId);
	};

	/**
	 * Registers listener invoked when session is unbound.
	 * 
	 * @param listener
	 */
	public void registerUnboundListener(UnboundListener listener);

	/**
	 * Unregisters listener invoked when session is unbound.
	 * 
	 * @param listener
	 */
	public void unregisterUnboundListener(UnboundListener listener);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16862.java