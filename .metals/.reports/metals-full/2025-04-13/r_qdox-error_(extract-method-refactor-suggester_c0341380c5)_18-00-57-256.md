error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6834.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6834.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6834.java
text:
```scala
t@@his.actualReceiverType = scope.environment().convertToRawType(this.receiver.resolvedType);

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

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.lookup.*;


public class JavadocMessageSend extends MessageSend {

	public int tagSourceStart, tagSourceEnd;
	public int tagValue;
	public boolean superAccess = false;

	public JavadocMessageSend(char[] name, long pos) {
		this.selector = name;
		this.nameSourcePosition = pos;
		this.sourceStart = (int) (this.nameSourcePosition >>> 32);
		this.sourceEnd = (int) this.nameSourcePosition;
		this.bits |= InsideJavadoc;
	}
	public JavadocMessageSend(char[] name, long pos, JavadocArgumentExpression[] arguments) {
		this(name, pos);
		this.arguments = arguments;
	}

	/*
	 * Resolves type on a Block or Class scope.
	 */
	private TypeBinding internalResolveType(Scope scope) {
		// Answer the signature return type
		// Base type promotion
		this.constant = NotAConstant;
		if (this.receiver == null) {
			this.actualReceiverType = scope.enclosingSourceType();
		} else if (scope.kind == Scope.CLASS_SCOPE) {
			this.actualReceiverType = this.receiver.resolveType((ClassScope) scope);
		} else {
			this.actualReceiverType = this.receiver.resolveType((BlockScope) scope);
		}

		// will check for null after args are resolved
		
		TypeBinding[] argumentTypes = NoParameters;
		boolean hasArgsTypeVar = false;
		if (this.arguments != null) {
			boolean argHasError = false; // typeChecks all arguments 
			int length = this.arguments.length;
			argumentTypes = new TypeBinding[length];
			for (int i = 0; i < length; i++){
				Expression argument = this.arguments[i];
				if (scope.kind == Scope.CLASS_SCOPE) {
					argumentTypes[i] = argument.resolveType((ClassScope)scope);
				} else {
					argumentTypes[i] = argument.resolveType((BlockScope)scope);
				}
				if (argumentTypes[i] == null) {
					argHasError = true;
				} else if (!hasArgsTypeVar) {
					hasArgsTypeVar = argumentTypes[i].isTypeVariable();
				}
			}
			if (argHasError) {
				return null;
			}
		}

		// check receiver type
		if (this.actualReceiverType == null) {
			return null;
		}
		this.actualReceiverType = scope.convertToRawType(this.receiver.resolvedType);
		SourceTypeBinding enclosingType = scope.enclosingSourceType();
		this.superAccess = enclosingType==null ? false : enclosingType.isCompatibleWith(this.actualReceiverType);

		// base type cannot receive any message
		if (this.actualReceiverType.isBaseType()) {
			scope.problemReporter().javadocErrorNoMethodFor(this, this.actualReceiverType, argumentTypes, scope.getDeclarationModifiers());
			return null;
		}
		this.binding = scope.getMethod(this.actualReceiverType, this.selector, argumentTypes, this);
		if (!this.binding.isValidBinding()) {
			// Try method in enclosing types
			TypeBinding enclosingTypeBinding = this.actualReceiverType;
			MethodBinding methodBinding = this.binding;
			while (!methodBinding.isValidBinding() && (enclosingTypeBinding.isMemberType() || enclosingTypeBinding.isLocalType())) {
				enclosingTypeBinding = enclosingTypeBinding.enclosingType();
				methodBinding = scope.getMethod(enclosingTypeBinding, this.selector, argumentTypes, this);
			}
			if (methodBinding.isValidBinding()) {
				this.binding = methodBinding;
			} else {
				// Try to search a constructor instead
				enclosingTypeBinding = this.actualReceiverType;
				MethodBinding contructorBinding = this.binding;
				while (!contructorBinding.isValidBinding() && (enclosingTypeBinding.isMemberType() || enclosingTypeBinding.isLocalType())) {
					enclosingTypeBinding = enclosingTypeBinding.enclosingType();
					if (CharOperation.equals(this.selector, enclosingTypeBinding.shortReadableName())) {
						contructorBinding = scope.getConstructor((ReferenceBinding)enclosingTypeBinding, argumentTypes, this);
					}
				}
				if (contructorBinding.isValidBinding()) {
					this.binding = contructorBinding;
				}
			}
		}
		if (!this.binding.isValidBinding()) {
			// implicit lookup may discover issues due to static/constructor contexts. javadoc must be resilient
			switch (this.binding.problemId()) {
				case ProblemReasons.NonStaticReferenceInConstructorInvocation:
				case ProblemReasons.NonStaticReferenceInStaticContext:
				case ProblemReasons.InheritedNameHidesEnclosingName : 
					MethodBinding closestMatch = ((ProblemMethodBinding)this.binding).closestMatch;
					if (closestMatch != null) {
						this.binding = closestMatch; // ignore problem if can reach target method through it
					}
			}
		}
		if (!this.binding.isValidBinding()) {
			if (this.binding.declaringClass == null) {
				if (this.actualReceiverType instanceof ReferenceBinding) {
					this.binding.declaringClass = (ReferenceBinding) this.actualReceiverType;
				} else { 
					scope.problemReporter().javadocErrorNoMethodFor(this, this.actualReceiverType, argumentTypes, scope.getDeclarationModifiers());
					return null;
				}
			}
			scope.problemReporter().javadocInvalidMethod(this, this.binding, scope.getDeclarationModifiers());
			// record the closest match, for clients who may still need hint about possible method match
			if (this.binding instanceof ProblemMethodBinding){
				MethodBinding closestMatch = ((ProblemMethodBinding)this.binding).closestMatch;
				if (closestMatch != null) this.binding = closestMatch;
			}
			return this.resolvedType = this.binding == null ? null : this.binding.returnType;
		} else if (hasArgsTypeVar) {
			MethodBinding problem = new ProblemMethodBinding(this.binding, this.selector, argumentTypes, ProblemReasons.NotFound);
			scope.problemReporter().javadocInvalidMethod(this, problem, scope.getDeclarationModifiers());
		} else if (binding.isVarargs()) {
			int length = argumentTypes.length;
			if (!(binding.parameters.length == length && argumentTypes[length-1].isArrayType())) {
				MethodBinding problem = new ProblemMethodBinding(this.binding, this.selector, argumentTypes, ProblemReasons.NotFound);
				scope.problemReporter().javadocInvalidMethod(this, problem, scope.getDeclarationModifiers());
			}
		} else if (this.binding instanceof ParameterizedMethodBinding && this.actualReceiverType instanceof ReferenceBinding) {
			ParameterizedMethodBinding paramMethodBinding = (ParameterizedMethodBinding) this.binding;
			if (paramMethodBinding.hasSubstitutedParameters()) {
				int length = argumentTypes.length;
				for (int i=0; i<length; i++) {
					if (paramMethodBinding.parameters[i] != argumentTypes[i] &&
							paramMethodBinding.parameters[i].erasure() != argumentTypes[i].erasure()) {
						MethodBinding problem = new ProblemMethodBinding(this.binding, this.selector, argumentTypes, ProblemReasons.NotFound);
						scope.problemReporter().javadocInvalidMethod(this, problem, scope.getDeclarationModifiers());
						break;
					}
				}
			}
		}
		if (isMethodUseDeprecated(this.binding, scope)) {
			scope.problemReporter().javadocDeprecatedMethod(this.binding, this, scope.getDeclarationModifiers());
		}

		return this.resolvedType = this.binding.returnType;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.lookup.InvocationSite#isSuperAccess()
	 */
	public boolean isSuperAccess() {
		return this.superAccess;
	}

	public StringBuffer printExpression(int indent, StringBuffer output){
	
		if (this.receiver != null) {
			this.receiver.printExpression(0, output);
		}
		output.append('#').append(this.selector).append('(');
		if (this.arguments != null) {
			for (int i = 0; i < this.arguments.length ; i ++) {	
				if (i > 0) output.append(", "); //$NON-NLS-1$
				this.arguments[i].printExpression(0, output);
			}
		}
		return output.append(')');
	}

	public TypeBinding resolveType(BlockScope scope) {
		return internalResolveType(scope);
	}

	public TypeBinding resolveType(ClassScope scope) {
		return internalResolveType(scope);
	}

	/* (non-Javadoc)
	 * Redefine to capture javadoc specific signatures
	 * @see org.eclipse.jdt.internal.compiler.ast.ASTNode#traverse(org.eclipse.jdt.internal.compiler.ASTVisitor, org.eclipse.jdt.internal.compiler.lookup.BlockScope)
	 */
	public void traverse(ASTVisitor visitor, BlockScope blockScope) {
		if (visitor.visit(this, blockScope)) {
			if (this.receiver != null) {
				this.receiver.traverse(visitor, blockScope);
			}
			if (this.arguments != null) {
				int argumentsLength = this.arguments.length;
				for (int i = 0; i < argumentsLength; i++)
					this.arguments[i].traverse(visitor, blockScope);
			}
		}
		visitor.endVisit(this, blockScope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6834.java