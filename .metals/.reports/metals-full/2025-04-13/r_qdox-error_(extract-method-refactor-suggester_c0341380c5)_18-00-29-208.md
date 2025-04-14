error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7331.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7331.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7331.java
text:
```scala
C@@ollection<String> indices = new ArrayList<>();

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

package org.elasticsearch.index.query;

import org.apache.lucene.search.Filter;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.lucene.search.Queries;
import org.elasticsearch.common.regex.Regex;
import org.elasticsearch.common.xcontent.XContentParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 */
public class IndicesFilterParser implements FilterParser {

    public static final String NAME = "indices";

    @Nullable
    private final ClusterService clusterService;

    @Inject
    public IndicesFilterParser(@Nullable ClusterService clusterService) {
        this.clusterService = clusterService;
    }

    @Override
    public String[] names() {
        return new String[]{NAME};
    }

    @Override
    public Filter parse(QueryParseContext parseContext) throws IOException, QueryParsingException {
        XContentParser parser = parseContext.parser();

        Filter filter = null;
        Filter noMatchFilter = Queries.MATCH_ALL_FILTER;
        boolean filterFound = false;
        boolean indicesFound = false;
        boolean currentIndexMatchesIndices = false;
        String filterName = null;

        String currentFieldName = null;
        XContentParser.Token token;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.START_OBJECT) {
                if ("filter".equals(currentFieldName)) {
                    filterFound = true;
                    //TODO We are able to decide whether to parse the filter or not only if indices in the query appears first
                    if (indicesFound && !currentIndexMatchesIndices) {
                        parseContext.parser().skipChildren(); // skip the filter object without parsing it
                    } else {
                        filter = parseContext.parseInnerFilter();
                    }
                } else if ("no_match_filter".equals(currentFieldName)) {
                    if (indicesFound && currentIndexMatchesIndices) {
                        parseContext.parser().skipChildren(); // skip the filter object without parsing it
                    } else {
                        noMatchFilter = parseContext.parseInnerFilter();
                    }
                } else {
                    throw new QueryParsingException(parseContext.index(), "[indices] filter does not support [" + currentFieldName + "]");
                }
            } else if (token == XContentParser.Token.START_ARRAY) {
                if ("indices".equals(currentFieldName)) {
                    if (indicesFound) {
                        throw  new QueryParsingException(parseContext.index(), "[indices] indices or index already specified");
                    }
                    indicesFound = true;
                    Collection<String> indices = new ArrayList<String>();
                    while (parser.nextToken() != XContentParser.Token.END_ARRAY) {
                        String value = parser.textOrNull();
                        if (value == null) {
                            throw new QueryParsingException(parseContext.index(), "[indices] no value specified for 'indices' entry");
                        }
                        indices.add(value);
                    }
                    currentIndexMatchesIndices = matchesIndices(parseContext.index().name(), indices.toArray(new String[indices.size()]));
                } else {
                    throw new QueryParsingException(parseContext.index(), "[indices] filter does not support [" + currentFieldName + "]");
                }
            } else if (token.isValue()) {
                if ("index".equals(currentFieldName)) {
                    if (indicesFound) {
                        throw  new QueryParsingException(parseContext.index(), "[indices] indices or index already specified");
                    }
                    indicesFound = true;
                    currentIndexMatchesIndices = matchesIndices(parseContext.index().name(), parser.text());
                } else if ("no_match_filter".equals(currentFieldName)) {
                    String type = parser.text();
                    if ("all".equals(type)) {
                        noMatchFilter = Queries.MATCH_ALL_FILTER;
                    } else if ("none".equals(type)) {
                        noMatchFilter = Queries.MATCH_NO_FILTER;
                    }
                } else if ("_name".equals(currentFieldName)) {
                    filterName = parser.text();
                } else {
                    throw new QueryParsingException(parseContext.index(), "[indices] filter does not support [" + currentFieldName + "]");
                }
            }
        }
        if (!filterFound) {
            throw new QueryParsingException(parseContext.index(), "[indices] requires 'filter' element");
        }
        if (!indicesFound) {
            throw new QueryParsingException(parseContext.index(), "[indices] requires 'indices' or 'index' element");
        }

        Filter chosenFilter;
        if (currentIndexMatchesIndices) {
            chosenFilter = filter;
        } else {
            chosenFilter = noMatchFilter;
        }

        if (filterName != null) {
            parseContext.addNamedFilter(filterName, chosenFilter);
        }

        return chosenFilter;
    }

    protected boolean matchesIndices(String currentIndex, String... indices) {
        final String[] concreteIndices = clusterService.state().metaData().concreteIndicesIgnoreMissing(indices);
        for (String index : concreteIndices) {
            if (Regex.simpleMatch(index, currentIndex)) {
                return true;
            }
        }
        return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7331.java