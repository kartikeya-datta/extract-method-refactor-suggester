error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7563.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7563.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7563.java
text:
```scala
s@@earchScript.setNextReader(context);

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

import com.google.common.collect.Maps;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BitsFilteredDocIdSet;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FilteredDocIdSet;
import org.apache.lucene.util.Bits;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.lucene.docset.GetDocSet;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.cache.filter.support.CacheKeyFilter;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.Map;

/**
 *
 */
public class ScriptFilterParser implements FilterParser {

    public static final String NAME = "script";

    @Inject
    public ScriptFilterParser() {
    }

    @Override
    public String[] names() {
        return new String[]{NAME};
    }

    @Override
    public Filter parse(QueryParseContext parseContext) throws IOException, QueryParsingException {
        XContentParser parser = parseContext.parser();

        XContentParser.Token token;

        boolean cache = false; // no need to cache it by default, changes a lot?
        CacheKeyFilter.Key cacheKey = null;
        // also, when caching, since its isCacheable is false, will result in loading all bit set...
        String script = null;
        String scriptLang = null;
        Map<String, Object> params = null;

        String filterName = null;
        String currentFieldName = null;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.START_OBJECT) {
                if ("params".equals(currentFieldName)) {
                    params = parser.map();
                } else {
                    throw new QueryParsingException(parseContext.index(), "[script] filter does not support [" + currentFieldName + "]");
                }
            } else if (token.isValue()) {
                if ("script".equals(currentFieldName)) {
                    script = parser.text();
                } else if ("lang".equals(currentFieldName)) {
                    scriptLang = parser.text();
                } else if ("_name".equals(currentFieldName)) {
                    filterName = parser.text();
                } else if ("_cache".equals(currentFieldName)) {
                    cache = parser.booleanValue();
                } else if ("_cache_key".equals(currentFieldName) || "_cacheKey".equals(currentFieldName)) {
                    cacheKey = new CacheKeyFilter.Key(parser.text());
                } else {
                    throw new QueryParsingException(parseContext.index(), "[script] filter does not support [" + currentFieldName + "]");
                }
            }
        }

        if (script == null) {
            throw new QueryParsingException(parseContext.index(), "script must be provided with a [script] filter");
        }
        if (params == null) {
            params = Maps.newHashMap();
        }

        Filter filter = new ScriptFilter(scriptLang, script, params, parseContext.scriptService());
        if (cache) {
            filter = parseContext.cacheFilter(filter, cacheKey);
        }
        if (filterName != null) {
            parseContext.addNamedFilter(filterName, filter);
        }
        return filter;
    }

    public static class ScriptFilter extends Filter {

        private final String script;

        private final Map<String, Object> params;

        private final SearchScript searchScript;

        private ScriptFilter(String scriptLang, String script, Map<String, Object> params, ScriptService scriptService) {
            this.script = script;
            this.params = params;

            SearchContext context = SearchContext.current();
            if (context == null) {
                throw new ElasticSearchIllegalStateException("No search context on going...");
            }

            this.searchScript = context.scriptService().search(context.lookup(), scriptLang, script, params);
        }

        @Override
        public String toString() {
            StringBuilder buffer = new StringBuilder();
            buffer.append("ScriptFilter(");
            buffer.append(script);
            buffer.append(")");
            return buffer.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ScriptFilter that = (ScriptFilter) o;

            if (params != null ? !params.equals(that.params) : that.params != null) return false;
            if (script != null ? !script.equals(that.script) : that.script != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = script != null ? script.hashCode() : 0;
            result = 31 * result + (params != null ? params.hashCode() : 0);
            return result;
        }

        @Override
        public DocIdSet getDocIdSet(AtomicReaderContext context, Bits acceptDocs) throws IOException {
            searchScript.setNextReader(context.reader());
            // LUCENE 4 UPGRADE: we can simply wrap this here since it is not cacheable and if we are not top level we will get a null passed anyway 
            return BitsFilteredDocIdSet.wrap(new ScriptDocSet(context.reader(), searchScript), acceptDocs);
        }

        static class ScriptDocSet extends GetDocSet {

            private final SearchScript searchScript;

            public ScriptDocSet(IndexReader reader, SearchScript searchScript) {
                super(reader.maxDoc());
                this.searchScript = searchScript;
            }

            @Override
            public long sizeInBytes() {
                return 0;
            }

            @Override
            public boolean isCacheable() {
                // not cacheable for several reasons:
                // 1. The script service is shared and holds the current reader executing against, and it
                //    gets changed on each getDocIdSet (which is fine for sequential reader search)
                // 2. If its really going to be cached (the _cache setting), its better to just load it into in memory bitset
                return false;
            }

            @Override
            public boolean get(int doc) {
                searchScript.setNextDocId(doc);
                Object val = searchScript.run();
                if (val == null) {
                    return false;
                }
                if (val instanceof Boolean) {
                    return (Boolean) val;
                }
                if (val instanceof Number) {
                    return ((Number) val).longValue() != 0;
                }
                throw new ElasticSearchIllegalArgumentException("Can't handle type [" + val + "] in script filter");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7563.java