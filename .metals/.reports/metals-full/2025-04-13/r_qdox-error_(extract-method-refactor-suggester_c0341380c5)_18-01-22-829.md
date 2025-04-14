error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3513.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3513.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3513.java
text:
```scala
private i@@nt bufferPosition; // TODO: (olivier) is this used?

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
package org.eclipse.jdt.internal.compiler.classfmt;

public class ClassFormatException extends Exception {
	private int errorCode;
	private int bufferPosition;

	public static final int ErrBadMagic = 1;
	public static final int ErrBadMinorVersion = 2;
	public static final int ErrBadMajorVersion = 3;

	public static final int ErrBadConstantClass= 4;
	public static final int ErrBadConstantString= 5;
	public static final int ErrBadConstantNameAndType = 6;
	public static final int ErrBadConstantFieldRef= 7;
	public static final int ErrBadConstantMethodRef = 8;
	public static final int ErrBadConstantInterfaceMethodRef = 9;
	public static final int ErrBadConstantPoolIndex = 10;
	public static final int ErrBadSuperclassName = 11;
	public static final int ErrInterfaceCannotBeFinal = 12;
	public static final int ErrInterfaceMustBeAbstract = 13;
	public static final int ErrBadModifiers = 14;
	public static final int ErrClassCannotBeAbstractFinal = 15;
	public static final int ErrBadClassname = 16;
	public static final int ErrBadFieldInfo = 17;
	public static final int ErrBadMethodInfo = 17; 

	public static final int ErrEmptyConstantPool =18;
	public static final int ErrMalformedUtf8 = 19;
	public static final int ErrUnknownConstantTag = 20;
	public static final int ErrTruncatedInput = 21;
	public static final int ErrMethodMustBeAbstract = 22;
	public static final int ErrMalformedAttribute = 23;
	public static final int ErrBadInterface = 24;
	public static final int ErrInterfaceMustSubclassObject = 25;
	public static final int ErrIncorrectInterfaceMethods = 26;
	public static final int ErrInvalidMethodName = 27;
	public static final int ErrInvalidMethodSignature = 28;
    
public ClassFormatException(int code) {
	errorCode = code;
}
public ClassFormatException(int code, int bufPos) {
	errorCode = code;
	bufferPosition = bufPos;
}
/**
 * @return int
 */
public int getErrorCode() {
	return errorCode;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3513.java