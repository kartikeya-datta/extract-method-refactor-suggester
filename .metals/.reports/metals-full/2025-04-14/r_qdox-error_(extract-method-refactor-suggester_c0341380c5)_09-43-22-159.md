error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13372.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13372.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13372.java
text:
```scala
private static final l@@ong serialVersionUID = -370076995648386763L;

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
package org.apache.commons.math.stat.descriptive.summary;

import java.io.Serializable;

import org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic;

/**
 * Returns the sum of the natural logs for this collection of values.  
 * <p>
 * Uses {@link java.lang.Math#log(double)} to compute the logs.  Therefore,
 * <ul>
 * <li>If any of values are < 0, the result is <code>NaN.</code></li>
 * <li>If all values are non-negative and less than 
 * <code>Double.POSITIVE_INFINITY</code>,  but at least one value is 0, the
 * result is <code>Double.NEGATIVE_INFINITY.</code></li>
 * <li>If both <code>Double.POSITIVE_INFINITY</code> and 
 * <code>Double.NEGATIVE_INFINITY</code> are among the values, the result is
 * <code>NaN.</code></li>
 * </ul>
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> If 
 * multiple threads access an instance of this class concurrently, and at least
 * one of the threads invokes the <code>increment()</code> or 
 * <code>clear()</code> method, it must be synchronized externally.
 * 
 * @version $Revision$ $Date$
 */
public class SumOfLogs extends AbstractStorelessUnivariateStatistic implements Serializable {

    /** Serializable version identifier */
    static final long serialVersionUID = -370076995648386763L;    

    /**Number of values that have been added */
    private int n;
    
    /**
     * The currently running value
     */
    private double value;
    
    /**
     * Create a SumOfLogs instance
     */
    public SumOfLogs() {
       value = 0d;
       n = 0;
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic#increment(double)
     */
    public void increment(final double d) {
        value += Math.log(d);
        n++;
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic#getResult()
     */
    public double getResult() {
        if (n > 0) {
            return value;
        } else {
            return Double.NaN;
        }
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic#getN()
     */
    public long getN() {
        return n;
    }
    
    /**
     * @see org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic#clear()
     */
    public void clear() {
        value = 0d;
        n = 0;
    }

    /**
     * Returns the sum of the natural logs of the entries in the specified portion of
     * the input array, or <code>Double.NaN</code> if the designated subarray
     * is empty.
     * <p>
     * Throws <code>IllegalArgumentException</code> if the array is null.
     * <p>
     * See {@link SumOfLogs}.
     * 
     * @param values the input array
     * @param begin index of the first array element to include
     * @param length the number of elements to include
     * @return the sum of the natural logs of the values or Double.NaN if 
     * length = 0
     * @throws IllegalArgumentException if the array is null or the array index
     *  parameters are not valid
     */
    public double evaluate(final double[] values, final int begin, final int length) {
        double sumLog = Double.NaN;
        if (test(values, begin, length)) {
            sumLog = 0.0;
            for (int i = begin; i < begin + length; i++) {
                sumLog += Math.log(values[i]);
            }
        }
        return sumLog;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13372.java