error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/274.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/274.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/274.java
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

import wicket.examples.WicketExamplePage;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.CompoundPropertyModel;

/**
 * Page with examples on {@link wicket.markup.html.form.TextField}.
 *
 * @author Eelco Hillenius
 */
public class TextFieldPage extends WicketExamplePage
{
	/**
	 * Constructor
	 */
	public TextFieldPage()
	{
		final Input input = new Input();

		// we use the magical compound property model here as our 'super' model.
		// when components do not have an explicit model, but on of their parents
		// has the compound property model set, it will use that parent's model
		// as the taget for getting and setting the property which is based on
		// its own component id. Thus, component with id 'text' will act as if its
		// model is property 'text' of the compound property model. And as the compound
		// property model's actual object is an instance of 'Input', it will map to
		// Input's 'text' property.
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

		// add a simple text field that uses Input's 'text' property. Nothing can go wrong here
		form.add(new TextField("text"));

		// here we add a text field that uses Input's 'integer' property. Something could go
		// wrong here, as the user's (textual) input might be an invalid value for an
		// Integer object. If we provide the class constructor argument like we do here, we
		// get two things:
		// 1. A type validator is added, so that before any actual updating is tried, first the
		// 		user input is checked for validity. When the user input is wrong for an integer,
		//		the model updating is cancelled, and an error message is displayed to the user
		// 2.When updating the model, the given type is explicitly used instead of Ognl trying
		//		to figure out what type should be converted to.
		// Note that the default validation message mechanism uses resource bundles for the actual
		// message lookup. The message for this component can be found in TextFieldPage.properties
		// with key 'form.integer.TypeValidator'. Read more about how this works in the javadocs
		// of AbstractValidator
		form.add(new TextField("integer", Integer.class));
	}

	/** Simple data class that acts as a model for the input fields. */
	private static class Input implements Serializable
	{
		// Normally we would have played nice and made it a proper JavaBean with getters and
		// setters for its properties. But this is an example which we like to keep small.

		/** some plain text. */
		public String text = "some text";

		/** an integer. */
		public Integer integer = new Integer(12);

		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString()
		{
			return "text = '" + text + "', integer = '" + integer + "'";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/274.java