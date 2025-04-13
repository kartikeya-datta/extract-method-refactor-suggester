error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5595.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5595.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5595.java
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

final class KeyNode {

	static void add(SortedMap tree, KeyBinding binding, State state) {
		List keyStrokes = binding.getKeySequence().getKeyStrokes();		
		SortedMap root = tree;
		KeyNode node = null;
	
		for (int i = 0; i < keyStrokes.size(); i++) {
			KeyStroke keyStroke = (KeyStroke) keyStrokes.get(i);
			node = (KeyNode) root.get(keyStroke);
			
			if (node == null) {
				node = new KeyNode();	
				root.put(keyStroke, node);
			}
			
			root = node.childMap;
		}

		if (node != null)
			node.add(binding, state);
	}

	static SortedMap find(SortedMap tree, KeySequence prefix) {	
		Iterator iterator = prefix.getKeyStrokes().iterator();
	
		while (iterator.hasNext()) {
			KeyNode node = (KeyNode) tree.get(iterator.next());
			
			if (node == null)
				return null;
				
			tree = node.childMap;
		}		
		
		return tree;			
	}

	static void remove(SortedMap tree, KeyBinding binding, State state) {
		List keyStrokes = binding.getKeySequence().getKeyStrokes();		
		SortedMap root = tree;
		KeyNode node = null;
	
		for (int i = 0; i < keyStrokes.size(); i++) {
			KeyStroke keyStroke = (KeyStroke) keyStrokes.get(i);
			node = (KeyNode) root.get(keyStroke);
			
			if (node == null)
				break;
			
			root = node.childMap;
		}

		if (node != null)
			node.remove(binding, state);
	}

	static void solve(SortedMap tree, State[] stack) {
		Iterator iterator = tree.values().iterator();	
		
		while (iterator.hasNext()) {
			KeyNode node = (KeyNode) iterator.next();			
			node.match = solveStateMap(node.stateMap, stack);
			solve(node.childMap, stack);								
			node.bestChildMatch = null;			
			Iterator iterator2 = node.childMap.values().iterator();	
			
			while (iterator2.hasNext()) {
				KeyNode child = (KeyNode) iterator2.next();
				KeyBindingMatch childMatch = child.match;				
				
				if (childMatch != null && (node.bestChildMatch == null || childMatch.getValue() < node.bestChildMatch.getValue())) 
					node.bestChildMatch = childMatch;
			}
		}		
	}

	static KeyBinding solveActionMap(Map actionMap) {	
		Set bindingSet = (Set) actionMap.get(null);
			
		if (bindingSet == null) {
			bindingSet = new TreeSet();
			Iterator iterator = actionMap.values().iterator();
		
			while (iterator.hasNext())
				bindingSet.addAll((Set) iterator.next());
		}

		return bindingSet.size() == 1 ? (KeyBinding) bindingSet.iterator().next() : null;		
	}

	static KeyBinding solvePluginMap(Map pluginMap) {	
		Map actionMap = (Map) pluginMap.get(null);
		
		if (actionMap != null)
			return solveActionMap(actionMap);
		else {
			Set bindingSet = new TreeSet();
			Iterator iterator = pluginMap.values().iterator();
		
			while (iterator.hasNext())
				bindingSet.add(solveActionMap((Map) iterator.next()));	
			
			return bindingSet.size() == 1 ? (KeyBinding) bindingSet.iterator().next() : null;	
		}			
	}
	
	static KeyBindingMatch solveStateMap(SortedMap stateMap, State state) {
		KeyBindingMatch match = null;
		Iterator iterator = stateMap.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			State testState = (State) entry.getKey();
			Map testPluginMap = (Map) entry.getValue();

			if (testPluginMap != null) {
				KeyBinding testBinding = solvePluginMap(testPluginMap);
			
				if (testBinding != null) {
					int testMatch = testState.match(state);
					
					if (testMatch >= 0) {
						if (testMatch == 0)
							return KeyBindingMatch.create(testBinding, 0);
						else if (match == null || testMatch < match.getValue())
							match = KeyBindingMatch.create(testBinding, testMatch);
					}
				}
			}
		}
			
