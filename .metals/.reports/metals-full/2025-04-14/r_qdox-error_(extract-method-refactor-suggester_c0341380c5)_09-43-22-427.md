error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2022.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2022.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2022.java
text:
```scala
T@@ermsEnum termsEnum = in.intersect(automaton, bytes);

package org.apache.lucene.index;

import java.io.IOException;
import java.util.Iterator;

import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.automaton.CompiledAutomaton;

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

/**
 * A {@link FilterAtomicReader} that can be used to apply
 * additional checks for tests.
 */
public class AssertingAtomicReader extends FilterAtomicReader {

  public AssertingAtomicReader(AtomicReader in) {
    super(in);
    // check some basic reader sanity
    assert in.maxDoc() >= 0;
    assert in.numDocs() <= in.maxDoc();
    assert in.numDeletedDocs() + in.numDocs() == in.maxDoc();
    assert !in.hasDeletions() || in.numDeletedDocs() > 0 && in.numDocs() < in.maxDoc();
  }

  @Override
  public Fields fields() throws IOException {
    Fields fields = super.fields();
    return fields == null ? null : new AssertingFields(fields);
  }
  
  @Override
  public Fields getTermVectors(int docID) throws IOException {
    Fields fields = super.getTermVectors(docID);
    return fields == null ? null : new AssertingFields(fields);
  }

  /**
   * Wraps a Fields but with additional asserts
   */
  public static class AssertingFields extends FilterFields {
    public AssertingFields(Fields in) {
      super(in);
    }

    @Override
    public Iterator<String> iterator() {
      Iterator<String> iterator = super.iterator();
      assert iterator != null;
      return iterator;
    }

    @Override
    public Terms terms(String field) throws IOException {
      Terms terms = super.terms(field);
      return terms == null ? null : new AssertingTerms(terms);
    }
  }
  
  /**
   * Wraps a Terms but with additional asserts
   */
  public static class AssertingTerms extends FilterTerms {
    public AssertingTerms(Terms in) {
      super(in);
    }

    @Override
    public TermsEnum intersect(CompiledAutomaton automaton, BytesRef bytes) throws IOException {
      TermsEnum termsEnum = super.intersect(automaton, bytes);
      assert termsEnum != null;
      assert bytes == null || bytes.isValid();
      return new AssertingTermsEnum(termsEnum);
    }

    @Override
    public TermsEnum iterator(TermsEnum reuse) throws IOException {
      // TODO: should we give this thing a random to be super-evil,
      // and randomly *not* unwrap?
      if (reuse instanceof AssertingTermsEnum) {
        reuse = ((AssertingTermsEnum) reuse).in;
      }
      TermsEnum termsEnum = super.iterator(reuse);
      assert termsEnum != null;
      return new AssertingTermsEnum(termsEnum);
    }
  }
  
  static class AssertingTermsEnum extends FilterTermsEnum {
    private enum State {INITIAL, POSITIONED, UNPOSITIONED};
    private State state = State.INITIAL;

    public AssertingTermsEnum(TermsEnum in) {
      super(in);
    }

    @Override
    public DocsEnum docs(Bits liveDocs, DocsEnum reuse, int flags) throws IOException {
      assert state == State.POSITIONED: "docs(...) called on unpositioned TermsEnum";

      // TODO: should we give this thing a random to be super-evil,
      // and randomly *not* unwrap?
      if (reuse instanceof AssertingDocsEnum) {
        reuse = ((AssertingDocsEnum) reuse).in;
      }
      DocsEnum docs = super.docs(liveDocs, reuse, flags);
      return docs == null ? null : new AssertingDocsEnum(docs);
    }

    @Override
    public DocsAndPositionsEnum docsAndPositions(Bits liveDocs, DocsAndPositionsEnum reuse, int flags) throws IOException {
      assert state == State.POSITIONED: "docsAndPositions(...) called on unpositioned TermsEnum";

      // TODO: should we give this thing a random to be super-evil,
      // and randomly *not* unwrap?
      if (reuse instanceof AssertingDocsAndPositionsEnum) {
        reuse = ((AssertingDocsAndPositionsEnum) reuse).in;
      }
      DocsAndPositionsEnum docs = super.docsAndPositions(liveDocs, reuse, flags);
      return docs == null ? null : new AssertingDocsAndPositionsEnum(docs);
    }

    // TODO: we should separately track if we are 'at the end' ?
    // someone should not call next() after it returns null!!!!
    @Override
    public BytesRef next() throws IOException {
      assert state == State.INITIAL || state == State.POSITIONED: "next() called on unpositioned TermsEnum";
      BytesRef result = super.next();
      if (result == null) {
        state = State.UNPOSITIONED;
      } else {
        assert result.isValid();
        state = State.POSITIONED;
      }
      return result;
    }

    @Override
    public long ord() throws IOException {
      assert state == State.POSITIONED : "ord() called on unpositioned TermsEnum";
      return super.ord();
    }

    @Override
    public int docFreq() throws IOException {
      assert state == State.POSITIONED : "docFreq() called on unpositioned TermsEnum";
      return super.docFreq();
    }

    @Override
    public long totalTermFreq() throws IOException {
      assert state == State.POSITIONED : "totalTermFreq() called on unpositioned TermsEnum";
      return super.totalTermFreq();
    }

    @Override
    public BytesRef term() throws IOException {
      assert state == State.POSITIONED : "term() called on unpositioned TermsEnum";
      BytesRef ret = super.term();
      assert ret == null || ret.isValid();
      return ret;
    }

    @Override
    public void seekExact(long ord) throws IOException {
      super.seekExact(ord);
      state = State.POSITIONED;
    }

    @Override
    public SeekStatus seekCeil(BytesRef term, boolean useCache) throws IOException {
      assert term.isValid();
      SeekStatus result = super.seekCeil(term, useCache);
      if (result == SeekStatus.END) {
        state = State.UNPOSITIONED;
      } else {
        state = State.POSITIONED;
      }
      return result;
    }

    @Override
    public boolean seekExact(BytesRef text, boolean useCache) throws IOException {
      assert text.isValid();
      if (super.seekExact(text, useCache)) {
        state = State.POSITIONED;
        return true;
      } else {
        state = State.UNPOSITIONED;
        return false;
      }
    }

    @Override
    public TermState termState() throws IOException {
      assert state == State.POSITIONED : "termState() called on unpositioned TermsEnum";
      return super.termState();
    }

    @Override
    public void seekExact(BytesRef term, TermState state) throws IOException {
      assert term.isValid();
      super.seekExact(term, state);
      this.state = State.POSITIONED;
    }
  }
  
  static enum DocsEnumState { START, ITERATING, FINISHED };
  static class AssertingDocsEnum extends FilterDocsEnum {
    private DocsEnumState state = DocsEnumState.START;
    private int doc;
    
    public AssertingDocsEnum(DocsEnum in) {
      super(in);
      int docid = in.docID();
      assert docid == -1 || docid == DocIdSetIterator.NO_MORE_DOCS : "invalid initial doc id: " + docid;
      doc = -1;
    }

    @Override
    public int nextDoc() throws IOException {
      assert state != DocsEnumState.FINISHED : "nextDoc() called after NO_MORE_DOCS";
      int nextDoc = super.nextDoc();
      assert nextDoc > doc : "backwards nextDoc from " + doc + " to " + nextDoc;
      if (nextDoc == DocIdSetIterator.NO_MORE_DOCS) {
        state = DocsEnumState.FINISHED;
      } else {
        state = DocsEnumState.ITERATING;
      }
      assert docID() == nextDoc;
      return doc = nextDoc;
    }

    @Override
    public int advance(int target) throws IOException {
      assert state != DocsEnumState.FINISHED : "advance() called after NO_MORE_DOCS";
      assert target > doc : "target must be > docID(), got " + target + " <= " + doc;
      int advanced = super.advance(target);
      assert advanced >= target : "backwards advance from: " + target + " to: " + advanced;
      if (advanced == DocIdSetIterator.NO_MORE_DOCS) {
        state = DocsEnumState.FINISHED;
      } else {
        state = DocsEnumState.ITERATING;
      }
      assert docID() == advanced;
      return doc = advanced;
    }

    // NOTE: We don't assert anything for docId(). Specifically DocsEnum javadocs
    // are ambiguous with DocIdSetIterator here, DocIdSetIterator says its ok
    // to call this method before nextDoc(), just that it must be -1 or NO_MORE_DOCS!

    @Override
    public int freq() throws IOException {
      assert state != DocsEnumState.START : "freq() called before nextDoc()/advance()";
      assert state != DocsEnumState.FINISHED : "freq() called after NO_MORE_DOCS";
      int freq = super.freq();
      assert freq > 0;
      return freq;
    }
  }
  
  static class AssertingDocsAndPositionsEnum extends FilterDocsAndPositionsEnum {
    private DocsEnumState state = DocsEnumState.START;
    private int positionMax = 0;
    private int positionCount = 0;
    private int doc;

    public AssertingDocsAndPositionsEnum(DocsAndPositionsEnum in) {
      super(in);
      int docid = in.docID();
      assert docid == -1 || docid == DocIdSetIterator.NO_MORE_DOCS : "invalid initial doc id: " + docid;
      doc = -1;
    }

    @Override
    public int nextDoc() throws IOException {
      assert state != DocsEnumState.FINISHED : "nextDoc() called after NO_MORE_DOCS";
      int nextDoc = super.nextDoc();
      assert nextDoc > doc : "backwards nextDoc from " + doc + " to " + nextDoc;
      positionCount = 0;
      if (nextDoc == DocIdSetIterator.NO_MORE_DOCS) {
        state = DocsEnumState.FINISHED;
        positionMax = 0;
      } else {
        state = DocsEnumState.ITERATING;
        positionMax = super.freq();
      }
      assert docID() == nextDoc;
      return doc = nextDoc;
    }

    @Override
    public int advance(int target) throws IOException {
      assert state != DocsEnumState.FINISHED : "advance() called after NO_MORE_DOCS";
      assert target > doc : "target must be > docID(), got " + target + " <= " + doc;
      int advanced = super.advance(target);
      assert advanced >= target : "backwards advance from: " + target + " to: " + advanced;
      positionCount = 0;
      if (advanced == DocIdSetIterator.NO_MORE_DOCS) {
        state = DocsEnumState.FINISHED;
        positionMax = 0;
      } else {
        state = DocsEnumState.ITERATING;
        positionMax = super.freq();
      }
      assert docID() == advanced;
      return doc = advanced;
    }

    @Override
    public int freq() throws IOException {
      assert state != DocsEnumState.START : "freq() called before nextDoc()/advance()";
      assert state != DocsEnumState.FINISHED : "freq() called after NO_MORE_DOCS";
      int freq = super.freq();
      assert freq > 0;
      return freq;
    }

    @Override
    public int nextPosition() throws IOException {
      assert state != DocsEnumState.START : "nextPosition() called before nextDoc()/advance()";
      assert state != DocsEnumState.FINISHED : "nextPosition() called after NO_MORE_DOCS";
      assert positionCount < positionMax : "nextPosition() called more than freq() times!";
      int position = super.nextPosition();
      assert position >= 0 || position == -1 : "invalid position: " + position;
      positionCount++;
      return position;
    }

    @Override
    public int startOffset() throws IOException {
      assert state != DocsEnumState.START : "startOffset() called before nextDoc()/advance()";
      assert state != DocsEnumState.FINISHED : "startOffset() called after NO_MORE_DOCS";
      assert positionCount > 0 : "startOffset() called before nextPosition()!";
      return super.startOffset();
    }

    @Override
    public int endOffset() throws IOException {
      assert state != DocsEnumState.START : "endOffset() called before nextDoc()/advance()";
      assert state != DocsEnumState.FINISHED : "endOffset() called after NO_MORE_DOCS";
      assert positionCount > 0 : "endOffset() called before nextPosition()!";
      return super.endOffset();
    }

    @Override
    public BytesRef getPayload() throws IOException {
      assert state != DocsEnumState.START : "getPayload() called before nextDoc()/advance()";
      assert state != DocsEnumState.FINISHED : "getPayload() called after NO_MORE_DOCS";
      assert positionCount > 0 : "getPayload() called before nextPosition()!";
      BytesRef payload = super.getPayload();
      assert payload == null || payload.isValid() && payload.length > 0 : "getPayload() returned payload with invalid length!";
      return payload;
    }
  }
  
  /** Wraps a NumericDocValues but with additional asserts */
  public static class AssertingNumericDocValues extends NumericDocValues {
    private final NumericDocValues in;
    private final int maxDoc;
    
    public AssertingNumericDocValues(NumericDocValues in, int maxDoc) {
      this.in = in;
      this.maxDoc = maxDoc;
    }

    @Override
    public long get(int docID) {
      assert docID >= 0 && docID < maxDoc;
      return in.get(docID);
    }    
  }
  
  /** Wraps a BinaryDocValues but with additional asserts */
  public static class AssertingBinaryDocValues extends BinaryDocValues {
    private final BinaryDocValues in;
    private final int maxDoc;
    
    public AssertingBinaryDocValues(BinaryDocValues in, int maxDoc) {
      this.in = in;
      this.maxDoc = maxDoc;
    }

    @Override
    public void get(int docID, BytesRef result) {
      assert docID >= 0 && docID < maxDoc;
      assert result.isValid();
      in.get(docID, result);
      assert result.isValid();
    }
  }
  
  /** Wraps a SortedDocValues but with additional asserts */
  public static class AssertingSortedDocValues extends SortedDocValues {
    private final SortedDocValues in;
    private final int maxDoc;
    private final int valueCount;
    
    public AssertingSortedDocValues(SortedDocValues in, int maxDoc) {
      this.in = in;
      this.maxDoc = maxDoc;
      this.valueCount = in.getValueCount();
      assert valueCount >= 1 && valueCount <= maxDoc;
    }

    @Override
    public int getOrd(int docID) {
      assert docID >= 0 && docID < maxDoc;
      int ord = in.getOrd(docID);
      assert ord >= 0 && ord < valueCount;
      return ord;
    }

    @Override
    public void lookupOrd(int ord, BytesRef result) {
      assert ord >= 0 && ord < valueCount;
      assert result.isValid();
      in.lookupOrd(ord, result);
      assert result.isValid();
    }

    @Override
    public int getValueCount() {
      int valueCount = in.getValueCount();
      assert valueCount == this.valueCount; // should not change
      return valueCount;
    }

    @Override
    public void get(int docID, BytesRef result) {
      assert docID >= 0 && docID < maxDoc;
      assert result.isValid();
      in.get(docID, result);
      assert result.isValid();
    }

    @Override
    public int lookupTerm(BytesRef key) {
      assert key.isValid();
      int result = in.lookupTerm(key);
      assert result < valueCount;
      assert key.isValid();
      return result;
    }
  }
  
  /** Wraps a SortedSetDocValues but with additional asserts */
  public static class AssertingSortedSetDocValues extends SortedSetDocValues {
    private final SortedSetDocValues in;
    private final int maxDoc;
    private final long valueCount;
    long lastOrd = NO_MORE_ORDS;
    
    public AssertingSortedSetDocValues(SortedSetDocValues in, int maxDoc) {
      this.in = in;
      this.maxDoc = maxDoc;
      this.valueCount = in.getValueCount();
      assert valueCount >= 0;
    }
    
    @Override
    public long nextOrd() {
      assert lastOrd != NO_MORE_ORDS;
      long ord = in.nextOrd();
      assert ord < valueCount;
      assert ord == NO_MORE_ORDS || ord > lastOrd;
      lastOrd = ord;
      return ord;
    }

    @Override
    public void setDocument(int docID) {
      assert docID >= 0 && docID < maxDoc : "docid=" + docID + ",maxDoc=" + maxDoc;
      in.setDocument(docID);
      lastOrd = -2;
    }

    @Override
    public void lookupOrd(long ord, BytesRef result) {
      assert ord >= 0 && ord < valueCount;
      assert result.isValid();
      in.lookupOrd(ord, result);
      assert result.isValid();
    }

    @Override
    public long getValueCount() {
      long valueCount = in.getValueCount();
      assert valueCount == this.valueCount; // should not change
      return valueCount;
    }

    @Override
    public long lookupTerm(BytesRef key) {
      assert key.isValid();
      long result = in.lookupTerm(key);
      assert result < valueCount;
      assert key.isValid();
      return result;
    }
  }

  @Override
  public NumericDocValues getNumericDocValues(String field) throws IOException {
    NumericDocValues dv = super.getNumericDocValues(field);
    FieldInfo fi = getFieldInfos().fieldInfo(field);
    if (dv != null) {
      assert fi != null;
      assert fi.getDocValuesType() == FieldInfo.DocValuesType.NUMERIC;
      return new AssertingNumericDocValues(dv, maxDoc());
    } else {
      assert fi == null || fi.getDocValuesType() != FieldInfo.DocValuesType.NUMERIC;
      return null;
    }
  }

  @Override
  public BinaryDocValues getBinaryDocValues(String field) throws IOException {
    BinaryDocValues dv = super.getBinaryDocValues(field);
    FieldInfo fi = getFieldInfos().fieldInfo(field);
    if (dv != null) {
      assert fi != null;
      assert fi.getDocValuesType() == FieldInfo.DocValuesType.BINARY;
      return new AssertingBinaryDocValues(dv, maxDoc());
    } else {
      assert fi == null || fi.getDocValuesType() != FieldInfo.DocValuesType.BINARY;
      return null;
    }
  }

  @Override
  public SortedDocValues getSortedDocValues(String field) throws IOException {
    SortedDocValues dv = super.getSortedDocValues(field);
    FieldInfo fi = getFieldInfos().fieldInfo(field);
    if (dv != null) {
      assert fi != null;
      assert fi.getDocValuesType() == FieldInfo.DocValuesType.SORTED;
      return new AssertingSortedDocValues(dv, maxDoc());
    } else {
      assert fi == null || fi.getDocValuesType() != FieldInfo.DocValuesType.SORTED;
      return null;
    }
  }

  @Override
  public SortedSetDocValues getSortedSetDocValues(String field) throws IOException {
    SortedSetDocValues dv = super.getSortedSetDocValues(field);
    FieldInfo fi = getFieldInfos().fieldInfo(field);
    if (dv != null) {
      assert fi != null;
      assert fi.getDocValuesType() == FieldInfo.DocValuesType.SORTED_SET;
      return new AssertingSortedSetDocValues(dv, maxDoc());
    } else {
      assert fi == null || fi.getDocValuesType() != FieldInfo.DocValuesType.SORTED_SET;
      return null;
    }
  }

  @Override
  public NumericDocValues getNormValues(String field) throws IOException {
    NumericDocValues dv = super.getNormValues(field);
    FieldInfo fi = getFieldInfos().fieldInfo(field);
    if (dv != null) {
      assert fi != null;
      assert fi.hasNorms();
      return new AssertingNumericDocValues(dv, maxDoc());
    } else {
      assert fi == null || fi.hasNorms() == false;
      return null;
    }
  }

  // this is the same hack as FCInvisible
  @Override
  public Object getCoreCacheKey() {
    return cacheKey;
  }

  @Override
  public Object getCombinedCoreAndDeletesKey() {
    return cacheKey;
  }
  
  private final Object cacheKey = new Object();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2022.java