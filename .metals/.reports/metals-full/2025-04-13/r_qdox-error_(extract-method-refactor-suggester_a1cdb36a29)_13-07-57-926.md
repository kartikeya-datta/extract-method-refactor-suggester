error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/423.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/423.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/423.java
text:
```scala
i@@f (checkLocal && declaringType.enclosingType() != null) {

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
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class FieldDeclaration extends AbstractVariableDeclaration {
	
	public FieldBinding binding;
	boolean hasBeenResolved = false;
	public Javadoc javadoc;

	//allows to retrieve both the "type" part of the declaration (part1)
	//and also the part that decribe the name and the init and optionally
	//some other dimension ! .... 
	//public int[] a, b[] = X, c ;
	//for b that would give for 
	// - part1 : public int[]
	// - part2 : b[] = X,

	public int endPart1Position;
	public int endPart2Position;

	public FieldDeclaration() {
		// for subtypes or conversion
	}

	public FieldDeclaration(
		char[] name,
		int sourceStart,
		int sourceEnd) {

		this.name = name;

		//due to some declaration like 
		// int x, y = 3, z , x ;
		//the sourceStart and the sourceEnd is ONLY on  the name
		this.sourceStart = sourceStart;
		this.sourceEnd = sourceEnd;
	}

	public FlowInfo analyseCode(
		MethodScope initializationScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {

		if (this.binding != null && this.binding.isPrivate() && !this.binding.isPrivateUsed()) {
			if (!initializationScope.referenceCompilationUnit().compilationResult.hasSyntaxError()) {
				initializationScope.problemReporter().unusedPrivateField(this);
			}
		}
		// cannot define static non-constant field inside nested class
		if (this.binding != null
				&& this.binding.isValidBinding()
				&& this.binding.isStatic()
				&& !this.binding.isConstantValue()
				&& this.binding.declaringClass.isNestedType()
				&& this.binding.declaringClass.isClass()
				&& !this.binding.declaringClass.isStatic()) {
			initializationScope.problemReporter().unexpectedStaticModifierForField(
				(SourceTypeBinding) this.binding.declaringClass,
				this);
		}

		checkAnnotationField: {
			if (!this.binding.declaringClass.isAnnotationType())
				break checkAnnotationField;
			if (this.initialization != null) {
				if (this.binding.type.isArrayType() && (this.initialization instanceof ArrayInitializer))
					break checkAnnotationField;
				if (this.initialization.constant != NotAConstant)
					break checkAnnotationField;
			}
			initializationScope.problemReporter().annotationFieldNeedConstantInitialization(this);
		}
		if (this.initialization != null) {
			flowInfo =
				this.initialization
					.analyseCode(initializationScope, flowContext, flowInfo)
					.unconditionalInits();
			flowInfo.markAsDefinitelyAssigned(this.binding);
		}
		return flowInfo;
	}

	/**
	 * Code generation for a field declaration:
	 *	   standard assignment to a field 
	 *
	 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {

		if ((this.bits & IsReachableMASK) == 0) {
			return;
		}
		// do not generate initialization code if final and static (constant is then
		// recorded inside the field itself).
		int pc = codeStream.position;
		boolean isStatic;
		if (this.initialization != null
			&& !((isStatic = this.binding.isStatic()) && this.binding.isConstantValue())) {
			// non-static field, need receiver
			if (!isStatic)
				codeStream.aload_0();
			// generate initialization value
			this.initialization.generateCode(currentScope, codeStream, true);
			// store into field
			if (isStatic) {
				codeStream.putstatic(this.binding);
			} else {
				codeStream.putfield(this.binding);
			}
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration#getKind()
	 */
	public int getKind() {
		return this.type == null ? ENUM_CONSTANT : FIELD;
	}
	
	public boolean isStatic() {

		if (this.binding != null)
			return this.binding.isStatic();
		return (this.modifiers & AccStatic) != 0;
	}
	
	public void resolve(MethodScope initializationScope) {

		// the two <constant = Constant.NotAConstant> could be regrouped into
		// a single line but it is clearer to have two lines while the reason of their
		// existence is not at all the same. See comment for the second one.

		//--------------------------------------------------------
		if (!this.hasBeenResolved && this.binding != null && this.binding.isValidBinding()) {

			this.hasBeenResolved = true;

			resolveAnnotations(initializationScope, this.annotations, this.binding);

			// check if field is hiding some variable - issue is that field binding already got inserted in scope
			// thus must lookup separately in super type and outer context
			ClassScope classScope = initializationScope.enclosingClassScope();
			
			if (classScope != null) {
				SourceTypeBinding declaringType = classScope.enclosingSourceType();
				boolean checkLocal = true;
				if (declaringType.superclass != null) {
					Binding existingVariable = classScope.findField(declaringType.superclass, this.name, this,  false /*do not resolve hidden field*/);
					if (existingVariable != null && this.binding != existingVariable && existingVariable.isValidBinding()){
						initializationScope.problemReporter().fieldHiding(this, existingVariable);
						checkLocal = false; // already found a matching field
					}
				}
				if (checkLocal) {
					Scope outerScope = classScope.parent;
					// only corner case is: lookup of outer field through static declaringType, which isn't detected by #getBinding as lookup starts
					// from outer scope. Subsequent static contexts are detected for free.
					Binding existingVariable = outerScope.getBinding(this.name, Binding.VARIABLE, this, false /*do not resolve hidden field*/);
					if (existingVariable != null && this.binding != existingVariable && existingVariable.isValidBinding()
							&& (!(existingVariable instanceof FieldBinding)
 ((FieldBinding) existingVariable).isStatic() 
 !declaringType.isStatic())) {
						initializationScope.problemReporter().fieldHiding(this, existingVariable);
					}
				}
			}
			
			if (this.type != null ) { // enum constants have no declared type
				this.type.resolvedType = this.binding.type; // update binding for type reference
			}

			FieldBinding previousField = initializationScope.initializedField;
			int previousFieldID = initializationScope.lastVisibleFieldID;
			try {
				initializationScope.initializedField = this.binding;
				initializationScope.lastVisibleFieldID = this.binding.id;

				// the resolution of the initialization hasn't been done
				if (this.initialization == null) {
					this.binding.setConstant(Constant.NotAConstant);
				} else {
					// break dead-lock cycles by forcing constant to NotAConstant
					this.binding.setConstant(Constant.NotAConstant);
					
					TypeBinding fieldType = this.binding.type;
					TypeBinding initializationType;
					this.initialization.setExpectedType(fieldType); // needed in case of generic method invocation
					if (this.initialization instanceof ArrayInitializer) {

						if ((initializationType = this.initialization.resolveTypeExpecting(initializationScope, fieldType)) != null) {
							((ArrayInitializer) this.initialization).binding = (ArrayBinding) initializationType;
							this.initialization.computeConversion(initializationScope, fieldType, initializationType);
						}
					} else if ((initializationType = this.initialization.resolveType(initializationScope)) != null) {

						if (fieldType != initializationType) // must call before computeConversion() and typeMismatchError()
							initializationScope.compilationUnitScope().recordTypeConversion(fieldType, initializationType);
						if (this.initialization.isConstantValueOfTypeAssignableToType(initializationType, fieldType)
 (fieldType.isBaseType() && BaseTypeBinding.isWidening(fieldType.id, initializationType.id))
 initializationType.isCompatibleWith(fieldType)) {
							this.initialization.computeConversion(initializationScope, fieldType, initializationType);
							if (initializationType.needsUncheckedConversion(fieldType)) {
								    initializationScope.problemReporter().unsafeRawConversion(this.initialization, initializationType, fieldType);
							}									
						} else if (initializationScope.environment().options.sourceLevel >= JDK1_5 // autoboxing
										&& (initializationScope.isBoxingCompatibleWith(initializationType, fieldType) 
 (initializationType.isBaseType()  // narrowing then boxing ?
														&& initializationType != null 
														&& !fieldType.isBaseType()
														&& initialization.isConstantValueOfTypeAssignableToType(initializationType, initializationScope.environment().computeBoxingType(fieldType))))) {
							this.initialization.computeConversion(initializationScope, fieldType, initializationType);
						} else {
							initializationScope.problemReporter().typeMismatchError(initializationType, fieldType, this);
						}
						if (this.binding.isFinal()){ // cast from constant actual type to variable type
							this.binding.setConstant(this.initialization.constant.castTo((this.binding.type.id << 4) + this.initialization.constant.typeID()));
						}
					} else {
						this.binding.setConstant(NotAConstant);
					}
				}
				// Resolve Javadoc comment if one is present
				if (this.javadoc != null) {
					/*
					if (classScope != null) {
						this.javadoc.resolve(classScope);
					}
					*/
					this.javadoc.resolve(initializationScope);
				} else if (this.binding != null && this.binding.declaringClass != null && !this.binding.declaringClass.isLocalType()) {
					initializationScope.problemReporter().javadocMissing(this.sourceStart, this.sourceEnd, this.binding.modifiers);
				}
			} finally {
				initializationScope.initializedField = previousField;
				initializationScope.lastVisibleFieldID = previousFieldID;
				if (this.binding.constant() == null)
					this.binding.setConstant(Constant.NotAConstant);
			}
		}
	}

	public void traverse(ASTVisitor visitor, MethodScope scope) {

		if (visitor.visit(this, scope)) {
			if (this.annotations != null) {
				int annotationsLength = this.annotations.length;
				for (int i = 0; i < annotationsLength; i++)
					this.annotations[i].traverse(visitor, scope);
			}
			if (this.type != null) {
				this.type.traverse(visitor, scope);
			}
			if (this.initialization != null)
				this.initialization.traverse(visitor, scope);
		}
		visitor.endVisit(this, scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/423.java