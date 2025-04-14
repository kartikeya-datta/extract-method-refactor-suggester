error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3347.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3347.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3347.java
text:
```scala
public v@@oid abort() {

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

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/** This class implements {@link InvertedDocConsumer}, which
 *  is passed each token produced by the analyzer on each
 *  field.  It stores these tokens in a hash table, and
 *  allocates separate byte streams per token.  Consumers of
 *  this class, eg {@link FreqProxTermsWriter} and {@link
 *  TermVectorsTermsWriter}, write their own byte streams
 *  under each term.
 */
final class TermsHash extends InvertedDocConsumer {

  final TermsHashConsumer consumer;
  final TermsHash nextTermsHash;
  final DocumentsWriter docWriter;

  boolean trackAllocations;

  public TermsHash(final DocumentsWriter docWriter, boolean trackAllocations, final TermsHashConsumer consumer, final TermsHash nextTermsHash) {
    this.docWriter = docWriter;
    this.consumer = consumer;
    this.nextTermsHash = nextTermsHash;
    this.trackAllocations = trackAllocations;
  }

  @Override
  InvertedDocConsumerPerThread addThread(DocInverterPerThread docInverterPerThread) {
    return new TermsHashPerThread(docInverterPerThread, this, nextTermsHash, null);
  }

  TermsHashPerThread addThread(DocInverterPerThread docInverterPerThread, TermsHashPerThread primaryPerThread) {
    return new TermsHashPerThread(docInverterPerThread, this, nextTermsHash, primaryPerThread);
  }

  @Override
  void setFieldInfos(FieldInfos fieldInfos) {
    this.fieldInfos = fieldInfos;
    consumer.setFieldInfos(fieldInfos);
  }

  @Override
  synchronized public void abort() {
    consumer.abort();
    if (nextTermsHash != null)
      nextTermsHash.abort();
  }

  @Override
  synchronized void closeDocStore(SegmentWriteState state) throws IOException {
    consumer.closeDocStore(state);
    if (nextTermsHash != null)
      nextTermsHash.closeDocStore(state);
  }

  @Override
  synchronized void flush(Map<InvertedDocConsumerPerThread,Collection<InvertedDocConsumerPerField>> threadsAndFields, final SegmentWriteState state) throws IOException {
    Map<TermsHashConsumerPerThread,Collection<TermsHashConsumerPerField>> childThreadsAndFields = new HashMap<TermsHashConsumerPerThread,Collection<TermsHashConsumerPerField>>();
    Map<InvertedDocConsumerPerThread,Collection<InvertedDocConsumerPerField>> nextThreadsAndFields;

    if (nextTermsHash != null)
      nextThreadsAndFields = new HashMap<InvertedDocConsumerPerThread,Collection<InvertedDocConsumerPerField>>();
    else
      nextThreadsAndFields = null;

    for (final Map.Entry<InvertedDocConsumerPerThread,Collection<InvertedDocConsumerPerField>> entry : threadsAndFields.entrySet()) {

      TermsHashPerThread perThread = (TermsHashPerThread) entry.getKey();

      Collection<InvertedDocConsumerPerField> fields = entry.getValue();

      Iterator<InvertedDocConsumerPerField> fieldsIt = fields.iterator();
      Collection<TermsHashConsumerPerField> childFields = new HashSet<TermsHashConsumerPerField>();
      Collection<InvertedDocConsumerPerField> nextChildFields;

      if (nextTermsHash != null)
        nextChildFields = new HashSet<InvertedDocConsumerPerField>();
      else
        nextChildFields = null;

      while(fieldsIt.hasNext()) {
        TermsHashPerField perField = (TermsHashPerField) fieldsIt.next();
        childFields.add(perField.consumer);
        if (nextTermsHash != null)
          nextChildFields.add(perField.nextPerField);
      }

      childThreadsAndFields.put(perThread.consumer, childFields);
      if (nextTermsHash != null)
        nextThreadsAndFields.put(perThread.nextPerThread, nextChildFields);
    }
    
    consumer.flush(childThreadsAndFields, state);

    if (nextTermsHash != null)
      nextTermsHash.flush(nextThreadsAndFields, state);
  }

  @Override
  synchronized public boolean freeRAM() {
    return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3347.java