error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2399.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2399.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2399.java
text:
```scala
final S@@tring fileName = SimpleTextPostingsFormat.getPostingsFileName(state.segmentName, state.segmentSuffix);

package org.apache.lucene.index.codecs.simpletext;

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

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.UnicodeUtil;
import org.apache.lucene.index.codecs.FieldsConsumer;
import org.apache.lucene.index.codecs.TermsConsumer;
import org.apache.lucene.index.codecs.PostingsConsumer;
import org.apache.lucene.index.codecs.TermStats;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.SegmentWriteState;
import org.apache.lucene.store.IndexOutput;

import java.io.IOException;
import java.util.Comparator;

class SimpleTextFieldsWriter extends FieldsConsumer {
  
  private final IndexOutput out;
  private final BytesRef scratch = new BytesRef(10);
  final static byte NEWLINE = 10;
  final static byte ESCAPE = 92;

  final static BytesRef END     = new BytesRef("END");
  final static BytesRef FIELD   = new BytesRef("field ");
  final static BytesRef TERM    = new BytesRef("  term ");
  final static BytesRef DOC     = new BytesRef("    doc ");
  final static BytesRef FREQ    = new BytesRef("      freq ");
  final static BytesRef POS     = new BytesRef("      pos ");
  final static BytesRef PAYLOAD = new BytesRef("        payload ");

  public SimpleTextFieldsWriter(SegmentWriteState state) throws IOException {
    final String fileName = SimpleTextPostingsFormat.getPostingsFileName(state.segmentName, state.formatId);
    out = state.directory.createOutput(fileName, state.context);
  }

  private void write(String s) throws IOException {
    UnicodeUtil.UTF16toUTF8(s, 0, s.length(), scratch);
    write(scratch);
  }

  private void write(BytesRef b) throws IOException {
    for(int i=0;i<b.length;i++) {
      final byte bx = b.bytes[b.offset+i];
      if (bx == NEWLINE || bx == ESCAPE) {
        out.writeByte(ESCAPE);
      }
      out.writeByte(bx);
    }
  }

  private void newline() throws IOException {
    out.writeByte(NEWLINE);
  }

  @Override
  public TermsConsumer addField(FieldInfo field) throws IOException {
    write(FIELD);
    write(field.name);
    out.writeByte(NEWLINE);
    return new SimpleTextTermsWriter(field);
  }

  private class SimpleTextTermsWriter extends TermsConsumer {
    private final SimpleTextPostingsWriter postingsWriter;
    
    public SimpleTextTermsWriter(FieldInfo field) {
      postingsWriter = new SimpleTextPostingsWriter(field);
    }

    @Override
    public PostingsConsumer startTerm(BytesRef term) throws IOException {
      return postingsWriter.reset(term);
    }

    @Override
    public void finishTerm(BytesRef term, TermStats stats) throws IOException {
    }

    @Override
    public void finish(long sumTotalTermFreq, long sumDocFreq, int docCount) throws IOException {
    }

    @Override
    public Comparator<BytesRef> getComparator() {
      return BytesRef.getUTF8SortedAsUnicodeComparator();
    }
  }

  private class SimpleTextPostingsWriter extends PostingsConsumer {
    private BytesRef term;
    private boolean wroteTerm;
    private IndexOptions indexOptions;

    public SimpleTextPostingsWriter(FieldInfo field) {
      this.indexOptions = field.indexOptions;
    }

    @Override
    public void startDoc(int docID, int termDocFreq) throws IOException {
      if (!wroteTerm) {
        // we lazily do this, in case the term had zero docs
        write(TERM);
        write(term);
        newline();
        wroteTerm = true;
      }

      write(DOC);
      write(Integer.toString(docID));
      newline();
      if (indexOptions != IndexOptions.DOCS_ONLY) {
        write(FREQ);
        write(Integer.toString(termDocFreq));
        newline();
      }
    }
    
    

    public PostingsConsumer reset(BytesRef term) {
      this.term = term;
      wroteTerm = false;
      return this;
    }

    @Override
    public void addPosition(int position, BytesRef payload) throws IOException {
      write(POS);
      write(Integer.toString(position));
      newline();
      if (payload != null && payload.length > 0) {
        assert payload.length != 0;
        write(PAYLOAD);
        write(payload);
        newline();
      }
    }

    @Override
    public void finishDoc() {
    }
  }

  @Override
  public void close() throws IOException {
    try {
      write(END);
      newline();
    } finally {
      out.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2399.java