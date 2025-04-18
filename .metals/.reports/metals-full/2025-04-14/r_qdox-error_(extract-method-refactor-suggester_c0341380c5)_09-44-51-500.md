error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16876.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16876.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16876.java
text:
```scala
t@@ag.put("multiple", "multiple");

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import wicket.markup.ComponentTag;
import wicket.model.IModel;
import wicket.util.string.Strings;

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
	protected static void setDefaultMaxRows(final int defaultMaxRows)
	{
		ListMultipleChoice.defaultMaxRows = defaultMaxRows;
	}
	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String)
	 */
	public ListMultipleChoice(final String id)
	{
		super(id);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String, List)
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
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String, List)
	 */
	public ListMultipleChoice(final String id, final List choices, final int maxRows)
	{
		super(id, choices);
		this.maxRows = maxRows;
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      List,IChoiceRenderer)
	 */
	public ListMultipleChoice(final String id, final List choices, final IChoiceRenderer renderer)
	{
		super(id, choices, renderer);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      IModel, List)
	 */
	public ListMultipleChoice(final String id, IModel object, final List choices)
	{
		super(id, object, choices);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      IModel, List,IChoiceRenderer)
	 */
	public ListMultipleChoice(final String id, IModel object, final List choices,
			final IChoiceRenderer renderer)
	{
		super(id, object, choices, renderer);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      IModel)
	 */
	public ListMultipleChoice(String id, IModel choices)
	{
		super(id, choices);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      IModel,IModel)
	 */
	public ListMultipleChoice(String id, IModel model, IModel choices)
	{
		super(id, model, choices);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      IModel,IChoiceRenderer)
	 */
	public ListMultipleChoice(String id, IModel choices, IChoiceRenderer renderer)
	{
		super(id, choices, renderer);
	}


	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
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
		final Collection selectedValues = (Collection)getModelObject();
		final StringBuffer buffer = new StringBuffer();
		if (selectedValues != null)
		{
			final List choices = getChoices();
			for (final Iterator iterator = selectedValues.iterator(); iterator.hasNext();)
			{
				final Object object = iterator.next();

				int index = choices.indexOf(object);
				buffer.append(getChoiceRenderer().getIdValue(object, index));
				buffer.append(";");
			}
		}
		return buffer.toString();
	}

	/**
	 * @see FormComponent#setModelValue(java.lang.String)
	 */
	public final void setModelValue(final String value)
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
		final List choices = getChoices();
		for (final StringTokenizer tokenizer = new StringTokenizer(value, ";"); tokenizer
				.hasMoreTokens();)
		{
			String selected = tokenizer.nextToken();

			for (int index = 0; index < choices.size(); index++)
			{
				// Get next choice
				final Object choice = choices.get(index);
				if (getChoiceRenderer().getIdValue(choice, index).equals(selected))
				{
					selectedValues.add(choice);
					break;
				}
			}

		}
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#isSelected(Object,int, String)
	 */
	protected final boolean isSelected(Object choice, int index, String selected)
	{
		// Get value of the form "id1;id2;id3"
		final String value = getValue();

		// Have a value at all?
		if (value != null)
		{
			// Loop through ids
			for (final StringTokenizer tokenizer = new StringTokenizer(value, ";"); tokenizer
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
	protected final void onComponentTag(final ComponentTag tag)
	{
		super.onComponentTag(tag);
		tag.put("multiple", true);
		tag.put("size", Math.min(maxRows, getChoices().size()));
	}

	/**
	 * @see FormComponent#updateModel()
	 */
	public final void updateModel()
	{
		final String[] ids = inputAsStringArray();

		// if input was null then value was not submitted (disabled field), ignore it
		// FIXME General: This can't be done! null is nothing selected! we need check for isEnabled for this.
//		if (ids != null)
		{
			// Get the list of selected values
			Collection selectedValues = (Collection)getModelObject();
	
			if (selectedValues != null)
			{
				selectedValues.clear();
			}
	
			// Get indices selected from request
	
			// If one or more ids is selected
			if (ids != null && ids.length > 0 && !Strings.isEmpty(ids[0]))
			{
				if(selectedValues == null)
				{
					selectedValues = new ArrayList();
				}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16876.java