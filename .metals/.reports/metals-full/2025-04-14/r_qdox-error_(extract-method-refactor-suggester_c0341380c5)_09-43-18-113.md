error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7717.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7717.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7717.java
text:
```scala
e@@lse /* avoid double diagnostic */ if ((localBinding.tagBits & TagBits.IsArgument) != 0) {

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;

public class SingleNameReference extends NameReference implements OperatorIds {
    
	public static final int READ = 0;
	public static final int WRITE = 1;
	public char[] token;
	public MethodBinding[] syntheticAccessors; // [0]=read accessor [1]=write accessor
	public TypeBinding genericCast;
	
	public SingleNameReference(char[] source, long pos) {
		super();
		token = source;
		sourceStart = (int) (pos >>> 32);
		sourceEnd = (int) pos;
	}
	public FlowInfo analyseAssignment(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo, Assignment assignment, boolean isCompound) {
	
		boolean isReachable = (flowInfo.tagBits & FlowInfo.UNREACHABLE) == 0;
		// compound assignment extra work
		if (isCompound) { // check the variable part is initialized if blank final
			switch (bits & RestrictiveFlagMASK) {
				case Binding.FIELD : // reading a field
					FieldBinding fieldBinding;
					if ((fieldBinding = (FieldBinding) binding).isBlankFinal() 
							&& currentScope.allowBlankFinalFieldAssignment(fieldBinding)) {
						if (!flowInfo.isDefinitelyAssigned(fieldBinding)) {
							currentScope.problemReporter().uninitializedBlankFinalField(fieldBinding, this);
						}
					}
					manageSyntheticAccessIfNecessary(currentScope, flowInfo, true /*read-access*/);
					break;
				case Binding.LOCAL : // reading a local variable
					// check if assigning a final blank field
					LocalVariableBinding localBinding;
					if (!flowInfo.isDefinitelyAssigned(localBinding = (LocalVariableBinding) binding)) {
						currentScope.problemReporter().uninitializedLocalVariable(localBinding, this);
						// we could improve error msg here telling "cannot use compound assignment on final local variable"
					}
					if (isReachable) {
						localBinding.useFlag = LocalVariableBinding.USED;
					} else if (localBinding.useFlag == LocalVariableBinding.UNUSED) {
						localBinding.useFlag = LocalVariableBinding.FAKE_USED;
					}
			}
		}
		if (assignment.expression != null) {
			flowInfo = assignment.expression.analyseCode(currentScope, flowContext, flowInfo).unconditionalInits();
		}
		switch (bits & RestrictiveFlagMASK) {
			case Binding.FIELD : // assigning to a field
				manageSyntheticAccessIfNecessary(currentScope, flowInfo, false /*write-access*/);
	
				FieldBinding fieldBinding = (FieldBinding) binding;
				ReferenceBinding declaringClass = fieldBinding.declaringClass;
				// check if accessing enum static field in initializer
				if (declaringClass.isEnum()) {
					MethodScope methodScope = currentScope.methodScope();
					SourceTypeBinding sourceType = currentScope.enclosingSourceType();
					if (fieldBinding.isStatic()
							&& this.constant == Constant.NotAConstant
							&& !methodScope.isStatic
							&& (sourceType == declaringClass || sourceType.superclass == declaringClass) // enum constant body
							&& methodScope.isInsideInitializerOrConstructor()) {
						currentScope.problemReporter().enumStaticFieldUsedDuringInitialization(fieldBinding, this);
					}
				}					
				// check if assigning a final field
				if (fieldBinding.isFinal()) {
					// inside a context where allowed
					if (!isCompound && fieldBinding.isBlankFinal() && currentScope.allowBlankFinalFieldAssignment(fieldBinding)) {
						if (flowInfo.isPotentiallyAssigned(fieldBinding)) {
							currentScope.problemReporter().duplicateInitializationOfBlankFinalField(fieldBinding, this);
						} else {
							flowContext.recordSettingFinal(fieldBinding, this, flowInfo);						
						}
						flowInfo.markAsDefinitelyAssigned(fieldBinding);
					} else {
						currentScope.problemReporter().cannotAssignToFinalField(fieldBinding, this);
					}
				}
				break;
			case Binding.LOCAL : // assigning to a local variable 
				LocalVariableBinding localBinding = (LocalVariableBinding) binding;
				if (!flowInfo.isDefinitelyAssigned(localBinding)){// for local variable debug attributes
					bits |= FirstAssignmentToLocal;
				} else {
					bits &= ~FirstAssignmentToLocal;
				}
				if (localBinding.isFinal()) {
					if ((bits & DepthMASK) == 0) {
						// tolerate assignment to final local in unreachable code (45674)
						if ((isReachable && isCompound) || !localBinding.isBlankFinal()){
							currentScope.problemReporter().cannotAssignToFinalLocal(localBinding, this);
						} else if (flowInfo.isPotentiallyAssigned(localBinding)) {
							currentScope.problemReporter().duplicateInitializationOfFinalLocal(localBinding, this);
						} else {
							flowContext.recordSettingFinal(localBinding, this, flowInfo);								
						}
					} else {
						currentScope.problemReporter().cannotAssignToFinalOuterLocal(localBinding, this);
					}
				}
				else /* avoid double diagnostic */ if (localBinding.isArgument) {
					currentScope.problemReporter().parameterAssignment(localBinding, this);
				}
				flowInfo.markAsDefinitelyAssigned(localBinding);
		}
		manageEnclosingInstanceAccessIfNecessary(currentScope, flowInfo);
		return flowInfo;
	}
	public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
		return analyseCode(currentScope, flowContext, flowInfo, true);
	}
	public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo, boolean valueRequired) {
	
		switch (bits & RestrictiveFlagMASK) {
			case Binding.FIELD : // reading a field
				if (valueRequired) {
					manageSyntheticAccessIfNecessary(currentScope, flowInfo, true /*read-access*/);
				}
				FieldBinding fieldBinding = (FieldBinding) binding;
				ReferenceBinding declaringClass = fieldBinding.declaringClass;
				// check if accessing enum static field in initializer
				if (declaringClass.isEnum()) {
					MethodScope methodScope = currentScope.methodScope();
					SourceTypeBinding sourceType = currentScope.enclosingSourceType();
					if (fieldBinding.isStatic()
							&& this.constant == Constant.NotAConstant
							&& !methodScope.isStatic
							&& (sourceType == declaringClass || sourceType.superclass == declaringClass) // enum constant body
							&& methodScope.isInsideInitializerOrConstructor()) {
						currentScope.problemReporter().enumStaticFieldUsedDuringInitialization(fieldBinding, this);
					}
				}				
				// check if reading a final blank field
				if (fieldBinding.isBlankFinal() && currentScope.allowBlankFinalFieldAssignment(fieldBinding)) {
					if (!flowInfo.isDefinitelyAssigned(fieldBinding)) {
						currentScope.problemReporter().uninitializedBlankFinalField(fieldBinding, this);
					}
				}
				break;
			case Binding.LOCAL : // reading a local variable
				LocalVariableBinding localBinding;
				if (!flowInfo.isDefinitelyAssigned(localBinding = (LocalVariableBinding) binding)) {
					currentScope.problemReporter().uninitializedLocalVariable(localBinding, this);
				}
				if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) == 0)	{
					localBinding.useFlag = LocalVariableBinding.USED;
				} else if (localBinding.useFlag == LocalVariableBinding.UNUSED) {
					localBinding.useFlag = LocalVariableBinding.FAKE_USED;
				}
		}
		if (valueRequired) {
			manageEnclosingInstanceAccessIfNecessary(currentScope, flowInfo);
		}
		return flowInfo;
	}
	
	public TypeBinding checkFieldAccess(BlockScope scope) {
	
		FieldBinding fieldBinding = (FieldBinding) binding;
		
		bits &= ~RestrictiveFlagMASK; // clear bits
		bits |= Binding.FIELD;
		MethodScope methodScope = scope.methodScope();
		boolean isStatic = fieldBinding.isStatic();
		if (!isStatic) {
			// must check for the static status....
			if (methodScope.isStatic) {
				scope.problemReporter().staticFieldAccessToNonStaticVariable(this, fieldBinding);
				this.constant = Constant.NotAConstant;
				return fieldBinding.type;
			}
		}
		this.constant = fieldBinding.constant();
	
		if (isFieldUseDeprecated(fieldBinding, scope, (this.bits & IsStrictlyAssigned) !=0))
			scope.problemReporter().deprecatedField(fieldBinding, this);
	
		if ((this.bits & IsStrictlyAssigned) == 0
			&& methodScope.enclosingSourceType() == fieldBinding.declaringClass
			&& methodScope.lastVisibleFieldID >= 0
			&& fieldBinding.id >= methodScope.lastVisibleFieldID) {
			//if the field is static and ms is not .... then it is valid
			if (!fieldBinding.isStatic() || methodScope.isStatic)
				scope.problemReporter().forwardReference(this, 0, methodScope.enclosingSourceType());
		}
		//====================================================
	
		return fieldBinding.type;
	
	}
	
	/**
	 * @see org.eclipse.jdt.internal.compiler.ast.Expression#computeConversion(org.eclipse.jdt.internal.compiler.lookup.Scope, org.eclipse.jdt.internal.compiler.lookup.TypeBinding, org.eclipse.jdt.internal.compiler.lookup.TypeBinding)
	 */
	public void computeConversion(Scope scope, TypeBinding runtimeTimeType, TypeBinding compileTimeType) {
		if (runtimeTimeType == null || compileTimeType == null)
			return;				
		if ((bits & Binding.FIELD) != 0 && this.binding != null && this.binding.isValidBinding()) {
			// set the generic cast after the fact, once the type expectation is fully known (no need for strict cast)
			FieldBinding field = (FieldBinding) this.binding;
			FieldBinding originalBinding = field.original();
			TypeBinding originalType = originalBinding.type;
		    // extra cast needed if method return type is type variable
			if (originalBinding != field 
					&& originalType != field.type
					&& runtimeTimeType.id != T_JavaLangObject
					&& (originalType.tagBits & TagBits.HasTypeVariable) != 0) {
		    	TypeBinding targetType = (!compileTimeType.isBaseType() && runtimeTimeType.isBaseType()) 
		    		? compileTimeType  // unboxing: checkcast before conversion
		    		: runtimeTimeType;
		        this.genericCast = originalType.genericCast(scope.boxing(targetType));
			} 	
		}
		super.computeConversion(scope, runtimeTimeType, compileTimeType);
	}	

	public void generateAssignment(BlockScope currentScope, CodeStream codeStream, Assignment assignment, boolean valueRequired) {
	
		// optimizing assignment like: i = i + 1 or i = 1 + i
		if (assignment.expression.isCompactableOperation()) {
			BinaryExpression operation = (BinaryExpression) assignment.expression;
			int operator = (operation.bits & OperatorMASK) >> OperatorSHIFT;
			SingleNameReference variableReference;
			if ((operation.left instanceof SingleNameReference) && ((variableReference = (SingleNameReference) operation.left).binding == binding)) {
				// i = i + value, then use the variable on the right hand side, since it has the correct implicit conversion
				variableReference.generateCompoundAssignment(currentScope, codeStream, syntheticAccessors == null ? null : syntheticAccessors[WRITE], operation.right, operator, operation.implicitConversion, valueRequired);
				if (valueRequired) {
					codeStream.generateImplicitConversion(assignment.implicitConversion);
				}				
				return;
			} 
			if ((operation.right instanceof SingleNameReference)
					&& ((operator == PLUS) || (operator == MULTIPLY)) // only commutative operations
					&& ((variableReference = (SingleNameReference) operation.right).binding == binding)
					&& (operation.left.constant != Constant.NotAConstant) // exclude non constant expressions, since could have side-effect
					&& (((operation.left.implicitConversion & IMPLICIT_CONVERSION_MASK) >> 4) != T_JavaLangString) // exclude string concatenation which would occur backwards
					&& (((operation.right.implicitConversion & IMPLICIT_CONVERSION_MASK) >> 4) != T_JavaLangString)) { // exclude string concatenation which would occur backwards
				// i = value + i, then use the variable on the right hand side, since it has the correct implicit conversion
				variableReference.generateCompoundAssignment(currentScope, codeStream, syntheticAccessors == null ? null : syntheticAccessors[WRITE], operation.left, operator, operation.implicitConversion, valueRequired);
				if (valueRequired) {
					codeStream.generateImplicitConversion(assignment.implicitConversion);
				}				
				return;
			}
		}
		switch (bits & RestrictiveFlagMASK) {
			case Binding.FIELD : // assigning to a field
				FieldBinding fieldBinding;
				int pc = codeStream.position;
				if (!(fieldBinding = (FieldBinding) this.codegenBinding).isStatic()) { // need a receiver?
					if ((bits & DepthMASK) != 0) {
						ReferenceBinding targetType = currentScope.enclosingSourceType().enclosingTypeAt((bits & DepthMASK) >> DepthSHIFT);
						Object[] emulationPath = currentScope.getEmulationPath(targetType, true /*only exact match*/, false/*consider enclosing arg*/);
						codeStream.generateOuterAccess(emulationPath, this, targetType, currentScope);
					} else {
						this.generateReceiver(codeStream);
					}
				}
				codeStream.recordPositionsFrom(pc, this.sourceStart);
				assignment.expression.generateCode(currentScope, codeStream, true);
				fieldStore(codeStream, fieldBinding, syntheticAccessors == null ? null : syntheticAccessors[WRITE], valueRequired);
				if (valueRequired) {
					codeStream.generateImplicitConversion(assignment.implicitConversion);
				}
				// no need for generic cast as value got dupped
				return;
			case Binding.LOCAL : // assigning to a local variable
				LocalVariableBinding localBinding = (LocalVariableBinding) this.codegenBinding;
				if (localBinding.resolvedPosition != -1) {
					assignment.expression.generateCode(currentScope, codeStream, true);
				} else {
					if (assignment.expression.constant != Constant.NotAConstant) {
						// assigning an unused local to a constant value = no actual assignment is necessary
						if (valueRequired) {
							codeStream.generateConstant(assignment.expression.constant, assignment.implicitConversion);
						}
					} else {
						assignment.expression.generateCode(currentScope, codeStream, true);
						/* Even though the value may not be required, we force it to be produced, and discard it later
						on if it was actually not necessary, so as to provide the same behavior as JDK1.2beta3.	*/
						if (valueRequired) {
							codeStream.generateImplicitConversion(assignment.implicitConversion); // implicit conversion
						} else {
							if ((localBinding.type == TypeBinding.LONG) || (localBinding.type == TypeBinding.DOUBLE)) {
								codeStream.pop2();
							} else {
								codeStream.pop();
							}
						}
					}
					return;
				}
				// 26903, need extra cast to store null in array local var	
				if (localBinding.type.isArrayType() 
					&& (assignment.expression.resolvedType == TypeBinding.NULL	// arrayLoc = null
 ((assignment.expression instanceof CastExpression)	// arrayLoc = (type[])null
							&& (((CastExpression)assignment.expression).innermostCastedExpression().resolvedType == TypeBinding.NULL)))){
					codeStream.checkcast(localBinding.type); 
				}
				
				// normal local assignment (since cannot store in outer local which are final locations)
				codeStream.store(localBinding, valueRequired);
				if ((bits & FirstAssignmentToLocal) != 0) { // for local variable debug attributes
					localBinding.recordInitializationStartPC(codeStream.position);
				}
				// implicit conversion
				if (valueRequired) {
					codeStream.generateImplicitConversion(assignment.implicitConversion);
				}
		}
	}
	public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
		int pc = codeStream.position;
		if (constant != Constant.NotAConstant) {
			if (valueRequired) {
				codeStream.generateConstant(constant, implicitConversion);
			}
		} else {
			switch (bits & RestrictiveFlagMASK) {
				case Binding.FIELD : // reading a field
					FieldBinding fieldBinding = (FieldBinding) this.codegenBinding;
					Constant fieldConstant = fieldBinding.constant();
					if (fieldConstant != Constant.NotAConstant) {
						// directly use inlined value for constant fields
						if (valueRequired) {
							codeStream.generateConstant(fieldConstant, implicitConversion);
						}
					} else {
						if (valueRequired) {
							boolean isStatic = fieldBinding.isStatic();
							if (!isStatic) {
								if ((bits & DepthMASK) != 0) {
									ReferenceBinding targetType = currentScope.enclosingSourceType().enclosingTypeAt((bits & DepthMASK) >> DepthSHIFT);
									Object[] emulationPath = currentScope.getEmulationPath(targetType, true /*only exact match*/, false/*consider enclosing arg*/);
									codeStream.generateOuterAccess(emulationPath, this, targetType, currentScope);
								} else {
									generateReceiver(codeStream);
								}
							}
							// managing private access							
							if ((syntheticAccessors == null) || (syntheticAccessors[READ] == null)) {
								if (isStatic) {
									codeStream.getstatic(fieldBinding);
								} else {
									codeStream.getfield(fieldBinding);
								}
							} else {
								codeStream.invokestatic(syntheticAccessors[READ]);
							}
							if (valueRequired) {
								if (this.genericCast != null) codeStream.checkcast(this.genericCast);			
								codeStream.generateImplicitConversion(implicitConversion);
							} else {
								// could occur if !valueRequired but compliance >= 1.4
								switch (fieldBinding.type.id) {
									case T_long :
									case T_double :
										codeStream.pop2();
										break;
									default :
										codeStream.pop();
								}
							}							
						}
					}
					break;
				case Binding.LOCAL : // reading a local
					LocalVariableBinding localBinding = (LocalVariableBinding) this.codegenBinding;
					if (valueRequired) {
						// outer local?
						if ((bits & DepthMASK) != 0) {
							// outer local can be reached either through a synthetic arg or a synthetic field
							VariableBinding[] path = currentScope.getEmulationPath(localBinding);
							codeStream.generateOuterAccess(path, this, localBinding, currentScope);
						} else {
							// regular local variable read
							codeStream.load(localBinding);
						}
						codeStream.generateImplicitConversion(implicitConversion);
					}
			}
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}
	/*
	 * Regular API for compound assignment, relies on the fact that there is only one reference to the
	 * variable, which carries both synthetic read/write accessors.
	 * The APIs with an extra argument is used whenever there are two references to the same variable which
	 * are optimized in one access: e.g "a = a + 1" optimized into "a++".
	 */
	public void generateCompoundAssignment(BlockScope currentScope, CodeStream codeStream, Expression expression, int operator, int assignmentImplicitConversion, boolean valueRequired) {
	
		this.generateCompoundAssignment(
			currentScope, 
			codeStream, 
			syntheticAccessors == null ? null : syntheticAccessors[WRITE],
			expression,
			operator, 
			assignmentImplicitConversion, 
			valueRequired);
	}
	/*
	 * The APIs with an extra argument is used whenever there are two references to the same variable which
	 * are optimized in one access: e.g "a = a + 1" optimized into "a++".
	 */
	public void generateCompoundAssignment(BlockScope currentScope, CodeStream codeStream, MethodBinding writeAccessor, Expression expression, int operator, int assignmentImplicitConversion, boolean valueRequired) {
		switch (bits & RestrictiveFlagMASK) {
			case Binding.FIELD : // assigning to a field
				FieldBinding fieldBinding;
				if ((fieldBinding = (FieldBinding) this.codegenBinding).isStatic()) {
					if ((syntheticAccessors == null) || (syntheticAccessors[READ] == null)) {
						codeStream.getstatic(fieldBinding);
					} else {
						codeStream.invokestatic(syntheticAccessors[READ]);
					}
				} else {
					if ((bits & DepthMASK) != 0) {
						ReferenceBinding targetType = currentScope.enclosingSourceType().enclosingTypeAt((bits & DepthMASK) >> DepthSHIFT);
						Object[] emulationPath = currentScope.getEmulationPath(targetType, true /*only exact match*/, false/*consider enclosing arg*/);
						codeStream.generateOuterAccess(emulationPath, this, targetType, currentScope);
					} else {
						codeStream.aload_0();
					}
					codeStream.dup();
					if ((syntheticAccessors == null) || (syntheticAccessors[READ] == null)) {
						codeStream.getfield(fieldBinding);
					} else {
						codeStream.invokestatic(syntheticAccessors[READ]);
					}
				}
				break;
			case Binding.LOCAL : // assigning to a local variable (cannot assign to outer local)
				LocalVariableBinding localBinding = (LocalVariableBinding) this.codegenBinding;
				Constant assignConstant;
				int increment;
				// using incr bytecode if possible
				switch (localBinding.type.id) {
					case T_JavaLangString :
						codeStream.generateStringConcatenationAppend(currentScope, this, expression);
						if (valueRequired) {
							codeStream.dup();
						}
						codeStream.store(localBinding, false);
						return;
					case T_int :
						if (((assignConstant = expression.constant) != Constant.NotAConstant) 
							&& (assignConstant.typeID() != T_float) // only for integral types
							&& (assignConstant.typeID() != T_double)
							&& ((increment = assignConstant.intValue()) == (short) increment)) { // 16 bits value
							switch (operator) {
								case PLUS :
									codeStream.iinc(localBinding.resolvedPosition, increment);
									if (valueRequired) {
										codeStream.load(localBinding);
									}
									return;
								case MINUS :
									codeStream.iinc(localBinding.resolvedPosition, -increment);
									if (valueRequired) {
										codeStream.load(localBinding);
									}
									return;
							}
						}
					default :
						codeStream.load(localBinding);
				}
		}
		// perform the actual compound operation
		int operationTypeID;
		switch(operationTypeID = (implicitConversion & IMPLICIT_CONVERSION_MASK) >> 4) {
			case T_JavaLangString :
			case T_JavaLangObject :
			case T_undefined :
				// we enter here if the single name reference is a field of type java.lang.String or if the type of the 
				// operation is java.lang.Object
				// For example: o = o + ""; // where the compiled type of o is java.lang.Object.
				codeStream.generateStringConcatenationAppend(currentScope, null, expression);
				// no need for generic cast on previous #getfield since using Object string buffer methods.			
				break;
			default :
				// promote the array reference to the suitable operation type
				if (this.genericCast != null)
					codeStream.checkcast(this.genericCast);
				codeStream.generateImplicitConversion(this.implicitConversion);
				// generate the increment value (will by itself  be promoted to the operation value)
				if (expression == IntLiteral.One){ // prefix operation
					codeStream.generateConstant(expression.constant, this.implicitConversion);			
				} else {
					expression.generateCode(currentScope, codeStream, true);
				}		
				// perform the operation
				codeStream.sendOperator(operator, operationTypeID);
				// cast the value back to the array reference type
				codeStream.generateImplicitConversion(assignmentImplicitConversion);
		}
		// store the result back into the variable
		switch (bits & RestrictiveFlagMASK) {
			case Binding.FIELD : // assigning to a field
				fieldStore(codeStream, (FieldBinding) this.codegenBinding, writeAccessor, valueRequired);
				// no need for generic cast as value got dupped
				return;
			case Binding.LOCAL : // assigning to a local variable
				LocalVariableBinding localBinding = (LocalVariableBinding) this.codegenBinding;
				if (valueRequired) {
					if ((localBinding.type == TypeBinding.LONG) || (localBinding.type == TypeBinding.DOUBLE)) {
						codeStream.dup2();
					} else {
						codeStream.dup();
					}
				}
				codeStream.store(localBinding, false);
		}
	}
	
	public void generatePostIncrement(BlockScope currentScope, CodeStream codeStream, CompoundAssignment postIncrement, boolean valueRequired) {
		switch (bits & RestrictiveFlagMASK) {
			case Binding.FIELD : // assigning to a field
				FieldBinding fieldBinding;
				if ((fieldBinding = (FieldBinding) this.codegenBinding).isStatic()) {
					if ((syntheticAccessors == null) || (syntheticAccessors[READ] == null)) {
						codeStream.getstatic(fieldBinding);
					} else {
						codeStream.invokestatic(syntheticAccessors[READ]);
					}
				} else {
					if ((bits & DepthMASK) != 0) {
						ReferenceBinding targetType = currentScope.enclosingSourceType().enclosingTypeAt((bits & DepthMASK) >> DepthSHIFT);
						Object[] emulationPath = currentScope.getEmulationPath(targetType, true /*only exact match*/, false/*consider enclosing arg*/);
						codeStream.generateOuterAccess(emulationPath, this, targetType, currentScope);
					} else {
						codeStream.aload_0();
					}
					codeStream.dup();
					if ((syntheticAccessors == null) || (syntheticAccessors[READ] == null)) {
						codeStream.getfield(fieldBinding);
					} else {
						codeStream.invokestatic(syntheticAccessors[READ]);
					}
				}
				if (valueRequired) {
					if (fieldBinding.isStatic()) {
						if ((fieldBinding.type == TypeBinding.LONG) || (fieldBinding.type == TypeBinding.DOUBLE)) {
							codeStream.dup2();
						} else {
							codeStream.dup();
						}
					} else { // Stack:  [owner][old field value]  ---> [old field value][owner][old field value]
						if ((fieldBinding.type == TypeBinding.LONG) || (fieldBinding.type == TypeBinding.DOUBLE)) {
							codeStream.dup2_x1();
						} else {
							codeStream.dup_x1();
						}
					}
				}
				if (this.genericCast != null) 
					codeStream.checkcast(this.genericCast);
				codeStream.generateImplicitConversion(this.implicitConversion);		
				codeStream.generateConstant(postIncrement.expression.constant, this.implicitConversion);
				codeStream.sendOperator(postIncrement.operator, this.implicitConversion & COMPILE_TYPE_MASK);
				codeStream.generateImplicitConversion(postIncrement.preAssignImplicitConversion);
				fieldStore(codeStream, fieldBinding, this.syntheticAccessors == null ? null : this.syntheticAccessors[WRITE], false);
				// no need for generic cast 
				return;
			case Binding.LOCAL : // assigning to a local variable
				LocalVariableBinding localBinding = (LocalVariableBinding) this.codegenBinding;
				// using incr bytecode if possible
				if (localBinding.type == TypeBinding.INT) {
					if (valueRequired) {
						codeStream.load(localBinding);
					}
					if (postIncrement.operator == PLUS) {
						codeStream.iinc(localBinding.resolvedPosition, 1);
					} else {
						codeStream.iinc(localBinding.resolvedPosition, -1);
					}
				} else {
					codeStream.load(localBinding);
					if (valueRequired){
						if ((localBinding.type == TypeBinding.LONG) || (localBinding.type == TypeBinding.DOUBLE)) {
							codeStream.dup2();
						} else {
							codeStream.dup();
						}
					}
					codeStream.generateImplicitConversion(implicitConversion);
					codeStream.generateConstant(postIncrement.expression.constant, implicitConversion);
					codeStream.sendOperator(postIncrement.operator, this.implicitConversion & COMPILE_TYPE_MASK);
					codeStream.generateImplicitConversion(postIncrement.preAssignImplicitConversion);
	
					codeStream.store(localBinding, false);
				}
		}
	}	
	
	public void generateReceiver(CodeStream codeStream) {
		codeStream.aload_0();
	}
	
	/**
	 * @see org.eclipse.jdt.internal.compiler.lookup.InvocationSite#genericTypeArguments()
	 */
	public TypeBinding[] genericTypeArguments() {
		return null;
	}

	/**
	 * Returns the local variable referenced by this node. Can be a direct reference (SingleNameReference)
	 * or thru a cast expression etc...
	 */
	public LocalVariableBinding localVariableBinding() {
		switch (bits & RestrictiveFlagMASK) {
			case Binding.FIELD : // reading a field
				break;
			case Binding.LOCAL : // reading a local variable
				return (LocalVariableBinding) this.binding;
		}
		return null;
	}
	
	public void manageEnclosingInstanceAccessIfNecessary(BlockScope currentScope, FlowInfo flowInfo) {
	
		if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) == 0)	{
		//If inlinable field, forget the access emulation, the code gen will directly target it
		if (((bits & DepthMASK) == 0) || (constant != Constant.NotAConstant)) return;
	
		if ((bits & RestrictiveFlagMASK) == Binding.LOCAL) {
			currentScope.emulateOuterAccess((LocalVariableBinding) binding);
		}
		}
	}
	public void manageSyntheticAccessIfNecessary(BlockScope currentScope, FlowInfo flowInfo, boolean isReadAccess) {
	
		if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) != 0)	return;
	
		//If inlinable field, forget the access emulation, the code gen will directly target it
		if (constant != Constant.NotAConstant)
			return;
	
		if ((bits & Binding.FIELD) != 0) {
			FieldBinding fieldBinding = (FieldBinding) binding;
			FieldBinding codegenField = fieldBinding.original();
			this.codegenBinding = codegenField;
			if (((bits & DepthMASK) != 0)
				&& (codegenField.isPrivate() // private access
 (codegenField.isProtected() // implicit protected access
							&& codegenField.declaringClass.getPackage() != currentScope.enclosingSourceType().getPackage()))) {
				if (syntheticAccessors == null)
					syntheticAccessors = new MethodBinding[2];
				syntheticAccessors[isReadAccess ? READ : WRITE] = 
				    ((SourceTypeBinding)currentScope.enclosingSourceType().
						enclosingTypeAt((bits & DepthMASK) >> DepthSHIFT)).addSyntheticMethod(codegenField, isReadAccess);
				currentScope.problemReporter().needToEmulateFieldAccess(codegenField, this, isReadAccess);
				return;
			}
			// if the binding declaring class is not visible, need special action
			// for runtime compatibility on 1.2 VMs : change the declaring class of the binding
			// NOTE: from target 1.2 on, field's declaring class is touched if any different from receiver type
			// and not from Object or implicit static field access.	
			if (fieldBinding.declaringClass != this.actualReceiverType
					&& !this.actualReceiverType.isArrayType()
					&& fieldBinding.declaringClass != null // array.length
					&& fieldBinding.constant() == Constant.NotAConstant) {
				CompilerOptions options = currentScope.compilerOptions();
				if ((options.targetJDK >= ClassFileConstants.JDK1_2
						&& (options.complianceLevel >= ClassFileConstants.JDK1_4 || !fieldBinding.isStatic())
						&& fieldBinding.declaringClass.id != T_JavaLangObject) // no change for Object fields
 !fieldBinding.declaringClass.canBeSeenBy(currentScope)) {
		
					this.codegenBinding = 
					    currentScope.enclosingSourceType().getUpdatedFieldBinding(
						       codegenField, 
						        (ReferenceBinding)this.actualReceiverType.erasure());
				}
			}					
		}
	}

public int nullStatus(FlowInfo flowInfo) {
	if (this.constant != null && this.constant != Constant.NotAConstant) {
		return FlowInfo.NON_NULL; // constant expression cannot be null
	}
	switch (bits & RestrictiveFlagMASK) {
		case Binding.FIELD : // reading a field
			return FlowInfo.UNKNOWN;
		case Binding.LOCAL : // reading a local variable
			LocalVariableBinding local = (LocalVariableBinding) this.binding;
			if (local != null) {
				if (flowInfo.isDefinitelyNull(local))
					return FlowInfo.NULL;
				if (flowInfo.isDefinitelyNonNull(local))
					return FlowInfo.NON_NULL;
				return FlowInfo.UNKNOWN;
			}
	}
	return FlowInfo.NON_NULL; 
// REVIEW should never get here?
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
	
	public StringBuffer printExpression(int indent, StringBuffer output){
	
		return output.append(token);
	}
	public TypeBinding reportError(BlockScope scope) {
		
		//=====error cases=======
		constant = Constant.NotAConstant;
		if (binding instanceof ProblemFieldBinding) {
			scope.problemReporter().invalidField(this, (FieldBinding) binding);
		} else if (binding instanceof ProblemReferenceBinding) {
			scope.problemReporter().invalidType(this, (TypeBinding) binding);
		} else {
			scope.problemReporter().unresolvableReference(this, binding);
		}
		return null;
	}
		
	public TypeBinding resolveType(BlockScope scope) {
		// for code gen, harm the restrictiveFlag 	
	
		if (this.actualReceiverType != null) {
			this.binding = scope.getField(this.actualReceiverType, token, this);
		} else {
			this.actualReceiverType = scope.enclosingSourceType();
			this.binding = scope.getBinding(token, bits & RestrictiveFlagMASK, this, true /*resolve*/);
		}
		this.codegenBinding = this.binding;
		if (this.binding.isValidBinding()) {
			switch (bits & RestrictiveFlagMASK) {
				case Binding.VARIABLE : // =========only variable============
				case Binding.VARIABLE | Binding.TYPE : //====both variable and type============
					if (binding instanceof VariableBinding) {
						VariableBinding variable = (VariableBinding) binding;
						if (binding instanceof LocalVariableBinding) {
							bits &= ~RestrictiveFlagMASK;  // clear bits
							bits |= Binding.LOCAL;
							if (!variable.isFinal() && (bits & DepthMASK) != 0) {
								scope.problemReporter().cannotReferToNonFinalOuterLocal((LocalVariableBinding)variable, this);
							}
							TypeBinding fieldType = variable.type;
							if ((this.bits & IsStrictlyAssigned) == 0) {
								constant = variable.constant();
								if (fieldType != null) 
									fieldType = fieldType.capture(scope, this.sourceEnd); // perform capture conversion if read access
							} else {
								constant = Constant.NotAConstant;
							}
							return this.resolvedType = fieldType;
						}
						// a field
						FieldBinding field = (FieldBinding) this.binding;
						if (!field.isStatic() && scope.compilerOptions().getSeverity(CompilerOptions.UnqualifiedFieldAccess) != ProblemSeverities.Ignore) {
							scope.problemReporter().unqualifiedFieldAccess(this, field);
						}
						// perform capture conversion if read access
						TypeBinding fieldType = checkFieldAccess(scope);
						return this.resolvedType = 
							(((this.bits & IsStrictlyAssigned) == 0) 
								? fieldType.capture(scope, this.sourceEnd)
								: fieldType);
					}
	
					// thus it was a type
					bits &= ~RestrictiveFlagMASK;  // clear bits
					bits |= Binding.TYPE;
				case Binding.TYPE : //========only type==============
					constant = Constant.NotAConstant;
					//deprecated test
					TypeBinding type = (TypeBinding)binding;
					if (isTypeUseDeprecated(type, scope))
						scope.problemReporter().deprecatedType(type, this);
					type = scope.environment().convertToRawType(type);
					return this.resolvedType = type;
			}
		}
	
		// error scenarii
		return this.resolvedType = this.reportError(scope);
	}
	
	public void traverse(ASTVisitor visitor, BlockScope scope) {
		
		visitor.visit(this, scope);
		visitor.endVisit(this, scope);
	}
	
	public String unboundReferenceErrorName(){
	
		return new String(token);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7717.java