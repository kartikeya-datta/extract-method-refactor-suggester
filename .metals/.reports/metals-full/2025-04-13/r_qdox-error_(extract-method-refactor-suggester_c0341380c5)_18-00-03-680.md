error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1916.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1916.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 70
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1916.java
text:
```scala
public final class AssertingPostingsFormat extends PostingsFormat {

p@@ackage org.apache.lucene.codecs.asserting;

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
import java.util.Iterator;

import org.apache.lucene.codecs.FieldsConsumer;
import org.apache.lucene.codecs.FieldsProducer;
import org.apache.lucene.codecs.PostingsConsumer;
import org.apache.lucene.codecs.PostingsFormat;
import org.apache.lucene.codecs.TermStats;
import org.apache.lucene.codecs.TermsConsumer;
import org.apache.lucene.codecs.lucene40.Lucene40PostingsFormat;
import org.apache.lucene.index.AssertingAtomicReader;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.SegmentReadState;
import org.apache.lucene.index.SegmentWriteState;
import org.apache.lucene.index.Terms;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.OpenBitSet;

/**
 * Just like {@link Lucene40PostingsFormat} but with additional asserts.
 */
public class AssertingPostingsFormat extends PostingsFormat {
  private final PostingsFormat in = new Lucene40PostingsFormat();
  
  public AssertingPostingsFormat() {
    super("Asserting");
  }
  
  @Override
  public FieldsConsumer fieldsConsumer(SegmentWriteState state) throws IOException {
    return new AssertingFieldsConsumer(in.fieldsConsumer(state));
  }

  @Override
  public FieldsProducer fieldsProducer(SegmentReadState state) throws IOException {
    return new AssertingFieldsProducer(in.fieldsProducer(state));
  }
  
  static class AssertingFieldsProducer extends FieldsProducer {
    private final FieldsProducer in;
    
    AssertingFieldsProducer(FieldsProducer in) {
      this.in = in;
    }
    
    @Override
    public void close() throws IOException {
      in.close();
    }

    @Override
    public Iterator<String> iterator() {
      Iterator<String> iterator = in.iterator();
      assert iterator != null;
      return iterator;
    }

    @Override
    public Terms terms(String field) throws IOException {
      Terms terms = in.terms(field);
      return terms == null ? null : new AssertingAtomicReader.AssertingTerms(terms);
    }

    @Override
    public int size() {
      return in.size();
    }
  }
  
  static class AssertingFieldsConsumer extends FieldsConsumer {
    private final FieldsConsumer in;
    
    AssertingFieldsConsumer(FieldsConsumer in) {
      this.in = in;
    }
    
    @Override
    public TermsConsumer addField(FieldInfo field) throws IOException {
      TermsConsumer consumer = in.addField(field);
      assert consumer != null;
      return new AssertingTermsConsumer(consumer, field);
    }

    @Override
    public void close() throws IOException {
      in.close();
    }
  }
  
  static enum TermsConsumerState { INITIAL, START, FINISHED };
  static class AssertingTermsConsumer extends TermsConsumer {
    private final TermsConsumer in;
    private final FieldInfo fieldInfo;
    private BytesRef lastTerm = null;
    private TermsConsumerState state = TermsConsumerState.INITIAL;
    private AssertingPostingsConsumer lastPostingsConsumer = null;
    private long sumTotalTermFreq = 0;
    private long sumDocFreq = 0;
    private OpenBitSet visitedDocs = new OpenBitSet();
    
    AssertingTermsConsumer(TermsConsumer in, FieldInfo fieldInfo) {
      this.in = in;
      this.fieldInfo = fieldInfo;
    }
    
    @Override
    public PostingsConsumer startTerm(BytesRef text) throws IOException {
      assert state == TermsConsumerState.INITIAL || state == TermsConsumerState.START && lastPostingsConsumer.docFreq == 0;
      state = TermsConsumerState.START;
      assert lastTerm == null || in.getComparator().compare(text, lastTerm) > 0;
      lastTerm = BytesRef.deepCopyOf(text);
      return lastPostingsConsumer = new AssertingPostingsConsumer(in.startTerm(text), fieldInfo, visitedDocs);
    }

    @Override
    public void finishTerm(BytesRef text, TermStats stats) throws IOException {
      assert state == TermsConsumerState.START;
      state = TermsConsumerState.INITIAL;
      assert text.equals(lastTerm);
      assert stats.docFreq > 0; // otherwise, this method should not be called.
      assert stats.docFreq == lastPostingsConsumer.docFreq;
      sumDocFreq += stats.docFreq;
      if (fieldInfo.getIndexOptions() == IndexOptions.DOCS_ONLY) {
        assert stats.totalTermFreq == -1;
      } else {
        assert stats.totalTermFreq == lastPostingsConsumer.totalTermFreq;
        sumTotalTermFreq += stats.totalTermFreq;
      }
      in.finishTerm(text, stats);
    }

    @Override
    public void finish(long sumTotalTermFreq, long sumDocFreq, int docCount) throws IOException {
      assert state == TermsConsumerState.INITIAL || state == TermsConsumerState.START && lastPostingsConsumer.docFreq == 0;
      state = TermsConsumerState.FINISHED;
      assert docCount >= 0;
      assert docCount == visitedDocs.cardinality();
      assert sumDocFreq >= docCount;
      assert sumDocFreq == this.sumDocFreq;
      if (fieldInfo.getIndexOptions() == IndexOptions.DOCS_ONLY) {
        assert sumTotalTermFreq == -1;
      } else {
        assert sumTotalTermFreq >= sumDocFreq;
        assert sumTotalTermFreq == this.sumTotalTermFreq;
      }
      in.finish(sumTotalTermFreq, sumDocFreq, docCount);
    }

    @Override
    public Comparator<BytesRef> getComparator() throws IOException {
      return in.getComparator();
    }
  }
  
  static enum PostingsConsumerState { INITIAL, START };
  static class AssertingPostingsConsumer extends PostingsConsumer {
    private final PostingsConsumer in;
    private final FieldInfo fieldInfo;
    private final OpenBitSet visitedDocs;
    private PostingsConsumerState state = PostingsConsumerState.INITIAL;
    private int freq;
    private int positionCount;
    private int lastPosition = 0;
    private int lastStartOffset = 0;
    int docFreq = 0;
    long totalTermFreq = 0;
    
    AssertingPostingsConsumer(PostingsConsumer in, FieldInfo fieldInfo, OpenBitSet visitedDocs) {
      this.in = in;
      this.fieldInfo = fieldInfo;
      this.visitedDocs = visitedDocs;
    }

    @Override
    public void startDoc(int docID, int freq) throws IOException {
      assert state == PostingsConsumerState.INITIAL;
      state = PostingsConsumerState.START;
      assert docID >= 0;
      if (fieldInfo.getIndexOptions() == IndexOptions.DOCS_ONLY) {
        assert freq == -1;
        this.freq = 0; // we don't expect any positions here
      } else {
        assert freq > 0;
        this.freq = freq;
        totalTermFreq += freq;
      }
      this.positionCount = 0;
      this.lastPosition = 0;
      this.lastStartOffset = 0;
      docFreq++;
      visitedDocs.set(docID);
      in.startDoc(docID, freq);
    }

    @Override
    public void addPosition(int position, BytesRef payload, int startOffset, int endOffset) throws IOException {
      assert state == PostingsConsumerState.START;
      assert positionCount < freq;
      positionCount++;
      assert position >= lastPosition || position == -1; /* we still allow -1 from old 3.x indexes */
      lastPosition = position;
      if (fieldInfo.getIndexOptions() == IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS) {
        assert startOffset >= 0;
        assert startOffset >= lastStartOffset;
        lastStartOffset = startOffset;
        assert endOffset >= startOffset;
      } else {
        assert startOffset == -1;
        assert endOffset == -1;
      }
      if (payload != null) {
        assert fieldInfo.hasPayloads();
      }
      in.addPosition(position, payload, startOffset, endOffset);
    }

    @Override
    public void finishDoc() throws IOException {
      assert state == PostingsConsumerState.START;
      state = PostingsConsumerState.INITIAL;
      if (fieldInfo.getIndexOptions().compareTo(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS) < 0) {
        assert positionCount == 0; // we should not have fed any positions!
      } else {
        assert positionCount == freq;
      }
      in.finishDoc();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1916.java