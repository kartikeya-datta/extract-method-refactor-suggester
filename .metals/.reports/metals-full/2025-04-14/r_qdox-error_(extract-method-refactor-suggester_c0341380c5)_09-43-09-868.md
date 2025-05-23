error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5183.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5183.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5183.java
text:
```scala
C@@opyright (c) 2003 IBM Corporation and others.

/************************************************************************
Copyright (c) 2002 IBM Corporation and others.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v10.html

Contributors:
	IBM - Initial implementation
************************************************************************/

package org.eclipse.ui.internal.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public final class KeyMachine {

	public static KeyMachine create() {
		return new KeyMachine();
	}

	private Map commandMap;
	private Map commandMapForMode;
	private SortedSet keyBindingSet;
	private String keyConfiguration;
	private SortedMap keyConfigurationMap;
	private Map keySequenceMap;
	private Map keySequenceMapForMode;
	private SortedSet matchSet;	
	private SortedSet matchSetForMode;
	private KeySequence mode;
	private SortedMap scopeMap;
	private String[] scopes;
	private boolean solved;
	private SortedMap tree;

	private KeyMachine() {
		super();
		keyConfigurationMap = new TreeMap();
		scopeMap = new TreeMap();
		keyBindingSet = new TreeSet();		
		keyConfiguration = "org.eclipse.ui.defaultConfiguration";
		scopes = new String[] { "org.eclipse.ui.globalScope" };
		mode = KeySequence.create();	
	}

	public Map getActionMap() {
		if (commandMap == null)
			commandMap = Collections.unmodifiableMap(KeyNode.toActionMap(getMatchSet()));				
		
		return commandMap;
	}
	
	public Map getActionMapForMode() {
		if (commandMapForMode == null)
			commandMapForMode = Collections.unmodifiableMap(KeyNode.toActionMap(getMatchSetForMode()));				
		
		return commandMapForMode;
	}

	public SortedSet getKeyBindingSet() {
		return keyBindingSet;	
	}

	public String getKeyConfiguration() {
		return keyConfiguration;
	}		

	public SortedMap getKeyConfigurationMap() {
		return keyConfigurationMap;	
	}

	public Map getKeySequenceMap() {
		if (keySequenceMap == null)
			keySequenceMap = Collections.unmodifiableMap(KeyNode.toKeySequenceMap(getMatchSet()));				
		
		return keySequenceMap;
	}

	public Map getKeySequenceMapForMode() {
		if (keySequenceMapForMode == null)
			keySequenceMapForMode = Collections.unmodifiableMap(KeyNode.toKeySequenceMap(getMatchSetForMode()));				
		
		return keySequenceMapForMode;
	}

	public SortedSet getMatchSet() {
		if (matchSet == null) {
			solve();
			SortedSet matchSet = new TreeSet();			
			KeyNode.toMatchSet(tree, matchSet);
			this.matchSet = Collections.unmodifiableSortedSet(matchSet);
		}
		
		return matchSet;
	}

	public SortedSet getMatchSetForMode() {
		if (matchSetForMode == null) {
			SortedSet matchSetForMode = new TreeSet();
			Iterator iterator = getMatchSet().iterator();
			
			while (iterator.hasNext()) {
				KeyBindingMatch keyBindingMatch = (KeyBindingMatch) iterator.next();

				if (keyBindingMatch.getKeyBinding().getKeySequence().isChildOf(mode, false))
					matchSetForMode.add(keyBindingMatch);				
			}

			this.matchSetForMode = Collections.unmodifiableSortedSet(matchSetForMode);
		}
		
		return matchSetForMode;
	}

	public KeySequence getMode() {
		return mode;	
	}	

	public SortedMap getScopeMap() {
		return scopeMap;	
	}	
	
	public String[] getScopes() {
		return (String[]) scopes.clone();
	}		

	public boolean setKeyBindingSet(SortedSet keyBindingSet)
		throws IllegalArgumentException {
		if (keyBindingSet == null)
			throw new IllegalArgumentException();
		
		keyBindingSet = new TreeSet(keyBindingSet);
		Iterator iterator = keyBindingSet.iterator();
		
		while (iterator.hasNext())
			if (!(iterator.next() instanceof KeyBinding))
				throw new IllegalArgumentException();

		if (this.keyBindingSet.equals(keyBindingSet))
			return false;
		
		this.keyBindingSet = Collections.unmodifiableSortedSet(keyBindingSet);
		invalidateTree();
		return true;
	}

	public boolean setKeyConfiguration(String keyConfiguration) {
		if (keyConfiguration == null)
			throw new IllegalArgumentException();
			
		if (this.keyConfiguration.equals(keyConfiguration))
			return false;
		
		this.keyConfiguration = keyConfiguration;
		invalidateSolution();
		return true;
	}

	public boolean setKeyConfigurationMap(SortedMap keyConfigurationMap)
		throws IllegalArgumentException {
		if (keyConfigurationMap == null)
			throw new IllegalArgumentException();
			
		keyConfigurationMap = new TreeMap(keyConfigurationMap);
		Iterator iterator = keyConfigurationMap.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			
			if (!(entry.getKey() instanceof String) || !(entry.getValue() instanceof Path))
				throw new IllegalArgumentException();			
		}

		if (this.keyConfigurationMap.equals(keyConfigurationMap))
			return false;
					
		this.keyConfigurationMap = Collections.unmodifiableSortedMap(keyConfigurationMap);
		invalidateTree();
		return true;
	}

	public boolean setMode(KeySequence mode)
		throws IllegalArgumentException {
		if (mode == null)
			throw new IllegalArgumentException();
			
		if (this.mode.equals(mode))
			return false;
		
		this.mode = mode;
		invalidateMode();
		return true;
	}
	
	public boolean setScopeMap(SortedMap scopeMap)
		throws IllegalArgumentException {
		if (scopeMap == null)
			throw new IllegalArgumentException();
			
		scopeMap = new TreeMap(scopeMap);
		Iterator iterator = scopeMap.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			
			if (!(entry.getKey() instanceof String) || !(entry.getValue() instanceof Path))
				throw new IllegalArgumentException();			
		}
		
		if (this.scopeMap.equals(scopeMap))
			return false;
				
		this.scopeMap = Collections.unmodifiableSortedMap(scopeMap);
		invalidateTree();
		return true;
	}	
	
	public boolean setScopes(String[] scopes)
		throws IllegalArgumentException {
		if (scopes == null || scopes.length == 0)
			throw new IllegalArgumentException();

		scopes = (String[]) scopes.clone();
		
		for (int i = 0; i < scopes.length; i++)
			if (scopes[i] == null)
				throw new IllegalArgumentException();	
		
		if (Arrays.equals(this.scopes, scopes))
			return false;
		
		this.scopes = scopes;
		invalidateSolution();
		return true;		
	}	

	private void build() {
		if (tree == null) {		
			tree = new TreeMap();		
			Iterator iterator = keyBindingSet.iterator();
		
			while (iterator.hasNext()) {
				KeyBinding keyBinding = (KeyBinding) iterator.next();
				Path scope = (Path) scopeMap.get(keyBinding.getScope());
		
				if (scope == null)
					continue;

				Path keyConfiguration = (Path) keyConfigurationMap.get(keyBinding.getKeyConfiguration());
					
				if (keyConfiguration == null)
					continue;
	
				List listPaths = new ArrayList();
				listPaths.add(scope);
				listPaths.add(keyConfiguration);					
				KeyNode.add(tree, keyBinding, State.create(listPaths));
			}
		}
	}

	private void invalidateMode() {
		commandMapForMode = null;
		keySequenceMapForMode = null;
		matchSetForMode = null;
	}

	private void invalidateSolution() {
		solved = false;
		commandMap = null;	
		keySequenceMap = null;
		matchSet = null;
		invalidateMode();
	}
	
	private void invalidateTree() {
		tree = null;
		invalidateSolution();
	}
	
	private void solve() {
		if (!solved) {
			build();
			State[] states = new State[scopes.length];
			Path keyConfiguration = (Path) keyConfigurationMap.get(this.keyConfiguration);
			
			if (keyConfiguration == null)
				keyConfiguration = Path.create();
							
			for (int i = 0; i < scopes.length; i++) {
				Path scope = (Path) scopeMap.get(scopes[i]);
			
				if (scope == null)
					scope = Path.create();

				List paths = new ArrayList();
				paths.add(scope);			
				paths.add(keyConfiguration);					
				states[i] = State.create(paths);
			}
			
			KeyNode.solve(tree, states);
			solved = true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5183.java