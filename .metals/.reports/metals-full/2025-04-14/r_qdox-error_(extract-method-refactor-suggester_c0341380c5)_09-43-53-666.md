error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1407.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1407.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1407.java
text:
```scala
C@@onfiguration conf = getConfiguration();

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

package org.apache.mahout.math.hadoop.stochasticsvd;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.common.MahoutTestCase;
import org.apache.mahout.math.DenseMatrix;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.junit.Test;

/**
 * 
 * Tests SSVD solver with a made-up data running hadoop solver in a local mode.
 * It requests full-rank SSVD and then compares singular values to that of
 * Colt's SVD asserting epsilon(precision) 1e-10 or whatever most recent value
 * configured.
 * 
 */
public class LocalSSVDSolverDenseTest extends MahoutTestCase {

  private static final double s_epsilon = 1.0E-10d;

  /*
   * I actually never saw errors more than 3% worst case for this particular
   * test, but since it's non-deterministic test, it still may occasionally
   * produce bad results with a non-zero probability, so i put this pct% for
   * error margin high enough so it (almost) never fails.
   */
  private static final double s_precisionPct = 10;

  @Test
  public void testSSVDSolverDense() throws IOException {
    runSSVDSolver(0);
  }

  @Test
  public void testSSVDSolverPowerIterations1() throws IOException {
    runSSVDSolver(1);
  }

  // remove from active tests to save time.
  /* 
  @Test
  public void testSSVDSolverPowerIterations2() throws IOException {
    runSSVDSolver(2);
  }
   */

  public void runSSVDSolver(int q) throws IOException {

    Configuration conf = new Configuration();
    conf.set("mapred.job.tracker", "local");
    conf.set("fs.default.name", "file:///");

    // conf.set("mapred.job.tracker","localhost:11011");
    // conf.set("fs.default.name","hdfs://localhost:11010/");

    File tmpDir = getTestTempDir("svdtmp");
    conf.set("hadoop.tmp.dir", tmpDir.getAbsolutePath());

    Path aLocPath = new Path(getTestTempDirPath("svdtmp/A"), "A.seq");

    // create distributed row matrix-like struct
    // SequenceFile.Writer w = SequenceFile.createWriter(
    // FileSystem.getLocal(conf), conf, aLocPath, IntWritable.class,
    // VectorWritable.class, CompressionType.NONE, new DefaultCodec());
    // closeables.addFirst(w);

    // make input equivalent to 2 mln non-zero elements.
    // With 100mln the precision turns out to be only better (LLN law i guess)
    // With oversampling of 100, i don't get any error at all.
    int n = 100;
    int m = 2000;
    Vector singularValues =
      new DenseVector(new double[] { 10, 4, 1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1,
          0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1,
          0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1,
          0.1, 0.1, 0.1, 0.1, 0.1, 0.1 });

    SSVDTestsHelper.generateDenseInput(aLocPath,
                                       FileSystem.getLocal(conf),
                                       singularValues,
                                       m,
                                       n);

    FileSystem fs = FileSystem.get(aLocPath.toUri(), conf);

    Path tempDirPath = getTestTempDirPath("svd-proc");
    Path aPath = new Path(tempDirPath, "A/A.seq");
    fs.copyFromLocalFile(aLocPath, aPath);

    Path svdOutPath = new Path(tempDirPath, "SSVD-out");

    // Solver starts here:
    System.out.println("Input prepared, starting solver...");

    int ablockRows = 867;
    int p = 10;
    int k = 3;
    SSVDSolver ssvd =
      new SSVDSolver(conf,
                     new Path[] { aPath },
                     svdOutPath,
                     ablockRows,
                     k,
                     p,
                     3);
    /*
     * these are only tiny-test values to simulate high load cases, in reality
     * one needs much bigger
     */
    ssvd.setOuterBlockHeight(500);
    ssvd.setAbtBlockHeight(400);
    ssvd.setOverwrite(true);
    ssvd.setQ(q);
    ssvd.setBroadcast(false);
    ssvd.run();

    Vector stochasticSValues = ssvd.getSingularValues();
    System.out.println("--SSVD solver singular values:");
    dumpSv(stochasticSValues);

    // the full-rank svd for this test size takes too long to run,
    // so i comment it out, instead, i will be comparing
    // result singular values to the original values used
    // to generate input (which are guaranteed to be right).

    /*
     * System.out.println("--Colt SVD solver singular values:"); // try to run
     * 
     * the same thing without stochastic algo double[][] a =
     * SSVDSolver.drmLoadAsDense(fs, aPath, conf);
     * 
     * 
     * 
     * SingularValueDecomposition svd2 = new SingularValueDecomposition(new
     * DenseMatrix(a));
     * 
     * a = null;
     * 
     * double[] svalues2 = svd2.getSingularValues(); dumpSv(svalues2);
     * 
     * for (int i = 0; i < k ; i++) { Assert .assertTrue(1-Math.abs((svalues2[i]
     * - stochasticSValues[i])/svalues2[i]) <= s_precisionPct/100); }
     */

    // assert first k against those
    // used to generate surrogate input

    for (int i = 0; i < k; i++) {
      assertTrue(Math.abs((singularValues.getQuick(i) - stochasticSValues.getQuick(i))
          / singularValues.getQuick(i)) <= s_precisionPct / 100);
    }

    DenseMatrix mQ =
      SSVDHelper.drmLoadAsDense(fs, new Path(svdOutPath, "Bt-job/"
        + BtJob.OUTPUT_Q + "-*"), conf);

    SSVDCommonTest.assertOrthonormality(mQ,
                                        false,
                                        s_epsilon);

    DenseMatrix u =
      SSVDHelper.drmLoadAsDense(fs,
                                new Path(svdOutPath, "U/*"),
                                conf);
    SSVDCommonTest.assertOrthonormality(u, false, s_epsilon);

    DenseMatrix v =
      SSVDHelper.drmLoadAsDense(fs,
                                new Path(svdOutPath, "V/*"),
                                conf);
    SSVDCommonTest.assertOrthonormality(v, false, s_epsilon);
  }

  static void dumpSv(Vector s) {
    System.out.printf("svs: ");
    for (Vector.Element el : s.all()) {
      System.out.printf("%f  ", el.get());
    }
    System.out.println();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1407.java