error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3744.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3744.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3744.java
text:
```scala
S@@tandardAnalyzer analyzer = new StandardAnalyzer(TEST_VERSION_CURRENT, Collections.emptySet());

package org.apache.lucene.store;

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

import java.util.Collections;
import java.util.Random;
import java.io.File;

import org.apache.lucene.util.LuceneTestCase;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;

public class TestWindowsMMap extends LuceneTestCase {
  
  private final static String alphabet = "abcdefghijklmnopqrstuvwzyz";
  private Random random;
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    random = newRandom();
  }
  
  private String randomToken() {
    int tl = 1 + random.nextInt(7);
    StringBuilder sb = new StringBuilder();
    for(int cx = 0; cx < tl; cx ++) {
      int c = random.nextInt(25);
      sb.append(alphabet.substring(c, c+1));
    }
    return sb.toString();
  }
  
  private String randomField() {
    int fl = 1 + random.nextInt(3);
    StringBuilder fb = new StringBuilder();
    for(int fx = 0; fx < fl; fx ++) {
      fb.append(randomToken());
      fb.append(" ");
    }
    return fb.toString();
  }
  
  private final static String storePathname = 
    new File(System.getProperty("tempDir"),"testLuceneMmap").getAbsolutePath();

  public void testMmapIndex() throws Exception {
    // sometimes the directory is not cleaned by rmDir, because on Windows it
    // may take some time until the files are finally dereferenced. So clean the
    // directory up front, or otherwise new IndexWriter will fail.
    rmDir(new File(storePathname));
    FSDirectory storeDirectory = new MMapDirectory(new File(storePathname), null);

    // plan to add a set of useful stopwords, consider changing some of the
    // interior filters.
    StandardAnalyzer analyzer = new StandardAnalyzer(org.apache.lucene.util.Version.LUCENE_CURRENT, Collections.emptySet());
    // TODO: something about lock timeouts and leftover locks.
    IndexWriter writer = new IndexWriter(storeDirectory, analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
    writer.commit();
    IndexSearcher searcher = new IndexSearcher(storeDirectory, true);
    
    for(int dx = 0; dx < 1000; dx ++) {
      String f = randomField();
      Document doc = new Document();
      doc.add(new Field("data", f, Field.Store.YES, Field.Index.ANALYZED));	
      writer.addDocument(doc);
    }
    
    searcher.close();
    writer.close();
    rmDir(new File(storePathname));
  }

  private void rmDir(File dir) {
    if (!dir.exists()) {
      return;
    }
    for (File file : dir.listFiles()) {
      file.delete();
    }
    dir.delete();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3744.java