error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1506.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1506.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1506.java
text:
```scala
F@@ileSystem dfs = FileSystem.get(outPath.toUri(), conf);

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

package org.apache.mahout.classifier.cbayes;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DefaultStringifier;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.util.GenericsUtil;
import org.apache.mahout.classifier.bayes.io.SequenceFileModelReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.io.IOException;

/**
 * Create and run the Bayes Trainer.
 */
public class CBayesNormalizedWeightDriver {

  private static final Logger log = LoggerFactory.getLogger(CBayesNormalizedWeightDriver.class);

  private CBayesNormalizedWeightDriver() {
  }

  /**
   * Takes in two arguments:
   * <ol>
   * <li>The input {@link org.apache.hadoop.fs.Path} where the input documents live</li>
   * <li>The output {@link org.apache.hadoop.fs.Path} where to write the {@link org.apache.mahout.common.Model}
   * as a {@link org.apache.hadoop.io.SequenceFile}</li>
   * </ol>
   * @param args The args
   */
  public static void main(String[] args) throws IOException {
    String input = args[0];
    String output = args[1];

    runJob(input, output);
  }

  /**
   * Run the job
   *
   * @param input            the input pathname String
   * @param output           the output pathname String
   */
  public static void runJob(String input, String output) throws IOException {
    JobClient client = new JobClient();
    JobConf conf = new JobConf(CBayesNormalizedWeightDriver.class);
    

    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(DoubleWritable.class);
    FileInputFormat.addInputPath(conf, new Path(output + "/trainer-theta"));
    Path outPath = new Path(output + "/trainer-weight");
    FileOutputFormat.setOutputPath(conf, outPath);
    conf.setNumMapTasks(100);
    //conf.setNumReduceTasks(1);
    conf.setMapperClass(CBayesNormalizedWeightMapper.class);
    conf.setInputFormat(SequenceFileInputFormat.class);
    conf.setCombinerClass(CBayesNormalizedWeightReducer.class);    
    conf.setReducerClass(CBayesNormalizedWeightReducer.class);    
    conf.setOutputFormat(SequenceFileOutputFormat.class);
    
    conf.set("io.serializations",
             "org.apache.hadoop.io.serializer.JavaSerialization,org.apache.hadoop.io.serializer.WritableSerialization");
    // Dont ever forget this. People should keep track of how hadoop conf parameters and make or break a piece of code
    FileSystem dfs = FileSystem.get(conf);
    if (dfs.exists(outPath))
      dfs.delete(outPath, true);

    Path thetaNormalizationsFiles = new Path(output+"/trainer-thetaNormalizer/part*");
    Map<String,Double> thetaNormalizer= SequenceFileModelReader.readLabelSums(dfs, thetaNormalizationsFiles, conf);
    double perLabelWeightSumNormalisationFactor = Double.MAX_VALUE;
    for(Map.Entry<String, Double> stringDoubleEntry1 : thetaNormalizer.entrySet())
    {

      double Sigma_W_ij = stringDoubleEntry1.getValue();
      if(perLabelWeightSumNormalisationFactor > Math.abs(Sigma_W_ij)){
        perLabelWeightSumNormalisationFactor = Math.abs(Sigma_W_ij);
      }
    }

    for(Map.Entry<String, Double> stringDoubleEntry : thetaNormalizer.entrySet())
    {
      double Sigma_W_ij = stringDoubleEntry.getValue();
      thetaNormalizer.put(stringDoubleEntry.getKey(), Sigma_W_ij / perLabelWeightSumNormalisationFactor) ;
    }


    DefaultStringifier<Map<String,Double>> mapStringifier =
        new DefaultStringifier<Map<String,Double>>(conf, GenericsUtil.getClass(thetaNormalizer));
    String thetaNormalizationsString = mapStringifier.toString(thetaNormalizer);

    Map<String,Double> c = mapStringifier.fromString(thetaNormalizationsString);
    log.info("{}", c);
    conf.set("cnaivebayes.thetaNormalizations", thetaNormalizationsString);


    client.setConf(conf);

    JobClient.runJob(conf);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1506.java