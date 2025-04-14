error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4946.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4946.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4946.java
text:
```scala
r@@eturn isSameClassOrSubclassOf(subType, superType); // interfaces are subclasses of Object too

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

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;

class MethodVerifier15 extends MethodVerifier {

MethodVerifier15(LookupEnvironment environment) {
	super(environment);
}
boolean areTypesEqual(TypeBinding one, TypeBinding two) {
	return one == two || super.areTypesEqual(one.erasure(), two.erasure());
}
void checkAgainstInheritedMethods(MethodBinding currentMethod, MethodBinding[] methods, int length) {
	// methods includes the inherited methods that the currentMethod must comply with
	// likely only 1 but could be more if mutiple declared supertypes define the method (1 superclass & 1 to many declared interfaces)
	nextMethod : for (int i = length; --i >= 0;) {
		MethodBinding inheritedMethod = methods[i];
		if (currentMethod.isStatic() != inheritedMethod.isStatic()) {  // Cannot override a static method or hide an instance method
			this.problemReporter(currentMethod).staticAndInstanceConflict(currentMethod, inheritedMethod);
			continue nextMethod;
		}

		// curentMethod is always resolved as its defined by the source type BUT the inheritedMethod may not be
		// so now with generics, the inheritedMethod should be resolved since we don't want to waste time dealing
		// with Unresolved types over & over
		if (inheritedMethod.declaringClass instanceof BinaryTypeBinding)
			((BinaryTypeBinding) inheritedMethod.declaringClass).resolveTypesFor(inheritedMethod);

		// must check each parameter pair to see if you have raw to parameterized conversions
		TypeBinding[] currentArgs = currentMethod.parameters;
		TypeBinding[] inheritedArgs = inheritedMethod.parameters;
		if (currentArgs != inheritedArgs) {
			for (int j = 0, k = currentArgs.length; j < k; j++) {
				TypeBinding currentArg = currentArgs[j].leafComponentType();
				TypeBinding inheritedArg = inheritedArgs[j].leafComponentType();
				if (currentArg != inheritedArg) {
					if (currentArg.isParameterizedType() && hasBoundedParameters((ParameterizedTypeBinding) currentArg)) {
						if (inheritedArg.isRawType()) {
//						if (inheritedArg.isRawType() || !inheritedArg.isEquivalentTo(currentArg)) {
							this.problemReporter(currentMethod).methodNameClash(currentMethod, inheritedMethod);
							continue nextMethod;
						}
					}
				}
			}
		}

		if (!currentMethod.isAbstract() && inheritedMethod.isAbstract()) {
			if ((currentMethod.modifiers & CompilerModifiers.AccOverriding) == 0)
				currentMethod.modifiers |= CompilerModifiers.AccImplementing;
		} else {
			currentMethod.modifiers |= CompilerModifiers.AccOverriding;
		}

		boolean addBridgeMethod = inheritedMethod.hasSubstitutedReturnType();
		if (currentMethod.returnType != inheritedMethod.returnType) {
			// can be [] of Class#RAW vs. Class<T>
			if (!isReturnTypeSubstituable(currentMethod, inheritedMethod)) {
				this.problemReporter(currentMethod).incompatibleReturnType(currentMethod, inheritedMethod);
				continue nextMethod;
			}

			TypeBinding inheritedReturnType = inheritedMethod.returnType.leafComponentType();
			TypeBinding returnType = currentMethod.returnType.leafComponentType();
			if (inheritedReturnType.isRawType()) {
				if (returnType.isParameterizedType() && hasBoundedParameters((ParameterizedTypeBinding) returnType)) {
					this.problemReporter(currentMethod).methodNameClash(currentMethod, inheritedMethod);
					continue nextMethod;
				}
			} else if (inheritedReturnType.isParameterizedType()) {
				if (!returnType.isParameterizedType()) 
					this.problemReporter(currentMethod).unsafeReturnTypeOverride(currentMethod, inheritedMethod, ((MethodDeclaration)currentMethod.sourceMethod()).returnType);
			} else if (inheritedReturnType.isTypeVariable()) {
				this.problemReporter(currentMethod).unsafeReturnTypeOverride(currentMethod, inheritedMethod,  ((MethodDeclaration)currentMethod.sourceMethod()).returnType);
			}
			addBridgeMethod = true;
		}

		if (addBridgeMethod || inheritedMethod.hasSubstitutedParameters()) {
		    MethodBinding original = inheritedMethod.original();
		    if (!areReturnTypesEqual(original, currentMethod) || !areParametersEqual(original, currentMethod))
				this.type.addSyntheticBridgeMethod(original, currentMethod);
		}

		if (currentMethod.thrownExceptions != NoExceptions)
			this.checkExceptions(currentMethod, inheritedMethod);
		if (inheritedMethod.isFinal())
			this.problemReporter(currentMethod).finalMethodCannotBeOverridden(currentMethod, inheritedMethod);
		if (!this.isAsVisible(currentMethod, inheritedMethod))
			this.problemReporter(currentMethod).visibilityConflict(currentMethod, inheritedMethod);
		if (environment.options.reportDeprecationWhenOverridingDeprecatedMethod && inheritedMethod.isViewedAsDeprecated()) {
			if (!currentMethod.isViewedAsDeprecated() || environment.options.reportDeprecationInsideDeprecatedCode) {
				// check against the other inherited methods to see if they hide this inheritedMethod
				ReferenceBinding declaringClass = inheritedMethod.declaringClass;
				if (declaringClass.isInterface())
					for (int j = length; --j >= 0;)
						if (i != j && methods[j].declaringClass.implementsInterface(declaringClass, false))
							continue nextMethod;

				this.problemReporter(currentMethod).overridesDeprecatedMethod(currentMethod, inheritedMethod);
			}
		}
	}
}
boolean doesMethodOverride(MethodBinding method, MethodBinding inheritedMethod) {
	return areParametersEqual(method, inheritedMethod) && isReturnTypeSubstituable(method, inheritedMethod);
}
boolean hasBoundedParameters(ParameterizedTypeBinding parameterizedType) {
	TypeBinding[] arguments = parameterizedType.arguments;
	if (arguments == null) return false;

	nextArg : for (int i = 0, l = arguments.length; i < l; i++) {
		if (arguments[i].isWildcard())
			if (((WildcardBinding) arguments[i]).kind == org.eclipse.jdt.internal.compiler.ast.Wildcard.UNBOUND)
				continue nextArg;
		if (arguments[i].isTypeVariable())
			if (((TypeVariableBinding) arguments[i]).firstBound == null)
				continue nextArg;
		return true;
	}
	return false;
}
boolean isReturnTypeSubstituable(MethodBinding one, MethodBinding two) {
	if (one.returnType == two.returnType) return true;

	return isTypeSubstituable(one.returnType.erasure(), two.returnType.erasure());
}
boolean isTypeSubstituable(TypeBinding one, TypeBinding two) {
	if (one == two) return true;
	if (one.isArrayType() || two.isArrayType()) {
		if (one.isArrayType() != two.isArrayType()) return false;
		ArrayBinding arrayOne = (ArrayBinding) one;
		ArrayBinding arrayTwo = (ArrayBinding) two;
		if (arrayOne.dimensions != arrayTwo.dimensions) return false;
		one = arrayOne.leafComponentType;
		two = arrayTwo.leafComponentType;
	}
	if (one.isBaseType() || two.isBaseType()) return false;

	ReferenceBinding subType = (ReferenceBinding) one;
	ReferenceBinding superType = (ReferenceBinding) two;
	if (CharOperation.equals(subType.compoundName, superType.compoundName)) return true;

	superType = BinaryTypeBinding.resolveType(superType, this.environment, true);
	subType = BinaryTypeBinding.resolveType(subType, this.environment, true);
	if (superType.isInterface())
		return subType.implementsInterface(superType, true);
	return subType.isClass() && isSameClassOrSubclassOf(subType, superType);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4946.java