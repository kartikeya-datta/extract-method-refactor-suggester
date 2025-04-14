error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2510.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2510.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2510.java
text:
```scala
public v@@oid setSamplingThreshold(int samplingThreshold) {

package org.apache.lucene.facet.search.sampling;

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

/**
 * Parameters for sampling, dictating whether sampling is to take place and how. 
 * 
 * @lucene.experimental
 */
public class SamplingParams {

  /**
   * Default factor by which more results are requested over the sample set.
   * @see SamplingParams#getOversampleFactor()
   */
  public static final double DEFAULT_OVERSAMPLE_FACTOR = 2d;
  
  /**
   * Default ratio between size of sample to original size of document set.
   * @see Sampler#getSampleSet(org.apache.lucene.facet.search.ScoredDocIDs)
   */
  public static final double DEFAULT_SAMPLE_RATIO = 0.01;
  
  /**
   * Default maximum size of sample.
   * @see Sampler#getSampleSet(org.apache.lucene.facet.search.ScoredDocIDs)
   */
  public static final int DEFAULT_MAX_SAMPLE_SIZE = 10000;
  
  /**
   * Default minimum size of sample.
   * @see Sampler#getSampleSet(org.apache.lucene.facet.search.ScoredDocIDs)
   */
  public static final int DEFAULT_MIN_SAMPLE_SIZE = 100;
  
  /**
   * Default sampling threshold, if number of results is less than this number - no sampling will take place
   * @see SamplingParams#getSampleRatio()
   */
  public static final int DEFAULT_SAMPLING_THRESHOLD = 75000;

  private int maxSampleSize = DEFAULT_MAX_SAMPLE_SIZE;
  private int minSampleSize = DEFAULT_MIN_SAMPLE_SIZE;
  private double sampleRatio = DEFAULT_SAMPLE_RATIO;
  private int samplingThreshold = DEFAULT_SAMPLING_THRESHOLD;
  private double oversampleFactor = DEFAULT_OVERSAMPLE_FACTOR;
  
  /**
   * Return the maxSampleSize.
   * In no case should the resulting sample size exceed this value.  
   * @see Sampler#getSampleSet(org.apache.lucene.facet.search.ScoredDocIDs)
   */
  public final int getMaxSampleSize() {
    return maxSampleSize;
  }

  /**
   * Return the minSampleSize.
   * In no case should the resulting sample size be smaller than this value.  
   * @see Sampler#getSampleSet(org.apache.lucene.facet.search.ScoredDocIDs)
   */
  public final int getMinSampleSize() {
    return minSampleSize;
  }

  /**
   * @return the sampleRatio
   * @see Sampler#getSampleSet(org.apache.lucene.facet.search.ScoredDocIDs)
   */
  public final double getSampleRatio() {
    return sampleRatio;
  }
  
  /**
   * Return the samplingThreshold.
   * Sampling would be performed only for document sets larger than this.  
   */
  public final int getSamplingThreshold() {
    return samplingThreshold;
  }

  /**
   * @param maxSampleSize
   *          the maxSampleSize to set
   * @see #getMaxSampleSize()
   */
  public void setMaxSampleSize(int maxSampleSize) {
    this.maxSampleSize = maxSampleSize;
  }

  /**
   * @param minSampleSize
   *          the minSampleSize to set
   * @see #getMinSampleSize()
   */
  public void setMinSampleSize(int minSampleSize) {
    this.minSampleSize = minSampleSize;
  }

  /**
   * @param sampleRatio
   *          the sampleRatio to set
   * @see #getSampleRatio()
   */
  public void setSampleRatio(double sampleRatio) {
    this.sampleRatio = sampleRatio;
  }

  /**
   * Set a sampling-threshold
   * @see #getSamplingThreshold()
   */
  public void setSampingThreshold(int samplingThreshold) {
    this.samplingThreshold = samplingThreshold;
  }

  /**
   * Check validity of sampling settings, making sure that
   * <ul>
   * <li> <code>minSampleSize <= maxSampleSize <= samplingThreshold </code></li>
   * <li> <code>0 < samplingRatio <= 1 </code></li>
   * </ul> 
   * 
   * @return true if valid, false otherwise
   */
  public boolean validate() {
    return 
      samplingThreshold >= maxSampleSize && 
      maxSampleSize >= minSampleSize && 
      sampleRatio > 0 &&
      sampleRatio < 1;
  }

  /**
   * Return the oversampleFactor. When sampling, we would collect that much more
   * results, so that later, when selecting top out of these, chances are higher
   * to get actual best results. Note that having this value larger than 1 only
   * makes sense when using a SampleFixer which finds accurate results, such as
   * <code>TakmiSampleFixer</code>. When this value is smaller than 1, it is
   * ignored and no oversampling takes place.
   */
  public final double getOversampleFactor() {
    return oversampleFactor;
  }

  /**
   * @param oversampleFactor the oversampleFactor to set
   * @see #getOversampleFactor()
   */
  public void setOversampleFactor(double oversampleFactor) {
    this.oversampleFactor = oversampleFactor;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2510.java