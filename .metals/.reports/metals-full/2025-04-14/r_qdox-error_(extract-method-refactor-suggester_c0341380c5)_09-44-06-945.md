error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7337.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7337.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7337.java
text:
```scala
private M@@ap<String, Float> fields = new HashMap<>();

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

import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * SimpleQuery is a query parser that acts similar to a query_string
 * query, but won't throw exceptions for any weird string syntax.
 */
public class SimpleQueryStringBuilder extends BaseQueryBuilder {
    private Map<String, Float> fields = new HashMap<String, Float>();
    private String analyzer;
    private Operator operator;
    private final String queryText;
    private int flags = -1;
    private Boolean lowercaseExpandedTerms;
    private Boolean lenient;
    private Locale locale;

    /**
     * Operators for the default_operator
     */
    public static enum Operator {
        AND,
        OR
    }

    /**
     * Construct a new simple query with the given text
     */
    public SimpleQueryStringBuilder(String text) {
        this.queryText = text;
    }

    /**
     * Add a field to run the query against
     */
    public SimpleQueryStringBuilder field(String field) {
        this.fields.put(field, null);
        return this;
    }

    /**
     * Add a field to run the query against with a specific boost
     */
    public SimpleQueryStringBuilder field(String field, float boost) {
        this.fields.put(field, boost);
        return this;
    }

    /**
     * Specify an analyzer to use for the query
     */
    public SimpleQueryStringBuilder analyzer(String analyzer) {
        this.analyzer = analyzer;
        return this;
    }

    /**
     * Specify the default operator for the query. Defaults to "OR" if no
     * operator is specified
     */
    public SimpleQueryStringBuilder defaultOperator(Operator defaultOperator) {
        this.operator = defaultOperator;
        return this;
    }

    /**
     * Specify the enabled features of the SimpleQueryString.
     */
    public SimpleQueryStringBuilder flags(SimpleQueryStringFlag... flags) {
        int value = 0;
        if (flags.length == 0) {
            value = SimpleQueryStringFlag.ALL.value;
        } else {
            for (SimpleQueryStringFlag flag : flags) {
                value |= flag.value;
            }
        }
        this.flags = value;
        return this;
    }

    public SimpleQueryStringBuilder lowercaseExpandedTerms(boolean lowercaseExpandedTerms) {
        this.lowercaseExpandedTerms = lowercaseExpandedTerms;
        return this;
    }

    public SimpleQueryStringBuilder locale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public SimpleQueryStringBuilder lenient(boolean lenient) {
        this.lenient = lenient;
        return this;
    }

    @Override
    public void doXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(SimpleQueryStringParser.NAME);

        builder.field("query", queryText);

        if (fields.size() > 0) {
            builder.startArray("fields");
            for (Map.Entry<String, Float> entry : fields.entrySet()) {
                String field = entry.getKey();
                Float boost = entry.getValue();
                if (boost != null) {
                    builder.value(field + "^" + boost);
                } else {
                    builder.value(field);
                }
            }
            builder.endArray();
        }

        if (flags != -1) {
            builder.field("flags", flags);
        }

        if (analyzer != null) {
            builder.field("analyzer", analyzer);
        }

        if (operator != null) {
            builder.field("default_operator", operator.name().toLowerCase(Locale.ROOT));
        }

        if (lowercaseExpandedTerms != null) {
            builder.field("lowercase_expanded_terms", lowercaseExpandedTerms);
        }

        if (lenient != null) {
            builder.field("lenient", lenient);
        }

        if (locale != null) {
            builder.field("locale", locale.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7337.java