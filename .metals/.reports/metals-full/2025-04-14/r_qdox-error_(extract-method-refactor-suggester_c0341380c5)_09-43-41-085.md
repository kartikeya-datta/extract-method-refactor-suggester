error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3751.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3751.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3751.java
text:
```scala
I@@ndexWriter writer = new IndexWriter (indexStore, new StandardAnalyzer(TEST_VERSION_CURRENT), true, IndexWriter.MaxFieldLength.LIMITED);

package org.apache.lucene.search;

/**
 * Copyright 2005 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.LuceneTestCase;

/**
 * Unit test for sorting code.
 *
 */

public class TestCustomSearcherSort
extends LuceneTestCase
implements Serializable {

    private Directory index = null;
    private Query query = null;
    // reduced from 20000 to 2000 to speed up test...
    private final static int INDEX_SIZE = 2000;

  public TestCustomSearcherSort (String name) {
    super (name);
  }

  public static void main (String[] argv) {
      TestRunner.run (suite());
  }

  public static Test suite() {
    return new TestSuite (TestCustomSearcherSort.class);
  }


  // create an index for testing
  private Directory getIndex()
  throws IOException {
          RAMDirectory indexStore = new RAMDirectory ();
          IndexWriter writer = new IndexWriter (indexStore, new StandardAnalyzer(org.apache.lucene.util.Version.LUCENE_CURRENT), true, IndexWriter.MaxFieldLength.LIMITED);
          RandomGen random = new RandomGen(newRandom());
          for (int i=0; i<INDEX_SIZE; ++i) { // don't decrease; if to low the problem doesn't show up
          Document doc = new Document();
              if((i%5)!=0) { // some documents must not have an entry in the first sort field
                  doc.add (new Field("publicationDate_", random.getLuceneDate(), Field.Store.YES, Field.Index.NOT_ANALYZED));
              }
              if((i%7)==0) { // some documents to match the query (see below) 
                  doc.add (new Field("content", "test", Field.Store.YES, Field.Index.ANALYZED));
              }
              // every document has a defined 'mandant' field
              doc.add(new Field("mandant", Integer.toString(i%3), Field.Store.YES, Field.Index.NOT_ANALYZED));
              writer.addDocument (doc);
          }
          writer.optimize ();
          writer.close ();
      return indexStore;
  }

  /**
   * Create index and query for test cases. 
   */
  @Override
  public void setUp() throws Exception {
          super.setUp();
          index = getIndex();
          query = new TermQuery( new Term("content", "test"));
  }

  /**
   * Run the test using two CustomSearcher instances. 
   */
  public void testFieldSortCustomSearcher() throws Exception {
    // log("Run testFieldSortCustomSearcher");
    // define the sort criteria
      Sort custSort = new Sort(
              new SortField("publicationDate_", SortField.STRING), 
              SortField.FIELD_SCORE
      );
      Searcher searcher = new CustomSearcher (index, 2);
      // search and check hits
    matchHits(searcher, custSort);
  }
  /**
   * Run the test using one CustomSearcher wrapped by a MultiSearcher. 
   */
  public void testFieldSortSingleSearcher() throws Exception {
    // log("Run testFieldSortSingleSearcher");
    // define the sort criteria
      Sort custSort = new Sort(
              new SortField("publicationDate_", SortField.STRING), 
              SortField.FIELD_SCORE
      );
      Searcher searcher = new MultiSearcher(new Searcher[] { new CustomSearcher(
        index, 2) });
      // search and check hits
    matchHits(searcher, custSort);
  }
  /**
   * Run the test using two CustomSearcher instances. 
   */
  public void testFieldSortMultiCustomSearcher() throws Exception {
    // log("Run testFieldSortMultiCustomSearcher");
    // define the sort criteria
      Sort custSort = new Sort(
              new SortField("publicationDate_", SortField.STRING), 
              SortField.FIELD_SCORE
      );
      Searcher searcher = 
          new MultiSearcher(new Searchable[] {
                  new CustomSearcher (index, 0),
                  new CustomSearcher (index, 2)});
      // search and check hits
    matchHits(searcher, custSort);
  }


  // make sure the documents returned by the search match the expected list
  private void matchHits (Searcher searcher, Sort sort)
  throws IOException {
      // make a query without sorting first
    ScoreDoc[] hitsByRank = searcher.search(query, null, 1000).scoreDocs;
    checkHits(hitsByRank, "Sort by rank: "); // check for duplicates
        Map resultMap = new TreeMap();
        // store hits in TreeMap - TreeMap does not allow duplicates; existing entries are silently overwritten
        for(int hitid=0;hitid<hitsByRank.length; ++hitid) {
            resultMap.put(
                    Integer.valueOf(hitsByRank[hitid].doc),  // Key:   Lucene Document ID
                    Integer.valueOf(hitid));				// Value: Hits-Objekt Index
        }
        
        // now make a query using the sort criteria
    ScoreDoc[] resultSort = searcher.search (query, null, 1000, sort).scoreDocs;
    checkHits(resultSort, "Sort by custom criteria: "); // check for duplicates
    
        // besides the sorting both sets of hits must be identical
        for(int hitid=0;hitid<resultSort.length; ++hitid) {
            Integer idHitDate = Integer.valueOf(resultSort[hitid].doc); // document ID from sorted search
            if(!resultMap.containsKey(idHitDate)) {
                log("ID "+idHitDate+" not found. Possibliy a duplicate.");
            }
            assertTrue(resultMap.containsKey(idHitDate)); // same ID must be in the Map from the rank-sorted search
            // every hit must appear once in both result sets --> remove it from the Map.
            // At the end the Map must be empty!
            resultMap.remove(idHitDate);
        }
        if(resultMap.size()==0) {
            // log("All hits matched");
        } else {
        log("Couldn't match "+resultMap.size()+" hits.");
        }
        assertEquals(resultMap.size(), 0);
  }

  /**
   * Check the hits for duplicates.
   * @param hits
   */
    private void checkHits(ScoreDoc[] hits, String prefix) {
        if(hits!=null) {
            Map idMap = new TreeMap();
            for(int docnum=0;docnum<hits.length;++docnum) {
                Integer luceneId = null;

                luceneId = Integer.valueOf(hits[docnum].doc);
                if(idMap.containsKey(luceneId)) {
                    StringBuilder message = new StringBuilder(prefix);
                    message.append("Duplicate key for hit index = ");
                    message.append(docnum);
                    message.append(", previous index = ");
                    message.append(((Integer)idMap.get(luceneId)).toString());
                    message.append(", Lucene ID = ");
                    message.append(luceneId);
                    log(message.toString());
                } else { 
                    idMap.put(luceneId, Integer.valueOf(docnum));
                }
            }
        }
    }
    
    // Simply write to console - choosen to be independant of log4j etc 
    private void log(String message) {
        System.out.println(message);
    }
    
    public class CustomSearcher extends IndexSearcher {
        private int switcher;
        /**
         * @param directory
         * @throws IOException
         */
        public CustomSearcher(Directory directory, int switcher) throws IOException {
            super(directory, true);
            this.switcher = switcher;
        }
        /**
         * @param r
         */
        public CustomSearcher(IndexReader r, int switcher) {
            super(r);
            this.switcher = switcher;
        }
        /* (non-Javadoc)
         * @see org.apache.lucene.search.Searchable#search(org.apache.lucene.search.Query, org.apache.lucene.search.Filter, int, org.apache.lucene.search.Sort)
         */
        @Override
        public TopFieldDocs search(Query query, Filter filter, int nDocs,
                Sort sort) throws IOException {
            BooleanQuery bq = new BooleanQuery();
            bq.add(query, BooleanClause.Occur.MUST);
            bq.add(new TermQuery(new Term("mandant", Integer.toString(switcher))), BooleanClause.Occur.MUST);
            return super.search(bq, filter, nDocs, sort);
        }
        /* (non-Javadoc)
         * @see org.apache.lucene.search.Searchable#search(org.apache.lucene.search.Query, org.apache.lucene.search.Filter, int)
         */
        @Override
        public TopDocs search(Query query, Filter filter, int nDocs)
        throws IOException {
            BooleanQuery bq = new BooleanQuery();
            bq.add(query, BooleanClause.Occur.MUST);
            bq.add(new TermQuery(new Term("mandant", Integer.toString(switcher))), BooleanClause.Occur.MUST);
            return super.search(bq, filter, nDocs);
        }
    }
    private class RandomGen {
      RandomGen(Random random) {
        this.random = random;
      }
      private Random random;
      private Calendar base = new GregorianCalendar(1980, 1, 1);

      // Just to generate some different Lucene Date strings
      private String getLuceneDate() {
        return DateTools.timeToString(base.getTimeInMillis() + random.nextInt() - Integer.MIN_VALUE, DateTools.Resolution.DAY);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3751.java