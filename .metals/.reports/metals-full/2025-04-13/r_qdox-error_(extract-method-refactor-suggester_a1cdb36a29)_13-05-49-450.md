error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/304.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/304.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/304.java
text:
```scala
i@@t = vector.iterateNonZero();

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

package org.apache.mahout.math;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** Implements subset view of a Vector */
public class VectorView extends AbstractVector {

  private Vector vector;

  // the offset into the Vector
  private int offset;

  /** For serialization purposes only */
  public VectorView() {
    super(0);
  }

  public VectorView(Vector vector, int offset, int cardinality) {
    super(cardinality);
    this.vector = vector;
    this.offset = offset;
  }

  @Override
  protected Matrix matrixLike(int rows, int columns) {
    return ((AbstractVector) vector).matrixLike(rows, columns);
  }

  @Override
  public Vector clone() {
    VectorView r = (VectorView) super.clone();
    r.vector = vector.clone();
    r.offset = offset;
    return r;
  }

  public boolean isDense() {
    return vector.isDense();
  }

  public boolean isSequentialAccess() {
    return vector.isSequentialAccess();
  }

  public VectorView like() {
    return new VectorView(vector.like(), offset, size());
  }

  public double getQuick(int index) {
    return vector.getQuick(offset + index);
  }

  public void setQuick(int index, double value) {
    vector.setQuick(offset + index, value);
  }

  public int getNumNondefaultElements() {
    return size();
  }

  @Override
  public Vector viewPart(int offset, int length) {
    if (offset < 0) {
      throw new IndexException(offset, size());
    }
    if (offset + length > size()) {
      throw new IndexException(offset + length, size());
    }
    return new VectorView(vector, offset + this.offset, length);
  }

  /** @return true if index is a valid index in the underlying Vector */
  private boolean isInView(int index) {
    return index >= offset && index < offset + size();
  }

  public Iterator<Element> iterateNonZero() {
    return new NonZeroIterator();
  }

  public Iterator<Element> iterator() {
    return new AllIterator();
  }

  public final class NonZeroIterator implements Iterator<Element> {

    private final Iterator<Element> it;

    private Element el;

    private NonZeroIterator() {
      it = vector.iterator();
      buffer();
    }

    private void buffer() {
      while (it.hasNext()) {
        el = it.next();
        if (isInView(el.index()) && el.get() != 0) {
          final Element decorated = vector.getElement(el.index());
          el = new Element() {
            public double get() {
              return decorated.get();
            }

            public int index() {
              return decorated.index() - offset;
            }

            public void set(double value) {
              decorated.set(value);
            }
          };
          return;
        }
      }
      el = null; // No element was found
    }

    public Element next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      Element buffer = el;
      buffer();
      return buffer;
    }

    public boolean hasNext() {
      return el != null;
    }

    /** @throws UnsupportedOperationException all the time. method not implemented. */
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  public final class AllIterator implements Iterator<Element> {

    private final Iterator<Element> it;

    private Element el;

    private AllIterator() {
      it = vector.iterator();
      buffer();
    }

    private void buffer() {
      while (it.hasNext()) {
        el = it.next();
        if (isInView(el.index())) {
          final Element decorated = vector.getElement(el.index());
          el = new Element() {
            public double get() {
              return decorated.get();
            }

            public int index() {
              return decorated.index() - offset;
            }

            public void set(double value) {
              decorated.set(value);
            }
          };
          return;
        }
      }
      el = null; // No element was found
    }

    public Element next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      Element buffer = el;
      buffer();
      return buffer;
    }

    public boolean hasNext() {
      return el != null;
    }

    /** @throws UnsupportedOperationException all the time. method not implemented. */
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }


  @Override
  public double dot(Vector x) {
    if (size() != x.size()) {
      throw new CardinalityException(size(), x.size());
    }
    double result = 0;
    for (int i = 0; i < size(); i++) {
      result += getQuick(i) * x.getQuick(i);
    }
    return result;
  }

  @Override
  public double getLengthSquared() {
    double result = 0.0;
    int size = size();
    for (int i = 0; i < size; i++) {
      double value = getQuick(i);
      result += value * value;
    }
    return result;
  }

  @Override
  public double getDistanceSquared(Vector v) {
    double result = 0.0;
    int size = size();
    for (int i = 0; i < size; i++) {
      double delta = getQuick(i) - v.getQuick(i);
      result += delta * delta;
    }
    return result;
  }

  @Override
  public void addTo(Vector v) {
    Iterator<Element> iter = iterateNonZero();
    while (iter.hasNext()) {
      Element elt = iter.next();
      v.set(elt.index(), elt.get() + v.get(elt.index()));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/304.java