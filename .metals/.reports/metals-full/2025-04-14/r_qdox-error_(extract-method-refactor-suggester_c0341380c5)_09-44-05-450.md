error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/128.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/128.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/128.java
text:
```scala
f@@s.delete(inpath, true);

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

package org.apache.mahout.ga.watchmaker;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.mahout.utils.StringUtils;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Generic Mahout distributed evaluator. takes an evaluator and a population and
 * launches a Hadoop job. The job evaluates the fitness of each individual of the
 * population using the given evaluator. Takes care of storing the population
 * into an input file, and loading the fitness from job outputs.
 */
public class MahoutEvaluator {
  private MahoutEvaluator() {
  }

  /**
   * Uses Mahout to evaluate every candidate from the input population using the
   * given evaluator.
   * 
   * @param evaluator FitnessEvaluator to use
   * @param population input population
   * @param evaluations <code>List&lt;Double&gt;</code> that contains the
   *        evaluated fitness for each candidate from the input population,
   *        sorted in the same order as the candidates.
   * @throws IOException
   */
  public static void evaluate(FitnessEvaluator<?> evaluator, List<?> population,
      List<Double> evaluations) throws IOException {
    JobConf conf = new JobConf(MahoutEvaluator.class);
    FileSystem fs = FileSystem.get(conf);

    Path inpath = prepareInput(fs, population);
    Path outpath = OutputUtils.prepareOutput(fs);

    configureJob(conf, evaluator, inpath, outpath);
    JobClient.runJob(conf);

    OutputUtils.importEvaluations(fs, conf, outpath, evaluations);
  }

  /**
   * Create the input directory and stores the population in it.
   * 
   * @param fs <code>FileSystem</code> to use
   * @param population population to store
   * @return input <code>Path</code>
   *
   * @throws IOException
   */
  private static Path prepareInput(FileSystem fs, List<?> population)
      throws IOException {
    Path inpath = new Path(fs.getWorkingDirectory(), "input");

    // Delete the input if it already exists
    if (fs.exists(inpath)) {
      FileUtil.fullyDelete(fs, inpath);
    }

    fs.mkdirs(inpath);

    storePopulation(fs, new Path(inpath, "population"), population);

    return inpath;
  }

  /**
   * Configure the job
   * 
   * @param conf
   * @param evaluator FitnessEvaluator passed to the mapper
   * @param inpath input <code>Path</code>
   * @param outpath output <code>Path</code>
   */
  private static void configureJob(JobConf conf, FitnessEvaluator<?> evaluator,
      Path inpath, Path outpath) {
    FileInputFormat.setInputPaths(conf, inpath);
    FileOutputFormat.setOutputPath(conf, outpath);

    conf.setOutputKeyClass(LongWritable.class);
    conf.setOutputValueClass(DoubleWritable.class);

    conf.setMapperClass(EvalMapper.class);
    // no combiner
    // identity reducer
    // TODO do we really need a reducer at all ?

    conf.setInputFormat(TextInputFormat.class);
    conf.setOutputFormat(SequenceFileOutputFormat.class);

    // store the stringified evaluator
    conf.set(EvalMapper.MAHOUT_GA_EVALUATOR, StringUtils.toString(evaluator));
  }

  /**
   * Stores a population of candidates in the output file path.
   * 
   * @param fs FileSystem used to create the output file
   * @param f output file path
   * @param population population to store
   * @throws IOException
   */
  static void storePopulation(FileSystem fs, Path f, List<?> population)
      throws IOException {
    FSDataOutputStream out = fs.create(f);
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

    try {
      for (Object candidate : population) {
        writer.write(StringUtils.toString(candidate));
        writer.newLine();
      }
    } finally {
      writer.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/128.java