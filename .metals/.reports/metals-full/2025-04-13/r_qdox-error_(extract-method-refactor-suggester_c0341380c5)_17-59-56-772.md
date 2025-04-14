error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5423.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5423.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5423.java
text:
```scala
c@@d.scope = new MethodScope(scope, cd, true);

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

import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.*;

public class AnonymousLocalTypeDeclaration extends LocalTypeDeclaration {

	public static final char[] ANONYMOUS_EMPTY_NAME = new char[] {};
	public QualifiedAllocationExpression allocation;

	public AnonymousLocalTypeDeclaration(CompilationResult compilationResult) {
		super(compilationResult);
		modifiers = AccDefault;
		name = ANONYMOUS_EMPTY_NAME;
	} 
	
	// use a default name in order to th name lookup 
	// to operate juat like a regular type (which has a name)
	//without checking systematically if the naem is null .... 
	public MethodBinding createsInternalConstructorWithBinding(MethodBinding inheritedConstructorBinding) {

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
		cd.modifiers = modifiers & AccVisibilityMASK;
		cd.isDefaultConstructor = true;

		if (argumentsLength > 0) {
			Argument[] arguments = (cd.arguments = new Argument[argumentsLength]);
			for (int i = argumentsLength; --i >= 0;) {
				arguments[i] = new Argument((baseName + i).toCharArray(), 0L, null /*type ref*/, AccDefault);
			}
		}

		//the super call inside the constructor
		cd.constructorCall =
			new ExplicitConstructorCall(ExplicitConstructorCall.ImplicitSuper);
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
				argumentsLength == 0 ? NoParameters : argumentTypes, //arguments bindings
				inheritedConstructorBinding.thrownExceptions, //exceptions
				binding); //declaringClass
				
		cd.scope = new MethodScope(scope, this, true);
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
	public void resolve(BlockScope scope) {

		// scope and binding are provided in updateBindingSuperclass 
		resolve();
		updateMaxFieldCount();
	}

	public String toString(int tab) {

		return toStringBody(tab);
	}

	/**
	 *	Iteration for a local anonymous innertype
	 *
	 */
	public void traverse(
		IAbstractSyntaxTreeVisitor visitor,
		BlockScope blockScope) {

		if (ignoreFurtherInvestigation)
			return;
		try {
			if (visitor.visit(this, blockScope)) {

				int fieldsLength;
				int methodsLength;
				int memberTypesLength;

				// <superclass> is bound to the actual type from the allocation expression
				// therefore it has already been iterated at this point.

				if (memberTypes != null) {
					memberTypesLength = memberTypes.length;
					for (int i = 0; i < memberTypesLength; i++)
						memberTypes[i].traverse(visitor, scope);
				}
				if (fields != null) {
					fieldsLength = fields.length;
					for (int i = 0; i < fieldsLength; i++) {
						FieldDeclaration field;
						if ((field = fields[i]).isStatic()) {
							// local type cannot have static fields
						} else {
							field.traverse(visitor, initializerScope);
						}
					}
				}
				if (methods != null) {
					methodsLength = methods.length;
					for (int i = 0; i < methodsLength; i++)
						methods[i].traverse(visitor, scope);
				}
			}
			visitor.endVisit(this, blockScope);
		} catch (AbortType e) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5423.java