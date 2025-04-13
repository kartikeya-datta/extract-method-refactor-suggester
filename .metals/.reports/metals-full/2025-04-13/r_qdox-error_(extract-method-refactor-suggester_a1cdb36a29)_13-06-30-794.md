error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4404.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4404.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4404.java
text:
```scala
n@@ew TypeBinding[] {  scope.environment().createWildcard(genericClassType, 0, receiverType.erasure(), null /*no extra bound*/, Wildcard.EXTENDS) },

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
package org.eclipse.jdt.internal.compiler.lookup;

import org.eclipse.jdt.internal.compiler.ast.Wildcard;

/**
 * Binding denoting a method after type parameter substitutions got performed.
 * On parameterized type bindings, all methods got substituted, regardless whether
 * their signature did involve generics or not, so as to get the proper declaringClass for
 * these methods.
 */
public class ParameterizedMethodBinding extends MethodBinding {

	protected MethodBinding originalMethod;

	/**
	 * Create method of parameterized type, substituting original parameters/exception/return type with type arguments.
	 */
	public ParameterizedMethodBinding(final ParameterizedTypeBinding parameterizedDeclaringClass, MethodBinding originalMethod) {

		super(
				originalMethod.modifiers,
				originalMethod.selector,
				 originalMethod.returnType,
				originalMethod.parameters,
				originalMethod.thrownExceptions,
				parameterizedDeclaringClass);
		this.originalMethod = originalMethod;

		final TypeVariableBinding[] originalVariables = originalMethod.typeVariables;
		Substitution substitution = null;
		final int length = originalVariables.length;
		final boolean isStatic = originalMethod.isStatic();
		if (length == 0) {
			this.typeVariables = NoTypeVariables;
			if (!isStatic) substitution = parameterizedDeclaringClass;
		} else {
			// at least fix up the declaringElement binding + bound substitution if non static
			final TypeVariableBinding[] substitutedVariables = new TypeVariableBinding[length];
			for (int i = 0; i < length; i++) { // copy original type variable to relocate
				TypeVariableBinding originalVariable = originalVariables[i];
				substitutedVariables[i] = new TypeVariableBinding(originalVariable.sourceName, this, originalVariable.rank);
			}
			this.typeVariables = substitutedVariables;
			
			// need to substitute old var refs with new ones (double substitution: declaringClass + new type variables)
			substitution = new Substitution() {
				public LookupEnvironment environment() { 
					return parameterizedDeclaringClass.environment; 
				}
				public boolean isRawSubstitution() {
					return !isStatic && parameterizedDeclaringClass.isRawSubstitution();
				}
				public TypeBinding substitute(TypeVariableBinding typeVariable) {
			        // check this variable can be substituted given copied variables
			        if (typeVariable.rank < length && originalVariables[typeVariable.rank] == typeVariable) {
						return substitutedVariables[typeVariable.rank];
			        }
			        if (!isStatic)
						return parameterizedDeclaringClass.substitute(typeVariable);
			        return typeVariable;
				}
			};
		
			// initialize new variable bounds
			for (int i = 0; i < length; i++) {
				TypeVariableBinding originalVariable = originalVariables[i];
				TypeVariableBinding substitutedVariable = substitutedVariables[i];
				substitutedVariable.superclass = (ReferenceBinding) Scope.substitute(substitution, originalVariable.superclass);
				substitutedVariable.superInterfaces = Scope.substitute(substitution, originalVariable.superInterfaces);
				if (originalVariable.firstBound != null) {
					substitutedVariable.firstBound = originalVariable.firstBound == originalVariable.superclass
						? substitutedVariable.superclass
						: substitutedVariable.superInterfaces[0];
				}
			}
		}
		if (substitution != null) {
			this.returnType = Scope.substitute(substitution, this.returnType);
			this.parameters = Scope.substitute(substitution, this.parameters);
			this.thrownExceptions = Scope.substitute(substitution, this.thrownExceptions);
		}
	}

	public ParameterizedMethodBinding() {
		// no init
	}

	/*
	 * parameterizedDeclaringUniqueKey dot selector originalMethodGenericSignature
	 * p.X<U> { void bar(U u) { new X<String>().bar("") } } --> Lp/X<Ljava/lang/String;>;.bar(TU;)V
	 */
	public char[] computeUniqueKey() {
		return computeUniqueKey(original());
	}

	/**
	 * The type of x.getClass() is substituted from 'Class<? extends Object>' into: 'Class<? extends |X|> where |X| is X's erasure.
	 */
	public static ParameterizedMethodBinding instantiateGetClass(TypeBinding receiverType, MethodBinding originalMethod, Scope scope) {
		ParameterizedMethodBinding method = new ParameterizedMethodBinding();
		method.modifiers = originalMethod.modifiers;
		method.selector = originalMethod.selector;
		method.declaringClass = originalMethod.declaringClass;
		method.typeVariables = NoTypeVariables;
		method.originalMethod = originalMethod;
		method.parameters = originalMethod.parameters;
		method.thrownExceptions = originalMethod.thrownExceptions;
		ReferenceBinding genericClassType = scope.getJavaLangClass();
		method.returnType = scope.createParameterizedType(
			genericClassType,
			new TypeBinding[] {  scope.environment().createWildcard(genericClassType, 0, receiverType.erasure(), Wildcard.EXTENDS) },
			null);
		return method;
	}

	/**
	 * Returns true if some parameters got substituted.
	 */
	public boolean hasSubstitutedParameters() {
		return this.parameters != originalMethod.parameters;
	}

	/**
	 * Returns true if the return type got substituted.
	 */
	public boolean hasSubstitutedReturnType() {
		return this.returnType != originalMethod.returnType;
	}

	/**
	 * Returns the original method (as opposed to parameterized instances)
	 */
	public MethodBinding original() {
		return this.originalMethod.original();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4404.java