error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/492.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/492.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/492.java
text:
```scala
S@@eekingTermSetTermsEnum(TermsEnum tenum, BytesRefHash terms) {

package org.apache.lucene.search.join;

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

import org.apache.lucene.index.FilteredTermsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefHash;

import java.io.IOException;
import java.util.Comparator;

/**
 * A query that has an array of terms from a specific field. This query will match documents have one or more terms in
 * the specified field that match with the terms specified in the array.
 *
 * @lucene.experimental
 */
class TermsQuery extends MultiTermQuery {

  private final BytesRefHash terms;

  /**
   * @param field The field that should contain terms that are specified in the previous parameter
   * @param terms The terms that matching documents should have. The terms must be sorted by natural order.
   */
  TermsQuery(String field, BytesRefHash terms) {
    super(field);
    this.terms = terms;
  }

  protected TermsEnum getTermsEnum(Terms terms, AttributeSource atts) throws IOException {
    if (this.terms.size() == 0) {
      return TermsEnum.EMPTY;
    }

    return new SeekingTermSetTermsEnum(terms.iterator(null), this.terms);
  }

  public String toString(String string) {
    return "TermsQuery{" +
        "field=" + field +
        '}';
  }

  static class SeekingTermSetTermsEnum extends FilteredTermsEnum {

    private final BytesRefHash terms;
    private final int[] ords;
    private final int lastElement;

    private final BytesRef lastTerm;
    private final BytesRef spare = new BytesRef();
    private final Comparator<BytesRef> comparator;

    private BytesRef seekTerm;
    private int upto = 0;

    SeekingTermSetTermsEnum(TermsEnum tenum, BytesRefHash terms) throws IOException {
      super(tenum);
      this.terms = terms;

      lastElement = terms.size() - 1;
      ords = terms.sort(comparator = tenum.getComparator());
      lastTerm = terms.get(ords[lastElement], new BytesRef());
      seekTerm = terms.get(ords[upto], spare);
    }

    @Override
    protected BytesRef nextSeekTerm(BytesRef currentTerm) throws IOException {
      BytesRef temp = seekTerm;
      seekTerm = null;
      return temp;
    }

    protected AcceptStatus accept(BytesRef term) throws IOException {
      if (comparator.compare(term, lastTerm) > 0) {
        return AcceptStatus.END;
      }

      BytesRef currentTerm = terms.get(ords[upto], spare);
      if (comparator.compare(term, currentTerm) == 0) {
        if (upto == lastElement) {
          return AcceptStatus.YES;
        } else {
          seekTerm = terms.get(ords[++upto], spare);
          return AcceptStatus.YES_AND_SEEK;
        }
      } else {
        if (upto == lastElement) {
          return AcceptStatus.NO;
        } else { // Our current term doesn't match the the given term.
          int cmp;
          do { // We maybe are behind the given term by more than one step. Keep incrementing till we're the same or higher.
            if (upto == lastElement) {
              return AcceptStatus.NO;
            }
            // typically the terms dict is a superset of query's terms so it's unusual that we have to skip many of
            // our terms so we don't do a binary search here
            seekTerm = terms.get(ords[++upto], spare);
          } while ((cmp = comparator.compare(seekTerm, term)) < 0);
          if (cmp == 0) {
            if (upto == lastElement) {
              return AcceptStatus.YES;
            }
            seekTerm = terms.get(ords[++upto], spare);
            return AcceptStatus.YES_AND_SEEK;
          } else {
            return AcceptStatus.NO_AND_SEEK;
          }
        }
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/492.java