error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6921.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6921.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6921.java
text:
```scala
r@@eturn this.leftPattern.toString() + "\n| "/*nonNLS*/ + this.rightPattern.toString();

package org.eclipse.jdt.internal.core.search.matching;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.runtime.*;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.core.index.*;
import org.eclipse.jdt.core.search.*;

import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.core.index.impl.*;
import org.eclipse.jdt.internal.core.search.*;

import java.io.*;

import org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment;

public class OrPattern extends SearchPattern {

	public SearchPattern leftPattern;
	public SearchPattern rightPattern;
public OrPattern(SearchPattern leftPattern, SearchPattern rightPattern) {
	super(-1, false); // values ignored for a OrPattern
		
	this.leftPattern = leftPattern;
	this.rightPattern = rightPattern;

	this.needsResolve = leftPattern.needsResolve || rightPattern.needsResolve;
}
/**
 * see SearchPattern.decodedIndexEntry
 */
protected void decodeIndexEntry(IEntryResult entry) {

	// will never be directly invoked on a composite pattern
}
/**
 * see SearchPattern.feedIndexRequestor
 */
public void feedIndexRequestor(IIndexSearchRequestor requestor, int detailLevel, int[] references, IndexInput input, IJavaSearchScope scope)  throws IOException {
	// will never be directly invoked on a composite pattern
}
/**
 * see SearchPattern.findMatches
 */
public void findIndexMatches(IndexInput input, IIndexSearchRequestor requestor, int detailLevel, IProgressMonitor progressMonitor, IJavaSearchScope scope) throws IOException {

	if (progressMonitor != null && progressMonitor.isCanceled()) throw new OperationCanceledException();

	IIndexSearchRequestor orCombiner;
	if (detailLevel == IInfoConstants.NameInfo) {
		orCombiner = new OrNameCombiner(requestor);
	} else {
		orCombiner = new OrPathCombiner(requestor);
	}
	leftPattern.findIndexMatches(input, orCombiner, detailLevel, progressMonitor, scope);
	if (progressMonitor != null && progressMonitor.isCanceled()) throw new OperationCanceledException();
	rightPattern.findIndexMatches(input, orCombiner, detailLevel, progressMonitor, scope);
}
/**
 * see SearchPattern.indexEntryPrefix
 */
public char[] indexEntryPrefix() {

	// will never be directly invoked on a composite pattern
	return null;
}
/**
 * @see SearchPattern#matchContainer()
 */
protected int matchContainer() {
	return leftPattern.matchContainer()
 rightPattern.matchContainer();
}
/**
 * @see SearchPattern#matches(AstNode, boolean)
 */
protected boolean matches(AstNode node, boolean resolve) {
	return this.leftPattern.matches(node, resolve) || this.rightPattern.matches(node, resolve);
}
/**
 * @see SearchPattern#matches(Binding)
 */
public boolean matches(Binding binding) {
	return this.leftPattern.matches(binding) || this.rightPattern.matches(binding);
}
/**
 * see SearchPattern.matchIndexEntry
 */
protected boolean matchIndexEntry() {

	return leftPattern.matchIndexEntry()
 rightPattern.matchIndexEntry();
}
/**
 * @see SearchPattern#matchReportReference
 */
protected void matchReportReference(AstNode reference, IJavaElement element, int accuracy, MatchLocator locator) throws CoreException {
	if (this.leftPattern.matches(reference)) {
		this.leftPattern.matchReportReference(reference, element, accuracy, locator);
	} else {
		this.rightPattern.matchReportReference(reference, element, accuracy, locator);
	}
}
public String toString(){
	return this.leftPattern.toString() + "\n| " + this.rightPattern.toString();
}

/**
 * see SearchPattern.initializeFromLookupEnvironment
 */
public boolean initializeFromLookupEnvironment(LookupEnvironment env) {

	// need to perform both operand initialization due to side-effects.
	boolean leftInit = this.leftPattern.initializeFromLookupEnvironment(env);
	boolean rightInit = this.rightPattern.initializeFromLookupEnvironment(env);
	return leftInit || rightInit;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6921.java