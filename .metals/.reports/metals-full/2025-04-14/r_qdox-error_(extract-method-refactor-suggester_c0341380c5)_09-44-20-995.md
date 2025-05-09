error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1621.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1621.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1621.java
text:
```scala
r@@eturn new Leaf(Double.NaN);

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

package org.apache.mahout.classifier.df.mapreduce.partial;

import static org.easymock.EasyMock.*;
import java.util.Random;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.classifier.df.builder.TreeBuilder;
import org.apache.mahout.classifier.df.data.Data;
import org.apache.mahout.classifier.df.data.DataLoader;
import org.apache.mahout.classifier.df.data.Dataset;
import org.apache.mahout.classifier.df.data.Utils;
import org.apache.mahout.classifier.df.node.Leaf;
import org.apache.mahout.classifier.df.node.Node;
import org.apache.mahout.common.MahoutTestCase;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.junit.Test;

public final class Step1MapperTest extends MahoutTestCase {

  /**
   * Make sure that the data used to build the trees is from the mapper's
   * partition
   * 
   */
  private static class MockTreeBuilder implements TreeBuilder {

    private Data expected;

    public void setExpected(Data data) {
      expected = data;
    }

    @Override
    public Node build(Random rng, Data data) {
      for (int index = 0; index < data.size(); index++) {
        assertTrue(expected.contains(data.get(index)));
      }

      return new Leaf(-1);
    }
  }

  /**
   * Special Step1Mapper that can be configured without using a Configuration
   * 
   */
  private static class MockStep1Mapper extends Step1Mapper {
    private MockStep1Mapper(TreeBuilder treeBuilder, Dataset dataset, Long seed,
        int partition, int numMapTasks, int numTrees) {
      configure(false, treeBuilder, dataset);
      configure(seed, partition, numMapTasks, numTrees);
    }
  }

  private static class TreeIDCapture extends Capture<TreeID> {

    private TreeIDCapture() {
      super(CaptureType.ALL);
    }

    @Override
    public void setValue(final TreeID value) {
      super.setValue(value.clone());
    }
  }

  /** nb attributes per generated data instance */
  static final int NUM_ATTRIBUTES = 4;

  /** nb generated data instances */
  static final int NUM_INSTANCES = 100;

  /** nb trees to build */
  static final int NUM_TREES = 10;

  /** nb mappers to use */
  static final int NUM_MAPPERS = 2;

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Test
  public void testMapper() throws Exception {
    Long seed = null;
    Random rng = RandomUtils.getRandom();

    // prepare the data
    String descriptor = Utils.randomDescriptor(rng, NUM_ATTRIBUTES);
    double[][] source = Utils.randomDoubles(rng, descriptor, false, NUM_INSTANCES);
    String[] sData = Utils.double2String(source);
    Dataset dataset = DataLoader.generateDataset(descriptor, false, sData);
    String[][] splits = Utils.splitData(sData, NUM_MAPPERS);

    MockTreeBuilder treeBuilder = new MockTreeBuilder();

    LongWritable key = new LongWritable();
    Text value = new Text();

    int treeIndex = 0;

    for (int partition = 0; partition < NUM_MAPPERS; partition++) {
      String[] split = splits[partition];
      treeBuilder.setExpected(DataLoader.loadData(dataset, split));

      // expected number of trees that this mapper will build
      int mapNbTrees = Step1Mapper.nbTrees(NUM_MAPPERS, NUM_TREES, partition);

      Mapper.Context context =
        createMock(Mapper.Context.class);
      Capture<TreeID> capturedKeys = new TreeIDCapture();
      context.write(capture(capturedKeys), anyObject());
      expectLastCall().anyTimes();

      replay(context);

      MockStep1Mapper mapper = new MockStep1Mapper(treeBuilder, dataset, seed,
          partition, NUM_MAPPERS, NUM_TREES);

      // make sure the mapper computed firstTreeId correctly
      assertEquals(treeIndex, mapper.getFirstTreeId());

      for (int index = 0; index < split.length; index++) {
        key.set(index);
        value.set(split[index]);
        mapper.map(key, value, context);
      }

      mapper.cleanup(context);
      verify(context);

      // make sure the mapper built all its trees
      assertEquals(mapNbTrees, capturedKeys.getValues().size());

      // check the returned keys
      for (TreeID k : capturedKeys.getValues()) {
        assertEquals(partition, k.partition());
        assertEquals(treeIndex, k.treeId());

        treeIndex++;
      }
    }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1621.java