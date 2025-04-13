error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1092.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1092.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1092.java
text:
```scala
a@@ddOption("usersFile", "u", "File of users to recommend for", null);

/*
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

package org.apache.mahout.cf.taste.hadoop.pseudo;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.common.AbstractJob;
import org.apache.mahout.cf.taste.hadoop.RecommendedItemsWritable;
import org.apache.mahout.math.VarLongWritable;

/**
 * <p>
 * This job runs a "pseudo-distributed" recommendation process on Hadoop. It merely runs many
 * {@link org.apache.mahout.cf.taste.recommender.Recommender} instances on Hadoop,
 * where each instance is a normal non-distributed implementation.
 * </p>
 *
 * <p>This class configures and runs a {@link RecommenderReducer} using Hadoop.</p>
 *
 * <p>Command line arguments specific to this class are:</p>
 *
 * <ol>
 * <li>-Dmapred.input.dir=(path): Location of a data model file containing preference data, suitable for use with
 * {@link org.apache.mahout.cf.taste.impl.model.file.FileDataModel}</li>
 * <li>-Dmapred.output.dir=(path): output path where recommender output should go</li>
 * <li>--recommenderClassName (string): Fully-qualified class name of
 * {@link org.apache.mahout.cf.taste.recommender.Recommender} to use to make recommendations.
 * Note that it must have a constructor which takes a {@link org.apache.mahout.cf.taste.model.DataModel}
 * argument.</li>
 * <li>--numRecommendations (integer): Number of recommendations to compute per user</li>
 * <li>--usersFile (path): file containing user IDs to recommend for (optional)</li>
 * </ol>
 *
 * <p>General command line options are documented in {@link AbstractJob}.</p>
 *
 * <p>Note that because of how Hadoop parses arguments, all "-D" arguments must appear before all other
 * arguments.</p>
 *
 * <p>
 * For example, to get started trying this out, set up Hadoop in a pseudo-distributed manner:
 * http://hadoop.apache.org/common/docs/current/quickstart.html You can stop at the point where it instructs
 * you to copy files into HDFS.
 * </p>
 *
 * <p>
 * Assume your preference data file is {@code input.csv}. You will also need to create a file containing
 * all user IDs to write recommendations for, as something like {@code users.txt}. Place this input on
 * HDFS like so:
 * </p>
 *
 * {@code hadoop fs -put input.csv input/input.csv; hadoop fs -put users.txt input/users.txt * }
 *
 * <p>
 * Build Mahout code with {@code mvn package} in the core/ directory. Locate
 * {@code target/mahout-core-X.Y-SNAPSHOT.job}. This is a JAR file; copy it out to a convenient location
 * and name it {@code recommender.jar}.
 * </p>
 *
 * <p>
 * Now add your own custom recommender code and dependencies. Your IDE produced compiled .class files
 * somewhere and they need to be packaged up as well:
 * </p>
 *
 * {@code jar uf recommender.jar -C (your classes directory) . * }
 *
 * <p>
 * And launch:
 * </p>
 *
 * {@code hadoop jar recommender.jar \
 *   org.apache.mahout.cf.taste.hadoop.pseudo.RecommenderJob \
 *   -Dmapred.input.dir=input/users.csv \
 *   -Dmapred.output.dir=output \
 *   --recommenderClassName your.project.Recommender \
 *   --numRecommendations 10  *
 * }
 */
public final class RecommenderJob extends AbstractJob {
  
  @Override
  public int run(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

    addInputOption();
    addOutputOption();
    addOption("recommenderClassName", "r", "Name of recommender class to instantiate");
    addOption("numRecommendations", "n", "Number of recommendations per user", "10");
    addOption("usersFile", "u", "Number of recommendations per user", null);
    
    Map<String,String> parsedArgs = parseArguments(args);
    if (parsedArgs == null) {
      return -1;
    }

    Path inputFile = getInputPath();
    Path outputPath = getOutputPath();
    Path usersFile = parsedArgs.get("--usersFile") == null ? inputFile : new Path(parsedArgs.get("--usersFile"));
    
    String recommendClassName = parsedArgs.get("--recommenderClassName");
    int recommendationsPerUser = Integer.parseInt(parsedArgs.get("--numRecommendations"));
    
    Job job = prepareJob(usersFile,
                         outputPath,
                         TextInputFormat.class,
                         UserIDsMapper.class,
                         VarLongWritable.class,
                         NullWritable.class,
                         RecommenderReducer.class,
                         VarLongWritable.class,
                         RecommendedItemsWritable.class,
                         TextOutputFormat.class);
    FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
    Configuration jobConf = job.getConfiguration();
    jobConf.set(RecommenderReducer.RECOMMENDER_CLASS_NAME, recommendClassName);
    jobConf.setInt(RecommenderReducer.RECOMMENDATIONS_PER_USER, recommendationsPerUser);
    jobConf.set(RecommenderReducer.DATA_MODEL_FILE, inputFile.toString());

    job.waitForCompletion(true);
    return 0;
  }
  
  public static void main(String[] args) throws Exception {
    ToolRunner.run(new RecommenderJob(), args);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1092.java