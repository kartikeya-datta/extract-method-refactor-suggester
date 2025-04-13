error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9463.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9463.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9463.java
text:
```scala
r@@eturn Math.log10(channelScore(path[at], candidateSet[at].originalTerm) * scoreUnigram(path[at]));

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.search.suggest.phrase.DirectCandidateGenerator.Candidate;
import org.elasticsearch.search.suggest.phrase.DirectCandidateGenerator.CandidateSet;

//TODO public for tests
public abstract class WordScorer {
    protected final IndexReader reader;
    protected final String field;
    protected final Terms terms;
    protected final long vocabluarySize;
    protected double realWordLikelyhood;
    protected final BytesRef spare = new BytesRef();
    protected final BytesRef separator;
    protected final TermsEnum termsEnum;
    private final long numTerms;
    private final boolean useTotalTermFreq;
    
    public WordScorer(IndexReader reader, String field, double realWordLikelyHood, BytesRef separator) throws IOException {
        this.field = field;
        this.terms = MultiFields.getTerms(reader, field);
        if (terms == null) {
            throw new ElasticSearchIllegalArgumentException("Field: [" + field + "] does not exist");
        }
        final long vocSize = terms.getSumTotalTermFreq();
        this.vocabluarySize =  vocSize == -1 ? reader.maxDoc() : vocSize;
        this.useTotalTermFreq = vocSize != -1;
        this.numTerms = terms.size();
        this.termsEnum = terms.iterator(null);
        this.reader = reader;
        this.realWordLikelyhood = realWordLikelyHood;
        this.separator = separator;
   }
    
   public long frequency(BytesRef term) throws IOException {
      if (termsEnum.seekExact(term, true)) {
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
           return Math.log10(channelScore(path[0], candidateSet[0].originalTerm) * scoreUnigram(path[0]));
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
   
   protected double scoreTrigram(Candidate word, Candidate w_1, Candidate w_2)  throws IOException {
       return scoreBigram(word, w_1);
   }
   
   public static interface WordScorerFactory {
       public WordScorer newScorer(IndexReader reader, String field,
            double realWordLikelyhood, BytesRef separator) throws IOException;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9463.java