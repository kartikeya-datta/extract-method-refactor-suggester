error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10901.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10901.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10901.java
text:
```scala
p@@ublic static class Input implements IClusterable

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
package org.apache.wicket.examples.compref;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.IClusterable;
import org.apache.wicket.examples.WicketExamplePage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;


/**
 * Page with examples on {@link org.apache.wicket.markup.html.form.DropDownChoice}.
 * 
 * @author Eelco Hillenius
 */
public class DropDownChoicePage extends WicketExamplePage<DropDownChoicePage.Input>
{
	/** available sites for selection. */
	private static final List<String> SITES = Arrays.asList(new String[] { "The Server Side",
			"Java Lobby", "Java.Net" });

	/** available numbers for selection. */
	private static final List<Integer> INTEGERS = Arrays.asList(new Integer[] { 1, 2, 3 });

	/**
	 * Constructor
	 */
	public DropDownChoicePage()
	{
		final Input input = new Input();
		setModel(new CompoundPropertyModel<Input>(input));

		// Add a FeedbackPanel for displaying our messages
		FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
		add(feedbackPanel);

		// Add a form with an onSumbit implementation that sets a message
		Form<?> form = new Form<Void>("form")
		{
			@Override
			protected void onSubmit()
			{
				info("input: " + input);
			}
		};
		add(form);

		// Add a dropdown choice component that uses Input's 'site' property to
		// designate the
		// current selection, and that uses the SITES list for the available
		// options.
		// Note that when the selection is null, Wicket will lookup a localized
		// string to
		// represent this null with key: "id + '.null'". In this case, this is
		// 'site.null'
		// which can be found in DropDownChoicePage.properties
		form.add(new DropDownChoice<String>("site", SITES));

		// Allthough the default behavior of displaying the string
		// representations of the choices
		// by calling toString on the object might be alright in some cases, you
		// usually want to have
		// more control over it. You achieve this by providing an instance of
		// IChoiceRenderer,
		// like the example below. Don't forget to check out the default
		// implementation of
		// IChoiceRenderer, ChoiceRenderer.
		form.add(new DropDownChoice<Integer>("integer", INTEGERS, new IChoiceRenderer<Integer>()
		{
			/**
			 * Gets the display value that is visible to the end user.
			 * 
			 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
			 */
			public Object getDisplayValue(Integer object)
			{
				// Use an ugly switch statement. Usually you would hide this in
				// your business
				// object or in a utility.
				String stringrep;
				int value = object;
				switch (value)
				{
					case 1 :
						stringrep = "One";
						break;
					case 2 :
						stringrep = "Two";
						break;
					case 3 :
						stringrep = "Three";
						break;
					default :
						throw new IllegalStateException(value + " is not mapped!");
				}
				return stringrep;
			}

			/**
			 * Gets the value that is invisible to the end user, and that is used as the selection
			 * id.
			 * 
			 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object,
			 *      int)
			 */
			public String getIdValue(Integer object, int index)
			{
				// though we could do kind of the reverse of what we did in
				// getDisplayValue,
				// just using your index in the list of provided options is
				// usually more
				// convenient
				return String.valueOf(INTEGERS.get(index));
			}
		}));
	}

	/** Simple data class that acts as a model for the input fields. */
	private static class Input implements IClusterable
	{
		/** the selected site. */
		public String site;

		/** the selected integer. */
		public Integer integer = INTEGERS.get(0);

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "site = '" + site + "', integer = " + integer;
		}
	}

	/**
	 * Override base method to provide an explanation
	 */
	@Override
	protected void explain()
	{
		String html = "<select wicket:id=\"site\">\n" + "    <option>site 1</option>\n"
			+ "    <option>site 2</option>\n" + "</select>\n" + "<select wicket:id=\"integer\">\n"
			+ "    <option>Fifty</option>\n" + "    <option>Sixty</option>\n" + "</select>";
		String code = "/** available sites for selection. */\n"
			+ "private static final List SITES = Arrays.asList(new String[] {\"The Server Side\", \"Java Lobby\", \"Java.Net\" });\n"
			+ "/** available numbers for selection. */\n"
			+ "private static final List INTEGERS = Arrays.asList(new Integer[] { new Integer(1), new Integer(2), new Integer(3) });\n"
			+ "\n"
			+ "public DropDownChoicePage() {\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;...\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;// Add a dropdown choice component that uses the model object's 'site' property to designate the\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;// current selection, and that uses the SITES list for the available options.\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;// Note that when the selection is null, Wicket will lookup a localized string to\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;// represent this null with key: \"id + '.null'\". In this case, this is 'site.null'\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;// which can be found in DropDownChoicePage.properties\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;form.add(new DropDownChoice(\"site\", SITES));\n"
			+ "\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;// Allthough the default behavior of displaying the string representations of the choices\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;// by calling toString on the object might be alright in some cases, you usually want to have\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;// more control over it. You achieve this by providing an instance of IChoiceRenderer.\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;// Don't forget to check out the default implementation of IChoiceRenderer, ChoiceRenderer.\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;form.add(new DropDownChoice(\"integer\", INTEGERS, new IChoiceRenderer() {\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; // Gets the display value that is visible to the end user.\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;public String getDisplayValue(Object object) {\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n"
			+ "\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; // Gets the value that is invisble to the end user, and that is used as the selection id.\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;public String getIdValue(Object object, int index) {\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;}));\n" + "}";
		add(new ExplainPanel(html, code));

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10901.java