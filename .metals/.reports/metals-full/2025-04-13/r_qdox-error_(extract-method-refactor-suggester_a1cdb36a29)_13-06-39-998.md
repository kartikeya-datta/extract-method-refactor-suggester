error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/485.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/485.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/485.java
text:
```scala
r@@esult.append(unitDeclaration.print(tab + 1, result));

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
 * Internal field structure for parsing recovery 
 */
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AnonymousLocalTypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AstNode;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;
import org.eclipse.jdt.internal.compiler.ast.LocalTypeDeclaration;
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
	if (typeCount > 0){
		RecoveredType type = this.types[typeCount -1];
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
	if (typeCount > 0){
		RecoveredType type = this.types[typeCount -1];
		type.bodyEnd = 0; // reset position
		type.typeDeclaration.declarationSourceEnd = 0; // reset position
		type.typeDeclaration.bodyEnd = 0;
		return type.add(fieldDeclaration, bracketBalanceValue);
	}
	return this; // ignore
}
public RecoveredElement add(ImportReference importReference, int bracketBalanceValue) {
	if (imports == null) {
		imports = new RecoveredImport[5];
		importCount = 0;
	} else {
		if (importCount == imports.length) {
			System.arraycopy(
				imports, 
				0, 
				(imports = new RecoveredImport[2 * importCount]), 
				0, 
				importCount); 
		}
	}
	RecoveredImport element = new RecoveredImport(importReference, this, bracketBalanceValue);
	imports[importCount++] = element;

	/* if import not finished, then import becomes current */
	if (importReference.declarationSourceEnd == 0) return element;
	return this;		
}
public RecoveredElement add(TypeDeclaration typeDeclaration, int bracketBalanceValue) {
	
	if (typeDeclaration instanceof AnonymousLocalTypeDeclaration){
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
	if (types == null) {
		types = new RecoveredType[5];
		typeCount = 0;
	} else {
		if (typeCount == types.length) {
			System.arraycopy(
				types, 
				0, 
				(types = new RecoveredType[2 * typeCount]), 
				0, 
				typeCount); 
		}
	}
	RecoveredType element = new RecoveredType(typeDeclaration, this, bracketBalanceValue);
	types[typeCount++] = element;

	/* if type not finished, then type becomes current */
	if (typeDeclaration.declarationSourceEnd == 0) return element;
	return this;	
}
/* 
 * Answer the associated parsed structure
 */
public AstNode parseTree(){
	return unitDeclaration;
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
	result.append(unitDeclaration.toString(tab + 1));
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
	if (importCount > 0){
		ImportReference[] importRefences = new ImportReference[importCount];
		for (int i = 0; i < importCount; i++){
			importRefences[i] = imports[i].updatedImportReference();
		}
		unitDeclaration.imports = importRefences;
	}
	/* update types */
	if (typeCount > 0){
		int existingCount = unitDeclaration.types == null ? 0 : unitDeclaration.types.length;
		TypeDeclaration[] typeDeclarations = new TypeDeclaration[existingCount + typeCount];
		if (existingCount > 0){
			System.arraycopy(unitDeclaration.types, 0, typeDeclarations, 0, existingCount);
		}
		// may need to update the declarationSourceEnd of the last type
		if (types[typeCount - 1].typeDeclaration.declarationSourceEnd == 0){
			types[typeCount - 1].typeDeclaration.declarationSourceEnd = unitDeclaration.sourceEnd;
			types[typeCount - 1].typeDeclaration.bodyEnd = unitDeclaration.sourceEnd;
		}
		int actualCount = existingCount;
		for (int i = 0; i < typeCount; i++){
			TypeDeclaration typeDecl = types[i].updatedTypeDeclaration();
			// filter out local types (12454)
			if (!(typeDecl instanceof LocalTypeDeclaration)){
				typeDeclarations[actualCount++] = typeDecl;
			}
		}
		if (actualCount != typeCount){
			System.arraycopy(
				typeDeclarations, 
				0, 
				typeDeclarations = new TypeDeclaration[existingCount+actualCount], 
				0, 
				existingCount+actualCount);
		}
		unitDeclaration.types = typeDeclarations;
	}
	return unitDeclaration;
}
public void updateParseTree(){
	this.updatedCompilationUnitDeclaration();
}
/*
 * Update the sourceEnd of the corresponding parse node
 */
public void updateSourceEndIfNecessary(int sourceEnd){
	if (this.unitDeclaration.sourceEnd == 0)
		this.unitDeclaration.sourceEnd = sourceEnd;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/485.java