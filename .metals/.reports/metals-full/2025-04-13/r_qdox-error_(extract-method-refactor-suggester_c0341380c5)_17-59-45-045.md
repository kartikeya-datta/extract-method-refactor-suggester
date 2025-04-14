error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13304.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13304.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13304.java
text:
```scala
public I@@Model getNestedModel()

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
package wicket.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Component;
import wicket.RequestCycle;

/**
 * Model that makes working with detachable models a breeze.
 * LoadableDetachableModel holds a temporary, transient model object, that is set
 * on 'onAttach' by calling abstract method 'load', and that will be reset/ set
 * to null on 'onDetach'.
 *
 * A usage example:
 * <pre>
 * LoadableDetachableModel venueListModel = new LoadableDetachableModel()
 * {
 *   protected Object load()
 *   {
 *      return getVenueDao().findVenues();
 *   }	
 * };
 * </pre>
 *
 * @author Eelco Hillenius
 */
public abstract class LoadableDetachableModel extends AbstractDetachableModel
{
	/** Logger. */
	private static final Log log = LogFactory.getLog(LoadableDetachableModel.class);

	/** temporary, transient object. */
	private transient Object tempModelObject;

	/**
	 * Construct.
	 */
	public LoadableDetachableModel()
	{
		super();
	}

	/**
	 * @see wicket.model.AbstractDetachableModel#onAttach()
	 */
	protected final void onAttach()
	{
		Object loadedObject = load();
		this.setObject(loadedObject);

		if (log.isDebugEnabled())
		{
			log.debug("loaded transient object " + loadedObject + " for " + this +
					", requestCycle " + RequestCycle.get());
		}
	}

	/**
	 * Loads and returns the (temporary) model object.
	 * @return the (temporary) model object
	 */
	protected abstract Object load();

	/**
	 * @see wicket.model.AbstractDetachableModel#onDetach()
	 */
	protected final void onDetach()
	{
		tempModelObject = null;

		if (log.isDebugEnabled())
		{
			log.debug("removed transient object for " + this +
					", requestCycle " + RequestCycle.get());
		}
	}

	/**
	 * @see wicket.model.AbstractDetachableModel#onGetObject(wicket.Component)
	 */
	protected final Object onGetObject(Component component)
	{
		return tempModelObject;
	}

	/**
	 * Sets the object.
	 * @param object the object
	 */
	protected final void setObject(Object object)
	{
		setObject(null, object);
	}

	/**
	 * @see wicket.model.AbstractDetachableModel#onSetObject(wicket.Component, java.lang.Object)
	 */
	protected final void onSetObject(Component component, Object object)
	{
		this.tempModelObject = object;
	}

	/**
	 * @see wicket.model.IModel#getNestedModel()
	 */
	public final IModel getNestedModel()
	{
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13304.java