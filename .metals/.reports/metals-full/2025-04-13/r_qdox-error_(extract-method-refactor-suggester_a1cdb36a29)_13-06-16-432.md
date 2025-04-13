error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/747.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/747.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/747.java
text:
```scala
f@@ieldType.setOmitNorms(fieldType.omitNorms() && boost == 1.0f);

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

package org.elasticsearch.index.mapper.core;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.NumericUtils;
import org.elasticsearch.common.Explicit;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Numbers;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.analysis.NamedAnalyzer;
import org.elasticsearch.index.analysis.NumericIntegerAnalyzer;
import org.elasticsearch.index.cache.field.data.FieldDataCache;
import org.elasticsearch.index.field.data.FieldDataType;
import org.elasticsearch.index.mapper.*;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.index.search.NumericRangeFieldDataFilter;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.common.xcontent.support.XContentMapValues.nodeShortValue;
import static org.elasticsearch.index.mapper.MapperBuilders.shortField;
import static org.elasticsearch.index.mapper.core.TypeParsers.parseNumberField;

/**
 *
 */
public class ShortFieldMapper extends NumberFieldMapper<Short> {

    public static final String CONTENT_TYPE = "short";

    public static class Defaults extends NumberFieldMapper.Defaults {
        public static final FieldType SHORT_FIELD_TYPE = new FieldType(NumberFieldMapper.Defaults.NUMBER_FIELD_TYPE);

        static {
            SHORT_FIELD_TYPE.freeze();
        }

        public static final Short NULL_VALUE = null;
    }

    public static class Builder extends NumberFieldMapper.Builder<Builder, ShortFieldMapper> {

        protected Short nullValue = Defaults.NULL_VALUE;

        public Builder(String name) {
            super(name, new FieldType(Defaults.SHORT_FIELD_TYPE));
            builder = this;
        }

        public Builder nullValue(short nullValue) {
            this.nullValue = nullValue;
            return this;
        }

        @Override
        public ShortFieldMapper build(BuilderContext context) {
            fieldType.setOmitNorms(fieldType.omitNorms() || boost != 1.0f);
            ShortFieldMapper fieldMapper = new ShortFieldMapper(buildNames(context),
                    precisionStep, fuzzyFactor, boost, fieldType, nullValue,
                    ignoreMalformed(context));
            fieldMapper.includeInAll(includeInAll);
            return fieldMapper;
        }
    }

    public static class TypeParser implements Mapper.TypeParser {
        @Override
        public Mapper.Builder parse(String name, Map<String, Object> node, ParserContext parserContext) throws MapperParsingException {
            ShortFieldMapper.Builder builder = shortField(name);
            parseNumberField(builder, name, node, parserContext);
            for (Map.Entry<String, Object> entry : node.entrySet()) {
                String propName = Strings.toUnderscoreCase(entry.getKey());
                Object propNode = entry.getValue();
                if (propName.equals("null_value")) {
                    builder.nullValue(nodeShortValue(propNode));
                }
            }
            return builder;
        }
    }

    private Short nullValue;

    private String nullValueAsString;

    protected ShortFieldMapper(Names names, int precisionStep, String fuzzyFactor,
                               float boost, FieldType fieldType,
                               Short nullValue, Explicit<Boolean> ignoreMalformed) {
        super(names, precisionStep, fuzzyFactor, boost, fieldType,
                ignoreMalformed, new NamedAnalyzer("_short/" + precisionStep, new NumericIntegerAnalyzer(precisionStep)),
                new NamedAnalyzer("_short/max", new NumericIntegerAnalyzer(Integer.MAX_VALUE)));
        this.nullValue = nullValue;
        this.nullValueAsString = nullValue == null ? null : nullValue.toString();
    }

    @Override
    protected int maxPrecisionStep() {
        return 32;
    }

    @Override
    public Short value(Field field) {
        BytesRef value = field.binaryValue();
        if (value == null) {
            return null;
        }
        return Numbers.bytesToShort(value.bytes);
    }

    @Override
    public Short valueFromString(String value) {
        return Short.valueOf(value);
    }

    @Override
    public String indexedValue(String value) {
        BytesRef bytesRef = new BytesRef();
        NumericUtils.intToPrefixCoded(Short.parseShort(value), precisionStep(), bytesRef);
        return bytesRef.utf8ToString();
    }

    @Override
    public Query fuzzyQuery(String value, String minSim, int prefixLength, int maxExpansions, boolean transpositions) {
        short iValue = Short.parseShort(value);
        short iSim;
        try {
            iSim = Short.parseShort(minSim);
        } catch (NumberFormatException e) {
            iSim = (short) Float.parseFloat(minSim);
        }
        return NumericRangeQuery.newIntRange(names.indexName(), precisionStep,
                iValue - iSim,
                iValue + iSim,
                true, true);
    }

    @Override
    public Query fuzzyQuery(String value, double minSim, int prefixLength, int maxExpansions, boolean transpositions) {
        short iValue = Short.parseShort(value);
        short iSim = (short) (minSim * dFuzzyFactor);
        return NumericRangeQuery.newIntRange(names.indexName(), precisionStep,
                iValue - iSim,
                iValue + iSim,
                true, true);
    }

    @Override
    public Query fieldQuery(String value, @Nullable QueryParseContext context) {
        int iValue = Integer.parseInt(value);
        return NumericRangeQuery.newIntRange(names.indexName(), precisionStep,
                iValue, iValue, true, true);
    }

    @Override
    public Query rangeQuery(String lowerTerm, String upperTerm, boolean includeLower, boolean includeUpper, @Nullable QueryParseContext context) {
        return NumericRangeQuery.newIntRange(names.indexName(), precisionStep,
                lowerTerm == null ? null : Integer.parseInt(lowerTerm),
                upperTerm == null ? null : Integer.parseInt(upperTerm),
                includeLower, includeUpper);
    }

    @Override
    public Filter fieldFilter(String value, @Nullable QueryParseContext context) {
        int iValue = Integer.parseInt(value);
        return NumericRangeFilter.newIntRange(names.indexName(), precisionStep,
                iValue, iValue, true, true);
    }

    @Override
    public Filter rangeFilter(String lowerTerm, String upperTerm, boolean includeLower, boolean includeUpper, @Nullable QueryParseContext context) {
        return NumericRangeFilter.newIntRange(names.indexName(), precisionStep,
                lowerTerm == null ? null : Integer.parseInt(lowerTerm),
                upperTerm == null ? null : Integer.parseInt(upperTerm),
                includeLower, includeUpper);
    }

    @Override
    public Filter rangeFilter(FieldDataCache fieldDataCache, String lowerTerm, String upperTerm, boolean includeLower, boolean includeUpper, @Nullable QueryParseContext context) {
        return NumericRangeFieldDataFilter.newShortRange(fieldDataCache, names.indexName(),
                lowerTerm == null ? null : Short.parseShort(lowerTerm),
                upperTerm == null ? null : Short.parseShort(upperTerm),
                includeLower, includeUpper);
    }

    @Override
    public Filter nullValueFilter() {
        if (nullValue == null) {
            return null;
        }
        return NumericRangeFilter.newIntRange(names.indexName(), precisionStep,
                nullValue.intValue(),
                nullValue.intValue(),
                true, true);
    }

    @Override
    protected boolean customBoost() {
        return true;
    }

    @Override
    protected Field innerParseCreateField(ParseContext context) throws IOException {
        short value;
        float boost = this.boost;
        if (context.externalValueSet()) {
            Object externalValue = context.externalValue();
            if (externalValue == null) {
                if (nullValue == null) {
                    return null;
                }
                value = nullValue;
            } else if (externalValue instanceof String) {
                String sExternalValue = (String) externalValue;
                if (sExternalValue.length() == 0) {
                    if (nullValue == null) {
                        return null;
                    }
                    value = nullValue;
                } else {
                    value = Short.parseShort(sExternalValue);
                }
            } else {
                value = ((Number) externalValue).shortValue();
            }
            if (context.includeInAll(includeInAll, this)) {
                context.allEntries().addText(names.fullName(), Short.toString(value), boost);
            }
        } else {
            XContentParser parser = context.parser();
            if (parser.currentToken() == XContentParser.Token.VALUE_NULL ||
                    (parser.currentToken() == XContentParser.Token.VALUE_STRING && parser.textLength() == 0)) {
                if (nullValue == null) {
                    return null;
                }
                value = nullValue;
                if (nullValueAsString != null && (context.includeInAll(includeInAll, this))) {
                    context.allEntries().addText(names.fullName(), nullValueAsString, boost);
                }
            } else if (parser.currentToken() == XContentParser.Token.START_OBJECT) {
                XContentParser.Token token;
                String currentFieldName = null;
                Short objValue = nullValue;
                while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                    if (token == XContentParser.Token.FIELD_NAME) {
                        currentFieldName = parser.currentName();
                    } else {
                        if ("value".equals(currentFieldName) || "_value".equals(currentFieldName)) {
                            if (parser.currentToken() != XContentParser.Token.VALUE_NULL) {
                                objValue = parser.shortValue();
                            }
                        } else if ("boost".equals(currentFieldName) || "_boost".equals(currentFieldName)) {
                            boost = parser.floatValue();
                        }
                    }
                }
                if (objValue == null) {
                    // no value
                    return null;
                }
                value = objValue;
            } else {
                value = parser.shortValue();
                if (context.includeInAll(includeInAll, this)) {
                    context.allEntries().addText(names.fullName(), parser.text(), boost);
                }
            }
        }
        CustomShortNumericField field = new CustomShortNumericField(this, value, fieldType);
        field.setBoost(boost);
        return field;
    }

    @Override
    public FieldDataType fieldDataType() {
        return FieldDataType.DefaultTypes.SHORT;
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
            this.nullValue = ((ShortFieldMapper) mergeWith).nullValue;
            this.nullValueAsString = ((ShortFieldMapper) mergeWith).nullValueAsString;
        }
    }

    @Override
    protected void doXContentBody(XContentBuilder builder) throws IOException {
        super.doXContentBody(builder);
        if (indexed() != Defaults.SHORT_FIELD_TYPE.indexed() ||
                analyzed() != Defaults.SHORT_FIELD_TYPE.tokenized()) {
            builder.field("index", indexTokenizeOptionToString(indexed(), analyzed()));
        }
        if (stored() != Defaults.SHORT_FIELD_TYPE.stored()) {
            builder.field("store", stored());
        }
        if (storeTermVectors() != Defaults.SHORT_FIELD_TYPE.storeTermVectors()) {
            builder.field("store_term_vector", storeTermVectors());
        }
        if (storeTermVectorOffsets() != Defaults.SHORT_FIELD_TYPE.storeTermVectorOffsets()) {
            builder.field("store_term_vector_offsets", storeTermVectorOffsets());
        }
        if (storeTermVectorPositions() != Defaults.SHORT_FIELD_TYPE.storeTermVectorPositions()) {
            builder.field("store_term_vector_positions", storeTermVectorPositions());
        }
        if (storeTermVectorPayloads() != Defaults.SHORT_FIELD_TYPE.storeTermVectorPayloads()) {
            builder.field("store_term_vector_payloads", storeTermVectorPayloads());
        }
        if (omitNorms() != Defaults.SHORT_FIELD_TYPE.omitNorms()) {
            builder.field("omit_norms", omitNorms());
        }
        if (indexOptions() != Defaults.SHORT_FIELD_TYPE.indexOptions()) {
            builder.field("index_options", indexOptionToString(indexOptions()));
        }
        if (precisionStep != Defaults.PRECISION_STEP) {
            builder.field("precision_step", precisionStep);
        }
        if (fuzzyFactor != Defaults.FUZZY_FACTOR) {
            builder.field("fuzzy_factor", fuzzyFactor);
        }
        if (nullValue != null) {
            builder.field("null_value", nullValue);
        }
        if (includeInAll != null) {
            builder.field("include_in_all", includeInAll);
        }
    }

    public static class CustomShortNumericField extends CustomNumericField {

        private final short number;

        private final NumberFieldMapper mapper;

        public CustomShortNumericField(NumberFieldMapper mapper, short number, FieldType fieldType) {
            super(mapper, mapper.stored() ? Numbers.shortToBytes(number) : null, fieldType);
            this.mapper = mapper;
            this.number = number;
        }

        @Override
        public TokenStream tokenStream(Analyzer analyzer) throws IOException {
            if (fieldType().indexed()) {
                return mapper.popCachedStream().setIntValue(number);
            }
            return null;
        }

        @Override
        public String numericAsString() {
            return Short.toString(number);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/747.java