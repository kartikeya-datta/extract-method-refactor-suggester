error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6103.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6103.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6103.java
text:
```scala
public v@@oid mouseReleased(MouseEvent evt)

/*
 * EnhancedCheckBoxMenuItem.java - Check box menu item
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 1999, 2000, 2001, 2002 Slava Pestov
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
import java.awt.event.*;
import java.awt.*;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.Log;
//}}}

/**
 * jEdit's custom menu item. It adds support for multi-key shortcuts.
 */
public class EnhancedCheckBoxMenuItem extends JCheckBoxMenuItem
{
	//{{{ EnhancedCheckBoxMenuItem constructor
	public EnhancedCheckBoxMenuItem(String label, EditAction action)
	{
		super(label);
		this.action = action;

		if(action != null)
		{
			setEnabled(true);
			addActionListener(new EditAction.Wrapper(action));
			shortcutProp1 = action.getName() + ".shortcut";
			shortcutProp2 = action.getName() + ".shortcut2";

			addMouseListener(new MouseHandler());
		}
		else
			setEnabled(false);

		setModel(new Model());
	} //}}}

	//{{{ getPreferredSize() method
	public Dimension getPreferredSize()
	{
		Dimension d = super.getPreferredSize();

		String shortcut = getShortcut();

		if(shortcut != null)
		{
			d.width += (getFontMetrics(acceleratorFont)
				.stringWidth(shortcut) + 15);
		}
		return d;
	} //}}}

	//{{{ paint() method
	public void paint(Graphics g)
	{
		super.paint(g);

		String shortcut = getShortcut();

		if(shortcut != null)
		{
			g.setFont(acceleratorFont);
			g.setColor(getModel().isArmed() ?
				acceleratorSelectionForeground :
				acceleratorForeground);
			FontMetrics fm = g.getFontMetrics();
			Insets insets = getInsets();
			g.drawString(shortcut,getWidth() - (fm.stringWidth(
				shortcut) + insets.right + insets.left + 5),
				getFont().getSize() + (insets.top - 1)
				/* XXX magic number */);
		}
	} //}}}

	//{{{ getActionCommand() method
	public String getActionCommand()
	{
		return getModel().getActionCommand();
	} //}}}

	//{{{ Private members

	//{{{ Instance variables
	private String shortcutProp1;
	private String shortcutProp2;
	private EditAction action;
	private static Font acceleratorFont;
	private static Color acceleratorForeground;
	private static Color acceleratorSelectionForeground;
	//}}}

	//{{{ getShortcut() method
	private String getShortcut()
	{
		if(action == null)
			return null;
		else
		{
			String shortcut1 = jEdit.getProperty(shortcutProp1);
			String shortcut2 = jEdit.getProperty(shortcutProp2);

			if(shortcut1 == null || shortcut1.length() == 0)
			{
				if(shortcut2 == null || shortcut2.length() == 0)
					return null;
				else
					return shortcut2;
			}
			else
			{
				if(shortcut2 == null || shortcut2.length() == 0)
					return shortcut1;
				else
					return shortcut1 + " or " + shortcut2;
			}
		}
	} //}}}

	//{{{ Class initializer
	static
	{
		acceleratorFont = UIManager.getFont("MenuItem.acceleratorFont");
		acceleratorFont = new Font("Monospaced",
			acceleratorFont.getStyle(),
			acceleratorFont.getSize());
		acceleratorForeground = UIManager
			.getColor("MenuItem.acceleratorForeground");
		acceleratorSelectionForeground = UIManager
			.getColor("MenuItem.acceleratorSelectionForeground");
	} //}}}

	//}}}

	//{{{ Model class
	class Model extends DefaultButtonModel
	{
		public boolean isSelected()
		{
			if(!isShowing())
				return false;

			try
			{
				return action.isSelected(GUIUtilities.getView(
					EnhancedCheckBoxMenuItem.this));
			}
			catch(Throwable t)
			{
				Log.log(Log.ERROR,this,t);
				return false;
			}
		}

		public void setSelected(boolean b) {}
	} //}}}

	//{{{ MouseHandler class
	class MouseHandler extends MouseAdapter
	{
		public void mouseClicked(MouseEvent evt)
		{
			GUIUtilities.getView((Component)evt.getSource())
				.getStatus().setMessage(null);
		}

		public void mouseEntered(MouseEvent evt)
		{
			String msg = action.getMouseOverText();
			if(msg != null)
			{
				GUIUtilities.getView((Component)evt.getSource())
					.getStatus().setMessage(msg);
			}
		}

		public void mouseExited(MouseEvent evt)
		{
			GUIUtilities.getView((Component)evt.getSource())
				.getStatus().setMessage(null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6103.java