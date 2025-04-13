error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/765.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/765.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/765.java
text:
```scala
r@@eturn nodeSet.addMatch(node, ((InternalSearchPattern)this.pattern).mustResolve ? POSSIBLE_MATCH : ACCURATE_MATCH);

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
package org.eclipse.jdt.internal.core.search.matching;

import org.eclipse.jdt.internal.compiler.ast.*;

public class VariableLocator extends PatternLocator {

protected VariablePattern pattern;

public VariableLocator(VariablePattern pattern) {
	super(pattern);

	this.pattern = pattern;
}
public int match(Expression node, MatchingNodeSet nodeSet) { // interested in Assignment
	if (this.pattern.writeAccess) {
		if (this.pattern.readAccess) return IMPOSSIBLE_MATCH; // already checked the lhs in match(Reference...) before we reached here

		if (node instanceof Assignment) {
			Expression lhs = ((Assignment) node).lhs;
			if (lhs instanceof Reference)
				return matchReference((Reference) lhs, nodeSet, true);
		}
	} else if (this.pattern.readAccess) {
		if (node instanceof Assignment && !(node instanceof CompoundAssignment)) {
			// the lhs of a simple assignment may be added in match(Reference...) before we reach here
			// for example, the fieldRef to 'this.x' in the statement this.x = x; is not considered a readAccess
			Expression lhs = ((Assignment) node).lhs;
			nodeSet.removePossibleMatch(lhs);
			nodeSet.removeTrustedMatch(lhs);
		}
	}
	return IMPOSSIBLE_MATCH;
}
public int match(Reference node, MatchingNodeSet nodeSet) { // interested in NameReference & its subtypes
	return this.pattern.readAccess
		? matchReference(node, nodeSet, false)
		: IMPOSSIBLE_MATCH;
}
protected int matchReference(Reference node, MatchingNodeSet nodeSet, boolean writeOnlyAccess) {
	if (node instanceof NameReference) {
		if (this.pattern.name == null) {
			return nodeSet.addMatch(node, this.pattern.mustResolve ? POSSIBLE_MATCH : ACCURATE_MATCH);
		} else if (node instanceof SingleNameReference) {
			if (matchesName(this.pattern.name, ((SingleNameReference) node).token))
				return nodeSet.addMatch(node, POSSIBLE_MATCH);
		} else {
			QualifiedNameReference qNameRef = (QualifiedNameReference) node;
			char[][] tokens = qNameRef.tokens;
			if (writeOnlyAccess) {
				// in the case of the assigment of a qualified name reference, the match must be on the last token
				if (matchesName(this.pattern.name, tokens[tokens.length-1]))
					return nodeSet.addMatch(node, POSSIBLE_MATCH);
			} else {
				for (int i = 0, max = tokens.length; i < max; i++)
					if (matchesName(this.pattern.name, tokens[i]))
						return nodeSet.addMatch(node, POSSIBLE_MATCH);
			}
		}
	}
	return IMPOSSIBLE_MATCH;
}
public String toString() {
	return "Locator for " + this.pattern.toString(); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/765.java