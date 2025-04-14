error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3153.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3153.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3153.java
text:
```scala
w@@riter.shutdown();

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
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.codecs.lucene41.Lucene41PostingsFormat;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.MockDirectoryWrapper;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;
import org.junit.Before;

/**
 * This testcase tests whether multi-level skipping is being used
 * to reduce I/O while skipping through posting lists.
 * 
 * Skipping in general is already covered by several other
 * testcases.
 * 
 */
public class TestMultiLevelSkipList extends LuceneTestCase {
  
  class CountingRAMDirectory extends MockDirectoryWrapper {
    public CountingRAMDirectory(Directory delegate) {
      super(random(), delegate);
    }

    @Override
    public IndexInput openInput(String fileName, IOContext context) throws IOException {
      IndexInput in = super.openInput(fileName, context);
      if (fileName.endsWith(".frq"))
        in = new CountingStream(in);
      return in;
    }
  }
  
  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    counter = 0;
  }

  public void testSimpleSkip() throws IOException {
    Directory dir = new CountingRAMDirectory(new RAMDirectory());
    IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig( TEST_VERSION_CURRENT, new PayloadAnalyzer()).setCodec(TestUtil.alwaysPostingsFormat(new Lucene41PostingsFormat())).setMergePolicy(newLogMergePolicy()));
    Term term = new Term("test", "a");
    for (int i = 0; i < 5000; i++) {
      Document d1 = new Document();
      d1.add(newTextField(term.field(), term.text(), Field.Store.NO));
      writer.addDocument(d1);
    }
    writer.commit();
    writer.forceMerge(1);
    writer.close();

    AtomicReader reader = getOnlySegmentReader(DirectoryReader.open(dir));
    
    for (int i = 0; i < 2; i++) {
      counter = 0;
      DocsAndPositionsEnum tp = reader.termPositionsEnum(term);
      checkSkipTo(tp, 14, 185); // no skips
      checkSkipTo(tp, 17, 190); // one skip on level 0
      checkSkipTo(tp, 287, 200); // one skip on level 1, two on level 0
    
      // this test would fail if we had only one skip level,
      // because than more bytes would be read from the freqStream
      checkSkipTo(tp, 4800, 250);// one skip on level 2
    }
  }

  public void checkSkipTo(DocsAndPositionsEnum tp, int target, int maxCounter) throws IOException {
    tp.advance(target);
    if (maxCounter < counter) {
      fail("Too many bytes read: " + counter + " vs " + maxCounter);
    }

    assertEquals("Wrong document " + tp.docID() + " after skipTo target " + target, target, tp.docID());
    assertEquals("Frequency is not 1: " + tp.freq(), 1,tp.freq());
    tp.nextPosition();
    BytesRef b = tp.getPayload();
    assertEquals(1, b.length);
    assertEquals("Wrong payload for the target " + target + ": " + b.bytes[b.offset], (byte) target, b.bytes[b.offset]);
  }

  private static class PayloadAnalyzer extends Analyzer {
    private final AtomicInteger payloadCount = new AtomicInteger(-1);
    @Override
    public TokenStreamComponents createComponents(String fieldName) {
      Tokenizer tokenizer = new MockTokenizer(MockTokenizer.WHITESPACE, true);
      return new TokenStreamComponents(tokenizer, new PayloadFilter(payloadCount, tokenizer));
    }

  }

  private static class PayloadFilter extends TokenFilter {
    
    PayloadAttribute payloadAtt;
    private AtomicInteger payloadCount;
    
    protected PayloadFilter(AtomicInteger payloadCount , TokenStream input) {
      super(input);
      this.payloadCount = payloadCount;
      payloadAtt = addAttribute(PayloadAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {
      boolean hasNext = input.incrementToken();
      if (hasNext) {
        payloadAtt.setPayload(new BytesRef(new byte[] { (byte) payloadCount.incrementAndGet() }));
      } 
      return hasNext;
    }

  }

  private int counter = 0;

  // Simply extends IndexInput in a way that we are able to count the number
  // of bytes read
  class CountingStream extends IndexInput {
    private IndexInput input;

    CountingStream(IndexInput input) {
      super("CountingStream(" + input + ")");
      this.input = input;
    }

    @Override
    public byte readByte() throws IOException {
      TestMultiLevelSkipList.this.counter++;
      return this.input.readByte();
    }

    @Override
    public void readBytes(byte[] b, int offset, int len) throws IOException {
      TestMultiLevelSkipList.this.counter += len;
      this.input.readBytes(b, offset, len);
    }

    @Override
    public void close() throws IOException {
      this.input.close();
    }

    @Override
    public long getFilePointer() {
      return this.input.getFilePointer();
    }

    @Override
    public void seek(long pos) throws IOException {
      this.input.seek(pos);
    }

    @Override
    public long length() {
      return this.input.length();
    }

    @Override
    public CountingStream clone() {
      return new CountingStream(this.input.clone());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3153.java