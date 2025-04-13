error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/866.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/866.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/866.java
text:
```scala
public v@@oid testIsPatternSafeForCaching() {

/*
 * Copyright 1999,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.log4j.pattern;

import junit.framework.TestCase;


/**
 * @author Ceki Gulcu
 *  */
public class CacheUtilTest extends TestCase {
  
  
  public CacheUtilTest(String arg0) {
    super(arg0);
  }

  protected void setUp() throws Exception {
    super.setUp();
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  public void testRemoveLiteral() {
    String result;
    result = CacheUtil.removeLiterals("a");
    assertEquals("a", result);
    
    result = CacheUtil.removeLiterals("a'a'");
    assertEquals("a", result);
    
    result = CacheUtil.removeLiterals("-+?.a124'a'");
    assertEquals("a", result);

    result = CacheUtil.removeLiterals("ZZZEEE");
    assertEquals("ZZZEEE", result);
  }

  public void testIsPatternSafeForCachingRemoveLiteral() {
    boolean result;
    result = CacheUtil.isPatternSafeForCaching("a");
    assertEquals(true, result);
    
    result = CacheUtil.isPatternSafeForCaching("aS");
    assertEquals(true, result);

    result = CacheUtil.isPatternSafeForCaching("aSS");
    assertEquals(true, result);

    result = CacheUtil.isPatternSafeForCaching("aSSS");
    assertEquals(true, result);

    result = CacheUtil.isPatternSafeForCaching("aSSSS");
    assertEquals(true, result);

    result = CacheUtil.isPatternSafeForCaching("aSaS");
    assertEquals(false, result);
    
    result = CacheUtil.isPatternSafeForCaching("aSSSSSaSSS");
    assertEquals(false, result);
    
    result = CacheUtil.isPatternSafeForCaching("aSaSa");
    assertEquals(false, result);

    result = CacheUtil.isPatternSafeForCaching("aSSaSSa");
    assertEquals(false, result);
    
    result = CacheUtil.isPatternSafeForCaching("aSSSaSSSa");
    assertEquals(false, result);
    


    result = CacheUtil.isPatternSafeForCaching("aEEEE SSS");
    assertEquals(true, result);
    
    result = CacheUtil.isPatternSafeForCaching("aEEEEMMMMM SSS");
    assertEquals(false, result);
  }
  
  public void testComputeSuccessiveS() {
    int result;
    result = CacheUtil.computeSuccessiveS("a");
    assertEquals(0, result);
    
    result = CacheUtil.computeSuccessiveS("aS");
    assertEquals(1, result);

    result = CacheUtil.computeSuccessiveS("aSS");
    assertEquals(2, result);

    result = CacheUtil.computeSuccessiveS("aSSS");
    assertEquals(3, result);
    
    result = CacheUtil.computeSuccessiveS("aSSSS");
    assertEquals(4, result);
    
    result = CacheUtil.computeSuccessiveS("aSxx");
    assertEquals(1, result);

    result = CacheUtil.computeSuccessiveS("aSSxx");
    assertEquals(2, result);

    result = CacheUtil.computeSuccessiveS("aSSSxx");
    assertEquals(3, result);
    
    result = CacheUtil.computeSuccessiveS("aSSSSxx");
    assertEquals(4, result);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/866.java