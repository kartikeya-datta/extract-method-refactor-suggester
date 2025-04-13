error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7045.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7045.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7045.java
text:
```scala
f@@or (int i = length-1; i >= 0; i--)

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
package org.eclipse.jdt.internal.core.util;

/**
 * Hashtable of {Object[] --> Object }
 */
public final class HashtableOfArrayToObject implements Cloneable {
	
	// to avoid using Enumerations, walk the individual tables skipping nulls
	public Object[][] keyTable;
	public Object[] valueTable;

	public int elementSize; // number of elements in the table
	int threshold;

	public HashtableOfArrayToObject() {
		this(13);
	}

	public HashtableOfArrayToObject(int size) {

		this.elementSize = 0;
		this.threshold = size; // size represents the expected number of elements
		int extraRoom = (int) (size * 1.75f);
		if (this.threshold == extraRoom)
			extraRoom++;
		this.keyTable = new Object[extraRoom][];
		this.valueTable = new Object[extraRoom];
	}

	public Object clone() throws CloneNotSupportedException {
		HashtableOfArrayToObject result = (HashtableOfArrayToObject) super.clone();
		result.elementSize = this.elementSize;
		result.threshold = this.threshold;

		int length = this.keyTable.length;
		result.keyTable = new Object[length][];
		System.arraycopy(this.keyTable, 0, result.keyTable, 0, length);

		length = this.valueTable.length;
		result.valueTable = new Object[length];
		System.arraycopy(this.valueTable, 0, result.valueTable, 0, length);
		return result;
	}

	public boolean containsKey(Object[] key) {

		int index = hashCode(key) % this.valueTable.length;
		int keyLength = key.length;
		Object[] currentKey;
		while ((currentKey = this.keyTable[index]) != null) {
			if (currentKey.length == keyLength && Util.equalArraysOrNull(currentKey, key))
				return true;
			index = (index + 1) % this.keyTable.length;
		}
		return false;
	}

	public Object get(Object[] key) {

		int index = hashCode(key) % this.valueTable.length;
		int keyLength = key.length;
		Object[] currentKey;
		while ((currentKey = this.keyTable[index]) != null) {
			if (currentKey.length == keyLength && Util.equalArraysOrNull(currentKey, key))
				return this.valueTable[index];
			index = (index + 1) % keyTable.length;
		}
		return null;
	}

	public Object[] getKey(Object[] key, int keyLength) {

		int index = hashCode(key, keyLength) % this.valueTable.length;
		Object[] currentKey;
		while ((currentKey = this.keyTable[index]) != null) {
			if (currentKey.length == keyLength && Util.equalArrays(currentKey, key, keyLength))
				return currentKey;
			index = (index + 1) % this.keyTable.length;
		}
		return null;
	}

	private int hashCode(Object[] element) {
		return hashCode(element, element.length);
	}
	
	private int hashCode(Object[] element, int length) {
		int hash = 0;
		for (int i = 0; i < length; i++)
			hash = Util.combineHashCodes(hash, element[i].hashCode());
		return hash & 0x7FFFFFFF;
	}
	
	public Object put(Object[] key, Object value) {

		int index = hashCode(key) % this.valueTable.length;
		int keyLength = key.length;
		Object[] currentKey;
		while ((currentKey = this.keyTable[index]) != null) {
			if (currentKey.length == keyLength && Util.equalArraysOrNull(currentKey, key))
				return this.valueTable[index] = value;
			index = (index + 1) % keyTable.length;
		}
		this.keyTable[index] = key;
		this.valueTable[index] = value;

		// assumes the threshold is never equal to the size of the table
		if (++this.elementSize > threshold)
			rehash();
		return value;
	}

	public Object removeKey(Object[] key) {

		int index = hashCode(key) % this.valueTable.length;
		int keyLength = key.length;
		Object[] currentKey;
		while ((currentKey = this.keyTable[index]) != null) {
			if (currentKey.length == keyLength && Util.equalArraysOrNull(currentKey, key)) {
				Object value = this.valueTable[index];
				this.elementSize--;
				this.keyTable[index] = null;
				this.valueTable[index] = null;
				rehash();
				return value;
			}
			index = (index + 1) % this.keyTable.length;
		}
		return null;
	}

	private void rehash() {

		HashtableOfArrayToObject newHashtable = new HashtableOfArrayToObject(elementSize * 2);		// double the number of expected elements
		Object[] currentKey;
		for (int i = this.keyTable.length; --i >= 0;)
			if ((currentKey = this.keyTable[i]) != null)
				newHashtable.put(currentKey, this.valueTable[i]);

		this.keyTable = newHashtable.keyTable;
		this.valueTable = newHashtable.valueTable;
		this.threshold = newHashtable.threshold;
	}

	public int size() {
		return elementSize;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		Object[] element;
		for (int i = 0, length = this.keyTable.length; i < length; i++)
			if ((element = this.keyTable[i]) != null) {
				buffer.append('{');
				for (int j = 0, length2 = element.length; j < length2; j++) {
					buffer.append(element[j]);
					if (j != length2-1) 
						buffer.append(", "); //$NON-NLS-1$
				}
				buffer.append("} -> ");  //$NON-NLS-1$
				buffer.append(this.valueTable[i]);
				if (i != length-1)
					buffer.append('\n');
			}
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7045.java