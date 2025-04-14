error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15251.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15251.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15251.java
text:
```scala
i@@f (numberOfPoints % 2 != 0) {

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
package org.apache.commons.math3.analysis.integration.gauss;

import java.math.MathContext;
import java.math.BigDecimal;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/**
 * Factory that creates Gauss-type quadrature rule using Legendre polynomials.
 * In this implementation, the lower and upper bounds of the natural interval
 * of integration are -1 and 1, respectively.
 * The Legendre polynomials are evaluated using the recurrence relation
 * presented in <a href="http://en.wikipedia.org/wiki/Abramowitz_and_Stegun"
 * Abramowitz and Stegun, 1964</a>.
 *
 * @since 3.1
 * @version $Id$
 */
public class LegendreHighPrecisionRuleFactory extends BaseRuleFactory<BigDecimal> {
    /** Settings for enhanced precision computations. */
    private final MathContext mContext;
    /** The number {@code 2}. */
    private final BigDecimal two;
    /** The number {@code -1}. */
    private final BigDecimal minusOne;
    /** The number {@code 0.5}. */
    private final BigDecimal oneHalf;

    /**
     * Default precision is {@link MathContext#DECIMAL128 DECIMAL128}.
     */
    public LegendreHighPrecisionRuleFactory() {
        this(MathContext.DECIMAL128);
    }

    /**
     * @param mContext Precision setting for computing the quadrature rules.
     */
    public LegendreHighPrecisionRuleFactory(MathContext mContext) {
        this.mContext = mContext;
        two = new BigDecimal("2", mContext);
        minusOne = new BigDecimal("-1", mContext);
        oneHalf = new BigDecimal("0.5", mContext);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NotStrictlyPositiveException if {@code numberOfPoints < 1}.
     */
    @Override
    protected Pair<BigDecimal[], BigDecimal[]> computeRule(int numberOfPoints) {
        if (numberOfPoints <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_POINTS,
                                                   numberOfPoints);
        }

        if (numberOfPoints == 1) {
            // Break recursion.
            return new Pair<BigDecimal[], BigDecimal[]>(new BigDecimal[] { BigDecimal.ZERO },
                                                        new BigDecimal[] { two });
        }

        // Get previous rule.
        // If it has not been computed yet it will trigger a recursive call
        // to this method.
        final BigDecimal[] previousPoints = getRuleInternal(numberOfPoints - 1).getFirst();

        // Compute next rule.
        final BigDecimal[] points = new BigDecimal[numberOfPoints];
        final BigDecimal[] weights = new BigDecimal[numberOfPoints];

        // Find i-th root of P[n+1] by bracketing.
        final int iMax = numberOfPoints / 2;
        for (int i = 0; i < iMax; i++) {
            // Lower-bound of the interval.
            BigDecimal a = (i == 0) ? minusOne : previousPoints[i - 1];
            // Upper-bound of the interval.
            BigDecimal b = (iMax == 1) ? BigDecimal.ONE : previousPoints[i];
            // P[j-1](a)
            BigDecimal pma = BigDecimal.ONE;
            // P[j](a)
            BigDecimal pa = a;
            // P[j-1](b)
            BigDecimal pmb = BigDecimal.ONE;
            // P[j](b)
            BigDecimal pb = b;
            for (int j = 1; j < numberOfPoints; j++) {
                final BigDecimal b_two_j_p_1 = new BigDecimal(2 * j + 1, mContext);
                final BigDecimal b_j = new BigDecimal(j, mContext);
                final BigDecimal b_j_p_1 = new BigDecimal(j + 1, mContext);

                // Compute P[j+1](a)
                // ppa = ((2 * j + 1) * a * pa - j * pma) / (j + 1);

                BigDecimal tmp1 = a.multiply(b_two_j_p_1, mContext);
                tmp1 = pa.multiply(tmp1, mContext);
                BigDecimal tmp2 = pma.multiply(b_j, mContext);
                // P[j+1](a)
                BigDecimal ppa = tmp1.subtract(tmp2, mContext);
                ppa = ppa.divide(b_j_p_1, mContext);

                // Compute P[j+1](b)
                // ppb = ((2 * j + 1) * b * pb - j * pmb) / (j + 1);

                tmp1 = b.multiply(b_two_j_p_1, mContext);
                tmp1 = pb.multiply(tmp1, mContext);
                tmp2 = pmb.multiply(b_j, mContext);
                // P[j+1](b)
                BigDecimal ppb = tmp1.subtract(tmp2, mContext);
                ppb = ppb.divide(b_j_p_1, mContext);

                pma = pa;
                pa = ppa;
                pmb = pb;
                pb = ppb;
            }
            // Now pa = P[n+1](a), and pma = P[n](a). Same holds for b.
            // Middle of the interval.
            BigDecimal c = a.add(b, mContext).multiply(oneHalf, mContext);
            // P[j-1](c)
            BigDecimal pmc = BigDecimal.ONE;
            // P[j](c)
            BigDecimal pc = c;
            boolean done = false;
            while (!done) {
                BigDecimal tmp1 = b.subtract(a, mContext);
                BigDecimal tmp2 = c.ulp().multiply(BigDecimal.TEN, mContext);
                done = tmp1.compareTo(tmp2) <= 0;
                pmc = BigDecimal.ONE;
                pc = c;
                for (int j = 1; j < numberOfPoints; j++) {
                    final BigDecimal b_two_j_p_1 = new BigDecimal(2 * j + 1, mContext);
                    final BigDecimal b_j = new BigDecimal(j, mContext);
                    final BigDecimal b_j_p_1 = new BigDecimal(j + 1, mContext);

                    // Compute P[j+1](c)
                    tmp1 = c.multiply(b_two_j_p_1, mContext);
                    tmp1 = pc.multiply(tmp1, mContext);
                    tmp2 = pmc.multiply(b_j, mContext);
                    // P[j+1](c)
                    BigDecimal ppc = tmp1.subtract(tmp2, mContext);
                    ppc = ppc.divide(b_j_p_1, mContext);

                    pmc = pc;
                    pc = ppc;
                }
                // Now pc = P[n+1](c) and pmc = P[n](c).
                if (!done) {
                    if (pa.signum() * pc.signum() <= 0) {
                        b = c;
                        pmb = pmc;
                        pb = pc;
                    } else {
                        a = c;
                        pma = pmc;
                        pa = pc;
                    }
                    c = a.add(b, mContext).multiply(oneHalf, mContext);
                }
            }
            final BigDecimal nP = new BigDecimal(numberOfPoints, mContext);
            BigDecimal tmp1 = pmc.subtract(c.multiply(pc, mContext), mContext);
            tmp1 = tmp1.multiply(nP);
            tmp1 = tmp1.pow(2, mContext);
            BigDecimal tmp2 = c.pow(2, mContext);
            tmp2 = BigDecimal.ONE.subtract(tmp2, mContext);
            tmp2 = tmp2.multiply(two, mContext);
            tmp2 = tmp2.divide(tmp1, mContext);

            points[i] = c;
            weights[i] = tmp2;

            final int idx = numberOfPoints - i - 1;
            points[idx] = c.negate(mContext);
            weights[idx] = tmp2;
        }
        // If "numberOfPoints" is odd, 0 is a root.
        if (numberOfPoints % 2 == 1) {
            BigDecimal pmc = BigDecimal.ONE;
            for (int j = 1; j < numberOfPoints; j += 2) {
                final BigDecimal b_j = new BigDecimal(j, mContext);
                final BigDecimal b_j_p_1 = new BigDecimal(j + 1, mContext);

                // pmc = -j * pmc / (j + 1);
                pmc = pmc.multiply(b_j, mContext);
                pmc = pmc.divide(b_j_p_1, mContext);
                pmc = pmc.negate(mContext);
            }

            // 2 / pow(numberOfPoints * pmc, 2);
            final BigDecimal nP = new BigDecimal(numberOfPoints, mContext);
            BigDecimal tmp1 = pmc.multiply(nP, mContext);
            tmp1 = tmp1.pow(2, mContext);
            BigDecimal tmp2 = two.divide(tmp1, mContext);

            points[iMax] = BigDecimal.ZERO;
            weights[iMax] = tmp2;
        }

        return new Pair<BigDecimal[], BigDecimal[]>(points, weights);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15251.java