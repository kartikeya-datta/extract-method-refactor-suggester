error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3189.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3189.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3189.java
text:
```scala
w@@riter.shutdown();

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
package org.apache.lucene.document;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.store.*;
import org.apache.lucene.document.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;

import org.junit.After;
import org.junit.Before;

public class TestLazyDocument extends LuceneTestCase {

  public final int NUM_DOCS = atLeast(10);
  public final String[] FIELDS = new String[] 
    { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k" };
  public final int NUM_VALUES = atLeast(100);

  public Directory dir = newDirectory();
  
  @After
  public void removeIndex() {
    if (null != dir) {
      try { 
        dir.close(); 
        dir = null;
      } catch (Exception e) { /* NOOP */ }
    }
  }

  @Before
  public void createIndex() throws Exception {

    Analyzer analyzer = new MockAnalyzer(random());
    IndexWriter writer = new IndexWriter
      (dir, newIndexWriterConfig(TEST_VERSION_CURRENT, analyzer));
    try {
      for (int docid = 0; docid < NUM_DOCS; docid++) {
        Document d = new Document();
        d.add(newStringField("docid", ""+docid, Field.Store.YES));
        d.add(newStringField("never_load", "fail", Field.Store.YES));
        for (String f : FIELDS) {
          for (int val = 0; val < NUM_VALUES; val++) {
            d.add(newStringField(f, docid+"_"+f+"_"+val, Field.Store.YES));
          }
        }
        d.add(newStringField("load_later", "yes", Field.Store.YES));
        writer.addDocument(d);
      }
    } finally {
      writer.close();
    }
  }

  public void testLazy() throws Exception {
    final int id = random().nextInt(NUM_DOCS);
    IndexReader reader = DirectoryReader.open(dir);
    try {
      Query q = new TermQuery(new Term("docid", ""+id));
      IndexSearcher searcher = newSearcher(reader);
      ScoreDoc[] hits = searcher.search(q, 100).scoreDocs;
      assertEquals("Too many docs", 1, hits.length);
      LazyTestingStoredFieldVisitor visitor 
        = new LazyTestingStoredFieldVisitor(new LazyDocument(reader, hits[0].doc),
                                            FIELDS);
      reader.document(hits[0].doc, visitor);
      StoredDocument d = visitor.doc;

      int numFieldValues = 0;
      Map<String,Integer> fieldValueCounts = new HashMap<>();

      // at this point, all FIELDS should be Lazy and unrealized
      for (StorableField f : d) {
        numFieldValues++;   
        if (f.name().equals("never_load")) {
          fail("never_load was loaded");
        }
        if (f.name().equals("load_later")) {
          fail("load_later was loaded on first pass");
        }
        if (f.name().equals("docid")) {
          assertFalse(f.name(), f instanceof LazyDocument.LazyField);
        } else {
          int count = fieldValueCounts.containsKey(f.name()) ?
            fieldValueCounts.get(f.name()) : 0;
          count++;
          fieldValueCounts.put(f.name(), count);
          assertTrue(f.name() + " is " + f.getClass(),
                     f instanceof LazyDocument.LazyField);
          LazyDocument.LazyField lf = (LazyDocument.LazyField) f;
          assertFalse(f.name() + " is loaded", lf.hasBeenLoaded());
        }
      }
      System.out.println("numFieldValues == " + numFieldValues);
      assertEquals("numFieldValues", 1 + (NUM_VALUES * FIELDS.length), 
                   numFieldValues);
        
      for (String fieldName : fieldValueCounts.keySet()) {
        assertEquals("fieldName count: " + fieldName, 
                     NUM_VALUES, (int)fieldValueCounts.get(fieldName));
      }

      // pick a single field name to load a single value
      final String fieldName = FIELDS[random().nextInt(FIELDS.length)];
      final StorableField[] fieldValues = d.getFields(fieldName);
      assertEquals("#vals in field: " + fieldName, 
                   NUM_VALUES, fieldValues.length);
      final int valNum = random().nextInt(fieldValues.length);
      assertEquals(id + "_" + fieldName + "_" + valNum,
                   fieldValues[valNum].stringValue());
      
      // now every value of fieldName should be loaded
      for (StorableField f : d) {
        if (f.name().equals("never_load")) {
          fail("never_load was loaded");
        }
        if (f.name().equals("load_later")) {
          fail("load_later was loaded too soon");
        }
        if (f.name().equals("docid")) {
          assertFalse(f.name(), f instanceof LazyDocument.LazyField);
        } else {
          assertTrue(f.name() + " is " + f.getClass(),
                     f instanceof LazyDocument.LazyField);
          LazyDocument.LazyField lf = (LazyDocument.LazyField) f;
          assertEquals(f.name() + " is loaded?", 
                       lf.name().equals(fieldName), lf.hasBeenLoaded());
        }
      }

      // use the same LazyDoc to ask for one more lazy field
      visitor = new LazyTestingStoredFieldVisitor(new LazyDocument(reader, hits[0].doc),
                                                  "load_later");
      reader.document(hits[0].doc, visitor);
      d = visitor.doc;
      
      // ensure we have all the values we expect now, and that
      // adding one more lazy field didn't "unload" the existing LazyField's
      // we already loaded.
      for (StorableField f : d) {
        if (f.name().equals("never_load")) {
          fail("never_load was loaded");
        }
        if (f.name().equals("docid")) {
          assertFalse(f.name(), f instanceof LazyDocument.LazyField);
        } else {
          assertTrue(f.name() + " is " + f.getClass(),
                     f instanceof LazyDocument.LazyField);
          LazyDocument.LazyField lf = (LazyDocument.LazyField) f;
          assertEquals(f.name() + " is loaded?", 
                       lf.name().equals(fieldName), lf.hasBeenLoaded());
        }
      }

      // even the underlying doc shouldn't have never_load
      assertNull("never_load was loaded in wrapped doc",
                 visitor.lazyDoc.getDocument().getField("never_load"));

    } finally {
      reader.close();
    }
  }

  private static class LazyTestingStoredFieldVisitor extends StoredFieldVisitor {
    public final StoredDocument doc = new StoredDocument();
    public final LazyDocument lazyDoc;
    public final Set<String> lazyFieldNames;

    LazyTestingStoredFieldVisitor(LazyDocument l, String... fields) {
      lazyDoc = l;
      lazyFieldNames = new HashSet<>(Arrays.asList(fields));
    }

    @Override
    public Status needsField(FieldInfo fieldInfo) {
      if (fieldInfo.name.equals("docid")) {
        return Status.YES;
      } else if (fieldInfo.name.equals("never_load")) {
        return Status.NO;
      } else {
        if (lazyFieldNames.contains(fieldInfo.name)) {
          doc.add(lazyDoc.getField(fieldInfo));
        }
      }
      return Status.NO;
    }

    @Override
    public void stringField(FieldInfo fieldInfo, String value) throws IOException {
      final FieldType ft = new FieldType(TextField.TYPE_STORED);
      ft.setStoreTermVectors(fieldInfo.hasVectors());
      ft.setIndexed(fieldInfo.isIndexed());
      ft.setOmitNorms(fieldInfo.omitsNorms());
      ft.setIndexOptions(fieldInfo.getIndexOptions());
      doc.add(new Field(fieldInfo.name, value, ft));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3189.java