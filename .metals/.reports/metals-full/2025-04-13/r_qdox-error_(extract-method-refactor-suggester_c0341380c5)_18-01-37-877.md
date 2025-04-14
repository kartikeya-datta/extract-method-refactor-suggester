error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6457.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6457.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6457.java
text:
```scala
public T@@okenStream tokenStream(Analyzer analyzer, TokenStream reuse) throws IOException {

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
package org.elasticsearch.common.lucene.uid;

import com.google.common.collect.ImmutableMap;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.*;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.common.Numbers;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.index.mapper.internal.UidFieldMapper;
import org.elasticsearch.index.mapper.internal.VersionFieldMapper;
import org.elasticsearch.index.merge.policy.ElasticsearchMergePolicy;
import org.elasticsearch.test.ElasticsearchLuceneTestCase;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class VersionsTests extends ElasticsearchLuceneTestCase {
    
    public static DirectoryReader reopen(DirectoryReader reader) throws IOException {
        return reopen(reader, true);
    }

    public static DirectoryReader reopen(DirectoryReader reader, boolean newReaderExpected) throws IOException {
        DirectoryReader newReader = DirectoryReader.openIfChanged(reader);
        if (newReader != null) {
            reader.close();
        } else {
            assertFalse(newReaderExpected);
        }
        return newReader;
    }
    @Test
    public void testVersions() throws Exception {
        Directory dir = newDirectory();
        IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(Lucene.VERSION, Lucene.STANDARD_ANALYZER));
        DirectoryReader directoryReader = DirectoryReader.open(writer, true);
        MatcherAssert.assertThat(Versions.loadVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")), equalTo(Versions.NOT_FOUND));

        Document doc = new Document();
        doc.add(new Field(UidFieldMapper.NAME, "1", UidFieldMapper.Defaults.FIELD_TYPE));
        writer.addDocument(doc);
        directoryReader = reopen(directoryReader);
        assertThat(Versions.loadVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")), equalTo(Versions.NOT_SET));
        assertThat(Versions.loadDocIdAndVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")).version, equalTo(Versions.NOT_SET));

        doc = new Document();
        doc.add(new Field(UidFieldMapper.NAME, "1", UidFieldMapper.Defaults.FIELD_TYPE));
        doc.add(new NumericDocValuesField(VersionFieldMapper.NAME, 1));
        writer.updateDocument(new Term(UidFieldMapper.NAME, "1"), doc);
        directoryReader = reopen(directoryReader);
        assertThat(Versions.loadVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")), equalTo(1l));
        assertThat(Versions.loadDocIdAndVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")).version, equalTo(1l));

        doc = new Document();
        Field uid = new Field(UidFieldMapper.NAME, "1", UidFieldMapper.Defaults.FIELD_TYPE);
        Field version = new NumericDocValuesField(VersionFieldMapper.NAME, 2);
        doc.add(uid);
        doc.add(version);
        writer.updateDocument(new Term(UidFieldMapper.NAME, "1"), doc);
        directoryReader = reopen(directoryReader);
        assertThat(Versions.loadVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")), equalTo(2l));
        assertThat(Versions.loadDocIdAndVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")).version, equalTo(2l));

        // test reuse of uid field
        doc = new Document();
        version.setLongValue(3);
        doc.add(uid);
        doc.add(version);
        writer.updateDocument(new Term(UidFieldMapper.NAME, "1"), doc);
        
        directoryReader = reopen(directoryReader);
        assertThat(Versions.loadVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")), equalTo(3l));
        assertThat(Versions.loadDocIdAndVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")).version, equalTo(3l));

        writer.deleteDocuments(new Term(UidFieldMapper.NAME, "1"));
        directoryReader = reopen(directoryReader);
        assertThat(Versions.loadVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")), equalTo(Versions.NOT_FOUND));
        assertThat(Versions.loadDocIdAndVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")), nullValue());
        directoryReader.close();
        writer.close();
        dir.close();
    }

    @Test
    public void testNestedDocuments() throws IOException {
        Directory dir = newDirectory();
        IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(Lucene.VERSION, Lucene.STANDARD_ANALYZER));

        List<Document> docs = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            // Nested
            Document doc = new Document();
            doc.add(new Field(UidFieldMapper.NAME, "1", UidFieldMapper.Defaults.NESTED_FIELD_TYPE));
            docs.add(doc);
        }
        // Root
        Document doc = new Document();
        doc.add(new Field(UidFieldMapper.NAME, "1", UidFieldMapper.Defaults.FIELD_TYPE));
        NumericDocValuesField version = new NumericDocValuesField(VersionFieldMapper.NAME, 5L);
        doc.add(version);
        docs.add(doc);

        writer.updateDocuments(new Term(UidFieldMapper.NAME, "1"), docs);
        DirectoryReader directoryReader = DirectoryReader.open(writer, true);
        assertThat(Versions.loadVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")), equalTo(5l));
        assertThat(Versions.loadDocIdAndVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")).version, equalTo(5l));

        version.setLongValue(6L);
        writer.updateDocuments(new Term(UidFieldMapper.NAME, "1"), docs);
        version.setLongValue(7L);
        writer.updateDocuments(new Term(UidFieldMapper.NAME, "1"), docs);
        directoryReader = reopen(directoryReader);
        assertThat(Versions.loadVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")), equalTo(7l));
        assertThat(Versions.loadDocIdAndVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")).version, equalTo(7l));

        writer.deleteDocuments(new Term(UidFieldMapper.NAME, "1"));
        directoryReader = reopen(directoryReader);
        assertThat(Versions.loadVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")), equalTo(Versions.NOT_FOUND));
        assertThat(Versions.loadDocIdAndVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")), nullValue());
        directoryReader.close();
        writer.close();
        dir.close();
    }

    @Test
    public void testBackwardCompatibility() throws IOException {
        Directory dir = newDirectory();
        IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(Lucene.VERSION, Lucene.STANDARD_ANALYZER));

        DirectoryReader directoryReader = DirectoryReader.open(writer, true);
        MatcherAssert.assertThat(Versions.loadVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")), equalTo(Versions.NOT_FOUND));

        Document doc = new Document();
        UidField uidAndVersion = new UidField("1", 1L);
        doc.add(uidAndVersion);
        writer.addDocument(doc);

        uidAndVersion.uid = "2";
        uidAndVersion.version = 2;
        writer.addDocument(doc);
        writer.commit();

        directoryReader = reopen(directoryReader);
        assertThat(Versions.loadVersion(directoryReader, new Term(UidFieldMapper.NAME, "1")), equalTo(1l));
        assertThat(Versions.loadVersion(directoryReader, new Term(UidFieldMapper.NAME, "2")), equalTo(2l));
        assertThat(Versions.loadVersion(directoryReader, new Term(UidFieldMapper.NAME, "3")), equalTo(Versions.NOT_FOUND));
        directoryReader.close();
        writer.close();
        dir.close();
    }

    // This is how versions used to be encoded
    private static class UidField extends Field {
        private static final FieldType FIELD_TYPE = new FieldType();
        static {
            FIELD_TYPE.setTokenized(true);
            FIELD_TYPE.setIndexed(true);
            FIELD_TYPE.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
            FIELD_TYPE.setStored(true);
            FIELD_TYPE.freeze();
        }
        String uid;
        long version;
        UidField(String uid, long version) {
            super(UidFieldMapper.NAME, uid, FIELD_TYPE);
            this.uid = uid;
            this.version = version;
        }
        @Override
        public TokenStream tokenStream(Analyzer analyzer) throws IOException {
            return new TokenStream() {
                boolean finished = true;
                final CharTermAttribute term = addAttribute(CharTermAttribute.class);
                final PayloadAttribute payload = addAttribute(PayloadAttribute.class);
                @Override
                public boolean incrementToken() throws IOException {
                    if (finished) {
                        return false;
                    }
                    term.setEmpty().append(uid);
                    payload.setPayload(new BytesRef(Numbers.longToBytes(version)));
                    finished = true;
                    return true;
                }
                @Override
                public void reset() throws IOException {
                    finished = false;
                }
            };
        }
    }

    @Test
    public void testMergingOldIndices() throws Exception {
        final IndexWriterConfig iwConf = new IndexWriterConfig(Lucene.VERSION, new KeywordAnalyzer());
        iwConf.setMergePolicy(new ElasticsearchMergePolicy(iwConf.getMergePolicy()));
        final Directory dir = newDirectory();
        final IndexWriter iw = new IndexWriter(dir, iwConf);

        // 1st segment, no _version
        Document document = new Document();
        // Add a dummy field (enough to trigger #3237)
        document.add(new StringField("a", "b", Store.NO));
        StringField uid = new StringField(UidFieldMapper.NAME, "1", Store.YES);
        document.add(uid);
        iw.addDocument(document);
        uid.setStringValue("2");
        iw.addDocument(document);
        iw.commit();

        // 2nd segment, old layout
        document = new Document();
        UidField uidAndVersion = new UidField("3", 3L);
        document.add(uidAndVersion);
        iw.addDocument(document);
        uidAndVersion.uid = "4";
        uidAndVersion.version = 4L;
        iw.addDocument(document);
        iw.commit();

        // 3rd segment new layout
        document = new Document();
        uid.setStringValue("5");
        Field version = new NumericDocValuesField(VersionFieldMapper.NAME, 5L);
        document.add(uid);
        document.add(version);
        iw.addDocument(document);
        uid.setStringValue("6");
        version.setLongValue(6L);
        iw.addDocument(document);
        iw.commit();

        final Map<String, Long> expectedVersions = ImmutableMap.<String, Long>builder()
                .put("1", 0L).put("2", 0L).put("3", 0L).put("4", 4L).put("5", 5L).put("6", 6L).build();

        // Force merge and check versions
        iw.forceMerge(1, true);
        final AtomicReader ir = SlowCompositeReaderWrapper.wrap(DirectoryReader.open(iw.getDirectory()));
        final NumericDocValues versions = ir.getNumericDocValues(VersionFieldMapper.NAME);
        assertThat(versions, notNullValue());
        for (int i = 0; i < ir.maxDoc(); ++i) {
            final String uidValue = ir.document(i).get(UidFieldMapper.NAME);
            final long expectedVersion = expectedVersions.get(uidValue);
            assertThat(versions.get(i), equalTo(expectedVersion));
        }

        iw.close();
        assertThat(IndexWriter.isLocked(iw.getDirectory()), is(false));
        ir.close();
        dir.close();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6457.java