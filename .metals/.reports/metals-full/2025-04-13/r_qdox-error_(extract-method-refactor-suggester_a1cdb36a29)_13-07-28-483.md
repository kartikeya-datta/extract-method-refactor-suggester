error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8870.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8870.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8870.java
text:
```scala
public v@@oid updateSourceEndIfNecessary(int braceStart, int braceEnd){

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
package org.eclipse.jdt.internal.compiler.parser;

/**
 * Internal structure for parsing recovery 
 */
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AstNode;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;

public class RecoveredElement {

	public RecoveredElement parent;
	public int bracketBalance;
	public boolean foundOpeningBrace;
	protected Parser recoveringParser;
public RecoveredElement(RecoveredElement parent, int bracketBalance){
	this(parent, bracketBalance, null);
}
public RecoveredElement(RecoveredElement parent, int bracketBalance, Parser parser){
	this.parent = parent;
	this.bracketBalance = bracketBalance;
	this.recoveringParser = parser;
}
/*
 *	Record a method declaration
 */
public RecoveredElement add(AbstractMethodDeclaration methodDeclaration, int bracketBalanceValue) {

	/* default behavior is to delegate recording to parent if any */
	if (parent == null) {
		return this; // ignore
	} else {
		this.updateSourceEndIfNecessary(this.previousAvailableLineEnd(methodDeclaration.declarationSourceStart - 1));	
		return this.parent.add(methodDeclaration, bracketBalanceValue);
	}
}
/*
 * Record a nested block declaration
 */
public RecoveredElement add(Block nestedBlockDeclaration, int bracketBalanceValue) {

	/* default behavior is to delegate recording to parent if any */
	if (parent == null) {
		return this; // ignore
	} else {
		this.updateSourceEndIfNecessary(this.previousAvailableLineEnd(nestedBlockDeclaration.sourceStart - 1));	
		return this.parent.add(nestedBlockDeclaration, bracketBalanceValue);
	}
}
/*
 * Record a field declaration
 */
public RecoveredElement add(FieldDeclaration fieldDeclaration, int bracketBalanceValue) {

	/* default behavior is to delegate recording to parent if any */
	if (parent == null) {
		return this; // ignore
	} else {
		this.updateSourceEndIfNecessary(this.previousAvailableLineEnd(fieldDeclaration.declarationSourceStart - 1));	
		return this.parent.add(fieldDeclaration, bracketBalanceValue);
	}
}
/*
 *	Record an import reference
 */
public RecoveredElement add(ImportReference importReference, int bracketBalanceValue){

	/* default behavior is to delegate recording to parent if any */
	if (parent == null) {
		return this; // ignore
	} else {
		this.updateSourceEndIfNecessary(this.previousAvailableLineEnd(importReference.declarationSourceStart - 1));	
		return this.parent.add(importReference, bracketBalanceValue);
	}
}
/*
 * Record a local declaration
 */
public RecoveredElement add(LocalDeclaration localDeclaration, int bracketBalanceValue) {

	/* default behavior is to delegate recording to parent if any */
	if (parent == null) {
		return this; // ignore
	} else {
		this.updateSourceEndIfNecessary(this.previousAvailableLineEnd(localDeclaration.declarationSourceStart - 1));	
		return this.parent.add(localDeclaration, bracketBalanceValue);
	}
}
/*
 * Record a statement
 */
public RecoveredElement add(Statement statement, int bracketBalanceValue) {

	/* default behavior is to delegate recording to parent if any */
	if (parent == null) {
		return this; // ignore
	} else {
		this.updateSourceEndIfNecessary(this.previousAvailableLineEnd(statement.sourceStart - 1));	
		return this.parent.add(statement, bracketBalanceValue);
	}
}
/*
 *	Record a type declaration
 */
public RecoveredElement add(TypeDeclaration typeDeclaration, int bracketBalanceValue){

	/* default behavior is to delegate recording to parent if any */
	if (parent == null) {
		return this; // ignore
	} else {
		this.updateSourceEndIfNecessary(this.previousAvailableLineEnd(typeDeclaration.declarationSourceStart - 1));	
		return this.parent.add(typeDeclaration, bracketBalanceValue);
	}
}
/*
 * Answer the depth of this element, considering the parent link.
 */
public int depth(){
	int depth = 0;
	RecoveredElement current = this;
	while ((current = current.parent) != null) depth++;
	return depth;
}
/*
 * Answer the enclosing method node, or null if none
 */
public RecoveredInitializer enclosingInitializer(){
	RecoveredElement current = this;
	while (current != null){
		if (current instanceof RecoveredInitializer){
			return (RecoveredInitializer) current;
		}
		current = current.parent;
	}
	return null;
}
/*
 * Answer the enclosing method node, or null if none
 */
public RecoveredMethod enclosingMethod(){
	RecoveredElement current = this;
	while (current != null){
		if (current instanceof RecoveredMethod){
			return (RecoveredMethod) current;
		}
		current = current.parent;
	}
	return null;
}
/*
 * Answer the enclosing type node, or null if none
 */
public RecoveredType enclosingType(){
	RecoveredElement current = this;
	while (current != null){
		if (current instanceof RecoveredType){
			return (RecoveredType) current;
		}
		current = current.parent;
	}
	return null;
}
/*
 * Answer the closest specified parser
 */
public Parser parser(){
	RecoveredElement current = this;
	while (current != null){
		if (current.recoveringParser != null){
			return current.recoveringParser;
		}
		current = current.parent;
	}
	return null;
}
/* 
 * Answer the associated parsed structure
 */
public AstNode parseTree(){
	return null;
}
/*
 * Iterate the enclosing blocks and tag them so as to preserve their content
 */
public void preserveEnclosingBlocks(){
	RecoveredElement current = this;
	while (current != null){
		if (current instanceof RecoveredBlock){
			((RecoveredBlock)current).preserveContent = true;
		}
		if (current instanceof RecoveredType){ // for anonymous types
			((RecoveredType)current).preserveContent = true;
		}
		current = current.parent;
	}
}
/*
 * Answer the position of the previous line end if
 * there is nothing but spaces in between it and the
 * line end. Used to trim spaces on unclosed elements.
 */
public int previousAvailableLineEnd(int position){

	Parser parser = this.parser();
	if (parser == null) return position;
	
	Scanner scanner = parser.scanner;
	if (scanner.lineEnds == null) return position;
	
	int index = scanner.getLineNumber(position);
	if (index < 2) return position;
	int previousLineEnd = scanner.lineEnds[index-2];

	char[] source = scanner.source;
	for (int i = previousLineEnd+1; i < position; i++){
		if (!(source[i] == ' ' || source[i] == '\t')) return position;
	}
	return previousLineEnd;
}
/*
 * Answer the very source end of the corresponding parse node
 */
public int sourceEnd(){
	return 0;
}
protected String tabString(int tab) {
	StringBuffer result = new StringBuffer();
	for (int i = tab; i > 0; i--) {
		result.append("  "); //$NON-NLS-1$
	}
	return result.toString();
}
/*
 * Answer the top node
 */
public RecoveredElement topElement(){
	RecoveredElement current = this;
	while (current.parent != null){
		current = current.parent;
	}
	return current;
}
public String toString() {
	return toString(0);
}
public String toString(int tab) {
	return super.toString();
}
/*
 * Answer the enclosing type node, or null if none
 */
public RecoveredType type(){
	RecoveredElement current = this;
	while (current != null){
		if (current instanceof RecoveredType){
			return (RecoveredType) current;
		}
		current = current.parent;
	}
	return null;
}
/*
 * Update the bodyStart of the corresponding parse node
 */
public void updateBodyStart(int bodyStart){
	this.foundOpeningBrace = true;	
}
/*
 * Update the corresponding parse node from parser state which
 * is about to disappear because of restarting recovery
 */
public void updateFromParserState(){
}
/*
 * A closing brace got consumed, might have closed the current element,
 * in which case both the currentElement is exited
 */
public RecoveredElement updateOnClosingBrace(int braceStart, int braceEnd){
	if ((--bracketBalance <= 0) && (parent != null)){
		this.updateSourceEndIfNecessary(braceStart, braceEnd);
		return parent;
	}
	return this;
}
/*
 * An opening brace got consumed, might be the expected opening one of the current element,
 * in which case the bodyStart is updated.
 */
/*public RecoveredElement updateOnOpeningBrace(int braceEnd){return null;}*/
public RecoveredElement updateOnOpeningBrace(int braceStart, int braceEnd){

	if (bracketBalance++ == 0){
		this.updateBodyStart(braceEnd + 1);
		return this;
	}
	return null; // no update is necessary
}
/*
 * Final update the corresponding parse node
 */
public void updateParseTree(){
}
/*
 * Update the declarationSourceEnd of the corresponding parse node
 */
public void updateSourceEndIfNecessary(int bodyStart, int bodyEnd){
}
public void updateSourceEndIfNecessary(int sourceEnd){
	this.updateSourceEndIfNecessary(sourceEnd + 1, sourceEnd);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8870.java