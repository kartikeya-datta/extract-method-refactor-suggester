error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9003.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9003.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9003.java
text:
```scala
s@@uper(false /*comment*/, false /*whitespace*/, false /*nls*/, assertMode /*assert*/, null /*taskTags*/, null/*taskPriorities*/);

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
package org.eclipse.jdt.internal.codeassist.select;

/*
 * Scanner aware of a selection range. If finding an identifier which source range is exactly
 * the same, then will record it so that the parser can make use of it.
 *
 * Source positions are zero-based and inclusive.
 */
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.internal.compiler.parser.Scanner;

public class SelectionScanner extends Scanner {

	public char[] selectionIdentifier;
	public int selectionStart, selectionEnd;
/* 
 * Truncate the current identifier if it is containing the cursor location. Since completion is performed
 * on an identifier prefix.
 *
 */
 
public SelectionScanner(boolean assertMode) {
	super(false /*comment*/, false /*whitespace*/, false /*nls*/, assertMode /*assert*/, false /*strict comment*/, null /*taskTags*/, null/*taskPriorities*/);
}

public char[] getCurrentIdentifierSource() {

	if (selectionIdentifier == null){
		if (selectionStart == startPosition && selectionEnd == currentPosition-1){
			if (withoutUnicodePtr != 0){			// check unicode scenario
				System.arraycopy(withoutUnicodeBuffer, 1, selectionIdentifier = new char[withoutUnicodePtr], 0, withoutUnicodePtr);
			} else {
				int length = currentPosition - startPosition;
				// no char[] sharing around completionIdentifier, we want it to be unique so as to use identity checks	
				System.arraycopy(source, startPosition, (selectionIdentifier = new char[length]), 0, length);
			}
			return selectionIdentifier;
		}
	}
	return super.getCurrentIdentifierSource();
}
/*
 * In case we actually read a keyword which corresponds to the selected
 * range, we pretend we read an identifier.
 */
public int scanIdentifierOrKeyword() throws InvalidInputException {

	int id = super.scanIdentifierOrKeyword();

	// convert completed keyword into an identifier
	if (id != TokenNameIdentifier
		&& startPosition == selectionStart 
		&& currentPosition == selectionEnd+1){
		return TokenNameIdentifier;
	}
	return id;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9003.java