error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4517.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4517.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4517.java
text:
```scala
s@@witch (matchLevel & PatternLocator.NODE_SET_MASK) {

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.search.matching;

import java.util.ArrayList;

import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.util.HashtableOfLong;
import org.eclipse.jdt.internal.compiler.util.SimpleLookupTable;
import org.eclipse.jdt.internal.core.util.SimpleSet;
import org.eclipse.jdt.internal.core.util.Util;

/**
 * A set of matches and possible matches, which need to be resolved.
 */
public class MatchingNodeSet {

/**
 * Map of matching ast nodes that don't need to be resolved to their accuracy level.
 * Each node is removed as it is reported.
 */
SimpleLookupTable matchingNodes = new SimpleLookupTable(3); // node -> accuracy
private HashtableOfLong matchingNodesKeys = new HashtableOfLong(3); // sourceRange -> node
static Integer EXACT_MATCH = new Integer(SearchMatch.A_ACCURATE);
static Integer POTENTIAL_MATCH = new Integer(SearchMatch.A_INACCURATE);
static Integer ERASURE_MATCH = new Integer(SearchPattern.R_ERASURE_MATCH);

/**
 * Tell whether locators need to resolve or not for current matching node set.
 */
public boolean mustResolve;

/**
 * Set of possible matching ast nodes. They need to be resolved
 * to determine if they really match the search pattern.
 */
SimpleSet possibleMatchingNodesSet = new SimpleSet(7);
private HashtableOfLong possibleMatchingNodesKeys = new HashtableOfLong(7);


public MatchingNodeSet(boolean mustResolvePattern) {
	super();
	mustResolve = mustResolvePattern;
}

public int addMatch(ASTNode node, int matchLevel) {
	switch (matchLevel) {
		case PatternLocator.INACCURATE_MATCH:
			addTrustedMatch(node, POTENTIAL_MATCH);
			break;
		case PatternLocator.POSSIBLE_MATCH:
			addPossibleMatch(node);
			break;
		case PatternLocator.ERASURE_MATCH:
			addTrustedMatch(node, ERASURE_MATCH);
			break;
		case PatternLocator.ACCURATE_MATCH:
			addTrustedMatch(node, EXACT_MATCH);
	}
	return matchLevel;
}
public void addPossibleMatch(ASTNode node) {
	// remove existing node at same position from set
	// (case of recovery that created the same node several time
	// see http://bugs.eclipse.org/bugs/show_bug.cgi?id=29366)
	long key = (((long) node.sourceStart) << 32) + node.sourceEnd;
	ASTNode existing = (ASTNode) this.possibleMatchingNodesKeys.get(key);
	if (existing != null && existing.getClass().equals(node.getClass()))
		this.possibleMatchingNodesSet.remove(existing);

	// add node to set
	this.possibleMatchingNodesSet.add(node);
	this.possibleMatchingNodesKeys.put(key, node);
}
public void addTrustedMatch(ASTNode node, boolean isExact) {
	addTrustedMatch(node, isExact ? EXACT_MATCH : POTENTIAL_MATCH);
	
}
void addTrustedMatch(ASTNode node, Integer level) {
	// remove existing node at same position from set
	// (case of recovery that created the same node several time
	// see http://bugs.eclipse.org/bugs/show_bug.cgi?id=29366)
	long key = (((long) node.sourceStart) << 32) + node.sourceEnd;
	ASTNode existing = (ASTNode) this.matchingNodesKeys.get(key);
	if (existing != null && existing.getClass().equals(node.getClass()))
		this.matchingNodes.removeKey(existing);
	
	// map node to its accuracy level
	this.matchingNodes.put(node, level);
	this.matchingNodesKeys.put(key, node);
}
protected boolean hasPossibleNodes(int start, int end) {
	Object[] nodes = this.possibleMatchingNodesSet.values;
	for (int i = 0, l = nodes.length; i < l; i++) {
		ASTNode node = (ASTNode) nodes[i];
		if (node != null && start <= node.sourceStart && node.sourceEnd <= end)
			return true;
	}
	nodes = this.matchingNodes.keyTable;
	for (int i = 0, l = nodes.length; i < l; i++) {
		ASTNode node = (ASTNode) nodes[i];
		if (node != null && start <= node.sourceStart && node.sourceEnd <= end)
			return true;
	}
	return false;
}
/**
 * Returns the matching nodes that are in the given range in the source order.
 */
protected ASTNode[] matchingNodes(int start, int end) {
	ArrayList nodes = null;
	Object[] keyTable = this.matchingNodes.keyTable;
	for (int i = 0, l = keyTable.length; i < l; i++) {
		ASTNode node = (ASTNode) keyTable[i];
		if (node != null && start <= node.sourceStart && node.sourceEnd <= end) {
			if (nodes == null) nodes = new ArrayList();
			nodes.add(node);
		}
	}
	if (nodes == null) return null;

	ASTNode[] result = new ASTNode[nodes.size()];
	nodes.toArray(result);

	// sort nodes by source starts
	Util.Comparer comparer = new Util.Comparer() {
		public int compare(Object o1, Object o2) {
			return ((ASTNode) o1).sourceStart - ((ASTNode) o2).sourceStart;
		}
	};
	Util.sort(result, comparer);
	return result;
}
public Object removePossibleMatch(ASTNode node) {
	long key = (((long) node.sourceStart) << 32) + node.sourceEnd;
	ASTNode existing = (ASTNode) this.possibleMatchingNodesKeys.get(key);
	if (existing == null) return null;

	this.possibleMatchingNodesKeys.put(key, null);
	return this.possibleMatchingNodesSet.remove(node);
}
public Object removeTrustedMatch(ASTNode node) {
	long key = (((long) node.sourceStart) << 32) + node.sourceEnd;
	ASTNode existing = (ASTNode) this.matchingNodesKeys.get(key);
	if (existing == null) return null;

	this.matchingNodesKeys.put(key, null);
	return this.matchingNodes.removeKey(node);
}
public String toString() {
	// TODO (jerome) should show both tables
	StringBuffer result = new StringBuffer();
	result.append("Exact matches:"); //$NON-NLS-1$
	Object[] keyTable = this.matchingNodes.keyTable;
	Object[] valueTable = this.matchingNodes.valueTable;
	for (int i = 0, l = keyTable.length; i < l; i++) {
		ASTNode node = (ASTNode) keyTable[i];
		if (node == null) continue;
		result.append("\n\t"); //$NON-NLS-1$
		switch (((Integer)valueTable[i]).intValue()) {
			case SearchMatch.A_ACCURATE:
				result.append("ACCURATE_MATCH: "); //$NON-NLS-1$
				break;
			case SearchMatch.A_INACCURATE:
				result.append("INACCURATE_MATCH: "); //$NON-NLS-1$
				break;
			case SearchPattern.R_ERASURE_MATCH:
				result.append("ERASURE_MATCH: "); //$NON-NLS-1$
				break;
		}
		node.print(0, result);
	}

	result.append("\nPossible matches:"); //$NON-NLS-1$
	Object[] nodes = this.possibleMatchingNodesSet.values;
	for (int i = 0, l = nodes.length; i < l; i++) {
		ASTNode node = (ASTNode) nodes[i];
		if (node == null) continue;
		result.append("\nPOSSIBLE_MATCH: "); //$NON-NLS-1$
		node.print(0, result);
	}
	return result.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4517.java