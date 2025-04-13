error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6445.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6445.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6445.java
text:
```scala
public l@@ong ramBytesUsed() {

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

package org.elasticsearch.index.fielddata.plain;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.fielddata.*;
import org.elasticsearch.index.fielddata.fieldcomparator.BytesRefFieldComparatorSource;
import org.elasticsearch.index.fielddata.ordinals.GlobalOrdinalsBuilder;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.indices.fielddata.breaker.CircuitBreakerService;
import org.elasticsearch.search.MultiValueMode;

public class IndexIndexFieldData implements IndexFieldData.WithOrdinals<AtomicFieldData.WithOrdinals<ScriptDocValues>> {

    public static class Builder implements IndexFieldData.Builder {

        @Override
        public IndexFieldData<?> build(Index index, Settings indexSettings, FieldMapper<?> mapper, IndexFieldDataCache cache,
                CircuitBreakerService breakerService, MapperService mapperService, GlobalOrdinalsBuilder globalOrdinalBuilder) {
            return new IndexIndexFieldData(index, mapper.names());
        }

    }

    private static class IndexBytesValues extends BytesValues.WithOrdinals {

        private final BytesRef scratch;

        protected IndexBytesValues(String index) {
            super(false);
            scratch = new BytesRef();
            scratch.copyChars(index);
        }

        @Override
        public int setDocument(int docId) {
            return 1;
        }

        @Override
        public long nextOrd() {
            return BytesValues.WithOrdinals.MIN_ORDINAL;
        }

        @Override
        public long getOrd(int docId) {
            return BytesValues.WithOrdinals.MIN_ORDINAL;
        }

        @Override
        public long getMaxOrd() {
            return 1;
        }

        @Override
        public BytesRef getValueByOrd(long ord) {
            return scratch;
        }

    }

    private static class IndexAtomicFieldData implements AtomicFieldData.WithOrdinals<ScriptDocValues> {

        private final String index;

        IndexAtomicFieldData(String index) {
            this.index = index;
        }

        @Override
        public long getMemorySizeInBytes() {
            return 0;
        }

        @Override
        public BytesValues.WithOrdinals getBytesValues() {
            return new IndexBytesValues(index);
        }

        @Override
        public ScriptDocValues getScriptValues() {
            return new ScriptDocValues.Strings(getBytesValues());
        }

        @Override
        public void close() {
        }

    }

    private final FieldMapper.Names names;
    private final Index index;

    private IndexIndexFieldData(Index index, FieldMapper.Names names) {
        this.index = index;
        this.names = names;
    }

    @Override
    public Index index() {
        return index;
    }

    @Override
    public org.elasticsearch.index.mapper.FieldMapper.Names getFieldNames() {
        return names;
    }

    @Override
    public FieldDataType getFieldDataType() {
        return new FieldDataType("string");
    }

    @Override
    public boolean valuesOrdered() {
        return true;
    }

    @Override
    public org.elasticsearch.index.fielddata.IndexFieldData.XFieldComparatorSource comparatorSource(Object missingValue,
            MultiValueMode sortMode) {
        return new BytesRefFieldComparatorSource(this, missingValue, sortMode);
    }

    @Override
    public void clear() {
    }

    @Override
    public void clear(IndexReader reader) {
    }

    @Override
    public AtomicFieldData.WithOrdinals<ScriptDocValues> load(AtomicReaderContext context) {
        return new IndexAtomicFieldData(index().name());
    }

    @Override
    public AtomicFieldData.WithOrdinals<ScriptDocValues> loadDirect(AtomicReaderContext context)
            throws Exception {
        return load(context);
    }

    @Override
    public IndexFieldData.WithOrdinals<?> loadGlobal(IndexReader indexReader) {
        return this;
    }

    @Override
    public IndexFieldData.WithOrdinals<?> localGlobalDirect(IndexReader indexReader) throws Exception {
        return loadGlobal(indexReader);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6445.java