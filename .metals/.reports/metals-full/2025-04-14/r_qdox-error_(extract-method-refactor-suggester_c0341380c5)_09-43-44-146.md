error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1131.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1131.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1131.java
text:
```scala
protected R@@eader initReader(String fieldName, Reader reader) {

package org.apache.lucene.analysis.core;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.CharStream;
import org.apache.lucene.analysis.MockCharFilter;
import org.apache.lucene.analysis.MockTokenFilter;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.charfilter.MappingCharFilter;
import org.apache.lucene.analysis.charfilter.NormalizeCharMap;
import org.apache.lucene.analysis.commongrams.CommonGramsFilter;
import org.apache.lucene.analysis.util.CharArraySet;

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

public class TestBugInSomething extends BaseTokenStreamTestCase {
  public void test() throws Exception {
    final CharArraySet cas = new CharArraySet(TEST_VERSION_CURRENT, 3, false);
    cas.add("jjp");
    cas.add("wlmwoknt");
    cas.add("tcgyreo");
    
    final NormalizeCharMap.Builder builder = new NormalizeCharMap.Builder();
    builder.add("mtqlpi", "");
    builder.add("mwoknt", "jjp");
    builder.add("tcgyreo", "zpfpajyws");
    final NormalizeCharMap map = builder.build();
    
    Analyzer a = new Analyzer() {
      @Override
      protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        Tokenizer t = new MockTokenizer(new TestRandomChains.CheckThatYouDidntReadAnythingReaderWrapper(reader), MockTokenFilter.ENGLISH_STOPSET, false, -65);
        TokenFilter f = new CommonGramsFilter(TEST_VERSION_CURRENT, t, cas);
        return new TokenStreamComponents(t, f);
      }

      @Override
      protected Reader initReader(Reader reader) {
        reader = new MockCharFilter(reader, 0);
        reader = new MappingCharFilter(map, reader);
        return reader;
      }
    };
    checkAnalysisConsistency(random(), a, false, "wmgddzunizdomqyj");
  }
  
  CharStream wrappedStream = new CharStream() {

    @Override
    public void mark(int readAheadLimit) throws IOException {
      throw new UnsupportedOperationException("mark(int)");
    }

    @Override
    public boolean markSupported() {
      throw new UnsupportedOperationException("markSupported()");
    }

    @Override
    public int read() throws IOException {
      throw new UnsupportedOperationException("read()");
    }

    @Override
    public int read(char[] cbuf) throws IOException {
      throw new UnsupportedOperationException("read(char[])");
    }

    @Override
    public int read(CharBuffer target) throws IOException {
      throw new UnsupportedOperationException("read(CharBuffer)");
    }

    @Override
    public boolean ready() throws IOException {
      throw new UnsupportedOperationException("ready()");
    }

    @Override
    public void reset() throws IOException {
      throw new UnsupportedOperationException("reset()");
    }

    @Override
    public long skip(long n) throws IOException {
      throw new UnsupportedOperationException("skip(long)");
    }

    @Override
    public int correctOffset(int currentOff) {
      throw new UnsupportedOperationException("correctOffset(int)");
    }

    @Override
    public void close() throws IOException {
      throw new UnsupportedOperationException("close()");
    }

    @Override
    public int read(char[] arg0, int arg1, int arg2) throws IOException {
      throw new UnsupportedOperationException("read(char[], int, int)");
    }
  };
  
  public void testWrapping() throws Exception {
    CharStream cs = new TestRandomChains.CheckThatYouDidntReadAnythingReaderWrapper(wrappedStream);
    try {
      cs.mark(1);
      fail();
    } catch (Exception e) {
      assertEquals("mark(int)", e.getMessage());
    }
    
    try {
      cs.markSupported();
      fail();
    } catch (Exception e) {
      assertEquals("markSupported()", e.getMessage());
    }
    
    try {
      cs.read();
      fail();
    } catch (Exception e) {
      assertEquals("read()", e.getMessage());
    }
    
    try {
      cs.read(new char[0]);
      fail();
    } catch (Exception e) {
      assertEquals("read(char[])", e.getMessage());
    }
    
    try {
      cs.read(CharBuffer.wrap(new char[0]));
      fail();
    } catch (Exception e) {
      assertEquals("read(CharBuffer)", e.getMessage());
    }
    
    try {
      cs.reset();
      fail();
    } catch (Exception e) {
      assertEquals("reset()", e.getMessage());
    }
    
    try {
      cs.skip(1);
      fail();
    } catch (Exception e) {
      assertEquals("skip(long)", e.getMessage());
    }
    
    try {
      cs.correctOffset(1);
      fail();
    } catch (Exception e) {
      assertEquals("correctOffset(int)", e.getMessage());
    }
    
    try {
      cs.close();
      fail();
    } catch (Exception e) {
      assertEquals("close()", e.getMessage());
    }
    
    try {
      cs.read(new char[0], 0, 0);
      fail();
    } catch (Exception e) {
      assertEquals("read(char[], int, int)", e.getMessage());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1131.java