error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8011.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8011.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8011.java
text:
```scala
R@@ecoveredMethod element = new RecoveredMethod(methodDeclaration, this, bracketBalance, this.recoveringParser);

package org.eclipse.jdt.internal.compiler.parser;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.util.*;

/**
 * Internal type structure for parsing recovery 
 */

public class RecoveredType extends RecoveredStatement implements TerminalSymbols, CompilerModifiers {
	public TypeDeclaration typeDeclaration;

	public RecoveredType[] memberTypes;
	public int memberTypeCount;
	public RecoveredField[] fields;
	public int fieldCount;
	public RecoveredMethod[] methods;
	public int methodCount;

	public boolean preserveContent = false;	// only used for anonymous types
	public int bodyEnd;
public RecoveredType(TypeDeclaration typeDeclaration, RecoveredElement parent, int bracketBalance){
	super(typeDeclaration, parent, bracketBalance);
	this.typeDeclaration = typeDeclaration;
	this.foundOpeningBrace = !bodyStartsAtHeaderEnd();
	if(this.foundOpeningBrace) {
		this.bracketBalance++;
	}
}
public RecoveredElement add(AbstractMethodDeclaration methodDeclaration, int bracketBalance) {

	/* do not consider a method starting passed the type end (if set)
		it must be belonging to an enclosing type */
	if (typeDeclaration.declarationSourceEnd != 0 
		&& methodDeclaration.declarationSourceStart > typeDeclaration.declarationSourceEnd){
		return this.parent.add(methodDeclaration, bracketBalance);
	}

	if (methods == null) {
		methods = new RecoveredMethod[5];
		methodCount = 0;
	} else {
		if (methodCount == methods.length) {
			System.arraycopy(
				methods, 
				0, 
				(methods = new RecoveredMethod[2 * methodCount]), 
				0, 
				methodCount); 
		}
	}
	RecoveredMethod element = new RecoveredMethod(methodDeclaration, this, bracketBalance);
	methods[methodCount++] = element;

	/* consider that if the opening brace was not found, it is there */
	if (!foundOpeningBrace){
		foundOpeningBrace = true;
		this.bracketBalance++;
	}
	/* if method not finished, then method becomes current */
	if (methodDeclaration.declarationSourceEnd == 0) return element;
	return this;
}
public RecoveredElement add(FieldDeclaration fieldDeclaration, int bracketBalance) {
	
	/* do not consider a field starting passed the type end (if set)
	it must be belonging to an enclosing type */
	if (typeDeclaration.declarationSourceEnd != 0
		&& fieldDeclaration.declarationSourceStart > typeDeclaration.declarationSourceEnd) {
		return this.parent.add(fieldDeclaration, bracketBalance);
	}
	if (fields == null) {
		fields = new RecoveredField[5];
		fieldCount = 0;
	} else {
		if (fieldCount == fields.length) {
			System.arraycopy(
				fields, 
				0, 
				(fields = new RecoveredField[2 * fieldCount]), 
				0, 
				fieldCount); 
		}
	}
	RecoveredField element = fieldDeclaration.isField() 
								? new RecoveredField(fieldDeclaration, this, bracketBalance)
								: new RecoveredInitializer(fieldDeclaration, this, bracketBalance);
	fields[fieldCount++] = element;

	/* consider that if the opening brace was not found, it is there */
	if (!foundOpeningBrace){
		foundOpeningBrace = true;
		this.bracketBalance++;
	}
	/* if field not finished, then field becomes current */
	if (fieldDeclaration.declarationSourceEnd == 0) return element;
	return this;
}
public RecoveredElement add(TypeDeclaration memberTypeDeclaration, int bracketBalance) {

	/* do not consider a type starting passed the type end (if set)
		it must be belonging to an enclosing type */
	if (typeDeclaration.declarationSourceEnd != 0 
		&& memberTypeDeclaration.declarationSourceStart > typeDeclaration.declarationSourceEnd){
		return this.parent.add(memberTypeDeclaration, bracketBalance);
	}
	
	if (memberTypes == null) {
		memberTypes = new RecoveredType[5];
		memberTypeCount = 0;
	} else {
		if (memberTypeCount == memberTypes.length) {
			System.arraycopy(
				memberTypes, 
				0, 
				(memberTypes = new RecoveredType[2 * memberTypeCount]), 
				0, 
				memberTypeCount); 
		}
	}
	RecoveredType element = new RecoveredType(memberTypeDeclaration, this, bracketBalance);
	memberTypes[memberTypeCount++] = element;

	/* consider that if the opening brace was not found, it is there */
	if (!foundOpeningBrace){
		foundOpeningBrace = true;
		this.bracketBalance++;
	}
	/* if member type not finished, then member type becomes current */
	if (memberTypeDeclaration.declarationSourceEnd == 0) return element;
	return this;
}
/*
 * Answer the body end of the corresponding parse node
 */
public int bodyEnd(){
	if (bodyEnd == 0) return typeDeclaration.declarationSourceEnd;
	return bodyEnd;
}
public boolean bodyStartsAtHeaderEnd(){
	if (typeDeclaration.superInterfaces == null){
		if (typeDeclaration.superclass == null){
			return typeDeclaration.bodyStart == typeDeclaration.sourceEnd+1;
		} else {
			return typeDeclaration.bodyStart == typeDeclaration.superclass.sourceEnd+1;
		}
	} else {
		return typeDeclaration.bodyStart 
				== typeDeclaration.superInterfaces[typeDeclaration.superInterfaces.length-1].sourceEnd+1;
	}
}
/*
 * Answer the enclosing type node, or null if none
 */
public RecoveredType enclosingType(){
	RecoveredElement current = parent;
	while (current != null){
		if (current instanceof RecoveredType){
			return (RecoveredType) current;
		}
		current = current.parent;
	}
	return null;
}
public char[] name(){
	return typeDeclaration.name;
}
/* 
 * Answer the associated parsed structure
 */
public AstNode parseTree(){
	return typeDeclaration;
}
/*
 * Answer the very source end of the corresponding parse node
 */
public int sourceEnd(){
	return this.typeDeclaration.declarationSourceEnd;
}
public String toString(int tab) {
	StringBuffer result = new StringBuffer(tabString(tab));
	result.append("Recovered type:\n"); //$NON-NLS-1$
	if (typeDeclaration instanceof AnonymousLocalTypeDeclaration) {
		result.append(tabString(tab));
		result.append(" "); //$NON-NLS-1$
	}
	result.append(typeDeclaration.toString(tab + 1));
	if (this.memberTypes != null) {
		for (int i = 0; i < this.memberTypeCount; i++) {
			result.append("\n"); //$NON-NLS-1$
			result.append(this.memberTypes[i].toString(tab + 1));
		}
	}
	if (this.fields != null) {
		for (int i = 0; i < this.fieldCount; i++) {
			result.append("\n"); //$NON-NLS-1$
			result.append(this.fields[i].toString(tab + 1));
		}
	}
	if (this.methods != null) {
		for (int i = 0; i < this.methodCount; i++) {
			result.append("\n"); //$NON-NLS-1$
			result.append(this.methods[i].toString(tab + 1));
		}
	}
	return result.toString();
}
/*
 * Update the bodyStart of the corresponding parse node
 */
public void updateBodyStart(int bodyStart){
	this.foundOpeningBrace = true;
	this.typeDeclaration.bodyStart = bodyStart;
}
public Statement updatedStatement(){

	// ignore closed anonymous type
	if (typeDeclaration instanceof AnonymousLocalTypeDeclaration
		&& !this.preserveContent){
		return null;
	}
		
	TypeDeclaration updatedType = this.updatedTypeDeclaration();
	if (updatedType instanceof AnonymousLocalTypeDeclaration){
		/* in presence of an anonymous type, we want the full allocation expression */
		return ((AnonymousLocalTypeDeclaration)updatedType).allocation;
	}
	return updatedType;
}
public TypeDeclaration updatedTypeDeclaration(){

	/* update member types */
	if (memberTypeCount > 0){
		int existingCount = typeDeclaration.memberTypes == null ? 0 : typeDeclaration.memberTypes.length;
		MemberTypeDeclaration[] memberTypeDeclarations = new MemberTypeDeclaration[existingCount + memberTypeCount];
		if (existingCount > 0){
			System.arraycopy(typeDeclaration.memberTypes, 0, memberTypeDeclarations, 0, existingCount);
		}
		// may need to update the declarationSourceEnd of the last type
		if (memberTypes[memberTypeCount - 1].typeDeclaration.declarationSourceEnd == 0){
			memberTypes[memberTypeCount - 1].typeDeclaration.declarationSourceEnd = bodyEnd();
		}
		for (int i = 0; i < memberTypeCount; i++){
			memberTypeDeclarations[existingCount + i] = (MemberTypeDeclaration)memberTypes[i].updatedTypeDeclaration();
		}
		typeDeclaration.memberTypes = memberTypeDeclarations;
	}
	/* update fields */
	if (fieldCount > 0){
		int existingCount = typeDeclaration.fields == null ? 0 : typeDeclaration.fields.length;
		FieldDeclaration[] fieldDeclarations = new FieldDeclaration[existingCount + fieldCount];
		if (existingCount > 0){
			System.arraycopy(typeDeclaration.fields, 0, fieldDeclarations, 0, existingCount);
		}
		// may need to update the declarationSourceEnd of the last field
		if (fields[fieldCount - 1].fieldDeclaration.declarationSourceEnd == 0){
			fields[fieldCount - 1].fieldDeclaration.declarationSourceEnd = bodyEnd();
		}
		for (int i = 0; i < fieldCount; i++){
			fieldDeclarations[existingCount + i] = fields[i].updatedFieldDeclaration();
		}
		typeDeclaration.fields = fieldDeclarations;
	}
	/* update methods */
	int existingCount = typeDeclaration.methods == null ? 0 : typeDeclaration.methods.length;
	boolean hasConstructor = false, hasRecoveredConstructor = false;
	int defaultConstructorIndex = -1;
	if (methodCount > 0){
		AbstractMethodDeclaration[] methodDeclarations = new AbstractMethodDeclaration[existingCount + methodCount];
		for (int i = 0; i < existingCount; i++){
			AbstractMethodDeclaration m = typeDeclaration.methods[i];
			if (m.isDefaultConstructor()) defaultConstructorIndex = i;
			methodDeclarations[i] = m;
		}
		// may need to update the declarationSourceEnd of the last method
		if (methods[methodCount - 1].methodDeclaration.declarationSourceEnd == 0){
			methods[methodCount - 1].methodDeclaration.declarationSourceEnd = bodyEnd();
		}
		for (int i = 0; i < methodCount; i++){
			AbstractMethodDeclaration updatedMethod = methods[i].updatedMethodDeclaration();			
			if (updatedMethod.isConstructor()) hasRecoveredConstructor = true;
			methodDeclarations[existingCount + i] = updatedMethod;			
		}
		typeDeclaration.methods = methodDeclarations;
		hasConstructor = typeDeclaration.checkConstructors(this.parser());
	} else {
		for (int i = 0; i < existingCount; i++){
			if (typeDeclaration.methods[i].isConstructor()) hasConstructor = true;
		}		
	}
	/* add clinit ? */
	if (typeDeclaration.needClassInitMethod()){
		boolean alreadyHasClinit = false;
		for (int i = 0; i < existingCount; i++){
			if (typeDeclaration.methods[i].isClinit()){
				alreadyHasClinit = true;
				break;
			}
		}
		if (!alreadyHasClinit) typeDeclaration.addClinit();
	}
	/* add default constructor ? */
	if (defaultConstructorIndex >= 0 && hasRecoveredConstructor){
		/* should discard previous default construtor */
		AbstractMethodDeclaration[] methodDeclarations = new AbstractMethodDeclaration[typeDeclaration.methods.length - 1];
		if (defaultConstructorIndex != 0){
			System.arraycopy(typeDeclaration.methods, 0, methodDeclarations, 0, defaultConstructorIndex);
		}
		if (defaultConstructorIndex != typeDeclaration.methods.length-1){
			System.arraycopy(
				typeDeclaration.methods, 
				defaultConstructorIndex+1, 
				methodDeclarations, 
				defaultConstructorIndex, 
				typeDeclaration.methods.length - defaultConstructorIndex - 1);
		}
		typeDeclaration.methods = methodDeclarations;
	} else {
		if (!hasConstructor) {// if was already reduced, then constructor
			typeDeclaration.createsInternalConstructor(true, true);
		} 
	}
	/* might need to cast itself into a MemberTypeDeclaration or a LocalTypeDeclaration */
	TypeDeclaration newTypeDeclaration = null;
	if ((typeDeclaration instanceof TypeDeclaration) && (parent instanceof RecoveredType)){
		newTypeDeclaration = new MemberTypeDeclaration();
	} else {
		if ((typeDeclaration instanceof TypeDeclaration) && (parent instanceof RecoveredMethod)){
			newTypeDeclaration = new LocalTypeDeclaration();
		}
	}
	/* copy slots into new type */
	if (newTypeDeclaration != null){
		newTypeDeclaration.modifiers = typeDeclaration.modifiers;
		newTypeDeclaration.modifiersSourceStart = typeDeclaration.modifiersSourceStart;
		newTypeDeclaration.name = typeDeclaration.name;
		newTypeDeclaration.superclass = typeDeclaration.superclass;
		newTypeDeclaration.superInterfaces = typeDeclaration.superInterfaces;
		newTypeDeclaration.fields = typeDeclaration.fields;
		newTypeDeclaration.methods = typeDeclaration.methods;
		newTypeDeclaration.memberTypes = typeDeclaration.memberTypes;
		newTypeDeclaration.ignoreFurtherInvestigation = typeDeclaration.ignoreFurtherInvestigation;
		newTypeDeclaration.maxFieldCount = typeDeclaration.maxFieldCount;
		newTypeDeclaration.declarationSourceStart = typeDeclaration.declarationSourceStart;
		newTypeDeclaration.declarationSourceEnd = typeDeclaration.declarationSourceEnd;
		newTypeDeclaration.bodyStart = typeDeclaration.bodyStart;
		typeDeclaration = newTypeDeclaration;
	}
	return typeDeclaration;
}
/*
 * Update the corresponding parse node from parser state which
 * is about to disappear because of restarting recovery
 */
public void updateFromParserState(){

	if(this.bodyStartsAtHeaderEnd()){
		Parser parser = this.parser();
		/* might want to recover implemented interfaces */
		if (parser.listLength > 0){ // awaiting interface type references
			parser.consumeClassHeaderImplements(); 
			// will reset typeListLength to zero
			// thus this check will only be performed on first errorCheck after class X implements Y,Z,
		}
	}
}
/*
 * A closing brace got consumed, might have closed the current element,
 * in which case both the currentElement is exited
 */
public RecoveredElement updateOnClosingBrace(int braceStart, int braceEnd){
	if ((--bracketBalance <= 0) && (parent != null)){
		this.updateSourceEndIfNecessary(braceEnd);
		this.bodyEnd = braceStart - 1;
		return parent;
	}
	return this;
}
/*
 * An opening brace got consumed, might be the expected opening one of the current element,
 * in which case the bodyStart is updated.
 */
public RecoveredElement updateOnOpeningBrace(int braceEnd){
	/* in case the opening brace is not close enough to the signature, ignore it */
	if (bracketBalance == 0){
		/*
			if (parser.scanner.searchLineNumber(typeDeclaration.sourceEnd) 
				!= parser.scanner.searchLineNumber(braceEnd)){
		 */
		Parser parser = this.parser();
		switch(parser.lastIgnoredToken){
			case -1 :
			case TokenNameextends :
			case TokenNameimplements :
				if (parser.recoveredStaticInitializerStart == 0) break;
			default:
				this.foundOpeningBrace = true;				
				bracketBalance = 1; // pretend the brace was already there
		}
	}	
	// might be an initializer
	if (this.bracketBalance == 1){
		Block block = new Block(0);
		Parser parser = this.parser();
		block.sourceStart = parser.scanner.startPosition;
		Initializer init;
		if (parser.recoveredStaticInitializerStart == 0){
			init = new Initializer(block, AccDefault);
		} else {
			init = new Initializer(block, AccStatic);
			init.declarationSourceStart = parser.recoveredStaticInitializerStart;
		}
		return this.add(init, 1);
	}
	return super.updateOnOpeningBrace(braceEnd);
}
public void updateParseTree(){
	this.updatedTypeDeclaration();
}
/*
 * Update the declarationSourceEnd of the corresponding parse node
 */
public void updateSourceEndIfNecessary(int sourceEnd){
	if (this.typeDeclaration.declarationSourceEnd == 0){
		this.bodyEnd = 0;
		this.typeDeclaration.declarationSourceEnd = sourceEnd;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8011.java