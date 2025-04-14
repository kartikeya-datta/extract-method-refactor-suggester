error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2042.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2042.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2042.java
text:
```scala
D@@ocsEnum de = _TestUtil.docs(random(), reader, "foo", new BytesRef("test"), null, null, true);

package org.apache.lucene.index;

/*
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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;

/**
 * 
 * @lucene.experimental
 */
public class TestOmitPositions extends LuceneTestCase {

  public void testBasic() throws Exception {   
    Directory dir = newDirectory();
    RandomIndexWriter w = new RandomIndexWriter(random(), dir);
    Document doc = new Document();
    FieldType ft = new FieldType(TextField.TYPE_NOT_STORED);
    ft.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
    Field f = newField("foo", "this is a test test", ft);
    doc.add(f);
    for (int i = 0; i < 100; i++) {
      w.addDocument(doc);
    }
    
    IndexReader reader = w.getReader();
    w.close();
    
    assertNull(MultiFields.getTermPositionsEnum(reader, null, "foo", new BytesRef("test")));
    
    DocsEnum de = _TestUtil.docs(random(), reader, "foo", new BytesRef("test"), null, null, DocsEnum.FLAG_FREQS);
    while (de.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
      assertEquals(2, de.freq());
    }
    
    reader.close();
    dir.close();
  }
  
  // Tests whether the DocumentWriter correctly enable the
  // omitTermFreqAndPositions bit in the FieldInfo
  public void testPositions() throws Exception {
    Directory ram = newDirectory();
    Analyzer analyzer = new MockAnalyzer(random());
    IndexWriter writer = new IndexWriter(ram, newIndexWriterConfig( TEST_VERSION_CURRENT, analyzer));
    Document d = new Document();
        
    // f1,f2,f3: docs only
    FieldType ft = new FieldType(TextField.TYPE_NOT_STORED);
    ft.setIndexOptions(IndexOptions.DOCS_ONLY);
    
    Field f1 = newField("f1", "This field has docs only", ft);
    d.add(f1);
       
    Field f2 = newField("f2", "This field has docs only", ft);
    d.add(f2);
    
    Field f3 = newField("f3", "This field has docs only", ft);
    d.add(f3);

    FieldType ft2 = new FieldType(TextField.TYPE_NOT_STORED);
    ft2.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
    
    // f4,f5,f6 docs and freqs
    Field f4 = newField("f4", "This field has docs and freqs", ft2);
    d.add(f4);
       
    Field f5 = newField("f5", "This field has docs and freqs", ft2);
    d.add(f5);
    
    Field f6 = newField("f6", "This field has docs and freqs", ft2);
    d.add(f6);
    
    FieldType ft3 = new FieldType(TextField.TYPE_NOT_STORED);
    ft3.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
    
    // f7,f8,f9 docs/freqs/positions
    Field f7 = newField("f7", "This field has docs and freqs and positions", ft3);
    d.add(f7);
       
    Field f8 = newField("f8", "This field has docs and freqs and positions", ft3);
    d.add(f8);
    
    Field f9 = newField("f9", "This field has docs and freqs and positions", ft3);
    d.add(f9);
        
    writer.addDocument(d);
    writer.forceMerge(1);

    // now we add another document which has docs-only for f1, f4, f7, docs/freqs for f2, f5, f8, 
    // and docs/freqs/positions for f3, f6, f9
    d = new Document();
    
    // f1,f4,f7: docs only
    f1 = newField("f1", "This field has docs only", ft);
    d.add(f1);
    
    f4 = newField("f4", "This field has docs only", ft);
    d.add(f4);
    
    f7 = newField("f7", "This field has docs only", ft);
    d.add(f7);

    // f2, f5, f8: docs and freqs
    f2 = newField("f2", "This field has docs and freqs", ft2);
    d.add(f2);
    
    f5 = newField("f5", "This field has docs and freqs", ft2);
    d.add(f5);
    
    f8 = newField("f8", "This field has docs and freqs", ft2);
    d.add(f8);
    
    // f3, f6, f9: docs and freqs and positions
    f3 = newField("f3", "This field has docs and freqs and positions", ft3);
    d.add(f3);     
    
    f6 = newField("f6", "This field has docs and freqs and positions", ft3);
    d.add(f6);
    
    f9 = newField("f9", "This field has docs and freqs and positions", ft3);
    d.add(f9);
        
    writer.addDocument(d);

    // force merge
    writer.forceMerge(1);
    // flush
    writer.close();

    SegmentReader reader = getOnlySegmentReader(DirectoryReader.open(ram));
    FieldInfos fi = reader.getFieldInfos();
    // docs + docs = docs
    assertEquals(IndexOptions.DOCS_ONLY, fi.fieldInfo("f1").getIndexOptions());
    // docs + docs/freqs = docs
    assertEquals(IndexOptions.DOCS_ONLY, fi.fieldInfo("f2").getIndexOptions());
    // docs + docs/freqs/pos = docs
    assertEquals(IndexOptions.DOCS_ONLY, fi.fieldInfo("f3").getIndexOptions());
    // docs/freqs + docs = docs
    assertEquals(IndexOptions.DOCS_ONLY, fi.fieldInfo("f4").getIndexOptions());
    // docs/freqs + docs/freqs = docs/freqs
    assertEquals(IndexOptions.DOCS_AND_FREQS, fi.fieldInfo("f5").getIndexOptions());
    // docs/freqs + docs/freqs/pos = docs/freqs
    assertEquals(IndexOptions.DOCS_AND_FREQS, fi.fieldInfo("f6").getIndexOptions());
    // docs/freqs/pos + docs = docs
    assertEquals(IndexOptions.DOCS_ONLY, fi.fieldInfo("f7").getIndexOptions());
    // docs/freqs/pos + docs/freqs = docs/freqs
    assertEquals(IndexOptions.DOCS_AND_FREQS, fi.fieldInfo("f8").getIndexOptions());
    // docs/freqs/pos + docs/freqs/pos = docs/freqs/pos
    assertEquals(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS, fi.fieldInfo("f9").getIndexOptions());
    
    reader.close();
    ram.close();
  }
  
  private void assertNoPrx(Directory dir) throws Throwable {
    final String[] files = dir.listAll();
    for(int i=0;i<files.length;i++) {
      assertFalse(files[i].endsWith(".prx"));
      assertFalse(files[i].endsWith(".pos"));
    }
  }

  // Verifies no *.prx exists when all fields omit term positions:
  public void testNoPrxFile() throws Throwable {
    Directory ram = newDirectory();
    Analyzer analyzer = new MockAnalyzer(random());
    IndexWriter writer = new IndexWriter(ram, newIndexWriterConfig(
                                                                   TEST_VERSION_CURRENT, analyzer).setMaxBufferedDocs(3).setMergePolicy(newLogMergePolicy()));
    LogMergePolicy lmp = (LogMergePolicy) writer.getConfig().getMergePolicy();
    lmp.setMergeFactor(2);
    lmp.setUseCompoundFile(false);
    Document d = new Document();

    FieldType ft = new FieldType(TextField.TYPE_NOT_STORED);
    ft.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
    Field f1 = newField("f1", "This field has term freqs", ft);
    d.add(f1);

    for(int i=0;i<30;i++)
      writer.addDocument(d);

    writer.commit();

    assertNoPrx(ram);
    
    // now add some documents with positions, and check there is no prox after optimization
    d = new Document();
    f1 = newTextField("f1", "This field has positions", Field.Store.NO);
    d.add(f1);
    
    for(int i=0;i<30;i++)
      writer.addDocument(d);

    // force merge
    writer.forceMerge(1);
    // flush
    writer.close();

    assertNoPrx(ram);
    ram.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2042.java