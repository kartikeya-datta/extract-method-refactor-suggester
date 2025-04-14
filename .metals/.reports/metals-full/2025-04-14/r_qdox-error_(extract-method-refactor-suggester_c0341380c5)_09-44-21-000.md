error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2171.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2171.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2171.java
text:
```scala
r@@eturn new XLuceneConstantScoreQuery(termFilter(value, context));

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

package org.elasticsearch.index.mapper.internal;

import com.google.common.collect.Iterables;
import org.apache.lucene.document.BinaryDocValuesField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.TermsFilter;
import org.apache.lucene.search.*;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.lucene.BytesRefs;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.lucene.search.RegexpFilter;
import org.elasticsearch.common.lucene.search.XBooleanFilter;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.codec.docvaluesformat.DocValuesFormatProvider;
import org.elasticsearch.index.codec.postingsformat.PostingsFormatProvider;
import org.elasticsearch.index.fielddata.FieldDataType;
import org.elasticsearch.index.mapper.*;
import org.elasticsearch.index.mapper.core.AbstractFieldMapper;
import org.elasticsearch.index.query.QueryParseContext;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.mapper.MapperBuilders.id;
import static org.elasticsearch.index.mapper.core.TypeParsers.parseField;

/**
 *
 */
public class IdFieldMapper extends AbstractFieldMapper<String> implements InternalMapper, RootMapper {

    public static final String NAME = "_id";

    public static final String CONTENT_TYPE = "_id";

    public static class Defaults extends AbstractFieldMapper.Defaults {
        public static final String NAME = IdFieldMapper.NAME;
        public static final String INDEX_NAME = IdFieldMapper.NAME;

        public static final FieldType FIELD_TYPE = new FieldType(AbstractFieldMapper.Defaults.FIELD_TYPE);

        static {
            FIELD_TYPE.setIndexed(false);
            FIELD_TYPE.setStored(false);
            FIELD_TYPE.setOmitNorms(true);
            FIELD_TYPE.setIndexOptions(IndexOptions.DOCS_ONLY);
            FIELD_TYPE.freeze();
        }

        public static final String PATH = null;
    }

    public static class Builder extends AbstractFieldMapper.Builder<Builder, IdFieldMapper> {

        private String path = Defaults.PATH;

        public Builder() {
            super(Defaults.NAME, new FieldType(Defaults.FIELD_TYPE));
            indexName = Defaults.INDEX_NAME;
        }

        public Builder path(String path) {
            this.path = path;
            return builder;
        }

        @Override
        public IdFieldMapper build(BuilderContext context) {
            return new IdFieldMapper(name, indexName, boost, fieldType, path, postingsProvider, docValuesProvider, fieldDataSettings, context.indexSettings());
        }
    }

    public static class TypeParser implements Mapper.TypeParser {
        @Override
        public Mapper.Builder parse(String name, Map<String, Object> node, ParserContext parserContext) throws MapperParsingException {
            IdFieldMapper.Builder builder = id();
            parseField(builder, builder.name, node, parserContext);
            for (Map.Entry<String, Object> entry : node.entrySet()) {
                String fieldName = Strings.toUnderscoreCase(entry.getKey());
                Object fieldNode = entry.getValue();
                if (fieldName.equals("path")) {
                    builder.path(fieldNode.toString());
                }
            }
            return builder;
        }
    }

    private final String path;

    public IdFieldMapper() {
        this(Defaults.NAME, Defaults.INDEX_NAME, new FieldType(Defaults.FIELD_TYPE));
    }

    public IdFieldMapper(FieldType fieldType) {
        this(Defaults.NAME, Defaults.INDEX_NAME, fieldType);
    }

    protected IdFieldMapper(String name, String indexName, FieldType fieldType) {
        this(name, indexName, Defaults.BOOST, fieldType, Defaults.PATH, null, null, null, ImmutableSettings.EMPTY);
    }

    protected IdFieldMapper(String name, String indexName, float boost, FieldType fieldType, String path,
                            PostingsFormatProvider postingsProvider, DocValuesFormatProvider docValuesProvider,
                            @Nullable Settings fieldDataSettings, Settings indexSettings) {
        super(new Names(name, indexName, indexName, name), boost, fieldType, Lucene.KEYWORD_ANALYZER,
                Lucene.KEYWORD_ANALYZER, postingsProvider, docValuesProvider, null, fieldDataSettings, indexSettings);
        this.path = path;
    }

    public String path() {
        return this.path;
    }

    @Override
    public FieldType defaultFieldType() {
        return Defaults.FIELD_TYPE;
    }

    @Override
    public FieldDataType defaultFieldDataType() {
        return new FieldDataType("string");
    }

    @Override
    public String value(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    @Override
    public boolean useTermQueryWithQueryString() {
        return true;
    }

    @Override
    public Query termQuery(Object value, @Nullable QueryParseContext context) {
        if (fieldType.indexed() || context == null) {
            return super.termQuery(value, context);
        }
        // no need for constant score filter, since we don't cache the filter, and it always takes deletes into account
        return new ConstantScoreQuery(termFilter(value, context));
    }

    @Override
    public Filter termFilter(Object value, @Nullable QueryParseContext context) {
        if (fieldType.indexed() || context == null) {
            return super.termFilter(value, context);
        }
        return new TermsFilter(UidFieldMapper.NAME, Uid.createTypeUids(context.queryTypes(), value));
    }

    @Override
    public Filter termsFilter(List values, @Nullable QueryParseContext context) {
        if (fieldType.indexed() || context == null) {
            return super.termsFilter(values, context);
        }
        return new TermsFilter(UidFieldMapper.NAME, Uid.createTypeUids(context.queryTypes(), values));
    }

    @Override
    public Query prefixQuery(Object value, @Nullable MultiTermQuery.RewriteMethod method, @Nullable QueryParseContext context) {
        if (fieldType.indexed() || context == null) {
            return super.prefixQuery(value, method, context);
        }
        Collection<String> queryTypes = context.queryTypes();
        if (queryTypes.size() == 1) {
            PrefixQuery prefixQuery = new PrefixQuery(new Term(UidFieldMapper.NAME, Uid.createUidAsBytes(Iterables.getFirst(queryTypes, null), BytesRefs.toBytesRef(value))));
            if (method != null) {
                prefixQuery.setRewriteMethod(method);
            }
            return prefixQuery;
        }
        BooleanQuery query = new BooleanQuery();
        for (String queryType : queryTypes) {
            PrefixQuery prefixQuery = new PrefixQuery(new Term(UidFieldMapper.NAME, Uid.createUidAsBytes(queryType, BytesRefs.toBytesRef(value))));
            if (method != null) {
                prefixQuery.setRewriteMethod(method);
            }
            query.add(prefixQuery, BooleanClause.Occur.SHOULD);
        }
        return query;
    }

    @Override
    public Filter prefixFilter(Object value, @Nullable QueryParseContext context) {
        if (fieldType.indexed() || context == null) {
            return super.prefixFilter(value, context);
        }
        Collection<String> queryTypes = context.queryTypes();
        if (queryTypes.size() == 1) {
            return new PrefixFilter(new Term(UidFieldMapper.NAME, Uid.createUidAsBytes(Iterables.getFirst(queryTypes, null), BytesRefs.toBytesRef(value))));
        }
        XBooleanFilter filter = new XBooleanFilter();
        for (String queryType : queryTypes) {
            filter.add(new PrefixFilter(new Term(UidFieldMapper.NAME, Uid.createUidAsBytes(queryType, BytesRefs.toBytesRef(value)))), BooleanClause.Occur.SHOULD);
        }
        return filter;
    }

    @Override
    public Query regexpQuery(Object value, int flags, @Nullable MultiTermQuery.RewriteMethod method, @Nullable QueryParseContext context) {
        if (fieldType.indexed() || context == null) {
            return super.regexpQuery(value, flags, method, context);
        }
        Collection<String> queryTypes = context.queryTypes();
        if (queryTypes.size() == 1) {
            RegexpQuery regexpQuery = new RegexpQuery(new Term(UidFieldMapper.NAME, Uid.createUidAsBytes(Iterables.getFirst(queryTypes, null), BytesRefs.toBytesRef(value))), flags);
            if (method != null) {
                regexpQuery.setRewriteMethod(method);
            }
            return regexpQuery;
        }
        BooleanQuery query = new BooleanQuery();
        for (String queryType : queryTypes) {
            RegexpQuery regexpQuery = new RegexpQuery(new Term(UidFieldMapper.NAME, Uid.createUidAsBytes(queryType, BytesRefs.toBytesRef(value))), flags);
            if (method != null) {
                regexpQuery.setRewriteMethod(method);
            }
            query.add(regexpQuery, BooleanClause.Occur.SHOULD);
        }
        return query;
    }

    public Filter regexpFilter(Object value, int flags, @Nullable QueryParseContext context) {
        if (fieldType.indexed() || context == null) {
            return super.regexpFilter(value, flags, context);
        }
        Collection<String> queryTypes = context.queryTypes();
        if (queryTypes.size() == 1) {
            return new RegexpFilter(new Term(UidFieldMapper.NAME, Uid.createUidAsBytes(Iterables.getFirst(queryTypes, null), BytesRefs.toBytesRef(value))), flags);
        }
        XBooleanFilter filter = new XBooleanFilter();
        for (String queryType : queryTypes) {
            filter.add(new RegexpFilter(new Term(UidFieldMapper.NAME, Uid.createUidAsBytes(queryType, BytesRefs.toBytesRef(value))), flags), BooleanClause.Occur.SHOULD);
        }
        return filter;
    }

    @Override
    public void preParse(ParseContext context) throws IOException {
        if (context.sourceToParse().id() != null) {
            context.id(context.sourceToParse().id());
            super.parse(context);
        }
    }

    @Override
    public void postParse(ParseContext context) throws IOException {
        if (context.id() == null && !context.sourceToParse().flyweight()) {
            throw new MapperParsingException("No id found while parsing the content source");
        }
        // it either get built in the preParse phase, or get parsed...
    }

    @Override
    public void parse(ParseContext context) throws IOException {
        super.parse(context);
    }

    @Override
    public void validate(ParseContext context) throws MapperParsingException {
    }

    @Override
    public boolean includeInObject() {
        return true;
    }

    @Override
    protected void parseCreateField(ParseContext context, List<Field> fields) throws IOException {
        XContentParser parser = context.parser();
        if (parser.currentName() != null && parser.currentName().equals(Defaults.NAME) && parser.currentToken().isValue()) {
            // we are in the parse Phase
            String id = parser.text();
            if (context.id() != null && !context.id().equals(id)) {
                throw new MapperParsingException("Provided id [" + context.id() + "] does not match the content one [" + id + "]");
            }
            context.id(id);
        } // else we are in the pre/post parse phase

        if (fieldType.indexed() || fieldType.stored()) {
            fields.add(new Field(names.indexName(), context.id(), fieldType));
        }
        if (hasDocValues()) {
            fields.add(new BinaryDocValuesField(names.indexName(), new BytesRef(context.id())));
        }
    }

    @Override
    protected String contentType() {
        return CONTENT_TYPE;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        // if all are defaults, no sense to write it at all
        if (fieldType.stored() == Defaults.FIELD_TYPE.stored()
                && fieldType.indexed() == Defaults.FIELD_TYPE.indexed()
                && path == Defaults.PATH
                && customFieldDataSettings == null
                && (postingsFormat == null || postingsFormat.name().equals(defaultPostingFormat()))
                && (docValuesFormat == null || docValuesFormat.name().equals(defaultDocValuesFormat()))) {
            return builder;
        }
        builder.startObject(CONTENT_TYPE);
        if (fieldType.stored() != Defaults.FIELD_TYPE.stored()) {
            builder.field("store", fieldType.stored());
        }
        if (fieldType.indexed() != Defaults.FIELD_TYPE.indexed()) {
            builder.field("index", indexTokenizeOptionToString(fieldType.indexed(), fieldType.tokenized()));
        }
        if (path != Defaults.PATH) {
            builder.field("path", path);
        }
        if (postingsFormat != null && !postingsFormat.name().equals(defaultPostingFormat())) {
            builder.field("postings_format", postingsFormat.name());
        }
        if (docValuesFormat != null && !docValuesFormat.name().equals(defaultDocValuesFormat())) {
            builder.field(DOC_VALUES_FORMAT, docValuesFormat.name());
        }
        if (customFieldDataSettings != null) {
            builder.field("fielddata", (Map) customFieldDataSettings.getAsMap());
        }
        builder.endObject();
        return builder;
    }

    @Override
    public void merge(Mapper mergeWith, MergeContext mergeContext) throws MergeMappingException {
        // do nothing here, no merging, but also no exception
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2171.java