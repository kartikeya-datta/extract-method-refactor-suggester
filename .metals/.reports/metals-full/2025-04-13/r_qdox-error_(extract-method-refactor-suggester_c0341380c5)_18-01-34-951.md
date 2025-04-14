error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4861.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4861.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4861.java
text:
```scala
c@@lass PluginManagerProgress extends JDialog

/*
 * PluginManagerProgress.java - Plugin download progress meter
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2000, 2001 Slava Pestov
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
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import org.gjt.sp.jedit.*;
//}}}

public class PluginManagerProgress extends JDialog
{
	//{{{ PluginManagerProgress constructor
	public PluginManagerProgress(JDialog dialog, String type, Roster roster)
	{
		super(dialog,
			jEdit.getProperty("plugin-manager.progress."
			+ type + "-task"),true);

		this.dialog = dialog;
		this.roster = roster;
		this.type = type;

		JPanel content = new JPanel(new BorderLayout(12,12));
		content.setBorder(new EmptyBorder(12,12,12,12));
		setContentPane(content);

		progress = new JProgressBar();
		progress.setStringPainted(true);
		progress.setString(jEdit.getProperty("plugin-manager.progress."
			+ type + "-task"));

		int maximum = 0;
		count = roster.getOperationCount();
		for(int i = 0; i < count; i++)
		{
			maximum += roster.getOperation(i).getMaximum();
		}

		progress.setMaximum(maximum);
		content.add(BorderLayout.CENTER,progress);

		stop = new JButton(jEdit.getProperty("plugin-manager.progress.stop"));
		stop.addActionListener(new ActionHandler());
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createGlue());
		panel.add(stop);
		panel.add(Box.createGlue());
		content.add(BorderLayout.SOUTH,panel);

		addWindowListener(new WindowHandler());

		pack();

		Dimension size = getSize();
		size.width = Math.max(size.width,500);
		setSize(size);
		setLocationRelativeTo(dialog);

		show();
	} //}}}

	//{{{ removing() method
	public void removing(String plugin)
	{
		String[] args = { plugin };
		showMessage(jEdit.getProperty("plugin-manager.progress.removing",args));
		stop.setEnabled(true);
	} //}}}

	//{{{ downloading() method
	public void downloading(String plugin)
	{
		String[] args = { plugin };
		showMessage(jEdit.getProperty("plugin-manager.progress.downloading",args));
		stop.setEnabled(true);
	} //}}}

	//{{{ installing() method
	public void installing(String plugin)
	{
		String[] args = { plugin };
		showMessage(jEdit.getProperty("plugin-manager.progress.installing",args));
		stop.setEnabled(false);
	} //}}}

	//{{{ setValue() method
	public void setValue(final int value)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				progress.setValue(valueSoFar + value);
			}
		});
	} //}}}

	//{{{ done() method
	public void done(final boolean ok)
	{
		this.ok |= ok;

		try
		{
			if(!ok || done == count)
			{
				SwingUtilities.invokeAndWait(new Runnable()
				{
					public void run()
					{
						dispose();
						if(ok)
						{
							GUIUtilities.message(dialog,
								"plugin-manager." + type
								+ "-done",null);
						}
						else
						{
							// user will see an error in any case

							//GUIUtilities.message(PluginManagerProgress.this,
							//	"plugin-manager.failed",null);
						}
					}
				});
			}
			else
			{
				SwingUtilities.invokeAndWait(new Runnable()
				{
					public void run()
					{
						valueSoFar += roster.getOperation(done - 1)
							.getMaximum();
						progress.setValue(valueSoFar);
						done++;
					}
				});
			}
		}
		catch(Exception e)
		{
		}
	} //}}}

	//{{{ isOK() method
	public boolean isOK()
	{
		return ok;
	} //}}}

	//{{{ Private members

	//{{{ Instance variables
	private JDialog dialog;

	private Thread thread;

	private String type;

	private JProgressBar progress;
	private JButton stop;
	private int count;
	private int done = 1;

	// progress value as of start of current task
	private int valueSoFar;

	private boolean ok;

	private Roster roster;
	//}}}

	//{{{ showMessage() method
	private void showMessage(final String msg)
	{
		/* try
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{
				public void run()
				{
					localProgress.setString(msg);
				}
			});
		}
		catch(Exception e)
		{
		}

		Thread.yield(); */
	} //}}}

	//{{{ ActionHandler class
	class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			if(evt.getSource() == stop)
			{
				thread.stop();
				dispose();
			}
		}
	} //}}}

	//{{{ WindowHandler class
	class WindowHandler extends WindowAdapter
	{
		boolean done;

		public void windowOpened(WindowEvent evt)
		{
			if(done)
				return;

			done = true;
			thread = new RosterThread();
			thread.start();
		}

		public void windowClosing(WindowEvent evt)
		{
			thread.stop();
			dispose();
		}
	} //}}}

	//{{{ RosterThread class
	class RosterThread extends Thread
	{
		RosterThread()
		{
			super("Plugin manager thread");
		}

		public void run()
		{
			roster.performOperations(PluginManagerProgress.this);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4861.java