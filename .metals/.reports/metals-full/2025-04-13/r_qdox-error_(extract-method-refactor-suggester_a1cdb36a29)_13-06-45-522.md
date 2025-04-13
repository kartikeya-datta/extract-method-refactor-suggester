error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8681.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8681.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8681.java
text:
```scala
public v@@oid windowOpened(WindowEvent evt)

/*
 * PluginListDownloadProgress.java - Plugin list download progress dialog
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

package org.gjt.sp.jedit.pluginmgr;

import com.microstar.xml.XmlException;
import javax.swing.border.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.InterruptedIOException;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.Log;

class PluginListDownloadProgress extends JDialog
{
	PluginListDownloadProgress(PluginManager window)
	{
		super(JOptionPane.getFrameForComponent(window),
			jEdit.getProperty("plugin-list.progress.title"),true);

		this.window = window;

		JPanel content = new JPanel(new BorderLayout());
		content.setBorder(new EmptyBorder(12,12,12,12));
		setContentPane(content);

		JLabel caption = new JLabel(jEdit.getProperty("plugin-list.progress.caption"));
		caption.setBorder(new EmptyBorder(0,0,12,0));
		content.add(BorderLayout.NORTH,caption);

		Box box = new Box(BoxLayout.X_AXIS);
		box.add(Box.createGlue());
		JButton stop = new JButton(jEdit.getProperty("plugin-list.progress.stop"));
		stop.addActionListener(new ActionHandler());
		stop.setMaximumSize(stop.getPreferredSize());
		box.add(stop);
		box.add(Box.createGlue());
		content.add(BorderLayout.CENTER,box);

		addWindowListener(new WindowHandler());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setLocationRelativeTo(window);
		setResizable(false);
		show();
	}

	PluginList getPluginList()
	{
		return list;
	}

	// private members
	private PluginManager window;
	private PluginList list;
	private DownloadThread thread;

	class DownloadThread extends Thread
	{
		public void run()
		{
			try
			{
				list = new PluginList();
				dispose();
			}
			catch(XmlException xe)
			{
				dispose();

				int line = xe.getLine();
				String path = jEdit.getProperty("plugin-manager.url");
				String message = xe.getMessage();
				Log.log(Log.ERROR,this,path + ":" + line
					+ ": " + message);
				String[] pp = { path, String.valueOf(line), message };
				GUIUtilities.error(window,"plugin-list.xmlerror",pp);
			}
			catch(Exception e)
			{
				dispose();

				Log.log(Log.ERROR,this,e);
				String[] pp = { e.toString() };
				GUIUtilities.error(window,"plugin-list.ioerror",pp);
			}
		}
	}

	class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			thread.stop();
			dispose();
		}
	}

	class WindowHandler extends WindowAdapter
	{
		boolean done;

		public void windowActivated(WindowEvent evt)
		{
			if(done)
				return;

			done = true;
			thread = new DownloadThread();
			thread.start();
		}

		public void windowClosing(WindowEvent evt)
		{
			thread.stop();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8681.java