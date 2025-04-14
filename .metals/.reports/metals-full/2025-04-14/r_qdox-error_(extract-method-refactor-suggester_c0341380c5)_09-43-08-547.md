error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1109.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1109.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1109.java
text:
```scala
I@@ndexWriterConfig conf = newIndexWriterConfig(new KeywordAnalyzer()); // use keyword analyzer we rely on the stored field holding the exact term.

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

package org.elasticsearch.common.lucene.index;

import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Lists;
import com.carrotsearch.ant.tasks.junit4.dependencies.com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.TermsFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IOUtils;
import org.elasticsearch.common.lucene.search.Queries;
import org.elasticsearch.common.util.BigArrays;
import org.elasticsearch.test.ElasticsearchLuceneTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static com.carrotsearch.randomizedtesting.RandomizedTest.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 */
public class FreqTermsEnumTests extends ElasticsearchLuceneTestCase {

    private String[] terms;
    private IndexWriter iw;
    private IndexReader reader;
    private Map<String, FreqHolder> referenceAll;
    private Map<String, FreqHolder> referenceNotDeleted;
    private Map<String, FreqHolder> referenceFilter;
    private Filter filter;

    static class FreqHolder {
        int docFreq;
        long totalTermFreq;
    }


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        referenceAll = Maps.newHashMap();
        referenceNotDeleted = Maps.newHashMap();
        referenceFilter = Maps.newHashMap();

        Directory dir = newDirectory();
        IndexWriterConfig conf = newIndexWriterConfig(TEST_VERSION_CURRENT, new KeywordAnalyzer()); // use keyword analyzer we rely on the stored field holding the exact term.
        if (frequently()) {
            // we don't want to do any merges, so we won't expunge deletes
            conf.setMergePolicy(NoMergePolicy.INSTANCE);
        }

        iw = new IndexWriter(dir, conf);
        terms = new String[scaledRandomIntBetween(10, 300)];
        for (int i = 0; i < terms.length; i++) {
            terms[i] = randomAsciiOfLength(5);
        }

        int numberOfDocs = scaledRandomIntBetween(30, 300);
        Document[] docs = new Document[numberOfDocs];
        for (int i = 0; i < numberOfDocs; i++) {
            Document doc = new Document();
            doc.add(new StringField("id", Integer.toString(i), Field.Store.YES));
            docs[i] = doc;
            for (String term : terms) {
                if (randomBoolean()) {
                    continue;
                }
                int freq = randomIntBetween(1, 3);
                for (int j = 0; j < freq; j++) {
                    doc.add(new TextField("field", term, Field.Store.YES));
                }
            }
        }

        // add all docs

        for (int i = 0; i < docs.length; i++) {
            Document doc = docs[i];
            iw.addDocument(doc);
            if (rarely()) {
                iw.commit();
            }
        }

        Set<String> deletedIds = Sets.newHashSet();
        for (int i = 0; i < docs.length; i++) {
            Document doc = docs[i];
            if (randomInt(5) == 2) {
                Term idTerm = new Term("id", doc.getField("id").stringValue());
                deletedIds.add(idTerm.text());
                iw.deleteDocuments(idTerm);
            }
        }

        for (String term : terms) {
            referenceAll.put(term, new FreqHolder());
            referenceFilter.put(term, new FreqHolder());
            referenceNotDeleted.put(term, new FreqHolder());
        }

        // now go over each doc, build the relevant references and filter
        reader = DirectoryReader.open(iw, true);
        List<Term> filterTerms = Lists.newArrayList();
        for (int docId = 0; docId < reader.maxDoc(); docId++) {
            Document doc = reader.document(docId);
            addFreqs(doc, referenceAll);
            if (!deletedIds.contains(doc.getField("id").stringValue())) {
                addFreqs(doc, referenceNotDeleted);
                if (randomBoolean()) {
                    filterTerms.add(new Term("id", doc.getField("id").stringValue()));
                    addFreqs(doc, referenceFilter);
                }
            }
        }
        filter = new TermsFilter(filterTerms);
    }

    private void addFreqs(Document doc, Map<String, FreqHolder> reference) {
        Set<String> addedDocFreq = Sets.newHashSet();
        for (IndexableField field : doc.getFields("field")) {
            String term = field.stringValue();
            FreqHolder freqHolder = reference.get(term);
            if (!addedDocFreq.contains(term)) {
                freqHolder.docFreq++;
                addedDocFreq.add(term);
            }
            freqHolder.totalTermFreq++;
        }
    }

    @After
    @Override
    public void tearDown() throws Exception {
        IOUtils.close(reader, iw, iw.getDirectory());
        super.tearDown();
    }

    @Test
    public void testAllFreqs() throws Exception {
        assertAgainstReference(true, true, null, referenceAll);
        assertAgainstReference(true, false, null, referenceAll);
        assertAgainstReference(false, true, null, referenceAll);
    }

    @Test
    public void testNonDeletedFreqs() throws Exception {
        assertAgainstReference(true, true, Queries.MATCH_ALL_FILTER, referenceNotDeleted);
        assertAgainstReference(true, false, Queries.MATCH_ALL_FILTER, referenceNotDeleted);
        assertAgainstReference(false, true, Queries.MATCH_ALL_FILTER, referenceNotDeleted);
    }

    @Test
    public void testFilterFreqs() throws Exception {
        assertAgainstReference(true, true, filter, referenceFilter);
        assertAgainstReference(true, false, filter, referenceFilter);
        assertAgainstReference(false, true, filter, referenceFilter);
    }

    private void assertAgainstReference(boolean docFreq, boolean totalTermFreq, Filter filter, Map<String, FreqHolder> reference) throws Exception {
        FreqTermsEnum freqTermsEnum = new FreqTermsEnum(reader, "field", docFreq, totalTermFreq, filter, BigArrays.NON_RECYCLING_INSTANCE);
        assertAgainstReference(freqTermsEnum, reference, docFreq, totalTermFreq);
    }

    private void assertAgainstReference(FreqTermsEnum termsEnum, Map<String, FreqHolder> reference, boolean docFreq, boolean totalTermFreq) throws Exception {
        int cycles = randomIntBetween(1, 5);
        for (int i = 0; i < cycles; i++) {
            List<String> terms = Lists.newArrayList(Arrays.asList(this.terms));

           Collections.shuffle(terms, getRandom());
            for (String term : terms) {
                if (!termsEnum.seekExact(new BytesRef(term))) {
                    assertThat("term : " + term, reference.get(term).docFreq, is(0));
                    continue;
                }
                if (docFreq) {
                    assertThat("cycle " + i + ", term " + term + ", docFreq", termsEnum.docFreq(), equalTo(reference.get(term).docFreq));
                }
                if (totalTermFreq) {
                    assertThat("cycle " + i + ", term " + term + ", totalTermFreq", termsEnum.totalTermFreq(), equalTo(reference.get(term).totalTermFreq));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1109.java