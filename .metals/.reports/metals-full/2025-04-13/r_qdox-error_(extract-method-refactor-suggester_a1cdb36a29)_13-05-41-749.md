error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7445.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7445.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7445.java
text:
```scala
i@@f (matchRule == (SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE) && key != null) {

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
package org.eclipse.jdt.internal.core.index;

import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.core.util.*;
import org.eclipse.jdt.internal.compiler.util.HashtableOfObject;
import org.eclipse.jdt.internal.compiler.util.SimpleLookupTable;

public class MemoryIndex {

public int NUM_CHANGES = 100; // number of separate document changes... used to decide when to merge

SimpleLookupTable docsToReferences; // document paths -> HashtableOfObject(category names -> set of words)
SimpleWordSet allWords; // save space by locally interning the referenced words, since an indexer can generate numerous duplicates

MemoryIndex() {
	this.docsToReferences = new SimpleLookupTable(7);
	this.allWords = new SimpleWordSet(7);
}
void addDocumentNames(String substring, SimpleSet results) {
	// assumed the disk index already skipped over documents which have been added/changed/deleted
	Object[] paths = this.docsToReferences.keyTable;
	Object[] referenceTables = this.docsToReferences.valueTable;
	if (substring == null) { // add all new/changed documents
		for (int i = 0, l = referenceTables.length; i < l; i++)
			if (referenceTables[i] != null)
				results.add(paths[i]);
	} else {
		for (int i = 0, l = referenceTables.length; i < l; i++)
			if (referenceTables[i] != null && ((String) paths[i]).startsWith(substring, 0))
				results.add(paths[i]);
	}
}
void addIndexEntry(char[] category, char[] key, String documentName) {
	// assumed a document was removed before its reindexed
	HashtableOfObject referenceTable = (HashtableOfObject) this.docsToReferences.get(documentName);
	if (referenceTable == null)
		this.docsToReferences.put(documentName, referenceTable = new HashtableOfObject(3));

	SimpleWordSet existingWords = (SimpleWordSet) referenceTable.get(category);
	if (existingWords == null)
		referenceTable.put(category, existingWords = new SimpleWordSet(1));

	existingWords.add(this.allWords.add(key));
}
HashtableOfObject addQueryResults(char[][] categories, char[] key, int matchRule, HashtableOfObject results) {
	// assumed the disk index already skipped over documents which have been added/changed/deleted
	// results maps a word -> EntryResult
	Object[] paths = this.docsToReferences.keyTable;
	Object[] referenceTables = this.docsToReferences.valueTable;
	if (matchRule == (SearchPattern.R_EXACT_MATCH + SearchPattern.R_CASE_SENSITIVE) && key != null) {
		nextPath : for (int i = 0, l = referenceTables.length; i < l; i++) {
			HashtableOfObject categoryToWords = (HashtableOfObject) referenceTables[i];
			if (categoryToWords != null) {
				for (int j = 0, m = categories.length; j < m; j++) {
					SimpleWordSet wordSet = (SimpleWordSet) categoryToWords.get(categories[j]);
					if (wordSet != null && wordSet.includes(key)) {
						if (results == null)
							results = new HashtableOfObject(13);
						EntryResult result = (EntryResult) results.get(key);
						if (result == null)
							results.put(key, result = new EntryResult(key, null));
						result.addDocumentName((String) paths[i]);
						continue nextPath;
					}
				}
			}
		}
	} else {
		for (int i = 0, l = referenceTables.length; i < l; i++) {
			HashtableOfObject categoryToWords = (HashtableOfObject) referenceTables[i];
			if (categoryToWords != null) {
				for (int j = 0, m = categories.length; j < m; j++) {
					SimpleWordSet wordSet = (SimpleWordSet) categoryToWords.get(categories[j]);
					if (wordSet != null) {
						char[][] words = wordSet.words;
						for (int k = 0, n = words.length; k < n; k++) {
							char[] word = words[k];
							if (word != null && Index.isMatch(key, word, matchRule)) {
								if (results == null)
									results = new HashtableOfObject(13);
								EntryResult result = (EntryResult) results.get(word);
								if (result == null)
									results.put(word, result = new EntryResult(word, null));
								result.addDocumentName((String) paths[i]);
							}
						}
					}
				}
			}
		}
	}
	return results;
}
boolean hasChanged() {
	return this.docsToReferences.elementSize > 0;
}
void remove(String documentName) {
	this.docsToReferences.put(documentName, null);
}
boolean shouldMerge() {
	return this.docsToReferences.elementSize >= NUM_CHANGES;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7445.java