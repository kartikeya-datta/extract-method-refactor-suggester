error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6458.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6458.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6458.java
text:
```scala
a@@ssertThat(fieldData.ramBytesUsed(), greaterThan(0l));

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

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.*;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.common.compress.CompressedString;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.mapper.MapperTestUtils;
import org.elasticsearch.index.mapper.Uid;
import org.elasticsearch.index.mapper.internal.ParentFieldMapper;
import org.elasticsearch.index.mapper.internal.UidFieldMapper;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.search.MultiValueMode;
import org.elasticsearch.test.index.service.StubIndexService;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

/**
 */
public class ParentChildFieldDataTests extends AbstractFieldDataTests {

    private final String parentType = "parent";
    private final String childType = "child";
    private final String grandChildType = "grand-child";

    @Before
    public void before() throws Exception {
        MapperService mapperService = MapperTestUtils.newMapperService(ifdService.index(), ImmutableSettings.Builder.EMPTY_SETTINGS);
        mapperService.merge(
                childType, new CompressedString(PutMappingRequest.buildFromSimplifiedDef(childType, "_parent", "type=" + parentType).string()), true
        );
        mapperService.merge(
                grandChildType, new CompressedString(PutMappingRequest.buildFromSimplifiedDef(grandChildType, "_parent", "type=" + childType).string()), true
        );
        IndexService indexService = new StubIndexService(mapperService);
        ifdService.setIndexService(indexService);

        Document d = new Document();
        d.add(new StringField(UidFieldMapper.NAME, Uid.createUid(parentType, "1"), Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField(UidFieldMapper.NAME, Uid.createUid(childType, "2"), Field.Store.NO));
        d.add(new StringField(ParentFieldMapper.NAME, Uid.createUid(parentType, "1"), Field.Store.NO));
        writer.addDocument(d);
        writer.commit();

        d = new Document();
        d.add(new StringField(UidFieldMapper.NAME, Uid.createUid(childType, "3"), Field.Store.NO));
        d.add(new StringField(ParentFieldMapper.NAME, Uid.createUid(parentType, "1"), Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField(UidFieldMapper.NAME, Uid.createUid(parentType, "2"), Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField(UidFieldMapper.NAME, Uid.createUid(childType, "4"), Field.Store.NO));
        d.add(new StringField(ParentFieldMapper.NAME, Uid.createUid(parentType, "2"), Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField(UidFieldMapper.NAME, Uid.createUid(childType, "5"), Field.Store.NO));
        d.add(new StringField(ParentFieldMapper.NAME, Uid.createUid(parentType, "1"), Field.Store.NO));
        writer.addDocument(d);
        writer.commit();

        d = new Document();
        d.add(new StringField(UidFieldMapper.NAME, Uid.createUid(grandChildType, "6"), Field.Store.NO));
        d.add(new StringField(ParentFieldMapper.NAME, Uid.createUid(childType, "2"), Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField(UidFieldMapper.NAME, Uid.createUid("other-type", "1"), Field.Store.NO));
        writer.addDocument(d);
    }

    @Test
    public void testGetBytesValues() throws Exception {
        IndexFieldData indexFieldData = getForField(childType);
        AtomicFieldData fieldData = indexFieldData.load(refreshReader());
        assertThat(fieldData.getMemorySizeInBytes(), greaterThan(0l));

        BytesValues bytesValues = fieldData.getBytesValues();
        assertThat(bytesValues.setDocument(0), equalTo(1));
        assertThat(bytesValues.nextValue().utf8ToString(), equalTo("1"));

        assertThat(bytesValues.setDocument(1), equalTo(2));
        assertThat(bytesValues.nextValue().utf8ToString(), equalTo("1"));
        assertThat(bytesValues.nextValue().utf8ToString(), equalTo("2"));

        assertThat(bytesValues.setDocument(2), equalTo(2));
        assertThat(bytesValues.nextValue().utf8ToString(), equalTo("1"));
        assertThat(bytesValues.nextValue().utf8ToString(), equalTo("3"));

        assertThat(bytesValues.setDocument(3), equalTo(1));
        assertThat(bytesValues.nextValue().utf8ToString(), equalTo("2"));

        assertThat(bytesValues.setDocument(4), equalTo(2));
        assertThat(bytesValues.nextValue().utf8ToString(), equalTo("2"));
        assertThat(bytesValues.nextValue().utf8ToString(), equalTo("4"));

        assertThat(bytesValues.setDocument(5), equalTo(2));
        assertThat(bytesValues.nextValue().utf8ToString(), equalTo("1"));
        assertThat(bytesValues.nextValue().utf8ToString(), equalTo("5"));

        assertThat(bytesValues.setDocument(6), equalTo(1));
        assertThat(bytesValues.nextValue().utf8ToString(), equalTo("2"));

        assertThat(bytesValues.setDocument(7), equalTo(0));
    }

    @Test
    public void testSorting() throws Exception {
        IndexFieldData indexFieldData = getForField(childType);
        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(writer, true));
        IndexFieldData.XFieldComparatorSource comparator = indexFieldData.comparatorSource("_last", MultiValueMode.MIN);

        TopFieldDocs topDocs = searcher.search(new MatchAllDocsQuery(), 10, new Sort(new SortField(ParentFieldMapper.NAME, comparator, false)));
        assertThat(topDocs.totalHits, equalTo(8));
        assertThat(topDocs.scoreDocs.length, equalTo(8));
        assertThat(topDocs.scoreDocs[0].doc, equalTo(0));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[0]).fields[0]).utf8ToString(), equalTo("1"));
        assertThat(topDocs.scoreDocs[1].doc, equalTo(1));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[1]).fields[0]).utf8ToString(), equalTo("1"));
        assertThat(topDocs.scoreDocs[2].doc, equalTo(2));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[2]).fields[0]).utf8ToString(), equalTo("1"));
        assertThat(topDocs.scoreDocs[3].doc, equalTo(5));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[3]).fields[0]).utf8ToString(), equalTo("1"));
        assertThat(topDocs.scoreDocs[4].doc, equalTo(3));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[4]).fields[0]).utf8ToString(), equalTo("2"));
        assertThat(topDocs.scoreDocs[5].doc, equalTo(4));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[5]).fields[0]).utf8ToString(), equalTo("2"));
        assertThat(topDocs.scoreDocs[6].doc, equalTo(6));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[6]).fields[0]).utf8ToString(), equalTo("2"));
        assertThat(topDocs.scoreDocs[7].doc, equalTo(7));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[7]).fields[0]), equalTo(IndexFieldData.XFieldComparatorSource.MAX_TERM));

        topDocs = searcher.search(new MatchAllDocsQuery(), 10, new Sort(new SortField(ParentFieldMapper.NAME, comparator, true)));
        assertThat(topDocs.totalHits, equalTo(8));
        assertThat(topDocs.scoreDocs.length, equalTo(8));
        assertThat(topDocs.scoreDocs[0].doc, equalTo(3));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[0]).fields[0]).utf8ToString(), equalTo("2"));
        assertThat(topDocs.scoreDocs[1].doc, equalTo(4));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[1]).fields[0]).utf8ToString(), equalTo("2"));
        assertThat(topDocs.scoreDocs[2].doc, equalTo(6));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[2]).fields[0]).utf8ToString(), equalTo("2"));
        assertThat(topDocs.scoreDocs[3].doc, equalTo(0));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[3]).fields[0]).utf8ToString(), equalTo("1"));
        assertThat(topDocs.scoreDocs[4].doc, equalTo(1));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[4]).fields[0]).utf8ToString(), equalTo("1"));
        assertThat(topDocs.scoreDocs[5].doc, equalTo(2));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[5]).fields[0]).utf8ToString(), equalTo("1"));
        assertThat(topDocs.scoreDocs[6].doc, equalTo(5));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[6]).fields[0]).utf8ToString(), equalTo("1"));
        assertThat(topDocs.scoreDocs[7].doc, equalTo(7));
        assertThat(((FieldDoc) topDocs.scoreDocs[7]).fields[0], nullValue());
    }

    @Override
    protected FieldDataType getFieldDataType() {
        return new FieldDataType("_parent");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6458.java