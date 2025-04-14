error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1312.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1312.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1312.java
text:
```scala
S@@patialRelation rel = cell.getShape().relate(shapeFilter);

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

package org.apache.lucene.spatial.prefix.tree;

import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.Shape;
import com.spatial4j.core.shape.SpatialRelation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents a grid cell. These are not necessarily threadsafe, although new Cell("") (world cell) must be.
 *
 * @lucene.experimental
 */
public abstract class Node implements Comparable<Node> {
  public static final byte LEAF_BYTE = '+';//NOTE: must sort before letters & numbers

  /*
  Holds a byte[] and/or String representation of the cell. Both are lazy constructed from the other.
  Neither contains the trailing leaf byte.
   */
  private byte[] bytes;
  private int b_off;
  private int b_len;

  private String token;//this is the only part of equality

  protected SpatialRelation shapeRel;//set in getSubCells(filter), and via setLeaf().
  private SpatialPrefixTree spatialPrefixTree;

  protected Node(SpatialPrefixTree spatialPrefixTree, String token) {
    this.spatialPrefixTree = spatialPrefixTree;
    this.token = token;
    if (token.length() > 0 && token.charAt(token.length() - 1) == (char) LEAF_BYTE) {
      this.token = token.substring(0, token.length() - 1);
      setLeaf();
    }

    if (getLevel() == 0)
      getShape();//ensure any lazy instantiation completes to make this threadsafe
  }

  protected Node(SpatialPrefixTree spatialPrefixTree, byte[] bytes, int off, int len) {
    this.spatialPrefixTree = spatialPrefixTree;
    this.bytes = bytes;
    this.b_off = off;
    this.b_len = len;
    b_fixLeaf();
  }

  public void reset(byte[] bytes, int off, int len) {
    assert getLevel() != 0;
    token = null;
    shapeRel = null;
    this.bytes = bytes;
    this.b_off = off;
    this.b_len = len;
    b_fixLeaf();
  }

  private void b_fixLeaf() {
    if (bytes[b_off + b_len - 1] == LEAF_BYTE) {
      b_len--;
      setLeaf();
    } else if (getLevel() == spatialPrefixTree.getMaxLevels()) {
      setLeaf();
    }
  }

  public SpatialRelation getShapeRel() {
    return shapeRel;
  }

  public boolean isLeaf() {
    return shapeRel == SpatialRelation.WITHIN;
  }

  public void setLeaf() {
    assert getLevel() != 0;
    shapeRel = SpatialRelation.WITHIN;
  }

  /**
   * Note: doesn't contain a trailing leaf byte.
   */
  public String getTokenString() {
    if (token == null) {
      token = new String(bytes, b_off, b_len, SpatialPrefixTree.UTF8);
    }
    return token;
  }

  /**
   * Note: doesn't contain a trailing leaf byte.
   */
  public byte[] getTokenBytes() {
    if (bytes != null) {
      if (b_off != 0 || b_len != bytes.length) {
        throw new IllegalStateException("Not supported if byte[] needs to be recreated.");
      }
    } else {
      bytes = token.getBytes(SpatialPrefixTree.UTF8);
      b_off = 0;
      b_len = bytes.length;
    }
    return bytes;
  }

  public int getLevel() {
    return token != null ? token.length() : b_len;
  }

  //TODO add getParent() and update some algorithms to use this?
  //public Cell getParent();

  /**
   * Like {@link #getSubCells()} but with the results filtered by a shape. If that shape is a {@link com.spatial4j.core.shape.Point} then it
   * must call {@link #getSubCell(com.spatial4j.core.shape.Point)};
   * Precondition: Never called when getLevel() == maxLevel.
   *
   * @param shapeFilter an optional filter for the returned cells.
   * @return A set of cells (no dups), sorted. Not Modifiable.
   */
  public Collection<Node> getSubCells(Shape shapeFilter) {
    //Note: Higher-performing subclasses might override to consider the shape filter to generate fewer cells.
    if (shapeFilter instanceof Point) {
      return Collections.singleton(getSubCell((Point) shapeFilter));
    }
    Collection<Node> cells = getSubCells();

    if (shapeFilter == null) {
      return cells;
    }
    List<Node> copy = new ArrayList<Node>(cells.size());//copy since cells contractually isn't modifiable
    for (Node cell : cells) {
      SpatialRelation rel = cell.getShape().relate(shapeFilter, spatialPrefixTree.ctx);
      if (rel == SpatialRelation.DISJOINT)
        continue;
      cell.shapeRel = rel;
      copy.add(cell);
    }
    cells = copy;
    return cells;
  }

  /**
   * Performant implementations are expected to implement this efficiently by considering the current
   * cell's boundary.
   * Precondition: Never called when getLevel() == maxLevel.
   * Precondition: this.getShape().relate(p) != DISJOINT.
   */
  public abstract Node getSubCell(Point p);

  //TODO Cell getSubCell(byte b)

  /**
   * Gets the cells at the next grid cell level that cover this cell.
   * Precondition: Never called when getLevel() == maxLevel.
   *
   * @return A set of cells (no dups), sorted. Not Modifiable.
   */
  protected abstract Collection<Node> getSubCells();

  /**
   * {@link #getSubCells()}.size() -- usually a constant. Should be >=2
   */
  public abstract int getSubCellsSize();

  public abstract Shape getShape();

  public Point getCenter() {
    return getShape().getCenter();
  }

  @Override
  public int compareTo(Node o) {
    return getTokenString().compareTo(o.getTokenString());
  }

  @Override
  public boolean equals(Object obj) {
    return !(obj == null || !(obj instanceof Node)) && getTokenString().equals(((Node) obj).getTokenString());
  }

  @Override
  public int hashCode() {
    return getTokenString().hashCode();
  }

  @Override
  public String toString() {
    return getTokenString() + (isLeaf() ? (char) LEAF_BYTE : "");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1312.java