error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11284.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11284.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11284.java
text:
```scala
p@@ublic class ChoiceFilter<T> extends AbstractFilter

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
package org.apache.wicket.extensions.markup.html.repeater.data.table.filter;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;


/**
 * Filter that can be represented by a drop down list of choices
 * 
 * @see DropDownChoice
 * 
 * @author Igor Vaynberg (ivaynberg)
 * @param <T>
 *            The model object type
 * 
 */
public class ChoiceFilter<T> extends AbstractFilter<T>
{
	private static final long serialVersionUID = 1L;
	private static final IChoiceRenderer defaultRenderer = new ChoiceRenderer();

	private final DropDownChoice<T> choice;

	/**
	 * @param id
	 * @param model
	 * @param form
	 * @param choices
	 * @param autoSubmit
	 */
	public ChoiceFilter(String id, IModel<T> model, FilterForm form,
		IModel<List<? extends T>> choices, boolean autoSubmit)
	{
		this(id, model, form, choices, defaultRenderer, autoSubmit);
	}

	/**
	 * @param id
	 * @param model
	 * @param form
	 * @param choices
	 * @param autoSubmit
	 */
	public ChoiceFilter(String id, IModel<T> model, FilterForm form, List<? extends T> choices,
		boolean autoSubmit)
	{
		this(id, model, form, Model.valueOf(choices), defaultRenderer, autoSubmit);
	}

	/**
	 * @param id
	 * @param model
	 * @param form
	 * @param choices
	 * @param renderer
	 * @param autoSubmit
	 */
	public ChoiceFilter(String id, IModel<T> model, FilterForm form, List<? extends T> choices,
		IChoiceRenderer<T> renderer, boolean autoSubmit)
	{
		this(id, model, form, Model.valueOf(choices), renderer, autoSubmit);
	}


	/**
	 * @param id
	 *            component id
	 * @param model
	 *            model for the drop down choice component
	 * @param form
	 *            filter form this component will be attached to
	 * @param choices
	 *            list of choices, see {@link DropDownChoice}
	 * @param renderer
	 *            choice renderer, see {@link DropDownChoice}
	 * @param autoSubmit
	 *            if true this filter will submit the form on selection change
	 * @see DropDownChoice
	 */
	public ChoiceFilter(String id, IModel<T> model, FilterForm form,
		IModel<List<? extends T>> choices, IChoiceRenderer<T> renderer, boolean autoSubmit)
	{
		super(id, form);

		choice = newDropDownChoice("filter", model, choices, renderer);

		if (autoSubmit)
		{
			choice.add(new AttributeModifier("onchange", true, new Model<String>(
				"this.form.submit();")));
		}
		enableFocusTracking(choice);

		add(choice);
	}

	/**
	 * Factory method for the drop down choice component
	 * 
	 * @param id
	 *            component id
	 * @param model
	 *            component model
	 * @param choices
	 *            choices model
	 * @param renderer
	 *            choice renderer
	 * @return created drop down component
	 */
	protected DropDownChoice<T> newDropDownChoice(String id, IModel<T> model,
		IModel<List<? extends T>> choices, IChoiceRenderer<T> renderer)
	{
		return new DropDownChoice<T>(id, model, choices, renderer);
	}

	/**
	 * @return the DropDownChoice form component created to represent this filter
	 */
	public DropDownChoice<T> getChoice()
	{
		return choice;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11284.java