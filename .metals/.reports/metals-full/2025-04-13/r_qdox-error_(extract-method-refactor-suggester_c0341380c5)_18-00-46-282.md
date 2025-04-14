error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7443.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7443.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7443.java
text:
```scala
L@@ist<Document> docs = new ArrayList<>();

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.fielddata;

import com.carrotsearch.randomizedtesting.annotations.Repeat;
import com.carrotsearch.randomizedtesting.generators.RandomPicks;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.TermFilter;
import org.apache.lucene.search.*;
import org.apache.lucene.search.join.FixedBitSetCachingWrapperFilter;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.OpenBitSet;
import org.apache.lucene.util.UnicodeUtil;
import org.apache.lucene.util._TestUtil;
import org.elasticsearch.common.lucene.search.NotFilter;
import org.elasticsearch.common.lucene.search.XFilteredQuery;
import org.elasticsearch.index.fielddata.IndexFieldData.XFieldComparatorSource;
import org.elasticsearch.index.fielddata.fieldcomparator.BytesRefFieldComparatorSource;
import org.elasticsearch.index.fielddata.fieldcomparator.SortMode;
import org.elasticsearch.index.search.nested.NestedFieldComparatorSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 */
public abstract class AbstractStringFieldDataTests extends AbstractFieldDataImplTests {

    protected void fillSingleValueAllSet() throws Exception {
        Document d = new Document();
        d.add(new StringField("_id", "1", Field.Store.NO));
        d.add(new StringField("value", "2", Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField("_id", "1", Field.Store.NO));
        d.add(new StringField("value", "1", Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField("_id", "3", Field.Store.NO));
        d.add(new StringField("value", "3", Field.Store.NO));
        writer.addDocument(d);
    }

    protected void add2SingleValuedDocumentsAndDeleteOneOfThem() throws Exception {
        Document d = new Document();
        d.add(new StringField("_id", "1", Field.Store.NO));
        d.add(new StringField("value", "2", Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField("_id", "2", Field.Store.NO));
        d.add(new StringField("value", "4", Field.Store.NO));
        writer.addDocument(d);

        writer.commit();

        writer.deleteDocuments(new Term("_id", "1"));
    }

    protected void fillSingleValueWithMissing() throws Exception {
        Document d = new Document();
        d.add(new StringField("_id", "1", Field.Store.NO));
        d.add(new StringField("value", "2", Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField("_id", "2", Field.Store.NO));
        //d.add(new StringField("value", one(), Field.Store.NO)); // MISSING....
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField("_id", "3", Field.Store.NO));
        d.add(new StringField("value", "3", Field.Store.NO));
        writer.addDocument(d);
    }

    protected void fillMultiValueAllSet() throws Exception {
        Document d = new Document();
        d.add(new StringField("_id", "1", Field.Store.NO));
        d.add(new StringField("value", "2", Field.Store.NO));
        d.add(new StringField("value", "4", Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField("_id", "2", Field.Store.NO));
        d.add(new StringField("value", "1", Field.Store.NO));
        writer.addDocument(d);
        writer.commit(); // TODO: Have tests with more docs for sorting

        d = new Document();
        d.add(new StringField("_id", "3", Field.Store.NO));
        d.add(new StringField("value", "3", Field.Store.NO));
        writer.addDocument(d);
    }

    protected void fillMultiValueWithMissing() throws Exception {
        Document d = new Document();
        d.add(new StringField("_id", "1", Field.Store.NO));
        d.add(new StringField("value", "2", Field.Store.NO));
        d.add(new StringField("value", "4", Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField("_id", "2", Field.Store.NO));
        //d.add(new StringField("value", one(), Field.Store.NO)); // MISSING
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField("_id", "3", Field.Store.NO));
        d.add(new StringField("value", "3", Field.Store.NO));
        writer.addDocument(d);
    }

    protected void fillAllMissing() throws Exception {
        Document d = new Document();
        d.add(new StringField("_id", "1", Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField("_id", "2", Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField("_id", "3", Field.Store.NO));
        writer.addDocument(d);
    }

    protected void fillExtendedMvSet() throws Exception {
        Document d = new Document();
        d.add(new StringField("_id", "1", Field.Store.NO));
        d.add(new StringField("value", "02", Field.Store.NO));
        d.add(new StringField("value", "04", Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField("_id", "2", Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField("_id", "3", Field.Store.NO));
        d.add(new StringField("value", "03", Field.Store.NO));
        writer.addDocument(d);
        writer.commit();

        d = new Document();
        d.add(new StringField("_id", "4", Field.Store.NO));
        d.add(new StringField("value", "04", Field.Store.NO));
        d.add(new StringField("value", "05", Field.Store.NO));
        d.add(new StringField("value", "06", Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField("_id", "5", Field.Store.NO));
        d.add(new StringField("value", "06", Field.Store.NO));
        d.add(new StringField("value", "07", Field.Store.NO));
        d.add(new StringField("value", "08", Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField("_id", "6", Field.Store.NO));
        writer.addDocument(d);

        d = new Document();
        d.add(new StringField("_id", "7", Field.Store.NO));
        d.add(new StringField("value", "08", Field.Store.NO));
        d.add(new StringField("value", "09", Field.Store.NO));
        d.add(new StringField("value", "10", Field.Store.NO));
        writer.addDocument(d);
        writer.commit();

        d = new Document();
        d.add(new StringField("_id", "8", Field.Store.NO));
        d.add(new StringField("value", "!08", Field.Store.NO));
        d.add(new StringField("value", "!09", Field.Store.NO));
        d.add(new StringField("value", "!10", Field.Store.NO));
        writer.addDocument(d);
    }

    @Repeat(iterations=10)
    public void testActualMissingValue() throws IOException {
        testActualMissingValue(false);
    }

    @Repeat(iterations=10)
    public void testActualMissingValueReverse() throws IOException {
        testActualMissingValue(true);
    }

    public void testActualMissingValue(boolean reverse) throws IOException {
        // missing value is set to an actual value
        Document d = new Document();
        final StringField s = new StringField("value", "", Field.Store.YES);
        d.add(s);
        final String[] values = new String[randomIntBetween(2, 30)];
        for (int i = 1; i < values.length; ++i) {
            values[i] = _TestUtil.randomUnicodeString(getRandom());
        }
        final int numDocs = scaledRandomIntBetween(100, 10000);
        for (int i = 0; i < numDocs; ++i) {
            final String value = RandomPicks.randomFrom(getRandom(), values);
            if (value == null) {
                writer.addDocument(new Document());
            } else {
                s.setStringValue(value);
                writer.addDocument(d);
            }
            if (randomInt(10) == 0) {
                writer.commit();
            }
        }

        final IndexFieldData indexFieldData = getForField("value");
        final String missingValue = values[1];
        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(writer, true));
        XFieldComparatorSource comparator = indexFieldData.comparatorSource(missingValue, SortMode.MIN);
        TopFieldDocs topDocs = searcher.search(new MatchAllDocsQuery(), randomBoolean() ? numDocs : randomIntBetween(10, numDocs), new Sort(new SortField("value", comparator, reverse)));
        assertEquals(numDocs, topDocs.totalHits);
        BytesRef previousValue = reverse ? UnicodeUtil.BIG_TERM : new BytesRef();
        for (int i = 0; i < topDocs.scoreDocs.length; ++i) {
            final String docValue = searcher.doc(topDocs.scoreDocs[i].doc).get("value");
            final BytesRef value = new BytesRef(docValue == null ? missingValue : docValue);
            if (reverse) {
                assertTrue(previousValue.compareTo(value) >= 0);
            } else {
                assertTrue(previousValue.compareTo(value) <= 0);
            }
            previousValue = value;
        }
        searcher.getIndexReader().close();
    }

    @Repeat(iterations=3)
    public void testSortMissingFirst() throws IOException {
        testSortMissing(true, false);
    }

    @Repeat(iterations=3)
    public void testSortMissingFirstReverse() throws IOException {
        testSortMissing(true, true);
    }

    @Repeat(iterations=3)
    public void testSortMissingLast() throws IOException {
        testSortMissing(false, false);
    }

    @Repeat(iterations=3)
    public void testSortMissingLastReverse() throws IOException {
        testSortMissing(false, true);
    }

    public void testSortMissing(boolean first, boolean reverse) throws IOException {
        Document d = new Document();
        final StringField s = new StringField("value", "", Field.Store.YES);
        d.add(s);
        final String[] values = new String[randomIntBetween(2, 10)];
        for (int i = 1; i < values.length; ++i) {
            values[i] = _TestUtil.randomUnicodeString(getRandom());
        }
        final int numDocs = scaledRandomIntBetween(100, 10000);
        for (int i = 0; i < numDocs; ++i) {
            final String value = RandomPicks.randomFrom(getRandom(), values);
            if (value == null) {
                writer.addDocument(new Document());
            } else {
                s.setStringValue(value);
                writer.addDocument(d);
            }
            if (randomInt(10) == 0) {
                writer.commit();
            }
        }
        final IndexFieldData indexFieldData = getForField("value");
        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(writer, true));
        XFieldComparatorSource comparator = indexFieldData.comparatorSource(first ? "_first" : "_last", SortMode.MIN);
        TopFieldDocs topDocs = searcher.search(new MatchAllDocsQuery(), randomBoolean() ? numDocs : randomIntBetween(10, numDocs), new Sort(new SortField("value", comparator, reverse)));
        assertEquals(numDocs, topDocs.totalHits);
        BytesRef previousValue = first ? null : reverse ? UnicodeUtil.BIG_TERM : new BytesRef();
        for (int i = 0; i < topDocs.scoreDocs.length; ++i) {
            final String docValue = searcher.doc(topDocs.scoreDocs[i].doc).get("value");
            if (first && docValue == null) {
                assertNull(previousValue);
            } else if (!first && docValue != null) {
                assertNotNull(previousValue);
            }
            final BytesRef value = docValue == null ? null : new BytesRef(docValue);
            if (previousValue != null && value != null) {
                if (reverse) {
                    assertTrue(previousValue.compareTo(value) >= 0);
                } else {
                    assertTrue(previousValue.compareTo(value) <= 0);
                }
            }
            previousValue = value;
        }
        searcher.getIndexReader().close();
    }

    @Repeat(iterations=3)
    public void testNestedSortingMin() throws IOException {
        testNestedSorting(SortMode.MIN);
    }

    @Repeat(iterations=3)
    public void testNestedSortingMax() throws IOException {
        testNestedSorting(SortMode.MAX);
    }

    public void testNestedSorting(SortMode sortMode) throws IOException {
        final String[] values = new String[randomIntBetween(2, 20)];
        for (int i = 0; i < values.length; ++i) {
            values[i] = _TestUtil.randomSimpleString(getRandom());
        }
        final int numParents = scaledRandomIntBetween(100, 10000);
        List<Document> docs = new ArrayList<Document>();
        final OpenBitSet parents = new OpenBitSet();
        for (int i = 0; i < numParents; ++i) {
            docs.clear();
            final int numChildren = randomInt(4);
            for (int j = 0; j < numChildren; ++j) {
                final Document child = new Document();
                final int numValues = randomInt(3);
                for (int k = 0; k < numValues; ++k) {
                    final String value = RandomPicks.randomFrom(getRandom(), values);
                    child.add(new StringField("text", value, Store.YES));
                }
                docs.add(child);
            }
            final Document parent = new Document();
            parent.add(new StringField("type", "parent", Store.YES));
            final String value = RandomPicks.randomFrom(getRandom(), values);
            if (value != null) {
                parent.add(new StringField("text", value, Store.YES));
            }
            docs.add(parent);
            parents.set(parents.prevSetBit(parents.length() - 1) + docs.size());
            writer.addDocuments(docs);
            if (randomInt(10) == 0) {
                writer.commit();
            }
        }
        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(writer, true));
        IndexFieldData<?> fieldData = getForField("text");
        final BytesRef missingValue;
        switch (randomInt(4)) {
        case 0:
            missingValue = new BytesRef();
            break;
        case 1:
            missingValue = BytesRefFieldComparatorSource.MAX_TERM;
            break;
        case 2:
            missingValue = new BytesRef(RandomPicks.randomFrom(getRandom(), values));
            break;
        default:
            missingValue = new BytesRef(_TestUtil.randomSimpleString(getRandom()));
            break;
        }
        BytesRefFieldComparatorSource innerSource = new BytesRefFieldComparatorSource(fieldData, missingValue, sortMode);
        Filter parentFilter = new TermFilter(new Term("type", "parent"));
        Filter childFilter = new NotFilter(parentFilter);
        NestedFieldComparatorSource nestedComparatorSource = new NestedFieldComparatorSource(sortMode, innerSource, parentFilter, childFilter);
        ToParentBlockJoinQuery query = new ToParentBlockJoinQuery(new XFilteredQuery(new MatchAllDocsQuery(), childFilter), new FixedBitSetCachingWrapperFilter(parentFilter), ScoreMode.None);
        Sort sort = new Sort(new SortField("text", nestedComparatorSource));
        TopFieldDocs topDocs = searcher.search(query, randomIntBetween(1, numParents), sort);
        assertTrue(topDocs.scoreDocs.length > 0);
        BytesRef previous = null;
        for (int i = 0; i < topDocs.scoreDocs.length; ++i) {
            final int docID = topDocs.scoreDocs[i].doc;
            assertTrue("expected " + docID + " to be a parent", parents.get(docID));
            BytesRef cmpValue = null;
            for (int child = parents.prevSetBit(docID - 1) + 1; child < docID; ++child) {
                String[] vals = searcher.doc(child).getValues("text");
                if (vals.length == 0) {
                    vals = new String[] {missingValue.utf8ToString()};
                }
                for (String value : vals) {
                    final BytesRef bytesValue = new BytesRef(value);
                    if (cmpValue == null) {
                        cmpValue = bytesValue;
                    } else if (sortMode == SortMode.MIN && bytesValue.compareTo(cmpValue) < 0) {
                        cmpValue = bytesValue;
                    } else if (sortMode == SortMode.MAX && bytesValue.compareTo(cmpValue) > 0) {
                        cmpValue = bytesValue;
                    }
                }
            }
            if (cmpValue == null) {
                cmpValue = missingValue;
            }
            if (previous != null) {
                assertNotNull(cmpValue);
                assertTrue(previous.utf8ToString() + "   /   " + cmpValue.utf8ToString(), previous.compareTo(cmpValue) <= 0);
            }
            previous = cmpValue;
        }
        searcher.getIndexReader().close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7443.java