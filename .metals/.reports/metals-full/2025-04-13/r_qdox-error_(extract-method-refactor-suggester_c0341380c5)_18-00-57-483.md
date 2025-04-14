error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9176.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9176.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9176.java
text:
```scala
i@@f (this.isCaseSensitive || this.isCamelCase) {

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.search.matching;

import java.io.IOException;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.internal.core.index.*;

public class MultiTypeDeclarationPattern extends JavaSearchPattern {

public char[][] simpleNames;
public char[][] qualifications;

// set to CLASS_SUFFIX for only matching classes 
// set to INTERFACE_SUFFIX for only matching interfaces
// set to ENUM_SUFFIX for only matching enums
// set to ANNOTATION_TYPE_SUFFIX for only matching annotation types
// set to TYPE_SUFFIX for matching both classes and interfaces
public char typeSuffix;

protected static char[][] CATEGORIES = { TYPE_DECL };

public MultiTypeDeclarationPattern(
	char[][] qualifications,
	char[][] simpleNames,
	char typeSuffix,
	int matchRule) {

	this(matchRule);

	if (isCaseSensitive() || qualifications == null) {
		this.qualifications = qualifications;
	} else {
		int length = qualifications.length;
		this.qualifications = new char[length][];
		for (int i = 0; i < length; i++)
			this.qualifications[i] = CharOperation.toLowerCase(qualifications[i]);
	}
	// null simple names are allowed (should return all names)
	if (simpleNames != null) {
		if ((isCaseSensitive() || isCamelCase()) ) {
			this.simpleNames = simpleNames;
		} else {
			int length = simpleNames.length;
			this.simpleNames = new char[length][];
			for (int i = 0; i < length; i++)
				this.simpleNames[i] = CharOperation.toLowerCase(simpleNames[i]);
		}
	}
	this.typeSuffix = typeSuffix;

	((InternalSearchPattern)this).mustResolve = typeSuffix != TYPE_SUFFIX; // only used to report type declarations, not their positions
}
MultiTypeDeclarationPattern(int matchRule) {
	super(TYPE_DECL_PATTERN, matchRule);
}
public SearchPattern getBlankPattern() {
	return new QualifiedTypeDeclarationPattern(R_EXACT_MATCH | R_CASE_SENSITIVE);
}
public char[][] getIndexCategories() {
	return CATEGORIES;
}
public boolean matchesDecodedKey(SearchPattern decodedPattern) {
	QualifiedTypeDeclarationPattern pattern = (QualifiedTypeDeclarationPattern) decodedPattern;
	
	// check type suffix
	if (this.typeSuffix != pattern.typeSuffix && typeSuffix != TYPE_SUFFIX) {
		if (!matchDifferentTypeSuffixes(this.typeSuffix, pattern.typeSuffix)) {
			return false;
		}
	}

	// check qualified name
	if (this.qualifications != null) {
		int count = 0;
		int max = this.qualifications.length;
		if (max == 0 && pattern.qualification.length > 0) {
			return false;
		}
		if (max > 0) {
			for (; count < max; count++)
				if (matchesName(this.qualifications[count], pattern.qualification))
					break;
			if (count == max) return false;
		}
	}

	// check simple name (null are allowed)
	if (this.simpleNames == null) return true;
	int count = 0;
	int max = this.simpleNames.length;
	for (; count < max; count++)
		if (matchesName(this.simpleNames[count], pattern.simpleName))
			break;
	return count < max;
}
EntryResult[] queryIn(Index index) throws IOException {
	if (this.simpleNames == null) {
		// if no simple names then return all possible ones from index
		return index.query(getIndexCategories(), null, -1); // match rule is irrelevant when the key is null
	}

	int count = -1;
	int numOfNames = this.simpleNames.length;
	EntryResult[][] allResults = numOfNames > 1 ? new EntryResult[numOfNames][] : null;
	for (int i = 0; i < numOfNames; i++) {
		char[] key = this.simpleNames[i];
		int matchRule = getMatchRule();

		switch(getMatchMode()) {
			case R_PREFIX_MATCH :
				// do a prefix query with the simpleName
				break;
			case R_EXACT_MATCH :
				if (!this.isCamelCase) {
					// do a prefix query with the simpleName
					matchRule &= ~R_EXACT_MATCH;
					matchRule |= R_PREFIX_MATCH;
					key = CharOperation.append(key, SEPARATOR);
				}
				break;
			case R_PATTERN_MATCH :
				if (key[key.length - 1] != '*')
					key = CharOperation.concat(key, ONE_STAR, SEPARATOR);
				break;
			case R_REGEXP_MATCH :
				// TODO (frederic) implement regular expression match
				break;
		}

		EntryResult[] entries = index.query(getIndexCategories(), key, matchRule); // match rule is irrelevant when the key is null
		if (entries != null) {
			if (allResults == null) return entries;
			allResults[++count] = entries;
		}
	}

	if (count == -1) return null;
	int total = 0;
	for (int i = 0; i <= count; i++)
		total += allResults[i].length;
	EntryResult[] allEntries = new EntryResult[total];
	int next = 0;
	for (int i = 0; i <= count; i++) {
		EntryResult[] entries = allResults[i];
		System.arraycopy(entries, 0, allEntries, next, entries.length);
		next += entries.length;
	}
	return allEntries;
}
protected StringBuffer print(StringBuffer output) {
	switch (this.typeSuffix){
		case CLASS_SUFFIX :
			output.append("MultiClassDeclarationPattern: "); //$NON-NLS-1$
			break;
		case CLASS_AND_INTERFACE_SUFFIX :
			output.append("MultiClassAndInterfaceDeclarationPattern: "); //$NON-NLS-1$
			break;
		case CLASS_AND_ENUM_SUFFIX :
			output.append("MultiClassAndEnumDeclarationPattern: "); //$NON-NLS-1$
			break;
		case INTERFACE_SUFFIX :
			output.append("MultiInterfaceDeclarationPattern: "); //$NON-NLS-1$
			break;
		case INTERFACE_AND_ANNOTATION_SUFFIX :
			output.append("MultiInterfaceAndAnnotationDeclarationPattern: "); //$NON-NLS-1$
			break;
		case ENUM_SUFFIX :
			output.append("MultiEnumDeclarationPattern: "); //$NON-NLS-1$
			break;
		case ANNOTATION_TYPE_SUFFIX :
			output.append("MultiAnnotationTypeDeclarationPattern: "); //$NON-NLS-1$
			break;
		default :
			output.append("MultiTypeDeclarationPattern: "); //$NON-NLS-1$
			break;
	}
	if (qualifications != null) {
		output.append("qualifications: <"); //$NON-NLS-1$
		for (int i = 0; i < qualifications.length; i++){
			output.append(qualifications[i]);
			if (i < qualifications.length - 1)
				output.append(", "); //$NON-NLS-1$
		}
		output.append("> "); //$NON-NLS-1$
	}
	if (simpleNames != null) {
		output.append("simpleNames: <"); //$NON-NLS-1$
		for (int i = 0; i < simpleNames.length; i++){
			output.append(simpleNames[i]);
			if (i < simpleNames.length - 1)
				output.append(", "); //$NON-NLS-1$
		}
		output.append(">"); //$NON-NLS-1$
	}
	return super.print(output);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9176.java