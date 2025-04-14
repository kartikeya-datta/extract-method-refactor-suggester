error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1990.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1990.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1990.java
text:
```scala
i@@f (field.name().equals(path)) {

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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexableField;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.mapper.*;
import org.elasticsearch.search.highlight.HighlighterContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.mapper.MapperBuilders.analyzer;

/**
 *
 */
public class AnalyzerMapper implements Mapper, InternalMapper, RootMapper {

    public static final String NAME = "_analyzer";
    public static final String CONTENT_TYPE = "_analyzer";

    public static class Defaults {
        public static final String PATH = "_analyzer";
    }

    public static class Builder extends Mapper.Builder<Builder, AnalyzerMapper> {

        private String field = Defaults.PATH;

        public Builder() {
            super(CONTENT_TYPE);
            this.builder = this;
        }

        public Builder field(String field) {
            this.field = field;
            return this;
        }

        @Override
        public AnalyzerMapper build(BuilderContext context) {
            return new AnalyzerMapper(field);
        }
    }

    public static class TypeParser implements Mapper.TypeParser {
        @Override
        public Mapper.Builder parse(String name, Map<String, Object> node, ParserContext parserContext) throws MapperParsingException {
            AnalyzerMapper.Builder builder = analyzer();
            for (Map.Entry<String, Object> entry : node.entrySet()) {
                String fieldName = Strings.toUnderscoreCase(entry.getKey());
                Object fieldNode = entry.getValue();
                if (fieldName.equals("path")) {
                    builder.field(fieldNode.toString());
                }
            }
            return builder;
        }
    }

    private final String path;

    public AnalyzerMapper() {
        this(Defaults.PATH);
    }

    public AnalyzerMapper(String path) {
        this.path = path.intern();
    }

    @Override
    public String name() {
        return CONTENT_TYPE;
    }

    @Override
    public void preParse(ParseContext context) throws IOException {
    }

    @Override
    public void postParse(ParseContext context) throws IOException {
        Analyzer analyzer = context.docMapper().mappers().indexAnalyzer();
        if (path != null) {
            String value = null;
            List<IndexableField> fields = context.doc().getFields();
            for (int i = 0, fieldsSize = fields.size(); i < fieldsSize; i++) {
                IndexableField field = fields.get(i);
                if (field.name() == path) {
                    value = field.stringValue();
                    break;
                }
            }
            if (value == null) {
                value = context.ignoredValue(path);
            }
            if (value != null) {
                analyzer = context.analysisService().analyzer(value);
                if (analyzer == null) {
                    throw new MapperParsingException("No analyzer found for [" + value + "] from path [" + path + "]");
                }
                analyzer = context.docMapper().mappers().indexAnalyzer(analyzer);
            }
        }
        context.analyzer(analyzer);
    }

    @Override
    public boolean includeInObject() {
        return false;
    }

    public Analyzer setAnalyzer(HighlighterContext context){
        if (context.analyzer() != null){
            return context.analyzer();
        }

        Analyzer analyzer = null;

        if (path != null) {
            String analyzerName = (String) context.context.lookup().source().extractValue(path);
            analyzer = context.context.mapperService().analysisService().analyzer(analyzerName);
        }

        if (analyzer == null) {
            analyzer = context.context.mapperService().documentMapper(context.hitContext.hit().type()).mappers().indexAnalyzer();
        }
        context.analyzer(analyzer);

        return analyzer;
    }

    @Override
    public void parse(ParseContext context) throws IOException {
    }

    @Override
    public void merge(Mapper mergeWith, MergeContext mergeContext) throws MergeMappingException {
    }

    @Override
    public void traverse(FieldMapperListener fieldMapperListener) {
    }

    @Override
    public void traverse(ObjectMapperListener objectMapperListener) {
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        if (path.equals(Defaults.PATH)) {
            return builder;
        }
        builder.startObject(CONTENT_TYPE);
        if (!path.equals(Defaults.PATH)) {
            builder.field("path", path);
        }
        builder.endObject();
        return builder;
    }

    @Override
    public void close() {

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1990.java