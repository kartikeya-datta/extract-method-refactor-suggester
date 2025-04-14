error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13335.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13335.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13335.java
text:
```scala
private static final l@@ong serialVersionUID = 6751309484392813623L;

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

import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;
import org.apache.commons.math.util.MathUtils;

/**
 * The default implementation of {@link BinomialDistribution}.
 *
 * @version $Revision$ $Date$
 */
public class BinomialDistributionImpl
    extends AbstractIntegerDistribution
    implements BinomialDistribution, Serializable {

    /** Serializable version identifier */
    static final long serialVersionUID = 6751309484392813623L;

    /** The number of trials. */
    private int numberOfTrials;

    /** The probability of success. */
    private double probabilityOfSuccess;

    /**
     * Create a binomial distribution with the given number of trials and
     * probability of success.
     * @param trials the number of trials.
     * @param p the probability of success.
     */
    public BinomialDistributionImpl(int trials, double p) {
        super();
        setNumberOfTrials(trials);
        setProbabilityOfSuccess(p);
    }

    /**
     * Access the number of trials for this distribution.
     * @return the number of trials.
     */
    public int getNumberOfTrials() {
        return numberOfTrials;
    }

    /**
     * Access the probability of success for this distribution.
     * @return the probability of success.
     */
    public double getProbabilityOfSuccess() {
        return probabilityOfSuccess;
    }

    /**
     * Change the number of trials for this distribution.
     * @param trials the new number of trials.
     * @throws IllegalArgumentException if <code>trials</code> is not a valid
     *         number of trials.
     */
    public void setNumberOfTrials(int trials) {
        if (trials < 0) {
            throw new IllegalArgumentException("number of trials must be non-negative.");
        }
        numberOfTrials = trials;
    }

    /**
     * Change the probability of success for this distribution.
     * @param p the new probability of success.
     * @throws IllegalArgumentException if <code>p</code> is not a valid
     *         probability.
     */
    public void setProbabilityOfSuccess(double p) {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("probability of success must be between 0.0 and 1.0, inclusive.");
        }
        probabilityOfSuccess = p;
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
        return -1;
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
        return getNumberOfTrials();
    }

    /**
     * For this distribution, X, this method returns P(X &le; x).
     * @param x the value at which the PDF is evaluated.
     * @return PDF for this distribution. 
     * @throws MathException if the cumulative probability can not be
     *            computed due to convergence or other numerical errors.
     */
    public double cumulativeProbability(int x) throws MathException {
        double ret;
        if (x < 0) {
            ret = 0.0;
        } else if (x >= getNumberOfTrials()) {
            ret = 1.0;
        } else {
            ret =
                1.0 - Beta.regularizedBeta(
                        getProbabilityOfSuccess(),
                        x + 1.0,
                        getNumberOfTrials() - x);
        }
        return ret;
    }

    /**
     * For this disbution, X, this method returns P(X = x).
     * 
     * @param x the value at which the PMF is evaluated.
     * @return PMF for this distribution. 
     */
    public double probability(int x) {
        double ret;
        if (x < 0 || x > getNumberOfTrials()) {
            ret = 0.0;
        } else {
            ret = MathUtils.binomialCoefficientDouble(
                    getNumberOfTrials(), x) *
                  Math.pow(getProbabilityOfSuccess(), x) *
                  Math.pow(1.0 - getProbabilityOfSuccess(),
                        getNumberOfTrials() - x);
        }
        return ret;
    }
    
    /**
     * For this distribution, X, this method returns the largest x, such
     * that P(X &le; x) &le; <code>p</code>.
     * <p>
     * Returns <code>-1</code> for p=0 and <code>Integer.MAX_VALUE</code> for
     * p=1.
     *
     * @param p the desired probability
     * @return the largest x such that P(X &le; x) <= p
     * @throws MathException if the inverse cumulative probability can not be
     *            computed due to convergence or other numerical errors.
     * @throws IllegalArgumentException if p < 0 or p > 1
     */
    public int inverseCumulativeProbability(final double p) throws MathException {
        // handle extreme values explicitly
        if (p == 0) {
            return -1;
        } 
        if (p == 1) {
            return Integer.MAX_VALUE; 
        }
        
        // use default bisection impl
        return super.inverseCumulativeProbability(p);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13335.java