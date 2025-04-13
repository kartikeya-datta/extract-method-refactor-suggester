error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2078.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2078.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2078.java
text:
```scala
r@@eturn "ProblemDetail("/*nonNLS*/ + getMessage() + ")"/*nonNLS*/;

package org.eclipse.jdt.internal.core.builder.impl;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.runtime.IPath;

import org.eclipse.jdt.internal.compiler.IProblem;
import org.eclipse.jdt.internal.core.builder.*;
import org.eclipse.jdt.internal.core.Util;

/**
 * @see IProblemDetail
 */
public class ProblemDetailImpl implements IProblemDetail, IProblem {
	protected SourceEntry fSourceEntry;
	protected String fMessage;
	protected int fStartPos, fEndPos, fLineNumber;
	protected int fSeverity;

	/**
	 * The ID of the problem returned by the compiler.
	 * @see com.ibm.compiler.java.problem.ProblemIrritants
	 */
	protected int fID;

	/**
	 * Severity flag indicating a syntax error (also covers namespace errors such as duplicates).
	 */
	protected static final int S_SYNTAX_ERROR = 2;
/**
 * Creates a problem detail.
 */
public ProblemDetailImpl(String msg, int id, int severity, SourceEntry sourceEntry, int startPos, int endPos, int lineNumber) {
	fMessage = msg;
	fID = id;
	fSeverity = severity;
	fSourceEntry = sourceEntry;
	fStartPos = startPos;
	fEndPos = endPos;
	fLineNumber = lineNumber;
}
/**
 * Creates a problem detail.
 */
public ProblemDetailImpl(String msg, SourceEntry sourceEntry) {
	this(msg, 0, S_ERROR, sourceEntry, -1, -1, -1);
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(this instanceof ProblemDetailImpl)) return false;
	return equals((ProblemDetailImpl) o, false);
}
public boolean equals(ProblemDetailImpl pb, boolean ignorePositions) {
	return
		fMessage.equals(pb.fMessage)
			&& Util.equalOrNull(fSourceEntry, pb.fSourceEntry)
			&& fSeverity == pb.fSeverity
			&& (ignorePositions || 
				(fStartPos == pb.fStartPos && fEndPos == pb.fEndPos));
}
/**
 * @see IProblem
 */
public String[] getArguments() {
	return null; // not kept
}
/**
 * Returns the end pos.
 */
public int getEndPos() {
	return fEndPos;
}
/**
 * @see IProblemDetail
 */
public int getID() {
	return fID;
}
/**
 * @see IProblemDetail
 */
public int getKind() {
	return IProblemDetail.K_COMPILATION_PROBLEM;
}
/**
 * @see IProblemDetail
 */
public int getLineNumber() {
	return fLineNumber;
}
/**
 * @see IProblemDetail
 */
public String getMessage() {
	return fMessage;
}
/**
 * getOriginatingFileName method comment.
 */
public char[] getOriginatingFileName() {
	return fSourceEntry.getPathWithZipEntryName().toCharArray();
}
/**
 * Returns the path of the source entry.
 */
IPath getPath() {
	return fSourceEntry == null ? null : fSourceEntry.getPath();
}
/**
 * @see IProblemDetail
 */
public int getSeverity() {
	return fSeverity;
}
/**
 * @see IProblem
 */
public int getSourceEnd() {
	return fEndPos;
}
/**
 * Returns the source entry
 */
SourceEntry getSourceEntry() {
	return fSourceEntry;
}
/**
 * @see ICompilationProblem
 */
public ISourceFragment getSourceFragment() {
	if (fSourceEntry == null) {
		return null;
	}
	return new SourceFragmentImpl(fStartPos, fEndPos, fSourceEntry);
}
/**
 * @see IProblem
 */
public int getSourceLineNumber() {
	return fLineNumber; 
}
/**
 * @see IProblem
 */
public int getSourceStart() {
	return fStartPos;
}
/**
 * Returns the start pos.
 */
public int getStartPos() {
	return fStartPos;
}
public int hashCode() {
	return fMessage.hashCode() * 17 + (fSourceEntry == null ? 0 : fSourceEntry.hashCode());
}
/**
 * @see IProblem
 */
public boolean isError() {
	return (fSeverity & S_ERROR) != 0;
}
/**
 * @see IProblem
 */
public boolean isWarning() {
	return (fSeverity & S_ERROR) == 0;
}
/**
 * @see IProblem
 */
public void setSourceEnd(int sourceEnd) {
	fEndPos = sourceEnd;
}
/**
 * Internal - Set the source entry.
 */
public void setSourceEntry(SourceEntry sourceEntry) {
	fSourceEntry = sourceEntry;
}
/**
 * @see IProblem
 */
public void setSourceLineNumber(int lineNumber) {
	fLineNumber = lineNumber;
}
/**
 * @see IProblem
 */
public void setSourceStart(int sourceStart) {
	fStartPos = sourceStart;
}
/**
 * Returns a readable representation of the class.  This method is for debugging
 * purposes only. Non-NLS.
 */
public String toString() {
	return "ProblemDetail(" + getMessage() + ")";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2078.java