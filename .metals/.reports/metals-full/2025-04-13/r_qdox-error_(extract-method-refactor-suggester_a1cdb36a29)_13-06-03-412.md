error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2902.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2902.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2902.java
text:
```scala
I@@ndexWriter writer = new IndexWriter( directory, new IndexWriterConfig(Version.LUCENE_42,new StandardAnalyzer(Version.LUCENE_42)));

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

import com.google.common.io.Closeables;
import java.io.IOException;
import java.util.Iterator;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.utils.MahoutTestCase;
import org.apache.mahout.utils.vectors.TermInfo;
import org.apache.mahout.vectorizer.TFIDF;
import org.apache.mahout.vectorizer.Weight;
import org.junit.Test;

public final class LuceneIterableTest extends MahoutTestCase {

  private static final String [] DOCS = {
      "The quick red fox jumped over the lazy brown dogs.",
      "Mary had a little lamb whose fleece was white as snow.",
      "Moby Dick is a story of a whale and a man obsessed.",
      "The robber wore a black fleece jacket and a baseball cap.",
      "The English Springer Spaniel is the best of all dogs."
  };

  private RAMDirectory directory;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    directory = createTestIndex(Field.TermVector.YES);
  }

  @Test
  public void testIterable() throws Exception {
    IndexReader reader = DirectoryReader.open(directory);
    Weight weight = new TFIDF();
    TermInfo termInfo = new CachedTermInfo(reader, "content", 1, 100);
    LuceneIterable iterable = new LuceneIterable(reader, "id", "content", termInfo,weight);

    //TODO: do something more meaningful here
    for (Vector vector : iterable) {
      assertNotNull(vector);
      assertTrue("vector is not an instanceof " + NamedVector.class, vector instanceof NamedVector);
      assertTrue("vector Size: " + vector.size() + " is not greater than: " + 0, vector.size() > 0);
      assertTrue(((NamedVector) vector).getName().startsWith("doc_"));
    }

    iterable = new LuceneIterable(reader, "id", "content", termInfo,weight, 3);

    //TODO: do something more meaningful here
    for (Vector vector : iterable) {
      assertNotNull(vector);
      assertTrue("vector is not an instanceof " + NamedVector.class, vector instanceof NamedVector);
      assertTrue("vector Size: " + vector.size() + " is not greater than: " + 0, vector.size() > 0);
      assertTrue(((NamedVector) vector).getName().startsWith("doc_"));
    }

  }

  @Test(expected = IllegalStateException.class)
  public void testIterableNoTermVectors() throws IOException {
    RAMDirectory directory = createTestIndex(Field.TermVector.NO);
    IndexReader reader = DirectoryReader.open(directory);
    
    
    Weight weight = new TFIDF();
    TermInfo termInfo = new CachedTermInfo(reader, "content", 1, 100);
    LuceneIterable iterable = new LuceneIterable(reader, "id", "content",  termInfo,weight);

    Iterator<Vector> iterator = iterable.iterator();
    iterator.hasNext();
    iterator.next();
  }

  @Test
  public void testIterableSomeNoiseTermVectors() throws IOException {
    //get noise vectors
    RAMDirectory directory = createTestIndex(Field.TermVector.YES, new RAMDirectory(), true, 0);
    //get real vectors
    createTestIndex(Field.TermVector.NO, directory, false, 5);
    IndexReader reader = DirectoryReader.open(directory);

    Weight weight = new TFIDF();
    TermInfo termInfo = new CachedTermInfo(reader, "content", 1, 100);
    
    boolean exceptionThrown;
    //0 percent tolerance
    LuceneIterable iterable = new LuceneIterable(reader, "id", "content", termInfo,weight);
    try {
      for (Object a : iterable) {
      }
      exceptionThrown = false;
    }
    catch(IllegalStateException ise) {
        exceptionThrown = true;
    }
    assertTrue(exceptionThrown);
    
    //100 percent tolerance
    iterable = new LuceneIterable(reader, "id", "content", termInfo,weight, -1, 1.0);
    try {
      for (Object a : iterable) {
      }
      exceptionThrown = false;
    }
    catch(IllegalStateException ise) {
        exceptionThrown = true;
    }
    assertFalse(exceptionThrown);
    
    //50 percent tolerance
    iterable = new LuceneIterable(reader, "id", "content", termInfo,weight, -1, 0.5);
    Iterator<Vector> iterator = iterable.iterator();
    iterator.next();
    iterator.next();
    iterator.next();
    iterator.next();
    iterator.next();

    try {
        while (iterator.hasNext()) {
            iterator.next();
        }
        exceptionThrown = false;
    }
    catch(IllegalStateException ise) {
        exceptionThrown = true;
    }
    assertTrue(exceptionThrown);
  }
  
  static RAMDirectory createTestIndex(Field.TermVector termVector) throws IOException {
      return createTestIndex(termVector, new RAMDirectory(), true, 0);
  }
  
  static RAMDirectory createTestIndex(Field.TermVector termVector,
                                              RAMDirectory directory,
                                              boolean createNew,
                                              int startingId) throws IOException {
    IndexWriter writer = new IndexWriter( directory, new IndexWriterConfig(Version.LUCENE_41,new StandardAnalyzer(Version.LUCENE_41)));
        
    try {
      for (int i = 0; i < DOCS.length; i++) {
        Document doc = new Document();
        Field id = new StringField("id", "doc_" + (i + startingId), Field.Store.YES);
        doc.add(id);
        //Store both position and offset information
        //Says it is deprecated, but doesn't seem to offer an alternative that supports term vectors...
        Field text = new Field("content", DOCS[i], Field.Store.NO, Field.Index.ANALYZED, termVector);
        doc.add(text);
        Field text2 = new Field("content2", DOCS[i], Field.Store.NO, Field.Index.ANALYZED, termVector);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2902.java