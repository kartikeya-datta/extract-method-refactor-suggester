error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5150.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5150.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5150.java
text:
```scala
i@@f (!docMapper.typeMapper().fieldType().indexed()) {

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

package org.elasticsearch.index.mapper;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.AnalyzerWrapper;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.FilterClause;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Filter;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.collect.MapBuilder;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.Streams;
import org.elasticsearch.common.lucene.search.TermFilter;
import org.elasticsearch.common.lucene.search.XBooleanFilter;
import org.elasticsearch.common.lucene.search.XTermsFilter;
import org.elasticsearch.common.regex.Regex;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.FailedToResolveConfigException;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AnalysisService;
import org.elasticsearch.index.codec.postingsformat.PostingsFormatService;
import org.elasticsearch.index.mapper.internal.TypeFieldMapper;
import org.elasticsearch.index.mapper.object.ObjectMapper;
import org.elasticsearch.index.search.nested.NonNestedDocsFilter;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.index.similarity.SimilarityLookupService;
import org.elasticsearch.indices.InvalidTypeNameException;
import org.elasticsearch.indices.TypeMissingException;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.elasticsearch.common.collect.MapBuilder.newMapBuilder;
import static org.elasticsearch.index.mapper.DocumentMapper.MergeFlags.mergeFlags;

/**
 *
 */
public class MapperService extends AbstractIndexComponent implements Iterable<DocumentMapper> {

    public static final String DEFAULT_MAPPING = "_default_";

    private final AnalysisService analysisService;
    private final PostingsFormatService postingsFormatService;

    /**
     * Will create types automatically if they do not exists in the mapping definition yet
     */
    private final boolean dynamic;

    private volatile String defaultMappingSource;

    private volatile Map<String, DocumentMapper> mappers = ImmutableMap.of();

    private final Object mutex = new Object();

    private volatile Map<String, FieldMappers> nameFieldMappers = ImmutableMap.of();
    private volatile Map<String, FieldMappers> indexNameFieldMappers = ImmutableMap.of();
    private volatile Map<String, FieldMappers> fullNameFieldMappers = ImmutableMap.of();
    private volatile Map<String, ObjectMappers> fullPathObjectMappers = ImmutableMap.of();
    private boolean hasNested = false; // updated dynamically to true when a nested object is added

    private final DocumentMapperParser documentParser;

    private final InternalFieldMapperListener fieldMapperListener = new InternalFieldMapperListener();
    private final InternalObjectMapperListener objectMapperListener = new InternalObjectMapperListener();

    private final SmartIndexNameSearchAnalyzer searchAnalyzer;
    private final SmartIndexNameSearchQuoteAnalyzer searchQuoteAnalyzer;

    @Inject
    public MapperService(Index index, @IndexSettings Settings indexSettings, Environment environment, AnalysisService analysisService,
                         PostingsFormatService postingsFormatService, SimilarityLookupService similarityLookupService) {
        super(index, indexSettings);
        this.analysisService = analysisService;
        this.postingsFormatService = postingsFormatService;
        this.documentParser = new DocumentMapperParser(index, indexSettings, analysisService, postingsFormatService, similarityLookupService);
        this.searchAnalyzer = new SmartIndexNameSearchAnalyzer(analysisService.defaultSearchAnalyzer());
        this.searchQuoteAnalyzer = new SmartIndexNameSearchQuoteAnalyzer(analysisService.defaultSearchQuoteAnalyzer());

        this.dynamic = componentSettings.getAsBoolean("dynamic", true);
        String defaultMappingLocation = componentSettings.get("default_mapping_location");
        URL defaultMappingUrl;
        if (defaultMappingLocation == null) {
            try {
                defaultMappingUrl = environment.resolveConfig("default-mapping.json");
            } catch (FailedToResolveConfigException e) {
                // not there, default to the built in one
                defaultMappingUrl = indexSettings.getClassLoader().getResource("org/elasticsearch/index/mapper/default-mapping.json");
                if (defaultMappingUrl == null) {
                    defaultMappingUrl = MapperService.class.getClassLoader().getResource("org/elasticsearch/index/mapper/default-mapping.json");
                }
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

        if (defaultMappingUrl == null) {
            logger.info("failed to find default-mapping.json in the classpath, using the default template");
            defaultMappingSource = "{\n" +
                    "    \"_default_\":{\n" +
                    "    }\n" +
                    "}";
        } else {
            try {
                defaultMappingSource = Streams.copyToString(new InputStreamReader(defaultMappingUrl.openStream(), Charsets.UTF_8));
            } catch (IOException e) {
                throw new MapperException("Failed to load default mapping source from [" + defaultMappingLocation + "]", e);
            }
        }

        logger.debug("using dynamic[{}], default mapping: default_mapping_location[{}], loaded_from[{}] and source[{}]", dynamic, defaultMappingLocation, defaultMappingUrl, defaultMappingSource);
    }

    public void close() {
        for (DocumentMapper documentMapper : mappers.values()) {
            documentMapper.close();
        }
    }

    public boolean hasNested() {
        return this.hasNested;
    }

    @Override
    public UnmodifiableIterator<DocumentMapper> iterator() {
        return Iterators.unmodifiableIterator(mappers.values().iterator());
    }

    public AnalysisService analysisService() {
        return this.analysisService;
    }

    public DocumentMapperParser documentMapperParser() {
        return this.documentParser;
    }

    public DocumentMapper merge(String type, String mappingSource, boolean applyDefault) {
        if (DEFAULT_MAPPING.equals(type)) {
            // verify we can parse it
            DocumentMapper mapper = documentParser.parse(type, mappingSource);
            // still add it as a document mapper so we have it registered and, for example, persisted back into
            // the cluster meta data if needed, or checked for existence
            synchronized (mutex) {
                mappers = newMapBuilder(mappers).put(type, mapper).map();
            }
            defaultMappingSource = mappingSource;
            return mapper;
        } else {
            return merge(parse(type, mappingSource, applyDefault));
        }
    }

    // never expose this to the outside world, we need to reparse the doc mapper so we get fresh
    // instances of field mappers to properly remove existing doc mapper
    private DocumentMapper merge(DocumentMapper mapper) {
        synchronized (mutex) {
            if (mapper.type().length() == 0) {
                throw new InvalidTypeNameException("mapping type name is empty");
            }
            if (mapper.type().charAt(0) == '_') {
                throw new InvalidTypeNameException("mapping type name [" + mapper.type() + "] can't start with '_'");
            }
            if (mapper.type().contains("#")) {
                throw new InvalidTypeNameException("mapping type name [" + mapper.type() + "] should not include '#' in it");
            }
            if (mapper.type().contains(",")) {
                throw new InvalidTypeNameException("mapping type name [" + mapper.type() + "] should not include ',' in it");
            }
            if (mapper.type().contains(".")) {
                logger.warn("Type [{}] contains a '.', it is recommended not to include it within a type name", mapper.type());
            }
            // we can add new field/object mappers while the old ones are there
            // since we get new instances of those, and when we remove, we remove
            // by instance equality
            DocumentMapper oldMapper = mappers.get(mapper.type());

            if (oldMapper != null) {
                DocumentMapper.MergeResult result = oldMapper.merge(mapper, mergeFlags().simulate(false));
                if (result.hasConflicts()) {
                    // TODO: What should we do???
                    if (logger.isDebugEnabled()) {
                        logger.debug("merging mapping for type [{}] resulted in conflicts: [{}]", mapper.type(), Arrays.toString(result.conflicts()));
                    }
                }
                return oldMapper;
            } else {
                FieldMapperListener.Aggregator fieldMappersAgg = new FieldMapperListener.Aggregator();
                mapper.traverse(fieldMappersAgg);
                addFieldMappers(fieldMappersAgg.mappers.toArray(new FieldMapper[fieldMappersAgg.mappers.size()]));
                mapper.addFieldMapperListener(fieldMapperListener, false);

                ObjectMapperListener.Aggregator objectMappersAgg = new ObjectMapperListener.Aggregator();
                mapper.traverse(objectMappersAgg);
                addObjectMappers(objectMappersAgg.mappers.toArray(new ObjectMapper[objectMappersAgg.mappers.size()]));
                mapper.addObjectMapperListener(objectMapperListener, false);

                mappers = newMapBuilder(mappers).put(mapper.type(), mapper).map();
                return mapper;
            }
        }
    }

    private void addObjectMappers(ObjectMapper[] objectMappers) {
        synchronized (mutex) {
            MapBuilder<String, ObjectMappers> fullPathObjectMappers = newMapBuilder(this.fullPathObjectMappers);
            for (ObjectMapper objectMapper : objectMappers) {
                ObjectMappers mappers = fullPathObjectMappers.get(objectMapper.fullPath());
                if (mappers == null) {
                    mappers = new ObjectMappers(objectMapper);
                } else {
                    mappers = mappers.concat(objectMapper);
                }
                fullPathObjectMappers.put(objectMapper.fullPath(), mappers);
                // update the hasNested flag
                if (objectMapper.nested().isNested()) {
                    hasNested = true;
                }
            }
            this.fullPathObjectMappers = fullPathObjectMappers.map();
        }
    }

    private void addFieldMappers(FieldMapper[] fieldMappers) {
        synchronized (mutex) {
            MapBuilder<String, FieldMappers> nameFieldMappers = newMapBuilder(this.nameFieldMappers);
            MapBuilder<String, FieldMappers> indexNameFieldMappers = newMapBuilder(this.indexNameFieldMappers);
            MapBuilder<String, FieldMappers> fullNameFieldMappers = newMapBuilder(this.fullNameFieldMappers);
            for (FieldMapper fieldMapper : fieldMappers) {
                FieldMappers mappers = nameFieldMappers.get(fieldMapper.names().name());
                if (mappers == null) {
                    mappers = new FieldMappers(fieldMapper);
                } else {
                    mappers = mappers.concat(fieldMapper);
                }
                nameFieldMappers.put(fieldMapper.names().name(), mappers);


                mappers = indexNameFieldMappers.get(fieldMapper.names().indexName());
                if (mappers == null) {
                    mappers = new FieldMappers(fieldMapper);
                } else {
                    mappers = mappers.concat(fieldMapper);
                }
                indexNameFieldMappers.put(fieldMapper.names().indexName(), mappers);


                mappers = fullNameFieldMappers.get(fieldMapper.names().fullName());
                if (mappers == null) {
                    mappers = new FieldMappers(fieldMapper);
                } else {
                    mappers = mappers.concat(fieldMapper);
                }
                fullNameFieldMappers.put(fieldMapper.names().fullName(), mappers);
            }

            this.nameFieldMappers = nameFieldMappers.map();
            this.indexNameFieldMappers = indexNameFieldMappers.map();
            this.fullNameFieldMappers = fullNameFieldMappers.map();
        }
    }

    public void remove(String type) {
        synchronized (mutex) {
            DocumentMapper docMapper = mappers.get(type);
            if (docMapper == null) {
                return;
            }
            docMapper.close();
            mappers = newMapBuilder(mappers).remove(type).map();
            removeObjectAndFieldMappers(docMapper);
        }
    }

    private void removeObjectAndFieldMappers(DocumentMapper docMapper) {
        // we need to remove those mappers
        MapBuilder<String, FieldMappers> nameFieldMappers = newMapBuilder(this.nameFieldMappers);
        MapBuilder<String, FieldMappers> indexNameFieldMappers = newMapBuilder(this.indexNameFieldMappers);
        MapBuilder<String, FieldMappers> fullNameFieldMappers = newMapBuilder(this.fullNameFieldMappers);

        for (FieldMapper mapper : docMapper.mappers()) {
            FieldMappers mappers = nameFieldMappers.get(mapper.names().name());
            if (mappers != null) {
                mappers = mappers.remove(mapper);
                if (mappers.isEmpty()) {
                    nameFieldMappers.remove(mapper.names().name());
                } else {
                    nameFieldMappers.put(mapper.names().name(), mappers);
                }
            }

            mappers = indexNameFieldMappers.get(mapper.names().indexName());
            if (mappers != null) {
                mappers = mappers.remove(mapper);
                if (mappers.isEmpty()) {
                    indexNameFieldMappers.remove(mapper.names().indexName());
                } else {
                    indexNameFieldMappers.put(mapper.names().indexName(), mappers);
                }
            }

            mappers = fullNameFieldMappers.get(mapper.names().fullName());
            if (mappers != null) {
                mappers = mappers.remove(mapper);
                if (mappers.isEmpty()) {
                    fullNameFieldMappers.remove(mapper.names().fullName());
                } else {
                    fullNameFieldMappers.put(mapper.names().fullName(), mappers);
                }
            }
        }
        this.nameFieldMappers = nameFieldMappers.map();
        this.indexNameFieldMappers = indexNameFieldMappers.map();
        this.fullNameFieldMappers = fullNameFieldMappers.map();

        MapBuilder<String, ObjectMappers> fullPathObjectMappers = newMapBuilder(this.fullPathObjectMappers);
        for (ObjectMapper mapper : docMapper.objectMappers().values()) {
            ObjectMappers mappers = fullPathObjectMappers.get(mapper.fullPath());
            if (mappers != null) {
                mappers = mappers.remove(mapper);
                if (mappers.isEmpty()) {
                    fullPathObjectMappers.remove(mapper.fullPath());
                } else {
                    fullPathObjectMappers.put(mapper.fullPath(), mappers);
                }
            }
        }

        this.fullPathObjectMappers = fullPathObjectMappers.map();
    }

    /**
     * Just parses and returns the mapper without adding it, while still applying default mapping.
     */
    public DocumentMapper parse(String mappingType, String mappingSource) throws MapperParsingException {
        return parse(mappingType, mappingSource, true);
    }

    public DocumentMapper parse(String mappingType, String mappingSource, boolean applyDefault) throws MapperParsingException {
        return documentParser.parse(mappingType, mappingSource, applyDefault ? defaultMappingSource : null);
    }

    public boolean hasMapping(String mappingType) {
        return mappers.containsKey(mappingType);
    }

    public Collection<String> types() {
        return mappers.keySet();
    }

    public DocumentMapper documentMapper(String type) {
        return mappers.get(type);
    }

    public DocumentMapper documentMapperWithAutoCreate(String type) {
        DocumentMapper mapper = mappers.get(type);
        if (mapper != null) {
            return mapper;
        }
        if (!dynamic) {
            throw new TypeMissingException(index, type, "trying to auto create mapping, but dynamic mapping is disabled");
        }
        // go ahead and dynamically create it
        synchronized (mutex) {
            mapper = mappers.get(type);
            if (mapper != null) {
                return mapper;
            }
            merge(type, null, true);
            return mappers.get(type);
        }
    }

    /**
     * A filter for search. If a filter is required, will return it, otherwise, will return <tt>null</tt>.
     */
    @Nullable
    public Filter searchFilter(String... types) {
        if (types == null || types.length == 0) {
            if (hasNested) {
                return NonNestedDocsFilter.INSTANCE;
            } else {
                return null;
            }
        }
        // if we filter by types, we don't need to filter by non nested docs
        // since they have different types (starting with __)
        if (types.length == 1) {
            DocumentMapper docMapper = documentMapper(types[0]);
            if (docMapper == null) {
                return new TermFilter(new Term(types[0]));
            }
            return docMapper.typeFilter();
        }
        // see if we can use terms filter
        boolean useTermsFilter = true;
        for (String type : types) {
            DocumentMapper docMapper = documentMapper(type);
            if (docMapper == null) {
                useTermsFilter = false;
                break;
            }
            if (!docMapper.typeMapper().indexed()) {
                useTermsFilter = false;
                break;
            }
        }
        if (useTermsFilter) {
            Term[] typesTerms = new Term[types.length];
            for (int i = 0; i < typesTerms.length; i++) {
                typesTerms[i] = new Term(TypeFieldMapper.NAME, types[i]);
            }
            return new XTermsFilter(typesTerms);
        } else {
            XBooleanFilter bool = new XBooleanFilter();
            for (String type : types) {
                DocumentMapper docMapper = documentMapper(type);
                if (docMapper == null) {
                    bool.add(new FilterClause(new TermFilter(new Term(TypeFieldMapper.NAME, type)), BooleanClause.Occur.SHOULD));
                } else {
                    bool.add(new FilterClause(docMapper.typeFilter(), BooleanClause.Occur.SHOULD));
                }
            }
            return bool;
        }
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
     * Returns objects mappers based on the full path of the object.
     */
    public ObjectMappers objectMapper(String path) {
        return fullPathObjectMappers.get(path);
    }

    public Set<String> simpleMatchToIndexNames(String pattern) {
        int dotIndex = pattern.indexOf('.');
        if (dotIndex != -1) {
            String possibleType = pattern.substring(0, dotIndex);
            DocumentMapper possibleDocMapper = mappers.get(possibleType);
            if (possibleDocMapper != null) {
                Set<String> typedFields = Sets.newHashSet();
                for (String indexName : possibleDocMapper.mappers().simpleMatchToIndexNames(pattern)) {
                    typedFields.add(possibleType + "." + indexName);
                }
                return typedFields;
            }
        }
        Set<String> fields = Sets.newHashSet();
        for (Map.Entry<String, FieldMappers> entry : fullNameFieldMappers.entrySet()) {
            if (Regex.simpleMatch(pattern, entry.getKey())) {
                for (FieldMapper mapper : entry.getValue()) {
                    fields.add(mapper.names().indexName());
                }
            }
        }
        for (Map.Entry<String, FieldMappers> entry : indexNameFieldMappers.entrySet()) {
            if (Regex.simpleMatch(pattern, entry.getKey())) {
                for (FieldMapper mapper : entry.getValue()) {
                    fields.add(mapper.names().indexName());
                }
            }
        }
        for (Map.Entry<String, FieldMappers> entry : nameFieldMappers.entrySet()) {
            if (Regex.simpleMatch(pattern, entry.getKey())) {
                for (FieldMapper mapper : entry.getValue()) {
                    fields.add(mapper.names().indexName());
                }
            }
        }
        return fields;
    }

    public SmartNameObjectMapper smartNameObjectMapper(String smartName, @Nullable String[] types) {
        if (types == null || types.length == 0) {
            return smartNameObjectMapper(smartName);
        }
        if (types.length == 1 && types[0].equals("_all")) {
            return smartNameObjectMapper(smartName);
        }
        for (String type : types) {
            DocumentMapper possibleDocMapper = mappers.get(type);
            if (possibleDocMapper != null) {
                ObjectMapper mapper = possibleDocMapper.objectMappers().get(smartName);
                if (mapper != null) {
                    return new SmartNameObjectMapper(mapper, possibleDocMapper);
                }
            }
        }
        // did not find one, see if its prefixed by type
        int dotIndex = smartName.indexOf('.');
        if (dotIndex != -1) {
            String possibleType = smartName.substring(0, dotIndex);
            DocumentMapper possibleDocMapper = mappers.get(possibleType);
            if (possibleDocMapper != null) {
                String possiblePath = smartName.substring(dotIndex + 1);
                ObjectMapper mapper = possibleDocMapper.objectMappers().get(possiblePath);
                if (mapper != null) {
                    return new SmartNameObjectMapper(mapper, possibleDocMapper);
                }
            }
        }
        // did not explicitly find one under the types provided, or prefixed by type...
        return null;
    }

    public SmartNameObjectMapper smartNameObjectMapper(String smartName) {
        int dotIndex = smartName.indexOf('.');
        if (dotIndex != -1) {
            String possibleType = smartName.substring(0, dotIndex);
            DocumentMapper possibleDocMapper = mappers.get(possibleType);
            if (possibleDocMapper != null) {
                String possiblePath = smartName.substring(dotIndex + 1);
                ObjectMapper mapper = possibleDocMapper.objectMappers().get(possiblePath);
                if (mapper != null) {
                    return new SmartNameObjectMapper(mapper, possibleDocMapper);
                }
            }
        }
        ObjectMappers mappers = objectMapper(smartName);
        if (mappers != null) {
            return new SmartNameObjectMapper(mappers.mapper(), null);
        }
        return null;
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

    public FieldMapper smartNameFieldMapper(String smartName, @Nullable String[] types) {
        FieldMappers fieldMappers = smartNameFieldMappers(smartName, types);
        if (fieldMappers != null) {
            return fieldMappers.mapper();
        }
        return null;
    }

    public FieldMappers smartNameFieldMappers(String smartName, @Nullable String[] types) {
        if (types == null || types.length == 0) {
            return smartNameFieldMappers(smartName);
        }
        for (String type : types) {
            DocumentMapper documentMapper = mappers.get(type);
            // we found a mapper
            if (documentMapper != null) {
                // see if we find a field for it
                FieldMappers mappers = documentMapper.mappers().smartName(smartName);
                if (mappers != null) {
                    return mappers;
                }
            }
        }
        // did not find explicit field in the type provided, see if its prefixed with type
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
        // we did not find the field mapping in any of the types, so don't go and try to find
        // it in other types...
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
        mappers = indexName(smartName);
        if (mappers != null) {
            return mappers;
        }
        return name(smartName);
    }

    public SmartNameFieldMappers smartName(String smartName, @Nullable String[] types) {
        if (types == null || types.length == 0) {
            return smartName(smartName);
        }
        if (types.length == 1 && types[0].equals("_all")) {
            return smartName(smartName);
        }
        for (String type : types) {
            DocumentMapper documentMapper = mappers.get(type);
            // we found a mapper
            if (documentMapper != null) {
                // see if we find a field for it
                FieldMappers mappers = documentMapper.mappers().smartName(smartName);
                if (mappers != null) {
                    return new SmartNameFieldMappers(this, mappers, documentMapper, false);
                }
            }
        }
        // did not find explicit field in the type provided, see if its prefixed with type
        int dotIndex = smartName.indexOf('.');
        if (dotIndex != -1) {
            String possibleType = smartName.substring(0, dotIndex);
            DocumentMapper possibleDocMapper = mappers.get(possibleType);
            if (possibleDocMapper != null) {
                String possibleName = smartName.substring(dotIndex + 1);
                FieldMappers mappers = possibleDocMapper.mappers().smartName(possibleName);
                if (mappers != null) {
                    return new SmartNameFieldMappers(this, mappers, possibleDocMapper, true);
                }
            }
        }
        // we did not find the field mapping in any of the types, so don't go and try to find
        // it in other types...
        return null;
    }

    /**
     * Returns smart field mappers based on a smart name. A smart name is one that can optioannly be prefixed
     * with a type (and then a '.'). If it is, then the {@link MapperService.SmartNameFieldMappers}
     * will have the doc mapper set.
     * <p/>
     * <p>It also (without the optional type prefix) try and find the {@link FieldMappers} for the specific
     * name. It will first try to find it based on the full name (with the dots if its a compound name). If
     * it is not found, will try and find it based on the indexName (which can be controlled in the mapping),
     * and last, will try it based no the name itself.
     * <p/>
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
                    return new SmartNameFieldMappers(this, mappers, possibleDocMapper, true);
                }
            }
        }
        FieldMappers fieldMappers = fullName(smartName);
        if (fieldMappers != null) {
            return new SmartNameFieldMappers(this, fieldMappers, null, false);
        }
        fieldMappers = indexName(smartName);
        if (fieldMappers != null) {
            return new SmartNameFieldMappers(this, fieldMappers, null, false);
        }
        fieldMappers = name(smartName);
        if (fieldMappers != null) {
            return new SmartNameFieldMappers(this, fieldMappers, null, false);
        }
        return null;
    }

    public Analyzer searchAnalyzer() {
        return this.searchAnalyzer;
    }

    public Analyzer searchQuoteAnalyzer() {
        return this.searchQuoteAnalyzer;
    }

    public static class SmartNameObjectMapper {
        private final ObjectMapper mapper;
        private final DocumentMapper docMapper;

        public SmartNameObjectMapper(ObjectMapper mapper, @Nullable DocumentMapper docMapper) {
            this.mapper = mapper;
            this.docMapper = docMapper;
        }

        public boolean hasMapper() {
            return mapper != null;
        }

        public ObjectMapper mapper() {
            return mapper;
        }

        public boolean hasDocMapper() {
            return docMapper != null;
        }

        public DocumentMapper docMapper() {
            return docMapper;
        }
    }

    public static class SmartNameFieldMappers {
        private final MapperService mapperService;
        private final FieldMappers fieldMappers;
        private final DocumentMapper docMapper;
        private final boolean explicitTypeInName;

        public SmartNameFieldMappers(MapperService mapperService, FieldMappers fieldMappers, @Nullable DocumentMapper docMapper, boolean explicitTypeInName) {
            this.mapperService = mapperService;
            this.fieldMappers = fieldMappers;
            this.docMapper = docMapper;
            this.explicitTypeInName = explicitTypeInName;
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

        /**
         * Returns <tt>true</tt> if the type is explicitly specified in the name.
         */
        public boolean explicitTypeInName() {
            return this.explicitTypeInName;
        }

        public boolean explicitTypeInNameWithDocMapper() {
            return explicitTypeInName && docMapper != null;
        }

        /**
         * The best effort search analyzer associated with this field.
         */
        public Analyzer searchAnalyzer() {
            if (hasMapper()) {
                Analyzer analyzer = mapper().searchAnalyzer();
                if (analyzer != null) {
                    return analyzer;
                }
            }
            if (docMapper != null && docMapper.searchAnalyzer() != null) {
                return docMapper.searchAnalyzer();
            }
            return mapperService.searchAnalyzer();
        }

        public Analyzer searchQuoteAnalyzer() {
            if (hasMapper()) {
                Analyzer analyzer = mapper().searchQuoteAnalyzer();
                if (analyzer != null) {
                    return analyzer;
                }
            }
            if (docMapper != null && docMapper.searchQuotedAnalyzer() != null) {
                return docMapper.searchQuotedAnalyzer();
            }
            return mapperService.searchQuoteAnalyzer();
        }
    }

    final class SmartIndexNameSearchAnalyzer extends AnalyzerWrapper {

        private final Analyzer defaultAnalyzer;

        SmartIndexNameSearchAnalyzer(Analyzer defaultAnalyzer) {
            this.defaultAnalyzer = defaultAnalyzer;
        }

        @Override
        protected Analyzer getWrappedAnalyzer(String fieldName) {
            int dotIndex = fieldName.indexOf('.');
            if (dotIndex != -1) {
                String possibleType = fieldName.substring(0, dotIndex);
                DocumentMapper possibleDocMapper = mappers.get(possibleType);
                if (possibleDocMapper != null) {
                    return possibleDocMapper.mappers().searchAnalyzer();
                }
            }
            FieldMappers mappers = fullNameFieldMappers.get(fieldName);
            if (mappers != null && mappers.mapper() != null && mappers.mapper().searchAnalyzer() != null) {
                return mappers.mapper().searchAnalyzer();
            }

            mappers = indexNameFieldMappers.get(fieldName);
            if (mappers != null && mappers.mapper() != null && mappers.mapper().searchAnalyzer() != null) {
                return mappers.mapper().searchAnalyzer();
            }
            return defaultAnalyzer;
        }

        @Override
        protected TokenStreamComponents wrapComponents(String fieldName, TokenStreamComponents components) {
            return components;
        }
    }

    final class SmartIndexNameSearchQuoteAnalyzer extends AnalyzerWrapper {

        private final Analyzer defaultAnalyzer;

        SmartIndexNameSearchQuoteAnalyzer(Analyzer defaultAnalyzer) {
            this.defaultAnalyzer = defaultAnalyzer;
        }

        @Override
        protected Analyzer getWrappedAnalyzer(String fieldName) {
            int dotIndex = fieldName.indexOf('.');
            if (dotIndex != -1) {
                String possibleType = fieldName.substring(0, dotIndex);
                DocumentMapper possibleDocMapper = mappers.get(possibleType);
                if (possibleDocMapper != null) {
                    return possibleDocMapper.mappers().searchQuoteAnalyzer();
                }
            }
            FieldMappers mappers = fullNameFieldMappers.get(fieldName);
            if (mappers != null && mappers.mapper() != null && mappers.mapper().searchQuoteAnalyzer() != null) {
                return mappers.mapper().searchQuoteAnalyzer();
            }

            mappers = indexNameFieldMappers.get(fieldName);
            if (mappers != null && mappers.mapper() != null && mappers.mapper().searchQuoteAnalyzer() != null) {
                return mappers.mapper().searchQuoteAnalyzer();
            }
            return defaultAnalyzer;
        }

        @Override
        protected TokenStreamComponents wrapComponents(String fieldName, TokenStreamComponents components) {
            return components;
        }
    }

    class InternalFieldMapperListener extends FieldMapperListener {
        @Override
        public void fieldMapper(FieldMapper fieldMapper) {
            addFieldMappers(new FieldMapper[]{fieldMapper});
        }

        @Override
        public void fieldMappers(FieldMapper... fieldMappers) {
            addFieldMappers(fieldMappers);
        }
    }

    class InternalObjectMapperListener extends ObjectMapperListener {
        @Override
        public void objectMapper(ObjectMapper objectMapper) {
            addObjectMappers(new ObjectMapper[]{objectMapper});
        }

        @Override
        public void objectMappers(ObjectMapper... objectMappers) {
            addObjectMappers(objectMappers);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5150.java