error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4491.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4491.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4491.java
text:
```scala
S@@tring[] uids = mediator.getTable().getUids();

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
package org.columba.addressbook.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.columba.addressbook.folder.AbstractFolder;
import org.columba.addressbook.folder.AddressbookTreeNode;
import org.columba.addressbook.folder.IGroupFolder;
import org.columba.addressbook.gui.focus.FocusManager;
import org.columba.addressbook.gui.focus.FocusOwner;
import org.columba.addressbook.gui.frame.AddressbookFrameMediator;
import org.columba.addressbook.gui.table.TableController;
import org.columba.addressbook.gui.tree.AddressbookTreeModel;
import org.columba.addressbook.gui.tree.TreeController;
import org.columba.addressbook.util.AddressbookResourceLoader;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.gui.dialog.ErrorDialog;
import org.columba.core.logging.Logging;
import org.columba.core.resourceloader.IconKeys;
import org.columba.core.resourceloader.ImageLoader;

/**
 * Delete selected contact or group item.
 * 
 * @author fdietz
 */
public class RemoveCardAction extends DefaultTableAction implements
		TreeSelectionListener {
	public RemoveCardAction(IFrameMediator frameController) {
		super(frameController, AddressbookResourceLoader.getString("menu",
				"mainframe", "menu_file_remove"));

		// tooltip text
		putValue(SHORT_DESCRIPTION, AddressbookResourceLoader.getString("menu",
				"mainframe", "menu_file_remove_tooltip").replaceAll("&", ""));

		putValue(TOOLBAR_NAME, AddressbookResourceLoader.getString("menu",
				"mainframe", "menu_file_remove_toolbar"));

		// icons
		putValue(SMALL_ICON, ImageLoader.getSmallIcon(IconKeys.EDIT_DELETE));
		putValue(LARGE_ICON, ImageLoader.getIcon(IconKeys.EDIT_DELETE));


		setEnabled(false);

		//		 register interest on tree selection changes
		((AddressbookFrameMediator) frameMediator)
				.addTreeSelectionListener(this);
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		AddressbookFrameMediator mediator = (AddressbookFrameMediator) frameMediator;

		FocusOwner focusOwner = FocusManager.getInstance().getCurrentOwner();

		TableController table = ((AddressbookFrameMediator) frameMediator)
				.getTable();

		boolean tableHasFocus = false;
		if (table.equals(focusOwner))
			tableHasFocus = true;

		if (tableHasFocus) {

			// get selected contact/group card
			Object[] uids = mediator.getTable().getUids();

			// get selected folder
			AbstractFolder folder = (AbstractFolder) mediator.getTree()
					.getSelectedFolder();

			// remove contacts/group cards from folder
			for (int i = 0; i < uids.length; i++) {
				try {
					folder.remove(uids[i]);
				} catch (Exception e) {
					if (Logging.DEBUG)
						e.printStackTrace();

					ErrorDialog.createDialog(e.getMessage(), e);
				}
			}

			if (folder instanceof IGroupFolder)
				//		 re-select folder
				mediator.getTree().setSelectedFolder(folder);
		} else {
			// tree has focus

			// get selected folder
			AbstractFolder folder = (AbstractFolder) mediator.getTree()
					.getSelectedFolder();

			// get parent
			AddressbookTreeNode parent = (AddressbookTreeNode) folder
					.getParent();

			mediator.getTree().setSelectedFolder((AbstractFolder) parent);
			
			// remove folder from parent
			folder.removeFromParent();

			// notify model
			AddressbookTreeModel.getInstance()
					.nodeStructureChanged(parent);

			
		}
	}

	/**
	 * Enable or disable action on selection change.
	 * 
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent event) {
		// return if selection change is in flux
		if (event.getValueIsAdjusting()) {
			return;
		}

		FocusOwner focusOwner = FocusManager.getInstance().getCurrentOwner();

		TableController table = ((AddressbookFrameMediator) frameMediator)
				.getTable();

		if (table.equals(focusOwner)) {

			// table has focus

			Object[] uids = ((AddressbookFrameMediator) frameMediator)
					.getTable().getUids();

			if (uids.length > 0) {
				setEnabled(true);
				return;
			}
		}

		setEnabled(false);

	}

	public void valueChanged(TreeSelectionEvent e) {
		TreePath path = e.getNewLeadSelectionPath();

		FocusOwner focusOwner = FocusManager.getInstance().getCurrentOwner();

		TreeController tree = ((AddressbookFrameMediator) frameMediator)
				.getTree();

		if (tree.equals(focusOwner)) {
			// tree has focus

			AddressbookTreeNode treeNode = null;
			// remember last selected folder treenode
			if (path != null) {
				treeNode = (AddressbookTreeNode) path.getLastPathComponent();
			}

			// enable, if more than zero treenodes selected
			if ((path != null) && (treeNode instanceof IGroupFolder)) {
				setEnabled(true);
			} else
				setEnabled(false);

		} else

			setEnabled(false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4491.java