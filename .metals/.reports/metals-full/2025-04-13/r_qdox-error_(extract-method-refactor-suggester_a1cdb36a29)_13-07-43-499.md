error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1045.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1045.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1045.java
text:
```scala
public I@@Renderable createCell(TreeNode node, int level)

package wicket.xtree.table;

import javax.swing.tree.TreeNode;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.Response;
import wicket.util.string.Strings;

/**
 * Convenience class for creating non-interactive lightweight (IRenderable based)
 * columns.
 * 
 * @author Matej Knopp
 */
public abstract class AbstractRenderableColumn extends AbstractColumn {
	
	private boolean escapeContent = true;
	private boolean contentAsTooltip = false;

	/**
	 * Creates the column
	 * 
	 * @param location
	 *			Specifies how the column should be aligned and what his size should be 			
	 * 
	 * @param header
	 * 			Header caption
	 */
	public AbstractRenderableColumn(ColumnLocation location, String header) 
	{
		super(location, header);
	}

	/**
	 * @see IColumn#createCell(TreeTable, TreeNode, int)
	 */
	public IRenderable createCell(TreeTable treeTable, TreeNode node, int level) 
	{
		return new IRenderable() 
		{
			public void render(TreeNode node, Response response) 
			{				
				String content = getNodeValue(node);
				
				// escape if necessary
				if (isEscapeContent())
				{
					content = Strings.escapeMarkup(content).toString();
				}
				
				response.write("<span class=\"text\"");
				if (isContentAsTooltip())
				{
					response.write(" title=\"" + content + "\"");
				}
				response.write(">");
				response.write(content);
				response.write("</span>");
			}			
		};
	}
	
	/**
	 * @see IColumn#createCell(MarkupContainer, String, TreeNode, int)
	 */
	public Component createCell(MarkupContainer<?> parent, String id, TreeNode node, int level) 
	{
		return null;
	}

	/**
	 * Returns the string value for this column.
	 * 
	 * @param node
	 * 			Determines the position in tree
	 * 
	 */
	public abstract String getNodeValue(TreeNode node);
	
	/**
	 * Sets whether the special html characters of content should be escaped. 
	 */
	public void setEscapeContent(boolean escapeContent) 
	{
		this.escapeContent = escapeContent;
	}

	/**
	 * Returns whether the special html characters of content will be escaped.
	 */
	public boolean isEscapeContent() 
	{
		return escapeContent;
	}

	/**
	 * Sets whether the content should also be visible as tooltip (html title attribute)
	 * of the cell.
	 */
	public void setContentAsTooltip(boolean contentAsTooltip) {
		this.contentAsTooltip = contentAsTooltip;
	}

	/**
	 * Returns whether the content should also be visible as tooltip of the cell.
	 */
	public boolean isContentAsTooltip() {
		return contentAsTooltip;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1045.java