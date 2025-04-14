error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8679.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8679.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8679.java
text:
```scala
F@@astFourierTransformer transformer = FastFourierTransformer.create();

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
package org.apache.commons.math.transform;

import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.util.FastMath;

/**
 * Implements the <a href="http://documents.wolfram.com/v5/Add-onsLinks/
 * StandardPackages/LinearAlgebra/FourierTrig.html">Fast Cosine Transform</a>
 * for transformation of one-dimensional data sets. For reference, see
 * <b>Fast Fourier Transforms</b>, ISBN 0849371635, chapter 3.
 * <p>
 * FCT is its own inverse, up to a multiplier depending on conventions.
 * The equations are listed in the comments of the corresponding methods.</p>
 * <p>
 * Different from FFT and FST, FCT requires the length of data set to be
 * power of 2 plus one. Users should especially pay attention to the
 * function transformation on how this affects the sampling.</p>
 * <p>As of version 2.0 this no longer implements Serializable</p>
 *
 * @version $Id$
 * @since 1.2
 */
public class FastCosineTransformer implements RealTransformer {

    /** Construct a default transformer. */
    public FastCosineTransformer() {
        super();
    }

    /**
     * Transform the given real data set.
     * <p>
     * The formula is F<sub>n</sub> = (1/2) [f<sub>0</sub> + (-1)<sup>n</sup> f<sub>N</sub>] +
     *                        &sum;<sub>k=1</sub><sup>N-1</sup> f<sub>k</sub> cos(&pi; nk/N)
     * </p>
     *
     * @param f the real data array to be transformed
     * @return the real transformed array
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public double[] transform(double[] f) throws IllegalArgumentException {
        return fct(f);
    }

    /**
     * Transform the given real function, sampled on the given interval.
     * <p>
     * The formula is F<sub>n</sub> = (1/2) [f<sub>0</sub> + (-1)<sup>n</sup> f<sub>N</sub>] +
     *                        &sum;<sub>k=1</sub><sup>N-1</sup> f<sub>k</sub> cos(&pi; nk/N)
     * </p>
     *
     * @param f the function to be sampled and transformed
     * @param min the lower bound for the interval
     * @param max the upper bound for the interval
     * @param n the number of sample points
     * @return the real transformed array
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public double[] transform(UnivariateFunction f,
                              double min, double max, int n)
        throws IllegalArgumentException {
        double[] data = FastFourierTransformer.sample(f, min, max, n);
        return fct(data);
    }

    /**
     * Transform the given real data set.
     * <p>
     * The formula is F<sub>n</sub> = &radic;(1/2N) [f<sub>0</sub> + (-1)<sup>n</sup> f<sub>N</sub>] +
     *                        &radic;(2/N) &sum;<sub>k=1</sub><sup>N-1</sup> f<sub>k</sub> cos(&pi; nk/N)
     * </p>
     *
     * @param f the real data array to be transformed
     * @return the real transformed array
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public double[] transform2(double[] f) throws IllegalArgumentException {

        double scalingCoefficient = FastMath.sqrt(2.0 / (f.length - 1));
        return FastFourierTransformer.scaleArray(fct(f), scalingCoefficient);
    }

    /**
     * Transform the given real function, sampled on the given interval.
     * <p>
     * The formula is F<sub>n</sub> = &radic;(1/2N) [f<sub>0</sub> + (-1)<sup>n</sup> f<sub>N</sub>] +
     *                        &radic;(2/N) &sum;<sub>k=1</sub><sup>N-1</sup> f<sub>k</sub> cos(&pi; nk/N)
     *
     * </p>
     *
     * @param f the function to be sampled and transformed
     * @param min the lower bound for the interval
     * @param max the upper bound for the interval
     * @param n the number of sample points
     * @return the real transformed array
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public double[] transform2(UnivariateFunction f,
                               double min, double max, int n)
        throws IllegalArgumentException {

        double[] data = FastFourierTransformer.sample(f, min, max, n);
        double scalingCoefficient = FastMath.sqrt(2.0 / (n - 1));
        return FastFourierTransformer.scaleArray(fct(data), scalingCoefficient);
    }

    /**
     * Inversely transform the given real data set.
     * <p>
     * The formula is f<sub>k</sub> = (1/N) [F<sub>0</sub> + (-1)<sup>k</sup> F<sub>N</sub>] +
     *                        (2/N) &sum;<sub>n=1</sub><sup>N-1</sup> F<sub>n</sub> cos(&pi; nk/N)
     * </p>
     *
     * @param f the real data array to be inversely transformed
     * @return the real inversely transformed array
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public double[] inverseTransform(double[] f)
    throws IllegalArgumentException {

        double scalingCoefficient = 2.0 / (f.length - 1);
        return FastFourierTransformer.scaleArray(fct(f), scalingCoefficient);
    }

    /**
     * Inversely transform the given real function, sampled on the given
     * interval.
     * <p>
     * The formula is f<sub>k</sub> = (1/N) [F<sub>0</sub> + (-1)<sup>k</sup> F<sub>N</sub>] +
     *                        (2/N) &sum;<sub>n=1</sub><sup>N-1</sup> F<sub>n</sub> cos(&pi; nk/N)
     * </p>
     *
     * @param f the function to be sampled and inversely transformed
     * @param min the lower bound for the interval
     * @param max the upper bound for the interval
     * @param n the number of sample points
     * @return the real inversely transformed array
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public double[] inverseTransform(UnivariateFunction f,
                                     double min, double max, int n)
        throws IllegalArgumentException {

        double[] data = FastFourierTransformer.sample(f, min, max, n);
        double scalingCoefficient = 2.0 / (n - 1);
        return FastFourierTransformer.scaleArray(fct(data), scalingCoefficient);
    }

    /**
     * Inversely transform the given real data set.
     * <p>
     * The formula is f<sub>k</sub> = &radic;(1/2N) [F<sub>0</sub> + (-1)<sup>k</sup> F<sub>N</sub>] +
     *                        &radic;(2/N) &sum;<sub>n=1</sub><sup>N-1</sup> F<sub>n</sub> cos(&pi; nk/N)
     * </p>
     *
     * @param f the real data array to be inversely transformed
     * @return the real inversely transformed array
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public double[] inverseTransform2(double[] f)
        throws IllegalArgumentException {
        return transform2(f);
    }

    /**
     * Inversely transform the given real function, sampled on the given
     * interval.
     * <p>
     * The formula is f<sub>k</sub> = &radic;(1/2N) [F<sub>0</sub> + (-1)<sup>k</sup> F<sub>N</sub>] +
     *                        &radic;(2/N) &sum;<sub>n=1</sub><sup>N-1</sup> F<sub>n</sub> cos(&pi; nk/N)
     * </p>
     *
     * @param f the function to be sampled and inversely transformed
     * @param min the lower bound for the interval
     * @param max the upper bound for the interval
     * @param n the number of sample points
     * @return the real inversely transformed array
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public double[] inverseTransform2(UnivariateFunction f,
                                      double min, double max, int n)
        throws IllegalArgumentException {

        return transform2(f, min, max, n);
    }

    /**
     * Perform the FCT algorithm (including inverse).
     *
     * @param f the real data array to be transformed
     * @return the real transformed array
     * @throws IllegalArgumentException if any parameters are invalid
     */
    protected double[] fct(double[] f)
        throws IllegalArgumentException {

        final double[] transformed = new double[f.length];

        final int n = f.length - 1;
        if (!FastFourierTransformer.isPowerOf2(n)) {
            throw MathRuntimeException.createIllegalArgumentException(
                    LocalizedFormats.NOT_POWER_OF_TWO_PLUS_ONE,
                    f.length);
        }
        if (n == 1) {       // trivial case
            transformed[0] = 0.5 * (f[0] + f[1]);
            transformed[1] = 0.5 * (f[0] - f[1]);
            return transformed;
        }

        // construct a new array and perform FFT on it
        final double[] x = new double[n];
        x[0] = 0.5 * (f[0] + f[n]);
        x[n >> 1] = f[n >> 1];
        // temporary variable for transformed[1]
        double t1 = 0.5 * (f[0] - f[n]);
        for (int i = 1; i < (n >> 1); i++) {
            final double a = 0.5 * (f[i] + f[n - i]);
            final double b = FastMath.sin(i * FastMath.PI / n) * (f[i] - f[n - i]);
            final double c = FastMath.cos(i * FastMath.PI / n) * (f[i] - f[n - i]);
            x[i] = a - b;
            x[n - i] = a + b;
            t1 += c;
        }
        FastFourierTransformer transformer = new FastFourierTransformer();
        Complex[] y = transformer.transform(x);

        // reconstruct the FCT result for the original array
        transformed[0] = y[0].getReal();
        transformed[1] = t1;
        for (int i = 1; i < (n >> 1); i++) {
            transformed[2 * i]     = y[i].getReal();
            transformed[2 * i + 1] = transformed[2 * i - 1] - y[i].getImaginary();
        }
        transformed[n] = y[n >> 1].getReal();

        return transformed;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8679.java