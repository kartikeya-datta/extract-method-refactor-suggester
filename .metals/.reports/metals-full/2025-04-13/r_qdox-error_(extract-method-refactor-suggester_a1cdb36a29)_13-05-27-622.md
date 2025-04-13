error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/520.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/520.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/520.java
text:
```scala
a@@ssertThat(Lucene.count(searcher, new DeletionAwareConstantScoreQuery(cachedFilter), -1), equalTo(0l));

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

package org.elasticsearch.index.cache.filter;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.lucene.search.TermFilter;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.cache.filter.none.NoneFilterCache;
import org.elasticsearch.index.cache.filter.soft.SoftFilterCache;
import org.elasticsearch.index.cache.filter.weak.WeakFilterCache;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.elasticsearch.common.lucene.DocumentBuilder.*;
import static org.elasticsearch.common.settings.ImmutableSettings.Builder.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author kimchy (shay.banon)
 */
public class FilterCacheTests {


    @Test public void testNoCache() throws Exception {
        verifyCache(new NoneFilterCache(new Index("test"), EMPTY_SETTINGS));
    }

    @Test public void testSoftCache() throws Exception {
        verifyCache(new SoftFilterCache(new Index("test"), EMPTY_SETTINGS));
    }

    @Test public void testWeakCache() throws Exception {
        verifyCache(new WeakFilterCache(new Index("test"), EMPTY_SETTINGS));
    }

    private void verifyCache(FilterCache filterCache) throws Exception {
        Directory dir = new RAMDirectory();
        IndexWriter indexWriter = new IndexWriter(dir, Lucene.STANDARD_ANALYZER, true, IndexWriter.MaxFieldLength.UNLIMITED);
        IndexReader reader = indexWriter.getReader();

        for (int i = 0; i < 100; i++) {
            indexWriter.addDocument(doc()
                    .add(field("id", Integer.toString(i)))
                    .boost(i).build());
        }

        reader = refreshReader(reader);
        IndexSearcher searcher = new IndexSearcher(reader);
        assertThat(Lucene.count(searcher, new ConstantScoreQuery(filterCache.cache(new TermFilter(new Term("id", "1")))), -1), equalTo(1l));
        assertThat(Lucene.count(searcher, new FilteredQuery(new MatchAllDocsQuery(), filterCache.cache(new TermFilter(new Term("id", "1")))), -1), equalTo(1l));

        indexWriter.deleteDocuments(new Term("id", "1"));
        reader = refreshReader(reader);
        searcher = new IndexSearcher(reader);
        TermFilter filter = new TermFilter(new Term("id", "1"));
        Filter cachedFilter = filterCache.cache(filter);
        long constantScoreCount = filter == cachedFilter ? 0 : 1;
        // sadly, when caching based on cacheKey with NRT, this fails, that's why we have DeletionAware one
        assertThat(Lucene.count(searcher, new ConstantScoreQuery(cachedFilter), -1), equalTo(constantScoreCount));
        assertThat(Lucene.count(searcher, new DeletionAwareConstantScoreQuery(cachedFilter, true), -1), equalTo(0l));
        assertThat(Lucene.count(searcher, new FilteredQuery(new MatchAllDocsQuery(), cachedFilter), -1), equalTo(0l));

        indexWriter.close();
    }

    private IndexReader refreshReader(IndexReader reader) throws IOException {
        IndexReader oldReader = reader;
        reader = reader.reopen();
        if (reader != oldReader) {
            oldReader.close();
        }
        return reader;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/520.java