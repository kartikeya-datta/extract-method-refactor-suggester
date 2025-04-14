error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1510.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1510.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1510.java
text:
```scala
F@@ileSystem fs = FileSystem.get(outPath.toUri(), conf);

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

package org.apache.mahout.clustering.syntheticcontrol.dirichlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;
import org.apache.mahout.clustering.dirichlet.DirichletCluster;
import org.apache.mahout.clustering.dirichlet.DirichletDriver;
import org.apache.mahout.clustering.dirichlet.DirichletJob;
import org.apache.mahout.clustering.dirichlet.DirichletMapper;
import org.apache.mahout.clustering.dirichlet.models.Model;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.clustering.syntheticcontrol.canopy.InputDriver;
import org.apache.mahout.matrix.Vector;

public class Job {
  private Job() {
  }

  public static void main(String[] args) throws IOException,
      ClassNotFoundException, InstantiationException, IllegalAccessException {
    if (args.length == 7) {
      String input = args[0];
      String output = args[1];
      String modelFactory = args[2];
      int numClusters = Integer.parseInt(args[3]);
      int maxIterations = Integer.parseInt(args[4]);
      double alpha_0 = Double.parseDouble(args[5]);
      int numReducers = Integer.parseInt(args[6]);
      runJob(input, output, modelFactory, numClusters, maxIterations, alpha_0,
          numReducers);
    } else
      runJob(
          "testdata",
          "output",
          "org.apache.mahout.clustering.syntheticcontrol.dirichlet.NormalScModelDistribution",
          10, 5, 1.0, 1);
  }

  /**
   * Run the job using supplied arguments, deleting the output directory if it
   * exists beforehand
   * 
   * @param input the directory pathname for input points
   * @param output the directory pathname for output points
   * @param modelFactory the ModelDistribution class name
   * @param numModels the number of Models
   * @param maxIterations the maximum number of iterations
   * @param alpha_0 the alpha0 value for the DirichletDistribution
   * @param numReducers the desired number of reducers
   * @throws IllegalAccessException 
   * @throws InstantiationException 
   * @throws ClassNotFoundException 
   */
  public static void runJob(String input, String output, String modelFactory,
      int numModels, int maxIterations, double alpha_0, int numReducers)
      throws IOException, ClassNotFoundException, InstantiationException,
      IllegalAccessException {
    // delete the output directory
    JobConf conf = new JobConf(DirichletJob.class);
    Path outPath = new Path(output);
    FileSystem fs = FileSystem.get(conf);
    if (fs.exists(outPath)) {
      fs.delete(outPath, true);
    }
    fs.mkdirs(outPath);
    InputDriver.runJob(input, output + "/data");
    DirichletDriver.runJob(output + "/data", output + "/state", modelFactory,
        numModels, maxIterations, alpha_0, numReducers);
    printResults(output + "/state", modelFactory, maxIterations, numModels,
        alpha_0);
  }

  /**
   * Prints out all of the clusters during each iteration
   * @param output the String output directory
   * @param modelDistribution the String class name of the ModelDistribution
   * @param numIterations the int number of Iterations
   * @param numModels the int number of models
   * @param alpha_0 the double alpha_0 value
   */
  public static void printResults(String output, String modelDistribution,
      int numIterations, int numModels, double alpha_0) {
    List<List<DirichletCluster<Vector>>> clusters = new ArrayList<List<DirichletCluster<Vector>>>();
    JobConf conf = new JobConf(KMeansDriver.class);
    conf.set(DirichletDriver.MODEL_FACTORY_KEY, modelDistribution);
    conf.set(DirichletDriver.NUM_CLUSTERS_KEY, Integer.toString(numModels));
    conf.set(DirichletDriver.ALPHA_0_KEY, Double.toString(alpha_0));
    for (int i = 0; i < numIterations; i++) {
      conf.set(DirichletDriver.STATE_IN_KEY, output + "/state-" + i);
      clusters.add(DirichletMapper.getDirichletState(conf).clusters);
    }
    printResults(clusters, 0);

  }

  /**
   * Actually prints out the clusters
   * @param clusters a List of Lists of DirichletClusters
   * @param significant the minimum number of samples to enable printing a model
   */
  private static void printResults(
      List<List<DirichletCluster<Vector>>> clusters, int significant) {
    int row = 0;
    for (List<DirichletCluster<Vector>> r : clusters) {
      System.out.print("sample[" + row++ + "]= ");
      for (int k = 0; k < r.size(); k++) {
        Model<Vector> model = r.get(k).model;
        if (model.count() > significant) {
          int total = (int) r.get(k).totalCount;
          System.out.print("m" + k + '(' + total + ')' + model.toString()
              + ", ");
        }
      }
      System.out.println();
    }
    System.out.println();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1510.java