		return match;	
	}

	static KeyBindingMatch solveStateMap(SortedMap stateMap, State[] stack) {
		for (int i = 0; i < stack.length; i++) {
			KeyBindingMatch match = solveStateMap(stateMap, stack[i]);
				
			if (match != null)
				return match;
		}
		
		return null;
	}

	static Map toActionMap(Set matches) {
		Map actionMap = new HashMap();
		Iterator iterator = matches.iterator();
		
		while (iterator.hasNext()) {
			KeyBindingMatch match = (KeyBindingMatch) iterator.next();
			String action = match.getKeyBinding().getCommand();
			Set matchSet = (Set) actionMap.get(action);
			
			if (matchSet == null) {
				matchSet = new TreeSet();
				actionMap.put(action, matchSet);
			}

			matchSet.add(match);
		}
		
		return actionMap;
	}

	static void toBindingSet(SortedMap tree, Set bindingSet) {
		Iterator iterator = tree.values().iterator();	
			
		while (iterator.hasNext())
			toBindingSet((KeyNode) iterator.next(), bindingSet);
	}

	static void toBindingSet(KeyNode node, Set bindingSet) {
		toBindingSet(node.childMap, bindingSet);		
		Iterator iterator = node.stateMap.values().iterator();
		
		while (iterator.hasNext()) {
			Map pluginMap = (Map) iterator.next();
			Iterator iterator2 = pluginMap.values().iterator();
			
			while (iterator2.hasNext()) {
				Map actionMap = (Map) iterator2.next();
				Iterator iterator3 = actionMap.values().iterator();
				
				while (iterator3.hasNext())
					bindingSet.addAll((Set) iterator3.next());
			}
		}
	}

	static void toMatchSet(SortedMap tree, SortedSet matchSet) {
		Iterator iterator = tree.values().iterator();	
			
		while (iterator.hasNext())
			toMatchSet((KeyNode) iterator.next(), matchSet);		
	}

	static void toMatchSet(KeyNode node, SortedSet matchSet) {
		if (node.bestChildMatch != null && (node.match == null || node.bestChildMatch.getValue() < node.match.getValue()))
			toMatchSet(node.childMap, matchSet);
		else if (node.match != null)
			matchSet.add(node.match);
	}

	static Map toKeySequenceMap(Set matches) {
		Map keySequenceMap = new TreeMap();
		Iterator iterator = matches.iterator();
		
		while (iterator.hasNext()) {
			KeyBindingMatch match = (KeyBindingMatch) iterator.next();
			KeySequence keySequence = match.getKeyBinding().getKeySequence();
			Set matchSet = (Set) keySequenceMap.get(keySequence);
			
			if (matchSet == null) {
				matchSet = new TreeSet();
				keySequenceMap.put(keySequence, matchSet);
			}

			matchSet.add(match);
		}
		
		return keySequenceMap;
	}

	KeyBindingMatch bestChildMatch = null;
	SortedMap childMap = new TreeMap();	
	KeyBindingMatch match = null;
	SortedMap stateMap = new TreeMap();
	
	private KeyNode() {
		super();
	}

	void add(KeyBinding binding, State state) {			
		Map pluginMap = (Map) stateMap.get(state);
		
		if (pluginMap == null) {
			pluginMap = new HashMap();	
			stateMap.put(state, pluginMap);
		}

		String plugin = binding.getPlugin();
		Map actionMap = (Map) pluginMap.get(plugin);
		
		if (actionMap == null) {
			actionMap = new HashMap();
			pluginMap.put(plugin, actionMap);
		}	
	
		String action = binding.getCommand();
		Set bindingSet = (Set) actionMap.get(action);
		
		if (bindingSet == null) {
			bindingSet = new TreeSet();
			actionMap.put(action, bindingSet);
		}
		
		bindingSet.add(binding);
	}

	void remove(KeyBinding binding, State state) {		
		Map pluginMap = (Map) stateMap.get(state);
		
		if (pluginMap != null) {
			String plugin = binding.getPlugin();
			Map actionMap = (Map) pluginMap.get(plugin);
			
			if (actionMap != null) {
				String action = binding.getCommand();
				Set bindingSet = (Set) actionMap.get(action);
				
				if (bindingSet != null) {
					bindingSet.remove(binding);
					
					if (bindingSet.isEmpty()) {
						actionMap.remove(action);
						
						if (actionMap.isEmpty()) {
							pluginMap.remove(plugin);	
							
							if (pluginMap.isEmpty())
								stateMap.remove(state);	
						}						
					}
				}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5595.java