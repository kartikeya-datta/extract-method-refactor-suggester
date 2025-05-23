error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3462.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3462.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3462.java
text:
```scala
D@@irectory dir = newFSDirectory(_TestUtil.getTempDir("longpostings" + "." + random.nextLong()));

package org.apache.lucene.index;

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

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermToBytesRefAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.codecs.CodecProvider;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.OpenBitSet;
import org.apache.lucene.util._TestUtil;

public class TestLongPostings extends LuceneTestCase {

  // Produces a realistic unicode random string that
  // survives MockAnalyzer unchanged:
  private String getRandomTerm(String other) throws IOException {
    Analyzer a = new MockAnalyzer(random);
    while(true) {
      String s = _TestUtil.randomRealisticUnicodeString(random);
      if (other != null && s.equals(other)) {
        continue;
      }
      final TokenStream ts = a.tokenStream("foo", new StringReader(s));
      final TermToBytesRefAttribute termAtt = ts.getAttribute(TermToBytesRefAttribute.class);
      final BytesRef termBytes = termAtt.getBytesRef();
      int count = 0;
      while(ts.incrementToken()) {
        termAtt.fillBytesRef();
        if (count == 0 && !termBytes.utf8ToString().equals(s)) {
          break;
        }
        count++;
      }
      if (count == 1) {
        return s;
      }
    }
  }

  public void testLongPostings() throws Exception {
    assumeFalse("Too slow with SimpleText codec", CodecProvider.getDefault().getFieldCodec("field").equals("SimpleText"));

    // Don't use _TestUtil.getTempDir so that we own the
    // randomness (ie same seed will point to same dir):
    Directory dir = newFSDirectory(new File(LuceneTestCase.TEMP_DIR, "longpostings" + "." + random.nextLong()));

    final int NUM_DOCS = (int) ((TEST_NIGHTLY ? 4e6 : (RANDOM_MULTIPLIER*2e4)) * (1+random.nextDouble()));

    if (VERBOSE) {
      System.out.println("TEST: NUM_DOCS=" + NUM_DOCS);
    }

    final String s1 = getRandomTerm(null);
    final String s2 = getRandomTerm(s1);

    if (VERBOSE) {
      System.out.println("\nTEST: s1=" + s1 + " s2=" + s2);
      /*
      for(int idx=0;idx<s1.length();idx++) {
        System.out.println("  s1 ch=0x" + Integer.toHexString(s1.charAt(idx)));
      }
      for(int idx=0;idx<s2.length();idx++) {
        System.out.println("  s2 ch=0x" + Integer.toHexString(s2.charAt(idx)));
      }
      */
    }

    final OpenBitSet isS1 = new OpenBitSet(NUM_DOCS);
    for(int idx=0;idx<NUM_DOCS;idx++) {
      if (random.nextBoolean()) {
        isS1.set(idx);
      }
    }

    final IndexReader r;
    if (true) { 
      final IndexWriterConfig iwc = newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random))
        .setOpenMode(IndexWriterConfig.OpenMode.CREATE)
        .setMergePolicy(newLogMergePolicy());
      iwc.setRAMBufferSizeMB(16.0 + 16.0 * random.nextDouble());
      iwc.setMaxBufferedDocs(-1);
      final RandomIndexWriter riw = new RandomIndexWriter(random, dir, iwc);

      for(int idx=0;idx<NUM_DOCS;idx++) {
        final Document doc = new Document();
        String s = isS1.get(idx) ? s1 : s2;
        final Field f = newField("field", s, Field.Index.ANALYZED);
        final int count = _TestUtil.nextInt(random, 1, 4);
        for(int ct=0;ct<count;ct++) {
          doc.add(f);
        }
        riw.addDocument(doc);
      }

      r = riw.getReader();
      riw.close();
    } else {
      r = IndexReader.open(dir);
    }

    /*
    if (VERBOSE) {
      System.out.println("TEST: terms");
      TermEnum termEnum = r.terms();
      while(termEnum.next()) {
        System.out.println("  term=" + termEnum.term() + " len=" + termEnum.term().text().length());
        assertTrue(termEnum.docFreq() > 0);
        System.out.println("    s1?=" + (termEnum.term().text().equals(s1)) + " s1len=" + s1.length());
        System.out.println("    s2?=" + (termEnum.term().text().equals(s2)) + " s2len=" + s2.length());
        final String s = termEnum.term().text();
        for(int idx=0;idx<s.length();idx++) {
          System.out.println("      ch=0x" + Integer.toHexString(s.charAt(idx)));
        }
      }
    }
    */

    assertEquals(NUM_DOCS, r.numDocs());
    assertTrue(r.docFreq(new Term("field", s1)) > 0);
    assertTrue(r.docFreq(new Term("field", s2)) > 0);

    for(int iter=0;iter<1000*RANDOM_MULTIPLIER;iter++) {

      final String term;
      final boolean doS1;
      if (random.nextBoolean()) {
        term = s1;
        doS1 = true;
      } else {
        term = s2;
        doS1 = false;
      }

      if (VERBOSE) {
        System.out.println("\nTEST: iter=" + iter + " doS1=" + doS1);
      }
        
      final DocsAndPositionsEnum postings = MultiFields.getTermPositionsEnum(r, null, "field", new BytesRef(term));

      int docID = -1;
      while(docID < DocsEnum.NO_MORE_DOCS) {
        final int what = random.nextInt(3);
        if (what == 0) {
          if (VERBOSE) {
            System.out.println("TEST: docID=" + docID + "; do next()");
          }
          // nextDoc
          int expected = docID+1;
          while(true) {
            if (expected == NUM_DOCS) {
              expected = Integer.MAX_VALUE;
              break;
            } else if (isS1.get(expected) == doS1) {
              break;
            } else {
              expected++;
            }
          }
          docID = postings.nextDoc();
          if (VERBOSE) {
            System.out.println("  got docID=" + docID);
          }
          assertEquals(expected, docID);
          if (docID == DocsEnum.NO_MORE_DOCS) {
            break;
          }

          if (random.nextInt(6) == 3) {
            final int freq = postings.freq();
            assertTrue(freq >=1 && freq <= 4);
            for(int pos=0;pos<freq;pos++) {
              assertEquals(pos, postings.nextPosition());
              if (random.nextBoolean() && postings.hasPayload()) {
                postings.getPayload();
              }
            }
          }
        } else {
          // advance
          final int targetDocID;
          if (docID == -1) {
            targetDocID = random.nextInt(NUM_DOCS+1);
          } else {
            targetDocID = docID + _TestUtil.nextInt(random, 1, NUM_DOCS - docID);
          }
          if (VERBOSE) {
            System.out.println("TEST: docID=" + docID + "; do advance(" + targetDocID + ")");
          }
          int expected = targetDocID;
          while(true) {
            if (expected == NUM_DOCS) {
              expected = Integer.MAX_VALUE;
              break;
            } else if (isS1.get(expected) == doS1) {
              break;
            } else {
              expected++;
            }
          }
          
          docID = postings.advance(targetDocID);
          if (VERBOSE) {
            System.out.println("  got docID=" + docID);
          }
          assertEquals(expected, docID);
          if (docID == DocsEnum.NO_MORE_DOCS) {
            break;
          }
          
          if (random.nextInt(6) == 3) {
            final int freq = postings.freq();
            assertTrue(freq >=1 && freq <= 4);
            for(int pos=0;pos<freq;pos++) {
              assertEquals(pos, postings.nextPosition());
              if (random.nextBoolean() && postings.hasPayload()) {
                postings.getPayload();
              }
            }
          }
        }
      }
    }
    r.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3462.java