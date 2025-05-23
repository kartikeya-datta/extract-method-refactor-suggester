error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4233.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4233.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,3]

error in qdox parser
file content:
```java
offset: 3
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4233.java
text:
```scala
+ (@@isSelected(choice,index) ? " checked=\"checked\"" : "") + " value=\"" + id

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

import wicket.Page;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.model.IModel;
import wicket.util.string.Strings;
import wicket.version.undo.Change;

/**
 * A choice subclass that shows choices via checkboxes.
 * <p>
 * Java:
 * <pre>
 * 	List SITES = Arrays.asList(new String[] { "The Server Side", "Java Lobby", "Java.Net" });
 *	// Add a set of checkboxes uses Input's 'site' property to designate the
 *	// current selections, and that uses the SITES list for the available options.
 *	form.add(new CheckBoxMultipleChoice("site", SITES));
 * </pre>
 * HTML:
 * <pre>
 * &lt;span valign="top" wicket:id="site"&gt;
 *	&lt;input type="checkbox"&gt;site 1&lt;/input&gt;
 *	&lt;input type="checkbox"&gt;site 2&lt;/input&gt;
 * &lt;/span&gt;
 * </pre>
 * </p>
 * 
 * @author Jonathan Locke
 * @author Johan Compagner
 * @author Martijn Dashorst
 * @author Gwyn Evans
 */
public class CheckBoxMultipleChoice extends ListMultipleChoice
{
	private static final long serialVersionUID = 1L;
	
	private class SuffixChange extends Change
	{
		private static final long serialVersionUID = 1L;
		
		final String prevSuffix;
		
		SuffixChange(String prevSuffix)
		{
			this.prevSuffix = prevSuffix;
		}
		
		/**
		 * @see wicket.version.undo.Change#undo()
		 */
		public void undo()
		{
			setSuffix(prevSuffix);
		}
	}

	private class PrefixChange extends Change
	{
		private static final long serialVersionUID = 1L;
		
		final String prevPrefix;
		
		PrefixChange(String prevSuffix)
		{
			this.prevPrefix = prevSuffix;
		}
		
		/**
		 * @see wicket.version.undo.Change#undo()
		 */
		public void undo()
		{
			setPrefix(prevPrefix);
		}
	}
	
	private String prefix = "";
	private String suffix = "<br />\n";
	
    /**
     * Constructor
     *
     * @param id
     *            See Component
     * @see wicket.Component#Component(String)
     * @see AbstractChoice#AbstractChoice(String)
     */
    public CheckBoxMultipleChoice(final String id)
    {
        super(id);
    }

