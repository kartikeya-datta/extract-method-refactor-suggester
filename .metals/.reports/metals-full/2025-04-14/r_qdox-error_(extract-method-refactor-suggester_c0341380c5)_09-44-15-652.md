error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3188.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3188.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3188.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.misc;

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

import java.util.Random;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class TestHighFreqTerms extends LuceneTestCase {
 
  private static IndexWriter writer =null;
  private static Directory dir = null;
  private static IndexReader reader =null;
  
  @BeforeClass
  public static void setUpClass() throws Exception {
    dir = newDirectory();
    writer = new IndexWriter(dir, newIndexWriterConfig(random(),
       TEST_VERSION_CURRENT, new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false))
       .setMaxBufferedDocs(2));
    indexDocs(writer);
    reader = DirectoryReader.open(dir);
    TestUtil.checkIndex(dir);
  }
  
  @AfterClass
  public static void tearDownClass() throws Exception{
    reader.close();
    dir.close();
    dir = null;
    reader = null;
    writer = null;
  }
/******************** Tests for getHighFreqTerms **********************************/
  
  // test without specifying field (i.e. if we pass in field=null it should examine all fields)
  // the term "diff" in the field "different_field" occurs 20 times and is the highest df term
  public void testFirstTermHighestDocFreqAllFields () throws Exception{
    int numTerms = 12;
    String field =null;
    TermStats[] terms = HighFreqTerms.getHighFreqTerms(reader, numTerms, field, new HighFreqTerms.DocFreqComparator());
    assertEquals("Term with highest docfreq is first", 20,terms[0].docFreq );
  }
  
  public void testFirstTermHighestDocFreq () throws Exception{
    int numTerms = 12;
    String field="FIELD_1";
    TermStats[] terms = HighFreqTerms.getHighFreqTerms(reader, numTerms, field, new HighFreqTerms.DocFreqComparator());
    assertEquals("Term with highest docfreq is first", 10,terms[0].docFreq );
  }

  public void testOrderedByDocFreqDescending () throws Exception{
    int numTerms = 12;
    String field="FIELD_1";
    TermStats[] terms = HighFreqTerms.getHighFreqTerms(reader, numTerms, field, new HighFreqTerms.DocFreqComparator());
    for (int i = 0; i < terms.length; i++) {
      if (i > 0) {
        assertTrue ("out of order " + terms[i-1].docFreq + "should be >= " + terms[i].docFreq,terms[i-1].docFreq >= terms[i].docFreq);
      }
    }    
  }
  
  public void testNumTerms () throws Exception{
    int numTerms = 12;
    String field = null;
    TermStats[] terms = HighFreqTerms.getHighFreqTerms(reader, numTerms, field, new HighFreqTerms.DocFreqComparator());
    assertEquals("length of terms array equals numTerms :" + numTerms, numTerms, terms.length);
  }
    
  public void testGetHighFreqTerms () throws Exception{
    int numTerms=12;
    String field="FIELD_1";
    TermStats[] terms = HighFreqTerms.getHighFreqTerms(reader, numTerms, field, new HighFreqTerms.DocFreqComparator());
       
    for (int i = 0; i < terms.length; i++) {
      String termtext = terms[i].termtext.utf8ToString();
      // hardcoded highTF or highTFmedDF
      if (termtext.contains("highTF")) {
        if (termtext.contains("medDF")) {
          assertEquals("doc freq is not as expected", 5, terms[i].docFreq);
        } else {
          assertEquals("doc freq is not as expected", 1, terms[i].docFreq);
        }
      } else {
        int n = Integer.parseInt(termtext);
        assertEquals("doc freq is not as expected", getExpecteddocFreq(n),
            terms[i].docFreq);
      }
    }
  }
  
  /********************Test sortByTotalTermFreq**********************************/
  
  public void testFirstTermHighestTotalTermFreq () throws Exception{
    int numTerms = 20;
    String field = null;
    TermStats[] terms = HighFreqTerms.getHighFreqTerms(reader, numTerms, field, new HighFreqTerms.TotalTermFreqComparator());
    assertEquals("Term with highest totalTermFreq is first",200, terms[0].totalTermFreq);
  }

  public void testFirstTermHighestTotalTermFreqDifferentField () throws Exception{
    int numTerms = 20;
    String field = "different_field";
    TermStats[] terms = HighFreqTerms.getHighFreqTerms(reader, numTerms, field, new HighFreqTerms.TotalTermFreqComparator());
    assertEquals("Term with highest totalTermFreq is first"+ terms[0].getTermText(),150, terms[0].totalTermFreq);
  }
  
  public void testOrderedByTermFreqDescending () throws Exception{
    int numTerms = 12;
    String field = "FIELD_1";
    TermStats[] terms = HighFreqTerms.getHighFreqTerms(reader, numTerms, field, new HighFreqTerms.TotalTermFreqComparator());
 
    for (int i = 0; i < terms.length; i++) {
      // check that they are sorted by descending termfreq
      // order
      if (i > 0) {
        assertTrue ("out of order" +terms[i-1]+ " > " +terms[i],terms[i-1].totalTermFreq >= terms[i].totalTermFreq);
      }
    } 
  }
  
  public void testGetTermFreqOrdered () throws Exception{
    int numTerms = 12;
    String field = "FIELD_1";
    TermStats[] terms = HighFreqTerms.getHighFreqTerms(reader, numTerms, field, new HighFreqTerms.TotalTermFreqComparator());
   
    for (int i = 0; i < terms.length; i++) {
      String text = terms[i].termtext.utf8ToString();
      if (text.contains("highTF")) {
        if (text.contains("medDF")) {
          assertEquals("total term freq is expected", 125,
                       terms[i].totalTermFreq);
        } else {
          assertEquals("total term freq is expected", 200,
                       terms[i].totalTermFreq);
        }
        
      } else {
        int n = Integer.parseInt(text);
        assertEquals("doc freq is expected", getExpecteddocFreq(n),
                     terms[i].docFreq);
        assertEquals("total term freq is expected", getExpectedtotalTermFreq(n),
                     terms[i].totalTermFreq);
      }
    }
  }

  /********************Testing Utils**********************************/
    
  private static void indexDocs(IndexWriter writer) throws Exception {
    Random rnd = random();
    
    /**
     * Generate 10 documents where term n  has a docFreq of n and a totalTermFreq of n*2 (squared). 
     */
    for (int i = 1; i <= 10; i++) {
      Document doc = new Document();
      String content = getContent(i);
    
      doc.add(newTextField(rnd, "FIELD_1", content, Field.Store.YES));
      //add a different field
      doc.add(newTextField(rnd, "different_field", "diff", Field.Store.YES));
      writer.addDocument(doc);
    }
    
    //add 10 more docs with the term "diff" this will make it have the highest docFreq if we don't ask for the
    //highest freq terms for a specific field.
    for (int i = 1; i <= 10; i++) {
      Document doc = new Document();
      doc.add(newTextField(rnd, "different_field", "diff", Field.Store.YES));
      writer.addDocument(doc);
    }
    // add some docs where tf < df so we can see if sorting works
    // highTF low df
    int highTF = 200;
    Document doc = new Document();
    String content = "";
    for (int i = 0; i < highTF; i++) {
      content += "highTF ";
    }
    doc.add(newTextField(rnd, "FIELD_1", content, Field.Store.YES));
    writer.addDocument(doc);
    // highTF medium df =5
    int medium_df = 5;
    for (int i = 0; i < medium_df; i++) {
      int tf = 25;
      Document newdoc = new Document();
      String newcontent = "";
      for (int j = 0; j < tf; j++) {
        newcontent += "highTFmedDF ";
      }
      newdoc.add(newTextField(rnd, "FIELD_1", newcontent, Field.Store.YES));
      writer.addDocument(newdoc);
    }
    // add a doc with high tf in field different_field
    int targetTF =150;
    doc = new Document();
    content = "";
    for (int i = 0; i < targetTF; i++) {
      content += "TF150 ";
    }
    doc.add(newTextField(rnd, "different_field", content, Field.Store.YES));
    writer.addDocument(doc);
    writer.close();
    
  }
  
  /**
   *  getContent
   *  return string containing numbers 1 to i with each number n occurring n times.
   *  i.e. for input of 3 return string "3 3 3 2 2 1" 
   */
    
  private static String getContent(int i) {
    String s = "";
    for (int j = 10; j >= i; j--) {
      for (int k = 0; k < j; k++) {
        // if j is 3 we return "3 3 3"
        s += String.valueOf(j) + " ";
      }
    }
    return s;
  }
  
  private static int getExpectedtotalTermFreq(int i) {
    return getExpecteddocFreq(i) * i;
  }
  
  private static int getExpecteddocFreq(int i) {
    return i;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3188.java