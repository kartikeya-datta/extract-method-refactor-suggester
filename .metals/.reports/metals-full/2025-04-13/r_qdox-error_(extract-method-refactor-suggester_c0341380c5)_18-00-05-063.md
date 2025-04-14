error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6293.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6293.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6293.java
text:
```scala
i@@f (typeBinding == null || !typeBinding.isValidBinding()) return INACCURATE_MATCH;

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
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.core.search.indexing.IIndexConstants;

public class SuperTypeReferenceLocator extends PatternLocator {

protected SuperTypeReferencePattern pattern;

public SuperTypeReferenceLocator(SuperTypeReferencePattern pattern) {
	super(pattern);

	this.pattern = pattern;
}
//public int match(ASTNode node, MatchingNodeSet nodeSet) - SKIP IT
//public int match(ConstructorDeclaration node, MatchingNodeSet nodeSet) - SKIP IT
//public int match(Expression node, MatchingNodeSet nodeSet) - SKIP IT
//public int match(FieldDeclaration node, MatchingNodeSet nodeSet) - SKIP IT
//public int match(MethodDeclaration node, MatchingNodeSet nodeSet) - SKIP IT
//public int match(MessageSend node, MatchingNodeSet nodeSet) - SKIP IT
//public int match(Reference node, MatchingNodeSet nodeSet) - SKIP IT
//public int match(TypeDeclaration node, MatchingNodeSet nodeSet) - SKIP IT
public int match(TypeReference node, MatchingNodeSet nodeSet) {
	if (this.pattern.superSimpleName == null)
		return nodeSet.addMatch(node, ((InternalSearchPattern)this.pattern).mustResolve ? POSSIBLE_MATCH : ACCURATE_MATCH);

	char[] typeRefSimpleName = null;
	if (node instanceof SingleTypeReference) {
		typeRefSimpleName = ((SingleTypeReference) node).token;
	} else { // QualifiedTypeReference
		char[][] tokens = ((QualifiedTypeReference) node).tokens;
		typeRefSimpleName = tokens[tokens.length-1];
	}				
	if (matchesName(this.pattern.superSimpleName, typeRefSimpleName))
		return nodeSet.addMatch(node, ((InternalSearchPattern)this.pattern).mustResolve ? POSSIBLE_MATCH : ACCURATE_MATCH);

	return IMPOSSIBLE_MATCH;
}

protected int matchContainer() {
	return CLASS_CONTAINER;
}
/* (non-Javadoc)
 * @see org.eclipse.jdt.internal.core.search.matching.PatternLocator#matchReportReference(org.eclipse.jdt.internal.compiler.ast.ASTNode, org.eclipse.jdt.core.IJavaElement, org.eclipse.jdt.internal.compiler.lookup.Binding, int, org.eclipse.jdt.internal.core.search.matching.MatchLocator)
 */
protected void matchReportReference(ASTNode reference, IJavaElement element, Binding elementBinding, int accuracy, MatchLocator locator) throws CoreException {
	if (elementBinding instanceof ReferenceBinding) {
		ReferenceBinding referenceBinding = (ReferenceBinding) elementBinding;
		if (referenceBinding.isClass() && this.pattern.typeSuffix == IIndexConstants.INTERFACE_SUFFIX) {
			// do not report class if expected types are only interfaces
			return;
		}
		if (referenceBinding.isInterface() && this.pattern.typeSuffix == IIndexConstants.CLASS_SUFFIX) {
			// do not report interface if expected types are only classes
			return;
		}
	}
	super.matchReportReference(reference, element, elementBinding, accuracy, locator);
}
protected int referenceType() {
	return IJavaElement.TYPE;
}
public int resolveLevel(ASTNode node) {
	if (!(node instanceof TypeReference)) return IMPOSSIBLE_MATCH;

	TypeReference typeRef = (TypeReference) node;
	TypeBinding typeBinding = typeRef.resolvedType;
	if (typeBinding instanceof ArrayBinding)
		typeBinding = ((ArrayBinding) typeBinding).leafComponentType;
	if (typeBinding instanceof ProblemReferenceBinding)
		typeBinding = ((ProblemReferenceBinding) typeBinding).closestMatch();

	if (typeBinding == null) return INACCURATE_MATCH;
	return resolveLevelForType(this.pattern.superSimpleName, this.pattern.superQualification, typeBinding);
}
public int resolveLevel(Binding binding) {
	if (binding == null) return INACCURATE_MATCH;
	if (!(binding instanceof ReferenceBinding)) return IMPOSSIBLE_MATCH;

	ReferenceBinding type = (ReferenceBinding) binding;
	int level = IMPOSSIBLE_MATCH;
	if (this.pattern.superRefKind != SuperTypeReferencePattern.ONLY_SUPER_INTERFACES) {
		level = resolveLevelForType(this.pattern.superSimpleName, this.pattern.superQualification, type.superclass());
		if (level == ACCURATE_MATCH) return ACCURATE_MATCH;
	}

	if (this.pattern.superRefKind != SuperTypeReferencePattern.ONLY_SUPER_CLASSES) {
		ReferenceBinding[] superInterfaces = type.superInterfaces();
		for (int i = 0, max = superInterfaces.length; i < max; i++) {
			int newLevel = resolveLevelForType(this.pattern.superSimpleName, this.pattern.superQualification, superInterfaces[i]);
			if (newLevel > level) {
				if (newLevel == ACCURATE_MATCH) return ACCURATE_MATCH;
				level = newLevel;
			}
		}
	}
	return level;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6293.java