	/**
	 * Constructor
	 * 
	 * @param id
	 *            See Component
	 * @param choices
	 *            The collection of choices in the radio choice
	 * @see wicket.Component#Component(String)
	 * @see AbstractChoice#AbstractChoice(String, java.util.List)
	 */
	public CheckBoxMultipleChoice(final String id, final List choices)
	{
		super(id, choices);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            See Component
	 * @param renderer
	 *            The rendering engine
	 * @param choices
	 *            The collection of choices in the radio choice
	 * @see wicket.Component#Component(String)
	 * @see AbstractChoice#AbstractChoice(String, java.util.List,wicket.markup.html.form.IChoiceRenderer)
	 */
	public CheckBoxMultipleChoice(final String id, final List choices, final IChoiceRenderer renderer)
	{
		super(id,choices,renderer);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param choices
	 *            The collection of choices in the radio choice
	 * @see wicket.Component#Component(String, wicket.model.IModel)
	 * @see AbstractChoice#AbstractChoice(String, wicket.model.IModel, java.util.List)
	 */
	public CheckBoxMultipleChoice(final String id, IModel model, final List choices)
	{
		super(id, model, choices);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param choices
	 *            The collection of choices in the radio choice
	 * @param renderer
	 *            The rendering engine
	 * @see wicket.Component#Component(String, wicket.model.IModel)
	 * @see AbstractChoice#AbstractChoice(String, wicket.model.IModel, java.util.List,wicket.markup.html.form.IChoiceRenderer)
	 */
	public CheckBoxMultipleChoice(final String id, IModel model, final List choices, final IChoiceRenderer renderer)
	{
		super(id, model, choices,renderer);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            See Component
	 * @param choices
	 *            The collection of choices in the radio choice
	 * @see wicket.Component#Component(String)
	 * @see AbstractChoice#AbstractChoice(String, wicket.model.IModel)
	 */
	public CheckBoxMultipleChoice(String id, IModel choices)
	{
		super(id, choices);
	}

	/**
	 * Constructor
	 * @param id
	 *            See Component
	 * @param model
	 *            The model that is updated with changes in this component. See Component
	 * @param choices
	 *            The collection of choices in the radio choice
	 * @see AbstractChoice#AbstractChoice(String, wicket.model.IModel,wicket.model.IModel)
	 * @see wicket.Component#Component(String, wicket.model.IModel)
	 */
	public CheckBoxMultipleChoice(String id, IModel model, IModel choices)
	{
		super(id, model, choices);
	}
	
	/**
	 * Constructor
	 * 
	 * @param id
	 *            See Component
	 * @param choices
	 *            The collection of choices in the radio choice
	 * @param renderer
	 *            The rendering engine
	 * @see AbstractChoice#AbstractChoice(String, wicket.model.IModel,wicket.markup.html.form.IChoiceRenderer)
	 * @see wicket.Component#Component(String)
	 */
	public CheckBoxMultipleChoice(String id, IModel choices, IChoiceRenderer renderer)
	{
		super(id, choices, renderer);
	}


	/**
	 * Constructor
	 *
	 * @param id
	 *            See Component
	 * @param model
	 *            The model that is updated with changes in this component. See Component
	 * @param choices
	 *            The collection of choices in the radio choice
	 * @param renderer
	 *            The rendering engine
	 * @see wicket.Component#Component(String, wicket.model.IModel)
	 * @see AbstractChoice#AbstractChoice(String, wicket.model.IModel, wicket.model.IModel,wicket.markup.html.form.IChoiceRenderer)
	 */
	public CheckBoxMultipleChoice(String id, IModel model, IModel choices, IChoiceRenderer renderer)
	{
		super(id, model, choices, renderer);
	}


	/**
	 * @return Prefix to use before choice
	 */
	public String getPrefix()
	{
		return prefix;
	}

	/**
	 * @param prefix Prefix to use before choice
	 * @return this
	 */
	public final CheckBoxMultipleChoice setPrefix(final String prefix)
	{
		// Tell the page that this component's prefix was changed
		final Page page = findPage();
		if (page != null)
		{
			addStateChange(new PrefixChange(this.prefix));
		}
		
		this.prefix = prefix;
		return this;
	}
	
	/**
	 * @return Separator to use between radio options
	 */
	public String getSuffix()
	{
		return suffix;
	}

	/**
	 * @param suffix Separator to use between radio options
	 * @return this
	 */
	public  final CheckBoxMultipleChoice setSuffix(final String suffix)
	{
		// Tell the page that this component's suffix was changed
		final Page page = findPage();
		if (page != null)
		{
			addStateChange(new SuffixChange(this.suffix));
		}
		
		this.suffix = suffix;
		return this;
	}
	
	/**
	 * @see wicket.Component#onComponentTagBody(wicket.markup.MarkupStream, wicket.markup.ComponentTag)
	 */
	protected final void onComponentTagBody(final MarkupStream markupStream,
			final ComponentTag openTag)
	{
		// Buffer to hold generated body
		final StringBuffer buffer = new StringBuffer();

		// Iterate through choices
		final List choices = getChoices();

		// Loop through choices
		for (int index=0;index<choices.size();index++)
		{
			// Get next choice
			final Object choice = choices.get(index);

			// Get label for choice
			final String label = getChoiceRenderer().getDisplayValue(choice);

			// If there is a display value for the choice, then we know that the
			// choice is automatic in some way. If label is /null/ then we know
			// that the choice is a manually created checkbox tag at some random
			// location in the page markup!
			if (label != null)
			{
				// Append option suffix
				buffer.append(getPrefix());

				String id = getChoiceRenderer().getIdValue(choice, index);
				// Add checkbox element
				buffer.append("<input name=\"" + getInputName() + "\"" + " type=\"checkbox\""
						+ (isSelected(choice,index) ? " checked" : "") + " value=\"" + id
						+ "\">");

				// Add label for checkbox
				String display = getLocalizer().getString(getId() + "." + label, this, label);
				String escaped = Strings.escapeMarkup(display, false, true);
				buffer.append(escaped);

				// Append option suffix
				buffer.append(getSuffix());
			}
		}

		// Replace body
		replaceComponentTagBody(markupStream, openTag, buffer.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4233.java