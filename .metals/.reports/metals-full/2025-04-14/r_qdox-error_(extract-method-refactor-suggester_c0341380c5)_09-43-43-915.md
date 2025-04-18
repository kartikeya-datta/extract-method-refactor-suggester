error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3758.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3758.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3758.java
text:
```scala
a@@nlzr = new StandardAnalyzer(TEST_VERSION_CURRENT);

package org.apache.lucene.search.function;

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
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import org.apache.lucene.util.LuceneTestCase;

/**
 * Setup for function tests
 */
public abstract class FunctionTestSetup extends LuceneTestCase {

  /**
   * Actual score computation order is slightly different than assumptios
   * this allows for a small amount of variation
   */
  public static float TEST_SCORE_TOLERANCE_DELTA = 0.001f;
  
  protected static final boolean DBG = false; // change to true for logging to print

  protected static final int N_DOCS = 17; // select a primary number > 2

  protected static final String ID_FIELD = "id";
  protected static final String TEXT_FIELD = "text";
  protected static final String INT_FIELD = "iii";
  protected static final String FLOAT_FIELD = "fff";
  
  private static final String DOC_TEXT_LINES[] = {
    "Well, this is just some plain text we use for creating the ",
    "test documents. It used to be a text from an online collection ",
    "devoted to first aid, but if there was there an (online) lawyers ",
    "first aid collection with legal advices, \"it\" might have quite ",
    "probably advised one not to include \"it\"'s text or the text of ",
    "any other online collection in one's code, unless one has money ",
    "that one don't need and one is happy to donate for lawyers ",
    "charity. Anyhow at some point, rechecking the usage of this text, ",
    "it became uncertain that this text is free to use, because ",
    "the web site in the disclaimer of he eBook containing that text ",
    "was not responding anymore, and at the same time, in projGut, ",
    "searching for first aid no longer found that eBook as well. ",
    "So here we are, with a perhaps much less interesting ",
    "text for the test, but oh much much safer. ",
  };
  
  protected Directory dir;
  protected Analyzer anlzr;
  
  /* @override constructor */
  public FunctionTestSetup(String name) {
    super(name);
  }

  /* @override */
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    dir = null;
    anlzr = null;
  }

  /* @override */
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    // prepare a small index with just a few documents.  
    super.setUp();
    dir = new RAMDirectory();
    anlzr = new StandardAnalyzer(org.apache.lucene.util.Version.LUCENE_CURRENT);
    IndexWriter iw = new IndexWriter(dir, anlzr,
                                     IndexWriter.MaxFieldLength.LIMITED);
    // add docs not exactly in natural ID order, to verify we do check the order of docs by scores
    int remaining = N_DOCS;
    boolean done[] = new boolean[N_DOCS];
    int i = 0;
    while (remaining>0) {
      if (done[i]) {
        throw new Exception("to set this test correctly N_DOCS="+N_DOCS+" must be primary and greater than 2!");
      }
      addDoc(iw,i);
      done[i] = true;
      i = (i+4)%N_DOCS;
      remaining --;
    }
    iw.close();
  }

  private void addDoc(IndexWriter iw, int i) throws Exception {
    Document d = new Document();
    Fieldable f;
    int scoreAndID = i+1;
    
    f = new Field(ID_FIELD,id2String(scoreAndID),Field.Store.YES,Field.Index.NOT_ANALYZED); // for debug purposes
    f.setOmitNorms(true);
    d.add(f);
    
    f = new Field(TEXT_FIELD,"text of doc"+scoreAndID+textLine(i),Field.Store.NO,Field.Index.ANALYZED); // for regular search
    f.setOmitNorms(true);
    d.add(f);
    
    f = new Field(INT_FIELD,""+scoreAndID,Field.Store.NO,Field.Index.NOT_ANALYZED); // for function scoring
    f.setOmitNorms(true);
    d.add(f);
    
    f = new Field(FLOAT_FIELD,scoreAndID+".000",Field.Store.NO,Field.Index.NOT_ANALYZED); // for function scoring
    f.setOmitNorms(true);
    d.add(f);

    iw.addDocument(d);
    log("added: "+d);
  }

  // 17 --> ID00017
  protected String id2String(int scoreAndID) {
    String s = "000000000"+scoreAndID;
    int n = (""+N_DOCS).length() + 3;
    int k = s.length() - n; 
    return "ID"+s.substring(k);
  }
  
  // some text line for regular search
  private String textLine(int docNum) {
    return DOC_TEXT_LINES[docNum % DOC_TEXT_LINES.length];
  }

  // extract expected doc score from its ID Field: "ID7" --> 7.0
  protected float expectedFieldScore(String docIDFieldVal) {
    return Float.parseFloat(docIDFieldVal.substring(2)); 
  }
  
  // debug messages (change DBG to true for anything to print) 
  protected void log (Object o) {
    if (DBG) {
      System.out.println(o.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3758.java