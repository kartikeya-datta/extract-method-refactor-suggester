error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2580.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2580.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2580.java
text:
```scala
s@@uper(maxSize);

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
package org.apache.lucene.benchmark.quality.utils;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.PriorityQueue;

/**
 * Suggest Quality queries based on an index contents.
 * Utility class, used for making quality test benchmarks.
 */
public class QualityQueriesFinder {

  private static final String newline = System.getProperty("line.separator");
  private Directory dir;
  
  /**
   * Constructor over a directory containing the index.
   * @param dir directory containing the index we search for the quality test. 
   */
  private QualityQueriesFinder(Directory dir) {
    this.dir = dir;
  }

  /**
   * @param args {index-dir}
   * @throws IOException  if cannot access the index.
   */
  public static void main(String[] args) throws IOException {
    if (args.length<1) {
      System.err.println("Usage: java QualityQueriesFinder <index-dir>");
      System.exit(1);
    }
    QualityQueriesFinder qqf = new QualityQueriesFinder(FSDirectory.open(new File(args[0])));
    String q[] = qqf.bestQueries("body",20);
    for (int i=0; i<q.length; i++) {
      System.out.println(newline+formatQueryAsTrecTopic(i,q[i],null,null));
    }
  }

  private String [] bestQueries(String field,int numQueries) throws IOException {
    String words[] = bestTerms("body",4*numQueries);
    int n = words.length;
    int m = n/4;
    String res[] = new String[m];
    for (int i=0; i<res.length; i++) {
      res[i] = words[i] + " " + words[m+i]+ "  " + words[n-1-m-i]  + " " + words[n-1-i];
      //System.out.println("query["+i+"]:  "+res[i]);
    }
    return res;
  }
  
  private static String formatQueryAsTrecTopic (int qnum, String title, String description, String narrative) {
    return 
      "<top>" + newline +
      "<num> Number: " + qnum             + newline + newline + 
      "<title> " + (title==null?"":title) + newline + newline + 
      "<desc> Description:"               + newline +
      (description==null?"":description)  + newline + newline +
      "<narr> Narrative:"                 + newline +
      (narrative==null?"":narrative)      + newline + newline +
      "</top>";
  }
  
  private String [] bestTerms(String field,int numTerms) throws IOException {
    PriorityQueue<TermDf> pq = new TermsDfQueue(numTerms);
    IndexReader ir = IndexReader.open(dir, true);
    try {
      int threshold = ir.maxDoc() / 10; // ignore words too common.
      Terms terms = MultiFields.getTerms(ir, field);
      if (terms != null) {
        TermsEnum termsEnum = terms.iterator();
        while (termsEnum.next() != null) {
          int df = termsEnum.docFreq();
          if (df<threshold) {
            String ttxt = termsEnum.term().utf8ToString();
            pq.insertWithOverflow(new TermDf(ttxt,df));
          }
        }
      }
    } finally {
      ir.close();
    }
    String res[] = new String[pq.size()];
    int i = 0;
    while (pq.size()>0) {
      TermDf tdf = pq.pop(); 
      res[i++] = tdf.word;
      System.out.println(i+".   word:  "+tdf.df+"   "+tdf.word);
    }
    return res;
  }

  private static class TermDf {
    String word;
    int df;
    TermDf (String word, int freq) {
      this.word = word;
      this.df = freq;
    }
  }
  
  private static class TermsDfQueue extends PriorityQueue<TermDf> {
    TermsDfQueue (int maxSize) {
      initialize(maxSize);
    }
    @Override
    protected boolean lessThan(TermDf tf1, TermDf tf2) {
      return tf1.df < tf2.df;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2580.java