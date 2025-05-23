error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1811.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1811.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1811.java
text:
```scala
(@@(SourceTypeBinding) binding.declaringClass).addSyntheticMethod(binding, isSuperAccess());

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

import org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class AllocationExpression
	extends Expression
	implements InvocationSite {
		
	public TypeReference type;
	public Expression[] arguments;
	public MethodBinding binding;

	MethodBinding syntheticAccessor;

	public AllocationExpression() {
		super();
	}

	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {

		// must verify that exceptions potentially thrown by this expression are caught in the method

		// process arguments
		if (arguments != null) {
			for (int i = 0, count = arguments.length; i < count; i++) {
				flowInfo =
					arguments[i]
						.analyseCode(currentScope, flowContext, flowInfo)
						.unconditionalInits();
			}
		}
		// record some dependency information for exception types
		ReferenceBinding[] thrownExceptions;
		if (((thrownExceptions = binding.thrownExceptions).length) != 0) {
			// check exception handling
			flowContext.checkExceptionHandlers(
				thrownExceptions,
				this,
				flowInfo,
				currentScope);
		}
		manageEnclosingInstanceAccessIfNecessary(currentScope);
		manageSyntheticAccessIfNecessary(currentScope);
		return flowInfo;
	}

	public Expression enclosingInstance() {
		return null;
	}

	public void generateCode(
		BlockScope currentScope,
		CodeStream codeStream,
		boolean valueRequired) {

		int pc = codeStream.position;
		ReferenceBinding allocatedType = binding.declaringClass;

		codeStream.new_(allocatedType);
		if (valueRequired) {
			codeStream.dup();
		}
		// better highlight for allocation: display the type individually
		codeStream.recordPositionsFrom(pc, type.sourceStart);

		// handling innerclass instance allocation
		if (allocatedType.isNestedType()) {
			codeStream.generateSyntheticArgumentValues(
				currentScope,
				allocatedType,
				enclosingInstance(),
				this);
		}
		// generate the arguments for constructor
		if (arguments != null) {
			for (int i = 0, count = arguments.length; i < count; i++) {
				arguments[i].generateCode(currentScope, codeStream, true);
			}
		}
		// invoke constructor
		if (syntheticAccessor == null) {
			codeStream.invokespecial(binding);
		} else {
			// synthetic accessor got some extra arguments appended to its signature, which need values
			for (int i = 0,
				max = syntheticAccessor.parameters.length - binding.parameters.length;
				i < max;
				i++) {
				codeStream.aconst_null();
			}
			codeStream.invokespecial(syntheticAccessor);
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}

	public boolean isSuperAccess() {

		return false;
	}

	public boolean isTypeAccess() {

		return true;
	}

	/* Inner emulation consists in either recording a dependency 
	 * link only, or performing one level of propagation.
	 *
	 * Dependency mechanism is used whenever dealing with source target
	 * types, since by the time we reach them, we might not yet know their
	 * exact need.
	 */
	public void manageEnclosingInstanceAccessIfNecessary(BlockScope currentScope) {

		ReferenceBinding allocatedType;

		// perform some emulation work in case there is some and we are inside a local type only
		if ((allocatedType = binding.declaringClass).isNestedType()
			&& currentScope.enclosingSourceType().isLocalType()) {

			if (allocatedType.isLocalType()) {
				((LocalTypeBinding) allocatedType).addInnerEmulationDependent(
					currentScope,
					false,
					false);
				// request cascade of accesses
			} else {
				// locally propagate, since we already now the desired shape for sure
				currentScope.propagateInnerEmulation(allocatedType, false, false);
				// request cascade of accesses
			}
		}
	}

	public void manageSyntheticAccessIfNecessary(BlockScope currentScope) {

		if (binding.isPrivate()
			&& (currentScope.enclosingSourceType() != binding.declaringClass)) {

			if (currentScope
				.environment()
				.options
				.isPrivateConstructorAccessChangingVisibility) {
				binding.tagForClearingPrivateModifier();
				// constructor will not be dumped as private, no emulation required thus
			} else {
				syntheticAccessor =
					((SourceTypeBinding) binding.declaringClass).addSyntheticMethod(binding);
				currentScope.problemReporter().needToEmulateMethodAccess(binding, this);
			}
		}
	}

	public TypeBinding resolveType(BlockScope scope) {

		// Propagate the type checking to the arguments, and check if the constructor is defined.
		constant = NotAConstant;
		TypeBinding typeBinding = type.resolveType(scope);
		// will check for null after args are resolved

		// buffering the arguments' types
		TypeBinding[] argumentTypes = NoParameters;
		if (arguments != null) {
			boolean argHasError = false;
			int length = arguments.length;
			argumentTypes = new TypeBinding[length];
			for (int i = 0; i < length; i++)
				if ((argumentTypes[i] = arguments[i].resolveType(scope)) == null)
					argHasError = true;
			if (argHasError)
				return typeBinding;
		}
		if (typeBinding == null)
			return null;

		if (!typeBinding.canBeInstantiated()) {
			scope.problemReporter().cannotInstantiate(type, typeBinding);
			return typeBinding;
		}
		ReferenceBinding allocatedType = (ReferenceBinding) typeBinding;
		if (!(binding = scope.getConstructor(allocatedType, argumentTypes, this))
			.isValidBinding()) {
			if (binding.declaringClass == null)
				binding.declaringClass = allocatedType;
			scope.problemReporter().invalidConstructor(this, binding);
			return typeBinding;
		}
		if (isMethodUseDeprecated(binding, scope))
			scope.problemReporter().deprecatedMethod(binding, this);

		if (arguments != null)
			for (int i = 0; i < arguments.length; i++)
				arguments[i].implicitWidening(binding.parameters[i], argumentTypes[i]);
		return allocatedType;
	}

	public void setActualReceiverType(ReferenceBinding receiverType) {
		// ignored
	}

	public void setDepth(int i) {
		// ignored
	}

	public void setFieldIndex(int i) {
		// ignored
	}

	public String toStringExpression() {

		String s = "new " + type.toString(0); //$NON-NLS-1$
		if (arguments == null)
			s = s + "()"; //$NON-NLS-1$
		else {
			s = s + "("; //$NON-NLS-1$
			for (int i = 0; i < arguments.length; i++) {
				s = s + arguments[i].toStringExpression();
				if (i == (arguments.length - 1))
					s = s + ")"; //$NON-NLS-1$
				else
					s = s + ", "; //$NON-NLS-1$
			}
		}
		return s;
	}

	public void traverse(IAbstractSyntaxTreeVisitor visitor, BlockScope scope) {

		if (visitor.visit(this, scope)) {
			int argumentsLength;
			type.traverse(visitor, scope);
			if (arguments != null) {
				argumentsLength = arguments.length;
				for (int i = 0; i < argumentsLength; i++)
					arguments[i].traverse(visitor, scope);
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1811.java