error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/479.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/479.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/479.java
text:
```scala
public l@@ong size() {

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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.lucene.util.BytesRef;

import org.apache.lucene.util.automaton.CompiledAutomaton;

/**
 * Exposes flex API, merged from flex API of
 * sub-segments.
 *
 * @lucene.experimental
 */

public final class MultiTerms extends Terms {
  private final Terms[] subs;
  private final ReaderSlice[] subSlices;
  private final Comparator<BytesRef> termComp;

  public MultiTerms(Terms[] subs, ReaderSlice[] subSlices) throws IOException {
    this.subs = subs;
    this.subSlices = subSlices;
    
    Comparator<BytesRef> _termComp = null;
    for(int i=0;i<subs.length;i++) {
      if (_termComp == null) {
        _termComp = subs[i].getComparator();
      } else {
        // We cannot merge sub-readers that have
        // different TermComps
        final Comparator<BytesRef> subTermComp = subs[i].getComparator();
        if (subTermComp != null && !subTermComp.equals(_termComp)) {
          throw new IllegalStateException("sub-readers have different BytesRef.Comparators; cannot merge");
        }
      }
    }

    termComp = _termComp;
  }

  @Override
  public TermsEnum intersect(CompiledAutomaton compiled, BytesRef startTerm) throws IOException {
    final List<MultiTermsEnum.TermsEnumIndex> termsEnums = new ArrayList<MultiTermsEnum.TermsEnumIndex>();
    for(int i=0;i<subs.length;i++) {
      final TermsEnum termsEnum = subs[i].intersect(compiled, startTerm);
      if (termsEnum != null) {
        termsEnums.add(new MultiTermsEnum.TermsEnumIndex(termsEnum, i));
      }
    }

    if (termsEnums.size() > 0) {
      return new MultiTermsEnum(subSlices).reset(termsEnums.toArray(MultiTermsEnum.TermsEnumIndex.EMPTY_ARRAY));
    } else {
      return TermsEnum.EMPTY;
    }
  }

  @Override
  public TermsEnum iterator(TermsEnum reuse) throws IOException {

    final List<MultiTermsEnum.TermsEnumIndex> termsEnums = new ArrayList<MultiTermsEnum.TermsEnumIndex>();
    for(int i=0;i<subs.length;i++) {
      final TermsEnum termsEnum = subs[i].iterator(null);
      if (termsEnum != null) {
        termsEnums.add(new MultiTermsEnum.TermsEnumIndex(termsEnum, i));
      }
    }

    if (termsEnums.size() > 0) {
      return new MultiTermsEnum(subSlices).reset(termsEnums.toArray(MultiTermsEnum.TermsEnumIndex.EMPTY_ARRAY));
    } else {
      return TermsEnum.EMPTY;
    }
  }

  @Override
  public long size() throws IOException {
    return -1;
  }

  @Override
  public long getSumTotalTermFreq() throws IOException {
    long sum = 0;
    for(Terms terms : subs) {
      final long v = terms.getSumTotalTermFreq();
      if (v == -1) {
        return -1;
      }
      sum += v;
    }
    return sum;
  }
  
  @Override
  public long getSumDocFreq() throws IOException {
    long sum = 0;
    for(Terms terms : subs) {
      final long v = terms.getSumDocFreq();
      if (v == -1) {
        return -1;
      }
      sum += v;
    }
    return sum;
  }
  
  @Override
  public int getDocCount() throws IOException {
    int sum = 0;
    for(Terms terms : subs) {
      final int v = terms.getDocCount();
      if (v == -1) {
        return -1;
      }
      sum += v;
    }
    return sum;
  }

  @Override
  public Comparator<BytesRef> getComparator() {
    return termComp;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/479.java