error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8586.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8586.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8586.java
text:
```scala
S@@ortedSet definedCommandIds = new TreeSet(commandElementsById.keySet());

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.commands.ICommand;
import org.eclipse.ui.commands.ICommandManager;
import org.eclipse.ui.commands.ICommandManagerEvent;
import org.eclipse.ui.commands.ICommandManagerListener;
import org.eclipse.ui.internal.util.Util;

public final class CommandManager implements ICommandManager {

	private SortedSet activeCommandIds = new TreeSet();
	private SortedMap commandElementsById = new TreeMap();
	private ICommandManagerEvent commandManagerEvent;
	private List commandManagerListeners;
	private SortedMap commandsById = new TreeMap();
	private SortedSet definedCommandIds = new TreeSet();
	private RegistryReader registryReader;

	public CommandManager() {
		super();
		updateDefinedCommandIds();
	}

	public void addCommandManagerListener(ICommandManagerListener commandManagerListener)
		throws IllegalArgumentException {
		if (commandManagerListener == null)
			throw new IllegalArgumentException();
			
		if (commandManagerListeners == null)
			commandManagerListeners = new ArrayList();
		
		if (!commandManagerListeners.contains(commandManagerListener))
			commandManagerListeners.add(commandManagerListener);
	}

	public SortedSet getActiveCommandIds() {
		return Collections.unmodifiableSortedSet(activeCommandIds);
	}

	public ICommand getCommand(String commandId)
		throws IllegalArgumentException {
		if (commandId == null)
			throw new IllegalArgumentException();
			
		ICommand command = (ICommand) commandsById.get(commandId);
		
		if (command == null) {
			command = new Command(this, commandId);
			commandsById.put(commandId, command);
		}
		
		return command;
	}

	public SortedSet getDefinedCommandIds() {
		return Collections.unmodifiableSortedSet(activeCommandIds);
	}

	public void removeCommandManagerListener(ICommandManagerListener commandManagerListener)
		throws IllegalArgumentException {
		if (commandManagerListener == null)
			throw new IllegalArgumentException();
			
		if (commandManagerListeners != null) {
			commandManagerListeners.remove(commandManagerListener);
			
			if (commandManagerListeners.isEmpty())
				commandManagerListeners = null;
		}
	}

	public void setActiveCommandIds(SortedSet activeCommandIds)
		throws IllegalArgumentException {
		activeCommandIds = Util.safeCopy(activeCommandIds, String.class);
		SortedSet activatingCommandIds = new TreeSet();
		SortedSet deactivatingCommandIds = new TreeSet();
		Iterator iterator = activeCommandIds.iterator();

		while (iterator.hasNext()) {
			String id = (String) iterator.next();
		
			if (!this.activeCommandIds.contains(id))
				activatingCommandIds.add(id);
		}

		iterator = this.activeCommandIds.iterator();
		
		while (iterator.hasNext()) {
			String id = (String) iterator.next();
		
			if (!activeCommandIds.contains(id))
				deactivatingCommandIds.add(id);					
		}		

		SortedSet commandChanges = new TreeSet();
		commandChanges.addAll(activatingCommandIds);		
		commandChanges.addAll(deactivatingCommandIds);			

		if (!commandChanges.isEmpty()) {
			this.activeCommandIds = activeCommandIds;	
			fireCommandManagerChanged();

			iterator = commandChanges.iterator();
		
			while (iterator.hasNext())
				fireCommandChanged((String) iterator.next());
		}
	}

	public void updateDefinedCommandIds() {
		if (registryReader == null)
			registryReader = new RegistryReader(Platform.getPluginRegistry());
		
		registryReader.load();
		SortedMap commandElementsById = CommandElement.sortedMapById(registryReader.getCommandElements());		
		SortedSet commandElementAdditions = new TreeSet();		
		SortedSet commandElementChanges = new TreeSet();
		SortedSet commandElementRemovals = new TreeSet();		
		Iterator iterator = commandElementsById.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String id = (String) entry.getKey();
			CommandElement commandElement = (CommandElement) entry.getValue();
			
			if (!this.commandElementsById.containsKey(id))
				commandElementAdditions.add(id);
			else if (!Util.equals(commandElement, this.commandElementsById.get(id)))
				commandElementChanges.add(id);								
		}

		iterator = this.commandElementsById.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String id = (String) entry.getKey();
			CommandElement commandElement = (CommandElement) entry.getValue();
			
			if (!commandElementsById.containsKey(id))
				commandElementRemovals.add(id);						
		}

		SortedSet commandChanges = new TreeSet();
		commandChanges.addAll(commandElementAdditions);		
		commandChanges.addAll(commandElementChanges);		
		commandChanges.addAll(commandElementRemovals);
		
		if (!commandChanges.isEmpty()) {
			this.commandElementsById = commandElementsById;		
			SortedSet definedCommandIds = (SortedSet) commandElementsById.keySet();

			if (!Util.equals(definedCommandIds, this.definedCommandIds)) {	
				this.definedCommandIds = definedCommandIds;
				fireCommandManagerChanged();
			}

			iterator = commandChanges.iterator();
		
			while (iterator.hasNext())
				fireCommandChanged((String) iterator.next());
		}
	}

	CommandElement getCommandElement(String commandId) {
		return (CommandElement) commandElementsById.get(commandId);
	}
	
	private void fireCommandChanged(String commandId) {
		Command command = (Command) commandsById.get(commandId);
		
		if (command != null) 
			command.fireCommandChanged();		
	}

	private void fireCommandManagerChanged() {
		if (commandManagerListeners != null) {
			Iterator iterator = commandManagerListeners.iterator();
			
			if (iterator.hasNext()) {
				if (commandManagerEvent == null)
					commandManagerEvent = new CommandManagerEvent(this);
				
				while (iterator.hasNext())	
					((ICommandManagerListener) iterator.next()).commandManagerChanged(commandManagerEvent);
			}							
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8586.java