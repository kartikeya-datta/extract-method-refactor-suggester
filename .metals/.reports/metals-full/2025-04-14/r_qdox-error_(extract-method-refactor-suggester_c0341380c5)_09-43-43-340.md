error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/951.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/951.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/951.java
text:
```scala
T@@ermsEnum termsEnum = afd.getBytesValues().getTermsEnum();

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

import com.carrotsearch.randomizedtesting.generators.RandomPicks;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queries.TermFilter;
import org.apache.lucene.search.*;
import org.apache.lucene.search.join.FixedBitSetCachingWrapperFilter;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.OpenBitSet;
import org.apache.lucene.util.TestUtil;
import org.apache.lucene.util.UnicodeUtil;
import org.elasticsearch.common.lucene.search.NotFilter;
import org.elasticsearch.common.lucene.search.XFilteredQuery;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.index.fielddata.IndexFieldData.XFieldComparatorSource;
import org.elasticsearch.index.fielddata.fieldcomparator.BytesRefFieldComparatorSource;
import org.elasticsearch.index.fielddata.ordinals.GlobalOrdinalsIndexFieldData;
import org.elasticsearch.index.search.nested.NestedFieldComparatorSource;
import org.elasticsearch.search.MultiValueMode;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;

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

    public void testActualMissingValue() throws IOException {
        testActualMissingValue(false);
    }

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
            values[i] = TestUtil.randomUnicodeString(getRandom());
        }
        final int numDocs = scaledRandomIntBetween(10, 10000);
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
        XFieldComparatorSource comparator = indexFieldData.comparatorSource(missingValue, MultiValueMode.MIN);
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

    public void testSortMissingFirst() throws IOException {
        testSortMissing(true, false);
    }

    public void testSortMissingFirstReverse() throws IOException {
        testSortMissing(true, true);
    }

    public void testSortMissingLast() throws IOException {
        testSortMissing(false, false);
    }

    public void testSortMissingLastReverse() throws IOException {
        testSortMissing(false, true);
    }

    public void testSortMissing(boolean first, boolean reverse) throws IOException {
        Document d = new Document();
        final StringField s = new StringField("value", "", Field.Store.YES);
        d.add(s);
        final String[] values = new String[randomIntBetween(2, 10)];
        for (int i = 1; i < values.length; ++i) {
            values[i] = TestUtil.randomUnicodeString(getRandom());
        }
        final int numDocs = scaledRandomIntBetween(10, 10000);
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
        XFieldComparatorSource comparator = indexFieldData.comparatorSource(first ? "_first" : "_last", MultiValueMode.MIN);
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

    public void testNestedSortingMin() throws IOException {
        testNestedSorting(MultiValueMode.MIN);
    }

    public void testNestedSortingMax() throws IOException {
        testNestedSorting(MultiValueMode.MAX);
    }

    public void testNestedSorting(MultiValueMode sortMode) throws IOException {
        final String[] values = new String[randomIntBetween(2, 20)];
        for (int i = 0; i < values.length; ++i) {
            values[i] = TestUtil.randomSimpleString(getRandom());
        }
        final int numParents = scaledRandomIntBetween(10, 10000);
        List<Document> docs = new ArrayList<>();
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
            missingValue = new BytesRef(TestUtil.randomSimpleString(getRandom()));
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
                    } else if (sortMode == MultiValueMode.MIN && bytesValue.compareTo(cmpValue) < 0) {
                        cmpValue = bytesValue;
                    } else if (sortMode == MultiValueMode.MAX && bytesValue.compareTo(cmpValue) > 0) {
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

    @Test
    public void testGlobalOrdinals() throws Exception {
        fillExtendedMvSet();
        refreshReader();
        FieldDataType fieldDataType = new FieldDataType("string", ImmutableSettings.builder().put("global_values", "fixed"));
        IndexFieldData.WithOrdinals ifd = getForField(fieldDataType, "value");
        IndexFieldData.WithOrdinals globalOrdinals = ifd.loadGlobal(topLevelReader);
        assertThat(topLevelReader.leaves().size(), equalTo(3));

        // First segment
        assertThat(globalOrdinals, instanceOf(GlobalOrdinalsIndexFieldData.class));
        AtomicFieldData.WithOrdinals afd = globalOrdinals.load(topLevelReader.leaves().get(0));
        BytesValues.WithOrdinals values = afd.getBytesValues();
        assertThat(values.setDocument(0), equalTo(2));
        long ord = values.nextOrd();
        assertThat(ord, equalTo(3l));
        assertThat(values.getValueByOrd(ord).utf8ToString(), equalTo("02"));
        ord = values.nextOrd();
        assertThat(ord, equalTo(5l));
        assertThat(values.getValueByOrd(ord).utf8ToString(), equalTo("04"));
        assertThat(values.setDocument(1), equalTo(0));
        assertThat(values.setDocument(2), equalTo(1));
        ord = values.nextOrd();
        assertThat(ord, equalTo(4l));
        assertThat(values.getValueByOrd(ord).utf8ToString(), equalTo("03"));

        // Second segment
        afd = globalOrdinals.load(topLevelReader.leaves().get(1));
        values = afd.getBytesValues();
        assertThat(values.setDocument(0), equalTo(3));
        ord = values.nextOrd();
        assertThat(ord, equalTo(5l));
        assertThat(values.getValueByOrd(ord).utf8ToString(), equalTo("04"));
        ord = values.nextOrd();
        assertThat(ord, equalTo(6l));
        assertThat(values.getValueByOrd(ord).utf8ToString(), equalTo("05"));
        ord = values.nextOrd();
        assertThat(ord, equalTo(7l));
        assertThat(values.getValueByOrd(ord).utf8ToString(), equalTo("06"));
        assertThat(values.setDocument(1), equalTo(3));
        ord = values.nextOrd();
        assertThat(ord, equalTo(7l));
        assertThat(values.getValueByOrd(ord).utf8ToString(), equalTo("06"));
        ord = values.nextOrd();
        assertThat(ord, equalTo(8l));
        assertThat(values.getValueByOrd(ord).utf8ToString(), equalTo("07"));
        ord = values.nextOrd();
        assertThat(ord, equalTo(9l));
        assertThat(values.getValueByOrd(ord).utf8ToString(), equalTo("08"));
        assertThat(values.setDocument(2), equalTo(0));
        assertThat(values.setDocument(3), equalTo(3));
        ord = values.nextOrd();
        assertThat(ord, equalTo(9l));
        assertThat(values.getValueByOrd(ord).utf8ToString(), equalTo("08"));
        ord = values.nextOrd();
        assertThat(ord, equalTo(10l));
        assertThat(values.getValueByOrd(ord).utf8ToString(), equalTo("09"));
        ord = values.nextOrd();
        assertThat(ord, equalTo(11l));
        assertThat(values.getValueByOrd(ord).utf8ToString(), equalTo("10"));

        // Third segment
        afd = globalOrdinals.load(topLevelReader.leaves().get(2));
        values = afd.getBytesValues();
        assertThat(values.setDocument(0), equalTo(3));
        ord = values.nextOrd();
        assertThat(ord, equalTo(0l));
        assertThat(values.getValueByOrd(ord).utf8ToString(), equalTo("!08"));
        ord = values.nextOrd();
        assertThat(ord, equalTo(1l));
        assertThat(values.getValueByOrd(ord).utf8ToString(), equalTo("!09"));
        ord = values.nextOrd();
        assertThat(ord, equalTo(2l));
        assertThat(values.getValueByOrd(ord).utf8ToString(), equalTo("!10"));
    }

    @Test
    public void testTermsEnum() throws Exception {
        fillExtendedMvSet();
        AtomicReaderContext atomicReaderContext = refreshReader();

        IndexFieldData.WithOrdinals ifd = getForField("value");
        AtomicFieldData.WithOrdinals afd = ifd.load(atomicReaderContext);

        TermsEnum termsEnum = afd.getTermsEnum();
        int size = 0;
        while (termsEnum.next() != null) {
            size++;
        }
        assertThat(size, equalTo(12));

        assertThat(termsEnum.seekExact(new BytesRef("10")), is(true));
        assertThat(termsEnum.term().utf8ToString(), equalTo("10"));
        assertThat(termsEnum.next(), nullValue());

        assertThat(termsEnum.seekExact(new BytesRef("08")), is(true));
        assertThat(termsEnum.term().utf8ToString(), equalTo("08"));
        size = 0;
        while (termsEnum.next() != null) {
            size++;
        }
        assertThat(size, equalTo(2));

        termsEnum.seekExact(8);
        assertThat(termsEnum.term().utf8ToString(), equalTo("07"));
        size = 0;
        while (termsEnum.next() != null) {
            size++;
        }
        assertThat(size, equalTo(3));
    }

    @Test
    public void testGlobalOrdinalsGetRemovedOnceIndexReaderCloses() throws Exception {
        fillExtendedMvSet();
        refreshReader();
        FieldDataType fieldDataType = new FieldDataType("string", ImmutableSettings.builder().put("global_values", "fixed"));
        IndexFieldData.WithOrdinals ifd = getForField(fieldDataType, "value");
        IndexFieldData.WithOrdinals globalOrdinals = ifd.loadGlobal(topLevelReader);
        assertThat(ifd.loadGlobal(topLevelReader), sameInstance(globalOrdinals));
        // 3 b/c 1 segment level caches and 1 top level cache
        assertThat(indicesFieldDataCache.getCache().size(), equalTo(4l));

        IndexFieldData.WithOrdinals cachedInstace = null;
        for (RamUsage ramUsage : indicesFieldDataCache.getCache().asMap().values()) {
            if (ramUsage instanceof IndexFieldData.WithOrdinals) {
                cachedInstace = (IndexFieldData.WithOrdinals) ramUsage;
                break;
            }
        }
        assertThat(cachedInstace, sameInstance(globalOrdinals));
        topLevelReader.close();
        // Now only 3 segment level entries, only the toplevel reader has been closed, but the segment readers are still used by IW
        assertThat(indicesFieldDataCache.getCache().size(), equalTo(3l));

        refreshReader();
        assertThat(ifd.loadGlobal(topLevelReader), not(sameInstance(globalOrdinals)));

        ifdService.clear();
        assertThat(indicesFieldDataCache.getCache().size(), equalTo(0l));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/951.java