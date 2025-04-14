error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3177.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3177.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3177.java
text:
```scala
w@@.shutdown();

package org.apache.lucene.search.join;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.NoMergePolicy;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.FieldDoc;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class TestBlockJoinSorting extends LuceneTestCase {

  @Test
  public void testNestedSorting() throws Exception {
    final Directory dir = newDirectory();
    final RandomIndexWriter w = new RandomIndexWriter(random(), dir, newIndexWriterConfig(TEST_VERSION_CURRENT,
        new MockAnalyzer(random())).setMergePolicy(NoMergePolicy.COMPOUND_FILES));

    List<Document> docs = new ArrayList<>();
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
    w.addDocuments(docs);
    w.commit();

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
    w.addDocuments(docs);

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
    w.addDocuments(docs);

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
    w.addDocuments(docs);
    w.commit();

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
    w.addDocuments(docs);

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
    w.addDocuments(docs);

    // This doc will not be included, because it doesn't have nested docs
    document = new Document();
    document.add(new StringField("__type", "parent", Field.Store.NO));
    document.add(new StringField("field1", "h", Field.Store.NO));
    w.addDocument(document);

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
    w.addDocuments(docs);
    w.commit();

    // Some garbage docs, just to check if the NestedFieldComparator can deal with this.
    document = new Document();
    document.add(new StringField("fieldXXX", "x", Field.Store.NO));
    w.addDocument(document);
    document = new Document();
    document.add(new StringField("fieldXXX", "x", Field.Store.NO));
    w.addDocument(document);
    document = new Document();
    document.add(new StringField("fieldXXX", "x", Field.Store.NO));
    w.addDocument(document);

    IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(w.w, false));
    w.close();
    Filter parentFilter = new QueryWrapperFilter(new TermQuery(new Term("__type", "parent")));
    Filter childFilter = new QueryWrapperFilter(new PrefixQuery(new Term("field2")));
    ToParentBlockJoinQuery query = new ToParentBlockJoinQuery(
        new FilteredQuery(new MatchAllDocsQuery(), childFilter),
        new FixedBitSetCachingWrapperFilter(parentFilter),
        ScoreMode.None
    );

    // Sort by field ascending, order first
    ToParentBlockJoinSortField sortField = new ToParentBlockJoinSortField(
        "field2", SortField.Type.STRING, false, wrap(parentFilter), wrap(childFilter)
    );
    Sort sort = new Sort(sortField);
    TopFieldDocs topDocs = searcher.search(query, 5, sort);
    assertEquals(7, topDocs.totalHits);
    assertEquals(5, topDocs.scoreDocs.length);
    assertEquals(3, topDocs.scoreDocs[0].doc);
    assertEquals("a", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[0]).fields[0]).utf8ToString());
    assertEquals(7, topDocs.scoreDocs[1].doc);
    assertEquals("c", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[1]).fields[0]).utf8ToString());
    assertEquals(11, topDocs.scoreDocs[2].doc);
    assertEquals("e", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[2]).fields[0]).utf8ToString());
    assertEquals(15, topDocs.scoreDocs[3].doc);
    assertEquals("g", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[3]).fields[0]).utf8ToString());
    assertEquals(19, topDocs.scoreDocs[4].doc);
    assertEquals("i", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[4]).fields[0]).utf8ToString());

    // Sort by field ascending, order last
    sortField = new ToParentBlockJoinSortField(
        "field2", SortField.Type.STRING, false, true, wrap(parentFilter), wrap(childFilter)
    );
    sort = new Sort(sortField);
    topDocs = searcher.search(query, 5, sort);
    assertEquals(7, topDocs.totalHits);
    assertEquals(5, topDocs.scoreDocs.length);
    assertEquals(3, topDocs.scoreDocs[0].doc);
    assertEquals("c", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[0]).fields[0]).utf8ToString());
    assertEquals(7, topDocs.scoreDocs[1].doc);
    assertEquals("e", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[1]).fields[0]).utf8ToString());
    assertEquals(11, topDocs.scoreDocs[2].doc);
    assertEquals("g", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[2]).fields[0]).utf8ToString());
    assertEquals(15, topDocs.scoreDocs[3].doc);
    assertEquals("i", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[3]).fields[0]).utf8ToString());
    assertEquals(19, topDocs.scoreDocs[4].doc);
    assertEquals("k", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[4]).fields[0]).utf8ToString());

    // Sort by field descending, order last
    sortField = new ToParentBlockJoinSortField(
        "field2", SortField.Type.STRING, true, wrap(parentFilter), wrap(childFilter)
    );
    sort = new Sort(sortField);
    topDocs = searcher.search(query, 5, sort);
    assertEquals(topDocs.totalHits, 7);
    assertEquals(5, topDocs.scoreDocs.length);
    assertEquals(28, topDocs.scoreDocs[0].doc);
    assertEquals("o", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[0]).fields[0]).utf8ToString());
    assertEquals(23, topDocs.scoreDocs[1].doc);
    assertEquals("m", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[1]).fields[0]).utf8ToString());
    assertEquals(19, topDocs.scoreDocs[2].doc);
    assertEquals("k", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[2]).fields[0]).utf8ToString());
    assertEquals(15, topDocs.scoreDocs[3].doc);
    assertEquals("i", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[3]).fields[0]).utf8ToString());
    assertEquals(11, topDocs.scoreDocs[4].doc);
    assertEquals("g", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[4]).fields[0]).utf8ToString());

    // Sort by field descending, order last, sort filter (filter_1:T)
    childFilter = new QueryWrapperFilter(new TermQuery((new Term("filter_1", "T"))));
    query = new ToParentBlockJoinQuery(
        new FilteredQuery(new MatchAllDocsQuery(), childFilter),
        new FixedBitSetCachingWrapperFilter(parentFilter),
        ScoreMode.None
    );
    sortField = new ToParentBlockJoinSortField(
        "field2", SortField.Type.STRING, true, wrap(parentFilter), wrap(childFilter)
    );
    sort = new Sort(sortField);
    topDocs = searcher.search(query, 5, sort);
    assertEquals(6, topDocs.totalHits);
    assertEquals(5, topDocs.scoreDocs.length);
    assertEquals(23, topDocs.scoreDocs[0].doc);
    assertEquals("m", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[0]).fields[0]).utf8ToString());
    assertEquals(28, topDocs.scoreDocs[1].doc);
    assertEquals("m", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[1]).fields[0]).utf8ToString());
    assertEquals(11, topDocs.scoreDocs[2].doc);
    assertEquals("g", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[2]).fields[0]).utf8ToString());
    assertEquals(15, topDocs.scoreDocs[3].doc);
    assertEquals("g", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[3]).fields[0]).utf8ToString());
    assertEquals(7, topDocs.scoreDocs[4].doc);
    assertEquals("e", ((BytesRef) ((FieldDoc) topDocs.scoreDocs[4]).fields[0]).utf8ToString());

    searcher.getIndexReader().close();
    dir.close();
  }

  private Filter wrap(Filter filter) {
    return random().nextBoolean() ? new FixedBitSetCachingWrapperFilter(filter) : filter;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3177.java