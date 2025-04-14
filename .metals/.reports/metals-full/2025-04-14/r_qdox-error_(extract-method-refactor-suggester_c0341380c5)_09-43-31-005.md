error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1784.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1784.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1784.java
text:
```scala
m@@axValue = Math.max(val, maxValue);

package org.apache.lucene.codecs;

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

import java.io.Closeable;
import java.io.IOException;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.index.DocValues.SortedSource;
import org.apache.lucene.index.DocValues.Source;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.MergeState;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;

// prototype streaming DV api
public abstract class SimpleDVConsumer implements Closeable {
  // TODO: are any of these params too "infringing" on codec?
  // we want codec to get necessary stuff from IW, but trading off against merge complexity.

  // nocommit should we pass SegmentWriteState...?
  public abstract NumericDocValuesConsumer addNumericField(FieldInfo field, long minValue, long maxValue) throws IOException;
  public abstract BinaryDocValuesConsumer addBinaryField(FieldInfo field, boolean fixedLength, int maxLength) throws IOException;
  // nocommit: figure out whats fair here.
  public abstract SortedDocValuesConsumer addSortedField(FieldInfo field, int valueCount, boolean fixedLength, int maxLength) throws IOException;
  
  public void merge(MergeState mergeState) throws IOException {
    for (FieldInfo field : mergeState.fieldInfos) {
      if (field.hasDocValues()) {
        mergeState.fieldInfo = field;
        DocValues.Type type = field.getDocValuesType();
        switch(type) {
          case VAR_INTS:
          case FIXED_INTS_8:
          case FIXED_INTS_16:
          case FIXED_INTS_32:
          case FIXED_INTS_64:
          case FLOAT_64:
          case FLOAT_32:
            mergeNumericField(mergeState);
            break;
          case BYTES_VAR_SORTED:
          case BYTES_FIXED_SORTED:
          case BYTES_VAR_DEREF:
          case BYTES_FIXED_DEREF:
            mergeSortedField(mergeState);
            break;
          case BYTES_VAR_STRAIGHT:
          case BYTES_FIXED_STRAIGHT:
            mergeBinaryField(mergeState);
            break;
          default:
            throw new AssertionError();
        }
      }
    }
  }

  // dead simple impl: codec can optimize
  protected void mergeNumericField(MergeState mergeState) throws IOException {
    // first compute min and max value of live ones to be merged.
    long minValue = Long.MAX_VALUE;
    long maxValue = Long.MIN_VALUE;
    for (AtomicReader reader : mergeState.readers) {
      final int maxDoc = reader.maxDoc();
      final Bits liveDocs = reader.getLiveDocs();
      //System.out.println("merge field=" + mergeState.fieldInfo.name);
      NumericDocValues docValues = reader.getNumericDocValues(mergeState.fieldInfo.name);
      if (docValues == null) {
        docValues = NumericDocValues.DEFAULT;
      }
      for (int i = 0; i < maxDoc; i++) {
        if (liveDocs == null || liveDocs.get(i)) {
          long val = docValues.get(i);
          minValue = Math.min(val, minValue);
          maxValue = Math.min(val, maxValue);
        }
        mergeState.checkAbort.work(300);
      }
    }
    // now we can merge
    NumericDocValuesConsumer field = addNumericField(mergeState.fieldInfo, minValue, maxValue);
    field.merge(mergeState);
  }
  
  // dead simple impl: codec can optimize
  protected void mergeBinaryField(MergeState mergeState) throws IOException {
    // first compute fixedLength and maxLength of live ones to be merged.
    boolean fixedLength = true;
    int maxLength = -1;
    BytesRef bytes = new BytesRef();
    for (AtomicReader reader : mergeState.readers) {
      final int maxDoc = reader.maxDoc();
      final Bits liveDocs = reader.getLiveDocs();
      BinaryDocValues docValues = reader.getBinaryDocValues(mergeState.fieldInfo.name);
      if (docValues == null) {
        docValues = BinaryDocValues.DEFAULT;
      }
      for (int i = 0; i < maxDoc; i++) {
        if (liveDocs == null || liveDocs.get(i)) {
          docValues.get(i, bytes);
          if (maxLength == -1) {
            maxLength = bytes.length;
          } else {
            fixedLength &= bytes.length == maxLength;
            maxLength = Math.max(bytes.length, maxLength);
          }
        }
        mergeState.checkAbort.work(300);
      }
    }
    // now we can merge
    assert maxLength >= 0; // could this happen (nothing to do?)
    BinaryDocValuesConsumer field = addBinaryField(mergeState.fieldInfo, fixedLength, maxLength);
    field.merge(mergeState);
  }

  protected void mergeSortedField(MergeState mergeState) throws IOException {
    SortedDocValuesConsumer.Merger merger = new SortedDocValuesConsumer.Merger();
    merger.merge(mergeState);
    SortedDocValuesConsumer consumer = addSortedField(mergeState.fieldInfo, merger.numMergedTerms, merger.fixedLength >= 0, merger.maxLength);
    consumer.merge(mergeState, merger);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1784.java