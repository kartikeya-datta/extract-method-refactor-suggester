error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3118.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3118.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3118.java
text:
```scala
w@@riter.shutdown();

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
package org.apache.lucene.search;


import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;
import org.junit.Test;


public class FuzzyTermOnShortTermsTest extends LuceneTestCase {
   private final static String FIELD = "field";
   
   @Test
   public void test() throws Exception {
      // proves rule that edit distance between the two terms
      // must be > smaller term for there to be a match
      Analyzer a = getAnalyzer();
      //these work
      countHits(a, new String[]{"abc"}, new FuzzyQuery(new Term(FIELD, "ab"), 1), 1);
      countHits(a, new String[]{"ab"}, new FuzzyQuery(new Term(FIELD, "abc"), 1), 1);

      countHits(a, new String[]{"abcde"}, new FuzzyQuery(new Term(FIELD, "abc"), 2), 1);
      countHits(a, new String[]{"abc"}, new FuzzyQuery(new Term(FIELD, "abcde"), 2), 1);
      
      //these don't      
      countHits(a, new String[]{"ab"}, new FuzzyQuery(new Term(FIELD, "a"), 1), 0);
      countHits(a, new String[]{"a"}, new FuzzyQuery(new Term(FIELD, "ab"), 1), 0);
      
      countHits(a, new String[]{"abc"}, new FuzzyQuery(new Term(FIELD, "a"), 2), 0);
      countHits(a, new String[]{"a"}, new FuzzyQuery(new Term(FIELD, "abc"), 2), 0);

      countHits(a, new String[]{"abcd"}, new FuzzyQuery(new Term(FIELD, "ab"), 2), 0);
      countHits(a, new String[]{"ab"}, new FuzzyQuery(new Term(FIELD, "abcd"), 2), 0);
   }
   
   private void countHits(Analyzer analyzer, String[] docs, Query q, int expected) throws Exception {
      Directory d = getDirectory(analyzer, docs);
      IndexReader r = DirectoryReader.open(d);
      IndexSearcher s = new IndexSearcher(r);
      TotalHitCountCollector c = new TotalHitCountCollector();
      s.search(q,  c);
      assertEquals(q.toString(), expected, c.getTotalHits());
      r.close();
      d.close();
   }
   
   public static Analyzer getAnalyzer(){
      return new Analyzer() {
         @Override
         public TokenStreamComponents createComponents(String fieldName) {
            Tokenizer tokenizer = new MockTokenizer(MockTokenizer.SIMPLE, true);
            return new TokenStreamComponents(tokenizer, tokenizer);
         }
      };
   }
   public static Directory getDirectory(Analyzer analyzer, String[] vals) throws IOException{
      Directory directory = newDirectory();
      RandomIndexWriter writer = new RandomIndexWriter(random(), directory,
          newIndexWriterConfig(TEST_VERSION_CURRENT, analyzer)
          .setMaxBufferedDocs(TestUtil.nextInt(random(), 100, 1000)).setMergePolicy(newLogMergePolicy()));

      for (String s : vals){
         Document d = new Document();
         d.add(newTextField(FIELD, s, Field.Store.YES));
         writer.addDocument(d);
            
      }
      writer.close();
      return directory;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3118.java