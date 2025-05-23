error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/316.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/316.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/316.java
text:
```scala
e@@xecuteQueries(SpatialMatchConcern.FILTER, QTEST_Cities_Intersects_BBox);

package org.apache.lucene.spatial.prefix;

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

import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.io.GeohashUtils;
import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.Rectangle;
import com.spatial4j.core.shape.Shape;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.spatial.SpatialMatchConcern;
import org.apache.lucene.spatial.StrategyTestCase;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialOperation;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestRecursivePrefixTreeStrategy extends StrategyTestCase {

  private int maxLength;

  //Tests should call this first.
  private void init(int maxLength) {
    this.maxLength = maxLength;
    this.ctx = SpatialContext.GEO;
    GeohashPrefixTree grid = new GeohashPrefixTree(ctx, maxLength);
    this.strategy = new RecursivePrefixTreeStrategy(grid, getClass().getSimpleName());
  }

  @Test
  public void testFilterWithVariableScanLevel() throws IOException {
    init(GeohashPrefixTree.getMaxLevelsPossible());
    getAddAndVerifyIndexedDocuments(DATA_WORLD_CITIES_POINTS);

    //execute queries for each prefix grid scan level
    for(int i = 0; i <= maxLength; i++) {
      ((RecursivePrefixTreeStrategy)strategy).setPrefixGridScanLevel(i);
      executeQueries(SpatialMatchConcern.FILTER, QTEST_Cities_IsWithin_BBox);
    }
  }

  @Test
  public void testOneMeterPrecision() {
    init(GeohashPrefixTree.getMaxLevelsPossible());
    GeohashPrefixTree grid = (GeohashPrefixTree) ((RecursivePrefixTreeStrategy) strategy).getGrid();
    //DWS: I know this to be true.  11 is needed for one meter
    double degrees = DistanceUtils.dist2Degrees(0.001, DistanceUtils.EARTH_MEAN_RADIUS_KM);
    assertEquals(11, grid.getLevelForDistance(degrees));
  }

  @Test
  public void testPrecision() throws IOException{
    init(GeohashPrefixTree.getMaxLevelsPossible());

    Point iPt = ctx.makePoint(2.8028712999999925, 48.3708044);//lon, lat
    addDocument(newDoc("iPt", iPt));
    commit();

    Point qPt = ctx.makePoint(2.4632387000000335, 48.6003516);

    final double KM2DEG = DistanceUtils.dist2Degrees(1, DistanceUtils.EARTH_MEAN_RADIUS_KM);
    final double DEG2KM = 1 / KM2DEG;

    final double DIST = 35.75;//35.7499...
    assertEquals(DIST, ctx.getDistCalc().distance(iPt, qPt) * DEG2KM, 0.001);

    //distErrPct will affect the query shape precision. The indexed precision
    // was set to nearly zilch via init(GeohashPrefixTree.getMaxLevelsPossible());
    final double distErrPct = 0.025; //the suggested default, by the way
    final double distMult = 1+distErrPct;

    assertTrue(35.74*distMult >= DIST);
    checkHits(q(qPt, 35.74 * KM2DEG, distErrPct), 1, null);

    assertTrue(30*distMult < DIST);
    checkHits(q(qPt, 30 * KM2DEG, distErrPct), 0, null);

    assertTrue(33*distMult < DIST);
    checkHits(q(qPt, 33 * KM2DEG, distErrPct), 0, null);

    assertTrue(34*distMult < DIST);
    checkHits(q(qPt, 34 * KM2DEG, distErrPct), 0, null);
  }

  @Test
  public void geohashRecursiveRandom() throws IOException {
    init(12);

    //1. Iterate test with the cluster at some worldly point of interest
    Point[] clusterCenters = new Point[]{ctx.makePoint(-180,0), ctx.makePoint(0,90), ctx.makePoint(0,-90)};
    for (Point clusterCenter : clusterCenters) {
      //2. Iterate on size of cluster (a really small one and a large one)
      String hashCenter = GeohashUtils.encodeLatLon(clusterCenter.getY(), clusterCenter.getX(), maxLength);
      //calculate the number of degrees in the smallest grid box size (use for both lat & lon)
      String smallBox = hashCenter.substring(0,hashCenter.length()-1);//chop off leaf precision
      Rectangle clusterDims = GeohashUtils.decodeBoundary(smallBox,ctx);
      double smallRadius = Math.max(clusterDims.getMaxX()-clusterDims.getMinX(),clusterDims.getMaxY()-clusterDims.getMinY());
      assert smallRadius < 1;
      double largeRadius = 20d;//good large size; don't use >=45 for this test code to work
      double[] radiusDegs = {largeRadius,smallRadius};
      for (double radiusDeg : radiusDegs) {
        //3. Index random points in this cluster circle
        deleteAll();
        List<Point> points = new ArrayList<Point>();
        for(int i = 0; i < 20; i++) {
          //Note that this will not result in randomly distributed points in the
          // circle, they will be concentrated towards the center a little. But
          // it's good enough.
          Point pt = ctx.getDistCalc().pointOnBearing(clusterCenter,
              random().nextDouble() * radiusDeg, random().nextInt() * 360, ctx, null);
          pt = alignGeohash(pt);
          points.add(pt);
          addDocument(newDoc("" + i, pt));
        }
        commit();

        //3. Use some query centers. Each is twice the cluster's radius away.
        for(int ri = 0; ri < 4; ri++) {
          Point queryCenter = ctx.getDistCalc().pointOnBearing(clusterCenter,
              radiusDeg*2, random().nextInt(360), ctx, null);
          queryCenter = alignGeohash(queryCenter);
          //4.1 Query a small box getting nothing
          checkHits(q(queryCenter, radiusDeg - smallRadius/2), 0, null);
          //4.2 Query a large box enclosing the cluster, getting everything
          checkHits(q(queryCenter, radiusDeg*3 + smallRadius/2), points.size(), null);
          //4.3 Query a medium box getting some (calculate the correct solution and verify)
          double queryDist = radiusDeg * 2;

          //Find matching points.  Put into int[] of doc ids which is the same thing as the index into points list.
          int[] ids = new int[points.size()];
          int ids_sz = 0;
          for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (ctx.getDistCalc().distance(queryCenter, point) <= queryDist)
              ids[ids_sz++] = i;
          }
          ids = Arrays.copyOf(ids, ids_sz);
          //assert ids_sz > 0 (can't because randomness keeps us from being able to)

          checkHits(q(queryCenter, queryDist), ids.length, ids);
        }

      }//for radiusDeg

    }//for clusterCenter

  }//randomTest()

  /** Query point-distance (in degrees) with zero error percent. */
  private SpatialArgs q(Point pt, double distDEG) {
    return q(pt, distDEG, 0.0);
  }

  private SpatialArgs q(Point pt, double distDEG, double distErrPct) {
    Shape shape = ctx.makeCircle(pt, distDEG);
    SpatialArgs args = new SpatialArgs(SpatialOperation.Intersects,shape);
    args.setDistErrPct(distErrPct);
    return args;
  }

  private void checkHits(SpatialArgs args, int assertNumFound, int[] assertIds) {
    SearchResults got = executeQuery(strategy.makeQuery(args), 100);
    assertEquals("" + args, assertNumFound, got.numFound);
    if (assertIds != null) {
      Set<Integer> gotIds = new HashSet<Integer>();
      for (SearchResult result : got.results) {
        gotIds.add(Integer.valueOf(result.document.get("id")));
      }
      for (int assertId : assertIds) {
        assertTrue("has "+assertId,gotIds.contains(assertId));
      }
    }
  }

  private Document newDoc(String id, Shape shape) {
    Document doc = new Document();
    doc.add(new StringField("id", id, Field.Store.YES));
    for (Field f : strategy.createIndexableFields(shape)) {
      doc.add(f);
    }
    if (storeShape)
      doc.add(new StoredField(strategy.getFieldName(), ctx.toString(shape)));
    return doc;
  }

  /** NGeohash round-trip for given precision. */
  private Point alignGeohash(Point p) {
    return GeohashUtils.decode(GeohashUtils.encodeLatLon(p.getY(), p.getX(), maxLength), ctx);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/316.java