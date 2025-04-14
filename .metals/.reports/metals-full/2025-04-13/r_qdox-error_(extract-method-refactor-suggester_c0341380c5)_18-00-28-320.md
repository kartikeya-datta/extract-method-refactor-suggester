error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13566.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13566.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13566.java
text:
```scala
public M@@edian withEstimationType(final EstimationType newEstimationType) {

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
package org.apache.commons.math3.stat.descriptive.rank;

import java.io.Serializable;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.descriptive.rank.Percentile.PivotingStrategy;
import org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType;
import org.apache.commons.math3.stat.ranking.NaNStrategy;


/**
 * Returns the median of the available values.  This is the same as the 50th percentile.
 * See {@link Percentile} for a description of the algorithm used.
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> If
 * multiple threads access an instance of this class concurrently, and at least
 * one of the threads invokes the <code>increment()</code> or
 * <code>clear()</code> method, it must be synchronized externally.</p>
 *
 * @version $Id$
 */
public class Median extends Percentile implements Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = -3961477041290915687L;

    /** Fixed quantile. */
    private static final double FIXED_QUANTILE_50 = 50.0;

    /**
     * Default constructor.
     */
    public Median() {
        // No try-catch or advertised exception - arg is valid
        super(FIXED_QUANTILE_50);
    }

    /**
     * Copy constructor, creates a new {@code Median} identical
     * to the {@code original}
     *
     * @param original the {@code Median} instance to copy
     * @throws NullArgumentException if original is null
     */
    public Median(Median original) throws NullArgumentException {
        super(original);
    }

    /**
     * Constructs a Median with the specific {@link EstimationType}, {@link NaNStrategy} and {@link PivotingStrategy}.
     *
     * @param estimationType one of the percentile {@link EstimationType  estimation types}
     * @param nanStrategy one of {@link NaNStrategy} to handle with NaNs
     * @param pivotingStrategy strategy to use for pivoting during search
     * @throws MathIllegalArgumentException if p is not within (0,100]
     * @throws NullArgumentException if type or NaNStrategy passed is null
     */
    private Median(final EstimationType estimationType, final NaNStrategy nanStrategy,
                   final PivotingStrategy pivotingStrategy)
        throws MathIllegalArgumentException {
        super(FIXED_QUANTILE_50, estimationType, nanStrategy, pivotingStrategy);
    }

    /** {@inheritDoc} */
    @Override
    public Median withEstimationtype(final EstimationType newEstimationType) {
        return new Median(newEstimationType, getNaNStrategy(), getPivotingStrategy());
    }

    /** {@inheritDoc} */
    @Override
    public Median withNaNStrategy(final NaNStrategy newNaNStrategy) {
        return new Median(getEstimationType(), newNaNStrategy, getPivotingStrategy());
    }

    /** {@inheritDoc} */
    @Override
    public Median withPivotingStrategy(final PivotingStrategy newPivotingStrategy) {
        return new Median(getEstimationType(), getNaNStrategy(), newPivotingStrategy);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13566.java