error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11155.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11155.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,63]

error in qdox parser
file content:
```java
offset: 63
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11155.java
text:
```scala
"org.apache.commons.math.distribution.DistributionFactoryImpl")@@;

/*
 * Copyright 2003-2005 The Apache Software Foundation.
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

import org.apache.commons.discovery.tools.DiscoverClass;

/**
 * This factory provids the means to create common statistical distributions.
 * The following distributions are supported:
 * <ul>
 * <li>Binomial</li>
 * <li>Cauchy</li>
 * <li>Chi-Squared</li>
 * <li>Exponential</li>
 * <li>F</li>
 * <li>Gamma</li>
 * <li>HyperGeometric</li>
 * <li>Poisson</li>
 * <li>Normal</li>
 * <li>Student's t</li>
 * <li>Weibull</li>
 * </ul>
 *
 * Common usage:<pre>
 * DistributionFactory factory = DistributionFactory.newInstance();
 *
 * // create a Chi-Square distribution with 5 degrees of freedom.
 * ChiSquaredDistribution chi = factory.createChiSquareDistribution(5.0);
 * </pre>
 *
 * @version $Revision$ $Date$
 */
public abstract class DistributionFactory {
    /**
     * Default constructor.
     */
    protected DistributionFactory() {
        super();
    }
    
    /**
     * Create an instance of a <code>DistributionFactory</code>
     * @return a new factory. 
     */
    public static DistributionFactory newInstance() {
        DistributionFactory factory = null;
        try {
            DiscoverClass dc = new DiscoverClass();
            factory = (DistributionFactory) dc.newInstance(
                DistributionFactory.class,
                "org.apache.commons.math.distribution.TestFactoryImpl");
        } catch(Throwable t) {
            return new DistributionFactoryImpl();
        }
        return factory;
    }

    /**
     * Create a binomial distribution with the given number of trials and
     * probability of success.
     * 
     * @param numberOfTrials the number of trials.
     * @param probabilityOfSuccess the probability of success
     * @return a new binomial distribution
     */
    public abstract BinomialDistribution createBinomialDistribution(
        int numberOfTrials, double probabilityOfSuccess);
    
    /**
     * Create a new cauchy distribution with the given median and scale.
     * @param median the median of the distribution
     * @param scale the scale
     * @return a new cauchy distribution  
     * @since 1.1
     */           
    public CauchyDistribution createCauchyDistribution(
        double median, double scale)
    {
        return new CauchyDistributionImpl(median, scale);
    }
        
    /**
     * Create a new chi-square distribution with the given degrees of freedom.
     * 
     * @param degreesOfFreedom degrees of freedom
     * @return a new chi-square distribution  
     */
    public abstract ChiSquaredDistribution createChiSquareDistribution(
        double degreesOfFreedom);
    
    /**
     * Create a new exponential distribution with the given degrees of freedom.
     * 
     * @param mean mean
     * @return a new exponential distribution  
     */
    public abstract ExponentialDistribution createExponentialDistribution(
        double mean);
    
    /**
     * Create a new F-distribution with the given degrees of freedom.
     * 
     * @param numeratorDegreesOfFreedom numerator degrees of freedom
     * @param denominatorDegreesOfFreedom denominator degrees of freedom
     * @return a new F-distribution 
     */
    public abstract FDistribution createFDistribution(
        double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom);
    
    /**
     * Create a new gamma distribution with the given shape and scale
     * parameters.
     * 
     * @param alpha the shape parameter
     * @param beta the scale parameter
     * 
     * @return a new gamma distribution  
     */
    public abstract GammaDistribution createGammaDistribution(
        double alpha, double beta);

    /**
     * Create a new t distribution with the given degrees of freedom.
     * 
     * @param degreesOfFreedom degrees of freedom
     * @return a new t distribution  
     */
    public abstract TDistribution createTDistribution(double degreesOfFreedom);
    
    /**
     * Create a new hypergeometric distribution with the given the population
     * size, the number of successes in the population, and the sample size.
     * 
     * @param populationSize the population size
     * @param numberOfSuccesses number of successes in the population
     * @param sampleSize the sample size
     * @return a new hypergeometric desitribution
     */
    public abstract HypergeometricDistribution
        createHypergeometricDistribution(int populationSize,
            int numberOfSuccesses, int sampleSize);
 
	/**
	 * Create a new normal distribution with the given mean and standard
	 * deviation.
     * 
	 * @param mean the mean of the distribution
	 * @param sd standard deviation
	 * @return a new normal distribution  
	 */           
    public abstract NormalDistribution 
    	createNormalDistribution(double mean, double sd);
    	
	/**
	 * Create a new normal distribution with mean zero and standard
	 * deviation one.
     * 
	 * @return a new normal distribution.  
	 */               
	public abstract NormalDistribution createNormalDistribution();
    
    /**
     * Create a new Poisson distribution with poisson parameter lambda.
     * 
     * @param lambda poisson parameter
     * @return a new poisson distribution.  
     */               
    public abstract PoissonDistribution 
        createPoissonDistribution(double lambda);
    
    /**
     * Create a new Weibull distribution with the given shape and scale
     * parameters.
     * 
     * @param alpha the shape parameter.
     * @param beta the scale parameter.
     * @return a new Weibull distribution.  
     * @since 1.1
     */               
    public WeibullDistribution createWeibullDistribution(
        double alpha, double beta)
    {
        return new WeibullDistributionImpl(alpha, beta);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11155.java