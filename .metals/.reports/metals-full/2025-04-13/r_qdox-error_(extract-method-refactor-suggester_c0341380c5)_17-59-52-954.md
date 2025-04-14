error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/478.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/478.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/478.java
text:
```scala
v@@oid finish() {

package org.apache.lucene.index;

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

import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.codecs.TermVectorsWriter;
import org.apache.lucene.util.ByteBlockPool;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.RamUsageEstimator;

final class TermVectorsConsumerPerField extends TermsHashConsumerPerField {

  final TermsHashPerField termsHashPerField;
  final TermVectorsConsumer termsWriter;
  final FieldInfo fieldInfo;
  final DocumentsWriterPerThread.DocState docState;
  final FieldInvertState fieldState;

  boolean doVectors;
  boolean doVectorPositions;
  boolean doVectorOffsets;

  int maxNumPostings;
  OffsetAttribute offsetAttribute;

  public TermVectorsConsumerPerField(TermsHashPerField termsHashPerField, TermVectorsConsumer termsWriter, FieldInfo fieldInfo) {
    this.termsHashPerField = termsHashPerField;
    this.termsWriter = termsWriter;
    this.fieldInfo = fieldInfo;
    docState = termsHashPerField.docState;
    fieldState = termsHashPerField.fieldState;
  }

  @Override
  int getStreamCount() {
    return 2;
  }

  @Override
  boolean start(IndexableField[] fields, int count) {
    doVectors = false;
    doVectorPositions = false;
    doVectorOffsets = false;

    for(int i=0;i<count;i++) {
      IndexableField field = fields[i];
      if (field.fieldType().indexed() && field.fieldType().storeTermVectors()) {
        doVectors = true;
        doVectorPositions |= field.fieldType().storeTermVectorPositions();
        doVectorOffsets |= field.fieldType().storeTermVectorOffsets();
      }
    }

    if (doVectors) {
      termsWriter.hasVectors = true;
      if (termsHashPerField.bytesHash.size() != 0) {
        // Only necessary if previous doc hit a
        // non-aborting exception while writing vectors in
        // this field:
        termsHashPerField.reset();
      }
    }

    // TODO: only if needed for performance
    //perThread.postingsCount = 0;

    return doVectors;
  }

  public void abort() {}

  /** Called once per field per document if term vectors
   *  are enabled, to write the vectors to
   *  RAMOutputStream, which is then quickly flushed to
   *  the real term vectors files in the Directory. */  @Override
  void finish() throws IOException {
    if (!doVectors || termsHashPerField.bytesHash.size() == 0) {
      return;
    }

    termsWriter.addFieldToFlush(this);
  }

  void finishDocument() throws IOException {
    assert docState.testPoint("TermVectorsTermsWriterPerField.finish start");

    final int numPostings = termsHashPerField.bytesHash.size();

    final BytesRef flushTerm = termsWriter.flushTerm;

    assert numPostings >= 0;

    if (numPostings > maxNumPostings)
      maxNumPostings = numPostings;

    // This is called once, after inverting all occurrences
    // of a given field in the doc.  At this point we flush
    // our hash into the DocWriter.

    assert termsWriter.vectorFieldsInOrder(fieldInfo);

    TermVectorsPostingsArray postings = (TermVectorsPostingsArray) termsHashPerField.postingsArray;
    final TermVectorsWriter tv = termsWriter.writer;

    final int[] termIDs = termsHashPerField.sortPostings(tv.getComparator());

    tv.startField(fieldInfo, numPostings, doVectorPositions, doVectorOffsets);
    
    final ByteSliceReader posReader = doVectorPositions ? termsWriter.vectorSliceReaderPos : null;
    final ByteSliceReader offReader = doVectorOffsets ? termsWriter.vectorSliceReaderOff : null;
    
    final ByteBlockPool termBytePool = termsHashPerField.termBytePool;

    for(int j=0;j<numPostings;j++) {
      final int termID = termIDs[j];
      final int freq = postings.freqs[termID];

      // Get BytesRef
      termBytePool.setBytesRef(flushTerm, postings.textStarts[termID]);
      tv.startTerm(flushTerm, freq);
      
      if (doVectorPositions || doVectorOffsets) {
        if (posReader != null) {
          termsHashPerField.initReader(posReader, termID, 0);
        }
        if (offReader != null) {
          termsHashPerField.initReader(offReader, termID, 1);
        }
        tv.addProx(freq, posReader, offReader);
      }
    }

    termsHashPerField.reset();

    // commit the termVectors once successful - FI will otherwise reset them
    fieldInfo.setStoreTermVectors();
  }

  void shrinkHash() {
    termsHashPerField.shrinkHash(maxNumPostings);
    maxNumPostings = 0;
  }

  @Override
  void start(IndexableField f) {
    if (doVectorOffsets) {
      offsetAttribute = fieldState.attributeSource.addAttribute(OffsetAttribute.class);
    } else {
      offsetAttribute = null;
    }
  }

  @Override
  void newTerm(final int termID) {
    assert docState.testPoint("TermVectorsTermsWriterPerField.newTerm start");
    TermVectorsPostingsArray postings = (TermVectorsPostingsArray) termsHashPerField.postingsArray;

    postings.freqs[termID] = 1;

    if (doVectorOffsets) {
      int startOffset = fieldState.offset + offsetAttribute.startOffset();
      int endOffset = fieldState.offset + offsetAttribute.endOffset();

      termsHashPerField.writeVInt(1, startOffset);
      termsHashPerField.writeVInt(1, endOffset - startOffset);
      postings.lastOffsets[termID] = endOffset;
    }

    if (doVectorPositions) {
      termsHashPerField.writeVInt(0, fieldState.position);
      postings.lastPositions[termID] = fieldState.position;
    }
  }

  @Override
  void addTerm(final int termID) {

    assert docState.testPoint("TermVectorsTermsWriterPerField.addTerm start");

    TermVectorsPostingsArray postings = (TermVectorsPostingsArray) termsHashPerField.postingsArray;

    postings.freqs[termID]++;

    if (doVectorOffsets) {
      int startOffset = fieldState.offset + offsetAttribute.startOffset();
      int endOffset = fieldState.offset + offsetAttribute.endOffset();

      termsHashPerField.writeVInt(1, startOffset - postings.lastOffsets[termID]);
      termsHashPerField.writeVInt(1, endOffset - startOffset);
      postings.lastOffsets[termID] = endOffset;
    }

    if (doVectorPositions) {
      termsHashPerField.writeVInt(0, fieldState.position - postings.lastPositions[termID]);
      postings.lastPositions[termID] = fieldState.position;
    }
  }

  @Override
  void skippingLongTerm() {}

  @Override
  ParallelPostingsArray createPostingsArray(int size) {
    return new TermVectorsPostingsArray(size);
  }

  static final class TermVectorsPostingsArray extends ParallelPostingsArray {
    public TermVectorsPostingsArray(int size) {
      super(size);
      freqs = new int[size];
      lastOffsets = new int[size];
      lastPositions = new int[size];
    }

    int[] freqs;                                       // How many times this term occurred in the current doc
    int[] lastOffsets;                                 // Last offset we saw
    int[] lastPositions;                               // Last position where this term occurred

    @Override
    ParallelPostingsArray newInstance(int size) {
      return new TermVectorsPostingsArray(size);
    }

    @Override
    void copyTo(ParallelPostingsArray toArray, int numToCopy) {
      assert toArray instanceof TermVectorsPostingsArray;
      TermVectorsPostingsArray to = (TermVectorsPostingsArray) toArray;

      super.copyTo(toArray, numToCopy);

      System.arraycopy(freqs, 0, to.freqs, 0, size);
      System.arraycopy(lastOffsets, 0, to.lastOffsets, 0, size);
      System.arraycopy(lastPositions, 0, to.lastPositions, 0, size);
    }

    @Override
    int bytesPerPosting() {
      return super.bytesPerPosting() + 3 * RamUsageEstimator.NUM_BYTES_INT;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/478.java