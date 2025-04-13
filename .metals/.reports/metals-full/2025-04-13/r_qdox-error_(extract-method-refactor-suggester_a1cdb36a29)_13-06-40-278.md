error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4311.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4311.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4311.java
text:
```scala
d@@oc.add(new Field("_source", builder.bytes().array(), builder.bytes().arrayOffset(), builder.bytes().length()));

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
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

package org.elasticsearch.benchmark.compress;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.elasticsearch.common.compress.CompressedDirectory;
import org.elasticsearch.common.compress.lzf.LZFCompressor;
import org.elasticsearch.common.compress.snappy.xerial.XerialSnappyCompressor;
import org.elasticsearch.common.io.FileSystemUtils;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.File;

/**
 */
public class LuceneCompressionBenchmark {

    public static void main(String[] args) throws Exception {
        final long MAX_SIZE = ByteSizeValue.parseBytesSizeValue("50mb").bytes();
        final boolean WITH_TV = true;

        File testFile = new File("target/test/compress/lucene");
        FileSystemUtils.deleteRecursively(testFile);
        testFile.mkdirs();

        FSDirectory uncompressedDir = new NIOFSDirectory(new File(testFile, "uncompressed"));
        IndexWriter uncompressedWriter = new IndexWriter(uncompressedDir, new IndexWriterConfig(Lucene.VERSION, Lucene.STANDARD_ANALYZER));

        Directory compressedLzfDir = new CompressedDirectory(new NIOFSDirectory(new File(testFile, "compressed_lzf")), new LZFCompressor(), false, "fdt", "tvf");
        IndexWriter compressedLzfWriter = new IndexWriter(compressedLzfDir, new IndexWriterConfig(Lucene.VERSION, Lucene.STANDARD_ANALYZER));

        Directory compressedSnappyDir = new CompressedDirectory(new NIOFSDirectory(new File(testFile, "compressed_snappy")), new XerialSnappyCompressor(), false, "fdt", "tvf");
        IndexWriter compressedSnappyWriter = new IndexWriter(compressedSnappyDir, new IndexWriterConfig(Lucene.VERSION, Lucene.STANDARD_ANALYZER));

        System.out.println("feeding data...");
        TestData testData = new TestData();
        while (testData.next() && testData.getTotalSize() < MAX_SIZE) {
            // json
            XContentBuilder builder = XContentFactory.jsonBuilder();
            testData.current(builder);
            builder.close();
            Document doc = new Document();
            doc.add(new Field("_source", builder.underlyingBytes(), 0, builder.underlyingBytesLength()));
            if (WITH_TV) {
                Field field = new Field("text", builder.string(), Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
                doc.add(field);
            }
            uncompressedWriter.addDocument(doc);
            compressedLzfWriter.addDocument(doc);
            compressedSnappyWriter.addDocument(doc);
        }
        System.out.println("optimizing...");
        uncompressedWriter.forceMerge(1);
        compressedLzfWriter.forceMerge(1);
        compressedSnappyWriter.forceMerge(1);
        uncompressedWriter.waitForMerges();
        compressedLzfWriter.waitForMerges();
        compressedSnappyWriter.waitForMerges();

        System.out.println("done");
        uncompressedWriter.close();
        compressedLzfWriter.close();
        compressedSnappyWriter.close();

        compressedLzfDir.close();
        compressedSnappyDir.close();
        uncompressedDir.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4311.java