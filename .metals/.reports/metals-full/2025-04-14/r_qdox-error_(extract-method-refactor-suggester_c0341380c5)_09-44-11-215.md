error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/52.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/52.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/52.java
text:
```scala
a@@ssertEquals( "subInfos=(d((9,10)))/1.0(3,103)", ffl.fragInfos.get( 0 ).toString() );

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

import org.apache.lucene.search.Query;

public class SimpleFragListBuilderTest extends AbstractTestCase {
  
  public void testNullFieldFragList() throws Exception {
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl( "a", "b c d" ), 100 );
    assertEquals( 0, ffl.fragInfos.size() );
  }
  
  public void testTooSmallFragSize() throws Exception {
    try{
      SimpleFragListBuilder sflb = new SimpleFragListBuilder();
      sflb.createFieldFragList( fpl( "a", "b c d" ), SimpleFragListBuilder.MIN_FRAG_CHAR_SIZE - 1 );
      fail( "IllegalArgumentException must be thrown" );
    }
    catch ( IllegalArgumentException expected ) {
    }
  }
  
  public void testSmallerFragSizeThanTermQuery() throws Exception {
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl( "abcdefghijklmnopqrs", "abcdefghijklmnopqrs" ), SimpleFragListBuilder.MIN_FRAG_CHAR_SIZE );
    assertEquals( 1, ffl.fragInfos.size() );
    assertEquals( "subInfos=(abcdefghijklmnopqrs((0,19)))/1.0(0,19)", ffl.fragInfos.get( 0 ).toString() );
  }
  
  public void testSmallerFragSizeThanPhraseQuery() throws Exception {
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl( "\"abcdefgh jklmnopqrs\"", "abcdefgh   jklmnopqrs" ), SimpleFragListBuilder.MIN_FRAG_CHAR_SIZE );
    assertEquals( 1, ffl.fragInfos.size() );
    if (VERBOSE) System.out.println( ffl.fragInfos.get( 0 ).toString() );
    assertEquals( "subInfos=(abcdefghjklmnopqrs((0,21)))/1.0(0,21)", ffl.fragInfos.get( 0 ).toString() );
  }
  
  public void test1TermIndex() throws Exception {
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl( "a", "a" ), 100 );
    assertEquals( 1, ffl.fragInfos.size() );
    assertEquals( "subInfos=(a((0,1)))/1.0(0,100)", ffl.fragInfos.get( 0 ).toString() );
  }
  
  public void test2TermsIndex1Frag() throws Exception {
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl( "a", "a a" ), 100 );
    assertEquals( 1, ffl.fragInfos.size() );
    assertEquals( "subInfos=(a((0,1))a((2,3)))/2.0(0,100)", ffl.fragInfos.get( 0 ).toString() );
  
    ffl = sflb.createFieldFragList( fpl( "a", "a b b b b b b b b a" ), 20 );
    assertEquals( 1, ffl.fragInfos.size() );
    assertEquals( "subInfos=(a((0,1))a((18,19)))/2.0(0,20)", ffl.fragInfos.get( 0 ).toString() );

    ffl = sflb.createFieldFragList( fpl( "a", "b b b b a b b b b a" ), 20 );
    assertEquals( 1, ffl.fragInfos.size() );
    assertEquals( "subInfos=(a((8,9))a((18,19)))/2.0(2,22)", ffl.fragInfos.get( 0 ).toString() );
  }
  
  public void test2TermsIndex2Frags() throws Exception {
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl( "a", "a b b b b b b b b b b b b b a" ), 20 );
    assertEquals( 2, ffl.fragInfos.size() );
    assertEquals( "subInfos=(a((0,1)))/1.0(0,20)", ffl.fragInfos.get( 0 ).toString() );
    assertEquals( "subInfos=(a((28,29)))/1.0(22,42)", ffl.fragInfos.get( 1 ).toString() );

    ffl = sflb.createFieldFragList( fpl( "a", "a b b b b b b b b b b b b a" ), 20 );
    assertEquals( 2, ffl.fragInfos.size() );
    assertEquals( "subInfos=(a((0,1)))/1.0(0,20)", ffl.fragInfos.get( 0 ).toString() );
    assertEquals( "subInfos=(a((26,27)))/1.0(20,40)", ffl.fragInfos.get( 1 ).toString() );

    ffl = sflb.createFieldFragList( fpl( "a", "a b b b b b b b b b a" ), 20 );
    assertEquals( 2, ffl.fragInfos.size() );
    assertEquals( "subInfos=(a((0,1)))/1.0(0,20)", ffl.fragInfos.get( 0 ).toString() );
    assertEquals( "subInfos=(a((20,21)))/1.0(20,40)", ffl.fragInfos.get( 1 ).toString() );
  }
  
  public void test2TermsQuery() throws Exception {
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl( "a b", "c d e" ), 20 );
    assertEquals( 0, ffl.fragInfos.size() );

    ffl = sflb.createFieldFragList( fpl( "a b", "d b c" ), 20 );
    assertEquals( 1, ffl.fragInfos.size() );
    assertEquals( "subInfos=(b((2,3)))/1.0(0,20)", ffl.fragInfos.get( 0 ).toString() );

    ffl = sflb.createFieldFragList( fpl( "a b", "a b c" ), 20 );
    assertEquals( 1, ffl.fragInfos.size() );
    assertEquals( "subInfos=(a((0,1))b((2,3)))/2.0(0,20)", ffl.fragInfos.get( 0 ).toString() );
  }
  
  public void testPhraseQuery() throws Exception {
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl( "\"a b\"", "c d e" ), 20 );
    assertEquals( 0, ffl.fragInfos.size() );

    ffl = sflb.createFieldFragList( fpl( "\"a b\"", "a c b" ), 20 );
    assertEquals( 0, ffl.fragInfos.size() );

    ffl = sflb.createFieldFragList( fpl( "\"a b\"", "a b c" ), 20 );
    assertEquals( 1, ffl.fragInfos.size() );
    assertEquals( "subInfos=(ab((0,3)))/1.0(0,20)", ffl.fragInfos.get( 0 ).toString() );
  }
  
  public void testPhraseQuerySlop() throws Exception {
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl( "\"a b\"~1", "a c b" ), 20 );
    assertEquals( 1, ffl.fragInfos.size() );
    assertEquals( "subInfos=(ab((0,1)(4,5)))/1.0(0,20)", ffl.fragInfos.get( 0 ).toString() );
  }

  private FieldPhraseList fpl( String queryValue, String indexValue ) throws Exception {
    make1d1fIndex( indexValue );
    Query query = paW.parse( queryValue );
    FieldQuery fq = new FieldQuery( query, true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    return new FieldPhraseList( stack, fq );
  }
  
  public void test1PhraseShortMV() throws Exception {
    makeIndexShortMV();

    FieldQuery fq = new FieldQuery( tq( "d" ), true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl, 100 );
    assertEquals( 1, ffl.fragInfos.size() );
    assertEquals( "subInfos=(d((6,7)))/1.0(0,100)", ffl.fragInfos.get( 0 ).toString() );
  }
  
  public void test1PhraseLongMV() throws Exception {
    makeIndexLongMV();

    FieldQuery fq = new FieldQuery( pqF( "search", "engines" ), true, true );
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl, 100 );
    assertEquals( 1, ffl.fragInfos.size() );
    assertEquals( "subInfos=(searchengines((102,116))searchengines((157,171)))/2.0(96,196)", ffl.fragInfos.get( 0 ).toString() );
  }

  public void test1PhraseLongMVB() throws Exception {
    makeIndexLongMVB();

    FieldQuery fq = new FieldQuery( pqF( "sp", "pe", "ee", "ed" ), true, true ); // "speed" -(2gram)-> "sp","pe","ee","ed"
    FieldTermStack stack = new FieldTermStack( reader, 0, F, fq );
    FieldPhraseList fpl = new FieldPhraseList( stack, fq );
    SimpleFragListBuilder sflb = new SimpleFragListBuilder();
    FieldFragList ffl = sflb.createFieldFragList( fpl, 100 );
    assertEquals( 1, ffl.fragInfos.size() );
    assertEquals( "subInfos=(sppeeeed((88,93)))/1.0(82,182)", ffl.fragInfos.get( 0 ).toString() );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/52.java