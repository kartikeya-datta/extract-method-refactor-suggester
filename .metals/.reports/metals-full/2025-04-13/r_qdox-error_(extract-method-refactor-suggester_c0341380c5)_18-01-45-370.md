error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3077.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3077.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3077.java
text:
```scala
i@@w.shutdown();

package org.apache.lucene.search.spans;

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

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;

/**
 * Tests for {@link SpanMultiTermQueryWrapper}, wrapping a few MultiTermQueries.
 */
public class TestSpanMultiTermQueryWrapper extends LuceneTestCase {
  private Directory directory;
  private IndexReader reader;
  private IndexSearcher searcher;
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    directory = newDirectory();
    RandomIndexWriter iw = new RandomIndexWriter(random(), directory);
    Document doc = new Document();
    Field field = newTextField("field", "", Field.Store.NO);
    doc.add(field);
    
    field.setStringValue("quick brown fox");
    iw.addDocument(doc);
    field.setStringValue("jumps over lazy broun dog");
    iw.addDocument(doc);
    field.setStringValue("jumps over extremely very lazy broxn dog");
    iw.addDocument(doc);
    reader = iw.getReader();
    iw.close();
    searcher = newSearcher(reader);
  }
  
  @Override
  public void tearDown() throws Exception {
    reader.close();
    directory.close();
    super.tearDown();
  }
  
  public void testWildcard() throws Exception {
    WildcardQuery wq = new WildcardQuery(new Term("field", "bro?n"));
    SpanQuery swq = new SpanMultiTermQueryWrapper<>(wq);
    // will only match quick brown fox
    SpanFirstQuery sfq = new SpanFirstQuery(swq, 2);
    assertEquals(1, searcher.search(sfq, 10).totalHits);
  }
  
  public void testPrefix() throws Exception {
    WildcardQuery wq = new WildcardQuery(new Term("field", "extrem*"));
    SpanQuery swq = new SpanMultiTermQueryWrapper<>(wq);
    // will only match "jumps over extremely very lazy broxn dog"
    SpanFirstQuery sfq = new SpanFirstQuery(swq, 3);
    assertEquals(1, searcher.search(sfq, 10).totalHits);
  }
  
  public void testFuzzy() throws Exception {
    FuzzyQuery fq = new FuzzyQuery(new Term("field", "broan"));
    SpanQuery sfq = new SpanMultiTermQueryWrapper<>(fq);
    // will not match quick brown fox
    SpanPositionRangeQuery sprq = new SpanPositionRangeQuery(sfq, 3, 6);
    assertEquals(2, searcher.search(sprq, 10).totalHits);
  }
  
  public void testFuzzy2() throws Exception {
    // maximum of 1 term expansion
    FuzzyQuery fq = new FuzzyQuery(new Term("field", "broan"), 1, 0, 1, false);
    SpanQuery sfq = new SpanMultiTermQueryWrapper<>(fq);
    // will only match jumps over lazy broun dog
    SpanPositionRangeQuery sprq = new SpanPositionRangeQuery(sfq, 0, 100);
    assertEquals(1, searcher.search(sprq, 10).totalHits);
  }
  public void testNoSuchMultiTermsInNear() throws Exception {
    //test to make sure non existent multiterms aren't throwing null pointer exceptions  
    FuzzyQuery fuzzyNoSuch = new FuzzyQuery(new Term("field", "noSuch"), 1, 0, 1, false);
    SpanQuery spanNoSuch = new SpanMultiTermQueryWrapper<>(fuzzyNoSuch);
    SpanQuery term = new SpanTermQuery(new Term("field", "brown"));
    SpanQuery near = new SpanNearQuery(new SpanQuery[]{term, spanNoSuch}, 1, true);
    assertEquals(0, searcher.search(near, 10).totalHits);
    //flip order
    near = new SpanNearQuery(new SpanQuery[]{spanNoSuch, term}, 1, true);
    assertEquals(0, searcher.search(near, 10).totalHits);
    
    WildcardQuery wcNoSuch = new WildcardQuery(new Term("field", "noSuch*"));
    SpanQuery spanWCNoSuch = new SpanMultiTermQueryWrapper<>(wcNoSuch);
    near = new SpanNearQuery(new SpanQuery[]{term, spanWCNoSuch}, 1, true);
    assertEquals(0, searcher.search(near, 10).totalHits);
  
    RegexpQuery rgxNoSuch = new RegexpQuery(new Term("field", "noSuch"));
    SpanQuery spanRgxNoSuch = new SpanMultiTermQueryWrapper<>(rgxNoSuch);
    near = new SpanNearQuery(new SpanQuery[]{term, spanRgxNoSuch}, 1, true);
    assertEquals(0, searcher.search(near, 10).totalHits);
    
    PrefixQuery prfxNoSuch = new PrefixQuery(new Term("field", "noSuch"));
    SpanQuery spanPrfxNoSuch = new SpanMultiTermQueryWrapper<>(prfxNoSuch);
    near = new SpanNearQuery(new SpanQuery[]{term, spanPrfxNoSuch}, 1, true);
    assertEquals(0, searcher.search(near, 10).totalHits);

    //test single noSuch
    near = new SpanNearQuery(new SpanQuery[]{spanPrfxNoSuch}, 1, true);
    assertEquals(0, searcher.search(near, 10).totalHits);
    
    //test double noSuch
    near = new SpanNearQuery(new SpanQuery[]{spanPrfxNoSuch, spanPrfxNoSuch}, 1, true);
    assertEquals(0, searcher.search(near, 10).totalHits);

  }
  
  public void testNoSuchMultiTermsInNotNear() throws Exception {
    //test to make sure non existent multiterms aren't throwing non-matching field exceptions  
    FuzzyQuery fuzzyNoSuch = new FuzzyQuery(new Term("field", "noSuch"), 1, 0, 1, false);
    SpanQuery spanNoSuch = new SpanMultiTermQueryWrapper<>(fuzzyNoSuch);
    SpanQuery term = new SpanTermQuery(new Term("field", "brown"));
    SpanNotQuery notNear = new SpanNotQuery(term, spanNoSuch, 0,0);
    assertEquals(1, searcher.search(notNear, 10).totalHits);

    //flip
    notNear = new SpanNotQuery(spanNoSuch, term, 0,0);
    assertEquals(0, searcher.search(notNear, 10).totalHits);
    
    //both noSuch
    notNear = new SpanNotQuery(spanNoSuch, spanNoSuch, 0,0);
    assertEquals(0, searcher.search(notNear, 10).totalHits);

    WildcardQuery wcNoSuch = new WildcardQuery(new Term("field", "noSuch*"));
    SpanQuery spanWCNoSuch = new SpanMultiTermQueryWrapper<>(wcNoSuch);
    notNear = new SpanNotQuery(term, spanWCNoSuch, 0,0);
    assertEquals(1, searcher.search(notNear, 10).totalHits);
  
    RegexpQuery rgxNoSuch = new RegexpQuery(new Term("field", "noSuch"));
    SpanQuery spanRgxNoSuch = new SpanMultiTermQueryWrapper<>(rgxNoSuch);
    notNear = new SpanNotQuery(term, spanRgxNoSuch, 1, 1);
    assertEquals(1, searcher.search(notNear, 10).totalHits);
    
    PrefixQuery prfxNoSuch = new PrefixQuery(new Term("field", "noSuch"));
    SpanQuery spanPrfxNoSuch = new SpanMultiTermQueryWrapper<>(prfxNoSuch);
    notNear = new SpanNotQuery(term, spanPrfxNoSuch, 1, 1);
    assertEquals(1, searcher.search(notNear, 10).totalHits);
    
  }
  
  public void testNoSuchMultiTermsInOr() throws Exception {
    //test to make sure non existent multiterms aren't throwing null pointer exceptions  
    FuzzyQuery fuzzyNoSuch = new FuzzyQuery(new Term("field", "noSuch"), 1, 0, 1, false);
    SpanQuery spanNoSuch = new SpanMultiTermQueryWrapper<>(fuzzyNoSuch);
    SpanQuery term = new SpanTermQuery(new Term("field", "brown"));
    SpanOrQuery near = new SpanOrQuery(new SpanQuery[]{term, spanNoSuch});
    assertEquals(1, searcher.search(near, 10).totalHits);
    
    //flip
    near = new SpanOrQuery(new SpanQuery[]{spanNoSuch, term});
    assertEquals(1, searcher.search(near, 10).totalHits);

    
    WildcardQuery wcNoSuch = new WildcardQuery(new Term("field", "noSuch*"));
    SpanQuery spanWCNoSuch = new SpanMultiTermQueryWrapper<>(wcNoSuch);
    near = new SpanOrQuery(new SpanQuery[]{term, spanWCNoSuch});
    assertEquals(1, searcher.search(near, 10).totalHits);
  
    RegexpQuery rgxNoSuch = new RegexpQuery(new Term("field", "noSuch"));
    SpanQuery spanRgxNoSuch = new SpanMultiTermQueryWrapper<>(rgxNoSuch);
    near = new SpanOrQuery(new SpanQuery[]{term, spanRgxNoSuch});
    assertEquals(1, searcher.search(near, 10).totalHits);
    
    PrefixQuery prfxNoSuch = new PrefixQuery(new Term("field", "noSuch"));
    SpanQuery spanPrfxNoSuch = new SpanMultiTermQueryWrapper<>(prfxNoSuch);
    near = new SpanOrQuery(new SpanQuery[]{term, spanPrfxNoSuch});
    assertEquals(1, searcher.search(near, 10).totalHits);
    
    near = new SpanOrQuery(new SpanQuery[]{spanPrfxNoSuch});
    assertEquals(0, searcher.search(near, 10).totalHits);
    
    near = new SpanOrQuery(new SpanQuery[]{spanPrfxNoSuch, spanPrfxNoSuch});
    assertEquals(0, searcher.search(near, 10).totalHits);

  }
  
  
  public void testNoSuchMultiTermsInSpanFirst() throws Exception {
    //this hasn't been a problem  
    FuzzyQuery fuzzyNoSuch = new FuzzyQuery(new Term("field", "noSuch"), 1, 0, 1, false);
    SpanQuery spanNoSuch = new SpanMultiTermQueryWrapper<>(fuzzyNoSuch);
    SpanQuery spanFirst = new SpanFirstQuery(spanNoSuch, 10);
 
    assertEquals(0, searcher.search(spanFirst, 10).totalHits);
    
    WildcardQuery wcNoSuch = new WildcardQuery(new Term("field", "noSuch*"));
    SpanQuery spanWCNoSuch = new SpanMultiTermQueryWrapper<>(wcNoSuch);
    spanFirst = new SpanFirstQuery(spanWCNoSuch, 10);
    assertEquals(0, searcher.search(spanFirst, 10).totalHits);
  
    RegexpQuery rgxNoSuch = new RegexpQuery(new Term("field", "noSuch"));
    SpanQuery spanRgxNoSuch = new SpanMultiTermQueryWrapper<>(rgxNoSuch);
    spanFirst = new SpanFirstQuery(spanRgxNoSuch, 10);
    assertEquals(0, searcher.search(spanFirst, 10).totalHits);
    
    PrefixQuery prfxNoSuch = new PrefixQuery(new Term("field", "noSuch"));
    SpanQuery spanPrfxNoSuch = new SpanMultiTermQueryWrapper<>(prfxNoSuch);
    spanFirst = new SpanFirstQuery(spanPrfxNoSuch, 10);
    assertEquals(0, searcher.search(spanFirst, 10).totalHits);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3077.java