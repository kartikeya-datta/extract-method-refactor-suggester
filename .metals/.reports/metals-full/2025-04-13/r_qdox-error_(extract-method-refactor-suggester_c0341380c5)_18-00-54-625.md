error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3359.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3359.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3359.java
text:
```scala
a@@ssertNull(filter.getDocIdSet((AtomicReaderContext) reader.getTopReaderContext(), reader.getLiveDocs()));

package org.apache.lucene.queryparser.xml.builders;

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

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexReader.AtomicReaderContext;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.SlowMultiReaderWrapper;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.queryparser.xml.ParserException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestNumericRangeFilterBuilder extends LuceneTestCase {

  public void testGetFilterHandleNumericParseErrorStrict() throws Exception {
    NumericRangeFilterBuilder filterBuilder = new NumericRangeFilterBuilder();
    filterBuilder.setStrictMode(true);

    String xml = "<NumericRangeFilter fieldName='AGE' type='int' lowerTerm='-1' upperTerm='NaN'/>";
    Document doc = getDocumentFromString(xml);
    try {
      filterBuilder.getFilter(doc.getDocumentElement());
    } catch (ParserException e) {
      return;
    }
    fail("Expected to throw " + ParserException.class);
  }

  public void testGetFilterHandleNumericParseError() throws Exception {
    NumericRangeFilterBuilder filterBuilder = new NumericRangeFilterBuilder();
    filterBuilder.setStrictMode(false);

    String xml = "<NumericRangeFilter fieldName='AGE' type='int' lowerTerm='-1' upperTerm='NaN'/>";
    Document doc = getDocumentFromString(xml);
    Filter filter = filterBuilder.getFilter(doc.getDocumentElement());
    Directory ramDir = newDirectory();
    IndexWriter writer = new IndexWriter(ramDir, newIndexWriterConfig(TEST_VERSION_CURRENT, null));
    writer.commit();
    try {
      IndexReader reader = new SlowMultiReaderWrapper(IndexReader.open(ramDir, true));
      try {
        assertNull(filter.getDocIdSet((AtomicReaderContext) reader.getTopReaderContext()));
      }
      finally {
        reader.close();
      }
    }
    finally {
      writer.commit();
      writer.close();
      ramDir.close();
    }
  }

  @SuppressWarnings("unchecked")
  public void testGetFilterInt() throws Exception {
    NumericRangeFilterBuilder filterBuilder = new NumericRangeFilterBuilder();
    filterBuilder.setStrictMode(true);

    String xml = "<NumericRangeFilter fieldName='AGE' type='int' lowerTerm='-1' upperTerm='10'/>";
    Document doc = getDocumentFromString(xml);
    Filter filter = filterBuilder.getFilter(doc.getDocumentElement());
    assertTrue(filter instanceof NumericRangeFilter<?>);

    NumericRangeFilter<Integer> numRangeFilter = (NumericRangeFilter<Integer>) filter;
    assertEquals(Integer.valueOf(-1), numRangeFilter.getMin());
    assertEquals(Integer.valueOf(10), numRangeFilter.getMax());
    assertEquals("AGE", numRangeFilter.getField());
    assertTrue(numRangeFilter.includesMin());
    assertTrue(numRangeFilter.includesMax());

    String xml2 = "<NumericRangeFilter fieldName='AGE' type='int' lowerTerm='-1' upperTerm='10' includeUpper='false'/>";
    Document doc2 = getDocumentFromString(xml2);
    Filter filter2 = filterBuilder.getFilter(doc2.getDocumentElement());
    assertTrue(filter2 instanceof NumericRangeFilter<?>);

    NumericRangeFilter<Integer> numRangeFilter2 = (NumericRangeFilter) filter2;
    assertEquals(Integer.valueOf(-1), numRangeFilter2.getMin());
    assertEquals(Integer.valueOf(10), numRangeFilter2.getMax());
    assertEquals("AGE", numRangeFilter2.getField());
    assertTrue(numRangeFilter2.includesMin());
    assertFalse(numRangeFilter2.includesMax());
  }

  @SuppressWarnings("unchecked")
  public void testGetFilterLong() throws Exception {
    NumericRangeFilterBuilder filterBuilder = new NumericRangeFilterBuilder();
    filterBuilder.setStrictMode(true);

    String xml = "<NumericRangeFilter fieldName='AGE' type='LoNg' lowerTerm='-2321' upperTerm='60000000'/>";
    Document doc = getDocumentFromString(xml);
    Filter filter = filterBuilder.getFilter(doc.getDocumentElement());
    assertTrue(filter instanceof NumericRangeFilter<?>);

    NumericRangeFilter<Long> numRangeFilter = (NumericRangeFilter) filter;
    assertEquals(Long.valueOf(-2321L), numRangeFilter.getMin());
    assertEquals(Long.valueOf(60000000L), numRangeFilter.getMax());
    assertEquals("AGE", numRangeFilter.getField());
    assertTrue(numRangeFilter.includesMin());
    assertTrue(numRangeFilter.includesMax());

    String xml2 = "<NumericRangeFilter fieldName='AGE' type='LoNg' lowerTerm='-2321' upperTerm='60000000' includeUpper='false'/>";
    Document doc2 = getDocumentFromString(xml2);
    Filter filter2 = filterBuilder.getFilter(doc2.getDocumentElement());
    assertTrue(filter2 instanceof NumericRangeFilter<?>);
    NumericRangeFilter<Long> numRangeFilter2 = (NumericRangeFilter) filter2;
    assertEquals(Long.valueOf(-2321L), numRangeFilter2.getMin());
    assertEquals(Long.valueOf(60000000L), numRangeFilter2.getMax());
    assertEquals("AGE", numRangeFilter2.getField());
    assertTrue(numRangeFilter2.includesMin());
    assertFalse(numRangeFilter2.includesMax());
  }

  @SuppressWarnings("unchecked")
  public void testGetFilterDouble() throws Exception {
    NumericRangeFilterBuilder filterBuilder = new NumericRangeFilterBuilder();
    filterBuilder.setStrictMode(true);

    String xml = "<NumericRangeFilter fieldName='AGE' type='doubLe' lowerTerm='-23.21' upperTerm='60000.00023'/>";
    Document doc = getDocumentFromString(xml);

    Filter filter = filterBuilder.getFilter(doc.getDocumentElement());
    assertTrue(filter instanceof NumericRangeFilter<?>);

    NumericRangeFilter<Double> numRangeFilter = (NumericRangeFilter) filter;
    assertEquals(Double.valueOf(-23.21d), numRangeFilter.getMin());
    assertEquals(Double.valueOf(60000.00023d), numRangeFilter.getMax());
    assertEquals("AGE", numRangeFilter.getField());
    assertTrue(numRangeFilter.includesMin());
    assertTrue(numRangeFilter.includesMax());

    String xml2 = "<NumericRangeFilter fieldName='AGE' type='doubLe' lowerTerm='-23.21' upperTerm='60000.00023' includeUpper='false'/>";
    Document doc2 = getDocumentFromString(xml2);
    Filter filter2 = filterBuilder.getFilter(doc2.getDocumentElement());
    assertTrue(filter2 instanceof NumericRangeFilter<?>);

    NumericRangeFilter<Double> numRangeFilter2 = (NumericRangeFilter) filter2;
    assertEquals(Double.valueOf(-23.21d), numRangeFilter2.getMin());
    assertEquals(Double.valueOf(60000.00023d), numRangeFilter2.getMax());
    assertEquals("AGE", numRangeFilter2.getField());
    assertTrue(numRangeFilter2.includesMin());
    assertFalse(numRangeFilter2.includesMax());
  }

  @SuppressWarnings("unchecked")
  public void testGetFilterFloat() throws Exception {
    NumericRangeFilterBuilder filterBuilder = new NumericRangeFilterBuilder();
    filterBuilder.setStrictMode(true);

    String xml = "<NumericRangeFilter fieldName='AGE' type='FLOAT' lowerTerm='-2.321432' upperTerm='32432.23'/>";
    Document doc = getDocumentFromString(xml);

    Filter filter = filterBuilder.getFilter(doc.getDocumentElement());
    assertTrue(filter instanceof NumericRangeFilter<?>);

    NumericRangeFilter<Float> numRangeFilter = (NumericRangeFilter) filter;
    assertEquals(Float.valueOf(-2.321432f), numRangeFilter.getMin());
    assertEquals(Float.valueOf(32432.23f), numRangeFilter.getMax());
    assertEquals("AGE", numRangeFilter.getField());
    assertTrue(numRangeFilter.includesMin());
    assertTrue(numRangeFilter.includesMax());

    String xml2 = "<NumericRangeFilter fieldName='AGE' type='FLOAT' lowerTerm='-2.321432' upperTerm='32432.23' includeUpper='false' precisionStep='2' />";
    Document doc2 = getDocumentFromString(xml2);

    Filter filter2 = filterBuilder.getFilter(doc2.getDocumentElement());
    assertTrue(filter2 instanceof NumericRangeFilter<?>);
    
    NumericRangeFilter<Float> numRangeFilter2 = (NumericRangeFilter) filter2;
    assertEquals(Float.valueOf(-2.321432f), numRangeFilter2.getMin());
    assertEquals(Float.valueOf(32432.23f), numRangeFilter2.getMax());
    assertEquals("AGE", numRangeFilter2.getField());
    assertTrue(numRangeFilter2.includesMin());
    assertFalse(numRangeFilter2.includesMax());
  }

  private static Document getDocumentFromString(String str)
      throws SAXException, IOException, ParserConfigurationException {
    InputStream is = new ByteArrayInputStream(str.getBytes());
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(is);
    is.close();
    return doc;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3359.java