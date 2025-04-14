error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/922.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/922.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/922.java
text:
```scala
.@@instanciateExtension(null);

package org.columba.core.gui.externaltools;

import java.io.File;

import org.columba.api.exception.PluginHandlerNotFoundException;
import org.columba.api.plugin.IExtension;
import org.columba.core.config.Config;
import org.columba.core.plugin.PluginManager;
import org.columba.core.pluginhandler.ExternalToolsExtensionHandler;
import org.columba.core.xml.XmlElement;

/**
 * Provides an easy way to integrate external apps in Columba.
 * <p>
 * This includes a first-time assistant for the user. And a configuration file
 * "external_tools.xml" to store the options of the external tools.
 * <p>
 * When using external commandline (already used examples are aspell and GnuPG)
 * tools, you should just use this handler to get the location of the
 * executable.
 * <p>
 * If the executable wasn't configured, yet a wizard will assist the user in
 * configuring the external tool. If everything is correctly configured, it will
 * just return the path of the commandline tool as <code>File</code>.
 * <p>
 * <verbatim> File file = getLocationOfExternalTool("gpg"); </verbatim>
 * 
 * <p>
 * 
 * @see org.columba.api.plugin.external_tools.xml
 * 
 * @author fdietz
 */
public class ExternalToolsManager {

	private static ExternalToolsManager instance = new ExternalToolsManager();

	private ExternalToolsExtensionHandler handler;

	private ExternalToolsManager() {
	}

	public static ExternalToolsManager getInstance() {
		return instance;
	}

	private ExternalToolsExtensionHandler getHandler() {
		if (handler == null) {
			try {
				handler = (ExternalToolsExtensionHandler) PluginManager
						.getInstance().getHandler(
								ExternalToolsExtensionHandler.NAME);
			} catch (PluginHandlerNotFoundException e) {
				e.printStackTrace();
			}
		}

		return handler;
	}

	/**
	 * Gets the location of an external commandline tool.
	 * <p>
	 * TODO: test this method
	 * 
	 * @param toolID
	 *            id of tool
	 * @return location of tool
	 */
	public File getLocationOfExternalTool(String toolID) {
		AbstractExternalToolsPlugin plugin = null;

		try {
			IExtension extension = getHandler().getExtension(toolID);

			plugin = (AbstractExternalToolsPlugin) extension
					.instanciateExtension(new Object[] { null });
		} catch (Exception e1) {
			e1.printStackTrace();

			return null;
		}

		// check configuration
		XmlElement root = getConfiguration(toolID);

		if (root == null) {
			// create xml node
			XmlElement parent = Config.getInstance().get("external_tools")
					.getElement("tools");
			XmlElement child = new XmlElement("tool");
			child.addAttribute("first_time", "true");
			child.addAttribute("name", toolID);
			parent.addElement(child);

			root = child;
		}

		boolean firsttime = false;

		if (root.getAttribute("first_time").equals("true")) {
			firsttime = true;
		}

		if (firsttime) {
			// start the configuration wizard
			ExternalToolsWizardLauncher launcher = new ExternalToolsWizardLauncher();
			launcher.launchWizard(toolID, true);

			if (launcher.isFinished()) {
				// ok, now the tool is initialized correctly
				XmlElement r = getConfiguration(toolID);
				File file = new File(r.getAttribute("location"));

				return file;
			}
		} else {
			String location = root.getAttribute("location");

			File file = new File(location);

			return file;
		}

		return null;
	}

	/**
	 * Gets xml configuration of tool with id.
	 * 
	 * @param id
	 *            id of tool
	 * @return xml treenode
	 */
	public XmlElement getConfiguration(String id) {
		XmlElement root = Config.getInstance().get("external_tools")
				.getElement("tools");
		boolean firsttime = false;

		for (int i = 0; i < root.count(); i++) {
			XmlElement child = root.getElement(i);

			if (child.getAttribute("name").equals(id)) {
				return child;
			}
		}

		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/922.java