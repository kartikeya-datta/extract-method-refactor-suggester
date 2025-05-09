error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2253.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2253.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2253.java
text:
```scala
o@@ffsetAtt.setOffset(correctOffset(startOffset), correctOffset(endOffset));

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
import java.io.Reader;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

/**
 * tokenizer for testing. Optionally lowercases.
 */
public class MockTokenizer extends Tokenizer {
  /** Acts Similar to WhitespaceTokenizer */
  public static final int WHITESPACE = 0; 
  /** Acts Similar to KeywordTokenizer.
   * TODO: Keyword returns an "empty" token for an empty reader... 
   */
  public static final int KEYWORD = 1;
  /** Acts like LetterTokenizer. */
  public static final int SIMPLE = 2;

  private final int pattern;
  private final boolean lowerCase;

  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
  int off = 0;

  // TODO: "register" with LuceneTestCase to ensure all streams are closed() ?
  // currently, we can only check that the lifecycle is correct if someone is reusing,
  // but not for "one-offs".
  private static enum State { 
    SETREADER,       // consumer set a reader input either via ctor or via reset(Reader)
    RESET,           // consumer has called reset()
    INCREMENT,       // consumer is consuming, has called incrementToken() == true
    INCREMENT_FALSE, // consumer has called incrementToken() which returned false
    END,             // consumer has called end() to perform end of stream operations
    CLOSE            // consumer has called close() to release any resources
  };
  
  private State streamState = State.CLOSE;
  private boolean enableChecks = true;
  
  public MockTokenizer(AttributeFactory factory, Reader input, int pattern, boolean lowerCase) {
    super(factory, input);
    this.pattern = pattern;
    this.lowerCase = lowerCase;
    this.streamState = State.SETREADER;
  }

  public MockTokenizer(Reader input, int pattern, boolean lowerCase) {
    super(input);
    this.pattern = pattern;
    this.lowerCase = lowerCase;
    this.streamState = State.SETREADER;
  }
  
  @Override
  public final boolean incrementToken() throws IOException {
    assert !enableChecks || (streamState == State.RESET || streamState == State.INCREMENT) 
                            : "incrementToken() called while in wrong state: " + streamState;
    clearAttributes();
    for (;;) {
      int startOffset = off;
      int cp = readCodePoint();
      if (cp < 0) {
        break;
      } else if (isTokenChar(cp)) {
        int endOffset;
        do {
          char chars[] = Character.toChars(normalize(cp));
          for (int i = 0; i < chars.length; i++)
            termAtt.append(chars[i]);
          endOffset = off;
          cp = readCodePoint();
        } while (cp >= 0 && isTokenChar(cp));
        offsetAtt.setOffset(startOffset, endOffset);
        streamState = State.INCREMENT;
        return true;
      }
    }
    streamState = State.INCREMENT_FALSE;
    return false;
  }

  protected int readCodePoint() throws IOException {
    int ch = input.read();
    if (ch < 0) {
      return ch;
    } else {
      assert ch != 0xffff; /* only on 3.x */
      assert !Character.isLowSurrogate((char) ch);
      off++;
      if (Character.isHighSurrogate((char) ch)) {
        int ch2 = input.read();
        if (ch2 >= 0) {
          off++;
          assert Character.isLowSurrogate((char) ch2);
          return Character.toCodePoint((char) ch, (char) ch2);
        }
      }
      return ch;
    }
  }

  protected boolean isTokenChar(int c) {
    switch(pattern) {
      case WHITESPACE: return !Character.isWhitespace(c);
      case KEYWORD: return true;
      case SIMPLE: return Character.isLetter(c);
      default: throw new RuntimeException("invalid pattern constant:" + pattern);
    }
  }
  
  protected int normalize(int c) {
    return lowerCase ? Character.toLowerCase(c) : c;
  }

  @Override
  public void reset() throws IOException {
    super.reset();
    off = 0;
    assert !enableChecks || streamState != State.RESET : "double reset()";
    streamState = State.RESET;
  }
  
  @Override
  public void close() throws IOException {
    super.close();
    // in some exceptional cases (e.g. TestIndexWriterExceptions) a test can prematurely close()
    // these tests should disable this check, by default we check the normal workflow.
    // TODO: investigate the CachingTokenFilter "double-close"... for now we ignore this
    assert !enableChecks || streamState == State.END || streamState == State.CLOSE : "close() called in wrong state: " + streamState;
    streamState = State.CLOSE;
  }

  @Override
  public void reset(Reader input) throws IOException {
    super.reset(input);
    assert !enableChecks || streamState == State.CLOSE : "setReader() called in wrong state: " + streamState;
    streamState = State.SETREADER;
  }

  @Override
  public void end() throws IOException {
    int finalOffset = correctOffset(off);
    offsetAtt.setOffset(finalOffset, finalOffset);
    // some tokenizers, such as limiting tokenizers, call end() before incrementToken() returns false.
    // these tests should disable this check (in general you should consume the entire stream)
    assert !enableChecks || streamState == State.INCREMENT_FALSE : "end() called before incrementToken() returned false!";
    streamState = State.END;
  }

  /** 
   * Toggle consumer workflow checking: if your test consumes tokenstreams normally you
   * should leave this enabled.
   */
  public void setEnableChecks(boolean enableChecks) {
    this.enableChecks = enableChecks;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2253.java