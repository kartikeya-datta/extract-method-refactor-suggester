error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7644.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7644.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7644.java
text:
```scala
r@@eturn this.queryBuilder == null ? super.forceAnalyzeQueryString() : this.queryBuilder.forceAnalyzeQueryString();

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
import org.apache.lucene.queries.BlendedTermQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.ElasticsearchIllegalStateException;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.common.lucene.search.Queries;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryParseContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiMatchQuery extends MatchQuery {

    private Float groupTieBreaker = null;

    public void setTieBreaker(float tieBreaker) {
        this.groupTieBreaker = tieBreaker;
    }

    public MultiMatchQuery(QueryParseContext parseContext) {
        super(parseContext);
    }
    
    private Query parseAndApply(Type type, String fieldName, Object value, String minimumShouldMatch, Float boostValue) throws IOException {
        Query query = parse(type, fieldName, value);
        if (query instanceof BooleanQuery) {
            Queries.applyMinimumShouldMatch((BooleanQuery) query, minimumShouldMatch);
        }
        if (boostValue != null && query != null) {
            query.setBoost(boostValue);
        }
        return query;
    }

    public Query parse(MultiMatchQueryBuilder.Type type, Map<String, Float> fieldNames, Object value, String minimumShouldMatch) throws IOException {
        if (fieldNames.size() == 1) {
            Map.Entry<String, Float> fieldBoost = fieldNames.entrySet().iterator().next();
            Float boostValue = fieldBoost.getValue();
            return parseAndApply(type.matchQueryType(), fieldBoost.getKey(), value, minimumShouldMatch, boostValue);
        }

        final float tieBreaker = groupTieBreaker == null ? type.tieBreaker() : groupTieBreaker;
        switch (type) {
            case PHRASE:
            case PHRASE_PREFIX:
            case BEST_FIELDS:
            case MOST_FIELDS:
                queryBuilder = new QueryBuilder(tieBreaker);
                break;
            case CROSS_FIELDS:
                queryBuilder = new CrossFieldsQueryBuilder(tieBreaker);
                break;
            default:
                throw new ElasticsearchIllegalStateException("No such type: " + type);
        }
        final List<? extends Query> queries = queryBuilder.buildGroupedQueries(type, fieldNames, value, minimumShouldMatch);
        return queryBuilder.conbineGrouped(queries);
    }

    private QueryBuilder queryBuilder;

    public class QueryBuilder {
        protected final boolean groupDismax;
        protected final float tieBreaker;

        public QueryBuilder(float tieBreaker) {
            this(tieBreaker != 1.0f, tieBreaker);
        }

        public QueryBuilder(boolean groupDismax, float tieBreaker) {
            this.groupDismax = groupDismax;
            this.tieBreaker = tieBreaker;
        }

        public  List<Query> buildGroupedQueries(MultiMatchQueryBuilder.Type type, Map<String, Float> fieldNames, Object value, String minimumShouldMatch) throws IOException{
            List<Query> queries = new ArrayList<>();
            for (String fieldName : fieldNames.keySet()) {
                Float boostValue = fieldNames.get(fieldName);
                Query query = parseGroup(type.matchQueryType(), fieldName, boostValue, value, minimumShouldMatch);
                if (query != null) {
                    queries.add(query);
                }
            }
            return queries;
        }

        public Query parseGroup(Type type, String field, Float boostValue, Object value, String minimumShouldMatch) throws IOException {
            return parseAndApply(type, field, value, minimumShouldMatch, boostValue);
        }

        public Query conbineGrouped(List<? extends Query> groupQuery) {
            if (groupQuery == null || groupQuery.isEmpty()) {
                return null;
            }
            if (groupQuery.size() == 1) {
                return groupQuery.get(0);
            }
            if (groupDismax) {
                DisjunctionMaxQuery disMaxQuery = new DisjunctionMaxQuery(tieBreaker);
                for (Query query : groupQuery) {
                    disMaxQuery.add(query);
                }
                return disMaxQuery;
            } else {
                final BooleanQuery booleanQuery = new BooleanQuery();
                for (Query query : groupQuery) {
                    booleanQuery.add(query, BooleanClause.Occur.SHOULD);
                }
                return booleanQuery;
            }
        }

        public Query blendTerm(Term term, FieldMapper mapper) {
            return MultiMatchQuery.super.blendTermQuery(term, mapper);
        }

        public boolean forceAnalyzeQueryString() {
            return false;
        }
    }

    public class CrossFieldsQueryBuilder extends QueryBuilder {
        private FieldAndMapper[] blendedFields;

        public CrossFieldsQueryBuilder(float tieBreaker) {
            super(false, tieBreaker);
        }

        public List<Query> buildGroupedQueries(MultiMatchQueryBuilder.Type type, Map<String, Float> fieldNames, Object value, String minimumShouldMatch) throws IOException {
            Map<Analyzer, List<FieldAndMapper>> groups = new HashMap<>();
            List<Tuple<String, Float>> missing = new ArrayList<>();
            for (Map.Entry<String, Float> entry : fieldNames.entrySet()) {
                String name = entry.getKey();
                MapperService.SmartNameFieldMappers smartNameFieldMappers = parseContext.smartFieldMappers(name);
                if (smartNameFieldMappers != null && smartNameFieldMappers.hasMapper()) {
                    Analyzer actualAnalyzer = getAnalyzer(smartNameFieldMappers.mapper(), smartNameFieldMappers);
                    name = smartNameFieldMappers.mapper().names().indexName();
                    if (!groups.containsKey(actualAnalyzer)) {
                       groups.put(actualAnalyzer, new ArrayList<FieldAndMapper>());
                    }
                    Float boost = entry.getValue();
                    boost = boost == null ? Float.valueOf(1.0f) : boost;
                    groups.get(actualAnalyzer).add(new FieldAndMapper(name, smartNameFieldMappers.mapper(), boost));
                } else {
                    missing.add(new Tuple(name, entry.getValue()));
                }

            }
            List<Query> queries = new ArrayList<>();
            for (Tuple<String, Float> tuple : missing) {
                Query q = parseGroup(type.matchQueryType(), tuple.v1(), tuple.v2(), value, minimumShouldMatch);
                if (q != null) {
                    queries.add(q);
                }
            }
            for (List<FieldAndMapper> group : groups.values()) {
                if (group.size() > 1) {
                    blendedFields = new FieldAndMapper[group.size()];
                    int i = 0;
                    for (FieldAndMapper fieldAndMapper : group) {
                        blendedFields[i++] = fieldAndMapper;
                    }
                } else {
                    blendedFields = null;
                }
                final FieldAndMapper fieldAndMapper= group.get(0);
                Query q = parseGroup(type.matchQueryType(), fieldAndMapper.field, fieldAndMapper.boost, value, minimumShouldMatch);
                if (q != null) {
                    queries.add(q);
                }
            }

            return queries.isEmpty() ? null : queries;
        }

        public boolean forceAnalyzeQueryString() {
            return blendedFields != null;
        }

        public Query blendTerm(Term term, FieldMapper mapper) {
            if (blendedFields == null) {
                return super.blendTerm(term, mapper);
            }
            final Term[] terms = new Term[blendedFields.length];
            float[] blendedBoost = new float[blendedFields.length];
            for (int i = 0; i < blendedFields.length; i++) {
                terms[i] = blendedFields[i].newTerm(term.text());
                blendedBoost[i] = blendedFields[i].boost;
            }
            if (commonTermsCutoff != null) {
                return BlendedTermQuery.commonTermsBlendedQuery(terms, blendedBoost, false, commonTermsCutoff);
            }

            if (tieBreaker == 1.0f) {
                return BlendedTermQuery.booleanBlendedQuery(terms, blendedBoost, false);
            }
            return BlendedTermQuery.dismaxBlendedQuery(terms, blendedBoost, tieBreaker);
        }
    }

    @Override
    protected Query blendTermQuery(Term term, FieldMapper mapper) {
        if (queryBuilder == null) {
            return super.blendTermQuery(term, mapper);
        }
        return queryBuilder.blendTerm(term, mapper);
    }

    private static final class FieldAndMapper {
        final String field;
        final FieldMapper mapper;
        final float boost;


        private FieldAndMapper(String field, FieldMapper mapper, float boost) {
            this.field = field;
            this.mapper = mapper;
            this.boost = boost;
        }

        public Term newTerm(String value) {
            try {
                final BytesRef bytesRef = mapper.indexedValueForSearch(value);
                return new Term(field, bytesRef);
            } catch (Exception ex) {
                // we can't parse it just use the incoming value -- it will
                // just have a DF of 0 at the end of the day and will be ignored
            }
            return new Term(field, value);
        }
    }

    protected boolean forceAnalyzeQueryString() {
        return this.queryBuilder.forceAnalyzeQueryString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7644.java