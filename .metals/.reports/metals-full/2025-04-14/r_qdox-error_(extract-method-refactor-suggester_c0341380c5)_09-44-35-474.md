error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11356.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11356.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11356.java
text:
```scala
R@@ungeKuttaStepInterpolator rki = (RungeKuttaStepInterpolator) prototype.copy();

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

package org.apache.commons.math.ode;

/**
 * This class implements the common part of all Runge-Kutta-Fehlberg
 * integrators for Ordinary Differential Equations.

 * <p>These methods are embedded explicit Runge-Kutta methods with two
 * sets of coefficients allowing to estimate the error, their Butcher
 * arrays are as follows :
 * <pre>
 *    0  |
 *   c2  | a21
 *   c3  | a31  a32
 *   ... |        ...
 *   cs  | as1  as2  ...  ass-1
 *       |--------------------------
 *       |  b1   b2  ...   bs-1  bs
 *       |  b'1  b'2 ...   b's-1 b's
 * </pre>
 * </p>

 * <p>In fact, we rather use the array defined by ej = bj - b'j to
 * compute directly the error rather than computing two estimates and
 * then comparing them.</p>

 * <p>Some methods are qualified as <i>fsal</i> (first same as last)
 * methods. This means the last evaluation of the derivatives in one
 * step is the same as the first in the next step. Then, this
 * evaluation can be reused from one step to the next one and the cost
 * of such a method is really s-1 evaluations despite the method still
 * has s stages. This behaviour is true only for successful steps, if
 * the step is rejected after the error estimation phase, no
 * evaluation is saved. For an <i>fsal</i> method, we have cs = 1 and
 * asi = bi for all i.</p>

 * @version $Id: RungeKuttaFehlbergIntegrator.java 1705 2006-09-17 19:57:39Z luc $

 */

public abstract class RungeKuttaFehlbergIntegrator
  extends AdaptiveStepsizeIntegrator {

  /** Build a Runge-Kutta integrator with the given Butcher array.
   * @param fsal indicate that the method is an <i>fsal</i>
   * @param c time steps from Butcher array (without the first zero)
   * @param a internal weights from Butcher array (without the first empty row)
   * @param b external weights for the high order method from Butcher array
   * @param prototype prototype of the step interpolator to use
   * @param minStep minimal step (must be positive even for backward
   * integration), the last step can be smaller than this
   * @param maxStep maximal step (must be positive even for backward
   * integration)
   * @param scalAbsoluteTolerance allowed absolute error
   * @param scalRelativeTolerance allowed relative error
   */
  protected RungeKuttaFehlbergIntegrator(boolean fsal,
                                         double[] c, double[][] a, double[] b,
                                         RungeKuttaStepInterpolator prototype,
                                         double minStep, double maxStep,
                                         double scalAbsoluteTolerance,
                                         double scalRelativeTolerance) {

    super(minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);

    this.fsal      = fsal;
    this.c         = c;
    this.a         = a;
    this.b         = b;
    this.prototype = prototype;

    exp = -1.0 / getOrder();

    this.safety = 0.9;

    // set the default values of the algorithm control parameters
    setMinReduction(0.2);
    setMaxGrowth(10.0);

  }

  /** Build a Runge-Kutta integrator with the given Butcher array.
   * @param fsal indicate that the method is an <i>fsal</i>
   * @param c time steps from Butcher array (without the first zero)
   * @param a internal weights from Butcher array (without the first empty row)
   * @param b external weights for the high order method from Butcher array
   * @param prototype prototype of the step interpolator to use
   * @param minStep minimal step (must be positive even for backward
   * integration), the last step can be smaller than this
   * @param maxStep maximal step (must be positive even for backward
   * integration)
   * @param vecAbsoluteTolerance allowed absolute error
   * @param vecRelativeTolerance allowed relative error
   */
  protected RungeKuttaFehlbergIntegrator(boolean fsal,
                                         double[] c, double[][] a, double[] b,
                                         RungeKuttaStepInterpolator prototype,
                                         double   minStep, double maxStep,
                                         double[] vecAbsoluteTolerance,
                                         double[] vecRelativeTolerance) {

    super(minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);

    this.fsal      = fsal;
    this.c         = c;
    this.a         = a;
    this.b         = b;
    this.prototype = prototype;

    exp = -1.0 / getOrder();

    this.safety = 0.9;

    // set the default values of the algorithm control parameters
    setMinReduction(0.2);
    setMaxGrowth(10.0);

  }

  /** Get the name of the method.
   * @return name of the method
   */
  public abstract String getName();

  /** Get the order of the method.
   * @return order of the method
   */
  public abstract int getOrder();

  /** Get the safety factor for stepsize control.
   * @return safety factor
   */
  public double getSafety() {
    return safety;
  }

  /** Set the safety factor for stepsize control.
   * @param safety safety factor
   */
  public void setSafety(double safety) {
    this.safety = safety;
  }

  public void integrate(FirstOrderDifferentialEquations equations,
                        double t0, double[] y0,
                        double t, double[] y)
  throws DerivativeException, IntegratorException {

    sanityChecks(equations, t0, y0, t, y);
    boolean forward = (t > t0);

    // create some internal working arrays
    int stages = c.length + 1;
    if (y != y0) {
      System.arraycopy(y0, 0, y, 0, y0.length);
    }
    double[][] yDotK = new double[stages][];
    for (int i = 0; i < stages; ++i) {
      yDotK [i] = new double[y0.length];
    }
    double[] yTmp = new double[y0.length];

    // set up an interpolator sharing the integrator arrays
    AbstractStepInterpolator interpolator;
    if (handler.requiresDenseOutput() || (! switchesHandler.isEmpty())) {
      RungeKuttaStepInterpolator rki = (RungeKuttaStepInterpolator) prototype.clone();
      rki.reinitialize(equations, yTmp, yDotK, forward);
      interpolator = rki;
    } else {
      interpolator = new DummyStepInterpolator(yTmp, forward);
    }
    interpolator.storeTime(t0);

    stepStart  = t0;
    double  hNew      = 0;
    boolean firstTime = true;
    boolean lastStep;
    handler.reset();
    do {

      interpolator.shift();

      double error = 0;
      for (boolean loop = true; loop;) {

        if (firstTime || !fsal) {
          // first stage
          equations.computeDerivatives(stepStart, y, yDotK[0]);
        }

        if (firstTime) {
          double[] scale;
          if (vecAbsoluteTolerance != null) {
            scale = vecAbsoluteTolerance;
          } else {
            scale = new double[y0.length];
            for (int i = 0; i < scale.length; ++i) {
              scale[i] = scalAbsoluteTolerance;
            }
          }
          hNew = initializeStep(equations, forward, getOrder(), scale,
                                stepStart, y, yDotK[0], yTmp, yDotK[1]);
          firstTime = false;
        }

        stepSize = hNew;

        // step adjustment near bounds
        if ((forward && (stepStart + stepSize > t))
 ((! forward) && (stepStart + stepSize < t))) {
          stepSize = t - stepStart;
        }

        // next stages
        for (int k = 1; k < stages; ++k) {

          for (int j = 0; j < y0.length; ++j) {
            double sum = a[k-1][0] * yDotK[0][j];
            for (int l = 1; l < k; ++l) {
              sum += a[k-1][l] * yDotK[l][j];
            }
            yTmp[j] = y[j] + stepSize * sum;
          }

          equations.computeDerivatives(stepStart + c[k-1] * stepSize, yTmp, yDotK[k]);

        }

        // estimate the state at the end of the step
        for (int j = 0; j < y0.length; ++j) {
          double sum    = b[0] * yDotK[0][j];
          for (int l = 1; l < stages; ++l) {
            sum    += b[l] * yDotK[l][j];
          }
          yTmp[j] = y[j] + stepSize * sum;
        }

        // estimate the error at the end of the step
        error = estimateError(yDotK, y, yTmp, stepSize);
        if (error <= 1.0) {

          // Switching functions handling
          interpolator.storeTime(stepStart + stepSize);
          if (switchesHandler.evaluateStep(interpolator)) {
            // reject the step to match exactly the next switch time
            hNew = switchesHandler.getEventTime() - stepStart;
          } else {
            // accept the step
            loop = false;
          }

        } else {
          // reject the step and attempt to reduce error by stepsize control
          double factor = Math.min(maxGrowth,
                                   Math.max(minReduction,
                                            safety * Math.pow(error, exp)));
          hNew = filterStep(stepSize * factor, false);
        }

      }

      // the step has been accepted
      stepStart += stepSize;
      System.arraycopy(yTmp, 0, y, 0, y0.length);
      switchesHandler.stepAccepted(stepStart, y);
      if (switchesHandler.stop()) {
        lastStep = true;
      } else {
        lastStep = forward ? (stepStart >= t) : (stepStart <= t);
      }

      // provide the step data to the step handler
      interpolator.storeTime(stepStart);
      handler.handleStep(interpolator, lastStep);

      if (fsal) {
        // save the last evaluation for the next step
        System.arraycopy(yDotK[stages - 1], 0, yDotK[0], 0, y0.length);
      }

      if (switchesHandler.reset(stepStart, y) && ! lastStep) {
        // some switching function has triggered changes that
        // invalidate the derivatives, we need to recompute them
        equations.computeDerivatives(stepStart, y, yDotK[0]);
      }

      if (! lastStep) {
        // stepsize control for next step
        double  factor     = Math.min(maxGrowth,
                                      Math.max(minReduction,
                                               safety * Math.pow(error, exp)));
        double  scaledH    = stepSize * factor;
        double  nextT      = stepStart + scaledH;
        boolean nextIsLast = forward ? (nextT >= t) : (nextT <= t);
        hNew = filterStep(scaledH, nextIsLast);
      }

    } while (! lastStep);

    resetInternalState();

  }

  /** Get the minimal reduction factor for stepsize control.
   * @return minimal reduction factor
   */
  public double getMinReduction() {
    return minReduction;
  }

  /** Set the minimal reduction factor for stepsize control.
   * @param minReduction minimal reduction factor
   */
  public void setMinReduction(double minReduction) {
    this.minReduction = minReduction;
  }

  /** Get the maximal growth factor for stepsize control.
   * @return maximal growth factor
   */
  public double getMaxGrowth() {
    return maxGrowth;
  }

  /** Set the maximal growth factor for stepsize control.
   * @param maxGrowth maximal growth factor
   */
  public void setMaxGrowth(double maxGrowth) {
    this.maxGrowth = maxGrowth;
  }

  /** Compute the error ratio.
   * @param yDotK derivatives computed during the first stages
   * @param y0 estimate of the step at the start of the step
   * @param y1 estimate of the step at the end of the step
   * @param h  current step
   * @return error ratio, greater than 1 if step should be rejected
   */
  protected abstract double estimateError(double[][] yDotK,
                                          double[] y0, double[] y1,
                                          double h);

  /** Indicator for <i>fsal</i> methods. */
  private boolean fsal;

  /** Time steps from Butcher array (without the first zero). */
  private double[] c;

  /** Internal weights from Butcher array (without the first empty row). */
  private double[][] a;

  /** External weights for the high order method from Butcher array. */
  private double[] b;

  /** Prototype of the step interpolator. */
  private RungeKuttaStepInterpolator prototype;
                                         
  /** Stepsize control exponent. */
  private double exp;

  /** Safety factor for stepsize control. */
  private double safety;

  /** Minimal reduction factor for stepsize control. */
  private double minReduction;

  /** Maximal growth factor for stepsize control. */
  private double maxGrowth;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11356.java