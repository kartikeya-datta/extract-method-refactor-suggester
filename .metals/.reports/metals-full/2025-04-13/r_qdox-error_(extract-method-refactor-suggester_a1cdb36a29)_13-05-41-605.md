error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/87.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/87.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/87.java
text:
```scala
r@@eturn distance / 10000.0;

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.lucene.util;

import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.GeoUtils;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.test.ElasticsearchTestCase;
import org.junit.Test;

import static org.hamcrest.number.IsCloseTo.closeTo;

public class SloppyMathTests extends ElasticsearchTestCase {

    @Test
    public void testAccuracy() {
        for (double lat1 = -90; lat1 <= 90; lat1+=1) {
            final double lon1 = randomLongitude();

            for (double i = -180; i <= 180; i+=1) {
                final double lon2 = i;
                final double lat2 = randomLatitude();

                assertAccurate(lat1, lon1, lat2, lon2);
            }
        }
    }

    @Test
    public void testSloppyMath() {
        assertThat(GeoDistance.ARC.calculate(-46.645, -171.057, -46.644, -171.058, DistanceUnit.METERS), closeTo(134.87709, maxError(134.87709)));
        assertThat(GeoDistance.ARC.calculate(-77.912, -81.173, -77.912, -81.171, DistanceUnit.METERS), closeTo(46.57161, maxError(46.57161)));
        assertThat(GeoDistance.ARC.calculate(65.75, -20.708, 65.75, -20.709, DistanceUnit.METERS), closeTo(45.66996, maxError(45.66996)));
        assertThat(GeoDistance.ARC.calculate(-86.9, 53.738, -86.9, 53.741, DistanceUnit.METERS), closeTo(18.03998, maxError(18.03998)));
        assertThat(GeoDistance.ARC.calculate(89.041, 115.93, 89.04, 115.946, DistanceUnit.METERS), closeTo(115.11711, maxError(115.11711)));
        
        testSloppyMath(DistanceUnit.METERS, 0.01, 5, 45, 90);
        testSloppyMath(DistanceUnit.KILOMETERS, 0.01, 5, 45, 90);
        testSloppyMath(DistanceUnit.INCH, 0.01, 5, 45, 90);
        testSloppyMath(DistanceUnit.MILES, 0.01, 5, 45, 90);
    }

    private static double maxError(double distance) {
        return distance / 1000.0;
    }
    
    private void testSloppyMath(DistanceUnit unit, double...deltaDeg) {
        final double lat1 = randomLatitude();
        final double lon1 = randomLongitude();
        logger.info("testing SloppyMath with {} at \"{}, {}\"", unit, lat1, lon1);

        GeoDistance.ArcFixedSourceDistance src = new GeoDistance.ArcFixedSourceDistance(lat1, lon1, unit); 

        for (int test = 0; test < deltaDeg.length; test++) {
            for (int i = 0; i < 100; i++) {
                final double lon2 = lon1 + (randomDouble() - 0.5) * 2 * deltaDeg[test];
                final double lat2 = lat1 + (randomDouble() - 0.5) * 2 * deltaDeg[test];
    
                final double accurate = unit.fromMeters(accurateHaversin(lat1, lon1, lat2, lon2));
                final double dist1 = GeoDistance.ARC.calculate(lat1, lon1, lat2, lon2, unit);
                final double dist2 = src.calculate(lat2, lon2);
    
                assertThat("distance between("+lat1+", "+lon1+") and ("+lat2+", "+lon2+"))", dist1, closeTo(accurate, maxError(accurate)));
                assertThat("distance between("+lat1+", "+lon1+") and ("+lat2+", "+lon2+"))", dist2, closeTo(accurate, maxError(accurate)));
            }
        }
    }
    
    // Slow but accurate implementation of the haversin function
    private static double accurateHaversin(double lat1, double lon1, double lat2, double lon2) {
        double longitudeDifference = lon2 - lon1;
        double a = Math.toRadians(90D - lat1);
        double c = Math.toRadians(90D - lat2);
        double factor = (Math.cos(a) * Math.cos(c)) + (Math.sin(a) * Math.sin(c) * Math.cos(Math.toRadians(longitudeDifference)));

        if (factor < -1D) {
            return Math.PI * GeoUtils.EARTH_MEAN_RADIUS;
        } else if (factor >= 1D) {
            return 0;
        } else {
            return Math.acos(factor) * GeoUtils.EARTH_MEAN_RADIUS;
        }
    }
    
    private static void assertAccurate(double lat1, double lon1, double lat2, double lon2) {
        double accurate = accurateHaversin(lat1, lon1, lat2, lon2);
        double sloppy = GeoDistance.ARC.calculate(lat1, lon1, lat2, lon2, DistanceUnit.METERS);
        assertThat("distance between("+lat1+", "+lon1+") and ("+lat2+", "+lon2+"))", sloppy, closeTo(accurate, maxError(accurate)));
    }

    private static final double randomLatitude() {
        return (getRandom().nextDouble() - 0.5) * 180d;
    }

    private static final double randomLongitude() {
        return (getRandom().nextDouble() - 0.5) * 360d;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/87.java