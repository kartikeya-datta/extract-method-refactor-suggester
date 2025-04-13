error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/826.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/826.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/826.java
text:
```scala
&@@& this.binding.declaringClass.id != T_JavaLangObject) // no change for Object methods

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
package org.eclipse.jdt.internal.eval;

import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.CodeStream;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemMethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.jdt.internal.compiler.lookup.TypeVariableBinding;

public class CodeSnippetMessageSend extends MessageSend implements ProblemReasons, EvaluationConstants {
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
public void generateCode(
	BlockScope currentScope,
	CodeStream codeStream,
	boolean valueRequired) {

	int pc = codeStream.position;

	if (this.codegenBinding.canBeSeenBy(this.actualReceiverType, this, currentScope)) {
		// generate receiver/enclosing instance access
		boolean isStatic = this.codegenBinding.isStatic();
		// outer access ?
		if (!isStatic && ((this.bits & DepthMASK) != 0)) {
			// outer method can be reached through emulation
			ReferenceBinding targetType = currentScope.enclosingSourceType().enclosingTypeAt((this.bits & DepthMASK) >> DepthSHIFT);
			Object[] path = currentScope.getEmulationPath(targetType, true /*only exact match*/, false/*consider enclosing arg*/);
			if (path == null) {
				// emulation was not possible (should not happen per construction)
				currentScope.problemReporter().needImplementation();
			} else {
				codeStream.generateOuterAccess(path, this, targetType, currentScope);
			}
		} else {
			this.receiver.generateCode(currentScope, codeStream, !isStatic);
		}
		// generate arguments
		if (this.arguments != null) {
			for (int i = 0, max = this.arguments.length; i < max; i++) {
				this.arguments[i].generateCode(currentScope, codeStream, true);
			}
		}
		// actual message invocation
		if (isStatic) {
			codeStream.invokestatic(this.codegenBinding);
		} else {
			if (this.receiver.isSuper()) {
				codeStream.invokespecial(this.codegenBinding);
			} else {
				if (this.codegenBinding.declaringClass.isInterface()) {
					codeStream.invokeinterface(this.codegenBinding);
				} else {
					codeStream.invokevirtual(this.codegenBinding);
				}
			}
		}
	} else {
		((CodeSnippetCodeStream) codeStream).generateEmulationForMethod(currentScope, this.codegenBinding);
		// generate receiver/enclosing instance access
		boolean isStatic = this.codegenBinding.isStatic();
		// outer access ?
		if (!isStatic && ((this.bits & DepthMASK) != 0)) {
			// not supported yet
			currentScope.problemReporter().needImplementation();
		} else {
			this.receiver.generateCode(currentScope, codeStream, !isStatic);
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
				TypeBinding parameterBinding = this.codegenBinding.parameters[i];
				if (parameterBinding.isBaseType() && parameterBinding != NullBinding) {
					((CodeSnippetCodeStream)codeStream).generateObjectWrapperForType(this.codegenBinding.parameters[i]);
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
		((CodeSnippetCodeStream) codeStream).invokeJavaLangReflectMethodInvoke();

		// convert the return value to the appropriate type for primitive types
		if (this.codegenBinding.returnType.isBaseType()) {
			int typeID = this.codegenBinding.returnType.id;
			if (typeID == T_void) {
				// remove the null from the stack
				codeStream.pop();
			}
			((CodeSnippetCodeStream) codeStream).checkcast(typeID);
			((CodeSnippetCodeStream) codeStream).getBaseTypeValue(typeID);
		} else {
			codeStream.checkcast(this.codegenBinding.returnType);
		}
	}
	// operation on the returned value
	if (valueRequired) {
		// implicit conversion if necessary
		codeStream.generateImplicitConversion(this.implicitConversion);
	} else {
		// pop return value if any
		switch (this.codegenBinding.returnType.id) {
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
	// TODO (philippe) need to revise codegen to include genericCast
	codeStream.recordPositionsFrom(pc, this.sourceStart);
}
public void manageSyntheticAccessIfNecessary(BlockScope currentScope, FlowInfo flowInfo) {

	if (!flowInfo.isReachable()) return;

	// if method from parameterized type got found, use the original method at codegen time
	this.codegenBinding = this.binding.original();
	if (this.codegenBinding != this.binding) {
	    // extra cast needed if method return type was type variable
	    if (this.codegenBinding.returnType.isTypeVariable()) {
	        TypeVariableBinding variableReturnType = (TypeVariableBinding) this.codegenBinding.returnType;
	        if (variableReturnType.firstBound != this.binding.returnType) { // no need for extra cast if same as first bound anyway
			    this.valueCast = this.binding.returnType;
	        }
	    }
	} 
	
	// if the binding declaring class is not visible, need special action
	// for runtime compatibility on 1.2 VMs : change the declaring class of the binding
	// NOTE: from target 1.2 on, method's declaring class is touched if any different from receiver type
	// and not from Object or implicit static method call.	
	if (this.binding.declaringClass != this.actualReceiverType
		&& !this.actualReceiverType.isArrayType()
		&& ((currentScope.environment().options.targetJDK >= ClassFileConstants.JDK1_2
				&& (!this.receiver.isImplicitThis() || !this.codegenBinding.isStatic())
				&& this.binding.declaringClass.id != T_Object) // no change for Object methods
 !this.codegenBinding.declaringClass.canBeSeenBy(currentScope))) {
		this.codegenBinding = currentScope.enclosingSourceType().getUpdatedMethodBinding(this.codegenBinding, (ReferenceBinding) this.actualReceiverType.erasure());
	}	
}
public TypeBinding resolveType(BlockScope scope) {
	// Answer the signature return type
	// Base type promotion

	this.constant = NotAConstant;
	this.actualReceiverType = this.receiver.resolveType(scope); 
	// will check for null after args are resolved
	TypeBinding[] argumentTypes = NoParameters;
	if (this.arguments != null) {
		boolean argHasError = false; // typeChecks all arguments 
		int length = this.arguments.length;
		argumentTypes = new TypeBinding[length];
		for (int i = 0; i < length; i++)
			if ((argumentTypes[i] = this.arguments[i].resolveType(scope)) == null)
				argHasError = true;
		if (argHasError)
			return null;
	}
	if (this.actualReceiverType == null) 
		return null;

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
			&& ((ProblemMethodBinding) this.binding).problemId() == NotVisible) {
			if (this.evaluationContext.declaringTypeName != null) {
				this.delegateThis = scope.getField(scope.enclosingSourceType(), DELEGATE_THIS, this);
				if (this.delegateThis == null){ // if not found then internal error, field should have been found
					this.constant = NotAConstant;
					scope.problemReporter().invalidMethod(this, this.binding);
					return null;
				}
			} else {
				this.constant = NotAConstant;
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
		if (this.receiver instanceof NameReference) {
			if ((((NameReference) this.receiver).bits & Binding.TYPE) != 0) {
				scope.problemReporter().mustUseAStaticMethod(this, this.binding);
				return null;
			}
		}
	}
	if (this.arguments != null)
		for (int i = 0; i < this.arguments.length; i++)
			this.arguments[i].computeConversion(scope, this.binding.parameters[i], argumentTypes[i]);

	//-------message send that are known to fail at compile time-----------
	if (this.binding.isAbstract()) {
		if (this.receiver.isSuper()) {
			scope.problemReporter().cannotDireclyInvokeAbstractMethod(this, this.binding);
			return null;
		}
		// abstract private methods cannot occur nor abstract static............
	}
	if (isMethodUseDeprecated(this.binding, scope))
		scope.problemReporter().deprecatedMethod(this.binding, this);

	return this.resolvedType = this.binding.returnType;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/826.java