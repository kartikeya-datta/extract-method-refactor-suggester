error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1896.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1896.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1896.java
text:
```scala
v@@alue += 1 / (mu * ((totalTermFrequency+1L)/(double)(sumOfTotalTermFreq+1L)));

package org.apache.lucene.search;

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

import java.io.IOException;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.IndexReader.AtomicReaderContext;
import org.apache.lucene.search.Explanation.IDFExplanation;
import org.apache.lucene.util.PerReaderTermState;
import org.apache.lucene.util.SmallFloat;

/**
 * Dirichlet LM Similarity.
 * <p>
 * This uses Terrier's modified formula "Bayesian smoothing with Dirichlet Prior" (which ensures only positive scores)
 * from Zhai & Lafferty's A Study of Smoothing Methods for Language Models Applied to Information Retrieval.
 * The formula has been modified in several ways:
 * <ul>
 *   <li>Supports doc/field/query boosting
 *   <li>Uses natural log instead of base2 for simplicity.
 *   <li>The formula has been re-arranged: as part is computed in the weight, we use totalTermFrequency+1/sumOfTotalTermFrequency+1
 *       to prevent any divide by zero, other parts of the formula re-arranged for performance
 * </ul>
 * </p>
 * <code>
 * log(1 + (tf/(mu * (totalTermFrequency / sumOfTotalTermFrequency)))) + log(mu / (numTerms + mu))
 * </code>
 * <p>
 * NOTE: to use this Similarity, use MockLMSimilarityProvider (as this formula already incorporates coord()
 * and currently depends upon a disabled queryNorm) 
 */
public class MockLMSimilarity extends Similarity {
  // TODO: the norm table can probably be per-sim so you can configure this.
  // its also pretty nice that we don't bake the parameter into the index... you can tune it at runtime.
  private static final float mu = 2000f;
  
  /**
   * Our normalization is log(mu / (doclen + mu))
   * currently we put doclen into the boost byte (divided by boost) for simple quantization
   * our decoder precomputes the full formula into the norm table
   */
  @Override
  public float computeNorm(FieldInvertState state) {
    final int numTerms = state.getLength() - state.getNumOverlap();
    return numTerms / state.getBoost();
  }
  
  /** Cache of decoded bytes. */
  private static final float[] NORM_TABLE = new float[256];

  static {
    for (int i = 0; i < 256; i++) {
      float doclen = SmallFloat.byte315ToFloat((byte)i);
      NORM_TABLE[i] = (float) Math.log(mu / (doclen + mu));
    }
  }
  
  @Override
  public float decodeNormValue(byte b) {
    return NORM_TABLE[b & 0xFF];
  }

  @Override
  public byte encodeNormValue(float f) {
    return SmallFloat.floatToByte315(f);
  }

  @Override
  public float sloppyFreq(int distance) {
    return 1.0f / (distance + 1);
  }

  // weight for a term as 1 / (mu * (totalTermFrequency / sumOfTotalTermFrequency))
  // nocommit: nuke IDFExplanation!
  // nocommit: evil how we shove this crap in weight and unsquare it.. need to generalize weight
  @Override
  public IDFExplanation computeWeight(IndexSearcher searcher, String fieldName, PerReaderTermState... termStats) throws IOException {
    float value = 0.0f;
    final StringBuilder exp = new StringBuilder();
    final long sumOfTotalTermFreq = MultiFields.getTerms(searcher.getIndexReader(), fieldName).getSumTotalTermFreq();
    
    for (final PerReaderTermState stat : termStats ) {
      final long totalTermFrequency = stat.totalTermFreq();
      value += 1 / (mu * (totalTermFrequency+1L/(double)(sumOfTotalTermFreq+1L)));
      exp.append(" ");
      exp.append(totalTermFrequency);
    }
    
    final float idfValue = value;
    return new IDFExplanation() {
      @Override
      public float getIdf() {
        return idfValue;
      }
      @Override
      public String explain() {
        return exp.toString();
      }
    };
  }

  @Override
  public ExactDocScorer exactDocScorer(Weight weight, String fieldName, AtomicReaderContext context) throws IOException {
    float unsquaredWeight = (float) Math.sqrt(weight.getValue());
    final byte norms[] = context.reader.norms(fieldName);
    return norms == null
    ? new RawExactMockLMDocScorer(unsquaredWeight)
    : new ExactMockLMDocScorer(unsquaredWeight, norms);
  }

  @Override
  public SloppyDocScorer sloppyDocScorer(Weight weight, String fieldName, AtomicReaderContext context) throws IOException {
    return new SloppyMockLMDocScorer((float) Math.sqrt(weight.getValue()), context.reader.norms(fieldName));
  }
  
  /**
   * log(1 + (tf/(mu * (totalTermFrequency / sumOfTotalTermFrequency))) ) + log(mu / (numTerms + mu))
   */
  private class ExactMockLMDocScorer extends ExactDocScorer {
    private final float weightValue;
    private final byte[] norms;
    private static final int SCORE_CACHE_SIZE = 32;
    private float[] scoreCache = new float[SCORE_CACHE_SIZE];
    
    ExactMockLMDocScorer(float weightValue, byte norms[]) {
      this.weightValue = weightValue;
      this.norms = norms;
      for (int i = 0; i < SCORE_CACHE_SIZE; i++)
        scoreCache[i] = (float)Math.log(1 + (i*weightValue));
    }
    
    @Override
    public float score(int doc, int freq) {
      return freq < SCORE_CACHE_SIZE                        // check cache
      ? scoreCache[freq] + decodeNormValue(norms[doc])      // cache hit
      : (float)Math.log(1 + (freq*weightValue)) + decodeNormValue(norms[doc]);  // cache miss
    }
  }
  
  private class RawExactMockLMDocScorer extends ExactDocScorer {
    private final float weightValue;
    private static final int SCORE_CACHE_SIZE = 32;
    private float[] scoreCache = new float[SCORE_CACHE_SIZE];
    
    RawExactMockLMDocScorer(float weightValue) {
      this.weightValue = weightValue;
      for (int i = 0; i < SCORE_CACHE_SIZE; i++)
        scoreCache[i] = (float)Math.log(1 + (i*weightValue));
    }
    
    @Override
    public float score(int doc, int freq) {
      return freq < SCORE_CACHE_SIZE    // check cache
      ? scoreCache[freq]                // cache hit
      : (float)Math.log(1 + (freq*weightValue));  // cache miss
    }
  }
  
  // TODO: worth specializing?
  private class SloppyMockLMDocScorer extends SloppyDocScorer {
    private final float weightValue;
    private final byte[] norms;
    
    SloppyMockLMDocScorer(float weightValue, byte norms[]) {
      this.weightValue = weightValue;
      this.norms = norms;
    }
    
    @Override
    public float score(int doc, float freq) {
      final float norm = (norms == null) ? 0 : decodeNormValue(norms[doc]);
      return (float)Math.log(1 + (freq*weightValue)) + norm;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1896.java