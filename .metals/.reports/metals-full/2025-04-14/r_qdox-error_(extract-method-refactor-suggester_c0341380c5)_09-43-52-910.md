error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9840.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9840.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9840.java
text:
```scala
.@@endObject().bytes().toBytes();

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

package org.elasticsearch.index.mapper.all;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.lucene.all.AllEntries;
import org.elasticsearch.common.lucene.all.AllField;
import org.elasticsearch.common.lucene.all.AllTermQuery;
import org.elasticsearch.common.lucene.all.AllTokenStream;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.mapper.DocumentMapper;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.mapper.MapperTestUtils;
import org.elasticsearch.index.mapper.ParseContext.Document;
import org.elasticsearch.test.ElasticsearchTestCase;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.elasticsearch.common.io.Streams.copyToBytesFromClasspath;
import static org.elasticsearch.common.io.Streams.copyToStringFromClasspath;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.hamcrest.Matchers.*;

/**
 *
 */
public class SimpleAllMapperTests extends ElasticsearchTestCase {

    @Test
    public void testSimpleAllMappers() throws Exception {
        String mapping = copyToStringFromClasspath("/org/elasticsearch/index/mapper/all/mapping.json");
        DocumentMapper docMapper = MapperTestUtils.newParser().parse(mapping);
        byte[] json = copyToBytesFromClasspath("/org/elasticsearch/index/mapper/all/test1.json");
        Document doc = docMapper.parse(new BytesArray(json)).rootDoc();
        AllField field = (AllField) doc.getField("_all");
        AllEntries allEntries = ((AllTokenStream) field.tokenStream(docMapper.mappers().indexAnalyzer())).allEntries();
        assertThat(allEntries.fields().size(), equalTo(3));
        assertThat(allEntries.fields().contains("address.last.location"), equalTo(true));
        assertThat(allEntries.fields().contains("name.last"), equalTo(true));
        assertThat(allEntries.fields().contains("simple1"), equalTo(true));
        FieldMapper mapper = docMapper.mappers().smartNameFieldMapper("_all");
        assertThat(field.fieldType().omitNorms(), equalTo(true));
        assertThat(mapper.queryStringTermQuery(new Term("_all", "foobar")), Matchers.instanceOf(AllTermQuery.class));
    }

    @Test
    public void testAllMappersNoBoost() throws Exception {
        String mapping = copyToStringFromClasspath("/org/elasticsearch/index/mapper/all/noboost-mapping.json");
        DocumentMapper docMapper = MapperTestUtils.newParser().parse(mapping);
        byte[] json = copyToBytesFromClasspath("/org/elasticsearch/index/mapper/all/test1.json");
        Document doc = docMapper.parse(new BytesArray(json)).rootDoc();
        AllField field = (AllField) doc.getField("_all");
        AllEntries allEntries = ((AllTokenStream) field.tokenStream(docMapper.mappers().indexAnalyzer())).allEntries();
        assertThat(allEntries.fields().size(), equalTo(3));
        assertThat(allEntries.fields().contains("address.last.location"), equalTo(true));
        assertThat(allEntries.fields().contains("name.last"), equalTo(true));
        assertThat(allEntries.fields().contains("simple1"), equalTo(true));
        FieldMapper mapper = docMapper.mappers().smartNameFieldMapper("_all");
        assertThat(field.fieldType().omitNorms(), equalTo(false));
        assertThat(mapper.queryStringTermQuery(new Term("_all", "foobar")), Matchers.instanceOf(TermQuery.class));
    }

    @Test
    public void testAllMappersTermQuery() throws Exception {
        String mapping = copyToStringFromClasspath("/org/elasticsearch/index/mapper/all/mapping_omit_positions_on_all.json");
        DocumentMapper docMapper = MapperTestUtils.newParser().parse(mapping);
        byte[] json = copyToBytesFromClasspath("/org/elasticsearch/index/mapper/all/test1.json");
        Document doc = docMapper.parse(new BytesArray(json)).rootDoc();
        AllField field = (AllField) doc.getField("_all");
        AllEntries allEntries = ((AllTokenStream) field.tokenStream(docMapper.mappers().indexAnalyzer())).allEntries();
        assertThat(allEntries.fields().size(), equalTo(3));
        assertThat(allEntries.fields().contains("address.last.location"), equalTo(true));
        assertThat(allEntries.fields().contains("name.last"), equalTo(true));
        assertThat(allEntries.fields().contains("simple1"), equalTo(true));
        FieldMapper mapper = docMapper.mappers().smartNameFieldMapper("_all");
        assertThat(field.fieldType().omitNorms(), equalTo(false));
        assertThat(mapper.queryStringTermQuery(new Term("_all", "foobar")), Matchers.instanceOf(TermQuery.class));

    }


    @Test
    public void testSimpleAllMappersWithReparse() throws Exception {
        String mapping = copyToStringFromClasspath("/org/elasticsearch/index/mapper/all/mapping.json");
        DocumentMapper docMapper = MapperTestUtils.newParser().parse(mapping);
        String builtMapping = docMapper.mappingSource().string();
        // reparse it
        DocumentMapper builtDocMapper = MapperTestUtils.newParser().parse(builtMapping);
        byte[] json = copyToBytesFromClasspath("/org/elasticsearch/index/mapper/all/test1.json");
        Document doc = builtDocMapper.parse(new BytesArray(json)).rootDoc();

        AllField field = (AllField) doc.getField("_all");
        AllEntries allEntries = ((AllTokenStream) field.tokenStream(docMapper.mappers().indexAnalyzer())).allEntries();
        assertThat(allEntries.fields().size(), equalTo(3));
        assertThat(allEntries.fields().contains("address.last.location"), equalTo(true));
        assertThat(allEntries.fields().contains("name.last"), equalTo(true));
        assertThat(allEntries.fields().contains("simple1"), equalTo(true));
        assertThat(field.fieldType().omitNorms(), equalTo(true));
    }

    @Test
    public void testSimpleAllMappersWithStore() throws Exception {
        String mapping = copyToStringFromClasspath("/org/elasticsearch/index/mapper/all/store-mapping.json");
        DocumentMapper docMapper = MapperTestUtils.newParser().parse(mapping);
        byte[] json = copyToBytesFromClasspath("/org/elasticsearch/index/mapper/all/test1.json");
        Document doc = docMapper.parse(new BytesArray(json)).rootDoc();
        AllField field = (AllField) doc.getField("_all");
        AllEntries allEntries = ((AllTokenStream) field.tokenStream(docMapper.mappers().indexAnalyzer())).allEntries();
        assertThat(allEntries.fields().size(), equalTo(2));
        assertThat(allEntries.fields().contains("name.last"), equalTo(true));
        assertThat(allEntries.fields().contains("simple1"), equalTo(true));

        String text = field.stringValue();
        assertThat(text, equalTo(allEntries.buildText()));
        assertThat(field.fieldType().omitNorms(), equalTo(false));
    }

    @Test
    public void testSimpleAllMappersWithReparseWithStore() throws Exception {
        String mapping = copyToStringFromClasspath("/org/elasticsearch/index/mapper/all/store-mapping.json");
        DocumentMapper docMapper = MapperTestUtils.newParser().parse(mapping);
        String builtMapping = docMapper.mappingSource().string();
        // reparse it
        DocumentMapper builtDocMapper = MapperTestUtils.newParser().parse(builtMapping);
        byte[] json = copyToBytesFromClasspath("/org/elasticsearch/index/mapper/all/test1.json");
        Document doc = builtDocMapper.parse(new BytesArray(json)).rootDoc();

        AllField field = (AllField) doc.getField("_all");
        AllEntries allEntries = ((AllTokenStream) field.tokenStream(docMapper.mappers().indexAnalyzer())).allEntries();
        assertThat(allEntries.fields().size(), equalTo(2));
        assertThat(allEntries.fields().contains("name.last"), equalTo(true));
        assertThat(allEntries.fields().contains("simple1"), equalTo(true));

        String text = field.stringValue();
        assertThat(text, equalTo(allEntries.buildText()));
        assertThat(field.fieldType().omitNorms(), equalTo(false));
    }

    @Test
    public void testRandom() throws Exception {
        boolean omitNorms = false;
        boolean stored = false;
        boolean enabled = true;
        boolean autoBoost = false;
        boolean tv_stored = false;
        boolean tv_payloads = false;
        boolean tv_offsets = false;
        boolean tv_positions = false;
        String similarity = null;
        boolean fieldData = false;
        XContentBuilder mappingBuilder = jsonBuilder();
        mappingBuilder.startObject().startObject("test");
        List<Tuple<String, Boolean>> booleanOptionList = new ArrayList<Tuple<String, Boolean>>();
        boolean allDefault = true;
        if (frequently()) {
            allDefault = false;
            mappingBuilder.startObject("_all");
            if (randomBoolean()) {
                booleanOptionList.add(new Tuple<String, Boolean>("omit_norms", omitNorms = randomBoolean()));
            }
            if (randomBoolean()) {
                booleanOptionList.add(new Tuple<String, Boolean>("store", stored = randomBoolean()));
            }
            if (randomBoolean()) {
                booleanOptionList.add(new Tuple<String, Boolean>("store_term_vectors", tv_stored = randomBoolean()));
            }
            if (randomBoolean()) {
                booleanOptionList.add(new Tuple<String, Boolean>("enabled", enabled = randomBoolean()));
            }
            if (randomBoolean()) {
                booleanOptionList.add(new Tuple<String, Boolean>("auto_boost", autoBoost = randomBoolean()));
            }
            if (randomBoolean()) {
                booleanOptionList.add(new Tuple<String, Boolean>("store_term_vector_offsets", tv_offsets = randomBoolean()));
            }
            if (randomBoolean()) {
                booleanOptionList.add(new Tuple<String, Boolean>("store_term_vector_positions", tv_positions = randomBoolean()));
            }
            if (randomBoolean()) {
                booleanOptionList.add(new Tuple<String, Boolean>("store_term_vector_payloads", tv_payloads = randomBoolean()));
            }
            Collections.shuffle(booleanOptionList, getRandom());
            for (Tuple<String, Boolean> option : booleanOptionList) {
                mappingBuilder.field(option.v1(), option.v2().booleanValue());
            }
            tv_stored |= tv_positions || tv_payloads || tv_offsets;
            if (randomBoolean()) {
                mappingBuilder.field("similarity", similarity = randomBoolean() ? "BM25" : "TF/IDF");
            }
            if (randomBoolean()) {
                fieldData = true;
                mappingBuilder.startObject("fielddata");
                mappingBuilder.field("foo", "bar");
                mappingBuilder.endObject();
            }
            mappingBuilder.endObject();
        }

        String mapping = mappingBuilder.endObject().endObject().bytes().toUtf8();
        logger.info(mapping);
        DocumentMapper docMapper = MapperTestUtils.newParser().parse(mapping);
        String builtMapping = docMapper.mappingSource().string();
        // reparse it
        DocumentMapper builtDocMapper = MapperTestUtils.newParser().parse(builtMapping);

        byte[] json = jsonBuilder().startObject()
                .field("foo", "bar")
                .field("_id", 1)
                .field("foobar", "foobar")
                .endObject().bytes().array();
        Document doc = builtDocMapper.parse(new BytesArray(json)).rootDoc();
        AllField field = (AllField) doc.getField("_all");
        if (enabled) {
            assertThat(field.fieldType().omitNorms(), equalTo(omitNorms));
            assertThat(field.fieldType().stored(), equalTo(stored));
            assertThat(field.fieldType().storeTermVectorOffsets(), equalTo(tv_offsets));
            assertThat(field.fieldType().storeTermVectorPayloads(), equalTo(tv_payloads));
            assertThat(field.fieldType().storeTermVectorPositions(), equalTo(tv_positions));
            assertThat(field.fieldType().storeTermVectors(), equalTo(tv_stored));
            AllEntries allEntries = ((AllTokenStream) field.tokenStream(docMapper.mappers().indexAnalyzer())).allEntries();
            assertThat(allEntries.fields().size(), equalTo(2));
            assertThat(allEntries.fields().contains("foobar"), equalTo(true));
            assertThat(allEntries.fields().contains("foo"), equalTo(true));
            if (!stored) {
                assertThat(field.stringValue(), nullValue());
            }
            String text = stored ? field.stringValue() : "bar foobar";
            assertThat(text.trim(), equalTo(allEntries.buildText().trim()));
        } else {
            assertThat(field, nullValue());
        }

        Term term = new Term("foo", "bar");
        Query query = builtDocMapper.allFieldMapper().queryStringTermQuery(term);
        if (autoBoost) {
            assertThat(query, equalTo((Query)new AllTermQuery(term)));
        } else {
            assertThat(query, equalTo((Query)new TermQuery(term)));
        }
        if (similarity == null || similarity.equals("TF/IDF")) {
            assertThat(builtDocMapper.allFieldMapper().similarity(), nullValue());
        }   else {
            assertThat(similarity, equalTo(builtDocMapper.allFieldMapper().similarity().name()));
        }
        assertThat(builtMapping.contains("fielddata"), is(fieldData));
        if (allDefault) {
            BytesStreamOutput bytesStreamOutput = new BytesStreamOutput(0);
            XContentBuilder b =  new XContentBuilder(XContentType.JSON.xContent(), bytesStreamOutput);
            XContentBuilder xContentBuilder = builtDocMapper.allFieldMapper().toXContent(b, ToXContent.EMPTY_PARAMS);
            xContentBuilder.flush();
            assertThat(bytesStreamOutput.size(), equalTo(0));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9840.java