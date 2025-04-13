error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3204.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3204.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3204.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.sandbox.queries.regex;

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

import org.apache.lucene.document.Field;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.store.Directory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.index.TermsEnum;

import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.LuceneTestCase;

public class TestRegexQuery extends LuceneTestCase {
  private IndexSearcher searcher;
  private IndexReader reader;
  private Directory directory;
  private final String FN = "field";


  @Override
  public void setUp() throws Exception {
    super.setUp();
    directory = newDirectory();
    RandomIndexWriter writer = new RandomIndexWriter(random(), directory);
    Document doc = new Document();
    doc.add(newTextField(FN, "the quick brown fox jumps over the lazy dog", Field.Store.NO));
    writer.addDocument(doc);
    reader = writer.getReader();
    writer.close();
    searcher = newSearcher(reader);
  }

  @Override
  public void tearDown() throws Exception {
    reader.close();
    directory.close();
    super.tearDown();
  }

  private Term newTerm(String value) { return new Term(FN, value); }

  private int  regexQueryNrHits(String regex, RegexCapabilities capability) throws Exception {
    RegexQuery query = new RegexQuery( newTerm(regex));
    
    if ( capability != null )
      query.setRegexImplementation(capability);
    
    return searcher.search(query, null, 1000).totalHits;
  }

  private int  spanRegexQueryNrHits(String regex1, String regex2, int slop, boolean ordered) throws Exception {
    SpanQuery srq1 = new SpanMultiTermQueryWrapper<>(new RegexQuery(newTerm(regex1)));
    SpanQuery srq2 = new SpanMultiTermQueryWrapper<>(new RegexQuery(newTerm(regex2)));
    SpanNearQuery query = new SpanNearQuery( new SpanQuery[]{srq1, srq2}, slop, ordered);

    return searcher.search(query, null, 1000).totalHits;
  }

  public void testMatchAll() throws Exception {
    Terms terms = MultiFields.getTerms(searcher.getIndexReader(), FN);
    TermsEnum te = new RegexQuery(new Term(FN, "jum.")).getTermsEnum(terms, new AttributeSource() /*dummy*/);
    // no term should match
    assertNull(te.next());
  }

  public void testRegex1() throws Exception {
    assertEquals(1, regexQueryNrHits("^q.[aeiou]c.*$", null));
  }

  public void testRegex2() throws Exception {
    assertEquals(0, regexQueryNrHits("^.[aeiou]c.*$", null));
  }

  public void testRegex3() throws Exception {
    assertEquals(0, regexQueryNrHits("^q.[aeiou]c$", null));
  }

  public void testSpanRegex1() throws Exception {
    assertEquals(1, spanRegexQueryNrHits("^q.[aeiou]c.*$", "dog", 6, true));
  }

  public void testSpanRegex2() throws Exception {
    assertEquals(0, spanRegexQueryNrHits("^q.[aeiou]c.*$", "dog", 5, true));
  }

  public void testEquals() throws Exception {
    RegexQuery query1 = new RegexQuery( newTerm("foo.*"));
    query1.setRegexImplementation(new JakartaRegexpCapabilities());

    RegexQuery query2 = new RegexQuery( newTerm("foo.*"));
    assertFalse(query1.equals(query2));
  }
  
  public void testJakartaCaseSensativeFail() throws Exception {
    assertEquals(0, regexQueryNrHits("^.*DOG.*$", null));
  }

  public void testJavaUtilCaseSensativeFail() throws Exception {
    assertEquals(0, regexQueryNrHits("^.*DOG.*$", null));
  }
  
  public void testJakartaCaseInsensative() throws Exception {
    assertEquals(1, regexQueryNrHits("^.*DOG.*$", new JakartaRegexpCapabilities(JakartaRegexpCapabilities.FLAG_MATCH_CASEINDEPENDENT)));
  }
  
  public void testJavaUtilCaseInsensative() throws Exception {
    assertEquals(1, regexQueryNrHits("^.*DOG.*$", new JavaUtilRegexCapabilities(JavaUtilRegexCapabilities.FLAG_CASE_INSENSITIVE)));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3204.java