error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/411.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/411.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/411.java
text:
```scala
public D@@ataTokenStream(String text, IntEncoder encoder) {

package org.apache.lucene.facet.search;

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.UnsafeByteArrayOutputStream;
import org.apache.lucene.util.encoding.DGapIntEncoder;
import org.apache.lucene.util.encoding.IntEncoder;
import org.apache.lucene.util.encoding.SortingIntEncoder;
import org.apache.lucene.util.encoding.UniqueValuesIntEncoder;
import org.apache.lucene.util.encoding.VInt8IntEncoder;
import org.junit.Test;

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

public class CategoryListIteratorTest extends LuceneTestCase {

  private static final class DataTokenStream extends TokenStream {

    private int idx;
    private PayloadAttribute payload = addAttribute(PayloadAttribute.class);
    private byte[] buf = new byte[20];
    UnsafeByteArrayOutputStream ubaos = new UnsafeByteArrayOutputStream(buf);
    IntEncoder encoder;
    private boolean exhausted = false;
    private CharTermAttribute term = addAttribute(CharTermAttribute.class);

    public DataTokenStream(String text, IntEncoder encoder) throws IOException {
      this.encoder = encoder;
      term.setEmpty().append(text);
    }

    public void setIdx(int idx) {
      this.idx = idx;
      exhausted = false;
    }

    @Override
    public boolean incrementToken() throws IOException {
      if (exhausted) {
        return false;
      }

      int[] values = data[idx];
      ubaos.reInit(buf);
      encoder.reInit(ubaos);
      for (int val : values) {
        encoder.encode(val);
      }
      encoder.close();
      payload.setPayload(new BytesRef(buf, 0, ubaos.length()));

      exhausted = true;
      return true;
    }

  }

  static final int[][] data = new int[][] {
    new int[] { 1, 2 }, new int[] { 3, 4 }, new int[] { 1, 3 }, new int[] { 1, 2, 3, 4 },
  };

  @Test
  public void testPayloadIntDecodingIterator() throws Exception {
    Directory dir = newDirectory();
    DataTokenStream dts = new DataTokenStream("1",new SortingIntEncoder(
        new UniqueValuesIntEncoder(new DGapIntEncoder(new VInt8IntEncoder()))));
    RandomIndexWriter writer = new RandomIndexWriter(random(), dir, newIndexWriterConfig(TEST_VERSION_CURRENT, 
        new MockAnalyzer(random(), MockTokenizer.KEYWORD, false)).setMergePolicy(newLogMergePolicy()));
    for (int i = 0; i < data.length; i++) {
      dts.setIdx(i);
      Document doc = new Document();
      doc.add(new TextField("f", dts));
      writer.addDocument(doc);
    }
    IndexReader reader = writer.getReader();
    writer.close();

    CategoryListIterator cli = new PayloadIntDecodingIterator(reader, new Term(
        "f","1"), dts.encoder.createMatchingDecoder());
    cli.init();
    int totalCategories = 0;
    for (int i = 0; i < data.length; i++) {
      Set<Integer> values = new HashSet<Integer>();
      for (int j = 0; j < data[i].length; j++) {
        values.add(data[i][j]);
      }
      cli.skipTo(i);
      long cat;
      while ((cat = cli.nextCategory()) < Integer.MAX_VALUE) {
        assertTrue("expected category not found: " + cat, values.contains((int) cat));
        totalCategories ++;
      }
    }
    assertEquals("Missing categories!",10,totalCategories);
    reader.close();
    dir.close();
  }

  /**
   * Test that a document with no payloads does not confuse the payload decoder.
   */
  @Test
  public void testPayloadIteratorWithInvalidDoc() throws Exception {
    Directory dir = newDirectory();
    DataTokenStream dts = new DataTokenStream("1",new SortingIntEncoder(
        new UniqueValuesIntEncoder(new DGapIntEncoder(new VInt8IntEncoder()))));
    // this test requires that no payloads ever be randomly present!
    final Analyzer noPayloadsAnalyzer = new Analyzer() {
      @Override
      public TokenStreamComponents createComponents(String fieldName, Reader reader) {
        return new TokenStreamComponents(new MockTokenizer(reader, MockTokenizer.KEYWORD, false));
      }
    };
    // NOTE: test is wired to LogMP... because test relies on certain docids having payloads
    RandomIndexWriter writer = new RandomIndexWriter(random(), dir, 
        newIndexWriterConfig(TEST_VERSION_CURRENT, noPayloadsAnalyzer).setMergePolicy(newLogMergePolicy()));
    for (int i = 0; i < data.length; i++) {
      Document doc = new Document();
      if (i == 0) {
        dts.setIdx(i);
        doc.add(new TextField("f", dts)); // only doc 0 has payloads!
      } else {
        doc.add(new TextField("f", "1", Field.Store.NO));
      }
      writer.addDocument(doc);
      writer.commit();
    }

    IndexReader reader = writer.getReader();
    writer.close();

    CategoryListIterator cli = new PayloadIntDecodingIterator(reader, new Term(
        "f","1"), dts.encoder.createMatchingDecoder());
    assertTrue("Failed to initialize payload iterator", cli.init());
    int totalCats = 0;
    for (int i = 0; i < data.length; i++) {
      // doc no. i
      Set<Integer> values = new HashSet<Integer>();
      for (int j = 0; j < data[i].length; j++) {
        values.add(data[i][j]);
      }
      boolean hasDoc = cli.skipTo(i);
      if (hasDoc) {
        assertTrue("Document " + i + " must not have a payload!", i == 0);
        long cat;
        while ((cat = cli.nextCategory()) < Integer.MAX_VALUE) {
          assertTrue("expected category not found: " + cat, values.contains((int) cat));
          ++totalCats;
        }
      } else {
        assertFalse("Document " + i + " must have a payload!", i == 0);
      }

    }
    assertEquals("Wrong number of total categories!", 2, totalCats);

    reader.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/411.java