error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1115.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1115.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1115.java
text:
```scala
i@@mportName+=".*"; //$NON-NLS-1$

package org.eclipse.jdt.internal.core.jdom;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.core.jdom.*;
import org.eclipse.jdt.internal.core.util.*;

import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Stack;

/**
 * A DOM builder that uses the SourceElementParser
 */
public class SimpleDOMBuilder extends AbstractDOMBuilder implements ISourceElementRequestor {

/**

 */
public void acceptImport(int declarationStart, int declarationEnd, char[] name, boolean onDemand) {
	int[] sourceRange = {declarationStart, declarationEnd};
	String importName = new String(name);
	/** name is set to contain the '*' */
	if (onDemand) {
		importName+=".*"/*nonNLS*/;
	}
	fNode= new DOMImport(fDocument, sourceRange, importName, onDemand);
	addChild(fNode);	
}
/**

 */
public void acceptInitializer(int modifiers, int declarationSourceStart, int declarationSourceEnd) {
	int[] sourceRange = {declarationSourceStart, declarationSourceEnd};
	fNode = new DOMInitializer(fDocument, sourceRange, modifiers);
	addChild(fNode);
}
/**
 */
public void acceptPackage(int declarationStart, int declarationEnd, char[] name) {
	int[] sourceRange= new int[] {declarationStart, declarationEnd};
	fNode= new DOMPackage(fDocument, sourceRange, CharArrayOps.charToString(name));
	addChild(fNode);	
}
/**
 * @see IDOMFactory#createCompilationUnit(String)
 */
public IDOMCompilationUnit createCompilationUnit(String sourceCode, String name) {
	return createCompilationUnit(sourceCode.toCharArray(), name.toCharArray());
}
/**
 * @see IDOMFactory#createCompilationUnit(String)
 */
public IDOMCompilationUnit createCompilationUnit(ICompilationUnit compilationUnit) {
	initializeBuild(compilationUnit.getContents(), true, true);
	getParser().parseCompilationUnit(compilationUnit, false);
	return super.createCompilationUnit(compilationUnit);
}
/**
 * Creates a new DOMMethod and inizializes.
 *
 * @param declarationStart - a source position corresponding to the first character 
 *		of this constructor declaration
 * @param modifiers - the modifiers for this constructor converted to a flag
 * @param returnType - the name of the return type
 * @param name - the name of this constructor
 * @param nameStart - a source position corresponding to the first character of the name
 * @param nameEnd - a source position corresponding to the last character of the name
 * @param parameterTypes - a list of parameter type names
 * @param parameterNames - a list of the names of the parameters
 * @param exceptionTypes - a list of the exception types
 */
protected void enterAbstractMethod(int declarationStart, int modifiers,
	char[] returnType, char[] name, int nameStart, int nameEnd, char[][] parameterTypes,
	char[][] parameterNames, char[][] exceptionTypes, boolean isConstructor) {
		
	int[] sourceRange = {declarationStart, -1}; // will be fixed up on exit
	int[] nameRange = {nameStart, nameEnd};
	fNode = new DOMMethod(fDocument, sourceRange, CharArrayOps.charToString(name), nameRange, modifiers, 
		isConstructor, CharArrayOps.charToString(returnType),
		CharArrayOps.charcharToString(parameterTypes),
		CharArrayOps.charcharToString(parameterNames), 
		CharArrayOps.charcharToString(exceptionTypes));
	addChild(fNode);
	fStack.push(fNode);
}
/**
 */
public void enterClass(int declarationStart, int modifiers, char[] name, int nameStart, int nameEnd, char[] superclass, char[][] superinterfaces) {
	enterType(declarationStart, modifiers, name, nameStart, nameEnd, superclass,
		superinterfaces, true);
}
/**
 */
public void enterConstructor(int declarationStart, int modifiers, char[] name, int nameStart, int nameEnd, char[][] parameterTypes, char[][] parameterNames, char[][] exceptionTypes) {
	/* see 1FVIIQZ */
	String nameString = new String(fDocument, nameStart, nameEnd - nameStart);
	int openParenPosition = nameString.indexOf('(');
	if (openParenPosition > -1)
		nameEnd = nameStart + openParenPosition - 1;

	enterAbstractMethod(declarationStart, modifiers, 
		null, name, nameStart, nameEnd, parameterTypes,
		parameterNames, exceptionTypes,true);
}
/**
 */
public void enterField(int declarationStart, int modifiers, char[] type, char[] name, int nameStart, int nameEnd) {

	int[] sourceRange = {declarationStart, -1};
	int[] nameRange = {nameStart, nameEnd};
	boolean isSecondary= false;
	if (fNode instanceof DOMField) {
		isSecondary = declarationStart == fNode.fSourceRange[0];
	}
	fNode = new DOMField(fDocument, sourceRange, CharArrayOps.charToString(name), nameRange, 
		modifiers, CharArrayOps.charToString(type), isSecondary);
	addChild(fNode);
	fStack.push(fNode);
}
/**
 */
public void enterInterface(int declarationStart, int modifiers, char[] name, int nameStart, int nameEnd, char[][] superinterfaces) {
	enterType(declarationStart, modifiers, name, nameStart, nameEnd, null,
		superinterfaces, false);
}
/**
 */
public void enterMethod(int declarationStart, int modifiers, char[] returnType, char[] name, int nameStart, int nameEnd, char[][] parameterTypes, char[][] parameterNames, char[][] exceptionTypes) {
	enterAbstractMethod(declarationStart, modifiers, 
		null, name, nameStart, nameEnd, parameterTypes,
		parameterNames, exceptionTypes,false);
}
/**
 */
protected void enterType(int declarationStart, int modifiers, char[] name, 
	int nameStart, int nameEnd, char[] superclass, char[][] superinterfaces, boolean isClass) {
	if (fBuildingType) {
		int[] sourceRange = {declarationStart, -1}; // will be fixed in the exit
		int[] nameRange = new int[] {nameStart, nameEnd};
		fNode = new DOMType(fDocument, sourceRange, new String(name), nameRange,
			modifiers, CharArrayOps.charcharToString(superinterfaces), isClass);
		addChild(fNode);
		fStack.push(fNode);
	}
}
/**
 * Finishes the configuration of the class DOM object which
 * was created by a previous enterClass call.
 *
 * @see ISourceElementRequestor.exitClass(...);
 */
public void exitClass(int declarationEnd) {
	exitType(declarationEnd);
}
/**
 * Finishes the configuration of the method DOM object which
 * was created by a previous enterConstructor call.
 *
 * @see ISourceElementRequestor.exitConstructor(...);
 */
public void exitConstructor(int declarationEnd) {
	exitMember(declarationEnd);
}
/**
 */
public void exitField(int declarationEnd) {
	exitMember(declarationEnd);
}
/**
 */
public void exitInterface(int declarationEnd) {
	exitType(declarationEnd);
}
/**
 * Finishes the configuration of the member.
 *
 * @param declarationEnd - a source position corresponding to the end of the method
 *		declaration.  This can include whitespace and comments following the closing bracket.
 */
protected void exitMember(int declarationEnd) {
	DOMMember m= (DOMMember) fStack.pop();
	m.setSourceRangeEnd(declarationEnd);
	fNode = m;
}
/**
 */
public void exitMethod(int declarationEnd) {
	exitMember(declarationEnd);
}
/**
 * @see AbstractDOMBuilder#exitType
 *
 * @param declarationEnd - a source position corresponding to the end of the class
 *		declaration.  This can include whitespace and comments following the closing bracket.
 */
protected void exitType(int declarationEnd) {
	exitType(declarationEnd, declarationEnd);
}
/**
 * Creates a new parser.
 */
protected SourceElementParser getParser() {
	return new SourceElementParser(this, new NullProblemFactory());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1115.java