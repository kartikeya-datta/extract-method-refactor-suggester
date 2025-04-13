error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/467.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/467.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/467.java
text:
```scala
public f@@loat score() {

package org.apache.lucene.search;

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
import java.util.Arrays;

import org.apache.lucene.index.*;
import org.apache.lucene.search.similarities.Similarity;

final class ExactPhraseScorer extends Scorer {
  private final int endMinus1;

  private final static int CHUNK = 4096;

  private int gen;
  private final int[] counts = new int[CHUNK];
  private final int[] gens = new int[CHUNK];

  boolean noDocs;

  private final static class ChunkState {
    final DocsAndPositionsEnum posEnum;
    final int offset;
    final boolean useAdvance;
    int posUpto;
    int posLimit;
    int pos;
    int lastPos;

    public ChunkState(DocsAndPositionsEnum posEnum, int offset, boolean useAdvance) {
      this.posEnum = posEnum;
      this.offset = offset;
      this.useAdvance = useAdvance;
    }
  }

  private final ChunkState[] chunkStates;

  private int docID = -1;
  private int freq;

  private final Similarity.ExactSimScorer docScorer;
  
  ExactPhraseScorer(Weight weight, PhraseQuery.PostingsAndFreq[] postings,
                    Similarity.ExactSimScorer docScorer) throws IOException {
    super(weight);
    this.docScorer = docScorer;

    chunkStates = new ChunkState[postings.length];

    endMinus1 = postings.length-1;

    for(int i=0;i<postings.length;i++) {

      // Coarse optimization: advance(target) is fairly
      // costly, so, if the relative freq of the 2nd
      // rarest term is not that much (> 1/5th) rarer than
      // the first term, then we just use .nextDoc() when
      // ANDing.  This buys ~15% gain for phrases where
      // freq of rarest 2 terms is close:
      final boolean useAdvance = postings[i].docFreq > 5*postings[0].docFreq;
      chunkStates[i] = new ChunkState(postings[i].postings, -postings[i].position, useAdvance);
      if (i > 0 && postings[i].postings.nextDoc() == DocIdSetIterator.NO_MORE_DOCS) {
        noDocs = true;
        return;
      }
    }
  }

  @Override
  public int nextDoc() throws IOException {
    while(true) {

      // first (rarest) term
      final int doc = chunkStates[0].posEnum.nextDoc();
      if (doc == DocIdSetIterator.NO_MORE_DOCS) {
        docID = doc;
        return doc;
      }

      // not-first terms
      int i = 1;
      while(i < chunkStates.length) {
        final ChunkState cs = chunkStates[i];
        int doc2 = cs.posEnum.docID();
        if (cs.useAdvance) {
          if (doc2 < doc) {
            doc2 = cs.posEnum.advance(doc);
          }
        } else {
          int iter = 0;
          while(doc2 < doc) {
            // safety net -- fallback to .advance if we've
            // done too many .nextDocs
            if (++iter == 50) {
              doc2 = cs.posEnum.advance(doc);
              break;
            } else {
              doc2 = cs.posEnum.nextDoc();
            }
          }
        }
        if (doc2 > doc) {
          break;
        }
        i++;
      }

      if (i == chunkStates.length) {
        // this doc has all the terms -- now test whether
        // phrase occurs
        docID = doc;

        freq = phraseFreq();
        if (freq != 0) {
          return docID;
        }
      }
    }
  }

  @Override
  public int advance(int target) throws IOException {

    // first term
    int doc = chunkStates[0].posEnum.advance(target);
    if (doc == DocIdSetIterator.NO_MORE_DOCS) {
      docID = DocIdSetIterator.NO_MORE_DOCS;
      return doc;
    }

    while(true) {
      
      // not-first terms
      int i = 1;
      while(i < chunkStates.length) {
        int doc2 = chunkStates[i].posEnum.docID();
        if (doc2 < doc) {
          doc2 = chunkStates[i].posEnum.advance(doc);
        }
        if (doc2 > doc) {
          break;
        }
        i++;
      }

      if (i == chunkStates.length) {
        // this doc has all the terms -- now test whether
        // phrase occurs
        docID = doc;
        freq = phraseFreq();
        if (freq != 0) {
          return docID;
        }
      }

      doc = chunkStates[0].posEnum.nextDoc();
      if (doc == DocIdSetIterator.NO_MORE_DOCS) {
        docID = doc;
        return doc;
      }
    }
  }

  @Override
  public String toString() {
    return "ExactPhraseScorer(" + weight + ")";
  }

  @Override
  public float freq() {
    return freq;
  }

  @Override
  public int docID() {
    return docID;
  }

  @Override
  public float score() throws IOException {
    return docScorer.score(docID, freq);
  }

  private int phraseFreq() throws IOException {

    freq = 0;

    // init chunks
    for(int i=0;i<chunkStates.length;i++) {
      final ChunkState cs = chunkStates[i];
      cs.posLimit = cs.posEnum.freq();
      cs.pos = cs.offset + cs.posEnum.nextPosition();
      cs.posUpto = 1;
      cs.lastPos = -1;
    }

    int chunkStart = 0;
    int chunkEnd = CHUNK;

    // process chunk by chunk
    boolean end = false;

    // TODO: we could fold in chunkStart into offset and
    // save one subtract per pos incr

    while(!end) {

      gen++;

      if (gen == 0) {
        // wraparound
        Arrays.fill(gens, 0);
        gen++;
      }

      // first term
      {
        final ChunkState cs = chunkStates[0];
        while(cs.pos < chunkEnd) {
          if (cs.pos > cs.lastPos) {
            cs.lastPos = cs.pos;
            final int posIndex = cs.pos - chunkStart;
            counts[posIndex] = 1;
            assert gens[posIndex] != gen;
            gens[posIndex] = gen;
          }

          if (cs.posUpto == cs.posLimit) {
            end = true;
            break;
          }
          cs.posUpto++;
          cs.pos = cs.offset + cs.posEnum.nextPosition();
        }
      }

      // middle terms
      boolean any = true;
      for(int t=1;t<endMinus1;t++) {
        final ChunkState cs = chunkStates[t];
        any = false;
        while(cs.pos < chunkEnd) {
          if (cs.pos > cs.lastPos) {
            cs.lastPos = cs.pos;
            final int posIndex = cs.pos - chunkStart;
            if (posIndex >= 0 && gens[posIndex] == gen && counts[posIndex] == t) {
              // viable
              counts[posIndex]++;
              any = true;
            }
          }

          if (cs.posUpto == cs.posLimit) {
            end = true;
            break;
          }
          cs.posUpto++;
          cs.pos = cs.offset + cs.posEnum.nextPosition();
        }

        if (!any) {
          break;
        }
      }

      if (!any) {
        // petered out for this chunk
        chunkStart += CHUNK;
        chunkEnd += CHUNK;
        continue;
      }

      // last term

      {
        final ChunkState cs = chunkStates[endMinus1];
        while(cs.pos < chunkEnd) {
          if (cs.pos > cs.lastPos) {
            cs.lastPos = cs.pos;
            final int posIndex = cs.pos - chunkStart;
            if (posIndex >= 0 && gens[posIndex] == gen && counts[posIndex] == endMinus1) {
              freq++;
            }
          }

          if (cs.posUpto == cs.posLimit) {
            end = true;
            break;
          }
          cs.posUpto++;
          cs.pos = cs.offset + cs.posEnum.nextPosition();
        }
      }

      chunkStart += CHUNK;
      chunkEnd += CHUNK;
    }

    return freq;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/467.java