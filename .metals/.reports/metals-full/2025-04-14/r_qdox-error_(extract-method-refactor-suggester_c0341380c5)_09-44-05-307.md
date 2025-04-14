error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9496.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9496.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9496.java
text:
```scala
t@@his.unitDeclaration.print(tab + 1, result);

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
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;

public class RecoveredUnit extends RecoveredElement {

	public CompilationUnitDeclaration unitDeclaration;
	
	public RecoveredImport[] imports;
	public int importCount;
	public RecoveredType[] types;
	public int typeCount;
public RecoveredUnit(CompilationUnitDeclaration unitDeclaration, int bracketBalance, Parser parser){
	super(null, bracketBalance, parser);
	this.unitDeclaration = unitDeclaration;
}
/*
 *	Record a method declaration: should be attached to last type
 */
public RecoveredElement add(AbstractMethodDeclaration methodDeclaration, int bracketBalanceValue) {

	/* attach it to last type - if any */
	if (this.typeCount > 0){
		RecoveredType type = this.types[this.typeCount -1];
		type.bodyEnd = 0; // reset position
		type.typeDeclaration.declarationSourceEnd = 0; // reset position
		type.typeDeclaration.bodyEnd = 0;
		return type.add(methodDeclaration, bracketBalanceValue);
	}
	return this; // ignore
}
/*
 *	Record a field declaration: should be attached to last type
 */
public RecoveredElement add(FieldDeclaration fieldDeclaration, int bracketBalanceValue) {

	/* attach it to last type - if any */
	if (this.typeCount > 0){
		RecoveredType type = this.types[this.typeCount -1];
		type.bodyEnd = 0; // reset position
		type.typeDeclaration.declarationSourceEnd = 0; // reset position
		type.typeDeclaration.bodyEnd = 0;
		return type.add(fieldDeclaration, bracketBalanceValue);
	}
	return this; // ignore
}
public RecoveredElement add(ImportReference importReference, int bracketBalanceValue) {
	if (this.imports == null) {
		this.imports = new RecoveredImport[5];
		this.importCount = 0;
	} else {
		if (this.importCount == this.imports.length) {
			System.arraycopy(
				this.imports, 
				0, 
				(this.imports = new RecoveredImport[2 * this.importCount]), 
				0, 
				this.importCount); 
		}
	}
	RecoveredImport element = new RecoveredImport(importReference, this, bracketBalanceValue);
	this.imports[this.importCount++] = element;

	/* if import not finished, then import becomes current */
	if (importReference.declarationSourceEnd == 0) return element;
	return this;		
}
public RecoveredElement add(TypeDeclaration typeDeclaration, int bracketBalanceValue) {
	
	if ((typeDeclaration.bits & ASTNode.IsAnonymousTypeMASK) != 0){
		if (this.typeCount > 0) {
			// add it to the last type
			RecoveredType lastType = this.types[this.typeCount-1];
			lastType.bodyEnd = 0; // reopen type
			lastType.typeDeclaration.bodyEnd = 0; // reopen type
			lastType.typeDeclaration.declarationSourceEnd = 0; // reopen type
			lastType.bracketBalance++; // expect one closing brace
			return lastType.add(typeDeclaration, bracketBalanceValue);
		}
	}
	if (this.types == null) {
		this.types = new RecoveredType[5];
		this.typeCount = 0;
	} else {
		if (this.typeCount == this.types.length) {
			System.arraycopy(
				this.types, 
				0, 
				(this.types = new RecoveredType[2 * this.typeCount]), 
				0, 
				this.typeCount); 
		}
	}
	RecoveredType element = new RecoveredType(typeDeclaration, this, bracketBalanceValue);
	this.types[this.typeCount++] = element;

	/* if type not finished, then type becomes current */
	if (typeDeclaration.declarationSourceEnd == 0) return element;
	return this;	
}
/* 
 * Answer the associated parsed structure
 */
public ASTNode parseTree(){
	return this.unitDeclaration;
}
/*
 * Answer the very source end of the corresponding parse node
 */
public int sourceEnd(){
	return this.unitDeclaration.sourceEnd;
}
public String toString(int tab) {
	StringBuffer result = new StringBuffer(tabString(tab));
	result.append("Recovered unit: [\n"); //$NON-NLS-1$
	result.append(this.unitDeclaration.print(tab + 1, result));
	result.append(tabString(tab + 1));
	result.append("]"); //$NON-NLS-1$
	if (this.imports != null) {
		for (int i = 0; i < this.importCount; i++) {
			result.append("\n"); //$NON-NLS-1$
			result.append(this.imports[i].toString(tab + 1));
		}
	}
	if (this.types != null) {
		for (int i = 0; i < this.typeCount; i++) {
			result.append("\n"); //$NON-NLS-1$
			result.append(this.types[i].toString(tab + 1));
		}
	}
	return result.toString();
}
public CompilationUnitDeclaration updatedCompilationUnitDeclaration(){

	/* update imports */
	if (this.importCount > 0){
		ImportReference[] importRefences = new ImportReference[this.importCount];
		for (int i = 0; i < this.importCount; i++){
			importRefences[i] = this.imports[i].updatedImportReference();
		}
		this.unitDeclaration.imports = importRefences;
	}
	/* update types */
	if (this.typeCount > 0){
		int existingCount = this.unitDeclaration.types == null ? 0 : this.unitDeclaration.types.length;
		TypeDeclaration[] typeDeclarations = new TypeDeclaration[existingCount + this.typeCount];
		if (existingCount > 0){
			System.arraycopy(this.unitDeclaration.types, 0, typeDeclarations, 0, existingCount);
		}
		// may need to update the declarationSourceEnd of the last type
		if (this.types[this.typeCount - 1].typeDeclaration.declarationSourceEnd == 0){
			this.types[this.typeCount - 1].typeDeclaration.declarationSourceEnd = this.unitDeclaration.sourceEnd;
			this.types[this.typeCount - 1].typeDeclaration.bodyEnd = this.unitDeclaration.sourceEnd;
		}
		int actualCount = existingCount;
		for (int i = 0; i < this.typeCount; i++){
			TypeDeclaration typeDecl = this.types[i].updatedTypeDeclaration();
			// filter out local types (12454)
			if ((typeDecl.bits & ASTNode.IsLocalTypeMASK) == 0){
				typeDeclarations[actualCount++] = typeDecl;
			}
		}
		if (actualCount != this.typeCount){
			System.arraycopy(
				typeDeclarations, 
				0, 
				typeDeclarations = new TypeDeclaration[existingCount+actualCount], 
				0, 
				existingCount+actualCount);
		}
		this.unitDeclaration.types = typeDeclarations;
	}
	return this.unitDeclaration;
}
public void updateParseTree(){
	this.updatedCompilationUnitDeclaration();
}
/*
 * Update the sourceEnd of the corresponding parse node
 */
public void updateSourceEndIfNecessary(int bodyStart, int bodyEnd){
	if (this.unitDeclaration.sourceEnd == 0)
		this.unitDeclaration.sourceEnd = bodyEnd;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9496.java