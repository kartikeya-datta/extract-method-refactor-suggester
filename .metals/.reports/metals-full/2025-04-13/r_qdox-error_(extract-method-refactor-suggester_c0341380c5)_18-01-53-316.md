error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/142.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/142.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,3]

error in qdox parser
file content:
```java
offset: 3
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/142.java
text:
```scala
: F@@loat.valueOf((String)config.get(THRESHOLD_TOKEN_FREQUENCY));

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

package org.apache.solr.spelling.suggest;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.spelling.SolrSpellChecker;
import org.apache.solr.spelling.SpellingOptions;
import org.apache.solr.spelling.SpellingResult;
import org.apache.solr.spelling.suggest.Lookup.LookupResult;
import org.apache.solr.spelling.suggest.jaspell.JaspellLookup;
import org.apache.solr.util.HighFrequencyDictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Suggester extends SolrSpellChecker {
  private static final Logger LOG = LoggerFactory.getLogger(Suggester.class);
  
  /** Location of the source data - either a path to a file, or null for the
   * current IndexReader.
   */
  public static final String LOCATION = "sourceLocation";
  /** Field to use as the source of terms if using IndexReader. */
  public static final String FIELD = "field";
  /** Fully-qualified class of the {@link Lookup} implementation. */
  public static final String LOOKUP_IMPL = "lookupImpl";
  /**
   * Minimum frequency of terms to consider when building the dictionary.
   */
  public static final String THRESHOLD_TOKEN_FREQUENCY = "threshold";
  /**
   * Name of the location where to persist the dictionary. If this location
   * is relative then the data will be stored under the core's dataDir. If this
   * is null the storing will be disabled.
   */
  public static final String STORE_DIR = "storeDir";
  
  protected String sourceLocation;
  protected File storeDir;
  protected String field;
  protected float threshold;
  protected Dictionary dictionary;
  protected IndexReader reader;
  protected Lookup lookup;
  protected String lookupImpl;
  protected SolrCore core;
  
  @Override
  public String init(NamedList config, SolrCore core) {
    LOG.info("init: " + config);
    String name = super.init(config, core);
    threshold = config.get(THRESHOLD_TOKEN_FREQUENCY) == null ? 0.0f
            : (Float) config.get(THRESHOLD_TOKEN_FREQUENCY);
    sourceLocation = (String) config.get(LOCATION);
    field = (String)config.get(FIELD);
    lookupImpl = (String)config.get(LOOKUP_IMPL);
    if (lookupImpl == null) {
      lookupImpl = JaspellLookup.class.getName();
    }
    String store = (String)config.get(STORE_DIR);
    if (store != null) {
      storeDir = new File(store);
      if (!storeDir.isAbsolute()) {
        storeDir = new File(core.getDataDir() + File.separator + storeDir);
      }
      if (!storeDir.exists()) {
        storeDir.mkdirs();
      }
    }
    return name;
  }
  
  @Override
  public void build(SolrCore core, SolrIndexSearcher searcher) {
    LOG.info("build()");
    if (sourceLocation == null) {
      reader = searcher.getReader();
      dictionary = new HighFrequencyDictionary(reader, field, threshold);
    } else {
      try {
        dictionary = new FileDictionary(new InputStreamReader(
                core.getResourceLoader().openResource(sourceLocation), "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    lookup = (Lookup) core.getResourceLoader().newInstance(lookupImpl);
    try {
      lookup.build(dictionary);
      if (storeDir != null) {
        lookup.store(storeDir);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void reload(SolrCore core, SolrIndexSearcher searcher) throws IOException {
    LOG.info("reload()");
    if (dictionary == null && storeDir != null) {
      // this may be a firstSearcher event, try loading it
      if (lookup.load(storeDir)) {
        return;  // loaded ok
      }
    }
    // dictionary based on the current index may need refreshing
    if (dictionary instanceof HighFrequencyDictionary) {
      reader = reader.reopen();
      dictionary = new HighFrequencyDictionary(reader, field, threshold);
      try {
        lookup.build(dictionary);
        if (storeDir != null) {
          lookup.store(storeDir);
        }
      } catch (Exception e) {
        throw new IOException(e.toString());
      }
    }
  }

  public void add(String query, int numHits) {
    LOG.info("add " + query + ", " + numHits);
    lookup.add(query, new Integer(numHits));
  }
  
  static SpellingResult EMPTY_RESULT = new SpellingResult();


  @Override
  public SpellingResult getSuggestions(Collection<Token> tokens, IndexReader reader, int count, boolean onlyMorePopular, boolean extendedResults) throws IOException {
    return getSuggestions(new SpellingOptions(tokens, reader, count, onlyMorePopular, extendedResults, Float.MIN_VALUE, null));
  }

  @Override
  public SpellingResult getSuggestions(SpellingOptions options) throws IOException {
    LOG.debug("getSuggestions: " + options.tokens);
    if (lookup == null) {
      LOG.info("Lookup is null - invoke spellchecker.build first");
      return EMPTY_RESULT;
    }
    SpellingResult res = new SpellingResult();
    for (Token t : options.tokens) {
      String term = new String(t.buffer(), 0, t.length());
      List<LookupResult> suggestions = lookup.lookup(term,
          options.onlyMorePopular, options.count);
      if (suggestions == null) {
        continue;
      }
      for (LookupResult lr : suggestions) {
        res.add(t, lr.key, ((Number)lr.value).intValue());
      }
    }
    return res;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/142.java