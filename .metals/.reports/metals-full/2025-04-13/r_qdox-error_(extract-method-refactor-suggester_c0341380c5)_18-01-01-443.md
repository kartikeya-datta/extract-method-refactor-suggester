error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6578.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6578.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6578.java
text:
```scala
public O@@bject getObject()

/*
 * $Id: MyTree.java 5394 2006-04-16 06:36:52 -0700 (Sun, 16 Apr 2006)
 * jdonnerstag $ $Revision: 5394 $ $Date: 2005-10-02 01:14:57 +0200 (So, 02 Okt
 * 2005) $
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
package wicket.examples.nested;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.Link;
import wicket.markup.html.panel.Panel;
import wicket.markup.html.tree.Tree;
import wicket.model.AbstractReadOnlyModel;
import wicket.model.IModel;

/**
 * Another customized tree implementation, this time with a custom node panel.
 * 
 * @author Eelco Hillenius
 */
public class AnotherTree extends Tree
{
	/**
	 * Custom node panel.
	 */
	public final class Node extends Panel
	{
		/**
		 * Construct.
		 * 
		 * @param parent
		 * @param panelId
		 *            The id of the component
		 * @param node
		 *            The tree node for this panel
		 */
		public Node(MarkupContainer parent, String panelId, final DefaultMutableTreeNode node)
		{
			super(parent, panelId);

			Object userObject = node.getUserObject();

			// create a link for expanding and collapsing the node
			final Link junctionLink = new Link(this, "junctionLink")
			{
				@Override
				public void onClick()
				{
					junctionLinkClicked(node);
				}
			};

			// we make this a proper model instead of just evaluating the
			// string, as we want to have the current value everytime
			// the label is rendered
			IModel junctionLabelModel = new AbstractReadOnlyModel()
			{
				@Override
				public Object getObject(Component component)
				{
					return (!node.isLeaf()) ? (isExpanded(node)) ? "^" : ">" : "";
				}
			};
			String junctionLabel = "";
			if (!node.isLeaf())
			{
				junctionLabel = (isExpanded(node)) ? "[-]" : "[+]";
			}
			new Label(junctionLink, "junctionLabel", junctionLabelModel);

			// create a link for selecting a node
			final Link nodeLink = new Link(this, "nodeLink")
			{
				@Override
				public void onClick()
				{
					nodeLinkClicked(node);
				}
			};
			String label = (userObject instanceof List) ? "" : String.valueOf(node.getUserObject());
			new Label(nodeLink, "label", label);
		}
	}

	/** Log. */
	private static Log log = LogFactory.getLog(AnotherTree.class);

	/**
	 * Construct.
	 * 
	 * @param parent
	 * @param id
	 *            The id of this component
	 * @param model
	 *            the tree model
	 */
	public AnotherTree(MarkupContainer parent, final String id, TreeModel model)
	{
		super(parent, id, model);
	}

	/**
	 * @see wicket.markup.html.tree.Tree#newNodePanel(MarkupContainer, java.lang.String,
	 *      javax.swing.tree.DefaultMutableTreeNode)
	 */
	@Override
	protected Component newNodePanel(MarkupContainer parent, String panelId,
			DefaultMutableTreeNode node)
	{
		return new Node(parent, panelId, node);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6578.java