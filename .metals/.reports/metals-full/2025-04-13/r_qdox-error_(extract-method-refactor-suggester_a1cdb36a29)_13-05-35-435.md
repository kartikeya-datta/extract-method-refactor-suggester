error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2901.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2901.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2901.java
text:
```scala
I@@ndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_42, new WhitespaceAnalyzer(Version.LUCENE_42)));

package org.apache.mahout.utils.vectors.lucene;


import com.google.common.io.Closeables;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.apache.mahout.utils.MahoutTestCase;
import org.junit.Test;

import java.io.IOException;

/**
 *
 *
 **/
public class CachedTermInfoTest extends MahoutTestCase {
  private RAMDirectory directory;
  private static final String[] DOCS = {
          "a a b b c c",
          "a b a b a b a b",
          "a b a",
          "a",
          "b",
          "a",
          "a"
  };

  private static final String[] DOCS2 = {
          "d d d d",
          "e e e e",
          "d e d e",
          "d",
          "e",
          "d",
          "e"
  };

  @Override
  public void setUp() throws Exception {
    super.setUp();
    directory = new RAMDirectory();
    directory = createTestIndex(Field.TermVector.NO, directory, true, 0);
  }

  @Test
  public void test() throws Exception {
    IndexReader reader = DirectoryReader.open(directory);
    CachedTermInfo cti = new CachedTermInfo(reader, "content", 0, 100);
    assertEquals(3, cti.totalTerms("content"));
    assertNotNull(cti.getTermEntry("content", "a"));
    assertNull(cti.getTermEntry("content", "e"));
    //minDf
    cti = new CachedTermInfo(reader, "content", 3, 100);
    assertEquals(2, cti.totalTerms("content"));
    assertNotNull(cti.getTermEntry("content", "a"));
    assertNull(cti.getTermEntry("content", "c"));
    //maxDFPercent, a is in 6 of 7 docs: numDocs * maxDfPercent / 100 < 6 to exclude, 85% should suffice to exclude a
    cti = new CachedTermInfo(reader, "content", 0, 85);
    assertEquals(2, cti.totalTerms("content"));
    assertNotNull(cti.getTermEntry("content", "b"));
    assertNotNull(cti.getTermEntry("content", "c"));
    assertNull(cti.getTermEntry("content", "a"));


  }

  static RAMDirectory createTestIndex(Field.TermVector termVector,
                                      RAMDirectory directory,
                                      boolean createNew,
                                      int startingId) throws IOException {
    IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_41, new WhitespaceAnalyzer(Version.LUCENE_41)));

    try {
      for (int i = 0; i < DOCS.length; i++) {
        Document doc = new Document();
        Field id = new StringField("id", "doc_" + (i + startingId), Field.Store.YES);
        doc.add(id);
        //Store both position and offset information
        //Says it is deprecated, but doesn't seem to offer an alternative that supports term vectors...
        Field text = new Field("content", DOCS[i], Field.Store.NO, Field.Index.ANALYZED, termVector);
        doc.add(text);
        Field text2 = new Field("content2", DOCS2[i], Field.Store.NO, Field.Index.ANALYZED, termVector);
        doc.add(text2);
        writer.addDocument(doc);
      }
    } finally {
      Closeables.closeQuietly(writer);
    }
    return directory;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2901.java