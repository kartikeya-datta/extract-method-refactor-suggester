error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9354.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9354.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9354.java
text:
```scala
s@@etVisible(true);

/*
 * ViewRegisters.java - View registers dialog
 * Copyright (C) 1999, 2000, 2001 Slava Pestov
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

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import org.gjt.sp.jedit.*;

public class ViewRegisters extends EnhancedDialog
{
	public ViewRegisters(View view)
	{
		super(view,jEdit.getProperty("view-registers.title"),true);

		JPanel content = new JPanel(new BorderLayout());
		content.setBorder(new EmptyBorder(12,12,12,12));
		setContentPane(content);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(0,12,0,0));

		JLabel label = new JLabel(jEdit.getProperty("view-registers.register"));
		label.setBorder(new EmptyBorder(0,0,3,0));
		panel.add(BorderLayout.NORTH,label);

		DefaultListModel registerModel = new DefaultListModel();
		registerList = new JList(registerModel);
		registerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		registerList.setCellRenderer(new Renderer());

		Registers.Register[] registers = Registers.getRegisters();

		int index = 0;
		for(int i = 0; i < registers.length; i++)
		{
			Registers.Register reg = registers[i];
			if(reg == null)
				continue;

			String value = reg.toString();
			if(value == null || value.length() == 0)
				continue;

			registerModel.addElement(new Character((char)i));
		}

		if(registerModel.getSize() == 0)
			registerModel.addElement(jEdit.getProperty("view-registers.none"));

		panel.add(BorderLayout.CENTER,new JScrollPane(registerList));

		content.add(BorderLayout.WEST,panel);

		panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(0,12,0,0));

		label = new JLabel(jEdit.getProperty("view-registers.contents"));
		label.setBorder(new EmptyBorder(0,0,3,0));
		panel.add(BorderLayout.NORTH,label);

		contentTextArea = new JTextArea(10,80);
		contentTextArea.setEditable(false);
		panel.add(BorderLayout.CENTER,new JScrollPane(contentTextArea));
		content.add(BorderLayout.CENTER,panel);

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.setBorder(new EmptyBorder(12,0,0,0));
		close = new JButton(jEdit.getProperty("common.close"));
		close.addActionListener(new ActionHandler());
		panel.add(Box.createGlue());
		panel.add(close);
		panel.add(Box.createGlue());
		getRootPane().setDefaultButton(close);
		content.add(BorderLayout.SOUTH,panel);

		registerList.addListSelectionListener(new ListHandler());
		registerList.setSelectedIndex(index);

		pack();
		setLocationRelativeTo(view);
		show();
	}

	// EnhancedDialog implementation
	public void ok()
	{
		dispose();
	}

	public void cancel()
	{
		dispose();
	}
	// end EnhancedDialog implementation

	// private members
	private JList registerList;
	private JTextArea contentTextArea;
	private JButton close;

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

				setText(label);
			}

			return this;
		}
	}

	class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			if(evt.getSource() == close)
				cancel();
		}
	}

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

			contentTextArea.setText(reg.toString());
			contentTextArea.setCaretPosition(0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9354.java