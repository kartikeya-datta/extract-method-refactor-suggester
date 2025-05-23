error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/800.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/800.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/800.java
text:
```scala
s@@cope.referenceCompilationUnit().compilationResult.getProblems();

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

import java.util.ArrayList;

import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.parser.*;
import org.eclipse.jdt.internal.compiler.problem.*;

public class ConstructorDeclaration extends AbstractMethodDeclaration {

	public ExplicitConstructorCall constructorCall;
	public final static char[] ConstantPoolName = "<init>".toCharArray(); //$NON-NLS-1$
	public boolean isDefaultConstructor = false;

	public ConstructorDeclaration(CompilationResult compilationResult){
		super(compilationResult);
	}
	
	public void analyseCode(
		ClassScope classScope,
		InitializationFlowContext initializerFlowContext,
		FlowInfo flowInfo) {

		if (ignoreFurtherInvestigation)
			return;

		if (!isDefaultConstructor && this.binding.isPrivate() && !this.binding.isPrivateUsed()) {
			if (!classScope.referenceCompilationUnit().compilationResult.hasSyntaxError()) {
				scope.problemReporter().unusedPrivateConstructor(this);
			}
		}
			
		// check constructor recursion, once all constructor got resolved
		if (isRecursive(null /*lazy initialized visited list*/)) {				
			this.scope.problemReporter().recursiveConstructorInvocation(this.constructorCall);
		}
			
		try {
			ExceptionHandlingFlowContext constructorContext =
				new ExceptionHandlingFlowContext(
					initializerFlowContext.parent,
					this,
					binding.thrownExceptions,
					scope,
					FlowInfo.DEAD_END);
			initializerFlowContext.checkInitializerExceptions(
				scope,
				constructorContext,
				flowInfo);

			// anonymous constructor can gain extra thrown exceptions from unhandled ones
			if (binding.declaringClass.isAnonymousType()) {
				ArrayList computedExceptions = constructorContext.extendedExceptions;
				if (computedExceptions != null){
					int size;
					if ((size = computedExceptions.size()) > 0){
						ReferenceBinding[] actuallyThrownExceptions;
						computedExceptions.toArray(actuallyThrownExceptions = new ReferenceBinding[size]);
						binding.thrownExceptions = actuallyThrownExceptions;
					}
				}
			}
			
			// propagate to constructor call
			if (constructorCall != null) {
				// if calling 'this(...)', then flag all non-static fields as definitely
				// set since they are supposed to be set inside other local constructor
				if (constructorCall.accessMode == ExplicitConstructorCall.This) {
					FieldBinding[] fields = binding.declaringClass.fields();
					for (int i = 0, count = fields.length; i < count; i++) {
						FieldBinding field;
						if (!(field = fields[i]).isStatic()) {
							flowInfo.markAsDefinitelyAssigned(field);
						}
					}
				}
				flowInfo = constructorCall.analyseCode(scope, constructorContext, flowInfo);
			}
			// propagate to statements
			if (statements != null) {
				boolean didAlreadyComplain = false;
				for (int i = 0, count = statements.length; i < count; i++) {
					Statement stat;
					if (!flowInfo.complainIfUnreachable(stat = statements[i], scope, didAlreadyComplain)) {
						flowInfo = stat.analyseCode(scope, constructorContext, flowInfo);
					} else {
						didAlreadyComplain = true;
					}
				}
			}
			// check for missing returning path
			this.needFreeReturn = flowInfo.isReachable();

			// check missing blank final field initializations
			if ((constructorCall != null)
				&& (constructorCall.accessMode != ExplicitConstructorCall.This)) {
				flowInfo = flowInfo.mergedWith(constructorContext.initsOnReturn);
				FieldBinding[] fields = binding.declaringClass.fields();
				for (int i = 0, count = fields.length; i < count; i++) {
					FieldBinding field;
					if ((!(field = fields[i]).isStatic())
						&& field.isFinal()
						&& (!flowInfo.isDefinitelyAssigned(fields[i]))) {
						scope.problemReporter().uninitializedBlankFinalField(
							field,
							isDefaultConstructor ? (AstNode) scope.referenceType() : this);
					}
				}
			}
		} catch (AbortMethod e) {
			this.ignoreFurtherInvestigation = true;
		}
	}

	/**
	 * Bytecode generation for a constructor
	 *
	 * @param classScope org.eclipse.jdt.internal.compiler.lookup.ClassScope
	 * @param classFile org.eclipse.jdt.internal.compiler.codegen.ClassFile
	 */
	public void generateCode(ClassScope classScope, ClassFile classFile) {
		
		int problemResetPC = 0;
		if (ignoreFurtherInvestigation) {
			if (this.binding == null)
				return; // Handle methods with invalid signature or duplicates
			int problemsLength;
			IProblem[] problems =
				scope.referenceCompilationUnit().compilationResult.getAllProblems();
			IProblem[] problemsCopy = new IProblem[problemsLength = problems.length];
			System.arraycopy(problems, 0, problemsCopy, 0, problemsLength);
			classFile.addProblemConstructor(this, binding, problemsCopy);
			return;
		}
		try {
			problemResetPC = classFile.contentsOffset;
			this.internalGenerateCode(classScope, classFile);
		} catch (AbortMethod e) {
			if (e.compilationResult == CodeStream.RESTART_IN_WIDE_MODE) {
				// a branch target required a goto_w, restart code gen in wide mode.
				try {
					if (statements != null) {
						for (int i = 0, max = statements.length; i < max; i++)
							statements[i].resetStateForCodeGeneration();
					}
					classFile.contentsOffset = problemResetPC;
					classFile.methodCount--;
					classFile.codeStream.wideMode = true; // request wide mode 
					this.internalGenerateCode(classScope, classFile); // restart method generation
				} catch (AbortMethod e2) {
					int problemsLength;
					IProblem[] problems =
						scope.referenceCompilationUnit().compilationResult.getAllProblems();
					IProblem[] problemsCopy = new IProblem[problemsLength = problems.length];
					System.arraycopy(problems, 0, problemsCopy, 0, problemsLength);
					classFile.addProblemConstructor(this, binding, problemsCopy, problemResetPC);
				}
			} else {
				int problemsLength;
				IProblem[] problems =
					scope.referenceCompilationUnit().compilationResult.getAllProblems();
				IProblem[] problemsCopy = new IProblem[problemsLength = problems.length];
				System.arraycopy(problems, 0, problemsCopy, 0, problemsLength);
				classFile.addProblemConstructor(this, binding, problemsCopy, problemResetPC);
			}
		}
	}

	public void generateSyntheticFieldInitializationsIfNecessary(
		MethodScope scope,
		CodeStream codeStream,
		ReferenceBinding declaringClass) {
			
		if (!declaringClass.isNestedType()) return;
		
		NestedTypeBinding nestedType = (NestedTypeBinding) declaringClass;

		SyntheticArgumentBinding[] syntheticArgs = nestedType.syntheticEnclosingInstances();
		for (int i = 0, max = syntheticArgs == null ? 0 : syntheticArgs.length; i < max; i++) {
			SyntheticArgumentBinding syntheticArg;
			if ((syntheticArg = syntheticArgs[i]).matchingField != null) {
				codeStream.aload_0();
				codeStream.load(syntheticArg);
				codeStream.putfield(syntheticArg.matchingField);
			}
		}
		syntheticArgs = nestedType.syntheticOuterLocalVariables();
		for (int i = 0, max = syntheticArgs == null ? 0 : syntheticArgs.length; i < max; i++) {
			SyntheticArgumentBinding syntheticArg;
			if ((syntheticArg = syntheticArgs[i]).matchingField != null) {
				codeStream.aload_0();
				codeStream.load(syntheticArg);
				codeStream.putfield(syntheticArg.matchingField);
			}
		}
	}

	private void internalGenerateCode(ClassScope classScope, ClassFile classFile) {
		
		classFile.generateMethodInfoHeader(binding);
		int methodAttributeOffset = classFile.contentsOffset;
		int attributeNumber = classFile.generateMethodInfoAttribute(binding);
		if ((!binding.isNative()) && (!binding.isAbstract())) {
			
			TypeDeclaration declaringType = classScope.referenceContext;
			int codeAttributeOffset = classFile.contentsOffset;
			classFile.generateCodeAttributeHeader();
			CodeStream codeStream = classFile.codeStream;
			codeStream.reset(this, classFile);

			// initialize local positions - including initializer scope.
			ReferenceBinding declaringClass = binding.declaringClass;

			int argSlotSize = 1; // this==aload0
			
			if (declaringClass.isNestedType()){
				NestedTypeBinding nestedType = (NestedTypeBinding) declaringClass;
				this.scope.extraSyntheticArguments = nestedType.syntheticOuterLocalVariables();
				scope.computeLocalVariablePositions(// consider synthetic arguments if any
					nestedType.enclosingInstancesSlotSize + 1,
					codeStream);
				argSlotSize += nestedType.enclosingInstancesSlotSize;
				argSlotSize += nestedType.outerLocalVariablesSlotSize;
			} else {
				scope.computeLocalVariablePositions(1,  codeStream);
			}
				
			if (arguments != null) {
				for (int i = 0, max = arguments.length; i < max; i++) {
					// arguments initialization for local variable debug attributes
					LocalVariableBinding argBinding;
					codeStream.addVisibleLocalVariable(argBinding = arguments[i].binding);
					argBinding.recordInitializationStartPC(0);
					TypeBinding argType;
					if ((argType = argBinding.type) == LongBinding || (argType == DoubleBinding)) {
						argSlotSize += 2;
					} else {
						argSlotSize++;
					}
				}
			}
			
			MethodScope initializerScope = declaringType.initializerScope;
			initializerScope.computeLocalVariablePositions(argSlotSize, codeStream); // offset by the argument size (since not linked to method scope)

			boolean needFieldInitializations = constructorCall == null || constructorCall.accessMode != ExplicitConstructorCall.This;

			// post 1.4 source level, synthetic initializations occur prior to explicit constructor call
			boolean preInitSyntheticFields = scope.environment().options.targetJDK >= CompilerOptions.JDK1_4;

			if (needFieldInitializations && preInitSyntheticFields){
				generateSyntheticFieldInitializationsIfNecessary(scope, codeStream, declaringClass);
			}			
			// generate constructor call
			if (constructorCall != null) {
				constructorCall.generateCode(scope, codeStream);
			}
			// generate field initialization - only if not invoking another constructor call of the same class
			if (needFieldInitializations) {
				if (!preInitSyntheticFields){
					generateSyntheticFieldInitializationsIfNecessary(scope, codeStream, declaringClass);
				}
				// generate user field initialization
				if (declaringType.fields != null) {
					for (int i = 0, max = declaringType.fields.length; i < max; i++) {
						FieldDeclaration fieldDecl;
						if (!(fieldDecl = declaringType.fields[i]).isStatic()) {
							fieldDecl.generateCode(initializerScope, codeStream);
						}
					}
				}
			}
			// generate statements
			if (statements != null) {
				for (int i = 0, max = statements.length; i < max; i++) {
					statements[i].generateCode(scope, codeStream);
				}
			}
			if (this.needFreeReturn) {
				codeStream.return_();
			}
			// local variable attributes
			codeStream.exitUserScope(scope);
			codeStream.recordPositionsFrom(0, this.bodyEnd);
			classFile.completeCodeAttribute(codeAttributeOffset);
			attributeNumber++;
		}
		classFile.completeMethodInfo(methodAttributeOffset, attributeNumber);

		// if a problem got reported during code gen, then trigger problem method creation
		if (ignoreFurtherInvestigation) {
			throw new AbortMethod(scope.referenceCompilationUnit().compilationResult);
		}
	}

	public boolean isConstructor() {

		return true;
	}

	public boolean isDefaultConstructor() {

		return isDefaultConstructor;
	}

	public boolean isInitializationMethod() {

		return true;
	}

	/**
	 * Returns true if the constructor is directly involved in a cycle.
	 * Given most constructors aren't, we only allocate the visited list
	 * lazily.
	 */
	public boolean isRecursive(ArrayList visited) {

		if (this.binding == null
 this.constructorCall == null
 this.constructorCall.binding == null
 this.constructorCall.isSuperAccess()
 !this.constructorCall.binding.isValidBinding()) {
			return false;
		}
		
		ConstructorDeclaration targetConstructor = 
			((ConstructorDeclaration)this.scope.referenceType().declarationOf(constructorCall.binding));
		if (this == targetConstructor) return true; // direct case

		if (visited == null) { // lazy allocation
			visited = new ArrayList(1);
		} else {
			int index = visited.indexOf(this);
			if (index >= 0) return index == 0; // only blame if directly part of the cycle
		}
		visited.add(this);

		return targetConstructor.isRecursive(visited);
	}
	
	public void parseStatements(Parser parser, CompilationUnitDeclaration unit) {

		//fill up the constructor body with its statements
		if (ignoreFurtherInvestigation)
			return;
		if (isDefaultConstructor){
			constructorCall = SuperReference.implicitSuperConstructorCall();
			constructorCall.sourceStart = sourceStart;
			constructorCall.sourceEnd = sourceEnd; 
			return;
		}
		parser.parse(this, unit);

	}

	/*
	 * Type checking for constructor, just another method, except for special check
	 * for recursive constructor invocations.
	 */
	public void resolveStatements(ClassScope upperScope) {

		if (!CharOperation.equals(scope.enclosingSourceType().sourceName, selector)){
			scope.problemReporter().missingReturnType(this);
		}

		// if null ==> an error has occurs at parsing time ....
		if (this.constructorCall != null) {
			// e.g. using super() in java.lang.Object
			if (this.binding != null
				&& this.binding.declaringClass.id == T_Object
				&& this.constructorCall.accessMode != ExplicitConstructorCall.This) {
					if (this.constructorCall.accessMode == ExplicitConstructorCall.Super) {
						scope.problemReporter().cannotUseSuperInJavaLangObject(this.constructorCall);
					}
					this.constructorCall = null;
			} else {
				this.constructorCall.resolve(this.scope);
			}
		}
		
		super.resolveStatements(upperScope);
	}

	public String toStringStatements(int tab) {

		String s = " {"; //$NON-NLS-1$
		if (constructorCall != null) {
			s = s + "\n" + constructorCall.toString(tab) + ";"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (statements != null) {
			for (int i = 0; i < statements.length; i++) {
				s = s + "\n" + statements[i].toString(tab); //$NON-NLS-1$
				if (!(statements[i] instanceof Block)) {
					s += ";"; //$NON-NLS-1$
				}
			}
		}
		s += "\n" + tabString(tab == 0 ? 0 : tab - 1) + "}"; //$NON-NLS-1$ //$NON-NLS-2$
		//$NON-NLS-2$ //$NON-NLS-1$
		return s;
	}

	public void traverse(
		IAbstractSyntaxTreeVisitor visitor,
		ClassScope classScope) {

		if (visitor.visit(this, classScope)) {
			if (arguments != null) {
				int argumentLength = arguments.length;
				for (int i = 0; i < argumentLength; i++)
					arguments[i].traverse(visitor, scope);
			}
			if (thrownExceptions != null) {
				int thrownExceptionsLength = thrownExceptions.length;
				for (int i = 0; i < thrownExceptionsLength; i++)
					thrownExceptions[i].traverse(visitor, scope);
			}
			if (constructorCall != null)
				constructorCall.traverse(visitor, scope);
			if (statements != null) {
				int statementsLength = statements.length;
				for (int i = 0; i < statementsLength; i++)
					statements[i].traverse(visitor, scope);
			}
		}
		visitor.endVisit(this, classScope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/800.java