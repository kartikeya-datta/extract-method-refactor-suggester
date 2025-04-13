error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5636.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5636.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5636.java
text:
```scala
i@@f (object instanceof CommandListenerWrapper) {

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

import org.eclipse.core.commands.CommandEvent;
import org.eclipse.core.commands.ICommandListener;
import org.eclipse.jface.bindings.BindingManager;
import org.eclipse.ui.commands.ICommand;

/**
 * Wraps a legacy listener in a new listener interface. This simply forwards
 * incoming events through to the old interface.
 * 
 * @since 3.1
 */
final class CommandListenerWrapper implements ICommandListener {

	/**
	 * The supporting binding manager; never <code>null</code>.
	 */
	private final BindingManager bindingManager;

	/**
	 * The listener which is being wrapped. This value should never be
	 * <code>null</code>.
	 */
	private final org.eclipse.ui.commands.ICommandListener listener;

	/**
	 * Constructs a new instance of <code>CommandListenerWrapper</code> around
	 * a legacy listener.
	 * 
	 * @param listener
	 *            The listener to be wrapped; must not be <code>null</code>.
	 */
	CommandListenerWrapper(
			final org.eclipse.ui.commands.ICommandListener listener,
			final BindingManager bindingManager) {
		if (listener == null) {
			throw new NullPointerException("Cannot wrap a null listener."); //$NON-NLS-1$
		}

		if (bindingManager == null) {
			throw new NullPointerException(
					"Cannot create a listener wrapper without a binding manager"); //$NON-NLS-1$
		}

		this.listener = listener;
		this.bindingManager = bindingManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.commands.ICommandListener#commandChanged(org.eclipse.commands.CommandEvent)
	 */
	public final void commandChanged(final CommandEvent commandEvent) {
		final ICommand command = new CommandWrapper(commandEvent.getCommand(),
				bindingManager);
		final boolean definedChanged = commandEvent.isDefinedChanged();
		final boolean descriptionChanged = commandEvent.isDescriptionChanged();
		final boolean handledChanged = commandEvent.isHandledChanged();
		final boolean nameChanged = commandEvent.isNameChanged();

		listener.commandChanged(new org.eclipse.ui.commands.CommandEvent(
				command, false, false, definedChanged, descriptionChanged,
				handledChanged, false, nameChanged, null));

	}

	public final boolean equals(final Object object) {
		if (object instanceof SchemeListenerWrapper) {
			final CommandListenerWrapper wrapper = (CommandListenerWrapper) object;
			return listener.equals(wrapper.listener);
		}

		if (object instanceof org.eclipse.ui.commands.ICommandListener) {
			final org.eclipse.ui.commands.ICommandListener other = (org.eclipse.ui.commands.ICommandListener) object;
			return listener.equals(other);
		}

		return false;
	}

	public final int hashCode() {
		return listener.hashCode();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5636.java