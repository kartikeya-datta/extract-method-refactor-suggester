error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11956.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11956.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11956.java
text:
```scala
A@@bstractMultipleLinearRegression regression = createRegression();

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
package org.apache.commons.math.stat.regression;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


public abstract class MultipleLinearRegressionAbstractTest {

    protected AbstractMultipleLinearRegression regression;

    @Before
    public void setUp(){
        regression = createRegression();
    }

    protected abstract AbstractMultipleLinearRegression createRegression();

    protected abstract int getNumberOfRegressors();

    protected abstract int getSampleSize();

    @Test
    public void canEstimateRegressionParameters(){
        double[] beta = regression.estimateRegressionParameters();
        assertEquals(getNumberOfRegressors(), beta.length);
    }

    @Test
    public void canEstimateResiduals(){
        double[] e = regression.estimateResiduals();
        assertEquals(getSampleSize(), e.length);
    }

    @Test
    public void canEstimateRegressionParametersVariance(){
        double[][] variance = regression.estimateRegressionParametersVariance();
        assertEquals(getNumberOfRegressors(), variance.length);
    }

    @Test
    public void canEstimateRegressandVariance(){
        if (getSampleSize() > getNumberOfRegressors()) {
            double variance = regression.estimateRegressandVariance();
            assertTrue(variance > 0.0);
        }
    }
    
    /**
     * Verifies that newSampleData methods consistently insert unitary columns
     * in design matrix.  Confirms the fix for MATH-411.
     */
    @Test
    public void testNewSample() throws Exception {
        double[] design = new double[] {
          1, 19, 22, 33,
          2, 20, 30, 40,
          3, 25, 35, 45,
          4, 27, 37, 47
        };
        double[] y = new double[] {1, 2, 3, 4}; 
        double[][] x = new double[][] {
          {19, 22, 33},
          {20, 30, 40},
          {25, 35, 45},
          {27, 37, 47}   
        };
        AbstractMultipleLinearRegression regression = (AbstractMultipleLinearRegression) createRegression();
        regression.newSampleData(design, 4, 3);
        RealMatrix flatX = regression.X.copy();
        RealVector flatY = regression.Y.copy();
        regression.newXSampleData(x);
        regression.newYSampleData(y);
        assertEquals(flatX, regression.X);
        assertEquals(flatY, regression.Y);
        
        // No intercept
        regression.setNoIntercept(true);
        regression.newSampleData(design, 4, 3);
        flatX = regression.X.copy();
        flatY = regression.Y.copy();
        regression.newXSampleData(x);
        regression.newYSampleData(y);
        assertEquals(flatX, regression.X);
        assertEquals(flatY, regression.Y);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewSampleNullData() throws Exception {
        double[] data = null;
        createRegression().newSampleData(data, 2, 3); 
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewSampleInvalidData() throws Exception {
        double[] data = new double[] {1, 2, 3, 4};
        createRegression().newSampleData(data, 2, 3);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewSampleInsufficientData() throws Exception {
        double[] data = new double[] {1, 2, 3, 4};
        createRegression().newSampleData(data, 1, 3);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testXSampleDataNull() {
        createRegression().newXSampleData(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testYSampleDataNull() {
        createRegression().newYSampleData(null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11956.java