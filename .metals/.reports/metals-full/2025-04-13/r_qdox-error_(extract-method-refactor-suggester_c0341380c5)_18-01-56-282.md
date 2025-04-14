error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3430.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3430.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3430.java
text:
```scala
S@@ingleFieldTestDb db1 = new SingleFieldTestDb(random, docs1, fieldName);

package org.apache.lucene.queryParser.surround.query;

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

import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.lucene.util.LuceneTestCase;

public class Test02Boolean extends LuceneTestCase {
  public static void main(String args[]) {
    TestRunner.run(new TestSuite(Test02Boolean.class));
  }

  final String fieldName = "bi";
  boolean verbose = false;
  int maxBasicQueries = 16;

  String[] docs1 = {
    "word1 word2 word3",
    "word4 word5",
    "ord1 ord2 ord3",
    "orda1 orda2 orda3 word2 worda3",
    "a c e a b c"
  };

  SingleFieldTestDb db1 = new SingleFieldTestDb(docs1, fieldName);

  public void normalTest1(String query, int[] expdnrs) throws Exception {
    BooleanQueryTst bqt = new BooleanQueryTst( query, expdnrs, db1, fieldName, this,
                                                new BasicQueryFactory(maxBasicQueries));
    bqt.setVerbose(verbose);
    bqt.doTest();
  }

  public void test02Terms01() throws Exception {
    int[] expdnrs = {0}; normalTest1( "word1", expdnrs);
  }
  public void test02Terms02() throws Exception {
    int[] expdnrs = {0, 1, 3}; normalTest1( "word*", expdnrs);
  }
  public void test02Terms03() throws Exception {
    int[] expdnrs = {2}; normalTest1( "ord2", expdnrs);
  }
  public void test02Terms04() throws Exception {
    int[] expdnrs = {}; normalTest1( "kxork*", expdnrs);
  }
  public void test02Terms05() throws Exception {
    int[] expdnrs = {0, 1, 3}; normalTest1( "wor*", expdnrs);
  }
  public void test02Terms06() throws Exception {
    int[] expdnrs = {}; normalTest1( "ab", expdnrs);
  }
  
  public void test02Terms10() throws Exception {
    int[] expdnrs = {}; normalTest1( "abc?", expdnrs);
  }
  public void test02Terms13() throws Exception {
    int[] expdnrs = {0,1,3}; normalTest1( "word?", expdnrs);
  }
  public void test02Terms14() throws Exception {
    int[] expdnrs = {0,1,3}; normalTest1( "w?rd?", expdnrs);
  }
  public void test02Terms20() throws Exception {
    int[] expdnrs = {0,1,3}; normalTest1( "w*rd?", expdnrs);
  }
  public void test02Terms21() throws Exception {
    int[] expdnrs = {3}; normalTest1( "w*rd??", expdnrs);
  }
  public void test02Terms22() throws Exception {
    int[] expdnrs = {3}; normalTest1( "w*?da?", expdnrs);
  }
  public void test02Terms23() throws Exception {
    int[] expdnrs = {}; normalTest1( "w?da?", expdnrs);
  }
  
  public void test03And01() throws Exception {
    int[] expdnrs = {0}; normalTest1( "word1 AND word2", expdnrs);
  }
  public void test03And02() throws Exception {
    int[] expdnrs = {3}; normalTest1( "word* and ord*", expdnrs);
  }
  public void test03And03() throws Exception {
    int[] expdnrs = {0}; normalTest1( "and(word1,word2)", expdnrs);
  }
  public void test04Or01() throws Exception {
    int[] expdnrs = {0, 3}; normalTest1( "word1 or word2", expdnrs);
  }
  public void test04Or02() throws Exception {
    int[] expdnrs = {0, 1, 2, 3}; normalTest1( "word* OR ord*", expdnrs);
  }
  public void test04Or03() throws Exception {
    int[] expdnrs = {0, 3}; normalTest1( "OR (word1, word2)", expdnrs);
  }
  public void test05Not01() throws Exception {
    int[] expdnrs = {3}; normalTest1( "word2 NOT word1", expdnrs);
  }
  public void test05Not02() throws Exception {
    int[] expdnrs = {0}; normalTest1( "word2* not ord*", expdnrs);
  }
  public void test06AndOr01() throws Exception {
    int[] expdnrs = {0}; normalTest1( "(word1 or ab)and or(word2,xyz, defg)", expdnrs);
  }
  public void test07AndOrNot02() throws Exception {
    int[] expdnrs = {0}; normalTest1( "or( word2* not ord*, and(xyz,def))", expdnrs);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3430.java