error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14330.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14330.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14330.java
text:
```scala
final I@@Converter converter = getConverter(objectClass);

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
import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.WildcardListModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.version.undo.Change;


/**
 * Abstract base class for all choice (html select) options.
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 * @author Johan Compagner
 * 
 * @param <T>
 *            The model object type
 * 
 * @param <E>
 *            class of a single element in the choices list
 */
public abstract class AbstractChoice<T, E> extends FormComponent<T>
{
	/** */
	private static final long serialVersionUID = 1L;

	/** The list of objects. */
	private IModel<List<? extends E>> choices;

	/** The renderer used to generate display/id values for the objects. */
	private IChoiceRenderer<E> renderer;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @see org.apache.wicket.Component#Component(String)
	 */
	public AbstractChoice(final String id)
	{
		this(id, new WildcardListModel<E>(new ArrayList<E>()), new ChoiceRenderer<E>());
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param choices
	 *            The collection of choices in the dropdown
	 * @see org.apache.wicket.Component#Component(String)
	 */
	public AbstractChoice(final String id, final List<? extends E> choices)
	{
		this(id, new WildcardListModel<E>(choices), new ChoiceRenderer<E>());
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param renderer
	 *            The rendering engine
	 * @param choices
	 *            The collection of choices in the dropdown
	 * @see org.apache.wicket.Component#Component(String)
	 */
	public AbstractChoice(final String id, final List<? extends E> choices,
		final IChoiceRenderer<E> renderer)
	{
		this(id, new WildcardListModel<E>(choices), renderer);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param choices
	 *            The collection of choices in the dropdown
	 * @see org.apache.wicket.Component#Component(String, IModel)
	 */
	public AbstractChoice(final String id, IModel<T> model, final List<? extends E> choices)
	{
		this(id, model, new WildcardListModel<E>(choices), new ChoiceRenderer<E>());
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param choices
	 *            The drop down choices
	 * @param renderer
	 *            The rendering engine
	 * @see org.apache.wicket.Component#Component(String, IModel)
	 */
	public AbstractChoice(final String id, IModel<T> model, final List<? extends E> choices,
		final IChoiceRenderer<E> renderer)
	{
		this(id, model, new WildcardListModel<E>(choices), renderer);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param choices
	 *            The collection of choices in the dropdown
	 * @see org.apache.wicket.Component#Component(String)
	 */
	public AbstractChoice(final String id, final IModel<List<? extends E>> choices)
	{
		this(id, choices, new ChoiceRenderer<E>());
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param renderer
	 *            The rendering engine
	 * @param choices
	 *            The collection of choices in the dropdown
	 * @see org.apache.wicket.Component#Component(String)
	 */
	public AbstractChoice(final String id, final IModel<List<? extends E>> choices,
		final IChoiceRenderer<E> renderer)
	{
		super(id);
		this.choices = wrap(choices);
		setChoiceRenderer(renderer);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param choices
	 *            The collection of choices in the dropdown
	 * @see org.apache.wicket.Component#Component(String, IModel)
	 */
	public AbstractChoice(final String id, IModel<T> model, final IModel<List<? extends E>> choices)
	{
		this(id, model, choices, new ChoiceRenderer<E>());
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param renderer
	 *            The rendering engine
	 * @param choices
	 *            The drop down choices
	 * @see org.apache.wicket.Component#Component(String, IModel)
	 */
	public AbstractChoice(final String id, IModel<T> model,
		final IModel<List<? extends E>> choices, final IChoiceRenderer<E> renderer)
	{
		super(id, model);
		this.choices = wrap(choices);
		setChoiceRenderer(renderer);
	}


	/**
	 * @return The collection of object that this choice has
	 */
	public List<? extends E> getChoices()
	{
		List<? extends E> choices = (this.choices != null) ? this.choices.getObject() : null;
		if (choices == null)
		{
			throw new NullPointerException(
				"List of choices is null - Was the supplied 'Choices' model empty?");
		}
		return choices;
	}


	/**
	 * Sets the list of choices
	 * 
	 * @param choices
	 *            model representing the list of choices
	 * @return this for chaining
	 */
	public final AbstractChoice<T, E> setChoices(IModel<List<? extends E>> choices)
	{
		if (this.choices != null && this.choices != choices)
		{
			if (isVersioned())
			{
				addStateChange(new ChoicesListChange());
			}
		}
		this.choices = wrap(choices);
		return this;
	}

	/**
	 * Sets the list of choices.
	 * 
	 * @param choices
	 *            the list of choices
	 * @return this for chaining
	 */
	public final AbstractChoice<T, E> setChoices(List<E> choices)
	{
		if ((this.choices != null))
		{
			if (isVersioned())
			{
				addStateChange(new ChoicesListChange());
			}
		}
		this.choices = new WildcardListModel<E>(choices);
		return this;
	}

	/**
	 * @return The IChoiceRenderer used for rendering the data objects
	 */
	public final IChoiceRenderer<E> getChoiceRenderer()
	{
		return renderer;
	}

	/**
	 * Set the choice renderer to be used.
	 * 
	 * @param renderer
	 * @return this for chaining
	 */
	public final AbstractChoice<T, E> setChoiceRenderer(IChoiceRenderer<E> renderer)
	{
		if (renderer == null)
		{
			renderer = new ChoiceRenderer<E>();
		}
		this.renderer = renderer;
		return this;
	}

	/**
	 * @see org.apache.wicket.Component#detachModel()
	 */
	@Override
	protected void detachModel()
	{
		super.detachModel();

		if (choices != null)
		{
			choices.detach();
		}
	}

	/**
	 * 
	 * @param selected
	 *            The object that's currently selected
	 * @return Any default choice, such as "Choose One", depending on the subclass
	 */
	protected CharSequence getDefaultChoice(final Object selected)
	{
		return "";
	}

	/**
	 * Gets whether the given value represents the current selection.
	 * 
	 * @param object
	 *            The object to check
	 * @param index
	 *            The index in the choices collection this object is in.
	 * @param selected
	 *            The currently selected string value
	 * @return Whether the given value represents the current selection
	 */
	protected abstract boolean isSelected(final E object, int index, String selected);

	/**
	 * Gets whether the given value is disabled. This default implementation always returns false.
	 * 
	 * @param object
	 *            The object to check
	 * @param index
	 *            The index in the choices collection this object is in.
	 * @param selected
	 *            The currently selected string value
	 * @return Whether the given value represents the current selection
	 */
	protected boolean isDisabled(final E object, int index, String selected)
	{
		return false;
	}

	/**
	 * Handle the container's body.
	 * 
	 * @param markupStream
	 *            The markup stream
	 * @param openTag
	 *            The open tag for the body
	 * @see org.apache.wicket.Component#onComponentTagBody(MarkupStream, ComponentTag)
	 */
	@Override
	protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
		List<? extends E> choices = getChoices();
		final AppendingStringBuffer buffer = new AppendingStringBuffer((choices.size() * 50) + 16);
		final String selected = getValue();

		// Append default option
		buffer.append(getDefaultChoice(selected));

		for (int index = 0; index < choices.size(); index++)
		{
			final E choice = choices.get(index);
			appendOptionHtml(buffer, choice, index, selected);
		}

		buffer.append("\n");
		replaceComponentTagBody(markupStream, openTag, buffer);
	}

	/**
	 * Generates and appends html for a single choice into the provided buffer
	 * 
	 * @param buffer
	 *            Appending string buffer that will have the generated html appended
	 * @param choice
	 *            Choice object
	 * @param index
	 *            The index of this option
	 * @param selected
	 *            The currently selected string value
	 */
	@SuppressWarnings("unchecked")
	protected void appendOptionHtml(AppendingStringBuffer buffer, E choice, int index,
		String selected)
	{
		T objectValue = (T)renderer.getDisplayValue(choice);
		Class<T> objectClass = (Class<T>)(objectValue == null ? null : objectValue.getClass());

		String displayValue = "";
		if (objectClass != null && objectClass != String.class)
		{
			final IConverter<T> converter = getConverter(objectClass);

			displayValue = converter.convertToString(objectValue, getLocale());
		}
		else if (objectValue != null)
		{
			displayValue = objectValue.toString();
		}
		buffer.append("\n<option ");
		if (isSelected(choice, index, selected))
		{
			buffer.append("selected=\"selected\" ");
		}
		if (isDisabled(choice, index, selected))
		{
			buffer.append("disabled=\"disabled\" ");
		}
		buffer.append("value=\"");
		buffer.append(Strings.escapeMarkup(renderer.getIdValue(choice, index)));
		buffer.append("\">");

		String display = displayValue;
		if (localizeDisplayValues())
		{
			display = getLocalizer().getString(displayValue, this, displayValue);
		}
		CharSequence escaped = display;
		if (getEscapeModelStrings())
		{
			escaped = escapeOptionHtml(display);
		}
		buffer.append(escaped);
		buffer.append("</option>");
	}

	/**
	 * Method to override if you want special escaping of the options html.
	 * 
	 * @param displayValue
	 * @return The escaped display value
	 */
	protected CharSequence escapeOptionHtml(String displayValue)
	{
		return Strings.escapeMarkup(displayValue, false, true);
	}

	/**
	 * @see org.apache.wicket.markup.html.form.FormComponent#supportsPersistence()
	 */
	@Override
	protected boolean supportsPersistence()
	{
		return true;
	}

	/**
	 * Override this method if you want to localize the display values of the generated options. By
	 * default false is returned so that the display values of options are not tested if they have a
	 * i18n key.
	 * 
	 * @return true If you want to localize the display values, default == false
	 */
	protected boolean localizeDisplayValues()
	{
		return false;
	}

	/**
	 * Change object to represent the change of the choices property
	 * 
	 * @author ivaynberg
	 */
	private class ChoicesListChange extends Change
	{
		private static final long serialVersionUID = 1L;

		private final IModel<List<? extends E>> oldChoices;

		/**
		 * Construct.
		 */
		public ChoicesListChange()
		{
			oldChoices = choices;
		}

		/**
		 * @see org.apache.wicket.version.undo.Change#undo()
		 */
		@Override
		public void undo()
		{
			choices = oldChoices;
		}

		/**
		 * Make debugging easier
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "ChoiceListChange[component: " + getPath() + ", old choices: " + oldChoices +
				"]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14330.java