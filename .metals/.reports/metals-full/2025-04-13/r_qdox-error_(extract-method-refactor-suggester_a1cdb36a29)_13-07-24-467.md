error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/487.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/487.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/487.java
text:
```scala
public F@@ieldsEnumWithSlice(FieldsEnum fields, ReaderSlice slice, int index) {

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

import org.apache.lucene.util.PriorityQueue;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Exposes flex API, merged from flex API of sub-segments.
 * This does a merge sort, by field name, of the
 * sub-readers.
 *
 * @lucene.experimental
 */

public final  class MultiFieldsEnum extends FieldsEnum {
  private final FieldMergeQueue queue;

  // Holds sub-readers containing field we are currently
  // on, popped from queue.
  private final FieldsEnumWithSlice[] top;

  private int numTop;

  private final Fields fields;

  private String currentField;

  /** The subs array must be newly initialized FieldsEnum
   *  (ie, {@link FieldsEnum#next} has not been called. */
  public MultiFieldsEnum(MultiFields fields, FieldsEnum[] subs, ReaderSlice[] subSlices) throws IOException {
    this.fields = fields;
    queue = new FieldMergeQueue(subs.length);
    top = new FieldsEnumWithSlice[subs.length];
    List<FieldsEnumWithSlice> enumWithSlices = new ArrayList<FieldsEnumWithSlice>();

    // Init q
    for(int i=0;i<subs.length;i++) {
      assert subs[i] != null;
      final String field = subs[i].next();
      if (field != null) {
        // this FieldsEnum has at least one field
        final FieldsEnumWithSlice sub = new FieldsEnumWithSlice(subs[i], subSlices[i], i);
        enumWithSlices.add(sub);
        sub.current = field;
        queue.add(sub);
      }
    }
  }

  @Override
  public String next() throws IOException {

    // restore queue
    for(int i=0;i<numTop;i++) {
      top[i].current = top[i].fields.next();
      if (top[i].current != null) {
        queue.add(top[i]);
      } else {
        // no more fields in this sub-reader
      }
    }

    numTop = 0;

    // gather equal top fields
    if (queue.size() > 0) {
      while(true) {
        top[numTop++] = queue.pop();
        if (queue.size() == 0 || !(queue.top()).current.equals(top[0].current)) {
          break;
        }
      }
      currentField = top[0].current;
    } else {
      currentField = null;
    }

    return currentField;
  }

  @Override
  public Terms terms() throws IOException {
    // Ask our parent MultiFields:
    return fields.terms(currentField);
  }

  public final static class FieldsEnumWithSlice {
    public static final FieldsEnumWithSlice[] EMPTY_ARRAY = new FieldsEnumWithSlice[0];
    final FieldsEnum fields;
    final ReaderSlice slice;
    final int index;
    String current;

    public FieldsEnumWithSlice(FieldsEnum fields, ReaderSlice slice, int index) throws IOException {
      this.slice = slice;
      this.index = index;
      assert slice.length >= 0: "length=" + slice.length;
      this.fields = fields;
    }
  }

  private final static class FieldMergeQueue extends PriorityQueue<FieldsEnumWithSlice> {
    FieldMergeQueue(int size) {
      super(size);
    }

    @Override
    protected final boolean lessThan(FieldsEnumWithSlice fieldsA, FieldsEnumWithSlice fieldsB) {
      // No need to break ties by field name: TermsEnum handles that
      return fieldsA.current.compareTo(fieldsB.current) < 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/487.java