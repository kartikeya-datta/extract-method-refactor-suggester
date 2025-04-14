error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3228.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3228.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3228.java
text:
```scala
public abstract v@@oid build(SolrCore core, SolrIndexSearcher searcher) throws IOException;

package org.apache.solr.spelling;
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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.search.spell.LevensteinDistance;
import org.apache.lucene.search.spell.StringDistance;
import org.apache.lucene.search.spell.SuggestWord;
import org.apache.lucene.search.spell.SuggestWordQueue;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.common.params.SpellingParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.component.SpellCheckMergeData;
import org.apache.solr.schema.FieldType;
import org.apache.solr.search.SolrIndexSearcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * Refer to <a href="http://wiki.apache.org/solr/SpellCheckComponent">SpellCheckComponent</a>
 * for more details.
 * </p>
 * 
 * @since solr 1.3
 */
public abstract class SolrSpellChecker {
  public static final String DICTIONARY_NAME = "name";
  public static final String DEFAULT_DICTIONARY_NAME = "default";
  public static final String FIELD = "field";
  public static final String FIELD_TYPE = "fieldType";
  /** Dictionary name */
  protected String name;
  protected Analyzer analyzer;
  protected String field;
  protected String fieldTypeName;

  public String init(NamedList config, SolrCore core) {
    name = (String) config.get(DICTIONARY_NAME);
    if (name == null) {
      name = DEFAULT_DICTIONARY_NAME;
    }
    field = (String)config.get(FIELD);
    if (field != null && core.getSchema().getFieldTypeNoEx(field) != null)  {
      analyzer = core.getSchema().getFieldType(field).getQueryAnalyzer();
    }
    fieldTypeName = (String) config.get(FIELD_TYPE);
    if (core.getSchema().getFieldTypes().containsKey(fieldTypeName))  {
      FieldType fieldType = core.getSchema().getFieldTypes().get(fieldTypeName);
      analyzer = fieldType.getQueryAnalyzer();
    }
    if (analyzer == null)   {
      analyzer = new WhitespaceAnalyzer(core.getSolrConfig().luceneMatchVersion);
    }
    return name;
  }
  /**
   * Integrate spelling suggestions from the various shards in a distributed environment.
   * 
   * @param mergeData
   * @param numSug
   * @param count
   * @param extendedResults
   */
  public SpellingResult mergeSuggestions(SpellCheckMergeData mergeData, int numSug, int count, boolean extendedResults) {
    float min = 0.5f;
    try {
      min = getAccuracy();
    } catch(UnsupportedOperationException uoe) {
      //just use .5 as a default
    }
    
    StringDistance sd = null;
    try {
      sd = getStringDistance() == null ? new LevensteinDistance() : getStringDistance();    
    } catch(UnsupportedOperationException uoe) {
      sd = new LevensteinDistance();
    }
    
    SpellingResult result = new SpellingResult();
    for (Map.Entry<String, HashSet<String>> entry : mergeData.origVsSuggested.entrySet()) {
      String original = entry.getKey();
      
      //Only use this suggestion if all shards reported it as misspelled.
      Integer numShards = mergeData.origVsShards.get(original);
      if(numShards<mergeData.totalNumberShardResponses) {
        continue;
      }
      
      HashSet<String> suggested = entry.getValue();
      SuggestWordQueue sugQueue = new SuggestWordQueue(numSug);
      for (String suggestion : suggested) {
        SuggestWord sug = mergeData.suggestedVsWord.get(suggestion);
        sug.score = sd.getDistance(original, sug.string);
        if (sug.score < min) continue;
        sugQueue.insertWithOverflow(sug);
        if (sugQueue.size() == numSug) {
          // if queue full, maintain the minScore score
          min = sugQueue.top().score;
        }
      }

      // create token
      SpellCheckResponse.Suggestion suggestion = mergeData.origVsSuggestion.get(original);
      Token token = new Token(original, suggestion.getStartOffset(), suggestion.getEndOffset());

      // get top 'count' suggestions out of 'sugQueue.size()' candidates
      SuggestWord[] suggestions = new SuggestWord[Math.min(count, sugQueue.size())];
      // skip the first sugQueue.size() - count elements
      for (int k=0; k < sugQueue.size() - count; k++) sugQueue.pop();
      // now collect the top 'count' responses
      for (int k = Math.min(count, sugQueue.size()) - 1; k >= 0; k--)  {
        suggestions[k] = sugQueue.pop();
      }

      if (extendedResults) {
        Integer o = mergeData.origVsFreq.get(original);
        if (o != null) result.addFrequency(token, o);
        for (SuggestWord word : suggestions)
          result.add(token, word.string, word.freq);
      } else {
        List<String> words = new ArrayList<String>(sugQueue.size());
        for (SuggestWord word : suggestions) words.add(word.string);
        result.add(token, words);
      }
    }
    return result;
  }
  
  public Analyzer getQueryAnalyzer() {
    return analyzer;
  }

  public String getDictionaryName() {
    return name;
  }

  /**
   * Reloads the index.  Useful if an external process is responsible for building the spell checker.
   *
   * @throws java.io.IOException
   */
  public abstract void reload(SolrCore core, SolrIndexSearcher searcher) throws IOException;

  /**
   * (re)Builds the spelling index.  May be a NOOP if the implementation doesn't require building, or can't be rebuilt.
   */
  public abstract void build(SolrCore core, SolrIndexSearcher searcher);
  
  /**
   * Get the value of {@link SpellingParams#SPELLCHECK_ACCURACY} if supported.  
   * Otherwise throws UnsupportedOperationException.
   */
  protected float getAccuracy() {
    throw new UnsupportedOperationException();
  }
  
  /**
   * Get the distance implementation used by this spellchecker, or NULL if not applicable.
   */
  protected StringDistance getStringDistance()  {
    throw new UnsupportedOperationException();
  }


  /**
   * Get suggestions for the given query.  Tokenizes the query using a field appropriate Analyzer.
   * The {@link SpellingResult#getSuggestions()} suggestions must be ordered by best suggestion first.
   * <p/>
   *
   * @param options The {@link SpellingOptions} to use
   * @return The {@link SpellingResult} suggestions
   * @throws IOException if there is an error producing suggestions
   */
  public abstract SpellingResult getSuggestions(SpellingOptions options) throws IOException;
  
  public boolean isSuggestionsMayOverlap() {
    return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3228.java