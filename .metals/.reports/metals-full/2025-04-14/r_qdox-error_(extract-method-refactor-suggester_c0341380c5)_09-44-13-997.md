error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3000.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3000.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3000.java
text:
```scala
s@@ += " " + numTermDeletes.get() + " deleted terms (unique count=" + terms.size() + ") terms=" + terms.keySet();

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.lucene.search.Query;
import org.apache.lucene.util.RamUsageEstimator;

/* Holds buffered deletes, by docID, term or query for a
 * single segment. This is used to hold buffered pending
 * deletes against the to-be-flushed segment.  Once the
 * deletes are pushed (on flush in DocumentsWriter), these
 * deletes are converted to a FrozenDeletes instance. */

// NOTE: we are sync'd by BufferedDeletes, ie, all access to
// instances of this class is via sync'd methods on
// BufferedDeletes

class BufferedDeletes {

  /* Rough logic: HashMap has an array[Entry] w/ varying
     load factor (say 2 * POINTER).  Entry is object w/ Term
     key, Integer val, int hash, Entry next
     (OBJ_HEADER + 3*POINTER + INT).  Term is object w/
     String field and String text (OBJ_HEADER + 2*POINTER).
     Term's field is String (OBJ_HEADER + 4*INT + POINTER +
     OBJ_HEADER + string.length*CHAR).
     Term's text is String (OBJ_HEADER + 4*INT + POINTER +
     OBJ_HEADER + string.length*CHAR).  Integer is
     OBJ_HEADER + INT. */
  final static int BYTES_PER_DEL_TERM = 9*RamUsageEstimator.NUM_BYTES_OBJECT_REF + 7*RamUsageEstimator.NUM_BYTES_OBJECT_HEADER + 10*RamUsageEstimator.NUM_BYTES_INT;

  /* Rough logic: del docIDs are List<Integer>.  Say list
     allocates ~2X size (2*POINTER).  Integer is OBJ_HEADER
     + int */
  final static int BYTES_PER_DEL_DOCID = 2*RamUsageEstimator.NUM_BYTES_OBJECT_REF + RamUsageEstimator.NUM_BYTES_OBJECT_HEADER + RamUsageEstimator.NUM_BYTES_INT;

  /* Rough logic: HashMap has an array[Entry] w/ varying
     load factor (say 2 * POINTER).  Entry is object w/
     Query key, Integer val, int hash, Entry next
     (OBJ_HEADER + 3*POINTER + INT).  Query we often
     undercount (say 24 bytes).  Integer is OBJ_HEADER + INT. */
  final static int BYTES_PER_DEL_QUERY = 5*RamUsageEstimator.NUM_BYTES_OBJECT_REF + 2*RamUsageEstimator.NUM_BYTES_OBJECT_HEADER + 2*RamUsageEstimator.NUM_BYTES_INT + 24;

  final AtomicInteger numTermDeletes = new AtomicInteger();
  final Map<Term,Integer> terms = new HashMap<Term,Integer>();
  final Map<Query,Integer> queries = new HashMap<Query,Integer>();
  final List<Integer> docIDs = new ArrayList<Integer>();

  public static final Integer MAX_INT = Integer.valueOf(Integer.MAX_VALUE);

  final AtomicLong bytesUsed;

  private final static boolean VERBOSE_DELETES = false;

  long gen;
  public BufferedDeletes() {
    this(new AtomicLong());
  }

  BufferedDeletes(AtomicLong bytesUsed) {
    assert bytesUsed != null;
    this.bytesUsed = bytesUsed;
  }

  @Override
  public String toString() {
    if (VERBOSE_DELETES) {
      return "gen=" + gen + " numTerms=" + numTermDeletes + ", terms=" + terms
        + ", queries=" + queries + ", docIDs=" + docIDs + ", bytesUsed="
        + bytesUsed;
    } else {
      String s = "gen=" + gen;
      if (numTermDeletes.get() != 0) {
        s += " " + numTermDeletes.get() + " deleted terms (unique count=" + terms.size() + ")";
      }
      if (queries.size() != 0) {
        s += " " + queries.size() + " deleted queries";
      }
      if (docIDs.size() != 0) {
        s += " " + docIDs.size() + " deleted docIDs";
      }
      if (bytesUsed.get() != 0) {
        s += " bytesUsed=" + bytesUsed.get();
      }

      return s;
    }
  }

  public void addQuery(Query query, int docIDUpto) {
    Integer current = queries.put(query, docIDUpto);
    // increment bytes used only if the query wasn't added so far.
    if (current == null) {
      bytesUsed.addAndGet(BYTES_PER_DEL_QUERY);
    }
  }

  public void addDocID(int docID) {
    docIDs.add(Integer.valueOf(docID));
    bytesUsed.addAndGet(BYTES_PER_DEL_DOCID);
  }

  public void addTerm(Term term, int docIDUpto) {
    Integer current = terms.get(term);
    if (current != null && docIDUpto < current) {
      // Only record the new number if it's greater than the
      // current one.  This is important because if multiple
      // threads are replacing the same doc at nearly the
      // same time, it's possible that one thread that got a
      // higher docID is scheduled before the other
      // threads.  If we blindly replace than we can
      // incorrectly get both docs indexed.
      return;
    }

    terms.put(term, Integer.valueOf(docIDUpto));
    numTermDeletes.incrementAndGet();
    if (current == null) {
      bytesUsed.addAndGet(BYTES_PER_DEL_TERM + term.bytes.length + (RamUsageEstimator.NUM_BYTES_CHAR * term.field().length()));
    }
  }
 
  void clear() {
    terms.clear();
    queries.clear();
    docIDs.clear();
    numTermDeletes.set(0);
    bytesUsed.set(0);
  }
  
  void clearDocIDs() {
    bytesUsed.addAndGet(-docIDs.size()*BYTES_PER_DEL_DOCID);
    docIDs.clear();
  }
  
  boolean any() {
    return terms.size() > 0 || docIDs.size() > 0 || queries.size() > 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3000.java