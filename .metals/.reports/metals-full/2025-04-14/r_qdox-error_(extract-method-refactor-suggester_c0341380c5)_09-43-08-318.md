error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7962.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7962.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7962.java
text:
```scala
G@@etResponse getResponse = client.get(new GetRequest(lookup.getIndex(), lookup.getType(), lookup.getId()).preference("_local").routing(lookup.getRouting())).actionGet();

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

package org.elasticsearch.indices.cache.filter.terms;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.Weigher;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.index.cache.filter.support.CacheKeyFilter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 */
public class IndicesTermsFilterCache extends AbstractComponent {

    private static TermsFilterValue NO_TERMS = new TermsFilterValue(0, null);

    private final Client client;

    private final Cache<CacheKeyFilter.Key, TermsFilterValue> cache;

    @Inject
    public IndicesTermsFilterCache(Settings settings, Client client) {
        super(settings);
        this.client = client;

        ByteSizeValue size = componentSettings.getAsBytesSize("size", new ByteSizeValue(10, ByteSizeUnit.MB));
        TimeValue expireAfterWrite = componentSettings.getAsTime("expire_after_write", null);
        TimeValue expireAfterAccess = componentSettings.getAsTime("expire_after_access", null);

        CacheBuilder<CacheKeyFilter.Key, TermsFilterValue> builder = CacheBuilder.newBuilder()
                .maximumWeight(size.bytes())
                .weigher(new TermsFilterValueWeigher());

        if (expireAfterAccess != null) {
            builder.expireAfterAccess(expireAfterAccess.millis(), TimeUnit.MILLISECONDS);
        }
        if (expireAfterWrite != null) {
            builder.expireAfterWrite(expireAfterWrite.millis(), TimeUnit.MILLISECONDS);
        }

        this.cache = builder.build();
    }

    /**
     * An external lookup terms filter. Note, already implements the {@link CacheKeyFilter} so no need
     * to double cache key it.
     */
    public Filter lookupTermsFilter(final CacheKeyFilter.Key cacheKey, final TermsLookup lookup) {
        return new LookupTermsFilter(lookup, cacheKey, this);
    }

    @Nullable
    private Filter termsFilter(final CacheKeyFilter.Key cacheKey, final TermsLookup lookup) throws RuntimeException {
        try {
            return cache.get(cacheKey, new Callable<TermsFilterValue>() {
                @Override
                public TermsFilterValue call() throws Exception {
                    GetResponse getResponse = client.get(new GetRequest(lookup.getIndex(), lookup.getType(), lookup.getId()).preference("_local")).actionGet();
                    if (!getResponse.isExists()) {
                        return NO_TERMS;
                    }
                    List<Object> values = XContentMapValues.extractRawValues(lookup.getPath(), getResponse.getSourceAsMap());
                    if (values.isEmpty()) {
                        return NO_TERMS;
                    }
                    Filter filter = lookup.getFieldMapper().termsFilter(values, lookup.getQueryParseContext());
                    return new TermsFilterValue(estimateSizeInBytes(values), filter);
                }
            }).filter;
        } catch (ExecutionException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw new ElasticSearchException(e.getMessage(), e.getCause());
        }
    }

    long estimateSizeInBytes(List<Object> terms) {
        long size = 8;
        for (Object term : terms) {
            if (term instanceof BytesRef) {
                size += ((BytesRef) term).length;
            } else if (term instanceof String) {
                size += ((String) term).length() / 2;
            } else {
                size += 4;
            }
        }
        return size;
    }

    public void clear(String reason) {
        cache.invalidateAll();
    }

    public void clear(String reason, String[] keys) {
        final BytesRef spare = new BytesRef();
        for (String key : keys) {
            cache.invalidate(new CacheKeyFilter.Key(Strings.toUTF8Bytes(key, spare)));
        }
    }

    static class LookupTermsFilter extends Filter implements CacheKeyFilter {

        private final TermsLookup lookup;
        private final CacheKeyFilter.Key cacheKey;
        private final IndicesTermsFilterCache cache;
        boolean termsFilterCalled;
        private Filter termsFilter;

        LookupTermsFilter(TermsLookup lookup, CacheKeyFilter.Key cacheKey, IndicesTermsFilterCache cache) {
            this.lookup = lookup;
            this.cacheKey = cacheKey;
            this.cache = cache;
        }

        @Override
        public DocIdSet getDocIdSet(AtomicReaderContext context, Bits acceptDocs) throws IOException {
            // only call the terms filter once per execution (across segments per single search request)
            if (!termsFilterCalled) {
                termsFilterCalled = true;
                termsFilter = cache.termsFilter(cacheKey, lookup);
            }
            if (termsFilter == null) return null;
            return termsFilter.getDocIdSet(context, acceptDocs);
        }

        @Override
        public Key cacheKey() {
            return this.cacheKey;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LookupTermsFilter that = (LookupTermsFilter) o;

            if (!cacheKey.equals(that.cacheKey)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return cacheKey.hashCode();
        }

        @Override
        public String toString() {
            return "terms(" + lookup.toString() + ")";
        }
    }

    static class TermsFilterValueWeigher implements Weigher<CacheKeyFilter.Key, TermsFilterValue> {

        @Override
        public int weigh(CacheKeyFilter.Key key, TermsFilterValue value) {
            return (int) (key.bytes().length + value.sizeInBytes);
        }
    }

    // TODO: if TermsFilter exposed sizeInBytes, we won't need this wrapper
    static class TermsFilterValue {
        public final long sizeInBytes;
        public final Filter filter;

        TermsFilterValue(long sizeInBytes, Filter filter) {
            this.sizeInBytes = sizeInBytes;
            this.filter = filter;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7962.java