error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1341.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1341.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1341.java
text:
```scala
r@@eturn new ParsedQuery(query, parseContext.copyNamedFilters());

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
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

package org.elasticsearch.index.query.xcontent;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.collect.ImmutableMap;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.io.FastByteArrayOutputStream;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.thread.ThreadLocals;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.cache.IndexCache;
import org.elasticsearch.index.engine.IndexEngine;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.query.IndexQueryParser;
import org.elasticsearch.index.query.ParsedQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryParsingException;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.index.similarity.SimilarityService;
import org.elasticsearch.script.ScriptService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.collect.Lists.*;
import static org.elasticsearch.common.collect.Maps.*;
import static org.elasticsearch.common.settings.ImmutableSettings.Builder.*;

/**
 * @author kimchy (shay.banon)
 */
public class XContentIndexQueryParser extends AbstractIndexComponent implements IndexQueryParser {

    public static final class Defaults {
        public static final String QUERY_PREFIX = "index.queryparser.query";
        public static final String FILTER_PREFIX = "index.queryparser.filter";
    }

    private ThreadLocal<ThreadLocals.CleanableValue<QueryParseContext>> cache = new ThreadLocal<ThreadLocals.CleanableValue<QueryParseContext>>() {
        @Override protected ThreadLocals.CleanableValue<QueryParseContext> initialValue() {
            return new ThreadLocals.CleanableValue<QueryParseContext>(new QueryParseContext(index, XContentIndexQueryParser.this));
        }
    };

    private final String name;

    final ScriptService scriptService;

    final MapperService mapperService;

    final SimilarityService similarityService;

    final IndexCache indexCache;

    final IndexEngine indexEngine;

    private final Map<String, XContentQueryParser> queryParsers;

    private final Map<String, XContentFilterParser> filterParsers;

    @Inject public XContentIndexQueryParser(Index index,
                                            @IndexSettings Settings indexSettings, ScriptService scriptService,
                                            MapperService mapperService, IndexCache indexCache, IndexEngine indexEngine,
                                            @Nullable SimilarityService similarityService,
                                            @Nullable Map<String, XContentQueryParserFactory> namedQueryParsers,
                                            @Nullable Map<String, XContentFilterParserFactory> namedFilterParsers,
                                            @Assisted String name, @Assisted @Nullable Settings settings) {
        super(index, indexSettings);
        this.name = name;
        this.scriptService = scriptService;
        this.mapperService = mapperService;
        this.similarityService = similarityService;
        this.indexCache = indexCache;
        this.indexEngine = indexEngine;

        List<XContentQueryParser> queryParsers = newArrayList();
        if (namedQueryParsers != null) {
            Map<String, Settings> queryParserGroups = indexSettings.getGroups(XContentIndexQueryParser.Defaults.QUERY_PREFIX);
            for (Map.Entry<String, XContentQueryParserFactory> entry : namedQueryParsers.entrySet()) {
                String queryParserName = entry.getKey();
                XContentQueryParserFactory queryParserFactory = entry.getValue();
                Settings queryParserSettings = queryParserGroups.get(queryParserName);
                if (queryParserSettings == null) {
                    queryParserSettings = EMPTY_SETTINGS;
                }
                queryParsers.add(queryParserFactory.create(queryParserName, queryParserSettings));
            }
        }

        Map<String, XContentQueryParser> queryParsersMap = newHashMap();
        if (queryParsers != null) {
            for (XContentQueryParser queryParser : queryParsers) {
                add(queryParsersMap, queryParser);
            }
        }
        this.queryParsers = ImmutableMap.copyOf(queryParsersMap);

        List<XContentFilterParser> filterParsers = newArrayList();
        if (namedFilterParsers != null) {
            Map<String, Settings> filterParserGroups = indexSettings.getGroups(XContentIndexQueryParser.Defaults.FILTER_PREFIX);
            for (Map.Entry<String, XContentFilterParserFactory> entry : namedFilterParsers.entrySet()) {
                String filterParserName = entry.getKey();
                XContentFilterParserFactory filterParserFactory = entry.getValue();
                Settings filterParserSettings = filterParserGroups.get(filterParserName);
                if (filterParserSettings == null) {
                    filterParserSettings = EMPTY_SETTINGS;
                }
                filterParsers.add(filterParserFactory.create(filterParserName, filterParserSettings));
            }
        }

        Map<String, XContentFilterParser> filterParsersMap = newHashMap();
        if (filterParsers != null) {
            for (XContentFilterParser filterParser : filterParsers) {
                add(filterParsersMap, filterParser);
            }
        }
        this.filterParsers = ImmutableMap.copyOf(filterParsersMap);
    }

    @Override public String name() {
        return this.name;
    }

    public XContentQueryParser queryParser(String name) {
        return queryParsers.get(name);
    }

    public XContentFilterParser filterParser(String name) {
        return filterParsers.get(name);
    }

    @Override public ParsedQuery parse(QueryBuilder queryBuilder) throws ElasticSearchException {
        XContentParser parser = null;
        try {
            FastByteArrayOutputStream unsafeBytes = queryBuilder.buildAsUnsafeBytes();
            parser = XContentFactory.xContent(unsafeBytes.unsafeByteArray(), 0, unsafeBytes.size()).createParser(unsafeBytes.unsafeByteArray(), 0, unsafeBytes.size());
            return parse(cache.get().get(), parser);
        } catch (QueryParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new QueryParsingException(index, "Failed to parse", e);
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    @Override public ParsedQuery parse(byte[] source) throws ElasticSearchException {
        return parse(source, 0, source.length);
    }

    @Override public ParsedQuery parse(byte[] source, int offset, int length) throws ElasticSearchException {
        XContentParser parser = null;
        try {
            parser = XContentFactory.xContent(source, offset, length).createParser(source, offset, length);
            return parse(cache.get().get(), parser);
        } catch (QueryParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new QueryParsingException(index, "Failed to parse", e);
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    @Override public ParsedQuery parse(String source) throws QueryParsingException {
        XContentParser parser = null;
        try {
            parser = XContentFactory.xContent(source).createParser(source);
            return parse(cache.get().get(), parser);
        } catch (QueryParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new QueryParsingException(index, "Failed to parse [" + source + "]", e);
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    public ParsedQuery parse(XContentParser parser) {
        try {
            return parse(cache.get().get(), parser);
        } catch (IOException e) {
            throw new QueryParsingException(index, "Failed to parse", e);
        }
    }

    public Filter parseInnerFilter(XContentParser parser) throws IOException {
        QueryParseContext context = cache.get().get();
        context.reset(parser);
        return context.parseInnerFilter();
    }

    public Query parseInnerQuery(XContentParser parser) throws IOException {
        QueryParseContext context = cache.get().get();
        context.reset(parser);
        return context.parseInnerQuery();
    }

    private ParsedQuery parse(QueryParseContext parseContext, XContentParser parser) throws IOException, QueryParsingException {
        parseContext.reset(parser);
        Query query = parseContext.parseInnerQuery();
        return new ParsedQuery(query, parseContext.copyNamedFilters(), parseContext.copyScopePhases());
    }

    private void add(Map<String, XContentFilterParser> map, XContentFilterParser filterParser) {
        for (String name : filterParser.names()) {
            map.put(name.intern(), filterParser);
        }
    }

    private void add(Map<String, XContentQueryParser> map, XContentQueryParser queryParser) {
        for (String name : queryParser.names()) {
            map.put(name.intern(), queryParser);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1341.java