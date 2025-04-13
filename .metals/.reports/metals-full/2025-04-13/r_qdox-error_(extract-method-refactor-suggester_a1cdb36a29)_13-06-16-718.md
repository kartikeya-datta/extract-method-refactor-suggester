error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/350.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/350.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/350.java
text:
```scala
r@@eturn new TermStats(df, indexOptions == IndexOptions.DOCS_ONLY ? -1 : totTF);

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

import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.MergeState;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.FixedBitSet;

/**
 * Abstract API that consumes postings for an individual term.
 * <p>
 * The lifecycle is:
 * <ol>
 *    <li>PostingsConsumer is returned for each term by
 *        {@link TermsConsumer#startTerm(BytesRef)}. 
 *    <li>{@link #startDoc(int, int)} is called for each
 *        document where the term occurs, specifying id 
 *        and term frequency for that document.
 *    <li>If positions are enabled for the field, then
 *        {@link #addPosition(int, BytesRef, int, int)}
 *        will be called for each occurrence in the 
 *        document.
 *    <li>{@link #finishDoc()} is called when the producer
 *        is done adding positions to the document.
 * </ol>
 * 
 * @lucene.experimental
 */
public abstract class PostingsConsumer {

  /** Adds a new doc in this term. */
  public abstract void startDoc(int docID, int freq) throws IOException;

  /** Add a new position & payload, and start/end offset.  A
   *  null payload means no payload; a non-null payload with
   *  zero length also means no payload.  Caller may reuse
   *  the {@link BytesRef} for the payload between calls
   *  (method must fully consume the payload). */
  public abstract void addPosition(int position, BytesRef payload, int startOffset, int endOffset) throws IOException;

  /** Called when we are done adding positions & payloads
   *  for each doc. */
  public abstract void finishDoc() throws IOException;

  /** Default merge impl: append documents, mapping around
   *  deletes */
  public TermStats merge(final MergeState mergeState, final DocsEnum postings, final FixedBitSet visitedDocs) throws IOException {

    int df = 0;
    long totTF = 0;

    IndexOptions indexOptions = mergeState.fieldInfo.getIndexOptions();
    if (indexOptions == IndexOptions.DOCS_ONLY) {
      while(true) {
        final int doc = postings.nextDoc();
        if (doc == DocIdSetIterator.NO_MORE_DOCS) {
          break;
        }
        visitedDocs.set(doc);
        this.startDoc(doc, 0);
        this.finishDoc();
        df++;
      }
      totTF = -1;
    } else if (indexOptions == IndexOptions.DOCS_AND_FREQS) {
      while(true) {
        final int doc = postings.nextDoc();
        if (doc == DocIdSetIterator.NO_MORE_DOCS) {
          break;
        }
        visitedDocs.set(doc);
        final int freq = postings.freq();
        this.startDoc(doc, freq);
        this.finishDoc();
        df++;
        totTF += freq;
      }
    } else if (indexOptions == IndexOptions.DOCS_AND_FREQS_AND_POSITIONS) {
      final DocsAndPositionsEnum postingsEnum = (DocsAndPositionsEnum) postings;
      while(true) {
        final int doc = postingsEnum.nextDoc();
        if (doc == DocIdSetIterator.NO_MORE_DOCS) {
          break;
        }
        visitedDocs.set(doc);
        final int freq = postingsEnum.freq();
        this.startDoc(doc, freq);
        totTF += freq;
        for(int i=0;i<freq;i++) {
          final int position = postingsEnum.nextPosition();
          final BytesRef payload;
          if (postingsEnum.hasPayload()) {
            payload = postingsEnum.getPayload();
          } else {
            payload = null;
          }
          this.addPosition(position, payload, -1, -1);
        }
        this.finishDoc();
        df++;
      }
    } else {
      assert indexOptions == IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS;
      final DocsAndPositionsEnum postingsEnum = (DocsAndPositionsEnum) postings;
      while(true) {
        final int doc = postingsEnum.nextDoc();
        if (doc == DocIdSetIterator.NO_MORE_DOCS) {
          break;
        }
        visitedDocs.set(doc);
        final int freq = postingsEnum.freq();
        this.startDoc(doc, freq);
        totTF += freq;
        for(int i=0;i<freq;i++) {
          final int position = postingsEnum.nextPosition();
          final BytesRef payload;
          if (postingsEnum.hasPayload()) {
            payload = postingsEnum.getPayload();
          } else {
            payload = null;
          }
          this.addPosition(position, payload, postingsEnum.startOffset(), postingsEnum.endOffset());
        }
        this.finishDoc();
        df++;
      }
    }
    return new TermStats(df, totTF);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/350.java