error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6682.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6682.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6682.java
text:
```scala
s@@uggestion.setModel(StupidBackoffScorer.FACTORY);

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

/*
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
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentParser.Token;
import org.elasticsearch.index.analysis.ShingleTokenFilterFactory;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.search.suggest.SuggestContextParser;
import org.elasticsearch.search.suggest.SuggestUtils;
import org.elasticsearch.search.suggest.SuggestionSearchContext;
import org.elasticsearch.search.suggest.phrase.PhraseSuggestionContext.DirectCandidateGenerator;

public final class PhraseSuggestParser implements SuggestContextParser {

    private final PhraseSuggester suggester = new PhraseSuggester();

    public SuggestionSearchContext.SuggestionContext parse(XContentParser parser, MapperService mapperService) throws IOException {
        PhraseSuggestionContext suggestion = new PhraseSuggestionContext(suggester);
        XContentParser.Token token;
        String fieldName = null;
        boolean gramSizeSet = false; 
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                fieldName = parser.currentName();
            } else if (token.isValue()) {
                if (!SuggestUtils.parseSuggestContext(parser, mapperService, fieldName, suggestion)) {
                    if ("real_word_error_likelihood".equals(fieldName)) {
                        suggestion.setRealWordErrorLikelihood(parser.floatValue());
                        if (suggestion.realworldErrorLikelyhood() <= 0.0) {
                            throw new ElasticSearchIllegalArgumentException("real_word_error_likelihood must be > 0.0");
                        }
                    } else if ("confidence".equals(fieldName)) {
                        suggestion.setConfidence(parser.floatValue());
                        if (suggestion.confidence() < 0.0) {
                            throw new ElasticSearchIllegalArgumentException("confidence must be >= 0.0");
                        }
                    } else if ("separator".equals(fieldName)) {
                        suggestion.setSeparator(new BytesRef(parser.text()));
                    } else if ("max_errors".equals(fieldName)) {
                        suggestion.setMaxErrors(parser.floatValue());
                        if (suggestion.maxErrors() <= 0.0) {
                            throw new ElasticSearchIllegalArgumentException("max_error must be > 0.0");
                        }
                    } else if ("gram_size".equals(fieldName)) {
                        suggestion.setGramSize(parser.intValue());
                        if (suggestion.gramSize() < 1) {
                            throw new ElasticSearchIllegalArgumentException("gram_size must be >= 1");
                        }
                        gramSizeSet = true;
                    } else if ("force_unigrams".equals(fieldName)) {
                        suggestion.setRequireUnigram(parser.booleanValue());
                    } else {
                        throw new ElasticSearchIllegalArgumentException("suggester[phrase] doesn't support field [" + fieldName + "]");
                    }
                }
            } else if (token == Token.START_ARRAY) {
                if ("direct_generator".equals(fieldName)) {
                    // for now we only have a single type of generators
                    while ((token = parser.nextToken()) == Token.START_OBJECT) {
                        PhraseSuggestionContext.DirectCandidateGenerator generator = new PhraseSuggestionContext.DirectCandidateGenerator();
                        while ((token = parser.nextToken()) != Token.END_OBJECT) {
                            if (token == XContentParser.Token.FIELD_NAME) {
                                fieldName = parser.currentName();
                            }
                            if (token.isValue()) {
                                parseCandidateGenerator(parser, mapperService, fieldName, generator);
                            }
                        }
                        verifyGenerator(generator);
                        suggestion.addGenerator(generator);
                    }
                } else {
                    throw new ElasticSearchIllegalArgumentException("suggester[phrase]  doesn't support array field [" + fieldName + "]");
                }
            } else if (token == Token.START_OBJECT && "smoothing".equals(fieldName)) {
                parseSmoothingModel(parser, suggestion, fieldName);
            } else {
                throw new ElasticSearchIllegalArgumentException("suggester[phrase] doesn't support field [" + fieldName + "]");
            }
        }
        
        if (suggestion.getField() == null) {
            throw new ElasticSearchIllegalArgumentException("The required field option is missing");
        }
        
        if (suggestion.model() == null) {
            suggestion.setModel(LaplaceScorer.FACTORY);
        }
        
        if (!gramSizeSet || suggestion.generators().isEmpty()) {
            final ShingleTokenFilterFactory.Factory shingleFilterFactory = SuggestUtils.getShingleFilterFactory(suggestion.getAnalyzer() == null ? mapperService.fieldSearchAnalyzer(suggestion.getField()) : suggestion.getAnalyzer()); ;
            if (!gramSizeSet) {
                // try to detect the shingle size
                if (shingleFilterFactory != null) {
                    suggestion.setGramSize(shingleFilterFactory.getMaxShingleSize());
                    if (suggestion.getAnalyzer() == null && shingleFilterFactory.getMinShingleSize() > 1 && !shingleFilterFactory.getOutputUnigrams()) {
                        throw new ElasticSearchIllegalArgumentException("The default analyzer for field: [" + suggestion.getField() + "] doesn't emit unigrams. If this is intentional try to set the analyzer explicitly");
                    }
                }
            }
            if (suggestion.generators().isEmpty()) {
                if (shingleFilterFactory != null && shingleFilterFactory.getMinShingleSize() > 1 && !shingleFilterFactory.getOutputUnigrams() && suggestion.getRequireUnigram()) {
                    throw new ElasticSearchIllegalArgumentException("The default candidate generator for phrase suggest can't operate on field: [" + suggestion.getField() + "] since it doesn't emit unigrams. If this is intentional try to set the candidate generator field explicitly");
                }
                // use a default generator on the same field
                DirectCandidateGenerator generator = new DirectCandidateGenerator();
                generator.setField(suggestion.getField());
                suggestion.addGenerator(generator);
            }
        }
        
        
        
        return suggestion;
    }

    public void parseSmoothingModel(XContentParser parser, PhraseSuggestionContext suggestion, String fieldName) throws IOException {
        XContentParser.Token token;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                fieldName = parser.currentName();
                if ("linear".equals(fieldName)) {
                    ensureNoSmoothing(suggestion);
                    final double[] lambdas = new double[3];
                    while ((token = parser.nextToken()) != Token.END_OBJECT) {
                        if (token == XContentParser.Token.FIELD_NAME) {
                            fieldName = parser.currentName();
                        }
                        if (token.isValue()) {
                            if ("trigram_lambda".equals(fieldName)) {
                                lambdas[0] = parser.doubleValue();
                                if (lambdas[0] < 0) {
                                    throw new ElasticSearchIllegalArgumentException("trigram_lambda must be positive");
                                }
                            } else if ("bigram_lambda".equals(fieldName)) {
                                lambdas[1] = parser.doubleValue();
                                if (lambdas[1] < 0) {
                                    throw new ElasticSearchIllegalArgumentException("bigram_lambda must be positive");
                                }
                            } else if ("unigram_lambda".equals(fieldName)) {
                                lambdas[2] = parser.doubleValue();
                                if (lambdas[2] < 0) {
                                    throw new ElasticSearchIllegalArgumentException("unigram_lambda must be positive");
                                }
                            } else {
                                throw new ElasticSearchIllegalArgumentException(
                                        "suggester[phrase][smoothing][linear] doesn't support field [" + fieldName + "]");
                            }
                        }
                    }
                    double sum = 0.0d;
                    for (int i = 0; i < lambdas.length; i++) {
                        sum += lambdas[i];
                    }
                    if (Math.abs(sum - 1.0) > 0.001) {
                        throw new ElasticSearchIllegalArgumentException("linear smoothing lambdas must sum to 1");
                    }
                    suggestion.setModel(new WordScorer.WordScorerFactory() {
                        @Override
                        public WordScorer newScorer(IndexReader reader, String field, double realWordLikelyhood, BytesRef separator)
                                throws IOException {
                            return new LinearInterpoatingScorer(reader, field, realWordLikelyhood, separator, lambdas[0], lambdas[1],
                                    lambdas[2]);
                        }
                    });
                } else if ("laplace".equals(fieldName)) {
                    ensureNoSmoothing(suggestion);
                    double theAlpha = 0.5;

                    while ((token = parser.nextToken()) != Token.END_OBJECT) {
                        if (token == XContentParser.Token.FIELD_NAME) {
                            fieldName = parser.currentName();
                        }
                        if (token.isValue() && "alpha".equals(fieldName)) {
                            theAlpha = parser.doubleValue();
                        }
                    }
                    final double alpha = theAlpha;
                    suggestion.setModel(new WordScorer.WordScorerFactory() {
                        @Override
                        public WordScorer newScorer(IndexReader reader, String field, double realWordLikelyhood, BytesRef separator)
                                throws IOException {
                            return new LaplaceScorer(reader, field, realWordLikelyhood, separator, alpha);
                        }
                    });

                } else if ("stupid_backoff".equals(fieldName)) {
                    ensureNoSmoothing(suggestion);
                    double theDiscount = 0.4;
                    while ((token = parser.nextToken()) != Token.END_OBJECT) {
                        if (token == XContentParser.Token.FIELD_NAME) {
                            fieldName = parser.currentName();
                        }
                        if (token.isValue() && "discount".equals(fieldName)) {
                            theDiscount = parser.doubleValue();
                        }
                    }
                    final double discount = theDiscount;
                    suggestion.setModel(new WordScorer.WordScorerFactory() {
                        @Override
                        public WordScorer newScorer(IndexReader reader, String field, double realWordLikelyhood, BytesRef separator)
                                throws IOException {
                            return new StupidBackoffScorer(reader, field, realWordLikelyhood, separator, discount);
                        }
                    });

                } else {
                    throw new ElasticSearchIllegalArgumentException("suggester[phrase] doesn't support object field [" + fieldName + "]");
                }
            }
        }
    }

    private void ensureNoSmoothing(PhraseSuggestionContext suggestion) {
        if (suggestion.model() != null) {
            throw new ElasticSearchIllegalArgumentException("only one smoothing model supported");
        }
    }

    private void verifyGenerator(PhraseSuggestionContext.DirectCandidateGenerator suggestion) {
        // Verify options and set defaults
        if (suggestion.field() == null) {
            throw new ElasticSearchIllegalArgumentException("The required field option is missing");
        }
    }

    private void parseCandidateGenerator(XContentParser parser, MapperService mapperService, String fieldName,
            PhraseSuggestionContext.DirectCandidateGenerator generator) throws IOException {
        if (!SuggestUtils.parseDirectSpellcheckerSettings(parser, fieldName, generator)) {
            if ("field".equals(fieldName)) {
                generator.setField(parser.text());
            } else if ("size".equals(fieldName)) {
                generator.size(parser.intValue());
            } else if ("pre_filter".equals(fieldName) || "preFilter".equals(fieldName)) {
                String analyzerName = parser.text();
                Analyzer analyzer = mapperService.analysisService().analyzer(analyzerName);
                if (analyzer == null) {
                    throw new ElasticSearchIllegalArgumentException("Analyzer [" + analyzerName + "] doesn't exists");
                }
                generator.preFilter(analyzer);
            } else if ("post_filter".equals(fieldName) || "postFilter".equals(fieldName)) {
                String analyzerName = parser.text();
                Analyzer analyzer = mapperService.analysisService().analyzer(analyzerName);
                if (analyzer == null) {
                    throw new ElasticSearchIllegalArgumentException("Analyzer [" + analyzerName + "] doesn't exists");
                }
                generator.postFilter(analyzer);
            } else {
                throw new ElasticSearchIllegalArgumentException("CandidateGenerator doesn't support [" + fieldName + "]");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6682.java