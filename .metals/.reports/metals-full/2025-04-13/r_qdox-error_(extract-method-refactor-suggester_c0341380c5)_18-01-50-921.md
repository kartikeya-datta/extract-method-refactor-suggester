error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5427.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5427.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5427.java
text:
```scala
r@@eturn this.findReferences; // need to check resolved default constructors and explicit constructor calls

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.search.matching;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.internal.core.index.IEntryResult;
import org.eclipse.jdt.internal.core.index.impl.IndexInput;
import org.eclipse.jdt.internal.core.search.IIndexSearchRequestor;

public class ConstructorPattern extends SearchPattern {

protected boolean findDeclarations;
protected boolean findReferences;

protected char[] declaringQualification;
protected char[] declaringSimpleName;

protected char[][] parameterQualifications;
protected char[][] parameterSimpleNames;

protected char[] decodedTypeName;
protected int decodedParameterCount;

// extra reference info
protected IType declaringType;

protected char[] currentTag;

public static char[] createDeclaration(char[] typeName, int argCount) {
	char[] countChars = argCount < 10 ? COUNTS[argCount] : ("/" + String.valueOf(argCount)).toCharArray(); //$NON-NLS-1$
	return CharOperation.concat(CONSTRUCTOR_DECL, typeName, countChars);
}
public static char[] createReference(char[] typeName, int argCount) {
	char[] countChars = argCount < 10 ? COUNTS[argCount] : ("/" + String.valueOf(argCount)).toCharArray(); //$NON-NLS-1$
	return CharOperation.concat(CONSTRUCTOR_REF, typeName, countChars);
}


public ConstructorPattern(
	boolean findDeclarations,
	boolean findReferences,
	char[] declaringSimpleName,
	int matchMode,
	boolean isCaseSensitive,
	char[] declaringQualification,
	char[][] parameterQualifications,
	char[][] parameterSimpleNames,
	IType declaringType) {

	super(CONSTRUCTOR_PATTERN, matchMode, isCaseSensitive);

	this.findDeclarations = findDeclarations;
	this.findReferences = findReferences;

	this.declaringQualification = isCaseSensitive ? declaringQualification : CharOperation.toLowerCase(declaringQualification);
	this.declaringSimpleName = isCaseSensitive ? declaringSimpleName : CharOperation.toLowerCase(declaringSimpleName);
	if (parameterSimpleNames != null) {
		this.parameterQualifications = new char[parameterSimpleNames.length][];
		this.parameterSimpleNames = new char[parameterSimpleNames.length][];
		for (int i = 0, max = parameterSimpleNames.length; i < max; i++) {
			this.parameterQualifications[i] = isCaseSensitive ? parameterQualifications[i] : CharOperation.toLowerCase(parameterQualifications[i]);
			this.parameterSimpleNames[i] = isCaseSensitive ? parameterSimpleNames[i] : CharOperation.toLowerCase(parameterSimpleNames[i]);
		}
	}

	this.declaringType = declaringType;
	this.mustResolve = mustResolve();
}
protected void acceptPath(IIndexSearchRequestor requestor, String path) {
	if (this.currentTag ==  CONSTRUCTOR_REF)
		requestor.acceptConstructorReference(path, this.decodedTypeName, this.decodedParameterCount);
	else
		requestor.acceptConstructorDeclaration(path, this.decodedTypeName, this.decodedParameterCount);
}
protected void decodeIndexEntry(IEntryResult entryResult){
	char[] word = entryResult.getWord();
	int size = word.length;
	int lastSeparatorIndex = CharOperation.lastIndexOf(SEPARATOR, word);	

	this.decodedParameterCount = Integer.parseInt(new String(word, lastSeparatorIndex + 1, size - lastSeparatorIndex - 1));
	this.decodedTypeName = CharOperation.subarray(word, currentTag.length, lastSeparatorIndex);
}
public void findIndexMatches(IndexInput input, IIndexSearchRequestor requestor, int detailLevel, IProgressMonitor progressMonitor, IJavaSearchScope scope) throws IOException {
	if (progressMonitor != null && progressMonitor.isCanceled()) throw new OperationCanceledException();

	// in the new story this will be a single call with a mask
	if (this.findReferences) {
		this.currentTag = CONSTRUCTOR_REF;
		super.findIndexMatches(input, requestor, detailLevel, progressMonitor, scope);
	}
	if (this.findDeclarations) {
		this.currentTag = CONSTRUCTOR_DECL;
		super.findIndexMatches(input, requestor, detailLevel, progressMonitor, scope);
	}
}
/**
 * Constructor declaration entries are encoded as 'constructorDecl/' TypeName '/' Arity:
 * e.g. 'constructorDecl/X/0'
 *
 * Constructor reference entries are encoded as 'constructorRef/' TypeName '/' Arity:
 * e.g. 'constructorRef/X/0'
 */
protected char[] indexEntryPrefix() {
	// will have a common pattern in the new story
	if (this.isCaseSensitive && this.declaringSimpleName != null) {
		switch(this.matchMode) {
			case EXACT_MATCH :
				int arity = this.parameterSimpleNames == null ? -1 : this.parameterSimpleNames.length;
				if (arity >= 0) {
					char[] countChars = arity < 10 ? COUNTS[arity] : ("/" + String.valueOf(arity)).toCharArray(); //$NON-NLS-1$
					return CharOperation.concat(this.currentTag, this.declaringSimpleName, countChars);
				}
			case PREFIX_MATCH :
				return CharOperation.concat(this.currentTag, this.declaringSimpleName);
			case PATTERN_MATCH :
				int starPos = CharOperation.indexOf('*', this.declaringSimpleName);
				switch(starPos) {
					case -1 :
						return CharOperation.concat(this.currentTag, this.declaringSimpleName);
					default : 
						int length = this.currentTag.length;
						char[] result = new char[length + starPos];
						System.arraycopy(this.currentTag, 0, result, 0, length);
						System.arraycopy(this.declaringSimpleName, 0, result, length, starPos);
						return result;
					case 0 : // fall through
				}
		}
	}
	return this.currentTag; // find them all
}
/**
 * @see SearchPattern#matchIndexEntry
 */
protected boolean matchIndexEntry() {
	if (parameterSimpleNames != null && parameterSimpleNames.length != decodedParameterCount) return false;

	if (declaringSimpleName != null) {
		switch(matchMode) {
			case EXACT_MATCH :
				return CharOperation.equals(declaringSimpleName, decodedTypeName, isCaseSensitive);
			case PREFIX_MATCH :
				return CharOperation.prefixEquals(declaringSimpleName, decodedTypeName, isCaseSensitive);
			case PATTERN_MATCH :
				return CharOperation.match(declaringSimpleName, decodedTypeName, isCaseSensitive);
		}
	}
	return true;
}
protected boolean mustResolve() {
	if (this.declaringQualification != null) return true;

	// parameter types
	if (this.parameterSimpleNames != null)
		for (int i = 0, max = this.parameterSimpleNames.length; i < max; i++)
			if (this.parameterQualifications[i] != null) return true;
	return false;
}
public String toString() {
	StringBuffer buffer = new StringBuffer(20);
	if (this.findDeclarations) {
		buffer.append(this.findReferences
			? "ConstructorCombinedPattern: " //$NON-NLS-1$
			: "ConstructorDeclarationPattern: "); //$NON-NLS-1$
	} else {
		buffer.append("ConstructorReferencePattern: "); //$NON-NLS-1$
	}
	if (declaringQualification != null)
		buffer.append(declaringQualification).append('.');
	if (declaringSimpleName != null) 
		buffer.append(declaringSimpleName);
	else if (declaringQualification != null)
		buffer.append("*"); //$NON-NLS-1$

	buffer.append('(');
	if (parameterSimpleNames == null) {
		buffer.append("..."); //$NON-NLS-1$
	} else {
		for (int i = 0, max = parameterSimpleNames.length; i < max; i++) {
			if (i > 0) buffer.append(", "); //$NON-NLS-1$
			if (parameterQualifications[i] != null) buffer.append(parameterQualifications[i]).append('.');
			if (parameterSimpleNames[i] == null) buffer.append('*'); else buffer.append(parameterSimpleNames[i]);
		}
	}
	buffer.append(')');
	buffer.append(", "); //$NON-NLS-1$
	switch(matchMode) {
		case EXACT_MATCH : 
			buffer.append("exact match, "); //$NON-NLS-1$
			break;
		case PREFIX_MATCH :
			buffer.append("prefix match, "); //$NON-NLS-1$
			break;
		case PATTERN_MATCH :
			buffer.append("pattern match, "); //$NON-NLS-1$
			break;
	}
	buffer.append(isCaseSensitive ? "case sensitive" : "case insensitive"); //$NON-NLS-1$ //$NON-NLS-2$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5427.java