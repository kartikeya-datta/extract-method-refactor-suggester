error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4714.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4714.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4714.java
text:
```scala
e@@nclosingTypeNames[--depth] = null;

package org.eclipse.jdt.internal.core.search.indexing;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.core.index.*;
import org.eclipse.jdt.internal.compiler.util.*;
import org.eclipse.jdt.internal.core.index.impl.*;

import java.io.File;

/**
 * This class is used by the JavaParserIndexer. When parsing the java file, the requestor
 * recognises the java elements (methods, fields, ...) and add them to an index.
 */
public class SourceIndexerRequestor implements ISourceElementRequestor, IIndexConstants {
	SourceIndexer indexer;
	IDocument document;

	char[] packageName;
	char[][] enclosingTypeNames = new char[5][];
	int depth = 0;
	int methodDepth = 0;
	
public SourceIndexerRequestor(SourceIndexer indexer, IDocument document) {
	super();
	this.indexer = indexer;
	this.document= document;
}
/**
 * acceptConstructorReference method comment.
 */
public void acceptConstructorReference(char[] typeName, int argCount, int sourcePosition) {
	this.indexer.addConstructorReference(typeName, argCount);
	int lastDot = CharOperation.lastIndexOf('.', typeName);
	if (lastDot != -1) {
		char[][] qualification = CharOperation.splitOn('.', CharOperation.subarray(typeName, 0, lastDot));
		for (int i = 0, length = qualification.length; i < length; i++) {
			this.indexer.addNameReference(qualification[i]);
		}
	}
}
/**
 * acceptFieldReference method comment.
 */
public void acceptFieldReference(char[] fieldName, int sourcePosition) {
	this.indexer.addFieldReference(fieldName);
}
/**
 * acceptImport method comment.
 */
public void acceptImport(int declarationStart, int declarationEnd, char[] name, boolean onDemand) {
	char[][] qualification = CharOperation.splitOn('.', CharOperation.subarray(name, 0, CharOperation.lastIndexOf('.', name)));
	for (int i = 0, length = qualification.length; i < length; i++) {
		this.indexer.addNameReference(qualification[i]);
	}
}
/**
 * acceptLineSeparatorPositions method comment.
 */
public void acceptLineSeparatorPositions(int[] positions) {
}
/**
 * acceptMethodReference method comment.
 */
public void acceptMethodReference(char[] methodName, int argCount, int sourcePosition) {
	this.indexer.addMethodReference(methodName, argCount);
}
/**
 * acceptPackage method comment.
 */
public void acceptPackage(int declarationStart, int declarationEnd, char[] name) {
	this.packageName = name;
}
/**
 * acceptProblem method comment.
 */
public void acceptProblem(IProblem problem) {
}
/**
 * acceptTypeReference method comment.
 */
public void acceptTypeReference(char[][] typeName, int sourceStart, int sourceEnd) {
	int length = typeName.length;
	for (int i = 0; i < length - 1; i++)
		acceptUnknownReference(typeName[i], 0); // ?
	acceptTypeReference(typeName[length - 1], 0);
}
/**
 * acceptTypeReference method comment.
 */
public void acceptTypeReference(char[] simpleTypeName, int sourcePosition) {
	this.indexer.addTypeReference(simpleTypeName);
}
/**
 * acceptUnknownReference method comment.
 */
public void acceptUnknownReference(char[][] name, int sourceStart, int sourceEnd) {
	for (int i = 0; i < name.length; i++) {
		acceptUnknownReference(name[i], 0);
	}
}
/**
 * acceptUnknownReference method comment.
 */
public void acceptUnknownReference(char[] name, int sourcePosition) {
	this.indexer.addNameReference(name);
}
/*
 * Rebuild the proper qualification for the current source type:
 *
 * java.lang.Object ---> null
 * java.util.Hashtable$Entry --> [Hashtable]
 * x.y.A$B$C --> [A, B]
 */
public char[][] enclosingTypeNames(){

	if (depth == 0) return null;

	char[][] qualification = new char[this.depth][];
	System.arraycopy(this.enclosingTypeNames, 0, qualification, 0, this.depth);
	return qualification;
}
/**
 * enterClass method comment.
 */
public void enterClass(int declarationStart, int modifiers, char[] name, int nameSourceStart, int nameSourceEnd, char[] superclass, char[][] superinterfaces) {

	// eliminate possible qualifications, given they need to be fully resolved again
	if (superclass != null){
		superclass = CharOperation.lastSegment(superclass, '.');
	}
	if (superinterfaces != null){
		for (int i = 0, length = superinterfaces.length; i < length; i++){
			superinterfaces[i] = CharOperation.lastSegment(superinterfaces[i], '.');
		}
	}
	char[][] enclosingTypeNames;
	if (this.methodDepth > 0) {
		enclosingTypeNames = ONE_ZERO_CHAR;
	} else {
		enclosingTypeNames = this.enclosingTypeNames();
	}
	this.indexer.addClassDeclaration(modifiers, packageName, name, enclosingTypeNames, superclass, superinterfaces);
	this.pushTypeName(name);
}
/**
 * enterCompilationUnit method comment.
 */
public void enterCompilationUnit() {
}
/**
 * enterConstructor method comment.
 */
public void enterConstructor(int declarationStart, int modifiers, char[] name, int nameSourceStart, int nameSourceEnd, char[][] parameterTypes, char[][] parameterNames, char[][] exceptionTypes) {
	this.indexer.addConstructorDeclaration(name, parameterTypes, exceptionTypes);
	this.methodDepth++;
}
/**
 * enterField method comment.
 */
public void enterField(int declarationStart, int modifiers, char[] type, char[] name, int nameSourceStart, int nameSourceEnd) {
	this.indexer.addFieldDeclaration(type, name);
	this.methodDepth++;
}
/**
 * enterInitializer method comment.
 */
public void enterInitializer(int declarationSourceStart, int modifiers) {
	this.methodDepth++;
}
/**
 * enterInterface method comment.
 */
public void enterInterface(int declarationStart, int modifiers, char[] name, int nameSourceStart, int nameSourceEnd, char[][] superinterfaces) {
	// eliminate possible qualifications, given they need to be fully resolved again
	if (superinterfaces != null){
		for (int i = 0, length = superinterfaces.length; i < length; i++){
			superinterfaces[i] = CharOperation.lastSegment(superinterfaces[i], '.');
		}
	}	
	char[][] enclosingTypeNames;
	if (this.methodDepth > 0) {
		enclosingTypeNames = ONE_ZERO_CHAR;
	} else {
		enclosingTypeNames = this.enclosingTypeNames();
	}
	this.indexer.addInterfaceDeclaration(modifiers, packageName, name, enclosingTypeNames, superinterfaces);
	this.pushTypeName(name);	
}
/**
 * enterMethod method comment.
 */
public void enterMethod(int declarationStart, int modifiers, char[] returnType, char[] name, int nameSourceStart, int nameSourceEnd, char[][] parameterTypes, char[][] parameterNames, char[][] exceptionTypes) {
	this.indexer.addMethodDeclaration(name, parameterTypes, returnType, exceptionTypes);
	this.methodDepth++;
}
/**
 * exitClass method comment.
 */
public void exitClass(int declarationEnd) {
	popTypeName();
}
/**
 * exitCompilationUnit method comment.
 */
public void exitCompilationUnit(int declarationEnd) {
}
/**
 * exitConstructor method comment.
 */
public void exitConstructor(int declarationEnd) {
	this.methodDepth--;
}
/**
 * exitField method comment.
 */
public void exitField(int declarationEnd) {
	this.methodDepth--;
}
/**
 * exitInitializer method comment.
 */
public void exitInitializer(int declarationEnd) {
	this.methodDepth--;
}
/**
 * exitInterface method comment.
 */
public void exitInterface(int declarationEnd) {
	popTypeName();	
}
/**
 * exitMethod method comment.
 */
public void exitMethod(int declarationEnd) {
	this.methodDepth--;
}
public void popTypeName(){
	try {
	enclosingTypeNames[depth--] = null;
	} catch (ArrayIndexOutOfBoundsException e) {
		e.printStackTrace();
	}
}
public void pushTypeName(char[] typeName){
	if (depth == enclosingTypeNames.length){
		System.arraycopy(enclosingTypeNames, 0, enclosingTypeNames = new char[depth*2][], 0, depth);
	}
	enclosingTypeNames[depth++] = typeName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4714.java