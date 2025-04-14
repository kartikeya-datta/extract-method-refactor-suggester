error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1199.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1199.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1199.java
text:
```scala
c@@heckRandomData(random(), a, 1000*RANDOM_MULTIPLIER);

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

package org.apache.lucene.analysis.miscellaneous;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;

import static org.apache.lucene.analysis.miscellaneous.CapitalizationFilter.*;

/** Tests {@link CapitalizationFilter} */
public class TestCapitalizationFilter extends BaseTokenStreamTestCase {  
  public void testCapitalization() throws Exception {
    CharArraySet keep = new CharArraySet(TEST_VERSION_CURRENT,
        Arrays.asList("and", "the", "it", "BIG"), false);
    
    assertCapitalizesTo("kiTTEN", new String[] { "Kitten" }, 
        true, keep, true, null, 0, DEFAULT_MAX_WORD_COUNT, DEFAULT_MAX_TOKEN_LENGTH);
    
    assertCapitalizesTo("and", new String[] { "And" }, 
        true, keep, true, null, 0, DEFAULT_MAX_WORD_COUNT, DEFAULT_MAX_TOKEN_LENGTH);
    
    assertCapitalizesTo("AnD", new String[] { "And" }, 
        true, keep, true, null, 0, DEFAULT_MAX_WORD_COUNT, DEFAULT_MAX_TOKEN_LENGTH);

    //first is not forced, but it's not a keep word, either
    assertCapitalizesTo("AnD", new String[] { "And" }, 
        true, keep, false, null, 0, DEFAULT_MAX_WORD_COUNT, DEFAULT_MAX_TOKEN_LENGTH);

    assertCapitalizesTo("big", new String[] { "Big" }, 
        true, keep, true, null, 0, DEFAULT_MAX_WORD_COUNT, DEFAULT_MAX_TOKEN_LENGTH);

    assertCapitalizesTo("BIG", new String[] { "BIG" }, 
        true, keep, true, null, 0, DEFAULT_MAX_WORD_COUNT, DEFAULT_MAX_TOKEN_LENGTH);
    
    assertCapitalizesToKeyword("Hello thEre my Name is Ryan", "Hello there my name is ryan", 
        true, keep, true, null, 0, DEFAULT_MAX_WORD_COUNT, DEFAULT_MAX_TOKEN_LENGTH);
    
    // now each token
    assertCapitalizesTo("Hello thEre my Name is Ryan", 
        new String[] { "Hello", "There", "My", "Name", "Is", "Ryan" }, 
        false, keep, true, null, 0, DEFAULT_MAX_WORD_COUNT, DEFAULT_MAX_TOKEN_LENGTH);
           
    // now only the long words
    assertCapitalizesTo("Hello thEre my Name is Ryan", 
        new String[] { "Hello", "There", "my", "Name", "is", "Ryan" }, 
        false, keep, true, null, 3, DEFAULT_MAX_WORD_COUNT, DEFAULT_MAX_TOKEN_LENGTH);
    
    // without prefix
    assertCapitalizesTo("McKinley", 
        new String[] { "Mckinley" }, 
        true, keep, true, null, 0, DEFAULT_MAX_WORD_COUNT, DEFAULT_MAX_TOKEN_LENGTH);
    
    // Now try some prefixes
    List<char[]> okPrefix = new ArrayList<char[]>();
    okPrefix.add("McK".toCharArray());
    
    assertCapitalizesTo("McKinley", 
        new String[] { "McKinley" }, 
        true, keep, true, okPrefix, 0, DEFAULT_MAX_WORD_COUNT, DEFAULT_MAX_TOKEN_LENGTH);
    
    // now try some stuff with numbers
    assertCapitalizesTo("1st 2nd third", 
        new String[] { "1st", "2nd", "Third" }, 
        false, keep, false, null, 0, DEFAULT_MAX_WORD_COUNT, DEFAULT_MAX_TOKEN_LENGTH);    
    
    assertCapitalizesToKeyword("the The the", "The The the", 
        false, keep, true, null, 0, DEFAULT_MAX_WORD_COUNT, DEFAULT_MAX_TOKEN_LENGTH);    
  }
  
  static void assertCapitalizesTo(Tokenizer tokenizer, String expected[],
      boolean onlyFirstWord, CharArraySet keep, boolean forceFirstLetter,
      Collection<char[]> okPrefix, int minWordLength, int maxWordCount,
      int maxTokenLength) throws IOException {
    CapitalizationFilter filter = new CapitalizationFilter(tokenizer, onlyFirstWord, keep, 
        forceFirstLetter, okPrefix, minWordLength, maxWordCount, maxTokenLength);
    assertTokenStreamContents(filter, expected);    
  }
  
  static void assertCapitalizesTo(String input, String expected[],
      boolean onlyFirstWord, CharArraySet keep, boolean forceFirstLetter,
      Collection<char[]> okPrefix, int minWordLength, int maxWordCount,
      int maxTokenLength) throws IOException {
    assertCapitalizesTo(new MockTokenizer(new StringReader(input), MockTokenizer.WHITESPACE, false),
        expected, onlyFirstWord, keep, forceFirstLetter, okPrefix, minWordLength, 
        maxWordCount, maxTokenLength);
  }
  
  static void assertCapitalizesToKeyword(String input, String expected,
      boolean onlyFirstWord, CharArraySet keep, boolean forceFirstLetter,
      Collection<char[]> okPrefix, int minWordLength, int maxWordCount,
      int maxTokenLength) throws IOException {
    assertCapitalizesTo(new MockTokenizer(new StringReader(input), MockTokenizer.KEYWORD, false),
        new String[] { expected }, onlyFirstWord, keep, forceFirstLetter, okPrefix,
        minWordLength, maxWordCount, maxTokenLength);    
  }
  
  /** blast some random strings through the analyzer */
  public void testRandomString() throws Exception {
    Analyzer a = new Analyzer() {

      @Override
      protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        Tokenizer tokenizer = new MockTokenizer(reader, MockTokenizer.WHITESPACE, false);
        return new TokenStreamComponents(tokenizer, new CapitalizationFilter(tokenizer));
      }
    };
    
    checkRandomData(random(), a, 10000*RANDOM_MULTIPLIER);
  }
  
  public void testEmptyTerm() throws IOException {
    Analyzer a = new Analyzer() {
      @Override
      protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        Tokenizer tokenizer = new KeywordTokenizer(reader);
        return new TokenStreamComponents(tokenizer, new CapitalizationFilter(tokenizer));
      }
    };
    checkOneTermReuse(a, "", "");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1199.java