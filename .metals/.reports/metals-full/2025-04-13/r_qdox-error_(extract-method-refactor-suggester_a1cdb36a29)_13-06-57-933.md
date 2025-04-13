error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6959.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6959.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6959.java
text:
```scala
p@@ublic static class NoPluginsPane extends AbstractOptionPane

/*
 * PluginOptions.java - Plugin options dialog
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2003 Slava Pestov
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

//{{{ Imports
import java.awt.Dialog;
import java.awt.Frame;
import org.gjt.sp.jedit.gui.OptionsDialog;
import org.gjt.sp.jedit.options.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.Log;
//}}}

public class PluginOptions extends OptionsDialog
{
	//{{{ PluginOptions constructor
	public PluginOptions(Frame frame)
	{
		super(frame,"plugin-options",jEdit.getProperty("plugin-options.last"));
	} //}}}

	//{{{ PluginOptions constructor
	public PluginOptions(Frame frame, String pane)
	{
		super(frame,"plugin-options",pane);
	} //}}}

	//{{{ PluginOptions constructor
	public PluginOptions(Dialog dialog)
	{
		super(dialog,"plugin-options",jEdit.getProperty("plugin-options.last"));
	} //}}}

	//{{{ PluginOptions constructor
	public PluginOptions(Dialog dialog, String pane)
	{
		super(dialog,"plugin-options",pane);
	} //}}}

	//{{{ createOptionTreeModel() method
	protected OptionTreeModel createOptionTreeModel()
	{
		OptionTreeModel paneTreeModel = new OptionTreeModel();
		OptionGroup rootGroup = (OptionGroup) paneTreeModel.getRoot();

		// initialize the Plugins branch of the options tree
		pluginsGroup = new OptionGroup("plugins");
		pluginsGroup.setSort(true);

		// Query plugins for option panes
		EditPlugin[] plugins = jEdit.getPlugins();
		for(int i = 0; i < plugins.length; i++)
		{
			EditPlugin ep = plugins[i];
			if(ep instanceof EditPlugin.Broken)
				continue;

			String className = ep.getClassName();
			if(jEdit.getProperty("plugin." + className + ".activate") == null)
			{
				// Old API
				try
				{
					ep.createOptionPanes(this);
				}
				catch(Throwable t)
				{
					Log.log(Log.ERROR, ep,
						"Error creating option pane");
					Log.log(Log.ERROR, ep, t);
				}
			}
			else
			{
				String optionPane = jEdit.getProperty(
					"plugin." + className + ".option-pane");
				if(optionPane != null)
					pluginsGroup.addOptionPane(optionPane);
				else
				{
					String options = jEdit.getProperty(
						"plugin." + className
						+ ".option-group");
					if(options != null)
					{
						pluginsGroup.addOptionGroup(
							new OptionGroup(
							"plugin." + className,
							jEdit.getProperty("plugin."
							+ className + ".name"),
							options)
						);
					}
				}
			}
		}

		// only add the Plugins branch if there are OptionPanes
		if (pluginsGroup.getMemberCount() == 0)
			pluginsGroup.addOptionPane(new NoPluginsPane());

		rootGroup.addOptionGroup(pluginsGroup);

		return paneTreeModel;
	} //}}}

	//{{{ getDefaultGroup() method
	protected OptionGroup getDefaultGroup()
	{
		return pluginsGroup;
	} //}}}

	//{{{ Private members
	private OptionGroup pluginsGroup;
	//}}}

	//{{{ NoPluginsPane class
	static class NoPluginsPane extends AbstractOptionPane
	{
		public NoPluginsPane()
		{
			super("no-plugins");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6959.java