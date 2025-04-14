error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9815.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9815.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9815.java
text:
```scala
r@@eturn jtsGeometry(geometry);

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

import org.elasticsearch.common.xcontent.XContentBuilder;

import com.spatial4j.core.shape.Shape;
import com.spatial4j.core.shape.jts.JtsGeometry;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class MultiLineStringBuilder extends ShapeBuilder {

    public static final GeoShapeType TYPE = GeoShapeType.MULTILINESTRING;

    private final ArrayList<BaseLineStringBuilder<?>> lines = new ArrayList<BaseLineStringBuilder<?>>();

    public InternalLineStringBuilder linestring() {
        InternalLineStringBuilder line = new InternalLineStringBuilder(this);
        this.lines.add(line);
        return line;
    }

    public MultiLineStringBuilder linestring(BaseLineStringBuilder<?> line) {
        this.lines.add(line);
        return this;
    }

    public Coordinate[][] coordinates() {
        Coordinate[][] result = new Coordinate[lines.size()][];
        for (int i = 0; i < result.length; i++) {
            result[i] = lines.get(i).coordinates(false);
        }
        return result;
    }

    @Override
    public GeoShapeType type() {
        return TYPE;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(FIELD_TYPE, TYPE.shapename);
        builder.field(FIELD_COORDINATES);
        builder.startArray();
        for(BaseLineStringBuilder<?> line : lines) {
            line.coordinatesToXcontent(builder, false);
        }
        builder.endArray();
        builder.endObject();
        return builder;
    }

    @Override
    public Shape build() {
        final Geometry geometry;
        if(wrapdateline) {
            ArrayList<LineString> parts = new ArrayList<LineString>();
            for (BaseLineStringBuilder<?> line : lines) {
                BaseLineStringBuilder.decompose(FACTORY, line.coordinates(false), parts);
            }
            if(parts.size() == 1) {
                geometry = parts.get(0);
            } else {
                LineString[] lineStrings = parts.toArray(new LineString[parts.size()]);
                geometry = FACTORY.createMultiLineString(lineStrings);
            }
        } else {
            LineString[] lineStrings = new LineString[lines.size()];
            Iterator<BaseLineStringBuilder<?>> iterator = lines.iterator();
            for (int i = 0; iterator.hasNext(); i++) {
                lineStrings[i] = FACTORY.createLineString(iterator.next().coordinates(false));
            }
            geometry = FACTORY.createMultiLineString(lineStrings);
        }
        return new JtsGeometry(geometry, SPATIAL_CONTEXT, true);
    }

    public static class InternalLineStringBuilder extends BaseLineStringBuilder<InternalLineStringBuilder> {

        private final MultiLineStringBuilder collection;
        
        public InternalLineStringBuilder(MultiLineStringBuilder collection) {
            super();
            this.collection = collection;
        }
        
        public MultiLineStringBuilder end() {
            return collection;
        }

        public Coordinate[] coordinates() {
            return super.coordinates(false);
        }

        @Override
        public GeoShapeType type() {
            return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9815.java