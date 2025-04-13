error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10973.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10973.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10973.java
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
 * @version $Id: MessagesResources_fr.java 1705 2006-09-17 19:57:39Z luc $
 * @author L. Maisonobe
 */
public class MessagesResources_fr
  extends ListResourceBundle {

  /** Simple constructor.
   */
  public MessagesResources_fr() {
  }

  public Object[][] getContents() {
    return contents;
  }

  static final Object[][] contents = {

    // org.spaceroots.mantissa.estimation.GaussNewtonEstimator
    { "unable to converge in {0} iterations",
      "pas de convergence apr\u00e8s {0} it\u00e9rations" },

    // org.spaceroots.mantissa.estimation.LevenbergMarquardtEstimator
    { "cost relative tolerance is too small ({0}), no further reduction in the sum of squares is possible",
      "trop petite tol\u00e9rance relative sur le co\u00fbt ({0}), aucune r\u00e9duction de la somme des carr\u00e9s n''est possible" },
    { "parameters relative tolerance is too small ({0}), no further improvement in the approximate solution is possible",
      "trop petite tol\u00e9rance relative sur les param\u00e8tres ({0}), aucune am\u00e9lioration de la solution approximative n''est possible" },
    { "orthogonality tolerance is too small ({0}), solution is orthogonal to the jacobian",
      "trop petite tol\u00e9rance sur l''orthogonalit\u00e9 ({0}), la solution est orthogonale \u00e0 la jacobienne" },
    { "maximal number of evaluations exceeded ({0})",
      "nombre maximal d''\u00e9valuations d\u00e9pass\u00e9 ({0})" },

    // org.spaceroots.mantissa.fitting.HarmonicCoefficientsGuesser
    { "unable to guess a first estimate",
      "impossible de trouver une premi\u00e8re estim\u00e9e" },

    // org.spaceroots.mantissa.fitting.HarmonicFitter
    { "sample must contain at least {0} points",
      "l''\u00e9chantillon doit contenir au moins {0} points" },

    // org.spaceroots.mantissa.functions.ExhaustedSampleException
    { "sample contains only {0} elements",
      "l''\u00e9chantillon ne contient que {0} points" },

    // org.spaceroots.mantissa.geometry.CardanEulerSingularityException
    { "Cardan angles singularity",
      "singularit\u00e9 d''angles de Cardan" },
    { "Euler angles singularity",
      "singularit\u00e9 d''angles d''Euler" },

    // org.spaceroots.mantissa.geometry.Rotation
    { "a {0}x{1} matrix cannot be a rotation matrix",
      "une matrice {0}x{1} ne peut pas \u00e9tre une matrice de rotation" },
    { "the closest orthogonal matrix has a negative determinant {0}",
      "la matrice orthogonale la plus proche a un d\u00e9terminant n\u00e9gatif {0}" },
    { "unable to orthogonalize matrix in {0} iterations",
      "impossible de rendre la matrice orthogonale en {0} it\u00e9rations" },

    // org.spaceroots.mantissa.linalg;.SingularMatrixException
    { "singular matrix",
      "matrice singuli\u00e8re" },

    // org.spaceroots.mantissa.ode.AdaptiveStepsizeIntegrator
    { "minimal step size ({0}) reached, integration needs {1}",
      "pas minimal ({0}) atteint, l''int\u00e9gration n\u00e9cessite {1}" },

    // org.spaceroots.mantissa.ode.GraggBulirschStoerIntegrator,
    // org.spaceroots.mantissa.ode.RungeKuttaFehlbergIntegrator,
    // org.spaceroots.mantissa.ode.RungeKuttaIntegrator
    { "dimensions mismatch: ODE problem has dimension {0},"
    + " state vector has dimension {1}",
      "incompatibilit\u00e9 de dimensions entre le probl\u00e8me ODE ({0}),"
    + " et le vecteur d''\u00e9tat ({1})" },
    { "too small integration interval: length = {0}",
      "intervalle d''int\u00e9gration trop petit : {0}" },

    // org.spaceroots.mantissa.optimization.DirectSearchOptimizer
    { "none of the {0} start points lead to convergence",
      "aucun des {0} points de d\u00e9part n''aboutit \u00e0 une convergence"  },

    // org.spaceroots.mantissa.random.CorrelatedRandomVectorGenerator
    { "dimension mismatch {0} != {1}",
      "dimensions incompatibles {0} != {1}" },

    // org.spaceroots.mantissa.random.NotPositiveDefiniteMatrixException
    { "not positive definite matrix",
      "matrice non d\u00e9finie positive" }

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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10973.java