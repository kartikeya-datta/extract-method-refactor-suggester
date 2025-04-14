error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5101.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5101.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5101.java
text:
```scala
c@@onsoleHandler.setLevel(Level.SEVERE);

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
package org.columba.core.logging;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.columba.ristretto.log.RistrettoLogger;

/**
 * Depending on the debug flag (--debug command line option reflected in
 * MainInterface.DEBUG) the logger will either show all debug messages or just
 * severe errors. Logging information is passed to a log file and to the
 * console.
 * <p>
 * Note, that Logging must not be called before MainInterface.DEBUG, was set.
 * Otherwise, the logger won't show the correct debug level.
 * <p>
 * If the user has defined their own logging config file, then this will take
 * precedence over Columba defined logging handlers, ie. Columba will not create
 * its own default logging handlers. All has already been defined in the system
 * property <b>java.util.logging.config.file</b>.
 * <p>
 * 
 * @see org.columba.core.main.Main
 * @see java.util.logging.Logger
 * @author redsolo
 */
public final class Logging {

	private static final Logger LOG = Logger.getLogger("org.columba");

	private static ConsoleHandler consoleHandler;

	/** If true, enables debugging output from org.columba.core.logging */
	public static boolean DEBUG = false;

	/**
	 * Don't instanciate this class.
	 */
	private Logging() {
	}

	/**
	 * Returns true if the user has defined a logging config file. The user can
	 * define a config file using the system property
	 * <code>java.util.logging.config.file</code>.
	 * 
	 * @return true if the user has defined a logging config file; false
	 *         otherwise.
	 */
	private static boolean userHasDefinedLogging() {
		return (System.getProperty("java.util.logging.config.file") != null);
	}

	/**
	 * Creates the console handler. The console handler outputs only the
	 * severest logging message unless the MainInterface.DEBUG flag is set.
	 */
	public static void createDefaultHandler() {

		if (!userHasDefinedLogging()) {

			// Since Columba is doing its own logging handlers, we should not
			// use handlers in the parent logger.
			LOG.setUseParentHandlers(false);

			// init console handler
			consoleHandler = new ConsoleHandler();

			consoleHandler.setFormatter(new OneLineFormatter());
			consoleHandler.setLevel(Level.ALL);

			LOG.addHandler(consoleHandler);
		}
	}

	public static void setDebugging(boolean debug) {
		if (debug) {
			consoleHandler.setFormatter(new DebugFormatter());
			consoleHandler.setLevel(Level.ALL);

			LOG.setLevel(Level.ALL);
			// System.setProperty("javax.net.debug",
			// "ssl,handshake,data,trustmanager"); // init java.net.ssl
			// debugging

			// TODO Ristretto should handle the logging of streams in another
			// way.
			RistrettoLogger.setLogStream(System.out);
		} else {
			consoleHandler.setFormatter(new OneLineFormatter());
			consoleHandler.setLevel(Level.SEVERE);

			LOG.setLevel(Level.SEVERE);
		}
	}

	/**
	 * Default logger configuration used by Columba.
	 * <p>
	 * If the user has not defined their own config file for the logging
	 * framework, then this will create a log file named
	 * <code>columba.log</code>, in the default config directory.
	 */
	public static void createDefaultFileHandler(File configDirectory) {

		if (!userHasDefinedLogging()) {
			String logConfigFile = System
					.getProperty("java.util.logging.config.file");
			if (logConfigFile == null) {

				// create logging file in "<users config-folder>/log"
				File file = new File(configDirectory, "log");
				if (!file.exists())
					file.mkdir();

				File loggingFile = new File(file, "columba.log");

				// Setup file logging
				try {
					Handler handler = new FileHandler(loggingFile.getPath(),
							false);
					handler.setFormatter(new SimpleFormatter()); // don't use
																	// standard
																	// XML
																	// formatting

					if (Logging.DEBUG) {
						handler.setLevel(Level.ALL);
					} else {
						handler.setLevel(Level.WARNING);
					}

					LOG.addHandler(handler);
				} catch (IOException ioe) {
					LOG.severe("Could not start the file logging due to: "
							+ ioe);
				}
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5101.java