error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3429.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3429.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3429.java
text:
```scala
s@@uper(random, delegate);

package org.apache.lucene.index;

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

import java.io.IOException;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.codecs.CodecProvider;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.MockDirectoryWrapper;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.BytesRef;

/**
 * Tests lazy skipping on the proximity file.
 *
 */
public class TestLazyProxSkipping extends LuceneTestCase {
    private Searcher searcher;
    private int seeksCounter = 0;
    
    private String field = "tokens";
    private String term1 = "xx";
    private String term2 = "yy";
    private String term3 = "zz";

    private class SeekCountingDirectory extends MockDirectoryWrapper {
      public SeekCountingDirectory(Directory delegate) {
        super(delegate);
      }

      @Override
      public IndexInput openInput(String name) throws IOException {
        IndexInput ii = super.openInput(name);
        if (name.endsWith(".prx") || name.endsWith(".pos") ) {
          // we decorate the proxStream with a wrapper class that allows to count the number of calls of seek()
          ii = new SeeksCountingStream(ii);
        }
        return ii;
      }
      
    }
    
    private void createIndex(int numHits) throws IOException {
        int numDocs = 500;
        
        Directory directory = new SeekCountingDirectory(new RAMDirectory());
        IndexWriter writer = new IndexWriter(directory, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(MockTokenizer.WHITESPACE, true, false)).setMaxBufferedDocs(10));
        ((LogMergePolicy) writer.getConfig().getMergePolicy()).setUseCompoundFile(false);
        ((LogMergePolicy) writer.getConfig().getMergePolicy()).setUseCompoundDocStore(false);
        for (int i = 0; i < numDocs; i++) {
            Document doc = new Document();
            String content;
            if (i % (numDocs / numHits) == 0) {
                // add a document that matches the query "term1 term2"
                content = this.term1 + " " + this.term2;
            } else if (i % 15 == 0) {
                // add a document that only contains term1
                content = this.term1 + " " + this.term1;
            } else {
                // add a document that contains term2 but not term 1
                content = this.term3 + " " + this.term2;
            }

            doc.add(newField(this.field, content, Field.Store.YES, Field.Index.ANALYZED));
            writer.addDocument(doc);
        }
        
        // make sure the index has only a single segment
        writer.optimize();
        writer.close();
        
        SegmentReader reader = SegmentReader.getOnlySegmentReader(directory);

        this.searcher = new IndexSearcher(reader);        
    }
    
    private ScoreDoc[] search() throws IOException {
        // create PhraseQuery "term1 term2" and search
        PhraseQuery pq = new PhraseQuery();
        pq.add(new Term(this.field, this.term1));
        pq.add(new Term(this.field, this.term2));
        return this.searcher.search(pq, null, 1000).scoreDocs;        
    }
    
    private void performTest(int numHits) throws IOException {
        createIndex(numHits);
        this.seeksCounter = 0;
        ScoreDoc[] hits = search();
        // verify that the right number of docs was found
        assertEquals(numHits, hits.length);
        
        // check if the number of calls of seek() does not exceed the number of hits
        assertTrue(this.seeksCounter > 0);
        assertTrue("seeksCounter=" + this.seeksCounter + " numHits=" + numHits, this.seeksCounter <= numHits + 1);
    }
 
    public void testLazySkipping() throws IOException {
        assumeFalse("This test cannot run with SimpleText codec", CodecProvider.getDefaultCodec().equals("SimpleText"));
        // test whether only the minimum amount of seeks()
        // are performed
        performTest(5);
        performTest(10);
    }
    
    public void testSeek() throws IOException {
        Directory directory = newDirectory();
        IndexWriter writer = new IndexWriter(directory, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer()));
        for (int i = 0; i < 10; i++) {
            Document doc = new Document();
            doc.add(newField(this.field, "a b", Field.Store.YES, Field.Index.ANALYZED));
            writer.addDocument(doc);
        }
        
        writer.close();
        IndexReader reader = IndexReader.open(directory, true);

        DocsAndPositionsEnum tp = MultiFields.getTermPositionsEnum(reader,
                                                                   MultiFields.getDeletedDocs(reader),
                                                                   this.field,
                                                                   new BytesRef("b"));

        for (int i = 0; i < 10; i++) {
            tp.nextDoc();
            assertEquals(tp.docID(), i);
            assertEquals(tp.nextPosition(), 1);
        }

        tp = MultiFields.getTermPositionsEnum(reader,
                                              MultiFields.getDeletedDocs(reader),
                                              this.field,
                                              new BytesRef("a"));

        for (int i = 0; i < 10; i++) {
            tp.nextDoc();
            assertEquals(tp.docID(), i);
            assertEquals(tp.nextPosition(), 0);
        }
        reader.close();
        directory.close();
        
    }
    

    // Simply extends IndexInput in a way that we are able to count the number
    // of invocations of seek()
    class SeeksCountingStream extends IndexInput {
          private IndexInput input;      
          
          
          SeeksCountingStream(IndexInput input) {
              this.input = input;
          }      
                
          @Override
          public byte readByte() throws IOException {
              return this.input.readByte();
          }
    
          @Override
          public void readBytes(byte[] b, int offset, int len) throws IOException {
              this.input.readBytes(b, offset, len);        
          }
    
          @Override
          public void close() throws IOException {
              this.input.close();
          }
    
          @Override
          public long getFilePointer() {
              return this.input.getFilePointer();
          }
    
          @Override
          public void seek(long pos) throws IOException {
              TestLazyProxSkipping.this.seeksCounter++;
              this.input.seek(pos);
          }
    
          @Override
          public long length() {
              return this.input.length();
          }
          
          @Override
          public Object clone() {
              return new SeeksCountingStream((IndexInput) this.input.clone());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3429.java