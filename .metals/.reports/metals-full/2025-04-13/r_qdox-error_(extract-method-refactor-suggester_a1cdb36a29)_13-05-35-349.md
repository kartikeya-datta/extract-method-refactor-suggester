error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/675.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/675.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/675.java
text:
```scala
f@@ieldDeclaration.print(tab + 1, buffer);

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
 * Internal field structure for parsing recovery 
 */
import org.eclipse.jdt.internal.compiler.ast.ArrayTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;

public class RecoveredField extends RecoveredElement {

	public FieldDeclaration fieldDeclaration;
	boolean alreadyCompletedFieldInitialization;
	
	public RecoveredType[] anonymousTypes;
	public int anonymousTypeCount;
public RecoveredField(FieldDeclaration fieldDeclaration, RecoveredElement parent, int bracketBalance){
	this(fieldDeclaration, parent, bracketBalance, null);
}
public RecoveredField(FieldDeclaration fieldDeclaration, RecoveredElement parent, int bracketBalance, Parser parser){
	super(parent, bracketBalance, parser);
	this.fieldDeclaration = fieldDeclaration;
	this.alreadyCompletedFieldInitialization = fieldDeclaration.initialization != null;
}
/*
 * Record an expression statement if field is expecting an initialization expression,
 * used for completion inside field initializers.
 */
public RecoveredElement add(Statement statement, int bracketBalanceValue) {

	if (this.alreadyCompletedFieldInitialization || !(statement instanceof Expression)) {
		return super.add(statement, bracketBalanceValue);
	} else {
		this.alreadyCompletedFieldInitialization = true;
		this.fieldDeclaration.initialization = (Expression)statement;
		this.fieldDeclaration.declarationSourceEnd = statement.sourceEnd;
		this.fieldDeclaration.declarationEnd = statement.sourceEnd;
		return this;
	}
}
/*
 * Record a type declaration if this field is expecting an initialization expression 
 * and the type is an anonymous type.
 * Used for completion inside field initializers.
 */
public RecoveredElement add(TypeDeclaration typeDeclaration, int bracketBalanceValue) {

	if (this.alreadyCompletedFieldInitialization 
 ((typeDeclaration.bits & ASTNode.IsAnonymousTypeMASK) == 0)
 (this.fieldDeclaration.declarationSourceEnd != 0 && typeDeclaration.sourceStart > this.fieldDeclaration.declarationSourceEnd)) {
		return super.add(typeDeclaration, bracketBalanceValue);
	} else { 
		// Prepare anonymous type list
		if (this.anonymousTypes == null) {
			this.anonymousTypes = new RecoveredType[5];
			this.anonymousTypeCount = 0;
		} else {
			if (this.anonymousTypeCount == this.anonymousTypes.length) {
				System.arraycopy(
					this.anonymousTypes, 
					0, 
					(this.anonymousTypes = new RecoveredType[2 * this.anonymousTypeCount]), 
					0, 
					this.anonymousTypeCount); 
			}
		}
		// Store type declaration as an anonymous type
		RecoveredType element = new RecoveredType(typeDeclaration, this, bracketBalanceValue);
		this.anonymousTypes[this.anonymousTypeCount++] = element;
		return element;
	}
}
/* 
 * Answer the associated parsed structure
 */
public ASTNode parseTree(){
	return fieldDeclaration;
}
/*
 * Answer the very source end of the corresponding parse node
 */
public int sourceEnd(){
	return this.fieldDeclaration.declarationSourceEnd;
}
public String toString(int tab){
	StringBuffer buffer = new StringBuffer(tabString(tab));
	buffer.append("Recovered field:\n"); //$NON-NLS-1$
	buffer.append(fieldDeclaration.print(tab + 1, buffer));
	if (this.anonymousTypes != null) {
		for (int i = 0; i < this.anonymousTypeCount; i++){
			buffer.append("\n"); //$NON-NLS-1$
			buffer.append(anonymousTypes[i].toString(tab + 1));
		}
	}
	return buffer.toString();
}
public FieldDeclaration updatedFieldDeclaration(){

	if (this.anonymousTypes != null && fieldDeclaration.initialization == null) {
		for (int i = 0; i < this.anonymousTypeCount; i++){
			if (anonymousTypes[i].preserveContent){
				fieldDeclaration.initialization = this.anonymousTypes[i].updatedTypeDeclaration().allocation;
			}
		}
		if (this.anonymousTypeCount > 0) fieldDeclaration.bits |= ASTNode.HasLocalTypeMASK;
	}
	return fieldDeclaration;
}
/*
 * A closing brace got consumed, might have closed the current element,
 * in which case both the currentElement is exited.
 *
 * Fields have no associated braces, thus if matches, then update parent.
 */
public RecoveredElement updateOnClosingBrace(int braceStart, int braceEnd){
	if (bracketBalance > 0){ // was an array initializer
		bracketBalance--;
		if (bracketBalance == 0) alreadyCompletedFieldInitialization = true;
		return this;
	}
	if (parent != null){
		return parent.updateOnClosingBrace(braceStart, braceEnd);
	}
	return this;
}
/*
 * An opening brace got consumed, might be the expected opening one of the current element,
 * in which case the bodyStart is updated.
 */
public RecoveredElement updateOnOpeningBrace(int braceStart, int braceEnd){
	if (fieldDeclaration.declarationSourceEnd == 0 
		&& fieldDeclaration.type instanceof ArrayTypeReference
		&& !alreadyCompletedFieldInitialization){
		bracketBalance++;
		return null; // no update is necessary	(array initializer)
	}
	// might be an array initializer
	this.updateSourceEndIfNecessary(braceStart - 1, braceEnd - 1);	
	return this.parent.updateOnOpeningBrace(braceStart, braceEnd);	
}
public void updateParseTree(){
	this.updatedFieldDeclaration();
}
/*
 * Update the declarationSourceEnd of the corresponding parse node
 */
public void updateSourceEndIfNecessary(int bodyStart, int bodyEnd){
	if (this.fieldDeclaration.declarationSourceEnd == 0) {
		this.fieldDeclaration.declarationSourceEnd = bodyEnd;
		this.fieldDeclaration.declarationEnd = bodyEnd;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/675.java