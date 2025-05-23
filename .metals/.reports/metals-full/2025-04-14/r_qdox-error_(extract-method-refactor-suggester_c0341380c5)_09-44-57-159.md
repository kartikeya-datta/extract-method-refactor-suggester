error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2943.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2943.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2943.java
text:
```scala
s@@earcher = new IndexSearcher(directory, true);

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

import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.spans.SpanFirstQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanNotQuery;
import org.apache.lucene.search.spans.SpanOrQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.LuceneTestCase;

/**
 * Tests primitive queries (ie: that rewrite to themselves) to
 * insure they match the expected set of docs, and that the score of each
 * match is equal to the value of the scores explanation.
 *
 * <p>
 * The assumption is that if all of the "primitive" queries work well,
 * then anything that rewrites to a primitive will work well also.
 * </p>
 *
 * @see "Subclasses for actual tests"
 */
public class TestExplanations extends LuceneTestCase {
  protected IndexSearcher searcher;

  public static final String KEY = "KEY";
  public static final String FIELD = "field";
  public static final QueryParser qp =
    new QueryParser(FIELD, new WhitespaceAnalyzer());

  public void tearDown() throws Exception {
    super.tearDown();
    searcher.close();
  }
  
  public void setUp() throws Exception {
    super.setUp();
    RAMDirectory directory = new RAMDirectory();
    IndexWriter writer= new IndexWriter(directory, new WhitespaceAnalyzer(), true,
                                        IndexWriter.MaxFieldLength.LIMITED);
    for (int i = 0; i < docFields.length; i++) {
      Document doc = new Document();
      doc.add(new Field(KEY, ""+i, Field.Store.NO, Field.Index.NOT_ANALYZED));
      doc.add(new Field(FIELD, docFields[i], Field.Store.NO, Field.Index.ANALYZED));
      writer.addDocument(doc);
    }
    writer.close();
    searcher = new IndexSearcher(directory);
  }

  protected String[] docFields = {
    "w1 w2 w3 w4 w5",
    "w1 w3 w2 w3 zz",
    "w1 xx w2 yy w3",
    "w1 w3 xx w2 yy w3 zz"
  };

  public Query makeQuery(String queryText) throws ParseException {
    return qp.parse(queryText);
  }

  /** check the expDocNrs first, then check the query (and the explanations) */
  public void qtest(String queryText, int[] expDocNrs) throws Exception {
    qtest(makeQuery(queryText), expDocNrs);
  }
  
  /** check the expDocNrs first, then check the query (and the explanations) */
  public void qtest(Query q, int[] expDocNrs) throws Exception {
    CheckHits.checkHitCollector(q, FIELD, searcher, expDocNrs);
  }

  /**
   * Tests a query using qtest after wrapping it with both optB and reqB
   * @see #qtest
   * @see #reqB
   * @see #optB
   */
  public void bqtest(Query q, int[] expDocNrs) throws Exception {
    qtest(reqB(q), expDocNrs);
    qtest(optB(q), expDocNrs);
  }
  /**
   * Tests a query using qtest after wrapping it with both optB and reqB
   * @see #qtest
   * @see #reqB
   * @see #optB
   */
  public void bqtest(String queryText, int[] expDocNrs) throws Exception {
    bqtest(makeQuery(queryText), expDocNrs);
  }
  
  /** 
   * Convenience subclass of FieldCacheTermsFilter
   */
  public static class ItemizedFilter extends FieldCacheTermsFilter {
    private static String[] int2str(int [] terms) {
      String [] out = new String[terms.length];
      for (int i = 0; i < terms.length; i++) {
        out[i] = ""+terms[i];
      }
      return out;
    }
    public ItemizedFilter(String keyField, int [] keys) {
      super(keyField, int2str(keys));
    }
    public ItemizedFilter(int [] keys) {
      super(KEY, int2str(keys));
    }
  }

  /** helper for generating MultiPhraseQueries */
  public static Term[] ta(String[] s) {
    Term[] t = new Term[s.length];
    for (int i = 0; i < s.length; i++) {
      t[i] = new Term(FIELD, s[i]);
    }
    return t;
  }

  /** MACRO for SpanTermQuery */
  public SpanTermQuery st(String s) {
    return new SpanTermQuery(new Term(FIELD,s));
  }
  
  /** MACRO for SpanNotQuery */
  public SpanNotQuery snot(SpanQuery i, SpanQuery e) {
    return new SpanNotQuery(i,e);
  }

  /** MACRO for SpanOrQuery containing two SpanTerm queries */
  public SpanOrQuery sor(String s, String e) {
    return sor(st(s), st(e));
  }
  /** MACRO for SpanOrQuery containing two SpanQueries */
  public SpanOrQuery sor(SpanQuery s, SpanQuery e) {
    return new SpanOrQuery(new SpanQuery[] { s, e });
  }
  
  /** MACRO for SpanOrQuery containing three SpanTerm queries */
  public SpanOrQuery sor(String s, String m, String e) {
    return sor(st(s), st(m), st(e));
  }
  /** MACRO for SpanOrQuery containing two SpanQueries */
  public SpanOrQuery sor(SpanQuery s, SpanQuery m, SpanQuery e) {
    return new SpanOrQuery(new SpanQuery[] { s, m, e });
  }
  
  /** MACRO for SpanNearQuery containing two SpanTerm queries */
  public SpanNearQuery snear(String s, String e, int slop, boolean inOrder) {
    return snear(st(s), st(e), slop, inOrder);
  }
  /** MACRO for SpanNearQuery containing two SpanQueries */
  public SpanNearQuery snear(SpanQuery s, SpanQuery e,
                             int slop, boolean inOrder) {
    return new SpanNearQuery(new SpanQuery[] { s, e }, slop, inOrder);
  }
  
  
  /** MACRO for SpanNearQuery containing three SpanTerm queries */
  public SpanNearQuery snear(String s, String m, String e,
                             int slop, boolean inOrder) {
    return snear(st(s), st(m), st(e), slop, inOrder);
  }
  /** MACRO for SpanNearQuery containing three SpanQueries */
  public SpanNearQuery snear(SpanQuery s, SpanQuery m, SpanQuery e,
                             int slop, boolean inOrder) {
    return new SpanNearQuery(new SpanQuery[] { s, m, e }, slop, inOrder);
  }
  
  /** MACRO for SpanFirst(SpanTermQuery) */
  public SpanFirstQuery sf(String s, int b) {
    return new SpanFirstQuery(st(s), b);
  }

  /**
   * MACRO: Wraps a Query in a BooleanQuery so that it is optional, along
   * with a second prohibited clause which will never match anything
   */
  public Query optB(String q) throws Exception {
    return optB(makeQuery(q));
  }
  /**
   * MACRO: Wraps a Query in a BooleanQuery so that it is optional, along
   * with a second prohibited clause which will never match anything
   */
  public Query optB(Query q) throws Exception {
    BooleanQuery bq = new BooleanQuery(true);
    bq.add(q, BooleanClause.Occur.SHOULD);
    bq.add(new TermQuery(new Term("NEVER","MATCH")), BooleanClause.Occur.MUST_NOT);
    return bq;
  }
  
  /**
   * MACRO: Wraps a Query in a BooleanQuery so that it is required, along
   * with a second optional clause which will match everything
   */
  public Query reqB(String q) throws Exception {
    return reqB(makeQuery(q));
  }
  /**
   * MACRO: Wraps a Query in a BooleanQuery so that it is required, along
   * with a second optional clause which will match everything
   */
  public Query reqB(Query q) throws Exception {
    BooleanQuery bq = new BooleanQuery(true);
    bq.add(q, BooleanClause.Occur.MUST);
    bq.add(new TermQuery(new Term(FIELD,"w1")), BooleanClause.Occur.SHOULD);
    return bq;
  }
  
  /**
   * Placeholder: JUnit freaks if you don't have one test ... making
   * class abstract doesn't help
   */
  public void testNoop() {
    /* NOOP */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2943.java