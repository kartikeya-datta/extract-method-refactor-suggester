error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2525.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2525.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2525.java
text:
```scala
<@@li> {@link MultiPhraseQuery}

package org.apache.lucene.search;

/**
 * Copyright 2002-2004 The Apache Software Foundation
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

import java.io.IOException;

import java.util.HashSet;
import java.util.Iterator;

import org.apache.lucene.index.IndexReader;

/** The abstract base class for queries.
    <p>Instantiable subclasses are:
    <ul>
    <li> {@link TermQuery}
    <li> {@link MultiTermQuery}
    <li> {@link BooleanQuery}
    <li> {@link WildcardQuery}
    <li> {@link PhraseQuery}
    <li> {@link PrefixQuery}
    <li> {@link PhrasePrefixQuery}
    <li> {@link FuzzyQuery}
    <li> {@link RangeQuery}
    <li> {@link org.apache.lucene.search.spans.SpanQuery}
    </ul>
    <p>A parser for queries is contained in:
    <ul>
    <li>{@link org.apache.lucene.queryParser.QueryParser QueryParser}
    </ul>
*/
public abstract class Query implements java.io.Serializable, Cloneable {
  private float boost = 1.0f;                     // query boost factor

  /** Sets the boost for this query clause to <code>b</code>.  Documents
   * matching this clause will (in addition to the normal weightings) have
   * their score multiplied by <code>b</code>.
   */
  public void setBoost(float b) { boost = b; }

  /** Gets the boost for this clause.  Documents matching
   * this clause will (in addition to the normal weightings) have their score
   * multiplied by <code>b</code>.   The boost is 1.0 by default.
   */
  public float getBoost() { return boost; }

  /** Prints a query to a string, with <code>field</code> as the default field
   * for terms.  <p>The representation used is one that is supposed to be readable
   * by {@link org.apache.lucene.queryParser.QueryParser QueryParser}. However,
   * there are the following limitations:
   * <ul>
   *  <li>If the query was created by the parser, the printed
   *  representation may not be exactly what was parsed. For example,
   *  characters that need to be escaped will be represented without
   *  the required backslash.</li>
   * <li>Some of the more complicated queries (e.g. span queries)
   *  don't have a representation that can be parsed by QueryParser.</li>
   * </ul>
   */
  public abstract String toString(String field);

  /** Prints a query to a string. */
  public String toString() {
    return toString("");
  }

  /** Expert: Constructs an appropriate Weight implementation for this query.
   *
   * <p>Only implemented by primitive queries, which re-write to themselves.
   */
  protected Weight createWeight(Searcher searcher) {
    throw new UnsupportedOperationException();
  }

  /** Expert: Constructs an initializes a Weight for a top-level query. */
  public Weight weight(Searcher searcher)
    throws IOException {
    Query query = searcher.rewrite(this);
    Weight weight = query.createWeight(searcher);
    float sum = weight.sumOfSquaredWeights();
    float norm = getSimilarity(searcher).queryNorm(sum);
    weight.normalize(norm);
    return weight;
  }

  /** Expert: called to re-write queries into primitive queries. */
  public Query rewrite(IndexReader reader) throws IOException {
    return this;
  }

  /** Expert: called when re-writing queries under MultiSearcher.
   *
   * <p>Only implemented by derived queries, with no
   * {@link #createWeight(Searcher)} implementatation.
   */
  public Query combine(Query[] queries) {
    throw new UnsupportedOperationException();
  }


  /** Expert: merges the clauses of a set of BooleanQuery's into a single
   * BooleanQuery.
   *
   *<p>A utility for use by {@link #combine(Query[])} implementations.
   */
  public static Query mergeBooleanQueries(Query[] queries) {
    HashSet allClauses = new HashSet();
    for (int i = 0; i < queries.length; i++) {
      BooleanClause[] clauses = ((BooleanQuery)queries[i]).getClauses();
      for (int j = 0; j < clauses.length; j++) {
        allClauses.add(clauses[j]);
      }
    }

    BooleanQuery result = new BooleanQuery();
    Iterator i = allClauses.iterator();
    while (i.hasNext()) {
      result.add((BooleanClause)i.next());
    }
    return result;
  }

  /** Expert: Returns the Similarity implementation to be used for this query.
   * Subclasses may override this method to specify their own Similarity
   * implementation, perhaps one that delegates through that of the Searcher.
   * By default the Searcher's Similarity implementation is returned.*/
  public Similarity getSimilarity(Searcher searcher) {
    return searcher.getSimilarity();
  }

  /** Returns a clone of this query. */
  public Object clone() {
    try {
      return (Query)super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException("Clone not supported: " + e.getMessage());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2525.java