error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4218.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4218.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4218.java
text:
```scala
t@@his.fieldDeclaration.print(tab + 1, result);

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

/**
 * Internal initializer structure for parsing recovery 
 */
import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Initializer;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.BaseTypes;
import org.eclipse.jdt.internal.compiler.lookup.CompilerModifiers;

public class RecoveredInitializer extends RecoveredField implements CompilerModifiers, TerminalTokens, BaseTypes {

	public RecoveredType[] localTypes;
	public int localTypeCount;

	public RecoveredBlock initializerBody;	

public RecoveredInitializer(FieldDeclaration fieldDeclaration, RecoveredElement parent, int bracketBalance){
	this(fieldDeclaration, parent, bracketBalance, null);
}
public RecoveredInitializer(FieldDeclaration fieldDeclaration, RecoveredElement parent, int bracketBalance, Parser parser){
	super(fieldDeclaration, parent, bracketBalance, parser);
	this.foundOpeningBrace = true;
}
/*
 * Record a nested block declaration
 */
public RecoveredElement add(Block nestedBlockDeclaration, int bracketBalanceValue) {

	/* default behavior is to delegate recording to parent if any,
	do not consider elements passed the known end (if set)
	it must be belonging to an enclosing element 
	*/
	if (fieldDeclaration.declarationSourceEnd > 0
			&& nestedBlockDeclaration.sourceStart > fieldDeclaration.declarationSourceEnd){
		if (this.parent == null) return this; // ignore
		return this.parent.add(nestedBlockDeclaration, bracketBalanceValue);
	}
	/* consider that if the opening brace was not found, it is there */
	if (!foundOpeningBrace){
		foundOpeningBrace = true;
		this.bracketBalance++;
	}
	initializerBody = new RecoveredBlock(nestedBlockDeclaration, this, bracketBalanceValue);
	if (nestedBlockDeclaration.sourceEnd == 0) return initializerBody;
	return this;
}
/*
 * Record a field declaration (act like inside method body)
 */
public RecoveredElement add(FieldDeclaration newFieldDeclaration, int bracketBalanceValue) {

	/* local variables inside initializer can only be final and non void */
	char[][] fieldTypeName;
	if ((newFieldDeclaration.modifiers & ~AccFinal) != 0 /* local var can only be final */
 (newFieldDeclaration.type == null) // initializer
 ((fieldTypeName = newFieldDeclaration.type.getTypeName()).length == 1 // non void
				&& CharOperation.equals(fieldTypeName[0], VoidBinding.sourceName()))){ 
		if (this.parent == null) return this; // ignore
		this.updateSourceEndIfNecessary(this.previousAvailableLineEnd(newFieldDeclaration.declarationSourceStart - 1));
		return this.parent.add(newFieldDeclaration, bracketBalanceValue);
	}

	/* default behavior is to delegate recording to parent if any,
	do not consider elements passed the known end (if set)
	it must be belonging to an enclosing element 
	*/
	if (this.fieldDeclaration.declarationSourceEnd > 0
			&& newFieldDeclaration.declarationSourceStart > this.fieldDeclaration.declarationSourceEnd){
		if (this.parent == null) return this; // ignore
		return this.parent.add(newFieldDeclaration, bracketBalanceValue);
	}
	// still inside initializer, treat as local variable
	return this; // ignore
}
/*
 * Record a local declaration - regular method should have been created a block body
 */
public RecoveredElement add(LocalDeclaration localDeclaration, int bracketBalanceValue) {

	/* do not consider a type starting passed the type end (if set)
		it must be belonging to an enclosing type */
	if (fieldDeclaration.declarationSourceEnd != 0 
			&& localDeclaration.declarationSourceStart > fieldDeclaration.declarationSourceEnd){
		if (parent == null) return this; // ignore
		return this.parent.add(localDeclaration, bracketBalanceValue);
	}
	/* method body should have been created */
	Block block = new Block(0);
	block.sourceStart = ((Initializer)fieldDeclaration).sourceStart;
	RecoveredElement element = this.add(block, 1);
	return element.add(localDeclaration, bracketBalanceValue);	
}
/*
 * Record a statement - regular method should have been created a block body
 */
public RecoveredElement add(Statement statement, int bracketBalanceValue) {

	/* do not consider a statement starting passed the initializer end (if set)
		it must be belonging to an enclosing type */
	if (fieldDeclaration.declarationSourceEnd != 0 
			&& statement.sourceStart > fieldDeclaration.declarationSourceEnd){
		if (parent == null) return this; // ignore
		return this.parent.add(statement, bracketBalanceValue);
	}
	/* initializer body should have been created */
	Block block = new Block(0);
	block.sourceStart = ((Initializer)fieldDeclaration).sourceStart;
	RecoveredElement element = this.add(block, 1);
	return element.add(statement, bracketBalanceValue);	
}
public RecoveredElement add(TypeDeclaration typeDeclaration, int bracketBalanceValue) {

	/* do not consider a type starting passed the type end (if set)
		it must be belonging to an enclosing type */
	if (fieldDeclaration.declarationSourceEnd != 0 
			&& typeDeclaration.declarationSourceStart > fieldDeclaration.declarationSourceEnd){
		if (parent == null) return this; // ignore
		return this.parent.add(typeDeclaration, bracketBalanceValue);
	}
	if ((typeDeclaration.bits & ASTNode.IsLocalTypeMASK) != 0){
		/* method body should have been created */
		Block block = new Block(0);
		block.sourceStart = ((Initializer)fieldDeclaration).sourceStart;
		RecoveredElement element = this.add(block, 1);
		return element.add(typeDeclaration, bracketBalanceValue);	
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
public String toString(int tab) {
	StringBuffer result = new StringBuffer(tabString(tab));
	result.append("Recovered initializer:\n"); //$NON-NLS-1$
	result.append(this.fieldDeclaration.print(tab + 1, result));
	if (this.initializerBody != null) {
		result.append("\n"); //$NON-NLS-1$
		result.append(this.initializerBody.toString(tab + 1));
	}
	return result.toString();
}
public FieldDeclaration updatedFieldDeclaration(){

	if (initializerBody != null){
		Block block = initializerBody.updatedBlock();
		if (block != null){
			((Initializer)fieldDeclaration).block = block;
		}
		if (this.localTypeCount > 0) fieldDeclaration.bits |= ASTNode.HasLocalTypeMASK;

	}	
	if (fieldDeclaration.sourceEnd == 0){
		fieldDeclaration.sourceEnd = fieldDeclaration.declarationSourceEnd;
	}
	return fieldDeclaration;
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
public RecoveredElement updateOnOpeningBrace(int braceStart, int braceEnd){
	bracketBalance++;
	return this; // request to restart
}
/*
 * Update the declarationSourceEnd of the corresponding parse node
 */
public void updateSourceEndIfNecessary(int braceStart, int braceEnd){
	if (this.fieldDeclaration.declarationSourceEnd == 0) {
		Initializer initializer = (Initializer)fieldDeclaration;
		if(parser().rBraceSuccessorStart >= braceEnd) {
			initializer.declarationSourceEnd = parser().rBraceEnd;
			initializer.bodyEnd = parser().rBraceStart;
		} else {
			initializer.declarationSourceEnd = braceEnd;
			initializer.bodyEnd  = braceStart - 1;
		}
		if(initializer.block != null) {
			initializer.block.sourceEnd = initializer.declarationSourceEnd;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4218.java