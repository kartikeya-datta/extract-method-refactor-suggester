error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1014.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1014.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1014.java
text:
```scala
r@@eturn grid.getLevelForDistance(degrees);

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

import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.Rectangle;
import com.spatial4j.core.shape.Shape;
import com.spatial4j.core.util.GeohashUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * A SpatialPrefixGrid based on Geohashes.  Uses {@link GeohashUtils} to do all the geohash work.
 *
 * @lucene.experimental
 */
public class GeohashPrefixTree extends SpatialPrefixTree {

  public static class Factory extends SpatialPrefixTreeFactory {

    @Override
    protected int getLevelForDistance(double degrees) {
      GeohashPrefixTree grid = new GeohashPrefixTree(ctx, GeohashPrefixTree.getMaxLevelsPossible());
      return grid.getLevelForDistance(degrees) + 1;//returns 1 greater
    }

    @Override
    protected SpatialPrefixTree newSPT() {
      return new GeohashPrefixTree(ctx,
          maxLevels != null ? maxLevels : GeohashPrefixTree.getMaxLevelsPossible());
    }
  }

  public GeohashPrefixTree(SpatialContext ctx, int maxLevels) {
    super(ctx, maxLevels);
    Rectangle bounds = ctx.getWorldBounds();
    if (bounds.getMinX() != -180)
      throw new IllegalArgumentException("Geohash only supports lat-lon world bounds. Got "+bounds);
    int MAXP = getMaxLevelsPossible();
    if (maxLevels <= 0 || maxLevels > MAXP)
      throw new IllegalArgumentException("maxLen must be [1-"+MAXP+"] but got "+ maxLevels);
  }

  /** Any more than this and there's no point (double lat & lon are the same). */
  public static int getMaxLevelsPossible() {
    return GeohashUtils.MAX_PRECISION;
  }

  @Override
  public int getLevelForDistance(double dist) {
    final int level = lookupHashLenForWidthHeight(dist, dist);
    return Math.max(Math.min(level, maxLevels), 1);
  }

  /* TODO temporarily in-lined GeoHashUtils.lookupHashLenForWidthHeight() is fixed in Spatial4j 0.3 */

  /**
   * Return the longest geohash length that will have a width & height >= specified arguments.
   */
  private static int lookupHashLenForWidthHeight(double lonErr, double latErr) {
    //loop through hash length arrays from beginning till we find one.
    for(int len = 1; len <= GeohashUtils.MAX_PRECISION; len++) {
      double latHeight = hashLenToLatHeight[len];
      double lonWidth = hashLenToLonWidth[len];
      if (latHeight < latErr && lonWidth < lonErr)
        return len;
    }
    return GeohashUtils.MAX_PRECISION;
  }

  /** See the table at http://en.wikipedia.org/wiki/Geohash */
  private static final double[] hashLenToLatHeight, hashLenToLonWidth;
  static {
    hashLenToLatHeight = new double[GeohashUtils.MAX_PRECISION +1];
    hashLenToLonWidth = new double[GeohashUtils.MAX_PRECISION +1];
    hashLenToLatHeight[0] = 90*2;
    hashLenToLonWidth[0] = 180*2;
    boolean even = false;
    for(int i = 1; i <= GeohashUtils.MAX_PRECISION; i++) {
      hashLenToLatHeight[i] = hashLenToLatHeight[i-1]/(even?8:4);
      hashLenToLonWidth[i] = hashLenToLonWidth[i-1]/(even?4:8);
      even = ! even;
    }
  }

  @Override
  public Node getNode(Point p, int level) {
    return new GhCell(GeohashUtils.encodeLatLon(p.getY(), p.getX(), level));//args are lat,lon (y,x)
  }

  @Override
  public Node getNode(String token) {
    return new GhCell(token);
  }

  @Override
  public Node getNode(byte[] bytes, int offset, int len) {
    return new GhCell(bytes, offset, len);
  }

  @Override
  public List<Node> getNodes(Shape shape, int detailLevel, boolean inclParents) {
    return shape instanceof Point ? super.getNodesAltPoint((Point) shape, detailLevel, inclParents) :
        super.getNodes(shape, detailLevel, inclParents);
  }

  class GhCell extends Node {
    GhCell(String token) {
      super(GeohashPrefixTree.this, token);
    }

    GhCell(byte[] bytes, int off, int len) {
      super(GeohashPrefixTree.this, bytes, off, len);
    }

    @Override
    public void reset(byte[] bytes, int off, int len) {
      super.reset(bytes, off, len);
      shape = null;
    }

    @Override
    public Collection<Node> getSubCells() {
      String[] hashes = GeohashUtils.getSubGeohashes(getGeohash());//sorted
      List<Node> cells = new ArrayList<Node>(hashes.length);
      for (String hash : hashes) {
        cells.add(new GhCell(hash));
      }
      return cells;
    }

    @Override
    public int getSubCellsSize() {
      return 32;//8x4
    }

    @Override
    public Node getSubCell(Point p) {
      return GeohashPrefixTree.this.getNode(p,getLevel()+1);//not performant!
    }

    private Shape shape;//cache

    @Override
    public Shape getShape() {
      if (shape == null) {
        shape = GeohashUtils.decodeBoundary(getGeohash(), ctx);
      }
      return shape;
    }

    @Override
    public Point getCenter() {
      return GeohashUtils.decode(getGeohash(), ctx);
    }

    private String getGeohash() {
      return getTokenString();
    }

  }//class GhCell

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1014.java