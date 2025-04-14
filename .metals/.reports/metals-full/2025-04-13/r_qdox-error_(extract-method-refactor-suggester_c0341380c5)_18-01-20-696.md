error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2216.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2216.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2216.java
text:
```scala
i@@f (this.payload != null) {

package org.apache.mahout.ep;

import java.util.Arrays;
import java.util.Random;

/**
 * Records evolutionary state and provides a mutation operation for recorded-step meta-mutation.
 *
 * You provide the payload, this class provides the mutation operations.  During mutation,
 * the payload is copied and after the state variables are changed, they are passed to the
 * payload.
 *
 * Parameters are internally mutated in a state space that spans all of R^n, but parameters
 * passed to the payload are transformed as specified by a call to setMap().  The default
 * mapping is the identity map, but uniform-ish or exponential-ish coverage of a range are
 * also supported.
 *
 * More information on the underlying algorithm can be found in the following paper
 *
 * http://arxiv.org/abs/0803.3838
 *
 * @see Mapping
 * @see State
 */
public class State<T extends Payload<T>> implements Comparable<State<T>> {
  // object count is kept to break ties in comparison.
  static volatile int objectCount = 0;

  int id = objectCount++;

  private Random gen = new Random();

  // current state
  private double[] params;

  // mappers to transform state
  private Mapping[] maps;

  // omni-directional mutation
  private double omni;

  // directional mutation
  private double[] step;

  // current fitness value
  private double value;

  private T payload;

  private State() {
  }

  /**
   * Invent a new state with no momentum (yet).
   */
  public State(double[] x0, double omni) {
    params = Arrays.copyOf(x0, x0.length);
    this.omni = omni;
    step = new double[params.length];
    maps = new Mapping[params.length];
  }

  /**
   * Deep copies a state, useful in mutation.
   */
  public State<T> copy() {
    State<T> r = new State<T>();
    r.params = Arrays.copyOf(this.params, this.params.length);
    r.omni = this.omni;
    r.step = Arrays.copyOf(this.step, this.step.length);
    r.maps = Arrays.copyOf(this.maps, this.maps.length);
    if (this.payload != null) {
      r.payload = this.payload.copy();
    }
    r.gen = this.gen;
    return r;
  }

  /**
   * Clones this state with a random change in position.  Copies the payload and
   * lets it know about the change.
   *
   * @return A new state.
   */
  public State<T> mutate() {
    double sum = 0;
    for (double v : step) {
      sum += v * v;
    }
    sum = Math.sqrt(sum);
    double lambda = 1 + gen.nextGaussian();

    State<T> r = this.copy();
    double magnitude = 0.9 * omni + sum / 10;
    r.omni = magnitude * -Math.log(1 - gen.nextDouble());
    for (int i = 0; i < step.length; i++) {
      r.step[i] = lambda * step[i] + r.omni * gen.nextGaussian();
      r.params[i] += r.step[i];
    }
    if (r.payload != null) {
      r.payload.update(r.getMappedParams());
    }
    return r;
  }

  /**
   * Defines the transformation for a parameter.
   * @param i Which parameter's mapping to define.
   * @param m The mapping to use.
   * @see org.apache.mahout.ep.Mapping
   */
  public void setMap(int i, Mapping m) {
    maps[i] = m;
  }

  /**
   * Returns a transformed parameter.
   * @param i  The parameter to return.
   * @return The value of the parameter.
   */
  public double get(int i) {
    Mapping m = maps[i];
    if (m == null) {
      return params[i];
    } else {
      return m.apply(params[i]);
    }
  }

  /**
   * Returns all the parameters in mapped form.
   * @return An array of parameters.
   */
  public double[] getMappedParams() {
    double[] r = Arrays.copyOf(params, params.length);
    for (int i = 0; i < params.length; i++) {
      r[i] = get(i);
    }
    return r;
  }

  public double getOmni() {
    return omni;
  }

  public double[] getStep() {
    return step;
  }

  public T getPayload() {
    return payload;
  }

  public double getValue() {
    return value;
  }

  public void setRand(Random rand) {
    this.gen = rand;
  }

  public void setOmni(double omni) {
    this.omni = omni;
  }

  public void setValue(double v) {
    value = v;
  }

  public void setPayload(T payload) {
    this.payload = payload;
  }

  /**
   * Natural order is to sort in descending order of score.  Creation order is used as a
   * tie-breaker.
   *
   * @param other The state to compare with.
   * @return -1, 0, 1 if the other state is better, identical or worse than this one.
   */
  @Override
  public int compareTo(State other) {
    int r = Double.compare(other.value, this.value);
    if (r != 0) {
      return r;
    } else {
      return this.id - other.id;
    }
  }

  public String toString() {
    double sum = 0;
    for (double v : step) {
      sum += v * v;
    }
    return String.format("<S/%s %.3f %.3f>", payload, omni + Math.sqrt(sum), value);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2216.java