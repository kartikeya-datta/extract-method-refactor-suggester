error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/146.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/146.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/146.java
text:
```scala
a@@ssertEquals("size", 3, getTestVector().getNumNondefaultElements());

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.mahout.math;

import org.apache.mahout.math.function.Functions;
import org.junit.Test;


public class WeightedVectorTest extends AbstractVectorTest {
  @Test
  public void testLength() {
    Vector v = new DenseVector(new double[]{0.9921337470551008, 1.0031004325833064, 0.9963963182745947});
    Centroid c = new Centroid(3, new DenseVector(v), 2);
    assertEquals(c.getVector().getLengthSquared(), c.getLengthSquared(), 1.0e-17);
    // previously, this wouldn't clear the cached squared length value correctly which would cause bad distances
    c.set(0, -1);
    System.out.printf("c = %.9f\nv = %.9f\n", c.getLengthSquared(), c.getVector().getLengthSquared());
    assertEquals(c.getVector().getLengthSquared(), c.getLengthSquared(), 1.0e-17);
  }

  @Override
  public Vector vectorToTest(int size) {
    return new WeightedVector(new DenseVector(size), 4.52, 345);
  }

  @Test
  public void testOrdering() {
    WeightedVector v1 = new WeightedVector(new DenseVector(new double[]{1, 2, 3}), 5.41, 31);
    WeightedVector v2 = new WeightedVector(new DenseVector(new double[]{1, 2, 3}), 5.00, 31);
    WeightedVector v3 = new WeightedVector(new DenseVector(new double[]{1, 3, 3}), 5.00, 31);
    WeightedVector v4 = (WeightedVector) v1.clone();
    WeightedVectorComparator comparator = new WeightedVectorComparator();

    assertTrue(comparator.compare(v1, v2) > 0);
    assertTrue(comparator.compare(v3, v1) < 0);
    assertTrue(comparator.compare(v3, v2) > 0);
    assertEquals(0, comparator.compare(v4, v1));
    assertEquals(0, comparator.compare(v1, v1));
  }

  @Test
  public void testProjection() {
    Vector v1 = new DenseVector(10).assign(Functions.random());
    WeightedVector v2 = new WeightedVector(v1, v1, 31);
    assertEquals(v1.dot(v1), v2.getWeight(), 1.0e-13);
    assertEquals(31, v2.getIndex());

    Matrix y = new DenseMatrix(10, 4).assign(Functions.random());
    Matrix q = new QRDecomposition(y.viewPart(0, 10, 0, 3)).getQ();

    Vector nullSpace = y.viewColumn(3).minus(q.times(q.transpose().times(y.viewColumn(3))));

    WeightedVector v3 = new WeightedVector(q.viewColumn(0).plus(q.viewColumn(1)), nullSpace, 1);
    assertEquals(0, v3.getWeight(), 1.0e-13);

    Vector qx = q.viewColumn(0).plus(q.viewColumn(1)).normalize();
    WeightedVector v4 = new WeightedVector(qx, q.viewColumn(0), 2);
    assertEquals(Math.sqrt(0.5), v4.getWeight(), 1.0e-13);

    WeightedVector v5 = WeightedVector.project(q.viewColumn(0), qx);
    assertEquals(Math.sqrt(0.5), v5.getWeight(), 1.0e-13);
  }

  @Override
  public void testSize() {
    assertEquals("size", 7, getTestVector().getNumNondefaultElements());
  }

  @Override
  Vector generateTestVector(int cardinality) {
    return new WeightedVector(new DenseVector(cardinality), 3.14, 53);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/146.java