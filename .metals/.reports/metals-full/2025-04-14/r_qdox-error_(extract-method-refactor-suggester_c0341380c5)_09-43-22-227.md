error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/119.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/119.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/119.java
text:
```scala
f@@or (final AtomicReaderContext ctx : reader.leaves()) {

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.FieldInfosWriter;
import org.apache.lucene.codecs.FieldsConsumer;
import org.apache.lucene.codecs.PerDocConsumer;
import org.apache.lucene.codecs.StoredFieldsWriter;
import org.apache.lucene.codecs.TermVectorsWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.InfoStream;

/**
 * The SegmentMerger class combines two or more Segments, represented by an IndexReader ({@link #add},
 * into a single Segment.  After adding the appropriate readers, call the merge method to combine the
 * segments.
 *
 * @see #merge
 * @see #add
 */
final class SegmentMerger {
  private final Directory directory;
  private final int termIndexInterval;

  private final Codec codec;
  
  private final IOContext context;
  
  private final MergeState mergeState = new MergeState();
  private final FieldInfos.Builder fieldInfosBuilder;

  // note, just like in codec apis Directory 'dir' is NOT the same as segmentInfo.dir!!
  SegmentMerger(SegmentInfo segmentInfo, InfoStream infoStream, Directory dir, int termIndexInterval,
                MergeState.CheckAbort checkAbort, PayloadProcessorProvider payloadProcessorProvider,
                FieldInfos.FieldNumbers fieldNumbers, IOContext context) {
    mergeState.segmentInfo = segmentInfo;
    mergeState.infoStream = infoStream;
    mergeState.readers = new ArrayList<AtomicReader>();
    mergeState.checkAbort = checkAbort;
    mergeState.payloadProcessorProvider = payloadProcessorProvider;
    directory = dir;
    this.termIndexInterval = termIndexInterval;
    this.codec = segmentInfo.getCodec();
    this.context = context;
    this.fieldInfosBuilder = new FieldInfos.Builder(fieldNumbers);
  }

  /**
   * Add an IndexReader to the collection of readers that are to be merged
   * @param reader
   */
  final void add(IndexReader reader) {
    for (final AtomicReaderContext ctx : reader.getTopReaderContext().leaves()) {
      final AtomicReader r = ctx.reader();
      mergeState.readers.add(r);
    }
  }

  final void add(SegmentReader reader) {
    mergeState.readers.add(reader);
  }

  /**
   * Merges the readers specified by the {@link #add} method into the directory passed to the constructor
   * @return The number of documents that were merged
   * @throws CorruptIndexException if the index is corrupt
   * @throws IOException if there is a low-level IO error
   */
  final MergeState merge() throws IOException {
    // NOTE: it's important to add calls to
    // checkAbort.work(...) if you make any changes to this
    // method that will spend alot of time.  The frequency
    // of this check impacts how long
    // IndexWriter.close(false) takes to actually stop the
    // threads.
    
    mergeState.segmentInfo.setDocCount(setDocMaps());
    mergeDocValuesAndNormsFieldInfos();
    setMatchingSegmentReaders();
    int numMerged = mergeFields();
    assert numMerged == mergeState.segmentInfo.getDocCount();

    final SegmentWriteState segmentWriteState = new SegmentWriteState(mergeState.infoStream, directory, mergeState.segmentInfo,
                                                                      mergeState.fieldInfos, termIndexInterval, null, context);
    mergeTerms(segmentWriteState);
    mergePerDoc(segmentWriteState);
    
    if (mergeState.fieldInfos.hasNorms()) {
      mergeNorms(segmentWriteState);
    }

    if (mergeState.fieldInfos.hasVectors()) {
      numMerged = mergeVectors();
      assert numMerged == mergeState.segmentInfo.getDocCount();
    }
    
    // write the merged infos
    FieldInfosWriter fieldInfosWriter = codec.fieldInfosFormat().getFieldInfosWriter();
    fieldInfosWriter.write(directory, mergeState.segmentInfo.name, mergeState.fieldInfos, context);

    return mergeState;
  }

  private void setMatchingSegmentReaders() {
    // If the i'th reader is a SegmentReader and has
    // identical fieldName -> number mapping, then this
    // array will be non-null at position i:
    int numReaders = mergeState.readers.size();
    mergeState.matchingSegmentReaders = new SegmentReader[numReaders];

    // If this reader is a SegmentReader, and all of its
    // field name -> number mappings match the "merged"
    // FieldInfos, then we can do a bulk copy of the
    // stored fields:
    for (int i = 0; i < numReaders; i++) {
      AtomicReader reader = mergeState.readers.get(i);
      // TODO: we may be able to broaden this to
      // non-SegmentReaders, since FieldInfos is now
      // required?  But... this'd also require exposing
      // bulk-copy (TVs and stored fields) API in foreign
      // readers..
      if (reader instanceof SegmentReader) {
        SegmentReader segmentReader = (SegmentReader) reader;
        boolean same = true;
        FieldInfos segmentFieldInfos = segmentReader.getFieldInfos();
        for (FieldInfo fi : segmentFieldInfos) {
          FieldInfo other = mergeState.fieldInfos.fieldInfo(fi.number);
          if (other == null || !other.name.equals(fi.name)) {
            same = false;
            break;
          }
        }
        if (same) {
          mergeState.matchingSegmentReaders[i] = segmentReader;
          mergeState.matchedCount++;
        }
      }
    }

    if (mergeState.infoStream.isEnabled("SM")) {
      mergeState.infoStream.message("SM", "merge store matchedCount=" + mergeState.matchedCount + " vs " + mergeState.readers.size());
      if (mergeState.matchedCount != mergeState.readers.size()) {
        mergeState.infoStream.message("SM", "" + (mergeState.readers.size() - mergeState.matchedCount) + " non-bulk merges");
      }
    }
  }
  
  // returns an updated typepromoter (tracking type and size) given a previous one,
  // and a newly encountered docvalues
  private TypePromoter mergeDocValuesType(TypePromoter previous, DocValues docValues) {
    TypePromoter incoming = TypePromoter.create(docValues.getType(),  docValues.getValueSize());
    if (previous == null) {
      previous = TypePromoter.getIdentityPromoter();
    }
    return previous.promote(incoming);
  }

  // NOTE: this is actually merging all the fieldinfos
  public void mergeDocValuesAndNormsFieldInfos() throws IOException {
    // mapping from all docvalues fields found to their promoted types
    // this is because FieldInfos does not store the
    // valueSize
    Map<FieldInfo,TypePromoter> docValuesTypes = new HashMap<FieldInfo,TypePromoter>();
    Map<FieldInfo,TypePromoter> normValuesTypes = new HashMap<FieldInfo,TypePromoter>();

    for (AtomicReader reader : mergeState.readers) {
      FieldInfos readerFieldInfos = reader.getFieldInfos();
      for (FieldInfo fi : readerFieldInfos) {
        FieldInfo merged = fieldInfosBuilder.add(fi);
        // update the type promotion mapping for this reader
        if (fi.hasDocValues()) {
          TypePromoter previous = docValuesTypes.get(merged);
          docValuesTypes.put(merged, mergeDocValuesType(previous, reader.docValues(fi.name))); 
        }
        if (fi.hasNorms()) {
          TypePromoter previous = normValuesTypes.get(merged);
          normValuesTypes.put(merged, mergeDocValuesType(previous, reader.normValues(fi.name))); 
        }
      }
    }
    updatePromoted(normValuesTypes, true);
    updatePromoted(docValuesTypes, false);
    mergeState.fieldInfos = fieldInfosBuilder.finish();
  }
  
  protected void updatePromoted(Map<FieldInfo,TypePromoter> infoAndPromoter, boolean norms) {
    // update any promoted doc values types:
    for (Map.Entry<FieldInfo,TypePromoter> e : infoAndPromoter.entrySet()) {
      FieldInfo fi = e.getKey();
      TypePromoter promoter = e.getValue();
      if (promoter == null) {
        if (norms) {
          fi.setNormValueType(null);
        } else {
          fi.setDocValuesType(null);
        }
      } else {
        assert promoter != TypePromoter.getIdentityPromoter();
        if (norms) {
          if (fi.getNormType() != promoter.type() && !fi.omitsNorms()) {
            // reset the type if we got promoted
            fi.setNormValueType(promoter.type());
          }  
        } else {
          if (fi.getDocValuesType() != promoter.type()) {
            // reset the type if we got promoted
            fi.setDocValuesType(promoter.type());
          }
        }
      }
    }
  }


  /**
   *
   * @return The number of documents in all of the readers
   * @throws CorruptIndexException if the index is corrupt
   * @throws IOException if there is a low-level IO error
   */
  private int mergeFields() throws IOException {
    final StoredFieldsWriter fieldsWriter = codec.storedFieldsFormat().fieldsWriter(directory, mergeState.segmentInfo, context);
    
    try {
      return fieldsWriter.merge(mergeState);
    } finally {
      fieldsWriter.close();
    }
  }

  /**
   * Merge the TermVectors from each of the segments into the new one.
   * @throws IOException
   */
  private final int mergeVectors() throws IOException {
    final TermVectorsWriter termVectorsWriter = codec.termVectorsFormat().vectorsWriter(directory, mergeState.segmentInfo, context);
    
    try {
      return termVectorsWriter.merge(mergeState);
    } finally {
      termVectorsWriter.close();
    }
  }

  // NOTE: removes any "all deleted" readers from mergeState.readers
  private int setDocMaps() throws IOException {
    final int numReaders = mergeState.readers.size();

    // Remap docIDs
    mergeState.docMaps = new MergeState.DocMap[numReaders];
    mergeState.docBase = new int[numReaders];
    mergeState.readerPayloadProcessor = new PayloadProcessorProvider.ReaderPayloadProcessor[numReaders];
    mergeState.currentPayloadProcessor = new PayloadProcessorProvider.PayloadProcessor[numReaders];

    int docBase = 0;

    int i = 0;
    while(i < mergeState.readers.size()) {

      final AtomicReader reader = mergeState.readers.get(i);

      mergeState.docBase[i] = docBase;
      final MergeState.DocMap docMap = MergeState.DocMap.build(reader);
      mergeState.docMaps[i] = docMap;
      docBase += docMap.numDocs();

      if (mergeState.payloadProcessorProvider != null) {
        mergeState.readerPayloadProcessor[i] = mergeState.payloadProcessorProvider.getReaderProcessor(reader);
      }

      i++;
    }

    return docBase;
  }

  private final void mergeTerms(SegmentWriteState segmentWriteState) throws IOException {
    
    final List<Fields> fields = new ArrayList<Fields>();
    final List<ReaderSlice> slices = new ArrayList<ReaderSlice>();

    int docBase = 0;

    for(int readerIndex=0;readerIndex<mergeState.readers.size();readerIndex++) {
      final AtomicReader reader = mergeState.readers.get(readerIndex);
      final Fields f = reader.fields();
      final int maxDoc = reader.maxDoc();
      if (f != null) {
        slices.add(new ReaderSlice(docBase, maxDoc, readerIndex));
        fields.add(f);
      }
      docBase += maxDoc;
    }

    final FieldsConsumer consumer = codec.postingsFormat().fieldsConsumer(segmentWriteState);
    boolean success = false;
    try {
      consumer.merge(mergeState,
                     new MultiFields(fields.toArray(Fields.EMPTY_ARRAY),
                                     slices.toArray(ReaderSlice.EMPTY_ARRAY)));
      success = true;
    } finally {
      if (success) {
        IOUtils.close(consumer);
      } else {
        IOUtils.closeWhileHandlingException(consumer);
      }
    }
  }

  private void mergePerDoc(SegmentWriteState segmentWriteState) throws IOException {
      final PerDocConsumer docsConsumer = codec.docValuesFormat()
          .docsConsumer(new PerDocWriteState(segmentWriteState));
      assert docsConsumer != null;
      boolean success = false;
      try {
        docsConsumer.merge(mergeState);
        success = true;
      } finally {
        if (success) {
          IOUtils.close(docsConsumer);
        } else {
          IOUtils.closeWhileHandlingException(docsConsumer);
        }
      }
  }
  
  private void mergeNorms(SegmentWriteState segmentWriteState) throws IOException {
    final PerDocConsumer docsConsumer = codec.normsFormat()
        .docsConsumer(new PerDocWriteState(segmentWriteState));
    assert docsConsumer != null;
    boolean success = false;
    try {
      docsConsumer.merge(mergeState);
      success = true;
    } finally {
      if (success) {
        IOUtils.close(docsConsumer);
      } else {
        IOUtils.closeWhileHandlingException(docsConsumer);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/119.java