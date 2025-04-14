error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/988.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/988.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/988.java
text:
```scala
protected v@@oid setUp() {

package org.apache.mahout.ga.watchmaker.cd;

import junit.framework.TestCase;
import org.apache.mahout.ga.watchmaker.cd.utils.MockDataSet;
import org.uncommons.maths.random.MersenneTwisterRNG;
import static org.easymock.classextension.EasyMock.*;

import java.util.Random;

public class CDRuleTest extends TestCase {

  private Random rng;

  private MockDataSet mock;

  /**
   * Test method for
   * {@link org.apache.mahout.ga.watchmaker.cd.CDFactory#generateRandomCandidate(java.util.Random)}.
   */
  public void testRandomCDRule() {
    DataSet dataset = DataSet.getDataSet();
    double threshold = 0f;

    int n = 100;
    for (int nloop = 0; nloop < n; nloop++) {
      mock.randomDataset();

      CDRule rule = new CDRule(threshold, rng);
      for (int condInd = 0; condInd < rule.getNbConditions(); condInd++) {
        int attrInd = rule.attributeIndex(condInd);
        
        assertInRange(rule.getW(condInd), 0, 1);
        
        if (dataset.isNumerical(attrInd)) {
          assertInRange(rule.getV(condInd), dataset.getMin(attrInd), dataset
              .getMax(attrInd));
        } else {
          assertInRange(rule.getV(condInd), 0, dataset.getNbValues(attrInd) - 1);
        }
      }

      mock.verify();
    }
  }

  private void assertInRange(double value, double min, double max) {
    TestCase.assertTrue("value < min", value >= min);
    TestCase.assertTrue("value > max", value <= max);
  }

  @Override
  protected void setUp() throws Exception {
    rng = new MersenneTwisterRNG();
    mock = new MockDataSet(rng, 50);
  }

  /**
   * Test the Weight part of the condition.
   * 
   */
  public void testWCondition() {
    int n = 100; // repeat the test n times

    // the dataline has all its attributes set to 0d
    DataLine dl = createMock(DataLine.class);
    expect(dl.getAttribut(anyInt())).andReturn(0d).atLeastOnce();
    replay(dl);

    // all the conditions are : attribut < 0
    for (int nloop = 0; nloop < n; nloop++) {
      double thr = rng.nextDouble();

      mock.numericalDataset();

      CDRule rule = new CDRule(thr);
      for (int index = 0; index < rule.getNbConditions(); index++) {
        rule.setW(index, rng.nextDouble());
        rule.setO(index, false);
        rule.setV(index, 0);
      }

      // all coditions should return false unless w < threshold
      for (int condInd = 0; condInd < rule.getNbConditions(); condInd++) {
        if (rule.getW(condInd) < thr)
          assertTrue(rule.condition(condInd, dl));
        else
          assertFalse(rule.condition(condInd, dl));
      }

      mock.verify();
    }

    verify(dl);
  }

  /**
   * Test the Operator part of the condition, on numerical attributes
   * 
   */
  public void testOConditionNumerical() {
    int n = 100; // repeat the test n times

    // the dataline has all its attributes set to 1d
    DataLine dl = createMock(DataLine.class);
    expect(dl.getAttribut(anyInt())).andReturn(1d).atLeastOnce();
    replay(dl);

    for (int nloop = 0; nloop < n; nloop++) {
      mock.numericalDataset();

      CDRule rule = new CDRule(0.);
      for (int condInd = 0; condInd < rule.getNbConditions(); condInd++) {
        rule.setW(condInd, 1.); // all weights are 1 (active)
        rule.setO(condInd, rng.nextBoolean());
        rule.setV(condInd, 0);
      }

      // the condition is true if the operator is >=
      for (int condInd = 0; condInd < rule.getNbConditions(); condInd++) {
        if (rule.getO(condInd))
          assertTrue(rule.condition(condInd, dl));
        else
          assertFalse(rule.condition(condInd, dl));
      }

      mock.verify();
    }

    verify(dl);
  }

  /**
   * Test the Operator part of the condition, on numerical attributes
   * 
   */
  public void testOConditionCategorical() {
    int n = 100; // repeat the test n times

    // the dataline has all its attributes set to 1d
    DataLine dl = createMock(DataLine.class);
    expect(dl.getAttribut(anyInt())).andReturn(1d).atLeastOnce();
    replay(dl);

    Random rng = new MersenneTwisterRNG();
    for (int nloop = 0; nloop < n; nloop++) {
      mock.categoricalDataset();

      // all weights are 1 (active)
      CDRule rule = new CDRule(0.);
      for (int condInd = 0; condInd < rule.getNbConditions(); condInd++) {
        rule.setW(condInd, 1.);
        rule.setO(condInd, rng.nextBoolean());
        rule.setV(condInd, rng.nextInt(2)); // two categories
      }

      // the condition is true if the operator is == and the values are equal
      // (value==1), or the operator is != and the values are no equal
      // (value==0)
      for (int condInd = 0; condInd < rule.getNbConditions(); condInd++) {
        if ((rule.getO(condInd) && rule.getV(condInd) == 1)
 (!rule.getO(condInd) && rule.getV(condInd) != 1))
          assertTrue(rule.condition(condInd, dl));
        else
          assertFalse(rule.condition(condInd, dl));
      }

      mock.verify();
    }

    verify(dl);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/988.java