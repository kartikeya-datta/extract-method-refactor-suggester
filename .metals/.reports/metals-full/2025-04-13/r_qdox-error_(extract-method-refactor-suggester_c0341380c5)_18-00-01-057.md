error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10155.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10155.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10155.java
text:
```scala
final S@@tring[] indicesOrIds = inputAsStringArray();

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
package wicket.markup.html.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import wicket.markup.ComponentTag;
import wicket.model.Model;
import wicket.model.PropertyModel;
import wicket.util.string.Strings;

/**
 * A multiple choice list component.
 *
 * @author Jonathan Locke
 * @author Johan Compagner
 */
public class ListMultipleChoice extends AbstractChoice
{
	/** Serial Version ID. */
	private static final long serialVersionUID = -1000324612688307682L;

	/**
	 * @see AbstractChoice#AbstractChoice(String, Serializable, String, Collection)
	 */
	public ListMultipleChoice(final String componentName, final Serializable model,
			final String expression, final Collection values)
	{
		super(componentName, new PropertyModel(new Model(model), expression), values);
	}

	/**
	 * @see AbstractChoice#AbstractChoice(String, Serializable, Collection)
	 */
	public ListMultipleChoice(final String componentName, final Serializable model,
			final Collection values)
	{
		super(componentName, model, values);
	}

	/**
	 * @see FormComponent#getValue()
	 */
	public final String getValue()
	{
		// Get the list of selected values
		final Collection selectedValues = (Collection)getModelObject();
		final StringBuffer value = new StringBuffer();
		if (selectedValues != null)
		{
			final List list = getValues();
			final Iterator it = selectedValues.iterator();
			while (it.hasNext())
			{
				final int index = list.indexOf(it.next());
				if (list instanceof IDetachableChoiceList)
				{
					value.append(((IDetachableChoiceList)list).getId(index));
				}
				else
				{
					value.append(index);
				}
				// the id's can't have ; in there id!! should we escape it or
				// something??
				value.append(";");
			}
		}
		return value.toString();
	}

	/**
	 * Sets the cookie value for this component.
	 *
	 * @param value
	 *            the cookie value for this component
	 * @see FormComponent#setValue(java.lang.String)
	 */
	public final void setValue(final String value)
	{
		Collection selectedValues = (Collection)getModelObject();
		if (selectedValues == null)
		{
			selectedValues = new ArrayList();
			setModelObject(selectedValues);
		}
		else
		{
			selectedValues.clear();
		}
		final List list = getValues();
		final StringTokenizer st = new StringTokenizer(value, ";");
		while (st.hasMoreTokens())
		{
			final String idOrIndex = st.nextToken();
			if (list instanceof IDetachableChoiceList)
			{
				selectedValues.add(((IDetachableChoiceList)list).objectForId(idOrIndex));
			}
			else
			{
				final int index = Integer.parseInt(idOrIndex);
				selectedValues.add(list.get(index));
			}
		}
	}

	/**
	 * Updates this forms model from the request.
	 *
	 * @see FormComponent#updateModel()
	 */
	public final void updateModel()
	{
		// Get the list of selected values
		Collection selectedValues = (Collection)getModelObject();

		if (selectedValues != null)
		{
			selectedValues.clear();
		}
		else
		{
			selectedValues = new ArrayList();
			setModelObject(selectedValues);
		}

		// Get indices selected from request
		final String[] indicesOrIds = getRequestStrings();

		// If one or more ids is selected
		if (indicesOrIds != null && indicesOrIds.length > 0 && !Strings.isEmpty(indicesOrIds[0]))
		{
			// Get values that could be selected
			final List list = getValues();

			// Loop through selected indices
			for (int i = 0; i < indicesOrIds.length; i++)
			{
				if (list instanceof IDetachableChoiceList)
				{
					selectedValues.add(((IDetachableChoiceList)list).objectForId(indicesOrIds[i]));
				}
				else
				{
					// Get index
					final int index = Integer.parseInt(indicesOrIds[i]);
					
					// Add the value at the given index to the collection of
					// selected values
					selectedValues.add(list.get(index));
				}
			}
		}
	}

	/**
	 * Processes the component tag.
	 *
	 * @param tag
	 *            Tag to modify
	 * @see wicket.Component#onComponentTag(ComponentTag)
	 */
	protected final void onComponentTag(final ComponentTag tag)
	{
		super.onComponentTag(tag);
		tag.put("multiple", true);
	}

	/**
	 * Gets whether the given value represents the current selection.
	 *
	 * @param currentValue
	 *            the current list value
	 * @return whether the given value represents the current selection
	 * @see wicket.markup.html.form.AbstractChoice#isSelected(java.lang.Object)
	 */
	protected final boolean isSelected(Object currentValue)
	{
		Collection collection = (Collection)getModelObject();
		if (collection != null)
			return collection.contains(currentValue);
		return false;
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10155.java