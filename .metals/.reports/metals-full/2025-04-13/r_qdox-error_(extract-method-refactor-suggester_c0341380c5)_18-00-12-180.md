error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1394.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1394.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1394.java
text:
```scala
i@@f (comp.compare(array[i], pivotValue) == -1) {

package com.badlogic.gdx.utils;

import java.util.Comparator;


/**
 * Implementation of Tony Hoare's quickselect algorithm.
 * Running time is generally O(n), but worst case is O(n**2)
 * Pivot choice is median of three method, providing better performance
 * than a random pivot for partially sorted data.
 * @author Jon Renner
 */
public class QuickSelect<T> {
	private T[] array;
	private Comparator<? super T> comp;

	public int select(T[] items, Comparator<T> comp, int n, int size) {
		this.array = items;
		this.comp = comp;
		return recursiveSelect(0, size - 1, n);
	}

	private int partition(int left, int right, int pivot) {
		T pivotValue = array[pivot];
		swap(right, pivot);
		int storage = left;
		for (int i = left; i < right; i++) {
			if (comp.compare(array[i], pivotValue) < 0) {
				swap(storage, i);
				storage++;
			}
		}
		swap(right, storage);
		return storage;
	}

	private int recursiveSelect(int left, int right, int k) {
		if (left == right) return left;
		int pivotIndex = medianOfThreePivot(left, right);
		int pivotNewIndex = partition(left, right, pivotIndex);
		int pivotDist = (pivotNewIndex - left) + 1;
		int result;
		if (pivotDist == k) {
			result = pivotNewIndex;
		}
		else if (k < pivotDist) {
			result = recursiveSelect(left, pivotNewIndex - 1, k);
		} else {
			result = recursiveSelect(pivotNewIndex + 1, right, k - pivotDist);
		}
		return result;
	}

	/** Median of Three has the potential to outperform a random pivot, especially for partially sorted arrays */
	private int medianOfThreePivot(int leftIdx, int rightIdx) {
		T left = array[leftIdx];
		int midIdx = (leftIdx + rightIdx) / 2;
		T mid = array[midIdx];
		T right = array[rightIdx];

		// spaghetti median of three algorithm
		// does at most 2 comparisons
		if (comp.compare(left, mid) > 0) {
			if (comp.compare(mid, right) > 0) {
				return midIdx;
			} else if (comp.compare(left, right) > 0) {
				return rightIdx;
			} else {
				return leftIdx;
			}
		} else {
			if (comp.compare(left, right) > 0) {
				return leftIdx;
			} else if (comp.compare(mid, right) > 0) {
				return rightIdx;
			} else {
				return midIdx;
			}
		}
	}

	private void swap(int left, int right) {
		T tmp = array[left];
		array[left] = array[right];
		array[right] = tmp;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1394.java