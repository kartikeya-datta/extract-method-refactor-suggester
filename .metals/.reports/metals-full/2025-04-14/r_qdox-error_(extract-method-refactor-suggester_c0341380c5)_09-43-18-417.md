error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1403.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1403.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1403.java
text:
```scala
c@@onf = getConfiguration();

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

package org.apache.mahout.utils.vectors.lucene;

import com.google.common.collect.Sets;
import com.google.common.io.Closeables;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.apache.mahout.common.MahoutTestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class DriverTest extends MahoutTestCase {

  private File indexDir;
  private File outputDir;
  private Configuration conf;

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    indexDir = getTestTempDir("intermediate");
    indexDir.delete();
    outputDir = getTestTempDir("output");
    outputDir.delete();

    conf = new Configuration();
  }

  private Document asDocument(String line) {
    Document doc = new Document();
    doc.add(new TextFieldWithTermVectors("text", line));
    return doc;
  }

  static class TextFieldWithTermVectors extends Field {

    public static final FieldType TYPE = new FieldType();

    static {
      TYPE.setIndexed(true);
      TYPE.setOmitNorms(true);
      TYPE.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS);
      TYPE.setStored(true);
      TYPE.setTokenized(true);
      TYPE.setStoreTermVectors(true);
      TYPE.freeze();
    }

    public TextFieldWithTermVectors(String name, String value) {
      super(name, value, TYPE);
    }
  }

  @Test
  public void sequenceFileDictionary() throws IOException {

    Directory index = new SimpleFSDirectory(indexDir);
    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43, analyzer);
    final IndexWriter writer = new IndexWriter(index, config);

    try {
      writer.addDocument(asDocument("One Ring to rule them all"));
      writer.addDocument(asDocument("One Ring to find them,"));
      writer.addDocument(asDocument("One Ring to bring them all"));
      writer.addDocument(asDocument("and in the darkness bind them"));

    } finally {
      writer.close(true);
    }

    File seqDict = new File(outputDir, "dict.seq");

    Driver.main(new String[] {
        "--dir", indexDir.getAbsolutePath(),
        "--output", new File(outputDir, "out").getAbsolutePath(),
        "--field", "text",
        "--dictOut", new File(outputDir, "dict.txt").getAbsolutePath(),
        "--seqDictOut", seqDict.getAbsolutePath(),
    });

    SequenceFile.Reader reader = null;
    Set<String> indexTerms = Sets.newHashSet();
    try {
      reader = new SequenceFile.Reader(FileSystem.getLocal(conf), new Path(seqDict.getAbsolutePath()), conf);
      Text term = new Text();
      IntWritable termIndex = new IntWritable();

      while (reader.next(term, termIndex)) {
        indexTerms.add(term.toString());
      }
    } finally {
      Closeables.close(reader, true);
    }

    Set<String> expectedIndexTerms = Sets.newHashSet("all", "bind", "bring", "darkness", "find", "one", "ring", "rule");

    // should contain the same terms as expected
    assertEquals(expectedIndexTerms.size(), Sets.union(expectedIndexTerms, indexTerms).size());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1403.java