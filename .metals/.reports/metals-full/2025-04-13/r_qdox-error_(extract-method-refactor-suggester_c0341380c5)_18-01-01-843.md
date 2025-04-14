error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6825.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6825.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6825.java
text:
```scala
s@@uper(new Names(Defaults.NAME, Defaults.NAME, Defaults.NAME, Defaults.NAME), 1.0f, fieldType, null, Lucene.KEYWORD_ANALYZER,

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

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.codec.docvaluesformat.DocValuesFormatProvider;
import org.elasticsearch.index.codec.postingsformat.PostingsFormatProvider;
import org.elasticsearch.index.fielddata.FieldDataType;
import org.elasticsearch.index.mapper.*;
import org.elasticsearch.index.mapper.core.AbstractFieldMapper;
import org.elasticsearch.index.mapper.core.NumberFieldMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.support.XContentMapValues.nodeBooleanValue;
import static org.elasticsearch.index.mapper.MapperBuilders.routing;
import static org.elasticsearch.index.mapper.core.TypeParsers.parseField;

/**
 *
 */
public class RoutingFieldMapper extends AbstractFieldMapper<String> implements InternalMapper, RootMapper {

    public static final String NAME = "_routing";
    public static final String CONTENT_TYPE = "_routing";

    public static class Defaults extends AbstractFieldMapper.Defaults {
        public static final String NAME = "_routing";

        public static final FieldType FIELD_TYPE = new FieldType(AbstractFieldMapper.Defaults.FIELD_TYPE);

        static {
            FIELD_TYPE.setIndexed(true);
            FIELD_TYPE.setTokenized(false);
            FIELD_TYPE.setStored(true);
            FIELD_TYPE.setOmitNorms(true);
            FIELD_TYPE.setIndexOptions(IndexOptions.DOCS_ONLY);
            FIELD_TYPE.freeze();
        }

        public static final boolean REQUIRED = false;
        public static final String PATH = null;
    }

    public static class Builder extends AbstractFieldMapper.Builder<Builder, RoutingFieldMapper> {

        private boolean required = Defaults.REQUIRED;

        private String path = Defaults.PATH;

        public Builder() {
            super(Defaults.NAME, new FieldType(Defaults.FIELD_TYPE));
        }

        public Builder required(boolean required) {
            this.required = required;
            return builder;
        }

        public Builder path(String path) {
            this.path = path;
            return builder;
        }

        @Override
        public RoutingFieldMapper build(BuilderContext context) {
            return new RoutingFieldMapper(fieldType, required, path, postingsProvider, docValuesProvider, fieldDataSettings, context.indexSettings());
        }
    }

    public static class TypeParser implements Mapper.TypeParser {
        @Override
        public Mapper.Builder parse(String name, Map<String, Object> node, ParserContext parserContext) throws MapperParsingException {
            RoutingFieldMapper.Builder builder = routing();
            parseField(builder, builder.name, node, parserContext);
            for (Map.Entry<String, Object> entry : node.entrySet()) {
                String fieldName = Strings.toUnderscoreCase(entry.getKey());
                Object fieldNode = entry.getValue();
                if (fieldName.equals("required")) {
                    builder.required(nodeBooleanValue(fieldNode));
                } else if (fieldName.equals("path")) {
                    builder.path(fieldNode.toString());
                }
            }
            return builder;
        }
    }


    private boolean required;

    private final String path;

    public RoutingFieldMapper() {
        this(new FieldType(Defaults.FIELD_TYPE), Defaults.REQUIRED, Defaults.PATH, null, null, null, ImmutableSettings.EMPTY);
    }

    protected RoutingFieldMapper(FieldType fieldType, boolean required, String path, PostingsFormatProvider postingsProvider,
                                 DocValuesFormatProvider docValuesProvider, @Nullable Settings fieldDataSettings, Settings indexSettings) {
        super(new Names(Defaults.NAME, Defaults.NAME, Defaults.NAME, Defaults.NAME), 1.0f, fieldType, Lucene.KEYWORD_ANALYZER,
                Lucene.KEYWORD_ANALYZER, postingsProvider, docValuesProvider, null, fieldDataSettings, indexSettings);
        this.required = required;
        this.path = path;
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
    public boolean hasDocValues() {
        return false;
    }

    public void markAsRequired() {
        this.required = true;
    }

    public boolean required() {
        return this.required;
    }

    public String path() {
        return this.path;
    }

    public String value(Document document) {
        Field field = (Field) document.getField(names.indexName());
        return field == null ? null : value(field);
    }

    @Override
    public String value(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    @Override
    public void validate(ParseContext context) throws MapperParsingException {
        String routing = context.sourceToParse().routing();
        if (path != null && routing != null) {
            // we have a path, check if we can validate we have the same routing value as the one in the doc...
            String value = null;
            Field field = (Field) context.doc().getField(path);
            if (field != null) {
                value = field.stringValue();
                if (value == null) {
                    // maybe its a numeric field...
                    if (field instanceof NumberFieldMapper.CustomNumericField) {
                        value = ((NumberFieldMapper.CustomNumericField) field).numericAsString();
                    }
                }
            }
            if (value == null) {
                value = context.ignoredValue(path);
            }
            if (!routing.equals(value)) {
                throw new MapperParsingException("External routing [" + routing + "] and document path routing [" + value + "] mismatch");
            }
        }
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
        // no need ot parse here, we either get the routing in the sourceToParse
        // or we don't have routing, if we get it in sourceToParse, we process it in preParse
        // which will always be called
    }

    @Override
    public boolean includeInObject() {
        return true;
    }

    @Override
    protected void parseCreateField(ParseContext context, List<Field> fields) throws IOException {
        if (context.sourceToParse().routing() != null) {
            String routing = context.sourceToParse().routing();
            if (routing != null) {
                if (!fieldType.indexed() && !fieldType.stored()) {
                    context.ignoredValue(names.indexName(), routing);
                    return;
                }
                fields.add(new Field(names.indexName(), routing, fieldType));
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
        if (!includeDefaults && fieldType.indexed() == Defaults.FIELD_TYPE.indexed() &&
                fieldType.stored() == Defaults.FIELD_TYPE.stored() && required == Defaults.REQUIRED && path == Defaults.PATH) {
            return builder;
        }
        builder.startObject(CONTENT_TYPE);
        if (includeDefaults || fieldType.indexed() != Defaults.FIELD_TYPE.indexed()) {
            builder.field("index", indexTokenizeOptionToString(fieldType.indexed(), fieldType.tokenized()));
        }
        if (includeDefaults || fieldType.stored() != Defaults.FIELD_TYPE.stored()) {
            builder.field("store", fieldType.stored());
        }
        if (includeDefaults || required != Defaults.REQUIRED) {
            builder.field("required", required);
        }
        if (includeDefaults || path != Defaults.PATH) {
            builder.field("path", path);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6825.java