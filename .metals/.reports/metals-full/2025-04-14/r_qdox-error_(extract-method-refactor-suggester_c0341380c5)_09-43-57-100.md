error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6792.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6792.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6792.java
text:
```scala
b@@lockDeclaration.print(tab + 1, result);

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
 * Internal block structure for parsing recovery 
 */
import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.AstNode;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.BaseTypes;
import org.eclipse.jdt.internal.compiler.lookup.CompilerModifiers;

public class RecoveredBlock extends RecoveredStatement implements CompilerModifiers, TerminalTokens, BaseTypes {

	public Block blockDeclaration;

	public RecoveredStatement[] statements;
	public int statementCount;

	public boolean preserveContent = false;
	public RecoveredLocalVariable pendingArgument;
public RecoveredBlock(Block block, RecoveredElement parent, int bracketBalance){
	super(block, parent, bracketBalance);
	this.blockDeclaration = block;
	this.foundOpeningBrace = true;
}
/*
 * Record a nested block declaration 
 */
public RecoveredElement add(Block nestedBlockDeclaration, int bracketBalanceValue) {

	/* do not consider a nested block starting passed the block end (if set)
		it must be belonging to an enclosing block */
	if (blockDeclaration.sourceEnd != 0 
		&& nestedBlockDeclaration.sourceStart > blockDeclaration.sourceEnd){
		return this.parent.add(nestedBlockDeclaration, bracketBalanceValue);
	}
			
	RecoveredBlock element = new RecoveredBlock(nestedBlockDeclaration, this, bracketBalanceValue);

	// if we have a pending Argument, promote it into the new block
	if (pendingArgument != null){
		element.attach(pendingArgument);
		pendingArgument = null;
	}
	this.attach(element);
	if (nestedBlockDeclaration.sourceEnd == 0) return element;
	return this;	
}
/*
 * Record a local declaration 
 */
public RecoveredElement add(LocalDeclaration localDeclaration, int bracketBalanceValue) {
	return this.add(localDeclaration, bracketBalanceValue, false);
}
/*
 * Record a local declaration 
 */
public RecoveredElement add(LocalDeclaration localDeclaration, int bracketBalanceValue, boolean delegatedByParent) {

	/* local variables inside method can only be final and non void */
/*	
	char[][] localTypeName; 
	if ((localDeclaration.modifiers & ~AccFinal) != 0 // local var can only be final 
 (localDeclaration.type == null) // initializer
 ((localTypeName = localDeclaration.type.getTypeName()).length == 1 // non void
			&& CharOperation.equals(localTypeName[0], VoidBinding.sourceName()))){ 

		if (delegatedByParent){
			return this; //ignore
		} else {
			this.updateSourceEndIfNecessary(this.previousAvailableLineEnd(localDeclaration.declarationSourceStart - 1));
			return this.parent.add(localDeclaration, bracketBalance);
		}
	}
*/	
		/* do not consider a local variable starting passed the block end (if set)
		it must be belonging to an enclosing block */
	if (blockDeclaration.sourceEnd != 0 
		&& localDeclaration.declarationSourceStart > blockDeclaration.sourceEnd){

		if (delegatedByParent){
			return this; //ignore
		} else {
			return this.parent.add(localDeclaration, bracketBalanceValue);
		}
	}

	RecoveredLocalVariable element = new RecoveredLocalVariable(localDeclaration, this, bracketBalanceValue);

	if (localDeclaration instanceof Argument){
		pendingArgument = element;
		return this;
	}
	
	this.attach(element);
	if (localDeclaration.declarationSourceEnd == 0) return element;
	return this;	
}
/*
 * Record a statement declaration 
 */
public RecoveredElement add(Statement stmt, int bracketBalanceValue) {
	return this.add(stmt, bracketBalanceValue, false);
}

/*
 * Record a statement declaration 
 */
public RecoveredElement add(Statement stmt, int bracketBalanceValue, boolean delegatedByParent) {

	/* do not consider a nested block starting passed the block end (if set)
		it must be belonging to an enclosing block */
	if (blockDeclaration.sourceEnd != 0 
		&& stmt.sourceStart > blockDeclaration.sourceEnd){
			
		if (delegatedByParent){
			return this; //ignore
		} else {
			return this.parent.add(stmt, bracketBalanceValue);
		}			
	}
			
	RecoveredStatement element = new RecoveredStatement(stmt, this, bracketBalanceValue);
	this.attach(element);
	if (stmt.sourceEnd == 0) return element;
	return this;	
}
/*
 * Addition of a type to an initializer (act like inside method body)
 */
public RecoveredElement add(TypeDeclaration typeDeclaration, int bracketBalanceValue) {
	return this.add(typeDeclaration, bracketBalanceValue, false);
}
/*
 * Addition of a type to an initializer (act like inside method body)
 */
public RecoveredElement add(TypeDeclaration typeDeclaration, int bracketBalanceValue, boolean delegatedByParent) {

	/* do not consider a type starting passed the block end (if set)
		it must be belonging to an enclosing block */
	if (blockDeclaration.sourceEnd != 0 
		&& typeDeclaration.declarationSourceStart > blockDeclaration.sourceEnd){
		if (delegatedByParent){
			return this; //ignore
		} else {
			return this.parent.add(typeDeclaration, bracketBalanceValue);
		}
	}
			
	RecoveredStatement element = new RecoveredType(typeDeclaration, this, bracketBalanceValue);
	this.attach(element);
	if (typeDeclaration.declarationSourceEnd == 0) return element;
	return this;
}
/*
 * Attach a recovered statement
 */
void attach(RecoveredStatement recoveredStatement) {

	if (statements == null) {
		statements = new RecoveredStatement[5];
		statementCount = 0;
	} else {
		if (statementCount == statements.length) {
			System.arraycopy(
				statements, 
				0, 
				(statements = new RecoveredStatement[2 * statementCount]), 
				0, 
				statementCount); 
		}
	}
	statements[statementCount++] = recoveredStatement;
}
/* 
 * Answer the associated parsed structure
 */
public AstNode parseTree(){
	return blockDeclaration;
}
public String toString(int tab) {
	StringBuffer result = new StringBuffer(tabString(tab));
	result.append("Recovered block:\n"); //$NON-NLS-1$
	result.append(blockDeclaration.toString(tab + 1));
	if (this.statements != null) {
		for (int i = 0; i < this.statementCount; i++) {
			result.append("\n"); //$NON-NLS-1$
			result.append(this.statements[i].toString(tab + 1));
		}
	}
	return result.toString();
}
/*
 * Rebuild a block from the nested structure which is in scope
 */
public Block updatedBlock(){

	// if block was not marked to be preserved or empty, then ignore it
	if (!preserveContent || statementCount == 0) return null;

	Statement[] updatedStatements = new Statement[statementCount];
	int updatedCount = 0;
	
	// only collect the non-null updated statements
	for (int i = 0; i < statementCount; i++){
		Statement updatedStatement = statements[i].updatedStatement();
		if (updatedStatement != null){
			updatedStatements[updatedCount++] = updatedStatement;
		}
	}
	if (updatedCount == 0) return null; // not interesting block

	// resize statement collection if necessary
	if (updatedCount != statementCount){
		blockDeclaration.statements = new Statement[updatedCount];
		System.arraycopy(updatedStatements, 0, blockDeclaration.statements, 0, updatedCount);
	} else {
		blockDeclaration.statements = updatedStatements;
	}

	return blockDeclaration;
}
/*
 * Rebuild a statement from the nested structure which is in scope
 */
public Statement updatedStatement(){

	return this.updatedBlock();
}
/*
 * A closing brace got consumed, might have closed the current element,
 * in which case both the currentElement is exited
 */
public RecoveredElement updateOnClosingBrace(int braceStart, int braceEnd){
	if ((--bracketBalance <= 0) && (parent != null)){
		this.updateSourceEndIfNecessary(braceEnd);

		/* if the block is the method body, then it closes the method too */
		RecoveredMethod method = enclosingMethod();
		if (method != null && method.methodBody == this){
			return parent.updateOnClosingBrace(braceStart, braceEnd);
		}
		RecoveredInitializer initializer = enclosingInitializer();
		if (initializer != null && initializer.initializerBody == this){
			return parent.updateOnClosingBrace(braceStart, braceEnd);
		}
		return parent;
	}
	return this;
}
/*
 * An opening brace got consumed, might be the expected opening one of the current element,
 * in which case the bodyStart is updated.
 */
public RecoveredElement updateOnOpeningBrace(int currentPosition){

	// create a nested block
	Block block = new Block(0);
	block.sourceStart = parser().scanner.startPosition;
	return this.add(block, 1);
}
/*
 * Final update the corresponding parse node
 */
public void updateParseTree(){

	this.updatedBlock();
}
/*
 * Rebuild a flattened block from the nested structure which is in scope
 */
public Statement updateStatement(){

	// if block was closed or empty, then ignore it
	if (this.blockDeclaration.sourceEnd != 0 || statementCount == 0) return null;

	Statement[] updatedStatements = new Statement[statementCount];
	int updatedCount = 0;
	
	// only collect the non-null updated statements
	for (int i = 0; i < statementCount; i++){
		Statement updatedStatement = statements[i].updatedStatement();
		if (updatedStatement != null){
			updatedStatements[updatedCount++] = updatedStatement;
		}
	}
	if (updatedCount == 0) return null; // not interesting block

	// resize statement collection if necessary
	if (updatedCount != statementCount){
		blockDeclaration.statements = new Statement[updatedCount];
		System.arraycopy(updatedStatements, 0, blockDeclaration.statements, 0, updatedCount);
	} else {
		blockDeclaration.statements = updatedStatements;
	}

	return blockDeclaration;
}

/*
 * Record a field declaration 
 */
public RecoveredElement add(FieldDeclaration fieldDeclaration, int bracketBalanceValue) {

	/* local variables inside method can only be final and non void */
	char[][] fieldTypeName; 
	if ((fieldDeclaration.modifiers & ~AccFinal) != 0 // local var can only be final 
 (fieldDeclaration.type == null) // initializer
 ((fieldTypeName = fieldDeclaration.type.getTypeName()).length == 1 // non void
			&& CharOperation.equals(fieldTypeName[0], VoidBinding.sourceName()))){ 
		this.updateSourceEndIfNecessary(this.previousAvailableLineEnd(fieldDeclaration.declarationSourceStart - 1));
		return this.parent.add(fieldDeclaration, bracketBalanceValue);
	}
	
	/* do not consider a local variable starting passed the block end (if set)
		it must be belonging to an enclosing block */
	if (blockDeclaration.sourceEnd != 0 
		&& fieldDeclaration.declarationSourceStart > blockDeclaration.sourceEnd){
		return this.parent.add(fieldDeclaration, bracketBalanceValue);
	}

	// ignore the added field, since indicates a local variable behind recovery point
	// which thus got parsed as a field reference. This can happen if restarting after
	// having reduced an assistNode to get the following context (see 1GEK7SG)
	return this;	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6792.java