error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10972.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10972.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10972.java
text:
```scala
r@@eturn (Object[][]) contents.clone();

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

package org.spaceroots.mantissa;

import java.util.ListResourceBundle;

/** This class gather the message resources for the mantissa library.
 * @version $Id: MessagesResources.java 1705 2006-09-17 19:57:39Z luc $
 * @author L. Maisonobe
 */

public class MessagesResources
  extends ListResourceBundle {

  /** Simple constructor.
   */
  public MessagesResources() {
  }

  public Object[][] getContents() {
    return contents;
  }

  static final Object[][] contents = {

    // org.spaceroots.mantissa.estimation.GaussNewtonEstimator
    { "unable to converge in {0} iterations",
      "unable to converge in {0} iterations" },

    // org.spaceroots.mantissa.estimation.LevenbergMarquardtEstimator
    { "cost relative tolerance is too small ({0}), no further reduction in the sum of squares is possible",
      "cost relative tolerance is too small ({0}), no further reduction in the sum of squares is possible" },
    { "parameters relative tolerance is too small ({0}), no further improvement in the approximate solution is possible",
      "parameters relative tolerance is too small ({0}), no further improvement in the approximate solution is possible" },
    { "orthogonality tolerance is too small ({0}), solution is orthogonal to the jacobian",
      "orthogonality tolerance is too small ({0}), solution is orthogonal to the jacobian" },
    { "maximal number of evaluations exceeded ({0})",
      "maximal number of evaluations exceeded ({0})" },

    // org.spaceroots.mantissa.fitting.HarmonicCoefficientsGuesser
    { "unable to guess a first estimate",
      "unable to guess a first estimate" },

    // org.spaceroots.mantissa.fitting.HarmonicFitter
    { "sample must contain at least {0} points",
      "sample must contain at least {0} points" },

    // org.spaceroots.mantissa.functions.ExhaustedSampleException
    { "sample contains only {0} elements",
      "sample contains only {0} elements" },

    // org.spaceroots.mantissa.geometry.CardanEulerSingularityException
    { "Cardan angles singularity",
      "Cardan angles singularity" },
    { "Euler angles singularity",
      "Euler angles singularity" },

    // org.spaceroots.mantissa.geometry.Rotation
    { "a {0}x{1} matrix cannot be a rotation matrix",
      "a {0}x{1} matrix cannot be a rotation matrix" },
    { "the closest orthogonal matrix has a negative determinant {0}",
      "the closest orthogonal matrix has a negative determinant {0}" },
    { "unable to orthogonalize matrix in {0} iterations",
      "unable to orthogonalize matrix in {0} iterations" },

    // org.spaceroots.mantissa.linalg;.SingularMatrixException
    { "singular matrix",
      "singular matrix" },

    // org.spaceroots.mantissa.ode.AdaptiveStepsizeIntegrator
    { "minimal step size ({0}) reached, integration needs {1}",
      "minimal step size ({0}) reached, integration needs {1}" },

    // org.spaceroots.mantissa.ode.GraggBulirschStoerIntegrator,
    // org.spaceroots.mantissa.ode.RungeKuttaFehlbergIntegrator,
    // org.spaceroots.mantissa.ode.RungeKuttaIntegrator
    { "dimensions mismatch: ODE problem has dimension {0},"
    + " state vector has dimension {1}",
      "dimensions mismatch: ODE problem has dimension {0},"
    + " state vector has dimension {1}" },
    { "too small integration interval: length = {0}",
      "too small integration interval: length = {0}" },

    // org.spaceroots.mantissa.optimization.DirectSearchOptimizer
    { "none of the {0} start points lead to convergence",
      "none of the {0} start points lead to convergence"  },

    // org.spaceroots.mantissa.random.CorrelatedRandomVectorGenerator
    { "dimension mismatch {0} != {1}",
      "dimension mismatch {0} != {1}" },

    // org.spaceroots.mantissa.random.NotPositiveDefiniteMatrixException
    { "not positive definite matrix",
      "not positive definite matrix" }

  };
  
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10972.java