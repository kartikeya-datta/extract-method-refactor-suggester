error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3970.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3970.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3970.java
text:
```scala
Q@@uery booleanQuery = createBooleanQuery(field, queryText, lowFreqOccur);

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

package org.elasticsearch.index.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.ExtendedCommonTermsQuery;
import org.apache.lucene.search.*;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.ElasticsearchIllegalArgumentException;
import org.elasticsearch.ElasticsearchIllegalStateException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.lucene.search.MultiPhrasePrefixQuery;
import org.elasticsearch.common.lucene.search.Queries;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.index.query.support.QueryParsers;

import java.io.IOException;
import java.util.List;

import static org.elasticsearch.index.query.support.QueryParsers.wrapSmartNameQuery;

public class MatchQuery {

    public static enum Type {
        BOOLEAN,
        PHRASE,
        PHRASE_PREFIX
    }

    public static enum ZeroTermsQuery {
        NONE,
        ALL
    }

    protected final QueryParseContext parseContext;

    protected String analyzer;

    protected BooleanClause.Occur occur = BooleanClause.Occur.SHOULD;

    protected boolean enablePositionIncrements = true;

    protected int phraseSlop = 0;

    protected Fuzziness fuzziness = null;
    
    protected int fuzzyPrefixLength = FuzzyQuery.defaultPrefixLength;
    
    protected int maxExpansions = FuzzyQuery.defaultMaxExpansions;
    
    //LUCENE 4 UPGRADE we need a default value for this!
    protected boolean transpositions = false;

    protected MultiTermQuery.RewriteMethod rewriteMethod;

    protected MultiTermQuery.RewriteMethod fuzzyRewriteMethod;

    protected boolean lenient;

    protected ZeroTermsQuery zeroTermsQuery = ZeroTermsQuery.NONE;
    
    protected Float commonTermsCutoff = null;
    
    public MatchQuery(QueryParseContext parseContext) {
        this.parseContext = parseContext;
    }

    public void setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
    }

    public void setOccur(BooleanClause.Occur occur) {
        this.occur = occur;
    }
    
    public void setCommonTermsCutoff(float cutoff) {
        this.commonTermsCutoff = Float.valueOf(cutoff);
    }

    public void setEnablePositionIncrements(boolean enablePositionIncrements) {
        this.enablePositionIncrements = enablePositionIncrements;
    }

    public void setPhraseSlop(int phraseSlop) {
        this.phraseSlop = phraseSlop;
    }

    public void setFuzziness(Fuzziness fuzziness) {
        this.fuzziness = fuzziness;
    }

    public void setFuzzyPrefixLength(int fuzzyPrefixLength) {
        this.fuzzyPrefixLength = fuzzyPrefixLength;
    }

    public void setMaxExpansions(int maxExpansions) {
        this.maxExpansions = maxExpansions;
    }

    public void setTranspositions(boolean transpositions) {
        this.transpositions = transpositions;
    }

    public void setRewriteMethod(MultiTermQuery.RewriteMethod rewriteMethod) {
        this.rewriteMethod = rewriteMethod;
    }

    public void setFuzzyRewriteMethod(MultiTermQuery.RewriteMethod fuzzyRewriteMethod) {
        this.fuzzyRewriteMethod = fuzzyRewriteMethod;
    }

    public void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    public void setZeroTermsQuery(ZeroTermsQuery zeroTermsQuery) {
        this.zeroTermsQuery = zeroTermsQuery;
    }

    protected boolean forceAnalyzeQueryString() {
        return false;
    }

    protected Analyzer getAnalyzer(FieldMapper mapper, MapperService.SmartNameFieldMappers smartNameFieldMappers) {
        Analyzer analyzer = null;
        if (this.analyzer == null) {
            if (mapper != null) {
                analyzer = mapper.searchAnalyzer();
            }
            if (analyzer == null && smartNameFieldMappers != null) {
                analyzer = smartNameFieldMappers.searchAnalyzer();
            }
            if (analyzer == null) {
                analyzer = parseContext.mapperService().searchAnalyzer();
            }
        } else {
            analyzer = parseContext.mapperService().analysisService().analyzer(this.analyzer);
            if (analyzer == null) {
                throw new ElasticsearchIllegalArgumentException("No analyzer found for [" + this.analyzer + "]");
            }
        }
        return analyzer;
    }

    public Query parse(Type type, String fieldName, Object value) throws IOException {
        FieldMapper mapper = null;
        final String field;
        MapperService.SmartNameFieldMappers smartNameFieldMappers = parseContext.smartFieldMappers(fieldName);
        if (smartNameFieldMappers != null && smartNameFieldMappers.hasMapper()) {
            mapper = smartNameFieldMappers.mapper();
            field = mapper.names().indexName();
        } else {
            field = fieldName;
        }

        if (mapper != null && mapper.useTermQueryWithQueryString() && !forceAnalyzeQueryString()) {
            if (smartNameFieldMappers.explicitTypeInNameWithDocMapper()) {
                String[] previousTypes = QueryParseContext.setTypesWithPrevious(new String[]{smartNameFieldMappers.docMapper().type()});
                try {
                    return wrapSmartNameQuery(mapper.termQuery(value, parseContext), smartNameFieldMappers, parseContext);
                } catch (RuntimeException e) {
                    if (lenient) {
                        return null;
                    }
                    throw e;
                } finally {
                    QueryParseContext.setTypes(previousTypes);
                }
            } else {
                try {
                    return wrapSmartNameQuery(mapper.termQuery(value, parseContext), smartNameFieldMappers, parseContext);
                } catch (RuntimeException e) {
                    if (lenient) {
                        return null;
                    }
                    throw e;
                }
            }
        }
        Analyzer analyzer = getAnalyzer(mapper, smartNameFieldMappers);
        MatchQueryBuilder builder = new MatchQueryBuilder(analyzer, mapper);
        builder.setEnablePositionIncrements(this.enablePositionIncrements);

        Query query = null;
        switch (type) {
            case BOOLEAN:
                if (commonTermsCutoff == null) {
                    query = builder.createBooleanQuery(field, value.toString(), occur);
                } else {
                    query = builder.createCommonTermsQuery(field, value.toString(), occur, occur, commonTermsCutoff, mapper);
                }
                break;
            case PHRASE:
                query = builder.createPhraseQuery(field, value.toString(), phraseSlop);
                break;
            case PHRASE_PREFIX:
                query = builder.createPhrasePrefixQuery(field, value.toString(), phraseSlop, maxExpansions);
                break;
            default:
                throw new ElasticsearchIllegalStateException("No type found for [" + type + "]");
        }

        if (query == null) {
            return zeroTermsQuery();
        } else {
            return wrapSmartNameQuery(query, smartNameFieldMappers, parseContext);
        }
    }

    protected Query zeroTermsQuery() {
        return zeroTermsQuery == ZeroTermsQuery.NONE ? Queries.newMatchNoDocsQuery() : Queries.newMatchAllQuery();
    }

    private class MatchQueryBuilder extends QueryBuilder {

        private final FieldMapper mapper;
        /**
         * Creates a new QueryBuilder using the given analyzer.
         */
        public MatchQueryBuilder(Analyzer analyzer, @Nullable FieldMapper mapper) {
            super(analyzer);
            this.mapper = mapper;
         }

        @Override
        protected Query newTermQuery(Term term) {
            return blendTermQuery(term, mapper);
        }


        public Query createPhrasePrefixQuery(String field, String queryText, int phraseSlop, int maxExpansions) {
            final Query query = createFieldQuery(getAnalyzer(), Occur.MUST, field, queryText, true, phraseSlop);
            final MultiPhrasePrefixQuery prefixQuery = new MultiPhrasePrefixQuery();
            prefixQuery.setMaxExpansions(maxExpansions);
            prefixQuery.setSlop(phraseSlop);
            if (query instanceof PhraseQuery) {
                PhraseQuery pq = (PhraseQuery)query;
                Term[] terms = pq.getTerms();
                int[] positions = pq.getPositions();
                for (int i = 0; i < terms.length; i++) {
                    prefixQuery.add(new Term[] {terms[i]}, positions[i]);
                }
                return prefixQuery;
            } else if (query instanceof MultiPhraseQuery) {
                MultiPhraseQuery pq = (MultiPhraseQuery)query;
                List<Term[]> terms = pq.getTermArrays();
                int[] positions = pq.getPositions();
                for (int i = 0; i < terms.size(); i++) {
                    prefixQuery.add(terms.get(i), positions[i]);
                }
                return prefixQuery;
            } else if (query instanceof TermQuery) {
                prefixQuery.add(((TermQuery) query).getTerm());
                return prefixQuery;
            }
            return query;
        }

        public Query createCommonTermsQuery(String field, String queryText, Occur highFreqOccur, Occur lowFreqOccur, float maxTermFrequency, FieldMapper<?> mapper) {
            Query booleanQuery = createBooleanQuery(field, queryText, Occur.SHOULD);
            if (booleanQuery != null && booleanQuery instanceof BooleanQuery) {
                BooleanQuery bq = (BooleanQuery) booleanQuery;
                ExtendedCommonTermsQuery query = new ExtendedCommonTermsQuery(highFreqOccur, lowFreqOccur, maxTermFrequency, ((BooleanQuery)booleanQuery).isCoordDisabled(), mapper);
                for (BooleanClause clause : bq.clauses()) {
                    if (!(clause.getQuery() instanceof TermQuery)) {
                        return booleanQuery;
                    }
                    query.add(((TermQuery) clause.getQuery()).getTerm());
                }
                return query;
            }
            return booleanQuery;

        }
    }

    protected Query blendTermQuery(Term term, FieldMapper mapper) {
        if (fuzziness != null) {
            if (mapper != null) {
                Query query = mapper.fuzzyQuery(term.text(), fuzziness, fuzzyPrefixLength, maxExpansions, transpositions);
                if (query instanceof FuzzyQuery) {
                    QueryParsers.setRewriteMethod((FuzzyQuery) query, fuzzyRewriteMethod);
                }
            }
            int edits = fuzziness.asDistance(term.text());
            FuzzyQuery query = new FuzzyQuery(term, edits, fuzzyPrefixLength, maxExpansions, transpositions);
            QueryParsers.setRewriteMethod(query, rewriteMethod);
            return query;
        }
        if (mapper != null) {
            Query termQuery = mapper.queryStringTermQuery(term);
            if (termQuery != null) {
                return termQuery;
            }
        }
        return new TermQuery(term);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3970.java