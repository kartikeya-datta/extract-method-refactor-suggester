error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3329.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3329.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3329.java
text:
```scala
s@@etBorderBodyContainer(link);

/*
 * $Id: OrderByBorder.java 5840 2006-05-24 20:49:09 +0000 (Wed, 24 May 2006)
 * joco01 $ $Revision$ $Date: 2006-05-24 20:49:09 +0000 (Wed, 24 May
 * 2006) $
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
package wicket.extensions.markup.html.repeater.data.sort;

import wicket.MarkupContainer;
import wicket.markup.IAlternateParentProvider;
import wicket.markup.html.border.Border;

/**
 * A component that wraps markup with an OrderByLink. This has the advantage of
 * being able to add the attribute modifier to the wrapping element as opposed
 * to the link, so that it can be attached to &lt;th&gt; or any other element.
 * 
 * For example:
 * 
 * &lt;th wicket:id="order-by-border"&gt;Heading&lt;/th&gt;
 * 
 * 
 * @author Igor Vaynberg ( ivaynberg )
 * 
 */
public class OrderByBorder extends Border implements IAlternateParentProvider
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param parent
	 *            The parent of this component The parent of this component.
	 * @param id
	 *            see
	 *            {@link OrderByLink#OrderByLink(MarkupContainer, String, String, ISortStateLocator, OrderByLink.ICssProvider) }
	 * @param property
	 *            see
	 *            {@link OrderByLink#OrderByLink(MarkupContainer, String, String, ISortStateLocator, OrderByLink.ICssProvider) }
	 * @param stateLocator
	 *            see
	 *            {@link OrderByLink#OrderByLink(MarkupContainer, String, String, ISortStateLocator, OrderByLink.ICssProvider) }
	 * @param cssProvider
	 *            see
	 *            {@link OrderByLink#OrderByLink(MarkupContainer, String, String, ISortStateLocator, OrderByLink.ICssProvider) }
	 */
	public OrderByBorder(MarkupContainer parent, final String id, String property,
			ISortStateLocator stateLocator, OrderByLink.ICssProvider cssProvider)
	{
		super(parent, id);
		
		OrderByLink link = new OrderByLink(this, "orderByLink", property, stateLocator,
				OrderByLink.VoidCssProvider.getInstance())
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged()
			{
				OrderByBorder.this.onSortChanged();
			}
		};
		newBorderBodyContainer(link);
		
		add(new OrderByLink.CssModifier(link, cssProvider));
	}
	
	/**
	 * This method is a hook for subclasses to perform an action after sort has
	 * changed
	 */
	protected void onSortChanged()
	{
		// noop
	}

	/**
	 * @param parent
	 *            The parent of this component The parent of this component.
	 * @param id
	 *            see
	 *            {@link OrderByLink#OrderByLink(MarkupContainer, String, String, ISortStateLocator)}
	 * @param property
	 *            see
	 *            {@link OrderByLink#OrderByLink(MarkupContainer, String, String, ISortStateLocator)}
	 * @param stateLocator
	 *            see
	 *            {@link OrderByLink#OrderByLink(MarkupContainer, String, String, ISortStateLocator)}
	 */
	public OrderByBorder(MarkupContainer parent, final String id, String property,
			ISortStateLocator stateLocator)
	{
		this(parent, id, property, stateLocator, OrderByLink.DefaultCssProvider.getInstance());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3329.java