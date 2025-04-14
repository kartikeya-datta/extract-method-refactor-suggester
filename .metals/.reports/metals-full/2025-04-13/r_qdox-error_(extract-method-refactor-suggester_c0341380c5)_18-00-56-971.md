error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10161.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10161.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10161.java
text:
```scala
public F@@ieldDataType fieldDataType() {

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.lucene.BytesRefs;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.lucene.search.XTermsFilter;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.codec.postingsformat.PostingsFormatProvider;
import org.elasticsearch.index.fielddata.FieldDataType;
import org.elasticsearch.index.mapper.*;
import org.elasticsearch.index.mapper.core.AbstractFieldMapper;
import org.elasticsearch.index.query.QueryParseContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ParentFieldMapper extends AbstractFieldMapper<Uid> implements InternalMapper, RootMapper {

    public static final String NAME = "_parent";

    public static final String CONTENT_TYPE = "_parent";

    public static class Defaults extends AbstractFieldMapper.Defaults {
        public static final String NAME = ParentFieldMapper.NAME;

        public static final FieldType FIELD_TYPE = new FieldType(AbstractFieldMapper.Defaults.FIELD_TYPE);

        static {
            FIELD_TYPE.setIndexed(true);
            FIELD_TYPE.setTokenized(false);
            FIELD_TYPE.setStored(true);
            FIELD_TYPE.setOmitNorms(true);
            FIELD_TYPE.setIndexOptions(IndexOptions.DOCS_ONLY);
            FIELD_TYPE.freeze();
        }
    }

    public static class Builder extends Mapper.Builder<Builder, ParentFieldMapper> {

        protected String indexName;

        private String type;
        protected PostingsFormatProvider postingsFormat;

        public Builder() {
            super(Defaults.NAME);
            this.indexName = name;
        }

        public Builder type(String type) {
            this.type = type;
            return builder;
        }

        protected Builder postingsFormat(PostingsFormatProvider postingsFormat) {
            this.postingsFormat = postingsFormat;
            return builder;
        }

        @Override
        public ParentFieldMapper build(BuilderContext context) {
            if (type == null) {
                throw new MapperParsingException("Parent mapping must contain the parent type");
            }
            return new ParentFieldMapper(name, indexName, type, postingsFormat);
        }
    }

    public static class TypeParser implements Mapper.TypeParser {
        @Override
        public Mapper.Builder parse(String name, Map<String, Object> node, ParserContext parserContext) throws MapperParsingException {
            ParentFieldMapper.Builder builder = new ParentFieldMapper.Builder();
            for (Map.Entry<String, Object> entry : node.entrySet()) {
                String fieldName = Strings.toUnderscoreCase(entry.getKey());
                Object fieldNode = entry.getValue();
                if (fieldName.equals("type")) {
                    builder.type(fieldNode.toString());
                } else if (fieldName.equals("postings_format")) {
                    String postingFormatName = fieldNode.toString();
                    builder.postingsFormat(parserContext.postingFormatService().get(postingFormatName));
                }
            }
            return builder;
        }
    }

    private final String type;
    private final BytesRef typeAsBytes;

    protected ParentFieldMapper(String name, String indexName, String type, PostingsFormatProvider postingsFormat) {
        super(new Names(name, indexName, indexName, name), Defaults.BOOST, new FieldType(Defaults.FIELD_TYPE),
                Lucene.KEYWORD_ANALYZER, Lucene.KEYWORD_ANALYZER, postingsFormat, null);
        this.type = type;
        this.typeAsBytes = new BytesRef(type);
    }

    public String type() {
        return type;
    }

    @Override
    public FieldType defaultFieldType() {
        return Defaults.FIELD_TYPE;
    }

    @Override
    public org.elasticsearch.index.fielddata.FieldDataType fieldDataType2() {
        return new FieldDataType("string");
    }

    @Override
    public void preParse(ParseContext context) throws IOException {
    }

    @Override
    public void postParse(ParseContext context) throws IOException {
        parse(context);
    }

    @Override
    public void validate(ParseContext context) throws MapperParsingException {
    }

    @Override
    public boolean includeInObject() {
        return true;
    }

    @Override
    protected Field parseCreateField(ParseContext context) throws IOException {
        if (context.parser().currentName() != null && context.parser().currentName().equals(Defaults.NAME)) {
            // we are in the parsing of _parent phase
            String parentId = context.parser().text();
            context.sourceToParse().parent(parentId);
            return new Field(names.indexName(), Uid.createUid(context.stringBuilder(), type, parentId), fieldType);
        }
        // otherwise, we are running it post processing of the xcontent
        String parsedParentId = context.doc().get(Defaults.NAME);
        if (context.sourceToParse().parent() != null) {
            String parentId = context.sourceToParse().parent();
            if (parsedParentId == null) {
                if (parentId == null) {
                    throw new MapperParsingException("No parent id provided, not within the document, and not externally");
                }
                // we did not add it in the parsing phase, add it now
                return new Field(names.indexName(), Uid.createUid(context.stringBuilder(), type, parentId), fieldType);
            } else if (parentId != null && !parsedParentId.equals(Uid.createUid(context.stringBuilder(), type, parentId))) {
                throw new MapperParsingException("Parent id mismatch, document value is [" + Uid.createUid(parsedParentId).id() + "], while external value is [" + parentId + "]");
            }
        }
        // we have parent mapping, yet no value was set, ignore it...
        return null;
    }

    @Override
    public Uid value(Object value) {
        if (value == null) {
            return null;
        }
        return Uid.createUid(value.toString());
    }

    @Override
    public Object valueForSearch(Object value) {
        if (value == null) {
            return null;
        }
        String sValue = value.toString();
        if (sValue == null) {
            return null;
        }
        int index = sValue.indexOf(Uid.DELIMITER);
        if (index == -1) {
            return sValue;
        }
        return sValue.substring(index + 1);
    }

    @Override
    public BytesRef indexedValueForSearch(Object value) {
        if (value instanceof BytesRef) {
            BytesRef bytesRef = (BytesRef) value;
            if (Uid.hasDelimiter(bytesRef)) {
                return bytesRef;
            }
            return Uid.createUidAsBytes(typeAsBytes, bytesRef);
        }
        String sValue = value.toString();
        if (sValue.indexOf(Uid.DELIMITER) == -1) {
            return Uid.createUidAsBytes(type, sValue);
        }
        return super.indexedValueForSearch(value);
    }

    @Override
    public Query termQuery(Object value, @Nullable QueryParseContext context) {
        if (context == null) {
            return super.termQuery(value, context);
        }
        return new ConstantScoreQuery(termFilter(value, context));
    }

    @Override
    public Filter termFilter(Object value, @Nullable QueryParseContext context) {
        if (context == null) {
            return super.termFilter(value, context);
        }
        BytesRef bValue = BytesRefs.toBytesRef(value);
        // we use all types, cause we don't know if its exact or not...
        BytesRef[] typesValues = new BytesRef[context.mapperService().types().size()];
        int i = 0;
        for (String type : context.mapperService().types()) {
            typesValues[i++] = Uid.createUidAsBytes(type, bValue);
        }
        return new XTermsFilter(names.indexName(), typesValues);
    }

    @Override
    public Filter termsFilter(List<Object> values, @Nullable QueryParseContext context) {
        if (context == null) {
            return super.termFilter(values, context);
        }
        List<BytesRef> bValues = new ArrayList<BytesRef>(values.size());
        for (Object value : values) {
            BytesRef bValue = BytesRefs.toBytesRef(value);
            // we use all types, cause we don't know if its exact or not...
            for (String type : context.mapperService().types()) {
                bValues.add(Uid.createUidAsBytes(type, bValue));
            }
        }
        return new XTermsFilter(names.indexName(), bValues);
    }

    /**
     * We don't need to analyzer the text, and we need to convert it to UID...
     */
    @Override
    public boolean useTermQueryWithQueryString() {
        return true;
    }

    @Override
    protected String contentType() {
        return CONTENT_TYPE;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(CONTENT_TYPE);
        builder.field("type", type);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10161.java