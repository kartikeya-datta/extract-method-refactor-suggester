error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6009.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6009.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6009.java
text:
```scala
t@@ext = KeySupport.formatSequence(sequence, true);

/************************************************************************
Copyright (c) 2003 IBM Corporation and others.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v10.html

Contributors:
	IBM - Initial implementation
************************************************************************/

package org.eclipse.ui.internal.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

public class Manager {

	private static Manager instance;

	public static Manager getInstance() {
		if (instance == null)
			instance = new Manager();
			
		return instance;	
	}

	private SequenceMachine gestureMachine;	
	private SequenceMachine keyMachine;	
	
	private Manager() {
		super();
		gestureMachine = SequenceMachine.create();
		keyMachine = SequenceMachine.create();
		reset();		
	}

	public SequenceMachine getGestureMachine() {
		return gestureMachine;
	}

	public SequenceMachine getKeyMachine() {
		return keyMachine;
	}

	public String getGestureTextForCommand(String command)
		throws IllegalArgumentException {
		String text = null;
		Sequence sequence = getGestureMachine().getFirstSequenceForCommand(command);
		
		if (sequence != null)
			text = GestureSupport.formatSequence(sequence);
			
		return text != null ? text : Util.ZERO_LENGTH_STRING;
	}

	public String getKeyTextForCommand(String command)
		throws IllegalArgumentException {
		String text = null;
		Sequence sequence = getKeyMachine().getFirstSequenceForCommand(command);
		
		if (sequence != null)
			text = KeySupport.formatSequence(sequence);
			
		return text != null ? text : Util.ZERO_LENGTH_STRING;
	}

	public void reset() {
		CoreRegistry coreRegistry = CoreRegistry.getInstance();		
		LocalRegistry localRegistry = LocalRegistry.getInstance();
		PreferenceRegistry preferenceRegistry = PreferenceRegistry.getInstance();

		try {
			coreRegistry.load();
		} catch (IOException eIO) {
		}

		try {
			localRegistry.load();
		} catch (IOException eIO) {
		}
		
		try {
			preferenceRegistry.load();
		} catch (IOException eIO) {
		}

		List activeGestureConfigurations = new ArrayList();
		activeGestureConfigurations.addAll(coreRegistry.getActiveGestureConfigurations());
		activeGestureConfigurations.addAll(localRegistry.getActiveGestureConfigurations());
		activeGestureConfigurations.addAll(preferenceRegistry.getActiveGestureConfigurations());	
		String activeGestureConfigurationId;
			
		if (activeGestureConfigurations.size() == 0)
			activeGestureConfigurationId = Util.ZERO_LENGTH_STRING;
		else {
			ActiveConfiguration activeGestureConfiguration = (ActiveConfiguration) activeGestureConfigurations.get(activeGestureConfigurations.size() - 1);
			activeGestureConfigurationId = activeGestureConfiguration.getValue();
		}

		List activeKeyConfigurations = new ArrayList();
		activeKeyConfigurations.addAll(coreRegistry.getActiveKeyConfigurations());
		activeKeyConfigurations.addAll(localRegistry.getActiveKeyConfigurations());
		activeKeyConfigurations.addAll(preferenceRegistry.getActiveKeyConfigurations());	
		String activeKeyConfigurationId;
			
		if (activeKeyConfigurations.size() == 0)
			activeKeyConfigurationId = Util.ZERO_LENGTH_STRING;
		else {
			ActiveConfiguration activeKeyConfiguration = (ActiveConfiguration) activeKeyConfigurations.get(activeKeyConfigurations.size() - 1);
			activeKeyConfigurationId = activeKeyConfiguration.getValue();
		}

		SortedSet gestureBindingSet = new TreeSet();		
		gestureBindingSet.addAll(coreRegistry.getGestureBindings());
		gestureBindingSet.addAll(localRegistry.getGestureBindings());
		gestureBindingSet.addAll(preferenceRegistry.getGestureBindings());

		List gestureConfigurations = new ArrayList();
		gestureConfigurations.addAll(coreRegistry.getGestureConfigurations());
		gestureConfigurations.addAll(localRegistry.getGestureConfigurations());
		gestureConfigurations.addAll(preferenceRegistry.getGestureConfigurations());
		SortedMap gestureConfigurationMap = SequenceMachine.buildPathMapForConfigurationMap(Configuration.sortedMapById(gestureConfigurations));

		SortedSet keyBindingSet = new TreeSet();		
		keyBindingSet.addAll(coreRegistry.getKeyBindings());
		keyBindingSet.addAll(localRegistry.getKeyBindings());
		keyBindingSet.addAll(preferenceRegistry.getKeyBindings());

		List keyConfigurations = new ArrayList();
		keyConfigurations.addAll(coreRegistry.getKeyConfigurations());
		keyConfigurations.addAll(localRegistry.getKeyConfigurations());
		keyConfigurations.addAll(preferenceRegistry.getKeyConfigurations());
		SortedMap keyConfigurationMap = SequenceMachine.buildPathMapForConfigurationMap(Configuration.sortedMapById(keyConfigurations));
		
		List scopes = new ArrayList();
		scopes.addAll(coreRegistry.getScopes());
		scopes.addAll(localRegistry.getScopes());
		scopes.addAll(preferenceRegistry.getScopes());
		SortedMap scopeMap = SequenceMachine.buildPathMapForScopeMap(Scope.sortedMapById(scopes));

		gestureMachine.setConfiguration(activeGestureConfigurationId);
		gestureMachine.setConfigurationMap(Collections.unmodifiableSortedMap(gestureConfigurationMap));
		gestureMachine.setScopeMap(Collections.unmodifiableSortedMap(scopeMap));
		gestureMachine.setBindingSet(Collections.unmodifiableSortedSet(gestureBindingSet));

		keyMachine.setConfiguration(activeKeyConfigurationId);	
		keyMachine.setConfigurationMap(Collections.unmodifiableSortedMap(keyConfigurationMap));
		keyMachine.setScopeMap(Collections.unmodifiableSortedMap(scopeMap));
		keyMachine.setBindingSet(Collections.unmodifiableSortedSet(keyBindingSet));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6009.java