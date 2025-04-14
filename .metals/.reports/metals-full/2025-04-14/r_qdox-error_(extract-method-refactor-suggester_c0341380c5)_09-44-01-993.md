error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7075.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7075.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7075.java
text:
```scala
r@@fa.activateOptions();

/*
 * Copyright 1999,2005 The Apache Software Foundation.
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

package org.apache.log4j.rolling;

import junit.framework.TestCase;


import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.filter.LevelRangeFilter;
import org.apache.log4j.joran.JoranConfigurator;
import org.apache.log4j.util.Compare;


/**
 *
 * Tests of rolling file appender with a filter based triggering policy.
 *
 * @author Curt Arnold
 * @since 1.3
 *
 */
public class FilterBasedRollingTest extends TestCase {
  public FilterBasedRollingTest(String name) {
    super(name);
  }

  public void setUp() {
  }

  public void tearDown() {
    LogManager.shutdown();
  }

  /**
   * Test basic rolling functionality using configuration file.
   */
  public void test1() throws Exception {
    JoranConfigurator jc = new JoranConfigurator();
    jc.doConfigure(
      "./input/rolling/filter1.xml", LogManager.getLoggerRepository());
    jc.dumpErrors();

    common("output/filterBased-test1");
  }

  /**
   * Test basic rolling functionality using explicit configuration.
   * @remarks Test fails when run immediately after test1.
   */
  public void xtest2() throws Exception {
    PatternLayout layout = new PatternLayout("%m\n");
    RollingFileAppender rfa = new RollingFileAppender();
    rfa.setName("ROLLING");
    rfa.setLayout(layout);

    FixedWindowRollingPolicy swrp = new FixedWindowRollingPolicy();
    FilterBasedTriggeringPolicy fbtp = new FilterBasedTriggeringPolicy();

    LevelRangeFilter rf = new LevelRangeFilter();
    rf.setLevelMin(Level.INFO);
    fbtp.addFilter(rf);
    fbtp.activateOptions();

    swrp.setMinIndex(0);
    swrp.setActiveFileName("output/filterBased-test2.log");

    swrp.setFileNamePattern("output/filterBased-test2.%i");
    swrp.activateOptions();

    rfa.setRollingPolicy(swrp);
    rfa.setTriggeringPolicy(fbtp);
    rfa.activate();
    Logger.getRootLogger().addAppender(rfa);

    common("output/filterBased-test2");
  }

  /**
   *   Common aspects of test1 and test2
   */
  private void common(String baseName) throws Exception {
    Logger logger = Logger.getLogger(FilterBasedRollingTest.class);

    // Write exactly 10 bytes with each log
    for (int i = 0; i < 25; i++) {
      Thread.sleep(100);

      if (i < 10) {
        logger.debug("Hello---" + i);
      } else if (i < 100) {
        if ((i % 10) == 0) {
          //  on the 10th and 20th request, raise the severity
          logger.warn("Hello--" + i);
        } else {
          logger.debug("Hello--" + i);
        }
      }
    }

    //
    //  test was constructed to mimic SizeBasedRollingTest.test2
    //
    assertTrue(
      Compare.compare(baseName + ".log", "witness/rolling/sbr-test2.log"));
    assertTrue(
      Compare.compare(baseName + ".0", "witness/rolling/sbr-test2.0"));
    assertTrue(
      Compare.compare(baseName + ".1", "witness/rolling/sbr-test2.1"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7075.java