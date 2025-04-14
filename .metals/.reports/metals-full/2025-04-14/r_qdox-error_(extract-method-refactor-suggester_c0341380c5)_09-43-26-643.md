error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3284.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3284.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3284.java
text:
```scala
C@@artesianTierPlotter ctp = new CartesianTierPlotter( miles, projector, tierPrefix, minTier, maxTier );

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

package org.apache.lucene.spatial.tier;

import org.apache.lucene.search.Filter;
import org.apache.lucene.spatial.tier.projections.CartesianTierPlotter;
import org.apache.lucene.spatial.tier.projections.IProjector;
import org.apache.lucene.spatial.tier.projections.SinusoidalProjector;
import org.apache.lucene.spatial.geometry.LatLng;
import org.apache.lucene.spatial.geometry.FloatLatLng;
import org.apache.lucene.spatial.geometry.shape.LLRect;


/**
 * <p><font color="red"><b>NOTE:</b> This API is still in
 * flux and might change in incompatible ways in the next
 * release.</font>
 */
public class CartesianPolyFilterBuilder {

  // Finer granularity than 1 mile isn't accurate with
  // standard java math.  Also, there's already a 2nd
  // precise filter, if needed, in DistanceQueryBuilder,
  // that will make the filtering exact.
  public static final double MILES_FLOOR = 1.0;

  private IProjector projector = new SinusoidalProjector();
  private final String tierPrefix;
	private int minTier;
	private int maxTier;
  /**
   * 
   * @param tierPrefix The prefix for the name of the fields containing the tier info
   * @param minTierIndexed The minimum tier level indexed
   * @param maxTierIndexed The maximum tier level indexed
   */
  public CartesianPolyFilterBuilder( String tierPrefix, int minTierIndexed, int maxTierIndexed ) {
    this.tierPrefix = tierPrefix;
	this.minTier = minTierIndexed;
	this.maxTier = maxTierIndexed;
  }
  
  public Shape getBoxShape(double latitude, double longitude, double miles)
  {  
    if (miles < MILES_FLOOR) {
      miles = MILES_FLOOR;
    }
    LLRect box1 = LLRect.createBox( new FloatLatLng( latitude, longitude ), miles, miles );
    LatLng lowerLeft = box1.getLowerLeft();
    LatLng upperRight = box1.getUpperRight();

    double latUpperRight = upperRight.getLat();
    double latLowerLeft = lowerLeft.getLat();
    double longUpperRight = upperRight.getLng();
    double longLowerLeft = lowerLeft.getLng();

    CartesianTierPlotter ctp = new CartesianTierPlotter( miles, projector, tierPrefix );
    Shape shape = new Shape(ctp.getTierLevelId());

    if (longUpperRight < longLowerLeft) { // Box cross the 180 meridian
      addBoxes(shape, ctp, latLowerLeft, longLowerLeft, latUpperRight, LatLng.LONGITUDE_DEGREE_MAX);
      addBoxes(shape, ctp, latLowerLeft, -LatLng.LONGITUDE_DEGREE_MIN, latUpperRight, longUpperRight);
    } else {
      addBoxes(shape, ctp, latLowerLeft, longLowerLeft, latUpperRight, longUpperRight);
    }
 
    return shape; 
  } 
  
  private void addBoxes(Shape shape, CartesianTierPlotter tierPlotter, double lat1, double long1, double lat2, double long2) {
    double boxId1 = tierPlotter.getTierBoxId(lat1, long1);
    double boxId2 = tierPlotter.getTierBoxId(lat2, long2);

    double tierVert = tierPlotter.getTierVerticalPosDivider();

    int LongIndex1 = (int) Math.round(boxId1);
    int LatIndex1 = (int) Math.round((boxId1 - LongIndex1) * tierVert);

    int LongIndex2 = (int) Math.round(boxId2);
    int LatIndex2 = (int) Math.round((boxId2 - LongIndex2) * tierVert);

    int startLong, endLong;
    int startLat, endLat;

    if (LongIndex1 > LongIndex2) {
      startLong = LongIndex2;
      endLong = LongIndex1;
    } else {
      startLong = LongIndex1;
      endLong = LongIndex2;
    }

    if (LatIndex1 > LatIndex2) {
      startLat = LatIndex2;
      endLat = LatIndex1;
    } else {
      startLat = LatIndex1;
      endLat = LatIndex2;
    }

    int LatIndex, LongIndex;
    for (LongIndex = startLong; LongIndex <= endLong; LongIndex++) {
      for (LatIndex = startLat; LatIndex <= endLat; LatIndex++) {
        // create a boxId
        double boxId = LongIndex + LatIndex / tierVert;
        shape.addBox(boxId);
      }
    }
  }
  
  public Filter getBoundingArea(double latitude, double longitude, double miles) 
  {
    Shape shape = getBoxShape(latitude, longitude, miles);
    return new CartesianShapeFilter(shape, tierPrefix + shape.getTierId());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3284.java