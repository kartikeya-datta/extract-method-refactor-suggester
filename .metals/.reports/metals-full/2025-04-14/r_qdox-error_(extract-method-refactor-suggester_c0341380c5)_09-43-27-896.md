error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2748.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2748.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2748.java
text:
```scala
r@@eturn new BoundCompoundPropertyModel<T>(super.getListItemModel(model, index));

/*
 * $Id: WebMarkupContainerWithAssociatedMarkup.java,v 1.4 2006/03/04 08:49:02
 * jdonnerstag Exp $ $Revision: 1.1 $ $Date: 2006/03/10 22:31:50 $
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
package wicket.markup.html.list;

import java.util.List;

import wicket.MarkupContainer;
import wicket.model.BoundCompoundPropertyModel;
import wicket.model.IModel;

/**
 * Simple ListVew subclass that wraps its item models in a
 * BoundCompoundPropertyModel. Useful for lists where the item components will
 * be mapped through property expressions.
 * 
 * @param <T>
 *            The type
 * 
 * @author Nathan Hamblen
 */
public abstract class PropertyListView<T> extends ListView<T>
{
	/**
	 * Construct without model, assume bound externally.
	 * 
	 * @param parent
	 *            The parent of this component
	 * 
	 * @param id
	 *            Wicket id
	 */
	public PropertyListView(MarkupContainer parent, final String id)
	{
		super(parent, id);
	}

	/**
	 * Construct with a model.
	 * 
	 * @param parent
	 *            The parent of this component
	 * 
	 * @param id
	 *            Wicket id
	 * @param model
	 *            wrapping a List
	 */
	public PropertyListView(MarkupContainer parent, final String id, final IModel<List<T>> model)
	{
		super(parent, id, model);
	}

	/**
	 * Construct with a "small," unmodeled List. The object can not be detached
	 * and will reside in the session, but is convenient for lists of a limited
	 * size.
	 * 
	 * @param parent
	 *            The parent of this component
	 * 
	 * @param id
	 *            Wicket id
	 * @param list
	 *            unmodeled List
	 */
	public PropertyListView(MarkupContainer parent, final String id, final List<T> list)
	{
		super(parent, id, list);
	}

	/**
	 * Wraps a ListItemModel in a BoundCompoundPropertyModel.
	 * 
	 * @param model
	 * @param index
	 * @return a BoundCompoundPropertyModel wrapping a ListItemModel
	 */
	@Override
	protected IModel<T> getListItemModel(final IModel<List<T>> model, final int index)
	{
		return new BoundCompoundPropertyModel(super.getListItemModel(model, index));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2748.java