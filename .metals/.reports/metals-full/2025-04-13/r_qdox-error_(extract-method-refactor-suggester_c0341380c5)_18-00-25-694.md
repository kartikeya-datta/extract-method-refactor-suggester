error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7450.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7450.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7450.java
text:
```scala
L@@ist<Document> docs = new ArrayList<>();

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

package org.elasticsearch.index.search.nested;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.TermFilter;
import org.apache.lucene.search.*;
import org.apache.lucene.search.join.FixedBitSetCachingWrapperFilter;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.common.lucene.search.AndFilter;
import org.elasticsearch.common.lucene.search.NotFilter;
import org.elasticsearch.common.lucene.search.XFilteredQuery;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.index.fielddata.AbstractFieldDataTests;
import org.elasticsearch.index.fielddata.FieldDataType;
import org.elasticsearch.index.fielddata.fieldcomparator.BytesRefFieldComparatorSource;
import org.elasticsearch.index.fielddata.fieldcomparator.SortMode;
import org.elasticsearch.index.fielddata.plain.PagedBytesIndexFieldData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

/**
 */
public class NestedSortingTests extends AbstractFieldDataTests {

    @Override
    protected FieldDataType getFieldDataType() {
        return new FieldDataType("string", ImmutableSettings.builder().put("format", "paged_bytes"));
    }

    @Test
    public void testNestedSorting() throws Exception {
        List<Document> docs = new ArrayList<Document>();
        Document document = new Document();
        document.add(new StringField("field2", "a", Field.Store.NO));
        document.add(new StringField("filter_1", "T", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("field2", "b", Field.Store.NO));
        document.add(new StringField("filter_1", "T", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("field2", "c", Field.Store.NO));
        document.add(new StringField("filter_1", "T", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("__type", "parent", Field.Store.NO));
        document.add(new StringField("field1", "a", Field.Store.NO));
        docs.add(document);
        writer.addDocuments(docs);
        writer.commit();

        docs.clear();
        document = new Document();
        document.add(new StringField("field2", "c", Field.Store.NO));
        document.add(new StringField("filter_1", "T", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("field2", "d", Field.Store.NO));
        document.add(new StringField("filter_1", "T", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("field2", "e", Field.Store.NO));
        document.add(new StringField("filter_1", "T", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("__type", "parent", Field.Store.NO));
        document.add(new StringField("field1", "b", Field.Store.NO));
        docs.add(document);
        writer.addDocuments(docs);

        docs.clear();
        document = new Document();
        document.add(new StringField("field2", "e", Field.Store.NO));
        document.add(new StringField("filter_1", "T", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("field2", "f", Field.Store.NO));
        document.add(new StringField("filter_1", "T", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("field2", "g", Field.Store.NO));
        document.add(new StringField("filter_1", "T", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("__type", "parent", Field.Store.NO));
        document.add(new StringField("field1", "c", Field.Store.NO));
        docs.add(document);
        writer.addDocuments(docs);

        docs.clear();
        document = new Document();
        document.add(new StringField("field2", "g", Field.Store.NO));
        document.add(new StringField("filter_1", "T", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("field2", "h", Field.Store.NO));
        document.add(new StringField("filter_1", "F", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("field2", "i", Field.Store.NO));
        document.add(new StringField("filter_1", "F", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("__type", "parent", Field.Store.NO));
        document.add(new StringField("field1", "d", Field.Store.NO));
        docs.add(document);
        writer.addDocuments(docs);
        writer.commit();

        docs.clear();
        document = new Document();
        document.add(new StringField("field2", "i", Field.Store.NO));
        document.add(new StringField("filter_1", "F", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("field2", "j", Field.Store.NO));
        document.add(new StringField("filter_1", "F", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("field2", "k", Field.Store.NO));
        document.add(new StringField("filter_1", "F", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("__type", "parent", Field.Store.NO));
        document.add(new StringField("field1", "f", Field.Store.NO));
        docs.add(document);
        writer.addDocuments(docs);

        docs.clear();
        document = new Document();
        document.add(new StringField("field2", "k", Field.Store.NO));
        document.add(new StringField("filter_1", "T", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("field2", "l", Field.Store.NO));
        document.add(new StringField("filter_1", "T", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("field2", "m", Field.Store.NO));
        document.add(new StringField("filter_1", "T", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("__type", "parent", Field.Store.NO));
        document.add(new StringField("field1", "g", Field.Store.NO));
        docs.add(document);
        writer.addDocuments(docs);

        // This doc will not be included, because it doesn't have nested docs
        document = new Document();
        document.add(new StringField("__type", "parent", Field.Store.NO));
        document.add(new StringField("field1", "h", Field.Store.NO));
        writer.addDocument(document);

        docs.clear();
        document = new Document();
        document.add(new StringField("field2", "m", Field.Store.NO));
        document.add(new StringField("filter_1", "T", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("field2", "n", Field.Store.NO));
        document.add(new StringField("filter_1", "F", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("field2", "o", Field.Store.NO));
        document.add(new StringField("filter_1", "F", Field.Store.NO));
        docs.add(document);
        document = new Document();
        document.add(new StringField("__type", "parent", Field.Store.NO));
        document.add(new StringField("field1", "i", Field.Store.NO));
        docs.add(document);
        writer.addDocuments(docs);
        writer.commit();

        // Some garbage docs, just to check if the NestedFieldComparator can deal with this.
        document = new Document();
        document.add(new StringField("fieldXXX", "x", Field.Store.NO));
        writer.addDocument(document);
        document = new Document();
        document.add(new StringField("fieldXXX", "x", Field.Store.NO));
        writer.addDocument(document);
        document = new Document();
        document.add(new StringField("fieldXXX", "x", Field.Store.NO));
        writer.addDocument(document);

        SortMode sortMode = SortMode.MIN;
        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(writer, false));
        PagedBytesIndexFieldData indexFieldData = getForField("field2");
        BytesRefFieldComparatorSource innerSource = new BytesRefFieldComparatorSource(indexFieldData, null, sortMode);
        Filter parentFilter = new TermFilter(new Term("__type", "parent"));
        Filter childFilter = new NotFilter(parentFilter);
        NestedFieldComparatorSource nestedComparatorSource = new NestedFieldComparatorSource(sortMode, innerSource, parentFilter, childFilter);
        ToParentBlockJoinQuery query = new ToParentBlockJoinQuery(new XFilteredQuery(new MatchAllDocsQuery(), childFilter), new FixedBitSetCachingWrapperFilter(parentFilter), ScoreMode.None);

        Sort sort = new Sort(new SortField("field2", nestedComparatorSource));
        TopFieldDocs topDocs = searcher.search(query, 5, sort);
        assertThat(topDocs.totalHits, equalTo(7));
        assertThat(topDocs.scoreDocs.length, equalTo(5));
        assertThat(topDocs.scoreDocs[0].doc, equalTo(3));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[0]).fields[0]).utf8ToString(), equalTo("a"));
        assertThat(topDocs.scoreDocs[1].doc, equalTo(7));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[1]).fields[0]).utf8ToString(), equalTo("c"));
        assertThat(topDocs.scoreDocs[2].doc, equalTo(11));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[2]).fields[0]).utf8ToString(), equalTo("e"));
        assertThat(topDocs.scoreDocs[3].doc, equalTo(15));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[3]).fields[0]).utf8ToString(), equalTo("g"));
        assertThat(topDocs.scoreDocs[4].doc, equalTo(19));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[4]).fields[0]).utf8ToString(), equalTo("i"));

        sortMode = SortMode.MAX;
        nestedComparatorSource = new NestedFieldComparatorSource(sortMode, innerSource, parentFilter, childFilter);
        sort = new Sort(new SortField("field2", nestedComparatorSource, true));
        topDocs = searcher.search(query, 5, sort);
        assertThat(topDocs.totalHits, equalTo(7));
        assertThat(topDocs.scoreDocs.length, equalTo(5));
        assertThat(topDocs.scoreDocs[0].doc, equalTo(28));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[0]).fields[0]).utf8ToString(), equalTo("o"));
        assertThat(topDocs.scoreDocs[1].doc, equalTo(23));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[1]).fields[0]).utf8ToString(), equalTo("m"));
        assertThat(topDocs.scoreDocs[2].doc, equalTo(19));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[2]).fields[0]).utf8ToString(), equalTo("k"));
        assertThat(topDocs.scoreDocs[3].doc, equalTo(15));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[3]).fields[0]).utf8ToString(), equalTo("i"));
        assertThat(topDocs.scoreDocs[4].doc, equalTo(11));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[4]).fields[0]).utf8ToString(), equalTo("g"));


        childFilter = new AndFilter(Arrays.asList(new NotFilter(parentFilter), new TermFilter(new Term("filter_1", "T"))));
        nestedComparatorSource = new NestedFieldComparatorSource(sortMode, innerSource, parentFilter, childFilter);
        query = new ToParentBlockJoinQuery(
                new XFilteredQuery(new MatchAllDocsQuery(), childFilter),
                new FixedBitSetCachingWrapperFilter(parentFilter),
                ScoreMode.None
        );
        sort = new Sort(new SortField("field2", nestedComparatorSource, true));
        topDocs = searcher.search(query, 5, sort);
        assertThat(topDocs.totalHits, equalTo(6));
        assertThat(topDocs.scoreDocs.length, equalTo(5));
        assertThat(topDocs.scoreDocs[0].doc, equalTo(23));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[0]).fields[0]).utf8ToString(), equalTo("m"));
        assertThat(topDocs.scoreDocs[1].doc, equalTo(28));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[1]).fields[0]).utf8ToString(), equalTo("m"));
        assertThat(topDocs.scoreDocs[2].doc, equalTo(11));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[2]).fields[0]).utf8ToString(), equalTo("g"));
        assertThat(topDocs.scoreDocs[3].doc, equalTo(15));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[3]).fields[0]).utf8ToString(), equalTo("g"));
        assertThat(topDocs.scoreDocs[4].doc, equalTo(7));
        assertThat(((BytesRef) ((FieldDoc) topDocs.scoreDocs[4]).fields[0]).utf8ToString(), equalTo("e"));

        searcher.getIndexReader().close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7450.java