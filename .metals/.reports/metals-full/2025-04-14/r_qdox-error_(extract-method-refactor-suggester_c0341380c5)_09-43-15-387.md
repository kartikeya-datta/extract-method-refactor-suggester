error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10073.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10073.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10073.java
text:
```scala
final i@@nt HasDirectWildcard = ASTNode.Bit31; // set for parameterized types directly referencing wildcards

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
package org.eclipse.jdt.internal.compiler.lookup;

import org.eclipse.jdt.internal.compiler.ast.ASTNode;

public interface TagBits {
    
	// Tag bits in the tagBits int of every TypeBinding
	final int IsArrayType = ASTNode.Bit1;
	final int IsBaseType = ASTNode.Bit2;
	final int IsNestedType = ASTNode.Bit3;
	final int IsMemberType = ASTNode.Bit4;
	final int MemberTypeMask = IsNestedType | IsMemberType;
	final int IsLocalType = ASTNode.Bit5;
	final int LocalTypeMask = IsNestedType | IsLocalType;
	final int IsAnonymousType = ASTNode.Bit6;
	final int AnonymousTypeMask = LocalTypeMask | IsAnonymousType;
	final int IsBinaryBinding = ASTNode.Bit7;
	
	// for the type hierarchy check used by ClassScope
	final int BeginHierarchyCheck = ASTNode.Bit9;
	final int EndHierarchyCheck = ASTNode.Bit10;

	// test bit to see if default abstract methods were computed
	final int KnowsDefaultAbstractMethods = ASTNode.Bit11;

	// Reusable bit currently used by Scopes
	final int InterfaceVisited = ASTNode.Bit12;

	// test bits to see if parts of binary types are faulted
	final int AreFieldsComplete = ASTNode.Bit13;
	final int AreMethodsComplete = ASTNode.Bit14;

	// test bit to avoid asking a type for a member type (includes inherited member types)
	final int HasNoMemberTypes = ASTNode.Bit15;

	// test bit to identify if the type's hierarchy is inconsistent
	final int HierarchyHasProblems = ASTNode.Bit16;

	// set for parameterized type NOT of the form X<?,?>
	final int IsBoundParameterizedType = ASTNode.Bit24; 

	// used by BinaryTypeBinding
	final int HasUnresolvedTypeVariables = ASTNode.Bit25;
	final int HasUnresolvedSuperclass = ASTNode.Bit26;
	final int HasUnresolvedSuperinterfaces = ASTNode.Bit27;
	final int HasUnresolvedEnclosingType = ASTNode.Bit28;
	final int HasUnresolvedMemberTypes = ASTNode.Bit29;

	final int HasTypeVariable = ASTNode.Bit30; // set either for type variables (direct) or parameterized types indirectly referencing type variables
	final int HasWildcard = ASTNode.Bit31; // set either for wildcards (direct) or parameterized types indirectly referencing wildcards
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10073.java