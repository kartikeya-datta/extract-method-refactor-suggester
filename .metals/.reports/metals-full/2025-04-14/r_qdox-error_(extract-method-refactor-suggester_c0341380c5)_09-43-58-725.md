error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1995.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1995.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1995.java
text:
```scala
i@@f(len>=bits.length) {


package com.badlogic.gdx.utils;

import java.util.Arrays;

/** A bitset, without size limitation, allows comparison via bitwise operators to other bitfields.
 * @author mzechner */
public class Bits {
	long[] bits = { 0 };

	/**
	 * @param index the index of the bit
	 * @return whether the bit is set
	 * @throws ArrayIndexOutOfBoundsException if index < 0
	 */
	public boolean get (int index) {
		final int word = index >>> 6;
		if(word >= bits.length) return false;
		return (bits[word] & (1L << (index & 0x3F))) != 0L;
	}

	/**
	 * @param index the index of the bit to set
	 * @throws ArrayIndexOutOfBoundsException if index < 0
	 */
	public void set (int index) {
		final int word = index >>> 6;
		checkCapacity(word);
		bits[word] |= 1L << (index & 0x3F);
	}
	
	/**
	 * @param index the index of the bit to flip
	 */
	public void flip(int index) {
		final int word = index >>> 6;
		checkCapacity(word);
		bits[word] ^= 1L << (index & 0x3F);
	}
	
	private void checkCapacity(int len) {
		if(len> bits.length) {
			long[] newBits = new long[len+1];
			System.arraycopy(bits, 0, newBits, 0, bits.length);
			bits = newBits;
		}
	}

	/**
	 * @param index the index of the bit to clear
	 * @throws ArrayIndexOutOfBoundsException if index < 0
	 */
	public void clear (int index) {
		final int word = index >>> 6;
		if(word >= bits.length) return;
		bits[word] &= ~(1L << (index & 0x3F));
	}

	/**
	 * Clears the entire bitset
	 */
	public void clear () {
		int length = bits.length;
		for (int i = 0; i < length; i++) {
			bits[i] = 0L;
		}
	}
	
	/**
	 * @return the number of bits currently stored, <b>not</b> the highset set bit!
	 */
	public int numBits() {
		return bits.length << 6;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1995.java