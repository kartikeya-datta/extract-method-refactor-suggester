error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1296.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1296.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1296.java
text:
```scala
public v@@oid sort (Comparator<? super T> comparator) {

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

/** Queues any removals done after {@link #begin()} is called to occur once {@link #end()} is called. This can allow code out of
 * your control to remove items without affecting iteration. Between begin and end, most mutator methods will throw
 * IllegalStateException. Only {@link #removeIndex(int)}, {@link #removeValue(Object, boolean)}, and add methods are allowed.
 * <p>
 * Code using this class must not rely on items being removed immediately. Consider using {@link SnapshotArray} if this is a
 * problem..
 * @author Nathan Sweet */
public class DelayedRemovalArray<T> extends Array<T> {
	private boolean iterating;
	private IntArray remove = new IntArray(0);

	public DelayedRemovalArray () {
		super();
	}

	public DelayedRemovalArray (Array array) {
		super(array);
	}

	public DelayedRemovalArray (boolean ordered, int capacity, Class arrayType) {
		super(ordered, capacity, arrayType);
	}

	public DelayedRemovalArray (boolean ordered, int capacity) {
		super(ordered, capacity);
	}

	public DelayedRemovalArray (boolean ordered, T[] array, int startIndex, int count) {
		super(ordered, array, startIndex, count);
	}

	public DelayedRemovalArray (Class arrayType) {
		super(arrayType);
	}

	public DelayedRemovalArray (int capacity) {
		super(capacity);
	}

	public DelayedRemovalArray (T[] array) {
		super(array);
	}

	public void begin () {
		iterating = true;
	}

	public void end () {
		iterating = false;
		for (int i = 0, n = remove.size; i < n; i++)
			removeIndex(remove.pop());
	}

	private void remove (int index) {
		for (int i = 0, n = remove.size; i < n; i++) {
			int removeIndex = remove.get(i);
			if (index == removeIndex) return;
			if (index < removeIndex) {
				remove.insert(i, index);
				return;
			}
		}
		remove.add(index);
	}

	public boolean removeValue (T value, boolean identity) {
		if (iterating) {
			int index = indexOf(value, identity);
			if (index == -1) return false;
			remove(index);
			return true;
		}
		return super.removeValue(value, identity);
	}

	public T removeIndex (int index) {
		if (iterating) {
			remove(index);
			return get(index);
		}
		return super.removeIndex(index);
	}

	public void set (int index, T value) {
		if (iterating) throw new IllegalStateException("Invalid between begin/end.");
		super.set(index, value);
	}

	public void insert (int index, T value) {
		if (iterating) throw new IllegalStateException("Invalid between begin/end.");
		super.insert(index, value);
	}

	public void swap (int first, int second) {
		if (iterating) throw new IllegalStateException("Invalid between begin/end.");
		super.swap(first, second);
	}

	public T pop () {
		if (iterating) throw new IllegalStateException("Invalid between begin/end.");
		return super.pop();
	}

	public void clear () {
		if (iterating) throw new IllegalStateException("Invalid between begin/end.");
		super.clear();
	}

	public void sort () {
		if (iterating) throw new IllegalStateException("Invalid between begin/end.");
		super.sort();
	}

	public void sort (Comparator<T> comparator) {
		if (iterating) throw new IllegalStateException("Invalid between begin/end.");
		super.sort(comparator);
	}

	public void reverse () {
		if (iterating) throw new IllegalStateException("Invalid between begin/end.");
		super.reverse();
	}

	public void shuffle () {
		if (iterating) throw new IllegalStateException("Invalid between begin/end.");
		super.shuffle();
	}

	public void truncate (int newSize) {
		if (iterating) throw new IllegalStateException("Invalid between begin/end.");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1296.java