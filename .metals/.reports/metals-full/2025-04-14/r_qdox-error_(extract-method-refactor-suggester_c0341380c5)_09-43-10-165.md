error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/497.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/497.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/497.java
text:
```scala
w@@riter.writeStr(name, f.stringValue(), true);

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

package org.apache.solr.schema;

import org.apache.lucene.document.FieldType;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.valuesource.VectorValueSource;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import com.spatial4j.core.io.ParseUtils;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.exception.InvalidShapeException;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.response.TextResponseWriter;
import org.apache.solr.search.QParser;
import org.apache.solr.search.SpatialOptions;

import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * A point type that indexes a point in an n-dimensional space as separate fields and supports range queries.
 * See {@link LatLonType} for geo-spatial queries.
 */
public class PointType extends CoordinateFieldType implements SpatialQueryable {

  @Override
  protected void init(IndexSchema schema, Map<String, String> args) {
    SolrParams p = new MapSolrParams(args);
    dimension = p.getInt(DIMENSION, DEFAULT_DIMENSION);
    if (dimension < 1) {
      throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
              "The dimension must be > 0: " + dimension);
    }
    args.remove(DIMENSION);
    this.schema = schema;
    super.init(schema, args);

    // cache suffixes
    createSuffixCache(dimension);
  }


  @Override
  public boolean isPolyField() {
    return true;   // really only true if the field is indexed
  }

  @Override
  public IndexableField[] createFields(SchemaField field, Object value, float boost) {
    String externalVal = value.toString();
    String[] point = new String[0];
    try {
      point = ParseUtils.parsePoint(null, externalVal, dimension);
    } catch (InvalidShapeException e) {
      throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, e);
    }

    // TODO: this doesn't currently support polyFields as sub-field types
    IndexableField[] f = new IndexableField[ (field.indexed() ? dimension : 0) + (field.stored() ? 1 : 0) ];

    if (field.indexed()) {
      for (int i=0; i<dimension; i++) {
        SchemaField sf = subField(field, i);
        f[i] = sf.createField(point[i], sf.indexed() && !sf.omitNorms() ? boost : 1f);
      }
    }

    if (field.stored()) {
      String storedVal = externalVal;  // normalize or not?
      FieldType customType = new FieldType();
      customType.setStored(true);
      f[f.length - 1] = createField(field.getName(), storedVal, customType, 1f);
    }
    
    return f;
  }

  @Override
  public ValueSource getValueSource(SchemaField field, QParser parser) {
    ArrayList<ValueSource> vs = new ArrayList<ValueSource>(dimension);
    for (int i=0; i<dimension; i++) {
      SchemaField sub = subField(field, i);
      vs.add(sub.getType().getValueSource(sub, parser));
    }
    return new PointTypeValueSource(field, vs);
  }


  /**
   * It never makes sense to create a single field, so make it impossible to happen by
   * throwing UnsupportedOperationException
   *
   */
  @Override
  public IndexableField createField(SchemaField field, Object value, float boost) {
    throw new UnsupportedOperationException("PointType uses multiple fields.  field=" + field.getName());
  }

  @Override
  public void write(TextResponseWriter writer, String name, IndexableField f) throws IOException {
    writer.writeStr(name, f.stringValue(), false);
  }

  @Override
  public SortField getSortField(SchemaField field, boolean top) {
    throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "Sorting not supported on PointType " + field.getName());
  }

  @Override
  /**
   * Care should be taken in calling this with higher order dimensions for performance reasons.
   */
  public Query getRangeQuery(QParser parser, SchemaField field, String part1, String part2, boolean minInclusive, boolean maxInclusive) {
    //Query could look like: [x1,y1 TO x2,y2] for 2 dimension, but could look like: [x1,y1,z1 TO x2,y2,z2], and can be extrapolated to n-dimensions
    //thus, this query essentially creates a box, cube, etc.
    String[] p1;
    String[] p2;
    try {
      p1 = ParseUtils.parsePoint(null, part1, dimension);
      p2 = ParseUtils.parsePoint(null, part2, dimension);
    } catch (InvalidShapeException e) {
      throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, e);
    }
    BooleanQuery result = new BooleanQuery(true);
    for (int i = 0; i < dimension; i++) {
      SchemaField subSF = subField(field, i);
      // points must currently be ordered... should we support specifying any two opposite corner points?
      result.add(subSF.getType().getRangeQuery(parser, subSF, p1[i], p2[i], minInclusive, maxInclusive), BooleanClause.Occur.MUST);
    }
    return result;
  }

  @Override
  public Query getFieldQuery(QParser parser, SchemaField field, String externalVal) {
    String[] p1 = new String[0];
    try {
      p1 = ParseUtils.parsePoint(null, externalVal, dimension);
    } catch (InvalidShapeException e) {
      throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, e);
    }
    //TODO: should we assert that p1.length == dimension?
    BooleanQuery bq = new BooleanQuery(true);
    for (int i = 0; i < dimension; i++) {
      SchemaField sf = subField(field, i);
      Query tq = sf.getType().getFieldQuery(parser, sf, p1[i]);
      bq.add(tq, BooleanClause.Occur.MUST);
    }
    return bq;
  }

  /**
   * Calculates the range and creates a RangeQuery (bounding box) wrapped in a BooleanQuery (unless the dimension is 1, one range for every dimension, AND'd together by a Boolean
   * @param parser The parser
   * @param options The {@link org.apache.solr.search.SpatialOptions} for this filter.
   * @return The Query representing the bounding box around the point.
   */
  public Query createSpatialQuery(QParser parser, SpatialOptions options) {
    Query result = null;
    double [] point = new double[0];
    try {
      point = ParseUtils.parsePointDouble(null, options.pointStr, dimension);
    } catch (InvalidShapeException e) {
      throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, e);
    }
    if (dimension == 1){
      //TODO: Handle distance measures
      String lower = String.valueOf(point[0] - options.distance);
      String upper = String.valueOf(point[0] + options.distance);
      SchemaField subSF = subField(options.field, 0);
      // points must currently be ordered... should we support specifying any two opposite corner points?
      result = subSF.getType().getRangeQuery(parser, subSF, lower, upper, true, true);
    } else {
      BooleanQuery tmp = new BooleanQuery();
      //TODO: Handle distance measures, as this assumes Euclidean
      double [] ur = DistanceUtils.vectorBoxCorner(point, null, options.distance, true);
      double [] ll = DistanceUtils.vectorBoxCorner(point, null, options.distance, false);
      for (int i = 0; i < ur.length; i++) {
        SchemaField subSF = subField(options.field, i);
        Query range = subSF.getType().getRangeQuery(parser, subSF, String.valueOf(ll[i]), String.valueOf(ur[i]), true, true);
        tmp.add(range, BooleanClause.Occur.MUST);

      }
      result = tmp;
    }
    return result;
  }
}


class PointTypeValueSource extends VectorValueSource {
  private final SchemaField sf;
  
  public PointTypeValueSource(SchemaField sf, List<ValueSource> sources) {
    super(sources);
    this.sf = sf;
  }

  @Override
  public String name() {
    return "point";
  }

  @Override
  public String description() {
    return name()+"("+sf.getName()+")";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/497.java