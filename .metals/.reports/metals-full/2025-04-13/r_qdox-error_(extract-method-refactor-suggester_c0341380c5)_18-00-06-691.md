error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/769.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/769.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/769.java
text:
```scala
.@@getFolder("101").getPath()));

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
package org.columba.addressbook.gui.tree.util;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import net.javaprog.ui.wizard.plaf.basic.SingleSideEtchedBorder;

import org.columba.addressbook.config.FolderItem;
import org.columba.addressbook.folder.AddressbookFolder;
import org.columba.addressbook.folder.AddressbookTreeNode;
import org.columba.addressbook.folder.IFolder;
import org.columba.addressbook.gui.tree.AddressbookTreeModel;
import org.columba.addressbook.util.AddressbookResourceLoader;
import org.columba.core.gui.base.ButtonWithMnemonic;

public class SelectAddressbookFolderDialog extends JDialog implements
		ActionListener, TreeSelectionListener, ISelectFolderDialog {

	// private MainInterface mainInterface;
	private boolean bool = false;

	// public SelectFolderTree tree;
	private JTree tree;

	private JButton[] buttons;

	// private TreeView treeViewer;
	private IFolder selectedFolder;

	private TreeModel model;

	public SelectAddressbookFolderDialog(TreeModel model) {
		super(new JFrame(), true);

		setTitle(AddressbookResourceLoader.getString("tree", "folderdialog",
				"select_folder"));

		this.model = model;

		initComponents();

		layoutComponents();

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void layoutComponents() {
		getContentPane().setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		mainPanel.add(new JScrollPane(tree), BorderLayout.CENTER);

		getContentPane().add(mainPanel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBorder(new SingleSideEtchedBorder(SwingConstants.TOP));

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 6, 0));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		buttonPanel.add(buttons[0]);
		buttonPanel.add(buttons[1]);
		bottomPanel.add(buttonPanel, BorderLayout.EAST);

		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
	}

	private void initComponents() {
		buttons = new JButton[3];

		buttons[0] = new ButtonWithMnemonic(AddressbookResourceLoader
				.getString("global", "cancel"));
		buttons[0].setActionCommand("CANCEL");
		buttons[0].setDefaultCapable(true);
		buttons[1] = new ButtonWithMnemonic(AddressbookResourceLoader
				.getString("global", "ok"));
		buttons[1].setEnabled(true);
		buttons[1].setActionCommand("OK");
		buttons[2] = new JButton(AddressbookResourceLoader.getString("tree",
				"folderdialog", "new_subFolder"));
		buttons[2].setActionCommand("NEW");
		buttons[2].setEnabled(false);

		getRootPane().setDefaultButton(buttons[1]);

		tree = new JTree(model);
		tree.expandRow(0);
		tree.expandRow(1);
		tree.putClientProperty("JTree.lineStyle", "Angled");
		tree.setShowsRootHandles(true);
		tree.setRootVisible(false);

		tree.addTreeSelectionListener(this);
		tree.setCellRenderer(new AddressbookTreeCellRenderer(true));

		for (int i = 0; i < 3; i++) {
			buttons[i].addActionListener(this);
		}

		tree.setSelectionPath(new TreePath(AddressbookTreeModel.getInstance()
				.getFolder(101).getPath()));
	}

	public boolean success() {
		return bool;
	}

	public IFolder getSelectedFolder() {
		return selectedFolder;
	}

	public int getUid() {
		/*
		 * FolderTreeNode node = tree.getSelectedNode(); FolderItem item =
		 * node.getFolderItem();
		 */
		return 101;

		// return item.getUid();
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals("OK")) {
			bool = true;
			setVisible(false);
		} else if (action.equals("CANCEL")) {
			bool = false;
			setVisible(false);
		} else if (action.equals("NEW")) {
			/*
			 * EditFolderDialog dialog = treeViewer.getEditFolderDialog( "New
			 * Folder" ); dialog.showDialog(); String name; if (
			 * dialog.success() == true ) { // ok pressed name =
			 * dialog.getName(); } else { // cancel pressed return; }
			 * treeViewer.getFolderTree().addUserFolder( getSelectedFolder(),
			 * name ); //TreeNodeEvent updateEvent2 = new TreeNodeEvent(
			 * getSelectedFolder(), TreeNodeEvent.STRUCTURE_CHANGED );
			 * //treeViewer.mainInterface.crossbar.fireTreeNodeChanged(updateEvent2);
			 */
		}
	}

	/**
	 * ***************************** tree selection listener
	 * *******************************
	 */
	public void valueChanged(TreeSelectionEvent e) {
		AddressbookTreeNode node = (AddressbookTreeNode) tree
				.getLastSelectedPathComponent();

		if (node == null) {
			return;
		}

		FolderItem item = node.getFolderItem();

		if (item.get("type").equals("AddressbookFolder")) {
			buttons[1].setEnabled(true);
			selectedFolder = (AddressbookFolder) node;
		} else {
			buttons[1].setEnabled(false);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/769.java