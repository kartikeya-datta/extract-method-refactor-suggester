error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3439.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3439.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3439.java
text:
```scala
public static final b@@oolean INDEX_CODEC_BLOOM_LOAD_DEFAULT = false;

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

package org.elasticsearch.index.codec;

import com.google.common.collect.ImmutableMap;
import org.apache.lucene.codecs.Codec;
import org.elasticsearch.ElasticsearchIllegalArgumentException;
import org.elasticsearch.common.collect.MapBuilder;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.codec.docvaluesformat.DocValuesFormatService;
import org.elasticsearch.index.codec.postingsformat.PostingsFormatService;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.settings.IndexSettings;

/**
 * Since Lucene 4.0 low level index segments are read and written through a
 * codec layer that allows to use use-case specific file formats &
 * data-structures per field. Elasticsearch exposes the full
 * {@link Codec} capabilities through this {@link CodecService}.
 *
 * @see PostingsFormatService
 * @see DocValuesFormatService
 */
public class CodecService extends AbstractIndexComponent {

    public static final String INDEX_CODEC_BLOOM_LOAD = "index.codec.bloom.load";
    public static final boolean INDEX_CODEC_BLOOM_LOAD_DEFAULT = true;

    private final PostingsFormatService postingsFormatService;
    private final DocValuesFormatService docValuesFormatService;
    private final MapperService mapperService;
    private final ImmutableMap<String, Codec> codecs;

    private volatile boolean loadBloomFilter = true;

    public final static String DEFAULT_CODEC = "default";

    public CodecService(Index index) {
        this(index, ImmutableSettings.Builder.EMPTY_SETTINGS);
    }

    public CodecService(Index index, @IndexSettings Settings indexSettings) {
        this(index, indexSettings, new PostingsFormatService(index, indexSettings), new DocValuesFormatService(index, indexSettings), null);
    }

    @Inject
    public CodecService(Index index, @IndexSettings Settings indexSettings, PostingsFormatService postingsFormatService,
                        DocValuesFormatService docValuesFormatService, MapperService mapperService) {
        super(index, indexSettings);
        this.postingsFormatService = postingsFormatService;
        this.docValuesFormatService = docValuesFormatService;
        this.mapperService = mapperService;
        MapBuilder<String, Codec> codecs = MapBuilder.<String, Codec>newMapBuilder();
        if (mapperService == null) {
            codecs.put(DEFAULT_CODEC, Codec.getDefault());
        } else {
            codecs.put(DEFAULT_CODEC, new PerFieldMappingPostingFormatCodec(mapperService,
                    postingsFormatService.get(PostingsFormatService.DEFAULT_FORMAT).get(),
                    docValuesFormatService.get(DocValuesFormatService.DEFAULT_FORMAT).get(), logger));
        }
        for (String codec : Codec.availableCodecs()) {
            codecs.put(codec, Codec.forName(codec));
        }
        this.codecs = codecs.immutableMap();
        this.loadBloomFilter = indexSettings.getAsBoolean(INDEX_CODEC_BLOOM_LOAD, INDEX_CODEC_BLOOM_LOAD_DEFAULT);
    }

    public PostingsFormatService postingsFormatService() {
        return this.postingsFormatService;
    }

    public DocValuesFormatService docValuesFormatService() {
        return docValuesFormatService;
    }

    public MapperService mapperService() {
        return mapperService;
    }

    public Codec codec(String name) throws ElasticsearchIllegalArgumentException {
        Codec codec = codecs.get(name);
        if (codec == null) {
            throw new ElasticsearchIllegalArgumentException("failed to find codec [" + name + "]");
        }
        return codec;
    }

    public boolean isLoadBloomFilter() {
        return this.loadBloomFilter;
    }

    public void setLoadBloomFilter(boolean loadBloomFilter) {
        this.loadBloomFilter = loadBloomFilter;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3439.java