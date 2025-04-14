error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3749.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3749.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3749.java
text:
```scala
I@@ndexWriter writer = new IndexWriter(indexStore, new StandardAnalyzer(TEST_VERSION_CURRENT, Collections.emptySet()), true, IndexWriter.MaxFieldLength.LIMITED);

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

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import org.apache.lucene.util.LuceneTestCase;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Collections;

/**
 * This class tests the MultiPhraseQuery class.
 *
 *
 */
public class TestMultiPhraseQuery extends LuceneTestCase
{
    public TestMultiPhraseQuery(String name) {
        super(name);
    }

    public void testPhrasePrefix() throws IOException {
        RAMDirectory indexStore = new RAMDirectory();
        IndexWriter writer = new IndexWriter(indexStore, new SimpleAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);
        add("blueberry pie", writer);
        add("blueberry strudel", writer);
        add("blueberry pizza", writer);
        add("blueberry chewing gum", writer);
        add("bluebird pizza", writer);
        add("bluebird foobar pizza", writer);
        add("piccadilly circus", writer);
        writer.optimize();
        writer.close();

        IndexSearcher searcher = new IndexSearcher(indexStore, true);

        // search for "blueberry pi*":
        MultiPhraseQuery query1 = new MultiPhraseQuery();
        // search for "strawberry pi*":
        MultiPhraseQuery query2 = new MultiPhraseQuery();
        query1.add(new Term("body", "blueberry"));
        query2.add(new Term("body", "strawberry"));

        LinkedList termsWithPrefix = new LinkedList();
        IndexReader ir = IndexReader.open(indexStore, true);

        // this TermEnum gives "piccadilly", "pie" and "pizza".
        String prefix = "pi";
        TermEnum te = ir.terms(new Term("body", prefix));
        do {
            if (te.term().text().startsWith(prefix))
            {
                termsWithPrefix.add(te.term());
            }
        } while (te.next());

        query1.add((Term[])termsWithPrefix.toArray(new Term[0]));
        assertEquals("body:\"blueberry (piccadilly pie pizza)\"", query1.toString());
        query2.add((Term[])termsWithPrefix.toArray(new Term[0]));
        assertEquals("body:\"strawberry (piccadilly pie pizza)\"", query2.toString());

        ScoreDoc[] result;
        result = searcher.search(query1, null, 1000).scoreDocs;
        assertEquals(2, result.length);
        result = searcher.search(query2, null, 1000).scoreDocs;
        assertEquals(0, result.length);

        // search for "blue* pizza":
        MultiPhraseQuery query3 = new MultiPhraseQuery();
        termsWithPrefix.clear();
        prefix = "blue";
        te = ir.terms(new Term("body", prefix));
        do {
            if (te.term().text().startsWith(prefix))
            {
                termsWithPrefix.add(te.term());
            }
        } while (te.next());
        query3.add((Term[])termsWithPrefix.toArray(new Term[0]));
        query3.add(new Term("body", "pizza"));

        result = searcher.search(query3, null, 1000).scoreDocs;
        assertEquals(2, result.length); // blueberry pizza, bluebird pizza
        assertEquals("body:\"(blueberry bluebird) pizza\"", query3.toString());

        // test slop:
        query3.setSlop(1);
        result = searcher.search(query3, null, 1000).scoreDocs;
        assertEquals(3, result.length); // blueberry pizza, bluebird pizza, bluebird foobar pizza

        MultiPhraseQuery query4 = new MultiPhraseQuery();
        try {
          query4.add(new Term("field1", "foo"));
          query4.add(new Term("field2", "foobar"));
          fail();
        } catch(IllegalArgumentException e) {
          // okay, all terms must belong to the same field
        }
        
        searcher.close();
        indexStore.close();

    }
    
    private void add(String s, IndexWriter writer) throws IOException {
      Document doc = new Document();
      doc.add(new Field("body", s, Field.Store.YES, Field.Index.ANALYZED));
      writer.addDocument(doc);
    }
    
    public void testBooleanQueryContainingSingleTermPrefixQuery() throws IOException {
      // this tests against bug 33161 (now fixed)
      // In order to cause the bug, the outer query must have more than one term 
      // and all terms required.
      // The contained PhraseMultiQuery must contain exactly one term array.

      RAMDirectory indexStore = new RAMDirectory();
      IndexWriter writer = new IndexWriter(indexStore, new SimpleAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);
      add("blueberry pie", writer);
      add("blueberry chewing gum", writer);
      add("blue raspberry pie", writer);
      writer.optimize();
      writer.close();

      IndexSearcher searcher = new IndexSearcher(indexStore, true);
      // This query will be equivalent to +body:pie +body:"blue*"
      BooleanQuery q = new BooleanQuery();
      q.add(new TermQuery(new Term("body", "pie")), BooleanClause.Occur.MUST);

      MultiPhraseQuery trouble = new MultiPhraseQuery();
      trouble.add(new Term[] {
          new Term("body", "blueberry"),
          new Term("body", "blue")
      });
      q.add(trouble, BooleanClause.Occur.MUST);

      // exception will be thrown here without fix
      ScoreDoc[] hits = searcher.search(q, null, 1000).scoreDocs;

      assertEquals("Wrong number of hits", 2, hits.length);
      searcher.close();
  }
    
  public void testPhrasePrefixWithBooleanQuery() throws IOException {
    RAMDirectory indexStore = new RAMDirectory();
    IndexWriter writer = new IndexWriter(indexStore, new StandardAnalyzer(org.apache.lucene.util.Version.LUCENE_CURRENT, Collections.emptySet()), true, IndexWriter.MaxFieldLength.LIMITED);
    add("This is a test", "object", writer);
    add("a note", "note", writer);
    writer.close();
    
    IndexSearcher searcher = new IndexSearcher(indexStore, true);

    // This query will be equivalent to +type:note +body:"a t*"
    BooleanQuery q = new BooleanQuery();
    q.add(new TermQuery(new Term("type", "note")), BooleanClause.Occur.MUST);

    MultiPhraseQuery trouble = new MultiPhraseQuery();
    trouble.add(new Term("body", "a"));
    trouble.add(new Term[] { new Term("body", "test"), new Term("body", "this") });
    q.add(trouble, BooleanClause.Occur.MUST);

    // exception will be thrown here without fix for #35626:
    ScoreDoc[] hits = searcher.search(q, null, 1000).scoreDocs;
    assertEquals("Wrong number of hits", 0, hits.length);
    searcher.close();
  }
  
  public void testHashCodeAndEquals(){
    MultiPhraseQuery query1 = new MultiPhraseQuery();
    MultiPhraseQuery query2 = new MultiPhraseQuery();
    
    assertEquals(query1.hashCode(), query2.hashCode());
    assertEquals(query1,query2);
    
    Term term1= new Term("someField","someText");
    
    query1.add(term1);
    query2.add(term1);
    
    assertEquals(query1.hashCode(), query2.hashCode());
    assertEquals(query1,query2);
    
    Term term2= new Term("someField","someMoreText");
    
    query1.add(term2);
    
    assertFalse(query1.hashCode()==query2.hashCode());
    assertFalse(query1.equals(query2));
    
    query2.add(term2);
    
    assertEquals(query1.hashCode(), query2.hashCode());
    assertEquals(query1,query2);
  }

  
  private void add(String s, String type, IndexWriter writer) throws IOException {
    Document doc = new Document();
    doc.add(new Field("body", s, Field.Store.YES, Field.Index.ANALYZED));
    doc.add(new Field("type", type, Field.Store.YES, Field.Index.NOT_ANALYZED));
    writer.addDocument(doc);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3749.java