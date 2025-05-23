error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2732.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2732.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2732.java
text:
```scala
P@@arameters params = new Parameters(job.get("bayes.parameters", ""));

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.mahout.classifier.bayes.mapreduce.common;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.mahout.common.Parameters;
import org.apache.mahout.common.StringTuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Can also be used as a local Combiner */
public class BayesWeightSummerReducer extends MapReduceBase implements
    Reducer<StringTuple,DoubleWritable,StringTuple,DoubleWritable> {
  
  private static final Logger log = LoggerFactory.getLogger(BayesWeightSummerReducer.class);
  
  private HTable table;
  
  private boolean useHbase;
  
  @Override
  public void reduce(StringTuple key,
                     Iterator<DoubleWritable> values,
                     OutputCollector<StringTuple,DoubleWritable> output,
                     Reporter reporter) throws IOException {
    // Key is label,word, value is the tfidf of the feature of times we've seen
    // this label word per local node. Output is the same
    
    double sum = 0.0;
    while (values.hasNext()) {
      reporter.setStatus("Weight Summer Reducer: " + key);
      sum += values.next().get();
    }
    reporter.setStatus("Bayes Weight Summer Reducer: " + key + " => " + sum);
    // char firstChar = key.toString().charAt(0);
    if (useHbase) {
      if (key.stringAt(0).equals(BayesConstants.FEATURE_SUM)) { // sum of weight
        // for all
        // labels for a
        // feature
        // Sigma_j
        String feature = key.stringAt(1);
        
        Put bu = new Put(Bytes.toBytes(feature));
        bu.add(Bytes.toBytes(BayesConstants.HBASE_COLUMN_FAMILY), Bytes.toBytes(BayesConstants.FEATURE_SUM),
          Bytes.toBytes(sum));
        table.put(bu);
        
      } else if (key.stringAt(0).equals(BayesConstants.LABEL_SUM)) {
        String label = key.stringAt(1);
        Put bu = new Put(Bytes.toBytes(BayesConstants.LABEL_SUM));
        bu.add(Bytes.toBytes(BayesConstants.HBASE_COLUMN_FAMILY), Bytes.toBytes(label), Bytes.toBytes(sum));
        table.put(bu);
      } else if (key.stringAt(0).equals(BayesConstants.TOTAL_SUM)) {
        Put bu = new Put(Bytes.toBytes(BayesConstants.HBASE_COUNTS_ROW));
        bu.add(Bytes.toBytes(BayesConstants.HBASE_COLUMN_FAMILY), Bytes.toBytes(BayesConstants.TOTAL_SUM),
          Bytes.toBytes(sum));
        table.put(bu);
      }
    }
    
    output.collect(key, new DoubleWritable(sum));
  }
  
  @Override
  public void configure(JobConf job) {
    try {
      Parameters params = Parameters.fromString(job.get("bayes.parameters", ""));
      if (params.get("dataSource").equals("hbase")) {
        useHbase = true;
      } else {
        return;
      }
      
      HBaseConfiguration hBconf = new HBaseConfiguration(job);
      table = new HTable(hBconf, job.get("output.table"));
    } catch (IOException e) {
      log.error("Unexpected error during configuration", e);
    }
    
  }
  
  @Override
  public void close() throws IOException {
    if (useHbase) {
      table.close();
    }
    super.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2732.java