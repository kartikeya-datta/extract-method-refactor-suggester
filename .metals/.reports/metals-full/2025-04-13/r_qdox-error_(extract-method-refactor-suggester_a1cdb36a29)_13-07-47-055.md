error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/363.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/363.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/363.java
text:
```scala
r@@eturn KeySupport.convertAcceleratorToKeyStroke(modifiers + key);

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.internal.commands;

import java.util.*;
import java.util.List;

import org.eclipse.jface.action.ContextResolver;
import org.eclipse.jface.action.IContextResolver;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.commands.*;
import org.eclipse.ui.commands.NotDefinedException;
import org.eclipse.ui.contexts.*;
import org.eclipse.ui.internal.*;
import org.eclipse.ui.internal.keys.*;
import org.eclipse.ui.keys.*;

/**
 * <p>
 * Manages the relationship between the context and command managers, as well as 
 * filtering all incoming key strokes before allowing them to reach the widget
 * hierarchy.  This is the magic glue that makes key bindings work, and keeps
 * everyone agreeing about the state of the active contexts.
 * </p>
 * <p>
 * If an incoming key matches one of the active key bindings, then it dispatches
 * the event to the appropriate handler.  Otherwise, the key is allowed to
 * propagate normally through the widget hierarchy.
 * </p>
 */
public final class ContextAndHandlerManager implements IContextResolver, Listener {

	/**
	 * A listener for changes to command definitions.  These changes can occur
	 * if a plugin is loaded, unloaded or reloaded (for example). 
	 */
	private final ICommandManagerListener commandManagerListener = new ICommandManagerListener() {
		public final void commandManagerChanged(final ICommandManagerEvent commandManagerEvent) {
			update();
		}
	};
	
	/** 
	 * A listener for changes to the contexts.  These are intended to be
	 * triggered by the application itself -- when such things as parts are
	 * activated and deactivated, and perspectives change. 
	 */
	private final IContextManagerListener contextManagerListener = new IContextManagerListener() {
		public final void contextManagerChanged(final IContextManagerEvent contextManagerEvent) {
			update();
		}
	};
	
	/** The status line contribution item. */
	private final StatusLineContributionItem statusLineContributionItem = new StatusLineContributionItem("ModeContributionItem"); //$NON-NLS-1$
	
	/** The workbench to which listeners should be added.  This will never be
	 * <code>null</code>.
	 */
	private final Workbench workbench; // TODO This should be IWorkbench.

	/**
	 * Constructs a new instance of <code>AcceleratorKeyListener</code>.
	 * 
	 * @param workbench The workbench on which the accelerator key listener
	 * should act; must not be <code>null</code>.
	 */
	public ContextAndHandlerManager(final IWorkbench workbench) {
		this.workbench = (Workbench) workbench; // TODO This should really be IWorkbench.
		final ICommandManager commandManager = this.workbench.getCommandManager();
		commandManager.addCommandManagerListener(commandManagerListener);
		final IContextManager contextManager = this.workbench.getContextManager();
		contextManager.addContextManagerListener(contextManagerListener);
		update();
	}

	/** 
	 * Clears the existing data, and performs an update.  This completely
	 * resets the accelerators.
	 */
	private final void clear() {
		CommandManager.getInstance().setMode(KeySequence.getInstance());
		statusLineContributionItem.setText(""); //$NON-NLS-1$ 
		update();
	}

	/**
	 * Called by the workbench when a window is about to close.  This simply
	 * removes the status line contribution item from the closing window. 
	 * @param window The window which is closing; must not be <code>null</code>.
	 */
	public final void deregisterWindow(final WorkbenchWindow window) {
		window.getStatusLineManager().remove(statusLineContributionItem);
	}

	/**
	 * Tries to dispatch the given key event to all appropriate handlers, if 
	 * any.  By dispatching, it simply tells the given action or command to
	 * execute itself.
	 * 
	 * @param event The event that should be dispatched; must not be 
	 * <code>null</code>.
	 * @return <code>true</code> if the event has a corresponding handler;
	 * <code>false</code> otherwise.
	 */
	private final boolean dispatchEvent(final Event event) {
		final CommandManager commandManager = CommandManager.getInstance();
		final List keyStrokes = new ArrayList(commandManager.getMode().getKeyStrokes());
		keyStrokes.add(generateStroke(event));
		final KeySequence childMode = KeySequence.getInstance(keyStrokes);
		final Map matchesByKeySequenceForMode = commandManager.getMatchesByKeySequenceForMode();
		commandManager.setMode(childMode);
		final Map childMatchesByKeySequenceForMode = commandManager.getMatchesByKeySequenceForMode();

		if (childMatchesByKeySequenceForMode.isEmpty()) {
			clear();
			final Match match = (Match) matchesByKeySequenceForMode.get(childMode);

			if (match != null) {
				final String commandId = match.getCommandId();
				final Map actionsById = commandManager.getActionsById();
				org.eclipse.ui.commands.IAction action = (org.eclipse.ui.commands.IAction) actionsById.get(commandId);

				if ((action != null) && (action.isEnabled())) {
					try {
						action.execute(event);
						return true; 
					} catch (final Exception e) {
						// TODO What do we do if an exception occurs?
					}
				}
			}
		} else {
			statusLineContributionItem.setText(childMode.format());
			update();
		}

		return false; 
	}

	/**
	 * Parses an event into a key stroke -- converting the key code (where
	 * appropriate) to the appropriate integer value.  This involves shifting
	 * the key code value in some circumstances.
	 * 
	 * @param event The event from which the key stroke should be generated;
	 * must not be <code>null</code>.
	 * @return The key stroke representing the event; never <code>null</code>.
	 */
	public static final KeyStroke generateStroke(final Event event) {
		int key = event.character;
		
		if (key == 0) {
			key = event.keyCode;
		} else {
			if (0 <= key && key <= 0x1F) {
				if ((event.stateMask & SWT.CTRL) != 0) {
					key += 0x40;
				}
			} else {
				if ('a' <= key && key <= 'z') {
					key -= 'a' - 'A';
				}
			}
		}
		
		final int modifiers = event.stateMask & SWT.MODIFIER_MASK;
		return KeySupport.convertFromSWT(modifiers + key);
	}

	/**
	 * Handles the key pressed event on the <code>Display</code>.  This grabs
	 * all keys before they are released to the application, and attempts to 
	 * execute any corresponding actions or commands (i.e., accelerator keys).
	 * 
	 * @param event The triggering event; must not be <code>null</code>.
	 */
	public final void handleEvent(final Event event) {
		// We will not process any key strokes that are only modifier keys.
		if ((event.keyCode & SWT.MODIFIER_MASK) != 0)
			return;

		// TODO Allow accelerator keys in modal dialogs.
		// Check to see if there is a modal dialog open.  If so, do nothing.
		final Display display = Display.getCurrent();
		final Shell[] shells = display.getShells();
		
		for (int i = 0; i < shells.length; i++) {
			final int style = shells[i].getStyle();
	
			if (((style & SWT.APPLICATION_MODAL) != 0) || ((style & SWT.PRIMARY_MODAL) != 0) || ((style & SWT.SYSTEM_MODAL) != 0))
				return;
		}

		// Attempt to dispatch the event to the appropriate action or command.         
		if (dispatchEvent(event)) {
			/* The event was dispatched, so check what kind of key press
			 * occurred.
			 */
			switch (event.type) {
				case SWT.KeyDown :
					// Simply kill the event here; don't allow it to continue.
					event.doit = false;
					break;
				case SWT.Traverse :
					// Allow other people to process the traversal.
					event.detail = SWT.TRAVERSE_NONE;
					event.doit = true;
					break;
				default :
					// Do nothing.
			}

			event.type = SWT.NONE;
		}
	}

	/**
	 * Checks to see whether the given command is available in the current
	 * context.
	 * 
	 * @param commandId The command ID to check for whether its in context.
	 * @return <code>true</code> if the command is in context; 
	 * <code>false</code> otherwise.
	 */
	public final boolean inContext(final String commandId) {
		if (commandId != null) {
			final ICommandManager commandManager = workbench.getCommandManager();
			final ICommand command = commandManager.getCommand(commandId);

			if ((command != null) && (command.isDefined())) {
				try {
					final SortedSet contextBindings = command.getContextBindings();

					if ((contextBindings != null) && (!contextBindings.isEmpty())) {
						final IContextManager contextManager = workbench.getContextManager();
						final List activeContextIds = contextManager.getActiveContextIds();
						final Iterator contextBindingItr = contextBindings.iterator();

						while (contextBindingItr.hasNext()) {
							final IContextBinding contextBinding = (IContextBinding) contextBindingItr.next();
							
							if (activeContextIds.contains(contextBinding.getContextId())) {
								return true;
							}
						}

						return false;                        
					} else {
                        return true;                                      
                    }                    
				} catch (final NotDefinedException e) {
					// Nothing needs to be done.
				}
			}
		}

		return true;
	}

	/**
	 * Called by the workbench when a window is about to open.  This simply
	 * adds the status line contribution item to the opening window. 
	 * @param window The window which is opening; must not be <code>null</code>.
	 */
	public final void registerWindow(final WorkbenchWindow window) {
		window.getStatusLineManager().add(statusLineContributionItem);
	}

	/**
	 * Updates the list of accelerator keys stored in the system.
	 */
	public final void update() {
		final IContextManager contextManager = workbench.getContextManager();
        final List activeContextIds = new ArrayList(contextManager.getActiveContextIds());
		activeContextIds.add(IWorkbenchConstants.DEFAULT_ACCELERATOR_SCOPE_ID);
        CommandManager.getInstance().setActiveContextIds(activeContextIds);
		ContextResolver.getInstance().setContextResolver(this);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/363.java