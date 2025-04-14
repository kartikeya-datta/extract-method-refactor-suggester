error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9417.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9417.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9417.java
text:
```scala
r@@eportDeprecatedType(this.resolvedType, scope, i);

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;

public class QualifiedTypeReference extends TypeReference {

	public char[][] tokens;
	public long[] sourcePositions;

	public QualifiedTypeReference(char[][] sources , long[] poss) {

		this.tokens = sources ;
		this.sourcePositions = poss ;
		this.sourceStart = (int) (this.sourcePositions[0]>>>32) ;
		this.sourceEnd = (int)(this.sourcePositions[this.sourcePositions.length-1] & 0x00000000FFFFFFFFL ) ;
	}

	public TypeReference copyDims(int dim){
		//return a type reference copy of me with some dimensions
		//warning : the new type ref has a null binding
		return new ArrayQualifiedTypeReference(this.tokens, dim, this.sourcePositions);
	}

	protected TypeBinding findNextTypeBinding(int tokenIndex, Scope scope, PackageBinding packageBinding) {
		LookupEnvironment env = scope.environment();
		try {
			env.missingClassFileLocation = this;
			if (this.resolvedType == null) {
				this.resolvedType = scope.getType(this.tokens[tokenIndex], packageBinding);
			} else {
				this.resolvedType = scope.getMemberType(this.tokens[tokenIndex], (ReferenceBinding) this.resolvedType);
				if (!this.resolvedType.isValidBinding()) {
					this.resolvedType = new ProblemReferenceBinding(
						CharOperation.subarray(this.tokens, 0, tokenIndex + 1),
						(ReferenceBinding)this.resolvedType.closestMatch(),
						this.resolvedType.problemId());
				}
			}
			return this.resolvedType;
		} catch (AbortCompilation e) {
			e.updateContext(this, scope.referenceCompilationUnit().compilationResult);
			throw e;
		} finally {
			env.missingClassFileLocation = null;
		}
	}

	public char[] getLastToken() {
		return this.tokens[this.tokens.length-1];
	}
	protected TypeBinding getTypeBinding(Scope scope) {

		if (this.resolvedType != null) {
			return this.resolvedType;
		}
		Binding binding = scope.getPackage(this.tokens);
		if (binding != null && !binding.isValidBinding()) {
			if (binding instanceof ProblemReferenceBinding && binding.problemId() == ProblemReasons.NotFound) {
				ProblemReferenceBinding problemBinding = (ProblemReferenceBinding) binding;
				Binding pkg = scope.getTypeOrPackage(this.tokens);
				return new ProblemReferenceBinding(problemBinding.compoundName, pkg instanceof PackageBinding ? null : scope.environment().createMissingType(null, this.tokens), ProblemReasons.NotFound);
			}
			return (ReferenceBinding) binding; // not found
		}
	    PackageBinding packageBinding = binding == null ? null : (PackageBinding) binding;
	    boolean isClassScope = scope.kind == Scope.CLASS_SCOPE;
	    ReferenceBinding qualifiedType = null;
		for (int i = packageBinding == null ? 0 : packageBinding.compoundName.length, max = this.tokens.length, last = max-1; i < max; i++) {
			findNextTypeBinding(i, scope, packageBinding);
			if (!this.resolvedType.isValidBinding())
				return this.resolvedType;
			if (i == 0 && this.resolvedType.isTypeVariable() && ((TypeVariableBinding) this.resolvedType).firstBound == null) { // cannot select from a type variable
				scope.problemReporter().illegalAccessFromTypeVariable((TypeVariableBinding) this.resolvedType, this);
				return null;
			}
			if (i < last && isTypeUseDeprecated(this.resolvedType, scope)) {
				reportDeprecatedType(this.resolvedType, scope);
			}
			if (isClassScope)
				if (((ClassScope) scope).detectHierarchyCycle(this.resolvedType, this)) // must connect hierarchy to find inherited member types
					return null;
			ReferenceBinding currentType = (ReferenceBinding) this.resolvedType;
			if (qualifiedType != null) {
				ReferenceBinding enclosingType = currentType.enclosingType();
				if (enclosingType != null && enclosingType.erasure() != qualifiedType.erasure()) {
					qualifiedType = enclosingType; // inherited member type, leave it associated with its enclosing rather than subtype
				}
				boolean rawQualified;
				if (currentType.isGenericType()) {
					qualifiedType = scope.environment().createRawType(currentType, qualifiedType);
				} else if ((rawQualified = qualifiedType.isRawType()) && !currentType.isStatic()) {
					qualifiedType = scope.environment().createRawType((ReferenceBinding)currentType.erasure(), qualifiedType);
				} else if ((rawQualified || qualifiedType.isParameterizedType()) && qualifiedType.erasure() == currentType.enclosingType().erasure()) {
					qualifiedType = scope.environment().createParameterizedType((ReferenceBinding)currentType.erasure(), null, qualifiedType);
				} else {
					qualifiedType = currentType;
				}
			} else {
				qualifiedType = currentType.isGenericType() ? (ReferenceBinding)scope.environment().convertToRawType(currentType, false /*do not force conversion of enclosing types*/) : currentType;
			}
		}
		this.resolvedType = qualifiedType;
		return this.resolvedType;
	}

	public char[][] getTypeName(){

		return this.tokens;
	}

	public StringBuffer printExpression(int indent, StringBuffer output) {

		for (int i = 0; i < this.tokens.length; i++) {
			if (i > 0) output.append('.');
			output.append(this.tokens[i]);
		}
		return output;
	}

	public void traverse(ASTVisitor visitor, BlockScope scope) {

		visitor.visit(this, scope);
		visitor.endVisit(this, scope);
	}

	public void traverse(ASTVisitor visitor, ClassScope scope) {

		visitor.visit(this, scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9417.java