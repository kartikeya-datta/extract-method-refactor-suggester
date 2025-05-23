error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2741.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2741.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2741.java
text:
```scala
b@@uilder.startObject(TermsFacet.TYPE);

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

package org.elasticsearch.search.facet.terms;

import org.elasticsearch.common.collect.Maps;
import org.elasticsearch.common.regex.Regex;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.xcontent.XContentFilterBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilderException;
import org.elasticsearch.search.facet.AbstractFacetBuilder;

import java.io.IOException;
import java.util.Map;

/**
 * Term facets allow to collect frequency of terms within one (or more) field.
 *
 * @author kimchy (shay.banon)
 */
public class TermsFacetBuilder extends AbstractFacetBuilder {
    private String fieldName;
    private String[] fieldsNames;
    private int size = 10;
    private String[] exclude;
    private String regex;
    private int regexFlags = 0;
    private TermsFacet.ComparatorType comparatorType;
    private String script;
    private String lang;
    private Map<String, Object> params;

    /**
     * Construct a new term facet with the provided facet name.
     *
     * @param name The facet name.
     */
    public TermsFacetBuilder(String name) {
        super(name);
    }

    /**
     * Should the fact run in global mode (not bounded by the search query) or not. Defaults
     * to <tt>false</tt>.
     */
    public TermsFacetBuilder global(boolean global) {
        super.global(global);
        return this;
    }

    /**
     * Marks the facet to run in a specific scope.
     */
    @Override public TermsFacetBuilder scope(String scope) {
        super.scope(scope);
        return this;
    }

    /**
     * An additional facet filter that will further filter the documents the facet will be
     * executed on.
     */
    public TermsFacetBuilder facetFilter(XContentFilterBuilder filter) {
        this.facetFilter = filter;
        return this;
    }

    /**
     * The field the terms will be collected from.
     */
    public TermsFacetBuilder field(String field) {
        this.fieldName = field;
        return this;
    }

    /**
     * The fields the terms will be collected from.
     */
    public TermsFacetBuilder fields(String... fields) {
        this.fieldsNames = fields;
        return this;
    }

    /**
     * Define a script field that will control the terms that will be used (and not filtered, as is the
     * case when the script is provided on top of field / fields).
     */
    public TermsFacetBuilder scriptField(String scriptField) {
        this.script = scriptField;
        return this;
    }

    /**
     * A set of terms that will be excluded.
     */
    public TermsFacetBuilder exclude(String... exclude) {
        this.exclude = exclude;
        return this;
    }

    /**
     * The number of terms (and frequencies) to return. Defaults to 10.
     */
    public TermsFacetBuilder size(int size) {
        this.size = size;
        return this;
    }

    /**
     * A regular expression to use in order to further filter terms.
     */
    public TermsFacetBuilder regex(String regex) {
        return regex(regex, 0);
    }

    /**
     * A regular expression (with flags) to use in order to further filter terms.
     */
    public TermsFacetBuilder regex(String regex, int flags) {
        this.regex = regex;
        this.regexFlags = flags;
        return this;
    }

    /**
     * The order by which to return the facets by. Defaults to {@link TermsFacet.ComparatorType#COUNT}.
     */
    public TermsFacetBuilder order(TermsFacet.ComparatorType comparatorType) {
        this.comparatorType = comparatorType;
        return this;
    }

    /**
     * A script allowing to either modify or ignore a provided term (can be accessed using <tt>term</tt> var).
     */
    public TermsFacetBuilder script(String script) {
        this.script = script;
        return this;
    }

    /**
     * The language of the script.
     */
    public TermsFacetBuilder lang(String lang) {
        this.lang = lang;
        return this;
    }

    /**
     * A parameter that will be passed to the script.
     *
     * @param name  The name of the script parameter.
     * @param value The value of the script parameter.
     */
    public TermsFacetBuilder param(String name, Object value) {
        if (params == null) {
            params = Maps.newHashMap();
        }
        params.put(name, value);
        return this;
    }

    @Override public void toXContent(XContentBuilder builder, Params params) throws IOException {
        if (fieldName == null && fieldsNames == null && script == null) {
            throw new SearchSourceBuilderException("field/fields/script must be set on terms facet for facet [" + name + "]");
        }
        builder.startObject(name);

        builder.startObject(TermsFacetCollectorParser.NAME);
        if (fieldsNames != null) {
            if (fieldsNames.length == 1) {
                builder.field("field", fieldsNames[0]);
            } else {
                builder.field("fields", fieldsNames);
            }
        } else if (fieldName != null) {
            builder.field("field", fieldName);
        }
        builder.field("size", size);
        if (exclude != null) {
            builder.startArray("exclude");
            for (String ex : exclude) {
                builder.value(ex);
            }
            builder.endArray();
        }
        if (regex != null) {
            builder.field("regex", regex);
            if (regexFlags != 0) {
                builder.field("regex_flags", Regex.flagsToString(regexFlags));
            }
        }
        if (comparatorType != null) {
            builder.field("order", comparatorType.name().toLowerCase());
        }

        if (script != null) {
            builder.field("script", script);
            if (lang != null) {
                builder.field("lang", lang);
            }
            if (this.params != null) {
                builder.field("params", this.params);
            }
        }

        builder.endObject();

        addFilterFacetAndGlobal(builder, params);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2741.java