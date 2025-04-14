error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/701.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/701.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/701.java
text:
```scala
r@@eturn new TopGroups<BytesRef>(groupSort.getSort(), sortWithinGroup.getSort(), 0, 0, new GroupDocs[0], Float.NaN);

package org.apache.solr.search.grouping.distributed.command;

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

import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.SearchGroup;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.search.grouping.term.TermSecondPassGroupingCollector;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.grouping.Command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Defines all collectors for retrieving the second phase and how to handle the collector result.
 */
public class TopGroupsFieldCommand implements Command<TopGroups<BytesRef>> {

  public static class Builder {

    private SchemaField field;
    private Sort groupSort;
    private Sort sortWithinGroup;
    private Collection<SearchGroup<BytesRef>> firstPhaseGroups;
    private Integer maxDocPerGroup;
    private boolean needScores = false;
    private boolean needMaxScore = false;

    public Builder setField(SchemaField field) {
      this.field = field;
      return this;
    }

    public Builder setGroupSort(Sort groupSort) {
      this.groupSort = groupSort;
      return this;
    }

    public Builder setSortWithinGroup(Sort sortWithinGroup) {
      this.sortWithinGroup = sortWithinGroup;
      return this;
    }

    public Builder setFirstPhaseGroups(Collection<SearchGroup<BytesRef>> firstPhaseGroups) {
      this.firstPhaseGroups = firstPhaseGroups;
      return this;
    }

    public Builder setMaxDocPerGroup(int maxDocPerGroup) {
      this.maxDocPerGroup = maxDocPerGroup;
      return this;
    }

    public Builder setNeedScores(Boolean needScores) {
      this.needScores = needScores;
      return this;
    }

    public Builder setNeedMaxScore(Boolean needMaxScore) {
      this.needMaxScore = needMaxScore;
      return this;
    }

    public TopGroupsFieldCommand build() {
      if (field == null || groupSort == null ||  sortWithinGroup == null || firstPhaseGroups == null ||
          maxDocPerGroup == null) {
        throw new IllegalStateException("All required fields must be set");
      }

      return new TopGroupsFieldCommand(field, groupSort, sortWithinGroup, firstPhaseGroups, maxDocPerGroup, needScores, needMaxScore);
    }

  }

  private final SchemaField field;
  private final Sort groupSort;
  private final Sort sortWithinGroup;
  private final Collection<SearchGroup<BytesRef>> firstPhaseGroups;
  private final int maxDocPerGroup;
  private final boolean needScores;
  private final boolean needMaxScore;
  private TermSecondPassGroupingCollector secondPassCollector;

  private TopGroupsFieldCommand(SchemaField field,
                                Sort groupSort,
                                Sort sortWithinGroup,
                                Collection<SearchGroup<BytesRef>> firstPhaseGroups,
                                int maxDocPerGroup,
                                boolean needScores,
                                boolean needMaxScore) {
    this.field = field;
    this.groupSort = groupSort;
    this.sortWithinGroup = sortWithinGroup;
    this.firstPhaseGroups = firstPhaseGroups;
    this.maxDocPerGroup = maxDocPerGroup;
    this.needScores = needScores;
    this.needMaxScore = needMaxScore;
  }

  public List<Collector> create() throws IOException {
    if (firstPhaseGroups.isEmpty()) {
      return Collections.emptyList();
    }

    List<Collector> collectors = new ArrayList<Collector>();
    secondPassCollector = new TermSecondPassGroupingCollector(
          field.getName(), firstPhaseGroups, groupSort, sortWithinGroup, maxDocPerGroup, needScores, needMaxScore, true
    );
    collectors.add(secondPassCollector);
    return collectors;
  }

  @SuppressWarnings("unchecked")
  public TopGroups<BytesRef> result() {
    if (firstPhaseGroups.isEmpty()) {
      return new TopGroups<BytesRef>(groupSort.getSort(), sortWithinGroup.getSort(), 0, 0, new GroupDocs[0]);
    }

    return secondPassCollector.getTopGroups(0);
  }

  public String getKey() {
    return field.getName();
  }

  public Sort getGroupSort() {
    return groupSort;
  }

  public Sort getSortWithinGroup() {
    return sortWithinGroup;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/701.java