error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2796.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2796.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2796.java
text:
```scala
d@@ocBase = context.docBase;

package org.apache.lucene.search;

/**
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

import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexReader.AtomicReaderContext;
import org.apache.lucene.util.*;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Scorer.ScorerVisitor;
import org.apache.lucene.store.*;

import java.util.*;
import java.io.*;

import org.junit.Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class TestSubScorerFreqs extends LuceneTestCase {

  private static Directory dir;
  private static IndexSearcher s;

  @BeforeClass
  public static void makeIndex() throws Exception {
    dir = new RAMDirectory();
    RandomIndexWriter w = new RandomIndexWriter(
        random, dir);
    // make sure we have more than one segment occationally
    for (int i = 0; i < 31 * RANDOM_MULTIPLIER; i++) {
      Document doc = new Document();
      doc.add(newField("f", "a b c d b c d c d d", Field.Store.NO,
          Field.Index.ANALYZED));
      w.addDocument(doc);

      doc = new Document();
      doc.add(newField("f", "a b c d", Field.Store.NO, Field.Index.ANALYZED));
      w.addDocument(doc);
    }

    s = new IndexSearcher(w.getReader());
    w.close();
  }

  @AfterClass
  public static void finish() throws Exception {
    s.getIndexReader().close();
    s.close();
    s = null;
    dir.close();
    dir = null;
  }

  private static class CountingCollector extends Collector {
    private final Collector other;
    private int docBase;

    public final Map<Integer, Map<Query, Float>> docCounts = new HashMap<Integer, Map<Query, Float>>();

    private final Map<Query, Scorer> subScorers = new HashMap<Query, Scorer>();
    private final ScorerVisitor<Query, Query, Scorer> visitor = new MockScorerVisitor();
    private final EnumSet<Occur> collect;

    private class MockScorerVisitor extends ScorerVisitor<Query, Query, Scorer> {

      @Override
      public void visitOptional(Query parent, Query child, Scorer scorer) {
        if (collect.contains(Occur.SHOULD))
          subScorers.put(child, scorer);
      }

      @Override
      public void visitProhibited(Query parent, Query child, Scorer scorer) {
        if (collect.contains(Occur.MUST_NOT))
          subScorers.put(child, scorer);
      }

      @Override
      public void visitRequired(Query parent, Query child, Scorer scorer) {
        if (collect.contains(Occur.MUST))
          subScorers.put(child, scorer);
      }

    }

    public CountingCollector(Collector other) {
      this(other, EnumSet.allOf(Occur.class));
    }

    public CountingCollector(Collector other, EnumSet<Occur> collect) {
      this.other = other;
      this.collect = collect;
    }

    @Override
    public void setScorer(Scorer scorer) throws IOException {
      other.setScorer(scorer);
      scorer.visitScorers(visitor);
    }

    @Override
    public void collect(int doc) throws IOException {
      final Map<Query, Float> freqs = new HashMap<Query, Float>();
      for (Map.Entry<Query, Scorer> ent : subScorers.entrySet()) {
        Scorer value = ent.getValue();
        int matchId = value.docID();
        freqs.put(ent.getKey(), matchId == doc ? value.freq() : 0.0f);
      }
      docCounts.put(doc + docBase, freqs);
      other.collect(doc);
    }

    @Override
    public void setNextReader(AtomicReaderContext context)
        throws IOException {
      this.docBase = docBase;
      other.setNextReader(context);
    }

    @Override
    public boolean acceptsDocsOutOfOrder() {
      return other.acceptsDocsOutOfOrder();
    }
  }

  private static final float FLOAT_TOLERANCE = 0.00001F;

  @Test
  public void testTermQuery() throws Exception {
    TermQuery q = new TermQuery(new Term("f", "d"));
    CountingCollector c = new CountingCollector(TopScoreDocCollector.create(10,
        true));
    s.search(q, null, c);
    final int maxDocs = s.maxDoc();
    assertEquals(maxDocs, c.docCounts.size());
    for (int i = 0; i < maxDocs; i++) {
      Map<Query, Float> doc0 = c.docCounts.get(i);
      assertEquals(1, doc0.size());
      assertEquals(4.0F, doc0.get(q), FLOAT_TOLERANCE);

      Map<Query, Float> doc1 = c.docCounts.get(++i);
      assertEquals(1, doc1.size());
      assertEquals(1.0F, doc1.get(q), FLOAT_TOLERANCE);
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testBooleanQuery() throws Exception {
    TermQuery aQuery = new TermQuery(new Term("f", "a"));
    TermQuery dQuery = new TermQuery(new Term("f", "d"));
    TermQuery cQuery = new TermQuery(new Term("f", "c"));
    TermQuery yQuery = new TermQuery(new Term("f", "y"));

    BooleanQuery query = new BooleanQuery();
    BooleanQuery inner = new BooleanQuery();

    inner.add(cQuery, Occur.SHOULD);
    inner.add(yQuery, Occur.MUST_NOT);
    query.add(inner, Occur.MUST);
    query.add(aQuery, Occur.MUST);
    query.add(dQuery, Occur.MUST);
    EnumSet<Occur>[] occurList = new EnumSet[] {EnumSet.of(Occur.MUST), EnumSet.of(Occur.MUST, Occur.SHOULD)};
    for (EnumSet<Occur> occur : occurList) {
      CountingCollector c = new CountingCollector(TopScoreDocCollector.create(
          10, true), occur);
      s.search(query, null, c);
      final int maxDocs = s.maxDoc();
      assertEquals(maxDocs, c.docCounts.size());
      boolean includeOptional = occur.contains(Occur.SHOULD);
      for (int i = 0; i < maxDocs; i++) {
        Map<Query, Float> doc0 = c.docCounts.get(i);
        assertEquals(includeOptional ? 5 : 4, doc0.size());
        assertEquals(1.0F, doc0.get(aQuery), FLOAT_TOLERANCE);
        assertEquals(4.0F, doc0.get(dQuery), FLOAT_TOLERANCE);
        if (includeOptional)
          assertEquals(3.0F, doc0.get(cQuery), FLOAT_TOLERANCE);

        Map<Query, Float> doc1 = c.docCounts.get(++i);
        assertEquals(includeOptional ? 5 : 4, doc1.size());
        assertEquals(1.0F, doc1.get(aQuery), FLOAT_TOLERANCE);
        assertEquals(1.0F, doc1.get(dQuery), FLOAT_TOLERANCE);
        if (includeOptional)
          assertEquals(1.0F, doc1.get(cQuery), FLOAT_TOLERANCE);

      }
    }
  }

  @Test
  public void testPhraseQuery() throws Exception {
    PhraseQuery q = new PhraseQuery();
    q.add(new Term("f", "b"));
    q.add(new Term("f", "c"));
    CountingCollector c = new CountingCollector(TopScoreDocCollector.create(10,
        true));
    s.search(q, null, c);
    final int maxDocs = s.maxDoc();
    assertEquals(maxDocs, c.docCounts.size());
    for (int i = 0; i < maxDocs; i++) {
      Map<Query, Float> doc0 = c.docCounts.get(i);
      assertEquals(1, doc0.size());
      assertEquals(2.0F, doc0.get(q), FLOAT_TOLERANCE);

      Map<Query, Float> doc1 = c.docCounts.get(++i);
      assertEquals(1, doc1.size());
      assertEquals(1.0F, doc1.get(q), FLOAT_TOLERANCE);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2796.java