error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2737.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2737.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2737.java
text:
```scala
b@@uilder.startObject(RangeFacet.TYPE);

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

package org.elasticsearch.search.facet.range;

import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.xcontent.XContentFilterBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilderException;
import org.elasticsearch.search.facet.AbstractFacetBuilder;

import java.io.IOException;
import java.util.List;

/**
 * A facet builder of range facets.
 *
 * @author kimchy (shay.banon)
 */
public class RangeFacetBuilder extends AbstractFacetBuilder {

    private String keyFieldName;
    private String valueFieldName;

    private List<Entry> entries = Lists.newArrayList();

    /**
     * Constructs a new range facet with the provided facet logical name.
     *
     * @param name The logical name of the facet
     */
    public RangeFacetBuilder(String name) {
        super(name);
    }

    /**
     * The field name to perform the range facet. Translates to perform the range facet
     * using the provided field as both the {@link #keyField(String)} and {@link #valueField(String)}.
     */
    public RangeFacetBuilder field(String field) {
        this.keyFieldName = field;
        this.valueFieldName = field;
        return this;
    }

    /**
     * The field name to use in order to control where the hit will "fall into" within the range
     * entries. Essentially, using the key field numeric value, the hit will be "rounded" into the relevant
     * bucket controlled by the interval.
     */
    public RangeFacetBuilder keyField(String keyField) {
        this.keyFieldName = keyField;
        return this;
    }

    /**
     * The field name to use as the value of the hit to compute data based on values within the interval
     * (for example, total).
     */
    public RangeFacetBuilder valueField(String valueField) {
        this.valueFieldName = valueField;
        return this;
    }

    /**
     * Adds a range entry with explicit from and to.
     *
     * @param from The from range limit
     * @param to   The to range limit
     */
    public RangeFacetBuilder addRange(double from, double to) {
        entries.add(new Entry(from, to));
        return this;
    }

    public RangeFacetBuilder addRange(String from, String to) {
        entries.add(new Entry(from, to));
        return this;
    }

    /**
     * Adds a range entry with explicit from and unbounded to.
     *
     * @param from the from range limit, to is unbounded.
     */
    public RangeFacetBuilder addUnboundedTo(double from) {
        entries.add(new Entry(from, Double.POSITIVE_INFINITY));
        return this;
    }

    public RangeFacetBuilder addUnboundedTo(String from) {
        entries.add(new Entry(from, null));
        return this;
    }

    /**
     * Adds a range entry with explicit to and unbounded from.
     *
     * @param to the to range limit, from is unbounded.
     */
    public RangeFacetBuilder addUnboundedFrom(double to) {
        entries.add(new Entry(Double.NEGATIVE_INFINITY, to));
        return this;
    }

    public RangeFacetBuilder addUnboundedFrom(String to) {
        entries.add(new Entry(null, to));
        return this;
    }

    /**
     * Should the facet run in global mode (not bounded by the search query) or not (bounded by
     * the search query). Defaults to <tt>false</tt>.
     */
    public RangeFacetBuilder global(boolean global) {
        super.global(global);
        return this;
    }

    /**
     * Marks the facet to run in a specific scope.
     */
    @Override public RangeFacetBuilder scope(String scope) {
        super.scope(scope);
        return this;
    }

    /**
     * An additional filter used to further filter down the set of documents the facet will run on.
     */
    public RangeFacetBuilder facetFilter(XContentFilterBuilder filter) {
        this.facetFilter = filter;
        return this;
    }

    @Override public void toXContent(XContentBuilder builder, Params params) throws IOException {
        if (keyFieldName == null) {
            throw new SearchSourceBuilderException("field must be set on range facet for facet [" + name + "]");
        }

        if (entries.isEmpty()) {
            throw new SearchSourceBuilderException("at least one range must be defined for range facet [" + name + "]");
        }

        builder.startObject(name);

        builder.startObject(RangeFacetCollectorParser.NAME);
        if (valueFieldName != null && !keyFieldName.equals(valueFieldName)) {
            builder.field("key_field", keyFieldName);
            builder.field("value_field", valueFieldName);
        } else {
            builder.field("field", keyFieldName);
        }

        builder.startArray("ranges");
        for (Entry entry : entries) {
            builder.startObject();
            if (entry.fromAsString != null) {
                builder.field("from", entry.fromAsString);
            } else if (!Double.isInfinite(entry.from)) {
                builder.field("from", entry.from);
            }
            if (entry.toAsString != null) {
                builder.field("to", entry.toAsString);
            } else if (!Double.isInfinite(entry.to)) {
                builder.field("to", entry.to);
            }
            builder.endObject();
        }
        builder.endArray();

        builder.endObject();

        addFilterFacetAndGlobal(builder, params);

        builder.endObject();
    }

    static class Entry {
        double from = Double.NEGATIVE_INFINITY;
        double to = Double.POSITIVE_INFINITY;

        String fromAsString;
        String toAsString;

        Entry(String fromAsString, String toAsString) {
            this.fromAsString = fromAsString;
            this.toAsString = toAsString;
        }

        Entry(double from, double to) {
            this.from = from;
            this.to = to;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2737.java