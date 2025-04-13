error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8024.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8024.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8024.java
text:
```scala
.@@getInstance().getExtensionHandler(IExtensionHandlerKeys.ORG_COLUMBA_MAIL_SPAM);

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
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.

package org.columba.mail.spam;

import java.util.logging.Logger;

import org.columba.api.exception.PluginException;
import org.columba.api.exception.PluginHandlerNotFoundException;
import org.columba.api.plugin.IExtension;
import org.columba.api.plugin.IExtensionHandler;
import org.columba.core.logging.Logging;
import org.columba.core.plugin.PluginManager;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.plugin.IExtensionHandlerKeys;

/**
 * High-level wrapper for the spam filter.
 * <p>
 * Class should be used by Columba, to add ham or spam messages to the training
 * database. And to score messages using this training set.
 * <p>
 * 
 * @author fdietz
 */
public class SpamController implements ISpamPlugin {

	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.core.gui.htmlviewer");

	/**
	 * singleton pattern instance of this class
	 */
	private static SpamController instance;

	private ISpamPlugin spamPlugin;

	/**
	 * private constructor
	 */
	private SpamController() {

		try {
			IExtensionHandler handler =  PluginManager
					.getInstance().getHandler(IExtensionHandlerKeys.ORG_COLUMBA_MAIL_SPAM);

			IExtension extension = handler.getExtension("SpamAssassin");

			spamPlugin = (ISpamPlugin) extension.instanciateExtension(null);

		} catch (PluginHandlerNotFoundException e) {
			LOG.severe(e.getMessage());
			if (Logging.DEBUG)
				e.printStackTrace();

		} catch (PluginException e) {
			LOG.severe(e.getMessage());
			if (Logging.DEBUG)
				e.printStackTrace();
		}
	}

	/**
	 * Get instance of class.
	 * 
	 * @return spam controller
	 */
	public static SpamController getInstance() {
		if (instance == null)
			instance = new SpamController();

		return instance;
	}

	public boolean scoreMessage(IMailbox mailbox, Object uid) throws Exception {

		return spamPlugin.scoreMessage(mailbox, uid);
	}

	public void trainMessageAsSpam(IMailbox mailbox, Object uid)
			throws Exception {
		spamPlugin.trainMessageAsSpam(mailbox, uid);
	}

	public void trainMessageAsHam(IMailbox mailbox, Object uid)
			throws Exception {
		spamPlugin.trainMessageAsHam(mailbox, uid);
	}

	/**
	 * Save frequency DB to file.
	 */
	public void save() {
		spamPlugin.save();
	}

	public void load() {
		spamPlugin.load();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8024.java