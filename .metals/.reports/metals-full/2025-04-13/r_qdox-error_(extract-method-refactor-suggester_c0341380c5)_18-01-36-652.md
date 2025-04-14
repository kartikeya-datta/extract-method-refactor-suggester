error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1512.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1512.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1512.java
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

package org.apache.mahout.classifier.bayes;

import org.apache.hadoop.util.GenericsUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.DefaultStringifier;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

/**
 * Create and run the Wikipedia Dataset Creator.
 */
public class WikipediaDatasetCreatorDriver {
  private WikipediaDatasetCreatorDriver() {
  }

  /**
   * Takes in two arguments:
   * <ol>
   * <li>The input {@link org.apache.hadoop.fs.Path} where the input documents live</li>
   * <li>The output {@link org.apache.hadoop.fs.Path} where to write the
   * {@link org.apache.mahout.classifier.bayes.BayesModel} as a {@link org.apache.hadoop.io.SequenceFile}</li>
   * </ol>
   * @param args The args
   */
  public static void main(String[] args) throws IOException {
    String input = args[0];
    String output = args[1];
    String countriesFile = args[2];

    runJob(input, output,countriesFile);
  }

  /**
   * Run the job
   *
   * @param input            the input pathname String
   * @param output           the output pathname String
   */
  public static void runJob(String input, String output, String countriesFile) throws IOException {
    JobClient client = new JobClient();
    JobConf conf = new JobConf(WikipediaDatasetCreatorDriver.class);

    conf.set("key.value.separator.in.input.line", " ");
    conf.set("xmlinput.start", "<text xml:space=\"preserve\">");
    conf.set("xmlinput.end", "</text>");
    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(Text.class);

    FileInputFormat.setInputPaths(conf, new Path(input));
    Path outPath = new Path(output);
    FileOutputFormat.setOutputPath(conf, outPath);

    conf.setMapperClass(WikipediaDatasetCreatorMapper.class);
    conf.setNumMapTasks(100);
    conf.setInputFormat(XmlInputFormat.class);
    //conf.setCombinerClass(WikipediaDatasetCreatorReducer.class);
    conf.setReducerClass(WikipediaDatasetCreatorReducer.class);
    conf.setOutputFormat(WikipediaDatasetCreatorOutputFormat.class);
    conf.set("io.serializations",
             "org.apache.hadoop.io.serializer.JavaSerialization,org.apache.hadoop.io.serializer.WritableSerialization");
    // Dont ever forget this. People should keep track of how hadoop conf parameters and make or break a piece of code

    FileSystem dfs = FileSystem.get(conf);
    if (dfs.exists(outPath))
      dfs.delete(outPath, true);

    Set<String> countries= new HashSet<String>();


    BufferedReader reader = new BufferedReader(new InputStreamReader(
        new FileInputStream(countriesFile), "UTF-8"));
    String line;
    while((line = reader.readLine())!=null){
      countries.add(line);
    }
    reader.close();

    DefaultStringifier<Set<String>> setStringifier = new DefaultStringifier<Set<String>>(conf,GenericsUtil.getClass(countries));

    String countriesString = setStringifier.toString(countries);

    conf.set("wikipedia.countries", countriesString);

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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1512.java