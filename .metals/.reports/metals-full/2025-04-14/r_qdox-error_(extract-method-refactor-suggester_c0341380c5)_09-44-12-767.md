error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5357.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5357.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,3]

error in qdox parser
file content:
```java
offset: 3
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5357.java
text:
```scala
+ (@@reference[position++] & 0xFF);

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.util;

/**
 * Abstract class that defines helpers methods for decoding .class file.
 */
public abstract class ClassFileStruct {

	protected double doubleAt(byte[] reference, int relativeOffset, int structOffset) {
		return (Double.longBitsToDouble(this.i8At(reference, relativeOffset, structOffset)));
	}

	protected float floatAt(byte[] reference, int relativeOffset, int structOffset) {
		return (Float.intBitsToFloat(this.i4At(reference, relativeOffset, structOffset)));
	}
	protected int i1At(byte[] reference, int relativeOffset, int structOffset) {
		return reference[relativeOffset + structOffset];
	}
	protected int i2At(byte[] reference, int relativeOffset, int structOffset) {
		int position = relativeOffset + structOffset;
		return (reference[position++] << 8) + (reference[position] & 0xFF);
	}
	protected int i4At(byte[] reference, int relativeOffset, int structOffset) {
		int position = relativeOffset + structOffset;
		return ((reference[position++] & 0xFF) << 24)
			+ ((reference[position++] & 0xFF) << 16)
			+ ((reference[position++] & 0xFF) << 8)
			+ (reference[position] & 0xFF);
	}
	protected long i8At(byte[] reference, int relativeOffset, int structOffset) {
		int position = relativeOffset + structOffset;
		return (((long) (reference[position++] & 0xFF)) << 56)
			+ (((long) (reference[position++] & 0xFF)) << 48)
			+ (((long) (reference[position++] & 0xFF)) << 40)
			+ (((long) (reference[position++] & 0xFF)) << 32)
			+ (((long) (reference[position++] & 0xFF)) << 24)
			+ (((long) (reference[position++] & 0xFF)) << 16)
			+ (((long) (reference[position++] & 0xFF)) << 8)
			+ ((long) (reference[position++] & 0xFF));
	}

	protected int u1At(byte[] reference, int relativeOffset, int structOffset) {
		return (reference[relativeOffset + structOffset] & 0xFF);
	}
	protected int u2At(byte[] reference, int relativeOffset, int structOffset) {
		int position = relativeOffset + structOffset;
		return ((reference[position++] & 0xFF) << 8) + (reference[position] & 0xFF);
	}
	protected long u4At(byte[] reference, int relativeOffset, int structOffset) {
		int position = relativeOffset + structOffset;
		return (
			((reference[position++] & 0xFFL) << 24)
				+ ((reference[position++] & 0xFF) << 16)
				+ ((reference[position++] & 0xFF) << 8)
				+ (reference[position] & 0xFF));
	}
	protected char[] utf8At(byte[] reference, int relativeOffset, int structOffset, int bytesAvailable) {
		int x, y, z;
		int length = bytesAvailable;
		char outputBuf[] = new char[bytesAvailable];
		int outputPos = 0;
		int readOffset = structOffset + relativeOffset;

		while (length != 0) {
			x = reference[readOffset++] & 0xFF;
			length--;
			if ((0x80 & x) != 0) {
				y = reference[readOffset++] & 0xFF;
				length--;
				if ((x & 0x20) != 0) {
					z = reference[readOffset++] & 0xFF;
					length--;
					x = ((x & 0x1F) << 12) + ((y & 0x3F) << 6) + (z & 0x3F);
				} else {
					x = ((x & 0x1F) << 6) + (y & 0x3F);
				}
			}
			outputBuf[outputPos++] = (char) x;
		}

		if (outputPos != bytesAvailable) {
			System.arraycopy(outputBuf, 0, (outputBuf = new char[outputPos]), 0, outputPos);
		}
		return outputBuf;
	}

	final boolean equals(char[] first, char[] second) {
		if (first == second)
			return true;
		if (first == null || second == null)
			return false;
		if (first.length != second.length)
			return false;
	
		for (int i = first.length; --i >= 0;)
			if (first[i] != second[i])
				return false;
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5357.java