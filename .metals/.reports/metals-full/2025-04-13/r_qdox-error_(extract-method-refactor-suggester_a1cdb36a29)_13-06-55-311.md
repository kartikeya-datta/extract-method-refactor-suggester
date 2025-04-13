error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1833.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1833.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1833.java
text:
```scala
private static final S@@tring ACTION_NAME = GetFieldMappingsAction.NAME + "[index]";

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

package org.elasticsearch.action.admin.indices.mapping.get;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsResponse.FieldMappingMetaData;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.single.custom.TransportSingleCustomOperationAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.routing.ShardsIterator;
import org.elasticsearch.common.collect.MapBuilder;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.regex.Regex;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.mapper.DocumentMapper;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.indices.TypeMissingException;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 */
public class TransportGetFieldMappingsIndexAction extends TransportSingleCustomOperationAction<GetFieldMappingsIndexRequest, GetFieldMappingsResponse> {

    private static final String ACTION_NAME = GetFieldMappingsAction.NAME + "/index";

    protected final ClusterService clusterService;
    private final IndicesService indicesService;

    @Inject
    public TransportGetFieldMappingsIndexAction(Settings settings, ClusterService clusterService,
                                                TransportService transportService,
                                                IndicesService indicesService,
                                                ThreadPool threadPool, ActionFilters actionFilters) {
        super(settings, ACTION_NAME, threadPool, clusterService, transportService, actionFilters);
        this.clusterService = clusterService;
        this.indicesService = indicesService;
    }

    @Override
    protected String executor() {
        return ThreadPool.Names.MANAGEMENT;
    }

    @Override
    protected ShardsIterator shards(ClusterState state, GetFieldMappingsIndexRequest request) {
        // Will balance requests between shards
        return state.routingTable().index(request.index()).randomAllActiveShardsIt();
    }

    @Override
    protected GetFieldMappingsResponse shardOperation(final GetFieldMappingsIndexRequest request, int shardId) throws ElasticsearchException {
        IndexService indexService = indicesService.indexServiceSafe(request.index());
        Collection<String> typeIntersection;
        if (request.types().length == 0) {
            typeIntersection = indexService.mapperService().types();

        } else {
            typeIntersection = Collections2.filter(indexService.mapperService().types(), new Predicate<String>() {

                @Override
                public boolean apply(String type) {
                    return Regex.simpleMatch(request.types(), type);
                }

            });
            if (typeIntersection.isEmpty()) {
                throw new TypeMissingException(new Index(request.index()), request.types());
            }
        }

        MapBuilder<String, ImmutableMap<String, FieldMappingMetaData>> typeMappings = new MapBuilder<>();
        for (String type : typeIntersection) {
            DocumentMapper documentMapper = indexService.mapperService().documentMapper(type);
            ImmutableMap<String, FieldMappingMetaData> fieldMapping = findFieldMappingsByType(documentMapper, request);
            if (!fieldMapping.isEmpty()) {
                typeMappings.put(type, fieldMapping);
            }
        }

        return new GetFieldMappingsResponse(ImmutableMap.of(request.index(), typeMappings.immutableMap()));
    }

    @Override
    protected GetFieldMappingsIndexRequest newRequest() {
        return new GetFieldMappingsIndexRequest();
    }

    @Override
    protected GetFieldMappingsResponse newResponse() {
        return new GetFieldMappingsResponse();
    }

    @Override
    protected ClusterBlockException checkGlobalBlock(ClusterState state, GetFieldMappingsIndexRequest request) {
        return state.blocks().globalBlockedException(ClusterBlockLevel.READ);
    }

    @Override
    protected ClusterBlockException checkRequestBlock(ClusterState state, GetFieldMappingsIndexRequest request) {
        return state.blocks().indexBlockedException(ClusterBlockLevel.READ, request.index());
    }

    private static final ToXContent.Params includeDefaultsParams = new ToXContent.Params() {

        final static String INCLUDE_DEFAULTS = "include_defaults";

        @Override
        public String param(String key) {
            if (INCLUDE_DEFAULTS.equals(key)) {
                return "true";
            }
            return null;
        }

        @Override
        public String param(String key, String defaultValue) {
            if (INCLUDE_DEFAULTS.equals(key)) {
                return "true";
            }
            return defaultValue;
        }

        @Override
        public boolean paramAsBoolean(String key, boolean defaultValue) {
            if (INCLUDE_DEFAULTS.equals(key)) {
                return true;
            }
            return defaultValue;
        }

        public Boolean paramAsBoolean(String key, Boolean defaultValue) {
            if (INCLUDE_DEFAULTS.equals(key)) {
                return true;
            }
            return defaultValue;
        }

        @Override
        @Deprecated
        public Boolean paramAsBooleanOptional(String key, Boolean defaultValue) {
            return paramAsBoolean(key, defaultValue);
        }
    };

    private ImmutableMap<String, FieldMappingMetaData> findFieldMappingsByType(DocumentMapper documentMapper, GetFieldMappingsIndexRequest request) throws ElasticsearchException {
        MapBuilder<String, FieldMappingMetaData> fieldMappings = new MapBuilder<>();
        final List<FieldMapper> allFieldMappers = documentMapper.mappers().mappers();
        for (String field : request.fields()) {
            if (Regex.isMatchAllPattern(field)) {
                for (FieldMapper fieldMapper : allFieldMappers) {
                    addFieldMapper(fieldMapper.names().fullName(), fieldMapper, fieldMappings, request.includeDefaults());
                }
            } else if (Regex.isSimpleMatchPattern(field)) {
                // go through the field mappers 3 times, to make sure we give preference to the resolve order: full name, index name, name.
                // also make sure we only store each mapper once.
                boolean[] resolved = new boolean[allFieldMappers.size()];
                for (int i = 0; i < allFieldMappers.size(); i++) {
                    FieldMapper fieldMapper = allFieldMappers.get(i);
                    if (Regex.simpleMatch(field, fieldMapper.names().fullName())) {
                        addFieldMapper(fieldMapper.names().fullName(), fieldMapper, fieldMappings, request.includeDefaults());
                        resolved[i] = true;
                    }
                }
                for (int i = 0; i < allFieldMappers.size(); i++) {
                    if (resolved[i]) {
                        continue;
                    }
                    FieldMapper fieldMapper = allFieldMappers.get(i);
                    if (Regex.simpleMatch(field, fieldMapper.names().indexName())) {
                        addFieldMapper(fieldMapper.names().indexName(), fieldMapper, fieldMappings, request.includeDefaults());
                        resolved[i] = true;
                    }
                }
                for (int i = 0; i < allFieldMappers.size(); i++) {
                    if (resolved[i]) {
                        continue;
                    }
                    FieldMapper fieldMapper = allFieldMappers.get(i);
                    if (Regex.simpleMatch(field, fieldMapper.names().name())) {
                        addFieldMapper(fieldMapper.names().name(), fieldMapper, fieldMappings, request.includeDefaults());
                        resolved[i] = true;
                    }
                }

            } else {
                // not a pattern
                FieldMapper fieldMapper = documentMapper.mappers().smartNameFieldMapper(field);
                if (fieldMapper != null) {
                    addFieldMapper(field, fieldMapper, fieldMappings, request.includeDefaults());
                } else if (request.probablySingleFieldRequest()) {
                    fieldMappings.put(field, FieldMappingMetaData.NULL);
                }
            }
        }
        return fieldMappings.immutableMap();
    }

    private void addFieldMapper(String field, FieldMapper fieldMapper, MapBuilder<String, FieldMappingMetaData> fieldMappings, boolean includeDefaults) {
        if (fieldMappings.containsKey(field)) {
            return;
        }
        try {
            XContentBuilder builder = XContentFactory.contentBuilder(XContentType.JSON);
            builder.startObject();
            fieldMapper.toXContent(builder, includeDefaults ? includeDefaultsParams : ToXContent.EMPTY_PARAMS);
            builder.endObject();
            fieldMappings.put(field, new FieldMappingMetaData(fieldMapper.names().fullName(), builder.bytes()));
        } catch (IOException e) {
            throw new ElasticsearchException("failed to serialize XContent of field [" + field + "]", e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1833.java