error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2439.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2439.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2439.java
text:
```scala
N@@umericUtils.longToPrefixCoded(parseValue(value), 0, bytesRef); // 0 because of exact match

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

package org.elasticsearch.index.mapper.ip;

import org.apache.lucene.analysis.NumericTokenStream;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.NumericUtils;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.common.Explicit;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Numbers;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.analysis.NamedAnalyzer;
import org.elasticsearch.index.analysis.NumericAnalyzer;
import org.elasticsearch.index.analysis.NumericTokenizer;
import org.elasticsearch.index.codec.postingsformat.PostingsFormatProvider;
import org.elasticsearch.index.fielddata.FieldDataType;
import org.elasticsearch.index.fielddata.IndexFieldDataService;
import org.elasticsearch.index.fielddata.IndexNumericFieldData;
import org.elasticsearch.index.mapper.*;
import org.elasticsearch.index.mapper.core.LongFieldMapper;
import org.elasticsearch.index.mapper.core.NumberFieldMapper;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.index.search.NumericRangeFieldDataFilter;
import org.elasticsearch.index.similarity.SimilarityProvider;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.regex.Pattern;

import static org.elasticsearch.index.mapper.MapperBuilders.ipField;
import static org.elasticsearch.index.mapper.core.TypeParsers.parseNumberField;

/**
 *
 */
public class IpFieldMapper extends NumberFieldMapper<Long> {

    public static final String CONTENT_TYPE = "ip";

    public static String longToIp(long longIp) {
        int octet3 = (int) ((longIp >> 24) % 256);
        int octet2 = (int) ((longIp >> 16) % 256);
        int octet1 = (int) ((longIp >> 8) % 256);
        int octet0 = (int) ((longIp) % 256);
        return octet3 + "." + octet2 + "." + octet1 + "." + octet0;
    }

    private static final Pattern pattern = Pattern.compile("\\.");

    public static long ipToLong(String ip) throws ElasticSearchIllegalArgumentException {
        try {
            String[] octets = pattern.split(ip);
            if (octets.length != 4) {
                throw new ElasticSearchIllegalArgumentException("failed to parse ip [" + ip + "], not full ip address (4 dots)");
            }
            return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16) +
                    (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
        } catch (Exception e) {
            if (e instanceof ElasticSearchIllegalArgumentException) {
                throw (ElasticSearchIllegalArgumentException) e;
            }
            throw new ElasticSearchIllegalArgumentException("failed to parse ip [" + ip + "]", e);
        }
    }

    public static class Defaults extends NumberFieldMapper.Defaults {
        public static final String NULL_VALUE = null;

        public static final FieldType FIELD_TYPE = new FieldType(NumberFieldMapper.Defaults.FIELD_TYPE);

        static {
            FIELD_TYPE.freeze();
        }
    }

    public static class Builder extends NumberFieldMapper.Builder<Builder, IpFieldMapper> {

        protected String nullValue = Defaults.NULL_VALUE;

        public Builder(String name) {
            super(name, new FieldType(Defaults.FIELD_TYPE));
            builder = this;
        }

        public Builder nullValue(String nullValue) {
            this.nullValue = nullValue;
            return this;
        }

        @Override
        public IpFieldMapper build(BuilderContext context) {
            fieldType.setOmitNorms(fieldType.omitNorms() && boost == 1.0f);
            IpFieldMapper fieldMapper = new IpFieldMapper(buildNames(context),
                    precisionStep, boost, fieldType, nullValue, ignoreMalformed(context), provider, similarity, fieldDataSettings);
            fieldMapper.includeInAll(includeInAll);
            return fieldMapper;
        }
    }

    public static class TypeParser implements Mapper.TypeParser {
        @Override
        public Mapper.Builder parse(String name, Map<String, Object> node, ParserContext parserContext) throws MapperParsingException {
            IpFieldMapper.Builder builder = ipField(name);
            parseNumberField(builder, name, node, parserContext);
            for (Map.Entry<String, Object> entry : node.entrySet()) {
                String propName = Strings.toUnderscoreCase(entry.getKey());
                Object propNode = entry.getValue();
                if (propName.equals("null_value")) {
                    builder.nullValue(propNode.toString());
                }
            }
            return builder;
        }
    }

    private String nullValue;

    protected IpFieldMapper(Names names, int precisionStep, float boost, FieldType fieldType,
                            String nullValue, Explicit<Boolean> ignoreMalformed,
                            PostingsFormatProvider provider, SimilarityProvider similarity, @Nullable Settings fieldDataSettings) {
        super(names, precisionStep, boost, fieldType,
                ignoreMalformed, new NamedAnalyzer("_ip/" + precisionStep, new NumericIpAnalyzer(precisionStep)),
                new NamedAnalyzer("_ip/max", new NumericIpAnalyzer(Integer.MAX_VALUE)), provider, similarity, fieldDataSettings);
        this.nullValue = nullValue;
    }

    @Override
    public FieldType defaultFieldType() {
        return Defaults.FIELD_TYPE;
    }

    @Override
    public FieldDataType defaultFieldDataType() {
        return new FieldDataType("long");
    }

    @Override
    protected int maxPrecisionStep() {
        return 64;
    }

    @Override
    public Long value(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof BytesRef) {
            return Numbers.bytesToLong((BytesRef) value);
        }
        return ipToLong(value.toString());
    }

    /**
     * IPs should return as a string.
     */
    @Override
    public Object valueForSearch(Object value) {
        Long val = value(value);
        if (val == null) {
            return null;
        }
        return longToIp(val);
    }

    @Override
    public BytesRef indexedValueForSearch(Object value) {
        BytesRef bytesRef = new BytesRef();
        NumericUtils.longToPrefixCoded(parseValue(value), precisionStep(), bytesRef);
        return bytesRef;
    }

    private long parseValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof BytesRef) {
            return ipToLong(((BytesRef) value).utf8ToString());
        }
        return ipToLong(value.toString());
    }

    @Override
    public Query fuzzyQuery(String value, String minSim, int prefixLength, int maxExpansions, boolean transpositions) {
        long iValue = ipToLong(value);
        long iSim;
        try {
            iSim = ipToLong(minSim);
        } catch (ElasticSearchIllegalArgumentException e) {
            try {
                iSim = Long.parseLong(minSim);
            } catch (NumberFormatException e1) {
                iSim = (long) Double.parseDouble(minSim);
            }
        }
        return NumericRangeQuery.newLongRange(names.indexName(), precisionStep,
                iValue - iSim,
                iValue + iSim,
                true, true);
    }

    @Override
    public Query rangeQuery(Object lowerTerm, Object upperTerm, boolean includeLower, boolean includeUpper, @Nullable QueryParseContext context) {
        return NumericRangeQuery.newLongRange(names.indexName(), precisionStep,
                lowerTerm == null ? null : parseValue(lowerTerm),
                upperTerm == null ? null : parseValue(upperTerm),
                includeLower, includeUpper);
    }

    @Override
    public Filter rangeFilter(Object lowerTerm, Object upperTerm, boolean includeLower, boolean includeUpper, @Nullable QueryParseContext context) {
        return NumericRangeFilter.newLongRange(names.indexName(), precisionStep,
                lowerTerm == null ? null : parseValue(lowerTerm),
                upperTerm == null ? null : parseValue(upperTerm),
                includeLower, includeUpper);
    }

    @Override
    public Filter rangeFilter(IndexFieldDataService fieldData, Object lowerTerm, Object upperTerm, boolean includeLower, boolean includeUpper, @Nullable QueryParseContext context) {
        return NumericRangeFieldDataFilter.newLongRange((IndexNumericFieldData) fieldData.getForField(this),
                lowerTerm == null ? null : parseValue(lowerTerm),
                upperTerm == null ? null : parseValue(upperTerm),
                includeLower, includeUpper);
    }

    @Override
    public Filter nullValueFilter() {
        if (nullValue == null) {
            return null;
        }
        final long value = ipToLong(nullValue);
        return NumericRangeFilter.newLongRange(names.indexName(), precisionStep,
                value,
                value,
                true, true);
    }

    @Override
    protected Field innerParseCreateField(ParseContext context) throws IOException {
        String ipAsString;
        if (context.externalValueSet()) {
            ipAsString = (String) context.externalValue();
            if (ipAsString == null) {
                ipAsString = nullValue;
            }
        } else {
            if (context.parser().currentToken() == XContentParser.Token.VALUE_NULL) {
                ipAsString = nullValue;
            } else {
                ipAsString = context.parser().text();
            }
        }

        if (ipAsString == null) {
            return null;
        }
        if (context.includeInAll(includeInAll, this)) {
            context.allEntries().addText(names.fullName(), ipAsString, boost);
        }

        final long value = ipToLong(ipAsString);
        return new LongFieldMapper.CustomLongNumericField(this, value, fieldType);
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
            this.nullValue = ((IpFieldMapper) mergeWith).nullValue;
        }
    }

    @Override
    protected void doXContentBody(XContentBuilder builder) throws IOException {
        super.doXContentBody(builder);
        if (precisionStep != Defaults.PRECISION_STEP) {
            builder.field("precision_step", precisionStep);
        }
        if (nullValue != null) {
            builder.field("null_value", nullValue);
        }
        if (includeInAll != null) {
            builder.field("include_in_all", includeInAll);
        }
    }

    public static class NumericIpAnalyzer extends NumericAnalyzer<NumericIpTokenizer> {

        private final int precisionStep;

        public NumericIpAnalyzer() {
            this(NumericUtils.PRECISION_STEP_DEFAULT);
        }

        public NumericIpAnalyzer(int precisionStep) {
            this.precisionStep = precisionStep;
        }

        @Override
        protected NumericIpTokenizer createNumericTokenizer(Reader reader, char[] buffer) throws IOException {
            return new NumericIpTokenizer(reader, precisionStep, buffer);
        }
    }

    public static class NumericIpTokenizer extends NumericTokenizer {

        public NumericIpTokenizer(Reader reader, int precisionStep) throws IOException {
            super(reader, new NumericTokenStream(precisionStep), null);
        }

        public NumericIpTokenizer(Reader reader, int precisionStep, char[] buffer) throws IOException {
            super(reader, new NumericTokenStream(precisionStep), buffer, null);
        }

        @Override
        protected void setValue(NumericTokenStream tokenStream, String value) {
            tokenStream.setLongValue(ipToLong(value));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2439.java