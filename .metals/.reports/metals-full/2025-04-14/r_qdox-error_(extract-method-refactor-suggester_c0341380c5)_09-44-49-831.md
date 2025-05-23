error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2370.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2370.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2370.java
text:
```scala
o@@penBitSet = new OpenBitSet(this.fcsi.numOrd());

package org.apache.lucene.search;

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

import org.apache.lucene.index.DocsEnum; // javadoc @link
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexReader.AtomicReaderContext;
import org.apache.lucene.util.OpenBitSet;
import org.apache.lucene.util.BytesRef;

/**
 * A {@link Filter} that only accepts documents whose single
 * term value in the specified field is contained in the
 * provided set of allowed terms.
 * 
 * <p/>
 * 
 * This is the same functionality as TermsFilter (from
 * contrib/queries), except this filter requires that the
 * field contains only a single term for all documents.
 * Because of drastically different implementations, they
 * also have different performance characteristics, as
 * described below.
 * 
 * <p/>
 * 
 * The first invocation of this filter on a given field will
 * be slower, since a {@link FieldCache.DocTermsIndex} must be
 * created.  Subsequent invocations using the same field
 * will re-use this cache.  However, as with all
 * functionality based on {@link FieldCache}, persistent RAM
 * is consumed to hold the cache, and is not freed until the
 * {@link IndexReader} is closed.  In contrast, TermsFilter
 * has no persistent RAM consumption.
 * 
 * 
 * <p/>
 * 
 * With each search, this filter translates the specified
 * set of Terms into a private {@link OpenBitSet} keyed by
 * term number per unique {@link IndexReader} (normally one
 * reader per segment).  Then, during matching, the term
 * number for each docID is retrieved from the cache and
 * then checked for inclusion using the {@link OpenBitSet}.
 * Since all testing is done using RAM resident data
 * structures, performance should be very fast, most likely
 * fast enough to not require further caching of the
 * DocIdSet for each possible combination of terms.
 * However, because docIDs are simply scanned linearly, an
 * index with a great many small documents may find this
 * linear scan too costly.
 * 
 * <p/>
 * 
 * In contrast, TermsFilter builds up an {@link OpenBitSet},
 * keyed by docID, every time it's created, by enumerating
 * through all matching docs using {@link DocsEnum} to seek
 * and scan through each term's docID list.  While there is
 * no linear scan of all docIDs, besides the allocation of
 * the underlying array in the {@link OpenBitSet}, this
 * approach requires a number of "disk seeks" in proportion
 * to the number of terms, which can be exceptionally costly
 * when there are cache misses in the OS's IO cache.
 * 
 * <p/>
 * 
 * Generally, this filter will be slower on the first
 * invocation for a given field, but subsequent invocations,
 * even if you change the allowed set of Terms, should be
 * faster than TermsFilter, especially as the number of
 * Terms being matched increases.  If you are matching only
 * a very small number of terms, and those terms in turn
 * match a very small number of documents, TermsFilter may
 * perform faster.
 *
 * <p/>
 *
 * Which filter is best is very application dependent.
 */

public class FieldCacheTermsFilter extends Filter {
  private String field;
  private BytesRef[] terms;

  public FieldCacheTermsFilter(String field, BytesRef... terms) {
    this.field = field;
    this.terms = terms;
  }

  public FieldCacheTermsFilter(String field, String... terms) {
    this.field = field;
    this.terms = new BytesRef[terms.length];
    for (int i = 0; i < terms.length; i++)
      this.terms[i] = new BytesRef(terms[i]);
  }

  public FieldCache getFieldCache() {
    return FieldCache.DEFAULT;
  }

  @Override
  public DocIdSet getDocIdSet(AtomicReaderContext context) throws IOException {
    return new FieldCacheTermsFilterDocIdSet(getFieldCache().getTermsIndex(context.reader, field));
  }

  protected class FieldCacheTermsFilterDocIdSet extends DocIdSet {
    private FieldCache.DocTermsIndex fcsi;

    private OpenBitSet openBitSet;

    public FieldCacheTermsFilterDocIdSet(FieldCache.DocTermsIndex fcsi) {
      this.fcsi = fcsi;
      openBitSet = new OpenBitSet(this.fcsi.size());
      final BytesRef spare = new BytesRef();
      for (int i=0;i<terms.length;i++) {
        int termNumber = this.fcsi.binarySearchLookup(terms[i], spare);
        if (termNumber > 0) {
          openBitSet.fastSet(termNumber);
        }
      }
    }

    @Override
    public DocIdSetIterator iterator() {
      return new FieldCacheTermsFilterDocIdSetIterator();
    }

    /** This DocIdSet implementation is cacheable. */
    @Override
    public boolean isCacheable() {
      return true;
    }

    protected class FieldCacheTermsFilterDocIdSetIterator extends DocIdSetIterator {
      private int doc = -1;

      @Override
      public int docID() {
        return doc;
      }

      @Override
      public int nextDoc() {
        try {
          while (!openBitSet.fastGet(fcsi.getOrd(++doc))) {}
        } catch (ArrayIndexOutOfBoundsException e) {
          doc = NO_MORE_DOCS;
        }
        return doc;
      }

      @Override
      public int advance(int target) {
        try {
          doc = target;
          while (!openBitSet.fastGet(fcsi.getOrd(doc))) {
            doc++;
          }
        } catch (ArrayIndexOutOfBoundsException e) {
          doc = NO_MORE_DOCS;
        }
        return doc;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2370.java