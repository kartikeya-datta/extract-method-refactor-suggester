error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4559.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4559.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4559.java
text:
```scala
i@@f (this.binding == Expression.getDirectBinding(this.initialization)) {

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;
import org.eclipse.jdt.internal.compiler.util.Util;

public class FieldDeclaration extends AbstractVariableDeclaration {
	
	public FieldBinding binding;
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

public FieldDeclaration(	char[] name, int sourceStart, int sourceEnd) {
	this.name = name;
	//due to some declaration like 
	// int x, y = 3, z , x ;
	//the sourceStart and the sourceEnd is ONLY on  the name
	this.sourceStart = sourceStart;
	this.sourceEnd = sourceEnd;
}

public FlowInfo analyseCode(MethodScope initializationScope, FlowContext flowContext, FlowInfo flowInfo) {
	if (this.binding != null && !this.binding.isUsed()) {
		if (this.binding.isPrivate() || (this.binding.declaringClass != null && this.binding.declaringClass.isLocalType())) {
			if (!initializationScope.referenceCompilationUnit().compilationResult.hasSyntaxError) {
				initializationScope.problemReporter().unusedPrivateField(this);
			}
		}
	}
	// cannot define static non-constant field inside nested class
	if (this.binding != null
			&& this.binding.isValidBinding()
			&& this.binding.isStatic()
			&& this.binding.constant() == Constant.NotAConstant
			&& this.binding.declaringClass.isNestedType()
			&& !this.binding.declaringClass.isStatic()) {
		initializationScope.problemReporter().unexpectedStaticModifierForField(
			(SourceTypeBinding) this.binding.declaringClass,
			this);
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
	if ((this.bits & IsReachable) == 0) {
		return;
	}
	// do not generate initialization code if final and static (constant is then
	// recorded inside the field itself).
	int pc = codeStream.position;
	boolean isStatic;
	if (this.initialization != null
		&& !((isStatic = this.binding.isStatic()) && this.binding.constant() != Constant.NotAConstant)) {
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
	return (this.modifiers & ClassFileConstants.AccStatic) != 0;
}

public StringBuffer printStatement(int indent, StringBuffer output) {
	if (this.javadoc != null) {
		this.javadoc.print(indent, output);
	}
	return super.printStatement(indent, output);
}

public void resolve(MethodScope initializationScope) {
	// the two <constant = Constant.NotAConstant> could be regrouped into
	// a single line but it is clearer to have two lines while the reason of their
	// existence is not at all the same. See comment for the second one.

	//--------------------------------------------------------
	if ((this.bits & ASTNode.HasBeenResolved) != 0) return;
	if (this.binding == null || !this.binding.isValidBinding()) return;
	
	this.bits |= ASTNode.HasBeenResolved;

	// check if field is hiding some variable - issue is that field binding already got inserted in scope
	// thus must lookup separately in super type and outer context
	ClassScope classScope = initializationScope.enclosingClassScope();
	
	if (classScope != null) {
		checkHiding: {
			SourceTypeBinding declaringType = classScope.enclosingSourceType();
			checkHidingSuperField: {
				if (declaringType.superclass == null) break checkHidingSuperField;
				Binding existingVariable = classScope.findField(declaringType.superclass, this.name, this,  false /*do not resolve hidden field*/);
				if (existingVariable == null) break checkHidingSuperField; // keep checking outer scenario
				if (!existingVariable.isValidBinding())  break checkHidingSuperField; // keep checking outer scenario
				if (existingVariable instanceof FieldBinding) {
					FieldBinding existingField = (FieldBinding) existingVariable;
					if (existingField.original() == this.binding) break checkHidingSuperField; // keep checking outer scenario
					if (!existingField.canBeSeenBy(declaringType, this, initializationScope)) break checkHidingSuperField; // keep checking outer scenario
				}
				// collision with supertype field
				initializationScope.problemReporter().fieldHiding(this, existingVariable);
				break checkHiding; // already found a matching field
			}					
			// only corner case is: lookup of outer field through static declaringType, which isn't detected by #getBinding as lookup starts
			// from outer scope. Subsequent static contexts are detected for free.
			Scope outerScope = classScope.parent;
			if (outerScope.kind == Scope.COMPILATION_UNIT_SCOPE) break checkHiding;
			Binding existingVariable = outerScope.getBinding(this.name, Binding.VARIABLE, this, false /*do not resolve hidden field*/);
			if (existingVariable == null) break checkHiding;
			if (!existingVariable.isValidBinding()) break checkHiding;
			if (existingVariable == this.binding) break checkHiding;
			if (existingVariable instanceof FieldBinding) {
				FieldBinding existingField = (FieldBinding) existingVariable;
				if (existingField.original() == this.binding) break checkHiding;
				if (!existingField.isStatic() && declaringType.isStatic()) break checkHiding;
			}
			// collision with outer field or local variable
			initializationScope.problemReporter().fieldHiding(this, existingVariable);
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

		resolveAnnotations(initializationScope, this.annotations, this.binding);
		// check @Deprecated annotation presence
		if ((this.binding.getAnnotationTagBits() & TagBits.AnnotationDeprecated) == 0
				&& (this.binding.modifiers & ClassFileConstants.AccDeprecated) != 0
				&& initializationScope.compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5) {
			initializationScope.problemReporter().missingDeprecatedAnnotationForField(this);
		}						
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
						    initializationScope.problemReporter().unsafeTypeConversion(this.initialization, initializationType, fieldType);
					}
					if (this.initialization instanceof CastExpression 
							&& (this.initialization.bits & ASTNode.UnnecessaryCast) == 0) {
						CastExpression.checkNeedForAssignedCast(initializationScope, fieldType, (CastExpression) this.initialization);
					}								
				} else if (initializationScope.isBoxingCompatibleWith(initializationType, fieldType) 
 (initializationType.isBaseType()  // narrowing then boxing ?
											&& initializationScope.compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5 // autoboxing
											&& !fieldType.isBaseType()
											&& initialization.isConstantValueOfTypeAssignableToType(initializationType, initializationScope.environment().computeBoxingType(fieldType)))) {
					this.initialization.computeConversion(initializationScope, fieldType, initializationType);
					if (this.initialization instanceof CastExpression 
							&& (this.initialization.bits & ASTNode.UnnecessaryCast) == 0) {
						CastExpression.checkNeedForAssignedCast(initializationScope, fieldType, (CastExpression) this.initialization);
					}							
				} else {
					if ((fieldType.tagBits & TagBits.HasMissingType) == 0) {
						// if problem already got signaled on type, do not report secondary problem
						initializationScope.problemReporter().typeMismatchError(initializationType, fieldType, this.initialization, null);
					}		
				}
				if (this.binding.isFinal()){ // cast from constant actual type to variable type
					this.binding.setConstant(this.initialization.constant.castTo((this.binding.type.id << 4) + this.initialization.constant.typeID()));
				}
			} else {
				this.binding.setConstant(Constant.NotAConstant);
			}
			// check for assignment with no effect
			if (this.binding == Assignment.getDirectBinding(this.initialization)) {
				initializationScope.problemReporter().assignmentHasNoEffect(this, this.name);
			}					
		}
		// Resolve Javadoc comment if one is present
		if (this.javadoc != null) {
			this.javadoc.resolve(initializationScope);
		} else if (this.binding != null && this.binding.declaringClass != null && !this.binding.declaringClass.isLocalType()) {
			// Set javadoc visibility
			int javadocVisibility = this.binding.modifiers & ExtraCompilerModifiers.AccVisibilityMASK;
			ProblemReporter reporter = initializationScope.problemReporter();
			int severity = reporter.computeSeverity(IProblem.JavadocMissing);
			if (severity != ProblemSeverities.Ignore) {
				if (classScope != null) {
					javadocVisibility = Util.computeOuterMostVisibility(classScope.referenceType(), javadocVisibility);
				}
				int javadocModifiers = (this.binding.modifiers & ~ExtraCompilerModifiers.AccVisibilityMASK) | javadocVisibility;
				reporter.javadocMissing(this.sourceStart, this.sourceEnd, severity, javadocModifiers);
			}			
		}
	} finally {
		initializationScope.initializedField = previousField;
		initializationScope.lastVisibleFieldID = previousFieldID;
		if (this.binding.constant() == null)
			this.binding.setConstant(Constant.NotAConstant);
	}
}

public void traverse(ASTVisitor visitor, MethodScope scope) {
	if (visitor.visit(this, scope)) {
		if (this.javadoc != null) {
			this.javadoc.traverse(visitor, scope);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4559.java