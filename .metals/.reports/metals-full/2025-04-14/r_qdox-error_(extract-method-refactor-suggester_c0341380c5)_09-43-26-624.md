error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2417.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2417.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2417.java
text:
```scala
s@@uper(PER_FIELD_REUSE_STRATEGY);

package org.apache.lucene.queryparser.classic;

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

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.LuceneTestCase;

/**
 * Tests QueryParser.
 */
public class TestMultiFieldQueryParser extends LuceneTestCase {

  /** test stop words parsing for both the non static form, and for the 
   * corresponding static form (qtxt, fields[]). */
  public void testStopwordsParsing() throws Exception {
    assertStopQueryEquals("one", "b:one t:one");  
    assertStopQueryEquals("one stop", "b:one t:one");  
    assertStopQueryEquals("one (stop)", "b:one t:one");  
    assertStopQueryEquals("one ((stop))", "b:one t:one");  
    assertStopQueryEquals("stop", "");  
    assertStopQueryEquals("(stop)", "");  
    assertStopQueryEquals("((stop))", "");  
  }

  // verify parsing of query using a stopping analyzer  
  private void assertStopQueryEquals (String qtxt, String expectedRes) throws Exception {
    String[] fields = {"b", "t"};
    Occur occur[] = {Occur.SHOULD, Occur.SHOULD};
    TestQueryParser.QPTestAnalyzer a = new TestQueryParser.QPTestAnalyzer();
    MultiFieldQueryParser mfqp = new MultiFieldQueryParser(TEST_VERSION_CURRENT, fields, a);
    
    Query q = mfqp.parse(qtxt);
    assertEquals(expectedRes, q.toString());
    
    q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, qtxt, fields, occur, a);
    assertEquals(expectedRes, q.toString());
  }
  
  public void testSimple() throws Exception {
    String[] fields = {"b", "t"};
    MultiFieldQueryParser mfqp = new MultiFieldQueryParser(TEST_VERSION_CURRENT, fields, new MockAnalyzer(random()));
    
    Query q = mfqp.parse("one");
    assertEquals("b:one t:one", q.toString());
    
    q = mfqp.parse("one two");
    assertEquals("(b:one t:one) (b:two t:two)", q.toString());
    
    q = mfqp.parse("+one +two");
    assertEquals("+(b:one t:one) +(b:two t:two)", q.toString());

    q = mfqp.parse("+one -two -three");
    assertEquals("+(b:one t:one) -(b:two t:two) -(b:three t:three)", q.toString());
    
    q = mfqp.parse("one^2 two");
    assertEquals("((b:one t:one)^2.0) (b:two t:two)", q.toString());

    q = mfqp.parse("one~ two");
    assertEquals("(b:one~2 t:one~2) (b:two t:two)", q.toString());

    q = mfqp.parse("one~0.8 two^2");
    assertEquals("(b:one~0 t:one~0) ((b:two t:two)^2.0)", q.toString());

    q = mfqp.parse("one* two*");
    assertEquals("(b:one* t:one*) (b:two* t:two*)", q.toString());

    q = mfqp.parse("[a TO c] two");
    assertEquals("(b:[a TO c] t:[a TO c]) (b:two t:two)", q.toString());

    q = mfqp.parse("w?ldcard");
    assertEquals("b:w?ldcard t:w?ldcard", q.toString());

    q = mfqp.parse("\"foo bar\"");
    assertEquals("b:\"foo bar\" t:\"foo bar\"", q.toString());

    q = mfqp.parse("\"aa bb cc\" \"dd ee\"");
    assertEquals("(b:\"aa bb cc\" t:\"aa bb cc\") (b:\"dd ee\" t:\"dd ee\")", q.toString());

    q = mfqp.parse("\"foo bar\"~4");
    assertEquals("b:\"foo bar\"~4 t:\"foo bar\"~4", q.toString());

    // LUCENE-1213: MultiFieldQueryParser was ignoring slop when phrase had a field.
    q = mfqp.parse("b:\"foo bar\"~4"); 
    assertEquals("b:\"foo bar\"~4", q.toString());

    // make sure that terms which have a field are not touched:
    q = mfqp.parse("one f:two");
    assertEquals("(b:one t:one) f:two", q.toString());

    // AND mode:
    mfqp.setDefaultOperator(QueryParserBase.AND_OPERATOR);
    q = mfqp.parse("one two");
    assertEquals("+(b:one t:one) +(b:two t:two)", q.toString());
    q = mfqp.parse("\"aa bb cc\" \"dd ee\"");
    assertEquals("+(b:\"aa bb cc\" t:\"aa bb cc\") +(b:\"dd ee\" t:\"dd ee\")", q.toString());

  }
  
  public void testBoostsSimple() throws Exception {
      Map<String,Float> boosts = new HashMap<String,Float>();
      boosts.put("b", Float.valueOf(5));
      boosts.put("t", Float.valueOf(10));
      String[] fields = {"b", "t"};
      MultiFieldQueryParser mfqp = new MultiFieldQueryParser(TEST_VERSION_CURRENT, fields, new MockAnalyzer(random()), boosts);
      
      
      //Check for simple
      Query q = mfqp.parse("one");
      assertEquals("b:one^5.0 t:one^10.0", q.toString());
      
      //Check for AND
      q = mfqp.parse("one AND two");
      assertEquals("+(b:one^5.0 t:one^10.0) +(b:two^5.0 t:two^10.0)", q.toString());
      
      //Check for OR
      q = mfqp.parse("one OR two");
      assertEquals("(b:one^5.0 t:one^10.0) (b:two^5.0 t:two^10.0)", q.toString());
      
      //Check for AND and a field
      q = mfqp.parse("one AND two AND foo:test");
      assertEquals("+(b:one^5.0 t:one^10.0) +(b:two^5.0 t:two^10.0) +foo:test", q.toString());
      
      q = mfqp.parse("one^3 AND two^4");
      assertEquals("+((b:one^5.0 t:one^10.0)^3.0) +((b:two^5.0 t:two^10.0)^4.0)", q.toString());
  }

  public void testStaticMethod1() throws ParseException {
    String[] fields = {"b", "t"};
    String[] queries = {"one", "two"};
    Query q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, queries, fields, new MockAnalyzer(random()));
    assertEquals("b:one t:two", q.toString());

    String[] queries2 = {"+one", "+two"};
    q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, queries2, fields, new MockAnalyzer(random()));
    assertEquals("(+b:one) (+t:two)", q.toString());

    String[] queries3 = {"one", "+two"};
    q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, queries3, fields, new MockAnalyzer(random()));
    assertEquals("b:one (+t:two)", q.toString());

    String[] queries4 = {"one +more", "+two"};
    q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, queries4, fields, new MockAnalyzer(random()));
    assertEquals("(b:one +b:more) (+t:two)", q.toString());

    String[] queries5 = {"blah"};
    try {
      q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, queries5, fields, new MockAnalyzer(random()));
      fail();
    } catch(IllegalArgumentException e) {
      // expected exception, array length differs
    }
    
    // check also with stop words for this static form (qtxts[], fields[]).
    TestQueryParser.QPTestAnalyzer stopA = new TestQueryParser.QPTestAnalyzer();
    
    String[] queries6 = {"((+stop))", "+((stop))"};
    q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, queries6, fields, stopA);
    assertEquals("", q.toString());
    
    String[] queries7 = {"one ((+stop)) +more", "+((stop)) +two"};
    q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, queries7, fields, stopA);
    assertEquals("(b:one +b:more) (+t:two)", q.toString());

  }

  public void testStaticMethod2() throws ParseException {
    String[] fields = {"b", "t"};
    BooleanClause.Occur[] flags = {BooleanClause.Occur.MUST, BooleanClause.Occur.MUST_NOT};
    Query q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, "one", fields, flags, new MockAnalyzer(random()));
    assertEquals("+b:one -t:one", q.toString());

    q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, "one two", fields, flags, new MockAnalyzer(random()));
    assertEquals("+(b:one b:two) -(t:one t:two)", q.toString());

    try {
      BooleanClause.Occur[] flags2 = {BooleanClause.Occur.MUST};
      q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, "blah", fields, flags2, new MockAnalyzer(random()));
      fail();
    } catch(IllegalArgumentException e) {
      // expected exception, array length differs
    }
  }

  public void testStaticMethod2Old() throws ParseException {
    String[] fields = {"b", "t"};
    //int[] flags = {MultiFieldQueryParser.REQUIRED_FIELD, MultiFieldQueryParser.PROHIBITED_FIELD};
      BooleanClause.Occur[] flags = {BooleanClause.Occur.MUST, BooleanClause.Occur.MUST_NOT};

    Query q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, "one", fields, flags, new MockAnalyzer(random()));//, fields, flags, new MockAnalyzer(random));
    assertEquals("+b:one -t:one", q.toString());

    q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, "one two", fields, flags, new MockAnalyzer(random()));
    assertEquals("+(b:one b:two) -(t:one t:two)", q.toString());

    try {
      BooleanClause.Occur[] flags2 = {BooleanClause.Occur.MUST};
      q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, "blah", fields, flags2, new MockAnalyzer(random()));
      fail();
    } catch(IllegalArgumentException e) {
      // expected exception, array length differs
    }
  }

  public void testStaticMethod3() throws ParseException {
    String[] queries = {"one", "two", "three"};
    String[] fields = {"f1", "f2", "f3"};
    BooleanClause.Occur[] flags = {BooleanClause.Occur.MUST,
        BooleanClause.Occur.MUST_NOT, BooleanClause.Occur.SHOULD};
    Query q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, queries, fields, flags, new MockAnalyzer(random()));
    assertEquals("+f1:one -f2:two f3:three", q.toString());

    try {
      BooleanClause.Occur[] flags2 = {BooleanClause.Occur.MUST};
      q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, queries, fields, flags2, new MockAnalyzer(random()));
      fail();
    } catch(IllegalArgumentException e) {
      // expected exception, array length differs
    }
  }

  public void testStaticMethod3Old() throws ParseException {
    String[] queries = {"one", "two"};
    String[] fields = {"b", "t"};
      BooleanClause.Occur[] flags = {BooleanClause.Occur.MUST, BooleanClause.Occur.MUST_NOT};
    Query q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, queries, fields, flags, new MockAnalyzer(random()));
    assertEquals("+b:one -t:two", q.toString());

    try {
      BooleanClause.Occur[] flags2 = {BooleanClause.Occur.MUST};
      q = MultiFieldQueryParser.parse(TEST_VERSION_CURRENT, queries, fields, flags2, new MockAnalyzer(random()));
      fail();
    } catch(IllegalArgumentException e) {
      // expected exception, array length differs
    }
  }

  public void testAnalyzerReturningNull() throws ParseException {
    String[] fields = new String[] { "f1", "f2", "f3" };
    MultiFieldQueryParser parser = new MultiFieldQueryParser(TEST_VERSION_CURRENT, fields, new AnalyzerReturningNull());
    Query q = parser.parse("bla AND blo");
    assertEquals("+(f2:bla f3:bla) +(f2:blo f3:blo)", q.toString());
    // the following queries are not affected as their terms are not analyzed anyway:
    q = parser.parse("bla*");
    assertEquals("f1:bla* f2:bla* f3:bla*", q.toString());
    q = parser.parse("bla~");
    assertEquals("f1:bla~2 f2:bla~2 f3:bla~2", q.toString());
    q = parser.parse("[a TO c]");
    assertEquals("f1:[a TO c] f2:[a TO c] f3:[a TO c]", q.toString());
  }

  public void testStopWordSearching() throws Exception {
    Analyzer analyzer = new MockAnalyzer(random());
    Directory ramDir = newDirectory();
    IndexWriter iw =  new IndexWriter(ramDir, newIndexWriterConfig(TEST_VERSION_CURRENT, analyzer));
    Document doc = new Document();
    doc.add(newTextField("body", "blah the footest blah", Field.Store.NO));
    iw.addDocument(doc);
    iw.close();
    
    MultiFieldQueryParser mfqp = 
      new MultiFieldQueryParser(TEST_VERSION_CURRENT, new String[] {"body"}, analyzer);
    mfqp.setDefaultOperator(QueryParser.Operator.AND);
    Query q = mfqp.parse("the footest");
    IndexReader ir = DirectoryReader.open(ramDir);
    IndexSearcher is = newSearcher(ir);
    ScoreDoc[] hits = is.search(q, null, 1000).scoreDocs;
    assertEquals(1, hits.length);
    ir.close();
    ramDir.close();
  }
  
  /**
   * Return no tokens for field "f1".
   */
  private static class AnalyzerReturningNull extends Analyzer {
    MockAnalyzer stdAnalyzer = new MockAnalyzer(random());

    public AnalyzerReturningNull() {
      super(new PerFieldReuseStrategy());
    }

    @Override
    protected Reader initReader(String fieldName, Reader reader) {
      if ("f1".equals(fieldName)) {
        // we don't use the reader, so close it:
        IOUtils.closeWhileHandlingException(reader);
        // return empty reader, so MockTokenizer returns no tokens:
        return new StringReader("");
      } else {
        return super.initReader(fieldName, reader);
      }
    }

    @Override
    public TokenStreamComponents createComponents(String fieldName, Reader reader) {
      return stdAnalyzer.createComponents(fieldName, reader);
    }
  }
  
  public void testSimpleRegex() throws ParseException {
    String[] fields = new String[] {"a", "b"};
    MultiFieldQueryParser mfqp = new MultiFieldQueryParser(TEST_VERSION_CURRENT, fields, new MockAnalyzer(random()));

    BooleanQuery bq = new BooleanQuery(true);
    bq.add(new RegexpQuery(new Term("a", "[a-z][123]")), Occur.SHOULD);
    bq.add(new RegexpQuery(new Term("b", "[a-z][123]")), Occur.SHOULD);
    assertEquals(bq, mfqp.parse("/[a-z][123]/"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2417.java