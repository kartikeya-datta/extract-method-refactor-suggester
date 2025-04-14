error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3161.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3161.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3161.java
text:
```scala
w@@.shutdown();

package org.apache.lucene.index;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.util.*;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.*;
import org.apache.lucene.document.*;

public class TestStressAdvance extends LuceneTestCase {

  public void testStressAdvance() throws Exception {
    for(int iter=0;iter<3;iter++) {
      if (VERBOSE) {
        System.out.println("\nTEST: iter=" + iter);
      }
      Directory dir = newDirectory();
      RandomIndexWriter w = new RandomIndexWriter(random(), dir);
      final Set<Integer> aDocs = new HashSet<>();
      final Document doc = new Document();
      final Field f = newStringField("field", "", Field.Store.NO);
      doc.add(f);
      final Field idField = newStringField("id", "", Field.Store.YES);
      doc.add(idField);
      int num = atLeast(4097);
      if (VERBOSE) {
        System.out.println("\nTEST: numDocs=" + num);
      }
      for(int id=0;id<num;id++) {
        if (random().nextInt(4) == 3) {
          f.setStringValue("a");
          aDocs.add(id);
        } else {
          f.setStringValue("b");
        }
        idField.setStringValue(""+id);
        w.addDocument(doc);
        if (VERBOSE) {
          System.out.println("\nTEST: doc upto " + id);
        }
      }

      w.forceMerge(1);

      final List<Integer> aDocIDs = new ArrayList<>();
      final List<Integer> bDocIDs = new ArrayList<>();

      final DirectoryReader r = w.getReader();
      final int[] idToDocID = new int[r.maxDoc()];
      for(int docID=0;docID<idToDocID.length;docID++) {
        int id = Integer.parseInt(r.document(docID).get("id"));
        if (aDocs.contains(id)) {
          aDocIDs.add(docID);
        } else {
          bDocIDs.add(docID);
        }
      }
      final TermsEnum te = getOnlySegmentReader(r).fields().terms("field").iterator(null);
      
      DocsEnum de = null;
      for(int iter2=0;iter2<10;iter2++) {
        if (VERBOSE) {
          System.out.println("\nTEST: iter=" + iter + " iter2=" + iter2);
        }
        assertEquals(TermsEnum.SeekStatus.FOUND, te.seekCeil(new BytesRef("a")));
        de = TestUtil.docs(random(), te, null, de, DocsEnum.FLAG_NONE);
        testOne(de, aDocIDs);

        assertEquals(TermsEnum.SeekStatus.FOUND, te.seekCeil(new BytesRef("b")));
        de = TestUtil.docs(random(), te, null, de, DocsEnum.FLAG_NONE);
        testOne(de, bDocIDs);
      }

      w.close();
      r.close();
      dir.close();
    }
  }

  private void testOne(DocsEnum docs, List<Integer> expected) throws Exception {
    if (VERBOSE) {
      System.out.println("test");
    }
    int upto = -1;
    while(upto < expected.size()) {
      if (VERBOSE) {
        System.out.println("  cycle upto=" + upto + " of " + expected.size());
      }
      final int docID;
      if (random().nextInt(4) == 1 || upto == expected.size()-1) {
        // test nextDoc()
        if (VERBOSE) {
          System.out.println("    do nextDoc");
        }
        upto++;
        docID = docs.nextDoc();
      } else {
        // test advance()
        final int inc = TestUtil.nextInt(random(), 1, expected.size() - 1 - upto);
        if (VERBOSE) {
          System.out.println("    do advance inc=" + inc);
        }
        upto += inc;
        docID = docs.advance(expected.get(upto));
      }
      if (upto == expected.size()) {
        if (VERBOSE) {
          System.out.println("  expect docID=" + DocIdSetIterator.NO_MORE_DOCS + " actual=" + docID);
        }
        assertEquals(DocIdSetIterator.NO_MORE_DOCS, docID);
      } else {
        if (VERBOSE) {
          System.out.println("  expect docID=" + expected.get(upto) + " actual=" + docID);
        }
        assertTrue(docID != DocIdSetIterator.NO_MORE_DOCS);
        assertEquals(expected.get(upto).intValue(), docID);
      }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3161.java