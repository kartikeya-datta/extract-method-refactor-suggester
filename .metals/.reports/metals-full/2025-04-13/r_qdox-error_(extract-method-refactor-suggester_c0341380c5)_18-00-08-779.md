error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7648.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7648.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7648.java
text:
```scala
.@@close();

package org.elasticsearch.test.unit.common.geo;

import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.Rectangle;
import com.spatial4j.core.shape.Shape;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import org.elasticsearch.common.geo.ShapeBuilder;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Tests for {@link ShapeBuilder}
 */
public class ShapeBuilderTests {

    @Test
    public void testNewPoint() {
        Point point = ShapeBuilder.newPoint(-100, 45);
        assertEquals(-100D, point.getX());
        assertEquals(45D, point.getY());
    }

    @Test
    public void testNewRectangle() {
        Rectangle rectangle = ShapeBuilder.newRectangle().topLeft(-45, 30).bottomRight(45, -30).build();
        assertEquals(-45D, rectangle.getMinX());
        assertEquals(-30D, rectangle.getMinY());
        assertEquals(45D, rectangle.getMaxX());
        assertEquals(30D, rectangle.getMaxY());
    }

    @Test
    public void testNewPolygon() {
        Polygon polygon = ShapeBuilder.newPolygon()
                .point(-45, 30)
                .point(45, 30)
                .point(45, -30)
                .point(-45, -30)
                .point(-45, 30).toPolygon();

        LineString exterior = polygon.getExteriorRing();
        assertEquals(exterior.getCoordinateN(0), new Coordinate(-45, 30));
        assertEquals(exterior.getCoordinateN(1), new Coordinate(45, 30));
        assertEquals(exterior.getCoordinateN(2), new Coordinate(45, -30));
        assertEquals(exterior.getCoordinateN(3), new Coordinate(-45, -30));
    }

    @Test
    public void testToJTSGeometry() {
        ShapeBuilder.PolygonBuilder polygonBuilder = ShapeBuilder.newPolygon()
                .point(-45, 30)
                .point(45, 30)
                .point(45, -30)
                .point(-45, -30)
                .point(-45, 30);

        Shape polygon = polygonBuilder.build();
        Geometry polygonGeometry = ShapeBuilder.toJTSGeometry(polygon);
        assertEquals(polygonBuilder.toPolygon(), polygonGeometry);

        Rectangle rectangle = ShapeBuilder.newRectangle().topLeft(-45, 30).bottomRight(45, -30).build();
        Geometry rectangleGeometry = ShapeBuilder.toJTSGeometry(rectangle);
        assertEquals(rectangleGeometry, polygonGeometry);

        Point point = ShapeBuilder.newPoint(-45, 30);
        Geometry pointGeometry = ShapeBuilder.toJTSGeometry(point);
        assertEquals(pointGeometry.getCoordinate(), new Coordinate(-45, 30));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7648.java