error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14569.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14569.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14569.java
text:
```scala
y@@ = problem.y.clone();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.math.ode.nonstiff;

/**
 * This class is used in the junit tests for the ODE integrators.

 * <p>This specific problem is the following differential equation :
 * <pre>
 *    y1'' = -y1/r^3  y1 (0) = 1-e  y1' (0) = 0
 *    y2'' = -y2/r^3  y2 (0) = 0    y2' (0) =sqrt((1+e)/(1-e))
 *    r = sqrt (y1^2 + y2^2), e = 0.9
 * </pre>
 * This is a two-body problem in the plane which can be solved by
 * Kepler's equation
 * <pre>
 *   y1 (t) = ...
 * </pre>
 * </p>

 */
public class TestProblem3
  extends TestProblemAbstract {

  /** Serializable version identifier. */
  private static final long serialVersionUID = 8567328542728919999L;

  /** Eccentricity */
  double e;

  /** theoretical state */
  private double[] y;

  /**
   * Simple constructor.
   * @param e eccentricity
   */
  public TestProblem3(double e) {
    super();
    this.e = e;
    double[] y0 = { 1 - e, 0, 0, Math.sqrt((1+e)/(1-e)) };
    setInitialConditions(0.0, y0);
    setFinalConditions(20.0);
    double[] errorScale = { 1.0, 1.0, 1.0, 1.0 };
    setErrorScale(errorScale);
    y = new double[y0.length];
  }
 
  /**
   * Simple constructor.
   */
  public TestProblem3() {
    this(0.1);
  }
 
  /**
   * Copy constructor.
   * @param problem problem to copy
   */
  public TestProblem3(TestProblem3 problem) {
    super(problem);
    e = problem.e;
    y = (double[]) problem.y.clone();
  }

  /**
   * Clone operation.
   * @return a copy of the instance
   */
  @Override
  public Object clone() {
    return new TestProblem3(this);
  }

  @Override
  public void doComputeDerivatives(double t, double[] y, double[] yDot) {

    // current radius
    double r2 = y[0] * y[0] + y[1] * y[1];
    double invR3 = 1 / (r2 * Math.sqrt(r2));

    // compute the derivatives
    yDot[0] = y[2];
    yDot[1] = y[3];
    yDot[2] = -invR3  * y[0];
    yDot[3] = -invR3  * y[1];

  }

  @Override
  public double[] computeTheoreticalState(double t) {

    // solve Kepler's equation
    double E = t;
    double d = 0;
    double corr = 0;
    do {
      double f2  = e * Math.sin(E);
      double f0  = d - f2;
      double f1  = 1 - e * Math.cos(E);
      double f12 = f1 + f1;
      corr  = f0 * f12 / (f1 * f12 - f0 * f2);
      d -= corr;
      E = t + d;
    } while (Math.abs(corr) > 1.0e-12);

    double cosE = Math.cos(E);
    double sinE = Math.sin(E);

    y[0] = cosE - e;
    y[1] = Math.sqrt(1 - e * e) * sinE;
    y[2] = -sinE / (1 - e * cosE);
    y[3] = Math.sqrt(1 - e * e) * cosE / (1 - e * cosE);

    return y;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14569.java