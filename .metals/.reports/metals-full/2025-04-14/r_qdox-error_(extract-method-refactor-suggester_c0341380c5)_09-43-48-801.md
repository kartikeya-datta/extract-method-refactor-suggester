error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/492.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/492.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/492.java
text:
```scala
public i@@nt sourceStart; // used by computeUniqueKey to uniquely identify this binding

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
package org.eclipse.jdt.internal.compiler.lookup;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.CaseStatement;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;

public final class LocalTypeBinding extends NestedTypeBinding {
	final static char[] LocalTypePrefix = { '$', 'L', 'o', 'c', 'a', 'l', '$' };

	private InnerEmulationDependency[] dependents;
	public ArrayBinding[] localArrayBindings; // used to cache array bindings of various dimensions for this local type
	public CaseStatement enclosingCase; // from 1.4 on, local types should not be accessed across switch case blocks (52221)
	int sourceStart; // used by computeUniqueKey to uniquely identify this binding
	public MethodBinding enclosingMethod;
	
public LocalTypeBinding(ClassScope scope, SourceTypeBinding enclosingType, CaseStatement switchCase) {
	super(
		new char[][] {CharOperation.concat(LocalTypePrefix, scope.referenceContext.name)},
		scope,
		enclosingType);
	
	if (this.sourceName == TypeDeclaration.ANONYMOUS_EMPTY_NAME)
		this.tagBits |= AnonymousTypeMask;
	else
		this.tagBits |= LocalTypeMask;
	this.enclosingCase = switchCase;
	this.sourceStart = scope.referenceContext.sourceStart;
	MethodScope methodScope = scope.enclosingMethodScope();
	AbstractMethodDeclaration declaration = methodScope.referenceMethod();
	if (declaration != null) {
		this.enclosingMethod = declaration.binding;
	}
}
/* Record a dependency onto a source target type which may be altered
* by the end of the innerclass emulation. Later on, we will revisit
* all its dependents so as to update them (see updateInnerEmulationDependents()).
*/

public void addInnerEmulationDependent(BlockScope dependentScope, boolean wasEnclosingInstanceSupplied) {
	int index;
	if (dependents == null) {
		index = 0;
		dependents = new InnerEmulationDependency[1];
	} else {
		index = dependents.length;
		for (int i = 0; i < index; i++)
			if (dependents[i].scope == dependentScope)
				return; // already stored
		System.arraycopy(dependents, 0, (dependents = new InnerEmulationDependency[index + 1]), 0, index);
	}
	dependents[index] = new InnerEmulationDependency(dependentScope, wasEnclosingInstanceSupplied);
	//  System.out.println("Adding dependency: "+ new String(scope.enclosingType().readableName()) + " --> " + new String(this.readableName()));
}
public char[] computeUniqueKey(boolean isLeaf) {
	char[] outerKey = outermostEnclosingType().computeUniqueKey(isLeaf);
	int semicolon = CharOperation.lastIndexOf(';', outerKey);

	// insert $sourceStart
	return CharOperation.concat(
		CharOperation.concat(
			CharOperation.subarray(outerKey, 0, semicolon),
			String.valueOf(
			this.sourceStart).toCharArray(),
			'$'),
		CharOperation.subarray(outerKey, semicolon, outerKey.length));
}

public char[] constantPoolName() /* java/lang/Object */ {
	return constantPoolName;
}

ArrayBinding createArrayType(int dimensionCount) {
	if (localArrayBindings == null) {
		localArrayBindings = new ArrayBinding[] {new ArrayBinding(this, dimensionCount, scope.environment())};
		return localArrayBindings[0];
	}

	// find the cached array binding for this dimensionCount (if any)
	int length = localArrayBindings.length;
	for (int i = 0; i < length; i++)
		if (localArrayBindings[i].dimensions == dimensionCount)
			return localArrayBindings[i];

	// no matching array
	System.arraycopy(localArrayBindings, 0, localArrayBindings = new ArrayBinding[length + 1], 0, length); 
	return localArrayBindings[length] = new ArrayBinding(this, dimensionCount, scope.environment());
}

/*
 * Overriden for code assist. In this case, the constantPoolName() has not been computed yet.
 * Slam the source name so that the signature is syntactically correct.
 * (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=99686)
 */
public char[] genericTypeSignature() {
	if (this.genericReferenceTypeSignature == null && constantPoolName() == null) {
		if (isAnonymousType())
			setConstantPoolName(superclass().sourceName());
		else
			setConstantPoolName(sourceName());
	}
	return super.genericTypeSignature();
}

public char[] readableName() /*java.lang.Object,  p.X<T> */ {
    char[] readableName;
	if (isAnonymousType()) {
		if (superInterfaces == NoSuperInterfaces)
			readableName = CharOperation.concat(TypeConstants.ANONYM_PREFIX, superclass.readableName(), TypeConstants.ANONYM_SUFFIX);
		else
			readableName = CharOperation.concat(TypeConstants.ANONYM_PREFIX, superInterfaces[0].readableName(), TypeConstants.ANONYM_SUFFIX);
	} else if (isMemberType()) {
		readableName = CharOperation.concat(enclosingType().readableName(), this.sourceName, '.');
	} else {
		readableName = this.sourceName;
	}    
	TypeVariableBinding[] typeVars;
	if ((typeVars = this.typeVariables()) != NoTypeVariables) {
	    StringBuffer nameBuffer = new StringBuffer(10);
	    nameBuffer.append(readableName).append('<');
	    for (int i = 0, length = typeVars.length; i < length; i++) {
	        if (i > 0) nameBuffer.append(',');
	        nameBuffer.append(typeVars[i].readableName());
	    }
	    nameBuffer.append('>');
	    int nameLength = nameBuffer.length();
		readableName = new char[nameLength];
		nameBuffer.getChars(0, nameLength, readableName, 0);
	}
	return readableName;
}

public char[] shortReadableName() /*Object*/ {
    char[] shortReadableName;
	if (isAnonymousType()) {
		if (superInterfaces == NoSuperInterfaces)
			shortReadableName = CharOperation.concat(TypeConstants.ANONYM_PREFIX, superclass.shortReadableName(), TypeConstants.ANONYM_SUFFIX);
		else
			shortReadableName = CharOperation.concat(TypeConstants.ANONYM_PREFIX, superInterfaces[0].shortReadableName(), TypeConstants.ANONYM_SUFFIX);
	} else if (isMemberType()) {
		shortReadableName = CharOperation.concat(enclosingType().shortReadableName(), sourceName, '.');
	} else {
		shortReadableName = sourceName;
	}
	TypeVariableBinding[] typeVars;
	if ((typeVars = this.typeVariables()) != NoTypeVariables) {
	    StringBuffer nameBuffer = new StringBuffer(10);
	    nameBuffer.append(shortReadableName).append('<');
	    for (int i = 0, length = typeVars.length; i < length; i++) {
	        if (i > 0) nameBuffer.append(',');
	        nameBuffer.append(typeVars[i].shortReadableName());
	    }
	    nameBuffer.append('>');
		int nameLength = nameBuffer.length();
		shortReadableName = new char[nameLength];
		nameBuffer.getChars(0, nameLength, shortReadableName, 0);	    
	}
	return shortReadableName;
}

// Record that the type is a local member type
public void setAsMemberType() {
	tagBits |= MemberTypeMask;
}

public void setConstantPoolName(char[] computedConstantPoolName) /* java/lang/Object */ {
	this.constantPoolName = computedConstantPoolName;
}

public char[] sourceName() {
	if (isAnonymousType()) {
		if (superInterfaces == NoSuperInterfaces)
			return CharOperation.concat(TypeConstants.ANONYM_PREFIX, superclass.sourceName(), TypeConstants.ANONYM_SUFFIX);
		else
			return CharOperation.concat(TypeConstants.ANONYM_PREFIX, superInterfaces[0].sourceName(), TypeConstants.ANONYM_SUFFIX);
			
	} else
		return sourceName;
}
public String toString() {
	if (isAnonymousType())
		return "Anonymous type : " + super.toString(); //$NON-NLS-1$
	if (isMemberType())
		return "Local member type : " + new String(sourceName()) + " " + super.toString(); //$NON-NLS-2$ //$NON-NLS-1$
	return "Local type : " + new String(sourceName()) + " " + super.toString(); //$NON-NLS-2$ //$NON-NLS-1$
}
/* Trigger the dependency mechanism forcing the innerclass emulation
* to be propagated to all dependent source types.
*/

public void updateInnerEmulationDependents() {
	if (dependents != null) {
		for (int i = 0; i < dependents.length; i++) {
			InnerEmulationDependency dependency = dependents[i];
			// System.out.println("Updating " + new String(this.readableName()) + " --> " + new String(dependency.scope.enclosingType().readableName()));
			dependency.scope.propagateInnerEmulation(this, dependency.wasEnclosingInstanceSupplied);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/492.java