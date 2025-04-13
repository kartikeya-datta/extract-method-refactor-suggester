error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1043.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1043.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1043.java
text:
```scala
S@@impleLookupTable newLookupTable = new SimpleLookupTable(elementSize * 2); // double the number of expected elements

package org.eclipse.jdt.internal.core.builder;

/*
 * (c) Copyright IBM Corp. 2000, 2002.
 * All Rights Reserved.
 */

/**
 * A simple lookup table is a non-synchronized Hashtable, whose keys
 * and values are Objects. It also uses linear probing to resolve collisions
 * rather than a linked list of hash table entries.
 */
public final class SimpleLookupTable implements Cloneable {

// to avoid using Enumerations, walk the individual tables skipping nulls
public Object keyTable[];
public Object valueTable[];
public int elementSize; // number of elements in the table
public int threshold;

public SimpleLookupTable() {
	this(13);
}

public SimpleLookupTable(int size) {
	if (size < 7) size = 7;
	this.elementSize = 0;
	this.threshold = size + 1; // size is the expected number of elements
	int tableLength = 2 * size + 1;
	this.keyTable = new Object[tableLength];
	this.valueTable = new Object[tableLength];
}

public Object clone() throws CloneNotSupportedException {
	SimpleLookupTable result = (SimpleLookupTable) super.clone();
	result.elementSize = this.elementSize;
	result.threshold = this.threshold;

	int length = this.keyTable.length;
	result.keyTable = new Object[length];
	System.arraycopy(this.keyTable, 0, result.keyTable, 0, length);

	length = this.valueTable.length;
	result.valueTable = new Object[length];
	System.arraycopy(this.valueTable, 0, result.valueTable, 0, length);
	return result;
}

public boolean containsKey(Object key) {
	int length = keyTable.length;
	int index = (key.hashCode() & 0x7FFFFFFF) % length;
	Object currentKey;
	while ((currentKey = keyTable[index]) != null) {
		if (currentKey.equals(key)) return true;
		if (++index == length) index = 0;
	}
	return false;
}

public Object get(Object key) {
	int length = keyTable.length;
	int index = (key.hashCode() & 0x7FFFFFFF) % length;
	Object currentKey;
	while ((currentKey = keyTable[index]) != null) {
		if (currentKey.equals(key)) return valueTable[index];
		if (++index == length) index = 0;
	}
	return null;
}

public Object put(Object key, Object value) {
	int length = keyTable.length;
	int index = (key.hashCode() & 0x7FFFFFFF) % length;
	Object currentKey;
	while ((currentKey = keyTable[index]) != null) {
		if (currentKey.equals(key)) return valueTable[index] = value;
		if (++index == length) index = 0;
	}
	keyTable[index] = key;
	valueTable[index] = value;

	// assumes the threshold is never equal to the size of the table
	if (++elementSize > threshold) rehash();
	return value;
}

public void removeKey(Object key) {
	int length = keyTable.length;
	int index = (key.hashCode() & 0x7FFFFFFF) % length;
	Object currentKey;
	while ((currentKey = keyTable[index]) != null) {
		if (currentKey.equals(key)) {
			elementSize--;
			keyTable[index] = null;
			valueTable[index] = null;
			if (keyTable[index + 1 == length ? 0 : index + 1] != null)
				rehash(); // only needed if a possible collision existed
			return;
		}
		if (++index == length) index = 0;
	}
}

public void removeValue(Object valueToRemove) {
	boolean rehash = false;
	for (int i = 0, l = valueTable.length; i < l; i++) {
		Object value = valueTable[i];
		if (value != null && value.equals(valueToRemove)) {
			elementSize--;
			keyTable[i] = null;
			valueTable[i] = null;
			if (!rehash && keyTable[i + 1 == l ? 0 : i + 1] != null)
				rehash = true; // only needed if a possible collision existed
		}
	}
	if (rehash) rehash();
}

private void rehash() {
	SimpleLookupTable newLookupTable = new SimpleLookupTable(elementSize);
	Object currentKey;
	for (int i = keyTable.length; --i >= 0;)
		if ((currentKey = keyTable[i]) != null)
			newLookupTable.put(currentKey, valueTable[i]);

	this.keyTable = newLookupTable.keyTable;
	this.valueTable = newLookupTable.valueTable;
	this.elementSize = newLookupTable.elementSize;
	this.threshold = newLookupTable.threshold;
}

public String toString() {
	String s = ""; //$NON-NLS-1$
	Object object;
	for (int i = 0, length = valueTable.length; i < length; i++)
		if ((object = valueTable[i]) != null)
			s += keyTable[i].toString() + " -> " + object.toString() + "\n"; 	//$NON-NLS-2$ //$NON-NLS-1$
	return s;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1043.java