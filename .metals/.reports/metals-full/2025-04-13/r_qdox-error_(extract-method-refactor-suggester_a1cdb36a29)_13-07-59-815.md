error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4998.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4998.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4998.java
text:
```scala
s@@tartTime = timeStamp - sample;

/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2001,2003 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache JMeter" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache JMeter", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package org.apache.jmeter.visualizers;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.math.*;

/**
 *  Title: Apache JMeter Description: Copyright: Copyright (c) 2000 Company:
 *  Apache Foundation
 *
 *@author     Michael Stover
 *@created    February 8, 2001
 *@version    1.0
 */

public class GraphModel implements Clearable, Serializable
{

    private String name;
    private List samples;
    private List listeners;
    private long previous = 0;
    private boolean bigChange = false;
    private Sample current = new Sample(0, 0, 0, 0, 0,false);
    private long startTime = 0;
    private int throughputMax = 20;
    private long graphMax = 20;
    private StatCalculator statCalc = new StatCalculator();

    /**
     *  Constructor for the GraphModel object
     */

    public GraphModel()
    {
        listeners = new LinkedList();
        samples = Collections.synchronizedList(new LinkedList());
    }

    /**
     *  Sets the Name attribute of the GraphModel object
     *
     *@param  name  The new Name value
     */

    public void setName(String name)
    {
        this.name = name;
    }

    /**
     *  Gets the CurrentData attribute of the GraphModel object
     *
     *@return    The CurrentData value
     */
    public long getCurrentData()
    {

        return current.data;
    }

    /**
     *  Gets the CurrentAverage attribute of the GraphModel object
     *
     *@return    The CurrentAverage value
     */
    public long getCurrentAverage()
    {

        return current.average;
    }
    
    public long getCurrentMedian()
    {
        return current.median;
    }

    /**
     *  Gets the CurrentDeviation attribute of the GraphModel object
     *
     *@return    The CurrentDeviation value
     */
    public long getCurrentDeviation()
    {

        return current.deviation;
    }

    public float getCurrentThroughput()
    {
        return current.throughput;
    }

    /**
     *  Gets the SampleCount attribute of the GraphModel object
     *
     *@return    The SampleCount value
     */
    public int getSampleCount()
    {

        return samples.size();
    }

    /**
     *  Gets the Samples attribute of the GraphModel object
     *
     *@return    The Samples value
     */
    public List getSamples()
    {

        return samples;
    }

    /**
     *  Gets the GuiClass attribute of the GraphModel object
     *
     *@return    The GuiClass value
     */

    public Class getGuiClass()
    {

        return GraphVisualizer.class;
    }

    /**
     *  Gets the Name attribute of the GraphModel object
     *
     *@return    The Name value
     */

    public String getName()
    {

        return name;
    }

    /**
     *  Gets the Max attribute of the GraphModel object
     *
     *@return    The Max value
     */
    public long getMaxSample()
    {
        return statCalc.getMax().longValue();
    }

    public long getGraphMax()
    {
        return graphMax;
    }

    public int getThroughputMax()
    {
        return throughputMax;
    }

    /**
     *  Adds a feature to the ModelListener attribute of the GraphModel object
     *
     *@param  modelListener  The feature to be added to the ModelListener attribute
     */
    public void addGraphListener(GraphListener listener)
    {
        listeners.add(listener);
    }

    /**
     *  Adds a feature to the Sample attribute of the GraphModel object
     *
     *@param  e  The feature to be added to the Sample attribute
     *@return    Description of the Returned Value
     */
    public Sample addSample(SampleResult e)
    {
        Sample s = addNewSample(e.getTime(), e.getTimeStamp(), e.isSuccessful());

        fireDataChanged();
        return s;
    }

    /**
     *  Description of the Method
     */
    public void clear()
    {
        samples.clear();
        previous = 0;
        graphMax = 1;
        bigChange = true;
        current = new Sample(0, 0, 0, 0, 0,false);
        statCalc.clear();
        this.fireDataChanged();
    }

    /**
     *  Description of the Method
     */
    protected void fireDataChanged()
    {
        Iterator iter = listeners.iterator();

        if (bigChange)
        {
            while (iter.hasNext())
            {
                ((GraphListener) iter.next()).updateGui();
            }
            bigChange = false;
        }
        else
        {
            quickUpdate(current);
        }
    }

    /**
     *  Description of the Method
     *
     *@param  s  Description of Parameter
     */
    protected void quickUpdate(Sample s)
    {
        Iterator iter = listeners.iterator();
        {
            while (iter.hasNext())
            {
                ((GraphListener) iter.next()).updateGui(s);
            }
        }
    }

    /**
     *  Adds a feature to the NewSample attribute of the GraphModel object
     *
     *@param  sample  The feature to be added to the NewSample attribute
     */
    protected Sample addNewSample(long sample, long timeStamp, boolean success)
    {
        int counter = 0;
        float average;
        long deviation, median;
        synchronized (statCalc)
        {
            statCalc.addValue(sample);
            counter = statCalc.getCount();
            average = (float) statCalc.getMean();
            deviation = (long) statCalc.getStandardDeviation();
            median = statCalc.getMedian().longValue();
        }

        if (samples.size() == 0)
        {
            startTime = timeStamp;
        }

        float throughput = 0;

        if (timeStamp - startTime > 0)
        {
            throughput = (float) (((float) (samples.size() + 1)) / ((float) (timeStamp - startTime)) * 60000);
        }
        if (throughput > throughputMax)
        {
            bigChange = true;
            throughputMax = (int) (throughput * 1.5F);
        }
        if (average > graphMax)
        {
            bigChange = true;
            graphMax = (long) average * 3;
        }
        if (deviation > graphMax)
        {
            bigChange = true;
            graphMax = deviation * 3;
        }
        Sample s = new Sample(sample, (long) average, deviation, throughput, median,!success);

        previous = sample;
        current = s;
        samples.add(s);
        return s;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4998.java