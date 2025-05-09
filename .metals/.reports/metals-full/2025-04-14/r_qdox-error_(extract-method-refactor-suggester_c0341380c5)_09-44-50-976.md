error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3981.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3981.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3981.java
text:
```scala
public c@@har[] computeUniqueKey(boolean isLeaf) {

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
import org.eclipse.jdt.internal.compiler.util.HashtableOfPackage;
import org.eclipse.jdt.internal.compiler.util.HashtableOfType;

public class PackageBinding extends Binding implements TypeConstants {
	public long tagBits = 0; // See values in the interface TagBits below
	
	public char[][] compoundName;
	PackageBinding parent;
	public LookupEnvironment environment;
	HashtableOfType knownTypes;
	HashtableOfPackage knownPackages;
protected PackageBinding() {
	// for creating problem package
}
public PackageBinding(char[][] compoundName, PackageBinding parent, LookupEnvironment environment) {
	this.compoundName = compoundName;
	this.parent = parent;
	this.environment = environment;
	this.knownTypes = null; // initialized if used... class counts can be very large 300-600
	this.knownPackages = new HashtableOfPackage(3); // sub-package counts are typically 0-3
}
public PackageBinding(char[] topLevelPackageName, LookupEnvironment environment) {
	this(new char[][] {topLevelPackageName}, null, environment);
}
/* Create the default package.
*/

public PackageBinding(LookupEnvironment environment) {
	this(CharOperation.NO_CHAR_CHAR, null, environment);
}
private void addNotFoundPackage(char[] simpleName) {
	knownPackages.put(simpleName, LookupEnvironment.TheNotFoundPackage);
}
private void addNotFoundType(char[] simpleName) {
	if (knownTypes == null)
		knownTypes = new HashtableOfType(25);
	knownTypes.put(simpleName, LookupEnvironment.TheNotFoundType);
}
void addPackage(PackageBinding element) {
	knownPackages.put(element.compoundName[element.compoundName.length - 1], element);
}
void addType(ReferenceBinding element) {
	if (knownTypes == null)
		knownTypes = new HashtableOfType(25);
	knownTypes.put(element.compoundName[element.compoundName.length - 1], element);
}
/* API
* Answer the receiver's binding type from Binding.BindingID.
*/

public final int kind() {
	return Binding.PACKAGE;
}
/*
 * slash separated name
 * org.eclipse.jdt.core --> org/eclipse/jdt/core
 */
public char[] computeUniqueKey(boolean withAccessFlags) {
	return CharOperation.concatWith(compoundName, '/');
}
private PackageBinding findPackage(char[] name) {
	if (!environment.isPackage(this.compoundName, name))
		return null;

	char[][] subPkgCompoundName = CharOperation.arrayConcat(this.compoundName, name);
	PackageBinding subPackageBinding = new PackageBinding(subPkgCompoundName, this, environment);
	addPackage(subPackageBinding);
	return subPackageBinding;
}
/* Answer the subpackage named name; ask the oracle for the package if its not in the cache.
* Answer null if it could not be resolved.
*
* NOTE: This should only be used when we know there is NOT a type with the same name.
*/

PackageBinding getPackage(char[] name) {
	PackageBinding binding = getPackage0(name);
	if (binding != null) {
		if (binding == LookupEnvironment.TheNotFoundPackage)
			return null;
		else
			return binding;
	}
	if ((binding = findPackage(name)) != null)
		return binding;

	// not found so remember a problem package binding in the cache for future lookups
	addNotFoundPackage(name);
	return null;
}
/* Answer the subpackage named name if it exists in the cache.
* Answer theNotFoundPackage if it could not be resolved the first time
* it was looked up, otherwise answer null.
*
* NOTE: Senders must convert theNotFoundPackage into a real problem
* package if its to returned.
*/

PackageBinding getPackage0(char[] name) {
	return knownPackages.get(name);
}
/* Answer the type named name; ask the oracle for the type if its not in the cache.
* Answer a NotVisible problem type if the type is not visible from the invocationPackage.
* Answer null if it could not be resolved.
*
* NOTE: This should only be used by source types/scopes which know there is NOT a
* package with the same name.
*/

ReferenceBinding getType(char[] name) {
	ReferenceBinding typeBinding = getType0(name);
	if (typeBinding == null) {
		if ((typeBinding = environment.askForType(this, name)) == null) {
			// not found so remember a problem type binding in the cache for future lookups
			addNotFoundType(name);
			return null;
		}
	}

	if (typeBinding == LookupEnvironment.TheNotFoundType)
		return null;

	typeBinding = BinaryTypeBinding.resolveType(typeBinding, environment, false); // no raw conversion for now
	if (typeBinding.isNestedType())
		return new ProblemReferenceBinding(name, InternalNameProvided);
	return typeBinding;
}
/* Answer the type named name if it exists in the cache.
* Answer theNotFoundType if it could not be resolved the first time
* it was looked up, otherwise answer null.
*
* NOTE: Senders must convert theNotFoundType into a real problem
* reference type if its to returned.
*/

ReferenceBinding getType0(char[] name) {
	if (knownTypes == null)
		return null;
	return knownTypes.get(name);
}
/* Answer the package or type named name; ask the oracle if it is not in the cache.
* Answer null if it could not be resolved.
*
* When collisions exist between a type name & a package name, answer the type.
* Treat the package as if it does not exist... a problem was already reported when the type was defined.
*
* NOTE: no visibility checks are performed.
* THIS SHOULD ONLY BE USED BY SOURCE TYPES/SCOPES.
*/

public Binding getTypeOrPackage(char[] name) {
	ReferenceBinding typeBinding = getType0(name);
	if (typeBinding != null && typeBinding != LookupEnvironment.TheNotFoundType) {
		typeBinding = BinaryTypeBinding.resolveType(typeBinding, environment, false); // no raw conversion for now
		if (typeBinding.isNestedType())
			return new ProblemReferenceBinding(name, InternalNameProvided);
		return typeBinding;
	}

	PackageBinding packageBinding = getPackage0(name);
	if (packageBinding != null && packageBinding != LookupEnvironment.TheNotFoundPackage)
		return packageBinding;

	if (typeBinding == null) { // have not looked for it before
		if ((typeBinding = environment.askForType(this, name)) != null) {
			if (typeBinding.isNestedType())
				return new ProblemReferenceBinding(name, InternalNameProvided);
			return typeBinding;
		}

		// Since name could not be found, add a problem binding
		// to the collections so it will be reported as an error next time.
		addNotFoundType(name);
	}

	if (packageBinding == null) { // have not looked for it before
		if ((packageBinding = findPackage(name)) != null)
			return packageBinding;
		addNotFoundPackage(name);
	}

	return null;
}
public char[] readableName() /*java.lang*/ {
	return CharOperation.concatWith(compoundName, '.');
}
public String toString() {
	if (compoundName == CharOperation.NO_CHAR_CHAR)
		return "The Default Package"; //$NON-NLS-1$
	else
		return "package " + ((compoundName != null) ? CharOperation.toString(compoundName) : "UNNAMED"); //$NON-NLS-1$ //$NON-NLS-2$
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3981.java