error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1871.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1871.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1871.java
text:
```scala
F@@uzzyKMeansDriver.main(args);

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

package org.apache.mahout.clustering.fuzzykmeans;

import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DataInputBuffer;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.clustering.ClusterObservations;
import org.apache.mahout.clustering.ClusteringTestUtils;
import org.apache.mahout.clustering.kmeans.TestKmeansClustering;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.MahoutTestCase;
import org.apache.mahout.common.commandline.DefaultOptionCreator;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.junit.Before;
import org.junit.Test;

import com.google.common.io.Closeables;

public final class TestFuzzyKmeansClustering extends MahoutTestCase {

  private FileSystem fs;
  private final DistanceMeasure measure = new EuclideanDistanceMeasure();

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    Configuration conf = new Configuration();
    fs = FileSystem.get(conf);
  }

  private static double round(double val, int places) {
    long factor = (long) Math.pow(10, places);

    // Shift the decimal the correct number of places
    // to the right.
    val *= factor;

    // Round to the nearest integer.
    long tmp = Math.round(val);

    // Shift the decimal the correct number of places
    // back to the left.
    return (double) tmp / factor;
  }

  private static Vector tweakValue(Vector point) {
    return point.plus(0.1);
  }

  @Test
  public void testFuzzyKMeansSeqJob() throws Exception {
    List<VectorWritable> points = TestKmeansClustering.getPointsWritable(TestKmeansClustering.REFERENCE);

    Path pointsPath = getTestTempDirPath("points");
    Path clustersPath = getTestTempDirPath("clusters");
    Configuration conf = new Configuration();
    ClusteringTestUtils.writePointsToFile(points, new Path(pointsPath, "file1"), fs, conf);

    for (int k = 0; k < points.size(); k++) {
      System.out.println("testKFuzzyKMeansMRJob k= " + k);
      // pick k initial cluster centers at random
      SequenceFile.Writer writer = new SequenceFile.Writer(fs,
                                                           conf,
                                                           new Path(clustersPath, "part-00000"),
                                                           Text.class,
                                                           SoftCluster.class);
      try {
        for (int i = 0; i < k + 1; i++) {
          Vector vec = tweakValue(points.get(i).get());
          SoftCluster cluster = new SoftCluster(vec, i, measure);
          /* add the center so the centroid will be correct upon output */
          cluster.observe(cluster.getCenter(), 1);
          // writer.write(cluster.getIdentifier() + '\t' + SoftCluster.formatCluster(cluster) + '\n');
          writer.append(new Text(cluster.getIdentifier()), cluster);
        }
      } finally {
        Closeables.closeQuietly(writer);
      }

      // now run the Job using the run() command line options.
      Path output = getTestTempDirPath("output");
      /*      FuzzyKMeansDriver.runJob(pointsPath,
                                     clustersPath,
                                     output,
                                     EuclideanDistanceMeasure.class.getName(),
                                     0.001,
                                     2,
                                     k + 1,
                                     2,
                                     false,
                                     true,
                                     0);
      */
      String[] args = {
          optKey(DefaultOptionCreator.INPUT_OPTION), pointsPath.toString(),
          optKey(DefaultOptionCreator.CLUSTERS_IN_OPTION),
          clustersPath.toString(),
          optKey(DefaultOptionCreator.OUTPUT_OPTION),
          output.toString(),
          optKey(DefaultOptionCreator.DISTANCE_MEASURE_OPTION),
          EuclideanDistanceMeasure.class.getName(),
          optKey(DefaultOptionCreator.CONVERGENCE_DELTA_OPTION),
          "0.001",
          optKey(DefaultOptionCreator.MAX_ITERATIONS_OPTION),
          "2",
          optKey(FuzzyKMeansDriver.M_OPTION),
          "2.0",
          optKey(DefaultOptionCreator.CLUSTERING_OPTION),
          optKey(DefaultOptionCreator.EMIT_MOST_LIKELY_OPTION),
          optKey(DefaultOptionCreator.OVERWRITE_OPTION),
          optKey(DefaultOptionCreator.METHOD_OPTION),
          DefaultOptionCreator.SEQUENTIAL_METHOD
      };
      new FuzzyKMeansDriver().run(args);
      long count = HadoopUtil.countRecords(new Path(output, "clusteredPoints/part-m-0"), conf);
      assertTrue(count > 0);
    }

  }

  @Test
  public void testFuzzyKMeansMRJob() throws Exception {
    List<VectorWritable> points = TestKmeansClustering.getPointsWritable(TestKmeansClustering.REFERENCE);

    Path pointsPath = getTestTempDirPath("points");
    Path clustersPath = getTestTempDirPath("clusters");
    Configuration conf = new Configuration();
    ClusteringTestUtils.writePointsToFile(points, new Path(pointsPath, "file1"), fs, conf);

    for (int k = 0; k < points.size(); k++) {
      System.out.println("testKFuzzyKMeansMRJob k= " + k);
      // pick k initial cluster centers at random
      SequenceFile.Writer writer = new SequenceFile.Writer(fs,
                                                           conf,
                                                           new Path(clustersPath, "part-00000"),
                                                           Text.class,
                                                           SoftCluster.class);
      try {
        for (int i = 0; i < k + 1; i++) {
          Vector vec = tweakValue(points.get(i).get());

          SoftCluster cluster = new SoftCluster(vec, i, measure);
          /* add the center so the centroid will be correct upon output */
          cluster.observe(cluster.getCenter(), 1);
          // writer.write(cluster.getIdentifier() + '\t' + SoftCluster.formatCluster(cluster) + '\n');
          writer.append(new Text(cluster.getIdentifier()), cluster);

        }
      } finally {
        Closeables.closeQuietly(writer);
      }

      // now run the Job using the run() command line options.
      Path output = getTestTempDirPath("output");
      /*      FuzzyKMeansDriver.runJob(pointsPath,
                                     clustersPath,
                                     output,
                                     EuclideanDistanceMeasure.class.getName(),
                                     0.001,
                                     2,
                                     k + 1,
                                     2,
                                     false,
                                     true,
                                     0);
      */
      String[] args = {
          optKey(DefaultOptionCreator.INPUT_OPTION),
          pointsPath.toString(),
          optKey(DefaultOptionCreator.CLUSTERS_IN_OPTION),
          clustersPath.toString(),
          optKey(DefaultOptionCreator.OUTPUT_OPTION),
          output.toString(),
          optKey(DefaultOptionCreator.DISTANCE_MEASURE_OPTION),
          EuclideanDistanceMeasure.class.getName(),
          optKey(DefaultOptionCreator.CONVERGENCE_DELTA_OPTION),
          "0.001",
          optKey(DefaultOptionCreator.MAX_ITERATIONS_OPTION),
          "2",
          optKey(FuzzyKMeansDriver.M_OPTION),
          "2.0",
          optKey(DefaultOptionCreator.CLUSTERING_OPTION),
          optKey(DefaultOptionCreator.EMIT_MOST_LIKELY_OPTION),
          optKey(DefaultOptionCreator.OVERWRITE_OPTION)
      };
      ToolRunner.run(new Configuration(), new FuzzyKMeansDriver(), args);
      long count = HadoopUtil.countRecords(new Path(output, "clusteredPoints/part-m-00000"), conf);
      assertTrue(count > 0);
    }

  }

  @Test
  public void testClusterObservationsSerialization() throws Exception {
    double[] data = { 1.1, 2.2, 3.3 };
    Vector vector = new DenseVector(data);
    ClusterObservations reference = new ClusterObservations(1, 2.0, vector, vector);
    DataOutputBuffer out = new DataOutputBuffer();
    reference.write(out);
    ClusterObservations info = new ClusterObservations();
    DataInputBuffer in = new DataInputBuffer();
    in.reset(out.getData(), out.getLength());
    info.readFields(in);
    assertEquals("probability", reference.getS0(), info.getS0(), EPSILON);
    assertEquals("point total", reference.getS1(), info.getS1());
    assertEquals("combiner", reference.getCombinerState(), info.getCombinerState());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1871.java