error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1591.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1591.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1591.java
text:
```scala
w@@rite((Token)t.clone());

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

package org.apache.solr.analysis;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import java.io.IOException;
import java.io.StringReader;

/**
 * Test that BufferedTokenStream behaves as advertised in subclasses.
 */
public class TestBufferedTokenStream extends BaseTokenTestCase {

  /** Example of a class implementing the rule "A" "B" => "Q" "B" */
  public static class AB_Q_Stream extends BufferedTokenStream {
    public AB_Q_Stream(TokenStream input) {super(input);}
    protected Token process(Token t) throws IOException {
      if ("A".equals(new String(t.termBuffer(), 0, t.termLength()))) {
        Token t2 = read();
        if (t2!=null && "B".equals(new String(t2.termBuffer(), 0, t2.termLength()))) t.setTermText("Q");
        if (t2!=null) pushBack(t2);
      }
      return t;
    }
  }

  /** Example of a class implementing "A" "B" => "A" "A" "B" */
  public static class AB_AAB_Stream extends BufferedTokenStream {
    public AB_AAB_Stream(TokenStream input) {super(input);}
    protected Token process(Token t) throws IOException {
      if ("A".equals(new String(t.termBuffer(), 0, t.termLength())) && 
          "B".equals(new String(peek(1).termBuffer(), 0, peek(1).termLength())))
        write(t);
      return t;
    }
  }
    
  public void testABQ() throws Exception {
    final String input = "How now A B brown A cow B like A B thing?";
    final String expected = "How now Q B brown A cow B like Q B thing?";
    TokenStream ts = new AB_Q_Stream
      (new WhitespaceTokenizer(new StringReader(input)));
    final String actual = tsToString(ts);
    //System.out.println(actual);
    assertEquals(expected, actual);
  }
  
  public void testABAAB() throws Exception {
    final String input = "How now A B brown A cow B like A B thing?";
    final String expected = "How now A A B brown A cow B like A A B thing?";
    TokenStream ts = new AB_AAB_Stream
      (new WhitespaceTokenizer(new StringReader(input)));
    final String actual = tsToString(ts);
    //System.out.println(actual);
    assertEquals(expected, actual);
  }
  
  public void testReset() throws Exception {
    final String input = "How now A B brown A cow B like A B thing?";
    Tokenizer tokenizer = new WhitespaceTokenizer(new StringReader(input));
    TokenStream ts = new AB_AAB_Stream(tokenizer);
    TermAttribute term = (TermAttribute) ts.addAttribute(TermAttribute.class);
    assertTrue(ts.incrementToken());
    assertEquals("How", term.term());
    assertTrue(ts.incrementToken());
    assertEquals("now", term.term());
    assertTrue(ts.incrementToken());
    assertEquals("A", term.term());
    // reset back to input, 
    // if reset() does not work correctly then previous buffered tokens will remain 
    tokenizer.reset(new StringReader(input));
    ts.reset();
    assertTrue(ts.incrementToken());
    assertEquals("How", term.term());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1591.java