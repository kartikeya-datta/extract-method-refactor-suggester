error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2730.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2730.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2730.java
text:
```scala
i@@f (params.isSkipCleanup()) {

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

package org.apache.mahout.classifier.bayes.mapreduce.cbayes;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.mahout.classifier.bayes.common.BayesParameters;
import org.apache.mahout.classifier.bayes.mapreduce.common.BayesFeatureDriver;
import org.apache.mahout.classifier.bayes.mapreduce.common.BayesJob;
import org.apache.mahout.classifier.bayes.mapreduce.common.BayesTfIdfDriver;
import org.apache.mahout.classifier.bayes.mapreduce.common.BayesWeightSummerDriver;
import org.apache.mahout.common.HadoopUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Create and run the Bayes Trainer. */
public class CBayesDriver implements BayesJob {
  
  private static final Logger log = LoggerFactory.getLogger(CBayesDriver.class);

  @Override
  public void runJob(Path input, Path output, BayesParameters params) throws IOException {
    HadoopUtil.overwriteOutput(output);
    
    log.info("Reading features...");
    // Read the features in each document normalized by length of each document
    BayesFeatureDriver feature = new BayesFeatureDriver();
    feature.runJob(input, output, params);
    
    log.info("Calculating Tf-Idf...");
    // Calculate the TfIdf for each word in each label
    BayesTfIdfDriver tfidf = new BayesTfIdfDriver();
    tfidf.runJob(input, output, params);
    
    log.info("Calculating weight sums for labels and features...");
    // Calculate the Sums of weights for each label, for each feature and for
    // each feature and for each label
    BayesWeightSummerDriver summer = new BayesWeightSummerDriver();
    summer.runJob(input, output, params);
    
    log.info("Calculating the weight Normalisation factor for each complement class...");
    // Calculate the normalization factor Sigma_W_ij for each complement class.
    CBayesThetaNormalizerDriver normalizer = new CBayesThetaNormalizerDriver();
    normalizer.runJob(input, output, params);
    
    if (Boolean.parseBoolean(params.get("skipCleanup"))) {
      return;
    }
    
    Path docCountOutPath = new Path(output, "trainer-docCount");
    HadoopUtil.overwriteOutput(docCountOutPath);

    Path termDocCountOutPath = new Path(output, "trainer-termDocCount");
    HadoopUtil.overwriteOutput(termDocCountOutPath);

    Path featureCountOutPath = new Path(output, "trainer-featureCount");
    HadoopUtil.overwriteOutput(featureCountOutPath);

    Path wordFreqOutPath = new Path(output, "trainer-wordFreq");
    HadoopUtil.overwriteOutput(wordFreqOutPath);

    Path vocabCountPath = new Path(output, "trainer-tfIdf/trainer-vocabCount");
    HadoopUtil.overwriteOutput(vocabCountPath);

    Path vocabCountOutPath = new Path(output, "trainer-vocabCount");
    HadoopUtil.overwriteOutput(vocabCountOutPath);

    
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2730.java