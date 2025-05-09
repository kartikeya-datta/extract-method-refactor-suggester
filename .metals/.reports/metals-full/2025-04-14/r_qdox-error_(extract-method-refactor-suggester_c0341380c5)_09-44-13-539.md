error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3473.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3473.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3473.java
text:
```scala
W@@eightedPropertyVectorWritable point = new WeightedPropertyVectorWritable();

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

package org.apache.mahout.clustering.classify;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.clustering.ClusteringTestUtils;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.clustering.iterator.CanopyClusteringPolicy;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.MahoutTestCase;
import org.apache.mahout.common.distance.ManhattanDistanceMeasure;
import org.apache.mahout.common.iterator.sequencefile.PathFilters;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ClusterClassificationDriverTest extends MahoutTestCase {

  private static final double[][] REFERENCE = { {1, 1}, {2, 1}, {1, 2}, {4, 4},
      {5, 4}, {4, 5}, {5, 5}, {9, 9}, {8, 8}};

  private FileSystem fs;
  private Path clusteringOutputPath;
  private Configuration conf;
  private Path pointsPath;
  private Path classifiedOutputPath;
  private List<Vector> firstCluster;
  private List<Vector> secondCluster;
  private List<Vector> thirdCluster;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    Configuration conf = getConfiguration();
    fs = FileSystem.get(conf);
    firstCluster = Lists.newArrayList();
    secondCluster = Lists.newArrayList();
    thirdCluster = Lists.newArrayList();

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

  @Test
  public void testVectorClassificationWithOutlierRemovalMR() throws Exception {
    List<VectorWritable> points = getPointsWritable(REFERENCE);

    pointsPath = getTestTempDirPath("points");
    clusteringOutputPath = getTestTempDirPath("output");
    classifiedOutputPath = getTestTempDirPath("classifiedClusters");
    HadoopUtil.delete(conf, classifiedOutputPath);

    conf = getConfiguration();

    ClusteringTestUtils.writePointsToFile(points, true,
        new Path(pointsPath, "file1"), fs, conf);
    runClustering(pointsPath, conf, false);
    runClassificationWithOutlierRemoval(false);
    collectVectorsForAssertion();
    assertVectorsWithOutlierRemoval();
  }

  @Test
  public void testVectorClassificationWithoutOutlierRemoval() throws Exception {
    List<VectorWritable> points = getPointsWritable(REFERENCE);

    pointsPath = getTestTempDirPath("points");
    clusteringOutputPath = getTestTempDirPath("output");
    classifiedOutputPath = getTestTempDirPath("classify");

    conf = getConfiguration();

    ClusteringTestUtils.writePointsToFile(points,
        new Path(pointsPath, "file1"), fs, conf);
    runClustering(pointsPath, conf, true);
    runClassificationWithoutOutlierRemoval();
    collectVectorsForAssertion();
    assertVectorsWithoutOutlierRemoval();
  }

  @Test
  public void testVectorClassificationWithOutlierRemoval() throws Exception {
    List<VectorWritable> points = getPointsWritable(REFERENCE);

    pointsPath = getTestTempDirPath("points");
    clusteringOutputPath = getTestTempDirPath("output");
    classifiedOutputPath = getTestTempDirPath("classify");

    conf = getConfiguration();

    ClusteringTestUtils.writePointsToFile(points,
        new Path(pointsPath, "file1"), fs, conf);
    runClustering(pointsPath, conf, true);
    runClassificationWithOutlierRemoval(true);
    collectVectorsForAssertion();
    assertVectorsWithOutlierRemoval();
  }

  private void runClustering(Path pointsPath, Configuration conf,
      Boolean runSequential) throws IOException, InterruptedException,
      ClassNotFoundException {
    CanopyDriver.run(conf, pointsPath, clusteringOutputPath,
        new ManhattanDistanceMeasure(), 3.1, 2.1, false, 0.0, runSequential);
    Path finalClustersPath = new Path(clusteringOutputPath, "clusters-0-final");
    ClusterClassifier.writePolicy(new CanopyClusteringPolicy(),
        finalClustersPath);
  }

  private void runClassificationWithoutOutlierRemoval()
    throws IOException, InterruptedException, ClassNotFoundException {
    ClusterClassificationDriver.run(getConfiguration(), pointsPath, clusteringOutputPath, classifiedOutputPath, 0.0, true, true);
  }

  private void runClassificationWithOutlierRemoval(boolean runSequential)
    throws IOException, InterruptedException, ClassNotFoundException {
    ClusterClassificationDriver.run(getConfiguration(), pointsPath, clusteringOutputPath, classifiedOutputPath, 0.73, true, runSequential);
  }

  private void collectVectorsForAssertion() throws IOException {
    Path[] partFilePaths = FileUtil.stat2Paths(fs
        .globStatus(classifiedOutputPath));
    FileStatus[] listStatus = fs.listStatus(partFilePaths,
        PathFilters.partFilter());
    for (FileStatus partFile : listStatus) {
      SequenceFile.Reader classifiedVectors = new SequenceFile.Reader(fs,
          partFile.getPath(), conf);
      Writable clusterIdAsKey = new IntWritable();
      WeightedVectorWritable point = new WeightedVectorWritable();
      while (classifiedVectors.next(clusterIdAsKey, point)) {
        collectVector(clusterIdAsKey.toString(), point.getVector());
      }
    }
  }

  private void collectVector(String clusterId, Vector vector) {
    if ("0".equals(clusterId)) {
      firstCluster.add(vector);
    } else if ("1".equals(clusterId)) {
      secondCluster.add(vector);
    } else if ("2".equals(clusterId)) {
      thirdCluster.add(vector);
    }
  }

  private void assertVectorsWithOutlierRemoval() {
    checkClustersWithOutlierRemoval();
  }

  private void assertVectorsWithoutOutlierRemoval() {
    assertFirstClusterWithoutOutlierRemoval();
    assertSecondClusterWithoutOutlierRemoval();
    assertThirdClusterWithoutOutlierRemoval();
  }

  private void assertThirdClusterWithoutOutlierRemoval() {
    Assert.assertEquals(2, thirdCluster.size());
    for (Vector vector : thirdCluster) {
      Assert.assertTrue(ArrayUtils.contains(new String[] {"{0:9.0,1:9.0}",
          "{0:8.0,1:8.0}"}, vector.asFormatString()));
    }
  }

  private void assertSecondClusterWithoutOutlierRemoval() {
    Assert.assertEquals(4, secondCluster.size());
    for (Vector vector : secondCluster) {
      Assert.assertTrue(ArrayUtils.contains(new String[] {"{0:4.0,1:4.0}",
          "{0:5.0,1:4.0}", "{0:4.0,1:5.0}", "{0:5.0,1:5.0}"},
          vector.asFormatString()));
    }
  }

  private void assertFirstClusterWithoutOutlierRemoval() {
    Assert.assertEquals(3, firstCluster.size());
    for (Vector vector : firstCluster) {
      Assert.assertTrue(ArrayUtils.contains(new String[] {"{0:1.0,1:1.0}",
          "{0:2.0,1:1.0}", "{0:1.0,1:2.0}"}, vector.asFormatString()));
    }
  }

  private void checkClustersWithOutlierRemoval() {
    Set<String> reference = Sets.newHashSet("{0:9.0,1:9.0}", "{0:1.0,1:1.0}");

    List<List<Vector>> clusters = Lists.newArrayList();
    clusters.add(firstCluster);
    clusters.add(secondCluster);
    clusters.add(thirdCluster);

    int singletonCnt = 0;
    int emptyCnt = 0;
    for (List<Vector> vList : clusters) {
      if (vList.isEmpty()) {
        emptyCnt++;
      } else {
        singletonCnt++;
        assertEquals("expecting only singleton clusters; got size=" + vList.size(), 1, vList.size());
        Assert.assertTrue("not expecting cluster:" + vList.get(0).asFormatString(),
                          reference.contains(vList.get(0).asFormatString()));
        reference.remove(vList.get(0).asFormatString());
      }
    }
    Assert.assertEquals("Different number of empty clusters than expected!", 1, emptyCnt);
    Assert.assertEquals("Different number of singletons than expected!", 2, singletonCnt);
    Assert.assertEquals("Didn't match all reference clusters!", 0, reference.size());
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3473.java