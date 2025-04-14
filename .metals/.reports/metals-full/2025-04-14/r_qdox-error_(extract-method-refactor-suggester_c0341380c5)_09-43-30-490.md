error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/201.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/201.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/201.java
text:
```scala
s@@uper(factory, (BreakIterator)sentenceProto.clone());

package org.apache.lucene.analysis.th;

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

import java.text.BreakIterator;
import java.util.Locale;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.util.CharArrayIterator;
import org.apache.lucene.analysis.util.SegmentingTokenizerBase;

/**
 * Tokenizer that use {@link BreakIterator} to tokenize Thai text.
 * <p>WARNING: this tokenizer may not be supported by all JREs.
 *    It is known to work with Sun/Oracle and Harmony JREs.
 *    If your application needs to be fully portable, consider using ICUTokenizer instead,
 *    which uses an ICU Thai BreakIterator that will always be available.
 */
public class ThaiTokenizer extends SegmentingTokenizerBase {
  /** 
   * True if the JRE supports a working dictionary-based breakiterator for Thai.
   * If this is false, this tokenizer will not work at all!
   */
  public static final boolean DBBI_AVAILABLE;
  private static final BreakIterator proto = BreakIterator.getWordInstance(new Locale("th"));
  static {
    // check that we have a working dictionary-based break iterator for thai
    proto.setText("ภาษาไทย");
    DBBI_AVAILABLE = proto.isBoundary(4);
  }
  
  /** used for breaking the text into sentences */
  private static final BreakIterator sentenceProto = BreakIterator.getSentenceInstance(Locale.ROOT);
  
  private final BreakIterator wordBreaker;
  private final CharArrayIterator wrapper = CharArrayIterator.newWordInstance();
  
  int sentenceStart;
  int sentenceEnd;
  
  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
  
  /** Creates a new ThaiTokenizer */
  public ThaiTokenizer() {
    this(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY);
  }
      
  /** Creates a new ThaiTokenizer, supplying the AttributeFactory */
  public ThaiTokenizer(AttributeFactory factory) {
    super((BreakIterator)sentenceProto.clone());
    if (!DBBI_AVAILABLE) {
      throw new UnsupportedOperationException("This JRE does not have support for Thai segmentation");
    }
    wordBreaker = (BreakIterator)proto.clone();
  }

  @Override
  protected void setNextSentence(int sentenceStart, int sentenceEnd) {
    this.sentenceStart = sentenceStart;
    this.sentenceEnd = sentenceEnd;
    wrapper.setText(buffer, sentenceStart, sentenceEnd - sentenceStart);
    wordBreaker.setText(wrapper);
  }

  @Override
  protected boolean incrementWord() {
    int start = wordBreaker.current();
    if (start == BreakIterator.DONE) {
      return false; // BreakIterator exhausted
    }

    // find the next set of boundaries, skipping over non-tokens
    int end = wordBreaker.next();
    while (end != BreakIterator.DONE &&
           !Character.isLetterOrDigit(Character.codePointAt(buffer, sentenceStart + start, sentenceEnd))) {
      start = end;
      end = wordBreaker.next();
    }

    if (end == BreakIterator.DONE) {
      return false; // BreakIterator exhausted
    }

    clearAttributes();
    termAtt.copyBuffer(buffer, start, end - start);
    offsetAtt.setOffset(correctOffset(offset + sentenceStart + start), correctOffset(offset + sentenceStart + end));
    return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/201.java