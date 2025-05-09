error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9990.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9990.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9990.java
text:
```scala
i@@f (staticFieldInfo == FlowInfo.DEAD_END) {

/*******************************************************************************
 * Copyright (c) 2000, 2001, 2002 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.parser.*;
import org.eclipse.jdt.internal.compiler.problem.*;

public class TypeDeclaration
	extends Statement
	implements ProblemSeverities, ReferenceContext {

	public int modifiers;
	public int modifiersSourceStart;
	public char[] name;
	public TypeReference superclass;
	public TypeReference[] superInterfaces;
	public FieldDeclaration[] fields;
	public AbstractMethodDeclaration[] methods;
	public MemberTypeDeclaration[] memberTypes;
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
	private MethodDeclaration[] missingAbstractMethods;

	public TypeDeclaration(CompilationResult compilationResult){
		this.compilationResult = compilationResult;
	}
		
	/*
	 *	We cause the compilation task to abort to a given extent.
	 */
	public void abort(int abortLevel) {

		if (scope == null) {
			throw new AbortCompilation(); // cannot do better
		}

		CompilationResult compilationResult =
			scope.referenceCompilationUnit().compilationResult;

		switch (abortLevel) {
			case AbortCompilation :
				throw new AbortCompilation(compilationResult);
			case AbortCompilationUnit :
				throw new AbortCompilationUnit(compilationResult);
			case AbortMethod :
				throw new AbortMethod(compilationResult);
			default :
				throw new AbortType(compilationResult);
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
			AbstractMethodDeclaration[] methods;
			if ((methods = this.methods) == null) {
				length = 0;
				methods = new AbstractMethodDeclaration[1];
			} else {
				length = methods.length;
				System.arraycopy(
					methods,
					0,
					(methods = new AbstractMethodDeclaration[length + 1]),
					1,
					length);
			}
			Clinit clinit = new Clinit(this.compilationResult);
			methods[0] = clinit;
			// clinit is added in first location, so as to minimize the use of ldcw (big consumer of constant inits)
			clinit.declarationSourceStart = clinit.sourceStart = sourceStart;
			clinit.declarationSourceEnd = clinit.sourceEnd = sourceEnd;
			clinit.bodyEnd = sourceEnd;
			this.methods = methods;
		}
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
			// remember local types binding for innerclass emulation propagation
			currentScope.referenceCompilationUnit().record((LocalTypeBinding) binding);

			ReferenceBinding[] defaultHandledExceptions = new ReferenceBinding[] { scope.getJavaLangThrowable()}; // tolerate any kind of exception
			InitializationFlowContext initializerContext =
				new InitializationFlowContext(null, this, initializerScope);
			
			updateMaxFieldCount(); // propagate down the max field count
			FlowInfo fieldInfo = flowInfo.copy(); // so as not to propagate changes outside this type
			if (fields != null) {
				for (int i = 0, count = fields.length; i < count; i++) {
					FieldDeclaration field = fields[i];
					if (field.isField() && !this.binding.isAnonymousType()){
						initializerContext.handledExceptions = NoExceptions; // no exception is allowed jls8.3.2, except for anonymous
					} else {
						initializerContext.handledExceptions = defaultHandledExceptions; // tolerate them all, and record them
					}
					fieldInfo = field.analyseCode(initializerScope, initializerContext, fieldInfo);
					if (fieldInfo == FlowInfo.DEAD_END) {
						// in case the initializer is not reachable, use a reinitialized flowInfo and enter a fake reachable
						// branch, since the previous initializer already got the blame.
						initializerScope.problemReporter().initializerMustCompleteNormally(field);
						fieldInfo = FlowInfo.initial(maxFieldCount).setReachMode(FlowInfo.UNREACHABLE);
					}
				}
			}
			if (memberTypes != null) {
				for (int i = 0, count = memberTypes.length; i < count; i++) {
					memberTypes[i].analyseCode(scope, flowContext, fieldInfo.copy());
				}
			}
			if (methods != null) {
				for (int i = 0, count = methods.length; i < count; i++) {
					AbstractMethodDeclaration method = methods[i];
					if (method.ignoreFurtherInvestigation)
						continue;
					if (method.isConstructor()) { // constructor
						((ConstructorDeclaration)method).analyseCode(scope, initializerContext, fieldInfo.copy());
					} else { // regular method
						method.analyseCode(scope, null, flowInfo.copy());
					}
				}
			}
		} catch (AbortType e) {
			this.ignoreFurtherInvestigation = true;
		}
		return flowInfo;
	}

	/**
	 *	Flow analysis for a member innertype
	 *
	 */
	public void analyseCode(ClassScope classScope1) {

		if (ignoreFurtherInvestigation)
			return;
		try {
			// propagate down the max field count
			updateMaxFieldCount();
			FlowInfo flowInfo = FlowInfo.initial(maxFieldCount); // start fresh init info

			ReferenceBinding[] defaultHandledExceptions = new ReferenceBinding[] { scope.getJavaLangThrowable()}; // tolerate any kind of exception
			InitializationFlowContext initializerContext =
				new InitializationFlowContext(null, this, initializerScope);
			InitializationFlowContext staticInitializerContext =
				new InitializationFlowContext(null, this, staticInitializerScope);
			FlowInfo nonStaticFieldInfo = flowInfo.copy(),
				staticFieldInfo = flowInfo.copy();
			if (fields != null) {
				for (int i = 0, count = fields.length; i < count; i++) {
					FieldDeclaration field = fields[i];
					if (field.isStatic()) {
						if (field.isField()){
							staticInitializerContext.handledExceptions = NoExceptions; // no exception is allowed jls8.3.2
						} else {
							staticInitializerContext.handledExceptions = defaultHandledExceptions; // tolerate them all, and record them
						}
						staticFieldInfo =
							field.analyseCode(
								staticInitializerScope,
								staticInitializerContext,
								staticFieldInfo);
						// in case the initializer is not reachable, use a reinitialized flowInfo and enter a fake reachable
						// branch, since the previous initializer already got the blame.
						if (staticFieldInfo == FlowInfo.DEAD_END) { // TODO: could move behavior onto flowInfo
							staticInitializerScope.problemReporter().initializerMustCompleteNormally(field);
							staticFieldInfo = FlowInfo.initial(maxFieldCount).setReachMode(FlowInfo.UNREACHABLE);
						}
					} else {
						if (field.isField()){
							initializerContext.handledExceptions = NoExceptions; // no exception is allowed jls8.3.2
						} else {
							initializerContext.handledExceptions = defaultHandledExceptions; // tolerate them all, and record them
						}
						nonStaticFieldInfo = field.analyseCode(initializerScope, initializerContext, nonStaticFieldInfo);
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
					memberTypes[i].analyseCode(scope);
				}
			}
			if (methods != null) {
				for (int i = 0, count = methods.length; i < count; i++) {
					AbstractMethodDeclaration method = methods[i];
					if (method.ignoreFurtherInvestigation)
						continue;
					if (method.isInitializationMethod()) {
						if (method.isStatic()) { // <clinit>
							((Clinit)method).analyseCode(scope, staticInitializerContext, staticFieldInfo);
						} else { // constructor
							((ConstructorDeclaration)method).analyseCode(scope, initializerContext, nonStaticFieldInfo.copy());
						}
					} else { // regular method
						method.analyseCode(scope, null, FlowInfo.initial(maxFieldCount));
					}
				}
			}
		} catch (AbortType e) {
			this.ignoreFurtherInvestigation = true;
		};
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
			// remember local types binding for innerclass emulation propagation
			currentScope.referenceCompilationUnit().record((LocalTypeBinding) binding);

			/* force to emulation of access to direct enclosing instance: only for local members.
			 * By using the initializer scope, we actually only request an argument emulation, the
			 * field is not added until actually used. However we will force allocations to be qualified
			 * with an enclosing instance.
			 */
			initializerScope.emulateOuterAccess(
				(SourceTypeBinding) binding.enclosingType(),
				false);

			ReferenceBinding[] defaultHandledExceptions = new ReferenceBinding[] { scope.getJavaLangThrowable()}; // tolerate any kind of exception
			InitializationFlowContext initializerContext =
				new InitializationFlowContext(null, this, initializerScope);

			updateMaxFieldCount(); // propagate down the max field count
			FlowInfo fieldInfo = flowInfo.copy();// so as not to propagate changes outside this type
			if (fields != null) {
				for (int i = 0, count = fields.length; i < count; i++) {
					FieldDeclaration field = fields[i];
					if (field.isField()){
						initializerContext.handledExceptions = NoExceptions; // no exception is allowed jls8.3.2
					} else {
						initializerContext.handledExceptions = defaultHandledExceptions; // tolerate them all, and record them
					}
					if (!fields[i].isStatic()) {
						fieldInfo =
							field.analyseCode(initializerScope, initializerContext, fieldInfo);
						if (fieldInfo == FlowInfo.DEAD_END) {
							// in case the initializer is not reachable, use a reinitialized flowInfo and enter a fake reachable
							// branch, since the previous initializer already got the blame.
							initializerScope.problemReporter().initializerMustCompleteNormally(field);
							fieldInfo = FlowInfo.initial(maxFieldCount).setReachMode(FlowInfo.UNREACHABLE);
						}
					}
				}
			}
			if (memberTypes != null) {
				for (int i = 0, count = memberTypes.length; i < count; i++) {
					memberTypes[i].analyseCode(scope, flowContext, fieldInfo.copy());
				}
			}
			if (methods != null) {
				for (int i = 0, count = methods.length; i < count; i++) {
					AbstractMethodDeclaration method = methods[i];
					if (method.ignoreFurtherInvestigation)
						continue;
					if (method.isConstructor()) { // constructor
						((ConstructorDeclaration)method).analyseCode(scope, initializerContext, fieldInfo.copy());
					} else { // regular method
						method.analyseCode(scope, null, flowInfo.copy());
					}
				}
			}
		} catch (AbortType e) {
			this.ignoreFurtherInvestigation = true;
		};
	}

	/**
	 *	Flow analysis for a package member type
	 *
	 */
	public void analyseCode(CompilationUnitScope unitScope) {

		if (ignoreFurtherInvestigation)
			return;
		try {
			FlowInfo flowInfo = FlowInfo.initial(maxFieldCount); // start fresh init info
			ReferenceBinding[] defaultHandledExceptions = new ReferenceBinding[] { scope.getJavaLangThrowable()}; // tolerate any kind of exception
			InitializationFlowContext initializerContext =
				new InitializationFlowContext(null, this, initializerScope);
			InitializationFlowContext staticInitializerContext =
				new InitializationFlowContext(null, this, staticInitializerScope);
			FlowInfo nonStaticFieldInfo = flowInfo.copy(),
				staticFieldInfo = flowInfo.copy();
			if (fields != null) {
				for (int i = 0, count = fields.length; i < count; i++) {
					FieldDeclaration field = fields[i];
					if (field.isStatic()) {
						if (field.isField()){
							staticInitializerContext.handledExceptions = NoExceptions; // no exception is allowed jls8.3.2
						} else {
							staticInitializerContext.handledExceptions = defaultHandledExceptions; // tolerate them all, and record them
						}
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
						if (field.isField()){
							initializerContext.handledExceptions = NoExceptions; // no exception is allowed jls8.3.2
						} else {
							initializerContext.handledExceptions = defaultHandledExceptions; // tolerate them all, and record them
						}
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
					memberTypes[i].analyseCode(scope);
				}
			}
			if (methods != null) {
				for (int i = 0, count = methods.length; i < count; i++) {
					AbstractMethodDeclaration method = methods[i];
					if (method.ignoreFurtherInvestigation)
						continue;
					if (method.isInitializationMethod()) {
						if (method.isStatic()) { // <clinit>
							((Clinit)method).analyseCode(scope, staticInitializerContext, staticFieldInfo);
						} else { // constructor
							((ConstructorDeclaration)method).analyseCode(scope, initializerContext, nonStaticFieldInfo.copy());
						}
					} else { // regular method
						method.analyseCode(scope, null, FlowInfo.initial(maxFieldCount));
					}
				}
			}
		} catch (AbortType e) {
			this.ignoreFurtherInvestigation = true;
		};
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
						if ((c.constructorCall == null)
 (c.constructorCall.isImplicitSuper())) { //changed to a method
							MethodDeclaration m = new MethodDeclaration(this.compilationResult);
							m.sourceStart = c.sourceStart;
							m.sourceEnd = c.sourceEnd;
							m.bodyStart = c.bodyStart;
							m.bodyEnd = c.bodyEnd;
							m.declarationSourceEnd = c.declarationSourceEnd;
							m.declarationSourceStart = c.declarationSourceStart;
							m.selector = c.selector;
							m.statements = c.statements;
							m.modifiers = c.modifiers;
							m.arguments = c.arguments;
							m.thrownExceptions = c.thrownExceptions;
							m.explicitDeclarations = c.explicitDeclarations;
							m.returnType = null;
							methods[i] = m;
						}
					} else {
						if (this.isInterface()) {
							// report the problem and continue the parsing
							parser.problemReporter().interfaceCannotHaveConstructors(
								(ConstructorDeclaration) am);
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

	public ConstructorDeclaration createsInternalConstructor(
		boolean needExplicitConstructorCall,
		boolean needToInsert) {

		//Add to method'set, the default constuctor that just recall the
		//super constructor with no arguments
		//The arguments' type will be positionned by the TC so just use
		//the default int instead of just null (consistency purpose)

		//the constructor
		ConstructorDeclaration constructor = new ConstructorDeclaration(this.compilationResult);
		constructor.isDefaultConstructor = true;
		constructor.selector = name;
		if (modifiers != AccDefault) {
			constructor.modifiers =
				((this instanceof MemberTypeDeclaration) && (modifiers & AccPrivate) != 0)
					? AccDefault
					: modifiers & AccVisibilityMASK;
		}

		//if you change this setting, please update the 
		//SourceIndexer2.buildTypeDeclaration(TypeDeclaration,char[]) method
		constructor.declarationSourceStart = constructor.sourceStart = sourceStart;
		constructor.declarationSourceEnd =
			constructor.sourceEnd = constructor.bodyEnd = sourceEnd;

		//the super call inside the constructor
		if (needExplicitConstructorCall) {
			constructor.constructorCall =
				new ExplicitConstructorCall(ExplicitConstructorCall.ImplicitSuper);
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

	/**
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
		methodDeclaration.modifiers = methodBinding.getAccessFlags() & ~AccAbstract;

		if (argumentsLength > 0) {
			String baseName = "arg";//$NON-NLS-1$
			Argument[] arguments = (methodDeclaration.arguments = new Argument[argumentsLength]);
			for (int i = argumentsLength; --i >= 0;) {
				arguments[i] = new Argument((baseName + i).toCharArray(), 0L, null /*type ref*/, AccDefault);
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
				argumentsLength == 0 ? NoParameters : argumentTypes, //arguments bindings
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
	
	/*
	 * Find the matching parse node, answers null if nothing found
	 */
	public FieldDeclaration declarationOf(FieldBinding fieldBinding) {

		if (fieldBinding != null) {
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

		if (memberTypeBinding != null) {
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

		if (methodBinding != null) {
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
			
			classFile.generateMissingAbstractMethods(this.missingAbstractMethods, scope.referenceCompilationUnit().compilationResult);

			// generate all methods
			classFile.addSpecialMethods();

			if (ignoreFurtherInvestigation) { // trigger problem type generation for code gen errors
				throw new AbortType(scope.referenceCompilationUnit().compilationResult);
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

	public boolean isInterface() {

		return (modifiers & AccInterface) != 0;
	}

	public boolean hasErrors() {
		return this.ignoreFurtherInvestigation;
	}

	/**
	 * A <clinit> will be requested as soon as static fields or assertions are present. It will be eliminated during
	 * classfile creation if no bytecode was actually produced based on some optimizations/compiler settings.
	 */
	public final boolean needClassInitMethod() {

		// always need a <clinit> when assertions are present
		if ((this.bits & AddAssertionMASK) != 0)
			return true;
		if (fields == null)
			return false;
		if (isInterface())
			return true; // fields are implicitly statics
		for (int i = fields.length; --i >= 0;) {
			FieldDeclaration field = fields[i];
			//need to test the modifier directly while there is no binding yet
			if ((field.modifiers & AccStatic) != 0)
				return true;
		}
		return false;
	}

	public void parseMethod(Parser parser, CompilationUnitDeclaration unit) {

		//connect method bodies
		if (unit.ignoreMethodBodies)
			return;

		// no scope were created, so cannot report further errors
//		if (binding == null)
//			return;

		//members
		if (memberTypes != null) {
			for (int i = memberTypes.length; --i >= 0;)
				memberTypes[i].parseMethod(parser, unit);
		}

		//methods
		if (methods != null) {
			for (int i = methods.length; --i >= 0;)
				methods[i].parseStatements(parser, unit);
		}

		//initializers
		if (fields != null) {
			for (int i = fields.length; --i >= 0;) {
				if (fields[i] instanceof Initializer) {
					((Initializer) fields[i]).parseStatements(parser, this, unit);
				}
			}
		}
	}

	public void resolve() {

		if (binding == null) {
			ignoreFurtherInvestigation = true;
			return;
		}

		try {
			// check superclass & interfaces
			if (binding.superclass != null) // watch out for Object ! (and other roots)	
				if (isTypeUseDeprecated(binding.superclass, scope))
					scope.problemReporter().deprecatedType(binding.superclass, superclass);
			if (superInterfaces != null)
				for (int i = superInterfaces.length; --i >= 0;)
					if (superInterfaces[i].resolvedType != null)
						if (isTypeUseDeprecated(superInterfaces[i].resolvedType, scope))
							scope.problemReporter().deprecatedType(
								superInterfaces[i].resolvedType,
								superInterfaces[i]);
			maxFieldCount = 0;
			int lastFieldID = -1;
			if (fields != null) {
				for (int i = 0, count = fields.length; i < count; i++) {
					FieldDeclaration field = fields[i];
					if (field.isField()) {
						if (field.binding == null) {
							ignoreFurtherInvestigation = true;
							continue;
						}
						maxFieldCount++;
						lastFieldID = field.binding.id;
					} else { // initializer
						 ((Initializer) field).lastFieldID = lastFieldID + 1;
					}
					field.resolve(field.isStatic() ? staticInitializerScope : initializerScope);
				}
			}
			if (memberTypes != null) {
				for (int i = 0, count = memberTypes.length; i < count; i++) {
					memberTypes[i].resolve(scope);
				}
			}
			if (methods != null) {
				for (int i = 0, count = methods.length; i < count; i++) {
					methods[i].resolve(scope);
				}
			}
		} catch (AbortType e) {
			this.ignoreFurtherInvestigation = true;
			return;
		};
	}

	public void resolve(BlockScope blockScope) {
		// local type declaration

		// need to build its scope first and proceed with binding's creation
		blockScope.addLocalType(this);

		// and TC....
		if (binding != null) {
			// binding is not set if the receiver could not be created
			resolve();
			updateMaxFieldCount();
		}
	}

	public void resolve(ClassScope upperScope) {
		// member scopes are already created
		// request the construction of a binding if local member type

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

	public String toString(int tab) {

		return tabString(tab) + toStringHeader() + toStringBody(tab);
	}

	public String toStringBody(int tab) {

		String s = " {"; //$NON-NLS-1$
		if (memberTypes != null) {
			for (int i = 0; i < memberTypes.length; i++) {
				if (memberTypes[i] != null) {
					s += "\n" + memberTypes[i].toString(tab + 1); //$NON-NLS-1$
				}
			}
		}
		if (fields != null) {
			for (int fieldI = 0; fieldI < fields.length; fieldI++) {
				if (fields[fieldI] != null) {
					s += "\n" + fields[fieldI].toString(tab + 1); //$NON-NLS-1$
					if (fields[fieldI].isField())
						s += ";"; //$NON-NLS-1$
				}
			}
		}
		if (methods != null) {
			for (int i = 0; i < methods.length; i++) {
				if (methods[i] != null) {
					s += "\n" + methods[i].toString(tab + 1); //$NON-NLS-1$
				}
			}
		}
		s += "\n" + tabString(tab) + "}"; //$NON-NLS-2$ //$NON-NLS-1$
		return s;
	}

	public String toStringHeader() {

		String s = ""; //$NON-NLS-1$
		if (modifiers != AccDefault) {
			s += modifiersString(modifiers);
		}
		s += (isInterface() ? "interface " : "class ") + new String(name);//$NON-NLS-1$ //$NON-NLS-2$
		if (superclass != null)
			s += " extends " + superclass.toString(0); //$NON-NLS-1$
		if (superInterfaces != null && superInterfaces.length > 0) {
			s += (isInterface() ? " extends " : " implements ");//$NON-NLS-2$ //$NON-NLS-1$
			for (int i = 0; i < superInterfaces.length; i++) {
				s += superInterfaces[i].toString(0);
				if (i != superInterfaces.length - 1)
					s += ", "; //$NON-NLS-1$
			};
		};
		return s;
	}

	/**
	 *	Iteration for a package member type
	 *
	 */
	public void traverse(
		IAbstractSyntaxTreeVisitor visitor,
		CompilationUnitScope unitScope) {

		if (ignoreFurtherInvestigation)
			return;
		try {
			if (visitor.visit(this, unitScope)) {
				if (superclass != null)
					superclass.traverse(visitor, scope);
				if (superInterfaces != null) {
					int superInterfaceLength = superInterfaces.length;
					for (int i = 0; i < superInterfaceLength; i++)
						superInterfaces[i].traverse(visitor, scope);
				}
				if (memberTypes != null) {
					int memberTypesLength = memberTypes.length;
					for (int i = 0; i < memberTypesLength; i++)
						memberTypes[i].traverse(visitor, scope);
				}
				if (fields != null) {
					int fieldsLength = fields.length;
					for (int i = 0; i < fieldsLength; i++) {
						FieldDeclaration field;
						if ((field = fields[i]).isStatic()) {
							field.traverse(visitor, staticInitializerScope);
						} else {
							field.traverse(visitor, initializerScope);
						}
					}
				}
				if (methods != null) {
					int methodsLength = methods.length;
					for (int i = 0; i < methodsLength; i++)
						methods[i].traverse(visitor, scope);
				}
			}
			visitor.endVisit(this, unitScope);
		} catch (AbortType e) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9990.java