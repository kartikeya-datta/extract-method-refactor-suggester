error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2475.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2475.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2475.java
text:
```scala
B@@ytesValues bytesValues = fieldData.getBytesValues();

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

package org.elasticsearch.index.fielddata;

import com.carrotsearch.hppc.ObjectArrayList;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.util.CollectionUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.mapper.DocumentMapper;
import org.elasticsearch.index.mapper.MapperTestUtils;
import org.elasticsearch.index.mapper.ParsedDocument;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

/**
 *
 */
public class BinaryDVFieldDataTests extends AbstractFieldDataTests {

    @Test
    public void testDocValue() throws Exception {
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("test")
                .startObject("properties")
                .startObject("field")
                .field("type", "binary")
                .startObject("fielddata").field("format", "doc_values").endObject()
                .endObject()
                .endObject()
                .endObject().endObject().string();

        final DocumentMapper mapper = MapperTestUtils.newParser().parse(mapping);


        ObjectArrayList<byte[]> bytesList1 = new ObjectArrayList<>(2);
        bytesList1.add(randomBytes());
        bytesList1.add(randomBytes());
        XContentBuilder doc = XContentFactory.jsonBuilder().startObject().startArray("field").value(bytesList1.get(0)).value(bytesList1.get(1)).endArray().endObject();
        ParsedDocument d = mapper.parse("test", "1", doc.bytes());
        writer.addDocument(d.rootDoc());

        byte[] bytes1 = randomBytes();
        doc = XContentFactory.jsonBuilder().startObject().field("field", bytes1).endObject();
        d = mapper.parse("test", "2", doc.bytes());
        writer.addDocument(d.rootDoc());

        doc = XContentFactory.jsonBuilder().startObject().endObject();
        d = mapper.parse("test", "3", doc.bytes());
        writer.addDocument(d.rootDoc());

        // test remove duplicate value
        ObjectArrayList<byte[]> bytesList2 = new ObjectArrayList<>(2);
        bytesList2.add(randomBytes());
        bytesList2.add(randomBytes());
        doc = XContentFactory.jsonBuilder().startObject().startArray("field").value(bytesList2.get(0)).value(bytesList2.get(1)).value(bytesList2.get(0)).endArray().endObject();
        d = mapper.parse("test", "4", doc.bytes());
        writer.addDocument(d.rootDoc());

        AtomicReaderContext reader = refreshReader();
        IndexFieldData indexFieldData = getForField("field");
        AtomicFieldData fieldData = indexFieldData.load(reader);

        BytesValues bytesValues = fieldData.getBytesValues(randomBoolean());

        CollectionUtils.sortAndDedup(bytesList1);
        assertThat(bytesValues.setDocument(0), equalTo(2));
        assertThat(bytesValues.nextValue(), equalTo(new BytesRef(bytesList1.get(0))));
        assertThat(bytesValues.nextValue(), equalTo(new BytesRef(bytesList1.get(1))));

        assertThat(bytesValues.setDocument(1), equalTo(1));
        assertThat(bytesValues.nextValue(), equalTo(new BytesRef(bytes1)));

        assertThat(bytesValues.setDocument(2), equalTo(0));

        CollectionUtils.sortAndDedup(bytesList2);
        assertThat(bytesValues.setDocument(3), equalTo(2));
        assertThat(bytesValues.nextValue(), equalTo(new BytesRef(bytesList2.get(0))));
        assertThat(bytesValues.nextValue(), equalTo(new BytesRef(bytesList2.get(1))));
    }

    private byte[] randomBytes() {
        int size = randomIntBetween(10, 1000);
        byte[] bytes = new byte[size];
        getRandom().nextBytes(bytes);
        return bytes;
    }

    @Override
    protected FieldDataType getFieldDataType() {
        return new FieldDataType("binary", ImmutableSettings.builder().put("format", "doc_values"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2475.java