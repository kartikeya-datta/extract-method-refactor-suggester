error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2796.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2796.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2796.java
text:
```scala
D@@ockPositionCellRenderer.this.setRequestFocusEnabled(false);

/*
 * DockingOptionPane.java - Dockable window options panel
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2000, 2001, 2002 Slava Pestov
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.gjt.sp.jedit.options;

//{{{ Imports
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import org.gjt.sp.jedit.gui.*;
import org.gjt.sp.jedit.*;
//}}}

//{{{ DockingOptionPane class
public class DockingOptionPane extends AbstractOptionPane
{
	//{{{ DockingOptionPane constructor
	public DockingOptionPane()
	{
		super("docking");
	} //}}}

	//{{{ _init() method
	public void _init()
	{
		Box box = new Box(BoxLayout.X_AXIS);
		ButtonGroup grp = new ButtonGroup();

		layout1 = new JToggleButton(GUIUtilities.loadIcon("dock_layout1.gif"));
		grp.add(layout1);
		box.add(layout1);

		box.add(Box.createHorizontalStrut(6));

		layout2 = new JToggleButton(GUIUtilities.loadIcon("dock_layout2.gif"));
		grp.add(layout2);
		box.add(layout2);

		if(jEdit.getBooleanProperty("view.docking.alternateLayout"))
			layout2.setSelected(true);
		else
			layout1.setSelected(true);

		addComponent(jEdit.getProperty("options.docking.layout"),box);

		addComponent(Box.createVerticalStrut(6));

		GridBagConstraints cons = new GridBagConstraints();
		cons.gridy = 3;
		cons.gridwidth = cons.gridheight = GridBagConstraints.REMAINDER;
		cons.fill = GridBagConstraints.BOTH;
		cons.weightx = cons.weighty = 1.0f;

		JScrollPane windowScroller = createWindowTableScroller();
		gridBag.setConstraints(windowScroller,cons);
		add(windowScroller);
	} //}}}

	//{{{ _save() method
	public void _save()
	{
		jEdit.setBooleanProperty("view.docking.alternateLayout",
			layout2.isSelected());
		windowModel.save();
	} //}}}

	//{{{ Private members

	//{{{ Instance variables
	private JToggleButton layout1;
	private JToggleButton layout2;
	private JTable windowTable;
	private WindowTableModel windowModel;
	//}}}

	//{{{ createWindowTableScroller() method
	private JScrollPane createWindowTableScroller()
	{
		windowModel = createWindowModel();
		windowTable = new JTable(windowModel);
		windowTable.getTableHeader().setReorderingAllowed(false);
		windowTable.setColumnSelectionAllowed(false);
		windowTable.setRowSelectionAllowed(false);
		windowTable.setCellSelectionEnabled(false);

		DockPositionCellRenderer comboBox = new DockPositionCellRenderer();
		windowTable.setRowHeight(comboBox.getPreferredSize().height);
		TableColumn column = windowTable.getColumnModel().getColumn(1);
		column.setCellRenderer(comboBox);
		column.setCellEditor(new DefaultCellEditor(new DockPositionCellRenderer()));

		Dimension d = windowTable.getPreferredSize();
		d.height = Math.min(d.height,200);
		JScrollPane scroller = new JScrollPane(windowTable);
		scroller.setPreferredSize(d);
		return scroller;
	} //}}}

	//{{{ createWindowModel() method
	private WindowTableModel createWindowModel()
	{
		return new WindowTableModel();
	} //}}}

	//}}}

	//{{{ DockPositionCellRenderer class
	class DockPositionCellRenderer extends JComboBox
		implements TableCellRenderer
	{
		DockPositionCellRenderer()
		{
			super(new String[] {
				DockableWindowManager.FLOATING,
				DockableWindowManager.TOP,
				DockableWindowManager.LEFT,
				DockableWindowManager.BOTTOM,
				DockableWindowManager.RIGHT
			});
			setRequestFocusEnabled(false);
		}

		public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus,
			int row, int column)
		{
			setSelectedItem(value);
			return this;
		}
	} //}}}
} //}}}

//{{{ WindowTableModel class
class WindowTableModel extends AbstractTableModel
{
	private Vector windows;

	//{{{ WindowTableModel constructor
	WindowTableModel()
	{
		windows = new Vector();

		// old dockable window API compatibility code
		Object[] list = EditBus.getNamedList(DockableWindow.DOCKABLE_WINDOW_LIST);
		if(list != null)
		{
			for(int i = 0; i < list.length; i++)
			{
				windows.addElement(new Entry((String)list[i]));
			}
		}
		// end compatibility code

		String[] dockables = DockableWindowManager.getRegisteredDockableWindows();
		for(int i = 0; i < dockables.length; i++)
		{
			windows.addElement(new Entry(dockables[i]));
		}

		sort();
	} //}}}

	//{{{ sort() method
	public void sort()
	{
		MiscUtilities.quicksort(windows,new WindowCompare());
		fireTableDataChanged();
	} //}}}

	//{{{ getColumnCount() method
	public int getColumnCount()
	{
		return 2;
	} //}}}

	//{{{ getRowCount() method
	public int getRowCount()
	{
		return windows.size();
	} //}}}

	//{{{ getColumnClass() method
	public Class getColumnClass(int col)
	{
		switch(col)
		{
		case 0:
		case 1:
			return String.class;
		default:
			throw new InternalError();
		}
	} //}}}

	//{{{ getValueAt() method
	public Object getValueAt(int row, int col)
	{
		Entry window = (Entry)windows.elementAt(row);
		switch(col)
		{
		case 0:
			return window.title;
		case 1:
			return window.dockPosition;
		default:
			throw new InternalError();
		}
	} //}}}

	//{{{ isCellEditable() method
	public boolean isCellEditable(int row, int col)
	{
		return (col != 0);
	} //}}}

	//{{{ setValueAt() method
	public void setValueAt(Object value, int row, int col)
	{
		if(col == 0)
			return;

		Entry window = (Entry)windows.elementAt(row);
		switch(col)
		{
		case 1:
			window.dockPosition = (String)value;
			break;
		default:
			throw new InternalError();
		}

		fireTableRowsUpdated(row,row);
	} //}}}

	//{{{ getColumnName() method
	public String getColumnName(int index)
	{
		switch(index)
		{
		case 0:
			return jEdit.getProperty("options.docking.title");
		case 1:
			return jEdit.getProperty("options.docking.dockPosition");
		default:
			throw new InternalError();
		}
	} //}}}

	//{{{ save() method
	public void save()
	{
		for(int i = 0; i < windows.size(); i++)
		{
			((Entry)windows.elementAt(i)).save();
		}
	} //}}}

	//{{{ Entry class
	class Entry
	{
		String name;
		String title;
		String dockPosition;

		Entry(String name)
		{
			this.name = name;
			title = jEdit.getProperty(name + ".title");
			if(title == null)
				title = name;

			dockPosition = jEdit.getProperty(name + ".dock-position");
			if(dockPosition == null)
				dockPosition = DockableWindowManager.FLOATING;
		}

		void save()
		{
			jEdit.setProperty(name + ".dock-position",dockPosition);
		}
	} //}}}

	//{{{ WindowCompare class
	class WindowCompare implements MiscUtilities.Compare
	{
		public int compare(Object obj1, Object obj2)
		{
			Entry e1 = (Entry)obj1;
			Entry e2 = (Entry)obj2;

			return MiscUtilities.compareStrings(
				e1.title,e2.title,true);
		}
	} //}}}
} //}}}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2796.java