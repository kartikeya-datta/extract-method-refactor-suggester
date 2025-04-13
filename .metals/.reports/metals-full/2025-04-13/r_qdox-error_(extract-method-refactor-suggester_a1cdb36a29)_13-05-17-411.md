error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1879.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1879.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1879.java
text:
```scala
a@@ddVmOpt(b, "tests.file.encoding", System.getProperty("file.encoding"));

package org.apache.lucene.util;

import static org.apache.lucene.util.LuceneTestCase.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import com.carrotsearch.randomizedtesting.LifecycleScope;
import com.carrotsearch.randomizedtesting.RandomizedContext;

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

/**
 * A suite listener printing a "reproduce string". This ensures test result
 * events are always captured properly even if exceptions happen at
 * initialization or suite/ hooks level.
 */
public final class RunListenerPrintReproduceInfo extends RunListener {
  /**
   * A list of all test suite classes executed so far in this JVM (ehm, 
   * under this class's classloader).
   */
  private static List<String> testClassesRun = new ArrayList<String>();

  /**
   * The currently executing scope.
   */
  private LifecycleScope scope;

  /** Current test failed. */
  private boolean testFailed;

  /** Suite-level code (initialization, rule, hook) failed. */
  private boolean suiteFailed;

  /** A marker to print full env. diagnostics after the suite. */
  private boolean printDiagnosticsAfterClass;


  @Override
  public void testRunStarted(Description description) throws Exception {
    suiteFailed = false;
    testFailed = false;
    scope = LifecycleScope.SUITE;

    Class<?> targetClass = RandomizedContext.current().getTargetClass();
    testClassesRun.add(targetClass.getSimpleName());
  }

  @Override
  public void testStarted(Description description) throws Exception {
    this.testFailed = false;
    this.scope = LifecycleScope.TEST;
  }

  @Override
  public void testFailure(Failure failure) throws Exception {
    if (scope == LifecycleScope.TEST) {
      testFailed = true;
    } else {
      suiteFailed = true;
    }
    printDiagnosticsAfterClass = true;
  }

  @Override
  public void testFinished(Description description) throws Exception {
    if (testFailed) {
      reportAdditionalFailureInfo(description.getMethodName());
    }
    scope = LifecycleScope.SUITE;
    testFailed = false;
  }

  @Override
  public void testRunFinished(Result result) throws Exception {
    if (printDiagnosticsAfterClass || LuceneTestCase.VERBOSE) {
      RunListenerPrintReproduceInfo.printDebuggingInformation();
    }

    if (suiteFailed) {
      reportAdditionalFailureInfo(null);
    }
  }

  /** print some useful debugging information about the environment */
  private static void printDebuggingInformation() {
    if (classEnvRule != null) {
      System.err.println("NOTE: test params are: codec=" + classEnvRule.codec +
          ", sim=" + classEnvRule.similarity +
          ", locale=" + classEnvRule.locale +
          ", timezone=" + (classEnvRule.timeZone == null ? "(null)" : classEnvRule.timeZone.getID()));
    }
    System.err.println("NOTE: " + System.getProperty("os.name") + " "
        + System.getProperty("os.version") + " "
        + System.getProperty("os.arch") + "/"
        + System.getProperty("java.vendor") + " "
        + System.getProperty("java.version") + " "
        + (Constants.JRE_IS_64BIT ? "(64-bit)" : "(32-bit)") + "/"
        + "cpus=" + Runtime.getRuntime().availableProcessors() + ","
        + "threads=" + Thread.activeCount() + ","
        + "free=" + Runtime.getRuntime().freeMemory() + ","
        + "total=" + Runtime.getRuntime().totalMemory());
    System.err.println("NOTE: All tests run in this JVM: " + Arrays.toString(testClassesRun.toArray()));
  }

  private void reportAdditionalFailureInfo(final String testName) {
    if (TEST_LINE_DOCS_FILE.endsWith(JENKINS_LARGE_LINE_DOCS_FILE)) {
      System.err.println("NOTE: download the large Jenkins line-docs file by running " +
      		"'ant get-jenkins-line-docs' in the lucene directory.");
    }

    final StringBuilder b = new StringBuilder();
    b.append("NOTE: reproduce with: ant test ");

    // Test case, method, seed.
    addVmOpt(b, "testcase", RandomizedContext.current().getTargetClass().getSimpleName());
    addVmOpt(b, "tests.method", testName);
    addVmOpt(b, "tests.seed", RandomizedContext.current().getRunnerSeedAsString());

    // Test groups and multipliers.
    if (RANDOM_MULTIPLIER > 1) addVmOpt(b, "tests.multiplier", RANDOM_MULTIPLIER);
    if (TEST_NIGHTLY) addVmOpt(b, SYSPROP_NIGHTLY, TEST_NIGHTLY);
    if (TEST_WEEKLY) addVmOpt(b, SYSPROP_WEEKLY, TEST_WEEKLY);
    if (TEST_SLOW) addVmOpt(b, SYSPROP_SLOW, TEST_SLOW);
    if (TEST_AWAITSFIX) addVmOpt(b, SYSPROP_AWAITSFIX, TEST_AWAITSFIX);

    // Codec, postings, directories.
    if (!TEST_CODEC.equals("random")) addVmOpt(b, "tests.codec", TEST_CODEC);
    if (!TEST_POSTINGSFORMAT.equals("random")) addVmOpt(b, "tests.postingsformat", TEST_POSTINGSFORMAT);
    if (!TEST_DIRECTORY.equals("random")) addVmOpt(b, "tests.directory", TEST_DIRECTORY);

    // Environment.
    if (!TEST_LINE_DOCS_FILE.equals(DEFAULT_LINE_DOCS_FILE)) addVmOpt(b, "tests.linedocsfile", TEST_LINE_DOCS_FILE);
    if (classEnvRule != null) {
      addVmOpt(b, "tests.locale", classEnvRule.locale);
      if (classEnvRule.timeZone != null) {
        addVmOpt(b, "tests.timezone", classEnvRule.timeZone.getID());
      }
    }

    addVmOpt(b, "randomized.file.encoding", System.getProperty("file.encoding"));

    System.err.println(b.toString());
  }

  /**
   * Append a VM option (-Dkey=value) to a {@link StringBuilder}. Add quotes if 
   * spaces or other funky characters are detected.
   */
  static void addVmOpt(StringBuilder b, String key, Object value) {
    if (value == null) return;

    b.append(" -D").append(key).append("=");
    String v = value.toString();
    // Add simplistic quoting. This varies a lot from system to system and between
    // shells... ANT should have some code for doing it properly.
    if (Pattern.compile("[\\s=']").matcher(v).find()) {
      v = '"' + v + '"';
    }
    b.append(v);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1879.java