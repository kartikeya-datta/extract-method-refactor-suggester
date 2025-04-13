error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2991.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2991.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2991.java
text:
```scala
I@@tem sub = group.getSubroutine();

/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.xtend.profiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.xtend.profiler.profilermodel.CallGroup;
import org.eclipse.xtend.profiler.profilermodel.Cycle;
import org.eclipse.xtend.profiler.profilermodel.Item;
import org.eclipse.xtend.profiler.profilermodel.ModelFactory;
import org.eclipse.xtend.profiler.profilermodel.ProfilingResult;


/**
 * Detects cycles in call graph of a given profiling result. This implementation uses
 * a slightly modified Tarjan's strongly connected components (SCC) algorithm to accept
 * trivial SCCs only if there is an reflexive edge (self recursion).
 * 
 * @see http://en.wikipedia.org/wiki/Tarjan's_strongly_connected_components_algorithm
 * @author Heiko Behrens - Initial contribution and API
 */
public class CycleDetector {
	private final ProfilingResult profilingResult;

	public CycleDetector(ProfilingResult result) {
		profilingResult = result;
	}
		
	private void tarjan(Item item) {
		visited.add(item);
		setLowLink(item, getIndex(item));
		stack.add(0, item);
		for (CallGroup group : item.getSubroutines()) {
			Item sub = (Item)group.getSubroutine();
			if (!hasBeenVisited(sub)) {
				tarjan(sub);
				setLowLinkIfSmaller(item, getLowLink(sub));
			} else if(stack.contains(sub))
				setLowLinkIfSmaller(item, getIndex(sub));
		}
		
		if(getLowLink(item) == getIndex(item))
			buildCycleFromStack(item);
	}

	private void buildCycleFromStack(Item item) {
		List<Item> items = new ArrayList<Item>();
		while (true) {
			Item v = stack.remove(0);
			// use inverted order to support human's understanding
			items.add(0, v);
			if (item.equals(v))
				break;
		}
		if (items.size() > 1 || isSelfRecursion(item)) {
			Cycle cycle = ModelFactory.eINSTANCE.createCycle();
			cycle.getItems().addAll(items);
			// use inverted order to support human's understanding
			profilingResult.getCycles().add(0, cycle);
		}
	}
	
	private boolean isSelfRecursion(Item item) {
		for (CallGroup g : item.getSubroutines())
			if (item.equals(g.getSubroutine()))
				return true;
		return false;
	}

	List<Item> visited = new ArrayList<Item>();
	List<Item> stack = new ArrayList<Item>();
	Map<Item, Integer> lowLink = new HashMap<Item, Integer>(); 
	
	public void detectCycles() {
		profilingResult.getCycles().clear();
		Set<Item> toBeVisited = new LinkedHashSet<Item>(profilingResult.getItems());
		while(!toBeVisited.isEmpty()) {
			Item item = toBeVisited.iterator().next();
			tarjan(item);	
			toBeVisited.removeAll(visited);
		}
	}
	
	private void setLowLink(Item item, int value) {
		lowLink.put(item, value);
	}
	
	private void setLowLinkIfSmaller(Item item, int potentiallySmallerValue) {
		setLowLink(item, Math.min(getLowLink(item), potentiallySmallerValue));
	}
	
	private int getLowLink(Item item) {
		return lowLink.get(item);
	}

	private boolean hasBeenVisited(Item item) {
		return visited.contains(item);
	}
	
	private int getIndex(Item item) {
		if(!hasBeenVisited(item))
			throw new IllegalStateException("Item has not been visited, yet.");
		return visited.indexOf(item);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2991.java