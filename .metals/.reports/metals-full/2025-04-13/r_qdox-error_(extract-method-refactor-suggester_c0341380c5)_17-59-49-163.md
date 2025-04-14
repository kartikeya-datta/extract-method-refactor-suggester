error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2218.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2218.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2218.java
text:
```scala
a@@cceptMatch((String) names[i], containerPath, separator, null/*no pattern*/, requestor, participant, scope, progressMonitor); // AndPatterns cannot provide the decoded result

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.compiler.util.SimpleSet;
import org.eclipse.jdt.internal.core.index.*;
import org.eclipse.jdt.internal.core.search.IndexQueryRequestor;

/**
 * Query the index multiple times and do an 'and' on the results.
 */
public abstract class IntersectingPattern extends JavaSearchPattern {

public IntersectingPattern(int patternKind, int matchRule) {
	super(patternKind, matchRule);
}
public void findIndexMatches(Index index, IndexQueryRequestor requestor, SearchParticipant participant, IJavaSearchScope scope, IProgressMonitor progressMonitor) throws IOException {
	if (progressMonitor != null && progressMonitor.isCanceled()) throw new OperationCanceledException();

	resetQuery();
	SimpleSet intersectedNames = null;
	try {
		index.startQuery();
		do {
			SearchPattern pattern = currentPattern();
			EntryResult[] entries = pattern.queryIn(index);
			if (entries == null) return;

			SearchPattern decodedResult = pattern.getBlankPattern();
			SimpleSet newIntersectedNames = new SimpleSet(3);
			for (int i = 0, l = entries.length; i < l; i++) {
				if (progressMonitor != null && progressMonitor.isCanceled()) throw new OperationCanceledException();

				EntryResult entry = entries[i];
				decodedResult.decodeIndexKey(entry.getWord());
				if (pattern.matchesDecodedKey(decodedResult)) {
					String[] names = entry.getDocumentNames(index);
					if (intersectedNames != null) {
						for (int j = 0, n = names.length; j < n; j++)
							if (intersectedNames.includes(names[j]))
								newIntersectedNames.add(names[j]);
					} else {
						for (int j = 0, n = names.length; j < n; j++)
							newIntersectedNames.add(names[j]);
					}
				}
			}

			if (newIntersectedNames.elementSize == 0) return;
			intersectedNames = newIntersectedNames;
		} while (hasNextQuery());
	} finally {
		index.stopQuery();
	}

	String containerPath = index.containerPath;
	char separator = index.separator;
	Object[] names = intersectedNames.values;
	for (int i = 0, l = names.length; i < l; i++)
		if (names[i] != null)
			acceptMatch((String) names[i], containerPath, separator, null/*no pattern*/, requestor, participant, scope); // AndPatterns cannot provide the decoded result
}
/**
 * Returns whether another query must be done.
 */
protected abstract boolean hasNextQuery();
/**
 * Resets the query and prepares this pattern to be queried.
 */
protected abstract void resetQuery();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2218.java