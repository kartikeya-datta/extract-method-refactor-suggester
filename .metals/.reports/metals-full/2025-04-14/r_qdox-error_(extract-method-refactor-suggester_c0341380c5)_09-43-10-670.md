error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/49.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/49.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/49.java
text:
```scala
public abstract N@@umericDocValues getNormValues(String field) throws IOException;

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

import org.apache.lucene.search.SearcherManager; // javadocs
import org.apache.lucene.util.Bits;

/** {@code AtomicReader} is an abstract class, providing an interface for accessing an
 index.  Search of an index is done entirely through this abstract interface,
 so that any subclass which implements it is searchable. IndexReaders implemented
 by this subclass do not consist of several sub-readers,
 they are atomic. They support retrieval of stored fields, doc values, terms,
 and postings.

 <p>For efficiency, in this API documents are often referred to via
 <i>document numbers</i>, non-negative integers which each name a unique
 document in the index.  These document numbers are ephemeral -- they may change
 as documents are added to and deleted from an index.  Clients should thus not
 rely on a given document having the same number between sessions.

 <p>
 <a name="thread-safety"></a><p><b>NOTE</b>: {@link
 IndexReader} instances are completely thread
 safe, meaning multiple threads can call any of its methods,
 concurrently.  If your application requires external
 synchronization, you should <b>not</b> synchronize on the
 <code>IndexReader</code> instance; use your own
 (non-Lucene) objects instead.
*/
public abstract class AtomicReader extends IndexReader {

  private final AtomicReaderContext readerContext = new AtomicReaderContext(this);
  
  /** Sole constructor. (For invocation by subclass 
   *  constructors, typically implicit.) */
  protected AtomicReader() {
    super();
  }

  @Override
  public final AtomicReaderContext getContext() {
    ensureOpen();
    return readerContext;
  }

  /**
   * Returns {@link Fields} for this reader.
   * This method may return null if the reader has no
   * postings.
   */
  public abstract Fields fields() throws IOException;
  
  @Override
  public final int docFreq(Term term) throws IOException {
    final Fields fields = fields();
    if (fields == null) {
      return 0;
    }
    final Terms terms = fields.terms(term.field());
    if (terms == null) {
      return 0;
    }
    final TermsEnum termsEnum = terms.iterator(null);
    if (termsEnum.seekExact(term.bytes(), true)) {
      return termsEnum.docFreq();
    } else {
      return 0;
    }
  }

  /** Returns the number of documents containing the term
   * <code>t</code>.  This method returns 0 if the term or
   * field does not exists.  This method does not take into
   * account deleted documents that have not yet been merged
   * away. */
  @Override
  public final long totalTermFreq(Term term) throws IOException {
    final Fields fields = fields();
    if (fields == null) {
      return 0;
    }
    final Terms terms = fields.terms(term.field());
    if (terms == null) {
      return 0;
    }
    final TermsEnum termsEnum = terms.iterator(null);
    if (termsEnum.seekExact(term.bytes(), true)) {
      return termsEnum.totalTermFreq();
    } else {
      return 0;
    }
  }

  /** This may return null if the field does not exist.*/
  public final Terms terms(String field) throws IOException {
    final Fields fields = fields();
    if (fields == null) {
      return null;
    }
    return fields.terms(field);
  }

  /** Returns {@link DocsEnum} for the specified term.
   *  This will return null if either the field or
   *  term does not exist. 
   *  @see TermsEnum#docs(Bits, DocsEnum) */
  public final DocsEnum termDocsEnum(Term term) throws IOException {
    assert term.field() != null;
    assert term.bytes() != null;
    final Fields fields = fields();
    if (fields != null) {
      final Terms terms = fields.terms(term.field());
      if (terms != null) {
        final TermsEnum termsEnum = terms.iterator(null);
        if (termsEnum.seekExact(term.bytes(), true)) {
          return termsEnum.docs(getLiveDocs(), null);
        }
      }
    }
    return null;
  }

  /** Returns {@link DocsAndPositionsEnum} for the specified
   *  term.  This will return null if the
   *  field or term does not exist or positions weren't indexed. 
   *  @see TermsEnum#docsAndPositions(Bits, DocsAndPositionsEnum) */
  public final DocsAndPositionsEnum termPositionsEnum(Term term) throws IOException {
    assert term.field() != null;
    assert term.bytes() != null;
    final Fields fields = fields();
    if (fields != null) {
      final Terms terms = fields.terms(term.field());
      if (terms != null) {
        final TermsEnum termsEnum = terms.iterator(null);
        if (termsEnum.seekExact(term.bytes(), true)) {
          return termsEnum.docsAndPositions(getLiveDocs(), null);
        }
      }
    }
    return null;
  }

  /** Returns {@link NumericDocValues} for this field, or
   *  null if no {@link NumericDocValues} were indexed for
   *  this field.  The returned instance should only be
   *  used by a single thread. */
  public abstract NumericDocValues getNumericDocValues(String field) throws IOException;

  /** Returns {@link BinaryDocValues} for this field, or
   *  null if no {@link BinaryDocValues} were indexed for
   *  this field.  The returned instance should only be
   *  used by a single thread. */
  public abstract BinaryDocValues getBinaryDocValues(String field) throws IOException;

  /** Returns {@link SortedDocValues} for this field, or
   *  null if no {@link SortedDocValues} were indexed for
   *  this field.  The returned instance should only be
   *  used by a single thread. */
  public abstract SortedDocValues getSortedDocValues(String field) throws IOException;

  // nocommit document that these are thread-private:
  /** Returns {@link NumericDocValues} representing norms
   *  for this field, or null if no {@link NumericDocValues}
   *  were indexed. */
  public abstract NumericDocValues simpleNormValues(String field) throws IOException;

  /**
   * Get the {@link FieldInfos} describing all fields in
   * this reader.
   * @lucene.experimental
   */
  public abstract FieldInfos getFieldInfos();
  
  /** Returns the {@link Bits} representing live (not
   *  deleted) docs.  A set bit indicates the doc ID has not
   *  been deleted.  If this method returns null it means
   *  there are no deleted documents (all documents are
   *  live).
   *
   *  The returned instance has been safely published for
   *  use by multiple threads without additional
   *  synchronization.
   */
  public abstract Bits getLiveDocs();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/49.java