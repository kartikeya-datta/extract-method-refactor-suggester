error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/409.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/409.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/409.java
text:
```scala
private v@@oid doTest(boolean withCache, boolean plantWrongData) throws Exception {

package org.apache.lucene.facet.search;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.MatchAllDocsQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.apache.lucene.facet.FacetTestBase;
import org.apache.lucene.facet.index.params.CategoryListParams;
import org.apache.lucene.facet.index.params.FacetIndexingParams;
import org.apache.lucene.facet.search.cache.CategoryListCache;
import org.apache.lucene.facet.search.cache.CategoryListData;
import org.apache.lucene.facet.search.params.CountFacetRequest;
import org.apache.lucene.facet.search.params.FacetSearchParams;
import org.apache.lucene.facet.search.results.FacetResult;
import org.apache.lucene.facet.taxonomy.CategoryPath;

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

public class TestCategoryListCache extends FacetTestBase {

  public TestCategoryListCache() {
    super();
  }
  
  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    initIndex();
  }
  
  @After
  @Override
  public void tearDown() throws Exception {
    closeAll();
    super.tearDown();
  }
  
  @Test
  public void testNoClCache() throws Exception {
    doTest(false,false);
  }

  @Test
  public void testCorrectClCache() throws Exception {
    doTest(true,false);
  }
  
  @Test
  public void testWrongClCache() throws Exception {
    doTest(true,true);
  }
  
  private void doTest(boolean withCache, boolean plantWrongData) throws IOException, Exception {
    Map<CategoryPath,Integer> truth = facetCountsTruth();
    CategoryPath cp = (CategoryPath) truth.keySet().toArray()[0]; // any category path will do for this test 
    CountFacetRequest frq = new CountFacetRequest(cp, 10);
    FacetSearchParams sParams = getFacetedSearchParams();
    sParams.addFacetRequest(frq);
    if (withCache) {
      //let's use a cached cl data
      FacetIndexingParams iparams = sParams.getFacetIndexingParams();
      CategoryListParams clp = new CategoryListParams(); // default term ok as only single list
      CategoryListCache clCache = new CategoryListCache();
      clCache.loadAndRegister(clp, indexReader, taxoReader, iparams);
      if (plantWrongData) {
        // let's mess up the cached data and then expect a wrong result...
        messCachedData(clCache, clp);
      }
      sParams.setClCache(clCache);
    }
    FacetsCollector fc = new FacetsCollector(sParams, indexReader, taxoReader);
    searcher.search(new MatchAllDocsQuery(), fc);
    List<FacetResult> res = fc.getFacetResults();
    try {
      assertCountsAndCardinality(truth, res);
      assertFalse("Correct results not expected when wrong data was cached", plantWrongData);
    } catch (Throwable e) {
      assertTrue("Wrong results not expected unless wrong data was cached", withCache);
      assertTrue("Wrong results not expected unless wrong data was cached", plantWrongData);
    }
  }

  /** Mess the cached data for this {@link CategoryListParams} */
  private void messCachedData(CategoryListCache clCache, CategoryListParams clp) {
    final CategoryListData cld = clCache.get(clp);
    CategoryListData badCld = new CategoryListData() {
      @Override
      public CategoryListIterator iterator(int partition)  throws IOException {
        final CategoryListIterator it = cld.iterator(partition);
        return new CategoryListIterator() {              
          public boolean skipTo(int docId) throws IOException {
            return it.skipTo(docId);
          }
          public long nextCategory() throws IOException {
            long res = it.nextCategory();
            if (res>Integer.MAX_VALUE) {
              return res;
            }
            return res>1 ? res-1 : res+1;
          }
          public boolean init() throws IOException {
            return it.init();
          }
        };
      }
    };
    clCache.register(clp, badCld);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/409.java