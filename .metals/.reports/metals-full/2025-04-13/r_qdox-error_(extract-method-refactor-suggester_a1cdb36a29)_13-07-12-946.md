error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14826.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14826.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 884
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14826.java
text:
```scala
public class SynchronizedDescriptiveStatistics extends DescriptiveStatistics {

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
p@@ackage org.apache.commons.math.stat.descriptive;

/**
 * Implementation of
 * {@link org.apache.commons.math.stat.descriptive.DescriptiveStatistics} that
 * is safe to use in a mulithreaded environment.  Multiple threads can safely
 * operate on a single instance without causing runtime exceptions due to race
 * conditions.  In effect, this implementation makes modification and access
 * methods atomic operations for a signle instance.  That is to say, as one
 * thread is computing a statistic from the instance, no other thread can modify
 * the instance nor compute another statistic. 
 *
 * @since 1.2
 * @version $Revision$ $Date$
 */
public class SynchronizedDescriptiveStatistics extends DescriptiveStatisticsImpl {

    /** Serialization UID */
    private static final long serialVersionUID = 1L;

    /**
     * Construct an instance with infinite window
     */
    public SynchronizedDescriptiveStatistics() {
        this(INFINITE_WINDOW);
    }

    /**
     * Construct an instance with finite window
     * @param window the finite window size.
     */
    public SynchronizedDescriptiveStatistics(int window) {
        super(window);
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.DescriptiveStatistics#addValue(double)
     */
    public synchronized void addValue(double v) {
        super.addValue(v);
    }

    /**
     * Apply the given statistic to this univariate collection.
     * @param stat the statistic to apply
     * @return the computed value of the statistic.
     */
    public synchronized double apply(UnivariateStatistic stat) {
        return super.apply(stat);
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.DescriptiveStatistics#clear()
     */
    public synchronized void clear() {
        super.clear();
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.DescriptiveStatistics#getElement(int)
     */
    public synchronized double getElement(int index) {
        return super.getElement(index);
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.DescriptiveStatistics#getN()
     */
    public synchronized long getN() {
        return super.getN();
    }

    /** 
     * Returns the standard deviation of the available values.
     * @return The standard deviation, Double.NaN if no values have been added 
     * or 0.0 for a single value set. 
     */
    public synchronized double getStandardDeviation() {
        return super.getStandardDeviation();
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.DescriptiveStatistics#getValues()
     */
    public synchronized double[] getValues() {
        return super.getValues();
    }

    /**
     * Access the window size.
     * @return the current window size.
     */
    public synchronized int getWindowSize() {
        return super.getWindowSize();
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.DescriptiveStatistics#setWindowSize(int)
     */
    public synchronized void setWindowSize(int windowSize) {
        super.setWindowSize(windowSize);
    }

    /**
     * Generates a text report displaying univariate statistics from values
     * that have been added.  Each statistic is displayed on a separate
     * line.
     * 
     * @return String with line feeds displaying statistics
     */
    public synchronized String toString() {
        return super.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14826.java