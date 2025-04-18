error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/764.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/764.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/764.java
text:
```scala
i@@f (keys != null && index != keys.length) {

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

package org.apache.mahout.df.mapred.partial;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.mahout.df.DFUtils;
import org.apache.mahout.df.DecisionForest;
import org.apache.mahout.df.builder.TreeBuilder;
import org.apache.mahout.df.callback.PredictionCallback;
import org.apache.mahout.df.mapred.Builder;
import org.apache.mahout.df.mapred.partial.Step0Job.Step0Output;
import org.apache.mahout.df.mapreduce.MapredOutput;
import org.apache.mahout.df.mapreduce.partial.TreeID;
import org.apache.mahout.df.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builds a random forest using partial data. Each mapper uses only the data given by its InputSplit
 */
public class PartialBuilder extends Builder {
  
  private static final Logger log = LoggerFactory.getLogger(PartialBuilder.class);
  
  /**
   * Indicates if we should run the second step of the builder.<br>
   * This parameter is only meant for debuging, so we keep it protected.
   * 
   * @param conf
   * @return
   */
  protected static boolean isStep2(Configuration conf) {
    return conf.getBoolean("debug.mahout.rf.partial.step2", true);
  }
  
  /**
   * Should run the second step of the builder ?
   * 
   * @param conf
   * @param value
   *          true to indicate that the second step will be launched
   * 
   */
  protected static void setStep2(Configuration conf, boolean value) {
    conf.setBoolean("debug.mahout.rf.partial.step2", value);
  }
  
  public PartialBuilder(TreeBuilder treeBuilder, Path dataPath, Path datasetPath, Long seed) {
    this(treeBuilder, dataPath, datasetPath, seed, new Configuration());
  }
  
  public PartialBuilder(TreeBuilder treeBuilder,
                        Path dataPath,
                        Path datasetPath,
                        Long seed,
                        Configuration conf) {
    super(treeBuilder, dataPath, datasetPath, seed, conf);
  }
  
  @Override
  protected void configureJob(JobConf job, int nbTrees, boolean oobEstimate) throws IOException {
    FileInputFormat.setInputPaths(job, getDataPath());
    FileOutputFormat.setOutputPath(job, getOutputPath(job));
    
    job.setOutputKeyClass(TreeID.class);
    job.setOutputValueClass(MapredOutput.class);
    
    job.setMapperClass(Step1Mapper.class);
    job.setNumReduceTasks(0); // no reducers
    
    job.setInputFormat(TextInputFormat.class);
    job.setOutputFormat(SequenceFileOutputFormat.class);
    
    // if we are in 'local' mode, correct the number of maps
    // or the mappers won't be able to compute the right indexes
    String tracker = job.get("mapred.job.tracker", "local");
    if ("local".equals(tracker)) {
      log.warn("Hadoop running in 'local' mode, only one map task will be launched");
      job.setNumMapTasks(1);
    }
  }
  
  @Override
  protected DecisionForest parseOutput(JobConf job, PredictionCallback callback) throws IOException {
    int numMaps = job.getNumMapTasks();
    int numTrees = Builder.getNbTrees(job);
    
    Path outputPath = getOutputPath(job);
    
    log.info("Computing partitions' first ids...");
    Step0Job step0 = new Step0Job(getOutputPath(job), getDataPath(), getDatasetPath());
    Step0Output[] partitions = step0.run(getConf());
    
    log.info("Processing the output...");
    TreeID[] keys = new TreeID[numTrees];
    Node[] trees = new Node[numTrees];
    int[] firstIds = Step0Output.extractFirstIds(partitions);
    processOutput(job, outputPath, firstIds, keys, trees, callback);
    
    // call the second step in order to complete the oob predictions
    if ((callback != null) && (numMaps > 1) && isStep2(getConf())) {
      log.info("*****************************");
      log.info("Second Step");
      log.info("*****************************");
      Step2Job step2 = new Step2Job(getOutputPath(job), getDataPath(), getDatasetPath(), partitions);
      
      step2.run(job, keys, trees, callback);
    }
    
    return new DecisionForest(Arrays.asList(trees));
  }
  
  /**
   * Processes the output from the output path.<br>
   * 
   * @param job
   * @param outputPath
   *          directory that contains the output of the job
   * @param firstIds
   *          partitions' first ids in hadoop's order
   * @param keys
   * @param callback
   *          can be null
   * @throws IOException
   */
  protected static void processOutput(JobConf job,
                                      Path outputPath,
                                      int[] firstIds,
                                      TreeID[] keys,
                                      Node[] trees,
                                      PredictionCallback callback) throws IOException {
    FileSystem fs = outputPath.getFileSystem(job);
    
    Path[] outfiles = DFUtils.listOutputFiles(fs, outputPath);
    
    // read all the outputs
    TreeID key = new TreeID();
    MapredOutput value = new MapredOutput();
    
    int index = 0;
    for (Path path : outfiles) {
      Reader reader = new Reader(fs, path, job);
      
      try {
        while (reader.next(key, value)) {
          if (keys != null) {
            keys[index] = key.clone();
          }
          
          if (trees != null) {
            trees[index] = value.getTree();
          }
          
          processOutput(firstIds, key, value, callback);
          
          index++;
        }
      } finally {
        reader.close();
      }
    }
    
    // make sure we got all the keys/values
    if (index != keys.length) {
      throw new IllegalStateException();
    }
  }
  
  /**
   * Process the output, extracting the trees and passing the predictions to the callback
   * 
   * @param firstIds
   *          partitions' first ids in hadoop's order
   * @param callback
   * @return
   */
  private static void processOutput(int[] firstIds,
                                    TreeID key,
                                    MapredOutput value,
                                    PredictionCallback callback) {
    
    if (callback != null) {
      int[] predictions = value.getPredictions();
      
      for (int instanceId = 0; instanceId < predictions.length; instanceId++) {
        callback.prediction(key.treeId(), firstIds[key.partition()] + instanceId, predictions[instanceId]);
      }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/764.java