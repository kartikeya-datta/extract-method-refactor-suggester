error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10330.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10330.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10330.java
text:
```scala
.@@put(Tuple.tuple("string", "paged_bytes"), new PagedBytesIndexFieldData.Builder())

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

package org.elasticsearch.index.fielddata;

import com.google.common.collect.ImmutableMap;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.common.collect.MapBuilder;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.concurrent.ConcurrentCollections;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.fielddata.plain.*;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.settings.IndexSettings;

import java.util.concurrent.ConcurrentMap;

/**
 */
public class IndexFieldDataService extends AbstractIndexComponent {

    private final static ImmutableMap<String, IndexFieldData.Builder> buildersByType;
    private final static ImmutableMap<Tuple<String, String>, IndexFieldData.Builder> buildersByTypeAndFormat;

    static {
        buildersByType = MapBuilder.<String, IndexFieldData.Builder>newMapBuilder()
                .put("string", new ConcreteBytesRefIndexFieldData.Builder())
                .put("float", new FloatArrayIndexFieldData.Builder())
                .put("double", new DoubleArrayIndexFieldData.Builder())
                .put("byte", new ByteArrayIndexFieldData.Builder())
                .put("short", new ShortArrayIndexFieldData.Builder())
                .put("int", new IntArrayIndexFieldData.Builder())
                .put("long", new LongArrayIndexFieldData.Builder())
                .put("geo_point", new GeoPointDoubleArrayIndexFieldData.Builder())
                .immutableMap();

        buildersByTypeAndFormat = MapBuilder.<Tuple<String, String>, IndexFieldData.Builder>newMapBuilder()
                .put(Tuple.tuple("string", "concrete_bytes"), new ConcreteBytesRefIndexFieldData.Builder())
                .put(Tuple.tuple("string", "paged_bytes"), new PagesBytesIndexFieldData.Builder())
                .put(Tuple.tuple("float", "array"), new FloatArrayIndexFieldData.Builder())
                .put(Tuple.tuple("double", "array"), new DoubleArrayIndexFieldData.Builder())
                .put(Tuple.tuple("byte", "array"), new ByteArrayIndexFieldData.Builder())
                .put(Tuple.tuple("short", "array"), new ShortArrayIndexFieldData.Builder())
                .put(Tuple.tuple("int", "array"), new IntArrayIndexFieldData.Builder())
                .put(Tuple.tuple("long", "array"), new LongArrayIndexFieldData.Builder())
                .put(Tuple.tuple("geo_point", "array"), new GeoPointDoubleArrayIndexFieldData.Builder())
                .immutableMap();
    }

    private final ConcurrentMap<String, IndexFieldData> loadedFieldData = ConcurrentCollections.newConcurrentMap();

    public IndexFieldDataService(Index index) {
        this(index, ImmutableSettings.Builder.EMPTY_SETTINGS);
    }

    @Inject
    public IndexFieldDataService(Index index, @IndexSettings Settings indexSettings) {
        super(index, indexSettings);
    }

    public void clear() {
        synchronized (loadedFieldData) {
            for (IndexFieldData fieldData : loadedFieldData.values()) {
                fieldData.clear();
            }
            loadedFieldData.clear();
        }
    }

    public void clearField(String fieldName) {
        synchronized (loadedFieldData) {
            IndexFieldData fieldData = loadedFieldData.remove(fieldName);
            if (fieldData != null) {
                fieldData.clear();
            }
        }
    }

    public <IFD extends IndexFieldData> IFD getForField(FieldMapper mapper) {
        return getForField(mapper.names(), mapper.fieldDataType2());
    }

    public <IFD extends IndexFieldData> IFD getForField(FieldMapper.Names fieldNames, FieldDataType type) {
        IndexFieldData fieldData = loadedFieldData.get(type.getType());
        if (fieldData == null) {
            synchronized (loadedFieldData) {
                fieldData = loadedFieldData.get(type.getType());
                if (fieldData == null) {
                    IndexFieldData.Builder builder = null;
                    if (type.getFormat() != null) {
                        builder = buildersByTypeAndFormat.get(Tuple.tuple(type.getType(), type.getFormat()));
                    }
                    if (builder == null) {
                        builder = buildersByType.get(type.getType());
                    }
                    if (builder == null) {
                        throw new ElasticSearchIllegalArgumentException("failed to find field data builder for field " + fieldNames.fullName() + ", and type " + type);
                    }

                    IndexFieldDataCache cache;
                    if (type.getOptions().containsKey("cache")) {
                        String cacheType = type.getOptions().get("cache");
                        if ("resident".equals(cacheType)) {
                            cache = new IndexFieldDataCache.Resident();
                        } else if ("soft".equals(cacheType)) {
                            cache = new IndexFieldDataCache.Soft();
                        } else {
                            throw new ElasticSearchIllegalArgumentException("cache type not supported [" + cacheType + "] for field [" + fieldNames.fullName() + "]");
                        }
                    } else {
                        cache = new IndexFieldDataCache.Resident();
                    }

                    fieldData = builder.build(index, indexSettings, fieldNames, type, cache);
                    loadedFieldData.put(fieldNames.indexName(), fieldData);
                }
            }
        }
        return (IFD) fieldData;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10330.java