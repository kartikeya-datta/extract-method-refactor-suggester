error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7414.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7414.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7414.java
text:
```scala
final L@@ist<CandidateSet> candidateSetsList = new ArrayList<>();

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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.UnicodeUtil;
import org.elasticsearch.common.io.FastCharArrayReader;
import org.elasticsearch.search.suggest.SuggestUtils;
import org.elasticsearch.search.suggest.phrase.DirectCandidateGenerator.Candidate;
import org.elasticsearch.search.suggest.phrase.DirectCandidateGenerator.CandidateSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TODO public for tests
public final class NoisyChannelSpellChecker {
    public static final double REAL_WORD_LIKELYHOOD = 0.95d;
    public static final int DEFAULT_TOKEN_LIMIT = 10;
    private final double realWordLikelihood;
    private final boolean requireUnigram;
    private final int tokenLimit;

    public NoisyChannelSpellChecker() {
        this(REAL_WORD_LIKELYHOOD);
    }

    public NoisyChannelSpellChecker(double nonErrorLikelihood) {
        this(nonErrorLikelihood, true, DEFAULT_TOKEN_LIMIT);
    }
    
    public NoisyChannelSpellChecker(double nonErrorLikelihood, boolean requireUnigram, int tokenLimit) {
        this.realWordLikelihood = nonErrorLikelihood;
        this.requireUnigram = requireUnigram;
        this.tokenLimit = tokenLimit;
                
    }

    public Result getCorrections(TokenStream stream, final CandidateGenerator generator,
            float maxErrors, int numCorrections, IndexReader reader, WordScorer wordScorer, BytesRef separator, float confidence, int gramSize) throws IOException {
        
        final List<CandidateSet> candidateSetsList = new ArrayList<DirectCandidateGenerator.CandidateSet>();
        SuggestUtils.analyze(stream, new SuggestUtils.TokenConsumer() {
            CandidateSet currentSet = null;
            private TypeAttribute typeAttribute;
            private final BytesRef termsRef = new BytesRef();
            private boolean anyUnigram = false;
            private boolean anyTokens = false;
            @Override
            public void reset(TokenStream stream) {
                super.reset(stream);
                typeAttribute = stream.addAttribute(TypeAttribute.class);
            }
            
            @Override
            public void nextToken() throws IOException {
                anyTokens = true;
                BytesRef term = fillBytesRef(termsRef);
                if (requireUnigram && typeAttribute.type() == ShingleFilter.DEFAULT_TOKEN_TYPE) {
                    return;
                }
                anyUnigram = true;
                if (posIncAttr.getPositionIncrement() == 0 && typeAttribute.type() == SynonymFilter.TYPE_SYNONYM) {
                    assert currentSet != null;
                    long freq = 0;
                    if ((freq = generator.frequency(term)) > 0) {
                        currentSet.addOneCandidate(generator.createCandidate(BytesRef.deepCopyOf(term), freq, realWordLikelihood));
                    }
                } else {
                    if (currentSet != null) {
                        candidateSetsList.add(currentSet);
                    }
                    currentSet = new CandidateSet(Candidate.EMPTY, generator.createCandidate(BytesRef.deepCopyOf(term), true));
                }
            }
            
            @Override
            public void end() {
                if (currentSet != null) {
                    candidateSetsList.add(currentSet);
                }
                if (requireUnigram && !anyUnigram && anyTokens) {
                    throw new IllegalStateException("At least one unigram is required but all tokens were ngrams");
                }
            }
        });
        
        if (candidateSetsList.isEmpty() || candidateSetsList.size() >= tokenLimit) {
            return Result.EMPTY;
        }
        
        for (CandidateSet candidateSet : candidateSetsList) {
            generator.drawCandidates(candidateSet);
        }
        double cutoffScore = Double.MIN_VALUE;
        CandidateScorer scorer = new CandidateScorer(wordScorer, numCorrections, gramSize);
        CandidateSet[] candidateSets = candidateSetsList.toArray(new CandidateSet[candidateSetsList.size()]);
        if (confidence > 0.0) {
            Candidate[] candidates = new Candidate[candidateSets.length];
            for (int i = 0; i < candidates.length; i++) {
                candidates[i] = candidateSets[i].originalTerm;
            }
            double inputPhraseScore = scorer.score(candidates, candidateSets);
            cutoffScore = inputPhraseScore * confidence;
        }
        Correction[] findBestCandiates = scorer.findBestCandiates(candidateSets, maxErrors, cutoffScore);
        
        return new Result(findBestCandiates, cutoffScore);
    }

    public Result getCorrections(Analyzer analyzer, BytesRef query, CandidateGenerator generator,
            float maxErrors, int numCorrections, IndexReader reader, String analysisField, WordScorer scorer, float confidence, int gramSize) throws IOException {
       
        return getCorrections(tokenStream(analyzer, query, new CharsRef(), analysisField), generator, maxErrors, numCorrections, reader, scorer, new BytesRef(" "), confidence, gramSize);

    }

    public TokenStream tokenStream(Analyzer analyzer, BytesRef query, CharsRef spare, String field) throws IOException {
        UnicodeUtil.UTF8toUTF16(query, spare);
        return analyzer.tokenStream(field, new FastCharArrayReader(spare.chars, spare.offset, spare.length));
    }

    public static class Result {
        public static final Result EMPTY = new Result(Correction.EMPTY, Double.MIN_VALUE);
        public final Correction[] corrections;
        public final double cutoffScore;

        public Result(Correction[] corrections, double cutoffScore) {
            this.corrections = corrections;
            this.cutoffScore = cutoffScore;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7414.java