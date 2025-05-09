error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1603.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1603.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1603.java
text:
```scala
c@@onf = getConfiguration();

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

package org.apache.mahout.classifier.naivebayes;

import com.google.common.io.Closeables;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.classifier.AbstractVectorClassifier;
import org.apache.mahout.classifier.naivebayes.training.TrainNaiveBayesJob;
import org.apache.mahout.common.MahoutTestCase;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.math.hadoop.MathHelper;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class NaiveBayesTest extends MahoutTestCase {

  private Configuration conf;
  private File inputFile;
  private File outputDir;
  private File tempDir;

  static final Text LABEL_STOLEN = new Text("/stolen/");
  static final Text LABEL_NOT_STOLEN = new Text("/not_stolen/");

  static final Vector.Element COLOR_RED = MathHelper.elem(0, 1);
  static final Vector.Element COLOR_YELLOW = MathHelper.elem(1, 1);
  static final Vector.Element TYPE_SPORTS = MathHelper.elem(2, 1);
  static final Vector.Element TYPE_SUV = MathHelper.elem(3, 1);
  static final Vector.Element ORIGIN_DOMESTIC = MathHelper.elem(4, 1);
  static final Vector.Element ORIGIN_IMPORTED = MathHelper.elem(5, 1);


  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    conf = new Configuration();

    inputFile = getTestTempFile("trainingInstances.seq");
    outputDir = getTestTempDir("output");
    outputDir.delete();
    tempDir = getTestTempDir("tmp");

    SequenceFile.Writer writer = new SequenceFile.Writer(FileSystem.get(conf), conf,
        new Path(inputFile.getAbsolutePath()), Text.class, VectorWritable.class);

    try {
      writer.append(LABEL_STOLEN, trainingInstance(COLOR_RED, TYPE_SPORTS, ORIGIN_DOMESTIC));
      writer.append(LABEL_NOT_STOLEN, trainingInstance(COLOR_RED, TYPE_SPORTS, ORIGIN_DOMESTIC));
      writer.append(LABEL_STOLEN, trainingInstance(COLOR_RED, TYPE_SPORTS, ORIGIN_DOMESTIC));
      writer.append(LABEL_NOT_STOLEN, trainingInstance(COLOR_YELLOW, TYPE_SPORTS, ORIGIN_DOMESTIC));
      writer.append(LABEL_STOLEN, trainingInstance(COLOR_YELLOW, TYPE_SPORTS, ORIGIN_IMPORTED));
      writer.append(LABEL_NOT_STOLEN, trainingInstance(COLOR_YELLOW, TYPE_SUV, ORIGIN_IMPORTED));
      writer.append(LABEL_STOLEN, trainingInstance(COLOR_YELLOW, TYPE_SUV, ORIGIN_IMPORTED));
      writer.append(LABEL_NOT_STOLEN, trainingInstance(COLOR_YELLOW, TYPE_SUV, ORIGIN_DOMESTIC));
      writer.append(LABEL_NOT_STOLEN, trainingInstance(COLOR_RED, TYPE_SUV, ORIGIN_IMPORTED));
      writer.append(LABEL_STOLEN, trainingInstance(COLOR_RED, TYPE_SPORTS, ORIGIN_IMPORTED));
    } finally {
      Closeables.closeQuietly(writer);
    }
  }

  @Test
  public void toyData() throws Exception {
    TrainNaiveBayesJob trainNaiveBayes = new TrainNaiveBayesJob();
    trainNaiveBayes.setConf(conf);
    trainNaiveBayes.run(new String[] { "--input", inputFile.getAbsolutePath(), "--output", outputDir.getAbsolutePath(),
        "-el", "--tempDir", tempDir.getAbsolutePath() });

    NaiveBayesModel naiveBayesModel = NaiveBayesModel.materialize(new Path(outputDir.getAbsolutePath()), conf);

    AbstractVectorClassifier classifier = new StandardNaiveBayesClassifier(naiveBayesModel);

    assertEquals(2, classifier.numCategories());

    Vector prediction = classifier.classifyFull(trainingInstance(COLOR_RED, TYPE_SUV, ORIGIN_DOMESTIC).get());

    // should be classified as not stolen
    assertTrue(prediction.get(0) < prediction.get(1));
  }

  @Test
  public void toyDataComplementary() throws Exception {
    TrainNaiveBayesJob trainNaiveBayes = new TrainNaiveBayesJob();
    trainNaiveBayes.setConf(conf);
    trainNaiveBayes.run(new String[] { "--input", inputFile.getAbsolutePath(), "--output", outputDir.getAbsolutePath(),
        "-el", "--trainComplementary",
        "--tempDir", tempDir.getAbsolutePath() });

    NaiveBayesModel naiveBayesModel = NaiveBayesModel.materialize(new Path(outputDir.getAbsolutePath()), conf);

    AbstractVectorClassifier classifier = new ComplementaryNaiveBayesClassifier(naiveBayesModel);

    assertEquals(2, classifier.numCategories());

    Vector prediction = classifier.classifyFull(trainingInstance(COLOR_RED, TYPE_SUV, ORIGIN_DOMESTIC).get());

    // should be classified as not stolen
    assertTrue(prediction.get(0) < prediction.get(1));
  }

  static VectorWritable trainingInstance(Vector.Element... elems) {
    DenseVector trainingInstance = new DenseVector(6);
    for (Vector.Element elem : elems) {
      trainingInstance.set(elem.index(), elem.get());
    }
    return new VectorWritable(trainingInstance);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1603.java