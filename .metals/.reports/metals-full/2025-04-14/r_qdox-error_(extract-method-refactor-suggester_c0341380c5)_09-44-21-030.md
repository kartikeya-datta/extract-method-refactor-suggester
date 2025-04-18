error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1405.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1405.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1405.java
text:
```scala
C@@onfiguration conf = getConfiguration();

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

package org.apache.mahout.math.hadoop.similarity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.clustering.ClusteringTestUtils;
import org.apache.mahout.common.DummyOutputCollector;
import org.apache.mahout.common.MahoutTestCase;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.StringTuple;
import org.apache.mahout.common.commandline.DefaultOptionCreator;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileIterable;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TestVectorDistanceSimilarityJob extends MahoutTestCase {

  private FileSystem fs;

  private static final double[][] REFERENCE = { { 1, 1 }, { 2, 1 }, { 1, 2 }, { 2, 2 }, { 3, 3 }, { 4, 4 }, { 5, 4 },
      { 4, 5 }, { 5, 5 } };

  private static final double[][] SEEDS = { { 1, 1 }, { 10, 10 } };

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    fs = FileSystem.get(getConfiguration());
  }

  @Test
  public void testVectorDistanceMapper() throws Exception {
    Mapper<WritableComparable<?>, VectorWritable, StringTuple, DoubleWritable>.Context context =
            EasyMock.createMock(Mapper.Context.class);
    StringTuple tuple = new StringTuple();
    tuple.add("foo");
    tuple.add("123");
    context.write(tuple, new DoubleWritable(Math.sqrt(2.0)));
    tuple = new StringTuple();
    tuple.add("foo2");
    tuple.add("123");
    context.write(tuple, new DoubleWritable(1));

    EasyMock.replay(context);

    Vector vector = new RandomAccessSparseVector(2);
    vector.set(0, 2);
    vector.set(1, 2);

    VectorDistanceMapper mapper = new VectorDistanceMapper();
    setField(mapper, "measure", new EuclideanDistanceMeasure());
    Collection<NamedVector> seedVectors = Lists.newArrayList();
    Vector seed1 = new RandomAccessSparseVector(2);
    seed1.set(0, 1);
    seed1.set(1, 1);
    Vector seed2 = new RandomAccessSparseVector(2);
    seed2.set(0, 2);
    seed2.set(1, 1);

    seedVectors.add(new NamedVector(seed1, "foo"));
    seedVectors.add(new NamedVector(seed2, "foo2"));
    setField(mapper, "seedVectors", seedVectors);

    mapper.map(new IntWritable(123), new VectorWritable(vector), context);

    EasyMock.verify(context);
  }

  @Test
  public void testVectorDistanceInvertedMapper() throws Exception {
     Mapper<WritableComparable<?>, VectorWritable, Text, VectorWritable>.Context context =
            EasyMock.createMock(Mapper.Context.class);
    Vector expectVec = new DenseVector(new double[]{Math.sqrt(2.0), 1.0});
    context.write(new Text("other"), new VectorWritable(expectVec));
    EasyMock.replay(context);
    Vector vector = new NamedVector(new RandomAccessSparseVector(2), "other");
    vector.set(0, 2);
    vector.set(1, 2);

    VectorDistanceInvertedMapper mapper = new VectorDistanceInvertedMapper();
    setField(mapper, "measure", new EuclideanDistanceMeasure());
    Collection<NamedVector> seedVectors = Lists.newArrayList();
    Vector seed1 = new RandomAccessSparseVector(2);
    seed1.set(0, 1);
    seed1.set(1, 1);
    Vector seed2 = new RandomAccessSparseVector(2);
    seed2.set(0, 2);
    seed2.set(1, 1);

    seedVectors.add(new NamedVector(seed1, "foo"));
    seedVectors.add(new NamedVector(seed2, "foo2"));
    setField(mapper, "seedVectors", seedVectors);

    mapper.map(new IntWritable(123), new VectorWritable(vector), context);

    EasyMock.verify(context);

  }

  @Test
  public void testRun() throws Exception {
    Path input = getTestTempDirPath("input");
    Path output = getTestTempDirPath("output");
    Path seedsPath = getTestTempDirPath("seeds");

    List<VectorWritable> points = getPointsWritable(REFERENCE);
    List<VectorWritable> seeds = getPointsWritable(SEEDS);

    Configuration conf = getConfiguration();
    ClusteringTestUtils.writePointsToFile(points, true, new Path(input, "file1"), fs, conf);
    ClusteringTestUtils.writePointsToFile(seeds, true, new Path(seedsPath, "part-seeds"), fs, conf);

    String[] args = { optKey(DefaultOptionCreator.INPUT_OPTION), input.toString(),
        optKey(VectorDistanceSimilarityJob.SEEDS), seedsPath.toString(), optKey(DefaultOptionCreator.OUTPUT_OPTION),
        output.toString(), optKey(DefaultOptionCreator.DISTANCE_MEASURE_OPTION),
        EuclideanDistanceMeasure.class.getName() };

    ToolRunner.run(getConfiguration(), new VectorDistanceSimilarityJob(), args);

    int expectedOutputSize = SEEDS.length * REFERENCE.length;
    int outputSize = Iterables.size(new SequenceFileIterable<StringTuple, DoubleWritable>(new Path(output,
        "part-m-00000"), conf));
    assertEquals(expectedOutputSize, outputSize);
  }

  @Test
  public void testMaxDistance() throws Exception {

    Path input = getTestTempDirPath("input");
    Path output = getTestTempDirPath("output");
    Path seedsPath = getTestTempDirPath("seeds");

    List<VectorWritable> points = getPointsWritable(REFERENCE);
    List<VectorWritable> seeds = getPointsWritable(SEEDS);

    Configuration conf = new Configuration();
    ClusteringTestUtils.writePointsToFile(points, true, new Path(input, "file1"), fs, conf);
    ClusteringTestUtils.writePointsToFile(seeds, true, new Path(seedsPath, "part-seeds"), fs, conf);

    double maxDistance = 10;

    String[] args = { optKey(DefaultOptionCreator.INPUT_OPTION), input.toString(),
        optKey(VectorDistanceSimilarityJob.SEEDS), seedsPath.toString(), optKey(DefaultOptionCreator.OUTPUT_OPTION),
        output.toString(), optKey(DefaultOptionCreator.DISTANCE_MEASURE_OPTION),
        EuclideanDistanceMeasure.class.getName(),
        optKey(VectorDistanceSimilarityJob.MAX_DISTANCE), String.valueOf(maxDistance) };

    ToolRunner.run(getConfiguration(), new VectorDistanceSimilarityJob(), args);

    int outputSize = 0;

    for (Pair<StringTuple, DoubleWritable> record : new SequenceFileIterable<StringTuple, DoubleWritable>(
        new Path(output, "part-m-00000"), conf)) {
      outputSize++;
      assertTrue(record.getSecond().get() <= maxDistance);
    }

    assertEquals(14, outputSize);
  }

  @Test
  public void testRunInverted() throws Exception {
    Path input = getTestTempDirPath("input");
    Path output = getTestTempDirPath("output");
    Path seedsPath = getTestTempDirPath("seeds");
    List<VectorWritable> points = getPointsWritable(REFERENCE);
    List<VectorWritable> seeds = getPointsWritable(SEEDS);
    Configuration conf = getConfiguration();
    ClusteringTestUtils.writePointsToFile(points, true, new Path(input, "file1"), fs, conf);
    ClusteringTestUtils.writePointsToFile(seeds, true, new Path(seedsPath, "part-seeds"), fs, conf);
    String[] args = {optKey(DefaultOptionCreator.INPUT_OPTION), input.toString(),
        optKey(VectorDistanceSimilarityJob.SEEDS), seedsPath.toString(), optKey(DefaultOptionCreator.OUTPUT_OPTION),
        output.toString(), optKey(DefaultOptionCreator.DISTANCE_MEASURE_OPTION),
        EuclideanDistanceMeasure.class.getName(),
        optKey(VectorDistanceSimilarityJob.OUT_TYPE_KEY), "v"
    };
    ToolRunner.run(getConfiguration(), new VectorDistanceSimilarityJob(), args);

    DummyOutputCollector<Text, VectorWritable> collector = new DummyOutputCollector<Text, VectorWritable>();

    for (Pair<Text, VectorWritable> record :  new SequenceFileIterable<Text, VectorWritable>(
        new Path(output, "part-m-00000"), conf)) {
      collector.collect(record.getFirst(), record.getSecond());
    }
    assertEquals(REFERENCE.length, collector.getData().size());
    for (Map.Entry<Text, List<VectorWritable>> entry : collector.getData().entrySet()) {
      assertEquals(SEEDS.length, entry.getValue().iterator().next().get().size());
    }
  }

  private static List<VectorWritable> getPointsWritable(double[][] raw) {
    List<VectorWritable> points = Lists.newArrayList();
    for (double[] fr : raw) {
      Vector vec = new RandomAccessSparseVector(fr.length);
      vec.assign(fr);
      points.add(new VectorWritable(vec));
    }
    return points;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1405.java