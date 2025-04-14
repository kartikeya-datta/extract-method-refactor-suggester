error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/390.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/390.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/390.java
text:
```scala
public B@@its bits() {

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

package org.apache.solr.search.function;

import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.BitsFilteredDocIdSet;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.util.Bits;
import org.apache.solr.search.SolrFilter;

import java.io.IOException;
import java.util.Map;


/**
 * RangeFilter over a ValueSource.
 */
public class ValueSourceRangeFilter extends SolrFilter {
  private final ValueSource valueSource;
  private final String lowerVal;
  private final String upperVal;
  private final boolean includeLower;
  private final boolean includeUpper;

  public ValueSourceRangeFilter(ValueSource valueSource,
                                String lowerVal,
                                String upperVal,
                                boolean includeLower,
                                boolean includeUpper) {
    this.valueSource = valueSource;
    this.lowerVal = lowerVal;
    this.upperVal = upperVal;
    this.includeLower = lowerVal != null && includeLower;
    this.includeUpper = upperVal != null && includeUpper;
  }

  public ValueSource getValueSource() {
    return valueSource;
  }

  public String getLowerVal() {
    return lowerVal;
  }

  public String getUpperVal() {
    return upperVal;
  }

  public boolean isIncludeLower() {
    return includeLower;
  }

  public boolean isIncludeUpper() {
    return includeUpper;
  }


  @Override
  public DocIdSet getDocIdSet(final Map context, final AtomicReaderContext readerContext, Bits acceptDocs) throws IOException {
     return BitsFilteredDocIdSet.wrap(new DocIdSet() {
       @Override
       public DocIdSetIterator iterator() throws IOException {
         return valueSource.getValues(context, readerContext).getRangeScorer(readerContext.reader(), lowerVal, upperVal, includeLower, includeUpper);
       }
       @Override
       public Bits bits() throws IOException {
         return null;  // don't use random access
       }
     }, acceptDocs);
  }

  @Override
  public void createWeight(Map context, IndexSearcher searcher) throws IOException {
    valueSource.createWeight(context, searcher);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("frange(");
    sb.append(valueSource);
    sb.append("):");
    sb.append(includeLower ? '[' : '{');
    sb.append(lowerVal == null ? "*" : lowerVal);
    sb.append(" TO ");
    sb.append(upperVal == null ? "*" : upperVal);
    sb.append(includeUpper ? ']' : '}');
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ValueSourceRangeFilter)) return false;
    ValueSourceRangeFilter other = (ValueSourceRangeFilter)o;

    if (!this.valueSource.equals(other.valueSource)
 this.includeLower != other.includeLower
 this.includeUpper != other.includeUpper
    ) { return false; }
    if (this.lowerVal != null ? !this.lowerVal.equals(other.lowerVal) : other.lowerVal != null) return false;
    if (this.upperVal != null ? !this.upperVal.equals(other.upperVal) : other.upperVal != null) return false;
    return true;
  }

  @Override
  public int hashCode() {
    int h = valueSource.hashCode();
    h += lowerVal != null ? lowerVal.hashCode() : 0x572353db;
    h = (h << 16) | (h >>> 16);  // rotate to distinguish lower from upper
    h += (upperVal != null ? (upperVal.hashCode()) : 0xe16fe9e7);
    h += (includeLower ? 0xdaa47978 : 0)
    + (includeUpper ? 0x9e634b57 : 0);
    return h;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/390.java