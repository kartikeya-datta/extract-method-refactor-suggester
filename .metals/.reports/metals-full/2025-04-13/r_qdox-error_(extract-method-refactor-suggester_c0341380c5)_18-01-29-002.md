error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15326.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15326.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15326.java
text:
```scala
public O@@bject visit(TypeVariablePattern node, Object data) {

/*******************************************************************************
 * Copyright (c) 2005 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *   Alexandre Vasseur         initial implementation
 *******************************************************************************/
package org.aspectj.weaver.patterns;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class IdentityPointcutVisitor implements PatternNodeVisitor {

    public Object visit(AnyTypePattern node, Object data) {
        return node;
    }

    public Object visit(NoTypePattern node, Object data) {
        return node;
    }

    public Object visit(EllipsisTypePattern node, Object data) {
        return node;
    }

    public Object visit(AnyWithAnnotationTypePattern node, Object data) {
        return node;
    }

    public Object visit(AnyAnnotationTypePattern node, Object data) {
        return node;
    }

    public Object visit(EllipsisAnnotationTypePattern node, Object data) {
        return node;
    }

    public Object visit(AndAnnotationTypePattern node, Object data) {
        return node;
    }

    public Object visit(AndPointcut node, Object data) {
        return node;
    }

    public Object visit(AndTypePattern node, Object data) {
        return node;
    }

    public Object visit(AnnotationPatternList node, Object data) {
        return node;
    }

    public Object visit(AnnotationPointcut node, Object data) {
        return node;
    }

    public Object visit(ArgsAnnotationPointcut node, Object data) {
        return node;
    }

    public Object visit(ArgsPointcut node, Object data) {
        return node;
    }

    public Object visit(BindingAnnotationTypePattern node, Object data) {
        return node;
    }

    public Object visit(BindingTypePattern node, Object data) {
        return node;
    }

    public Object visit(CflowPointcut node, Object data) {
        return node;
    }

    public Object visit(ConcreteCflowPointcut node, Object data) {
        return node;
    }

    public Object visit(DeclareAnnotation node, Object data) {
        return node;
    }

    public Object visit(DeclareErrorOrWarning node, Object data) {
        return node;
    }

    public Object visit(DeclareParents node, Object data) {
        return node;
    }

    public Object visit(DeclarePrecedence node, Object data) {
        return node;
    }

    public Object visit(DeclareSoft node, Object data) {
        return node;
    }

    public Object visit(ExactAnnotationTypePattern node, Object data) {
        return node;
    }

    public Object visit(ExactTypePattern node, Object data) {
        return node;
    }

    public Object visit(HandlerPointcut node, Object data) {
        return node;
    }

    public Object visit(IfPointcut node, Object data) {
        return node;
    }

    public Object visit(KindedPointcut node, Object data) {
        return node;
    }

    public Object visit(ModifiersPattern node, Object data) {
        return node;
    }

    public Object visit(NamePattern node, Object data) {
        return node;
    }

    public Object visit(NotAnnotationTypePattern node, Object data) {
        return node;
    }

    public Object visit(NotPointcut node, Object data) {
        return node;
    }

    public Object visit(NotTypePattern node, Object data) {
        return node;
    }

    public Object visit(OrAnnotationTypePattern node, Object data) {
        return node;
    }

    public Object visit(OrPointcut node, Object data) {
        return node;
    }

    public Object visit(OrTypePattern node, Object data) {
        return node;
    }

    public Object visit(PerCflow node, Object data) {
        return node;
    }

    public Object visit(PerFromSuper node, Object data) {
        return node;
    }

    public Object visit(PerObject node, Object data) {
        return node;
    }

    public Object visit(PerSingleton node, Object data) {
        return node;
    }

    public Object visit(PerTypeWithin node, Object data) {
        return node;
    }

    public Object visit(PatternNode node, Object data) {
        throw new ParserException("Should implement for " + node.getClass(), null);
    }

    public Object visit(ReferencePointcut node, Object data) {
        return node;
    }

    public Object visit(SignaturePattern node, Object data) {
        return node;
    }

    public Object visit(ThisOrTargetAnnotationPointcut node, Object data) {
        return node;
    }

    public Object visit(ThisOrTargetPointcut node, Object data) {
        return node;
    }

    public Object visit(ThrowsPattern node, Object data) {
        return node;
    }

    public Object visit(TypePatternList node, Object data) {
        return node;
    }

    public Object visit(WildAnnotationTypePattern node, Object data) {
        return node;
    }

    public Object visit(WildTypePattern node, Object data) {
        return node;
    }

    public Object visit(WithinAnnotationPointcut node, Object data) {
        return node;
    }

    public Object visit(WithinCodeAnnotationPointcut node, Object data) {
        return node;
    }

    public Object visit(WithinPointcut node, Object data) {
        return node;
    }

    public Object visit(WithincodePointcut node, Object data) {
        return node;
    }

    public Object visit(Pointcut.MatchesNothingPointcut node, Object data) {
        return node;
    }

	public Object visit(TypeVariable node, Object data) {
		return node;
	}

	public Object visit(TypeVariablePatternList node, Object data) {
		return node;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15326.java