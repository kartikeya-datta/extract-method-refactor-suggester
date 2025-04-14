error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3280.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3280.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3280.java
text:
```scala
public b@@oolean requiresDocScores() {

package org.apache.lucene.facet.range;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.facet.params.FacetSearchParams;
import org.apache.lucene.facet.search.FacetRequest;
import org.apache.lucene.facet.search.FacetResult;
import org.apache.lucene.facet.search.FacetResultNode;
import org.apache.lucene.facet.search.FacetsAccumulator;
import org.apache.lucene.facet.search.FacetsAggregator;
import org.apache.lucene.facet.search.FacetsCollector.MatchingDocs;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.NumericDocValues;

/** Uses a {@link NumericDocValues} and accumulates
 *  counts for provided ranges.  This is dynamic (does not
 *  use the taxonomy index or anything from the index
 *  except the NumericDocValuesField). */

public class RangeAccumulator extends FacetsAccumulator {

  static class RangeSet {
    final Range[] ranges;
    final String field;

    public RangeSet(Range[] ranges, String field) {
      this.ranges = ranges;
      this.field = field;
    }
  }

  final List<RangeSet> requests = new ArrayList<RangeSet>();

  public RangeAccumulator(FacetSearchParams fsp, IndexReader reader) {
    super(fsp, reader, null, null);

    for(FacetRequest fr : fsp.facetRequests) {

      if (!(fr instanceof RangeFacetRequest)) {
        throw new IllegalArgumentException("only RangeFacetRequest is supported; got " + fsp.facetRequests.get(0).getClass());
      }

      if (fr.categoryPath.length != 1) {
        throw new IllegalArgumentException("only flat (dimension only) CategoryPath is allowed");
      }

      RangeFacetRequest<?> rfr = (RangeFacetRequest) fr;

      requests.add(new RangeSet(rfr.ranges, rfr.categoryPath.components[0]));
    }
  }

  @Override
  public FacetsAggregator getAggregator() {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<FacetResult> accumulate(List<MatchingDocs> matchingDocs) throws IOException {

    // TODO: test if this is faster (in the past it was
    // faster to do MachingDocs on the inside) ... see
    // patches on LUCENE-4965):
    List<FacetResult> results = new ArrayList<FacetResult>();
    for(int i=0;i<requests.size();i++) {
      RangeSet ranges = requests.get(i);

      int[] counts = new int[ranges.ranges.length];
      for(MatchingDocs hits : matchingDocs) {
        NumericDocValues ndv = hits.context.reader().getNumericDocValues(ranges.field);
        final int length = hits.bits.length();
        int doc = 0;
        while (doc < length && (doc = hits.bits.nextSetBit(doc)) != -1) {
          long v = ndv.get(doc);
          // TODO: use interval tree instead of linear search:
          for(int j=0;j<ranges.ranges.length;j++) {
            if (ranges.ranges[j].accept(v)) {
              counts[j]++;
            }
          }

          doc++;
        }
      }

      List<FacetResultNode> nodes = new ArrayList<FacetResultNode>(ranges.ranges.length);
      for(int j=0;j<ranges.ranges.length;j++) {
        nodes.add(new RangeFacetResultNode(ranges.field, ranges.ranges[j], counts[j]));
      }

      FacetResultNode rootNode = new FacetResultNode(-1, 0);
      rootNode.label = new CategoryPath(ranges.field);
      rootNode.subResults = nodes;

      results.add(new FacetResult(searchParams.facetRequests.get(i), rootNode, nodes.size()));
    }

    return results;
  }

  @Override
  protected boolean requiresDocScores() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3280.java