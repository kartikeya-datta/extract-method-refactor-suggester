error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3688.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3688.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3688.java
text:
```scala
S@@tring clazzString = action.getClass().getName();

//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.core.pluginhandler;

import java.util.HashMap;
import java.util.Map;

import org.columba.core.action.AbstractColumbaAction;
import org.columba.core.action.IMenu;
import org.columba.core.gui.frame.FrameMediator;
import org.columba.core.io.DiskIO;
import org.columba.core.plugin.AbstractPluginHandler;
import org.columba.core.plugin.PluginLoadingFailedException;
import org.columba.core.xml.XmlElement;
import org.columba.core.xml.XmlIO;

/**
 * Every action in Columba is handled by this class.
 * <p>
 * The core actions are listed in the org.columba.core.action.action.xml
 * <p>
 * These actions are used to generate the menu and the toolbar dynamically.
 * 
 * @author fdietz
 */
public class ActionPluginHandler extends AbstractPluginHandler {
	/**
	 * collects all singleton objects
	 * <p>
	 * this includes: - cut - copy - paste - delete - select all - undo - redo
	 * 
	 * @see isSingleton(String name)
	 * 
	 */
	Map map;

	public ActionPluginHandler() {
		super("org.columba.core.action", "org/columba/core/action/action.xml");

		parentNode = getConfig().getRoot().getElement("actionlist");

		map = new HashMap();
	}

	/**
	 * Returns true, if this is a single instance object which is shared among
	 * all frames.
	 * 
	 * Note that the most promiment actions overwriting this will be: -
	 * Cut/Copy/Paste/Delete - Select All - Undo/Redo
	 * 
	 * Specified by a property called "singletion" which can be of the value
	 * true/false. Found in the action node of the plugin.xml file respective
	 * the action.xml file.
	 * 
	 * @return true, if action follows the singleton pattern, which means its
	 *         instanciated only once, and reused by every MenuItem, Button,
	 *         etc.
	 * 
	 * false, is the default (which is the correct value for almost all actions)
	 * 
	 */
	public boolean isSingleton(String name) {
		return Boolean.valueOf(getAttribute(name, "singleton")).booleanValue();
	}

	public AbstractColumbaAction getAction(String name, FrameMediator controller) {
		if (isSingleton(name)) {
			// their should be only one shared instance
			if (map.containsKey(name)) {
				// already loaded
				// -> return existing instance from hashmap
				// -> don't use this temporary instance
				return (AbstractColumbaAction) map.get(name);
			} else {
				// put first time instance in hashmap
				AbstractColumbaAction a = null;

				try {
					a = (AbstractColumbaAction) getPlugin(name,
							new Object[] { controller });
					map.put(name, a);
				} catch (PluginLoadingFailedException e) {
					// exception already handled
				}

				return a;
			}
		}

		AbstractColumbaAction a = null;

		try {
			a = (AbstractColumbaAction) getPlugin(name,
					new Object[] { controller });
		} catch (PluginLoadingFailedException e) {
			// exception already handled
		}

		return a;
	}

	public IMenu getIMenu(String name, FrameMediator controller)
			throws Exception {
		return (IMenu) getPlugin(name, new Object[] { controller });
	}

	/**
	 * Register new action.
	 * 
	 * @param actionId
	 *            unique action id
	 * @param action
	 *            new action
	 */
	public void addAction(String actionId, AbstractColumbaAction action) {
		// generate xml element describing action
		XmlElement element = new XmlElement("action");
		element.addAttribute("name", actionId);
		String clazzString = action.getClass().getCanonicalName();
		element.addAttribute("class", clazzString);

		parentNode.addElement(element);
	}

	public void addActionList(String actionXml) {
		XmlIO actionXmlIO = new XmlIO();
		actionXmlIO.setURL(DiskIO.getResourceURL(actionXml));
		actionXmlIO.load();

		XmlElement actionlist = actionXmlIO.getRoot().getElement("actionlist");

		for (int i = 0; i < actionlist.count(); i++) {
			parentNode.addElement(actionlist.getElement(i));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3688.java