error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2103.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2103.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2103.java
text:
```scala
private static final l@@ong serialVersionUID = 20111120L;

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

import org.apache.commons.math.ode.AbstractIntegrator;
import org.apache.commons.math.ode.EquationsMapper;
import org.apache.commons.math.ode.sampling.StepInterpolator;

/**
 * This class represents an interpolator over the last step during an
 * ODE integration for the 5(4) Dormand-Prince integrator.
 *
 * @see DormandPrince54Integrator
 *
 * @version $Id$
 * @since 1.2
 */

class DormandPrince54StepInterpolator
  extends RungeKuttaStepInterpolator {

    /** Last row of the Butcher-array internal weights, element 0. */
    private static final double A70 =    35.0 /  384.0;

    // element 1 is zero, so it is neither stored nor used

    /** Last row of the Butcher-array internal weights, element 2. */
    private static final double A72 =   500.0 / 1113.0;

    /** Last row of the Butcher-array internal weights, element 3. */
    private static final double A73 =   125.0 /  192.0;

    /** Last row of the Butcher-array internal weights, element 4. */
    private static final double A74 = -2187.0 / 6784.0;

    /** Last row of the Butcher-array internal weights, element 5. */
    private static final double A75 =    11.0 /   84.0;

    /** Shampine (1986) Dense output, element 0. */
    private static final double D0 =  -12715105075.0 /  11282082432.0;

    // element 1 is zero, so it is neither stored nor used

    /** Shampine (1986) Dense output, element 2. */
    private static final double D2 =   87487479700.0 /  32700410799.0;

    /** Shampine (1986) Dense output, element 3. */
    private static final double D3 =  -10690763975.0 /   1880347072.0;

    /** Shampine (1986) Dense output, element 4. */
    private static final double D4 =  701980252875.0 / 199316789632.0;

    /** Shampine (1986) Dense output, element 5. */
    private static final double D5 =   -1453857185.0 /    822651844.0;

    /** Shampine (1986) Dense output, element 6. */
    private static final double D6 =      69997945.0 /     29380423.0;

    /** Serializable version identifier. */
    private static final long serialVersionUID = 20110928L;

    /** First vector for interpolation. */
    private double[] v1;

    /** Second vector for interpolation. */
    private double[] v2;

    /** Third vector for interpolation. */
    private double[] v3;

    /** Fourth vector for interpolation. */
    private double[] v4;

    /** Initialization indicator for the interpolation vectors. */
    private boolean vectorsInitialized;

  /** Simple constructor.
   * This constructor builds an instance that is not usable yet, the
   * {@link #reinitialize} method should be called before using the
   * instance in order to initialize the internal arrays. This
   * constructor is used only in order to delay the initialization in
   * some cases. The {@link EmbeddedRungeKuttaIntegrator} uses the
   * prototyping design pattern to create the step interpolators by
   * cloning an uninitialized model and latter initializing the copy.
   */
  public DormandPrince54StepInterpolator() {
    super();
    v1 = null;
    v2 = null;
    v3 = null;
    v4 = null;
    vectorsInitialized = false;
  }

  /** Copy constructor.
   * @param interpolator interpolator to copy from. The copy is a deep
   * copy: its arrays are separated from the original arrays of the
   * instance
   */
  public DormandPrince54StepInterpolator(final DormandPrince54StepInterpolator interpolator) {

    super(interpolator);

    if (interpolator.v1 == null) {

      v1 = null;
      v2 = null;
      v3 = null;
      v4 = null;
      vectorsInitialized = false;

    } else {

      v1 = interpolator.v1.clone();
      v2 = interpolator.v2.clone();
      v3 = interpolator.v3.clone();
      v4 = interpolator.v4.clone();
      vectorsInitialized = interpolator.vectorsInitialized;

    }

  }

  /** {@inheritDoc} */
  @Override
  protected StepInterpolator doCopy() {
    return new DormandPrince54StepInterpolator(this);
  }


  /** {@inheritDoc} */
  @Override
  public void reinitialize(final AbstractIntegrator integrator,
                           final double[] y, final double[][] yDotK, final boolean forward,
                           final EquationsMapper primaryMapper,
                           final EquationsMapper[] secondaryMappers) {
    super.reinitialize(integrator, y, yDotK, forward, primaryMapper, secondaryMappers);
    v1 = null;
    v2 = null;
    v3 = null;
    v4 = null;
    vectorsInitialized = false;
  }

  /** {@inheritDoc} */
  @Override
  public void storeTime(final double t) {
    super.storeTime(t);
    vectorsInitialized = false;
  }

  /** {@inheritDoc} */
  @Override
  protected void computeInterpolatedStateAndDerivatives(final double theta,
                                          final double oneMinusThetaH) {

    if (! vectorsInitialized) {

      if (v1 == null) {
        v1 = new double[interpolatedState.length];
        v2 = new double[interpolatedState.length];
        v3 = new double[interpolatedState.length];
        v4 = new double[interpolatedState.length];
      }

      // no step finalization is needed for this interpolator

      // we need to compute the interpolation vectors for this time step
      for (int i = 0; i < interpolatedState.length; ++i) {
          final double yDot0 = yDotK[0][i];
          final double yDot2 = yDotK[2][i];
          final double yDot3 = yDotK[3][i];
          final double yDot4 = yDotK[4][i];
          final double yDot5 = yDotK[5][i];
          final double yDot6 = yDotK[6][i];
          v1[i] = A70 * yDot0 + A72 * yDot2 + A73 * yDot3 + A74 * yDot4 + A75 * yDot5;
          v2[i] = yDot0 - v1[i];
          v3[i] = v1[i] - v2[i] - yDot6;
          v4[i] = D0 * yDot0 + D2 * yDot2 + D3 * yDot3 + D4 * yDot4 + D5 * yDot5 + D6 * yDot6;
      }

      vectorsInitialized = true;

    }

    // interpolate
    final double eta = 1 - theta;
    final double twoTheta = 2 * theta;
    final double dot2 = 1 - twoTheta;
    final double dot3 = theta * (2 - 3 * theta);
    final double dot4 = twoTheta * (1 + theta * (twoTheta - 3));
    for (int i = 0; i < interpolatedState.length; ++i) {
      interpolatedState[i] =
          currentState[i] - oneMinusThetaH * (v1[i] - theta * (v2[i] + theta * (v3[i] + eta * v4[i])));
      interpolatedDerivatives[i] = v1[i] + dot2 * v2[i] + dot3 * v3[i] + dot4 * v4[i];
    }

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2103.java