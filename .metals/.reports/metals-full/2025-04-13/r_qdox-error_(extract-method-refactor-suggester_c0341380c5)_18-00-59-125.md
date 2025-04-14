error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/637.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/637.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/637.java
text:
```scala
s@@uper("mail.table");

//The contents of this file are subject to the Mozilla Public License Version 1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.

package org.columba.mail.gui.table;

import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.columba.core.command.DefaultCommandReference;
import org.columba.core.gui.util.SelectionHandler;
import org.columba.core.logging.ColumbaLogger;
import org.columba.mail.command.FolderCommandReference;
import org.columba.mail.folder.Folder;
import org.columba.mail.gui.table.util.MessageNode;

public class HeaderTableSelectionHandler extends SelectionHandler implements TreeSelectionListener {

	private TableView view;
	private LinkedList messages;
	private Folder folder;

	private final static MessageNode[] messageNodeArray = {null}; 

	/**
	 * @param id
	 */
	public HeaderTableSelectionHandler(TableView view) {
		super("mail.headertable");
		this.view = view;
		view.addTreeSelectionListener(this);
		
		messages = new LinkedList();
	}

	/* (non-Javadoc)
	 * @see org.columba.core.gui.util.SelectionHandler#getSelection()
	 */
	public DefaultCommandReference[] getSelection() {
		FolderCommandReference[] references =
			new FolderCommandReference[1];
		
		references[0] = new FolderCommandReference(folder,getUidArray());
		
		return references;
	}

	/* (non-Javadoc)
	 * @see org.columba.core.gui.util.SelectionHandler#setSelection(org.columba.core.command.DefaultCommandReference[])
	 */
	public void setSelection(DefaultCommandReference[] selection) {
		FolderCommandReference ref = (FolderCommandReference) selection[0];

		Object[] uids = ref.getUids();

		view.clearSelection();
		view.requestFocus();

		TreePath path[] = new TreePath[uids.length];
		
		/*
		for (int i = 0; i < uids.length; i++) {
			path[i] =
					view.getMessagNode(uids[i]).getSelectionTreePath();
			view.setLeadSelectionPath(path[i]);
			view.setAnchorSelectionPath(path[i]);
			view.expandPath(path[i]);
		}

		view.setSelectionPaths(path);
		 */

	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) {
		for (int i = 0; i < e.getPaths().length; i++) {
			if (e.getPaths()[i].getLastPathComponent() instanceof MessageNode) {
				MessageNode message = (MessageNode) e.getPaths()[i].getLastPathComponent();
				if (e.isAddedPath(i)) {
					ColumbaLogger.log.debug(
						"Message added to Selection= " + message.getUid());
					messages.add(message);
				} else {
					ColumbaLogger.log.debug(
						"Message removed from Selection= " + message.getUid());
					messages.remove(message);
				}
			}
		}
		
		fireSelectionChanged( new MessageSelectionChangedEvent( folder, getUidArray() ));

		/*
		DefaultMutableTreeNode node =
			(DefaultMutableTreeNode) view
				.getTree()
				.getLastSelectedPathComponent();

		if (node == null)
			return;

		MessageNode[] nodes = getView().getSelectedNodes();
		if (nodes == null) {
			return;
		}

		//getActionListener().changeMessageActions();

		if (nodes.length == 0)
			return;

		newUidList = MessageNode.toUidArray(nodes);
		

		getTableSelectionManager().fireMessageSelectionEvent(null, newUidList);
		*/
	}

	/**
	 * Sets the folder.
	 * @param folder The folder to set
	 */
	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	private Object[] getUidArray() {
		Object[] result = new Object[messages.size()];
		ListIterator it = messages.listIterator();
		
		int i=0;
		while( it.hasNext() ) {
			result[i++] = ((MessageNode)it.next()).getUid();
		}
		
		return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/637.java