error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8966.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8966.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8966.java
text:
```scala
r@@eturn command.execute(new ExecutionEvent(command,

/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.jface.bindings.BindingManager;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.ui.commands.ExecutionException;
import org.eclipse.ui.commands.ICommand;
import org.eclipse.ui.commands.ICommandListener;
import org.eclipse.ui.commands.NotDefinedException;
import org.eclipse.ui.commands.NotHandledException;
import org.eclipse.ui.internal.keys.KeySequenceBinding;
import org.eclipse.ui.keys.KeySequence;

/**
 * A wrapper around a core command so that it satisfies the deprecated
 * <code>ICommand</code> interface.
 * 
 * @since 3.1
 */
final class CommandLegacyWrapper implements ICommand {

	/**
	 * The supporting binding manager; never <code>null</code>.
	 */
	private final BindingManager bindingManager;

	/**
	 * The wrapped command; never <code>null</code>.
	 */
	private final Command command;

	/**
	 * A parameterized representation of the command. This is created lazily. If
	 * it has not yet been created, it is <code>null</code>.
	 */
	private ParameterizedCommand parameterizedCommand;

	/**
	 * Constructs a new <code>CommandWrapper</code>
	 * 
	 * @param command
	 *            The command to be wrapped; must not be <code>null</code>.
	 * @param bindingManager
	 *            The binding manager to support this wrapper; must not be
	 *            <code>null</code>.
	 */
	CommandLegacyWrapper(final Command command,
			final BindingManager bindingManager) {
		if (command == null) {
			throw new NullPointerException(
					"The wrapped command cannot be <code>null</code>."); //$NON-NLS-1$
		}

		if (bindingManager == null) {
			throw new NullPointerException(
					"A binding manager is required to wrap a command"); //$NON-NLS-1$
		}

		this.command = command;
		this.bindingManager = bindingManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.ICommand#addCommandListener(org.eclipse.ui.commands.ICommandListener)
	 */

	public final void addCommandListener(final ICommandListener commandListener) {
		command.addCommandListener(new LegacyCommandListenerWrapper(
				commandListener, bindingManager));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.ICommand#execute(java.util.Map)
	 */
	public final Object execute(Map parameterValuesByName)
			throws ExecutionException, NotHandledException {
		try {
			return command.execute(new ExecutionEvent(
					(parameterValuesByName == null) ? Collections.EMPTY_MAP
							: parameterValuesByName, null, null));
		} catch (final org.eclipse.core.commands.ExecutionException e) {
			throw new ExecutionException(e);
		} catch (final org.eclipse.core.commands.NotHandledException e) {
			throw new NotHandledException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.ICommand#getAttributeValuesByName()
	 */
	public final Map getAttributeValuesByName() {
		final Map attributeValues = new HashMap();
		// avoid using Boolean.valueOf to allow compilation against JCL
		// Foundation (bug 80053)
		attributeValues.put(ILegacyAttributeNames.ENABLED,
				command.isEnabled() ? Boolean.TRUE : Boolean.FALSE);
		attributeValues.put(ILegacyAttributeNames.HANDLED,
				command.isHandled() ? Boolean.TRUE : Boolean.FALSE);
		return attributeValues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.ICommand#getCategoryId()
	 */
	public final String getCategoryId() throws NotDefinedException {
		try {
			return command.getCategory().getId();
		} catch (final org.eclipse.core.commands.common.NotDefinedException e) {
			throw new NotDefinedException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.ICommand#getDescription()
	 */
	public final String getDescription() throws NotDefinedException {
		try {
			return command.getDescription();
		} catch (final org.eclipse.core.commands.common.NotDefinedException e) {
			throw new NotDefinedException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.ICommand#getId()
	 */
	public final String getId() {
		return command.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.ICommand#getKeySequenceBindings()
	 */
	public final List getKeySequenceBindings() {
		final List legacyBindings = new ArrayList();
		if (parameterizedCommand == null) {
			parameterizedCommand = new ParameterizedCommand(command, null);
		}
		final TriggerSequence[] activeBindings = bindingManager
				.getActiveBindingsFor(parameterizedCommand);
		final int activeBindingsCount = activeBindings.length;
		for (int i = 0; i < activeBindingsCount; i++) {
			final TriggerSequence triggerSequence = activeBindings[i];
			if (triggerSequence instanceof org.eclipse.jface.bindings.keys.KeySequence) {
				legacyBindings
						.add(new KeySequenceBinding(
								KeySequence
										.getInstance((org.eclipse.jface.bindings.keys.KeySequence) triggerSequence),
								0));
			}
		}

		return legacyBindings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.ICommand#getName()
	 */
	public final String getName() throws NotDefinedException {
		try {
			return command.getName();
		} catch (final org.eclipse.core.commands.common.NotDefinedException e) {
			throw new NotDefinedException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.ICommand#isDefined()
	 */
	public final boolean isDefined() {
		return command.isDefined();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.ICommand#isHandled()
	 */
	public final boolean isHandled() {
		return command.isHandled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.ICommand#removeCommandListener(org.eclipse.ui.commands.ICommandListener)
	 */
	public final void removeCommandListener(
			final ICommandListener commandListener) {
		command.removeCommandListener(new LegacyCommandListenerWrapper(
				commandListener, bindingManager));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public final int compareTo(final Object o) {
		return command.compareTo(o);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8966.java