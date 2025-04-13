error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2756.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2756.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2756.java
text:
```scala
c@@ommand = new Command(commandsWithListeners, commandId);

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

package org.eclipse.ui.internal.csm.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.internal.csm.commands.api.ICommand;
import org.eclipse.ui.internal.csm.commands.api.ICommandEvent;
import org.eclipse.ui.internal.csm.commands.api.ICommandManager;
import org.eclipse.ui.internal.csm.commands.api.ICommandManagerEvent;
import org.eclipse.ui.internal.csm.commands.api.ICommandManagerListener;
import org.eclipse.ui.internal.csm.commands.api.IKeySequenceBinding;
import org.eclipse.ui.internal.util.Util;
import org.eclipse.ui.keys.KeySequence;

public final class CommandManager implements ICommandManager {

	static boolean isCommandDefinitionChildOf(String ancestor, String id, Map commandDefinitionsById) {
		Collection visited = new HashSet();

		while (id != null && !visited.contains(id)) {
			ICommandDefinition commandDefinition = (ICommandDefinition) commandDefinitionsById.get(id);				
			visited.add(id);

			if (commandDefinition != null && Util.equals(id = commandDefinition.getCategoryId(), ancestor))
				return true;
		}

		return false;
	}	

	private Set activeCommandIds = new HashSet();
	private Map commandsById = new WeakHashMap();
	private Set commandsWithListeners = new HashSet();
	private Map commandDefinitionsById = new HashMap();
	private List commandManagerListeners;
	private ICommandRegistry commandRegistry;	
	private Set definedCommandIds = new HashSet();
	private Set enabledCommandIds = new HashSet();	
	private Map keySequenceBindingsByCommandId = new HashMap();

	public CommandManager() {
		this(new ExtensionCommandRegistry(Platform.getExtensionRegistry()));
	}

	public CommandManager(ICommandRegistry commandRegistry) {
		if (commandRegistry == null)
			throw new NullPointerException();

		this.commandRegistry = commandRegistry;
		
		this.commandRegistry.addCommandRegistryListener(new ICommandRegistryListener() {
			public void commandRegistryChanged(ICommandRegistryEvent commandRegistryEvent) {
				readRegistry();
			}
		});

		readRegistry();
	}	
	
	public void addCommandManagerListener(ICommandManagerListener commandManagerListener) {
		if (commandManagerListener == null)
			throw new NullPointerException();
			
		if (commandManagerListeners == null)
			commandManagerListeners = new ArrayList();
		
		if (!commandManagerListeners.contains(commandManagerListener))
			commandManagerListeners.add(commandManagerListener);
	}

	public Set getActiveCommandIds() {
		return Collections.unmodifiableSet(activeCommandIds);
	}

	public ICommand getCommand(String commandId) {
		if (commandId == null)
			throw new NullPointerException();
			
		Command command = (Command) commandsById.get(commandId);
		
		if (command == null) {
			command = new Command(this, commandId);
			updateCommand(command);
			commandsById.put(commandId, command);
		}
		
		return command;
	}
	
	public Set getDefinedCommandIds() {
		return Collections.unmodifiableSet(definedCommandIds);
	}

	public Set getEnabledCommandIds() {
		return Collections.unmodifiableSet(enabledCommandIds);
	}	

	public void removeCommandManagerListener(ICommandManagerListener commandManagerListener) {
		if (commandManagerListener == null)
			throw new NullPointerException();
			
		if (commandManagerListeners != null)
			commandManagerListeners.remove(commandManagerListener);
	}

	public void setActiveCommandIds(Set activeCommandIds) {
		activeCommandIds = Util.safeCopy(activeCommandIds, String.class);
		boolean commandManagerChanged = false;
		Map commandEventsByCommandId = null;

		if (!this.activeCommandIds.equals(activeCommandIds)) {
			this.activeCommandIds = activeCommandIds;
			commandManagerChanged = true;	
			commandEventsByCommandId = updateCommands(commandsById.keySet());	
		}
		
		if (commandManagerChanged)
			fireCommandManagerChanged(new CommandManagerEvent(this, true, false, false));

		if (commandEventsByCommandId != null)
			notifyCommands(commandEventsByCommandId);	
	}
	
	public void setEnabledCommandIds(Set enabledCommandIds) {	
		enabledCommandIds = Util.safeCopy(enabledCommandIds, String.class);
		boolean commandManagerChanged = false;
		Map commandEventsByCommandId = null;

		if (!this.enabledCommandIds.equals(enabledCommandIds)) {
			this.enabledCommandIds = enabledCommandIds;
			commandManagerChanged = true;	
			commandEventsByCommandId = updateCommands(this.definedCommandIds);	
		}
		
		if (commandManagerChanged)
			fireCommandManagerChanged(new CommandManagerEvent(this, false, false, true));

		if (commandEventsByCommandId != null)
			notifyCommands(commandEventsByCommandId);	
	}	
	
	Set getCommandsWithListeners() {
		return commandsWithListeners;
	}

	private void fireCommandManagerChanged(ICommandManagerEvent commandManagerEvent) {
		if (commandManagerEvent == null)
			throw new NullPointerException();
		
		if (commandManagerListeners != null)
			for (int i = 0; i < commandManagerListeners.size(); i++)
				((ICommandManagerListener) commandManagerListeners.get(i)).commandManagerChanged(commandManagerEvent);
	}

	private void notifyCommands(Map commandEventsByCommandId) {	
		for (Iterator iterator = commandEventsByCommandId.entrySet().iterator(); iterator.hasNext();) {	
			Map.Entry entry = (Map.Entry) iterator.next();			
			String commandId = (String) entry.getKey();
			ICommandEvent commandEvent = (ICommandEvent) entry.getValue();
			Command command = (Command) commandsById.get(commandId);
			
			if (command != null)
				command.fireCommandChanged(commandEvent);
		}
	}

	private void readRegistry() {
		Collection commandDefinitions = new ArrayList();
		commandDefinitions.addAll(commandRegistry.getCommandDefinitions());				
		Map commandDefinitionsById = new HashMap(CommandDefinition.commandDefinitionsById(commandDefinitions, false));

		for (Iterator iterator = commandDefinitionsById.values().iterator(); iterator.hasNext();) {
			ICommandDefinition commandDefinition = (ICommandDefinition) iterator.next();
			String name = commandDefinition.getName();
				
			if (name == null || name.length() == 0)
				iterator.remove();
		}

		for (Iterator iterator = commandDefinitionsById.keySet().iterator(); iterator.hasNext();)
			if (!isCommandDefinitionChildOf(null, (String) iterator.next(), commandDefinitionsById))
				iterator.remove();

		Map keySequenceBindingDefinitionsByCommandId = KeySequenceBindingDefinition.keySequenceBindingDefinitionsByCommandId(commandRegistry.getKeySequenceBindingDefinitions());
		Map keySequenceBindingsByCommandId = new HashMap();		

		for (Iterator iterator = keySequenceBindingDefinitionsByCommandId.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String commandId = (String) entry.getKey();
			
			if (commandDefinitionsById.containsKey(commandId)) {			
				Collection keySequenceBindingDefinitions = (Collection) entry.getValue();
				
				if (keySequenceBindingDefinitions != null)
					for (Iterator iterator2 = keySequenceBindingDefinitions.iterator(); iterator2.hasNext();) {
						IKeySequenceBindingDefinition keySequenceBindingDefinition = (IKeySequenceBindingDefinition) iterator2.next();
						KeySequence keySequence = keySequenceBindingDefinition.getKeySequence();
					
						if (keySequence != null && keySequence.isComplete()) {
							IKeySequenceBinding keySequenceBinding = new KeySequenceBinding(keySequence);	
							List keySequenceBindings = (List) keySequenceBindingsByCommandId.get(commandId);
							
							if (keySequenceBindings == null) {
								keySequenceBindings = new ArrayList();
								keySequenceBindingsByCommandId.put(commandId, keySequenceBindings);
							}
							
							keySequenceBindings.add(keySequenceBinding);
						}
					}
			}
		}		
		
		this.commandDefinitionsById = commandDefinitionsById;
		this.keySequenceBindingsByCommandId = keySequenceBindingsByCommandId;			
		boolean commandManagerChanged = false;			
		Set definedCommandIds = new HashSet(commandDefinitionsById.keySet());		

		if (!definedCommandIds.equals(this.definedCommandIds)) {
			this.definedCommandIds = definedCommandIds;
			commandManagerChanged = true;	
		}

		Map commandEventsByCommandId = updateCommands(commandsById.keySet());	
		
		if (commandManagerChanged)
			fireCommandManagerChanged(new CommandManagerEvent(this, false, true, false));

		if (commandEventsByCommandId != null)
			notifyCommands(commandEventsByCommandId);		
	}

	private ICommandEvent updateCommand(Command command) {
		boolean activeChanged = command.setActive(activeCommandIds.contains(command.getId()));		
		ICommandDefinition commandDefinition = (ICommandDefinition) commandDefinitionsById.get(command.getId());
		boolean definedChanged = command.setDefined(commandDefinition != null);
		boolean descriptionChanged = command.setDescription(commandDefinition != null ? commandDefinition.getDescription() : null);		
		boolean enabledChanged = command.setEnabled(enabledCommandIds.contains(command.getId()));
		boolean nameChanged = command.setName(commandDefinition != null ? commandDefinition.getName() : null);
		boolean parentIdChanged = command.setCategoryId(commandDefinition != null ? commandDefinition.getCategoryId() : null);				
		List keySequenceBindings = (List) keySequenceBindingsByCommandId.get(command.getId());
		boolean keySequenceBindingsChanged = command.setKeySequenceBindings(keySequenceBindings != null ? keySequenceBindings : Collections.EMPTY_LIST);

		if (activeChanged || definedChanged || descriptionChanged || enabledChanged || nameChanged || parentIdChanged || keySequenceBindingsChanged)
			return new CommandEvent(command, activeChanged, parentIdChanged, definedChanged, descriptionChanged, enabledChanged, keySequenceBindingsChanged, nameChanged); 
		else 
			return null;
	}

	private Map updateCommands(Collection commandIds) {
		Map commandEventsByCommandId = new TreeMap();
		
		for (Iterator iterator = commandIds.iterator(); iterator.hasNext();) {		
			String commandId = (String) iterator.next();					
			Command command = (Command) commandsById.get(commandId);
			
			if (command != null) {
				ICommandEvent commandEvent = updateCommand(command);
				
				if (commandEvent != null)
					commandEventsByCommandId.put(commandId, commandEvent);
			}
		}
		
		return commandEventsByCommandId;			
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2756.java