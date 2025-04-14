error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6329.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6329.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6329.java
text:
```scala
public v@@oid sort (Comparator<T> comparator) {

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.utils;

import java.util.Comparator;

/** Guarantees that array entries provided by {@link #begin()} between indexes 0 and {@link #size} at the time begin was called
 * will not be modified until {@link #end()} is called. If modification of the SnapshotArray occurs between begin/end, the backing
 * array is copied prior to the modification, ensuring that the backing array that was returned by {@link #begin()} is unaffected.
 * To avoid allocation, an attempt is made to reuse any extra array created as a result of this copy on subsequent copies.
 * <p>
 * It is suggested iteration be done in this specific way:
 * 
 * <pre>
 * SnapshotArray array = new SnapshotArray();
 * // ...
 * Object[] items = array.begin();
 * for (int i = 0, n = array.size; i &lt; n; i++) {
 * 	Object item = items[i];
 * 	// ...
 * }
 * array.end();
 * </pre>
 * @author Nathan Sweet */
public class SnapshotArray<T> extends Array<T> {
	private T[] snapshot, recycled;
	private int snapshots;

	public SnapshotArray () {
		super();
	}

	public SnapshotArray (Array array) {
		super(array);
	}

	public SnapshotArray (boolean ordered, int capacity, Class arrayType) {
		super(ordered, capacity, arrayType);
	}

	public SnapshotArray (boolean ordered, int capacity) {
		super(ordered, capacity);
	}

	public SnapshotArray (boolean ordered, T[] array, int startIndex, int count) {
		super(ordered, array, startIndex, count);
	}

	public SnapshotArray (Class arrayType) {
		super(arrayType);
	}

	public SnapshotArray (int capacity) {
		super(capacity);
	}

	public SnapshotArray (T[] array) {
		super(array);
	}

	/** Returns the backing array, which is guaranteed to not be modified before {@link #end()}. */
	public T[] begin () {
		modified();
		snapshot = items;
		snapshots++;
		return items;
	}

	/** Releases the guarantee that the array returned by {@link #begin()} won't be modified. */
	public void end () {
		snapshots = Math.max(0, snapshots - 1);
		if (snapshot == null) return;
		if (snapshot != items && snapshots == 0) {
			// The backing array was copied, keep around the old array.
			recycled = snapshot;
			for (int i = 0, n = recycled.length; i < n; i++)
				recycled[i] = null;
		}
		snapshot = null;
	}

	private void modified () {
		if (snapshot == null || snapshot != items) return;
		// Snapshot is in use, copy backing array to recycled array or create new backing array.
		if (recycled != null && recycled.length >= size) {
			System.arraycopy(items, 0, recycled, 0, size);
			items = recycled;
			recycled = null;
		} else
			resize(items.length);
	}

	public void set (int index, T value) {
		modified();
		super.set(index, value);
	}

	public void insert (int index, T value) {
		modified();
		super.insert(index, value);
	}

	public void swap (int first, int second) {
		modified();
		super.swap(first, second);
	}

	public boolean removeValue (T value, boolean identity) {
		modified();
		return super.removeValue(value, identity);
	}

	public T removeIndex (int index) {
		modified();
		return super.removeIndex(index);
	}

	public T pop () {
		modified();
		return super.pop();
	}

	public void clear () {
		modified();
		super.clear();
	}

	public void sort () {
		modified();
		super.sort();
	}

	public void sort (Comparator<? super T> comparator) {
		modified();
		super.sort(comparator);
	}

	public void reverse () {
		modified();
		super.reverse();
	}

	public void shuffle () {
		modified();
		super.shuffle();
	}

	public void truncate (int newSize) {
		modified();
		super.truncate(newSize);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6329.java