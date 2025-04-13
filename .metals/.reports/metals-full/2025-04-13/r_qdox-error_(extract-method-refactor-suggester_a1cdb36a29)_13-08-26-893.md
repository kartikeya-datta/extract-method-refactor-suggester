error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6416.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6416.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[13,1]

error in qdox parser
file content:
```java
offset: 603
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6416.java
text:
```scala
public static class HashableWeakReference extends WeakReference {

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
p@@ackage org.eclipse.jdt.internal.compiler.util;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import org.eclipse.jdt.core.compiler.CharOperation;

/**
 * A hashset of char[] whose values can be garbage collected.
 */
public class WeakHashSetOfCharArray {
	
	public class HashableWeakReference extends WeakReference {
		public int hashCode;
		public HashableWeakReference(char[] referent, ReferenceQueue queue) {
			super(referent, queue);
			this.hashCode = CharOperation.hashCode(referent);
		}
		public boolean equals(Object obj) {
			if (!(obj instanceof HashableWeakReference)) return false;
			char[] referent = (char[]) get();
			char[] other = (char[]) ((HashableWeakReference) obj).get();
			if (referent == null) return other == null;
			return CharOperation.equals(referent, other);
		}
		public int hashCode() {
			return this.hashCode;
		}
		public String toString() {
			char[] referent = (char[]) get();
			if (referent == null) return "[hashCode=" + this.hashCode + "] <referent was garbage collected>"; //$NON-NLS-1$  //$NON-NLS-2$
			return "[hashCode=" + this.hashCode + "] \"" + new String(referent) + '\"'; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	HashableWeakReference[] values;
	public int elementSize; // number of elements in the table
	int threshold;
	ReferenceQueue referenceQueue = new ReferenceQueue();	
	
	public WeakHashSetOfCharArray() {
		this(5);
	}
	
	public WeakHashSetOfCharArray(int size) {
		this.elementSize = 0;
		this.threshold = size; // size represents the expected number of elements
		int extraRoom = (int) (size * 1.75f);
		if (this.threshold == extraRoom)
			extraRoom++;
		this.values = new HashableWeakReference[extraRoom];
	}
	
	/*
	 * Adds the given char array to this set.
	 * If a char array that is equals to the given char array already exists, do nothing.
	 * Returns the existing char array or the new char array if not found.
	 */
	public char[] add(char[] array) {
		cleanupGarbageCollectedValues();
		int index = (CharOperation.hashCode(array) & 0x7FFFFFFF) % this.values.length;
		HashableWeakReference currentValue;
		while ((currentValue = this.values[index]) != null) {
			char[] referent;
			if (CharOperation.equals(array, referent = (char[]) currentValue.get())) {
				return referent;
			}
			index = (index + 1) % this.values.length;
		}
		this.values[index] = new HashableWeakReference(array, this.referenceQueue);

		// assumes the threshold is never equal to the size of the table
		if (++this.elementSize > this.threshold)
			rehash();
		
		return array;
	}
		
	private void addValue(HashableWeakReference value) {
		char[] array = (char[]) value.get();
		if (array == null) return;
		int valuesLength = this.values.length;
		int index = (value.hashCode & 0x7FFFFFFF) % valuesLength;
		HashableWeakReference currentValue;
		while ((currentValue = this.values[index]) != null) {
			if (CharOperation.equals(array, (char[]) currentValue.get())) {
				return;
			}
			index = (index + 1) % valuesLength;
		}
		this.values[index] = value;

		// assumes the threshold is never equal to the size of the table
		if (++this.elementSize > this.threshold)
			rehash();
	}
	
	private void cleanupGarbageCollectedValues() {
		HashableWeakReference toBeRemoved;
		while ((toBeRemoved = (HashableWeakReference) this.referenceQueue.poll()) != null) {
			int hashCode = toBeRemoved.hashCode;
			int valuesLength = this.values.length;
			int index = (hashCode & 0x7FFFFFFF) % valuesLength;
			HashableWeakReference currentValue;
			while ((currentValue = this.values[index]) != null) {
				if (currentValue == toBeRemoved) {
					// replace the value at index with the last value with the same hash
					int sameHash = index;
					int current;
					while ((currentValue = this.values[current = (sameHash + 1) % valuesLength]) != null && currentValue.hashCode == hashCode)
						sameHash = current;
					this.values[index] = this.values[sameHash];
					this.values[sameHash] = null;
					this.elementSize--;
					break;
				}
				index = (index + 1) % valuesLength;
			}
		}
	}
	
	public boolean contains(char[] array) {
		return get(array) != null;
	}
	
	/*
	 * Return the char array that is in this set and that is equals to the given char array.
	 * Return null if not found.
	 */
	public char[] get(char[] array) {
		cleanupGarbageCollectedValues();
		int valuesLength = this.values.length;
		int index = (CharOperation.hashCode(array) & 0x7FFFFFFF) % valuesLength;
		HashableWeakReference currentValue;
		while ((currentValue = this.values[index]) != null) {
			char[] referent;
			if (CharOperation.equals(array, referent = (char[]) currentValue.get())) {
				return referent;
			}
			index = (index + 1) % valuesLength;
		}
		return null;
	}
		
	private void rehash() {
		WeakHashSetOfCharArray newHashSet = new WeakHashSetOfCharArray(this.elementSize * 2);		// double the number of expected elements
		newHashSet.referenceQueue = this.referenceQueue;
		HashableWeakReference currentValue;
		for (int i = 0, length = this.values.length; i < length; i++)
			if ((currentValue = this.values[i]) != null)
				newHashSet.addValue(currentValue);

		this.values = newHashSet.values;
		this.threshold = newHashSet.threshold;
		this.elementSize = newHashSet.elementSize;
	}

	/*
	 * Removes the char array that is in this set and that is equals to the given char array.
	 * Return the char array that was in the set, or null if not found.
	 */
	public char[] remove(char[] array) {
		cleanupGarbageCollectedValues();
		int valuesLength = this.values.length;
		int index = (CharOperation.hashCode(array) & 0x7FFFFFFF) % valuesLength;
		HashableWeakReference currentValue;
		while ((currentValue = this.values[index]) != null) {
			char[] referent;
			if (CharOperation.equals(array, referent = (char[]) currentValue.get())) {
				this.elementSize--;
				this.values[index] = null;
				rehash();
				return referent;
			}
			index = (index + 1) % valuesLength;
		}
		return null;
	}

	public int size() {
		return this.elementSize;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer("{"); //$NON-NLS-1$
		for (int i = 0, length = this.values.length; i < length; i++) {
			HashableWeakReference value = this.values[i];
			if (value != null) {
				char[] ref = (char[]) value.get();
				if (ref != null) {
					buffer.append('\"');
					buffer.append(ref);
					buffer.append("\", "); //$NON-NLS-1$
				}
			}
		}
		buffer.append("}"); //$NON-NLS-1$
		return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6416.java