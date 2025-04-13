error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7897.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7897.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7897.java
text:
```scala
"menu_view_sort")@@,"menu_view_sort");

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
package org.columba.mail.gui.table.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;

import org.columba.core.action.IMenu;
import org.columba.core.config.DefaultItem;
import org.columba.core.config.IDefaultItem;
import org.columba.core.gui.frame.FrameMediator;
import org.columba.core.gui.selection.SelectionChangedEvent;
import org.columba.core.gui.selection.ISelectionListener;
import org.columba.core.gui.util.ImageLoader;
import org.columba.core.xml.XmlElement;
import org.columba.mail.folder.AbstractFolder;
import org.columba.mail.folder.AbstractMessageFolder;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.frame.TableViewOwner;
import org.columba.mail.gui.table.SortingStateObservable;
import org.columba.mail.gui.tree.selection.TreeSelectionChangedEvent;
import org.columba.mail.util.MailResourceLoader;

public class SortMessagesMenu extends IMenu implements ActionListener,
		Observer, ISelectionListener {
	private ButtonGroup columnGroup;

	private ButtonGroup orderGroup;

	private JRadioButtonMenuItem ascendingMenuItem;

	private JRadioButtonMenuItem descendingMenuItem;

	private Observable observable;

	private AbstractMessageFolder selectedFolder;

	public SortMessagesMenu(FrameMediator controller) {
		super(controller, MailResourceLoader.getString("menu", "mainframe",
				"menu_view_sort"));

		setIcon(ImageLoader.getSmallImageIcon("stock_sort-ascending-16.png"));

		((MailFrameMediator) controller).registerTreeSelectionListener(this);

		// register as Observer
		TableViewOwner table = (TableViewOwner) getFrameMediator();
		observable = table.getTableController().getSortingStateObservable();
		observable.addObserver(this);

		//createSubMenu();
	}

	protected void createSubMenu() {
		removeAll();

		TableViewOwner table = (TableViewOwner) getFrameMediator();

		XmlElement columns = ((MailFrameMediator) getFrameMediator())
				.getFolderOptionsController().getConfigNode(selectedFolder,
						"ColumnOptions");

		Vector v = new Vector();

		// *20040510, karlpeder* columns may be null (first time we visit a
		// folder!?)
		if (columns != null) {
			for (int i = 0; i < columns.count(); i++) {
				XmlElement column = columns.getElement(i);

				String name = column.getAttribute("name");
				v.add(name);
			}
		}

		Object[] items = new String[v.size()];
		items = v.toArray();

		columnGroup = new ButtonGroup();

		JRadioButtonMenuItem headerMenuItem;

		for (int i = 0; i < items.length; i++) {
			String item = (String) items[i];

			// all headerfields are lowercase in property file
			String i18n = MailResourceLoader.getString("header", item
					.toLowerCase());

			headerMenuItem = new JRadioButtonMenuItem(i18n);
			headerMenuItem.setActionCommand(item);
			headerMenuItem.addActionListener(this);
			columnGroup.add(headerMenuItem);
			add(headerMenuItem);
		}

		addSeparator();

		orderGroup = new ButtonGroup();
		ascendingMenuItem = new JRadioButtonMenuItem(MailResourceLoader
				.getString("menu", "mainframe", "menu_view_sort_asc"));
		ascendingMenuItem.setActionCommand("Ascending");
		ascendingMenuItem.addActionListener(this);
		orderGroup.add(ascendingMenuItem);
		add(ascendingMenuItem);
		descendingMenuItem = new JRadioButtonMenuItem(MailResourceLoader
				.getString("menu", "mainframe", "menu_view_sort_desc"));
		descendingMenuItem.setActionCommand("Descending");
		descendingMenuItem.addActionListener(this);
		orderGroup.add(descendingMenuItem);
		add(descendingMenuItem);

		//update(observable, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		TableViewOwner table = (TableViewOwner) getFrameMediator();

		if (action.equals("Ascending")) {
			table.getTableController().setSortingOrder(true);

		} else if (action.equals("Descending")) {
			table.getTableController().setSortingOrder(false);

		} else {
			table.getTableController().setSortingColumn(action);

		}

		table.getTableController().getSortingStateObservable()
				.notifyObservers();

		//update(observable, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable observable, Object object) {
		String column = ((SortingStateObservable) observable).getColumn();
		boolean ascending = ((SortingStateObservable) observable).isOrder();

		updateState(column, ascending);
	}

	private void updateState(String column, boolean ascending) {
		if (columnGroup == null) {
			return;
		}

		Enumeration enumeration = columnGroup.getElements();

		while (enumeration.hasMoreElements()) {
			JRadioButtonMenuItem item = (JRadioButtonMenuItem) enumeration
					.nextElement();

			if (item.getActionCommand().equals(column)) {
				item.setSelected(true);

				break;
			}
		}

		if (ascending) {
			ascendingMenuItem.setSelected(true);
		} else {
			descendingMenuItem.setSelected(true);
		}
	}

	/**
	 * @see org.columba.core.gui.util.ISelectionListener#selectionChanged(org.columba.core.gui.util.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent e) {
		AbstractFolder[] selection = ((TreeSelectionChangedEvent) e)
				.getSelected();

		if (selection.length == 1) {
			if (!(selection[0] instanceof AbstractMessageFolder)) {
				return;
			}

			selectedFolder = (AbstractMessageFolder) selection[0];

			createSubMenu();

			XmlElement xmlElement = ((MailFrameMediator) getFrameMediator())
					.getFolderOptionsController().getConfigNode(selectedFolder,
							"SortingOptions");

			if (xmlElement != null) {
				// *20040510, karlpeder* columns may be null (first time we
				// visit a folder!?)
				IDefaultItem item = new DefaultItem(xmlElement);

				//String column = xmlElement.getAttribute("column");
				//String s = threadedview.getAttribute("order");
				boolean order = item.getBoolean("order");
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7897.java