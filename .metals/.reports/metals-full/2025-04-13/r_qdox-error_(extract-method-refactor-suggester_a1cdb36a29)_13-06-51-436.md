error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/114.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/114.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/114.java
text:
```scala
f@@or (AtomicReaderContext context : multi.leaves()) {

package org.apache.lucene.queries;

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

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.FixedBitSet;
import org.apache.lucene.util.LuceneTestCase;

public class TermsFilterTest extends LuceneTestCase {

  public void testCachability() throws Exception {
    TermsFilter a = new TermsFilter();
    a.addTerm(new Term("field1", "a"));
    a.addTerm(new Term("field1", "b"));
    HashSet<Filter> cachedFilters = new HashSet<Filter>();
    cachedFilters.add(a);
    TermsFilter b = new TermsFilter();
    b.addTerm(new Term("field1", "a"));
    b.addTerm(new Term("field1", "b"));

    assertTrue("Must be cached", cachedFilters.contains(b));
    b.addTerm(new Term("field1", "a")); //duplicate term
    assertTrue("Must be cached", cachedFilters.contains(b));
    b.addTerm(new Term("field1", "c"));
    assertFalse("Must not be cached", cachedFilters.contains(b));
  }

  public void testMissingTerms() throws Exception {
    String fieldName = "field1";
    Directory rd = newDirectory();
    RandomIndexWriter w = new RandomIndexWriter(random(), rd);
    for (int i = 0; i < 100; i++) {
      Document doc = new Document();
      int term = i * 10; //terms are units of 10;
      doc.add(newStringField(fieldName, "" + term, Field.Store.YES));
      w.addDocument(doc);
    }
    IndexReader reader = new SlowCompositeReaderWrapper(w.getReader());
    assertTrue(reader.getTopReaderContext() instanceof AtomicReaderContext);
    AtomicReaderContext context = (AtomicReaderContext) reader.getTopReaderContext();
    w.close();

    TermsFilter tf = new TermsFilter();
    tf.addTerm(new Term(fieldName, "19"));
    FixedBitSet bits = (FixedBitSet) tf.getDocIdSet(context, context.reader().getLiveDocs());
    assertEquals("Must match nothing", 0, bits.cardinality());

    tf.addTerm(new Term(fieldName, "20"));
    bits = (FixedBitSet) tf.getDocIdSet(context, context.reader().getLiveDocs());
    assertEquals("Must match 1", 1, bits.cardinality());

    tf.addTerm(new Term(fieldName, "10"));
    bits = (FixedBitSet) tf.getDocIdSet(context, context.reader().getLiveDocs());
    assertEquals("Must match 2", 2, bits.cardinality());

    tf.addTerm(new Term(fieldName, "00"));
    bits = (FixedBitSet) tf.getDocIdSet(context, context.reader().getLiveDocs());
    assertEquals("Must match 2", 2, bits.cardinality());

    reader.close();
    rd.close();
  }
  
  public void testMissingField() throws Exception {
    String fieldName = "field1";
    Directory rd1 = newDirectory();
    RandomIndexWriter w1 = new RandomIndexWriter(random(), rd1);
    Document doc = new Document();
    doc.add(newStringField(fieldName, "content1", Field.Store.YES));
    w1.addDocument(doc);
    IndexReader reader1 = w1.getReader();
    w1.close();
    
    fieldName = "field2";
    Directory rd2 = newDirectory();
    RandomIndexWriter w2 = new RandomIndexWriter(random(), rd2);
    doc = new Document();
    doc.add(newStringField(fieldName, "content2", Field.Store.YES));
    w2.addDocument(doc);
    IndexReader reader2 = w2.getReader();
    w2.close();
    
    TermsFilter tf = new TermsFilter();
    tf.addTerm(new Term(fieldName, "content1"));
    
    MultiReader multi = new MultiReader(reader1, reader2);
    for (AtomicReaderContext context : multi.getTopReaderContext().leaves()) {
      FixedBitSet bits = (FixedBitSet) tf.getDocIdSet(context, context.reader().getLiveDocs());
      assertTrue("Must be >= 0", bits.cardinality() >= 0);      
    }
    multi.close();
    reader1.close();
    reader2.close();
    rd1.close();
    rd2.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/114.java