error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7909.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7909.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7909.java
text:
```scala
I@@BM Corporation - initial API and implementation

/**********************************************************************
Copyright (c) 2002 IBM Corp. and others.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v0.5
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v05.html
 
Contributors:
    Philippe Mulet - Initial API and implementation
**********************************************************************/

package org.eclipse.jdt.core.compiler;
 
 /**
  * Definition of a Java scanner, as returned by the <code>ToolFactory</code>.
  * The scanner is responsible for tokenizing a given source, providing information about
  * the nature of the token read, its positions and source equivalent.
  * 
  * When the scanner has finished tokenizing, it answers an EOF token (<code>
  * ITerminalSymbols#TokenNameEOF</code>.
  * 
  * When encountering lexical errors, an <code>InvalidInputException</code> is thrown.
  * 
  * @see ToolFactory
  * @see ITerminalSymbols
  * @since 2.0
  */
public interface IScanner {

	/**
	 * Answers the current identifier source, after unicode escape sequences have
	 * been translated into unicode characters.
	 * e.g. if original source was <code>\\u0061bc</code> then it will answer <code>abc</code>.
	 */
	char[] getCurrentTokenSource();

	/**
	 * Answers the starting position of the current token inside the original source.
	 * This position is zero-based and inclusive. It corresponds to the position of the first character 
	 * which is part of this token. If this character was a unicode escape sequence, it points at the first 
	 * character of this sequence.
	 */
	int getCurrentTokenStartPosition();

	/**
	 * Answers the ending position of the current token inside the original source.
	 * This position is zero-based and inclusive. It corresponds to the position of the last character
	 * which is part of this token. If this character was a unicode escape sequence, it points at the last 
	 * character of this sequence.
	 */
	int getCurrentTokenEndPosition();

	/**
	 * Answers the starting position of a given line number. This line has to have been encountered
	 * already in the tokenization process (i.e. it cannot be used to compute positions of lines beyond
	 * current token). Once the entire source has been processed, it can be used without any limit.
	 * Line starting positions are zero-based, and start immediately after the previous line separator (if any).
	 */
	int getLineStart(int lineNumber);

	/**
	 * Answers the ending position of a given line number. This line has to have been encountered
	 * already in the tokenization process (i.e. it cannot be used to compute positions of lines beyond
	 * current token). Once the entire source has been processed, it can be used without any limit.
	* Line ending positions are zero-based, and correspond to the last character of the line separator 
	* (in case multi-character line separators).	 
	**/
	int getLineEnd(int lineNumber);

	/**
	 * Answers an array of the ending positions of the lines encountered so far. Line ending positions
	 * are zero-based, and correspond to the last character of the line separator (in case multi-character
	 * line separators).
	 */
	int[] getLineEnds();

	/**
	 * Read the next token in the source, and answers its ID as specified by <code>ITerminalSymbols</code>.
	 * Note that the actual token ID values are subject to change if new keywords were added to the language
	 * (i.e. 'assert' keyword in 1.4).
	 * 
	 * @throws InvalidInputException - in case a lexical error was detected while reading the current token
	 */
	int getNextToken() throws InvalidInputException;

	/**
	 * Reposition the scanner on some portion of the original source. Once reaching the given <code>endPosition</code>
	 * it will anser EOF tokens (<code>ITerminalSymbols.TokenNameEOF</code>).
	 */
	void resetTo(int startPosition, int endPosition);

	/**
	 * Set the scanner source to process. By default, the scanner will consider starting at the beginning of the
	 * source until it reaches its end.
	 */
	void setSourceBuffer(char[] source);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7909.java