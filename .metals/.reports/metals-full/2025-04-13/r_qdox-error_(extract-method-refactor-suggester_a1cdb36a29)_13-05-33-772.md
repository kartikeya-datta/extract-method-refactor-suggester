error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3035.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3035.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3035.java
text:
```scala
w@@.shutdown();

package org.apache.lucene.queryparser.complexPhrase;

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

import java.util.HashSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.StoredDocument;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;

public class TestComplexPhraseQuery extends LuceneTestCase {
  Directory rd;
  Analyzer analyzer;
  DocData docsContent[] = {
      new DocData("john smith", "1", "developer"),
      new DocData("johathon smith", "2", "developer"),
      new DocData("john percival smith", "3", "designer"),
      new DocData("jackson waits tom", "4", "project manager")
  };

  private IndexSearcher searcher;
  private IndexReader reader;

  String defaultFieldName = "name";

  boolean inOrder = true;

  public void testComplexPhrases() throws Exception {
    checkMatches("\"john smith\"", "1"); // Simple multi-term still works
    checkMatches("\"j*   smyth~\"", "1,2"); // wildcards and fuzzies are OK in
    // phrases
    checkMatches("\"(jo* -john)  smith\"", "2"); // boolean logic works
    checkMatches("\"jo*  smith\"~2", "1,2,3"); // position logic works.
    checkMatches("\"jo* [sma TO smZ]\" ", "1,2"); // range queries supported
    checkMatches("\"john\"", "1,3"); // Simple single-term still works
    checkMatches("\"(john OR johathon)  smith\"", "1,2"); // boolean logic with
    // brackets works.
    checkMatches("\"(jo* -john) smyth~\"", "2"); // boolean logic with
    // brackets works.

    // checkMatches("\"john -percival\"", "1"); // not logic doesn't work
    // currently :(.

    checkMatches("\"john  nosuchword*\"", ""); // phrases with clauses producing
    // empty sets

    checkBadQuery("\"jo*  id:1 smith\""); // mixing fields in a phrase is bad
    checkBadQuery("\"jo* \"smith\" \""); // phrases inside phrases is bad
  }


  public void testUnOrderedProximitySearches() throws Exception {

    inOrder = true;
    checkMatches("\"smith jo*\"~2", ""); // ordered proximity produces empty set

    inOrder = false;
    checkMatches("\"smith jo*\"~2", "1,2,3"); // un-ordered proximity

  }

  private void checkBadQuery(String qString) {
    ComplexPhraseQueryParser qp = new ComplexPhraseQueryParser(TEST_VERSION_CURRENT, defaultFieldName, analyzer);
    qp.setInOrder(inOrder);
    Throwable expected = null;
    try {
      qp.parse(qString);
    } catch (Throwable e) {
      expected = e;
    }
    assertNotNull("Expected parse error in " + qString, expected);

  }

  private void checkMatches(String qString, String expectedVals)
      throws Exception {
    ComplexPhraseQueryParser qp = new ComplexPhraseQueryParser(TEST_VERSION_CURRENT, defaultFieldName, analyzer);
    qp.setInOrder(inOrder);
    qp.setFuzzyPrefixLength(1); // usually a good idea

    Query q = qp.parse(qString);

    HashSet<String> expecteds = new HashSet<>();
    String[] vals = expectedVals.split(",");
    for (int i = 0; i < vals.length; i++) {
      if (vals[i].length() > 0)
        expecteds.add(vals[i]);
    }

    TopDocs td = searcher.search(q, 10);
    ScoreDoc[] sd = td.scoreDocs;
    for (int i = 0; i < sd.length; i++) {
      StoredDocument doc = searcher.doc(sd[i].doc);
      String id = doc.get("id");
      assertTrue(qString + "matched doc#" + id + " not expected", expecteds
          .contains(id));
      expecteds.remove(id);
    }

    assertEquals(qString + " missing some matches ", 0, expecteds.size());

  }
  
  public void testFieldedQuery() throws Exception {
    checkMatches("name:\"john smith\"", "1");
    checkMatches("name:\"j*   smyth~\"", "1,2");
    checkMatches("role:\"developer\"", "1,2");
    checkMatches("role:\"p* manager\"", "4");
    checkMatches("role:de*", "1,2,3");
    checkMatches("name:\"j* smyth~\"~5", "1,2,3");
    checkMatches("role:\"p* manager\" AND name:jack*", "4");
    checkMatches("+role:developer +name:jack*", "");
    checkMatches("name:\"john smith\"~2 AND role:designer AND id:3", "3");
  }
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    
    analyzer = new MockAnalyzer(random());
    rd = newDirectory();
    IndexWriter w = new IndexWriter(rd, newIndexWriterConfig(TEST_VERSION_CURRENT, analyzer));
    for (int i = 0; i < docsContent.length; i++) {
      Document doc = new Document();
      doc.add(newTextField("name", docsContent[i].name, Field.Store.YES));
      doc.add(newTextField("id", docsContent[i].id, Field.Store.YES));
      doc.add(newTextField("role", docsContent[i].role, Field.Store.YES));
      w.addDocument(doc);
    }
    w.close();
    reader = DirectoryReader.open(rd);
    searcher = newSearcher(reader);
  }

  @Override
  public void tearDown() throws Exception {
    reader.close();
    rd.close();
    super.tearDown();
  }

  static class DocData {
    String name;

    String id;
    
    String role;

    public DocData(String name, String id, String role) {
      super();
      this.name = name;
      this.id = id;
      this.role = role;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3035.java