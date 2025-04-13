error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10988.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10988.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10988.java
text:
```scala
p@@rivate static class Equations

// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
// 
//   http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.spaceroots.mantissa.ode;

import junit.framework.*;

public class FirstOrderConverterTest
  extends TestCase {

  public FirstOrderConverterTest(String name) {
    super(name);
  }

  public void testDoubleDimension() {
    for (int i = 1; i < 10; ++i) {
      SecondOrderDifferentialEquations eqn2 = new Equations(i, 0.2);
      FirstOrderConverter eqn1 = new FirstOrderConverter(eqn2);
      assertTrue(eqn1.getDimension() == (2 * eqn2.getDimension()));
    }
  }
  
  public void testDecreasingSteps()
    throws DerivativeException, IntegratorException {
      
    double previousError = Double.NaN;
    for (int i = 0; i < 10; ++i) {

      double step  = Math.pow(2.0, -(i + 1));
      double error = integrateWithSpecifiedStep(4.0, 0.0, 1.0, step)
                   - Math.sin(4.0);
      if (i > 0) {
        assertTrue(Math.abs(error) < Math.abs(previousError));
      }
      previousError = error;
      
    }
  }

  public void testSmallStep()
    throws DerivativeException, IntegratorException {
    double error = integrateWithSpecifiedStep(4.0, 0.0, 1.0, 1.0e-4)
                   - Math.sin(4.0);
    assertTrue(Math.abs(error) < 1.0e-10);
  }

  public void testBigStep()
    throws DerivativeException, IntegratorException {
    double error = integrateWithSpecifiedStep(4.0, 0.0, 1.0, 0.5)
                   - Math.sin(4.0);
    assertTrue(Math.abs(error) > 0.1);
  }
  
  public static Test suite() {
    return new TestSuite(FirstOrderConverterTest.class);
  }

  private class Equations
    implements SecondOrderDifferentialEquations {
      
      private int n;

      private double omega2;
      
      public Equations(int n, double omega) {
        this.n = n;
        omega2 = omega * omega;
      }
      
      public int getDimension() {
        return n;
      }
      
      public void computeSecondDerivatives(double t, double[] y, double[] yDot,
                                           double[] yDDot) {
        for (int i = 0; i < n; ++i) {
          yDDot[i] = -omega2 * y[i];
        }
    }
      
  }

  private double integrateWithSpecifiedStep(double omega,
                                            double t0, double t,
                                            double step)
  throws DerivativeException, IntegratorException {
    double[] y0 = new double[2];
    y0[0] = Math.sin(omega * t0);
    y0[1] = omega * Math.cos(omega * t0);
    ClassicalRungeKuttaIntegrator i = new ClassicalRungeKuttaIntegrator(step);
    double[] y = new double[2];
    i.integrate(new FirstOrderConverter(new Equations(1, omega)), t0, y0, t, y);
    return y[0];
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10988.java