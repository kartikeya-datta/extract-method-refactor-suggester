error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8785.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8785.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8785.java
text:
```scala
r@@eturn " "/*nonNLS*/ + Util.bind("problem.atLine"/*nonNLS*/,String.valueOf(line)) +

package org.eclipse.jdt.internal.compiler.problem;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.util.Util;

public class DefaultProblem implements IProblem, ProblemSeverities, ProblemIrritants {
	private char[] fileName;
	private int id;
	private int startPosition, endPosition,line;
	private int severity;
	private String[] arguments; 
	private String message;
public DefaultProblem(
	char[] originatingFileName,
	String message,
	int id, 
	String[] stringArguments, 
	int severity, 
	int startPosition, 
	int endPosition,
	int line) {
		
	this.fileName = originatingFileName;
	this.message = message;
	this.id = id;
	this.arguments = stringArguments;
	this.severity = severity;
	this.startPosition = startPosition;
	this.endPosition = endPosition;
	this.line = line;
}
public String errorReportSource(ICompilationUnit compilationUnit) {
	//extra from the source the innacurate     token
	//and "highlight" it using some underneath ^^^^^
	//put some context around too.

	//this code assumes that the font used in the console is fixed size

	//sanity .....
	if ((startPosition > endPosition)
 ((startPosition <= 0) && (endPosition <= 0)))
		return Util.bind("problem.noSourceInformation"/*nonNLS*/);

	//regular behavior....(slow code)

	final int AROUND = 60; //increase this value to see more ....
	final char SPACE = '\u0020';
	final char MARK = '^';
	final char TAB = '\t';
	char[] source = compilationUnit.getContents();
	//the next code tries to underline the token.....
	//it assumes (for a good display) that token source does not
	//contain any \r \n. This is false on statements ! 
	//(the code still works but the display is not optimal !)

	//compute the how-much-char we are displaying around the inaccurate token
	int begin = startPosition >= source.length ? source.length - 1 : startPosition;
	int relativeStart = 0;
	int end = endPosition >= source.length ? source.length - 1 : endPosition;
	int relativeEnd = 0;
	label : for (relativeStart = 0;; relativeStart++) {
		if (begin == 0)
			break label;
		if ((source[begin - 1] == '\n') || (source[begin - 1] == '\r'))
			break label;
		begin--;
	}
	label : for (relativeEnd = 0;; relativeEnd++) {
		if ((end + 1)>= source.length)
			break label;
		if ((source[end + 1] == '\r') || (source[end + 1] == '\n')) {
			break label;
		}
		end++;
	}
	//extract the message form the source
	char[] extract = new char[end - begin + 1];
	System.arraycopy(source, begin, extract, 0, extract.length);
	char c;
	//remove all SPACE and TAB that begin the error message...
	int trimLeftIndex = 0;
	while (((c = extract[trimLeftIndex++]) == TAB) || (c == SPACE)) {};
	System.arraycopy(
		extract, 
		trimLeftIndex - 1, 
		extract = new char[extract.length - trimLeftIndex + 1], 
		0, 
		extract.length); 
	relativeStart -= trimLeftIndex;
	//buffer spaces and tabs in order to reach the error position
	int pos = 0;
	char[] underneath = new char[extract.length]; // can't be bigger
	for (int i = 0; i <= relativeStart; i++) {
		if (extract[i] == TAB) {
			underneath[pos++] = TAB;
		} else {
			underneath[pos++] = SPACE;
		}
	}
	//mark the error position
	for (int i = startPosition; 
		i <= (endPosition >= source.length ? source.length - 1 : endPosition); 
		i++)
		underneath[pos++] = MARK;
	//resize underneathto remove 'null' chars
	System.arraycopy(underneath, 0, underneath = new char[pos], 0, pos);

	return " " + Util.bind("problem.atLine"/*nonNLS*/,String.valueOf(line)) +
	"\n\t"/*nonNLS*/ + new String(extract) + "\n\t"/*nonNLS*/ + new String(underneath);
}
/**
 * Answer back the original arguments recorded into the problem.
 * @return java.lang.String[]
 */
public String[] getArguments() {
	return arguments;
}
/**
 * Answer the type of problem.
 * @see com.ibm.compiler.java.problem.ProblemIrritants
 * @return int
 */
public int getID() {
	return id;
}
/**
 * Answer a localized, human-readable message string which describes the problem.
 * @return java.lang.String
 */
public String getMessage() {
	return message;
}
/**
 * Answer the file name in which the problem was found.
 * @return char[]
 */
public char[] getOriginatingFileName() {
	return fileName;
}
/**
 * Answer the severity of the problem.
 * @return int
 */
public int getSeverity() {
	return severity;
}
/**
 * Answer the end position of the problem (inclusive), or -1 if unknown.
 * @return int
 */
public int getSourceEnd() {
	return endPosition;
}
/**
 * Answer the line number in source where the problem begins.
 * @return int
 */
public int getSourceLineNumber() {
	return line;
}
/**
 * Answer the start position of the problem (inclusive), or -1 if unknown.
 * @return int
 */
public int getSourceStart() {
	return startPosition;
}
/*
 * Helper method: checks the severity to see if the Error bit is set.
 * @return boolean
 */
public boolean isError() {
	return (severity & ProblemSeverities.Error) != 0;
}
/*
 * Helper method: checks the severity to see if the Error bit is not set.
 * @return boolean
 */
public boolean isWarning() {
	return (severity & ProblemSeverities.Error) == 0;
}
/**
 * Set the end position of the problem (inclusive), or -1 if unknown.
 *
 * Used for shifting problem positions.
 * @param sourceEnd the new value of the sourceEnd of the receiver
 */
public void setSourceEnd(int sourceEnd) {
	endPosition = sourceEnd;
}
/**
 * Set the line number in source where the problem begins.
 * @param lineNumber the new value of the line number of the receiver
 */
public void setSourceLineNumber(int lineNumber) {
	line = lineNumber;
}
/**
 * Set the start position of the problem (inclusive), or -1 if unknown.
 *
 * Used for shifting problem positions.
 * @param sourceStart the new value of the source start position of the receiver
 */
public void setSourceStart(int sourceStart) {
	startPosition = sourceStart;
}
public String toString() {

	String s = "Pb("/*nonNLS*/ + (id & IgnoreCategoriesMask) + ") "/*nonNLS*/;
	if (message != null) {
		s += message;
	} else {
		if (arguments != null)
			for (int i = 0; i < arguments.length; i++)
				s += " "/*nonNLS*/ + arguments[i];
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8785.java