error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7620.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7620.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7620.java
text:
```scala
t@@his.type.resolvedType = this.binding.type; // update binding for type reference

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
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class FieldDeclaration extends AbstractVariableDeclaration {
	public FieldBinding binding;
	boolean hasBeenResolved = false;

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
	}

	public FieldDeclaration(
		Expression initialization,
		char[] name,
		int sourceStart,
		int sourceEnd) {

		this.initialization = initialization;
		this.name = name;

		//due to some declaration like 
		// int x, y = 3, z , x ;
		//the sourceStart and the sourceEnd is ONLY on  the name
		this.sourceStart = sourceStart;
		this.sourceEnd = sourceEnd;
	}

	public FlowInfo analyseCode(
		MethodScope initializationScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {

		// cannot define static non-constant field inside nested class
		if (binding != null
			&& binding.isValidBinding()
			&& binding.isStatic()
			&& binding.constant == NotAConstant
			&& binding.declaringClass.isNestedType()
			&& binding.declaringClass.isClass()
			&& !binding.declaringClass.isStatic()) {
			initializationScope.problemReporter().unexpectedStaticModifierForField(
				(SourceTypeBinding) binding.declaringClass,
				this);
		}

		if (initialization != null) {
			flowInfo =
				initialization
					.analyseCode(initializationScope, flowContext, flowInfo)
					.unconditionalInits();
			flowInfo.markAsDefinitelyAssigned(binding);
		} else {
			flowInfo.markAsDefinitelyNotAssigned(binding);
			// clear the bit in case it was already set (from enclosing info)
		}
		return flowInfo;
	}

	/**
	 * Code generation for a field declaration:
	 *	i.e.&nbsp;normal assignment to a field 
	 *
	 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {

		if ((bits & IsReachableMASK) == 0) {
			return;
		}
		// do not generate initialization code if final and static (constant is then
		// recorded inside the field itself).
		int pc = codeStream.position;
		boolean isStatic;
		if (initialization != null
			&& !((isStatic = binding.isStatic()) && binding.constant != NotAConstant)) {
			// non-static field, need receiver
			if (!isStatic)
				codeStream.aload_0();
			// generate initialization value
			initialization.generateCode(currentScope, codeStream, true);
			// store into field
			if (isStatic) {
				codeStream.putstatic(binding);
			} else {
				codeStream.putfield(binding);
			}
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}

	public TypeBinding getTypeBinding(Scope scope) {

		return type.getTypeBinding(scope);
	}

	public boolean isField() {

		return true;
	}

	public boolean isStatic() {

		if (binding != null)
			return binding.isStatic();
		return (modifiers & AccStatic) != 0;
	}

	public String name() {

		return String.valueOf(name);
	}

	public void resolve(MethodScope initializationScope) {

		// the two <constant = Constant.NotAConstant> could be regrouped into
		// a single line but it is clearer to have two lines while the reason of their
		// existence is not at all the same. See comment for the second one.

		//--------------------------------------------------------
		if (!this.hasBeenResolved && binding != null && this.binding.isValidBinding()) {

			this.hasBeenResolved = true;

			if (isTypeUseDeprecated(this.binding.type, initializationScope))
				initializationScope.problemReporter().deprecatedType(this.binding.type, this.type);

			this.type.binding = this.binding.type; // update binding for type reference

			// the resolution of the initialization hasn't been done
			if (this.initialization == null) {
				this.binding.constant = Constant.NotAConstant;
			} else {
				int previous = initializationScope.fieldDeclarationIndex;
				try {
					initializationScope.fieldDeclarationIndex = this.binding.id;

					// break dead-lock cycles by forcing constant to NotAConstant
					this.binding.constant = Constant.NotAConstant;
					
					TypeBinding typeBinding = this.binding.type;
					TypeBinding initializationTypeBinding;
					
					if (initialization instanceof ArrayInitializer) {

						if ((initializationTypeBinding = this.initialization.resolveTypeExpecting(initializationScope, typeBinding)) 	!= null) {
							((ArrayInitializer) this.initialization).binding = (ArrayBinding) initializationTypeBinding;
							this.initialization.implicitWidening(typeBinding, initializationTypeBinding);
						}
					} else if ((initializationTypeBinding = initialization.resolveType(initializationScope)) != null) {

						if (this.initialization.isConstantValueOfTypeAssignableToType(initializationTypeBinding, typeBinding)
 (typeBinding.isBaseType() && BaseTypeBinding.isWidening(typeBinding.id, initializationTypeBinding.id))) {

							this.initialization.implicitWidening(typeBinding, initializationTypeBinding);

						}	else if (Scope.areTypesCompatible(initializationTypeBinding, typeBinding)) {
							this.initialization.implicitWidening(typeBinding, initializationTypeBinding);

						} else {
							initializationScope.problemReporter().typeMismatchError(initializationTypeBinding, typeBinding, this);
						}
						if (this.binding.isFinal()){ // cast from constant actual type to variable type
							this.binding.constant =
								this.initialization.constant.castTo(
									(this.binding.type.id << 4) + this.initialization.constant.typeID());
						}
					} else {
						this.binding.constant = NotAConstant;
					}
				} finally {
					initializationScope.fieldDeclarationIndex = previous;
					if (this.binding.constant == null)
						this.binding.constant = Constant.NotAConstant;
				}
			}
		}
	}

	public void traverse(IAbstractSyntaxTreeVisitor visitor, MethodScope scope) {

		if (visitor.visit(this, scope)) {
			type.traverse(visitor, scope);
			if (initialization != null)
				initialization.traverse(visitor, scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7620.java