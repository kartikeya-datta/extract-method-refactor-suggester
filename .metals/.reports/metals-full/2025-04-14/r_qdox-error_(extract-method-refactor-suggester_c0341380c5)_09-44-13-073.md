error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2931.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2931.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2931.java
text:
```scala
I@@ndexReader reader = IndexReader.open(dir, true);

package org.apache.lucene.analysis;

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

import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class TestCachingTokenFilter extends BaseTokenStreamTestCase {
  private String[] tokens = new String[] {"term1", "term2", "term3", "term2"};
  
  public void testCaching() throws IOException {
    Directory dir = new RAMDirectory();
    IndexWriter writer = new IndexWriter(dir, new SimpleAnalyzer(), IndexWriter.MaxFieldLength.LIMITED);
    Document doc = new Document();
    TokenStream stream = new TokenStream() {
      private int index = 0;
      private TermAttribute termAtt = (TermAttribute) addAttribute(TermAttribute.class);
      private OffsetAttribute offsetAtt = (OffsetAttribute) addAttribute(OffsetAttribute.class);
      
      public boolean incrementToken() throws IOException {
        if (index == tokens.length) {
          return false;
        } else {
          termAtt.setTermBuffer(tokens[index++]);
          offsetAtt.setOffset(0,0);
          return true;
        }        
      }
      
    };
    
    stream = new CachingTokenFilter(stream);
    
    doc.add(new Field("preanalyzed", stream, TermVector.NO));
    
    // 1) we consume all tokens twice before we add the doc to the index
    checkTokens(stream);
    stream.reset();  
    checkTokens(stream);
    
    // 2) now add the document to the index and verify if all tokens are indexed
    //    don't reset the stream here, the DocumentWriter should do that implicitly
    writer.addDocument(doc);
    writer.close();
    
    IndexReader reader = IndexReader.open(dir);
    TermPositions termPositions = reader.termPositions(new Term("preanalyzed", "term1"));
    assertTrue(termPositions.next());
    assertEquals(1, termPositions.freq());
    assertEquals(0, termPositions.nextPosition());

    termPositions.seek(new Term("preanalyzed", "term2"));
    assertTrue(termPositions.next());
    assertEquals(2, termPositions.freq());
    assertEquals(1, termPositions.nextPosition());
    assertEquals(3, termPositions.nextPosition());
    
    termPositions.seek(new Term("preanalyzed", "term3"));
    assertTrue(termPositions.next());
    assertEquals(1, termPositions.freq());
    assertEquals(2, termPositions.nextPosition());
    reader.close();
    
    // 3) reset stream and consume tokens again
    stream.reset();
    checkTokens(stream);
  }
  
  private void checkTokens(TokenStream stream) throws IOException {
    int count = 0;
    
    TermAttribute termAtt = (TermAttribute) stream.getAttribute(TermAttribute.class);
    assertNotNull(termAtt);
    while (stream.incrementToken()) {
      assertTrue(count < tokens.length);
      assertEquals(tokens[count], termAtt.term());
      count++;
    }
    
    assertEquals(tokens.length, count);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2931.java