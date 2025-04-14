error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4925.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4925.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 62
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4925.java
text:
```scala
public final class HashtableOfObject implements Cloneable {

p@@ackage org.eclipse.jdt.internal.compiler.util;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.compiler.*;

/**
 * Hashtable of {char[] --> Object }
 */
public final class HashtableOfObject {
	
	// to avoid using Enumerations, walk the individual tables skipping nulls
	public char[] keyTable[];
	public Object valueTable[];

	int elementSize; // number of elements in the table
	int threshold;

	public HashtableOfObject() {
		this(13);
	}

	public HashtableOfObject(int size) {

		this.elementSize = 0;
		this.threshold = size; // size represents the expected number of elements
		int extraRoom = (int) (size * 1.75f);
		if (this.threshold == extraRoom)
			extraRoom++;
		this.keyTable = new char[extraRoom][];
		this.valueTable = new Object[extraRoom];
	}

	public Object clone() throws CloneNotSupportedException {
		HashtableOfObject result = (HashtableOfObject) super.clone();
		result.elementSize = this.elementSize;
		result.threshold = this.threshold;

		int length = this.keyTable.length;
		result.keyTable = new char[length][];
		System.arraycopy(this.keyTable, 0, result.keyTable, 0, length);

		length = this.valueTable.length;
		result.valueTable = new Object[length];
		System.arraycopy(this.valueTable, 0, result.valueTable, 0, length);
		return result;
	}

	public boolean containsKey(char[] key) {

		int index = CharOperation.hashCode(key) % valueTable.length;
		int keyLength = key.length;
		char[] currentKey;
		while ((currentKey = keyTable[index]) != null) {
			if (currentKey.length == keyLength
				&& CharOperation.prefixEquals(currentKey, key))
				return true;
			index = (index + 1) % keyTable.length;
		}
		return false;
	}

	public Object get(char[] key) {

		int index = CharOperation.hashCode(key) % valueTable.length;
		int keyLength = key.length;
		char[] currentKey;
		while ((currentKey = keyTable[index]) != null) {
			if (currentKey.length == keyLength
				&& CharOperation.prefixEquals(currentKey, key))
				return valueTable[index];
			index = (index + 1) % keyTable.length;
		}
		return null;
	}

	public Object put(char[] key, Object value) {

		int index = CharOperation.hashCode(key) % valueTable.length;
		int keyLength = key.length;
		char[] currentKey;
		while ((currentKey = keyTable[index]) != null) {
			if (currentKey.length == keyLength
				&& CharOperation.prefixEquals(currentKey, key))
				return valueTable[index] = value;
			index = (index + 1) % keyTable.length;
		}
		keyTable[index] = key;
		valueTable[index] = value;

		// assumes the threshold is never equal to the size of the table
		if (++elementSize > threshold)
			rehash();
		return value;
	}

	public Object removeKey(char[] key) {

		int index = CharOperation.hashCode(key) % valueTable.length;
		int keyLength = key.length;
		char[] currentKey;
		while ((currentKey = keyTable[index]) != null) {
			if (currentKey.length == keyLength
				&& CharOperation.prefixEquals(currentKey, key)) {
					Object value = valueTable[index];
					elementSize--;
					keyTable[index] = null;
					valueTable[index] = null;
					rehash();
					return value;
				}
			index = (index + 1) % keyTable.length;
		}
		return null;
	}

	private void rehash() {

		HashtableOfObject newHashtable = new HashtableOfObject(elementSize * 2);
		// double the number of expected elements
		char[] currentKey;
		for (int i = keyTable.length; --i >= 0;)
			if ((currentKey = keyTable[i]) != null)
				newHashtable.put(currentKey, valueTable[i]);

		this.keyTable = newHashtable.keyTable;
		this.valueTable = newHashtable.valueTable;
		this.threshold = newHashtable.threshold;
	}

	public int size() {
		return elementSize;
	}

	public String toString() {
		String s = ""; //$NON-NLS-1$
		Object object;
		for (int i = 0, length = valueTable.length; i < length; i++)
			if ((object = valueTable[i]) != null)
				s += new String(keyTable[i]) + " -> " + object.toString() + "\n"; 	//$NON-NLS-2$ //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4925.java