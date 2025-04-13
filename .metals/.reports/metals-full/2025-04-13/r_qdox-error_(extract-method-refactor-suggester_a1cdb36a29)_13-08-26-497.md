error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15969.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15969.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15969.java
text:
```scala
private final A@@jaxEventBehavior ajaxEventBehavior;

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

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * Test page for detach logic.
 * 
 * @author dashorst
 */
public class TestDetachPage extends WebPage
{
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	private int nrPageOnDetachCalls = 0;
	private int nrPageDetachModelCalls = 0;
	private int nrPageDetachModelsCalls = 0;

	private int nrComponentOnDetachCalls = 0;
	private int nrComponentDetachModelCalls = 0;
	private int nrComponentDetachModelsCalls = 0;

	private int nrAjaxBehaviorDetachModelCalls = 0;

	private int nrModelDetachCalls = 0;

	private AjaxEventBehavior ajaxEventBehavior;

	/**
	 * Model for testing detach logic.
	 * 
	 * @author dashorst
	 */
	private class DetachModel implements IModel
	{
		/** for serialization. */
		private static final long serialVersionUID = 1L;

		public Object getObject()
		{
			return "body";
		}

		public void setObject(Object object)
		{
		}

		public void detach()
		{
			nrModelDetachCalls++;
		}
	}

	/**
	 * Construct.
	 */
	public TestDetachPage()
	{
		final Label label = new Label("comp", new DetachModel())
		{
			/** For serialization */
			private static final long serialVersionUID = 1L;

			protected void onDetach()
			{
				nrComponentOnDetachCalls++;
				super.onDetach();
			}

			protected void detachModel()
			{
				nrComponentDetachModelCalls++;
				super.detachModel();
			}

			public void detachModels()
			{
				nrComponentDetachModelsCalls++;
				super.detachModels();
			}
		};
		label.setOutputMarkupId(true);
		ajaxEventBehavior = new AjaxEventBehavior("onclick")
		{
			/** for serialization. */
			private static final long serialVersionUID = 1L;

			public void detach(Component component)
			{
				nrAjaxBehaviorDetachModelCalls++;
				super.detach(component);
			}

			protected void onEvent(AjaxRequestTarget target)
			{
				target.addComponent(label);
			}
		};
		label.add(ajaxEventBehavior);
		add(label);
	}

	protected void onDetach()
	{
		nrPageOnDetachCalls++;
		super.onDetach();
	}

	protected void detachModel()
	{
		nrPageDetachModelCalls++;
		super.detachModel();
	}

	public void detachModels()
	{
		nrPageDetachModelsCalls++;
		super.detachModels();
	}

	/**
	 * @return nrComponentDetachModelCalls
	 */
	public int getNrComponentDetachModelCalls()
	{
		return nrComponentDetachModelCalls;
	}

	/**
	 * @return nrComponentDetachModelsCalls
	 */
	public int getNrComponentDetachModelsCalls()
	{
		return nrComponentDetachModelsCalls;
	}

	/**
	 * @return nrComponentOnDetachCalls
	 */
	public int getNrComponentOnDetachCalls()
	{
		return nrComponentOnDetachCalls;
	}

	/**
	 * @return nrPageDetachModelCalls
	 */
	public int getNrPageDetachModelCalls()
	{
		return nrPageDetachModelCalls;
	}

	/**
	 * @return nrPageDetachModelsCalls
	 */
	public int getNrPageDetachModelsCalls()
	{
		return nrPageDetachModelsCalls;
	}

	/**
	 * @return nrPageOnDetachCalls
	 */
	public int getNrPageOnDetachCalls()
	{
		return nrPageOnDetachCalls;
	}

	/**
	 * @return nrModelDetachCalls
	 */
	public int getNrModelDetachCalls()
	{
		return nrModelDetachCalls;
	}

	/**
	 * @return nrAjaxBehaviorDetachModelCalls
	 */
	public int getNrAjaxBehaviorDetachModelCalls()
	{
		return nrAjaxBehaviorDetachModelCalls;
	}

	/**
	 * @return ajaxEventBehavior
	 */
	public AjaxEventBehavior getAjaxBehavior()
	{
		return ajaxEventBehavior;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15969.java