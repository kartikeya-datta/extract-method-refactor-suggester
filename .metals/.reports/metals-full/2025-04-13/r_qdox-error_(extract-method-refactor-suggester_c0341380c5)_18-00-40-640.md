error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9784.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9784.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9784.java
text:
```scala
n@@ew HeaderTableCommonRenderer(getTree(), name),

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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JTable;
import javax.swing.event.MouseInputListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreePath;

import org.columba.core.config.HeaderItem;
import org.columba.core.config.TableItem;
import org.columba.core.gui.util.ImageLoader;
import org.columba.core.gui.util.treetable.TreeTable;
import org.columba.mail.config.MailConfig;
import org.columba.mail.gui.table.plugins.*;
import org.columba.mail.gui.table.util.MessageNode;
import org.columba.mail.gui.table.util.TableModelFilteredView;
import org.columba.mail.gui.table.util.TableModelThreadedView;
import org.columba.mail.message.HeaderList;
import org.columba.mail.util.MailResourceLoader;

/**
 * This widget is a mix between a JTable and a JTree
 * ( we need the JTree for the Threaded viewing of mailing lists )
 *
 * @version 0.9.1
 * @author Frederik
 */
public class TableView extends TreeTable {

	protected HeaderTableModel headerTableModel;

	private int selectedRow = 0;
	private int update = 0;

	//private ListSelectionModel listSelectionModel;

	private String column;

	private List tableModelPlugins;

	protected TableModelFilteredView tableModelFilteredView;
	protected HeaderTableModelSorter tableModelSorter;
	protected TableModelThreadedView tableModelThreadedView;

	protected HeaderList headerList;

	public TableView(HeaderTableModel headerTableModel) {
		super();

		this.headerTableModel = headerTableModel;

		setModel(headerTableModel);

		addMouseListenerToHeaderInTable();

		//setSelectionModel(new HeaderTableSelectionModel());
		tableModelFilteredView = new TableModelFilteredView(headerTableModel);

		tableModelSorter = new HeaderTableModelSorter(headerTableModel);
		tableModelSorter.setWindowItem(
			MailConfig.getMainFrameOptionsConfig().getWindowItem());

		tableModelThreadedView = new TableModelThreadedView(headerTableModel);

		//setUI(new ColumbaBasicTableUI());

		headerTableModel.registerPlugin(tableModelFilteredView);
		headerTableModel.registerPlugin(tableModelThreadedView);
		headerTableModel.registerPlugin(tableModelSorter);

		getTree().setCellRenderer(new SubjectTreeRenderer());

		try {
			initRenderer(false);
			//headerTableModel.update();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		adjustColumn();
	}

	protected void addMouseListenerToHeaderInTable() {
		setColumnSelectionAllowed(false);

		MouseAdapter listMouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				TableColumnModel columnModel = getColumnModel();
				int viewColumn = columnModel.getColumnIndexAtX(e.getX());
				int column = convertColumnIndexToModel(viewColumn);

				if (e.getClickCount() == 1 && column != -1) {
					getTableModelSorter().setSortingColumn(column);
					headerTableModel.update();
				}
			}
		};

		JTableHeader th = getTableHeader();
		th.addMouseListener(listMouseListener);
	}

	public void enableThreadedView(boolean b) {
		if (b) {
			//tree.setRootVisible(true);

			TableColumn tc = null;
			try {
				tc = getColumn("Subject");
				tc.setCellRenderer(null);

			} catch (Exception ex) {
				System.out.println(
					"headerTable->registerRenderer: " + ex.getMessage());
			}

			((HeaderTableModel) getModel()).enableThreadedView(true);

			//getTree().setCellRenderer()new SubjectTreeCellRenderer());

		} else {
			//tree.setRootVisible(false);

			 ((HeaderTableModel) getModel()).enableThreadedView(false);
			//setTreeCellRenderer(null);
			TableColumn tc = null;
			try {
				tc = getColumn("Subject");
				tc.setCellRenderer(
					new HeaderTableCommonRenderer(getTree(), "Subject"));

			} catch (Exception ex) {
				System.out.println(
					"headerTable->registerRenderer: " + ex.getMessage());
			}
		}
	}

	/**
	 * sets the header, which is going to be viewed
	 *
	 * @param f a <code>Folder</code>
	 * @see Folder
	 */

	protected void adjustColumn() {
		TableItem tableItem =
			(TableItem) MailConfig.getMainFrameOptionsConfig().getTableItem();

		//.clone();
		//v.removeEnabledItem();

		for (int i = 0; i < tableItem.count(); i++) {
			HeaderItem v = tableItem.getHeaderItem(i);
			boolean enabled = v.getBoolean("enabled");

			if (enabled == false)
				continue;

			String name = v.get("name");
			int size = v.getInteger("size");
			int position = v.getInteger("position");

			TableColumn tc = null;

			//ColumbaLogger.log.debug("name=" + name);

			try {
				tc = getColumn(name);
			} catch (Exception ex) {
				System.out.println(
					"headerTable->registerRenderer: " + ex.getMessage());
			}

			if (tc == null)
				continue;

			tc.setPreferredWidth(size);

			try {
				int index = getColumnModel().getColumnIndex(name);
				getColumnModel().moveColumn(index, position);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	protected void initRenderer(boolean b) throws Exception {
		TableItem tableItem =
			(TableItem) MailConfig.getMainFrameOptionsConfig().getTableItem();

		//.clone();
		//v.removeEnabledItem();

		for (int i = 0; i < tableItem.count(); i++) {
			HeaderItem v = tableItem.getHeaderItem(i);
			boolean enabled = v.getBoolean("enabled");

			if (enabled == false)
				continue;

			String name = v.get("name");
			int size = v.getInteger("size");
			int position = v.getInteger("position");

			if (name.equalsIgnoreCase("size")) {
				registerRenderer(
					"Size",
					new HeaderTableSizeRenderer(getTree()),
					new CommonHeaderRenderer(
						name,
						MailResourceLoader.getString("header", "size"),
						getTableModelSorter()),
					size,
					false,
					position);
			} else if (name.equalsIgnoreCase("Status")) {

				registerRenderer(
					"Status",
					new StatusRenderer(getTree()),
					new BooleanHeaderRenderer(
						ImageLoader.getSmallImageIcon("mail-new.png"),
						name,
						getTableModelSorter()),
					23,
					true,
					position);
			} else if (name.equalsIgnoreCase("Flagged")) {
				registerRenderer(
					"Flagged",
					new FlaggedRenderer(getTree()),
					new BooleanHeaderRenderer(
						ImageLoader.getSmallImageIcon(
							"mark-as-important-16.png"),
						name,
						getTableModelSorter()),
					23,
					true,
					position);
			} else if (name.equalsIgnoreCase("Attachment")) {
				registerRenderer(
					"Attachment",
					new BooleanRenderer(
						getTree(),
						true,
						ImageLoader.getSmallImageIcon("attachment.png"),
						"columba.attachment"),
					new BooleanHeaderRenderer(
						ImageLoader.getSmallImageIcon("attachment.png"),
						name,
						getTableModelSorter()),
					23,
					true,
					position);
			} else if (name.equalsIgnoreCase("Date")) {
				registerRenderer(
					"Date",
					new HeaderTableDateRenderer(getTree(), true),
					new DateHeaderRenderer(
						name,
						MailResourceLoader.getString("header", "date"),
						getTableModelSorter()),
					size,
					false,
					position);

			} else if (name.equalsIgnoreCase("Priority")) {
				registerRenderer(
					"Priority",
					new PriorityRenderer(getTree(), true),
					new BooleanHeaderRenderer(
						ImageLoader.getSmallImageIcon("priority-high.png"),
						name,
						getTableModelSorter()),
					23,
					true,
					position);

			} else if (name.equalsIgnoreCase("Subject")) {

				registerRenderer(
					"Subject",
					new HeaderTableCommonRenderer(getTree(), "Subject"),
					new CommonHeaderRenderer(
						name,
						MailResourceLoader.getString("header", "subject"),
						getTableModelSorter()),
					size,
					false,
					position);

			} else {
				String str = MailResourceLoader.getString(
							"header",
							name.toLowerCase());

				if (str.equals("FIX ME!")) {
					registerRenderer(
						name,
						new HeaderTableCommonRenderer(getTree(), name),
						new CommonHeaderRenderer(
							name,
							name,
							getTableModelSorter()),
						size,
						false,
						position);
                                } else {
					registerRenderer(
						name,
						new HeaderTableCommonRenderer(getTree(), str),
						new CommonHeaderRenderer(
							name,
							str,
							getTableModelSorter()),
						size,
						false,
						position);
                                }
			}
		}
	}

	public void registerRenderer(
		String name,
		TableCellRenderer cell,
		TableCellRenderer header,
		int size,
		boolean lockSize,
		int position) {
		TableColumn tc = null;

		//ColumbaLogger.log.debug("name=" + name);

		try {
			tc = getColumn(name);
		} catch (Exception ex) {
			System.out.println(
				"headerTable->registerRenderer: " + ex.getMessage());
		}

		if (tc == null)
			return;

		if (cell != null)
			tc.setCellRenderer(cell);

		if (header != null)
			tc.setHeaderRenderer(header);

		if (lockSize) {
			tc.setMaxWidth(size);
			tc.setMinWidth(size);
		} else {
			//ColumbaLogger.log.debug("setting size =" + size);

			tc.setPreferredWidth(size);
		}

		try {
			int index = getColumnModel().getColumnIndex(name);
			getColumnModel().moveColumn(index, position);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void registerRenderer(
		String name,
		TableCellRenderer cell,
		TableCellRenderer header,
		int size,
		boolean lockSize) {
		TableColumn tc = null;

		try {
			tc = getColumn(name);
		} catch (Exception ex) {
			System.out.println(
				"headerTable->registerRenderer: " + ex.getMessage());
		}

		if (tc == null)
			return;

		if (cell != null)
			tc.setCellRenderer(cell);

		if (header != null)
			tc.setHeaderRenderer(header);

		if (lockSize) {
			tc.setMaxWidth(size);
			tc.setMinWidth(size);
		} else
			tc.setPreferredWidth(size);

	}

	/**
	 * return the table model sorter
	 */
	public HeaderTableModelSorter getTableModelSorter() {
		return tableModelSorter;
	}

	/**
	 * return the threaded view model
	 */
	public TableModelThreadedView getTableModelThreadedView() {
		return tableModelThreadedView;
	}

	/**
	 * return the filtered view model
	 */
	public TableModelFilteredView getTableModelFilteredView() {
		return tableModelFilteredView;
	}

	public MessageNode getSelectedNode() {

		MessageNode node =
			(MessageNode) getTree().getLastSelectedPathComponent();

		return node;

	}

	/*
	public int getSelectedRowCount()
	{
	    int[] rows = table.getSelectedRows();
	
		if ( rows == null ) return 0;
		
	    return rows.length;
	}
	*/
	public MessageNode[] getSelectedNodes() {
		int[] rows = null;
		MessageNode[] nodes = null;

		rows = getSelectedRows();
		nodes = new MessageNode[rows.length];

		for (int i = 0; i < rows.length; i++) {
			TreePath treePath = getTree().getPathForRow(rows[i]);
			nodes[i] = (MessageNode) treePath.getLastPathComponent();

		}
		return nodes;
	}

	public MessageNode getMessagNode(Object uid) {
		return headerTableModel.getMessageNode(uid);
	}

	protected MouseInputListener createMouseInputListener() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9784.java