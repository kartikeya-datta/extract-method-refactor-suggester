error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2592.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2592.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2592.java
text:
```scala
d@@ouble t = Math.abs(homoscedasticT(m1, m2, v1, v2, n1, n2));

/*
 * Copyright 2004-2005 The Apache Software Foundation.
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
package org.apache.commons.math.stat.inference;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.DistributionFactory;
import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.stat.StatUtils;
import org.apache.commons.math.stat.descriptive.StatisticalSummary;

/**
 * Implements t-test statistics defined in the {@link TTest} interface.
 * <p>
 * Uses commons-math {@link org.apache.commons.math.distribution.TDistribution}
 * implementation to estimate exact p-values.
 *
 * @version $Revision$ $Date$
 */
public class TTestImpl implements TTest  {

    /** Cached DistributionFactory used to create TDistribution instances */
    private DistributionFactory distributionFactory = null;
    
    /**
     * Default constructor.
     */
    public TTestImpl() {
        super();
    }
    
    /**
     * Computes a paired, 2-sample t-statistic based on the data in the input 
     * arrays.  The t-statistic returned is equivalent to what would be returned by
     * computing the one-sample t-statistic {@link #t(double, double[])}, with
     * <code>mu = 0</code> and the sample array consisting of the (signed) 
     * differences between corresponding entries in <code>sample1</code> and 
     * <code>sample2.</code>
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The input arrays must have the same length and their common length
     * must be at least 2.
     * </li></ul>
     *
     * @param sample1 array of sample data values
     * @param sample2 array of sample data values
     * @return t statistic
     * @throws IllegalArgumentException if the precondition is not met
     * @throws MathException if the statistic can not be computed do to a
     *         convergence or other numerical error.
     */
    public double pairedT(double[] sample1, double[] sample2)
        throws IllegalArgumentException, MathException {
        if ((sample1 == null) || (sample2 == null ||
                Math.min(sample1.length, sample2.length) < 2)) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        double meanDifference = StatUtils.meanDifference(sample1, sample2);
        return t(meanDifference, 0,  
                StatUtils.varianceDifference(sample1, sample2, meanDifference),
                (double) sample1.length);
    }

     /**
     * Returns the <i>observed significance level</i>, or 
     * <i> p-value</i>, associated with a paired, two-sample, two-tailed t-test 
     * based on the data in the input arrays.
     * <p>
     * The number returned is the smallest significance level
     * at which one can reject the null hypothesis that the mean of the paired
     * differences is 0 in favor of the two-sided alternative that the mean paired 
     * difference is not equal to 0. For a one-sided test, divide the returned 
     * value by 2.
     * <p>
     * This test is equivalent to a one-sample t-test computed using
     * {@link #tTest(double, double[])} with <code>mu = 0</code> and the sample
     * array consisting of the signed differences between corresponding elements of 
     * <code>sample1</code> and <code>sample2.</code>
     * <p>
     * <strong>Usage Note:</strong><br>
     * The validity of the p-value depends on the assumptions of the parametric
     * t-test procedure, as discussed 
     * <a href="http://www.basic.nwu.edu/statguidefiles/ttest_unpaired_ass_viol.html">
     * here</a>
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The input array lengths must be the same and their common length must
     * be at least 2.
     * </li></ul>
     *
     * @param sample1 array of sample data values
     * @param sample2 array of sample data values
     * @return p-value for t-test
     * @throws IllegalArgumentException if the precondition is not met
     * @throws MathException if an error occurs computing the p-value
     */
    public double pairedTTest(double[] sample1, double[] sample2)
        throws IllegalArgumentException, MathException {
        double meanDifference = StatUtils.meanDifference(sample1, sample2);
        return tTest(meanDifference, 0, 
                StatUtils.varianceDifference(sample1, sample2, meanDifference), 
                (double) sample1.length);
    }

     /**
     * Performs a paired t-test evaluating the null hypothesis that the 
     * mean of the paired differences between <code>sample1</code> and
     * <code>sample2</code> is 0 in favor of the two-sided alternative that the 
     * mean paired difference is not equal to 0, with significance level 
     * <code>alpha</code>.
     * <p>
     * Returns <code>true</code> iff the null hypothesis can be rejected with 
     * confidence <code>1 - alpha</code>.  To perform a 1-sided test, use 
     * <code>alpha * 2</code>
     * <p>
     * <strong>Usage Note:</strong><br>
     * The validity of the test depends on the assumptions of the parametric
     * t-test procedure, as discussed 
     * <a href="http://www.basic.nwu.edu/statguidefiles/ttest_unpaired_ass_viol.html">
     * here</a>
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The input array lengths must be the same and their common length 
     * must be at least 2.
     * </li>
     * <li> <code> 0 < alpha < 0.5 </code>
     * </li></ul>
     *
     * @param sample1 array of sample data values
     * @param sample2 array of sample data values
     * @param alpha significance level of the test
     * @return true if the null hypothesis can be rejected with 
     * confidence 1 - alpha
     * @throws IllegalArgumentException if the preconditions are not met
     * @throws MathException if an error occurs performing the test
     */
    public boolean pairedTTest(double[] sample1, double[] sample2, double alpha)
        throws IllegalArgumentException, MathException {
        if ((alpha <= 0) || (alpha > 0.5)) {
            throw new IllegalArgumentException("bad significance level: " + alpha);
        }
        return (pairedTTest(sample1, sample2) < alpha);
    }

    /**
     * Computes a <a href="http://www.itl.nist.gov/div898/handbook/prc/section2/prc22.htm#formula"> 
     * t statistic </a> given observed values and a comparison constant.
     * <p>
     * This statistic can be used to perform a one sample t-test for the mean.
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The observed array length must be at least 2.
     * </li></ul>
     *
     * @param mu comparison constant
     * @param observed array of values
     * @return t statistic
     * @throws IllegalArgumentException if input array length is less than 2
     */
    public double t(double mu, double[] observed)
    throws IllegalArgumentException {
        if ((observed == null) || (observed.length < 2)) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        return t(StatUtils.mean(observed), mu, StatUtils.variance(observed),
                observed.length);
    }

    /**
     * Computes a <a href="http://www.itl.nist.gov/div898/handbook/prc/section2/prc22.htm#formula">
     * t statistic </a> to use in comparing the mean of the dataset described by 
     * <code>sampleStats</code> to <code>mu</code>.
     * <p>
     * This statistic can be used to perform a one sample t-test for the mean.
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li><code>observed.getN() > = 2</code>.
     * </li></ul>
     *
     * @param mu comparison constant
     * @param sampleStats DescriptiveStatistics holding sample summary statitstics
     * @return t statistic
     * @throws IllegalArgumentException if the precondition is not met
     */
    public double t(double mu, StatisticalSummary sampleStats)
    throws IllegalArgumentException {
        if ((sampleStats == null) || (sampleStats.getN() < 2)) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        return t(sampleStats.getMean(), mu, sampleStats.getVariance(),
                sampleStats.getN());
    }

    /**
     * Computes a 2-sample t statistic,  under the hypothesis of equal 
     * subpopulation variances.  To compute a t-statistic without the
     * equal variances hypothesis, use {@link #t(double[], double[])}.
     * <p>
     * This statistic can be used to perform a (homoscedastic) two-sample
     * t-test to compare sample means.   
     * <p>
     * The t-statisitc is
     * <p>
     * &nbsp;&nbsp;<code>  t = (m1 - m2) / (sqrt(1/n1 +1/n2) sqrt(var))</code>
     * <p>
     * where <strong><code>n1</code></strong> is the size of first sample; 
     * <strong><code> n2</code></strong> is the size of second sample; 
     * <strong><code> m1</code></strong> is the mean of first sample;  
     * <strong><code> m2</code></strong> is the mean of second sample</li>
     * </ul>
     * and <strong><code>var</code></strong> is the pooled variance estimate:
     * <p>
     * <code>var = sqrt(((n1 - 1)var1 + (n2 - 1)var2) / ((n1-1) + (n2-1)))</code>
     * <p> 
     * with <strong><code>var1<code></strong> the variance of the first sample and
     * <strong><code>var2</code></strong> the variance of the second sample.
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The observed array lengths must both be at least 2.
     * </li></ul>
     *
     * @param sample1 array of sample data values
     * @param sample2 array of sample data values
     * @return t statistic
     * @throws IllegalArgumentException if the precondition is not met
     */
    public double homoscedasticT(double[] sample1, double[] sample2)
    throws IllegalArgumentException {
        if ((sample1 == null) || (sample2 == null ||
                Math.min(sample1.length, sample2.length) < 2)) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        return homoscedasticT(StatUtils.mean(sample1), StatUtils.mean(sample2),
                StatUtils.variance(sample1), StatUtils.variance(sample2),
                (double) sample1.length, (double) sample2.length);
    }
    
    /**
     * Computes a 2-sample t statistic, without the hypothesis of equal
     * subpopulation variances.  To compute a t-statistic assuming equal
     * variances, use {@link #homoscedasticT(double[], double[])}.
     * <p>
     * This statistic can be used to perform a two-sample t-test to compare
     * sample means.
     * <p>
     * The t-statisitc is
     * <p>
     * &nbsp;&nbsp; <code>  t = (m1 - m2) / sqrt(var1/n1 + var2/n2)</code>
     * <p>
     *  where <strong><code>n1</code></strong> is the size of the first sample
     * <strong><code> n2</code></strong> is the size of the second sample; 
     * <strong><code> m1</code></strong> is the mean of the first sample;  
     * <strong><code> m2</code></strong> is the mean of the second sample;
     * <strong><code> var1</code></strong> is the variance of the first sample;
     * <strong><code> var2</code></strong> is the variance of the second sample;  
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The observed array lengths must both be at least 2.
     * </li></ul>
     *
     * @param sample1 array of sample data values
     * @param sample2 array of sample data values
     * @return t statistic
     * @throws IllegalArgumentException if the precondition is not met
     */
    public double t(double[] sample1, double[] sample2)
    throws IllegalArgumentException {
        if ((sample1 == null) || (sample2 == null ||
                Math.min(sample1.length, sample2.length) < 2)) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        return t(StatUtils.mean(sample1), StatUtils.mean(sample2),
                StatUtils.variance(sample1), StatUtils.variance(sample2),
                (double) sample1.length, (double) sample2.length);
    }

    /**
     * Computes a 2-sample t statistic </a>, comparing the means of the datasets
     * described by two {@link StatisticalSummary} instances, without the
     * assumption of equal subpopulation variances.  Use 
     * {@link #homoscedasticT(StatisticalSummary, StatisticalSummary)} to
     * compute a t-statistic under the equal variances assumption.
     * <p>
     * This statistic can be used to perform a two-sample t-test to compare
     * sample means.
     * <p>
      * The returned  t-statisitc is
     * <p>
     * &nbsp;&nbsp; <code>  t = (m1 - m2) / sqrt(var1/n1 + var2/n2)</code>
     * <p>
     * where <strong><code>n1</code></strong> is the size of the first sample; 
     * <strong><code> n2</code></strong> is the size of the second sample; 
     * <strong><code> m1</code></strong> is the mean of the first sample;  
     * <strong><code> m2</code></strong> is the mean of the second sample
     * <strong><code> var1</code></strong> is the variance of the first sample;  
     * <strong><code> var2</code></strong> is the variance of the second sample
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The datasets described by the two Univariates must each contain
     * at least 2 observations.
     * </li></ul>
     *
     * @param sampleStats1 StatisticalSummary describing data from the first sample
     * @param sampleStats2 StatisticalSummary describing data from the second sample
     * @return t statistic
     * @throws IllegalArgumentException if the precondition is not met
     */
    public double t(StatisticalSummary sampleStats1, 
            StatisticalSummary sampleStats2)
    throws IllegalArgumentException {
        if ((sampleStats1 == null) ||
                (sampleStats2 == null ||
                        Math.min(sampleStats1.getN(), sampleStats2.getN()) < 2)) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        return t(sampleStats1.getMean(), sampleStats2.getMean(), 
                sampleStats1.getVariance(), sampleStats2.getVariance(),
                (double) sampleStats1.getN(), (double) sampleStats2.getN());
    }
    
    /**
     * Computes a 2-sample t statistic, comparing the means of the datasets
     * described by two {@link StatisticalSummary} instances, under the
     * assumption of equal subpopulation variances.  To compute a t-statistic
     * without the equal variances assumption, use 
     * {@link #t(StatisticalSummary, StatisticalSummary)}.
     * <p>
     * This statistic can be used to perform a (homoscedastic) two-sample
     * t-test to compare sample means.
     * <p>
     * The t-statisitc returned is
     * <p>
     * &nbsp;&nbsp;<code>  t = (m1 - m2) / (sqrt(1/n1 +1/n2) sqrt(var))</code>
     * <p>
     * where <strong><code>n1</code></strong> is the size of first sample; 
     * <strong><code> n2</code></strong> is the size of second sample; 
     * <strong><code> m1</code></strong> is the mean of first sample;  
     * <strong><code> m2</code></strong> is the mean of second sample
     * and <strong><code>var</code></strong> is the pooled variance estimate:
     * <p>
     * <code>var = sqrt(((n1 - 1)var1 + (n2 - 1)var2) / ((n1-1) + (n2-1)))</code>
     * <p> 
     * with <strong><code>var1<code></strong> the variance of the first sample and
     * <strong><code>var2</code></strong> the variance of the second sample.
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The datasets described by the two Univariates must each contain
     * at least 2 observations.
     * </li></ul>
     *
     * @param sampleStats1 StatisticalSummary describing data from the first sample
     * @param sampleStats2 StatisticalSummary describing data from the second sample
     * @return t statistic
     * @throws IllegalArgumentException if the precondition is not met
     */
    public double homoscedasticT(StatisticalSummary sampleStats1, 
            StatisticalSummary sampleStats2)
    throws IllegalArgumentException {
        if ((sampleStats1 == null) ||
                (sampleStats2 == null ||
                        Math.min(sampleStats1.getN(), sampleStats2.getN()) < 2)) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        return homoscedasticT(sampleStats1.getMean(), sampleStats2.getMean(), 
                sampleStats1.getVariance(), sampleStats2.getVariance(), 
                (double) sampleStats1.getN(), (double) sampleStats2.getN());
    }

     /**
     * Returns the <i>observed significance level</i>, or 
     * <i>p-value</i>, associated with a one-sample, two-tailed t-test 
     * comparing the mean of the input array with the constant <code>mu</code>.
     * <p>
     * The number returned is the smallest significance level
     * at which one can reject the null hypothesis that the mean equals 
     * <code>mu</code> in favor of the two-sided alternative that the mean
     * is different from <code>mu</code>. For a one-sided test, divide the 
     * returned value by 2.
     * <p>
     * <strong>Usage Note:</strong><br>
     * The validity of the test depends on the assumptions of the parametric
     * t-test procedure, as discussed 
     * <a href="http://www.basic.nwu.edu/statguidefiles/ttest_unpaired_ass_viol.html">here</a>
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The observed array length must be at least 2.
     * </li></ul>
     *
     * @param mu constant value to compare sample mean against
     * @param sample array of sample data values
     * @return p-value
     * @throws IllegalArgumentException if the precondition is not met
     * @throws MathException if an error occurs computing the p-value
     */
    public double tTest(double mu, double[] sample)
    throws IllegalArgumentException, MathException {
        if ((sample == null) || (sample.length < 2)) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        return tTest( StatUtils.mean(sample), mu, StatUtils.variance(sample),
                sample.length);
    }

    /**
     * Performs a <a href="http://www.itl.nist.gov/div898/handbook/eda/section3/eda353.htm">
     * two-sided t-test</a> evaluating the null hypothesis that the mean of the population from
     * which <code>sample</code> is drawn equals <code>mu</code>.
     * <p>
     * Returns <code>true</code> iff the null hypothesis can be 
     * rejected with confidence <code>1 - alpha</code>.  To 
     * perform a 1-sided test, use <code>alpha * 2</code>
     * <p>
     * <strong>Examples:</strong><br><ol>
     * <li>To test the (2-sided) hypothesis <code>sample mean = mu </code> at
     * the 95% level, use <br><code>tTest(mu, sample, 0.05) </code>
     * </li>
     * <li>To test the (one-sided) hypothesis <code> sample mean < mu </code>
     * at the 99% level, first verify that the measured sample mean is less 
     * than <code>mu</code> and then use 
     * <br><code>tTest(mu, sample, 0.02) </code>
     * </li></ol>
     * <p>
     * <strong>Usage Note:</strong><br>
     * The validity of the test depends on the assumptions of the one-sample 
     * parametric t-test procedure, as discussed 
     * <a href="http://www.basic.nwu.edu/statguidefiles/sg_glos.html#one-sample">here</a>
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The observed array length must be at least 2.
     * </li></ul>
     *
     * @param mu constant value to compare sample mean against
     * @param sample array of sample data values
     * @param alpha significance level of the test
     * @return p-value
     * @throws IllegalArgumentException if the precondition is not met
     * @throws MathException if an error computing the p-value
     */
    public boolean tTest(double mu, double[] sample, double alpha)
    throws IllegalArgumentException, MathException {
        if ((alpha <= 0) || (alpha > 0.5)) {
            throw new IllegalArgumentException("bad significance level: " + alpha);
        }
        return (tTest(mu, sample) < alpha);
    }

    /**
     * Returns the <i>observed significance level</i>, or 
     * <i>p-value</i>, associated with a one-sample, two-tailed t-test 
     * comparing the mean of the dataset described by <code>sampleStats</code>
     * with the constant <code>mu</code>.
     * <p>
     * The number returned is the smallest significance level
     * at which one can reject the null hypothesis that the mean equals 
     * <code>mu</code> in favor of the two-sided alternative that the mean
     * is different from <code>mu</code>. For a one-sided test, divide the 
     * returned value by 2.
     * <p>
     * <strong>Usage Note:</strong><br>
     * The validity of the test depends on the assumptions of the parametric
     * t-test procedure, as discussed 
     * <a href="http://www.basic.nwu.edu/statguidefiles/ttest_unpaired_ass_viol.html">
     * here</a>
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The sample must contain at least 2 observations.
     * </li></ul>
     *
     * @param mu constant value to compare sample mean against
     * @param sampleStats StatisticalSummary describing sample data
     * @return p-value
     * @throws IllegalArgumentException if the precondition is not met
     * @throws MathException if an error occurs computing the p-value
     */
    public double tTest(double mu, StatisticalSummary sampleStats)
    throws IllegalArgumentException, MathException {
        if ((sampleStats == null) || (sampleStats.getN() < 2)) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        return tTest(sampleStats.getMean(), mu, sampleStats.getVariance(),
                sampleStats.getN());
    }

     /**
     * Performs a <a href="http://www.itl.nist.gov/div898/handbook/eda/section3/eda353.htm">
     * two-sided t-test</a> evaluating the null hypothesis that the mean of the
     * population from which the dataset described by <code>stats</code> is
     * drawn equals <code>mu</code>.
     * <p>
     * Returns <code>true</code> iff the null hypothesis can be rejected with
     * confidence <code>1 - alpha</code>.  To  perform a 1-sided test, use
     * <code>alpha * 2.</code>
     * <p>
     * <strong>Examples:</strong><br><ol>
     * <li>To test the (2-sided) hypothesis <code>sample mean = mu </code> at
     * the 95% level, use <br><code>tTest(mu, sampleStats, 0.05) </code>
     * </li>
     * <li>To test the (one-sided) hypothesis <code> sample mean < mu </code>
     * at the 99% level, first verify that the measured sample mean is less 
     * than <code>mu</code> and then use 
     * <br><code>tTest(mu, sampleStats, 0.02) </code>
     * </li></ol>
     * <p>
     * <strong>Usage Note:</strong><br>
     * The validity of the test depends on the assumptions of the one-sample 
     * parametric t-test procedure, as discussed 
     * <a href="http://www.basic.nwu.edu/statguidefiles/sg_glos.html#one-sample">here</a>
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The sample must include at least 2 observations.
     * </li></ul>
     *
     * @param mu constant value to compare sample mean against
     * @param sampleStats StatisticalSummary describing sample data values
     * @param alpha significance level of the test
     * @return p-value
     * @throws IllegalArgumentException if the precondition is not met
     * @throws MathException if an error occurs computing the p-value
     */
    public boolean tTest( double mu, StatisticalSummary sampleStats,
            double alpha)
    throws IllegalArgumentException, MathException {
        if ((alpha <= 0) || (alpha > 0.5)) {
            throw new IllegalArgumentException("bad significance level: " + alpha);
        }
        return (tTest(mu, sampleStats) < alpha);
    }

    /**
     * Returns the <i>observed significance level</i>, or 
     * <i>p-value</i>, associated with a two-sample, two-tailed t-test 
     * comparing the means of the input arrays.
     * <p>
     * The number returned is the smallest significance level
     * at which one can reject the null hypothesis that the two means are
     * equal in favor of the two-sided alternative that they are different. 
     * For a one-sided test, divide the returned value by 2.
     * <p>
     * The test does not assume that the underlying popuation variances are
     * equal  and it uses approximated degrees of freedom computed from the 
     * sample data to compute the p-value.  The t-statistic used is as defined in
     * {@link #t(double[], double[])} and the Welch-Satterthwaite approximation
     * to the degrees of freedom is used, 
     * as described 
     * <a href="http://www.itl.nist.gov/div898/handbook/prc/section3/prc31.htm">
     * here.</a>  To perform the test under the assumption of equal subpopulation
     * variances, use {@link #homoscedasticTTest(double[], double[])}. 
     * <p>
     * <strong>Usage Note:</strong><br>
     * The validity of the p-value depends on the assumptions of the parametric
     * t-test procedure, as discussed 
     * <a href="http://www.basic.nwu.edu/statguidefiles/ttest_unpaired_ass_viol.html">
     * here</a>
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The observed array lengths must both be at least 2.
     * </li></ul>
     *
     * @param sample1 array of sample data values
     * @param sample2 array of sample data values
     * @return p-value for t-test
     * @throws IllegalArgumentException if the precondition is not met
     * @throws MathException if an error occurs computing the p-value
     */
    public double tTest(double[] sample1, double[] sample2)
    throws IllegalArgumentException, MathException {
        if ((sample1 == null) || (sample2 == null ||
                Math.min(sample1.length, sample2.length) < 2)) {
            throw new IllegalArgumentException("insufficient data");
        }
        return tTest(StatUtils.mean(sample1), StatUtils.mean(sample2),
                StatUtils.variance(sample1), StatUtils.variance(sample2),
                (double) sample1.length, (double) sample2.length);
    }
    
    /**
     * Returns the <i>observed significance level</i>, or 
     * <i>p-value</i>, associated with a two-sample, two-tailed t-test 
     * comparing the means of the input arrays, under the assumption that
     * the two samples are drawn from subpopulations with equal variances.
     * To perform the test without the equal variances assumption, use
     * {@link #tTest(double[], double[])}.
     * <p>
     * The number returned is the smallest significance level
     * at which one can reject the null hypothesis that the two means are
     * equal in favor of the two-sided alternative that they are different. 
     * For a one-sided test, divide the returned value by 2.
     * <p>
     * A pooled variance estimate is used to compute the t-statistic.  See
     * {@link #homoscedasticT(double[], double[])}. The sum of the sample sizes
     * minus 2 is used as the degrees of freedom.
     * <p>
     * <strong>Usage Note:</strong><br>
     * The validity of the p-value depends on the assumptions of the parametric
     * t-test procedure, as discussed 
     * <a href="http://www.basic.nwu.edu/statguidefiles/ttest_unpaired_ass_viol.html">
     * here</a>
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The observed array lengths must both be at least 2.
     * </li></ul>
     *
     * @param sample1 array of sample data values
     * @param sample2 array of sample data values
     * @return p-value for t-test
     * @throws IllegalArgumentException if the precondition is not met
     * @throws MathException if an error occurs computing the p-value
     */
    public double homoscedasticTTest(double[] sample1, double[] sample2)
    throws IllegalArgumentException, MathException {
        if ((sample1 == null) || (sample2 == null ||
                Math.min(sample1.length, sample2.length) < 2)) {
            throw new IllegalArgumentException("insufficient data");
        }
        return homoscedasticTTest(StatUtils.mean(sample1), 
                StatUtils.mean(sample2), StatUtils.variance(sample1),
                StatUtils.variance(sample2), (double) sample1.length, 
                (double) sample2.length);
    }
    

     /**
     * Performs a 
     * <a href="http://www.itl.nist.gov/div898/handbook/eda/section3/eda353.htm">
     * two-sided t-test</a> evaluating the null hypothesis that <code>sample1</code> 
     * and <code>sample2</code> are drawn from populations with the same mean, 
     * with significance level <code>alpha</code>.  This test does not assume
     * that the subpopulation variances are equal.  To perform the test assuming
     * equal variances, use 
     * {@link #homoscedasticTTest(double[], double[], double)}.
     * <p>
     * Returns <code>true</code> iff the null hypothesis that the means are
     * equal can be rejected with confidence <code>1 - alpha</code>.  To 
     * perform a 1-sided test, use <code>alpha / 2</code>
     * <p>
     * See {@link #t(double[], double[])} for the formula used to compute the
     * t-statistic.  Degrees of freedom are approximated using the
     * <a href="http://www.itl.nist.gov/div898/handbook/prc/section3/prc31.htm">
     * Welch-Satterthwaite approximation.</a>
      
     * <p>
     * <strong>Examples:</strong><br><ol>
     * <li>To test the (2-sided) hypothesis <code>mean 1 = mean 2 </code> at
     * the 95% level,  use 
     * <br><code>tTest(sample1, sample2, 0.05). </code>
     * </li>
     * <li>To test the (one-sided) hypothesis <code> mean 1 < mean 2 </code> at
     * the 99% level, first verify that the measured  mean of <code>sample 1</code>
     * is less than the mean of <code>sample 2</code> and then use 
     * <br><code>tTest(sample1, sample2, 0.02) </code>
     * </li></ol>
     * <p>
     * <strong>Usage Note:</strong><br>
     * The validity of the test depends on the assumptions of the parametric
     * t-test procedure, as discussed 
     * <a href="http://www.basic.nwu.edu/statguidefiles/ttest_unpaired_ass_viol.html">
     * here</a>
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The observed array lengths must both be at least 2.
     * </li>
     * <li> <code> 0 < alpha < 0.5 </code>
     * </li></ul>
     *
     * @param sample1 array of sample data values
     * @param sample2 array of sample data values
     * @param alpha significance level of the test
     * @return true if the null hypothesis can be rejected with 
     * confidence 1 - alpha
     * @throws IllegalArgumentException if the preconditions are not met
     * @throws MathException if an error occurs performing the test
     */
    public boolean tTest(double[] sample1, double[] sample2,
            double alpha)
    throws IllegalArgumentException, MathException {
        if ((alpha <= 0) || (alpha > 0.5)) {
            throw new IllegalArgumentException("bad significance level: " + alpha);
        }
        return (tTest(sample1, sample2) < alpha);
    }
    
    /**
     * Performs a 
     * <a href="http://www.itl.nist.gov/div898/handbook/eda/section3/eda353.htm">
     * two-sided t-test</a> evaluating the null hypothesis that <code>sample1</code> 
     * and <code>sample2</code> are drawn from populations with the same mean, 
     * with significance level <code>alpha</code>,  assuming that the
     * subpopulation variances are equal.  Use 
     * {@link #tTest(double[], double[], double)} to perform the test without
     * the assumption of equal variances.
     * <p>
     * Returns <code>true</code> iff the null hypothesis that the means are
     * equal can be rejected with confidence <code>1 - alpha</code>.  To 
     * perform a 1-sided test, use <code>alpha * 2.</code>  To perform the test
     * without the assumption of equal subpopulation variances, use 
     * {@link #tTest(double[], double[], double)}.
     * <p>
     * A pooled variance estimate is used to compute the t-statistic. See
     * {@link #t(double[], double[])} for the formula. The sum of the sample
     * sizes minus 2 is used as the degrees of freedom.
     * <p>
     * <strong>Examples:</strong><br><ol>
     * <li>To test the (2-sided) hypothesis <code>mean 1 = mean 2 </code> at
     * the 95% level, use <br><code>tTest(sample1, sample2, 0.05). </code>
     * </li>
     * <li>To test the (one-sided) hypothesis <code> mean 1 < mean 2, </code>
     * at the 99% level, first verify that the measured mean of 
     * <code>sample 1</code> is less than the mean of <code>sample 2</code>
     * and then use
     * <br><code>tTest(sample1, sample2, 0.02) </code>
     * </li></ol>
     * <p>
     * <strong>Usage Note:</strong><br>
     * The validity of the test depends on the assumptions of the parametric
     * t-test procedure, as discussed 
     * <a href="http://www.basic.nwu.edu/statguidefiles/ttest_unpaired_ass_viol.html">
     * here</a>
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The observed array lengths must both be at least 2.
     * </li>
     * <li> <code> 0 < alpha < 0.5 </code>
     * </li></ul>
     *
     * @param sample1 array of sample data values
     * @param sample2 array of sample data values
     * @param alpha significance level of the test
     * @return true if the null hypothesis can be rejected with 
     * confidence 1 - alpha
     * @throws IllegalArgumentException if the preconditions are not met
     * @throws MathException if an error occurs performing the test
     */
    public boolean homoscedasticTTest(double[] sample1, double[] sample2,
            double alpha)
    throws IllegalArgumentException, MathException {
        if ((alpha <= 0) || (alpha > 0.5)) {
            throw new IllegalArgumentException("bad significance level: " + alpha);
        }
        return (homoscedasticTTest(sample1, sample2) < alpha);
    }

     /**
     * Returns the <i>observed significance level</i>, or 
     * <i>p-value</i>, associated with a two-sample, two-tailed t-test 
     * comparing the means of the datasets described by two StatisticalSummary
     * instances.
     * <p>
     * The number returned is the smallest significance level
     * at which one can reject the null hypothesis that the two means are
     * equal in favor of the two-sided alternative that they are different. 
     * For a one-sided test, divide the returned value by 2.
     * <p>
     * The test does not assume that the underlying popuation variances are
     * equal  and it uses approximated degrees of freedom computed from the 
     * sample data to compute the p-value.   To perform the test assuming
     * equal variances, use 
     * {@link #homoscedasticTTest(StatisticalSummary, StatisticalSummary)}.
     * <p>
     * <strong>Usage Note:</strong><br>
     * The validity of the p-value depends on the assumptions of the parametric
     * t-test procedure, as discussed 
     * <a href="http://www.basic.nwu.edu/statguidefiles/ttest_unpaired_ass_viol.html">
     * here</a>
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The datasets described by the two Univariates must each contain
     * at least 2 observations.
     * </li></ul>
     *
     * @param sampleStats1  StatisticalSummary describing data from the first sample
     * @param sampleStats2  StatisticalSummary describing data from the second sample
     * @return p-value for t-test
     * @throws IllegalArgumentException if the precondition is not met
     * @throws MathException if an error occurs computing the p-value
     */
    public double tTest(StatisticalSummary sampleStats1, StatisticalSummary sampleStats2)
    throws IllegalArgumentException, MathException {
        if ((sampleStats1 == null) || (sampleStats2 == null ||
                Math.min(sampleStats1.getN(), sampleStats2.getN()) < 2)) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        return tTest(sampleStats1.getMean(), sampleStats2.getMean(), sampleStats1.getVariance(),
                sampleStats2.getVariance(), (double) sampleStats1.getN(), 
                (double) sampleStats2.getN());
    }
    
    /**
     * Returns the <i>observed significance level</i>, or 
     * <i>p-value</i>, associated with a two-sample, two-tailed t-test 
     * comparing the means of the datasets described by two StatisticalSummary
     * instances, under the hypothesis of equal subpopulation variances. To
     * perform a test without the equal variances assumption, use
     * {@link #tTest(StatisticalSummary, StatisticalSummary)}.
     * <p>
     * The number returned is the smallest significance level
     * at which one can reject the null hypothesis that the two means are
     * equal in favor of the two-sided alternative that they are different. 
     * For a one-sided test, divide the returned value by 2.
     * <p>
     * See {@link #homoscedasticT(double[], double[])} for the formula used to
     * compute the t-statistic. The sum of the  sample sizes minus 2 is used as
     * the degrees of freedom.
     * <p>
     * <strong>Usage Note:</strong><br>
     * The validity of the p-value depends on the assumptions of the parametric
     * t-test procedure, as discussed 
     * <a href="http://www.basic.nwu.edu/statguidefiles/ttest_unpaired_ass_viol.html">here</a>
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The datasets described by the two Univariates must each contain
     * at least 2 observations.
     * </li></ul>
     *
     * @param sampleStats1  StatisticalSummary describing data from the first sample
     * @param sampleStats2  StatisticalSummary describing data from the second sample
     * @return p-value for t-test
     * @throws IllegalArgumentException if the precondition is not met
     * @throws MathException if an error occurs computing the p-value
     */
    public double homoscedasticTTest(StatisticalSummary sampleStats1, 
            StatisticalSummary sampleStats2)
    throws IllegalArgumentException, MathException {
        if ((sampleStats1 == null) || (sampleStats2 == null ||
                Math.min(sampleStats1.getN(), sampleStats2.getN()) < 2)) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        return homoscedasticTTest(sampleStats1.getMean(),
                sampleStats2.getMean(), sampleStats1.getVariance(),
                sampleStats2.getVariance(), (double) sampleStats1.getN(), 
                (double) sampleStats2.getN());
    }

    /**
     * Performs a 
     * <a href="http://www.itl.nist.gov/div898/handbook/eda/section3/eda353.htm">
     * two-sided t-test</a> evaluating the null hypothesis that 
     * <code>sampleStats1</code> and <code>sampleStats2</code> describe
     * datasets drawn from populations with the same mean, with significance
     * level <code>alpha</code>.   This test does not assume that the
     * subpopulation variances are equal.  To perform the test under the equal
     * variances assumption, use
     * {@link #homoscedasticTTest(StatisticalSummary, StatisticalSummary)}.
     * <p>
     * Returns <code>true</code> iff the null hypothesis that the means are
     * equal can be rejected with confidence <code>1 - alpha</code>.  To 
     * perform a 1-sided test, use <code>alpha * 2</code>
     * <p>
     * See {@link #t(double[], double[])} for the formula used to compute the
     * t-statistic.  Degrees of freedom are approximated using the
     * <a href="http://www.itl.nist.gov/div898/handbook/prc/section3/prc31.htm">
     * Welch-Satterthwaite approximation.</a>
     * <p>
     * <strong>Examples:</strong><br><ol>
     * <li>To test the (2-sided) hypothesis <code>mean 1 = mean 2 </code> at
     * the 95%, use 
     * <br><code>tTest(sampleStats1, sampleStats2, 0.05) </code>
     * </li>
     * <li>To test the (one-sided) hypothesis <code> mean 1 < mean 2 </code>
     * at the 99% level,  first verify that the measured mean of  
     * <code>sample 1</code> is less than  the mean of <code>sample 2</code>
     * and then use 
     * <br><code>tTest(sampleStats1, sampleStats2, 0.02) </code>
     * </li></ol>
     * <p>
     * <strong>Usage Note:</strong><br>
     * The validity of the test depends on the assumptions of the parametric
     * t-test procedure, as discussed 
     * <a href="http://www.basic.nwu.edu/statguidefiles/ttest_unpaired_ass_viol.html">
     * here</a>
     * <p>
     * <strong>Preconditions</strong>: <ul>
     * <li>The datasets described by the two Univariates must each contain
     * at least 2 observations.
     * </li>
     * <li> <code> 0 < alpha < 0.5 </code>
     * </li></ul>
     *
     * @param sampleStats1 StatisticalSummary describing sample data values
     * @param sampleStats2 StatisticalSummary describing sample data values
     * @param alpha significance level of the test
     * @return true if the null hypothesis can be rejected with 
     * confidence 1 - alpha
     * @throws IllegalArgumentException if the preconditions are not met
     * @throws MathException if an error occurs performing the test
     */
    public boolean tTest(StatisticalSummary sampleStats1,
            StatisticalSummary sampleStats2, double alpha)
    throws IllegalArgumentException, MathException {
        if ((alpha <= 0) || (alpha > 0.5)) {
            throw new IllegalArgumentException("bad significance level: " + alpha);
        }
        return (tTest(sampleStats1, sampleStats2) < alpha);
    }
    
    //----------------------------------------------- Protected methods 

    /**
     * Gets a DistributionFactory to use in creating TDistribution instances.
     * @return a distribution factory.
     */
    protected DistributionFactory getDistributionFactory() {
        if (distributionFactory == null) {
            distributionFactory = DistributionFactory.newInstance();
        }
        return distributionFactory;
    }
    
    /**
     * Computes approximate degrees of freedom for 2-sample t-test.
     * 
     * @param v1 first sample variance
     * @param v2 second sample variance
     * @param n1 first sample n
     * @param n2 second sample n
     * @return approximate degrees of freedom
     */
    protected double df(double v1, double v2, double n1, double n2) {
        return (((v1 / n1) + (v2 / n2)) * ((v1 / n1) + (v2 / n2))) /
        ((v1 * v1) / (n1 * n1 * (n1 - 1d)) + (v2 * v2) /
                (n2 * n2 * (n2 - 1d)));
    }

    /**
     * Computes t test statistic for 1-sample t-test.
     * 
     * @param m sample mean
     * @param mu constant to test against
     * @param v sample variance
     * @param n sample n
     * @return t test statistic
     */
    protected double t(double m, double mu, double v, double n) {
        return (m - mu) / Math.sqrt(v / n);
    }
    
    /**
     * Computes t test statistic for 2-sample t-test.
     * <p>
     * Does not assume that subpopulation variances are equal.
     * 
     * @param m1 first sample mean
     * @param m2 second sample mean
     * @param v1 first sample variance
     * @param v2 second sample variance
     * @param n1 first sample n
     * @param n2 second sample n
     * @return t test statistic
     */
    protected double t(double m1, double m2,  double v1, double v2, double n1,
            double n2)  {
            return (m1 - m2) / Math.sqrt((v1 / n1) + (v2 / n2));
    }
    
    /**
     * Computes t test statistic for 2-sample t-test under the hypothesis
     * of equal subpopulation variances.
     * 
     * @param m1 first sample mean
     * @param m2 second sample mean
     * @param v1 first sample variance
     * @param v2 second sample variance
     * @param n1 first sample n
     * @param n2 second sample n
     * @return t test statistic
     */
    protected double homoscedasticT(double m1, double m2,  double v1,
            double v2, double n1, double n2)  {
            double pooledVariance = ((n1  - 1) * v1 + (n2 -1) * v2 ) / (n1 + n2 - 2); 
            return (m1 - m2) / Math.sqrt(pooledVariance * (1d / n1 + 1d / n2));
    }
    
    /**
     * Computes p-value for 2-sided, 1-sample t-test.
     * 
     * @param m sample mean
     * @param mu constant to test against
     * @param v sample variance
     * @param n sample n
     * @return p-value
     * @throws MathException if an error occurs computing the p-value
     */
    protected double tTest(double m, double mu, double v, double n)
    throws MathException {
        double t = Math.abs(t(m, mu, v, n));
        TDistribution tDistribution = 
            getDistributionFactory().createTDistribution(n - 1);
        return 1.0 - tDistribution.cumulativeProbability(-t, t);
    }

    /**
     * Computes p-value for 2-sided, 2-sample t-test.
     * <p>
     * Does not assume subpopulation variances are equal. Degrees of freedom
     * are estimated from the data.
     * 
     * @param m1 first sample mean
     * @param m2 second sample mean
     * @param v1 first sample variance
     * @param v2 second sample variance
     * @param n1 first sample n
     * @param n2 second sample n
     * @return p-value
     * @throws MathException if an error occurs computing the p-value
     */
    protected double tTest(double m1, double m2, double v1, double v2, 
            double n1, double n2)
    throws MathException {
        double t = Math.abs(t(m1, m2, v1, v2, n1, n2));
        double degreesOfFreedom = 0;
        degreesOfFreedom= df(v1, v2, n1, n2);
        TDistribution tDistribution =
            getDistributionFactory().createTDistribution(degreesOfFreedom);
        return 1.0 - tDistribution.cumulativeProbability(-t, t);
    }
    
    /**
     * Computes p-value for 2-sided, 2-sample t-test, under the assumption
     * of equal subpopulation variances.
     * <p>
     * The sum of the sample sizes minus 2 is used as degrees of freedom.
     * 
     * @param m1 first sample mean
     * @param m2 second sample mean
     * @param v1 first sample variance
     * @param v2 second sample variance
     * @param n1 first sample n
     * @param n2 second sample n
     * @return p-value
     * @throws MathException if an error occurs computing the p-value
     */
    protected double homoscedasticTTest(double m1, double m2, double v1,
            double v2, double n1, double n2)
    throws MathException {
        double t = Math.abs(t(m1, m2, v1, v2, n1, n2));
        double degreesOfFreedom = 0;
            degreesOfFreedom = (double) (n1 + n2 - 2);
        TDistribution tDistribution =
            getDistributionFactory().createTDistribution(degreesOfFreedom);
        return 1.0 - tDistribution.cumulativeProbability(-t, t);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2592.java