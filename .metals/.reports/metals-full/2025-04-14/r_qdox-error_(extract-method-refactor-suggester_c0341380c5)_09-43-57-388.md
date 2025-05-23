error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2646.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2646.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2646.java
text:
```scala
i@@f (behavior.getStatelessHint(component))

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
package org.apache.wicket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.behavior.InvalidBehaviorIdException;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.util.lang.Args;

/**
 * Manages behaviors in a {@link Component} instance
 * 
 * @author igor
 */
final class Behaviors implements IDetachable
{
	private static final long serialVersionUID = 1L;
	private final Component component;

	public Behaviors(Component component)
	{
		this.component = component;
	}

	public void add(Behavior... behaviors)
	{
		if (behaviors == null)
		{
			throw new IllegalArgumentException("Argument may not be null");
		}

		for (Behavior behavior : behaviors)
		{
			if (behavior == null)
			{
				throw new IllegalArgumentException("Argument may not be null");
			}

			internalAdd(behavior);

			if (!behavior.isTemporary(component))
			{
				component.addStateChange();
			}

			// Give handler the opportunity to bind this component
			behavior.bind(component);
		}
	}

	private void internalAdd(final Behavior behavior)
	{
		component.data_add(behavior);
		if (behavior.isStateless(component))
		{
			getBehaviorId(behavior);
		}
	}

	@SuppressWarnings("unchecked")
	public <M extends Behavior> List<M> getBehaviors(Class<M> type)
	{
		final int len = component.data_length();
		final int start = component.data_start();
		if (len < start)
		{
			return Collections.emptyList();
		}

		List<M> subset = new ArrayList<M>(len);
		for (int i = component.data_start(); i < len; i++)
		{
			Object obj = component.data_get(i);
			if (obj != null && obj instanceof Behavior)
			{
				if (type == null || type.isAssignableFrom(obj.getClass()))
				{
					subset.add((M)obj);
				}
			}
		}
		return Collections.unmodifiableList(subset);
	}


	public void remove(Behavior behavior)
	{
		if (behavior == null)
		{
			throw new IllegalArgumentException("Argument `behavior` cannot be null");
		}

		if (internalRemove(behavior))
		{
			if (!behavior.isTemporary(component))
			{
				component.addStateChange();
			}
			behavior.detach(component);
		}
		else
		{
			throw new IllegalStateException(
				"Tried to remove a behavior that was not added to the component. Behavior: " +
					behavior.toString());
		}
	}

	/**
	 * THIS IS WICKET INTERNAL ONLY. DO NOT USE IT.
	 * 
	 * Traverses all behaviors and calls detachModel() on them. This is needed to cleanup behavior
	 * after render. This method is necessary for {@link AjaxRequestTarget} to be able to cleanup
	 * component's behaviors after header contribution has been done (which is separated from
	 * component render).
	 */
	public final void detach()
	{
		final int len = component.data_length();
		for (int i = component.data_start(); i < len; i++)
		{
			Object obj = component.data_get(i);
			if (obj != null && obj instanceof Behavior)
			{
				final Behavior behavior = (Behavior)obj;

				behavior.detach(component);

				if (behavior.isTemporary(component))
				{
					internalRemove(behavior);
				}
			}
		}
	}

	private boolean internalRemove(final Behavior behavior)
	{
		final int len = component.data_length();
		for (int i = component.data_start(); i < len; i++)
		{
			Object o = component.data_get(i);
			if (o != null && o.equals(behavior))
			{
				component.data_remove(i);
				behavior.unbind(component);

				// remove behavior from behavior-ids
				ArrayList<Behavior> ids = getBehaviorsIdList(false);
				if (ids != null)
				{
					int idx = ids.indexOf(behavior);
					if (idx == ids.size() - 1)
					{
						ids.remove(idx);
					}
					else if (idx >= 0)
					{
						ids.set(idx, null);
					}
					ids.trimToSize();

					if (ids.isEmpty())
					{
						removeBehaviorsIdList();
					}

				}
				return true;
			}
		}
		return false;
	}

	private void removeBehaviorsIdList()
	{
		for (int i = component.data_start(); i < component.data_length(); i++)
		{
			Object obj = component.data_get(i);
			if (obj != null && obj instanceof BehaviorIdList)
			{
				component.data_remove(i);
				return;
			}
		}
	}

	private BehaviorIdList getBehaviorsIdList(boolean createIfNotFound)
	{
		int len = component.data_length();
		for (int i = component.data_start(); i < len; i++)
		{
			Object obj = component.data_get(i);
			if (obj != null && obj instanceof BehaviorIdList)
			{
				return (BehaviorIdList)obj;
			}
		}
		if (createIfNotFound)
		{
			BehaviorIdList list = new BehaviorIdList();
			component.data_add(list);
			return list;
		}
		return null;
	}

	private static class BehaviorIdList extends ArrayList<Behavior>
	{
		private static final long serialVersionUID = 1L;

		public BehaviorIdList()
		{
			super(1);
		}
	}

	public final int getBehaviorId(Behavior behavior)
	{
		Args.notNull(behavior, "behavior");

		boolean found = false;
		for (int i = component.data_start(); i < component.data_length(); i++)
		{
			if (behavior == component.data_get(i))
			{
				found = true;
				break;
			}
		}
		if (!found)
		{
			throw new IllegalStateException(
				"Behavior must be added to component before its id can be generated. Behavior: " +
					behavior + ", Component: " + this);
		}

		ArrayList<Behavior> ids = getBehaviorsIdList(true);

		int id = ids.indexOf(behavior);

		if (id < 0)
		{
			// try to find an unused slot
			for (int i = 0; i < ids.size(); i++)
			{
				if (ids.get(i) == null)
				{
					ids.set(i, behavior);
					id = i;
					break;
				}
			}
		}

		if (id < 0)
		{
			// no unused slots, add to the end
			id = ids.size();
			ids.add(behavior);
			ids.trimToSize();
		}

		return id;
	}

	public final Behavior getBehaviorById(int id)
	{
		Behavior behavior = null;

		ArrayList<Behavior> ids = getBehaviorsIdList(false);
		if (ids != null)
		{
			if (id >= 0 && id < ids.size())
			{
				behavior = ids.get(id);
			}
		}

		if (behavior != null)
		{
			return behavior;
		}
		throw new InvalidBehaviorIdException(component, id);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2646.java