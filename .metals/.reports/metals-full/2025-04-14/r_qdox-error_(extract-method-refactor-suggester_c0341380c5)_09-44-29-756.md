error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4984.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4984.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4984.java
text:
```scala
C@@harOperation.NO_CHAR_CHAR}; // default package

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
package org.eclipse.jdt.internal.core.builder;

import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.jdt.internal.compiler.util.CharOperation;

import java.util.*;

public class ReferenceCollection {

char[][][] qualifiedNameReferences; // contains no simple names as in just 'a' which is kept in simpleNameReferences instead
char[][] simpleNameReferences;

protected ReferenceCollection(char[][][] qualifiedNameReferences, char[][] simpleNameReferences) {
	this.qualifiedNameReferences = internQualifiedNames(qualifiedNameReferences);
	this.simpleNameReferences = internSimpleNames(simpleNameReferences, true);
}

boolean includes(char[] simpleName) {
	for (int i = 0, l = simpleNameReferences.length; i < l; i++)
		if (simpleName == simpleNameReferences[i]) return true;
	return false;
}

boolean includes(char[][] qualifiedName) {
	for (int i = 0, l = qualifiedNameReferences.length; i < l; i++)
		if (qualifiedName == qualifiedNameReferences[i]) return true;
	return false;
}

boolean includes(char[][][] qualifiedNames, char[][] simpleNames) {
	// if either collection of names is null, it means it contained a well known name so we know it already has a match
	if (simpleNames == null || qualifiedNames == null) {
		if (simpleNames == null && qualifiedNames == null) {
			if (JavaBuilder.DEBUG)
				System.out.println("  found well known match"); //$NON-NLS-1$
			return true;
		} else if (qualifiedNames == null) {
			for (int i = 0, l = simpleNames.length; i < l; i++) {
				if (includes(simpleNames[i])) {
					if (JavaBuilder.DEBUG)
						System.out.println("  found match in well known package to " + new String(simpleNames[i])); //$NON-NLS-1$
					return true;
				}
			}
		} else {
			for (int i = 0, l = qualifiedNames.length; i < l; i++) {
				char[][] qualifiedName = qualifiedNames[i];
				if (qualifiedName.length == 1 ? includes(qualifiedName[0]) : includes(qualifiedName)) {
					if (JavaBuilder.DEBUG)
						System.out.println("  found well known match in " + CharOperation.toString(qualifiedName)); //$NON-NLS-1$
					return true;
				}
			}
		}
	} else {
		for (int i = 0, l = simpleNames.length; i < l; i++) {
			if (includes(simpleNames[i])) {
				for (int j = 0, m = qualifiedNames.length; j < m; j++) {
					char[][] qualifiedName = qualifiedNames[j];
					if (qualifiedName.length == 1 ? includes(qualifiedName[0]) : includes(qualifiedName)) {
						if (JavaBuilder.DEBUG)
							System.out.println("  found match in " + CharOperation.toString(qualifiedName) //$NON-NLS-1$
								+ " to " + new String(simpleNames[i])); //$NON-NLS-1$
						return true;
					}
				}
				return false;
			}
		}
	}
	return false;
}


// When any type is compiled, its methods are verified for certain problems
// the MethodVerifier requests 3 well known types which end up in the reference collection
// having WellKnownQualifiedNames & WellKnownSimpleNames, saves every type 40 bytes
// NOTE: These collections are sorted by length
static final char[][][] WellKnownQualifiedNames = new char[][][] {
	TypeConstants.JAVA_LANG_RUNTIMEEXCEPTION,
	TypeConstants.JAVA_LANG_THROWABLE,
	TypeConstants.JAVA_LANG_OBJECT,
	TypeConstants.JAVA_LANG,
	new char[][] {TypeConstants.JAVA},
	new char[][] {new char[] {'o', 'r', 'g'}},
	new char[][] {new char[] {'c', 'o', 'm'}},
	TypeConstants.NoCharChar}; // default package
static final char[][] WellKnownSimpleNames = new char[][] {
	TypeConstants.JAVA_LANG_RUNTIMEEXCEPTION[2],
	TypeConstants.JAVA_LANG_THROWABLE[2],
	TypeConstants.JAVA_LANG_OBJECT[2],
	TypeConstants.JAVA,
	TypeConstants.LANG,
	new char[] {'o', 'r', 'g'},
	new char[] {'c', 'o', 'm'}};

static final char[][][] EmptyQualifiedNames = new char[0][][];
static final char[][] EmptySimpleNames = new char[0][];

// each array contains qualified char[][], one for size 2, 3, 4, 5, 6, 7 & the rest
static final int MaxQualifiedNames = 7;
static ArrayList[] InternedQualifiedNames = new ArrayList[MaxQualifiedNames];
// each array contains simple char[], one for size 1 to 29 & the rest
static final int MaxSimpleNames = 30;
static ArrayList[] InternedSimpleNames = new ArrayList[MaxSimpleNames];
static {
	for (int i = 0; i < MaxQualifiedNames; i++)
		InternedQualifiedNames[i] = new ArrayList(37);
	for (int i = 0; i < MaxSimpleNames; i++)
		InternedSimpleNames[i] = new ArrayList(11);
};

static char[][][] internQualifiedNames(ArrayList qualifiedStrings) {
	if (qualifiedStrings == null) return EmptyQualifiedNames;
	int length = qualifiedStrings.size();
	if (length == 0) return EmptyQualifiedNames;

	char[][][] result = new char[length][][];
	for (int i = 0; i < length; i++)
		result[i] = CharOperation.splitOn('/', ((String) qualifiedStrings.get(i)).toCharArray());
	return internQualifiedNames(result);
}

static char[][][] internQualifiedNames(char[][][] qualifiedNames) {
	if (qualifiedNames == null) return EmptyQualifiedNames;
	int length = qualifiedNames.length;
	if (length == 0) return EmptyQualifiedNames;

	char[][][] keepers = new char[length][][];
	int index = 0;
	next : for (int i = 0; i < length; i++) {
		char[][] qualifiedName = qualifiedNames[i];
		int qLength = qualifiedName.length;
		for (int j = 0, k = WellKnownQualifiedNames.length; j < k; j++) {
			char[][] wellKnownName = WellKnownQualifiedNames[j];
			if (qLength > wellKnownName.length)
				break; // all remaining well known names are shorter
			if (CharOperation.equals(qualifiedName, wellKnownName))
				continue next;
		}

		// InternedQualifiedNames[0] is for the rest (> 7 & 1)
		// InternedQualifiedNames[1] is for size 2...
		// InternedQualifiedNames[6] is for size 7
		ArrayList internedNames = InternedQualifiedNames[qLength <= MaxQualifiedNames ? qLength - 1 : 0];
		for (int j = 0, k = internedNames.size(); j < k; j++) {
			char[][] internedName = (char[][]) internedNames.get(j);
			if (CharOperation.equals(qualifiedName, internedName)) {
				keepers[index++] = internedName;
				continue next;
			}
		}
		qualifiedName = internSimpleNames(qualifiedName, false);
		internedNames.add(qualifiedName);
		keepers[index++] = qualifiedName;
	}
	if (length > index) {
		if (length == 0) return EmptyQualifiedNames;
		System.arraycopy(keepers, 0, keepers = new char[index][][], 0, index);
	}
	return keepers;
}

static char[][] internSimpleNames(ArrayList simpleStrings) {
	if (simpleStrings == null) return EmptySimpleNames;
	int length = simpleStrings.size();
	if (length == 0) return EmptySimpleNames;

	char[][] result = new char[length][];
	for (int i = 0; i < length; i++)
		result[i] = ((String) simpleStrings.get(i)).toCharArray();
	return internSimpleNames(result, true);
}

static char[][] internSimpleNames(char[][] simpleNames, boolean removeWellKnown) {
	if (simpleNames == null) return EmptySimpleNames;
	int length = simpleNames.length;
	if (length == 0) return EmptySimpleNames;

	char[][] keepers = new char[length][];
	int index = 0;
	next : for (int i = 0; i < length; i++) {
		char[] name = simpleNames[i];
		int sLength = name.length;
		for (int j = 0, k = WellKnownSimpleNames.length; j < k; j++) {
			char[] wellKnownName = WellKnownSimpleNames[j];
			if (sLength > wellKnownName.length)
				break; // all remaining well known names are shorter
			if (CharOperation.equals(name, wellKnownName)) {
				if (!removeWellKnown)
					keepers[index++] = WellKnownSimpleNames[j];
				continue next;
			}
		}

		// InternedSimpleNames[0] is for the rest (> 29)
		// InternedSimpleNames[1] is for size 1...
		// InternedSimpleNames[29] is for size 29
		ArrayList internedNames = InternedSimpleNames[sLength < MaxSimpleNames ? sLength : 0];
		for (int j = 0, k = internedNames.size(); j < k; j++) {
			char[] internedName = (char[]) internedNames.get(j);
			if (CharOperation.equals(name, internedName)) {
				keepers[index++] = internedName;
				continue next;
			}
		}
		internedNames.add(name);
		keepers[index++] = name;
	}
	if (length > index) {
		if (index == 0) return EmptySimpleNames;
		System.arraycopy(keepers, 0, keepers = new char[index][], 0, index);
	}
	return keepers;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4984.java