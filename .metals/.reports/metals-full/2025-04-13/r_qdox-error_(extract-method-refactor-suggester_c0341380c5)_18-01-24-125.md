error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3880.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3880.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3880.java
text:
```scala
private S@@tring listAsString(final List list)

/*
 * $Id$ $Revision$ $Date$
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.examples.compref;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import wicket.examples.WicketExamplePage;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.ListMultipleChoice;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.CompoundPropertyModel;

/**
 * Page with examples on {@link wicket.markup.html.form.ListMultipleChoice}.
 * 
 * @author Eelco Hillenius
 */
public class ListMultipleChoicePage extends WicketExamplePage
{
	/** available sites for selection. */
	private static final List<String> SITES = Arrays.asList(new String[] { "The Server Side", "Java Lobby",
			"Java.Net" });

	/** available choices for large selection box. */
	private static final List<String> MANY_CHOICES = Arrays.asList(new String[] { "Choice1", "Choice2",
			"Choice3", "Choice4", "Choice5", "Choice6", "Choice7", "Choice8", "Choice9", });

	/**
	 * Constructor
	 */
	public ListMultipleChoicePage()
	{
		final Input input = new Input();
		setModel(new CompoundPropertyModel<Input>(input));

		// Add a FeedbackPanel for displaying our messages
		new FeedbackPanel(this, "feedback");

		// Add a form with an onSubmit implementation that sets a message
		Form form = new Form(this, "form")
		{
			@Override
			protected void onSubmit()
			{
				info("input: " + input);
			}
		};

		// Add a multiple list choice component that uses the model object's
		// 'site'
		// property to designate the current selection, and that uses the SITES
		// list for the available options.
		// Note that our model here holds a Collection, as we need to store
		// multiple values too
		new ListMultipleChoice<String>(form, "sites", SITES);

		new ListMultipleChoice<String>(form, "choices", MANY_CHOICES).setMaxRows(5);
	}

	/** Simple data class that acts as a model for the input fields. */
	private static class Input implements Serializable
	{
		/** the selected sites. */
		public List<String> sites = new ArrayList<String>();

		/** the selected choices. */
		public List<String> choices = new ArrayList<String>();

		/** adds pre-selected items to the choices list */
		public Input()
		{
			choices.add("Choice3");
			choices.add("Choice4");
			choices.add("Choice5");
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "sites = '" + listAsString(sites) + "', choices='" + listAsString(choices) + "'";
		}

		private String listAsString(List list)
		{
			StringBuffer b = new StringBuffer();
			for (Iterator i = list.iterator(); i.hasNext();)
			{
				b.append(i.next());
				if (i.hasNext())
				{
					b.append(", ");
				}
			}
			return b.toString();
		}
	}

	/**
	 * Override base method to provide an explanation
	 */
	@Override
	protected void explain()
	{
		String html = "<select wicket:id=\"sites\">\n" + "    <option>site 1</option>\n"
				+ "    <option>site 2</option>\n" + "</select>\n"
				+ "<select wicket:id=\"choices\">\n" + "    <option>choice 1</option>\n"
				+ "    <option>choice 2</option>\n" + "</select>";
		String code = "&nbsp;&nbsp;&nbsp;&nbsp;// Add a multiple list choice component that uses the model object's 'site'\n"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;// property to designate the current selection, and that uses the SITES\n"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;// list for the available options.\n"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;// Note that our model here holds a Collection, as we need to store\n"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;// multiple values too\n"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;ListMultipleChoice siteChoice = new ListMultipleChoice(\"sites\", SITES);\n"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;form.add(siteChoice);\n"
				+ "\n"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;ListMultipleChoice manyChoice = new ListMultipleChoice(\"choices\", MANY_CHOICES).setMaxRows(5);\n"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;form.add(manyChoice);";
		new ExplainPanel(this, html, code);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3880.java