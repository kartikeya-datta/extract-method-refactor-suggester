error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7960.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7960.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7960.java
text:
```scala
f@@ilterTerms[i] = fieldMapper.names().createIndexNameTerm(fieldMapper.indexedValueForSearch(terms.get(i)));

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

package org.elasticsearch.index.query;

import com.google.common.collect.Lists;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Filter;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.lucene.search.*;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.cache.filter.support.CacheKeyFilter;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.mapper.MapperService;

import java.io.IOException;
import java.util.List;

import static org.elasticsearch.index.query.support.QueryParsers.wrapSmartNameFilter;

/**
 *
 */
public class TermsFilterParser implements FilterParser {

    public static final String NAME = "terms";

    @Inject
    public TermsFilterParser() {
    }

    @Override
    public String[] names() {
        return new String[]{NAME, "in"};
    }

    @Override
    public Filter parse(QueryParseContext parseContext) throws IOException, QueryParsingException {
        XContentParser parser = parseContext.parser();

        MapperService.SmartNameFieldMappers smartNameFieldMappers = null;
        Boolean cache = null;
        String filterName = null;
        String currentFieldName = null;
        CacheKeyFilter.Key cacheKey = null;
        XContentParser.Token token;
        String execution = "plain";
        List<String> terms = Lists.newArrayList();
        String fieldName = null;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.START_ARRAY) {
                fieldName = currentFieldName;

                while ((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
                    String value = parser.text();
                    if (value == null) {
                        throw new QueryParsingException(parseContext.index(), "No value specified for term filter");
                    }
                    terms.add(value);
                }
            } else if (token.isValue()) {
                if ("execution".equals(currentFieldName)) {
                    execution = parser.text();
                } else if ("_name".equals(currentFieldName)) {
                    filterName = parser.text();
                } else if ("_cache".equals(currentFieldName)) {
                    cache = parser.booleanValue();
                } else if ("_cache_key".equals(currentFieldName) || "_cacheKey".equals(currentFieldName)) {
                    cacheKey = new CacheKeyFilter.Key(parser.text());
                } else {
                    throw new QueryParsingException(parseContext.index(), "[terms] filter does not support [" + currentFieldName + "]");
                }
            }
        }

        if (fieldName == null) {
            throw new QueryParsingException(parseContext.index(), "bool filter requires a field name, followed by array of terms");
        }

        FieldMapper fieldMapper = null;
        smartNameFieldMappers = parseContext.smartFieldMappers(fieldName);
        String[] previousTypes = null;
        if (smartNameFieldMappers != null) {
            if (smartNameFieldMappers.hasMapper()) {
                fieldMapper = smartNameFieldMappers.mapper();
                fieldName = fieldMapper.names().indexName();
            }
            // if we have a doc mapper, its explicit type, mark it
            if (smartNameFieldMappers.explicitTypeInNameWithDocMapper()) {
                previousTypes = QueryParseContext.setTypesWithPrevious(new String[]{smartNameFieldMappers.docMapper().type()});
            }
        }

        try {
            Filter filter;
            if ("plain".equals(execution)) {
                Term[] filterTerms = new Term[terms.size()];
                if (fieldMapper != null) {
                    for (int i = 0; i < filterTerms.length; i++) {
                        filterTerms[i] = fieldMapper.names().createIndexNameTerm(fieldMapper.indexedValue(terms.get(i)));
                    }
                } else {
                    for (int i = 0; i < filterTerms.length; i++) {
                        filterTerms[i] = new Term(fieldName, terms.get(i));
                    }
                }
                filter = new XTermsFilter(filterTerms);
                // cache the whole filter by default, or if explicitly told to
                if (cache == null || cache) {
                    filter = parseContext.cacheFilter(filter, cacheKey);
                }
            } else if ("bool".equals(execution)) {
                XBooleanFilter boolFiler = new XBooleanFilter();
                if (fieldMapper != null) {
                    for (String term : terms) {
                        boolFiler.add(parseContext.cacheFilter(fieldMapper.termFilter(term, parseContext), null), BooleanClause.Occur.SHOULD);
                    }
                } else {
                    for (String term : terms) {
                        boolFiler.add(parseContext.cacheFilter(new TermFilter(new Term(fieldName, term)), null), BooleanClause.Occur.SHOULD);
                    }
                }
                filter = boolFiler;
                // only cache if explicitly told to, since we cache inner filters
                if (cache != null && cache) {
                    filter = parseContext.cacheFilter(filter, cacheKey);
                }
            } else if ("bool_nocache".equals(execution)) {
                XBooleanFilter boolFiler = new XBooleanFilter();
                if (fieldMapper != null) {
                    for (String term : terms) {
                        boolFiler.add(fieldMapper.termFilter(term, parseContext), BooleanClause.Occur.SHOULD);
                    }
                } else {
                    for (String term : terms) {
                        boolFiler.add(new TermFilter(new Term(fieldName, term)), BooleanClause.Occur.SHOULD);
                    }
                }
                filter = boolFiler;
                // cache the whole filter by default, or if explicitly told to
                if (cache == null || cache) {
                    filter = parseContext.cacheFilter(filter, cacheKey);
                }
            } else if ("and".equals(execution)) {
                List<Filter> filters = Lists.newArrayList();
                if (fieldMapper != null) {
                    for (String term : terms) {
                        filters.add(parseContext.cacheFilter(fieldMapper.termFilter(term, parseContext), null));
                    }
                } else {
                    for (String term : terms) {
                        filters.add(parseContext.cacheFilter(new TermFilter(new Term(fieldName, term)), null));
                    }
                }
                filter = new AndFilter(filters);
                // only cache if explicitly told to, since we cache inner filters
                if (cache != null && cache) {
                    filter = parseContext.cacheFilter(filter, cacheKey);
                }
            } else if ("and_nocache".equals(execution)) {
                List<Filter> filters = Lists.newArrayList();
                if (fieldMapper != null) {
                    for (String term : terms) {
                        filters.add(fieldMapper.termFilter(term, parseContext));
                    }
                } else {
                    for (String term : terms) {
                        filters.add(new TermFilter(new Term(fieldName, term)));
                    }
                }
                filter = new AndFilter(filters);
                // cache the whole filter by default, or if explicitly told to
                if (cache == null || cache) {
                    filter = parseContext.cacheFilter(filter, cacheKey);
                }
            } else if ("or".equals(execution)) {
                List<Filter> filters = Lists.newArrayList();
                if (fieldMapper != null) {
                    for (String term : terms) {
                        filters.add(parseContext.cacheFilter(fieldMapper.termFilter(term, parseContext), null));
                    }
                } else {
                    for (String term : terms) {
                        filters.add(parseContext.cacheFilter(new TermFilter(new Term(fieldName, term)), null));
                    }
                }
                filter = new OrFilter(filters);
                // only cache if explicitly told to, since we cache inner filters
                if (cache != null && cache) {
                    filter = parseContext.cacheFilter(filter, cacheKey);
                }
            } else if ("or_nocache".equals(execution)) {
                List<Filter> filters = Lists.newArrayList();
                if (fieldMapper != null) {
                    for (String term : terms) {
                        filters.add(fieldMapper.termFilter(term, parseContext));
                    }
                } else {
                    for (String term : terms) {
                        filters.add(new TermFilter(new Term(fieldName, term)));
                    }
                }
                filter = new OrFilter(filters);
                // cache the whole filter by default, or if explicitly told to
                if (cache == null || cache) {
                    filter = parseContext.cacheFilter(filter, cacheKey);
                }
            } else {
                throw new QueryParsingException(parseContext.index(), "bool filter execution value [" + execution + "] not supported");
            }

            filter = wrapSmartNameFilter(filter, smartNameFieldMappers, parseContext);
            if (filterName != null) {
                parseContext.addNamedFilter(filterName, filter);
            }
            return filter;
        } finally {
            if (smartNameFieldMappers != null && smartNameFieldMappers.explicitTypeInNameWithDocMapper()) {
                QueryParseContext.setTypes(previousTypes);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7960.java