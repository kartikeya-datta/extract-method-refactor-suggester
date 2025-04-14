error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1920.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1920.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1920.java
text:
```scala
e@@ditLastRow();

// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Library General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

package org.columba.mail.gui.composer.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.columba.addressbook.folder.HeaderItem;
import org.columba.addressbook.folder.HeaderItemList;
import org.columba.addressbook.gui.table.AddressbookTableModel;
import org.columba.addressbook.gui.table.util.HeaderColumnInterface;
import org.columba.addressbook.util.AddressbookResourceLoader;

/**
 * @author frd
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class AddressbookTableView extends JTable {

	public JComboBox fieldComboBox;

	public AddressbookTableModel addressbookModel;

	public AddressComboBox comboBox;

	public AddressCellEditor addressEditor;

	public AddressbookTableView() {
		initComponents();

		initKeys();

	}

	protected void initKeys() {

	}

	protected void initComponents() {

		addressbookModel = new AddressbookTableModel();

		addressbookModel.editable = true;

		ComboBoxHeaderColumn c1 =
			new ComboBoxHeaderColumn(
				"field",
				AddressbookResourceLoader.getString("header", "field"));
		addressbookModel.addColumn(c1);

		DisplaynameHeaderColumn c2 =
			new DisplaynameHeaderColumn(
				"displayname",
				AddressbookResourceLoader.getString("header", "displayname"));
		addressbookModel.addColumn(c2);

		setModel(addressbookModel);

		addressEditor = new AddressCellEditor(this);

		getColumn("displayname").setCellEditor(addressEditor);

		getColumn("displayname").setCellRenderer(c2);

		TableColumn tc = getColumn("field");
		tc.setMaxWidth(80);
		tc.setMinWidth(80);
		fieldComboBox = new JComboBox();
		fieldComboBox.addItem("To");
		fieldComboBox.addItem("Cc");
		fieldComboBox.addItem("Bcc");
		FieldCellEditor editor = new FieldCellEditor(fieldComboBox, this);
		getColumn("field").setCellEditor(editor);
		getColumn("field").setCellRenderer(new FieldCellRenderer());

		setShowHorizontalLines(true);
		setShowVerticalLines(false);
		setIntercellSpacing(new Dimension(0, 2));
		setRowHeight(getRowHeight() + 4);

		setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
		setTableHeader(null);

	}

	public void initFocus(Component c) {
		(
			(JTextField) addressEditor
				.getEditor()
				.getEditorComponent())
				.setNextFocusableComponent(
			c);
	}

	public AddressbookTableModel getAddressbookTableModel() {
		return addressbookModel;
	}

	protected void makeVisible(int row, int column) {
		Rectangle r = getCellRect(row, column, true);

		scrollRectToVisible(r);
	}

	protected void focusToTextField() {
		JComboBox box = (JComboBox) getEditorComponent();
		if (box == null)
			return;

		JTextField textfield =
			(JTextField) box.getEditor().getEditorComponent();

		textfield.requestFocus();

	}

	protected void addEmptyRow() {

		if (emptyRowExists() == true)
			return;

		HeaderItem item = new HeaderItem(HeaderItem.CONTACT);
		item.add("displayname", "");
		item.add("field", "To");

		try {
			addressbookModel.addItem(item);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		makeVisible(getRowCount() - 1, 1);

	}

	protected void editLastRow() {

		int row = getRowCount() - 1;

		boolean b = editCellAt(row, 1);

		if (b == true) {
			focusToTextField();
		}

	}

	protected boolean isEmpty(int row) {
		HeaderItem item1 = (HeaderItem) addressbookModel.getValueAt(row, 1);

		String value = (String) item1.get("displayname");

		if (value.length() == 0)
			return true;

		return false;
	}

	protected boolean emptyRowExists() {
		int rowCount = getRowCount();

		if (rowCount == 0)
			return false;

		HeaderItem item1 =
			(HeaderItem) addressbookModel.getValueAt(rowCount - 1, 1);

		String value = (String) item1.get("displayname");

		if (value.length() == 0)
			return true;

		return false;
	}

	public void setHeaderItem(HeaderItem item) {
		int row = getEditingRow();

		addressbookModel.setHeaderItem(row, item);
	}

	public void removeEditingRow() {
		int rowCount = getRowCount();

		int row = getEditingRow();
		int column = getEditingColumn();

		if (row == rowCount - 1)
			return;

		if ((row == -1) || (column == -1)) {
			return;
		} else {
			getCellEditor(row, column).stopCellEditing();

		}

		HeaderItem[] items = new HeaderItem[1];
		items[0] = addressbookModel.getHeaderItem(row);

		addressbookModel.removeItem(items);

		boolean b = false;

		if (row == 0)
			b = editCellAt(0, 1);
		else
			b = editCellAt(row - 1, 1);

		if (b == true) {
			focusToTextField();
		}
	}

	public void appendRow() {
		int rowCount = getRowCount();
		int selected = getSelectedRow();

		if (emptyRowExists() == false) {

			addEmptyRow();
			//editLastRow();
		} else
			editLastRow();

	}

	public void cleanupHeaderItemList() {
		for (int i = getRowCount() - 1; i >= 0; i--) {
			boolean b = isEmpty(i);

			if (b == true) {
				HeaderItem[] items = new HeaderItem[1];
				items[0] = addressbookModel.getHeaderItem(i);

				addressbookModel.removeItem(items);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1920.java