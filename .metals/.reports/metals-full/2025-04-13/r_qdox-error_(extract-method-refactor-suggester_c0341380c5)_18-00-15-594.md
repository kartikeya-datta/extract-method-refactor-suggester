error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/702.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/702.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/702.java
text:
```scala
r@@b.mergedTopGroups.put(groupField, TopGroups.merge(topGroups.toArray(topGroupsArr), groupSort, sortWithinGroup, groupOffsetDefault, docsPerGroupDefault, TopGroups.ScoreMergeMode.None));

package org.apache.solr.search.grouping.distributed.responseprocessor;

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

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.handler.component.ShardDoc;
import org.apache.solr.handler.component.ShardRequest;
import org.apache.solr.handler.component.ShardResponse;
import org.apache.solr.search.Grouping;
import org.apache.solr.search.grouping.distributed.ShardResponseProcessor;
import org.apache.solr.search.grouping.distributed.command.QueryCommandResult;
import org.apache.solr.search.grouping.distributed.shardresultserializer.TopGroupsResultTransformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete implementation for merging {@link TopGroups} instances from shard responses.
 */
public class TopGroupsShardResponseProcessor implements ShardResponseProcessor {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void process(ResponseBuilder rb, ShardRequest shardRequest) {
    Sort groupSort = rb.getGroupingSpec().getGroupSort();
    String[] fields = rb.getGroupingSpec().getFields();
    String[] queries = rb.getGroupingSpec().getQueries();
    Sort sortWithinGroup = rb.getGroupingSpec().getSortWithinGroup();

    // If group.format=simple group.offset doesn't make sense
    int groupOffsetDefault;
    if (rb.getGroupingSpec().getResponseFormat() == Grouping.Format.simple || rb.getGroupingSpec().isMain()) {
      groupOffsetDefault = 0;
    } else {
      groupOffsetDefault = rb.getGroupingSpec().getGroupOffset();
    }
    int docsPerGroupDefault = rb.getGroupingSpec().getGroupLimit();

    Map<String, List<TopGroups<BytesRef>>> commandTopGroups = new HashMap<String, List<TopGroups<BytesRef>>>();
    for (String field : fields) {
      commandTopGroups.put(field, new ArrayList<TopGroups<BytesRef>>());
    }

    Map<String, List<QueryCommandResult>> commandTopDocs = new HashMap<String, List<QueryCommandResult>>();
    for (String query : queries) {
      commandTopDocs.put(query, new ArrayList<QueryCommandResult>());
    }

    TopGroupsResultTransformer serializer = new TopGroupsResultTransformer(rb);
    for (ShardResponse srsp : shardRequest.responses) {
      NamedList<NamedList> secondPhaseResult = (NamedList<NamedList>) srsp.getSolrResponse().getResponse().get("secondPhase");
      Map<String, ?> result = serializer.transformToNative(secondPhaseResult, groupSort, sortWithinGroup, srsp.getShard());
      for (String field : commandTopGroups.keySet()) {
        TopGroups<BytesRef> topGroups = (TopGroups<BytesRef>) result.get(field);
        if (topGroups == null) {
          continue;
        }
        commandTopGroups.get(field).add(topGroups);
      }
      for (String query : queries) {
        commandTopDocs.get(query).add((QueryCommandResult) result.get(query));
      }
    }
    try {
      for (String groupField : commandTopGroups.keySet()) {
        List<TopGroups<BytesRef>> topGroups = commandTopGroups.get(groupField);
        if (topGroups.isEmpty()) {
          continue;
        }

        TopGroups<BytesRef>[] topGroupsArr = new TopGroups[topGroups.size()];
        rb.mergedTopGroups.put(groupField, TopGroups.merge(topGroups.toArray(topGroupsArr), groupSort, sortWithinGroup, groupOffsetDefault, docsPerGroupDefault));
      }

      for (String query : commandTopDocs.keySet()) {
        List<QueryCommandResult> queryCommandResults = commandTopDocs.get(query);
        List<TopDocs> topDocs = new ArrayList<TopDocs>(queryCommandResults.size());
        int mergedMatches = 0;
        for (QueryCommandResult queryCommandResult : queryCommandResults) {
          topDocs.add(queryCommandResult.getTopDocs());
          mergedMatches += queryCommandResult.getMatches();
        }

        int topN = rb.getGroupingSpec().getOffset() + rb.getGroupingSpec().getLimit();
        TopDocs mergedTopDocs = TopDocs.merge(sortWithinGroup, topN, topDocs.toArray(new TopDocs[topDocs.size()]));
        rb.mergedQueryCommandResults.put(query, new QueryCommandResult(mergedTopDocs, mergedMatches));
      }

      Map<Object, ShardDoc> resultIds = new HashMap<Object, ShardDoc>();
      int i = 0;
      for (TopGroups<BytesRef> topGroups : rb.mergedTopGroups.values()) {
        for (GroupDocs<BytesRef> group : topGroups.groups) {
          for (ScoreDoc scoreDoc : group.scoreDocs) {
            ShardDoc solrDoc = (ShardDoc) scoreDoc;
            solrDoc.positionInResponse = i++;
            resultIds.put(solrDoc.id, solrDoc);
          }
        }
      }
      for (QueryCommandResult queryCommandResult : rb.mergedQueryCommandResults.values()) {
        for (ScoreDoc scoreDoc : queryCommandResult.getTopDocs().scoreDocs) {
          ShardDoc solrDoc = (ShardDoc) scoreDoc;
          solrDoc.positionInResponse = i++;
          resultIds.put(solrDoc.id, solrDoc);
        }
      }

      rb.resultIds = resultIds;
    } catch (IOException e) {
      throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/702.java