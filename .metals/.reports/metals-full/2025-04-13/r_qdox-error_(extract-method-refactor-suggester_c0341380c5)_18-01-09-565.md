error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7953.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7953.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7953.java
text:
```scala
a@@.activate();

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

package org.apache.log4j.performance;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.joran.JoranConfigurator;


/**
 * Logs in a loop a number of times and measure the elapsed time.
 *
 * @author Ceki G&uuml;lc&uuml;
 */
public class Loop {
  static int runLength;
  static final Logger logger = Logger.getLogger(Loop.class);

  public static void main(String[] args) throws Exception {
    Logger j = Logger.getLogger("org.apache.log4j.joran");
    j.setAdditivity(false);
    j.setLevel(Level.WARN);
    ConsoleAppender a = new ConsoleAppender();
    a.setLayout(new PatternLayout("%d %level %c - %m%n"));
    a.setName("console");
    a.activateOptions();
    j.addAppender(a);

    if (args.length == 2) {
      init(args[0], args[1]);
    } else {
      usage("Wrong number of arguments.");
    }

    //memPrint();
    loop(1000, logger, "Some fix message of medium length.");
    //memPrint();

    long res = loop(runLength, logger, "Some fix message of medium length.");
    double average = (res * 1000.0) / runLength;
    System.out.println(
      "Loop completed in [" + res + "] milliseconds, or [" + average
      + "] microseconds per log.");

    //memPrint();
  }

  static void usage(String msg) {
    System.err.println(msg);
    System.err.println(
      "Usage: java " + Loop.class.getName() + " runLength configFile");
    System.err.println("\trunLength (integer) is the length of test loop.");
    System.err.println("\tconfigFile is an XML configuration file");

    System.exit(1);
  }

  static void memPrint() {
    Runtime runtime = Runtime.getRuntime();
    long total = runtime.totalMemory();
    long free = runtime.freeMemory();
    long used = total - free;
    System.out.println(
      "Total: " + total + ", free: " + free + ", used: " + used);
  }

  static void init(String runLengthStr, String configFile)
    throws Exception {
    runLength = Integer.parseInt(runLengthStr);
    JoranConfigurator jc = new JoranConfigurator();
    jc.doConfigure(configFile, LogManager.getLoggerRepository());
  }

  static long loop(long len, Logger logger, String msg) {
    long before = System.currentTimeMillis();
    for (int i = 0; i < len; i++) {
      logger.debug(msg);
      //if(i == 1000) {
        //memPrint();
      //}
    }
    return (System.currentTimeMillis() - before);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7953.java