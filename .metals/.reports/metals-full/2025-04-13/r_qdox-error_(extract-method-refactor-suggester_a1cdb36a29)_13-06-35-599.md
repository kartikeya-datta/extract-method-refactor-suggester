error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2939.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2939.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2939.java
text:
```scala
T@@ object = getModelObject();

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Objects;
import org.apache.wicket.util.string.Strings;


/**
 * Component that represents a <code>&lt;select&gt;</code> box. Elements are provided by one or more
 * <code>SelectChoice</code> or <code>SelectOptions</code> components in the hierarchy below the
 * <code>Select</code> component.
 * 
 * Advantages to the standard choice components is that the user has a lot more control over the
 * markup between the &lt;select&gt; tag and its children &lt;option&gt; tags: allowing for such
 * things as &lt;optgroup&gt; tags.
 * 
 * <p>
 * Example HTML:
 * 
 * <pre>
 *    &lt;select wicket:id=&quot;select&quot; multiple=&quot;multiple&quot;&gt;
 *        &lt;wicket:container wicket:id=&quot;options&quot;&gt;
 *            &lt;option wicket:id=&quot;option&quot;&gt;Option Label&lt;/option&gt;
 *        &lt;/wicket:container&gt;
 *    &lt;/select&gt;
 * </pre>
 * 
 * Related Java Code:
 * 
 * <pre>
 * Select select = new Select(&quot;select&quot;, selectionModel);
 * add(select);
 * SelectOptions options = new SelectOptions(&quot;options&quot;, elements, renderer);
 * select.add(options);
 * </pre>
 * 
 * Note that you don't need to add component(s) for the &lt;option&gt; tag - they are created by
 * SelectOptions
 * <p>
 * 
 * @see SelectOption
 * @see SelectOptions
 * 
 * @author Igor Vaynberg
 * @param <T>
 */
public class Select<T> extends FormComponent<T>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that will create a default model collection
	 * 
	 * @param id
	 *            component id
	 */
	public Select(final String id)
	{
		super(id);
	}

	/**
	 * @param id
	 * @param model
	 * @see WebMarkupContainer#WebMarkupContainer(String, IModel)
	 */
	public Select(final String id, final IModel<T> model)
	{
		super(id, model);
	}

	@Override
	protected void convertInput()
	{
		boolean supportsMultiple = getModelObject() instanceof Collection;

		/*
		 * the input contains an array of full path of the selected option components unless nothing
		 * was selected in which case the input contains null
		 */
		String[] paths = getInputAsArray();

		if ((paths == null) || (paths.length == 0))
		{
			setConvertedInput(null);
			return;
		}

		if (!supportsMultiple && (paths.length > 1))
		{
			throw new WicketRuntimeException(
				"The model of Select component [" +
					getPath() +
					"] is not of type java.util.Collection, but more then one SelectOption component has been selected. Either remove the multiple attribute from the select tag or make the model of the Select component a collection");
		}

		List<Object> converted = new ArrayList<Object>(paths.length);

		/*
		 * if the input is null we do not need to do anything since the model collection has already
		 * been cleared
		 */
		for (String path : paths)
		{
			if (!Strings.isEmpty(path))
			{
				/*
				 * option component path sans select component path = relative path from group to
				 * option since we know the option is child of select
				 */
				path = path.substring(getPath().length() + 1);

				// retrieve the selected option component
				SelectOption<?> option = (SelectOption<?>)get(path);

				if (option == null)
				{
					throw new WicketRuntimeException(
						"submitted http post value [" +
							Arrays.toString(paths) +
							"] for SelectOption component [" +
							getPath() +
							"] contains an illegal relative path element [" +
							path +
							"] which does not point to an SelectOption component. Due to this the Select component cannot resolve the selected SelectOption component pointed to by the illegal value. A possible reason is that component hierarchy changed between rendering and form submission.");
				}
				converted.add(option.getDefaultModelObject());
			}
		}

		if (converted.isEmpty())
		{
			setConvertedInput(null);
		}
		else if (!supportsMultiple)
		{
			setConvertedInput((T)converted.get(0));
		}
		else
		{
			setConvertedInput((T)converted);
		}
	}


	/**
	 * @see FormComponent#updateModel()
	 */
	@Override
	public void updateModel()
	{
		Object object = getDefaultModelObject();
		boolean supportsMultiple = object instanceof Collection;

		Object converted = getConvertedInput();
		/*
		 * update the model
		 */
		if (supportsMultiple)
		{
			Collection<?> modelCollection = (Collection<?>)object;
			modelChanging();
			modelCollection.clear();
			if (converted != null)
			{
				modelCollection.addAll((Collection)converted);
			}
			modelChanged();
			// force notify of model update via setObject()
			setDefaultModelObject(modelCollection);
		}
		else
		{
			setDefaultModelObject(converted);
		}
	}

	/**
	 * Checks if the specified option is selected
	 * 
	 * @param option
	 * @return true if the option is selected, false otherwise
	 */
	boolean isSelected(final SelectOption<?> option)
	{
		// if the raw input is specified use that, otherwise use model
		if (hasRawInput())
		{
			String[] paths = getInputAsArray();
			if ((paths != null) && (paths.length > 0))
			{
				for (String path : paths)
				{
					if (path.equals(option.getPath()))
					{
						return true;
					}
				}
				return false;
			}
		}

		return compareModels(getDefaultModelObject(), option.getDefaultModelObject());
	}

	private boolean compareModels(final Object selected, final Object value)
	{
		if ((selected != null) && (selected instanceof Collection))
		{
			if (value instanceof Collection)
			{
				return ((Collection<?>)selected).containsAll((Collection<?>)value);
			}
			else
			{
				return ((Collection<?>)selected).contains(value);
			}
		}
		else
		{
			return Objects.equal(selected, value);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2939.java