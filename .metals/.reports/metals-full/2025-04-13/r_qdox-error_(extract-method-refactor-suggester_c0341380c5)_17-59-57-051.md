error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/447.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/447.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/447.java
text:
```scala
private S@@tring runAndReturnSyserr() {

package org.apache.lucene.util.junitcompat;

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

import java.util.Arrays;

import org.apache.lucene.util.LuceneTestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runners.model.Statement;

/**
 * Test reproduce message is right.
 */
public class TestReproduceMessage extends WithNestedTests {
  public static SorePoint where;
  public static SoreType  type;
  
  public static class Nested extends AbstractNestedTest {
    @BeforeClass
    public static void beforeClass() {
      if (isRunningNested()) {
        triggerOn(SorePoint.BEFORE_CLASS);
      }
    }

    @Rule
    public TestRule rule = new TestRule() {
      @Override
      public Statement apply(final Statement base, Description description) {
        return new Statement() {
          public void evaluate() throws Throwable {
            triggerOn(SorePoint.RULE);
            base.evaluate();
          }
        };
      }
    };

    /** Class initializer block/ default constructor. */
    public Nested() {
      triggerOn(SorePoint.INITIALIZER);
    }

    @Before
    public void before() {
      triggerOn(SorePoint.BEFORE);
    }    

    @Test
    public void test() {
      triggerOn(SorePoint.TEST);
    }
    
    @After
    public void after() {
      triggerOn(SorePoint.AFTER);
    }    

    @AfterClass
    public static void afterClass() {
      if (isRunningNested()) {
        triggerOn(SorePoint.AFTER_CLASS);
      }
    }    

    /** */
    private static void triggerOn(SorePoint pt) {
      if (pt == where) {
        switch (type) {
          case ASSUMPTION:
            LuceneTestCase.assumeTrue(pt.toString(), false);
            throw new RuntimeException("unreachable");
          case ERROR:
            throw new RuntimeException(pt.toString());
          case FAILURE:
            Assert.assertTrue(pt.toString(), false);
            throw new RuntimeException("unreachable");
        }
      }
    }
  }

  /*
   * ASSUMPTIONS.
   */
  
  public TestReproduceMessage() {
    super(true);
  }

  @Test
  public void testAssumeBeforeClass() throws Exception { 
    type = SoreType.ASSUMPTION; 
    where = SorePoint.BEFORE_CLASS;
    Assert.assertTrue(runAndReturnSyserr().isEmpty());
  }

  @Test
  public void testAssumeInitializer() throws Exception { 
    type = SoreType.ASSUMPTION; 
    where = SorePoint.INITIALIZER;
    Assert.assertTrue(runAndReturnSyserr().isEmpty());
  }

  @Test
  public void testAssumeRule() throws Exception { 
    type = SoreType.ASSUMPTION; 
    where = SorePoint.RULE;
    Assert.assertEquals("", runAndReturnSyserr());
  }

  @Test
  public void testAssumeBefore() throws Exception { 
    type = SoreType.ASSUMPTION; 
    where = SorePoint.BEFORE;
    Assert.assertTrue(runAndReturnSyserr().isEmpty());
  }

  @Test
  public void testAssumeTest() throws Exception { 
    type = SoreType.ASSUMPTION; 
    where = SorePoint.TEST;
    Assert.assertTrue(runAndReturnSyserr().isEmpty());
  }

  @Test
  public void testAssumeAfter() throws Exception { 
    type = SoreType.ASSUMPTION; 
    where = SorePoint.AFTER;
    Assert.assertTrue(runAndReturnSyserr().isEmpty());
  }

  @Test
  public void testAssumeAfterClass() throws Exception { 
    type = SoreType.ASSUMPTION; 
    where = SorePoint.AFTER_CLASS;
    Assert.assertTrue(runAndReturnSyserr().isEmpty());
  }

  /*
   * FAILURES
   */
  
  @Test
  public void testFailureBeforeClass() throws Exception { 
    type = SoreType.FAILURE; 
    where = SorePoint.BEFORE_CLASS;
    Assert.assertTrue(runAndReturnSyserr().contains("NOTE: reproduce with:"));
  }

  @Test
  public void testFailureInitializer() throws Exception { 
    type = SoreType.FAILURE; 
    where = SorePoint.INITIALIZER;
    Assert.assertTrue(runAndReturnSyserr().contains("NOTE: reproduce with:"));
  }

  @Test
  public void testFailureRule() throws Exception { 
    type = SoreType.FAILURE; 
    where = SorePoint.RULE;

    final String syserr = runAndReturnSyserr();
    
    Assert.assertTrue(syserr.contains("NOTE: reproduce with:"));
    Assert.assertTrue(Arrays.asList(syserr.split("\\s")).contains("-Dtests.method=test"));
    Assert.assertTrue(Arrays.asList(syserr.split("\\s")).contains("-Dtestcase=" + Nested.class.getSimpleName()));
  }

  @Test
  public void testFailureBefore() throws Exception { 
    type = SoreType.FAILURE; 
    where = SorePoint.BEFORE;
    final String syserr = runAndReturnSyserr();
    Assert.assertTrue(syserr.contains("NOTE: reproduce with:"));
    Assert.assertTrue(Arrays.asList(syserr.split("\\s")).contains("-Dtests.method=test"));
    Assert.assertTrue(Arrays.asList(syserr.split("\\s")).contains("-Dtestcase=" + Nested.class.getSimpleName()));
  }

  @Test
  public void testFailureTest() throws Exception { 
    type = SoreType.FAILURE; 
    where = SorePoint.TEST;
    final String syserr = runAndReturnSyserr();
    Assert.assertTrue(syserr.contains("NOTE: reproduce with:"));
    Assert.assertTrue(Arrays.asList(syserr.split("\\s")).contains("-Dtests.method=test"));
    Assert.assertTrue(Arrays.asList(syserr.split("\\s")).contains("-Dtestcase=" + Nested.class.getSimpleName()));
  }

  @Test
  public void testFailureAfter() throws Exception { 
    type = SoreType.FAILURE; 
    where = SorePoint.AFTER;
    final String syserr = runAndReturnSyserr();
    Assert.assertTrue(syserr.contains("NOTE: reproduce with:"));
    Assert.assertTrue(Arrays.asList(syserr.split("\\s")).contains("-Dtests.method=test"));
    Assert.assertTrue(Arrays.asList(syserr.split("\\s")).contains("-Dtestcase=" + Nested.class.getSimpleName()));
  }

  @Test
  public void testFailureAfterClass() throws Exception { 
    type = SoreType.FAILURE; 
    where = SorePoint.AFTER_CLASS;
    Assert.assertTrue(runAndReturnSyserr().contains("NOTE: reproduce with:"));
  }

  /*
   * ERRORS
   */
  
  @Test
  public void testErrorBeforeClass() throws Exception { 
    type = SoreType.ERROR; 
    where = SorePoint.BEFORE_CLASS;
    Assert.assertTrue(runAndReturnSyserr().contains("NOTE: reproduce with:"));
  }

  @Test
  public void testErrorInitializer() throws Exception { 
    type = SoreType.ERROR; 
    where = SorePoint.INITIALIZER;
    Assert.assertTrue(runAndReturnSyserr().contains("NOTE: reproduce with:"));
  }

  @Test
  public void testErrorRule() throws Exception { 
    type = SoreType.ERROR; 
    where = SorePoint.RULE;
    final String syserr = runAndReturnSyserr();
    Assert.assertTrue(syserr.contains("NOTE: reproduce with:"));
    Assert.assertTrue(Arrays.asList(syserr.split("\\s")).contains("-Dtests.method=test"));
    Assert.assertTrue(Arrays.asList(syserr.split("\\s")).contains("-Dtestcase=" + Nested.class.getSimpleName()));
  }

  @Test
  public void testErrorBefore() throws Exception { 
    type = SoreType.ERROR; 
    where = SorePoint.BEFORE;
    final String syserr = runAndReturnSyserr();
    Assert.assertTrue(syserr.contains("NOTE: reproduce with:"));
    Assert.assertTrue(Arrays.asList(syserr.split("\\s")).contains("-Dtests.method=test"));
    Assert.assertTrue(Arrays.asList(syserr.split("\\s")).contains("-Dtestcase=" + Nested.class.getSimpleName()));
  }

  @Test
  public void testErrorTest() throws Exception { 
    type = SoreType.ERROR; 
    where = SorePoint.TEST;
    final String syserr = runAndReturnSyserr();
    Assert.assertTrue(syserr.contains("NOTE: reproduce with:"));
    Assert.assertTrue(Arrays.asList(syserr.split("\\s")).contains("-Dtests.method=test"));
    Assert.assertTrue(Arrays.asList(syserr.split("\\s")).contains("-Dtestcase=" + Nested.class.getSimpleName()));
  }

  @Test
  public void testErrorAfter() throws Exception { 
    type = SoreType.ERROR; 
    where = SorePoint.AFTER;
    final String syserr = runAndReturnSyserr();
    Assert.assertTrue(syserr.contains("NOTE: reproduce with:"));
    Assert.assertTrue(Arrays.asList(syserr.split("\\s")).contains("-Dtests.method=test"));
    Assert.assertTrue(Arrays.asList(syserr.split("\\s")).contains("-Dtestcase=" + Nested.class.getSimpleName()));
  }

  @Test
  public void testErrorAfterClass() throws Exception { 
    type = SoreType.ERROR; 
    where = SorePoint.AFTER_CLASS;
    Assert.assertTrue(runAndReturnSyserr().contains("NOTE: reproduce with:"));
  }

  private String runAndReturnSyserr() throws Exception {
    JUnitCore.runClasses(Nested.class);

    String err = getSysErr();
    // super.prevSysErr.println("Type: " + type + ", point: " + where + " resulted in:\n" + err);
    // super.prevSysErr.println("---");
    return err;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/447.java