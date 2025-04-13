error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2402.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2402.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2402.java
text:
```scala
final S@@tring indexFileName = IndexFileNames.segmentFileName(state.segmentName, state.segmentSuffix, TERMS_INDEX_EXTENSION);

package org.apache.lucene.index.codecs;

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

import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexFileNames;
import org.apache.lucene.index.SegmentWriteState;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.CodecUtil;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.packed.PackedInts;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Selects every Nth term as and index term, and hold term
 * bytes fully expanded in memory.  This terms index
 * supports seeking by ord.  See {@link
 * VariableGapTermsIndexWriter} for a more memory efficient
 * terms index that does not support seeking by ord.
 *
 * @lucene.experimental */
public class FixedGapTermsIndexWriter extends TermsIndexWriterBase {
  protected final IndexOutput out;

  /** Extension of terms index file */
  static final String TERMS_INDEX_EXTENSION = "tii";

  final static String CODEC_NAME = "SIMPLE_STANDARD_TERMS_INDEX";
  final static int VERSION_START = 0;
  final static int VERSION_CURRENT = VERSION_START;

  final private int termIndexInterval;

  private final List<SimpleFieldWriter> fields = new ArrayList<SimpleFieldWriter>();
  private final FieldInfos fieldInfos; // unread

  public FixedGapTermsIndexWriter(SegmentWriteState state) throws IOException {
    final String indexFileName = IndexFileNames.segmentFileName(state.segmentName, state.formatId, TERMS_INDEX_EXTENSION);
    termIndexInterval = state.termIndexInterval;
    out = state.directory.createOutput(indexFileName, state.context);
    boolean success = false;
    try {
      fieldInfos = state.fieldInfos;
      writeHeader(out);
      out.writeInt(termIndexInterval);
      success = true;
    } finally {
      if (!success) {
        IOUtils.closeWhileHandlingException(out);
      }
    }
  }
  
  protected void writeHeader(IndexOutput out) throws IOException {
    CodecUtil.writeHeader(out, CODEC_NAME, VERSION_CURRENT);
    // Placeholder for dir offset
    out.writeLong(0);
  }

  @Override
  public FieldWriter addField(FieldInfo field, long termsFilePointer) {
    //System.out.println("FGW: addFfield=" + field.name);
    SimpleFieldWriter writer = new SimpleFieldWriter(field, termsFilePointer);
    fields.add(writer);
    return writer;
  }

  /** NOTE: if your codec does not sort in unicode code
   *  point order, you must override this method, to simply
   *  return indexedTerm.length. */
  protected int indexedTermPrefixLength(final BytesRef priorTerm, final BytesRef indexedTerm) {
    // As long as codec sorts terms in unicode codepoint
    // order, we can safely strip off the non-distinguishing
    // suffix to save RAM in the loaded terms index.
    final int idxTermOffset = indexedTerm.offset;
    final int priorTermOffset = priorTerm.offset;
    final int limit = Math.min(priorTerm.length, indexedTerm.length);
    for(int byteIdx=0;byteIdx<limit;byteIdx++) {
      if (priorTerm.bytes[priorTermOffset+byteIdx] != indexedTerm.bytes[idxTermOffset+byteIdx]) {
        return byteIdx+1;
      }
    }
    return Math.min(1+priorTerm.length, indexedTerm.length);
  }

  private class SimpleFieldWriter extends FieldWriter {
    final FieldInfo fieldInfo;
    int numIndexTerms;
    final long indexStart;
    final long termsStart;
    long packedIndexStart;
    long packedOffsetsStart;
    private long numTerms;

    // TODO: we could conceivably make a PackedInts wrapper
    // that auto-grows... then we wouldn't force 6 bytes RAM
    // per index term:
    private short[] termLengths;
    private int[] termsPointerDeltas;
    private long lastTermsPointer;
    private long totTermLength;

    private final BytesRef lastTerm = new BytesRef();

    SimpleFieldWriter(FieldInfo fieldInfo, long termsFilePointer) {
      this.fieldInfo = fieldInfo;
      indexStart = out.getFilePointer();
      termsStart = lastTermsPointer = termsFilePointer;
      termLengths = new short[0];
      termsPointerDeltas = new int[0];
    }

    @Override
    public boolean checkIndexTerm(BytesRef text, TermStats stats) throws IOException {
      // First term is first indexed term:
      //System.out.println("FGW: checkIndexTerm text=" + text.utf8ToString());
      if (0 == (numTerms++ % termIndexInterval)) {
        return true;
      } else {
        if (0 == numTerms % termIndexInterval) {
          // save last term just before next index term so we
          // can compute wasted suffix
          lastTerm.copy(text);
        }
        return false;
      }
    }

    @Override
    public void add(BytesRef text, TermStats stats, long termsFilePointer) throws IOException {
      final int indexedTermLength = indexedTermPrefixLength(lastTerm, text);
      //System.out.println("FGW: add text=" + text.utf8ToString() + " " + text + " fp=" + termsFilePointer);

      // write only the min prefix that shows the diff
      // against prior term
      out.writeBytes(text.bytes, text.offset, indexedTermLength);

      if (termLengths.length == numIndexTerms) {
        termLengths = ArrayUtil.grow(termLengths);
      }
      if (termsPointerDeltas.length == numIndexTerms) {
        termsPointerDeltas = ArrayUtil.grow(termsPointerDeltas);
      }

      // save delta terms pointer
      termsPointerDeltas[numIndexTerms] = (int) (termsFilePointer - lastTermsPointer);
      lastTermsPointer = termsFilePointer;

      // save term length (in bytes)
      assert indexedTermLength <= Short.MAX_VALUE;
      termLengths[numIndexTerms] = (short) indexedTermLength;
      totTermLength += indexedTermLength;

      lastTerm.copy(text);
      numIndexTerms++;
    }

    @Override
    public void finish(long termsFilePointer) throws IOException {

      // write primary terms dict offsets
      packedIndexStart = out.getFilePointer();

      PackedInts.Writer w = PackedInts.getWriter(out, numIndexTerms, PackedInts.bitsRequired(termsFilePointer));

      // relative to our indexStart
      long upto = 0;
      for(int i=0;i<numIndexTerms;i++) {
        upto += termsPointerDeltas[i];
        w.add(upto);
      }
      w.finish();

      packedOffsetsStart = out.getFilePointer();

      // write offsets into the byte[] terms
      w = PackedInts.getWriter(out, 1+numIndexTerms, PackedInts.bitsRequired(totTermLength));
      upto = 0;
      for(int i=0;i<numIndexTerms;i++) {
        w.add(upto);
        upto += termLengths[i];
      }
      w.add(upto);
      w.finish();

      // our referrer holds onto us, while other fields are
      // being written, so don't tie up this RAM:
      termLengths = null;
      termsPointerDeltas = null;
    }
  }

  public void close() throws IOException {
    boolean success = false;
    try {
      final long dirStart = out.getFilePointer();
      final int fieldCount = fields.size();
      
      int nonNullFieldCount = 0;
      for(int i=0;i<fieldCount;i++) {
        SimpleFieldWriter field = fields.get(i);
        if (field.numIndexTerms > 0) {
          nonNullFieldCount++;
        }
      }
      
      out.writeVInt(nonNullFieldCount);
      for(int i=0;i<fieldCount;i++) {
        SimpleFieldWriter field = fields.get(i);
        if (field.numIndexTerms > 0) {
          out.writeVInt(field.fieldInfo.number);
          out.writeVInt(field.numIndexTerms);
          out.writeVLong(field.termsStart);
          out.writeVLong(field.indexStart);
          out.writeVLong(field.packedIndexStart);
          out.writeVLong(field.packedOffsetsStart);
        }
      }
      writeTrailer(dirStart);
      success = true;
    } finally {
      if (success) {
        IOUtils.close(out);
      } else {
        IOUtils.closeWhileHandlingException(out);
      }
    }
  }

  protected void writeTrailer(long dirStart) throws IOException {
    out.seek(CodecUtil.headerLength(CODEC_NAME));
    out.writeLong(dirStart);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2402.java