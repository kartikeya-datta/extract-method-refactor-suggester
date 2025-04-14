error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/108.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/108.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/108.java
text:
```scala
protected L@@ock myLock;

package org.columba.mail.folder;

import java.util.Hashtable;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.columba.core.command.WorkerStatusController;
import org.columba.core.config.AdapterNode;
import org.columba.core.util.Lock;
import org.columba.main.MainInterface;
import org.columba.mail.config.FolderItem;

/**
 * @author freddy
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public abstract class FolderTreeNode
	extends DefaultMutableTreeNode
	implements TreeNodeInterface {

	protected AdapterNode node;
	private Lock myLock;

	public FolderTreeNode(AdapterNode node) {

		super();
		this.node = node;
		myLock = new Lock(this);
	}

	public int getUid() {
		return -1;
	}

	public CapabilityList getSupportedActions() {
		CapabilityList v = CapabilityList.getDefaultFolderCapabilities();

		Hashtable table = getAttributes();

		if (table.contains("accessrights")) {
			String accessrights = (String) table.get("accessrights");

			if (accessrights.equals("user")) {
				v.add(CapabilityList.RENAME_FOLDER_ACTION);
				v.add(CapabilityList.REMOVE_FOLDER_ACTION);
			}
		}

		if (table.contains("messagefolder")) {
			String messagefolder = (String) table.get("messagefolder");

			if (messagefolder.equals("true")) {
				v.add(CapabilityList.FOLDER_SHOW_HEADERLIST_ACTION);
			}
		}

		return v;

	}

	public synchronized boolean tryToGetLock() {
		return myLock.tryToGetLock();
	}
	
	public void releaseLock()
	{
		myLock.release();
	}

	public AdapterNode getNode() {
		return node;
	}

	public String getName() {
		return toString();
	}

	public void setName(String s) {
		this.setUserObject(s);
	}

	public String toString() {
		return (String) this.getUserObject();
	}

	public void insert(Folder newFolder, int newIndex) {

		Folder oldParent = (Folder) newFolder.getParent();
		int oldIndex = oldParent.getIndex(newFolder);
		oldParent.remove(oldIndex);

		AdapterNode oldParentNode = oldParent.getNode();
		AdapterNode newChildNode = newFolder.getNode();
		oldParentNode.removeChild(newChildNode);

		newFolder.setParent(this);
		children.insertElementAt(newFolder, newIndex);

		AdapterNode newParentNode = node;

		int j = -1;
		boolean inserted = false;
		for (int i = 0; i < newParentNode.getChildCount(); i++) {
			AdapterNode n = newParentNode.getChildAt(i);
			String name = n.getName();

			if (name.equals("folder")) {
				j++;
			}

			if (j == newIndex) {
				newParentNode.insert(newChildNode, i);
				inserted = true;
				System.out.println("------> adapternode insert correctly");
			}
		}

		if (inserted == false) {
			if (j + 1 == newIndex) {
				newParentNode.appendChild(newChildNode);
				System.out.println("------> adapternode appended correctly");
			}
		}

		//oldParent.fireTreeNodeStructureUpdate();
		//fireTreeNodeStructureUpdate();
	}

	public void removeFromParent() {
		AdapterNode childAdapterNode = getNode();
		childAdapterNode.remove();

		super.removeFromParent();
	}

	public void removeFolder() throws Exception {
		removeFromParent();
	}

	public void remove(FolderTreeNode childNode) {
		FolderTreeNode childFolder = (FolderTreeNode) childNode;
		AdapterNode childAdapterNode = childFolder.getNode();
		childAdapterNode.remove();

		int index = getIndex(childFolder);
		children.removeElementAt(index);
		//fireTreeNodeStructureUpdate();

		//return childFolder;
	}

	public abstract Folder instanceNewChildNode(
		AdapterNode node,
		FolderItem item);

	public abstract Hashtable getAttributes();

	public abstract void createChildren(WorkerStatusController worker);

	public FolderTreeNode addSubFolder(Hashtable attributes) throws Exception {
		AdapterNode node = MainInterface.treeModel.addFolder(this, attributes);
		FolderItem item = MainInterface.treeModel.getItem(node);

		Folder subFolder = instanceNewChildNode(node, item);
		add(subFolder);

		return subFolder;
	}

	public boolean addFolder(String name) throws Exception {
		Hashtable attributes = getAttributes();
		attributes.put("name", name);

		FolderTreeNode folder = addSubFolder(attributes);
		return true;
	}

	/*
	public void removeFromParent()
	{
		
		Folder parentFolder = (Folder) getParent();
		parentFolder.remove(this);
		
	}
	*/

	/*
	public void moveUp()
	{
		Folder parentFolder = (Folder) getParent();
		if (parentFolder == null)
			return;
	
		AdapterNode parentNode = parentFolder.getNode();
	
		int childCount = parentFolder.getChildCount();
	
		int childIndex = parentFolder.getIndex((Folder) this);
		if (childIndex == -1)
			return;
		if (childIndex == 0)
			return;
	
		parentFolder.insert(this, childIndex - 1);
	
	}
	*/

	/*
	public void moveDown()
	{
		Folder parentFolder = (Folder) getParent();
		if (parentFolder == null)
			return;
	
		AdapterNode parentNode = parentFolder.getNode();
	
		int childCount = parentFolder.getChildCount();
	
		int childIndex = parentFolder.getIndex((Folder) this);
		if (childIndex == -1)
			return;
		if (childIndex >= childCount - 1)
			return;
	
		parentFolder.insert(this, childIndex + 1);
	}
	*/

	public TreeNode getChild(String str) {
		for (int i = 0; i < getChildCount(); i++) {
			Folder child = (Folder) getChildAt(i);
			String name = child.getName();

			if (name.equalsIgnoreCase(str))
				return child;
		}
		return null;
	}

	public void append(Folder newFolder) {
		Folder oldParent = (Folder) newFolder.getParent();
		int oldIndex = oldParent.getIndex(newFolder);
		oldParent.remove(oldIndex);

		AdapterNode oldParentNode = oldParent.getNode();
		AdapterNode newChildNode = newFolder.getNode();
		oldParentNode.removeChild(newChildNode);

		newFolder.setParent(this);
		children.add(newFolder);

		AdapterNode newParentNode = node;
		newParentNode.appendChild(newChildNode);

		// oldParent.fireTreeNodeStructureUpdate();
		// fireTreeNodeStructureUpdate();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/108.java