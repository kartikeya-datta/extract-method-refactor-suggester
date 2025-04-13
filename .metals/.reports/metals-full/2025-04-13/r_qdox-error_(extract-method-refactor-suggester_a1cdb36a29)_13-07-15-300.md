error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2501.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2501.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2501.java
text:
```scala
u@@pdate.getTarget().add(this);

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
package org.apache.wicket.examples.events;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

/**
 * @author igor
 */
public class DecoupledAjaxUpdatePage extends BasePage
{
	private int counter;

	/**
	 * Construct.
	 */
	public DecoupledAjaxUpdatePage()
	{
		// add a counter label
		add(new CounterLabel("label1"));


		// add another counter label inside a container
		WebMarkupContainer container = new WebMarkupContainer("container");
		add(container);
		container.add(new CounterLabel("label2"));

		// add a form
		Form<?> form = new Form<Void>("form");
		add(form);

		// add the textfield that will update the counter value
		form.add(new TextField<Integer>("counter", new PropertyModel<Integer>(this, "counter"),
			Integer.class).setRequired(true));

		// add button that will broadcast counter update event
		form.add(new AjaxButton("submit")
		{

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				send(getPage(), Broadcast.BREADTH, new CounterUpdate(target));
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
			}

		});
	}

	/**
	 * An event payload that represents a counter update
	 */
	public class CounterUpdate
	{
		private final AjaxRequestTarget target;

		/**
		 * Constructor
		 * 
		 * @param target
		 */
		public CounterUpdate(AjaxRequestTarget target)
		{
			this.target = target;
		}

		/** @return ajax request target */
		public AjaxRequestTarget getTarget()
		{
			return target;
		}
	}

	/**
	 * A label that renders the value of the page's counter variable. Also listens to
	 * {@link CounterUpdate} event and updates itself.
	 * 
	 * @author igor
	 */
	public class CounterLabel extends Label
	{

		/**
		 * Construct.
		 * 
		 * @param id
		 */
		public CounterLabel(String id)
		{
			super(id, new PropertyModel<Integer>(DecoupledAjaxUpdatePage.this, "counter"));
			setOutputMarkupId(true);
		}

		/**
		 * @see org.apache.wicket.Component#onEvent(org.apache.wicket.event.IEvent)
		 */
		@Override
		public void onEvent(IEvent<?> event)
		{
			super.onEvent(event);

			// check if this is a counter update event and if so repaint self
			if (event.getPayload() instanceof CounterUpdate)
			{
				CounterUpdate update = (CounterUpdate)event.getPayload();
				update.getTarget().addComponent(this);
			}
		}

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2501.java