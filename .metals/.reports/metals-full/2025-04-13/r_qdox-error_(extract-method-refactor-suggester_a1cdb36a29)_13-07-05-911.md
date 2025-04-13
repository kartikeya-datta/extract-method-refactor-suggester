error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10427.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10427.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10427.java
text:
```scala
public F@@astCosineTransformer() {

/*
 * Copyright 2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import java.io.Serializable;
import org.apache.commons.math.analysis.*;
import org.apache.commons.math.complex.*;
import org.apache.commons.math.MathException;

/**
 * Implements the <a href="http://documents.wolfram.com/v5/Add-onsLinks/
 * StandardPackages/LinearAlgebra/FourierTrig.html">Fast Cosine Transform</a>
 * for transformation of one-dimensional data sets. For reference, see
 * <b>Fast Fourier Transforms</b>, ISBN 0849371635, chapter 3.
 * <p>
 * FCT is its own inverse, up to a multiplier depending on conventions.
 * The equations are listed in the comments of the corresponding methods.
 * <p>
 * Different from FFT and FST, FCT requires the length of data set to be
 * power of 2 plus one. Users should especially pay attention to the
 * function transformation on how this affects the sampling.
 *
 * @version $Revision$ $Date$
 */
public class FastCosineTransformer implements Serializable {

    /** serializable version identifier */
    static final long serialVersionUID = -7673941545134707766L;

    /**
     * Construct a default transformer.
     */
    FastCosineTransformer() {
        super();
    }

    /**
     * Transform the given real data set.
     * <p>
     * The formula is $ F_n = (1/2) [f_0 + (-1)^n f_N] +
     *                        \Sigma_{k=0}^{N-1} f_k \cos(\pi nk/N) $
     *
     * @param f the real data array to be transformed
     * @return the real transformed array
     * @throws MathException if any math-related errors occur
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public double[] transform(double f[]) throws MathException,
        IllegalArgumentException {

        return fct(f);
    }

    /**
     * Transform the given real function, sampled on the given interval.
     * <p>
     * The formula is $ F_n = (1/2) [f_0 + (-1)^n f_N] +
     *                        \Sigma_{k=0}^{N-1} f_k \cos(\pi nk/N) $
     *
     * @param f the function to be sampled and transformed
     * @param min the lower bound for the interval
     * @param max the upper bound for the interval
     * @param n the number of sample points
     * @return the real transformed array
     * @throws MathException if any math-related errors occur
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public double[] transform(
        UnivariateRealFunction f, double min, double max, int n)
        throws MathException, IllegalArgumentException {

        double data[] = FastFourierTransformer.sample(f, min, max, n);
        return fct(data);
    }

    /**
     * Transform the given real data set.
     * <p>
     * The formula is $ F_n = \sqrt{1/2N} [f_0 + (-1)^n f_N] +
     *                        \sqrt{2/N} \Sigma_{k=0}^{N-1} f_k \cos(\pi nk/N) $
     *
     * @param f the real data array to be transformed
     * @return the real transformed array
     * @throws MathException if any math-related errors occur
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public double[] transform2(double f[]) throws MathException,
        IllegalArgumentException {

        double scaling_coefficient = Math.sqrt(2.0 / (f.length-1));
        return FastFourierTransformer.scaleArray(fct(f), scaling_coefficient);
    }

    /**
     * Transform the given real function, sampled on the given interval.
     * <p>
     * The formula is $ F_n = \sqrt{1/2N} [f_0 + (-1)^n f_N] +
     *                        \sqrt{2/N} \Sigma_{k=0}^{N-1} f_k \cos(\pi nk/N) $
     *
     * @param f the function to be sampled and transformed
     * @param min the lower bound for the interval
     * @param max the upper bound for the interval
     * @param n the number of sample points
     * @return the real transformed array
     * @throws MathException if any math-related errors occur
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public double[] transform2(
        UnivariateRealFunction f, double min, double max, int n)
        throws MathException, IllegalArgumentException {

        double data[] = FastFourierTransformer.sample(f, min, max, n);
        double scaling_coefficient = Math.sqrt(2.0 / (n-1));
        return FastFourierTransformer.scaleArray(fct(data), scaling_coefficient);
    }

    /**
     * Inversely transform the given real data set.
     * <p>
     * The formula is $ f_k = (1/N) [F_0 + (-1)^k F_N] +
     *                        (2/N) \Sigma_{n=0}^{N-1} F_n \cos(\pi nk/N) $
     *
     * @param f the real data array to be inversely transformed
     * @return the real inversely transformed array
     * @throws MathException if any math-related errors occur
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public double[] inversetransform(double f[]) throws MathException,
        IllegalArgumentException {

        double scaling_coefficient = 2.0 / (f.length - 1);
        return FastFourierTransformer.scaleArray(fct(f), scaling_coefficient);
    }

    /**
     * Inversely transform the given real function, sampled on the given interval.
     * <p>
     * The formula is $ f_k = (1/N) [F_0 + (-1)^k F_N] +
     *                        (2/N) \Sigma_{n=0}^{N-1} F_n \cos(\pi nk/N) $
     *
     * @param f the function to be sampled and inversely transformed
     * @param min the lower bound for the interval
     * @param max the upper bound for the interval
     * @param n the number of sample points
     * @return the real inversely transformed array
     * @throws MathException if any math-related errors occur
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public double[] inversetransform(
        UnivariateRealFunction f, double min, double max, int n)
        throws MathException, IllegalArgumentException {

        double data[] = FastFourierTransformer.sample(f, min, max, n);
        double scaling_coefficient = 2.0 / (n - 1);
        return FastFourierTransformer.scaleArray(fct(data), scaling_coefficient);
    }

    /**
     * Inversely transform the given real data set.
     * <p>
     * The formula is $ f_k = \sqrt{1/2N} [F_0 + (-1)^k F_N] +
     *                        \sqrt{2/N} \Sigma_{n=0}^{N-1} F_n \cos(\pi nk/N) $
     *
     * @param f the real data array to be inversely transformed
     * @return the real inversely transformed array
     * @throws MathException if any math-related errors occur
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public double[] inversetransform2(double f[]) throws MathException,
        IllegalArgumentException {

        return transform2(f);
    }

    /**
     * Inversely transform the given real function, sampled on the given interval.
     * <p>
     * The formula is $ f_k = \sqrt{1/2N} [F_0 + (-1)^k F_N] +
     *                        \sqrt{2/N} \Sigma_{n=0}^{N-1} F_n \cos(\pi nk/N) $
     *
     * @param f the function to be sampled and inversely transformed
     * @param min the lower bound for the interval
     * @param max the upper bound for the interval
     * @param n the number of sample points
     * @return the real inversely transformed array
     * @throws MathException if any math-related errors occur
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public double[] inversetransform2(
        UnivariateRealFunction f, double min, double max, int n)
        throws MathException, IllegalArgumentException {

        return transform2(f, min, max, n);
    }

    /**
     * Perform the FCT algorithm (including inverse).
     *
     * @param f the real data array to be transformed
     * @return the real transformed array
     * @throws MathException if any math-related errors occur
     * @throws IllegalArgumentException if any parameters are invalid
     */
    protected double[] fct(double f[]) throws MathException,
        IllegalArgumentException {

        double A, B, C, F1, x[], F[] = new double[f.length];

        int N = f.length - 1;
        if (!FastFourierTransformer.isPowerOf2(N)) {
            throw new IllegalArgumentException
                ("Number of samples not power of 2 plus one: " + f.length);
        }
        if (N == 1) {       // trivial case
            F[0] = 0.5 * (f[0] + f[1]);
            F[1] = 0.5 * (f[0] - f[1]);
            return F;
        }

        // construct a new array and perform FFT on it
        x = new double[N];
        x[0] = 0.5 * (f[0] + f[N]);
        x[N >> 1] = f[N >> 1];
        F1 = 0.5 * (f[0] - f[N]);   // temporary variable for F[1]
        for (int i = 1; i < (N >> 1); i++) {
            A = 0.5 * (f[i] + f[N-i]);
            B = Math.sin(i * Math.PI / N) * (f[i] - f[N-i]);
            C = Math.cos(i * Math.PI / N) * (f[i] - f[N-i]);
            x[i] = A - B;
            x[N-i] = A + B;
            F1 += C;
        }
        FastFourierTransformer transformer = new FastFourierTransformer();
        Complex y[] = transformer.transform(x);

        // reconstruct the FCT result for the original array
        F[0] = y[0].getReal();
        F[1] = F1;
        for (int i = 1; i < (N >> 1); i++) {
            F[2*i] = y[i].getReal();
            F[2*i+1] = F[2*i-1] - y[i].getImaginary();
        }
        F[N] = y[N >> 1].getReal();

        return F;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10427.java