error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4325.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4325.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4325.java
text:
```scala
t@@his.documentParser = new XContentDocumentMapperParser(index, indexSettings, analysisService);

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
import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.collect.ImmutableMap;
import org.elasticsearch.common.collect.UnmodifiableIterator;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.Streams;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.concurrent.ThreadSafe;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.FailedToResolveConfigException;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AnalysisService;
import org.elasticsearch.index.mapper.xcontent.XContentDocumentMapperParser;
import org.elasticsearch.index.settings.IndexSettings;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import static org.elasticsearch.common.collect.MapBuilder.*;

/**
 * @author kimchy (shay.banon)
 */
@ThreadSafe
public class MapperService extends AbstractIndexComponent implements Iterable<DocumentMapper> {

    public static final String DEFAULT_MAPPING = "_default_";

    /**
     * Will create types automatically if they do not exists in the repo yet
     */
    private final boolean dynamic;

    private volatile String defaultMappingSource;

    private volatile ImmutableMap<String, DocumentMapper> mappers = ImmutableMap.of();

    private final Object mutex = new Object();

    private volatile ImmutableMap<String, FieldMappers> nameFieldMappers = ImmutableMap.of();
    private volatile ImmutableMap<String, FieldMappers> indexNameFieldMappers = ImmutableMap.of();
    private volatile ImmutableMap<String, FieldMappers> fullNameFieldMappers = ImmutableMap.of();
    private volatile FieldMappers idFieldMappers = new FieldMappers();
    private volatile FieldMappers typeFieldMappers = new FieldMappers();
    private volatile FieldMappers uidFieldMappers = new FieldMappers();
    private volatile FieldMappers sourceFieldMappers = new FieldMappers();

    // for now, just use the xcontent one. Can work on it more to support custom ones
    private final DocumentMapperParser documentParser;

    private final InternalFieldMapperListener fieldMapperListener = new InternalFieldMapperListener();

    private final SmartIndexNameSearchAnalyzer searchAnalyzer;

    @Inject public MapperService(Index index, @IndexSettings Settings indexSettings, Environment environment, AnalysisService analysisService) {
        super(index, indexSettings);
        this.documentParser = new XContentDocumentMapperParser(analysisService);
        this.searchAnalyzer = new SmartIndexNameSearchAnalyzer(analysisService.defaultSearchAnalyzer());

        this.dynamic = componentSettings.getAsBoolean("dynamic", true);
        String defaultMappingLocation = componentSettings.get("default_mapping_location");
        URL defaultMappingUrl;
        if (defaultMappingLocation == null) {
            try {
                defaultMappingUrl = environment.resolveConfig("default-mapping.json");
            } catch (FailedToResolveConfigException e) {
                // not there, default to the built in one
                defaultMappingUrl = indexSettings.getClassLoader().getResource("org/elasticsearch/index/mapper/xcontent/default-mapping.json");
            }
        } else {
            try {
                defaultMappingUrl = environment.resolveConfig(defaultMappingLocation);
            } catch (FailedToResolveConfigException e) {
                // not there, default to the built in one
                try {
                    defaultMappingUrl = new File(defaultMappingLocation).toURI().toURL();
                } catch (MalformedURLException e1) {
                    throw new FailedToResolveConfigException("Failed to resolve dynamic mapping location [" + defaultMappingLocation + "]");
                }
            }
        }

        try {
            defaultMappingSource = Streams.copyToString(new InputStreamReader(defaultMappingUrl.openStream(), "UTF-8"));
        } catch (IOException e) {
            throw new MapperException("Failed to load default mapping source from [" + defaultMappingLocation + "]", e);
        }

        logger.debug("using dynamic[{}], default mapping: location[{}] and source[{}]", dynamic, defaultMappingLocation, defaultMappingSource);
    }

    @Override public UnmodifiableIterator<DocumentMapper> iterator() {
        return mappers.values().iterator();
    }

    public DocumentMapper type(String type) {
        DocumentMapper mapper = mappers.get(type);
        if (mapper != null) {
            return mapper;
        }
        if (!dynamic) {
            return null;
        }
        // go ahead and dynamically create it
        synchronized (mutex) {
            mapper = mappers.get(type);
            if (mapper != null) {
                return mapper;
            }
            add(type, null);
            return mappers.get(type);
        }
    }

    public DocumentMapperParser documentMapperParser() {
        return this.documentParser;
    }

    public void add(String type, String mappingSource) {
        if (DEFAULT_MAPPING.equals(type)) {
            // verify we can parse it
            documentParser.parse(type, mappingSource);
            defaultMappingSource = mappingSource;
        } else {
            add(parse(type, mappingSource));
        }
    }

    private void add(DocumentMapper mapper) {
        synchronized (mutex) {
            if (mapper.type().charAt(0) == '_') {
                throw new InvalidTypeNameException("Document mapping type name can't start with '_'");
            }
            mappers = newMapBuilder(mappers).put(mapper.type(), mapper).immutableMap();
            mapper.addFieldMapperListener(fieldMapperListener, true);
        }
    }

    /**
     * Just parses and returns the mapper without adding it.
     */
    public DocumentMapper parse(String mappingType, String mappingSource) throws MapperParsingException {
        return documentParser.parse(mappingType, mappingSource, defaultMappingSource);
    }

    public boolean hasMapping(String mappingType) {
        return mappers.containsKey(mappingType);
    }

    public DocumentMapper documentMapper(String type) {
        return mappers.get(type);
    }

    public FieldMappers idFieldMappers() {
        return this.idFieldMappers;
    }

    public FieldMappers typeFieldMappers() {
        return this.typeFieldMappers;
    }

    public FieldMappers sourceFieldMappers() {
        return this.sourceFieldMappers;
    }

    public FieldMappers uidFieldMappers() {
        return this.uidFieldMappers;
    }

    /**
     * Returns {@link FieldMappers} for all the {@link FieldMapper}s that are registered
     * under the given name across all the different {@link DocumentMapper} types.
     *
     * @param name The name to return all the {@link FieldMappers} for across all {@link DocumentMapper}s.
     * @return All the {@link FieldMappers} for across all {@link DocumentMapper}s
     */
    public FieldMappers name(String name) {
        return nameFieldMappers.get(name);
    }

    /**
     * Returns {@link FieldMappers} for all the {@link FieldMapper}s that are registered
     * under the given indexName across all the different {@link DocumentMapper} types.
     *
     * @param indexName The indexName to return all the {@link FieldMappers} for across all {@link DocumentMapper}s.
     * @return All the {@link FieldMappers} across all {@link DocumentMapper}s for the given indexName.
     */
    public FieldMappers indexName(String indexName) {
        return indexNameFieldMappers.get(indexName);
    }

    /**
     * Returns the {@link FieldMappers} of all the {@link FieldMapper}s that are
     * registered under the give fullName across all the different {@link DocumentMapper} types.
     *
     * @param fullName The full name
     * @return All teh {@link FieldMappers} across all the {@link DocumentMapper}s for the given fullName.
     */
    public FieldMappers fullName(String fullName) {
        return fullNameFieldMappers.get(fullName);
    }

    /**
     * Same as {@link #smartNameFieldMappers(String)} but returns the first field mapper for it. Returns
     * <tt>null</tt> if there is none.
     */
    public FieldMapper smartNameFieldMapper(String smartName) {
        FieldMappers fieldMappers = smartNameFieldMappers(smartName);
        if (fieldMappers != null) {
            return fieldMappers.mapper();
        }
        return null;
    }

    /**
     * Same as {@link #smartName(String)}, except it returns just the field mappers.
     */
    public FieldMappers smartNameFieldMappers(String smartName) {
        int dotIndex = smartName.indexOf('.');
        if (dotIndex != -1) {
            String possibleType = smartName.substring(0, dotIndex);
            DocumentMapper possibleDocMapper = mappers.get(possibleType);
            if (possibleDocMapper != null) {
                String possibleName = smartName.substring(dotIndex + 1);
                FieldMappers mappers = possibleDocMapper.mappers().smartName(possibleName);
                if (mappers != null) {
                    return mappers;
                }
            }
        }
        FieldMappers mappers = fullName(smartName);
        if (mappers != null) {
            return mappers;
        }
        return indexName(smartName);
    }

    /**
     * Returns smart field mappers based on a smart name. A smart name is one that can optioannly be prefixed
     * with a type (and then a '.'). If it is, then the {@link MapperService.SmartNameFieldMappers}
     * will have the doc mapper set.
     *
     * <p>It also (without the optional type prefix) try and find the {@link FieldMappers} for the specific
     * name. It will first try to find it based on the full name (with the dots if its a compound name). If
     * it is not found, will try and find it based on the indexName (which can be controlled in the mapping).
     *
     * <p>If nothing is found, returns null.
     */
    public SmartNameFieldMappers smartName(String smartName) {
        int dotIndex = smartName.indexOf('.');
        if (dotIndex != -1) {
            String possibleType = smartName.substring(0, dotIndex);
            DocumentMapper possibleDocMapper = mappers.get(possibleType);
            if (possibleDocMapper != null) {
                String possibleName = smartName.substring(dotIndex + 1);
                FieldMappers mappers = possibleDocMapper.mappers().smartName(possibleName);
                if (mappers != null) {
                    return new SmartNameFieldMappers(mappers, possibleDocMapper);
                }
            }
        }
        FieldMappers fieldMappers = fullName(smartName);
        if (fieldMappers != null) {
            return new SmartNameFieldMappers(fieldMappers, null);
        }
        fieldMappers = indexName(smartName);
        if (fieldMappers != null) {
            return new SmartNameFieldMappers(fieldMappers, null);
        }
        return null;
    }

    public Analyzer searchAnalyzer() {
        return this.searchAnalyzer;
    }

    public static class SmartNameFieldMappers {
        private final FieldMappers fieldMappers;
        private final DocumentMapper docMapper;

        public SmartNameFieldMappers(FieldMappers fieldMappers, @Nullable DocumentMapper docMapper) {
            this.fieldMappers = fieldMappers;
            this.docMapper = docMapper;
        }

        /**
         * Has at least one mapper for the field.
         */
        public boolean hasMapper() {
            return !fieldMappers.isEmpty();
        }

        /**
         * The first mapper for the smart named field.
         */
        public FieldMapper mapper() {
            return fieldMappers.mapper();
        }

        /**
         * All the field mappers for the smart name field.
         */
        public FieldMappers fieldMappers() {
            return fieldMappers;
        }

        /**
         * If the smart name was a typed field, with a type that we resolved, will return
         * <tt>true</tt>.
         */
        public boolean hasDocMapper() {
            return docMapper != null;
        }

        /**
         * If the smart name was a typed field, with a type that we resolved, will return
         * the document mapper for it.
         */
        public DocumentMapper docMapper() {
            return docMapper;
        }
    }

    private class SmartIndexNameSearchAnalyzer extends Analyzer {

        private final Analyzer defaultAnalyzer;

        private SmartIndexNameSearchAnalyzer(Analyzer defaultAnalyzer) {
            this.defaultAnalyzer = defaultAnalyzer;
        }

        @Override public TokenStream tokenStream(String fieldName, Reader reader) {
            int dotIndex = fieldName.indexOf('.');
            if (dotIndex != -1) {
                String possibleType = fieldName.substring(0, dotIndex);
                DocumentMapper possibleDocMapper = mappers.get(possibleType);
                if (possibleDocMapper != null) {
                    return possibleDocMapper.mappers().searchAnalyzer().tokenStream(fieldName, reader);
                }
            }
            FieldMappers mappers = fullNameFieldMappers.get(fieldName);
            if (mappers != null && mappers.mapper() != null && mappers.mapper().searchAnalyzer() != null) {
                return mappers.mapper().searchAnalyzer().tokenStream(fieldName, reader);
            }

            mappers = indexNameFieldMappers.get(fieldName);
            if (mappers != null && mappers.mapper() != null && mappers.mapper().searchAnalyzer() != null) {
                return mappers.mapper().searchAnalyzer().tokenStream(fieldName, reader);
            }
            return defaultAnalyzer.tokenStream(fieldName, reader);
        }

        @Override public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
            int dotIndex = fieldName.indexOf('.');
            if (dotIndex != -1) {
                String possibleType = fieldName.substring(0, dotIndex);
                DocumentMapper possibleDocMapper = mappers.get(possibleType);
                if (possibleDocMapper != null) {
                    return possibleDocMapper.mappers().searchAnalyzer().reusableTokenStream(fieldName, reader);
                }
            }
            FieldMappers mappers = fullNameFieldMappers.get(fieldName);
            if (mappers != null && mappers.mapper() != null && mappers.mapper().searchAnalyzer() != null) {
                return mappers.mapper().searchAnalyzer().reusableTokenStream(fieldName, reader);
            }

            mappers = indexNameFieldMappers.get(fieldName);
            if (mappers != null && mappers.mapper() != null && mappers.mapper().searchAnalyzer() != null) {
                return mappers.mapper().searchAnalyzer().reusableTokenStream(fieldName, reader);
            }
            return defaultAnalyzer.reusableTokenStream(fieldName, reader);
        }
    }

    private class InternalFieldMapperListener implements FieldMapperListener {
        @Override public void fieldMapper(FieldMapper fieldMapper) {
            synchronized (mutex) {
                if (fieldMapper instanceof IdFieldMapper) {
                    idFieldMappers = idFieldMappers.concat(fieldMapper);
                }
                if (fieldMapper instanceof TypeFieldMapper) {
                    typeFieldMappers = typeFieldMappers.concat(fieldMapper);
                }
                if (fieldMapper instanceof SourceFieldMapper) {
                    sourceFieldMappers = sourceFieldMappers.concat(fieldMapper);
                }
                if (fieldMapper instanceof UidFieldMapper) {
                    uidFieldMappers = uidFieldMappers.concat(fieldMapper);
                }


                FieldMappers mappers = nameFieldMappers.get(fieldMapper.names().name());
                if (mappers == null) {
                    mappers = new FieldMappers(fieldMapper);
                } else {
                    mappers = mappers.concat(fieldMapper);
                }

                nameFieldMappers = newMapBuilder(nameFieldMappers).put(fieldMapper.names().name(), mappers).immutableMap();

                mappers = indexNameFieldMappers.get(fieldMapper.names().indexName());
                if (mappers == null) {
                    mappers = new FieldMappers(fieldMapper);
                } else {
                    mappers = mappers.concat(fieldMapper);
                }
                indexNameFieldMappers = newMapBuilder(indexNameFieldMappers).put(fieldMapper.names().indexName(), mappers).immutableMap();

                mappers = fullNameFieldMappers.get(fieldMapper.names().indexName());
                if (mappers == null) {
                    mappers = new FieldMappers(fieldMapper);
                } else {
                    mappers = mappers.concat(fieldMapper);
                }
                fullNameFieldMappers = newMapBuilder(fullNameFieldMappers).put(fieldMapper.names().fullName(), mappers).immutableMap();
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4325.java