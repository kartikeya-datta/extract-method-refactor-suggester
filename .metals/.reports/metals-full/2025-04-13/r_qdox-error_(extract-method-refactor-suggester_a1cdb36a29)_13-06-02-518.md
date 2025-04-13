error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/873.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/873.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/873.java
text:
```scala
i@@f (delegate == null || name == null) {

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

package org.apache.mahout.math;

import java.util.Iterator;

import org.apache.mahout.math.function.DoubleDoubleFunction;
import org.apache.mahout.math.function.DoubleFunction;

public class NamedVector implements Vector {

  private Vector delegate;
  private String name;

  public NamedVector() {
  }

  public NamedVector(NamedVector other) {
    this.delegate = other.getDelegate();
    this.name = other.getName();
  }

  public NamedVector(Vector delegate, String name) {
    if (delegate == null) {
      throw new IllegalArgumentException();
    }
    this.delegate = delegate;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Vector getDelegate() {
    return delegate;
  }

  public int hashCode() {
    return delegate.hashCode();
  }

  /**
   * To not break transitivity with other {@link Vector}s, this does not compare name.
   */
  public boolean equals(Object other) {
    return delegate.equals(other);
  }

  @Override
  public NamedVector clone() {
    return new NamedVector(delegate.clone(), name);
  }

  @Override
  public String asFormatString() {
    return toString();
  }

  @Override
  public Vector assign(double value) {
    return delegate.assign(value);
  }

  @Override
  public Vector assign(double[] values) {
    return delegate.assign(values);
  }

  @Override
  public Vector assign(Vector other) {
    return delegate.assign(other);
  }

  @Override
  public Vector assign(DoubleFunction function) {
    return delegate.assign(function);
  }

  @Override
  public Vector assign(Vector other, DoubleDoubleFunction function) {
    return delegate.assign(other, function);
  }

  @Override
  public Vector assign(DoubleDoubleFunction f, double y) {
    return delegate.assign(f, y);
  }

  @Override
  public int size() {
    return delegate.size();
  }

  @Override
  public boolean isDense() {
    return delegate.isDense();
  }

  @Override
  public boolean isSequentialAccess() {
    return delegate.isSequentialAccess();
  }

  @Override
  public Iterator<Element> iterator() {
    return delegate.iterator();
  }

  @Override
  public Iterator<Element> iterateNonZero() {
    return delegate.iterateNonZero();
  }

  @Override
  public Element getElement(int index) {
    return delegate.getElement(index);
  }

  @Override
  public Vector divide(double x) {
    return delegate.divide(x);
  }

  @Override
  public double dot(Vector x) {
    return delegate.dot(x);
  }

  @Override
  public double get(int index) {
    return delegate.get(index);
  }

  @Override
  public double getQuick(int index) {
    return delegate.getQuick(index);
  }

  @Override
  public NamedVector like() {
    return new NamedVector(delegate.like(), name);
  }

  @Override
  public Vector minus(Vector x) {
    return delegate.minus(x);
  }

  @Override
  public Vector normalize() {
    return delegate.normalize();
  }

  @Override
  public Vector normalize(double power) {
    return delegate.normalize(power);
  }

  @Override
  public Vector logNormalize() {
    return delegate.logNormalize();
  }

  @Override
  public Vector logNormalize(double power) {
    return delegate.logNormalize(power);
  }

  @Override
  public double norm(double power) {
    return delegate.norm(power);
  }

  @Override
  public double maxValue() {
    return delegate.maxValue();
  }

  @Override
  public int maxValueIndex() {
    return delegate.maxValueIndex();
  }

  @Override
  public double minValue() {
    return delegate.minValue();
  }

  @Override
  public int minValueIndex() {
    return delegate.minValueIndex();
  }

  @Override
  public Vector plus(double x) {
    return delegate.plus(x);
  }

  @Override
  public Vector plus(Vector x) {
    return delegate.plus(x);
  }

  @Override
  public void set(int index, double value) {
    delegate.set(index, value);
  }

  @Override
  public void setQuick(int index, double value) {
    delegate.setQuick(index, value);
  }

  @Override
  public int getNumNondefaultElements() {
    return delegate.getNumNondefaultElements();
  }

  @Override
  public Vector times(double x) {
    return delegate.times(x);
  }

  @Override
  public Vector times(Vector x) {
    return delegate.times(x);
  }

  @Override
  public Vector viewPart(int offset, int length) {
    return delegate.viewPart(offset, length);
  }

  @Override
  public double zSum() {
    return delegate.zSum();
  }

  @Override
  public Matrix cross(Vector other) {
    return delegate.cross(other);
  }

  @Override
  public double aggregate(DoubleDoubleFunction aggregator, DoubleFunction map) {
    return delegate.aggregate(aggregator, map);
  }

  @Override
  public double aggregate(Vector other, DoubleDoubleFunction aggregator, DoubleDoubleFunction combiner) {
    return delegate.aggregate(other, aggregator, combiner);
  }

  @Override
  public double getLengthSquared() {
    return delegate.getLengthSquared();
  }

  @Override
  public double getDistanceSquared(Vector v) {
    return delegate.getDistanceSquared(v);
  }

  @Override
  public void addTo(Vector v) {
    delegate.addTo(v);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/873.java