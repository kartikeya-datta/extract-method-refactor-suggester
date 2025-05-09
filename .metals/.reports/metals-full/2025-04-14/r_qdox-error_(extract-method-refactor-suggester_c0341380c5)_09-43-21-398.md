error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1037.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1037.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1037.java
text:
```scala
W@@eight w = searcher.createNormalizedWeight(q);

package org.apache.lucene.benchmark.byTask.tasks;

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

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.benchmark.byTask.PerfRunData;
import org.apache.lucene.benchmark.byTask.feeds.QueryMaker;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.Weight;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.store.Directory;


/**
 * Read index (abstract) task.
 * Sub classes implement withSearch(), withWarm(), withTraverse() and withRetrieve()
 * methods to configure the actual action.
 * <p/>
 * <p>Note: All ReadTasks reuse the reader if it is already open.
 * Otherwise a reader is opened at start and closed at the end.
 * <p>
 * The <code>search.num.hits</code> config parameter sets
 * the top number of hits to collect during searching.  If
 * <code>print.hits.field</code> is set, then each hit is
 * printed along with the value of that field.</p>
 *
 * <p>Other side effects: none.
 */
public abstract class ReadTask extends PerfTask {

  private final QueryMaker queryMaker;

  public ReadTask(PerfRunData runData) {
    super(runData);
    if (withSearch()) {
      queryMaker = getQueryMaker();
    } else {
      queryMaker = null;
    }
  }
  @Override
  public int doLogic() throws Exception {
    int res = 0;

    // open reader or use existing one
    IndexSearcher searcher = getRunData().getIndexSearcher();

    IndexReader reader;

    final boolean closeSearcher;
    if (searcher == null) {
      // open our own reader
      Directory dir = getRunData().getDirectory();
      reader = IndexReader.open(dir, true);
      searcher = new IndexSearcher(reader);
      closeSearcher = true;
    } else {
      // use existing one; this passes +1 ref to us
      reader = searcher.getIndexReader();
      closeSearcher = false;
    }

    // optionally warm and add num docs traversed to count
    if (withWarm()) {
      Document doc = null;
      for (int m = 0; m < reader.maxDoc(); m++) {
        if (!reader.isDeleted(m)) {
          doc = reader.document(m);
          res += (doc == null ? 0 : 1);
        }
      }
    }

    if (withSearch()) {
      res++;
      Query q = queryMaker.makeQuery();
      Sort sort = getSort();
      TopDocs hits = null;
      final int numHits = numHits();
      if (numHits > 0) {
        if (withCollector() == false) {
          if (sort != null) {
            Weight w = q.weight(searcher);
            TopFieldCollector collector = TopFieldCollector.create(sort, numHits,
                                                                   true, withScore(),
                                                                   withMaxScore(),
                                                                   !w.scoresDocsOutOfOrder());
            searcher.search(w, null, collector);
            hits = collector.topDocs();
          } else {
            hits = searcher.search(q, numHits);
          }
        } else {
          Collector collector = createCollector();
          searcher.search(q, null, collector);
          //hits = collector.topDocs();
        }

        final String printHitsField = getRunData().getConfig().get("print.hits.field", null);
        if (hits != null && printHitsField != null && printHitsField.length() > 0) {
          if (q instanceof MultiTermQuery) {
            System.out.println("MultiTermQuery term count = " + ((MultiTermQuery) q).getTotalNumberOfTerms());
          }
          System.out.println("totalHits = " + hits.totalHits);
          System.out.println("maxDoc()  = " + reader.maxDoc());
          System.out.println("numDocs() = " + reader.numDocs());
          for(int i=0;i<hits.scoreDocs.length;i++) {
            final int docID = hits.scoreDocs[i].doc;
            final Document doc = reader.document(docID);
            System.out.println("  " + i + ": doc=" + docID + " score=" + hits.scoreDocs[i].score + " " + printHitsField + " =" + doc.get(printHitsField));
          }
        }

        if (withTraverse()) {
          final ScoreDoc[] scoreDocs = hits.scoreDocs;
          int traversalSize = Math.min(scoreDocs.length, traversalSize());

          if (traversalSize > 0) {
            boolean retrieve = withRetrieve();
            int numHighlight = Math.min(numToHighlight(), scoreDocs.length);
            Analyzer analyzer = getRunData().getAnalyzer();
            BenchmarkHighlighter highlighter = null;
            if (numHighlight > 0) {
              highlighter = getBenchmarkHighlighter(q);
            }
            for (int m = 0; m < traversalSize; m++) {
              int id = scoreDocs[m].doc;
              res++;
              if (retrieve) {
                Document document = retrieveDoc(reader, id);
                res += document != null ? 1 : 0;
                if (numHighlight > 0 && m < numHighlight) {
                  Collection<String> fieldsToHighlight = getFieldsToHighlight(document);
                  for (final String field : fieldsToHighlight) {
                    String text = document.get(field);
                    res += highlighter.doHighlight(reader, id, field, document, analyzer, text);
                  }
                }
              }
            }
          }
        }
      }
    }

    if (closeSearcher) {
      searcher.close();
      reader.close();
    } else {
      // Release our +1 ref from above
      reader.decRef();
    }
    return res;
  }

  protected Collector createCollector() throws Exception {
    return TopScoreDocCollector.create(numHits(), true);
  }


  protected Document retrieveDoc(IndexReader ir, int id) throws IOException {
    return ir.document(id);
  }

  /**
   * Return query maker used for this task.
   */
  public abstract QueryMaker getQueryMaker();

  /**
   * Return true if search should be performed.
   */
  public abstract boolean withSearch();

  public boolean withCollector(){
    return false;
  }
  

  /**
   * Return true if warming should be performed.
   */
  public abstract boolean withWarm();

  /**
   * Return true if, with search, results should be traversed.
   */
  public abstract boolean withTraverse();

  /** Whether scores should be computed (only useful with
   *  field sort) */
  public boolean withScore() {
    return true;
  }

  /** Whether maxScores should be computed (only useful with
   *  field sort) */
  public boolean withMaxScore() {
    return true;
  }

  /**
   * Specify the number of hits to traverse.  Tasks should override this if they want to restrict the number
   * of hits that are traversed when {@link #withTraverse()} is true. Must be greater than 0.
   * <p/>
   * Read task calculates the traversal as: Math.min(hits.length(), traversalSize())
   *
   * @return Integer.MAX_VALUE
   */
  public int traversalSize() {
    return Integer.MAX_VALUE;
  }

  static final int DEFAULT_SEARCH_NUM_HITS = 10;
  private int numHits;

  @Override
  public void setup() throws Exception {
    super.setup();
    numHits = getRunData().getConfig().get("search.num.hits", DEFAULT_SEARCH_NUM_HITS);
  }

  /**
   * Specify the number of hits to retrieve.  Tasks should override this if they want to restrict the number
   * of hits that are collected during searching. Must be greater than 0.
   *
   * @return 10 by default, or search.num.hits config if set.
   */
  public int numHits() {
    return numHits;
  }

  /**
   * Return true if, with search & results traversing, docs should be retrieved.
   */
  public abstract boolean withRetrieve();

  /**
   * Set to the number of documents to highlight.
   *
   * @return The number of the results to highlight.  O means no docs will be highlighted.
   */
  public int numToHighlight() {
    return 0;
  }

  /**
   * Return an appropriate highlighter to be used with
   * highlighting tasks
   */
  protected BenchmarkHighlighter getBenchmarkHighlighter(Query q){
    return null;
  }
  
  protected Sort getSort() {
    return null;
  }

  /**
   * Define the fields to highlight.  Base implementation returns all fields
   * @param document The Document
   * @return A Collection of Field names (Strings)
   */
  protected Collection<String> getFieldsToHighlight(Document document) {
    List<Fieldable> fieldables = document.getFields();
    Set<String> result = new HashSet<String>(fieldables.size());
    for (final Fieldable fieldable : fieldables) {
      result.add(fieldable.name());
    }
    return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1037.java