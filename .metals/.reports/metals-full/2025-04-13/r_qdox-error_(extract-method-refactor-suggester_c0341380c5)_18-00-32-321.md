error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8474.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8474.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8474.java
text:
```scala
r@@eturn indexQueryParser.parseInnerFilter(parser).filter();

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

package org.elasticsearch.index.aliases;

import org.apache.lucene.queries.FilterClause;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Filter;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.compress.CompressedString;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.lucene.search.XBooleanFilter;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.concurrent.ConcurrentCollections;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.query.IndexQueryParserService;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.indices.AliasFilterParsingException;
import org.elasticsearch.indices.InvalidAliasNameException;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class IndexAliasesService extends AbstractIndexComponent implements Iterable<IndexAlias> {

    private final IndexQueryParserService indexQueryParser;
    private final Map<String, IndexAlias> aliases = ConcurrentCollections.newConcurrentMapWithAggressiveConcurrency();

    @Inject
    public IndexAliasesService(Index index, @IndexSettings Settings indexSettings, IndexQueryParserService indexQueryParser) {
        super(index, indexSettings);
        this.indexQueryParser = indexQueryParser;
    }

    public boolean hasAlias(String alias) {
        return aliases.containsKey(alias);
    }

    public IndexAlias alias(String alias) {
        return aliases.get(alias);
    }

    public IndexAlias create(String alias, @Nullable CompressedString filter) {
        return new IndexAlias(alias, filter, parse(alias, filter));
    }

    public void add(String alias, @Nullable CompressedString filter) {
        add(new IndexAlias(alias, filter, parse(alias, filter)));
    }

    public void addAll(Map<String, IndexAlias> aliases) {
        this.aliases.putAll(aliases);
    }

    /**
     * Returns the filter associated with listed filtering aliases.
     * <p/>
     * <p>The list of filtering aliases should be obtained by calling MetaData.filteringAliases.
     * Returns <tt>null</tt> if no filtering is required.</p>
     */
    public Filter aliasFilter(String... aliases) {
        if (aliases == null || aliases.length == 0) {
            return null;
        }
        if (aliases.length == 1) {
            IndexAlias indexAlias = alias(aliases[0]);
            if (indexAlias == null) {
                // This shouldn't happen unless alias disappeared after filteringAliases was called.
                throw new InvalidAliasNameException(index, aliases[0], "Unknown alias name was passed to alias Filter");
            }
            return indexAlias.parsedFilter();
        } else {
            // we need to bench here a bit, to see maybe it makes sense to use OrFilter
            XBooleanFilter combined = new XBooleanFilter();
            for (String alias : aliases) {
                IndexAlias indexAlias = alias(alias);
                if (indexAlias == null) {
                    // This shouldn't happen unless alias disappeared after filteringAliases was called.
                    throw new InvalidAliasNameException(index, aliases[0], "Unknown alias name was passed to alias Filter");
                }
                if (indexAlias.parsedFilter() != null) {
                    combined.add(new FilterClause(indexAlias.parsedFilter(), BooleanClause.Occur.SHOULD));
                } else {
                    // The filter might be null only if filter was removed after filteringAliases was called
                    return null;
                }
            }
            if (combined.clauses().size() == 0) {
                return null;
            }
            if (combined.clauses().size() == 1) {
                return combined.clauses().get(0).getFilter();
            }
            return combined;
        }
    }

    private void add(IndexAlias indexAlias) {
        aliases.put(indexAlias.alias(), indexAlias);
    }

    public void remove(String alias) {
        aliases.remove(alias);
    }

    private Filter parse(String alias, CompressedString filter) {
        if (filter == null) {
            return null;
        }
        try {
            byte[] filterSource = filter.uncompressed();
            XContentParser parser = XContentFactory.xContent(filterSource).createParser(filterSource);
            try {
                return indexQueryParser.parseInnerFilter(parser);
            } finally {
                parser.close();
            }
        } catch (IOException ex) {
            throw new AliasFilterParsingException(index, alias, "Invalid alias filter", ex);
        }
    }

    @Override
    public Iterator<IndexAlias> iterator() {
        return aliases.values().iterator();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8474.java