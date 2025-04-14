error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/324.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/324.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/324.java
text:
```scala
I@@mmutableMap<String, Object> meta();

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

package org.elasticsearch.index.mapper;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.search.Filter;
import org.elasticsearch.common.collect.ImmutableMap;
import org.elasticsearch.common.compress.CompressedString;
import org.elasticsearch.common.util.concurrent.ThreadSafe;

import javax.annotation.Nullable;

/**
 * @author kimchy (shay.banon)
 */
@ThreadSafe
public interface DocumentMapper {

    String type();

    /**
     * When constructed by parsing a mapping definition, will return it. Otherwise,
     * returns <tt>null</tt>.
     */
    CompressedString mappingSource();

    /**
     * Attributes of this type mappings.
     */
    ImmutableMap<String, Object> attributes();

    /**
     * Generates the source of the mapper based on the current mappings.
     */
    void refreshSource() throws FailedToGenerateSourceMapperException;

    UidFieldMapper uidMapper();

    IdFieldMapper idMapper();

    TypeFieldMapper typeMapper();

    IndexFieldMapper indexMapper();

    SourceFieldMapper sourceMapper();

    BoostFieldMapper boostMapper();

    AllFieldMapper allFieldMapper();

    DocumentFieldMappers mappers();

    /**
     * The default index analyzer to be used. Note, the {@link DocumentFieldMappers#indexAnalyzer()} should
     * probably be used instead.
     */
    Analyzer indexAnalyzer();

    /**
     * The default search analyzer to be used. Note, the {@link DocumentFieldMappers#searchAnalyzer()} should
     * probably be used instead.
     */
    Analyzer searchAnalyzer();

    /**
     * A filter based on the type of the field.
     */
    Filter typeFilter();

    /**
     * Parses the source into a parsed document.
     *
     * <p>Validates that the source has the provided id and type. Note, most times
     * we will already have the id and the type even though they exist in the source as well.
     */
    ParsedDocument parse(byte[] source) throws MapperParsingException;

    /**
     * Parses the source into a parsed document.
     *
     * <p>Validates that the source has the provided id and type. Note, most times
     * we will already have the id and the type even though they exist in the source as well.
     */
    ParsedDocument parse(String type, String id, byte[] source) throws MapperParsingException;

    /**
     * Parses the source into a parsed document.
     *
     * <p>Validates that the source has the provided id and type. Note, most times
     * we will already have the id and the type even though they exist in the source as well.
     */
    ParsedDocument parse(SourceToParse source) throws MapperParsingException;

    /**
     * Parses the source into a parsed document.
     *
     * <p>Validates that the source has the provided id and type. Note, most times
     * we will already have the id and the type even though they exist in the source as well.
     */
    ParsedDocument parse(SourceToParse source, @Nullable ParseListener listener) throws MapperParsingException;

    /**
     * Merges this document mapper with the provided document mapper. If there are conflicts, the
     * {@link MergeResult} will hold them.
     */
    MergeResult merge(DocumentMapper mergeWith, MergeFlags mergeFlags) throws MergeMappingException;

    /**
     * Adds a field mapper listener.
     */
    void addFieldMapperListener(FieldMapperListener fieldMapperListener, boolean includeExisting);

    /**
     * A result of a merge.
     */
    public static class MergeResult {

        private final String[] conflicts;

        public MergeResult(String[] conflicts) {
            this.conflicts = conflicts;
        }

        /**
         * Does the merge have conflicts or not?
         */
        public boolean hasConflicts() {
            return conflicts.length > 0;
        }

        /**
         * The merge conflicts.
         */
        public String[] conflicts() {
            return this.conflicts;
        }
    }

    public static class MergeFlags {

        public static MergeFlags mergeFlags() {
            return new MergeFlags();
        }

        private boolean simulate = true;

        public MergeFlags() {
        }

        /**
         * A simulation run, don't perform actual modifications to the mapping.
         */
        public boolean simulate() {
            return simulate;
        }

        public MergeFlags simulate(boolean simulate) {
            this.simulate = simulate;
            return this;
        }
    }

    /**
     * A listener to be called during the parse process.
     */
    public static interface ParseListener<ParseContext> {

        public static final ParseListener EMPTY = new ParseListenerAdapter();

        /**
         * Called before a field is added to the document. Return <tt>true</tt> to include
         * it in the document.
         */
        boolean beforeFieldAdded(FieldMapper fieldMapper, Fieldable fieldable, ParseContext parseContent);
    }

    public static class ParseListenerAdapter implements ParseListener {

        @Override public boolean beforeFieldAdded(FieldMapper fieldMapper, Fieldable fieldable, Object parseContext) {
            return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/324.java