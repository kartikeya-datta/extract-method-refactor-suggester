error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8098.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8098.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8098.java
text:
```scala
v@@iew.goToBuffer(jEdit.getBuffer((String)

/*
 * CloseDialog.java - Close all buffers dialog
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 1999, 2000 Slava Pestov
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
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import org.gjt.sp.jedit.buffer.BufferIORequest;
import org.gjt.sp.jedit.io.*;
import org.gjt.sp.jedit.*;
//}}}

public class CloseDialog extends EnhancedDialog
{
	//{{{ CloseDialog constructor
	public CloseDialog(View view)
	{
		super(view,jEdit.getProperty("close.title"),true);

		this.view = view;

		JPanel content = new JPanel(new BorderLayout(12,12));
		content.setBorder(new EmptyBorder(12,12,12,12));
		setContentPane(content);

		Box iconBox = new Box(BoxLayout.Y_AXIS);
		iconBox.add(new JLabel(UIManager.getIcon("OptionPane.warningIcon")));
		iconBox.add(Box.createGlue());
		content.add(BorderLayout.WEST,iconBox);

		JPanel centerPanel = new JPanel(new BorderLayout());

		JLabel label = new JLabel(jEdit.getProperty("close.caption"));
		label.setBorder(new EmptyBorder(0,0,6,0));
		centerPanel.add(BorderLayout.NORTH,label);

		bufferList = new JList(bufferModel = new DefaultListModel());
		bufferList.setVisibleRowCount(10);
		bufferList.addListSelectionListener(new ListHandler());

		Buffer[] buffers = jEdit.getBuffers();
		for(int i = 0; i < buffers.length; i++)
		{
			Buffer buffer = buffers[i];
			if(buffer.isDirty())
			{
				bufferModel.addElement(buffer.getPath());
			}
		}

		centerPanel.add(BorderLayout.CENTER,new JScrollPane(bufferList));

		content.add(BorderLayout.CENTER,centerPanel);

		ActionHandler actionListener = new ActionHandler();

		Box buttons = new Box(BoxLayout.X_AXIS);
		buttons.add(Box.createGlue());
		buttons.add(selectAll = new JButton(jEdit.getProperty("close.selectAll")));
		selectAll.setMnemonic(jEdit.getProperty("close.selectAll.mnemonic").charAt(0));
		selectAll.addActionListener(actionListener);
		buttons.add(Box.createHorizontalStrut(6));
		buttons.add(save = new JButton(jEdit.getProperty("close.save")));
		save.setMnemonic(jEdit.getProperty("close.save.mnemonic").charAt(0));
		save.addActionListener(actionListener);
		buttons.add(Box.createHorizontalStrut(6));
		buttons.add(discard = new JButton(jEdit.getProperty("close.discard")));
		discard.setMnemonic(jEdit.getProperty("close.discard.mnemonic").charAt(0));
		discard.addActionListener(actionListener);
		buttons.add(Box.createHorizontalStrut(6));
		buttons.add(cancel = new JButton(jEdit.getProperty("common.cancel")));
		cancel.addActionListener(actionListener);
		buttons.add(Box.createGlue());

		bufferList.setSelectedIndex(0);

		content.add(BorderLayout.SOUTH,buttons);

		GUIUtilities.requestFocus(this,bufferList);

		pack();
		setLocationRelativeTo(view);
		show();
	} //}}}

	//{{{ isOK() method
	public boolean isOK()
	{
		return ok;
	} //}}}

	//{{{ ok() method
	public void ok()
	{
		// do nothing
	} //}}}

	//{{{ cancel() method
	public void cancel()
	{
		dispose();
	} //}}}

	//{{{ Private members
	private View view;
	private JList bufferList;
	private DefaultListModel bufferModel;
	private JButton selectAll;
	private JButton save;
	private JButton discard;
	private JButton cancel;

	private boolean ok; // only set if all buffers saved/closed

	boolean selectAllFlag;

	private void updateButtons()
	{
		int index = bufferList.getSelectedIndex();
		save.getModel().setEnabled(index != -1);
		discard.getModel().setEnabled(index != -1);
	} //}}}

	//{{{ ActionHandler class
	class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			Object source = evt.getSource();
			if(source == selectAll)
			{
				// I'm too tired to think of a better way
				// to handle this right now.
				try
				{
					selectAllFlag = true;

					bufferList.setSelectionInterval(0,
						bufferModel.getSize() - 1);
				}
				finally
				{
					selectAllFlag = false;
				}
				bufferList.requestFocus();
			}
			else if(source == save)
			{
				Object[] paths = bufferList.getSelectedValues();

				for(int i = 0; i < paths.length; i++)
				{
					String path = (String)paths[i];
					Buffer buffer = jEdit.getBuffer(path);
					if(!buffer.save(view,null,true))
						return;
					VFSManager.waitForRequests();
					if(buffer.getBooleanProperty(BufferIORequest
						.ERROR_OCCURRED))
						return;
					jEdit._closeBuffer(view,buffer);
					bufferModel.removeElement(path);
				}

				if(bufferModel.getSize() == 0)
				{
					ok = true;
					dispose();
				}
				else
				{
					bufferList.setSelectedIndex(0);
					bufferList.requestFocus();
				}
			}
			else if(source == discard)
			{
				Object[] paths = bufferList.getSelectedValues();

				for(int i = 0; i < paths.length; i++)
				{
					String path = (String)paths[i];
					Buffer buffer = jEdit.getBuffer(path);
					jEdit._closeBuffer(view,buffer);
					bufferModel.removeElement(path);
				}

				if(bufferModel.getSize() == 0)
				{
					ok = true;
					dispose();
				}
				else
				{
					bufferList.setSelectedIndex(0);
					bufferList.requestFocus();
				}
			}
			else if(source == cancel)
				cancel();
		}
	} //}}}

	//{{{ ListHandler class
	class ListHandler implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent evt)
		{
			if(selectAllFlag)
				return;

			int index = bufferList.getSelectedIndex();
			if(index != -1)
				view.setBuffer(jEdit.getBuffer((String)
					bufferModel.getElementAt(index)));

			updateButtons();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8098.java