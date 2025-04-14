error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9174.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9174.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[15,1]

error in qdox parser
file content:
```java
offset: 560
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9174.java
text:
```scala
public class Test4d extends CoreTest {

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved.
 * This program and the accompanying materials are made available
 * under the terms of the Common Public License v1.0
 * which accompanies this distribution and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 *     PARC     initial implementation
 * ******************************************************************/

p@@ackage tests;

import figures.*;
import java.awt.Rectangle;

import junit.framework.*;

public class Test4d extends Test {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(Test4d.class);
    }

    public void testBasicEquality() {
        assertTrue(g.getBounds() == g.getBounds());
    }

    public void testNonGroupMove() {
        p1.move(3, 27);
    }

    public void testEqualityAfterAddition() {
        Rectangle r = g.getBounds();
        g.add(new Point(37, 90));
        assertTrue(g.getBounds() == r);
    }

    public void testEqualityAfterMove() {
        Point p0 = new Point(1, 2);
        Point p1 = new Point(2, 3);

        Group g0 = new Group(p0);
        Group g1 = new Group(p1);

        Rectangle r0 = g0.getBounds();
        Rectangle r1 = g1.getBounds();
        assertTrue(r0 != r1);
        assertTrue(g0.getBounds() == r0);
        assertTrue(g1.getBounds() != r0);
        assertTrue(g0.getBounds() != r1);
        assertTrue(g1.getBounds() == r1);

        p0.move(3, 1);
        Rectangle r00 = g0.getBounds();
        Rectangle r10 = g1.getBounds();

        assertTrue(r10 != r00);
        assertTrue(r0 != r00);
        assertTrue(g0.getBounds() == r00);
    }

    public void testEqualityAfterMove0() {
        g = new Group(p1);
        Rectangle r0 = g.getBounds();
        assertTrue(g.getBounds() == r0);
        p1.move(3, 1);
        Rectangle r1 = g.getBounds();
        assertTrue(r0 != r1);
        assertTrue(r1 == g.getBounds());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9174.java