error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7438.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7438.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7438.java
text:
```scala
final A@@rrayList<String> fieldsOrder = new ArrayList<>();

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

package org.elasticsearch.deps.lucene;

import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.NumericUtils;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.test.ElasticsearchTestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 *
 */
public class SimpleLuceneTests extends ElasticsearchTestCase {

    @Test
    public void testSortValues() throws Exception {
        Directory dir = new RAMDirectory();
        IndexWriter indexWriter = new IndexWriter(dir, new IndexWriterConfig(Lucene.VERSION, Lucene.STANDARD_ANALYZER));
        for (int i = 0; i < 10; i++) {
            Document document = new Document();
            document.add(new TextField("str", new String(new char[]{(char) (97 + i), (char) (97 + i)}), Field.Store.YES));
            indexWriter.addDocument(document);
        }
        IndexReader reader = DirectoryReader.open(indexWriter, true);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopFieldDocs docs = searcher.search(new MatchAllDocsQuery(), null, 10, new Sort(new SortField("str", SortField.Type.STRING)));
        for (int i = 0; i < 10; i++) {
            FieldDoc fieldDoc = (FieldDoc) docs.scoreDocs[i];
            assertThat((BytesRef) fieldDoc.fields[0], equalTo(new BytesRef(new String(new char[]{(char) (97 + i), (char) (97 + i)}))));
        }
    }

    @Test
    public void testAddDocAfterPrepareCommit() throws Exception {
        Directory dir = new RAMDirectory();
        IndexWriter indexWriter = new IndexWriter(dir, new IndexWriterConfig(Lucene.VERSION, Lucene.STANDARD_ANALYZER));
        Document document = new Document();
        document.add(new TextField("_id", "1", Field.Store.YES));
        indexWriter.addDocument(document);
        DirectoryReader reader = DirectoryReader.open(indexWriter, true);
        assertThat(reader.numDocs(), equalTo(1));

        indexWriter.prepareCommit();
        // Returns null b/c no changes.
        assertThat(DirectoryReader.openIfChanged(reader), equalTo(null));

        document = new Document();
        document.add(new TextField("_id", "2", Field.Store.YES));
        indexWriter.addDocument(document);
        indexWriter.commit();
        reader = DirectoryReader.openIfChanged(reader);
        assertThat(reader.numDocs(), equalTo(2));
    }

    @Test
    public void testSimpleNumericOps() throws Exception {
        Directory dir = new RAMDirectory();
        IndexWriter indexWriter = new IndexWriter(dir, new IndexWriterConfig(Lucene.VERSION, Lucene.STANDARD_ANALYZER));

        Document document = new Document();
        document.add(new TextField("_id", "1", Field.Store.YES));
        document.add(new IntField("test", 2, IntField.TYPE_STORED));
        indexWriter.addDocument(document);

        IndexReader reader = DirectoryReader.open(indexWriter, true);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs topDocs = searcher.search(new TermQuery(new Term("_id", "1")), 1);
        Document doc = searcher.doc(topDocs.scoreDocs[0].doc);
        IndexableField f = doc.getField("test");
        assertThat(f.stringValue(), equalTo("2"));

        BytesRef bytes = new BytesRef();
        NumericUtils.intToPrefixCoded(2, 0, bytes);
        topDocs = searcher.search(new TermQuery(new Term("test", bytes)), 1);
        doc = searcher.doc(topDocs.scoreDocs[0].doc);
        f = doc.getField("test");
        assertThat(f.stringValue(), equalTo("2"));

        indexWriter.close();
    }

    /**
     * Here, we verify that the order that we add fields to a document counts, and not the lexi order
     * of the field. This means that heavily accessed fields that use field selector should be added
     * first (with load and break).
     */
    @Test
    public void testOrdering() throws Exception {
        Directory dir = new RAMDirectory();
        IndexWriter indexWriter = new IndexWriter(dir, new IndexWriterConfig(Lucene.VERSION, Lucene.STANDARD_ANALYZER));

        Document document = new Document();
        document.add(new TextField("_id", "1", Field.Store.YES));
        document.add(new TextField("#id", "1", Field.Store.YES));
        indexWriter.addDocument(document);

        IndexReader reader = DirectoryReader.open(indexWriter, true);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs topDocs = searcher.search(new TermQuery(new Term("_id", "1")), 1);
        final ArrayList<String> fieldsOrder = new ArrayList<String>();
        searcher.doc(topDocs.scoreDocs[0].doc, new StoredFieldVisitor() {
            @Override
            public Status needsField(FieldInfo fieldInfo) throws IOException {
                fieldsOrder.add(fieldInfo.name);
                return Status.YES;
            }
        });

        assertThat(fieldsOrder.size(), equalTo(2));
        assertThat(fieldsOrder.get(0), equalTo("_id"));
        assertThat(fieldsOrder.get(1), equalTo("#id"));

        indexWriter.close();
    }

    @Test
    public void testBoost() throws Exception {
        Directory dir = new RAMDirectory();
        IndexWriter indexWriter = new IndexWriter(dir, new IndexWriterConfig(Lucene.VERSION, Lucene.STANDARD_ANALYZER));

        for (int i = 0; i < 100; i++) {
            // TODO (just setting the boost value does not seem to work...)
            StringBuilder value = new StringBuilder().append("value");
            for (int j = 0; j < i; j++) {
                value.append(" ").append("value");
            }
            Document document = new Document();
            TextField textField = new TextField("_id", Integer.toString(i), Field.Store.YES);
            textField.setBoost(i);
            document.add(textField);
            textField = new TextField("value", value.toString(), Field.Store.YES);
            textField.setBoost(i);
            document.add(textField);
            indexWriter.addDocument(document);
        }

        IndexReader reader = DirectoryReader.open(indexWriter, true);
        IndexSearcher searcher = new IndexSearcher(reader);
        TermQuery query = new TermQuery(new Term("value", "value"));
        TopDocs topDocs = searcher.search(query, 100);
        assertThat(100, equalTo(topDocs.totalHits));
        for (int i = 0; i < topDocs.scoreDocs.length; i++) {
            Document doc = searcher.doc(topDocs.scoreDocs[i].doc);
//            System.out.println(doc.get("id") + ": " + searcher.explain(query, topDocs.scoreDocs[i].doc));
            assertThat(doc.get("_id"), equalTo(Integer.toString(100 - i - 1)));
        }

        indexWriter.close();
    }

    @Test
    public void testNRTSearchOnClosedWriter() throws Exception {
        Directory dir = new RAMDirectory();
        IndexWriter indexWriter = new IndexWriter(dir, new IndexWriterConfig(Lucene.VERSION, Lucene.STANDARD_ANALYZER));
        DirectoryReader reader = DirectoryReader.open(indexWriter, true);

        for (int i = 0; i < 100; i++) {
            Document document = new Document();
            TextField field = new TextField("_id", Integer.toString(i), Field.Store.YES);
            field.setBoost(i);
            document.add(field);
            indexWriter.addDocument(document);
        }
        reader = refreshReader(reader);

        indexWriter.close();

        TermsEnum termDocs = SlowCompositeReaderWrapper.wrap(reader).terms("_id").iterator(null);
        termDocs.next();
    }

    /**
     * A test just to verify that term freqs are not stored for numeric fields. <tt>int1</tt> is not storing termFreq
     * and <tt>int2</tt> does.
     */
    @Test
    public void testNumericTermDocsFreqs() throws Exception {
        Directory dir = new RAMDirectory();
        IndexWriter indexWriter = new IndexWriter(dir, new IndexWriterConfig(Lucene.VERSION, Lucene.STANDARD_ANALYZER));

        Document doc = new Document();
        FieldType type = IntField.TYPE_NOT_STORED;
        IntField field = new IntField("int1", 1, type);
        doc.add(field);

        type = new FieldType(IntField.TYPE_NOT_STORED);
        type.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS);
        type.freeze();

        field = new IntField("int1", 1, type);
        doc.add(field);

        field = new IntField("int2", 1, type);
        doc.add(field);

        field = new IntField("int2", 1, type);
        doc.add(field);

        indexWriter.addDocument(doc);

        IndexReader reader = DirectoryReader.open(indexWriter, true);
        AtomicReader atomicReader = SlowCompositeReaderWrapper.wrap(reader);

        Terms terms = atomicReader.terms("int1");
        TermsEnum termsEnum = terms.iterator(null);
        termsEnum.next();

        DocsEnum termDocs = termsEnum.docs(atomicReader.getLiveDocs(), null);
        assertThat(termDocs.nextDoc(), equalTo(0));
        assertThat(termDocs.docID(), equalTo(0));
        assertThat(termDocs.freq(), equalTo(1));

        terms = atomicReader.terms("int2");
        termsEnum = terms.iterator(termsEnum);
        termsEnum.next();
        termDocs =  termsEnum.docs(atomicReader.getLiveDocs(), termDocs);
        assertThat(termDocs.nextDoc(), equalTo(0));
        assertThat(termDocs.docID(), equalTo(0));
        assertThat(termDocs.freq(), equalTo(2));

        reader.close();
        indexWriter.close();
    }

    private DirectoryReader refreshReader(DirectoryReader reader) throws IOException {
        DirectoryReader oldReader = reader;
        reader = DirectoryReader.openIfChanged(reader);
        if (reader != oldReader) {
            oldReader.close();
        }
        return reader;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7438.java