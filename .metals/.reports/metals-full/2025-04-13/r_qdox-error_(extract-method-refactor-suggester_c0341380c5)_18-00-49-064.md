error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3165.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3165.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3165.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.index;

/**
 * Copyright 2006 The Apache Software Foundation
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
import java.util.Random;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;

class RepeatingTokenizer extends Tokenizer {
  
  private final Random random;
  private final float percentDocs;
  private final int maxTF;
  private int num;
  CharTermAttribute termAtt;
  String value;

   public RepeatingTokenizer(String val, Random random, float percentDocs, int maxTF) {
     super();
     this.value = val;
     this.random = random;
     this.percentDocs = percentDocs;
     this.maxTF = maxTF;
     this.termAtt = addAttribute(CharTermAttribute.class);
   }

   @Override
   public boolean incrementToken() throws IOException {
     num--;
     if (num >= 0) {
       clearAttributes();
       termAtt.append(value);
       return true;
     }
     return false;
   }

  @Override
  public void reset() throws IOException {
    super.reset();
    if (random.nextFloat() < percentDocs) {
      num = random.nextInt(maxTF) + 1;
    } else {
      num = 0;
    }
  }
}


public class TestTermdocPerf extends LuceneTestCase {

  void addDocs(final Random random, Directory dir, final int ndocs, String field, final String val, final int maxTF, final float percentDocs) throws IOException {

    Analyzer analyzer = new Analyzer() {
      @Override
      public TokenStreamComponents createComponents(String fieldName) {
        return new TokenStreamComponents(new RepeatingTokenizer(val, random, percentDocs, maxTF));
      }
    };

    Document doc = new Document();
    
    doc.add(newStringField(field, val, Field.Store.NO));
    IndexWriter writer = new IndexWriter(
        dir,
        newIndexWriterConfig(TEST_VERSION_CURRENT, analyzer).
            setOpenMode(OpenMode.CREATE).
            setMaxBufferedDocs(100).
            setMergePolicy(newLogMergePolicy(100))
    );

    for (int i=0; i<ndocs; i++) {
      writer.addDocument(doc);
    }

    writer.forceMerge(1);
    writer.close();
  }


  public int doTest(int iter, int ndocs, int maxTF, float percentDocs) throws IOException {
    Directory dir = newDirectory();

    long start = System.currentTimeMillis();
    addDocs(random(), dir, ndocs, "foo", "val", maxTF, percentDocs);
    long end = System.currentTimeMillis();
    if (VERBOSE) System.out.println("milliseconds for creation of " + ndocs + " docs = " + (end-start));

    IndexReader reader = DirectoryReader.open(dir);

    TermsEnum tenum = MultiFields.getTerms(reader, "foo").iterator(null);

    start = System.currentTimeMillis();

    int ret=0;
    DocsEnum tdocs = null;
    final Random random = new Random(random().nextLong());
    for (int i=0; i<iter; i++) {
      tenum.seekCeil(new BytesRef("val"));
      tdocs = TestUtil.docs(random, tenum, MultiFields.getLiveDocs(reader), tdocs, DocsEnum.FLAG_NONE);
      while (tdocs.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
        ret += tdocs.docID();
      }
    }

    end = System.currentTimeMillis();
    if (VERBOSE) System.out.println("milliseconds for " + iter + " TermDocs iteration: " + (end-start));

    return ret;
  }

  public void testTermDocPerf() throws IOException {
    // performance test for 10% of documents containing a term
    // doTest(100000, 10000,3,.1f);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3165.java