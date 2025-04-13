error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2690.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2690.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2690.java
text:
```scala
public l@@ong size() throws IOException {

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
import java.util.Comparator;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.OpenBitSet;

/**
 * Rewrites MultiTermQueries into a filter, using the FieldCache for term enumeration.
 * <p>
 * WARNING: This is only appropriate for single-valued unanalyzed fields. Additionally, for 
 * most queries this method is actually SLOWER than using the default CONSTANT_SCORE_AUTO 
 * in MultiTermQuery. This method is only faster than other methods for certain queries,
 * such as ones that enumerate many terms.
 * 
 * @lucene.experimental
 */
public final class FieldCacheRewriteMethod extends MultiTermQuery.RewriteMethod {
  
  @Override
  public Query rewrite(IndexReader reader, MultiTermQuery query) {
    Query result = new ConstantScoreQuery(new MultiTermQueryFieldCacheWrapperFilter(query));
    result.setBoost(query.getBoost());
    return result;
  }
  
  static class MultiTermQueryFieldCacheWrapperFilter extends Filter {
    
    protected final MultiTermQuery query;
    
    /**
     * Wrap a {@link MultiTermQuery} as a Filter.
     */
    protected MultiTermQueryFieldCacheWrapperFilter(MultiTermQuery query) {
      this.query = query;
    }
    
    @Override
    public String toString() {
      // query.toString should be ok for the filter, too, if the query boost is 1.0f
      return query.toString();
    }
    
    @Override
    public final boolean equals(final Object o) {
      if (o==this) return true;
      if (o==null) return false;
      if (this.getClass().equals(o.getClass())) {
        return this.query.equals( ((MultiTermQueryFieldCacheWrapperFilter)o).query );
      }
      return false;
    }
    
    @Override
    public final int hashCode() {
      return query.hashCode();
    }
    
    /** Returns the field name for this query */
    public final String getField() { return query.getField(); }
    
    /**
     * Returns a DocIdSet with documents that should be permitted in search
     * results.
     */
    @Override
    public DocIdSet getDocIdSet(AtomicReaderContext context, final Bits acceptDocs) throws IOException {
      final FieldCache.DocTermsIndex fcsi = FieldCache.DEFAULT.getTermsIndex(context.reader(), query.field);
      // Cannot use FixedBitSet because we require long index (ord):
      final OpenBitSet termSet = new OpenBitSet(fcsi.numOrd());
      TermsEnum termsEnum = query.getTermsEnum(new Terms() {
        
        @Override
        public Comparator<BytesRef> getComparator() throws IOException {
          return BytesRef.getUTF8SortedAsUnicodeComparator();
        }
        
        @Override
        public TermsEnum iterator(TermsEnum reuse) throws IOException {
          return fcsi.getTermsEnum();
        }

        @Override
        public long getSumTotalTermFreq() {
          return -1;
        }

        @Override
        public long getSumDocFreq() throws IOException {
          return -1;
        }

        @Override
        public int getDocCount() throws IOException {
          return -1;
        }

        @Override
        public long getUniqueTermCount() throws IOException {
          return -1;
        }
      });
      
      assert termsEnum != null;
      if (termsEnum.next() != null) {
        // fill into a OpenBitSet
        do {
          long ord = termsEnum.ord();
          if (ord > 0) {
            termSet.set(ord);
          }
        } while (termsEnum.next() != null);
      } else {
        return DocIdSet.EMPTY_DOCIDSET;
      }
      
      return new FieldCacheDocIdSet(context.reader().maxDoc(), acceptDocs) {
        @Override
        protected final boolean matchDoc(int doc) throws ArrayIndexOutOfBoundsException {
          return termSet.get(fcsi.getOrd(doc));
        }
      };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2690.java