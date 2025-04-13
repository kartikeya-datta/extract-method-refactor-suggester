error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8873.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8873.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8873.java
text:
```scala
protected I@@CommandListener getCommandListener() {

/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.internal.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandEvent;
import org.eclipse.core.commands.ICommandListener;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.services.IServiceLocator;

/**
 * Instantiate an action that will execute the command.
 * <p>
 * This is a legacy bridge class, and should not be used outside of the
 * framework. Please use menu contributions to display a command in a menu or
 * toolbar.
 * </p>
 * <p>
 * <b>Note:</b> Clients my instantiate, but they must not subclass.
 * </p>
 * 
 * @since 3.3
 */
public class CommandAction extends Action {

	private IHandlerService handlerService = null;

	private ParameterizedCommand parameterizedCommand = null;

	private ICommandListener commandListener;
	
	protected CommandAction() {
		
	}

	/**
	 * Creates the action backed by a command. For commands that don't take
	 * parameters.
	 * 
	 * @param serviceLocator
	 *            The service locator that is closest in lifecycle to this
	 *            action.
	 * @param commandIdIn
	 *            the command id. Must not be <code>null</code>.
	 */
	public CommandAction(IServiceLocator serviceLocator, String commandIdIn) {
		this(serviceLocator, commandIdIn, null);
	}

	/**
	 * Creates the action backed by a parameterized command. The parameterMap
	 * must contain only all required parameters, and may contain the optional
	 * parameters.
	 * 
	 * @param serviceLocator
	 *            The service locator that is closest in lifecycle to this
	 *            action.
	 * @param commandIdIn
	 *            the command id. Must not be <code>null</code>.
	 * @param parameterMap
	 *            the parameter map. May be <code>null</code>.
	 */
	public CommandAction(IServiceLocator serviceLocator, String commandIdIn,
			Map parameterMap) {
		if (commandIdIn == null) {
			throw new NullPointerException("commandIdIn must not be null"); //$NON-NLS-1$
		}
		init(serviceLocator, commandIdIn, parameterMap);
	}

	private ICommandListener getCommandListener() {
		if (commandListener == null) {
			commandListener = new ICommandListener() {
				public void commandChanged(CommandEvent commandEvent) {
					if (commandEvent.isHandledChanged()
 commandEvent.isEnabledChanged()) {
						if (commandEvent.getCommand().isDefined()) {
							setEnabled(commandEvent.getCommand().isEnabled());
						}
					}
				}
			};
		}
		return commandListener;
	}

	/**
	 * Build a command from the executable extension information.
	 * 
	 * @param commandService
	 *            to get the Command object
	 * @param commandId
	 *            the command id for this action
	 * @param parameterMap
	 */
	private void createCommand(ICommandService commandService,
			String commandId, Map parameterMap) {
		try {
			Command cmd = commandService.getCommand(commandId);
			if (!cmd.isDefined()) {
				WorkbenchPlugin.log("Command " + commandId + " is undefined"); //$NON-NLS-1$//$NON-NLS-2$
				return;
			}

			if (parameterMap == null) {
				parameterizedCommand = new ParameterizedCommand(cmd, null);
				return;
			}

			ArrayList parameters = new ArrayList();
			Iterator i = parameterMap.keySet().iterator();
			while (i.hasNext()) {
				String parmName = (String) i.next();
				IParameter parm = cmd.getParameter(parmName);
				if (parm == null) {
					WorkbenchPlugin.log("Invalid parameter \'" + parmName //$NON-NLS-1$
							+ "\' for command " + commandId); //$NON-NLS-1$
					return;
				}
				parameters.add(new Parameterization(parm, (String) parameterMap
						.get(parmName)));
			}
			parameterizedCommand = new ParameterizedCommand(cmd,
					(Parameterization[]) parameters
							.toArray(new Parameterization[parameters.size()]));
		} catch (NotDefinedException e) {
			WorkbenchPlugin.log(e);
		}
	}

	public void dispose() {
		// not important for command ID, maybe for command though.
		handlerService = null;
		if (commandListener != null) {
			parameterizedCommand.getCommand().removeCommandListener(
					commandListener);
			commandListener = null;
		}
		parameterizedCommand = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#runWithEvent(org.eclipse.swt.widgets.Event)
	 */
	public void runWithEvent(Event event) {
		if (handlerService == null) {
			String commandId = (parameterizedCommand == null ? "unknownCommand" //$NON-NLS-1$
					: parameterizedCommand.getId());
			WorkbenchPlugin.log("Cannot run " + commandId //$NON-NLS-1$
					+ " before command action has been initialized"); //$NON-NLS-1$
			return;
		}
		try {
			if (parameterizedCommand != null) {
				handlerService.executeCommand(parameterizedCommand, event);
			}
		} catch (Exception e) {
			WorkbenchPlugin.log(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		// hopefully this is never called
		runWithEvent(null);
	}

	protected void init(IServiceLocator serviceLocator, String commandIdIn,
			Map parameterMap) {
		if (handlerService != null) {
			// already initialized
			return;
		}
		handlerService = (IHandlerService) serviceLocator
				.getService(IHandlerService.class);
		ICommandService commandService = (ICommandService) serviceLocator
				.getService(ICommandService.class);
		createCommand(commandService, commandIdIn, parameterMap);
		if (parameterizedCommand != null) {
			setId(parameterizedCommand.getId());
			try {
				setText(parameterizedCommand.getName());
			} catch (NotDefinedException e) {
				// if we get this far it shouldn't be a problem
			}
			parameterizedCommand.getCommand().addCommandListener(
					getCommandListener());
			setEnabled(parameterizedCommand.getCommand().isEnabled());
		}
	}

	protected ParameterizedCommand getParameterizedCommand() {
		return parameterizedCommand;
	}

	public String getActionDefinitionId() {
		if (parameterizedCommand != null) {
			return parameterizedCommand.getId();
		}
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8873.java