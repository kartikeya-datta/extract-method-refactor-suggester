error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6248.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6248.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6248.java
text:
```scala
l@@abel = String.valueOf(name);

/*
 * RegisterViewer.java - Dockable view of register contents
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2004, 2005 Nicholas O'Leary
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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.msg.RegisterChanged;
import org.gjt.sp.util.Log;
//}}}

public class RegisterViewer extends JPanel implements EBComponent
{
	//{{{ RegisterViewer constructor
	public RegisterViewer(View view, String position)
	{
		super(new BorderLayout());
		this.view = view;
		JLabel label = new JLabel(
			jEdit.getProperty("view-registers.title"));
		label.setBorder(new EmptyBorder(0,0,3,0));
		add(BorderLayout.NORTH,label);

		DefaultListModel registerModel = new DefaultListModel();
		registerList = new JList(registerModel);
		registerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		registerList.setCellRenderer(new Renderer());
		registerList.addListSelectionListener(new ListHandler());
		registerList.addMouseListener(new MouseHandler());

		contentTextArea = new JTextArea(10,20);
		contentTextArea.setEditable(true);
		documentHandler = new DocumentHandler();
		//contentTextArea.getDocument().addDocumentListener(documentHandler);
		contentTextArea.addFocusListener(new FocusHandler());

		int orientation = JSplitPane.HORIZONTAL_SPLIT;
		if (position.equals(DockableWindowManager.LEFT) ||
			position.equals(DockableWindowManager.RIGHT))
			orientation = JSplitPane.VERTICAL_SPLIT;

		add(BorderLayout.CENTER,new JSplitPane(orientation,
			true,
			new JScrollPane(registerList),
			new JScrollPane(contentTextArea)
			));

		refreshList();
	} //}}}

	//{{{ handleMessage
	public void handleMessage(EBMessage msg)
	{
		if (msg instanceof RegisterChanged)
		{
			if (((RegisterChanged)msg).getRegisterName() != '%')
				refreshList();
		}
	}//}}}

	//{{{ addNotify() method
	public void addNotify()
	{
		super.addNotify();
		EditBus.addToBus(this);
	} //}}}

	//{{{ removeNotify() method
	public void removeNotify()
	{
		super.removeNotify();
		EditBus.removeFromBus(this);
	} //}}}

	//{{{ Private members

	//{{{ Instance variables
	private JList registerList;
	private JTextArea contentTextArea;
	private DocumentHandler documentHandler;
	private View view;
	private boolean editing;
	//}}}

	//{{{ refreshList
	private void refreshList()
	{
		DefaultListModel registerModel = (DefaultListModel)registerList.getModel();
		Object o = registerList.getSelectedValue();
		int index = 0;
		int selected = -1;
		if (o != null && o instanceof Character)
			selected = ((Character)o).charValue();

		registerModel.removeAllElements();
		Registers.Register[] registers = Registers.getRegisters();

		for(int i = 0; i < registers.length; i++)
		{
			Registers.Register reg = registers[i];
			if(reg == null)
				continue;
			if (i == '%')
				continue;

			String value = reg.toString();
			if(value == null) // || value.length() == 0)
				continue;
			if (i == selected)
				index = registerModel.size();
			registerModel.addElement(new Character((char)i));
		}

		if(registerModel.getSize() == 0)
		{
			registerModel.addElement(jEdit.getProperty("view-registers.none"));
			registerList.setEnabled(false);
		}
		else
			registerList.setEnabled(true);
		registerList.setSelectedIndex(index);
	} //}}}

	//{{{ updateSelected
	private void updateSelected()
	{
		Object o = registerList.getSelectedValue();
		if (o == null)
			return;
		else if (o instanceof Character)
		{
			Registers.Register reg = Registers.getRegister(((Character)o).charValue());
			if (!editing)
			{
				contentTextArea.setText(reg.toString());
				contentTextArea.setEditable(true);
			}
		}
		else
		{
			if (!editing)
			{
				contentTextArea.setText("");
				contentTextArea.setEditable(false);
			}
		}
		if (!editing)
			contentTextArea.setCaretPosition(0);
	}//}}}

	//{{{ insertRegister
	private void insertRegister()
	{
		Object o = registerList.getSelectedValue();
		if (o == null || !(o instanceof Character))
			return;
		Registers.Register reg = Registers.getRegister(((Character)o).charValue());
		view.getTextArea().setSelectedText(reg.toString());
		view.getTextArea().requestFocus();
	} //}}}

	//}}}

	//{{{ Inner classes

	//{{{ Renderer Class
	class Renderer extends DefaultListCellRenderer
	{
		public Component getListCellRendererComponent(
			JList list, Object value, int index,
			boolean isSelected, boolean cellHasFocus)
		{
			super.getListCellRendererComponent(list,value,
			index,isSelected,cellHasFocus);

			if(value instanceof Character)
			{
				char name = ((Character)value).charValue();

				String label;

				if(name == '\n')
					label = "\n";
				else if(name == '\t')
					label = "\t";
				else if(name == '$')
					label = jEdit.getProperty("view-registers.clipboard");
				else if(name == '%')
					label = jEdit.getProperty("view-registers.selection");
				else
					label = String.valueOf((char)name);
				String registerValue = Registers.getRegister(name).toString();
				if (registerValue.length() > 100)
					registerValue = registerValue.substring(0,100)+"...";
				registerValue = registerValue.replaceAll("\n"," ");
				registerValue = registerValue.replaceAll("\t"," ");
				setText(label+" : "+registerValue);
			}

			return this;
		}
	} //}}}

	//{{{ ListHandler Class
	class ListHandler implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent evt)
		{
			Object value = registerList.getSelectedValue();
			if(!(value instanceof Character))
				return;

			char name = ((Character)value).charValue();

			Registers.Register reg = Registers.getRegister(name);
			if(reg == null)
				return;
			updateSelected();
		}
	} //}}}

	//{{{ MouseHandler Class
	class MouseHandler extends MouseAdapter
	{
		/*public void mousePressed(MouseEvent evt)
		{
			if(evt.isConsumed())
				return;
			int index = registerList.locationToIndex(
				evt.getPoint());
			registerList.setSelectedIndex(index);
		} */

		public void mouseClicked(MouseEvent evt)
		{
			if (evt.getClickCount() % 2 == 0)
				insertRegister();
		}
	} //}}}

	//{{{ DocumentHandler Class
	class DocumentHandler implements DocumentListener
	{
		public void changedUpdate(DocumentEvent e)
		{
			updateRegisterSafely();
		}

		public void insertUpdate(DocumentEvent e)
		{
			updateRegisterSafely();
		}

		public void removeUpdate(DocumentEvent e)
		{
			updateRegisterSafely();
		}

		private void updateRegisterSafely()
		{
			try
			{
				editing = true;
				updateRegister();
			}
			finally
			{
				editing = false;
			}
		}
		
		private void updateRegister()
		{
			Object value = registerList.getSelectedValue();
			if(!(value instanceof Character))
				return;
			char name = ((Character)value).charValue();
			Registers.setRegister(name,contentTextArea.getText());
		}
	} //}}}

	//{{{ FocusHandler Class
	class FocusHandler implements FocusListener
	{
		public void focusGained(FocusEvent e)
		{
			contentTextArea.getDocument().addDocumentListener(documentHandler);
		}
		public void focusLost(FocusEvent e)
		{
			contentTextArea.getDocument().removeDocumentListener(documentHandler);
		}
	}//}}}

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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6248.java