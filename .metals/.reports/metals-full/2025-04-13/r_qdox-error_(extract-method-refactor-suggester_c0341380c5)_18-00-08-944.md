error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2219.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2219.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2219.java
text:
```scala
final b@@oolean doSync = config.get("fsdirectory.dosync", false);

package org.apache.lucene.benchmark.byTask;

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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.benchmark.byTask.feeds.DocMaker;
import org.apache.lucene.benchmark.byTask.feeds.HTMLParser;
import org.apache.lucene.benchmark.byTask.feeds.QueryMaker;
import org.apache.lucene.benchmark.byTask.stats.Points;
import org.apache.lucene.benchmark.byTask.tasks.ReadTask;
import org.apache.lucene.benchmark.byTask.tasks.SearchTask;
import org.apache.lucene.benchmark.byTask.utils.Config;
import org.apache.lucene.benchmark.byTask.utils.FileUtils;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Data maintained by a performance test run.
 * <p>
 * Data includes:
 * <ul>
 *  <li>Configuration.
 *  <li>Directory, Writer, Reader.
 *  <li>Docmaker and a few instances of QueryMaker.
 *  <li>Analyzer.
 *  <li>Statistics data which updated during the run.
 * </ul>
 * Config properties: work.dir=&lt;path to root of docs and index dirs| Default: work&gt;
 * </ul>
 */
public class PerfRunData {

  private Points points;
  
  // objects used during performance test run
  // directory, analyzer, docMaker - created at startup.
  // reader, writer, searcher - maintained by basic tasks. 
  private Directory directory;
  private Analyzer analyzer;
  private DocMaker docMaker;
  private HTMLParser htmlParser;
  
  // we use separate (identical) instances for each "read" task type, so each can iterate the quries separately.
  private HashMap readTaskQueryMaker;
  private Class qmkrClass;

  private IndexReader indexReader;
  private IndexWriter indexWriter;
  private Config config;
  private long startTimeMillis;
  
  // constructor
  public PerfRunData (Config config) throws Exception {
    this.config = config;
    // analyzer (default is standard analyzer)
    analyzer = (Analyzer) Class.forName(config.get("analyzer",
        "org.apache.lucene.analysis.standard.StandardAnalyzer")).newInstance();
    // doc maker
    docMaker = (DocMaker) Class.forName(config.get("doc.maker",
        "org.apache.lucene.benchmark.byTask.feeds.SimpleDocMaker")).newInstance();
    docMaker.setConfig(config);
    // query makers
    readTaskQueryMaker = new HashMap();
    qmkrClass = Class.forName(config.get("query.maker","org.apache.lucene.benchmark.byTask.feeds.SimpleQueryMaker"));
    // html parser, used for some doc makers
    htmlParser = (HTMLParser) Class.forName(config.get("html.parser","org.apache.lucene.benchmark.byTask.feeds.DemoHTMLParser")).newInstance();
    docMaker.setHTMLParser(htmlParser);

    // index stuff
    reinit(false);
    
    // statistic points
    points = new Points(config);
    
    if (Boolean.valueOf(config.get("log.queries","false")).booleanValue()) {
      System.out.println("------------> queries:");
      System.out.println(getQueryMaker(new SearchTask(this)).printQueries());
    }

  }

  // clean old stuff, reopen 
  public void reinit(boolean eraseIndex) throws Exception {

    // cleanup index
    if (indexWriter!=null) {
      indexWriter.close();
      indexWriter = null;
    }
    if (indexReader!=null) {
      indexReader.close();
      indexReader = null;
    }
    if (directory!=null) {
      directory.close();
    }
    
    // directory (default is ram-dir).
    if ("FSDirectory".equals(config.get("directory","RAMDirectory"))) {
      File workDir = new File(config.get("work.dir","work"));
      File indexDir = new File(workDir,"index");
      if (eraseIndex && indexDir.exists()) {
        FileUtils.fullyDelete(indexDir);
      }
      indexDir.mkdirs();
      final boolean doSync = config.get("fsdirectory.dosync", true);
      directory = FSDirectory.getDirectory(indexDir, null, doSync);
    } else {
      directory = new RAMDirectory();
    }

    // inputs
    resetInputs();
    
    // release unused stuff
    System.runFinalization();
    System.gc();
  }
  
  public long setStartTimeMillis() {
    startTimeMillis = System.currentTimeMillis();
    return startTimeMillis;
  }

  /**
   * @return Start time in milliseconds
   */
  public long getStartTimeMillis() {
    return startTimeMillis;
  }

  /**
   * @return Returns the points.
   */
  public Points getPoints() {
    return points;
  }

  /**
   * @return Returns the directory.
   */
  public Directory getDirectory() {
    return directory;
  }

  /**
   * @param directory The directory to set.
   */
  public void setDirectory(Directory directory) {
    this.directory = directory;
  }

  /**
   * @return Returns the indexReader.
   */
  public IndexReader getIndexReader() {
    return indexReader;
  }

  /**
   * @param indexReader The indexReader to set.
   */
  public void setIndexReader(IndexReader indexReader) {
    this.indexReader = indexReader;
  }

  /**
   * @return Returns the indexWriter.
   */
  public IndexWriter getIndexWriter() {
    return indexWriter;
  }

  /**
   * @param indexWriter The indexWriter to set.
   */
  public void setIndexWriter(IndexWriter indexWriter) {
    this.indexWriter = indexWriter;
  }

  /**
   * @return Returns the anlyzer.
   */
  public Analyzer getAnalyzer() {
    return analyzer;
  }


  public void setAnalyzer(Analyzer analyzer) {
    this.analyzer = analyzer;
  }

  /**
   * @return Returns the docMaker.
   */
  public DocMaker getDocMaker() {
    return docMaker;
  }

  /**
   * @return Returns the config.
   */
  public Config getConfig() {
    return config;
  }

  public void resetInputs() {
    docMaker.resetInputs();
    Iterator it = readTaskQueryMaker.values().iterator();
    while (it.hasNext()) {
      ((QueryMaker) it.next()).resetInputs();
    }
  }

  /**
   * @return Returns the queryMaker by read task type (class)
   */
  public QueryMaker getQueryMaker(ReadTask readTask) {
    // mapping the query maker by task class allows extending/adding new search/read tasks
    // without needing to modify this class.
    Class readTaskClass = readTask.getClass();
    QueryMaker qm = (QueryMaker) readTaskQueryMaker.get(readTaskClass);
    if (qm == null) {
      try {
        qm = (QueryMaker) qmkrClass.newInstance();
        qm.setConfig(config);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      readTaskQueryMaker.put(readTaskClass,qm);
    }
    return qm;
  }

  /**
   * @return Returns the htmlParser.
   */
  public HTMLParser getHtmlParser() {
    return htmlParser;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2219.java