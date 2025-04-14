error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7441.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7441.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7441.java
text:
```scala
L@@ocalizedFormats.ENDPOINTS_NOT_AN_INTERVAL,

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
package org.apache.commons.math.analysis.integration;

import org.apache.commons.math.ConvergingAlgorithmImpl;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.util.LocalizedFormats;

/**
 * Provide a default implementation for several generic functions.
 *
 * @version $Revision$ $Date$
 * @since 1.2
 */
public abstract class UnivariateRealIntegratorImpl
    extends ConvergingAlgorithmImpl implements UnivariateRealIntegrator {

    /** Serializable version identifier. */
    private static final long serialVersionUID = 6248808456637441533L;

    /** minimum number of iterations */
    protected int minimalIterationCount;

    /** default minimum number of iterations */
    protected int defaultMinimalIterationCount;

    /** indicates whether an integral has been computed */
    protected boolean resultComputed = false;

    /** the last computed integral */
    protected double result;

    /** The integrand functione.
     * @deprecated as of 2.0 the integrand function is passed as an argument
     * to the {@link #integrate(UnivariateRealFunction, double, double)}method. */
    @Deprecated
    protected UnivariateRealFunction f;

    /**
     * Construct an integrator with given iteration count and accuracy.
     *
     * @param f the integrand function
     * @param defaultMaximalIterationCount maximum number of iterations
     * @throws IllegalArgumentException if f is null or the iteration
     * limits are not valid
     * @deprecated as of 2.0 the integrand function is passed as an argument
     * to the {@link #integrate(UnivariateRealFunction, double, double)}method.
     */
    @Deprecated
    protected UnivariateRealIntegratorImpl(final UnivariateRealFunction f,
                                           final int defaultMaximalIterationCount)
        throws IllegalArgumentException {
        super(defaultMaximalIterationCount, 1.0e-15);
        if (f == null) {
            throw MathRuntimeException.createIllegalArgumentException(LocalizedFormats.NULL_FUNCTION);
        }

        this.f = f;

        // parameters that are problem specific
        setRelativeAccuracy(1.0e-6);
        this.defaultMinimalIterationCount = 3;
        this.minimalIterationCount = defaultMinimalIterationCount;

        verifyIterationCount();
    }

    /**
     * Construct an integrator with given iteration count and accuracy.
     *
     * @param defaultMaximalIterationCount maximum number of iterations
     * @throws IllegalArgumentException if f is null or the iteration
     * limits are not valid
     */
    protected UnivariateRealIntegratorImpl(final int defaultMaximalIterationCount)
        throws IllegalArgumentException {
        super(defaultMaximalIterationCount, 1.0e-15);

        // parameters that are problem specific
        setRelativeAccuracy(1.0e-6);
        this.defaultMinimalIterationCount = 3;
        this.minimalIterationCount = defaultMinimalIterationCount;

        verifyIterationCount();
    }

    /**
     * Access the last computed integral.
     *
     * @return the last computed integral
     * @throws IllegalStateException if no integral has been computed
     */
    public double getResult() throws IllegalStateException {
        if (resultComputed) {
            return result;
        } else {
            throw MathRuntimeException.createIllegalStateException(LocalizedFormats.NO_RESULT_AVAILABLE);
        }
    }

    /**
     * Convenience function for implementations.
     *
     * @param newResult the result to set
     * @param iterationCount the iteration count to set
     */
    protected final void setResult(double newResult, int iterationCount) {
        this.result         = newResult;
        this.iterationCount = iterationCount;
        this.resultComputed = true;
    }

    /**
     * Convenience function for implementations.
     */
    protected final void clearResult() {
        this.iterationCount = 0;
        this.resultComputed = false;
    }

    /** {@inheritDoc} */
    public void setMinimalIterationCount(int count) {
        minimalIterationCount = count;
    }

    /** {@inheritDoc} */
    public int getMinimalIterationCount() {
        return minimalIterationCount;
    }

    /** {@inheritDoc} */
    public void resetMinimalIterationCount() {
        minimalIterationCount = defaultMinimalIterationCount;
    }

    /**
     * Verifies that the endpoints specify an interval.
     *
     * @param lower lower endpoint
     * @param upper upper endpoint
     * @throws IllegalArgumentException if not interval
     */
    protected void verifyInterval(double lower, double upper) throws
        IllegalArgumentException {
        if (lower >= upper) {
            throw MathRuntimeException.createIllegalArgumentException(
                    "endpoints do not specify an interval: [{0}, {1}]",
                    lower, upper);
        }
    }

    /**
     * Verifies that the upper and lower limits of iterations are valid.
     *
     * @throws IllegalArgumentException if not valid
     */
    protected void verifyIterationCount() throws IllegalArgumentException {
        if ((minimalIterationCount <= 0) || (maximalIterationCount <= minimalIterationCount)) {
            throw MathRuntimeException.createIllegalArgumentException(
                    LocalizedFormats.INVALID_ITERATIONS_LIMITS,
                    minimalIterationCount, maximalIterationCount);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7441.java