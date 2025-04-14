error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2615.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2615.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2615.java
text:
```scala
p@@ublic interface BaseUnivariateOptimizer<FUNC extends UnivariateFunction>

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

package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.optimization.BaseOptimizer;
import org.apache.commons.math.optimization.GoalType;

/**
 * This interface is mainly intended to enforce the internal coherence of
 * Commons-Math. Users of the API are advised to base their code on
 * the following interfaces:
 * <ul>
 *  <li>{@link org.apache.commons.math.optimization.univariate.UnivariateRealOptimizer}</li>
 * </ul>
 *
 * @param <FUNC> Type of the objective function to be optimized.
 *
 * @version $Id$
 * @since 3.0
 */
public interface BaseUnivariateRealOptimizer<FUNC extends UnivariateFunction>
    extends BaseOptimizer<UnivariateRealPointValuePair> {
    /**
     * Find an optimum in the given interval.
     *
     * An optimizer may require that the interval brackets a single optimum.
     *
     * @param f Function to optimize.
     * @param goalType Type of optimization goal: either
     * {@link GoalType#MAXIMIZE} or {@link GoalType#MINIMIZE}.
     * @param min Lower bound for the interval.
     * @param max Upper bound for the interval.
     * @param maxEval Maximum number of function evaluations.
     * @return a (point, value) pair where the function is optimum.
     * @throws org.apache.commons.math.exception.TooManyEvaluationsException
     * if the maximum evaluation count is exceeded.
     * @throws org.apache.commons.math.exception.ConvergenceException
     * if the optimizer detects a convergence problem.
     * @throws IllegalArgumentException if {@code min > max} or the endpoints
     * do not satisfy the requirements specified by the optimizer.
     */
    UnivariateRealPointValuePair optimize(int maxEval, FUNC f, GoalType goalType,
                                          double min, double max);

    /**
     * Find an optimum in the given interval, start at startValue.
     * An optimizer may require that the interval brackets a single optimum.
     *
     * @param f Function to optimize.
     * @param goalType Type of optimization goal: either
     * {@link GoalType#MAXIMIZE} or {@link GoalType#MINIMIZE}.
     * @param min Lower bound for the interval.
     * @param max Upper bound for the interval.
     * @param startValue Start value to use.
     * @param maxEval Maximum number of function evaluations.
     * @return a (point, value) pair where the function is optimum.
     * @throws org.apache.commons.math.exception.TooManyEvaluationsException
     * if the maximum evaluation count is exceeded.
     * @throws org.apache.commons.math.exception.ConvergenceException if the
     * optimizer detects a convergence problem.
     * @throws IllegalArgumentException if {@code min > max} or the endpoints
     * do not satisfy the requirements specified by the optimizer.
     * @throws org.apache.commons.math.exception.NullArgumentException if any
     * argument is {@code null}.
     */
    UnivariateRealPointValuePair optimize(int maxEval, FUNC f, GoalType goalType,
                                          double min, double max,
                                          double startValue);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2615.java