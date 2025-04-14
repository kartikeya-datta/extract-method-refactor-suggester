error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/351.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/351.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/351.java
text:
```scala
f@@inish(indexOptions == IndexOptions.DOCS_ONLY ? -1 : sumTotalTermFreq, sumDocFreq, visitedDocs.cardinality());

package org.apache.lucene.codecs;

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
import java.util.Comparator;

import org.apache.lucene.index.FieldInfo; // javadocs
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.MergeState;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.index.MultiDocsEnum;
import org.apache.lucene.index.MultiDocsAndPositionsEnum;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.FixedBitSet;

/**
 * Abstract API that consumes terms for an individual field.
 * <p>
 * The lifecycle is:
 * <ol>
 *   <li>TermsConsumer is returned for each field 
 *       by {@link FieldsConsumer#addField(FieldInfo)}.
 *   <li>TermsConsumer returns a {@link PostingsConsumer} for
 *       each term in {@link #startTerm(BytesRef)}.
 *   <li>When the producer (e.g. IndexWriter)
 *       is done adding documents for the term, it calls 
 *       {@link #finishTerm(BytesRef, TermStats)}, passing in
 *       the accumulated term statistics.
 *   <li>Producer calls {@link #finish(long, long, int)} with
 *       the accumulated collection statistics when it is finished
 *       adding terms to the field.
 * </ol>
 * 
 * @lucene.experimental
 */
public abstract class TermsConsumer {

  /** Starts a new term in this field; this may be called
   *  with no corresponding call to finish if the term had
   *  no docs. */
  public abstract PostingsConsumer startTerm(BytesRef text) throws IOException;

  /** Finishes the current term; numDocs must be > 0. */
  public abstract void finishTerm(BytesRef text, TermStats stats) throws IOException;

  /** Called when we are done adding terms to this field */
  public abstract void finish(long sumTotalTermFreq, long sumDocFreq, int docCount) throws IOException;

  /** Return the BytesRef Comparator used to sort terms
   *  before feeding to this API. */
  public abstract Comparator<BytesRef> getComparator() throws IOException;

  private MappingMultiDocsEnum docsEnum;
  private MappingMultiDocsEnum docsAndFreqsEnum;
  private MappingMultiDocsAndPositionsEnum postingsEnum;

  /** Default merge impl */
  public void merge(MergeState mergeState, TermsEnum termsEnum) throws IOException {

    BytesRef term;
    assert termsEnum != null;
    long sumTotalTermFreq = 0;
    long sumDocFreq = 0;
    long sumDFsinceLastAbortCheck = 0;
    FixedBitSet visitedDocs = new FixedBitSet(mergeState.segmentInfo.getDocCount());

    IndexOptions indexOptions = mergeState.fieldInfo.getIndexOptions();
    if (indexOptions == IndexOptions.DOCS_ONLY) {
      if (docsEnum == null) {
        docsEnum = new MappingMultiDocsEnum();
      }
      docsEnum.setMergeState(mergeState);

      MultiDocsEnum docsEnumIn = null;

      while((term = termsEnum.next()) != null) {
        // We can pass null for liveDocs, because the
        // mapping enum will skip the non-live docs:
        docsEnumIn = (MultiDocsEnum) termsEnum.docs(null, docsEnumIn, false);
        if (docsEnumIn != null) {
          docsEnum.reset(docsEnumIn);
          final PostingsConsumer postingsConsumer = startTerm(term);
          final TermStats stats = postingsConsumer.merge(mergeState, docsEnum, visitedDocs);
          if (stats.docFreq > 0) {
            finishTerm(term, stats);
            sumTotalTermFreq += stats.docFreq;
            sumDFsinceLastAbortCheck += stats.docFreq;
            sumDocFreq += stats.docFreq;
            if (sumDFsinceLastAbortCheck > 60000) {
              mergeState.checkAbort.work(sumDFsinceLastAbortCheck/5.0);
              sumDFsinceLastAbortCheck = 0;
            }
          }
        }
      }
    } else if (indexOptions == IndexOptions.DOCS_AND_FREQS) {
      if (docsAndFreqsEnum == null) {
        docsAndFreqsEnum = new MappingMultiDocsEnum();
      }
      docsAndFreqsEnum.setMergeState(mergeState);

      MultiDocsEnum docsAndFreqsEnumIn = null;

      while((term = termsEnum.next()) != null) {
        // We can pass null for liveDocs, because the
        // mapping enum will skip the non-live docs:
        docsAndFreqsEnumIn = (MultiDocsEnum) termsEnum.docs(null, docsAndFreqsEnumIn, true);
        assert docsAndFreqsEnumIn != null;
        docsAndFreqsEnum.reset(docsAndFreqsEnumIn);
        final PostingsConsumer postingsConsumer = startTerm(term);
        final TermStats stats = postingsConsumer.merge(mergeState, docsAndFreqsEnum, visitedDocs);
        if (stats.docFreq > 0) {
          finishTerm(term, stats);
          sumTotalTermFreq += stats.totalTermFreq;
          sumDFsinceLastAbortCheck += stats.docFreq;
          sumDocFreq += stats.docFreq;
          if (sumDFsinceLastAbortCheck > 60000) {
            mergeState.checkAbort.work(sumDFsinceLastAbortCheck/5.0);
            sumDFsinceLastAbortCheck = 0;
          }
        }
      }
    } else if (indexOptions == IndexOptions.DOCS_AND_FREQS_AND_POSITIONS) {
      if (postingsEnum == null) {
        postingsEnum = new MappingMultiDocsAndPositionsEnum();
      }
      postingsEnum.setMergeState(mergeState);
      MultiDocsAndPositionsEnum postingsEnumIn = null;
      while((term = termsEnum.next()) != null) {
        // We can pass null for liveDocs, because the
        // mapping enum will skip the non-live docs:
        postingsEnumIn = (MultiDocsAndPositionsEnum) termsEnum.docsAndPositions(null, postingsEnumIn, false);
        assert postingsEnumIn != null;
        postingsEnum.reset(postingsEnumIn);
        // set PayloadProcessor
        if (mergeState.payloadProcessorProvider != null) {
          for (int i = 0; i < mergeState.readers.size(); i++) {
            if (mergeState.readerPayloadProcessor[i] != null) {
              mergeState.currentPayloadProcessor[i] = mergeState.readerPayloadProcessor[i].getProcessor(mergeState.fieldInfo.name, term);
            }
          }
        }
        final PostingsConsumer postingsConsumer = startTerm(term);
        final TermStats stats = postingsConsumer.merge(mergeState, postingsEnum, visitedDocs);
        if (stats.docFreq > 0) {
          finishTerm(term, stats);
          sumTotalTermFreq += stats.totalTermFreq;
          sumDFsinceLastAbortCheck += stats.docFreq;
          sumDocFreq += stats.docFreq;
          if (sumDFsinceLastAbortCheck > 60000) {
            mergeState.checkAbort.work(sumDFsinceLastAbortCheck/5.0);
            sumDFsinceLastAbortCheck = 0;
          }
        }
      }
    } else {
      assert indexOptions == IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS;
      if (postingsEnum == null) {
        postingsEnum = new MappingMultiDocsAndPositionsEnum();
      }
      postingsEnum.setMergeState(mergeState);
      MultiDocsAndPositionsEnum postingsEnumIn = null;
      while((term = termsEnum.next()) != null) {
        // We can pass null for liveDocs, because the
        // mapping enum will skip the non-live docs:
        postingsEnumIn = (MultiDocsAndPositionsEnum) termsEnum.docsAndPositions(null, postingsEnumIn, true);
        assert postingsEnumIn != null;
        postingsEnum.reset(postingsEnumIn);
        // set PayloadProcessor
        if (mergeState.payloadProcessorProvider != null) {
          for (int i = 0; i < mergeState.readers.size(); i++) {
            if (mergeState.readerPayloadProcessor[i] != null) {
              mergeState.currentPayloadProcessor[i] = mergeState.readerPayloadProcessor[i].getProcessor(mergeState.fieldInfo.name, term);
            }
          }
        }
        final PostingsConsumer postingsConsumer = startTerm(term);
        final TermStats stats = postingsConsumer.merge(mergeState, postingsEnum, visitedDocs);
        if (stats.docFreq > 0) {
          finishTerm(term, stats);
          sumTotalTermFreq += stats.totalTermFreq;
          sumDFsinceLastAbortCheck += stats.docFreq;
          sumDocFreq += stats.docFreq;
          if (sumDFsinceLastAbortCheck > 60000) {
            mergeState.checkAbort.work(sumDFsinceLastAbortCheck/5.0);
            sumDFsinceLastAbortCheck = 0;
          }
        }
      }
    }
    finish(sumTotalTermFreq, sumDocFreq, visitedDocs.cardinality());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/351.java