error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4380.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4380.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4380.java
text:
```scala
i@@f (paramNameReference.token == null || paramNameReference.token.length == 0) {

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
package org.eclipse.jdt.internal.codeassist.complete;

import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

/**
 * Node representing a Javadoc comment including code selection.
 */
public class CompletionJavadoc extends Javadoc {

	Expression completionNode;

	public CompletionJavadoc(int sourceStart, int sourceEnd) {
		super(sourceStart, sourceEnd);
	}

	/**
	 * @return Returns the completionNode.
	 */
	public Expression getCompletionNode() {
		return this.completionNode;
	}

	/**
	 * Resolve selected node if not null and throw exception to let clients know
	 * that it has been found.
	 * 
	 * @throws CompletionNodeFound
	 */
	private void internalResolve(Scope scope) {
		if (this.completionNode != null) {
			if (this.completionNode instanceof CompletionOnJavadocTag) {
				((CompletionOnJavadocTag)this.completionNode).filterPossibleTags(scope);
			} else {
				boolean resolve = true;
				if (this.completionNode instanceof CompletionOnJavadocParamNameReference) {
					resolve = ((CompletionOnJavadocParamNameReference)this.completionNode).token != null;
				} else if (this.completionNode instanceof CompletionOnJavadocTypeParamReference) {
					resolve = ((CompletionOnJavadocTypeParamReference)this.completionNode).token != null;
				}
				if (resolve) {
					switch (scope.kind) {
						case Scope.CLASS_SCOPE:
							this.completionNode.resolveType((ClassScope)scope);
							break;
						case Scope.METHOD_SCOPE:
							this.completionNode.resolveType((MethodScope) scope);
							break;
					}
				}
				if (this.completionNode instanceof CompletionOnJavadocParamNameReference) {
					CompletionOnJavadocParamNameReference paramNameReference = (CompletionOnJavadocParamNameReference) this.completionNode;
					if (scope.kind == Scope.METHOD_SCOPE) {
						paramNameReference.missingParams = missingParamTags(paramNameReference.binding, (MethodScope)scope);
					}
					if (paramNameReference.token == null) {
						paramNameReference.missingTypeParams = missingTypeParameterTags(paramNameReference.binding, scope);
					}
				} else if (this.completionNode instanceof CompletionOnJavadocTypeParamReference) {
					CompletionOnJavadocTypeParamReference typeParamReference = (CompletionOnJavadocTypeParamReference) this.completionNode;
					typeParamReference.missingParams = missingTypeParameterTags(typeParamReference.resolvedType, scope);
				}
			}
			Binding qualifiedBinding = null;
			if (this.completionNode instanceof CompletionOnJavadocQualifiedTypeReference) {
				CompletionOnJavadocQualifiedTypeReference typeRef = (CompletionOnJavadocQualifiedTypeReference) this.completionNode;
				if (typeRef.packageBinding == null) {
					qualifiedBinding = typeRef.resolvedType;
				} else {
					qualifiedBinding = typeRef.packageBinding;
				}
			} else if (this.completionNode instanceof CompletionOnJavadocMessageSend) {
				CompletionOnJavadocMessageSend msg = (CompletionOnJavadocMessageSend) this.completionNode;
				if (!msg.receiver.isThis()) qualifiedBinding = msg.receiver.resolvedType;
			} else if (this.completionNode instanceof CompletionOnJavadocAllocationExpression) {
				CompletionOnJavadocAllocationExpression alloc = (CompletionOnJavadocAllocationExpression) this.completionNode;
				qualifiedBinding = alloc.type.resolvedType;
			}
			throw new CompletionNodeFound(this.completionNode, qualifiedBinding, scope);
		}
	}
	
	/*
	 * @see org.eclipse.jdt.internal.compiler.ast.ASTNode#print(int, java.lang.StringBuffer)
	 */
	public StringBuffer print(int indent, StringBuffer output) {
		printIndent(indent, output).append("/**\n"); //$NON-NLS-1$
		boolean nodePrinted = false;
		if (this.paramReferences != null) {
			for (int i = 0, length = this.paramReferences.length; i < length; i++) {
				printIndent(indent, output).append(" * @param "); //$NON-NLS-1$		
				this.paramReferences[i].print(indent, output).append('\n');
				if (!nodePrinted && this.completionNode != null) {
					nodePrinted =  this.completionNode == this.paramReferences[i];
				}
			}
		}
		if (this.paramTypeParameters != null) {
			for (int i = 0, length = this.paramTypeParameters.length; i < length; i++) {
				printIndent(indent, output).append(" * @param <"); //$NON-NLS-1$		
				this.paramTypeParameters[i].print(indent, output).append(">\n"); //$NON-NLS-1$
				if (!nodePrinted && this.completionNode != null) {
					nodePrinted =  this.completionNode == this.paramTypeParameters[i];
				}
			}
		}
		if (this.returnStatement != null) {
			printIndent(indent, output).append(" * @"); //$NON-NLS-1$
			this.returnStatement.print(indent, output).append('\n');
		}
		if (this.exceptionReferences != null) {
			for (int i = 0, length = this.exceptionReferences.length; i < length; i++) {
				printIndent(indent, output).append(" * @throws "); //$NON-NLS-1$		
				this.exceptionReferences[i].print(indent, output).append('\n');
				if (!nodePrinted && this.completionNode != null) {
					nodePrinted =  this.completionNode == this.exceptionReferences[i];
				}
			}
		}
		if (this.seeReferences != null) {
			for (int i = 0, length = this.seeReferences.length; i < length; i++) {
				printIndent(indent, output).append(" * @see "); //$NON-NLS-1$		
				this.seeReferences[i].print(indent, output).append('\n');
				if (!nodePrinted && this.completionNode != null) {
					nodePrinted =  this.completionNode == this.seeReferences[i];
				}
			}
		}
		if (!nodePrinted && this.completionNode != null) {
			printIndent(indent, output).append(" * "); //$NON-NLS-1$		
			this.completionNode.print(indent, output).append('\n');
		}
		printIndent(indent, output).append(" */\n"); //$NON-NLS-1$
		return output;
	}

	/**
	 * Resolve completion node if not null and throw exception to let clients know
	 * that it has been found.
	 * 
	 * @throws CompletionNodeFound
	 */
	public void resolve(ClassScope scope) {
		super.resolve(scope);
		internalResolve(scope);
	}

	/**
	 * Resolve completion node if not null and throw exception to let clients know
	 * that it has been found.
	 * 
	 * @throws CompletionNodeFound
	 */
	public void resolve(CompilationUnitScope scope) {
		internalResolve(scope);
	}

	/**
	 * Resolve completion node if not null and throw exception to let clients know
	 * that it has been found.
	 * 
	 * @throws CompletionNodeFound
	 */
	public void resolve(MethodScope scope) {
		super.resolve(scope);
		internalResolve(scope);
	}

	/*
	 * Look for missing method @param tags
	 */
	private char[][] missingParamTags(Binding paramNameRefBinding, MethodScope methScope) {

		// Verify if there's some possible param tag
		AbstractMethodDeclaration md = methScope.referenceMethod();
		int paramTagsSize = this.paramReferences == null ? 0 : this.paramReferences.length;
		if (md == null) return null;
		int argumentsSize = md.arguments == null ? 0 : md.arguments.length;
		if (argumentsSize == 0) return null;
		
		// Store all method arguments if there's no @param in javadoc
		if (paramTagsSize == 0) {
			char[][] missingParams = new char[argumentsSize][];
			for (int i = 0; i < argumentsSize; i++) {
				missingParams[i] = md.arguments[i].name;
			}
			return missingParams;
		}

		// Look for missing arguments
		char[][] missingParams = new char[argumentsSize][];
		int size = 0;
		for (int i = 0; i < argumentsSize; i++) {
			Argument arg = md.arguments[i];
			boolean found = false;
			int paramNameRefCount = 0;
			for (int j = 0; j < paramTagsSize && !found; j++) {
				JavadocSingleNameReference param = this.paramReferences[j];
				if (arg.binding == param.binding) {
					if (param.binding == paramNameRefBinding) { // do not count first occurence of param name reference
						paramNameRefCount++;
						found = paramNameRefCount > 1;
					} else {
						found = true;
					}
				}
			}
			if (!found) {
				missingParams[size++] = arg.name;
			}
		}
		if (size > 0) {
			if (size != argumentsSize) {
				System.arraycopy(missingParams, 0, missingParams = new char[size][], 0, size);
			}
			return missingParams;
		}
		return null;
	}

	/*
	 * Look for missing type parameters @param tags
	 */
	private char[][] missingTypeParameterTags(Binding paramNameRefBinding, Scope scope) {
		int paramTypeParamLength = this.paramTypeParameters == null ? 0 : this.paramTypeParameters.length;

		// Verify if there's any type parameter to tag
		TypeDeclaration typeDeclaration = null;
		AbstractMethodDeclaration methodDeclaration = null;
		TypeVariableBinding[] typeVariables = null;
		switch (scope.kind) {
			case Scope.METHOD_SCOPE:
				methodDeclaration = ((MethodScope)scope).referenceMethod();
				if (methodDeclaration == null) return null;
				typeVariables = methodDeclaration.binding.typeVariables;
				break;
			case Scope.CLASS_SCOPE:
				typeDeclaration = ((ClassScope) scope).referenceContext;
				typeVariables = typeDeclaration.binding.typeVariables;
				break;
		}
		if (typeVariables == null || typeVariables.length == 0) return null;
		
		// Store all type parameters if there's no @param in javadoc
		TypeParameter[] parameters = typeDeclaration==null ? methodDeclaration.typeParameters() : typeDeclaration.typeParameters;
		int typeParametersLength = parameters == null ? 0 : parameters.length;
		if (paramTypeParamLength == 0) {
			char[][] missingParams = new char[typeParametersLength][];
			for (int i = 0; i < typeParametersLength; i++) {
				missingParams[i] = parameters[i].name;
			}
			return missingParams;
		}

		// Look for missing type parameter
		char[][] missingParams = new char[typeParametersLength][];
		int size = 0;
		for (int i = 0; i < typeParametersLength; i++) {
			TypeParameter parameter = parameters[i];
			boolean found = false;
			int paramNameRefCount = 0;
			for (int j = 0; j < paramTypeParamLength && !found; j++) {
				if (parameter.binding == this.paramTypeParameters[j].resolvedType) {
					if (parameter.binding == paramNameRefBinding) { // do not count first occurence of param nmae reference
						paramNameRefCount++;
						found = paramNameRefCount > 1;
					} else {
						found = true;
					}
				}
			}
			if (!found) {
				missingParams[size++] = parameter.name;
			}
		}
		if (size > 0) {
			if (size != typeParametersLength) {
				System.arraycopy(missingParams, 0, missingParams = new char[size][], 0, size);
			}
			return missingParams;
		}
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4380.java