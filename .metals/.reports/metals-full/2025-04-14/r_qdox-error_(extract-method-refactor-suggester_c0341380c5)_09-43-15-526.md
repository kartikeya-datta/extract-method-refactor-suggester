error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3830.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3830.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3830.java
text:
```scala
protected v@@oid onComponentTag(final ComponentTag tag)

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
package org.apache.wicket.markup.html.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;


/**
 * A multiple choice list component.
 * 
 * @author Jonathan Locke
 * @author Johan Compagner
 * @author Martijn Dashorst
 */
public class ListMultipleChoice extends AbstractChoice
{
	private static final long serialVersionUID = 1L;

	/** The default maximum number of rows to display. */
	private static int defaultMaxRows = 8;

	/** The maximum number of rows to display. */
	private int maxRows = defaultMaxRows;

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
	protected static void setDefaultMaxRows(final int defaultMaxRows)
	{
		ListMultipleChoice.defaultMaxRows = defaultMaxRows;
	}

	/**
	 * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String)
	 */
	public ListMultipleChoice(final String id)
	{
		super(id);
	}

	/**
	 * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      List)
	 */
	public ListMultipleChoice(final String id, final List choices)
	{
		super(id, choices);
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
	 * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      List)
	 */
	public ListMultipleChoice(final String id, final List choices, final int maxRows)
	{
		super(id, choices);
		this.maxRows = maxRows;
	}

	/**
	 * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      List,IChoiceRenderer)
	 */
	public ListMultipleChoice(final String id, final List choices, final IChoiceRenderer renderer)
	{
		super(id, choices, renderer);
	}

	/**
	 * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      IModel, List)
	 */
	public ListMultipleChoice(final String id, IModel object, final List choices)
	{
		super(id, object, choices);
	}

	/**
	 * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      IModel, List,IChoiceRenderer)
	 */
	public ListMultipleChoice(final String id, IModel object, final List choices,
			final IChoiceRenderer renderer)
	{
		super(id, object, choices, renderer);
	}

	/**
	 * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      IModel)
	 */
	public ListMultipleChoice(String id, IModel choices)
	{
		super(id, choices);
	}

	/**
	 * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      IModel,IModel)
	 */
	public ListMultipleChoice(String id, IModel model, IModel choices)
	{
		super(id, model, choices);
	}

	/**
	 * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      IModel,IChoiceRenderer)
	 */
	public ListMultipleChoice(String id, IModel choices, IChoiceRenderer renderer)
	{
		super(id, choices, renderer);
	}


	/**
	 * @see org.apache.wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      IModel, IModel,IChoiceRenderer)
	 */
	public ListMultipleChoice(String id, IModel model, IModel choices, IChoiceRenderer renderer)
	{
		super(id, model, choices, renderer);
	}


	/**
	 * Sets the number of visible rows in the listbox.
	 * 
	 * @param maxRows
	 *            the number of visible rows
	 * @return this
	 */
	public final ListMultipleChoice setMaxRows(final int maxRows)
	{
		this.maxRows = maxRows;
		return this;
	}

	/**
	 * @see FormComponent#getModelValue()
	 */
	public final String getModelValue()
	{
		// Get the list of selected values
		Object modelObject = getModelObject();
		if (modelObject != null && !(modelObject instanceof Collection))
		{
			throw new WicketRuntimeException(
					"Model object for a ListMultipleChoice must be a Collection (found "
							+ modelObject.getClass() + ")");
		}
		final Collection selectedValues = (Collection)modelObject;
		final AppendingStringBuffer buffer = new AppendingStringBuffer();
		if (selectedValues != null)
		{
			final List choices = getChoices();
			for (final Iterator iterator = selectedValues.iterator(); iterator.hasNext();)
			{
				final Object object = iterator.next();

				int index = choices.indexOf(object);
				buffer.append(getChoiceRenderer().getIdValue(object, index));
				buffer.append(VALUE_SEPARATOR);
			}
		}
		return buffer.toString();
	}

	/**
	 * @see org.apache.wicket.markup.html.form.AbstractChoice#isSelected(Object,int,
	 *      String)
	 */
	protected final boolean isSelected(Object choice, int index, String selected)
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
	 * @see org.apache.wicket.Component#onComponentTag(ComponentTag)
	 */
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
	 * @see org.apache.wicket.markup.html.form.FormComponent#convertValue(String[])
	 */
	protected Object convertValue(String[] ids) throws ConversionException
	{
		if (ids != null && ids.length > 0 && !Strings.isEmpty(ids[0]))
		{
			return convertChoiceIdsToChoices(ids);
		}
		else
		{
			// TODO 1.3: check if its safe to return Collections.EMPTY_LIST here
			return new ArrayList();
		}
	}

	/**
	 * Converts submitted choice ids to choice objects.
	 * 
	 * @param ids
	 *            choice ids. this array is nonnull and always contains at least
	 *            one id.
	 * @return list of choices.
	 */
	protected List convertChoiceIdsToChoices(String[] ids)
	{
		ArrayList selectedValues = new ArrayList();

		// If one or more ids is selected
		if (ids != null && ids.length > 0 && !Strings.isEmpty(ids[0]))
		{
			// Get values that could be selected
			final List choices = getChoices();

			// Loop through selected indices
			for (int i = 0; i < ids.length; i++)
			{
				for (int index = 0; index < choices.size(); index++)
				{
					// Get next choice
					final Object choice = choices.get(index);
					if (getChoiceRenderer().getIdValue(choice, index).equals(ids[i]))
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
	 * If the model object exists, it is assumed to be a Collection, and it is
	 * modified in-place. Then {@link Model#setObject(Object)} is called with
	 * the same instance: it allows the Model to be notified of changes even
	 * when {@link Model#getObject()} returns a different {@link Collection} at
	 * every invocation.
	 * 
	 * @see FormComponent#updateModel()
	 * @throws UnsupportedOperationException
	 *             if the model object Collection cannot be modified
	 */
	public void updateModel()
	{
		Collection selectedValues = (Collection)getModelObject();
		if (selectedValues != null)
		{
			modelChanging();
			selectedValues.clear();
			selectedValues.addAll((Collection)getConvertedInput());
			modelChanged();
			getModel().setObject(selectedValues);
		}
		else
		{
			selectedValues = (Collection)getConvertedInput();
			modelChanging();
			getModel().setObject(selectedValues);
			modelChanged();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3830.java