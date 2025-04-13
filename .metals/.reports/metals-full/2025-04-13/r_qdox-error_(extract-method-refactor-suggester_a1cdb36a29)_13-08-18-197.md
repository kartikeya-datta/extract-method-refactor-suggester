error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1760.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1760.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1760.java
text:
```scala
f@@romDocTerms = FieldCache.DEFAULT.getTerms(context.reader(), field, false);

package org.apache.lucene.search.join;

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

import java.io.IOException;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.index.SortedSetDocValues;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefHash;

abstract class TermsWithScoreCollector extends Collector {

  private final static int INITIAL_ARRAY_SIZE = 256;

  final String field;
  final BytesRefHash collectedTerms = new BytesRefHash();
  final ScoreMode scoreMode;

  Scorer scorer;
  float[] scoreSums = new float[INITIAL_ARRAY_SIZE];

  TermsWithScoreCollector(String field, ScoreMode scoreMode) {
    this.field = field;
    this.scoreMode = scoreMode;
  }

  public BytesRefHash getCollectedTerms() {
    return collectedTerms;
  }

  public float[] getScoresPerTerm() {
    return scoreSums;
  }

  @Override
  public void setScorer(Scorer scorer) throws IOException {
    this.scorer = scorer;
  }

  @Override
  public boolean acceptsDocsOutOfOrder() {
    return true;
  }

  /**
   * Chooses the right {@link TermsWithScoreCollector} implementation.
   *
   * @param field                     The field to collect terms for
   * @param multipleValuesPerDocument Whether the field to collect terms for has multiple values per document.
   * @return a {@link TermsWithScoreCollector} instance
   */
  static TermsWithScoreCollector create(String field, boolean multipleValuesPerDocument, ScoreMode scoreMode) {
    if (multipleValuesPerDocument) {
      switch (scoreMode) {
        case Avg:
          return new MV.Avg(field);
        default:
          return new MV(field, scoreMode);
      }
    } else {
      switch (scoreMode) {
        case Avg:
          return new SV.Avg(field);
        default:
          return new SV(field, scoreMode);
      }
    }
  }

  // impl that works with single value per document
  static class SV extends TermsWithScoreCollector {

    final BytesRef spare = new BytesRef();
    BinaryDocValues fromDocTerms;

    SV(String field, ScoreMode scoreMode) {
      super(field, scoreMode);
    }

    @Override
    public void collect(int doc) throws IOException {
      fromDocTerms.get(doc, spare);
      int ord = collectedTerms.add(spare);
      if (ord < 0) {
        ord = -ord - 1;
      } else {
        if (ord >= scoreSums.length) {
          scoreSums = ArrayUtil.grow(scoreSums);
        }
      }

      float current = scorer.score();
      float existing = scoreSums[ord];
      if (Float.compare(existing, 0.0f) == 0) {
        scoreSums[ord] = current;
      } else {
        switch (scoreMode) {
          case Total:
            scoreSums[ord] = scoreSums[ord] + current;
            break;
          case Max:
            if (current > existing) {
              scoreSums[ord] = current;
            }
        }
      }
    }

    @Override
    public void setNextReader(AtomicReaderContext context) throws IOException {
      fromDocTerms = FieldCache.DEFAULT.getTerms(context.reader(), field);
    }

    static class Avg extends SV {

      int[] scoreCounts = new int[INITIAL_ARRAY_SIZE];

      Avg(String field) {
        super(field, ScoreMode.Avg);
      }

      @Override
      public void collect(int doc) throws IOException {
        fromDocTerms.get(doc, spare);
        int ord = collectedTerms.add(spare);
        if (ord < 0) {
          ord = -ord - 1;
        } else {
          if (ord >= scoreSums.length) {
            scoreSums = ArrayUtil.grow(scoreSums);
            scoreCounts = ArrayUtil.grow(scoreCounts);
          }
        }

        float current = scorer.score();
        float existing = scoreSums[ord];
        if (Float.compare(existing, 0.0f) == 0) {
          scoreSums[ord] = current;
          scoreCounts[ord] = 1;
        } else {
          scoreSums[ord] = scoreSums[ord] + current;
          scoreCounts[ord]++;
        }
      }

      @Override
      public float[] getScoresPerTerm() {
        if (scoreCounts != null) {
          for (int i = 0; i < scoreCounts.length; i++) {
            scoreSums[i] = scoreSums[i] / scoreCounts[i];
          }
          scoreCounts = null;
        }
        return scoreSums;
      }
    }
  }

  // impl that works with multiple values per document
  static class MV extends TermsWithScoreCollector {

    SortedSetDocValues fromDocTermOrds;
    final BytesRef scratch = new BytesRef();

    MV(String field, ScoreMode scoreMode) {
      super(field, scoreMode);
    }

    @Override
    public void collect(int doc) throws IOException {
      fromDocTermOrds.setDocument(doc);
      long ord;
      while ((ord = fromDocTermOrds.nextOrd()) != SortedSetDocValues.NO_MORE_ORDS) {
        fromDocTermOrds.lookupOrd(ord, scratch);
        
        int termID = collectedTerms.add(scratch);
        if (termID < 0) {
          termID = -termID - 1;
        } else {
          if (termID >= scoreSums.length) {
            scoreSums = ArrayUtil.grow(scoreSums);
          }
        }
        
        switch (scoreMode) {
          case Total:
            scoreSums[termID] += scorer.score();
            break;
          case Max:
            scoreSums[termID] = Math.max(scoreSums[termID], scorer.score());
        }
      }
    }

    @Override
    public void setNextReader(AtomicReaderContext context) throws IOException {
      fromDocTermOrds = FieldCache.DEFAULT.getDocTermOrds(context.reader(), field);
    }

    static class Avg extends MV {

      int[] scoreCounts = new int[INITIAL_ARRAY_SIZE];

      Avg(String field) {
        super(field, ScoreMode.Avg);
      }

      @Override
      public void collect(int doc) throws IOException {
        fromDocTermOrds.setDocument(doc);
        long ord;
        while ((ord = fromDocTermOrds.nextOrd()) != SortedSetDocValues.NO_MORE_ORDS) {
          fromDocTermOrds.lookupOrd(ord, scratch);
          
          int termID = collectedTerms.add(scratch);
          if (termID < 0) {
            termID = -termID - 1;
          } else {
            if (termID >= scoreSums.length) {
              scoreSums = ArrayUtil.grow(scoreSums);
            }
          }
          
          scoreSums[termID] += scorer.score();
          scoreCounts[termID]++;
        }
      }

      @Override
      public float[] getScoresPerTerm() {
        if (scoreCounts != null) {
          for (int i = 0; i < scoreCounts.length; i++) {
            scoreSums[i] = scoreSums[i] / scoreCounts[i];
          }
          scoreCounts = null;
        }
        return scoreSums;
      }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1760.java