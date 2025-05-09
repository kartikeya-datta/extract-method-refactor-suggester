error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6124.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6124.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6124.java
text:
```scala
T@@estUtils.assertChiSquareAccept(expected, observed, 0.001);

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
package org.apache.commons.math.random;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


import org.apache.commons.math.Retry;
import org.apache.commons.math.RetryRunner;
import org.apache.commons.math.TestUtils;
import org.apache.commons.math.distribution.BetaDistribution;
import org.apache.commons.math.distribution.BinomialDistribution;
import org.apache.commons.math.distribution.BinomialDistributionTest;
import org.apache.commons.math.distribution.CauchyDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ExponentialDistribution;
import org.apache.commons.math.distribution.FDistribution;
import org.apache.commons.math.distribution.GammaDistribution;
import org.apache.commons.math.distribution.HypergeometricDistribution;
import org.apache.commons.math.distribution.HypergeometricDistributionTest;
import org.apache.commons.math.distribution.PascalDistribution;
import org.apache.commons.math.distribution.PascalDistributionTest;
import org.apache.commons.math.distribution.PoissonDistribution;
import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.distribution.WeibullDistribution;
import org.apache.commons.math.distribution.ZipfDistribution;
import org.apache.commons.math.distribution.ZipfDistributionTest;
import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.apache.commons.math.stat.inference.ChiSquareTest;
import org.apache.commons.math.stat.inference.ChiSquareTestImpl;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test cases for the RandomData class.
 *
 * @version $Id$
 *          2009) $
 */

@RunWith(RetryRunner.class)
public class RandomDataTest {

    public RandomDataTest() {
        randomData = new RandomDataImpl();
        randomData.reSeed(1000);
    }

    protected final long smallSampleSize = 1000;
    protected final double[] expected = { 250, 250, 250, 250 };
    protected final int largeSampleSize = 10000;
    private final String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f" };
    protected RandomDataImpl randomData = null;
    protected final ChiSquareTestImpl testStatistic = new ChiSquareTestImpl();

    @Test
    public void testNextIntExtremeValues() {
        int x = randomData.nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
        int y = randomData.nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
        Assert.assertFalse(x == y);
    }

    @Test
    public void testNextLongExtremeValues() {
        long x = randomData.nextLong(Long.MIN_VALUE, Long.MAX_VALUE);
        long y = randomData.nextLong(Long.MIN_VALUE, Long.MAX_VALUE);
        Assert.assertFalse(x == y);
    }
    
    @Test
    public void testNextUniformExtremeValues() {
        double x = randomData.nextUniform(-Double.MAX_VALUE, Double.MAX_VALUE);
        double y = randomData.nextUniform(-Double.MAX_VALUE, Double.MAX_VALUE);
        Assert.assertFalse(x == y);
        Assert.assertFalse(Double.isNaN(x));
        Assert.assertFalse(Double.isNaN(y));
        Assert.assertFalse(Double.isInfinite(x));
        Assert.assertFalse(Double.isInfinite(y));
    }
    
    @Test
    public void testNextIntIAE() throws Exception {
        try {
            randomData.nextInt(4, 3);
            Assert.fail("MathIllegalArgumentException expected");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
    }
    
    @Test
    public void testNextIntNegativeToPositiveRange() throws Exception {
        for (int i = 0; i < 5; i++) {
            checkNextIntUniform(-3, 5);
            checkNextIntUniform(-3, 6);
        }
    }
    
    @Test 
    public void testNextIntNegativeRange() throws Exception {
        for (int i = 0; i < 5; i++) {
            checkNextIntUniform(-7, -4);
            checkNextIntUniform(-15, -2);
        }
    }
    
    @Test 
    public void testNextIntPositiveRange() throws Exception {
        for (int i = 0; i < 5; i++) {
            checkNextIntUniform(0, 3);
            checkNextIntUniform(2, 12);
            checkNextIntUniform(1,2);
        }
    }
    
    
    private void checkNextIntUniform(int min, int max) throws Exception {
        final Frequency freq = new Frequency();
        for (int i = 0; i < smallSampleSize; i++) {
            final int value = randomData.nextInt(min, max);
            Assert.assertTrue("nextInt range", (value >= min) && (value <= max));
            freq.addValue(value);
        }
        final int len = max - min + 1;
        final long[] observed = new long[len];
        for (int i = 0; i < len; i++) {
            observed[i] = freq.getCount(min + i);
        }
        final double[] expected = new double[len];
        for (int i = 0; i < len; i++) {
            expected[i] = 1d / len;
        }
        
        TestUtils.assertChiSquareAccept(expected, observed, 0.01);
    }

    @Test
    public void testNextLongIAE() {
        try {
            randomData.nextLong(4, 3);
            Assert.fail("MathIllegalArgumentException expected");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
    }
    
    @Test
    public void testNextLongNegativeToPositiveRange() throws Exception {
        for (int i = 0; i < 5; i++) {
            checkNextLongUniform(-3, 5);
            checkNextLongUniform(-3, 6);
        }
    }
    
    @Test 
    public void testNextLongNegativeRange() throws Exception {
        for (int i = 0; i < 5; i++) {
            checkNextLongUniform(-7, -4);
            checkNextLongUniform(-15, -2);
        }
    }
    
    @Test 
    public void testNextLongPositiveRange() throws Exception {
        for (int i = 0; i < 5; i++) {
            checkNextLongUniform(0, 3);
            checkNextLongUniform(2, 12);
        }
    }
    
    private void checkNextLongUniform(int min, int max) throws Exception {
        final Frequency freq = new Frequency();
        for (int i = 0; i < smallSampleSize; i++) {
            final long value = randomData.nextLong(min, max);
            Assert.assertTrue("nextLong range", (value >= min) && (value <= max));
            freq.addValue(value);
        }
        final int len = max - min + 1;
        final long[] observed = new long[len];
        for (int i = 0; i < len; i++) {
            observed[i] = freq.getCount(min + i);
        }
        final double[] expected = new double[len];
        for (int i = 0; i < len; i++) {
            expected[i] = 1d / len;
        }
        
        TestUtils.assertChiSquareAccept(expected, observed, 0.01);
    }

    @Test
    public void testNextSecureLongIAE() {
        try {
            randomData.nextSecureLong(4, 3);
            Assert.fail("MathIllegalArgumentException expected");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
    }
    @Test
    public void testNextSecureLongNegativeToPositiveRange() throws Exception {
        for (int i = 0; i < 5; i++) {
            checkNextSecureLongUniform(-3, 5);
            checkNextSecureLongUniform(-3, 6);
        }
    }
    
    @Test 
    public void testNextSecureLongNegativeRange() throws Exception {
        for (int i = 0; i < 5; i++) {
            checkNextSecureLongUniform(-7, -4);
            checkNextSecureLongUniform(-15, -2);
        }
    }
    
    @Test 
    public void testNextSecureLongPositiveRange() throws Exception {
        for (int i = 0; i < 5; i++) {
            checkNextSecureLongUniform(0, 3);
            checkNextSecureLongUniform(2, 12);
        }
    }
    
    private void checkNextSecureLongUniform(int min, int max) throws Exception {
        final Frequency freq = new Frequency();
        for (int i = 0; i < smallSampleSize; i++) {
            final long value = randomData.nextSecureLong(min, max);
            Assert.assertTrue("nextLong range", (value >= min) && (value <= max));
            freq.addValue(value);
        }
        final int len = max - min + 1;
        final long[] observed = new long[len];
        for (int i = 0; i < len; i++) {
            observed[i] = freq.getCount(min + i);
        }
        final double[] expected = new double[len];
        for (int i = 0; i < len; i++) {
            expected[i] = 1d / len;
        }
        
        TestUtils.assertChiSquareAccept(expected, observed, 0.0001);
    }

    @Test
    public void testNextSecureIntIAE() {
        try {
            randomData.nextSecureInt(4, 3);
            Assert.fail("MathIllegalArgumentException expected");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
    }
    
    @Test
    public void testNextSecureIntNegativeToPositiveRange() throws Exception {
        for (int i = 0; i < 5; i++) {
            checkNextSecureIntUniform(-3, 5);
            checkNextSecureIntUniform(-3, 6);
        }
    }
    
    @Test 
    public void testNextSecureIntNegativeRange() throws Exception {
        for (int i = 0; i < 5; i++) {
            checkNextSecureIntUniform(-7, -4);
            checkNextSecureIntUniform(-15, -2);
        }
    }
    
    @Test 
    public void testNextSecureIntPositiveRange() throws Exception {
        for (int i = 0; i < 5; i++) {
            checkNextSecureIntUniform(0, 3);
            checkNextSecureIntUniform(2, 12);
        }
    }
     
    private void checkNextSecureIntUniform(int min, int max) throws Exception {
        final Frequency freq = new Frequency();
        for (int i = 0; i < smallSampleSize; i++) {
            final int value = randomData.nextSecureInt(min, max);
            Assert.assertTrue("nextInt range", (value >= min) && (value <= max));
            freq.addValue(value);
        }
        final int len = max - min + 1;
        final long[] observed = new long[len];
        for (int i = 0; i < len; i++) {
            observed[i] = freq.getCount(min + i);
        }
        final double[] expected = new double[len];
        for (int i = 0; i < len; i++) {
            expected[i] = 1d / len;
        }
        
        TestUtils.assertChiSquareAccept(expected, observed, 0.0001);
    }
    
    

    /**
     * Make sure that empirical distribution of random Poisson(4)'s has P(X <=
     * 5) close to actual cumulative Poisson probability and that nextPoisson
     * fails when mean is non-positive TODO: replace with statistical test,
     * adding test stat to TestStatistic
     */
    @Test
    public void testNextPoisson() {
        try {
            randomData.nextPoisson(0);
            Assert.fail("zero mean -- expecting MathIllegalArgumentException");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
        Frequency f = new Frequency();
        for (int i = 0; i < largeSampleSize; i++) {
            f.addValue(randomData.nextPoisson(4.0d));
        }
        long cumFreq = f.getCount(0) + f.getCount(1) + f.getCount(2)
                + f.getCount(3) + f.getCount(4) + f.getCount(5);
        long sumFreq = f.getSumFreq();
        double cumPct = Double.valueOf(cumFreq).doubleValue()
                / Double.valueOf(sumFreq).doubleValue();
        Assert.assertEquals("cum Poisson(4)", cumPct, 0.7851, 0.2);
        try {
            randomData.nextPoisson(-1);
            Assert.fail("negative mean supplied -- MathIllegalArgumentException expected");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
        try {
            randomData.nextPoisson(0);
            Assert.fail("0 mean supplied -- MathIllegalArgumentException expected");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }

    }

    @Test
    public void testNextPoissonConsistency() throws Exception {

        // Small integral means
        for (int i = 1; i < 100; i++) {
            checkNextPoissonConsistency(i);
        }
        // non-integer means
        for (int i = 1; i < 10; i++) {
            checkNextPoissonConsistency(randomData.nextUniform(1, 1000));
        }
        // large means
        // TODO: When MATH-282 is resolved, s/3000/10000 below
        for (int i = 1; i < 10; i++) {
            checkNextPoissonConsistency(randomData.nextUniform(1000, 3000));
        }
    }

    /**
     * Verifies that nextPoisson(mean) generates an empirical distribution of values
     * consistent with PoissonDistributionImpl by generating 1000 values, computing a
     * grouped frequency distribution of the observed values and comparing this distribution
     * to the corresponding expected distribution computed using PoissonDistributionImpl.
     * Uses ChiSquare test of goodness of fit to evaluate the null hypothesis that the
     * distributions are the same. If the null hypothesis can be rejected with confidence
     * 1 - alpha, the check fails.
     */
    public void checkNextPoissonConsistency(double mean) throws Exception {
        // Generate sample values
        final int sampleSize = 1000;        // Number of deviates to generate
        final int minExpectedCount = 7;     // Minimum size of expected bin count
        long maxObservedValue = 0;
        final double alpha = 0.001;         // Probability of false failure
        Frequency frequency = new Frequency();
        for (int i = 0; i < sampleSize; i++) {
            long value = randomData.nextPoisson(mean);
            if (value > maxObservedValue) {
                maxObservedValue = value;
            }
            frequency.addValue(value);
        }

        /*
         *  Set up bins for chi-square test.
         *  Ensure expected counts are all at least minExpectedCount.
         *  Start with upper and lower tail bins.
         *  Lower bin = [0, lower); Upper bin = [upper, +inf).
         */
        PoissonDistribution poissonDistribution = new PoissonDistribution(mean);
        int lower = 1;
        while (poissonDistribution.cumulativeProbability(lower - 1) * sampleSize < minExpectedCount) {
            lower++;
        }
        int upper = (int) (5 * mean);  // Even for mean = 1, not much mass beyond 5
        while ((1 - poissonDistribution.cumulativeProbability(upper - 1)) * sampleSize < minExpectedCount) {
            upper--;
        }

        // Set bin width for interior bins.  For poisson, only need to look at end bins.
        int binWidth = 1;
        boolean widthSufficient = false;
        double lowerBinMass = 0;
        double upperBinMass = 0;
        while (!widthSufficient) {
            lowerBinMass = poissonDistribution.cumulativeProbability(lower, lower + binWidth - 1);
            upperBinMass = poissonDistribution.cumulativeProbability(upper - binWidth + 1, upper);
            widthSufficient = FastMath.min(lowerBinMass, upperBinMass) * sampleSize >= minExpectedCount;
            binWidth++;
        }

        /*
         *  Determine interior bin bounds.  Bins are
         *  [1, lower = binBounds[0]), [lower, binBounds[1]), [binBounds[1], binBounds[2]), ... ,
         *    [binBounds[binCount - 2], upper = binBounds[binCount - 1]), [upper, +inf)
         *
         */
        List<Integer> binBounds = new ArrayList<Integer>();
        binBounds.add(lower);
        int bound = lower + binWidth;
        while (bound < upper - binWidth) {
            binBounds.add(bound);
            bound += binWidth;
        }
        binBounds.add(bound);
        binBounds.add(upper);

        // Compute observed and expected bin counts
        final int binCount = binBounds.size() + 1;
        long[] observed = new long[binCount];
        double[] expected = new double[binCount];

        // Bottom bin
        observed[0] = 0;
        for (int i = 0; i < lower; i++) {
            observed[0] += frequency.getCount(i);
        }
        expected[0] = poissonDistribution.cumulativeProbability(lower - 1) * sampleSize;

        // Top bin
        observed[binCount - 1] = 0;
        for (int i = upper; i <= maxObservedValue; i++) {
            observed[binCount - 1] += frequency.getCount(i);
        }
        expected[binCount - 1] = (1 - poissonDistribution.cumulativeProbability(upper - 1)) * sampleSize;

        // Interior bins
        for (int i = 1; i < binCount - 1; i++) {
            observed[i] = 0;
            for (int j = binBounds.get(i - 1); j < binBounds.get(i); j++) {
                observed[i] += frequency.getCount(j);
            } // Expected count is (mass in [binBounds[i], binBounds[i+1])) * sampleSize
            expected[i] = (poissonDistribution.cumulativeProbability(binBounds.get(i) - 1) -
                poissonDistribution.cumulativeProbability(binBounds.get(i - 1) -1)) * sampleSize;
        }

        // Use chisquare test to verify that generated values are poisson(mean)-distributed
        ChiSquareTest chiSquareTest = new ChiSquareTestImpl();
            // Fail if we can reject null hypothesis that distributions are the same
        if (chiSquareTest.chiSquareTest(expected, observed, alpha)) {
            StringBuilder msgBuffer = new StringBuilder();
            DecimalFormat df = new DecimalFormat("#.##");
            msgBuffer.append("Chisquare test failed for mean = ");
            msgBuffer.append(mean);
            msgBuffer.append(" p-value = ");
            msgBuffer.append(chiSquareTest.chiSquareTest(expected, observed));
            msgBuffer.append(" chisquare statistic = ");
            msgBuffer.append(chiSquareTest.chiSquare(expected, observed));
            msgBuffer.append(". \n");
            msgBuffer.append("bin\t\texpected\tobserved\n");
            for (int i = 0; i < expected.length; i++) {
                msgBuffer.append("[");
                msgBuffer.append(i == 0 ? 1: binBounds.get(i - 1));
                msgBuffer.append(",");
                msgBuffer.append(i == binBounds.size() ? "inf": binBounds.get(i));
                msgBuffer.append(")");
                msgBuffer.append("\t\t");
                msgBuffer.append(df.format(expected[i]));
                msgBuffer.append("\t\t");
                msgBuffer.append(observed[i]);
                msgBuffer.append("\n");
            }
            msgBuffer.append("This test can fail randomly due to sampling error with probability ");
            msgBuffer.append(alpha);
            msgBuffer.append(".");
            Assert.fail(msgBuffer.toString());
        }
    }

    /** test dispersion and failure modes for nextHex() */
    @Test
    @Retry(3)
    public void testNextHex() {
        try {
            randomData.nextHexString(-1);
            Assert.fail("negative length supplied -- MathIllegalArgumentException expected");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
        try {
            randomData.nextHexString(0);
            Assert.fail("zero length supplied -- MathIllegalArgumentException expected");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
        String hexString = randomData.nextHexString(3);
        if (hexString.length() != 3) {
            Assert.fail("incorrect length for generated string");
        }
        hexString = randomData.nextHexString(1);
        if (hexString.length() != 1) {
            Assert.fail("incorrect length for generated string");
        }
        try {
            hexString = randomData.nextHexString(0);
            Assert.fail("zero length requested -- expecting MathIllegalArgumentException");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
        if (hexString.length() != 1) {
            Assert.fail("incorrect length for generated string");
        }
        Frequency f = new Frequency();
        for (int i = 0; i < smallSampleSize; i++) {
            hexString = randomData.nextHexString(100);
            if (hexString.length() != 100) {
                Assert.fail("incorrect length for generated string");
            }
            for (int j = 0; j < hexString.length(); j++) {
                f.addValue(hexString.substring(j, j + 1));
            }
        }
        double[] expected = new double[16];
        long[] observed = new long[16];
        for (int i = 0; i < 16; i++) {
            expected[i] = (double) smallSampleSize * 100 / 16;
            observed[i] = f.getCount(hex[i]);
        }
        /*
         * Use ChiSquare dist with df = 16-1 = 15, alpha = .001 Change to 30.58
         * for alpha = .01
         */
        Assert.assertTrue("chi-square test -- will fail about 1 in 1000 times",
                testStatistic.chiSquare(expected, observed) < 37.70);
    }

    /** test dispersion and failure modes for nextHex() */
    @Test
    public void testNextSecureHex() {
        try {
            randomData.nextSecureHexString(-1);
            Assert.fail("negative length -- MathIllegalArgumentException expected");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
        try {
            randomData.nextSecureHexString(0);
            Assert.fail("zero length -- MathIllegalArgumentException expected");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
        String hexString = randomData.nextSecureHexString(3);
        if (hexString.length() != 3) {
            Assert.fail("incorrect length for generated string");
        }
        hexString = randomData.nextSecureHexString(1);
        if (hexString.length() != 1) {
            Assert.fail("incorrect length for generated string");
        }
        try {
            hexString = randomData.nextSecureHexString(0);
            Assert.fail("zero length requested -- expecting MathIllegalArgumentException");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
        if (hexString.length() != 1) {
            Assert.fail("incorrect length for generated string");
        }
        Frequency f = new Frequency();
        for (int i = 0; i < smallSampleSize; i++) {
            hexString = randomData.nextSecureHexString(100);
            if (hexString.length() != 100) {
                Assert.fail("incorrect length for generated string");
            }
            for (int j = 0; j < hexString.length(); j++) {
                f.addValue(hexString.substring(j, j + 1));
            }
        }
        double[] expected = new double[16];
        long[] observed = new long[16];
        for (int i = 0; i < 16; i++) {
            expected[i] = (double) smallSampleSize * 100 / 16;
            observed[i] = f.getCount(hex[i]);
        }
        /*
         * Use ChiSquare dist with df = 16-1 = 15, alpha = .001 Change to 30.58
         * for alpha = .01
         */
        Assert.assertTrue("chi-square test -- will fail about 1 in 1000 times",
                testStatistic.chiSquare(expected, observed) < 37.70);
    }

    @Test
    public void testNextUniformIAE() {
        try {
            randomData.nextUniform(4, 3);
            Assert.fail("MathIllegalArgumentException expected");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
        try {
            randomData.nextUniform(0, Double.POSITIVE_INFINITY);
            Assert.fail("MathIllegalArgumentException expected");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
        try {
            randomData.nextUniform(Double.NEGATIVE_INFINITY, 0);
            Assert.fail("MathIllegalArgumentException expected");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
        try {
            randomData.nextUniform(0, Double.NaN);
            Assert.fail("MathIllegalArgumentException expected");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
        try {
            randomData.nextUniform(Double.NaN, 0);
            Assert.fail("MathIllegalArgumentException expected");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
    }
    
    @Test
    public void testNextUniformUniformPositiveBounds() throws Exception {
        for (int i = 0; i < 5; i++) {
            checkNextUniformUniform(0, 10);
        }
    }
    
    @Test
    public void testNextUniformUniformNegativeToPositiveBounds() throws Exception {
        for (int i = 0; i < 5; i++) {
            checkNextUniformUniform(-3, 5);
        }
    }
    
    @Test
    public void testNextUniformUniformNegaiveBounds() throws Exception {
        for (int i = 0; i < 5; i++) {
            checkNextUniformUniform(-7, -3);
        }
    }
    
    @Test
    public void testNextUniformUniformMaximalInterval() throws Exception {
        for (int i = 0; i < 5; i++) {
            checkNextUniformUniform(-Double.MAX_VALUE, Double.MAX_VALUE);
        }
    }
    
    private void checkNextUniformUniform(double min, double max) throws Exception {
        // Set up bin bounds - min, binBound[0], ..., binBound[binCount-2], max
        final int binCount = 5;
        final double binSize = max / binCount - min/binCount; // Prevent overflow in extreme value case
        final double[] binBounds = new double[binCount - 1];
        binBounds[0] = min + binSize;
        for (int i = 1; i < binCount - 1; i++) {
            binBounds[i] = binBounds[i - 1] + binSize;  // + instead of * to avoid overflow in extreme case
        }
        
        final Frequency freq = new Frequency();
        for (int i = 0; i < smallSampleSize; i++) {
            final double value = randomData.nextUniform(min, max);
            Assert.assertTrue("nextUniform range", (value > min) && (value < max));
            // Find bin
            int j = 0;
            while (j < binCount - 1 && value > binBounds[j]) {
                j++;
            }
            freq.addValue(j);
        }
       
        final long[] observed = new long[binCount];
        for (int i = 0; i < binCount; i++) {
            observed[i] = freq.getCount(i);
        }
        final double[] expected = new double[binCount];
        for (int i = 0; i < binCount; i++) {
            expected[i] = 1d / binCount;
        }
        
        TestUtils.assertChiSquareAccept(expected, observed, 0.01);
    }

    /** test exclusive endpoints of nextUniform **/
    @Test
    public void testNextUniformExclusiveEndpoints() {
        for (int i = 0; i < 1000; i++) {
            double u = randomData.nextUniform(0.99, 1);
            Assert.assertTrue(u > 0.99 && u < 1);
        }
    }

    /** test failure modes and distribution of nextGaussian() */
    @Test
    public void testNextGaussian() {
        try {
            randomData.nextGaussian(0, 0);
            Assert.fail("zero sigma -- MathIllegalArgumentException expected");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
        SummaryStatistics u = new SummaryStatistics();
        for (int i = 0; i < largeSampleSize; i++) {
            u.addValue(randomData.nextGaussian(0, 1));
        }
        double xbar = u.getMean();
        double s = u.getStandardDeviation();
        double n = u.getN();
        /*
         * t-test at .001-level TODO: replace with externalized t-test, with
         * test statistic defined in TestStatistic
         */
        Assert.assertTrue(FastMath.abs(xbar) / (s / FastMath.sqrt(n)) < 3.29);
    }

    /** test failure modes and distribution of nextExponential() */
    @Test
    public void testNextExponential() throws Exception {
        try {
            randomData.nextExponential(-1);
            Assert.fail("negative mean -- expecting MathIllegalArgumentException");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
        try {
            randomData.nextExponential(0);
            Assert.fail("zero mean -- expecting MathIllegalArgumentException");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
        long cumFreq = 0;
        double v = 0;
        for (int i = 0; i < largeSampleSize; i++) {
            v = randomData.nextExponential(1);
            Assert.assertTrue("exponential deviate postive", v > 0);
            if (v < 2)
                cumFreq++;
        }
        /*
         * TODO: Replace with a statistical test, with statistic added to
         * TestStatistic. Check below compares observed cumulative distribution
         * evaluated at 2 with exponential CDF
         */
        Assert.assertEquals("exponential cumulative distribution", (double) cumFreq
                / (double) largeSampleSize, 0.8646647167633873, .2);

        /**
         * Proposal on improving the test of generating exponentials
         */
        double[] quartiles;
        long[] counts;

        // Mean 1
        quartiles = TestUtils.getDistributionQuartiles(new ExponentialDistribution(1));
        counts = new long[4];
        randomData.reSeed(1000);
        for (int i = 0; i < 1000; i++) {
            double value = randomData.nextExponential(1);
            TestUtils.updateCounts(value, counts, quartiles);
        }
        TestUtils.assertChiSquareAccept(expected, counts, 0.001);

        // Mean 5
        quartiles = TestUtils.getDistributionQuartiles(new ExponentialDistribution(5));
        counts = new long[4];
        randomData.reSeed(1000);
        for (int i = 0; i < 1000; i++) {
            double value = randomData.nextExponential(5);
            TestUtils.updateCounts(value, counts, quartiles);
        }
        TestUtils.assertChiSquareAccept(expected, counts, 0.001);
    }

    /** test reseeding, algorithm/provider games */
    @Test
    public void testConfig() {
        randomData.reSeed(1000);
        double v = randomData.nextUniform(0, 1);
        randomData.reSeed();
        Assert.assertTrue("different seeds", Math
                .abs(v - randomData.nextUniform(0, 1)) > 10E-12);
        randomData.reSeed(1000);
        Assert.assertEquals("same seeds", v, randomData.nextUniform(0, 1), 10E-12);
        randomData.reSeedSecure(1000);
        String hex = randomData.nextSecureHexString(40);
        randomData.reSeedSecure();
        Assert.assertTrue("different seeds", !hex.equals(randomData
                .nextSecureHexString(40)));
        randomData.reSeedSecure(1000);
        Assert.assertTrue("same seeds", !hex
                .equals(randomData.nextSecureHexString(40)));

        /*
         * remove this test back soon, since it takes about 4 seconds
         *
         * try { randomData.setSecureAlgorithm("SHA1PRNG","SUN"); } catch
         * (NoSuchProviderException ex) { ; } Assert.assertTrue("different seeds",
         * !hex.equals(randomData.nextSecureHexString(40))); try {
         * randomData.setSecureAlgorithm("NOSUCHTHING","SUN");
         * Assert.fail("expecting NoSuchAlgorithmException"); } catch
         * (NoSuchProviderException ex) { ; } catch (NoSuchAlgorithmException
         * ex) { ; }
         *
         * try { randomData.setSecureAlgorithm("SHA1PRNG","NOSUCHPROVIDER");
         * Assert.fail("expecting NoSuchProviderException"); } catch
         * (NoSuchProviderException ex) { ; }
         */

        // test reseeding without first using the generators
        RandomDataImpl rd = new RandomDataImpl();
        rd.reSeed(100);
        rd.nextLong(1, 2);
        RandomDataImpl rd2 = new RandomDataImpl();
        rd2.reSeedSecure(2000);
        rd2.nextSecureLong(1, 2);
        rd = new RandomDataImpl();
        rd.reSeed();
        rd.nextLong(1, 2);
        rd2 = new RandomDataImpl();
        rd2.reSeedSecure();
        rd2.nextSecureLong(1, 2);
    }

    /** tests for nextSample() sampling from Collection */
    @Test
    public void testNextSample() {
        Object[][] c = { { "0", "1" }, { "0", "2" }, { "0", "3" },
                { "0", "4" }, { "1", "2" }, { "1", "3" }, { "1", "4" },
                { "2", "3" }, { "2", "4" }, { "3", "4" } };
        long[] observed = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        double[] expected = { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 };

        HashSet<Object> cPop = new HashSet<Object>(); // {0,1,2,3,4}
        for (int i = 0; i < 5; i++) {
            cPop.add(Integer.toString(i));
        }

        Object[] sets = new Object[10]; // 2-sets from 5
        for (int i = 0; i < 10; i++) {
            HashSet<Object> hs = new HashSet<Object>();
            hs.add(c[i][0]);
            hs.add(c[i][1]);
            sets[i] = hs;
        }

        for (int i = 0; i < 1000; i++) {
            Object[] cSamp = randomData.nextSample(cPop, 2);
            observed[findSample(sets, cSamp)]++;
        }

        /*
         * Use ChiSquare dist with df = 10-1 = 9, alpha = .001 Change to 21.67
         * for alpha = .01
         */
        Assert.assertTrue("chi-square test -- will fail about 1 in 1000 times",
                testStatistic.chiSquare(expected, observed) < 27.88);

        // Make sure sample of size = size of collection returns same collection
        HashSet<Object> hs = new HashSet<Object>();
        hs.add("one");
        Object[] one = randomData.nextSample(hs, 1);
        String oneString = (String) one[0];
        if ((one.length != 1) || !oneString.equals("one")) {
            Assert.fail("bad sample for set size = 1, sample size = 1");
        }

        // Make sure we fail for sample size > collection size
        try {
            one = randomData.nextSample(hs, 2);
            Assert.fail("sample size > set size, expecting MathIllegalArgumentException");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }

        // Make sure we fail for empty collection
        try {
            hs = new HashSet<Object>();
            one = randomData.nextSample(hs, 0);
            Assert.fail("n = k = 0, expecting MathIllegalArgumentException");
        } catch (MathIllegalArgumentException ex) {
            // ignored
        }
    }

    @SuppressWarnings("unchecked")
    private int findSample(Object[] u, Object[] samp) {
        for (int i = 0; i < u.length; i++) {
            HashSet<Object> set = (HashSet<Object>) u[i];
            HashSet<Object> sampSet = new HashSet<Object>();
            for (int j = 0; j < samp.length; j++) {
                sampSet.add(samp[j]);
            }
            if (set.equals(sampSet)) {
                return i;
            }
        }
        Assert.fail("sample not found:{" + samp[0] + "," + samp[1] + "}");
        return -1;
    }

    /** tests for nextPermutation */
    @Test
    public void testNextPermutation() {
        int[][] p = { { 0, 1, 2 }, { 0, 2, 1 }, { 1, 0, 2 }, { 1, 2, 0 },
                { 2, 0, 1 }, { 2, 1, 0 } };
        long[] observed = { 0, 0, 0, 0, 0, 0 };
        double[] expected = { 100, 100, 100, 100, 100, 100 };

        for (int i = 0; i < 600; i++) {
            int[] perm = randomData.nextPermutation(3, 3);
            observed[findPerm(p, perm)]++;
        }

        /*
         * Use ChiSquare dist with df = 6-1 = 5, alpha = .001 Change to 15.09
         * for alpha = .01
         */
        Assert.assertTrue("chi-square test -- will fail about 1 in 1000 times",
                testStatistic.chiSquare(expected, observed) < 20.52);

        // Check size = 1 boundary case
        int[] perm = randomData.nextPermutation(1, 1);
        if ((perm.length != 1) || (perm[0] != 0)) {
            Assert.fail("bad permutation for n = 1, sample k = 1");

            // Make sure we fail for k size > n
            try {
                perm = randomData.nextPermutation(2, 3);
                Assert.fail("permutation k > n, expecting MathIllegalArgumentException");
            } catch (MathIllegalArgumentException ex) {
                // ignored
            }

            // Make sure we fail for n = 0
            try {
                perm = randomData.nextPermutation(0, 0);
                Assert.fail("permutation k = n = 0, expecting MathIllegalArgumentException");
            } catch (MathIllegalArgumentException ex) {
                // ignored
            }

            // Make sure we fail for k < n < 0
            try {
                perm = randomData.nextPermutation(-1, -3);
                Assert.fail("permutation k < n < 0, expecting MathIllegalArgumentException");
            } catch (MathIllegalArgumentException ex) {
                // ignored
            }

        }
    }

    // Disable until we have equals
    //public void testSerial() {
    //    Assert.assertEquals(randomData, TestUtils.serializeAndRecover(randomData));
    //}

    private int findPerm(int[][] p, int[] samp) {
        for (int i = 0; i < p.length; i++) {
            boolean good = true;
            for (int j = 0; j < samp.length; j++) {
                if (samp[j] != p[i][j]) {
                    good = false;
                }
            }
            if (good) {
                return i;
            }
        }
        Assert.fail("permutation not found");
        return -1;
    }

    @Test
    public void testNextInversionDeviate() throws Exception {
        // Set the seed for the default random generator
        randomData.reSeed(100);
        double[] quantiles = new double[10];
        for (int i = 0; i < 10; i++) {
            quantiles[i] = randomData.nextUniform(0, 1);
        }
        // Reseed again so the inversion generator gets the same sequence
        randomData.reSeed(100);
        BetaDistribution betaDistribution = new BetaDistribution(2, 4);
        /*
         *  Generate a sequence of deviates using inversion - the distribution function
         *  evaluated at the random value from the distribution should match the uniform
         *  random value used to generate it, which is stored in the quantiles[] array.
         */
        for (int i = 0; i < 10; i++) {
            double value = randomData.nextInversionDeviate(betaDistribution);
            Assert.assertEquals(betaDistribution.cumulativeProbability(value), quantiles[i], 10E-9);
        }
    }

    @Test
    public void testNextBeta() throws Exception {
        double[] quartiles = TestUtils.getDistributionQuartiles(new BetaDistribution(2,5));
        long[] counts = new long[4];
        randomData.reSeed(1000);
        for (int i = 0; i < 1000; i++) {
            double value = randomData.nextBeta(2, 5);
            TestUtils.updateCounts(value, counts, quartiles);
        }
        TestUtils.assertChiSquareAccept(expected, counts, 0.001);
    }

    @Test
    public void testNextCauchy() throws Exception {
        double[] quartiles = TestUtils.getDistributionQuartiles(new CauchyDistribution(1.2, 2.1));
        long[] counts = new long[4];
        randomData.reSeed(1000);
        for (int i = 0; i < 1000; i++) {
            double value = randomData.nextCauchy(1.2, 2.1);
            TestUtils.updateCounts(value, counts, quartiles);
        }
        TestUtils.assertChiSquareAccept(expected, counts, 0.001);
    }

    @Test
    public void testNextChiSquare() throws Exception {
        double[] quartiles = TestUtils.getDistributionQuartiles(new ChiSquaredDistribution(12));
        long[] counts = new long[4];
        randomData.reSeed(1000);
        for (int i = 0; i < 1000; i++) {
            double value = randomData.nextChiSquare(12);
            TestUtils.updateCounts(value, counts, quartiles);
        }
        TestUtils.assertChiSquareAccept(expected, counts, 0.001);
    }

    @Test
    public void testNextF() throws Exception {
        double[] quartiles = TestUtils.getDistributionQuartiles(new FDistribution(12, 5));
        long[] counts = new long[4];
        randomData.reSeed(1000);
        for (int i = 0; i < 1000; i++) {
            double value = randomData.nextF(12, 5);
            TestUtils.updateCounts(value, counts, quartiles);
        }
        TestUtils.assertChiSquareAccept(expected, counts, 0.001);
    }

    @Test
    public void testNextGamma() throws Exception {
        double[] quartiles;
        long[] counts;

        // Tests shape > 1, one case in the rejection sampling
        quartiles = TestUtils.getDistributionQuartiles(new GammaDistribution(4, 2));
        counts = new long[4];
        randomData.reSeed(1000);
        for (int i = 0; i < 1000; i++) {
            double value = randomData.nextGamma(4, 2);
            TestUtils.updateCounts(value, counts, quartiles);
        }
        TestUtils.assertChiSquareAccept(expected, counts, 0.001);

        // Tests shape <= 1, another case in the rejection sampling
        quartiles = TestUtils.getDistributionQuartiles(new GammaDistribution(0.3, 3));
        counts = new long[4];
        randomData.reSeed(1000);
        for (int i = 0; i < 1000; i++) {
            double value = randomData.nextGamma(0.3, 3);
            TestUtils.updateCounts(value, counts, quartiles);
        }
        TestUtils.assertChiSquareAccept(expected, counts, 0.001);
    }

    @Test
    public void testNextT() throws Exception {
        double[] quartiles = TestUtils.getDistributionQuartiles(new TDistribution(10));
        long[] counts = new long[4];
        randomData.reSeed(1000);
        for (int i = 0; i < 1000; i++) {
            double value = randomData.nextT(10);
            TestUtils.updateCounts(value, counts, quartiles);
        }
        TestUtils.assertChiSquareAccept(expected, counts, 0.001);
    }

    @Test
    public void testNextWeibull() throws Exception {
        double[] quartiles = TestUtils.getDistributionQuartiles(new WeibullDistribution(1.2, 2.1));
        long[] counts = new long[4];
        randomData.reSeed(1000);
        for (int i = 0; i < 1000; i++) {
            double value = randomData.nextWeibull(1.2, 2.1);
            TestUtils.updateCounts(value, counts, quartiles);
        }
        TestUtils.assertChiSquareAccept(expected, counts, 0.001);
    }

    @Test
    public void testNextBinomial() throws Exception {
        BinomialDistributionTest testInstance = new BinomialDistributionTest();
        int[] densityPoints = testInstance.makeDensityTestPoints();
        double[] densityValues = testInstance.makeDensityTestValues();
        int sampleSize = 1000;
        int length = TestUtils.eliminateZeroMassPoints(densityPoints, densityValues);
        BinomialDistribution distribution = (BinomialDistribution) testInstance.makeDistribution();
        double[] expectedCounts = new double[length];
        long[] observedCounts = new long[length];
        for (int i = 0; i < length; i++) {
            expectedCounts[i] = sampleSize * densityValues[i];
        }
        randomData.reSeed(1000);
        for (int i = 0; i < sampleSize; i++) {
          int value = randomData.nextBinomial(distribution.getNumberOfTrials(),
                  distribution.getProbabilityOfSuccess());
          for (int j = 0; j < length; j++) {
              if (value == densityPoints[j]) {
                  observedCounts[j]++;
              }
          }
        }
        TestUtils.assertChiSquareAccept(densityPoints, expectedCounts, observedCounts, .001);
    }

    @Test
    public void testNextHypergeometric() throws Exception {
        HypergeometricDistributionTest testInstance = new HypergeometricDistributionTest();
        int[] densityPoints = testInstance.makeDensityTestPoints();
        double[] densityValues = testInstance.makeDensityTestValues();
        int sampleSize = 1000;
        int length = TestUtils.eliminateZeroMassPoints(densityPoints, densityValues);
        HypergeometricDistribution distribution = (HypergeometricDistribution) testInstance.makeDistribution();
        double[] expectedCounts = new double[length];
        long[] observedCounts = new long[length];
        for (int i = 0; i < length; i++) {
            expectedCounts[i] = sampleSize * densityValues[i];
        }
        randomData.reSeed(1000);
        for (int i = 0; i < sampleSize; i++) {
          int value = randomData.nextHypergeometric(distribution.getPopulationSize(),
                  distribution.getNumberOfSuccesses(), distribution.getSampleSize());
          for (int j = 0; j < length; j++) {
              if (value == densityPoints[j]) {
                  observedCounts[j]++;
              }
          }
        }
        TestUtils.assertChiSquareAccept(densityPoints, expectedCounts, observedCounts, .001);
    }

    @Test
    public void testNextPascal() throws Exception {
        PascalDistributionTest testInstance = new PascalDistributionTest();
        int[] densityPoints = testInstance.makeDensityTestPoints();
        double[] densityValues = testInstance.makeDensityTestValues();
        int sampleSize = 1000;
        int length = TestUtils.eliminateZeroMassPoints(densityPoints, densityValues);
        PascalDistribution distribution = (PascalDistribution) testInstance.makeDistribution();
        double[] expectedCounts = new double[length];
        long[] observedCounts = new long[length];
        for (int i = 0; i < length; i++) {
            expectedCounts[i] = sampleSize * densityValues[i];
        }
        randomData.reSeed(1000);
        for (int i = 0; i < sampleSize; i++) {
          int value = randomData.nextPascal(distribution.getNumberOfSuccesses(), distribution.getProbabilityOfSuccess());
          for (int j = 0; j < length; j++) {
              if (value == densityPoints[j]) {
                  observedCounts[j]++;
              }
          }
        }
        TestUtils.assertChiSquareAccept(densityPoints, expectedCounts, observedCounts, .001);
    }

    @Test
    public void testNextZipf() throws Exception {
        ZipfDistributionTest testInstance = new ZipfDistributionTest();
        int[] densityPoints = testInstance.makeDensityTestPoints();
        double[] densityValues = testInstance.makeDensityTestValues();
        int sampleSize = 1000;
        int length = TestUtils.eliminateZeroMassPoints(densityPoints, densityValues);
        ZipfDistribution distribution = (ZipfDistribution) testInstance.makeDistribution();
        double[] expectedCounts = new double[length];
        long[] observedCounts = new long[length];
        for (int i = 0; i < length; i++) {
            expectedCounts[i] = sampleSize * densityValues[i];
        }
        randomData.reSeed(1000);
        for (int i = 0; i < sampleSize; i++) {
          int value = randomData.nextZipf(distribution.getNumberOfElements(), distribution.getExponent());
          for (int j = 0; j < length; j++) {
              if (value == densityPoints[j]) {
                  observedCounts[j]++;
              }
          }
        }
        TestUtils.assertChiSquareAccept(densityPoints, expectedCounts, observedCounts, .001);
    }
    
    @Test
    /**
     * MATH-720
     */
    public void testReseed() {
        PoissonDistribution x = new PoissonDistribution(3.0);
        x.reseedRandomGenerator(0);
        final double u = x.sample();
        PoissonDistribution y = new PoissonDistribution(3.0);
        y.reseedRandomGenerator(0);
        Assert.assertEquals(u, y.sample(), 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6124.java