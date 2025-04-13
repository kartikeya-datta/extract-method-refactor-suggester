error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1396.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1396.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1396.java
text:
```scala
s@@etVisible(true);

/*
 * PluginManager.java - Plugin manager window
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2002 Kris Kopicki
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
import com.microstar.xml.XmlException;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import org.gjt.sp.jedit.gui.*;
import org.gjt.sp.jedit.io.VFSManager;
import org.gjt.sp.jedit.msg.*;
import org.gjt.sp.jedit.options.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.Log;
import org.gjt.sp.util.WorkRequest;
//}}}

public class PluginManager extends JFrame implements EBComponent
{
	//{{{ getInstance() method
	/**
	 * Returns the currently visible plugin manager window, or null.
	 * @since jEdit 4.2pre2
	 */
	public static PluginManager getInstance()
	{
		return instance;
	} //}}}

	//{{{ dispose() method
	public void dispose()
	{
		GUIUtilities.saveGeometry(this,"plugin-manager");
		instance = null;
		EditBus.removeFromBus(this);
		super.dispose();
	} //}}}

	//{{{ handleMessage() method
	public void handleMessage(EBMessage message)
	{
		// Force the install tab to refresh for possible
		// change of mirror
		if (message instanceof PropertiesChanged)
		{
			pluginList = null;
			updatePluginList();
			if(tabPane.getSelectedIndex() != 0)
			{
				installer.updateModel();
				updater.updateModel();
			}
		}
		else if (message instanceof PluginUpdate)
		{
			if(!queuedUpdate)
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						queuedUpdate = false;
						manager.update();
					}
				});
				queuedUpdate = true;
			}
		}
	} //}}}

	//{{{ showPluginManager() method
	public static void showPluginManager(Frame frame)
	{
		if (instance == null)
			instance = new PluginManager(frame);
		else
		{
			instance.toFront();
			return;
		}
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

	//{{{ getPluginList() method
	public PluginList getPluginList()
	{
		return pluginList;
	} //}}}

	//{{{ Private members
	private static PluginManager instance;

	//{{{ Instance variables
	private JTabbedPane tabPane;
	private JButton done;
	private JButton mgrOptions;
	private JButton pluginOptions;
	private InstallPanel installer;
	private InstallPanel updater;
	private ManagePanel manager;
	private PluginList pluginList;
	private boolean queuedUpdate;
	private boolean downloadingPluginList;
	//}}}

	//{{{ PluginManager constructor
	private PluginManager(Frame frame)
	{
		super(jEdit.getProperty("plugin-manager.title"));

		EditBus.addToBus(this);

		/* Setup panes */
		JPanel content = new JPanel(new BorderLayout(12,12));
		content.setBorder(new EmptyBorder(12,12,12,12));
		setContentPane(content);

		tabPane = new JTabbedPane();
		tabPane.addTab(jEdit.getProperty("manage-plugins.title"),
			manager = new ManagePanel(this));
		tabPane.addTab(jEdit.getProperty("update-plugins.title"),
			updater = new InstallPanel(this,true));
		tabPane.addTab(jEdit.getProperty("install-plugins.title"),
			installer = new InstallPanel(this,false));

		content.add(BorderLayout.CENTER,tabPane);

		tabPane.addChangeListener(new ListUpdater());

		/* Create the buttons */
		Box buttons = new Box(BoxLayout.X_AXIS);

		ActionListener al = new ActionHandler();
		mgrOptions = new JButton(jEdit.getProperty("plugin-manager.mgr-options"));
		mgrOptions.addActionListener(al);
		pluginOptions = new JButton(jEdit.getProperty("plugin-manager.plugin-options"));
		pluginOptions.addActionListener(al);
		done = new JButton(jEdit.getProperty("plugin-manager.done"));
		done.addActionListener(al);

		buttons.add(Box.createGlue());
		buttons.add(mgrOptions);
		buttons.add(Box.createHorizontalStrut(6));
		buttons.add(pluginOptions);
		buttons.add(Box.createHorizontalStrut(6));
		buttons.add(done);
		buttons.add(Box.createGlue());

		getRootPane().setDefaultButton(done);

		content.add(BorderLayout.SOUTH,buttons);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setIconImage(GUIUtilities.getPluginIcon());

		pack();
		GUIUtilities.loadGeometry(this,"plugin-manager");
		show();
	} //}}}

	//{{{ updatePluginList() method
	private void updatePluginList()
	{
		if(jEdit.getSettingsDirectory() == null
			&& jEdit.getJEditHome() == null)
		{
			GUIUtilities.error(this,"no-settings",null);
			return;
		}
		else if(pluginList != null || downloadingPluginList)
		{
			return;
		}

		final Exception[] exception = new Exception[1];

		VFSManager.runInWorkThread(new WorkRequest()
		{
			public void run()
			{
				try
				{
					downloadingPluginList = true;
					setStatus(jEdit.getProperty(
						"plugin-manager.list-download"));
					pluginList = new PluginList();
				}
				catch(Exception e)
				{
					exception[0] = e;
				}
				finally
				{
					downloadingPluginList = false;
				}
			}
		});

		VFSManager.runInAWTThread(new Runnable()
		{
			public void run()
			{
				if(exception[0] instanceof XmlException)
				{
					XmlException xe = (XmlException)
						exception[0];

					int line = xe.getLine();
					String path = jEdit.getProperty(
						"plugin-manager.export-url");
					String message = xe.getMessage();
					Log.log(Log.ERROR,this,path + ":" + line
						+ ": " + message);
					String[] pp = { path,
						String.valueOf(line),
						message };
					GUIUtilities.error(PluginManager.this,
						"plugin-list.xmlerror",pp);
				}
				else if(exception[0] != null)
				{
					Exception e = exception[0];

					Log.log(Log.ERROR,this,e);
					String[] pp = { e.toString() };

					String ok = jEdit.getProperty(
						"common.ok");
					String proxyButton = jEdit.getProperty(
						"plugin-list.ioerror.proxy-servers");
					int retVal =
						JOptionPane.showOptionDialog(
						PluginManager.this,
						jEdit.getProperty("plugin-list.ioerror.message",pp),
						jEdit.getProperty("plugin-list.ioerror.title"),
						JOptionPane.YES_NO_OPTION,
						JOptionPane.ERROR_MESSAGE,
						null,
						new Object[] {
							proxyButton,
							ok
						},
						ok);

					if(retVal == 0)
					{
						new GlobalOptions(
							PluginManager.this,
							"firewall");
					}
				}
			}
		});
	} //}}}

	//}}}

	//{{{ Inner classes

	//{{{ ActionHandler class
	class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			Object source = evt.getSource();
			if(source == done)
				ok();
			else if (source == mgrOptions)
				new GlobalOptions(PluginManager.this,"plugin-manager");
			else if (source == pluginOptions)
				new PluginOptions(PluginManager.this);
		}
	} //}}}

	//{{{ ListUpdater class
	class ListUpdater implements ChangeListener
	{
		public void stateChanged(ChangeEvent e)
		{
			final Component selected = tabPane.getSelectedComponent();
			if(selected == installer || selected == updater)
			{
				updatePluginList();
				installer.updateModel();
				updater.updateModel();
			}
			else if(selected == manager)
				manager.update();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1396.java