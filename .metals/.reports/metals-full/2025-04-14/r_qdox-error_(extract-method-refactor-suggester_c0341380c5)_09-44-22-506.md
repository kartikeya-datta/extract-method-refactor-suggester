error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3873.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3873.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3873.java
text:
```scala
r@@eturn "MethodDeclarationPattern: "; //$NON-NLS-1$

package org.eclipse.jdt.internal.core.search.matching;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.util.*;

import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.core.index.*;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.core.search.indexing.*;
import org.eclipse.jdt.internal.core.index.impl.*;
import org.eclipse.jdt.internal.core.search.*;

import java.io.*;

public class MethodDeclarationPattern extends MethodPattern {
public MethodDeclarationPattern(
	char[] selector, 
	int matchMode, 
	boolean isCaseSensitive,
	char[] declaringQualification,
	char[] declaringSimpleName,	
	char[] returnQualification, 
	char[] returnSimpleName,
	char[][] parameterQualifications, 
	char[][] parameterSimpleNames) {

	super(matchMode, isCaseSensitive);

	this.selector = isCaseSensitive ? selector : CharOperation.toLowerCase(selector);
	this.declaringQualification = isCaseSensitive ? declaringQualification : CharOperation.toLowerCase(declaringQualification);
	this.declaringSimpleName = isCaseSensitive ? declaringSimpleName : CharOperation.toLowerCase(declaringSimpleName);
	this.returnQualification = isCaseSensitive ? returnQualification : CharOperation.toLowerCase(returnQualification);
	this.returnSimpleName = isCaseSensitive ? returnSimpleName : CharOperation.toLowerCase(returnSimpleName);

	if (parameterSimpleNames != null){
		this.parameterQualifications = new char[parameterSimpleNames.length][];
		this.parameterSimpleNames = new char[parameterSimpleNames.length][];
		for (int i = 0, max = parameterSimpleNames.length; i < max; i++){
			this.parameterQualifications[i] = isCaseSensitive ? parameterQualifications[i] : CharOperation.toLowerCase(parameterQualifications[i]);
			this.parameterSimpleNames[i] = isCaseSensitive ? parameterSimpleNames[i] : CharOperation.toLowerCase(parameterSimpleNames[i]);
		}
	}	
	this.needsResolve = this.needsResolve();
}
public void decodeIndexEntry(IEntryResult entryResult){

	char[] word = entryResult.getWord();
	int size = word.length;
	int lastSeparatorIndex = CharOperation.lastIndexOf(SEPARATOR, word);	

	decodedParameterCount = Integer.parseInt(new String(word, lastSeparatorIndex + 1, size - lastSeparatorIndex - 1));
	decodedSelector = CharOperation.subarray(word, METHOD_DECL.length, lastSeparatorIndex);
}
/**
 * see SearchPattern.feedIndexRequestor
 */
public void feedIndexRequestor(IIndexSearchRequestor requestor, int detailLevel, int[] references, IndexInput input, IJavaSearchScope scope) throws IOException {
	for (int i = 0, max = references.length; i < max; i++) {
		IndexedFile file = input.getIndexedFile(references[i]);
		String path;
		if (file != null && scope.encloses(path = IndexedFile.convertPath(file.getPath()))) {
			requestor.acceptMethodDeclaration(path, decodedSelector, decodedParameterCount);
		}
	}
}
public String getPatternName(){
	return "MethodDeclarationPattern: "/*nonNLS*/;
}
/**
 * @see SearchPattern#indexEntryPrefix
 */
public char[] indexEntryPrefix() {

	return AbstractIndexer.bestMethodDeclarationPrefix(
			selector, 
			parameterSimpleNames == null ? -1 : parameterSimpleNames.length, 
			matchMode, 
			isCaseSensitive);
}
/**
 * @see SearchPattern#matchContainer()
 */
protected int matchContainer() {
	return CLASS;
}
/**
 * @see SearchPattern#matchesBinary(Object, Object)
 */
public boolean matchesBinary(Object binaryInfo, Object enclosingBinaryInfo) {
	if (!(binaryInfo instanceof IBinaryMethod)) return false;

	IBinaryMethod method = (IBinaryMethod)binaryInfo;
	
	// selector
	if (!this.matchesName(this.selector, method.getSelector()))
		return false;

	// declaring type
	IBinaryType declaringType = (IBinaryType)enclosingBinaryInfo;
	if (declaringType != null) {
		char[] declaringTypeName = (char[])declaringType.getName().clone();
		CharOperation.replace(declaringTypeName, '/', '.');
		if (!this.matchesType(this.declaringSimpleName, this.declaringQualification, declaringTypeName)) {
			return false;
		}
	}

	// return type
	String methodDescriptor = new String(method.getMethodDescriptor()).replace('/', '.');
	String returnTypeSignature = Signature.toString(Signature.getReturnType(methodDescriptor));
	if (!this.matchesType(this.returnSimpleName, this.returnQualification, returnTypeSignature.toCharArray())) {
		return false;
	}
		
	// parameter types
	int parameterCount = this.parameterSimpleNames == null ? -1 : this.parameterSimpleNames.length;
	if (parameterCount > -1) {
		String[] arguments = Signature.getParameterTypes(methodDescriptor);
		int argumentCount = arguments.length;
		if (parameterCount != argumentCount)
			return false;
		for (int i = 0; i < parameterCount; i++) {
			char[] qualification = this.parameterQualifications[i];
			char[] type = this.parameterSimpleNames[i];
			if (!this.matchesType(type, qualification, Signature.toString(arguments[i]).toCharArray()))
				return false;
		}
	}

	return true;
}

/**
 * @see SearchPattern#matchLevel(AstNode, boolean)
 */
public int matchLevel(AstNode node, boolean resolve) {
	if (!(node instanceof MethodDeclaration)) return IMPOSSIBLE_MATCH;

	MethodDeclaration method = (MethodDeclaration)node;

	if (resolve) {
		return this.matchLevel(method.binding);
	} else {
		// selector
		if (!this.matchesName(this.selector, method.selector))
			return IMPOSSIBLE_MATCH;

		// return type
		TypeReference methodReturnType = method.returnType;
		if (methodReturnType != null) {
			char[][] methodReturnTypeName = methodReturnType.getTypeName();
			char[] sourceName = this.toArrayName(
				methodReturnTypeName[methodReturnTypeName.length-1], 
				methodReturnType.dimensions());
			if (!this.matchesName(this.returnSimpleName, sourceName))
				return IMPOSSIBLE_MATCH;
		}
			
		// parameter types
		int parameterCount = this.parameterSimpleNames == null ? -1 : this.parameterSimpleNames.length;
		if (parameterCount > -1) {
			int argumentCount = method.arguments == null ? 0 : method.arguments.length;
			if (parameterCount != argumentCount)
				return IMPOSSIBLE_MATCH;
		}

		return POSSIBLE_MATCH;
	}
}

/**
 * @see SearchPattern#matchLevel(Binding)
 */
public int matchLevel(Binding binding) {
	if (binding == null) return INACCURATE_MATCH;
	if (!(binding instanceof MethodBinding)) return IMPOSSIBLE_MATCH;
	int level;

	MethodBinding method = (MethodBinding)binding;
	
	// selector
	if (!this.matchesName(this.selector, method.selector))
		return IMPOSSIBLE_MATCH;

	// declaring type
	ReferenceBinding declaringType = method.declaringClass;
	if (!method.isStatic() && !method.isPrivate()) {
		level = this.matchLevelAsSubtype(declaringType, this.declaringSimpleName, this.declaringQualification);
	} else {
		level = this.matchLevelForType(this.declaringSimpleName, this.declaringQualification, declaringType);
	}
	if (level == IMPOSSIBLE_MATCH) {
		return IMPOSSIBLE_MATCH;
	}

	// return type
	int newLevel = this.matchLevelForType(this.returnSimpleName, this.returnQualification, method.returnType);
	switch (newLevel) {
		case IMPOSSIBLE_MATCH:
			return IMPOSSIBLE_MATCH;
		case ACCURATE_MATCH: // keep previous level
			break;
		default: // ie. INACCURATE_MATCH
			level = newLevel;
			break;
	}
		
	// parameter types
	int parameterCount = this.parameterSimpleNames == null ? -1 : this.parameterSimpleNames.length;
	if (parameterCount > -1) {
		int argumentCount = method.parameters == null ? 0 : method.parameters.length;
		if (parameterCount != argumentCount)
			return IMPOSSIBLE_MATCH;
		for (int i = 0; i < parameterCount; i++) {
			char[] qualification = this.parameterQualifications[i];
			char[] type = this.parameterSimpleNames[i];
			newLevel = this.matchLevelForType(type, qualification, method.parameters[i]);
			switch (newLevel) {
				case IMPOSSIBLE_MATCH:
					return IMPOSSIBLE_MATCH;
				case ACCURATE_MATCH: // keep previous level
					break;
				default: // ie. INACCURATE_MATCH
					level = newLevel;
					break;
			}
		}
	}

	return level;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3873.java