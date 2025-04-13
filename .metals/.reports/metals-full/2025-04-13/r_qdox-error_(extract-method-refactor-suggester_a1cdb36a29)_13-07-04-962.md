error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2772.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2772.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2772.java
text:
```scala
t@@hrow new IllegalStateException("Benchmark was already executed");

package org.apache.lucene.benchmark.byTask;

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

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import org.apache.lucene.benchmark.byTask.utils.Algorithm;
import org.apache.lucene.benchmark.byTask.utils.Config;


/**
 * Run the benchmark algorithm.
 * <p>Usage: java Benchmark  algorithm-file
 * <ol>
 * <li>Read algorithm.</li>
 * <li> Run the algorithm.</li>
 * </ol>
 * Things to be added/fixed in "Benchmarking by tasks":
 * <ol>
 * <li>TODO - report into Excel and/or graphed view.</li>
 * <li>TODO - perf comparison between Lucene releases over the years.</li>
 * <li>TODO - perf report adequate to include in Lucene nightly build site? (so we can easily track performance changes.)</li>
 * <li>TODO - add overall time control for repeated execution (vs. current by-count only).</li>
 * <li>TODO - query maker that is based on index statistics.</li>
 * </ol>
 */
public class Benchmark {

  private PerfRunData runData;
  private Algorithm algorithm;
  private boolean executed;
  
  public Benchmark (Reader algReader) throws Exception {
    // prepare run data
    try {
      runData = new PerfRunData(new Config(algReader));
    } catch (Exception e) {
      throw new Exception("Error: cannot init PerfRunData!",e);
    }
    
    // parse algorithm
    try {
      algorithm = new Algorithm(runData);
    } catch (Exception e) {
      throw new Exception("Error: cannot understand algorithm!",e);
    }
  }
  
  public synchronized void  execute() throws Exception {
    if (executed) {
      throw new Exception("Benchmark was already executed");
    }
    executed = true;
    algorithm.execute();
  }
  
  /**
   * Run the benchmark algorithm.
   * @param args benchmark config and algorithm files
   */
  public static void main(String[] args) {
    // verify command line args
    if (args.length < 1) {
      System.err.println("Usage: java Benchmark <algorithm file>");
      System.exit(1);
    }
    
    // verify input files 
    File algFile = new File(args[0]);
    if (!algFile.exists() || !algFile.isFile() || !algFile.canRead()) {
      System.err.println("cannot find/read algorithm file: "+algFile.getAbsolutePath()); 
      System.exit(1);
    }
    
    System.out.println("Running algorithm from: "+algFile.getAbsolutePath());
    
    Benchmark benchmark = null;
    try {
      benchmark = new Benchmark(new FileReader(algFile));
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }

    System.out.println("------------> algorithm:");
    System.out.println(benchmark.getAlgorithm().toString());

    // execute
    try {
      benchmark.execute();
    } catch (Exception e) {
      System.err.println("Error: cannot execute the algorithm! "+e.getMessage());
      e.printStackTrace();
    }

    System.out.println("####################");
    System.out.println("###  D O N E !!! ###");
    System.out.println("####################");

  }

  /**
   * @return Returns the algorithm.
   */
  public Algorithm getAlgorithm() {
    return algorithm;
  }

  /**
   * @return Returns the runData.
   */
  public PerfRunData getRunData() {
    return runData;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2772.java