error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7260.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7260.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[13,1]

error in qdox parser
file content:
```java
offset: 634
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7260.java
text:
```scala
public class SuperTypeReferencePattern extends SearchPattern implements IIndexConstants {

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
p@@ackage org.eclipse.jdt.internal.core.search.matching;

import java.io.IOException;

import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.core.search.indexing.IIndexConstants;
import org.eclipse.jdt.internal.core.index.*;

public class SuperTypeReferencePattern extends SearchPattern {

public char[] superQualification;
public char[] superSimpleName;
public char superClassOrInterface;

public char[] pkgName;
public char[] simpleName;
public char[] enclosingTypeName;
public char classOrInterface;
public int modifiers;

protected boolean checkOnlySuperinterfaces; // used for IMPLEMENTORS

protected static char[][] CATEGORIES = { SUPER_REF };

public static char[] createIndexKey(
	int modifiers,
	char[] packageName,
	char[] typeName,
	char[][] enclosingTypeNames,
	char classOrInterface,
	char[] superTypeName,
	char superClassOrInterface) {

	if (superTypeName == null)
		superTypeName = OBJECT;
	char[] superSimpleName = CharOperation.lastSegment(superTypeName, '.');
	char[] superQualification = null;
	if (superSimpleName != superTypeName) {
		int length = superTypeName.length - superSimpleName.length - 1;
		superQualification = new char[length];
		System.arraycopy(superTypeName, 0, superQualification, 0, length);
	}

	// if the supertype name contains a $, then split it into: source name and append the $ prefix to the qualification
	//	e.g. p.A$B ---> p.A$ + B
	char[] superTypeSourceName = CharOperation.lastSegment(superSimpleName, '$');
	if (superTypeSourceName != superSimpleName) {
		int start = superQualification == null ? 0 : superQualification.length + 1;
		int prefixLength = superSimpleName.length - superTypeSourceName.length;
		char[] mangledQualification = new char[start + prefixLength];
		if (superQualification != null) {
			System.arraycopy(superQualification, 0, mangledQualification, 0, start-1);
			mangledQualification[start-1] = '.';
		}
		System.arraycopy(superSimpleName, 0, mangledQualification, start, prefixLength);
		superQualification = mangledQualification;
		superSimpleName = superTypeSourceName;
	}

	char[] simpleName = CharOperation.lastSegment(typeName, '.');
	char[] enclosingTypeName = CharOperation.concatWith(enclosingTypeNames, '$');
	if (superQualification != null && CharOperation.equals(superQualification, packageName))
		packageName = ONE_ZERO; // save some space

	// superSimpleName / superQualification / simpleName / enclosingTypeName / packageName / superClassOrInterface classOrInterface modifiers
	int superLength = superSimpleName == null ? 0 : superSimpleName.length;
	int superQLength = superQualification == null ? 0 : superQualification.length;
	int simpleLength = simpleName == null ? 0 : simpleName.length;
	int enclosingLength = enclosingTypeName == null ? 0 : enclosingTypeName.length;
	int packageLength = packageName == null ? 0 : packageName.length;
	char[] result = new char[superLength + superQLength + simpleLength + enclosingLength + packageLength + 8];
	int pos = 0;
	if (superLength > 0) {
		System.arraycopy(superSimpleName, 0, result, pos, superLength);
		pos += superLength;
	}
	result[pos++] = SEPARATOR;
	if (superQLength > 0) {
		System.arraycopy(superQualification, 0, result, pos, superQLength);
		pos += superQLength;
	}
	result[pos++] = SEPARATOR;
	if (simpleLength > 0) {
		System.arraycopy(simpleName, 0, result, pos, simpleLength);
		pos += simpleLength;
	}
	result[pos++] = SEPARATOR;
	if (enclosingLength > 0) {
		System.arraycopy(enclosingTypeName, 0, result, pos, enclosingLength);
		pos += enclosingLength;
	}
	result[pos++] = SEPARATOR;
	if (packageLength > 0) {
		System.arraycopy(packageName, 0, result, pos, packageLength);
		pos += packageLength;
	}
	result[pos++] = SEPARATOR;
	result[pos++] = superClassOrInterface;
	result[pos++] = classOrInterface;
	result[pos] = (char) modifiers;
	return result;
}

public SuperTypeReferencePattern(
	char[] superQualification,
	char[] superSimpleName,
	boolean checkOnlySuperinterfaces,
	int matchRule) {

	this(matchRule);

	this.superQualification = isCaseSensitive() ? superQualification : CharOperation.toLowerCase(superQualification);
	this.superSimpleName = isCaseSensitive() ? superSimpleName : CharOperation.toLowerCase(superSimpleName);
	((InternalSearchPattern)this).mustResolve = superQualification != null;
	this.checkOnlySuperinterfaces = checkOnlySuperinterfaces; // ie. skip the superclass
}
SuperTypeReferencePattern(int matchRule) {
	super(SUPER_REF_PATTERN, matchRule);
}
/*
 * superSimpleName / superQualification / simpleName / enclosingTypeName / pkgName / superClassOrInterface classOrInterface modifiers
 */
public void decodeIndexKey(char[] key) {
	int slash = CharOperation.indexOf(SEPARATOR, key, 0);
	this.superSimpleName = CharOperation.subarray(key, 0, slash);

	// some values may not have been know when indexed so decode as null
	int start = slash + 1;
	slash = CharOperation.indexOf(SEPARATOR, key, start);
	this.superQualification = slash == start ? null : CharOperation.subarray(key, start, slash);

	slash = CharOperation.indexOf(SEPARATOR, key, start = slash + 1);
	this.simpleName = CharOperation.subarray(key, start, slash);

	slash = CharOperation.indexOf(SEPARATOR, key, start = slash + 1);
	if (slash == start) {
		this.enclosingTypeName = null;
	} else {
		char[] names = CharOperation.subarray(key, start, slash);
		this.enclosingTypeName = CharOperation.equals(ONE_ZERO, names) ? ONE_ZERO : names;
	}

	slash = CharOperation.indexOf(SEPARATOR, key, start = slash + 1);
	if (slash == start) {
		this.pkgName = null;
	} else {
		char[] names = CharOperation.subarray(key, start, slash);
		this.pkgName = CharOperation.equals(ONE_ZERO, names) ? this.superQualification : names;
	}

	this.superClassOrInterface = key[slash + 1];
	this.classOrInterface = key[slash + 1];
	this.modifiers = key[slash + 2]; // implicit cast to int type
}
public SearchPattern getBlankPattern() {
	return new SuperTypeReferencePattern(R_EXACT_MATCH | R_CASE_SENSITIVE);
}
public char[][] getMatchCategories() {
	return CATEGORIES;
}
public boolean matchesDecodedKey(SearchPattern decodedPattern) {
	SuperTypeReferencePattern pattern = (SuperTypeReferencePattern) decodedPattern;
	if (this.checkOnlySuperinterfaces)
		if (pattern.superClassOrInterface != IIndexConstants.INTERFACE_SUFFIX) return false;

	if (pattern.superQualification != null)
		if (!matchesName(this.superQualification, pattern.superQualification)) return false;

	return matchesName(this.superSimpleName, pattern.superSimpleName);
}
EntryResult[] queryIn(Index index) throws IOException {
	char[] key = this.superSimpleName; // can be null
	int matchRule = getMatchRule();

	// cannot include the superQualification since it may not exist in the index
	switch(getMatchMode()) {
		case R_EXACT_MATCH :
			// do a prefix query with the superSimpleName
			matchRule = matchRule - R_EXACT_MATCH + R_PREFIX_MATCH;
			if (this.superSimpleName != null)
				key = CharOperation.append(this.superSimpleName, SEPARATOR);
			break;
		case R_PREFIX_MATCH :
			// do a prefix query with the superSimpleName
			break;
		case R_PATTERN_MATCH :
			// do a pattern query with the superSimpleName
			break;
	}

	return index.query(getMatchCategories(), key, matchRule); // match rule is irrelevant when the key is null
}
public String toString(){
	StringBuffer buffer = new StringBuffer(20);
	buffer.append(
		this.checkOnlySuperinterfaces
			? "SuperInterfaceReferencePattern: <" //$NON-NLS-1$
			: "SuperTypeReferencePattern: <"); //$NON-NLS-1$
	if (superSimpleName != null) 
		buffer.append(superSimpleName);
	else
		buffer.append("*"); //$NON-NLS-1$
	buffer.append(">, "); //$NON-NLS-1$
	switch(getMatchMode()) {
		case R_EXACT_MATCH : 
			buffer.append("exact match, "); //$NON-NLS-1$
			break;
		case R_PREFIX_MATCH :
			buffer.append("prefix match, "); //$NON-NLS-1$
			break;
		case R_PATTERN_MATCH :
			buffer.append("pattern match, "); //$NON-NLS-1$
			break;
	}
	if (isCaseSensitive())
		buffer.append("case sensitive"); //$NON-NLS-1$
	else
		buffer.append("case insensitive"); //$NON-NLS-1$
	return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7260.java