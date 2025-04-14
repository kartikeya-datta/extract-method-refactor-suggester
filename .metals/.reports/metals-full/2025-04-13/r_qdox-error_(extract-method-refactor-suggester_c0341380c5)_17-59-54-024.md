error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5452.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5452.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5452.java
text:
```scala
i@@f (this.pattern.findReferences || this.pattern.fineGrain != 0)

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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.core.LocalVariable;

public class LocalVariableLocator extends VariableLocator {

public LocalVariableLocator(LocalVariablePattern pattern) {
	super(pattern);
}
public int match(LocalDeclaration node, MatchingNodeSet nodeSet) {
	int referencesLevel = IMPOSSIBLE_MATCH;
	if (this.pattern.findReferences)
		// must be a write only access with an initializer
		if (this.pattern.writeAccess && !this.pattern.readAccess && node.initialization != null)
			if (matchesName(this.pattern.name, node.name))
				referencesLevel = ((InternalSearchPattern)this.pattern).mustResolve ? POSSIBLE_MATCH : ACCURATE_MATCH;

	int declarationsLevel = IMPOSSIBLE_MATCH;
	if (this.pattern.findDeclarations)
		if (matchesName(this.pattern.name, node.name))
			if (node.declarationSourceStart == getLocalVariable().declarationSourceStart)
				declarationsLevel = ((InternalSearchPattern)this.pattern).mustResolve ? POSSIBLE_MATCH : ACCURATE_MATCH;

	return nodeSet.addMatch(node, referencesLevel >= declarationsLevel ? referencesLevel : declarationsLevel); // use the stronger match
}
private LocalVariable getLocalVariable() {
	return ((LocalVariablePattern) this.pattern).localVariable;
}
protected void matchReportReference(ASTNode reference, IJavaElement element, Binding elementBinding, int accuracy, MatchLocator locator) throws CoreException {
	int offset = -1;
	int length = -1;
	if (reference instanceof SingleNameReference) {
		offset = reference.sourceStart;
		length = reference.sourceEnd-offset+1;
	} else if (reference instanceof QualifiedNameReference) {
		QualifiedNameReference qNameRef = (QualifiedNameReference) reference;
		long sourcePosition = qNameRef.sourcePositions[0];
		offset = (int) (sourcePosition >>> 32);
		length = ((int) sourcePosition) - offset +1;
	} else if (reference instanceof LocalDeclaration) {
		LocalVariable localVariable = getLocalVariable();
		offset = localVariable.nameStart;
		length = localVariable.nameEnd-offset+1;
		element = localVariable;
		this.match = locator.newDeclarationMatch(element, null, accuracy, offset, length);
		locator.report(match);
		return;
	}
	if (offset >= 0) {
		match = locator.newLocalVariableReferenceMatch(element, accuracy, offset, length, reference);
		locator.report(match);
	}
}
protected int matchContainer() {
	return METHOD_CONTAINER;
}
protected int matchLocalVariable(LocalVariableBinding variable, boolean matchName) {
	if (variable == null) return INACCURATE_MATCH;

	if (matchName && !matchesName(this.pattern.name, variable.readableName())) return IMPOSSIBLE_MATCH;

	return variable.declaration.declarationSourceStart == getLocalVariable().declarationSourceStart
		? ACCURATE_MATCH
		: IMPOSSIBLE_MATCH;
}
protected int referenceType() {
	return IJavaElement.LOCAL_VARIABLE;
}
public int resolveLevel(ASTNode possiblelMatchingNode) {
	if (this.pattern.findReferences)
		if (possiblelMatchingNode instanceof NameReference)
			return resolveLevel((NameReference) possiblelMatchingNode);
	if (possiblelMatchingNode instanceof LocalDeclaration)
		return matchLocalVariable(((LocalDeclaration) possiblelMatchingNode).binding, true);
	return IMPOSSIBLE_MATCH;
}
public int resolveLevel(Binding binding) {
	if (binding == null) return INACCURATE_MATCH;
	if (!(binding instanceof LocalVariableBinding)) return IMPOSSIBLE_MATCH;

	return matchLocalVariable((LocalVariableBinding) binding, true);
}
protected int resolveLevel(NameReference nameRef) {
	return resolveLevel(nameRef.binding);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5452.java