error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3764.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3764.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3764.java
text:
```scala
c@@heckRandomData(random(), analyzer, 200);

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

package org.apache.lucene.analysis.miscellaneous;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util._TestUtil;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Arrays;

public class TestRemoveDuplicatesTokenFilter extends BaseTokenStreamTestCase {

  public static Token tok(int pos, String t, int start, int end) {
    Token tok = new Token(t,start,end);
    tok.setPositionIncrement(pos);
    return tok;
  }
  public static Token tok(int pos, String t) {
    return tok(pos, t, 0,0);
  }

  public void testDups(final String expected, final Token... tokens)
    throws Exception {

    final Iterator<Token> toks = Arrays.asList(tokens).iterator();
    final TokenStream ts = new RemoveDuplicatesTokenFilter(
      (new TokenStream() {
          CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
          OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
          PositionIncrementAttribute posIncAtt = addAttribute(PositionIncrementAttribute.class);
          @Override
          public boolean incrementToken() {
            if (toks.hasNext()) {
              clearAttributes();
              Token tok = toks.next();
              termAtt.setEmpty().append(tok);
              offsetAtt.setOffset(tok.startOffset(), tok.endOffset());
              posIncAtt.setPositionIncrement(tok.getPositionIncrement());
              return true;
            } else {
              return false;
            }
          }
        }));
    
    assertTokenStreamContents(ts, expected.split("\\s"));   
  }
  
  public void testNoDups() throws Exception {

    testDups("A B B C D E"
             ,tok(1,"A", 0,  4)
             ,tok(1,"B", 5, 10)
             ,tok(1,"B",11, 15)
             ,tok(1,"C",16, 20)
             ,tok(0,"D",16, 20)
             ,tok(1,"E",21, 25)
             );
    
  }
  

  public void testSimpleDups() throws Exception {

    testDups("A B C D E"
             ,tok(1,"A", 0,  4)
             ,tok(1,"B", 5, 10)
             ,tok(0,"B",11, 15)
             ,tok(1,"C",16, 20)
             ,tok(0,"D",16, 20)
             ,tok(1,"E",21, 25)
             );
    
  }
  
  public void testComplexDups() throws Exception {

    testDups("A B C D E F G H I J K"
             ,tok(1,"A")
             ,tok(1,"B")
             ,tok(0,"B")
             ,tok(1,"C")
             ,tok(1,"D")
             ,tok(0,"D")
             ,tok(0,"D")
             ,tok(1,"E")
             ,tok(1,"F")
             ,tok(0,"F")
             ,tok(1,"G")
             ,tok(0,"H")
             ,tok(0,"H")
             ,tok(1,"I")
             ,tok(1,"J")
             ,tok(0,"K")
             ,tok(0,"J")
             );
             
  }
  
  // some helper methods for the below test with synonyms
  private String randomNonEmptyString() {
    while(true) {
      final String s = _TestUtil.randomUnicodeString(random()).trim();
      if (s.length() != 0 && s.indexOf('\u0000') == -1) {
        return s;
      }
    }
  }
  
  private void add(SynonymMap.Builder b, String input, String output, boolean keepOrig) {
    b.add(new CharsRef(input.replaceAll(" +", "\u0000")),
          new CharsRef(output.replaceAll(" +", "\u0000")),
          keepOrig);
  }
  
  /** blast some random strings through the analyzer */
  public void testRandomStrings() throws Exception {
    final int numIters = atLeast(10);
    for (int i = 0; i < numIters; i++) {
      SynonymMap.Builder b = new SynonymMap.Builder(random().nextBoolean());
      final int numEntries = atLeast(10);
      for (int j = 0; j < numEntries; j++) {
        add(b, randomNonEmptyString(), randomNonEmptyString(), random().nextBoolean());
      }
      final SynonymMap map = b.build();
      final boolean ignoreCase = random().nextBoolean();
      
      final Analyzer analyzer = new Analyzer() {
        @Override
        protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
          Tokenizer tokenizer = new MockTokenizer(reader, MockTokenizer.SIMPLE, true);
          TokenStream stream = new SynonymFilter(tokenizer, map, ignoreCase);
          return new TokenStreamComponents(tokenizer, new RemoveDuplicatesTokenFilter(stream));
        }
      };

      checkRandomData(random(), analyzer, 1000*RANDOM_MULTIPLIER);
    }
  }
  
  public void testEmptyTerm() throws IOException {
    Analyzer a = new Analyzer() {
      @Override
      protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        Tokenizer tokenizer = new KeywordTokenizer(reader);
        return new TokenStreamComponents(tokenizer, new RemoveDuplicatesTokenFilter(tokenizer));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3764.java