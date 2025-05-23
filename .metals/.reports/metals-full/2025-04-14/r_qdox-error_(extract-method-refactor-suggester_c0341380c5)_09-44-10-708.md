error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5406.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5406.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5406.java
text:
```scala
T@@ypeBinding constantPoolDeclaringClass = CodeStream.getConstantPoolDeclaringClass(currentScope, codegenBinding, this.actualReceiverType, this.receiver.isImplicitThis(), this.receiverGenericCast != null);

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
package org.eclipse.jdt.internal.eval;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.CodeStream;
import org.eclipse.jdt.internal.compiler.codegen.Opcodes;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemMethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import org.eclipse.jdt.internal.compiler.lookup.TypeVariableBinding;

public class CodeSnippetMessageSend extends MessageSend {
	EvaluationContext evaluationContext;
	FieldBinding delegateThis;
/**
 * CodeSnippetMessageSend constructor comment.
 */
public CodeSnippetMessageSend(EvaluationContext evaluationContext) {
	this.evaluationContext = evaluationContext;
}
/**
 * MessageSend code generation
 *
 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
 * @param valueRequired boolean
 */
public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
	int pc = codeStream.position;
	MethodBinding codegenBinding = this.binding.original();
	if (codegenBinding.canBeSeenBy(this.actualReceiverType, this, currentScope)) {
		// generate receiver/enclosing instance access
		boolean isStatic = codegenBinding.isStatic();
		// outer access ?
		if (!isStatic && ((this.bits & DepthMASK) != 0)) {
			// outer method can be reached through emulation
			ReferenceBinding targetType = currentScope.enclosingSourceType().enclosingTypeAt((this.bits & DepthMASK) >> DepthSHIFT);
			Object[] path = currentScope.getEmulationPath(targetType, true /*only exact match*/, false/*consider enclosing arg*/);
			if (path == null) {
				// emulation was not possible (should not happen per construction)
				currentScope.problemReporter().needImplementation(this);
			} else {
				codeStream.generateOuterAccess(path, this, targetType, currentScope);
			}
		} else {
			this.receiver.generateCode(currentScope, codeStream, !isStatic);
			if (this.receiverGenericCast != null)
				codeStream.checkcast(this.receiverGenericCast);
			codeStream.recordPositionsFrom(pc, this.sourceStart);
		}
		// generate arguments
		generateArguments(this.binding, this.arguments, currentScope, codeStream);
		// actual message invocation
		TypeBinding constantPoolDeclaringClass = getConstantPoolDeclaringClass(currentScope);
		if (isStatic) {
			codeStream.invoke(Opcodes.OPC_invokestatic, codegenBinding, constantPoolDeclaringClass);
		} else if( (this.receiver.isSuper()) || codegenBinding.isPrivate()){
			codeStream.invoke(Opcodes.OPC_invokespecial, codegenBinding, constantPoolDeclaringClass);
		} else {
			if (constantPoolDeclaringClass.isInterface()) { // interface or annotation type
				codeStream.invoke(Opcodes.OPC_invokeinterface, codegenBinding, constantPoolDeclaringClass);
			} else {
				codeStream.invoke(Opcodes.OPC_invokevirtual, codegenBinding, constantPoolDeclaringClass);
			}
		}
	} else {
		codeStream.generateEmulationForMethod(currentScope, codegenBinding);
		// generate receiver/enclosing instance access
		boolean isStatic = codegenBinding.isStatic();
		// outer access ?
		if (!isStatic && ((this.bits & DepthMASK) != 0)) {
			// not supported yet
			currentScope.problemReporter().needImplementation(this);
		} else {
			this.receiver.generateCode(currentScope, codeStream, !isStatic);
			if (this.receiverGenericCast != null)
				codeStream.checkcast(this.receiverGenericCast);
			codeStream.recordPositionsFrom(pc, this.sourceStart);
		}
		if (isStatic) {
			// we need an object on the stack which is ignored for the method invocation
			codeStream.aconst_null();
		}
		// generate arguments
		if (this.arguments != null) {
			int argsLength = this.arguments.length;
			codeStream.generateInlinedValue(argsLength);
			codeStream.newArray(currentScope.createArrayType(currentScope.getType(TypeConstants.JAVA_LANG_OBJECT, 3), 1));
			codeStream.dup();
			for (int i = 0; i < argsLength; i++) {
				codeStream.generateInlinedValue(i);
				this.arguments[i].generateCode(currentScope, codeStream, true);
				TypeBinding parameterBinding = codegenBinding.parameters[i];
				if (parameterBinding.isBaseType() && parameterBinding != TypeBinding.NULL) {
					codeStream.generateBoxingConversion(codegenBinding.parameters[i].id);
				}
				codeStream.aastore();
				if (i < argsLength - 1) {
					codeStream.dup();
				}
			}
		} else {
			codeStream.generateInlinedValue(0);
			codeStream.newArray(currentScope.createArrayType(currentScope.getType(TypeConstants.JAVA_LANG_OBJECT, 3), 1));
		}
		codeStream.invokeJavaLangReflectMethodInvoke();

		// convert the return value to the appropriate type for primitive types
		if (codegenBinding.returnType.isBaseType()) {
			int typeID = codegenBinding.returnType.id;
			if (typeID == T_void) {
				// remove the null from the stack
				codeStream.pop();
			}
			codeStream.checkcast(typeID);
			codeStream.getBaseTypeValue(typeID);
		} else {
			codeStream.checkcast(codegenBinding.returnType);
		}
	}
	// required cast must occur even if no value is required
	if (this.valueCast != null) codeStream.checkcast(this.valueCast);
	if (valueRequired){
		// implicit conversion if necessary
		codeStream.generateImplicitConversion(this.implicitConversion);
	} else {
		boolean isUnboxing = (this.implicitConversion & TypeIds.UNBOXING) != 0;
		// conversion only generated if unboxing
		if (isUnboxing) codeStream.generateImplicitConversion(this.implicitConversion);
		switch (isUnboxing ? postConversionType(currentScope).id : codegenBinding.returnType.id) {
			case T_long :
			case T_double :
				codeStream.pop2();
				break;
			case T_void :
				break;
			default :
				codeStream.pop();
		}
	}
	codeStream.recordPositionsFrom(pc, (int)(this.nameSourcePosition >>> 32)); // highlight selector
}
public void manageSyntheticAccessIfNecessary(BlockScope currentScope, FlowInfo flowInfo) {

	if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) == 0) {
		// if method from parameterized type got found, use the original method at codegen time
		MethodBinding codegenBinding = this.binding.original();
		if (codegenBinding != this.binding) {
		    // extra cast needed if method return type was type variable
		    if (codegenBinding.returnType.isTypeVariable()) {
		        TypeVariableBinding variableReturnType = (TypeVariableBinding) codegenBinding.returnType;
		        if (variableReturnType.firstBound != this.binding.returnType) { // no need for extra cast if same as first bound anyway
				    this.valueCast = this.binding.returnType;
		        }
		    }
		}
	}
}
public TypeBinding resolveType(BlockScope scope) {
	// Answer the signature return type
	// Base type promotion

	this.constant = Constant.NotAConstant;
	boolean receiverCast = false, argsContainCast = false;
	if (this.receiver instanceof CastExpression) {
		this.receiver.bits |= DisableUnnecessaryCastCheck; // will check later on
		receiverCast = true;
	}
	this.actualReceiverType = this.receiver.resolveType(scope);
	if (receiverCast && this.actualReceiverType != null) {
		 // due to change of declaring class with receiver type, only identity cast should be notified
		if (((CastExpression)this.receiver).expression.resolvedType == this.actualReceiverType) {
			scope.problemReporter().unnecessaryCast((CastExpression)this.receiver);
		}
	}
	// resolve type arguments (for generic constructor call)
	if (this.typeArguments != null) {
		int length = this.typeArguments.length;
		boolean argHasError = false; // typeChecks all arguments
		this.genericTypeArguments = new TypeBinding[length];
		for (int i = 0; i < length; i++) {
			if ((this.genericTypeArguments[i] = this.typeArguments[i].resolveType(scope, true /* check bounds*/)) == null) {
				argHasError = true;
			}
		}
		if (argHasError) {
			return null;
		}
	}
	// will check for null after args are resolved
	TypeBinding[] argumentTypes = Binding.NO_PARAMETERS;
	if (this.arguments != null) {
		boolean argHasError = false; // typeChecks all arguments
		int length = this.arguments.length;
		argumentTypes = new TypeBinding[length];
		for (int i = 0; i < length; i++) {
			Expression argument = this.arguments[i];
			if (argument instanceof CastExpression) {
				argument.bits |= DisableUnnecessaryCastCheck; // will check later on
				argsContainCast = true;
			}
			if ((argumentTypes[i] = this.arguments[i].resolveType(scope)) == null)
				argHasError = true;
		}
		if (argHasError) {
			if(this.actualReceiverType instanceof ReferenceBinding) {
				// record any selector match, for clients who may still need hint about possible method match
				this.binding = scope.findMethod((ReferenceBinding)this.actualReceiverType, this.selector, new TypeBinding[]{}, this);
			}
			return null;
		}
	}
	if (this.actualReceiverType == null) {
		return null;
	}
	// base type cannot receive any message
	if (this.actualReceiverType.isBaseType()) {
		scope.problemReporter().errorNoMethodFor(this, this.actualReceiverType, argumentTypes);
		return null;
	}

	this.binding =
		this.receiver.isImplicitThis()
			? scope.getImplicitMethod(this.selector, argumentTypes, this)
			: scope.getMethod(this.actualReceiverType, this.selector, argumentTypes, this);
	if (!this.binding.isValidBinding()) {
		if (this.binding instanceof ProblemMethodBinding
			&& ((ProblemMethodBinding) this.binding).problemId() == ProblemReasons.NotVisible) {
			if (this.evaluationContext.declaringTypeName != null) {
				this.delegateThis = scope.getField(scope.enclosingSourceType(), EvaluationConstants.DELEGATE_THIS, this);
				if (this.delegateThis == null){ // if not found then internal error, field should have been found
					this.constant = Constant.NotAConstant;
					scope.problemReporter().invalidMethod(this, this.binding);
					return null;
				}
			} else {
				this.constant = Constant.NotAConstant;
				scope.problemReporter().invalidMethod(this, this.binding);
				return null;
			}
			CodeSnippetScope localScope = new CodeSnippetScope(scope);
			MethodBinding privateBinding =
				this.receiver instanceof CodeSnippetThisReference && ((CodeSnippetThisReference) this.receiver).isImplicit
					? localScope.getImplicitMethod((ReferenceBinding)this.delegateThis.type, this.selector, argumentTypes, this)
					: localScope.getMethod(this.delegateThis.type, this.selector, argumentTypes, this);
			if (!privateBinding.isValidBinding()) {
				if (this.binding.declaringClass == null) {
					if (this.actualReceiverType instanceof ReferenceBinding) {
						this.binding.declaringClass = (ReferenceBinding) this.actualReceiverType;
					} else { // really bad error ....
						scope.problemReporter().errorNoMethodFor(this, this.actualReceiverType, argumentTypes);
						return null;
					}
				}
				scope.problemReporter().invalidMethod(this, this.binding);
				return null;
			} else {
				this.binding = privateBinding;
			}
		} else {
			if (this.binding.declaringClass == null) {
				if (this.actualReceiverType instanceof ReferenceBinding) {
					this.binding.declaringClass = (ReferenceBinding) this.actualReceiverType;
				} else { // really bad error ....
					scope.problemReporter().errorNoMethodFor(this, this.actualReceiverType, argumentTypes);
					return null;
				}
			}
			scope.problemReporter().invalidMethod(this, this.binding);
			return null;
		}
	}
	if (!this.binding.isStatic()) {
		// the "receiver" must not be a type, in other words, a NameReference that the TC has bound to a Type
		if (this.receiver instanceof NameReference
				&& (((NameReference) this.receiver).bits & Binding.TYPE) != 0) {
			scope.problemReporter().mustUseAStaticMethod(this, this.binding);
		} else {
			// compute generic cast if necessary
			TypeBinding receiverErasure = this.actualReceiverType.erasure();
			if (receiverErasure instanceof ReferenceBinding) {
				if (receiverErasure.findSuperTypeOriginatingFrom(this.binding.declaringClass) == null) {
					this.actualReceiverType = this.binding.declaringClass; // handle indirect inheritance thru variable secondary bound
				}
			}
			this.receiver.computeConversion(scope, this.actualReceiverType, this.actualReceiverType);
		}
	}
	checkInvocationArguments(scope, this.receiver, this.actualReceiverType, this.binding, this.arguments, argumentTypes, argsContainCast, this);

	//-------message send that are known to fail at compile time-----------
	if (this.binding.isAbstract()) {
		if (this.receiver.isSuper()) {
			scope.problemReporter().cannotDireclyInvokeAbstractMethod(this, this.binding);
		}
		// abstract private methods cannot occur nor abstract static............
	}
	if (isMethodUseDeprecated(this.binding, scope, true))
		scope.problemReporter().deprecatedMethod(this.binding, this);

	// from 1.5 compliance on, array#clone() returns the array type (but binding still shows Object)
	if (this.actualReceiverType.isArrayType()
			&& this.binding.parameters == Binding.NO_PARAMETERS
			&& scope.compilerOptions().complianceLevel >= ClassFileConstants.JDK1_5
			&& CharOperation.equals(this.binding.selector, CLONE)) {
		this.resolvedType = this.actualReceiverType;
	} else {
		TypeBinding returnType = this.binding.returnType;
		if (returnType != null) returnType = returnType.capture(scope, this.sourceEnd);
		this.resolvedType = returnType;
	}
	return this.resolvedType;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5406.java