error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3003.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3003.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3003.java
text:
```scala
i@@f (typeDeclaration.kind() == IGenericType.INTERFACE_DECL) {

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
package org.eclipse.jdt.internal.compiler.parser;

import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ExplicitConstructorCall;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.SuperReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.env.IGenericType;
import org.eclipse.jdt.internal.compiler.lookup.BaseTypes;
import org.eclipse.jdt.internal.compiler.lookup.CompilerModifiers;

/**
 * Internal method structure for parsing recovery 
 */

public class RecoveredMethod extends RecoveredElement implements CompilerModifiers, TerminalTokens, BaseTypes {

	public AbstractMethodDeclaration methodDeclaration;

	public RecoveredType[] localTypes;
	public int localTypeCount;

	public RecoveredBlock methodBody;
	public boolean discardBody = true;

public RecoveredMethod(AbstractMethodDeclaration methodDeclaration, RecoveredElement parent, int bracketBalance, Parser parser){
	super(parent, bracketBalance, parser);
	this.methodDeclaration = methodDeclaration;
	this.foundOpeningBrace = !bodyStartsAtHeaderEnd();
	if(this.foundOpeningBrace) {
		this.bracketBalance++;
	}
}
/*
 * Record a nested block declaration
 */
public RecoveredElement add(Block nestedBlockDeclaration, int bracketBalanceValue) {

	/* default behavior is to delegate recording to parent if any,
	do not consider elements passed the known end (if set)
	it must be belonging to an enclosing element 
	*/
	if (methodDeclaration.declarationSourceEnd > 0
		&& nestedBlockDeclaration.sourceStart
			> methodDeclaration.declarationSourceEnd){
				if (this.parent == null){
					return this; // ignore
				} else {
					return this.parent.add(nestedBlockDeclaration, bracketBalanceValue);
				}
	}
	/* consider that if the opening brace was not found, it is there */
	if (!foundOpeningBrace){
		foundOpeningBrace = true;
		this.bracketBalance++;
	}

	methodBody = new RecoveredBlock(nestedBlockDeclaration, this, bracketBalanceValue);
	if (nestedBlockDeclaration.sourceEnd == 0) return methodBody;
	return this;
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

		if (this.parent == null){
			return this; // ignore
		} else {
			this.updateSourceEndIfNecessary(this.previousAvailableLineEnd(fieldDeclaration.declarationSourceStart - 1));
			return this.parent.add(fieldDeclaration, bracketBalanceValue);
		}
	}
	/* default behavior is to delegate recording to parent if any,
	do not consider elements passed the known end (if set)
	it must be belonging to an enclosing element 
	*/
	if (methodDeclaration.declarationSourceEnd > 0
		&& fieldDeclaration.declarationSourceStart
			> methodDeclaration.declarationSourceEnd){
		if (this.parent == null){
			return this; // ignore
		} else {
			return this.parent.add(fieldDeclaration, bracketBalanceValue);
		}
	}
	/* consider that if the opening brace was not found, it is there */
	if (!foundOpeningBrace){
		foundOpeningBrace = true;
		this.bracketBalance++;
	}
	// still inside method, treat as local variable
	return this; // ignore
}
/*
 * Record a local declaration - regular method should have been created a block body
 */
public RecoveredElement add(LocalDeclaration localDeclaration, int bracketBalanceValue) {

	/* local variables inside method can only be final and non void */
/*	
	char[][] localTypeName; 
	if ((localDeclaration.modifiers & ~AccFinal) != 0 // local var can only be final 
 (localDeclaration.type == null) // initializer
 ((localTypeName = localDeclaration.type.getTypeName()).length == 1 // non void
			&& CharOperation.equals(localTypeName[0], VoidBinding.sourceName()))){ 

		if (this.parent == null){
			return this; // ignore
		} else {
			this.updateSourceEndIfNecessary(this.previousAvailableLineEnd(localDeclaration.declarationSourceStart - 1));
			return this.parent.add(localDeclaration, bracketBalance);
		}
	}
*/
	/* do not consider a type starting passed the type end (if set)
		it must be belonging to an enclosing type */
	if (methodDeclaration.declarationSourceEnd != 0 
		&& localDeclaration.declarationSourceStart > methodDeclaration.declarationSourceEnd){
			
		if (this.parent == null) {
			return this; // ignore
		} else {
			return this.parent.add(localDeclaration, bracketBalanceValue);
		}
	}
	if (methodBody == null){
		Block block = new Block(0);
		block.sourceStart = methodDeclaration.bodyStart;
		RecoveredElement currentBlock = this.add(block, 1);
		if (this.bracketBalance > 0){
			for (int i = 0; i < this.bracketBalance - 1; i++){
				currentBlock = currentBlock.add(new Block(0), 1);
			}
			this.bracketBalance = 1;
		}
		return currentBlock.add(localDeclaration, bracketBalanceValue);
	}
	return methodBody.add(localDeclaration, bracketBalanceValue, true);
}
/*
 * Record a statement - regular method should have been created a block body
 */
public RecoveredElement add(Statement statement, int bracketBalanceValue) {

	/* do not consider a type starting passed the type end (if set)
		it must be belonging to an enclosing type */
	if (methodDeclaration.declarationSourceEnd != 0 
		&& statement.sourceStart > methodDeclaration.declarationSourceEnd){

		if (this.parent == null) {
			return this; // ignore
		} else {
			return this.parent.add(statement, bracketBalanceValue);
		}
	}
	if (methodBody == null){
		Block block = new Block(0);
		block.sourceStart = methodDeclaration.bodyStart;
		RecoveredElement currentBlock = this.add(block, 1);
		if (this.bracketBalance > 0){
			for (int i = 0; i < this.bracketBalance - 1; i++){
				currentBlock = currentBlock.add(new Block(0), 1);
			}
			this.bracketBalance = 1;
		}
		return currentBlock.add(statement, bracketBalanceValue);
	}
	return methodBody.add(statement, bracketBalanceValue, true);	
}
public RecoveredElement add(TypeDeclaration typeDeclaration, int bracketBalanceValue) {

	/* do not consider a type starting passed the type end (if set)
		it must be belonging to an enclosing type */
	if (methodDeclaration.declarationSourceEnd != 0 
		&& typeDeclaration.declarationSourceStart > methodDeclaration.declarationSourceEnd){
			
		if (this.parent == null) {
			return this; // ignore
		}
		return this.parent.add(typeDeclaration, bracketBalanceValue);
	}
	if ((typeDeclaration.bits & ASTNode.IsLocalTypeMASK) != 0){
		if (methodBody == null){
			Block block = new Block(0);
			block.sourceStart = methodDeclaration.bodyStart;
			this.add(block, 1);
		}
		return methodBody.add(typeDeclaration, bracketBalanceValue, true);	
	}
	if (typeDeclaration.kind() == IGenericType.INTERFACE) {
		this.updateSourceEndIfNecessary(this.previousAvailableLineEnd(typeDeclaration.declarationSourceStart - 1));
		if (this.parent == null) {
			return this; // ignore
		}
		// close the constructor
		return this.parent.add(typeDeclaration, bracketBalanceValue);
	}
	if (localTypes == null) {
		localTypes = new RecoveredType[5];
		localTypeCount = 0;
	} else {
		if (localTypeCount == localTypes.length) {
			System.arraycopy(
				localTypes, 
				0, 
				(localTypes = new RecoveredType[2 * localTypeCount]), 
				0, 
				localTypeCount); 
		}
	}
	RecoveredType element = new RecoveredType(typeDeclaration, this, bracketBalanceValue);
	localTypes[localTypeCount++] = element;

	/* consider that if the opening brace was not found, it is there */
	if (!foundOpeningBrace){
		foundOpeningBrace = true;
		this.bracketBalance++;
	}
	return element;
}
public boolean bodyStartsAtHeaderEnd(){
	return methodDeclaration.bodyStart == methodDeclaration.sourceEnd+1;
}
/* 
 * Answer the associated parsed structure
 */
public ASTNode parseTree(){
	return methodDeclaration;
}
/*
 * Answer the very source end of the corresponding parse node
 */
public int sourceEnd(){
	return this.methodDeclaration.declarationSourceEnd;
}
public String toString(int tab) {
	StringBuffer result = new StringBuffer(tabString(tab));
	result.append("Recovered method:\n"); //$NON-NLS-1$
	this.methodDeclaration.print(tab + 1, result);
	if (this.localTypes != null) {
		for (int i = 0; i < this.localTypeCount; i++) {
			result.append("\n"); //$NON-NLS-1$
			result.append(this.localTypes[i].toString(tab + 1));
		}
	}
	if (this.methodBody != null) {
		result.append("\n"); //$NON-NLS-1$
		result.append(this.methodBody.toString(tab + 1));
	}
	return result.toString();
}
/*
 * Update the bodyStart of the corresponding parse node
 */
public void updateBodyStart(int bodyStart){
	this.foundOpeningBrace = true;		
	this.methodDeclaration.bodyStart = bodyStart;
}
public AbstractMethodDeclaration updatedMethodDeclaration(){

	if (methodBody != null){
		Block block = methodBody.updatedBlock();
		if (block != null){
			methodDeclaration.statements = block.statements;

			/* first statement might be an explict constructor call destinated to a special slot */
			if (methodDeclaration.isConstructor()) {
				ConstructorDeclaration constructor = (ConstructorDeclaration)methodDeclaration;
				if (methodDeclaration.statements != null
					&& methodDeclaration.statements[0] instanceof ExplicitConstructorCall){
					constructor.constructorCall = (ExplicitConstructorCall)methodDeclaration.statements[0];
					int length = methodDeclaration.statements.length;
					System.arraycopy(
						methodDeclaration.statements, 
						1, 
						(methodDeclaration.statements = new Statement[length-1]),
						0,
						length-1);
					}
					if (constructor.constructorCall == null){ // add implicit constructor call
						constructor.constructorCall = SuperReference.implicitSuperConstructorCall();
					}
			}
		}
	}
	if (localTypeCount > 0) methodDeclaration.bits |= ASTNode.HasLocalTypeMASK;
	return methodDeclaration;
}
/*
 * Update the corresponding parse node from parser state which
 * is about to disappear because of restarting recovery
 */
public void updateFromParserState(){

	if(this.bodyStartsAtHeaderEnd()){
		Parser parser = this.parser();
		/* might want to recover arguments or thrown exceptions */
		if (parser.listLength > 0 && parser.astLengthPtr > 0){ // awaiting interface type references
			/* has consumed the arguments - listed elements must be thrown exceptions */
			if (methodDeclaration.sourceEnd == parser.rParenPos) {
				
				// protection for bugs 15142
				int length = parser.astLengthStack[parser.astLengthPtr];
				int astPtr = parser.astPtr - length;
				boolean canConsume = astPtr >= 0;
				if(canConsume) {
					if((!(parser.astStack[astPtr] instanceof AbstractMethodDeclaration))) {
						canConsume = false;
					}
					for (int i = 1, max = length + 1; i < max; i++) {
						if(!(parser.astStack[astPtr + i ] instanceof TypeReference)) {
							canConsume = false;
						}
					}
				}
				if (canConsume){
					parser.consumeMethodHeaderThrowsClause(); 
					// will reset typeListLength to zero
					// thus this check will only be performed on first errorCheck after void foo() throws X, Y,
				} else {
					parser.listLength = 0;
				}
			} else {
				/* has not consumed arguments yet, listed elements must be arguments */
				if (parser.currentToken == TokenNameLPAREN || parser.currentToken == TokenNameSEMICOLON){
					/* if currentToken is parenthesis this last argument is a method/field signature */
					parser.astLengthStack[parser.astLengthPtr] --; 
					parser.astPtr --; 
					parser.listLength --;
					parser.currentToken = 0;
				}
				int argLength = parser.astLengthStack[parser.astLengthPtr];
				int argStart = parser.astPtr - argLength + 1;
				boolean needUpdateRParenPos = parser.rParenPos < parser.lParenPos; // 12387 : rParenPos will be used
				// to compute bodyStart, and thus used to set next checkpoint.
				int count;
				for (count = 0; count < argLength; count++){
					Argument argument = (Argument)parser.astStack[argStart+count];
					/* cannot be an argument if non final */
					char[][] argTypeName = argument.type.getTypeName();
					if ((argument.modifiers & ~AccFinal) != 0
 (argTypeName.length == 1
							&& CharOperation.equals(argTypeName[0], VoidBinding.sourceName()))){
						parser.astLengthStack[parser.astLengthPtr] = count; 
						parser.astPtr = argStart+count-1; 
						parser.listLength = count;
						parser.currentToken = 0;
						break;
					}
					if (needUpdateRParenPos) parser.rParenPos = argument.sourceEnd + 1;
				}
				if (parser.listLength > 0 && parser.astLengthPtr > 0){
					
					// protection for bugs 15142
					int length = parser.astLengthStack[parser.astLengthPtr];
					int astPtr = parser.astPtr - length;
					boolean canConsume = astPtr >= 0;
					if(canConsume) {
						if((!(parser.astStack[astPtr] instanceof AbstractMethodDeclaration))) {
							canConsume = false;
						}
						for (int i = 1, max = length + 1; i < max; i++) {
							if(!(parser.astStack[astPtr + i ] instanceof Argument)) {
								canConsume = false;
							}
						}
					}
					if(canConsume) {
						parser.consumeMethodHeaderRightParen();
						/* fix-up positions, given they were updated against rParenPos, which did not get set */
						if (parser.currentElement == this){ // parameter addition might have added an awaiting (no return type) method - see 1FVXQZ4 */
							methodDeclaration.sourceEnd = methodDeclaration.arguments[methodDeclaration.arguments.length-1].sourceEnd;
							methodDeclaration.bodyStart = methodDeclaration.sourceEnd+1;
							parser.lastCheckPoint = methodDeclaration.bodyStart;
						}
					}
				}
			}
		}
	}
}
/*
 * An opening brace got consumed, might be the expected opening one of the current element,
 * in which case the bodyStart is updated.
 */
public RecoveredElement updateOnOpeningBrace(int braceStart, int braceEnd){

	/* in case the opening brace is close enough to the signature */
	if (bracketBalance == 0){
		/*
			if (parser.scanner.searchLineNumber(methodDeclaration.sourceEnd) 
				!= parser.scanner.searchLineNumber(braceEnd)){
		 */
		switch(parser().lastIgnoredToken){
			case -1 :
			case TokenNamethrows :
				break;
			default:
				this.foundOpeningBrace = true;				
				bracketBalance = 1; // pretend the brace was already there
		}
	}	
	return super.updateOnOpeningBrace(braceStart, braceEnd);
}
public void updateParseTree(){
	this.updatedMethodDeclaration();
}
/*
 * Update the declarationSourceEnd of the corresponding parse node
 */
public void updateSourceEndIfNecessary(int braceStart, int braceEnd){
	if (this.methodDeclaration.declarationSourceEnd == 0) {
		if(parser().rBraceSuccessorStart >= braceEnd) {
			this.methodDeclaration.declarationSourceEnd = parser().rBraceEnd;
			this.methodDeclaration.bodyEnd = parser().rBraceStart;
		} else {
			this.methodDeclaration.declarationSourceEnd = braceEnd;
			this.methodDeclaration.bodyEnd  = braceStart - 1;
		}
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3003.java