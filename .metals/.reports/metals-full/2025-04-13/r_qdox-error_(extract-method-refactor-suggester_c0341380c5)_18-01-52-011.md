error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7369.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7369.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7369.java
text:
```scala
r@@ow = new SamplingStatCalculator(url);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.apache.jmeter.testelement;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.jmeter.report.DataSet;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.SamplingStatCalculator;

/**
 *
 * The purpose of TableData is to contain the results of a single .jtl file.
 * It is equivalent to what the AggregateListener table. A HashMap is used
 * to store the data. The URL is the key and the value is SamplingStatCalculator
 */
public class JTLData implements Serializable, DataSet {

    protected final HashMap<String, SamplingStatCalculator> data = new HashMap<String, SamplingStatCalculator>();
    protected String jtl_file = null;
    protected long startTimestamp = 0;
    protected long endTimestamp = 0;
    protected File inputFile = null;

    /**
     *
     */
    public JTLData() {
        super();
    }

    /**
     * Return a Set of the URLs
     * @return set of URLs
     */
    public Set<?> getURLs() {
        return this.data.keySet();
    }

    /**
     * Return a Set of the values
     * @return values
     */
    public Set<SamplingStatCalculator>  getStats() {
        return (Set<SamplingStatCalculator>) this.data.values();
    }

    /**
     * The purpose of the method is to make it convienant to pass a list
     * of the URLs and return a list of the SamplingStatCalculators. If
     * no URLs match, the list is empty.
     * TODO - this method seems to be wrong - it does not agree with the Javadoc
     * The SamplingStatCalculators will be returned in the same sequence
     * as the url list.
     * @param urls
     * @return array list of non-null entries (may be empty)
     */
    @SuppressWarnings("unchecked") // Method is broken anyway
    public List getStats(List urls) {
        ArrayList items = new ArrayList();
        Iterator itr = urls.iterator();
        if (itr.hasNext()) {
            SamplingStatCalculator row = (SamplingStatCalculator)itr.next();
            if (row != null) {
                items.add(row);
            }
        }
        return items;
    }

    public void setDataSource(String absolutePath) {
        this.jtl_file = absolutePath;
    }

    public String getDataSource() {
        return this.jtl_file;
    }

    public String getDataSourceName() {
        if (inputFile == null) {
            inputFile = new File(getDataSource());
        }
        return inputFile.getName().substring(0,inputFile.getName().length() - 4);
    }

    public void setStartTimestamp(long stamp) {
        this.startTimestamp = stamp;
    }

    public long getStartTimestamp() {
        return this.startTimestamp;
    }

    public void setEndTimestamp(long stamp) {
        this.endTimestamp = stamp;
    }

    public long getEndTimestamp() {
        return this.endTimestamp;
    }

    /**
     * The date we use for the result is the start timestamp. The
     * reasoning is that a test may run for a long time, but it
     * is most likely scheduled to run using CRON on unix or
     * scheduled task in windows.
     * @return start time
     */
    public Date getDate() {
        return new Date(this.startTimestamp);
    }

    public String getMonthDayDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(this.startTimestamp);
        return String.valueOf(cal.get(Calendar.MONTH)) + " - " +
        String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
    }

    public String getMonthDayYearDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(this.startTimestamp);
        return String.valueOf(cal.get(Calendar.MONTH)) + " - " +
            String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + " - " +
            String.valueOf(cal.get(Calendar.YEAR));
    }

    /**
     * The method will SamplingStatCalculator for the given URL. If the URL
     * doesn't exist, the method returns null.
     * @param url
     * @return data for this URL
     */
    public SamplingStatCalculator getStatistics(String url) {
        if (this.data.containsKey(url)) {
            return this.data.get(url);
        } else {
            return null;
        }
    }

    /**
     * The implementation loads a single .jtl file and cleans up the
     * ResultCollector.
     */
    public void loadData() {
        if (this.getDataSource() != null) {
            ResultCollector rc = new ResultCollector();
            rc.setFilename(this.getDataSource());
            rc.setListener(this);
            rc.loadExistingFile();
            // we clean up the ResultCollector to make sure there's
            // no slow leaks
            rc.clear();
            rc.setListener(null);
            rc = null;
        }
    }

    /**
     * the implementation will set the start timestamp if the HashMap
     * is empty. otherwise it will set the end timestamp using the
     * end time
     */
    public void add(SampleResult sample) {
        if (data.size() == 0) {
            this.startTimestamp = sample.getStartTime();
        } else {
            this.endTimestamp = sample.getEndTime();
        }
        // now add the samples to the HashMap
        String url = sample.getSampleLabel();
        if (url == null) {
            url = sample.getURL().toString();
        }
        SamplingStatCalculator row = data.get(url);
        if (row == null) {
            row = new SamplingStatCalculator();
            // just like the aggregate listener, we use the sample label to represent
            // a row. in this case, we use it as a key.
            this.data.put(url,row);
        }
        row.addSample(sample);
    }

    /**
     * By default, the method always returns true. Subclasses can over
     * ride the implementation.
     */
    public boolean isStats() {
        return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7369.java