error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1604.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1604.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1604.java
text:
```scala
c@@onf = getConfiguration();

package org.apache.mahout.math.hadoop.stats;
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


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.common.MahoutTestCase;
import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.math.jet.random.Normal;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public final class BasicStatsTest extends MahoutTestCase {

  private Configuration conf;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    conf = new Configuration();
  }

  @Test
  public void testVar() throws Exception {
    Path input = getTestTempFilePath("stdDev/counts.file");
    Path output = getTestTempFilePath("stdDev/output.file");
    
    produceTestData(input);
    
    double v = BasicStats.variance(input, output, conf);
    assertEquals(2.44, v, 0.01);
  }


  @Test
  public void testStdDev() throws Exception {
    Path input = getTestTempFilePath("stdDev/counts.file");
    Path output = getTestTempFilePath("stdDev/output.file");
    
    produceTestData(input);
    
    double v = BasicStats.stdDev(input, output, conf);
    assertEquals(1.56, v, 0.01); //sample std dev is 1.563, std. dev from a discrete set is 1.48

  }
  
  @Test
  public void testStdDevForGivenMean() throws Exception {
    Path input = getTestTempFilePath("stdDev/counts.file");
    Path output = getTestTempFilePath("stdDev/output.file");
    
    produceTestData(input);
    
    double v = BasicStats.stdDevForGivenMean(input, output, 0.0D, conf);
    assertEquals(10.65, v, 0.01); //sample std dev is 10.65

  }
  
  private void produceTestData(Path input) throws Exception {
	  FileSystem fs = FileSystem.get(input.toUri(), conf);
    SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf, input, IntWritable.class, DoubleWritable.class);
    //Random random = new MersenneTwisterRNG();
    /*Normal normal = new Normal(5, 3, random);
    for (int i = 0; i < 10000; i++) {
      writer.append(new IntWritable(i), new DoubleWritable((long)normal.nextDouble()));
    }*/
    int i = 0;
    writer.append(new IntWritable(i++), new DoubleWritable(7));
    writer.append(new IntWritable(i++), new DoubleWritable(9));
    writer.append(new IntWritable(i++), new DoubleWritable(9));
    writer.append(new IntWritable(i++), new DoubleWritable(10));
    writer.append(new IntWritable(i++), new DoubleWritable(10));
    writer.append(new IntWritable(i++), new DoubleWritable(10));
    writer.append(new IntWritable(i++), new DoubleWritable(10));
    writer.append(new IntWritable(i++), new DoubleWritable(11));
    writer.append(new IntWritable(i++), new DoubleWritable(11));
    writer.append(new IntWritable(i++), new DoubleWritable(13));
    writer.close();
  }

  //Not entirely sure on this test
  @Test
  public void testStdDev2() throws Exception {
    Path input = getTestTempFilePath("stdDev/counts.file");
    Path output = getTestTempFilePath("stdDev/output.file");
    FileSystem fs = FileSystem.get(input.toUri(), conf);
    SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf, input, IntWritable.class,
            DoubleWritable.class);
    Random random = RandomUtils.getRandom();
    Normal normal = new Normal(5, 3, random);
    for (int i = 0; i < 1000000; i++) {
      writer.append(new IntWritable(i), new DoubleWritable((long) normal.nextInt()));
    }
    writer.close();
    double v = BasicStats.stdDev(input, output, conf);
    assertEquals(3, v, 0.02);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1604.java