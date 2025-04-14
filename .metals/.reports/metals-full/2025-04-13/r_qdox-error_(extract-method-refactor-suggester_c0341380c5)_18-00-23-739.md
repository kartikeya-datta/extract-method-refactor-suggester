error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5880.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5880.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5880.java
text:
```scala
K@@eyBindingNode.add(tree, keyBindingDefinition.getKeySequence(), keyBindingDefinition.getActivityId(), keyBindingDefinition.getKeyConfigurationId(), i, keyBindingDefinition.getPlatform(), keyBindingDefinition.getLocale(), keyBindingDefinition.getCommandId());

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

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.ui.internal.commands.api.IKeyBindingDefinition;
import org.eclipse.ui.internal.util.Util;
import org.eclipse.ui.keys.KeySequence;

final class KeyBindingMachine {
		
	private String[] activeContextIds;
	private String[] activeKeyConfigurationIds;
	private String[] activeLocales;
	private String[] activePlatforms;
	private SortedSet[] keyBindings;
	private Map keyBindingsByCommandId;
	private Map keyBindingsByCommandIdForMode;	
	private Map matchesByKeySequence;
	private Map matchesByKeySequenceForMode;
	private KeySequence mode;	
	private boolean solved;
	private SortedMap tree;

	KeyBindingMachine() {
		activeContextIds = new String[0];
		activeKeyConfigurationIds = new String[0];
		activeLocales = new String[0];
		activePlatforms = new String[0];
		keyBindings = new SortedSet[] { new TreeSet(), new TreeSet() };
		mode = KeySequence.getInstance();	
	}

	String[] getActiveContextIds() {
		return (String[]) activeContextIds.clone();
	}		

	String[] getActiveKeyConfigurationIds() {
		return (String[]) activeKeyConfigurationIds.clone();
	}		
	
	String[] getActiveLocales() {
		return (String[]) activeLocales.clone();
	}

	String[] getActivePlatforms() {
		return (String[]) activePlatforms.clone();
	}

	Map getKeyBindingsByCommandId() {
		if (keyBindingsByCommandId == null) {
			solve();
			keyBindingsByCommandId = Collections.unmodifiableMap(KeyBindingNode.getKeyBindingsByCommandId(getMatchesByKeySequence()));				
		}
		
		return keyBindingsByCommandId;
	}
	
	Map getKeyBindingsByCommandIdForMode() {
		if (keyBindingsByCommandIdForMode == null) {
			solve();
			Map tree = KeyBindingNode.find(this.tree, mode);
	
			if (tree == null)
				tree = new TreeMap();

			keyBindingsByCommandIdForMode = Collections.unmodifiableMap(KeyBindingNode.getKeyBindingsByCommandId(getMatchesByKeySequenceForMode()));				
		}
		
		return keyBindingsByCommandIdForMode;
	}

	SortedSet getKeyBindings0() {
		return keyBindings[0];	
	}

	SortedSet getKeyBindings1() {
		return keyBindings[1];	
	}

	Map getMatchesByKeySequence() {
		if (matchesByKeySequence == null) {
			solve();
			matchesByKeySequence = Collections.unmodifiableMap(KeyBindingNode.getMatchesByKeySequence(tree, KeySequence.getInstance()));				
		}
		
		return matchesByKeySequence;
	}

	Map getMatchesByKeySequenceForMode() {
		if (matchesByKeySequenceForMode == null) {
			solve();
			Map tree = KeyBindingNode.find(this.tree, mode);
	
			if (tree == null)
				tree = new TreeMap();
							
			matchesByKeySequenceForMode = Collections.unmodifiableMap(KeyBindingNode.getMatchesByKeySequence(tree, mode));				
		}
		
		return matchesByKeySequenceForMode;
	}

	KeySequence getMode() {
		return mode;	
	}
	
	boolean setActiveContextIds(String[] activeContextIds) {
		if (activeContextIds == null)
			throw new NullPointerException();

		activeContextIds = (String[]) activeContextIds.clone();
			
		if (!Arrays.equals(this.activeContextIds, activeContextIds)) {
			this.activeContextIds = activeContextIds;
			invalidateSolution();
			return true;		
		}
			
		return false;		
	}

	boolean setActiveKeyConfigurationIds(String[] activeKeyConfigurationIds) {
		if (activeKeyConfigurationIds == null)
			throw new NullPointerException();

		activeKeyConfigurationIds = (String[]) activeKeyConfigurationIds.clone();
			
		if (!Arrays.equals(this.activeKeyConfigurationIds, activeKeyConfigurationIds)) {
			this.activeKeyConfigurationIds = activeKeyConfigurationIds;
			invalidateSolution();
			return true;		
		}
			
		return false;		
	}
	
	boolean setActiveLocales(String[] activeLocales) {
		if (activeLocales == null)
			throw new NullPointerException();

		activeLocales = (String[]) activeLocales.clone();
		
		if (!Arrays.equals(this.activeLocales, activeLocales)) {
			this.activeLocales = activeLocales;
			invalidateSolution();
			return true;		
		}
			
		return false;		
	}	

	boolean setActivePlatforms(String[] activePlatforms) {
		if (activePlatforms == null)
			throw new NullPointerException();

		activePlatforms = (String[]) activePlatforms.clone();
		
		if (!Arrays.equals(this.activePlatforms, activePlatforms)) {
			this.activePlatforms = activePlatforms;
			invalidateSolution();
			return true;		
		}
			
		return false;		
	}	

	boolean setKeyBindings0(SortedSet keyBindings0) {
		keyBindings0 = Util.safeCopy(keyBindings0, IKeyBindingDefinition.class);
		
		if (!this.keyBindings[0].equals(keyBindings0)) {
			this.keyBindings[0] = keyBindings0;
			invalidateTree();
			return true;
		}			
			
		return false;		
	}

	boolean setKeyBindings1(SortedSet keyBindings1) {
		keyBindings1 = Util.safeCopy(keyBindings1, IKeyBindingDefinition.class);
		
		if (!this.keyBindings[1].equals(keyBindings1)) {
			this.keyBindings[1] = keyBindings1;
			invalidateTree();
			return true;
		}			
			
		return false;		
	}

	boolean setMode(KeySequence mode) {
		if (mode == null)
			throw new NullPointerException();
			
		if (!this.mode.equals(mode)) {
			this.mode = mode;
			invalidateMode();
			return true;
		}

		return false;		
	}
	
	private void build() {
		if (tree == null) {
			tree = new TreeMap();
		
			for (int i = 0; i < keyBindings.length; i++) {		
				Iterator iterator = keyBindings[i].iterator();
			
				while (iterator.hasNext()) {
					IKeyBindingDefinition keyBindingDefinition = (IKeyBindingDefinition) iterator.next();
					KeyBindingNode.add(tree, keyBindingDefinition.getKeySequence(), keyBindingDefinition.getContextId(), keyBindingDefinition.getKeyConfigurationId(), i, keyBindingDefinition.getPlatform(), keyBindingDefinition.getLocale(), keyBindingDefinition.getCommandId());
				}
			}
		}
	}

	private void invalidateMode() {
		keyBindingsByCommandIdForMode = null;
		matchesByKeySequenceForMode = null;
	}

	private void invalidateSolution() {
		solved = false;
		keyBindingsByCommandId = null;	
		matchesByKeySequence = null;
		invalidateMode();
	}
	
	private void invalidateTree() {
		tree = null;
		invalidateSolution();
	}

	private void solve() {
		if (!solved) {
			build();		
			KeyBindingNode.solve(tree, activeContextIds, activeKeyConfigurationIds, activePlatforms, activeLocales);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5880.java