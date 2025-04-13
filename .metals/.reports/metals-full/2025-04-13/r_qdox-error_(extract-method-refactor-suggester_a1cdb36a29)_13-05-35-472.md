error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13339.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13339.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13339.java
text:
```scala
private static final l@@ong serialVersionUID = -436928820673516179L;

/*
 * Copyright 2003-2004 The Apache Software Foundation.
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

package org.apache.commons.math.distribution;

import java.io.Serializable;

import org.apache.commons.math.util.MathUtils;

/**
 * The default implementation of {@link HypergeometricDistribution}.
 *
 * @version $Revision$ $Date$
 */
public class HypergeometricDistributionImpl extends AbstractIntegerDistribution
    implements HypergeometricDistribution, Serializable 
{

    /** Serializable version identifier */
    static final long serialVersionUID = -436928820673516179L;

    /** The number of successes in the population. */
    private int numberOfSuccesses;
    
    /** The population size. */
    private int populationSize;
    
    /** The sample size. */
    private int sampleSize;
    
    /**
     * Construct a new hypergeometric distribution with the given the population
     * size, the number of successes in the population, and the sample size.
     * @param populationSize the population size.
     * @param numberOfSuccesses number of successes in the population.
     * @param sampleSize the sample size.
     */
    public HypergeometricDistributionImpl(int populationSize,
        int numberOfSuccesses, int sampleSize) {
        super();
        if (numberOfSuccesses > populationSize) {
            throw new IllegalArgumentException(
            	"number of successes must be less than or equal to " +
            	"population size");
        }
        if (sampleSize > populationSize) {
            throw new IllegalArgumentException(
            "sample size must be less than or equal to population size");
        }
        setPopulationSize(populationSize);
        setSampleSize(sampleSize);
        setNumberOfSuccesses(numberOfSuccesses);
    }

    /**
     * For this disbution, X, this method returns P(X &le; x).
     * @param x the value at which the PDF is evaluated.
     * @return PDF for this distribution. 
     */
    public double cumulativeProbability(int x) {
        double ret;
        
        int n = getPopulationSize();
        int m = getNumberOfSuccesses();
        int k = getSampleSize();

        int[] domain = getDomain(n, m, k);
        if (x < domain[0]) {
            ret = 0.0;
        } else if(x >= domain[1]) {
            ret = 1.0;
        } else {
            ret = innerCumulativeProbability(domain[0], x, 1, n, m, k);
        }
        
        return ret;
    }

    /**
     * Return the domain for the given hypergeometric distribution parameters.
     * @param n the population size.
     * @param m number of successes in the population.
     * @param k the sample size.
     * @return a two element array containing the lower and upper bounds of the
     *         hypergeometric distribution.  
     */
    private int[] getDomain(int n, int m, int k){
        return new int[]{
            getLowerDomain(n, m, k),
            getUpperDomain(m, k)
        };
    }
    
    /**
     * Access the domain value lower bound, based on <code>p</code>, used to
     * bracket a PDF root.
     * 
     * @param p the desired probability for the critical value
     * @return domain value lower bound, i.e.
     *         P(X &lt; <i>lower bound</i>) &lt; <code>p</code> 
     */
    protected int getDomainLowerBound(double p) {
        return getLowerDomain(getPopulationSize(), getNumberOfSuccesses(),
            getSampleSize());
    }
    
    /**
     * Access the domain value upper bound, based on <code>p</code>, used to
     * bracket a PDF root.
     * 
     * @param p the desired probability for the critical value
     * @return domain value upper bound, i.e.
     *         P(X &lt; <i>upper bound</i>) &gt; <code>p</code> 
     */
    protected int getDomainUpperBound(double p) {
        return getUpperDomain(getSampleSize(), getNumberOfSuccesses());
    }

    /**
     * Return the lowest domain value for the given hypergeometric distribution
     * parameters.
     * @param n the population size.
     * @param m number of successes in the population.
     * @param k the sample size.
     * @return the lowest domain value of the hypergeometric distribution.  
     */
    private int getLowerDomain(int n, int m, int k) {
        return Math.max(0, m - (n - k));
    }

    /**
     * Access the number of successes.
     * @return the number of successes.
     */
    public int getNumberOfSuccesses() {
        return numberOfSuccesses;
    }

    /**
     * Access the population size.
     * @return the population size.
     */
    public int getPopulationSize() {
        return populationSize;
    }

    /**
     * Access the sample size.
     * @return the sample size.
     */
    public int getSampleSize() {
        return sampleSize;
    }

    /**
     * Return the highest domain value for the given hypergeometric distribution
     * parameters.
     * @param m number of successes in the population.
     * @param k the sample size.
     * @return the highest domain value of the hypergeometric distribution.  
     */
    private int getUpperDomain(int m, int k){
        return Math.min(k, m);
    }

    /**
     * For this disbution, X, this method returns P(X = x).
     * 
     * @param x the value at which the PMF is evaluated.
     * @return PMF for this distribution. 
     */
    public double probability(int x) {
        double ret;
        
        int n = getPopulationSize();
        int m = getNumberOfSuccesses();
        int k = getSampleSize();

        int[] domain = getDomain(n, m, k);
        if(x < domain[0] || x > domain[1]){
            ret = 0.0;
        } else {
            ret = probability(n, m, k, x);
        }
        
        return ret;
    }
    
    /**
     * For the disbution, X, defined by the given hypergeometric distribution
     * parameters, this method returns P(X = x).
     * 
     * @param n the population size.
     * @param m number of successes in the population.
     * @param k the sample size.
     * @param x the value at which the PMF is evaluated.
     * @return PMF for the distribution. 
     */
    private double probability(int n, int m, int k, int x) {
        return Math.exp(MathUtils.binomialCoefficientLog(m, x) +
            MathUtils.binomialCoefficientLog(n - m, k - x) -
            MathUtils.binomialCoefficientLog(n, k));
    }

    /**
     * Modify the number of successes.
     * @param num the new number of successes.
     * @throws IllegalArgumentException if <code>num</code> is negative.
     */
    public void setNumberOfSuccesses(int num) {
        if(num < 0){
            throw new IllegalArgumentException(
                "number of successes must be non-negative.");
        }
        numberOfSuccesses = num;
    }

    /**
     * Modify the population size.
     * @param size the new population size.
     * @throws IllegalArgumentException if <code>size</code> is not positive.
     */
    public void setPopulationSize(int size) {
        if(size <= 0){
            throw new IllegalArgumentException(
                "population size must be positive.");
        }
        populationSize = size;
    }
    
	/**
     * Modify the sample size.
     * @param size the new sample size.
     * @throws IllegalArgumentException if <code>size</code> is negative.
     */
    public void setSampleSize(int size) {
        if (size < 0) {
            throw new IllegalArgumentException(
                "sample size must be non-negative.");
        }    
        sampleSize = size;
    }

    /**
     * For this disbution, X, this method returns P(X &ge; x).
     * @param x the value at which the CDF is evaluated.
     * @return upper tail CDF for this distribution.
     * @since 1.1
     */
	public double upperCumulativeProbability(int x) {
    	double ret;
    	
        int n = getPopulationSize();
        int m = getNumberOfSuccesses();
        int k = getSampleSize();

        int[] domain = getDomain(n, m, k);
        if (x < domain[0]) {
            ret = 1.0;
        } else if(x > domain[1]) {
            ret = 0.0;
        } else {
        	ret = innerCumulativeProbability(domain[1], x, -1, n, m, k);
        }
        
        return ret;
    }
	
    /**
     * For this disbution, X, this method returns P(x0 &le; X &le; x1).  This
     * probability is computed by summing the point probabilities for the values
     * x0, x0 + 1, x0 + 2, ..., x1, in the order directed by dx. 
     * @param x0 the inclusive, lower bound
     * @param x1 the inclusive, upper bound
     * @param dx the direction of summation. 1 indicates summing from x0 to x1.
     *           0 indicates summing from x1 to x0.
     * @param n the population size.
     * @param m number of successes in the population.
     * @param k the sample size.
     * @return P(x0 &le; X &le; x1). 
     */
    private double innerCumulativeProbability(
    	int x0, int x1, int dx, int n, int m, int k)
    {
    	double ret = probability(n, m, k, x0);
    	while (x0 != x1) {
    		x0 += dx;
    		ret += probability(n, m, k, x0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13339.java