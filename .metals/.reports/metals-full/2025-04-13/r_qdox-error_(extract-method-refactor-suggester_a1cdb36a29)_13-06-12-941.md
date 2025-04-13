error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1399.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1399.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1399.java
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

package org.apache.mahout.text;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.lucene.search.TermQuery;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.iterator.sequencefile.PathFilters;
import org.apache.mahout.common.iterator.sequencefile.PathType;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirIterator;
import org.apache.mahout.text.doc.SingleFieldDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;

public class SequenceFilesFromLuceneStorageDriverTest extends AbstractLuceneStorageTest {

  private SequenceFilesFromLuceneStorageDriver driver;
  private LuceneStorageConfiguration lucene2SeqConf;
  private String idField;
  private List<String> fields;
  private Path seqFilesOutputPath;
  private Configuration conf;

  @Before
  public void before() throws Exception {
    conf = new Configuration();
    conf.set("io.serializations", "org.apache.hadoop.io.serializer.JavaSerialization,"
      + "org.apache.hadoop.io.serializer.WritableSerialization");

    seqFilesOutputPath = new Path(getTestTempDirPath(), "seqfiles");
    idField = "id";
    fields = asList("field");

    driver = new SequenceFilesFromLuceneStorageDriver() {
      @Override
      public LuceneStorageConfiguration newLucene2SeqConfiguration(Configuration configuration, List<Path> indexPaths, Path seqPath, String idField, List<String> fields) {
        lucene2SeqConf = new LuceneStorageConfiguration(configuration, indexPaths, seqPath, idField, fields);
        return lucene2SeqConf;
      }
    };
    commitDocuments(getDirectory(getIndexPath1AsFile()), new SingleFieldDocument("1", "Mahout is cool"));
    commitDocuments(getDirectory(getIndexPath1AsFile()), new SingleFieldDocument("2", "Mahout is cool"));
  }

  @After
  public void after() throws IOException {
    HadoopUtil.delete(conf, seqFilesOutputPath);
    HadoopUtil.delete(conf, getIndexPath1());
  }

  @Test
  public void testNewLucene2SeqConfiguration() {
    lucene2SeqConf = driver.newLucene2SeqConfiguration(conf,
      asList(new Path(getIndexPath1().toString())),
      seqFilesOutputPath,
      idField,
      fields);

    assertEquals(conf, lucene2SeqConf.getConfiguration());
    assertEquals(asList(getIndexPath1()), lucene2SeqConf.getIndexPaths());
    assertEquals(seqFilesOutputPath, lucene2SeqConf.getSequenceFilesOutputPath());
    assertEquals(idField, lucene2SeqConf.getIdField());
    assertEquals(fields, lucene2SeqConf.getFields());
  }

  @Test
  public void testRun() throws Exception {
    String queryField = "queryfield";
    String queryTerm = "queryterm";
    String maxHits = "500";
    String field1 = "field1";
    String field2 = "field2";

    String[] args = {
      "-i", getIndexPath1AsFile().toString(),
      "-o", seqFilesOutputPath.toString(),
      "-id", idField,
      "-f", field1 + "," + field2,
      "-q", queryField + ":" + queryTerm,
      "-n", maxHits,
      "-xm", "sequential"
    };

    driver.setConf(conf);
    driver.run(args);
    assertEquals(1, lucene2SeqConf.getIndexPaths().size());
    assertEquals(getIndexPath1().toUri().getPath(), lucene2SeqConf.getIndexPaths().get(0).toUri().getPath());
    assertEquals(seqFilesOutputPath, lucene2SeqConf.getSequenceFilesOutputPath());
    assertEquals(idField, lucene2SeqConf.getIdField());
    assertEquals(asList(field1, field2), lucene2SeqConf.getFields());

    assertTrue(lucene2SeqConf.getQuery() instanceof TermQuery);
    assertEquals(queryField, ((TermQuery) lucene2SeqConf.getQuery()).getTerm().field());
    assertEquals(queryTerm, ((TermQuery) lucene2SeqConf.getQuery()).getTerm().text());
    assertEquals(new Integer(maxHits), (Integer) lucene2SeqConf.getMaxHits());
  }

  @Test
  public void testRunOptionalArguments() throws Exception {
    String[] args = {
      "-i", getIndexPath1AsFile().toString(),
      "-o", seqFilesOutputPath.toString(),
      "-id", idField,
      "-f", StringUtils.join(fields, SequenceFilesFromLuceneStorageDriver.SEPARATOR_FIELDS)
    };

    driver.setConf(conf);
    driver.run(args);

    assertEquals(1, lucene2SeqConf.getIndexPaths().size());
    assertEquals(getIndexPath1().toUri().getPath(), lucene2SeqConf.getIndexPaths().get(0).toUri().getPath());
    assertEquals(seqFilesOutputPath, lucene2SeqConf.getSequenceFilesOutputPath());
    assertEquals(idField, lucene2SeqConf.getIdField());
    assertEquals(fields, lucene2SeqConf.getFields());
    assertEquals(conf, lucene2SeqConf.getConfiguration());

    assertEquals(SequenceFilesFromLuceneStorageDriver.DEFAULT_QUERY, lucene2SeqConf.getQuery());
    assertEquals(SequenceFilesFromLuceneStorageDriver.DEFAULT_MAX_HITS, lucene2SeqConf.getMaxHits());
  }

  @Test
  public void testRunInvalidQuery() throws Exception {
    String[] args = {
      "-i", getIndexPath1AsFile().toString(),
      "-o", seqFilesOutputPath.toString(),
      "-id", idField,
      "-f", StringUtils.join(fields, SequenceFilesFromLuceneStorageDriver.SEPARATOR_FIELDS),
      "-q", "invalid:query"
    };

    driver.setConf(conf);
    driver.run(args);
    assertTrue(FileSystem.get(conf).exists(seqFilesOutputPath));
    //shouldn't be any real files in the seq files out path
    SequenceFileDirIterator<Writable, Writable> iter = new SequenceFileDirIterator<Writable, Writable>(seqFilesOutputPath, PathType.LIST, PathFilters.logsCRCFilter(), null, false, conf);
    assertFalse(iter.hasNext());

  }

  @Test
  public void testHelp() throws Exception {
    driver = new SequenceFilesFromLuceneStorageDriver();
    driver.run(new String[]{"--help"});
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1399.java