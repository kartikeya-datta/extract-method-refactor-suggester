error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2586.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2586.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2586.java
text:
```scala
s@@uper(size);

package org.apache.lucene.search.spans;

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

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexReader.AtomicReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.PriorityQueue;
import org.apache.lucene.util.ToStringUtils;
import org.apache.lucene.search.Query;

/** Matches the union of its clauses.*/
public class SpanOrQuery extends SpanQuery implements Cloneable {
  private List<SpanQuery> clauses;
  private String field;

  /** Construct a SpanOrQuery merging the provided clauses. */
  public SpanOrQuery(SpanQuery... clauses) {

    // copy clauses array into an ArrayList
    this.clauses = new ArrayList<SpanQuery>(clauses.length);
    for (int i = 0; i < clauses.length; i++) {
      addClause(clauses[i]);
    }
  }

  /** Adds a clause to this query */
  public final void addClause(SpanQuery clause) {
    if (field == null) {
      field = clause.getField();
    } else if (!clause.getField().equals(field)) {
      throw new IllegalArgumentException("Clauses must have same field.");
    }
    this.clauses.add(clause);
  }
  
  /** Return the clauses whose spans are matched. */
  public SpanQuery[] getClauses() {
    return clauses.toArray(new SpanQuery[clauses.size()]);
  }

  @Override
  public String getField() { return field; }

  @Override
  public void extractTerms(Set<Term> terms) {
    for(final SpanQuery clause: clauses) {
      clause.extractTerms(terms);
    }
  }
  
  @Override
  public Object clone() {
    int sz = clauses.size();
    SpanQuery[] newClauses = new SpanQuery[sz];

    for (int i = 0; i < sz; i++) {
      newClauses[i] = (SpanQuery) clauses.get(i).clone();
    }
    SpanOrQuery soq = new SpanOrQuery(newClauses);
    soq.setBoost(getBoost());
    return soq;
  }

  @Override
  public Query rewrite(IndexReader reader) throws IOException {
    SpanOrQuery clone = null;
    for (int i = 0 ; i < clauses.size(); i++) {
      SpanQuery c = clauses.get(i);
      SpanQuery query = (SpanQuery) c.rewrite(reader);
      if (query != c) {                     // clause rewrote: must clone
        if (clone == null)
          clone = (SpanOrQuery) this.clone();
        clone.clauses.set(i,query);
      }
    }
    if (clone != null) {
      return clone;                        // some clauses rewrote
    } else {
      return this;                         // no clauses rewrote
    }
  }

  @Override
  public String toString(String field) {
    StringBuilder buffer = new StringBuilder();
    buffer.append("spanOr([");
    Iterator<SpanQuery> i = clauses.iterator();
    while (i.hasNext()) {
      SpanQuery clause = i.next();
      buffer.append(clause.toString(field));
      if (i.hasNext()) {
        buffer.append(", ");
      }
    }
    buffer.append("])");
    buffer.append(ToStringUtils.boost(getBoost()));
    return buffer.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final SpanOrQuery that = (SpanOrQuery) o;

    if (!clauses.equals(that.clauses)) return false;
    if (!clauses.isEmpty() && !field.equals(that.field)) return false;

    return getBoost() == that.getBoost();
  }

  @Override
  public int hashCode() {
    int h = clauses.hashCode();
    h ^= (h << 10) | (h >>> 23);
    h ^= Float.floatToRawIntBits(getBoost());
    return h;
  }


  private class SpanQueue extends PriorityQueue<Spans> {
    public SpanQueue(int size) {
      initialize(size);
    }

    @Override
    protected final boolean lessThan(Spans spans1, Spans spans2) {
      if (spans1.doc() == spans2.doc()) {
        if (spans1.start() == spans2.start()) {
          return spans1.end() < spans2.end();
        } else {
          return spans1.start() < spans2.start();
        }
      } else {
        return spans1.doc() < spans2.doc();
      }
    }
  }

  @Override
  public Spans getSpans(final AtomicReaderContext context) throws IOException {
    if (clauses.size() == 1)                      // optimize 1-clause case
      return (clauses.get(0)).getSpans(context);

    return new Spans() {
        private SpanQueue queue = null;

        private boolean initSpanQueue(int target) throws IOException {
          queue = new SpanQueue(clauses.size());
          Iterator<SpanQuery> i = clauses.iterator();
          while (i.hasNext()) {
            Spans spans = i.next().getSpans(context);
            if (   ((target == -1) && spans.next())
 ((target != -1) && spans.skipTo(target))) {
              queue.add(spans);
            }
          }
          return queue.size() != 0;
        }

        @Override
        public boolean next() throws IOException {
          if (queue == null) {
            return initSpanQueue(-1);
          }

          if (queue.size() == 0) { // all done
            return false;
          }

          if (top().next()) { // move to next
            queue.updateTop();
            return true;
          }

          queue.pop();  // exhausted a clause
          return queue.size() != 0;
        }

        private Spans top() { return queue.top(); }

        @Override
        public boolean skipTo(int target) throws IOException {
          if (queue == null) {
            return initSpanQueue(target);
          }
  
          boolean skipCalled = false;
          while (queue.size() != 0 && top().doc() < target) {
            if (top().skipTo(target)) {
              queue.updateTop();
            } else {
              queue.pop();
            }
            skipCalled = true;
          }
  
          if (skipCalled) {
            return queue.size() != 0;
          }
          return next();
        }

        @Override
        public int doc() { return top().doc(); }
        @Override
        public int start() { return top().start(); }
        @Override
        public int end() { return top().end(); }

      @Override
      public Collection<byte[]> getPayload() throws IOException {
        ArrayList<byte[]> result = null;
        Spans theTop = top();
        if (theTop != null && theTop.isPayloadAvailable()) {
          result = new ArrayList<byte[]>(theTop.getPayload());
        }
        return result;
      }

      @Override
      public boolean isPayloadAvailable() {
        Spans top = top();
        return top != null && top.isPayloadAvailable();
      }

      @Override
      public String toString() {
          return "spans("+SpanOrQuery.this+")@"+
            ((queue == null)?"START"
             :(queue.size()>0?(doc()+":"+start()+"-"+end()):"END"));
        }

      };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2586.java