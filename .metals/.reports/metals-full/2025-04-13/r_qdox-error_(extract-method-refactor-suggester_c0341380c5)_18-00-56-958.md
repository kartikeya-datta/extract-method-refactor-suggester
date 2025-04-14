error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1725.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1725.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1725.java
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.swt.SWT;

public class KeyManager {

	private final static String KEY_SEQUENCE_SEPARATOR = ", "; //$NON-NLS-1$
	private final static String LOCALE_SEPARATOR = "_"; //$NON-NLS-1$
	private final static java.util.Locale SYSTEM_LOCALE = java.util.Locale.getDefault();
	private final static String SYSTEM_PLATFORM = SWT.getPlatform(); // "carbon"

	private static KeyManager instance;

	public static KeyManager getInstance() {
		if (instance == null)
			instance = new KeyManager();
			
		return instance;	
	}

	private static SortedMap buildPathMap(SortedMap itemMap) {
		SortedMap pathMap = new TreeMap();
		Iterator iterator = itemMap.keySet().iterator();

		while (iterator.hasNext()) {
			String id = (String) iterator.next();
			
			if (id != null) {			
				Path path = pathForItem(id, itemMap);
			
				if (path != null)
					pathMap.put(id, path);
			}			
		}

		return pathMap;		
	}

	private static Path pathForItem(String id, Map itemMap) {
		Path path = null;

		if (id != null) {
			List pathItems = new ArrayList();

			while (id != null) {	
				if (pathItems.contains(id))
					return null;
							
				Item item = (Item) itemMap.get(id);
				
				if (item == null)
					return null;
							
				pathItems.add(0, id);
				id = item.getParent();
			}
		
			path = Path.create(pathItems);
		}
		
		return path;			
	}	

	private static Path pathForLocale(String locale) {
		Path path = null;

		if (locale != null) {
			List pathItems = new ArrayList();				
			locale = locale.trim();
			
			if (locale.length() > 0) {
				StringTokenizer st = new StringTokenizer(locale, LOCALE_SEPARATOR);
						
				while (st.hasMoreElements()) {
					String value = ((String) st.nextElement()).trim();
					
					if (value != null)
						pathItems.add(value);
				}
			}

			path = Path.create(pathItems);
		}
			
		return path;		
	}

	private static Path pathForPlatform(String platform) {
		Path path = null;

		if (platform != null) {
			List pathItems = new ArrayList();				
			platform = platform.trim();
			
			if (platform.length() > 0)
				pathItems.add(platform);

			path = Path.create(pathItems);
		}
			
		return path;		
	}

	static SortedSet solveRegionalKeyBindingSet(SortedSet regionalBindingSet, State[] states) {
		
		class Key implements Comparable {		
		
			private final static int HASH_INITIAL = 17;
			private final static int HASH_FACTOR = 27;
			
			KeySequence keySequence;
			String configuration;
			String scope;

			public int compareTo(Object object) {
				Key key = (Key) object;
				int compareTo = keySequence.compareTo(key.keySequence);
		
				if (compareTo == 0) {
					compareTo = configuration.compareTo(key.configuration);
		
					if (compareTo == 0)
						compareTo = scope.compareTo(key.scope);
				}
				
				return compareTo;
			}
		
			public boolean equals(Object object) {
				if (!(object instanceof Key))
					return false;
				
				Key key = (Key) object;
				return keySequence.equals(key.keySequence) && configuration.equals(key.configuration) && scope.equals(key.scope);
			}

			public int hashCode() {
				int result = HASH_INITIAL;
				result = result * HASH_FACTOR + keySequence.hashCode();		
				result = result * HASH_FACTOR + configuration.hashCode();		
				result = result * HASH_FACTOR + scope.hashCode();		
				return result;
			}
		}

		SortedSet bindingSet = new TreeSet();
		Map map = new TreeMap();
		Iterator iterator = regionalBindingSet.iterator();
		
		while (iterator.hasNext()) {
			RegionalKeyBinding regionalBinding = (RegionalKeyBinding) iterator.next();
			KeyBinding binding = regionalBinding.getKeyBinding();
			List pathItems = new ArrayList();
			pathItems.add(pathForPlatform(regionalBinding.getPlatform()));
			pathItems.add(pathForLocale(regionalBinding.getLocale()));
			State state = State.create(pathItems);
			Key key = new Key();
			key.keySequence = binding.getKeySequence();
			key.configuration = binding.getKeyConfiguration();
			key.scope = binding.getScope();
			Map stateMap = (Map) map.get(key);
			
			if (stateMap == null) {
				stateMap = new TreeMap();
				map.put(key, stateMap);
			}
			
			List bindings = (List) stateMap.get(state);
			
			if (bindings == null) {
				bindings = new ArrayList();
				stateMap.put(state, bindings);	
			}			
		
			bindings.add(binding);		
		}

		Iterator iterator2 = map.values().iterator();

		while (iterator2.hasNext()) {
			Map stateMap = (Map) iterator2.next();				
			int bestMatch = -1;
			List bindings = null;
			Iterator iterator3 = stateMap.entrySet().iterator();

			while (iterator3.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator3.next();
				State testState = (State) entry.getKey();
				List testBindingSet = (List) entry.getValue();
							
				int testMatch = testState.match(states[0]);
				
				if (testMatch >= 0) {
					if (bindings == null || testMatch < bestMatch) {
						bindings = testBindingSet;
						bestMatch = testMatch;
					}
					
					if (bestMatch == 0)
						break;
				}
			}				

			if (bindings != null) {
				Iterator iterator4 = bindings.iterator();
				
				while (iterator4.hasNext()) {
					KeyBinding binding = (KeyBinding) iterator4.next();
					bindingSet.add(KeyBinding.create(binding.getCommand(), binding.getKeyConfiguration(), binding.getKeySequence(), binding.getPlugin(),
						binding.getRank() + bestMatch, binding.getScope()));								
				}				
			}
		}					

		return bindingSet;
	}

