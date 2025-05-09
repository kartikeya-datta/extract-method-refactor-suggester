error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2481.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2481.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2481.java
text:
```scala
a@@ssertQ(req("fl", "*,score", "indent", "true", "q", qry.toString()),

package org.apache.solr.schema;
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

import org.apache.lucene.document.Fieldable;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.spatial.tier.CartesianPolyFilterBuilder;
import org.apache.lucene.spatial.tier.Shape;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.core.SolrCore;
import org.apache.solr.common.SolrException;
import org.apache.solr.search.function.ValueSource;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;


/**
 * Test a whole slew of things related to PolyFields
 */
public class PolyFieldTest extends SolrTestCaseJ4 {
  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig.xml","schema.xml");
  }

  @Test
  public void testSchemaBasics() throws Exception {
    IndexSchema schema = h.getCore().getSchema();


    SchemaField home = schema.getField("home");
    assertNotNull(home);
    assertTrue(home.isPolyField());

    SchemaField[] dynFields = schema.getDynamicFieldPrototypes();
    boolean seen = false;
    for (SchemaField dynField : dynFields) {
      if (dynField.getName().equals("*" + FieldType.POLY_FIELD_SEPARATOR + "double")) {
        seen = true;
      }
    }
    assertTrue("Didn't find the expected dynamic field", seen);
    FieldType homeFT = schema.getFieldType("home");
    assertEquals(home.getType(), homeFT);
    FieldType xy = schema.getFieldTypeByName("xy");
    assertNotNull(xy);
    assertTrue(xy instanceof PointType);
    assertTrue(xy.isPolyField());
    home = schema.getFieldOrNull("home_0" + FieldType.POLY_FIELD_SEPARATOR + "double");
    assertNotNull(home);
    home = schema.getField("home");
    assertNotNull(home);

    home = schema.getField("homed");//sub field suffix
    assertNotNull(home);
    assertTrue(home.isPolyField());
  }

  @Test
  public void testPointFieldType() throws Exception {
    SolrCore core = h.getCore();
    IndexSchema schema = core.getSchema();
    SchemaField home = schema.getField("home");
    assertNotNull(home);
    assertTrue("home is not a poly field", home.isPolyField());
    FieldType tmp = home.getType();
    assertTrue(tmp instanceof PointType);
    PointType pt = (PointType) tmp;
    assertEquals(pt.getDimension(), 2);
    double[] xy = new double[]{35.0, -79.34};
    String point = xy[0] + "," + xy[1];
    Fieldable[] fields = home.createFields(point, 2);
    assertEquals(fields.length, 3);//should be 3, we have a stored field
    //first two fields contain the values, third is just stored and contains the original
    for (int i = 0; i < 3; i++) {
      boolean hasValue = fields[1].tokenStreamValue() != null
 fields[1].getBinaryValue() != null
 fields[1].stringValue() != null;
      assertTrue("Doesn't have a value: " + fields[1], hasValue);
    }
    /*assertTrue("first field " + fields[0].tokenStreamValue() +  " is not 35.0", pt.getSubType().toExternal(fields[0]).equals(String.valueOf(xy[0])));
    assertTrue("second field is not -79.34", pt.getSubType().toExternal(fields[1]).equals(String.valueOf(xy[1])));
    assertTrue("third field is not '35.0,-79.34'", pt.getSubType().toExternal(fields[2]).equals(point));*/


    home = schema.getField("home_ns");
    assertNotNull(home);
    fields = home.createFields(point, 2);
    assertEquals(fields.length, 2);//should be 2, since we aren't storing

    home = schema.getField("home_ns");
    assertNotNull(home);
    try {
      fields = home.createFields("35.0,foo", 2);
      assertTrue(false);
    } catch (Exception e) {
      //
    }

    //
    SchemaField s1 = schema.getField("test_p");
    SchemaField s2 = schema.getField("test_p");
    ValueSource v1 = s1.getType().getValueSource(s1, null);
    ValueSource v2 = s2.getType().getValueSource(s2, null);
    assertEquals(v1, v2);
    assertEquals(v1.hashCode(), v2.hashCode());
  }

  @Test
  public void testSearching() throws Exception {
    for (int i = 0; i < 50; i++) {
      assertU(adoc("id", "" + i, "home", i + "," + (i * 100), "homed", (i * 1000) + "," + (i * 10000)));
    }
    assertU(commit());

    assertQ(req("fl", "*,score", "q", "*:*"), "//*[@numFound='50']");
    assertQ(req("fl", "*,score", "q", "home:1,100"),
            "//*[@numFound='1']",
            "//str[@name='home'][.='1,100']");
    assertQ(req("fl", "*,score", "q", "homed:1000,10000"),
            "//*[@numFound='1']",
            "//str[@name='homed'][.='1000,10000']");
    assertQ(req("fl", "*,score", "q",
            "{!func}sqedist(home, vector(0, 0))"),
            "\"//*[@numFound='50']\"");
    assertQ(req("fl", "*,score", "q",
            "{!func}dist(2, home, vector(0, 0))"),
            "\"//*[@numFound='50']\"");

    assertQ(req("fl", "*,score", "q",
            "home:[10,10000 TO 30,30000]"),
            "\"//*[@numFound='3']\"");
    assertQ(req("fl", "*,score", "q",
            "homed:[1,1000 TO 2000,35000]"),
            "\"//*[@numFound='2']\"");
    //bad

    ignoreException("dimension");
    assertQEx("Query should throw an exception due to incorrect dimensions", req("fl", "*,score", "q",
            "homed:[1 TO 2000]"), SolrException.ErrorCode.BAD_REQUEST);
    resetExceptionIgnores();
    clearIndex();
  }

  @Test
  public void testSearchDetails() throws Exception {
    SolrCore core = h.getCore();
    IndexSchema schema = core.getSchema();
    double[] xy = new double[]{35.0, -79.34};
    String point = xy[0] + "," + xy[1];
    //How about some queries?
    //don't need a parser for this path currently.  This may change
    assertU(adoc("id", "0", "home_ns", point));
    assertU(commit());
    SchemaField home = schema.getField("home_ns");
    PointType pt = (PointType) home.getType();
    assertEquals(pt.getDimension(), 2);
    Query q = pt.getFieldQuery(null, home, point);
    assertNotNull(q);
    assertTrue(q instanceof BooleanQuery);
    //should have two clauses, one for 35.0 and the other for -79.34
    BooleanQuery bq = (BooleanQuery) q;
    BooleanClause[] clauses = bq.getClauses();
    assertEquals(clauses.length, 2);
    clearIndex();
  }

  @Test
  public void testCartesian() throws Exception {
    for (int i = 40; i < 50; i++) {
      for (int j = -85; j < -79; j++) {
        assertU(adoc("id", "" + i, "home_tier",
                i + "," + j));
      }
    }
    assertU(commit());
    CartesianPolyFilterBuilder cpfb = new CartesianPolyFilterBuilder("", 4, 15);
    //Get the box based on this point and our distance
    final Shape shape = cpfb.getBoxShape(45, -80, 10);//There's a bit of a bug in here that requires a small tier filter here.
    final List<Double> boxIds = shape.getArea();
    //do a box id search
    StringBuilder qry = new StringBuilder();
    boolean first = true;
    for (Double boxId : boxIds) {
      if (first == true){
        first = false;
      } else {
        qry.append(" OR ");
      }
      qry.append("home_tier:");
      if (boxId < 0) {
        qry.append('\\').append(boxId);
      } else {
        qry.append(boxId);
      }
    }

    assertQ(req("fl", "*,score", "q", qry.toString()),
            "//*[@numFound='1']");
    clearIndex();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2481.java