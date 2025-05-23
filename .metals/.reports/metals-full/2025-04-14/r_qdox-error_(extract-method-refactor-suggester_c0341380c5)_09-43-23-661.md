error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2605.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2605.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2605.java
text:
```scala
i@@f (r.getBinaryDocValues(field) != null) {

package org.apache.lucene.facet.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.FacetTestCase;
import org.apache.lucene.facet.index.FacetFields;
import org.apache.lucene.facet.index.params.CategoryListParams;
import org.apache.lucene.facet.index.params.FacetIndexingParams;
import org.apache.lucene.facet.index.params.PerDimensionIndexingParams;
import org.apache.lucene.facet.search.params.CountFacetRequest;
import org.apache.lucene.facet.search.params.FacetRequest;
import org.apache.lucene.facet.search.params.FacetRequest.ResultMode;
import org.apache.lucene.facet.search.params.FacetSearchParams;
import org.apache.lucene.facet.search.results.FacetResult;
import org.apache.lucene.facet.search.results.FacetResultNode;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.MultiCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.IOUtils;
import org.junit.Test;

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

public class TestMultipleCategoryLists extends FacetTestCase {

  private static final CategoryPath[] CATEGORIES = new CategoryPath[] {
    new CategoryPath("Author", "Mark Twain"),
    new CategoryPath("Author", "Stephen King"),
    new CategoryPath("Author", "Kurt Vonnegut"),
    new CategoryPath("Band", "Rock & Pop", "The Beatles"),
    new CategoryPath("Band", "Punk", "The Ramones"),
    new CategoryPath("Band", "Rock & Pop", "U2"),
    new CategoryPath("Band", "Rock & Pop", "REM"),
    new CategoryPath("Band", "Rock & Pop", "Dave Matthews Band"),
    new CategoryPath("Composer", "Bach"),
  };
  
  @Test
  public void testDefault() throws Exception {
    Directory indexDir = newDirectory();
    Directory taxoDir = newDirectory();
    
    // create and open an index writer
    RandomIndexWriter iw = new RandomIndexWriter(random(), indexDir, newIndexWriterConfig(
        TEST_VERSION_CURRENT, new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false)));
    // create and open a taxonomy writer
    TaxonomyWriter tw = new DirectoryTaxonomyWriter(taxoDir, OpenMode.CREATE);

    PerDimensionIndexingParams iParams = new PerDimensionIndexingParams(Collections.<CategoryPath, CategoryListParams>emptyMap());

    seedIndex(iw, tw, iParams);

    IndexReader ir = iw.getReader();
    tw.commit();

    // prepare index reader and taxonomy.
    TaxonomyReader tr = new DirectoryTaxonomyReader(taxoDir);

    // prepare searcher to search against
    IndexSearcher searcher = newSearcher(ir);

    FacetsCollector facetsCollector = performSearch(iParams, tr, ir, searcher);

    // Obtain facets results and hand-test them
    assertCorrectResults(facetsCollector);

    assertOrdinalsExist("$facets", ir);

    IOUtils.close(tr, ir, iw, tw);
    IOUtils.close(indexDir, taxoDir);
  }

  @Test
  public void testCustom() throws Exception {
    Directory indexDir = newDirectory();
    Directory taxoDir = newDirectory();
    
    // create and open an index writer
    RandomIndexWriter iw = new RandomIndexWriter(random(), indexDir, newIndexWriterConfig(
        TEST_VERSION_CURRENT, new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false)));
    // create and open a taxonomy writer
    TaxonomyWriter tw = new DirectoryTaxonomyWriter(taxoDir, OpenMode.CREATE);

    PerDimensionIndexingParams iParams = new PerDimensionIndexingParams(
        Collections.singletonMap(new CategoryPath("Author"), new CategoryListParams("$author")));
    seedIndex(iw, tw, iParams);

    IndexReader ir = iw.getReader();
    tw.commit();

    // prepare index reader and taxonomy.
    TaxonomyReader tr = new DirectoryTaxonomyReader(taxoDir);

    // prepare searcher to search against
    IndexSearcher searcher = newSearcher(ir);

    FacetsCollector facetsCollector = performSearch(iParams, tr, ir, searcher);

    // Obtain facets results and hand-test them
    assertCorrectResults(facetsCollector);

    assertOrdinalsExist("$facets", ir);
    assertOrdinalsExist("$author", ir);

    IOUtils.close(tr, ir, iw, tw);
    IOUtils.close(indexDir, taxoDir);
  }

  @Test
  public void testTwoCustomsSameField() throws Exception {
    Directory indexDir = newDirectory();
    Directory taxoDir = newDirectory();
    
    // create and open an index writer
    RandomIndexWriter iw = new RandomIndexWriter(random(), indexDir, newIndexWriterConfig(
        TEST_VERSION_CURRENT, new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false)));
    // create and open a taxonomy writer
    TaxonomyWriter tw = new DirectoryTaxonomyWriter(taxoDir, OpenMode.CREATE);

    Map<CategoryPath,CategoryListParams> paramsMap = new HashMap<CategoryPath,CategoryListParams>();
    paramsMap.put(new CategoryPath("Band"), new CategoryListParams("$music"));
    paramsMap.put(new CategoryPath("Composer"), new CategoryListParams("$music"));
    PerDimensionIndexingParams iParams = new PerDimensionIndexingParams(paramsMap);
    seedIndex(iw, tw, iParams);

    IndexReader ir = iw.getReader();
    tw.commit();

    // prepare index reader and taxonomy.
    TaxonomyReader tr = new DirectoryTaxonomyReader(taxoDir);

    // prepare searcher to search against
    IndexSearcher searcher = newSearcher(ir);

    FacetsCollector facetsCollector = performSearch(iParams, tr, ir, searcher);

    // Obtain facets results and hand-test them
    assertCorrectResults(facetsCollector);

    assertOrdinalsExist("$facets", ir);
    assertOrdinalsExist("$music", ir);
    assertOrdinalsExist("$music", ir);

    IOUtils.close(tr, ir, iw, tw);
    IOUtils.close(indexDir, taxoDir);
  }

  private void assertOrdinalsExist(String field, IndexReader ir) throws IOException {
    for (AtomicReaderContext context : ir.leaves()) {
      AtomicReader r = context.reader();
      if (r.docValues(field) != null) {
        return; // not all segments must have this DocValues
      }
    }
    fail("no ordinals found for " + field);
  }

  @Test
  public void testDifferentFieldsAndText() throws Exception {
    Directory indexDir = newDirectory();
    Directory taxoDir = newDirectory();

    // create and open an index writer
    RandomIndexWriter iw = new RandomIndexWriter(random(), indexDir, newIndexWriterConfig(
        TEST_VERSION_CURRENT, new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false)));
    // create and open a taxonomy writer
    TaxonomyWriter tw = new DirectoryTaxonomyWriter(taxoDir, OpenMode.CREATE);

    Map<CategoryPath,CategoryListParams> paramsMap = new HashMap<CategoryPath,CategoryListParams>();
    paramsMap.put(new CategoryPath("Band"), new CategoryListParams("$bands"));
    paramsMap.put(new CategoryPath("Composer"), new CategoryListParams("$composers"));
    PerDimensionIndexingParams iParams = new PerDimensionIndexingParams(paramsMap);
    seedIndex(iw, tw, iParams);

    IndexReader ir = iw.getReader();
    tw.commit();

    // prepare index reader and taxonomy.
    TaxonomyReader tr = new DirectoryTaxonomyReader(taxoDir);

    // prepare searcher to search against
    IndexSearcher searcher = newSearcher(ir);

    FacetsCollector facetsCollector = performSearch(iParams, tr, ir, searcher);

    // Obtain facets results and hand-test them
    assertCorrectResults(facetsCollector);
    assertOrdinalsExist("$facets", ir);
    assertOrdinalsExist("$bands", ir);
    assertOrdinalsExist("$composers", ir);

    IOUtils.close(tr, ir, iw, tw);
    IOUtils.close(indexDir, taxoDir);
  }

  @Test
  public void testSomeSameSomeDifferent() throws Exception {
    Directory indexDir = newDirectory();
    Directory taxoDir = newDirectory();
    
    // create and open an index writer
    RandomIndexWriter iw = new RandomIndexWriter(random(), indexDir, newIndexWriterConfig(
        TEST_VERSION_CURRENT, new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false)));
    // create and open a taxonomy writer
    TaxonomyWriter tw = new DirectoryTaxonomyWriter(taxoDir, OpenMode.CREATE);

    Map<CategoryPath,CategoryListParams> paramsMap = new HashMap<CategoryPath,CategoryListParams>();
    paramsMap.put(new CategoryPath("Band"), new CategoryListParams("$music"));
    paramsMap.put(new CategoryPath("Composer"), new CategoryListParams("$music"));
    paramsMap.put(new CategoryPath("Author"), new CategoryListParams("$literature"));
    PerDimensionIndexingParams iParams = new PerDimensionIndexingParams(paramsMap);

    seedIndex(iw, tw, iParams);

    IndexReader ir = iw.getReader();
    tw.commit();

    // prepare index reader and taxonomy.
    TaxonomyReader tr = new DirectoryTaxonomyReader(taxoDir);

    // prepare searcher to search against
    IndexSearcher searcher = newSearcher(ir);

    FacetsCollector facetsCollector = performSearch(iParams, tr, ir, searcher);

    // Obtain facets results and hand-test them
    assertCorrectResults(facetsCollector);
    assertOrdinalsExist("$music", ir);
    assertOrdinalsExist("$literature", ir);

    IOUtils.close(tr, ir, iw, tw);
    IOUtils.close(indexDir, taxoDir);
  }

  private void assertCorrectResults(FacetsCollector facetsCollector) throws IOException {
    List<FacetResult> res = facetsCollector.getFacetResults();

    FacetResult results = res.get(0);
    FacetResultNode resNode = results.getFacetResultNode();
    Iterable<? extends FacetResultNode> subResults = resNode.subResults;
    Iterator<? extends FacetResultNode> subIter = subResults.iterator();

    checkResult(subIter.next(), "Band/Rock & Pop", 4.0);
    checkResult(subIter.next(), "Band/Punk", 1.0);

    results = res.get(1);
    resNode = results.getFacetResultNode();
    subResults = resNode.subResults;
    subIter = subResults.iterator();

    checkResult(subIter.next(), "Band/Rock & Pop", 4.0);
    checkResult(subIter.next(), "Band/Rock & Pop/Dave Matthews Band", 1.0);
    checkResult(subIter.next(), "Band/Rock & Pop/REM", 1.0);
    checkResult(subIter.next(), "Band/Rock & Pop/U2", 1.0);
    checkResult(subIter.next(), "Band/Punk/The Ramones", 1.0);
    checkResult(subIter.next(), "Band/Punk", 1.0);
    checkResult(subIter.next(), "Band/Rock & Pop/The Beatles", 1.0);

    results = res.get(2);
    resNode = results.getFacetResultNode();
    subResults = resNode.subResults;
    subIter = subResults.iterator();

    checkResult(subIter.next(), "Author/Kurt Vonnegut", 1.0);
    checkResult(subIter.next(), "Author/Stephen King", 1.0);
    checkResult(subIter.next(), "Author/Mark Twain", 1.0);

    results = res.get(3);
    resNode = results.getFacetResultNode();
    subResults = resNode.subResults;
    subIter = subResults.iterator();

    checkResult(subIter.next(), "Band/Rock & Pop/Dave Matthews Band", 1.0);
    checkResult(subIter.next(), "Band/Rock & Pop/REM", 1.0);
    checkResult(subIter.next(), "Band/Rock & Pop/U2", 1.0);
    checkResult(subIter.next(), "Band/Rock & Pop/The Beatles", 1.0);
  }

  private FacetsCollector performSearch(FacetIndexingParams iParams, TaxonomyReader tr, IndexReader ir, 
      IndexSearcher searcher) throws IOException {
    // step 1: collect matching documents into a collector
    Query q = new MatchAllDocsQuery();
    TopScoreDocCollector topDocsCollector = TopScoreDocCollector.create(10, true);

    List<FacetRequest> facetRequests = new ArrayList<FacetRequest>();
    facetRequests.add(new CountFacetRequest(new CategoryPath("Band"), 10));
    CountFacetRequest bandDepth = new CountFacetRequest(new CategoryPath("Band"), 10);
    bandDepth.setDepth(2);
    // makes it easier to check the results in the test.
    bandDepth.setResultMode(ResultMode.GLOBAL_FLAT);
    facetRequests.add(bandDepth);
    facetRequests.add(new CountFacetRequest(new CategoryPath("Author"), 10));
    facetRequests.add(new CountFacetRequest(new CategoryPath("Band", "Rock & Pop"), 10));

    // Faceted search parameters indicate which facets are we interested in
    FacetSearchParams facetSearchParams = new FacetSearchParams(facetRequests, iParams);

    // perform documents search and facets accumulation
    FacetsCollector facetsCollector = FacetsCollector.create(facetSearchParams, ir, tr);
    searcher.search(q, MultiCollector.wrap(topDocsCollector, facetsCollector));
    return facetsCollector;
  }

  private void seedIndex(RandomIndexWriter iw, TaxonomyWriter tw, FacetIndexingParams iParams) throws IOException {
    FacetFields facetFields = new FacetFields(tw, iParams);
    for (CategoryPath cp : CATEGORIES) {
      Document doc = new Document();
      facetFields.addFields(doc, Collections.singletonList(cp));
      doc.add(new TextField("content", "alpha", Field.Store.YES));
      iw.addDocument(doc);
    }
  }

  private static void checkResult(FacetResultNode sub, String label, double value) {
    assertEquals("Label of subresult " + sub.label + " was incorrect", label, sub.label.toString());
    assertEquals("Value for " + sub.label + " subresult was incorrect", value, sub.value, 0.0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2605.java