	/*
	private SortedSet solveRegionalBindingSet(SortedSet regionalBindingSet, State[] states) {
		SortedMap tree = new TreeMap();
		Iterator iterator = regionalBindingSet.iterator();
		
		while (iterator.hasNext()) {
			RegionalBinding regionalBinding = (RegionalBinding) iterator.next();
			Binding binding = regionalBinding.getBinding();
			List pathItems = new ArrayList();
			pathItems.add(pathForPlatform(regionalBinding.getPlatform()));
			pathItems.add(pathForLocale(regionalBinding.getLocale()));
			Node.add(tree, binding, State.create(pathItems));
		}

		Node.solve(tree, states);
		SortedSet matchSet = new TreeSet();
		Node.toMatchSet(tree, matchSet);
		SortedSet bindingSet = new TreeSet();
		iterator = matchSet.iterator();
		
		while (iterator.hasNext())
			bindingSet.add(((Match) iterator.next()).getBinding());							

		return bindingSet;
	}
	*/

	static Path systemLocale() {
		return SYSTEM_LOCALE != null ? pathForLocale(SYSTEM_LOCALE.toString()) : null;
	}

	static Path systemPlatform() {
		return pathForPlatform(SYSTEM_PLATFORM);
	}

	private KeyMachine keyMachine;	
	
	private KeyManager() {
		super();
		keyMachine = KeyMachine.create();
		update();		
	}

	public KeyMachine getKeyMachine() {
		return keyMachine;
	}

	public String getTextForAction(String action)
		throws IllegalArgumentException {
		if (action == null)
			throw new IllegalArgumentException();					

		String text = null;
		Map actionMap = getKeyMachine().getActionMap();		
		SortedSet matchSet = (SortedSet) actionMap.get(action);
		
		if (matchSet != null && !matchSet.isEmpty()) {
			KeyBindingMatch match = (KeyBindingMatch) matchSet.first();
		
			if (match != null)
				text = match.getKeyBinding().getKeySequence().toString();
		}
		
		return text;
	}

	public void update() {
		List pathItems = new ArrayList();
		pathItems.add(systemPlatform());
		pathItems.add(systemLocale());
		State[] states = new State[] { State.create(pathItems) };	

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

		List registryKeyConfigurations = new ArrayList();
		registryKeyConfigurations.addAll(coreRegistry.getKeyConfigurations());
		registryKeyConfigurations.addAll(localRegistry.getKeyConfigurations());
		registryKeyConfigurations.addAll(preferenceRegistry.getKeyConfigurations());
		SortedMap registryKeyConfigurationMap = Item.sortedMap(registryKeyConfigurations);
		SortedMap keyConfigurationMap = buildPathMap(registryKeyConfigurationMap);
		
		List registryScopes = new ArrayList();
		registryScopes.addAll(coreRegistry.getScopes());
		registryScopes.addAll(localRegistry.getScopes());
		registryScopes.addAll(preferenceRegistry.getScopes());
		SortedMap registryScopeMap = Item.sortedMap(registryScopes);
		SortedMap scopeMap = buildPathMap(registryScopeMap);

		SortedSet coreRegistryKeyBindingSet = new TreeSet();
		coreRegistryKeyBindingSet.addAll(coreRegistry.getKeyBindings());	
		SortedSet coreRegistryRegionalKeyBindingSet = new TreeSet();
		coreRegistryRegionalKeyBindingSet.addAll(coreRegistry.getRegionalKeyBindings());
		coreRegistryKeyBindingSet.addAll(solveRegionalKeyBindingSet(coreRegistryRegionalKeyBindingSet, states));

		SortedSet localRegistryKeyBindingSet = new TreeSet();
		localRegistryKeyBindingSet.addAll(localRegistry.getKeyBindings());	
		SortedSet localRegistryRegionalKeyBindingSet = new TreeSet();
		localRegistryRegionalKeyBindingSet.addAll(localRegistry.getRegionalKeyBindings());
		localRegistryKeyBindingSet.addAll(solveRegionalKeyBindingSet(localRegistryRegionalKeyBindingSet, states));
		
		SortedSet preferenceRegistryKeyBindingSet = new TreeSet();
		preferenceRegistryKeyBindingSet.addAll(preferenceRegistry.getKeyBindings());	
		
		SortedSet keyBindingSet = new TreeSet();		
		keyBindingSet.addAll(coreRegistryKeyBindingSet);
		keyBindingSet.addAll(localRegistryKeyBindingSet);
		keyBindingSet.addAll(preferenceRegistryKeyBindingSet);

		keyMachine.setKeyConfigurationMap(Collections.unmodifiableSortedMap(keyConfigurationMap));
		keyMachine.setScopeMap(Collections.unmodifiableSortedMap(scopeMap));
		keyMachine.setKeyBindingSet(Collections.unmodifiableSortedSet(keyBindingSet));		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1725.java