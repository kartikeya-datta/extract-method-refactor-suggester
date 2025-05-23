error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1404.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1404.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1404.java
text:
```scala
c@@onf = getConfiguration();

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

package org.apache.mahout.cf.taste.hadoop.als;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.cf.taste.hadoop.TasteHadoopUtils;
import org.apache.mahout.cf.taste.impl.TasteTestCase;
import org.apache.mahout.cf.taste.impl.common.FullRunningAverage;
import org.apache.mahout.cf.taste.impl.common.RunningAverage;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.MatrixSlice;
import org.apache.mahout.math.SparseRowMatrix;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.Vector.Element;
import org.apache.mahout.math.hadoop.MathHelper;
import org.apache.mahout.math.map.OpenIntLongHashMap;
import org.apache.mahout.math.map.OpenIntObjectHashMap;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ParallelALSFactorizationJobTest extends TasteTestCase {

  private static final Logger log = LoggerFactory.getLogger(ParallelALSFactorizationJobTest.class);

  private File inputFile;
  private File intermediateDir;
  private File outputDir;
  private File tmpDir;
  private Configuration conf;

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    inputFile = getTestTempFile("prefs.txt");
    intermediateDir = getTestTempDir("intermediate");
    intermediateDir.delete();
    outputDir = getTestTempDir("output");
    outputDir.delete();
    tmpDir = getTestTempDir("tmp");

    conf = new Configuration();
    // reset as we run all tests in the same JVM
    SharingMapper.reset();
  }

  @Test
  public void completeJobToyExample() throws Exception {
    explicitExample(1);
  }

  @Test
  public void completeJobToyExampleMultithreaded() throws Exception {
    explicitExample(2);
  }

  /**
   * small integration test that runs the full job
   *
   * <pre>
   *
   *  user-item-matrix
   *
   *          burger  hotdog  berries  icecream
   *  dog       5       5        2        -
   *  rabbit    2       -        3        5
   *  cow       -       5        -        3
   *  donkey    3       -        -        5
   *
   * </pre>
   */
  private void explicitExample(int numThreads) throws Exception {

    Double na = Double.NaN;
    Matrix preferences = new SparseRowMatrix(4, 4, new Vector[] {
        new DenseVector(new double[] { 5.0, 5.0, 2.0, na }),
        new DenseVector(new double[] { 2.0, na,  3.0, 5.0 }),
        new DenseVector(new double[] { na,  5.0, na,  3.0 }),
        new DenseVector(new double[] { 3.0, na,  na,  5.0 }) });

    writeLines(inputFile, preferencesAsText(preferences));

    ParallelALSFactorizationJob alsFactorization = new ParallelALSFactorizationJob();
    alsFactorization.setConf(conf);

    int numFeatures = 3;
    int numIterations = 5;
    double lambda = 0.065;

    alsFactorization.run(new String[] { "--input", inputFile.getAbsolutePath(), "--output", outputDir.getAbsolutePath(),
        "--tempDir", tmpDir.getAbsolutePath(), "--lambda", String.valueOf(lambda),
        "--numFeatures", String.valueOf(numFeatures), "--numIterations", String.valueOf(numIterations),
        "--numThreadsPerSolver", String.valueOf(numThreads) });

    Matrix u = MathHelper.readMatrix(conf, new Path(outputDir.getAbsolutePath(), "U/part-m-00000"),
        preferences.numRows(), numFeatures);
    Matrix m = MathHelper.readMatrix(conf, new Path(outputDir.getAbsolutePath(), "M/part-m-00000"),
        preferences.numCols(), numFeatures);

    StringBuilder info = new StringBuilder();
    info.append("\nA - users x items\n\n");
    info.append(MathHelper.nice(preferences));
    info.append("\nU - users x features\n\n");
    info.append(MathHelper.nice(u));
    info.append("\nM - items x features\n\n");
    info.append(MathHelper.nice(m));
    Matrix Ak = u.times(m.transpose());
    info.append("\nAk - users x items\n\n");
    info.append(MathHelper.nice(Ak));
    info.append('\n');

    log.info(info.toString());

    RunningAverage avg = new FullRunningAverage();
    for (MatrixSlice slice : preferences) {
      for (Element e : slice.nonZeroes()) {
        if (!Double.isNaN(e.get())) {
          double pref = e.get();
          double estimate = u.viewRow(slice.index()).dot(m.viewRow(e.index()));
          double err = pref - estimate;
          avg.addDatum(err * err);
          log.info("Comparing preference of user [{}] towards item [{}], was [{}] estimate is [{}]",
                   slice.index(), e.index(), pref, estimate);
        }
      }
    }
    double rmse = Math.sqrt(avg.getAverage());
    log.info("RMSE: {}", rmse);

    assertTrue(rmse < 0.2);
  }

  @Test
  public void completeJobImplicitToyExample() throws Exception {
    implicitExample(1);
  }

  @Test
  public void completeJobImplicitToyExampleMultithreaded() throws Exception {
    implicitExample(2);
  }

  public void implicitExample(int numThreads) throws Exception {
    Matrix observations = new SparseRowMatrix(4, 4, new Vector[] {
        new DenseVector(new double[] { 5.0, 5.0, 2.0, 0 }),
        new DenseVector(new double[] { 2.0, 0,   3.0, 5.0 }),
        new DenseVector(new double[] { 0,   5.0, 0,   3.0 }),
        new DenseVector(new double[] { 3.0, 0,   0,   5.0 }) });

    Matrix preferences = new SparseRowMatrix(4, 4, new Vector[] {
        new DenseVector(new double[] { 1.0, 1.0, 1.0, 0 }),
        new DenseVector(new double[] { 1.0, 0,   1.0, 1.0 }),
        new DenseVector(new double[] { 0,   1.0, 0,   1.0 }),
        new DenseVector(new double[] { 1.0, 0,   0,   1.0 }) });

    writeLines(inputFile, preferencesAsText(observations));

    ParallelALSFactorizationJob alsFactorization = new ParallelALSFactorizationJob();
    alsFactorization.setConf(conf);

    int numFeatures = 3;
    int numIterations = 5;
    double lambda = 0.065;
    double alpha = 20;

    alsFactorization.run(new String[] { "--input", inputFile.getAbsolutePath(), "--output", outputDir.getAbsolutePath(),
        "--tempDir", tmpDir.getAbsolutePath(), "--lambda", String.valueOf(lambda),
        "--implicitFeedback", String.valueOf(true), "--alpha", String.valueOf(alpha),
        "--numFeatures", String.valueOf(numFeatures), "--numIterations", String.valueOf(numIterations),
        "--numThreadsPerSolver", String.valueOf(numThreads) });

    Matrix u = MathHelper.readMatrix(conf, new Path(outputDir.getAbsolutePath(), "U/part-m-00000"),
        observations.numRows(), numFeatures);
    Matrix m = MathHelper.readMatrix(conf, new Path(outputDir.getAbsolutePath(), "M/part-m-00000"),
        observations.numCols(), numFeatures);

    StringBuilder info = new StringBuilder();
    info.append("\nObservations - users x items\n");
    info.append(MathHelper.nice(observations));
    info.append("\nA - users x items\n\n");
    info.append(MathHelper.nice(preferences));
    info.append("\nU - users x features\n\n");
    info.append(MathHelper.nice(u));
    info.append("\nM - items x features\n\n");
    info.append(MathHelper.nice(m));
    Matrix Ak = u.times(m.transpose());
    info.append("\nAk - users x items\n\n");
    info.append(MathHelper.nice(Ak));
    info.append('\n');

    log.info(info.toString());

    RunningAverage avg = new FullRunningAverage();
    for (MatrixSlice slice : preferences) {
      for (Element e : slice.nonZeroes()) {
        if (!Double.isNaN(e.get())) {
          double pref = e.get();
          double estimate = u.viewRow(slice.index()).dot(m.viewRow(e.index()));
          double confidence = 1 + alpha * observations.getQuick(slice.index(), e.index());
          double err = confidence * (pref - estimate) * (pref - estimate);
          avg.addDatum(err);
          log.info("Comparing preference of user [{}] towards item [{}], was [{}] with confidence [{}] " 
                       + "estimate is [{}]", slice.index(), e.index(), pref, confidence, estimate);
        }
      }
    }
    double rmse = Math.sqrt(avg.getAverage());
    log.info("RMSE: {}", rmse);

    assertTrue(rmse < 0.4);
  }

  @Test
  public void exampleWithIDMapping() throws Exception {

    String[] preferencesWithLongIDs = {
        "5568227754922264005,-4758971626494767444,5.0",
        "5568227754922264005,3688396615879561990,5.0",
        "5568227754922264005,4594226737871995304,2.0",
        "550945997885173934,-4758971626494767444,2.0",
        "550945997885173934,4594226737871995304,3.0",
        "550945997885173934,706816485922781596,5.0",
        "2448095297482319463,3688396615879561990,5.0",
        "2448095297482319463,706816485922781596,3.0",
        "6839920411763636962,-4758971626494767444,3.0",
        "6839920411763636962,706816485922781596,5.0" };

    writeLines(inputFile, preferencesWithLongIDs);

    ParallelALSFactorizationJob alsFactorization = new ParallelALSFactorizationJob();
    alsFactorization.setConf(conf);

    int numFeatures = 3;
    int numIterations = 5;
    double lambda = 0.065;

    alsFactorization.run(new String[] { "--input", inputFile.getAbsolutePath(), "--output", outputDir.getAbsolutePath(),
        "--tempDir", tmpDir.getAbsolutePath(), "--lambda", String.valueOf(lambda),
        "--numFeatures", String.valueOf(numFeatures), "--numIterations", String.valueOf(numIterations),
        "--numThreadsPerSolver", String.valueOf(1), "--usesLongIDs", String.valueOf(true) });


    OpenIntLongHashMap userIDIndex =
        TasteHadoopUtils.readIDIndexMap(outputDir.getAbsolutePath() + "/userIDIndex/part-r-00000", conf);
    assertEquals(4, userIDIndex.size());

    OpenIntLongHashMap itemIDIndex =
        TasteHadoopUtils.readIDIndexMap(outputDir.getAbsolutePath() + "/itemIDIndex/part-r-00000", conf);
    assertEquals(4, itemIDIndex.size());

    OpenIntObjectHashMap<Vector> u =
        MathHelper.readMatrixRows(conf, new Path(outputDir.getAbsolutePath(), "U/part-m-00000"));
    OpenIntObjectHashMap<Vector> m =
        MathHelper.readMatrixRows(conf, new Path(outputDir.getAbsolutePath(), "M/part-m-00000"));

    assertEquals(4, u.size());
    assertEquals(4, m.size());

    RunningAverage avg = new FullRunningAverage();
    for (String line : preferencesWithLongIDs) {
      String[] tokens = TasteHadoopUtils.splitPrefTokens(line);
      long userID = Long.parseLong(tokens[TasteHadoopUtils.USER_ID_POS]);
      long itemID = Long.parseLong(tokens[TasteHadoopUtils.ITEM_ID_POS]);
      double rating = Double.parseDouble(tokens[2]);

      Vector userFeatures = u.get(TasteHadoopUtils.idToIndex(userID));
      Vector itemFeatures = m.get(TasteHadoopUtils.idToIndex(itemID));

      double estimate = userFeatures.dot(itemFeatures);

      double err = rating - estimate;
      avg.addDatum(err * err);
    }

    double rmse = Math.sqrt(avg.getAverage());
    log.info("RMSE: {}", rmse);

    assertTrue(rmse < 0.2);
  }

  protected static String preferencesAsText(Matrix preferences) {
    StringBuilder prefsAsText = new StringBuilder();
    String separator = "";
    for (MatrixSlice slice : preferences) {
      for (Element e : slice.nonZeroes()) {
        if (!Double.isNaN(e.get())) {
          prefsAsText.append(separator)
              .append(slice.index()).append(',').append(e.index()).append(',').append(e.get());
          separator = "\n";
        }
      }
    }
    System.out.println(prefsAsText.toString());
    return prefsAsText.toString();
  }

  @Test
  public void recommenderJobWithIDMapping() throws Exception {

    String[] preferencesWithLongIDs = {
        "5568227754922264005,-4758971626494767444,5.0",
        "5568227754922264005,3688396615879561990,5.0",
        "5568227754922264005,4594226737871995304,2.0",
        "550945997885173934,-4758971626494767444,2.0",
        "550945997885173934,4594226737871995304,3.0",
        "550945997885173934,706816485922781596,5.0",
        "2448095297482319463,3688396615879561990,5.0",
        "2448095297482319463,706816485922781596,3.0",
        "6839920411763636962,-4758971626494767444,3.0",
        "6839920411763636962,706816485922781596,5.0" };

    writeLines(inputFile, preferencesWithLongIDs);

    ParallelALSFactorizationJob alsFactorization = new ParallelALSFactorizationJob();
    alsFactorization.setConf(conf);

    int numFeatures = 3;
    int numIterations = 5;
    double lambda = 0.065;

    int success = alsFactorization.run(new String[] {
        "--input", inputFile.getAbsolutePath(),
        "--output", intermediateDir.getAbsolutePath(),
        "--tempDir", tmpDir.getAbsolutePath(),
        "--lambda", String.valueOf(lambda),
        "--numFeatures", String.valueOf(numFeatures),
        "--numIterations", String.valueOf(numIterations),
        "--numThreadsPerSolver", String.valueOf(1),
        "--usesLongIDs", String.valueOf(true) });

    assertEquals(0, success);

    // reset as we run in the same JVM
    SharingMapper.reset();

    RecommenderJob recommender = new RecommenderJob();

    success = recommender.run(new String[] {
        "--input", intermediateDir.getAbsolutePath() + "/userRatings/",
        "--userFeatures", intermediateDir.getAbsolutePath() + "/U/",
        "--itemFeatures", intermediateDir.getAbsolutePath() + "/M/",
        "--numRecommendations", String.valueOf(2),
        "--maxRating", String.valueOf(5.0),
        "--numThreads", String.valueOf(2),
        "--usesLongIDs", String.valueOf(true),
        "--userIDIndex", intermediateDir.getAbsolutePath() + "/userIDIndex/",
        "--itemIDIndex", intermediateDir.getAbsolutePath() + "/itemIDIndex/",
        "--output", outputDir.getAbsolutePath() });

    assertEquals(0, success);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1404.java