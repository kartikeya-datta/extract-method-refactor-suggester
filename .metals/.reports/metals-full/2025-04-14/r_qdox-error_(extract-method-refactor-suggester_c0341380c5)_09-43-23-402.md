error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/378.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/378.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/378.java
text:
```scala
i@@f (receiverErasure.findSuperTypeOriginatingFrom(fieldBinding.declaringClass) == null) {

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class FieldReference extends Reference implements InvocationSite {

	public static final int READ = 0;
	public static final int WRITE = 1;
	public Expression receiver;
	public char[] token;
	public FieldBinding binding;															// exact binding resulting from lookup
	protected FieldBinding codegenBinding;									// actual binding used for code generation (if no synthetic accessor)
	public MethodBinding[] syntheticAccessors; // [0]=read accessor [1]=write accessor
	
	public long nameSourcePosition; //(start<<32)+end
	public TypeBinding receiverType;
	public TypeBinding genericCast;
	
public FieldReference(char[] source, long pos) {
	token = source;
	nameSourcePosition = pos;
	//by default the position are the one of the field (not true for super access)
	sourceStart = (int) (pos >>> 32);
	sourceEnd = (int) (pos & 0x00000000FFFFFFFFL);
	bits |= Binding.FIELD;

}

public FlowInfo analyseAssignment(BlockScope currentScope, 	FlowContext flowContext, 	FlowInfo flowInfo, Assignment assignment, boolean isCompound) {
	// compound assignment extra work
	if (isCompound) { // check the variable part is initialized if blank final
		if (binding.isBlankFinal()
			&& receiver.isThis()
			&& currentScope.needBlankFinalFieldInitializationCheck(binding)
			&& (!flowInfo.isDefinitelyAssigned(binding))) {
			currentScope.problemReporter().uninitializedBlankFinalField(binding, this);
			// we could improve error msg here telling "cannot use compound assignment on final blank field"
		}
		manageSyntheticAccessIfNecessary(currentScope, flowInfo, true /*read-access*/);
	}
	flowInfo =
		receiver
			.analyseCode(currentScope, flowContext, flowInfo, !binding.isStatic())
			.unconditionalInits();
	if (assignment.expression != null) {
		flowInfo =
			assignment
				.expression
				.analyseCode(currentScope, flowContext, flowInfo)
				.unconditionalInits();
	}
	manageSyntheticAccessIfNecessary(currentScope, flowInfo, false /*write-access*/);

	// check if assigning a final field 
	if (binding.isFinal()) {
		// in a context where it can be assigned?
		if (binding.isBlankFinal()
			&& !isCompound
			&& receiver.isThis()
			&& !(receiver instanceof QualifiedThisReference)
			&& ((receiver.bits & ParenthesizedMASK) == 0) // (this).x is forbidden
			&& currentScope.allowBlankFinalFieldAssignment(binding)) {
			if (flowInfo.isPotentiallyAssigned(binding)) {
				currentScope.problemReporter().duplicateInitializationOfBlankFinalField(
					binding,
					this);
			} else {
				flowContext.recordSettingFinal(binding, this, flowInfo);
			}
			flowInfo.markAsDefinitelyAssigned(binding);
		} else {
			// assigning a final field outside an initializer or constructor or wrong reference
			currentScope.problemReporter().cannotAssignToFinalField(binding, this);
		}
	}
	return flowInfo;
}

public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
	return analyseCode(currentScope, flowContext, flowInfo, true);
}

public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo, boolean valueRequired) {
	boolean nonStatic = !binding.isStatic();
	receiver.analyseCode(currentScope, flowContext, flowInfo, nonStatic);
	if (nonStatic) {
		receiver.checkNPE(currentScope, flowContext, flowInfo);
	}
	
	if (valueRequired || currentScope.compilerOptions().complianceLevel >= ClassFileConstants.JDK1_4) {
		manageSyntheticAccessIfNecessary(currentScope, flowInfo, true /*read-access*/);
	}
	return flowInfo;
}

/**
 * @see org.eclipse.jdt.internal.compiler.ast.Expression#computeConversion(org.eclipse.jdt.internal.compiler.lookup.Scope, org.eclipse.jdt.internal.compiler.lookup.TypeBinding, org.eclipse.jdt.internal.compiler.lookup.TypeBinding)
 */
public void computeConversion(Scope scope, TypeBinding runtimeTimeType, TypeBinding compileTimeType) {
	if (runtimeTimeType == null || compileTimeType == null)
		return;		
	// set the generic cast after the fact, once the type expectation is fully known (no need for strict cast)
	if (this.binding != null && this.binding.isValidBinding()) {
		FieldBinding originalBinding = this.binding.original();
		TypeBinding originalType = originalBinding.type;
	    // extra cast needed if method return type is type variable
		if (originalBinding != this.binding 
				&& originalType != this.binding.type
				&& runtimeTimeType.id != T_JavaLangObject
				&& (originalType.tagBits & TagBits.HasTypeVariable) != 0) {
	    	TypeBinding targetType = (!compileTimeType.isBaseType() && runtimeTimeType.isBaseType()) 
	    		? compileTimeType  // unboxing: checkcast before conversion
	    		: runtimeTimeType;
	        this.genericCast = originalBinding.type.genericCast(targetType);
		}
	} 	
	super.computeConversion(scope, runtimeTimeType, compileTimeType);
}

public FieldBinding fieldBinding() {
	return binding;
}

public void generateAssignment(BlockScope currentScope, CodeStream codeStream, Assignment assignment, boolean valueRequired) {
	int pc = codeStream.position;
	receiver.generateCode(
		currentScope,
		codeStream,
		!this.codegenBinding.isStatic());
	codeStream.recordPositionsFrom(pc, this.sourceStart);
	assignment.expression.generateCode(currentScope, codeStream, true);
	fieldStore(
		codeStream,
		this.codegenBinding,
		syntheticAccessors == null ? null : syntheticAccessors[WRITE],
		valueRequired);
	if (valueRequired) {
		codeStream.generateImplicitConversion(assignment.implicitConversion);
	}
	// no need for generic cast as value got dupped
}

/**
 * Field reference code generation
 *
 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
 * @param valueRequired boolean
 */
public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
	int pc = codeStream.position;
	if (constant != Constant.NotAConstant) {
		if (valueRequired) {
			codeStream.generateConstant(constant, implicitConversion);
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
		return;
	}
	boolean isStatic = this.codegenBinding.isStatic();
	boolean isThisReceiver = this.receiver instanceof ThisReference;
	Constant fieldConstant = this.codegenBinding.constant();
	if (fieldConstant != Constant.NotAConstant) {
		if (!isThisReceiver) {
			receiver.generateCode(currentScope, codeStream, !isStatic);
			if (!isStatic){
				codeStream.invokeObjectGetClass();
				codeStream.pop();
			}
		}
		if (valueRequired) {
			codeStream.generateConstant(fieldConstant, implicitConversion);
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
		return;
	}
	if (valueRequired 
 (!isThisReceiver && currentScope.compilerOptions().complianceLevel >= ClassFileConstants.JDK1_4)
 ((implicitConversion & TypeIds.UNBOXING) != 0)
 (this.genericCast != null)) {
		receiver.generateCode(currentScope, codeStream, !isStatic);
		pc = codeStream.position;
		if (this.codegenBinding.declaringClass == null) { // array length
			codeStream.arraylength();
			if (valueRequired) {
				codeStream.generateImplicitConversion(implicitConversion);
			} else {
				// could occur if !valueRequired but compliance >= 1.4
				codeStream.pop();
			}
		} else {
			if (syntheticAccessors == null || syntheticAccessors[READ] == null) {
				if (isStatic) {
					codeStream.getstatic(this.codegenBinding);
				} else {
					codeStream.getfield(this.codegenBinding);
				}
			} else {
				codeStream.invokestatic(syntheticAccessors[READ]);
			}
			// required cast must occur even if no value is required
			if (this.genericCast != null) codeStream.checkcast(this.genericCast);
			if (valueRequired) {
				codeStream.generateImplicitConversion(implicitConversion);
			} else {
				boolean isUnboxing = (implicitConversion & TypeIds.UNBOXING) != 0;
				// conversion only generated if unboxing
				if (isUnboxing) codeStream.generateImplicitConversion(implicitConversion);
				switch (isUnboxing ? postConversionType(currentScope).id : this.codegenBinding.type.id) {
					case T_long :
					case T_double :
						codeStream.pop2();
						break;
					default :
						codeStream.pop();
				}
			}
		}
	} else {
		if (isThisReceiver) {
			if (isStatic){
				// if no valueRequired, still need possible side-effects of <clinit> invocation, if field belongs to different class
				if (this.binding.original().declaringClass != this.receiverType.erasure()) {
					MethodBinding accessor = syntheticAccessors == null ? null : syntheticAccessors[READ]; 
					if (accessor == null) {
						codeStream.getstatic(this.codegenBinding);
					} else {
						codeStream.invokestatic(accessor);
					}
					switch (this.codegenBinding.type.id) {
						case T_long :
						case T_double :
							codeStream.pop2();
							break;
						default :
							codeStream.pop();
					}
				}
			}
		} else {
			receiver.generateCode(currentScope, codeStream, !isStatic);				
			if (!isStatic){
				codeStream.invokeObjectGetClass(); // perform null check
				codeStream.pop();
			}
		}						
	}
	codeStream.recordPositionsFrom(pc, this.sourceEnd);
}

public void generateCompoundAssignment(BlockScope currentScope, CodeStream codeStream, Expression expression, int operator, int assignmentImplicitConversion, boolean valueRequired) {
	boolean isStatic;
	receiver.generateCode(
		currentScope,
		codeStream,
		!(isStatic = this.codegenBinding.isStatic()));
	if (isStatic) {
		if (syntheticAccessors == null || syntheticAccessors[READ] == null) {
			codeStream.getstatic(this.codegenBinding);
		} else {
			codeStream.invokestatic(syntheticAccessors[READ]);
		}
	} else {
		codeStream.dup();
		if (syntheticAccessors == null || syntheticAccessors[READ] == null) {
			codeStream.getfield(this.codegenBinding);
		} else {
			codeStream.invokestatic(syntheticAccessors[READ]);
		}
	}
	int operationTypeID;
	switch(operationTypeID = (implicitConversion & IMPLICIT_CONVERSION_MASK) >> 4) {
		case T_JavaLangString :
		case T_JavaLangObject :
		case T_undefined :
			codeStream.generateStringConcatenationAppend(currentScope, null, expression);
			break;
		default :
			if (this.genericCast != null)
				codeStream.checkcast(this.genericCast);				
			// promote the array reference to the suitable operation type
			codeStream.generateImplicitConversion(implicitConversion);
			// generate the increment value (will by itself  be promoted to the operation value)
			if (expression == IntLiteral.One) { // prefix operation
				codeStream.generateConstant(expression.constant, implicitConversion);
			} else {
				expression.generateCode(currentScope, codeStream, true);
			}
			// perform the operation
			codeStream.sendOperator(operator, operationTypeID);
			// cast the value back to the array reference type
			codeStream.generateImplicitConversion(assignmentImplicitConversion);
	}
	fieldStore(
		codeStream,
		this.codegenBinding,
		syntheticAccessors == null ? null : syntheticAccessors[WRITE],
		valueRequired);
	// no need for generic cast as value got dupped
}

public void generatePostIncrement(BlockScope currentScope, CodeStream codeStream, CompoundAssignment postIncrement, boolean valueRequired) {
	boolean isStatic;
	receiver.generateCode(
		currentScope,
		codeStream,
		!(isStatic = this.codegenBinding.isStatic()));
	if (isStatic) {
		if (syntheticAccessors == null || syntheticAccessors[READ] == null) {
			codeStream.getstatic(this.codegenBinding);
		} else {
			codeStream.invokestatic(syntheticAccessors[READ]);
		}
	} else {
		codeStream.dup();
		if (syntheticAccessors == null || syntheticAccessors[READ] == null) {
			codeStream.getfield(this.codegenBinding);
		} else {
			codeStream.invokestatic(syntheticAccessors[READ]);
		}
	}
	if (valueRequired) {
		if (isStatic) {
			if ((this.codegenBinding.type == TypeBinding.LONG)
 (this.codegenBinding.type == TypeBinding.DOUBLE)) {
				codeStream.dup2();
			} else {
				codeStream.dup();
			}
		} else { // Stack:  [owner][old field value]  ---> [old field value][owner][old field value]
			if ((this.codegenBinding.type == TypeBinding.LONG)
 (this.codegenBinding.type == TypeBinding.DOUBLE)) {
				codeStream.dup2_x1();
			} else {
				codeStream.dup_x1();
			}
		}
	}
	if (this.genericCast != null)
		codeStream.checkcast(this.genericCast);
	codeStream.generateImplicitConversion(this.implicitConversion);		
	codeStream.generateConstant(
		postIncrement.expression.constant,
		this.implicitConversion);
	codeStream.sendOperator(postIncrement.operator, this.implicitConversion & COMPILE_TYPE_MASK);
	codeStream.generateImplicitConversion(
		postIncrement.preAssignImplicitConversion);
	fieldStore(codeStream, this.codegenBinding, syntheticAccessors == null ? null : syntheticAccessors[WRITE], false);
}	

/**
 * @see org.eclipse.jdt.internal.compiler.lookup.InvocationSite#genericTypeArguments()
 */
public TypeBinding[] genericTypeArguments() {
	return null;
}
public boolean isSuperAccess() {
	return receiver.isSuper();
}

public boolean isTypeAccess() {
	return receiver != null && receiver.isTypeReference();
}

/*
 * No need to emulate access to protected fields since not implicitly accessed
 */
public void manageSyntheticAccessIfNecessary(BlockScope currentScope, FlowInfo flowInfo, boolean isReadAccess) {
	if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) != 0)	return;

	// if field from parameterized type got found, use the original field at codegen time
	this.codegenBinding = this.binding.original();
	
	if (binding.isPrivate()) {
		if ((currentScope.enclosingSourceType() != this.codegenBinding.declaringClass) 
				&& binding.constant() == Constant.NotAConstant) {
			if (syntheticAccessors == null)
				syntheticAccessors = new MethodBinding[2];
			syntheticAccessors[isReadAccess ? READ : WRITE] = 
				((SourceTypeBinding) this.codegenBinding.declaringClass).addSyntheticMethod(this.codegenBinding, isReadAccess);
			currentScope.problemReporter().needToEmulateFieldAccess(this.codegenBinding, this, isReadAccess);
			return;
		}

	} else if (receiver instanceof QualifiedSuperReference) { // qualified super

		// qualified super need emulation always
		SourceTypeBinding destinationType =
			(SourceTypeBinding) (((QualifiedSuperReference) receiver)
				.currentCompatibleType);
		if (syntheticAccessors == null)
			syntheticAccessors = new MethodBinding[2];
		syntheticAccessors[isReadAccess ? READ : WRITE] = destinationType.addSyntheticMethod(this.codegenBinding, isReadAccess);
		currentScope.problemReporter().needToEmulateFieldAccess(this.codegenBinding, this, isReadAccess);
		return;

	} else if (binding.isProtected()) {

		SourceTypeBinding enclosingSourceType;
		if (((bits & DepthMASK) != 0)
			&& binding.declaringClass.getPackage()
				!= (enclosingSourceType = currentScope.enclosingSourceType()).getPackage()) {

			SourceTypeBinding currentCompatibleType =
				(SourceTypeBinding) enclosingSourceType.enclosingTypeAt(
					(bits & DepthMASK) >> DepthSHIFT);
			if (syntheticAccessors == null)
				syntheticAccessors = new MethodBinding[2];
			syntheticAccessors[isReadAccess ? READ : WRITE] = currentCompatibleType.addSyntheticMethod(this.codegenBinding, isReadAccess);
			currentScope.problemReporter().needToEmulateFieldAccess(this.codegenBinding, this, isReadAccess);
			return;
		}
	}
	// if the binding declaring class is not visible, need special action
	// for runtime compatibility on 1.2 VMs : change the declaring class of the binding
	// NOTE: from target 1.2 on, field's declaring class is touched if any different from receiver type
	// and not from Object or implicit static field access.	
	if (this.binding.declaringClass != this.receiverType
			&& !this.receiverType.isArrayType()
			&& this.binding.declaringClass != null // array.length
			&& this.binding.constant() == Constant.NotAConstant) {
		CompilerOptions options = currentScope.compilerOptions();
		if ((options.targetJDK >= ClassFileConstants.JDK1_2
				&& (options.complianceLevel >= ClassFileConstants.JDK1_4 || !(receiver.isImplicitThis() && this.codegenBinding.isStatic()))
				&& this.binding.declaringClass.id != T_JavaLangObject) // no change for Object fields
 !this.binding.declaringClass.canBeSeenBy(currentScope)) {

			this.codegenBinding =
				currentScope.enclosingSourceType().getUpdatedFieldBinding(
					this.codegenBinding,
					(ReferenceBinding) this.receiverType.erasure());
		}
	}		
}

public int nullStatus(FlowInfo flowInfo) {
	return FlowInfo.UNKNOWN;
}

public Constant optimizedBooleanConstant() {
	switch (this.resolvedType.id) {
		case T_boolean :
		case T_JavaLangBoolean :		
			return this.constant != Constant.NotAConstant ? this.constant : this.binding.constant();
		default :
			return Constant.NotAConstant;
	}
}

/**
 * @see org.eclipse.jdt.internal.compiler.ast.Expression#postConversionType(Scope)
 */
public TypeBinding postConversionType(Scope scope) {
	TypeBinding convertedType = this.resolvedType;
	if (this.genericCast != null) 
		convertedType = this.genericCast;
	int runtimeType = (this.implicitConversion & IMPLICIT_CONVERSION_MASK) >> 4;
	switch (runtimeType) {
		case T_boolean :
			convertedType = TypeBinding.BOOLEAN;
			break;
		case T_byte :
			convertedType = TypeBinding.BYTE;
			break;
		case T_short :
			convertedType = TypeBinding.SHORT;
			break;
		case T_char :
			convertedType = TypeBinding.CHAR;
			break;
		case T_int :
			convertedType = TypeBinding.INT;
			break;
		case T_float :
			convertedType = TypeBinding.FLOAT;
			break;
		case T_long :
			convertedType = TypeBinding.LONG;
			break;
		case T_double :
			convertedType = TypeBinding.DOUBLE;
			break;
		default :
	}		
	if ((this.implicitConversion & BOXING) != 0) {
		convertedType = scope.environment().computeBoxingType(convertedType);
	}
	return convertedType;
}

public StringBuffer printExpression(int indent, StringBuffer output) {
	return receiver.printExpression(0, output).append('.').append(token);
}

public TypeBinding resolveType(BlockScope scope) {
	// Answer the signature type of the field.
	// constants are propaged when the field is final
	// and initialized with a (compile time) constant 

	//always ignore receiver cast, since may affect constant pool reference
	boolean receiverCast = false;
	if (this.receiver instanceof CastExpression) {
		this.receiver.bits |= DisableUnnecessaryCastCheck; // will check later on
		receiverCast = true;
	}
	this.receiverType = receiver.resolveType(scope);
	if (this.receiverType == null) {
		constant = Constant.NotAConstant;
		return null;
	}
	if (receiverCast) {
		 // due to change of declaring class with receiver type, only identity cast should be notified
		if (((CastExpression)this.receiver).expression.resolvedType == this.receiverType) { 
				scope.problemReporter().unnecessaryCast((CastExpression)this.receiver);		
		}
	}		
	// the case receiverType.isArrayType and token = 'length' is handled by the scope API
	FieldBinding fieldBinding = this.codegenBinding = this.binding = scope.getField(this.receiverType, token, this);
	if (!fieldBinding.isValidBinding()) {
		constant = Constant.NotAConstant;
		scope.problemReporter().invalidField(this, this.receiverType);
		return null;
	}
	TypeBinding receiverErasure = this.receiverType.erasure();
	if (receiverErasure instanceof ReferenceBinding) {
		if (receiverErasure.findSuperTypeWithSameErasure(fieldBinding.declaringClass) == null) {
			this.receiverType = fieldBinding.declaringClass; // handle indirect inheritance thru variable secondary bound
		}
	}
	this.receiver.computeConversion(scope, this.receiverType, this.receiverType);
	if (isFieldUseDeprecated(fieldBinding, scope, (this.bits & IsStrictlyAssigned) !=0)) {
		scope.problemReporter().deprecatedField(fieldBinding, this);
	}
	boolean isImplicitThisRcv = receiver.isImplicitThis();
	constant = isImplicitThisRcv ? fieldBinding.constant() : Constant.NotAConstant;
	if (fieldBinding.isStatic()) {
		// static field accessed through receiver? legal but unoptimal (optional warning)
		if (!(isImplicitThisRcv
 (receiver instanceof NameReference 
					&& (((NameReference) receiver).bits & Binding.TYPE) != 0))) {
			scope.problemReporter().nonStaticAccessToStaticField(this, fieldBinding);
		}
		if (!isImplicitThisRcv
				&& fieldBinding.declaringClass != receiverType
				&& fieldBinding.declaringClass.canBeSeenBy(scope)) {
			scope.problemReporter().indirectAccessToStaticField(this, fieldBinding);
		}
	}
	// perform capture conversion if read access
	return this.resolvedType = 
		(((this.bits & IsStrictlyAssigned) == 0) 
			? fieldBinding.type.capture(scope, this.sourceEnd)
			: fieldBinding.type);
}

public void setActualReceiverType(ReferenceBinding receiverType) {
	// ignored
}

public void setDepth(int depth) {
	bits &= ~DepthMASK; // flush previous depth if any			
	if (depth > 0) {
		bits |= (depth & 0xFF) << DepthSHIFT; // encoded on 8 bits
	}
}

public void setFieldIndex(int index) {
	// ignored
}

public void traverse(ASTVisitor visitor, BlockScope scope) {
	if (visitor.visit(this, scope)) {
		receiver.traverse(visitor, scope);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/378.java