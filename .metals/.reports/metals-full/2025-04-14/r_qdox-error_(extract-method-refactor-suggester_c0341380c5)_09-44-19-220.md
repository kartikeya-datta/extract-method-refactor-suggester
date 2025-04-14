error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2132.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2132.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2132.java
text:
```scala
E@@rrorDialog.createDialog(e1.getMessage(), e1);

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
package org.columba.addressbook.gui.table;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import org.columba.addressbook.folder.AbstractFolder;
import org.columba.addressbook.folder.AddressbookTreeNode;
import org.columba.addressbook.folder.FolderListener;
import org.columba.addressbook.folder.IContactStorage;
import org.columba.addressbook.folder.IFolderEvent;
import org.columba.addressbook.gui.focus.FocusManager;
import org.columba.addressbook.gui.focus.FocusOwner;
import org.columba.addressbook.gui.frame.AddressbookFrameMediator;
import org.columba.addressbook.gui.table.model.AddressbookTableModel;
import org.columba.addressbook.gui.table.model.FilterDecorator;
import org.columba.addressbook.gui.table.model.SortDecorator;
import org.columba.addressbook.model.IContactItem;
import org.columba.core.gui.dialog.ErrorDialog;
import org.columba.core.logging.Logging;

/**
 * @author fdietz
 */
public class TableController implements TreeSelectionListener, FolderListener,
		FocusOwner {

	private TableView view;

	private AddressbookFrameMediator mediator;

	private AddressbookTableModel addressbookModel;

	private SortDecorator sortDecorator;

	private FilterDecorator filterDecorator;

	private AddressbookTreeNode selectedFolder;

	/**
	 * 
	 */
	public TableController(AddressbookFrameMediator mediator) {
		super();

		this.mediator = mediator;

		addressbookModel = new AddressbookTableModel();

		sortDecorator = new SortDecorator(addressbookModel);

		filterDecorator = new FilterDecorator(sortDecorator);

		view = new TableView(this, filterDecorator);

		addMouseListenerToHeaderInTable();

		view.addMouseListener(new TableMouseListener(this));

		// register as focus owner
		FocusManager.getInstance().registerComponent(this);
	}

	/**
	 * Add MouseListener to JTableHeader to sort table based on clicked column
	 * header.
	 * 
	 */
	protected void addMouseListenerToHeaderInTable() {
		final JTable tableView = getView();

		tableView.setColumnSelectionAllowed(false);

		MouseAdapter listMouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				TableColumnModel columnModel = tableView.getColumnModel();
				int viewColumn = columnModel.getColumnIndexAtX(e.getX());
				int column = tableView.convertColumnIndexToModel(viewColumn);

				if ((e.getClickCount() == 1) && (column != -1)) {

					if (sortDecorator.getSelectedColumn() == column)
						sortDecorator
								.setSortOrder(!sortDecorator.isSortOrder());

					sortDecorator.sort(column);

				}
			}
		};

		JTableHeader th = tableView.getTableHeader();
		th.addMouseListener(listMouseListener);
	}

	/**
	 * @return AddressbookFrameController
	 */
	public AddressbookFrameMediator getMediator() {
		return mediator;
	}

	/**
	 * @return TableView
	 */
	public TableView getView() {
		return view;
	}

	/**
	 * Update table on tree selection changes.
	 */
	public void valueChanged(TreeSelectionEvent e) {
		AddressbookTreeNode node = (AddressbookTreeNode) e.getPath()
				.getLastPathComponent();

		if (node == null) {
			return;
		}

		if (node instanceof IContactStorage) {
			try {

				((AbstractFolder) node).removeFolderListener(this);

				selectedFolder = node;
				((AbstractFolder) node).addFolderListener(this);

				filterDecorator
						.setContactItemMap(((AbstractFolder) selectedFolder)
								.getContactItemMap());
			} catch (Exception e1) {
				if (Logging.DEBUG)
					e1.printStackTrace();

				new ErrorDialog(e1.getMessage(), e1);
			}
		} else {
			filterDecorator.setContactItemMap(null);
		}
	}

	/**
	 * Get selected uids.
	 * 
	 * @return
	 */
	public Object[] getUids() {
		int[] rows = getView().getSelectedRows();
		Object[] uids = new Object[rows.length];

		IContactItem item;

		for (int i = 0; i < rows.length; i++) {
			item = (IContactItem) filterDecorator.getContactItem(rows[i]);

			Object uid = item.getUid();
			uids[i] = uid;
		}

		return uids;
	}

	/**
	 * @return Returns the addressbookModel.
	 */
	public AddressbookTableModel getAddressbookModel() {
		return addressbookModel;
	}

	public IContactItem getSelectedItem() {
		int row = getView().getSelectedRow();

		// we use the SortDecorator, because the indices are sorted
		IContactItem item = (IContactItem) sortDecorator.getContactItem(row);

		return item;
	}

	/** ********************** FolderListener ***************** */

	/**
	 * @see org.columba.addressbook.folder.FolderListener#itemAdded(org.columba.addressbook.folder.FolderEvent)
	 */
	public void itemAdded(IFolderEvent e) {
		getAddressbookModel().update();

	}

	/**
	 * @see org.columba.addressbook.folder.FolderListener#itemChanged(org.columba.addressbook.folder.FolderEvent)
	 */
	public void itemChanged(IFolderEvent e) {
		getAddressbookModel().update();

	}

	/**
	 * @see org.columba.addressbook.folder.FolderListener#itemRemoved(org.columba.addressbook.folder.FolderEvent)
	 */
	public void itemRemoved(IFolderEvent e) {
		getAddressbookModel().update();

	}

	/** ************* FocusOwner Implementation ****************** */

	/**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#copy()
	 */
	public void copy() {

	}

	/**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#cut()
	 */
	public void cut() {

	}

	/**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#delete()
	 */
	public void delete() {

	}

	/**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#getComponent()
	 */
	public JComponent getComponent() {
		return getView();
	}

	/**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#isCopyActionEnabled()
	 */
	public boolean isCopyActionEnabled() {

		return false;
	}

	/**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#isCutActionEnabled()
	 */
	public boolean isCutActionEnabled() {

		return false;
	}

	/**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#isDeleteActionEnabled()
	 */
	public boolean isDeleteActionEnabled() {

		return false;
	}

	/**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#isPasteActionEnabled()
	 */
	public boolean isPasteActionEnabled() {

		return false;
	}

	/**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#isRedoActionEnabled()
	 */
	public boolean isRedoActionEnabled() {

		return false;
	}

	/**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#isSelectAllActionEnabled()
	 */
	public boolean isSelectAllActionEnabled() {
		if (getView().getRowCount() > 0) {
			return true;
		}

		return false;
	}

	/**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#isUndoActionEnabled()
	 */
	public boolean isUndoActionEnabled() {

		return false;
	}

	/**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#paste()
	 */
	public void paste() {

	}

	/**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#redo()
	 */
	public void redo() {

	}

	/**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#selectAll()
	 */
	public void selectAll() {
		getView().selectAll();
	}

	/**
	 * @see org.columba.addressbook.gui.focus.FocusOwner#undo()
	 */
	public void undo() {

	}

	/**
	 * @return Returns the sortDecorator.
	 */
	public SortDecorator getSortDecorator() {
		return sortDecorator;
	}

	/**
	 * @return Returns the filterDecorator.
	 */
	public FilterDecorator getFilterDecorator() {
		return filterDecorator;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2132.java