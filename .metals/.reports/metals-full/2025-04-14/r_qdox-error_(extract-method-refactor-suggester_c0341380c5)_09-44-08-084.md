error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1824.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1824.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1824.java
text:
```scala
public f@@loat tf(float freq) {

package org.apache.solr.search.similarities;

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

import org.apache.lucene.misc.SweetSpotSimilarity;
import org.apache.lucene.search.similarities.DefaultSimilarity; // jdoc
import org.apache.lucene.search.similarities.Similarity;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.schema.SimilarityFactory;
import org.apache.solr.common.SolrException;
import static org.apache.solr.common.SolrException.ErrorCode.*;
/**
 * <p>Factory for {@link SweetSpotSimilarity}.</p>
 * <p>
 * <code>SweetSpotSimilarity</code> is an extension of 
 * {@link DefaultSimilarity} that provides additional tuning options for 
 * specifying the "sweetspot" of optimal <code>tf</code> and 
 * <code>lengthNorm</code> values in the source data.
 * </p>
 * <p>
 * In addition to the <code>discountOverlaps</code> init param supported by 
 * {@link DefaultSimilarityFactory} The following sets of init params are 
 * supported by this factory:
 * </p>
 * <ul>
 *   <li>Length Norm Settings: <ul>
 *     <li><code>lengthNormMin</code> (int)</li>
 *     <li><code>lengthNormMax</code> (int)</li>
 *     <li><code>lengthNormSteepness</code> (float)</li>
 *   </ul></li>
 *   <li>Baseline TF Settings: <ul>
 *     <li><code>baselineTfBase</code> (float)</li>
 *     <li><code>baselineTfMin</code> (float)</li>
 *   </ul></li>
 *   <li>Hyperbolic TF Settings: <ul>
 *     <li><code>hyperbolicTfMin</code> (float)</li>
 *     <li><code>hyperbolicTfMax</code> (float)</li>
 *     <li><code>hyperbolicTfBase</code> (double)</li>
 *     <li><code>hyperbolicTfOffset</code> (float)</li>
 *   </ul></li>
 * </ul>
 * <p>
 * Note:
 * </p>
 * <ul>
 *  <li>If any individual settings from one of the above mentioned sets 
 *      are specified, then all settings from that set must be specified.
 *  </li>
 *  <li>If Baseline TF settings are spcified, then Hyperbolic TF settings 
 *      are not permitted, and vice versa. (The settings specified will 
 *      determine wether {@link SweetSpotSimilarity#baselineTf} or 
 *      {@link SweetSpotSimilarity#hyperbolicTf} will be used.
 *  </li>
 * </ul>
 * <p>
 * Example usage...
 * </p>
 * <pre class="prettyprint">
 * &lt;!-- using baseline TF --&gt;
 * &lt;fieldType name="text_baseline" class="solr.TextField"
 *            indexed="true" stored="false"&gt;
 *   &lt;analyzer class="org.apache.lucene.analysis.standard.StandardAnalyzer"/&gt;
 *   &lt;similarity class="solr.SweetSpotSimilarityFactory"&gt;
 *     &lt;!-- TF --&gt;
 *     &lt;float name="baselineTfMin"&gt;6.0&lt;/float&gt;
 *     &lt;float name="baselineTfBase"&gt;1.5&lt;/float&gt;
 *     &lt;!-- plateau norm --&gt;
 *     &lt;int name="lengthNormMin"&gt;3&lt;/int&gt;
 *     &lt;int name="lengthNormMax"&gt;5&lt;/int&gt;
 *     &lt;float name="lengthNormSteepness"&gt;0.5&lt;/float&gt;
 *   &lt;/similarity&gt;
 * &lt;/fieldType&gt;
 * 
 * &lt;!-- using hyperbolic TF --&gt;
 * &lt;fieldType name="text_hyperbolic" class="solr.TextField"
 *            indexed="true" stored="false" &gt;
 *   &lt;analyzer class="org.apache.lucene.analysis.standard.StandardAnalyzer"/&gt;
 *   &lt;similarity class="solr.SweetSpotSimilarityFactory"&gt;
 *     &lt;float name="hyperbolicTfMin"&gt;3.3&lt;/float&gt;
 *     &lt;float name="hyperbolicTfMax"&gt;7.7&lt;/float&gt;
 *     &lt;double name="hyperbolicTfBase"&gt;2.718281828459045&lt;/double&gt; &lt;!-- e --&gt;
 *     &lt;float name="hyperbolicTfOffset"&gt;5.0&lt;/float&gt;
 *     &lt;!-- plateau norm, shallower slope --&gt;
 *     &lt;int name="lengthNormMin"&gt;1&lt;/int&gt;
 *     &lt;int name="lengthNormMax"&gt;5&lt;/int&gt;
 *     &lt;float name="lengthNormSteepness"&gt;0.2&lt;/float&gt;
 *   &lt;/similarity&gt;
 * &lt;/fieldType&gt;
 * </pre>
 * @see SweetSpotSimilarity The javadocs for the individual methods in 
 *      <code>SweetSpotSimilarity</code> for SVG diagrams showing how the 
 *      each function behaves with various settings/inputs.
 */
public class SweetSpotSimilarityFactory extends DefaultSimilarityFactory {
  private SweetSpotSimilarity sim = null;

  @Override
  public void init(SolrParams params) {
    super.init(params);

    Integer ln_min = params.getInt("lengthNormMin");
    Integer ln_max = params.getInt("lengthNormMax");
    Float ln_steep = params.getFloat("lengthNormSteepness");
    if (! allOrNoneNull(ln_min, ln_max, ln_steep)) {
      throw new SolrException(SERVER_ERROR, "Overriding default lengthNorm settings requires all to be specified: lengthNormMin, lengthNormMax, lengthNormSteepness");
    }

    Float hyper_min = params.getFloat("hyperbolicTfMin");
    Float hyper_max = params.getFloat("hyperbolicTfMax");
    Double hyper_base = params.getDouble("hyperbolicTfBase");
    Float hyper_offset = params.getFloat("hyperbolicTfOffset");
    if (! allOrNoneNull(hyper_min, hyper_max, hyper_base, hyper_offset)) {
      throw new SolrException(SERVER_ERROR, "Overriding default hyperbolicTf settings requires all to be specified: hyperbolicTfMin, hyperbolicTfMax, hyperbolicTfBase, hyperbolicTfOffset");
    }

    Float baseline_base = params.getFloat("baselineTfBase");
    Float baseline_min = params.getFloat("baselineTfMin");
    if (! allOrNoneNull(baseline_min, baseline_base)) {
      throw new SolrException(SERVER_ERROR, "Overriding default baselineTf settings requires all to be specified: baselineTfBase, baselineTfMin");
    }

    // sanity check that they aren't trying to use two diff tf impls
    if ((null != hyper_min) && (null != baseline_min)) {
      throw new SolrException(SERVER_ERROR, "Can not mix hyperbolicTf settings with baselineTf settings");
    }

    // pick Similarity impl based on wether hyper tf settings are set
    sim = (null != hyper_min) ? new HyperbolicSweetSpotSimilarity() 
      : new SweetSpotSimilarity();
    
    if (null != ln_min) {
      // overlaps already handled by super factory
      sim.setLengthNormFactors(ln_min, ln_max, ln_steep, this.discountOverlaps);
    }

    if (null != hyper_min) {
      sim.setHyperbolicTfFactors(hyper_min, hyper_max, hyper_base, hyper_offset);
    }

    if (null != baseline_min) {
      sim.setBaselineTfFactors(baseline_base, baseline_min);
    }
  }

  @Override
  public Similarity getSimilarity() {
    assert sim != null : "SweetSpotSimilarityFactory was not initalized";
    return sim;
  }
  
  /** 
   * Returns true if either: all of the specified arguments are null;
   * or none of the specified arguments are null
   */
  private static boolean allOrNoneNull(Object... args) {
    int nulls = 0;
    int objs = 0;
    for (Object o : args) {
      objs++;
      if (null == o) nulls++;
    }
    return (0 == nulls || nulls == objs);
  }

  private static final class HyperbolicSweetSpotSimilarity 
    extends SweetSpotSimilarity {
    @Override
    public float tf(int freq) {
      return hyperbolicTf(freq);
    }
  };
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1824.java