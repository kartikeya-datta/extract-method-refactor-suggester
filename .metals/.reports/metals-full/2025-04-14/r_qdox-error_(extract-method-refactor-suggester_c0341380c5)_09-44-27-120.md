error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3032.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3032.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3032.java
text:
```scala
i@@w.shutdown();

package org.apache.lucene.expressions;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.expressions.js.JavascriptCompiler;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.CheckHits;
import org.apache.lucene.search.FieldDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;

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

/** simple demo of using expressions */
public class  TestDemoExpressions extends LuceneTestCase {
  IndexSearcher searcher;
  DirectoryReader reader;
  Directory dir;
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    dir = newDirectory();
    RandomIndexWriter iw = new RandomIndexWriter(random(), dir);
    
    Document doc = new Document();
    doc.add(newStringField("id", "1", Field.Store.YES));
    doc.add(newTextField("body", "some contents and more contents", Field.Store.NO));
    doc.add(new NumericDocValuesField("popularity", 5));
    doc.add(new DoubleField("latitude", 40.759011, Field.Store.NO));
    doc.add(new DoubleField("longitude", -73.9844722, Field.Store.NO));
    iw.addDocument(doc);
    
    doc = new Document();
    doc.add(newStringField("id", "2", Field.Store.YES));
    doc.add(newTextField("body", "another document with different contents", Field.Store.NO));
    doc.add(new NumericDocValuesField("popularity", 20));
    doc.add(new DoubleField("latitude", 40.718266, Field.Store.NO));
    doc.add(new DoubleField("longitude", -74.007819, Field.Store.NO));
    iw.addDocument(doc);
    
    doc = new Document();
    doc.add(newStringField("id", "3", Field.Store.YES));
    doc.add(newTextField("body", "crappy contents", Field.Store.NO));
    doc.add(new NumericDocValuesField("popularity", 2));
    doc.add(new DoubleField("latitude", 40.7051157, Field.Store.NO));
    doc.add(new DoubleField("longitude", -74.0088305, Field.Store.NO));
    iw.addDocument(doc);
    
    reader = iw.getReader();
    searcher = new IndexSearcher(reader);
    iw.close();
  }
  
  @Override
  public void tearDown() throws Exception {
    reader.close();
    dir.close();
    super.tearDown();
  }
  
  /** an example of how to rank by an expression */
  public void test() throws Exception {
    // compile an expression:
    Expression expr = JavascriptCompiler.compile("sqrt(_score) + ln(popularity)");
    
    // we use SimpleBindings: which just maps variables to SortField instances
    SimpleBindings bindings = new SimpleBindings();    
    bindings.add(new SortField("_score", SortField.Type.SCORE));
    bindings.add(new SortField("popularity", SortField.Type.INT));
    
    // create a sort field and sort by it (reverse order)
    Sort sort = new Sort(expr.getSortField(bindings, true));
    Query query = new TermQuery(new Term("body", "contents"));
    searcher.search(query, null, 3, sort);
  }
  
  /** tests the returned sort values are correct */
  public void testSortValues() throws Exception {
    Expression expr = JavascriptCompiler.compile("sqrt(_score)");
    
    SimpleBindings bindings = new SimpleBindings();    
    bindings.add(new SortField("_score", SortField.Type.SCORE));
    
    Sort sort = new Sort(expr.getSortField(bindings, true));
    Query query = new TermQuery(new Term("body", "contents"));
    TopFieldDocs td = searcher.search(query, null, 3, sort, true, true);
    for (int i = 0; i < 3; i++) {
      FieldDoc d = (FieldDoc) td.scoreDocs[i];
      float expected = (float) Math.sqrt(d.score);
      float actual = ((Double)d.fields[0]).floatValue();
      assertEquals(expected, actual, CheckHits.explainToleranceDelta(expected, actual));
    }
  }
  
  /** tests same binding used more than once in an expression */
  public void testTwoOfSameBinding() throws Exception {
    Expression expr = JavascriptCompiler.compile("_score + _score");
    
    SimpleBindings bindings = new SimpleBindings();    
    bindings.add(new SortField("_score", SortField.Type.SCORE));
    
    Sort sort = new Sort(expr.getSortField(bindings, true));
    Query query = new TermQuery(new Term("body", "contents"));
    TopFieldDocs td = searcher.search(query, null, 3, sort, true, true);
    for (int i = 0; i < 3; i++) {
      FieldDoc d = (FieldDoc) td.scoreDocs[i];
      float expected = 2*d.score;
      float actual = ((Double)d.fields[0]).floatValue();
      assertEquals(expected, actual, CheckHits.explainToleranceDelta(expected, actual));
    }
  }
  
  /** tests expression referring to another expression */
  public void testExpressionRefersToExpression() throws Exception {
    Expression expr1 = JavascriptCompiler.compile("_score");
    Expression expr2 = JavascriptCompiler.compile("2*expr1");
    
    SimpleBindings bindings = new SimpleBindings();    
    bindings.add(new SortField("_score", SortField.Type.SCORE));
    bindings.add("expr1", expr1);
    
    Sort sort = new Sort(expr2.getSortField(bindings, true));
    Query query = new TermQuery(new Term("body", "contents"));
    TopFieldDocs td = searcher.search(query, null, 3, sort, true, true);
    for (int i = 0; i < 3; i++) {
      FieldDoc d = (FieldDoc) td.scoreDocs[i];
      float expected = 2*d.score;
      float actual = ((Double)d.fields[0]).floatValue();
      assertEquals(expected, actual, CheckHits.explainToleranceDelta(expected, actual));
    }
  }
  
  /** tests huge amounts of variables in the expression */
  public void testLotsOfBindings() throws Exception {
    doTestLotsOfBindings(Byte.MAX_VALUE-1);
    doTestLotsOfBindings(Byte.MAX_VALUE);
    doTestLotsOfBindings(Byte.MAX_VALUE+1);
    // TODO: ideally we'd test > Short.MAX_VALUE too, but compilation is currently recursive.
    // so if we want to test such huge expressions, we need to instead change parser to use an explicit Stack
  }
  
  private void doTestLotsOfBindings(int n) throws Exception {
    SimpleBindings bindings = new SimpleBindings();    
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; i++) {
      if (i > 0) {
        sb.append("+");
      }
      sb.append("x" + i);
      bindings.add(new SortField("x" + i, SortField.Type.SCORE));
    }
    
    Expression expr = JavascriptCompiler.compile(sb.toString());
    Sort sort = new Sort(expr.getSortField(bindings, true));
    Query query = new TermQuery(new Term("body", "contents"));
    TopFieldDocs td = searcher.search(query, null, 3, sort, true, true);
    for (int i = 0; i < 3; i++) {
      FieldDoc d = (FieldDoc) td.scoreDocs[i];
      float expected = n*d.score;
      float actual = ((Double)d.fields[0]).floatValue();
      assertEquals(expected, actual, CheckHits.explainToleranceDelta(expected, actual));
    }
  }
  
  public void testDistanceSort() throws Exception {
    Expression distance = JavascriptCompiler.compile("haversin(40.7143528,-74.0059731,latitude,longitude)");
    SimpleBindings bindings = new SimpleBindings();
    bindings.add(new SortField("latitude", SortField.Type.DOUBLE));
    bindings.add(new SortField("longitude", SortField.Type.DOUBLE));
    Sort sort = new Sort(distance.getSortField(bindings, false));
    TopFieldDocs td = searcher.search(new MatchAllDocsQuery(), null, 3, sort);
    
    FieldDoc d = (FieldDoc) td.scoreDocs[0];
    assertEquals(0.4619D, (Double)d.fields[0], 1E-4);
    
    d = (FieldDoc) td.scoreDocs[1];
    assertEquals(1.0546D, (Double)d.fields[0], 1E-4);
    
    d = (FieldDoc) td.scoreDocs[2];
    assertEquals(5.2842D, (Double)d.fields[0], 1E-4);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3032.java