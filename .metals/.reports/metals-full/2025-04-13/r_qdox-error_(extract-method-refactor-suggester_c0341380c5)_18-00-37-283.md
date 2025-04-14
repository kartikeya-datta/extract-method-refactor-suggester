error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/389.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/389.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/389.java
text:
```scala
public M@@ap<String, Pair<Integer, Collection<SearchGroup<BytesRef>>>> transformToNative(NamedList<NamedList> shardResponse, Sort groupSort, Sort sortWithinGroup, String shard) {

package org.apache.solr.search.grouping.distributed.shardresultserializer;

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

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.grouping.SearchGroup;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.UnicodeUtil;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.search.grouping.Command;
import org.apache.solr.search.grouping.distributed.command.Pair;
import org.apache.solr.search.grouping.distributed.command.SearchGroupsFieldCommand;

import java.io.IOException;
import java.util.*;

/**
 * Implementation for transforming {@link SearchGroup} into a {@link NamedList} structure and visa versa.
 */
public class SearchGroupsResultTransformer implements ShardResultTransformer<List<Command>, Map<String, Pair<Integer, Collection<SearchGroup<BytesRef>>>>> {

  private final SolrIndexSearcher searcher;

  public SearchGroupsResultTransformer(SolrIndexSearcher searcher) {
    this.searcher = searcher;
  }

  /**
   * {@inheritDoc}
   */
  public NamedList transform(List<Command> data) throws IOException {
    NamedList<NamedList> result = new NamedList<NamedList>();
    for (Command command : data) {
      final NamedList<Object> commandResult = new NamedList<Object>();
      if (SearchGroupsFieldCommand.class.isInstance(command)) {
        SearchGroupsFieldCommand fieldCommand = (SearchGroupsFieldCommand) command;
        Pair<Integer, Collection<SearchGroup<BytesRef>>> pair = fieldCommand.result();
        Integer groupedCount = pair.getA();
        Collection<SearchGroup<BytesRef>> searchGroups = pair.getB();
        if (searchGroups != null) {
          commandResult.add("topGroups", serializeSearchGroup(searchGroups, fieldCommand.getGroupSort()));
        }
        if (groupedCount != null) {
          commandResult.add("groupCount", groupedCount);
        }
      } else {
        continue;
      }

      result.add(command.getKey(), commandResult);
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, Pair<Integer, Collection<SearchGroup<BytesRef>>>> transformToNative(NamedList<NamedList> shardResponse, Sort groupSort, Sort sortWithinGroup, String shard) throws IOException {
    Map<String, Pair<Integer, Collection<SearchGroup<BytesRef>>>> result = new HashMap<String, Pair<Integer, Collection<SearchGroup<BytesRef>>>>();
    for (Map.Entry<String, NamedList> command : shardResponse) {
      List<SearchGroup<BytesRef>> searchGroups = new ArrayList<SearchGroup<BytesRef>>();
      NamedList topGroupsAndGroupCount = command.getValue();
      @SuppressWarnings("unchecked")
      NamedList<List<Comparable>> rawSearchGroups = (NamedList<List<Comparable>>) topGroupsAndGroupCount.get("topGroups");
      if (rawSearchGroups != null) {
        for (Map.Entry<String, List<Comparable>> rawSearchGroup : rawSearchGroups){
          SearchGroup<BytesRef> searchGroup = new SearchGroup<BytesRef>();
          searchGroup.groupValue = rawSearchGroup.getKey() != null ? new BytesRef(rawSearchGroup.getKey()) : null;
          searchGroup.sortValues = rawSearchGroup.getValue().toArray(new Comparable[rawSearchGroup.getValue().size()]);
          searchGroups.add(searchGroup);
        }
      }

      Integer groupCount = (Integer) topGroupsAndGroupCount.get("groupCount");
      result.put(command.getKey(), new Pair<Integer, Collection<SearchGroup<BytesRef>>>(groupCount, searchGroups));
    }
    return result;
  }

  private NamedList serializeSearchGroup(Collection<SearchGroup<BytesRef>> data, Sort groupSort) {
    NamedList<Comparable[]> result = new NamedList<Comparable[]>();
    CharsRef spare = new CharsRef();

    for (SearchGroup<BytesRef> searchGroup : data) {
      Comparable[] convertedSortValues = new Comparable[searchGroup.sortValues.length];
      for (int i = 0; i < searchGroup.sortValues.length; i++) {
        Comparable sortValue = (Comparable) searchGroup.sortValues[i];
        SchemaField field = groupSort.getSort()[i].getField() != null ? searcher.getSchema().getFieldOrNull(groupSort.getSort()[i].getField()) : null;
        if (field != null) {
          FieldType fieldType = field.getType();
          if (sortValue instanceof BytesRef) {
            UnicodeUtil.UTF8toUTF16((BytesRef)sortValue, spare);
            String indexedValue = spare.toString();
            sortValue = (Comparable) fieldType.toObject(field.createField(fieldType.indexedToReadable(indexedValue), 0.0f));
          } else if (sortValue instanceof String) {
            sortValue = (Comparable) fieldType.toObject(field.createField(fieldType.indexedToReadable((String) sortValue), 0.0f));
          }
        }
        convertedSortValues[i] = sortValue;
      }
      String groupValue = searchGroup.groupValue != null ? searchGroup.groupValue.utf8ToString() : null;
      result.add(groupValue, convertedSortValues);
    }

    return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/389.java