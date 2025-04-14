error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3127.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3127.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3127.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.search;

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

import java.util.Arrays;

import org.apache.lucene.index.Term;
import org.apache.lucene.util.LuceneTestCase;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.StoredDocument;
import org.apache.lucene.store.Directory;

/**
 * Test date sorting, i.e. auto-sorting of fields with type "long".
 * See http://issues.apache.org/jira/browse/LUCENE-1045 
 */
public class TestDateSort extends LuceneTestCase {

  private static final String TEXT_FIELD = "text";
  private static final String DATE_TIME_FIELD = "dateTime";

  private Directory directory;
  private IndexReader reader;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    // Create an index writer.
    directory = newDirectory();
    RandomIndexWriter writer = new RandomIndexWriter(random(), directory);

    // oldest doc:
    // Add the first document.  text = "Document 1"  dateTime = Oct 10 03:25:22 EDT 2007
    writer.addDocument(createDocument("Document 1", 1192001122000L));
    // Add the second document.  text = "Document 2"  dateTime = Oct 10 03:25:26 EDT 2007 
    writer.addDocument(createDocument("Document 2", 1192001126000L));
    // Add the third document.  text = "Document 3"  dateTime = Oct 11 07:12:13 EDT 2007 
    writer.addDocument(createDocument("Document 3", 1192101133000L));
    // Add the fourth document.  text = "Document 4"  dateTime = Oct 11 08:02:09 EDT 2007
    writer.addDocument(createDocument("Document 4", 1192104129000L));
    // latest doc:
    // Add the fifth document.  text = "Document 5"  dateTime = Oct 12 13:25:43 EDT 2007
    writer.addDocument(createDocument("Document 5", 1192209943000L));

    reader = writer.getReader();
    writer.close();
  }

  @Override
  public void tearDown() throws Exception {
    reader.close();
    directory.close();
    super.tearDown();
  }

  public void testReverseDateSort() throws Exception {
    IndexSearcher searcher = newSearcher(reader);

    Sort sort = new Sort(new SortField(DATE_TIME_FIELD, SortField.Type.STRING, true));
    Query query = new TermQuery(new Term(TEXT_FIELD, "document"));

    // Execute the search and process the search results.
    String[] actualOrder = new String[5];
    ScoreDoc[] hits = searcher.search(query, null, 1000, sort).scoreDocs;
    for (int i = 0; i < hits.length; i++) {
      StoredDocument document = searcher.doc(hits[i].doc);
      String text = document.get(TEXT_FIELD);
      actualOrder[i] = text;
    }

    // Set up the expected order (i.e. Document 5, 4, 3, 2, 1).
    String[] expectedOrder = new String[5];
    expectedOrder[0] = "Document 5";
    expectedOrder[1] = "Document 4";
    expectedOrder[2] = "Document 3";
    expectedOrder[3] = "Document 2";
    expectedOrder[4] = "Document 1";

    assertEquals(Arrays.asList(expectedOrder), Arrays.asList(actualOrder));
  }

  private Document createDocument(String text, long time) {
    Document document = new Document();

    // Add the text field.
    Field textField = newTextField(TEXT_FIELD, text, Field.Store.YES);
    document.add(textField);

    // Add the date/time field.
    String dateTimeString = DateTools.timeToString(time, DateTools.Resolution.SECOND);
    Field dateTimeField = newStringField(DATE_TIME_FIELD, dateTimeString, Field.Store.YES);
    document.add(dateTimeField);

    return document;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3127.java