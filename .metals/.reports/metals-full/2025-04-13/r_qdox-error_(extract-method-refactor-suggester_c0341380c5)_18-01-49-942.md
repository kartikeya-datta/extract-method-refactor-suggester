error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/196.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/196.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/196.java
text:
```scala
c@@hildNode.setEnabled(Boolean.valueOf(enabled).booleanValue());

/*
 * Created on 06.08.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.columba.core.gui.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultTreeModel;

import org.columba.core.gui.util.treetable.Tree;
import org.columba.core.gui.util.treetable.TreeTable;
import org.columba.core.main.MainInterface;
import org.columba.core.xml.XmlElement;

/**
 * TreeTable component responsible for displaying plugins in a categorized
 * way.
 * 
 * Additionally shows plugin version information, the plugin description as
 * tooltip. 
 * 
 * The third column is a checkbox to enable/disable the plugin.
 *
 * @author fdietz
 */
public class PluginTree extends TreeTable {

	final static String[] columns = { "Description", "Version", "Enabled" };

	final static String[] CATEGORIES =
		{
			"Look and Feel",
			"Filter",
			"Filter Action",
			"Spam",
			"Mail Import",
			"Addressbook Import",
			"Interpreter Language",
			"Examples",
			"Uncategorized" };

	protected Map map;
	protected PluginTreeTableModel model;
	
	private JCheckBox enabledCheckBox;

	/**
	 * 
	 */
	public PluginTree() {
		super();

		map = new HashMap();

		model = new PluginTreeTableModel(columns);
		model.setTree((Tree) getTree());
		((DefaultTreeModel) model.getTree().getModel()).setAsksAllowsChildren(
			true);

		initTree();

		setModel(model);

		getTree().setCellRenderer(new DescriptionTreeRenderer());

		// make "version" column fixed size
		TableColumn tc = getColumn(columns[1]);
		tc.setCellRenderer(new VersionRenderer());
		tc.setMaxWidth(80);
		tc.setMinWidth(80);

		
		// make "enabled" column fixed size
		tc = getColumn(columns[2]);
		tc.setCellRenderer(new EnabledRenderer());
		tc.setCellEditor(new EnabledEditor());		
		
		tc.setMaxWidth(80);
		tc.setMinWidth(80);
		
	}

	public void addPlugin(XmlElement pluginElement) {
		//		plugin wasn't correctly loaded
		if (pluginElement == null)
			return;

		String category = pluginElement.getAttribute("category");
		if (category == null) {
			// this plugin doesn't define a category to which it belongs
			category = "Uncategorized";
		}

		PluginNode childNode = new PluginNode();
		childNode.setCategory(false);
		childNode.setId(pluginElement.getAttribute("id"));
		childNode.setTooltip(pluginElement.getAttribute("description"));
		childNode.setVersion(pluginElement.getAttribute("version"));
		String enabled = pluginElement.getAttribute("enabled");
		if (enabled == null)
			enabled = "true";
		childNode.setEnabled(new Boolean(enabled).booleanValue());

		System.out.println("adding plugin to table: "+enabled);
		
		PluginNode node = (PluginNode) map.get(category);
		if (node == null) {
			// unknown category found 
			// -> just add this plugin to "Uncategorized"
			category = "Uncategorized";
			node = (PluginNode) map.get(category);
		}

		node.add(childNode);
		
		// notify table
		model.fireTableDataChanged();
	}

	public void initTree() {
		PluginNode root = new PluginNode();
		root.setId("root");

		initCategories(root);

		List list = MainInterface.pluginManager.getIds();
		ListIterator it = list.listIterator();
		while (it.hasNext()) {
			// plugin id
			String id = (String) it.next();

			XmlElement pluginElement =
				MainInterface.pluginManager.getPluginElement(id);
	
			addPlugin(pluginElement);		
		}

		model.set(root);

	}

	protected void initCategories(PluginNode root) {
		for (int i = 0; i < CATEGORIES.length; i++) {
			String c = CATEGORIES[i];
			PluginNode node = new PluginNode();
			node.setAllowsChildren(true);
			node.setId(c);
			node.setEnabled(true);
			node.setCategory(true);
			root.add(node);
			map.put(c, node);
		}
	}

	public void removePluginNode(PluginNode node) {
		// notify tree
		node.removeFromParent();

		// notify table
		model.fireTableDataChanged();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/196.java