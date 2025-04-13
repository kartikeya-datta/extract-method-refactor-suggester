error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/631.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/631.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/631.java
text:
```scala
final B@@uilder.Arc<T> arc = node.arcs[arcUpto];

package org.apache.lucene.util.automaton.fst;

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

import java.io.IOException;

// Used to dedup states (lookup already-frozen states)
final class NodeHash<T> {

  private int[] table;
  private int count;
  private int mask;
  private final FST<T> fst;
  private final FST.Arc<T> scratchArc = new FST.Arc<T>();

  public NodeHash(FST<T> fst) {
    table = new int[16];
    mask = 15;
    this.fst = fst;
  }

  private boolean nodesEqual(Builder.UnCompiledNode<T> node, int address) throws IOException {
    fst.readFirstRealArc(address, scratchArc);
    if (scratchArc.bytesPerArc != 0 && node.numArcs != scratchArc.numArcs) {
      return false;
    }
    for(int arcUpto=0;arcUpto<node.numArcs;arcUpto++) {
      final Builder.Arc arc = node.arcs[arcUpto];
      if (arc.label != scratchArc.label ||
          !arc.output.equals(scratchArc.output) ||
          ((Builder.CompiledNode) arc.target).address != scratchArc.target ||
          !arc.nextFinalOutput.equals(scratchArc.nextFinalOutput) ||
          arc.isFinal != scratchArc.isFinal()) {
        return false;
      }

      if (scratchArc.isLast()) {
        if (arcUpto == node.numArcs-1) {
          return true;
        } else {
          return false;
        }
      }
      fst.readNextRealArc(scratchArc);
    }

    return false;
  }

  // hash code for an unfrozen node.  This must be identical
  // to the un-frozen case (below)!!
  private int hash(Builder.UnCompiledNode<T> node) {
    final int PRIME = 31;
    //System.out.println("hash unfrozen");
    int h = 0;
    // TODO: maybe if number of arcs is high we can safely subsample?
    for(int arcIdx=0;arcIdx<node.numArcs;arcIdx++) {
      final Builder.Arc<T> arc = node.arcs[arcIdx];
      //System.out.println("  label=" + arc.label + " target=" + ((Builder.CompiledNode) arc.target).address + " h=" + h + " output=" + fst.outputs.outputToString(arc.output) + " isFinal?=" + arc.isFinal);
      h = PRIME * h + arc.label;
      h = PRIME * h + ((Builder.CompiledNode) arc.target).address;
      h = PRIME * h + arc.output.hashCode();
      h = PRIME * h + arc.nextFinalOutput.hashCode();
      if (arc.isFinal) {
        h += 17;
      }
    }
    //System.out.println("  ret " + (h&Integer.MAX_VALUE));
    return h & Integer.MAX_VALUE;
  }

  // hash code for a frozen node
  private int hash(int node) throws IOException {
    final int PRIME = 31;
    //System.out.println("hash frozen");
    int h = 0;
    fst.readFirstRealArc(node, scratchArc);
    while(true) {
      //System.out.println("  label=" + scratchArc.label + " target=" + scratchArc.target + " h=" + h + " output=" + fst.outputs.outputToString(scratchArc.output) + " next?=" + scratchArc.flag(4) + " final?=" + scratchArc.isFinal());
      h = PRIME * h + scratchArc.label;
      h = PRIME * h + scratchArc.target;
      h = PRIME * h + scratchArc.output.hashCode();
      h = PRIME * h + scratchArc.nextFinalOutput.hashCode();
      if (scratchArc.isFinal()) {
        h += 17;
      }
      if (scratchArc.isLast()) {
        break;
      }
      fst.readNextRealArc(scratchArc);
    }
    //System.out.println("  ret " + (h&Integer.MAX_VALUE));
    return h & Integer.MAX_VALUE;
  }

  public int add(Builder.UnCompiledNode<T> node) throws IOException {
    // System.out.println("hash: add count=" + count + " vs " + table.length);
    final int h = hash(node);
    int pos = h & mask;
    int c = 0;
    while(true) {
      final int v = table[pos];
      if (v == 0) {
        // freeze & add
        final int address = fst.addNode(node);
        //System.out.println("  now freeze addr=" + address);
        assert hash(address) == h : "frozenHash=" + hash(address) + " vs h=" + h;
        count++;
        table[pos] = address;
        if (table.length < 2*count) {
          rehash();
        }
        return address;
      } else if (nodesEqual(node, v)) {
        // same node is already here
        return v;
      }

      // quadratic probe
      pos = (pos + (++c)) & mask;
    }
  }

  // called only by rehash
  private void addNew(int address) throws IOException {
    int pos = hash(address) & mask;
    int c = 0;
    while(true) {
      if (table[pos] == 0) {
        table[pos] = address;
        break;
      }

      // quadratic probe
      pos = (pos + (++c)) & mask;
    }
  }

  private void rehash() throws IOException {
    final int[] oldTable = table;
    table = new int[2*table.length];
    mask = table.length-1;
    for(int idx=0;idx<oldTable.length;idx++) {
      final int address = oldTable[idx];
      if (address != 0) {
        addNew(address);
      }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/631.java