error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10216.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10216.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10216.java
text:
```scala
V@@alueAndBoost valueAndBoost = parseCreateFieldForString(context, nullValue, boost);

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

package org.elasticsearch.index.mapper.core;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.SortedSetDocValuesField;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.ElasticsearchIllegalArgumentException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.index.analysis.NamedAnalyzer;
import org.elasticsearch.index.codec.docvaluesformat.DocValuesFormatProvider;
import org.elasticsearch.index.codec.postingsformat.PostingsFormatProvider;
import org.elasticsearch.index.fielddata.FieldDataType;
import org.elasticsearch.index.mapper.*;
import org.elasticsearch.index.mapper.internal.AllFieldMapper;
import org.elasticsearch.index.similarity.SimilarityProvider;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.mapper.MapperBuilders.stringField;
import static org.elasticsearch.index.mapper.core.TypeParsers.parseField;
import static org.elasticsearch.index.mapper.core.TypeParsers.parseMultiField;

/**
 *
 */
public class StringFieldMapper extends AbstractFieldMapper<String> implements AllFieldMapper.IncludeInAll {

    public static final String CONTENT_TYPE = "string";

    public static class Defaults extends AbstractFieldMapper.Defaults {
        public static final FieldType FIELD_TYPE = new FieldType(AbstractFieldMapper.Defaults.FIELD_TYPE);

        static {
            FIELD_TYPE.freeze();
        }

        // NOTE, when adding defaults here, make sure you add them in the builder
        public static final String NULL_VALUE = null;
        public static final int POSITION_OFFSET_GAP = 0;
        public static final int IGNORE_ABOVE = -1;
    }

    public static class Builder extends AbstractFieldMapper.Builder<Builder, StringFieldMapper> {

        protected String nullValue = Defaults.NULL_VALUE;

        protected int positionOffsetGap = Defaults.POSITION_OFFSET_GAP;

        protected NamedAnalyzer searchQuotedAnalyzer;

        protected int ignoreAbove = Defaults.IGNORE_ABOVE;

        public Builder(String name) {
            super(name, new FieldType(Defaults.FIELD_TYPE));
            builder = this;
        }

        public Builder nullValue(String nullValue) {
            this.nullValue = nullValue;
            return this;
        }

        @Override
        public Builder searchAnalyzer(NamedAnalyzer searchAnalyzer) {
            super.searchAnalyzer(searchAnalyzer);
            if (searchQuotedAnalyzer == null) {
                searchQuotedAnalyzer = searchAnalyzer;
            }
            return this;
        }

        public Builder positionOffsetGap(int positionOffsetGap) {
            this.positionOffsetGap = positionOffsetGap;
            return this;
        }

        public Builder searchQuotedAnalyzer(NamedAnalyzer analyzer) {
            this.searchQuotedAnalyzer = analyzer;
            return builder;
        }

        public Builder ignoreAbove(int ignoreAbove) {
            this.ignoreAbove = ignoreAbove;
            return this;
        }

        @Override
        public StringFieldMapper build(BuilderContext context) {
            if (positionOffsetGap > 0) {
                indexAnalyzer = new NamedAnalyzer(indexAnalyzer, positionOffsetGap);
                searchAnalyzer = new NamedAnalyzer(searchAnalyzer, positionOffsetGap);
                searchQuotedAnalyzer = new NamedAnalyzer(searchQuotedAnalyzer, positionOffsetGap);
            }
            // if the field is not analyzed, then by default, we should omit norms and have docs only
            // index options, as probably what the user really wants
            // if they are set explicitly, we will use those values
            // we also change the values on the default field type so that toXContent emits what
            // differs from the defaults
            FieldType defaultFieldType = new FieldType(Defaults.FIELD_TYPE);
            if (fieldType.indexed() && !fieldType.tokenized()) {
                defaultFieldType.setOmitNorms(true);
                defaultFieldType.setIndexOptions(IndexOptions.DOCS_ONLY);
                if (!omitNormsSet && boost == Defaults.BOOST) {
                    fieldType.setOmitNorms(true);
                }
                if (!indexOptionsSet) {
                    fieldType.setIndexOptions(IndexOptions.DOCS_ONLY);
                }
            }
            defaultFieldType.freeze();
            StringFieldMapper fieldMapper = new StringFieldMapper(buildNames(context),
                    boost, fieldType, defaultFieldType, docValues, nullValue, indexAnalyzer, searchAnalyzer, searchQuotedAnalyzer,
                    positionOffsetGap, ignoreAbove, postingsProvider, docValuesProvider, similarity, normsLoading, 
                    fieldDataSettings, context.indexSettings(), multiFieldsBuilder.build(this, context), copyTo);
            fieldMapper.includeInAll(includeInAll);
            return fieldMapper;
        }
    }

    public static class TypeParser implements Mapper.TypeParser {
        @Override
        public Mapper.Builder parse(String name, Map<String, Object> node, ParserContext parserContext) throws MapperParsingException {
            StringFieldMapper.Builder builder = stringField(name);
            parseField(builder, name, node, parserContext);
            for (Map.Entry<String, Object> entry : node.entrySet()) {
                String propName = Strings.toUnderscoreCase(entry.getKey());
                Object propNode = entry.getValue();
                if (propName.equals("null_value")) {
                    builder.nullValue(propNode.toString());
                } else if (propName.equals("search_quote_analyzer")) {
                    NamedAnalyzer analyzer = parserContext.analysisService().analyzer(propNode.toString());
                    if (analyzer == null) {
                        throw new MapperParsingException("Analyzer [" + propNode.toString() + "] not found for field [" + name + "]");
                    }
                    builder.searchQuotedAnalyzer(analyzer);
                } else if (propName.equals("position_offset_gap")) {
                    builder.positionOffsetGap(XContentMapValues.nodeIntegerValue(propNode, -1));
                    // we need to update to actual analyzers if they are not set in this case...
                    // so we can inject the position offset gap...
                    if (builder.indexAnalyzer == null) {
                        builder.indexAnalyzer = parserContext.analysisService().defaultIndexAnalyzer();
                    }
                    if (builder.searchAnalyzer == null) {
                        builder.searchAnalyzer = parserContext.analysisService().defaultSearchAnalyzer();
                    }
                    if (builder.searchQuotedAnalyzer == null) {
                        builder.searchQuotedAnalyzer = parserContext.analysisService().defaultSearchQuoteAnalyzer();
                    }
                } else if (propName.equals("ignore_above")) {
                    builder.ignoreAbove(XContentMapValues.nodeIntegerValue(propNode, -1));
                } else {
                    parseMultiField(builder, name, node, parserContext, propName, propNode);
                }
            }
            return builder;
        }
    }

    private String nullValue;
    private Boolean includeInAll;
    private int positionOffsetGap;
    private NamedAnalyzer searchQuotedAnalyzer;
    private int ignoreAbove;
    private final FieldType defaultFieldType;

    protected StringFieldMapper(Names names, float boost, FieldType fieldType,FieldType defaultFieldType, Boolean docValues,
                                String nullValue, NamedAnalyzer indexAnalyzer, NamedAnalyzer searchAnalyzer,
                                NamedAnalyzer searchQuotedAnalyzer, int positionOffsetGap, int ignoreAbove,
                                PostingsFormatProvider postingsFormat, DocValuesFormatProvider docValuesFormat,
                                SimilarityProvider similarity, Loading normsLoading, @Nullable Settings fieldDataSettings,
                                Settings indexSettings, MultiFields multiFields, CopyTo copyTo) {
        super(names, boost, fieldType, docValues, indexAnalyzer, searchAnalyzer, postingsFormat, docValuesFormat, 
                similarity, normsLoading, fieldDataSettings, indexSettings, multiFields, copyTo);
        if (fieldType.tokenized() && fieldType.indexed() && hasDocValues()) {
            throw new MapperParsingException("Field [" + names.fullName() + "] cannot be analyzed and have doc values");
        }
        this.defaultFieldType = defaultFieldType;
        this.nullValue = nullValue;
        this.positionOffsetGap = positionOffsetGap;
        this.searchQuotedAnalyzer = searchQuotedAnalyzer != null ? searchQuotedAnalyzer : this.searchAnalyzer;
        this.ignoreAbove = ignoreAbove;
    }

    @Override
    public FieldType defaultFieldType() {
        return defaultFieldType;
    }

    @Override
    public FieldDataType defaultFieldDataType() {
        return new FieldDataType("string");
    }

    @Override
    public void includeInAll(Boolean includeInAll) {
        if (includeInAll != null) {
            this.includeInAll = includeInAll;
        }
    }

    @Override
    public void includeInAllIfNotSet(Boolean includeInAll) {
        if (includeInAll != null && this.includeInAll == null) {
            this.includeInAll = includeInAll;
        }
    }

    @Override
    public void unsetIncludeInAll() {
        includeInAll = null;
    }

    @Override
    public String value(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    @Override
    protected boolean customBoost() {
        return true;
    }

    public int getPositionOffsetGap() {
        return this.positionOffsetGap;
    }

    @Override
    public Analyzer searchQuoteAnalyzer() {
        return this.searchQuotedAnalyzer;
    }

    @Override
    public Filter nullValueFilter() {
        if (nullValue == null) {
            return null;
        }
        return termFilter(nullValue, null);
    }

    @Override
    protected void parseCreateField(ParseContext context, List<Field> fields) throws IOException {
        ValueAndBoost valueAndBoost = parseCreateFieldForString(context, nullValue, context.fieldBoost(this));
        if (valueAndBoost.value() == null) {
            return;
        }
        if (ignoreAbove > 0 && valueAndBoost.value().length() > ignoreAbove) {
            return;
        }
        if (context.includeInAll(includeInAll, this)) {
            context.allEntries().addText(names.fullName(), valueAndBoost.value(), valueAndBoost.boost());
        }

        if (fieldType.indexed() || fieldType.stored()) {
            Field field = new StringField(names.indexName(), valueAndBoost.value(), fieldType);
            field.setBoost(valueAndBoost.boost());
            fields.add(field);
        }
        if (hasDocValues()) {
            fields.add(new SortedSetDocValuesField(names.indexName(), new BytesRef(valueAndBoost.value())));
        }
        if (fields.isEmpty()) {
            context.ignoredValue(names.indexName(), valueAndBoost.value());
        }
    }

    /**
     * Parse a field as though it were a string.
     * @param context parse context used during parsing
     * @param nullValue value to use for null
     * @param defaultBoost default boost value returned unless overwritten in the field
     * @return the parsed field and the boost either parsed or defaulted
     * @throws IOException if thrown while parsing
     */
    public static ValueAndBoost parseCreateFieldForString(ParseContext context, String nullValue, float defaultBoost) throws IOException {
        if (context.externalValueSet()) {
            return new ValueAndBoost((String) context.externalValue(), defaultBoost);
        }
        XContentParser parser = context.parser();
        if (parser.currentToken() == XContentParser.Token.VALUE_NULL) {
            return new ValueAndBoost(nullValue, defaultBoost);
        }
        if (parser.currentToken() == XContentParser.Token.START_OBJECT) {
            XContentParser.Token token;
            String currentFieldName = null;
            String value = nullValue;
            float boost = defaultBoost;
            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    currentFieldName = parser.currentName();
                } else {
                    if ("value".equals(currentFieldName) || "_value".equals(currentFieldName)) {
                        value = parser.textOrNull();
                    } else if ("boost".equals(currentFieldName) || "_boost".equals(currentFieldName)) {
                        boost = parser.floatValue();
                    } else {
                        throw new ElasticsearchIllegalArgumentException("unknown property [" + currentFieldName + "]");
                    }
                }
            }
            return new ValueAndBoost(value, boost);
        }
        return new ValueAndBoost(parser.textOrNull(), defaultBoost);
    }

    @Override
    protected String contentType() {
        return CONTENT_TYPE;
    }

    @Override
    public void merge(Mapper mergeWith, MergeContext mergeContext) throws MergeMappingException {
        super.merge(mergeWith, mergeContext);
        if (!this.getClass().equals(mergeWith.getClass())) {
            return;
        }
        if (!mergeContext.mergeFlags().simulate()) {
            this.includeInAll = ((StringFieldMapper) mergeWith).includeInAll;
            this.nullValue = ((StringFieldMapper) mergeWith).nullValue;
            this.ignoreAbove = ((StringFieldMapper) mergeWith).ignoreAbove;
        }
    }

    @Override
    protected void doXContentBody(XContentBuilder builder, boolean includeDefaults, Params params) throws IOException {
        super.doXContentBody(builder, includeDefaults, params);

        if (includeDefaults || nullValue != null) {
            builder.field("null_value", nullValue);
        }
        if (includeInAll != null) {
            builder.field("include_in_all", includeInAll);
        } else if (includeDefaults) {
            builder.field("include_in_all", false);
        }

        if (includeDefaults || positionOffsetGap != Defaults.POSITION_OFFSET_GAP) {
            builder.field("position_offset_gap", positionOffsetGap);
        }
        if (searchQuotedAnalyzer != null && searchAnalyzer != searchQuotedAnalyzer) {
            builder.field("search_quote_analyzer", searchQuotedAnalyzer.name());
        } else if (includeDefaults) {
            if (searchQuotedAnalyzer == null) {
                builder.field("search_quote_analyzer", "default");
            } else {
                builder.field("search_quote_analyzer", searchQuotedAnalyzer.name());
            }
        }
        if (includeDefaults || ignoreAbove != Defaults.IGNORE_ABOVE) {
            builder.field("ignore_above", ignoreAbove);
        }
    }

    /** Extension of {@link Field} supporting reuse of a cached TokenStream for not-tokenized values. */
    static class StringField extends Field {

        public StringField(String name, String value, FieldType fieldType) {
            super(name, fieldType);
            fieldsData = value;
        }

        @Override
        public TokenStream tokenStream(Analyzer analyzer) throws IOException {
            if (!fieldType().indexed()) {
                return null;
            }
            // Only use the cached TokenStream if the value is indexed and not-tokenized
            if (fieldType().tokenized()) {
                return super.tokenStream(analyzer);
            }
            return NOT_ANALYZED_TOKENSTREAM.get().setValue((String) fieldsData);
        }
    }

    private static final ThreadLocal<StringTokenStream> NOT_ANALYZED_TOKENSTREAM = new ThreadLocal<StringTokenStream>() {
        @Override
        protected StringTokenStream initialValue() {
            return new StringTokenStream();
        }
    };


    // Copied from Field.java
    static final class StringTokenStream extends TokenStream {
        private final CharTermAttribute termAttribute = addAttribute(CharTermAttribute.class);
        private final OffsetAttribute offsetAttribute = addAttribute(OffsetAttribute.class);
        private boolean used = false;
        private String value = null;

        /**
         * Creates a new TokenStream that returns a String as single token.
         * <p>Warning: Does not initialize the value, you must call
         * {@link #setValue(String)} afterwards!
         */
        StringTokenStream() {
        }

        /** Sets the string value. */
        StringTokenStream setValue(String value) {
            this.value = value;
            return this;
        }

        @Override
        public boolean incrementToken() {
            if (used) {
                return false;
            }
            clearAttributes();
            termAttribute.append(value);
            offsetAttribute.setOffset(0, value.length());
            used = true;
            return true;
        }

        @Override
        public void end() {
            final int finalOffset = value.length();
            offsetAttribute.setOffset(finalOffset, finalOffset);
            value = null;
        }

        @Override
        public void reset() {
            used = false;
        }

        @Override
        public void close() {
            value = null;
        }
    }

    /**
     * Parsed value and boost to be returned from {@link #parseCreateFieldForString}.
     */
    public static class ValueAndBoost {
        private final String value;
        private final float boost;

        public ValueAndBoost(String value, float boost) {
            this.value = value;
            this.boost = boost;
        }

        /**
         * Value of string field.
         * @return value of string field
         */
        public String value() {
            return value;
        }

        /**
         * Boost either parsed from the document or defaulted.
         * @return boost either parsed from the document or defaulted
         */
        public float boost() {
            return boost;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10216.java