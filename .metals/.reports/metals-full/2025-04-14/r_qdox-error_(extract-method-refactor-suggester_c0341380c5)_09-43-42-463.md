error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4126.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4126.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,51]

error in qdox parser
file content:
```java
offset: 51
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4126.java
text:
```scala
@SuppressWarnings({"unchecked"}) @Override public X@@ContentMapper.Builder parse(String name, Map<String, Object> node, ParserContext parserContext) throws MapperParsingException {

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

package org.elasticsearch.index.mapper.xcontent;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.elasticsearch.common.io.FastByteArrayInputStream;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.builder.XContentBuilder;
import org.elasticsearch.index.mapper.FieldMapperListener;
import org.elasticsearch.index.mapper.MapperParsingException;
import org.elasticsearch.index.mapper.MergeMappingException;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.index.mapper.xcontent.XContentMapperBuilders.*;
import static org.elasticsearch.index.mapper.xcontent.XContentTypeParsers.*;
import static org.elasticsearch.plugin.mapper.attachments.tika.TikaInstance.*;

/**
 * <pre>
 *      field1 : "..."
 * </pre>
 * <p>Or:
 * <pre>
 * {
 *      file1 : {
 *          _content_type : "application/pdf",
 *          _name : "..../something.pdf",
 *          content : ""
 *      }
 * }
 * </pre>
 *
 * @author kimchy (shay.banon)
 */
public class XContentAttachmentMapper implements XContentMapper {

    public static final String CONTENT_TYPE = "attachment";

    public static class Defaults {
        public static final ContentPath.Type PATH_TYPE = ContentPath.Type.FULL;
    }

    public static class Builder extends XContentMapper.Builder<Builder, XContentAttachmentMapper> {

        private ContentPath.Type pathType = Defaults.PATH_TYPE;

        private XContentStringFieldMapper.Builder contentBuilder;

        private XContentStringFieldMapper.Builder titleBuilder = stringField("title");

        private XContentStringFieldMapper.Builder authorBuilder = stringField("author");

        private XContentStringFieldMapper.Builder keywordsBuilder = stringField("keywords");

        private XContentDateFieldMapper.Builder dateBuilder = dateField("date");

        public Builder(String name) {
            super(name);
            this.builder = this;
            this.contentBuilder = stringField(name);
        }

        public Builder pathType(ContentPath.Type pathType) {
            this.pathType = pathType;
            return this;
        }

        public Builder content(XContentStringFieldMapper.Builder content) {
            this.contentBuilder = content;
            return this;
        }

        public Builder date(XContentDateFieldMapper.Builder date) {
            this.dateBuilder = date;
            return this;
        }

        public Builder author(XContentStringFieldMapper.Builder author) {
            this.authorBuilder = author;
            return this;
        }

        public Builder title(XContentStringFieldMapper.Builder title) {
            this.titleBuilder = title;
            return this;
        }

        public Builder keywords(XContentStringFieldMapper.Builder keywords) {
            this.keywordsBuilder = keywords;
            return this;
        }

        @Override public XContentAttachmentMapper build(BuilderContext context) {
            ContentPath.Type origPathType = context.path().pathType();
            context.path().pathType(pathType);

            // create the content mapper under the actual name
            XContentStringFieldMapper contentMapper = contentBuilder.build(context);

            // create the DC one under the name
            context.path().add(name);
            XContentDateFieldMapper dateMapper = dateBuilder.build(context);
            XContentStringFieldMapper authorMapper = authorBuilder.build(context);
            XContentStringFieldMapper titleMapper = titleBuilder.build(context);
            XContentStringFieldMapper keywordsMapper = keywordsBuilder.build(context);
            context.path().remove();

            context.path().pathType(origPathType);

            return new XContentAttachmentMapper(name, pathType, contentMapper, dateMapper, titleMapper, authorMapper, keywordsMapper);
        }
    }

    /**
     * <pre>
     *  field1 : { type : "attachment" }
     * </pre>
     * Or:
     * <pre>
     *  field1 : {
     *      type : "attachment",
     *      fields : {
     *          field1 : {type : "binary"},
     *          title : {store : "yes"},
     *          date : {store : "yes"}
     *      }
     * }
     * </pre>
     *
     * @author kimchy (shay.banon)
     */
    public static class TypeParser implements XContentTypeParser {

        @Override public XContentMapper.Builder parse(String name, Map<String, Object> node, ParserContext parserContext) throws MapperParsingException {
            XContentAttachmentMapper.Builder builder = new XContentAttachmentMapper.Builder(name);

            for (Map.Entry<String, Object> entry : node.entrySet()) {
                String fieldName = entry.getKey();
                Object fieldNode = entry.getValue();
                if (fieldName.equals("path")) {
                    builder.pathType(parsePathType(name, fieldNode.toString()));
                } else if (fieldName.equals("fields")) {
                    Map<String, Object> fieldsNode = (Map<String, Object>) fieldNode;
                    for (Map.Entry<String, Object> entry1 : fieldsNode.entrySet()) {
                        String propName = entry1.getKey();
                        Object propNode = entry1.getValue();

                        if (name.equals(propName)) {
                            // that is the content
                            builder.content((XContentStringFieldMapper.Builder) parserContext.typeParser("string").parse(name, (Map<String, Object>) propNode, parserContext));
                        } else if ("date".equals(propName)) {
                            builder.date((XContentDateFieldMapper.Builder) parserContext.typeParser("date").parse("date", (Map<String, Object>) propNode, parserContext));
                        } else if ("title".equals(propName)) {
                            builder.title((XContentStringFieldMapper.Builder) parserContext.typeParser("string").parse("title", (Map<String, Object>) propNode, parserContext));
                        } else if ("author".equals(propName)) {
                            builder.author((XContentStringFieldMapper.Builder) parserContext.typeParser("string").parse("author", (Map<String, Object>) propNode, parserContext));
                        } else if ("keywords".equals(propName)) {
                            builder.keywords((XContentStringFieldMapper.Builder) parserContext.typeParser("string").parse("keywords", (Map<String, Object>) propNode, parserContext));
                        }
                    }
                }
            }

            return builder;
        }
    }

    private final String name;

    private final ContentPath.Type pathType;

    private final XContentStringFieldMapper contentMapper;

    private final XContentDateFieldMapper dateMapper;

    private final XContentStringFieldMapper authorMapper;

    private final XContentStringFieldMapper titleMapper;

    private final XContentStringFieldMapper keywordsMapper;

    public XContentAttachmentMapper(String name, ContentPath.Type pathType, XContentStringFieldMapper contentMapper,
                                    XContentDateFieldMapper dateMapper, XContentStringFieldMapper titleMapper, XContentStringFieldMapper authorMapper,
                                    XContentStringFieldMapper keywordsMapper) {
        this.name = name;
        this.pathType = pathType;
        this.contentMapper = contentMapper;
        this.dateMapper = dateMapper;
        this.titleMapper = titleMapper;
        this.authorMapper = authorMapper;
        this.keywordsMapper = keywordsMapper;
    }

    @Override public String name() {
        return name;
    }

    @Override public void parse(ParseContext context) throws IOException {
        byte[] content = null;
        String contentType = null;
        String name = null;

        XContentParser parser = context.parser();
        XContentParser.Token token = parser.currentToken();
        if (token == XContentParser.Token.VALUE_STRING) {
            content = parser.binaryValue();
        } else {
            String currentFieldName = null;
            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    currentFieldName = parser.currentName();
                } else if (token == XContentParser.Token.VALUE_STRING) {
                    if ("content".equals(currentFieldName)) {
                        content = parser.binaryValue();
                    } else if ("_content_type".equals(currentFieldName)) {
                        contentType = parser.text();
                    } else if ("_name".equals(currentFieldName)) {
                        name = parser.text();
                    }
                }
            }
        }

        Metadata metadata = new Metadata();
        if (contentType != null) {
            metadata.add(Metadata.CONTENT_TYPE, contentType);
        }
        if (name != null) {
            metadata.add(Metadata.RESOURCE_NAME_KEY, name);
        }

        String parsedContent;
        try {
            parsedContent = tika().parseToString(new FastByteArrayInputStream(content), metadata);
        } catch (TikaException e) {
            throw new MapperParsingException("Failed to extract text for [" + name + "]", e);
        }

        context.externalValue(parsedContent);
        contentMapper.parse(context);

        context.externalValue(metadata.get(Metadata.DATE));
        dateMapper.parse(context);

        context.externalValue(metadata.get(Metadata.TITLE));
        titleMapper.parse(context);

        context.externalValue(metadata.get(Metadata.AUTHOR));
        authorMapper.parse(context);

        context.externalValue(metadata.get(Metadata.KEYWORDS));
        keywordsMapper.parse(context);
    }

    @Override public void merge(XContentMapper mergeWith, MergeContext mergeContext) throws MergeMappingException {
        // ignore this for now
    }

    @Override public void traverse(FieldMapperListener fieldMapperListener) {
        contentMapper.traverse(fieldMapperListener);
        dateMapper.traverse(fieldMapperListener);
        titleMapper.traverse(fieldMapperListener);
        authorMapper.traverse(fieldMapperListener);
        keywordsMapper.traverse(fieldMapperListener);
    }

    @Override public void toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(name);
        builder.field("type", CONTENT_TYPE);
        builder.field("path", pathType.name().toLowerCase());

        builder.startObject("fields");
        contentMapper.toXContent(builder, params);
        authorMapper.toXContent(builder, params);
        titleMapper.toXContent(builder, params);
        dateMapper.toXContent(builder, params);
        keywordsMapper.toXContent(builder, params);
        builder.endObject();

        builder.endObject();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4126.java