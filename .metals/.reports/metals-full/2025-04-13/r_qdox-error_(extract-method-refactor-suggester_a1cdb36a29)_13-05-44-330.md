error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11022.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11022.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11022.java
text:
```scala
S@@tring[] paths = getInputAsArray();

/*
 * $Id$
 * $Revision$ $Date$
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
package wicket.extensions.markup.html.form.select;

import java.util.Collection;

import wicket.WicketRuntimeException;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.form.FormComponent;
import wicket.model.IModel;
import wicket.util.lang.Objects;

/**
 * Component that represents a <code>&lt;select&gt;</code> box. Elements are
 * provided by one or more <code>SelectChoice</code> or
 * <code>SelectOptions</code> components in the hierarchy below the
 * <code>Select</code> component.
 * 
 * Advantages to the standard choice components is that the user has a lot more
 * control over the markup between the &lt;select&gt; tag and its children
 * &lt;option&gt; tags: allowing for such things as &lt;optgroup&gt; tags.
 * 
 * TODO Post 1.2: General: Example
 * 
 * @see SelectOption
 * @see SelectOptions
 * 
 * @author Igor Vaynberg (ivaynberg@users.sf.net)
 */
public class Select extends FormComponent
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that will create a default model collection
	 * 
	 * @param id
	 *            component id
	 */
	public Select(String id)
	{
		super(id);
	}

	/**
	 * @see WebMarkupContainer#WebMarkupContainer(String, IModel)
	 */
	public Select(String id, IModel model)
	{
		super(id, model);
	}

	/**
	 * @see FormComponent#updateModel()
	 */
	public void updateModel()
	{
		Object object = getModelObject();
		boolean isModelCollection = object instanceof Collection;

		/*
		 * clear the model
		 */
		if (isModelCollection)
		{
			modelChanging();

			((Collection)object).clear();
		}
		else
		{
			getModel().setObject(this, null);
		}

		/*
		 * the input contains an array of full path of the selected option
		 * components unless nothing was selected in which case the input
		 * contains null
		 */
		String[] paths = getInputAsArray();

		/*
		 * if the input is null we do not need to do anything since the model
		 * collection has already been cleared
		 */

		if (paths != null && paths.length > 0)
		{
			if (!isModelCollection && paths.length > 1)
			{
				throw new WicketRuntimeException(
						"The model of Select component ["
								+ getPath()
								+ "] is not of type java.util.Collection, but more then one SelectOption component has been selected. Either remove the multiple attribute from the select tag or make the model of the Select component a collection");
			}

			for (int i = 0; i < paths.length; i++)
			{
				String path = paths[i];

				if (path != null)
				{
					/*
					 * option component path sans select component path =
					 * relative path from group to option since we know the
					 * option is child of select
					 */

					path = path.substring(getPath().length() + 1);

					// retrieve the selected checkbox component
					SelectOption option = (SelectOption)get(path);

					if (option == null)
					{
						throw new WicketRuntimeException(
								"submitted http post value ["
										+ paths.toString()
										+ "] for SelectOption component ["
										+ getPath()
										+ "] contains an illegal relative path element ["
										+ path
										+ "] which does not point to an SelectOption component. Due to this the Select component cannot resolve the selected SelectOption component pointed to by the illegal value. A possible reason is that component hierarchy changed between rendering and form submission.");
					}

					// assign the value
					if (isModelCollection)
					{
						((Collection)object).add(option.getModelObject());
					}
					else
					{
						setModelObject(option.getModelObject());
					}
				}
			}
		}

		if (isModelCollection)
		{
			modelChanged();
		}
	}

	/**
	 * Checks if the specified option is selected
	 * 
	 * @param option
	 * @return true if the option is selected, false otherwise
	 */
	boolean isSelected(SelectOption option)
	{
		// if the raw input is specified use that, otherwise use model
		if (hasRawInput()) {
			String[] paths = inputAsStringArray();
			if (paths != null && paths.length > 0)
			{
				for (int i = 0; i < paths.length; i++)
				{
					String path = paths[i];
					if (path.equals(option.getPath())) {
						return true;
					}
				}
			}
		} else {
			Object selected = getModelObject();
			Object value = option.getModelObject();

			if (selected != null && selected instanceof Collection)
			{
				if (value instanceof Collection)
				{
					return ((Collection)selected).containsAll((Collection)value);
				}
				else
				{
					return ((Collection)selected).contains(value);
				}
			}
			else
			{
				return Objects.equal(selected, value);
			}
		}
		
		return false;
		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11022.java