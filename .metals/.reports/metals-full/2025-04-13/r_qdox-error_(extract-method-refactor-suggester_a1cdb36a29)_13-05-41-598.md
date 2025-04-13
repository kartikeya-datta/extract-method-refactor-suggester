error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4418.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4418.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4418.java
text:
```scala
t@@hrows DimensionMismatchException, NoDataException {

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
package org.apache.commons.math.analysis.interpolation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.DimensionMismatchException;
import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.exception.NoDataException;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.random.UnitSphereRandomVectorGenerator;

/**
 * Interpolating function that implements the
 * <a href="http://www.dudziak.com/microsphere.php">Microsphere Projection</a>.
 *
 * @version $Revision$ $Date$
 */
public class MicrosphereInterpolatingFunction
    implements MultivariateRealFunction {
    /**
     * Space dimension.
     */
    private final int dimension;
    /**
     * Internal accounting data for the interpolation algorithm.
     * Each element of the list corresponds to one surface element of
     * the microsphere.
     */
    private final List<MicrosphereSurfaceElement> microsphere;
    /**
     * Exponent used in the power law that computes the weights of the
     * sample data.
     */
    private final double brightnessExponent;
    /**
     * Sample data.
     */
    private final Map<RealVector, Double> samples;

    /**
     * Class for storing the accounting data needed to perform the
     * microsphere projection.
     */
    private static class MicrosphereSurfaceElement {

        /** Normal vector characterizing a surface element. */
        private final RealVector normal;

        /** Illumination received from the brightest sample. */
        private double brightestIllumination;

        /** Brightest sample. */
        private Map.Entry<RealVector, Double> brightestSample;

        /**
         * @param n Normal vector characterizing a surface element
         * of the microsphere.
         */
        MicrosphereSurfaceElement(double[] n) {
            normal = new ArrayRealVector(n);
        }

        /**
         * Return the normal vector.
         * @return the normal vector
         */
        RealVector normal() {
            return normal;
        }

        /**
         * Reset "illumination" and "sampleIndex".
         */
        void reset() {
            brightestIllumination = 0;
            brightestSample = null;
        }

        /**
         * Store the illumination and index of the brightest sample.
         * @param illuminationFromSample illumination received from sample
         * @param sample current sample illuminating the element
         */
        void store(final double illuminationFromSample,
                   final Map.Entry<RealVector, Double> sample) {
            if (illuminationFromSample > this.brightestIllumination) {
                this.brightestIllumination = illuminationFromSample;
                this.brightestSample = sample;
            }
        }

        /**
         * Get the illumination of the element.
         * @return the illumination.
         */
        double illumination() {
            return brightestIllumination;
        }

        /**
         * Get the sample illuminating the element the most.
         * @return the sample.
         */
        Map.Entry<RealVector, Double> sample() {
            return brightestSample;
        }
    }

    /**
     * @param xval the arguments for the interpolation points.
     * {@code xval[i][0]} is the first component of interpolation point
     * {@code i}, {@code xval[i][1]} is the second component, and so on
     * until {@code xval[i][d-1]}, the last component of that interpolation
     * point (where {@code dimension} is thus the dimension of the sampled
     * space).
     * @param yval the values for the interpolation points
     * @param brightnessExponent Brightness dimming factor.
     * @param microsphereElements Number of surface elements of the
     * microsphere.
     * @param rand Unit vector generator for creating the microsphere.
     * @throws DimensionMismatchException if the lengths of {@code yval} and
     * {@code xval} (equal to {@code n}, the number of interpolation points)
     * do not match, or the the arrays {@code xval[0]} ... {@code xval[n]},
     * have lengths different from {@code dimension}.
     * @throws NoDataException if there are no data (xval null or zero length)
     */
    public MicrosphereInterpolatingFunction(double[][] xval,
                                            double[] yval,
                                            int brightnessExponent,
                                            int microsphereElements,
                                            UnitSphereRandomVectorGenerator rand)
        throws DimensionMismatchException, IllegalArgumentException {
        if (xval.length == 0 || xval[0] == null) {
            throw new NoDataException();
        }

        if (xval.length != yval.length) {
            throw new DimensionMismatchException(xval.length, yval.length);
        }

        dimension = xval[0].length;
        this.brightnessExponent = brightnessExponent;

        // Copy data samples.
        samples = new HashMap<RealVector, Double>(yval.length);
        for (int i = 0; i < xval.length; ++i) {
            final double[] xvalI = xval[i];
            if ( xvalI.length != dimension) {
                throw new DimensionMismatchException(xvalI.length, dimension);
            }

            samples.put(new ArrayRealVector(xvalI), yval[i]);
        }

        microsphere = new ArrayList<MicrosphereSurfaceElement>(microsphereElements);
        // Generate the microsphere, assuming that a fairly large number of
        // randomly generated normals will represent a sphere.
        for (int i = 0; i < microsphereElements; i++) {
            microsphere.add(new MicrosphereSurfaceElement(rand.nextVector()));
        }

    }

    /**
     * @param point Interpolation point.
     * @return the interpolated value.
     */
    public double value(double[] point) {

        final RealVector p = new ArrayRealVector(point);

        // Reset.
        for (MicrosphereSurfaceElement md : microsphere) {
            md.reset();
        }

        // Compute contribution of each sample points to the microsphere elements illumination
        for (Map.Entry<RealVector, Double> sd : samples.entrySet()) {

            // Vector between interpolation point and current sample point.
            final RealVector diff = sd.getKey().subtract(p);
            final double diffNorm = diff.getNorm();

            if (Math.abs(diffNorm) < Math.ulp(1d)) {
                // No need to interpolate, as the interpolation point is
                // actually (very close to) one of the sampled points.
                return sd.getValue();
            }

            for (MicrosphereSurfaceElement md : microsphere) {
                final double w = Math.pow(diffNorm, -brightnessExponent);
                md.store(cosAngle(diff, md.normal()) * w, sd);
            }

        }

        // Interpolation calculation.
        double value = 0;
        double totalWeight = 0;
        for (MicrosphereSurfaceElement md : microsphere) {
            final double iV = md.illumination();
            final Map.Entry<RealVector, Double> sd = md.sample();
            if (sd != null) {
                value += iV * sd.getValue();
                totalWeight += iV;
            }
        }

        return value / totalWeight;

    }

    /**
     * Compute the cosine of the angle between 2 vectors.
     *
     * @param v Vector.
     * @param w Vector.
     * @return cosine of the angle
     */
    private double cosAngle(final RealVector v, final RealVector w) {
        return v.dotProduct(w) / (v.getNorm() * w.getNorm());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4418.java