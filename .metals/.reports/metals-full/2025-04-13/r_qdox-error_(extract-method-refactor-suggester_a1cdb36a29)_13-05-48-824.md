error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/464.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/464.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/464.java
text:
```scala
public T@@ermSpans(DocsAndPositionsEnum postings, Term term) {

package org.apache.lucene.search.spans;
/**
 * Copyright 2005 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.apache.lucene.index.Term;
import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.util.Collections;
import java.util.Collection;

/**
 * Expert:
 * Public for extension only
 */
public class TermSpans extends Spans {
  protected final DocsAndPositionsEnum postings;
  protected final Term term;
  protected int doc;
  protected int freq;
  protected int count;
  protected int position;

  public TermSpans(DocsAndPositionsEnum postings, Term term) throws IOException {
    this.postings = postings;
    this.term = term;
    doc = -1;
  }

  // only for EmptyTermSpans (below)
  TermSpans() {
    term = null;
    postings = null;
  }

  @Override
  public boolean next() throws IOException {
    if (count == freq) {
      if (postings == null) {
        return false;
      }
      doc = postings.nextDoc();
      if (doc == DocIdSetIterator.NO_MORE_DOCS) {
        return false;
      }
      freq = postings.freq();
      count = 0;
    }
    position = postings.nextPosition();
    count++;
    return true;
  }

  @Override
  public boolean skipTo(int target) throws IOException {
    doc = postings.advance(target);
    if (doc == DocIdSetIterator.NO_MORE_DOCS) {
      return false;
    }

    freq = postings.freq();
    count = 0;
    position = postings.nextPosition();
    count++;

    return true;
  }

  @Override
  public int doc() {
    return doc;
  }

  @Override
  public int start() {
    return position;
  }

  @Override
  public int end() {
    return position + 1;
  }

  // TODO: Remove warning after API has been finalized
  @Override
  public Collection<byte[]> getPayload() throws IOException {
    final BytesRef payload = postings.getPayload();
    final byte[] bytes;
    if (payload != null) {
      bytes = new byte[payload.length];
      System.arraycopy(payload.bytes, payload.offset, bytes, 0, payload.length);
    } else {
      bytes = null;
    }
    return Collections.singletonList(bytes);
  }

  // TODO: Remove warning after API has been finalized
  @Override
  public boolean isPayloadAvailable() {
    return postings.hasPayload();
  }

  @Override
  public String toString() {
    return "spans(" + term.toString() + ")@" +
            (doc == -1 ? "START" : (doc == Integer.MAX_VALUE) ? "END" : doc + "-" + position);
  }

  public DocsAndPositionsEnum getPostings() {
    return postings;
  }

  private static final class EmptyTermSpans extends TermSpans {

    @Override
    public boolean next() {
      return false;
    }

    @Override
    public boolean skipTo(int target) {
      return false;
    }

    @Override
    public int doc() {
      return DocIdSetIterator.NO_MORE_DOCS;
    }
    
    @Override
    public int start() {
      return -1;
    }

    @Override
    public int end() {
      return -1;
    }

    @Override
    public Collection<byte[]> getPayload() {
      return null;
    }

    @Override
    public boolean isPayloadAvailable() {
      return false;
    }
  }

  public static final TermSpans EMPTY_TERM_SPANS = new EmptyTermSpans();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/464.java