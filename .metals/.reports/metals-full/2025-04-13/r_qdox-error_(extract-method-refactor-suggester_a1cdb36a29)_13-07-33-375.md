error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3372.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3372.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3372.java
text:
```scala
d@@oc.add(new SortedBytesDocValuesField(groupField, new BytesRef(value)));

package org.apache.lucene.search.grouping;

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
import org.apache.lucene.document.*;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.valuesource.BytesRefFieldSource;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.mutable.MutableValueStr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupingSearchTest extends LuceneTestCase {

  // Tests some very basic usages...
  public void testBasic() throws Exception {

    final String groupField = "author";

    FieldType customType = new FieldType();
    customType.setStored(true);

    Directory dir = newDirectory();
    RandomIndexWriter w = new RandomIndexWriter(
        random(),
        dir,
        newIndexWriterConfig(TEST_VERSION_CURRENT,
            new MockAnalyzer(random())).setMergePolicy(newLogMergePolicy()));
    boolean canUseIDV = !"Lucene3x".equals(w.w.getConfig().getCodec().getName());
    List<Document> documents = new ArrayList<Document>();
    // 0
    Document doc = new Document();
    addGroupField(doc, groupField, "author1", canUseIDV);
    doc.add(new Field("content", "random text", TextField.TYPE_STORED));
    doc.add(new Field("id", "1", customType));
    documents.add(doc);

    // 1
    doc = new Document();
    addGroupField(doc, groupField, "author1", canUseIDV);
    doc.add(new Field("content", "some more random text", TextField.TYPE_STORED));
    doc.add(new Field("id", "2", customType));
    documents.add(doc);

    // 2
    doc = new Document();
    addGroupField(doc, groupField, "author1", canUseIDV);
    doc.add(new Field("content", "some more random textual data", TextField.TYPE_STORED));
    doc.add(new Field("id", "3", customType));
    doc.add(new Field("groupend", "x", StringField.TYPE_UNSTORED));
    documents.add(doc);
    w.addDocuments(documents);
    documents.clear();

    // 3
    doc = new Document();
    addGroupField(doc, groupField, "author2", canUseIDV);
    doc.add(new Field("content", "some random text", TextField.TYPE_STORED));
    doc.add(new Field("id", "4", customType));
    doc.add(new Field("groupend", "x", StringField.TYPE_UNSTORED));
    w.addDocument(doc);

    // 4
    doc = new Document();
    addGroupField(doc, groupField, "author3", canUseIDV);
    doc.add(new Field("content", "some more random text", TextField.TYPE_STORED));
    doc.add(new Field("id", "5", customType));
    documents.add(doc);

    // 5
    doc = new Document();
    addGroupField(doc, groupField, "author3", canUseIDV);
    doc.add(new Field("content", "random", TextField.TYPE_STORED));
    doc.add(new Field("id", "6", customType));
    doc.add(new Field("groupend", "x", StringField.TYPE_UNSTORED));
    documents.add(doc);
    w.addDocuments(documents);
    documents.clear();

    // 6 -- no author field
    doc = new Document();
    doc.add(new Field("content", "random word stuck in alot of other text", TextField.TYPE_STORED));
    doc.add(new Field("id", "6", customType));
    doc.add(new Field("groupend", "x", StringField.TYPE_UNSTORED));

    w.addDocument(doc);

    IndexSearcher indexSearcher = new IndexSearcher(w.getReader());
    w.close();

    Sort groupSort = Sort.RELEVANCE;
    GroupingSearch groupingSearch = createRandomGroupingSearch(groupField, groupSort, 5, canUseIDV);

    TopGroups<?> groups = groupingSearch.search(indexSearcher, null, new TermQuery(new Term("content", "random")), 0, 10);

    assertEquals(7, groups.totalHitCount);
    assertEquals(7, groups.totalGroupedHitCount);
    assertEquals(4, groups.groups.length);

    // relevance order: 5, 0, 3, 4, 1, 2, 6

    // the later a document is added the higher this docId
    // value
    GroupDocs<?> group = groups.groups[0];
    compareGroupValue("author3", group);
    assertEquals(2, group.scoreDocs.length);
    assertEquals(5, group.scoreDocs[0].doc);
    assertEquals(4, group.scoreDocs[1].doc);
    assertTrue(group.scoreDocs[0].score > group.scoreDocs[1].score);

    group = groups.groups[1];
    compareGroupValue("author1", group);
    assertEquals(3, group.scoreDocs.length);
    assertEquals(0, group.scoreDocs[0].doc);
    assertEquals(1, group.scoreDocs[1].doc);
    assertEquals(2, group.scoreDocs[2].doc);
    assertTrue(group.scoreDocs[0].score > group.scoreDocs[1].score);
    assertTrue(group.scoreDocs[1].score > group.scoreDocs[2].score);

    group = groups.groups[2];
    compareGroupValue("author2", group);
    assertEquals(1, group.scoreDocs.length);
    assertEquals(3, group.scoreDocs[0].doc);

    group = groups.groups[3];
    compareGroupValue(null, group);
    assertEquals(1, group.scoreDocs.length);
    assertEquals(6, group.scoreDocs[0].doc);

    Filter lastDocInBlock = new CachingWrapperFilter(new QueryWrapperFilter(new TermQuery(new Term("groupend", "x"))));
    groupingSearch = new GroupingSearch(lastDocInBlock);
    groups = groupingSearch.search(indexSearcher, null, new TermQuery(new Term("content", "random")), 0, 10);

    assertEquals(7, groups.totalHitCount);
    assertEquals(7, groups.totalGroupedHitCount);
    assertEquals(4, groups.totalGroupCount.longValue());
    assertEquals(4, groups.groups.length);
    
    indexSearcher.getIndexReader().close();
    dir.close();
  }

  private void addGroupField(Document doc, String groupField, String value, boolean canUseIDV) {
    doc.add(new Field(groupField, value, TextField.TYPE_STORED));
    if (canUseIDV) {
      doc.add(new DocValuesField(groupField, new BytesRef(value), DocValues.Type.BYTES_VAR_SORTED));
    }
  }

  private void compareGroupValue(String expected, GroupDocs<?> group) {
    if (expected == null) {
      if (group.groupValue == null) {
        return;
      } else if (group.groupValue.getClass().isAssignableFrom(MutableValueStr.class)) {
        return;
      } else if (((BytesRef) group.groupValue).length == 0) {
        return;
      }
      fail();
    }

    if (group.groupValue.getClass().isAssignableFrom(BytesRef.class)) {
      assertEquals(new BytesRef(expected), group.groupValue);
    } else if (group.groupValue.getClass().isAssignableFrom(MutableValueStr.class)) {
      MutableValueStr v = new MutableValueStr();
      v.value = new BytesRef(expected);
      assertEquals(v, group.groupValue);
    } else {
      fail();
    }
  }

  private GroupingSearch createRandomGroupingSearch(String groupField, Sort groupSort, int docsInGroup, boolean canUseIDV) throws IOException {
    GroupingSearch groupingSearch;
    if (random().nextBoolean()) {
      ValueSource vs = new BytesRefFieldSource(groupField);
      groupingSearch = new GroupingSearch(vs, new HashMap<Object, Object>());
    } else {
      if (canUseIDV && random().nextBoolean()) {
        boolean diskResident = random().nextBoolean();
        groupingSearch = new GroupingSearch(groupField, DocValues.Type.BYTES_VAR_SORTED, diskResident);
      } else {
        groupingSearch = new GroupingSearch(groupField);  
      }
    }

    groupingSearch.setGroupSort(groupSort);
    groupingSearch.setGroupDocsLimit(docsInGroup);

    if (random().nextBoolean()) {
      groupingSearch.setCachingInMB(4.0, true);
    }

    return groupingSearch;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3372.java