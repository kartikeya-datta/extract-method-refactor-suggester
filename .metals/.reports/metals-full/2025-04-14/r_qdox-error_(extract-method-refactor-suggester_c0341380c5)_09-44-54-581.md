error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10977.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10977.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10977.java
text:
```scala
private static final S@@tring methodName = "Dormand-Prince 5(4)";

// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
// 
//   http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.spaceroots.mantissa.ode;

/**
 * This class implements the 5(4) Dormand-Prince integrator for Ordinary
 * Differential Equations.

 * <p>This integrator is an embedded Runge-Kutta-Fehlberg integrator
 * of order 5(4) used in local extrapolation mode (i.e. the solution
 * is computed using the high order formula) with stepsize control
 * (and automatic step initialization) and continuous output. This
 * method uses 7 functions evaluations per step. However, since this
 * is an <i>fsal</i>, the last evaluation of one step is the same as
 * the first evaluation of the next step and hence can be avoided. So
 * the cost is really 6 functions evaluations per step.</p>

 * <p>This method has been published (whithout the continuous output
 * that was added by Shampine in 1986) in the following article :
 * <pre>
 *  A family of embedded Runge-Kutta formulae
 *  J. R. Dormand and P. J. Prince
 *  Journal of Computational and Applied Mathematics
 *  volume 6, no 1, 1980, pp. 19-26
 * </pre></p>

 * @version $Id: DormandPrince54Integrator.java 1705 2006-09-17 19:57:39Z luc $
 * @author L. Maisonobe

 */

public class DormandPrince54Integrator
  extends RungeKuttaFehlbergIntegrator {

  private static final String methodName = new String("Dormand-Prince 5(4)");

  private static final double[] c = {
    1.0/5.0, 3.0/10.0, 4.0/5.0, 8.0/9.0, 1.0, 1.0
  };

  private static final double[][] a = {
    {1.0/5.0},
    {3.0/40.0, 9.0/40.0},
    {44.0/45.0, -56.0/15.0, 32.0/9.0},
    {19372.0/6561.0, -25360.0/2187.0, 64448.0/6561.0,  -212.0/729.0},
    {9017.0/3168.0, -355.0/33.0, 46732.0/5247.0, 49.0/176.0, -5103.0/18656.0},
    {35.0/384.0, 0.0, 500.0/1113.0, 125.0/192.0, -2187.0/6784.0, 11.0/84.0}
  };

  private static final double[] b = {
    35.0/384.0, 0.0, 500.0/1113.0, 125.0/192.0, -2187.0/6784.0, 11.0/84.0, 0.0
  };

  private static final double e1 =     71.0 / 57600.0;
  private static final double e3 =    -71.0 / 16695.0;
  private static final double e4 =     71.0 / 1920.0;
  private static final double e5 = -17253.0 / 339200.0;
  private static final double e6 =     22.0 / 525.0;
  private static final double e7 =     -1.0 / 40.0;

  /** Simple constructor.
   * Build a fifth order Dormand-Prince integrator with the given step bounds
   * @param minStep minimal step (must be positive even for backward
   * integration), the last step can be smaller than this
   * @param maxStep maximal step (must be positive even for backward
   * integration)
   * @param scalAbsoluteTolerance allowed absolute error
   * @param scalRelativeTolerance allowed relative error
   */
  public DormandPrince54Integrator(double minStep, double maxStep,
                                   double scalAbsoluteTolerance,
                                   double scalRelativeTolerance) {
    super(true, c, a, b, new DormandPrince54StepInterpolator(),
          minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
  }

  /** Simple constructor.
   * Build a fifth order Dormand-Prince integrator with the given step bounds
   * @param minStep minimal step (must be positive even for backward
   * integration), the last step can be smaller than this
   * @param maxStep maximal step (must be positive even for backward
   * integration)
   * @param vecAbsoluteTolerance allowed absolute error
   * @param vecRelativeTolerance allowed relative error
   */
  public DormandPrince54Integrator(double minStep, double maxStep,
                                   double[] vecAbsoluteTolerance,
                                   double[] vecRelativeTolerance) {
    super(true, c, a, b, new DormandPrince54StepInterpolator(),
          minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
  }

  /** Get the name of the method.
   * @return name of the method
   */
  public String getName() {
    return methodName;
  }

  /** Get the order of the method.
   * @return order of the method
   */
  public int getOrder() {
    return 5;
  }

  /** Compute the error ratio.
   * @param yDotK derivatives computed during the first stages
   * @param y0 estimate of the step at the start of the step
   * @param y1 estimate of the step at the end of the step
   * @param h  current step
   * @return error ratio, greater than 1 if step should be rejected
   */
  protected double estimateError(double[][] yDotK,
                                 double[] y0, double[] y1,
                                 double h) {

    double error = 0;

    for (int j = 0; j < y0.length; ++j) {
      double errSum = e1 * yDotK[0][j] +  e3 * yDotK[2][j]
                    + e4 * yDotK[3][j] +  e5 * yDotK[4][j]
                    + e6 * yDotK[5][j] +  e7 * yDotK[6][j];

      double yScale = Math.max(Math.abs(y0[j]), Math.abs(y1[j]));
      double tol = (vecAbsoluteTolerance == null)
        ? (scalAbsoluteTolerance + scalRelativeTolerance * yScale)
        : (vecAbsoluteTolerance[j] + vecRelativeTolerance[j] * yScale);
      double ratio  = h * errSum / tol;
      error += ratio * ratio;

    }

    return Math.sqrt(error / y0.length);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10977.java