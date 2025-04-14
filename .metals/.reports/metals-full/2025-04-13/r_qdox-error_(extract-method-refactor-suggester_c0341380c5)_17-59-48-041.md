error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3175.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3175.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3175.java
text:
```scala
i@@ndexWriter.shutdown();

package org.apache.lucene.spatial;

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
import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.Shape;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.StoredDocument;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialArgsParser;
import org.apache.lucene.spatial.query.SpatialOperation;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.LuceneTestCase;

import java.io.IOException;

/**
 * This class serves as example code to show how to use the Lucene spatial
 * module.
 */
public class SpatialExample extends LuceneTestCase {

  //Note: Test invoked via TestTestFramework.spatialExample()

  public static void main(String[] args) throws Exception {
    new SpatialExample().test();
  }

  public void test() throws Exception {
    init();
    indexPoints();
    search();
  }

  /**
   * The Spatial4j {@link SpatialContext} is a sort of global-ish singleton
   * needed by Lucene spatial.  It's a facade to the rest of Spatial4j, acting
   * as a factory for {@link Shape}s and provides access to reading and writing
   * them from Strings.
   */
  private SpatialContext ctx;//"ctx" is the conventional variable name

  /**
   * The Lucene spatial {@link SpatialStrategy} encapsulates an approach to
   * indexing and searching shapes, and providing distance values for them.
   * It's a simple API to unify different approaches. You might use more than
   * one strategy for a shape as each strategy has its strengths and weaknesses.
   * <p />
   * Note that these are initialized with a field name.
   */
  private SpatialStrategy strategy;

  private Directory directory;

  protected void init() {
    //Typical geospatial context
    //  These can also be constructed from SpatialContextFactory
    this.ctx = SpatialContext.GEO;

    int maxLevels = 11;//results in sub-meter precision for geohash
    //TODO demo lookup by detail distance
    //  This can also be constructed from SpatialPrefixTreeFactory
    SpatialPrefixTree grid = new GeohashPrefixTree(ctx, maxLevels);

    this.strategy = new RecursivePrefixTreeStrategy(grid, "myGeoField");

    this.directory = new RAMDirectory();
  }

  private void indexPoints() throws Exception {
    IndexWriterConfig iwConfig = new IndexWriterConfig(TEST_VERSION_CURRENT,null);
    IndexWriter indexWriter = new IndexWriter(directory, iwConfig);

    //Spatial4j is x-y order for arguments
    indexWriter.addDocument(newSampleDocument(
        2, ctx.makePoint(-80.93, 33.77)));

    //Spatial4j has a WKT parser which is also "x y" order
    indexWriter.addDocument(newSampleDocument(
        4, ctx.readShapeFromWkt("POINT(60.9289094 -50.7693246)")));

    indexWriter.addDocument(newSampleDocument(
        20, ctx.makePoint(0.1,0.1), ctx.makePoint(0, 0)));

    indexWriter.close();
  }

  private Document newSampleDocument(int id, Shape... shapes) {
    Document doc = new Document();
    doc.add(new IntField("id", id, Field.Store.YES));
    //Potentially more than one shape in this field is supported by some
    // strategies; see the javadocs of the SpatialStrategy impl to see.
    for (Shape shape : shapes) {
      for (Field f : strategy.createIndexableFields(shape)) {
        doc.add(f);
      }
      //store it too; the format is up to you
      //  (assume point in this example)
      Point pt = (Point) shape;
      doc.add(new StoredField(strategy.getFieldName(), pt.getX()+" "+pt.getY()));
    }

    return doc;
  }

  private void search() throws Exception {
    IndexReader indexReader = DirectoryReader.open(directory);
    IndexSearcher indexSearcher = new IndexSearcher(indexReader);
    Sort idSort = new Sort(new SortField("id", SortField.Type.INT));

    //--Filter by circle (<= distance from a point)
    {
      //Search with circle
      //note: SpatialArgs can be parsed from a string
      SpatialArgs args = new SpatialArgs(SpatialOperation.Intersects,
          ctx.makeCircle(-80.0, 33.0, DistanceUtils.dist2Degrees(200, DistanceUtils.EARTH_MEAN_RADIUS_KM)));
      Filter filter = strategy.makeFilter(args);
      TopDocs docs = indexSearcher.search(new MatchAllDocsQuery(), filter, 10, idSort);
      assertDocMatchedIds(indexSearcher, docs, 2);
      //Now, lets get the distance for the 1st doc via computing from stored point value:
      // (this computation is usually not redundant)
      StoredDocument doc1 = indexSearcher.doc(docs.scoreDocs[0].doc);
      String doc1Str = doc1.getField(strategy.getFieldName()).stringValue();
      //assume doc1Str is "x y" as written in newSampleDocument()
      int spaceIdx = doc1Str.indexOf(' ');
      double x = Double.parseDouble(doc1Str.substring(0, spaceIdx));
      double y = Double.parseDouble(doc1Str.substring(spaceIdx+1));
      double doc1DistDEG = ctx.calcDistance(args.getShape().getCenter(), x, y);
      assertEquals(121.6d, DistanceUtils.degrees2Dist(doc1DistDEG, DistanceUtils.EARTH_MEAN_RADIUS_KM), 0.1);
      //or more simply:
      assertEquals(121.6d, doc1DistDEG * DistanceUtils.DEG_TO_KM, 0.1);
    }
    //--Match all, order by distance ascending
    {
      Point pt = ctx.makePoint(60, -50);
      ValueSource valueSource = strategy.makeDistanceValueSource(pt, DistanceUtils.DEG_TO_KM);//the distance (in km)
      Sort distSort = new Sort(valueSource.getSortField(false)).rewrite(indexSearcher);//false=asc dist
      TopDocs docs = indexSearcher.search(new MatchAllDocsQuery(), 10, distSort);
      assertDocMatchedIds(indexSearcher, docs, 4, 20, 2);
      //To get the distance, we could compute from stored values like earlier.
      // However in this example we sorted on it, and the distance will get
      // computed redundantly.  If the distance is only needed for the top-X
      // search results then that's not a big deal. Alternatively, try wrapping
      // the ValueSource with CachingDoubleValueSource then retrieve the value
      // from the ValueSource now. See LUCENE-4541 for an example.
    }
    //demo arg parsing
    {
      SpatialArgs args = new SpatialArgs(SpatialOperation.Intersects,
          ctx.makeCircle(-80.0, 33.0, 1));
      SpatialArgs args2 = new SpatialArgsParser().parse("Intersects(BUFFER(POINT(-80 33),1))", ctx);
      assertEquals(args.toString(),args2.toString());
    }

    indexReader.close();
  }

  private void assertDocMatchedIds(IndexSearcher indexSearcher, TopDocs docs, int... ids) throws IOException {
    int[] gotIds = new int[docs.totalHits];
    for (int i = 0; i < gotIds.length; i++) {
      gotIds[i] = indexSearcher.doc(docs.scoreDocs[i].doc).getField("id").numericValue().intValue();
    }
    assertArrayEquals(ids,gotIds);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3175.java