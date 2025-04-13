error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5954.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5954.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5954.java
text:
```scala
k@@eySequenceBindingDefinition.getContextId(),

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
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.ui.internal.util.Util;
import org.eclipse.ui.keys.KeySequence;

final class KeySequenceBindingMachine {

	private String[] activeActivityIds;
	private String[] activeKeyConfigurationIds;
	private String[] activeLocales;
	private String[] activePlatforms;
	private List[] keySequenceBindings;
	private Map keySequenceBindingsByCommandId;
	private Map matchesByKeySequence;
	private boolean solved;
	private SortedMap tree;

	KeySequenceBindingMachine() {
		activeActivityIds = new String[0];
		activeKeyConfigurationIds = new String[0];
		activeLocales = new String[0];
		activePlatforms = new String[0];
		keySequenceBindings = new List[] { new ArrayList(), new ArrayList()};
	}

	String[] getActiveActivityIds() {
		return (String[]) activeActivityIds.clone();
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

	List getKeySequenceBindings0() {
		return keySequenceBindings[0];
	}

	List getKeySequenceBindings1() {
		return keySequenceBindings[1];
	}

	Map getKeySequenceBindingsByCommandId() {
		if (keySequenceBindingsByCommandId == null) {
			validateSolution();
			keySequenceBindingsByCommandId =
				Collections.unmodifiableMap(
					KeySequenceBindingNode.getKeySequenceBindingsByCommandId(
						getMatchesByKeySequence()));
		}

		return keySequenceBindingsByCommandId;
	}

	Map getMatchesByKeySequence() {
		if (matchesByKeySequence == null) {
			validateSolution();
			matchesByKeySequence =
				Collections.unmodifiableMap(
					KeySequenceBindingNode.getMatchesByKeySequence(
						tree,
						KeySequence.getInstance()));
		}

		return matchesByKeySequence;
	}

	private void invalidateSolution() {
		solved = false;
		keySequenceBindingsByCommandId = null;
		matchesByKeySequence = null;
	}

	private void invalidateTree() {
		tree = null;
		invalidateSolution();
	}

	boolean setActiveActivityIds(String[] activeActivityIds) {
		if (activeActivityIds == null)
			throw new NullPointerException();

		activeActivityIds = (String[]) activeActivityIds.clone();

		if (!Arrays.equals(this.activeActivityIds, activeActivityIds)) {
			this.activeActivityIds = activeActivityIds;
			invalidateSolution();
			return true;
		}

		return false;
	}

	boolean setActiveKeyConfigurationIds(String[] activeKeyConfigurationIds) {
		if (activeKeyConfigurationIds == null)
			throw new NullPointerException();

		activeKeyConfigurationIds =
			(String[]) activeKeyConfigurationIds.clone();

		if (!Arrays
			.equals(
				this.activeKeyConfigurationIds,
				activeKeyConfigurationIds)) {
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

	boolean setKeySequenceBindings0(List keySequenceBindings0) {
		keySequenceBindings0 =
			Util.safeCopy(
				keySequenceBindings0,
				IKeySequenceBindingDefinition.class);

		if (!this.keySequenceBindings[0].equals(keySequenceBindings0)) {
			this.keySequenceBindings[0] = keySequenceBindings0;
			invalidateTree();
			return true;
		}

		return false;
	}

	boolean setKeySequenceBindings1(List keySequenceBindings1) {
		keySequenceBindings1 =
			Util.safeCopy(
				keySequenceBindings1,
				IKeySequenceBindingDefinition.class);

		if (!this.keySequenceBindings[1].equals(keySequenceBindings1)) {
			this.keySequenceBindings[1] = keySequenceBindings1;
			invalidateTree();
			return true;
		}

		return false;
	}

	private void validateSolution() {
		if (!solved) {
			validateTree();
			KeySequenceBindingNode.solve(
				tree,
				activeActivityIds,
				activeKeyConfigurationIds,
				activePlatforms,
				activeLocales);
			solved = true;
		}
	}

	private void validateTree() {
		if (tree == null) {
			tree = new TreeMap();

			for (int i = 0; i < keySequenceBindings.length; i++) {
				Iterator iterator = keySequenceBindings[i].iterator();

				while (iterator.hasNext()) {
					IKeySequenceBindingDefinition keySequenceBindingDefinition =
						(IKeySequenceBindingDefinition) iterator.next();
					KeySequenceBindingNode.add(
						tree,
						keySequenceBindingDefinition.getKeySequence(),
						keySequenceBindingDefinition.getActivityId(),
						keySequenceBindingDefinition.getKeyConfigurationId(),
						i,
						keySequenceBindingDefinition.getPlatform(),
						keySequenceBindingDefinition.getLocale(),
						keySequenceBindingDefinition.getCommandId());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5954.java