error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8765.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8765.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8765.java
text:
```scala
M@@apperService.SmartNameObjectMapper mapper = parseContext.smartObjectMapper(path);

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

package org.elasticsearch.index.query;

import org.apache.lucene.search.DeletionAwareConstantScoreQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.cache.filter.support.CacheKeyFilter;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.mapper.object.ObjectMapper;
import org.elasticsearch.index.search.nested.BlockJoinQuery;
import org.elasticsearch.index.search.nested.NonNestedDocsFilter;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;

public class NestedFilterParser implements FilterParser {

    public static final String NAME = "nested";

    @Inject public NestedFilterParser() {
    }

    @Override public String[] names() {
        return new String[]{NAME, Strings.toCamelCase(NAME)};
    }

    @Override public Filter parse(QueryParseContext parseContext) throws IOException, QueryParsingException {
        XContentParser parser = parseContext.parser();

        Query query = null;
        Filter filter = null;
        float boost = 1.0f;
        String scope = null;
        String path = null;
        boolean cache = false;
        CacheKeyFilter.Key cacheKey = null;
        String filterName = null;

        // we need a late binding filter so we can inject a parent nested filter inner nested queries
        NestedQueryParser.LateBindingParentFilter currentParentFilterContext = NestedQueryParser.parentFilterContext.get();

        NestedQueryParser.LateBindingParentFilter usAsParentFilter = new NestedQueryParser.LateBindingParentFilter();
        NestedQueryParser.parentFilterContext.set(usAsParentFilter);

        String currentFieldName = null;
        XContentParser.Token token;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.START_OBJECT) {
                if ("query".equals(currentFieldName)) {
                    query = parseContext.parseInnerQuery();
                } else if ("filter".equals(currentFieldName)) {
                    filter = parseContext.parseInnerFilter();
                }
            } else if (token.isValue()) {
                if ("path".equals(currentFieldName)) {
                    path = parser.text();
                } else if ("boost".equals(currentFieldName)) {
                    boost = parser.floatValue();
                } else if ("_scope".equals(currentFieldName)) {
                    scope = parser.text();
                } else if ("_name".equals(currentFieldName)) {
                    filterName = parser.text();
                } else if ("_cache".equals(currentFieldName)) {
                    cache = parser.booleanValue();
                } else if ("_cache_key".equals(currentFieldName) || "_cacheKey".equals(currentFieldName)) {
                    cacheKey = new CacheKeyFilter.Key(parser.text());
                }
            }
        }
        if (query == null && filter == null) {
            throw new QueryParsingException(parseContext.index(), "[nested] requires either 'query' or 'filter' field");
        }
        if (path == null) {
            throw new QueryParsingException(parseContext.index(), "[nested] requires 'path' field");
        }

        if (filter != null) {
            query = new DeletionAwareConstantScoreQuery(filter);
        }

        query.setBoost(boost);

        MapperService.SmartNameObjectMapper mapper = parseContext.mapperService().smartNameObjectMapper(path);
        if (mapper == null) {
            throw new QueryParsingException(parseContext.index(), "[nested] failed to find nested object under path [" + path + "]");
        }
        ObjectMapper objectMapper = mapper.mapper();
        if (objectMapper == null) {
            throw new QueryParsingException(parseContext.index(), "[nested] failed to find nested object under path [" + path + "]");
        }
        if (!objectMapper.nested().isNested()) {
            throw new QueryParsingException(parseContext.index(), "[nested] nested object under path [" + path + "] is not of nested type");
        }

        Filter childFilter = parseContext.cacheFilter(objectMapper.nestedTypeFilter(), null);
        usAsParentFilter.filter = childFilter;
        // wrap the child query to only work on the nested path type
        query = new FilteredQuery(query, childFilter);

        Filter parentFilter = currentParentFilterContext;
        if (parentFilter == null) {
            parentFilter = NonNestedDocsFilter.INSTANCE;
            if (mapper.hasDocMapper()) {
                // filter based on the type...
                parentFilter = mapper.docMapper().typeFilter();
            }
            parentFilter = parseContext.cacheFilter(parentFilter, null);
        }

        // restore the thread local one...
        NestedQueryParser.parentFilterContext.set(currentParentFilterContext);

        BlockJoinQuery joinQuery = new BlockJoinQuery(query, parentFilter, BlockJoinQuery.ScoreMode.None);

        if (scope != null) {
            SearchContext.current().addNestedQuery(scope, joinQuery);
        }

        Filter joinFilter = new QueryWrapperFilter(joinQuery);
        if (cache) {
            joinFilter = parseContext.cacheFilter(joinFilter, cacheKey);
        }
        if (filterName != null) {
            parseContext.addNamedFilter(filterName, joinFilter);
        }
        return joinFilter;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8765.java