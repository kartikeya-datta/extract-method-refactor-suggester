error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1034.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1034.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1034.java
text:
```scala
final W@@eight w = searcher.createNormalizedWeight(query);

package org.apache.lucene.search;

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
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.ReaderUtil;
import org.apache.lucene.util._TestUtil;

public class TestTopDocsMerge extends LuceneTestCase {

  private static class ShardSearcher {
    private final IndexSearcher subSearcher;

    public ShardSearcher(IndexReader subReader) {
      this.subSearcher = new IndexSearcher(subReader);
    }

    public void search(Weight weight, Collector collector) throws IOException {
      subSearcher.search(weight, null, collector);
    }

    public TopDocs search(Weight weight, int topN) throws IOException {
      return subSearcher.search(weight, null, topN);
    }

    @Override
    public String toString() {
      return "ShardSearcher(" + subSearcher + ")";
    }
  }

  public void testSort() throws Exception {

    IndexReader reader = null;
    Directory dir = null;

    final int numDocs = atLeast(1000);
    //final int numDocs = atLeast(50);

    final String[] tokens = new String[] {"a", "b", "c", "d", "e"};

    if (VERBOSE) {
      System.out.println("TEST: make index");
    }

    {
      dir = newDirectory();
      final RandomIndexWriter w = new RandomIndexWriter(random, dir);
      // w.setDoRandomOptimize(false);

      // w.w.getConfig().setMaxBufferedDocs(atLeast(100));

      final String[] content = new String[atLeast(20)];

      for(int contentIDX=0;contentIDX<content.length;contentIDX++) {
        final StringBuilder sb = new StringBuilder();
        final int numTokens = _TestUtil.nextInt(random, 1, 10);
        for(int tokenIDX=0;tokenIDX<numTokens;tokenIDX++) {
          sb.append(tokens[random.nextInt(tokens.length)]).append(' ');
        }
        content[contentIDX] = sb.toString();
      }

      for(int docIDX=0;docIDX<numDocs;docIDX++) {
        final Document doc = new Document();
        doc.add(newField("string", _TestUtil.randomRealisticUnicodeString(random), Field.Index.NOT_ANALYZED));
        doc.add(newField("text", content[random.nextInt(content.length)], Field.Index.ANALYZED));
        doc.add(new NumericField("float").setFloatValue(random.nextFloat()));
        final int intValue;
        if (random.nextInt(100) == 17) {
          intValue = Integer.MIN_VALUE;
        } else if (random.nextInt(100) == 17) {
          intValue = Integer.MAX_VALUE;
        } else {
          intValue = random.nextInt();
        }
        doc.add(new NumericField("int").setIntValue(intValue));
        if (VERBOSE) {
          System.out.println("  doc=" + doc);
        }
        w.addDocument(doc);
      }

      reader = w.getReader();
      w.close();
    }

    // NOTE: sometimes reader has just one segment, which is
    // important to test
    final IndexSearcher searcher = newSearcher(reader);
    IndexReader[] subReaders = searcher.getIndexReader().getSequentialSubReaders();
    if (subReaders == null) {
      subReaders = new IndexReader[] {searcher.getIndexReader()};
    }
    final ShardSearcher[] subSearchers = new ShardSearcher[subReaders.length];

    for(int searcherIDX=0;searcherIDX<subSearchers.length;searcherIDX++) { 
      subSearchers[searcherIDX] = new ShardSearcher(subReaders[searcherIDX]);
    }

    final List<SortField> sortFields = new ArrayList<SortField>();
    sortFields.add(new SortField("string", SortField.STRING, true));
    sortFields.add(new SortField("string", SortField.STRING, false));
    sortFields.add(new SortField("int", SortField.INT, true));
    sortFields.add(new SortField("int", SortField.INT, false));
    sortFields.add(new SortField("float", SortField.FLOAT, true));
    sortFields.add(new SortField("float", SortField.FLOAT, false));
    sortFields.add(new SortField(null, SortField.SCORE, true));
    sortFields.add(new SortField(null, SortField.SCORE, false));
    sortFields.add(new SortField(null, SortField.DOC, true));
    sortFields.add(new SortField(null, SortField.DOC, false));

    final int[] docStarts = new int[subSearchers.length];
    int docBase = 0;
    for(int subIDX=0;subIDX<docStarts.length;subIDX++) {
      docStarts[subIDX] = docBase;
      docBase += subReaders[subIDX].maxDoc();
      if (VERBOSE) {
        System.out.println("docStarts[" + subIDX + "]=" + docStarts[subIDX]);
      }
    }

    for(int iter=0;iter<1000*RANDOM_MULTIPLIER;iter++) {

      // TODO: custom FieldComp...
      final Query query = new TermQuery(new Term("text", tokens[random.nextInt(tokens.length)]));

      final Sort sort;
      if (random.nextInt(10) == 4) {
        // Sort by score
        sort = null;
      } else {
        final SortField[] randomSortFields = new SortField[_TestUtil.nextInt(random, 1, 3)];
        for(int sortIDX=0;sortIDX<randomSortFields.length;sortIDX++) {
          randomSortFields[sortIDX] = sortFields.get(random.nextInt(sortFields.size()));
        }
        sort = new Sort(randomSortFields);
      }

      final int numHits = _TestUtil.nextInt(random, 1, numDocs+5);
      //final int numHits = 5;
      
      if (VERBOSE) {
        System.out.println("TEST: search query=" + query + " sort=" + sort + " numHits=" + numHits);
      }

      // First search on whole index:
      final TopDocs topHits;
      if (sort == null) {
        topHits = searcher.search(query, numHits);
      } else {
        final TopFieldCollector c = TopFieldCollector.create(sort, numHits, true, true, true, random.nextBoolean());
        searcher.search(query, c);
        topHits = c.topDocs(0, numHits);
      }

      if (VERBOSE) {
        System.out.println("  top search: " + topHits.totalHits + " totalHits; hits=" + (topHits.scoreDocs == null ? "null" : topHits.scoreDocs.length));
        if (topHits.scoreDocs != null) {
          for(int hitIDX=0;hitIDX<topHits.scoreDocs.length;hitIDX++) {
            final ScoreDoc sd = topHits.scoreDocs[hitIDX];
            System.out.println("    doc=" + sd.doc + " score=" + sd.score);
          }
        }
      }

      // ... then all shards:
      final Weight w = query.weight(searcher);

      final TopDocs[] shardHits = new TopDocs[subSearchers.length];
      for(int shardIDX=0;shardIDX<subSearchers.length;shardIDX++) {
        final TopDocs subHits;
        final ShardSearcher subSearcher = subSearchers[shardIDX];
        if (sort == null) {
          subHits = subSearcher.search(w, numHits);
        } else {
          final TopFieldCollector c = TopFieldCollector.create(sort, numHits, true, true, true, random.nextBoolean());
          subSearcher.search(w, c);
          subHits = c.topDocs(0, numHits);
        }
        rebaseDocIDs(docStarts[shardIDX], subHits);

        shardHits[shardIDX] = subHits;
        if (VERBOSE) {
          System.out.println("  shard=" + shardIDX + " " + subHits.totalHits + " totalHits hits=" + (subHits.scoreDocs == null ? "null" : subHits.scoreDocs.length));
          if (subHits.scoreDocs != null) {
            for(ScoreDoc sd : subHits.scoreDocs) {
              System.out.println("    doc=" + sd.doc + " score=" + sd.score);
            }
          }
        }
      }

      // Merge:
      final TopDocs mergedHits = TopDocs.merge(sort, numHits, shardHits);

      if (VERBOSE) {
        System.out.println("  mergedHits: " + mergedHits.totalHits + " totalHits; hits=" + (mergedHits.scoreDocs == null ? "null" : mergedHits.scoreDocs.length));
        if (mergedHits.scoreDocs != null) {
          for(int hitIDX=0;hitIDX<mergedHits.scoreDocs.length;hitIDX++) {
            final ScoreDoc sd = mergedHits.scoreDocs[hitIDX];
            System.out.println("    doc=" + sd.doc + " score=" + sd.score);
          }
        }
      }
      if (mergedHits.scoreDocs != null) {
        // Make sure the returned shards are correct:
        for(int hitIDX=0;hitIDX<mergedHits.scoreDocs.length;hitIDX++) {
          final ScoreDoc sd = mergedHits.scoreDocs[hitIDX];
          assertEquals("doc=" + sd.doc + " wrong shard",
                       ReaderUtil.subIndex(sd.doc, docStarts),
                       sd.shardIndex);
        }
      }

      _TestUtil.assertEquals(topHits, mergedHits);
    }
    searcher.close();
    reader.close();
    dir.close();
  }

  private void rebaseDocIDs(int docBase, TopDocs hits) {
    List<Integer> docFieldLocs = new ArrayList<Integer>();
    if (hits instanceof TopFieldDocs) {
      TopFieldDocs fieldHits = (TopFieldDocs) hits;
      for(int fieldIDX=0;fieldIDX<fieldHits.fields.length;fieldIDX++) {
        if (fieldHits.fields[fieldIDX].getType() == SortField.DOC) {
          docFieldLocs.add(fieldIDX);
        }
      }
    }

    for(int hitIDX=0;hitIDX<hits.scoreDocs.length;hitIDX++) {
      final ScoreDoc sd = hits.scoreDocs[hitIDX];
      sd.doc += docBase;
      if (sd instanceof FieldDoc) {
        final FieldDoc fd = (FieldDoc) sd;
        if (fd.fields != null) {
          for(int idx : docFieldLocs) {
            fd.fields[idx] = Integer.valueOf(((Integer) fd.fields[idx]).intValue() + docBase);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1034.java