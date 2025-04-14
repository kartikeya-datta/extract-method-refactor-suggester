error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4935.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4935.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4935.java
text:
```scala
S@@ystem.out.print('\'');

/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ui.internal.commands.ILegacyAttributeNames;
import org.eclipse.ui.internal.misc.Policy;

/**
 * A handler that wraps a legacy handler. This provide backward compatibility
 * with the handlers release in Eclipse 3.0.
 * 
 * @since 3.1
 */
public final class LegacyHandlerWrapper implements IHandler {

	/**
	 * This flag can be set to <code>true</code> if commands should print
	 * information to <code>System.out</code> when changing handlers.
	 */
	private static final boolean DEBUG_HANDLERS = Policy.DEBUG_HANDLERS
			&& Policy.DEBUG_HANDLERS_VERBOSE;

	/**
	 * The wrapped handler; never <code>null</code>.
	 */
	private final org.eclipse.ui.commands.IHandler handler;

	/**
	 * Constructs a new instance of <code>HandlerWrapper</code>.
	 * 
	 * @param handler
	 *            The handler that should be wrapped; must not be
	 *            <code>null</code>.
	 */
	public LegacyHandlerWrapper(final org.eclipse.ui.commands.IHandler handler) {
		if (handler == null) {
			throw new NullPointerException(
					"A handler wrapper cannot be constructed on a null handler"); //$NON-NLS-1$
		}

		this.handler = handler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#addHandlerListener(org.eclipse.core.commands.IHandlerListener)
	 */
	public final void addHandlerListener(final IHandlerListener handlerListener) {
		handler.addHandlerListener(new LegacyHandlerListenerWrapper(this,
				handlerListener));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#dispose()
	 */
	public final void dispose() {
		handler.dispose();
	}

	public final boolean equals(final Object object) {
		if (object instanceof org.eclipse.ui.commands.IHandler) {
			return this.handler == object;
		}

		if (object instanceof LegacyHandlerWrapper) {
			return this.handler == ((LegacyHandlerWrapper) object).handler;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public final Object execute(final ExecutionEvent event)
			throws ExecutionException {
		// Debugging output
		if (DEBUG_HANDLERS) {
			System.out
					.print("HANDLERS >>> Executing LegacyHandlerWrapper for "); //$NON-NLS-1$
			if (handler == null) {
				System.out.println("no handler"); //$NON-NLS-1$
			} else {
				System.out.print('\''); //$NON-NLS-1$
				System.out.print(handler.getClass().getName());
				System.out.println('\'');
			}
		}
		
		try {
			return handler.execute(event.getParameters());
		} catch (final org.eclipse.ui.commands.ExecutionException e) {
			throw new ExecutionException(e.getMessage(), e.getCause());
		}
	}
	
	/**
	 * TODO Remove this method
	 */
	public final org.eclipse.ui.commands.IHandler getWrappedHandler() {
		return handler;
	}

	public final int hashCode() {
		return this.handler.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#isEnabled()
	 */
	public final boolean isEnabled() {
		final Object enabled = handler.getAttributeValuesByName().get(
				ILegacyAttributeNames.ENABLED);
		if (enabled instanceof Boolean) {
			return ((Boolean) enabled).booleanValue();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#isHandled()
	 */
	public final boolean isHandled() {
		final Object handled = handler.getAttributeValuesByName().get(
				ILegacyAttributeNames.HANDLED);
		if (handled instanceof Boolean) {
			return ((Boolean) handled).booleanValue();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#removeHandlerListener(org.eclipse.core.commands.IHandlerListener)
	 */
	public final void removeHandlerListener(
			final IHandlerListener handlerListener) {
		handler.removeHandlerListener(new LegacyHandlerListenerWrapper(this,
				handlerListener));
	}
	
	public final String toString() {
		final StringBuffer buffer = new StringBuffer();

		buffer.append("LegacyHandlerWrapper(handler="); //$NON-NLS-1$
		buffer.append(handler);
		buffer.append(')');

		return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4935.java