error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2118.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2118.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2118.java
text:
```scala
r@@eturn new AbstractLeastSquaresOptimizer(null) {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.apache.commons.math3.optimization.general;

import java.io.IOException;
import java.util.Arrays;

import junit.framework.Assert;

import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    public static AbstractLeastSquaresOptimizer createOptimizer() {
        return new AbstractLeastSquaresOptimizer() {

            @Override
            protected PointVectorValuePair doOptimize() {
                updateResidualsAndCost();
                updateJacobian();
                return null;
            }
        };
    }

    @Test
    public void testGetChiSquare() throws IOException {
        final StatisticalReferenceDataset dataset;
        dataset = StatisticalReferenceDatasetFactory.createKirby2();
        final AbstractLeastSquaresOptimizer optimizer;
        optimizer = createOptimizer();
        final double[] a = dataset.getParameters();
        final double[] y = dataset.getData()[1];
        final double[] w = new double[y.length];
        Arrays.fill(w, 1.0);

        optimizer.optimize(1, dataset.getLeastSquaresProblem(), y, w, a);
        final double expected = dataset.getResidualSumOfSquares();
        final double actual = optimizer.getChiSquare();
        Assert.assertEquals(dataset.getName(), expected, actual,
                            1E-11 * expected);
    }

    @Test
    public void testGetRMS() throws IOException {
        final StatisticalReferenceDataset dataset;
        dataset = StatisticalReferenceDatasetFactory.createKirby2();
        final AbstractLeastSquaresOptimizer optimizer;
        optimizer = createOptimizer();
        final double[] a = dataset.getParameters();
        final double[] y = dataset.getData()[1];
        final double[] w = new double[y.length];
        Arrays.fill(w, 1.0);

        optimizer.optimize(1, dataset.getLeastSquaresProblem(), y, w, a);
        final double expected = FastMath
            .sqrt(dataset.getResidualSumOfSquares() /
                  dataset.getNumObservations());
        final double actual = optimizer.getRMS();
        Assert.assertEquals(dataset.getName(), expected, actual,
                            1E-11 * expected);
    }

    @Test
    public void testGetSigma() throws IOException {
        final StatisticalReferenceDataset dataset;
        dataset = StatisticalReferenceDatasetFactory.createKirby2();
        final AbstractLeastSquaresOptimizer optimizer;
        optimizer = createOptimizer();
        final double[] a = dataset.getParameters();
        final double[] y = dataset.getData()[1];
        final double[] w = new double[y.length];
        Arrays.fill(w, 1.0);

        final int dof = y.length-a.length;
        optimizer.optimize(1, dataset.getLeastSquaresProblem(), y, w, a);
        final double[] sig = optimizer.getSigma();
        final double[] expected = dataset.getParametersStandardDeviations();
        for (int i = 0; i < sig.length; i++) {
            final double actual = FastMath.sqrt(optimizer.getChiSquare()/dof)*sig[i];
            Assert.assertEquals(dataset.getName() + ", parameter #" + i,
                                actual, expected[i], 1E-8 * expected[i]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2118.java