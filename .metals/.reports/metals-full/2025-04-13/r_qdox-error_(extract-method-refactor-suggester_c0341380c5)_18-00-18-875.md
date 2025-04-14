error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6418.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6418.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6418.java
text:
```scala
J@@sonBuilder builder = JsonBuilder.jsonBuilder();

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

package org.elasticsearch.search.builder;

import org.elasticsearch.index.query.json.JsonQueryBuilder;
import org.elasticsearch.util.json.JsonBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.*;

/**
 * @author kimchy (Shay Banon)
 */
public class SearchSourceBuilder {

    public static SearchSourceBuilder searchSource() {
        return new SearchSourceBuilder();
    }

    public static SearchSourceFacetsBuilder facets() {
        return new SearchSourceFacetsBuilder();
    }

    private JsonQueryBuilder queryBuilder;

    private int from = -1;

    private int size = -1;

    private String queryParserName;

    private Boolean explain;

    private List<SortTuple> sortFields;

    private List<String> fieldNames;

    private SearchSourceFacetsBuilder facetsBuilder;

    public SearchSourceBuilder() {
    }

    public SearchSourceBuilder query(JsonQueryBuilder query) {
        this.queryBuilder = query;
        return this;
    }

    public SearchSourceBuilder from(int from) {
        this.from = from;
        return this;
    }

    public SearchSourceBuilder size(int size) {
        this.size = size;
        return this;
    }

    public SearchSourceBuilder queryParserName(String queryParserName) {
        this.queryParserName = queryParserName;
        return this;
    }

    public SearchSourceBuilder explain(boolean explain) {
        this.explain = explain;
        return this;
    }

    public SearchSourceBuilder sort(String name, boolean reverse) {
        return sort(name, null, reverse);
    }

    public SearchSourceBuilder sort(String name) {
        return sort(name, null, false);
    }

    public SearchSourceBuilder sort(String name, String type) {
        return sort(name, type, false);
    }

    public SearchSourceBuilder sort(String name, String type, boolean reverse) {
        if (sortFields == null) {
            sortFields = newArrayListWithCapacity(2);
        }
        sortFields.add(new SortTuple(name, reverse, type));
        return this;
    }

    public SearchSourceBuilder facets(SearchSourceFacetsBuilder facetsBuilder) {
        this.facetsBuilder = facetsBuilder;
        return this;
    }

    public SearchSourceBuilder fields(List<String> fields) {
        this.fieldNames = fields;
        return this;
    }

    public SearchSourceBuilder field(String name) {
        if (fieldNames == null) {
            fieldNames = new ArrayList<String>();
        }
        fieldNames.add(name);
        return this;
    }

    public String build() {
        try {
            JsonBuilder builder = JsonBuilder.cached();
            builder.startObject();

            if (from != -1) {
                builder.field("from", from);
            }
            if (size != -1) {
                builder.field("size", size);
            }
            if (queryParserName != null) {
                builder.field("queryParserName", queryParserName);
            }

            builder.field("query");
            queryBuilder.toJson(builder);

            if (explain != null) {
                builder.field("explain", explain);
            }

            if (fieldNames != null) {
                if (fieldNames.size() == 1) {
                    builder.field("fields", fieldNames.get(0));
                } else {
                    builder.startArray("fields");
                    for (String fieldName : fieldNames) {
                        builder.string(fieldName);
                    }
                    builder.endArray();
                }
            }

            if (sortFields != null) {
                builder.field("sort");
                builder.startObject();
                for (SortTuple sortTuple : sortFields) {
                    builder.field(sortTuple.fieldName());
                    builder.startObject();
                    if (sortTuple.reverse) {
                        builder.field("reverse", true);
                    }
                    if (sortTuple.type != null) {
                        builder.field("type", sortTuple.type());
                    }
                    builder.endObject();
                }
                builder.endObject();
            }

            if (facetsBuilder != null) {
                facetsBuilder.json(builder);
            }

            builder.endObject();

            return builder.string();
        } catch (Exception e) {
            throw new SearchSourceBuilderException("Failed to build search source", e);
        }
    }

    private static class SortTuple {
        private final String fieldName;
        private final boolean reverse;
        private final String type;

        private SortTuple(String fieldName, boolean reverse, String type) {
            this.fieldName = fieldName;
            this.reverse = reverse;
            this.type = type;
        }

        public String fieldName() {
            return fieldName;
        }

        public boolean reverse() {
            return reverse;
        }

        public String type() {
            return type;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6418.java