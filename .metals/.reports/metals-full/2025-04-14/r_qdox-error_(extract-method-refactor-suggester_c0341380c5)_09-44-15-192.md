error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5892.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5892.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5892.java
text:
```scala
s@@etVisible(true);

/*
 * EditAbbrevDialog.java - Displayed when editing abbrevs
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2001 Slava Pestov
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
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import org.gjt.sp.jedit.*;
//}}}

public class EditAbbrevDialog extends JDialog
{
	//{{{ EditAbbrevDialog constructor
	/**
	 * @since jEdit 4.2pre3
	 */
	public EditAbbrevDialog(Frame frame, String abbrev, String expansion,
		Map abbrevs)
	{
		super(frame,jEdit.getProperty("edit-abbrev.title"),true);
		init(abbrev, expansion, abbrevs);
	} //}}}

	//{{{ EditAbbrevDialog constructor
	public EditAbbrevDialog(Dialog dialog, String abbrev, String expansion,
		Map abbrevs)
	{
		super(dialog,jEdit.getProperty("edit-abbrev.title"),true);
		init(abbrev, expansion, abbrevs);
	} //}}}

	//{{{ getAbbrev() method
	public String getAbbrev()
	{
		if(!isOK)
			return null;

		return editor.getAbbrev();
	} //}}}

	//{{{ getExpansion() method
	public String getExpansion()
	{
		if(!isOK)
			return null;

		return editor.getExpansion();
	} //}}}

	//{{{ Private members
	private AbbrevEditor editor;
	private JButton ok;
	private JButton cancel;
	private boolean isOK;
	private String originalAbbrev;
	private Map abbrevs;

	//{{{ init() method
	private void init(String abbrev, String expansion, Map abbrevs)
	{
		this.abbrevs = abbrevs;

		this.originalAbbrev = abbrev;

		JPanel content = new JPanel(new BorderLayout());
		content.setBorder(new EmptyBorder(12,12,12,12));
		setContentPane(content);

		editor = new AbbrevEditor();
		editor.setAbbrev(abbrev);
		editor.setExpansion(expansion);
		editor.setBorder(new EmptyBorder(0,0,12,0));
		content.add(BorderLayout.CENTER,editor);

		Box box = new Box(BoxLayout.X_AXIS);
		box.add(Box.createGlue());
		ok = new JButton(jEdit.getProperty("common.ok"));
		ok.addActionListener(new ActionHandler());
		getRootPane().setDefaultButton(ok);
		box.add(ok);
		box.add(Box.createHorizontalStrut(6));
		cancel = new JButton(jEdit.getProperty("common.cancel"));
		cancel.addActionListener(new ActionHandler());
		box.add(cancel);
		box.add(Box.createGlue());
		content.add(BorderLayout.SOUTH,box);

		KeyListener listener = new KeyHandler();
		addKeyListener(listener);
		editor.getBeforeCaretTextArea().addKeyListener(listener);
		editor.getAfterCaretTextArea().addKeyListener(listener);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(getParent());
		show();
	} //}}}

	//{{{ checkForExistingAbbrev() method
	private boolean checkForExistingAbbrev()
	{
		String abbrev = editor.getAbbrev();
		if(abbrevs.get(abbrev) != null)
		{
			if(abbrev.equals(originalAbbrev))
				return true;

			int result = GUIUtilities.confirm(this,
				"edit-abbrev.duplicate",null,
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
			return (result == JOptionPane.YES_OPTION);
		}

		return true;
	} //}}}

	//}}}

	//{{{ ActionHandler class
	class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			if(evt.getSource() == ok)
			{
				if(editor.getAbbrev() == null
 editor.getAbbrev().length() == 0)
				{
					getToolkit().beep();
					return;
				}

				if(!checkForExistingAbbrev())
					return;

				isOK = true;
			}

			dispose();
		}
	} //}}}

	//{{{ KeyHandler class
	class KeyHandler extends KeyAdapter
	{
		public void keyPressed(KeyEvent evt)
		{
			if(evt.getKeyCode() == KeyEvent.VK_ESCAPE)
				dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5892.java