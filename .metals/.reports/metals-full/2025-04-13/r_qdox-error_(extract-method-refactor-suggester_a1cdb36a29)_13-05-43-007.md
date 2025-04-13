error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6244.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6244.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6244.java
text:
```scala
c@@ontextId, expression, global);

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

package org.eclipse.ui.internal.contexts;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.contexts.Context;
import org.eclipse.core.commands.contexts.IContextManagerListener;
import org.eclipse.core.expressions.Expression;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISourceProvider;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.internal.expressions.AndExpression;

/**
 * A context service which delegates almost all responsibility to the parent
 * service.
 * <p>
 * This class is not intended for use outside of the
 * <code>org.eclipse.ui.workbench</code> plug-in.
 * </p>
 * <p>
 * <strong>EXPERIMENTAL</strong>. This class or interface has been added as
 * part of a work in progress. There is a guarantee neither that this API will
 * work nor that it will remain the same. Please do not use this API without
 * consulting with the Platform/UI team.
 * </p>
 * 
 * @since 3.2
 * 
 */
public class SlaveContextService implements IContextService {

	/**
	 * The parent context service, which is never <code>null</code>.
	 */
	protected IContextService fParentService;

	/**
	 * The default expression used when {@link #activateContext(String) } is
	 * called. Contexts contributed that use this expression will only be active
	 * with this service is active.
	 */
	protected Expression fDefaultExpression;

	/**
	 * Our contexts that are currently active with the parent context service.
	 */
	protected Set fParentActivations;

	/**
	 * A map of the local activation to the parent activations. If this service
	 * is inactive, then all parent activations are <code>null</code>.
	 * Otherwise, they point to the corresponding activation in the parent
	 * service.
	 */
	protected Map fLocalActivations;

	/**
	 * Construct the new slave.
	 * 
	 * @param parentService
	 *            the parent context service; must not be <code>null</code>.
	 * @param defaultExpression
	 *            A default expression to use to determine viability. It's
	 *            mainly used for conflict resolution. It can be
	 *            <code>null</code>.
	 */
	public SlaveContextService(IContextService parentService,
			Expression defaultExpression) {
		if (parentService == null) {
			throw new NullPointerException(
					"The parent context service must not be null"); //$NON-NLS-1$
		}
		fParentService = parentService;
		fDefaultExpression = defaultExpression;
		fParentActivations = new HashSet();
		fLocalActivations = new HashMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.contexts.IContextService#activateContext(java.lang.String)
	 */
	public IContextActivation activateContext(String contextId) {
		ContextActivation activation = new ContextActivation(contextId,
				fDefaultExpression, this);
		return doActivateContext(activation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.contexts.IContextService#activateContext(java.lang.String,
	 *      org.eclipse.core.expressions.Expression)
	 */
	public IContextActivation activateContext(String contextId,
			Expression expression) {
		return activateContext(contextId, expression, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.contexts.IContextService#activateContext(java.lang.String,
	 *      org.eclipse.core.expressions.Expression, boolean)
	 */
	public IContextActivation activateContext(String contextId,
			Expression expression, boolean global) {
		if (global) {
			IContextActivation activation = fParentService.activateContext(
					contextId, expression);
			fParentActivations.add(activation);
			return activation;
		}
		AndExpression andExpression = null;
		if (expression instanceof AndExpression) {
			andExpression = (AndExpression) expression;
		} else {
			andExpression = new AndExpression();
			andExpression.add(expression);
		}
		andExpression.add(fDefaultExpression);
		ContextActivation activation = new ContextActivation(contextId,
				andExpression, this);
		return doActivateContext(activation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.contexts.IContextService#activateContext(java.lang.String,
	 *      org.eclipse.core.expressions.Expression, int)
	 */
	public IContextActivation activateContext(String contextId,
			Expression expression, int sourcePriorities) {
		return activateContext(contextId, expression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.contexts.IContextService#addContextManagerListener(org.eclipse.core.commands.contexts.IContextManagerListener)
	 */
	public void addContextManagerListener(IContextManagerListener listener) {
		fParentService.addContextManagerListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.services.IServiceWithSources#addSourceProvider(org.eclipse.ui.ISourceProvider)
	 */
	public void addSourceProvider(ISourceProvider provider) {
		fParentService.addSourceProvider(provider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.contexts.IContextService#deactivateContext(org.eclipse.ui.contexts.IContextActivation)
	 */
	public void deactivateContext(IContextActivation activation) {
		IContextActivation parentActivation = null;
		if (fLocalActivations.containsKey(activation)) {
			parentActivation = (IContextActivation) fLocalActivations
					.remove(activation);
		} else {
			parentActivation = activation;
		}
		if (parentActivation != null) {
			fParentService.deactivateContext(parentActivation);
			fParentActivations.remove(parentActivation);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.contexts.IContextService#deactivateContexts(java.util.Collection)
	 */
	public void deactivateContexts(Collection activations) {
		Object[] array = activations.toArray();
		for (int i = 0; i < array.length; i++) {
			deactivateContext((IContextActivation) array[i]);
			array[i] = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.services.IDisposable#dispose()
	 */
	public void dispose() {
		fParentService.deactivateContexts(fParentActivations);
		fParentActivations.clear();
		fLocalActivations.clear();
	}

	/**
	 * Activate the context with respect to this slave service.
	 * 
	 * @param contextId
	 *            the context id
	 * @param expression
	 *            the expression to use
	 * @return the activated context
	 */
	protected IContextActivation doActivateContext(IContextActivation activation) {
		IContextActivation parentActivation = fParentService.activateContext(
				activation.getContextId(), activation.getExpression());
		fParentActivations.add(parentActivation);
		fLocalActivations.put(activation, parentActivation);
		return activation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.contexts.IContextService#getActiveContextIds()
	 */
	public Collection getActiveContextIds() {
		return fParentService.getActiveContextIds();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.contexts.IContextService#getContext(java.lang.String)
	 */
	public Context getContext(String contextId) {
		return fParentService.getContext(contextId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.contexts.IContextService#getDefinedContextIds()
	 */
	public Collection getDefinedContextIds() {
		return fParentService.getDefinedContextIds();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.contexts.IContextService#getDefinedContexts()
	 */
	public Context[] getDefinedContexts() {
		return fParentService.getDefinedContexts();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.contexts.IContextService#getShellType(org.eclipse.swt.widgets.Shell)
	 */
	public int getShellType(Shell shell) {
		return fParentService.getShellType(shell);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.contexts.IContextService#readRegistry()
	 */
	public void readRegistry() {
		fParentService.readRegistry();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.contexts.IContextService#registerShell(org.eclipse.swt.widgets.Shell,
	 *      int)
	 */
	public boolean registerShell(Shell shell, int type) {
		return fParentService.registerShell(shell, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.contexts.IContextService#removeContextManagerListener(org.eclipse.core.commands.contexts.IContextManagerListener)
	 */
	public void removeContextManagerListener(IContextManagerListener listener) {
		fParentService.removeContextManagerListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.services.IServiceWithSources#removeSourceProvider(org.eclipse.ui.ISourceProvider)
	 */
	public void removeSourceProvider(ISourceProvider provider) {
		fParentService.removeSourceProvider(provider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.contexts.IContextService#unregisterShell(org.eclipse.swt.widgets.Shell)
	 */
	public boolean unregisterShell(Shell shell) {
		return fParentService.unregisterShell(shell);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6244.java