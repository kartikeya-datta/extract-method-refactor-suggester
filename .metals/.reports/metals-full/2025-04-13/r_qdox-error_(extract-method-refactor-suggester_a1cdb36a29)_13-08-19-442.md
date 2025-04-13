error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/949.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/949.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/949.java
text:
```scala
final M@@ap currentState = new HashMap(7);

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

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.util.Util;

/**
 * Provides notifications when the active part changes.
 * 
 * @since 3.1
 */
public class ActivePartSourceProvider extends AbstractSourceProvider {

	/**
	 * The last active editor part seen as active by this provider. This value
	 * may be <code>null</code> if there is no currently active editor.
	 */
	private IEditorPart lastActiveEditor = null;

	/**
	 * The last active editor id seen as active by this provider. This value may
	 * be <code>null</code> if there is no currently active editor.
	 */
	private String lastActiveEditorId = null;

	/**
	 * The last active part seen as active by this provider. This value may be
	 * <code>null</code> if there is no currently active part.
	 */
	private IWorkbenchPart lastActivePart = null;

	/**
	 * The last active part id seen as active by this provider. This value may
	 * be <code>null</code> if there is no currently active part.
	 */
	private String lastActivePartId = null;

	/**
	 * The last active part site seen by this provider. This value may be
	 * <code>null</code> if there is no currently active site.
	 */
	private IWorkbenchPartSite lastActivePartSite = null;

	private final IPartListener partListener = new IPartListener() {

		public final void partActivated(final IWorkbenchPart part) {
			checkActivePart();
		}

		public final void partBroughtToTop(final IWorkbenchPart part) {
			checkActivePart();
		}

		public final void partClosed(final IWorkbenchPart part) {
			checkActivePart();
		}

		public final void partDeactivated(final IWorkbenchPart part) {
			checkActivePart();
		}

		public final void partOpened(final IWorkbenchPart part) {
			checkActivePart();
		}

	};

	private final IWindowListener windowListener = new IWindowListener() {

		public final void windowActivated(final IWorkbenchWindow window) {
			checkActivePart();
		}

		public final void windowClosed(final IWorkbenchWindow window) {
			if (window != null) {
				window.getPartService().removePartListener(partListener);
			}
			checkActivePart();
		}

		public final void windowDeactivated(final IWorkbenchWindow window) {
			checkActivePart();
		}

		public final void windowOpened(final IWorkbenchWindow window) {
			if (window != null) {
				window.getPartService().addPartListener(partListener);
			}
			checkActivePart();
		}

	};

	/**
	 * The workbench on which this source provider will act.
	 */
	private final IWorkbench workbench;

	/**
	 * Constructs a new instance of <code>ShellSourceProvider</code>.
	 * 
	 * @param workbench
	 *            The workbench on which to monitor shell activations; must not
	 *            be <code>null</code>.
	 */
	public ActivePartSourceProvider(final IWorkbench workbench) {
		this.workbench = workbench;
		workbench.addWindowListener(windowListener);
	}

	private final void checkActivePart() {
		final Map currentState = getCurrentState();
		int sources = 0;

		// Figure out what was changed.
		final Object newActivePart = currentState
				.get(ISources.ACTIVE_PART_NAME);
		if (!Util.equals(newActivePart, lastActivePart)) {
			sources |= ISources.ACTIVE_PART;
			lastActivePart = (IWorkbenchPart) newActivePart;
		}
		final Object newActivePartId = currentState
				.get(ISources.ACTIVE_PART_ID_NAME);
		if (!Util.equals(newActivePartId, lastActivePartId)) {
			sources |= ISources.ACTIVE_PART_ID;
			lastActivePartId = (String) newActivePartId;
		}
		final Object newActivePartSite = currentState
				.get(ISources.ACTIVE_SITE_NAME);
		if (!Util.equals(newActivePartSite, lastActivePartSite)) {
			sources |= ISources.ACTIVE_SITE;
			lastActivePartSite = (IWorkbenchPartSite) newActivePartSite;
		}
		final Object newActiveEditor = currentState
				.get(ISources.ACTIVE_EDITOR_NAME);
		if (!Util.equals(newActiveEditor, lastActiveEditor)) {
			sources |= ISources.ACTIVE_EDITOR;
			lastActiveEditor = (IEditorPart) newActiveEditor;
		}
		final Object newActiveEditorId = currentState
				.get(ISources.ACTIVE_EDITOR_ID_NAME);
		if (!Util.equals(newActiveEditorId, lastActiveEditorId)) {
			sources |= ISources.ACTIVE_EDITOR_ID;
			lastActiveEditorId = (String) newActiveEditorId;
		}

		// Fire the event, if something has changed.
		if (sources != 0) {
			if (DEBUG) {
				if ((sources & ISources.ACTIVE_PART) != 0) {
					logDebuggingInfo("Active part changed to " //$NON-NLS-1$
							+ lastActivePart);
				}
				if ((sources & ISources.ACTIVE_PART_ID) != 0) {
					logDebuggingInfo("Active part id changed to " //$NON-NLS-1$
							+ lastActivePartId);
				}
				if ((sources & ISources.ACTIVE_SITE) != 0) {
					logDebuggingInfo("Active site changed to " //$NON-NLS-1$
							+ lastActivePartSite);
				}
				if ((sources & ISources.ACTIVE_EDITOR) != 0) {
					logDebuggingInfo("Active editor changed to " //$NON-NLS-1$
							+ lastActiveEditor);
				}
				if ((sources & ISources.ACTIVE_EDITOR_ID) != 0) {
					logDebuggingInfo("Active editor id changed to " //$NON-NLS-1$
							+ lastActiveEditorId);
				}
			}
			fireSourceChanged(sources, currentState);
		}
	}

	public final void dispose() {
		workbench.removeWindowListener(windowListener);
	}

	public final Map getCurrentState() {
		final Map currentState = new HashMap(4);
		currentState.put(ISources.ACTIVE_SITE_NAME, null);
		currentState.put(ISources.ACTIVE_PART_NAME, null);
		currentState.put(ISources.ACTIVE_PART_ID_NAME, null);
		currentState.put(ISources.ACTIVE_EDITOR_NAME, null);
		currentState.put(ISources.ACTIVE_EDITOR_ID_NAME, null);

		final IWorkbenchWindow activeWorkbenchWindow = workbench
				.getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null) {
			final IWorkbenchPage activeWorkbenchPage = activeWorkbenchWindow
					.getActivePage();
			if (activeWorkbenchPage != null) {
				// Check the active workbench part.
				final IWorkbenchPart newActivePart = activeWorkbenchPage
						.getActivePart();
				currentState.put(ISources.ACTIVE_PART_NAME, newActivePart);
				if (newActivePart != null) {
					final IWorkbenchPartSite activeWorkbenchPartSite = newActivePart
							.getSite();
					currentState.put(ISources.ACTIVE_SITE_NAME,
							activeWorkbenchPartSite);
					if (activeWorkbenchPartSite != null) {
						final String newActivePartId = activeWorkbenchPartSite
								.getId();
						currentState.put(ISources.ACTIVE_PART_ID_NAME,
								newActivePartId);
					}
				}

				// Check the active editor part.
				final IEditorPart newActiveEditor = activeWorkbenchPage
						.getActiveEditor();
				currentState.put(ISources.ACTIVE_EDITOR_NAME, newActiveEditor);
				if (newActiveEditor != null) {
					final IEditorSite activeEditorSite = newActiveEditor
							.getEditorSite();
					if (activeEditorSite != null) {
						final String newActiveEditorId = activeEditorSite
								.getId();
						currentState.put(ISources.ACTIVE_EDITOR_ID_NAME,
								newActiveEditorId);
					}
				}
			}
		}

		return currentState;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/949.java