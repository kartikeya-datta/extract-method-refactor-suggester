error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2727.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2727.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2727.java
text:
```scala
P@@arameters params = new Parameters(context.getConfiguration().get(PFPGrowth.PFP_PARAMETERS, ""));

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

package org.apache.mahout.fpm.pfpgrowth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.mutable.MutableLong;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.Parameters;
import org.apache.mahout.fpm.pfpgrowth.convertors.ContextStatusUpdater;
import org.apache.mahout.fpm.pfpgrowth.convertors.ContextWriteOutputCollector;
import org.apache.mahout.fpm.pfpgrowth.convertors.integer.IntegerStringOutputConverter;
import org.apache.mahout.fpm.pfpgrowth.convertors.string.TopKStringPatterns;
import org.apache.mahout.fpm.pfpgrowth.fpgrowth.FPGrowth;
import org.apache.mahout.fpm.pfpgrowth.fpgrowth.FPTreeDepthCache;
import org.apache.mahout.math.list.IntArrayList;
import org.apache.mahout.math.map.OpenLongObjectHashMap;
import org.apache.mahout.math.map.OpenObjectIntHashMap;

/**
 *  takes each group of transactions and runs Vanilla FPGrowth on it and
 * outputs the the Top K frequent Patterns for each group.
 * 
 */

public class ParallelFPGrowthReducer extends Reducer<LongWritable,TransactionTree,Text,TopKStringPatterns> {
  
  private final List<String> featureReverseMap = new ArrayList<String>();
  
  private final OpenObjectIntHashMap<String> fMap = new OpenObjectIntHashMap<String>();
  
  private final OpenLongObjectHashMap<IntArrayList> groupFeatures = new OpenLongObjectHashMap<IntArrayList>();
  
  private int maxHeapSize = 50;
  
  private int minSupport = 3;
  
  @Override
  protected void reduce(LongWritable key, Iterable<TransactionTree> values, Context context) throws IOException {
    TransactionTree cTree = new TransactionTree();
    int nodes = 0;
    for (TransactionTree tr : values) {
      Iterator<Pair<List<Integer>,Long>> it = tr.getIterator();
      while (it.hasNext()) {
        Pair<List<Integer>,Long> p = it.next();
        nodes += cTree.addPattern(p.getFirst(), p.getSecond());
      }
    }
    
    List<Pair<Integer,Long>> localFList = new ArrayList<Pair<Integer,Long>>();
    for (Entry<Integer,MutableLong> fItem : cTree.generateFList().entrySet()) {
      localFList.add(new Pair<Integer,Long>(fItem.getKey(), fItem.getValue().toLong()));
      
    }
    
    Collections.sort(localFList, new Comparator<Pair<Integer,Long>>() {
      
      @Override
      public int compare(Pair<Integer,Long> o1, Pair<Integer,Long> o2) {
        int ret = o2.getSecond().compareTo(o1.getSecond());
        if (ret != 0) {
          return ret;
        }
        return o1.getFirst().compareTo(o2.getFirst());
      }
      
    });
    
    FPGrowth<Integer> fpGrowth = new FPGrowth<Integer>();
    fpGrowth.generateTopKFrequentPatterns(
        cTree.getIterator(),
        localFList,
        minSupport,
        maxHeapSize,
        new HashSet<Integer>(groupFeatures.get(key.get()).toList()),
        new IntegerStringOutputConverter(
            new ContextWriteOutputCollector<LongWritable,TransactionTree,Text,TopKStringPatterns>(context),
            featureReverseMap),
        new ContextStatusUpdater<LongWritable,TransactionTree,Text,TopKStringPatterns>(context));
  }
  
  @Override
  protected void setup(Context context) throws IOException, InterruptedException {
    
    super.setup(context);
    Parameters params = Parameters.fromString(context.getConfiguration().get(PFPGrowth.PFP_PARAMETERS, ""));
    
    int i = 0;
    for (Pair<String,Long> e : PFPGrowth.deserializeList(params, PFPGrowth.F_LIST, context.getConfiguration())) {
      featureReverseMap.add(e.getFirst());
      fMap.put(e.getFirst(), i++);
      
    }
    
    Map<String,Long> gList = PFPGrowth.deserializeMap(params, PFPGrowth.G_LIST, context.getConfiguration());
    
    for (Entry<String,Long> entry : gList.entrySet()) {
      IntArrayList groupList = groupFeatures.get(entry.getValue());
      Integer itemInteger = fMap.get(entry.getKey());
      if (groupList != null) {
        groupList.add(itemInteger);
      } else {
        groupList = new IntArrayList();
        groupList.add(itemInteger);
        groupFeatures.put(entry.getValue(), groupList);
      }
      
    }
    maxHeapSize = Integer.valueOf(params.get(PFPGrowth.MAX_HEAPSIZE, "50"));
    minSupport = Integer.valueOf(params.get(PFPGrowth.MIN_SUPPORT, "3"));
    FPTreeDepthCache.setFirstLevelCacheSize(Integer.valueOf(params.get(PFPGrowth.TREE_CACHE_SIZE, Integer
        .toString(FPTreeDepthCache.getFirstLevelCacheSize()))));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2727.java