error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2455.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2455.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2455.java
text:
```scala
S@@tring theLabel = ((Pair<?,?>) label).getFirst().toString().split("/")[1];

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

package org.apache.mahout.classifier.naivebayes;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.classifier.naivebayes.training.ThetaMapper;
import org.apache.mahout.classifier.naivebayes.training.TrainNaiveBayesJob;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.sequencefile.PathFilters;
import org.apache.mahout.common.iterator.sequencefile.PathType;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirIterable;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileIterable;
import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.SparseMatrix;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.math.map.OpenObjectIntHashMap;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public final class BayesUtils {

  private BayesUtils() {}

  public static NaiveBayesModel readModelFromDir(Path base, Configuration conf) {

    float alphaI = conf.getFloat(ThetaMapper.ALPHA_I, 1.0f);

    // read feature sums and label sums
    Vector scoresPerLabel = null;
    Vector scoresPerFeature = null;
    for (Pair<Text,VectorWritable> record : new SequenceFileDirIterable<Text, VectorWritable>(
        new Path(base, TrainNaiveBayesJob.WEIGHTS), PathType.LIST, PathFilters.partFilter(), conf)) {
      String key = record.getFirst().toString();
      VectorWritable value = record.getSecond();
      if (key.equals(TrainNaiveBayesJob.WEIGHTS_PER_FEATURE)) {
        scoresPerFeature = value.get();
      } else if (key.equals(TrainNaiveBayesJob.WEIGHTS_PER_LABEL)) {
        scoresPerLabel = value.get();
      }
    }

    Preconditions.checkNotNull(scoresPerFeature);
    Preconditions.checkNotNull(scoresPerLabel);

    Matrix scoresPerLabelAndFeature = new SparseMatrix(scoresPerLabel.size(), scoresPerFeature.size());
    for (Pair<IntWritable,VectorWritable> entry : new SequenceFileDirIterable<IntWritable,VectorWritable>(
        new Path(base, TrainNaiveBayesJob.SUMMED_OBSERVATIONS), PathType.LIST, PathFilters.partFilter(), conf)) {
      scoresPerLabelAndFeature.assignRow(entry.getFirst().get(), entry.getSecond().get());
    }

    Vector perlabelThetaNormalizer = null;
    for (Pair<Text,VectorWritable> entry : new SequenceFileDirIterable<Text,VectorWritable>(
        new Path(base, TrainNaiveBayesJob.THETAS), PathType.LIST, PathFilters.partFilter(), conf)) {
      if (entry.getFirst().toString().equals(TrainNaiveBayesJob.LABEL_THETA_NORMALIZER)) {
        perlabelThetaNormalizer = entry.getSecond().get();
      }
    }

    Preconditions.checkNotNull(perlabelThetaNormalizer);

    return new NaiveBayesModel(scoresPerLabelAndFeature, scoresPerFeature, scoresPerLabel, perlabelThetaNormalizer,
        alphaI);
  }

  /** Write the list of labels into a map file */
  public static int writeLabelIndex(Configuration conf, Iterable<String> labels, Path indexPath)
      throws IOException {
    FileSystem fs = FileSystem.get(indexPath.toUri(), conf);
    SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf, indexPath, Text.class, IntWritable.class);
    int i = 0;
    try {
      for (String label : labels) {
        writer.append(new Text(label), new IntWritable(i++));
      }
    } finally {
      Closeables.closeQuietly(writer);
    }
    return i;
  }

  public static int writeLabelIndex(Configuration conf, Path indexPath,
                                    Iterable<Pair<Text,IntWritable>> labels) throws IOException {
    FileSystem fs = FileSystem.get(indexPath.toUri(), conf);
    SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf, indexPath, Text.class, IntWritable.class);
    Collection<String> seen = new HashSet<String>();
    int i = 0;
    try {
      for (Object label : labels) {
        String theLabel = ((Pair<?,?>) label).getFirst().toString();
        if (!seen.contains(theLabel)) {
          writer.append(new Text(theLabel), new IntWritable(i++));
          seen.add(theLabel);
        }
      }
    } finally {
      Closeables.closeQuietly(writer);
    }
    return i;
  }

  public static Map<Integer, String> readLabelIndex(Configuration conf, Path indexPath) {
    Map<Integer, String> labelMap = new HashMap<Integer, String>();
    for (Pair<Text, IntWritable> pair : new SequenceFileIterable<Text, IntWritable>(indexPath, true, conf)) {
      labelMap.put(pair.getSecond().get(), pair.getFirst().toString());
    }
    return labelMap;
  }

  public static OpenObjectIntHashMap<String> readIndexFromCache(Configuration conf) throws IOException {
    OpenObjectIntHashMap<String> index = new OpenObjectIntHashMap<String>();
    for (Pair<Writable,IntWritable> entry :
         new SequenceFileIterable<Writable,IntWritable>(HadoopUtil.cachedFile(conf), conf)) {
      index.put(entry.getFirst().toString(), entry.getSecond().get());
    }
    return index;
  }

  public static Map<String,Vector> readScoresFromCache(Configuration conf) throws IOException {
    Map<String,Vector> sumVectors = Maps.newHashMap();
    for (Pair<Text,VectorWritable> entry :
         new SequenceFileDirIterable<Text,VectorWritable>(HadoopUtil.cachedFile(conf),
        PathType.LIST, PathFilters.partFilter(), conf)) {
      sumVectors.put(entry.getFirst().toString(), entry.getSecond().get());
    }
    return sumVectors;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2455.java