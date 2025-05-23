error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1241.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1241.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1241.java
text:
```scala
F@@ile file = new File(url.toURI());

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
package org.apache.commons.math3.random;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.math3.TestUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.BaseAbstractUnivariateIntegrator;
import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.RealDistributionAbstractTest;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the EmpiricalDistribution class
 *
 * @version $Id$
 */

public final class EmpiricalDistributionTest extends RealDistributionAbstractTest {

    protected EmpiricalDistribution empiricalDistribution = null;
    protected EmpiricalDistribution empiricalDistribution2 = null;
    protected File file = null;
    protected URL url = null;
    protected double[] dataArray = null;
    protected final int n = 10000;

    @Before
    public void setUp() {
        super.setUp();
        empiricalDistribution = new EmpiricalDistribution(100);
//         empiricalDistribution = new EmpiricalDistribution(100, new RandomDataImpl()); // XXX Deprecated API
        url = getClass().getResource("testData.txt");
        final ArrayList<Double> list = new ArrayList<Double>();
        try {
            empiricalDistribution2 = new EmpiricalDistribution(100);
//             empiricalDistribution2 = new EmpiricalDistribution(100, new RandomDataImpl()); // XXX Deprecated API
            BufferedReader in =
                new BufferedReader(new InputStreamReader(
                        url.openStream()));
            String str = null;
            while ((str = in.readLine()) != null) {
                list.add(Double.valueOf(str));
            }
            in.close();
            in = null;
        } catch (IOException ex) {
            Assert.fail("IOException " + ex);
        }

        dataArray = new double[list.size()];
        int i = 0;
        for (Double data : list) {
            dataArray[i] = data.doubleValue();
            i++;
        }
    }

    /**
     * Test EmpiricalDistrbution.load() using sample data file.<br>
     * Check that the sampleCount, mu and sigma match data in
     * the sample data file. Also verify that load is idempotent.
     */
    @Test
    public void testLoad() throws Exception {
        // Load from a URL
        empiricalDistribution.load(url);
        checkDistribution();
        
        // Load again from a file (also verifies idempotency of load)
        File file = new File(url.getFile());
        empiricalDistribution.load(file);
        checkDistribution();
    }
    
    private void checkDistribution() {
        // testData File has 10000 values, with mean ~ 5.0, std dev ~ 1
        // Make sure that loaded distribution matches this
        Assert.assertEquals(empiricalDistribution.getSampleStats().getN(),1000,10E-7);
        //TODO: replace with statistical tests
        Assert.assertEquals(empiricalDistribution.getSampleStats().getMean(),
                5.069831575018909,10E-7);
        Assert.assertEquals(empiricalDistribution.getSampleStats().getStandardDeviation(),
                1.0173699343977738,10E-7);
    }

    /**
     * Test EmpiricalDistrbution.load(double[]) using data taken from
     * sample data file.<br>
     * Check that the sampleCount, mu and sigma match data in
     * the sample data file.
     */
    @Test
    public void testDoubleLoad() throws Exception {
        empiricalDistribution2.load(dataArray);
        // testData File has 10000 values, with mean ~ 5.0, std dev ~ 1
        // Make sure that loaded distribution matches this
        Assert.assertEquals(empiricalDistribution2.getSampleStats().getN(),1000,10E-7);
        //TODO: replace with statistical tests
        Assert.assertEquals(empiricalDistribution2.getSampleStats().getMean(),
                5.069831575018909,10E-7);
        Assert.assertEquals(empiricalDistribution2.getSampleStats().getStandardDeviation(),
                1.0173699343977738,10E-7);

        double[] bounds = empiricalDistribution2.getGeneratorUpperBounds();
        Assert.assertEquals(bounds.length, 100);
        Assert.assertEquals(bounds[99], 1.0, 10e-12);

    }

    /**
      * Generate 1000 random values and make sure they look OK.<br>
      * Note that there is a non-zero (but very small) probability that
      * these tests will fail even if the code is working as designed.
      */
    @Test
    public void testNext() throws Exception {
        tstGen(0.1);
        tstDoubleGen(0.1);
    }

    /**
      * Make sure exception thrown if digest getNext is attempted
      * before loading empiricalDistribution.
     */
    @Test
    public void testNexFail() {
        try {
            empiricalDistribution.getNextValue();
            empiricalDistribution2.getNextValue();
            Assert.fail("Expecting IllegalStateException");
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    /**
     * Make sure we can handle a grid size that is too fine
     */
    @Test
    public void testGridTooFine() throws Exception {
        empiricalDistribution = new EmpiricalDistribution(1001);
        tstGen(0.1);
        empiricalDistribution2 = new EmpiricalDistribution(1001);
        tstDoubleGen(0.1);
    }

    /**
     * How about too fat?
     */
    @Test
    public void testGridTooFat() throws Exception {
        empiricalDistribution = new EmpiricalDistribution(1);
        tstGen(5); // ridiculous tolerance; but ridiculous grid size
                   // really just checking to make sure we do not bomb
        empiricalDistribution2 = new EmpiricalDistribution(1);
        tstDoubleGen(5);
    }

    /**
     * Test bin index overflow problem (BZ 36450)
     */
    @Test
    public void testBinIndexOverflow() throws Exception {
        double[] x = new double[] {9474.94326071674, 2080107.8865462579};
        new EmpiricalDistribution().load(x);
    }

    @Test
    public void testSerialization() {
        // Empty
        EmpiricalDistribution dist = new EmpiricalDistribution();
        EmpiricalDistribution dist2 = (EmpiricalDistribution) TestUtils.serializeAndRecover(dist);
        verifySame(dist, dist2);

        // Loaded
        empiricalDistribution2.load(dataArray);
        dist2 = (EmpiricalDistribution) TestUtils.serializeAndRecover(empiricalDistribution2);
        verifySame(empiricalDistribution2, dist2);
    }

    @Test(expected=NullArgumentException.class)
    public void testLoadNullDoubleArray() {
       new EmpiricalDistribution().load((double[]) null);
    }

    @Test(expected=NullArgumentException.class)
    public void testLoadNullURL() throws Exception {
        new EmpiricalDistribution().load((URL) null);
    }

    @Test(expected=NullArgumentException.class)
    public void testLoadNullFile() throws Exception {
        new EmpiricalDistribution().load((File) null);
    }

    /**
     * MATH-298
     */
    @Test
    public void testGetBinUpperBounds() {
        double[] testData = {0, 1, 1, 2, 3, 4, 4, 5, 6, 7, 8, 9, 10};
        EmpiricalDistribution dist = new EmpiricalDistribution(5);
        dist.load(testData);
        double[] expectedBinUpperBounds = {2, 4, 6, 8, 10};
        double[] expectedGeneratorUpperBounds = {4d/13d, 7d/13d, 9d/13d, 11d/13d, 1};
        double tol = 10E-12;
        TestUtils.assertEquals(expectedBinUpperBounds, dist.getUpperBounds(), tol);
        TestUtils.assertEquals(expectedGeneratorUpperBounds, dist.getGeneratorUpperBounds(), tol);
    }
    
    @Test
    public void testGeneratorConfig() {
        double[] testData = {0, 1, 2, 3, 4};
        RandomGenerator generator = new RandomAdaptorTest.ConstantGenerator(0.5);
        
        EmpiricalDistribution dist = new EmpiricalDistribution(5, generator);
        dist.load(testData);
        for (int i = 0; i < 5; i++) {
            Assert.assertEquals(2.0, dist.getNextValue(), 0d);
        }
        
        // Verify no NPE with null generator argument
        dist = new EmpiricalDistribution(5, (RandomGenerator) null);
        dist.load(testData);
        dist.getNextValue();
    }
    
    @Test
    public void testReSeed() throws Exception {
        empiricalDistribution.load(url);
        empiricalDistribution.reSeed(100);
        final double [] values = new double[10];
        for (int i = 0; i < 10; i++) {
            values[i] = empiricalDistribution.getNextValue();
        }
        empiricalDistribution.reSeed(100);
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(values[i],empiricalDistribution.getNextValue(), 0d);
        }
    }

    private void verifySame(EmpiricalDistribution d1, EmpiricalDistribution d2) {
        Assert.assertEquals(d1.isLoaded(), d2.isLoaded());
        Assert.assertEquals(d1.getBinCount(), d2.getBinCount());
        Assert.assertEquals(d1.getSampleStats(), d2.getSampleStats());
        if (d1.isLoaded()) {
            for (int i = 0;  i < d1.getUpperBounds().length; i++) {
                Assert.assertEquals(d1.getUpperBounds()[i], d2.getUpperBounds()[i], 0);
            }
            Assert.assertEquals(d1.getBinStats(), d2.getBinStats());
        }
    }

    private void tstGen(double tolerance)throws Exception {
        empiricalDistribution.load(url);
        empiricalDistribution.reSeed(1000);
        SummaryStatistics stats = new SummaryStatistics();
        for (int i = 1; i < 1000; i++) {
            stats.addValue(empiricalDistribution.getNextValue());
        }
        Assert.assertEquals("mean", 5.069831575018909, stats.getMean(),tolerance);
        Assert.assertEquals("std dev", 1.0173699343977738, stats.getStandardDeviation(),tolerance);
    }

    private void tstDoubleGen(double tolerance)throws Exception {
        empiricalDistribution2.load(dataArray);
        empiricalDistribution2.reSeed(1000);
        SummaryStatistics stats = new SummaryStatistics();
        for (int i = 1; i < 1000; i++) {
            stats.addValue(empiricalDistribution2.getNextValue());
        }
        Assert.assertEquals("mean", 5.069831575018909, stats.getMean(), tolerance);
        Assert.assertEquals("std dev", 1.0173699343977738, stats.getStandardDeviation(), tolerance);
    }
   
    //  Setup for distribution tests
    
    @Override
    public RealDistribution makeDistribution() {
        // Create a uniform distribution on [0, 10,000]
        final double[] sourceData = new double[n + 1];
        for (int i = 0; i < n + 1; i++) {
            sourceData[i] = i;
        }
        EmpiricalDistribution dist = new EmpiricalDistribution();
        dist.load(sourceData);
        return dist;
    }
    
    /** Uniform bin mass = 10/10001 == mass of all but the first bin */
    private final double binMass = 10d / (double) (n + 1);
    
    /** Mass of first bin = 11/10001 */
    private final double firstBinMass = 11d / (double) (n + 1);

    @Override
    public double[] makeCumulativeTestPoints() {
       final double[] testPoints = new double[] {9, 10, 15, 1000, 5004, 9999};
       return testPoints;
    }
    

    @Override
    public double[] makeCumulativeTestValues() {
        /* 
         * Bins should be [0, 10], (10, 20], ..., (9990, 10000]
         * Kernels should be N(4.5, 3.02765), N(14.5, 3.02765)...
         * Each bin should have mass 10/10000 = .001
         */
        final double[] testPoints = getCumulativeTestPoints();
        final double[] cumValues = new double[testPoints.length];
        final EmpiricalDistribution empiricalDistribution = (EmpiricalDistribution) makeDistribution();
        final double[] binBounds = empiricalDistribution.getUpperBounds();
        for (int i = 0; i < testPoints.length; i++) {
            final int bin = findBin(testPoints[i]);
            final double lower = bin == 0 ? empiricalDistribution.getSupportLowerBound() :
                binBounds[bin - 1];
            final double upper = binBounds[bin];
            // Compute bMinus = sum or mass of bins below the bin containing the point
            // First bin has mass 11 / 10000, the rest have mass 10 / 10000.
            final double bMinus = bin == 0 ? 0 : (bin - 1) * binMass + firstBinMass;
            final RealDistribution kernel = findKernel(lower, upper);
            final double withinBinKernelMass = kernel.cumulativeProbability(lower, upper);
            final double kernelCum = kernel.cumulativeProbability(lower, testPoints[i]);
            cumValues[i] = bMinus + (bin == 0 ? firstBinMass : binMass) * kernelCum/withinBinKernelMass;
        }
        return cumValues;
    }

    @Override
    public double[] makeDensityTestValues() {
        final double[] testPoints = getCumulativeTestPoints();
        final double[] densityValues = new double[testPoints.length];
        final EmpiricalDistribution empiricalDistribution = (EmpiricalDistribution) makeDistribution();
        final double[] binBounds = empiricalDistribution.getUpperBounds();
        for (int i = 0; i < testPoints.length; i++) {
            final int bin = findBin(testPoints[i]);
            final double lower = bin == 0 ? empiricalDistribution.getSupportLowerBound() :
                binBounds[bin - 1];
            final double upper = binBounds[bin];
            final RealDistribution kernel = findKernel(lower, upper);
            final double withinBinKernelMass = kernel.cumulativeProbability(lower, upper);
            final double density = kernel.density(testPoints[i]);
            densityValues[i] = density * (bin == 0 ? firstBinMass : binMass) / withinBinKernelMass;   
        }
        return densityValues;
    }
    
    /** 
     * Modify test integration bounds from the default. Because the distribution
     * has discontinuities at bin boundaries, integrals spanning multiple bins
     * will face convergence problems.  Only test within-bin integrals and spans
     * across no more than 3 bin boundaries.
     */
    @Override
    @Test
    public void testDensityIntegrals() {
        final RealDistribution distribution = makeDistribution();
        final double tol = 1.0e-9;
        final BaseAbstractUnivariateIntegrator integrator =
            new IterativeLegendreGaussIntegrator(5, 1.0e-12, 1.0e-10);
        final UnivariateFunction d = new UnivariateFunction() {
            public double value(double x) {
                return distribution.density(x);
            }
        };
        final double[] lower = {0, 5, 1000, 5001, 9995};
        final double[] upper = {5, 12, 1030, 5010, 10000};
        for (int i = 1; i < 5; i++) {
            Assert.assertEquals(
                    distribution.cumulativeProbability( 
                            lower[i], upper[i]),
                            integrator.integrate(
                                    1000000, // Triangle integrals are very slow to converge
                                    d, lower[i], upper[i]), tol);
        }
    }
    
    /**
     * Find the bin that x belongs (relative to {@link #makeDistribution()}).
     */
    private int findBin(double x) {
        // Number of bins below x should be trunc(x/10)
        final double nMinus = Math.floor(x / 10);
        final int bin =  (int) Math.round(nMinus);
        // If x falls on a bin boundary, it is in the lower bin
        return Math.floor(x / 10) == x / 10 ? bin - 1 : bin;
    }
    
    /**
     * Find the within-bin kernel for the bin with lower bound lower
     * and upper bound upper. All bins other than the first contain 10 points
     * exclusive of the lower bound and are centered at (lower + upper + 1) / 2.
     * The first bin includes its lower bound, 0, so has different mean and
     * standard deviation.
     */
    private RealDistribution findKernel(double lower, double upper) {
        if (lower < 1) {
            return new NormalDistribution(5d, 3.3166247903554);
        } else {
            return new NormalDistribution((upper + lower + 1) / 2d, 3.0276503540974917); 
        }
    }
    
    @Test
    public void testKernelOverrideConstant() {
        final EmpiricalDistribution dist = new ConstantKernelEmpiricalDistribution(5);
        final double[] data = {1d,2d,3d, 4d,5d,6d, 7d,8d,9d, 10d,11d,12d, 13d,14d,15d};
        dist.load(data);
        // Bin masses concentrated on 2, 5, 8, 11, 14 <- effectively discrete uniform distribution over these
        double[] values = {2d, 5d, 8d, 11d, 14d};
        for (int i = 0; i < 20; i++) {
            Assert.assertTrue(Arrays.binarySearch(values, dist.sample()) >= 0);
        }
        final double tol = 10E-12;
        Assert.assertEquals(0.0, dist.cumulativeProbability(1), tol);
        Assert.assertEquals(0.2, dist.cumulativeProbability(2), tol);
        Assert.assertEquals(0.6, dist.cumulativeProbability(10), tol);
        Assert.assertEquals(0.8, dist.cumulativeProbability(12), tol);
        Assert.assertEquals(0.8, dist.cumulativeProbability(13), tol);
        Assert.assertEquals(1.0, dist.cumulativeProbability(15), tol);

        Assert.assertEquals(2.0, dist.inverseCumulativeProbability(0.1), tol);
        Assert.assertEquals(2.0, dist.inverseCumulativeProbability(0.2), tol);
        Assert.assertEquals(5.0, dist.inverseCumulativeProbability(0.3), tol);
        Assert.assertEquals(5.0, dist.inverseCumulativeProbability(0.4), tol);
        Assert.assertEquals(8.0, dist.inverseCumulativeProbability(0.5), tol);
        Assert.assertEquals(8.0, dist.inverseCumulativeProbability(0.6), tol);
    }
    
    @Test
    public void testKernelOverrideUniform() {
        final EmpiricalDistribution dist = new UniformKernelEmpiricalDistribution(5);
        final double[] data = {1d,2d,3d, 4d,5d,6d, 7d,8d,9d, 10d,11d,12d, 13d,14d,15d};
        dist.load(data);
        // Kernels are uniform distributions on [1,3], [4,6], [7,9], [10,12], [13,15]
        final double bounds[] = {3d, 6d, 9d, 12d};
        final double tol = 10E-12; 
        for (int i = 0; i < 20; i++) {
            final double v = dist.sample();
            // Make sure v is not in the excluded range between bins - that is (bounds[i], bounds[i] + 1)
            for (int j = 0; j < bounds.length; j++) {
                Assert.assertFalse(v > bounds[j] + tol && v < bounds[j] + 1 - tol);
            }
        }   
        Assert.assertEquals(0.0, dist.cumulativeProbability(1), tol);
        Assert.assertEquals(0.1, dist.cumulativeProbability(2), tol);
        Assert.assertEquals(0.6, dist.cumulativeProbability(10), tol);
        Assert.assertEquals(0.8, dist.cumulativeProbability(12), tol);
        Assert.assertEquals(0.8, dist.cumulativeProbability(13), tol);
        Assert.assertEquals(1.0, dist.cumulativeProbability(15), tol);

        Assert.assertEquals(2.0, dist.inverseCumulativeProbability(0.1), tol);
        Assert.assertEquals(3.0, dist.inverseCumulativeProbability(0.2), tol);
        Assert.assertEquals(5.0, dist.inverseCumulativeProbability(0.3), tol);
        Assert.assertEquals(6.0, dist.inverseCumulativeProbability(0.4), tol);
        Assert.assertEquals(8.0, dist.inverseCumulativeProbability(0.5), tol);
        Assert.assertEquals(9.0, dist.inverseCumulativeProbability(0.6), tol);
    }
    
    
    /**
     * Empirical distribution using a constant smoothing kernel.
     */
    private class ConstantKernelEmpiricalDistribution extends EmpiricalDistribution {
        private static final long serialVersionUID = 1L;
        public ConstantKernelEmpiricalDistribution(int i) {
            super(i);
        }
        // Use constant distribution equal to bin mean within bin
        protected RealDistribution getKernel(SummaryStatistics bStats) {
            return new ConstantDistribution(bStats.getMean());
        }
    }
    
    /**
     * Empirical distribution using a uniform smoothing kernel.
     */
    private class UniformKernelEmpiricalDistribution extends EmpiricalDistribution {
        public UniformKernelEmpiricalDistribution(int i) {
            super(i);
        }
        protected RealDistribution getKernel(SummaryStatistics bStats) {
            return new UniformRealDistribution(randomData.getRandomGenerator(), bStats.getMin(), bStats.getMax(),
                    UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
        }
    }
    
    /**
     * Distribution that takes just one value.
     */
    private class ConstantDistribution extends AbstractRealDistribution {
        private static final long serialVersionUID = 1L;
        
        /** Singleton value in the sample space */
        private final double c;
        
        public ConstantDistribution(double c) {
            this.c = c;
        }
        
        public double density(double x) {
            return 0;
        }

        public double cumulativeProbability(double x) {
            return x < c ? 0 : 1;
        }
        
        @Override
        public double inverseCumulativeProbability(double p) {
            if (p < 0.0 || p > 1.0) {
                throw new OutOfRangeException(p, 0, 1);
            }
            return c;
        }

        public double getNumericalMean() {
            return c;
        }

        public double getNumericalVariance() {
            return 0;
        }

        public double getSupportLowerBound() {
            return c;
        }

        public double getSupportUpperBound() {
            return c;
        }

        public boolean isSupportLowerBoundInclusive() {
            return false;
        }

        public boolean isSupportUpperBoundInclusive() {
            return true;
        }

        public boolean isSupportConnected() {
            return true;
        }
        
        @Override
        public double sample() {
            return c;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1241.java