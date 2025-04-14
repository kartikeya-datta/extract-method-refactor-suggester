error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14697.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14697.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14697.java
text:
```scala
b@@utton.getForm().setModelObject(Objects.clone(originalState));

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
package wicket.extensions.markup.html.repeater.data.table.filter;

import wicket.markup.html.form.Button;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.util.lang.Objects;

/**
 * Filter component that generates a 'go' and 'clear' buttons.
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 */
public class GoAndClearFilter extends GoFilter
{
	private static final long serialVersionUID = 1L;

	protected static final IModel defaultClearModel = new Model("clear");

	private final Button clear;

	private final Object originalState;

	/**
	 * Constructor
	 * 
	 * This constructor will use default models for the 'clear' and 'go' button
	 * labels
	 * 
	 * @param id
	 *            component id
	 * @param form
	 *            filter form of the filter toolbar
	 */
	public GoAndClearFilter(String id, FilterForm form)
	{
		this(id, form, defaultGoModel, defaultClearModel);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            component id
	 * @param form
	 *            filter form of the filter toolbar
	 * @param goModel
	 *            model for the label of the 'go' button
	 * @param clearModel
	 *            model for the label of the 'clear' button
	 */
	public GoAndClearFilter(String id, FilterForm form, IModel goModel, IModel clearModel)
	{
		super(id, goModel);

		originalState = Objects.clone(form.getModelObject());

		clear = new Button("clear", clearModel)
		{
			private static final long serialVersionUID = 1L;

			protected void onSubmit()
			{
				onClearSubmit(this);
			}
		};

		clear.setDefaultFormProcessing(false);

		add(clear);
	}

	/**
	 * @return button component representing the clear button
	 */
	protected Button getClearButton()
	{
		return clear;
	}

	/**
	 * This method should be implemented by subclasses to provide behaviour for
	 * the clear button.
	 * 
	 * @param button
	 *            the 'clear' button
	 * 
	 */
	protected void onClearSubmit(Button button) {
		button.getForm().setModelObject(originalState);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14697.java