error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6696.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6696.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6696.java
text:
```scala
s@@etVisible(true);

/*
 * AboutDialog.java - About jEdit dialog box
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
import javax.swing.border.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import org.gjt.sp.jedit.*;
//}}}

public class AboutDialog extends EnhancedDialog
{
	//{{{ AboutDialog constructor
	public AboutDialog(View view)
	{
		super(view,jEdit.getProperty("about.title"),true);

		JPanel content = new JPanel(new BorderLayout());
		content.setBorder(new EmptyBorder(12,12,12,12));
		setContentPane(content);

		content.add(BorderLayout.CENTER,new AboutPanel());

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
		buttonPanel.setBorder(new EmptyBorder(12,0,0,0));

		buttonPanel.add(Box.createGlue());
		close = new JButton(jEdit.getProperty("common.close"));
		close.addActionListener(new ActionHandler());
		getRootPane().setDefaultButton(close);
		buttonPanel.add(close);
		buttonPanel.add(Box.createGlue());
		content.add(BorderLayout.SOUTH,buttonPanel);

		pack();
		setResizable(false);
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

	// private members
	private JButton close;

	//{{{ ActionHandler class
	class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			dispose();
		}
	} //}}}

	//{{{ AboutPanel class
	static class AboutPanel extends JComponent
	{
		ImageIcon image;
		Vector text;
		int scrollPosition;
		AnimationThread thread;
		int maxWidth;
		FontMetrics fm;

		public static int TOP = 120;
		public static int BOTTOM = 30;

		AboutPanel()
		{
			setFont(UIManager.getFont("Label.font"));
			fm = getFontMetrics(getFont());

			setForeground(new Color(96,96,96));
			image = new ImageIcon(getClass().getResource(
				"/org/gjt/sp/jedit/icons/about.png"));

			setBorder(new MatteBorder(1,1,1,1,Color.gray));

			text = new Vector(50);
			StringTokenizer st = new StringTokenizer(
				jEdit.getProperty("about.text"),"\n");
			while(st.hasMoreTokens())
			{
				String line = st.nextToken();
				text.addElement(line);
				maxWidth = Math.max(maxWidth,
					fm.stringWidth(line) + 10);
			}

			scrollPosition = -250;

			thread = new AnimationThread();
		}

		public void paintComponent(Graphics g)
		{
			g.setColor(new Color(96,96,96));
			image.paintIcon(this,g,1,1);

			FontMetrics fm = g.getFontMetrics();

			String[] args = { jEdit.getVersion() };
			String version = jEdit.getProperty("about.version",args);
			g.drawString(version,(getWidth() - fm.stringWidth(version)) / 2,
				getHeight() - 5);

			g = g.create((getWidth() - maxWidth) / 2,TOP,maxWidth,
				getHeight() - TOP - BOTTOM);

			int height = fm.getHeight();
			int firstLine = scrollPosition / height;

			int firstLineOffset = height - scrollPosition % height;
			int lines = (getHeight() - TOP - BOTTOM) / height;

			int y = firstLineOffset;

			for(int i = 0; i <= lines; i++)
			{
				if(i + firstLine >= 0 && i + firstLine < text.size())
				{
					String line = (String)text.get(i + firstLine);
					g.drawString(line,(maxWidth - fm.stringWidth(line))/2,y);
				}
				y += fm.getHeight();
			}
		}

		public Dimension getPreferredSize()
		{
			return new Dimension(1 + image.getIconWidth(),
				1 + image.getIconHeight());
		}

		public void addNotify()
		{
			super.addNotify();
			thread.start();
		}

		public void removeNotify()
		{
			super.removeNotify();
			thread.kill();
		}

		class AnimationThread extends Thread
		{
			private boolean running = true;
			private long last;

			AnimationThread()
			{
				super("About box animation thread");
				setPriority(Thread.MIN_PRIORITY);
			}
			
			public void kill()
			{
				running = false;
			}

			public void run()
			{
				FontMetrics fm = getFontMetrics(getFont());
				int max = (text.size() * fm.getHeight());

				while (running)
				{
					scrollPosition += 2;

					if(scrollPosition > max)
						scrollPosition = -250;

					if(last != 0)
					{
						long frameDelay =
							System.currentTimeMillis()
							- last;

						try
						{
							Thread.sleep(
								75
								- frameDelay);
						}
						catch(Exception e)
						{
						}
					}

					last = System.currentTimeMillis();

					repaint(getWidth() / 2 - maxWidth,
						TOP,maxWidth * 2,
						getHeight() - TOP - BOTTOM);
				}
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6696.java