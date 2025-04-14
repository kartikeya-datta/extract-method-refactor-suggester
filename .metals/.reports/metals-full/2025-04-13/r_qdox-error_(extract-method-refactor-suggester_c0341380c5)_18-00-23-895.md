error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/72.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/72.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/72.java
text:
```scala
private i@@nt maxRows = defaultMaxRows;

/*
 * $Id: ListMultipleChoice.java 5861 2006-05-25 20:55:07 +0000 (Thu, 25 May
 * 2006) eelco12 $ $Revision$ $Date: 2006-05-25 20:55:07 +0000 (Thu, 25
 * May 2006) $
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import wicket.MarkupContainer;
import wicket.markup.ComponentTag;
import wicket.model.IModel;
import wicket.util.convert.ConversionException;
import wicket.util.string.AppendingStringBuffer;
import wicket.util.string.Strings;

/**
 * A multiple choice list component.
 * 
 * @param <T>
 *            The type
 * 
 * @author Jonathan Locke
 * @author Johan Compagner
 * @author Martijn Dashorst
 */
public class ListMultipleChoice<T> extends AbstractChoice<Collection<T>, T>
{
	private static final long serialVersionUID = 1L;

	/** The default maximum number of rows to display. */
	private static int defaultMaxRows = 8;

	/** The maximum number of rows to display. */
	private int maxRows;

	/**
	 * Gets the default maximum number of rows to display.
	 * 
	 * @return Returns the defaultMaxRows.
	 */
	protected static int getDefaultMaxRows()
	{
		return defaultMaxRows;
	}

	/**
	 * Sets the default maximum number of rows to display.
	 * 
	 * @param defaultMaxRows
	 *            The defaultMaxRows to set.
	 */
	@SuppressWarnings("unchecked")
	protected static void setDefaultMaxRows(final int defaultMaxRows)
	{
		ListMultipleChoice.defaultMaxRows = defaultMaxRows;
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(MarkupContainer,String)
	 */
	public ListMultipleChoice(MarkupContainer parent, final String id)
	{
		super(parent, id);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(MarkupContainer,String,
	 *      List)
	 */
	public ListMultipleChoice(MarkupContainer parent, final String id, final List<T> choices)
	{
		super(parent, id, choices);
	}

	/**
	 * Creates a multiple choice list with a maximum number of visible rows.
	 * 
	 * @param id
	 *            component id
	 * @param choices
	 *            list of choices
	 * @param maxRows
	 *            the maximum number of visible rows.
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(MarkupContainer,String,
	 *      List)
	 */
	public ListMultipleChoice(MarkupContainer parent, final String id, final List<T> choices,
			final int maxRows)
	{
		super(parent, id, choices);
		this.maxRows = maxRows;
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(MarkupContainer,String,
	 *      List,IChoiceRenderer)
	 */
	public ListMultipleChoice(MarkupContainer parent, final String id, final List<T> choices,
			final IChoiceRenderer<T> renderer)
	{
		super(parent, id, choices, renderer);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(MarkupContainer,String,
	 *      IModel, List)
	 */
	public ListMultipleChoice(MarkupContainer parent, final String id,
			IModel<Collection<T>> object, final List<T> choices)
	{
		super(parent, id, object, choices);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(MarkupContainer,String,
	 *      IModel, List,IChoiceRenderer)
	 */
	public ListMultipleChoice(MarkupContainer parent, final String id,
			IModel<Collection<T>> object, final List<T> choices, final IChoiceRenderer<T> renderer)
	{
		super(parent, id, object, choices, renderer);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(MarkupContainer,String,
	 *      IModel)
	 */
	public ListMultipleChoice(MarkupContainer parent, String id, IModel<List<T>> choices)
	{
		super(parent, id, choices);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(MarkupContainer,String,
	 *      IModel,IModel)
	 */
	public ListMultipleChoice(MarkupContainer parent, String id, IModel<Collection<T>> model,
			IModel<List<T>> choices)
	{
		super(parent, id, model, choices);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(MarkupContainer,String,
	 *      IModel,IChoiceRenderer)
	 */
	public ListMultipleChoice(MarkupContainer parent, String id, IModel<List<T>> choices,
			IChoiceRenderer<T> renderer)
	{
		super(parent, id, choices, renderer);
	}


	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(MarkupContainer,String,
	 *      IModel, IModel,IChoiceRenderer)
	 */
	public ListMultipleChoice(MarkupContainer parent, String id, IModel<Collection<T>> model,
			IModel<List<T>> choices, IChoiceRenderer<T> renderer)
	{
		super(parent, id, model, choices, renderer);
	}


	/**
	 * Sets the number of visible rows in the listbox.
	 * 
	 * @param maxRows
	 *            the number of visible rows
	 * @return this
	 */
	public final ListMultipleChoice<T> setMaxRows(final int maxRows)
	{
		this.maxRows = maxRows;
		return this;
	}

	/**
	 * @see FormComponent#getModelValue()
	 */
	@Override
	public final String getModelValue()
	{
		// Get the list of selected values
		final Collection<T> selectedValues = getModelObject();
		final AppendingStringBuffer buffer = new AppendingStringBuffer();
		if (selectedValues != null)
		{
			final List<T> choices = getChoices();
			for (T object : selectedValues)
			{
				int index = choices.indexOf(object);
				buffer.append(getChoiceRenderer().getIdValue(object, index));
				buffer.append(VALUE_SEPARATOR);
			}
		}
		return buffer.toString();
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#isSelected(java.lang.Object,
	 *      int, java.lang.String)
	 */
	@Override
	protected final boolean isSelected(T choice, int index, String selected)
	{
		// Have a value at all?
		if (selected != null)
		{
			// Loop through ids
			for (final StringTokenizer tokenizer = new StringTokenizer(selected, VALUE_SEPARATOR); tokenizer
					.hasMoreTokens();)
			{
				final String id = tokenizer.nextToken();
				if (id.equals(getChoiceRenderer().getIdValue(choice, index)))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @see wicket.Component#onComponentTag(ComponentTag)
	 */
	@Override
	protected final void onComponentTag(final ComponentTag tag)
	{
		super.onComponentTag(tag);
		tag.put("multiple", "multiple");

		if (!tag.getAttributes().containsKey("size"))
		{
			tag.put("size", Math.min(maxRows, getChoices().size()));
		}
	}

	/**
	 * @see wicket.markup.html.form.FormComponent#convertValue(String[])
	 */
	@Override
	protected Collection<T> convertValue(String[] ids) throws ConversionException
	{
		ArrayList<T> selectedValues = new ArrayList<T>();

		// If one or more ids is selected
		if (ids != null && ids.length > 0 && !Strings.isEmpty(ids[0]))
		{
			// Get values that could be selected
			final List<T> choices = getChoices();

			for (String element : ids)
			{
				for (int index = 0; index < choices.size(); index++)
				{
					// Get next choice
					final T choice = choices.get(index);
					if (getChoiceRenderer().getIdValue(choice, index).equals(element))
					{
						selectedValues.add(choice);
						break;
					}
				}
			}
		}
		return selectedValues;
	}

	/**
	 * @see FormComponent#updateModel()
	 */
	@Override
	public final void updateModel()
	{
		Collection<T> selectedValues = getModelObject();
		if (selectedValues != null)
		{
			modelChanging();
			selectedValues.clear();
			selectedValues.addAll(getConvertedInput());
			modelChanged();
		}
		else
		{
			selectedValues = getConvertedInput();
			setModelObject(selectedValues);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/72.java