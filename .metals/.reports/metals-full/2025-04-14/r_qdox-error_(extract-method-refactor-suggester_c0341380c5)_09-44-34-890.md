error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5868.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5868.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5868.java
text:
```scala
X@@mlElement element = PluginManager.getInstance()

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.

package org.columba.core.plugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.columba.core.gui.util.ErrorDialog;
import org.columba.core.gui.util.MultiLineLabel;
import org.columba.core.loader.DefaultClassLoader;
import org.columba.core.main.MainInterface;
import org.columba.core.util.GlobalResourceLoader;
import org.columba.core.xml.XmlElement;

/**
 * Every entrypoint is represented by this abstract handler class
 * <p>
 * We use the Strategy Pattern here.
 * <p>
 * The <class>AbstractPluginHandler </class> is the Context of the Strategy
 * pattern.
 * <p>
 * The plugins ( <interface>Plugin </interface>) represent the used strategy.
 * <p>
 * Therefore, the context is responsible to set the appropriate strategy we use.
 * <p>
 * In other words the plugin handler decides which plugin should be executed and
 * returns an instance of the plugin class.
 * <p>
 * 
 * example of loading a plugin: <code>
 *
 * ActionPluginHandler handler = (ActionPluginHandler)
 *           MainInterface.pluginManager.getHandler("org.columba.core.action");
 *
 * Object[] parameter = { myparam };
 *
 * Action action = handler.getPlugin("MoveAction", parameter);
 * </code>
 * <p>
 * Please read the documentation of the corresponding methods for more details.
 * 
 * @author fdietz
 */
public abstract class AbstractPluginHandler implements PluginHandler {

	private static final java.util.logging.Logger LOG = java.util.logging.Logger
			.getLogger("org.columba.core.plugin");

	private static final String RESOURCE_PATH = "org.columba.core.i18n.dialog";

	protected String id;

	protected XmlElement parentNode;

	protected PluginListConfig pluginListConfig;

	protected PluginManager pluginManager;

	//	translate plugin-id to user-visible name
	//  example: org.columba.example.HelloWorld$HelloPlugin -> HelloWorld
	protected Hashtable transformationTable;

	/**
	 * associate extension with plugin which owns the extension
	 */
	protected Map pluginMap;

	protected List externalPlugins;

	/**
	 * @param id
	 * @param config
	 */
	public AbstractPluginHandler(String id, String config) {
		super();
		this.id = id;
		transformationTable = new Hashtable();

		if (config != null) {
			pluginListConfig = new PluginListConfig(config);
		}

		externalPlugins = new Vector();

		pluginMap = new HashMap();

		LOG.info("initialising plugin-handler: " + id);
	}

	/**
	 * @return
	 */
	protected PluginListConfig getConfig() {
		return pluginListConfig;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns an instance of the plugin
	 * 
	 * example plugin constructor:
	 * 
	 * public Action(JFrame frame, String text) { .. do anything .. }
	 * 
	 * --> arguments:
	 * 
	 * Object[] args = { frame, text };
	 * 
	 * @param name
	 *            name of plugin
	 * @param args
	 *            constructor arguments needed to instanciate the plugin
	 * @return instance of plugin class
	 * 
	 * @throws Exception
	 */
	public Object getPlugin(String name, Object[] args)
			throws PluginLoadingFailedException {
		String className = getPluginClassName(name, "class");

		if (className == null) {
			// if className isn't specified show error dialog
			handlePluginError(name);
			throw new PluginLoadingFailedException();
		}

		Object plugin = null;

		try {
			plugin = getPlugin(name, className, args);
		} catch (Exception e) {
			if (MainInterface.DEBUG)
				e.printStackTrace();

			// show error message
			handlePluginError(name);
			throw new PluginLoadingFailedException();
		}

		return plugin;
	}

	/**
	 * Shows an error message and disables the plugin.
	 */
	protected void handlePluginError(String plugin) {
		JOptionPane.showMessageDialog(null, new MultiLineLabel(MessageFormat
				.format(GlobalResourceLoader.getString(RESOURCE_PATH,
						"pluginmanager", "errLoad.msg"),
						new String[] { plugin })), GlobalResourceLoader
				.getString(RESOURCE_PATH, "pluginmanager", "errLoad.title"),
				JOptionPane.ERROR_MESSAGE);

		// get plugin id
		String pluginId = (String) pluginMap.get(plugin);

		// disable plugin
		pluginManager.setEnabled(pluginId, false);
	}

	/**
	 * @param name
	 * @param className
	 * @param args
	 * @return @throws
	 *         Exception
	 */
	protected Object getPlugin(String name, String className, Object[] args)
			throws Exception {
		try {
			// first try to load this plugin as internal plugin
			return loadPlugin(className, args);
		} catch (ClassNotFoundException ex) {
			// this didn't work -> try to load it as external plugin

			// get plugin id
			String pluginId = (String) pluginMap.get(name);

			// get runtime properties:
			// get type of plugin: "java" or "python"
			String type = pluginManager.getPluginType(pluginId);

			// if type=="java", it could be packaged as jar-file
			File pluginDir = pluginManager.getJarFile(pluginId);

			return PluginLoader.loadExternalPlugin(className, type, pluginDir,
					args);
		} catch (InvocationTargetException ex) {
			// error while instanciating plugin
			ex.getTargetException().printStackTrace();

			// show exception in dialog
			new ErrorDialog(ex.getMessage(), ex.getTargetException());

			throw ex;
		}
	}

	/**
	 * @param name
	 *            example: "org.columba.example.TextPlugin"
	 * @param id
	 *            this is usually just "class"
	 * @return
	 */
	protected String getPluginClassName(String name, String id) {
		int count = parentNode.count();

		for (int i = 0; i < count; i++) {
			XmlElement action = parentNode.getElement(i);

			String s = action.getAttribute("name");

			if (name.equals(s)) {
				String clazz = action.getAttribute(id);

				if (clazz == null) {
					// couldn't find attribute <id>, which is
					// usually simply "class"
					LOG.severe("Can't find classname \"" + id
							+ "\" for plugin \"" + name + "\".");

					// list all attributes of this plugin
					if (MainInterface.DEBUG)
						XmlElement.printNode(action, " ");
				}

				return clazz;
			}
		}

		// oops - couldn't find plugin with this "name"
		LOG.severe("Can't find plugin with name \"" + name
				+ "\" in plugin-handler.");
		LOG
				.severe("Check if the plugin was disabled before. If so, set the enabled-attribute in plugin.xml to \"true\"");

		if (MainInterface.DEBUG) {
			// list all available plugins of this handler
			XmlElement.printNode(parentNode, " ");
		}

		return null;
	}

	/**
	 * return value of xml attribute of specific plugin
	 * 
	 * @param name
	 *            name of plugin
	 * @param attribute
	 *            key of xml attribute
	 * @return value of xml attribute
	 */
	public String getAttribute(String name, String attribute) {
		int count = parentNode.count();

		for (int i = 0; i < count; i++) {
			XmlElement action = parentNode.getElement(i);

			String s = action.getAttribute("name");

			// return if attribute was not found
			if (s == null) {
				return null;
			}

			/*
			 * if (s.indexOf('$') != -1) { // this is an external plugin // ->
			 * extract the correct id s = s.substring(0, s.indexOf('$')); }
			 */
			if (name.equals(s)) {
				String value = action.getAttribute(attribute);

				return value;
			}
		}

		return null;
	}

	/**
	 * Return array of enabled plugins.
	 * 
	 * @return array of enabled plugins
	 */
	public String[] getPluginIdList() {
		int count = parentNode.count();

		//String[] list = new String[count];
		List list = new Vector();

		for (int i = 0; i < count; i++) {
			XmlElement action = parentNode.getElement(i);
			String s = action.getAttribute("name");

			XmlElement element = MainInterface.pluginManager
					.getPluginElement(s);

			if (element == null) {
				// this is no external plugin
				// -> just add it to the list
				list.add(s);

				continue;
			}

			String enabled = element.getAttribute("enabled");

			if (enabled == null) {
				enabled = "true";
			}

			boolean e = Boolean.valueOf(enabled).booleanValue();

			if (e) {
				list.add(s);
			}

			//list[i] = s;
		}

		String[] strs = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			strs[i] = (String) list.get(i);
		}

		//return list;
		return strs;
	}

	public boolean exists(String id) {
		LOG.fine("id=" + id);

		String[] list = getPluginIdList();

		for (int i = 0; i < list.length; i++) {
			String plugin = list[i];
			String searchId = plugin;

			/*
			 * int index = plugin.indexOf("$"); String searchId; if (index !=
			 * -1) searchId = plugin.substring(0, plugin.indexOf("$")); else
			 * searchId = plugin;
			 */
			LOG.fine(" - plugin id=" + plugin);
			LOG.fine(" - search id=" + searchId);

			if (searchId.equals(id)) {
				return true;
			}
		}

		return false;
	}

	public ListIterator getExternalPlugins() {
		return externalPlugins.listIterator();
	}

	/**
	 * @return
	 */
	public PluginManager getPluginManager() {
		return pluginManager;
	}

	/**
	 * @param className
	 * @param args
	 * @return @throws
	 *         Exception
	 */
	protected Object loadPlugin(String className, Object[] args)
			throws Exception {
		return new DefaultClassLoader().instanciate(className, args);
	}

	/**
	 * @param pluginManager
	 */
	public void setPluginManager(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}

	public String getUserVisibleName(String id) {
		// this is no external plugin
		//  -> just return the name
		if (id.indexOf('$') == -1) {
			return id;
		}

		//String pluginId = id.substring(0, id.indexOf('$'));
		//String name = id.substring(id.indexOf('$'), id.length() - 1);
		int count = parentNode.count();

		for (int i = 0; i < count; i++) {
			XmlElement action = parentNode.getElement(i);
			String s = action.getAttribute("name");
			String s2 = action.getAttribute("uservisiblename");

			if (id.equals(s)) {
				return s2;
			}
		}

		return null;
	}

	/**
	 * Register plugin at this extension point.
	 * 
	 * @param id
	 * @param extension
	 */
	public void addExtension(String id, XmlElement extension) {
		// add external plugin to list
		// --> this is used to distinguish internal/external plugins
		externalPlugins.add(id);

		ListIterator iterator = extension.getElements().listIterator();
		XmlElement action;

		while (iterator.hasNext()) {
			action = (XmlElement) iterator.next();
			action.addAttribute("uservisiblename", action.getAttribute("name"));

			// associate extension with plugin which owns the extension
			pluginMap.put(action.getAttribute("name"), id);

			/*
			 * String newName = id + '$' + action.getAttribute("name"); String
			 * userVisibleName = action.getAttribute("name"); // associate id
			 * with newName for later reference transformationTable.put(id,
			 * newName);
			 * 
			 * action.addAttribute("name", newName);
			 * action.addAttribute("uservisiblename", userVisibleName);
			 */
			parentNode.addElement(action);
		}
	}

	public XmlElement getParent() {
		return parentNode;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5868.java