error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1444.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1444.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1444.java
text:
```scala
b@@uilder.field("precision_step", precisionStep);

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

package org.elasticsearch.index.mapper.json;

import org.apache.lucene.analysis.NumericTokenStream;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.util.NumericUtils;
import org.elasticsearch.index.analysis.NamedAnalyzer;
import org.elasticsearch.util.ThreadLocals;
import org.elasticsearch.util.gnu.trove.TIntObjectHashMap;
import org.elasticsearch.util.json.JsonBuilder;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author kimchy (shay.banon)
 */
public abstract class JsonNumberFieldMapper<T extends Number> extends JsonFieldMapper<T> implements JsonIncludeInAllMapper {

    public static class Defaults extends JsonFieldMapper.Defaults {
        public static final int PRECISION_STEP = NumericUtils.PRECISION_STEP_DEFAULT;
        public static final Field.Index INDEX = Field.Index.NOT_ANALYZED;
        public static final boolean OMIT_NORMS = true;
        public static final boolean OMIT_TERM_FREQ_AND_POSITIONS = true;
    }

    public abstract static class Builder<T extends Builder, Y extends JsonNumberFieldMapper> extends JsonFieldMapper.Builder<T, Y> {

        protected int precisionStep = Defaults.PRECISION_STEP;

        public Builder(String name) {
            super(name);
            this.index = Defaults.INDEX;
            this.omitNorms = Defaults.OMIT_NORMS;
            this.omitTermFreqAndPositions = Defaults.OMIT_TERM_FREQ_AND_POSITIONS;
        }

        @Override public T store(Field.Store store) {
            return super.store(store);
        }

        @Override public T boost(float boost) {
            return super.boost(boost);
        }

        @Override public T indexName(String indexName) {
            return super.indexName(indexName);
        }

        @Override public T includeInAll(Boolean includeInAll) {
            return super.includeInAll(includeInAll);
        }

        public T precisionStep(int precisionStep) {
            this.precisionStep = precisionStep;
            return builder;
        }
    }

    private static final ThreadLocal<ThreadLocals.CleanableValue<TIntObjectHashMap<Deque<CachedNumericTokenStream>>>> cachedStreams = new ThreadLocal<ThreadLocals.CleanableValue<TIntObjectHashMap<Deque<CachedNumericTokenStream>>>>() {
        @Override protected ThreadLocals.CleanableValue<TIntObjectHashMap<Deque<CachedNumericTokenStream>>> initialValue() {
            return new ThreadLocals.CleanableValue<TIntObjectHashMap<Deque<CachedNumericTokenStream>>>(new TIntObjectHashMap<Deque<CachedNumericTokenStream>>());
        }
    };

    protected final int precisionStep;

    protected Boolean includeInAll;

    protected JsonNumberFieldMapper(Names names, int precisionStep,
                                    Field.Index index, Field.Store store,
                                    float boost, boolean omitNorms, boolean omitTermFreqAndPositions,
                                    NamedAnalyzer indexAnalyzer, NamedAnalyzer searchAnalyzer) {
        super(names, index, store, Field.TermVector.NO, boost, omitNorms, omitTermFreqAndPositions, indexAnalyzer, searchAnalyzer);
        if (precisionStep <= 0 || precisionStep >= maxPrecisionStep()) {
            this.precisionStep = Integer.MAX_VALUE;
        } else {
            this.precisionStep = precisionStep;
        }
    }

    @Override public void includeInAll(Boolean includeInAll) {
        if (includeInAll != null) {
            this.includeInAll = includeInAll;
        }
    }

    protected abstract int maxPrecisionStep();

    public int precisionStep() {
        return this.precisionStep;
    }

    /**
     * Use the field query created here when matching on numbers.
     */
    @Override public boolean useFieldQueryWithQueryString() {
        return true;
    }

    /**
     * Override the default behavior (to return the string, and return the actual Number instance).
     */
    @Override public Object valueForSearch(Fieldable field) {
        return value(field);
    }

    @Override public String valueAsString(Fieldable field) {
        Number num = value(field);
        return num == null ? null : num.toString();
    }

    @Override public abstract Object valueFromTerm(String term);

    @Override public abstract Object valueFromString(String text);

    /**
     * Breaks on this text if its <tt>null</tt>.
     */
    @Override public boolean shouldBreakTermEnumeration(Object text) {
        return text == null;
    }

    @Override protected void doJsonBody(JsonBuilder builder) throws IOException {
        super.doJsonBody(builder);
        builder.field("precisionStep", precisionStep);
    }

    @Override public abstract int sortType();

    /**
     * Removes a cached numeric token stream. The stream will be returned to the cahed once it is used
     * sicne it implements the end method.
     */
    protected CachedNumericTokenStream popCachedStream(int precisionStep) {
        Deque<CachedNumericTokenStream> deque = cachedStreams.get().get().get(precisionStep);
        if (deque == null) {
            deque = new ArrayDeque<CachedNumericTokenStream>();
            cachedStreams.get().get().put(precisionStep, deque);
            deque.add(new CachedNumericTokenStream(new NumericTokenStream(precisionStep), precisionStep));
        }
        if (deque.isEmpty()) {
            deque.add(new CachedNumericTokenStream(new NumericTokenStream(precisionStep), precisionStep));
        }
        return deque.pollFirst();
    }

    /**
     * A wrapper around a numeric stream allowing to reuse it by implementing the end method which returns
     * this stream back to the thread local cache.
     */
    protected static final class CachedNumericTokenStream extends TokenStream {

        private final int precisionStep;

        private final NumericTokenStream numericTokenStream;

        public CachedNumericTokenStream(NumericTokenStream numericTokenStream, int precisionStep) {
            super(numericTokenStream);
            this.numericTokenStream = numericTokenStream;
            this.precisionStep = precisionStep;
        }

        public void end() throws IOException {
            numericTokenStream.end();
        }

        /**
         * Close the input TokenStream.
         */
        public void close() throws IOException {
            numericTokenStream.close();
            cachedStreams.get().get().get(precisionStep).add(this);
        }

        /**
         * Reset the filter as well as the input TokenStream.
         */
        public void reset() throws IOException {
            numericTokenStream.reset();
        }

        @Override public boolean incrementToken() throws IOException {
            return numericTokenStream.incrementToken();
        }

        public CachedNumericTokenStream setIntValue(int value) {
            numericTokenStream.setIntValue(value);
            return this;
        }

        public CachedNumericTokenStream setLongValue(long value) {
            numericTokenStream.setLongValue(value);
            return this;
        }

        public CachedNumericTokenStream setFloatValue(float value) {
            numericTokenStream.setFloatValue(value);
            return this;
        }

        public CachedNumericTokenStream setDoubleValue(double value) {
            numericTokenStream.setDoubleValue(value);
            return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1444.java