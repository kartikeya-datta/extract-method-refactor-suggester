error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7955.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7955.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7955.java
text:
```scala
i@@f(current.getItemCount() >= 20 && i != list.length - 1)

/*
 * CurrentDirectoryMenu.java - File list menu
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

package org.gjt.sp.jedit.gui;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import org.gjt.sp.jedit.browser.*;
import org.gjt.sp.jedit.io.VFSManager;
import org.gjt.sp.jedit.*;

public class CurrentDirectoryMenu extends EnhancedMenu
{
	//{{{ CurrentDirectoryMenu constructor
	public CurrentDirectoryMenu()
	{
		super("current-directory");
	} //}}}

	//{{{ setPopupMenuVisible() method
	public void setPopupMenuVisible(boolean b)
	{
		if(b)
		{
			final View view = GUIUtilities.getView(this);

			if(getMenuComponentCount() != 0)
				removeAll();

			final String path = MiscUtilities.getParentOfPath(
				view.getBuffer().getPath());
			JMenuItem mi = new JMenuItem(path);
			mi.setIcon(FileCellRenderer.openDirIcon);

			//{{{ Directory action listener...
			mi.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					VFSBrowser.browseDirectory(view,path);
				}
			}); //}}}

			add(mi);
			addSeparator();

			if(view.getBuffer().getFile() == null)
			{
				mi = new JMenuItem(jEdit.getProperty(
					"current-directory.not-local"));
				mi.setEnabled(false);
				add(mi);
				super.setPopupMenuVisible(b);
				return;
			}

			File dir = new File(path);

			JMenu current = this;

			//{{{ ActionListener class
			ActionListener listener = new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jEdit.openFile(view,evt.getActionCommand());
				}
			}; //}}}

			// for filtering out backups
			String backupPrefix = jEdit.getProperty("backup.prefix");
			String backupSuffix = jEdit.getProperty("backup.suffix");

			String[] list = dir.list();
			if(list == null || list.length == 0)
			{
				mi = new JMenuItem(jEdit.getProperty(
					"current-directory.no-files"));
				mi.setEnabled(false);
				add(mi);
			}
			else
			{
				MiscUtilities.quicksort(list,
					new MiscUtilities.StringICaseCompare());
				for(int i = 0; i < list.length; i++)
				{
					String name = list[i];

					// skip marker files
					if(name.endsWith(".marks"))
						continue;

					// skip autosave files
					if(name.startsWith("#") && name.endsWith("#"))
						continue;

					// skip backup files
					if((backupPrefix.length() != 0
						&& name.startsWith(backupPrefix))
 (backupSuffix.length() != 0
						&& name.endsWith(backupSuffix)))
						continue;

					// skip directories
					File file = new File(path,name);
					if(file.isDirectory())
						continue;

					mi = new JMenuItem(name);
					mi.setActionCommand(file.getPath());
					mi.addActionListener(listener);
					mi.setIcon(FileCellRenderer.fileIcon);

					if(current.getItemCount() >= 20)
					{
						//current.addSeparator();
						JMenu newCurrent = new JMenu(
							jEdit.getProperty(
							"common.more"));
						current.add(newCurrent);
						current = newCurrent;
					}
					current.add(mi);
				}
			}

		}

		super.setPopupMenuVisible(b);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7955.java