error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9272.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9272.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9272.java
text:
```scala
public W@@ebSession(final WebApplication application)

/*
 * $Id: WebSession.java 5064 2006-03-21 11:30:05 -0800 (Tue, 21 Mar 2006)
 * jonathanlocke $ $Revision$ $Date: 2006-03-21 11:30:05 -0800 (Tue, 21
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

import wicket.IRequestCycleFactory;
import wicket.RequestCycle;
import wicket.Session;

/**
 * A session subclass for the HTTP protocol.
 * 
 * @author Jonathan Locke
 */
public class WebSession extends Session
{
	/** log. careful, this log is used to trigger profiling too! */
	// private static Log log = LogFactory.getLog(WebSession.class);
	private static final long serialVersionUID = 1L;

	/** The request cycle factory for the session */
	private transient IRequestCycleFactory requestCycleFactory;

	/** True, if session has been invalidated */
	private transient boolean sessionInvalidated = false;

	/**
	 * Constructor
	 * 
	 * @param application
	 *            The application
	 */
	protected WebSession(final WebApplication application)
	{
		super(application);
	}

	/**
	 * Invalidates this session at the end of the current request. If you need
	 * to invalidate the session immediately, you can do this by calling
	 * invalidateNow(), however this will remove all Wicket components from this
	 * session, which means that you will no longer be able to work with them.
	 */
	public void invalidate()
	{
		sessionInvalidated = true;
	}

	/**
	 * Invalidates this session immediately. Calling this method will remove all
	 * Wicket components from this session, which means that you will no longer
	 * be able to work with them.
	 */
	public void invalidateNow()
	{
		getSessionStore().invalidate(RequestCycle.get().getRequest());
	}

	/**
	 * Any attach logic for session subclasses.
	 */
	protected void attach()
	{
	}

	/**
	 * Called on the end of handling a request, when the RequestCycle is about
	 * to be detached from the current thread.
	 * 
	 * @see wicket.Session#detach()
	 */
	protected void detach()
	{
		if (sessionInvalidated)
		{
			invalidateNow();
		}
	}

	/**
	 * @see wicket.Session#getRequestCycleFactory()
	 */
	protected IRequestCycleFactory getRequestCycleFactory()
	{
		if (requestCycleFactory == null)
		{
			this.requestCycleFactory = ((WebApplication)getApplication())
					.getDefaultRequestCycleFactory();
		}

		return this.requestCycleFactory;
	}

	/**
	 * Updates the session, e.g. for replication purposes.
	 */
	protected void update()
	{
		if (sessionInvalidated == false)
		{
			super.update();
		}
	}

	/**
	 * Initializes this session for a request.
	 */
	final void initForRequest()
	{
		// Set the current session
		set(this);

		attach();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9272.java