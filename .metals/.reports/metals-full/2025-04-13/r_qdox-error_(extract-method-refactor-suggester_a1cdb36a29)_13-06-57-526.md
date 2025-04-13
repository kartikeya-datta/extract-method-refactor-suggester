error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2126.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2126.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2126.java
text:
```scala
N@@amedList<Object> commands = new SimpleOrderedMap<Object>();

package org.apache.solr.search.grouping.endresulttransformer;

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
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.search.grouping.distributed.command.QueryCommandResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link EndResultTransformer} that keeps each grouped result separate in the final response.
 */
public class GroupedEndResultTransformer implements EndResultTransformer {

  private final SolrIndexSearcher searcher;

  public GroupedEndResultTransformer(SolrIndexSearcher searcher) {
    this.searcher = searcher;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void transform(Map<String, ?> result, ResponseBuilder rb, SolrDocumentSource solrDocumentSource) {
    NamedList<Object> commands = new NamedList<Object>();
    for (Map.Entry<String, ?> entry : result.entrySet()) {
      Object value = entry.getValue();
      if (TopGroups.class.isInstance(value)) {
        @SuppressWarnings("unchecked")
        TopGroups<BytesRef> topGroups = (TopGroups<BytesRef>) value;
        NamedList<Object> command = new SimpleOrderedMap<Object>();
        command.add("matches", rb.totalHitCount);
        Integer totalGroupCount = rb.mergedGroupCounts.get(entry.getKey());
        if (totalGroupCount != null) {
          command.add("ngroups", totalGroupCount);
        }

        List<NamedList> groups = new ArrayList<NamedList>();
        SchemaField groupField = searcher.getSchema().getField(entry.getKey());
        FieldType groupFieldType = groupField.getType();
        for (GroupDocs<BytesRef> group : topGroups.groups) {
          SimpleOrderedMap<Object> groupResult = new SimpleOrderedMap<Object>();
          if (group.groupValue != null) {
            groupResult.add(
                "groupValue", groupFieldType.toObject(groupField.createField(group.groupValue.utf8ToString(), 1.0f))
            );
          } else {
            groupResult.add("groupValue", null);
          }
          SolrDocumentList docList = new SolrDocumentList();
          docList.setNumFound(group.totalHits);
          if (!Float.isNaN(group.maxScore)) {
            docList.setMaxScore(group.maxScore);
          }
          docList.setStart(rb.getGroupingSpec().getGroupOffset());
          for (ScoreDoc scoreDoc : group.scoreDocs) {
            docList.add(solrDocumentSource.retrieve(scoreDoc));
          }
          groupResult.add("doclist", docList);
          groups.add(groupResult);
        }
        command.add("groups", groups);
        commands.add(entry.getKey(), command);
      } else if (QueryCommandResult.class.isInstance(value)) {
        QueryCommandResult queryCommandResult = (QueryCommandResult) value;
        NamedList<Object> command = new SimpleOrderedMap<Object>();
        command.add("matches", queryCommandResult.getMatches());
        SolrDocumentList docList = new SolrDocumentList();
        docList.setNumFound(queryCommandResult.getTopDocs().totalHits);
        if (!Float.isNaN(queryCommandResult.getTopDocs().getMaxScore())) {
          docList.setMaxScore(queryCommandResult.getTopDocs().getMaxScore());
        }
        docList.setStart(rb.getGroupingSpec().getGroupOffset());
        for (ScoreDoc scoreDoc :queryCommandResult.getTopDocs().scoreDocs){
          docList.add(solrDocumentSource.retrieve(scoreDoc));
        }
        command.add("doclist", docList);
        commands.add(entry.getKey(), command);
      }
    }
    rb.rsp.add("grouped", commands);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2126.java