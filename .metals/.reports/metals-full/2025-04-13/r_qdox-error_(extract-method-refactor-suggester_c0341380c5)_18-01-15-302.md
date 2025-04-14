error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9992.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9992.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[22,1]

error in qdox parser
file content:
```java
offset: 858
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9992.java
text:
```scala
public interface FieldMapper<T> extends Mapper {

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

p@@ackage org.elasticsearch.index.mapper;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.index.codec.docvaluesformat.DocValuesFormatProvider;
import org.elasticsearch.index.codec.postingsformat.PostingsFormatProvider;
import org.elasticsearch.index.fielddata.FieldDataType;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.index.similarity.SimilarityProvider;

import java.util.List;

/**
 *
 */
public interface FieldMapper<T> {

    public static final String DOC_VALUES_FORMAT = "doc_values_format";

    public static class Names {

        private final String name;

        private final String indexName;

        private final String indexNameClean;

        private final String fullName;

        private final String sourcePath;

        public Names(String name) {
            this(name, name, name, name);
        }

        public Names(String name, String indexName, String indexNameClean, String fullName) {
            this(name, indexName, indexNameClean, fullName, fullName);
        }

        public Names(String name, String indexName, String indexNameClean, String fullName, @Nullable String sourcePath) {
            this.name = name.intern();
            this.indexName = indexName.intern();
            this.indexNameClean = indexNameClean.intern();
            this.fullName = fullName.intern();
            this.sourcePath = sourcePath == null ? this.fullName : sourcePath.intern();
        }

        /**
         * The logical name of the field.
         */
        public String name() {
            return name;
        }

        /**
         * The indexed name of the field. This is the name under which we will
         * store it in the index.
         */
        public String indexName() {
            return indexName;
        }

        /**
         * The cleaned index name, before any "path" modifications performed on it.
         */
        public String indexNameClean() {
            return indexNameClean;
        }

        /**
         * The full name, including dot path.
         */
        public String fullName() {
            return fullName;
        }

        /**
         * The dot path notation to extract the value from source.
         */
        public String sourcePath() {
            return sourcePath;
        }

        /**
         * Creates a new index term based on the provided value.
         */
        public Term createIndexNameTerm(String value) {
            return new Term(indexName, value);
        }

        /**
         * Creates a new index term based on the provided value.
         */
        public Term createIndexNameTerm(BytesRef value) {
            return new Term(indexName, value);
        }
    }

    Names names();

    FieldType fieldType();

    float boost();

    /**
     * The analyzer that will be used to index the field.
     */
    Analyzer indexAnalyzer();

    /**
     * The analyzer that will be used to search the field.
     */
    Analyzer searchAnalyzer();

    /**
     * The analyzer that will be used for quoted search on the field.
     */
    Analyzer searchQuoteAnalyzer();

    /**
     * Similarity used for scoring queries on the field
     */
    SimilarityProvider similarity();

    /**
     * Returns the actual value of the field.
     */
    T value(Object value);

    /**
     * Returns the value that will be used as a result for search. Can be only of specific types... .
     */
    Object valueForSearch(Object value);

    /**
     * Returns the indexed value used to construct search "values".
     */
    BytesRef indexedValueForSearch(Object value);

    /**
     * Should the field query {@link #termQuery(Object, org.elasticsearch.index.query.QueryParseContext)}  be used when detecting this
     * field in query string.
     */
    boolean useTermQueryWithQueryString();

    Query termQuery(Object value, @Nullable QueryParseContext context);

    Filter termFilter(Object value, @Nullable QueryParseContext context);

    Filter termsFilter(List values, @Nullable QueryParseContext context);

    Query rangeQuery(Object lowerTerm, Object upperTerm, boolean includeLower, boolean includeUpper, @Nullable QueryParseContext context);

    Filter rangeFilter(Object lowerTerm, Object upperTerm, boolean includeLower, boolean includeUpper, @Nullable QueryParseContext context);

    Query fuzzyQuery(String value, String minSim, int prefixLength, int maxExpansions, boolean transpositions);

    Query prefixQuery(Object value, @Nullable MultiTermQuery.RewriteMethod method, @Nullable QueryParseContext context);

    Filter prefixFilter(Object value, @Nullable QueryParseContext context);

    Query regexpQuery(Object value, int flags, @Nullable MultiTermQuery.RewriteMethod method, @Nullable QueryParseContext context);

    Filter regexpFilter(Object value, int flags, @Nullable QueryParseContext parseContext);

    /**
     * A term query to use when parsing a query string. Can return <tt>null</tt>.
     */
    @Nullable
    Query queryStringTermQuery(Term term);

    /**
     * Null value filter, returns <tt>null</tt> if there is no null value associated with the field.
     */
    @Nullable
    Filter nullValueFilter();

    FieldDataType fieldDataType();

    PostingsFormatProvider postingsFormatProvider();

    DocValuesFormatProvider docValuesFormatProvider();

    boolean isNumeric();

    boolean isSortable();

    boolean hasDocValues();
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9992.java