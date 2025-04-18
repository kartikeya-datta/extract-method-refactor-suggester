error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5271.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5271.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5271.java
text:
```scala
E@@rrorDialog.createDialog(details, e);

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

package org.columba.core.gui.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.logging.Logger;

import org.columba.api.exception.IExceptionListener;
import org.columba.core.connectionstate.ConnectionStateImpl;
import org.columba.core.gui.dialog.ErrorDialog;
import org.columba.core.resourceloader.GlobalResourceLoader;
import org.columba.ristretto.imap.IMAPDisconnectedException;
import org.columba.ristretto.imap.IMAPException;
import org.columba.ristretto.io.ConnectionDroppedException;

import sun.net.ConnectionResetException;

/**
 * Handles all exceptions catched by Worker.construct(). Opens error dialogs.
 * 
 * @author fdietz
 */
public class ExceptionHandler implements IExceptionListener {
	private static final String RESOURCE_PATH = "org.columba.core.i18n.dialog";

	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.api.command");

	/**
	 * Handle all kinds of exceptions.
	 * 
	 * @param e
	 *            exception to process
	 */
	public void processException(Exception e) {
		// Print the stacktrace to our log file.
		StringWriter error = new StringWriter();
		e.printStackTrace(new PrintWriter(error));
		LOG.severe(error.toString());

		if (e instanceof SocketException) {
			processSocketException((SocketException) e);
			ConnectionStateImpl.getInstance().setOnline(false);
		} else if (e instanceof IOException) {
			processIOException((IOException) e);
		} else if (e instanceof IMAPException) {
			processIMAPExcpetion((IMAPException) e);
		} else {
			// show error dialog, with exception message and stack-trace
			// -> dialog also provides a button for the user to easily
			// -> report a bug
			showErrorDialog(e.getMessage(), e);
		}
	}

	/**
	 * @param exception
	 */
	private void processIMAPExcpetion(IMAPException exception) {
		String errorMessage = "";
		String serverResponse = "";

		if (exception.getResponse() != null) {
			serverResponse = ": "
					+ exception.getResponse().getResponseMessage();
		}

		if (exception instanceof IMAPDisconnectedException) {
			errorMessage = GlobalResourceLoader.getString(RESOURCE_PATH,
					"error", "imap_disconnected_error");
		} else {
			errorMessage = GlobalResourceLoader.getString(RESOURCE_PATH,
					"error", "imap_error")
					+ serverResponse;
		}

		showErrorDialog(errorMessage, exception);
	}

	/**
	 * Handle all java.net.SocketException
	 * 
	 * @param e
	 *            a socket exception
	 */
	private void processSocketException(SocketException e) {
		String errorMessage = "";

		if (e instanceof ConnectException) {
			errorMessage = GlobalResourceLoader.getString(RESOURCE_PATH,
					"error", "connect_error");
		} else if (e instanceof NoRouteToHostException) {
			errorMessage = GlobalResourceLoader.getString(RESOURCE_PATH,
					"error", "no_route_to_host_error");
		} else if (e instanceof PortUnreachableException) {
			errorMessage = GlobalResourceLoader.getString(RESOURCE_PATH,
					"error", "port_unreachable_error");
		} else if (e instanceof ConnectionResetException) {
			errorMessage = GlobalResourceLoader.getString(RESOURCE_PATH,
					"error", "connection_reset");
		} else {
			errorMessage = GlobalResourceLoader.getString(RESOURCE_PATH,
					"error", "generic_socket_error");
		}

		showErrorDialog(errorMessage, e);
	}

	/**
	 * Handle all java.io.IOExceptions
	 * 
	 * @param e
	 *            io exception to process
	 */
	private void processIOException(IOException e) {
		String errorMessage = e.getMessage();

		if (e instanceof SocketTimeoutException) {
			errorMessage = GlobalResourceLoader.getString(RESOURCE_PATH,
					"error", "socket_timeout_error");
		} else if (e instanceof UnknownHostException) {
			errorMessage = MessageFormat.format(GlobalResourceLoader.getString(
					RESOURCE_PATH, "error", "unknown_host_error"),
					new Object[] { e.getMessage() });
		} else if (e instanceof ConnectionDroppedException) {
			errorMessage = GlobalResourceLoader.getString(RESOURCE_PATH,
					"error", "connection_dropped_error");
		} else {
			errorMessage = GlobalResourceLoader.getString(RESOURCE_PATH,
					"error", "generic_io_error");
		}

		showErrorDialog(errorMessage, e);
	}

	/**
	 * Show error dialog.
	 * 
	 * @param errorMessage
	 *            human-readable error message
	 * @param e
	 *            exception to process
	 */
	private void showErrorDialog(String details, Exception e) {

		if (details == null)
			details = e.toString();

		new ErrorDialog(details, e);

	}

	public void exceptionOccured(Exception e) {
		processException(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5271.java