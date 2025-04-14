error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2000.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2000.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2000.java
text:
```scala
T@@oken token = perThread.localToken.reinit(stringValue, 0, valueLength);

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
import java.io.Reader;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

/**
 * Holds state for inverting all occurrences of a single
 * field in the document.  This class doesn't do anything
 * itself; instead, it forwards the tokens produced by
 * analysis to its own consumer
 * (InvertedDocConsumerPerField).  It also interacts with an
 * endConsumer (InvertedDocEndConsumerPerField).
 */

final class DocInverterPerField extends DocFieldConsumerPerField {

  final private DocInverterPerThread perThread;
  final private FieldInfo fieldInfo;
  final InvertedDocConsumerPerField consumer;
  final InvertedDocEndConsumerPerField endConsumer;
  final DocumentsWriter.DocState docState;
  final FieldInvertState fieldState;

  public DocInverterPerField(DocInverterPerThread perThread, FieldInfo fieldInfo) {
    this.perThread = perThread;
    this.fieldInfo = fieldInfo;
    docState = perThread.docState;
    fieldState = perThread.fieldState;
    this.consumer = perThread.consumer.addField(this, fieldInfo);
    this.endConsumer = perThread.endConsumer.addField(this, fieldInfo);
  }

  void abort() {
    consumer.abort();
    endConsumer.abort();
  }

  public void processFields(final Fieldable[] fields,
                            final int count) throws IOException {

    fieldState.reset(docState.doc.getBoost());

    final int maxFieldLength = docState.maxFieldLength;

    final boolean doInvert = consumer.start(fields, count);

    for(int i=0;i<count;i++) {

      final Fieldable field = fields[i];

      // TODO FI: this should be "genericized" to querying
      // consumer if it wants to see this particular field
      // tokenized.
      if (field.isIndexed() && doInvert) {

        if (fieldState.length > 0)
          fieldState.position += docState.analyzer.getPositionIncrementGap(fieldInfo.name);

        if (!field.isTokenized()) {		  // un-tokenized field
          String stringValue = field.stringValue();
          final int valueLength = stringValue.length();
          Token token = perThread.localToken.reinit(stringValue, fieldState.offset, fieldState.offset + valueLength);
          boolean success = false;
          try {
            consumer.add(token);
            success = true;
          } finally {
            if (!success)
              docState.docWriter.setAborting();
          }
          fieldState.offset += valueLength;
          fieldState.length++;
          fieldState.position++;
        } else {                                  // tokenized field
          final TokenStream stream;
          final TokenStream streamValue = field.tokenStreamValue();

          if (streamValue != null) 
            stream = streamValue;
          else {
            // the field does not have a TokenStream,
            // so we have to obtain one from the analyzer
            final Reader reader;			  // find or make Reader
            final Reader readerValue = field.readerValue();

            if (readerValue != null)
              reader = readerValue;
            else {
              String stringValue = field.stringValue();
              if (stringValue == null)
                throw new IllegalArgumentException("field must have either TokenStream, String or Reader value");
              perThread.stringReader.init(stringValue);
              reader = perThread.stringReader;
            }
          
            // Tokenize field and add to postingTable
            stream = docState.analyzer.reusableTokenStream(fieldInfo.name, reader);
          }

          // reset the TokenStream to the first token
          stream.reset();

          try {
            int offsetEnd = fieldState.offset-1;
            final Token localToken = perThread.localToken;
            for(;;) {

              // If we hit an exception in stream.next below
              // (which is fairly common, eg if analyzer
              // chokes on a given document), then it's
              // non-aborting and (above) this one document
              // will be marked as deleted, but still
              // consume a docID
              Token token = stream.next(localToken);

              if (token == null) break;
              final int posIncr = token.getPositionIncrement();
              fieldState.position += posIncr - 1;
              if (posIncr == 0)
                fieldState.numOverlap++;

              boolean success = false;
              try {
                // If we hit an exception in here, we abort
                // all buffered documents since the last
                // flush, on the likelihood that the
                // internal state of the consumer is now
                // corrupt and should not be flushed to a
                // new segment:
                consumer.add(token);
                success = true;
              } finally {
                if (!success)
                  docState.docWriter.setAborting();
              }
              fieldState.position++;
              offsetEnd = fieldState.offset + token.endOffset();
              if (++fieldState.length >= maxFieldLength) {
                if (docState.infoStream != null)
                  docState.infoStream.println("maxFieldLength " +maxFieldLength+ " reached for field " + fieldInfo.name + ", ignoring following tokens");
                break;
              }
            }
            fieldState.offset = offsetEnd+1;
          } finally {
            stream.close();
          }
        }

        fieldState.boost *= field.getBoost();
      }
    }

    consumer.finish();
    endConsumer.finish();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2000.java