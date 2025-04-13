error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2420.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2420.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2420.java
text:
```scala
private final C@@ommandManagerLegacyWrapper commandManagerWrapper;

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
package org.eclipse.ui.internal.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.CommandManager;
import org.eclipse.core.commands.contexts.ContextManager;
import org.eclipse.jface.bindings.BindingManager;
import org.eclipse.ui.ISources;
import org.eclipse.ui.LegacyHandlerSubmissionExpression;
import org.eclipse.ui.commands.HandlerSubmission;
import org.eclipse.ui.commands.ICommandManager;
import org.eclipse.ui.commands.IWorkbenchCommandSupport;
import org.eclipse.ui.commands.Priority;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.handlers.LegacyHandlerWrapper;

/**
 * Provides command support in terms of the workbench.
 * 
 * @since 3.0
 */
public class WorkbenchCommandSupport implements IWorkbenchCommandSupport {

	/**
	 * The map of activations that have been given to the handler service (<code>IHandlerActivation</code>),
	 * indexed by the submissions (<code>HandlerSubmission</code>). This map
	 * should be <code>null</code> if there are no such activations.
	 */
	private Map activationsBySubmission = null;

	/**
	 * The mutable command manager that should be notified of changes to the
	 * list of active handlers. This value is never <code>null</code>.
	 */
	private final CommandManagerWrapper commandManagerWrapper;

	/**
	 * The handler service for the workbench. This value is never
	 * <code>null</code>.
	 */
	private final IHandlerService handlerService;

	/**
	 * Constructs a new instance of <code>WorkbenchCommandSupport</code>
	 * 
	 * @param bindingManager
	 *            The binding manager providing support for the command manager;
	 *            must not be <code>null</code>.
	 * @param commandManager
	 *            The command manager for the workbench; must not be
	 *            <code>null</code>.
	 * @param contextManager
	 *            The context manager providing support for the command manager
	 *            and binding manager; must not be <code>null</code>.
	 * @param handlerService
	 *            The handler service for the workbench; must not be
	 *            <code>null</code>.
	 */
	public WorkbenchCommandSupport(final BindingManager bindingManager,
			final CommandManager commandManager,
			final ContextManager contextManager,
			final IHandlerService handlerService) {
		if (handlerService == null) {
			throw new NullPointerException("The handler service cannot be null"); //$NON-NLS-1$
		}

		this.handlerService = handlerService;

		commandManagerWrapper = CommandManagerFactory.getCommandManagerWrapper(
				bindingManager, commandManager, contextManager);

		// Initialize the old key formatter settings.
		org.eclipse.ui.keys.KeyFormatterFactory
				.setDefault(org.eclipse.ui.keys.SWTKeySupport
						.getKeyFormatterForPlatform());
	}

	public final void addHandlerSubmission(
			final HandlerSubmission handlerSubmission) {
		/*
		 * Create the source priorities based on the conditions mentioned in the
		 * submission.
		 */
		int sourcePriorities = 0;
		if (handlerSubmission.getActivePartId() != null) {
			sourcePriorities |= ISources.ACTIVE_PART;
		}
		if (handlerSubmission.getActiveShell() != null) {
			sourcePriorities |= (ISources.ACTIVE_SHELL | ISources.ACTIVE_WORKBENCH_WINDOW);
		}
		if (handlerSubmission.getActiveWorkbenchPartSite() != null) {
			sourcePriorities |= ISources.ACTIVE_SITE;
		}
		if (handlerSubmission.getPriority() == Priority.LEGACY) {
			sourcePriorities |= ISources.LEGACY_LEGACY;
		} else if (handlerSubmission.getPriority() == Priority.LOW) {
			sourcePriorities |= ISources.LEGACY_LOW;
		} else if (handlerSubmission.getPriority() == Priority.MEDIUM) {
			sourcePriorities |= ISources.LEGACY_MEDIUM;
		}

		final IHandlerActivation activation = handlerService.activateHandler(
				handlerSubmission.getCommandId(), new LegacyHandlerWrapper(
						handlerSubmission.getHandler()),
				new LegacyHandlerSubmissionExpression(handlerSubmission
						.getActivePartId(), handlerSubmission.getActiveShell(),
						handlerSubmission.getActiveWorkbenchPartSite()));
		if (activationsBySubmission == null) {
			activationsBySubmission = new HashMap();
		}
		activationsBySubmission.put(handlerSubmission, activation);
	}

	public final void addHandlerSubmissions(final Collection handlerSubmissions) {
		final Iterator submissionItr = handlerSubmissions.iterator();
		while (submissionItr.hasNext()) {
			addHandlerSubmission((HandlerSubmission) submissionItr.next());
		}
	}

	public ICommandManager getCommandManager() {
		return commandManagerWrapper;
	}

	public final void removeHandlerSubmission(
			final HandlerSubmission handlerSubmission) {
		if (activationsBySubmission == null) {
			return;
		}

		final Object value = activationsBySubmission.remove(handlerSubmission);
		if (value instanceof IHandlerActivation) {
			final IHandlerActivation activation = (IHandlerActivation) value;
			handlerService.deactivateHandler(activation);
		}
	}

	public final void removeHandlerSubmissions(
			final Collection handlerSubmissions) {
		final Iterator submissionItr = handlerSubmissions.iterator();
		while (submissionItr.hasNext()) {
			removeHandlerSubmission((HandlerSubmission) submissionItr.next());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2420.java