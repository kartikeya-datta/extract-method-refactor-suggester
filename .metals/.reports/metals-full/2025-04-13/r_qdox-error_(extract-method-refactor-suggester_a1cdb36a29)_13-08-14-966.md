error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4318.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4318.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[13,1]

error in qdox parser
file content:
```java
offset: 622
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4318.java
text:
```scala
public class CharArrayOps { // TODO: (jerome) should promote to CharOperation

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
p@@ackage org.eclipse.jdt.internal.core.util;

/**
 * A class to do characters operations so that we can use
 * char arrays more effectively.
 */
public class CharArrayOps { // TODO: should promote to CharOperation
/**
 * Returns the char arrays as an array of Strings
 */
public static String[] charcharToString(char[][] charchar) {
	if (charchar == null) {
		return null;
	}
	String[] strings= new String[charchar.length];
	for (int i= 0; i < charchar.length; i++) {
		strings[i]= new String(charchar[i]);
	}
	return strings;
}
/**
 * Returns the char array as a String
 */
public static String charToString(char[] chars) {
	if (chars == null) {
		return null;
	} else {
		return new String(chars);
	}
}
/**
 * Concatinates the two arrays into one big array.
 * If the first array is null, returns the second array.
 * If the second array is null, returns the first array.
 *
 * @param first - the array which the other array is concatinated onto
 * @param second - the array which is to be concatinated onto the first array
 */
public static char[] concat(char[] first, char[] second) {
	if (first == null)
		return second;
	if (second == null)
		return first;

	int length1 = first.length;
	int length2 = second.length;
	char[] result = new char[length1 + length2];
	System.arraycopy(first, 0, result, 0, length1);
	System.arraycopy(second, 0, result, length1, length2);
	return result;
}
/**
 * Checks the two character arrays for equality.
 *
 * @param first - one of the arrays to be compared
 * @param second - the other array which is to be compared
 */
public static boolean equals(char[] first, char[] second) {
	if (first == second)
		return true;
	if (first == null || second == null)
		return false;
	if (first.length != second.length)
		return false;

	for (int i = 0, length = first.length; i < length; i++)
		if (first[i] != second[i])
			return false;
	return true;
}
/**
 * Returns the index of the first occurrence of character in buffer,
 * starting from offset, or -1 if not found.
 */
public static int indexOf(char character, char[] buffer, int offset) {
	for (int i= offset; i < buffer.length; i++) {
		if (buffer[i] == character) {
			return i;
		}
	}
	return -1;
}
/**
 * Extracts a sub-array from the given array, starting
 * at the given startIndex and proceeding for length characters.
 * Returns null if:
 *  1. the src array is null
 *  2. the start index is out of bounds
 *  3. the length parameter specifies a end point which is out of bounds
 * Does not return a copy of the array if possible, in other words, if start is zero
 * and length equals the length of the src array.
 *
 * @param src - the array from which elements need to be copied
 * @param start - the start index in the src array
 * @param length - the number of characters to copy
 */
public static char[] subarray(char[] src, int start, int length) {
	if (src == null)
		return null;
	int srcLength = src.length;
	if (start < 0 || start >= srcLength)
		return null;
	if (length < 0 || start + length > srcLength)
		return null;
	if (srcLength == length && start == 0)
		return src;
		
	char[] result = new char[length];
	if (length > 0)
		System.arraycopy(src, start, result, 0, length);
	return result;
}
/**
 * Extracts a substring from the given array, starting
 * at the given startIndex and proceeding for length characters.
 * Returns null if:
 *  1. the src array is null
 *  2. the start index is out of bounds
 *  3. the length parameter specifies a end point which is out of bounds
 * Does not return a copy of the array if possible (if start is zero
 * and length equals the length of the src array).
 *
 * @param src - the array from which elements need to be copied
 * @param start - the start index in the src array
 * @param length - the number of characters to copy
 */
public static String substring(char[] src, int start, int length) {
	char[] chars= subarray(src, start, length);
	if (chars != null) {
		return new String(chars);
	} else {
		return null;
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4318.java