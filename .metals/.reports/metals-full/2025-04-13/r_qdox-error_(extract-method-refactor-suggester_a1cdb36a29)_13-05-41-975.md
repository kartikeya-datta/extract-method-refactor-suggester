error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4867.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4867.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4867.java
text:
```scala
s@@etVisible(true);

/*
 * TipOfTheDay.java - Tip of the day window
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
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.Random;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.Log;
//}}}

public class TipOfTheDay extends EnhancedDialog
{
	//{{{ TipOfTheDay constructor
	public TipOfTheDay(View view)
	{
		super(view,jEdit.getProperty("tip.title"),false);

		JPanel content = new JPanel(new BorderLayout(12,12));
		content.setBorder(new EmptyBorder(12,12,12,12));
		setContentPane(content);

		JLabel label = new JLabel(jEdit.getProperty("tip.caption"));
		label.setFont(new Font("SansSerif",Font.PLAIN,24));
		label.setForeground(UIManager.getColor("Button.foreground"));
		content.add(BorderLayout.NORTH,label);

		tipText = new JEditorPane();
		tipText.setEditable(false);
		tipText.setContentType("text/html");

		nextTip();

		JScrollPane scroller = new JScrollPane(tipText);
		scroller.setPreferredSize(new Dimension(150,150));
		content.add(BorderLayout.CENTER,scroller);

		ActionHandler actionHandler = new ActionHandler();

		Box buttons = new Box(BoxLayout.X_AXIS);

		showNextTime = new JCheckBox(jEdit.getProperty("tip.show-next-time"),
			jEdit.getBooleanProperty("tip.show"));
		showNextTime.addActionListener(actionHandler);
		buttons.add(showNextTime);

		buttons.add(Box.createHorizontalStrut(6));
		buttons.add(Box.createGlue());

		nextTip = new JButton(jEdit.getProperty("tip.next-tip"));
		nextTip.addActionListener(actionHandler);
		buttons.add(nextTip);

		buttons.add(Box.createHorizontalStrut(6));

		close = new JButton(jEdit.getProperty("common.close"));
		close.addActionListener(actionHandler);
		buttons.add(close);
		content.getRootPane().setDefaultButton(close);

		Dimension dim = nextTip.getPreferredSize();
		dim.width = Math.max(dim.width,close.getPreferredSize().width);
		nextTip.setPreferredSize(dim);
		close.setPreferredSize(dim);

		content.add(BorderLayout.SOUTH,buttons);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(view);
		show();
	} //}}}

	//{{{ ok() method
	public void ok()
	{
		dispose();
	} //}}}

	//{{{ cancel() method
	public void cancel()
	{
		dispose();
	} //}}}

	//{{{ Private members

	//{{{ Instance variables
	private JCheckBox showNextTime;
	private JButton nextTip, close;
	private JEditorPane tipText;
	private int currentTip = -1;
	//}}}

	//{{{ nextTip() method
	private void nextTip()
	{
		File[] tips = new File(MiscUtilities.constructPath(
			jEdit.getJEditHome(),"doc","tips")).listFiles();
		if(tips == null || tips.length == 0)
		{
			tipText.setText(jEdit.getProperty("tip.not-found"));
			return;
		}

		int count = tips.length;

		// so that we don't see the same tip again if the user
		// clicks 'Next Tip'
		int tipToShow = currentTip;
		while(tipToShow == currentTip || !tips[tipToShow].getName().endsWith(".html"))
			tipToShow = Math.abs(new Random().nextInt()) % count;
		try
		{
			tipText.setPage(tips[tipToShow].toURL());
		}
		catch(Exception e)
		{
			Log.log(Log.ERROR,this,e);
		}
	} //}}}

	//}}}

	//{{{ ActionHandler class
	class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			Object source = evt.getSource();
			if(source == showNextTime)
			{
				jEdit.setBooleanProperty("tip.show",showNextTime
					.isSelected());
			}
			else if(source == nextTip)
				nextTip();
			else if(source == close)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4867.java