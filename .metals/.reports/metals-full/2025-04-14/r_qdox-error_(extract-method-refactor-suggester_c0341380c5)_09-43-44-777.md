error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1390.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1390.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1390.java
text:
```scala
F@@irst, Avg, Max, Sum, Min, Multiply

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

package org.elasticsearch.common.lucene.search.function;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.ToStringUtils;
import org.elasticsearch.common.lucene.docset.DocIdSets;

import java.io.IOException;
import java.util.*;

/**
 * A query that allows for a pluggable boost function / filter. If it matches
 * the filter, it will be boosted by the formula.
 */
public class FiltersFunctionScoreQuery extends Query {

    public static class FilterFunction {
        public final Filter filter;
        public final ScoreFunction function;

        public FilterFunction(Filter filter, ScoreFunction function) {
            this.filter = filter;
            this.function = function;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            FilterFunction that = (FilterFunction) o;

            if (filter != null ? !filter.equals(that.filter) : that.filter != null)
                return false;
            if (function != null ? !function.equals(that.function) : that.function != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = filter != null ? filter.hashCode() : 0;
            result = 31 * result + (function != null ? function.hashCode() : 0);
            return result;
        }
    }

    public static enum ScoreMode {
        First, Avg, Max, Total, Min, Multiply
    }

    Query subQuery;
    final FilterFunction[] filterFunctions;
    final ScoreMode scoreMode;
    final float maxBoost;

    protected CombineFunction combineFunction;

    public FiltersFunctionScoreQuery(Query subQuery, ScoreMode scoreMode, FilterFunction[] filterFunctions, float maxBoost) {
        this.subQuery = subQuery;
        this.scoreMode = scoreMode;
        this.filterFunctions = filterFunctions;
        this.maxBoost = maxBoost;
        combineFunction = CombineFunction.MULT;
    }

    public FiltersFunctionScoreQuery setCombineFunction(CombineFunction combineFunction){
        this.combineFunction = combineFunction;
        return this;
    }
    public Query getSubQuery() {
        return subQuery;
    }

    public FilterFunction[] getFilterFunctions() {
        return filterFunctions;
    }

    @Override
    public Query rewrite(IndexReader reader) throws IOException {
        Query newQ = subQuery.rewrite(reader);
        if (newQ == subQuery)
            return this;
        FiltersFunctionScoreQuery bq = (FiltersFunctionScoreQuery) this.clone();
        bq.subQuery = newQ;
        return bq;
    }

    @Override
    public void extractTerms(Set<Term> terms) {
        subQuery.extractTerms(terms);
    }

    @Override
    public Weight createWeight(IndexSearcher searcher) throws IOException {
        Weight subQueryWeight = subQuery.createWeight(searcher);
        return new CustomBoostFactorWeight(subQueryWeight, filterFunctions.length);
    }

    class CustomBoostFactorWeight extends Weight {

        final Weight subQueryWeight;
        final Bits[] docSets;

        public CustomBoostFactorWeight(Weight subQueryWeight, int filterFunctionLength) throws IOException {
            this.subQueryWeight = subQueryWeight;
            this.docSets = new Bits[filterFunctionLength];
        }

        public Query getQuery() {
            return FiltersFunctionScoreQuery.this;
        }

        @Override
        public float getValueForNormalization() throws IOException {
            float sum = subQueryWeight.getValueForNormalization();
            sum *= getBoost() * getBoost();
            return sum;
        }

        @Override
        public void normalize(float norm, float topLevelBoost) {
            subQueryWeight.normalize(norm, topLevelBoost * getBoost());
        }

        @Override
        public Scorer scorer(AtomicReaderContext context, boolean scoreDocsInOrder, boolean topScorer, Bits acceptDocs) throws IOException {
            Scorer subQueryScorer = subQueryWeight.scorer(context, scoreDocsInOrder, false, acceptDocs);
            if (subQueryScorer == null) {
                return null;
            }
            for (int i = 0; i < filterFunctions.length; i++) {
                FilterFunction filterFunction = filterFunctions[i];
                filterFunction.function.setNextReader(context);
                docSets[i] = DocIdSets.toSafeBits(context.reader(), filterFunction.filter.getDocIdSet(context, acceptDocs));
            }
            return new CustomBoostFactorScorer(this, subQueryScorer, scoreMode, filterFunctions, maxBoost, docSets, combineFunction);
        }

        @Override
        public Explanation explain(AtomicReaderContext context, int doc) throws IOException {

            Explanation subQueryExpl = subQueryWeight.explain(context, doc);
            if (!subQueryExpl.isMatch()) {
                return subQueryExpl;
            }
            // First: Gather explanations for all filters
            List<ComplexExplanation> filterExplanations = new ArrayList<ComplexExplanation>();
            for (FilterFunction filterFunction : filterFunctions) {
                Bits docSet = DocIdSets.toSafeBits(context.reader(),
                        filterFunction.filter.getDocIdSet(context, context.reader().getLiveDocs()));
                if (docSet.get(doc)) {
                    filterFunction.function.setNextReader(context);
                    Explanation functionExplanation = filterFunction.function.explainScore(doc, subQueryExpl);
                    double factor = functionExplanation.getValue();
                    float sc = CombineFunction.toFloat(factor);
                    ComplexExplanation filterExplanation = new ComplexExplanation(true, sc, "function score, product of:");
                    filterExplanation.addDetail(new Explanation(1.0f, "match filter: " + filterFunction.filter.toString()));
                    filterExplanation.addDetail(functionExplanation);
                    filterExplanations.add(filterExplanation);
                }
            }
            if (filterExplanations.size() == 0) {
                float sc = getBoost() * subQueryExpl.getValue();
                Explanation res = new ComplexExplanation(true, sc, "function score, no filter match, product of:");
                res.addDetail(subQueryExpl);
                res.addDetail(new Explanation(getBoost(), "queryBoost"));
                return res;
            }

            // Second: Compute the factor that would have been computed by the
            // filters
            double factor = 1.0;
            switch (scoreMode) {
            case First:

                factor = filterExplanations.get(0).getValue();
                break;
            case Max:
                double maxFactor = Double.NEGATIVE_INFINITY;
                for (int i = 0; i < filterExplanations.size(); i++) {
                    factor = Math.max(filterExplanations.get(i).getValue(), maxFactor);
                }
                break;
            case Min:
                double minFactor = Double.POSITIVE_INFINITY;
                for (int i = 0; i < filterExplanations.size(); i++) {
                    factor = Math.min(filterExplanations.get(i).getValue(), minFactor);
                }
                break;
            case Multiply:
                for (int i = 0; i < filterExplanations.size(); i++) {
                    factor *= filterExplanations.get(i).getValue();
                }
                break;
            default: // Avg / Total
                double totalFactor = 0.0f;
                int count = 0;
                for (int i = 0; i < filterExplanations.size(); i++) {
                    totalFactor += filterExplanations.get(i).getValue();
                    count++;
                }
                if (count != 0) {
                    factor = totalFactor;
                    if (scoreMode == ScoreMode.Avg) {
                        factor /= count;
                    }
                }
            }
            ComplexExplanation factorExplanaition = new ComplexExplanation(true, CombineFunction.toFloat(factor),
                    "function score, score mode [" + scoreMode.toString().toLowerCase(Locale.ROOT) + "]");
            for (int i = 0; i < filterExplanations.size(); i++) {
                factorExplanaition.addDetail(filterExplanations.get(i));
            }
            return combineFunction.explain(getBoost(), subQueryExpl, factorExplanaition, maxBoost);
        }
    }

    static class CustomBoostFactorScorer extends Scorer {

        private final float subQueryBoost;
        private final Scorer scorer;
        private final FilterFunction[] filterFunctions;
        private final ScoreMode scoreMode;
        private final float maxBoost;
        private final Bits[] docSets;
        private final CombineFunction scoreCombiner;

        private CustomBoostFactorScorer(CustomBoostFactorWeight w, Scorer scorer, ScoreMode scoreMode, FilterFunction[] filterFunctions,
                float maxBoost, Bits[] docSets, CombineFunction scoreCombiner) throws IOException {
            super(w);
            this.subQueryBoost = w.getQuery().getBoost();
            this.scorer = scorer;
            this.scoreMode = scoreMode;
            this.filterFunctions = filterFunctions;
            this.maxBoost = maxBoost;
            this.docSets = docSets;
            this.scoreCombiner = scoreCombiner;
        }

        @Override
        public int docID() {
            return scorer.docID();
        }

        @Override
        public int advance(int target) throws IOException {
            return scorer.advance(target);
        }

        @Override
        public int nextDoc() throws IOException {
            return scorer.nextDoc();
        }

        @Override
        public float score() throws IOException {
            int docId = scorer.docID();
            double factor = 1.0f;
            float subQueryScore = scorer.score();
            if (scoreMode == ScoreMode.First) {
                for (int i = 0; i < filterFunctions.length; i++) {
                    if (docSets[i].get(docId)) {
                        factor = filterFunctions[i].function.score(docId, subQueryScore);
                        break;
                    }
                }
            } else if (scoreMode == ScoreMode.Max) {
                double maxFactor = Double.NEGATIVE_INFINITY;
                for (int i = 0; i < filterFunctions.length; i++) {
                    if (docSets[i].get(docId)) {
                        maxFactor = Math.max(filterFunctions[i].function.score(docId, subQueryScore), maxFactor);
                    }
                }
                if (maxFactor != Float.NEGATIVE_INFINITY) {
                    factor = maxFactor;
                }
            } else if (scoreMode == ScoreMode.Min) {
                double minFactor = Double.POSITIVE_INFINITY;
                for (int i = 0; i < filterFunctions.length; i++) {
                    if (docSets[i].get(docId)) {
                        minFactor = Math.min(filterFunctions[i].function.score(docId, subQueryScore), minFactor);
                    }
                }
                if (minFactor != Float.POSITIVE_INFINITY) {
                    factor = minFactor;
                }
            } else if (scoreMode == ScoreMode.Multiply) {
                for (int i = 0; i < filterFunctions.length; i++) {
                    if (docSets[i].get(docId)) {
                        factor *= filterFunctions[i].function.score(docId, subQueryScore);
                    }
                }
            } else { // Avg / Total
                double totalFactor = 0.0f;
                int count = 0;
                for (int i = 0; i < filterFunctions.length; i++) {
                    if (docSets[i].get(docId)) {
                        totalFactor += filterFunctions[i].function.score(docId, subQueryScore);
                        count++;
                    }
                }
                if (count != 0) {
                    factor = totalFactor;
                    if (scoreMode == ScoreMode.Avg) {
                        factor /= count;
                    }
                }
            }
            return scoreCombiner.combine(subQueryBoost, subQueryScore, factor, maxBoost);
        }

        @Override
        public int freq() throws IOException {
            return scorer.freq();
        }

        @Override
        public long cost() {
            return scorer.cost();
        }
    }

    public String toString(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("function score (").append(subQuery.toString(field)).append(", functions: [");
        for (FilterFunction filterFunction : filterFunctions) {
            sb.append("{filter(").append(filterFunction.filter).append("), function [").append(filterFunction.function).append("]}");
        }
        sb.append("])");
        sb.append(ToStringUtils.boost(getBoost()));
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (getClass() != o.getClass())
            return false;
        FiltersFunctionScoreQuery other = (FiltersFunctionScoreQuery) o;
        if (this.getBoost() != other.getBoost())
            return false;
        if (!this.subQuery.equals(other.subQuery)) {
            return false;
        }
        return Arrays.equals(this.filterFunctions, other.filterFunctions);
    }

    public int hashCode() {
        return subQuery.hashCode() + 31 * Arrays.hashCode(filterFunctions) ^ Float.floatToIntBits(getBoost());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1390.java