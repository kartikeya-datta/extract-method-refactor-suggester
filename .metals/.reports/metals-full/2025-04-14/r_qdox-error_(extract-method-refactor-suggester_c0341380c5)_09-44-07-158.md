error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7417.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7417.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7417.java
text:
```scala
private O@@bjectObjectMap<BytesRef, CacheEntry> cache = new ObjectObjectOpenHashMap<>();

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.search.suggest.phrase;

import com.carrotsearch.hppc.ObjectObjectMap;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import org.apache.lucene.index.*;
import org.apache.lucene.index.FilterAtomicReader.FilterTermsEnum;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.ElasticsearchIllegalArgumentException;
import org.elasticsearch.search.suggest.phrase.DirectCandidateGenerator.Candidate;
import org.elasticsearch.search.suggest.phrase.DirectCandidateGenerator.CandidateSet;

import java.io.IOException;

//TODO public for tests
public abstract class WordScorer {
    protected final IndexReader reader;
    protected final String field;
    protected final Terms terms;
    protected final long vocabluarySize;
    protected final double realWordLikelyhood;
    protected final BytesRef spare = new BytesRef();
    protected final BytesRef separator;
    protected final TermsEnum termsEnum;
    private final long numTerms;
    private final boolean useTotalTermFreq;

    public WordScorer(IndexReader reader, String field, double realWordLikelyHood, BytesRef separator) throws IOException {
        this(reader, MultiFields.getTerms(reader, field), field, realWordLikelyHood, separator);
    }

    public WordScorer(IndexReader reader, Terms terms, String field, double realWordLikelyHood, BytesRef separator) throws IOException {
        this.field = field;
        if (terms == null) {
            throw new ElasticsearchIllegalArgumentException("Field: [" + field + "] does not exist");
        }
        this.terms = terms;
        final long vocSize = terms.getSumTotalTermFreq();
        this.vocabluarySize =  vocSize == -1 ? reader.maxDoc() : vocSize;
        this.useTotalTermFreq = vocSize != -1;
        this.numTerms = terms.size();
        this.termsEnum = new FrequencyCachingTermsEnumWrapper(terms.iterator(null));
        this.reader = reader;
        this.realWordLikelyhood = realWordLikelyHood;
        this.separator = separator;
    }

    public long frequency(BytesRef term) throws IOException {
        if (termsEnum.seekExact(term)) {
            return useTotalTermFreq ? termsEnum.totalTermFreq() : termsEnum.docFreq();
        }
        return 0;
   }

   protected double channelScore(Candidate candidate, Candidate original) throws IOException {
       if (candidate.stringDistance == 1.0d) {
           return realWordLikelyhood;
       }
       return candidate.stringDistance;
   }
   
   public double score(Candidate[] path, CandidateSet[] candidateSet, int at, int gramSize) throws IOException {
       if (at == 0 || gramSize == 1) {
           return Math.log10(channelScore(path[at], candidateSet[at].originalTerm) * scoreUnigram(path[at]));
       } else if (at == 1 || gramSize == 2) {
           return Math.log10(channelScore(path[at], candidateSet[at].originalTerm) * scoreBigram(path[at], path[at - 1]));
       } else {
           return Math.log10(channelScore(path[at], candidateSet[at].originalTerm) * scoreTrigram(path[at], path[at - 1], path[at - 2]));
       }
   }
   
   protected double scoreUnigram(Candidate word)  throws IOException {
       return (1.0 + frequency(word.term)) / (vocabluarySize + numTerms);
   }
   
   protected double scoreBigram(Candidate word, Candidate w_1) throws IOException {
       return scoreUnigram(word);
   }
   
   protected double scoreTrigram(Candidate word, Candidate w_1, Candidate w_2) throws IOException {
       return scoreBigram(word, w_1);
   }

   public static interface WordScorerFactory {
       public WordScorer newScorer(IndexReader reader, Terms terms,
            String field, double realWordLikelyhood, BytesRef separator) throws IOException;
   }

   /**
    * Terms enum wrapper that caches term frequencies in an effort to outright skip seeks.  Only works with seekExact(BytesRef), not next or
    * not seekCeil.  Because of this it really only makes sense in this context.
    */
   private static class FrequencyCachingTermsEnumWrapper extends FilterTermsEnum {
       private ObjectObjectMap<BytesRef, CacheEntry> cache = new ObjectObjectOpenHashMap<BytesRef, CacheEntry>();
       /**
        * The last term that the called attempted to seek to.
        */
       private CacheEntry last;

       public FrequencyCachingTermsEnumWrapper(TermsEnum in) {
           super(in);
       }

       @Override
       public boolean seekExact(BytesRef text) throws IOException {
           last = cache.get(text);
           if (last != null) {
               // This'll fail to work properly if the user seeks but doesn't check the frequency, causing us to cache it.
               // That is OK because WordScorer only seeks to check the frequency.
               return last.ttf != 0 || last.df != 0;
           }
           last = new CacheEntry();
           cache.put(BytesRef.deepCopyOf(text), last);
           if (in.seekExact(text)) {
               // Found so mark the term uncached.
               last.df = -1;
               last.ttf = -1;
               return true;
           }
           // Not found.  The cache will default to 0 for the freqs, meaning not found.
           return false;
       }

       @Override
       public long totalTermFreq() throws IOException {
           if (last.ttf == -1) {
               last.ttf = in.totalTermFreq();
           }
           return last.ttf;
       }

       @Override
       public int docFreq() throws IOException {
           if (last.df == -1) {
               last.df = in.docFreq();
           }
           return last.df;
       }

       @Override
       public void seekExact(long ord) throws IOException {
           throw new UnsupportedOperationException();
       }

       @Override
       public DocsEnum docs(Bits liveDocs, DocsEnum reuse, int flags) throws IOException {
           throw new UnsupportedOperationException();
       }

       @Override
       public DocsAndPositionsEnum docsAndPositions(Bits liveDocs, DocsAndPositionsEnum reuse, int flags) throws IOException {
           throw new UnsupportedOperationException();
       }

       public SeekStatus seekCeil(BytesRef text) throws IOException {
           throw new UnsupportedOperationException();
       }

       @Override
       public BytesRef next() {
           throw new UnsupportedOperationException();
       }

       private static class CacheEntry {
           private long ttf;
           private int df;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7417.java