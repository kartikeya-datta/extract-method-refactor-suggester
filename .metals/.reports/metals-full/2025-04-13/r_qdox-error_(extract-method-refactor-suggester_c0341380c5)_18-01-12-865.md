error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/299.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/299.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/299.java
text:
```scala
i@@f (4+((long) state.numDocsInStore)*8 != state.directory.fileLength(state.docStoreSegmentName + "." + IndexFileNames.FIELDS_INDEX_EXTENSION))

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

import java.util.Map;
import java.io.IOException;
import org.apache.lucene.store.RAMOutputStream;
import org.apache.lucene.util.ArrayUtil;

/** This is a DocFieldConsumer that writes stored fields. */
final class StoredFieldsWriter extends DocFieldConsumer {

  FieldsWriter fieldsWriter;
  final DocumentsWriter docWriter;
  int lastDocID;

  PerDoc[] docFreeList = new PerDoc[1];
  int freeCount;

  public StoredFieldsWriter(DocumentsWriter docWriter) {
    this.docWriter = docWriter;
  }

  public DocFieldConsumerPerThread addThread(DocFieldProcessorPerThread docFieldProcessorPerThread) throws IOException {
    return new StoredFieldsWriterPerThread(docFieldProcessorPerThread, this);
  }

  synchronized public void flush(Map threadsAndFields, SegmentWriteState state) throws IOException {

    if (state.numDocsInStore > 0) {
      // It's possible that all documents seen in this segment
      // hit non-aborting exceptions, in which case we will
      // not have yet init'd the FieldsWriter:
      initFieldsWriter();

      // Fill fdx file to include any final docs that we
      // skipped because they hit non-aborting exceptions
      fill(state.numDocsInStore - docWriter.getDocStoreOffset());
    }

    if (fieldsWriter != null)
      fieldsWriter.flush();
  }
  
  private void initFieldsWriter() throws IOException {
    if (fieldsWriter == null) {
      final String docStoreSegment = docWriter.getDocStoreSegment();
      if (docStoreSegment != null) {
        assert docStoreSegment != null;
        fieldsWriter = new FieldsWriter(docWriter.directory,
                                        docStoreSegment,
                                        fieldInfos);
        docWriter.addOpenFile(docStoreSegment + "." + IndexFileNames.FIELDS_EXTENSION);
        docWriter.addOpenFile(docStoreSegment + "." + IndexFileNames.FIELDS_INDEX_EXTENSION);
        lastDocID = 0;
      }
    }
  }

  synchronized public void closeDocStore(SegmentWriteState state) throws IOException {
    final int inc = state.numDocsInStore - lastDocID;
    if (inc > 0) {
      initFieldsWriter();
      fill(state.numDocsInStore - docWriter.getDocStoreOffset());
    }

    if (fieldsWriter != null) {
      fieldsWriter.close();
      fieldsWriter = null;
      lastDocID = 0;
      assert state.docStoreSegmentName != null;
      state.flushedFiles.add(state.docStoreSegmentName + "." + IndexFileNames.FIELDS_EXTENSION);
      state.flushedFiles.add(state.docStoreSegmentName + "." + IndexFileNames.FIELDS_INDEX_EXTENSION);

      state.docWriter.removeOpenFile(state.docStoreSegmentName + "." + IndexFileNames.FIELDS_EXTENSION);
      state.docWriter.removeOpenFile(state.docStoreSegmentName + "." + IndexFileNames.FIELDS_INDEX_EXTENSION);

      if (4+state.numDocsInStore*8 != state.directory.fileLength(state.docStoreSegmentName + "." + IndexFileNames.FIELDS_INDEX_EXTENSION))
        throw new RuntimeException("after flush: fdx size mismatch: " + state.numDocsInStore + " docs vs " + state.directory.fileLength(state.docStoreSegmentName + "." + IndexFileNames.FIELDS_INDEX_EXTENSION) + " length in bytes of " + state.docStoreSegmentName + "." + IndexFileNames.FIELDS_INDEX_EXTENSION);
    }
  }

  int allocCount;

  synchronized PerDoc getPerDoc() {
    if (freeCount == 0) {
      allocCount++;
      if (allocCount > docFreeList.length) {
        // Grow our free list up front to make sure we have
        // enough space to recycle all outstanding PerDoc
        // instances
        assert allocCount == 1+docFreeList.length;
        docFreeList = new PerDoc[ArrayUtil.getNextSize(allocCount)];
      }
      return new PerDoc();
    } else
      return docFreeList[--freeCount];
  }

  synchronized void abort() {
    if (fieldsWriter != null) {
      try {
        fieldsWriter.close();
      } catch (Throwable t) {
      }
      fieldsWriter = null;
      lastDocID = 0;
    }
  }

  /** Fills in any hole in the docIDs */
  void fill(int docID) throws IOException {
    final int docStoreOffset = docWriter.getDocStoreOffset();

    // We must "catch up" for all docs before us
    // that had no stored fields:
    final int end = docID+docStoreOffset;
    while(lastDocID < end) {
      fieldsWriter.skipDocument();
      lastDocID++;
    }
  }

  synchronized void finishDocument(PerDoc perDoc) throws IOException {
    assert docWriter.writer.testPoint("StoredFieldsWriter.finishDocument start");
    initFieldsWriter();

    fill(perDoc.docID);

    // Append stored fields to the real FieldsWriter:
    fieldsWriter.flushDocument(perDoc.numStoredFields, perDoc.fdt);
    lastDocID++;
    perDoc.reset();
    free(perDoc);
    assert docWriter.writer.testPoint("StoredFieldsWriter.finishDocument end");
  }

  public boolean freeRAM() {
    return false;
  }

  synchronized void free(PerDoc perDoc) {
    assert freeCount < docFreeList.length;
    assert 0 == perDoc.numStoredFields;
    assert 0 == perDoc.fdt.length();
    assert 0 == perDoc.fdt.getFilePointer();
    docFreeList[freeCount++] = perDoc;
  }

  class PerDoc extends DocumentsWriter.DocWriter {

    // TODO: use something more memory efficient; for small
    // docs the 1024 buffer size of RAMOutputStream wastes alot
    RAMOutputStream fdt = new RAMOutputStream();
    int numStoredFields;

    void reset() {
      fdt.reset();
      numStoredFields = 0;
    }

    void abort() {
      reset();
      free(this);
    }

    public long sizeInBytes() {
      return fdt.sizeInBytes();
    }

    public void finish() throws IOException {
      finishDocument(this);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/299.java