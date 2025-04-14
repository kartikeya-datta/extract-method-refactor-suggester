error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16245.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16245.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16245.java
text:
```scala
public abstract v@@oid updateModel();

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

import java.util.List;

import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.model.IDetachable;
import wicket.model.IModel;
import wicket.util.string.Strings;
import wicket.version.undo.Change;

/**
 * Abstract base class for all choice (html select) options.
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 * @author Johan Compagner
 */
abstract class AbstractChoice extends FormComponent
{
	/** The list of objects. */
	private List choices;

	/** The renderer used to generate display/id values for the objects. */
	private IChoiceRenderer renderer;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            See Component
	 * @see wicket.Component#Component(String)
	 */
	public AbstractChoice(final String id)
	{
		super(id);
	}

	/**
	 * @param id
	 *            See Component
	 * @param choices
	 *            The collection of choices in the dropdown
	 * @see wicket.Component#Component(String)
	 */
	public AbstractChoice(final String id, final List choices)
	{
		this(id, choices,new ChoiceRenderer());
	}

	/**
	 * @param id
	 *            See Component
	 * @param renderer
	 *            The rendering engine
	 * @param choices
	 *            The collection of choices in the dropdown
	 * @see wicket.Component#Component(String)
	 */
	public AbstractChoice(final String id, final List choices,final IChoiceRenderer renderer)
	{
		super(id);
		this.choices = choices;
		this.renderer = renderer;
	}

	/**
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param choices
	 *            The collection of choices in the dropdown
	 * @see wicket.Component#Component(String, IModel)
	 */
	public AbstractChoice(final String id, IModel model, final List choices)
	{
		this(id, model, choices, new ChoiceRenderer());
	}

	/**
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param renderer
	 *            The rendering engine
	 * @param choices
	 *            The drop down choices
	 * @see wicket.Component#Component(String, IModel)
	 */
	public AbstractChoice(final String id, IModel model, final List choices, final IChoiceRenderer renderer)
	{
		super(id, model);
		this.choices = choices;
		this.renderer = renderer;
	}

	/**
	 * @return The collection of object that this choice has
	 */
	public List getChoices()
	{
		return choices;
	}

	/**
	 * Sets the list of choices.
	 * 
	 * @param choices
	 *            the list of choices
	 */
	public final void setChoices(List choices)
	{
		if ((this.choices != null) && (this.choices != choices))
		{
			if (isVersioned())
			{
				addStateChange(new Change()
				{
					final List oldList = AbstractChoice.this.choices;
					public void undo()
					{
						AbstractChoice.this.choices = oldList;
					}
				});
			}
		}
		this.choices = choices;
	}

	/**
	 * @return The IChoiceRenderer used for rendering the data objects
	 */
	public final IChoiceRenderer getChoiceRenderer()
	{
		return renderer;
	}

	/**
	 * Set the choice renderer to be used.
	 *  
	 * @param renderer
	 */
	public final void setChoiceRenderer(IChoiceRenderer renderer)
	{
	    this.renderer = renderer;
	}
	
	/**
	 * @see wicket.Component#detachModel()
	 */
	protected void detachModel()
	{
		super.detachModel();
		
		if (choices instanceof IDetachable)
		{
			((IDetachable)choices).detach();
		}
	}

	/**
	 * @param selected
	 *            The object that's currently selected
	 * @return Any default choice, such as "Choose One", depending on the
	 *         subclass
	 */
	protected String getDefaultChoice(final Object selected)
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
	 * @return Whether the given value represents the current selection
	 */
	protected abstract boolean isSelected(final Object object, int index);

	/**
	 * Handle the container's body.
	 * 
	 * @param markupStream
	 *            The markup stream
	 * @param openTag
	 *            The open tag for the body
	 * @see wicket.Component#onComponentTagBody(MarkupStream, ComponentTag)
	 */
	protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
		final StringBuffer buffer = new StringBuffer();
		final Object selected = getModelObject();

		// Append default option
		buffer.append(getDefaultChoice(selected));

		List choices = getChoices();
		for(int index=0;index<choices.size();index++)
		{
			// Get next choice
			final Object choice = choices.get(index);
			if (choice != null)
			{
				final String displayValue = renderer.getDisplayValue(choice);
				buffer.append("\n<option ");
				if (isSelected(choice, index))
				{
					buffer.append("selected=\"selected\"");
				}
				buffer.append("value=\"");
				buffer.append(renderer.getIdValue(choice, index));
				buffer.append("\">");
				String display = getLocalizer().getString(getId() + "." + displayValue, this,
						displayValue);
				String escaped = Strings.escapeMarkup(display, false, true);
				buffer.append(escaped);
				buffer.append("</option>");
			}
			else
			{
				throw new IllegalArgumentException("Choice list has null value at index " + index);
			}
		}

		buffer.append("\n");
		replaceComponentTagBody(markupStream, openTag, buffer.toString());
	}

	/**
	 * @see wicket.markup.html.form.FormComponent#supportsPersistence()
	 */
	protected boolean supportsPersistence()
	{
		return true;
	}

	/**
	 * Updates the model of this component from the request.
	 * 
	 * @see wicket.markup.html.form.FormComponent#updateModel()
	 */
	protected abstract void updateModel();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16245.java