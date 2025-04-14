error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1443.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1443.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1443.java
text:
```scala
d@@estTaxWriter.addTaxonomy(srcTaxDir, map);

package org.apache.lucene.facet.example.merge;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.PayloadProcessorProvider;
import org.apache.lucene.store.Directory;

import org.apache.lucene.facet.example.ExampleUtils;
import org.apache.lucene.facet.index.FacetsPayloadProcessorProvider;
import org.apache.lucene.facet.index.params.DefaultFacetIndexingParams;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter.DiskOrdinalMap;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter.MemoryOrdinalMap;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter.OrdinalMap;

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

/**
 * @lucene.experimental
 */
public class TaxonomyMergeUtils {

  /**
   * Merges the given taxonomy and index directories. Note that this method
   * opens {@link DirectoryTaxonomyWriter} and {@link IndexWriter} on the
   * respective destination indexes. Therefore if you have a writer open on any
   * of them, it should be closed, or you should use
   * {@link #merge(Directory, Directory, IndexWriter, DirectoryTaxonomyWriter)}
   * instead.
   * 
   * @see #merge(Directory, Directory, IndexWriter, DirectoryTaxonomyWriter)
   */
  public static void merge(Directory srcIndexDir, Directory srcTaxDir,
                            Directory destIndexDir, Directory destTaxDir) throws IOException {
    IndexWriter destIndexWriter = new IndexWriter(destIndexDir,
        new IndexWriterConfig(ExampleUtils.EXAMPLE_VER, null));
    DirectoryTaxonomyWriter destTaxWriter = new DirectoryTaxonomyWriter(destTaxDir);
    merge(srcIndexDir, srcTaxDir, new MemoryOrdinalMap(), destIndexWriter, destTaxWriter);
    destTaxWriter.close();
    destIndexWriter.close();
  }

  /**
   * Merges the given taxonomy and index directories and commits the changes to
   * the given writers. This method uses {@link MemoryOrdinalMap} to store the
   * mapped ordinals. If you cannot afford the memory, you can use
   * {@link #merge(Directory, Directory, DirectoryTaxonomyWriter.OrdinalMap, IndexWriter, DirectoryTaxonomyWriter)}
   * by passing {@link DiskOrdinalMap}.
   * 
   * @see #merge(Directory, Directory, DirectoryTaxonomyWriter.OrdinalMap, IndexWriter, DirectoryTaxonomyWriter)
   */
  public static void merge(Directory srcIndexDir, Directory srcTaxDir,
                            IndexWriter destIndexWriter, 
                            DirectoryTaxonomyWriter destTaxWriter) throws IOException {
    merge(srcIndexDir, srcTaxDir, new MemoryOrdinalMap(), destIndexWriter, destTaxWriter);
  }
  
  /**
   * Merges the given taxonomy and index directories and commits the changes to
   * the given writers.
   */
  public static void merge(Directory srcIndexDir, Directory srcTaxDir,
                            OrdinalMap map, IndexWriter destIndexWriter,
                            DirectoryTaxonomyWriter destTaxWriter) throws IOException {
    // merge the taxonomies
    destTaxWriter.addTaxonomies(new Directory[] { srcTaxDir }, new OrdinalMap[] { map });

    PayloadProcessorProvider payloadProcessor = new FacetsPayloadProcessorProvider(
        srcIndexDir, map.getMap(), new DefaultFacetIndexingParams());
    destIndexWriter.setPayloadProcessorProvider(payloadProcessor);

    IndexReader reader = IndexReader.open(srcIndexDir);
    try {
      destIndexWriter.addIndexes(reader);
      
      // commit changes to taxonomy and index respectively.
      destTaxWriter.commit();
      destIndexWriter.commit();
    } finally {
      reader.close();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1443.java