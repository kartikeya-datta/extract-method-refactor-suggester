error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1489.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1489.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1489.java
text:
```scala
F@@ileSystem fs = FileSystem.get(outputPathPath.toUri(), jobConf);

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

package org.apache.mahout.cf.taste.hadoop;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapred.lib.IdentityReducer;
import org.apache.mahout.cf.taste.recommender.Recommender;

import java.io.IOException;

/**
 * <p>This class configures and runs a {@link RecommenderMapper} using Hadoop.</p>
 *
 * <p>Command line arguments are:</p>
 * <ol>
 *  <li>Fully-qualified class name of {@link Recommender} to use to make recommendations.
 *   Note that it must have a no-arg constructor.</li>
 *  <li>Number of recommendations to compute per user</li>
 *  <li>Location of a text file containing user IDs for which recommendations should be computed,
 *   one per line</li>
 *  <li>Location of a data model file containing preference data, suitable for use with
 *   {@link org.apache.mahout.cf.taste.impl.model.file.FileDataModel}</li>
 *  <li>Output path where reducer output should go</li>
 *  <li>Number of mapper tasks to use</li>
 * </ol>
 *
 * <p>Example:</p>
 *
 * <p><code>org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender 10 path/to/users.txt
 *  path/to/data.csv path/to/reducerOutputDir 5</code></p>
 *
 * <p>TODO I am not a bit sure this works yet in a real distributed environment.</p>
 */
public final class RecommenderJob {
  private RecommenderJob() {
  }

  public static void main(String[] args) throws IOException {
    String recommendClassName = args[0];
    int recommendationsPerUser = Integer.parseInt(args[1]);
    String userIDFile = args[2];
    String dataModelFile = args[3];
    String outputPath = args[4];
    JobConf jobConf =
        buildJobConf(recommendClassName, recommendationsPerUser, userIDFile, dataModelFile, outputPath);
    JobClient.runJob(jobConf);
  }

  public static JobConf buildJobConf(String recommendClassName,
                                     int recommendationsPerUser,
                                     String userIDFile,
                                     String dataModelFile,
                                     String outputPath) throws IOException {

    Path userIDFilePath = new Path(userIDFile);
    Path outputPathPath = new Path(outputPath);

    JobConf jobConf = new JobConf(RecommenderJob.class);

    FileSystem fs = FileSystem.get(jobConf);
    if (fs.exists(outputPathPath)) {
      fs.delete(outputPathPath, true);
    }

    jobConf.set(RecommenderMapper.RECOMMENDER_CLASS_NAME, recommendClassName);
    jobConf.set(RecommenderMapper.RECOMMENDATIONS_PER_USER, String.valueOf(recommendationsPerUser));
    jobConf.set(RecommenderMapper.DATA_MODEL_FILE, dataModelFile);

    jobConf.setInputFormat(TextInputFormat.class);
    FileInputFormat.setInputPaths(jobConf, userIDFilePath);

    jobConf.setMapperClass(RecommenderMapper.class);
    jobConf.setMapOutputKeyClass(Text.class);
    jobConf.setMapOutputValueClass(RecommendedItemsWritable.class);

    jobConf.setReducerClass(IdentityReducer.class);
    jobConf.setOutputKeyClass(Text.class);
    jobConf.setOutputValueClass(RecommendedItemsWritable.class);

    jobConf.setOutputFormat(TextOutputFormat.class);
    FileOutputFormat.setOutputPath(jobConf, outputPathPath);

    return jobConf;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1489.java