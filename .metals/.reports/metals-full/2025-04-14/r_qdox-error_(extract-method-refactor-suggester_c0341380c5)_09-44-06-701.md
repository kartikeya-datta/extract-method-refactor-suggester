error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14570.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14570.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14570.java
text:
```scala
y@@ = problem.y.clone();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math.ode.events.EventHandler;

/**
 * This class is used in the junit tests for the ODE integrators.

 * <p>This specific problem is the following differential equation :
 * <pre>
 *    x'' = -x
 * </pre>
 * And when x decreases down to 0, the state should be changed as follows :
 * <pre>
 *   x' -> -x'
 * </pre>
 * The theoretical solution of this problem is x = |sin(t+a)|
 * </p>

 */
public class TestProblem4
  extends TestProblemAbstract {

  /** Serializable version identifier. */
  private static final long serialVersionUID = -5910438521889015745L;

  /** Time offset. */
  private double a;

  /** theoretical state */
  private double[] y;

  /** Simple constructor. */
  public TestProblem4() {
    super();
    a = 1.2;
    double[] y0 = { Math.sin(a), Math.cos(a) };
    setInitialConditions(0.0, y0);
    setFinalConditions(15);
    double[] errorScale = { 1.0, 0.0 };
    setErrorScale(errorScale);
    y = new double[y0.length];
  }
 
  /**
   * Copy constructor.
   * @param problem problem to copy
   */
  public TestProblem4(TestProblem4 problem) {
    super(problem);
    a = problem.a;
    y = (double[]) problem.y.clone();
  }

  /**
   * Clone operation.
   * @return a copy of the instance
   */
  @Override
  public Object clone() {
    return new TestProblem4(this);
  }

  @Override
  public EventHandler[] getEventsHandlers() {
    return new EventHandler[] { new Bounce(), new Stop() };
  }

  @Override
  public void doComputeDerivatives(double t, double[] y, double[] yDot) {
    yDot[0] =  y[1];
    yDot[1] = -y[0];
  }

  @Override
  public double[] computeTheoreticalState(double t) {
    double sin = Math.sin(t + a);
    double cos = Math.cos(t + a);
    y[0] = Math.abs(sin);
    y[1] = (sin >= 0) ? cos : -cos;
    return y;
  }

  private static class Bounce implements EventHandler {

    private static final long serialVersionUID = 1356097180027801200L;
    private int sign;

    public Bounce() {
      sign = +1;
    }

    public double g(double t, double[] y) {
      return sign * y[0];
    }

    public int eventOccurred(double t, double[] y) {
      // this sign change is needed because the state will be reset soon
      sign = -sign;
      return EventHandler.RESET_STATE;
    }
  
    public void resetState(double t, double[] y) {
      y[0] = -y[0];
      y[1] = -y[1];
    }

  }

  private static class Stop implements EventHandler {

    private static final long serialVersionUID = 6975050568227951931L;

    public Stop() {
    }

    public double g(double t, double[] y) {
      return t - 12.0;
    }

    public int eventOccurred(double t, double[] y) {
      return EventHandler.STOP;
    }
  
    public void resetState(double t, double[] y) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14570.java