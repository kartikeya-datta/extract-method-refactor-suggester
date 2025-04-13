error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7512.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7512.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7512.java
text:
```scala
r@@eturn true;

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.ui.internal.commands.ws;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ExternalActionManager.ICallback;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.commands.CommandEvent;
import org.eclipse.ui.commands.ICommand;
import org.eclipse.ui.commands.ICommandListener;
import org.eclipse.ui.commands.ICommandManager;
import org.eclipse.ui.commands.IKeySequenceBinding;
import org.eclipse.ui.commands.NotDefinedException;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.util.Util;
import org.eclipse.ui.keys.KeySequence;
import org.eclipse.ui.keys.KeyStroke;
import org.eclipse.ui.keys.SWTKeySupport;

/**
 * @since 3.0
 */
public final class CommandCallback implements ICallback {

    /**
     * The internationalization bundle for text produced by this class.
     */
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(CommandCallback.class.getName());

    /**
     * A set of all the command identifiers that have been logged as broken so
     * far. For each of these, there will be a listener on the corresponding
     * command. If the command ever becomes defined, the item will be removed
     * from this set and the listener removed. This value may be empty, but
     * never <code>null</code>.
     */
    private final Set loggedCommandIds = new HashSet();

    /**
     * The list of listeners that have registered for property change
     * notification. This is a map os <code>IPropertyChangeListener</code> to
     * <code>ICommandListener</code>.
     */
    private final Map registeredListeners = new HashMap();

    /**
     * The workbench to query about command and context information. This value
     * should never be <code>null</code>.
     */
    private final IWorkbench workbench;

    /**
     * Constructs a new instance of <code>CommandCallback</code> with the
     * workbench it should be using.
     * 
     * @param workbenchToUse
     *            The workbench that should be used for resolving command
     *            information; must not be <code>null</code>.
     */
    public CommandCallback(final IWorkbench workbenchToUse) {
        workbench = workbenchToUse;
    }

    /**
     * @see org.eclipse.jface.action.ExternalActionManager.ICallback#addPropertyChangeListener(String,
     *      IPropertyChangeListener)
     */
    public void addPropertyChangeListener(final String commandId,
            final IPropertyChangeListener listener) {
        final ICommand command = workbench.getCommandSupport()
                .getCommandManager().getCommand(commandId);
        final ICommandListener commandListener = new ICommandListener() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.ui.commands.ICommandListener#commandChanged(org.eclipse.ui.commands.CommandEvent)
             */
            public void commandChanged(CommandEvent commandEvent) {
                // Check if the text has changed.
                if (commandEvent.hasNameChanged()
 commandEvent.haveKeySequenceBindingsChanged()) {
                    PropertyChangeEvent event;
                    try {
                        event = new PropertyChangeEvent(command, IAction.TEXT,
                                null /* TODO Don't have enough information */,
                                command.getName());
                    } catch (final NotDefinedException e) {
                        event = new PropertyChangeEvent(command, IAction.TEXT,
                                null /* TODO Don't have enough information */,
                                null /* Couldn't get the name */);
                    }
                    listener.propertyChange(event);
                }

                // TODO Add enabled property change.
            }
        };

        command.addCommandListener(commandListener);
        Assert.isTrue(!registeredListeners.containsKey(listener));
        registeredListeners.put(listener, commandListener);
    }

    /**
     * @see org.eclipse.jface.action.ExternalActionManager.ICallback#getAccelerator(String)
     */
    public final Integer getAccelerator(final String commandId) {
        final ICommand command = workbench.getCommandSupport()
                .getCommandManager().getCommand(commandId);
        Integer accelerator = null;

        if (command.isDefined()) {
            List keySequenceBindings = command.getKeySequenceBindings();
            final int size = keySequenceBindings.size();

            for (int i = 0; i < size; i++) {
                IKeySequenceBinding keySequenceBinding = (IKeySequenceBinding) keySequenceBindings
                        .get(i);
                List keyStrokes = keySequenceBinding.getKeySequence()
                        .getKeyStrokes();

                if (keyStrokes.size() == 1) {
                    KeyStroke keyStroke = (KeyStroke) keyStrokes.get(0);
                    accelerator = new Integer(SWTKeySupport
                            .convertKeyStrokeToAccelerator(keyStroke));
                    break;
                }
            }
        }

        return accelerator;
    }

    /**
     * @see org.eclipse.jface.action.ExternalActionManager.ICallback#getAcceleratorText(String)
     */
    public final String getAcceleratorText(final String commandId) {
        final ICommand command = workbench.getCommandSupport()
                .getCommandManager().getCommand(commandId);
        String acceleratorText = null;

        if (command.isDefined()) {
            List keySequenceBindings = command.getKeySequenceBindings();

            if (!keySequenceBindings.isEmpty()) {
                IKeySequenceBinding keySequenceBinding = (IKeySequenceBinding) keySequenceBindings
                        .get(0);
                acceleratorText = keySequenceBinding.getKeySequence().format();
            }
        }

        return acceleratorText;
    }

    /**
     * @see org.eclipse.jface.action.ExternalActionManager.ICallback#isAcceleratorInUse(int)
     */
    public boolean isAcceleratorInUse(int accelerator) {
        final KeySequence keySequence = KeySequence.getInstance(SWTKeySupport
                .convertAcceleratorToKeyStroke(accelerator));
        final ICommandManager commandManager = workbench.getCommandSupport()
                .getCommandManager();
        return commandManager.isPerfectMatch(keySequence)
 commandManager.isPartialMatch(keySequence);
    }

    /**
     * Calling this method with an undefined command id will generate a log
     * message.
     * 
     * @see org.eclipse.jface.action.ExternalActionManager.ICallback#isActive(String)
     */
    public final boolean isActive(final String commandId) {
        if (commandId != null) {
            final ICommand command = workbench.getCommandSupport()
                    .getCommandManager().getCommand(commandId);

            if (!command.isDefined() && (!loggedCommandIds.contains(commandId))) {
                // The command is not yet defined, so we should log this.
                final StringBuffer message = new StringBuffer();
                message.append("The command '"); //$NON-NLS-1$
                message.append(command.getId());
                message
                        .append("' is not defined, but is being asked if it is active.  Are you using an actionDefinitionId without defining a command?"); //$NON-NLS-1$
                IStatus status = new Status(IStatus.ERROR,
                        WorkbenchPlugin.PI_WORKBENCH, 0, Util.translateString(
                                RESOURCE_BUNDLE,
                                "undefinedCommand.WarningMessage"), null); //$NON-NLS-1$
                WorkbenchPlugin.log(message.toString(), status);

                // And remember this item so we don't log it again.
                loggedCommandIds.add(commandId);
                command.addCommandListener(new ICommandListener() {
                    /*
                     * (non-Javadoc)
                     * 
                     * @see org.eclipse.ui.commands.ICommandListener#commandChanged(org.eclipse.ui.commands.CommandEvent)
                     */
                    public final void commandChanged(
                            final CommandEvent commandEvent) {
                        if (command.isDefined()) {
                            command.removeCommandListener(this);
                            loggedCommandIds.remove(commandId);
                        }
                    }
                });

                return false;
            }

            return workbench.getActivitySupport().getActivityManager()
                    .getIdentifier(command.getId()).isEnabled();
        }

        return true;
    }

    /**
     * @see org.eclipse.jface.action.ExternalActionManager.ICallback#removePropertyChangeListener(String,
     *      IPropertyChangeListener)
     */
    public final void removePropertyChangeListener(final String commandId,
            final IPropertyChangeListener listener) {
        final ICommand command = workbench.getCommandSupport()
                .getCommandManager().getCommand(commandId);
        final Object associatedListener = registeredListeners.remove(listener);
        if (associatedListener instanceof ICommandListener) {
            final ICommandListener commandListener = (ICommandListener) associatedListener;
            command.removeCommandListener(commandListener);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7512.java