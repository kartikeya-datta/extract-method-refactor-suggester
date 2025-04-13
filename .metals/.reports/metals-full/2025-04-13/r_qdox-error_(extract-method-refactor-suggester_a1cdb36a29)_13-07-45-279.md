error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/268.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/268.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/268.java
text:
```scala
F@@orm form = new Form("form")

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.examples.compref;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import wicket.examples.WicketExamplePage;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.CompoundPropertyModel;

/**
 * Page with examples on {@link wicket.markup.html.form.DropDownChoice}.
 *
 * @author Eelco Hillenius
 */
public class DropDownChoicePage extends WicketExamplePage
{
	/** available sites for selection. */
	private static final List SITES = Arrays.asList(new String[] { 
	        "The Server Side", "Java Lobby", "Java.Net" });

	/** available numbers for selection. */
	private static final List INTEGERS = Arrays.asList(new Integer[] { 
	        new Integer(1), new Integer(2), new Integer(3) });

	/**
	 * Constructor
	 */
	public DropDownChoicePage()
	{
		final Input input = new Input();
		setModel(new CompoundPropertyModel(input));

		// Add a FeedbackPanel for displaying our messages
		FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
		add(feedbackPanel);

		// Add a form with an onSumbit implementation that sets a message
		Form form = new Form("form", feedbackPanel)
		{
			protected void onSubmit()
			{
				info("input: " + input);
			}
		};
		add(form);

		// Add a dropdown choice component that uses Input's 'site' property to designate the
		// current selection, and that uses the SITES list for the available options.
		// Note that when the selection is null, Wicket will lookup a localized string to
		// represent this null with key: "id + '.null'". In this case, this is 'site.null'
		// which can be found in DropDownChoicePage.properties
		form.add(new DropDownChoice("site", SITES));

		// Allthough the default behaviour of displaying the string representations of the choices
		// by calling toString on the might be allright in some case, you usually want to have
		// more control over it. You achieve this by providing an instance of IChoiceRenderer,
		// like the example below. Don't forget to check out the default implementation of
		// IChoiceRenderer, ChoiceRenderer.
		form.add(new DropDownChoice("integer", INTEGERS, new IChoiceRenderer()
		{
			/**
			 * Gets the display value that is visible to the end user.
			 * @see wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
			 */
			public String getDisplayValue(Object object)
			{
				// Use an ugly switch statement. Usually you would hide this in your business
				// object or in a utility.
				String stringrep;
				int value = ((Integer)object).intValue();
				switch (value)
				{
					case 1: stringrep = "One"; break;
					case 2: stringrep = "Two"; break;
					case 3: stringrep = "Three"; break;
					default: throw new IllegalStateException(value + " is not mapped!");
				}
				return stringrep;
			}

			/**
			 * Gets the value that is invisble to the end user, and that is used
			 * as the selection id.
			 * @see wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object, int)
			 */
			public String getIdValue(Object object, int index)
			{
				// though we could do kind of the reverse of what we did in getDisplayValue,
				// just using your index in the list of provided options is usually more
				// convenient
				return String.valueOf(INTEGERS.get(index));
			}
		}));
	}

	/** Simple data class that acts as a model for the input fields. */
	private static class Input implements Serializable
	{
		/** the selected site. */
		public String site;

		/** the selected integer. */
		public Integer integer = (Integer)INTEGERS.get(0);

		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString()
		{
			return "site = '" + site + "', integer = " + integer;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/268.java