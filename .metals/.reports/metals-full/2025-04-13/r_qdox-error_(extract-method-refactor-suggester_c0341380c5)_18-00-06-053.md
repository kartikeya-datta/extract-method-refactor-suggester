error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/269.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/269.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/269.java
text:
```scala
private C@@ontextManagerLegacyWrapper contextManagerWrapper;

/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.contexts;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.contexts.ContextManager;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISources;
import org.eclipse.ui.LegacyHandlerSubmissionExpression;
import org.eclipse.ui.contexts.EnabledSubmission;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextManager;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.contexts.IWorkbenchContextSupport;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.keys.IBindingService;

/**
 * Provides support for contexts within the workbench -- including key bindings,
 * and some default contexts for shell types.
 * 
 * @since 3.0
 */
public class WorkbenchContextSupport implements IWorkbenchContextSupport {

	/**
	 * The map of activations that have been given to the handler service (<code>IHandlerActivation</code>),
	 * indexed by the submissions (<code>HandlerSubmission</code>). This map
	 * should be <code>null</code> if there are no such activations.
	 */
	private Map activationsBySubmission = null;

	/**
	 * The binding service for the workbench. This value is never
	 * <code>null</code>.
	 */
	private IBindingService bindingService;

	/**
	 * The context service for the workbench. This value is never
	 * <code>null</code>.
	 */
	private IContextService contextService;

	/**
	 * The legacy context manager supported by this application.
	 */
	private ContextManagerWrapper contextManagerWrapper;

	/**
	 * The workbench for which context support is being provided. This value
	 * must not be <code>null</code>.
	 */
	private final Workbench workbench;

	/**
	 * Constructs a new instance of <code>WorkbenchCommandSupport</code>.
	 * This attaches the key binding support, and adds a global shell activation
	 * filter.
	 * 
	 * @param workbenchToSupport
	 *            The workbench that needs to be supported by this instance;
	 *            must not be <code>null</code>.
	 * @param contextManager
	 *            The context manager to be wrappered; must not be
	 *            <code>null</code>.
	 */
	public WorkbenchContextSupport(final Workbench workbenchToSupport,
			final ContextManager contextManager) {
		workbench = workbenchToSupport;
		contextService = (IContextService) workbench
				.getAdapter(IContextService.class);
		bindingService = (IBindingService) workbench
				.getAdapter(IBindingService.class);
		contextManagerWrapper = ContextManagerFactory
				.getContextManagerWrapper(contextManager);
	}

	public final void addEnabledSubmission(
			final EnabledSubmission enabledSubmission) {
		/*
		 * Create the source priorities based on the conditions mentioned in the
		 * submission.
		 */
		int sourcePriorities = 0;
		if (enabledSubmission.getActivePartId() != null) {
			sourcePriorities |= ISources.ACTIVE_PART;
		}
		if (enabledSubmission.getActiveShell() != null) {
			sourcePriorities |= (ISources.ACTIVE_SHELL | ISources.ACTIVE_WORKBENCH_WINDOW);
		}
		if (enabledSubmission.getActiveWorkbenchPartSite() != null) {
			sourcePriorities |= ISources.ACTIVE_SITE;
		}

		final IContextActivation activation = contextService.activateContext(
				enabledSubmission.getContextId(),
				new LegacyHandlerSubmissionExpression(enabledSubmission
						.getActivePartId(), enabledSubmission.getActiveShell(),
						enabledSubmission.getActiveWorkbenchPartSite()));
		if (activationsBySubmission == null) {
			activationsBySubmission = new HashMap();
		}
		activationsBySubmission.put(enabledSubmission, activation);
	}

	public final void addEnabledSubmissions(final Collection enabledSubmissions) {
		final Iterator submissionItr = enabledSubmissions.iterator();
		while (submissionItr.hasNext()) {
			addEnabledSubmission((EnabledSubmission) submissionItr.next());
		}
	}

	public final IContextManager getContextManager() {
		return contextManagerWrapper;
	}

	public final int getShellType(Shell shell) {
		return contextService.getShellType(shell);
	}

	public final boolean isKeyFilterEnabled() {
		return bindingService.isKeyFilterEnabled();
	}

	public final void openKeyAssistDialog() {
		bindingService.openKeyAssistDialog();
	}

	public final boolean registerShell(final Shell shell, final int type) {
		return contextService.registerShell(shell, type);
	}

	public final void removeEnabledSubmission(
			final EnabledSubmission enabledSubmission) {
		if (activationsBySubmission == null) {
			return;
		}

		final Object value = activationsBySubmission.remove(enabledSubmission);
		if (value instanceof IContextActivation) {
			final IContextActivation activation = (IContextActivation) value;
			contextService.deactivateContext(activation);
		}
	}

	public final void removeEnabledSubmissions(
			final Collection enabledSubmissions) {
		final Iterator submissionItr = enabledSubmissions.iterator();
		while (submissionItr.hasNext()) {
			removeEnabledSubmission((EnabledSubmission) submissionItr.next());
		}
	}

	public final void setKeyFilterEnabled(final boolean enabled) {
		bindingService.setKeyFilterEnabled(enabled);
	}

	public final boolean unregisterShell(final Shell shell) {
		return contextService.unregisterShell(shell);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/269.java