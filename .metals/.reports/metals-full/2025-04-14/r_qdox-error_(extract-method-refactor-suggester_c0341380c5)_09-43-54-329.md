error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/384.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/384.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/384.java
text:
```scala
public static v@@oid afterClass() {

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
package org.apache.solr.spelling;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.JaroWinklerDistance;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.spell.StringDistance;
import org.apache.lucene.search.spell.SuggestMode;
import org.apache.lucene.search.spell.SuggestWord;
import org.apache.lucene.search.spell.SuggestWordFrequencyComparator;
import org.apache.lucene.store.Directory;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.component.SpellCheckComponent;
import org.apache.solr.util.RefCounted;
import org.apache.solr.search.SolrIndexSearcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

/**
 * @since solr 1.3
 */
public class IndexBasedSpellCheckerTest extends SolrTestCaseJ4 {
  protected static SpellingQueryConverter queryConverter;

  protected static String[] DOCS = new String[]{
          "This is a title",
          "The quick reb fox jumped over the lazy brown dogs.",
          "This is a document",
          "another document",
          "red fox",
          "green bun",
          "green bud"
  };


  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig.xml","schema.xml");
    //Index something with a title
    for (int i = 0; i < DOCS.length; i++) {
      assertNull(h.validateUpdate(adoc("id", String.valueOf(i), "title", DOCS[i])));
    }
    assertNull(h.validateUpdate(commit()));
    queryConverter = new SimpleQueryConverter();
  }
  
  @AfterClass
  public static void afterClass() throws Exception {
    queryConverter = null;
  }

  @Test
  public void testComparator() throws Exception {
    SpellCheckComponent component = (SpellCheckComponent) h.getCore().getSearchComponent("spellcheck");
    assertNotNull(component);
    AbstractLuceneSpellChecker spellChecker;
    Comparator<SuggestWord> comp;
    spellChecker = (AbstractLuceneSpellChecker) component.getSpellChecker("freq");
    assertNotNull(spellChecker);
    comp = spellChecker.getSpellChecker().getComparator();
    assertNotNull(comp);
    assertTrue(comp instanceof SuggestWordFrequencyComparator);

    spellChecker = (AbstractLuceneSpellChecker) component.getSpellChecker("fqcn");
    assertNotNull(spellChecker);
    comp = spellChecker.getSpellChecker().getComparator();
    assertNotNull(comp);
    assertTrue(comp instanceof SampleComparator);


  }

  @Test
  public void testSpelling() throws Exception {
    IndexBasedSpellChecker checker = new IndexBasedSpellChecker();

    NamedList spellchecker = new NamedList();
    spellchecker.add("classname", IndexBasedSpellChecker.class.getName());

    File indexDir = new File(TEMP_DIR, "spellingIdx" + new Date().getTime());
    indexDir.mkdirs();
    spellchecker.add(AbstractLuceneSpellChecker.INDEX_DIR, indexDir.getAbsolutePath());
    spellchecker.add(AbstractLuceneSpellChecker.FIELD, "title");
    spellchecker.add(AbstractLuceneSpellChecker.SPELLCHECKER_ARG_NAME, spellchecker);
    SolrCore core = h.getCore();

    String dictName = checker.init(spellchecker, core);
    assertTrue(dictName + " is not equal to " + SolrSpellChecker.DEFAULT_DICTIONARY_NAME,
            dictName.equals(SolrSpellChecker.DEFAULT_DICTIONARY_NAME) == true);
    RefCounted<SolrIndexSearcher> holder = core.getSearcher();
    SolrIndexSearcher searcher = holder.get();
    try {
    checker.build(core, searcher);

    IndexReader reader = searcher.getIndexReader();
    Collection<Token> tokens = queryConverter.convert("documemt");
    SpellingOptions spellOpts = new SpellingOptions(tokens, reader);
    SpellingResult result = checker.getSuggestions(spellOpts);
    assertTrue("result is null and it shouldn't be", result != null);
    //should be lowercased, b/c we are using a lowercasing analyzer
    Map<String, Integer> suggestions = result.get(spellOpts.tokens.iterator().next());
    assertTrue("documemt is null and it shouldn't be", suggestions != null);
    assertTrue("documemt Size: " + suggestions.size() + " is not: " + 1, suggestions.size() == 1);
    Map.Entry<String, Integer> entry = suggestions.entrySet().iterator().next();
    assertTrue(entry.getKey() + " is not equal to " + "document", entry.getKey().equals("document") == true);
    assertTrue(entry.getValue() + " does not equal: " + SpellingResult.NO_FREQUENCY_INFO, entry.getValue() == SpellingResult.NO_FREQUENCY_INFO);

    //test something not in the spell checker
    spellOpts.tokens = queryConverter.convert("super");
    result = checker.getSuggestions(spellOpts);
    assertTrue("result is null and it shouldn't be", result != null);
    suggestions = result.get(spellOpts.tokens.iterator().next());
    assertTrue("suggestions size should be 0", suggestions.size()==0);

    //test something that is spelled correctly
    spellOpts.tokens = queryConverter.convert("document");
    result = checker.getSuggestions(spellOpts);
    assertTrue("result is null and it shouldn't be", result != null);
    suggestions = result.get(spellOpts.tokens.iterator().next());
    assertTrue("suggestions is null and it shouldn't be", suggestions == null);

    //Has multiple possibilities, but the exact exists, so that should be returned
    spellOpts.tokens = queryConverter.convert("red");
    spellOpts.count = 2;
    result = checker.getSuggestions(spellOpts);
    assertNotNull(result);
    suggestions = result.get(spellOpts.tokens.iterator().next());
    assertTrue("suggestions is not null and it should be", suggestions == null);

    //Try out something which should have multiple suggestions
    spellOpts.tokens = queryConverter.convert("bug");
    result = checker.getSuggestions(spellOpts);
    assertNotNull(result);
    suggestions = result.get(spellOpts.tokens.iterator().next());
    assertNotNull(suggestions);
    assertTrue("suggestions Size: " + suggestions.size() + " is not: " + 2, suggestions.size() == 2);

    entry = suggestions.entrySet().iterator().next();
    assertTrue(entry.getKey() + " is equal to " + "bug and it shouldn't be", entry.getKey().equals("bug") == false);
    assertTrue(entry.getValue() + " does not equal: " + SpellingResult.NO_FREQUENCY_INFO, entry.getValue() == SpellingResult.NO_FREQUENCY_INFO);

    entry = suggestions.entrySet().iterator().next();
    assertTrue(entry.getKey() + " is equal to " + "bug and it shouldn't be", entry.getKey().equals("bug") == false);
    assertTrue(entry.getValue() + " does not equal: " + SpellingResult.NO_FREQUENCY_INFO, entry.getValue() == SpellingResult.NO_FREQUENCY_INFO);
    } finally {
      holder.decref();
    }
  }

  @Test
  public void testExtendedResults() throws Exception {
    IndexBasedSpellChecker checker = new IndexBasedSpellChecker();
    NamedList spellchecker = new NamedList();
    spellchecker.add("classname", IndexBasedSpellChecker.class.getName());

    File indexDir = new File(TEMP_DIR, "spellingIdx" + new Date().getTime());
    indexDir.mkdirs();
    spellchecker.add(AbstractLuceneSpellChecker.INDEX_DIR, indexDir.getAbsolutePath());
    spellchecker.add(AbstractLuceneSpellChecker.FIELD, "title");
    spellchecker.add(AbstractLuceneSpellChecker.SPELLCHECKER_ARG_NAME, spellchecker);
    SolrCore core = h.getCore();
    String dictName = checker.init(spellchecker, core);
    assertTrue(dictName + " is not equal to " + SolrSpellChecker.DEFAULT_DICTIONARY_NAME,
            dictName.equals(SolrSpellChecker.DEFAULT_DICTIONARY_NAME) == true);
    RefCounted<SolrIndexSearcher> holder = core.getSearcher();
    SolrIndexSearcher searcher = holder.get();
    try {
    checker.build(core, searcher);

    IndexReader reader = searcher.getIndexReader();
    Collection<Token> tokens = queryConverter.convert("documemt");
    SpellingOptions spellOpts = new SpellingOptions(tokens, reader, 1, SuggestMode.SUGGEST_WHEN_NOT_IN_INDEX, true, 0.5f, null);
    SpellingResult result = checker.getSuggestions(spellOpts);
    assertTrue("result is null and it shouldn't be", result != null);
    //should be lowercased, b/c we are using a lowercasing analyzer
    Map<String, Integer> suggestions = result.get(spellOpts.tokens.iterator().next());
    assertTrue("documemt is null and it shouldn't be", suggestions != null);
    assertTrue("documemt Size: " + suggestions.size() + " is not: " + 1, suggestions.size() == 1);
    Map.Entry<String, Integer> entry = suggestions.entrySet().iterator().next();
    assertTrue(entry.getKey() + " is not equal to " + "document", entry.getKey().equals("document") == true);
    assertTrue(entry.getValue() + " does not equal: " + 2, entry.getValue() == 2);

    //test something not in the spell checker
    spellOpts.tokens = queryConverter.convert("super");
    result = checker.getSuggestions(spellOpts);
    assertTrue("result is null and it shouldn't be", result != null);
    suggestions = result.get(spellOpts.tokens.iterator().next());
    assertTrue("suggestions size should be 0", suggestions.size()==0);

    spellOpts.tokens = queryConverter.convert("document");
    result = checker.getSuggestions(spellOpts);
    assertTrue("result is null and it shouldn't be", result != null);
    suggestions = result.get(spellOpts.tokens.iterator().next());
    assertTrue("suggestions is not null and it should be", suggestions == null);
    } finally {
      holder.decref();
    }
  }

  private class TestSpellChecker extends IndexBasedSpellChecker{
    @Override
    public SpellChecker getSpellChecker(){
      return spellChecker;
    }
  }

  @Test
  public void testAlternateDistance() throws Exception {
    TestSpellChecker checker = new TestSpellChecker();
    NamedList spellchecker = new NamedList();
    spellchecker.add("classname", IndexBasedSpellChecker.class.getName());

    File indexDir = new File(TEMP_DIR, "spellingIdx" + new Date().getTime());
    indexDir.mkdirs();
    spellchecker.add(AbstractLuceneSpellChecker.INDEX_DIR, indexDir.getAbsolutePath());
    spellchecker.add(AbstractLuceneSpellChecker.FIELD, "title");
    spellchecker.add(AbstractLuceneSpellChecker.SPELLCHECKER_ARG_NAME, spellchecker);
    spellchecker.add(AbstractLuceneSpellChecker.STRING_DISTANCE, JaroWinklerDistance.class.getName());
    SolrCore core = h.getCore();
    String dictName = checker.init(spellchecker, core);
    assertTrue(dictName + " is not equal to " + SolrSpellChecker.DEFAULT_DICTIONARY_NAME,
            dictName.equals(SolrSpellChecker.DEFAULT_DICTIONARY_NAME) == true);
    RefCounted<SolrIndexSearcher> holder = core.getSearcher();
    SolrIndexSearcher searcher = holder.get();
    try {
    checker.build(core, searcher);
    SpellChecker sc = checker.getSpellChecker();
    assertTrue("sc is null and it shouldn't be", sc != null);
    StringDistance sd = sc.getStringDistance();
    assertTrue("sd is null and it shouldn't be", sd != null);
    assertTrue("sd is not an instance of " + JaroWinklerDistance.class.getName(), sd instanceof JaroWinklerDistance);
    } finally {
      holder.decref();
    }
  }

  @Test
  public void testAlternateLocation() throws Exception {
    String[] ALT_DOCS = new String[]{
            "jumpin jack flash",
            "Sargent Peppers Lonely Hearts Club Band",
            "Born to Run",
            "Thunder Road",
            "Londons Burning",
            "A Horse with No Name",
            "Sweet Caroline"
    };

    IndexBasedSpellChecker checker = new IndexBasedSpellChecker();
    NamedList spellchecker = new NamedList();
    spellchecker.add("classname", IndexBasedSpellChecker.class.getName());

    File indexDir = new File(TEMP_DIR, "spellingIdx" + new Date().getTime());
    //create a standalone index
    File altIndexDir = new File(TEMP_DIR, "alternateIdx" + new Date().getTime());
    Directory dir = newFSDirectory(altIndexDir);
    IndexWriter iw = new IndexWriter(
        dir,
        new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT))
    );
    for (int i = 0; i < ALT_DOCS.length; i++) {
      Document doc = new Document();
      doc.add(new TextField("title", ALT_DOCS[i], Field.Store.YES));
      iw.addDocument(doc);
    }
    iw.forceMerge(1);
    iw.close();
    dir.close();
    indexDir.mkdirs();
    spellchecker.add(AbstractLuceneSpellChecker.INDEX_DIR, indexDir.getAbsolutePath());
    spellchecker.add(AbstractLuceneSpellChecker.LOCATION, altIndexDir.getAbsolutePath());
    spellchecker.add(AbstractLuceneSpellChecker.FIELD, "title");
    spellchecker.add(AbstractLuceneSpellChecker.SPELLCHECKER_ARG_NAME, spellchecker);
    SolrCore core = h.getCore();
    String dictName = checker.init(spellchecker, core);
    assertTrue(dictName + " is not equal to " + SolrSpellChecker.DEFAULT_DICTIONARY_NAME,
            dictName.equals(SolrSpellChecker.DEFAULT_DICTIONARY_NAME) == true);
    RefCounted<SolrIndexSearcher> holder = core.getSearcher();
    SolrIndexSearcher searcher = holder.get();
    try {
    checker.build(core, searcher);

    IndexReader reader = searcher.getIndexReader();
    Collection<Token> tokens = queryConverter.convert("flesh");
    SpellingOptions spellOpts = new SpellingOptions(tokens, reader, 1, SuggestMode.SUGGEST_WHEN_NOT_IN_INDEX, true, 0.5f, null);
    SpellingResult result = checker.getSuggestions(spellOpts);
    assertTrue("result is null and it shouldn't be", result != null);
    //should be lowercased, b/c we are using a lowercasing analyzer
    Map<String, Integer> suggestions = result.get(spellOpts.tokens.iterator().next());
    assertTrue("flesh is null and it shouldn't be", suggestions != null);
    assertTrue("flesh Size: " + suggestions.size() + " is not: " + 1, suggestions.size() == 1);
    Map.Entry<String, Integer> entry = suggestions.entrySet().iterator().next();
    assertTrue(entry.getKey() + " is not equal to " + "flash", entry.getKey().equals("flash") == true);
    assertTrue(entry.getValue() + " does not equal: " + 1, entry.getValue() == 1);

    //test something not in the spell checker
    spellOpts.tokens = queryConverter.convert("super");
    result = checker.getSuggestions(spellOpts);
    assertTrue("result is null and it shouldn't be", result != null);
    suggestions = result.get(spellOpts.tokens.iterator().next());
    assertTrue("suggestions size should be 0", suggestions.size()==0);

    spellOpts.tokens = queryConverter.convert("Caroline");
    result = checker.getSuggestions(spellOpts);
    assertTrue("result is null and it shouldn't be", result != null);
    suggestions = result.get(spellOpts.tokens.iterator().next());
    assertTrue("suggestions is not null and it should be", suggestions == null);
    } finally {
      holder.decref();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/384.java