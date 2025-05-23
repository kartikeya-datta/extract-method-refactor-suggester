error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4018.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4018.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4018.java
text:
```scala
r@@eturn new Boolean(entry.checked);

/*
 * JCheckBoxList.java - A list, each item can be checked or unchecked
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

package org.gjt.sp.jedit.gui;

//{{{ Imports
import java.awt.Component;
import java.awt.Font;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.*;
//}}}

/**
 * A list where items can be selected and checked off independently.
 * @since jEdit 3.2pre9
 */
public class JCheckBoxList extends JTable
{
	//{{{ JCheckBoxList constructor
	/**
	 * Creates a checkbox list with the given list of objects. The elements
	 * of this array can either be Entry instances, or other objects (if the
	 * latter, they will default to being unchecked).
	 */
	public JCheckBoxList(Object[] items)
	{
		setModel(items);
	} //}}}

	//{{{ JCheckBoxList constructor
	/**
	 * Creates a checkbox list with the given list of objects. The elements
	 * of this vector can either be Entry instances, or other objects (if the
	 * latter, they will default to being unchecked).
	 */
	public JCheckBoxList(Vector items)
	{
		setModel(items);
	} //}}}

	//{{{ setModel() method
	/**
	 * Sets the model to the given list of objects. The elements of this
	 * array can either be Entry instances, or other objects (if the
	 * latter, they will default to being unchecked).
	 */
	public void setModel(Object[] items)
	{
		setModel(new CheckBoxListModel(items));
		init();
	} //}}}

	//{{{ setModel() method
	/**
	 * Sets the model to the given list of objects. The elements of this
	 * vector can either be Entry instances, or other objects (if the
	 * latter, they will default to being unchecked).
	 */
	public void setModel(Vector items)
	{
		setModel(new CheckBoxListModel(items));
		init();
	} //}}}

	//{{{ getCheckedValues() method
	public Object[] getCheckedValues()
	{
		Vector values = new Vector();
		CheckBoxListModel model = (CheckBoxListModel)getModel();
		for(int i = 0; i < model.items.size(); i++)
		{
			Entry entry = (Entry)model.items.elementAt(i);
			if(entry.checked && !entry.caption)
				values.addElement(entry.value);
		}

		Object[] retVal = new Object[values.size()];
		values.copyInto(retVal);
		return retVal;
	} //}}}

	//{{{ selectAll() method
	public void selectAll()
	{
		CheckBoxListModel model = (CheckBoxListModel)getModel();
		for(int i = 0; i < model.items.size(); i++)
		{
			Entry entry = (Entry)model.items.elementAt(i);
			if(!entry.caption)
				entry.checked = true;
		}

		model.fireTableRowsUpdated(0,model.getRowCount());
	} //}}}

	//{{{ getValues() method
	public Entry[] getValues()
	{
		CheckBoxListModel model = (CheckBoxListModel)getModel();
		Entry[] retVal = new Entry[model.items.size()];
		model.items.copyInto(retVal);
		return retVal;
	} //}}}

	//{{{ getSelectedValue() method
	public Object getSelectedValue()
	{
		int row = getSelectedRow();
		if(row == -1)
			return null;
		else
			return getModel().getValueAt(row,1);
	} //}}}

	//{{{ getCellRenderer() method
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		if(column == 0)
		{
			Entry entry = (Entry)((CheckBoxListModel)getModel()).items.get(row);
			if(entry.caption)
				return dummy;
		}

		return super.getCellRenderer(row,column);
	} //}}}

	//{{{ Private members
	private TableCellRenderer dummy;

	//{{{ init() method
	private void init()
	{
		dummy = new DummyRenderer();
		getSelectionModel().setSelectionMode(ListSelectionModel
			.SINGLE_SELECTION);
		setShowGrid(false);
		setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		TableColumn column = getColumnModel().getColumn(0);
		int checkBoxWidth = new JCheckBox().getPreferredSize().width;
		column.setPreferredWidth(checkBoxWidth);
		column.setMinWidth(checkBoxWidth);
		column.setWidth(checkBoxWidth);
		column.setMaxWidth(checkBoxWidth);
		column.setResizable(false);

		column = getColumnModel().getColumn(1);
		column.setCellRenderer(new LabelRenderer());
	} //}}}

	//}}}

	//{{{ Entry class
	/**
	 * A check box list entry.
	 */
	public static class Entry
	{
		boolean checked;
		boolean caption;
		Object value;

		public Entry(Object value)
		{
			this.caption = true;
			this.value = value;
		}

		public Entry(boolean checked, Object value)
		{
			this.checked = checked;
			this.value = value;
		}

		public boolean isChecked()
		{
			return checked;
		}

		public Object getValue()
		{
			return value;
		}
	} //}}}

	//{{{ DummyRenderer class
	private class DummyRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
		{
			return super.getTableCellRendererComponent(table,null /* value */,
				isSelected,false /* hasFocus */,row,column);
		}
	} //}}}

	//{{{ LabelRenderer class
	private class LabelRenderer extends DefaultTableCellRenderer
	{
		Font plainFont, boldFont;

		LabelRenderer()
		{
			plainFont = UIManager.getFont("Tree.font");
			boldFont = plainFont.deriveFont(Font.BOLD);
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
		{
			super.getTableCellRendererComponent(table,value,isSelected,
				hasFocus,row,column);

			Entry entry = (Entry)((CheckBoxListModel)getModel()).items.get(row);
			if(entry.caption)
				setFont(boldFont);
			else
				setFont(plainFont);
			return this;
		}
	} //}}}
}

class CheckBoxListModel extends AbstractTableModel
{
	Vector items;

	CheckBoxListModel(Vector _items)
	{
		items = new Vector(_items.size());
		for(int i = 0; i < _items.size(); i++)
		{
			items.addElement(createEntry(_items.elementAt(i)));
		}
	}

	CheckBoxListModel(Object[] _items)
	{
		items = new Vector(_items.length);
		for(int i = 0; i < _items.length; i++)
		{
			items.addElement(createEntry(_items[i]));
		}
	}

	private JCheckBoxList.Entry createEntry(Object obj)
	{
		if(obj instanceof JCheckBoxList.Entry)
			return (JCheckBoxList.Entry)obj;
		else
			return new JCheckBoxList.Entry(false,obj);
	}

	public int getRowCount()
	{
		return items.size();
	}

	public int getColumnCount()
	{
		return 2;
	}

	public String getColumnName(int col)
	{
		return null;
	}

	public Object getValueAt(int row, int col)
	{
		JCheckBoxList.Entry entry = (JCheckBoxList.Entry)items.elementAt(row);
		switch(col)
		{
		case 0:
			return Boolean.valueOf(entry.checked);
		case 1:
			return entry.value;
		default:
			throw new InternalError();
		}
	}

	public Class getColumnClass(int col)
	{
		switch(col)
		{
		case 0:
			return Boolean.class;
		case 1:
			return String.class;
		default:
			throw new InternalError();
		}
	}

	public boolean isCellEditable(int row, int col)
	{
		JCheckBoxList.Entry entry = (JCheckBoxList.Entry)items.elementAt(row);
		return col == 0 && !entry.caption;
	}

	public void setValueAt(Object value, int row, int col)
	{
		if(col == 0)
		{
			JCheckBoxList.Entry entry = (JCheckBoxList.Entry)items.elementAt(row);
			if(!entry.caption)
			{
				entry.checked = (value.equals(Boolean.TRUE));
				fireTableRowsUpdated(row,row);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4018.java