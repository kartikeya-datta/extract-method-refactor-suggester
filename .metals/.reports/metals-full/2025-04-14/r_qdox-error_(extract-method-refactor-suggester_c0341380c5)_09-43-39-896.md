error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2881.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2881.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,2]

error in qdox parser
file content:
```java
offset: 2
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2881.java
text:
```scala
0,@@ 64);

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

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

/**
 * Implements the <a href="http://mathworld.wolfram.com/SimpsonsRule.html">
 * Simpson's Rule</a> for integration of real univariate functions. For
 * reference, see <b>Introduction to Numerical Analysis</b>, ISBN 038795452X,
 * chapter 3.
 * <p>
 * This implementation employs basic trapezoid rule as building blocks to
 * calculate the Simpson's rule of alternating 2/3 and 4/3.</p>
 *  
 * @version $Revision$ $Date$
 * @since 1.2
 */
public class SimpsonIntegrator extends UnivariateRealIntegratorImpl {

    /** Serializable version identifier. */
    private static final long serialVersionUID = 2535890386567281329L;

    /**
     * Construct an integrator for the given function.
     * 
     * @param f function to integrate
     * @deprecated as of 2.0 the integrand function is passed as an argument
     * to the {@link #integrate(UnivariateRealFunction, double, double)}method.
     */
    @Deprecated
    public SimpsonIntegrator(UnivariateRealFunction f) {
        super(f, 64);
    }

    /**
     * Construct an integrator.
     */
    public SimpsonIntegrator() {
        super(64);
    }

    /** {@inheritDoc} */
    @Deprecated
    public double integrate(final double min, final double max)
        throws MaxIterationsExceededException, FunctionEvaluationException, IllegalArgumentException {
        return integrate(f, min, max);
    }

    /** {@inheritDoc} */
    public double integrate(final UnivariateRealFunction f,
                            final double min, final double max)
        throws MaxIterationsExceededException, FunctionEvaluationException, IllegalArgumentException {
        
        int i = 1;
        double s, olds, t, oldt;
        
        clearResult();
        verifyInterval(min, max);
        verifyIterationCount();

        TrapezoidIntegrator qtrap = new TrapezoidIntegrator();
        if (minimalIterationCount == 1) {
            s = (4 * qtrap.stage(f, min, max, 1) - qtrap.stage(f, min, max, 0)) / 3.0;
            setResult(s, 1);
            return result;
        }
        // Simpson's rule requires at least two trapezoid stages.
        olds = 0;
        oldt = qtrap.stage(f, min, max, 0);
        while (i <= maximalIterationCount) {
            t = qtrap.stage(f, min, max, i);
            s = (4 * t - oldt) / 3.0;
            if (i >= minimalIterationCount) {
                final double delta = Math.abs(s - olds);
                final double rLimit =
                    relativeAccuracy * (Math.abs(olds) + Math.abs(s)) * 0.5; 
                if ((delta <= rLimit) || (delta <= absoluteAccuracy)) {
                    setResult(s, i);
                    return result;
                }
            }
            olds = s;
            oldt = t;
            i++;
        }
        throw new MaxIterationsExceededException(maximalIterationCount);
    }

    /** {@inheritDoc} */
    protected void verifyIterationCount() throws IllegalArgumentException {
        super.verifyIterationCount();
        // at most 64 bisection refinements
        if (maximalIterationCount > 64) {
            throw MathRuntimeException.createIllegalArgumentException(
                    "invalid iteration limits: min={0}, max={1}",
                    new Object[] { 0, 64 });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2881.java