error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1147.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1147.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1147.java
text:
```scala
T@@ value = it.next();

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
package org.apache.wicket.extensions.markup.html.form.select;

import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.WildcardCollectionModel;


/**
 * Component that makes it easy to produce a list of SelectOption components
 * 
 * Example markup:
 * 
 * <pre>
 * &lt;code&gt;
 * &lt;wicket:container wicket:id=&quot;selectOptions&quot;&gt;&lt;option wicket:id=&quot;option&quot;&gt;&lt;/option&gt;&lt;/wicket:contaner&gt;
 * &lt;/code&gt;
 * </pre>
 * 
 * @param <T>
 * @author Igor Vaynberg (ivaynberg)
 */
public class SelectOptions<T> extends RepeatingView
{
	private static final long serialVersionUID = 1L;
	private boolean recreateChoices = false;
	private final IOptionRenderer<T> renderer;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param model
	 * @param renderer
	 */
	public SelectOptions(String id, IModel<Collection<? extends T>> model,
		IOptionRenderer<T> renderer)
	{
		super(id, model);
		this.renderer = renderer;
		setRenderBodyOnly(true);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param elements
	 * @param renderer
	 */
	public SelectOptions(String id, Collection<? extends T> elements, IOptionRenderer<T> renderer)
	{
		this(id, new WildcardCollectionModel<T>(elements), renderer);
	}

	/**
	 * Controls whether or not SelectChoice objects are recreated every request
	 * 
	 * @param refresh
	 * @return this for chaining
	 */
	public SelectOptions<T> setRecreateChoices(boolean refresh)
	{
		recreateChoices = refresh;
		return this;
	}

	/**
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected final void onPopulate()
	{
		if (size() == 0 || recreateChoices)
		{
			// populate this repeating view with SelectOption components
			removeAll();

			Collection<? extends T> modelObject = (Collection<? extends T>)getDefaultModelObject();

			if (modelObject != null)
			{
				if (!(modelObject instanceof Collection))
				{
					throw new WicketRuntimeException("Model object " + modelObject +
						" not a collection");
				}

				// iterator over model objects for SelectOption components
				Iterator<? extends T> it = modelObject.iterator();

				while (it.hasNext())
				{
					// we need a container to represent a row in repeater
					WebMarkupContainer row = new WebMarkupContainer(newChildId());
					row.setRenderBodyOnly(true);
					add(row);

					// we add our actual SelectOption component to the row
					Object value = it.next();
					String text = renderer.getDisplayValue(value);
					IModel<T> model = renderer.getModel(value);
					row.add(newOption(text, model));
				}
			}
		}
	}

	/**
	 * Factory method for creating a new <code>SelectOption</code>. Override to add your own
	 * extensions, such as Ajax behaviors.
	 * 
	 * @param text
	 * @param model
	 * @return a {@link SelectOption}
	 */
	protected SelectOption<T> newOption(String text, IModel<T> model)
	{
		return new SimpleSelectOption<T>("option", model, text);
	}

	private static class SimpleSelectOption<V> extends SelectOption<V>
	{
		private static final long serialVersionUID = 1L;

		private final String text;

		/**
		 * @param id
		 * @param model
		 * @param text
		 */
		public SimpleSelectOption(String id, IModel<V> model, String text)
		{
			super(id, model);
			this.text = text;
		}

		@Override
		protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
		{
			replaceComponentTagBody(markupStream, openTag, text);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1147.java