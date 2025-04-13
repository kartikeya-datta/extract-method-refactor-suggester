error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7447.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7447.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7447.java
text:
```scala
S@@et<String> fields = new HashSet<>(Arrays.asList("field1", "field2", "field3"));

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

package org.elasticsearch.index.mapper.lucene;

import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.common.Numbers;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.fieldvisitor.CustomFieldsVisitor;
import org.elasticsearch.index.mapper.DocumentMapper;
import org.elasticsearch.index.mapper.ParsedDocument;
import org.elasticsearch.index.mapper.MapperTestUtils;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 *
 */
public class StoredNumericValuesTest {

    @Test
    public void testBytesAndNumericRepresentation() throws Exception {
        IndexWriter writer = new IndexWriter(new RAMDirectory(), new IndexWriterConfig(Lucene.VERSION, Lucene.STANDARD_ANALYZER));

        String mapping = XContentFactory.jsonBuilder()
                .startObject()
                    .startObject("type")
                        .startObject("properties")
                            .startObject("field1").field("type", "integer").field("store", "yes").endObject()
                            .startObject("field2").field("type", "float").field("store", "yes").endObject()
                            .startObject("field3").field("type", "long").field("store", "yes").endObject()
                        .endObject()
                    .endObject()
                .endObject()
                .string();
        DocumentMapper mapper = MapperTestUtils.newParser().parse(mapping);

        ParsedDocument doc = mapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                    .field("field1", 1)
                    .field("field2", 1.1)
                    .startArray("field3").value(1).value(2).value(3).endArray()
                .endObject()
                .bytes());

        writer.addDocument(doc.rootDoc(), doc.analyzer());

        // Indexing a doc in the old way
        FieldType fieldType = new FieldType();
        fieldType.setStored(true);
        fieldType.setNumericType(FieldType.NumericType.INT);
        Document doc2 = new Document();
        doc2.add(new StoredField("field1", new BytesRef(Numbers.intToBytes(1))));
        doc2.add(new StoredField("field2", new BytesRef(Numbers.floatToBytes(1.1f))));
        doc2.add(new StoredField("field3", new BytesRef(Numbers.longToBytes(1l))));
        doc2.add(new StoredField("field3", new BytesRef(Numbers.longToBytes(2l))));
        doc2.add(new StoredField("field3", new BytesRef(Numbers.longToBytes(3l))));
        writer.addDocument(doc2);

        DirectoryReader reader = DirectoryReader.open(writer, true);
        IndexSearcher searcher = new IndexSearcher(reader);

        Set<String> fields = new HashSet<String>(Arrays.asList("field1", "field2", "field3"));
        CustomFieldsVisitor fieldsVisitor = new CustomFieldsVisitor(fields, false);
        searcher.doc(0, fieldsVisitor);
        fieldsVisitor.postProcess(mapper);
        assertThat(fieldsVisitor.fields().size(), equalTo(3));
        assertThat(fieldsVisitor.fields().get("field1").size(), equalTo(1));
        assertThat((Integer) fieldsVisitor.fields().get("field1").get(0), equalTo(1));
        assertThat(fieldsVisitor.fields().get("field2").size(), equalTo(1));
        assertThat((Float) fieldsVisitor.fields().get("field2").get(0), equalTo(1.1f));
        assertThat(fieldsVisitor.fields().get("field3").size(), equalTo(3));
        assertThat((Long) fieldsVisitor.fields().get("field3").get(0), equalTo(1l));
        assertThat((Long) fieldsVisitor.fields().get("field3").get(1), equalTo(2l));
        assertThat((Long) fieldsVisitor.fields().get("field3").get(2), equalTo(3l));

        // Make sure the doc gets loaded as if it was stored in the new way
        fieldsVisitor.reset();
        searcher.doc(1, fieldsVisitor);
        fieldsVisitor.postProcess(mapper);
        assertThat(fieldsVisitor.fields().size(), equalTo(3));
        assertThat(fieldsVisitor.fields().get("field1").size(), equalTo(1));
        assertThat((Integer) fieldsVisitor.fields().get("field1").get(0), equalTo(1));
        assertThat(fieldsVisitor.fields().get("field2").size(), equalTo(1));
        assertThat((Float) fieldsVisitor.fields().get("field2").get(0), equalTo(1.1f));
        assertThat(fieldsVisitor.fields().get("field3").size(), equalTo(3));
        assertThat((Long) fieldsVisitor.fields().get("field3").get(0), equalTo(1l));
        assertThat((Long) fieldsVisitor.fields().get("field3").get(1), equalTo(2l));
        assertThat((Long) fieldsVisitor.fields().get("field3").get(2), equalTo(3l));

        reader.close();
        writer.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7447.java