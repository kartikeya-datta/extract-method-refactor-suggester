error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9875.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9875.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9875.java
text:
```scala
public b@@oolean doesMethodOverride(MethodBinding method, MethodBinding inheritedMethod) {

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

import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;

class MethodVerifier15 extends MethodVerifier {

MethodVerifier15(LookupEnvironment environment) {
	super(environment);
}
boolean areMethodsEqual(MethodBinding one, MethodBinding substituteTwo) {
	TypeBinding[] oneParams = one.parameters;
	TypeBinding[] twoParams = substituteTwo.parameters;
	boolean checkParameters = false;
	if (oneParams != twoParams) {
		int length = oneParams.length;
		if (length != twoParams.length) return false; // no match

		for (int i = 0; i < length; i++) {
			if (oneParams[i] != twoParams[i]) {
				checkParameters |= oneParams[i].leafComponentType().isParameterizedType();
				if (!areTypesEqual(oneParams[i], twoParams[i])) {
					while (!checkParameters && ++i < length)
						checkParameters |= oneParams[i].leafComponentType().isParameterizedType();
					if (checkParameters && one.areParameterErasuresEqual(substituteTwo)) // at least one parameter may cause a name clash
						detectNameClash(one, substituteTwo, true);
					return false; // no match but needed to check for a name clash
				}
			}
		}
	}
	return !detectNameClash(one, substituteTwo, checkParameters);
}
boolean areReturnTypesEqual(MethodBinding one, MethodBinding substituteTwo) {
	if (one.returnType == substituteTwo.returnType) return true;

	// methods from classes are always before methods from interfaces
	if (one.declaringClass.isClass() || one.declaringClass.implementsInterface(substituteTwo.declaringClass, true))
		return one.returnType.isCompatibleWith(substituteTwo.returnType);

	if (substituteTwo.declaringClass.implementsInterface(one.declaringClass, true))
		return substituteTwo.returnType.isCompatibleWith(one.returnType);

	// unrelated interfaces... one must be a subtype of the other
	return one.returnType.isCompatibleWith(substituteTwo.returnType)
 substituteTwo.returnType.isCompatibleWith(one.returnType);
}
boolean areTypesEqual(TypeBinding one, TypeBinding two) {
	if (one == two) return true;

	switch (one.bindingType()) {
		case Binding.PARAMETERIZED_TYPE :
		case Binding.RAW_TYPE :
			return one.isEquivalentTo(two);
//		case Binding.TYPE_PARAMETER : // won't work for variables from different classes - need substitution
	}

	// Can skip this since we resolved each method before comparing it, see computeSubstituteMethod()
	//	if (one instanceof UnresolvedReferenceBinding)
	//		return ((UnresolvedReferenceBinding) one).resolvedType == two;
	//	if (two instanceof UnresolvedReferenceBinding)
	//		return ((UnresolvedReferenceBinding) two).resolvedType == one;
	return false; // all other type bindings are identical
}
void checkForBridgeMethod(MethodBinding currentMethod, MethodBinding inheritedMethod) {
	MethodBinding originalInherited = inheritedMethod.original();
	if (inheritedMethod != originalInherited) {
		MethodBinding[] toCheck = (MethodBinding[]) this.currentMethods.get(currentMethod.selector);
		if (toCheck.length > 1) {
			// must check to see if a bridge method will collide with another current method (see 77861)
			for (int i = 0, length = toCheck.length; i < length; i++) {
				if (currentMethod != toCheck[i] && toCheck[i].areParameterErasuresEqual(originalInherited)) {
					problemReporter(toCheck[i]).methodNameClash(toCheck[i], originalInherited); // bridge method will collide
					return;
				}
			}
		}
	}

	// so the parameters are equal and the return type is compatible b/w the currentMethod & the substituted inheritedMethod
	// then when do you need a bridge method?
	if (originalInherited.returnType != currentMethod.returnType) {
		switch (originalInherited.returnType.leafComponentType().bindingType()) {
			case Binding.PARAMETERIZED_TYPE :
				if (!currentMethod.returnType.leafComponentType().isParameterizedType())
					problemReporter(currentMethod).unsafeReturnTypeOverride(currentMethod, originalInherited, ((MethodDeclaration) currentMethod.sourceMethod()).returnType);
				break;
			case Binding.TYPE_PARAMETER :
				if (!currentMethod.returnType.leafComponentType().isTypeVariable())
					problemReporter(currentMethod).unsafeReturnTypeOverride(currentMethod, originalInherited, ((MethodDeclaration) currentMethod.sourceMethod()).returnType);
				break;
		}   
	}
	this.type.addSyntheticBridgeMethod(originalInherited, currentMethod);
}
MethodBinding computeSubstituteMethod(MethodBinding inheritedMethod, MethodBinding currentMethod) {
	if (inheritedMethod == null) return null;

	// due to hierarchy & compatibility checks, we need to ensure these 2 methods are resolved
	// should we push these tests to where they're needed? returnType.isCompatibleWith && parameter isEquivalentTo ?
	if (currentMethod.declaringClass instanceof BinaryTypeBinding)
		((BinaryTypeBinding) currentMethod.declaringClass).resolveTypesFor(currentMethod);
	if (inheritedMethod.declaringClass instanceof BinaryTypeBinding)
		((BinaryTypeBinding) inheritedMethod.declaringClass).resolveTypesFor(inheritedMethod);

	TypeVariableBinding[] inheritedTypeVariables = inheritedMethod.typeVariables();
	if (inheritedTypeVariables == NoTypeVariables) return inheritedMethod;
	TypeVariableBinding[] typeVariables = currentMethod == null ? NoTypeVariables : currentMethod.typeVariables;

	int inheritedLength = inheritedTypeVariables.length;
	int length = typeVariables.length;
	TypeBinding[] arguments = new TypeBinding[inheritedLength];
	if (inheritedLength <= length) {
		System.arraycopy(typeVariables, 0, arguments, 0, inheritedLength);
	} else {
		System.arraycopy(typeVariables, 0, arguments, 0, length);
		for (int i = length; i < inheritedLength; i++)
			arguments[i] = inheritedTypeVariables[i].erasure();
	}
	ParameterizedGenericMethodBinding substitute =
		new ParameterizedGenericMethodBinding(inheritedMethod, arguments, this.environment);
	for (int i = 0; i < inheritedLength; i++)
	    if (!inheritedTypeVariables[i].boundCheck(substitute, arguments[i]))
	    	return inheritedMethod; // incompatible due to bound check
   return substitute;
}
boolean detectNameClash(MethodBinding one, MethodBinding substituteTwo, boolean checkParameters) {
	if (doTypeVariablesClash(one, substituteTwo) || (checkParameters && doParametersClash(one, substituteTwo))) {
		if (this.type == one.declaringClass)
		 	problemReporter(one).methodNameClash(one, substituteTwo);
		else
			problemReporter().inheritedMethodsHaveNameClash(this.type, one, substituteTwo);
		return true;
	}
	return false;
}
boolean doesMethodOverride(MethodBinding method, MethodBinding inheritedMethod) {
	return super.doesMethodOverride(method, computeSubstituteMethod(inheritedMethod, method));
}
boolean doParametersClash(MethodBinding one, MethodBinding substituteTwo) {
	// must check each parameter pair to see if parameterized types are compatible
	TypeBinding[] oneParams = one.parameters;
	TypeBinding[] twoParams = substituteTwo.parameters;
	for (int i = 0, l = oneParams.length; i < l; i++) {
		if (oneParams[i] == twoParams[i]) continue;
		if (!oneParams[i].leafComponentType().isParameterizedType()) continue;

		if (!twoParams[i].leafComponentType().isParameterizedType()
 !oneParams[i].isEquivalentTo(twoParams[i])
 !twoParams[i].isEquivalentTo(oneParams[i])) {
				return true;
		}
	}
	return false;
}
boolean doTypeVariablesClash(MethodBinding one, MethodBinding substituteTwo) {
	TypeBinding[] currentVars = one.typeVariables;
	TypeBinding[] inheritedVars = substituteTwo.original().typeVariables;
	return currentVars.length != inheritedVars.length
		&& currentVars.length > 0 && inheritedVars.length > 0; // must match unless all are replaced
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
boolean isInterfaceMethodImplemented(MethodBinding inheritedMethod, MethodBinding existingMethod, ReferenceBinding superType) {
	inheritedMethod = computeSubstituteMethod(inheritedMethod, existingMethod);
	return inheritedMethod.returnType == existingMethod.returnType
		&& super.isInterfaceMethodImplemented(inheritedMethod, existingMethod, superType);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9875.java