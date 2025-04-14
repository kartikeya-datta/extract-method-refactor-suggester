error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5283.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5283.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5283.java
text:
```scala
t@@his.resolvedType = scope.environment().convertToRawType(this.type.resolvedType);

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

import org.eclipse.jdt.internal.compiler.lookup.*;

public class JavadocAllocationExpression extends AllocationExpression {

	public int tagSourceStart, tagSourceEnd;
	public int tagValue;
	public boolean superAccess = false;
	
	public JavadocAllocationExpression(int start, int end) {
		this.sourceStart = start;
		this.sourceEnd = end;
		this.bits |= InsideJavadoc;
	}
	public JavadocAllocationExpression(long pos) {
		this((int) (pos >>> 32), (int) pos);
	}

	private TypeBinding internalResolveType(Scope scope) {
	
		// Propagate the type checking to the arguments, and check if the constructor is defined.
		this.constant = NotAConstant;
		if (this.type == null) {
			this.resolvedType = scope.enclosingSourceType();
		} else if (scope.kind == Scope.CLASS_SCOPE) {
			this.resolvedType = this.type.resolveType((ClassScope)scope);
		} else {
			this.resolvedType = this.type.resolveType((BlockScope)scope, true /* check bounds*/);
		}
	
		// buffering the arguments' types
		TypeBinding[] argumentTypes = NoParameters;
		boolean hasTypeVarArgs = false;
		if (this.arguments != null) {
			boolean argHasError = false;
			int length = this.arguments.length;
			argumentTypes = new TypeBinding[length];
			for (int i = 0; i < length; i++) {
				Expression argument = this.arguments[i];
				if (scope.kind == Scope.CLASS_SCOPE) {
					argumentTypes[i] = argument.resolveType((ClassScope)scope);
				} else {
					argumentTypes[i] = argument.resolveType((BlockScope)scope);
				}
				if (argumentTypes[i] == null) {
					argHasError = true;
				} else if (!hasTypeVarArgs) {
					hasTypeVarArgs = argumentTypes[i].isTypeVariable();
				}
			}
			if (argHasError) {
				return null;
			}
		}
	
		// check resolved type
		if (this.resolvedType == null) {
			return null;
		}
		this.resolvedType = scope.convertToRawType(this.type.resolvedType);
		SourceTypeBinding enclosingType = scope.enclosingSourceType();
		this.superAccess = enclosingType==null ? false : enclosingType.isCompatibleWith(this.resolvedType);
	
		ReferenceBinding allocationType = (ReferenceBinding) this.resolvedType;
		this.binding = scope.getConstructor(allocationType, argumentTypes, this);
		if (!this.binding.isValidBinding()) {
			ReferenceBinding enclosingTypeBinding = allocationType;
			MethodBinding contructorBinding = this.binding;
			while (!contructorBinding.isValidBinding() && (enclosingTypeBinding.isMemberType() || enclosingTypeBinding.isLocalType())) {
				enclosingTypeBinding = enclosingTypeBinding.enclosingType();
				contructorBinding = scope.getConstructor(enclosingTypeBinding, argumentTypes, this);
			}
			if (contructorBinding.isValidBinding()) {
				this.binding = contructorBinding;
			}
		}
		if (!this.binding.isValidBinding()) {
			// First try to search a method instead
			MethodBinding methodBinding = scope.getMethod(this.resolvedType, this.resolvedType.sourceName(), argumentTypes, this);
			if (methodBinding.isValidBinding()) {
				this.binding = methodBinding;
			} else {
				if (this.binding.declaringClass == null) {
					this.binding.declaringClass = allocationType;
				}
				scope.problemReporter().javadocInvalidConstructor(this, this.binding, scope.getDeclarationModifiers());
			}
			return this.resolvedType;
		} else if (binding.isVarargs()) {
			int length = argumentTypes.length;
			if (!(binding.parameters.length == length && argumentTypes[length-1].isArrayType())) {
				MethodBinding problem = new ProblemMethodBinding(this.binding, this.binding.selector, argumentTypes, ProblemReasons.NotFound);
				scope.problemReporter().javadocInvalidConstructor(this, problem, scope.getDeclarationModifiers());
			}
		} else if (hasTypeVarArgs) {
			MethodBinding problem = new ProblemMethodBinding(this.binding, this.binding.selector, argumentTypes, ProblemReasons.NotFound);
			scope.problemReporter().javadocInvalidConstructor(this, problem, scope.getDeclarationModifiers());
		} else if (this.binding instanceof ParameterizedMethodBinding) {
			ParameterizedMethodBinding paramMethodBinding = (ParameterizedMethodBinding) this.binding;
			if (paramMethodBinding.hasSubstitutedParameters()) {
				int length = argumentTypes.length;
				for (int i=0; i<length; i++) {
					if (paramMethodBinding.parameters[i] != argumentTypes[i] &&
							paramMethodBinding.parameters[i].erasure() != argumentTypes[i].erasure()) {
						MethodBinding problem = new ProblemMethodBinding(this.binding, this.binding.selector, argumentTypes, ProblemReasons.NotFound);
						scope.problemReporter().javadocInvalidConstructor(this, problem, scope.getDeclarationModifiers());
						break;
					}
				}
			}
		}
		if (isMethodUseDeprecated(this.binding, scope)) {
			scope.problemReporter().javadocDeprecatedMethod(this.binding, this, scope.getDeclarationModifiers());
		}
		return allocationType;
	}

	public boolean isSuperAccess() {
		return this.superAccess;
	}

	public TypeBinding resolveType(BlockScope scope) {
		return internalResolveType(scope);
	}

	public TypeBinding resolveType(ClassScope scope) {
		return internalResolveType(scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5283.java