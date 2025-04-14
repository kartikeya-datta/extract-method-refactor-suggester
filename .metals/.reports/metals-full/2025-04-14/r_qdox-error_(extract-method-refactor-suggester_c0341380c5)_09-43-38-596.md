error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3030.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3030.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3030.java
text:
```scala
i@@w.shutdown();

package org.apache.lucene.expressions;

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

import java.util.HashMap;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.expressions.js.JavascriptCompiler;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.ValueSourceScorer;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.SortField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.LuceneTestCase.SuppressCodecs;

@SuppressCodecs("Lucene3x")
public class TestExpressionValueSource extends LuceneTestCase {
  DirectoryReader reader;
  Directory dir;
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    dir = newDirectory();
    IndexWriterConfig iwc = newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random()));
    iwc.setMergePolicy(newLogMergePolicy());
    RandomIndexWriter iw = new RandomIndexWriter(random(), dir, iwc);
    
    Document doc = new Document();
    doc.add(newStringField("id", "1", Field.Store.YES));
    doc.add(newTextField("body", "some contents and more contents", Field.Store.NO));
    doc.add(new NumericDocValuesField("popularity", 5));
    iw.addDocument(doc);
    
    doc = new Document();
    doc.add(newStringField("id", "2", Field.Store.YES));
    doc.add(newTextField("body", "another document with different contents", Field.Store.NO));
    doc.add(new NumericDocValuesField("popularity", 20));
    iw.addDocument(doc);
    
    doc = new Document();
    doc.add(newStringField("id", "3", Field.Store.YES));
    doc.add(newTextField("body", "crappy contents", Field.Store.NO));
    doc.add(new NumericDocValuesField("popularity", 2));
    iw.addDocument(doc);
    iw.forceMerge(1);
    
    reader = iw.getReader();
    iw.close();
  }
  
  @Override
  public void tearDown() throws Exception {
    reader.close();
    dir.close();
    super.tearDown();
  }
  
  public void testTypes() throws Exception {
    Expression expr = JavascriptCompiler.compile("2*popularity");
    SimpleBindings bindings = new SimpleBindings();
    bindings.add(new SortField("popularity", SortField.Type.LONG));
    ValueSource vs = expr.getValueSource(bindings);
    
    assertEquals(1, reader.leaves().size());
    AtomicReaderContext leaf = reader.leaves().get(0);
    FunctionValues values = vs.getValues(new HashMap<String,Object>(), leaf);
    
    assertEquals(10, values.doubleVal(0), 0);
    assertEquals(10, values.floatVal(0), 0);
    assertEquals(10, values.longVal(0));
    assertEquals(10, values.intVal(0));
    assertEquals(10, values.shortVal(0));
    assertEquals(10, values.byteVal(0));
    assertEquals("10.0", values.strVal(0));
    assertEquals(new Double(10), values.objectVal(0));
    
    assertEquals(40, values.doubleVal(1), 0);
    assertEquals(40, values.floatVal(1), 0);
    assertEquals(40, values.longVal(1));
    assertEquals(40, values.intVal(1));
    assertEquals(40, values.shortVal(1));
    assertEquals(40, values.byteVal(1));
    assertEquals("40.0", values.strVal(1));
    assertEquals(new Double(40), values.objectVal(1));
    
    assertEquals(4, values.doubleVal(2), 0);
    assertEquals(4, values.floatVal(2), 0);
    assertEquals(4, values.longVal(2));
    assertEquals(4, values.intVal(2));
    assertEquals(4, values.shortVal(2));
    assertEquals(4, values.byteVal(2));
    assertEquals("4.0", values.strVal(2));
    assertEquals(new Double(4), values.objectVal(2));    
  }
  
  public void testRangeScorer() throws Exception {
    Expression expr = JavascriptCompiler.compile("2*popularity");
    SimpleBindings bindings = new SimpleBindings();
    bindings.add(new SortField("popularity", SortField.Type.LONG));
    ValueSource vs = expr.getValueSource(bindings);
    
    assertEquals(1, reader.leaves().size());
    AtomicReaderContext leaf = reader.leaves().get(0);
    FunctionValues values = vs.getValues(new HashMap<String,Object>(), leaf);
    
    // everything
    ValueSourceScorer scorer = values.getRangeScorer(leaf.reader(), "4", "40", true, true);
    assertEquals(-1, scorer.docID());
    assertEquals(0, scorer.nextDoc());
    assertEquals(1, scorer.nextDoc());
    assertEquals(2, scorer.nextDoc());
    assertEquals(DocIdSetIterator.NO_MORE_DOCS, scorer.nextDoc());

    // just the first doc
    scorer = values.getRangeScorer(leaf.reader(), "4", "40", false, false);
    assertEquals(-1, scorer.docID());
    assertEquals(0, scorer.nextDoc());
    assertEquals(DocIdSetIterator.NO_MORE_DOCS, scorer.nextDoc());
  }
  
  public void testEquals() throws Exception {
    Expression expr = JavascriptCompiler.compile("sqrt(a) + ln(b)");
    
    SimpleBindings bindings = new SimpleBindings();    
    bindings.add(new SortField("a", SortField.Type.INT));
    bindings.add(new SortField("b", SortField.Type.INT));
    
    ValueSource vs1 = expr.getValueSource(bindings);
    // same instance
    assertEquals(vs1, vs1);
    // null
    assertFalse(vs1.equals(null));
    // other object
    assertFalse(vs1.equals("foobar"));
    // same bindings and expression instances
    ValueSource vs2 = expr.getValueSource(bindings);
    assertEquals(vs1.hashCode(), vs2.hashCode());
    assertEquals(vs1, vs2);
    // equiv bindings (different instance)
    SimpleBindings bindings2 = new SimpleBindings();    
    bindings2.add(new SortField("a", SortField.Type.INT));
    bindings2.add(new SortField("b", SortField.Type.INT));
    ValueSource vs3 = expr.getValueSource(bindings2);
    assertEquals(vs1, vs3);
    // different bindings (same names, different types)
    SimpleBindings bindings3 = new SimpleBindings();    
    bindings3.add(new SortField("a", SortField.Type.LONG));
    bindings3.add(new SortField("b", SortField.Type.INT));
    ValueSource vs4 = expr.getValueSource(bindings3);
    assertFalse(vs1.equals(vs4));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3030.java