error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1908.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1908.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1908.java
text:
```scala
v@@oid exitField(int initializationStart, int declarationEnd, int declarationSourceEnd);

/*******************************************************************************
 * Copyright (c) 2000, 2001, 2002 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.jdt.internal.compiler;

import org.eclipse.jdt.core.compiler.IProblem;

/*
 * Part of the source element parser responsible for building the output.
 * It gets notified of structural information as they are detected, relying
 * on the requestor to assemble them together, based on the notifications it got.
 *
 * The structural investigation includes:
 * - package statement
 * - import statements
 * - top-level types: package member, member types (member types of member types...)
 * - fields
 * - methods
 *
 * If reference information is requested, then all source constructs are
 * investigated and type, field & method references are provided as well.
 *
 * Any (parsing) problem encountered is also provided.
 *
 * All positions are relative to the exact source fed to the parser.
 *
 * Elements which are complex are notified in two steps:
 * - enter<Element> : once the element header has been identified
 * - exit<Element> : once the element has been fully consumed
 *
 * other simpler elements (package, import) are read all at once:
 * - accept<Element>
 */
 
public interface ISourceElementRequestor {
void acceptConstructorReference(char[] typeName, int argCount, int sourcePosition);
void acceptFieldReference(char[] fieldName, int sourcePosition);
/**
 * @param declarationStart This is the position of the first character of the
 *		  				   import keyword.
 * @param declarationEnd This is the position of the ';' ending the import statement
 *						 or the end of the comment following the import.
 * @param name This is the name of the import like specified in the source including the dots. The '.*'
 *             is never included in the name.
 * @param onDemand set to true if the import is an import on demand (e.g. import java.io.*). False otherwise.
 */
void acceptImport(
	int declarationStart,
	int declarationEnd,
	char[] name,
	boolean onDemand);
/*
 * Table of line separator position. This table is passed once at the end
 * of the parse action, so as to allow computation of normalized ranges.
 *
 * A line separator might corresponds to several characters in the source,
 * 
 */
void acceptLineSeparatorPositions(int[] positions);
void acceptMethodReference(char[] methodName, int argCount, int sourcePosition);
void acceptPackage(
	int declarationStart,
	int declarationEnd,
	char[] name);
void acceptProblem(IProblem problem);
void acceptTypeReference(char[][] typeName, int sourceStart, int sourceEnd);
void acceptTypeReference(char[] typeName, int sourcePosition);
void acceptUnknownReference(char[][] name, int sourceStart, int sourceEnd);
void acceptUnknownReference(char[] name, int sourcePosition);
void enterClass(
	int declarationStart,
	int modifiers,
	char[] name,
	int nameSourceStart,
	int nameSourceEnd,
	char[] superclass,
	char[][] superinterfaces);
void enterCompilationUnit();
void enterConstructor(
	int declarationStart,
	int modifiers,
	char[] name,
	int nameSourceStart,
	int nameSourceEnd,	
	char[][] parameterTypes,
	char[][] parameterNames,
	char[][] exceptionTypes);
void enterField(
	int declarationStart,
	int modifiers,
	char[] type,
	char[] name,
	int nameSourceStart,
	int nameSourceEnd);
void enterInitializer(
	int declarationStart,
	int modifiers);
void enterInterface(
	int declarationStart,
	int modifiers,
	char[] name,
	int nameSourceStart,
	int nameSourceEnd,
	char[][] superinterfaces);
void enterMethod(
	int declarationStart,
	int modifiers,
	char[] returnType,
	char[] name,
	int nameSourceStart,
	int nameSourceEnd,	
	char[][] parameterTypes,
	char[][] parameterNames,
	char[][] exceptionTypes);
void exitClass(int declarationEnd);
void exitCompilationUnit(int declarationEnd);
void exitConstructor(int declarationEnd);
/*
 * initializationStart denotes the source start of the expression used for initializing
 * the field if any (-1 if no initialization).
 */
void exitField(int initializationStart, int declarationEnd);
void exitInitializer(int declarationEnd);
void exitInterface(int declarationEnd);
void exitMethod(int declarationEnd);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1908.java