error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1712.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1712.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1712.java
text:
```scala
S@@tring name = item.getString("property", "name");

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
package org.columba.addressbook.gui.tree;

import java.util.Enumeration;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.columba.addressbook.config.AddressbookConfig;
import org.columba.addressbook.config.FolderItem;
import org.columba.addressbook.folder.AddressbookTreeNode;
import org.columba.addressbook.folder.IFolder;
import org.columba.addressbook.folder.Root;
import org.columba.addressbook.gui.tree.util.ISelectFolderDialog;
import org.columba.addressbook.gui.tree.util.SelectAddressbookFolderDialog;
import org.columba.addressbook.plugin.FolderPluginHandler;
import org.columba.core.config.DefaultXmlConfig;
import org.columba.core.gui.util.NotifyDialog;
import org.columba.core.plugin.PluginHandlerNotFoundException;
import org.columba.core.plugin.PluginManager;
import org.columba.core.xml.XmlElement;

public class AddressbookTreeModel extends DefaultTreeModel implements TreeModel {

	protected DefaultXmlConfig folderXmlConfig;

	private final Class[] FOLDER_ITEM_ARG = new Class[] { FolderItem.class };

	private static AddressbookTreeModel instance = new AddressbookTreeModel(
			AddressbookConfig.getInstance().get("tree").getElement("/tree"));

	public AddressbookTreeModel(XmlElement root) {
		super(new Root(root));

		createDirectories(((AddressbookTreeNode) getRoot()).getNode(),
				(AddressbookTreeNode) getRoot());
	}

	public static AddressbookTreeModel getInstance() {
		return instance;
	}

	public ISelectFolderDialog getSelectAddressbookFolderDialog() {
		ISelectFolderDialog dialog = new SelectAddressbookFolderDialog(
				this);

		return dialog;
	}

	public void createDirectories(XmlElement parentTreeNode,
			AddressbookTreeNode parentFolder) {
		int count = parentTreeNode.count();

		XmlElement child;

		for (int i = 0; i < count; i++) {
			child = parentTreeNode.getElement(i);

			String name = child.getName();

			if (name.equals("folder")) {
				AddressbookTreeNode folder = add(child, parentFolder);

				if (folder != null) {
					createDirectories(child, folder);
				}
			}
		}
	}

	public AddressbookTreeNode add(XmlElement childNode,
			AddressbookTreeNode parentFolder) {
		FolderItem item = new FolderItem(childNode);

		if (item == null) {
			return null;
		}

		// i18n stuff
		String name = item.get("property", "name");

		//XmlElement.printNode(item.getRoot(), "");
		int uid = item.getInteger("uid");

		if (AddressbookTreeNode.getNextFolderUid() <= uid)
			AddressbookTreeNode.setNextFolderUid(uid + 1);

		// now instanciate the folder classes
		String type = item.get("type");

		Object[] args = { item };

		FolderPluginHandler handler = null;

		try {
			handler = (FolderPluginHandler) PluginManager.getInstance()
					.getHandler("org.columba.addressbook.folder");
		} catch (PluginHandlerNotFoundException ex) {
			NotifyDialog d = new NotifyDialog();
			d.showDialog(ex);
		}

		AddressbookTreeNode folder = null;

		try {
			folder = (AddressbookTreeNode) handler.getPlugin(type, args);
			parentFolder.add(folder);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return folder;

	}

	public AddressbookTreeNode getFolder(int uid) {
		AddressbookTreeNode root = (AddressbookTreeNode) getRoot();

		for (Enumeration e = root.breadthFirstEnumeration(); e
				.hasMoreElements();) {
			AddressbookTreeNode node = (AddressbookTreeNode) e.nextElement();

			if (node instanceof Root) {
				continue;
			}

			int id = node.getUid();

			if (uid == id) {
				return node;
			}
		}

		return null;
	}

	public IFolder getFolder(String name) {
		AddressbookTreeNode root = (AddressbookTreeNode) getRoot();

		for (Enumeration e = root.breadthFirstEnumeration(); e
				.hasMoreElements();) {
			AddressbookTreeNode node = (AddressbookTreeNode) e.nextElement();

			if (node.getName().equals(name))
				return node;

		}

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1712.java