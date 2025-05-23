error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4050.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4050.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4050.java
text:
```scala
h@@andler = PluginManager.getInstance().getExtensionHandler(

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
package org.columba.mail.folderoptions;

import java.util.Enumeration;

import org.columba.api.exception.PluginException;
import org.columba.api.exception.PluginHandlerNotFoundException;
import org.columba.api.plugin.IExtension;
import org.columba.api.plugin.IExtensionHandler;
import org.columba.core.plugin.PluginManager;
import org.columba.core.xml.XmlElement;
import org.columba.mail.config.FolderItem;
import org.columba.mail.config.IFolderItem;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.table.TableController;
import org.columba.mail.plugin.IExtensionHandlerKeys;

/**
 * Controller used by {@link TableController} to handle all folder-related
 * option plugins.
 * <p>
 * Note, that every {@link MailFrameMediator} keeps its own
 * <code>FolderOptionsController<code>, which makes sure that
 * all plugins are singletons.
 *
 * @author fdietz
 */
public class FolderOptionsController implements IFolderOptionsController {
	/**
	 * Plugins executed before updating the table model
	 * <p>
	 * example:<b> Sorting/Filtering state
	 */
	public final static int STATE_BEFORE = 0;

	/**
	 * Plugins executed after updating the table model
	 * <p>
	 * example:<br>
	 * Selection of messages
	 */
	public final static int STATE_AFTER = 1;

	/**
	 * mail frame mediator
	 */
	private MailFrameMediator mediator;

	/**
	 * plugin handler for instanciating folder options plugins
	 */
	private IExtensionHandler handler;

	/**
	 * Constructor
	 * 
	 * @param mediator
	 *            mail frame mediator
	 */
	public FolderOptionsController(MailFrameMediator mediator) {
		this.mediator = mediator;

		// init plugin handler
		try {
			handler = PluginManager.getInstance().getHandler(
					IExtensionHandlerKeys.ORG_COLUMBA_MAIL_FOLDEROPTIONS);
		} catch (PluginHandlerNotFoundException e) {
			// TODO (@author fdietz): show error dialoghere
			e.printStackTrace();
		}
	}

	/**
	 * Get plugin with specific name.
	 * 
	 * @param name
	 *            name of plugin
	 * @return instance of plugin
	 */
	public AbstractFolderOptionsPlugin getPlugin(String name) {

		AbstractFolderOptionsPlugin plugin = null;

		try {
			IExtension extension = handler.getExtension(name);

			plugin = (AbstractFolderOptionsPlugin) extension
					.instanciateExtension(new Object[] { mediator });
		} catch (Exception e) {
			// TODO (@author fdietz): add error dialog
			e.printStackTrace();
		}

		return plugin;

	}

	/**
	 * Load all folder options for this folder.
	 * 
	 * @param folder
	 *            selected folder
	 */
	public void load(IMailbox folder, int state) {
		// get list of plugins
		Enumeration e = handler.getExtensionEnumeration();
		while (e.hasMoreElements()) {
			IExtension extension = (IExtension) e.nextElement();
			String stateString = extension.getMetadata().getAttribute("state");
			try {
				AbstractFolderOptionsPlugin plugin = (AbstractFolderOptionsPlugin) extension
						.instanciateExtension(new Object[] { mediator });

				if ((state == STATE_BEFORE) && (stateString.equals("before"))) {
					plugin.loadOptionsFromXml(null);
				} else if ((state == STATE_AFTER)
						&& (stateString.equals("after"))) {
					plugin.loadOptionsFromXml(folder);
				}
			} catch (PluginException e1) {
				e1.printStackTrace();
			}

		}
	}

	/**
	 * Save all folder options for this folder.
	 * 
	 * @param folder
	 *            selected folder
	 */
	public void save(IMailbox folder) {
		// get list of plugins
		String[] ids = handler.getPluginIdList();

		for (int i = 0; i < ids.length; i++) {
			AbstractFolderOptionsPlugin plugin = getPlugin(ids[i]);
			plugin.saveOptionsToXml(folder);
		}
	}

	/**
	 * Load all folder options globally.
	 * 
	 */
	public void load(int state) {
		// get list of plugins
		Enumeration e = handler.getExtensionEnumeration();
		while (e.hasMoreElements()) {
			IExtension extension = (IExtension) e.nextElement();
			String stateString = extension.getMetadata().getAttribute("state");
			try {
				AbstractFolderOptionsPlugin plugin = (AbstractFolderOptionsPlugin) extension
						.instanciateExtension(new Object[] { mediator });

				if ((state == STATE_BEFORE) && (stateString.equals("before"))) {
					plugin.loadOptionsFromXml(null);
				} else if ((state == STATE_AFTER)
						&& (stateString.equals("after"))) {
					plugin.loadOptionsFromXml(null);
				}
			} catch (Exception e1) {
				// TODO (@author fdietz): add error dialog
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Get parent configuration node of plugin.
	 * <p>
	 * Example for the sorting plugin configuration node. This is how it can be
	 * found in options.xml and tree.xml:<br>
	 * 
	 * <pre>
	 *         
	 *          
	 *            &lt;sorting column=&quot;Date&quot; order=&quot;true&quot; /&gt;
	 *           
	 *          
	 * </pre>
	 * 
	 * <p>
	 * 
	 * @param folder
	 *            selected folder
	 * @param name
	 *            name of plugin (example: ColumnOptions)
	 * @return parent configuration node
	 */
	public XmlElement getConfigNode(IMailbox folder, String name) {
		XmlElement parent = null;
		boolean global = false;

		if ((folder == null) || (name == null)) {
			// if no folder was passed as argument, use global options
			parent = FolderItem.getGlobalOptions();
			global = true;
		} else {
			// use folder specific options
			parent = folder.getConfiguration().getFolderOptions();
			global = false;
		}

		// load plugin
		AbstractFolderOptionsPlugin plugin = getPlugin(name);
		XmlElement child = parent.getElement(plugin.getName());

		if (child == null) {
			// create default configuration
			child = plugin.createDefaultElement(global);
			parent.addElement(child);
		}

		if (global) {
			return child;
		}

		String overwrite = child.getAttribute("overwrite");

		// check if this folder is overwriting global options
		if ((overwrite != null) && (overwrite.equals("true"))) {
			// use folder-based options
			return child;
		} else {
			// use global options
			parent = FolderItem.getGlobalOptions();
			child = parent.getElement(plugin.getName());

			return child;
		}
	}

	/**
	 * Create default settings for this folder.
	 * 
	 * @param folder
	 *            selected folder
	 */
	public void createDefaultSettings(IMailbox folder) {
		IFolderItem item = folder.getConfiguration();
		XmlElement parent = item.getElement("property");

		// use global settings
		String[] ids = handler.getPluginIdList();

		for (int i = 0; i < ids.length; i++) {
			AbstractFolderOptionsPlugin plugin = getPlugin(ids[i]);
			XmlElement child = plugin.createDefaultElement(false);
			parent.addElement(child);
		}
	}

	/**
	 * Save global settings.
	 * <p>
	 * Method is called when shutting down Columba. Note, that when a folder is
	 * selected which overwrites options, only his options are saved.
	 * 
	 * @param folder
	 *            selected folder
	 */
	/*
	 * public void saveGlobalSettings(AbstractFolder folder) { if (folder
	 * instanceof AbstractMessageFolder) { if
	 * (isOverwritingDefaults((AbstractMessageFolder) folder)) {
	 * 
	 * save((AbstractMessageFolder)folder); } else { save(null); } } }
	 */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4050.java