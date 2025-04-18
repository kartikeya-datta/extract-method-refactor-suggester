error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1450.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1450.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1450.java
text:
```scala
public v@@oid addLoggedStatus(IStatus status) {

/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.statushandlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.internal.WorkbenchErrorHandlerProxy;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.misc.StatusUtil;
import org.eclipse.ui.internal.statushandlers.StatusHandlerDescriptor;
import org.eclipse.ui.internal.statushandlers.StatusHandlerRegistry;

/**
 * <p>
 * Status manager is responsible for handling statuses due to the set handling
 * policy.
 * </p>
 * 
 * <p>
 * Handlers shoudn't be used directly but through the StatusManager singleton
 * which keeps the status handling policy and chooses handlers due to it.
 * <code>StatusManager.getManager().handle(IStatus)</code> and
 * <code>handle(IStatus status, int
 * hint)</code> methods are used for passing
 * all problems to the facility.
 * </p>
 * 
 * <p>
 * Handling hints
 * <ul>
 * <li>NONE - nothing should be done with the status</li>
 * <li>LOG - the status should be logged</li>
 * <li>SHOW - the status should be shown to an user</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Default policy (steps):
 * <ul>
 * <li>manager tries to handle the status with a default handler</li>
 * <li>manager tries to find a right handler for the status</li>
 * <li>manager delegates the status to workbench handler</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Each status handler defined in "statusHandlers" extension can have package
 * prefix assigned to it. During step 2 status manager is looking for the most
 * specific handler for given status checking status pluginId against these
 * prefixes. The default handler is not used in this step.
 * </p>
 * 
 * <p>
 * The default handler can be set for product using
 * "statusHandlerProductBinding" element in "statusHandlers" extension.
 * </p>
 * 
 * <p>
 * Workbench handler is the
 * {@link org.eclipse.ui.internal.WorkbenchErrorHandlerProxy} object which
 * passes handling to handler assigned to the workbench advisor. This handler
 * doesn't have to be added as "statusHandlers" extension.
 * </p>
 * 
 * <strong>EXPERIMENTAL</strong> This class or interface has been added as part
 * of a work in progress. This API may change at any given time. Please do not
 * use this API without consulting with the Platform/UI team.
 * 
 * @since 3.3
 */
public class StatusManager {
	/**
	 * A handling hint indicating that nothing should be done with a problem
	 */
	public static final int NONE = 0;

	/**
	 * A handling hint indicating that handlers should log a problem
	 */
	public static final int LOG = 0x01;

	/**
	 * A handling hint indicating that handlers should show a problem to an user
	 */
	public static final int SHOW = 0x02;

	private static StatusManager MANAGER;

	private StatusHandlerRegistry statusHandlerRegistry;

	private AbstractStatusHandler workbenchHandler;

	private List loggedStatuses = new ArrayList();

	/**
	 * Returns StatusManager singleton instance
	 * 
	 * @return StatusManager instance
	 */
	public static StatusManager getManager() {
		if (MANAGER == null) {
			MANAGER = new StatusManager();
		}
		return MANAGER;
	}

	private StatusManager() {
		statusHandlerRegistry = new StatusHandlerRegistry();
		Platform.addLogListener(new StatusManagerLogListener());
	}

	/**
	 * @return the workbench status handler
	 */
	private AbstractStatusHandler getWorkbenchHandler() {
		if (workbenchHandler == null) {
			workbenchHandler = new WorkbenchErrorHandlerProxy();
		}

		return workbenchHandler;
	}

	/**
	 * Handles status due to the prefix policy.
	 * 
	 * @param status
	 *            status to handle
	 * @param hint
	 *            handling hint
	 */
	public void handle(IStatus status, int hint) {
		StatusHandlingState handlingState = new StatusHandlingState(status,
				hint);

		// tries to handle the problem with default (product) handler
		if (statusHandlerRegistry.getDefaultHandlerDescriptor() != null) {
			try {
				boolean shouldContinue = statusHandlerRegistry
						.getDefaultHandlerDescriptor().getStatusHandler()
						.handle(handlingState);

				if (!shouldContinue) {
					return;
				}
			} catch (CoreException ex) {
				logError("Errors during the default handler creating", ex); //$NON-NLS-1$
			}
		}

		// tries to handle the problem with any handler due to the prefix policy
		List okHandlerDescriptors = statusHandlerRegistry
				.getHandlerDescriptors(status.getPlugin());

		if (okHandlerDescriptors != null && okHandlerDescriptors.size() > 0) {
			StatusHandlerDescriptor handlerDescriptor = null;

			for (Iterator it = okHandlerDescriptors.iterator(); it.hasNext();) {
				handlerDescriptor = (StatusHandlerDescriptor) it.next();

				try {
					boolean shouldContinue = handlerDescriptor
							.getStatusHandler().handle(handlingState);

					if (!shouldContinue) {
						return;
					}
				} catch (CoreException ex) {
					logError("Errors during the handler creating", ex); //$NON-NLS-1$
				}
			}
		}

		// delegates the problem to workbench handler
		getWorkbenchHandler().handle(handlingState);
	}

	/**
	 * Handles status due to the prefix policy.
	 * 
	 * @param status
	 *            status to handle
	 */
	public void handle(IStatus status) {
		handle(status, LOG);
	}

	/**
	 * This method informs the StatusManager that this IStatus is being handled
	 * by the handler and to ignore it when it shows up in our ILogListener.
	 * 
	 * @param status
	 *            already handled and logged status
	 */
	void addLoggedStatus(IStatus status) {
		loggedStatuses.add(status);
	}

	private void logError(String message, Throwable ex) {
		IStatus status = StatusUtil.newStatus(WorkbenchPlugin.PI_WORKBENCH,
				message, ex);
		addLoggedStatus(status);
		WorkbenchPlugin.log(status);
	}

	/**
	 * This log listener handles statuses added to a plug-in's log. If our own
	 * WorkbenchErrorHandler inserts it into the log, then ignore it.
	 * 
	 * @see #addLoggedStatus(IStatus)
	 * @since 3.3
	 */
	private class StatusManagerLogListener implements ILogListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.ILogListener#logging(org.eclipse.core.runtime.IStatus,
		 *      java.lang.String)
		 */
		public void logging(IStatus status, String plugin) {
			if (!loggedStatuses.contains(status)) {
				handle(status, NONE);
			} else {
				loggedStatuses.remove(status);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1450.java