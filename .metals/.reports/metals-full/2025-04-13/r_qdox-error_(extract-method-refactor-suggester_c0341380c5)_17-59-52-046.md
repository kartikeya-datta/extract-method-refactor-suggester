error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2452.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2452.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2452.java
text:
```scala
C@@olumbaCmdLineParser.getInstance().setRestoreLastSession(false);

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
package org.columba.addressbook.main;

import java.io.InputStream;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.columba.addressbook.shutdown.SaveAllAddressbooksPlugin;
import org.columba.api.exception.PluginHandlerNotFoundException;
import org.columba.api.exception.PluginLoadingFailedException;
import org.columba.api.plugin.IExtensionHandlerKeys;
import org.columba.core.backgroundtask.BackgroundTaskManager;
import org.columba.core.component.IComponentPlugin;
import org.columba.core.gui.frame.FrameManager;
import org.columba.core.main.ColumbaCmdLineParser;
import org.columba.core.main.Main;
import org.columba.core.plugin.PluginManager;
import org.columba.core.resourceloader.GlobalResourceLoader;
import org.columba.core.services.ServiceRegistry;
import org.columba.core.shutdown.ShutdownManager;

/**
 * Main entrypoint for addressbook component
 * 
 * @author fdietz
 */
public class AddressbookMain implements IComponentPlugin {
	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.addressbook.main");

	private static final String RESOURCE_PATH = "org.columba.addressbook.i18n.global";

	public AddressbookMain() {
	}

	/**
	 * @see org.columba.core.component.IComponentPlugin#handleCommandLineParameters()
	 */
	public void handleCommandLineParameters(CommandLine commandLine) {
		if (commandLine.hasOption("addressbook")) {
			try {
				FrameManager.getInstance().openView("Addressbook");

				Main.getInstance().setRestoreLastSession(false);
			} catch (PluginLoadingFailedException e) {
				LOG.severe(e.getLocalizedMessage());
			}
		}
	}

	/**
	 * @see org.columba.core.component.IComponentPlugin#init()
	 */
	public void init() {
		// init addressbook plugin handlers
//		PluginManager.getInstance().addHandlers(
//				"org/columba/addressbook/plugin/pluginhandler.xml");

	/*	try {
			InputStream is = this.getClass().getResourceAsStream(
					"/org/columba/addressbook/action/action.xml");
			 PluginManager.getInstance().getHandler(
					IExtensionHandlerKeys.ORG_COLUMBA_CORE_ACTION).loadExtensionsFromStream(is);
		} catch (PluginHandlerNotFoundException ex) {
		}*/

		Runnable plugin = new SaveAllAddressbooksPlugin();
		BackgroundTaskManager.getInstance().register(plugin);
		ShutdownManager.getInstance().register(plugin);

		ServiceRegistry.getInstance().register(
				org.columba.addressbook.facade.IContactFacade.class,
				"org.columba.addressbook.facade.ContactFacade");
		ServiceRegistry.getInstance().register(
				org.columba.addressbook.facade.IFolderFacade.class,
				"org.columba.addressbook.facade.FolderFacade");
		ServiceRegistry.getInstance().register(
				org.columba.addressbook.facade.IConfigFacade.class,
				"org.columba.addressbook.facade.ConfigFacade");
		ServiceRegistry.getInstance().register(
				org.columba.addressbook.facade.IDialogFacade.class,
				"org.columba.addressbook.facade.DialogFacade");
		ServiceRegistry.getInstance().register(
				org.columba.addressbook.facade.IModelFacade.class,
				"org.columba.addressbook.facade.ModelFacade");
	}

	/**
	 * @see org.columba.core.component.IComponentPlugin#postStartup()
	 */
	public void postStartup() {
	}

	/**
	 * @see org.columba.core.component.IComponentPlugin#registerCommandLineArguments()
	 */
	public void registerCommandLineArguments() {
		ColumbaCmdLineParser parser = ColumbaCmdLineParser.getInstance();

		parser.addOption(new Option("addressbook", GlobalResourceLoader
				.getString(RESOURCE_PATH, "global", "cmdline_addressbook")));

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2452.java