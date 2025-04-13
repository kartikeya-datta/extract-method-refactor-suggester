error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7372.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7372.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7372.java
text:
```scala
l@@ist.addAll(collection);

/*
 * $Id$ $Revision:
 * 1.5 $ $Date$
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
package wicket.markup.html.form.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Simple choice list backed by an ArrayList. This class implements
 * {@link wicket.markup.html.form.model.IChoiceList}so that it is easier to
 * create subclasses and anonymous implementations.
 * 
 * @author Jonathan Locke
 */
public class ChoiceList implements IChoiceList
{
	/** List of choices */
	private final List list;

	/**
	 * Implementation of IChoice for simple objects.
	 * 
	 * @author Jonathan Locke
	 */
	private class Choice implements IChoice
	{
		/** The index of the choice */
		private final int index;

		/** The choice model object */
		private final Object object;

		/**
		 * Constructor
		 * 
		 * @param object
		 *            The object
		 * @param index
		 *            The index of the object in the choice list
		 */
		public Choice(final Object object, final int index)
		{
			this.object = object;
			this.index = index;
		}

		/**
		 * @see wicket.markup.html.form.model.IChoice#getDisplayValue()
		 */
		public String getDisplayValue()
		{
			return object.toString();
		}

		/**
		 * @see wicket.markup.html.form.model.IChoice#getId()
		 */
		public String getId()
		{
			return Integer.toString(index);
		}

		/**
		 * @see wicket.markup.html.form.model.IChoice#getObject()
		 */
		public Object getObject()
		{
			return object;
		}
	}

	/**
	 * Constructor
	 */
	public ChoiceList()
	{
		list = new ArrayList();
	}

	/**
	 * Constructor
	 * 
	 * @param choices
	 *            Choices to add to this list
	 */
	public ChoiceList(final Collection choices)
	{
		this();
		list.addAll(choices);
	}

	/**
	 * Constructor
	 * 
	 * @param initialCapacity
	 *            Initial capacity of list
	 */
	public ChoiceList(final int initialCapacity)
	{
		list = new ArrayList(initialCapacity);
	}

	/**
	 * @param object
	 *            Object to add to list
	 */
	public void add(final Object object)
	{
		attach();
		list.add(object);
	}

	/**
	 * Add all the elements from a collection to this choice list
	 * 
	 * @param collection
	 *            The collection
	 */
	public void addAll(final Collection collection)
	{
		attach();
		list.add(collection);
	}

	/**
	 * @see wicket.model.IDetachable#attach()
	 */
	public void attach()
	{
	}

	/**
	 * @see wicket.markup.html.form.model.IChoiceList#choiceForId(java.lang.String)
	 */
	public IChoice choiceForId(String id)
	{
		return get(Integer.parseInt(id));
	}

	/**
	 * @see wicket.markup.html.form.model.IChoiceList#choiceForObject(java.lang.Object)
	 */
	public IChoice choiceForObject(Object object)
	{
		return get(list.indexOf(object));
	}

	/**
	 * Clears this list
	 */
	public void clear()
	{
		list.clear();
	}

	/**
	 * @see wicket.model.IDetachable#detach()
	 */
	public void detach()
	{
	}

	/**
	 * @see wicket.markup.html.form.model.IChoiceList#get(int)
	 */
	public IChoice get(final int index)
	{
		attach();
		if (index != -1)
		{
			return newChoice(list.get(index), index);
		}
		return null;
	}

	/**
	 * @return Returns the list.
	 */
	public List getList()
	{
		attach();
		return list;
	}

	/**
	 * @see wicket.markup.html.form.model.IChoiceList#size()
	 */
	public int size()
	{
		attach();
		return list.size();
	}

	/**
	 * IChoice factory method
	 * 
	 * @param object
	 *            Choice object
	 * @param index
	 *            Index of choice
	 * @return The IChoice wrapper for the object at the given index
	 */
	protected IChoice newChoice(final Object object, final int index)
	{
		return new Choice(list.get(index), index);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7372.java