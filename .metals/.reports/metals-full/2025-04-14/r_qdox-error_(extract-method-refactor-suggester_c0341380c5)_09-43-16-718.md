error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1496.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1496.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1496.java
text:
```scala
F@@ileSystem fs = FileSystem.get(outPath.toUri(), conf);

/* Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.mahout.clustering.kmeans;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KMeansDriver {

  private static final Logger log = LoggerFactory.getLogger(KMeansDriver.class);

  private KMeansDriver() {
  }

  public static void main(String[] args) {
    String input = args[0];
    String clusters = args[1];
    String output = args[2];
    String measureClass = args[3];
    double convergenceDelta = Double.parseDouble(args[4]);
    int maxIterations = Integer.parseInt(args[5]);
    runJob(input, clusters, output, measureClass, convergenceDelta,
        maxIterations, 2);
  }

  /**
   * Run the job using supplied arguments
   *
   * @param input the directory pathname for input points
   * @param clustersIn the directory pathname for initial & computed clusters
   * @param output the directory pathname for output points
   * @param measureClass the classname of the DistanceMeasure
   * @param convergenceDelta the convergence delta value
   * @param maxIterations the maximum number of iterations
   */
  public static void runJob(String input, String clustersIn, String output,
      String measureClass, double convergenceDelta, int maxIterations,
      int numCentroids) {
    // iterate until the clusters converge
    boolean converged = false;
    int iteration = 0;
    String delta = Double.toString(convergenceDelta);

    while (!converged && iteration < maxIterations) {
      log.info("Iteration {}", iteration);
      // point the output to a new directory per iteration
      String clustersOut = output + "/clusters-" + iteration;
      converged = runIteration(input, clustersIn, clustersOut, measureClass,
          delta, numCentroids);
      // now point the input to the old output directory
      clustersIn = output + "/clusters-" + iteration;
      iteration++;
    }
    // now actually cluster the points
    log.info("Clustering ");
    runClustering(input, clustersIn, output + "/points", measureClass, delta);
  }

  /**
   * Run the job using supplied arguments
   *
   * @param input the directory pathname for input points
   * @param clustersIn the directory pathname for iniput clusters
   * @param clustersOut the directory pathname for output clusters
   * @param measureClass the classname of the DistanceMeasure
   * @param convergenceDelta the convergence delta value
   * @return true if the iteration successfully runs
   */
  private static boolean runIteration(String input, String clustersIn,
      String clustersOut, String measureClass, String convergenceDelta,
      int numReduceTasks) {
    JobClient client = new JobClient();
    JobConf conf = new JobConf(KMeansDriver.class);
    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(Text.class);

    FileInputFormat.setInputPaths(conf, new Path(input));
    Path outPath = new Path(clustersOut);
    FileOutputFormat.setOutputPath(conf, outPath);

    conf.setOutputFormat(SequenceFileOutputFormat.class);
    conf.setMapperClass(KMeansMapper.class);
    conf.setCombinerClass(KMeansCombiner.class);
    conf.setReducerClass(KMeansReducer.class);
    // conf.setNumMapTasks(numMapTasks);
    conf.setNumReduceTasks(numReduceTasks);
    conf.set(Cluster.CLUSTER_PATH_KEY, clustersIn);
    conf.set(Cluster.DISTANCE_MEASURE_KEY, measureClass);
    conf.set(Cluster.CLUSTER_CONVERGENCE_KEY, convergenceDelta);

//    conf.set("mapred.child.java.opts", "-Xmx1536m");
    // uncomment it to run locally
//    conf.set("mapred.job.tracker", "local");

    client.setConf(conf);
    try {
      JobClient.runJob(conf);
      FileSystem fs = FileSystem.get(conf);
      return isConverged(clustersOut + "/part-00000", conf, fs);
    } catch (IOException e) {
      log.warn(e.toString(), e);
      return true;
    }
  }

  /**
   * Run the job using supplied arguments
   *
   * @param input the directory pathname for input points
   * @param clustersIn the directory pathname for input clusters
   * @param output the directory pathname for output points
   * @param measureClass the classname of the DistanceMeasure
   * @param convergenceDelta the convergence delta value
   */
  private static void runClustering(String input, String clustersIn,
      String output, String measureClass, String convergenceDelta) {
    JobClient client = new JobClient();
    JobConf conf = new JobConf(KMeansDriver.class);

    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(Text.class);

    FileInputFormat.setInputPaths(conf, new Path(input));
    Path outPath = new Path(output);
    FileOutputFormat.setOutputPath(conf, outPath);

    conf.setMapperClass(KMeansClusterMapper.class);
    conf.setNumReduceTasks(0);
    conf.set(Cluster.CLUSTER_PATH_KEY, clustersIn);
    conf.set(Cluster.DISTANCE_MEASURE_KEY, measureClass);
    conf.set(Cluster.CLUSTER_CONVERGENCE_KEY, convergenceDelta);

    client.setConf(conf);
    // uncomment it to run locally
    // conf.set("mapred.job.tracker", "local");
//    conf.set("mapred.child.java.opts", "-Xmx1536m");
    try {
      JobClient.runJob(conf);
    } catch (IOException e) {
      log.warn(e.toString(), e);
    }
  }

  /**
   * Return if all of the Clusters in the filePath have converged or not
   *
   * @param filePath the file path to the single file containing the clusters
   * @param conf     the JobConf
   * @param fs       the FileSystem
   * @return true if all Clusters are converged
   * @throws IOException if there was an IO error
   */
 private static boolean isConverged(String filePath, JobConf conf, FileSystem fs)
          throws IOException {
    Path outPart = new Path(filePath);
    SequenceFile.Reader reader = new SequenceFile.Reader(fs, outPart, conf);
    Text key = new Text();
    Text value = new Text();
    boolean converged = true;
    while (converged && reader.next(key, value)) {
      converged = value.toString().charAt(0) == 'V';
    }
    return converged;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1496.java