error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9612.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9612.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,32]

error in qdox parser
file content:
```java
offset: 32
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9612.java
text:
```scala
"org.columba.mail.filteraction")@@;

// The contents of this file are subject to the Mozilla Public License Version 1.1
// (the "License"); you may not use this file except in compliance with the 
// License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
// Software distributed under the License is distributed on an "AS IS" basis,
// WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
// for the specific language governing rights and
// limitations under the License.
//
// The Original Code is "The Columba Project"
//
// The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
// Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
// All Rights Reserved.
//$Log: Filter.java,v $

package org.columba.mail.filter;

import org.columba.core.command.Command;
import org.columba.core.command.CompoundCommand;
import org.columba.core.config.DefaultItem;
import org.columba.core.main.MainInterface;
import org.columba.core.xml.XmlElement;
import org.columba.mail.filter.plugins.AbstractFilterAction;
import org.columba.mail.folder.Folder;
import org.columba.mail.plugin.AbstractFilterPluginHandler;
import org.columba.mail.plugin.FilterActionPluginHandler;

/**
 * 
 * @author frd
 *
 * This is a wrapper for the filter xml configuration, which makes
 * code easier to read in comparison to using the XmlElement stuff.
 * 
 */

// example configuration (tree.xml):
//
// <filter description="gnome" enabled="true">
//  <rules condition="matchany">
//   <criteria criteria="contains" headerfield="To or Cc" pattern="gnome" type="To or Cc"></criteria>
//  </rules>
//  <actionlist>
//   <action uid="120" type="Move Message"></action>
//  </actionlist>
// </filter>
//
public class Filter extends DefaultItem {

	/**
	 * 
	 * Constructor for Filter
	 * 
	 * XmlElement should be "filter" in this case
	 * 
	 * @see org.columba.core.config.DefaultItem#DefaultItem(XmlElement)
	 */
	public Filter(XmlElement root) {
		super(root);

	}

	/**
	 * 
	 * @return FilterActionList 	this is also a simple wrapper
	 */
	public FilterActionList getFilterActionList() {
		return new FilterActionList(getRoot().getElement("actionlist"));
	}

	/**
	 * 
	 * 
	 * @return FilterRule	this is also a simple wrapper
	 */
	public FilterRule getFilterRule() {
		return new FilterRule(getRoot().getElement("rules"));
	}

	/**
	 * Is filter enabled?
	 * 
	 * @return boolean	true if enabled
	 */
	public boolean getEnabled() {

		return getBoolean("enabled");
	}

	/**
	 * 
	 * enable Filter
	 * 
	 * @param bool	if true enable filter otherwise disable filter
	 */
	public void setEnabled(boolean bool) {
		set("enabled", bool);

	}
	
	/**
	 * Set filter name
	 * 
	 * @param s		new filter name
	 */
	public void setName(String s) {
		set("description", s);

	}
	
	/**
	 * 
	 *  return Name of Filter
	 * 
	 * @return String
	 */
	public String getName() {
		return get("description");

	}

	/**
	 * if filter matches we need to execute all actions 
	 * 
	 * For efficiency reasons all commands are packaged in
	 * a compound command object. This compound command uses
	 * only one worker to execute all commands, instead of 
	 * creating new workers for every command
	 * 
	 * @param srcFolder				source folder
	 * @param uids					message uid array
	 * @return CompoundCommand		return Collection of Commands
	 * @throws Exception
	 */
	public CompoundCommand getCommand(Folder srcFolder, Object[] uids)
		throws Exception {
			
		// instanciate CompoundCommand
		CompoundCommand c = new CompoundCommand();

		// get plugin handler for filter actions
		FilterActionPluginHandler pluginHandler =
			(FilterActionPluginHandler) MainInterface.pluginManager.getHandler(
				"filter_actions");

		// get list of all filter actions
		FilterActionList list = getFilterActionList();
		for (int i = 0; i < list.getChildCount(); i++) {
			// interate through all filter actions
			FilterAction action = list.get(i);

			// name is used to load plugin
			String name = action.getAction();
			AbstractFilterAction instance = null;

			// try to get instance of FilterAction
			try {
				instance =
					(AbstractFilterAction)
						(
							(
								AbstractFilterPluginHandler) pluginHandler)
									.getActionPlugin(
						name,
						null);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			// retrieve Command of filter action
			Command command = instance.getCommand(action, srcFolder, uids);

			// add command to CompoundCommand
			if (command != null)
				c.add(command);

		}

		// return CompoundCommand
		return c;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9612.java