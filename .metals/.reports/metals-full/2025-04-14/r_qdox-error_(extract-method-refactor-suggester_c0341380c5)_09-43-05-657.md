error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/432.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/432.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/432.java
text:
```scala
private S@@impleAutomatonTermsEnum(TermsEnum tenum) {

package org.apache.lucene.search;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.FilteredTermsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.UnicodeUtil;
import org.apache.lucene.util._TestUtil;
import org.apache.lucene.util.automaton.Automaton;
import org.apache.lucene.util.automaton.AutomatonTestUtil;
import org.apache.lucene.util.automaton.CharacterRunAutomaton;
import org.apache.lucene.util.automaton.RegExp;

/**
 * Create an index with random unicode terms
 * Generates random regexps, and validates against a simple impl.
 */
public class TestRegexpRandom2 extends LuceneTestCase {
  protected IndexSearcher searcher1;
  protected IndexSearcher searcher2;
  private IndexReader reader;
  private Directory dir;
  protected String fieldName;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    dir = newDirectory();
    fieldName = random().nextBoolean() ? "field" : ""; // sometimes use an empty string as field name
    RandomIndexWriter writer = new RandomIndexWriter(random(), dir, 
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random(), MockTokenizer.KEYWORD, false))
        .setMaxBufferedDocs(_TestUtil.nextInt(random(), 50, 1000)));
    Document doc = new Document();
    Field field = newStringField(fieldName, "", Field.Store.NO);
    doc.add(field);
    List<String> terms = new ArrayList<String>();
    int num = atLeast(200);
    for (int i = 0; i < num; i++) {
      String s = _TestUtil.randomUnicodeString(random());
      field.setStringValue(s);
      terms.add(s);
      writer.addDocument(doc);
    }

    if (VERBOSE) {
      // utf16 order
      Collections.sort(terms);
      System.out.println("UTF16 order:");
      for(String s : terms) {
        System.out.println("  " + UnicodeUtil.toHexString(s));
      }
    }
    
    reader = writer.getReader();
    searcher1 = newSearcher(reader);
    searcher2 = newSearcher(reader);
    writer.close();
  }

  @Override
  public void tearDown() throws Exception {
    reader.close();
    dir.close();
    super.tearDown();
  }
  
  /** a stupid regexp query that just blasts thru the terms */
  private class DumbRegexpQuery extends MultiTermQuery {
    private final Automaton automaton;
    
    DumbRegexpQuery(Term term, int flags) {
      super(term.field());
      RegExp re = new RegExp(term.text(), flags);
      automaton = re.toAutomaton();
    }
    
    @Override
    protected TermsEnum getTermsEnum(Terms terms, AttributeSource atts) throws IOException {
      return new SimpleAutomatonTermsEnum(terms.iterator(null));
    }

    private class SimpleAutomatonTermsEnum extends FilteredTermsEnum {
      CharacterRunAutomaton runAutomaton = new CharacterRunAutomaton(automaton);
      CharsRef utf16 = new CharsRef(10);

      private SimpleAutomatonTermsEnum(TermsEnum tenum) throws IOException {
        super(tenum);
        setInitialSeekTerm(new BytesRef(""));
      }
      
      @Override
      protected AcceptStatus accept(BytesRef term) throws IOException {
        UnicodeUtil.UTF8toUTF16(term.bytes, term.offset, term.length, utf16);
        return runAutomaton.run(utf16.chars, 0, utf16.length) ? 
            AcceptStatus.YES : AcceptStatus.NO;
      }
    }

    @Override
    public String toString(String field) {
      return field.toString() + automaton.toString();
    }
  }
  
  /** test a bunch of random regular expressions */
  public void testRegexps() throws Exception {
    int num = atLeast(1000);
    for (int i = 0; i < num; i++) {
      String reg = AutomatonTestUtil.randomRegexp(random());
      if (VERBOSE) {
        System.out.println("TEST: regexp=" + reg);
      }
      assertSame(reg);
    }
  }
  
  /** check that the # of hits is the same as from a very
   * simple regexpquery implementation.
   */
  protected void assertSame(String regexp) throws IOException {   
    RegexpQuery smart = new RegexpQuery(new Term(fieldName, regexp), RegExp.NONE);
    DumbRegexpQuery dumb = new DumbRegexpQuery(new Term(fieldName, regexp), RegExp.NONE);
   
    TopDocs smartDocs = searcher1.search(smart, 25);
    TopDocs dumbDocs = searcher2.search(dumb, 25);

    CheckHits.checkEqual(smart, smartDocs.scoreDocs, dumbDocs.scoreDocs);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/432.java