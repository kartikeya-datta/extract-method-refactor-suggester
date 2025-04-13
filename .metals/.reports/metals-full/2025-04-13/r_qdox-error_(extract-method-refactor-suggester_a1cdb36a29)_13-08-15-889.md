error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5956.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5956.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5956.java
text:
```scala
public v@@oid onSubmit()

/*
 * $Id: org.eclipse.jdt.ui.prefs 5004 2006-03-17 20:47:08 -0800 (Fri, 17 Mar
 * 2006) eelco12 $ $Revision$ $Date: 2006-03-17 20:47:08 -0800 (Fri, 17
 * Mar 2006) $
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
package wicket.util.tester.apps_3;

import java.util.ArrayList;
import java.util.List;

import wicket.markup.html.WebPage;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.Check;
import wicket.markup.html.form.CheckBoxMultipleChoice;
import wicket.markup.html.form.CheckGroup;
import wicket.markup.html.form.ChoiceRenderer;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.ListChoice;
import wicket.markup.html.form.ListMultipleChoice;
import wicket.markup.html.form.Radio;
import wicket.markup.html.form.RadioChoice;
import wicket.markup.html.form.RadioGroup;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.model.CompoundPropertyModel;
import wicket.model.Model;
import wicket.util.tester.apps_1.Book;

/**
 * @author Ingram Chen
 */
public class ChoicePage extends WebPage
{
	private static final long serialVersionUID = 1L;

	/** test DropDownChoice */
	public Book dropDownChoice;
	
	/** test ListChoice */
	public Book listChoice;
	
	/** test RadioChoice */
	public Book radioChoice;

	/** test RadioChoice */
	public Book radioGroup;

	/** test CheckBoxMultipleChoice */
	public List checkBoxMultipleChoice = new ArrayList();

	/** test CheckGroup */
	public List checkGroup = new ArrayList();
	
	/** test ListMultipleChoice */
	public List listMultipleChoice = new ArrayList();

	/** test multiple button */
	public boolean anotherButtonPressed;

	/**
	 * Test page for FormTester.select()
	 * 
	 * @param candidateChoices
	 */
	public ChoicePage(List candidateChoices)
	{
		ChoiceRenderer bookChoiceRenderer = new ChoiceRenderer("name", "id");

		Form form = new Form("choiceForm");
		add(form);

		form.setModel(new CompoundPropertyModel(this));

		// single select family
		form.add(new DropDownChoice("dropDownChoice", candidateChoices, bookChoiceRenderer));
		form.add(new ListChoice("listChoice", candidateChoices, bookChoiceRenderer).setMaxRows(4));
		form.add(new RadioChoice("radioChoice", candidateChoices, bookChoiceRenderer));
		form.add(newRadioGroup(candidateChoices));

		// mulitple select family
		form.add(new ListMultipleChoice("listMultipleChoice", candidateChoices, bookChoiceRenderer)
				.setMaxRows(4));
		form.add(new CheckBoxMultipleChoice("checkBoxMultipleChoice", candidateChoices,
				bookChoiceRenderer));
		form.add(newCheckGroup(candidateChoices));
		form.add(new Button("anotherButton")
		{
			private static final long serialVersionUID = 1L;

			protected void onSubmit()
			{
				anotherButtonPressed = true;
			}
		});
	}

	private CheckGroup newCheckGroup(List candidateChoices)
	{
		CheckGroup checkGroupComponent = new CheckGroup("checkGroup");
		ListView listView = new ListView("loop", candidateChoices)
		{
			private static final long serialVersionUID = 1L;

			protected void populateItem(ListItem item)
			{
				item.add(new Check("check", new Model((Book)item.getModelObject())));
			}

		};
		checkGroupComponent.add(listView);
		return checkGroupComponent;
	}

	private RadioGroup newRadioGroup(List candidateChoices)
	{
		RadioGroup radioGroupComponent = new RadioGroup("radioGroup");
		ListView listView = new ListView("loop", candidateChoices)
		{
			private static final long serialVersionUID = 1L;

			protected void populateItem(ListItem item)
			{
				item.add(new Radio("radio", new Model((Book)item.getModelObject())));
			}

		};
		radioGroupComponent.add(listView);
		return radioGroupComponent;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5956.java