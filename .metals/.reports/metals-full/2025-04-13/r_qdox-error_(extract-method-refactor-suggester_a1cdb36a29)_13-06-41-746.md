error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4319.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4319.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4319.java
text:
```scala
p@@ublic class PluginManagerOptionPane extends AbstractOptionPane

/*
 * PluginManagerOptionPane.java - Plugin options panel
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2003 Kris Kopicki
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

package org.gjt.sp.jedit.options;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.gui.*;
import org.gjt.sp.jedit.io.VFSManager;
import org.gjt.sp.jedit.pluginmgr.*;
import org.gjt.sp.util.*;

class PluginManagerOptionPane extends AbstractOptionPane
{
	//{{{ Constructor
	public PluginManagerOptionPane()
	{
		super("plugin-manager");
	} //}}}

	//{{{ Private members

	//{{{ Instance variables
	private JLabel locationLabel;
	private JLabel mirrorLabel;

	private ButtonGroup locGrp;
	private JRadioButton settingsDir;
	private JRadioButton appDir;
	private JCheckBox downloadSource;

	private MirrorModel miraModel;
	private JList miraList;
	//}}}

	//{{{ _init() method
	protected void _init()
	{
		setLayout(new BorderLayout());

		locationLabel = new JLabel(jEdit.getProperty(
			"options.plugin-manager.location"));
		mirrorLabel = new JLabel(jEdit.getProperty(
			"options.plugin-manager.mirror"));
		if(jEdit.getSettingsDirectory() != null)
		{
			settingsDir = new JRadioButton(jEdit.getProperty(
				"options.plugin-manager.settings-dir"));
			settingsDir.setToolTipText(MiscUtilities.constructPath(
				jEdit.getSettingsDirectory(),"jars"));
		}
		appDir = new JRadioButton(jEdit.getProperty(
			"options.plugin-manager.app-dir"));
		appDir.setToolTipText(MiscUtilities.constructPath(
			jEdit.getJEditHome(),"jars"));

		// Start downloading the list nice and early
		miraList = new JList(miraModel = new MirrorModel());
		miraList.setSelectionModel(new SingleSelectionModel());
		miraList.setSelectedIndex(0);

		/* Download mirror */
		add(BorderLayout.NORTH,mirrorLabel);
		add(BorderLayout.CENTER,new JScrollPane(miraList));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));

		buttonPanel.add(Box.createVerticalStrut(6));

		/* Update mirror list */
		JButton updateMirrors = new JButton(jEdit.getProperty(
			"options.plugin-manager.updateMirrors"));
		updateMirrors.addActionListener(new ActionHandler());
		buttonPanel.add(updateMirrors);

		buttonPanel.add(Box.createVerticalStrut(6));

		/* Download source */
		downloadSource = new JCheckBox(jEdit.getProperty(
			"options.plugin-manager.downloadSource"));
		downloadSource.setSelected(jEdit.getBooleanProperty("plugin-manager.downloadSource"));
		buttonPanel.add(downloadSource);

		buttonPanel.add(Box.createVerticalStrut(6));

		/* Install location */
		locGrp = new ButtonGroup();
		if(jEdit.getSettingsDirectory() != null)
			locGrp.add(settingsDir);
		locGrp.add(appDir);
		JPanel locPanel = new JPanel();
		locPanel.setBorder(new EmptyBorder(3,12,0,0));
		locPanel.setLayout(new BoxLayout(locPanel,BoxLayout.Y_AXIS));
		if(jEdit.getSettingsDirectory() != null)
		{
			locPanel.add(settingsDir);
			locPanel.add(Box.createVerticalStrut(3));
		}
		locPanel.add(appDir);
		buttonPanel.add(locationLabel);
		buttonPanel.add(locPanel);

		buttonPanel.add(Box.createGlue());
		add(BorderLayout.SOUTH,buttonPanel);

		if (jEdit.getBooleanProperty("plugin-manager.installUser")
			&& jEdit.getSettingsDirectory() != null)
			settingsDir.setSelected(true);
		else
			appDir.setSelected(true);
	} //}}}

	//{{{ _save() method
	protected void _save()
	{
		jEdit.setBooleanProperty("plugin-manager.installUser",settingsDir.isSelected());
		jEdit.setBooleanProperty("plugin-manager.downloadSource",downloadSource.isSelected());

		if(miraList.getSelectedIndex() != -1)
		{
			String currentMirror = miraModel.getID(miraList.getSelectedIndex());
			String previousMirror = jEdit.getProperty("plugin-manager.mirror.id");

			if (!previousMirror.equals(currentMirror))
			{
				jEdit.setProperty("plugin-manager.mirror.id",currentMirror);
				// Insert code to update the plugin managers list here later
			}
		}
	} //}}}

	//}}}

	//{{{ MirrorModel class
	class MirrorModel extends AbstractListModel
	{
		private ArrayList mirrors;

		public MirrorModel()
		{
			mirrors = new ArrayList();
		}

		public String getID(int index)
		{
			return ((MirrorList.Mirror)mirrors.get(index)).id;
		}

		public int getSize()
		{
			return mirrors.size();
		}

		public Object getElementAt(int index)
		{
			MirrorList.Mirror mirror = (MirrorList.Mirror)mirrors.get(index);
			if(mirror.id.equals(MirrorList.Mirror.NONE))
				return jEdit.getProperty("options.plugin-manager.none");
			else
				return mirror.continent+": "+mirror.description+" ("+mirror.location+")";
		}

		public void setList(ArrayList mirrors)
		{
			this.mirrors = mirrors;
			fireContentsChanged(this,0,mirrors.size() - 1);
		}
	} //}}}

	//{{{ SingleSelectionModel class
	class SingleSelectionModel extends DefaultListSelectionModel
	{
		public SingleSelectionModel()
		{
			super();
			setSelectionMode(SINGLE_SELECTION);
		}

		public void removeSelectionInterval(int index0, int index1) {}
	} //}}}

	//{{{ ActionHandler class
	class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			VFSManager.runInWorkThread(new DownloadMirrorsThread());
		}
	} //}}}

	//{{{ DownloadMirrorsThread class
	class DownloadMirrorsThread extends WorkRequest
	{
		public void run()
		{
			setStatus(jEdit.getProperty("options.plugin-manager.workthread"));
			setProgressMaximum(1);
			setProgressValue(0);

			final ArrayList mirrors = new ArrayList();
			try
			{
				mirrors.addAll(new MirrorList().mirrors);
			}
			catch (Exception ex)
			{
				Log.log(Log.ERROR,this,ex);
				GUIUtilities.error(PluginManagerOptionPane.this,
					"ioerror",new String[] { ex.toString() });
			}

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					System.err.println(mirrors);
					miraModel.setList(mirrors);

					String id = jEdit.getProperty("plugin-manager.mirror.id");
					int size = miraModel.getSize();
					for (int i=0; i < size; i++)
					{
						if (size == 1 || miraModel.getID(i).equals(id))
						{
							miraList.setSelectedIndex(i);
							break;
						}
					}
				}
			});

			setProgressValue(1);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4319.java