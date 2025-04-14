error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7279.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7279.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7279.java
text:
```scala
l@@ist.add(Double.valueOf(v));

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
package org.apache.commons.math.stat.descriptive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.MathException;
import org.apache.commons.math.stat.descriptive.UnivariateStatistic;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.util.DefaultTransformer;
import org.apache.commons.math.util.NumberTransformer;

/**
 * @version $Revision$ $Date$
 */
public class ListUnivariateImpl extends DescriptiveStatistics implements Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = -8837442489133392138L;
    
    /**
     * Holds a reference to a list - GENERICs are going to make
     * our lives easier here as we could only accept List<Number>
     */
    protected List<Object> list;

    /** Number Transformer maps Objects to Number for us. */
    protected NumberTransformer transformer;

    /**
     * No argument Constructor
     */
    public ListUnivariateImpl(){
        this(new ArrayList<Object>());
    }
    
    /**
     * Construct a ListUnivariate with a specific List.
     * @param list The list that will back this DescriptiveStatistics
     */
    public ListUnivariateImpl(List<Object> list) {
        this(list, new DefaultTransformer());
    }
    
    /**
     * Construct a ListUnivariate with a specific List.
     * @param list The list that will back this DescriptiveStatistics
     * @param transformer the number transformer used to convert the list items.
     */
    public ListUnivariateImpl(List<Object> list, NumberTransformer transformer) {
        super();
        this.list = list;
        this.transformer = transformer;
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.DescriptiveStatistics#getValues()
     */
    public double[] getValues() {

        int length = list.size();

        // If the window size is not INFINITE_WINDOW AND
        // the current list is larger that the window size, we need to
        // take into account only the last n elements of the list
        // as definied by windowSize

        if (windowSize != DescriptiveStatistics.INFINITE_WINDOW &&
            windowSize < list.size())
        {
            length = list.size() - Math.max(0, list.size() - windowSize);
        }

        // Create an array to hold all values
        double[] copiedArray = new double[length];

        for (int i = 0; i < copiedArray.length; i++) {
            copiedArray[i] = getElement(i);
        }
        return copiedArray;
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.DescriptiveStatistics#getElement(int)
     */
    public double getElement(int index) {

        double value = Double.NaN;

        int calcIndex = index;

        if (windowSize != DescriptiveStatistics.INFINITE_WINDOW &&
            windowSize < list.size())
        {
            calcIndex = (list.size() - windowSize) + index;
        }

        
        try {
            value = transformer.transform(list.get(calcIndex));
        } catch (MathException e) {
            e.printStackTrace();
        }
        
        return value;
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.DescriptiveStatistics#getN()
     */
    public long getN() {
        int n = 0;

        if (windowSize != DescriptiveStatistics.INFINITE_WINDOW) {
            if (list.size() > windowSize) {
                n = windowSize;
            } else {
                n = list.size();
            }
        } else {
            n = list.size();
        }
        return n;
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.DescriptiveStatistics#addValue(double)
     */
    public void addValue(double v) {
        list.add(new Double(v));
    }
    
    /**
     * Adds an object to this list. 
     * @param o Object to add to the list
     */
    public void addObject(Object o) {
        list.add(o);
    }

    /**
     * Clears all statistics.
     * <p>
     * <strong>N.B.: </strong> This method has the side effect of clearing the underlying list.
     */
    public void clear() {
        list.clear();
    }
    
    /**
     * Apply the given statistic to this univariate collection.
     * @param stat the statistic to apply
     * @return the computed value of the statistic.
     */
    public double apply(UnivariateStatistic stat) {
        double[] v = this.getValues();

        if (v != null) {
            return stat.evaluate(v, 0, v.length);
        }
        return Double.NaN;
    }
    
    /**
     * Access the number transformer.
     * @return the number transformer.
     */
    public NumberTransformer getTransformer() {
        return transformer;
    }

    /**
     * Modify the number transformer.
     * @param transformer the new number transformer.
     */
    public void setTransformer(NumberTransformer transformer) {
        this.transformer = transformer;
    }
    
    /**
     * @see org.apache.commons.math.stat.descriptive.DescriptiveStatistics#setWindowSize(int)
     */
    public synchronized void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
        //Discard elements from the front of the list if the windowSize is less than 
        // the size of the list.
        int extra = list.size() - windowSize;
        for (int i = 0; i < extra; i++) {
            list.remove(0);
        }
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.DescriptiveStatistics#getWindowSize
     */
    public synchronized int getWindowSize() {
        return windowSize;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7279.java