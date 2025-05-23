error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2090.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2090.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2090.java
text:
```scala
a@@ssertTrue(reader.leaves().size() > 1);

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

package org.apache.solr.search;

import java.io.IOException;
import java.util.*;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.*;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.OpenBitSet;
import org.apache.lucene.util._TestUtil;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.request.SolrQueryRequest;
import org.junit.BeforeClass;

public class TestSort extends SolrTestCaseJ4 {
  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig.xml","schema-minimal.xml");
  }

  Random r;

  int ndocs = 77;
  int iter = 50;
  int qiter = 1000;
  int commitCount = ndocs/5 + 1;
  int maxval = ndocs*2;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    r = random();
  }

  static class MyDoc {
    int doc;
    String val;
    String val2;

    @Override
    public String toString() {
      return "{id=" +doc + " val1="+val + " val2="+val2 + "}";
    }
  }

  public void testRandomFieldNameSorts() throws Exception {
    SolrQueryRequest req = lrf.makeRequest("q", "*:*");

    final int iters = atLeast(5000);

    // infinite loop abort when trying to generate a non-blank sort "name"
    final int nonBlankAttempts = 37;

    for (int i = 0; i < iters; i++) {
      final StringBuilder input = new StringBuilder();
      final String[] names = new String[_TestUtil.nextInt(r,1,10)];
      final boolean[] reverse = new boolean[names.length];
      for (int j = 0; j < names.length; j++) {
        names[j] = null;
        for (int k = 0; k < nonBlankAttempts && null == names[j]; k++) {
          names[j] = _TestUtil.randomRealisticUnicodeString(r, 1, 100);

          // munge anything that might make this a function
          names[j] = names[j].replaceFirst("\\{","\\{\\{");
          names[j] = names[j].replaceFirst("\\(","\\(\\(");
          names[j] = names[j].replaceFirst("(\\\"|\\')","$1$1z");
          names[j] = names[j].replaceFirst("(\\d)","$1x");

          // eliminate pesky problem chars
          names[j] = names[j].replaceAll("\\p{Cntrl}|\\p{javaWhitespace}","");
          
          if (0 == names[j].length()) {
            names[j] = null;
          }
        }
        // with luck this bad, never go to vegas
        // alternatively: if (null == names[j]) names[j] = "never_go_to_vegas";
        assertNotNull("Unable to generate a (non-blank) names["+j+"] after "
                      + nonBlankAttempts + " attempts", names[j]);

        reverse[j] = r.nextBoolean();

        input.append(r.nextBoolean() ? " " : "");
        input.append(names[j]);
        input.append(" ");
        input.append(reverse[j] ? "desc," : "asc,");
      }
      input.deleteCharAt(input.length()-1);
      SortField[] sorts = null;
      try {
        sorts = QueryParsing.parseSort(input.toString(), req).getSort();
      } catch (RuntimeException e) {
        throw new RuntimeException("Failed to parse sort: " + input, e);
      }
      assertEquals("parsed sorts had unexpected size", 
                   names.length, sorts.length);
      for (int j = 0; j < names.length; j++) {
        assertEquals("sorts["+j+"] had unexpected reverse: " + input,
                     reverse[j], sorts[j].getReverse());

        final Type type = sorts[j].getType();

        if (Type.SCORE.equals(type)) {
          assertEquals("sorts["+j+"] is (unexpectedly) type score : " + input,
                       "score", names[j]);
        } else if (Type.DOC.equals(type)) {
          assertEquals("sorts["+j+"] is (unexpectedly) type doc : " + input,
                       "_docid_", names[j]);
        } else if (Type.CUSTOM.equals(type) || Type.REWRITEABLE.equals(type)) {

          fail("sorts["+j+"] resulted in a '" + type.toString()
               + "', either sort parsing code is broken, or func/query " 
               + "semantics have gotten broader and munging in this test "
               + "needs improved: " + input);

        } else {
          assertEquals("sorts["+j+"] ("+type.toString()+
                       ") had unexpected field in: " + input,
                       names[j], sorts[j].getField());
        }
      }
    }
  }



  public void testSort() throws Exception {
    Directory dir = new RAMDirectory();
    Field f = new StringField("f", "0", Field.Store.NO);
    Field f2 = new StringField("f2", "0", Field.Store.NO);

    for (int iterCnt = 0; iterCnt<iter; iterCnt++) {
      IndexWriter iw = new IndexWriter(
          dir,
          new IndexWriterConfig(TEST_VERSION_CURRENT, new SimpleAnalyzer(TEST_VERSION_CURRENT)).
              setOpenMode(IndexWriterConfig.OpenMode.CREATE)
      );
      final MyDoc[] mydocs = new MyDoc[ndocs];

      int v1EmptyPercent = 50;
      int v2EmptyPercent = 50;

      int commitCountdown = commitCount;
      for (int i=0; i< ndocs; i++) {
        MyDoc mydoc = new MyDoc();
        mydoc.doc = i;
        mydocs[i] = mydoc;

        Document document = new Document();
        if (r.nextInt(100) < v1EmptyPercent) {
          mydoc.val = Integer.toString(r.nextInt(maxval));
          f.setStringValue(mydoc.val);
          document.add(f);
        }
        if (r.nextInt(100) < v2EmptyPercent) {
          mydoc.val2 = Integer.toString(r.nextInt(maxval));
          f2.setStringValue(mydoc.val2);
          document.add(f2);
        }


        iw.addDocument(document);
        if (--commitCountdown <= 0) {
          commitCountdown = commitCount;
          iw.commit();
        }
      }
      iw.close();


      DirectoryReader reader = DirectoryReader.open(dir);
      IndexSearcher searcher = new IndexSearcher(reader);
      // System.out.println("segments="+searcher.getIndexReader().getSequentialSubReaders().length);
      assertTrue(reader.getSequentialSubReaders().size() > 1);

      for (int i=0; i<qiter; i++) {
        Filter filt = new Filter() {
          @Override
          public DocIdSet getDocIdSet(AtomicReaderContext context, Bits acceptDocs) {
            return BitsFilteredDocIdSet.wrap(randSet(context.reader().maxDoc()), acceptDocs);
          }
        };

        int top = r.nextInt((ndocs>>3)+1)+1;
        final boolean luceneSort = r.nextBoolean();
        final boolean sortMissingLast = !luceneSort && r.nextBoolean();
        final boolean sortMissingFirst = !luceneSort && !sortMissingLast;
        final boolean reverse = r.nextBoolean();
        List<SortField> sfields = new ArrayList<SortField>();

        final boolean secondary = r.nextBoolean();
        final boolean luceneSort2 = r.nextBoolean();
        final boolean sortMissingLast2 = !luceneSort2 && r.nextBoolean();
        final boolean sortMissingFirst2 = !luceneSort2 && !sortMissingLast2;
        final boolean reverse2 = r.nextBoolean();

        if (r.nextBoolean()) sfields.add( new SortField(null, SortField.Type.SCORE));
        // hit both use-cases of sort-missing-last
        sfields.add( Sorting.getStringSortField("f", reverse, sortMissingLast, sortMissingFirst) );
        if (secondary) {
          sfields.add( Sorting.getStringSortField("f2", reverse2, sortMissingLast2, sortMissingFirst2) );
        }
        if (r.nextBoolean()) sfields.add( new SortField(null, SortField.Type.SCORE));

        Sort sort = new Sort(sfields.toArray(new SortField[sfields.size()]));

        final String nullRep = luceneSort || sortMissingFirst && !reverse || sortMissingLast && reverse ? "" : "zzz";
        final String nullRep2 = luceneSort2 || sortMissingFirst2 && !reverse2 || sortMissingLast2 && reverse2 ? "" : "zzz";

        boolean trackScores = r.nextBoolean();
        boolean trackMaxScores = r.nextBoolean();
        boolean scoreInOrder = r.nextBoolean();
        final TopFieldCollector topCollector = TopFieldCollector.create(sort, top, true, trackScores, trackMaxScores, scoreInOrder);

        final List<MyDoc> collectedDocs = new ArrayList<MyDoc>();
        // delegate and collect docs ourselves
        Collector myCollector = new Collector() {
          int docBase;

          @Override
          public void setScorer(Scorer scorer) throws IOException {
            topCollector.setScorer(scorer);
          }

          @Override
          public void collect(int doc) throws IOException {
            topCollector.collect(doc);
            collectedDocs.add(mydocs[doc + docBase]);
          }

          @Override
          public void setNextReader(AtomicReaderContext context) throws IOException {
            topCollector.setNextReader(context);
            docBase = context.docBase;
          }

          @Override
          public boolean acceptsDocsOutOfOrder() {
            return topCollector.acceptsDocsOutOfOrder();
          }
        };

        searcher.search(new MatchAllDocsQuery(), filt, myCollector);

        Collections.sort(collectedDocs, new Comparator<MyDoc>() {
          public int compare(MyDoc o1, MyDoc o2) {
            String v1 = o1.val==null ? nullRep : o1.val;
            String v2 = o2.val==null ? nullRep : o2.val;
            int cmp = v1.compareTo(v2);
            if (reverse) cmp = -cmp;
            if (cmp != 0) return cmp;

            if (secondary) {
               v1 = o1.val2==null ? nullRep2 : o1.val2;
               v2 = o2.val2==null ? nullRep2 : o2.val2;
               cmp = v1.compareTo(v2);
               if (reverse2) cmp = -cmp;
            }

            cmp = cmp==0 ? o1.doc-o2.doc : cmp;
            return cmp;
          }
        });


        TopDocs topDocs = topCollector.topDocs();
        ScoreDoc[] sdocs = topDocs.scoreDocs;
        for (int j=0; j<sdocs.length; j++) {
          int id = sdocs[j].doc;
          if (id != collectedDocs.get(j).doc) {
            log.error("Error at pos " + j
            + "\n\tsortMissingFirst=" + sortMissingFirst + " sortMissingLast=" + sortMissingLast + " reverse=" + reverse
            + "\n\tEXPECTED=" + collectedDocs 
            );
          }
          assertEquals(id, collectedDocs.get(j).doc);
        }
      }
      reader.close();
    }
    dir.close();

  }

  public DocIdSet randSet(int sz) {
    OpenBitSet obs = new OpenBitSet(sz);
    int n = r.nextInt(sz);
    for (int i=0; i<n; i++) {
      obs.fastSet(r.nextInt(sz));
    }
    return obs;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2090.java