error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3024.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3024.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3024.java
text:
```scala
R@@andomIndexWriter iw = new RandomIndexWriter(random, directory, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random)).setMergePolicy(newLogMergePolicy()));

package org.apache.lucene.search;

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
import java.util.BitSet;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexReader.AtomicReaderContext;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.TimeLimitingCollector.TimeExceededException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.ThreadInterruptedException;
import org.junit.Ignore;

/**
 * Tests the {@link TimeLimitingCollector}.  This test checks (1) search
 * correctness (regardless of timeout), (2) expected timeout behavior,
 * and (3) a sanity test with multiple searching threads.
 */
@Ignore("broken: see https://issues.apache.org/jira/browse/LUCENE-2822")
public class TestTimeLimitingCollector extends LuceneTestCase {
  private static final int SLOW_DOWN = 3;
  private static final long TIME_ALLOWED = 17 * SLOW_DOWN; // so searches can find about 17 docs.
  
  // max time allowed is relaxed for multithreading tests. 
  // the multithread case fails when setting this to 1 (no slack) and launching many threads (>2000).  
  // but this is not a real failure, just noise.
  private static final double MULTI_THREAD_SLACK = 7;      
            
  private static final int N_DOCS = 3000;
  private static final int N_THREADS = 50;

  private IndexSearcher searcher;
  private Directory directory;
  private IndexReader reader;

  private final String FIELD_NAME = "body";
  private Query query;

  /**
   * initializes searcher with a document set
   */
  @Override
  public void setUp() throws Exception {
    super.setUp();
    final String docText[] = {
        "docThatNeverMatchesSoWeCanRequireLastDocCollectedToBeGreaterThanZero",
        "one blah three",
        "one foo three multiOne",
        "one foobar three multiThree",
        "blueberry pancakes",
        "blueberry pie",
        "blueberry strudel",
        "blueberry pizza",
    };
    directory = newDirectory();
    RandomIndexWriter iw = new RandomIndexWriter(random, directory);
    
    for (int i=0; i<N_DOCS; i++) {
      add(docText[i%docText.length], iw);
    }
    reader = iw.getReader();
    iw.close();
    searcher = newSearcher(reader);

    String qtxt = "one";
    // start from 1, so that the 0th doc never matches
    for (int i = 1; i < docText.length; i++) {
      qtxt += ' ' + docText[i]; // large query so that search will be longer
    }
    QueryParser queryParser = new QueryParser(TEST_VERSION_CURRENT, FIELD_NAME, new MockAnalyzer(random));
    query = queryParser.parse(qtxt);
    
    // warm the searcher
    searcher.search(query, null, 1000);

  }

  @Override
  public void tearDown() throws Exception {
    searcher.close();
    reader.close();
    directory.close();
    super.tearDown();
  }

  private void add(String value, RandomIndexWriter iw) throws IOException {
    Document d = new Document();
    d.add(newField(FIELD_NAME, value, Field.Store.NO, Field.Index.ANALYZED));
    iw.addDocument(d);
  }

  private void search(Collector collector) throws Exception {
    searcher.search(query, collector);
  }

  /**
   * test search correctness with no timeout
   */
  public void testSearch() {
    doTestSearch();
  }
  
  private void doTestSearch() {
    int totalResults = 0;
    int totalTLCResults = 0;
    try {
      MyHitCollector myHc = new MyHitCollector();
      search(myHc);
      totalResults = myHc.hitCount();
      
      myHc = new MyHitCollector();
      long oneHour = 3600000;
      Collector tlCollector = createTimedCollector(myHc, oneHour, false);
      search(tlCollector);
      totalTLCResults = myHc.hitCount();
    } catch (Exception e) {
      e.printStackTrace();
      assertTrue("Unexpected exception: "+e, false); //==fail
    }
    assertEquals( "Wrong number of results!", totalResults, totalTLCResults );
  }

  private Collector createTimedCollector(MyHitCollector hc, long timeAllowed, boolean greedy) {
    TimeLimitingCollector res = new TimeLimitingCollector(hc, timeAllowed);
    res.setGreedy(greedy); // set to true to make sure at least one doc is collected.
    return res;
  }

  /**
   * Test that timeout is obtained, and soon enough!
   */
  public void testTimeoutGreedy() {
    doTestTimeout(false, true);
  }
  
  /**
   * Test that timeout is obtained, and soon enough!
   */
  public void testTimeoutNotGreedy() {
    doTestTimeout(false, false);
  }

  private void doTestTimeout(boolean multiThreaded, boolean greedy) {
    // setup
    MyHitCollector myHc = new MyHitCollector();
    myHc.setSlowDown(SLOW_DOWN);
    Collector tlCollector = createTimedCollector(myHc, TIME_ALLOWED, greedy);

    // search
    TimeExceededException timoutException = null;
    try {
      search(tlCollector);
    } catch (TimeExceededException x) {
      timoutException = x;
    } catch (Exception e) {
      assertTrue("Unexpected exception: "+e, false); //==fail
    }
    
    // must get exception
    assertNotNull( "Timeout expected!", timoutException );

    // greediness affect last doc collected
    int exceptionDoc = timoutException.getLastDocCollected();
    int lastCollected = myHc.getLastDocCollected(); 
    assertTrue( "doc collected at timeout must be > 0!", exceptionDoc > 0 );
    if (greedy) {
      assertTrue("greedy="+greedy+" exceptionDoc="+exceptionDoc+" != lastCollected="+lastCollected, exceptionDoc==lastCollected);
      assertTrue("greedy, but no hits found!", myHc.hitCount() > 0 );
    } else {
      assertTrue("greedy="+greedy+" exceptionDoc="+exceptionDoc+" not > lastCollected="+lastCollected, exceptionDoc>lastCollected);
    }

    // verify that elapsed time at exception is within valid limits
    assertEquals( timoutException.getTimeAllowed(), TIME_ALLOWED);
    // a) Not too early
    assertTrue ( "elapsed="+timoutException.getTimeElapsed()+" <= (allowed-resolution)="+(TIME_ALLOWED-TimeLimitingCollector.getResolution()),
        timoutException.getTimeElapsed() > TIME_ALLOWED-TimeLimitingCollector.getResolution());
    // b) Not too late.
    //    This part is problematic in a busy test system, so we just print a warning.
    //    We already verified that a timeout occurred, we just can't be picky about how long it took.
    if (timoutException.getTimeElapsed() > maxTime(multiThreaded)) {
      System.out.println("Informative: timeout exceeded (no action required: most probably just " +
        " because the test machine is slower than usual):  " +
        "lastDoc="+exceptionDoc+
        " ,&& allowed="+timoutException.getTimeAllowed() +
        " ,&& elapsed="+timoutException.getTimeElapsed() +
        " >= " + maxTimeStr(multiThreaded));
    }
  }

  private long maxTime(boolean multiThreaded) {
    long res = 2 * TimeLimitingCollector.getResolution() + TIME_ALLOWED + SLOW_DOWN; // some slack for less noise in this test
    if (multiThreaded) {
      res *= MULTI_THREAD_SLACK; // larger slack  
    }
    return res;
  }

  private String maxTimeStr(boolean multiThreaded) {
    String s =
      "( " +
      "2*resolution +  TIME_ALLOWED + SLOW_DOWN = " +
      "2*" + TimeLimitingCollector.getResolution() + " + " + TIME_ALLOWED + " + " + SLOW_DOWN +
      ")";
    if (multiThreaded) {
      s = MULTI_THREAD_SLACK + " * "+s;  
    }
    return maxTime(multiThreaded) + " = " + s;
  }

  /**
   * Test timeout behavior when resolution is modified. 
   */
  public void testModifyResolution() {
    try {
      // increase and test
      long resolution = 20 * TimeLimitingCollector.DEFAULT_RESOLUTION; //400
      TimeLimitingCollector.setResolution(resolution);
      assertEquals(resolution, TimeLimitingCollector.getResolution());
      doTestTimeout(false,true);
      // decrease much and test
      resolution = 5;
      TimeLimitingCollector.setResolution(resolution);
      assertEquals(resolution, TimeLimitingCollector.getResolution());
      doTestTimeout(false,true);
      // return to default and test
      resolution = TimeLimitingCollector.DEFAULT_RESOLUTION;
      TimeLimitingCollector.setResolution(resolution);
      assertEquals(resolution, TimeLimitingCollector.getResolution());
      doTestTimeout(false,true);
    } finally {
      TimeLimitingCollector.setResolution(TimeLimitingCollector.DEFAULT_RESOLUTION);
    }
  }
  
  /** 
   * Test correctness with multiple searching threads.
   */
  public void testSearchMultiThreaded() throws Exception {
    doTestMultiThreads(false);
  }

  /** 
   * Test correctness with multiple searching threads.
   */
  public void testTimeoutMultiThreaded() throws Exception {
    doTestMultiThreads(true);
  }
  
  private void doTestMultiThreads(final boolean withTimeout) throws Exception {
    Thread [] threadArray = new Thread[N_THREADS];
    final BitSet success = new BitSet(N_THREADS);
    for( int i = 0; i < threadArray.length; ++i ) {
      final int num = i;
      threadArray[num] = new Thread() {
          @Override
          public void run() {
            if (withTimeout) {
              doTestTimeout(true,true);
            } else {
              doTestSearch();
            }
            synchronized(success) {
              success.set(num);
            }
          }
      };
    }
    for( int i = 0; i < threadArray.length; ++i ) {
      threadArray[i].start();
    }
    for( int i = 0; i < threadArray.length; ++i ) {
      threadArray[i].join();
    }
    assertEquals("some threads failed!", N_THREADS,success.cardinality());
  }
  
  // counting collector that can slow down at collect().
  private class MyHitCollector extends Collector {
    private final BitSet bits = new BitSet();
    private int slowdown = 0;
    private int lastDocCollected = -1;
    private int docBase = 0;

    /**
     * amount of time to wait on each collect to simulate a long iteration
     */
    public void setSlowDown( int milliseconds ) {
      slowdown = milliseconds;
    }
    
    public int hitCount() {
      return bits.cardinality();
    }

    public int getLastDocCollected() {
      return lastDocCollected;
    }

    @Override
    public void setScorer(Scorer scorer) throws IOException {
      // scorer is not needed
    }
    
    @Override
    public void collect(final int doc) throws IOException {
      int docId = doc + docBase;
      if( slowdown > 0 ) {
        try {
          Thread.sleep(slowdown);
        } catch (InterruptedException ie) {
          throw new ThreadInterruptedException(ie);
        }
      }
      assert docId >= 0: " base=" + docBase + " doc=" + doc;
      bits.set( docId );
      lastDocCollected = docId;
    }
    
    @Override
    public void setNextReader(AtomicReaderContext context) {
      docBase = context.docBase;
    }
    
    @Override
    public boolean acceptsDocsOutOfOrder() {
      return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3024.java