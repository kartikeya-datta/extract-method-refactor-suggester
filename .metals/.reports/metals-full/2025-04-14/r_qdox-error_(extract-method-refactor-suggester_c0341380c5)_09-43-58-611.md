error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5550.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5550.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5550.java
text:
```scala
i@@ndexSettings, MultiFields.empty(), null);

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
import org.apache.lucene.document.NumericDocValuesField;
import org.elasticsearch.common.Explicit;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.joda.FormatDateTimeFormatter;
import org.elasticsearch.common.joda.Joda;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.codec.docvaluesformat.DocValuesFormatProvider;
import org.elasticsearch.index.codec.postingsformat.PostingsFormatProvider;
import org.elasticsearch.index.mapper.*;
import org.elasticsearch.index.mapper.core.DateFieldMapper;
import org.elasticsearch.index.mapper.core.LongFieldMapper;
import org.elasticsearch.index.mapper.core.NumberFieldMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.xcontent.support.XContentMapValues.nodeBooleanValue;
import static org.elasticsearch.index.mapper.MapperBuilders.timestamp;
import static org.elasticsearch.index.mapper.core.TypeParsers.parseDateTimeFormatter;
import static org.elasticsearch.index.mapper.core.TypeParsers.parseField;

/**
 */
public class TimestampFieldMapper extends DateFieldMapper implements InternalMapper, RootMapper {

    public static final String NAME = "_timestamp";
    public static final String CONTENT_TYPE = "_timestamp";
    public static final String DEFAULT_DATE_TIME_FORMAT = "dateOptionalTime";

    public static class Defaults extends DateFieldMapper.Defaults {
        public static final String NAME = "_timestamp";

        public static final FieldType FIELD_TYPE = new FieldType(DateFieldMapper.Defaults.FIELD_TYPE);

        static {
            FIELD_TYPE.setStored(false);
            FIELD_TYPE.setIndexed(true);
            FIELD_TYPE.setTokenized(false);
            FIELD_TYPE.freeze();
        }

        public static final EnabledAttributeMapper ENABLED = EnabledAttributeMapper.DISABLED;
        public static final String PATH = null;
        public static final FormatDateTimeFormatter DATE_TIME_FORMATTER = Joda.forPattern(DEFAULT_DATE_TIME_FORMAT);
    }

    public static class Builder extends NumberFieldMapper.Builder<Builder, TimestampFieldMapper> {

        private EnabledAttributeMapper enabledState = EnabledAttributeMapper.UNSET_DISABLED;
        private String path = Defaults.PATH;
        private FormatDateTimeFormatter dateTimeFormatter = Defaults.DATE_TIME_FORMATTER;

        public Builder() {
            super(Defaults.NAME, new FieldType(Defaults.FIELD_TYPE));
        }

        public Builder enabled(EnabledAttributeMapper enabledState) {
            this.enabledState = enabledState;
            return builder;
        }

        public Builder path(String path) {
            this.path = path;
            return builder;
        }

        public Builder dateTimeFormatter(FormatDateTimeFormatter dateTimeFormatter) {
            this.dateTimeFormatter = dateTimeFormatter;
            return builder;
        }

        @Override
        public TimestampFieldMapper build(BuilderContext context) {
            boolean roundCeil = Defaults.ROUND_CEIL;
            if (context.indexSettings() != null) {
                Settings settings = context.indexSettings();
                roundCeil =  settings.getAsBoolean("index.mapping.date.round_ceil", settings.getAsBoolean("index.mapping.date.parse_upper_inclusive", Defaults.ROUND_CEIL));
            }
            return new TimestampFieldMapper(fieldType, docValues, enabledState, path, dateTimeFormatter, roundCeil,
                    ignoreMalformed(context), coerce(context), postingsProvider, docValuesProvider, normsLoading, fieldDataSettings, context.indexSettings());
        }
    }

    public static class TypeParser implements Mapper.TypeParser {
        @Override
        public Mapper.Builder parse(String name, Map<String, Object> node, ParserContext parserContext) throws MapperParsingException {
            TimestampFieldMapper.Builder builder = timestamp();
            parseField(builder, builder.name, node, parserContext);
            for (Map.Entry<String, Object> entry : node.entrySet()) {
                String fieldName = Strings.toUnderscoreCase(entry.getKey());
                Object fieldNode = entry.getValue();
                if (fieldName.equals("enabled")) {
                    EnabledAttributeMapper enabledState = nodeBooleanValue(fieldNode) ? EnabledAttributeMapper.ENABLED : EnabledAttributeMapper.DISABLED;
                    builder.enabled(enabledState);
                } else if (fieldName.equals("path")) {
                    builder.path(fieldNode.toString());
                } else if (fieldName.equals("format")) {
                    builder.dateTimeFormatter(parseDateTimeFormatter(builder.name(), fieldNode.toString()));
                }
            }
            return builder;
        }
    }


    private EnabledAttributeMapper enabledState;

    private final String path;

    public TimestampFieldMapper() {
        this(new FieldType(Defaults.FIELD_TYPE), null, Defaults.ENABLED, Defaults.PATH, Defaults.DATE_TIME_FORMATTER,
                Defaults.ROUND_CEIL, Defaults.IGNORE_MALFORMED, Defaults.COERCE, null, null, null, null, ImmutableSettings.EMPTY);
    }

    protected TimestampFieldMapper(FieldType fieldType, Boolean docValues, EnabledAttributeMapper enabledState, String path,
                                   FormatDateTimeFormatter dateTimeFormatter, boolean roundCeil,
                                   Explicit<Boolean> ignoreMalformed,Explicit<Boolean> coerce, PostingsFormatProvider postingsProvider,
                                   DocValuesFormatProvider docValuesProvider, Loading normsLoading,
                                   @Nullable Settings fieldDataSettings, Settings indexSettings) {
        super(new Names(Defaults.NAME, Defaults.NAME, Defaults.NAME, Defaults.NAME), dateTimeFormatter,
                Defaults.PRECISION_STEP, Defaults.BOOST, fieldType, docValues,
                Defaults.NULL_VALUE, TimeUnit.MILLISECONDS /*always milliseconds*/,
                roundCeil, ignoreMalformed, coerce, postingsProvider, docValuesProvider, null, normsLoading, fieldDataSettings, 
                indexSettings, MultiFields.empty());
        this.enabledState = enabledState;
        this.path = path;
    }

    @Override
    public FieldType defaultFieldType() {
        return Defaults.FIELD_TYPE;
    }

    public boolean enabled() {
        return this.enabledState.enabled;
    }

    public String path() {
        return this.path;
    }

    public FormatDateTimeFormatter dateTimeFormatter() {
        return this.dateTimeFormatter;
    }

    /**
     * Override the default behavior to return a timestamp
     */
    @Override
    public Object valueForSearch(Object value) {
        return value(value);
    }

    @Override
    public void validate(ParseContext context) throws MapperParsingException {
    }

    @Override
    public void preParse(ParseContext context) throws IOException {
        super.parse(context);
    }

    @Override
    public void postParse(ParseContext context) throws IOException {
    }

    @Override
    public void parse(ParseContext context) throws IOException {
        // nothing to do here, we call the parent in preParse
    }

    @Override
    public boolean includeInObject() {
        return true;
    }

    @Override
    protected void innerParseCreateField(ParseContext context, List<Field> fields) throws IOException {
        if (enabledState.enabled) {
            long timestamp = context.sourceToParse().timestamp();
            if (!fieldType.indexed() && !fieldType.stored() && !hasDocValues()) {
                context.ignoredValue(names.indexName(), String.valueOf(timestamp));
            }
            if (fieldType.indexed() || fieldType.stored()) {
                fields.add(new LongFieldMapper.CustomLongNumericField(this, timestamp, fieldType));
            }
            if (hasDocValues()) {
                fields.add(new NumericDocValuesField(names.indexName(), timestamp));
            }
        }
    }

    @Override
    protected String contentType() {
        return CONTENT_TYPE;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        boolean includeDefaults = params.paramAsBoolean("include_defaults", false);

        // if all are defaults, no sense to write it at all
        if (!includeDefaults && fieldType.indexed() == Defaults.FIELD_TYPE.indexed() && customFieldDataSettings == null &&
                fieldType.stored() == Defaults.FIELD_TYPE.stored() && enabledState == Defaults.ENABLED && path == Defaults.PATH
                && dateTimeFormatter.format().equals(Defaults.DATE_TIME_FORMATTER.format())) {
            return builder;
        }
        builder.startObject(CONTENT_TYPE);
        if (includeDefaults || enabledState != Defaults.ENABLED) {
            builder.field("enabled", enabledState.enabled);
        }
        if (enabledState.enabled) {
            if (includeDefaults || fieldType.indexed() != Defaults.FIELD_TYPE.indexed()) {
                builder.field("index", indexTokenizeOptionToString(fieldType.indexed(), fieldType.tokenized()));
            }
            if (includeDefaults || fieldType.stored() != Defaults.FIELD_TYPE.stored()) {
                builder.field("store", fieldType.stored());
            }
            if (includeDefaults || path != Defaults.PATH) {
                builder.field("path", path);
            }
            if (includeDefaults || !dateTimeFormatter.format().equals(Defaults.DATE_TIME_FORMATTER.format())) {
                builder.field("format", dateTimeFormatter.format());
            }
            if (customFieldDataSettings != null) {
                builder.field("fielddata", (Map) customFieldDataSettings.getAsMap());
            } else if (includeDefaults) {
                builder.field("fielddata", (Map) fieldDataType.getSettings().getAsMap());
            }

        }
        builder.endObject();
        return builder;
    }

    @Override
    public void merge(Mapper mergeWith, MergeContext mergeContext) throws MergeMappingException {
        TimestampFieldMapper timestampFieldMapperMergeWith = (TimestampFieldMapper) mergeWith;
        if (!mergeContext.mergeFlags().simulate()) {
            if (timestampFieldMapperMergeWith.enabledState != enabledState && !timestampFieldMapperMergeWith.enabledState.unset()) {
                this.enabledState = timestampFieldMapperMergeWith.enabledState;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5550.java