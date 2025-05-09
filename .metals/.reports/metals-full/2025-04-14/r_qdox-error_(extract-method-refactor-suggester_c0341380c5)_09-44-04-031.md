error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7261.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7261.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7261.java
text:
```scala
A@@rrayList<Coordinate[]> parts = new ArrayList<>();

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

package org.elasticsearch.common.geo.builders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.spatial4j.core.shape.ShapeCollection;
import org.elasticsearch.common.xcontent.XContentBuilder;

import com.spatial4j.core.shape.Shape;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

public abstract class BaseLineStringBuilder<E extends BaseLineStringBuilder<E>> extends PointCollection<E> {

    protected BaseLineStringBuilder() {
        this(new ArrayList<Coordinate>());
    }

    protected BaseLineStringBuilder(ArrayList<Coordinate> points) {
        super(points);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        return coordinatesToXcontent(builder, false);
    }

    @Override
    public Shape build() {
        Coordinate[] coordinates = points.toArray(new Coordinate[points.size()]);
        Geometry geometry;
        if(wrapdateline) {
            ArrayList<LineString> strings = decompose(FACTORY, coordinates, new ArrayList<LineString>());

            if(strings.size() == 1) {
                geometry = strings.get(0);
            } else {
                LineString[] linestrings = strings.toArray(new LineString[strings.size()]);
                geometry = FACTORY.createMultiLineString(linestrings);
            }

        } else {
            geometry = FACTORY.createLineString(coordinates);
        }
        return jtsGeometry(geometry);
    }

    protected static ArrayList<LineString> decompose(GeometryFactory factory, Coordinate[] coordinates, ArrayList<LineString> strings) {
        for(Coordinate[] part : decompose(+DATELINE, coordinates)) {
            for(Coordinate[] line : decompose(-DATELINE, part)) {
                strings.add(factory.createLineString(line));
            }
        }
        return strings;
    }

    /**
     * Decompose a linestring given as array of coordinates at a vertical line.
     * 
     * @param dateline x-axis intercept of the vertical line
     * @param coordinates coordinates forming the linestring
     * @return array of linestrings given as coordinate arrays 
     */
    protected static Coordinate[][] decompose(double dateline, Coordinate[] coordinates) {
        int offset = 0;
        ArrayList<Coordinate[]> parts = new ArrayList<Coordinate[]>();
        
        double shift = coordinates[0].x > DATELINE ? DATELINE : (coordinates[0].x < -DATELINE ? -DATELINE : 0);

        for (int i = 1; i < coordinates.length; i++) {
            double t = intersection(coordinates[i-1], coordinates[i], dateline);
            if(!Double.isNaN(t)) {
                Coordinate[] part;
                if(t<1) {
                    part = Arrays.copyOfRange(coordinates, offset, i+1);
                    part[part.length-1] = Edge.position(coordinates[i-1], coordinates[i], t);
                    coordinates[offset+i-1] = Edge.position(coordinates[i-1], coordinates[i], t);
                    shift(shift, part);
                    offset = i-1;
                    shift = coordinates[i].x > DATELINE ? DATELINE : (coordinates[i].x < -DATELINE ? -DATELINE : 0);
                } else {
                    part = shift(shift, Arrays.copyOfRange(coordinates, offset, i+1));
                    offset = i;
                }
                parts.add(part);
            }
        }

        if(offset == 0) {
            parts.add(shift(shift, coordinates));
        } else if(offset < coordinates.length-1) {
            Coordinate[] part = Arrays.copyOfRange(coordinates, offset, coordinates.length);
            parts.add(shift(shift, part));
        }
        return parts.toArray(new Coordinate[parts.size()][]);
    }

    private static Coordinate[] shift(double shift, Coordinate...coordinates) {
        if(shift != 0) {
            for (int j = 0; j < coordinates.length; j++) {
                coordinates[j] = new Coordinate(coordinates[j].x - 2 * shift, coordinates[j].y);
            }
        }
        return coordinates;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7261.java