error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/54.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/54.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/54.java
text:
```scala
a@@ssertEquals( "d(1.0)((9,10))", fpl.phraseList.get( 0 ).toString() );

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

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanClause.Occur;

public class FieldPhraseListTest extends AbstractTestCase {
  
  public void test1TermIndex() throws Exception {
    make1d1fIndex( "a" );

    FieldQuery fq = new FieldQuery( tq( "a" ), true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    assertEquals( 1, fpl.phraseList.size() );
    assertEquals( "a(1.0)((0,1))", fpl.phraseList.get( 0 ).toString() );

    fq = new FieldQuery( tq( "b" ), true, true );
    stack = new FieldTermStack( reader, 0, F, fq );
    fpl = new FieldPhraseList( stack, fq );
    assertEquals( 0, fpl.phraseList.size() );
  }
  
  public void test2TermsIndex() throws Exception {
    make1d1fIndex( "a a" );

    FieldQuery fq = new FieldQuery( tq( "a" ), true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    assertEquals( 2, fpl.phraseList.size() );
    assertEquals( "a(1.0)((0,1))", fpl.phraseList.get( 0 ).toString() );
    assertEquals( "a(1.0)((2,3))", fpl.phraseList.get( 1 ).toString() );
  }
  
  public void test1PhraseIndex() throws Exception {
    make1d1fIndex( "a b" );

    FieldQuery fq = new FieldQuery( pqF( "a", "b" ), true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    assertEquals( 1, fpl.phraseList.size() );
    assertEquals( "ab(1.0)((0,3))", fpl.phraseList.get( 0 ).toString() );

    fq = new FieldQuery( tq( "b" ), true, true );
    stack = new FieldTermStack( reader, 0, F, fq );
    fpl = new FieldPhraseList( stack, fq );
    assertEquals( 1, fpl.phraseList.size() );
    assertEquals( "b(1.0)((2,3))", fpl.phraseList.get( 0 ).toString() );
  }
  
  public void test1PhraseIndexB() throws Exception {
    // 01 12 23 34 45 56 67 78 (offsets)
    // bb|bb|ba|ac|cb|ba|ab|bc
    //  0  1  2  3  4  5  6  7 (positions)
    make1d1fIndexB( "bbbacbabc" );

    FieldQuery fq = new FieldQuery( pqF( "ba", "ac" ), true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    assertEquals( 1, fpl.phraseList.size() );
    assertEquals( "baac(1.0)((2,5))", fpl.phraseList.get( 0 ).toString() );
  }
  
  public void test2ConcatTermsIndexB() throws Exception {
    // 01 12 23 (offsets)
    // ab|ba|ab
    //  0  1  2 (positions)
    make1d1fIndexB( "abab" );

    FieldQuery fq = new FieldQuery( tq( "ab" ), true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    assertEquals( 2, fpl.phraseList.size() );
    assertEquals( "ab(1.0)((0,2))", fpl.phraseList.get( 0 ).toString() );
    assertEquals( "ab(1.0)((2,4))", fpl.phraseList.get( 1 ).toString() );
  }
  
  public void test2Terms1PhraseIndex() throws Exception {
    make1d1fIndex( "c a a b" );

    // phraseHighlight = true
    FieldQuery fq = new FieldQuery( pqF( "a", "b" ), true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    assertEquals( 1, fpl.phraseList.size() );
    assertEquals( "ab(1.0)((4,7))", fpl.phraseList.get( 0 ).toString() );

    // phraseHighlight = false
    fq = new FieldQuery( pqF( "a", "b" ), false, true );
    stack = new FieldTermStack( reader, 0, F, fq );
    fpl = new FieldPhraseList( stack, fq );
    assertEquals( 2, fpl.phraseList.size() );
    assertEquals( "a(1.0)((2,3))", fpl.phraseList.get( 0 ).toString() );
    assertEquals( "ab(1.0)((4,7))", fpl.phraseList.get( 1 ).toString() );
  }
  
  public void testPhraseSlop() throws Exception {
    make1d1fIndex( "c a a b c" );

    FieldQuery fq = new FieldQuery( pqF( 2F, 1, "a", "c" ), true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    assertEquals( 1, fpl.phraseList.size() );
    assertEquals( "ac(2.0)((4,5)(8,9))", fpl.phraseList.get( 0 ).toString() );
    assertEquals( 4, fpl.phraseList.get( 0 ).getStartOffset() );
    assertEquals( 9, fpl.phraseList.get( 0 ).getEndOffset() );
  }
  
  public void test2PhrasesOverlap() throws Exception {
    make1d1fIndex( "d a b c d" );

    BooleanQuery query = new BooleanQuery();
    query.add( pqF( "a", "b" ), Occur.SHOULD );
    query.add( pqF( "b", "c" ), Occur.SHOULD );
    FieldQuery fq = new FieldQuery( query, true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    assertEquals( 1, fpl.phraseList.size() );
    assertEquals( "abc(1.0)((2,7))", fpl.phraseList.get( 0 ).toString() );
  }
  
  public void test3TermsPhrase() throws Exception {
    make1d1fIndex( "d a b a b c d" );

    FieldQuery fq = new FieldQuery( pqF( "a", "b", "c" ), true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    assertEquals( 1, fpl.phraseList.size() );
    assertEquals( "abc(1.0)((6,11))", fpl.phraseList.get( 0 ).toString() );
  }
  
  public void testSearchLongestPhrase() throws Exception {
    make1d1fIndex( "d a b d c a b c" );

    BooleanQuery query = new BooleanQuery();
    query.add( pqF( "a", "b" ), Occur.SHOULD );
    query.add( pqF( "a", "b", "c" ), Occur.SHOULD );
    FieldQuery fq = new FieldQuery( query, true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    assertEquals( 2, fpl.phraseList.size() );
    assertEquals( "ab(1.0)((2,5))", fpl.phraseList.get( 0 ).toString() );
    assertEquals( "abc(1.0)((10,15))", fpl.phraseList.get( 1 ).toString() );
  }
  
  public void test1PhraseShortMV() throws Exception {
    makeIndexShortMV();

    FieldQuery fq = new FieldQuery( tq( "d" ), true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    assertEquals( 1, fpl.phraseList.size() );
    assertEquals( "d(1.0)((6,7))", fpl.phraseList.get( 0 ).toString() );
  }
  
  public void test1PhraseLongMV() throws Exception {
    makeIndexLongMV();

    FieldQuery fq = new FieldQuery( pqF( "search", "engines" ), true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    assertEquals( 2, fpl.phraseList.size() );
    assertEquals( "searchengines(1.0)((102,116))", fpl.phraseList.get( 0 ).toString() );
    assertEquals( "searchengines(1.0)((157,171))", fpl.phraseList.get( 1 ).toString() );
  }

  public void test1PhraseLongMVB() throws Exception {
    makeIndexLongMVB();

    FieldQuery fq = new FieldQuery( pqF( "sp", "pe", "ee", "ed" ), true, true ); // "speed" -(2gram)-> "sp","pe","ee","ed"
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    assertEquals( 1, fpl.phraseList.size() );
    assertEquals( "sppeeeed(1.0)((88,93))", fpl.phraseList.get( 0 ).toString() );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/54.java