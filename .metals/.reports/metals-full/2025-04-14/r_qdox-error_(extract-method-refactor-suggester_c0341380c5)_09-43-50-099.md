error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2179.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2179.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2179.java
text:
```scala
p@@osition += code >>> 1;

package org.apache.lucene.index;

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
import java.util.Comparator;
import java.util.Map;

import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.codecs.FieldsConsumer;
import org.apache.lucene.codecs.PostingsConsumer;
import org.apache.lucene.codecs.TermStats;
import org.apache.lucene.codecs.TermsConsumer;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.FixedBitSet;
import org.apache.lucene.util.RamUsageEstimator;

// TODO: break into separate freq and prox writers as
// codecs; make separate container (tii/tis/skip/*) that can
// be configured as any number of files 1..N
final class FreqProxTermsWriterPerField extends TermsHashConsumerPerField implements Comparable<FreqProxTermsWriterPerField> {

  final FreqProxTermsWriter parent;
  final TermsHashPerField termsHashPerField;
  final FieldInfo fieldInfo;
  final DocumentsWriterPerThread.DocState docState;
  final FieldInvertState fieldState;
  IndexOptions indexOptions;
  private boolean writeFreq;
  private boolean writeProx;
  private boolean writeOffsets;
  PayloadAttribute payloadAttribute;
  OffsetAttribute offsetAttribute;

  public FreqProxTermsWriterPerField(TermsHashPerField termsHashPerField, FreqProxTermsWriter parent, FieldInfo fieldInfo) {
    this.termsHashPerField = termsHashPerField;
    this.parent = parent;
    this.fieldInfo = fieldInfo;
    docState = termsHashPerField.docState;
    fieldState = termsHashPerField.fieldState;
    setIndexOptions(fieldInfo.indexOptions);
  }

  @Override
  int getStreamCount() {
    if (!writeProx) {
      return 1;
    } else {
      return 2;
    }
  }

  @Override
  void finish() {}

  boolean hasPayloads;

  @Override
  void skippingLongTerm() throws IOException {}

  public int compareTo(FreqProxTermsWriterPerField other) {
    return fieldInfo.name.compareTo(other.fieldInfo.name);
  }

  // Called after flush
  void reset() {
    // Record, up front, whether our in-RAM format will be
    // with or without term freqs:
    setIndexOptions(fieldInfo.indexOptions);
    payloadAttribute = null;
  }

  private void setIndexOptions(IndexOptions indexOptions) {
    this.indexOptions = indexOptions;
    writeFreq = indexOptions.compareTo(IndexOptions.DOCS_AND_FREQS) >= 0;
    writeProx = indexOptions.compareTo(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS) >= 0;
    writeOffsets = indexOptions.compareTo(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS) >= 0;
  }

  @Override
  boolean start(IndexableField[] fields, int count) {
    for(int i=0;i<count;i++) {
      if (fields[i].fieldType().indexed()) {
        return true;
      }
    }
    return false;
  }

  @Override
  void start(IndexableField f) {
    if (fieldState.attributeSource.hasAttribute(PayloadAttribute.class)) {
      payloadAttribute = fieldState.attributeSource.getAttribute(PayloadAttribute.class);
    } else {
      payloadAttribute = null;
    }
    if (writeOffsets) {
      offsetAttribute = fieldState.attributeSource.addAttribute(OffsetAttribute.class);
    } else {
      offsetAttribute = null;
    }
  }

  void writeProx(final int termID, int proxCode) {
    //System.out.println("writeProx termID=" + termID + " proxCode=" + proxCode);
    assert writeProx;
    final Payload payload;
    if (payloadAttribute == null) {
      payload = null;
    } else {
      payload = payloadAttribute.getPayload();
    }

    if (payload != null && payload.length > 0) {
      termsHashPerField.writeVInt(1, (proxCode<<1)|1);
      termsHashPerField.writeVInt(1, payload.length);
      termsHashPerField.writeBytes(1, payload.data, payload.offset, payload.length);
      hasPayloads = true;
    } else {
      termsHashPerField.writeVInt(1, proxCode<<1);
    }

    FreqProxPostingsArray postings = (FreqProxPostingsArray) termsHashPerField.postingsArray;
    postings.lastPositions[termID] = fieldState.position;
  }

  void writeOffsets(final int termID, int prevOffset) {
    assert writeOffsets;
    final int startOffset = offsetAttribute.startOffset();
    final int endOffset = offsetAttribute.endOffset();
    //System.out.println("writeOffsets termID=" + termID + " prevOffset=" + prevOffset + " startOff=" + startOffset + " endOff=" + endOffset);
    termsHashPerField.writeVInt(1, startOffset - prevOffset);
    termsHashPerField.writeVInt(1, endOffset - startOffset);

    FreqProxPostingsArray postings = (FreqProxPostingsArray) termsHashPerField.postingsArray;
    postings.lastOffsets[termID] = startOffset;
  }

  @Override
  void newTerm(final int termID) {
    // First time we're seeing this term since the last
    // flush
    assert docState.testPoint("FreqProxTermsWriterPerField.newTerm start");

    FreqProxPostingsArray postings = (FreqProxPostingsArray) termsHashPerField.postingsArray;
    postings.lastDocIDs[termID] = docState.docID;
    if (!writeFreq) {
      postings.lastDocCodes[termID] = docState.docID;
    } else {
      postings.lastDocCodes[termID] = docState.docID << 1;
      postings.docFreqs[termID] = 1;
      if (writeProx) {
        writeProx(termID, fieldState.position);
        if (writeOffsets) {
          writeOffsets(termID, fieldState.offset);
        }
      } else {
        assert !writeOffsets;
      }
    }
    fieldState.maxTermFrequency = Math.max(1, fieldState.maxTermFrequency);
    fieldState.uniqueTermCount++;
  }

  @Override
  void addTerm(final int termID) {

    assert docState.testPoint("FreqProxTermsWriterPerField.addTerm start");

    FreqProxPostingsArray postings = (FreqProxPostingsArray) termsHashPerField.postingsArray;

    assert !writeFreq || postings.docFreqs[termID] > 0;

    if (!writeFreq) {
      assert postings.docFreqs == null;
      if (docState.docID != postings.lastDocIDs[termID]) {
        assert docState.docID > postings.lastDocIDs[termID];
        termsHashPerField.writeVInt(0, postings.lastDocCodes[termID]);
        postings.lastDocCodes[termID] = docState.docID - postings.lastDocIDs[termID];
        postings.lastDocIDs[termID] = docState.docID;
        fieldState.uniqueTermCount++;
      }
    } else if (docState.docID != postings.lastDocIDs[termID]) {
      assert docState.docID > postings.lastDocIDs[termID]:"id: "+docState.docID + " postings ID: "+ postings.lastDocIDs[termID] + " termID: "+termID;
      // Term not yet seen in the current doc but previously
      // seen in other doc(s) since the last flush

      // Now that we know doc freq for previous doc,
      // write it & lastDocCode
      if (1 == postings.docFreqs[termID]) {
        termsHashPerField.writeVInt(0, postings.lastDocCodes[termID]|1);
      } else {
        termsHashPerField.writeVInt(0, postings.lastDocCodes[termID]);
        termsHashPerField.writeVInt(0, postings.docFreqs[termID]);
      }
      postings.docFreqs[termID] = 1;
      fieldState.maxTermFrequency = Math.max(1, fieldState.maxTermFrequency);
      postings.lastDocCodes[termID] = (docState.docID - postings.lastDocIDs[termID]) << 1;
      postings.lastDocIDs[termID] = docState.docID;
      if (writeProx) {
        writeProx(termID, fieldState.position);
        if (writeOffsets) {
          writeOffsets(termID, fieldState.offset);
        }
      } else {
        assert !writeOffsets;
      }
      fieldState.uniqueTermCount++;
    } else {
      fieldState.maxTermFrequency = Math.max(fieldState.maxTermFrequency, ++postings.docFreqs[termID]);
      if (writeProx) {
        writeProx(termID, fieldState.position-postings.lastPositions[termID]);
      }
      if (writeOffsets) {
        writeOffsets(termID, postings.lastOffsets[termID]);
      }
    }
  }

  @Override
  ParallelPostingsArray createPostingsArray(int size) {
    return new FreqProxPostingsArray(size, writeFreq, writeProx, writeOffsets);
  }

  static final class FreqProxPostingsArray extends ParallelPostingsArray {
    public FreqProxPostingsArray(int size, boolean writeFreqs, boolean writeProx, boolean writeOffsets) {
      super(size);
      if (writeFreqs) {
        docFreqs = new int[size];
      }
      lastDocIDs = new int[size];
      lastDocCodes = new int[size];
      if (writeProx) {
        lastPositions = new int[size];
        if (writeOffsets) {
          lastOffsets = new int[size];
        }
      } else {
        assert !writeOffsets;
      }
      //System.out.println("PA init freqs=" + writeFreqs + " pos=" + writeProx + " offs=" + writeOffsets);
    }

    int docFreqs[];                                    // # times this term occurs in the current doc
    int lastDocIDs[];                                  // Last docID where this term occurred
    int lastDocCodes[];                                // Code for prior doc
    int lastPositions[];                               // Last position where this term occurred
    int lastOffsets[];                                 // Last endOffset where this term occurred

    @Override
    ParallelPostingsArray newInstance(int size) {
      return new FreqProxPostingsArray(size, docFreqs != null, lastPositions != null, lastOffsets != null);
    }

    @Override
    void copyTo(ParallelPostingsArray toArray, int numToCopy) {
      assert toArray instanceof FreqProxPostingsArray;
      FreqProxPostingsArray to = (FreqProxPostingsArray) toArray;

      super.copyTo(toArray, numToCopy);

      System.arraycopy(lastDocIDs, 0, to.lastDocIDs, 0, numToCopy);
      System.arraycopy(lastDocCodes, 0, to.lastDocCodes, 0, numToCopy);
      if (lastPositions != null) {
        assert to.lastPositions != null;
        System.arraycopy(lastPositions, 0, to.lastPositions, 0, numToCopy);
      }
      if (lastOffsets != null) {
        assert to.lastOffsets != null;
        System.arraycopy(lastOffsets, 0, to.lastOffsets, 0, numToCopy);
      }
      if (docFreqs != null) {
        assert to.docFreqs != null;
        System.arraycopy(docFreqs, 0, to.docFreqs, 0, numToCopy);
      }
    }

    @Override
    int bytesPerPosting() {
      int bytes = ParallelPostingsArray.BYTES_PER_POSTING + 2 * RamUsageEstimator.NUM_BYTES_INT;
      if (lastPositions != null) {
        bytes += RamUsageEstimator.NUM_BYTES_INT;
      }
      if (lastOffsets != null) {
        bytes += RamUsageEstimator.NUM_BYTES_INT;
      }
      if (docFreqs != null) {
        bytes += RamUsageEstimator.NUM_BYTES_INT;
      }

      return bytes;
    }
  }

  public void abort() {}

  BytesRef payload;

  /* Walk through all unique text tokens (Posting
   * instances) found in this field and serialize them
   * into a single RAM segment. */
  void flush(String fieldName, FieldsConsumer consumer,  final SegmentWriteState state)
    throws CorruptIndexException, IOException {

    final TermsConsumer termsConsumer = consumer.addField(fieldInfo);
    final Comparator<BytesRef> termComp = termsConsumer.getComparator();

    // CONFUSING: this.indexOptions holds the index options
    // that were current when we first saw this field.  But
    // it's possible this has changed, eg when other
    // documents are indexed that cause a "downgrade" of the
    // IndexOptions.  So we must decode the in-RAM buffer
    // according to this.indexOptions, but then write the
    // new segment to the directory according to
    // currentFieldIndexOptions:
    final IndexOptions currentFieldIndexOptions = fieldInfo.indexOptions;

    final boolean writeTermFreq = currentFieldIndexOptions.compareTo(IndexOptions.DOCS_AND_FREQS) >= 0;
    final boolean writePositions = currentFieldIndexOptions.compareTo(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS) >= 0;
    final boolean writeOffsets = currentFieldIndexOptions.compareTo(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS) >= 0;

    final boolean readTermFreq = indexOptions.compareTo(IndexOptions.DOCS_AND_FREQS) >= 0;
    final boolean readPositions = indexOptions.compareTo(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS) >= 0;
    final boolean readOffsets = indexOptions.compareTo(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS) >= 0;

    //System.out.println("flush readTF=" + readTermFreq + " readPos=" + readPositions + " readOffs=" + readOffsets);

    // Make sure FieldInfo.update is working correctly!:
    assert !writeTermFreq || readTermFreq;
    assert !writePositions || readPositions;
    assert !writeOffsets || readOffsets;

    assert !writeOffsets || writePositions;

    final Map<Term,Integer> segDeletes;
    if (state.segDeletes != null && state.segDeletes.terms.size() > 0) {
      segDeletes = state.segDeletes.terms;
    } else {
      segDeletes = null;
    }

    final int[] termIDs = termsHashPerField.sortPostings(termComp);
    final int numTerms = termsHashPerField.bytesHash.size();
    final BytesRef text = new BytesRef();
    final FreqProxPostingsArray postings = (FreqProxPostingsArray) termsHashPerField.postingsArray;
    final ByteSliceReader freq = new ByteSliceReader();
    final ByteSliceReader prox = new ByteSliceReader();

    FixedBitSet visitedDocs = new FixedBitSet(state.numDocs);
    long sumTotalTermFreq = 0;
    long sumDocFreq = 0;

    for (int i = 0; i < numTerms; i++) {
      final int termID = termIDs[i];
      //System.out.println("term=" + termID);
      // Get BytesRef
      final int textStart = postings.textStarts[termID];
      termsHashPerField.bytePool.setBytesRef(text, textStart);

      termsHashPerField.initReader(freq, termID, 0);
      if (readPositions || readOffsets) {
        termsHashPerField.initReader(prox, termID, 1);
      }

      // TODO: really TermsHashPerField should take over most
      // of this loop, including merge sort of terms from
      // multiple threads and interacting with the
      // TermsConsumer, only calling out to us (passing us the
      // DocsConsumer) to handle delivery of docs/positions

      final PostingsConsumer postingsConsumer = termsConsumer.startTerm(text);

      final int delDocLimit;
      if (segDeletes != null) {
        final Integer docIDUpto = segDeletes.get(new Term(fieldName, text));
        if (docIDUpto != null) {
          delDocLimit = docIDUpto;
        } else {
          delDocLimit = 0;
        }
      } else {
        delDocLimit = 0;
      }

      // Now termStates has numToMerge FieldMergeStates
      // which all share the same term.  Now we must
      // interleave the docID streams.
      int numDocs = 0;
      long totTF = 0;
      int docID = 0;

      while(true) {
        //System.out.println("  cycle");
        final int termDocFreq;
        if (freq.eof()) {
          if (postings.lastDocCodes[termID] != -1) {
            // Return last doc
            docID = postings.lastDocIDs[termID];
            if (readTermFreq) {
              termDocFreq = postings.docFreqs[termID];
            } else {
              termDocFreq = 0;
            }
            postings.lastDocCodes[termID] = -1;
          } else {
            // EOF
            break;
          }
        } else {
          final int code = freq.readVInt();
          if (!readTermFreq) {
            docID += code;
            termDocFreq = 0;
          } else {
            docID += code >>> 1;
            if ((code & 1) != 0) {
              termDocFreq = 1;
            } else {
              termDocFreq = freq.readVInt();
            }
          }

          assert docID != postings.lastDocIDs[termID];
        }

        numDocs++;
        assert docID < state.numDocs: "doc=" + docID + " maxDoc=" + state.numDocs;

        // NOTE: we could check here if the docID was
        // deleted, and skip it.  However, this is somewhat
        // dangerous because it can yield non-deterministic
        // behavior since we may see the docID before we see
        // the term that caused it to be deleted.  This
        // would mean some (but not all) of its postings may
        // make it into the index, which'd alter the docFreq
        // for those terms.  We could fix this by doing two
        // passes, ie first sweep marks all del docs, and
        // 2nd sweep does the real flush, but I suspect
        // that'd add too much time to flush.
        visitedDocs.set(docID);
        postingsConsumer.startDoc(docID, termDocFreq);
        if (docID < delDocLimit) {
          // Mark it deleted.  TODO: we could also skip
          // writing its postings; this would be
          // deterministic (just for this Term's docs).
          
          // TODO: can we do this reach-around in a cleaner way????
          if (state.liveDocs == null) {
            state.liveDocs = docState.docWriter.codec.liveDocsFormat().newLiveDocs(state.numDocs);
          }
          if (state.liveDocs.get(docID)) {
            state.delCountOnFlush++;
            state.liveDocs.clear(docID);
          }
        }

        totTF += termDocFreq;
        
        // Carefully copy over the prox + payload info,
        // changing the format to match Lucene's segment
        // format.

        if (readPositions || readOffsets) {
          // we did record positions (& maybe payload) and/or offsets
          int position = 0;
          int offset = 0;
          for(int j=0;j<termDocFreq;j++) {
            final BytesRef thisPayload;

            if (readPositions) {
              final int code = prox.readVInt();
              position += code >> 1;

              if ((code & 1) != 0) {

                // This position has a payload
                final int payloadLength = prox.readVInt();

                if (payload == null) {
                  payload = new BytesRef();
                  payload.bytes = new byte[payloadLength];
                } else if (payload.bytes.length < payloadLength) {
                  payload.grow(payloadLength);
                }

                prox.readBytes(payload.bytes, 0, payloadLength);
                payload.length = payloadLength;
                thisPayload = payload;

              } else {
                thisPayload = null;
              }

              if (readOffsets) {
                final int startOffset = offset + prox.readVInt();
                final int endOffset = startOffset + prox.readVInt();
                offset = startOffset;
                if (writePositions) {
                  postingsConsumer.addPosition(position, thisPayload, startOffset, endOffset);
                }
              } else if (writePositions) {
                postingsConsumer.addPosition(position, thisPayload, -1, -1);
              }
            }
          }
        }
        postingsConsumer.finishDoc();
      }
      termsConsumer.finishTerm(text, new TermStats(numDocs, totTF));
      sumTotalTermFreq += totTF;
      sumDocFreq += numDocs;
    }

    termsConsumer.finish(sumTotalTermFreq, sumDocFreq, visitedDocs.cardinality());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2179.java