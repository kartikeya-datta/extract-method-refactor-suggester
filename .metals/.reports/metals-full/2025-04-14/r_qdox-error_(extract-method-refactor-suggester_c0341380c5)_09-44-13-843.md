error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2735.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2735.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2735.java
text:
```scala
P@@arameters params = new Parameters(job.get("bayes.parameters", ""));

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

package org.apache.mahout.classifier.bayes.mapreduce.bayes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.DefaultStringifier;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.GenericsUtil;
import org.apache.mahout.classifier.bayes.mapreduce.common.BayesConstants;
import org.apache.mahout.common.Parameters;
import org.apache.mahout.common.StringTuple;
import org.apache.mahout.math.map.OpenObjectDoubleHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mapper for Calculating the ThetaNormalizer for a label in Naive Bayes Algorithm
 * 
 */
public class BayesThetaNormalizerMapper extends MapReduceBase implements
    Mapper<StringTuple,DoubleWritable,StringTuple,DoubleWritable> {
  
  private static final Logger log = LoggerFactory.getLogger(BayesThetaNormalizerMapper.class);
  
  private final OpenObjectDoubleHashMap<String> labelWeightSum = new OpenObjectDoubleHashMap<String>();
  private double sigmaJSigmaK;
  private double vocabCount;
  private double alphaI = 1.0;
  
  /**
   * We need to calculate the thetaNormalization factor of each label
   * 
   * @param key
   *          The label,feature pair
   * @param value
   *          The tfIdf of the pair
   */
  @Override
  public void map(StringTuple key,
                  DoubleWritable value,
                  OutputCollector<StringTuple,DoubleWritable> output,
                  Reporter reporter) throws IOException {
    
    String label = key.stringAt(1);
    
    reporter.setStatus("Bayes Theta Normalizer Mapper: " + label);
    
    double weight = Math.log((value.get() + alphaI) / (labelWeightSum.get(label) + vocabCount));
    StringTuple thetaNormalizerTuple = new StringTuple(BayesConstants.LABEL_THETA_NORMALIZER);
    thetaNormalizerTuple.add(label);
    output.collect(thetaNormalizerTuple, new DoubleWritable(weight));
  }
  
  @Override
  public void configure(JobConf job) {
    try {
      labelWeightSum.clear();
      Map<String,Double> labelWeightSumTemp = new HashMap<String,Double>();
      
      DefaultStringifier<Map<String,Double>> mapStringifier = new DefaultStringifier<Map<String,Double>>(job,
          GenericsUtil.getClass(labelWeightSumTemp));
      
      String labelWeightSumString = job.get("cnaivebayes.sigma_k", mapStringifier.toString(labelWeightSumTemp));
      labelWeightSumTemp = mapStringifier.fromString(labelWeightSumString);
      for (Map.Entry<String, Double> stringDoubleEntry : labelWeightSumTemp.entrySet()) {
        this.labelWeightSum.put(stringDoubleEntry.getKey(), stringDoubleEntry.getValue());
      }
      DefaultStringifier<Double> stringifier = new DefaultStringifier<Double>(job, GenericsUtil
          .getClass(sigmaJSigmaK));
      String sigmaJSigmaKString = job.get("cnaivebayes.sigma_jSigma_k", stringifier.toString(sigmaJSigmaK));
      sigmaJSigmaK = stringifier.fromString(sigmaJSigmaKString);
      
      String vocabCountString = stringifier.toString(vocabCount);
      vocabCountString = job.get("cnaivebayes.vocabCount", vocabCountString);
      vocabCount = stringifier.fromString(vocabCountString);
      
      Parameters params = Parameters.fromString(job.get("bayes.parameters", ""));
      alphaI = Double.valueOf(params.get("alpha_i", "1.0"));
      
    } catch (IOException ex) {
      log.warn(ex.toString(), ex);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2735.java