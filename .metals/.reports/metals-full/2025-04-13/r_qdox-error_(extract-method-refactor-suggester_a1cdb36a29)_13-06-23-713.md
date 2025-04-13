error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6888.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6888.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6888.java
text:
```scala
public C@@omponent getFilter(MarkupContainer parent, String componentId, FilterForm form)

package wicket.extensions.markup.html.repeater.data.table.filter;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.model.IModel;
import wicket.model.PropertyModel;

/**
 * A filtered property column that creates a textfield filter component. The
 * default model of the created textfield is a property model with the same
 * property expression as the one used to display data. This works well when the
 * filter state object is of the same type as the objects in the data table.
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 */
public class ChoiceFilteredPropertyColumn extends FilteredPropertyColumn
{
	private static final long serialVersionUID = 1L;
	private IModel filterChoices;

	/**
	 * @param displayModel
	 * @param sortProperty
	 * @param propertyExpression
	 * @param filterChoices
	 *            collection choices used in the choice filter
	 */
	public ChoiceFilteredPropertyColumn(IModel displayModel, String sortProperty,
			String propertyExpression, IModel filterChoices)
	{
		super(displayModel, sortProperty, propertyExpression);
		this.filterChoices = filterChoices;
	}

	/**
	 * @param displayModel
	 * @param propertyExpression
	 * @param filterChoices
	 *            collection of choices used in the choice filter
	 */
	public ChoiceFilteredPropertyColumn(IModel displayModel, String propertyExpression,
			IModel filterChoices)
	{
		super(displayModel, propertyExpression);
		this.filterChoices = filterChoices;
	}

	/**
	 * @see wicket.extensions.markup.html.repeater.data.table.filter.IFilteredColumn#getFilter(java.lang.String,
	 *      wicket.extensions.markup.html.repeater.data.table.filter.FilterForm)
	 */
	public Component getFilter(MarkupContainer<?> parent, String componentId, FilterForm form)
	{
		ChoiceFilter filter = new ChoiceFilter(parent,componentId, getFilterModel(form), form,
				filterChoices, enableAutoSubmit());

		IChoiceRenderer renderer = getChoiceRenderer();
		if (renderer != null)
		{
			filter.getChoice().setChoiceRenderer(renderer);
		}
		return filter;
	}

	/**
	 * Returns the model that will be passed on to the text filter. Users can
	 * override this method to change the model.
	 * 
	 * @param form
	 *            filter form
	 * @return model passed on to the text filter
	 */
	protected IModel getFilterModel(FilterForm form)
	{
		return new PropertyModel(form.getModel(), getPropertyExpression());
	}

	/**
	 * Returns true if the constructed choice filter should autosubmit the form
	 * when its value is changed.
	 * 
	 * @return true to make choice filter autosubmit, false otherwise
	 */
	protected boolean enableAutoSubmit()
	{
		return true;
	}

	/**
	 * Returns choice renderer that will be used to create the choice filter
	 * 
	 * @return choice renderer that will be used to create the choice filter
	 */
	protected IChoiceRenderer getChoiceRenderer()
	{
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6888.java