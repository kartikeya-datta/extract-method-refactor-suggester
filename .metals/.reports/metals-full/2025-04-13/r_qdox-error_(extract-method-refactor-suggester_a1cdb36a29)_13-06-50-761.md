error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3072.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3072.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3072.java
text:
```scala
i@@w.shutdown();

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

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.similarities.PerFieldSimilarityWrapper;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;

public class TestSimilarityProvider extends LuceneTestCase {
  private Directory directory;
  private DirectoryReader reader;
  private IndexSearcher searcher;
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    directory = newDirectory();
    PerFieldSimilarityWrapper sim = new ExampleSimilarityProvider();
    IndexWriterConfig iwc = newIndexWriterConfig(TEST_VERSION_CURRENT, 
        new MockAnalyzer(random())).setSimilarity(sim);
    RandomIndexWriter iw = new RandomIndexWriter(random(), directory, iwc);
    Document doc = new Document();
    Field field = newTextField("foo", "", Field.Store.NO);
    doc.add(field);
    Field field2 = newTextField("bar", "", Field.Store.NO);
    doc.add(field2);
    
    field.setStringValue("quick brown fox");
    field2.setStringValue("quick brown fox");
    iw.addDocument(doc);
    field.setStringValue("jumps over lazy brown dog");
    field2.setStringValue("jumps over lazy brown dog");
    iw.addDocument(doc);
    reader = iw.getReader();
    iw.close();
    searcher = newSearcher(reader);
    searcher.setSimilarity(sim);
  }
  
  @Override
  public void tearDown() throws Exception {
    reader.close();
    directory.close();
    super.tearDown();
  }
  
  public void testBasics() throws Exception {
    // sanity check of norms writer
    // TODO: generalize
    AtomicReader slow = SlowCompositeReaderWrapper.wrap(reader);
    NumericDocValues fooNorms = slow.getNormValues("foo");
    NumericDocValues barNorms = slow.getNormValues("bar");
    for (int i = 0; i < slow.maxDoc(); i++) {
      assertFalse(fooNorms.get(i) == barNorms.get(i));
    }
    
    // sanity check of searching
    TopDocs foodocs = searcher.search(new TermQuery(new Term("foo", "brown")), 10);
    assertTrue(foodocs.totalHits > 0);
    TopDocs bardocs = searcher.search(new TermQuery(new Term("bar", "brown")), 10);
    assertTrue(bardocs.totalHits > 0);
    assertTrue(foodocs.scoreDocs[0].score < bardocs.scoreDocs[0].score);
  }
  
  private class ExampleSimilarityProvider extends PerFieldSimilarityWrapper {
    private Similarity sim1 = new Sim1();
    private Similarity sim2 = new Sim2();
    
    @Override
    public Similarity get(String field) {
      if (field.equals("foo")) {
        return sim1;
      } else {
        return sim2;
      }
    }
  }
  
  private class Sim1 extends TFIDFSimilarity {
    
    @Override
    public long encodeNormValue(float f) {
      return (long) f;
    }
    
    @Override
    public float decodeNormValue(long norm) {
      return norm;
    }
    
    @Override
    public float coord(int overlap, int maxOverlap) {
      return 1f;
    }

    @Override
    public float queryNorm(float sumOfSquaredWeights) {
      return 1f;
    }

    @Override
    public float lengthNorm(FieldInvertState state) {
      return 1f;
    }

    @Override
    public float sloppyFreq(int distance) {
      return 1f;
    }

    @Override
    public float tf(float freq) {
      return 1f;
    }

    @Override
    public float idf(long docFreq, long numDocs) {
      return 1f;
    }

    @Override
    public float scorePayload(int doc, int start, int end, BytesRef payload) {
      return 1f;
    }
  }
  
  private class Sim2 extends TFIDFSimilarity {
    
    @Override
    public long encodeNormValue(float f) {
      return (long) f;
    }
    
    @Override
    public float decodeNormValue(long norm) {
      return norm;
    }
    
    @Override
    public float coord(int overlap, int maxOverlap) {
      return 1f;
    }

    @Override
    public float queryNorm(float sumOfSquaredWeights) {
      return 1f;
    }
    
    @Override
    public float lengthNorm(FieldInvertState state) {
      return 10f;
    }

    @Override
    public float sloppyFreq(int distance) {
      return 10f;
    }

    @Override
    public float tf(float freq) {
      return 10f;
    }

    @Override
    public float idf(long docFreq, long numDocs) {
      return 10f;
    }

    @Override
    public float scorePayload(int doc, int start, int end, BytesRef payload) {
      return 1f;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3072.java