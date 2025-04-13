error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4048.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4048.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4048.java
text:
```scala
public v@@oid exitMethod(int declarationEnd, int defaultValueStart, int defaultValueEnd) {

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.jdom;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.jdom.*;
import org.eclipse.jdt.internal.compiler.ISourceElementRequestor;
import org.eclipse.jdt.internal.compiler.SourceElementParser;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.IGenericType;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
/**
 * A DOM builder that uses the SourceElementParser
 * @deprecated The JDOM was made obsolete by the addition in 2.0 of the more
 * powerful, fine-grained DOM/AST API found in the 
 * org.eclipse.jdt.core.dom package.
 */
public class SimpleDOMBuilder extends AbstractDOMBuilder implements ISourceElementRequestor {

/**
 * Does nothing.
 */
public void acceptProblem(IProblem problem) {
	// nothing to do
}

public void acceptImport(int declarationStart, int declarationEnd, char[] name, boolean onDemand, int modifiers) {
	int[] sourceRange = {declarationStart, declarationEnd};
	String importName = new String(name);
	/** name is set to contain the '*' */
	if (onDemand) {
		importName+=".*"; //$NON-NLS-1$
	}
	fNode= new DOMImport(fDocument, sourceRange, importName, onDemand, modifiers);
	addChild(fNode);	
}
public void acceptPackage(int declarationStart, int declarationEnd, char[] name) {
	int[] sourceRange= new int[] {declarationStart, declarationEnd};
	fNode= new DOMPackage(fDocument, sourceRange, CharOperation.charToString(name));
	addChild(fNode);	
}
/**
 * @see IDOMFactory#createCompilationUnit(String, String)
 */
public IDOMCompilationUnit createCompilationUnit(String sourceCode, String name) {
	return createCompilationUnit(sourceCode.toCharArray(), name.toCharArray());
}
/**
 * @see IDOMFactory#createCompilationUnit(String, String)
 */
public IDOMCompilationUnit createCompilationUnit(ICompilationUnit compilationUnit) {
	initializeBuild(compilationUnit.getContents(), true, true);
	getParser(JavaCore.getOptions()).parseCompilationUnit(compilationUnit, false/*diet parse*/);
	return super.createCompilationUnit(compilationUnit);
}
/**
 * Creates a new DOMMethod and inizializes.
 */
protected void enterAbstractMethod(MethodInfo methodInfo) {
		
	int[] sourceRange = {methodInfo.declarationStart, -1}; // will be fixed up on exit
	int[] nameRange = {methodInfo.nameSourceStart, methodInfo.nameSourceEnd};
	fNode = new DOMMethod(fDocument, sourceRange, CharOperation.charToString(methodInfo.name), nameRange, methodInfo.modifiers, 
		methodInfo.isConstructor, CharOperation.charToString(methodInfo.returnType),
		CharOperation.charArrayToStringArray(methodInfo.parameterTypes),
		CharOperation.charArrayToStringArray(methodInfo.parameterNames), 
		CharOperation.charArrayToStringArray(methodInfo.exceptionTypes));
	addChild(fNode);
	fStack.push(fNode);
	
	// type parameters not supported by JDOM
}
/**
 */
public void enterClass(TypeInfo typeInfo) {
	enterType(typeInfo);
}
/**
 */
public void enterConstructor(MethodInfo methodInfo) {
	/* see 1FVIIQZ */
	String nameString = new String(fDocument, methodInfo.nameSourceStart, methodInfo.nameSourceEnd - methodInfo.nameSourceStart);
	int openParenPosition = nameString.indexOf('(');
	if (openParenPosition > -1)
		methodInfo.nameSourceEnd = methodInfo.nameSourceStart + openParenPosition - 1;

	enterAbstractMethod(methodInfo);
}
/**
 */
public void enterEnum(TypeInfo typeInfo) {
	enterType(typeInfo);
}
/**
 */
public void enterField(FieldInfo fieldInfo) {

	int[] sourceRange = {fieldInfo.declarationStart, -1};
	int[] nameRange = {fieldInfo.nameSourceStart, fieldInfo.nameSourceEnd};
	boolean isSecondary= false;
	if (fNode instanceof DOMField) {
		isSecondary = fieldInfo.declarationStart == fNode.fSourceRange[0];
	}
	fNode = new DOMField(fDocument, sourceRange, CharOperation.charToString(fieldInfo.name), nameRange, 
		fieldInfo.modifiers, CharOperation.charToString(fieldInfo.type), isSecondary);
	addChild(fNode);
	fStack.push(fNode);
}
/**

 */
public void enterInitializer(int declarationSourceStart, int modifiers) {
	int[] sourceRange = {declarationSourceStart, -1};
	fNode = new DOMInitializer(fDocument, sourceRange, modifiers);
	addChild(fNode);
	fStack.push(fNode);
}
/**
 */
public void enterInterface(TypeInfo typeInfo) {
	enterType(typeInfo);
}
/**
 */
public void enterMethod(MethodInfo methodInfo) {
	enterAbstractMethod(methodInfo);
}
/**
 */
protected void enterType(TypeInfo typeInfo) {
	if (fBuildingType) {
		int[] sourceRange = {typeInfo.declarationStart, -1}; // will be fixed in the exit
		int[] nameRange = new int[] {typeInfo.nameSourceStart, typeInfo.nameSourceEnd};
		fNode = new DOMType(fDocument, sourceRange, new String(typeInfo.name), nameRange,
			typeInfo.modifiers, CharOperation.charArrayToStringArray(typeInfo.superinterfaces), typeInfo.kind == IGenericType.CLASS); // TODO (jerome) should pass in kind
		addChild(fNode);
		fStack.push(fNode);
		
		// type parameters not supported by JDOM
	}
}
/**
 * Finishes the configuration of the class DOM object which
 * was created by a previous enterClass call.
 *
 * @see ISourceElementRequestor#exitClass(int)
 */
public void exitClass(int declarationEnd) {
	exitType(declarationEnd);
}
/**
 * Finishes the configuration of the method DOM object which
 * was created by a previous enterConstructor call.
 *
 * @see ISourceElementRequestor#exitConstructor(int)
 */
public void exitConstructor(int declarationEnd) {
	exitMember(declarationEnd);
}
/**
 * Finishes the configuration of the class DOM object which
 * was created by a previous enterEnum call.
 *
 * @see ISourceElementRequestor#exitEnum(int)
 */
public void exitEnum(int declarationEnd) {
	exitType(declarationEnd);
}/**
 */
public void exitField(int initializationStart, int declarationEnd, int declarationSourceEnd) {
	exitMember(declarationEnd);
}
/**
 */
public void exitInitializer(int declarationEnd) {
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
protected SourceElementParser getParser(Map settings) {
	return new SourceElementParser(this, new DefaultProblemFactory(), new CompilerOptions(settings));
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4048.java