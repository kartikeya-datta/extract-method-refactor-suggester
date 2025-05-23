error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3721.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3721.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3721.java
text:
```scala
a@@ssertEquals("[2,2]", "[0.429, 0.429, 0.143]", AbstractCluster.formatVector(pdf, null));

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

package org.apache.mahout.clustering.iterator;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.AbstractCluster;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.ClusteringTestUtils;
import org.apache.mahout.clustering.canopy.Canopy;
import org.apache.mahout.clustering.classify.ClusterClassifier;
import org.apache.mahout.clustering.dirichlet.models.GaussianCluster;
import org.apache.mahout.clustering.fuzzykmeans.SoftCluster;
import org.apache.mahout.clustering.kmeans.TestKmeansClustering;
import org.apache.mahout.clustering.meanshift.MeanShiftCanopy;
import org.apache.mahout.common.MahoutTestCase;
import org.apache.mahout.common.distance.CosineDistanceMeasure;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.ManhattanDistanceMeasure;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.junit.Test;

import com.google.common.collect.Lists;

public final class TestClusterClassifier extends MahoutTestCase {
  
  private static ClusterClassifier newDMClassifier() {
    List<Cluster> models = Lists.newArrayList();
    DistanceMeasure measure = new ManhattanDistanceMeasure();
    models.add(new DistanceMeasureCluster(new DenseVector(2).assign(1), 0, measure));
    models.add(new DistanceMeasureCluster(new DenseVector(2), 1, measure));
    models.add(new DistanceMeasureCluster(new DenseVector(2).assign(-1), 2, measure));
    return new ClusterClassifier(models, new KMeansClusteringPolicy());
  }
  
  private static ClusterClassifier newKlusterClassifier() {
    List<Cluster> models = Lists.newArrayList();
    DistanceMeasure measure = new ManhattanDistanceMeasure();
    models.add(new org.apache.mahout.clustering.kmeans.Kluster(new DenseVector(2).assign(1), 0, measure));
    models.add(new org.apache.mahout.clustering.kmeans.Kluster(new DenseVector(2), 1, measure));
    models.add(new org.apache.mahout.clustering.kmeans.Kluster(new DenseVector(2).assign(-1), 2, measure));
    return new ClusterClassifier(models, new KMeansClusteringPolicy());
  }
  
  private static ClusterClassifier newCosineKlusterClassifier() {
    List<Cluster> models = Lists.newArrayList();
    DistanceMeasure measure = new CosineDistanceMeasure();
    models.add(new org.apache.mahout.clustering.kmeans.Kluster(new DenseVector(2).assign(1), 0, measure));
    models.add(new org.apache.mahout.clustering.kmeans.Kluster(new DenseVector(2), 1, measure));
    models.add(new org.apache.mahout.clustering.kmeans.Kluster(new DenseVector(2).assign(-1), 2, measure));
    return new ClusterClassifier(models, new KMeansClusteringPolicy());
  }

  private static ClusterClassifier newSoftClusterClassifier() {
    List<Cluster> models = Lists.newArrayList();
    DistanceMeasure measure = new ManhattanDistanceMeasure();
    models.add(new SoftCluster(new DenseVector(2).assign(1), 0, measure));
    models.add(new SoftCluster(new DenseVector(2), 1, measure));
    models.add(new SoftCluster(new DenseVector(2).assign(-1), 2, measure));
    return new ClusterClassifier(models, new FuzzyKMeansClusteringPolicy());
  }
  
  private static ClusterClassifier newGaussianClassifier() {
    List<Cluster> models = Lists.newArrayList();
    models.add(new GaussianCluster(new DenseVector(2).assign(1), new DenseVector(2).assign(1), 0));
    models.add(new GaussianCluster(new DenseVector(2), new DenseVector(2).assign(1), 1));
    models.add(new GaussianCluster(new DenseVector(2).assign(-1), new DenseVector(2).assign(1), 2));
    return new ClusterClassifier(models, new DirichletClusteringPolicy(3, 1.0));
  }
  
  private ClusterClassifier writeAndRead(ClusterClassifier classifier) throws IOException {
    Path path = new Path(getTestTempDirPath(), "output");
    classifier.writeToSeqFiles(path);
    ClusterClassifier newClassifier = new ClusterClassifier();
    newClassifier.readFromSeqFiles(new Configuration(), path);
    return newClassifier;
  }
  
  @Test
  public void testDMClusterClassification() {
    ClusterClassifier classifier = newDMClassifier();
    Vector pdf = classifier.classify(new DenseVector(2));
    assertEquals("[0,0]", "[0.200, 0.600, 0.200]", AbstractCluster.formatVector(pdf, null));
    pdf = classifier.classify(new DenseVector(2).assign(2));
    assertEquals("[2,2]", "[0.493, 0.296, 0.211]", AbstractCluster.formatVector(pdf, null));
  }
  
  @Test
  public void testCanopyClassification() {
    List<Cluster> models = Lists.newArrayList();
    DistanceMeasure measure = new ManhattanDistanceMeasure();
    models.add(new Canopy(new DenseVector(2).assign(1), 0, measure));
    models.add(new Canopy(new DenseVector(2), 1, measure));
    models.add(new Canopy(new DenseVector(2).assign(-1), 2, measure));
    ClusterClassifier classifier = new ClusterClassifier(models, new CanopyClusteringPolicy());
    Vector pdf = classifier.classify(new DenseVector(2));
    assertEquals("[0,0]", "[0.200, 0.600, 0.200]", AbstractCluster.formatVector(pdf, null));
    pdf = classifier.classify(new DenseVector(2).assign(2));
    assertEquals("[2,2]", "[0.493, 0.296, 0.211]", AbstractCluster.formatVector(pdf, null));
  }
  
  @Test
  public void testClusterClassification() {
    ClusterClassifier classifier = newKlusterClassifier();
    Vector pdf = classifier.classify(new DenseVector(2));
    assertEquals("[0,0]", "[0.200, 0.600, 0.200]", AbstractCluster.formatVector(pdf, null));
    pdf = classifier.classify(new DenseVector(2).assign(2));
    assertEquals("[2,2]", "[0.493, 0.296, 0.211]", AbstractCluster.formatVector(pdf, null));
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testMSCanopyClassification() {
    List<Cluster> models = Lists.newArrayList();
    DistanceMeasure measure = new ManhattanDistanceMeasure();
    models.add(new MeanShiftCanopy(new DenseVector(2).assign(1), 0, measure));
    models.add(new MeanShiftCanopy(new DenseVector(2), 1, measure));
    models.add(new MeanShiftCanopy(new DenseVector(2).assign(-1), 2, measure));
    ClusterClassifier classifier = new ClusterClassifier(models, new MeanShiftClusteringPolicy());
    classifier.classify(new DenseVector(2));
  }
  
  @Test
  public void testSoftClusterClassification() {
    ClusterClassifier classifier = newSoftClusterClassifier();
    Vector pdf = classifier.classify(new DenseVector(2));
    assertEquals("[0,0]", "[0.000, 1.000, 0.000]", AbstractCluster.formatVector(pdf, null));
    pdf = classifier.classify(new DenseVector(2).assign(2));
    assertEquals("[2,2]", "[0.735, 0.184, 0.082]", AbstractCluster.formatVector(pdf, null));
  }
  
  @Test
  public void testGaussianClusterClassification() {
    ClusterClassifier classifier = newGaussianClassifier();
    Vector pdf = classifier.classify(new DenseVector(2));
    assertEquals("[0,0]", "[0.212, 0.576, 0.212]", AbstractCluster.formatVector(pdf, null));
    pdf = classifier.classify(new DenseVector(2).assign(2));
    assertEquals("[2,2]", "[0.952, 0.047, 0.000]", AbstractCluster.formatVector(pdf, null));
  }
  
  @Test
  public void testDMClassifierSerialization() throws Exception {
    ClusterClassifier classifier = newDMClassifier();
    ClusterClassifier classifierOut = writeAndRead(classifier);
    assertEquals(classifier.getModels().size(), classifierOut.getModels().size());
    assertEquals(classifier.getModels().get(0).getClass().getName(), classifierOut.getModels().get(0).getClass()
        .getName());
  }
  
  @Test
  public void testClusterClassifierSerialization() throws Exception {
    ClusterClassifier classifier = newKlusterClassifier();
    ClusterClassifier classifierOut = writeAndRead(classifier);
    assertEquals(classifier.getModels().size(), classifierOut.getModels().size());
    assertEquals(classifier.getModels().get(0).getClass().getName(), classifierOut.getModels().get(0).getClass()
        .getName());
  }
  
  @Test
  public void testSoftClusterClassifierSerialization() throws Exception {
    ClusterClassifier classifier = newSoftClusterClassifier();
    ClusterClassifier classifierOut = writeAndRead(classifier);
    assertEquals(classifier.getModels().size(), classifierOut.getModels().size());
    assertEquals(classifier.getModels().get(0).getClass().getName(), classifierOut.getModels().get(0).getClass()
        .getName());
  }
  
  @Test
  public void testGaussianClassifierSerialization() throws Exception {
    ClusterClassifier classifier = newGaussianClassifier();
    ClusterClassifier classifierOut = writeAndRead(classifier);
    assertEquals(classifier.getModels().size(), classifierOut.getModels().size());
    assertEquals(classifier.getModels().get(0).getClass().getName(), classifierOut.getModels().get(0).getClass()
        .getName());
  }
  
  @Test
  public void testClusterIteratorKMeans() {
    List<Vector> data = TestKmeansClustering.getPoints(TestKmeansClustering.REFERENCE);
    ClusterClassifier prior = newKlusterClassifier();
    ClusterClassifier posterior = ClusterIterator.iterate(data, prior, 5);
    assertEquals(3, posterior.getModels().size());
    for (Cluster cluster : posterior.getModels()) {
      System.out.println(cluster.asFormatString(null));
    }
  }
  
  @Test
  public void testClusterIteratorDirichlet() {
    List<Vector> data = TestKmeansClustering.getPoints(TestKmeansClustering.REFERENCE);
    ClusterClassifier prior = newKlusterClassifier();
    ClusterClassifier posterior = ClusterIterator.iterate(data, prior, 5);
    assertEquals(3, posterior.getModels().size());
    for (Cluster cluster : posterior.getModels()) {
      System.out.println(cluster.asFormatString(null));
    }
  }
  
  @Test
  public void testSeqFileClusterIteratorKMeans() throws IOException {
    Path pointsPath = getTestTempDirPath("points");
    Path priorPath = getTestTempDirPath("prior");
    Path outPath = getTestTempDirPath("output");
    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.get(pointsPath.toUri(), conf);
    List<VectorWritable> points = TestKmeansClustering.getPointsWritable(TestKmeansClustering.REFERENCE);
    ClusteringTestUtils.writePointsToFile(points, new Path(pointsPath, "file1"), fs, conf);
    Path path = new Path(priorPath, "priorClassifier");
    ClusterClassifier prior = newKlusterClassifier();
    prior.writeToSeqFiles(path);
    assertEquals(3, prior.getModels().size());
    System.out.println("Prior");
    for (Cluster cluster : prior.getModels()) {
      System.out.println(cluster.asFormatString(null));
    }
    ClusterIterator.iterateSeq(conf, pointsPath, path, outPath, 5);
    
    for (int i = 1; i <= 4; i++) {
      System.out.println("Classifier-" + i);
      ClusterClassifier posterior = new ClusterClassifier();
      String name = i == 4 ? "clusters-4-final" : "clusters-" + i;
      posterior.readFromSeqFiles(conf, new Path(outPath, name));
      assertEquals(3, posterior.getModels().size());
      for (Cluster cluster : posterior.getModels()) {
        System.out.println(cluster.asFormatString(null));
      }
      
    }
  }
  
  @Test
  public void testMRFileClusterIteratorKMeans() throws Exception {
    Path pointsPath = getTestTempDirPath("points");
    Path priorPath = getTestTempDirPath("prior");
    Path outPath = getTestTempDirPath("output");
    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.get(pointsPath.toUri(), conf);
    List<VectorWritable> points = TestKmeansClustering.getPointsWritable(TestKmeansClustering.REFERENCE);
    ClusteringTestUtils.writePointsToFile(points, new Path(pointsPath, "file1"), fs, conf);
    Path path = new Path(priorPath, "priorClassifier");
    ClusterClassifier prior = newKlusterClassifier();
    prior.writeToSeqFiles(path);
    ClusteringPolicy policy = new KMeansClusteringPolicy();
    ClusterClassifier.writePolicy(policy, path);
    assertEquals(3, prior.getModels().size());
    System.out.println("Prior");
    for (Cluster cluster : prior.getModels()) {
      System.out.println(cluster.asFormatString(null));
    }
    ClusterIterator.iterateMR(conf, pointsPath, path, outPath, 5);
    
    for (int i = 1; i <= 4; i++) {
      System.out.println("Classifier-" + i);
      ClusterClassifier posterior = new ClusterClassifier();
      String name = i == 4 ? "clusters-4-final" : "clusters-" + i;
      posterior.readFromSeqFiles(conf, new Path(outPath, name));
      assertEquals(3, posterior.getModels().size());
      for (Cluster cluster : posterior.getModels()) {
        System.out.println(cluster.asFormatString(null));
      }     
    }
  }
  
  @Test
  public void testCosineKlusterClassification() {
    ClusterClassifier classifier = newCosineKlusterClassifier();
    Vector pdf = classifier.classify(new DenseVector(2));
    assertEquals("[0,0]", "[0.333, 0.333, 0.333]", AbstractCluster.formatVector(pdf, null));
    pdf = classifier.classify(new DenseVector(2).assign(2));
    assertEquals("[2,2]", "[0.545, 0.273, 0.182]", AbstractCluster.formatVector(pdf, null));
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3721.java