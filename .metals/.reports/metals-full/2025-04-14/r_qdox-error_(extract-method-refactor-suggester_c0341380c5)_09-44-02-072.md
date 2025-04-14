error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/71.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/71.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/71.java
text:
```scala
i@@f (userVector.get(index) == 0.0) {

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

package org.apache.mahout.cf.taste.hadoop.item;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.hadoop.MapFilesMap;
import org.apache.mahout.cf.taste.hadoop.RecommendedItemsWritable;
import org.apache.mahout.cf.taste.impl.common.Cache;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.Retriever;
import org.apache.mahout.cf.taste.impl.recommender.GenericRecommendedItem;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.common.FileLineIterable;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public final class RecommenderMapper
    extends MapReduceBase
    implements Mapper<LongWritable, VectorWritable, LongWritable, RecommendedItemsWritable> {

  static final String COOCCURRENCE_PATH = "cooccurrencePath";
  static final String ITEMID_INDEX_PATH = "itemIDIndexPath";
  static final String RECOMMENDATIONS_PER_USER = "recommendationsPerUser";
  static final String USERS_FILE = "usersFile";

  private int recommendationsPerUser;
  private MapFilesMap<IntWritable,LongWritable> indexItemIDMap;
  private MapFilesMap<IntWritable, VectorWritable> cooccurrenceColumnMap;
  private Cache<IntWritable,Vector> cooccurrenceColumnCache;
  private FastIDSet usersToRecommendFor;

  @Override
  public void configure(JobConf jobConf) {
    try {
      FileSystem fs = FileSystem.get(jobConf);
      Path cooccurrencePath = new Path(jobConf.get(COOCCURRENCE_PATH)).makeQualified(fs);
      Path itemIDIndexPath = new Path(jobConf.get(ITEMID_INDEX_PATH)).makeQualified(fs);
      recommendationsPerUser = jobConf.getInt(RECOMMENDATIONS_PER_USER, 10);
      indexItemIDMap = new MapFilesMap<IntWritable,LongWritable>(fs, itemIDIndexPath, new Configuration());
      cooccurrenceColumnMap = new MapFilesMap<IntWritable,VectorWritable>(fs, cooccurrencePath, new Configuration());
      String usersFilePathString = jobConf.get(USERS_FILE);
      if (usersFilePathString == null) {
        usersToRecommendFor = null;
      } else {
        usersToRecommendFor = new FastIDSet();
        Path usersFilePath = new Path(usersFilePathString).makeQualified(fs);
        FSDataInputStream in = fs.open(usersFilePath);
        for (String line : new FileLineIterable(in)) {
          usersToRecommendFor.add(Long.parseLong(line));
        }
      }
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe);
    }
    cooccurrenceColumnCache = new Cache<IntWritable,Vector>(new CooccurrenceCache(cooccurrenceColumnMap), 100);
  }

  @Override
  public void map(LongWritable userID,
                  VectorWritable vectorWritable,
                  OutputCollector<LongWritable, RecommendedItemsWritable> output,
                  Reporter reporter) throws IOException {

    if (usersToRecommendFor != null && !usersToRecommendFor.contains(userID.get())) {
      return;
    }
    Vector userVector = vectorWritable.get();
    Iterator<Vector.Element> userVectorIterator = userVector.iterateNonZero();
    Vector recommendationVector = new RandomAccessSparseVector(Integer.MAX_VALUE, 1000);
    while (userVectorIterator.hasNext()) {
      Vector.Element element = userVectorIterator.next();
      int index = element.index();
      double value = element.get();
      Vector columnVector;
      try {
        columnVector = cooccurrenceColumnCache.get(new IntWritable(index));
      } catch (TasteException te) {
        if (te.getCause() instanceof IOException) {
          throw (IOException) te.getCause();
        } else {
          throw new IOException(te.getCause());
        }
      }
      columnVector.times(value).addTo(recommendationVector);
    }

    Queue<RecommendedItem> topItems =
      new PriorityQueue<RecommendedItem>(recommendationsPerUser + 1, Collections.reverseOrder());

    Iterator<Vector.Element> recommendationVectorIterator = recommendationVector.iterateNonZero();
    LongWritable itemID = new LongWritable();
    while (recommendationVectorIterator.hasNext()) {
      Vector.Element element = recommendationVectorIterator.next();
      int index = element.index();
      if (userVector.get(index) != 0.0) {
        if (topItems.size() < recommendationsPerUser) {
          indexItemIDMap.get(new IntWritable(index), itemID);
          topItems.add(new GenericRecommendedItem(itemID.get(), (float) element.get()));
        } else if (element.get() > topItems.peek().getValue()) {
          indexItemIDMap.get(new IntWritable(index), itemID);
          topItems.add(new GenericRecommendedItem(itemID.get(), (float) element.get()));
          topItems.poll();
        }
      }
    }

    List<RecommendedItem> recommendations = new ArrayList<RecommendedItem>(topItems.size());
    recommendations.addAll(topItems);
    Collections.sort(recommendations);
    output.collect(userID, new RecommendedItemsWritable(recommendations));
  }

  @Override
  public void close() {
    indexItemIDMap.close();
    cooccurrenceColumnMap.close();
  }

  private static class CooccurrenceCache implements Retriever<IntWritable,Vector> {

    private final MapFilesMap<IntWritable,VectorWritable> map;
    private VectorWritable columnVector;

    private CooccurrenceCache(MapFilesMap<IntWritable,VectorWritable> map) {
      this.map = map;
      columnVector = new VectorWritable();
      columnVector.set(new RandomAccessSparseVector(Integer.MAX_VALUE, 1000));
    }

    @Override
    public Vector get(IntWritable key) throws TasteException {
      Vector value;
      try {
        value = map.get(key, columnVector).get();
      } catch (IOException ioe) {
        throw new TasteException(ioe);
      }
      if (value == null) {
        return null;
      }
      columnVector = new VectorWritable();
      columnVector.set(new RandomAccessSparseVector(Integer.MAX_VALUE, 1000));
      return value;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/71.java