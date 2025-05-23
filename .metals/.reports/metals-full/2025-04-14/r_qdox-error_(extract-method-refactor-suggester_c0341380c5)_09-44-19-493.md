error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8952.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8952.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8952.java
text:
```scala
c@@ase 0: return " ";

/*
 * InstallPanel.java - For installing plugins
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2002 Kris Kopicki
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

package org.gjt.sp.jedit.pluginmgr;

//{{{ Imports
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.*;
import org.gjt.sp.jedit.gui.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.Log;
//}}}

class InstallPanel extends JPanel
{
	//{{{ InstallPanel constructor
	InstallPanel(PluginManager window, boolean updates)
	{
		super(new BorderLayout(12,12));

		this.window = window;
		this.updates = updates;

		setBorder(new EmptyBorder(12,12,12,12));

		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true);

		/* Setup the table */
		table = new JTable(pluginModel = new PluginTableModel());
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0,0));
		table.setRowHeight(table.getRowHeight() + 2);
		table.setPreferredScrollableViewportSize(new Dimension(500,200));
		table.setRequestFocusEnabled(false);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setDefaultRenderer(Object.class, new TextRenderer(
			(DefaultTableCellRenderer)table.getDefaultRenderer(Object.class)));

		TableColumn col1 = table.getColumnModel().getColumn(0);
		TableColumn col2 = table.getColumnModel().getColumn(1);
		TableColumn col3 = table.getColumnModel().getColumn(2);
		TableColumn col4 = table.getColumnModel().getColumn(3);
		TableColumn col5 = table.getColumnModel().getColumn(4);

		col1.setPreferredWidth(50);
		col1.setMinWidth(50);
		col1.setMaxWidth(50);
		col1.setResizable(false);

		col2.setPreferredWidth(180);
		col3.setPreferredWidth(130);
		col4.setPreferredWidth(70);
		col5.setPreferredWidth(70);

		JTableHeader header = table.getTableHeader();
		header.setReorderingAllowed(false);
		header.addMouseListener(new HeaderMouseHandler());

		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.getViewport().setBackground(table.getBackground());
		split.setTopComponent(scrollpane);

		/* Create description */
		JScrollPane infoPane = new JScrollPane(new PluginInfoBox());
		infoPane.setPreferredSize(new Dimension(500,100));
		split.setBottomComponent(infoPane);

		split.setDividerLocation(200);

		add(BorderLayout.CENTER,split);

		/* Create buttons */
		Box buttons = new Box(BoxLayout.X_AXIS);

		buttons.add(new InstallButton());
		buttons.add(Box.createHorizontalStrut(12));
		buttons.add(new SelectallButton());
		buttons.add(Box.createGlue());
		buttons.add(new SizeLabel());

		add(BorderLayout.SOUTH,buttons);
	} //}}}

	//{{{ updateModel() method
	public void updateModel()
	{
		pluginModel.update();
	} //}}}

	//{{{ Private members

	//{{{ Variables
	private JTable table;
	private PluginTableModel pluginModel;
	private PluginManager window;

	private boolean updates;
	//}}}

	//{{{ formatSize() method
	private String formatSize(int size)
	{
		NumberFormat df = NumberFormat.getInstance();
		df.setMaximumFractionDigits(1);
		df.setMinimumFractionDigits(0);
		String sizeText;
		if (size < 1048576)
			sizeText = size/1024 + "KB";
		else
			sizeText = df.format(size/1048576d) + "MB";
		return sizeText;
	} //}}}

	//}}}

	//{{{ Inner classes

	//{{{ PluginTableModel class
	class PluginTableModel extends AbstractTableModel
	{
		private ArrayList entries = new ArrayList();
		private int sortType = EntryCompare.CATEGORY;

		//{{{ getColumnClass() method
		public Class getColumnClass(int columnIndex)
		{
			switch (columnIndex)
			{
				case 0: return Boolean.class;
				case 1:
				case 2:
				case 3:
				case 4: return Object.class;
				default: throw new Error("Column out of range");
			}
		} //}}}

		//{{{ getColumnCount() method
		public int getColumnCount()
		{
			return 5;
		} //}}}

		//{{{ getColumnName() method
		public String getColumnName(int column)
		{
			switch (column)
			{
				case 0: return " "+jEdit.getProperty("install-plugins.info.install");
				case 1: return " "+jEdit.getProperty("install-plugins.info.name");
				case 2: return " "+jEdit.getProperty("install-plugins.info.category");
				case 3: return " "+jEdit.getProperty("install-plugins.info.version");
				case 4: return " "+jEdit.getProperty("install-plugins.info.size");
				default: throw new Error("Column out of range");
			}
		} //}}}

		//{{{ getEntry() method
		public Entry getEntry(int rowIndex)
		{
			return (Entry)entries.get(rowIndex);
		} //}}}

		//{{{ getRowCount() method
		public int getRowCount()
		{
			return entries.size();
		} //}}}

		//{{{ getValueAt() method
		public Object getValueAt(int rowIndex,int columnIndex)
		{
			Entry entry = (Entry)entries.get(rowIndex);

			switch (columnIndex)
			{
				case 0: return new Boolean(entry.install);
				case 1: return entry.name;
				case 2: return entry.set;
				case 3: return entry.version;
				case 4: return formatSize(entry.size);
				default: throw new Error("Column out of range");
			}
		} //}}}

		//{{{ isCellEditable() method
		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
			return (columnIndex == 0);
		} //}}}

		//{{{ setSelectAll() method
		public void setSelectAll(boolean b)
		{
			int length = getRowCount();
			for (int i = 0; i < length; i++)
			{
				if (b)
					setValueAt(new Boolean(true),i,0);
				else
				{
					Entry entry = getEntry(i);
					entry.parents = new LinkedList();
					entry.install = false;
				}
			}
			fireTableChanged(new TableModelEvent(this));
		} //}}}

		//{{{ setSortType() method
		public void setSortType(int type)
		{
			sortType = type;
			sort(type);
		} //}}}

		//{{{ setValueAt() method
		public void setValueAt(Object aValue, int row, int column)
		{
			if (column != 0) return;

			Entry entry = getEntry(row);
			Vector deps = entry.plugin.getCompatibleBranch().deps;
			boolean value = ((Boolean)aValue).booleanValue();
			if (!value)
			{
				if (entry.parents.size() > 0)
				{
					String[] args = {
						entry.name,
						entry.getParentString()
					};
					int result = GUIUtilities.confirm(
						window,"plugin-manager.dependency",
						args,JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE);
					if (result != JOptionPane.OK_OPTION)
						return;
					Iterator parentsIter = entry.parents.iterator();
					while(parentsIter.hasNext())
					{
						((Entry)parentsIter.next()).install = false;
					}

					fireTableRowsUpdated(0,getRowCount() - 1);
				}
			}

			for (int i = 0; i < deps.size(); i++)
			{
				PluginList.Dependency dep = (PluginList.Dependency)deps.elementAt(i);
				if (dep.what.equals("plugin"))
				{
					for (int j = 0; j < entries.size(); j++)
					{
						Entry temp = (Entry)entries.get(j);
						if (temp.plugin == dep.plugin)
						{
							if (value)
							{
								temp.parents.add(entry);
								setValueAt(new Boolean(true),j,0);
							}
							else
								temp.parents.remove(entry);
						}
					}
				}
			}

			entry.install = Boolean.TRUE.equals(aValue);
			fireTableCellUpdated(row,column);
		} //}}}

		//{{{ sort() method
		public void sort(int type)
		{
			Collections.sort(entries,new EntryCompare(type));
			fireTableChanged(new TableModelEvent(this));
		}
		//}}}

		//{{{ update() method
		public void update()
		{
			PluginList pluginList = window.getPluginList();

			if (pluginList == null) return;

			entries = new ArrayList();

			for(int i = 0; i < pluginList.pluginSets.size(); i++)
			{
				PluginList.PluginSet set = (PluginList.PluginSet)
					pluginList.pluginSets.get(i);
				for(int j = 0; j < set.plugins.size(); j++)
				{
					PluginList.Plugin plugin = (PluginList.Plugin)
						pluginList.pluginHash.get(set.plugins.get(j));
					PluginList.Branch branch = plugin.getCompatibleBranch();
					String installedVersion =
						plugin.getInstalledVersion();
					if (updates)
					{
						if(branch != null
							&& branch.canSatisfyDependencies()
							&& installedVersion != null
							&& MiscUtilities.compareStrings(branch.version,
							installedVersion,false) > 0)
						{
							entries.add(new Entry(plugin,set.name));
						}
					}
					else
					{
						if(installedVersion == null && plugin.canBeInstalled())
							entries.add(new Entry(plugin,set.name));
					}
				}
			}

			sort(sortType);

			fireTableChanged(new TableModelEvent(this));
		} //}}}
	} //}}}

	//{{{ Entry class
	class Entry
	{
		String name, version, author, date, description, set;
		int size;
		boolean install;
		PluginList.Plugin plugin;
		LinkedList parents = new LinkedList();

		Entry(PluginList.Plugin plugin, String set)
		{
			PluginList.Branch branch = plugin.getCompatibleBranch();
			boolean downloadSource = jEdit.getBooleanProperty("plugin-manager.downloadSource");
			int size = (downloadSource) ? branch.downloadSourceSize : branch.downloadSize;

			this.name = plugin.name;
			this.author = plugin.author;
			this.version = branch.version;
			this.size = size;
			this.date = branch.date;
			this.description = plugin.description;
			this.set = set;
			this.install = false;
			this.plugin = plugin;
		}

		String getParentString()
		{
			StringBuffer buf = new StringBuffer();
			Iterator iter = parents.iterator();
			while(iter.hasNext())
			{
				buf.append(((Entry)iter.next()).name);
				if(iter.hasNext())
					buf.append('\n');
			}
			return buf.toString();
		}
	} //}}}

	//{{{ PluginInfoBox class
	class PluginInfoBox extends JTextArea implements ListSelectionListener
	{
		public PluginInfoBox()
		{
			setEditable(false);
			setLineWrap(true);
			setWrapStyleWord(true);
			table.getSelectionModel().addListSelectionListener(this);
		}


		public void valueChanged(ListSelectionEvent e)
		{
			String text = "";
			if (table.getSelectedRowCount() == 1)
			{
				Entry entry = pluginModel.getEntry(table.getSelectedRow());
				text = jEdit.getProperty("install-plugins.info",
					new String[] {entry.author,entry.date,entry.description});
			}
			setText(text);
			setCaretPosition(0);
		}
	} //}}}

	//{{{ SizeLabel class
	class SizeLabel extends JLabel implements TableModelListener
	{
		private int size;

		public SizeLabel()
		{
			size = 0;
			setText(jEdit.getProperty("install-plugins.totalSize")+formatSize(size));
			pluginModel.addTableModelListener(this);
		}

		public void tableChanged(TableModelEvent e)
		{
			if (e.getType() == TableModelEvent.UPDATE)
			{
				size = 0;
				int length = pluginModel.getRowCount();
				for (int i = 0; i < length; i++)
				{
					Entry entry = pluginModel.getEntry(i);
					if (entry.install)
						size += entry.size;
				}
				setText(jEdit.getProperty("install-plugins.totalSize")+formatSize(size));
			}
		}
	} //}}}

	//{{{ SelectallButton class
	class SelectallButton extends JCheckBox implements ActionListener, TableModelListener
	{
		public SelectallButton()
		{
			super(jEdit.getProperty("install-plugins.select-all"));
			addActionListener(this);
			pluginModel.addTableModelListener(this);
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent evt)
		{
			pluginModel.setSelectAll(isSelected());
		}

		public void tableChanged(TableModelEvent e)
		{
			setEnabled(pluginModel.getRowCount() != 0);
			if (e.getType() == TableModelEvent.UPDATE)
			{
				int length = pluginModel.getRowCount();
				for (int i = 0; i < length; i++)
					if (!((Boolean)pluginModel.getValueAt(i,0)).booleanValue())
					{
						setSelected(false);
						return;
					}
				if (length > 0)
					setSelected(true);
			}
		}
	} //}}}

	//{{{ InstallButton class
	class InstallButton extends JButton implements ActionListener, TableModelListener
	{
		public InstallButton()
		{
			super(jEdit.getProperty("install-plugins.install"));
			pluginModel.addTableModelListener(this);
			addActionListener(this);
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent evt)
		{
			boolean downloadSource = jEdit.getBooleanProperty(
				"plugin-manager.downloadSource");
			boolean installUser = jEdit.getBooleanProperty(
				"plugin-manager.installUser");
			Roster roster = new Roster();
			String installDirectory;
			if(installUser)
			{
				installDirectory = MiscUtilities.constructPath(
					jEdit.getSettingsDirectory(),"jars");
			}
			else
			{
				installDirectory = MiscUtilities.constructPath(
					jEdit.getJEditHome(),"jars");
			}

			int length = pluginModel.getRowCount();
			LinkedList selected = new LinkedList();
			for (int i = 0; i < length; i++)
			{
				Entry entry = pluginModel.getEntry(i);
				if (entry.install)
				{
					entry.plugin.install(roster,installDirectory,downloadSource);
					selected.add(entry.plugin);
				}
			}

			if(roster.isEmpty())
				return;

			new PluginManagerProgress(window,roster);

			roster.performOperationsInAWTThread(window);
			pluginModel.update();
		}

		public void tableChanged(TableModelEvent e)
		{
			if (e.getType() == TableModelEvent.UPDATE)
			{
				int length = pluginModel.getRowCount();
				for (int i = 0; i < length; i++)
					if (((Boolean)pluginModel.getValueAt(i,0)).booleanValue())
					{
						setEnabled(true);
						return;
					}
				setEnabled(false);
			}
		}
	} //}}}

	//{{{ EntryCompare class
	static class EntryCompare implements Comparator
	{
		public static final int NAME = 0;
		public static final int CATEGORY = 1;

		private int type;

		public EntryCompare(int type)
		{
			this.type = type;
		}

		public int compare(Object o1, Object o2)
		{
			Entry e1 = (Entry)o1;
			Entry e2 = (Entry)o2;

			if (type == NAME)
				return e1.name.compareToIgnoreCase(e2.name);
			else
			{
				int result;
				if ((result = e1.set.compareToIgnoreCase(e2.set)) == 0)
					return e1.name.compareToIgnoreCase(e2.name);
				return result;
			}
		}
	} //}}}

	//{{{ HeaderMouseHandler()
	class HeaderMouseHandler extends MouseAdapter
	{
		public void mouseClicked(MouseEvent evt)
		{
			switch(table.getTableHeader().columnAtPoint(evt.getPoint()))
			{
				case 1:
					pluginModel.sort(EntryCompare.NAME);
					break;
				case 2:
					pluginModel.sort(EntryCompare.CATEGORY);
					break;
				default:
			}
		}
	} //}}}

	//{{{ TextRenderer
	class TextRenderer extends DefaultTableCellRenderer
	{
		private DefaultTableCellRenderer tcr;

		public TextRenderer(DefaultTableCellRenderer tcr)
		{
			this.tcr = tcr;
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
		{
			return tcr.getTableCellRendererComponent(table,value,isSelected,false,row,column);
		}
	} //}}}

	//}}}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8952.java