error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1673.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1673.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1673.java
text:
```scala
d@@ocsEnum = _TestUtil.docs(random(), termsEnum, null, docsEnum, 0);

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

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.TermVectorsReader;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;

public class TestTermVectorsReader extends LuceneTestCase {
  //Must be lexicographically sorted, will do in setup, versus trying to maintain here
  private String[] testFields = {"f1", "f2", "f3", "f4"};
  private boolean[] testFieldsStorePos = {true, false, true, false};
  private boolean[] testFieldsStoreOff = {true, false, false, true};
  private String[] testTerms = {"this", "is", "a", "test"};
  private int[][] positions = new int[testTerms.length][];
  private Directory dir;
  private SegmentInfoPerCommit seg;
  private FieldInfos fieldInfos = new FieldInfos(new FieldInfo[0]);
  private static int TERM_FREQ = 3;

  private class TestToken implements Comparable<TestToken> {
    String text;
    int pos;
    int startOffset;
    int endOffset;
    public int compareTo(TestToken other) {
      return pos - other.pos;
    }
  }

  TestToken[] tokens = new TestToken[testTerms.length * TERM_FREQ];

  @Override
  public void setUp() throws Exception {
    super.setUp();
    /*
    for (int i = 0; i < testFields.length; i++) {
      fieldInfos.add(testFields[i], true, true, testFieldsStorePos[i], testFieldsStoreOff[i]);
    }
    */

    Arrays.sort(testTerms);
    int tokenUpto = 0;
    for (int i = 0; i < testTerms.length; i++) {
      positions[i] = new int[TERM_FREQ];
      // first position must be 0
      for (int j = 0; j < TERM_FREQ; j++) {
        // positions are always sorted in increasing order
        positions[i][j] = (int) (j * 10 + Math.random() * 10);
        TestToken token = tokens[tokenUpto++] = new TestToken();
        token.text = testTerms[i];
        token.pos = positions[i][j];
        token.startOffset = j * 10;
        token.endOffset = j * 10 + testTerms[i].length();
      }
    }
    Arrays.sort(tokens);

    dir = newDirectory();
    IndexWriter writer = new IndexWriter(
        dir,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MyAnalyzer()).
            setMaxBufferedDocs(-1).
            setMergePolicy(newLogMergePolicy(false, 10))
    );

    Document doc = new Document();
    for(int i=0;i<testFields.length;i++) {
      FieldType customType = new FieldType(TextField.TYPE_NOT_STORED);
      if (testFieldsStorePos[i] && testFieldsStoreOff[i]) {
        customType.setStoreTermVectors(true);
        customType.setStoreTermVectorPositions(true);
        customType.setStoreTermVectorOffsets(true);
      }
      else if (testFieldsStorePos[i] && !testFieldsStoreOff[i]) {
        customType.setStoreTermVectors(true);
        customType.setStoreTermVectorPositions(true);
      }
      else if (!testFieldsStorePos[i] && testFieldsStoreOff[i]) {
        customType.setStoreTermVectors(true);
        customType.setStoreTermVectorOffsets(true);
      }
      else {
        customType.setStoreTermVectors(true);
      }
      doc.add(new Field(testFields[i], "", customType));
    }

    //Create 5 documents for testing, they all have the same
    //terms
    for(int j=0;j<5;j++) {
      writer.addDocument(doc);
    }
    writer.commit();
    seg = writer.newestSegment();
    writer.close();

    fieldInfos = _TestUtil.getFieldInfos(seg.info);
  }
  
  @Override
  public void tearDown() throws Exception {
    dir.close();
    super.tearDown();
  }

  private class MyTokenizer extends Tokenizer {
    private int tokenUpto;
    
    private final CharTermAttribute termAtt;
    private final PositionIncrementAttribute posIncrAtt;
    private final OffsetAttribute offsetAtt;
    
    public MyTokenizer(Reader reader) {
      super(reader);
      termAtt = addAttribute(CharTermAttribute.class);
      posIncrAtt = addAttribute(PositionIncrementAttribute.class);
      offsetAtt = addAttribute(OffsetAttribute.class);
    }
    
    @Override
    public boolean incrementToken() {
      if (tokenUpto >= tokens.length) {
        return false;
      } else {
        final TestToken testToken = tokens[tokenUpto++];
        clearAttributes();
        termAtt.append(testToken.text);
        offsetAtt.setOffset(testToken.startOffset, testToken.endOffset);
        if (tokenUpto > 1) {
          posIncrAtt.setPositionIncrement(testToken.pos - tokens[tokenUpto-2].pos);
        } else {
          posIncrAtt.setPositionIncrement(testToken.pos+1);
        }
        return true;
      }
    }

    @Override
    public void reset() throws IOException {
      super.reset();
      this.tokenUpto = 0;
    }
  }

  private class MyAnalyzer extends Analyzer {
    @Override
    public TokenStreamComponents createComponents(String fieldName, Reader reader) {
      return new TokenStreamComponents(new MyTokenizer(reader));
    }
  }

  public void test() throws IOException {
    //Check to see the files were created properly in setup
    DirectoryReader reader = DirectoryReader.open(dir);
    for (IndexReader r : reader.getSequentialSubReaders()) {
      SegmentInfoPerCommit s = ((SegmentReader) r).getSegmentInfo();
      assertTrue(((SegmentReader) r).getFieldInfos().hasVectors());
    }
    reader.close();
  }

  public void testReader() throws IOException {
    TermVectorsReader reader = Codec.getDefault().termVectorsFormat().vectorsReader(dir, seg.info, fieldInfos, newIOContext(random()));
    for (int j = 0; j < 5; j++) {
      Terms vector = reader.get(j).terms(testFields[0]);
      assertNotNull(vector);
      assertEquals(testTerms.length, vector.size());
      TermsEnum termsEnum = vector.iterator(null);
      for (int i = 0; i < testTerms.length; i++) {
        final BytesRef text = termsEnum.next();
        assertNotNull(text);
        String term = text.utf8ToString();
        //System.out.println("Term: " + term);
        assertEquals(testTerms[i], term);
      }
      assertNull(termsEnum.next());
    }
    reader.close();
  }
  
  public void testDocsEnum() throws IOException {
    TermVectorsReader reader = Codec.getDefault().termVectorsFormat().vectorsReader(dir, seg.info, fieldInfos, newIOContext(random()));
    for (int j = 0; j < 5; j++) {
      Terms vector = reader.get(j).terms(testFields[0]);
      assertNotNull(vector);
      assertEquals(testTerms.length, vector.size());
      TermsEnum termsEnum = vector.iterator(null);
      DocsEnum docsEnum = null;
      for (int i = 0; i < testTerms.length; i++) {
        final BytesRef text = termsEnum.next();
        assertNotNull(text);
        String term = text.utf8ToString();
        //System.out.println("Term: " + term);
        assertEquals(testTerms[i], term);
        
        docsEnum = _TestUtil.docs(random(), termsEnum, null, docsEnum, false);
        assertNotNull(docsEnum);
        int doc = docsEnum.docID();
        assertTrue(doc == -1 || doc == DocIdSetIterator.NO_MORE_DOCS);
        assertTrue(docsEnum.nextDoc() != DocIdSetIterator.NO_MORE_DOCS);
        assertEquals(DocIdSetIterator.NO_MORE_DOCS, docsEnum.nextDoc());
      }
      assertNull(termsEnum.next());
    }
    reader.close();
  }

  public void testPositionReader() throws IOException {
    TermVectorsReader reader = Codec.getDefault().termVectorsFormat().vectorsReader(dir, seg.info, fieldInfos, newIOContext(random()));
    BytesRef[] terms;
    Terms vector = reader.get(0).terms(testFields[0]);
    assertNotNull(vector);
    assertEquals(testTerms.length, vector.size());
    TermsEnum termsEnum = vector.iterator(null);
    DocsAndPositionsEnum dpEnum = null;
    for (int i = 0; i < testTerms.length; i++) {
      final BytesRef text = termsEnum.next();
      assertNotNull(text);
      String term = text.utf8ToString();
      //System.out.println("Term: " + term);
      assertEquals(testTerms[i], term);

      dpEnum = termsEnum.docsAndPositions(null, dpEnum);
      assertNotNull(dpEnum);
      int doc = dpEnum.docID();
      assertTrue(doc == -1 || doc == DocIdSetIterator.NO_MORE_DOCS);
      assertTrue(dpEnum.nextDoc() != DocIdSetIterator.NO_MORE_DOCS);
      assertEquals(dpEnum.freq(), positions[i].length);
      for (int j = 0; j < positions[i].length; j++) {
        assertEquals(positions[i][j], dpEnum.nextPosition());
      }
      assertEquals(DocIdSetIterator.NO_MORE_DOCS, dpEnum.nextDoc());

      dpEnum = termsEnum.docsAndPositions(null, dpEnum);
      doc = dpEnum.docID();
      assertTrue(doc == -1 || doc == DocIdSetIterator.NO_MORE_DOCS);
      assertTrue(dpEnum.nextDoc() != DocIdSetIterator.NO_MORE_DOCS);
      assertNotNull(dpEnum);
      assertEquals(dpEnum.freq(), positions[i].length);
      for (int j = 0; j < positions[i].length; j++) {
        assertEquals(positions[i][j], dpEnum.nextPosition());
        assertEquals(j*10, dpEnum.startOffset());
        assertEquals(j*10 + testTerms[i].length(), dpEnum.endOffset());
      }
      assertEquals(DocIdSetIterator.NO_MORE_DOCS, dpEnum.nextDoc());
    }

    Terms freqVector = reader.get(0).terms(testFields[1]); //no pos, no offset
    assertNotNull(freqVector);
    assertEquals(testTerms.length, freqVector.size());
    termsEnum = freqVector.iterator(null);
    assertNotNull(termsEnum);
    for (int i = 0; i < testTerms.length; i++) {
      final BytesRef text = termsEnum.next();
      assertNotNull(text);
      String term = text.utf8ToString();
      //System.out.println("Term: " + term);
      assertEquals(testTerms[i], term);
    }
    reader.close();
  }

  public void testOffsetReader() throws IOException {
    TermVectorsReader reader = Codec.getDefault().termVectorsFormat().vectorsReader(dir, seg.info, fieldInfos, newIOContext(random()));
    Terms vector = reader.get(0).terms(testFields[0]);
    assertNotNull(vector);
    TermsEnum termsEnum = vector.iterator(null);
    assertNotNull(termsEnum);
    assertEquals(testTerms.length, vector.size());
    DocsAndPositionsEnum dpEnum = null;
    for (int i = 0; i < testTerms.length; i++) {
      final BytesRef text = termsEnum.next();
      assertNotNull(text);
      String term = text.utf8ToString();
      assertEquals(testTerms[i], term);

      dpEnum = termsEnum.docsAndPositions(null, dpEnum);
      assertNotNull(dpEnum);
      assertTrue(dpEnum.nextDoc() != DocIdSetIterator.NO_MORE_DOCS);
      assertEquals(dpEnum.freq(), positions[i].length);
      for (int j = 0; j < positions[i].length; j++) {
        assertEquals(positions[i][j], dpEnum.nextPosition());
      }
      assertEquals(DocIdSetIterator.NO_MORE_DOCS, dpEnum.nextDoc());

      dpEnum = termsEnum.docsAndPositions(null, dpEnum);
      assertTrue(dpEnum.nextDoc() != DocIdSetIterator.NO_MORE_DOCS);
      assertNotNull(dpEnum);
      assertEquals(dpEnum.freq(), positions[i].length);
      for (int j = 0; j < positions[i].length; j++) {
        assertEquals(positions[i][j], dpEnum.nextPosition());
        assertEquals(j*10, dpEnum.startOffset());
        assertEquals(j*10 + testTerms[i].length(), dpEnum.endOffset());
      }
      assertEquals(DocIdSetIterator.NO_MORE_DOCS, dpEnum.nextDoc());
    }
    reader.close();
  }

  /**
   * Make sure exceptions and bad params are handled appropriately
   */
  public void testBadParams() throws IOException {
    TermVectorsReader reader = null;
    try {
      reader = Codec.getDefault().termVectorsFormat().vectorsReader(dir, seg.info, fieldInfos, newIOContext(random()));
      //Bad document number, good field number
      reader.get(50);
      fail();
    } catch (IllegalArgumentException e) {
      // expected exception
    } finally {
      reader.close();
    }
    reader = Codec.getDefault().termVectorsFormat().vectorsReader(dir, seg.info, fieldInfos, newIOContext(random()));
    //good document number, bad field
    Terms vector = reader.get(0).terms("f50");
    assertNull(vector);
    reader.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1673.java