error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7617.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7617.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7617.java
text:
```scala
b@@uilder.field("index", indexTokenizeOptionToString(fieldType.indexed(), fieldType.tokenized()));

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

package org.elasticsearch.index.mapper.internal;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.NumericUtils;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Numbers;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.analysis.NumericFloatAnalyzer;
import org.elasticsearch.index.codec.docvaluesformat.DocValuesFormatProvider;
import org.elasticsearch.index.codec.postingsformat.PostingsFormatProvider;
import org.elasticsearch.index.fielddata.FieldDataType;
import org.elasticsearch.index.fielddata.IndexNumericFieldData;
import org.elasticsearch.index.mapper.*;
import org.elasticsearch.index.mapper.core.FloatFieldMapper;
import org.elasticsearch.index.mapper.core.NumberFieldMapper;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.index.search.NumericRangeFieldDataFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.support.XContentMapValues.nodeFloatValue;
import static org.elasticsearch.index.mapper.core.TypeParsers.parseNumberField;

/**
 *
 */
public class BoostFieldMapper extends NumberFieldMapper<Float> implements InternalMapper, RootMapper {

    public static final String CONTENT_TYPE = "_boost";
    public static final String NAME = "_boost";

    public static class Defaults extends NumberFieldMapper.Defaults {
        public static final String NAME = "_boost";
        public static final Float NULL_VALUE = null;

        public static final FieldType FIELD_TYPE = new FieldType(NumberFieldMapper.Defaults.FIELD_TYPE);

        static {
            FIELD_TYPE.setIndexed(false);
            FIELD_TYPE.setStored(false);
        }
    }

    public static class Builder extends NumberFieldMapper.Builder<Builder, BoostFieldMapper> {

        protected Float nullValue = Defaults.NULL_VALUE;

        public Builder(String name) {
            super(name, new FieldType(Defaults.FIELD_TYPE), Defaults.PRECISION_STEP_32_BIT);
            builder = this;
        }

        public Builder nullValue(float nullValue) {
            this.nullValue = nullValue;
            return this;
        }

        @Override
        public BoostFieldMapper build(BuilderContext context) {
            return new BoostFieldMapper(name, buildIndexName(context),
                    fieldType.numericPrecisionStep(), boost, fieldType, docValues, nullValue, postingsProvider, docValuesProvider, fieldDataSettings, context.indexSettings());
        }
    }

    public static class TypeParser implements Mapper.TypeParser {
        @Override
        public Mapper.Builder parse(String fieldName, Map<String, Object> node, ParserContext parserContext) throws MapperParsingException {
            String name = node.get("name") == null ? BoostFieldMapper.Defaults.NAME : node.get("name").toString();
            BoostFieldMapper.Builder builder = MapperBuilders.boost(name);
            parseNumberField(builder, name, node, parserContext);
            for (Map.Entry<String, Object> entry : node.entrySet()) {
                String propName = Strings.toUnderscoreCase(entry.getKey());
                Object propNode = entry.getValue();
                if (propName.equals("null_value")) {
                    builder.nullValue(nodeFloatValue(propNode));
                }
            }
            return builder;
        }
    }

    private final Float nullValue;

    public BoostFieldMapper() {
        this(Defaults.NAME, Defaults.NAME);
    }

    protected BoostFieldMapper(String name, String indexName) {
        this(name, indexName, Defaults.PRECISION_STEP_32_BIT, Defaults.BOOST, new FieldType(Defaults.FIELD_TYPE), null,
                Defaults.NULL_VALUE, null, null, null, ImmutableSettings.EMPTY);
    }

    protected BoostFieldMapper(String name, String indexName, int precisionStep, float boost, FieldType fieldType, Boolean docValues, Float nullValue,
                               PostingsFormatProvider postingsProvider, DocValuesFormatProvider docValuesProvider, @Nullable Settings fieldDataSettings, Settings indexSettings) {
        super(new Names(name, indexName, indexName, name), precisionStep, boost, fieldType, docValues, Defaults.IGNORE_MALFORMED, Defaults.COERCE,
                NumericFloatAnalyzer.buildNamedAnalyzer(precisionStep), NumericFloatAnalyzer.buildNamedAnalyzer(Integer.MAX_VALUE),
                postingsProvider, docValuesProvider, null, null, fieldDataSettings, indexSettings, MultiFields.empty(), null);
        this.nullValue = nullValue;
    }

    @Override
    public FieldType defaultFieldType() {
        return Defaults.FIELD_TYPE;
    }

    @Override
    public FieldDataType defaultFieldDataType() {
        return new FieldDataType("float");
    }

    @Override
    public boolean hasDocValues() {
        return false;
    }

    @Override
    protected int maxPrecisionStep() {
        return 32;
    }

    @Override
    public Float value(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        if (value instanceof BytesRef) {
            return Numbers.bytesToFloat((BytesRef) value);
        }
        return Float.parseFloat(value.toString());
    }

    @Override
    public BytesRef indexedValueForSearch(Object value) {
        int intValue = NumericUtils.floatToSortableInt(parseValue(value));
        BytesRef bytesRef = new BytesRef();
        NumericUtils.intToPrefixCoded(intValue, precisionStep(), bytesRef);
        return bytesRef;
    }

    private float parseValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        if (value instanceof BytesRef) {
            return Float.parseFloat(((BytesRef) value).utf8ToString());
        }
        return Float.parseFloat(value.toString());
    }

    @Override
    public Query fuzzyQuery(String value, Fuzziness fuzziness, int prefixLength, int maxExpansions, boolean transpositions) {
        float iValue = Float.parseFloat(value);
        float iSim = fuzziness.asFloat();
        return NumericRangeQuery.newFloatRange(names.indexName(), precisionStep,
                iValue - iSim,
                iValue + iSim,
                true, true);
    }

    @Override
    public Query rangeQuery(Object lowerTerm, Object upperTerm, boolean includeLower, boolean includeUpper, @Nullable QueryParseContext context) {
        return NumericRangeQuery.newFloatRange(names.indexName(), precisionStep,
                lowerTerm == null ? null : parseValue(lowerTerm),
                upperTerm == null ? null : parseValue(upperTerm),
                includeLower, includeUpper);
    }

    @Override
    public Filter rangeFilter(Object lowerTerm, Object upperTerm, boolean includeLower, boolean includeUpper, @Nullable QueryParseContext context) {
        return NumericRangeFilter.newFloatRange(names.indexName(), precisionStep,
                lowerTerm == null ? null : parseValue(lowerTerm),
                upperTerm == null ? null : parseValue(upperTerm),
                includeLower, includeUpper);
    }

    @Override
    public Filter rangeFilter(QueryParseContext parseContext, Object lowerTerm, Object upperTerm, boolean includeLower, boolean includeUpper, @Nullable QueryParseContext context) {
        return NumericRangeFieldDataFilter.newFloatRange((IndexNumericFieldData) parseContext.getForField(this),
                lowerTerm == null ? null : parseValue(lowerTerm),
                upperTerm == null ? null : parseValue(upperTerm),
                includeLower, includeUpper);
    }

    @Override
    public Filter nullValueFilter() {
        if (nullValue == null) {
            return null;
        }
        return NumericRangeFilter.newFloatRange(names.indexName(), precisionStep,
                nullValue,
                nullValue,
                true, true);
    }

    @Override
    public void preParse(ParseContext context) throws IOException {
    }

    @Override
    public void postParse(ParseContext context) throws IOException {
    }

    @Override
    public boolean includeInObject() {
        return true;
    }

    @Override
    public void parse(ParseContext context) throws IOException {
        // we override parse since we want to handle cases where it is not indexed and not stored (the default)
        float value = parseFloatValue(context);
        if (!Float.isNaN(value)) {
            context.docBoost(value);
        }
        super.parse(context);
    }

    @Override
    protected void innerParseCreateField(ParseContext context, List<Field> fields) throws IOException {
        final float value = parseFloatValue(context);
        if (Float.isNaN(value)) {
            return;
        }
        context.docBoost(value);
        fields.add(new FloatFieldMapper.CustomFloatNumericField(this, value, fieldType));
    }

    private float parseFloatValue(ParseContext context) throws IOException {
        float value;
        if (context.parser().currentToken() == XContentParser.Token.VALUE_NULL) {
            if (nullValue == null) {
                return Float.NaN;
            }
            value = nullValue;
        } else {
            value = context.parser().floatValue(coerce.value());
        }
        return value;
    }

    @Override
    protected String contentType() {
        return CONTENT_TYPE;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        boolean includeDefaults = params.paramAsBoolean("include_defaults", false);

        // all are defaults, don't write it at all
        if (!includeDefaults && name().equals(Defaults.NAME) && nullValue == null &&
                fieldType.indexed() == Defaults.FIELD_TYPE.indexed() &&
                fieldType.stored() == Defaults.FIELD_TYPE.stored() &&
                customFieldDataSettings == null) {
            return builder;
        }
        builder.startObject(contentType());
        if (includeDefaults || !name().equals(Defaults.NAME)) {
            builder.field("name", name());
        }
        if (includeDefaults || nullValue != null) {
            builder.field("null_value", nullValue);
        }
        if (includeDefaults || fieldType.indexed() != Defaults.FIELD_TYPE.indexed()) {
            builder.field("index", fieldType.indexed());
        }
        if (includeDefaults || fieldType.stored() != Defaults.FIELD_TYPE.stored()) {
            builder.field("store", fieldType.stored());
        }
        if (customFieldDataSettings != null) {
            builder.field("fielddata", (Map) customFieldDataSettings.getAsMap());
        } else if (includeDefaults) {
            builder.field("fielddata", (Map) fieldDataType.getSettings().getAsMap());

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7617.java