error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/81.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/81.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/81.java
text:
```scala
i@@dField.setStringValue(""+i);

package org.apache.lucene;

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

import java.io.*;
import java.util.*;

import org.apache.lucene.analysis.*;
import org.apache.lucene.codecs.*;
import org.apache.lucene.codecs.lucene40.Lucene40Codec;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.*;

/* Intentionally outside of oal.index to verify fully
   external codecs work fine */

public class TestExternalCodecs extends LuceneTestCase {

  private static final class CustomPerFieldCodec extends Lucene40Codec {
    
    private final PostingsFormat ramFormat = PostingsFormat.forName("RAMOnly");
    private final PostingsFormat defaultFormat = PostingsFormat.forName("Lucene40");
    private final PostingsFormat pulsingFormat = PostingsFormat.forName("Pulsing40");

    @Override
    public PostingsFormat getPostingsFormatForField(String field) {
      if (field.equals("field2") || field.equals("id")) {
        return pulsingFormat;
      } else if (field.equals("field1")) {
        return defaultFormat;
      } else {
        return ramFormat;
      }
    }
  }

  // tests storing "id" and "field2" fields as pulsing codec,
  // whose term sort is backwards unicode code point, and
  // storing "field1" as a custom entirely-in-RAM codec
  public void testPerFieldCodec() throws Exception {
    
    final int NUM_DOCS = atLeast(173);
    if (VERBOSE) {
      System.out.println("TEST: NUM_DOCS=" + NUM_DOCS);
    }

    MockDirectoryWrapper dir = newDirectory();
    dir.setCheckIndexOnClose(false); // we use a custom codec provider
    IndexWriter w = new IndexWriter(
        dir,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random)).
        setCodec(new CustomPerFieldCodec()).
            setMergePolicy(newLogMergePolicy(3))
    );
    Document doc = new Document();
    // uses default codec:
    doc.add(newField("field1", "this field uses the standard codec as the test", TextField.TYPE_UNSTORED));
    // uses pulsing codec:
    Field field2 = newField("field2", "this field uses the pulsing codec as the test", TextField.TYPE_UNSTORED);
    doc.add(field2);
    
    Field idField = newField("id", "", StringField.TYPE_UNSTORED);

    doc.add(idField);
    for(int i=0;i<NUM_DOCS;i++) {
      idField.setValue(""+i);
      w.addDocument(doc);
      if ((i+1)%10 == 0) {
        w.commit();
      }
    }
    if (VERBOSE) {
      System.out.println("TEST: now delete id=77");
    }
    w.deleteDocuments(new Term("id", "77"));

    IndexReader r = IndexReader.open(w, true);
    
    assertEquals(NUM_DOCS-1, r.numDocs());
    IndexSearcher s = newSearcher(r);
    assertEquals(NUM_DOCS-1, s.search(new TermQuery(new Term("field1", "standard")), 1).totalHits);
    assertEquals(NUM_DOCS-1, s.search(new TermQuery(new Term("field2", "pulsing")), 1).totalHits);
    r.close();

    if (VERBOSE) {
      System.out.println("\nTEST: now delete 2nd doc");
    }
    w.deleteDocuments(new Term("id", "44"));

    if (VERBOSE) {
      System.out.println("\nTEST: now force merge");
    }
    w.forceMerge(1);
    if (VERBOSE) {
      System.out.println("\nTEST: now open reader");
    }
    r = IndexReader.open(w, true);
    assertEquals(NUM_DOCS-2, r.maxDoc());
    assertEquals(NUM_DOCS-2, r.numDocs());
    s = newSearcher(r);
    assertEquals(NUM_DOCS-2, s.search(new TermQuery(new Term("field1", "standard")), 1).totalHits);
    assertEquals(NUM_DOCS-2, s.search(new TermQuery(new Term("field2", "pulsing")), 1).totalHits);
    assertEquals(1, s.search(new TermQuery(new Term("id", "76")), 1).totalHits);
    assertEquals(0, s.search(new TermQuery(new Term("id", "77")), 1).totalHits);
    assertEquals(0, s.search(new TermQuery(new Term("id", "44")), 1).totalHits);

    if (VERBOSE) {
      System.out.println("\nTEST: now close NRT reader");
    }
    r.close();

    w.close();

    dir.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/81.java