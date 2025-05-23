error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7590.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7590.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7590.java
text:
```scala
d@@oc.add(new Field("_uid", "1", UidFieldMapper.Defaults.FIELD_TYPE));

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.test.unit.common.lucene.uid;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.store.RAMDirectory;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.lucene.uid.UidField;
import org.elasticsearch.index.mapper.internal.UidFieldMapper;
import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

/**
 *
 */
public class UidFieldTests {

    @Test
    public void testUidField() throws Exception {
        IndexWriter writer = new IndexWriter(new RAMDirectory(), new IndexWriterConfig(Lucene.VERSION, Lucene.STANDARD_ANALYZER));

        DirectoryReader directoryReader = DirectoryReader.open(writer, true);
        AtomicReader atomicReader = SlowCompositeReaderWrapper.wrap(directoryReader);
        MatcherAssert.assertThat(UidField.loadVersion(atomicReader.getContext(), new Term("_uid", "1")), equalTo(-1l));

        Document doc = new Document();
        doc.add(new Field("_uid", "1", UidFieldMapper.Defaults.UID_FIELD_TYPE));
        writer.addDocument(doc);
        directoryReader = DirectoryReader.openIfChanged(directoryReader);
        atomicReader = SlowCompositeReaderWrapper.wrap(directoryReader);
        assertThat(UidField.loadVersion(atomicReader.getContext(), new Term("_uid", "1")), equalTo(-2l));
        assertThat(UidField.loadDocIdAndVersion(atomicReader.getContext(), new Term("_uid", "1")).version, equalTo(-2l));

        doc = new Document();
        doc.add(new UidField("_uid", "1", 1));
        writer.updateDocument(new Term("_uid", "1"), doc);
        directoryReader = DirectoryReader.openIfChanged(directoryReader);
        atomicReader = SlowCompositeReaderWrapper.wrap(directoryReader);
        assertThat(UidField.loadVersion(atomicReader.getContext(), new Term("_uid", "1")), equalTo(1l));
        assertThat(UidField.loadDocIdAndVersion(atomicReader.getContext(), new Term("_uid", "1")).version, equalTo(1l));

        doc = new Document();
        UidField uid = new UidField("_uid", "1", 2);
        doc.add(uid);
        writer.updateDocument(new Term("_uid", "1"), doc);
        directoryReader = DirectoryReader.openIfChanged(directoryReader);
        atomicReader = SlowCompositeReaderWrapper.wrap(directoryReader);
        assertThat(UidField.loadVersion(atomicReader.getContext(), new Term("_uid", "1")), equalTo(2l));
        assertThat(UidField.loadDocIdAndVersion(atomicReader.getContext(), new Term("_uid", "1")).version, equalTo(2l));

        // test reuse of uid field
        doc = new Document();
        uid.version(3);
        doc.add(uid);
        writer.updateDocument(new Term("_uid", "1"), doc);
        directoryReader = DirectoryReader.openIfChanged(directoryReader);
        atomicReader = SlowCompositeReaderWrapper.wrap(directoryReader);
        assertThat(UidField.loadVersion(atomicReader.getContext(), new Term("_uid", "1")), equalTo(3l));
        assertThat(UidField.loadDocIdAndVersion(atomicReader.getContext(), new Term("_uid", "1")).version, equalTo(3l));

        writer.deleteDocuments(new Term("_uid", "1"));
        directoryReader = DirectoryReader.openIfChanged(directoryReader);
        atomicReader = SlowCompositeReaderWrapper.wrap(directoryReader);
        assertThat(UidField.loadVersion(atomicReader.getContext(), new Term("_uid", "1")), equalTo(-1l));
        assertThat(UidField.loadDocIdAndVersion(atomicReader.getContext(), new Term("_uid", "1")), nullValue());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7590.java