error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6778.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6778.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6778.java
text:
```scala
i@@f (this.binding.isEnum() && !this.binding.isAnonymousType()) {

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

import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.parser.*;
import org.eclipse.jdt.internal.compiler.problem.*;

public class TypeDeclaration
	extends Statement
	implements ProblemSeverities, ReferenceContext {

	public static final char[] ANONYMOUS_EMPTY_NAME = new char[] {};

	// Type decl kinds
	public static final int CLASS_DECL = 1;
	public static final int INTERFACE_DECL = 2;
	public static final int ENUM_DECL = 3;	
	public static final int ANNOTATION_TYPE_DECL = 4;
	
	public int modifiers = ClassFileConstants.AccDefault;
	public int modifiersSourceStart;
	public Annotation[] annotations;
	public char[] name;
	public TypeReference superclass;
	public TypeReference[] superInterfaces;
	public FieldDeclaration[] fields;
	public AbstractMethodDeclaration[] methods;
	public TypeDeclaration[] memberTypes;
	public SourceTypeBinding binding;
	public ClassScope scope;
	public MethodScope initializerScope;
	public MethodScope staticInitializerScope;
	public boolean ignoreFurtherInvestigation = false;
	public int maxFieldCount;
	public int declarationSourceStart;
	public int declarationSourceEnd;
	public int bodyStart;
	public int bodyEnd; // doesn't include the trailing comment if any.
	protected boolean hasBeenGenerated = false;
	public CompilationResult compilationResult;
	public MethodDeclaration[] missingAbstractMethods;
	public Javadoc javadoc;	
	
	public QualifiedAllocationExpression allocation; // for anonymous only
	public TypeDeclaration enclosingType; // for member types only
	
	public FieldBinding enumValuesSyntheticfield; 	// for enum

	// 1.5 support
	public TypeParameter[] typeParameters;
	
	public TypeDeclaration(CompilationResult compilationResult){
		this.compilationResult = compilationResult;
	}
		
	/*
	 *	We cause the compilation task to abort to a given extent.
	 */
	public void abort(int abortLevel, IProblem problem) {

		switch (abortLevel) {
			case AbortCompilation :
				throw new AbortCompilation(this.compilationResult, problem);
			case AbortCompilationUnit :
				throw new AbortCompilationUnit(this.compilationResult, problem);
			case AbortMethod :
				throw new AbortMethod(this.compilationResult, problem);
			default :
				throw new AbortType(this.compilationResult, problem);
		}
	}
	/**
	 * This method is responsible for adding a <clinit> method declaration to the type method collections.
	 * Note that this implementation is inserting it in first place (as VAJ or javac), and that this
	 * impacts the behavior of the method ConstantPool.resetForClinit(int. int), in so far as 
	 * the latter will have to reset the constant pool state accordingly (if it was added first, it does 
	 * not need to preserve some of the method specific cached entries since this will be the first method).
	 * inserts the clinit method declaration in the first position.
	 * 
	 * @see org.eclipse.jdt.internal.compiler.codegen.ConstantPool#resetForClinit(int, int)
	 */
	public final void addClinit() {

		//see comment on needClassInitMethod
		if (needClassInitMethod()) {
			int length;
			AbstractMethodDeclaration[] methodDeclarations;
			if ((methodDeclarations = this.methods) == null) {
				length = 0;
				methodDeclarations = new AbstractMethodDeclaration[1];
			} else {
				length = methodDeclarations.length;
				System.arraycopy(
					methodDeclarations,
					0,
					(methodDeclarations = new AbstractMethodDeclaration[length + 1]),
					1,
					length);
			}
			Clinit clinit = new Clinit(this.compilationResult);
			methodDeclarations[0] = clinit;
			// clinit is added in first location, so as to minimize the use of ldcw (big consumer of constant inits)
			clinit.declarationSourceStart = clinit.sourceStart = sourceStart;
			clinit.declarationSourceEnd = clinit.sourceEnd = sourceEnd;
			clinit.bodyEnd = sourceEnd;
			this.methods = methodDeclarations;
		}
	}

	/*
	 * INTERNAL USE ONLY - Creates a fake method declaration for the corresponding binding.
	 * It is used to report errors for missing abstract methods.
	 */
	public MethodDeclaration addMissingAbstractMethodFor(MethodBinding methodBinding) {
		TypeBinding[] argumentTypes = methodBinding.parameters;
		int argumentsLength = argumentTypes.length;
		//the constructor
		MethodDeclaration methodDeclaration = new MethodDeclaration(this.compilationResult);
		methodDeclaration.selector = methodBinding.selector;
		methodDeclaration.sourceStart = sourceStart;
		methodDeclaration.sourceEnd = sourceEnd;
		methodDeclaration.modifiers = methodBinding.getAccessFlags() & ~ClassFileConstants.AccAbstract;

		if (argumentsLength > 0) {
			String baseName = "arg";//$NON-NLS-1$
			Argument[] arguments = (methodDeclaration.arguments = new Argument[argumentsLength]);
			for (int i = argumentsLength; --i >= 0;) {
				arguments[i] = new Argument((baseName + i).toCharArray(), 0L, null /*type ref*/, ClassFileConstants.AccDefault);
			}
		}

		//adding the constructor in the methods list
		if (this.missingAbstractMethods == null) {
			this.missingAbstractMethods = new MethodDeclaration[] { methodDeclaration };
		} else {
			MethodDeclaration[] newMethods;
			System.arraycopy(
				this.missingAbstractMethods,
				0,
				newMethods = new MethodDeclaration[this.missingAbstractMethods.length + 1],
				1,
				this.missingAbstractMethods.length);
			newMethods[0] = methodDeclaration;
			this.missingAbstractMethods = newMethods;
		}

		//============BINDING UPDATE==========================
		methodDeclaration.binding = new MethodBinding(
				methodDeclaration.modifiers, //methodDeclaration
				methodBinding.selector,
				methodBinding.returnType,
				argumentsLength == 0 ? Binding.NO_PARAMETERS : argumentTypes, //arguments bindings
				methodBinding.thrownExceptions, //exceptions
				binding); //declaringClass
				
		methodDeclaration.scope = new MethodScope(scope, methodDeclaration, true);
		methodDeclaration.bindArguments();

/*		if (binding.methods == null) {
			binding.methods = new MethodBinding[] { methodDeclaration.binding };
		} else {
			MethodBinding[] newMethods;
			System.arraycopy(
				binding.methods,
				0,
				newMethods = new MethodBinding[binding.methods.length + 1],
				1,
				binding.methods.length);
			newMethods[0] = methodDeclaration.binding;
			binding.methods = newMethods;
		}*/
		//===================================================

		return methodDeclaration;
	}

	/**
	 *	Flow analysis for a local innertype
	 *
	 */
	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {

		if (ignoreFurtherInvestigation)
			return flowInfo;
		try {
			if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) == 0) {
				bits |= IsReachable;
				LocalTypeBinding localType = (LocalTypeBinding) binding;
				localType.setConstantPoolName(currentScope.compilationUnitScope().computeConstantPoolName(localType));
			}
			manageEnclosingInstanceAccessIfNecessary(currentScope, flowInfo);
			updateMaxFieldCount(); // propagate down the max field count
			internalAnalyseCode(flowContext, flowInfo); 
		} catch (AbortType e) {
			this.ignoreFurtherInvestigation = true;
		}
		return flowInfo;
	}

	/**
	 *	Flow analysis for a member innertype
	 *
	 */
	public void analyseCode(ClassScope enclosingClassScope) {

		if (ignoreFurtherInvestigation)
			return;
		try {
			// propagate down the max field count
			updateMaxFieldCount();
			internalAnalyseCode(null, FlowInfo.initial(maxFieldCount));
		} catch (AbortType e) {
			this.ignoreFurtherInvestigation = true;
		}
	}

	/**
	 *	Flow analysis for a local member innertype
	 *
	 */
	public void analyseCode(
		ClassScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {

		if (ignoreFurtherInvestigation)
			return;
		try {
			if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) == 0) {
				bits |= IsReachable;
				LocalTypeBinding localType = (LocalTypeBinding) binding;
				localType.setConstantPoolName(currentScope.compilationUnitScope().computeConstantPoolName(localType));
			}
			manageEnclosingInstanceAccessIfNecessary(currentScope, flowInfo);
			updateMaxFieldCount(); // propagate down the max field count
			internalAnalyseCode(flowContext, flowInfo);
		} catch (AbortType e) {
			this.ignoreFurtherInvestigation = true;
		}
	}

	/**
	 *	Flow analysis for a package member type
	 *
	 */
	public void analyseCode(CompilationUnitScope unitScope) {

		if (ignoreFurtherInvestigation)
			return;
		try {
			internalAnalyseCode(null, FlowInfo.initial(maxFieldCount));
		} catch (AbortType e) {
			this.ignoreFurtherInvestigation = true;
		}
	}

	/*
	 * Check for constructor vs. method with no return type.
	 * Answers true if at least one constructor is defined
	 */
	public boolean checkConstructors(Parser parser) {

		//if a constructor has not the name of the type,
		//convert it into a method with 'null' as its return type
		boolean hasConstructor = false;
		if (methods != null) {
			for (int i = methods.length; --i >= 0;) {
				AbstractMethodDeclaration am;
				if ((am = methods[i]).isConstructor()) {
					if (!CharOperation.equals(am.selector, name)) {
						// the constructor was in fact a method with no return type
						// unless an explicit constructor call was supplied
						ConstructorDeclaration c = (ConstructorDeclaration) am;
						if (c.constructorCall == null || c.constructorCall.isImplicitSuper()) { //changed to a method
							MethodDeclaration m = parser.convertToMethodDeclaration(c, this.compilationResult);
							methods[i] = m;
						}
					} else {
						switch (kind(this.modifiers)) {
							case TypeDeclaration.INTERFACE_DECL :
								// report the problem and continue the parsing
								parser.problemReporter().interfaceCannotHaveConstructors((ConstructorDeclaration) am);
								break;
							case TypeDeclaration.ANNOTATION_TYPE_DECL :
								// report the problem and continue the parsing
								parser.problemReporter().annotationTypeDeclarationCannotHaveConstructor((ConstructorDeclaration) am);
								break;
								
						}
						hasConstructor = true;
					}
				}
			}
		}
		return hasConstructor;
	}

	public CompilationResult compilationResult() {

		return this.compilationResult;
	}
	
	public ConstructorDeclaration createDefaultConstructor(
		boolean needExplicitConstructorCall,
		boolean needToInsert) {

		//Add to method'set, the default constuctor that just recall the
		//super constructor with no arguments
		//The arguments' type will be positionned by the TC so just use
		//the default int instead of just null (consistency purpose)

		//the constructor
		ConstructorDeclaration constructor = new ConstructorDeclaration(this.compilationResult);
		constructor.isDefaultConstructor = true;
		constructor.selector = this.name;
		if (modifiers != ClassFileConstants.AccDefault) {
			constructor.modifiers =
				(((this.bits & ASTNode.IsMemberType) != 0) && (modifiers & ClassFileConstants.AccPrivate) != 0)
					? ClassFileConstants.AccDefault
					: modifiers & ExtraCompilerModifiers.AccVisibilityMASK;
		}

		//if you change this setting, please update the 
		//SourceIndexer2.buildTypeDeclaration(TypeDeclaration,char[]) method
		constructor.declarationSourceStart = constructor.sourceStart = sourceStart;
		constructor.declarationSourceEnd =
			constructor.sourceEnd = constructor.bodyEnd = sourceEnd;

		//the super call inside the constructor
		if (needExplicitConstructorCall) {
			constructor.constructorCall = SuperReference.implicitSuperConstructorCall();
			constructor.constructorCall.sourceStart = sourceStart;
			constructor.constructorCall.sourceEnd = sourceEnd;
		}

		//adding the constructor in the methods list
		if (needToInsert) {
			if (methods == null) {
				methods = new AbstractMethodDeclaration[] { constructor };
			} else {
				AbstractMethodDeclaration[] newMethods;
				System.arraycopy(
					methods,
					0,
					newMethods = new AbstractMethodDeclaration[methods.length + 1],
					1,
					methods.length);
				newMethods[0] = constructor;
				methods = newMethods;
			}
		}
		return constructor;
	}
	
	// anonymous type constructor creation
	public MethodBinding createDefaultConstructorWithBinding(MethodBinding inheritedConstructorBinding) {

		//Add to method'set, the default constuctor that just recall the
		//super constructor with the same arguments
		String baseName = "$anonymous"; //$NON-NLS-1$
		TypeBinding[] argumentTypes = inheritedConstructorBinding.parameters;
		int argumentsLength = argumentTypes.length;
		//the constructor
		ConstructorDeclaration cd = new ConstructorDeclaration(this.compilationResult);
		cd.selector = new char[] { 'x' }; //no maining
		cd.sourceStart = sourceStart;
		cd.sourceEnd = sourceEnd;
		int newModifiers = modifiers & ExtraCompilerModifiers.AccVisibilityMASK;
		if (inheritedConstructorBinding.isVarargs()) {
			newModifiers |= ClassFileConstants.AccVarargs;
		}
		cd.modifiers = newModifiers;
		cd.isDefaultConstructor = true;

		if (argumentsLength > 0) {
			Argument[] arguments = (cd.arguments = new Argument[argumentsLength]);
			for (int i = argumentsLength; --i >= 0;) {
				arguments[i] = new Argument((baseName + i).toCharArray(), 0L, null /*type ref*/, ClassFileConstants.AccDefault);
			}
		}

		//the super call inside the constructor
		cd.constructorCall = SuperReference.implicitSuperConstructorCall();
		cd.constructorCall.sourceStart = sourceStart;
		cd.constructorCall.sourceEnd = sourceEnd;

		if (argumentsLength > 0) {
			Expression[] args;
			args = cd.constructorCall.arguments = new Expression[argumentsLength];
			for (int i = argumentsLength; --i >= 0;) {
				args[i] = new SingleNameReference((baseName + i).toCharArray(), 0L);
			}
		}

		//adding the constructor in the methods list
		if (methods == null) {
			methods = new AbstractMethodDeclaration[] { cd };
		} else {
			AbstractMethodDeclaration[] newMethods;
			System.arraycopy(
				methods,
				0,
				newMethods = new AbstractMethodDeclaration[methods.length + 1],
				1,
				methods.length);
			newMethods[0] = cd;
			methods = newMethods;
		}

		//============BINDING UPDATE==========================
		cd.binding = new MethodBinding(
				cd.modifiers, //methodDeclaration
				argumentsLength == 0 ? Binding.NO_PARAMETERS : argumentTypes, //arguments bindings
				inheritedConstructorBinding.thrownExceptions, //exceptions
				binding); //declaringClass
				
		cd.scope = new MethodScope(scope, cd, true);
		cd.bindArguments();
		cd.constructorCall.resolve(cd.scope);

		if (binding.methods == null) {
			binding.methods = new MethodBinding[] { cd.binding };
		} else {
			MethodBinding[] newMethods;
			System.arraycopy(
				binding.methods,
				0,
				newMethods = new MethodBinding[binding.methods.length + 1],
				1,
				binding.methods.length);
			newMethods[0] = cd.binding;
			binding.methods = newMethods;
		}
		//===================================================

		return cd.binding;
	}

	/*
	 * Find the matching parse node, answers null if nothing found
	 */
	public FieldDeclaration declarationOf(FieldBinding fieldBinding) {

		if (fieldBinding != null && this.fields != null) {
			for (int i = 0, max = this.fields.length; i < max; i++) {
				FieldDeclaration fieldDecl;
				if ((fieldDecl = this.fields[i]).binding == fieldBinding)
					return fieldDecl;
			}
		}
		return null;
	}

	/*
	 * Find the matching parse node, answers null if nothing found
	 */
	public TypeDeclaration declarationOf(MemberTypeBinding memberTypeBinding) {

		if (memberTypeBinding != null && this.memberTypes != null) {
			for (int i = 0, max = this.memberTypes.length; i < max; i++) {
				TypeDeclaration memberTypeDecl;
				if ((memberTypeDecl = this.memberTypes[i]).binding == memberTypeBinding)
					return memberTypeDecl;
			}
		}
		return null;
	}

	/*
	 * Find the matching parse node, answers null if nothing found
	 */
	public AbstractMethodDeclaration declarationOf(MethodBinding methodBinding) {

		if (methodBinding != null && this.methods != null) {
			for (int i = 0, max = this.methods.length; i < max; i++) {
				AbstractMethodDeclaration methodDecl;

				if ((methodDecl = this.methods[i]).binding == methodBinding)
					return methodDecl;
			}
		}
		return null;
	}

	/*
	 * Finds the matching type amoung this type's member types.
	 * Returns null if no type with this name is found.
	 * The type name is a compound name relative to this type
	 * eg. if this type is X and we're looking for Y.X.A.B
	 *     then a type name would be {X, A, B}
	 */
	public TypeDeclaration declarationOfType(char[][] typeName) {

		int typeNameLength = typeName.length;
		if (typeNameLength < 1 || !CharOperation.equals(typeName[0], this.name)) {
			return null;
		}
		if (typeNameLength == 1) {
			return this;
		}
		char[][] subTypeName = new char[typeNameLength - 1][];
		System.arraycopy(typeName, 1, subTypeName, 0, typeNameLength - 1);
		for (int i = 0; i < this.memberTypes.length; i++) {
			TypeDeclaration typeDecl = this.memberTypes[i].declarationOfType(subTypeName);
			if (typeDecl != null) {
				return typeDecl;
			}
		}
		return null;
	}

	/**
	 * Generic bytecode generation for type
	 */
	public void generateCode(ClassFile enclosingClassFile) {

		if (hasBeenGenerated)
			return;
		hasBeenGenerated = true;
		if (ignoreFurtherInvestigation) {
			if (binding == null)
				return;
			ClassFile.createProblemType(
				this,
				scope.referenceCompilationUnit().compilationResult);
			return;
		}
		try {
			// create the result for a compiled type
			ClassFile classFile = new ClassFile(binding, enclosingClassFile, false);
			// generate all fiels
			classFile.addFieldInfos();

			// record the inner type inside its own .class file to be able
			// to generate inner classes attributes
			if (binding.isMemberType())
				classFile.recordEnclosingTypeAttributes(binding);
			if (binding.isLocalType()) {
				enclosingClassFile.recordNestedLocalAttribute(binding);
				classFile.recordNestedLocalAttribute(binding);
			}
			if (memberTypes != null) {
				for (int i = 0, max = memberTypes.length; i < max; i++) {
					// record the inner type inside its own .class file to be able
					// to generate inner classes attributes
					classFile.recordNestedMemberAttribute(memberTypes[i].binding);
					memberTypes[i].generateCode(scope, classFile);
				}
			}
			// generate all methods
			classFile.setForMethodInfos();
			if (methods != null) {
				for (int i = 0, max = methods.length; i < max; i++) {
					methods[i].generateCode(scope, classFile);
				}
			}
			// generate all synthetic and abstract methods
			classFile.addSpecialMethods();

			if (ignoreFurtherInvestigation) { // trigger problem type generation for code gen errors
				throw new AbortType(scope.referenceCompilationUnit().compilationResult, null);
			}

			// finalize the compiled type result
			classFile.addAttributes();
			scope.referenceCompilationUnit().compilationResult.record(
				binding.constantPoolName(),
				classFile);
		} catch (AbortType e) {
			if (binding == null)
				return;
			ClassFile.createProblemType(
				this,
				scope.referenceCompilationUnit().compilationResult);
		}
	}

	/**
	 * Bytecode generation for a local inner type (API as a normal statement code gen)
	 */
	public void generateCode(BlockScope blockScope, CodeStream codeStream) {

		if ((this.bits & IsReachable) == 0) {
			return;
		}		
		if (hasBeenGenerated) return;
		int pc = codeStream.position;
		if (binding != null) ((NestedTypeBinding) binding).computeSyntheticArgumentSlotSizes();
		generateCode(codeStream.classFile);
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}

	/**
	 * Bytecode generation for a member inner type
	 */
	public void generateCode(ClassScope classScope, ClassFile enclosingClassFile) {

		if (hasBeenGenerated) return;
		if (binding != null) ((NestedTypeBinding) binding).computeSyntheticArgumentSlotSizes();
		generateCode(enclosingClassFile);
	}

	/**
	 * Bytecode generation for a package member
	 */
	public void generateCode(CompilationUnitScope unitScope) {

		generateCode((ClassFile) null);
	}

	public boolean hasErrors() {
		return this.ignoreFurtherInvestigation;
	}

	/**
	 *	Common flow analysis for all types
	 *
	 */
	private void internalAnalyseCode(FlowContext flowContext, FlowInfo flowInfo) {

		if ((this.binding.isPrivate()/* || (this.binding.tagBits & (TagBits.IsAnonymousType|TagBits.IsLocalType)) == TagBits.IsLocalType*/) && !this.binding.isUsed()) {
			if (!scope.referenceCompilationUnit().compilationResult.hasSyntaxError) {
				scope.problemReporter().unusedPrivateType(this);
			}
		}

		InitializationFlowContext initializerContext = new InitializationFlowContext(null, this, initializerScope);
		InitializationFlowContext staticInitializerContext = new InitializationFlowContext(null, this, staticInitializerScope);
		FlowInfo nonStaticFieldInfo = flowInfo.unconditionalFieldLessCopy();
		FlowInfo staticFieldInfo = flowInfo.unconditionalFieldLessCopy();
		if (fields != null) {
			for (int i = 0, count = fields.length; i < count; i++) {
				FieldDeclaration field = fields[i];
				if (field.isStatic()) {
					if ((staticFieldInfo.tagBits & FlowInfo.UNREACHABLE) != 0)
						field.bits &= ~ASTNode.IsReachable;
					
					/*if (field.isField()){
						staticInitializerContext.handledExceptions = NoExceptions; // no exception is allowed jls8.3.2
					} else {*/
					staticInitializerContext.handledExceptions = Binding.ANY_EXCEPTION; // tolerate them all, and record them
					/*}*/
					staticFieldInfo =
						field.analyseCode(
							staticInitializerScope,
							staticInitializerContext,
							staticFieldInfo);
					// in case the initializer is not reachable, use a reinitialized flowInfo and enter a fake reachable
					// branch, since the previous initializer already got the blame.
					if (staticFieldInfo == FlowInfo.DEAD_END) {
						staticInitializerScope.problemReporter().initializerMustCompleteNormally(field);
						staticFieldInfo = FlowInfo.initial(maxFieldCount).setReachMode(FlowInfo.UNREACHABLE);
					}
				} else {
					if ((nonStaticFieldInfo.tagBits & FlowInfo.UNREACHABLE) != 0)
						field.bits &= ~ASTNode.IsReachable;
					
					/*if (field.isField()){
						initializerContext.handledExceptions = NoExceptions; // no exception is allowed jls8.3.2
					} else {*/
						initializerContext.handledExceptions = Binding.ANY_EXCEPTION; // tolerate them all, and record them
					/*}*/
					nonStaticFieldInfo =
						field.analyseCode(initializerScope, initializerContext, nonStaticFieldInfo);
					// in case the initializer is not reachable, use a reinitialized flowInfo and enter a fake reachable
					// branch, since the previous initializer already got the blame.
					if (nonStaticFieldInfo == FlowInfo.DEAD_END) {
						initializerScope.problemReporter().initializerMustCompleteNormally(field);
						nonStaticFieldInfo = FlowInfo.initial(maxFieldCount).setReachMode(FlowInfo.UNREACHABLE);
					} 
				}
			}
		}
		if (memberTypes != null) {
			for (int i = 0, count = memberTypes.length; i < count; i++) {
				if (flowContext != null){ // local type
					memberTypes[i].analyseCode(scope, flowContext, nonStaticFieldInfo.copy().setReachMode(flowInfo.reachMode())); // reset reach mode in case initializers did abrupt completely
				} else {
					memberTypes[i].analyseCode(scope);
				}
			}
		}
		if (methods != null) {
			UnconditionalFlowInfo outerInfo = flowInfo.unconditionalFieldLessCopy();
			FlowInfo constructorInfo = nonStaticFieldInfo.unconditionalInits().discardNonFieldInitializations().addInitializationsFrom(outerInfo);
			for (int i = 0, count = methods.length; i < count; i++) {
				AbstractMethodDeclaration method = methods[i];
				if (method.ignoreFurtherInvestigation)
					continue;
				if (method.isInitializationMethod()) {
					if (method.isStatic()) { // <clinit>
						method.analyseCode(
							scope, 
							staticInitializerContext,  
							staticFieldInfo.unconditionalInits().discardNonFieldInitializations().addInitializationsFrom(outerInfo).setReachMode(flowInfo.reachMode()));  // reset reach mode in case initializers did abrupt completely
					} else { // constructor
						method.analyseCode(scope, initializerContext, constructorInfo.copy().setReachMode(flowInfo.reachMode())); // reset reach mode in case initializers did abrupt completely
					}
				} else { // regular method
					method.analyseCode(scope, null, flowInfo.copy());
				}
			}
		}
		// enable enum support ?
		if (this.binding.isEnum()) {
			this.enumValuesSyntheticfield = this.binding.addSyntheticFieldForEnumValues();
		}
	}

	public final static int kind(int flags) {
		switch (flags & (ClassFileConstants.AccInterface|ClassFileConstants.AccAnnotation|ClassFileConstants.AccEnum)) {
			case ClassFileConstants.AccInterface :
				return TypeDeclaration.INTERFACE_DECL;
			case ClassFileConstants.AccInterface|ClassFileConstants.AccAnnotation :
				return TypeDeclaration.ANNOTATION_TYPE_DECL;
			case ClassFileConstants.AccEnum :
				return TypeDeclaration.ENUM_DECL;
			default : 
				return TypeDeclaration.CLASS_DECL;
		}		
	}

	/* 
	 * Access emulation for a local type
	 * force to emulation of access to direct enclosing instance.
	 * By using the initializer scope, we actually only request an argument emulation, the
	 * field is not added until actually used. However we will force allocations to be qualified
	 * with an enclosing instance.
	 * 15.9.2
	 */
	public void manageEnclosingInstanceAccessIfNecessary(BlockScope currentScope, FlowInfo flowInfo) {

 		if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) != 0) return;
		NestedTypeBinding nestedType = (NestedTypeBinding) binding;
		
		MethodScope methodScope = currentScope.methodScope();
		if (!methodScope.isStatic && !methodScope.isConstructorCall){
			nestedType.addSyntheticArgumentAndField(nestedType.enclosingType());	
		}
		// add superclass enclosing instance arg for anonymous types (if necessary)
		if (nestedType.isAnonymousType()) {
			ReferenceBinding superclassBinding = (ReferenceBinding)nestedType.superclass.erasure();
			if (superclassBinding.enclosingType() != null && !superclassBinding.isStatic()) {
				if (!superclassBinding.isLocalType()
 ((NestedTypeBinding)superclassBinding).getSyntheticField(superclassBinding.enclosingType(), true) != null){

					nestedType.addSyntheticArgument(superclassBinding.enclosingType());	
				}
			}
			// From 1.5 on, provide access to enclosing instance synthetic constructor argument when declared inside constructor call
			// only for direct anonymous type
			//public class X {
			//	void foo() {}
			//	class M {
			//		M(Object o) {}
			//		M() { this(new Object() { void baz() { foo(); }}); } // access to #foo() indirects through constructor synthetic arg: val$this$0
			//	}
			//}
			if (!methodScope.isStatic && methodScope.isConstructorCall && currentScope.compilerOptions().complianceLevel >= ClassFileConstants.JDK1_5) {
				ReferenceBinding enclosing = nestedType.enclosingType();
				if (enclosing.isNestedType()) {
					NestedTypeBinding nestedEnclosing = (NestedTypeBinding)enclosing;
//					if (nestedEnclosing.findSuperTypeErasingTo(nestedEnclosing.enclosingType()) == null) { // only if not inheriting
						SyntheticArgumentBinding syntheticEnclosingInstanceArgument = nestedEnclosing.getSyntheticArgument(nestedEnclosing.enclosingType(), true);
						if (syntheticEnclosingInstanceArgument != null) {
							nestedType.addSyntheticArgumentAndField(syntheticEnclosingInstanceArgument);	
						}
					}
//				}
			}
		}
	}
	
	/* 
	 * Access emulation for a local member type
	 * force to emulation of access to direct enclosing instance.
	 * By using the initializer scope, we actually only request an argument emulation, the
	 * field is not added until actually used. However we will force allocations to be qualified
	 * with an enclosing instance.
	 * 
	 * Local member cannot be static.
	 */
	public void manageEnclosingInstanceAccessIfNecessary(ClassScope currentScope, FlowInfo flowInfo) {

		if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) == 0) {
		NestedTypeBinding nestedType = (NestedTypeBinding) binding;
		nestedType.addSyntheticArgumentAndField(binding.enclosingType());
		}
	}	
	
	/**
	 * A <clinit> will be requested as soon as static fields or assertions are present. It will be eliminated during
	 * classfile creation if no bytecode was actually produced based on some optimizations/compiler settings.
	 */
	public final boolean needClassInitMethod() {

		// always need a <clinit> when assertions are present
		if ((this.bits & ContainsAssertion) != 0)
			return true;
		
		switch (kind(this.modifiers)) {
			case TypeDeclaration.INTERFACE_DECL:
			case TypeDeclaration.ANNOTATION_TYPE_DECL:
				return this.fields != null; // fields are implicitly statics
			case TypeDeclaration.ENUM_DECL:
				return true; // even if no enum constants, need to set $VALUES array
		}
		if (this.fields != null) {
			for (int i = this.fields.length; --i >= 0;) {
				FieldDeclaration field = this.fields[i];
				//need to test the modifier directly while there is no binding yet
				if ((field.modifiers & ClassFileConstants.AccStatic) != 0)
					return true; // TODO (philippe) shouldn't it check whether field is initializer or has some initial value ?
			}
		}
		return false;
	}

	public void parseMethod(Parser parser, CompilationUnitDeclaration unit) {

		//connect method bodies
		if (unit.ignoreMethodBodies)
			return;

		//members
		if (memberTypes != null) {
			int length = memberTypes.length;
			for (int i = 0; i < length; i++)
				memberTypes[i].parseMethod(parser, unit);
		}

		//methods
		if (methods != null) {
			int length = methods.length;
			for (int i = 0; i < length; i++) {
				methods[i].parseStatements(parser, unit);
			}
		}

		//initializers
		if (fields != null) {
			int length = fields.length;
			for (int i = 0; i < length; i++) {
				final FieldDeclaration fieldDeclaration = fields[i];
				switch(fieldDeclaration.getKind()) {
					case AbstractVariableDeclaration.INITIALIZER:
						((Initializer) fieldDeclaration).parseStatements(parser, this, unit);
						break;
				}
			}
		}
	}

	public StringBuffer print(int indent, StringBuffer output) {

		if (this.javadoc != null) {
			this.javadoc.print(indent, output);
		}
		if ((this.bits & IsAnonymousType) == 0) {
			printIndent(indent, output);
			printHeader(0, output);
		}
		return printBody(indent, output);
	}

	public StringBuffer printBody(int indent, StringBuffer output) {

		output.append(" {"); //$NON-NLS-1$
		if (memberTypes != null) {
			for (int i = 0; i < memberTypes.length; i++) {
				if (memberTypes[i] != null) {
					output.append('\n');
					memberTypes[i].print(indent + 1, output);
				}
			}
		}
		if (fields != null) {
			for (int fieldI = 0; fieldI < fields.length; fieldI++) {
				if (fields[fieldI] != null) {
					output.append('\n');
					fields[fieldI].print(indent + 1, output);
				}
			}
		}
		if (methods != null) {
			for (int i = 0; i < methods.length; i++) {
				if (methods[i] != null) {
					output.append('\n');
					methods[i].print(indent + 1, output); 
				}
			}
		}
		output.append('\n');
		return printIndent(indent, output).append('}');
	}

	public StringBuffer printHeader(int indent, StringBuffer output) {

		printModifiers(this.modifiers, output);
		if (this.annotations != null) printAnnotations(this.annotations, output);
		
		switch (kind(this.modifiers)) {
			case TypeDeclaration.CLASS_DECL :
				output.append("class "); //$NON-NLS-1$
				break;
			case TypeDeclaration.INTERFACE_DECL :
				output.append("interface "); //$NON-NLS-1$
				break;
			case TypeDeclaration.ENUM_DECL :
				output.append("enum "); //$NON-NLS-1$
				break;
			case TypeDeclaration.ANNOTATION_TYPE_DECL :
				output.append("@interface "); //$NON-NLS-1$
				break;
		}			
		output.append(name);
		if (typeParameters != null) {
			output.append("<");//$NON-NLS-1$
			for (int i = 0; i < typeParameters.length; i++) {
				if (i > 0) output.append( ", "); //$NON-NLS-1$
				typeParameters[i].print(0, output);
			}
			output.append(">");//$NON-NLS-1$
		}
		if (superclass != null) {
			output.append(" extends ");  //$NON-NLS-1$
			superclass.print(0, output);
		}
		if (superInterfaces != null && superInterfaces.length > 0) {
			switch (kind(this.modifiers)) {
				case TypeDeclaration.CLASS_DECL :
				case TypeDeclaration.ENUM_DECL :
					output.append(" implements "); //$NON-NLS-1$
					break;
				case TypeDeclaration.INTERFACE_DECL :
				case TypeDeclaration.ANNOTATION_TYPE_DECL :
					output.append(" extends "); //$NON-NLS-1$
					break;
			}			
			for (int i = 0; i < superInterfaces.length; i++) {
				if (i > 0) output.append( ", "); //$NON-NLS-1$
				superInterfaces[i].print(0, output);
			}
		}
		return output;
	}

	public StringBuffer printStatement(int tab, StringBuffer output) {
		return print(tab, output);
	}
	


	public void resolve() {
		SourceTypeBinding sourceType = this.binding;
		if (sourceType == null) {
			this.ignoreFurtherInvestigation = true;
			return;
		}
		try {
			boolean old = this.staticInitializerScope.insideTypeAnnotation;
			try {
				this.staticInitializerScope.insideTypeAnnotation = true;
				resolveAnnotations(this.staticInitializerScope, this.annotations, sourceType);
			} finally {
				this.staticInitializerScope.insideTypeAnnotation = old;
			}
			// check @Deprecated annotation
			if ((sourceType.getAnnotationTagBits() & TagBits.AnnotationDeprecated) == 0
					&& (sourceType.modifiers & ClassFileConstants.AccDeprecated) != 0 
					&& scope.compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5) {
				scope.problemReporter().missingDeprecatedAnnotationForType(this);
			}			
			if ((this.bits & UndocumentedEmptyBlock) != 0) {
				this.scope.problemReporter().undocumentedEmptyBlock(this.bodyStart-1, this.bodyEnd);
			}
			boolean needSerialVersion = 
							this.scope.compilerOptions().getSeverity(CompilerOptions.MissingSerialVersion) != ProblemSeverities.Ignore
							&& sourceType.isClass() 
							&& !sourceType.isAbstract() 
							&& sourceType.findSuperTypeErasingTo(T_JavaIoSerializable, false /*Serializable is not a class*/) != null;
			
			if (this.typeParameters != null && scope.getJavaLangThrowable().isSuperclassOf(sourceType)) {
				this.scope.problemReporter().genericTypeCannotExtendThrowable(this);
			}
			this.maxFieldCount = 0;
			int lastVisibleFieldID = -1;
			boolean hasEnumConstants = false;
			boolean hasEnumConstantsWithoutBody = false;
			
			if (this.typeParameters != null) {
				for (int i = 0, count = this.typeParameters.length; i < count; i++) {
					this.typeParameters[i].resolve(this.scope);
				}
			}
			if (this.memberTypes != null) {
				for (int i = 0, count = this.memberTypes.length; i < count; i++) {
					this.memberTypes[i].resolve(this.scope);
				}
			}
			if (this.fields != null) {
				for (int i = 0, count = this.fields.length; i < count; i++) {
					FieldDeclaration field = this.fields[i];
					switch(field.getKind()) {
						case AbstractVariableDeclaration.ENUM_CONSTANT:
							hasEnumConstants = true;
							if (!(field.initialization instanceof QualifiedAllocationExpression))
								hasEnumConstantsWithoutBody = true;
						case AbstractVariableDeclaration.FIELD:
							FieldBinding fieldBinding = field.binding;
							if (fieldBinding == null) {
								// still discover secondary errors
								if (field.initialization != null) field.initialization.resolve(field.isStatic() ? this.staticInitializerScope : this.initializerScope);
								this.ignoreFurtherInvestigation = true;
								continue;
							}
							if (needSerialVersion
									&& ((fieldBinding.modifiers & (ClassFileConstants.AccStatic | ClassFileConstants.AccFinal)) == (ClassFileConstants.AccStatic | ClassFileConstants.AccFinal))
									&& CharOperation.equals(TypeConstants.SERIALVERSIONUID, fieldBinding.name)
									&& TypeBinding.LONG == fieldBinding.type) {
								needSerialVersion = false;
							}
							this.maxFieldCount++;
							lastVisibleFieldID = field.binding.id;
							break;
	
						case AbstractVariableDeclaration.INITIALIZER:
							 ((Initializer) field).lastVisibleFieldID = lastVisibleFieldID + 1;
							break;
					}
					field.resolve(field.isStatic() ? this.staticInitializerScope : this.initializerScope);
				}
			}
			if (needSerialVersion) {
				this.scope.problemReporter().missingSerialVersion(this);
			}
			// check extends/implements for annotation type
			switch(kind(this.modifiers)) {
				case TypeDeclaration.ANNOTATION_TYPE_DECL :
					if (this.superclass != null) {
						this.scope.problemReporter().annotationTypeDeclarationCannotHaveSuperclass(this);
					}
					if (this.superInterfaces != null) {
						this.scope.problemReporter().annotationTypeDeclarationCannotHaveSuperinterfaces(this);
					}		
					break;
				case TypeDeclaration.ENUM_DECL :
					// check enum abstract methods
					if (this.binding.isAbstract()) {
						if (!hasEnumConstants || hasEnumConstantsWithoutBody) {
							for (int i = 0, count = this.methods.length; i < count; i++) {
								final AbstractMethodDeclaration methodDeclaration = this.methods[i];
								if (methodDeclaration.isAbstract() && methodDeclaration.binding != null) {
									this.scope.problemReporter().enumAbstractMethodMustBeImplemented(methodDeclaration);
								}
							}
						}
					}
					break;
			}
			
			int missingAbstractMethodslength = this.missingAbstractMethods == null ? 0 : this.missingAbstractMethods.length;
			int methodsLength = this.methods == null ? 0 : this.methods.length;
			if ((methodsLength + missingAbstractMethodslength) > 0xFFFF) {
				this.scope.problemReporter().tooManyMethods(this);
			}
			
			if (this.methods != null) {
				for (int i = 0, count = this.methods.length; i < count; i++) {
					this.methods[i].resolve(this.scope);
				}
			}
			// Resolve javadoc
			if (this.javadoc != null) {
				if (this.scope != null) {
					this.javadoc.resolve(this.scope);
				}
			} else if (sourceType != null && !sourceType.isLocalType()) {
				this.scope.problemReporter().javadocMissing(this.sourceStart, this.sourceEnd, sourceType.modifiers);
			}
			
		} catch (AbortType e) {
			this.ignoreFurtherInvestigation = true;
			return;
		}
	}

	public void resolve(BlockScope blockScope) {
		// local type declaration

		// need to build its scope first and proceed with binding's creation
		if ((this.bits & IsAnonymousType) == 0) blockScope.addLocalType(this);

		if (binding != null) {
			// remember local types binding for innerclass emulation propagation
			blockScope.referenceCompilationUnit().record((LocalTypeBinding)binding);

			// binding is not set if the receiver could not be created
			resolve();
			updateMaxFieldCount();
		}
	}
	
	public void resolve(ClassScope upperScope) {
		// member scopes are already created
		// request the construction of a binding if local member type

		if (binding != null && binding instanceof LocalTypeBinding) {
			// remember local types binding for innerclass emulation propagation
			upperScope.referenceCompilationUnit().record((LocalTypeBinding)binding);
		}
		resolve();
		updateMaxFieldCount();
	}

	public void resolve(CompilationUnitScope upperScope) {
		// top level : scope are already created

		resolve();
		updateMaxFieldCount();
	}

	public void tagAsHavingErrors() {
		ignoreFurtherInvestigation = true;
	}


	/**
	 *	Iteration for a package member type
	 *
	 */
	public void traverse(
		ASTVisitor visitor,
		CompilationUnitScope unitScope) {

		if (ignoreFurtherInvestigation)
			return;
		try {
			if (visitor.visit(this, unitScope)) {
				if (this.annotations != null) {
					int annotationsLength = this.annotations.length;
					for (int i = 0; i < annotationsLength; i++)
						this.annotations[i].traverse(visitor, scope);
				}
				if (this.superclass != null)
					this.superclass.traverse(visitor, scope);
				if (this.superInterfaces != null) {
					int length = this.superInterfaces.length;
					for (int i = 0; i < length; i++)
						this.superInterfaces[i].traverse(visitor, scope);
				}
				if (this.typeParameters != null) {
					int length = this.typeParameters.length;
					for (int i = 0; i < length; i++) {
						this.typeParameters[i].traverse(visitor, scope);
					}
				}				
				if (this.memberTypes != null) {
					int length = this.memberTypes.length;
					for (int i = 0; i < length; i++)
						this.memberTypes[i].traverse(visitor, scope);
				}
				if (this.fields != null) {
					int length = this.fields.length;
					for (int i = 0; i < length; i++) {
						FieldDeclaration field;
						if ((field = this.fields[i]).isStatic()) {
							field.traverse(visitor, staticInitializerScope);
						} else {
							field.traverse(visitor, initializerScope);
						}
					}
				}
				if (this.methods != null) {
					int length = this.methods.length;
					for (int i = 0; i < length; i++)
						this.methods[i].traverse(visitor, scope);
				}
			}
			visitor.endVisit(this, unitScope);
		} catch (AbortType e) {
			// silent abort
		}
	}

	/**
	 *	Iteration for a local innertype
	 *
	 */
	public void traverse(ASTVisitor visitor, BlockScope blockScope) {
		if (ignoreFurtherInvestigation)
			return;
		try {
			if (visitor.visit(this, blockScope)) {
				if (this.annotations != null) {
					int annotationsLength = this.annotations.length;
					for (int i = 0; i < annotationsLength; i++)
						this.annotations[i].traverse(visitor, scope);
				}
				if (this.superclass != null)
					this.superclass.traverse(visitor, scope);
				if (this.superInterfaces != null) {
					int length = this.superInterfaces.length;
					for (int i = 0; i < length; i++)
						this.superInterfaces[i].traverse(visitor, scope);
				}
				if (this.typeParameters != null) {
					int length = this.typeParameters.length;
					for (int i = 0; i < length; i++) {
						this.typeParameters[i].traverse(visitor, scope);
					}
				}				
				if (this.memberTypes != null) {
					int length = this.memberTypes.length;
					for (int i = 0; i < length; i++)
						this.memberTypes[i].traverse(visitor, scope);
				}
				if (this.fields != null) {
					int length = this.fields.length;
					for (int i = 0; i < length; i++) {
						FieldDeclaration field;
						if ((field = this.fields[i]).isStatic()) {
							// local type cannot have static fields
						} else {
							field.traverse(visitor, initializerScope);
						}
					}
				}
				if (this.methods != null) {
					int length = this.methods.length;
					for (int i = 0; i < length; i++)
						this.methods[i].traverse(visitor, scope);
				}
			}
			visitor.endVisit(this, blockScope);
		} catch (AbortType e) {
			// silent abort
		}
	}

	/**
	 *	Iteration for a member innertype
	 *
	 */
	public void traverse(ASTVisitor visitor, ClassScope classScope) {
		if (ignoreFurtherInvestigation)
			return;
		try {
			if (visitor.visit(this, classScope)) {
				if (this.annotations != null) {
					int annotationsLength = this.annotations.length;
					for (int i = 0; i < annotationsLength; i++)
						this.annotations[i].traverse(visitor, scope);
				}
				if (this.superclass != null)
					this.superclass.traverse(visitor, scope);
				if (this.superInterfaces != null) {
					int length = this.superInterfaces.length;
					for (int i = 0; i < length; i++)
						this.superInterfaces[i].traverse(visitor, scope);
				}
				if (this.typeParameters != null) {
					int length = this.typeParameters.length;
					for (int i = 0; i < length; i++) {
						this.typeParameters[i].traverse(visitor, scope);
					}
				}				
				if (this.memberTypes != null) {
					int length = this.memberTypes.length;
					for (int i = 0; i < length; i++)
						this.memberTypes[i].traverse(visitor, scope);
				}
				if (this.fields != null) {
					int length = this.fields.length;
					for (int i = 0; i < length; i++) {
						FieldDeclaration field;
						if ((field = this.fields[i]).isStatic()) {
							field.traverse(visitor, staticInitializerScope);
						} else {
							field.traverse(visitor, initializerScope);
						}
					}
				}
				if (this.methods != null) {
					int length = this.methods.length;
					for (int i = 0; i < length; i++)
						this.methods[i].traverse(visitor, scope);
				}
			}
			visitor.endVisit(this, classScope);
		} catch (AbortType e) {
			// silent abort
		}
	}	

	/**
	 * MaxFieldCount's computation is necessary so as to reserve space for
	 * the flow info field portions. It corresponds to the maximum amount of
	 * fields this class or one of its innertypes have.
	 *
	 * During name resolution, types are traversed, and the max field count is recorded
	 * on the outermost type. It is then propagated down during the flow analysis.
	 *
	 * This method is doing either up/down propagation.
	 */
	void updateMaxFieldCount() {

		if (binding == null)
			return; // error scenario
		TypeDeclaration outerMostType = scope.outerMostClassScope().referenceType();
		if (maxFieldCount > outerMostType.maxFieldCount) {
			outerMostType.maxFieldCount = maxFieldCount; // up
		} else {
			maxFieldCount = outerMostType.maxFieldCount; // down
		}
	}	

	/**
	 * Returns whether the type is a secondary one or not.
	 */
	public boolean isSecondary() {
		return (this.bits & IsSecondaryType) != 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6778.java