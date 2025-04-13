error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3185.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3185.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3185.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.analysis.query;
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

import org.apache.lucene.analysis.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.RAMDirectory;

import java.util.Arrays;
import java.util.Collections;

public class QueryAutoStopWordAnalyzerTest extends BaseTokenStreamTestCase {
  String variedFieldValues[] = {"the", "quick", "brown", "fox", "jumped", "over", "the", "lazy", "boring", "dog"};
  String repetitiveFieldValues[] = {"boring", "boring", "vaguelyboring"};
  RAMDirectory dir;
  Analyzer appAnalyzer;
  IndexReader reader;
  QueryAutoStopWordAnalyzer protectedAnalyzer;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    dir = new RAMDirectory();
    appAnalyzer = new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false);
    IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(TEST_VERSION_CURRENT, appAnalyzer));
    int numDocs = 200;
    for (int i = 0; i < numDocs; i++) {
      Document doc = new Document();
      String variedFieldValue = variedFieldValues[i % variedFieldValues.length];
      String repetitiveFieldValue = repetitiveFieldValues[i % repetitiveFieldValues.length];
      doc.add(new TextField("variedField", variedFieldValue, Field.Store.YES));
      doc.add(new TextField("repetitiveField", repetitiveFieldValue, Field.Store.YES));
      writer.addDocument(doc);
    }
    writer.close();
    reader = DirectoryReader.open(dir);
  }

  @Override
  public void tearDown() throws Exception {
    reader.close();
    super.tearDown();
  }

  public void testNoStopwords() throws Exception {
    // Note: an empty list of fields passed in
    protectedAnalyzer = new QueryAutoStopWordAnalyzer(TEST_VERSION_CURRENT, appAnalyzer, reader, Collections.<String>emptyList(), 1);
    TokenStream protectedTokenStream = protectedAnalyzer.tokenStream("variedField", "quick");
    assertTokenStreamContents(protectedTokenStream, new String[]{"quick"});

    protectedTokenStream = protectedAnalyzer.tokenStream("repetitiveField", "boring");
    assertTokenStreamContents(protectedTokenStream, new String[]{"boring"});
  }

  public void testDefaultStopwordsAllFields() throws Exception {
    protectedAnalyzer = new QueryAutoStopWordAnalyzer(TEST_VERSION_CURRENT, appAnalyzer, reader);
    TokenStream protectedTokenStream = protectedAnalyzer.tokenStream("repetitiveField", "boring");
    assertTokenStreamContents(protectedTokenStream, new String[0]); // Default stop word filtering will remove boring
  }

  public void testStopwordsAllFieldsMaxPercentDocs() throws Exception {
    protectedAnalyzer = new QueryAutoStopWordAnalyzer(TEST_VERSION_CURRENT, appAnalyzer, reader, 1f / 2f);

    TokenStream protectedTokenStream = protectedAnalyzer.tokenStream("repetitiveField", "boring");
    // A filter on terms in > one half of docs remove boring
    assertTokenStreamContents(protectedTokenStream, new String[0]);

    protectedTokenStream = protectedAnalyzer.tokenStream("repetitiveField", "vaguelyboring");
     // A filter on terms in > half of docs should not remove vaguelyBoring
    assertTokenStreamContents(protectedTokenStream, new String[]{"vaguelyboring"});

    protectedAnalyzer = new QueryAutoStopWordAnalyzer(TEST_VERSION_CURRENT, appAnalyzer, reader, 1f / 4f);
    protectedTokenStream = protectedAnalyzer.tokenStream("repetitiveField", "vaguelyboring");
     // A filter on terms in > quarter of docs should remove vaguelyBoring
    assertTokenStreamContents(protectedTokenStream, new String[0]);
  }

  public void testStopwordsPerFieldMaxPercentDocs() throws Exception {
    protectedAnalyzer = new QueryAutoStopWordAnalyzer(TEST_VERSION_CURRENT, appAnalyzer, reader, Arrays.asList("variedField"), 1f / 2f);
    TokenStream protectedTokenStream = protectedAnalyzer.tokenStream("repetitiveField", "boring");
    // A filter on one Field should not affect queries on another
    assertTokenStreamContents(protectedTokenStream, new String[]{"boring"});

    protectedAnalyzer = new QueryAutoStopWordAnalyzer(TEST_VERSION_CURRENT, appAnalyzer, reader, Arrays.asList("variedField", "repetitiveField"), 1f / 2f);
    protectedTokenStream = protectedAnalyzer.tokenStream("repetitiveField", "boring");
    // A filter on the right Field should affect queries on it
    assertTokenStreamContents(protectedTokenStream, new String[0]);
  }

  public void testStopwordsPerFieldMaxDocFreq() throws Exception {
    protectedAnalyzer = new QueryAutoStopWordAnalyzer(TEST_VERSION_CURRENT, appAnalyzer, reader, Arrays.asList("repetitiveField"), 10);
    int numStopWords = protectedAnalyzer.getStopWords("repetitiveField").length;
    assertTrue("Should have identified stop words", numStopWords > 0);

    protectedAnalyzer = new QueryAutoStopWordAnalyzer(TEST_VERSION_CURRENT, appAnalyzer, reader, Arrays.asList("repetitiveField", "variedField"), 10);
    int numNewStopWords = protectedAnalyzer.getStopWords("repetitiveField").length + protectedAnalyzer.getStopWords("variedField").length;
    assertTrue("Should have identified more stop words", numNewStopWords > numStopWords);
  }

  public void testNoFieldNamePollution() throws Exception {
    protectedAnalyzer = new QueryAutoStopWordAnalyzer(TEST_VERSION_CURRENT, appAnalyzer, reader, Arrays.asList("repetitiveField"), 10);

    TokenStream protectedTokenStream = protectedAnalyzer.tokenStream("repetitiveField", "boring");
    // Check filter set up OK
    assertTokenStreamContents(protectedTokenStream, new String[0]);

    protectedTokenStream = protectedAnalyzer.tokenStream("variedField", "boring");
    // Filter should not prevent stopwords in one field being used in another
    assertTokenStreamContents(protectedTokenStream, new String[]{"boring"});
  }
  
  public void testTokenStream() throws Exception {
    QueryAutoStopWordAnalyzer a = new QueryAutoStopWordAnalyzer(
        TEST_VERSION_CURRENT,
        new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false), reader, 10);
    TokenStream ts = a.tokenStream("repetitiveField", "this boring");
    assertTokenStreamContents(ts, new String[] { "this" });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3185.java