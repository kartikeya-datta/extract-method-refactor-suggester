error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/11.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/11.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/11.java
text:
```scala
r@@eturn this.expressionType = binding.returnType;

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
package org.eclipse.jdt.internal.eval;

import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.codegen.CodeStream;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.jdt.internal.compiler.lookup.BindingIds;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemMethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

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

	if (binding.canBeSeenBy(receiverType, this, currentScope)) {
		// generate receiver/enclosing instance access
		boolean isStatic = binding.isStatic();
		// outer access ?
		if (!isStatic && ((bits & DepthMASK) != 0)) {
			// outer method can be reached through emulation
			Object[] path =
				currentScope.getExactEmulationPath(
					currentScope.enclosingSourceType().enclosingTypeAt(
						(bits & DepthMASK) >> DepthSHIFT));
			if (path == null) {
				// emulation was not possible (should not happen per construction)
				currentScope.problemReporter().needImplementation();
			} else {
				codeStream.generateOuterAccess(path, this, currentScope);
			}
		} else {
			receiver.generateCode(currentScope, codeStream, !isStatic);
		}
		// generate arguments
		if (arguments != null) {
			for (int i = 0, max = arguments.length; i < max; i++) {
				arguments[i].generateCode(currentScope, codeStream, true);
			}
		}
		// actual message invocation
		if (isStatic) {
			codeStream.invokestatic(binding);
		} else {
			if (receiver.isSuper()) {
				codeStream.invokespecial(binding);
			} else {
				if (binding.declaringClass.isInterface()) {
					codeStream.invokeinterface(binding);
				} else {
					codeStream.invokevirtual(binding);
				}
			}
		}
	} else {
		((CodeSnippetCodeStream) codeStream).generateEmulationForMethod(currentScope, binding);
		// generate receiver/enclosing instance access
		boolean isStatic = binding.isStatic();
		// outer access ?
		if (!isStatic && ((bits & DepthMASK) != 0)) {
			// not supported yet
			currentScope.problemReporter().needImplementation();
		} else {
			receiver.generateCode(currentScope, codeStream, !isStatic);
		}
		if (isStatic) {
			// we need an object on the stack which is ignored for the method invocation
			codeStream.aconst_null();
		}
		// generate arguments
		if (arguments != null) {
			int argsLength = arguments.length;
			codeStream.generateInlinedValue(argsLength);
			codeStream.newArray(currentScope, new ArrayBinding(currentScope.getType(TypeBinding.JAVA_LANG_OBJECT), 1));
			codeStream.dup();
			for (int i = 0; i < argsLength; i++) {
				codeStream.generateInlinedValue(i);
				arguments[i].generateCode(currentScope, codeStream, true);
				TypeBinding parameterBinding = binding.parameters[i];
				if (parameterBinding.isBaseType() && parameterBinding != NullBinding) {
					((CodeSnippetCodeStream)codeStream).generateObjectWrapperForType(binding.parameters[i]);
				}
				codeStream.aastore();
				if (i < argsLength - 1) {
					codeStream.dup();
				}	
			}
		} else {
			codeStream.generateInlinedValue(0);
			codeStream.newArray(currentScope, new ArrayBinding(currentScope.getType(TypeBinding.JAVA_LANG_OBJECT), 1));			
		}
		((CodeSnippetCodeStream) codeStream).invokeJavaLangReflectMethodInvoke();

		// convert the return value to the appropriate type for primitive types
		if (binding.returnType.isBaseType()) {
			int typeID = binding.returnType.id;
			if (typeID == T_void) {
				// remove the null from the stack
				codeStream.pop();
			}
			((CodeSnippetCodeStream) codeStream).checkcast(typeID);
			((CodeSnippetCodeStream) codeStream).getBaseTypeValue(typeID);
		} else {
			codeStream.checkcast(binding.returnType);
		}
	}
	// operation on the returned value
	if (valueRequired) {
		// implicit conversion if necessary
		codeStream.generateImplicitConversion(implicitConversion);
	} else {
		// pop return value if any
		switch (binding.returnType.id) {
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
	codeStream.recordPositionsFrom(pc, this.sourceStart);
}
public void manageEnclosingInstanceAccessIfNecessary(BlockScope currentScope) {
}
public void manageSyntheticAccessIfNecessary(BlockScope currentScope) {

	// if the binding declaring class is not visible, need special action
	// for runtime compatibility on 1.2 VMs : change the declaring class of the binding
	// NOTE: from 1.4 on, method's declaring class is touched if any different from receiver type
	// and not from Object or implicit static method call.	
	if (binding.declaringClass != this.qualifyingType
		&& !this.qualifyingType.isArrayType()
		&& ((currentScope.environment().options.complianceLevel >= CompilerOptions.JDK1_4
				&& (receiver != ThisReference.ThisImplicit || !binding.isStatic())
				&& binding.declaringClass.id != T_Object) // no change for Object methods
 !binding.declaringClass.canBeSeenBy(currentScope))) {
		codegenBinding = currentScope.enclosingSourceType().getUpdatedMethodBinding(binding, (ReferenceBinding) this.qualifyingType);
	}	
}
public TypeBinding resolveType(BlockScope scope) {
	// Answer the signature return type
	// Base type promotion

	constant = NotAConstant;
	this.qualifyingType = this.receiverType = receiver.resolveType(scope); 
	// will check for null after args are resolved
	TypeBinding[] argumentTypes = NoParameters;
	if (arguments != null) {
		boolean argHasError = false; // typeChecks all arguments 
		int length = arguments.length;
		argumentTypes = new TypeBinding[length];
		for (int i = 0; i < length; i++)
			if ((argumentTypes[i] = arguments[i].resolveType(scope)) == null)
				argHasError = true;
		if (argHasError)
			return null;
	}
	if (receiverType == null) 
		return null;

	// base type cannot receive any message
	if (receiverType.isBaseType()) {
		scope.problemReporter().errorNoMethodFor(this, receiverType, argumentTypes);
		return null;
	}

	binding = 
		receiver == ThisReference.ThisImplicit
			? scope.getImplicitMethod(selector, argumentTypes, this)
			: scope.getMethod(receiverType, selector, argumentTypes, this); 
	if (!binding.isValidBinding()) {
		if (binding instanceof ProblemMethodBinding
			&& ((ProblemMethodBinding) binding).problemId() == NotVisible) {
			if (this.evaluationContext.declaringTypeName != null) {
				delegateThis = scope.getField(scope.enclosingSourceType(), DELEGATE_THIS, this);
				if (delegateThis == null){ ; // if not found then internal error, field should have been found
					constant = NotAConstant;
					scope.problemReporter().invalidMethod(this, binding);
					return null;
				}
			} else {
				constant = NotAConstant;
				scope.problemReporter().invalidMethod(this, binding);
				return null;
			}
			CodeSnippetScope localScope = new CodeSnippetScope(scope);			
			MethodBinding privateBinding = 
				receiver instanceof CodeSnippetThisReference && ((CodeSnippetThisReference) receiver).isImplicit
					? localScope.getImplicitMethod((ReferenceBinding)delegateThis.type, selector, argumentTypes, this)
					: localScope.getMethod(delegateThis.type, selector, argumentTypes, this); 
			if (!privateBinding.isValidBinding()) {
				if (binding.declaringClass == null) {
					if (receiverType instanceof ReferenceBinding) {
						binding.declaringClass = (ReferenceBinding) receiverType;
					} else { // really bad error ....
						scope.problemReporter().errorNoMethodFor(this, receiverType, argumentTypes);
						return null;
					}
				}
				scope.problemReporter().invalidMethod(this, binding);
				return null;
			} else {
				binding = privateBinding;
			}
		} else {
			if (binding.declaringClass == null) {
				if (receiverType instanceof ReferenceBinding) {
					binding.declaringClass = (ReferenceBinding) receiverType;
				} else { // really bad error ....
					scope.problemReporter().errorNoMethodFor(this, receiverType, argumentTypes);
					return null;
				}
			}
			scope.problemReporter().invalidMethod(this, binding);
			return null;
		}
	}
	if (!binding.isStatic()) {
		// the "receiver" must not be a type, i.e. a NameReference that the TC has bound to a Type
		if (receiver instanceof NameReference) {
			if ((((NameReference) receiver).bits & BindingIds.TYPE) != 0) {
				scope.problemReporter().mustUseAStaticMethod(this, binding);
				return null;
			}
		}
	}
	if (arguments != null)
		for (int i = 0; i < arguments.length; i++)
			arguments[i].implicitWidening(binding.parameters[i], argumentTypes[i]);

	//-------message send that are known to fail at compile time-----------
	if (binding.isAbstract()) {
		if (receiver.isSuper()) {
			scope.problemReporter().cannotDireclyInvokeAbstractMethod(this, binding);
			return null;
		}
		// abstract private methods cannot occur nor abstract static............
	}
	if (isMethodUseDeprecated(binding, scope))
		scope.problemReporter().deprecatedMethod(binding, this);

	return binding.returnType;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/11.java