error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4740.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4740.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4740.java
text:
```scala
F@@older folder = (Folder) event.getSrcFolder();

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

import java.util.Vector;

import javax.swing.JPopupMenu;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;

import org.columba.core.config.HeaderItem;
import org.columba.core.config.TableItem;
import org.columba.core.gui.util.CScrollPane;
import org.columba.core.logging.ColumbaLogger;
import org.columba.core.main.MainInterface;
import org.columba.core.util.SwingWorker;
import org.columba.mail.command.FolderCommandReference;
import org.columba.mail.config.MailConfig;
import org.columba.mail.folder.Folder;
import org.columba.mail.folder.FolderTreeNode;
import org.columba.mail.gui.frame.MailFrameController;
import org.columba.mail.gui.message.command.ViewMessageCommand;
import org.columba.mail.gui.table.action.FilterActionListener;
import org.columba.mail.gui.table.action.HeaderItemActionListener;
import org.columba.mail.gui.table.action.HeaderTableActionListener;
import org.columba.mail.gui.table.action.HeaderTableDnd;
import org.columba.mail.gui.table.action.HeaderTableFocusListener;
import org.columba.mail.gui.table.action.HeaderTableMouseListener;
import org.columba.mail.gui.table.menu.HeaderTableMenu;
import org.columba.mail.gui.table.util.MarkAsReadTimer;
import org.columba.mail.gui.table.util.MessageNode;
import org.columba.mail.gui.tree.FolderSelectionListener;
import org.columba.mail.message.HeaderList;

/**
 * This class shows the messageheaderlist
 *
 *
 * @version 0.9.1
 * @author Frederik
 */

public class TableController
	implements FolderSelectionListener, TreeSelectionListener {

	private HeaderTableMenu menu;

	private TableView headerTable;
	private HeaderTableModel headerTableModel;

	public CScrollPane scrollPane;
	private SwingWorker worker;
	private Folder folder;
	private HeaderList headerList;

	private FilterToolbar filterToolbar;

	private HeaderTableActionListener headerTableActionListener;

	private MessageNode[] messageNodes;

	private MessageNode node;
	private MessageNode oldNode;

	private Object[] selection;

	private HeaderTableMouseListener headerTableMouseListener;
	private HeaderTableDnd headerTableDnd;
	private HeaderTableFocusListener headerTableFocusListener;
	private HeaderItemActionListener headerItemActionListener;
	private FilterActionListener filterActionListener;

	private TableItem headerTableItem;

	private boolean folderChanged = false;

	private int counter = 1;

	//protected SelectionManager selectionManager;
	protected TableView view;
	protected HeaderTableActionListener actionListener;

	protected TableSelectionManager tableSelectionManager;

	protected MailFrameController mailFrameController;

	protected Object[] oldUidList;
	protected Object[] newUidList;

	protected MarkAsReadTimer markAsReadTimer;
	
	protected Vector tableChangedListenerList;

	public TableController(MailFrameController mailFrameController) {

		this.mailFrameController = mailFrameController;

		//setLayout(new BorderLayout());

		oldUidList = null;
		newUidList = null;
		
		headerTableItem =
			(TableItem) MailConfig
				.getMainFrameOptionsConfig()
				.getTableItem();
				
				//.clone();
		//headerTableItem.removeEnabledItem();

		headerTableModel = new HeaderTableModel(headerTableItem);

		view = new TableView(headerTableModel);

		tableSelectionManager = new TableSelectionManager();
		tableSelectionManager.addFolderSelectionListener(this);

		tableChangedListenerList = new Vector();
		
		actionListener = new HeaderTableActionListener(this);

		menu = new HeaderTableMenu(this);

		headerTableDnd = new HeaderTableDnd(this);

		headerTableMouseListener = new HeaderTableMouseListener(this);
		view.addMouseListener(headerTableMouseListener);

		/*
		headerTableFocusListener = new HeaderTableFocusListener();
		view.addFocusListener(headerTableFocusListener);
		*/

		headerItemActionListener =
			new HeaderItemActionListener(this, headerTableItem);
		filterActionListener = new FilterActionListener(this);

		markAsReadTimer = new MarkAsReadTimer(this);

		view.addTreeSelectionListener(this);
		
		getHeaderTableModel().getTableModelSorter().setSortingColumn( headerTableItem.get("selected") );
		getHeaderTableModel().getTableModelSorter().setSortingOrder( headerTableItem.getBoolean("ascending"));
		
		
		
	}
	
	public void addTableChangedListener( TableChangeListener l )
	{
		tableChangedListenerList.add(l);
	}
	
	public void fireTableChangedEvent(TableChangedEvent e)
	{
		for ( int i=0; i<tableChangedListenerList.size(); i++ )
		{
			TableChangeListener l = (TableChangeListener) tableChangedListenerList.get(i);
			l.tableChanged(e);
		}
	}
	

	public TableView getView() {

		return view;
	}

	/*
	public void setSelectionManager(SelectionManager manager) {
		this.selectionManager = manager;
	
		selectionManager.addFolderSelectionListener(this);
	}
	*/

	public void folderSelectionChanged(FolderTreeNode newFolder) {

		/*
		ColumbaLogger.log.debug("selection=" + newFolder);

		if (newFolder instanceof Folder) {
			MainInterface.processor.addOp(
				new ViewHeaderListCommand(
					mailFrameController,
					getTableSelectionManager().getSelection()));

		}
		*/
	}

	public HeaderTableMouseListener getHeaderTableMouseListener() {
		return headerTableMouseListener;
	}

	public boolean hasFocus() {
		return headerTableFocusListener.hasFocus();
	}

	/**
	 * return FilterToolbar
	 */

	public FilterToolbar getFilterToolbar() {
		return filterToolbar;
	}

	/**
	 * return HeaderTableItem
	 */
	public TableItem getHeaderTableItem() {
		return headerTableItem;
	}

	/**
	 * set the render for each column
	 */

	/**
	 * return a Dialog wich is used by the copy/move message operation
	 * to get the destination folder
	 */

	/*
	public org.columba.modules.mail.gui.tree.util.SelectFolderDialog getSelectFolderDialog()
	{
		return MainInterface.treeViewer.getSelectFolderDialog();
	}
	*/
	/**
	 * show the filter toolbar
	 */

	/*
	public void showToolbar()
	{
		add(filterToolbar, BorderLayout.NORTH);
		TableModelFilteredView model = getTableModelFilteredView();
		try
		{
			model.setDataFiltering(true);
		}
		catch ( Exception ex )
		{
		}
	
		getHeaderTableModel().update();
	
		validate();
		repaint();
	}
	*/
	/**
	 * hide the filter toolbar
	 */

	/*
	public void hideToolbar()
	{
		remove(filterToolbar);
		TableModelFilteredView model = getTableModelFilteredView();
		try
		{
			model.setDataFiltering(false);
		}
		catch ( Exception ex )
		{
		}
	
		validate();
		repaint();
	}
	*/
	/**
	 * return the ActionListener
	 *
	 */
	public HeaderTableActionListener getActionListener() {
		return actionListener;
	}

	/**
	 * save the column state:
	 *  - position
	 *  - size
	 *  - appearance
	 * of every column
	 */

	public void saveColumnConfig() {
		TableItem tableItem =
			(TableItem) MailConfig
				.getMainFrameOptionsConfig()
				.getTableItem();
		
		boolean ascending = getHeaderTableModel().getTableModelSorter().getSortingOrder();
		String sortingColumn = getHeaderTableModel().getTableModelSorter().getSortingColumn();
		
		tableItem.set("ascending", ascending);
		tableItem.set("selected", sortingColumn);

		ColumbaLogger.log.info("save table column config");
						
				//.clone();
		//v.removeEnabledItem();

		for (int i = 0; i < tableItem.getChildCount(); i++) {
			HeaderItem v = tableItem.getHeaderItem(i);
			boolean enabled = v.getBoolean("enabled");
			if ( enabled == false ) continue;
			
			String c = v.get("name");
			ColumbaLogger.log.debug("name="+c);
			
			TableColumn tc = getView().getColumn(c);

			v.set("size", tc.getWidth());
			ColumbaLogger.log.debug("size"+tc.getWidth());
			try {
				int index = getView().getColumnModel().getColumnIndex(c);
				v.set("position", index);
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			}

		}

	}

	/**
	 * set folder to show
	 */
	public void setFolder(Folder f) {
		this.folder = f;
	}

	/**
	 * return currently showed folder
	 */
	public Folder getFolder() {
		return folder;
	}

	/**
	 * return an array of all selected message uids
	 */
	/*
	public Object[] getUids()
	{
		MessageNode[] nodes = getHeaderTable().getSelectedNodes();
	
		Object[] uids = new Object[nodes.length];
	
		for (int i = 0; i < nodes.length; i++)
		{
			uids[i] = nodes[i].getUid();
		}
	
		return uids;
	
	}
	*/
	/**
	 * return HeaderTable widget which does all the dirty work
	 */
	public TableView getHeaderTable() {
		return headerTable;
	}

	/**
	 * return the Model which contains a HeaderList
	 */
	public HeaderTableModel getHeaderTableModel() {
		return headerTableModel;

	}

	/*
	public void backupSelection()
	{
		if ( messageNodes != null )
			getHeaderTable().setSelection( messageNodes );
	}
	*/

	public void clearMessageNodeList() {
		messageNodes = null;
	}

	/**
	 * return ActionListener for the headeritem sorting
	 */
	public HeaderItemActionListener getHeaderItemActionListener() {
		return headerItemActionListener;
	}

	/**
	 * return ActionListener for FilterToolbar
	 */
	public FilterActionListener getFilterActionListener() {
		return filterActionListener;
	}
	
	public void setSelection( Object[] uids)
	{
		oldUidList = newUidList;
		newUidList = uids;
		
		getTableSelectionManager().fireMessageSelectionEvent(oldUidList, newUidList);

	}

	public void valueChanged(TreeSelectionEvent e) {
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

		oldUidList = newUidList;
		newUidList = MessageNode.toUidArray(nodes);

		if ( oldUidList == null ) oldUidList = newUidList;		

		getTableSelectionManager().fireMessageSelectionEvent(oldUidList, newUidList);

	}

	/**
	 * show the message in the messageviewer
	 */

	public void showMessage() {

		FolderCommandReference[] reference =
			(FolderCommandReference[]) 
				getTableSelectionManager()
				.getSelection();

		FolderTreeNode treeNode = reference[0].getFolder();
		Object[] uids = reference[0].getUids();

		// this is no message-viewing action,
		// but a selection of multiple messages
		if (uids.length > 1)
			return;

		
		
			getMailFrameController()
			.attachmentController
			.getAttachmentSelectionManager()
			.setFolder(treeNode);
		
			getMailFrameController()
			.attachmentController
			.getAttachmentSelectionManager()
			.setUids(uids);
		
		
		MainInterface.processor.addOp(
			new ViewMessageCommand(mailFrameController, reference));
	}

	/**
	 * return the PopupMenu for the table
	 */
	public JPopupMenu getPopupMenu() {
		return menu.getPopupMenu();
	}

	/************************** actions ********************************/

	/**
	 * create the PopupMenu
	 */

	/**
	 * MouseListener sorts table when clicking on a column header
	 */

	// method is called when folder data changed
	// the method updates the model

	public void tableChanged(TableChangedEvent event) throws Exception {
		ColumbaLogger.log.info("event="+event);
		
		Folder folder = event.getSrcFolder();

		if (folder == null) {
			if (event.getEventType() == TableChangedEvent.UPDATE)
				getHeaderTableModel().update();

			fireTableChangedEvent(event);
			return;
		}

		FolderCommandReference[] r =
			(FolderCommandReference[]) getTableSelectionManager()
				.getSelection();
		Folder srcFolder = (Folder) r[0].getFolder();

		if (!folder.equals(srcFolder))
			return;
		//System.out.println("headertableviewer->folderChanged");

		switch (event.getEventType()) {
			case TableChangedEvent.UPDATE :
				{
					getHeaderTableModel().update();
						
					break;
				}
			case TableChangedEvent.ADD :
				{
					getHeaderTableModel().addHeaderList(event.getHeaderList());

					break;
				}
			case TableChangedEvent.REMOVE :
				{
					getHeaderTableModel().removeHeaderList(event.getUids());
					break;
				}
			case TableChangedEvent.MARK :
				{
					getHeaderTableModel().markHeader(
						event.getUids(),
						event.getMarkVariant());
					break;
				}
		}

		fireTableChangedEvent(event);
	}

	/**
	 * Returns the tableSelectionManager.
	 * @return TableSelectionManager
	 */
	public TableSelectionManager getTableSelectionManager() {
		return tableSelectionManager;
	}

	/**
	 * Returns the mailFrameController.
	 * @return MailFrameController
	 */
	public MailFrameController getMailFrameController() {
		return mailFrameController;
	}

	/**
	 * Returns the markAsReadTimer.
	 * @return MarkAsReadTimer
	 */
	public MarkAsReadTimer getMarkAsReadTimer() {
		return markAsReadTimer;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4740.java