error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/246.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/246.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/246.java
text:
```scala
P@@luginManager.getInstance().initCorePlugins();

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
package org.columba.mail.filter.plugins;

import java.io.ByteArrayInputStream;
import java.io.File;

import junit.framework.TestCase;

import org.columba.addressbook.main.AddressbookMain;
import org.columba.core.config.Config;
import org.columba.core.logging.Logging;
import org.columba.core.plugin.PluginManager;
import org.columba.mail.folder.AbstractMessageFolder;
import org.columba.mail.folder.FolderTstHelper;
import org.columba.mail.folder.MHFolderFactory;
import org.columba.mail.folder.MailboxTstFactory;
import org.columba.mail.main.MailMain;

/**
 * Base class for all filter tests.
 * <p>
 * Provides a test folder.
 * 
 * @author fdietz
 *  
 */
public class AbstractFilterTst extends TestCase {

	protected AbstractMessageFolder sourceFolder;

	protected MailboxTstFactory factory;

	/**
	 * Constructor for test.
	 * <p>
	 * This is used when executing this individual test only or by the ant task.
	 * <p>
	 */
	public AbstractFilterTst(String test) {
		super(test);

		this.factory = new MHFolderFactory();
	}

	/**
	 * Constructor for AbstractFilterTest.
	 * 
	 * @param arg0
	 * @param factory
	 */
	public AbstractFilterTst(MailboxTstFactory factory, String arg0) {
		super(arg0);
		this.factory = factory;
	}

	/**
	 * @return Returns the folder.
	 */
	public AbstractMessageFolder getSourceFolder() {
		return sourceFolder;
	}

	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {

		// create config-folder
		File file = new File("test_config");
		file.mkdir();

		new Config(file);

		Logging.DEBUG = true;
		Logging.createDefaultHandler();

		//		 init mail component
		new MailMain().init();

		new AddressbookMain().init();

		// now load all available plugins
		PluginManager.getInstance().initPlugins();

		sourceFolder = factory.createFolder(1);

		//		 create MH folder
		// -> use homeDirectory as top-level folder
		// -> this has to be an absolute path
		/*
		 * sourceFolder = new CachedMHFolder("test", "CachedMHFolder",
		 * FolderTstHelper.homeDirectory + "/folders/");
		 */
	}

	/**
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		File f = sourceFolder.getDirectoryFile();

		// delete all mails in folder
		File[] list = f.listFiles();

		for (int i = 0; i < list.length; i++) {
			list[i].delete();
		}

		// delete folder
		f.delete();

		sourceFolder.removeFolder();
	}

	/**
	 * Add message to test folder.
	 * 
	 * @throws Exception
	 */
	public Object addMessage() throws Exception {
		// add message "0.eml" as inputstream to folder
		String input = FolderTstHelper.getString(0);
		System.out.println("input=" + input);

		// create stream from string
		ByteArrayInputStream inputStream = FolderTstHelper
				.getByteArrayInputStream(input);

		// add stream to folder
		Object uid = getSourceFolder().addMessage(inputStream);

		// close stream
		inputStream.close();

		return uid;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/246.java