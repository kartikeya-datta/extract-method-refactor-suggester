error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3504.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3504.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3504.java
text:
```scala
.@@getService(IContextService.class);

/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.internal.services;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.contexts.IContextService;

/**
 * A provider of notifications for when the active shell changes.
 * 
 * @since 3.1
 */
public final class ActiveShellSourceProvider extends AbstractSourceProvider {

	/**
	 * The names of the sources supported by this source provider.
	 */
	private static final String[] PROVIDED_SOURCE_NAMES = new String[] {
			ISources.ACTIVE_SHELL_NAME, ISources.ACTIVE_WORKBENCH_WINDOW_NAME,
			ISources.ACTIVE_WORKBENCH_WINDOW_SHELL_NAME };

	/**
	 * The display on which this provider is working.
	 */
	private final Display display;

	/**
	 * The last shell seen as active by this provider. This value may be
	 * <code>null</code> if the last call to
	 * <code>Display.getActiveShell()</code> returned <code>null</code>.
	 */
	private Shell lastActiveShell = null;

	/**
	 * The last workbench window shell seen as active by this provider. This
	 * value may be <code>null</code> if the last call to
	 * <code>workbench.getActiveWorkbenchWindow()</code> returned
	 * <code>null</code>.
	 */
	private Shell lastActiveWorkbenchWindowShell = null;

	/**
	 * The listener to shell activations on the display.
	 */
	private final Listener listener = new Listener() {
		/**
		 * Notifies all listeners that the source has changed.
		 */
		public final void handleEvent(final Event event) {
			if (!(event.widget instanceof Shell)) {
				return;
			}

			final Map currentState = getCurrentState();
			final Shell newActiveShell = (Shell) currentState
					.get(ISources.ACTIVE_SHELL_NAME);
			final IWorkbenchWindow newActiveWorkbenchWindow = (IWorkbenchWindow) currentState
					.get(ISources.ACTIVE_WORKBENCH_WINDOW_NAME);
			final Shell newActiveWorkbenchWindowShell = (Shell) currentState
					.get(ISources.ACTIVE_WORKBENCH_WINDOW_SHELL_NAME);

			// Figure out which variables have changed.
			final boolean shellChanged = newActiveShell != lastActiveShell;
			final boolean windowChanged = newActiveWorkbenchWindowShell != lastActiveWorkbenchWindowShell;

			// Fire an event for those sources that have changed.
			if (shellChanged && windowChanged) {
				final Map sourceValuesByName = new HashMap(5);
				sourceValuesByName.put(ISources.ACTIVE_SHELL_NAME,
						newActiveShell);
				sourceValuesByName.put(ISources.ACTIVE_WORKBENCH_WINDOW_NAME,
						newActiveWorkbenchWindow);
				sourceValuesByName.put(
						ISources.ACTIVE_WORKBENCH_WINDOW_SHELL_NAME,
						newActiveWorkbenchWindowShell);

				if (DEBUG) {
					logDebuggingInfo("Active shell changed to " //$NON-NLS-1$
							+ newActiveShell);
					logDebuggingInfo("Active workbench window changed to " //$NON-NLS-1$
							+ newActiveWorkbenchWindow);
					logDebuggingInfo("Active workbench window shell changed to " //$NON-NLS-1$
							+ newActiveWorkbenchWindowShell);
				}

				fireSourceChanged(ISources.ACTIVE_SHELL
 ISources.ACTIVE_WORKBENCH_WINDOW, sourceValuesByName);

			} else if (shellChanged) {
				if (DEBUG) {
					logDebuggingInfo("Active shell changed to " //$NON-NLS-1$
							+ newActiveShell);
				}
				fireSourceChanged(ISources.ACTIVE_SHELL,
						ISources.ACTIVE_SHELL_NAME, newActiveShell);

			} else if (windowChanged) {
				final Map sourceValuesByName = new HashMap(4);
				sourceValuesByName.put(ISources.ACTIVE_WORKBENCH_WINDOW_NAME,
						newActiveWorkbenchWindow);
				sourceValuesByName.put(
						ISources.ACTIVE_WORKBENCH_WINDOW_SHELL_NAME,
						newActiveWorkbenchWindowShell);

				if (DEBUG) {
					logDebuggingInfo("Active workbench window changed to " //$NON-NLS-1$
							+ newActiveWorkbenchWindow);
					logDebuggingInfo("Active workbench window shell changed to " //$NON-NLS-1$
							+ newActiveWorkbenchWindowShell);
				}

				fireSourceChanged(ISources.ACTIVE_WORKBENCH_WINDOW
 ISources.ACTIVE_WORKBENCH_WINDOW_SHELL,
						sourceValuesByName);

			}

			// Update the member variables.
			lastActiveShell = newActiveShell;
			lastActiveWorkbenchWindowShell = newActiveWorkbenchWindowShell;
		}
	};

	/**
	 * The workbench on which to work; never <code>null</code>.
	 */
	private final IWorkbench workbench;

	/**
	 * Constructs a new instance of <code>ShellSourceProvider</code>.
	 * 
	 * @param workbench
	 *            The workbench on which to monitor shell activations; must not
	 *            be <code>null</code>.
	 */
	public ActiveShellSourceProvider(final IWorkbench workbench) {
		this.workbench = workbench;
		this.display = workbench.getDisplay();
		this.display.addFilter(SWT.Activate, listener);
	}

	public final void dispose() {
		display.removeFilter(SWT.Activate, listener);
	}

	public final Map getCurrentState() {
		final Map currentState = new HashMap(4);

		final Shell newActiveShell = display.getActiveShell();
		currentState.put(ISources.ACTIVE_SHELL_NAME, newActiveShell);

		/*
		 * We will fallback to the workbench window, but only if a dialog is not
		 * open.
		 */
		final IContextService contextService = (IContextService) workbench
				.getAdapter(IContextService.class);
		final int shellType = contextService.getShellType(newActiveShell);
		if (shellType != IContextService.TYPE_DIALOG) {
			final IWorkbenchWindow newActiveWorkbenchWindow = workbench
					.getActiveWorkbenchWindow();
			final Shell newActiveWorkbenchWindowShell;
			if (newActiveWorkbenchWindow == null) {
				newActiveWorkbenchWindowShell = null;
			} else {
				newActiveWorkbenchWindowShell = newActiveWorkbenchWindow
						.getShell();
			}
			currentState.put(ISources.ACTIVE_WORKBENCH_WINDOW_NAME,
					newActiveWorkbenchWindow);
			currentState.put(ISources.ACTIVE_WORKBENCH_WINDOW_SHELL_NAME,
					newActiveWorkbenchWindowShell);
		}

		return currentState;
	}

	public final String[] getProvidedSourceNames() {
		return PROVIDED_SOURCE_NAMES;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3504.java