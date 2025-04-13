error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1612.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1612.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1612.java
text:
```scala
a@@ssertEquals( "//a b c//<b>d</b> e", sfb.createFragment( reader, 0, F, ffl ) );

package org.apache.lucene.search.vectorhighlight;

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

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.highlight.SimpleHTMLEncoder;

public class SimpleFragmentsBuilderTest extends AbstractTestCase {
  
  public void test1TermIndex() throws Exception {
    FieldFragList ffl = ffl(new TermQuery(new Term(F, "a")), "a" );
    SimpleFragmentsBuilder sfb = new SimpleFragmentsBuilder();
    assertEquals( "<b>a</b>", sfb.createFragment( reader, 0, F, ffl ) );

    // change tags
    sfb = new SimpleFragmentsBuilder( new String[]{ "[" }, new String[]{ "]" } );
    assertEquals( "[a]", sfb.createFragment( reader, 0, F, ffl ) );
  }
  
  public void test2Frags() throws Exception {
    FieldFragList ffl = ffl(new TermQuery(new Term(F, "a")), "a b b b b b b b b b b b a b a b" );
    SimpleFragmentsBuilder sfb = new SimpleFragmentsBuilder();
    String[] f = sfb.createFragments( reader, 0, F, ffl, 3 );
    // 3 snippets requested, but should be 2
    assertEquals( 2, f.length );
    assertEquals( "<b>a</b> b b b b b b b b b b", f[0] );
    assertEquals( "b b <b>a</b> b <b>a</b> b", f[1] );
  }
  
  public void test3Frags() throws Exception {
    BooleanQuery booleanQuery = new BooleanQuery();
    booleanQuery.add(new TermQuery(new Term(F, "a")), BooleanClause.Occur.SHOULD);
    booleanQuery.add(new TermQuery(new Term(F, "c")), BooleanClause.Occur.SHOULD);
    
    FieldFragList ffl = ffl(booleanQuery, "a b b b b b b b b b b b a b a b b b b b c a a b b" );
    SimpleFragmentsBuilder sfb = new SimpleFragmentsBuilder();
    String[] f = sfb.createFragments( reader, 0, F, ffl, 3 );
    assertEquals( 3, f.length );
    assertEquals( "<b>a</b> b b b b b b b b b b", f[0] );
    assertEquals( "b b <b>a</b> b <b>a</b> b b b b b c", f[1] );
    assertEquals( "<b>c</b> <b>a</b> <b>a</b> b b", f[2] );
  }
  
  public void testTagsAndEncoder() throws Exception {
    FieldFragList ffl = ffl(new TermQuery(new Term(F, "a")), "<h1> a </h1>" );
    SimpleFragmentsBuilder sfb = new SimpleFragmentsBuilder();
    String[] preTags = { "[" };
    String[] postTags = { "]" };
    assertEquals( "&lt;h1&gt; [a] &lt;/h1&gt;",
        sfb.createFragment( reader, 0, F, ffl, preTags, postTags, new SimpleHTMLEncoder() ) );
  }

  private FieldFragList ffl(Query query, String indexValue ) throws Exception {
    make1d1fIndex( indexValue );
    FieldQuery fq = new FieldQuery( query, true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    return new SimpleFragListBuilder().createFieldFragList( fpl, 20 );
  }
  
  public void test1PhraseShortMV() throws Exception {
    makeIndexShortMV();

    FieldQuery fq = new FieldQuery( tq( "d" ), true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl, 100 );
    SimpleFragmentsBuilder sfb = new SimpleFragmentsBuilder();
    assertEquals( "a b c  <b>d</b> e", sfb.createFragment( reader, 0, F, ffl ) );
  }
  
  public void test1PhraseLongMV() throws Exception {
    makeIndexLongMV();

    FieldQuery fq = new FieldQuery( pqF( "search", "engines" ), true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl, 100 );
    SimpleFragmentsBuilder sfb = new SimpleFragmentsBuilder();
    assertEquals( "The most <b>search engines</b> use only one of these methods. Even the <b>search engines</b> that says they can use the",
        sfb.createFragment( reader, 0, F, ffl ) );
  }

  public void test1PhraseLongMVB() throws Exception {
    makeIndexLongMVB();

    FieldQuery fq = new FieldQuery( pqF( "sp", "pe", "ee", "ed" ), true, true ); // "speed" -(2gram)-> "sp","pe","ee","ed"
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl, 100 );
    SimpleFragmentsBuilder sfb = new SimpleFragmentsBuilder();
    assertEquals( "processing <b>speed</b>, the", sfb.createFragment( reader, 0, F, ffl ) );
  }
  
  public void testUnstoredField() throws Exception {
    makeUnstoredIndex();

    FieldQuery fq = new FieldQuery( tq( "aaa" ), true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl, 100 );
    SimpleFragmentsBuilder sfb = new SimpleFragmentsBuilder();
    assertNull( sfb.createFragment( reader, 0, F, ffl ) );
  }
  
  protected void makeUnstoredIndex() throws Exception {
    IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(
        TEST_VERSION_CURRENT, analyzerW).setOpenMode(OpenMode.CREATE));
    Document doc = new Document();
    FieldType customType = new FieldType(TextField.TYPE_UNSTORED);
    customType.setStoreTermVectors(true);
    customType.setStoreTermVectorOffsets(true);
    customType.setStoreTermVectorPositions(true);
    doc.add( new Field( F, "aaa", customType) );
    //doc.add( new Field( F, "aaa", Store.NO, Index.ANALYZED, TermVector.WITH_POSITIONS_OFFSETS ) );
    writer.addDocument( doc );
    writer.close();
    if (reader != null) reader.close();
    reader = IndexReader.open(dir);
  }
  
  public void test1StrMV() throws Exception {
    makeIndexStrMV();

    FieldQuery fq = new FieldQuery( tq( "defg" ), true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl, 100 );
    SimpleFragmentsBuilder sfb = new SimpleFragmentsBuilder();
    sfb.setMultiValuedSeparator( '/' );
    assertEquals( "abc/<b>defg</b>/hijkl", sfb.createFragment( reader, 0, F, ffl ) );
  }
  
  public void testMVSeparator() throws Exception {
    makeIndexShortMV();

    FieldQuery fq = new FieldQuery( tq( "d" ), true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl, 100 );
    SimpleFragmentsBuilder sfb = new SimpleFragmentsBuilder();
    sfb.setMultiValuedSeparator( '/' );
    assertEquals( " b c//<b>d</b> e", sfb.createFragment( reader, 0, F, ffl ) );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1612.java