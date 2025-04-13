error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6718.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6718.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6718.java
text:
```scala
v@@ersionedMap = new ConcurrentVersionedMapLong();

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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

package org.elasticsearch.util.lucene.versioned;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.store.RAMDirectory;
import org.elasticsearch.util.lucene.Lucene;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.elasticsearch.util.lucene.DocumentBuilder.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author kimchy (Shay Banon)
 */
public class VersionedIndexReaderTests {

    private RAMDirectory dir;
    private IndexReader indexReader;
    private IndexWriter indexWriter;
    private VersionedMap versionedMap;

    @BeforeTest public void setUp() throws Exception {
        versionedMap = new NonBlockingVersionedMap();
        dir = new RAMDirectory();
        indexWriter = new IndexWriter(dir, Lucene.STANDARD_ANALYZER, true, IndexWriter.MaxFieldLength.UNLIMITED);
        indexWriter.addDocument(doc().add(field("value", "0")).build());
        indexWriter.addDocument(doc().add(field("value", "1")).build());
        indexWriter.addDocument(doc().add(field("value", "2")).build());
        indexWriter.addDocument(doc().add(field("value", "3")).build());
        indexWriter.commit();
        indexReader = IndexReader.open(dir, true);
    }

    @AfterTest public void tearDown() throws Exception {
        indexWriter.close();
        indexReader.close();
        dir.close();
    }

    @Test public void verifyExpected() throws Exception {
        TermDocs termDocs;
        Document doc = indexReader.document(0);

        assertThat(doc.getField("value").stringValue(), equalTo("0"));
        termDocs = indexReader.termDocs(new Term("value", "0"));
        assertThat(termDocs.next(), equalTo(true));
        assertThat(termDocs.next(), equalTo(false));

        doc = indexReader.document(1);
        assertThat(doc.getField("value").stringValue(), equalTo("1"));
        termDocs = indexReader.termDocs(new Term("value", "1"));
        assertThat(termDocs.next(), equalTo(true));
        assertThat(termDocs.next(), equalTo(false));

        doc = indexReader.document(2);
        assertThat(doc.getField("value").stringValue(), equalTo("2"));
        termDocs = indexReader.termDocs(new Term("value", "2"));
        assertThat(termDocs.next(), equalTo(true));
        assertThat(termDocs.next(), equalTo(false));

        doc = indexReader.document(3);
        assertThat(doc.getField("value").stringValue(), equalTo("3"));
        termDocs = indexReader.termDocs(new Term("value", "3"));
        assertThat(termDocs.next(), equalTo(true));
        assertThat(termDocs.next(), equalTo(false));
    }

    @Test public void testSimple() throws Exception {
        TermDocs termDocs;
        // open a versioned index reader in version 0
        VersionedIndexReader versionedIndexReader = new VersionedIndexReader(indexReader, 0, versionedMap);
        // delete doc 0 in version 1
        versionedMap.putVersion(0, 1);

        // we can see doc 0 still (versioned reader is on version 0)
        termDocs = versionedIndexReader.termDocs(new Term("value", "0"));
        assertThat(termDocs.next(), equalTo(true));
        assertThat(termDocs.next(), equalTo(false));
        // make sure we see doc 1, it was never deleted
        termDocs = versionedIndexReader.termDocs(new Term("value", "1"));
        assertThat(termDocs.next(), equalTo(true));
        assertThat(termDocs.next(), equalTo(false));

        // delete doc 1 in version 2, we still
        versionedMap.putVersion(1, 2);
        // we can see doc 0 still (versioned reader is on version 0)
        termDocs = versionedIndexReader.termDocs(new Term("value", "0"));
        assertThat(termDocs.next(), equalTo(true));
        // we can see doc 1 still (versioned reader is on version 0)
        termDocs = versionedIndexReader.termDocs(new Term("value", "1"));
        assertThat(termDocs.next(), equalTo(true));

        // move the versioned reader to 1
        versionedIndexReader = new VersionedIndexReader(indexReader, 1, versionedMap);
        // we now can't see the deleted version 0
        termDocs = versionedIndexReader.termDocs(new Term("value", "0"));
        assertThat(termDocs.next(), equalTo(false));
        // we can still see deleted version 1
        termDocs = versionedIndexReader.termDocs(new Term("value", "1"));
        assertThat(termDocs.next(), equalTo(true));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6718.java