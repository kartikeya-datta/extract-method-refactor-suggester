error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2962.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2962.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2962.java
text:
```scala
s@@earcher = new IndexSearcher(mDirectory, true);

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

import org.apache.lucene.util.LuceneTestCase;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/*******************************************************************************
 * Tests the span query bug in Lucene. It demonstrates that SpanTermQuerys don't
 * work correctly in a BooleanQuery.
 *
 */
public class TestSpansAdvanced extends LuceneTestCase {

    // location to the index
    protected Directory mDirectory;;

    protected IndexSearcher searcher;

    // field names in the index
    private final static String FIELD_ID = "ID";
    protected final static String FIELD_TEXT = "TEXT";

    /**
     * Initializes the tests by adding 4 identical documents to the index.
     */
    protected void setUp() throws Exception {
        super.setUp();
        super.setUp();

        // create test index
        mDirectory = new RAMDirectory();
        final IndexWriter writer = new IndexWriter(mDirectory, new StandardAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);
        addDocument(writer, "1", "I think it should work.");
        addDocument(writer, "2", "I think it should work.");
        addDocument(writer, "3", "I think it should work.");
        addDocument(writer, "4", "I think it should work.");
        writer.close();
        searcher = new IndexSearcher(mDirectory);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        searcher.close();
        mDirectory.close();
        mDirectory = null;
    }

    /**
     * Adds the document to the index.
     *
     * @param writer the Lucene index writer
     * @param id the unique id of the document
     * @param text the text of the document
     * @throws IOException
     */
    protected void addDocument(final IndexWriter writer, final String id, final String text) throws IOException {

        final Document document = new Document();
        document.add(new Field(FIELD_ID, id, Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new Field(FIELD_TEXT, text, Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(document);
    }

    /**
     * Tests two span queries.
     *
     * @throws IOException
     */
    public void testBooleanQueryWithSpanQueries() throws IOException {

        doTestBooleanQueryWithSpanQueries(searcher,0.3884282f);
    }

    /**
     * Tests two span queries.
     *
     * @throws IOException
     */
    protected void doTestBooleanQueryWithSpanQueries(IndexSearcher s, final float expectedScore) throws IOException {

        final Query spanQuery = new SpanTermQuery(new Term(FIELD_TEXT, "work"));
        final BooleanQuery query = new BooleanQuery();
        query.add(spanQuery, BooleanClause.Occur.MUST);
        query.add(spanQuery, BooleanClause.Occur.MUST);
        final String[] expectedIds = new String[] { "1", "2", "3", "4" };
        final float[] expectedScores = new float[] { expectedScore, expectedScore, expectedScore, expectedScore };
        assertHits(s, query, "two span queries", expectedIds, expectedScores);
    }


    /**
     * Checks to see if the hits are what we expected.
     *
     * @param query the query to execute
     * @param description the description of the search
     * @param expectedIds the expected document ids of the hits
     * @param expectedScores the expected scores of the hits
     *
     * @throws IOException
     */
    protected static void assertHits(Searcher s, Query query, final String description, final String[] expectedIds,
            final float[] expectedScores) throws IOException {
        QueryUtils.check(query,s);

        final float tolerance = 1e-5f;

        // Hits hits = searcher.search(query);
        // hits normalizes and throws things off if one score is greater than 1.0
        TopDocs topdocs = s.search(query,null,10000);

        /*****
        // display the hits
        System.out.println(hits.length() + " hits for search: \"" + description + '\"');
        for (int i = 0; i < hits.length(); i++) {
            System.out.println("  " + FIELD_ID + ':' + hits.doc(i).get(FIELD_ID) + " (score:" + hits.score(i) + ')');
        }
        *****/

        // did we get the hits we expected
        assertEquals(expectedIds.length, topdocs.totalHits);
        for (int i = 0; i < topdocs.totalHits; i++) {
            //System.out.println(i + " exp: " + expectedIds[i]);
            //System.out.println(i + " field: " + hits.doc(i).get(FIELD_ID));

            int id = topdocs.scoreDocs[i].doc;
            float score = topdocs.scoreDocs[i].score;
            Document doc = s.doc(id);
            assertEquals(expectedIds[i], doc.get(FIELD_ID));
            boolean scoreEq = Math.abs(expectedScores[i] - score) < tolerance;
            if (!scoreEq) {
              System.out.println(i + " warning, expected score: " + expectedScores[i] + ", actual " + score);
              System.out.println(s.explain(query,id));
            }
            assertEquals(expectedScores[i], score, tolerance);
            assertEquals(s.explain(query,id).getValue(), score, tolerance);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2962.java