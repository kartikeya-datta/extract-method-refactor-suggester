error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/459.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/459.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/459.java
text:
```scala
private i@@nt parseIntAt(BytesRef bytes, int offset, CharsRef scratch) {

package org.apache.lucene.codecs.simpletext;

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
import java.util.BitSet;
import java.util.Collection;

import org.apache.lucene.codecs.LiveDocsFormat;
import org.apache.lucene.index.IndexFileNames;
import org.apache.lucene.index.SegmentInfoPerCommit;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.MutableBits;
import org.apache.lucene.util.StringHelper;
import org.apache.lucene.util.UnicodeUtil;

/**
 * reads/writes plaintext live docs
 * <p>
 * <b><font color="red">FOR RECREATIONAL USE ONLY</font></B>
 * @lucene.experimental
 */
public class SimpleTextLiveDocsFormat extends LiveDocsFormat {

  static final String LIVEDOCS_EXTENSION = "liv";
  
  final static BytesRef SIZE             = new BytesRef("size ");
  final static BytesRef DOC              = new BytesRef("  doc ");
  final static BytesRef END              = new BytesRef("END");
  
  @Override
  public MutableBits newLiveDocs(int size) throws IOException {
    return new SimpleTextMutableBits(size);
  }

  @Override
  public MutableBits newLiveDocs(Bits existing) throws IOException {
    final SimpleTextBits bits = (SimpleTextBits) existing;
    return new SimpleTextMutableBits((BitSet)bits.bits.clone(), bits.size);
  }

  @Override
  public Bits readLiveDocs(Directory dir, SegmentInfoPerCommit info, IOContext context) throws IOException {
    assert info.hasDeletions();
    BytesRef scratch = new BytesRef();
    CharsRef scratchUTF16 = new CharsRef();
    
    String fileName = IndexFileNames.fileNameFromGeneration(info.info.name, LIVEDOCS_EXTENSION, info.getDelGen());
    IndexInput in = null;
    boolean success = false;
    try {
      in = dir.openInput(fileName, context);
      
      SimpleTextUtil.readLine(in, scratch);
      assert StringHelper.startsWith(scratch, SIZE);
      int size = parseIntAt(scratch, SIZE.length, scratchUTF16);
      
      BitSet bits = new BitSet(size);
      
      SimpleTextUtil.readLine(in, scratch);
      while (!scratch.equals(END)) {
        assert StringHelper.startsWith(scratch, DOC);
        int docid = parseIntAt(scratch, DOC.length, scratchUTF16);
        bits.set(docid);
        SimpleTextUtil.readLine(in, scratch);
      }
      
      success = true;
      return new SimpleTextBits(bits, size);
    } finally {
      if (success) {
        IOUtils.close(in);
      } else {
        IOUtils.closeWhileHandlingException(in);
      }
    }
  }
  
  private int parseIntAt(BytesRef bytes, int offset, CharsRef scratch) throws IOException {
    UnicodeUtil.UTF8toUTF16(bytes.bytes, bytes.offset+offset, bytes.length-offset, scratch);
    return ArrayUtil.parseInt(scratch.chars, 0, scratch.length);
  }

  @Override
  public void writeLiveDocs(MutableBits bits, Directory dir, SegmentInfoPerCommit info, int newDelCount, IOContext context) throws IOException {
    BitSet set = ((SimpleTextBits) bits).bits;
    int size = bits.length();
    BytesRef scratch = new BytesRef();
    
    String fileName = IndexFileNames.fileNameFromGeneration(info.info.name, LIVEDOCS_EXTENSION, info.getNextDelGen());
    IndexOutput out = null;
    boolean success = false;
    try {
      out = dir.createOutput(fileName, context);
      SimpleTextUtil.write(out, SIZE);
      SimpleTextUtil.write(out, Integer.toString(size), scratch);
      SimpleTextUtil.writeNewline(out);
      
      for (int i = set.nextSetBit(0); i >= 0; i=set.nextSetBit(i + 1)) { 
        SimpleTextUtil.write(out, DOC);
        SimpleTextUtil.write(out, Integer.toString(i), scratch);
        SimpleTextUtil.writeNewline(out);
      }
      
      SimpleTextUtil.write(out, END);
      SimpleTextUtil.writeNewline(out);
      success = true;
    } finally {
      if (success) {
        IOUtils.close(out);
      } else {
        IOUtils.closeWhileHandlingException(out);
      }
    }
  }

  @Override
  public void files(SegmentInfoPerCommit info, Collection<String> files) throws IOException {
    if (info.hasDeletions()) {
      files.add(IndexFileNames.fileNameFromGeneration(info.info.name, LIVEDOCS_EXTENSION, info.getDelGen()));
    }
  }
  
  // read-only
  static class SimpleTextBits implements Bits {
    final BitSet bits;
    final int size;
    
    SimpleTextBits(BitSet bits, int size) {
      this.bits = bits;
      this.size = size;
    }
    
    @Override
    public boolean get(int index) {
      return bits.get(index);
    }

    @Override
    public int length() {
      return size;
    }
  }
  
  // read-write
  static class SimpleTextMutableBits extends SimpleTextBits implements MutableBits {

    SimpleTextMutableBits(int size) {
      this(new BitSet(size), size);
      bits.set(0, size);
    }
    
    SimpleTextMutableBits(BitSet bits, int size) {
      super(bits, size);
    }
    
    @Override
    public void clear(int bit) {
      bits.clear(bit);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/459.java