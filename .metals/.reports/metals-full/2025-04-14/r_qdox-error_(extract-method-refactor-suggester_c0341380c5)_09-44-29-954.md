error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3395.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3395.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3395.java
text:
```scala
public N@@umericRangeFilterBuilder lte(Object to) {

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

import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

/**
 * A filter that restricts search results to values that are within the given numeric range.
 *
 * <p>Uses the field data cache (loading all the values for the specified field into memory).
 *
 * @author kimchy (shay.banon)
 */
public class NumericRangeFilterBuilder extends BaseFilterBuilder {

    private final String name;

    private Object from;

    private Object to;

    private boolean includeLower = true;

    private boolean includeUpper = true;

    private Boolean cache;
    private String cacheKey;

    private String filterName;

    /**
     * A filter that restricts search results to values that are within the given range.
     *
     * @param name The field name
     */
    public NumericRangeFilterBuilder(String name) {
        this.name = name;
    }

    /**
     * The from part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder from(Object from) {
        this.from = from;
        return this;
    }

    /**
     * The from part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder from(int from) {
        this.from = from;
        return this;
    }

    /**
     * The from part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder from(long from) {
        this.from = from;
        return this;
    }

    /**
     * The from part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder from(float from) {
        this.from = from;
        return this;
    }

    /**
     * The from part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder from(double from) {
        this.from = from;
        return this;
    }

    /**
     * The from part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder gt(Object from) {
        this.from = from;
        this.includeLower = false;
        return this;
    }

    /**
     * The from part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder gt(int from) {
        this.from = from;
        this.includeLower = false;
        return this;
    }

    /**
     * The from part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder gt(long from) {
        this.from = from;
        this.includeLower = false;
        return this;
    }

    /**
     * The from part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder gt(float from) {
        this.from = from;
        this.includeLower = false;
        return this;
    }

    /**
     * The from part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder gt(double from) {
        this.from = from;
        this.includeLower = false;
        return this;
    }

    /**
     * The from part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder gte(Object from) {
        this.from = from;
        this.includeLower = true;
        return this;
    }

    /**
     * The from part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder gte(int from) {
        this.from = from;
        this.includeLower = true;
        return this;
    }

    /**
     * The from part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder gte(long from) {
        this.from = from;
        this.includeLower = true;
        return this;
    }

    /**
     * The from part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder gte(float from) {
        this.from = from;
        this.includeLower = true;
        return this;
    }

    /**
     * The from part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder gte(double from) {
        this.from = from;
        this.includeLower = true;
        return this;
    }

    /**
     * The to part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder to(Object to) {
        this.to = to;
        return this;
    }

    /**
     * The to part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder to(int to) {
        this.to = to;
        return this;
    }

    /**
     * The to part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder to(long to) {
        this.to = to;
        return this;
    }

    /**
     * The to part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder to(float to) {
        this.to = to;
        return this;
    }

    /**
     * The to part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder to(double to) {
        this.to = to;
        return this;
    }

    /**
     * The to part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder lt(Object to) {
        this.to = to;
        this.includeUpper = false;
        return this;
    }

    /**
     * The to part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder lt(int to) {
        this.to = to;
        this.includeUpper = false;
        return this;
    }

    /**
     * The to part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder lt(long to) {
        this.to = to;
        this.includeUpper = false;
        return this;
    }

    /**
     * The to part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder lt(float to) {
        this.to = to;
        this.includeUpper = false;
        return this;
    }

    /**
     * The to part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder lt(double to) {
        this.to = to;
        this.includeUpper = false;
        return this;
    }

    /**
     * The to part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder lte(String to) {
        this.to = to;
        this.includeUpper = true;
        return this;
    }

    /**
     * The to part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder lte(int to) {
        this.to = to;
        this.includeUpper = true;
        return this;
    }

    /**
     * The to part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder lte(long to) {
        this.to = to;
        this.includeUpper = true;
        return this;
    }

    /**
     * The to part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder lte(float to) {
        this.to = to;
        this.includeUpper = true;
        return this;
    }

    /**
     * The to part of the filter query. Null indicates unbounded.
     */
    public NumericRangeFilterBuilder lte(double to) {
        this.to = to;
        this.includeUpper = true;
        return this;
    }

    /**
     * Should the lower bound be included or not. Defaults to <tt>true</tt>.
     */
    public NumericRangeFilterBuilder includeLower(boolean includeLower) {
        this.includeLower = includeLower;
        return this;
    }

    /**
     * Should the upper bound be included or not. Defaults to <tt>true</tt>.
     */
    public NumericRangeFilterBuilder includeUpper(boolean includeUpper) {
        this.includeUpper = includeUpper;
        return this;
    }

    /**
     * Sets the filter name for the filter that can be used when searching for matched_filters per hit.
     */
    public NumericRangeFilterBuilder filterName(String filterName) {
        this.filterName = filterName;
        return this;
    }

    /**
     * Should the filter be cached or not. Defaults to <tt>false</tt>.
     */
    public NumericRangeFilterBuilder cache(boolean cache) {
        this.cache = cache;
        return this;
    }

    public NumericRangeFilterBuilder cacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
        return this;
    }

    @Override protected void doXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(NumericRangeFilterParser.NAME);

        builder.startObject(name);
        builder.field("from", from);
        builder.field("to", to);
        builder.field("include_lower", includeLower);
        builder.field("include_upper", includeUpper);
        builder.endObject();

        if (filterName != null) {
            builder.field("_name", filterName);
        }
        if (cache != null) {
            builder.field("_cache", cache);
        }
        if (cacheKey != null) {
            builder.field("_cache_key", cacheKey);
        }

        builder.endObject();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3395.java