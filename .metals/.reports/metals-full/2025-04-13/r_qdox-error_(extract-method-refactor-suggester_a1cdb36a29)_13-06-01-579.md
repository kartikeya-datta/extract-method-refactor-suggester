error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1302.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1302.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1302.java
text:
```scala
private static final d@@ouble DEFAULT_EPSILON = 10e-15;

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
package org.apache.commons.math.special;

import java.io.Serializable;

import org.apache.commons.math.MathException;
import org.apache.commons.math.util.ContinuedFraction;

/**
 * This is a utility class that provides computation methods related to the
 * Beta family of functions.
 *
 * @version $Revision$ $Date$
 */
public class Beta implements Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = -3833485397404128220L;

    /** Maximum allowed numerical error. */
    private static final double DEFAULT_EPSILON = 10e-9;

    /**
     * Default constructor.  Prohibit instantiation.
     */
    private Beta() {
        super();
    }

    /**
     * Returns the
     * <a href="http://mathworld.wolfram.com/RegularizedBetaFunction.html">
     * regularized beta function</a> I(x, a, b).
     * 
     * @param x the value.
     * @param a the a parameter.
     * @param b the b parameter.
     * @return the regularized beta function I(x, a, b)
     * @throws MathException if the algorithm fails to converge.
     */
    public static double regularizedBeta(double x, double a, double b)
        throws MathException
    {
        return regularizedBeta(x, a, b, DEFAULT_EPSILON, Integer.MAX_VALUE);
    }

    /**
     * Returns the
     * <a href="http://mathworld.wolfram.com/RegularizedBetaFunction.html">
     * regularized beta function</a> I(x, a, b).
     * 
     * @param x the value.
     * @param a the a parameter.
     * @param b the b parameter.
     * @param epsilon When the absolute value of the nth item in the
     *                series is less than epsilon the approximation ceases
     *                to calculate further elements in the series.
     * @return the regularized beta function I(x, a, b)
     * @throws MathException if the algorithm fails to converge.
     */
    public static double regularizedBeta(double x, double a, double b,
        double epsilon) throws MathException
    {
        return regularizedBeta(x, a, b, epsilon, Integer.MAX_VALUE);
    }

    /**
     * Returns the regularized beta function I(x, a, b).
     * 
     * @param x the value.
     * @param a the a parameter.
     * @param b the b parameter.
     * @param maxIterations Maximum number of "iterations" to complete. 
     * @return the regularized beta function I(x, a, b)
     * @throws MathException if the algorithm fails to converge.
     */
    public static double regularizedBeta(double x, double a, double b,
        int maxIterations) throws MathException
    {
        return regularizedBeta(x, a, b, DEFAULT_EPSILON, maxIterations);
    }
    
    /**
     * Returns the regularized beta function I(x, a, b).
     * 
     * The implementation of this method is based on:
     * <ul>
     * <li>
     * <a href="http://mathworld.wolfram.com/RegularizedBetaFunction.html">
     * Regularized Beta Function</a>.</li>
     * <li>
     * <a href="http://functions.wolfram.com/06.21.10.0001.01">
     * Regularized Beta Function</a>.</li>
     * </ul>
     * 
     * @param x the value.
     * @param a the a parameter.
     * @param b the b parameter.
     * @param epsilon When the absolute value of the nth item in the
     *                series is less than epsilon the approximation ceases
     *                to calculate further elements in the series.
     * @param maxIterations Maximum number of "iterations" to complete. 
     * @return the regularized beta function I(x, a, b)
     * @throws MathException if the algorithm fails to converge.
     */
    public static double regularizedBeta(double x, final double a,
        final double b, double epsilon, int maxIterations) throws MathException
    {
        double ret;

        if (Double.isNaN(x) || Double.isNaN(a) || Double.isNaN(b) || (x < 0) ||
            (x > 1) || (a <= 0.0) || (b <= 0.0))
        {
            ret = Double.NaN;
        } else if (x > (a + 1.0) / (a + b + 2.0)) {
            ret = 1.0 - regularizedBeta(1.0 - x, b, a, epsilon, maxIterations);
        } else {
            ContinuedFraction fraction = new ContinuedFraction() {

                private static final long serialVersionUID = -7658917278956100597L;

                protected double getB(int n, double x) {
                    double ret;
                    double m;
                    if (n % 2 == 0) { // even
                        m = n / 2.0;
                        ret = (m * (b - m) * x) /
                            ((a + (2 * m) - 1) * (a + (2 * m)));
                    } else {
                        m = (n - 1.0) / 2.0;
                        ret = -((a + m) * (a + b + m) * x) /
                                ((a + (2 * m)) * (a + (2 * m) + 1.0));
                    }
                    return ret;
                }

                protected double getA(int n, double x) {
                    return 1.0;
                }
            };
            ret = Math.exp((a * Math.log(x)) + (b * Math.log(1.0 - x)) -
                Math.log(a) - logBeta(a, b, epsilon, maxIterations)) *
                1.0 / fraction.evaluate(x, epsilon, maxIterations);
        }

        return ret;
    }

    /**
     * Returns the natural logarithm of the beta function B(a, b).
     * 
     * @param a the a parameter.
     * @param b the b parameter.
     * @return log(B(a, b))
     */
    public static double logBeta(double a, double b) {
        return logBeta(a, b, DEFAULT_EPSILON, Integer.MAX_VALUE);
    }
    
    /**
     * Returns the natural logarithm of the beta function B(a, b).
     *
     * The implementation of this method is based on:
     * <ul>
     * <li><a href="http://mathworld.wolfram.com/BetaFunction.html">
     * Beta Function</a>, equation (1).</li>
     * </ul>
     * 
     * @param a the a parameter.
     * @param b the b parameter.
     * @param epsilon When the absolute value of the nth item in the
     *                series is less than epsilon the approximation ceases
     *                to calculate further elements in the series.
     * @param maxIterations Maximum number of "iterations" to complete. 
     * @return log(B(a, b))
     */
    public static double logBeta(double a, double b, double epsilon,
        int maxIterations) {
            
        double ret;

        if (Double.isNaN(a) || Double.isNaN(b) || (a <= 0.0) || (b <= 0.0)) {
            ret = Double.NaN;
        } else {
            ret = Gamma.logGamma(a) + Gamma.logGamma(b) -
                Gamma.logGamma(a + b);
        }

        return ret;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1302.java