error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8847.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8847.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8847.java
text:
```scala
public I@@mageIcon getIcon() {

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.addressbook.folder;

import java.lang.reflect.Method;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import org.columba.addressbook.config.FolderItem;
import org.columba.api.command.IWorkerStatusController;
import org.columba.api.plugin.IExtensionInterface;
import org.columba.core.base.Lock;
import org.columba.core.resourceloader.IconKeys;
import org.columba.core.resourceloader.ImageLoader;
import org.columba.core.xml.XmlElement;

public abstract class AddressbookTreeNode extends DefaultMutableTreeNode
		implements IFolder, IExtensionInterface {
	protected final static ImageIcon expandedIcon = ImageLoader
			.getSmallIcon(IconKeys.FOLDER_OPEN);

	private static int nextFolderUid = 0;

	protected ImageIcon collapsedIcon = ImageLoader
			.getSmallIcon(IconKeys.FOLDER);

	protected FolderItem node;

	protected Lock myLock;

	private final Class[] FOLDER_ITEM_ARG = new Class[] { FolderItem.class };

	public AddressbookTreeNode(String name) {
		super(name);

	}

	public AddressbookTreeNode(FolderItem item) {
		super();

		setNode(item);

	}

	public static Object generateNextFolderUid() {
		return new Integer(++nextFolderUid);
	}

	/**
	 * @param nextUid
	 *            The nextUid to set.
	 */
	public static void setNextFolderUid(int uid) {
		nextFolderUid = uid;
	}

	/**
	 * @return Returns the nextUid.
	 */
	public static int getNextFolderUid() {
		return nextFolderUid;
	}

	/*
	 * public AddressbookTreeNode(String name) { super(name); this.name = name; }
	 */
	public FolderItem getFolderItem() {
		return node;
	}

	public ImageIcon getCollapsedIcon() {
		return collapsedIcon;
	}

	public final static FolderItem getDefaultItem(String className,
			XmlElement props) {
		XmlElement defaultElement = new XmlElement("folder");
		defaultElement.addAttribute("class", className);
		defaultElement.addAttribute("uid", Integer.toString(nextFolderUid++));

		if (props != null) {
			defaultElement.addElement(props);
		}

		// FAILURE!!!

		/*
		 * XmlElement filter = new XmlElement("filter");
		 * defaultElement.addElement(filter);
		 */
		return new FolderItem(defaultElement);
	}

	/*
	 * public TreePath getSelectionTreePath() { //TreeNodeList list = new
	 * TreeNodeList( getTreePath() ); TreeNode[] treeNodes = getPathToRoot(this,
	 * 0); TreePath path = new TreePath(treeNodes[0]);
	 * 
	 * for (int i = 1; i < treeNodes.length; i++) { Folder folder = (Folder)
	 * treeNodes[i]; path.pathByAddingChild(folder); }
	 * 
	 * return path; }
	 */

	public static XmlElement getDefaultProperties() {
		return null;
	}


	public ImageIcon getExpandedIcon() {
		return expandedIcon;
	}

	/*
	 * public void setName(String s) { name = s; } public String getName() {
	 * return name; }
	 */
	public int getUid() {
		return node.getInteger("uid");
	}

	public boolean tryToGetLock() {
		return myLock.tryToGetLock(null);
	}

	public void releaseLock() {
		myLock.release(null);
	}

	/**
	 * Returns the node.
	 * 
	 * @return FolderItem
	 */
	public XmlElement getNode() {
		return node.getRoot();
	}

	/**
	 * Sets the node.
	 * 
	 * @param node
	 *            The node to set
	 */
	public void setNode(FolderItem node) {
		this.node = node;
	}

	// public abstract Class getDefaultChild();
	public Class getDefaultChild() {
		return null;
	}

	final public Hashtable getAttributes() {
		return node.getElement("property").getAttributes();
	}

	public abstract void createChildren(IWorkerStatusController worker);

	public void addFolder(String name, Class childClass) throws Exception {
		Method m_getDefaultProperties = childClass.getMethod(
				"getDefaultProperties", null);

		XmlElement props = (XmlElement) m_getDefaultProperties.invoke(null,
				null);
		FolderItem childNode = getDefaultItem(childClass.getName(), props);

		childNode.setString("property", "name", name);

		// Put properties that should be copied from parent here
		AbstractFolder subFolder = (AbstractFolder) childClass.getConstructor(
				FOLDER_ITEM_ARG).newInstance(new Object[] { childNode });
		addWithXml(subFolder);
	}

	public void addFolder(String name) throws Exception {
		addFolder(name, getDefaultChild());
	}

	public void addWithXml(AddressbookTreeNode folder) {
		add(folder);
		this.getNode().addElement(folder.getNode());
	}

	/**
	 * @see javax.swing.tree.MutableTreeNode#removeFromParent()
	 */
	public void removeFromParent() {

		// remove xml config
		getNode().removeFromParent();

		// remove node
		super.removeFromParent();
	}

	public String getName() {
		String name = null;

		FolderItem item = getFolderItem();
		name = item.getString("property", "name");

		return name;
	}

	/**
	 * @see org.columba.modules.mail.folder.FolderTreeNode#setName(String)
	 */
	public void setName(String newName) {
		FolderItem item = getFolderItem();
		item.setString("property", "name", newName);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8847.java