error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9908.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9908.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9908.java
text:
```scala
s@@etVisible(true);

/*
 * PasteFromListDialog.java - Paste previous/paste deleted dialog
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2003 Slava Pestov
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
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import org.gjt.sp.jedit.*;
//}}}

public class PasteFromListDialog extends EnhancedDialog
{
	//{{{ PasteFromListDialog constructor
	public PasteFromListDialog(String name, View view, ListModel model)
	{
		super(view,jEdit.getProperty(name + ".title"),true);
		this.view = view;

		JPanel content = new JPanel(new BorderLayout());
		content.setBorder(new EmptyBorder(12,12,12,12));
		setContentPane(content);
		JPanel center = new JPanel(new GridLayout(2,1,2,12));

		clips = new JList(model);
		clips.setCellRenderer(new Renderer());
		clips.setVisibleRowCount(12);

		clips.addMouseListener(new MouseHandler());
		clips.addListSelectionListener(new ListHandler());

		insert = new JButton(jEdit.getProperty("common.insert"));
		cancel = new JButton(jEdit.getProperty("common.cancel"));

		JLabel label = new JLabel(jEdit.getProperty(name + ".caption"));
		label.setBorder(new EmptyBorder(0,0,6,0));
		content.add(BorderLayout.NORTH,label);

		JScrollPane scroller = new JScrollPane(clips);
		scroller.setPreferredSize(new Dimension(500,150));
		center.add(scroller);

		clipText = new JTextArea();
		clipText.setEditable(false);
		scroller = new JScrollPane(clipText);
		scroller.setPreferredSize(new Dimension(500,150));
		center.add(scroller);

		content.add(center, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.setBorder(new EmptyBorder(12,0,0,0));
		panel.add(Box.createGlue());
		panel.add(insert);
		panel.add(Box.createHorizontalStrut(6));
		panel.add(cancel);
		panel.add(Box.createGlue());
		content.add(panel, BorderLayout.SOUTH);

		if(model.getSize() >= 1)
			clips.setSelectedIndex(0);
		updateButtons();

		getRootPane().setDefaultButton(insert);
		insert.addActionListener(new ActionHandler());
		cancel.addActionListener(new ActionHandler());

		GUIUtilities.requestFocus(this,clips);

		pack();
		setLocationRelativeTo(view);
		show();
	} //}}}

	//{{{ ok() method
	public void ok()
	{
		Object[] selected = clips.getSelectedValues();
		if(selected == null || selected.length == 0)
		{
			getToolkit().beep();
			return;
		}

		view.getTextArea().setSelectedText(getSelectedClipText());

		dispose();
	} //}}}

	//{{{ cancel() method
	public void cancel()
	{
		dispose();
	} //}}}

	//{{{ Private members

	//{{{ Instance variables
	private View view;
	private JList clips;
	private JTextArea clipText;
	private JButton insert;
	private JButton cancel;
	//}}}

	//{{{ getSelectedClipText()
	private String getSelectedClipText()
	{
		Object[] selected = clips.getSelectedValues();
		StringBuffer clip = new StringBuffer();
		for(int i = 0; i < selected.length; i++)
		{
			if(i != 0)
				clip.append('\n');
			clip.append(selected[i]);
		}
		return clip.toString();
	}
	//}}}

	//{{{ updateButtons() method
	private void updateButtons()
	{
		int selected = clips.getSelectedIndex();
		insert.setEnabled(selected != -1);
	} //}}}

	//{{{ showClipText() method
	private void showClipText()
	{
		Object[] selected = clips.getSelectedValues();
		if(selected == null || selected.length == 0)
			clipText.setText("");
		else
			clipText.setText(getSelectedClipText());
		clipText.setCaretPosition(0);
	}
	//}}}

	//}}}

	//{{{ Renderer class
	class Renderer extends DefaultListCellRenderer
	{
		String shorten(String item)
		{
			StringBuffer buf = new StringBuffer();
			// workaround for Swing rendering labels starting
			// with <html> using the HTML engine
			if(item.toLowerCase().startsWith("<html>"))
				buf.append(' ');
			boolean ws = true;
			for(int i = 0; i < item.length(); i++)
			{
				char ch = item.charAt(i);
				if(Character.isWhitespace(ch))
				{
					if(ws)
						/* do nothing */;
					else
					{
						buf.append(' ');
						ws = true;
					}
				}
				else
				{
					ws = false;
					buf.append(ch);
				}
			}

			if(buf.length() == 0)
				return jEdit.getProperty("paste-from-list.whitespace");
			return buf.toString();
		}

		public Component getListCellRendererComponent(
			JList list, Object value, int index,
			boolean isSelected, boolean cellHasFocus)
		{
			super.getListCellRendererComponent(list,value,index,
				isSelected,cellHasFocus);

			setText(shorten(value.toString()));

			return this;
		}
	} //}}}

	//{{{ ActionHandler class
	class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			Object source = evt.getSource();
			if(source == insert)
				ok();
			else if(source == cancel)
				cancel();
		}
	} //}}}

	//{{{ ListHandler class
	class ListHandler implements ListSelectionListener
	{
		//{{{ valueChanged() method
		public void valueChanged(ListSelectionEvent evt)
		{
			showClipText();
			updateButtons();
		} //}}}
	} //}}}

	//{{{ MouseHandler class
	class MouseHandler extends MouseAdapter
	{
		public void mouseClicked(MouseEvent evt)
		{
			if(evt.getClickCount() == 2)
				ok();
		}
	} //}}}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9908.java